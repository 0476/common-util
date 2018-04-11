package com.ailikes.util.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * 
 * 功能描述: <Pattern>%d{yyyy-MM-dd HH:mm:ss:SSS}[%p][%logger][seq:%X{seq}] - %m%n</Pattern> 带请求线程的输出，尤其是在多线程并发的时候适用
 * 
 * @version 1.0.0
 * @author 徐大伟
 */
public class LogFilter implements Filter {

    // private static final String WORD = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static Logger      logger    = LoggerFactory.getLogger(LogFilter.class);
    public static final String callIdKey = "callId";

    public LogFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        // String callId = RandomStringUtils.random(10, WORD);
        HttpServletRequest req = (HttpServletRequest) request;
        String callId = UUID.randomUUID().toString().replace("-", "");
        MDC.put(LogFilter.callIdKey, callId);
        String url = req.getRequestURL().toString();
        logger.info("{}?{}", url, req.getQueryString());
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }

    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

    public static String getCallId() {
        return MDC.get(LogFilter.callIdKey);
    }
}
