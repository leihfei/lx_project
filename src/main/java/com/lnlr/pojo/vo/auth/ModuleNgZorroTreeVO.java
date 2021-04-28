package com.lnlr.pojo.vo.auth;

import com.google.common.collect.Lists;
import com.lnlr.pojo.vo.zorro.NgZorroTreeBaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 菜单ng-zorro 树数据视图
 * @date 2019-04-15 17:18:52
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ModuleNgZorroTreeVO extends NgZorroTreeBaseVO implements Serializable {

    private String url;

    /**
     * 显示优先级
     */
    private Integer showLevel;

    /**
     * 模块状态：1-可见，0-隐藏
     */
    private Integer status;

    private Boolean checkStatus = Boolean.FALSE;

    private Integer level;

    private String icon;

    private List<ModuleNgZorroTreeVO> children = Lists.newArrayList();
}
