package com.ailikes.util.string;

import java.util.UUID;

/**
 * 
 * 功能描述: 生成统一格式的UUID码      
 * 
 * date:   2018年4月11日 下午5:02:24
 * @author: ailikes
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class UuidUtil {
    /**
     * 
     * 功能描述: 生成无"-"的UUID值
     *
     * @return String
     * date:   2018年4月11日 下午5:02:37
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
