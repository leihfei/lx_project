package com.lnlr.common.utils;

import com.lnlr.common.entity.HashPassword;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author:leihfei
 * @description: 密码处理工具
 * @date:Create in 11:29 2018/10/19
 * @email:leihfein@gmail.com
 */
@Slf4j
public class PassUtils {

    /**
     * 散列次数
     */
    private static final int INTERATIONS = 1024;

    /**
     * 盐值长度
     */
    private static final int SALT_SIZE = 8;


    /**
     * @author: leihfei
     * @description: 对密码进行加密
     * @param: 密码
     * @return: hash密码
     * @date: 14:04 2018/8/7
     * @email: leihfein@gmail.com
     */
    public static HashPassword encryptPassword(String plainPassword) {
        HashPassword result = new HashPassword();
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        result.salt = Encodes.encodeHex(salt);
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, INTERATIONS);
        result.password = Encodes.encodeHex(hashPassword);
        return result;
    }

    /**
     * @param plainPassword: 铭文密码*
     * @param password       密文密码
     * @param salt           盐值
     * @author: leihfei
     * @description: 盐值密码
     * @return: 真假
     * @date: 14:05 2018/8/7
     * @email: leihfein@gmail.com
     */
    public static boolean validatePassword(String plainPassword, String password, String salt) {
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), Encodes.decodeHex(salt), INTERATIONS);
        String oldPassword = Encodes.encodeHex(hashPassword);
        return password.equals(oldPassword);
    }

    /**
     * @param text 明文
     * @param key  秘钥
     * @return java.lang.String
     * @author leihfei
     * @description 进行md5加密
     * @date 10:59 2019/2/15
     */
    public static String md5(String text, String key) {
        //加密后的字符串
        String encodeStr = DigestUtils.md5Hex(text);
        log.info("MD5加密后的字符串为:encodeStr=" + encodeStr);
        return encodeStr;
    }

    /**
     * @param text 明文
     * @param key  密钥
     * @param md5  密文
     * @return boolean
     * @author leihfei
     * @description 校验md5是否正确
     * @date 11:00 2019/2/15
     */
    public static boolean verify(String text, String key, String md5) {
        //根据传入的密钥进行验证
        String md5Text = md5(text, key);
        if (md5Text.equalsIgnoreCase(md5)) {
            log.info("密码校验成功!");
            return true;
        }

        return false;
    }
}


