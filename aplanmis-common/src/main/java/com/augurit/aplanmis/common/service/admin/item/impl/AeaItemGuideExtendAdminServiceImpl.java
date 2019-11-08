package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemGuideExtend;
import com.augurit.aplanmis.common.mapper.AeaItemGuideExtendMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemGuideExtendAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service
@Transactional
public class AeaItemGuideExtendAdminServiceImpl implements AeaItemGuideExtendAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemGuideExtendAdminServiceImpl.class);

    @Autowired
    private AeaItemGuideExtendMapper aeaItemGuideExtendMapper;

    @Autowired
    private FileUtilsService fileUtilsService;


    @Override
    public void saveAeaItemGuideExtend(AeaItemGuideExtend aeaItemGuideExtend) {

        aeaItemGuideExtend.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemGuideExtendMapper.insertAeaItemGuideExtend(aeaItemGuideExtend);
    }

    @Override
    public void updateAeaItemGuideExtend(AeaItemGuideExtend aeaItemGuideExtend) {

        aeaItemGuideExtendMapper.updateAeaItemGuideExtend(aeaItemGuideExtend);
    }

    @Override
    public void deleteAeaItemGuideExtendById(String id) {

        Assert.notNull(id, "id is null.");
        aeaItemGuideExtendMapper.deleteAeaItemGuideExtend(id);
    }

    @Override
    public void deleteGuideExtendByItemVerId(String itemVerId, String rootOrgId) {

        if (StringUtils.isNotBlank(itemVerId)) {
            aeaItemGuideExtendMapper.deleteGuideExtendByItemVerId(itemVerId, rootOrgId);
        }
    }

    @Override
    public PageInfo<AeaItemGuideExtend> listAeaItemGuideExtend(AeaItemGuideExtend aeaItemGuideExtend, Page page) {

        aeaItemGuideExtend.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemGuideExtend> list = aeaItemGuideExtendMapper.listAeaItemGuideExtend(aeaItemGuideExtend);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public AeaItemGuideExtend getAeaItemGuideExtendById(String id) {

        Assert.notNull(id, "id is null.");
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemGuideExtendMapper.getAeaItemGuideExtendById(id);
    }

    @Override
    public List<AeaItemGuideExtend> listAeaItemGuideExtend(AeaItemGuideExtend aeaItemGuideExtend) {

        aeaItemGuideExtend.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemGuideExtend> list = aeaItemGuideExtendMapper.listAeaItemGuideExtend(aeaItemGuideExtend);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void saveSampleFile(AeaItemGuideExtend aeaItemGuideExtend, HttpServletRequest request) throws Exception {

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        aeaItemGuideExtend.setZzzResultGuid(getSampleFile(req, "resultSample", "AEA_ITEM_GUIDE_EXTEND", "ZZZ_RESULT_GUID", aeaItemGuideExtend.getId()));
        updateAeaItemGuideExtend(aeaItemGuideExtend);
    }

    private String getSampleFile(StandardMultipartHttpServletRequest request, String fileName, String tableName, String pkName, String recordId) throws Exception {

        List<MultipartFile> files = request.getFiles(fileName);
        if (files.size() > 0) {
            StringBuilder ids = new StringBuilder();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    BscAttForm form = fileUtilsService.upload(tableName, pkName, recordId, null, file);
                    if (null != form) {


                        ids.append(form.getDetailId()).append(",");
                    }
                }
            }
            return ids.toString();
        }
        return null;
    }

    @Override
    public void delItemGuideExtendAtt(String itemGuideExtendId, String type, String detailId) throws Exception {

        AeaItemGuideExtend extend = aeaItemGuideExtendMapper.getAeaItemGuideExtendById(itemGuideExtendId);
        if (extend != null) {
            if (StringUtils.isNotBlank(extend.getZzzResultGuid())) {
                String replaceStr = extend.getZzzResultGuid().replaceAll(detailId + ",", "");
                extend.setZzzResultGuid(replaceStr);
                updateAeaItemGuideExtend(extend);
            }
        }
        fileUtilsService.deleteAttachment(detailId);
    }
}

