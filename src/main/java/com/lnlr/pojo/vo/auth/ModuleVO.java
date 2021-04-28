package com.lnlr.pojo.vo.auth;

import lombok.Data;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 菜单查询返回视图
 * @date 2019-04-10 13:48:32
 */
@Data
public class ModuleVO {

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 上一级菜单
     */
    private String parentName;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 状态
     */
    private String status;

    /**
     * 链接
     */
    private String url;

    /**
     * 目标名称
     */
    private String targetName;

    /**
     * 排序等级
     */
    private Integer showLevel;

    /**
     * 图标
     */
    private String icon;

    /**
     * 备注
     */
    private String remark;
}
