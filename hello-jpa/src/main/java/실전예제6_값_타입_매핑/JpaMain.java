package 실전예제6_값_타입_매핑;

import 실전예제6_값_타입_매핑.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        // EntityManagerFactory 싱글톤으로 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // persistence.xml

        EntityManager entityManager = emf.createEntityManager();

        // jpa 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {

            Book book = new Book();
            book.setAuthor("JPA");
            book.setAuthor("김영한");

            entityManager.persist(book);

            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}