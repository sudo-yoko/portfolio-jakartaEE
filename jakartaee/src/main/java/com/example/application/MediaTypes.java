package com.example.application;

import jakarta.ws.rs.core.MediaType;

public class MediaTypes {
    public static final String CHARSET_UTF_8 = "charset=UTF-8";
    public static final String APPLICATION_JSON_UTF_8 = MediaType.APPLICATION_JSON + "; " + CHARSET_UTF_8;
    public static final String TEXT_PLAIN_UTF_8 = MediaType.TEXT_PLAIN + "; " + CHARSET_UTF_8;
}
