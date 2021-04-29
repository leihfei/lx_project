package com.lnlr.pojo.vo.auth;

import lombok.Data;

/**
 * @author:leihfei
 * @description 系统用户返回视图
 * @date:Create in 17:05 2018/9/6
 * @email:leihfein@gmail.com
 */
@Data
public class LoginUserVO {

    private String id;


    /**
     * 系统用户名称
     */
    private String realName;


    /**
     * 用户类型：
     */
    private Integer userType;

    /**
     * 性别：0-男，1-女
     */
    private Integer sex;

    /**
     * 电话
     */
    private String telphone;

    /**
     * 邮箱
     */
    private String email;

}
