package 섹션3_영속성관리_섹션4_엔티티매핑;

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
//            // 저장
//            Member member = new Member();
//            member.setId(1L);
//            member.setName("hello");
//
//            entityManager.persist(member);

//            // 수정
//            Member findMember = entityManager.find(Member.class, 1L); // pk
//            findMember.setName("updateName");

            // 비영속 상태
            Member member = new Member(100L, "Hello");

            // 영속 상태 - entityManager 의 영속성 컨텍스트에 저장함(1차 캐시)
            // DB에 저장되지 않음, commit(flush) 에서 저장
            System.out.println("--- BEFORE ---");
            entityManager.persist(member);
            System.out.println("--- AFTER ---");

            // 영속 상태이기 때문에 쿼리로 찾아오지 않고 영속성 컨텍스트에서 찾아온다.(1차 캐시)
            // 영속 컨텍스트에서 pk 로 조회 후, 없으면 DB 에서 가져와서 영속성 컨텍스트에 등록.
            Member findMember1 = entityManager.find(Member.class, 100L);
            Member findMember2 = entityManager.find(Member.class, 100L);

            // 영속 엔티티의 동일성 보장
            System.out.println("result = " + (findMember1 == findMember2)); // true

            // 트랙잭션 커밋 시 DB 로 쿼리 전송
            // flush 호출 - 커밋이나 쿼리(JPQL)를 실행할 때 호출 됨
            // flush 는 영속성 컨텍스트를 비우지 않음. 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}
