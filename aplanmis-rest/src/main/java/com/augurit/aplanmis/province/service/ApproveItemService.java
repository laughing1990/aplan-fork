package com.augurit.aplanmis.province.service;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.province.vo.ProviceBaseInfoVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApproveItemService {

    @Autowired
    private ApproveCommonService approveCommonService;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;
    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Value("${dg.sso.access.platform.org.top-org-id:0368948a-1cdf-4bf8-a828-71d796ba89f6}")
    private String topOrgId;


    /**
     * 根据 项目编码，事项实例ID 查询办理信息
     *
     * @param proj_code
     * @param item_instance_code
     * @return
     * @throws Exception
     */
    public AeaHiIteminst getAeaHiIteminstByProjCodeAndIteminstId(String proj_code, String item_instance_code) throws Exception {
        AeaHiIteminst aeaHiIteminst = aeaHiIteminstMapper.getAeaHiIteminstById(item_instance_code);
        if (aeaHiIteminst == null) return new AeaHiIteminst();
        //事项版本id
        String itemVerId = aeaHiIteminst.getItemVerId();
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getOneByItemVerId(itemVerId, topOrgId);
        if (null == aeaItemBasic) return aeaHiIteminst;
        //办件类型
        BscDicCodeItem itemPropertyDic = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("ITEM_PROPERTY", aeaItemBasic.getItemProperty(), topOrgId);
        if (null != itemPropertyDic) {
            aeaHiIteminst.setItemProperty(itemPropertyDic.getItemName());
        }


        aeaHiIteminst.setIsTiming(null == aeaItemBasic.getDueNum() ? "非计时" : "计时");
        //当时限单位为空是，当1即工作日处理
        if (StringUtils.isEmpty(aeaItemBasic.getBjType())) aeaItemBasic.setBjType("1");

        BscDicCodeItem dic = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("ITEMINST_STATE", aeaHiIteminst.getIteminstState(), topOrgId);
        if (dic != null) {
            aeaHiIteminst.setIteminstState(dic.getItemName());
        }
        //承诺办结时限=受理时间+承诺办结时限
        int dueNumInt = aeaItemBasic.getDueNum().intValue();
        if (aeaHiIteminst.getAcceptTime() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(aeaHiIteminst.getAcceptTime());
            switch (aeaItemBasic.getBjType()) {
                case "1"://工作日
                    c.add(Calendar.DAY_OF_MONTH, dueNumInt);
                    break;
                case "2"://自然日
                    c.setTime(CommonTools.addDateByWorkDay(c, dueNumInt));
                    break;
                case "3"://小时
                    c.add(Calendar.HOUR, dueNumInt);
                    break;
                case "4"://分钟
                    c.add(Calendar.MINUTE, dueNumInt);
                    break;

            }
            aeaHiIteminst.setDueDate(c.getTime());
        }
        BscDicCodeItem dic1 = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("DUE_UNIT_TYPE", aeaItemBasic.getBjType(), topOrgId);
        if (dic1 != null) aeaHiIteminst.setBjType(dic1.getItemName());

        aeaHiIteminst.setDueNum(aeaItemBasic.getDueNum());
        if (StringUtils.isEmpty(aeaHiIteminst.getBjType())) aeaHiIteminst.setBjType("工作日");
        return aeaHiIteminst == null ? new AeaHiIteminst() : aeaHiIteminst;
    }

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    public ProviceBaseInfoVo getProvinceBaseInfo(String localCode, String iteminstId) throws Exception {
        ProviceBaseInfoVo vo = new ProviceBaseInfoVo();
        AeaHiApplyinst aeaHiApplyinst = approveCommonService.getApplyinstByProjCodeAndIteminstId(localCode, iteminstId);
        if (null == aeaHiApplyinst) return vo;
        //获取申报信息
        BscDicCodeItem dic = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("APPLYINST_SOURCE", aeaHiApplyinst.getApplyinstSource(), topOrgId);
        if (dic != null) aeaHiApplyinst.setApplyinstSource(dic.getItemName());
        BscDicCodeItem applyStateDic = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("APPLYINST_STATE", aeaHiApplyinst.getApplyinstState(), topOrgId);
        if (applyStateDic != null) aeaHiApplyinst.setApplyinstState(applyStateDic.getItemName());
        vo.changeAppinstToVo(vo, aeaHiApplyinst);

        //获取事项实例信息
        AeaHiIteminst iteminst = this.getAeaHiIteminstByProjCodeAndIteminstId(localCode, iteminstId);

        vo.changeIteminstToVo(vo, iteminst);

        //获取项目信息
        List<AeaProjInfo> applyProj = aeaProjInfoMapper.findApplyProj(aeaHiApplyinst.getApplyinstId());
        if (applyProj.size() > 0) {
            //获取建设单位信息
            List<AeaUnitInfo> unitInfo = aeaUnitInfoService.findApplyOwnerUnitProj(aeaHiApplyinst.getApplyinstId(), applyProj.get(0).getProjInfoId());
            //去重单位
            List<AeaUnitInfo> collect = unitInfo.stream().distinct().collect(Collectors.toList());
            vo.changeProjInfoToVo(vo, applyProj, collect);
        }

        //获取联系人信息
        String linkmanInfoId = aeaHiApplyinst.getLinkmanInfoId();
        AeaLinkmanInfo linkmanInfo = aeaLinkmanInfoMapper.getAeaLinkmanInfoById(linkmanInfoId);
        vo.changLinkmanToVo(vo, linkmanInfo);
        return vo;
    }
}
