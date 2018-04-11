package com.ailikes.util.http;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 功能描述: 针对URL地址参数进行操作     
 * 
 * date:   2018年4月11日 下午4:34:37
 * @author: ailikes
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class RequestUtil {
	
    /**
     * 
     * 功能描述: 将paramName和paramValue拼接到buffer后面
     *
     * @param url
     * @param paramName
     * @param paramValue
     * @return String
     * date:   2018年4月11日 下午4:34:55
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
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
	 * 功能描述: 根据http请求，获取发送请求的域名
	 *
	 * @param request
	 * @return String
	 * date:   2018年4月11日 下午4:35:05
	 * @author: ailikes
	 * @version 1.0.0
	 * @since: 1.0.0
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
	 * 功能描述: 根据http请求，获取发送请求的域名
	 *
	 * @param url
	 * @return String
	 * date:   2018年4月11日 下午4:35:15
	 * @author: ailikes
	 * @version 1.0.0
	 * @since: 1.0.0
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
	 * 功能描述: 按照targeturl全路径查找其申请资源
	 *
	 * @param url
	 * @return String
	 * date:   2018年4月11日 下午4:35:24
	 * @author: ailikes
	 * @version 1.0.0
	 * @since: 1.0.0
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
	 * 功能描述:根据request构造请求地址 
	 *
	 * @param request
	 * @return String
	 * date:   2018年4月11日 下午4:35:33
	 * @author: ailikes
	 * @version 1.0.0
	 * @since: 1.0.0
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
     * 
     * 功能描述: 将当前请求中的QueryString拼接到指定地址后
     *
     * @param request
     * @param url
     * @return String
     * date:   2018年4月11日 下午4:35:44
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
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
    /**
     * 
     * 功能描述: 移除URL中参数
     *
     * @param url
     * @param paramName
     * @return String
     * date:   2018年4月11日 下午4:36:06
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
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
    /**
     * 
     * 功能描述: 获取URL中参数
     *
     * @param url
     * @param paramName
     * @return String
     * date:   2018年4月11日 下午4:36:06
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
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
