package com.example.application.users;

import com.example.domain.EjbExceptionTranslator;
import com.example.domain.entities.user.User;
import com.example.domain.services.users.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class UsersServiceProxy {

    @Inject
    private UserService service;

    public User findUser(String userId) {
        try {
            return service.findUser(userId);
        } catch (Exception e) {
            String errorDetail = String.format("ユーザーID=%s", userId);
            throw EjbExceptionTranslator.translate(e, errorDetail);
        }
    }

    public void createUser(User user) {
        try {
            service.createUser(user);
        } catch (Exception e) {
            String errorDetail = String.format("ユーザーID=%s", user.getUserId());
            throw EjbExceptionTranslator.translate(e, errorDetail);
        }
    }

    public void createOrReplaceUser(User user) {
        try {
            service.createOrReplaceUser(user);
        } catch (Exception e) {
            String errorDetail = String.format("ユーザーID=%s", user.getUserId());
            throw EjbExceptionTranslator.translate(e, errorDetail);
        }
    }

    public void deactivateUser(String userId) {
        try {
            service.deactivateUser(userId);
        } catch (Exception e) {
            String errorDetail = String.format("ユーザーID=%s", userId);
            throw EjbExceptionTranslator.translate(e, errorDetail);
        }
    }

    public void deleteUser(String userId) {
        try {
            service.deleteUser(userId);
        } catch (Exception e) {
            String errorDetail = String.format("ユーザーID=%s", userId);
            throw EjbExceptionTranslator.translate(e, errorDetail);
        }
    }
}
