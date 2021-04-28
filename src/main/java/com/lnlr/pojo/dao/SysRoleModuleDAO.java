package com.lnlr.pojo.dao;


import com.lnlr.pojo.base.BaseDAO;
import com.lnlr.pojo.entity.SysRoleModule;

import java.util.List;
import java.util.Set;

/**
 * @author leihfei
 * @description 角色菜单持久化接口
 * @date 13:47:38 2019-04-10
 */
public interface SysRoleModuleDAO extends BaseDAO<SysRoleModule> {


    /**
     * @param moduleIds 菜单IDS
     * @return java.util.List<com.xzl.security.pojo.master.entity.SysRoleModule>
     * @author leihfei
     * @description 通过菜单id查询数据
     * @date 13:47:14 2019-04-10
     */
    List<SysRoleModule> findAllByModuleIdIn(Set<String> moduleIds);

    /**
     * @param ids module IDS
     * @return void
     * @author leihfei
     * @description 通过moduleid删除
     * @date 15:20:51 2019-04-10
     */
    void deleteAllByModuleIdIn(Set<String> ids);

    /**
     * @param id moduleid
     * @return void
     * @author leihfei
     * @description 通过id删除
     * @date 15:22:13 2019-04-10
     */
    void deleteAllByModuleId(String id);

    /**
     * @param roleId 角色id
     * @return void
     * @author leihfei
     * @description 通过角色id删除数据
     * @date 15:39:47 2019-04-10
     */
    void deleteAllByRoleId(String roleId);

    /**
     * @param roleId 角色id
     * @return java.util.List<com.xzl.security.pojo.master.entity.SysRoleModule>
     * @author leihfei
     * @description 通过角色id查询所有数据
     * @date 16:51:03 2019-04-15
     */
    List<SysRoleModule> findAllByRoleId(String roleId);

    /**
     * @param roleIds 角色id
     * @return java.util.List<com.xzl.security.pojo.master.entity.SysRoleModule>
     * @author leihfei
     * @description 通过角色id查询所有数据
     * @date 13:19:55 2019-04-16
     */
    List<SysRoleModule> findAllByRoleIdIn(Set<String> roleIds);

    List<SysRoleModule> findAllByModuleId (String moduleIds);
}
