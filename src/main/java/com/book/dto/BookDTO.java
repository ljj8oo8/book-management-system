package com.book.dto;

import com.book.constant.CommonConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * 图书数据传输对象（DTO）
 * 用于接收前端新增/修改图书的请求参数，包含参数校验规则
 */
@Data
@ApiModel("图书请求DTO")
public class BookDTO {

    @ApiModelProperty(value = "图书ID（新增时不传，修改时必填）", example = "1")
    private Long id;

    @NotBlank(message = "图书名称不能为空")
    @Length(min = 1, max = 100, message = "图书名称长度需在1-100字符之间")
    @ApiModelProperty(value = "图书名称", required = true, example = "Spring Boot实战")
    private String bookName;

    @NotBlank(message = "作者不能为空")
    @Length(min = 1, max = 50, message = "作者名称长度需在1-50字符之间")
    @ApiModelProperty(value = "作者", required = true, example = "张三")
    private String author;

    @NotBlank(message = "出版社不能为空")
    @Length(min = 1, max = 50, message = "出版社名称长度需在1-50字符之间")
    @ApiModelProperty(value = "出版社", required = true, example = "机械工业出版社")
    private String publisher;

    @NotNull(message = "出版日期不能为空")
    @Past(message = "出版日期不能晚于当前时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "出版日期（格式：yyyy-MM-dd）", required = true, example = "2023-01-01")
    private Date publishDate;

    @Length(max = 500, message = "图书描述长度不能超过500字符")
    @ApiModelProperty(value = "图书描述", example = "本书详细讲解Spring Boot核心特性与实战案例")
    private String description;

    @NotBlank(message = "ISBN不能为空")
    @Length(min = 1, max = 50, message = "ISBN长度需在1-50字符之间")
    @ApiModelProperty(value = "ISBN", required = true, example = "9787539683591")
    private String isbn;
}