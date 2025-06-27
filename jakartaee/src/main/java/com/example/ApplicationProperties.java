package com.example;

import java.util.ResourceBundle;
import java.util.function.Function;

public class ApplicationProperties {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("application");

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        if (bundle.containsKey(key)) {
            return get(key);
        }
        return defaultValue;
    }

    public static int getInt(String key) {
        return get(key, Integer::parseInt);
    }

    public static int getIntOrDefault(String key, int defaultValue) {
        return getOrDefault(key, Integer::parseInt, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return get(key, Boolean::parseBoolean);
    }

    public static boolean getBooleanOrDefault(String key, boolean defaultValue) {
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
