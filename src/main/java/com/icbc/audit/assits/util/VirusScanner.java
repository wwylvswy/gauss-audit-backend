package com.icbc.audit.assits.util;

import com.icbc.audit.assits.exception.ErrorCode;
import com.icbc.audit.assits.exception.FileProcessException;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class VirusScanner {

    public boolean scan(InputStream inputStream) {
        try {
            // 模拟检查文件头，防止恶意文件伪装
            byte[] header = new byte[8];
            // mark a position
            if (inputStream.markSupported()) {
                inputStream.mark(1024);
            }
            int read = inputStream.read(header);
            if (read > 0) {
                String headerStr = new String(header).toUpperCase();
                if (headerStr.contains("DROP") && headerStr.contains("TABLE")) {
                    return false; // 检测到潜在危险SQL
                }
            }

            // 重置输入流，以便后续处理
            if (inputStream.markSupported()) {
                inputStream.reset();
            }

            return true; // 扫描通过
        } catch (Exception e) {
            throw new FileProcessException(ErrorCode.UNSUPPORTED_FILE_TYPE, e);
        }
    }
}