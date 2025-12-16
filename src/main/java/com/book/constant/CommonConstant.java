package com.book.constant;

/**
 * 系统通用常量类
 * 包含状态码、路径、正则、缓存键等全局常量
 */
public class CommonConstant {

    // ====================== 通用状态常量 ======================
    /** 启用/有效状态 */
    public static final Integer STATUS_ENABLE = 1;
    /** 禁用/无效状态 */
    public static final Integer STATUS_DISABLE = 0;

    // ====================== 缓存键常量 ======================
    /** 图书缓存键前缀 */
    public static final String CACHE_KEY_BOOK = "bookCache:";
    /** 用户缓存键前缀 */
    public static final String CACHE_KEY_USER = "userCache:";
    /** 验证码缓存键前缀 */
    public static final String CACHE_KEY_CODE = "codeCache:";
    /** 所有图书缓存键 */
    public static final String CACHE_KEY_ALL_BOOKS = "allBooks";

    // ====================== 文件相关常量 ======================
    /** PDF文件后缀 */
    public static final String FILE_SUFFIX_PDF = ".pdf";
    /** 封面图片后缀 */
    public static final String FILE_SUFFIX_PNG = ".png";
    /** 文件上传最大大小（50MB） */
    public static final long FILE_MAX_SIZE = 50 * 1024 * 1024;


    /** 成功状态码 */
    public static final int HTTP_STATUS_SUCCESS = 200;
    /** 权限不足状态码 */
    public static final int HTTP_STATUS_FORBIDDEN = 403;
    /** 未登录状态码 */
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    /** 系统异常状态码 */
    public static final int HTTP_STATUS_ERROR = 500;

    // ====================== 路径常量 ======================
    /** 图书PDF上传基础路径 */
    public static final String UPLOAD_PATH_BOOK = "./uploads/book/";
    /** 图书封面生成基础路径 */
    public static final String UPLOAD_PATH_COVER = "./uploads/cover/";
    /** 验证码缓存过期时间（5分钟，单位：秒） */
    public static final int CODE_EXPIRE_SECONDS = 300;

    // ====================== 日志操作类型常量 ======================
    /** 操作类型-新增 */
    public static final String OPERATION_TYPE_ADD = "ADD";
    /** 操作类型-修改 */
    public static final String OPERATION_TYPE_UPDATE = "UPDATE";
    /** 操作类型-删除 */
    public static final String OPERATION_TYPE_DELETE = "DELETE";
    /** 操作类型-查询 */
    public static final String OPERATION_TYPE_QUERY = "QUERY";
    /** 操作类型-登录 */
    public static final String OPERATION_TYPE_LOGIN = "LOGIN";

    // 私有构造方法，禁止实例化
    private CommonConstant() {
        throw new UnsupportedOperationException("常量类不允许实例化");
    }
}