package com.lnlr.common.response;

/**
 * 操作成功Controller返回結果
 *
 * @author 雷洪飞
 */
public class SuccessResponse extends ResponseResult implements Response {

    /**
     * 返回消息
     */
    private String message;

    public SuccessResponse() {
        this.message = ResponseEnum.SUCCESS_CODE.getMessage();
    }

    public SuccessResponse(String message) {
        this.message = message;
    }


    /**
     * 返回结果编码
     *
     * @return
     */
    @Override
    public Integer getStatus() {
        return ResponseEnum.SUCCESS_CODE.getCode();
    }

    /**
     * 返回结果信息
     *
     * @return
     */
    @Override
    public String getMessage() {
        return this.message;
    }
}
