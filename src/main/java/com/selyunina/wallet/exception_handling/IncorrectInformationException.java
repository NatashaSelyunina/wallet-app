package com.selyunina.wallet.exception_handling;

import org.springframework.http.HttpStatus;

public class IncorrectInformationException extends RuntimeException {

    private HttpStatus status;

    public IncorrectInformationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
