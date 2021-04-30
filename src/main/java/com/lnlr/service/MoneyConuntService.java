package com.lnlr.service;

import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.jpa.model.NgData;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.common.response.Response;
import com.lnlr.pojo.param.MoneyParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author leihfei
 * @date 2021-04-30
 */
public interface MoneyConuntService {
    NgData<Object> page(NgPager ngPager);

    Response create(MoneyParam param);

    Response update(MoneyParam param);

    Response delete(IdEntity param);

    /**
     * 导出
     * @param ngPager
     * @param request
     * @param servletResponse
     */
    void exportExcel(NgPager ngPager, HttpServletRequest request, HttpServletResponse servletResponse);
}
