package com.lnlr.pojo.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author leihfei
 * @date 2021-04-27
 * 银行总流水
 */
@Data
public class MoneyParam {
    private String id;
    /**
     * 记账日期
     */
    private LocalDate insertDate;
    /**
     * 授信收入
     */
    @NotNull(message = "收入不能为空")
    private BigDecimal wxCount;

    /**
     * 现金收入
     */
    @NotNull(message = "收入不能为空")
    private BigDecimal cashCount;

    /**
     * 刷卡收入
     */
    @NotNull(message = "收入不能为空")
    private BigDecimal vashCount;


    /**
     * 系统录入人员
     */
    private String insertName;

    /**
     * 系统录入时间
     */
    private LocalDateTime insertTime;

}
