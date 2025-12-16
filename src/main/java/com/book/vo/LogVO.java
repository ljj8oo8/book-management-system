package com.book.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 操作日志视图对象
 * 格式化时间、补充操作类型文本，便于前端展示
 */
@Data
@ApiModel("操作日志视图对象")
public class LogVO {

    @ApiModelProperty("日志ID")
    private Long id;

    @ApiModelProperty("操作用户ID")
    private Long userId;

    @ApiModelProperty("操作用户名")
    private String username;

    @ApiModelProperty("操作类型（ADD/UPDATE/DELETE/QUERY/LOGIN等）")
    private String operation;

    @ApiModelProperty("操作类型文本（新增/修改/删除/查询/登录等）")
    private String operationText;

    @ApiModelProperty("请求方法（如POST /book/add）")
    private String method;

    @ApiModelProperty("请求参数/操作内容")
    private String params;

    @ApiModelProperty("操作IP")
    private String ip;

    @ApiModelProperty("操作耗时（毫秒）")
    private Long time;

    @ApiModelProperty("操作时间（格式化：yyyy-MM-dd HH:mm:ss）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    // ====================== 辅助方法 ======================
    /**
     * 操作类型码转文本
     */
    public void setOperationTextByOperation() {
        switch (this.operation) {
            case "ADD":
                this.operationText = "新增";
                break;
            case "UPDATE":
                this.operationText = "修改";
                break;
            case "DELETE":
                this.operationText = "删除";
                break;
            case "QUERY":
                this.operationText = "查询";
                break;
            case "LOGIN":
                this.operationText = "登录";
                break;
            case "LOGOUT":
                this.operationText = "退出";
                break;
            default:
                this.operationText = "其他";
        }
    }

    /**
     * 格式化耗时展示（如 100ms → 0.1s）
     */
    public String getTimeFormat() {
        if (this.time == null) {
            return "0ms";
        }
        if (this.time >= 1000) {
            return String.format("%.1fs", this.time / 1000.0);
        } else {
            return this.time + "ms";
        }
    }
}