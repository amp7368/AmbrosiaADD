package com.ambrosia.add.discord.active.account;

public class UpdateAccountException extends Exception {

    public UpdateAccountException() {
    }

    public UpdateAccountException(String message) {
        super(message);
    }

    public UpdateAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateAccountException(Throwable cause) {
        super(cause);
    }

    public UpdateAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
