package com.lnlr.controller;

import com.google.common.collect.Maps;
import com.lnlr.common.annonation.ControllerLogAnontation;
import com.lnlr.common.constains.LogConstants;
import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.exception.ParamException;
import com.lnlr.common.jpa.model.MatchModel;
import com.lnlr.common.jpa.model.NgData;
import com.lnlr.common.jpa.model.NgFilter;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.common.response.FailedResponse;
import com.lnlr.common.response.ObjectResponse;
import com.lnlr.common.response.Response;
import com.lnlr.common.response.SuccessResponse;
import com.lnlr.pojo.dto.ExcelDownDTO;
import com.lnlr.pojo.entity.SysRole;
import com.lnlr.pojo.entity.SysUser;
import com.lnlr.pojo.entity.SysUserRole;
import com.lnlr.pojo.param.base.AuthorityParam;
import com.lnlr.pojo.param.base.CheckPassParam;
import com.lnlr.pojo.param.base.UserParam;
import com.lnlr.service.RoleService;
import com.lnlr.service.SysUserRoleService;
import com.lnlr.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author:leihfei
 * @description 权限管理系统用户控制层
 * @date:Create in 19:49 2018/8/30
 * @email:leihfein@gmail.com
 */
@RestController
@RequestMapping(value = "/sys/user")
@Api(value = "系统用户", description = "系统用户管理类")
@Slf4j
public class SysUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @PostMapping(value = "/findAll")
    @ApiOperation(value = "查询用户信息列表（不包含检查站用户）")
    public Response findAll() {
        return new ObjectResponse<>(userService.findAll());
    }

    @PostMapping(value = "/checkPass")
    @ApiOperation(value = "检查用户旧密码")
    @ControllerLogAnontation(type = LogConstants.CHECK_STATUS, value = "检查旧密码", moduleName = "用户管理")
    @ApiImplicitParam(dataTypeClass = CheckPassParam.class, paramType = "query")
    public Response checkPass(@RequestBody CheckPassParam checkPassParam) {
        return new ObjectResponse<>(userService.checkPass(checkPassParam));
    }

    @PostMapping(value = "/updatePass")
    @ApiOperation(value = "更新密码")
    @ControllerLogAnontation(type = LogConstants.UPDATE_STATUS, value = "更新密码", moduleName = "用户管理")
    @ApiImplicitParam(dataTypeClass = CheckPassParam.class, paramType = "query")
    public Response updatePass(@RequestBody CheckPassParam checkPassParam) {
        if (userService.updatePass(checkPassParam)) {
            return new ObjectResponse<>("密码更新成功!");
        }
        return new FailedResponse<>("密码更新失败!");
    }

    @PostMapping(value = "/updateUserStatus")
    @ApiOperation(value = "更改用户状态")
    @ControllerLogAnontation(type = LogConstants.UPDATE_STATUS, value = "更新用户状态", moduleName = "用户管理")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response updateUserStatus(@RequestBody IdEntity idEntity) {
        userService.updateStatus(idEntity);
        return new SuccessResponse("状态更新成功!");
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "删除用户")
    @ControllerLogAnontation(type = LogConstants.DELETE_STATUS, value = "删除用户", moduleName = "用户管理")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response delete(@RequestBody IdEntity idEntity) {
        userService.delete(idEntity);
        return new SuccessResponse("删除成功!");
    }

    @PostMapping("/auths")
    @ApiOperation(value = "查询用户角色数据")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response auths(@RequestBody IdEntity idEntity) {
        Map<String, Object> map = Maps.newHashMap();
        // 通过用户id查询已分配角色
        List<SysUserRole> allRoleListByUserId = userRoleService.findAllRoleListByUserId(idEntity.getId());
        Set<String> collect = allRoleListByUserId.stream().map(e -> e.getRoleId()).collect(Collectors.toSet());
        // 再查询一次角色
        List<SysRole> roles = roleService.findAllByRoleIds(collect);
        // 已分配角色数据
        map.put("auths", roles);
        // 未分配角色数据
        // 校验一次当前用户是否是本管理员下的用户，不是不予分配
        SysUser byId = userService.findById(idEntity.getId());
        if (byId == null) {
            throw new ParamException("用户不存在,权限异常!");
        }
        // 查询数据,通过管理员的组织机构id查询角色数据
        collect.add("-556de");
        List<SysRole> noAuths = roleService.findAllByIdNotIn(collect);
        map.put("roles", noAuths);
        return new ObjectResponse<>(map);
    }

    @PostMapping(value = "/authority")
    @ApiOperation(value = "分配角色给用户")
    @ApiImplicitParam(dataTypeClass = AuthorityParam.class, paramType = "query")
    public Response authority(@RequestBody AuthorityParam authorityParam) {
        userService.authority(authorityParam);
        return new SuccessResponse("授予角色成功!");
    }

    @PostMapping(value = "/unAuthority")
    @ApiOperation(value = "撤销用户角色")
    @ApiImplicitParam(dataTypeClass = AuthorityParam.class, paramType = "query")
    public Response unAuthority(@RequestBody AuthorityParam authorityParam) {
        userService.unAuthority(authorityParam);
        return new SuccessResponse("撤销角色成功!");
    }

    @PostMapping(value = "/resetPassword")
    @ApiOperation(value = "管理员进行密码重置")
    @ApiImplicitParam(dataTypeClass = IdEntity.class, paramType = "query")
    public Response resetPassword(@RequestBody IdEntity entity) {
        userService.resetPassword(entity);
        return new SuccessResponse("密码重置成功!");
    }


    @PostMapping(value = "/list")
    @ApiOperation(value = "下拉查看所有用户")
    public Response list() {
        return new ObjectResponse<>(userService.list());
    }

    @PostMapping(value = "/page")
    @ApiOperation(value = "分页查询用户列表")
    @ControllerLogAnontation(type = LogConstants.QUERY_STATUS, value = "分页查询用户列表", moduleName = "平台管理-用户")
    @ApiImplicitParam(dataTypeClass = NgPager.class, paramType = "query")
    public Response page(@RequestBody NgPager ngPager) {
        NgData<Object> ngData = userService.page(ngPager);
        return new ObjectResponse<>(ngData);
    }
    @PostMapping(value = "/create")
    @ApiOperation(value = "新增用户")
    @ControllerLogAnontation(type = LogConstants.CREATE_STATUS, value = "新增用户", moduleName = "平台管理-用户")
    @ApiImplicitParam(dataTypeClass = UserParam.class, paramType = "query")
    public Response create(@RequestBody UserParam param) {
        return userService.create(param);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "更新用户")
    @ControllerLogAnontation(type = LogConstants.UPDATE_STATUS, value = "更新用户", moduleName = "平台管理-用户")
    @ApiImplicitParam(dataTypeClass = UserParam.class, paramType = "query")
    public Response update(@RequestBody UserParam param) {
        return userService.update(param);
    }

    @GetMapping(value = "/exportExcel")
    @ApiOperation(value = "导出数据")
    @ControllerLogAnontation(type = LogConstants.QUERY_STATUS, value = "导出数据", moduleName = "平台管理-学生")
    @ApiImplicitParam(dataTypeClass = NgPager.class, paramType = "query")
    public void exportExcel(@ModelAttribute ExcelDownDTO dto, HttpServletRequest request, HttpServletResponse servletResponse) {
        NgPager ngPager = new NgPager();
        ngPager.setRows(2000);
        Map<String, NgFilter> filters = ngPager.getFilters();
        if (filters == null) {
            ngPager.setFilters(filters);
        }
        if (StringUtils.isNotEmpty(dto.getName())) {
            filters.put("realName", new NgFilter(dto.getName(), MatchModel.CONTAINS));
        }
        if (StringUtils.isNotEmpty(dto.getNumber())) {
            filters.put("tealphone", new NgFilter(dto.getNumber(), MatchModel.CONTAINS));
        }
        ngPager.setFilters(filters);
        userService.exportExcel(ngPager, request, servletResponse);
    }

}
