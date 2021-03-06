package 섹션5_연관관계_매핑_기초_섹션6_다양한_연관관계_매핑.domain;

import javax.persistence.*;

// final 클래스, enum, interface, inner 클래스 사용X
@Entity // JPA 가 객체를 관리
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
