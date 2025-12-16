package com.book;

import com.book.filter.SqlInjectFilter;
import com.book.filter.XssFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * 图书管理系统启动类
 * @author BookSystem
 */
@SpringBootApplication
@MapperScan("com.book.mapper")
@EnableCaching
@EnableAsync
public class BookManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookManagementApplication.class, args);
	}

	/**
	 * 注册XSS过滤过滤器
	 */
	@Bean
	public FilterRegistrationBean<XssFilter> xssFilterRegistrationBean() {
		FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new XssFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName("XssFilter");
		registrationBean.setOrder(1);
		return registrationBean;
	}

	/**
	 * 注册SQL注入过滤过滤器
	 */
	@Bean
	public FilterRegistrationBean<SqlInjectFilter> sqlInjectFilterRegistrationBean() {
		FilterRegistrationBean<SqlInjectFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new SqlInjectFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setName("SqlInjectFilter");
		registrationBean.setOrder(2);
		return registrationBean;
	}
}