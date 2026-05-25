package com.innowise.userservice.exception;

public class PaymentCardException extends RuntimeException {
    public PaymentCardException(String message) {
        super(message);
    }

    public PaymentCardException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentCardException(Throwable cause) {
        super(cause);
    }

    public PaymentCardException() {
    }
}
