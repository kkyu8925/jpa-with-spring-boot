package com.example.jpaapp.api;

import com.example.jpaapp.domain.Address;
import com.example.jpaapp.domain.Order;
import com.example.jpaapp.domain.OrderStatus;
import com.example.jpaapp.repository.order.OrderRepository;
import com.example.jpaapp.repository.order.query.OrderSimpleQueryDTO;
import com.example.jpaapp.repository.order.query.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * xToOne(ManyToOne, OneToOne) 관계 최적화
 * Order
 * Order -> Member (ManyToOne)
 * Order -> Delivery (OneToOne)
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리
     * - 양방향 관계 문제 발생 -> @JsonIgnore, 사용X!
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
        return orders;
    }

    /**
     * V2. 엔티티를 조회해서 DTO 변환(fetch join 사용X)
     * - 단점: 지연로딩으로 쿼리 N번 호출
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
//                .map(o -> new SimpleOrderDto(o))
                .map(SimpleOrderDto::new)
                .collect(toList());
    }

    /**
     * V3. 엔티티를 조회해서 DTO 변환(fetch join 사용O)
     * - fetch join 쿼리 1번 호출
     * 참고: fetch join 자세한 내용은 JPA 기본편 참고(정말 중요함)
     * 대부분의 경우 여기까지만 해도 좋은 성능이 나온다!
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
//                .map(o -> new SimpleOrderDto(o))
                .map(SimpleOrderDto::new)
                .collect(toList());
    }

    /**
     * V4. JPA에서 DTO 바로 조회
     * - 쿼리 1번 호출
     * - select 절에서 원하는 데이터만 선택해서 조회
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDTO> ordersV4() {
        return orderSimpleQueryRepository.findAllOrderDTO();
    }

    @Data
    static class SimpleOrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // Lazy 초기화
        }
    }
}
