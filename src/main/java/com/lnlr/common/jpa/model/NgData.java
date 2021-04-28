package com.lnlr.common.jpa.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:leihfei
 * @description 返回前端分页数据
 * @date:Create in 20:53 2018/9/4
 * @email:leihfein@gmail.com
 */
@Data
@ApiModel(value = "分页数据")
public class NgData<T> {
    /**
     * 数据
     */
    @ApiModelProperty(value = "分页数据")
    private List<T> data = new ArrayList<>();


    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数")
    private Long totalRecords = 0L;
    /**
     * 过滤数
     */
    @ApiModelProperty(value = "过滤数")
    private Long filteredValue = 0L;


    /**
     * fjw  add
     * 用于获取不分页的数据
     *
     * @param aaData
     */
    public NgData(List<T> aaData) {
        this.data = aaData;
    }

    public NgData() {
    }

    public NgData(Page<T> dataPage, NgPager pager) {
        this.data = dataPage.getContent();
        if (this.data != null) {
            this.totalRecords = dataPage.getTotalElements();
            this.filteredValue = dataPage.getTotalElements();
        }
    }


    public NgData(List<T> aaData, Long iTotalRecords, Long iTotalDisplayRecords) {
        this.data = aaData;
        this.totalRecords = iTotalRecords;
        this.filteredValue = iTotalDisplayRecords;
    }
}
