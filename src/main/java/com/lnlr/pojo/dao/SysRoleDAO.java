package com.lnlr.pojo.dao;


import com.lnlr.pojo.base.BaseDAO;
import com.lnlr.pojo.entity.SysRole;

import java.util.List;
import java.util.Set;

public interface SysRoleDAO extends BaseDAO<SysRole> {

    List<SysRole> findAllByIdNotIn(Set<String> ids);

    SysRole findByName(String name);
}
