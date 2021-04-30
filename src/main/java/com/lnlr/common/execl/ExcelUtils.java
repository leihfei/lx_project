package com.lnlr.common.execl;

import com.lnlr.common.exception.ParamException;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description Excel操作工具类
 * @date 2019/2/13 19:41
 */
public class ExcelUtils {

    /* excel两种结尾 */
    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

    /**
     * @param file multipartFile文件
     * @return org.apache.poi.ss.usermodel.Workbook poi工作簿
     * @author leihfei
     * @description 获取MultipartFile中的文件路径及文件Input流
     * @date 10:28 2019/2/14
     */
    public static Workbook getWorkbook(MultipartFile file) {
        String ext = "";
        ext = ext.toLowerCase();
        String fpath = file.getOriginalFilename();   //取得文件路径
        if (fpath.lastIndexOf(".") > -1) {
            ext = fpath.substring(fpath.lastIndexOf(".") + 1);//获取后缀名
        }
        try {
            InputStream is = file.getInputStream();  //转为Input流
            return getWorkbook(is, ext);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * excel添加下拉数据校验
     *
     * @param sheet      哪个 sheet 页添加校验
     * @param dataSource 数据源数组
     * @param col        第几列校验（0开始）
     * @return
     */
    public static DataValidation createDataValidation(Sheet sheet, String[] dataSource, int col) {
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(0, 65535, col, col);

        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createExplicitListConstraint(dataSource);

        DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);


        //处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation) {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        } else {
            dataValidation.setSuppressDropDownArrow(false);
        }

        dataValidation.setEmptyCellAllowed(true);
        dataValidation.setShowPromptBox(true);
        dataValidation.createPromptBox("提示", "只能选择下拉框里面的数据");
        return dataValidation;
    }


    /**
     * @param sheetName
     * @return org.apache.poi.ss.usermodel.Workbook
     * @author leihfei
     * @description 创建工作簿
     * @date 10:51 2019/2/18
     */
    public static Workbook createWorkBook(String sheetName) {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        wb.createSheet(sheetName);
        return wb;
    }

