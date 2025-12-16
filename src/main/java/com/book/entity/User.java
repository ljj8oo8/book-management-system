package com.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.book.constant.CommonConstant;
import com.book.constant.RoleConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 * 对应数据库表：user
 * 实现 UserDetails 接口，适配 Spring Security 认证
 */
@Data
@ApiModel("用户实体")
@TableName("user")
public class User implements UserDetails {

    @ApiModelProperty("用户ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty("密码（加密后）")
    @TableField("password")
    private String password;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("状态 1-启用 0-禁用")
    @TableField("status")
    private Integer status = CommonConstant.STATUS_ENABLE; // 默认启用

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    @ApiModelProperty("用户角色")
    @TableField(exist = false)
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        return authorities;
    }

    /**
     * Spring Security - 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Spring Security - 账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return CommonConstant.STATUS_ENABLE.equals(status);
    }

    /**
     * Spring Security - 凭证是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Spring Security - 账户是否可用
     */
    @Override
    public boolean isEnabled() {
        return CommonConstant.STATUS_ENABLE.equals(status);
    }
}