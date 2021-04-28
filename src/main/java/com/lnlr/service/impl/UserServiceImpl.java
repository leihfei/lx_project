package com.lnlr.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.lnlr.common.annonation.ServiceLogAnonation;
import com.lnlr.common.constains.ApplicationConstants;
import com.lnlr.common.constains.LogConstants;
import com.lnlr.common.constains.SystemConstants;
import com.lnlr.common.entity.HashPassword;
import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.exception.ParamException;
import com.lnlr.common.exception.WarnException;
import com.lnlr.common.jpa.enums.Operator;
import com.lnlr.common.jpa.model.NgData;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.common.jpa.model.SearchFilter;
import com.lnlr.common.jpa.query.DynamicSpecifications;
import com.lnlr.common.utils.BeanValidator;
import com.lnlr.common.utils.IpUtils;
import com.lnlr.common.utils.PassUtils;
import com.lnlr.common.utils.RequestHolder;
import com.lnlr.pojo.dao.SysRoleDAO;
import com.lnlr.pojo.dao.SysUserDAO;
import com.lnlr.pojo.dao.SysUserRoleDAO;
import com.lnlr.pojo.entity.SysModule;
import com.lnlr.pojo.entity.SysRole;
import com.lnlr.pojo.entity.SysUser;
import com.lnlr.pojo.entity.SysUserRole;
import com.lnlr.pojo.param.base.AuthorityParam;
import com.lnlr.pojo.param.base.CheckPassParam;
import com.lnlr.pojo.vo.auth.UserVO;
import com.lnlr.service.ModuleServices;
import com.lnlr.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author:leihfei
 * @description: 用户业务层实现类
 * @date:Create in 15:17 2018/11/26
 * @email:leihfein@gmail.com
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * 超级管理员角色
     */
    private final static String USER_ORLE_SUPER = "4028b8816a1f1c4f016a2000dafa0271";

    @Autowired
    private SysUserDAO userDAO;

    @Autowired
    private SysUserRoleDAO userRoleDAO;

    @Autowired
    private SysRoleDAO roleDAO;

    @Autowired
    private ModuleServices moduleService;

    /**
     * 判读是否为超级管理员
     *
     * @param id 用户id
     */
    @Override
    public Boolean findUserIsManager(String id) {
        List<SysUserRole> roleList = userRoleDAO.findAllByUserId(id);
        boolean falt = false;
        for (SysUserRole sysUserRole : roleList) {
            if (USER_ORLE_SUPER.equals(sysUserRole.getRoleId())) {
                falt = true;
                break;
            }
        }
        return falt;
    }


    @Override
    @ServiceLogAnonation(type = "query", value = "查询所有用户", moduleName = "用户管理")
    public List<SysUser> findAll() {
        Collection<SearchFilter> searchFilters = new ArrayList<>();
        searchFilters.add(new SearchFilter("userType", Operator.IN, "1,0"));
        return userDAO.findAll(DynamicSpecifications.bySearchFilter(SysUser.class, searchFilters));
    }

    /**
     * @param keyword 通过关键字查询用户
     * @return SysUser
     * @author: leihfei
     * @description
     * @date: 11:38 2018/9/8
     * @email: leihfein@gmail.com
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "通过关键字查询用户", moduleName = "用户管理")
    public SysUser findKeyword(String keyword) {
        SysUser user;
        if (!StringUtils.isNotEmpty(keyword)) {
            throw new ParamException("用户信息异常，无法查询");
        }
        try {
            user = userDAO.findAllByUsername(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParamException("用户数据异常，请联系管理员");
        }
        if (user != null) {
            if (!user.getUsername().equals(keyword)) {
                return null;
            }
        }
        return user;
    }


    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "通过id查询用户信息", moduleName = "用户管理")
    public SysUser view(String id) {
        return userDAO.findById(id).orElse(null);
    }

    /**
     * 分页查询用户数据
     *
     * @param ngPager
     * @return
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "分页查询用户信息", moduleName = "用户管理")
    public NgData page(NgPager ngPager) {
        return null;
    }

    @Override
    @ServiceLogAnonation(type = LogConstants.UPDATE_STATUS, value = "更新密码", moduleName = "用户管理")
    public boolean updatePass(CheckPassParam checkPassParam) {
        Preconditions.checkNotNull(checkPassParam.getNewPass(), "新密码不能为空!");
        boolean b = checkPass(checkPassParam);
        if (b == false) {
            throw new RuntimeException("用户不存在或密码错误!");
        }
        if (checkPassParam.getOldPass().equals(checkPassParam.getNewPass())) {
            throw new WarnException("待更改密码不能和原始密码相同!");
        }
        // 更新密码
        SysUser sysUser = userDAO.findById(checkPassParam.getUserId()).orElse(null);
        HashPassword hashPassword = PassUtils.encryptPassword(checkPassParam.getNewPass());
        sysUser.setPassword(hashPassword.getPassword());
        sysUser.setSalt(hashPassword.getSalt());
        userDAO.save(sysUser);
        return true;
    }

    @Override
    @ServiceLogAnonation(type = LogConstants.CHECK_STATUS, value = "检查密码", moduleName = "用户管理")
    public boolean checkPass(CheckPassParam checkPassParam) {
        BeanValidator.check(checkPassParam);
        // 检查用户是否存在
        SysUser sysUser = userDAO.findById(checkPassParam.getUserId()).orElse(null);
        Preconditions.checkNotNull(sysUser, "用户不存在!");
        boolean b = PassUtils.validatePassword(checkPassParam.getOldPass(), sysUser.getPassword(), sysUser.getSalt());
        return b;
    }

    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "通过id查询用户", moduleName = "用户管理")
    public SysUser findById(String id) {
        return userDAO.findById(id).orElse(null);
    }

    @Override
    @ServiceLogAnonation(type = LogConstants.DELETE_STATUS, value = "通过id删除用户", moduleName = "用户管理")
    public void delete(IdEntity idEntity) {
        if (StringUtils.isEmpty(idEntity.getId())) {
            throw new ParamException("用户id不能为空!");
        }
        SysUser sysUser = userDAO.findById(idEntity.getId()).orElse(null);
        if (sysUser == null) {
            throw new ParamException("用户信息不存在!");
        }
        // 将用户状态设置为删除
        sysUser.setStatus(ApplicationConstants.DELETE);
        sysUser.setOperator(RequestHolder.currentUser().getRealName());
        sysUser.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
        userDAO.save(sysUser);
    }

    /**
     * 更新用户信息，将冻结解冻，将正常状态设置为冻结
     *
     * @param idEntity
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.UPDATE_STATUS, value = "通过用户id更新用户状态", moduleName = "用户管理")
    public void updateStatus(IdEntity idEntity) {
        if (StringUtils.isEmpty(idEntity.getId())) {
            throw new ParamException("用户id不能为空!");
        }
        SysUser sysUser = userDAO.findById(idEntity.getId()).orElse(null);
        if (sysUser == null) {
            throw new ParamException("用户信息不存在!");
        }
        // 将用户状态设置为删除
        if (sysUser.getStatus() == ApplicationConstants.ENABLED) {
            sysUser.setStatus(ApplicationConstants.DISABLED);
        } else {
            sysUser.setStatus(ApplicationConstants.ENABLED);
        }
        sysUser.setOperator(RequestHolder.currentUser().getRealName());
        sysUser.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
        userDAO.save(sysUser);
    }

    @Override
    @ServiceLogAnonation(type = "授权", value = "给用户授予权限", moduleName = "用户管理")
    public void authority(AuthorityParam authorityParam) {
        // 检查用户，id,角色等信息
        queryUser(authorityParam);
        // 检查角色是否已经分配，已经分配直接返回错误信息，刷新后重试
        List<SysUserRole> users = userRoleDAO.findAllByRoleIdInAndUserId(authorityParam.getRoleIds(), authorityParam.getUserId());
        if (CollectionUtils.isNotEmpty(users)) {
            // 计算交集
            Set<String> collect = users.stream().map(e -> e.getRoleId()).collect(Collectors.toSet());
            authorityParam.getRoleIds().removeAll(collect);
        }
        List<SysUserRole> userRoles = Lists.newArrayList();
        String operator = RequestHolder.currentUser().getRealName();
        String operId = IpUtils.getRemoteIp(RequestHolder.currentRequest());
        // 进行用户角色分配
        authorityParam.getRoleIds().forEach(item -> {
            // 检查角色是否有效
            SysRole sysRole = roleDAO.findById(item).orElse(null);
            if (sysRole != null) {
                userRoles.add(new SysUserRole(authorityParam.getUserId(), sysRole.getId(), operator, operId));
            }
        });
        if (CollectionUtils.isNotEmpty(userRoles)) {
            userRoleDAO.saveAll(userRoles);
        }
    }

    @Override
    @Transactional
    @ServiceLogAnonation(type = "授权", value = "给用户撤销权限", moduleName = "用户管理")
    public void unAuthority(AuthorityParam authorityParam) {
        queryUser(authorityParam);
        // 进行用户角色撤销,删除记录即可
        List<SysUserRole> allByUserId = userRoleDAO.findAllByUserId(authorityParam.getUserId());
        if (CollectionUtils.isEmpty(allByUserId)) {
            return;
        }
        // 进行删除，首先取交际
        Set<String> collect = allByUserId.stream().map(e -> e.getRoleId()).collect(Collectors.toSet());
        // 交际
        collect.retainAll(authorityParam.getRoleIds());
        if (collect != null && collect.size() > 0) {
            // 删除
            userRoleDAO.deleteAllByRoleIdInAndUserId(collect, authorityParam.getUserId());
        }
    }

    private void queryUser(AuthorityParam authorityParam) {
        if (StringUtils.isEmpty(authorityParam.getUserId())) {
            throw new ParamException("用户id不存在");
        }
        if (CollectionUtils.isEmpty(authorityParam.getRoleIds())) {
            throw new ParamException("角色数据异常，请刷新重试!");
        }
        // 1. 检查当前用户是否有效
        SysUser sysUser = userDAO.findById(authorityParam.getUserId()).orElse(null);
        if (sysUser == null) {
            throw new ParamException("用户无效，请刷新重试!");
        }
    }


    @Override
    @ServiceLogAnonation(type = "reset", value = "重置密码", moduleName = "用户管理")
    public void resetPassword(IdEntity entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            throw new ParamException("用户id不能为空!");
        }
        SysUser userHolder = RequestHolder.currentUser();
        if (entity.getId().equals("1")) {
            throw new ParamException("管理员账号禁止重置密码!");
        }
        if (!userHolder.getUserInfoId().equals("1")) {
            throw new ParamException("您不是管理员，无法重置密码!");
        }
        // 查询用户
        SysUser sysUser = userDAO.findByUserInfoId(entity.getId());
        if (sysUser == null) {
            throw new ParamException("用户数据为空，无法重置!");
        }

        // 初始化密码，盐值等
        HashPassword hashPassword = PassUtils.encryptPassword(SystemConstants.DEFAULT_PASSWORD);
        sysUser.setPassword(hashPassword.getPassword());
        sysUser.setSalt(hashPassword.getSalt());
        sysUser.setOperator(RequestHolder.currentUser().getRealName());
        sysUser.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
        userDAO.save(sysUser);
    }


    UserVO getUserVO(SysUser user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setRealName(user.getRealName());
        vo.setStatus(user.getStatus());
        vo.setUserType(user.getUserType());
        // todo 头像信息
        return vo;
    }


    @Override
    public List<Map<String, String>> list() {
        List<SysUser> all = userDAO.findAll();
        return all.stream().map(e -> {
            Map<String, String> map = new HashMap<>();
            map.put("id", e.getId());
            map.put("name", e.getRealName());
            return map;
        }).collect(Collectors.toList());
    }


    /**
     * 用户所有有权限的路由
     *
     * @return 路由
     */
    private Set<String> listUserRoleUrl() {
        List<SysModule> modules = moduleService.listChildMenu();
        //拿到用户自己拥有权限的路由
        return modules.stream().map(SysModule::getUrl).collect(Collectors.toSet());
    }

}
