package com.example.domain.entities;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.development.EntityManagerProvider;
import com.example.development.EntityManagerProvider.PersistenceUnitName;
import com.example.domain.entities.user.User;
import com.example.domain.entities.user.UserRepository;
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
    // mvn -Dtest=UserRepositoryTest1#test test
    @Test
    void test() {
        String userId = "12345";
        String userName = "テスト 太郎";

        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);

        // Clock clock = Clock.system(ZoneId.of("Asia/Tokyo")); // システム時刻(日本時間)
        //Clock clock = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneId.of("UTC")); // システム時刻(固定値)
        //ReflectionUtils.setFieldValue(user, "clock", clock);

        EntityManager em = ReflectionUtils.getFieldValue(repository, "em", EntityManager.class);
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        repository.persist(user);
        transaction.commit();

        User result = repository.find(userId);
        System.out.println(">>> result -> " + result);
    }
}
