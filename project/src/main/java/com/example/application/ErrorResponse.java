package com.example.application;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    private List<Error> errors;

    public ErrorResponse() {
        errors = new ArrayList<>();
    }

    public ErrorResponse(String type, String message) {
        this();
        addError(type, message);
    }

    public List<Error> getErrors() {
        return List.copyOf(errors);
    }

    public void addError(String type, String message) {
        errors.add(new Error(type, message));
    }

    public void addAllErrors(List<Error> errors) {
        errors.forEach(e -> addError(e.getType(), e.getMessage()));
    }

    public static class Error {
        private String type;
        private String message;

        public Error() {
        }

        public Error(String type, String message) {
            this.type = type;
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
