package 섹션3_영속성관리_섹션4_엔티티매핑;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

//@Entity // JPA 가 객체를 관리
// final 클래스, enum, interface, inner 클래스 사용X
//@Table(name="MBA")
public class Member {
    //  저장할 필드에 final 사용X

    @Id // pk 매핑, 직접 할당 시 @Id 만 사용
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동 할당
    private Long id;

    @Column(name = "name")
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING) // enum 타입 매핑, ORDINAL 사용X!!
    private RoleType roleType;

    // 날짜 타입 옛날 버전
    @Temporal(TemporalType.TIMESTAMP) // 날짜 타입 매핑
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP) // 날짜 타입 매핑
    private Date lastModifiedDate;

    // 날짜 타입 요즘 버전
    private LocalDate testLocalDate; // date
    private LocalDateTime testLocalDateTime; // timestamp

    @Lob // 필드 길이 제한 없음
    private String description;

    @Transient // 필드 매핑X
    private String tmp;

    // jpa 기본 생성자 필수 (public, protected)
    public Member() {
    }

    public Member(Long id, String name) {
        this.id = id;
        this.username = name;
    }

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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LocalDate getTestLocalDate() {
        return testLocalDate;
    }

    public void setTestLocalDate(LocalDate testLocalDate) {
        this.testLocalDate = testLocalDate;
    }

    public LocalDateTime getTestLocalDateTime() {
        return testLocalDateTime;
    }

    public void setTestLocalDateTime(LocalDateTime testLocalDateTime) {
        this.testLocalDateTime = testLocalDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
