package com.augurit.aplanmis.common.service.receive.utils;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.service.receive.vo.ConstructPermitVo;
import com.augurit.aplanmis.common.service.receive.vo.MatCorrectVo;
import com.augurit.aplanmis.common.service.receive.vo.MatReceiveVo;
import com.augurit.aplanmis.common.service.receive.vo.ReceiveBaseVo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * pdf模板生成工具类
 */
public class ReceivePDFTemplate {


    //市民服务中心申报材料清单模板
    public static String createMatTypeTemplate(ReceiveBaseVo receiveBaseVo) throws Exception {
        String name = receiveBaseVo.getReceiveId();
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(50, 50, 50, 50);
        // 设置pdf生成的路径
        StringBuffer str = pdfFilePath();
        str.append(name);
        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        // demo
        String title = ReceiveCommonParam.CITY_NAME + "市民服务中心申报材料清单";
        String content1 = "申报流水号：" + receiveBaseVo.getApplyinstCode();
        String content2 = "申报主体：" + receiveBaseVo.getApplicant();
        String content3 = "申报主体编号：" + (StringUtils.isNotBlank(receiveBaseVo.getApplicantIDCard()) ? receiveBaseVo.getApplicantIDCard() : "");
        String content4 = "办理人姓名：" + receiveBaseVo.getReceiveUserName() +
                "         联系电话：" + receiveBaseVo.getReceiveUserMobile();
        String content6 = "申报事项名称：" + receiveBaseVo.getItemName();

        String content7 = "办理时限：" + (receiveBaseVo.getDueNum() != null ? receiveBaseVo.getDueNum().intValue() : "-") + "个工作日";
        String content8 = "今收到申请材料：";
        String content9 = "备注：" + receiveBaseVo.getReceiveMemo();
        String content10 = "工程名称：" + receiveBaseVo.getProjName();
        int rowNumber1 = 5;
        int rowNumber2 = 6;

        // 定义字体
        FontFactoryImp ffi = new FontFactoryImp();
        // 注册全部默认字体目录，windows会自动找fonts文件夹的，返回值为注册到了多少字体
        ffi.registerDirectories();
        // 获取字体，其实不用这么麻烦，后面有简单方法
        Font font1 = ffi.getFont("黑体", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 22, Font.BOLD, null);
        Font font2 = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.BOLD, null);
        Font font = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.UNDEFINED, null);
        // 打开文档，只有打开后才能往里面加东西
        document.open();

        Paragraph paragraph1 = new Paragraph(title, font1);
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph1.setSpacingAfter(20);
        // 增加一个段落
        document.add(paragraph1);
        Paragraph paragraph2 = new Paragraph(content1, font2);
        paragraph2.setAlignment(Element.ALIGN_RIGHT);
        document.add(paragraph2);
        document.add(new Paragraph(content2, font2));
        document.add(new Paragraph(content3, font2));
        document.add(new Paragraph(content4, font2));


        document.add(new Paragraph(content6, font2));
        document.add(new Paragraph(content7, font2));
        ReceivePDFUtils.oneLine(document, font);

        // 创建表格，5列的表格
        PdfPTable table = new PdfPTable(rowNumber1);
        int width[] = {5, 20, 8, 5, 5};
        table.setWidths(width);
        table.setTotalWidth(PageSize.A4.getWidth() - 100);
        table.setLockedWidth(true);

        // 添加内容
