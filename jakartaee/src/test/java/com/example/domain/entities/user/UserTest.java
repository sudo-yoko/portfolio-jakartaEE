package com.example.domain.entities.user;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

import com.example.development.EntityManagerProvider;
import com.example.development.EntityManagerProvider.PersistenceUnitName;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class UserTest {

    // mvn -Dtest=UserTest#test1 test
    @Test
    void test1() {
        User target = new User();
        System.out.println(">>> result -> " + target);
    }

    // mvn -Dtest=UserTest#test2 test
    @Test
    void test2() {
        EntityManagerProvider provider = new EntityManagerProvider();
        EntityManager em = provider.getEntityManager(PersistenceUnitName.DEV_PU1);
        EntityTransaction transaction = em.getTransaction();

        String userId = "12345";
        String userName = "テスト 太郎";
        Clock clock = Clock.system(ZoneId.of("Asia/Tokyo"));
        // Clock clock = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"),
        // ZoneId.of("UTC"));
        LocalDateTime timestamp = LocalDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS);

        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setTimestamp(timestamp);

        transaction.begin();
        em.persist(user);
        transaction.commit();

        System.out.println(">>> result -> " + em.find(User.class, userId));
        provider.close();
    }
}
