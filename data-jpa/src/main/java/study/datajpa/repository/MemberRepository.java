package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 어노테이션 생략해도 동작
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // 오류 시 애플리케이션 로딩시 에러
    // @NamedQuery 똑같이 동작
    @Query("select m from Member m where m.username= :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
