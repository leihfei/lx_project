package com.lnlr.pojo.vo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @date 2019-04-15 20:15:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleCheckVO implements Serializable {

    /**
     *
     */
    @Id
    private String id;

    /**
     * 教师名称
     */
    private String name;

    /**
     * 编号
     */
    private String number;

    private boolean checked;
}
