package com.icbc.audit.input.exception;

public class DbConnectionException extends BusinessException {
    public DbConnectionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DbConnectionException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}