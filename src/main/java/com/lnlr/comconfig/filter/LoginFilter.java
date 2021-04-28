package com.lnlr.comconfig.filter;

import com.lnlr.common.constains.ApplicationConstants;
import com.lnlr.common.constains.SystemConstants;
import com.lnlr.common.jwt.Audience;
import com.lnlr.common.jwt.JwtUtils;
import com.lnlr.common.utils.ExcludePathUtils;
import com.lnlr.common.utils.JsonUtils;
import com.lnlr.common.utils.RedisUtil;
import com.lnlr.common.utils.RequestHolder;
import com.lnlr.pojo.entity.SysDefaultFilter;
import com.lnlr.pojo.entity.SysUser;
import com.lnlr.service.SysExcludePathService;
import com.lnlr.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author:leihfei
 * @description 登录拦截器
 * @date:Create in 11:04 2018/10/26
 * @email:leihfein@gmail.com
 */
@Slf4j
public class LoginFilter implements Filter {

    @Autowired
    private Audience audience;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserService sysUserService;


    @Autowired
    private SysExcludePathService excludePathService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化登录拦截器,作用于获取到当前登录用户，request");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("进入LoginFilter");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getServletPath();
        String redisD = redisUtil.get(SystemConstants.DEFAULT_URL_CATCH);
        List<SysDefaultFilter> all = null;
        if (StringUtils.isEmpty(redisD)) {
            all = excludePathService.findAll();
        } else {
            all = JsonUtils.json2List(redisD, SysDefaultFilter.class);
        }
        if (!ExcludePathUtils.checkExcludeUrl(path, all)) {
            // 需要认证，由于在jwt处认证过，这里就不需要再次认证，只需要做出当前登录用户保存即可
            String header = request.getHeader(SystemConstants.AUTHORIZATION);
            String token = header.substring(SystemConstants.TOKEN_TYPE.length());
            Claims claims = JwtUtils.parseJWT(token, audience.getBase64Secret());
            if (claims == null) {
                throw new RuntimeException("无法解析登录信息，请重试!");
            }
            // 查询当前用户数据,1 从redis取，没有从数据库查
            String id = (String) claims.get(SystemConstants.JWT_LOGIN_USER);
            String objStr = redisUtil.get(id);
            SysUser sysUsers = JsonUtils.json2Object(objStr, SysUser.class);
            if (sysUsers == null) {
                // 从数据库查询
                sysUsers = sysUserService.findById(id);
                if (sysUsers == null) {
                    throw new RuntimeException("用户数据异常，请重新登录!");
                }
                redisUtil.set(id, JsonUtils.object2Json(sysUsers), ApplicationConstants.REDIS_EXPIRE_DEFAULT);
            }
            RequestHolder.add(request);
            RequestHolder.add(sysUsers);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
