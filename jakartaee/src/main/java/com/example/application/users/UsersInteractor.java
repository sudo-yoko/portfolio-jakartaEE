package com.example.application.users;

import com.example.OffsetDateTimeUtils;
import com.example.domain.entities.User;
import com.example.infrastructure.clients.slack.SlackClientAsyncAdapter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@RequestScoped
public class UsersInteractor {
    @Inject
    private UsersServiceProxy proxy;
    @Inject
    private SlackClientAsyncAdapter slack;

    public UsersResponse getUser(String userId) {
        User user = proxy.findUser(userId);
        if (user == null) {
            throw new NotFoundException("ユーザー情報がありません。");
        }
        UsersResponse response = new UsersResponse();
        response.setUserId(user.getUserId());
        response.setUserName(user.getUserName());
        response.setTimestamp(OffsetDateTimeUtils.toJapanIsoString(user.getTimestamp()));
        response.setVersion(Long.toString(user.getVersion()));
        return response;
    }

    public void postUser(UsersRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserName(request.getUserName());
        proxy.createUser(user);
        slack.postMessageAsync(String.format("ユーザーが登録されました。[%s]", user.getUserId()));
    }

    public void putUser(UsersRequest request) {
        User user = new User();
        user.setUserId(request.getUserId());
        user.setUserName(request.getUserName());
        proxy.createOrReplaceUser(user);
    }

    public void deactivateUser(String userId) {
        proxy.deactivateUser(userId);
    }

    public void deleteUser(String userId) {
        proxy.deleteUser(userId);
    }
}
