package com.lnlr.pojo.dao;


import com.lnlr.pojo.base.BaseDAO;
import com.lnlr.pojo.entity.SysUserRole;

import java.util.List;
import java.util.Set;

public interface SysUserRoleDAO extends BaseDAO<SysUserRole> {

    /**
     * 查询用户下所有角色
     *
     * @param userId 用户ID
     */
    List<SysUserRole> findAllByUserId(String userId);

    void deleteByUserIdIn(Iterable<String> userIds);

    void deleteAllByRoleIdInAndUserId(Set<String> roleIds, String userId);

    /**
     * 通过角色id和用户id查询角色是否分配
     *
     * @param roleIds
     * @param userId
     */
    List<SysUserRole> findAllByRoleIdInAndUserId(Set<String> roleIds, String userId);


    /**
     * @param roleId 角色id
     * @return java.util.List<com.xzl.security.pojo.master.entity.SysUserRole>
     * @author leihfei
     * @description 通过角色查询角色
     * @date 20:06:10 2019-04-15
     */
    List<SysUserRole> findAllByRoleId(String roleId);

    List<SysUserRole> findAllByUserIdIn(Set<String> userIds);


    void deleteAllByUserIdIn(Set<String> userIds);
}
