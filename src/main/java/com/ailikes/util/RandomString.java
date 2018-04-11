package com.ailikes.util;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成随机字符串工具类
 *
 * @author yangwenqi
 * @version 1.0.0
 */
public class RandomString
{

    private static final Logger logger = LoggerFactory.getLogger(RandomString.class);

    private RandomString()
    {
    }

    /**
     * 在给定的字符数组中获取指定长度的随机字符串
     *
     * @param num 指定获取的字符长度
     * @param str 给定的字符数组
     * @return 指定长度的随机字符串
     */
    public static String getRandom(int num, String[] str)
    {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < num; i++)
        {
            int number = random.nextInt(str.length);
            sb.append(str[number]);
        }

        return sb.toString();
    }

    /**
     * 在0-9、a-z、A-Z中随机获取指定长度的字符组成随机字符串
     *
     * @param num 指定获取的字符长度
     * @return 随机字符串
     *
     */
    public static String getRandomString(int num)
    {
        String[] str
                 =
                {
                    "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
                    "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3",
                    "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
                    "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
                    "Y", "Z"
                };
        return getRandom(num, str);
    }

    /**
     * 
     * 功能描述: 返回随机数
     * 
     * @return int
     * @version 1.0.0
     * @author 王佳田
     */
    public static int getRandomInt(){
        int max=1000000;
        int min=100000;
        Random random = new Random();

        return random.nextInt(max)%(max-min+1) + min;
    }
}
