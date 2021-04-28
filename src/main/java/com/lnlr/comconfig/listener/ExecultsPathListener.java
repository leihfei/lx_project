package com.lnlr.comconfig.listener;

import com.google.common.collect.Lists;
import com.lnlr.common.constains.SystemConstants;
import com.lnlr.common.utils.ExcludePathUtils;
import com.lnlr.common.utils.JsonUtils;
import com.lnlr.common.utils.RedisUtil;
import com.lnlr.pojo.entity.SysDefaultFilter;
import com.lnlr.service.SysExcludePathService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * @author:leihfei
 * @description:
 * @date:Create in 10:43 2018/11/27
 * @email:leihfein@gmail.com
 */
@Component
@Slf4j
public class ExecultsPathListener implements ServletContextListener {

    @Autowired
    private SysExcludePathService sysExcludePathService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("初始化listenter,加载请求过滤地址");
        List<SysDefaultFilter> all = sysExcludePathService.findAll();
        redisUtil.set(SystemConstants.DEFAULT_URL_CATCH, JsonUtils.list2Json(all), SystemConstants.DEFAULT_URL_CATCH_TIME);
        // Spring初始化监听，并且开启线程 设置环境数据采集
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ExcludePathUtils.paths = Lists.newArrayList();
        log.info("销毁请求过滤地址");
    }

}
