package com.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.book.entity.Role;

/**
 * 角色服务接口
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据角色编码查询角色
     * @param roleCode 角色编码
     * @return 角色实体
     */
    Role getRoleByCode(String roleCode);
}