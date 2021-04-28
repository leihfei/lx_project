package com.lnlr.common.aspect;

import com.lnlr.common.annonation.ControllerLogAnontation;
import com.lnlr.common.annonation.ServiceLogAnonation;
import com.lnlr.common.response.Response;
import com.lnlr.common.service.SysOperatorLogService;
import com.lnlr.common.utils.IpUtils;
import com.lnlr.common.utils.JsonUtils;
import com.lnlr.common.utils.RequestHolder;
import com.lnlr.pojo.entity.SysOperatorLog;
import com.lnlr.pojo.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author:leihfei
 * @description:
 * @date:Create in 19:18 2018/11/29
 * @email:leihfein@gmail.com
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysOperatorLogService operatorLogService;

    /**
     * Controller层切点 使用到了我们定义的 ControllerLog 作为切点表达式。
     * 而且我们可以看出此表达式是基于 annotation 的。
     */
    @Pointcut("@annotation(com.lnlr.common.annonation.ControllerLogAnontation)")
    public void controllerAspect() {
    }

    /**
     * Service层切点 使用到了我们定义的 ServiceLog 作为切点表达式。
     * 而且我们可以看出此表达式基于 annotation。
     */
    @Pointcut("@annotation(com.lnlr.common.annonation.ServiceLogAnonation)")
    public void serviceAspect() {
    }

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 连接点
     */
    @AfterReturning(pointcut = "controllerAspect()", returning = "response")
    public void doAfterReturningController(JoinPoint joinPoint, Response response) {
        SysUser sysUser = RequestHolder.currentUser();
        try {
            String params = "";
            /*
             * 如果需要区分角色，从session中取出保存的用户，则Controller函数的第一个参数为HttpServletRequest
             * 而对于登录、修改密码不记录参数，防止日志管理员看到不同角色的用户参数
             */
            if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    Object o = joinPoint.getArgs()[i];
                    if (o instanceof MultipartFile) {
                        params += "文件";
                    } else {
                        params += JsonUtils.object2Json(joinPoint.getArgs()[i]) + ";";
                    }
                }
            }
            SysOperatorLog log = getControllerMethodAnnotationValue(joinPoint);
            log.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
            if (RequestHolder.currentUser() != null) {
                log.setOperator(RequestHolder.currentUser().getRealName());
            }
            log.setGmtCreate(LocalDateTime.now());
            log.setOperValue(JsonUtils.object2Json(response));
            //保存数据库
            operatorLogService.create(log);
        } catch (Exception e) {
            e.printStackTrace();
            //记录本地异常日志
            log.error("==后置Controller通知异常==");
            log.error("异常信息:{}", e.getMessage());
        }
    }

    /**
     * 后置通知 用于拦截Service层记录用户的操作,用于记录系统自动清空日志操作
     *
     * @param joinPoint 连接点
     */
    @After("serviceAspect()")
    public void doAfterService(JoinPoint joinPoint) {
        try {
            //构造数据库日志对象
            SysOperatorLog log = getServiceMethodAnnotationValue(joinPoint);
            //保存数据库
            log.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
            if (RequestHolder.currentUser() != null) {
                log.setOperator(RequestHolder.currentUser().getRealName());
            }
            log.setGmtCreate(LocalDateTime.now());
            operatorLogService.create(log);
        } catch (Exception e) {
            //记录本地异常日志
            e.printStackTrace();
            log.error("==后置Service通知异常==");
            log.error("异常信息:{}", e.getMessage());
        }
    }

    /**
     * @param joinPoint
     * @return com.xzl.security.pojo.master.entity.SysOperatorLog
     * @author leihfei
     * @description 获取Controller中注解值
     * @date 10:21 2019/2/16
     */
    public static SysOperatorLog getControllerMethodAnnotationValue(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        SysOperatorLog annotationValue = new SysOperatorLog();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    String type = method.getAnnotation(ControllerLogAnontation.class).type();
                    String moduleName = method.getAnnotation(ControllerLogAnontation.class).moduleName();
                    annotationValue.setOperationType(type);
                    annotationValue.setModuleName(moduleName);
                    break;
                }
            }
        }
        annotationValue.setMethodName(methodName);
        annotationValue.setClassName(targetName);
        return annotationValue;
    }

    /**
     * @param joinPoint
     * @return com.xzl.security.pojo.master.entity.SysOperatorLog
     * @author leihfei
     * @description 获取Service中注解值
     * @date 10:21 2019/2/16
     */
    public static SysOperatorLog getServiceMethodAnnotationValue(JoinPoint joinPoint) throws Exception {
        // 类全路径
        String targetName = joinPoint.getTarget().getClass().getName();
        // 方法名称
        String methodName = joinPoint.getSignature().getName();
        // 其中的参数
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        SysOperatorLog annotationValue = new SysOperatorLog();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    String type = method.getAnnotation(ServiceLogAnonation.class).type();
                    String moduleName = method.getAnnotation(ServiceLogAnonation.class).moduleName();
                    annotationValue.setOperationType(type);
                    annotationValue.setModuleName(moduleName);
                    break;
                }
            }
        }
        annotationValue.setOperValue(JsonUtils.object2Json(arguments));
        annotationValue.setMethodName(methodName);
        annotationValue.setClassName(targetName);
        return annotationValue;
    }
}
