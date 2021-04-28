package com.lnlr.pojo.vo.auth;

import lombok.Data;

/**
 * @author leihfei
 * @description 权限划分返回视图
 * @date 20:04:31 2019-04-15
 */
@Data
public class RoleVO {

    /**
     * id
     */
    private String key;

    /**
     * 角色名称
     */
    private String title;

    /**
     * 状态值： left/right
     */
    private String direction;
}
