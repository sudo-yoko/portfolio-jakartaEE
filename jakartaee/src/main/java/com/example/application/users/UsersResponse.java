package com.example.application.users;

public class UsersResponse {

    private String userId;
    private String userName;
    private String timestamp;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("userId=").append(userId).append(", ");
        sb.append("userName=").append(userName).append(", ");
        sb.append("timestamp=").append(timestamp);
        sb.append(" }");
        return sb.toString();
    }
}
