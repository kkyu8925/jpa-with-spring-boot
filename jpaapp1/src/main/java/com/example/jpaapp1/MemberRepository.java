package com.example.jpaapp1;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    // EntityManager 주입
    @PersistenceContext
    private EntityManager entityManager;

    public Long save(Member member) {
        entityManager.persist(member);
        return member.getId();
    }

    public Member find(Long id) {
        return entityManager.find(Member.class, id);
    }
}
