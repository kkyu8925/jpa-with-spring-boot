// 프록시 객체는 처음 사용할 때 한번만 초기화
// 프록시 객체를 초기화 할때, 프로식 객체가 실제 엔티티 클래스로 바뀌는 것이 아님.
// 초기화 되면 프록시 객체를 통해서 접근 가능

// 타입 비교는 ==으로 하면 안됨!! instance of 사용

// 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 getReference()를 호출해도 실제 엔티티 반환
// 영속성 컨텍스트에 프록시가 있으면 프록시 반환

// 프록시는 영속성 컨텍스트를 통하여 초기화한다 > 오류 조심
package 섹션9_값타입;

import 섹션9_값타입.domain.Address;
import 섹션9_값타입.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        // EntityManagerFactory 싱글톤으로 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // persistence.xml

        EntityManager entityManager = emf.createEntityManager();

        // jpa 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {

            Member member = new Member();
            member.setUsername("aaa");
            member.setHomeAddress(new Address("city", "street", "zipcode"));

            member.getFavoriteFoods().add("라면");
            member.getFavoriteFoods().add("족발");
            member.getFavoriteFoods().add("치킨");

            entityManager.persist(member);

            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}
