package com.lnlr.common.utils;

import com.lnlr.common.exception.ParamException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;

/**
 * @author:leihfei
 * @description 利用反射，将更新的对象的值进行比对存入
 * @date:Create in 0:14 2018/10/25
 * @email:leihfein@gmail.com
 */
public class CopyUtils {

    /**
     * @param target 需要设置的对象
     * @param source 新值对象
     * @param <T>    对象类型
     * @param <V>    对象类型
     * @throws IllegalAccessException
     * @description 对对象的属性进行拷贝
     */
    public static <T, V> void copy(T target, V source) throws IllegalAccessException {
        // 得到参数的字段
        Field[] sourceFields = source.getClass().getDeclaredFields();
        // 待保存的目标字段
        Field[] targetFields = target.getClass().getDeclaredFields();
        dealReflect((V) source, (T) target, sourceFields, targetFields);
    }

    /**
     * @param target 需要设置的对象
     * @param source 新值对象
     * @param <T>    对象类型
     * @param <V>    对象类型
     * @throws IllegalAccessException
     * @description 对对象的属性进行拷贝
     */
    public static <T, V> void copyProperties(V source, T target) throws IllegalAccessException {
        // 得到参数的字段
        Field[] sourceFields = source.getClass().getDeclaredFields();
        // 待保存的目标字段
        Field[] targetFields = target.getClass().getDeclaredFields();
        dealReflect((V) source, (T) target, sourceFields, targetFields);
    }

    private static <T, V> void dealReflect(V source, T target, Field[] sourceFields, Field[] targetFields) throws IllegalAccessException {
        for (Field field : sourceFields) {
            field.setAccessible(true);
            Object value = field.get(source);
            if (value != null) {
                // 表示有值，那么需要将值写入到t对象中
                for (Field target1 : targetFields) {
                    target1.setAccessible(true);
                    if (!target1.getName().equalsIgnoreCase("serialVersionUID") && target1.getName().equals(field.getName())) {
                        // 表示为同一个字段，那么需要对目标进行赋值
                        target1.set(target, value);
                    }
                }
            }
        }
    }

    /**
     * @param source 源对象
     * @param target 目标对象
     * @return V 目标对象
     * @author: leihfei
     * @description 对象属性的拷贝
     * @date: 11:19 2018/10/30
     * @email: leihfein@gmail.com
     */
    public static <T, V> V beanCopy(T source, V target) {
        if (source == null || target == null) {
            return null;
        }
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * @param target 需要设置的对象
     * @param source 新值对象
     * @param <T>    对象类型
     * @param <V>    对象类型
     * @throws IllegalAccessException
     * @description 对对象的属性进行拷贝
     */
    public static <T, V> void copyProperties2(V source, T target) {
        // 得到参数的字段
        Field[] sourceFields = source.getClass().getDeclaredFields();
        // 待保存的目标字段
        Field[] targetFields = target.getClass().getDeclaredFields();
        try {
            dealReflect((V) source, (T) target, sourceFields, targetFields);
        } catch (IllegalAccessException e) {
            throw new ParamException("对象拷贝异常");
        }
    }

    public static <T> T get(Class<T> clz, Object o) {
        if (clz.isInstance(o)) {
            return clz.cast(o);
        }
        return null;
    }
}
