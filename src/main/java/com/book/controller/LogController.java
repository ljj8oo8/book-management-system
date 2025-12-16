package com.book.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.book.constant.RoleConstant;
import com.book.dto.LogDTO;
import com.book.service.LogService;
import com.book.util.Result;
import com.book.vo.LogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作日志控制器
 * 仅管理员可查询用户操作日志
 */
@Api(tags = "操作日志接口")
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页查询操作日志（仅管理员）
     */
    @ApiOperation("查询操作日志")
    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public Result<IPage<LogVO>> listLogs(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("用户名（可选）") @RequestParam(required = false) String username,
            @ApiParam("操作类型（可选）") @RequestParam(required = false) String operation) {
        LogDTO logDTO = new LogDTO();
        logDTO.setUsername(username);
        logDTO.setOperation(operation);
        logDTO.setPageNum(pageNum);
        logDTO.setPageSize(pageSize);

        IPage<LogVO> logs = logService.listLogs(logDTO);
        return Result.success(logs);
    }

    /**
     * 查询单条日志详情（仅管理员）
     */
    @ApiOperation("查询日志详情")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detail/{id}")
    public Result<LogVO> getLogDetail(
            @ApiParam("日志ID") @RequestParam Long id) {
        LogVO log = logService.getLogDetail(id);
        return log != null ? Result.success(log) : Result.error("日志不存在");
    }
}