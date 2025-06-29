package com.example.domain.entities.user;

import java.time.Clock;
import java.time.ZoneId;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import com.example.ApplicationClock;
import com.example.development.EntityManagerProvider;
import com.example.development.EntityManagerProvider.PersistenceUnitName;
import com.example.development.ReflectionUtils;
import com.example.domain.EjbExceptionTranslator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * 楽観ロックエラーを発生させるテスト
 */
public class UserRepositoryTest2 {

    // mvn -Dtest=UserRepositoryTest2#test test -Duser.timezone=Asia/Tokyo
    // mvn -Dtest=UserRepositoryTest2#test test
    @Test
    void test() {
        String userId = "12345";

        // 初期データ投入
        Consumer<UserRepository> action = (repo) -> {
            String userName = "テスト 太郎";
            User user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            repo.persist(user);
        };
        executeInTransaction(action);

        UserRepository repo1 = new UserRepository();
        UserRepository repo2 = new UserRepository();

        EntityManagerProvider prv1 = new EntityManagerProvider();
        EntityManagerProvider prv2 = new EntityManagerProvider();

        ReflectionUtils.setFieldValue(repo1, "em", prv1.getEntityManager(PersistenceUnitName.DEV_PU1));
        ReflectionUtils.setFieldValue(repo2, "em", prv2.getEntityManager(PersistenceUnitName.DEV_PU1));

        EntityManager em1 = ReflectionUtils.getFieldValue(repo1, "em", EntityManager.class);
        EntityManager em2 = ReflectionUtils.getFieldValue(repo2, "em", EntityManager.class);

        inject(repo1);
        inject(repo2);

        EntityTransaction tran1 = em1.getTransaction();
        EntityTransaction tran2 = em2.getTransaction();

        try {
            tran1.begin();
            tran2.begin();

            User user1 = repo1.find(userId);
            user1.setUserName("test taro1");
            repo1.merge(user1);

            User user2 = repo2.find(userId);
            user2.setUserName("test taro2");
            repo2.merge(user2);

            tran2.commit();
            tran1.commit();

        } catch (Exception e) {
            if (tran1.isActive()) {
                tran1.rollback();
            }
            if (tran2.isActive()) {
                tran2.rollback();
            }
            Exception ex = EjbExceptionTranslator.translate(e, "エラー詳細");
            ex.printStackTrace();

        } finally {
            executeNoTransaction((repository) -> {
                User result = repository.find(userId);
                System.out.println(">>> result -> " + result);
            });
        }
    }

    void executeInTransaction(Consumer<UserRepository> action) {
        UserRepository repo = new UserRepository();
        inject(repo);
        EntityManagerProvider prv = new EntityManagerProvider();
        EntityManager em = prv.getEntityManager(PersistenceUnitName.DEV_PU1);
        ReflectionUtils.setFieldValue(repo, "em", prv.getEntityManager(PersistenceUnitName.DEV_PU1));
        EntityTransaction tran = em.getTransaction();
        try {
            tran.begin();
            action.accept(repo);
            tran.commit();
        } catch (Exception e) {
            if (tran.isActive()) {
                tran.rollback();
            }
            e.printStackTrace();
        } finally {
            prv.close();
        }
    }

    void executeNoTransaction(Consumer<UserRepository> action) {
        EntityManagerProvider prv = new EntityManagerProvider();
        UserRepository repo = new UserRepository();
        inject(repo);
        ReflectionUtils.setFieldValue(repo, "em", prv.getEntityManager(PersistenceUnitName.DEV_PU1));
        action.accept(repo);
        prv.close();
    }

    void inject(UserRepository repo) {
        ApplicationClock clock = new ApplicationClock();
        ReflectionUtils.setFieldValue(clock, "clock", Clock.system(ZoneId.of("Asia/Tokyo")));
        ReflectionUtils.setFieldValue(repo, "clock", clock);
    }
}
