- <a href="https://github.com/kkyu8925/jpa-with-spring-boot">홈</a>

### 페치 조인(fetch join)

- SQL 조인 종류X
- JPQL에서 성능 최적화를 위해 제공하는 기능
- 연관된 엔티티나 컬렉션을 SQL 한 번에 함께 조회하는 기능
- join fetch 명령어 사용
- 페치 조인 ::= [ LEFT [OUTER] | INNER ] JOIN FETCH 조인경로

### 엔티티 페치 조인

- 회원을 조회하면서 연관된 팀도 함께 조회(SQL 한 번에)
- SQL을 보면 회원 뿐만 아니라 팀(T.*)도 함께 SELECT
- [JPQL]
    ```text
    select m from Member m join fetch m.team
    ```
- [SQL]
    ```text
    SELECT M.*, T.* FROM MEMBER M
    INNER JOIN TEAM T ON M.TEAM_ID=T.ID
    ```

### 페치 조인과 일반 조인의 차이

- 일반 조인 실행시 연관된 엔티티를 함께 조회하지 않음
- [JPQL]
  ```text
  select t
  from Team t join t.members m
  where t.name = ‘팀A'
  ```
- [SQL]
  ```text
  SELECT T.*
  FROM TEAM T
  INNER JOIN MEMBER M ON T.ID=M.TEAM_ID
  WHERE T.NAME = '팀A'
  ```
- JPQL은 결과를 반환할 때 연관관계 고려X
- 단지 SELECT 절에 지정한 엔티티만 조회할 뿐
- 여기서는 팀 엔티티만 조회하고, 회원 엔티티는 조회X
- 페치 조인을 사용할 때만 연관된 엔티티도 함께 조회(즉시 로딩)
- 페치 조인은 객체 그래프를 SQL 한번에 조회하는 개념

### 페치 조인의 특징과 한계

- 페치 조인 대상에는 별칭을 줄 수 없다.
    - 하이버네이트는 가능, 가급적 사용X
- 둘 이상의 컬렉션은 페치 조인 할 수 없다.
- 컬렉션을 페치 조인하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 없다.
    - 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징 가능
    - 하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)
- 연관된 엔티티들을 SQL 한 번으로 조회 - 성능 최적화
- 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
    - @OneToMany(fetch = FetchType.LAZY) // 글로벌 로딩 전략
- 실무에서 글로벌 로딩 전략은 모두 지연 로딩
- 최적화가 필요한 곳은 페치 조인 적용

### 페치 조인 - 정리

- 모든 것을 페치 조인으로 해결할 수는 없음
- 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적
- 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면, 페치 조인 보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적

### Named 쿼리 - 정적 쿼리

- 미리 정의해서 이름을 부여해두고 사용하는 JPQL
- 정적 쿼리
- 어노테이션, XML에 정의
- 애플리케이션 로딩 시점에 초기화 후 재사용
- 애플리케이션 로딩 시점에 쿼리를 검증

### 벌크 연산

- 쿼리 한 번으로 여러 테이블 로우 변경(엔티티)
- executeUpdate()의 결과는 영향받은 엔티티 수 반환
- UPDATE, DELETE 지원
- INSERT(insert into .. select, 하이버네이트 지원)

```text
String qlString = "update Product p " +
 "set p.price = p.price * 1.1 " +
 "where p.stockAmount < :stockAmount";
 
int resultCount = em.createQuery(qlString)
 .setParameter("stockAmount", 10)
 .executeUpdate(); 
```

### 벌크 연산 주의

- 벌크 연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 쿼리
    - 벌크 연산을 먼저 실행
    - 벌크 연산 수행 후 영속성 컨텍스트 초기화