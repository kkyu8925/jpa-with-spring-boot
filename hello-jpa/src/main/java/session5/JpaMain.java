package session5;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // EntityManagerFactory 싱글톤으로 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // persistence.xml

        EntityManager entityManager = emf.createEntityManager();

        // jpa 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {

//            // 팀 저장
//            Team team = new Team();
//            team.setName("TeamA");
//            entityManager.persist(team);
//
//            // 회원 저장
//            Member member = new Member();
//            member.setUsername("member1");
//            member.setTeamId(team.getId()); // 외래키 식별자를 직접 다룸
//            entityManager.persist(member);
//
//            // 조회
//            Member findMember = entityManager.find(Member.class, member.getId());
//            // 연관관계가 없음
//            // 식별자로 다시 조회, 객체지향적이지 않다.
//            Team findTeam = entityManager.find(Team.class, team.getId());

            // 팀 저장
            Team team = new Team();
            team.setName("TeamA");
            entityManager.persist(team);

            // 회원 저장
            Member member = new Member();
            member.setUsername("member1");
            member.changeTeam(team); // 연관관계 주인만 입력 수정 가능!!
            entityManager.persist(member);

            // 순수 객체 상태를 고려해서 항상 양쪽에 값을 설정하자
            // Member.java setTeam() 에 설정함
//            team.getMembers().add(member);

            entityManager.flush();
            entityManager.clear();

            Member findMember = entityManager.find(Member.class, member.getId());

            Team findTeam = findMember.getTeam();
            System.out.println("findTeam.getName() = " + findTeam.getName());

            List<Member> members = findTeam.getMembers(); // 조회만 가능

            for (Member m : members) {
                System.out.println("m = " + m.getUsername());
            }

            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}
