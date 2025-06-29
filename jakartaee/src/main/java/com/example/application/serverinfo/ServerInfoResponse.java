package com.example.application.serverinfo;

public class ServerInfoResponse {

    private String now;

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("now=").append(now);
        sb.append(" }");
        return sb.toString();
    }
}
