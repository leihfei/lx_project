package com.lnlr.service;

import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.jpa.model.NgData;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.common.response.Response;
import com.lnlr.pojo.param.MoneyParam;

/**
 * @author leihfei
 * @date 2021-04-30
 */
public interface MoneyConuntService {
    NgData<Object> page(NgPager ngPager);

    Response create(MoneyParam param);

    Response update(MoneyParam param);

    Response delete(IdEntity param);
}
