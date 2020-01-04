package com.augurit.aplanmis.mall.userCenter.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.constants.GuideApplyState;
import com.augurit.aplanmis.common.domain.AeaHiGuide;
import com.augurit.aplanmis.common.domain.AeaHiGuideDetail;
import com.augurit.aplanmis.common.domain.AeaSolicitOrg;
import com.augurit.aplanmis.common.domain.AeaSolicitOrgUser;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitOrgService;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitOrgUserService;
import com.augurit.aplanmis.common.service.apply.AeaHiGuideDetailService;
import com.augurit.aplanmis.common.service.apply.AeaHiGuideService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaGuideApplyVo;
import com.augurit.aplanmis.mall.userCenter.vo.AeaGuideItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RestAeaHiGuideService {
    @Autowired
    private AeaHiGuideService aeaHiGuideService;
    @Autowired
    private AeaSolicitOrgService aeaSolicitOrgService;
    @Autowired
    private AeaSolicitOrgUserService aeaSolicitOrgUserService;
    @Autowired
    private AeaHiGuideDetailService aeaHiGuideDetailService;

    public void initAeaHiGuide(AeaGuideApplyVo aeaGuideApplyVo) throws Exception {
        AeaHiGuide aeaHiGuide=new AeaHiGuide();
        aeaHiGuide.setGuideId(UUID.randomUUID().toString());
        aeaHiGuide.setApplyinstId(aeaGuideApplyVo.getApplyinstId());
        aeaHiGuide.setApplyLinkmanInfoId(aeaGuideApplyVo.getLinkmanInfoId());
        aeaHiGuide.setGuideType("1".equals(aeaGuideApplyVo.getApplySubject())?"u":"p");
        aeaHiGuide.setProjInfoId(aeaGuideApplyVo.getProjInfoId());
        aeaHiGuide.setApplyState(GuideApplyState.LEADER_SIGNING.getValue());
        aeaHiGuide.setGuideStartTime(new Date());
        aeaHiGuide.setDueTimeLimit(4.0);
        aeaHiGuide.setStageId(aeaGuideApplyVo.getStageId());
        aeaHiGuide.setIsItGuide(aeaGuideApplyVo.getIsItGuide());
        aeaHiGuide.setCreater(SecurityContext.getCurrentUserName());
        aeaHiGuide.setCreateTime(new Date());
        aeaHiGuide.setApplyUnitInfoId(aeaGuideApplyVo.getUnitInfoId());
        AeaSolicitOrg aeaSolicitOrg=new AeaSolicitOrg();
        aeaSolicitOrg.setStageId(aeaGuideApplyVo.getStageId());
        aeaSolicitOrg.setIsBusSolicit("0");
        List<AeaSolicitOrg> solicitOrgs = aeaSolicitOrgService.listAeaSolicitOrg(aeaSolicitOrg);
        if(solicitOrgs.size()==0) throw new Exception("找不到牵头部门");
        aeaHiGuide.setLeaderOrgId(solicitOrgs.size()>0?solicitOrgs.get(0).getOrgId():null);
        if(solicitOrgs.size()>0){
            AeaSolicitOrgUser aeaSolicitOrgUser=new AeaSolicitOrgUser();
            aeaSolicitOrgUser.setSolicitOrgId(solicitOrgs.get(0).getSolicitOrgId());
            aeaSolicitOrgUser.setIsActive("1");
            List<AeaSolicitOrgUser> solicitOrgUsers = aeaSolicitOrgUserService.listAeaSolicitOrgUser(aeaSolicitOrgUser);
            aeaHiGuide.setLeaderUserId(solicitOrgUsers.size()>0?solicitOrgUsers.get(0).getUserId():null);
        }
        aeaHiGuideService.insertAeaHiGuide(aeaHiGuide);
        if("1".equals(aeaGuideApplyVo.getIsItGuide()))initAeaHiGuideDetail(aeaGuideApplyVo,aeaHiGuide.getGuideId(),"s");
        initAeaHiGuideDetail(aeaGuideApplyVo,aeaHiGuide.getGuideId(),"o");

    }


    public void initAeaHiGuideDetail(AeaGuideApplyVo aeaGuideApplyVo,String guideId,String detailType){

        List<AeaGuideItemVo> itemList=aeaGuideApplyVo.getItemList();//申请人选择
        List<AeaGuideItemVo> itItemList=aeaGuideApplyVo.getItItemList();//智能引导选择

        if(itemList.size()==0) return;
        for (AeaGuideItemVo aeaGuideItemVo:itemList){
            AeaHiGuideDetail aeaHiGuideDetail=new AeaHiGuideDetail();
            aeaHiGuideDetail.setGuideDetailId(UUID.randomUUID().toString());
            aeaHiGuideDetail.setGuideId(guideId);
            aeaHiGuideDetail.setDetailType(detailType);//s表示智能引导，o表示业主，l表示牵头部门，i表示事项部门，r表示最终结果
            String guideChangeAction="a";
            if("o".equals(detailType) && "1".equals(aeaGuideApplyVo.getIsItGuide())){
                //TODO
//                if("1".equals(aeaGuideItemVo.getIsITSel()) && "0".equals(aeaGuideItemVo.getApplySelOpinion())){
//                    guideChangeAction="d";
//                }
            }
            aeaHiGuideDetail.setGuideChangeAction(guideChangeAction);//c表示change，a表示add，d表示delete, s表示不变
            aeaHiGuideDetail.setCreater(SecurityContext.getCurrentUserName());
            aeaHiGuideDetail.setThemeId(aeaGuideApplyVo.getThemeId());
            aeaHiGuideDetail.setThemeVerId(aeaGuideApplyVo.getThemeVerId());
            aeaHiGuideDetail.setStageId(aeaGuideApplyVo.getStageId());
            aeaHiGuideDetail.setItemId(aeaGuideItemVo.getItemId());
            aeaHiGuideDetail.setItemVerId(aeaGuideItemVo.getItemVerId());
            aeaHiGuideDetail.setGuideOpinion(aeaGuideItemVo.getApplySelOpinion());
            aeaHiGuideDetail.setCreateTime(new Date());
            aeaHiGuideDetailService.insertAeaHiGuideDetail(aeaHiGuideDetail);

        }
    }

}
