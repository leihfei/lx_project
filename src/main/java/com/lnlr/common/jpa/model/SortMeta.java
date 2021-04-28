package com.lnlr.common.jpa.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author:leihfei
 * @description 排序规则
 * @date:Create in 20:58 2018/9/4
 * @email:leihfein@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "排序对象")
public class SortMeta {
    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String field;

    /**
     * 排序规则，1->升序asc，-1 -> 降序desc
     */
    @ApiModelProperty(value = "排序规则")
    private int order;


}
