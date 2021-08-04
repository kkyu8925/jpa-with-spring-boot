package session7;

import session5.Member;
import session5.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // EntityManagerFactory 싱글톤으로 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); // persistence.xml

        EntityManager entityManager = emf.createEntityManager();

        // jpa 모든 데이터 변경은 트랜잭션 안에서 실행
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {

            Movie movie = new Movie();
            movie.setDirector("aa");
            movie.setActor("bb");
            movie.setName("ccc");
            movie.setPrice(10000);

            entityManager.persist(movie);

            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
        } finally {
            entityManager.close();
        }

        emf.close();
    }
}
