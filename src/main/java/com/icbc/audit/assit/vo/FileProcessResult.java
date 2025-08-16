package com.icbc.audit.assit.vo;

/**
 * 文件处理结果封装类
 *
 * 功能说明：
 * 1. 存储文件处理后的元数据信息
 * 2. 包含文件字符集检测结果
 * 3. 记录处理状态(成功/失败)
 * 4. 保存文件内容文本
 * 5. 提供获取安全内容的接口
 */

public class FileProcessResult {
    public enum ProcessStatus { SUCCESS, FAILURE }

    private String fileName;    // 文件名
    private long fileSize;    // 文件大小
    private String charset;    // 文件字符编码
    private ProcessStatus status;    // 处理状态(枚举)
    private String content;    // 文件内容文本
    private String errorMessage;    // 错误信息(处理失败时)

    // Getter & Setter
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public String getCharset() { return charset; }
    public void setCharset(String charset) { this.charset = charset; }
    public ProcessStatus getStatus() { return status; }
    public void setStatus(ProcessStatus status) { this.status = status; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}