package com.augurit.aplanmis.front.receive.service;

import com.augurit.aplanmis.common.service.receive.vo.ConstructPermitVo;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.documents.BookmarksNavigator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.Date;

@Service
@Transactional
public class DocTemplateService {

    public void createDocDocument(ConstructPermitVo vo, HttpServletResponse response) {

        Document doc = new Document();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File file = null;

        try {
            doc.loadFromFile("src/main/resources/static/receive/default/施工许可证新版(总承包).doc");

            //定位到指定书签位置起始标签位置，插入图片
//        BookmarksNavigator bookmarksNavigator1 = new BookmarksNavigator(doc);
//        bookmarksNavigator1.moveToBookmark("bookmark1",true,false);
//        Paragraph para = new Paragraph(doc);
//        DocPicture picture = para.appendPicture("eth.png");
//        picture.setTextWrappingStyle(TextWrappingStyle.Through);
//        bookmarksNavigator1.insertParagraph(para);

            //定位到指定书签位置末尾标签位置，插入文本
            BookmarksNavigator bookmarksNavigator2 = new BookmarksNavigator(doc);
            bookmarksNavigator2.moveToBookmark("projectName", false, true);
            bookmarksNavigator2.insertText(vo.getProjectName());

            bookmarksNavigator2.moveToBookmark("supervisionUnitName", false, true);
            bookmarksNavigator2.insertText(vo.getSupervisionUnitName());

            bookmarksNavigator2.moveToBookmark("buildUnitName", false, true);
            bookmarksNavigator2.insertText(vo.getBuildUnitName());

            bookmarksNavigator2.moveToBookmark("chiefEngineer", false, true);
            bookmarksNavigator2.insertText(vo.getChiefEngineer());

            bookmarksNavigator2.moveToBookmark("constructPermitCode", false, true);
            bookmarksNavigator2.insertText(vo.getConstructPermitCode());

            bookmarksNavigator2.moveToBookmark("constructUnitLeader", false, true);
            bookmarksNavigator2.insertText(vo.getConstructUnitLeader());

            bookmarksNavigator2.moveToBookmark("constructUnitName", false, true);
            bookmarksNavigator2.insertText(vo.getConstructUnitName());

            bookmarksNavigator2.moveToBookmark("contractDuration", false, true);
            bookmarksNavigator2.insertText(vo.getContractDuration());

            bookmarksNavigator2.moveToBookmark("contractPrice", false, true);
            bookmarksNavigator2.insertText(vo.getContractPrice().toString());

            bookmarksNavigator2.moveToBookmark("contructAddress", false, true);
            bookmarksNavigator2.insertText(vo.getContructAddress());

            bookmarksNavigator2.moveToBookmark("contructScale", false, true);
            bookmarksNavigator2.insertText(vo.getContructScale());

            bookmarksNavigator2.moveToBookmark("designUnitLeader", false, true);
            bookmarksNavigator2.insertText(vo.getDesignUnitLeader());

            bookmarksNavigator2.moveToBookmark("designUnitName", false, true);
            bookmarksNavigator2.insertText(vo.getDesignUnitName());

            bookmarksNavigator2.moveToBookmark("explorationUnitLeader", false, true);
            bookmarksNavigator2.insertText(vo.getExplorationUnitLeader());

            bookmarksNavigator2.moveToBookmark("explorationUnitName", false, true);
            bookmarksNavigator2.insertText(vo.getExplorationUnitName());

            bookmarksNavigator2.moveToBookmark("gczcbUnitLeader", false, true);
            bookmarksNavigator2.insertText(vo.getGczcbUnitLeader());

            bookmarksNavigator2.moveToBookmark("gczcbUnitName", false, true);
            bookmarksNavigator2.insertText(vo.getGczcbUnitName());

            bookmarksNavigator2.moveToBookmark("remarks", false, true);
            bookmarksNavigator2.insertText(vo.getRemarks());

            bookmarksNavigator2.moveToBookmark("issueOrgName", false, true);
            bookmarksNavigator2.insertText(vo.getIssueOrgName());

            bookmarksNavigator2.moveToBookmark("issueYear", false, true);
            bookmarksNavigator2.insertText(vo.getIssueYear());

            bookmarksNavigator2.moveToBookmark("issueMonth", false, true);
            bookmarksNavigator2.insertText(vo.getIssueMonth());

            bookmarksNavigator2.moveToBookmark("issueDay", false, true);
            bookmarksNavigator2.insertText(vo.getIssueDay());

            String newFileName = "施工许可证_" + new Date().getTime() + ".docx";

            //保存文档
            doc.saveToFile("src/main/resources/static/receive/default/" + newFileName, FileFormat.Docx);
            doc.dispose();

            //word文件输出流
            file = new File("src/main/resources/static/receive/default/" + newFileName);
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(newFileName, "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length)))
                bos.write(buff, 0, bytesRead);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (doc != null)
                doc.close();
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (file != null)
                    file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
