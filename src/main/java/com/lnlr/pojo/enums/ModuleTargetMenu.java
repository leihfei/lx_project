package com.lnlr.pojo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 菜单管理目标枚举
 * @date 2019-04-10 13:57:31
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ModuleTargetMenu {
    INNER_OPEN(0, "系统内部打开"),
    OUTTER_OPEN(1, "新窗口打开"),
    OTHER_OPEN(2, "第三方系统打开");
    private int code;

    private String message;

    public static String getMessage(int code) {
        for (ModuleTargetMenu value : ModuleTargetMenu.values()) {
            if (code == value.getCode()) {
                return value.getMessage();
            }
        }
        return null;
    }

}
