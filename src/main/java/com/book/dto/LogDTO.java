package com.book.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("日志查询DTO")
public class LogDTO {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("操作类型")
    private String operation;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页条数")
    private Integer pageSize;
}