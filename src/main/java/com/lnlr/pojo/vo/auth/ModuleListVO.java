package com.lnlr.pojo.vo.auth;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 列表返回
 * @date 2019-04-10 14:39:26
 */
@Data
public class ModuleListVO {

    private String id;

    private String key;

    private String name;

    private String url;

    private Integer showLevel;

    private Integer status;

    /**
     * 前端是否显示展开按钮
     */
    private boolean showExpand = false;

    private List<ModuleListVO> children = Lists.newArrayList();
}
