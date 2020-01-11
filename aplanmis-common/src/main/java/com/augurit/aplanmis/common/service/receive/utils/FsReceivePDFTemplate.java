package com.augurit.aplanmis.common.service.receive.utils;

import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.service.projAccept.vo.ProjAcceptOpinionSummaryVo;
import com.augurit.aplanmis.common.service.receive.vo.ReceiveBaseVo;
import com.augurit.aplanmis.common.utils.DateUtils;
import com.google.common.collect.Maps;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 4.1.0佛山分支-回执PDF模板生成
 */
public class FsReceivePDFTemplate {

    public static final String TI_JIAO_SHEN_QING = "提交申请回执";
    public static final String JIE_SHOU = "接收回执";
    public static final String SHOU_LI = "受理回执";
    public static final String BU_ZHENG = "申请材料补正告知书";
    public static final String BU_YU_SHOU_LI = "不予受理通知书";
    public static final String TE_SHU = "特殊程序审查期限告知书";
    public static final String YAN_CHANG = "延长办理期限通知书";

    public static final String PI_ZHUN = "批准决定书";
    public static final String BU_YU_PI_ZHUN = "不予批准决定书";
    public static final String JIAO_FEI = "缴费通知书";
    public static final String SONG_DA = "送达回执";
    public static final String WU_LIAO_LIU_ZHUANG = "物料流转凭证";

    public static final String LHYS_ZHONGSHEN_YJS = "工程建设项目竣工联合验收终审意见书";

    public static final String BMYZ = "（部门印章）";

    public static Map<String,Font> fontMap = new HashMap<>();

