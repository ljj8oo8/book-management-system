package com.book.service.impl;

import com.book.entity.Permission;
import com.book.entity.User;
import com.book.service.PermissionService;
import com.book.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security 用户认证服务实现
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 根据用户名加载用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            log.warn("用户{}不存在", username);
            throw new UsernameNotFoundException("用户不存在");
        }


        List<Permission> permissions = permissionService.selectByUserId(user.getId());

        List<GrantedAuthority> authorities = permissions.stream()
                .map(perm -> new SimpleGrantedAuthority(perm.getPermCode()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getStatus() == 1, // 是否启用
                true, true, true,
                authorities
        );
    }


}