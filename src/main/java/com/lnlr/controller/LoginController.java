package com.lnlr.controller;

import com.google.common.collect.Lists;
import com.lnlr.common.annonation.ControllerLogAnontation;
import com.lnlr.common.constains.ApplicationConstants;
import com.lnlr.common.constains.SystemConstants;
import com.lnlr.common.jwt.Audience;
import com.lnlr.common.jwt.JwtUtils;
import com.lnlr.common.response.*;
import com.lnlr.common.utils.*;
import com.lnlr.pojo.dao.SysUserDAO;
import com.lnlr.pojo.entity.*;
import com.lnlr.pojo.param.base.LoginParam;
import com.lnlr.pojo.vo.auth.LoginResultVO;
import com.lnlr.pojo.vo.auth.ModuleNgZorroTreeVO;
import com.lnlr.pojo.vo.auth.UserVO;
import com.lnlr.service.ModuleServices;
import com.lnlr.service.PatchcaService;
import com.lnlr.service.RoleService;
import com.lnlr.service.UserService;
import com.lnlr.service.impl.DefaultUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author:leihfei
 * @description 登录控制器
 * @date:Create in 17:41 2018/9/14
 * @email:leihfein@gmail.com
 */
@Controller
@RequestMapping(value = "/sys/authorizing")
@Api(value = "登录管理", description = "权限管理系统登录管理")
@Slf4j
public class LoginController {


    /**
     * 用户业务接口
     */
    private final UserService userService;

    /**
     * jwt业务
     */
    private final Audience audience;

    /**
     * 默认url业务
     */
    private final DefaultUrlService defaultUrlService;

    /**
     * redis
     */
    private final RedisUtil redisUtil;


    /**
     * 验证码处理service
     */
    @Autowired
    private PatchcaService patchcaService;

    /**
     * 角色业务接口
     */
    @Autowired
    private RoleService roleService;

    @Autowired
    private ModuleServices moduleService;


    @Autowired
    public LoginController(
            UserService userService,
            Audience audience,
            DefaultUrlService defaultUrlService,
            RedisUtil redisUtil,
            SysUserDAO userViewDAO) {
        this.userService = userService;
        this.audience = audience;
        this.defaultUrlService = defaultUrlService;
        this.redisUtil = redisUtil;
    }


    @GetMapping(value = "/logout")
    @ResponseBody
    @ApiOperation(value = "退出登录")
    public Response logout() {
        // 清理redis缓存
        return new SuccessResponse("退出成功!");
    }

    @PostMapping(value = "/login")
    @ResponseBody
    @ApiOperation(value = "登录")
    @ApiImplicitParam(name = "loginParam", value = "登录用户信息", required = true, dataTypeClass = LoginParam.class, paramType = "query")
    public Response login(@RequestBody LoginParam loginParam, HttpServletRequest request) {
        return login(loginParam, request, true);
    }

    @PostMapping(value = "/loginNoCode")
    @ResponseBody
    @ApiOperation(value = "无验证码登录")
    @ApiImplicitParam(name = "loginParam", value = "登录用户信息", required = true, dataTypeClass = LoginParam.class, paramType = "query")
    public Response loginNoCode(@RequestBody LoginParam loginParam, HttpServletRequest request) {
        String username = loginParam.getUsername();
        SysUser user = userService.findKeyword(username);
        if (user == null) {
            return new FailedResponse<>("账号或密码不存在！");
        }
        if (user.getUserType() == 1) {
            return new FailedResponse<>("学生不能登录该系统！");
        }
        return login(loginParam, request, false);
    }

    @PostMapping(value = "/loginApp")
    @ResponseBody
    @ApiOperation(value = "app登录")
    @ApiImplicitParam(name = "loginParam", value = "登录用户信息", required = true, dataTypeClass = LoginParam.class, paramType = "query")
    public Response loginApp(@RequestBody LoginParam loginParam, HttpServletRequest request) {
        String username = loginParam.getUsername();
        SysUser user = userService.findKeyword(username);
        if (user == null) {
            return new FailedResponse<>("账号或密码不存在！");
        }
        loginParam.setCode("app");
        loginParam.setQueryParam("app");
        return login(loginParam, request, false);
    }

    /**
     * 执行登录
     *
     * @param loginParam
     * @param request
     * @param needPatchca 是否需要验证码
     * @return
     */
    private Response login(LoginParam loginParam, HttpServletRequest request, boolean needPatchca) {
        log.info("IP:{} 尝试登录，登录数据:{}", IpUtils.getIpAddr(request), loginParam);
        BeanValidator.check(loginParam);

        // 校验验证码
        if (needPatchca) {
            if (patchcaService.validateExpire(loginParam.getQueryParam())) {
                return new FailedResponse<>("验证码过期！");
            }
            if (!patchcaService.validate(loginParam.getQueryParam(), loginParam.getCode())) {
                return new FailedResponse<>("验证码错误！");
            }
        }
        Response ret;
        SysUser user = userService.findKeyword(loginParam.getUsername());
        if (user == null) {
            ret = new FailedResponse<>("用户不存在!");
        } else {
            // 当用户存在的时候
            if (user.getStatus() != ApplicationConstants.ENABLED) {
                // 用户被锁定
                ret = new FailedResponse<>("用户被锁定!");
            } else {
                // 判断密码是否相等匹配
                boolean validate = PassUtils.validatePassword(loginParam.getPassword(),
                        user.getPassword(), user.getSalt());
                if (!validate) {
                    ret = new FailedResponse<>("用户名或密码错误!");
                } else {
                    // 登录成功数据，返回jwt,返回菜单数据，返回权限数据
                    ret = loginSuccess(user);
                }
            }
        }
        log.info("登录返回信息：{}", ret);
        return ret;
    }


