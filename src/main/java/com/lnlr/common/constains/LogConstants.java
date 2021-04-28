package com.lnlr.common.constains;

/**
 * @author:leihfei
 * @description: 日志记录类型
 * @date:Create in 0:05 2018/11/10
 * @email:leihfein@gmail.com
 */
public interface LogConstants {
    /**
     * 部门操作
     */
    int TYPE_DEPT = 1;

    /**
     * 用户操作
     */
    int TYPE_USER = 2;

    /**
     * 权限模块
     */
    int TYPE_ACL_MODULE = 3;

    /**
     * 权限点
     */
    int TYPE_ACL = 4;

    /**
     * 角色
     */
    int TYPE_ROLE = 5;

    /**
     * 角色权限
     */
    int TYPE_ROLE_ACL = 6;

    /**
     * 角色用户
     */
    int TYPE_ROLE_USER = 7;

    String DELETE_STATUS = "delete";

    String UPDATE_STATUS = "update";

    String QUERY_STATUS = "query";

    String CREATE_STATUS = "create";

    String CHECK_STATUS = "check";

}
