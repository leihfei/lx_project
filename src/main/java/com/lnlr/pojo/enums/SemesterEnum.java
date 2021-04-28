package com.lnlr.pojo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 学期设置枚举
 * @date 2019-04-10 13:57:31
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum SemesterEnum {
    FIRST_SEMESTER(0, "第一学期"),
    SECOND_SEMESTER(1, "第二学期"),
    SMALL_SEMESTER(2, "小学期");
    private int code;

    private String message;

    public static String getMessage(int code) {
        for (SemesterEnum value : SemesterEnum.values()) {
            if (code == value.getCode()) {
                return value.getMessage();
            }
        }
        return null;
    }

}
