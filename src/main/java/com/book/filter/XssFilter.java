package com.book.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS攻击过滤过滤器
 */
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 包装请求，过滤XSS脚本
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }


    /**
     * XSS请求包装类
     */
    private static class XssHttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {

        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            if (value != null) {
                // 过滤XSS脚本关键字
                value = value.replaceAll("<", "&lt;")
                        .replaceAll(">", "&gt;")
                        .replaceAll("\\(", "&#40;")
                        .replaceAll("\\)", "&#41;")
                        .replaceAll("'", "&#39;")
                        .replaceAll("eval\\((.*)\\)", "")
                        .replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
            }
            return value;
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].replaceAll("<", "&lt;")
                            .replaceAll(">", "&gt;")
                            .replaceAll("\\(", "&#40;")
                            .replaceAll("\\)", "&#41;")
                            .replaceAll("'", "&#39;");
                }
            }
            return values;
        }
    }
}