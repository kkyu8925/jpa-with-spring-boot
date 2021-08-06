// 프록시 객체는 처음 사용할 때 한번만 초기화
// 프록시 객체를 초기화 할때, 프로식 객체가 실제 엔티티 클래스로 바뀌는 것이 아님.
// 초기화 되면 프록시 객체를 통해서 접근 가능

// 타입 비교는 ==으로 하면 안됨!! instance of 사용

// 영속성 컨텍스트에 찾는 엔티티가 이미 있으면 getReference()를 호출해도 실제 엔티티 반환
// 영속성 컨텍스트에 프록시가 있으면 프록시 반환

// 프록시는 영속성 컨텍스트를 통하여 초기화한다 > 오류 조심
package session8;

import javax.persistence.*;

public class JpaMainCASCADE {

    public static void main(String[] args) {
        // EntityManagerFactory 싱글톤으로 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // persistence.xml

        EntityManager entityManager = emf.createEntityManager();

        // jpa 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            entityManager.persist(parent);
            // cascade
//            entityManager.persist(child1);
//            entityManager.persist(child2);

            entityManager.flush();
            entityManager.clear();

            Parent findParent = entityManager.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0);

            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}