package com.lnlr.common.interceptor;

import com.lnlr.common.utils.JsonUtils;
import com.lnlr.common.utils.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author:leihfei
 * @description http启动监听器，拦截器
 * @date:Create in 1:16 2018/9/3
 * @email:leihfein@gmail.com
 */
@Slf4j
@Component
public class HttpInterceptor implements HandlerInterceptor {

    private final static String CURRENT_TIME = "current_time";

    /**
     * 请求过来之前处理
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        Map<String, String[]> parameterMap = request.getParameterMap();
        log.info("{请求开始时： 当前url:{},params:{}", url, JsonUtils.object2Json(parameterMap));
        request.setAttribute(CURRENT_TIME, System.currentTimeMillis());
        // 把request请求放入其中
        RequestHolder.add(request);
        return true;
    }

    /**
     * 请求正常结束之后调用
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String url = request.getRequestURL().toString();
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        log.info("rquest finish {url:{},params:{}", url, JsonUtils.object2Json(parameterMap));
        removeThreadLocalInfo();
    }

    /**
     * 请求正常结束之后调用
     * 异常情况下也会调用
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String url = request.getRequestURL().toString();
        long time = (Long) request.getAttribute(CURRENT_TIME);
        log.info("请求结束时： 当前请求url:{},花费:{}", url, System.currentTimeMillis() - time);
        removeThreadLocalInfo();
    }

    /**
     * 移除本地线程信息
     */
    public void removeThreadLocalInfo() {
        RequestHolder.remove();
    }
}