//		Paragraph p = new Paragraph("序号",font2);
//		p.setSpacingAfter(20);
//		p.setSpacingBefore(20);
//		table.addCell(p);
        table.addCell(new Paragraph("序号", font2));
        table.addCell(new Paragraph("材料名称", font2));
        table.addCell(new Paragraph("材料形式", font2));
        table.addCell(new Paragraph("份数", font2));
        table.addCell(new Paragraph("备注", font2));
        //表格内容行数的填充leContentCell);
        if (receiveBaseVo.getAllMatList() != null && receiveBaseVo.getAllMatList().size() > 0) {
            for (int i = 0; i < receiveBaseVo.getAllMatList().size(); i++) {
                AeaHiItemMatinst item = receiveBaseVo.getAllMatList().get(i);
                table.addCell(ReceivePDFUtils.pdfPcellCenter(i + 1 + "", font));
                table.addCell(ReceivePDFUtils.pdfPcellCenter(item.getMatinstName(), font));
                long count = 0;
                String type = "-";
                if (item.getRealCopyCount() != null && item.getRealCopyCount() > 0) {
                    count = item.getRealCopyCount();
                    type = "复印件";
                } else if (item.getRealPaperCount() != null && item.getRealPaperCount() > 0) {
                    count = item.getRealPaperCount();
                    type = "原件";
                } else if (item.getAttCount() != null && item.getAttCount() > 0) {
                    count = item.getAttCount();
                    type = "电子件";
                }

                table.addCell(ReceivePDFUtils.pdfPcellCenter(type, font));
                table.addCell(ReceivePDFUtils.pdfPcellCenter(count + "", font));
                table.addCell(ReceivePDFUtils.pdfPcellCenter(item.getMemo(), font));
            }
        }
        ReceivePDFUtils.spaceParagraph(content8, document, font2);
        document.add(table);
        ReceivePDFUtils.oneLine(document, font);
        document.add(new Paragraph(content9, font));
        document.add(new Paragraph(content10, font));
        ReceivePDFUtils.oneLine(document, font);

        // 创建表格，6列的表格
        PdfPTable table1 = new PdfPTable(rowNumber2);
        int widths[] = {14, 10, 14, 10, 18, 10};
        table1.setWidths(widths);
        table1.setTotalWidth(PageSize.A4.getWidth() - 100);
        table1.setLockedWidth(true);

        table1.addCell(new Paragraph("送达部门", font));
        PdfPCell cell = new PdfPCell(new Paragraph(((MatReceiveVo) receiveBaseVo).getReceiveOrgName(), font));
        cell.setColspan(5);//合并单元格
        table1.addCell(cell);
        table1.addCell(new Paragraph("部门收件人", font));
        table1.addCell(new Paragraph("", font));
        table1.addCell(new Paragraph("取件时间", font));
        table1.addCell(new Paragraph("", font));
        table1.addCell(new Paragraph("中转人确认签名", font));
        table1.addCell(new Paragraph("", font));
        table1.addCell(new Paragraph("部门送件人", font));
        table1.addCell(new Paragraph("", font));
        table1.addCell(new Paragraph("送达时间", font));
        table1.addCell(new Paragraph("", font));
        table1.addCell(new Paragraph("中转人确认签名", font));
        table1.addCell(new Paragraph("", font));

        document.add(table1);

        // 关闭文档，才能输出
        document.close();
        writer.close();
        return str.toString();
    }


    //行政许可申请受理通知书90327
    public static String createAcceptTypeTemplate(ReceiveBaseVo receiveBaseVo) throws Exception {
        String name = null;
        if (receiveBaseVo == null) {
            return null;
        } else {
            name = receiveBaseVo.getReceiveId();
        }
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(50, 50, 50, 50);
        // 定义字体
        FontFactoryImp ffi = new FontFactoryImp();
        // 注册全部默认字体目录，windows会自动找fonts文件夹的，返回值为注册到了多少字体
        ffi.registerDirectories();
        // 获取字体，其实不用这么麻烦，后面有简单方法
        Font font1 = ffi.getFont("黑体", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 22, Font.BOLD, null);
        Font font = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.UNDEFINED, null);
        // 设置pdf生成的路径
        StringBuffer str = pdfFilePath();
        str.append(name);
        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        // demo
        String title = "行政许可申请受理通知书";
        String content1 = "编号：" + receiveBaseVo.getApplyinstCode();
        String content2 = null;
        if (StringUtils.isNotBlank(receiveBaseVo.getAgentName())) {
            content2 = receiveBaseVo.getAgentName() + ":";
        } else {
            content2 = receiveBaseVo.getApplicant() + ":";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String content3 = sdf.format(receiveBaseVo.getCreateTime()) + ",本机关收到你申请所提交的下列（补正）材料：" + "";
        //材料清单
        List<String> list = new ArrayList<String>();
        String cailiao = null;
        if (receiveBaseVo.getAllMatList() != null && receiveBaseVo.getAllMatList().size() > 0) {
            for (int i = 0; i < receiveBaseVo.getAllMatList().size(); i++) {
                AeaHiItemMatinst mat = receiveBaseVo.getAllMatList().get(i);
                if (mat.getRealCopyCount() != null && mat.getRealCopyCount() > 0) {
                    cailiao = i + 1 + "、" + mat.getMatinstName() + "(复印件" + mat.getRealCopyCount() + "份)";
                } else if (mat.getRealPaperCount() != null && mat.getRealPaperCount() > 0) {
                    cailiao = i + 1 + "、" + mat.getMatinstName() + "(原件" + mat.getRealPaperCount() + "份)";
                } else if (mat.getAttCount() != null && mat.getAttCount() > 0) {
                    cailiao = i + 1 + "、" + mat.getMatinstName() + "(电子件" + mat.getAttCount() + "份)";
                }
                list.add(cailiao);
            }
        }
        String content4 = "经审查，你（单位）申请的该行政许可事项属于本机关职权范围，申请材料齐全，符合法定形式，根据《中华人民共和国行政许可法》第三十二条的规定，现予受理。" + "";
        String content5 = "特此通知。";
        String content6 = "联系人：" + receiveBaseVo.getReceiveUserName();
        String content7 = "联系电话：" + ReceiveCommonParam.Telephone;
        String content8 = "监督电话：" + ReceiveCommonParam.Complaint_Telephone_Number;
        String content9 = "（行政机关专用印章）";
        String content10 = "注：本通知书一式两份，申请人、受理机关各存一份。";


        // 打开文档，只有打开后才能往里面加东西
        document.open();

        ReceivePDFUtils.paragrahCenter(document, title, font1);
        ReceivePDFUtils.oneLine(document, font);
        ReceivePDFUtils.paragrahCenter(document, content1, font);

        ReceivePDFUtils.paragrahLeft(document, content2, font);

        ReceivePDFUtils.twoParagraph(content3, document, font);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                document.add(new Paragraph(list.get(i), font));
            }
        }
        ReceivePDFUtils.oneLine(document, font);
        document.add(new Paragraph(content4, font));
        ReceivePDFUtils.twoParagraph(content5, document, font);
        ReceivePDFUtils.oneLine(document, font);
        ReceivePDFUtils.twoParagraph(content6, document, font);
        ;
        ReceivePDFUtils.twoParagraph(content7, document, font);
        ReceivePDFUtils.twoParagraph(content8, document, font);
        ReceivePDFUtils.oneLine(document, font);
        ReceivePDFUtils.paragrahRight(document, content9, font);
        ReceivePDFUtils.oneLine(document, font);
        document.add(new Paragraph(content10, font));
        ReceivePDFUtils.oneLine(document, font);
        // 关闭文档，才能输出
        document.close();
        writer.close();
        return str.toString();
    }

    /**
     * 领证回执模板
     *
     * @return pdf保存地址  str
     * @throws Exception
     */
    public static String createCertTypeTemplate(ReceiveBaseVo receiveBaseVo) throws Exception {
        String name = null;
        if (receiveBaseVo == null) {
            return null;
        } else {
            name = receiveBaseVo.getReceiveId();
        }
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(50, 50, 50, 50);
        // 定义字体
        FontFactoryImp ffi = new FontFactoryImp();
        // 注册全部默认字体目录，windows会自动找fonts文件夹的，返回值为注册到了多少字体
        ffi.registerDirectories();
        // 获取字体，其实不用这么麻烦，后面有简单方法
        Font font1 = ffi.getFont("黑体", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 22, Font.BOLD, null);
        Font font = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.UNDEFINED, null);
        // 设置pdf生成的路径
        StringBuffer str = pdfFilePath();
        str.append(name);
        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);
        // 打开文档，只有打开后才能往里面加东西
        document.open();

        String title = "行政许可文书送达回证";
        ReceivePDFUtils.oneLine(document, font);
        String content1 = "编号：" + receiveBaseVo.getApplyinstCode();
        ReceivePDFUtils.paragrahCenter(document, title, font1);
        ReceivePDFUtils.paragrahCenter(document, content1, font);

        //新建表格，4列的表格
        PdfPTable table = new PdfPTable(4);
        // 设置表格宽度比例为%100
        table.setWidthPercentage(100);
        // 设置表格的宽度
        table.setTotalWidth(PageSize.A4.getWidth() - 100);
        // 锁住宽度
        table.setLockedWidth(true);
        // 构建每个单元格

        table.addCell(ReceivePDFUtils.pdfPcellCenter("许可事项", font));
        PdfPCell cell = ReceivePDFUtils.pdfPcellCenter(receiveBaseVo.getItemName(), font);
        cell.setColspan(3);//合并列
        table.addCell(cell);
        table.addCell(ReceivePDFUtils.pdfPcellCenter("受送达人", font));
        table.addCell(ReceivePDFUtils.pdfPcellCenter(receiveBaseVo.getReceiveUserName(), font));
        table.addCell(ReceivePDFUtils.pdfPcellCenter(receiveBaseVo.getServiceAddress(), font));
        table.addCell(ReceivePDFUtils.pdfPcellCenter("送达人", font));
        table.addCell(ReceivePDFUtils.pdfPcellCenter(receiveBaseVo.getReceiveMemo(), font));
        table.addCell(ReceivePDFUtils.pdfPcellCenter("送达日期", font));
        //当前系统时间的年月日时分秒
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = dateFormat.format(date);
        table.addCell(ReceivePDFUtils.pdfPcellCenter(time, font));
        PdfPCell cell2 = new PdfPCell(ReceivePDFUtils.pdfPcellCenter("送达文件名称", font));
        cell2.setColspan(2);
        table.addCell(cell2);
        PdfPCell cell3 = new PdfPCell(ReceivePDFUtils.pdfPcellCenter("送达文件文号", font));
        cell3.setColspan(2);
        table.addCell(cell3);
        for (int i = 0; i < 3; i++) {
            PdfPCell cells = new PdfPCell(ReceivePDFUtils.pdfPcellCenter("", font));
            cells.setColspan(2);
            table.addCell(cells);
            PdfPCell cells2 = new PdfPCell(ReceivePDFUtils.pdfPcellCenter("", font));
            cells2.setColspan(2);
            table.addCell(cells2);
        }
        PdfPCell cell4 = new PdfPCell(ReceivePDFUtils.pdfPcellCenter("送达方式", font));
        cell4.setColspan(2);
        table.addCell(cell4);
        PdfPCell cell5 = new PdfPCell(ReceivePDFUtils.pdfPcellCenter(receiveBaseVo.getReceiveMode() == "1" ? "窗口领证" : "邮政快递", font));
        cell5.setColspan(2);
        table.addCell(cell5);
        PdfPCell cell6 = new PdfPCell(ReceivePDFUtils.pdfPcellCenter("收件人签字或盖章", font));
        cell6.setColspan(2);
        table.addCell(cell6);
        PdfPCell cell7 = new PdfPCell(ReceivePDFUtils.pdfPcellCenter("", font));
        cell7.setColspan(2);
        table.addCell(cell7);
        PdfPCell cell8 = new PdfPCell(ReceivePDFUtils.pdfPcellCenter("代收人及代收原因", font));
        cell8.setColspan(2);
        table.addCell(cell8);
        PdfPCell cell9 = new PdfPCell(ReceivePDFUtils.pdfPcellCenter("", font));
        cell9.setColspan(2);
        table.addCell(cell9);
        StringBuffer remark = new StringBuffer("备注：");
        remark.append(receiveBaseVo.getReceiveMemo());
        PdfPCell cell10 = new PdfPCell(new Paragraph(remark.toString(), font));
        cell10.setColspan(4);
        table.addCell(cell10);
        //加入表格
        document.add(table);

        document.close();
        writer.close();
        return str.toString();
    }

    /**
     * 行政许可不予受理模板
     *
     * @return pdf保存地址  str
     * @throws Exception
     */
    public static String createRefuseTypeTemplate(ReceiveBaseVo receiveBaseVo) throws Exception {
        String name = null;
        if (receiveBaseVo == null) {
            return null;
        } else {
            name = receiveBaseVo.getReceiveId();
        }
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(50, 50, 50, 50);
        // 定义字体
        FontFactoryImp ffi = new FontFactoryImp();
        // 注册全部默认字体目录，windows会自动找fonts文件夹的，返回值为注册到了多少字体
        ffi.registerDirectories();
        // 获取字体，其实不用这么麻烦，后面有简单方法
        Font font1 = ffi.getFont("黑体", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 22, Font.BOLD, null);
        Font font = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.UNDEFINED, null);
        // 设置pdf生成的路径
        StringBuffer str = pdfFilePath();
        str.append(name);
        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);
        // 打开文档，只有打开后才能往里面加东西
        document.open();
        // demo
        String title = "行政许可不予受理决定书";
        ReceivePDFUtils.oneLine(document, font);
        String content1 = "编号：" + receiveBaseVo.getApplyinstCode();
        String content2 = null;
        if (StringUtils.isNotBlank(receiveBaseVo.getAgentName())) {
            content2 = receiveBaseVo.getAgentName() + ":";
        } else {
            content2 = receiveBaseVo.getApplicant() + ":";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String content3 = sdf.format(receiveBaseVo.getCreateTime()) + ",你向我机关提出的申请事项，经审查，（根据不同情况，应具体填写属于以下的某种情形：" + "";

        //情形
        String condition = receiveBaseVo.getReceiveMemo();
        String content4 = "本机关决定不予受理。" + "";
        String content5 = "对本决定不服，可以自接到本决定之日起60日内，依法向（行政复议机关名称）申请行政复议，也可以在3个月内依法向（人民法院名称）提起行政诉讼。";

        String content6 = "（行政机关专用印章）";
        String content7 = "注：本通知书一式两份，申请人、受理机关各存一份。";


        ReceivePDFUtils.paragrahCenter(document, title, font1);
        ReceivePDFUtils.paragrahCenter(document, content1, font);

        ReceivePDFUtils.paragrahLeft(document, content2, font);

        ReceivePDFUtils.indentationParagraph(content3, document, font);
        ReceivePDFUtils.oneLine(document, font);
        ReceivePDFUtils.indentationParagraph(condition, document, font);
        ReceivePDFUtils.oneLine(document, font);
        ReceivePDFUtils.indentationParagraph(content4, document, font);
        ReceivePDFUtils.indentationParagraph(content5, document, font);
        ReceivePDFUtils.moreLine(document, font);

        ReceivePDFUtils.oneLine(document, font);
        ReceivePDFUtils.paragrahRight(document, content6, font);
        ReceivePDFUtils.oneLine(document, font);
        document.add(new Paragraph(content7, font));
        ReceivePDFUtils.oneLine(document, font);
        // 关闭文档，才能输出
        document.close();
        writer.close();
        return str.toString();
    }

    /**
     * 密码回执模板
     */
    public static void createPasswordTemplate(HttpServletResponse resp, String account, String password) throws Exception {
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(50, 50, 50, 50);
        // 定义字体
        FontFactoryImp ffi = new FontFactoryImp();
        // 注册全部默认字体目录，windows会自动找fonts文件夹的，返回值为注册到了多少字体
        ffi.registerDirectories();
        // 获取字体，其实不用这么麻烦，后面有简单方法
        Font font_ht = ffi.getFont("黑体", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 22, Font.BOLD, null);
        Font font_st = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.UNDEFINED, null);
        //设置保存地址弹出框
        resp.setContentType("application/octet-stream");
        resp.setCharacterEncoding("UTF-8");
        //设置文件的名字
