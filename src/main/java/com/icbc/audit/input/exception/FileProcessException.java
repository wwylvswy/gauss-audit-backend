package com.icbc.audit.input.exception;

public class FileProcessException extends BusinessException {
    public FileProcessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FileProcessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
