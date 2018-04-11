package com.ailikes.util;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * ID生成工具类
 * @author 徐大伟
 *
 */
public final class IdGeneratorUtil {
    /** 全局Id临时变量 */
    private static long      tmpID       = 0;
    /** Id锁定标记 */
    private static boolean   tmpIDlocked = false;
    /** 生成的id的最大允许长度 */
    private static final int MAX_LENGTH  = 6;

    /**
     * 生成唯一有序Id
     * @return Id
     */
    public static long getUniqueId() {
        long ltime = System.nanoTime();
        if (String.valueOf(ltime).length() > MAX_LENGTH) {
            ltime = Long.parseLong(String.valueOf(ltime).substring(0, MAX_LENGTH));
        }
        if (!tmpIDlocked) {
            tmpIDlocked = true;
            if (tmpID < ltime) {
                tmpID = ltime;
            } else {
                tmpID = tmpID + 1;
                ltime = tmpID;
            }
            tmpIDlocked = false;
        }
        return ltime;
    }
    /**
     * 
     * 功能描述: 当前年月日
     * @return String
     * @version 1.0.0
     * @author 徐大伟
     */
    public static String getDate(){
        Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");  
        String dateNowStr = sdf.format(d);  
        return dateNowStr;
    }
    /**
     * 生成订单编号
     * @return
     */
    public static Long getOrderId(){
    	return Long.valueOf(getDate()+getUniqueId());
    }
    public static void main(String[] args) {
        System.out.println(getOrderId());
    }
}
