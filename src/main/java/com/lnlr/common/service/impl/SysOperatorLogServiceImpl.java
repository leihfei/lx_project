package com.lnlr.common.service.impl;

import com.lnlr.common.service.SysOperatorLogService;
import com.lnlr.pojo.dao.SysOperatorLogDAO;
import com.lnlr.pojo.entity.SysOperatorLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author:leihfei
 * @description:
 * @date:Create in 19:19 2018/11/29
 * @email:leihfein@gmail.com
 */
@Service
public class SysOperatorLogServiceImpl implements SysOperatorLogService {

    @Autowired
    private SysOperatorLogDAO operatorLogDAO;

    @Override
    public void create(SysOperatorLog log) {
        try {
            operatorLogDAO.save(log);
        } catch (RuntimeException e) {
            e.getMessage();
        }
    }
}
