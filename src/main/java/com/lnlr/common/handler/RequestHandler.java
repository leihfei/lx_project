package com.lnlr.common.handler;


import com.lnlr.pojo.entity.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:leihfei
 * @description 本地线程
 * 用于保存当前登录用户
 * request信息
 * @date:Create in 10:58 2018/10/26
 * @email:leihfein@gmail.com
 */
public class RequestHandler {
    /**
     * 用户列表
     */
    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<>();

    /**
     * 请求request列表
     */
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    /**
     * 添加用户
     *
     * @param sysUser
     */
    public static void add(SysUser sysUser) {
        userHolder.set(sysUser);
    }

    /**
     * 添加请求
     *
     * @param request
     */
    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    /**
     * 得到当前用户
     *
     * @return
     */
    public static SysUser getCurrentUser() {
        return userHolder.get();
    }

    /**
     * 得到当前请求
     *
     * @return
     */
    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    /**
     * 移除数据
     */
    public static void remove() {
        userHolder.remove();
        requestHolder.remove();
    }
}
