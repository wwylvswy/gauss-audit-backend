package com.icbc.audit.assits.controller;

import com.icbc.audit.assits.service.review.AdminDdlReviewService;
import com.icbc.audit.assits.service.review.AdminDmlReviewService;
import com.icbc.audit.assits.service.review.UserDdlReviewService;
import com.icbc.audit.assits.service.review.UserDmlReviewService;
import com.icbc.audit.assits.util.FileProcessor;
import com.icbc.audit.assits.vo.ApiResponse;
import com.icbc.audit.assits.vo.FileProcessResult;
import com.icbc.audit.assits.vo.ReviewRequest;
import com.icbc.audit.assits.vo.ReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/review")
@Slf4j
@Tag(name = "基于大模型的SQL审核")
public class ReviewController {

    private final UserDdlReviewService userDdlReviewService;
    private final AdminDdlReviewService adminDdlReviewService;
    private final UserDmlReviewService userDmlReviewService;
    private final AdminDmlReviewService adminDmlReviewService;
    private final FileProcessor fileProcessor;

    @Autowired
    public ReviewController(UserDdlReviewService userDdlReviewService,
                            AdminDdlReviewService adminDdlReviewService,
                            UserDmlReviewService userDmlReviewService,
                            AdminDmlReviewService adminDmlReviewService,
                            FileProcessor fileProcessor) {
        this.userDdlReviewService = userDdlReviewService;
        this.adminDdlReviewService = adminDdlReviewService;
        this.userDmlReviewService = userDmlReviewService;
        this.adminDmlReviewService = adminDmlReviewService;
        this.fileProcessor = fileProcessor;
    }

    @Operation(
            summary = "普通用户DDL审核",
            description = "审核普通用户提交的DDL语句，返回审核结果。"
    )
    @PostMapping("/common/ddl")
    public ResponseEntity<ApiResponse<?>> reviewUserDdl(@RequestBody ReviewRequest request) {
        log.info("接收到【普通用户 DDL】手动输入审核请求...");
        try {
            ReviewResponse responseData = userDdlReviewService.review(request.getSqlText());
            return ResponseEntity.ok(ApiResponse.success(responseData));
        } catch (Exception e) {
            return handleException("普通用户DDL审核", e);
        }
    }

    @PostMapping("/common/ddl/from-file")
    public ResponseEntity<ApiResponse<?>> reviewUserDdlFromFile(@RequestParam("file") MultipartFile file) {
        log.info("接收到【用户 DDL】文件上传审核请求: {}", file.getOriginalFilename());
        return handleDdlFileUpload(file, "latest", false);
    }
    @Operation(
            summary = "管理员DDL审核",
            description = "审核管理员提交的DDL语句，返回审核结果。"
    )
    @PostMapping("/admin/ddl")
    public ResponseEntity<ApiResponse<?>> reviewAdminDdl(@RequestBody ReviewRequest request) {
        log.info("接收到【管理员 DDL】手动输入审核请求...");
        try {
            ReviewResponse responseData = adminDdlReviewService.review(request.getSqlText(), request.getVersion());
            return ResponseEntity.ok(ApiResponse.success(responseData));
        } catch (Exception e) {
            return handleException("管理员DDL审核", e);
        }
    }

