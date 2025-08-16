package com.icbc.audit.input.exception;

public class SqlParseException extends BusinessException {
    public SqlParseException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SqlParseException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}