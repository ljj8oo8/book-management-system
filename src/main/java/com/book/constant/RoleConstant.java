package com.book.constant;

/**
 * 角色与权限常量类
 * 适配Spring Security的角色/权限命名规范（角色以ROLE_开头）
 */
public class RoleConstant {

    // ====================== 角色编码常量 ======================
    /** 管理员角色编码（Spring Security角色名需加ROLE_前缀） */
    public static final String ROLE_ADMIN = "ADMIN";
    /** 普通用户角色编码 */
    public static final String ROLE_USER = "USER";
    /** 游客角色（未登录） */
    public static final String ROLE_GUEST = "ROLE_GUEST";

    // ====================== 角色名称常量 ======================
    /** 管理员角色名称 */
    public static final String ROLE_NAME_ADMIN = "管理员";
    /** 普通用户角色名称 */
    public static final String ROLE_NAME_USER = "普通用户";

    // ====================== 权限编码常量（细粒度权限控制） ======================
    /** 图书新增权限 */
    public static final String PERMISSION_BOOK_ADD = "book:add";
    /** 图书修改权限 */
    public static final String PERMISSION_BOOK_UPDATE = "book:update";
    /** 图书删除权限 */
    public static final String PERMISSION_BOOK_DELETE = "book:del";
    /** 图书查询权限 */
    public static final String PERMISSION_BOOK_QUERY = "book:query";
    /** 日志查看权限 */
    public static final String PERMISSION_LOG_VIEW = "log:view";

    // ====================== 角色-权限映射常量 ======================
    /** 管理员拥有的所有权限 */
    public static final String[] ADMIN_PERMISSIONS = {
            PERMISSION_BOOK_ADD,
            PERMISSION_BOOK_UPDATE,
            PERMISSION_BOOK_DELETE,
            PERMISSION_BOOK_QUERY,
            PERMISSION_LOG_VIEW
    };
    /** 普通用户拥有的权限 */
    public static final String[] USER_PERMISSIONS = {
            PERMISSION_BOOK_QUERY
    };
    /** 游客拥有的权限 */
    public static final String[] GUEST_PERMISSIONS = {
            PERMISSION_BOOK_QUERY
    };

    // 私有构造方法，禁止实例化
    private RoleConstant() {
        throw new UnsupportedOperationException("常量类不允许实例化");
    }
}