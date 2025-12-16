package com.book.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * SQL注入过滤过滤器
 */
public class SqlInjectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        // 获取所有请求参数，过滤SQL关键字
        String queryString = req.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            if (containsSqlInjectKeywords(queryString)) {
                throw new ServletException("请求参数包含非法SQL关键字");
            }
        }
        chain.doFilter(request, response);
    }


    /**
     * 检查是否包含SQL注入关键字
     */
    private boolean containsSqlInjectKeywords(String str) {
        String lowerStr = str.toLowerCase();
        String[] keywords = {
                 "select", "insert", "update", "delete", "drop", "truncate", "exec", "xp_cmdshell", "declare",
                 "from", "grant","where"};
        for (String keyword : keywords) {
            if (lowerStr.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}