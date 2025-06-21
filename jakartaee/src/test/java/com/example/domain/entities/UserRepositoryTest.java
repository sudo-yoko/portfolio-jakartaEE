package com.example.domain.entities;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.development.EntityManagerProvider;
import com.example.development.EntityManagerProvider.PersistenceUnitName;
import com.example.development.ReflectionUtils;

public class UserRepositoryTest {
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

    // mvn -Dtest=UserRepositoryTest#test_persist test -Duser.timezone=Asia/Tokyo
    @Test
    void test_persist() {
        String userId = "12345";
        String userName = "テスト 太郎";

        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        repository.persist(user);

        User result = repository.find(userId);
        System.out.println(">>> result -> " + result);
    }

}
