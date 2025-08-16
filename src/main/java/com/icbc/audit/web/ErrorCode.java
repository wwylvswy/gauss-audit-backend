package com.icbc.audit.web;

import lombok.Getter;

/**
 * 错误码枚举。
 */
@Getter
public enum ErrorCode {
    SUCCESS(200, "成功"),
    INVALID_PARAM(401, "参数错误"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(405, "系统异常");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
