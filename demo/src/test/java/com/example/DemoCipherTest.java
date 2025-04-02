package com.example;

import org.junit.jupiter.api.Test;

class DemoCipherTest {

    @Test
    void test_encrypt() {

        String password = "password";
        String encrypted = DemoCipher.encrypt(password);

        System.out.println(encrypted);
        
    }

}
