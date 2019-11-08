package com.augurit.aplanmis.common.service.file;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件下载公共方法-单||批量
 *
 * @author xiaohutu
 */
@Service
public interface FileUtilsService {

    /**
     * ，
     * 根据detailId下载文件、批量或单独
     *
     * @param detailIds BSC_ATT_DETAIL表的主键 每个文件存储后对应的唯一值
     * @param response  HttpServletResponse
     * @param request   HttpServletRequuploadAttachmentsest
     * @param request   isDownloadCovertPdf 是否下载转换的pdf文件，默认false
     * @return true || false
     * @throws Exception e
     */
    boolean downloadAttachment(String[] detailIds, HttpServletResponse response, HttpServletRequest request, Boolean isDownloadCovertPdf) throws Exception;

    /**
     * 下载单个文件
     *
     * @param detailId
     * @param response
     * @param request
     * @param isDownloadCovertPdf
     * @return
     * @throws Exception
     */
    boolean downloadAttachment(String detailId, HttpServletResponse response, HttpServletRequest request, Boolean isDownloadCovertPdf) throws Exception;

    /**
     * 根据detailId删除文件
     *
     * @param detailId
     * @throws Exception
     */
    void deleteAttachment(String detailId) throws Exception;

    /**
     * 根据多个detailId删除文件列表
     *
     * @param detailIds
     * @throws Exception
     */
    void deleteAttachments(String[] detailIds) throws Exception;

    /**
     * 根据多个业务ID返回文件列表
     *
     * @param recordIds 业务ID
     * @param tableName 业务表名
     * @param pkName    业务主键
     * @return
     * @throws Exception
     */
    List<BscAttForm> getAttachmentsByRecordId(String[] recordIds, String tableName, String pkName) throws Exception;

    /**
     * 单文件或多文件上传
     *
     * @param tableName 业务表名
     * @param pkName    业务主键
     * @param recordId  业务ID
     * @param request   存放 file 文件流
     * @throws Exception
     */
    void uploadAttachments(String tableName, String pkName, String recordId, String dirId, HttpServletRequest request) throws Exception;

    /**
     * 单文件或多文件上传
     *
     * @param tableName 业务表名
     * @param pkName    业务主键
     * @param recordId  业务ID
     * @param files     文件流
     * @throws Exception
     */
    void uploadAttachments(String tableName, String pkName, String recordId, List<MultipartFile> files) throws Exception;

    /**
     * 根据材料实例ID 查询附件列表
     *
     * @param matinstId 材料实例ID
     * @return List<BscAttFileAndDir>
     * @throws Exception
     */
    List<BscAttFileAndDir> getMatAttDetailByMatinstId(String matinstId) throws Exception;

    /**
     * 通用查询附件方法
     *
     * @param recordId  业务表主键ID
     * @param pkName    业务表主键
     * @param tableName 业务表名
     * @return List<BscAttFileAndDir>
     * @throws Exception e
     */
    List<BscAttFileAndDir> getBscAttFileAndDirListByinstId(String recordId, String pkName, String tableName) throws Exception;

    /**
     * 文件预览
     *
     * @param detailId           文件ID
     * @param request            HttpServletRequest
     * @param response           HttpServletResponse
     * @param redirectAttributes RedirectAttributes
     * @return ModelAndView
     * @throws Exception e
     */
    ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception;

    /**
     * 读取电子件 text文档
     *
     * @param detailId 文件ID
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws Exception E
     */
    void readFile(String detailId, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 文件上传
     *
     * @param tableName 业务表名
     * @param pkName    业务表主键
     * @param recordId  业务表主键ID
     * @param dirId     文件夹id
     * @param file      文件
     * @throws Exception e
     */
    BscAttForm upload(String tableName, String pkName, String recordId, String dirId, MultipartFile file) throws Exception;

}
