package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        // EntityManagerFactory 싱글톤으로 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");// persistence.xml

        EntityManager entityManager = emf.createEntityManager();

        // jpa 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
//            // 저장
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("hello");
//
//            entityManager.persist(member);

//            // 수정
//            Member findMember = entityManager.find(Member.class, 1L); // pk
//            findMember.setName("updateName");

            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}
