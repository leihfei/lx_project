package com.lnlr.common.jpa.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author:leihfei
 * @description 封装匹配模式
 * @date:Create in 21:02 2018/9/4
 * @email:leihfein@gmail.com
 */
@ApiModel(value = "数据匹配类型")
public interface MatchModel {
    /**
     * 包含
     */
    @ApiModelProperty(value = "包含")
    String CONTAINS = "contains";
    /**
     * 以xx开始
     */
    @ApiModelProperty(value = "以开始")
    String STARTS_WITH = "startswith";
    /**
     * 以xx结束
     */
    @ApiModelProperty(value = "以结束")
    String ENDS_WITH = "endswith";
    /**
     * 等于
     */
    @ApiModelProperty(value = "全等于")
    String EQUALS = "equals";
    /**
     * 在列表中
     */
    @ApiModelProperty(value = "列表中")
    String IN = "in";
    /**
     * is null
     */
    @ApiModelProperty(value = "是空")
    String ISNULL = "isnull";
    /**
     * 小于
     */
    @ApiModelProperty(value = "小于")
    String LT = "lt";
    /**
     * 大于
     */
    @ApiModelProperty(value = "大于")
    String GT = "gt";

    /**
     * 大于等于
     */
    @ApiModelProperty(value = "大于等于")
    String GTE = "gte";
    /**
     * 小于等于
     */
    @ApiModelProperty(value = "小于等于")
    String LTE = "lte";

    /**
     * 两者之间
     */
    @ApiModelProperty(value = "两者之间")
    String BETWEEN = "between";

    /**
     * 不等于
     */
    @ApiModelProperty(value = "不等于")
    String NOTEQ = "noteq";

    /**
     * 不在列表中
     */
    @ApiModelProperty(value = "不在列表中")
    String NOTIN = "notin";
}
