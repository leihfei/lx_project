package com.lnlr.config.websocket;

import lombok.Data;

/**
 * @author 雷洪飞
 * websocket消息返回实体类
 */

@Data
public class WSResponse {
	private String code;

	private String status;

	private String message;

	public WSResponse(String code, String status, String message) {
		this.code = code;
		this.message = message;
		this.status = status;
	}

	public WSResponse() {
	}

	public WSResponse(String message) {
		this.code = "200";
		this.status = "Success";
		this.message = message;
	}


}
