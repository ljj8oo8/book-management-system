package com.book.vo;

import com.book.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户视图对象
 * 剔除密码等敏感字段，补充角色展示信息
 */
@Data
@ApiModel("用户视图对象")
public class UserVO {

    @ApiModelProperty("用户ID")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("状态 1-启用 0-禁用")
    private Integer status;

    @ApiModelProperty("状态文本（启用/禁用）")
    private String statusText;

    @ApiModelProperty("角色信息")
    private Role role;

    @ApiModelProperty("角色名称（简化展示用）")
    private String roleName;

    @ApiModelProperty("创建时间（格式化：yyyy-MM-dd HH:mm:ss）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    // ====================== 敏感字段隐藏（前端不可见） ======================
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String password;

    // ====================== 辅助方法 ======================
    /**
     * 状态码转文本
     */
    public void setStatusTextByStatus() {
        this.statusText = this.status == 1 ? "启用" : "禁用";
    }

    /**
     * 从角色对象提取角色名称
     */
    public void setRoleNameByRole() {
        if (this.role != null) {
            this.roleName = this.role.getRoleName();
        }
    }
}