    @PostMapping("/admin/ddl/from-file")
    public ResponseEntity<ApiResponse<?>> reviewAdminDdlFromFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "version", required = false) String version) {
        log.info("接收到【管理员 DDL】文件上传审核请求: {}, 版本: {}", file.getOriginalFilename(), version);
        return handleDdlFileUpload(file, version, true);
    }

    @Operation(
            summary = "普通用户DML审核",
            description = "审核普通用户提交的DML语句，返回审核结果。"
    )
    @PostMapping("/common/dml")
    public ResponseEntity<ApiResponse<?>> reviewUserDml(@RequestBody ReviewRequest request) {
        log.info("接收到【普通用户 DML】手动输入审核请求...");
        try {
            ReviewResponse responseData = userDmlReviewService.review(request.getSqlText());
            return ResponseEntity.ok(ApiResponse.success(responseData));
        } catch (Exception e) {
            return handleException("普通用户DML审核", e);
        }
    }

    @PostMapping("/common/dml/from-file")
    public ResponseEntity<ApiResponse<?>> reviewUserDmlFromFile(@RequestParam("file") MultipartFile file) {
        log.info("接收到【用户 DML】文件上传审核请求: {}", file.getOriginalFilename());
        return handleDmlFileUpload(file, "latest", false);
    }

    @Operation(
            summary = "管理员DML审核",
            description = "审核管理员提交的DML语句，返回审核结果。"
    )
    @PostMapping("/admin/dml")
    public ResponseEntity<ApiResponse<?>> reviewAdminDml(@RequestBody ReviewRequest request) {
        log.info("接收到【管理员 DML】手动输入审核请求...");
        try {
            ReviewResponse responseData = adminDmlReviewService.review(request.getSqlText(), request.getVersion());
            return ResponseEntity.ok(ApiResponse.success(responseData));
        } catch (Exception e) {
            return handleException("管理员DML审核", e);
        }
    }

    @PostMapping("/admin/dml/from-file")
    public ResponseEntity<ApiResponse<?>> reviewAdminDmlFromFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "version", required = false) String version) {
        log.info("接收到【管理员 DML】文件上传审核请求: {}, 版本: {}", file.getOriginalFilename(), version);
        return handleDmlFileUpload(file, version, true);
    }

    // --- 内部辅助方法 ---
    private ResponseEntity<ApiResponse<?>> handleDdlFileUpload(MultipartFile file, String version, boolean isAdmin) {
        try {
            FileProcessResult fileResult = fileProcessor.process(file);
            if (fileResult.getStatus() == FileProcessResult.ProcessStatus.FAILURE) {
                return buildFileErrorResponse(fileResult);
            }
            String sqlText = fileResult.getContent();
            if (sqlText == null || sqlText.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error(400, "从文件中未提取到有效的 DDL 语句。"));
            }
            ReviewResponse responseData = isAdmin ?
                    adminDdlReviewService.review(sqlText, version) :
                    userDdlReviewService.review(sqlText);
            return ResponseEntity.ok(ApiResponse.success(responseData));
        } catch (Exception e) {
            return handleException(isAdmin ? "管理员DDL文件审核" : "用户DDL文件审核", e);
        }
    }

    private ResponseEntity<ApiResponse<?>> handleDmlFileUpload(MultipartFile file, String version, boolean isAdmin) {
        try {
            FileProcessResult fileResult = fileProcessor.processAndGetContent(file);
            if (fileResult.getStatus() == FileProcessResult.ProcessStatus.FAILURE) {
                return buildFileErrorResponse(fileResult);
            }
            String sqlText = fileResult.getContent();
            if (sqlText == null || sqlText.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error(400, "从文件中未提取到任何SQL内容。"));
            }
            ReviewResponse responseData = isAdmin ?
                    adminDmlReviewService.review(sqlText, version) :
                    userDmlReviewService.review(sqlText);
            return ResponseEntity.ok(ApiResponse.success(responseData));
        } catch (Exception e) {
            return handleException(isAdmin ? "管理员DML文件审核" : "用户DML文件审核", e);
        }
    }

    private ResponseEntity<ApiResponse<?>> buildFileErrorResponse(FileProcessResult fileResult) {
        String errorMessage = "文件处理失败: " + fileResult.getErrorMessage();
        log.error(errorMessage);
        // 【重要修正】我们在这里显式地创建 ApiResponse<?> 类型的变量
        ApiResponse<?> errorResponse = ApiResponse.error(400, errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // 【重要修正】修正了泛型，使其返回 ResponseEntity<ApiResponse<?>>，与所有调用方完全匹配
    private ResponseEntity<ApiResponse<?>> handleException(String context, Exception e) {
        log.error("{}过程中捕获到致命异常！", context, e);
        // 【重要修正】显式创建 ApiResponse<?> 类型的变量来消除编译器的类型推断歧义
        ApiResponse<?> errorResponse = ApiResponse.error(500, context + "时发生内部错误: " + e.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}