// 프록시 객체는 처음 사용할 때 한번만 초기화
// 프록시 객체를 초기화 할때, 프로식 객체가 실제 엔티티 클래스로 바뀌는 것이 아님.
// 초기화 되면 프록시 객체를 통해서 접근 가능

// 타입 비교는 ==으로 하면 안됨!! instance of 사용

// 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 getReference()를 호출해도 실제 엔티티 반환
// 영속성 컨텍스트에 프록시가 있으면 프록시 반환

// 프록시는 영속성 컨텍스트를 통하여 초기화한다 > 오류 조심
package 섹션8_프로시와_연관관계_관리;

import 섹션8_프로시와_연관관계_관리.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMainProxy {

    public static void main(String[] args) {
        // EntityManagerFactory 싱글톤으로 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // persistence.xml

        EntityManager entityManager = emf.createEntityManager();

        // jpa 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {

            Member m1 = new Member();
            m1.setUsername("aaa");
            entityManager.persist(m1);

            Member m2 = new Member();
            m2.setUsername("bb");
            entityManager.persist(m2);

            entityManager.flush();
            entityManager.clear();

//            Member findMember = entityManager.find(Member.class, member.getId());
//            System.out.println("findMember.getUsername() = " + findMember.getUsername());

//            Member findRefMember = entityManager.getReference(Member.class, m1.getId()); // DB 쿼리 안나감
//            System.out.println("findRefMember.getClass() = " + findRefMember.getClass()); // 프록시
//            System.out.println("findRefMember.getUsername() = " + findRefMember.getUsername()); // 쿼리 나감

//            Member findM1 = entityManager.find(Member.class, m1.getId());
//            Member findM2 = entityManager.find(Member.class, m2.getId());
//            System.out.println("findM1==findM2 : " + (findM1.getClass() == findM2.getClass())); // true

//            Member findM1 = entityManager.find(Member.class, m1.getId());
//            Member findM2 = entityManager.getReference(Member.class, m2.getId());
//            System.out.println("findM1==findM2 : " + (findM1.getClass() == findM2.getClass())); // false

            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}