    public static void initFontMap(){
        if(fontMap.isEmpty()){
            // 定义字体
            FontFactoryImp ffi = new FontFactoryImp();
            // 注册全部默认字体目录，windows会自动找fonts文件夹的，返回值为注册到了多少字体
            ffi.registerDirectories();
            //设置字体格式
            Font font1 = ffi.getFont("黑体", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.UNDEFINED, null);
            Font font2 = ffi.getFont("宋体", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 22, Font.BOLD, null);
            Font font3 = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.UNDEFINED, null);
            Font font4 = ffi.getFont("仿宋", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10.5f, Font.UNDEFINED, null);
            Font font5 = ffi.getFont("Segoe UI Symbol", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.UNDEFINED, null);
            Font font6 = ffi.getFont("宋体", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10.5f, Font.UNDEFINED, null);

            fontMap.put("font1",font1);
            fontMap.put("font2",font2);
            fontMap.put("font3",font3);
            fontMap.put("font4",font4);
            fontMap.put("font5",font5);
            fontMap.put("font6",font6);
        }
    }

    /**
     * 提交申请回执
     * @return  返回生成回执文件保存的路径，调用的对象记得删除文件
     * @throws Exception
     */
    public static String createTiJiaoShenQingReceipt() throws Exception {
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(70, 70, 10, 10);
        // 设置pdf生成的路径
//        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        StringBuffer str = new StringBuffer("C:\\document\\奥格\\工改\\工建与广东省平台（数广）对接\\佛山工改\\回执模板\\");

        str.append(TI_JIAO_SHEN_QING);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        //设置字体格式
        initFontMap();
        Font font3 = fontMap.get("font3");
        /*******************************************************回执填写的参数*******************************************************/
        String seqNo = "序号1-1： ";
        String receiveName = TI_JIAO_SHEN_QING;
        String instrumentNo = "12345678987654321";
        String applyName = "三水进欣制线有限公司三水进欣制线有限公司";
        String projectName = "测试项目";
        String projectCode = "201911111111";
        String certNo1 = "999999121212344555";
        String phone1 =  "15338888999";
        String address = "江门市蓬江区群福路南侧地段江门市蓬江区群福路南侧地段";
        String username =  "王龙龙";
        String certNo2 = "92371121MA3MXH0H82";
        String linkmanName =  "王龙龙";
        String linkmanPhone =  "13111111111";
        String certNo3 = "92371121MA3MXH0H82";
        String itemName = "属地公共区域，一、二级风险等级或者投资30万元以上的安全技术防范系统竣工验收";
        Date applyDate = new Date();
        Date printDate = new Date();
        List<PdfTableMatInfo> matInfoList = new ArrayList<>();
        matInfoList.add(new PdfTableMatInfo("1","测试材料1","1","1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("2","测试材料2","1","1"));//测试材料
        /*******************************************************回执填写的参数*******************************************************/

        //设置页码
        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(seqNo, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer,font3.getBaseFont(),14,PageSize.A4,receiveHeaderInfo);
        // 打开文档，只有打开后才能往里面加东西
        document.open();
        Map<String,String> dataMap = new HashMap();
        dataMap.put("projectName",projectName);
        dataMap.put("projectCode",projectCode);
        dataMap.put("applyName",applyName);
        dataMap.put("certNo1",certNo1);
        dataMap.put("phone1",phone1);
        dataMap.put("address",address);
        dataMap.put("username",username);
        dataMap.put("certNo2",certNo2);
        dataMap.put("linkmanName",linkmanName);
        dataMap.put("linkmanPhone",linkmanPhone);
        dataMap.put("certNo3",certNo3);
        createfirstTable(document,font3,dataMap);

        String content1 = "你于 ";
        Paragraph paragraph = new Paragraph(content1, font3);
        //缩进两格
        paragraph.setFirstLineIndent(30);
        //设置段落行间距
        paragraph.setLeading(25f);
        //日期设置下滑线
        setDateUnderLine(paragraph,font3,applyDate);
        paragraph.add("申请办理 ");
        paragraph.add(getUnderLineChunk(itemName,font3));
        paragraph.add(", 本申请将于1个工作日内作出接收决定。符合接收要求的，予以接收；不符合要求的，退回本申请，补正内容后重新提交申请。");
        document.add(paragraph);
        ReceivePDFUtils.twoSpacing("接收材料清单如下：",document,font3,25f);

        //新建表格，4列的表格
        PdfPTable table2 = new PdfPTable(4);
        table2.setTotalWidth(PageSize.A4.getWidth());
        int width2[] = {10, 30,20, 20};
        table2.setWidths(width2);
        table2.setTotalWidth(PageSize.A4.getWidth() - 130);
        //表格外上间距 设为10
        table2.setSpacingBefore(10f);
        table2.setLockedWidth(true);
        addOneRow4Cell(table2,font3,"序号","材料名称","原件（份）","复印件（份）");

        //表格除了表头以外默认行数
        int defaultRow = 4;
        //添加材料
        addMatListTable(document,defaultRow,matInfoList,table2,font3);
        calculateCreateStampPart(writer,document,printDate,null);

        // 关闭文档，才能输出
        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    /**
     * 接收回执
     * @return  返回生成回执文件保存的路径，调用的对象记得删除文件
     * @throws Exception
     */
    public static String createJieShouReceipt(ReceiveBaseVo receiveBaseVo) throws Exception {
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(70, 70, 10, 10);
        // 设置pdf生成的路径
//        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        StringBuffer str = new StringBuffer("C:\\document\\奥格\\工改\\工建与广东省平台（数广）对接\\佛山工改\\回执模板\\");

        str.append(JIE_SHOU);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        //设置字体格式
        initFontMap();
        Font font3 = fontMap.get("font3");
        /*******************************************************回执填写的参数*******************************************************/
        String seqNo = "序号1-2： ";
        String receiveName = JIE_SHOU;
        String instrumentNo = receiveBaseVo.getDocumentNum() == null?"":receiveBaseVo.getDocumentNum();
        String itemName = receiveBaseVo.getItemName();
        Date applyDate = new Date();
        Date printDate = new Date();
        List<PdfTableMatInfo> matInfoList = new ArrayList<>();
        matInfoList.add(new PdfTableMatInfo("1","测试材料1","1","1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("2","测试材料2","1","1"));//测试材料
        /*******************************************************回执填写的参数*******************************************************/

        //设置页码
        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(seqNo, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer,font3.getBaseFont(),14,PageSize.A4,receiveHeaderInfo);
        // 打开文档，只有打开后才能往里面加东西
        document.open();
        //组装展示数据
        Map<String,String> dataMap = buildBasicData(receiveBaseVo);
        createfirstTable(document,font3,dataMap);

        String content1 = "你于 ";
        Paragraph paragraph = new Paragraph(content1, font3);
        //缩进两格
        paragraph.setFirstLineIndent(30);
        //设置段落行间距
        paragraph.setLeading(25f);
        //日期设置下滑线
        setDateUnderLine(paragraph,font3,applyDate);
        paragraph.add("申请办理 ");
        paragraph.add(getUnderLineChunk(itemName,font3));
        paragraph.add(" ，经核查，符合接收要求，予以接收。本申请将于 2 个工作日内作出受理决定。");
        document.add(paragraph);
        ReceivePDFUtils.twoSpacing("接收材料清单如下：",document,font3,25f);

        //新建表格，4列的表格
        PdfPTable table2 = new PdfPTable(4);
        table2.setTotalWidth(PageSize.A4.getWidth());
        int width2[] = {10, 30,20, 20};
        table2.setWidths(width2);
        table2.setTotalWidth(PageSize.A4.getWidth() - 130);
        //表格外上间距 设为10
        table2.setSpacingBefore(10f);
        table2.setLockedWidth(true);
        addOneRow4Cell(table2,font3,"序号","材料名称","原件（份）","复印件（份）");

        //表格除了表头以外默认行数
        int defaultRow = 4;
        //添加材料
        addMatListTable(document,defaultRow,matInfoList,table2,font3);
        calculateCreateStampPart(writer,document,printDate,null);

        // 关闭文档，才能输出
        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    /**
     * 受理回执
     * @return
     * @throws Exception
     */
    public static String createShouLiReceipt(ReceiveBaseVo receiveBaseVo)throws Exception{
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(70, 70, 10, 10);
        StringBuffer str = ReceivePDFTemplate.pdfFilePath();

        str.append(SHOU_LI);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        initFontMap();
        Font font3 = fontMap.get("font3");
        /*******************************************************回执填写的参数*******************************************************/
        String seqNo = "序号2-1： ";
        String receiveName = SHOU_LI;
        String instrumentNo = receiveBaseVo.getDocumentNum() == null?"":receiveBaseVo.getDocumentNum();
        String itemName = receiveBaseVo.getItemName();
//        String legalProvisions = "《中华人民共和国行政许可法》第三十二条的规定";
        String legalProvisions = receiveBaseVo.getLegalProvisions();
        String timeLimit = (receiveBaseVo.getDueNum() != null ? receiveBaseVo.getDueNum().intValue() : "-") + "个工作日";
//        String timeLimit = "1个工作日";
        Date applyDate = new Date();
        Date printDate = new Date();
        List<PdfTableMatInfo> matInfoList = new ArrayList<>();
        buildMatList(receiveBaseVo,matInfoList);
        /*******************************************************回执填写的参数*******************************************************/

        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(seqNo, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer,font3.getBaseFont(),14,PageSize.A4,receiveHeaderInfo);
        document.open();
        //组装展示数据
        Map<String,String> dataMap = buildBasicData(receiveBaseVo);
        createfirstTable(document,font3,dataMap);

        String content1 = "你单位于 ";
        Paragraph paragraph = new Paragraph(content1, font3);
        paragraph.setFirstLineIndent(30);
        paragraph.setLeading(25f);
        setDateUnderLine(paragraph,font3,applyDate);
        paragraph.add("申请办理 ");
        paragraph.add(getUnderLineChunk(itemName,font3));
        paragraph.add(" ，根据");
        paragraph.add(getUnderLineChunk(legalProvisions,font3));
        paragraph.add("，符合受理条件，予以受理。");
        document.add(paragraph);
        ReceivePDFUtils.twoSpacing("受理材料清单如下：",document,font3,25f);

        //新建表格，4列的表格
        PdfPTable table2 = new PdfPTable(4);
        table2.setTotalWidth(PageSize.A4.getWidth());
        int width2[] = {10, 30,20, 20};
        table2.setWidths(width2);
        table2.setTotalWidth(PageSize.A4.getWidth() - 130);
        table2.setSpacingBefore(10f);
        table2.setLockedWidth(true);
        addOneRow4Cell(table2,font3,"序号","材料名称","原件（份）","复印件（份）");

        //表格除了表头以外默认行数
        int defaultRow = 1;
        //添加材料
        addMatListTable(document,defaultRow,matInfoList,table2,font3);

        Paragraph paragraph2 = new Paragraph("请于申请之前起", font3);
        paragraph2.setFirstLineIndent(30);
        paragraph2.setLeading(25f);
        paragraph2.add(getUnderLineChunk(" "+timeLimit+" ",font3));
        paragraph2.add("（不含特殊程序审查期限）后领取结果，具体请登录广东政务服务网（佛山）查询办理状态。");
        document.add(paragraph2);

        //新建表格，4列的表格，领证需要的材料
//        PdfPTable table3 = new PdfPTable(4);
//        table3.setTotalWidth(PageSize.A4.getWidth());
//        int width3[] = {10, 30,20, 20};
//        table3.setWidths(width3);
//        table3.setTotalWidth(PageSize.A4.getWidth() - 130);
//        table3.setSpacingBefore(10f);
//        table3.setLockedWidth(true);
//        addOneRow4Cell(table3,font3,"序号","材料名称","原件（份）","复印件（份）");
//        addOneRow(table3,font3,"1","申请人有效身份证明文件","1","1");
//        addOneRow(table3,font3,"2","授权委托书","1","1");
//        addOneRow(table3,font3,"3","经办人有效身份证明文件","1","1");
//        addOneRow(table3,font3,"4","法人证书复印件","1","1");
//        document.add(table3);

        calculateCreateStampPart(writer,document,printDate,BMYZ);

        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    /**
     * 组装文书前半部分的信息
     * @param receiveBaseVo
     * @return
     */
    private static Map<String,String> buildBasicData(ReceiveBaseVo receiveBaseVo){
        Map<String,String> dataMap = Maps.newHashMap();
        dataMap.put("projectName",receiveBaseVo.getProjName());
        dataMap.put("projectCode",receiveBaseVo.getProjLocalCode());
        dataMap.put("applyName",receiveBaseVo.getApplicant());//单位名称
        dataMap.put("certNo1",receiveBaseVo.getApplicantIDCard());//单位统一社会信用代码
        dataMap.put("phone1",receiveBaseVo.getAgentLinkmanTel());//单位联系电话
        dataMap.put("address",receiveBaseVo.getApplicantDetailSite());//单位地址
        dataMap.put("username",receiveBaseVo.getIdrepresentative());//法人
        dataMap.put("certNo2",receiveBaseVo.getIdno());
        dataMap.put("linkmanName",receiveBaseVo.getAgentLinkmanName());//法人证件
        dataMap.put("linkmanPhone",receiveBaseVo.getAgentLinkmanTel());
        dataMap.put("certNo3",receiveBaseVo.getAgentLinkmanIDCard());
        return dataMap;
    }

    /**
     * 组装材料信息
     * @param receiveBaseVo
     * @param matInfoList
     */
    private static void buildMatList(ReceiveBaseVo receiveBaseVo,List<PdfTableMatInfo> matInfoList){
        List<AeaHiItemMatinst> allMatList = receiveBaseVo.getAllMatList();
        if(allMatList != null && allMatList.size() > 0){
            for(int i=0,len=allMatList.size(); i<len; i++){
                AeaHiItemMatinst temp = allMatList.get(i);
                String index = i+1+"";
                matInfoList.add(new PdfTableMatInfo(index,temp.getMatinstName(),
                        temp.getRealPaperCount()==null?"0":temp.getRealCopyCount().toString(),temp.getRealCopyCount() == null?"0":temp.getRealCopyCount().toString()));//测试材料
            }
        }
    }

    /**
     * 批准决定书
     *
     * @return
     * @throws Exception
     */
    public static String createPiZhunReceipt(ReceiveBaseVo receiveBaseVo) throws Exception {
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(70, 70, 10, 10);
        // 设置pdf生成的路径
        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        //StringBuffer str = new StringBuffer("D:\\Augur\\foshan\\回执模板\\");

        str.append(PI_ZHUN);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        //设置字体格式
        initFontMap();
        Font font3 = fontMap.get("font3");
        /*******************************************************回执填写的参数*******************************************************/
        String seqNo = "序号3-1：";
        String receiveName = PI_ZHUN;
        String instrumentNo = receiveBaseVo.getDocumentNum() == null?"":receiveBaseVo.getDocumentNum();
        String itemName = receiveBaseVo.getItemName();
        String legalProvisions = " 相关法律条款或理由";
        Date applyDate = new Date();
        Date printDate = new Date();
        /*******************************************************回执填写的参数*******************************************************/

        //设置页码
        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(seqNo, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer, font3.getBaseFont(), 14, PageSize.A4, receiveHeaderInfo);
        // 打开文档，只有打开后才能往里面加东西
        document.open();
        //组装展示数据
        Map<String,String> dataMap = buildBasicData(receiveBaseVo);
        createfirstTable(document,font3,dataMap);

        String content = "你单位于 ";
        Paragraph paragraph = new Paragraph(content, font3);
        //缩进两格
        paragraph.setFirstLineIndent(30);
        //日期设置下滑线
        setDateUnderLine(paragraph, font3, applyDate);
        paragraph.add("申请办理 ");
        paragraph.add(getUnderLineChunk(itemName, font3));
        paragraph.add(" ，根据");
        paragraph.add(getUnderLineChunk(legalProvisions, font3));
        paragraph.add("，经审查，符合审批条件，予以批准。");
        document.add(paragraph);
        ReceivePDFUtils.twoParagraph("", document, font3);

        calculateCreateStampPart(writer, document, printDate,BMYZ);

        // 关闭文档，才能输出
        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    /**
     * 不予批准决定书
     *
     * @return
     * @throws Exception
     */
    public static String createBuYuPiZhunReceipt(ReceiveBaseVo receiveBaseVo) throws Exception {
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(70, 70, 10, 10);
        // 设置pdf生成的路径
        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        //StringBuffer str = new StringBuffer("D:\\Augur\\foshan\\回执模板\\");

        str.append(BU_YU_PI_ZHUN);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        //设置字体格式
        initFontMap();
        Font font3 = fontMap.get("font3");
        /*******************************************************回执填写的参数*******************************************************/
        String seqNo = "序号3-2：";
        String receiveName = BU_YU_PI_ZHUN;
        String instrumentNo = receiveBaseVo.getDocumentNum() == null?"":receiveBaseVo.getDocumentNum();
        String itemName = receiveBaseVo.getItemName();
        String legalProvisions = receiveBaseVo.getLegalProvisions();
        String regionName = receiveBaseVo.getReconsiderationRegion();
        String parentOrgName = receiveBaseVo.getSuperiorDepartment();
        Date applyDate = new Date();
        Date printDate = new Date();
        /*******************************************************回执填写的参数*******************************************************/

        //设置页码
        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(seqNo, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer, font3.getBaseFont(), 14, PageSize.A4, receiveHeaderInfo);
        // 打开文档，只有打开后才能往里面加东西
        document.open();
        //组装展示数据
        Map<String,String> dataMap = buildBasicData(receiveBaseVo);
        createfirstTable(document,font3,dataMap);

        String content1 = "你（单位）于 ";
        Paragraph paragraph = new Paragraph(content1, font3);
        //缩进两格
        paragraph.setFirstLineIndent(30);
        //日期设置下滑线
        setDateUnderLine(paragraph, font3, applyDate);
        paragraph.add("申请办理 ");
        paragraph.add(getUnderLineChunk(itemName, font3));
        paragraph.add(" ，根据");
        paragraph.add(getUnderLineChunk(legalProvisions, font3));
        paragraph.add(" ，经审查，不符合审批条件，不予批准。退回材料清单如下：");
        document.add(paragraph);

        //新建表格，4列的表格
        PdfPTable table2 = new PdfPTable(4);
        table2.setTotalWidth(PageSize.A4.getWidth());
        int width2[] = {10, 30, 20, 20};
        table2.setWidths(width2);
        table2.setTotalWidth(PageSize.A4.getWidth() - 130);
        //表格外上间距 设为10
        table2.setSpacingBefore(10f);
        table2.setLockedWidth(true);
        addOneRow4Cell(table2, font3, "序号", "材料名称", "原件（份）", "复印件（份）");

        ReceivePDFUtils.oneLine(document, font3);
        Paragraph dissent = new Paragraph("", font3);
        //缩进两格
        dissent.setFirstLineIndent(30);
        dissent.add("申请人如有异议，可自收到本决定书之日起六十日内向");
        dissent.add(getUnderLineChunk("（" + regionName + "）", font3));
        dissent.add("人民政府复议委员会或");
        dissent.add(getUnderLineChunk("（" + parentOrgName + "）", font3));
        dissent.add("申请行政复议，或者六个月内依法向佛山市顺德区人民法院提起行政诉讼。");
        document.add(dissent);

        calculateCreateStampPart(writer, document, printDate,BMYZ);

        // 关闭文档，才能输出
        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    public static String createJiaoFeiTongzhiReceipt() throws Exception {
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(70, 70, 10, 10);
        // 设置pdf生成的路径
        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        //StringBuffer str = new StringBuffer("D:\\Augur\\foshan\\回执模板\\");

        str.append(JIAO_FEI);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        //设置字体格式
        initFontMap();
        Font font3 = fontMap.get("font3");
        /*******************************************************回执填写的参数*******************************************************/
        String orgName = "佛山市南海区自然资源局";
        String receiveName = JIAO_FEI;
        String instrumentNo = "12345678987654321";
        String applyName = "三水进欣制线有限公司三水进欣制线有限公司";
        String itemName = "建设项目环境影响评价文件审批——报告表申报";
        String payItemName = "xxx";
        String pay = "0.00";
        Date applyDate = new Date();
        Date printDate = new Date();
        String payAdress = "xxx";
        List<Option> options = new ArrayList<>();
        options.add(new Option(false, "现金", "现金"));
        options.add(new Option(false, "银行转账", "银行转账"));
        options.add(new Option(false, "POS机", "POS机"));
        options.add(new Option(true, "网上支付", "网上支付"));
        options.add(new Option(true, "微信缴费", "微信缴费"));
        options.add(new Option(true, "其他________", "其他"));
        /*******************************************************回执填写的参数*******************************************************/

        //设置页码
        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(orgName, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer, font3.getBaseFont(), 14, PageSize.A4, receiveHeaderInfo);
        // 打开文档，只有打开后才能往里面加东西
        document.open();

        //新建表格，4列的表格
        PdfPTable table = new PdfPTable(4);
//        table2.setWidthPercentage(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        int width2[] = {15, 30, 20, 20};
        table.setWidths(width2);
        table.setSpacingAfter(50f);
        table.setTotalWidth(PageSize.A4.getWidth() - 130);
        //表格外上间距 设为10
        table.setSpacingBefore(10f);
        table.setLockedWidth(true);

//        addOneRow4Cell(table,font3,"申请人",applyName,"事项名称",itemName);
//        addOneRow4Cell(table,font3,"收费项目",payItemName,"应交金额",pay);
//        addOneRow4Cell(table,font3,"缴费时间",DateUtils.convertDateToString(applyDate, "yyyy-MM-dd HH:mm:ss"),"缴费地点",payAdress);

        addCellWithPadding(table, font3, "申请人", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, applyName, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "事项名称", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, itemName, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "收费项目", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, payItemName, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "应交金额", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, pay, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "缴费时间", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, DateUtils.convertDateToString(applyDate, "yyyy-MM-dd HH:mm:ss"), Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "缴费地点", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, payAdress, Element.ALIGN_CENTER, false, 0);

        addCellWithPadding(table, font3, "缴费方式", Element.ALIGN_CENTER, false, 0);

        Paragraph optionParagraph = new Paragraph();
        for (int i = 0; i < options.size(); i++) {
            Option option = options.get(i);
            optionParagraph.add(new Chunk(option.getSelect(), fontMap.get("font5")));
            optionParagraph.add(new Chunk(option.getContent(), font3));
            optionParagraph.add(new Chunk("  ", font3));
        }

        addCellWithPadding(table, optionParagraph, Element.ALIGN_LEFT, false, 3);

        addCellWithPadding(table, font3, "减免情形", Element.ALIGN_CENTER, false, 0);
        addCellWithPaddingAndHeight(table, font3, "", Element.ALIGN_CENTER, false, 3, 200);
        document.add(table);

        calculateCreateStampPart(writer, document, printDate,BMYZ);

        // 关闭文档，才能输出
        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    public static String createSongDaReceipt() throws Exception {
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(70, 70, 10, 10);
        // 设置pdf生成的路径
        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        //StringBuffer str = new StringBuffer("D:\\Augur\\foshan\\回执模板\\");

        str.append(SONG_DA);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        //设置字体格式
        initFontMap();
        Font font1 = fontMap.get("font1");
        Font font3 = fontMap.get("font3");
        /*******************************************************回执填写的参数*******************************************************/
        String orgName = "佛山市南海区自然资源局";
        String receiveName = SONG_DA;
        String instrumentNo = "12345678987654321";
        String applyName = "三水进欣制线有限公司三水进欣制线有限公司";
        String itemName = "建设项目环境影响评价文件审批——报告表申报";
        String phone = "13512121212";
        String address = "xxxxxxxx";
        List<PdfServiceDocInfo> docsInfoList = new ArrayList<>();
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        docsInfoList.add(new PdfServiceDocInfo("xxxx", "123213123", "1"));
        List<PdfTableMatInfo> matInfoList = new ArrayList<>();
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        List<Option> options = new ArrayList<>();
        options.add(new Option(true, "窗口送达", "窗口送达"));
        options.add(new Option(true, "邮政送达", "邮政送达"));
        options.add(new Option(true, "自助收件", "自助收件"));

        Date applyDate = new Date();
        Date printDate = new Date();
        /*******************************************************回执填写的参数*******************************************************/

        //设置页码
        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(orgName, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer, font3.getBaseFont(), 14, PageSize.A4, receiveHeaderInfo);
        // 打开文档，只有打开后才能往里面加东西
        document.open();

        //新建表格，4列的表格
        PdfPTable table1 = new PdfPTable(4);
        table1.setTotalWidth(PageSize.A4.getWidth());
        int width2[] = {20, 30, 20, 20};
        table1.setWidths(width2);
        table1.setTotalWidth(PageSize.A4.getWidth() - 130);
        table1.setLockedWidth(true);

        addCellWithPadding(table1, font1, "基本信息", Element.ALIGN_CENTER, false, 4);
        addCellWithPadding(table1, font3, "受送达人", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, applyName, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, "事项名称", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, itemName, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, "联系电话", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, phone, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, "送达方式", Element.ALIGN_CENTER, false, 0);

        Paragraph optionParagraph = new Paragraph();
        for (int i = 0; i < options.size(); i++) {
            Option option = options.get(i);
            optionParagraph.add(new Chunk(option.getSelect(), fontMap.get("font5")));
            optionParagraph.add(new Chunk(option.getContent(), font3));
            optionParagraph.add(new Chunk("\n", font3));
        }

        addCellWithPadding(table1, optionParagraph, Element.ALIGN_CENTER, false, 3);
        addCellWithPadding(table1, font3, "送达地址", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, address, Element.ALIGN_CENTER, false, 3);
//        document.add(table1);

//        //新建表格，4列的表格
//        PdfPTable table2 = new PdfPTable(3);
//        table2.setTotalWidth(PageSize.A4.getWidth());
//        int width3[] = {20,20,20};
//        table2.setWidths(width3);
//        table2.setTotalWidth(PageSize.A4.getWidth() - 130);
//        table2.setLockedWidth(true);
        addCellWithPadding(table1, font1, "送达文书清单", Element.ALIGN_CENTER, false, 4);
        addCellWithPadding(table1, font3, "送达文书名称", Element.ALIGN_CENTER, false, 2);
        addCellWithPadding(table1, font3, "文书编号", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, "份数", Element.ALIGN_CENTER, false, 0);
        for (PdfServiceDocInfo docInfo : docsInfoList) {
            addCellWithPadding(table1, font3, docInfo.getDocName(), Element.ALIGN_CENTER, false, 2);
            addCellWithPadding(table1, font3, docInfo.getDocNo(), Element.ALIGN_CENTER, false, 0);
            addCellWithPadding(table1, font3, docInfo.getPaperCount(), Element.ALIGN_CENTER, false, 0);
        }
//        document.add(table2);

//        //新建表格，4列的表格
//        PdfPTable table3 = new PdfPTable(4);
//        table3.setTotalWidth(PageSize.A4.getWidth());
//        table3.setWidths(width2);
//        table3.setTotalWidth(PageSize.A4.getWidth() - 130);
//        table3.setLockedWidth(true);
        addCellWithPadding(table1, font1, "送达情况", Element.ALIGN_CENTER, false, 4);
        addCellWithPadding(table1, font3, "□受送达人\n签名", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, "", Element.ALIGN_CENTER, false, 3);
        addCellWithPadding(table1, font3, "□代收人\n签名", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, "", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, "与申请人\n关系", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table1, font3, "", Element.ALIGN_CENTER, false, 0);
        addCellWithPaddingAndHeight(table1, font3, "□送达回证", Element.ALIGN_CENTER, false, 0, 50);
        addCellWithPaddingAndHeight(table1, font3, "（粘贴送达回证）", Element.ALIGN_CENTER, false, 3, 80);
        addCellWithPaddingAndHeight(table1, font3, "送达时间", Element.ALIGN_CENTER, false, 0, 80);
        addCellWithPaddingAndHeight(table1, font3, "", Element.ALIGN_CENTER, false, 3, 60);

        document.add(table1);

        calculateCreateStampPart(writer, document, printDate,BMYZ);

        // 关闭文档，才能输出
        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    public static String createWuLiaoLiuZhuangPinZheng() throws Exception {
        // 创建一个文档（默认大小A4，边距36, 36, 36, 36）
        Document document = new Document();
        // 设置文档大小
        document.setPageSize(PageSize.A4);
        // 设置边距，单位都是像素，换算大约1厘米=28.33像素
        document.setMargins(70, 70, 10, 10);
        // 设置pdf生成的路径
        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        //StringBuffer str = new StringBuffer("D:\\Augur\\foshan\\回执模板\\");

        str.append(WU_LIAO_LIU_ZHUANG);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        // 创建writer，通过writer将文档写入磁盘
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        //设置字体格式
        initFontMap();
        Font font1 = fontMap.get("font1");
        Font font3 = fontMap.get("font3");
        /*******************************************************回执填写的参数*******************************************************/
        String orgName = "佛山市南海区自然资源局";
        String receiveName = WU_LIAO_LIU_ZHUANG;
        String instrumentNo = "12345678987654321";
        String applyName = "三水进欣制线有限公司三水进欣制线有限公司";
        String itemName = "建设项目环境影响评价文件审批——报告表申报";
        String nodeName = "环节";
        String flow = "流转方式";
        String expressCompany = "快递公司";
        String expressNo = "快递单号";

        String sendUnit = "发件单位";
        String sendPerson = "经办人";
        String sendPhone = "13512121212";

        String receiveUnit = "收件单位";
        String receivePerson = "经办人";
        String receivePhone = "13512121212";

        List<PdfTableMatInfo> matInfoList = new ArrayList<>();
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料
        matInfoList.add(new PdfTableMatInfo("1", "测试材料", "1", "1"));//测试材料

        Date applyDate = new Date();
        Date printDate = new Date();
        /*******************************************************回执填写的参数*******************************************************/

        //设置页码
        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(orgName, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer, font3.getBaseFont(), 14, PageSize.A4, receiveHeaderInfo);
        // 打开文档，只有打开后才能往里面加东西
        document.open();

        //新建表格，4列的表格
        PdfPTable table = new PdfPTable(4);
        table.setTotalWidth(PageSize.A4.getWidth());
        int width2[] = {20, 30, 20, 25};
        table.setWidths(width2);
        table.setTotalWidth(PageSize.A4.getWidth() - 130);
        table.setLockedWidth(true);

        addCellWithPadding(table, font3, "事项名称", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, itemName, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "申请人", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, applyName, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "所在环节", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, nodeName, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "流转方式", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, flow, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "快递公司", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, expressCompany, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "快递单号", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, expressNo, Element.ALIGN_CENTER, false, 0);

        addCellWithPadding(table, font1, "物料收发单位信息", Element.ALIGN_CENTER, false, 4);
        addCellWithPadding(table, font3, "发件单位", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "经办人", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "联系电话", Element.ALIGN_CENTER, false, 2);
        addCellWithPadding(table, font3, sendUnit, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, sendPerson, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, sendPhone, Element.ALIGN_CENTER, false, 2);

        addCellWithPadding(table, font3, "收件单位", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "经办人", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "联系电话", Element.ALIGN_CENTER, false, 2);
        addCellWithPadding(table, font3, receiveUnit, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, receivePerson, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, receivePhone, Element.ALIGN_CENTER, false, 2);

        addCellWithPadding(table, font1, "物料清单", Element.ALIGN_CENTER, false, 4);
        addCellWithPadding(table, font3, "序号", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "材料名称", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "原件（份）", Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font3, "复印件（份）", Element.ALIGN_CENTER, false, 0);

        for (PdfTableMatInfo matInfo : matInfoList) {
            addCellWithPadding(table, font3, matInfo.getNumber(), Element.ALIGN_CENTER, false, 0);
            addCellWithPadding(table, font3, matInfo.getMatName(), Element.ALIGN_CENTER, false, 0);
            addCellWithPadding(table, font3, matInfo.getPaperCount(), Element.ALIGN_CENTER, false, 0);
            addCellWithPadding(table, font3, matInfo.getCopyCount(), Element.ALIGN_CENTER, false, 0);
        }
        document.add(table);

        calculateWuliaoCreateStampPart(writer, document, printDate);

        // 关闭文档，才能输出
        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    /**
     * 申请材料补正告知书
     * @return
     * @throws Exception
     */
    public static String createBuZhengReceipt(ReceiveBaseVo receiveBaseVo)throws Exception{
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(70, 70, 10, 10);
        StringBuffer str = ReceivePDFTemplate.pdfFilePath();

        str.append(BU_ZHENG);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        initFontMap();
        Font font3 = fontMap.get("font3");
        /*******************************************************回执填写的参数*******************************************************/
        String seqNo = "序号2-3： ";
        String receiveName = BU_ZHENG;
        String instrumentNo = receiveBaseVo.getDocumentNum() == null?"":receiveBaseVo.getDocumentNum();
        String itemName = receiveBaseVo.getItemName();
        String legalProvisions = receiveBaseVo.getLegalProvisions();
        String timeLimit = (receiveBaseVo.getDueNum() != null ? receiveBaseVo.getDueNum().intValue() : "-") + "个工作日";
        Date applyDate = new Date();
        Date printDate = new Date();
        List<PdfTableMatInfo> matInfoList = new ArrayList<>();
        /*******************************************************回执填写的参数*******************************************************/

        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(seqNo, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer,font3.getBaseFont(),14,PageSize.A4,receiveHeaderInfo);
        document.open();
        //组装数据
        Map<String,String> dataMap = buildBasicData(receiveBaseVo);
        createfirstTable(document,font3,dataMap);

        String content1 = "你单位于 ";
        Paragraph paragraph = new Paragraph(content1, font3);
        paragraph.setFirstLineIndent(30);
        paragraph.setLeading(25f);
        setDateUnderLine(paragraph,font3,applyDate);
        paragraph.add("申请办理 ");
        paragraph.add(getUnderLineChunk(itemName,font3));
        paragraph.add(" ，根据");
        paragraph.add(getUnderLineChunk(legalProvisions,font3));
        paragraph.add("，需补正的申请材料如下：");
        document.add(paragraph);

        //新建表格，5列的表格
        PdfPTable table2 = new PdfPTable(3);
        table2.setTotalWidth(PageSize.A4.getWidth());
        int width2[] = {10, 40, 40};
        table2.setWidths(width2);
        table2.setTotalWidth(PageSize.A4.getWidth() - 130);
        table2.setSpacingBefore(20f);
        table2.setLockedWidth(true);
        addOneRow(table2,font3,"序号","材料名称","材料补正意见");
        //表格除了表头以外默认行数
        int defaultRow = 3;
        //添加材料
        int matCount = matInfoList.size();
        if(matCount == 0){
            for(int k=0;k<defaultRow;k++){
                addEmptyRow(table2,font3,3);
            }
        }
        for(int i=0;i<matCount;i++){
            PdfTableMatInfo matInfo = matInfoList.get(i);
            addOneRow(table2,font3,matInfo.getNumber(),matInfo.getMatName(),matInfo.getPaperCount(),matInfo.getCopyCount(),matInfo.getCorrectionOpinion());
            if(i == matCount-1 && matCount < defaultRow){
                for(int k=0;k<defaultRow-matCount;k++){
                    addEmptyRow(table2,font3,3);
                }
            }
        }
        document.add(table2);
        Paragraph paragraph2 = new Paragraph("请收到本告知书之日起", font3);
        paragraph2.setFirstLineIndent(30);
        paragraph2.setLeading(25f);
        paragraph2.add(getUnderLineChunk(" "+timeLimit+" ",font3));
        paragraph2.add("内提交补正材料，逾期未补正的，作退件处理。");
        document.add(paragraph2);
        ReceivePDFUtils.oneLine(document, font3);
        Paragraph paragraph3 = new Paragraph("咨询联系人：", font3);
        paragraph3.setFirstLineIndent(30);
        paragraph3.add(getUnderLineChunk(" "+receiveBaseVo.getConsultLinkmanName()+" ",font3));
        paragraph3.add("；咨询电话：");
        paragraph3.add(getUnderLineChunk(" "+receiveBaseVo.getConsultLinkmanTel()+" ",font3));
        paragraph3.add("。");
        document.add(paragraph3);
        calculateCreateStampPart(writer,document,printDate,BMYZ);

        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    /**
     * 不予受理通知书
     * @return
     * @throws Exception
     */
    public static String createBuYuShouLiReceipt(ReceiveBaseVo receiveBaseVo)throws Exception{
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(70, 70, 10, 10);
        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        str.append(BU_YU_SHOU_LI);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);
        initFontMap();
        Font font3 = fontMap.get("font3");
        Font font5 = fontMap.get("font5");
        /*******************************************************回执填写的参数*******************************************************/
        String seqNo = "序号2-2： ";
        String receiveName = BU_YU_SHOU_LI;
        String instrumentNo = receiveBaseVo.getDocumentNum() == null?"":receiveBaseVo.getDocumentNum();
        String itemName = receiveBaseVo.getItemName();
        String legalProvisions = receiveBaseVo.getLegalProvisions();
        //构建不予受理的理由选项
        Map<String,Option> optionMap = new HashMap<>();
        Option op1 = new Option(false, null, "其他部门");
        Option op2 = new Option(false, null, null);
        Option op3 = new Option(false, null, null);
        Option op4 = new Option(false, null, null);
        Option op5 = new Option(false, null, "就不予受理，咋的");
        optionMap.put("option1",op1);
        optionMap.put("option2",op2);
        optionMap.put("option3",op3);
        optionMap.put("option4",op4);
        optionMap.put("option5",op5);
        Date applyDate = new Date();
        String regionName = receiveBaseVo.getReconsiderationRegion();
        String parentOrgName = receiveBaseVo.getSuperiorDepartment();
        Date printDate = new Date();
        /*******************************************************回执填写的参数*******************************************************/

        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(seqNo, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer,font3.getBaseFont(),14,PageSize.A4,receiveHeaderInfo);
        document.open();
        //组装数据
        Map<String,String> dataMap = buildBasicData(receiveBaseVo);
        createfirstTable(document,font3,dataMap);

        String content1 = "你单位于 ";
        Paragraph paragraph = new Paragraph(content1, font3);
        paragraph.setFirstLineIndent(30);
        paragraph.setLeading(25f);
        setDateUnderLine(paragraph,font3,applyDate);
        paragraph.add("申请办理 ");
        paragraph.add(getUnderLineChunk(itemName,font3));
        paragraph.add(" ，根据");
        paragraph.add(getUnderLineChunk(legalProvisions,font3));
        paragraph.add("，不符合受理条件，不予受理。");
        document.add(paragraph);

        ReceivePDFUtils.twoSpacing("具体理由如下（打“√”部分）：",document,font3,25f);
        String select = "☑";
        String noSelect = "□";

        Option option1 = optionMap.get("option1");
        Paragraph o1 = new Paragraph("", font3);
        o1.setLeading(25f);
        String param1 = option1.isSelect?" "+option1.getParam()+" ":"               ";
        Chunk chunk1 = new Chunk(param1,font3).setUnderline(0.5f,-3f);
        o1.setIndentationLeft(30);
        if(option1 != null && option1.isSelect){
            o1.add(new Chunk(select, font5));
        }else{
            o1.add(noSelect);
        }
        o1.add("1.申请不属于本事项受理范围，应向");
        o1.add(chunk1);
        o1.add("提出申请。");
        document.add(o1);

        Option option2 = optionMap.get("option2");
        createOption(document,option2,font3,"2.经告知补正材料后，未及时提交申请材料。");
        Option option3 = optionMap.get("option3");
        createOption(document,option3,font3,"3.补正材料后，申请材料仍不符合要求。");
        Option option4 = optionMap.get("option4");
        createOption(document,option4,font3,"4.申请人不符合申请资格。");
        Option option5 = optionMap.get("option5");
        Paragraph o5 = new Paragraph("", font3);
        String param5 = option5.isSelect?" "+option5.getParam()+" ":"                                             ";
        Chunk chunk5 = new Chunk(param5,font3).setUnderline(0.5f,-3f);
        o5.setIndentationLeft(30);
        o5.setLeading(25f);
        if(option5 != null && option5.isSelect){
            o5.add(new Chunk(select, font5));
        }else{
            o5.add(noSelect);
        }
        o5.add("5.");
        o5.add(chunk5);
        document.add(o5);

//        ReceivePDFUtils.twoSpacing("退回材料清单如下：",document,font3,25f);
//        //新建表格，4列的表格
//        PdfPTable table2 = new PdfPTable(4);
//        table2.setTotalWidth(PageSize.A4.getWidth());
//        int width2[] = {10, 30,20, 20};
//        table2.setWidths(width2);
//        table2.setTotalWidth(PageSize.A4.getWidth() - 130);
//        table2.setSpacingBefore(10f);
//        table2.setLockedWidth(true);
//        addOneRow4Cell(table2,font3,"序号","材料名称","原件（份）","复印件（份）");
//        //表格除了表头以外默认行数
//        int defaultRow = 6;
//        //添加材料
//        addMatListTable(document,defaultRow,matInfoList,table2,font3);
        //暂时加上几行空行，等确定需求了再看是否要去掉
        ReceivePDFUtils.oneLine(document,font3);

        String content = "申请人如有异议，可自收到本通知书之日起六十日内向";
        Paragraph paragraph2 = new Paragraph(content, font3);
        paragraph2.setFirstLineIndent(30);
        paragraph2.setLeading(25f);
        paragraph2.add(new Chunk(regionName,font3).setUnderline(0.5f,-3f));
        paragraph2.add("人民政府复议委员会或");
        paragraph2.add(new Chunk(parentOrgName,font3).setUnderline(0.5f,-3f));
        paragraph2.add("申请行政复议，或者六个月内依法向佛山市顺德区人民法院提起行政诉讼。");
        document.add(paragraph2);
        calculateCreateStampPart(writer,document,printDate,BMYZ);

        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    /**
     * 特殊程序审查期限告知书
     * @return
     * @throws Exception
     */
    public static String createTeShuReceipt(ReceiveBaseVo receiveBaseVo)throws Exception{
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(70, 70, 10, 10);
        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        str.append(TE_SHU);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        initFontMap();
        Font font3 = fontMap.get("font3");
        /*******************************************************回执填写的参数*******************************************************/
        String seqNo = "序号8-1：";
        String receiveName = TE_SHU;
        String instrumentNo = receiveBaseVo.getDocumentNum() == null?"":receiveBaseVo.getDocumentNum();
        String itemName = receiveBaseVo.getItemName();
        String legalProvisions = receiveBaseVo.getLegalProvisions();
        String linkmanName = receiveBaseVo.getConsultLinkmanName();
        String linkmanPhone = receiveBaseVo.getConsultLinkmanTel();
        String specialName = receiveBaseVo.getSpecialProcessName();
        String readyConetent = receiveBaseVo.getReadyConetent() == null?"                     ":receiveBaseVo.getReadyConetent();
        Date applyDate = new Date();
        Date startDate = new Date();
        Date printDate = new Date();
        /*******************************************************回执填写的参数*******************************************************/

        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(seqNo, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer,font3.getBaseFont(),14,PageSize.A4,receiveHeaderInfo);
        document.open();

        String content1 = "本单位于";
        Paragraph paragraph = new Paragraph(content1, font3);
        paragraph.setFirstLineIndent(30);
        paragraph.setLeading(25f);
        setDateUnderLine(paragraph,font3,applyDate);
        paragraph.add("受理你单位的");
        paragraph.add(getUnderLineChunk(itemName,font3));
        paragraph.add("申请。根据");
        paragraph.add(getUnderLineChunk(legalProvisions,font3));
        paragraph.add("将于");
        setDateUnderLine(paragraph,font3,startDate);
        paragraph.add("开展");
        paragraph.add(getUnderLineChunk(specialName,font3));
        paragraph.add("，请提前做好准备工作，");
        paragraph.add(getUnderLineChunk(readyConetent,font3));
        paragraph.add("。");
        document.add(paragraph);
        ReceivePDFUtils.twoSpacing("特此告知。",document,font3,25f);

        ReceivePDFUtils.oneLine(document, font3);
        ReceivePDFUtils.oneLine(document, font3);
        Paragraph paragraph3 = new Paragraph("咨询联系人：", font3);
        paragraph3.setFirstLineIndent(30);
        paragraph3.add(getUnderLineChunk(" "+linkmanName+" ",font3));
        paragraph3.add("；咨询电话：");
        paragraph3.add(getUnderLineChunk(" "+linkmanPhone+" ",font3));
        paragraph3.add("。");
        document.add(paragraph3);
        calculateCreateStampPart(writer,document,printDate,BMYZ);

        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    /**
     * 延长办理期限通知书
     * @return
     * @throws Exception
     */
    public static String createYanChangReceipt()throws Exception{
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(70, 70, 10, 10);
        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        str.append(YAN_CHANG);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        initFontMap();
        Font font3 = fontMap.get("font3");
        /*******************************************************回执填写的参数*******************************************************/
        String orgName = "佛山市南海区自然资源局";
        String receiveName = YAN_CHANG;
        String instrumentNo = "12345678987654321";
        String itemName = "属地公共区域，一、二级风险等级或者投资30万元以上的安全技术防范系统竣工验收(蓬江区)";
        String reason = "不想那么早办理";
        String standardTimeLimit = "3个工作日";
        String afterTimeLimit = "5个工作日";
        Date acceptDate = new Date();
        Date aproveDate = new Date();
        Date printDate = new Date();
        List<PdfTableMatInfo> matInfoList = new ArrayList<>();
        /*******************************************************回执填写的参数*******************************************************/

        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(orgName, receiveName, instrumentNo);
        ItextPdfPagination.setFooter(writer,font3.getBaseFont(),14,PageSize.A4,receiveHeaderInfo);
        document.open();

        String content1 = "本单位于";
        Paragraph paragraph = new Paragraph(content1, font3);
        paragraph.setFirstLineIndent(30);
        paragraph.setLeading(25f);
        setDateUnderLine(paragraph,font3,acceptDate);
        paragraph.add("受理你（单位）的");
        paragraph.add(getUnderLineChunk(itemName,font3));
        paragraph.add("申请。因");
        paragraph.add(getUnderLineChunk(reason,font3));
        paragraph.add("，在原承诺时限");
        paragraph.add(getUnderLineChunk(standardTimeLimit,font3));
        paragraph.add("的基础上延长");
        paragraph.add(getUnderLineChunk(afterTimeLimit,font3));
        paragraph.add("，将于");
        setDateUnderLine(paragraph,font3,aproveDate);
        paragraph.add("前作出决定。");
        document.add(paragraph);
        ReceivePDFUtils.twoSpacing("特此通知。",document,font3,25f);

        calculateCreateStampPart(writer,document,printDate,BMYZ);

        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }

    /**
     * 联合验收终审意见书
     * @param receiveBaseVo
     * @return
     * @throws Exception
     */
    public static String createLhysZhongshenYjsReceipt(ReceiveBaseVo receiveBaseVo)throws Exception{
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(70, 70, 10, 10);
        StringBuffer str = ReceivePDFTemplate.pdfFilePath();
        str.append(LHYS_ZHONGSHEN_YJS);
//        str.append((int) ((Math.random() * 9 + 1) * 1000));
        str.append(".pdf");
        FileOutputStream fileOutputStream = new FileOutputStream(str.toString());
        PdfWriter writer = PdfWriter.getInstance(document, fileOutputStream);

        initFontMap();
        Font font3 = fontMap.get("font3");
        Font font4 = fontMap.get("font4");
        /*******************************************************回执填写的参数*******************************************************/
        String seqNo = "序号8-6：";
        String receiveName = LHYS_ZHONGSHEN_YJS;
        String instrumentNo = receiveBaseVo.getDocumentNum() == null?"":receiveBaseVo.getDocumentNum();
        String region = "（佛山市/" + receiveBaseVo.getLhpsYjsRegion() + ")";
        Map<String, String> deptOpinions = null;
        ProjAcceptOpinionSummaryVo projAcceptOpinionSummaryVo = receiveBaseVo.getProjAcceptOpinionSummaryVo();
        if(projAcceptOpinionSummaryVo != null){
            deptOpinions = receiveBaseVo.getProjAcceptOpinionSummaryVo().getDeptOpinions();
        }
        Date printDate = new Date();
        /*******************************************************回执填写的参数*******************************************************/

        ItextPdfPagination.ReceiveHeaderInfo receiveHeaderInfo = ItextPdfPagination.getReceiveHeaderInfo(seqNo, receiveName, instrumentNo,region);
        ItextPdfPagination.setFooter(writer,font3.getBaseFont(),14,PageSize.A4,receiveHeaderInfo);
        document.open();

        //新建表格，5列的表格
        PdfPTable table1 = new PdfPTable(6);
        table1.setTotalWidth(PageSize.A4.getWidth());
        int width2[] = {30, 10,15,15,15,20};
        table1.setWidths(width2);
        table1.setTotalWidth(PageSize.A4.getWidth() - 130);
        table1.setSpacingBefore(20f);
        table1.setLockedWidth(true);

        addCell(table1,font3,"项目统一代码",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getCentralCode(),Element.ALIGN_LEFT,false,2,30);
        addCell(table1,font3,"联合验收日期",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,formatDate(projAcceptOpinionSummaryVo.getCheckTime()),Element.ALIGN_LEFT,false,2,30);
        addCell(table1,font3,"项目名称",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getProjName(),Element.ALIGN_LEFT,false,5,30);
        addCell(table1,font3,"工程地址",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getProjAddr(),Element.ALIGN_LEFT,false,5,30);
        addCell(table1,font3,"建设单位",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getBuildUnitName(),Element.ALIGN_LEFT,false,5,30);
        addCell(table1,font3,"施工单位",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getShigongUnitName(),Element.ALIGN_LEFT,false,5,30);
        addCell(table1,font3,"监理单位",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getJianliUnitName(),Element.ALIGN_LEFT,false,5,30);
        addCell(table1,font3,"勘察单位",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getKanchaUnitName(),Element.ALIGN_LEFT,false,5,30);
        addCell(table1,font3,"设计单位",Element.ALIGN_LEFT,false,1,5);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getDesignUnitName(),Element.ALIGN_LEFT,false,5,30);
        addCell(table1,font3,"建筑面积（m2）",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getBuildArea(),Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,"层    数\r\n（层）",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getAboveFloor(),Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,"是否重大项目",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getImportantProj(),Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,"联系人",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getLinkman(),Element.ALIGN_LEFT,false,2,30);
        addCell(table1,font3,"联系电话",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,projAcceptOpinionSummaryVo.getLinkmanPhone(),Element.ALIGN_LEFT,false,2,30);
        addCell(table1,font3,"验收（监督）部门名称",Element.ALIGN_LEFT,false,1,30);
        addCell(table1,font3,"意见",Element.ALIGN_CENTER,false,5,30);
        if(deptOpinions != null && deptOpinions.size() > 0){
            for (String key : deptOpinions.keySet()) {
                addCell(table1,font3,key,Element.ALIGN_LEFT,false,1);
                addCell(table1,font3,deptOpinions.get(key),Element.ALIGN_CENTER,false,5,50);
            }
        }

        document.add(table1);

        float currentHeight = currentHeight(writer);
        float pageHeight = document.getPageSize().getHeight();
        float remanentHeight = pageHeight - currentHeight;
        while (remanentHeight > 50){
            remanentHeight = remanentHeight - 25;
            ReceivePDFUtils.oneLine(document,font3);
        }
        ReceivePDFUtils.paragrahLeft(document, "备注：此文书需上传到佛山市工程建设项目审批服务平台。",font4);
        ReceivePDFUtils.drawLine(document,1f,100,BaseColor.BLACK,Element.ALIGN_CENTER,-5f);
//        calculateCreateStampPart(writer,document,printDate,BMYZ);

        document.close();
        writer.flush();
        writer.close();
        System.out.println(str.toString());
        return str.toString();
    }


    /**
     * 创建不予受理回执的不予受理选项
     * @param document
     * @param option
     * @param font
     * @param content
     * @throws Exception
     */
    public static void createOption(Document document,Option option,Font font,String content)throws Exception{
        String select = "☑";
        String noSelect = "□";
        Paragraph paragraph = new Paragraph("", font);
        paragraph.setLeading(25f);
        paragraph.setIndentationLeft(30);
        if(option != null && option.isSelect){
            paragraph.add(new Chunk(select,fontMap.get("font5")));
        }else{
            paragraph.add(noSelect);
        }
        paragraph.add(content);
        document.add(paragraph);
    }

    /**
     * 创建接收回执、受理回执、不予受理通知书的第一个表格（无边框）
     * @param document
     * @param font
     * @param dataMap
     * @throws Exception
     */
    public static void createfirstTable(Document document,Font font,Map<String,String> dataMap) throws Exception{
        //新建表格，4列的表格
        PdfPTable table1 = new PdfPTable(6);
        // 设置表格的宽度
        int width[] = {45,25,20,45,30,25};
        table1.setWidths(width);
        table1.setTotalWidth(PageSize.A4.getWidth() - 130);
        // 锁住宽度
        table1.setLockedWidth(true);
        // 构建每个单元格
        addCell(table1,font,"申请单位名称：",Element.ALIGN_LEFT,true,1);
        addCell(table1,font,dataMap.get("applyName"),Element.ALIGN_LEFT,true,5);
        addCell(table1,font,"统一社会信用代码/组织机构代码/营业执照注册号：",Element.ALIGN_LEFT,true,4);
        addCell(table1,font,dataMap.get("certNo1"),Element.ALIGN_LEFT,true,2);
        addCell(table1,font,"申请项目名称：",Element.ALIGN_LEFT,true,1);
        addCell(table1,font,dataMap.get("projectName"),Element.ALIGN_LEFT,true,2);
        addCell(table1,font,"统一项目代码：",Element.ALIGN_LEFT,true,1);
        addCell(table1,font,dataMap.get("projectCode"),Element.ALIGN_LEFT,true,2);
        addCell(table1,font,"单位联系电话：",Element.ALIGN_LEFT,true,1);
        addCell(table1,font,dataMap.get("phone1"),Element.ALIGN_LEFT,true,5);
        addCell(table1,font,"单位通讯地址：",Element.ALIGN_LEFT,true,1);
        addCell(table1,font,dataMap.get("address"),Element.ALIGN_LEFT,true,5);
        document.add(table1);

        PdfPTable table2 = new PdfPTable(6);
        // 设置表格的宽度
        int width2[] = {62,20,30,24,30,24};
        table2.setWidths(width2);
        table2.setTotalWidth(PageSize.A4.getWidth() - 130);
        // 锁住宽度
        table2.setLockedWidth(true);
        addCell(table2,font,"法定代表人姓名：",Element.ALIGN_LEFT,true,1);
        addCell(table2,font,dataMap.get("username"),Element.ALIGN_LEFT,true,1);
        addCell(table2,font,"证件号码：",Element.ALIGN_LEFT,true,1);
        addCell(table2,font,dataMap.get("certNo2"),Element.ALIGN_LEFT,true,3);
        addCell(table2,font,"受委托人/联系人姓名：",Element.ALIGN_LEFT,true,1);
        addCell(table2,font,dataMap.get("linkmanName"),Element.ALIGN_LEFT,true,1);
        addCell(table2,font,"证件号码：",Element.ALIGN_LEFT,true,1);
        addCell(table2,font,dataMap.get("certNo3"),Element.ALIGN_LEFT,true,1);
        addCell(table2,font,"联系电话：",Element.ALIGN_LEFT,true,1);
        addCell(table2,font,dataMap.get("linkmanPhone"),Element.ALIGN_LEFT,true,1);
        document.add(table2);

        ReceivePDFUtils.oneLine(document,font);
    }

    /**
     * 添加材料清单列表
     * @param document
     * @param defaultRow
     * @param matInfoList
     * @param table
     * @param font
     * @throws Exception
     */
    public static void addMatListTable(Document document,int defaultRow,List<PdfTableMatInfo> matInfoList,PdfPTable table,Font font)throws Exception{
        int matCount = matInfoList.size();
        if(matCount == 0){
            for(int k=0;k<defaultRow;k++){
                addEmptyRow(table,font,4);
            }
        }
        for(int i=0;i<matCount;i++){
            PdfTableMatInfo matInfo = matInfoList.get(i);
            addOneRow(table,font,matInfo.getNumber(),matInfo.getMatName(),matInfo.getPaperCount(),matInfo.getCopyCount());
            //最后一个材料添加完，判断是否要添加表格空行
            if(i == matCount-1 && matCount < defaultRow){
                for(int k=0;k<defaultRow-matCount;k++){
                    addEmptyRow(table,font,4);
                }
            }
        }
        document.add(table);
    }
    /**
     * 计算生成盖章部分
     * @param writer
     * @param document
     * @param printDate
     * @param text 印章部分显示的字，默认不传则显示印章两个字
     * @throws Exception
     */
    public static void calculateCreateStampPart (PdfWriter writer,Document document,Date printDate,String text) throws Exception{
        //尾部需预留盖章部分的高度 200 头部预留 126
        float currentHeight = currentHeight(writer);
        float pageHeight = document.getPageSize().getHeight();
        float remanentHeight = document.getPageSize().getHeight() - currentHeight;
        float spacing = remanentHeight < 200? pageHeight - 126 - 200:remanentHeight - 200;
        if(remanentHeight < 200){
            //新开一页盖章
            document.newPage();
        }
        FsReceivePDFTemplate.setTail(document,printDate,spacing,text);
    }

    /**
     * 计算生成物料流转凭证盖章部分
     *
     * @param writer
     * @param document
     * @param printDate
     * @throws Exception
     */
    public static void calculateWuliaoCreateStampPart(PdfWriter writer, Document document, Date printDate) throws Exception {
        //尾部需预留盖章部分的高度 200 头部预留 126
        float currentHeight = currentHeight(writer);
        float pageHeight = document.getPageSize().getHeight();
        float remanentHeight = document.getPageSize().getHeight() - currentHeight;
        float spacing = remanentHeight < 200 ? pageHeight - 126 - 200 : remanentHeight - 200;
        if (remanentHeight < 200) {
            //新开一页盖章
            document.newPage();
        }
        FsReceivePDFTemplate.setWuliaoTail(document, printDate, spacing);
    }

    /**
     * 设置文档最后盖章部分
     * @param document
     * @param date
     * @param spacing
     * @param text 印章部分显示的字，默认不传则显示印章两个字
     * @throws Exception
     */
    public static void setTail(Document document, Date date, float spacing,String text)throws Exception{
        Font font3 = fontMap.get("font3");
        Font font4 = fontMap.get("font4");
        String yzText = "（印章）";
        if(StringUtils.isNotBlank(text)){
            yzText = text;
        }
        Paragraph paragraph = new Paragraph(yzText, font3);
        paragraph.setSpacingBefore(spacing);
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(paragraph);
        //设置日期下划线
        Paragraph paragraph2 = new Paragraph(" ", font3);
        paragraph2.setAlignment(Element.ALIGN_RIGHT);
        setDateUnderLine(paragraph2,font3,date);
        document.add(paragraph2);
        ReceivePDFUtils.oneLine(document,font3);
        ReceivePDFUtils.paragrahLeft(document, "备注：此文书即时送达申请人。",font4);
        ReceivePDFUtils.drawLine(document,1f,100,BaseColor.BLACK,Element.ALIGN_CENTER,-5f);
        //新建表格，4列的表格
        PdfPTable table3 = new PdfPTable(4);
        table3.setTotalWidth(PageSize.A4.getWidth());
        int width3[] = {35, 18,26, 21};
        table3.setWidths(width3);
        table3.setTotalWidth(PageSize.A4.getWidth() - 130);
        table3.setSpacingBefore(15f);
        table3.setLockedWidth(true);
        addCell(table3,font4,"微信扫描二维码查询办理进度。\r\n咨 询 及 投 诉 电 话：12345。",Element.ALIGN_LEFT,true,0);
        //设置二维码图片
        addCellImage(table3,"static/receive/fs/佛山政务服务微信公众号.png",70f,70f,Element.ALIGN_CENTER);
        addCell(table3,font4,"",Element.ALIGN_LEFT,true,0);
        addCell(table3,font4,"",Element.ALIGN_LEFT,true,0);
//        addCell(table3,font4,"更多服务请关注“佛山。\r\n政务服务”微信公众号。",Element.ALIGN_LEFT,true,0);
//        addCellImage(table3,"static/receive/fs/佛山政务服务微信公众号.png",70f,70f,Element.ALIGN_LEFT);
        document.add(table3);
    }

    /**
     * 设置物料流转凭证最后盖章部分
     *
     * @param document
     * @param date
     * @param spacing
     * @throws Exception
     */
    public static void setWuliaoTail(Document document, Date date, float spacing) throws Exception {
        Font font3 = fontMap.get("font3");
        Font font4 = fontMap.get("font4");
        Paragraph paragraph = new Paragraph("发件单位：（印章）                    收件单位：（印章）\n\n_____年_____月_____日                _____年_____月_____日", font3);
        paragraph.setSpacingBefore(spacing);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
        ReceivePDFUtils.oneLine(document, font3);
        ReceivePDFUtils.paragrahLeft(document, "备注：此文书一式两份，一份送达申请人，一份存档。", font4);
        ReceivePDFUtils.drawLine(document, 1f, 100, BaseColor.BLACK, Element.ALIGN_CENTER, -5f);
        //新建表格，4列的表格
        PdfPTable table3 = new PdfPTable(4);
        table3.setTotalWidth(PageSize.A4.getWidth());
        int width3[] = {35, 18, 26, 21};
        table3.setWidths(width3);
        table3.setTotalWidth(PageSize.A4.getWidth() - 130);
        table3.setSpacingBefore(15f);
        table3.setLockedWidth(true);
        addCell(table3, font4, "微信扫描二维码查询办理进度。\r\n咨 询 及 投 诉 电 话：12345。", Element.ALIGN_LEFT, true, 0);
        //设置二维码图片
        addCellImage(table3, "static/receive/fs/佛山政务服务微信公众号.png", 70f, 70f, Element.ALIGN_CENTER);
        addCell(table3, font4, "更多服务请关注“佛山。\r\n政务服务”微信公众号。", Element.ALIGN_LEFT, true, 0);
        addCellImage(table3, "static/receive/fs/佛山政务服务微信公众号.png", 70f, 70f, Element.ALIGN_LEFT);
        document.add(table3);
    }


    /**
     * 回执头部
     * @param document
     * @param seqNo       序号
     * @param receiveName   回执名称
     * @param instrumentNo  文书编号
     * @throws Exception
     */
    public static void setHeader(Document document,String seqNo,String receiveName,String instrumentNo,String region)throws Exception{
        Font font2 = fontMap.get("font2");
        Font font3 = fontMap.get("font3");
        Font font6 = fontMap.get("font6");
        //设置序号
        ReceivePDFUtils.paragrahLeft(document,seqNo,font6);
        //设置行政区划
        if(StringUtils.isNotBlank(region)){
            ReceivePDFUtils.paragrahCenter(document,region,font2);
        }
        //设置回执名称
        ReceivePDFUtils.paragrahCenter(document,receiveName,font2);
        //设置文书编号
        ReceivePDFUtils.paragrahCenter(document,"（"+ instrumentNo +"）",font3);
        ReceivePDFUtils.oneLine(document,font3);
        ReceivePDFUtils.oneLine(document,font3);
        //画横线
        ReceivePDFUtils.drawLine(document,1f,100,BaseColor.BLACK,Element.ALIGN_CENTER,-5);
        ReceivePDFUtils.oneLine(document,font3);
    }

    /**
     * 格式化日期
     * @param date
     * @return
     */
    private static String formatDate(Date date){
        if(date != null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            return format.format(date);
        }
        return "";
    }

    /**
     * 表格添加一个单元格图片
     * @param table
     * @param imagePath
     * @param fitWidth
     * @param fitHeight
     * @param horizontalAlignment
     * @throws Exception
     */
    private static void addCellImage(PdfPTable table,String imagePath,float fitWidth,float fitHeight,int horizontalAlignment)throws Exception {
        Paragraph paragraph = new Paragraph();
        //运行的时候用这个
        ClassPathResource imgPathResource = new ClassPathResource(imagePath);
        URL imgUrl = imgPathResource.getURL();
        Image image = Image.getInstance(imgUrl);
        //测试时获取图片的路径
//        Image image = Image.getInstance("F:\\develop\\foshan20191120\\4、关于印发佛山市行政许可和公共服务事项流程标准应用规范的通知（ 佛政务〔2017〕81号）\\回执模板\\佛山政务服务微信公众号.png");
        image.scaleToFit(fitWidth, fitHeight);
//        image.scalePercent(200);
        paragraph.add(new Chunk(image, 0, 0, true));
        PdfPCell imageCell = new PdfPCell();
        imageCell.addElement(paragraph);
        imageCell.setHorizontalAlignment(horizontalAlignment);
        imageCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(imageCell);
    }

    /**
     * 表格添加一个单元格的内容
     * @param table
     * @param font
     * @param content
     * @param horizontalAlignment
     * @param isHideBorder
     * @param colspan
     */
    private static void addCell(PdfPTable table, Font font, String content, int horizontalAlignment, boolean isHideBorder, int colspan){
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        cell.setPaddingLeft(1);
        //设置单元格的垂直对齐方式
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        //设置单元格的水平对齐方式
        cell.setHorizontalAlignment(horizontalAlignment);
        if(colspan > 0){
            //合并单元格
            cell.setColspan(colspan);
        }
        if(isHideBorder){
            //隐藏单元格边框
            cell.disableBorderSide(15);
        }
        table.addCell(cell);
    }

    /**
     * 加上上下边距
     * @param table
     * @param font
     * @param content
     * @param horizontalAlignment
     * @param isHideBorder
     * @param colspan
     * @param padding
     */
    private static void addCell(PdfPTable table, Font font, String content, int horizontalAlignment, boolean isHideBorder, int colspan,int minHeight){
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        cell.setPaddingLeft(1);
        cell.setMinimumHeight(minHeight);
        //设置单元格的垂直对齐方式
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        //设置单元格的水平对齐方式
        cell.setHorizontalAlignment(horizontalAlignment);
        if(colspan > 0){
            //合并单元格
            cell.setColspan(colspan);
        }
        if(isHideBorder){
            //隐藏单元格边框
            cell.disableBorderSide(15);
        }
        table.addCell(cell);
    }

    /**
     * 表格添加一个单元格的内容
     *
     * @param table
     * @param font
     * @param content
     * @param horizontalAlignment
     * @param isHideBorder
     * @param colspan
     */
    private static void addCellWithPadding(PdfPTable table, Font font, String content, int horizontalAlignment, boolean isHideBorder, int colspan) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        //设置单元格的垂直对齐方式
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        //设置单元格的水平对齐方式
        cell.setHorizontalAlignment(horizontalAlignment);
        if (colspan > 0) {
            //合并单元格
            cell.setColspan(colspan);
        }
        if (isHideBorder) {
            //隐藏单元格边框
            cell.disableBorderSide(15);
        }
        cell.setPadding(10);
        table.addCell(cell);
    }

    private static void addCellWithPadding(PdfPTable table, Paragraph content, int horizontalAlignment, boolean isHideBorder, int colspan) {
        PdfPCell cell = new PdfPCell(content);
        //设置单元格的垂直对齐方式
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        //设置单元格的水平对齐方式
        cell.setHorizontalAlignment(horizontalAlignment);
        if (colspan > 0) {
            //合并单元格
            cell.setColspan(colspan);
        }
        if (isHideBorder) {
            //隐藏单元格边框
            cell.disableBorderSide(15);
        }
        cell.setPadding(10);
        table.addCell(cell);
    }

    /**
     * 表格添加一个单元格的内容
     *
     * @param table
     * @param font
     * @param content
     * @param horizontalAlignment
     * @param isHideBorder
     * @param colspan
     */
    private static void addCellWithPaddingAndHeight(PdfPTable table, Font font, String content, int horizontalAlignment, boolean isHideBorder, int colspan, float minHeight) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        //设置单元格的垂直对齐方式
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        //设置单元格的水平对齐方式
        cell.setHorizontalAlignment(horizontalAlignment);
        if (colspan > 0) {
            //合并单元格
            cell.setColspan(colspan);
        }
        if (isHideBorder) {
            //隐藏单元格边框
            cell.disableBorderSide(15);
        }
        cell.setPadding(10);
        cell.setMinimumHeight(minHeight);
        table.addCell(cell);
    }

    /**
     * 表格添加一个空行
     * @param table
     * @param font
     * @param numColumns
     */
    private static void addEmptyRow(PdfPTable table,Font font,int numColumns){
        int i = 0;
        while (numColumns > 0 && i < numColumns){
            PdfPCell cell = new PdfPCell(new Paragraph(" ", font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            i++;
        }
    }

    /**
     * 表格添加四个单元格的内容
     * @param table
     * @param font
     * @param firstCellContent
     * @param secondCellContent
     * @param thirdCellContent
     * @param fourthCellContent
     */
    private static void addOneRow4Cell(PdfPTable table, Font font, String firstCellContent, String secondCellContent, String thirdCellContent, String fourthCellContent) {
        addCellWithPadding(table, font, firstCellContent, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font, secondCellContent, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font, thirdCellContent, Element.ALIGN_CENTER, false, 0);
        addCellWithPadding(table, font, fourthCellContent,Element.ALIGN_CENTER,false,0);
    }

    /**
     * 表格添加指定格式的一行内容
     * @param table
     * @param font
     * @param contents
     */
    private static void addOneRow(PdfPTable table,Font font,String... contents){
        if(contents != null && contents.length > 0){
            for(int i=0,len=contents.length;i<len;i++){
                String content = contents[i];
                addCellWithPadding(table, font,content,Element.ALIGN_CENTER,false,0);
            }
        }
    }

    /**
     * 获取带下划线的内容块
     * @param content
     * @param font
     * @return
     */
    public static Chunk getUnderLineChunk(String content,Font font){
        Chunk chunk = new Chunk(content,font);
        chunk.setUnderline(0.5f,-3f);
        return chunk;
    }

    /**
     * 添加带下划线格式的日期
     * @param paragraph
     * @param font
     * @param date
     * @throws ParseException
     */
    private static void setDateUnderLine(Paragraph paragraph,Font font, Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        paragraph.add(getUnderLineChunk(" "+calendar.get(Calendar.YEAR)+" ",font));
        paragraph.add(" 年 ");
        paragraph.add(getUnderLineChunk(" "+(calendar.get(Calendar.MONTH)+1)+" ",font));
        paragraph.add(" 月 ");
        paragraph.add(getUnderLineChunk(" "+calendar.get(Calendar.DATE)+" ",font));
        paragraph.add(" 日");
    }

    /**
     * 获取当前页文档的高度
     * @param writer
     * @return
     * @throws ReflectiveOperationException
     */
    public static float currentHeight(PdfWriter writer) throws ReflectiveOperationException{
        Method getPdfDocument = writer.getClass().getDeclaredMethod("getPdfDocument");
        getPdfDocument.setAccessible(true);
        PdfDocument pdfD = (PdfDocument) getPdfDocument.invoke(writer);
        Field getHeight = pdfD.getClass().getDeclaredField("currentHeight");
        getHeight.setAccessible(true);
        return  (float)getHeight.get(pdfD);
    }

    @Data
    static class PdfTableMatInfo{
        /**序号**/
        private String number;
        /**材料名称**/
        private String matName;
        /**原件（份）**/
        private String paperCount;
        /**复印（份）**/
        private String copyCount;
        /**补正意见**/
        private String correctionOpinion;

        public static PdfTableMatInfo getInstance(String number, String matName, String paperCount, String copyCount){
            return new PdfTableMatInfo(number,matName,paperCount,copyCount);
        }

        public PdfTableMatInfo(String number, String matName, String paperCount, String copyCount) {
            this.number = number;
            this.matName = matName;
            this.paperCount = paperCount;
            this.copyCount = copyCount;
        }

        public PdfTableMatInfo(String number, String matName, String paperCount, String copyCount, String correctionOpinion) {
            this.number = number;
            this.matName = matName;
            this.paperCount = paperCount;
            this.copyCount = copyCount;
            this.correctionOpinion = correctionOpinion;
        }
    }

    @Data
    static class Option {

        public static String SELECT = "☑";
        public static String NOSELECT = "□";

        /**是否选中**/
        private boolean isSelect;
        /**选项内容**/
        private String content;
        /**选项参数**/
        private String param;

        public Option(boolean isSelect, String content, String param) {
            this.isSelect = isSelect;
            this.content = content;
            this.param = param;
        }

        public String getSelect() {
            if (this.isSelect) {
                return SELECT;
            } else {
                return NOSELECT;
            }
        }
    }

    @Data
    static class PdfServiceDocInfo {
        /**
         * 送达文书名称
         **/
        private String docName;
        /**
         * 文书编号
         **/
        private String docNo;
        /**
         * 份数
         **/
        private String paperCount;

        public static PdfServiceDocInfo getInstance(String docName, String docNo, String paperCount) {
            return new PdfServiceDocInfo(docName, docNo, paperCount);
        }

        public PdfServiceDocInfo(String docName, String docNo, String paperCount) {
            this.docName = docName;
            this.docNo = docNo;
            this.paperCount = paperCount;
        }
    }

    public static void test() throws Exception{
        ReceiveBaseVo vo = new ReceiveBaseVo();
        vo.setReceiveCertNo("88888888");
        vo.setApplicant("广东拓杰机电工程有限公司");
        vo.setApplicantIDCard("91441900598964612J");
        vo.setApplicantDetailSite("广东省佛山市发展大厦25楼");
        vo.setIdrepresentative("李丝丝");
        vo.setIdmobile("13560829811");
        vo.setIdno("440183198205252434");
        vo.setApplyinstCode("202001060015");
        vo.setProjName("广东拓杰机电工程有限公司木材市场工程");
        vo.setProjLocalCode("2019929292929292929");
        vo.setItemName("南海区建设项目用地预审与选址意见书核发");
        vo.setAgentLinkmanTel("13666666666");
        vo.setAgentLinkmanName("谢丽丽");
        vo.setAgentLinkmanIDCard("441121521221221222");
        vo.setTimeLimit("20");
        vo.setDueUnit("1");
        vo.setSpecialProcessName("现场听证");

        ProjAcceptOpinionSummaryVo projAcceptOpinionSummaryVo = new ProjAcceptOpinionSummaryVo();
        Map temp = Maps.newHashMap();
        temp.put("自然资源局","同意");
        temp.put("发改委","同意");
        temp.put("气象局","同意");
        temp.put("环保局","同意");
        projAcceptOpinionSummaryVo.setCentralCode("2019929292929292929");
        projAcceptOpinionSummaryVo.setProjName("广东拓杰机电工程有限公司木材市场工程");
        projAcceptOpinionSummaryVo.setProjAddr("广东省佛山市发展大厦25楼");
        projAcceptOpinionSummaryVo.setBuildArea("1500");
        projAcceptOpinionSummaryVo.setLinkman("李莉莉");
        projAcceptOpinionSummaryVo.setLinkmanPhone("13799999999");
        projAcceptOpinionSummaryVo.setAboveFloor("15");
        projAcceptOpinionSummaryVo.setDesignUnitName("设计单位");
        projAcceptOpinionSummaryVo.setKanchaUnitName("勘察单位");
        projAcceptOpinionSummaryVo.setShigongUnitName("施工单位");
        projAcceptOpinionSummaryVo.setJianliUnitName("监理单位");
        projAcceptOpinionSummaryVo.setBuildUnitName("建设单位");
        projAcceptOpinionSummaryVo.setDeptOpinions(temp);
        vo.setProjAcceptOpinionSummaryVo(projAcceptOpinionSummaryVo);

        createLhysZhongshenYjsReceipt(vo);
    }

    public static void main(String[] args) throws Exception{
        test();
//        createTeShuReceipt();
//        createShouLiReceipt(null);
    }

}
