package com.lnlr.common.entity;

import lombok.Data;

/**
 * @author:leihfei
 * @description hash编码的密码数据
 * @date:Create in 15:36 2018/9/6
 * @email:leihfein@gmail.com
 */
@Data
public class HashPassword {
    /**
     * 盐值
     */
    public String salt;
    /**
     * 密码
     */
    public String password;
}