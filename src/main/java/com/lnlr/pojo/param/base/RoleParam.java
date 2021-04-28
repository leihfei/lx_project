package com.lnlr.pojo.param.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 角色提交对象
 * @date 2019-04-10 15:03:19
 */
@Data
@ApiModel(value = "角色model")
public class RoleParam {

    private String id;
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不允许为空")
    @ApiModelProperty(value = "角色名称", required = true)
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = false)
    private String remark;

    /**
     * 删除状态: 0-删除，1-正常
     */
    @ApiModelProperty(hidden = true)
    private Integer status = 1;

    /**
     * 角色大类型（0.老师的角色，1.学生的角色）
     */
    @ApiModelProperty(hidden = true)
    private Integer type;
}
