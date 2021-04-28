package com.lnlr.pojo.param.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author:leihfei
 * @description: 检查密码、更新密码实体类
 * @date:Create in 0:40 2018/10/26
 * @email:leihfein@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "检查密码、更新密码")
public class CheckPassParam {
    @ApiModelProperty(value = "用户id")
    @NotNull
    private String userId;

    @ApiModelProperty(value = "旧密码")
    @NotBlank(message = "旧密码不能为空")
    @NotNull
    private String oldPass;

    @ApiModelProperty(value = "新密码")
    private String newPass;
}
