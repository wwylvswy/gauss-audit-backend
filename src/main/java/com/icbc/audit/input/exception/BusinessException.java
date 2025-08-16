package com.icbc.audit.input.exception;

/**
 * 业务异常基类 - 提供统一的异常处理框架
 * 核心功能：
 * 1. 封装错误代码和错误信息
 * 2. 支持多语言错误消息
 * 3. 记录异常发生时间
 * 4. 传递原始异常上下文
 * 5. 提供标准化序列化格式
 */

public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

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