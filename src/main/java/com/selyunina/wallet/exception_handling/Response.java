package com.selyunina.wallet.exception_handling;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

public class Response {

    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Map<String, String> errors;

    public Response(String message, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String message;

        private Map<String, String> errors;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder errors(Map<String, String> errors) {
            this.errors = errors;
            return this;
        }

        public Response build() {
            return new Response(message, errors);
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
