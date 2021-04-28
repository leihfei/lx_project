package com.lnlr.common.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author leihf
 * @date 18-7-11
 * @email leihfein@gmail.com
 * @descent jwt 配置数据
 */
@Component
@Data
@ConfigurationProperties(prefix = "audience")
public class Audience {
    /**
     * clientID
     */
    private String clientId;

    /**
     * 64位
     */
    private String base64Secret;

    private String name;

    private int expiresSecond;

}
