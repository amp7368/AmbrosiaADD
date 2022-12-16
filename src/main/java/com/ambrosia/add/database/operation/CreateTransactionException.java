package com.ambrosia.add.database.operation;

public class CreateTransactionException extends Exception {

    public CreateTransactionException() {
    }

    public CreateTransactionException(String message) {
        super(message);
    }

    public CreateTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateTransactionException(Throwable cause) {
        super(cause);
    }

    public CreateTransactionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
