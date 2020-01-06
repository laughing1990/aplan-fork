package com.augurit.aplanmis.common.template;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 导入模板
 *
 * @param <T>
 */
public abstract class ExcelTemplate<T> {

    protected Workbook readbook;
    protected Workbook writebook;

    protected InputStream is;

    protected ExcelTemplate(String path) throws Exception {
        this(path, path.substring(path.lastIndexOf("."), path.length()));
    }

    protected ExcelTemplate(String path, String type) throws Exception {
        this(new FileInputStream(path), type);
    }

    @SuppressWarnings("resource")
    protected ExcelTemplate(InputStream is, String type) throws Exception {
        this.is = is;
        if ("xls".equals(type)) {
            readbook = new HSSFWorkbook(is, true);
        } else if ("xlsx".equals(type)) {
            readbook = new XSSFWorkbook(is);
        } else {
            throw new Exception("不支持的文件类型");
        }
    }

    @SuppressWarnings("resource")
    protected ExcelTemplate(InputStream is, String type, boolean write) throws Exception {
        if (write) {
            if ("xls".equals(type)) {
                writebook = new HSSFWorkbook();
            } else if ("xlsx".equals(type)) {
                writebook = new XSSFWorkbook();
            } else {
                throw new Exception("不支持的文件类型");
            }
        } else {
            if ("xls".equals(type)) {
                readbook = new HSSFWorkbook(is, true);
            } else if ("xlsx".equals(type)) {
                readbook = new XSSFWorkbook(is);
            } else {
                throw new Exception("不支持的文件类型");
            }
        }
    }

    public void checkReader() throws Exception {
        if (readbook == null) {
            throw new Exception("readbook is null");
        }
    }

    public void checkWriter() throws Exception {
        if (writebook == null) {
            throw new Exception("writebook is null");
        }
    }

    public ResultForm checkFormat() throws Exception {
        return new ResultForm(false);
    }

    public List<T> readData() throws Exception {
        return null;
    }

    public T readOneRowData(Integer rowIndex) throws Exception {
        return null;
    }

    public Integer getLastRowNum() throws Exception {
        return 0;
    }

    public void writeData(List<T> data) throws Exception {
    }

    public void writeOutputStream(OutputStream out) throws Exception {
        writebook.write(out);
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public String getValue(Cell cell) {
        String value = null;
        switch (cell.getCellType()) {
            // 数字
            case Cell.CELL_TYPE_NUMERIC:
                // 如果为时间格式的内容
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 注：format格式 yyyy-MM-dd hh:mm:ss 中小时为12小时制，若要24小时制，则把小h变为H即可，yyyy-MM-dd
                    // HH:mm:ss
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    value = sdf.format(DateUtil.getJavaDate(cell.getNumericCellValue())).toString();
                    break;
                } else {
                    value = new DecimalFormat("0").format(cell.getNumericCellValue());
                }
                break;
            // 字符串
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            // Boolean
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue() + "";
                break;
            // 公式
            case Cell.CELL_TYPE_FORMULA:
                value = cell.getCellFormula() + "";
                break;
            // 空值
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            // 故障
            case Cell.CELL_TYPE_ERROR:
                value = "非法字符";
                break;
            default:
                value = "未知类型";
                break;
        }
        return value;
    }

    /**
     * 关闭工作簿
     *
     * @throws Exception
     */
    public void close() throws Exception {
        if (is != null) {
            is.close();
        }
    }

}
