package com.lnlr.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author:leihfei
 * @description json操作工具类
 * @date:Create in 13:06 2018/8/31
 * @email:leihfein@gmail.com
 */
public class JsonUtils {
    /**
     * java对象转换为JSON字符串
     *
     * @param object
     * @return
     */
    public static <T> String object2Json(T object) {
        return JSON.toJSONString(object);
    }

    /**
     * JSON字符串转换为Java对象
     *
     * @param json
     * @param obj
     * @return
     */
    public static <T> T json2Object(String json, Class<T> obj) {
        JSONObject jsonObject = JSON.parseObject(json);
        return (T) JSON.toJavaObject(jsonObject, obj);
    }

    /**
     * List集合转换为JSON字符串
     *
     * @param list
     * @return
     */
    public static <T> String list2Json(List<T> list) {
        return JSONArray.toJSONString(list);
    }

    /**
     * 将JSON字符串转换为List集合
     *
     * @param json
     * @param obj
     * @return
     */
    public static <T> List<T> json2List(String json, Class<T> obj) {
        return JSON.parseArray(json, obj);
    }
}
