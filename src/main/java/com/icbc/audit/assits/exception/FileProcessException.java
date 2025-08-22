// gauss-audit-assistant/src/main/java/com/gauss/audit/exception/FileProcessException.java

// 1. 确保包路径正确
package com.icbc.audit.assits.exception;

// 2. BusinessException 的构造函数需要 ErrorCode，这里的 ErrorCode 必须是本包内的
public class FileProcessException extends BusinessException {
    public FileProcessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FileProcessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}