package com.example.infrastructure.auth;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;

/**
 * APIクライアント認証：認証結果をリクエストスコープに保持する
 */
@RequestScoped
public class AuthInfo {
    private AuthResult authResult = new AuthResult(List.of(), true);

    public void setAuthResult(AuthResult authResult) {
        this.authResult = authResult != null ? authResult : this.authResult;
    }

    public boolean hasPermission(String method, String path) {
        return authResult.getPermissions().stream()
                .anyMatch(p -> p.getMethod().equalsIgnoreCase(method) && path.startsWith(p.getPath()));
    }
}
