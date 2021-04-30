package com.lnlr.pojo.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author leihfei
 * @date 2021-04-27
 * 银行总流水
 */
@Data
public class MoneyVO {

    private String id;
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

    private Integer status;

}
