package com.example.jpaapp.repository.order.simplequery;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager entityManager;

    public List<OrderSimpleQueryDTO> findAllOrderDTO() {
        return entityManager.createQuery(
                "select new com.example.jpaapp.repository.order.simplequery.OrderSimpleQueryDTO(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderSimpleQueryDTO.class)
                .getResultList();
    }
}
