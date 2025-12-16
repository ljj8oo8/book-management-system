package com.book.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("用户请求DTO")
public class
UserDTO {
    @ApiModelProperty("用户ID")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^\\w{2,20}$", message = "用户名仅支持字母、数字、下划线，2-20位")
    @ApiModelProperty("用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    @ApiModelProperty("手机号")
    private String phone;

    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式错误")
    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("状态 1-启用 0-禁用")
    private Integer status;
}