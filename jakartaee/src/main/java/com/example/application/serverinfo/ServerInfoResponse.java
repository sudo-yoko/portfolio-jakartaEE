package com.example.application.serverinfo;

public class ServerInfoResponse {

    private String serverTime;

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("serverTime=").append(serverTime);
        sb.append(" }");
        return sb.toString();
    }
}
