package com.lnlr.common.response;

/**
 * Controller 返回值顶层接口
 *
 * @author ycitss
 */
public interface Response {
    /**
     * 返回结果编码
     *
     * @return
     */
    Integer getStatus();

    /**
     * 返回结果信息
     *
     * @return
     */
    String getMessage();
}
