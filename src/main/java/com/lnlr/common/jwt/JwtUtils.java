package com.lnlr.common.jwt;

import com.lnlr.common.constains.SystemConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * @author 雷洪飞 on 9:59 2018/1/5.
 * desc jwt加解密工具类
 */
public class JwtUtils {

    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * 创建JWT
     *
     * @param name           登陆用户名
     * @param userId         登录用户id
     * @param audience       jwtid
     * @param issuer         用于说明该JWT是由谁签发的
     * @param TTLMillis      过期时间
     * @param base64Security jwtbase64
     * @return
     */
    public static String createJWT(String name,
                                   String userId,
                                   String audience,
                                   String issuer,
                                   long TTLMillis,
                                   String base64Security) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim(SystemConstants.JWT_LOGIN_USERNAME, name)
                .claim(SystemConstants.JWT_LOGIN_USER, userId)
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + (TTLMillis * 1000);
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        //生成JWT
        return builder.compact();
    }


    /**
     * @param audience jwt配置文件
     * @param userId   用户id
     * @param userName 用户名称
     * @return java.lang.String
     * @author: leihfei
     * @description
     * @date: 15:59 2018/11/27
     * @email: leihfein@gmail.com
     */
    public static String createJWt(Audience audience, String userId, String userName) {
        // 重新创建token返回
        String jwt = JwtUtils.createJWT(
                userId,
                userName,
                audience.getClientId(),
                audience.getName(),
                audience.getExpiresSecond(),
                audience.getBase64Secret());
        return SystemConstants.TOKEN_TYPE.concat(jwt);
    }

}
