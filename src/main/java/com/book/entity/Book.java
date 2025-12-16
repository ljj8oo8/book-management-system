package com.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.book.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 图书实体类
 * 对应数据库表：book
 */
@Data
@ApiModel("图书实体")
@TableName("book")
public class Book {

    @ApiModelProperty("图书ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("图书名称")
    @TableField("book_name")
    private String bookName;

    @ApiModelProperty("作者")
    @TableField("author")
    private String author;

    @ApiModelProperty("出版社")
    @TableField("publisher")
    private String publisher;

    @ApiModelProperty("出版日期")
    @TableField("publish_date")
    private Date publishDate;

    @ApiModelProperty("PDF文件路径")
    @TableField("pdf_path")
    private String pdfPath;

    @ApiModelProperty("封面图片路径")
    @TableField("cover_path")
    private String coverPath;

    @ApiModelProperty("图书描述")
    @TableField("description")
    private String description;

    @ApiModelProperty("ISBN")
    @TableField("isbn")
    private String isbn;



    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}