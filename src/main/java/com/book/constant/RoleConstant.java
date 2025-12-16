package com.book.constant;


public class RoleConstant {


    /** 管理员角色编码（Spring Security角色名需加ROLE_前缀） */
    public static final String ROLE_ADMIN = "ADMIN";
    /** 普通用户角色编码 */
    public static final String ROLE_USER = "USER";
    /** 游客角色（未登录） */
    public static final String ROLE_GUEST = "ROLE_GUEST";


    private RoleConstant() {
        throw new UnsupportedOperationException("常量类不允许实例化");
    }
}