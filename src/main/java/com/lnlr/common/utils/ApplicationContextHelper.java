package com.lnlr.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author:leihfei
 * @description 全局Spring context 工具类
 * @date:Create in 0:57 2018/9/3
 * @email:leihfein@gmail.com
 */
@Component("applicationContextHelper")
public class ApplicationContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @param clazz 类类型
     * @return T
     * @author: leihfei
     * @description 从spring context 获取bean
     * @date: 1:00 2018/9/3
     * @email: leihfein@gmail.com
     */
    public static <T> T popBean(Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(clazz);
    }


    /**
     * @param name   bean 名称,
     * @param tClass 类类型
     * @return T
     * @author: leihfei
     * @description 从spring context 获取bean
     * @date: 1:00 2018/9/3
     * @email: leihfein@gmail.com
     */
    public static <T> T popBean(String name, Class<T> tClass) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(name, tClass);
    }
}
