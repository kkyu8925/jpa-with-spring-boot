- <a href="https://github.com/kkyu8925/jpa-with-spring-boot">홈</a>

### JPQL(Java persistence Query language)

- 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
- SQL을 추상화해서 특정 DB SQL에 의존X
- JPQL을 한마디로 정의하면 객체 지향 SQL

### JPQL 문법

- select m from Member as m where m.age > 18
- 엔티티와 속성은 대소문자 구분 O (Member, age)
- JPQL 키워드는 대소문자 구분 X (select, from, where)
- 엔티티 이름 사용, 테이블 이름이 아님 (Member)
- 별칭은 필수(m) (as 생략가능)
- select m from Member m where m.age > 18

### TypeQuery, Query

- TypeQuery : 반환 타입이 명확할 때 사용

```text
TypedQuery<Member> query =
em.createQuery("SELECT m FROM Member m", Member.class);
```

- Query : 반환 타입이 명확하지 않을 때 사용

```text
Query query =
em.createQuery("SELECT m.username, m.age from Member m"); 
```

### 결과 조회 API

- query.getResultList(): 결과가 하나 이상일 때, 리스트 반환
    - 결과가 없으면 빈 리스트 반환
- query.getSingleResult(): 결과가 정확히 하나, 단일 객체 반환
    - 결과가 없으면: javax.persistence.NoResultException
    - 둘 이상이면: javax.persistence.NonUniqueResultException

### 파라미터 바인딩 - 이름 기준

```text
SELECT m FROM Member m where m.username=:username
query.setParameter("username", usernameParam);
```

### 프로젝션

- SELECT 절에 조회할 대상을 지정하는 것
- 프로젝션 대상: 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자등 기본 데이터 타입)
- SELECT m FROM Member m -> 엔티티 프로젝션
- SELECT m.team FROM Member m -> 엔티티 프로젝션
- SELECT m.address FROM Member m -> 임베디드 타입 프로젝션
- SELECT m.username, m.age FROM Member m -> 스칼라 타입 프로젝션
- DISTINCT 중복 제거

### 프로젝션 - 여러 값 조회

- SELECT m.username, m.age FROM Member m
- 1, Query 타입으로 조회
- 2, Object[] 타입으로 조회
- 3, new 명령어로 조회
    - 단순 값을 DTO로 바로 조회
    - `SELECT new jpabook.jpql.UserDTO(m.username, m.age) FROM Member m`
    - 패키지 명을 포함한 전체 클래스 명 입력
    - 순서와 타입이 일치하는 생성자 필요

### 페이징 API

- JPA는 페이징을 다음 두 API로 추상화
- setFirstResult(int startPosition) : 조회 시작 위치(0부터 시작)
- setMaxResults(int maxResult) : 조회할 데이터 수
- 예시
  ![image](https://user-images.githubusercontent.com/64997245/149725545-3761332f-3854-4ffe-b236-a56c2a393dd8.png)

### 조인

- 내부 조인

```text
SELECT m FROM Member m [INNER] JOIN m.team t
```

- 외부 조인

```text
SELECT m FROM Member m LEFT [OUTER] JOIN m.team t
```

- 세타 조인

```text
select count(m) from Member m, Team t where m.username = t.name
```