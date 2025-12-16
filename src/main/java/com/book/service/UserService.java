package com.book.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.book.entity.User;
import com.book.dto.UserDTO;
import com.book.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userDTO 用户信息
     * @return 注册结果
     */
    boolean register(UserDTO userDTO);

    /**
     * 根据用户名查询用户（含角色）
     * @param username 用户名
     * @return 用户实体
     */
    User getUserByUsername(String username);


    /**
     * 获取当前登录用户信息
     * @return 当前用户DTO
     */
    UserDTO getCurrentUser();

    /**
     * 修改用户角色
     * @param userId 用户ID
     * @param roleCode 角色编码
     * @return 修改结果
     */
    boolean updateUserRole(Long userId, String roleCode);

    /**
     * 修改用户状态
     * @param userId 用户ID
     * @param status 状态（1-启用 0-禁用）
     * @return 修改结果
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 分页查询用户
     * @param page 分页参数
     * @param username 用户名（模糊）
     * @param status 状态
     * @return 分页结果
     */
    IPage<UserVO> selectUserPage(Page<UserVO> page, String username, Integer status);
}