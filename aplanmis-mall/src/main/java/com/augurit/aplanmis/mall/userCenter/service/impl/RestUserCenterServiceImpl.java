package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.sc.dic.region.service.BscDicRegionService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.UnitType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaProjLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.service.dic.FsGcbmBscRuleCodeStrategy;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.augurit.aplanmis.common.utils.DesensitizedUtil;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestUserCenterService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaLinkmanInfoVo;
import com.augurit.aplanmis.mall.userCenter.vo.AeaUnitInfoVo;
import com.google.common.base.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RestUserCenterServiceImpl implements RestUserCenterService {

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private AeaProjLinkmanMapper aeaProjLinkmanMapper;
    @Autowired
    private FsGcbmBscRuleCodeStrategy fsGcbmBscRuleCodeStrategy;
    @Autowired
    BscDicRegionService bscDicRegionService;
    @Value("${mall.check.authority:false}")
    private boolean isCheckAuthority;
    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

    @Override
    public String saveChildProject(HttpServletRequest request, AeaProjInfo aeaProjInfo) throws Exception {
        //判定登录类型
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        String userId = loginInfoVo.getUserId();
        String modify="";
        if (StringUtils.isNotBlank(userId)) {//用户id不为空
            modify=loginInfoVo.getPersonName();
        } else {
            modify=loginInfoVo.getUnitName();
        }
        AeaProjInfo oldAeaProjInfo;
        //判定是编辑主项目还是保存分离子项目
        if (StringUtils.isNotBlank(aeaProjInfo.getProjInfoId())) {//如果aeaProjInfo的id不为空，则为编辑主项目,需创建新的aeaProjInfoLog
            oldAeaProjInfo = aeaProjInfo;
            aeaProjInfo.setModifier(modify);//更新修改人
            aeaProjInfo.setModifyTime(new Date());//更新修改时间
            aeaProjInfoMapper.updateAeaProjInfo(aeaProjInfo);//更新aeaProjInfo
            //创建新的aeaProjInfoLog
//            insertAeaProjInfoLog(modify,aeaProjInfo,oldAeaProjInfo);
        } else {//如果aeaProjInfo的id为空，则为分离出来的子项目，需创建新的aeaProjInfo，新的aeaParentProj，新的aeaProjInfoLog
            aeaProjInfo.setProjInfoId(UUID.randomUUID().toString()); //更新主键
            aeaProjInfo.setCreater(modify);//更新创建人
            aeaProjInfo.setCreateTime(new Date());//更新创建时间
            aeaProjInfo.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaProjInfo.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            aeaProjInfo.setProjFlag("c");
            aeaProjInfoMapper.insertAeaProjInfo(aeaProjInfo);//insert新的aeaProjInfo
            //创建新的aeaParentProj
            AeaParentProj aeaParentProj = new AeaParentProj();
            aeaParentProj.setNodeProjId(UUID.randomUUID().toString());//更新主键
            aeaParentProj.setCreater(modify); //更新创建人
            aeaParentProj.setCreateTime(new Date()); //更新创建时间
            aeaParentProj.setParentProjId(aeaProjInfo.getParentProjId()); //更新parentProjId
            aeaParentProj.setChildProjId(aeaProjInfo.getProjInfoId());//更新childProjId
            aeaParentProj.setProjSeq(aeaProjInfo.getParentProjId() + "." + aeaParentProj.getChildProjId());//更新projSeq
            aeaProjInfoMapper.insertAeaParentProj(aeaParentProj); //insert新的aeaParentProj
            //继承父项目的单位关系
            updateProjUnitFromParent(aeaProjInfo.getParentProjId(),aeaProjInfo.getProjInfoId());
            //查询子项目的父项
            oldAeaProjInfo = aeaProjInfoMapper.getOnlyAeaProjInfoById(aeaParentProj.getParentProjId());
//            insertAeaProjInfoLog(modify,aeaProjInfo,oldAeaProjInfo);
            oldAeaProjInfo=aeaProjInfo;
        }
        return oldAeaProjInfo.getProjInfoId();
    }

    private void updateProjUnitFromParent(String parentProjId,String projInfoId) throws Exception {
        if(StringUtils.isNotBlank(parentProjId) && StringUtils.isNotBlank(projInfoId)){
            AeaUnitProj query=new AeaUnitProj();
            query.setProjInfoId(parentProjId);
            query.setIsDeleted("0");
            List<AeaUnitProj> list = aeaUnitProjMapper.listAeaUnitProj(query);
            if(list.size()>0){
                for (AeaUnitProj param:list){
                    param.setProjInfoId(projInfoId);
                    param.setCreateTime(new Date());
                    param.setCreater(SecurityContext.getCurrentUserName());
                    param.setUnitProjId(UUID.randomUUID().toString());
                    aeaUnitProjMapper.insertAeaUnitProj(param);
                }
            }
        }
    }


    @Override
    public String saveProjectInfo(HttpServletRequest request, AeaProjInfo aeaProjInfo) throws Exception {
        String isNeedGeneCode = aeaProjInfo.getIsNeedGeneCode();
        String postCode = "";//邮编
        if (StringUtils.isNotBlank(isNeedGeneCode)&&"1".equals(isNeedGeneCode)){//需要生成编码
            String regionId = aeaProjInfo.getRegionalism();
            BscDicRegion bscDicRegion = bscDicRegionService.getBscDicRegionById(regionId);
            if (bscDicRegion!=null)  postCode = bscDicRegion.getRegionNum();
            String code = fsGcbmBscRuleCodeStrategy.generateCode(aeaProjInfo.getGbCodeYear(),aeaProjInfo.getProjNature(),StringUtils.isBlank(aeaProjInfo.getProjType())?"01":aeaProjInfo.getProjType(),StringUtils.isBlank(postCode)?"528000":postCode,topOrgId);
            aeaProjInfo.setLocalCode(code);
            aeaProjInfo.setGcbm(code);
        }

        if (StringUtils.isNotBlank(aeaProjInfo.getProjInfoId())) {
            aeaProjInfoService.updateAeaProjInfo(aeaProjInfo);
        } else {
            aeaProjInfo.setProjInfoId(UUID.randomUUID().toString());
            aeaProjInfoService.insertAeaProjInfo(aeaProjInfo);
        }

        //判定登录类型
        LoginInfoVo loginInfoVo = SessionUtil.getLoginInfo(request);
        String userId = loginInfoVo.getUserId();
        if (StringUtils.isNotBlank(userId)) {//用户id不为空，则为个人用户，或者委托人用户

            AeaProjLinkman aeaProjLinkman=new AeaProjLinkman();
            aeaProjLinkman.setProjInfoId(aeaProjInfo.getProjInfoId());
            aeaProjLinkman.setLinkmanInfoId(userId);
            List<AeaProjLinkman> list = aeaProjLinkmanMapper.listAeaProjLinkman(aeaProjLinkman);
            if (list.size()>0) return aeaProjInfo.getProjInfoId();
            aeaProjLinkman.setProjLinkmanId(UUID.randomUUID().toString());
            aeaProjLinkman.setType("link");
            aeaProjLinkman.setCreater(SecurityContext.getCurrentUserName());
            aeaProjLinkman.setCreateTime(new Date());
            aeaProjLinkmanMapper.insertAeaProjLinkman(aeaProjLinkman);
        } else {//企业用户
            AeaUnitProj aeaUnitProj = new AeaUnitProj();

            aeaUnitProj.setUnitInfoId(loginInfoVo.getUnitId());
            aeaUnitProj.setIsOwner("1");  //是否业主单位，0表示代建单位，1表示业主/建设单位
            aeaUnitProj.setProjInfoId(aeaProjInfo.getProjInfoId());
            //
            List<AeaUnitProj> list = aeaUnitProjMapper.listAeaUnitProj(aeaUnitProj);
            if (list.size()>0) return aeaProjInfo.getProjInfoId();
            aeaUnitProj.setUnitProjId(UUID.randomUUID().toString());
            aeaUnitProj.setCreater(SecurityContext.getCurrentUserName());
            aeaUnitProj.setCreateTime(new Date());
            aeaUnitProj.setUnitType(UnitType.DEVELOPMENT_UNIT.getValue());
            aeaUnitProj.setLinkmanInfoId(loginInfoVo.getUserId());
            aeaUnitProj.setIsDeleted("0");
            aeaUnitProj.setUnitType("1");
            aeaUnitProjMapper.insertAeaUnitProj(aeaUnitProj);
        }

        return aeaProjInfo.getProjInfoId();
    }

    @Override
    public void withDrawProject(String applyInstId) throws Exception{
        AeaHiApplyinst aeaHiApplyinst = new AeaHiApplyinst();
        aeaHiApplyinst.setApplyinstId(applyInstId);
        aeaHiApplyinst.setIsDeleted("1");
        aeaHiApplyinstService.updateAeaHiApplyinst(aeaHiApplyinst);
    }

    @Override
    public AeaLinkmanInfoVo getAeaLinkmanInfoByLinkmanInfoId(String userId) throws Exception {
            AeaLinkmanInfo aeaLinkmanInfo=aeaLinkmanInfoService.getAeaLinkmanInfoByLinkmanInfoId(userId);
            AeaLinkmanInfoVo aeaLinkmanInfoVo = new AeaLinkmanInfoVo();
            BeanUtils.copyProperties(aeaLinkmanInfo,aeaLinkmanInfoVo);
            if (isCheckAuthority){
                aeaLinkmanInfoVo.setLinkmanCertNo(DesensitizedUtil.desensitizedIdNumber(aeaLinkmanInfoVo.getLinkmanCertNo()));
                aeaLinkmanInfoVo.setLinkmanMobilePhone(DesensitizedUtil.desensitizedPhoneNumber(aeaLinkmanInfoVo.getLinkmanMobilePhone()));
            }
            return aeaLinkmanInfoVo;
    }

    @Override
    public AeaUnitInfoVo getAeaUnitInfoByUnitInfoId(String unitInfoId) throws Exception {
        AeaUnitInfo aeaUnitInfo = aeaUnitInfoService.getAeaUnitInfoByUnitInfoId(unitInfoId);
        AeaUnitInfoVo aeaUnitInfoVo = new AeaUnitInfoVo();
        BeanUtils.copyProperties(aeaUnitInfo,aeaUnitInfoVo);
        if (isCheckAuthority){
            aeaUnitInfoVo.setIdno(DesensitizedUtil.desensitizedIdNumber(aeaUnitInfoVo.getIdno()));
        }
        return aeaUnitInfoVo;
    }

    @Override
    public List<AeaUnitInfoVo> getUnitInfoListByLinkmanInfoId(String userId) {
        List<AeaUnitInfo> aeaUnitList=aeaUnitInfoService.getUnitInfoByLinkmanInfoId(userId);
        List<AeaUnitInfoVo> list =  aeaUnitList.stream().map(AeaUnitInfoVo::build).collect(Collectors.toList());
        if (isCheckAuthority){
            list.stream().forEach(aeaUnitInfoVo->{
                aeaUnitInfoVo.setIdno(DesensitizedUtil.desensitizedIdNumber(aeaUnitInfoVo.getIdno()));
            });
        }

        return list;
    }

    @Override
    public List<AeaLinkmanInfoVo> findAllUnitLinkman(String unitInfoId) {
        List<AeaLinkmanInfo> linkmanInfoList=aeaLinkmanInfoService.findAllUnitLinkman(unitInfoId);
        List<AeaLinkmanInfoVo> list = linkmanInfoList.stream().map(AeaLinkmanInfoVo::build).collect(Collectors.toList());
        if (isCheckAuthority){
            list.stream().forEach(aeaLinkmanInfoVo->{
                aeaLinkmanInfoVo.setLinkmanMobilePhone(DesensitizedUtil.desensitizedPhoneNumber(aeaLinkmanInfoVo.getLinkmanMobilePhone()));
                aeaLinkmanInfoVo.setLinkmanCertNo(DesensitizedUtil.desensitizedIdNumber(aeaLinkmanInfoVo.getLinkmanCertNo()));
            });
        }
        return list;
    }

    @Override
    public Boolean isBelongUnit(String userId, HttpServletRequest request) throws Exception{
        if (!isCheckAuthority) return true;
        try {
            LoginInfoVo user = SessionUtil.getLoginInfo(request);
            AeaUnitLinkman query=new AeaUnitLinkman();
            if (StringUtils.isBlank(user.getUnitId())) {//个人或委托人登录
                if (StringUtils.isNotBlank(userId) && !userId.equals(user.getUserId())) return false;
                return true;
            }else {
                query.setUnitInfoId(user.getUnitId());
                query.setLinkmanInfoId(userId);
                List<AeaUnitLinkman> unitLinkmans = aeaUnitLinkmanMapper.listAeaUnitLinkman(query);
                //加入权限校验，只能查询属于自己单位的联系人
                if (unitLinkmans==null||unitLinkmans.size()==0)  return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
        return true;
    }

    public static void main(String[] args) {
        String gcbm = "abcd";
        int childNumber = 2;
        String projChildGcbm = gcbm + "-" + ("000" + (childNumber + 1)).substring(("000" + (childNumber + 1)).length() - 4);
        System.out.println(projChildGcbm);
    }

}
