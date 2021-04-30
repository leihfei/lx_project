package com.lnlr.service.impl;

import com.google.common.collect.Lists;
import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.jpa.model.NgData;
import com.lnlr.common.jpa.model.NgPager;
import com.lnlr.common.jpa.model.SearchFilter;
import com.lnlr.common.jpa.query.DynamicSpecifications;
import com.lnlr.common.jpa.query.PageUtils;
import com.lnlr.common.response.FailedResponse;
import com.lnlr.common.response.Response;
import com.lnlr.common.response.SuccessResponse;
import com.lnlr.common.utils.BeanValidator;
import com.lnlr.common.utils.CopyUtils;
import com.lnlr.common.utils.IpUtils;
import com.lnlr.common.utils.RequestHolder;
import com.lnlr.pojo.dao.MoneyCountDAO;
import com.lnlr.pojo.entity.MoneyCount;
import com.lnlr.pojo.param.MoneyParam;
import com.lnlr.pojo.vo.MoneyVO;
import com.lnlr.service.MoneyConuntService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author leihfei
 * @date 2021-04-30
 */
@Service
@Slf4j
public class MoneyConuntServiceImpl implements MoneyConuntService {

    @Autowired
    private MoneyCountDAO countDAO;

    @Override
    public NgData page(NgPager ngPager) {
        // 1.查询试题数据
        Collection<SearchFilter> searchFilters = PageUtils.buildSearchFilter(ngPager);
        NgData<MoneyCount> ngData = new NgData<>(
                countDAO.findAll(DynamicSpecifications.bySearchFilter(MoneyCount.class,
                        searchFilters), PageUtils.buildPageRequest(ngPager, PageUtils.buildSort(ngPager))), ngPager);

        NgData<MoneyVO> retuNg = CopyUtils.beanCopy(ngData, new NgData<>());
        retuNg.setData(Lists.newArrayList());
        ngData.getData().forEach(item -> {
            MoneyVO vo = CopyUtils.beanCopy(item, new MoneyVO());
            retuNg.getData().add(vo);
        });
        return retuNg;
    }

    @Override
    public Response create(MoneyParam param) {
        if (param.getCashCount() == null && param.getWxCount() == null && param.getVashCount() == null) {
            return new FailedResponse<>("记账数额不能全部为空！");
        }
        BeanValidator.check(param);
        MoneyCount count = CopyUtils.beanCopy(param, new MoneyCount());
        count.setAllCount(param.getCashCount().add(param.getVashCount()).add(param.getWxCount()));
//        count.setInsertDate(LocalDate.now());
        count.setStatus(0);
        count.setInsertTime(LocalDateTime.now());
        count.setInsertName(RequestHolder.currentUser().getRealName());
        count.setOperator(RequestHolder.currentUser().getRealName());
        count.setOperatorIp(IpUtils.getRemoteIp(RequestHolder.currentRequest()));
        countDAO.save(count);
        return new SuccessResponse("记账成功！");
    }

    @Override
    public Response update(MoneyParam param) {
        return null;
    }

    @Override
    public Response delete(IdEntity param) {
        BeanValidator.check(param);
        MoneyCount coum = countDAO.findById(param.getId()).orElse(null);
        if(coum == null){
            return new FailedResponse<>("数据为空，无法查询!");
        }
        coum.setStatus(1);
        coum.setInsertName(RequestHolder.currentUser().getRealName());
        coum.setOperator(RequestHolder.currentUser().getRealName());
        coum.setOperatorIp(IpUtils.getRemoteIp(RequestHolder.currentRequest()));
        countDAO.save(coum);
        return new SuccessResponse("设置成功！");
    }
}
