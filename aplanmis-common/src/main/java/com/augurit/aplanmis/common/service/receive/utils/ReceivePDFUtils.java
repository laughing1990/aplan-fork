package com.augurit.aplanmis.common.service.receive.utils;


import com.augurit.aplanmis.common.service.receive.constant.ReceiveConstant;
import com.augurit.aplanmis.common.service.receive.vo.MatCorrectVo;
import com.augurit.aplanmis.common.service.receive.vo.ReceiveBaseVo;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import org.springframework.beans.BeanUtils;


//pdf回执模板工具类
public class ReceivePDFUtils {

    public static String createPDF(ReceiveBaseVo receiveBaseVo) throws Exception {
        String str = "";
        switch (receiveBaseVo.getReceiptType()) {
            case ReceiveConstant.MAT_TYPE://物料回执//1
            case ReceiveConstant.ITEM_MAT_CORRECT_MAT_RECEIVE_TYPE:
            case ReceiveConstant.APPLY_MAT_CORRECT_MAT_RECEIVE_TYPE:
                str = ReceivePDFTemplate.createMatTypeTemplate(receiveBaseVo);
                break;
            case ReceiveConstant.ACCEPT_TYPE://受理回执//2
            case ReceiveConstant.RETURNED_TYPE://退件回执//3
                str = ReceivePDFTemplate.createAcceptTypeTemplate(receiveBaseVo);
                break;
            case ReceiveConstant.REJECT_TYPE://不受理回执//4
                str = ReceivePDFTemplate.createRefuseTypeTemplate(receiveBaseVo);
                break;
            case ReceiveConstant.CERT_TYPE://领证回执//5
                str = ReceivePDFTemplate.createCertTypeTemplate(receiveBaseVo);
                break;
            case ReceiveConstant.MAT_CORRECT_TYPE://材料补正回执-部门//6
            case ReceiveConstant.APPLY_MAT_CORRECT_TYPE://材料补全回执-窗口//7
                MatCorrectVo correctVo = new MatCorrectVo();
                BeanUtils.copyProperties(receiveBaseVo, correctVo);
                str = ReceivePDFTemplate.createCorrectMatTemplat(correctVo);
                break;
            default:
                break;
        }
        return str;
    }


    //设置段落缩进两格
    public static void twoParagraph(String content, Document document, Font font) throws Exception {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setIndentationLeft(30);
        document.add(paragraph);
    }

    public static void indentationParagraph(String content, Document document, Font font) throws Exception {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setFirstLineIndent(30);
        document.add(paragraph);
    }

    //设置行间距
    public static void spaceParagraph(String content, Document document, Font font) throws Exception {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setSpacingAfter(5);
        document.add(paragraph);
    }

    //空一行
    public static void oneLine(Document document, Font font) throws Exception {
        Paragraph blankRow = new Paragraph(18f, " ", font);
        document.add(blankRow);
    }

    //空多行
    public static void moreLine(Document document, Font font) throws Exception {
        Paragraph blankRow = new Paragraph(18f, " ", font);
        document.add(blankRow);
        document.add(blankRow);
        document.add(blankRow);
        document.add(blankRow);
    }

    //段落（标题）靠右
    public static void paragrahRight(Document document, String content, Font font) throws Exception {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(paragraph);
    }

    //段落（标题）靠左
    public static void paragrahLeft(Document document, String content, Font font) throws Exception {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        document.add(paragraph);
    }

    //段落（标题）居中
    public static void paragrahCenter(Document document, String content, Font font) throws Exception {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
    }

    //表格段落（标题）居中
    public static Paragraph paragrahCenterCell(String content, Font font) throws Exception {
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    //表格居中
    public static PdfPCell pdfPcellCenter(String content, Font font) throws Exception {
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//水平居中
        return cell;
    }

}
