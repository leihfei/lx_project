package com.lnlr.common.utils;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class MD5Util {
    /**
     * @param pwd 需要加密的字符串
     *            字母大小写(false为默认小写，true为大写)
     * @param bit 加密的类型（16,32,64）
     * @return
     */
    public static String getMD5(String pwd, boolean isUpper, Integer bit) {
        String md5 = new String();
        try {
            // 创建加密对象
            MessageDigest md = MessageDigest.getInstance("md5");
            if (bit == 64) {
                BASE64Encoder bw = new BASE64Encoder();
                String bsB64 = bw.encode(md.digest(pwd.getBytes("utf-8")));
                md5 = bsB64;
            } else {
                // 计算MD5函数
                md.update(pwd.getBytes());
                byte b[] = md.digest();
                int i;
                StringBuffer sb = new StringBuffer("");
                for (int offset = 0; offset < b.length; offset++) {
                    i = b[offset];
                    if (i < 0) {
                        i += 256;
                    }
                    if (i < 16) {
                        sb.append("0");
                    }
                    sb.append(Integer.toHexString(i));
                }
                md5 = sb.toString();
                if (bit == 16) {
                    //截取32位md5为16位
                    String md16 = md5.substring(8, 24).toString();
                    md5 = md16;
                    if (isUpper) {
                        md5 = md5.toUpperCase();
                    }
                    return md5;
                }
            }
            //转换成大写
            if (isUpper) {
                md5 = md5.toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("md5加密抛出异常！");
        }

        return md5;
    }

    /**
     * @param pwd 明文密码
     * @return java.lang.String
     * @author leihfei
     * @description 默认创建32位，小写字符md5 hash值
     * @date 13:46:25 2019-04-16
     */
    public static String md5Hash(String pwd) {
        return getMD5(pwd, false, 32);
    }

    /**
     * 编码
     *
     * @param bstr bstr
     * @return String
     */
    public static String Base64encode(byte[] bstr) {
        return Base64.encodeBase64String(bstr);
    }

    /**
     * 解码
     *
     * @param str str
     * @return string
     */
    public static byte[] Base64decode(String str) {
        return Base64.decodeBase64(str);
    }

    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws Exception ex
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        /*防止linux下 随机生成key*/
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(encryptKey.getBytes());
        kgen.init(128, random);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));

        return cipher.doFinal(content.getBytes("UTF-8"));
    }

    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception ex
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return Base64encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception ex
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) {
        byte[] decryptBytes = new byte[0];
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            /*防止linux下 随机生成key*/
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(decryptKey.getBytes());
            kgen.init(128, random);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
            decryptBytes = cipher.doFinal(encryptBytes);
            return new String(decryptBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptKey;

    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception ex
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) {
        return aesDecryptByBytes(Base64decode(encryptStr), decryptKey);
    }

    public static void main(String[] args) throws Exception {
        String content = "{'id':'sourse_1585723504201.docx/贵阳职院问题记录11-30.docx','name':'sourse_1585723504201.docx/贵阳职院问题记录11-30.docx'}";
        System.out.println("加密内容：" + content);
        String key = "void";
        System.out.println("加密密钥和解密密钥：" + key);
        String encrypt = aesEncrypt(content, key);
        System.out.println("加密后：" + encrypt);
        String decrypt = aesDecrypt(encrypt, key);
        System.out.println("解密后：" + decrypt);
    }


}
