package com.example.domain.entities.user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.time.ZoneId;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import com.example.ApplicationClock;
import com.example.development.EntityManagerProvider;
import com.example.development.EntityManagerProvider.PersistenceUnitName;
import com.example.development.ReflectionUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.OptimisticLockException;

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
        executeInTransaction((repo) -> {
            String userName = "テスト 太郎";
            User user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            repo.persist(user);
        });

        UserRepository repo1 = createUserRepository();
        UserRepository repo2 = createUserRepository();

        EntityManagerProvider prv1 = new EntityManagerProvider();
        EntityManagerProvider prv2 = new EntityManagerProvider();

        EntityManager em1 = prv1.getEntityManager(PersistenceUnitName.DEV_PU1);
        EntityManager em2 = prv2.getEntityManager(PersistenceUnitName.DEV_PU1);

        ReflectionUtils.setFieldValue(repo1, "em", em1);
        ReflectionUtils.setFieldValue(repo2, "em", em2);

        EntityTransaction tran1 = em1.getTransaction();
        EntityTransaction tran2 = em2.getTransaction();

        tran1.begin();
        tran2.begin();

        User user1 = repo1.find(userId);
        user1.setUserName("test taro1");
        repo1.merge(user1);

        User user2 = repo2.find(userId);
        user2.setUserName("test taro2");
        repo2.merge(user2);

        tran2.commit();

        Exception e = assertThrows(Exception.class, () -> tran1.commit());
        e.printStackTrace();
        // Exception ex = EJBExceptionTranslator.translate(e, "エラー詳細");
        // ex.printStackTrace();
        assertTrue(isCausedBy(e, OptimisticLockException.class), "楽観ロックエラーが発生していないためテスト失敗");

        prv1.close();
        prv2.close();

        executeNonTransaction((repo) -> {
            User result = repo.find(userId);
            System.out.println(">>> result -> " + result);
        });
    }

    void executeInTransaction(Consumer<UserRepository> action) {
        UserRepository repo = createUserRepository();
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

    void executeNonTransaction(Consumer<UserRepository> action) {
        UserRepository repo = createUserRepository();
        EntityManagerProvider prv = new EntityManagerProvider();
        ReflectionUtils.setFieldValue(repo, "em", prv.getEntityManager(PersistenceUnitName.DEV_PU1));
        action.accept(repo);
        prv.close();
    }

    /**
     * 依存注入済みの UserRepository インスタンスを返す
     */
    UserRepository createUserRepository() {
        UserRepository repo = new UserRepository();
        ApplicationClock clock = new ApplicationClock();
        ReflectionUtils.setFieldValue(clock, "clock", Clock.system(ZoneId.of("Asia/Tokyo")));
        ReflectionUtils.setFieldValue(repo, "clock", clock);
        return repo;
    }

    private static boolean isCausedBy(Throwable t, Class<? extends Throwable> exceptionClass) {
        final int MAX_DEPTH = 10;

        Throwable cause = t;
        for (int i = 0; i < MAX_DEPTH; i++) {
            if (cause == null) {
                return false;
            }
            if (exceptionClass.isInstance(cause)) {
                return true;
            }
            cause = cause.getCause();
        }
        System.out.println(">>> 原因例外の取得で、ループの最大回数を超えました。");
        return false;
    }
}
