package com.ailikes.util;

import java.util.regex.Pattern;
/**
 * 
 * 功能描述: 正则表达式工具类
 * 
 * date:   2018年4月11日 下午5:09:02
 * @author: ailikes
 * @version: 1.0.0
 * @since: 1.0.0
 */
public class PatternUtils
{

    public final static String IS_MOBILE = "^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\\d{8})$";

    public final static String IS_NUMBER = "^[0-9]\\d*$";

    public static boolean matches(String regex, String input)
    {
        return Pattern.matches(regex, input);

    }
    /**
     * 
     * 功能描述: 是否是手机号码
     *
     * @param phoneNo
     * @return boolean
     * date:   2018年4月11日 下午5:08:45
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static boolean isMobile(String phoneNo){
        return Pattern.matches(IS_MOBILE, phoneNo);
    }
    
    /**
     * 
     * 功能描述:是否是数字  
     *
     * @param Number
     * @return boolean
     * date:   2018年4月11日 下午5:08:36
     * @author: ailikes
     * @version 1.0.0
     * @since: 1.0.0
     */
    public static boolean isNumber(String Number){
        return Pattern.matches(IS_NUMBER, Number);
    }
    
}
