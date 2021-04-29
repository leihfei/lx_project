package com.lnlr.pojo.param.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 菜单请求对象
 * @date 2019-04-10 11:24:04
 */
@Data
@ApiModel(value = "菜单")
public class ModuleParam {
    private String id;

    /**
     * 模块名称
     */
    @NotBlank(message = "菜单名称不允许为空")
    @ApiModelProperty(value = "菜单名称", required = true)
    private String name;

    /**
     * 父节点
     */
    @NotBlank(message = "上一级菜单不允许为空")
    @ApiModelProperty(value = "上一级菜单", required = true)
    private String parentId;

    /**
     * 显示优先级
     */
    @NotNull(message = "优先级不允许为空")
    @ApiModelProperty(value = "优先级", required = true)
    private Integer showLevel;

    /**
     * 模块状态：1-可见，0-隐藏
     */
    @ApiModelProperty(value = "是否可见:1-可见，0-隐藏", notes = "1-可见，0-隐藏")
    private Integer status;

    /**
     * 链接
     */
    @ApiModelProperty(value = "链接", required = false)
    private String url;

    /**
     * 目标
     * 0-内部系统
     * 1-新窗口打开
     * 2-第三方连接
     */
    @ApiModelProperty(value = "目标:0-内部系统,1-新窗口打开,2-第三方连接", required = false, notes = "0-内部系统,1-新窗口打开,2-第三方连接")
    private Integer targetType = 0;

    /**
     * 类型
     * 0-菜单
     * 1-按钮
     * 2-其他
     */
    @NotNull(message = "类型不允许为空")
    @ApiModelProperty(value = "类型:0-菜单，1-按钮，2-其他", required = true, notes = "0-菜单，1-按钮，2-其他")
    private Integer type;

    /**
     * 图标
     */
    @ApiModelProperty(value = "图标", required = false)
    private String icon;

    /**
     * 备注
     */
    private String remark;
}
