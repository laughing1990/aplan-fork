package com.augurit.aplanmis.front.supermarket.service.impl;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaImQualLevel;
import com.augurit.aplanmis.common.domain.AeaImServiceMajor;
import com.augurit.aplanmis.common.mapper.AeaImQualLevelMapper;
import com.augurit.aplanmis.common.mapper.AeaImQualMapper;
import com.augurit.aplanmis.common.mapper.AeaImServiceMajorMapper;
import com.augurit.aplanmis.common.mapper.AeaImServiceMapper;
import com.augurit.aplanmis.common.utils.BusinessUtils;
import com.augurit.aplanmis.common.utils.FileUtils;
import com.augurit.aplanmis.common.vo.AeaImQualVo;
import com.augurit.aplanmis.common.vo.AeaImServiceVo;
import com.augurit.aplanmis.front.supermarket.service.AeaImProjPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/***
 * @description 中介服务接口实现类
 * @author mohaoqi
 * @date 2019/11/4 0004
 ***/
@Service
public class AeaImProjPurchaseServiceImpl implements AeaImProjPurchaseService {

    @Autowired
    private AeaImServiceMapper aeaImServiceMapper;

    @Autowired
    private AeaImQualLevelMapper aeaImQualLevelMapper;

    @Autowired
    private AeaImQualMapper aeaImQualMapper;


    @Autowired
    private AeaImServiceMajorMapper aeaImServiceMajorMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

    @Override
    public List<AeaImServiceVo> getItemServiceListByItemVerId(String itemVerId) throws Exception {
        List<AeaImServiceVo> serviceVoList = aeaImServiceMapper.listAeaImServiceVoByItemVerId(itemVerId);

        for (AeaImServiceVo aeaImServiceVo : serviceVoList) {
            List<AeaImQualVo> qualVoList = aeaImQualMapper.listAeaImQualVoByServiceId(aeaImServiceVo.getServiceId());
            aeaImServiceVo.setAeaImQualVos(qualVoList);
            for (AeaImQualVo aeaImQualVo : qualVoList) {
                AeaImQualLevel qualLevel = new AeaImQualLevel();
                qualLevel.setParentQualLevelId(aeaImQualVo.getQualLevelId());
                qualLevel.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                qualLevel.setRootOrgId(topOrgId);
                List<AeaImQualLevel> aeaImQualLevelList = aeaImQualLevelMapper.listAeaImQualLevel(qualLevel);

                BusinessUtils.sort(aeaImQualLevelList);

                aeaImQualVo.setAeaImQualLevels(aeaImQualLevelList);

                AeaImServiceMajor queryServiceMajor = new AeaImServiceMajor();
                queryServiceMajor.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                queryServiceMajor.setQualId(aeaImQualVo.getQualId());
                queryServiceMajor.setRootOrgId(topOrgId);
                List<AeaImServiceMajor> aeaImServiceMajorList = aeaImServiceMajorMapper.listAeaImServiceMajor(queryServiceMajor);

                for (AeaImServiceMajor aeaImServiceMajor : aeaImServiceMajorList) {
                    aeaImServiceMajor.setpId(aeaImServiceMajor.getParentMajorId());
                    aeaImServiceMajor.setName(aeaImServiceMajor.getMajorName());
                    aeaImServiceMajor.setId(aeaImServiceMajor.getMajorId());
                    aeaImServiceMajor.setChildren(null);
                }
                aeaImQualVo.setAeaImServiceMajors(BusinessUtils.listToTree(aeaImServiceMajorList));
            }
        }

        return serviceVoList;
    }

    @Override
    public String uploadFiles(HttpServletRequest request) throws Exception  {

        Boolean uploadFlag=false;
        String resultRecordId="";
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        if (request instanceof StandardMultipartHttpServletRequest) {
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;

            /***要求说明文件***/
            List<MultipartFile> requireExplainFiles = req.getFiles("requireExplainFile");
            if (requireExplainFiles != null && !requireExplainFiles.isEmpty()) {
                String recordId = UuidUtil.generateUuid();
                uploadFlag=FileUtils.uploadFile("AEA_IM_PROJ_PURCHASE", "REQUIRE_EXPLAIN_FILE", recordId, requireExplainFiles);
                if(uploadFlag){
                    resultRecordId=recordId;
                }
            }
            /***批文文件***/
            List<MultipartFile> officialRemarkFiles = req.getFiles("officialRemarkFile");
            if (officialRemarkFiles != null && !officialRemarkFiles.isEmpty()) {
                String recordId =  UuidUtil.generateUuid();;
                uploadFlag=FileUtils.uploadFile("AEA_IM_PROJ_PURCHASE", "OFFICIAL_REMARK_FILE", recordId, officialRemarkFiles);
                if(uploadFlag){
                    resultRecordId=recordId;
                }
            }
        }

        return resultRecordId;
    }


}
