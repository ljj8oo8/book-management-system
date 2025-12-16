package com.book.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.dto.LogDTO;
import com.book.entity.OperationLog;
import com.book.entity.User;
import com.book.mapper.OperationLogMapper;
import com.book.service.LogService;
import com.book.service.UserService;
import com.book.vo.LogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 日志服务实现类
 */
@Slf4j
@Service
public class LogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements LogService {

    @Autowired
    private OperationLogMapper logMapper;

    @Autowired
    private UserService userService;

    /**
     * 记录操作日志
     */
    @Override
    public void recordLog(HttpServletRequest request, String operation, String content) {
        // 获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "匿名用户";
        Long userId = 0L;
        if (authentication != null && !"anonymousUser".equals(authentication.getName())) {
            username = authentication.getName();
            User user = userService.getUserByUsername(username);
            if (user != null) {
                userId = user.getId();
            }
        }
        // 构建日志实体
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setOperation(operation);
        log.setMethod(request.getMethod() + " " + request.getRequestURI());
        log.setParams(content);
        log.setIp(request.getRemoteAddr());
        log.setTime(System.currentTimeMillis());
        log.setCreateTime(new Date());
        // 保存日志
        this.save(log);
    }

    /**
     * 分页查询日志
     */
    @Override
    public IPage<LogVO> listLogs(LogDTO logDTO) {
        Page<LogVO> page = new Page<>(logDTO.getPageNum(), logDTO.getPageSize());
        return logMapper.selectLogPage(page, logDTO.getUsername(), logDTO.getOperation());
    }

    /**
     * 查询日志详情
     */
    @Override
    public LogVO getLogDetail(Long id) {
        return logMapper.selectLogDetailById(id);
    }
}