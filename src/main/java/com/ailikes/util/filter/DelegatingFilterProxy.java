package com.ailikes.util.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 功能描述：Filter 的代理类
 * 
 * @version 2.0.0
 * @author guanyang/14050360
 */
public class DelegatingFilterProxy implements Filter {

    private static final String   TARGET_FILTER_BEAN_PARAM = "targetFilterBean";

    private String                targetFilterBean;
    private Filter                proxy;
    private ServletContext        servletContext;
    private WebApplicationContext webApplicationContext;

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     * @version 2.0.0
     * @author guanyang/14050360
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        this.targetFilterBean = config.getInitParameter(TARGET_FILTER_BEAN_PARAM);
        this.servletContext = config.getServletContext();
        this.webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        this.proxy = (Filter) webApplicationContext.getBean(this.targetFilterBean);
        this.proxy.init(config);

    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     *      javax.servlet.FilterChain)
     * @version 2.0.0
     * @author guanyang/14050360
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        proxy.doFilter(request, response, chain);

    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#destroy()
     * @version 2.0.0
     * @author guanyang/14050360
     */
    @Override
    public void destroy() {
        proxy.destroy();
    }

}
