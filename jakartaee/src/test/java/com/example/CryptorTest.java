package com.example;

import org.junit.jupiter.api.Test;

class CryptorTest {
    static final String LOG_PREFIX = "[TEST] " + CryptorTest.class.getSimpleName() + ": ";

    // mvn -Dtest=CryptorTest#test test
    @Test
    void test() {
        String password = "password";
        System.out.println(LOG_PREFIX + "password=" + password);

        String encrypted = Cryptor.encrypt(password);
        System.out.println(LOG_PREFIX + "encrypted=" + encrypted);

        String decrypted = Cryptor.decrypt(encrypted);
        System.out.println(LOG_PREFIX + "decrypted=" + decrypted);
    }
}
