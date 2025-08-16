package com.icbc.audit.input.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icbc.audit.input.exception.BusinessException;
import com.icbc.audit.input.exception.ErrorCode;

/**
 * JSON数据处理工具类
 *
 * 功能说明：
 * 1. 提供Java对象与JSON的互相转换
 * 2. 处理复杂对象的序列化/反序列化
 * 3. 格式化JSON输出
 * 4. 解析JSON Path表达式
 */

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将对象转换为JSON字符串
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.ENCODING_DETECTION_FAILED, e);
        }
    }

    /**
     * 将JSON字符串转换为指定类型的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.ENCODING_DETECTION_FAILED, e);
        }
    }
}