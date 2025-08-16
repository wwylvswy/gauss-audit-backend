package com.icbc.audit.input.exception;

public enum ErrorCode {
    SQL_SYNTAX_ERROR("SQL001", "SQL语法错误"),
    FILE_TOO_LARGE("FILE001", "文件大小超出限制"),
    UNSUPPORTED_FILE_TYPE("FILE002", "不支持的文件类型"),
    FILE_EMPTY("FILE003", "文件内容为空"),
    DB_CONNECTION_FAILED("DB001", "数据库连接失败"),
    INVALID_CONNECTION_PARAMS("DB002", "无效的数据库连接参数"),
    ENCODING_DETECTION_FAILED("FILE004", "文件编码检测失败");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
