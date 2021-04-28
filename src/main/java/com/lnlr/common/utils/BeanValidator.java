package com.lnlr.common.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lnlr.common.exception.ParamException;
import com.lnlr.common.exception.WarnException;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @author:leihfei
 * @description 验证工具
 * @date:Create in 0:20 2018/9/3
 * @email:leihfein@gmail.com
 */
public class BeanValidator {

    public static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static <T> Map<String, String> validator(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set validatorResult = validator.validate(t, groups);
        if (validatorResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap errors = Maps.newLinkedHashMap();
            Iterator iterator = validatorResult.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation violation = (ConstraintViolation) iterator.next();
                errors.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errors;
        }
    }

    public static Map<String, String> validateList(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;
        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validator(object, new Class[0]);
        } while (errors.isEmpty());
        return errors;
    }

    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validator(first, new Class[0]);
        }
    }

    public static void check(Object param) throws ParamException {
        Map<String, String> validator = BeanValidator.validator(param);
        if (MapUtils.isNotEmpty(validator)) {
            List<String> errMsg = new ArrayList<>();
//            validator.forEach((K, V) -> errMsg.add(K + V));
            validator.forEach((K, V) -> errMsg.add(V));
            throw new WarnException(errMsg.toString());
        }
    }

}
