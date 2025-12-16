
-- ----------------------------
-- 用户表：存储系统用户基础信息
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID，主键自增',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '登录用户名，唯一不可重复',
  `password` VARCHAR(100) NOT NULL COMMENT '登录密码（BCrypt加密存储）',
  `email` VARCHAR(100) COMMENT '用户邮箱（可选，用于找回密码）',
  `phone` VARCHAR(20) COMMENT '用户手机号（可选）',
  `status` TINYINT DEFAULT 1 COMMENT '用户状态：1-启用，0-禁用',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户信息更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

CREATE INDEX idx_user_username ON `user`(`username`) ;
CREATE INDEX idx_user_status ON `user`(`status`);

-- ----------------------------
-- 角色表：存储系统角色定义（权限维度）
-- ----------------------------
CREATE TABLE IF NOT EXISTS `role` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID，主键自增',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称（如：系统管理员、普通用户）',
  `role_code` VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码（如：ROLE_ADMIN、ROLE_USER），权限控制使用',
  `description` VARCHAR(200) COMMENT '角色描述（说明角色权限范围）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 新增索引：角色编码（权限校验高频查询）
CREATE INDEX idx_role_code ON `role`(`role_code`);

-- ----------------------------
-- 用户角色关联表：用户与角色的多对多关联
-- ----------------------------
CREATE TABLE IF NOT EXISTS `user_role` (
   `user_id` BIGINT NOT NULL COMMENT '用户ID，关联user表主键',
   `role_id` BIGINT NOT NULL COMMENT '角色ID，关联role表主键',
   PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';


CREATE TABLE IF NOT EXISTS `permission` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `perm_name` VARCHAR(100) NOT NULL COMMENT '权限名称（如：图书查询）',
    `perm_code` VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码（如：book:list）'
) COMMENT='权限表';

CREATE TABLE IF NOT EXISTS `role_permission` (
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    UNIQUE KEY `uk_role_perm` (`role_id`, `permission_id`)
) COMMENT='角色-权限关联表';


-- ----------------------------
-- 图书表：存储图书核心信息
-- ----------------------------
CREATE TABLE IF NOT EXISTS `book` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '图书ID，主键自增',
  `book_name` VARCHAR(100) NOT NULL COMMENT '图书名称（必填）',
  `author` VARCHAR(50) COMMENT '图书作者（可选）',
  `publisher` VARCHAR(50) COMMENT '出版社（可选）',
  `publish_date` DATE COMMENT '出版日期（可选）',
  `isbn` VARCHAR(30) NOT NULL COMMENT '图书ISBN',
  `pdf_path` VARCHAR(200) NOT NULL COMMENT 'PDF文件存储路径（相对/绝对路径，必填）',
  `cover_path` VARCHAR(200) COMMENT '封面图片存储路径（可选）',
  `description` TEXT COMMENT '图书简介/描述（可选）',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '图书创建时间',
  `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '图书信息更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书信息表';

-- 新增索引：图书名称（模糊查询）、作者、出版社、状态（高频筛选）
CREATE INDEX idx_book_name ON `book`(`book_name`) ;
CREATE INDEX idx_book_isbn ON `book`(`isbn`) ;


-- ----------------------------
-- 操作日志表：存储用户操作行为记录
-- ----------------------------
CREATE TABLE IF NOT EXISTS `operation_log` (
   `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID，主键自增',
   `user_id` BIGINT NOT NULL COMMENT '操作用户ID，关联user表主键',
   `username` VARCHAR(50) NOT NULL COMMENT '操作用户名（冗余存储，避免用户删除后日志无用户名）',
   `operation` VARCHAR(100) NOT NULL COMMENT '操作名称（如：新增图书、编辑用户、登录系统）',
   `method` VARCHAR(200) COMMENT '请求方法全路径（如：com.book.controller.BookController.addBook）',
   `params` TEXT COMMENT '请求参数（JSON格式存储）',
   `ip` VARCHAR(50) COMMENT '操作IP地址',
   `time` BIGINT COMMENT '操作耗时（单位：毫秒）',
   `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作发生时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户操作日志表';

-- 新增索引：用户ID（查询用户所有操作）、操作时间（按时间筛选日志）、操作类型（按操作筛选）
CREATE INDEX idx_operation_log_user_id ON `operation_log`(`user_id`) ;
CREATE INDEX idx_operation_log_create_time ON `operation_log`(`create_time`) ;
CREATE INDEX idx_operation_log_operation ON `operation_log`(`operation`) ;