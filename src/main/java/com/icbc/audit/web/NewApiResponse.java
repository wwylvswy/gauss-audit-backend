package com.icbc.audit.web;

import lombok.Data;

@Data
public class NewApiResponse<T> {

    /** 业务码 */
    private int code;
    /** 状态 */
    private boolean success;
    /** 描述 */
    private String message;
    /** 数据 */
    private T data;

    private NewApiResponse(int code, boolean success, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public static <T> NewApiResponse<T> ok(T data) {
        return new NewApiResponse<>(ErrorCode.SUCCESS.getCode(), true, ErrorCode.SUCCESS.getMessage(), data);
    }

    public static <T> NewApiResponse<T> ok() {
        return new NewApiResponse<>(ErrorCode.SUCCESS.getCode(), true, ErrorCode.SUCCESS.getMessage(), null);
    }

    public static <T> NewApiResponse<T> ok(String message, T data) {
        return new NewApiResponse<>(ErrorCode.SUCCESS.getCode(), true, message, data);
    }

    public static <T> NewApiResponse<T> fail(ErrorCode code, String message) {
        return new NewApiResponse<>(code.getCode(), false, message, null);
    }
}
