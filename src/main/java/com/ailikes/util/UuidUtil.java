package com.ailikes.util;

import java.util.UUID;

/**
 * 
 * Copyright (C), 2002-2014,北京天道和元科技有限公司
 * FileName: UuidUtil.java
 * Author:   陈天宇
 * Date:     2014年12月31日 上午10:32:49
 * Description: 生成统一格式的UUID码      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
public class UuidUtil {
	/**
	 * 
	 * 功能描述: 生成无“-”的UUID值<br>
	 *
	 * @Author:   陈天宇
	 * @Date:     2014年12月31日 上午10:36:08
	 * @Description: //模块目的、功能描述      
	 * @History: //修改记录
	 * <author>      <time>      <version>    <desc>
	 * 修改人姓名             修改时间            版本号                  描述
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
