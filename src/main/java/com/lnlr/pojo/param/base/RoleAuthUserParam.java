package com.lnlr.pojo.param.base;

import lombok.Data;

import java.util.Set;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 用户角色授权
 * @date 2019-04-10 16:12:38
 */
@Data
public class RoleAuthUserParam {

    private Set<String> roleIds;

    private Set<String> userIds;
}
