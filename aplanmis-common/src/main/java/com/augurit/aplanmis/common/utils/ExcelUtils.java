package com.augurit.aplanmis.common.utils;

import com.augurit.agcloud.framework.util.StringUtils;
import lombok.Data;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.apache.poi.ss.usermodel.Font.DEFAULT_CHARSET;

/**
 * @author tiantian
 * @date 2019/9/6
 */
public class ExcelUtils {

    private static int HEAD_ROW_POSITION = 0;
    private static int TITLE_ROW_POSITION = 1;
    private static int CONTENT_ROW_POSITON = 2;

    public static void exportExcel(ExcelParam excelParam, HttpServletRequest req, HttpServletResponse response) throws Exception{

        if(excelParam==null){
            throw new RuntimeException("excel请求数据为空！");
        }

        if(excelParam.getDataList()==null || excelParam.getDataList().size()==0){
            throw new RuntimeException("excel导出数据为空！");
        }

        List<String> rowTitle = excelParam.getRowTitleList();

        if(rowTitle==null || rowTitle.size()==0){
            throw new RuntimeException("没有表头数据！");
        }

        String fileName = excelParam.getFileName();

        if(StringUtils.isBlank(fileName)) {
            fileName = UUID.randomUUID().toString() + ".xls";
        }

        String sheetName = excelParam.getSheetName();

        if(StringUtils.isBlank(sheetName)) {
            sheetName = "查询结果";
        }

        HSSFWorkbook workbook = new HSSFWorkbook();//初始化表单
        HSSFSheet sheet = initSheet(workbook,sheetName);

        if(excelParam.getColumnWidthList()!=null && excelParam.getColumnWidthList().size()>0){
            sheet.setColumnWidth(0,1500);//序号项宽
            for(int i=0;i<excelParam.getColumnWidthList().size();i++){
                sheet.setColumnWidth(i+1,excelParam.getColumnWidthList().get(i));
            }
        }

        String titleName = excelParam.getTitleName();

        if(StringUtils.isBlank(titleName)) {
            titleName = "查询结果";
        }

        //创建标题
        createTitle(workbook,sheet,titleName,excelParam.getFieldList().size()+1);

        //创建表头
        createRowTitle(workbook,sheet,rowTitle);

        //创建内容
        createContent(workbook,sheet,excelParam.getFieldList(),excelParam.getDataList());

        response.reset();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
    }


    private static void createContent(HSSFWorkbook workbook,HSSFSheet sheet,List<String> fieldList,List dataList) throws Exception{
        HSSFRow row;
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setFontName("等线");
        font.setFontHeightInPoints((short) 11);//设置字体大小
        cellStyle.setFont(font);
        setVertical(cellStyle);
        cellStyle.setWrapText(true);//自动换行
        setBorder(cellStyle);
        for(int i=0;i<dataList.size();i++){
            Object object = dataList.get(i);

            row = sheet.createRow(CONTENT_ROW_POSITON + i);
            HSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(i+1);
            cell0.setCellStyle(cellStyle);

            Class objCalss = object.getClass();

            for(int j=0;j<fieldList.size();j++){
                HSSFCell cell = row.createCell(j+1);
                String fieldName = fieldList.get(j);
                String cKey = fieldName;
                cKey = cKey.substring(0, 1).toUpperCase() + cKey.substring(1);
                Method fMethod;
                Object fValue = null;
                try {
                    fMethod = objCalss.getMethod("get" + cKey);// public方法
                    fValue = fMethod.invoke(object);// 取getfKey的值
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(fValue!=null && fValue instanceof Date){
                    Date date = (Date)fValue;
                    fValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                }

                String value = fValue==null?null:fValue.toString();
                cell.setCellValue(value);
                cell.setCellStyle(cellStyle);

            }


        }
    }

    private static HSSFSheet initSheet(HSSFWorkbook workbook,String sheetName){
        return workbook.createSheet(sheetName);
    }

    //创建表头
    private static void createTitle(HSSFWorkbook workbook,HSSFSheet sheet,String titleName,int dateSize){
        HSSFRow row = sheet.createRow(HEAD_ROW_POSITION);
        HSSFCell cell = row.createCell(HEAD_ROW_POSITION);
        cell.setCellValue(titleName);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setCharSet(DEFAULT_CHARSET);

        font.setFontName("宋体");

        font.setFontHeight((short)18);

        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗

        font.setFontHeightInPoints((short) 18);//设置字体大小

        cellStyle.setFont(font);
        //居中
        setVertical(cellStyle);
        setBorder(cellStyle);
        cell.setCellStyle(cellStyle);

        for(int i=1;i<dateSize;i++){
            cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,dateSize-1));
    }


    //设置边框
    private static void setBorder(HSSFCellStyle cellStyle){
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
    }

    //垂直居中
    private static void setVertical(HSSFCellStyle cellStyle){
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
    }

    //创建行标题
    private static void createRowTitle(HSSFWorkbook workbook,HSSFSheet sheet,List<String> titleList){
        HSSFRow row = sheet.createRow(TITLE_ROW_POSITION);
        HSSFCell cell;
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
        font.setFontName("等线");
        font.setFontHeightInPoints((short)14);
        font.setFontHeight((short)14);
        setBackYellowColor(cellStyle);//背景色
        setVertical(cellStyle);//居中
        //自动换行 耗内存
        cellStyle.setWrapText(true);

        setBorder(cellStyle);//边框

        cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(cellStyle);

        for(int i=0;i<titleList.size();i++){
            cell = row.createCell(i+1);
            cell.setCellValue(titleList.get(i));
            cell.setCellStyle(cellStyle);
        }
        HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
        //背景色
        hssfCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        hssfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //居中
        setVertical(hssfCellStyle);
        //自动换行
        hssfCellStyle.setWrapText(true);
    }

    //设置黄色背景色
    private static void setBackYellowColor(HSSFCellStyle cellStyle){
        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    }

    @Data
    public static class ExcelParam{
        private String FileName;
        private List<? extends Object> dataList;
        private String sheetName;
        private String titleName;
        private List<Integer> columnWidthList;
        private List<String> rowTitleList;
        private List<String> fieldList;
    }



}
