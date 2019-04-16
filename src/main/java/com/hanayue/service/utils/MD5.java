package com.hanayue.service.utils;


import org.springframework.util.DigestUtils;

/**
 * MD5工具类
 *
 */
public class MD5 {

    /**
     * 将客户端传来的字符串拼接私钥后进行MD5加密  防止撞库暴力破解
     */
    private static final String KEY = "HanAYue";

    /**
     * MD5方法
     *
     * @param text 明文
     * @return 密文
     */
    public static String md5(String text) {
        return DigestUtils.md5DigestAsHex((text+KEY).getBytes());
    }

    public static String md5Only(String text){
        return DigestUtils.md5DigestAsHex((text).getBytes());
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param md5 密文
     * @return true/false
     */
    public static boolean verify(String text, String md5) {
        return md5(text).equalsIgnoreCase(md5);
    }
}