//		String fileName = new String("password".getBytes(),"ISO8859-1");
        resp.addHeader("Content-Disposition", "attachment; filename=password.pdf");
        OutputStream out = resp.getOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        String title = "机密函件";
        //虚线
        Paragraph p2 = new Paragraph();
        p2.add(new Chunk(new DottedLineSeparator()));
        p2.setSpacingBefore(-15);
        String loginName = "登录名：" + account;
        String pwd = "密码：" + password;
        Calendar now = Calendar.getInstance();
        String inscription = now.get(Calendar.YEAR) + "年" + (now.get(Calendar.MONTH) + 1) + "月" + now.get(Calendar.DAY_OF_MONTH) + "日";
        //标题
        // 打开文档，只有打开后才能往里面加东西
        document.open();
        ReceivePDFUtils.paragrahCenter(document, title, font_ht);
        ReceivePDFUtils.oneLine(document, font_st);
        document.add(p2);
        ReceivePDFUtils.oneLine(document, font_st);
        ReceivePDFUtils.twoParagraph(loginName, document, font_st);
        ReceivePDFUtils.twoParagraph(pwd, document, font_st);
        ReceivePDFUtils.paragrahRight(document, inscription, font_st);
        document.close();
        writer.close();
    }

    //材料补正回执
    public static String createCorrectMatTemplat(MatCorrectVo receiveBaseVo) throws Exception {

        String name = receiveBaseVo.getReceiveId();
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(50, 50, 50, 50);
        // 设置pdf生成的路径
        String property = System.getProperty("user.dir");
        System.out.println(property);
//        StringBuffer str = new StringBuffer("C:" + File.separator + "Users" + File.separator + "23948" + File.separator + "Downloads");
//        StringBuffer str = new StringBuffer("D:/");
        StringBuffer str = pdfFilePath();
        str.append(name);
        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        // demo
        String title = ReceiveCommonParam.CITY_NAME + "市民服务中心材料补正清单";
        String content1 = "申报流水号：" + receiveBaseVo.getApplyinstCode();
        String content2 = "申报主体：" + receiveBaseVo.getApplicant();
        String content3 = "申报主体编号：" + (StringUtils.isNotBlank(receiveBaseVo.getApplicantIDCard()) ? receiveBaseVo.getApplicantIDCard() : "");
        String content4 = "办理人姓名：" + receiveBaseVo.getReceiveUserName() +
                "         联系电话：" + ReceivePDFTemplate.changeNullToEmpty(receiveBaseVo.getReceiveUserMobile());
        String content6 = "申报事项名称：" + ReceivePDFTemplate.changeNullToEmpty(receiveBaseVo.getItemName());
        String content7 = "办理时限：" + (receiveBaseVo.getDueNum() != null ? receiveBaseVo.getDueNum().intValue() : "-") + "个工作日";
        String content8 = "今收到申请材料：";
        String content9 = "备注：" + receiveBaseVo.getReceiveMemo();
        String content10 = "工程名称：" + receiveBaseVo.getProjName();
        String content11 = "工程编号：" + receiveBaseVo.getProjLocalCode();
        String content12 = "受理单位：" + receiveBaseVo.getChargeOrgName();
        int rowNumber1 = 5;
        int rowNumber2 = 6;

        // 定义字体
        FontFactoryImp ffi = new FontFactoryImp();
        // 注册全部默认字体目录，windows会自动找fonts文件夹的，返回值为注册到了多少字体
        ffi.registerDirectories();
        // 获取字体，其实不用这么麻烦，后面有简单方法
        Font font1 = ffi.getFont("黑体", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 22, Font.BOLD, null);
        Font font2 = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.BOLD, null);
        Font font = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.UNDEFINED, null);
        // 打开文档，只有打开后才能往里面加东西
        document.open();

        Paragraph paragraph1 = new Paragraph(title, font1);
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph1.setSpacingAfter(20);
        // 增加一个段落
        document.add(paragraph1);
        Paragraph paragraph2 = new Paragraph(content1, font2);
        paragraph2.setAlignment(Element.ALIGN_RIGHT);
        document.add(paragraph2);
        document.add(new Paragraph(content11, font2));
        document.add(new Paragraph(content10, font2));
        document.add(new Paragraph(content2, font2));
        document.add(new Paragraph(content3, font2));
        document.add(new Paragraph(content4, font2));


        document.add(new Paragraph(content6, font2));
        document.add(new Paragraph(content12, font2));
        document.add(new Paragraph(content7, font2));
        ReceivePDFUtils.oneLine(document, font);

        // 创建表格，5列的表格
        PdfPTable table = new PdfPTable(rowNumber1);
        int width[] = {5, 20, 8, 5, 5};
        table.setWidths(width);
        table.setTotalWidth(PageSize.A4.getWidth() - 100);
        table.setLockedWidth(true);

        // 添加内容
