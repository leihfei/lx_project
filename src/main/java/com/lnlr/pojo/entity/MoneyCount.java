package com.lnlr.pojo.entity;

import com.lnlr.pojo.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author leihfei
 * @date 2021-04-30
 * 银行流水
 */
@Data
@Builder
@Table(name = "money_count")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@EqualsAndHashCode(callSuper = true)
public class MoneyCount extends BaseEntity {

    /**
     * 记账日期
     */
    private LocalDate insertDate;
    /**
     * 授信收入
     */
    private BigDecimal wxCount;

    /**
     * 现金收入
     */
    private BigDecimal cashCount;

    /**
     * 刷卡收入
     */
    private BigDecimal vashCount;

    /**
     * 今日总计
     */
    private BigDecimal allCount;

    /**
     * 系统录入人员
     */
    private String insertName;

    /**
     * 系统录入时间
     */
    private LocalDateTime insertTime;

    /**
     * 记账类型：0-否，1-坏账
     */
    private Integer status;
}
