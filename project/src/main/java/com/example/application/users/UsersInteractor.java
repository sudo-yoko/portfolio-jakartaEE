package com.example.application.users;

import com.example.OffsetDateTimeUtils;
import com.example.Properties;
import com.example.domain.entities.User;
import com.example.domain.services.UserService;
import com.example.infrastructure.clients.SlackClient;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@RequestScoped
public class UsersInteractor {
    @Inject
    private UserService service;
    @Inject
    private SlackClient client;

    public UsersResponse getUser(String userId) {
        User user = service.findUser(userId);
        if (user == null) {
            throw new NotFoundException("ユーザー情報がありません。");
        }
        UsersResponse response = new UsersResponse();
        response.setUserId(user.getUserId());
        response.setUserName(user.getUserName());
        response.setTimestamp(OffsetDateTimeUtils.toJapanIsoString(user.getTimestamp()));
        return response;
    }

    public void postUser(UsersRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserName(request.getUserName());
        service.createUser(user);
        client.postMessage(Properties.get("slack.webhook.url"), String.format("ユーザーが登録されました。[%s]", user.getUserId()));
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
