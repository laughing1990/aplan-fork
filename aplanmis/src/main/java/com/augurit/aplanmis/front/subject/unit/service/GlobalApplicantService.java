package com.augurit.aplanmis.front.subject.unit.service;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.utils.GeneratePasswordUtils;
import com.augurit.aplanmis.common.utils.Md5Utils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 全局单位库-Service服务调用接口类
 */
@Service
public class GlobalApplicantService {

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
   /* @Autowired
    private BpmProcessRestService bpmProcessRestService;*/

    public AeaUnitInfo getApplicantById(String id) {
        AeaUnitInfo aeaUnitInfo = aeaUnitInfoMapper.getAeaUnitInfoById(id);
        return aeaUnitInfo;
    }


    public PageInfo<AeaUnitInfo> listApplicants(String keyword, Page page) {
        PageHelper.startPage(page);
        List<AeaUnitInfo> listAeaUnitInfos = aeaUnitInfoMapper.findAeaUnitInfoByKeyword(keyword,SecurityContext.getCurrentOrgId());
        return new PageInfo<AeaUnitInfo>(listAeaUnitInfos);
    }

    public void updateAeaApplicant(AeaUnitInfo aeaUnitInfo) throws Exception {
        List<AeaUnitInfo> listAeaUnitInfos = aeaUnitInfoService.getUnitInfoListByIdCard(aeaUnitInfo.getUnifiedSocialCreditCode());
        if (listAeaUnitInfos.size() > 0 && !aeaUnitInfo.getUnitInfoId().equals(listAeaUnitInfos.get(0).getUnitInfoId()))
            throw new Exception("该统一社会信用代码已注册！");
        if (StringUtils.isBlank(aeaUnitInfo.getIsDeleted())) {
            aeaUnitInfo.setIsDeleted("0");
        }
        if (StringUtils.isBlank(aeaUnitInfo.getModifier())) {
            aeaUnitInfo.setModifier(SecurityContext.getCurrentUserName());
        }
        if (aeaUnitInfo.getModifyTime() == null) {
            aeaUnitInfo.setModifyTime(new Date());
        }
        aeaUnitInfoMapper.updateAeaUnitInfo(aeaUnitInfo);
    }

    public String insertAeaApplicant(AeaUnitInfo aeaUnitInfo) throws Exception {
        List<AeaUnitInfo> listAeaUnitInfos = aeaUnitInfoService.getUnitInfoListByIdCard(aeaUnitInfo.getUnifiedSocialCreditCode());
        if (listAeaUnitInfos.size() > 0) throw new Exception("该统一社会信用代码已注册！");
        aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
        aeaUnitInfo.setCreateTime(new Date());
        aeaUnitInfo.setIsDeleted("0");
        aeaUnitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaUnitInfoMapper.insertAeaUnitInfo(aeaUnitInfo);
        return aeaUnitInfo.getUnitInfoId();
    }

    public void deleteAeaApplicant(String id) {
        aeaUnitInfoMapper.deleteOrEnableAeaUnitInfo(id, SecurityContext.getCurrentUserName(), "1");
    }

    public void batchDeleteApplicantByIds(String[] ids) {
        aeaUnitInfoMapper.batchDeleteUnitInfo(ids);
    }

    public Map generatePassWord(String unitInfoId, HttpServletRequest request) throws Exception {
        Map<String,Object> map=new HashMap<>();
        AeaUnitInfo unit = aeaUnitInfoMapper.getAeaUnitInfoById(unitInfoId);
        if (unit != null) {
            String key = GeneratePasswordUtils.generatePassword(8);
            AeaUnitInfo updateParam = new AeaUnitInfo();
            updateParam.setUnifiedSocialCreditCode(unit.getUnifiedSocialCreditCode());
            updateParam.setUnitInfoId(unitInfoId);
            if (StringUtils.isNotBlank(key)) updateParam.setLoginPwd(Md5Utils.encrypt32(key));
            String loginName = unit.getLoginName();
            if (StringUtils.isBlank(loginName) && StringUtils.isNotBlank(unit.getUnifiedSocialCreditCode())) {
                loginName = unit.getUnifiedSocialCreditCode();//将证照号作为登录名
                updateParam.setLoginName(loginName);
            }

            updateAeaApplicant(updateParam);
            request.getSession().setAttribute("applicantLoginName", loginName);
            request.getSession().setAttribute("applicantLoginPwd", key);
            map.put("loginName",loginName);
            map.put("loginPwd",key);
        }
        return map;
    }

    public List<AeaUnitInfo> listApplicantsNoPage(AeaUnitInfo aeaUnitInfo) {
        return aeaUnitInfoMapper.listAeaUnitInfo(aeaUnitInfo);
    }

  /*  public boolean deleteFile(String detailIds) throws Exception{
        String[] str = detailIds.split(",");
        return bpmProcessRestService.delelteFiles(str);
    }*/

    public List<BscAttFileAndDir> getAttFiles(String linkmanInfoId, String tableName, String pkName) throws Exception {
        String[] recordIds = {linkmanInfoId};
        List<BscAttFileAndDir> list = new ArrayList<BscAttFileAndDir>();
        if (recordIds.length > 0) {
            list.addAll(bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_UNIT_INFO", "UNIT_INFO_ID", recordIds));
        }
        return list;
    }

    /* public void uploadFiles(String tableName, String pkName, String linkmanInfoId, HttpServletRequest request) throws Exception {
         if(StringUtils.isNotBlank(pkName) && StringUtils.isNotBlank(tableName)){
             String GPY = request.getParameter("GPY");
             if (StringUtils.isNotBlank(GPY) && "true".equals(GPY)) {
                 bpmProcessRestService.uploadAttachment(linkmanInfoId, "", "", false, request);
             } else {
                 aeaBusinessService.uploadFilesToApplicantOrLinkman(tableName, pkName, linkmanInfoId, request);
             }
         }
     }*/
    public List<AeaUnitInfo> getBuildUnitListByProjId(String projInfoId) {
        List<AeaUnitInfo> buildUnitList = aeaUnitInfoMapper.findUnitProjByProjInfoIdAndIsOwner(projInfoId, "1");

        for (AeaUnitInfo info : buildUnitList) {
            List<AeaLinkmanInfo> aeaLinkmanInfoList = aeaLinkmanInfoMapper.findAllUnitLinkman(info.getUnitInfoId());
            if (aeaLinkmanInfoList.size() > 0)
                info.setLinkmanInfoList(aeaLinkmanInfoList);
        }
        return buildUnitList;
    }

    public List<AeaUnitInfo> getAgencyUnitListByProjId(String projInfoId) {
        List<AeaUnitInfo> agencyUnitList = aeaUnitInfoMapper.findUnitProjByProjInfoIdAndIsOwner(projInfoId, "0");
        for (AeaUnitInfo info : agencyUnitList) {
            List<AeaLinkmanInfo> aeaLinkmanInfoList = aeaLinkmanInfoMapper.findAllUnitLinkman(info.getUnitInfoId());
            if (aeaLinkmanInfoList.size() > 0)
                info.setLinkmanInfoList(aeaLinkmanInfoList);
        }
        return agencyUnitList;
    }
}
