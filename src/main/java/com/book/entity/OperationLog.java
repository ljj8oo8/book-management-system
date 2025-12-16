package com.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 操作日志实体类
 * 对应数据库表：operation_log
 */
@Data
@ApiModel("操作日志实体")
@TableName("operation_log")
public class OperationLog {

    @ApiModelProperty("日志ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("操作用户ID")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("操作用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty("操作类型（ADD/UPDATE/DELETE/QUERY/LOGIN等）")
    @TableField("operation")
    private String operation;

    @ApiModelProperty("请求方法（如POST /book/add）")
    @TableField("method")
    private String method;

    @ApiModelProperty("请求参数（JSON格式）")
    @TableField("params")
    private String params;

    @ApiModelProperty("操作IP")
    @TableField("ip")
    private String ip;

    @ApiModelProperty("操作耗时（毫秒）")
    @TableField("time")
    private Long time;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}