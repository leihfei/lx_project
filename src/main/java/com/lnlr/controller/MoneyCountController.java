package com.lnlr.controller;

import com.lnlr.common.annonation.ControllerLogAnontation;
import com.lnlr.common.constains.LogConstants;
import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.jpa.model.NgData;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.common.response.ObjectResponse;
import com.lnlr.common.response.Response;
import com.lnlr.pojo.param.MoneyParam;
import com.lnlr.pojo.param.base.UserParam;
import com.lnlr.service.MoneyConuntService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
