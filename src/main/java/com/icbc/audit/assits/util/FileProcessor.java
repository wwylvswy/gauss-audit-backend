package com.icbc.audit.assits.util;

import com.icbc.audit.assits.exception.ErrorCode;
import com.icbc.audit.assits.exception.FileProcessException;
import com.icbc.audit.assits.vo.FileProcessResult;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
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

    /**
     * 【为DDL服务】处理文件，并过滤掉非DDL语句
     */
    public FileProcessResult process(MultipartFile file) {
        FileProcessResult result = processAndGetContent(file);
        if (result.getStatus() == FileProcessResult.ProcessStatus.SUCCESS && result.getContent() != null) {
            result.setContent(filterDdl(result.getContent()));
        }
        return result;
    }

    /**
     * 【为DML服务】处理文件，安全地读取完整内容，不过滤SQL类型
     */
    public FileProcessResult processAndGetContent(MultipartFile file) {
        FileProcessResult result = new FileProcessResult();
        result.setFileName(file.getOriginalFilename());
        result.setFileSize(file.getSize());

        try {
            long maxBytes = DataSize.parse(maxFileSizeValue).toBytes();
            if (file.getSize() > maxBytes) {
                throw new FileProcessException(ErrorCode.FILE_TOO_LARGE);
            }

            if (file.isEmpty()) {
                result.setStatus(FileProcessResult.ProcessStatus.SUCCESS);
                result.setContent("");
                return result;
            }

            byte[] fileBytes = file.getBytes();

            if (!virusScanner.scan(new ByteArrayInputStream(fileBytes))) {
                throw new FileProcessException(ErrorCode.UNSUPPORTED_FILE_TYPE);
            }

            String charset = detectCharset(new ByteArrayInputStream(fileBytes));
            result.setCharset(charset);

            String content = new String(fileBytes, charset);
            result.setContent(content);
            result.setStatus(FileProcessResult.ProcessStatus.SUCCESS);

        } catch (IOException | FileProcessException e) {
            result.setStatus(FileProcessResult.ProcessStatus.FAILURE);
            result.setErrorMessage(e.getMessage());
        }
        return result;
    }

    private String detectCharset(InputStream is) throws IOException {
        byte[] buf = new byte[4096];
        UniversalDetector detector = new UniversalDetector(null);
        int nread;
        try (InputStream stream = is) {
            while ((nread = stream.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
        }
        detector.dataEnd();
        String encoding = detector.getDetectedCharset();
        detector.reset();
        return (encoding == null) ? "UTF-8" : encoding;
    }

    private String filterDdl(String content) {
        if (content == null) return "";
        StringBuilder ddlContent = new StringBuilder();
        String[] statements = content.split(";");
        for (String stmt : statements) {
            String trimmed = stmt.trim().toUpperCase();
            if (trimmed.startsWith("CREATE TABLE") || trimmed.startsWith("ALTER TABLE")) {
                ddlContent.append(stmt.trim()).append(";\n");
            }
        }
        return ddlContent.toString();
    }
}