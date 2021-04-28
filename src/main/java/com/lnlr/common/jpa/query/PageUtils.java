package com.lnlr.common.jpa.query;

import com.google.common.collect.Lists;
import com.lnlr.common.jpa.enums.Operator;
import com.lnlr.common.jpa.model.MatchModel;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.common.jpa.model.SearchFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author:leihfei
 * @description 设置分页工具类
 * @date:Create in 12:48 2018/9/5
 * @email:leihfein@gmail.com
 */
@Slf4j
public class PageUtils {
    /**
     * 降序配置
     */
    public static final String DESC = "desc";

    /**
     * 升序排序
     */
    public static final String ASC = "asc";

    /**
     * 分页方法
     *
     * @param data      全部数据
     * @param startPage 第几页
     * @param pageNum   每页数量
     * @return 指定页的数据
     */
    public static <T> Page<T> page(List<T> data, int startPage, int pageNum) {
        int page = dealFirst(startPage);
        // 构建分页对象
        Pageable pageable = PageRequest.of(page, pageNum);
        // 偏移量大于数据长度
        if (pageable.getOffset() > data.size()) {
            return new PageImpl<>(Lists.newArrayList(), pageable, data.size());
        }

        if (pageable.getOffset() <= data.size() && pageable.getOffset() + pageable.getPageSize() > data.size()) {
            return new PageImpl<>(data.subList((int) pageable.getOffset(), data.size()), pageable, data.size());
        }

        return new PageImpl<>(
                data.subList((int) pageable.getOffset(), (int) pageable.getOffset() + pageable.getPageSize()),
                pageable, data.size());
    }

    /**
     * @param pager 分页对象
     * @return java.util.Collection<com.lnlr.authority.common.jpa.model.SearchFilter>
     * @author: leihfei
     * @description 得到过滤set集合
     * @date: 13:04 2018/9/5
     * @email: leihfein@gmail.com
     */
    public static Collection<SearchFilter> buildSearchFilter(NgPager pager) {
        Set<SearchFilter> searchFilters = new HashSet<>();
        if (pager == null || CollectionUtils.isEmpty(pager.getFilters())) {
            return searchFilters;
        }
        pager.getFilters().forEach((k, v) -> searchFilters.add(new SearchFilter(k, parse(v.getMatchMode()), v.getValue(), v.getWhereType())));
        return searchFilters;
    }

    /**
     * @param array     原始数组
     * @param addLength 需要补充的长度
     * @return java.lang.Object
     * @author leihfei
     * @description 动态数组的扩建
     * @date 15:10:22 2019-05-12
     */
    public static Object dynaicArray(Object array, int addLength) {
        Class c = array.getClass();
        if (!c.isArray()) {
            return null;
        }
        Class componentType = c.getComponentType();
        int length = Array.getLength(array);
        int newLength = length + addLength;
        Object newArray = Array.newInstance(componentType, newLength);
        System.arraycopy(array, 0, newArray, 0, length);
        return newArray;
    }

    /**
     * @param pager 分页对象
     * @return org.springframework.data.domain.Sort
     * @author: leihfei
     * @description 通过分页对象构建一个sort排序对象，将所有的排序规则和字段都封装进去
     * @date: 13:05 2018/9/5
     * @email: leihfein@gmail.com
     */
    public static Sort buildSort(NgPager pager) {
        if (null != pager.getMultiSortMeta() && pager.getMultiSortMeta().length > 0) {
            List<Sort.Order> orders = java.util.Arrays.stream(pager.getMultiSortMeta())
                    .map(k -> new Sort.Order(Sort.Direction.fromString(k.getOrder() == -1 ? DESC : ASC), k.getField()))
                    .collect(Collectors.toList());
            return Sort.by(orders);
        }
        return Sort.by(Sort.Direction.ASC, "id");
    }

