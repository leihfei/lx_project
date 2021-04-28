package com.lnlr.controller;

import com.lnlr.common.annonation.ControllerLogAnontation;
import com.lnlr.common.constains.LogConstants;
import com.lnlr.common.constains.SystemConstants;
import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.response.ObjectResponse;
import com.lnlr.common.response.Response;
import com.lnlr.common.response.SuccessResponse;
import com.lnlr.common.utils.JsonUtils;
import com.lnlr.common.utils.RedisUtil;
import com.lnlr.pojo.param.base.ModuleParam;
import com.lnlr.pojo.vo.auth.ModuleNgZorroTreeVO;
import com.lnlr.service.ModuleServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 菜单管理
 * @date 2019-04-10 11:08:58
 */
@RestController
@RequestMapping(value = "/sys/module")
@Api(value = "菜单管理", description = "权限管理系统菜单管理")
@Slf4j
public class ModuleController {
    @Autowired
    private ModuleServices moduleService;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${module_expire}")
    private Integer moduleExpire;


    @PostMapping("/create")
    @ApiOperation(value = "新增菜单接口")
    @ControllerLogAnontation(type = LogConstants.CREATE_STATUS, value = "权限管理系统新增菜单管理", moduleName = "权限管理-菜单管理")
    @ApiImplicitParam(dataTypeClass = ModuleParam.class, paramType = "query")
    public Response create(@RequestBody ModuleParam param) {
        moduleService.create(param);
        redisUtil.del(SystemConstants.MDOULE_REDIS_PREFIX);
        return new SuccessResponse("新增菜单成功");
    }


    @PostMapping("/update")
    @ApiOperation(value = "更新菜单")
    @ControllerLogAnontation(type = LogConstants.CREATE_STATUS, value = "权限管理系统更新菜单树管理", moduleName = "权限管理-菜单管理")
    @ApiImplicitParam(dataTypeClass = ModuleParam.class, paramType = "query")
    public Response update(@RequestBody ModuleParam param) {
        moduleService.update(param);
        redisUtil.del(SystemConstants.MDOULE_REDIS_PREFIX);
        return new SuccessResponse("更新菜单成功");
    }


    @GetMapping("/listTree")
    @ApiOperation(value = "新增菜单时，查询菜单树列表")
    @ControllerLogAnontation(type = LogConstants.CREATE_STATUS, value = "权限管理系统查询菜单树管理", moduleName = "权限管理-菜单管理")
    public Response list() {
        return new ObjectResponse(moduleService.list());
    }

    @PostMapping("/view")
    @ApiOperation(value = "查询菜单树详情")
    @ControllerLogAnontation(type = LogConstants.CREATE_STATUS, value = "权限管理系统查询菜单管理", moduleName = "权限管理-菜单管理")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response view(@RequestBody IdEntity idEntity) {
        return new ObjectResponse<>(moduleService.view(idEntity));
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑菜单树详情")
    @ControllerLogAnontation(type = LogConstants.CREATE_STATUS, value = "权限管理系统编辑菜单管理", moduleName = "权限管理-菜单管理")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response edit(@RequestBody IdEntity idEntity) {
        redisUtil.del(SystemConstants.MDOULE_REDIS_PREFIX);
        return new ObjectResponse<>(moduleService.edit(idEntity));
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除菜单")
    @ControllerLogAnontation(type = LogConstants.DELETE_STATUS, value = "删除菜单", moduleName = "权限管理-菜单管理")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response delete(@RequestBody IdEntity ids) {
        redisUtil.del(SystemConstants.MDOULE_REDIS_PREFIX);
        moduleService.delete(ids);
        return new SuccessResponse("删除成功!");
    }

    @PostMapping(value = "/pageQuery")
    @ApiOperation(value = "查询菜单列表，使用节点懒加载")
    @ControllerLogAnontation(type = LogConstants.QUERY_STATUS, value = "查询菜单列表，使用节点懒加载", moduleName = "权限管理-菜单管理")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response page(@RequestBody IdEntity idEntity) {
        return new ObjectResponse(moduleService.page(idEntity));
    }


    @PostMapping(value = "/pageTree")
    @ApiOperation(value = "查询全部菜单，权限数据")
    @ControllerLogAnontation(type = LogConstants.QUERY_STATUS, value = "查询全部菜单，权限数据", moduleName = "权限管理-菜单管理")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response pageTree(@RequestBody IdEntity idEntity) {
        String module_tree = redisUtil.get(SystemConstants.MDOULE_REDIS_PREFIX);
        List<ModuleNgZorroTreeVO> tree = null;
        if (StringUtils.isNotEmpty(module_tree)) {
            tree = JsonUtils.json2List(module_tree, ModuleNgZorroTreeVO.class);
        } else {
            tree = moduleService.pageTree(idEntity);
            // 并且将tree进行序列化
            redisUtil.set(SystemConstants.MDOULE_REDIS_PREFIX, JsonUtils.list2Json(tree), moduleExpire);
        }
        return new ObjectResponse(tree);
    }

    @PostMapping(value = "/listChildMenu")
    @ApiOperation(value = "查询二级及所有子菜单")
    public Response listChildMenu() {
        return new ObjectResponse<>(moduleService.listChildMenu());
    }

}
