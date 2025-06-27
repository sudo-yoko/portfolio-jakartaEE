package com.example.domain.entities;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.development.EntityManagerProvider;
import com.example.development.EntityManagerProvider.PersistenceUnitName;
import com.example.development.ReflectionUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class UserRepositoryTest1 {
    static EntityManagerProvider provider;
    static UserRepository repository;

    @BeforeAll
    static void initialize() {
        provider = new EntityManagerProvider();
        repository = new UserRepository();
        ReflectionUtils.setFieldValue(repository, "em", provider.getEntityManager(PersistenceUnitName.DEV_PU1));
    }

    @AfterAll
    static void teardown() {
        provider.close();
    }

    // mvn -Dtest=UserRepositoryTest1#test test -Duser.timezone=Asia/Tokyo
    @Test
    void test() {
        String userId = "12345";
        String userName = "テスト 太郎";

        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);

        EntityManager em = ReflectionUtils.getFieldValue(repository, "em", EntityManager.class);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        repository.persist(user);
        transaction.commit();

        User result = repository.find(userId);
        System.out.println(">>> result -> " + result);
    }
}
