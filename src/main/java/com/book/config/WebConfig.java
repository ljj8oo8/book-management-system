package com.book.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 * 配置静态资源映射、视图解析等
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 静态资源映射（适配图书封面、PDF文件访问）
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 图书封面资源映射
        registry.addResourceHandler("/cover/**")
                .addResourceLocations("file:./uploads/cover/");
        // PDF文件资源映射
        registry.addResourceHandler("/pdf/**")
                .addResourceLocations("file:./uploads/");
        // Swagger静态资源（适配Spring Boot 2.7.x）
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        // 自定义静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}