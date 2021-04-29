package com.lnlr.pojo.dao;


import com.lnlr.pojo.base.BaseDAO;
import com.lnlr.pojo.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * @author:leihfei
 * @description 用户管理持久层
 * @date:Create in 22:30 2018/9/3
 * @email:leihfein@gmail.com
 */
public interface SysUserDAO extends BaseDAO<SysUser> {

    /**
     * 通过用户名查询
     *
     * @param username
     */
    SysUser findAllByUsername(String username);

    SysUser findByTelphone(String telphone);


    List<SysUser> findAllByIdIn(Set<String> ids);

    void deleteByIdIn(Iterable<String> ids);


    /**
     * 通过用户类型查询
     *
     * @param userType 用户类型
     */
    List<SysUser> findAllByUserType(Integer userType);
}
