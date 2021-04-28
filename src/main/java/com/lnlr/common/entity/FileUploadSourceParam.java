package com.lnlr.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 新增资源和业务关系
 * @date 2019/2/12 14:44
 */
@Data
@ApiModel(value = "上传附件对象")
public class FileUploadSourceParam {
    /**
     * 资源id
     */
    @ApiModelProperty(value = "资源id列表", required = true, dataType = "List")
    @NotNull(message = "资源列表不允许为空")
    private List<String> sourceIds;

    /**
     * service id
     */
    @ApiModelProperty(value = "业务id", required = true, dataType = "String")
    @NotBlank(message = "业务id不允许为空")
    private String id;
}
