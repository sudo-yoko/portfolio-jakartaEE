package com.example.application.users;

import com.example.domain.entities.User;
import com.example.domain.services.UserService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class UsersInteractor {

    @Inject
    private UserService service;

    public UsersResponse getUser(String userId) {
        User user = service.findUser(userId);

        UsersResponse response = new UsersResponse();
        response.setUserId(user.getUserId());
        response.setUserName(user.getUserName());
        return response;
    }

    public void postUser(UsersRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserName(request.getUserName());
        service.createUser(user);
    }

    public void putUser(UsersRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserName(request.getUserName());
        service.createOrReplaceUser(user);
    }

    public void deleteUser(String userId) {
        service.deactivateUser(userId);
    }
}
