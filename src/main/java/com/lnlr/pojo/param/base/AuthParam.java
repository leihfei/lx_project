package com.lnlr.pojo.param.base;

import lombok.Data;

import java.util.Set;

@Data
public class AuthParam {
    private Set<String> userIds;

    private Set<String> roleIds;
}
