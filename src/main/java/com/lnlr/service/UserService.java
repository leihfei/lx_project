package com.lnlr.service;


import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.jpa.model.NgData;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.pojo.entity.SysUser;
import com.lnlr.pojo.param.base.AuthorityParam;
import com.lnlr.pojo.param.base.CheckPassParam;

import java.util.List;
import java.util.Map;

/**
 * @author:leihfei
 * @description: 用户管理相关业务接口
 * @date:Create in 15:17 2018/11/26
 * @email:leihfein@gmail.com
 */
public interface UserService {

    /**
     * @param
     * @return
     * @author: leihfei
     * @description 查询所有用户数据
     * @date: 19:47 2018/8/30
     * @email: leihfein@gmail.com
     */
    List<SysUser> findAll();


    /**
     * 通过关键字查询用户是否存在
     *
     * @param keyword
     * @return
     */
    SysUser findKeyword(String keyword);

    /**
     * 通过id查询用户
     *
     * @param id
     * @return
     */
    SysUser view(String id);

    /**
     * 分页查询用户数据
     *
     * @param ngPager
     * @return
     */
    NgData page(NgPager ngPager);

    /**
     * 更新密码
     *
     * @param checkPassParam
     * @return
     */
    boolean updatePass(CheckPassParam checkPassParam);

    /**
     * 检查旧密码
     *
     * @param checkPassParam
     * @return
     */
    boolean checkPass(CheckPassParam checkPassParam);

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    SysUser findById(String id);


    /**
     * 删除用户
     *
     * @param idEntity
     */
    void delete(IdEntity idEntity);

    /**
     * 更新yoghurt状态
     *
     * @param idEntity
     */
    void updateStatus(IdEntity idEntity);

    /**
     * 给用户分配角色
     *
     * @param authorityParam
     */
    void authority(AuthorityParam authorityParam);

    /**
     * 给用户撤销角色
     *
     * @param authorityParam
     */
    void unAuthority(AuthorityParam authorityParam);

    /**
     * 用户密码重置
     *
     * @param entity
     */
    void resetPassword(IdEntity entity);


    /**
     * 判断是否为超级管理员 是-rue ,否-false
     *
     * @param id 用户id
     */
    Boolean findUserIsManager(String id);


    /**
     * 所有用户id，name
     *
     * @return 用户
     */
    List<Map<String, String>> list();

}
