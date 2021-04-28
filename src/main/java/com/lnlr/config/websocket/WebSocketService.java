package com.lnlr.config.websocket;

import com.lnlr.config.websocket.conf.SocketConfEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @author:leihfei
 * @description:
 * @date:Create in 10:32 2018/8/28
 * @email:leihfein@gmail.com
 */
@Component
public class WebSocketService {


    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private SocketConfEntity socketConfEntity;

    /**
     * 推送数据
     *
     * @param url
     * @param message
     */
    public void send(String url, String message) {
        template.convertAndSend(socketConfEntity.getTopic() + url, message);
    }
}