    /**
     * 创建表头
     *
     * @param wb    workbook
     * @param sheet sheet
     */
    public static void createHeader(Workbook wb, Sheet sheet, String[] headerArr) {
        //表头
        Row header = sheet.createRow(0);
        header.setHeight((short) (28 * 20));
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setBold(true);
        font.setColor(HSSFFont.COLOR_NORMAL);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        for (int i = 0; i < headerArr.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headerArr[i]);
            cell.setCellStyle(style);
        }
    }

    /**
     * @param is  文件流
     * @param ext 后缀
     * @return org.apache.poi.ss.usermodel.Workbook
     * @author leihfei
     * @description 获取poi工作簿
     * @date 10:28 2019/2/14
     */
    public static Workbook getWorkbook(InputStream is, String ext) {
        Workbook workbook;
        try {
            if (OFFICE_EXCEL_2003_POSTFIX.equals(ext)) {   //xlsx
                workbook = new HSSFWorkbook(is);
            } else if (OFFICE_EXCEL_2010_POSTFIX.equals(ext)) {  //xls
                workbook = new XSSFWorkbook(is);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return workbook;
    }


    /**
     * 下载模板时控制写入的内容并且做验证
     *
     * @param sheet    工作普
     * @param textList 文本
     * @param firstRow 开始行
     * @param endRow   结束行
     * @param firstCol 开始列
     * @param endCol   结束列
     * @return
     */
    public static DataValidation setDataValidation(Sheet sheet, String[] textList, int firstRow, int endRow, int firstCol, int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        //加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
        constraint.setExplicitListValues(textList);
        //设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList((short) firstRow, (short) endRow, (short) firstCol, (short) endCol);
        //数据有效性对象
        DataValidation data_validation = helper.createValidation(constraint, regions);
        return data_validation;
    }


    /**
     * @param sheetName   excel中sheet名称
     * @param list        数据
     * @param fieldNames  导出对象中的字段名称
     * @param columnNames 导出excel中的列名称
     * @return org.apache.poi.ss.usermodel.Workbook
     * @author leihfei
     * @description 自动生成poi excel数据
     * @date 10:25 2019/2/14
     */
    public static Workbook createWorkBook(String sheetName, List list, List<String> fieldNames, List<String> columnNames) throws NoSuchFieldException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (CollectionUtils.isEmpty(fieldNames)) {
            throw new RuntimeException("key不能为空!");
        }
        if (CollectionUtils.isEmpty(columnNames)) {
            throw new RuntimeException("列名不能为空");
        }
        if (fieldNames.size() != columnNames.size()) {
            throw new RuntimeException("字段和列名不对应!");
        }
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(sheetName);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();
        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();
        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBold(true);
        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());
        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(BorderStyle.THICK);
        cs.setBorderRight(BorderStyle.THICK);
        cs.setBorderTop(BorderStyle.THICK);
        cs.setBorderBottom(BorderStyle.THICK);
        // 设置水平左对齐
        cs.setAlignment(HorizontalAlignment.LEFT);
        // 设置上下居中
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(BorderStyle.THIN);
        cs2.setBorderRight(BorderStyle.THIN);
        cs2.setBorderTop(BorderStyle.THIN);
        cs2.setBorderBottom(BorderStyle.THIN);
        cs2.setAlignment(HorizontalAlignment.LEFT);
        // 手动补充序号
        fieldNames.add(0, "index");
        columnNames.add(0, "序号");
        // 创建第一行
        Row row = sheet.createRow((short) 0);
        //设置列名
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cs);
            cell.setCellValue(columnNames.get(i));
            // 设置动态列宽
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 2);
        }
        //设置每行每列的值
        for (short i = 0; i < list.size(); i++) {
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) i + 1);
            // 在row行上创建多个格子，写入数据
            for (short j = 0; j < fieldNames.size(); j++) {
                Cell cell = row1.createCell(j);
                if (j == 0) {
                    cell.setCellValue(i + 1);
                } else {
                    Field declaredField = list.get(i).getClass().getDeclaredField(fieldNames.get(j));
                    declaredField.setAccessible(true);
                    Object o = declaredField.get(list.get(i));
                    if (o == null) {
                        o = "";
                    }
                    if (declaredField.getGenericType().equals(LocalDateTime.class)) {
                        cell.setCellValue(dealLocalDateTime(o.toString()));
                    } else if (declaredField.getGenericType().equals(LocalDate.class)) {
                        cell.setCellValue(dealLocalDate(o.toString()));
                    } else {
                        cell.setCellValue(o.toString());
                    }
                }
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }


    /**
     * @param localDate 日期
     * @return java.lang.String
     * @author leihfei
     * @description 将字符串转为指定日期
     * @date 10:33 2019/2/14
     */
    private static String dealLocalDate(String localDate) {
        if (StringUtils.isBlank(localDate)) {
            return "";
        }
        LocalDateTime time = LocalDateTime.parse(localDate);
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dtf2.format(time);
    }

    /**
     * 判断客户端浏览器类型
     *
     * @param request
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        String UserAgent = request.getHeader("User-Agent").toLowerCase();
        if (UserAgent.indexOf("firefox") >= 0) {
            return "FF";
        } else if (UserAgent.indexOf("safari") >= 0) {
            return "Chrome";
        } else {
            return "IE";
        }
    }

    /**
     * 对下载文件名进行编码
     *
     * @param request
     * @param fileName
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getDownFileNameByCode(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        String newFileName = new String();
        String browser = getBrowser(request);
        if (browser.equalsIgnoreCase("Chrome")) {
            newFileName = URLEncoder.encode(fileName, "UTF8");
        } else if (browser.equalsIgnoreCase("Mozilla")) {
            newFileName = new String(fileName.getBytes(), "ISO8859-1");
        } else {
            newFileName = URLEncoder.encode(fileName, "UTF8");
        }
        return newFileName;
    }

    /**
     * @param sheetName   excel中sheet名称
     * @param titleName   表格头名称
     * @param rowDatas    数据
     * @param columnNames 导出excel中的列名称
     * @return org.apache.poi.ss.usermodel.Workbook
     * @author leihfei
     * @description 自动生成poi excel数据
     * @date 10:25 2019/2/14
     */
    public static Workbook createWorkBook(String sheetName, String titleName, List<List<String>> rowDatas, List<String> columnNames) throws NoSuchFieldException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        // 创建excel工作簿
        Workbook wb = new XSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(sheetName);
        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();
        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();
        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBold(true);
        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());
        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderBottom(BorderStyle.THIN);
        // 设置水平左对齐
        cs.setAlignment(HorizontalAlignment.LEFT);
        // 设置上下居中
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(BorderStyle.THIN);
        cs2.setBorderRight(BorderStyle.THIN);
        cs2.setBorderTop(BorderStyle.THIN);
        cs2.setBorderBottom(BorderStyle.THIN);
        cs2.setAlignment(HorizontalAlignment.LEFT);

        CellStyle indexStyle = wb.createCellStyle();
        // 设置水平左对齐
        indexStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置上下居中
        indexStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        int indexRow = 0;
        if (titleName != null && StringUtils.isNotEmpty(titleName)) {
            CellStyle titleStyle = wb.createCellStyle();
            // 设置水平左对齐
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            // 设置上下居中
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setFont(f);
            Row row = sheet.createRow((short) indexRow++);
            Cell cell = row.createCell(0);
            cell.setCellValue(titleName);
            cell.setCellStyle(titleStyle);
            ExcelUtils.mergeCell(sheet, 0, 0, 0, columnNames.size() - 1);
        }
        // 创建第一行
        Row row = sheet.createRow((short) indexRow++);
        //设置列名
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cs);
            cell.setCellValue(columnNames.get(i));
            // 设置动态列宽
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 20 / 10);
        }
        //设置每行每列的值
        if (rowDatas != null) {
            for (short i = 0; i < rowDatas.size(); i++) {
                // 创建一行，在页sheet上
                int currentRow = i + indexRow;
                Row row1 = sheet.createRow(currentRow);
                List<String> rowData = rowDatas.get(i);
                // 在row行上创建多个格子，写入数据
                for (short j = 0; j < rowData.size(); j++) {
                    Cell cell = row1.createCell(j);
                    String data = rowData.get(j);
                    if (data == null) {
                        data = "";
                    }
                    cell.setCellValue(data);
                    if (j == 0) {
                        cell.setCellStyle(indexStyle);
                    } else {
                        cell.setCellStyle(cs2);
                    }
                }
            }
        }
        return wb;
    }


    /**
     * @param sheetName   excel中sheet名称
     * @param rowDatas    数据
     * @param columnNames 导出excel中的列名称
     * @return org.apache.poi.ss.usermodel.Workbook
     * @author leihfei
     * @description 自动生成poi excel数据
     * @date 10:25 2019/2/14
     */
    public static Workbook createWorkBook(String sheetName, List<List<String>> rowDatas, List<String> columnNames) throws NoSuchFieldException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        // 创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(sheetName);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();
        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();
        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBold(true);
        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());
        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(BorderStyle.THICK);
        cs.setBorderRight(BorderStyle.THICK);
        cs.setBorderTop(BorderStyle.THICK);
        cs.setBorderBottom(BorderStyle.THICK);
        // 设置水平左对齐
        cs.setAlignment(HorizontalAlignment.LEFT);
        // 设置上下居中
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(BorderStyle.THIN);
        cs2.setBorderRight(BorderStyle.THIN);
        cs2.setBorderTop(BorderStyle.THIN);
        cs2.setBorderBottom(BorderStyle.THIN);
        cs2.setAlignment(HorizontalAlignment.LEFT);

        CellStyle indexStyle = wb.createCellStyle();
        // 设置水平左对齐
        indexStyle.setAlignment(HorizontalAlignment.CENTER);
        // 设置上下居中
        indexStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 创建第一行
        Row row = sheet.createRow((short) 0);
        //设置列名
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cs);
            cell.setCellValue(columnNames.get(i));
            // 设置动态列宽
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 2);
        }
        //设置每行每列的值
        for (short i = 0; i < rowDatas.size(); i++) {
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) i + 1);
            List<String> rowData = rowDatas.get(i);
            // 在row行上创建多个格子，写入数据
            for (short j = 0; j < rowData.size(); j++) {
                Cell cell = row1.createCell(j);
                String data = rowData.get(j);
                if (data == null) {
                    data = "";
                }
                cell.setCellValue(data);
                if (j == 0) {
                    cell.setCellStyle(indexStyle);
                } else {
                    cell.setCellStyle(cs2);
                }
            }
        }
        return wb;
    }


    /**
     * @param localDateTime 日期时分秒
     * @return java.lang.String
     * @author leihfei
     * @description 将字符串转为指定日期时间格式
     * @date 10:32 2019/2/14
     */
    private static String dealLocalDateTime(String localDateTime) {
        if (StringUtils.isBlank(localDateTime)) {
            return "";
        }
        LocalDateTime time = LocalDateTime.parse(localDateTime);
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dtf2.format(time);
    }

    /**
     * @param sheet        sheet工作簿
     * @param firstRow     开始行
     * @param lastRow      结束行
     * @param firstCellNum 开始列
     * @param lastCellNum  结束列
     * @return void
     * @author leihfei
     * @description 单元格合并
     * @date 15:45 2019/2/21
     */
    public static void mergeCell(Sheet sheet, Integer firstRow, Integer lastRow, Integer firstCellNum, Integer lastCellNum) {
        CellRangeAddress er2 = new CellRangeAddress(firstRow, lastRow, firstCellNum, lastCellNum);
        sheet.addMergedRegion(er2);
    }


    /**
     * 功能说明：下载Excel文件
     *
     * @param location
     * @param fileName
     * @param fileRealName
     * @param response
     * @return
     */
    public static void downloadExcel(String location, String fileName, String fileRealName
            , HttpServletRequest request
            , HttpServletResponse response) {
        File file = new File(location, fileName);

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            response.setContentType("multipart/form-data");

            String userAgent = request.getHeader("user-agent");
            if (userAgent != null && userAgent.contains("Firefox") || userAgent.contains("Chrome")
                    || userAgent.contains("Safari")) {
                fileRealName = new String((fileRealName).getBytes(), "ISO8859-1");
            } else {
                fileRealName = URLEncoder.encode(fileRealName, "UTF8"); //其他浏览器
            }
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileRealName);
            outputStream = response.getOutputStream();
            //循环写入输出流
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, length);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ParamException("Excel导出异常！");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 读 一个工作表的数据
     *
     * @param sheet
     */
    private static SheetData readSheet(Sheet sheet) {
        //读第一行作为表头
        Row headRow = sheet.getRow(0);
        List<String> tableHeads = new ArrayList<>();
        // 设置表头
        int columnNum = 0;
        while (true) {
            Cell cell = headRow.getCell(columnNum);
            if (cell == null) {
                break;
            }
            String value = cell.getStringCellValue();
            tableHeads.add(value.trim());
            columnNum++;
        }
        // 读第一行之后的作为数据
        List<List<String>> datas = new ArrayList<>();
        int columnSize = tableHeads.size();
        int rowNum = 1;
        while (true) {
            Row dataRow = sheet.getRow(rowNum);
            if (dataRow == null) {
                break;
            }
            int columnIndex = 0;
            boolean isBlankRow = true;
            List<String> rowData = new ArrayList<>();
            while (columnIndex < columnSize) {
                Cell cell = dataRow.getCell(columnIndex);
                String value = getCellValue(cell);
                if (StringUtils.isNotBlank(value)) {
                    isBlankRow = false;
                }
                rowData.add(value.trim());
                columnIndex++;
            }
            //空行，则跳过
            if (!isBlankRow) {
                datas.add(rowData);
            }
            rowNum++;
        }
        return new SheetData(tableHeads, datas);
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        if (cell == null)
            return "";
        CellType cellType = cell.getCellTypeEnum();
        String cellValue = "";
        switch (cellType) {
            case STRING:
                cellValue = cell.getStringCellValue().trim();
                break;

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    //判断日期类型
                    cellValue = new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                } else {
                    cellValue = new DecimalFormat("#.######").format(cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
        }
        return cellValue;
    }


    /**
     * Excel-sheet数据传输对象
     */
    @Data
    public static class SheetData {
        /**
         * 表头
         */
        private List<String> columnNames;

        /**
         * 表格内容
         */
        List<List<String>> datas;

        public SheetData(List<String> columnNames, List<List<String>> datas) {
            this.columnNames = columnNames;
            this.datas = datas;
        }
    }

}
