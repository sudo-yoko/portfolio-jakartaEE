package com.example;

import org.junit.jupiter.api.Test;

public class ValidationErrorExceptionTest {

    // mvn -Dtest=ValidationErrorExceptionTest#testConstructor_1 test
    @Test
    void testConstructor_1() {
        ValidationErrorException target = new ValidationErrorException();
        target.printStackTrace();
        System.out.println("details=" + target.getErrors());
    }

    // mvn -Dtest=ValidationErrorExceptionTest#testConstructor_2 test
    @Test
    void testConstructor_2() {
        ValidationErrorException target = new ValidationErrorException("userId", "userIdが不正です。");
        target.printStackTrace();
        System.out.println("details=" + target.getErrors());
    }

    // mvn -Dtest=ValidationErrorExceptionTest#testAddDetail_1 test
    @Test
    void testAddDetail_1() {
        ValidationErrorException target = new ValidationErrorException();
        target.addError("userId", "userIdが不正です。");
        target.printStackTrace();
        System.out.println("details=" + target.getErrors());
    }

    // mvn -Dtest=ValidationErrorExceptionTest#testAddDetail_2 test
    @Test
    void testAddDetail_2() {
        ValidationErrorException target = new ValidationErrorException();
        target.addError("userId", "userIdが不正です。");
        target.addError("userName", "userNameが不正です。");
        target.printStackTrace();
        System.out.println("details=" + target.getErrors());
    }
}
