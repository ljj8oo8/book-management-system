package com.book.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;


@Configuration
public class DruidConfig {


	@Value("${spring.datasource.druid.stat-view-servlet.login-username}")
	private String loginUsername;

	@Value("${spring.datasource.druid.stat-view-servlet.login-password}")
	private String loginPassword;

	@Value("${spring.datasource.druid.stat-view-servlet.reset-enable}")
	private String resetEnable;

	@Value("${spring.datasource.druid.web-stat-filter.exclusions}")
	private String exclusions;

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.druid")
	public DataSource druidDataSource() {
		// 仅创建 Druid 数据源对象，参数全部由配置文件注入，无需硬编码
		return new DruidDataSource();
	}

	@Bean
	public ServletRegistrationBean<StatViewServlet> druidStatViewServlet() {
		ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

		// 监控面板的账号密码也可从配置文件读取（推荐），避免硬编码
		registrationBean.addInitParameter("loginUsername", loginUsername);
		registrationBean.addInitParameter("loginPassword", loginPassword);
		registrationBean.addInitParameter("resetEnable", resetEnable);

		return registrationBean;
	}

	/**
	 * 注册 Druid 监控过滤器（必须代码注册，配置文件无法替代）
	 */
	@Bean
	public FilterRegistrationBean<WebStatFilter> druidWebStatFilter() {
		FilterRegistrationBean<WebStatFilter> registrationBean = new FilterRegistrationBean<>(new WebStatFilter());

		// 过滤规则/排除项从配置文件读取
		registrationBean.addUrlPatterns("/*");
		registrationBean.addInitParameter("exclusions", exclusions);
		registrationBean.addInitParameter("sessionStatEnable", "true");
		registrationBean.addInitParameter("sessionStatMaxCount", "100");
		registrationBean.addInitParameter("principalSessionName", "username");

		return registrationBean;
	}
}