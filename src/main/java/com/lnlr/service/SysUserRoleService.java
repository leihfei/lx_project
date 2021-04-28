package com.lnlr.service;

import com.lnlr.pojo.entity.SysUserRole;

import java.util.List;

/**
 * @author:leihfei
 * @description: 用户角色业务接口
 * @date:Create in 9:54 2018/11/29
 * @email:leihfein@gmail.com
 */

public interface SysUserRoleService {
    /**
     * 通过用户id查询角色列表
     *
     * @param id 用户ID
     */
    List<SysUserRole> findAllRoleListByUserId(String id);
}
