package com.lnlr.pojo.entity;

import com.lnlr.pojo.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author:leihfei
 * @description 用户实体类
 * @date:Create in 19:41 2018/8/30
 * @email:leihfein@gmail.com
 */
@Entity
@Table(name = "sys_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3711382332982623791L;

    /**
     * 系统登录用户名称
     */
    private String username;

    /**
     * 系统用户名称
     */
    private String realName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 密码盐值
     */
    private String salt;

    /**
     * 状态：1-正常，2-删除,0-冻结
     */
    private Integer status;

    /**
     * 用户类型： 0-老师，1-学生
     */
    private Integer userType;

    /**
     * 用户信息id
     */
    private String userInfoId;

    /**
     * 头像资源文件id
     */
    private String photo;

}
