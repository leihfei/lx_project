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
 * @description: 系统操作日志
 * @date:Create in 19:20 2018/11/29
 * @email:leihfein@gmail.com
 */
@Entity(name = "sys_operation_log")
@Table
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = true)
public class SysOperatorLog extends BaseEntity implements Serializable {

    /**
     * 模块
     */
    private String moduleName;
    /**
     * 模块
     */
    private String operationType;
    /**
     * 模块
     */
    private String operValue;

    /**
     * 类全路径
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

}
