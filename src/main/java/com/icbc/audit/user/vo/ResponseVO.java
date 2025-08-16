package com.icbc.audit.user.vo;

import lombok.Data;

@Data
public class ResponseVO<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ResponseVO<T> success(T data) {
        ResponseVO<T> vo = new ResponseVO<>();
        vo.setCode(0);
        vo.setMessage("success");
        vo.setData(data);
        return vo;
    }

    public static <T> ResponseVO<T> fail(String message) {
        ResponseVO<T> vo = new ResponseVO<>();
        vo.setCode(-1);
        vo.setMessage(message);
        vo.setData(null);
        return vo;
    }
}
