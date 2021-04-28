package com.lnlr.common.jpa.query;

import com.google.common.collect.Lists;
import com.lnlr.common.jpa.enums.WhereOperator;
import com.lnlr.common.jpa.model.SearchFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author:leihfei
 * @description 动态构建jpa查询语言
 * @date:Create in 21:08 2018/9/4
 * @email:leihfein@gmail.com
 */
@Slf4j
public class DynamicSpecifications {
    /**
     * 用于存储每个线程的request请求
     */
    private static final ThreadLocal<HttpServletRequest> LOCAL_REQUEST = new ThreadLocal<>();
    /**
     * 年月日时间格式
     */
    private static final String SHORT_DATE = "yyyy-MM-dd";
    /**
     * 年月日时分秒时间格式
     */
    private static final String LONG_DATE = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时分秒时间格式
     */
    private static final String TIME = "HH:mm:ss";

    private static final String PERCENTAGE_NUMBER = "%";

    private static final String SPOT = ".";

    private static final String COMMA = ",";


    /**
     * @param entityClazz 实体类对象
     * @param filterSet   条件
     * @return org.springframework.data.jpa.domain.Specification<T>
     * @author: leihfei
     * @description 构建动态查询语句
     * root 表示要构建的实体类信息
     * query 相当于查询数据库中关键字，是一个接口
     * builder 构建查询信息
     * @date: 13:50 2018/12/6
     * @email: leihfein@gmail.com
     */
    public static <T> Specification<T> bySearchFilter(final Class<T> entityClazz, final Set<SearchFilter> filterSet) {
        return (Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            if (CollectionUtils.isNotEmpty(filterSet)) {
                List<Predicate> predicates = Lists.newArrayList();
                List<Predicate> andPredicates = Lists.newArrayList();
                // 构建查询对象
                filterSet.forEach(filter -> {
                    // 实体类字段名称
                    String[] names = StringUtils.split(filter.getFieldName(), SPOT);
                    // 得到构造查询,字段名称
                    Path expression = root.get(names[0]);
                    for (int i = 1; i < names.length; i++) {
                        expression = expression.get(names[i]);
                    }
                    // 处理时间，或者枚举
                    Class clazz = dealDateAndEnum(filter, expression);
                    if (filter.getWhereType().equals(WhereOperator.OR)) {
                        setBuilder(criteriaBuilder, predicates, filter, expression, clazz);
                    } else {
                        setBuilder(criteriaBuilder, andPredicates, filter, expression, clazz);
                    }
                });
                if (CollectionUtils.isNotEmpty(predicates) && CollectionUtils.isNotEmpty(andPredicates)) {
                    Predicate or = criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
                    Predicate and = criteriaBuilder.and(andPredicates.toArray(new Predicate[andPredicates.size()]));
                    return criteriaBuilder.and(or, and);
                } else if (CollectionUtils.isNotEmpty(predicates) && CollectionUtils.isEmpty(andPredicates)) {
                    return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
                } else {
                    return criteriaBuilder.and(andPredicates.toArray(new Predicate[andPredicates.size()]));
                }
            }
            return criteriaBuilder.conjunction();
        };
    }

    /**
     * @param filter     过滤值
     * @param expression 表达式
     * @return java.lang.Class
     * @author: leihfei
     * @description 处理时间
     * @date: 13:36 2018/9/6
     * @email: leihfein@gmail.com
     */
    private static Class dealDateAndEnum(SearchFilter filter, Path expression) {
        // 进行日期，枚举等转换
        Class clazz = expression.getJavaType();
        if (Date.class.isAssignableFrom(clazz) &&
                !filter.getValue().getClass().equals(clazz)) {
            String value = (String) filter.getValue();
            if (!value.contains(COMMA)) {
                filter.setValue(transform2Date(value));
            }
        } else if (LocalDateTime.class.isAssignableFrom(clazz) &&
                !filter.getValue().getClass().equals(clazz)
        ) {
            // 时间很有可能包含between
            String value = (String) filter.getValue();
            if (!value.contains(COMMA)) {
                filter.setValue(transform2LocalDateTime(value));
            }
        } else if (LocalDate.class.isAssignableFrom(clazz) &&
                !filter.getValue().getClass().equals(clazz)
        ) {
            String value = (String) filter.getValue();
            if (!value.contains(COMMA)) {
                filter.setValue(transform2LocalDate(value));
            }
        }
        if (Enum.class.isAssignableFrom(clazz) && !filter.getValue().getClass().equals(clazz)) {
            filter.setValue(transform2Enum(clazz, (String) filter.getValue()));
        }
        return clazz;
    }

