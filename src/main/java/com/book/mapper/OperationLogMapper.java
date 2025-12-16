package com.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.entity.OperationLog;
import com.book.vo.LogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 操作日志Mapper接口
 * 适配日志CRUD、分页查询
 */
@Repository
public interface OperationLogMapper extends BaseMapper<OperationLog> {

    /**
     * 分页查询操作日志
     * @param page 分页参数
     * @param username 用户名（可选）
     * @param operation 操作类型（可选）
     * @return 分页日志VO列表
     */
    IPage<LogVO> selectLogPage(
            Page<LogVO> page,
            @Param("username") String username,
            @Param("operation") String operation);

    /**
     * 根据ID查询日志详情
     * @param id 日志ID
     * @return 日志VO
     */
    LogVO selectLogDetailById(@Param("id") Long id);
}