package com.lnlr.pojo.vo.zorro;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 树形返回数据结构，常规树结构
 * 配合ng-zooro 树形插件
 * @date 2019-04-10 12:07:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NgZorroTreeVO extends NgZorroTreeBaseVO {

    /**
     * 子节点
     */
    private List<NgZorroTreeVO> children = Lists.newArrayList();
}
