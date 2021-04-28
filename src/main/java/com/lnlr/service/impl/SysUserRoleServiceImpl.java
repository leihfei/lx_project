package com.lnlr.service.impl;

import com.lnlr.pojo.dao.SysRoleDAO;
import com.lnlr.pojo.dao.SysUserRoleDAO;
import com.lnlr.pojo.entity.SysUserRole;
import com.lnlr.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:leihfei
 * @description: 用户角色业务实现
 * @date:Create in 9:54 2018/11/29
 * @email:leihfein@gmail.com
 */
@Service
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Autowired
    private SysUserRoleDAO userRoleDAO;

    @Override
    public List<SysUserRole> findAllRoleListByUserId(String id) {
        return userRoleDAO.findAllByUserId(id);
    }
}
