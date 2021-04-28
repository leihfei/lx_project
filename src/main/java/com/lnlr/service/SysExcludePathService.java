package com.lnlr.service;


import com.lnlr.pojo.entity.SysDefaultFilter;

import java.util.List;

/**
 * @author:leihfei
 * @description: 获取默认放过拦截服务
 * @date:Create in 11:19 2018/10/26
 * @email:leihfein@gmail.com
 */
public interface SysExcludePathService {
    /**
     * 查询所有放过拦截地址
     *
     * @return
     */
    List<SysDefaultFilter> findAll();
}
