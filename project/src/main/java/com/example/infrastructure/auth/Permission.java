package com.example.infrastructure.auth;

/**
 * APIクライアント認証：クライアントに対して許可されているHTTPメソッドとパスの組み合わせ（エンドポイント）を保持する
 */
public class Permission {
    private final String method;
    private final String path;

    public Permission(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
