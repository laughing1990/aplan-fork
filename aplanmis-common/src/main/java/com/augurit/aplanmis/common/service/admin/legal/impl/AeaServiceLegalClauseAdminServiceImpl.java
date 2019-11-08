package com.augurit.aplanmis.common.service.admin.legal.impl;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemServiceBasic;
import com.augurit.aplanmis.common.domain.AeaServiceLegalClause;
import com.augurit.aplanmis.common.mapper.AeaItemServiceBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaServiceLegalClauseMapper;
import com.augurit.aplanmis.common.service.admin.legal.AeaServiceLegalClauseAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/7/25 025 15:18
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaServiceLegalClauseAdminServiceImpl implements AeaServiceLegalClauseAdminService {
    private static Logger logger = LoggerFactory.getLogger(AeaServiceLegalClauseAdminServiceImpl.class);

    @Autowired
    AeaServiceLegalClauseMapper aeaServiceLegalClauseMapper;

    @Autowired
    AeaItemServiceBasicMapper aeaItemServiceBasicMapper;


    @Autowired
    private FileUtilsService fileUtilsService;


    @Override
    public void saveAeaServiceLegalClause(AeaServiceLegalClause legalClause) {

        legalClause.setCreater(SecurityContext.getCurrentUserId());
        legalClause.setRootOrgId(SecurityContext.getCurrentOrgId());
        legalClause.setCreateTime(new Date());
        legalClause.setIsActive(Status.ON);
        legalClause.setIsDeleted(Status.OFF);
        aeaServiceLegalClauseMapper.insertAeaServiceLegalClause(legalClause);
    }

    @Override
    public void saveAeaServiceLegalClauseAndAtt(HttpServletRequest request, AeaServiceLegalClause legalClause) throws Exception {

        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<MultipartFile> clauseAttFiles = req.getFiles("clauseAttFile");
        handleAttr("clauseAtt", clauseAttFiles, legalClause);
        saveAeaServiceLegalClause(legalClause);
    }

    @Override
    public void updateAeaServiceLegalClause(AeaServiceLegalClause legalClause) {

        legalClause.setRootOrgId(SecurityContext.getCurrentOrgId());
        legalClause.setModifier(SecurityContext.getCurrentUserId());
        legalClause.setModifyTime(new Date());
        aeaServiceLegalClauseMapper.updateAeaServiceLegalClause(legalClause);
    }

    @Override
    public void updateAeaServiceLegalClauseAndAtt(HttpServletRequest request, AeaServiceLegalClause legalClause) throws Exception {

        AeaServiceLegalClause oldLegalClause = getAeaServiceLegalClauseById(legalClause.getLegalClauseId());
        if (oldLegalClause != null) {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            List<MultipartFile> clauseAttFiles = req.getFiles("clauseAttFile");
            handleAttr("clauseAtt", clauseAttFiles, oldLegalClause);
            legalClause.setClauseAtt(oldLegalClause.getClauseAtt());
        }
        updateAeaServiceLegalClause(legalClause);
    }

    @Override
    public void deleteAeaServiceLegalClauseById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("id is null");
        }
        aeaServiceLegalClauseMapper.deleteAeaServiceLegalClause(id);
    }

    private void handleAttr(String type, List<MultipartFile> files, AeaServiceLegalClause legalClause) throws Exception {

        if (files != null && files.size() > 0) {
            String pkName = "";
            if ("clauseAtt".equals(type)) {
                pkName = "CLAUSE_ATT";
            }
            StringBuilder ids = new StringBuilder();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    BscAttForm form = fileUtilsService.upload("AEA_SERVICE_LEGAL_CLAUSE", pkName, legalClause.getLegalClauseId(), null, file);
                    if (null != form) {

                        ids.append(form.getDetailId()).append(",");
                    }
                }
            }
            if (StringUtils.isNotBlank(ids.toString())) {
                if ("clauseAtt".equals(type)) {
                    legalClause.setClauseAtt((StringUtils.isBlank(legalClause.getClauseAtt()) ? "" : legalClause.getClauseAtt()) + ids);
                }
            }
        }
    }

    @Override
    public AeaServiceLegalClause getAeaServiceLegalClauseById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("id is null");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaServiceLegalClauseMapper.getAeaServiceLegalClauseById(id);
    }

    @Override
    public Long getMaxSortNo(String rootOrgId) {

        Long sortNo = aeaServiceLegalClauseMapper.getMaxSortNo(rootOrgId);
        return sortNo == null ? 1L : (sortNo + 1);
    }

    @Override
    public List<AeaServiceLegalClause> listItemServiceLegalNoPage(String itemVerId) throws Exception {
        List<AeaServiceLegalClause> result = new ArrayList<>();
        if(StringUtils.isNotBlank(itemVerId)){
            AeaServiceLegalClause aeaServiceLegalClause = new AeaServiceLegalClause();
            aeaServiceLegalClause.setTableName("AEA_ITEM_VER");
            aeaServiceLegalClause.setPkName("ITEM_VER_ID");
            aeaServiceLegalClause.setRecordId(itemVerId);
            result=aeaServiceLegalClauseMapper.listAeaServiceLegalClauseForRecordId(aeaServiceLegalClause);
        }
        return result;
    }


    @Override
    public EasyuiPageInfo<AeaServiceLegalClause> listAeaItemServiceLegalClause(String itemVerId, String keyword,Page page) {
        PageHelper.startPage(page);
        List<AeaServiceLegalClause> result = new ArrayList<>();
        if(StringUtils.isNotBlank(itemVerId)){
            AeaServiceLegalClause aeaServiceLegalClause = new AeaServiceLegalClause();
            aeaServiceLegalClause.setTableName("AEA_ITEM_VER");
            aeaServiceLegalClause.setPkName("ITEM_VER_ID");
            aeaServiceLegalClause.setRecordId(itemVerId);
            aeaServiceLegalClause.setKeyword(keyword);
            result=aeaServiceLegalClauseMapper.listAeaServiceLegalClauseForRecordId(aeaServiceLegalClause);
        }
        EasyuiPageInfo<AeaServiceLegalClause> pageInfo = PageHelper.toEasyuiPageInfo(new PageInfo<>(result));
        return pageInfo;
    }


}
