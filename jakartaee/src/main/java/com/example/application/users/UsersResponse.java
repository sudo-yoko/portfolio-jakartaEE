package com.example.application.users;

public class UsersResponse {

    private String userId;
    private String userName;
    private String timestamp;
    private String version;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("userId=").append(userId).append(", ");
        sb.append("userName=").append(userName).append(", ");
        sb.append("timestamp=").append(timestamp).append(", ");
        sb.append("version=").append(version);
        sb.append(" }");
        return sb.toString();
    }
}
