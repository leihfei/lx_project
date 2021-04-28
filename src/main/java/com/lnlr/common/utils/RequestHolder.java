package com.lnlr.common.utils;


import com.lnlr.pojo.entity.SysUser;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:leihfei
 * @description 存储本地HTTP请求，当前登录用户
 * @date:Create in 10:13 2018/9/14
 * @email:leihfein@gmail.com
 */
public class RequestHolder {
    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    public static void add(SysUser sysUser) {
        userHolder.set(sysUser);
    }

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static SysUser currentUser() {
        return userHolder.get();
    }

    public static HttpServletRequest currentRequest() {
        return requestHolder.get();
    }

    public static void remove() {
        userHolder.remove();
        requestHolder.remove();
    }
}
