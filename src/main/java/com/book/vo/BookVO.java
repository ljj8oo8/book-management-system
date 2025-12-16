package com.book.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 图书视图对象
 * 用于前端展示图书信息，剔除数据库层冗余字段，格式化日期等
 */
@Data
@ApiModel("图书视图对象")
public class BookVO {

    @ApiModelProperty("图书ID")
    private Long id;

    @ApiModelProperty("图书名称")
    private String bookName;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("出版社")
    private String publisher;

    @ApiModelProperty("出版日期（格式化：yyyy-MM-dd）")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date publishDate;

    @ApiModelProperty("PDF文件访问路径（前端可直接访问）")
    private String pdfPath;

    @ApiModelProperty("封面图片访问路径（前端可直接访问）")
    private String coverPath;

    @ApiModelProperty("图书描述")
    private String description;

    @ApiModelProperty("图书描述")
    private String isbn;





    @ApiModelProperty("创建时间（格式化：yyyy-MM-dd HH:mm:ss）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}