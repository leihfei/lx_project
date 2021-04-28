package com.lnlr.common.tree;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description 树形结构
 * @Date 2019/8/28 15:00
 * @Author yangyi
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "Tree", description = "树形结构")
public class TreeVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String key;

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String value;

    /**
     * 试题名称
     */
    @ApiModelProperty(value = "名称")
    private String title;

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id")
    private String parentId;

    /**
     * 子节点
     */
    @ApiModelProperty(value = "叶子节点")
    private List<TreeVO> children;

    /**
     * 是否末端节点；默认给false
     */
    private Boolean isLeaf;

    /**
     * 是否展开
     */
    private Boolean opened;

    private Integer studentNumber;

}
