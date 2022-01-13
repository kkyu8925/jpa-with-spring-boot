// 가급적 지연 로딩만 사용(특히 실무에서) - FetchType.LAZY
// 즉시 로딩을 적용하면 예상하지 못한 SQL 발생
// 즉시 로딩은 JPQL 에서 N+1 문제를 일으킨다.
package 섹션8_프로시와_연관관계_관리;

import 섹션5_연관관계_매핑_기초_섹션6_다양한_연관관계_매핑.Locker;

import javax.persistence.*;

@Entity // JPA 가 객체를 관리
// final 클래스, enum, interface, inner 클래스 사용X
//@Table(name="MBA")
public class Member {
    //  저장할 필드에 final 사용X

    @Id // pk 매핑, 직접 할당 시 @Id 만 사용
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동 할당
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

//    @Column(name = "TEAM_ID")
//    private long teamId;

    // 연관관계 주인
    // 외래키 관리(등록, 수정)
    @ManyToOne(fetch = FetchType.LAZY) // 프록시 객체로 받음
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
