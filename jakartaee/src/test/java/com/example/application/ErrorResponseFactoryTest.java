package com.example.application;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.ValidationErrorException;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;

public class ErrorResponseFactoryTest {
    private static final String LOG_PREFIX = ">>> [" + ErrorResponseFactoryTest.class.getSimpleName() + "]: ";

    private static Jsonb jsonb;

    @BeforeAll
    static void init() {
        JsonbConfig config = new JsonbConfig().withFormatting(true);
        jsonb = JsonbBuilder.create(config);
    }

    // mvn -Dtest=ErrorResponseFactoryTest#testBadRequestException_1 test
    @Test
    void testBadRequestException_1() {
        BadRequestException ex = new BadRequestException("不正なリクエストです。");
        Response response = ErrorResponseFactory.build(ex);
        System.out.println(LOG_PREFIX
                + String.format("status=%s, body=%s", response.getStatus(), jsonb.toJson(response.getEntity())));
    }

    // mvn -Dtest=ErrorResponseFactoryTest#testValidationErrorException_1 test
    @Test
    void testValidationErrorException_1() {
        ValidationErrorException ex = new ValidationErrorException();
        ex.addError("userId", "ユーザー名が不正です。");
        Response response = ErrorResponseFactory.build(ex);
        System.out.println(LOG_PREFIX
                + String.format("status=%s, body=%s", response.getStatus(), jsonb.toJson(response.getEntity())));
    }

    // mvn -Dtest=ErrorResponseFactoryTest#testValidationErrorException_2 test
    @Test
    void testValidationErrorException_2() {
        ValidationErrorException ex = new ValidationErrorException();
        ex.addError("userId", "ユーザーIDが不正です。");
        ex.addError("userName", "ユーザー名が不正です。");
        Response response = ErrorResponseFactory.build(ex);
        System.out.println(LOG_PREFIX
                + String.format("status=%s, body=%s", response.getStatus(), jsonb.toJson(response.getEntity())));
    }

    // mvn -Dtest=ErrorResponseFactoryTest#testOtherException_1 test
    @Test
    void testOtherException_1() {
        IllegalArgumentException ex = new IllegalArgumentException("不正な引数です。");
        Response response = ErrorResponseFactory.build(ex);
        System.out.println(LOG_PREFIX
                + String.format("status=%s, body=%s", response.getStatus(), jsonb.toJson(response.getEntity())));
    }

    // mvn -Dtest=ErrorResponseFactoryTest#testFindCause_1 test
    @Test
    void testFindCause_1() {
        // 例外の循環参照
        Exception ex1 = new Exception("例外１");
        Exception ex2 = new Exception("例外２", ex1);
        ex1.initCause(ex2); // 例外１の原因例外に、例外２を設定する

        Response response = ErrorResponseFactory.build(ex1);
        System.out.println(LOG_PREFIX
                + String.format("status=%s, body=%s", response.getStatus(), jsonb.toJson(response.getEntity())));
    }
}
