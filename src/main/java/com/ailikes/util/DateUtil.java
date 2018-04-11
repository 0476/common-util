package com.ailikes.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ailikes.util.string.StringUtil;

/**
 *
 * 功能描述: 时间工具类
 *
 * @version 1.0.0
 * @author 徐大伟
 */
public class DateUtil
{
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	public static final DateTime START = DateTime.parse("2014-01-01");
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";



	public static void main(String[] args) {
		Date date = new Date();
		System.out.print(str2Date("2014-01-01"));
//		System.out.println(buildMinOfDate(date));
//		System.out.println(buildMaxOfDate(date));

	}
	/**
	 * 转换字符串为日期格式（pattern自行指定）
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static Date str2Date(String str, String pattern) {
		try {
			DateTime dt = DateTime.parse(str, DateTimeFormat.forPattern(pattern));
			return dt.toDate();
		} catch (Exception e) {
			logger.error("Failed parse to Date type, string is " + str, e);
			return null;
		}

	}
	
	/**
	 * 转换字符串为不带时间的日期格式，如"2015-08-10"
	 * 
	 * @param str
	 * @return
	 */
	public static Date str2Date(String str) {
		return str2Date(str, DATE_PATTERN);
	}

	/**
	 * 转换字符串为带时间的日期格式，如"2015-08-10 20:11:20"
	 * 
	 * @param str
	 * @return
	 */
	public static Date str2Datetime(String str) {
		return str2Date(str, DATETIME_PATTERN);
	}
	
	/**
	 * 计算指定日期的最小时间，如2015-08-01 00:00:00
	 * 
	 * @param d
	 * @return
	 */
	public static Date buildMinOfDate(Date d) {
		DateTime time = new DateTime(d);
		return time.millisOfDay().withMinimumValue().toDate();
	}

	/**
	 * 计算指定日期的最大时间，如2015-08-01 23:59:59
	 * 
	 * @param d
	 * @return
	 */
	public static Date buildMaxOfDate(Date d) {
		DateTime time = new DateTime(d);
		return time.millisOfDay().withMaximumValue().toDate();
	}

	
    /**
     *
     * 功能描述: 获取当前时间
     *
     * @param format
     * @return String
     * @version 1.0.0
     * @author 徐大伟
     */
    public static String getTodayByFormat(String format)
    {
        java.util.Calendar c = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        String szSBDate = sdf.format(c.getTime());
        return szSBDate;
    }

    /**
     *
     * 功能描述: 传入空返回当前时间
     *
     * @param dateStr
     * @return Date
     * @version 1.0.0
     * @author 徐大伟
     */
    public static Date getDateFromString(String dateStr)
    {
        Date date = new Date();
        if (StringUtil.isNotBlank(dateStr))
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try
            {
                date = format.parse(dateStr);
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        return date;
    }
    
    public static String date2String(Date dateStr)
    {
        if (null != dateStr)
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy年-MM月-dd日");
            try
            {
                return format.format(dateStr);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 功能描述:传入空，返回当前时间字符串
     *
     * @param date
     * @return String
     * @version 1.0.0
     * @author 徐大伟
     */
    public static String dateToString(Date date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null == date)
        {
            date = new Date();
        }
        String str = format.format(date);
        return str;
    }

    /**
     * 功能描述: 获取年月字符串
     *
     * @return String
     * @version 1.0.0
     * @author 徐大伟
     */
    public static String getCurrentYearMonth()
    {
        Calendar cal = Calendar.getInstance();// 使用日历类
        int year = cal.get(Calendar.YEAR);// 得到年
        int month = cal.get(Calendar.MONTH) + 1;// 得到月
        return String.valueOf("" + year + month);
    }

}
