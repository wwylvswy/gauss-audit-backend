package com.icbc.audit.assit.vo;

import lombok.Data;
// 对应文档 5.4.2 (表5-5) 的 API接口响应数据字段 用于标准化所有后端API的返回格式
@Data
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }
}

