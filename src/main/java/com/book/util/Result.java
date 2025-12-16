package com.book.util;

import com.book.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 全局统一响应结果类
 * 所有接口返回结果均使用此类封装
 */
@Data
@ApiModel("统一响应结果")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    @ApiModelProperty("响应码 200-成功 400-参数错误 401-未登录 403-权限不足 500-系统异常")
    private int code;

    /**
     * 响应消息
     */
    @ApiModelProperty("响应消息")
    private String msg;

    /**
     * 响应数据
     */
    @ApiModelProperty("响应数据")
    private T data;

    // ====================== 私有构造方法 ======================
    private Result() {}

    private Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // ====================== 静态构造方法 ======================

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(CommonConstant.HTTP_STATUS_SUCCESS, "操作成功", null);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(CommonConstant.HTTP_STATUS_SUCCESS, "操作成功", data);
    }

    /**
     * 成功响应（自定义消息+数据）
     */
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(CommonConstant.HTTP_STATUS_SUCCESS, msg, data);
    }

    /**
     * 失败响应（自定义码+消息）
     */
    public static <T> Result<T> error(int code, String msg) {
        return new Result<>(code, msg, null);
    }

    /**
     * 失败响应（默认500码+自定义消息）
     */
    public static <T> Result<T> error(String msg) {
        return new Result<>(CommonConstant.HTTP_STATUS_ERROR, msg, null);
    }

    /**
     * 权限不足响应
     */
    public static <T> Result<T> forbidden() {
        return new Result<>(CommonConstant.HTTP_STATUS_FORBIDDEN, "权限不足", null);
    }

    /**
     * 未登录响应
     */
    public static <T> Result<T> unauthorized() {
        return new Result<>(CommonConstant.HTTP_STATUS_UNAUTHORIZED, "请先登录", null);
    }

    /**
     * 参数错误响应
     */
    public static <T> Result<T> badRequest(String msg) {
        return new Result<>(400, msg, null);
    }

    // ====================== 链式调用方法（可选） ======================
    public Result<T> code(int code) {
        this.code = code;
        return this;
    }

    public Result<T> msg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }
}