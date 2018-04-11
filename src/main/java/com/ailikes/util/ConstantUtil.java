package com.ailikes.util;
/**
 * 
 * 全局属性类，该类主要定义全局变量，保证同一变量名称在工程中的一致性<br> 
 * @author chentianyu
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ConstantUtil {
	
	/**
	 * 单点登录会话的逻辑ID
	 * 		<li>Cookie存放域：.ync365.com</li>
	 * 		<li>Cookie生命周期：session</li>
	 * 		<li>Cookie secure：false</li>
	 */
	public static final String YNCSID = "yncsid";
	
	/**
	 * 用户编码
	 * 		<li>Cookie存放域：.ync365.com</li>
	 * 		<li>Cookie生命周期：30分钟</li>
	 * 		<li>Cookie secure：false</li>
	 */
	public static final String YNCUID = "yncuid";
	
	/**
	 * 登录状态
	 * 	<li>Cookie存放域：.ync365.com</li>
	 * 	<li>Cookie生命周期：session</li>
	 * 	<li>Cookie secure：true</li>
	 */
	public static final String YNCTGC = "ynctgc";
	
	/**
	 * 登录状态
	 * 	<li>Cookie存放域：.ync365.com</li>
	 * 	<li>Cookie生命周期：session</li>
	 * 	<li>Cookie secure：true</li>
	 */
	public static final String YNCTGT = "ynctgt";
	
	/**
	 * 单点登录安全域会话的逻辑ID
	 * 	<li>Cookie存放域：.ync365.com</li>
	 * 	<li>Cookie生命周期：session</li>
	 * 	<li>Cookie secure：true</li>
	 */
	public static final String YNCST = "yncst";
	
	/**
	 * 与认证中心交换用户信息的安全票据
	 */
	public static final String TICKET = "tt";
	
	/**
	 * 应用编码
	 */
	public static final String APPCODE = "ac";
	
	
	/**
	 * Cookie默认的存储路径“/”
	 */
	public static final String DEFAULT_COOKIE_PATH = "/";
	/**
	 * 登陆页样式编码
	 */
	public static final String LOGIN_THEME_PARAM = "loginTheme";
	/**
	 * 应用端想认证中心传递的cbu
	 */
	public static final String TARGET_URL_PARAM = "targetUrl";
	
	/**
	 * 定义访问策略
	 * 	<li>LIMIT : 受限资源</li>
	 * 	<li>UNDEFINED : 放行的资源</li>
	 * 	<li>PASS : 无需强制登录</li>
	 */
	public static enum AccessPolicy {
		LIMIT, UNDEFINED, PASS
	}
	
	
	
	
	
	
	
	
	
	
	
}
