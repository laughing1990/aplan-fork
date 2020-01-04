package com.augurit.aplanmis.common.service.project.impl;

import com.augurit.agcloud.bpm.admin.sto.service.impl.AbstractFormDataOptManager;
import com.augurit.agcloud.bpm.common.constant.EDataOpt;
import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.domain.ActStoForminst;
import com.augurit.agcloud.bpm.common.domain.vo.FormDataOptResult;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.AgencyState;
import com.augurit.aplanmis.common.constants.CommonConstant;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.exception.ProjectCodeDuplicateException;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaParentProjMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.dic.GcbmBscRuleCodeStrategy;
import com.augurit.aplanmis.common.service.dic.GdGcbmBscRuleCodeStrategy;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import com.augurit.aplanmis.common.service.window.AeaProjApplyAgentService;
import com.augurit.aplanmis.common.service.window.AeaProjWindowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yinlf
 * @Date 2019/7/5
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AeaProjInofServiceImpl extends AbstractFormDataOptManager implements AeaProjInfoService {

    private static Logger LOGGER = LoggerFactory.getLogger(AeaProjInofServiceImpl.class);

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    @Autowired
    private DatabaseIdProvider databaseIdProvider;
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AeaHiApplyinstMapper aeaHiApplyinstMapper;

    @Autowired
    private AeaParentProjMapper aeaParentProjMapper;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private BscDicRegionMapper bscDicRegionMapper;

    @Autowired
    private GcbmBscRuleCodeStrategy gcbmBscRuleCodeStrategy;

    @Autowired
    private GdGcbmBscRuleCodeStrategy gdGcbmBscRuleCodeStrategy;

    @Autowired
    private AeaParThemeService aeaParThemeService;

    @Override
    public void insertAeaProjInfo(AeaProjInfo aeaProjInfo) {
        if (StringUtils.isBlank(aeaProjInfo.getGcbm())) aeaProjInfo.setGcbm(aeaProjInfo.getLocalCode());
        AeaProjInfo sameGcdm = this.getAeaProjInfoByGcbm(aeaProjInfo.getGcbm());
        if (sameGcdm != null) {
            throw new ProjectCodeDuplicateException("工程编码已存在", "请设置为其他编码");
        }
        if (StringUtils.isBlank(aeaProjInfo.getCreater())) {
            aeaProjInfo.setCreater(SecurityContext.getCurrentUserId());
        }
        if (StringUtils.isBlank(aeaProjInfo.getProjectCreateDate())) {
            aeaProjInfo.setProjectCreateDate(dateFormat.format(new Date()));
        }
        aeaProjInfo.setProjInfoId(UUID.randomUUID().toString());
        aeaProjInfo.setCreateTime(new Date());
        aeaProjInfo.setIsDeleted(Status.OFF);
        aeaProjInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
        LOGGER.debug("新增项目，项目工程代码：{}，项目名称：{}", aeaProjInfo.getGcbm(), aeaProjInfo.getProjName());
        aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);
    }

    @Override
    public void updateAeaProjInfo(AeaProjInfo aeaProjInfo) {
        aeaProjInfo.setModifier(SecurityContext.getCurrentUserId());
        aeaProjInfo.setModifyTime(new Date());
        LOGGER.debug("修改项目信息");
        aeaProjInfoMapper.updateAeaProjInfo(aeaProjInfo);
    }

    @Override
    public void deleteAeaProjInfo(String... projInfoId) {
        LOGGER.debug("批量删除项目信息");
        aeaProjInfoMapper.batchDeleteAeaProjInfo(projInfoId);
    }

    @Override
    public void deleteAeaProjInfoCascade(String... projInfoId) {
        //删除项目单位关联
        //aeaUnitProjMapper.
        //删除项目联系人关联
        //删除项目信息
        aeaProjInfoMapper.batchDeleteAeaProjInfo(projInfoId);
    }

    @Override
    public AeaProjInfo getAeaProjInfoByGcbm(String gcbm) {
        LOGGER.debug("根据工程编码查询项目信息，工程编码：{}", gcbm);
        return aeaProjInfoMapper.getAeaProjInfoByGcbm(gcbm);
    }

    @Override
    public AeaProjInfo getAeaProjInfoByProjInfoId(String projInfoId) {
        LOGGER.debug("根据项目ID查询项目信息，项目ID：{}", projInfoId);
        return aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
    }

    @Override
    public List<AeaProjInfo> findApplyProj(String applyinstId) {
        LOGGER.debug("根据申请实例ID查询项目列表，申请实例ID：{}", applyinstId);
        String orgId = SecurityContext.getCurrentOrgId();
        List<BscDicCodeItem> xmlxCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_PROJECT_STEP", orgId);
        List<BscDicCodeItem> tzlxCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_TZLX", orgId);
        List<BscDicCodeItem> zjlyCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_ZJLY", orgId);
        List<BscDicCodeItem> tdlyCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_TDLY", orgId);
        List<BscDicCodeItem> gcflCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_GCFL", orgId);
        List<BscDicCodeItem> jsxzCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_NATURE", orgId);
        List<BscDicCodeItem> gbhyCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_GBHY", orgId);
        List<BscDicCodeItem> zdxmCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_PROJECT_LEVEL", orgId);
        List<AeaProjInfo> applyProj = aeaProjInfoMapper.findApplyProj(applyinstId);
        for (AeaProjInfo aeaProjInfo : applyProj) {
            String projType = getValueByBscDIcCode(xmlxCodeList, aeaProjInfo.getProjType());
            aeaProjInfo.setProjType(projType);
            String investType = getValueByBscDIcCode(tzlxCodeList, aeaProjInfo.getInvestType());
            aeaProjInfo.setInvestType(investType);
            String zjly = getValueByBscDIcCode(zjlyCodeList, aeaProjInfo.getFinancialSource());
            aeaProjInfo.setFinancialSource(zjly);
            String tdly = getValueByBscDIcCode(tdlyCodeList, aeaProjInfo.getLandSource());
            aeaProjInfo.setLandSource(tdly);
            String gcfl = getValueByBscDIcCode(gcflCodeList, aeaProjInfo.getProjCategory());
            aeaProjInfo.setProjCategory(gcfl);
            String jsxz = getValueByBscDIcCode(jsxzCodeList, aeaProjInfo.getProjNature());
            aeaProjInfo.setProjNature(jsxz);
            String gbhy = getValueByBscDIcCode(gbhyCodeList, aeaProjInfo.getTheIndustry());
            aeaProjInfo.setTheIndustry(gbhy);
            String zdxm = getValueByBscDIcCode(zdxmCodeList, aeaProjInfo.getProjLevel());
            aeaProjInfo.setProjLevel(zdxm);
            aeaProjInfo.setIsForeign("1".equals(aeaProjInfo.getIsForeign()) ? "是" : "否");
            aeaProjInfo.setIsDesignSolution("1".equals(aeaProjInfo.getIsDesignSolution()) ? "是" : "否");
            aeaProjInfo.setIsAreaEstimate("1".equals(aeaProjInfo.getIsAreaEstimate()) ? "是" : "否");
            String regionalism = aeaProjInfo.getRegionalism();
            BscDicRegion bscDicRegion = bscDicRegionMapper.getBscDicRegionById(regionalism);
            if (bscDicRegion != null) {
                aeaProjInfo.setRegionalism(bscDicRegion.getRegionName());
            }
            String projectAddress = aeaProjInfo.getProjectAddress();
            if (StringUtils.isNotBlank(projectAddress)) {
                String[] split = projectAddress.split(",");
                String result = "";
                for (String str : split) {
                    //BscDicRegion query = new BscDicRegion();
                    //query.setRegionNum(str);
                    BscDicRegion addrRegion = bscDicRegionMapper.getBscDicRegionById(str);
//                    BscDicRegion addrBscDicRegion = bscDicRegionMapper.getBscDicRegionById(str);
                    if (addrRegion != null) {
                        result += addrRegion.getRegionName() + ",";
                    }
                }
                if (result.length() > 0) {
                    result = result.substring(0, result.length() - 1);
                }
                aeaProjInfo.setProjectAddress(result);
            }
            try {
                AeaParTheme theme = aeaParThemeService.getAeaParThemeByThemeId(aeaProjInfo.getThemeId());
                aeaProjInfo.setThemeName(Optional.ofNullable(theme).orElse(new AeaParTheme()).getThemeName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return applyProj;
    }

    private String getValueByBscDIcCode(List<BscDicCodeItem> list, String code) {
        for (BscDicCodeItem item : list) {
            if (item.getItemCode().equals(code)) {
                return item.getItemName();
            }
        }
        return code;//找不到则返回原值
    }

    @Override
    public List<AeaProjInfo> findChildProj(String projInfoId) {
        LOGGER.debug("查询子项目列表，项目ID：{}", projInfoId);
        return aeaProjInfoMapper.findChildProj(projInfoId);
    }

    @Override
    public AeaProjInfo findParentProj(String projInfoId) {
        LOGGER.debug("查询父项目信息，项目ID：{}", projInfoId);
        return aeaProjInfoMapper.findParentProj(projInfoId);
    }

    @Override
    public List<AeaProjInfo> findAeaProjInfoByKeyword(String keyword) {
        LOGGER.debug("搜索项目信息");
        return aeaProjInfoMapper.findAeaProjInfoByKeyword(keyword, SecurityContext.getCurrentOrgId());
    }

    @Override
    public PageInfo<AeaProjInfo> listAeaProjInfoByKeyword(String keyword, Page page) {
        LOGGER.debug("分页搜索项目信息");
        PageHelper.startPage(page);
        List<AeaProjInfo> list = aeaProjInfoMapper.findAeaProjInfoByKeyword(keyword, SecurityContext.getCurrentOrgId());
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaProjInfo> findAeaProjInfo(AeaProjInfo aeaProjInfo) {
        LOGGER.debug("查询项目信息");
        return aeaProjInfoMapper.listAeaProjInfo(aeaProjInfo);
    }

    @Override
    public PageInfo<AeaProjInfo> listAeaProjInfo(AeaProjInfo aeaProjInfo, Page page) {
        LOGGER.debug("分页查询项目信息");
        PageHelper.startPage(page);
        List<AeaProjInfo> list = aeaProjInfoMapper.listAeaProjInfo(aeaProjInfo);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaProjInfo> findAeaProjInfoByApplyLinkmanInfoId(String linkmanInfoId) {
        LOGGER.debug("根据申请人查询项目信息");
        return aeaProjInfoMapper.findAeaProjInfoByApplyLinkmanInfoId(linkmanInfoId);
    }

    @Override
    public List<AeaProjInfo> findRootAeaProjInfoByLinkmanInfoId(String linkmanInfoId,String keyword) {
        LOGGER.debug("根据项目联系人查询项目信息");
        List<AeaProjInfo> list = aeaProjInfoMapper.findRootAeaProjInfoByLinkmanInfoId(linkmanInfoId,keyword);
        return setThemeNameAndAgentFlag(list);
    }

    @Override
    public List<AeaProjInfo> findRootAeaProjInfoByUnitInfoId(String unitInfoId,String keyword) {
        List<AeaProjInfo> aeaProjInfos = aeaProjInfoMapper.findRootAeaProjInfoByUnitInfoId(unitInfoId,keyword);
        return setThemeNameAndAgentFlag(aeaProjInfos);
    }

    @Override
    public List<AeaProjInfo> findRootAeaProjInfoByLinkmanInfoIdAndUnitInfoId(String linkmanInfoId, String unitInfoId,String keyword) {
        List<AeaProjInfo> list = aeaProjInfoMapper.findRootAeaProjInfoByLinkmanInfoIdAndUnitInfoId(linkmanInfoId, unitInfoId,keyword);
        return setThemeNameAndAgentFlag(list);
    }
    @Autowired
    private AeaProjWindowService aeaProjWindowService;
    @Autowired
    private AeaProjApplyAgentService aeaProjApplyAgentService;

    private List<AeaProjInfo> setThemeNameAndAgentFlag(List<AeaProjInfo> list) {
        try {
            for (AeaProjInfo aeaProjInfo : list) {
                AeaParTheme aeaParTheme = aeaParThemeService.getAeaParThemeByThemeId(aeaProjInfo.getThemeId());
                if (aeaParTheme != null) aeaProjInfo.setThemeName(aeaParTheme.getThemeName());
                List<AeaServiceWindow> windows = aeaProjWindowService.listAeaServiceWindowByProjInfoId(aeaProjInfo.getProjInfoId());
                if(windows.size()>0){
                    aeaProjInfo.setIsAgentProj("1");
                    List<AeaProjApplyAgent> agentApplys=aeaProjApplyAgentService.listAeaProjApplyAgentByProjInfoId(aeaProjInfo.getProjInfoId());
                    aeaProjInfo.setProjAgentState(agentApplys.size()>0?agentApplys.get(0).getAgentApplyState(): AgencyState.WAIT_SIGNING.getValue());
                }else{
                    aeaProjInfo.setIsAgentProj("0");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    /**用于mysql下
     * 根据项目的id获取所在树的全部项目信息
     * @param projInfoId
     * @return
     */
    public List<AeaProjInfo> getListProjZtreeNodeMysql(String projInfoId) {
        if (org.springframework.util.StringUtils.isEmpty(projInfoId)) {
            return null;
        }
        List<AeaProjInfo> list = new ArrayList<>();
        String topProjId = "";
        AeaParentProj parentProj = aeaProjInfoMapper.getAeaParentProjByChildId(projInfoId);
        List<AeaParentProj> parentProjs = aeaProjInfoMapper.listAeaParentProj(new AeaParentProj());

        //当传入的是最顶层节点，直接从顶层遍历
        if (parentProj == null) {
            AeaProjInfo onlyAeaProjInfoById2 = aeaProjInfoMapper.getOnlyAeaProjInfoById(projInfoId);
            topProjId = onlyAeaProjInfoById2.getProjInfoId();
            list = getAllNode(topProjId, parentProjs);
        }

        //当传入的不是最顶层节点
        if (parentProj != null && !StringUtils.isEmpty(parentProj.getProjSeq())) {
            String[] projInfoIds = parentProj.getProjSeq().split(",");
            topProjId = projInfoIds[0];

            list = getAllNode(topProjId, parentProjs);//递归得到所有projInfo
        }
        if (list.size() == 0) {
            AeaProjInfo onlyAeaProjInfoById = aeaProjInfoMapper.getOnlyAeaProjInfoById(projInfoId);
            list.add(onlyAeaProjInfoById);
        }
        LOGGER.debug("成功执行查询项目树，查询结果为：{}", list);
        return list;
    }

    /**
     * 根据最顶层id查询所有的子节点
     *
     * @param topProjId
     * @param parentProjs
     * @return
     */
    public List<AeaProjInfo> getAllNode(String topProjId, List<AeaParentProj> parentProjs) {
        List<AeaParentProj> parentProjlist;
        parentProjlist = getAllTreeNode(parentProjs, topProjId);

        String[] strings = parentProjlist.stream().map(AeaParentProj::getChildProjId).toArray(String[]::new);
        String[] pIds = StringUtils.addStringToArray(strings, topProjId);
        return aeaProjInfoMapper.listAeaProjInfoByIds(pIds);
    }

    /**
     * 获取自身外的所有子节点
     *
     * @param parentProjs
     * @param topProjId
     * @return
     */
    private List<AeaParentProj> getAllTreeNode(List<AeaParentProj> parentProjs, String topProjId) {
        List<AeaParentProj> result = new ArrayList<>();
        List<AeaParentProj> children = hasChild(parentProjs, topProjId);
        if (children != null && children.size() > 0) {
            result.addAll(children);
            parentProjs.removeAll(children);
            for (AeaParentProj child : children) {
                result.addAll(getAllTreeNode(parentProjs, child.getChildProjId()));
            }
        }
        return result;
    }

    /**
     * 返回该节点于其子节点的关联关系
     *
     * @param parentProjs
     * @param topProjId
     * @return
     */
    private List<AeaParentProj> hasChild(List<AeaParentProj> parentProjs, String topProjId) {
        List<AeaParentProj> list = parentProjs.stream().filter(parentP -> parentP.getParentProjId().equalsIgnoreCase(topProjId)).collect(Collectors.toList());
        return list;
    }


    /**
     * 新增子项目---2019-01-30 xiaohutu
     *
     * @param aeaProjInfo
     * @param isSecond
     * @return
     */
    public AeaProjInfo addChildProjInfo(AeaProjInfo aeaProjInfo, Boolean isSecond) throws Exception {
        if (StringUtils.isNotBlank(aeaProjInfo.getParentProjId()) && StringUtils.isNotBlank(aeaProjInfo.getProjName())) {
            AeaProjInfo proj = new AeaProjInfo();
            proj.setProjName(aeaProjInfo.getProjName().trim());
            List<AeaProjInfo> checkNameProj = aeaProjInfoMapper.listAeaProjInfo(aeaProjInfo);
            //查询是否有重名的项目工程
            if (checkNameProj != null && checkNameProj.size() > 0) {
                return null;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            proj.setProjInfoId(aeaProjInfo.getParentProjId());
            AeaProjInfo parentProj = aeaProjInfoMapper.getOnlyAeaProjInfoById(aeaProjInfo.getParentProjId());
            if (null == parentProj) {
                return null;
            }

            String rootId = null;
            if (isSecond && !"r".equals(parentProj.getProjFlag())) {   //不存在root项目，则自动创建一个root项目。
                rootId = createRootProj(parentProj);

            } else if (isSecond && "r".equals(parentProj.getProjFlag())) {//存在root项目
                rootId = parentProj.getProjInfoId();
            } else if (!isSecond) {    //申报页面 右键按钮添加的子项目工程进这个判断
                String temprootId = this.getRootProjInfoId(parentProj.getProjInfoId());
                rootId = temprootId == null ? this.createRootProj(parentProj) : temprootId;
            }
            String rootOrgId = SecurityContext.getCurrentOrgId();
            String childGcbm;
            if (rootId.equals(aeaProjInfo.getParentProjId())) {//传进来的是根节点
//                throw new BaseRuntimeException(601, "root节点不能新增项目");
                return null;
            } else {
                //广东模式
//                String childGCBMCode = parentProj.getGcbm() + "-" + ("2".equals(aeaProjInfo.getStageFlag()) == true ? "1" : "2");
//                childGcbm = gdGcbmBscRuleCodeStrategy.generateCode(childGCBMCode, childGCBMCode, "工程编码", rootOrgId);

                //非广东模式
                childGcbm = gcbmBscRuleCodeStrategy.generateCode(parentProj.getLocalCode(), parentProj.getLocalCode(), "工程编码", rootOrgId);
            }

            if (childGcbm != null) {
                AeaProjInfo provideProjInfo = (!isSecond && !"r".equals(parentProj.getProjFlag())) ? parentProj :
                        //默认获取第一个备案项目。如果没有备案项目则返回null。如果存在多个备案项目挂在root下，后期实现指定一个备案项目信息给子项目工程。
                        this.getFirstRecordProjInfo(rootId, aeaProjInfo);// aeaProjInfo 只含有 parentId  和 projName foreignRemark
                if (provideProjInfo != null) {
                    BeanUtils.copyProperties(provideProjInfo, proj);
                }
                //新增子项目
                String childNodeId = UUID.randomUUID().toString();
                proj.setProjInfoId(childNodeId);
                proj.setLocalCode(parentProj.getLocalCode());
                proj.setGcbm(childGcbm);
                proj.setProjName(aeaProjInfo.getProjName().trim());
                proj.setForeignRemark(aeaProjInfo.getForeignRemark());
                proj.setProjFlag("c");  //r表示ROOT项目，p表示发改项目，c表示子项目或子子项目
                proj.setHaveDetail("1");
                proj.setIsDeleted("0");
                // 主题保持一致
                proj.setThemeId(parentProj.getThemeId());
                proj.setCreater(SecurityContext.getCurrentUserName());
                proj.setCreateTime(new Date());
                proj.setProjectCreateDate(dateFormat.format(new Date()));
                aeaProjInfoMapper.insertAeaProjInfo(proj);

                AeaParentProj parentProj1 = new AeaParentProj();
                parentProj1.setChildProjId(parentProj.getProjInfoId());
                List<AeaParentProj> aeaParentProjs = aeaProjInfoMapper.listAeaParentProj(parentProj1);
                if (aeaParentProjs.size() > 0) {
                    BeanUtils.copyProperties(aeaParentProjs.get(0), parentProj1);
                }
                //英文逗号分隔
                String seq = (!isSecond && !"r".equals(parentProj.getProjFlag())) ? parentProj1.getProjSeq() + "," + proj.getProjInfoId() : rootId + "," + proj.getProjInfoId();


                //增加父子项目关联
                AeaParentProj aeaParentProj = new AeaParentProj();
                aeaParentProj.setNodeProjId(UUID.randomUUID().toString());
                aeaParentProj.setParentProjId(parentProj.getProjInfoId());
                aeaParentProj.setChildProjId(proj.getProjInfoId());
                aeaParentProj.setCreater(SecurityContext.getCurrentUserName());
                aeaParentProj.setCreateTime(new Date());
                aeaParentProj.setProjSeq(seq);
                aeaProjInfoMapper.insertAeaParentProj(aeaParentProj);

                String currentUserId = SecurityContext.getCurrentUserId();
                if (provideProjInfo != null) {
                    List<AeaUnitProj> unitProjList = this.getAeaUnitProjByProjInfoId(provideProjInfo.getProjInfoId());       //获取项目的单位关系赋予新建的子项目
                    if (unitProjList != null && unitProjList.size() > 0) {
                        for (int i = 0; i < unitProjList.size(); i++) {
                            AeaUnitProj unitProj = unitProjList.get(i);
                            unitProj.setUnitProjId(UUID.randomUUID().toString());
                            unitProj.setProjInfoId(proj.getProjInfoId());
                            unitProj.setCreater(currentUserId);
                            unitProj.setCreateTime(new Date());
                            unitProj.setIsDeleted("0");
                            aeaUnitProjMapper.insertAeaUnitProj(unitProj);
                        }
                    }
                }
                return proj;
            }
        }
        return null;
    }

    /**
     * 合并子项目
     * 目前只能合并同级项目
     *
     * @param aeaProjInfo
     * @param projInfoIds
     * @return
     */
    @Deprecated
    public boolean mergeChildProjInfo(@NonNull AeaProjInfo aeaProjInfo, @NonNull String projInfoIds) {
        boolean result = true;
        if (StringUtils.isNotBlank(projInfoIds)) {
            String[] ids = projInfoIds.split(",");
            if (ids.length > 1) {
                //待合并项目信息可能会有多个，不一定是两个
                List<AeaProjInfo> mergeProjList = aeaProjInfoMapper.listAeaProjInfoByIds(ids);
                if (mergeProjList.size() > 1) {
                    //增加父子关联表后，parentProdId不能为空
                    String parentProjId = mergeProjList.get(0).getParentProjId();
                    String projSeq = mergeProjList.get(0).getProjSeq();
                    if (org.springframework.util.StringUtils.isEmpty(projSeq)) {
//                        resultForm.setMessage("序列不存在");
                        return false;
                    }

                    if (StringUtils.isBlank(parentProjId)) {
//                        resultForm.setMessage("root项目不能合并");
                        return false;
                    }
                    //查询root节点信息
//                    AeaProjInfo rootProjInfo = aeaProjInfoMapper.getRootByChildNode(mergeProjList.get(0).getProjInfoId());

                    String[] split = projSeq.split(",");
                    List<AeaProjInfo> aeaProjInfos = aeaProjInfoMapper.listAeaProjInfoByIds(split);
                    if (aeaProjInfos.size() == 0) {
//                        resultForm.setMessage("沒有查到待合并項目关联信息");
                        return false;
                    }
                    List<AeaProjInfo> rootProjs = aeaProjInfos.stream().filter(proj -> StringUtils.isNotBlank(proj.getProjFlag()) && "r".equalsIgnoreCase(proj.getProjFlag()))
                            .collect(Collectors.toList());
                    AeaProjInfo rootProjInfo = null;
                    if (rootProjs.size() == 1) {
                        rootProjInfo = rootProjs.get(0);
                    } else {
//                        resultForm.setMessage("查询到多个root信息");
                        return false;
                    }
                    Boolean flag = true;
                    for (AeaProjInfo projInfo : mergeProjList) {
                        //判断是否发改编码项目(不是自编码的项目)。
                        if (null != projInfo.getLocalCode() && !(projInfo.getLocalCode().contains("ZBM") || projInfo.getLocalCode().contains("#"))) {
//                            resultForm.setMessage("项目工程《" + projInfo.getProjName() + "》不支持合并");
                            flag = false;
                            break;
                        }
                        if (!parentProjId.equals(projInfo.getParentProjId())) {    //判断是否同级的项目工程
//                            resultForm.setMessage("目前不支持合并非同级项目工程");
                            flag = false;
                            break;
                        }
                    }

                    if (flag && StringUtils.isNotBlank(aeaProjInfo.getProjName())) {
                        //判断是否重名
                        AeaProjInfo proj = new AeaProjInfo();//合并后项目
                        proj.setProjName(aeaProjInfo.getProjName().trim());
                        List<AeaProjInfo> checkNameProj = aeaProjInfoMapper.listAeaProjInfo(proj);

                        if (checkNameProj.size() > 0) {
//                            resultForm.setMessage("项目工程名称已经存在");
                            return false;
                        } else {
                            //复制合并前的项目信息
                            BeanUtils.copyProperties(mergeProjList.get(0), proj);
                            proj.setProjName(aeaProjInfo.getProjName().trim());

                            //插入合并后项目
                            String merProjId = UUID.randomUUID().toString();
                            proj.setProjInfoId(merProjId);
                            proj.setForeignRemark(aeaProjInfo.getForeignRemark());
                            proj.setProjFlag("c");
                            proj.setHaveDetail("0");
                            proj.setProjectCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            proj.setCreater(SecurityContext.getCurrentUserName());
                            proj.setIsDeleted("0");
                            proj.setCreateTime(new Date());
                            aeaProjInfoMapper.insertAeaProjInfo(proj);//保存合并的项目
                            //插入父子关联表
                            AeaParentProj aeaParentProj = new AeaParentProj();
                            aeaParentProj.setChildProjId(parentProjId);
                            String nodeInfoId = UUID.randomUUID().toString();
                            List<AeaParentProj> aeaParentProjs = aeaProjInfoMapper.listAeaParentProj(aeaParentProj);
                            if (aeaParentProjs.size() == 0) {//没有查到，parentProjid ==rootId
                                aeaParentProj.setProjSeq(parentProjId + "," + nodeInfoId);
                            } else {
                                aeaParentProj.setProjSeq(aeaParentProjs.get(0).getProjSeq() + "," + merProjId);
                            }
                            aeaParentProj.setNodeProjId(nodeInfoId);
                            aeaParentProj.setParentProjId(parentProjId);
                            aeaParentProj.setChildProjId(merProjId);
                            aeaParentProj.setCreateTime(new Date());
                            aeaParentProj.setCreater(SecurityContext.getCurrentUserName());
                            aeaProjInfoMapper.insertAeaParentProj(aeaParentProj);

                            //将合并前的项目挂载到合并后的项目下
                            for (AeaProjInfo aeaProjInfo1 : mergeProjList) {
                                AeaParentProj aeaParentProj1 = new AeaParentProj();
                                aeaParentProj1.setParentProjId(merProjId);
                                aeaParentProj1.setChildProjId(aeaProjInfo1.getProjInfoId());
                                aeaParentProj1.setModifier(SecurityContext.getCurrentUserName());
                                aeaParentProj1.setModifyTime(new Date());
                                aeaParentProj1.setProjSeq(aeaProjInfo1.getProjSeq() + "," + aeaProjInfo1.getProjInfoId());
                                aeaProjInfoMapper.updateParentProjByChildId(aeaParentProj1);
                            }
                            /*resultForm.setSuccess(true);
                            resultForm.setMessage("合并项目工程成功");*/
                            return true;

                        }
                    }
                }
            }
        }


        return result;
    }


    /**
     * 删除子项目---root项目，同步的发改委项目，已申报的项目不能删除
     *
     * @param delProjId 待删除项目projInfoId
     * @return
     * @throws Exception
     */
    @Transactional
    public boolean deleteChildChildProj(String delProjId) throws Exception {
        if (StringUtils.isEmpty(delProjId)) {
            return false;
        }
        AeaProjInfo delProjInfo = aeaProjInfoMapper.getOnlyAeaProjInfoById(delProjId);//info 待删除项目
        //顶级项目工程不能删除  p备案项目不能删除
        if ("r".equals(delProjInfo.getProjFlag()) || StringUtils.isBlank(delProjInfo.getParentProjId()) || "p".equals(delProjInfo.getProjFlag())) {
//            resultForm.setMessage("1");
            return false;
        }
        //已申报项目不能删除
        List<AeaHiApplyinst> aeaHiApplyinstByProjInfoId = aeaHiApplyinstMapper.getAeaHiApplyinstByProjInfoId(delProjId);
        if (aeaHiApplyinstByProjInfoId.size() > 0) {
//            resultForm.setMessage("已申报项目不能删除");
            return false;
        }
        //查询被删除项目的父项目，如果删除项目下边有子项目，将子项目挂在在父项目上
        AeaProjInfo delProjParentInfo = aeaProjInfoMapper.getAeaProjInfoById(delProjInfo.getParentProjId());//待删除项目 父项目
        if ("r".equals(delProjParentInfo.getProjFlag())) {
//            resultForm.setMessage("1");
            return false;
        }
        //3 先更新序列: 删除子项目序列中 已删除项目ID；然后在删除项目，否则查出来的记录会缺少一条
        //3-1 根据删除id查询下属子节点
        List<AeaParentProj> allChildOfDelNode = new ArrayList<>();
        if (this.isCurrentMysql(databaseIdProvider, dataSource)) {
            List<AeaParentProj> parentProjs = aeaProjInfoMapper.listAeaParentProj(new AeaParentProj());
            allChildOfDelNode = getAllTreeNode(parentProjs, delProjId);
        } else {
            allChildOfDelNode = aeaProjInfoMapper.listAllChildOfNodeTree(delProjId);
        }
        //3-2 更新projSeq
        int updateNum = 0;
        if (allChildOfDelNode.size() > 0) {
            for (AeaParentProj parentProj : allChildOfDelNode) {
                String projSeq = parentProj.getProjSeq();
                if (StringUtils.isNotBlank(projSeq)) {
                    String newSeq = Arrays.stream(projSeq.split(",")).filter(s -> !s.equals(delProjId)).collect(Collectors.joining(","));
                    parentProj.setProjSeq(newSeq);
                    parentProj.setModifier(SecurityContext.getCurrentUserName());
                    parentProj.setModifyTime(new Date());
                    int i = aeaProjInfoMapper.updateProjSeqByChildId(parentProj);
                    updateNum += i;
                    LOGGER.debug("更新projSeq记录数：" + i);
                }

            }
        }
        LOGGER.debug("总更新projSeq记录数：" + updateNum);
        //删除关联表中项目
        AeaParentProj tempAeaParentProj = new AeaParentProj();
        tempAeaParentProj.setChildProjId(delProjId);
        int delNum = aeaProjInfoMapper.delParentProjByCondition(tempAeaParentProj);
        LOGGER.debug("删除父子关联表中记录数：" + delNum);

        //将删除项目下的子项目挂载到父项目下---即将父ID为删除项目id的  改为删除项目的父项目ID
        //1 根据删除项目ID查询子项目
        tempAeaParentProj.setChildProjId(null);
        tempAeaParentProj.setParentProjId(delProjId);
        List<AeaParentProj> aeaParentProjs = aeaProjInfoMapper.listAeaParentProj(tempAeaParentProj);
        String[] childProjIds = null;

        if (aeaParentProjs.size() > 0) {
            childProjIds = aeaParentProjs.parallelStream().map(AeaParentProj::getChildProjId).filter(x -> x != null).toArray(String[]::new);
        }
        //childProjIds 不为空就 更新父子关系
        if (childProjIds != null && childProjIds.length > 0) {
            int i1 = aeaProjInfoMapper.updateParentProjByChildProjIds(delProjParentInfo.getProjInfoId(), childProjIds);
            LOGGER.debug("更新父子关联表中父子关系记录数：" + i1);
        }
        //删除项目单位信息
        aeaUnitProjMapper.deleteAeaUnitProjByProjInfoId(delProjId);
        //删除项目--由于外检约束，需要先删除子记录
        aeaProjInfoMapper.physicsDeleteAeaProjInfoByProjInfoId(delProjId);
//        resultForm.setSuccess(true);
//        resultForm.setMessage("删除项目工程成功！");
        LOGGER.debug("删除子项目工程成功！！，ID为：{}", delProjId);
        return true;
    }

    //获取即将要生成的子项目
    public AeaProjInfo getChildProject(String projName, String projInfoId, String localCode, String gcbm,String stageFlag) throws Exception {
        String childGcbm = "";
        AeaProjInfo parentProjInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
        AeaProjInfo aeaProjInfo = new AeaProjInfo();
        aeaProjInfo.setLocalCode(localCode);
        //广东模式
                //String childGCBMCode = parentProjInfo.getGcbm() + "-" + stageFlag;
                //childGcbm = gdGcbmBscRuleCodeStrategy.generateCode(childGCBMCode, childGCBMCode, "工程编码", parentProjInfo.getRootOrgId());

        //非广东模式
        childGcbm = gcbmBscRuleCodeStrategy.generateCode(localCode, localCode, "工程编码", parentProjInfo.getRootOrgId());

        aeaProjInfo.setGcbm(childGcbm);
        aeaProjInfo.setProjName(projName);
        //返还新的aeaProjInfo,只有三个字段，localCode，gcbm和projName，其他都为null，localCode与父项目保持相同
        return aeaProjInfo;
    }

    @Override
    public List<String> getProjAddressRegion(String projInfoId) {
        ArrayList<String> regionIds = new ArrayList<>();
        AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
        if (aeaProjInfo == null || StringUtils.isBlank(aeaProjInfo.getProjectAddress())) {
            return regionIds;
        }
        /// projectAddress 改为了 regionId
        /*// bscDicRegionMapper为bsc里的代码， 无法增加根据regionNum批量查询方法，只能单个查 mmp
        String[] regionNum = aeaProjInfo.getProjectAddress().split(CommonConstant.COMMA_SEPARATOR);
        for (String num : regionNum) {
            BscDicRegion param = new BscDicRegion();
            param.setRegionNum(num);
            List<BscDicRegion> bscDicRegions = bscDicRegionMapper.listBscDicRegion(param);
            if (CollectionUtils.isNotEmpty(bscDicRegions)) {
                regionIds.add(bscDicRegions.get(0).getRegionId());
            }
        }*/
        regionIds.addAll(Arrays.asList(aeaProjInfo.getProjectAddress().split(CommonConstant.COMMA_SEPARATOR)));
        return regionIds;
    }

    @Override
    public AeaProjInfo addChildProjInfo(AeaProjInfo aeaProjInfo) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String parentProjId = aeaProjInfo.getParentProjId();
        if (StringUtils.isNotBlank(parentProjId) && StringUtils.isNotBlank(aeaProjInfo.getProjName())) {
            AeaProjInfo parentProj = aeaProjInfoMapper.getAeaProjInfoById(parentProjId);

            String rootOrgId = SecurityContext.getCurrentOrgId();
            String childGcbm = gcbmBscRuleCodeStrategy.generateCode(parentProj.getLocalCode(), parentProj.getLocalCode(), "工程编码", rootOrgId);
            if (childGcbm != null) {
                //新增子项目
                String childNodeId = UUID.randomUUID().toString();
                aeaProjInfo.setProjInfoId(childNodeId);
                aeaProjInfo.setLocalCode(parentProj.getLocalCode());
                aeaProjInfo.setGcbm(childGcbm);
                aeaProjInfo.setProjFlag("c");  //r表示ROOT项目，p表示发改项目，c表示子项目或子子项目
                aeaProjInfo.setHaveDetail("1");
                aeaProjInfo.setIsDeleted("0");
                // 主题保持一致
                aeaProjInfo.setThemeId(parentProj.getThemeId());
                aeaProjInfo.setCreater(SecurityContext.getCurrentUserName());
                aeaProjInfo.setCreateTime(new Date());
                aeaProjInfo.setProjectCreateDate(dateFormat.format(new Date()));
                aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);
                if (StringUtils.isBlank(aeaProjInfo.getFormId())) throw new Exception("缺少formId");
                this.formSave(aeaProjInfo.getFormId(), aeaProjInfo.getProjInfoId(), EDataOpt.INSERT.getOpareteType(), null);
                //增加父子项目关联
                AeaParentProj aeaParentProj = new AeaParentProj();
                aeaParentProj.setNodeProjId(UUID.randomUUID().toString());
                aeaParentProj.setParentProjId(parentProj.getProjInfoId());
                aeaParentProj.setChildProjId(aeaProjInfo.getProjInfoId());
                aeaParentProj.setCreater(SecurityContext.getCurrentUserName());
                aeaParentProj.setCreateTime(new Date());
                aeaProjInfoMapper.insertAeaParentProj(aeaParentProj);

                return aeaProjInfo;
            }
        }
        return null;
    }

    /**
     * 判断是数据库类型是否是mysql
     *
     * @param databaseIdProvider
     * @param dataSource
     * @return
     * @throws Exception
     */
    public boolean isCurrentMysql(DatabaseIdProvider databaseIdProvider, DataSource dataSource) throws Exception {
        String databaseId = databaseIdProvider.getDatabaseId(dataSource);
        return "mysql".equalsIgnoreCase(databaseId) ? true : false;
    }

    @Override
    public List<ZtreeNode> buildProjectTree(String projInfoId) {
        Assert.isTrue(StringUtils.isNotBlank(projInfoId), "projInfoId is null");

        List<ZtreeNode> projTree = new ArrayList<>();
        Map<String, String> child2Parent = new HashMap<>();

        // 构造树的所有项目
        List<AeaProjInfo> aeaProjInfos = getAllProjectOfTree(projInfoId, child2Parent);
        aeaProjInfos.forEach(proj -> {
            // root节点不显示
            if (!"r".equals(proj.getProjFlag())) {
                String pId = child2Parent.get(proj.getProjInfoId());
                projTree.add(buildTreeNode(proj.getProjInfoId(), proj.getProjName(), pId, true, false));
            }
        });

        return projTree;
    }

    @Override
    public List<AeaProjInfo> getProjListAndChildProjsByParent(String[] localCodes) {
        return aeaProjInfoMapper.getProjInfoByLocalCodes(localCodes);
    }

    @Override
    public List<AeaProjInfo> getAllProjectOfTree(String projInfoId, Map<String, String> child2Parent) {
        Set<String> projInfoIds = new HashSet<>();
        // 把自己加进去
        projInfoIds.add(projInfoId);
        // 递归查找父项目
        String childProjId = projInfoId;
        buildParentProjs(child2Parent, projInfoIds, childProjId);

        // 递归查找子项目
        List<String> pIds = new ArrayList<>();
        pIds.add(projInfoId);
        buildChildProjects(child2Parent, projInfoIds, pIds);

        // 构造树的所有项目
        List<AeaProjInfo> aeaProjInfos = aeaProjInfoMapper.listAeaProjInfoByIds(projInfoIds.toArray(new String[0]));
        return aeaProjInfos;
    }

    /**
     * 获取转换字段后的项目详情信息
     *
     * @param projInfoId 项目采购信息
     * @return AeaProjInfo
     * @throws Exception e
     */
    @Override
    public AeaProjInfo getTransProjInfoDetail(String projInfoId) throws Exception {
        LOGGER.debug("根据申请实例ID查询项目列表，申请实例ID：{}", projInfoId);
        String orgId = SecurityContext.getCurrentOrgId();
        List<BscDicCodeItem> xmlxCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_PROJECT_STEP", orgId);
        List<BscDicCodeItem> tzlxCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_TZLX", orgId);
        List<BscDicCodeItem> zjlyCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_ZJLY", orgId);
        List<BscDicCodeItem> tdlyCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_TDLY", orgId);
        List<BscDicCodeItem> gcflCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_GCFL", orgId);
        List<BscDicCodeItem> jsxzCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_NATURE", orgId);
        List<BscDicCodeItem> gbhyCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_GBHY", orgId);
        List<BscDicCodeItem> zdxmCodeList = bscDicCodeService.getActiveItemsByTypeCode("XM_PROJECT_LEVEL", orgId);
        AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
        if (aeaProjInfo == null) throw new Exception("can not find proj info");
        String projType = getValueByBscDIcCode(xmlxCodeList, aeaProjInfo.getProjType());
        aeaProjInfo.setProjType(projType);
        String investType = getValueByBscDIcCode(tzlxCodeList, aeaProjInfo.getInvestType());
        aeaProjInfo.setInvestType(investType);
        String zjly = getValueByBscDIcCode(zjlyCodeList, aeaProjInfo.getFinancialSource());
        aeaProjInfo.setFinancialSource(zjly);
        String tdly = getValueByBscDIcCode(tdlyCodeList, aeaProjInfo.getLandSource());
        aeaProjInfo.setLandSource(tdly);
        String gcfl = getValueByBscDIcCode(gcflCodeList, aeaProjInfo.getProjCategory());
        aeaProjInfo.setProjCategory(gcfl);
        String jsxz = getValueByBscDIcCode(jsxzCodeList, aeaProjInfo.getProjNature());
        aeaProjInfo.setProjNature(jsxz);
        String gbhy = getValueByBscDIcCode(gbhyCodeList, aeaProjInfo.getTheIndustry());
        aeaProjInfo.setTheIndustry(gbhy);
        String zdxm = getValueByBscDIcCode(zdxmCodeList, aeaProjInfo.getProjLevel());
        aeaProjInfo.setProjLevel(zdxm);
        aeaProjInfo.setIsForeign("1".equals(aeaProjInfo.getIsForeign()) ? "是" : "否");
        aeaProjInfo.setIsDesignSolution("1".equals(aeaProjInfo.getIsDesignSolution()) ? "是" : "否");
        aeaProjInfo.setIsAreaEstimate("1".equals(aeaProjInfo.getIsAreaEstimate()) ? "是" : "否");
        String regionalism = aeaProjInfo.getRegionalism();
        BscDicRegion bscDicRegion = bscDicRegionMapper.getBscDicRegionById(regionalism);
        if (bscDicRegion != null) {
            aeaProjInfo.setRegionalism(bscDicRegion.getRegionName());
        }
        String projectAddress = aeaProjInfo.getProjectAddress();
        if (StringUtils.isNotBlank(projectAddress)) {
            String[] split = projectAddress.split(",");
            String result = "";
            for (String str : split) {
                BscDicRegion addrRegion = bscDicRegionMapper.getBscDicRegionById(str);
                if (addrRegion != null) {
                    result += addrRegion.getRegionName() + ",";
                }
            }
            if (result.length() > 0) {
                result = result.substring(0, result.length() - 1);
            }
            aeaProjInfo.setProjectAddress(result);
        }
        try {
            AeaParTheme theme = aeaParThemeService.getAeaParThemeByThemeId(aeaProjInfo.getThemeId());
            aeaProjInfo.setThemeName(Optional.ofNullable(theme).orElse(new AeaParTheme()).getThemeName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aeaProjInfo;
    }


    private void buildChildProjects(Map<String, String> child2Parent, Set<String> projInfoIds, List<String> pIds) {
        List<AeaParentProj> childProjs;
        do {
            childProjs = aeaParentProjMapper.listChildProjByProjInfoIds(pIds);
            pIds.clear();
            if (childProjs.size() > 0) {
                childProjs.forEach(child -> {
                    String pId = child.getParentProjId();
                    String cId = child.getChildProjId();
                    projInfoIds.add(cId);
                    child2Parent.put(cId, pId);
                    pIds.add(cId);
                });
            }
        } while (childProjs.size() > 0);
    }

    private void buildParentProjs(Map<String, String> child2Parent, Set<String> projInfoIds, String childProjId) {
        AeaParentProj parentProj;
        do {
            parentProj = aeaParentProjMapper.getParentProjByProjInfoId(childProjId);
            if (parentProj != null) {
                String parentProjId = parentProj.getParentProjId();
                projInfoIds.add(parentProjId);
                child2Parent.put(childProjId, parentProjId);
                childProjId = parentProjId;
            }
        } while (parentProj != null);
    }

    public ZtreeNode buildTreeNode(String id, String name, String pId, Boolean open, Boolean isParent) {
        ZtreeNode ztreeNode = new ZtreeNode();
        ztreeNode.setId(id);
        ztreeNode.setName(name);
        ztreeNode.setpId(pId);
        ztreeNode.setpName("");
        ztreeNode.setOpen(open);
        ztreeNode.setIsParent(isParent);
        ztreeNode.setIconSkin("");
        ztreeNode.setIsHorizontal(false);
        ztreeNode.setType("");
        ztreeNode.setNocheck(false);
        ztreeNode.setIcon("");
        return ztreeNode;
    }

    public AeaProjInfo getFirstRecordProjInfo(String rootId, AeaProjInfo aeaProjInfo) {//aeaProjInfo 只含有 parentId  和 projName foreignRemark
        AeaProjInfo result = null;
        List<AeaProjInfo> projInfos = getAllProjectOfTree(rootId, new HashMap<>());//包括root节点，及兄弟节点
        for (AeaProjInfo aeaProj : projInfos) {
            if ("p".equals(aeaProj.getProjFlag())) {
                result = aeaProj;
                break;
            }
        }
        return result;
    }

    public List<AeaUnitProj> getAeaUnitProjByProjInfoId(String projInfoId) throws Exception {
        if (org.apache.commons.lang3.StringUtils.isNotBlank(projInfoId)) {
            AeaUnitProj aeaUnitProj = new AeaUnitProj();
            aeaUnitProj.setProjInfoId(projInfoId);
            List<AeaUnitProj> aeaUnitProjs = aeaUnitProjMapper.listAeaUnitProj(aeaUnitProj);
            return aeaUnitProjs;
        }
        return null;
    }

    private String createRootProj(AeaProjInfo proj) {
        String rootId = null;
        if (proj != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String num = String.valueOf(date.getTime());
            AeaProjInfo root = new AeaProjInfo();
            BeanUtils.copyProperties(proj, root);
            rootId = UUID.randomUUID().toString();
            //zbm表示自编码，r表示root，日期加8位数字（当前时间毫秒值截取后8位）例如：ZBM-R-20181203-56899466
            String code = "ZBM-R-" + sdf.format(date) + "-" + num.substring(num.length() - 8);
            root.setProjInfoId(rootId);
            //root項目名称拷贝第一个项目的名称
            root.setProjName(proj.getProjName());
            root.setLocalCode(code);
            root.setProjFlag("r");
            root.setHaveDetail("0");
            root.setCreater(SecurityContext.getCurrentUserName());
            root.setCreateTime(date);
            root.setProjectCreateDate(dateFormat.format(new Date()));
            //root项目不生成序列
            aeaProjInfoMapper.insertAeaProjInfo(root);

            //插入AEA_PARENT_PROJ  parentProj subParent
            AeaParentProj aeaParentProj = new AeaParentProj();
            String nodeProjId = UUID.randomUUID().toString();
            aeaParentProj.setNodeProjId(nodeProjId);
            aeaParentProj.setParentProjId(rootId);
            aeaParentProj.setChildProjId(proj.getProjInfoId());
            aeaParentProj.setCreater(SecurityContext.getCurrentUserName());
            aeaParentProj.setCreateTime(new Date());
            aeaParentProj.setProjSeq(rootId + "," + proj.getProjInfoId());
            aeaProjInfoMapper.insertAeaParentProj(aeaParentProj);
            if (!proj.getLocalCode().contains("ZB")) {
                proj.setProjFlag("p");
            }
            proj.setHaveDetail("1");
            aeaProjInfoMapper.updateAeaProjInfo(proj);
        }
        return rootId;
    }

    public String getRootProjInfoId(String projInfoId) {
        String rootId = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(projInfoId)) {
            //需要修改为冲AEA_PARENT_PROJ查询
            //添加根据当前数据库类型进行查询
            String databaseId = null;
            List<AeaProjInfo> projInfos = new ArrayList<>();
            try {
                databaseId = databaseIdProvider.getDatabaseId(dataSource);
                if ("mysql".equalsIgnoreCase(databaseId)) {
                    List<AeaParentProj> parentProjs = aeaProjInfoMapper.listAeaParentProj(new AeaParentProj());
                    List<AeaParentProj> result = recursiveGetParentProj(parentProjs, projInfoId);
                    if (result != null && result.size() > 0) {
                        /*List<String> ids = result.stream().map(p->p.getParentProjId()).collect(Collectors.toList());
                        ids.add(projInfoId);//包含自身节点
                        String[] idsArr = new String[ids.size()];
                        ids.toArray(idsArr);
*/
                        //用下面一句代替,等等所有父级不包括自身节点
                        String[] idsArr = result.stream().map(AeaParentProj::getParentProjId).toArray(String[]::new);
                        projInfos = aeaProjInfoMapper.listAeaProjInfoByIds(idsArr);
                    }

                } else {
                    projInfos = aeaProjInfoMapper.listAllParentOfNode(projInfoId);
                }
                if (projInfos.size() > 0) {
                    for (AeaProjInfo aeaProjInfo : projInfos) {
                        if ("r".equalsIgnoreCase(aeaProjInfo.getProjFlag())) {
                            rootId = aeaProjInfo.getProjInfoId();
                            break;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rootId;
    }


    private List<AeaParentProj> recursiveGetParentProj(List<AeaParentProj> parentProjs, String projInfoId) {
        List<AeaParentProj> result = new ArrayList<>();
        List<AeaParentProj> tag = parentProjs.stream().filter(parentP -> parentP.getChildProjId().equalsIgnoreCase(projInfoId)).collect(Collectors.toList());
        if (tag.size() == 0) {
            return result;
        } else {
            result.addAll(tag);
            List<AeaParentProj> tag2 = recursiveGetParentProj(parentProjs, tag.get(0).getParentProjId());
            result.addAll(tag2);
            return result;
        }
    }

    @Override
    public FormDataOptResult doformSave(String formId, String metaTableId, Integer opType, Object dataEntity) throws Exception {
        FormDataOptResult result = new FormDataOptResult();
        result.setSuccess(true);
        ActStoForminst actStoForminst = new ActStoForminst();
        actStoForminst.setFormId(formId);
        actStoForminst.setFormPrimaryKey(metaTableId);
        result.setActStoForminst(actStoForminst);
        result.setDataOpt(EDataOpt.INSERT);
        return result;
    }

    @Override
    public FormDataOptResult doformDelete(ActStoForm formVo, Object dataEntity) throws Exception {
        return null;
    }
}
