package com.example;

import org.junit.jupiter.api.Test;

class DemoCipherTest {
    static final String LOG_PREFIX = "[TEST] " + DemoCipherTest.class.getSimpleName() + ": ";

    @Test
    void test() {
        String password = "password";
        System.out.println(LOG_PREFIX + "password=" + password);

        String encrypted = DemoCipher.encrypt(password);
        System.out.println(LOG_PREFIX + "encrypted=" + encrypted);

        String decrypted = DemoCipher.decrypt(encrypted);
        System.out.println(LOG_PREFIX + "decrypted=" + decrypted);
    }
}
