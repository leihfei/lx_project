package com.lnlr.common.utils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author:leihfei
 * @description:
 * @date:Create in 20:13 2018/9/19
 * @email:leihfein@gmail.com
 */
public class UrlExclusionsUtils {

    public static boolean partten(ServletRequest servletRequest, ServletResponse servletResponse, List<String> exclusions) {
        // 在jwt，authorinzation中取出jwt，但是又得排除
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        // 请求地址
        String servletPath = req.getServletPath();
        for (int i = exclusions.size() - 1; i >= 0; i--) {
            if (servletPath.contains(exclusions.get(i))) {
                return true;
            }
        }
        return false;
    }
}