//		Paragraph p = new Paragraph("序号",font2);
//		p.setSpacingAfter(20);
//		p.setSpacingBefore(20);
//		table.addCell(p);
        table.addCell(new Paragraph("序号", font2));
        table.addCell(new Paragraph("材料名称", font2));
        table.addCell(new Paragraph("材料形式", font2));
        table.addCell(new Paragraph("份数", font2));
        table.addCell(new Paragraph("备注", font2));
        //表格内容行数的填充leContentCell);
        if (receiveBaseVo.getAllMatList() != null && receiveBaseVo.getAllMatList().size() > 0) {
            for (int i = 0; i < receiveBaseVo.getAllMatList().size(); i++) {
                AeaHiItemMatinst item = receiveBaseVo.getAllMatList().get(i);
                table.addCell(ReceivePDFUtils.pdfPcellCenter(i + 1 + "", font));
                table.addCell(ReceivePDFUtils.pdfPcellCenter(item.getMatinstName(), font));
                long count = 0;
                String type = "-";
                if (item.getAttCount() != null && item.getAttCount() > 0) {
                    count = item.getAttCount();
                    type = "电子件";
                } else if (item.getRealCopyCount() != null && item.getRealCopyCount() > 0) {
                    count = item.getRealCopyCount();
                    type = "复印件";
                } else if (item.getRealPaperCount() != null && item.getRealPaperCount() > 0) {
                    count = item.getRealPaperCount();
                    type = "原件";
                }

                table.addCell(ReceivePDFUtils.pdfPcellCenter(type, font));
                table.addCell(ReceivePDFUtils.pdfPcellCenter(count + "", font));
                table.addCell(ReceivePDFUtils.pdfPcellCenter(item.getMemo(), font));
            }
        }
        ReceivePDFUtils.spaceParagraph(content8, document, font2);
        document.add(table);
        ReceivePDFUtils.oneLine(document, font);
        document.add(new Paragraph(content9, font));
        /*document.add(new Paragraph(content10, font));*/
        ReceivePDFUtils.oneLine(document, font);

        // 创建表格，6列的表格
        /*PdfPTable table1 = new PdfPTable(rowNumber2);
        int widths[] = {14, 10, 14, 10, 18, 10};
        table1.setWidths(widths);
        table1.setTotalWidth(PageSize.A4.getWidth() - 100);
        table1.setLockedWidth(true);

        table1.addCell(new Paragraph("送达部门", font));

        PdfPCell cell = new PdfPCell(new Paragraph(((MatCorrectVo) receiveBaseVo).getReceiveOrgName(), font));
        cell.setColspan(5);//合并单元格
        table1.addCell(cell);
        table1.addCell(new Paragraph("部门收件人", font));
        table1.addCell(new Paragraph("", font));
        table1.addCell(new Paragraph("取件时间", font));
        table1.addCell(new Paragraph("", font));
        table1.addCell(new Paragraph("中转人确认签名", font));
        table1.addCell(new Paragraph("", font));
        table1.addCell(new Paragraph("部门送件人", font));
        table1.addCell(new Paragraph("", font));
        table1.addCell(new Paragraph("送达时间", font));
        table1.addCell(new Paragraph("", font));
        table1.addCell(new Paragraph("中转人确认签名", font));
        table1.addCell(new Paragraph("", font));

        document.add(table1);*/

        // 关闭文档，才能输出
        document.close();
        writer.close();
        return str.toString();
    }

    /**
     * 根据路径读取pdf文档
     *
     * @param str
     * @param resp
     * @return
     */
    public static void readPdf(String str, HttpServletResponse resp) {
        //读取指定路径下的pdf文件
        File file = new File(str);
        if (file.exists()) {
            byte[] data = null;
            try {
                FileInputStream input = new FileInputStream(file);
                data = new byte[input.available()];
                input.read(data);
                resp.getOutputStream().write(data);
                input.close();
            } catch (Exception e) {
                System.out.println("pdf文件处理异常：" + e);
            }
        } else {
            System.out.println("该路径下不存在pdf文件");
        }
    }


    private static String changeNullToEmpty(Object o) {
        if (null == o) {
            return "无";
        } else {
            return o.toString();
        }
    }


    //建筑工程施工许可证pdf模板
    public static void createConstructPermitPdf(Boolean print, ConstructPermitVo vo, HttpServletResponse response) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();

        try {
            ClassPathResource classPathResource = new ClassPathResource("static/receive/default/施工许可证新版(总承包) - 无底图.pdf");
            InputStream inputStream = classPathResource.getInputStream();

            PdfReader reader = new PdfReader(inputStream);
            PdfStamper stamper = new PdfStamper(reader, ba);
            PdfContentByte under = stamper.getOverContent(1);

            if (null == print || !print) {
                ClassPathResource imgPathResource = new ClassPathResource("static/receive/default/施工许可证新版(总承包).png");
                URL imgUrl = imgPathResource.getURL();
                Image img1 = Image.getInstance(imgUrl);
                img1.setAbsolutePosition(40, 0);
                img1.scaleToFit(reader.getPageSize(1));
                under.addImage(img1, img1.getScaledWidth(), 0, 0, img1.getScaledHeight(), 0, 0);

            }

            //二维码
            if (vo.getCertBuildQrcode() != null && vo.getCertBuildQrcode().length > 0) {
                Image img = Image.getInstance(vo.getCertBuildQrcode());
                img.setAbsolutePosition(100, 90);
                img.scaleToFit(130, 130);
//                img.scaleToFit(reader.getPageSize(1));//大小*/
//                under.addImage(img, 13, 0, 0, 40, 0, 0); //设置图片大小
                under.addImage(img);

            }


            //使用中文字体
            BaseFont bf = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            AcroFields form = stamper.getAcroFields();
            form.getFields();
//            form.setFieldProperty(nameField, "textfont", baseFont, null);
            form.addSubstitutionFont(bf);
            //ConstructPermitVo tempVo = ConstructPermitVo.buildDemoVo();
            Field[] declaredFields = ConstructPermitVo.class.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                String name = field.getName();
                //Object o = field.get(tempVo);
                Object o = field.get(vo);
                String value = "";
                if (null != o) {
                    value = o.toString();
                }
                System.out.println(value);
                form.setField(name, value);
            }
            stamper.setFormFlattening(true);

            stamper.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        response.setContentType("application/pdf");
