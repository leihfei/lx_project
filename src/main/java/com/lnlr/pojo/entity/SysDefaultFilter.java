package com.lnlr.pojo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author:leihfei
 * @description shiro拦截器实体类
 * @date:Create in 9:22 2018/10/9
 * @email:leihfein@gmail.com
 */
@Data
@Table(name = "sys_default_filter")
@Entity
public class SysDefaultFilter implements Serializable {

    private static final long serialVersionUID = 7916151847033920045L;

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    private String id;

    /**
     * url地址
     */
    private String url;

    /**
     * 过滤器名称
     */
    private String filterName;

    /**
     * 类型：0-过滤地址，1-权限不拦截
     */
    private Integer type;


    private Integer sort;
}
