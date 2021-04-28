package com.lnlr.pojo.vo.auth;

import lombok.Data;

/**
 * 功能说明：用户列表返回对象
 *
 * @author wangwt
 * @date 2019-04-10 16:22
 */
@Data
public class UserListVO {

    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 工号
     */
    private String number;

}
