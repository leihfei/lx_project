package com.lnlr.pojo.vo.auth;

import lombok.Data;

import java.util.List;

/***
 * @author leihfei
 * @description 角色菜单关系返回视图
 * @date 16:52:37 2019-04-15
 */
@Data
public class RoleMouleViewVO {
    /**
     * 选中的节点
     */
    private List<String> defuleSelectKeys;

    /**
     * 所有的菜单数据
     */
    private List<ModuleNgZorroTreeVO> modules;
}
