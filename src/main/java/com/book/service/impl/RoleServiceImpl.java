package com.book.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.entity.Role;
import com.book.mapper.RoleMapper;
import com.book.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色服务实现类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 根据角色编码查询角色
     */
    @Override
    public Role getRoleByCode(String roleCode) {
        return roleMapper.selectRoleByCode(roleCode);
    }
}