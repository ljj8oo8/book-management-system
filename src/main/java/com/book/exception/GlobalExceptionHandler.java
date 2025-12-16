package com.book.controller;

import com.book.constant.CommonConstant;
import com.book.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 * 统一捕获并处理系统异常，返回标准化响应
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 权限不足异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.error("权限不足：请求路径{}，异常信息：{}", request.getRequestURI(), e.getMessage());
        return Result.error(CommonConstant.HTTP_STATUS_FORBIDDEN, "权限不足，无法访问该资源");
    }

    /**
     * 登录认证异常（密码错误/用户不存在）
     */
    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    public Result<Void> handleAuthException(Exception e) {
        log.error("登录认证失败：{}", e.getMessage());
        return Result.error(401, "用户名或密码错误");
    }

    /**
     * 请求参数验证异常
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result<Void> handleValidException(BindException e) {
        String msg = e.getBindingResult().getFieldError() != null ?
                e.getBindingResult().getFieldError().getDefaultMessage() : "请求参数格式错误";
        log.error("参数验证失败：{}", msg);
        return Result.error(400, msg);
    }

    /**
     * 文件上传大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("文件上传超限：{}", e.getMessage());
        return Result.error(413, "文件大小超出限制（最大50MB）");
    }

    /**
     * 重复主键/唯一索引冲突异常
     */
    @ExceptionHandler({DuplicateKeyException.class, SQLIntegrityConstraintViolationException.class})
    public Result<Void> handleDuplicateKeyException(Exception e) {
        log.error("数据重复：{}", e.getMessage());
        return Result.error(409, "数据已存在，请勿重复提交");
    }

    /**
     * 请求方式不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("请求方式不支持：{}", e.getMessage());
        return Result.error(405, "请求方式不支持，请检查请求方法");
    }

    /**
     * 接口不存在异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("接口不存在：{}", e.getMessage());
        return Result.error(404, "请求的接口不存在");
    }

    /**
     * 业务自定义异常（可扩展）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.error(400, e.getMessage());
    }

    /**
     * 全局通用异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常：请求路径{}，异常信息：", request.getRequestURI(), e);
        return Result.error(CommonConstant.HTTP_STATUS_ERROR, "系统内部异常，请联系管理员");
    }
}