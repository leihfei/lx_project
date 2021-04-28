package com.lnlr.config.websocket.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Data
public class SocketConfEntity {
    /**端点*/
    @Value("${socket.point}")
    private String point;

    /**
     * 默认主题
     */
    @Value("${socket.topic}")
    private String topic;

    /**
     * 后缀
     */
    @Value("${socket.prefixes}")
    private String prefixes;
}
