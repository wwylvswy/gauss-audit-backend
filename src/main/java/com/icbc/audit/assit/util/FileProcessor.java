package com.icbc.audit.assit.util;

import com.icbc.audit.assit.exception.ErrorCode;
import com.icbc.audit.assit.exception.FileProcessException;
import com.icbc.audit.assit.vo.FileProcessResult;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FileProcessor {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSizeValue;

    private final VirusScanner virusScanner;

    @Autowired
    public FileProcessor(VirusScanner virusScanner) {
        this.virusScanner = virusScanner;
    }

    public FileProcessResult process(MultipartFile file) {
        FileProcessResult result = new FileProcessResult();
        result.setFileName(file.getOriginalFilename());
        result.setFileSize(file.getSize());

        try {
            // 校验文件大小
            long maxBytes = DataSize.parse(maxFileSizeValue).toBytes();
            if (file.getSize() > maxBytes) {
                throw new FileProcessException(ErrorCode.FILE_TOO_LARGE);
            }

            // 空文件校验
            if (file.isEmpty()) {
                result.setStatus(FileProcessResult.ProcessStatus.SUCCESS);
                return result;
            }

            // 病毒扫描
            if (!virusScanner.scan(file.getInputStream())) {
                throw new FileProcessException(ErrorCode.UNSUPPORTED_FILE_TYPE);
            }

            // 检测编码
            String charset = detectCharset(file.getInputStream());
            result.setCharset(charset);

            // 提取内容（仅保留DDL语句）
            String content = new String(file.getBytes(), charset);
            result.setContent(filterDdl(content));
            result.setStatus(FileProcessResult.ProcessStatus.SUCCESS);

        } catch (IOException | FileProcessException e) {
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
            // 默认为 UTF-8
            return "UTF-8";
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