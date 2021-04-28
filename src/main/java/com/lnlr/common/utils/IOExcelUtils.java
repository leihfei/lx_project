package com.lnlr.common.utils;

import com.lnlr.common.exception.ParamException;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description Excel操作工具类
 * @date 2019/2/13 19:41
 */
public class IOExcelUtils {

    /* excel两种结尾 */
    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

    /**
     * @param multipartFile 资源文件
     * @return org.apache.poi.ss.usermodel.Workbook poi工作簿
     * @author leihfei
     * @description 获取MultipartFile中的文件路径及文件Input流
     * @date 10:28 2019/2/14
     */
    public static Workbook getWorkbook(MultipartFile multipartFile) {
        String ext = "";
        ext = ext.toLowerCase();
        String fpath = multipartFile.getOriginalFilename();   //取得文件路径
        if (fpath.lastIndexOf(".") > -1) {
            ext = fpath.substring(fpath.lastIndexOf(".") + 1);//获取后缀名
        }
        try {
            InputStream is = multipartFile.getInputStream();
            return getWorkbook(is, ext);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
                cell.setCellStyle(cs2);
            }
        }
        return wb;
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


    /********************************************************************/

    /**
     * 导出Excel
     *
     * @param location    文件路径
     * @param datas       要导出的数据
     * @param columnNames 表头
     * @return 临时文件uuid
     */
    public static void generateTempExcel(String location, String fileUUID, List<List<String>> datas, List<String> columnNames) {
        String tempExcelFileName = fileUUID + "." + OFFICE_EXCEL_2003_POSTFIX;
        try {
            Workbook wb = createWorkBook("sheet1", datas, columnNames);
            wb.write(new FileOutputStream(new File(location, tempExcelFileName)));
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParamException("Excel导出异常！");
        }
    }

    /**
     * 获取32 位uuid
     *
     * @return
     */
    public static String get32UUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
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
     * 功能说明：删除临时Excel文件
     *
     * @param location 文件地址
     * @param fileName 文件名
     * @return
     */
    public static void deleteTmpExcel(String location, String fileName) {
        File file = new File(location, fileName);
        if (file.exists()) {
            file.delete();
        }
    }


    /**
     * 校验导入的Excel数据
     *
     * @param multipartFile - 导入的文件
     * @param columnNames   - 模板表头
     * @return 返回表格数据
     */
    public static SheetData validExcel(MultipartFile multipartFile, List<String> columnNames) {
        // 读取Excel数据
        List<SheetData> sheetDatas = readExcel(multipartFile);
        SheetData sheetData = null;
        if (CollectionUtils.isNotEmpty(sheetDatas)) {
            sheetData = sheetDatas.get(0);
        }
        if (sheetData == null) {
            throw new ParamException("Excel读取错误！");
        }
        List<String> expNames = sheetData.getColumnNames();
        if (columnNames.size() != expNames.size()) {
            throw new ParamException("请按模板填写数据!");
        }
        //校验表头
        for (int i = 0; i < expNames.size(); i++) {
            if (!expNames.get(i).equals(columnNames.get(i))) {
                throw new ParamException("请按模板填写数据!");
            }
        }
        return sheetData;
    }

    /**
     * 读取Excel数据
     *
     * @param multipartFile
     * @return
     */
    public static List<SheetData> readExcel(MultipartFile multipartFile) {
        //读取工作簿
        Workbook workBook = getWorkbook(multipartFile);
        if (workBook == null) {
            throw new ParamException("Excel解析错误！");
        }
        List<SheetData> excelDatas = new ArrayList<>();
        //读所有的工作表
        int len = workBook.getNumberOfSheets();
        for (int i = 0; i < len; i++) {
            //读取工作表
            Sheet sheet = workBook.getSheetAt(i);
            SheetData sheetData = readSheet(sheet);
            if (sheetData != null) {
                excelDatas.add(sheetData);
            }

        }
        //最后关闭工作簿
        try {
            workBook.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParamException("Excel解析错误！");
        }
        return excelDatas;
    }

    /**
     * 读 一个工作表的数据
     *
     * @param sheet
     */
    private static SheetData readSheet(Sheet sheet) {
        //读第一行作为表头
        Row headRow = sheet.getRow(0);
        if (headRow == null) {
            return null;
        }
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
