package com.lnlr.pojo.param.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * @author:leihfei
 * @description: 针对用户授权
 * @date:Create in 19:10 2018/11/28
 * @email:leihfein@gmail.com
 */
@Data
@ApiModel(description = "用户分配角色、撤销角色传输类")
public class AuthorityParam {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private String userId;

    /**
     * 用户角色idlist
     */
    @ApiModelProperty(value = "角色ids列表")
    private Set<String> roleIds;
}
