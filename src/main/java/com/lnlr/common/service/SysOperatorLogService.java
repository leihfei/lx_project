package com.lnlr.common.service;


import com.lnlr.pojo.entity.SysOperatorLog;

/**
 * @author:leihfei
 * @description:
 * @date:Create in 19:19 2018/11/29
 * @email:leihfein@gmail.com
 */
public interface SysOperatorLogService {
    /**
     * 保存日志
     *
     * @param log
     */
    void create(SysOperatorLog log);
}
