package com.lnlr.service;

import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.jpa.model.NgData;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.common.response.Response;
import com.lnlr.pojo.entity.SysRole;
import com.lnlr.pojo.entity.SysUserRole;
import com.lnlr.pojo.param.base.AuthParam;
import com.lnlr.pojo.param.base.RoleAuthParam;
import com.lnlr.pojo.param.base.RoleAuthUserParam;
import com.lnlr.pojo.param.base.RoleParam;

import java.util.List;
import java.util.Set;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 角色管理业务接口
 * @date 2019-04-10 11:10:17
 */
public interface RoleService {

    /**
     * @param ids 角色id
     * @return void
     * @author leihfei
     * @description 删除角色
     * @date 15:01:39 2019-04-10
     */
    void delete(IdEntity ids);

    /**
     * @param param 角色信息
     * @return void
     * @author leihfei
     * @description 新增角色
     * @date 15:01:56 2019-04-10
     */
    void create(RoleParam param);

    /**
     * @param ngPager 分页查询
     * @return com.xzl.common.jpa.model.NgData
     * @author leihfei
     * @description 分页查询角色
     * @date 15:02:24 2019-04-10
     */
    NgData page(NgPager ngPager);

    /**
     * @param param 角色参数
     * @return void
     * @author leihfei
     * @description 更新角色
     * @date 15:14:39 2019-04-10
     */
    void update(RoleParam param);

    /**
     * @param param 授权参数
     * @return void
     * @author leihfei
     * @description 角色授权
     * @date 15:37:12 2019-04-10
     */
    void auth(RoleAuthParam param);


    /**
     * @param param 用户，角色数据
     * @return void
     * @author leihfei
     * @description 对用户进行授权
     * @date 16:13:52 2019-04-10
     */
    void authUser(RoleAuthUserParam param);

    /**
     * 通过角色id查询角色
     *
     * @param roleIds
     * @return
     */
    List<SysRole> findAllByRoleIds(Set<String> roleIds);

    /**
     * 不是超级管理员的管理员通过组织机构id查询当前组织机构下的所有数据
     *
     * @param ids 已分配的角色id
     * @return
     */
    List<SysRole> findAllByIdNotIn(Set<String> ids);

    /**
     * @param param
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 查询角色的授权数据
     * @date 16:36:54 2019-04-15
     */
    Response viewAuth(IdEntity param);

    /**
     * @param param 参数
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 用户查询自己角色信息
     * @date 19:40:13 2019-04-15
     */
    Response showAuth(IdEntity param);

    /**
     * @param param 角色id
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 角色查询用户是否分配该角色
     * @date 20:03:30 2019-04-15
     */
    Response queryUserAuth(IdEntity param);


    /**
     * @param param 参数
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 移除角色
     * @date 20:28:44 2019-04-15
     */
    Response deleteAuth(AuthParam param);


    /**
     * @param param 参数
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 查询未分配角色的用户
     * @date 20:35:01 2019-04-15
     */
    Response pageQueryUser(NgPager param);


    /**
     * @param param 参数信息
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 用户授权
     * @date 20:54:31 2019-04-15
     */
    Response userAuth(AuthParam param);


    List<SysUserRole> findAllByUserId(String userId);

    /**
     * @param userId 用户
     * @return java.util.Set<java.lang.String>
     * @author leihfei
     * @description 通过用户id查询所拥有的模块id
     * @date 13:21:17 2019-04-16
     */
    Set<String> findUserRolesByUserId(String userId);
}
