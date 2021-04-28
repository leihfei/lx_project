package com.lnlr.pojo.entity;

import com.lnlr.pojo.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 菜单管理
 */
@Data
@Builder
@Table(name = "sys_module")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@EqualsAndHashCode(callSuper = true)
public class SysModule extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7435076453792804142L;
    /**
     * 模块名称
     */
    private String name;

    /**
     * 父节点
     */
    private String parentId;

    /**
     * 显示优先级
     */
    private Integer showLevel;

    /**
     * 模块状态：1-可见，0-隐藏
     */
    private Integer status;

    /**
     * 路由
     */
    private String url;

    /**
     * 类型
     * 0-菜单
     * 1-按钮
     * 2-其他
     */
    private Integer type;


    /**
     * 所属系统
     * 0-内部系统
     * 1-新窗口打开
     * 2-第三方连接
     */
    private Integer targetType;

    /**
     * 图标
     */
    private String icon;

    /**
     * 备注
     */
    private String remark;

    /**
     * 关键字
     */
    private Integer authKey;

}
