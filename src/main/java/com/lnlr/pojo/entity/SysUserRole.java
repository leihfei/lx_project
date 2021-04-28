package com.lnlr.pojo.entity;

import com.lnlr.pojo.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author:leihfei
 * @description: 角色用户关联表
 * @date:Create in 16:42 2018/10/26
 * @email:leihfein@gmail.com
 */
@Entity
@Table(name = "sys_user_role")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class SysUserRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 899375844777880228L;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 用户id
     */
    private String userId;


    public SysUserRole() {
    }

    public SysUserRole(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public SysUserRole(String userId, String roleId, String operator, String operatorIp) {
        super(operator, operatorIp);
        this.userId = userId;
        this.roleId = roleId;
    }
}
