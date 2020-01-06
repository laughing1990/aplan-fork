package com.augurit.aplanmis.common.template;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImportAgentProjTemplate extends ExcelTemplate<AeaProjInfo> {

    public ImportAgentProjTemplate(String path) throws Exception {
        super(path);
    }

    public ImportAgentProjTemplate(String path, String type) throws Exception {
        super(path, type);
    }

    public ImportAgentProjTemplate(InputStream is, String type) throws Exception {
        super(is, type);
    }

    public ImportAgentProjTemplate(InputStream is, String type, boolean write) throws Exception {
        super(is, type, write);
    }

    @Override
    public ResultForm checkFormat() throws Exception {
        checkReader();
        ResultForm resultForm = new ResultForm(true);
        Sheet sheet = readbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        if (row.getCell(0) == null || !"项目代码".equals(row.getCell(0).getStringCellValue().trim())) {
            resultForm.setSuccess(false);
        }
        if (row.getCell(0) == null || !"项目/工程名称".equals(row.getCell(1).getStringCellValue().trim())) {
            resultForm.setSuccess(false);
        }
        if (row.getCell(0) == null || !"代办中心".equals(row.getCell(2).getStringCellValue().trim())) {
            resultForm.setSuccess(false);
        }
        return resultForm;
    }

    @Override
    public List<AeaProjInfo> readData() throws Exception {
        checkReader();
        Sheet sheet = readbook.getSheetAt(0);
        Row row = null;
        List<AeaProjInfo> data = new ArrayList<>();
        int startRow = 1;
        int lastRowNum = sheet.getLastRowNum();

        for (int i = startRow; i <= lastRowNum; i++) {
            row = sheet.getRow(i);
            data.add(rowToObj(row));
        }
        return data;
    }

    public List<AeaProjInfo> readPageData(Integer page, Integer pageSize) throws Exception {
        checkReader();
        Sheet sheet = readbook.getSheetAt(0);
        Row row = null;
        List<AeaProjInfo> data = new ArrayList<>();
        int startRow = (page - 1) * pageSize + 1;
        int lastRowNum = startRow + pageSize - 1;
        if (lastRowNum > sheet.getLastRowNum()) {
            lastRowNum = sheet.getLastRowNum();
        }
        for (int i = startRow; i <= lastRowNum; i++) {
            row = sheet.getRow(i);
            data.add(rowToObj(row));
        }
        return data;
    }

    @Override
    public AeaProjInfo readOneRowData(Integer rowIndex) throws Exception {
        checkReader();
        Sheet sheet = readbook.getSheetAt(0);
        Row row = sheet.getRow(rowIndex);
        return rowToObj(row);
    }

    @Override
    public Integer getLastRowNum() throws Exception {
        checkReader();
        Sheet sheet = readbook.getSheetAt(0);
        return sheet.getLastRowNum();
    }

    @Override
    public void writeData(List<AeaProjInfo> data) {
        Row row = null;
        Cell cell = null;
        Sheet sheet = writebook.createSheet("导入失败的代办项目信息");
        CellStyle titleCell = writebook.createCellStyle();
        Font font = writebook.createFont();
        //加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //水平居中
        titleCell.setAlignment(CellStyle.ALIGN_CENTER);
        //垂直居中
        titleCell.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        titleCell.setFont(font);
        sheet.setDefaultRowHeight((short) (30 * 20));
        sheet.setColumnWidth(0,30 * 256);
        sheet.setColumnWidth(1,60 * 256);
        sheet.setColumnWidth(2,60 * 256);
        sheet.setColumnWidth(3,60 * 256);
        row = sheet.createRow(0);
        row.setHeight((short) (30 * 20));
        cell = row.createCell(0);
        cell.setCellStyle(titleCell);
        cell.setCellValue("项目代码");
        cell = row.createCell(1);
        cell.setCellStyle(titleCell);
        cell.setCellValue("项目/工程名称");
        cell = row.createCell(2);
        cell.setCellStyle(titleCell);
        cell.setCellValue("代办中心");
        cell = row.createCell(3);
        cell.setCellStyle(titleCell);
        cell.setCellValue("失败信息");

        int rowIndex = 1;
        int cellIndex = 0;
        CellStyle bodyCell = writebook.createCellStyle();
        bodyCell.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        for(AeaProjInfo proj:data){
            row = sheet.createRow(rowIndex++);
            row.setHeight((short) (30 * 20));
            cell = row.createCell(cellIndex++);
            cell.setCellStyle(bodyCell);
            cell.setCellValue(proj.getLocalCode());
            cell = row.createCell(cellIndex++);
            cell.setCellStyle(bodyCell);
            cell.setCellValue(proj.getProjName());
            cell = row.createCell(cellIndex++);
            cell.setCellStyle(bodyCell);
            cell.setCellValue(proj.getAgentName());
            cell = row.createCell(cellIndex++);
            cell.setCellStyle(bodyCell);
            cell.setCellValue(proj.getFailMsg());
            cellIndex = 0;
        }
    }

    private AeaProjInfo rowToObj(Row row){
        AeaProjInfo proj = null;
        if(row != null){
            proj = new AeaProjInfo();
            proj.setLocalCode(getValue(row.getCell(0)));
            proj.setProjName(getValue(row.getCell(1)));
            proj.setAgentName(getValue(row.getCell(2)));
        }
        return proj;
    }
}
