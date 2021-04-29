package com.lnlr.pojo.param.base;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author leihfei
 * @date 2021-04-29
 */
@Data
public class UserParam {
    private String id;


    /**
     * 系统用户名称
     */
    @NotBlank(message = "昵称不能为空")
    private String realName;

    /**
     * 状态：1-正常，2-删除,0-冻结
     */
    @NotNull(message = "用户状态不能为空")
    private Integer status;

    /**
     * 用户类型：0-管理员，1-其他
     */
    @NotNull(message = "用户类型不能为空")
    private Integer userType;

    /**
     * 性别：0-男，1-女
     */
    private Integer sex;

    /**
     * 电话
     */
    @NotBlank(message = "登录电话不能为空")
    private String telphone;

    /**
     * 邮箱
     */
    private String email;
}
