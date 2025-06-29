package com.example.domain.entities;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import com.example.development.EntityManagerProvider;
import com.example.development.EntityManagerProvider.PersistenceUnitName;
import com.example.domain.EjbExceptionTranslator;
import com.example.domain.entities.user.User;
import com.example.domain.entities.user.UserRepository;
import com.example.development.ReflectionUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * 楽観ロックエラーを発生させるテスト
 */
public class UserRepositoryTest2 {

    // mvn -Dtest=UserRepositoryTest2#test test -Duser.timezone=Asia/Tokyo
    @Test
    void test() {
        String userId = "12345";

        // 初期データ投入
        Consumer<UserRepository> action = (repository) -> {
            String userName = "テスト 太郎";
            User user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            repository.persist(user);
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
        EntityManagerProvider provider = new EntityManagerProvider();
        EntityManager em = provider.getEntityManager(PersistenceUnitName.DEV_PU1);
        UserRepository repository = new UserRepository();
        ReflectionUtils.setFieldValue(repository, "em", em);
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            action.accept(repository);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            provider.close();
        }
    }

    void executeNoTransaction(Consumer<UserRepository> action) {
        EntityManagerProvider provider = new EntityManagerProvider();
        UserRepository repository = new UserRepository();
        ReflectionUtils.setFieldValue(repository, "em", provider.getEntityManager(PersistenceUnitName.DEV_PU1));
        action.accept(repository);
        provider.close();
    }
}
