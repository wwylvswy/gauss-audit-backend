// gauss-audit-assistant/src/main/java/com/gauss/audit/exception/BusinessException.java

// 1. 确保包路径正确
package com.icbc.audit.assits.exception;

public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    // 2. 确保这里的 ErrorCode 参数来自同一个包 (com.gauss.audit.exception)
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}