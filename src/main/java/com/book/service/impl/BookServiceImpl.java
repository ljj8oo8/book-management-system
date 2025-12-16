package com.book.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.constant.CommonConstant;
import com.book.dto.BookDTO;
import com.book.entity.Book;
import com.book.mapper.BookMapper;
import com.book.service.BookService;
import com.book.vo.BookVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * 图书服务实现类
 */
@Slf4j
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {


    @Value("${book.uploads.pdf.path}")
    private String pdfDir;

    @Value("${book.uploads.cover.path}")
    private String coverDir;
    @Autowired
    private BookMapper bookMapper;

    /**
     * 新增图书
     */
    @Override
    public boolean addBook(BookDTO bookDTO, MultipartFile pdfFile) {
        // 1. 文件上传处理
        String pdfPath = uploadPdfFile(pdfFile);
        // 2. 生成封面（简化版：仅记录路径，实际可调用PDF封面生成工具）
        String coverPath = generateCoverPath(pdfPath);
        // 3. 转换为实体
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);
        book.setPdfPath(pdfPath);
        book.setCoverPath(coverPath);
        // 4. 保存数据库
        return this.save(book);
    }

    /**
     * 修改图书
     */
    @Override
    public boolean updateBook(BookDTO bookDTO, MultipartFile pdfFile) {
        Book book = this.getById(bookDTO.getId());
        if (book == null) {
            throw new IllegalArgumentException("图书不存在");
        }
        // 如有新PDF文件，重新上传
        if (pdfFile != null && !pdfFile.isEmpty()) {
            // 删除旧文件
            deleteFile(book.getPdfPath());
            deleteFile(book.getCoverPath());
            // 上传新文件
            String newPdfPath = uploadPdfFile(pdfFile);
            String newCoverPath = generateCoverPath(newPdfPath);
            book.setPdfPath(newPdfPath);
            book.setCoverPath(newCoverPath);
        }
        // 更新基础信息
        BeanUtils.copyProperties(bookDTO, book, "id", "pdfPath", "coverPath");
        return this.updateById(book);
    }

    /**
     * 删除图书
     */
    @Override
    public boolean deleteBook(Long id) {
        Book book = this.getById(id);
        if (book == null) {
            return false;
        }
        // 删除文件
        deleteFile(book.getPdfPath());
        deleteFile(book.getCoverPath());
        // 删除数据库记录
        return this.removeById(id);
    }

    /**
     * 搜索图书
     */
    @Override
    public List<BookVO> searchBooks(String keyword) {
        return bookMapper.searchBooks(keyword);
    }

    /**
     * 分页查询图书
     */
    @Override
    public IPage<BookVO> selectBookPage(Page<BookVO> page, String keyword) {
        return bookMapper.selectBookPage(page, keyword);
    }

    /**
     * 查询图书详情
     */
    @Override
    public BookVO getBookDetail(Long id) {
        return bookMapper.selectBookDetailById(id);
    }


    /**
     * 上传PDF文件
     */
    private String uploadPdfFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("PDF文件不能为空");
        }

        Path path = Paths.get(pdfDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error("创建目录失败: " + e.getMessage());
            }
        }

        String fileName = UUID.randomUUID() + CommonConstant.FILE_SUFFIX_PDF;
        File destFile = new File(pdfDir, fileName);
        try {
            file.transferTo(destFile);
            log.info("PDF文件上传成功：{}", destFile.getAbsolutePath());
            return destFile.getAbsolutePath();
        } catch (IOException e) {
            log.error("PDF文件上传失败", e);
            throw new RuntimeException("文件上传失败");
        }
    }

    /**
     *
     * 生成封面路径（简化版）
     */
    private String generateCoverPath(String pdfPath) {

        if (StringUtils.isEmpty(pdfPath)) {
            throw new IllegalArgumentException("PDF文件路径为空！");
        }

        Path path = Paths.get(coverDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                System.err.println("创建目录失败: " + e.getMessage());
            }
        }

        String fileName = UUID.randomUUID() + CommonConstant.FILE_SUFFIX_PNG;
        String coverImag=coverDir+fileName;

        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImageWithDPI(0, 300); // 0 是页面索引，300是DPI
            ImageIO.write(image, "PNG", new File(coverImag)); // 保存为PNG格式
        } catch (IOException e) {
            e.printStackTrace();
        }
        return coverImag;
    }

    /**
     * 删除文件
     *
     */
    private void deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return;
        }
        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                log.info("文件删除成功：{}", filePath);
            } else {
                log.warn("文件删除失败：{}", filePath);
            }
        }
    }
}