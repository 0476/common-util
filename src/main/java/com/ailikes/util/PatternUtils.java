package com.ailikes.util;

import java.util.regex.Pattern;
/**
 * 
 * 功能描述: 正则表达式
 *
 * @date 2016-11-10
 * @since 1.0.0
 * @version 1.0.0
 * @author 徐大伟
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
     * @param phoneNo
     * @return boolean
     * @date 2016-11-10
     * @since 1.0.0
     * @version 1.0.0
     * @author 徐大伟
     */
    public static boolean isMobile(String phoneNo){
        return Pattern.matches(IS_MOBILE, phoneNo);
    }
    
    /**
     * 
     * 功能描述:是否是数字 
     * 
     * @param phoneNo
     * @return boolean
     * @date 2016-11-10
     * @since 1.0.0
     * @version 1.0.0
     * @author 徐大伟
     */
    public static boolean isNumber(String Number){
        return Pattern.matches(IS_NUMBER, Number);
    }
    
}
