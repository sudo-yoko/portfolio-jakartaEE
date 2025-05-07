package com.example.application.users;

public class UsersResponse {

    private String userId;
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("userId=").append(userId).append(", ");
        sb.append("userName=").append(userName);
        sb.append("}");
        return sb.toString();
    }
}
