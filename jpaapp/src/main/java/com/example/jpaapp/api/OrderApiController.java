package com.example.jpaapp.api;

import com.example.jpaapp.domain.Address;
import com.example.jpaapp.domain.Order;
import com.example.jpaapp.domain.OrderItem;
import com.example.jpaapp.domain.OrderStatus;
import com.example.jpaapp.repository.order.OrderRepository;
import com.example.jpaapp.repository.order.query.OrderFlatDto;
import com.example.jpaapp.repository.order.query.OrderItemQueryDto;
import com.example.jpaapp.repository.order.query.OrderQueryDto;
import com.example.jpaapp.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * 권장 순서
 * 1. 엔티티 조회 방식으로 우선 접근 v2
 * * 1-1. 페치조인으로 쿼리 수를 최적화
 * * 1-2. 컬렉션 최적화 v3, v3.1
 * * * 1-2-1. 페이징 필요 hibernate.default_batch_fetch_size , @BatchSize 로 최적화 v3.1
 * * * 1-2-2. 페이징 필요X 페치 조인 사용 v3
 * 2. 엔티티 조회 방식으로 해결이 안되면 DTO 조회 방식 사용
 * 3. DTO 조회 방식으로 해결이 안되면 NativeSQL or 스프링 JdbcTemplate
 */
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     * 문제점
     * - 엔티티가 변하면 API 스펙이 변한다.
     * - 트랜잭션 안에서 지연 로딩 필요
     * 해결
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll();
        for (Order order : all) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(o -> o.getItem().getName()); // Lazy 강제 초기화
        }
        return all;
    }

    /**
     * V2. 엔티티를 조회해서 DTO 변환(fetch join 사용X)
     * - 트랜잭션 안에서 지연 로딩 필요
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
//                .map(o -> new OrderDto(o))
                .map(OrderDto::new)
                .collect(toList());
    }

    /**
     * V3. 엔티티를 조회해서 DTO 변환(fetch join 사용O)
     * 단점
     * - 컬렉션 페치 조인은 하나만 사용하기!!
     * - 데이터 뻥튀기로 페이징 불가능!!! ( 부모 1, 자식 10 이면 10개로 늘어남)
     * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경 가능)
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        return orders.stream()
//                .map(o -> new OrderDto(o))
                .map(OrderDto::new)
                .collect(toList());
    }

    /**
     * V3.1 엔티티를 조회해서 DTO 변환 페이징 고려
     * - ToOne 관계만 우선 모두 페치 조인으로 최적화
     * - 컬렉션 관계는 application.yml hibernate.default_batch_fetch_size, @BatchSize 최적화
     * V3 처럼 하나의 쿼리로 가져오지는 않지만 성능+페이징이 가능하다!
     * 또한 쿼리는 조금더 나가지만 데이터베이스에서 애플리케이션으로 가져오는 데이터 전송량이 줄어든다. (뻥튀기 안일어남)
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        return orders.stream()
//                .map(o -> new OrderDto(o))
                .map(OrderDto::new)
                .collect(toList());
    }

    /**
     * V4. JPA에서 DTO 바로 조회, 컬렉션 N 조회 (1 + N Query)
     * - 페이징 가능
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    /**
     * V5. JPA에서 DTO 바로 조회, 컬렉션 1 조회 최적화 버전 (1 + 1 Query)
     * - 페이징 가능
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    /**
     * V6. JPA에서 DTO 바로 조회, 플랫 데이터(1Query) (1 Query)
     * - 페이징 불가능
     * - 뻥튀기
     */
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                                o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }

    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream()
//                    .map(orderItem -> new OrderItemDto(orderItem))
                    .map(OrderItemDto::new)
                    .collect(toList());
        }
    }

    @Data
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}
