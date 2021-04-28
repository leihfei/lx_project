package com.lnlr.common.constains;

/**
 * @author:leihfei
 * @description: 审核状态
 * @date:Create in 10:39 2018/12/13
 * @email:leihfein@gmail.com
 */

public interface CheckConstants {
    /**
     * 待审核
     */
    Integer CHECK_AUDIT = 0;

    /**
     * 未通过
     */
    Integer CHECK_FAILED = 1;


    /**
     * 已通过
     */
    Integer CHECK_PASS = 2;

    /**
     * 审批中
     */
    Integer CHECK_APPROVAL = 3;
}
