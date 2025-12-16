package com.book.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.book.entity.Book;
import com.book.vo.BookVO;

import java.util.List;

/**
 * 图书服务接口
 * 继承IService，获得MyBatis-Plus基础CRUD能力
 */
public interface BookService extends IService<Book> {

    /**
     * 新增图书（含PDF上传、封面生成）
     * @param bookDTO 图书信息
     * @param pdfFile PDF文件
     * @return 新增结果
     */
    boolean addBook(com.book.dto.BookDTO bookDTO, org.springframework.web.multipart.MultipartFile pdfFile);

    /**
     * 修改图书（支持PDF重新上传）
     * @param bookDTO 图书信息
     * @param pdfFile PDF文件（可选）
     * @return 修改结果
     */
    boolean updateBook(com.book.dto.BookDTO bookDTO, org.springframework.web.multipart.MultipartFile pdfFile);

    /**
     * 删除图书（含文件删除）
     * @param id 图书ID
     * @return 删除结果
     */
    boolean deleteBook(Long id);

    /**
     * 搜索图书（公开接口）
     * @param keyword 搜索关键词
     * @return 图书列表
     */
    List<BookVO> searchBooks(String keyword);

    /**
     * 分页查询图书
     * @param page 分页参数
     * @param keyword 关键字
     * @return 分页结果
     */
    IPage<BookVO> selectBookPage(Page<BookVO> page, String keyword);

    /**
     * 查询图书详情
     * @param id 图书ID
     * @return 图书详情
     */
    BookVO getBookDetail(Long id);
}