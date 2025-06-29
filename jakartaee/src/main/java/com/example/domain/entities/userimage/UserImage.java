package com.example.domain.entities.userimage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(schema = "DEV", name = "USER_INFO")
public class UserImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    @Version
    @Column(name = "VERSION")
    private long version;

    @PrePersist
    private void prePersist() {
        this.timestamp = now();
    }

    @PreUpdate
    private void preUpdate() {
        this.timestamp = now();
    }

    private LocalDateTime now() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        sb.append("userId=").append(userId).append(", ");
        sb.append("image.length=").append(image.length).append(", ");
        sb.append("timestamp=").append(timestamp).append(", ");
        sb.append("version=").append(version);
        sb.append(" }");
        return sb.toString();
    }

}
