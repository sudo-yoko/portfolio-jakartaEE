package com.example.infrastructure.auth;

import java.util.List;

/**
 * APIクライアント認証：認証結果を格納する
 */
public class AuthResult {
    private final List<Permission> permissions;
    private final boolean isExpired;

    public AuthResult(List<Permission> permissions, boolean isExpired) {
        this.permissions = List.copyOf(permissions);
        this.isExpired = isExpired;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public boolean isExpired() {
        return isExpired;
    }
}
