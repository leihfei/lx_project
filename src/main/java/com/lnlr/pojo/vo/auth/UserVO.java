package com.lnlr.pojo.vo.auth;

import lombok.Data;

/**
 * @author:leihfei
 * @description 系统用户返回视图
 * @date:Create in 17:05 2018/9/6
 * @email:leihfein@gmail.com
 */
@Data
public class UserVO {

    private String id;

    /**
     * 系统登录用户名称
     */
    private String username;

    /**
     * 系统用户名称
     */
    private String realName;

    /**
     * 状态：1-正常，2-删除,0-冻结
     */
    private Integer status;

    /**
     * 用户类型：0-老师，1-学生
     */
    private Integer userType;

}
