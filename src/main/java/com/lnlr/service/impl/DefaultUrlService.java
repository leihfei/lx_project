package com.lnlr.service.impl;

import com.lnlr.common.annonation.ServiceLogAnonation;
import com.lnlr.common.constains.LogConstants;
import com.lnlr.pojo.dao.SysDefaultFilterDAO;
import com.lnlr.pojo.entity.SysDefaultFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:leihfei
 * @description: 查询默认的过滤列表
 * @date:Create in 9:39 2018/10/9
 * @email:leihfein@gmail.com
 */
@Service
public class DefaultUrlService {

    @Autowired
    private SysDefaultFilterDAO sysDefaultFilterDAO;

    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "查询数据库过滤信息", moduleName = "过滤管理")
    public List<SysDefaultFilter> findAll() {
        return sysDefaultFilterDAO.findAllByType(1);
    }

    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "查询数据库过滤信息，过滤值为2", moduleName = "过滤管理")
    public List<SysDefaultFilter> findAllByFilter() {
        return sysDefaultFilterDAO.findAllByType(2);
    }
}
