package com.augurit.aplanmis.common.service.receive.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 佛山分支-设置回执pdf页码
 * 参考：https://my.oschina.net/u/3183495/blog/1529522
 */
@Data
public class ItextPdfPagination extends PdfPageEventHelper {

    /**
     * 页眉
     */
    public String header = "";

    /**
     * 文档字体大小，页脚页眉最好和文本大小一致
     */
    public int presentFontSize = 12;

    /**
     * 文档页面大小，最好前面传入，否则默认为A4纸张
     */
    public Rectangle pageSize = PageSize.A4;

    // 模板
    public PdfTemplate total;

    // 基础字体对象
    public BaseFont bf = null;

    // 利用基础字体生成的字体对象，一般用于生成中文文字
    public Font fontDetail = null;

    //回执头部信息
    public ReceiveHeaderInfo headerInfo;

    public ItextPdfPagination() {

    }

    /**
     * @param bf 基础字体大小
     * @param presentFontSize 数据字体大小
     * @param pageSize 文档格式
     */
    public ItextPdfPagination(BaseFont bf, int presentFontSize, Rectangle pageSize) {
        this.bf = bf;
        this.presentFontSize = presentFontSize;
        this.pageSize = pageSize;
    }

    /**
     * @param yeMei 页眉字符串
     * @param presentFontSize 数据体字体大小
     * @param pageSize 页面文档大小，A4，A5，A6横转翻转等Rectangle对象
     */
    public ItextPdfPagination(String yeMei, int presentFontSize, Rectangle pageSize) {
        this.header = yeMei;
        this.presentFontSize = presentFontSize;
        this.pageSize = pageSize;
    }

    /**
     * 设置事件触发对象
     * @param writer
     * @param bf
     * @param presentFontSize
     * @param pageSize
     * @param headerInfo
     */
    public static void setFooter(PdfWriter writer, BaseFont bf, int presentFontSize, Rectangle pageSize,ReceiveHeaderInfo headerInfo){
        ItextPdfPagination headerFooter = new ItextPdfPagination(bf,presentFontSize,pageSize);
        headerFooter.setHeaderInfo(headerInfo);
        writer.setPageEvent(headerFooter);
    }

    /**
     * 文档打开时创建模板
     */
    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(50, 50);// 共 页 的矩形的长宽高
    }

    /**
     * 新开一页的时候写入头部信息
     * @param writer
     * @param document
     */
    @Override
    public void onStartPage(PdfWriter writer,Document document) {
        FsReceivePDFTemplate.initFontMap();
        int pageS = writer.getPageNumber();
        try {
            if(pageS == 1){
                ItextPdfPagination pageEvent = (ItextPdfPagination)writer.getPageEvent();
                ReceiveHeaderInfo headerInfo = pageEvent.getHeaderInfo();
                String seqNo = headerInfo==null?"":headerInfo.getSeqNo();
                String receiveName = headerInfo==null?"":headerInfo.getReceiveName();
                String instrumentNo = headerInfo==null?"":headerInfo.getInstrumentNo();
                if(StringUtils.isNotBlank(headerInfo.getRegion())){
                    FsReceivePDFTemplate.setHeader(document,seqNo,receiveName,instrumentNo,headerInfo.getRegion());
                }else{
                    FsReceivePDFTemplate.setHeader(document,seqNo,receiveName,instrumentNo,null);
                }
            }else{
                ReceivePDFUtils.drawLine(document,1f,100,BaseColor.BLACK,Element.ALIGN_CENTER,-114);
                ReceivePDFUtils.moreLine(document, FsReceivePDFTemplate.fontMap.get("font3"));
                ReceivePDFUtils.oneLine(document, FsReceivePDFTemplate.fontMap.get("font3"));
                ReceivePDFUtils.oneLine(document, FsReceivePDFTemplate.fontMap.get("font3"));
                ReceivePDFUtils.oneLine(document, FsReceivePDFTemplate.fontMap.get("font3"));
                //下面添加其他内容
            }
            writer.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  关闭每页的时候，写入页眉，写入' 页 第 X 页'这几个字。
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            if (fontDetail == null) {
                fontDetail = new Font(bf, presentFontSize, Font.NORMAL);// 数据体字体
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 1.写入页眉
//        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(header, fontDetail),document.left(), document.top() + 20, 0);

        // 2.写入 第 X 页
        int pageS = writer.getPageNumber();
        String foot1 = " 页 第 " + pageS + " 页";
        Phrase footer = new Phrase(foot1, fontDetail);
        // 3.计算foot1的长度，后面好定位'共Y'这俩字的x轴坐标，字体长度也要计算进去 = len
        float len = bf.getWidthPoint(foot1, presentFontSize);

        // 4.拿到当前的PdfContentByte
        PdfContentByte cb = writer.getDirectContent();

        //计算头部页码的间距，如果去掉序号则为80
        int height = 100;
        ItextPdfPagination pageEvent = (ItextPdfPagination)writer.getPageEvent();
        if(pageEvent.getHeaderInfo() != null && StringUtils.isNotBlank(pageEvent.getHeaderInfo().getRegion())){
            height = 132;//如果去掉序号则为112
        }

        // 5.写入页脚1
        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, footer,(document.rightMargin() + document.right() + document.leftMargin() - document.left() - len) - 40F,document.top() - height, 0);

        // 6.写入页脚2的模板
        cb.addTemplate(total,(document.rightMargin() + document.right() + document.leftMargin() - document.left()) - 180F,document.top() - height);
    }

    /**
     * 关闭文档时，替换上面写入的模板，完成整个页眉页脚组件
     */
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        // 7.最后一步了，就是关闭文档的时候，将模板替换成实际的 Y 值。
        total.beginText();
        // 生成的模版的字体、颜色
        total.setFontAndSize(bf, presentFontSize);
        //如果是main方法本地测试，则需要减掉1
//        String foot2 = "共 " + (writer.getPageNumber()-1);
        //如果是在集成环境运行，则不需要减1
        String foot2 = "共 " + writer.getPageNumber();
        // 模版显示的内容
        total.showText(foot2);
        total.endText();
        total.closePath();
    }

    public static ReceiveHeaderInfo getReceiveHeaderInfo(String seqNo, String receiveName, String instrumentNo) {
        return new ReceiveHeaderInfo(seqNo,receiveName,instrumentNo);
    }

    public static ReceiveHeaderInfo getReceiveHeaderInfo(String seqNo, String receiveName, String instrumentNo,String region) {
        return new ReceiveHeaderInfo(seqNo,receiveName,instrumentNo,region);
    }

    @Data
    static class ReceiveHeaderInfo{
        private String seqNo;
        private String region;
        private String instrumentNo;
        private String receiveName;

        public ReceiveHeaderInfo(String seqNo, String receiveName, String instrumentNo,String region) {
            this.seqNo = seqNo;
            this.receiveName = receiveName;
            this.region = region;
            this.instrumentNo = instrumentNo;
        }

        public ReceiveHeaderInfo(String seqNo, String receiveName, String instrumentNo) {
            this.seqNo = seqNo;
            this.receiveName = receiveName;
            this.instrumentNo = instrumentNo;
        }
    }

}