    /**
     * @param user 登录user
     * @return com.lnlr.common.response.Response
     * @author: leihfei
     * @description 返回登录用户的权限，菜单，jwt等数据
     * @date: 0:34 2018/10/19
     * @email: leihfein@gmail.com
     */
    @ControllerLogAnontation(type = "query", value = "", moduleName = "用户登陆")
    private Response loginSuccess(SysUser user) {
        // 存入RequesHolder中
        RequestHolder.add(user);
        UserVO userVO = CopyUtils.beanCopy(user, new UserVO());
        //todo设置头像资源
        // 返回jwt数据UserVO
        LoginResultVO vo = new LoginResultVO(userVO, JwtUtils.createJWt(audience, userVO.getUsername(), String.valueOf(userVO.getId())));
        // 获取一下菜单
        createModule(user.getId(), vo);
        List<String> roles = getRoleNames(user);
        // 存储redis缓存用户信息
        redisUtil.set(String.valueOf(user.getId()), JsonUtils.object2Json(user), ApplicationConstants.REDIS_EXPIRE_DEFAULT);
        return new ObjectResponse<>(vo);
    }


    /**
     * @param user 用户信息
     * @return java.util.List<java.lang.String>
     * @author leihfei
     * @description 得到用户角色信息
     * @date 16:55:10 2019-04-24
     */
    private List<String> getRoleNames(SysUser user) {
        // 查询当前用户的角色信息
        List<SysUserRole> allByUserId = roleService.findAllByUserId(user.getId());
        Set<String> collect = allByUserId.stream().map(e -> e.getRoleId()).collect(Collectors.toSet());
        List<SysRole> allByRoleIds = roleService.findAllByRoleIds(collect);
        List<String> roles = Lists.newArrayList();
        allByRoleIds.forEach(item -> {
            roles.add(item.getName());
        });
        return roles;
    }


    /**
     * @param userId 用户id
     * @param vo     返回的视图
     * @return void
     * @author leihfei
     * @description 处理登录之后返回的权限点和菜单
     * @date 13:56:47 2019-05-12
     */
    private void createModule(String userId, LoginResultVO vo) {
        // 查询所有的module
        List<SysModule> allModules = moduleService.findAllBySortShowLevel();

        // 进行处理过滤
        Set<String> userModuleIds = roleService.findUserRolesByUserId(userId);
        List<SysModule> currentUserModule = allModules.stream()
                .filter(e -> userModuleIds.contains(e.getId()))
                .collect(Collectors.toList());
        // 得到当前用户的所有modules
        List<SysModule> currentModules = currentUserModule.stream().filter(e -> e.getStatus() == 1)
                // 过滤菜单为0
                .filter(e -> e.getType() == 0)
                .collect(Collectors.toList());
        // 得到属性结构
        List<ModuleNgZorroTreeVO> tree = moduleService.tree(currentModules);
        vo.setMenus(tree);
        // 处理用户的权限点数据，其实数据就存在SysModule中，值需要取出即可
        Set<String> collect = currentUserModule.stream().filter(e -> e.getStatus() == 1)
                // 过滤菜单为0
                .filter(e -> e.getType() != 0).map(e -> e.getAuthKey()).map(e -> String.valueOf(e)).
                        collect(Collectors.toSet());
        vo.setAuths(collect);
    }

    /**
     * 重新生成token
     */
    @GetMapping(value = "/create_token")
    @ResponseBody
    public Response create_token(HttpServletRequest request) {
        String userId = (String) request.getAttribute(SystemConstants.JWT_LOGIN_USER);
        String userName = (String) request.getAttribute(SystemConstants.JWT_LOGIN_USERNAME);
        String token = JwtUtils.createJWt(audience, userName, userId);
        log.info("重新生成token信息:{}", token);
        return new ObjectResponse(ResponseEnum.AGAIN_TOKEN_CODE.getMessage(), token);
    }


    /**
     * 当没有权限访问时进入此处
     */
    @GetMapping(value = "/unauthorized")
    @ResponseBody
    public Response unauthorized() {
        return new FailedResponse<>(ResponseEnum.NO_PRESSION_CODE.getCode(), "您没有权限访问!");
    }


    /**
     * 对拦截过滤重新进行赋值
     * 刷新拦截过滤url
     */
    @GetMapping(value = "flush")
    @ResponseBody
    public Response flush() {
        // 重新保存redis
        List<SysDefaultFilter> all = defaultUrlService.findAll();
        redisUtil.set(SystemConstants.DEFAULT_URL_CATCH, JsonUtils.list2Json(all), SystemConstants.DEFAULT_URL_CATCH_TIME);
        return new ObjectResponse<>(all);
    }
}
