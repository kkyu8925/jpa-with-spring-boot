package session5;

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
    @ManyToOne
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

    // 연연관관계편의 메서드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
