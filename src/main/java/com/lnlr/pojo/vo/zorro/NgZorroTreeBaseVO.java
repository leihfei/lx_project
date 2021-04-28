package com.lnlr.pojo.vo.zorro;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description ng-zorro树形结构基础视图
 * @date 2019-04-15 17:21:46
 */
@Data
public class NgZorroTreeBaseVO implements Serializable {
    /**
     * id
     */
    private String key;

    /**
     * 值
     */
    private String value;

    /**
     * title名称
     */
    private String title;

    /**
     * 是否启动
     */
    private boolean disabled = false;

    /**
     * 是否为叶子节点
     */
    @JSONField(name = "isLeaf")
    private boolean isLeaf = true;
}
