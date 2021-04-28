package com.lnlr.pojo.vo.auth;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author:leihfei
 * @description 登录成功返回前端数据
 * @date:Create in 13:02 2018/9/17
 * @email:leihfein@gmail.com
 */
@Data
public class LoginResultVO {

    /**
     * 登陆对象
     */
    private UserVO user;

    /**
     * token信息
     */
    private String token;

    /**
     * 菜单列表
     */
    private List menus;

    /**
     * 权限点
     */
    private Set<String> auths;


    public LoginResultVO() {
    }

    public LoginResultVO(UserVO user, String token) {
        this.user = user;
        this.token = token;
    }
}
