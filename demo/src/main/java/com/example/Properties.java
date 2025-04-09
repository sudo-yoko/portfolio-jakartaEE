package com.example;

import java.util.ResourceBundle;
import java.util.function.Function;

public class Properties {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("demo");

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        if (bundle.containsKey(key)) {
            return get(key);
        }
        return defaultValue;
    }

    public static Integer getInteger(String key) {
        return get(key, Integer::parseInt);
    }

    public static Integer getIntegerOrDefault(String key, Integer defaultValue) {
        return getOrDefault(key, Integer::parseInt, defaultValue);
    }

    public static Boolean getBoolean(String key) {
        return get(key, Boolean::parseBoolean);
    }

    public static Boolean getBooleanOrDefault(String key, Boolean defaultValue) {
        return getOrDefault(key, Boolean::parseBoolean, defaultValue);
    }

    private static <T> T get(String key, Function<String, T> converter) {
        String value = bundle.getString(key);
        return converter.apply(value);
    }

    private static <T> T getOrDefault(String key, Function<String, T> converter, T defaultValue) {
        if (bundle.containsKey(key)) {
            return get(key, converter);
        }
        return defaultValue;
    }
}
