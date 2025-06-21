package com.example.infrastructure.auth;

import jakarta.ws.rs.NotAuthorizedException;

/**
 * APIクライアント認証：認証認可の定義を読み込み、キャッシュする
 */
public interface AuthCache {
    AuthResult authenticate(String applicationName, String apiKey) throws NotAuthorizedException;
}
