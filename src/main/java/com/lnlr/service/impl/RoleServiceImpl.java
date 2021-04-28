package com.lnlr.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lnlr.common.annonation.ControllerLogAnontation;
import com.lnlr.common.annonation.ServiceLogAnonation;
import com.lnlr.common.constains.LogConstants;
import com.lnlr.common.constains.SystemConstants;
import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.exception.WarnException;
import com.lnlr.common.jpa.model.NgData;
import com.lnlr.common.jpa.model.NgFilter;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.common.jpa.query.DynamicSpecifications;
import com.lnlr.common.jpa.query.PageUtils;
import com.lnlr.common.response.*;
import com.lnlr.common.utils.BeanValidator;
import com.lnlr.common.utils.CopyUtils;
import com.lnlr.common.utils.IpUtils;
import com.lnlr.common.utils.RequestHolder;
import com.lnlr.pojo.dao.*;
import com.lnlr.pojo.entity.*;
import com.lnlr.pojo.param.base.AuthParam;
import com.lnlr.pojo.param.base.RoleAuthParam;
import com.lnlr.pojo.param.base.RoleAuthUserParam;
import com.lnlr.pojo.param.base.RoleParam;
import com.lnlr.pojo.vo.auth.ModuleNgZorroTreeVO;
import com.lnlr.pojo.vo.auth.RoleMouleViewVO;
import com.lnlr.pojo.vo.auth.RoleVO;
import com.lnlr.pojo.vo.auth.UserRoleCheckVO;
import com.lnlr.service.ModuleServices;
import com.lnlr.service.RoleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 角色管理业务接口实现
 * @date 2019-04-10 11:10:37
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final String STUDENTROLE = "402881f37234ee130172355756a20071";

    private final String SUPERROLE = "4028b8816a1f1c4f016a2000dafa0271";

    @Autowired
    private SysRoleDAO roleDAO;

    /**
     * 菜单和角色的绑定关系持久化接口
     */
    @Autowired
    private SysRoleModuleDAO roleModuleDAO;


    @Autowired
    private SysUserRoleDAO userRoleDAO;

    @Autowired
    private SysUserDAO userDAO;


    /**
     * 菜单持久化接口
     */
    @Autowired
    private SysModuleDAO moduleDAO;

    @Autowired
    private ModuleServices moduleService;


    /**
     * @param ids 角色id
     * @return void
     * @author leihfei
     * @description 删除角色
     * @date 15:01:39 2019-04-10
     */
    @Transactional
    @Override
    @ServiceLogAnonation(type = LogConstants.DELETE_STATUS, value = "删除角色", moduleName = "角色管理")
    public void delete(IdEntity ids) {
        BeanValidator.check(ids);
        SysRole sys = roleDAO.findById(ids.getId()).orElse(null);
        if (sys == null) {
            throw new WarnException("数据异常，无法删除!");
        }
        if (ids.getId().equals(STUDENTROLE)) {
            throw new WarnException("学生角色禁止删除!");
        }
        if (ids.getId().equals(SUPERROLE)) {
            throw new WarnException("超管角色禁止删除!");
        }
        // 删除本地数据
        roleDAO.deleteById(sys.getId());
        // 继续删除role和module的绑定
        roleModuleDAO.deleteAllByRoleId(sys.getId());
    }

    /**
     * @param param 角色信息
     * @return void
     * @author leihfei
     * @description 新增角色
     * @date 15:01:56 2019-04-10
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.CREATE_STATUS, value = "新增角色", moduleName = "角色管理")
    public void create(RoleParam param) {
        if (StringUtils.isNotEmpty(param.getId())) {
            throw new WarnException("ID不允许有值!");
        }
        BeanValidator.check(param);
        SysRole existData = roleDAO.findByName(param.getName());
        if (existData != null) {
            throw new WarnException("角色名称已存在");
        }
        SysRole sysRole = CopyUtils.beanCopy(param, new SysRole());
        sysRole.setOperator(RequestHolder.currentUser().getRealName());
        sysRole.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
        roleDAO.save(sysRole);
    }

    /**
     * @param param 角色参数
     * @return void
     * @author leihfei
     * @description 更新角色
     * @date 15:14:39 2019-04-10
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.UPDATE_STATUS, value = "更新角色", moduleName = "角色管理")
    public void update(RoleParam param) {
        if (StringUtils.isEmpty(param.getId())) {
            throw new WarnException("ID不允许为空!");
        }
        SysRole befor = roleDAO.findById(param.getId()).orElse(null);
        if (befor == null) {
            throw new WarnException("数据不存在，无法更新!");
        }
        SysRole existData = roleDAO.findByName(param.getName());
        if (existData != null && !existData.getId().equals(param.getId())) {
            throw new WarnException("角色名称已存在");
        }
        // 进行属性值赋值
        CopyUtils.copyProperties2(param, befor);
        BeanValidator.check(befor);
        befor.setOperator(RequestHolder.currentUser().getRealName());
        befor.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
        roleDAO.save(befor);
    }


    /**
     * @param ngPager 分页查询
     * @return com.xzl.common.jpa.model.NgData
     * @author leihfei
     * @description 分页查询角色
     * @date 15:02:24 2019-04-10
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "分页查询角色", moduleName = "角色管理")
    public NgData page(NgPager ngPager) {
        NgData<SysRole> ngData = new NgData<>(
                roleDAO.findAll(
                        DynamicSpecifications.bySearchFilter(
                                SysRole.class,
                                PageUtils.buildSearchFilter(ngPager)
                        ),
                        PageUtils.buildPageRequest(
                                ngPager,
                                PageUtils.buildSort(ngPager)
                        )
                ), ngPager);
        return ngData;
    }

    /**
     * @param param 授权参数
     * @return void
     * @author leihfei
     * @description 角色授权
     * @date 15:37:12 2019-04-10
     */
    @Transactional
    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "分页查询", moduleName = "角色管理")
    public void auth(RoleAuthParam param) {
        BeanValidator.check(param);
        SysRole sysRole = roleDAO.findById(param.getRoleId()).orElse(null);
        if (sysRole == null) {
            throw new WarnException("角色参数错误，无法授权!");
        }
        // 对role-module进行指定role删除，然后再次添加
        roleModuleDAO.deleteAllByRoleId(param.getRoleId());
        // 继续构建数据进行保存
        List<SysRoleModule> saveAll = Lists.newArrayList();
        List<SysModule> all = moduleDAO.findAll();
        Map<String, SysModule> pMaps = all.stream().collect(Collectors.toMap(SysModule::getId, Function.identity()));
        Set<String> mids = Sets.newHashSet();
        param.getModuleIds().forEach(item -> {
            // 需要去反选数据有的数据
            findP(item, pMaps, mids);
        });
        mids.forEach(item -> {
            SysRoleModule sysRoleModule = new SysRoleModule(param.getRoleId(), item);
            sysRoleModule.setOperator(RequestHolder.currentUser().getRealName());
            sysRoleModule.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
            saveAll.add(sysRoleModule);
        });
        roleModuleDAO.saveAll(saveAll);
    }

    /**
     * @param id    当前节点
     * @param pMaps 所有的节点
     * @param mids  需要保存的数据
     * @return void
     * @author leihfei
     * @description 寻找父节点
     * @date 17:28:36 2019-05-28
     */
    private void findP(String id, Map<String, SysModule> pMaps, Set<String> mids) {
        SysModule sysModule = pMaps.get(id);
        if (sysModule != null) {
            mids.add(id);
            SysModule sm = pMaps.get(sysModule.getParentId());
            if (sm != null && !sm.getId().equals(SystemConstants.DEFAULT_MODULE_PARENTID)) {
                // 得到父节点id，继续寻找
                mids.add(sm.getId());
                findP(sm.getParentId(), pMaps, mids);
            }
        }
    }

    /**
     * @param param 用户，角色数据
     * @return void
     * @author leihfei
     * @description 对用户进行授权
     * @date 16:13:52 2019-04-10
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.CREATE_STATUS, value = "对用户进行授权", moduleName = "角色管理")
    public void authUser(RoleAuthUserParam param) {
        List<SysUser> users = userDAO.findAllByIdIn(param.getUserIds());
        if (CollectionUtils.isEmpty(users) || users.size() != param.getUserIds().size()) {
            throw new WarnException("用户数据不正确，请检查后重试!");
        }
        // 检查角色
        List<SysRole> roles = roleDAO.findAllById(param.getRoleIds());
        if (CollectionUtils.isEmpty(roles) || roles.size() != param.getRoleIds().size()) {
            throw new WarnException("角色数据不正确，请检查后重试!");
        }
        // 进行循环保存即可
        List<SysUserRole> data = Lists.newArrayList();
        param.getUserIds().forEach(user -> {
            param.getRoleIds().forEach(role -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(user);
                userRole.setRoleId(role);
                userRole.setOperator(RequestHolder.currentUser().getRealName());
                userRole.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
                data.add(userRole);
            });
        });
        userRoleDAO.saveAll(data);
    }


    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "通过角色ids查询数据", moduleName = "角色管理模块")
    public List<SysRole> findAllByRoleIds(Set<String> roleIds) {
        return roleDAO.findAllById(roleIds);
    }


    /**
     * @param ids 角色id
     * @return java.util.List<com.xzl.security.pojo.master.entity.SysRole>
     * @author leihfei
     * @description 查询不属于id的角色数据
     * @date 17:02:21 2019-04-15
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "不存在的角色id set 查询数据", moduleName = "角色管理模块")
    public List<SysRole> findAllByIdNotIn(Set<String> ids) {
        return roleDAO.findAllByIdNotIn(ids);
    }


    /**
     * @param param
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 查询角色的授权数据
     * @date 16:36:54 2019-04-15
     */
    @Override
    @ControllerLogAnontation(type = LogConstants.QUERY_STATUS, value = "查询角色的授权数据", moduleName = "权限管理-角色授权")
    public Response viewAuth(IdEntity param) {
        BeanValidator.check(param);
        List<SysModule> modules = moduleDAO.findAll();
        List<SysRoleModule> roleModules = roleModuleDAO.findAllByRoleId(param.getId());
        List<String> roleModulesIds = roleModules.stream().map(e -> e.getModuleId()).collect(Collectors.toList());
        RoleMouleViewVO vo = new RoleMouleViewVO();
        // 需要处理当前id存在父节点的时候，需要清除掉父节点数据
        Map<String, SysModule> mapsP = modules.stream().collect(Collectors.toMap(SysModule::getId, Function.identity()));
        Set<String> remove = Sets.newHashSet();
        roleModulesIds.forEach(item -> {
            SysModule sysModule = mapsP.get(item);
            if (sysModule != null && roleModulesIds.contains(sysModule.getParentId())) {
                remove.add(sysModule.getParentId());
            }
        });
        List<String> collect = roleModulesIds.stream().filter(e -> !remove.contains(e)).collect(Collectors.toList());
        roleModulesIds.remove(remove);
        vo.setDefuleSelectKeys(collect);
        // 对菜单进行赋值
        if (CollectionUtils.isEmpty(modules)) {
            return new FailedResponse<>(ResponseEnum.WARN_CODE.getCode(), "菜单数据为空!");
        }
        List<ModuleNgZorroTreeVO> tree = moduleService.tree(modules);
        vo.setModules(tree);
        return new ObjectResponse<>(vo);
    }

    /**
     * @param param 参数
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 用户查询自己角色信息
     * @date 19:40:13 2019-04-15
     */
    @Override
    @ControllerLogAnontation(type = LogConstants.QUERY_STATUS, value = "用户查询自己角色信息", moduleName = "权限管理-角色授权")
    public Response showAuth(IdEntity param) {
        BeanValidator.check(param);
        // 在用户中查询一次
        SysUser user = userDAO.findByUserInfoId(param.getId());
        if (user == null) {
            return new FailedResponse(ResponseEnum.WARN_CODE.getCode(), "用户信息不存在，无法授权!");
        }
        // 用户已存在的权限
        List<SysUserRole> userRoles = userRoleDAO.findAllByUserId(user.getId());
        // 所有权限，
        List<SysRole> allRoles = roleDAO.findAll();
        if (CollectionUtils.isEmpty(allRoles)) {
            return new FailedResponse(ResponseEnum.WARN_CODE.getCode(), "系统中无可分配角色!");
        }
        List<String> userRoleIds = userRoles.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
        // 过滤掉超级管理员
        allRoles = allRoles.stream().filter(e -> !e.getId().equals(SystemConstants.SUPER_USER)).collect(Collectors.toList());
        // 循环处理
        List<RoleVO> vos = Lists.newArrayList();
        allRoles.forEach(item -> {
            RoleVO vo = new RoleVO();
            vo.setKey(item.getId());
            vo.setTitle(item.getName());
            if (userRoleIds.contains(item.getId())) {
                vo.setDirection("right");
            } else {
                vo.setDirection("left");
            }
            vos.add(vo);
        });
        // 进行权限的查询
        return new ObjectResponse(vos);
    }

    /**
     * @param param 角色id
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 角色查询用户是否分配该角色
     * @date 20:03:30 2019-04-15
     */
    @Override
    @ControllerLogAnontation(type = LogConstants.QUERY_STATUS, value = "角色查询用户是否分配该角色", moduleName = "权限管理-角色授权")
    public Response queryUserAuth(IdEntity param) {
        BeanValidator.check(param);
        // 查询角色
        SysRole role = roleDAO.findById(param.getId()).orElse(null);
        if (role == null) {
            return new FailedResponse(ResponseEnum.WARN_CODE.getCode(), "角色信息异常!");
        }
        // 查询用户中哪些分配了该角色,查询出角色分配的用户
        List<SysUserRole> users = userRoleDAO.findAllByRoleId(role.getId());
        List<String> roleIds = users.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        // 所有的用户信息
        List<SysUser> allData = userDAO.findAll();
        // 分配了角色的用户
        List<UserRoleCheckVO> userRoleUsers = allData.stream().filter(e -> roleIds.contains(e.getId()))
                .map(e -> {
                    return new UserRoleCheckVO(e.getId(), e.getRealName(), e.getUsername(), false);
                })
                .collect(Collectors.toList());
        return new ObjectResponse<>(userRoleUsers);
    }

    /**
     * @param param 参数
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 移除角色
     * @date 20:28:44 2019-04-15
     */
    @Override
    @Transactional
    @ControllerLogAnontation(type = LogConstants.DELETE_STATUS, value = "移除角色", moduleName = "权限管理-角色授权")
    public Response deleteAuth(AuthParam param) {
        if (CollectionUtils.isEmpty(param.getRoleIds()) || CollectionUtils.isEmpty(param.getUserIds())) {
            return new FailedResponse(ResponseEnum.WARN_CODE.getCode(), "参数异常，必须角色，用户都有值!");
        }
        // 删除角色
        List<SysUserRole> roles = userRoleDAO.findAllByUserIdIn(param.getUserIds());
        // 过滤掉不需要删除的角色
        roles = roles.stream().filter(e -> param.getRoleIds().contains(e.getRoleId())).collect(Collectors.toList());
        // 删除这些用户的一些角色
        userRoleDAO.deleteAll(roles);
        return new SuccessResponse("撤销权限成功!");
    }

    /**
     * @param ngPager 参数
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 查询未分配角色的用户
     * @date 20:35:01 2019-04-15
     */
    @Override
    @ControllerLogAnontation(type = LogConstants.QUERY_STATUS, value = "查询未分配角色的用户", moduleName = "权限管理-角色授权")
    public Response pageQueryUser(NgPager ngPager) {
        // 查询角色
        Map<String, NgFilter> filters = ngPager.getFilters();
        if (filters == null || filters.size() == 0) {
            return new FailedResponse<>(ResponseEnum.WARN_CODE.getCode(), "参数不正确!");
        }
        NgFilter roleId = filters.get("roleId");
        if (roleId == null) {
            return new FailedResponse<>(ResponseEnum.WARN_CODE.getCode(), "角色id不存在!");
        } else {
            // 删除掉roleId这个属性
            filters.remove("roleId");
        }
        String roleValue = (String) roleId.getValue();
        SysRole role = roleDAO.findById(roleValue).orElse(null);
        if (role == null) {
            return new FailedResponse<>(ResponseEnum.WARN_CODE.getCode(), "角色不存在!");
        }
        // 查询所有的用户信息
        NgData<SysUser> ngData = new NgData<>(
                userDAO.findAll(
                        DynamicSpecifications.bySearchFilter(
                                SysUser.class,
                                PageUtils.buildSearchFilter(ngPager)
                        ),
                        PageUtils.buildPageRequest(
                                ngPager,
                                PageUtils.buildSort(ngPager)
                        )
                ), ngPager);
        // 取到了数据
        NgData<UserRoleCheckVO> retuData = CopyUtils.beanCopy(ngData, new NgData<UserRoleCheckVO>());
        retuData.setData(new ArrayList<>());
        // 查询该角色已经分配的人
        List<SysUserRole> allByRoleId = userRoleDAO.findAllByRoleId(roleValue);
        List<String> collect = allByRoleId.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        // 循环ngData
        List<UserRoleCheckVO> vois = Lists.newArrayList();
        ngData.getData().forEach(item -> {
            UserRoleCheckVO vo = new UserRoleCheckVO();
            vo.setId(item.getId());
            vo.setName(item.getRealName());
            vo.setNumber(item.getUsername());
            if (!collect.contains(item.getId())) {
                vo.setChecked(false);
            } else {
                vo.setChecked(true);
            }
            vois.add(vo);
        });
        retuData.setData(vois);
        return new ObjectResponse<>(retuData);
    }


    /**
     * @param param 参数信息
     * @return com.xzl.common.response.Response
     * @author leihfei
     * @description 用户授权
     * @date 20:54:31 2019-04-15
     */
    @Override
    @Transactional
    @ControllerLogAnontation(type = LogConstants.CREATE_STATUS, value = "用户授权", moduleName = "权限管理-角色授权")
    public Response userAuth(AuthParam param) {
        if (CollectionUtils.isEmpty(param.getUserIds())) {
            return new FailedResponse(ResponseEnum.WARN_CODE.getCode(), "参数异常，用户信息不能为空!");
        }
        if (CollectionUtils.isEmpty(param.getRoleIds())) {
            return new FailedResponse(ResponseEnum.WARN_CODE.getCode(), "参数异常，角色不能为空!");
        }
        List<SysUser> users = userDAO.findAllByUserInfoIdIn(param.getUserIds());
        param.setUserIds(users.stream().map(e -> e.getId()).collect(Collectors.toSet()));
        // 对这些人再次授予一个权限
        List<SysUserRole> sysUserRoles = Lists.newArrayList();
        if (param.getUserIds().size() == 1) {
            // 说明是对在用户处分配角色，直接删除用户的所有角色，然后新增
            userRoleDAO.deleteAllByUserIdIn(param.getUserIds());
            for (String roleId : param.getRoleIds()) {
                param.getUserIds().forEach(item -> {
                    SysUserRole role = new SysUserRole();
                    role.setUserId(item);
                    role.setRoleId(roleId);
                    role.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
                    role.setOperator(RequestHolder.currentUser().getRealName());
                    sysUserRoles.add(role);
                });
            }
        } else {
            // 说明是在角色处分配用户，那么角色肯定只有一个
            param.getRoleIds().forEach(item -> {
                for (SysUser user : users) {
                    SysUserRole role = new SysUserRole();
                    role.setUserId(user.getId());
                    role.setRoleId(item);
                    role.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
                    role.setOperator(RequestHolder.currentUser().getRealName());
                    sysUserRoles.add(role);
                }
            });
        }
        // 存储
        userRoleDAO.saveAll(sysUserRoles);
        return new SuccessResponse("授权成功!");
    }


    @Override
    public List<SysUserRole> findAllByUserId(String userId) {
        return userRoleDAO.findAllByUserId(userId);
    }

    /**
     * @param userId 用户
     * @return java.util.Set<java.lang.String>
     * @author leihfei
     * @description 通过用户id查询所拥有的模块id
     * @date 13:21:17 2019-04-16
     */
    @Override
    public Set<String> findUserRolesByUserId(String userId) {
        List<SysUserRole> userRoles = userRoleDAO.findAllByUserId(userId);
        Set<String> roleIds = userRoles.stream().map(e -> e.getRoleId()).collect(Collectors.toSet());
        // 查询角色所对应的菜单模块
        List<SysRoleModule> sysRoleModules = roleModuleDAO.findAllByRoleIdIn(roleIds);
        // 取出每一个菜单的id
        Set<String> moduleIds = sysRoleModules.stream().map(e -> e.getModuleId()).collect(Collectors.toSet());
        return moduleIds;
    }
}





