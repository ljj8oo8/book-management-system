package com.book.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.book.entity.OperationLog;
import com.book.dto.LogDTO;
import com.book.vo.LogVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 操作日志服务接口
 */
public interface LogService extends IService<OperationLog> {

    /**
     * 记录操作日志
     * @param request 请求对象
     * @param operation 操作类型
     * @param content 操作内容
     */
    void recordLog(HttpServletRequest request, String operation, String content);

    /**
     * 分页查询日志
     * @param logDTO 查询条件
     * @return 日志列表
     */
    IPage<LogVO> listLogs(LogDTO logDTO);

    /**
     * 查询日志详情
     * @param id 日志ID
     * @return 日志详情
     */
    LogVO getLogDetail(Long id);
}