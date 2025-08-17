package com.icbc.audit.input.exception;

public class InputFileProcessException extends BusinessException {

    public InputFileProcessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InputFileProcessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
