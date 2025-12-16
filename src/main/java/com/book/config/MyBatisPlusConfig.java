package com.book.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis-Plus 配置类
 * 包含分页插件、SQL分析插件、Mapper扫描等
 */
@Configuration
@MapperScan("com.book.mapper") // 扫描Mapper接口
@EnableTransactionManagement // 启用事务管理
public class MyBatisPlusConfig {

    /**
     * MyBatis-Plus插件配置（分页+SQL分析）
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 分页插件（适配H2数据库）
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.H2);
        paginationInterceptor.setMaxLimit(1000L); // 最大分页条数
        paginationInterceptor.setOverflow(true); // 页码溢出时返回最后一页
        interceptor.addInnerInterceptor(paginationInterceptor);

        // SQL分析插件（防止全表更新/删除）
        BlockAttackInnerInterceptor blockAttackInterceptor = new BlockAttackInnerInterceptor();
        interceptor.addInnerInterceptor(blockAttackInterceptor);

        return interceptor;
    }
}