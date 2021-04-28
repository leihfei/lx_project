package com.lnlr.pojo.entity;

import com.lnlr.pojo.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 角色管理
 */
@Data
@Table(name = "sys_role")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7038630017113296535L;
    /**
     * 角色名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除状态
     * 0-删除
     * 1-有效
     */
    private Integer status = 1;

    /**
     * 角色大类型（0.老师的角色，1.学生的角色）
     */
    private Integer type;
}
