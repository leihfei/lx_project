package com.lnlr.pojo.param.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;


/**
 * @author:leihfei
 * @description 登录参数
 * @date:Create in 17:43 2018/9/14
 * @email:leihfein@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "登录参数")
public class LoginParam {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private boolean rememberMe = false;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 请求参数
     */
    @ApiModelProperty(value = "查询参数", required = true)
    @NotBlank(message = "请求参数不能为空")
    private String queryParam;

}
