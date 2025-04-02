package com.example;

import org.junit.jupiter.api.Test;

class DemoCipherTest {
    @Test
    void test() {
        String password = "password";
        System.out.println("password=" + password);

        String encrypted = DemoCipher.encrypt(password);
        System.out.println("encrypted=" + encrypted);

        String decrypted = DemoCipher.decrypt(encrypted);
        System.out.println("decrypted=" + decrypted);
    }
}
