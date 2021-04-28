package com.lnlr.pojo.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author:郭涛
 * Date:2018/12/6 10:57
 */
@Data
@MappedSuperclass
@ApiModel(value = "基础实体类")
public class BaseEntity implements Serializable {

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    private String id;

    /**
     * 操作用户
     */
    @ApiModelProperty(hidden = true)
    private String operator;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    private LocalDateTime gmtCreate;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(hidden = true)
    private LocalDateTime gmtModified;

    /**
     * 操作ip
     */
    @ApiModelProperty(hidden = true)
    private String operatorIp;

    public BaseEntity() {
    }

    public BaseEntity(String id) {
        this.id = id;
    }

    public BaseEntity(String operator, String operatorIp) {
        this.operator = operator;
        this.operatorIp = operatorIp;
    }

}
