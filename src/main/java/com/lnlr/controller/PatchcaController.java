package com.lnlr.controller;

import com.lnlr.service.PatchcaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author:leihfei
 * @description: 验证码生成器
 * @date:Create in 9:08 2018/8/8
 * @email:leihfein@gmail.com
 */
@Controller
@RequestMapping(value = "/servlet")
@Slf4j
@Api(value = "验证码控制器", description = "验证码控制器类")
public class PatchcaController {

    /**
     * 验证码服务
     */
    @Autowired
    private PatchcaService patchcaService;

    /**
     * @param action   名称
     * @param appid    APPID
     * @param key      密匙
     * @param request
     * @param response
     * @return 为空，图片以流的形式写回前端页面
     * @author: leihfei
     * @description: 获取验证码接口
     * @date: 10:12 2018/8/10
     * @email: leihfein@gmail.com
     */

    @ApiOperation(value = "获取验证码")
    @RequestMapping(value = "/patchcaServlet", method = RequestMethod.GET)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "action", value = "一个任意名称"),
            @ApiImplicitParam(name = "appid", value = "前端随机生成的uuid"),
            @ApiImplicitParam(name = "key", value = "前端生成的时间戳")
    })
    public void doGet(String action, String appid, String key, HttpServletRequest request, HttpServletResponse response) {
        try {
            patchcaService.doGet(action, appid, key, request, response);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("生成验证码失败！");
        }
    }

}
