package com.example;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.MissingResourceException;

import org.junit.jupiter.api.Test;

class ApplicationPropertiesTest {
    static final String LOG_PREFIX = "[TEST] " + ApplicationPropertiesTest.class.getSimpleName() + ": ";

    // mvn -Dtest=PropertiesTest#testString test
    @Test
    void get() {
        String value = ApplicationProperties.get("value.string");
        System.out.println(LOG_PREFIX + "result -> " + value);
    }

    // mvn -Dtest=PropertiesTest#getMissingResource test
    @Test
    void getMissingResource() {
        assertThrows(MissingResourceException.class, () -> ApplicationProperties.get("value"));
    }

    // mvn -Dtest=PropertiesTest#getOrDefault test
    @Test
    void getOrDefault() {
        String value = ApplicationProperties.getOrDefault("value", null);
        System.out.println(LOG_PREFIX + "result -> " + value);
    }

    // mvn -Dtest=PropertiesTest#getInteger test
    @Test
    void getInteger() {
        Integer value = ApplicationProperties.getInt("value.integer");
        System.out.println(LOG_PREFIX + "result -> " + value);
    }

    // mvn -Dtest=PropertiesTest#getIntegerMissingResource test
    @Test
    void getIntegerMissingResource() {
        assertThrows(MissingResourceException.class, () -> ApplicationProperties.getInt("value"));
    }

    // mvn -Dtest=PropertiesTest#getIntegerOrDefault test
    // @Test
    // void getIntegerOrDefault() {
    // Integer value = Properties.getIntOrDefault("value", null);
    // System.out.println(LOG_PREFIX + "result -> " + value);
    // }

    // mvn -Dtest=PropertiesTest#getBoolean test
    @Test
    void getBoolean() {
        Boolean value = ApplicationProperties.getBoolean("value.boolean");
        System.out.println(LOG_PREFIX + "result -> " + value);
    }

    // mvn -Dtest=PropertiesTest#getBooleanMissingResource test
    @Test
    void getBooleanMissingResource() {
        assertThrows(MissingResourceException.class, () -> ApplicationProperties.getBoolean("value"));
    }

    // mvn -Dtest=PropertiesTest#getBooleanOrDefault test
    @Test
    void getBooleanOrDefault() {
        Boolean value = ApplicationProperties.getBooleanOrDefault("value", true);
        System.out.println(LOG_PREFIX + "result -> " + value);
    }
}
