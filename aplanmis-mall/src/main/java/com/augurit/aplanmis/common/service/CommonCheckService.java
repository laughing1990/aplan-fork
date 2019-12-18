package com.augurit.aplanmis.common.service;


import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaProjLinkman;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.mapper.AeaProjLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonCheckService {

    @Autowired
    protected IBscAttService bscAttService;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaProjLinkmanMapper aeaProjLinkmanMapper;
    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;
    @Value("${mall.check.authority:false}")
    private boolean isCheckAuthority;

    /**
     * 判断当前 matinstId是否属于request
     * @param matinstId
     * @param request
     * @return
     */
    public Boolean isMatBelong(String matinstId, HttpServletRequest request)throws Exception {
        if (!isCheckAuthority) return true;
        try {
            AeaHiItemMatinst aeaHiItemMatinst = aeaHiItemMatinstMapper.getAeaHiItemMatinstById(matinstId);
            if (aeaHiItemMatinst==null) return false;
            LoginInfoVo user = SessionUtil.getLoginInfo(request);
            if ("1".equals(aeaHiItemMatinst.getMatinstSource())){//个人 委托人
                if(("1".equals(user.getIsPersonAccount())|| StringUtils.isNotBlank(user.getUserId()))&&(user.getUserId().equals(aeaHiItemMatinst.getLinkmanInfoId()))){//个人
                    return true;
                }else if (("1".equals(user.getIsPersonAccount())||StringUtils.isNotBlank(user.getUserId()))&&(!user.getUserId().equals(aeaHiItemMatinst.getLinkmanInfoId()))){//企业
                    return false;
                } else{//企业
                    return false;
                }
            }else if ("0".equals(aeaHiItemMatinst.getMatinstSource())){
                if("1".equals(user.getIsPersonAccount())||StringUtils.isNotBlank(user.getUserId())){//个人
                    return false;
                }else if (user.getUnitId().equals(aeaHiItemMatinst.getUnitInfoId())){//企业
                    return true;
                }else {
                    return false;
                }
            }else return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * 判断当前 detailId是否属于request
     * @param detailId
     * @param request
     * @return
     * @throws Exception
     */
    public Boolean isFileBelong(String detailId,HttpServletRequest request)throws Exception{
        if (!isCheckAuthority) return true;
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        BscAttLink param = new BscAttLink();
        param.setDetailId(detailId);
        //param.setPkName("MATINST_ID");
        List<BscAttLink> links = bscAttMapper.listBscAttLink(param);
        if (links.size()>1){//说明此detailId必然是申请实例文件，有可能是补全补正文件，也有可能是材料库文件
            param.setPkName("MATINST_ID");
            List<BscAttLink> matInstLinks = bscAttMapper.listBscAttLink(param);
            if (matInstLinks.size()>0){
                return  this.isMatBelong(matInstLinks.get(0).getRecordId(),request);
            }
            if ("1".equals(loginInfoVo.getIsPersonAccount())||StringUtils.isNotBlank(loginInfoVo.getUserId())){//个人
                param.setPkName("LINKMAN_INFO_ID");
                List<BscAttLink> linkmanLinks = bscAttMapper.listBscAttLink(param);
                if (linkmanLinks.size()==1) return true;
                return false;
            }else {
                param.setPkName("UNIT_INFO_ID");
                List<BscAttLink> unitLinks = bscAttMapper.listBscAttLink(param);
                if (unitLinks.size()==1) return true;
                return false;
            }
        }else if (links.size()==1){
            String pkName = links.get(0).getPkName();
            String recordId = links.get(0).getRecordId();
            if ("WSBLLCT".equals(pkName)){//网上流程图，不需要查询权限，返回true
                return true;
            }else if ("UNIT_INFO_ID".equals(pkName)&&StringUtils.isNotBlank(recordId)
                    &&recordId.equals(loginInfoVo.getUnitId())){//单位上传的材料
                return true;
            }else if ("LINKMAN_INFO_ID".equals(pkName)&&StringUtils.isNotBlank(recordId)
                    &&recordId.equals(loginInfoVo.getUserId())){
                return true;
            }else if ("MATINST_ID".equals(pkName)){
                return  this.isMatBelong(recordId,request);
            }else if ("CERTINST_ID".equals(pkName)){
                return false;
            }else if ("REAL_ININST_ID".equals(pkName)){
                return  true;
            }else if ("GUIDE_ATT".equals(pkName)){
                return  true;
            }else return false;
        }else return false;
    }


    public Boolean isMatInstFileBelong(String detailId,HttpServletRequest request)throws Exception{
        if (!isCheckAuthority) return true;
        BscAttLink param = new BscAttLink();
        param.setDetailId(detailId);
        param.setPkName("MATINST_ID");
        List<BscAttLink> links = bscAttMapper.listBscAttLink(param);
        if (links.size()==0) return false;
        String matInstId = links.get(0).getRecordId();
        return  this.isMatBelong(matInstId,request);
    }

    public Boolean isApplyBelong(String applyInstId,String projinfoId, HttpServletRequest request)throws Exception {
        if (!isCheckAuthority) return true;
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyInstId);
        if (aeaHiApplyinst==null) throw new Exception("查询出错");
        String applySubject = aeaHiApplyinst.getApplySubject();//(申办主体：1 单位，0 个人)
        LoginInfoVo user = SessionUtil.getLoginInfo(request);
        if ("0".equals(applySubject)){
            AeaProjLinkman param = new AeaProjLinkman();
            param.setApplyinstId(applyInstId);
            param.setLinkmanInfoId(user.getUserId());
            List<AeaProjLinkman> list = aeaProjLinkmanMapper.listAeaProjLinkman(param);
            if (list.size()==0) return false;
        }else {
            List<AeaUnitInfo> unitInfos = aeaUnitInfoMapper.findApplyUnitProj(applyInstId,projinfoId,"1");
            if (unitInfos.size()==0) return false;
            List<String> unitInfoIds = unitInfos.stream()
                    .map(AeaUnitInfo::getUnitInfoId).distinct()
                    .collect(Collectors.toList());
            if (!unitInfoIds.contains(user.getUnitId()))return false;
        }
        return true;
    }
}
