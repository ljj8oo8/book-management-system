package com.book.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.constant.CommonConstant;
import com.book.constant.RoleConstant;
import com.book.dto.UserDTO;
import com.book.entity.User;
import com.book.mapper.UserMapper;
import com.book.service.RoleService;
import com.book.service.UserService;
import com.book.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    /**
     * 用户注册
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(UserDTO userDTO) {
        // 校验用户名是否已存在
        User existUser = userMapper.selectUserByUsername(userDTO.getUsername());
        if (existUser != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        // 转换为实体
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        // 绑定角色（默认普通用户）
        user.getRole().setRoleCode(RoleConstant.ROLE_USER);
        user.setStatus(CommonConstant.STATUS_ENABLE);
        // 保存用户
        return this.save(user);
    }

    /**
     * 根据用户名查询用户
     */
    @Override
    public User getUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    /**
     * 获取当前登录用户
     */
    @Override
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = this.getUserByUsername(username);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoleCode(user.getRole().getRoleCode());
        return userDTO;
    }

    /**
     * 修改用户角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserRole(Long userId, String roleCode) {
        // 校验角色是否存在
        if (roleService.getRoleByCode(roleCode) == null) {
            throw new IllegalArgumentException("角色不存在");
        }
        int rows = userMapper.updateUserRole(userId, roleCode);
        return rows > 0;
    }

    /**
     * 修改用户状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserStatus(Long userId, Integer status) {
        int rows = userMapper.updateUserStatus(userId, status);
        return rows > 0;
    }

    /**
     * 分页查询用户
     */
    @Override
    public IPage<UserVO> selectUserPage(Page<UserVO> page, String username, Integer status) {
        return userMapper.selectUserPage(page, username, status);
    }
}