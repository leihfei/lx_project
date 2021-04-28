package com.lnlr.common.constains;

import org.springframework.stereotype.Component;

/**
 * @author:leihfei
 * @description 系统常量
 * @date:Create in 16:12 2018/9/14
 * @email:leihfein@gmail.com
 */
@Component
public class SystemConstants {

    /**
     * 环境传感器缓存地址前缀
     */

    public static final String ENVIROMENT_PRIFEX = "enviorment_";
    /**
     * 全局过滤地址缓存前缀
     */
    public static final String DEFAULT_URL_CATCH = "default_catch";

    /**
     * 全局过滤地址缓存过期时间: 一天
     */
    public static final Integer DEFAULT_URL_CATCH_TIME = 86400;

    /**
     * 消息中心-系统消息
     */
    public static final String MESSAGETYPESYS = "1";

    /**
     * 消息中心-个人消息
     */
    public static final String MESSAGETYPEMY = "2";

    /**
     * 消息中心-设备消息
     */
    public static final String MESSAGETYPEMO = "3";

    /**
     * 消息中心返回跳转路径参数（Id为moduleID和parentID）
     */
    public static final String MESSAGEMODULE = "systemId=4028813d67ed3f410167ed6b92c600b0&pageId=4028813d67ed3f410167ed6faab70183";

    /**
     * 是否冻结
     */
    public static final String STATUS = "1";

    /**
     * 默认的module父节点id
     */
    public static final String DEFAULT_MODULE_PARENTID = "980989898999";


    /**
     * 默认学院
     */
    public static final String DEFAULT_COLLEAGE_PARENTID = "0";

    /**
     * 默认部门
     */
    public static final String DEFAULT_DEPT_PARENTID = "0";

    /**
     * 默认试题分类
     */
    public static final String DEFAULT_SUBJECT_PARENTID = "0";

    /**
     * 超级管理员id,不允许删除
     */
    public static final String SUPER_USER = "11111";

    /**
     * 企业版
     */
    private static String contextPath = "/security";


    /**
     * 模块缓存名字
     */
    public static final String MDOULE_REDIS_PREFIX = "module_tree";

    /**
     * jwt 登录用户
     */
    public static final String JWT_LOGIN_USER = "loginUser";

    /**
     * jwt登录用户唯一编号
     */
    public static final String JWT_LOGIN_USERNAME = "unique_name";

    /**
     * TOKEN_TYPE
     */
    public static final String TOKEN_TYPE = "bearer ";

    /**
     * authorizaiton
     */
    public static final String AUTHORIZATION = "Authorization";


    /**
     * 跳转到token生成页面
     */
    public static final String CREATE_TOKEN = contextPath + "/sys/authorizing/create_token";

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 用户类型：老师
     */
    public static final Integer USER_TYPE_TEACHER = 0;

    /**
     * 用户类型：学生
     */
    public static final Integer USER_TYPE_STUDENT = 1;

}
