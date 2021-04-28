package com.lnlr.common.jpa.model;

import com.lnlr.common.jpa.enums.Operator;
import com.lnlr.common.jpa.enums.WhereOperator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:leihfei
 * @description 构建过滤对象
 * @date:Create in 21:12 2018/9/4
 * @email:leihfein@gmail.com
 */
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchFilter {
    /**
     * 列名
     */
    private String fieldName;

    /**
     * 数据库过滤类型
     */
    private Operator operator;


    /**
     * 过滤值
     */
    private Object value;

    /**
     * 字段间拼接条件
     * and or
     */
    private WhereOperator whereType;

    public SearchFilter(String fileName, Operator operator, Object value) {
        this.fieldName = fileName;
        this.operator = operator;
        this.value = value;
        this.whereType = WhereOperator.AND;
    }

    /**
     * @param searchParams 参数
     * @return java.util.Map<java.lang.String, com.lnlr.authority.common.jpa.model.SearchFilter>
     * @author: leihfei
     * @description 解析过滤，转为map对象
     * @date: 21:20 2018/9/4
     * @email: leihfein@gmail.com
     */
    public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = new HashMap<>(searchParams.entrySet().size());
        if (MapUtils.isNotEmpty(searchParams)) {
            searchParams.forEach((key, value) -> {
                if (StringUtils.isBlank((String) value)) {
                    return;
                }
                // 拆分operator与filed
                String[] names = StringUtils.split(key, "_");
                if (names.length != 2) {
                    throw new IllegalArgumentException(key + " is not a valid search filter name");
                }
                String filedName = names[1];
                Operator operator = Operator.valueOf(names[0]);
                // 创建searchFilter
                SearchFilter filter = new SearchFilter(filedName, operator, value, WhereOperator.AND);
                filters.put(key, filter);
            });
        }
        return filters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SearchFilter that = (SearchFilter) o;
        if (fieldName != null ? !fieldName.equals(that.fieldName) : that.fieldName != null) {
            return false;
        }
        if (value != null ? !value.equals(that.value) : that.value != null) {
            return false;
        }
        return operator == that.operator;
    }

    @Override
    public int hashCode() {
        int result = fieldName != null ? fieldName.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        return result;
    }

}