    /**
     * @param pager 分页对象
     * @return org.springframework.data.domain.PageRequest
     * @author: leihfei
     * @description 通过分页对象转化为jpa分页排序对象，是对整个数据排序
     * @date: 13:02 2018/9/5
     * @email: leihfein@gmail.com
     */
    public static PageRequest buildPageRequest(NgPager pager) {
        if (null != pager.getMultiSortMeta() && pager.getMultiSortMeta().length > 0) {
            List<Sort.Order> orders = java.util.Arrays.stream(pager.getMultiSortMeta())
                    .map(k -> new Sort.Order(Sort.Direction.fromString(k.getOrder() == -1 ? DESC : ASC), k.getField()))
                    .collect(Collectors.toList());
            return buildPageRequest(dealFirst(pager.getFirst()), pager.getRows(), Sort.by(orders));
        }
        if (pager.getSortField() != null) {
            Sort sort = Sort.by(Sort.Direction.fromString(pager.getSortOrder() == -1 ? DESC : ASC), pager.getSortField());
            return buildPageRequest(dealFirst(pager.getFirst()), pager.getRows(), sort);
        }
        return PageRequest.of(dealFirst(pager.getFirst()), pager.getRows());
    }

    /**
     * @param pager 分页对象
     * @param sort  排序对象
     * @return org.springframework.data.domain.PageRequest
     * @author: leihfei
     * @description 通过分页对象转化为jpa分页对象
     * @date: 13:02 2018/9/5
     * @email: leihfein@gmail.com
     */
    public static PageRequest buildPageRequest(NgPager pager, Sort sort) {
        if (null != pager.getMultiSortMeta() && pager.getMultiSortMeta().length > 0) {
            List<Sort.Order> orders = java.util.Arrays.stream(pager.getMultiSortMeta())
                    .map(k -> new Sort.Order(Sort.Direction.fromString(k.getOrder() == -1 ? DESC : ASC), k.getField()))
                    .collect(Collectors.toList());
            return buildPageRequest(dealFirst(pager.getFirst()), pager.getRows(), Sort.by(orders));
        }
        return PageRequest.of(dealFirst(pager.getFirst()), pager.getRows(), sort);
    }

    private static Integer dealFirst(Integer first) {
        if (first == 0) {
            return first;
        } else {
            first -= 1;
            return first;
        }
    }

    /**
     * @param startRow 开始行
     * @param rows     行数
     * @param sort     排序
     * @return org.springframework.data.domain.PageRequest
     * @author: leihfei
     * @description 设置分页排序对象
     * @date: 13:02 2018/9/5
     * @email: leihfein@gmail.com
     */
    public static PageRequest buildPageRequest(@NotNull Integer startRow, @NotNull Integer rows, Sort sort) {
        return PageRequest.of(startRow, rows, sort);
    }

    /**
     * @param matchMode 过滤匹配模式
     * @return com.lnlr.authority.common.jpa.enums.Operator
     * @author: leihfei
     * @description 匹配模式
     * @date: 13:01 2018/9/5
     * @email: leihfein@gmail.com
     */
    private static Operator parse(String matchMode) {
        if (matchMode == null) {
            return Operator.EQ;
        }
        Operator op;
        switch (matchMode.toLowerCase()) {
            case MatchModel.CONTAINS:
                op = Operator.LIKE;
                break;
            case MatchModel.EQUALS:
                op = Operator.EQ;
                break;
            case MatchModel.IN:
                op = Operator.IN;
                break;
            case MatchModel.STARTS_WITH:
                op = Operator.RLIKE;
                break;
            case MatchModel.ENDS_WITH:
                op = Operator.LLIKE;
                break;
            case MatchModel.ISNULL:
                op = Operator.ISNULL;
                break;
            case MatchModel.LT:
                op = Operator.LT;
                break;
            case MatchModel.GT:
                op = Operator.GT;
                break;
            case MatchModel.GTE:
                op = Operator.GTE;
                break;
            case MatchModel.LTE:
                op = Operator.LTE;
                break;
            case MatchModel.BETWEEN:
                op = Operator.BETWEEN;
                break;
            case MatchModel.NOTEQ:
                op = Operator.NOTEQ;
                break;
            case MatchModel.NOTIN:
                op = Operator.NOTIN;
                break;
            default:
                op = Operator.EQ;
        }
        return op;
    }
}
