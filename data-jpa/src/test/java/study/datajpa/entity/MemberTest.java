package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() throws Exception {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);
        Team teamB = new Team("teamB");
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        em.persist(member1);
        Member member2 = new Member("member2", 20, teamA);
        em.persist(member2);
        Member member3 = new Member("member3", 30, teamB);
        em.persist(member3);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member4);

        // 초기화
        em.flush();
        em.clear();

        // when

        // then
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());
        }
    }
}