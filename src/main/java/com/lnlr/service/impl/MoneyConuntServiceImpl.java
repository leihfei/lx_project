package com.lnlr.service.impl;

import com.google.common.collect.Lists;
import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.exception.WarnException;
import com.lnlr.common.execl.ExcelUtils;
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
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (coum == null) {
            return new FailedResponse<>("数据为空，无法查询!");
        }
        if(coum.getStatus() == 1){
            return new FailedResponse<>("已经设置，无需重复设置!");
        }
        coum.setStatus(1);
        coum.setInsertName(RequestHolder.currentUser().getRealName());
        coum.setOperator(RequestHolder.currentUser().getRealName());
        coum.setOperatorIp(IpUtils.getRemoteIp(RequestHolder.currentRequest()));
        countDAO.save(coum);
        return new SuccessResponse("设置成功！");
    }

    /**
     * 获取表头
     *
     * @return 表头
     */
    private String[] getHeader() {
        return new String[]{"序号", "微信收益", "现金收益", "刷卡收益", "总收益", "收益时间", "记录人", "记录时间"};
    }

    /**
     * 表头对应字段名
     *
     * @return 表头字段名
     */
    private String[] getHeaderField() {
        return new String[]{
                "wxCount", "cashCount", "vashCount", "allCount", "insertDate", "insertName", "insertTime"
        };
    }


    @Override
    public void exportExcel(NgPager ngPager, HttpServletRequest request, HttpServletResponse response) {
        // 1.查询试题数据
        NgData page = page(ngPager);
        List<MoneyVO> data = page.getData();
        // 进行数据导出
        Workbook workbook = ExcelUtils.createWorkBook("每日流水统计");
        Sheet sheet = workbook.getSheetAt(0);
        ExcelUtils.createHeader(workbook, sheet, getHeader());
        int firstRowNum = 1;
        String[] headerField = this.getHeaderField();
        try {
            if (CollectionUtils.isNotEmpty(data)) {
                Map<Integer, Integer> maxColumnWidth = new HashMap<>(headerField.length);
                for (int i = 0; i < data.size(); i++) {
                    MoneyVO money = data.get(i);
                    Row row = sheet.createRow(i + firstRowNum);
                    // 设置第一个为索引的值
                    Cell cellIndex = row.createCell(0);
                    cellIndex.setCellValue(i + 1);
                    for (int j = 0; j < headerField.length; j++) {
                        Field field = money.getClass().getDeclaredField(headerField[j]);
                        field.setAccessible(true);
                        Object cellData = field.get(money);
                        if (cellData != null) {
                            //自适应行宽
                            int columnWidth = maxColumnWidth.getOrDefault(j, 4);
                            int characters = Math.min(cellData.toString().length(), 255 / 2);
                            if (characters > columnWidth) {
                                maxColumnWidth.put(j + 1, characters);
                                sheet.setColumnWidth(j + 1, characters * 256 * 2);
                            }
                            Cell cell = row.createCell(j + 1);
                            if (cellData instanceof Integer) {
                                cell.setCellValue((int) cellData);
                            } else if (cellData instanceof String) {
                                cell.setCellValue((String) cellData);
                            } else if (cellData instanceof LocalDateTime) {
                                cell.setCellValue(cellData.toString().replace("T", " "));
                            } else {
                                cell.setCellValue(cellData.toString());
                            }
                        }
                    }
                }
            }
            response.addHeader("Access-Control-Expose-Headers", "*");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode("每日流水", "UTF-8"));
            workbook.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            throw new WarnException("导出失败！", e.getCause());
        }
    }
}
