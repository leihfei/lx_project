package com.lnlr.pojo.param.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 角色授权
 * @date 2019-04-10 15:34:51
 */
@Data
@ApiModel(value = "角色授权")
public class RoleAuthParam {

    /**
     * 角色id
     */
    @NotBlank(message = "角色不允许为空")
    @ApiModelProperty(value = "角色id", required = true)
    private String roleId;

    /**
     * 菜单id
     */
    @NotNull(message = "菜单不允许为空")
    @ApiModelProperty(value = "菜单id列表", required = true)
    private Set<String> moduleIds;
}
