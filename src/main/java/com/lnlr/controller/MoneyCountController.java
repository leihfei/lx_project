package com.lnlr.controller;

import com.lnlr.common.annonation.ControllerLogAnontation;
import com.lnlr.common.constains.LogConstants;
import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.jpa.model.*;
import com.lnlr.common.response.ObjectResponse;
import com.lnlr.common.response.Response;
import com.lnlr.pojo.dto.MondyExcelDownDTO;
import com.lnlr.pojo.param.MoneyParam;
import com.lnlr.service.MoneyConuntService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author leihfei
 * @date 2021-04-30
 * 每日总流水
 */
@RestController
@RequestMapping(value = "/sys/money")
@Api(value = "总流水")
@Slf4j
public class MoneyCountController {

    @Autowired
    private MoneyConuntService service;


    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询流水列表")
    @ControllerLogAnontation(type = LogConstants.QUERY_STATUS, value = "分页查询流水列表", moduleName = "流水管理-流水")
    @ApiImplicitParam(dataTypeClass = NgPager.class, paramType = "query")
    public Response page(@RequestBody NgPager ngPager) {
        NgData<Object> ngData = service.page(ngPager);
        return new ObjectResponse<>(ngData);
    }

    @PostMapping(value = "/create")
    @ApiOperation(value = "新增流水")
    @ControllerLogAnontation(type = LogConstants.CREATE_STATUS, value = "新增流水", moduleName = "流水管理-流水")
    @ApiImplicitParam(dataTypeClass = MoneyParam.class, paramType = "query")
    public Response create(@RequestBody MoneyParam param) {
        return service.create(param);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "更新流水")
    @ControllerLogAnontation(type = LogConstants.UPDATE_STATUS, value = "更新流水", moduleName = "流水管理-流水")
    @ApiImplicitParam(dataTypeClass = MoneyParam.class, paramType = "query")
    public Response update(@RequestBody MoneyParam param) {
        return service.update(param);
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除设置坏账")
    @ControllerLogAnontation(type = LogConstants.UPDATE_STATUS, value = "更新流水", moduleName = "流水管理-流水")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response update(@RequestBody IdEntity param) {
        return service.delete(param);
    }


    @PostMapping(value = "/exportExcel")
    @ApiOperation(value = "导出数据")
    @ControllerLogAnontation(type = LogConstants.QUERY_STATUS, value = "导出数据", moduleName = "平台管理-学生")
    @ApiImplicitParam(dataTypeClass = NgPager.class, paramType = "query")
    public void exportExcel(@ModelAttribute MondyExcelDownDTO dto, HttpServletRequest request, HttpServletResponse servletResponse) {
        NgPager ngPager = new NgPager();
        ngPager.setRows(2000);
        Map<String, NgFilter> filters = ngPager.getFilters();
        if (filters == null) {
            ngPager.setFilters(filters);
        }
        if (StringUtils.isNotEmpty(dto.getInsertName())) {
            filters.put("insertName", new NgFilter(dto.getInsertName(), MatchModel.CONTAINS));
        }
        if (StringUtils.isNotEmpty(dto.getInsertDateGte()) && StringUtils.isNotBlank(dto.getInsertDateLte())) {
            filters.put("insertDate", new NgFilter(dto.getInsertDateGte() + "," + dto.getInsertDateLte(), MatchModel.BETWEEN));
        } else if (StringUtils.isNotBlank(dto.getInsertDateLte())) {
            filters.put("insertDate", new NgFilter(dto.getInsertDateLte(), MatchModel.LTE));
        } else if (StringUtils.isNotBlank(dto.getInsertDateGte())) {
            filters.put("insertDate", new NgFilter(dto.getInsertDateGte(), MatchModel.GTE));
        }
        SortMeta[] sortMetas = new SortMeta[1];
        sortMetas[0] = new SortMeta("insertTime", -1);
        ngPager.setMultiSortMeta(sortMetas);
        ngPager.setFilters(filters);
        service.exportExcel(ngPager, request, servletResponse);
    }
}
