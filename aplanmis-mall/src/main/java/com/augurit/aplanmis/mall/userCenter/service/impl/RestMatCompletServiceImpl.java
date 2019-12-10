package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstCorrectMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectRealIninstService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.mall.userCenter.service.RestMatCompletService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaLinkmanInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.UserInfoVo;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestMatCompletServiceImpl implements RestMatCompletService {

    @Autowired
    private AeaHiApplyinstCorrectRealIninstService aeaHiApplyinstCorrectRealIninstService;
    @Autowired
    private BscAttMapper bscAttMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    AeaHiApplyinstCorrectMapper aeaHiApplyinstCorrectMapper;
    @Autowired
    AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    AeaUnitInfoService aeaUnitInfoService;

    public void uploadFileByCloud(String attRealIninstId, String  detailIds) throws Exception {
        AeaHiApplyinstCorrectRealIninst realIninst = aeaHiApplyinstCorrectRealIninstService.getAeaHiApplyinstCorrectRealIninstById(attRealIninstId);
        if (realIninst == null) throw new Exception("材料实例不存在！");
        String[] detailIdArr = detailIds.split(",");
        for (String s : detailIdArr) {
            insertCorrectRealInst(attRealIninstId,s);
        }
        updateAeaHiItemCorrectRealIninst(attRealIninstId, realIninst);
    }


    private void insertCorrectRealInst(String  attRealIninstId,String detailId) throws Exception {
        BscAttLink bscAttLink = new BscAttLink();
        bscAttLink.setLinkId(UUID.randomUUID().toString());
        bscAttLink.setTableName("AEA_HI_APPLYINST_CORRECT_REAL_ININST");
        bscAttLink.setPkName("REAL_ININST_ID");
        bscAttLink.setRecordId(attRealIninstId);
        bscAttLink.setDetailId(detailId);
        bscAttLink.setLinkType("a");
        bscAttMapper.insertLink(bscAttLink);
    }
    private void updateAeaHiItemCorrectRealIninst(String attRealIninstId, AeaHiApplyinstCorrectRealIninst realIninst) throws Exception {
        List<BscAttFileAndDir> matAttDetail = bscAttDetailMapper.searchFileAndDirsSimple(null, null, "AEA_HI_APPLYINST_CORRECT_REAL_ININST", "REAL_ININST_ID", new String[]{attRealIninstId});
        realIninst.setAttCount(Long.valueOf(matAttDetail.size()));
        realIninst.setModifyTime(new Date());
        realIninst.setModifier(SecurityContext.getCurrentUserName());
        aeaHiApplyinstCorrectRealIninstService.updateAeaHiApplyinstCorrectRealIninst(realIninst);
    }

    @Override
    public UserInfoVo getAllApplyObject(HttpServletRequest request, String applyinstCorrectId) throws Exception {
        UserInfoVo vo = new UserInfoVo();
        Assert.hasText(applyinstCorrectId, "applyinstCorrectId is null");
        AeaHiApplyinstCorrect aeaHiApplyinstCorrect = aeaHiApplyinstCorrectMapper.getAeaHiApplyinstCorrectById(applyinstCorrectId);
        Assert.notNull(aeaHiApplyinstCorrect,"找不到材料补全实例信息！");
        String applyInstId = aeaHiApplyinstCorrect.getApplyinstId();
        String projInfoId = aeaHiApplyinstCorrect.getProjInfoId();
        AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyInstId);
        Assert.notNull(aeaHiApplyinst,"aeaHiApplyinst is null");
        String applySubject = aeaHiApplyinst.getApplySubject();//申报主体
        AeaLinkmanInfoVo aeaLinkmanInfoVo = new AeaLinkmanInfoVo();
        if ("0".equals(applySubject)){//个人
            vo.setRole("0");
            AeaLinkmanInfo aeaLinkmanInfo = aeaLinkmanInfoMapper.getApplyProjLinkman(applyInstId,projInfoId);
            AeaLinkmanInfoVo aeaLinkmanIanfoVo = new AeaLinkmanInfoVo();
            org.springframework.beans.BeanUtils.copyProperties(aeaLinkmanInfo, aeaLinkmanIanfoVo);
            vo.setAeaLinkmanInfo(aeaLinkmanInfoVo);
        }else {//单位
            vo.setRole("2");
            List<AeaUnitInfo> unitInfos = aeaUnitInfoService.findOwerUnitProj(projInfoId);
            vo.setAeaUnitList(unitInfos.stream().map(AeaUnitInfoVo::build).collect(Collectors.toList()));
        }

        return vo;
    }
}
