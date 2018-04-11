package com.ailikes.util.crypto;

import java.io.IOException;

/**
 *
 *
 */
public class JrptCryptoUtils
{

    public final static String KEY = "map!@#$1";

    /**
     * 加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception
    {
        return DesUtil.encrypt(data, KEY);
    }

    /**
     * 解密
     *
     * @param data
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data) throws IOException, Exception
    {
        return DesUtil.decrypt(data, KEY);
    }
}
