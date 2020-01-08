package com.augurit.aplanmis.front.subject.project.service;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.att.service.IBscAttService;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpusLoginUser;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.agcloud.opus.common.service.om.OpuOmOrgService;
import com.augurit.aplanmis.common.constants.DicConstants;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.vo.AeaProjInfoVo;
import com.augurit.aplanmis.common.vo.conditional.ConditionalQueryAeaProjInfo;
import com.augurit.aplanmis.front.subject.unit.service.GlobalApplicantService;
import com.augurit.aplanmis.thirdPlatform.service.ProjectCodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

//import com.augurit.aplanmis.front.window.vo.AeaProjInfoVo;

/**
 * 项目库信息-Service服务接口实现类
 */
@Service
@Transactional
public class GlobalProjService {

    private static Logger logger = LoggerFactory.getLogger(GlobalProjService.class);

    @Autowired
    private GlobalApplicantService globalApplicantService;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Autowired
    protected IBscAttService bscAtService;

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private OpuOmOrgService opuOmOrgService;

    @Autowired
    private AeaProjInfoService aeaProjInfoService;

    @Autowired
    OpuOmOrgMapper opuOmOrgMapper;

    @Autowired
    private ProjectCodeService projectCodeService;

    @Autowired
    AeaServiceWindowMapper aeaServiceWindowMapper;

    @Autowired
    AeaProjWindowMapper aeaProjWindowMapper;

    public String saveAeaProjInfo(AeaProjInfo aeaProjInfo, List<AeaUnitInfo> buildUnitList, List<AeaUnitInfo> agencyUnitList) throws Exception {
        if (StringUtils.isNotBlank(aeaProjInfo.getProjInfoId()))
            aeaProjInfoService.updateAeaProjInfo(aeaProjInfo);// 更新项目信息
        else
            aeaProjInfoService.insertAeaProjInfo(aeaProjInfo);// 插入项目信息

        //删除与此项目相关的全部单位关联关系
        //aeaUnitProjMapper.batchDeleteAllUnitProjByProjInfoId(aeaProjInfo.getProjInfoId());  //由于加了外键，没法删除数据
        saveUnitList(buildUnitList, aeaProjInfo.getProjInfoId(), "1");
        saveUnitList(agencyUnitList, aeaProjInfo.getProjInfoId(), "0");

        return aeaProjInfo.getProjInfoId();
    }

    /**
     * 通过业主单位id获取项目信息
     */
    public List<AeaProjInfo> getAeaProjInfoByUnitId(String unitInfoId) {
        List<AeaProjInfo> aeaProjInfos = new ArrayList<>();
        return aeaProjInfos;
    }

    public AeaProjInfo getOnlyAeaProjInfoById(String id) {
        AeaProjInfo onlyAeaProjInfoById = aeaProjInfoMapper.getOnlyAeaProjInfoById(id);
        return onlyAeaProjInfoById;
    }

    public void updateAeaProjInfo(AeaProjInfo aeaProjInfo) {
        aeaProjInfoMapper.updateAeaProjInfo(aeaProjInfo);
    }

    public ResultForm listAeaProjInfo(String keyword, Page page) throws Exception {

        PageHelper.startPage(page);
        List<AeaProjInfo> list = aeaProjInfoMapper.findAeaProjInfoByKeyword(keyword,SecurityContext.getCurrentOrgId());
        logger.debug("成功执行分页查询！！");
//        if (list != null && list.size() > 0) {
//            for (AeaProjInfo proj : list) {
//                String isChildProj = org.apache.commons.lang3.StringUtils.isNotBlank(proj.getParentProjId()) ? "0" : "1";//判断是否子项目
//                proj.setIsChildProj(isChildProj);
//            }
//        }

        //对立项类型进行name替换code值
        OpuOmOrg topOrg = opuOmOrgService.getTopOrgByCurOrgId(SecurityContext.getCurrentOrgId());

        if (list.size() > 0){
            //立项类型
            List<BscDicCodeItem> dicCodeList = bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_STEP, topOrg.getOrgId());
            if (dicCodeList.size() > 0){
                for (AeaProjInfo aeaProjInfo : list) {
                    for (BscDicCodeItem dicCodeItem : dicCodeList) {
                        if (dicCodeItem.getItemCode().equalsIgnoreCase(aeaProjInfo.getProjType())) {
                            aeaProjInfo.setProjType(dicCodeItem.getItemName());
                            continue;
                        }
                    }
                }
            }

            //建设性质
            List<BscDicCodeItem> natureDicCodeList = bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_NATURE, topOrg.getOrgId());
            if (natureDicCodeList.size() > 0){
                for (AeaProjInfo aeaProjInfo : list) {
                    for (BscDicCodeItem dicCodeItem : natureDicCodeList) {
                        if (dicCodeItem.getItemCode().equalsIgnoreCase(aeaProjInfo.getProjNature())) {
                            aeaProjInfo.setProjNatureStr(dicCodeItem.getItemName());
                            continue;
                        }
                    }
                }
            }

