-- 初始化角色
INSERT INTO `role` (`role_name`, `role_code`, `description`) VALUES
('管理员', 'ADMIN', '系统管理员，拥有所有权限'),
('普通用户', 'USER', '普通用户，仅可浏览和搜索图书');

-- 初始化管理员用户 (密码: admin123, BCrypt加密后)
INSERT INTO `user` (`username`, `password`, `email`, `phone`) VALUES
('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'admin@book.com', '13800138000'),
-- 初始化普通用户 (密码: user123, BCrypt加密后)
('user', '$2a$10$rp2Wi1KuLHDEScvV8kpTxOlg1cdPYkGim2EjETUfThr/m/F3n5za6', 'user@book.com', '13900139000');

INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (1, 1);
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (2, 2);

-- ----------------------------
-- 初始化权限/菜单数据
-- ----------------------------
INSERT INTO `permission` (id,perm_name, perm_code) VALUES
(1,'图书列表', 'book:list'),
(2,'图书查看', 'book:view'),
(3,'图书新增', 'book:add'),
(4,'图书编辑', 'book:edit'),
(5,'图书删除', 'book:del'),
(6,'查看图书内容', 'book:pdf');


INSERT INTO `role_permission` (role_id, permission_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),(1, 6);
INSERT INTO `role_permission` (role_id, permission_id) VALUES
(2, 1), (2, 2), (2, 6);