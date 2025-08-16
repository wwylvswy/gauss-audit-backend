package com.icbc.audit.assit.controller;


import com.icbc.audit.assit.service.DmlReviewService;
import com.icbc.audit.assit.service.ReviewEngineService;
import com.icbc.audit.assit.util.FileProcessor;
import com.icbc.audit.assit.vo.ApiResponse;
import com.icbc.audit.assit.vo.FileProcessResult;
import com.icbc.audit.assit.vo.ReviewRequest;
import com.icbc.audit.assit.vo.ReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/model/review")
@Slf4j
public class ReviewController {

    private final ReviewEngineService ddlReviewService;
    private final DmlReviewService dmlReviewService;

    // --- 新增 FileProcessor 依赖注入 ---
    @Autowired
    private FileProcessor fileProcessor;


    public ReviewController(ReviewEngineService ddlReviewService, DmlReviewService dmlReviewService) {
        this.ddlReviewService = ddlReviewService;
        this.dmlReviewService = dmlReviewService;
    }

    @PostMapping("/ddl")
    @Operation(
        summary = "审核 DDL 语句",
        description = "接收 DDL SQL 语句进行审核，并返回审核结果。",
        tags = {"DDL 审核"}
    )
    public ResponseEntity<ApiResponse<ReviewResponse>> reviewDdl(@RequestBody ReviewRequest request) {
        log.info("接收到 DDL 审核请求...");
        log.info("请求内容 SQL: {}", request.getSqlText());
        try {
            ReviewResponse responseData = ddlReviewService.performReview(request.getSqlText());
            log.info("DDL 审核成功完成，准备返回结果。");
            return ResponseEntity.ok(ApiResponse.success(responseData));
        } catch (Exception e) {
            log.error("DDL 审核过程中捕获到致命异常！", e);
            return ResponseEntity.internalServerError().body(ApiResponse.error(500, "DDL评审时发生内部错误: " + e.getMessage()));
        }
    }

    @PostMapping("/ddl/from-file")
    @Operation(
        summary = "从文件审核 DDL",
        description = "接收上传的 DDL 文件，提取 SQL 内容并进行审核。",
        tags = {"DDL 文件审核"}
    )
    public ResponseEntity<ApiResponse<?>> reviewDdlFromFile(@RequestParam("file") MultipartFile file) {
        log.info("接收到 DDL 文件上传审核请求: {}", file.getOriginalFilename());

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, "上传的文件不能为空。"));
        }

        try {
            // 1. 调用移植过来的 FileProcessor 处理文件，读取 SQL 内容
            FileProcessResult fileResult = fileProcessor.process(file);

            // 2. 检查文件处理结果
            if (fileResult.getStatus() == FileProcessResult.ProcessStatus.FAILURE) {
                String errorMessage = "文件处理失败: " + fileResult.getErrorMessage();
                log.error(errorMessage);
                return ResponseEntity.badRequest().body(ApiResponse.error(400, errorMessage));
            }

            String sqlText = fileResult.getContent();
            if (sqlText == null || sqlText.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error(400, "从文件中未提取到有效的 DDL 语句。"));
            }

            // 3. 将提取的 SQL 文本送入原有的审核引擎
            log.info("文件解析成功，将读取到的 SQL 内容送入审核引擎...");
            ReviewResponse reviewResponse = ddlReviewService.performReview(sqlText);
            log.info("DDL 文件审核成功完成。");

            // 4. 返回审核结果，这就是您提到的“JSON输出”
            return ResponseEntity.ok(ApiResponse.success(reviewResponse));

        } catch (Exception e) {
            log.error("DDL 文件审核过程中发生未知异常！", e);
            return ResponseEntity.internalServerError().body(ApiResponse.error(500, "服务器内部错误: " + e.getMessage()));
        }
    }


    @PostMapping("/dml")
    @Operation(
        summary = "审核 DML 语句",
        description = "接收 DML SQL 语句进行审核，并返回审核结果。",
        tags = {"DML 审核"}
    )
    public ResponseEntity<ApiResponse<ReviewResponse>> reviewDml(@RequestBody ReviewRequest request) {
        // ... DML 审核逻辑保持不变
        log.info("接收到 DML 审核请求...");
        log.info("请求内容 SQL: {}", request.getSqlText());
        try {
            ReviewResponse responseData = dmlReviewService.performDmlReview(request.getSqlText());
            log.info("DML 审核成功完成，准备返回结果。");
            return ResponseEntity.ok(ApiResponse.success(responseData));
        } catch (Exception e) {
            log.error("DML 审核过程中捕获到致命异常！", e);
            return ResponseEntity.internalServerError().body(ApiResponse.error(500, "DML评审时发生内部错误: " + e.getMessage()));
        }
    }
}