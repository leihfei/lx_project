package com.lnlr.config.websocket.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author 雷洪飞
 * @desc websocket消息配置
 * @date 18-7-11
 * @Email leihfein@gmail.com
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConf implements WebSocketMessageBrokerConfigurer {

	/**
	 *  WebSocket 基础信息设置
	 */
	@Autowired
	private SocketConfEntity socketConfEntity;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 配置接受消息
		config.enableSimpleBroker(socketConfEntity.getTopic());
		// 配置发送消息
		config.setApplicationDestinationPrefixes(socketConfEntity.getPrefixes());

	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 配置一个端点，并且设置任何请求都能通过
		registry.addEndpoint(socketConfEntity.getPoint()).setAllowedOrigins("*").withSockJS();
	}
}
