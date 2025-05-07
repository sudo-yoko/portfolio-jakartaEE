package com.example.domain.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(schema = "DEV", name = "USER_INFO")
@NamedQuery(name = "User.findActive", query = "SELECT e FROM User e WHERE e.userId=:userId AND e.deleted=false")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "DELETED")
    private boolean deleted;

    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    @PrePersist
    private void prePersist() {
        this.timestamp = now();
    }

    @PreUpdate
    private void preUpdate() {
        this.timestamp = now();
    }

    private LocalDateTime now() {
        // 現在時刻。秒以下の精度を切り捨て
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("userId=").append(userId).append(", ");
        sb.append("userName=").append(userName).append(", ");
        sb.append("deleted=").append(deleted).append(", ");
        sb.append("timestamp=").append(timestamp);
        sb.append("}");
        return sb.toString();
    }
}