            //重点工程
            List<BscDicCodeItem> projLevelDicCodeList = bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_LEVEL, topOrg.getOrgId());
            if (projLevelDicCodeList.size() > 0){
                for (AeaProjInfo aeaProjInfo : list) {
                    for (BscDicCodeItem dicCodeItem : projLevelDicCodeList) {
                        if (dicCodeItem.getItemCode().equalsIgnoreCase(aeaProjInfo.getProjLevel())) {
                            aeaProjInfo.setProjLevel(dicCodeItem.getItemName());
                            continue;
                        }
                    }
                }
            }
        }
        PageInfo<AeaProjInfo> pageInfo = new PageInfo(list);
        return new ContentResultForm<>(true, pageInfo);
    }

    public AeaProjInfo getAeaProjInfoById(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException(id);
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaProjInfoMapper.getAeaProjInfoById(id);
    }

    public List<AeaProjInfo> listAeaProjInfo(AeaProjInfo aeaProjInfo) {

        List<AeaProjInfo> list = aeaProjInfoMapper.listAeaProjInfo(aeaProjInfo);
        logger.debug("成功执行查询list！！");
        return list;
    }

    private boolean deleteAttachmentRecordAndFileByDetailId(String atPath, String atDiskName, boolean isAbsoluteFilePath) throws Exception {
        boolean result = false;
        if (atPath != null && atPath.trim().length() > 0 && atDiskName != null && atDiskName.trim().length() > 0) {
            atPath = atPath.replace('/', '\\');
            String filePath = isAbsoluteFilePath ? atPath : this.getRequest().getSession().getServletContext().getRealPath("\\");
            String wholeFilePath = filePath + "\\" + atDiskName;
            return this.deletePhysicalFile(wholeFilePath).isSuccess();
        } else {
            return result;
        }
    }

    private ResultForm deletePhysicalFile(String wholeFilePath) {
        boolean result = false;
        new ResultForm(result, "");

        ResultForm resultForm;
        try {
            File file = new File(wholeFilePath);
            if (file.exists()) {
                result = file.delete();
                resultForm = new ResultForm(result, "实际文件被删除!");
            } else {
                resultForm = new ResultForm(true, "实际文件已经不存在!");
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            resultForm = new ResultForm(false, var5.getMessage());
        }

        return resultForm;
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    private void deleteMat(String detailId) throws Exception {
        BscAttDetail form = this.bscAtService.getAttDetailByDetailId(detailId);
        if (form != null) {
            boolean success = false;
            if (!org.apache.commons.lang3.StringUtils.isEmpty(form.getAttPath())) {
                success = this.deleteAttachmentRecordAndFileByDetailId(form.getAttPath(), form.getAttCode() + "." + form.getAttFormat(), true);
            } else {
                success = true;
            }
            if (success && UploadType.DATABASE.getValue().equals(form.getStoreType())) {
                this.bscAtService.deleteAttDetailCascadeByDetailId(form.getDetailId());
            }
        }
    }

    public List<AeaLinkmanInfo> listDgLinkmanInfoByunitInfoId(String unitInfoId) throws Exception {
        List<AeaLinkmanInfo> results = new ArrayList<>();
        AeaLinkmanInfo query = new AeaLinkmanInfo();
        query.setUnitInfoId(unitInfoId);
        List<AeaLinkmanInfo> list = aeaLinkmanInfoMapper.listAeaLinkmanInfo(query);
        if (list != null && list.size() > 0) {
            results = list.parallelStream().filter(info -> !"1".equals(info.getIsDeleted())).collect(Collectors.toList());
            /*for (AeaLinkmanInfo linkmanInfo:list){
                if("1".equals(linkmanInfo.getIsDeleted())){
                    continue;
                }
                results.add(linkmanInfo);
            }*/
        }
        return results;
    }
//    /**
//     * 查询某条申报数据下的所有项目
//     */
//    public List<AeaProjInfo> getAeaProjInfoListByApplyinstId(String applyinstId) throws Exception{
//        return aeaProjInfoMapper.getAeaProjInfoListByApplyinstId(applyinstId);
//    }


    /*
     *进行对应单位更新或新增的操作
     * */
    public String beforeSaveUnitList(List<AeaUnitInfo> buildUnitList, List<AeaUnitInfo> agencyUnitList) throws Exception {
        for (AeaUnitInfo aeaUnitInfo : buildUnitList) {
            List<AeaUnitInfo> listAeaUnitInfos = aeaUnitInfoService.getUnitInfoListByIdCard(aeaUnitInfo.getUnifiedSocialCreditCode());
            if (listAeaUnitInfos.size() > 0 && !listAeaUnitInfos.get(0).getUnitInfoId().equalsIgnoreCase(aeaUnitInfo.getUnitInfoId()))
                throw new Exception("建设单位" + aeaUnitInfo.getApplicant() + "的证件号已注册！");
        }
        for (AeaUnitInfo aeaUnitInfo : agencyUnitList) {
            List<AeaUnitInfo> listAeaUnitInfos = aeaUnitInfoService.getUnitInfoListByIdCard(aeaUnitInfo.getUnifiedSocialCreditCode());
            if (listAeaUnitInfos.size() > 0 && !listAeaUnitInfos.get(0).getUnitInfoId().equalsIgnoreCase(aeaUnitInfo.getUnitInfoId()))
                throw new Exception("在办单位" + aeaUnitInfo.getApplicant() + "的证件号已注册！");
        }
        return "";
    }

    /*
     * 保存项目信息对应的单位信息
     * */
    public void saveUnitList(List<AeaUnitInfo> aeaUnitInfoList, String projInfoId, String isOwner) throws Exception {
        for (AeaUnitInfo aeaUnitInfo : aeaUnitInfoList) {
            if (StringUtils.isBlank(aeaUnitInfo.getApplicant()))
                continue;//单位名为空，直接跳过
            String aeaUnitInfoId = "";
            //单位证照号码唯一性已经验证过，直接进行操作
            aeaUnitInfoId = saveAeaUnitInfo(aeaUnitInfo, isOwner);//保存单位信息
            if (StringUtils.isNotBlank(aeaUnitInfoId)) {
                AeaUnitProj queryResult = aeaUnitProjMapper.findUnitPorojByProjInfoIdAndUnitInfoId(projInfoId, aeaUnitInfoId, isOwner);
                if (queryResult == null) {
                    if (isOwner.equalsIgnoreCase("1"))
                        aeaUnitInfoService.insertOwnerUnitProj(projInfoId, aeaUnitInfoId);
                    if (isOwner.equalsIgnoreCase("0"))
                        aeaUnitInfoService.insertNonOwnerUnitProj(projInfoId, aeaUnitInfoId);
                }
            }
            if (StringUtils.isNotBlank(aeaUnitInfo.getLinkmanName())) {//对选定的联系人操作，姓名为空直接跳过
                saveAeaLinkmanInfo(aeaUnitInfo, aeaUnitInfoId);
            }
        }
    }

    /*
     *进行对应单位更新或新增的操作
     *此时已经对单位证照好唯一性进行过验证
     * */
    public String saveAeaUnitInfo(AeaUnitInfo aeaUnitInfo, String isOwner) throws Exception {

        if (StringUtils.isBlank(aeaUnitInfo.getUnitInfoId()) && StringUtils.isBlank(aeaUnitInfo.getApplicant()))
            return "";//单位名称和证照号同时为空，不进行操作
        if (StringUtils.isBlank(aeaUnitInfo.getUnitInfoId())) {
            aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
            aeaUnitInfo.setCreater(SecurityContext.getCurrentUserName());
            aeaUnitInfo.setCreateTime(new Date());
            aeaUnitInfo.setIsDeleted("0");
            aeaUnitInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaUnitInfoMapper.insertAeaUnitInfo(aeaUnitInfo);
        } else {
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
        return aeaUnitInfo.getUnitInfoId();
    }

    /*
     *进行对应单位选择的联系人，进行更新或新增的操作,并进行联系人-单位的关联关系插入
     * */
    public String saveAeaLinkmanInfo(AeaUnitInfo aeaUnitInfo, String aeaUnitInfoId) throws Exception {
        if (StringUtils.isBlank(aeaUnitInfo.getLinkmanInfoId()) && StringUtils.isBlank(aeaUnitInfo.getLinkmanCertNo()))
            return "";//联系人名称和证件号同时为空，不进行操作
        AeaLinkmanInfo aeaLinkmanInfo = new AeaLinkmanInfo();
        aeaLinkmanInfo.setLinkmanName(aeaUnitInfo.getLinkmanName());
        aeaLinkmanInfo.setLinkmanMobilePhone(aeaUnitInfo.getLinkmanMobilePhone());
        aeaLinkmanInfo.setLinkmanCertNo(aeaUnitInfo.getLinkmanCertNo());
        aeaLinkmanInfo.setLinkmanMail(aeaUnitInfo.getLinkmanMail());

        List<AeaLinkmanInfo> links = aeaLinkmanInfoService.getAeaLinkmanInfoListByCertNo(aeaUnitInfo.getLinkmanCertNo());//   判断证件号的唯一性
        if (links.size() > 0) {//证件号有重复
            if (StringUtils.isBlank(aeaUnitInfo.getLinkmanInfoId())) {//未传入联系人id，则对已查到首个联系人进行更新操作
                aeaLinkmanInfo.setLinkmanCertNo(aeaUnitInfo.getLinkmanCertNo());
                aeaLinkmanInfo.setLinkmanInfoId(links.get(0).getLinkmanInfoId());
            }
            // 若传入的有id，因为证件号存在重复，故直接跳过证件号，不对此id的证件号进行操作
        } else {//表示证件号唯一
            aeaLinkmanInfo.setLinkmanCertNo(aeaUnitInfo.getLinkmanCertNo());
        }

        if (StringUtils.isNotBlank(aeaUnitInfo.getLinkmanInfoId())) {//包括通过证件号查到的人，也只进行更新操作
            if (StringUtils.isBlank(aeaLinkmanInfo.getLinkmanType())) {
                aeaLinkmanInfo.setLinkmanType("c");
            }
            aeaLinkmanInfo.setLinkmanInfoId(aeaUnitInfo.getLinkmanInfoId());
            aeaLinkmanInfo.setModifyTime(new Date());
            aeaLinkmanInfoService.updateAeaLinkmanInfo(aeaLinkmanInfo);
        } else {//新增联系人
            aeaLinkmanInfo.setLinkmanType("c");
            aeaLinkmanInfoService.insertAeaLinkmanInfo(aeaLinkmanInfo);
            aeaLinkmanInfoService.insertUnitLinkman(aeaUnitInfoId, aeaLinkmanInfo.getLinkmanInfoId());//新增联系人，插入联系人与单位的关联关系
        }
        return aeaLinkmanInfo.getLinkmanInfoId();
    }

    public ResultForm conditionalQueryAeaProjInfo(ConditionalQueryAeaProjInfo conditionalQueryAeaProjInfo, Page page) throws Exception {

        OpusLoginUser opusLoginUser = SecurityContext.getOpusLoginUser();
        conditionalQueryAeaProjInfo.setRootOrgId(opusLoginUser.getCurrentOrgId());
        conditionalQueryAeaProjInfo.setUserId(opusLoginUser.getUser().getUserId());

        if(conditionalQueryAeaProjInfo.isOnlyRegion()){

            List<String> selfAndChildRegionIds = aeaProjInfoMapper.listBelongRegionAndChildRegionsByUserId(opusLoginUser.getUser().getUserId());
            if(selfAndChildRegionIds==null || selfAndChildRegionIds.size()==0){
                throw new RuntimeException("查询不到用户所属的行政区划");
            }
            conditionalQueryAeaProjInfo.setSelfAndChildRegionIds(selfAndChildRegionIds);
        }

        if(conditionalQueryAeaProjInfo.isHandler()){
            conditionalQueryAeaProjInfo.setLoginName(opusLoginUser.getUser().getLoginName());
        }

        if(conditionalQueryAeaProjInfo.isOnlyOrg()){

            List<OpuOmOrg> opuOmOrgList = opuOmOrgMapper.listBelongOrgByUserId(opusLoginUser.getUser().getUserId());

            Set<String> currentUserOrgIdList = new HashSet<>();
            Set<String> selfAndParentOrgIdList = new HashSet<>();
            for(OpuOmOrg opuOmOrg: opuOmOrgList){
                currentUserOrgIdList.add(opuOmOrg.getOrgId());
                selfAndParentOrgIdList.add(opuOmOrg.getOrgId());
                if(StringUtils.isNotBlank(opuOmOrg.getOrgSeq())){
                    String[] orgIds = opuOmOrg.getOrgSeq().split(".");
                    for(String id:orgIds){
                        if(StringUtils.isNotBlank(id)){
                            selfAndParentOrgIdList.add(id);
                        }
                    }
                }
            }
            conditionalQueryAeaProjInfo.setCurrentUserOrgIdList(currentUserOrgIdList);
            conditionalQueryAeaProjInfo.setSelfAndParentOrgIdList(selfAndParentOrgIdList);

        }

        PageHelper.startPage(page);

        List<AeaProjInfoVo> list = aeaProjInfoMapper.conditionalQueryAeaProjInfo(conditionalQueryAeaProjInfo);

        if (list.size() > 0){
            //立项类型
            List<BscDicCodeItem> dicCodeList = bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_STEP, conditionalQueryAeaProjInfo.getRootOrgId());
            if (dicCodeList.size() > 0){
                for (AeaProjInfoVo aeaProjInfo : list) {
                    for (BscDicCodeItem dicCodeItem : dicCodeList) {
                        if (dicCodeItem.getItemCode().equalsIgnoreCase(aeaProjInfo.getProjType())) {
                            aeaProjInfo.setProjTypeText(dicCodeItem.getItemName());
                            break;
                        }
                    }
                }
            }

            //建设性质
            List<BscDicCodeItem> natureDicCodeList = bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_NATURE, conditionalQueryAeaProjInfo.getRootOrgId());
            if (natureDicCodeList.size() > 0){
                for (AeaProjInfoVo aeaProjInfo : list) {
                    for (BscDicCodeItem dicCodeItem : natureDicCodeList) {
                        if (dicCodeItem.getItemCode().equalsIgnoreCase(aeaProjInfo.getProjNature())) {
                            aeaProjInfo.setProjNatureText(dicCodeItem.getItemName());
                            break;
                        }
                    }
                }
            }

            //重点工程
            List<BscDicCodeItem> projLevelDicCodeList = bscDicCodeService.getActiveItemsByTypeCode(DicConstants.XM_PROJECT_LEVEL, conditionalQueryAeaProjInfo.getRootOrgId());
            if (projLevelDicCodeList.size() > 0){
                for (AeaProjInfoVo aeaProjInfo : list) {
                    for (BscDicCodeItem dicCodeItem : projLevelDicCodeList) {
                        if (dicCodeItem.getItemCode().equalsIgnoreCase(aeaProjInfo.getProjLevel())) {
                            aeaProjInfo.setProjLevelText(dicCodeItem.getItemName());
                            break;
                        }
                    }
                }
            }
        }
        PageInfo<AeaProjInfoVo> pageInfo = new PageInfo(list);
        return new ContentResultForm<>(true, pageInfo);
    }

    public List<AeaProjInfoVo> listChildProjInfoByProjInfoId(String projInfoId){
        return aeaProjInfoMapper.listChildProjInfoByProjInfoId(projInfoId);
    }

    public void batchDeleteProjInfo(String projInfoIds){
        if(StringUtils.isNotBlank(projInfoIds)){
            String[] ids = projInfoIds.split(",");
            if(ids!=null && ids.length>0){
                aeaProjInfoMapper.batchDeleteProjInfoAndChildProjInfo(ids);
            }
        }
    }

    /**
     * 导入项目关联代办中心
     * @param aeaProjInfo
     * @return
     */
    public ResultForm handleAgentPorjRelation(AeaProjInfo aeaProjInfo) throws Exception{
        ResultForm resultForm = new ResultForm(true);
        try {
            String localCode = aeaProjInfo.getLocalCode();
            if(StringUtils.isBlank(localCode)){
                resultForm.setSuccess(false);
                resultForm.setMessage("项目代码不能为空！");
                return resultForm;
            }
            localCode = handleUninterruptedSpaces(localCode);
            //先查询本地数据库
            AeaProjInfo proj = aeaProjInfoMapper.getNotChildrenAndNotRootAeaProjInfoByLocalCode(localCode);
            if(proj == null){
                List<AeaProjInfo> projInfos = projectCodeService.getProjInfoFromThirdPlatform(localCode, null, null);
                if(projInfos != null && projInfos.size() > 0){
                    proj = projInfos.get(0);
                }
            }
            if(proj == null){
                resultForm.setSuccess(false);
                resultForm.setMessage("该项目代码没有找到项目信息！");
                return resultForm;
            }
            if(StringUtils.isBlank(aeaProjInfo.getProjName())){
                resultForm.setSuccess(false);
                resultForm.setMessage("项目名称不能为空！");
                return resultForm;
            }
            //比较项目名称是否一致
            if(!proj.getProjName().equals(aeaProjInfo.getProjName().trim())){
                resultForm.setSuccess(false);
                resultForm.setMessage("项目名称不一致！");
                return resultForm;
            }
            //查询代办中心是否存在
            String agentName = aeaProjInfo.getAgentName();
            if(StringUtils.isBlank(agentName)){
                resultForm.setSuccess(false);
                resultForm.setMessage("代办中心不能为空！");
                return resultForm;
            }
            String currentOrgId = SecurityContext.getCurrentOrgId();
            AeaServiceWindow window = new AeaServiceWindow();
            window.setWindowName(agentName.trim());
            window.setWindowType("d");
            window.setRootOrgId(currentOrgId);
            List<AeaServiceWindow> windows = aeaServiceWindowMapper.listAeaServiceWindow(window);
            if(windows.size() == 0){
                resultForm.setSuccess(false);
                resultForm.setMessage("找不到该代办中心！");
                return resultForm;
            }
            if(windows.size() > 1){
                resultForm.setSuccess(false);
                resultForm.setMessage("存在多个同名的代办中心！");
                return resultForm;
            }
            String projInfoId = proj.getProjInfoId();
            String windowId = windows.get(0).getWindowId();
            AeaProjWindow win = aeaProjWindowMapper.getAeaProjWindowByProjInfoIdAndWindowId(projInfoId, windowId);
            if(win != null){
                resultForm.setSuccess(false);
                resultForm.setMessage("该项目已关联该代办中心！");
                return resultForm;
            }
            //创建关系
            AeaProjWindow projWindow = new AeaProjWindow();
            projWindow.setProjWindowId(UUID.randomUUID().toString());
            projWindow.setProjInfoId(projInfoId);
            projWindow.setWindowId(windowId);
            projWindow.setCreater(SecurityContext.getCurrentUserName());
            projWindow.setCreateTime(new Date());
            projWindow.setRootOrgId(currentOrgId);
            aeaProjWindowMapper.insertAeaProjWindow(projWindow);
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage(e.getMessage());
            return resultForm;
        }
        return resultForm;
    }

    /**
     * 处理不间断空格 unicode编码是\u00A0
     * @param localCode
     * @return
     */
    public String handleUninterruptedSpaces(String localCode){
        String regex = "^[A-Za-z0-9|-]$";
        StringBuilder sb = new StringBuilder();
        for(int i=0,len=localCode.length();i<len;i++){
            char c = localCode.charAt(i);
            boolean matches = (c+"").matches(regex);
            if(matches){
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

