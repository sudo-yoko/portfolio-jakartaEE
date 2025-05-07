package com.example.application.users;

public class UsersRequest {

    private String userId;
    private String userName;
    private String properties;

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

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("userId=").append(userId).append(", ");
        sb.append("userName=").append(userName).append(", ");
        sb.append("properties=").append(properties);
        sb.append(" }");
        return sb.toString();
    }

}