    /**
     * @param criteriaBuilder 构建查询语句
     * @param predicates,     查询构建对象数组
     * @param filter          过滤值
     * @param expression      表达式
     * @param clazz           实体类
     * @return void
     * @author: leihfei
     * @description
     * @date: 13:33 2018/9/6
     * @email: leihfein@gmail.com
     */
    private static void setBuilder(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, SearchFilter filter, Path expression, Class clazz) {
        // 设置匹配模式
        Predicate predicate = null;
        switch (filter.getOperator()) {
            case EQ:
                predicate = criteriaBuilder.equal(expression, filter.getValue());
                break;
            case LIKE:
                predicate = criteriaBuilder.like(expression, PERCENTAGE_NUMBER + filter.getValue() + PERCENTAGE_NUMBER);
                break;
            case LLIKE:
                predicate = criteriaBuilder.like(expression, PERCENTAGE_NUMBER + filter.getValue());
                break;
            case RLIKE:
                predicate = criteriaBuilder.like(expression, filter.getValue() + PERCENTAGE_NUMBER);
                break;
            case GT:
                predicate = criteriaBuilder.greaterThan(expression, (Comparable) filter.getValue());
                break;
            case GTE:
                predicate = criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) filter.getValue());
                break;
            case LT:
                predicate = criteriaBuilder.lessThan(expression, (Comparable) filter.getValue());
                break;
            case LTE:
                predicate = criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) filter.getValue());
                break;
            case IN:
                predicate = expression.in((Object[]) filter.getValue().toString().split(COMMA));
                break;
            case ISNULL:
                predicate = criteriaBuilder.isNull(expression);
                break;
            case ISNOTNULL:
                predicate = criteriaBuilder.isNotNull(expression);
                break;
            case NOTEQ:
                predicate = criteriaBuilder.notEqual(expression, filter.getValue());
                break;
            case BETWEEN:
                // 时间between 样式: 2018-09-05 11:22:333, 2018-09-05 11:26:333
                // 字符串between 样式
                if (LocalDateTime.class.isAssignableFrom(clazz)) {
                    predicate = criteriaBuilder.between(expression, getDate(filter.getValue(), 0), getDate(filter.getValue(), 1));
                } else if (LocalDate.class.isAssignableFrom(clazz)) {
                    predicate = criteriaBuilder.between(expression, transform2LocalDate(((String) filter.getValue()).split(COMMA)[0])
                            , transform2LocalDate(((String) filter.getValue()).split(COMMA)[1]));
                } else if (String.class.isAssignableFrom(clazz)) {
                    if (filter.getValue() != null) {
                        predicate = criteriaBuilder.between(expression, ((String) filter.getValue()).split(COMMA)[0]
                                , ((String) filter.getValue()).split(COMMA)[1]);
                    }
                }
                break;
            case NOTIN:
                predicate = criteriaBuilder.not(expression.in((Object[]) filter.getValue().toString().split(COMMA)));
                break;
        }
        if (predicate == null) {
            return;
        }
        predicates.add(predicate);
    }

    /**
     * @param value 日期值
     * @param i     需要截取位数
     * @return java.time.LocalDateTime
     * @author: leihfei
     * @description
     * @date: 14:00 2018/9/6
     * @email: leihfein@gmail.com
     */
    private static LocalDateTime getDate(Object value, int i) {
        String s = value == null ? "" : (String) value;
        String s1 = s.split(COMMA)[i];
        if ("undefined".equals(s1) || "null".equals(s1)) {
            return LocalDateTime.now();
        }
        if (s1.length() == 10) {
            return LocalDateTime.of(LocalDate.parse(s1), LocalTime.MIN);
        }
        return LocalDateTime.parse(s1, DateTimeFormatter.ofPattern(LONG_DATE));
    }


    public static <T> Specification<T> bySearchFilter(final Class<T> entityClazz, final Collection<SearchFilter> searchFilters) {
        return bySearchFilter(entityClazz, searchFilters.toArray(new SearchFilter[]{}));
    }

    public static <T> Specification<T> bySearchFilter(final Class<T> entityClazz, final SearchFilter... searchFilters) {
        final Set<SearchFilter> filterSet = new HashSet<>();
        for (SearchFilter searchFilter : searchFilters) {
            filterSet.add(searchFilter);
        }
        return bySearchFilter(entityClazz, filterSet);
    }


    /**
     * @param value 日期时间字符串
     * @return java.time.LocalDateTime
     * @author: leihfei
     * @description 转为LocalDateTime时间
     * @date: 11:18 2018/9/5
     * @email: leihfein@gmail.com
     */
    private static LocalDateTime transform2LocalDateTime(String value) {
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(LONG_DATE));
    }

    private static LocalDateTime transformStringToLocal(String value) {
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * @param value 日期字符串
     * @return java.util.Date
     * @author: leihfei
     * @description 字符串日期转日期
     * @date: 11:17 2018/9/5
     * @email: leihfein@gmail.com
     */
    private static LocalDate transform2LocalDate(String value) {
        return LocalDate.parse(value);
    }


    /**
     * @param dateString 日期字符串
     * @return java.util.Date
     * @author: leihfei
     * @description 字符串日期转日期
     * @date: 11:17 2018/9/5
     * @email: leihfein@gmail.com
     */
    private static Date transform2Date(String dateString) {
        SimpleDateFormat sFormat = new SimpleDateFormat(SHORT_DATE);
        try {
            return sFormat.parse(dateString);
        } catch (ParseException e) {
            try {
                return sFormat.parse(LONG_DATE);
            } catch (ParseException e1) {
                try {
                    return sFormat.parse(TIME);
                } catch (ParseException e2) {
                    log.error("构建jpa查询日志转换错误!,字符串日期:{}", dateString);
                }
            }
        }
        return null;
    }


    /**
     * @param enumClass  枚举类
     * @param enumString 枚举字符串
     * @return E
     * @author: leihfei
     * @description 将字符串转化为指定枚举类
     * @date: 11:14 2018/9/5
     * @email: leihfein@gmail.com
     */
    private static <E extends Enum<E>> E transform2Enum(Class<E> enumClass, String enumString) {
        return EnumUtils.getEnum(enumClass, enumString);
    }
}
