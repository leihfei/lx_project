package com.lnlr.controller;

import com.lnlr.common.annonation.ControllerLogAnontation;
import com.lnlr.common.constains.LogConstants;
import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.common.response.ObjectResponse;
import com.lnlr.common.response.Response;
import com.lnlr.common.response.SuccessResponse;
import com.lnlr.pojo.param.base.AuthParam;
import com.lnlr.pojo.param.base.RoleAuthParam;
import com.lnlr.pojo.param.base.RoleParam;
import com.lnlr.service.RoleService;
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
 * @author leihf
 * @email leihfein@gmail.com
 * @description 角色管理
 * @date 2019-04-10 11:09:14
 */
@RestController
@RequestMapping(value = "/sys/role")
@Api(value = "角色管理", description = "权限管理系统角色管理")
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;


    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询角色")
    @ControllerLogAnontation(type = LogConstants.QUERY_STATUS, value = "分页查询角色", moduleName = "权限管理-角色管理")
    @ApiImplicitParam(dataTypeClass = NgPager.class, paramType = "query")
    public Response page(@RequestBody NgPager ngPager) {
        return new ObjectResponse(roleService.page(ngPager));
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新角色接口")
    @ControllerLogAnontation(type = LogConstants.CREATE_STATUS, value = "权限管理系统新更新角色管理", moduleName = "权限管理-更新管理")
    @ApiImplicitParam(dataTypeClass = RoleParam.class, paramType = "query")
    public Response update(@RequestBody RoleParam param) {
        roleService.update(param);
        return new SuccessResponse("更新角色成功");
    }

    @PostMapping("/create")
    @ApiOperation(value = "新增角色接口")
    @ControllerLogAnontation(type = LogConstants.CREATE_STATUS, value = "权限管理系统新增角色管理", moduleName = "权限管理-角色管理")
    @ApiImplicitParam(dataTypeClass = RoleParam.class, paramType = "query")
    public Response create(@RequestBody RoleParam param) {
        roleService.create(param);
        return new SuccessResponse("新增角色成功");
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除角色")
    @ControllerLogAnontation(type = LogConstants.DELETE_STATUS, value = "删除角色", moduleName = "权限管理-角色管理")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response delete(@RequestBody IdEntity ids) {
        roleService.delete(ids);
        return new SuccessResponse("删除成功!");
    }

    @PostMapping(value = "/auth")
    @ApiOperation(value = "角色授权")
    @ControllerLogAnontation(type = LogConstants.DELETE_STATUS, value = "角色授权", moduleName = "权限管理-角色管理")
    @ApiImplicitParam(dataTypeClass = RoleAuthParam.class, paramType = "query")
    public Response auth(@RequestBody RoleAuthParam param) {
        roleService.auth(param);
        return new SuccessResponse("角色授权成功!");
    }

    @PostMapping(value = "/viewAuth")
    @ApiOperation(value = "查询角色权限")
    @ControllerLogAnontation(type = LogConstants.DELETE_STATUS, value = "查询角色权限", moduleName = "权限管理-角色管理")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response viewAuth(@RequestBody IdEntity param) {
        return roleService.viewAuth(param);
    }


    @PostMapping(value = "/showAuth")
    @ApiOperation(value = "用户查询自己角色信息")
    @ControllerLogAnontation(type = LogConstants.DELETE_STATUS, value = "用户查询自己角色信息", moduleName = "权限管理-角色管理")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response showAuth(@RequestBody IdEntity param) {
        return roleService.showAuth(param);
    }


    @PostMapping(value = "/queryUserAuth")
    @ApiOperation(value = "角色查询哪些用户分配了该角色")
    @ControllerLogAnontation(type = LogConstants.DELETE_STATUS, value = "角色查询哪些用户分配了该角色", moduleName = "权限管理-角色管理")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response queryUserAuth(@RequestBody IdEntity param) {
        return roleService.queryUserAuth(param);
    }

    @PostMapping(value = "/deleteAuth")
    @ApiOperation(value = "删除该用户下的角色")
    @ControllerLogAnontation(type = LogConstants.DELETE_STATUS, value = "删除该用户下的角色", moduleName = "权限管理-角色管理")
    @ApiImplicitParam(dataTypeClass = AuthParam.class, paramType = "query")
    public Response deleteAuth(@RequestBody AuthParam param) {
        return roleService.deleteAuth(param);
    }


    @PostMapping(value = "/pageQueryUser")
    @ApiOperation(value = "查询未分配该角色的用户")
    @ControllerLogAnontation(type = LogConstants.DELETE_STATUS, value = "查询未分配该角色的用户", moduleName = "权限管理-角色管理")
    @ApiImplicitParam(dataTypeClass = NgPager.class, paramType = "query")
    public Response pageQueryUser(@RequestBody NgPager param) {
        return roleService.pageQueryUser(param);
    }


    @PostMapping(value = "/userAuth")
    @ApiOperation(value = "用户授权")
    @ControllerLogAnontation(type = LogConstants.DELETE_STATUS, value = "用户授权", moduleName = "权限管理-角色管理")
    @ApiImplicitParam(dataTypeClass = AuthParam.class, paramType = "query")
    public Response userAuth(@RequestBody AuthParam param) {
        return roleService.userAuth(param);
    }
}


