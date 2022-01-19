# 실전! 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화

- <a href="https://github.com/kkyu8925/jpa-with-spring-boot">홈</a>
- <a href="https://github.com/kkyu8925/jpa-with-spring-boot/blob/main/app-jpa/src/main/java/com/example/jpaapp/api/MemberApiController.java">
  API 개발 기본 - MemberApiController 소스코드</a>
- <a href="https://github.com/kkyu8925/jpa-with-spring-boot/blob/main/app-jpa/src/main/java/com/example/jpaapp/api/OrderSimpleApiController.java">
  API 개발 고급 : 지연 로딩과 조회 성능 최적화 - OrderSimpleApiController 소스코드</a>

```text
*권장 순서
- 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
- 필요하면 페이 조인으로 성능을 최적화한다 -> 대부분의 성능 이슈 해결
- 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
- 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다.
```

- <a href="https://github.com/kkyu8925/jpa-with-spring-boot/blob/main/app-jpa/src/main/java/com/example/jpaapp/api/OrderApiController.java">
  API 개발 고급 : 컬렉션 조회 최적화 - OrderApiController 소스코드</a>

```text
*권장 순서
- 엔티티 조회 방식
   - 페치 조인으로 쿼리 수를 최적화
   - 컬렉션 최적화
      - 페이징 필요시, hibernate.default_batch_fetch_size, @BatchSize으로 최적화
      - 페이징 필요 없을시, 페치 조인 사용
- 엔티티 조회 방식으로 해결이 안되면 DTO 조회 방식 사용
- DTO 조회 방식으로 해결이 안되면 NativeSQL of 스프링 JDBC Template

* 참고: 엔티티 조회방식은 페치조인이나, default_batch_fetch_size, @BatchSize 같이 코드를 거의 수정하지 않고, 옵션만 약간 변경해서 다양한 성능 최적화를 시도할 수 있다. 반면 DTO를 직접 조회하는 방식은 성능 최적화를 하거나 방식을 변경할 때 많은 코드를 변경해야 한다.
```
