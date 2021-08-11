package com.example.jpaapp.service;

import com.example.jpaapp.domain.Address;
import com.example.jpaapp.domain.Member;
import com.example.jpaapp.domain.Order;
import com.example.jpaapp.domain.OrderStatus;
import com.example.jpaapp.domain.item.Book;
import com.example.jpaapp.domain.item.Item;
import com.example.jpaapp.exception.NotEnoughStockException;
import com.example.jpaapp.repository.order.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();

        Book book = createBook("JPA", 10, 10000);

        int orderCnt = 2;
        int orderPrice = 10000;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCnt);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(orderPrice * orderCnt, getOrder.getTotalPrice(), "주문 가격은 가격*수량");
        assertEquals(8, book.getStock(), "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("JPA", 10, 10000);

        int orderCnt = 11;

        // when

        // then
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), item.getId(), orderCnt));
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("JPA", 10, 10000);

        int orderCnt = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCnt);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 상태 CANCEL");
        assertEquals(10, item.getStock(), "재고 수량 확인");
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        entityManager.persist(member);
        return member;
    }

    private Book createBook(String name, int stock, int price) {
        Book book = new Book();
        book.setName(name);
        book.setStock(stock);
        book.setPrice(price);
        entityManager.persist(book);
        return book;
    }
}