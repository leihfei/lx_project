package com.lnlr.common.service.base;

import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.common.response.Response;
import com.lnlr.pojo.base.BaseParam;
import com.lnlr.pojo.base.BaseVO;

import java.util.Set;

/**
*@author  leihfei
*@date  2021-04-21
*/
public interface BaseService<T extends BaseParam, U extends T, V extends BaseVO> {

    void create(T param);

    void update(U param);

    void delete(IdEntity idEntity);

    void deleteAll(Set<String> ids);

    V view(IdEntity idEntity);

    Response page(NgPager ngPager);
}
