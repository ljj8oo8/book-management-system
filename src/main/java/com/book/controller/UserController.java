package com.book.controller;

import com.book.constant.CommonConstant;
import com.book.constant.RoleConstant;
import com.book.dto.UserDTO;
import com.book.service.LogService;
import com.book.service.PermissionService;
import com.book.service.UserService;
import com.book.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户管理控制器
 * 处理用户注册、查询、角色分配等接口
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogService logService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 用户注册（公开接口）
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<Boolean> register(
            @ApiParam("用户信息") @RequestBody UserDTO userDTO,
            HttpServletRequest request) {
        // 密码加密
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // 默认分配普通用户角色
        userDTO.setRoleCode(RoleConstant.ROLE_USER);
        // 注册用户
        boolean result = userService.register(userDTO);
        // 记录日志
        logService.recordLog(
                request,
                CommonConstant.OPERATION_TYPE_ADD,
                "用户注册：" + userDTO.getUsername()
        );
        return result ? Result.success(true) : Result.error("注册失败，用户名已存在");
    }

    /**
     * 查询当前登录用户信息
     */
    @ApiOperation("查询当前用户信息")
    @GetMapping("/current")
    public Result<UserDTO> getCurrentUser(HttpServletRequest request) {
        UserDTO user = userService.getCurrentUser();
        // 脱敏密码
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 修改用户角色（仅管理员）
     */
    @ApiOperation("修改用户角色")
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/role")
    public Result<Boolean> updateUserRole(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("角色编码") @RequestParam String roleCode,
            HttpServletRequest request) {
        boolean result = userService.updateUserRole(userId, roleCode);
        // 记录日志
        logService.recordLog(
                request,
                CommonConstant.OPERATION_TYPE_UPDATE,
                "修改用户角色：ID=" + userId + "，角色=" + roleCode
        );
        return result ? Result.success(true) : Result.error("修改角色失败");
    }

    /**
     * 禁用/启用用户（仅管理员）
     */
    @ApiOperation("禁用/启用用户")
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/status")
    public Result<Boolean> updateUserStatus(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("状态 1-启用 0-禁用") @RequestParam Integer status,
            HttpServletRequest request) {
        boolean result = userService.updateUserStatus(userId, status);
        // 记录日志
        logService.recordLog(
                request,
                CommonConstant.OPERATION_TYPE_UPDATE,
                "修改用户状态：ID=" + userId + "，状态=" + status
        );
        return result ? Result.success(true) : Result.error("修改状态失败");
    }
    @ApiOperation("获取当前用户权限")
    @GetMapping("/permissions")
    public Result<List<String>> getUserPermissions() {
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 查询用户权限
        List<String> permissions = permissionService.selectByUserName(userDetails.getUsername());
        return Result.success(permissions);
    }
}