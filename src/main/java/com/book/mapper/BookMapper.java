package com.book.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.entity.Book;
import com.book.vo.BookVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 图书Mapper接口
 * 继承MyBatis-Plus BaseMapper，无需手动编写基础CRUD SQL
 */
@Repository
public interface BookMapper extends BaseMapper<Book> {

    /**
     * 搜索图书（支持关键词模糊匹配名称/作者/出版社）
     * @param keyword 搜索关键词
     * @return 图书列表
     */
    List<BookVO> searchBooks(@Param("keyword") String keyword);

    /**
     * 分页查询图书
     * @param page 分页参数
     * @param status 图书状态（可选）
     * @return 分页图书VO列表
     */
    IPage<BookVO> selectBookPage(Page<BookVO> page, @Param("keyword") String keyword);

    /**
     * 根据ID查询图书详情（包含完整信息）
     * @param id 图书ID
     * @return 图书VO
     */
    BookVO selectBookDetailById(@Param("id") Long id);
}