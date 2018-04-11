package com.ailikes.util.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 功能描述：app识别
 * 
 * @version 2.0.0
 * @author guanyang/14050360
 */
public class AppInterceptor implements HandlerInterceptor {

    private static final String USER_AGENT    = "user-agent";
    /**
     * userAgent中app标识
     */
    private static final String APP_FLAG      = "SNEBUY-APP";

    /**
     * 是否是app，1是，0不是
     */
    private static final String APP_MODEL_KEY = "isApp";

    /**
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     * @version 2.0.0
     * @author guanyang/14050360
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        return true;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
     * @version 2.0.0
     * @author guanyang/14050360
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        String userAgent = request.getHeader(USER_AGENT);
        if (userAgent.contains(APP_FLAG)) {
            modelAndView.addObject(APP_MODEL_KEY, 1);// 是app过来的请求
        } else {
            modelAndView.addObject(APP_MODEL_KEY, 0);
        }

    }

    /**
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
     * @version 2.0.0
     * @author guanyang/14050360
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        // TODO Auto-generated method stub

    }

}
