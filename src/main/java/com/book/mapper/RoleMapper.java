package com.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.book.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 角色Mapper接口
 * 适配角色CRUD、角色编码查询
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色编码查询角色
     * @param roleCode 角色编码（如ROLE_ADMIN）
     * @return 角色实体
     */
    Role selectRoleByCode(@Param("roleCode") String roleCode);
}