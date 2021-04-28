package com.lnlr.common.response;

import lombok.Data;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 返回响应结果
 * @date 2019-04-04 13:35:41
 */
@Data
public class ResponseResult {

    protected Integer status;

    protected String message;
}
