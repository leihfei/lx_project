package com.lnlr.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author:leihfei
 * @description 部门层级工具类
 * @date:Create in 22:38 2018/9/3
 * @email:leihfein@gmail.com
 */
public class LevelUtils {

    private final static String SEPARATOR = ".";

    public final static String ROOT = "0";

    public static String calculatelevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
