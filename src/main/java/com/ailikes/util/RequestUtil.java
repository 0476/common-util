package com.ailikes.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * Copyright (C), 2002-2014,北京天道和元科技有限公司
 * FileName: RequestUtil.java
 * Author:   陈天宇
 * Date:     2014年12月31日 上午11:11:47
 * Description: 针对get地址参数进行操作     
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
public class RequestUtil {
	
	/**
	 * 
	 * 功能描述: 将paramName和paramValue拼接到buffer后面<br>
	 *
	 * @param buffer
	 * 						get请求地址
	 * @param paramName
	 * 						拼接参数名称
	 * @param paramValue
	 * 						拼接参数值
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static String addParam(String url, String paramName, String paramValue){
		if(null != paramName && !"".equals(paramName)){
			url = url + (url.indexOf("?") < 0 ? "?" : "&");
			url = url + paramName + "=";
			if(null != paramValue && !"".equals(paramValue)){
				url = url + paramValue;
			}
		}
		return url;
	}
	
	/**
	 * 
	 * 功能描述: 根据http请求，获取发送请求的域名<br>
	 *
	 * @param request
	 * 					Http请求
	 * @return
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static String getDomainByRequest(HttpServletRequest request){
		StringBuffer url = request.getRequestURL();  
		url = url.delete(0,url.toString().indexOf("."));
		if(url.indexOf(":") > 0){
			url = url.delete(url.indexOf(":"), url.length());
		}
		if(url.indexOf("/") > 0){
			url = url.delete(url.indexOf("/"), url.length());
		}
		return url.toString();
	}
	
	/**
	 * 
	 * 功能描述: 根据http请求，获取发送请求的域名<br>
	 *
	 * @param request
	 * 					Http请求
	 * @return
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static String getDomainByRequest(String url){
		StringBuffer urlBuffer = new StringBuffer(url);  
		urlBuffer = urlBuffer.delete(0,urlBuffer.toString().indexOf("."));
		if(urlBuffer.indexOf(":") > 0){
			urlBuffer = urlBuffer.delete(urlBuffer.indexOf(":"), urlBuffer.length());
		}
		if(urlBuffer.indexOf("/") > 0){
			urlBuffer = urlBuffer.delete(urlBuffer.indexOf("/"), urlBuffer.length());
		}
		return urlBuffer.toString();
	}
	/**
	 * 
	 * 功能描述: 按照targeturl全路径查找其申请资源<br>
	 *
	 * @autor wangkaining
	 * @param url
	 * @return
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static String getResByCompleteUrl(String url){
        StringBuffer urlBuffer = new StringBuffer(url);  
        urlBuffer = urlBuffer.delete(0,urlBuffer.toString().indexOf("."));
        if(urlBuffer.indexOf("/") > 0){
            urlBuffer = urlBuffer.delete(0,urlBuffer.indexOf("/"));
        }
        return urlBuffer.toString();
    }
	
	/**
	 * 
	 * 功能描述: 根据request构造请求地址<br>
	 *
	 * @param request
	 * @return
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
    public static String buildRequestUrl(HttpServletRequest request) {
        final StringBuilder buffer = new StringBuilder();
        String requestUrlFromHeader = request.getHeader("x-request-url");
        if (null != requestUrlFromHeader && !"".equals(requestUrlFromHeader)) {
            buffer.append(requestUrlFromHeader);
        } else {
            buffer.append(request.isSecure() ? "https://" : "http://").append(request.getServerName()).append(":").append(request.getServerPort()).append(request.getRequestURI());
        }
        return buffer.toString();
    }
    
    /**
     * 将当前请求中的QueryString拼接到指定地址后
     * @param request
     * 					HttpRequestServlet
     * @param url
     * 					请求地址
     * @return
     */
    public static String appendQueryStr(HttpServletRequest request, String url) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(url);
        String queryStr = request.getQueryString();
        if (null != queryStr && !"".equals(queryStr)) {
            buffer.append("?");
            buffer.append(queryStr);
        }
        return buffer.toString();
    }
    
    public static String removeParameter(String url, String paramName) {
    	if(null == paramName || "".equals(paramName)){
    		return url;
    	}
    	if (url.indexOf(paramName) < 0){
            return url;
        }
        ParamOffsets paramOffsets = getParamOffsets(url, paramName);
        if (paramOffsets != null) {
            StringBuilder sb = new StringBuilder(url);
            int paramEnd = paramOffsets.nameStart + paramName.length(); // If param has no value, remove up to and
            // including =
            if (paramOffsets.valStart >= 0){
                paramEnd = paramOffsets.valEnd; // If param has a value, remove up to and include value end.
            }
            int separatorIndex = paramOffsets.nameStart - 1; // Index of ? or & just before param name.
            char separator = sb.charAt(separatorIndex);
            sb.replace(separatorIndex, paramEnd + 1, "");
            if (sb.length() > separatorIndex){
                sb.setCharAt(separatorIndex, separator);
            }
            return sb.toString();
        }
        return url;
    }
    
    private static class ParamOffsets {
        int nameStart = -1;
        int valStart = -1;
        int valEnd = -1;
    }

    private static ParamOffsets getParamOffsets(String url, String paramName) {
        if(null == paramName || "".equals(paramName)){
        	return null;
        }
        int lastCharIndex = url.length() - 1;
        char paramSeparator = '?';
        int startIndex = 0;
        while (startIndex <= lastCharIndex) {
            int nextParamIndex = url.indexOf(paramSeparator, startIndex);
            if (nextParamIndex < 0){
                return null;
            }
            int paramNameIndex = nextParamIndex + 1;
            int charAfterParamNameIndex = paramNameIndex + paramName.length();
            if (charAfterParamNameIndex > lastCharIndex){
                return null;
            }
            if ((url.charAt(charAfterParamNameIndex) == '=')
                    && url.regionMatches(paramNameIndex, paramName, 0, paramName.length())) {
                ParamOffsets paramOffsets = new ParamOffsets();
                paramOffsets.nameStart = paramNameIndex;
                int paramValueIndex = charAfterParamNameIndex + 1;
                if ((paramValueIndex <= lastCharIndex) && (url.charAt(paramValueIndex) != '&')) {
                    paramOffsets.valStart = paramValueIndex;
                    nextParamIndex = url.indexOf('&', paramValueIndex);
                    if (nextParamIndex == -1) {
                        paramOffsets.valEnd = url.length() - 1;
                    } else {
                        paramOffsets.valEnd = nextParamIndex - 1;
                    }
                }
                return paramOffsets;
            }
            paramSeparator = '&';
            startIndex = paramNameIndex;
        }
        return null;
    }
}
