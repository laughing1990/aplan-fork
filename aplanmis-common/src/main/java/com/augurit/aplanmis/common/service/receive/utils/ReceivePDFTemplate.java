package com.augurit.aplanmis.common.service.receive.utils;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaProjApplyAgent;
import com.augurit.aplanmis.common.service.receive.vo.ConstructPermitVo;
import com.augurit.aplanmis.common.service.receive.vo.MatCorrectVo;
import com.augurit.aplanmis.common.service.receive.vo.MatReceiveVo;
import com.augurit.aplanmis.common.service.receive.vo.ReceiveBaseVo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import lombok.Data;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * pdf模板生成工具类
 */
public class ReceivePDFTemplate {

    public static Map<String,Font> fontMap = new HashMap<>();

    public static void initFontMap(){
        if(fontMap.isEmpty()){
            // 定义字体
            FontFactoryImp ffi = new FontFactoryImp();
            // 注册全部默认字体目录，windows会自动找fonts文件夹的，返回值为注册到了多少字体
            ffi.registerDirectories();
            //黑体三号加粗
            Font font1 = ffi.getFont("黑体", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.BOLD, null);
            //宋体二号加粗
            Font font2 = ffi.getFont("宋体", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 22, Font.BOLD, null);
            //仿宋4号
            Font font3 = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.UNDEFINED, null);
            //仿宋5号
            Font font4 = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10.5f, Font.UNDEFINED, null);
            //特殊符号☑、□字体
            Font font5 = ffi.getFont("Segoe UI Symbol", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.UNDEFINED, null);
            //仿宋三号
            Font font6 = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.UNDEFINED, null);
            fontMap.put("font1",font1);
            fontMap.put("font2",font2);
            fontMap.put("font3",font3);
            fontMap.put("font4",font4);
            fontMap.put("font5",font5);
            fontMap.put("font6",font6);
        }
    }

    /**
     * 佛山代办协议模板生成
     * @return  返回生成模板文件保存的路径，调用的对象记得删除文件
     * @throws Exception
     */
    public static String createAgencyAgreement(AeaProjApplyAgent aeaProjApplyAgent) throws Exception {
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(80, 80, 10, 10);
        // 设置pdf生成的路径
//        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        StringBuffer str = new StringBuffer("F:\\develop\\foshan20191120\\4、关于印发佛山市行政许可和公共服务事项流程标准应用规范的通知（ 佛政务〔2017〕81号）\\回执模板\\");

        str.append("佛山市重点工程建设项目代办委托协议");
        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        //设置字体格式
        initFontMap();
        Font font1 = fontMap.get("font1");
        Font font2 = fontMap.get("font2");
        Font font4 = fontMap.get("font4");
        Font font6 = fontMap.get("font6");
        /*******************************************************模板填写的参数*******************************************************/
        String receiveName = "佛山市重点工程建设项目代办委托协议";
        String instrumentNo = aeaProjApplyAgent.getAgreementCode();
        String firstParty  = aeaProjApplyAgent.getUnitName();
        String secondParty  = aeaProjApplyAgent.getWindowName();
        String projName = aeaProjApplyAgent.getProjName();
        String stageName = aeaProjApplyAgent.getAgentStageName();
        String firstName  = aeaProjApplyAgent.getApplyUserName();
        String firstPhone = aeaProjApplyAgent.getApplyUserPhone();
        String secondName  =  aeaProjApplyAgent.getAgentUserName();
        String secondPhone = aeaProjApplyAgent.getAgentUserMobile();
        Date signatureDate = aeaProjApplyAgent.getAgreementSignTime();
        /*******************************************************模板填写的参数*******************************************************/

        // 打开文档，只有打开后才能往里面加东西
        document.open();
        ReceivePDFUtils.paragrahCenter(document,receiveName,font2);
        ReceivePDFUtils.paragrahCenter(document,"协议编号："+ instrumentNo,font4);
        ReceivePDFUtils.oneLine(document,font6);
        ReceivePDFUtils.oneLine(document,font6);

        Paragraph paragraph = new Paragraph("委托单位：", font6);
        //设置段落行间距
        paragraph.setLeading(25f);
        paragraph.add(ReceivePDFUtils.getUnderLineChunk(firstParty,font6));
        paragraph.add("（以下简称甲方）");
        document.add(paragraph);

        Paragraph paragraph2 = new Paragraph("受托单位：", font6);
        paragraph2.setLeading(25f);
        paragraph2.add(ReceivePDFUtils.getUnderLineChunk(secondParty,font6));
        paragraph2.add("（以下简称乙方）");
        document.add(paragraph2);

        String content1 = "甲乙双方根据《佛山市工程建设项目审批制度改革实施方案》（佛府办函〔2019〕6号）和《佛山市重点工程建设项目审批代办服务实施办法》的文件精神，签订本重点工程建设项目联合审批代办委托协议。";
        ReceivePDFUtils.twoSpacing(content1,document,font6,25f);

        ReceivePDFUtils.twoSpacing("一、代办内容",document,font1,25f);
        ReceivePDFUtils.twoSpacing("甲方将下列重点工程建设项目所涉及的审批服务事项委托乙方代办：",document,font6,25f);
        ReceivePDFUtils.twoSpacingConcatUnderLineChunk("（一）委托代办项目名称：",projName,document,font6,25f);
        ReceivePDFUtils.twoSpacingConcatUnderLineChunk("（二）委托代办具体事项：",stageName,document,font6,25f);
        List<ReceivePDFUtils.ParagraphCentent> centents = new ArrayList<>();
        centents.add(new ReceivePDFUtils.ParagraphCentent(false,"（三）甲方确定"));
        centents.add(new ReceivePDFUtils.ParagraphCentent(true,firstName));
        centents.add(new ReceivePDFUtils.ParagraphCentent(false,"联系电话"));
        centents.add(new ReceivePDFUtils.ParagraphCentent(true,firstPhone));
        centents.add(new ReceivePDFUtils.ParagraphCentent(false,"为联系人，具体负责与乙方代办员的日常沟通联系。联系人一经确定不得随意变更,如有变更需及时通知乙方。"));
        ReceivePDFUtils.twoSpacingConcatUnderLineChunk(centents,document,font6,25f);
        centents.clear();
        centents.add(new ReceivePDFUtils.ParagraphCentent(false,"（四）乙方根据项目具体情况，指派"));
        centents.add(new ReceivePDFUtils.ParagraphCentent(true,secondName));
        centents.add(new ReceivePDFUtils.ParagraphCentent(false,"联系电话"));
        centents.add(new ReceivePDFUtils.ParagraphCentent(true,secondPhone));
        centents.add(new ReceivePDFUtils.ParagraphCentent(false,"为甲方提供代办服务，代办员一经确定不得随意变更,如有变更需及时通知甲方。"));
        ReceivePDFUtils.twoSpacingConcatUnderLineChunk(centents,document,font6,25f);

        ReceivePDFUtils.twoSpacing("二、甲方职责",document,font1,25f);
        ReceivePDFUtils.twoSpacing("（一）负责及时、真实、充分地提供项目申报相关材料, 与代办员共同做好项目申报材料整理工作。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（二）根据审批职能部门提出的要求，及时对申报材料进行修改或补充。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（三）审批环节必须由五方责任主体到场的，甲方应及时派人到场。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（四）负责按规定及时交纳各类规费。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（五）协助乙方对代办员的管理，向乙方及时、客观反映代办员的工作表现，并对代办工作提出意见和建议。",document,font6,25f);

        ReceivePDFUtils.twoSpacing("三、乙方职责",document,font1,25f);
        ReceivePDFUtils.twoSpacing("（一）按照“合法高效”“自愿委托”“无偿代办”“全程服务”“协同联动”等原则，为重点工程建设项目联合审批工作提供全程代办服务。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（二）负责为工程建设项目联合审批提供咨询辅导，指导甲方熟悉办事流程及办事指南，详细说明各项材料的具体要求以及材料的获取途径等。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（三）协助甲方分阶段准备申报材料，然后由代办员通过审批服务平台填写一张表单，上传一套材料，通过系统分派至相关审批部门办理。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（四）负责跟踪监督各部门审批进度，跟进了解审批中的问题并及时向甲方反馈，同时制定相关解决办法，落实协调工作等。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（五）按要求做好代办项目相关资料的整理、保管和移交工作。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（六）对甲方提交的有关材料中所涉商业秘密、技术秘密和个人隐私负有保密责任。",document,font6,25f);

        ReceivePDFUtils.twoSpacing("四、协议终止",document,font1,25f);
        ReceivePDFUtils.twoSpacing("（一）委托代办事项完成，办理相关手续后，本协议自行终止。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（二）甲方有权根据项目代办实际情况，提出终止本委托协议，双方填妥《佛山市重点工程建设项目代办委托终止单》后，本委托协议终止。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（三）因项目本身不具备办结条件，或者甲方在项目申报过程中有弄虚作假行为的，乙方有权提出终止本委托协议，双方填妥《佛山市重点工程建设项目代办委托终止单》后，本委托协议终止。",document,font6,25f);

        ReceivePDFUtils.twoSpacing("五、其他约定",document,font1,25f);
        ReceivePDFUtils.twoSpacing("（一）本协议所涉及的代办服务，除按法律、法规明确规定必须由甲方交纳的费用外，一律实行免费代办服务，甲方无须为此支付代办费用。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（二）乙方将认真履行代办职能，并充分发挥协调作用，力争及时、有效完成代办任务，但乙方不保证所代办项目能完全按照甲方所希望的时间或结果办结。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（三）本协议所指的代办服务，适用《中华人民共和国民法通则》关于“代理”的规定。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（四）本协议经甲乙双方或其代表人签字（或盖章）后生效。",document,font6,25f);
        ReceivePDFUtils.twoSpacing("（五）本协议一式两份，甲、乙双方各执一份。",document,font6,25f);
        ReceivePDFUtils.oneLine(document,font6);
        ReceivePDFUtils.oneLine(document,font6);

        ReceivePDFUtils.twoSpacing("甲方（盖章）：　              乙方（盖章）：",document,font6,25f);
        ReceivePDFUtils.twoSpacing("代表人（签字）：",document,font6,25f);
        ReceivePDFUtils.moreLine(document,font6);
        ReceivePDFUtils.twoSpacing("　                签订时间：" + new SimpleDateFormat(" yyyy 年 MM 月 dd 日").format(signatureDate),document,font6,25f);

        // 关闭文档，才能输出
        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }


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

    public static void main(String[] args) throws Exception{
//        String property = System.getProperty("java.io.tmpdir");
//        System.out.println(pdfFilePath());
//        createAgencyAgreement(null);
        String str = "2,3";
        String[] split = str.split(",");
        System.out.println(split.length);
    }
}
