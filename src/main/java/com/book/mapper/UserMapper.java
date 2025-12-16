package com.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.entity.User;
import com.book.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户Mapper接口
 * 继承MyBatis-Plus BaseMapper，适配用户CRUD
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户（含关联角色）
     * @param username 用户名
     * @return 用户实体（含角色信息）
     */
    User selectUserByUsername(@Param("username") String username);


    /**
     * 分页查询用户列表（含角色名称）
     * @param page 分页参数
     * @param username 用户名（模糊查询，可选）
     * @param status 用户状态（可选）
     * @return 分页用户VO列表
     */
    IPage<UserVO> selectUserPage(
            Page<UserVO> page,
            @Param("username") String username,
            @Param("status") Integer status);

    /**
     * 修改用户角色
     * @param userId 用户ID
     * @param roleCode 角色编码
     * @return 影响行数
     */
    int updateUserRole(@Param("userId") Long userId, @Param("roleCode") String roleCode);

    /**
     * 修改用户状态
     * @param userId 用户ID
     * @param status 状态（1-启用 0-禁用）
     * @return 影响行数
     */
    int updateUserStatus(@Param("userId") Long userId, @Param("status") Integer status);
}