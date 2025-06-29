package com.example.domain.entities.user;

import java.time.Clock;
import java.time.ZoneId;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.ApplicationClock;
import com.example.development.EntityManagerProvider;
import com.example.development.EntityManagerProvider.PersistenceUnitName;
import com.example.development.ReflectionUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class UserRepositoryTest1 {

    static EntityManagerProvider prv;
    static UserRepository repo;

    @BeforeEach
    void setup() {
        prv = new EntityManagerProvider();
        repo = new UserRepository();
        ReflectionUtils.setFieldValue(repo, "em", prv.getEntityManager(PersistenceUnitName.DEV_PU1));

        ApplicationClock clock = new ApplicationClock();
        ReflectionUtils.setFieldValue(clock, "clock", Clock.system(ZoneId.of("Asia/Tokyo")));
        ReflectionUtils.setFieldValue(repo, "clock", clock);
    }

    @AfterEach
    void cleanup() {
        prv.close();
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

        EntityManager em = ReflectionUtils.getFieldValue(repo, "em", EntityManager.class);
        EntityTransaction tran = em.getTransaction();

        tran.begin();
        repo.persist(user);
        tran.commit();

        User result = repo.find(userId);
        System.out.println(">>> result -> " + result);
    }
}
