package com.example;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.MissingResourceException;

import org.junit.jupiter.api.Test;

class PropertiesTest {
    static final String LOG_PREFIX = "[TEST] " + PropertiesTest.class.getSimpleName() + ": ";

    // mvn -Dtest=PropertiesTest#testString test
    @Test
    void get() {
        String value = Properties.get("value.string");
        System.out.println(LOG_PREFIX + "result -> " + value);
    }

    // mvn -Dtest=PropertiesTest#getMissingResource test
    @Test
    void getMissingResource() {
        assertThrows(MissingResourceException.class, () -> Properties.get("value"));
    }

    // mvn -Dtest=PropertiesTest#getOrDefault test
    @Test
    void getOrDefault() {
        String value = Properties.getOrDefault("value", null);
        System.out.println(LOG_PREFIX + "result -> " + value);
    }

    // mvn -Dtest=PropertiesTest#getInteger test
    @Test
    void getInteger() {
        Integer value = Properties.getInteger("value.integer");
        System.out.println(LOG_PREFIX + "result -> " + value);
    }

    // mvn -Dtest=PropertiesTest#getIntegerMissingResource test
    @Test
    void getIntegerMissingResource() {
        assertThrows(MissingResourceException.class, () -> Properties.getInteger("value"));
    }

    // mvn -Dtest=PropertiesTest#getIntegerOrDefault test
    @Test
    void getIntegerOrDefault() {
        Integer value = Properties.getIntegerOrDefault("value", null);
        System.out.println(LOG_PREFIX + "result -> " + value);
    }

    // mvn -Dtest=PropertiesTest#getBoolean test
    @Test
    void getBoolean() {
        Boolean value = Properties.getBoolean("value.boolean");
        System.out.println(LOG_PREFIX + "result -> " + value);
    }

    // mvn -Dtest=PropertiesTest#getBooleanMissingResource test
    @Test
    void getBooleanMissingResource() {
        assertThrows(MissingResourceException.class, () -> Properties.getBoolean("value"));
    }

    // mvn -Dtest=PropertiesTest#getBooleanOrDefault test
    @Test
    void getBooleanOrDefault() {
        Boolean value = Properties.getBooleanOrDefault("value", true);
        System.out.println(LOG_PREFIX + "result -> " + value);
    }
}
