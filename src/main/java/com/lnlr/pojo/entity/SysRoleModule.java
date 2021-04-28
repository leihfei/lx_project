package com.lnlr.pojo.entity;

import com.lnlr.pojo.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 角色菜单管理
 */
@Data
@Builder
@Table(name = "sys_role_module")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@EqualsAndHashCode(callSuper = true)
public class SysRoleModule extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -8624687533303758984L;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 菜单ID
     */
    private String moduleId;
}