//        response.setHeader("Content-disposition", "attachment; filename=" + UUID.randomUUID().toString() + ".pdf");
        response.setContentLength(ba.size());
        try {
            ServletOutputStream out = response.getOutputStream();
            ba.writeTo(out);
            out.flush();
            out.close();
            ba.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("A Document error:" + e.getMessage());
        }

    }

    //设置text字体
    public void setFront() throws IOException, DocumentException {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(bf, 12, Font.BOLD);
//Font font = FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD, BaseColor.RED);
        //jar包也可以获取
        ClassPathResource classPathResource = new ClassPathResource("static/receive/default/建筑工程施工许可证-无底图.pdf");

        InputStream inputStream = classPathResource.getInputStream();
        PdfReader reader = new PdfReader(inputStream);
        PdfStamper stamper = new PdfStamper(reader, ba);
        AcroFields s = stamper.getAcroFields();

// 设置加粗只能用这种方式
        List<AcroFields.FieldPosition> multiLinePosition = s.getFieldPositions("name");
        int page = multiLinePosition.get(0).page;
        Rectangle rectangle = multiLinePosition.get(0).position;
        float left = rectangle.getLeft();
        float right = rectangle.getRight();
        float top = rectangle.getTop();
        float bottom = rectangle.getBottom();
        PdfContentByte pdfContentByte = stamper.getOverContent(page);
        ColumnText columnText = new ColumnText(pdfContentByte);

        Rectangle r = new Rectangle(left, bottom, right, top);
        columnText.setSimpleColumn(r);

//FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD, BaseColor.RED)
        Chunk chunk = new Chunk("小糊涂");
        Paragraph paragraph = new Paragraph(12, chunk);
//paragraph.setSpacingBefore(16);
        columnText.addText(paragraph);
// 设置字体，如果不设置添加的中文将无法显示
        paragraph.setFont(font);
        columnText.addElement(paragraph);
        columnText.go();
    }

    private static StringBuffer pdfFilePath() {
        // 设置pdf生成的路径
        StringBuffer str = new StringBuffer(System.getProperty("java.io.tmpdir"));
        // linux路径
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
            String dir = "/opt/pdf/";
            str = new StringBuffer(dir);
            File dirFile = new File(dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
        }
        return str;
    }

    public static void main(String[] args) {
        String property = System.getProperty("java.io.tmpdir");
        System.out.println(pdfFilePath());
    }
}
