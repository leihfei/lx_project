package com.lnlr.common.advice;

import com.alibaba.fastjson.JSONException;
import com.lnlr.common.exception.ParamException;
import com.lnlr.common.exception.PermissionException;
import com.lnlr.common.exception.WarnException;
import com.lnlr.common.response.FailedResponse;
import com.lnlr.common.response.Response;
import com.lnlr.common.response.ResponseEnum;
import com.lnlr.common.response.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.DataException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.persistence.NonUniqueResultException;
import java.util.List;

/**
 * @author:leihfei
 * @description 全局异常捕获
 * @date:Create in 14:18 2018/8/31
 * @email:leihfein@gmail.com
 */
@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    /**
     * 参数合法性校验异常
     * 捕获 @Valid 标记参数异常
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response validationException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error: errors) {
                return new FailedResponse<>(error.getDefaultMessage());
            }
        }
        return new SuccessResponse();
    }

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     */
    @ResponseBody
    @ExceptionHandler(value = NullPointerException.class)
    public Response errorHandler(NullPointerException ex) {
        if (ex == null) {
            return new FailedResponse<>("出现异常，但是异常未知!");
        }
        log.error("全局处理异常，出现空指针异常", ex);
        ex.printStackTrace();
        if (StringUtils.isNotEmpty(ex.getMessage())) {
            return new FailedResponse<>(ex.getMessage());
        }
        return new FailedResponse<>(ResponseEnum.FAIL_CODE.getCode(), "空指针异常!");
    }

    /**
     * 拦截捕捉自定义异常 ,权限异常
     * PermissionException.class
     *
     * @param ex
     */
    @ResponseBody
    @ExceptionHandler(value = PermissionException.class)
    public Response myErrorHandler(PermissionException ex) {
        if (ex == null) {
            return new FailedResponse<>("出现异常，但是异常未知!");
        }
        ex.printStackTrace();
        log.error("全局异常处理：权限异常", ex);
        return new FailedResponse<>(ResponseEnum.NO_PRESSION_CODE.getCode(), ex.getMessage());
    }

    /**
     * 参数异常
     *
     * @param ex
     */
    @ResponseBody
    @ExceptionHandler(value = ParamException.class)
    public Response ParamException(ParamException ex) {
        if (ex == null) {
            return new FailedResponse<>("出现异常，但是异常未知!");
        }
        ex.printStackTrace();
        log.error("全局异常处理：请求参数异常", ex);
        return new FailedResponse<>(ResponseEnum.WARN_CODE.getCode(), ex.getMessage());
    }

    /**
     * redis未开启连接异常
     *
     * @param ex
     */
    @ResponseBody
    @ExceptionHandler(value = JedisConnectionException.class)
    public Response ParamException(JedisConnectionException ex) {
        if (ex == null) {
            return new FailedResponse<>("出现异常，但是异常未知!");
        }
        ex.printStackTrace();
        log.error("全局异常处理：请求参数异常", ex);
        return new FailedResponse<>(ResponseEnum.WARN_CODE.getCode(), "redis未开启，请先启动redis服务!");
    }

    /**
     * 数据库查询结果不唯一异常
     *
     * @param ex
     */
    @ResponseBody
    @ExceptionHandler(value = NonUniqueResultException.class)
    public Response ParamException(NonUniqueResultException ex) {
        if (ex == null) {
            return new FailedResponse<>("出现异常，但是异常未知!");
        }
        ex.printStackTrace();
        log.error("全局异常处理：请求参数异常", ex);
        return new FailedResponse<>(ResponseEnum.FAIL_CODE.getCode(), "数据查询出现结果不唯一异常，请联系管理员!");
    }

    /**
     * 阿里巴巴fastJson异常
     *
     * @param ex
     */
    @ResponseBody
    @ExceptionHandler(value = JSONException.class)
    public Response JSONException(JSONException ex) {
        if (ex == null) {
            return new FailedResponse<>("出现异常，但是异常未知!");
        }
        ex.printStackTrace();
        log.error("全局异常处理：请求参数异常", ex);
        return new FailedResponse<>(ResponseEnum.FAIL_CODE.getCode(), "fastJson转换异常，请检查参数配置!");
    }

    @ResponseBody
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public Response HttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        if (ex == null) {
            return new FailedResponse<>("出现异常，但是异常未知!");
        }
        ex.printStackTrace();
        log.error("全局异常处理：请求参数异常", ex);
        return new FailedResponse<>(ResponseEnum.FAIL_CODE.getCode(), "fastJson转换异常，请检查参数配置!");
    }

    /**
     * 数据库保存异常
     *
     * @param ex
     */
    @ResponseBody
    @ExceptionHandler(value = DataException.class)
    public Response DataException(DataException ex) {
        if (ex == null) {
            return new FailedResponse<>("出现异常，但是异常未知!");
        }
        ex.printStackTrace();
        log.error("全局异常处理：请求参数异常", ex);
        return new FailedResponse<>(ResponseEnum.FAIL_CODE.getCode(), "数据库保存异常，请检查参数配置!");
    }

    /**
     * 警告异常
     *
     * @param ex
     */
    @ResponseBody
    @ExceptionHandler(value = WarnException.class)
    public Response WarnException(WarnException ex) {
        if (ex == null) {
            return new FailedResponse<>("出现异常，但是异常未知!");
        }
        ex.printStackTrace();
        log.error("全局异常处理：警告异常", ex);
        return new FailedResponse<>(ResponseEnum.WARN_CODE.getCode(), ex.getMessage());
    }

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response errorHandler(Exception ex) {
        if (ex == null) {
            return new FailedResponse<>("出现异常，但是异常未知!");
        }
        log.error("全局处理异常", ex);
        return new FailedResponse<>(ResponseEnum.FAIL_CODE.getCode(), ex.getMessage());
    }
}
