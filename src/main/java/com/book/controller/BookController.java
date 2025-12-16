package com.book.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.constant.CommonConstant;
import com.book.constant.RoleConstant;
import com.book.dto.BookDTO;
import com.book.dto.LogDTO;
import com.book.entity.Book;
import com.book.service.BookService;
import com.book.service.LogService;
import com.book.util.Result;
import com.book.vo.BookVO;
import com.book.vo.LogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.Base64Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 图书管理控制器
 * 处理图书增删改查、搜索、详情浏览等接口
 */
@Slf4j
@Api(tags = "图书管理接口")
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private LogService logService;

    @ApiOperation("查询操作日志")
    @PreAuthorize("hasAuthority('book:list')")
    @GetMapping("/list")
    public Result<IPage<BookVO>> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("关键字") @RequestParam(required = false) String keyword) {

        Page<BookVO> page = new Page<>(pageNum, pageSize);
        IPage<BookVO> result = bookService.selectBookPage(page, keyword);
        result.getRecords().forEach(bookVO -> {
        if (bookVO != null && bookVO.getCoverPath() != null) {
            bookVO.setCoverPath(img2Sting(bookVO.getCoverPath()));
        }
    });


        return Result.success(result);
    }

    /**
     * 新增图书（仅管理员）
     */
    @ApiOperation("新增图书")
    @PreAuthorize("hasAuthority('book:add')")
    @PostMapping("/add")
    public Result<Boolean> addBook(
            @ApiParam("图书信息") BookDTO bookDTO,
            @ApiParam("PDF文件") @RequestParam("pdfFile") MultipartFile pdfFile,
            HttpServletRequest request) {
        // 校验文件类型
        if (!pdfFile.getOriginalFilename().endsWith(CommonConstant.FILE_SUFFIX_PDF)) {
            return Result.error("仅支持上传PDF格式文件");
        }
        // 新增图书
        boolean result = bookService.addBook(bookDTO, pdfFile);
        // 记录操作日志
        logService.recordLog(
                request,
                CommonConstant.OPERATION_TYPE_ADD,
                "新增图书：" + bookDTO.getBookName()
        );
        return result ? Result.success(true) : Result.error("新增图书失败");
    }

    /**
     * 修改图书（仅管理员）
     */
    @ApiOperation("修改图书")
    @PreAuthorize("hasAuthority('book:edit')")
    @PostMapping("/update")
    public Result<Boolean> updateBook(
            @ApiParam("图书信息") BookDTO bookDTO,
            @ApiParam("PDF文件（可选）") @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,
            HttpServletRequest request) {
        // 校验文件类型（如有新文件）
        if (pdfFile != null && !pdfFile.isEmpty() && !pdfFile.getOriginalFilename().endsWith(CommonConstant.FILE_SUFFIX_PDF)) {
            return Result.error("仅支持上传PDF格式文件");
        }
        // 修改图书
        boolean result = bookService.updateBook(bookDTO, pdfFile);
        // 记录操作日志
        logService.recordLog(
                request,
                CommonConstant.OPERATION_TYPE_UPDATE,
                "修改图书：ID=" + bookDTO.getId()
        );
        return result ? Result.success(true) : Result.error("修改图书失败");
    }

    /**
     * 删除图书（仅管理员）
     */
    @ApiOperation("删除图书")
    @PreAuthorize("hasAuthority('book:del')")
    @PostMapping("/delete/{id}")
    public Result<Boolean> deleteBook(
            @ApiParam("图书ID") @PathVariable Long id,
            HttpServletRequest request) {
        boolean result = bookService.deleteBook(id);
        // 记录操作日志
        logService.recordLog(
                request,
                CommonConstant.OPERATION_TYPE_DELETE,
                "删除图书：ID=" + id
        );
        return result ? Result.success(true) : Result.error("删除图书失败");
    }

    /**
     * 搜索图书（无需登录）
     */
    @ApiOperation("搜索图书（公开接口）")
    @GetMapping("/search")
    public Result<List<BookVO>> searchBooks(
            @ApiParam("搜索关键词") @RequestParam String keyword) {
        List<BookVO> books = bookService.searchBooks(keyword);
        return Result.success(books);
    }



    /**
     * 查看图书详情（仅登录用户）
     */
    @ApiOperation("查看图书详情")
    @PreAuthorize("hasAuthority('book:view')")
    @GetMapping("/detail/{id}")
    public Result<BookVO> getBookDetail(
            @ApiParam("图书ID") @PathVariable Long id,
            HttpServletRequest request) {
        BookVO book = bookService.getBookDetail(id);
        if (book != null && book.getCoverPath() != null) {
            book.setCoverPath(img2Sting(book.getCoverPath()));
        }
        // 记录查询日志
        logService.recordLog(
                request,
                CommonConstant.OPERATION_TYPE_QUERY,
                "查看图书详情：ID=" + id
        );
        return book != null ? Result.success(book) : Result.error("图书不存在");
    }

    @ApiOperation("查看PDF内容")
    @PreAuthorize("hasAuthority('book:pdf')")
    @GetMapping("/pdf/{id}")
    public ResponseEntity<InputStreamResource> getPdfFile(@PathVariable Long id) throws IOException {

        Book book= bookService.getById(id);
        Path pdfPath = Paths.get(book.getPdfPath());
        File pdfFile = pdfPath.toFile();

        if (!pdfFile.exists() || !pdfFile.isFile()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + book.getBookName() + "\""); // inline表示在线打开

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfFile.length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }


    private String img2Sting(String path){
        String base64Image="";
        if (StringUtils.isNotEmpty(path)) {
            try {
                // 读取图片文件并转换为Base64
                Path imagePath = Paths.get(path);
                if (Files.exists(imagePath)) {
                    byte[] imageBytes = Files.readAllBytes(imagePath);
                    base64Image = "data:image/jpeg;base64," + Base64Utils.encodeToString(imageBytes);
                }
            } catch (IOException e) {
                log.error("读取封面图片失败", e);
            }
        }
        return base64Image;
    }

}