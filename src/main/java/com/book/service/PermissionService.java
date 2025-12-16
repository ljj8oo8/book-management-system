package com.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.book.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionService extends IService<Permission> {

    List<Permission> selectByUserId(Long userId);

    List<String> selectByUserName(String username);

}
