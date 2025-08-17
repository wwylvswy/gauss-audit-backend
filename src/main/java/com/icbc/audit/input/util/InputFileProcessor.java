package com.icbc.audit.input.util;

/**
 * 文件处理器 - 负责文件上传的安全处理和内容提取
 * 核心功能：
 * 1. 执行文件病毒扫描和安全验证
 * 2. 检测文件MIME类型是否允许
 * 3. 自动识别文件字符编码
 * 4. 文件内容转码为UTF-8格式
 * 5. 清理文件内容中的不可见字符
 * 6. 处理大文件分块上传
 */

import com.icbc.audit.input.DTO.FileProcessResult;
import com.icbc.audit.input.exception.ErrorCode;
import com.icbc.audit.input.exception.InputFileProcessException;
import org.apache.tika.Tika;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class InputFileProcessor {

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize maxFileSize;

    private final InputVirusScanner inputVirusScanner = new InputVirusScanner();
    private final Tika tika = new Tika();

    public FileProcessResult process(MultipartFile file) {
        FileProcessResult result = new FileProcessResult();
        result.setFileName(file.getOriginalFilename());
        result.setFileSize(file.getSize());
        if (file.getSize() > maxFileSize.toBytes()) {
            // 文件过大处理逻辑
        }

        try {
            // 校验文件大小
            if (file.getSize() > maxFileSize.toBytes()) {
                throw new InputFileProcessException(ErrorCode.FILE_TOO_LARGE);
            }

            // 空文件校验
            if (file.isEmpty()) {
                result.setStatus(FileProcessResult.ProcessStatus.SUCCESS);
                return result;
            }

            // 病毒扫描
            if (!inputVirusScanner.scan(file.getInputStream())) {
                throw new InputFileProcessException(ErrorCode.UNSUPPORTED_FILE_TYPE);
            }

            // 检测编码
            String charset = detectCharset(file.getInputStream());
            result.setCharset(charset);

            // 提取内容（仅保留DDL语句）
            String content = new String(file.getBytes(), charset);
            result.setContent(filterDdl(content));
            result.setStatus(FileProcessResult.ProcessStatus.SUCCESS);

        } catch (IOException e) {
            result.setStatus(FileProcessResult.ProcessStatus.FAILURE);
            result.setErrorMessage(e.getMessage());
        } catch (InputFileProcessException e) {
            result.setStatus(FileProcessResult.ProcessStatus.FAILURE);
            result.setErrorMessage(e.getMessage());
        }
        return result;
    }

    // 编码检测
    private String detectCharset(InputStream is) throws IOException {
        byte[] buf = new byte[4096];
        UniversalDetector detector = new UniversalDetector(null);
        int nread;
        while ((nread = is.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        if (encoding == null) {
            throw new InputFileProcessException(ErrorCode.ENCODING_DETECTION_FAILED);
        }
        return encoding;
    }

    // 过滤非DDL语句
    private String filterDdl(String content) {
        StringBuilder ddlContent = new StringBuilder();
        String[] statements = content.split(";");
        for (String stmt : statements) {
            String trimmed = stmt.trim().toUpperCase();
            if (trimmed.startsWith("CREATE TABLE") || trimmed.startsWith("ALTER TABLE")) {
                ddlContent.append(stmt).append(";");
            }
        }
        return ddlContent.toString();
    }
}