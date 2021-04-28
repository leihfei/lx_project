package com.lnlr.common.constains;

/**
 * @author:leihfei
 * @description 应用常量
 * @date:Create in 15:46 2018/9/6
 * @email:leihfein@gmail.com
 */
public interface ApplicationConstants {

    /**
     * 冻结
     */
    Integer DISABLED = 0;

    /**
     * 启用
     */
    Integer ENABLED = 1;

    /**
     * 删除
     */
    Integer DELETE = 2;

    /**
     * redis默认过期时间，默认600秒，10分钟
     */
    Integer REDIS_EXPIRE_DEFAULT = 600;

}
