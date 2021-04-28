package com.lnlr.common.jpa.query;

import com.lnlr.common.jpa.model.SearchFilter;
import com.lnlr.common.jpa.enums.Operator;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author:leihfei
 * @description 过滤条件转换工具类
 * @date:Create in 11:41 2018/9/5
 * @email:leihfein@gmail.com
 */
@Slf4j
public class SearchFilterUtils {

    private static final List<String> PRIMITIVE = Arrays.asList("class java.lang.String",
            "class java.lang.Integer",
            "class java.lang.Boolean",
            "class java.lang.Long",
            "class java.lang.Byte",
            "class java.lang.Character",
            "class java.lang.Short",
            "class java.lang.Double",
            "class java.lang.Float",
            "class java.time.LocalDate",
            "class java.time.LocalDateTime");

    public static Collection<SearchFilter> build(Object obj) {
        log.info("进行构建本次jpa查询过滤数组,数据为:{}", obj);
        List<SearchFilter> filters = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
//            log.info("查询过滤字段类型", type.toString());
            try {
                Object o = field.get(obj);
                if (o != null) {
                    if (String.class.equals(type)
                            && !field.getName().contains("id")
                            && !field.getName().contains("Id")) {
                        filters.add(new SearchFilter(field.getName(), Operator.LIKE, o));
                    } else if (PRIMITIVE.contains(type.toString())) {
                        filters.add(new SearchFilter(field.getName(), Operator.EQ, o));
                    } else if (type.toString().startsWith("class com.xzl.operation")) {
                        filters.addAll(buildChild(o, field.getName()));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return filters;
    }

    private static Collection<SearchFilter> buildChild(Object obj, String parent) {
        List<SearchFilter> filters = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();
            log.info("查询过滤字段类型", type.toString());
            try {
                Object o = field.get(obj);
                if (o != null) {
                    if (String.class.equals(type)
                            && !field.getName().contains("id")
                            && !field.getName().contains("Id")) {
                        filters.add(new SearchFilter(parent + "." + field.getName(), Operator.LIKE, o));
                    } else if (PRIMITIVE.contains(type.toString())) {
                        filters.add(new SearchFilter(parent + "." + field.getName(), Operator.EQ, o));
                    } else if (type.toString().startsWith("class com.xzl.common.operation")) {
                        filters.addAll(buildChild(o, parent + "." + field.getName()));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return filters;
    }


}

