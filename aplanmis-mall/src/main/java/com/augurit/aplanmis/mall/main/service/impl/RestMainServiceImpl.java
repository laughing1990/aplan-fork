package com.augurit.aplanmis.mall.main.service.impl;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.constants.AeaItemBasicContants;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.DicConstants;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.enumer.ThemeTypeEnum;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.service.dic.BscDicCodeItemService;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.stage.AeaParStageService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.main.service.RestMainService;
import com.augurit.aplanmis.mall.main.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestMainServiceImpl implements RestMainService {


    @Autowired
    BscDicCodeItemService bscDicCodeItemService;
    @Autowired
    AeaParThemeService aeaParThemeService;
    @Autowired
    AeaParStageService aeaParStageService;
    @Autowired
    AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    AeaItemBasicService aeaItemBasicService;
    @Autowired
    private AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    private BscDicRegionMapper bscDicRegionMapper;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private AeaParThemeMapper aeaParThemeMapper;
    @Autowired
    private BscDicCodeService bscDicCodeService;
    @Autowired
    AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Override
    public List<ThemeTypeVo> getThemeTypeList(String rootOrgId) throws Exception{
        //主题类型
        List<BscDicCodeItem> themeTypes = bscDicCodeItemService.getActiveItemsByTypeCode(DicConstants.THEME_TYPE, rootOrgId);
        //AeaParTheme aeaParTheme = new AeaParTheme();
        //aeaParTheme.setIsOnlineSb("1");
        //List<AeaParTheme> themeList = aeaParThemeService.listAeaParTheme(aeaParTheme);//假如最新版本未发布，这里会查出未发布的主题版本，有问题
        List<ThemeTypeVo> vos=new ArrayList<>();
        for (BscDicCodeItem themeType:themeTypes){
            ThemeTypeVo vo=new ThemeTypeVo();
            List<AeaParTheme> newList=new ArrayList<>();
            List<AeaParTheme> themeList = aeaParThemeMapper.getAeaParThemeListByThemeType(themeType.getItemCode(), rootOrgId);
            for (AeaParTheme theme:themeList){
                if (theme.getThemeType().equals(themeType.getItemCode()) && "1".equals(theme.getIsOnlineSb())) {
                    theme.setThemeMemo(StringUtils.isNotBlank(theme.getThemeMemo())?theme.getThemeMemo().replaceAll("\r\n","<br>"):"");
                    newList.add(theme);
                }
            }
            vo.setThemeTypeName(themeType.getItemName());
            vo.setThemeTypeCode(themeType.getItemCode());
            vo.setThemeList(newList.size() > 0 ? newList.stream().sorted(Comparator.comparing(AeaParTheme::getSortNo)).collect(Collectors.toList()) : newList);
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<AeaParStage> getStageByThemeId(String themeId, String projInfoId, String rootOrgId,String unitInfoId,String dygjbzfxfw, HttpServletRequest request) throws Exception {

        List<AeaParStage> results = new ArrayList<>();
        AeaParTheme theme = aeaParThemeService.getAeaParThemeByThemeId(themeId);
        if (theme == null) return results;
        //AeaParThemeSeq aeaParThemeSeq = aeaParThemeService.getAeaParThemeSeqByThemeId(themeId,rootOrgId);
        //if(aeaParThemeSeq==null) return results;
        //AeaParThemeVer aeaParThemeVer = aeaParThemeService.getAeaParThemeVerByThemeIdAndVerNum(themeId,aeaParThemeSeq.getMaxNum(),rootOrgId);
        //if(aeaParThemeVer==null) return results;
        List<AeaParTheme> publishedThemes = aeaParThemeService.getTestRunOrPublishedVerAeaParTheme(theme.getThemeType(), rootOrgId);
        if (publishedThemes.size() == 0) return results;
        List<String> themeVerIds = publishedThemes.stream().filter(parTheme -> themeId.equals(parTheme.getThemeId())).map(AeaParTheme::getThemeVerId).collect(Collectors.toList());
        if (themeVerIds.size() == 0) return results;
        List<AeaParStage> list = aeaParStageService.listAeaParStageByThemeIdOrThemeVerId(StringUtils.EMPTY, themeVerIds.get(0), rootOrgId);
        for (AeaParStage aeaParStage : list) {
            if(StringUtils.isNotBlank(dygjbzfxfw) && !dygjbzfxfw.equals(aeaParStage.getDygjbzfxfw())){
                continue;
            }
            if (("0".equals(theme.getIsMainline()) && "1".equals(aeaParStage.getIsNode())
                    ||("0".equals(theme.getIsAuxiline()) && "2".equals(aeaParStage.getIsNode()))
                    ||("0".equals(theme.getIsTechspectline()) && "3".equals(aeaParStage.getIsNode()))))  continue;
            aeaParStage.setStageMemo(StringUtils.isNotBlank(aeaParStage.getStageMemo()) ? aeaParStage.getStageMemo().replaceAll("\r\n", "").trim() : "");
            if (StringUtils.isEmpty(projInfoId)) {results.add(aeaParStage);continue;}
            //阶段是否已办及申请实例状态
            List<String> applyInstStatusList = new ArrayList<>();
            LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
            if("1".equals(loginInfo.getIsPersonAccount())){//个人
                applyInstStatusList = aeaParStageService.getApplyInstStatusByProjInfoIdAndStageId(aeaParStage.getStageId(),projInfoId,"",loginInfo.getUserId());
            }else if(com.augurit.agcloud.framework.util.StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
                if (StringUtils.isNotBlank(unitInfoId)){
                    AeaUnitLinkman query=new AeaUnitLinkman();
                    query.setUnitInfoId(unitInfoId);
                    query.setLinkmanInfoId(loginInfo.getUserId());
                    List<AeaUnitLinkman> unitLinkmans = aeaUnitLinkmanMapper.listAeaUnitLinkman(query);
                    //加入权限校验，判断当前单位是否委托人所有
                    if (unitLinkmans==null||unitLinkmans.size()==0) throw new Exception("不可查看其他单位信息");
                    applyInstStatusList = aeaParStageService.getApplyInstStatusByProjInfoIdAndStageId(aeaParStage.getStageId(),projInfoId,unitInfoId,"");
                }else {
                    applyInstStatusList = aeaParStageService.getApplyInstStatusByProjInfoIdAndStageId(aeaParStage.getStageId(),projInfoId,"",loginInfo.getUserId());
                }
            }else{//企业
                applyInstStatusList = aeaParStageService.getApplyInstStatusByProjInfoIdAndStageId(aeaParStage.getStageId(),projInfoId,loginInfo.getUnitId(),"");

            }
            if (applyInstStatusList!=null && applyInstStatusList.size()>0){
                aeaParStage.setIsDoing("1");
                aeaParStage.setApplyinstStatusCode(applyInstStatusList.get(0));
                BscDicCodeItem dic = bscDicCodeItemService.getItemByTypeCodeAndItemCodeAndOrgId(DicConstants.APPLYINST_STATE, applyInstStatusList.get(0), SecurityContext.getCurrentOrgId());
                aeaParStage.setApplyinstStatus(dic==null?"":dic.getItemName());
            }else {
                aeaParStage.setIsDoing("0");
            }
            results.add(aeaParStage);
        }

        return results;
    }

    @Override
    public StaticticsVo getApplyStatistics(String rootOrgId) throws Exception {
        int allCount = aeaHiApplyinstService.countApplyinstByApplyinstState(StringUtils.EMPTY,rootOrgId);
        int allComplete = aeaHiApplyinstService.countApplyinstByApplyinstState(ApplyState.COMPLETED.getValue(),rootOrgId);
        List<String> list = new ArrayList<>();
        int nowMonthCount = aeaHiApplyinstService.countCurrentMonthApplyinstByApplyinstStates(list,rootOrgId);
        list.add(ApplyState.COMPLETED.getValue());
        int nowMonthComplete = aeaHiApplyinstService.countCurrentMonthApplyinstByApplyinstStates(list,rootOrgId);
        return getStaticticsVo(allCount, allComplete, nowMonthCount, nowMonthComplete);
    }

    private StaticticsVo getStaticticsVo(int allCount, int allComplete, int nowMonthCount, int nowMonthComplete) {
        StaticticsVo vo = new StaticticsVo();
        vo.setAllCount(allCount);
        vo.setAllComplete(allComplete);
        vo.setNowMonthCount(nowMonthCount);
        vo.setNowMonthComplete(nowMonthComplete);
        return vo;
    }

    @Override
    public StaticticsVo getItemStatistics(String rootOrgId) throws Exception{
        int allCount = aeaHiIteminstService.countTotalItemByStates(new String[]{},rootOrgId);
        int allComplete = aeaHiIteminstService.countTotalItemByStates(new String[]{ItemStatus.AGREE.getValue(),ItemStatus.AGREE_TOLERANCE.getValue(),ItemStatus.DISAGREE.getValue()},rootOrgId);
        int nowMonthCount = aeaHiIteminstService.countCurrentMonthCountItemByStates(new String[]{},rootOrgId);
        int nowMonthComplete = aeaHiIteminstService.countCurrentMonthCountItemByStates(new String[]{ItemStatus.AGREE.getValue(),ItemStatus.AGREE_TOLERANCE.getValue(),ItemStatus.DISAGREE.getValue()},rootOrgId);
        return getStaticticsVo(allCount, allComplete, nowMonthCount, nowMonthComplete);
    }

    @Override
    public AeaRegionVo getAeaRegionVo(String topOrgId) throws Exception {
        AeaRegionVo aeaRegionVo = new AeaRegionVo();
        BscDicRegion bscDicRegion = bscDicRegionMapper.selectRegionByOrgId(topOrgId);
        if (bscDicRegion==null) return aeaRegionVo;
        OpuOmOrg opuOmOrg = opuOmOrgMapper.getOrg(topOrgId);
        AeaBasicOrgVo aeaBasicOrgVo = new AeaBasicOrgVo();
        if ("p".equals(bscDicRegion.getRegionType())) {//省
            aeaBasicOrgVo.setOrgId(opuOmOrg.getOrgId());
            aeaBasicOrgVo.setOrgName(opuOmOrg.getOrgName());
            aeaBasicOrgVo.setOrgDeptList(aeaItemBasicService.listOpuOmOrgByAeaItemBasic1(opuOmOrg.getOrgId()));
            aeaRegionVo.setFirAeaOrgVo(aeaBasicOrgVo);
            //查询市
            List<AeaOrgVo> secAeaOrgVoList = getAeaOrgVos(topOrgId);
            //查询县
            secAeaOrgVoList.stream().forEach(secAeaOrgVo ->{
                List<AeaBasicOrgVo> areaAeaOrgVoList = getAreaAeaOrgVos(secAeaOrgVo.getOrgId());
                secAeaOrgVo.setChildAeaOrgVo(areaAeaOrgVoList);
            });
            aeaRegionVo.setSecAeaOrgVo(secAeaOrgVoList);
        }else if ("c".equals(bscDicRegion.getRegionType())){//市
            aeaBasicOrgVo.setOrgId(opuOmOrg.getOrgId());
            aeaBasicOrgVo.setOrgName(opuOmOrg.getOrgName());
            aeaBasicOrgVo.setOrgDeptList(aeaItemBasicService.listOpuOmOrgByAeaItemBasic1(opuOmOrg.getOrgId()));
            aeaRegionVo.setFirAeaOrgVo(aeaBasicOrgVo);
            //县
            List<AeaOrgVo> secAeaOrgVoList = getAeaOrgVos(topOrgId);
            aeaRegionVo.setSecAeaOrgVo(secAeaOrgVoList);
        }
            return aeaRegionVo;
    }

    @Override
    public PageInfo<AeaItemBasic> getItemMainList(String keyword, String topOrgId, int pageNum, int pageSize) {
        try {
            AeaItemBasic aeaItemBasic = new AeaItemBasic();
            aeaItemBasic.setKeyword(keyword);
            aeaItemBasic.setIsCatalog(AeaItemBasicContants.IS_CATALOG_NO);
            PageHelper.startPage(pageNum,pageSize);
            List<AeaItemBasic>  itemList = aeaItemBasicMapper.listAeaItemBasicOfMain(aeaItemBasic);
            return new PageInfo<>(itemList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PageInfo<AeaParTheme> getThemeMainList(String keyword, String topOrgId, int pageNum, int pageSize) {
        try {
        AeaParTheme aeaParTheme = new AeaParTheme();
        aeaParTheme.setKeyword(keyword);
        aeaParTheme.setIsOnlineSb("1");
        PageHelper.startPage(pageNum,pageSize);
        List<AeaParTheme> themeList = aeaParThemeMapper.listAeaParThemeByKeyword(aeaParTheme);
        if (themeList.size()==0) new PageInfo<>(themeList);
        for (AeaParTheme param:themeList){
            List<AeaParStage> list = aeaParStageService.listAeaParStageByThemeIdOrThemeVerId(param.getThemeId(),"",topOrgId);
            if (list.size()>0){
                param.setStageList(list.stream().filter(aeaParStage->"1".equals(aeaParStage.getIsNode())).collect(Collectors.toList()));
            }else {
                param.setStageList(new ArrayList<>());
            }
            if (StringUtils.isNotBlank(param.getThemeType())){
                param.setThemeType(ThemeTypeEnum.getName(param.getThemeType()));
            }
        }
        return new PageInfo<>(themeList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<AeaOrgVo> getAeaOrgVos(String topOrgId) {
        OpuOmOrg areaParam = new OpuOmOrg();
        areaParam.setParentOrgId(topOrgId);
        areaParam.setOrgProperty("u");//u代表单位
        List<OpuOmOrg> areaOpuOmOrgList = opuOmOrgMapper.getAllChildActiveOrgs(areaParam);
        return areaOpuOmOrgList.stream().map(c -> new AeaOrgVo(c.getOrgId(), c.getOrgName())).collect(Collectors.toList());
    }

    private List<AeaBasicOrgVo> getAreaAeaOrgVos(String orgId) {
        OpuOmOrg areaParam = new OpuOmOrg();
        areaParam.setParentOrgId(orgId);
        areaParam.setOrgProperty("u");//u代表单位
        List<OpuOmOrg> areaOpuOmOrgList = opuOmOrgMapper.getAllChildActiveOrgs(areaParam);
        return areaOpuOmOrgList.stream().map(c -> new AeaBasicOrgVo(c.getOrgId(), c.getOrgName())).collect(Collectors.toList());
    }
}
