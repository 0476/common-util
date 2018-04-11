package com.ailikes.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 
 * 功能描述: Cookie工具类
 * 
 * date:   2018年4月11日 下午5:42:27
 * @author: ailikes
 * @version: 1.0.0
 * @since: 1.0.0
 */
public abstract class CookieUtil {
	
	/**
	 * 从cookie中获取值，如果无值则返回null
	 * @param request
	 * 						HttpServletRequest对象
	 * @param cookieName
	 * 						Cookie的key
	 * @return
	 */
    public static String getCookieValue(final HttpServletRequest request, String cookieName) {
        final Cookie cookie = getCookie(request, cookieName);
        if (cookie == null) {
            return null;
        }
        try {
            return URLDecoder.decode(cookie.getValue(), "UTF-8");
        } catch (Exception ex) {
            return cookie.getValue();
        }
    }

    /**
     * 从cookie中获取值，如果无值则返回null
     * @param request
     * 					HttpServletRequest对象
     * @param name
     * 					Cookie的key
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }
    
    /**
     * 向response中添加cookie
     * @param response
     * 							HttpServletResponse
     * @param cookieName
     * 							要移除的Cookie的key
     * @param cookieDomain
     * 							Cookie的作用域
     * @param cookiePath
     * 							设定哪些目录下的请求能获取该Cookie，“/”所有应用均可以获取
     * @param isCookieSecure
     * 							cookie的secure值为true时，在http中是无效的；在https中才有效
     * @param maxAge
     * 							设置cookie的生命周期，单位：秒
     */
    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue,
                                 String cookieDomain, String cookiePath, boolean isCookieSecure, Integer maxAge) {
        String cookieValueEncoded = cookieValue;
        try {
            cookieValueEncoded = URLEncoder.encode(cookieValue, "UTF-8");
        } catch (Exception ex) {
            //do nothing
        }
        Cookie cookie = new Cookie(cookieName, cookieValueEncoded);
        if (cookieDomain != null) {
            cookie.setDomain(cookieDomain);
        }
        cookie.setPath(cookiePath);
        if (maxAge != null) {
            cookie.setMaxAge(maxAge);
        }
        if (isCookieSecure) {
            cookie.setSecure(true);
        }
        response.addCookie(cookie);
    }

}
