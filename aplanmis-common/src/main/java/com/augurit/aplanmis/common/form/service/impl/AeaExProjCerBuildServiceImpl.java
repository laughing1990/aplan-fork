package com.augurit.aplanmis.common.form.service.impl;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.bpm.admin.sto.service.impl.AbstractFormDataOptManager;
import com.augurit.agcloud.bpm.common.constant.EDataOpt;
import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.domain.ActStoForminst;
import com.augurit.agcloud.bpm.common.domain.vo.FormDataOptResult;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.GDUnitType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.form.service.AeaExProjCertBuildService;
import com.augurit.aplanmis.common.form.vo.ExProjFormVo;
import com.augurit.aplanmis.common.mapper.AeaExProjCertBuildMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.vo.AeaCertiVo;
import com.augurit.aplanmis.common.vo.AeaProjDrawing;
import com.augurit.aplanmis.common.vo.AeaProjDrawingVo;
import com.augurit.aplanmis.front.basis.stage.service.RestStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class AeaExProjCerBuildServiceImpl extends AbstractFormDataOptManager implements AeaExProjCertBuildService {
    @Autowired
    private AeaExProjCertBuildMapper aeaExProjCertBuildMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;
    @Autowired
    private RestStageService restStageService;

    @Override
    public ContentResultForm<String> SynchronizeDataByExProjForm(ExProjFormVo vo) {
        try {
            AeaExProjCertBuild aeaExProjCertBuild = new AeaExProjCertBuild();
            //查询工程名称
            AeaProjInfo aeaProjInfoById = aeaProjInfoMapper.getAeaProjInfoById(vo.getProjInfoId());
            //查询该项目编码是否存在建设工程施工许可证
            AeaExProjCertBuild aeaExProjCertBuildByProjId = aeaExProjCertBuildMapper.findAeaExProjCertBuildByProjId(vo.getProjInfoId());
            if (aeaExProjCertBuildByProjId != null) {
                aeaExProjCertBuild.setBuildId(aeaExProjCertBuildByProjId.getBuildId());//主键
                aeaExProjCertBuild.setConstructionsSize(vo.getScaleContent());//建设规模
                aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuild);
                aeaProjInfoById.setProjAddr(vo.getScaleContent());
                aeaProjInfoMapper.updateAeaProjInfo(aeaProjInfoById);//同步更新到申报页面
                return new ContentResultForm<>(true, "同步信息成功", "success");
            } else {
                //查询建设单位名称
                List<AeaUnitInfo> unitInfo = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(vo.getProjInfoId(), "1");
                if (unitInfo != null && unitInfo.size() > 0) {
                    StringBuilder stringBuilder = this.spliceUnit(unitInfo);//拼接建设单位
                    aeaExProjCertBuild.setBuildId(UuidUtil.generateUuid());//主键
                    aeaExProjCertBuild.setProjInfoId(vo.getProjInfoId());//项目ID
                    aeaExProjCertBuild.setCertBuildCode("0");//建设工程规划许可证编号
                    aeaExProjCertBuild.setProjName(aeaProjInfoById.getProjName());//工程名称
                    aeaExProjCertBuild.setConstructionAddr(aeaProjInfoById.getProjAddr());//建设地址
                    aeaExProjCertBuild.setConstructionUnit(stringBuilder.toString());//建设单位
                    aeaExProjCertBuild.setConstructionsSize(vo.getScaleContent());//建设规模
                    aeaExProjCertBuild.setCreater(SecurityContext.getCurrentUserName());
                    aeaExProjCertBuild.setCreateTime(new Date());
                    aeaExProjCertBuild.setRootOrgId(SecurityContext.getCurrentOrgCode());
                    aeaExProjCertBuildMapper.insertAeaExProjCertBuild(aeaExProjCertBuild);
                    aeaProjInfoById.setProjAddr(vo.getScaleContent());
                    aeaProjInfoMapper.updateAeaProjInfo(aeaProjInfoById);//同步更新到申报页面
                    return new ContentResultForm<>(true, "同步信息成功", "success");
                } else {
                    return new ContentResultForm<>(false, "同步信息失败，请在申报页面添加建设单位信息", "error");
                }
            }
        } catch (Exception e) {
            return new ContentResultForm<>(false, "同步信息失败 " + e.getMessage(), "error");
        }
    }

    @Override
    public ContentResultForm<String> SynchronizeDataByAeaExProjCertLandForm(AeaCertiVo vo) {
        try {
            AeaExProjCertBuild aeaExProjCertBuild = new AeaExProjCertBuild();
            //查询工程名称
            AeaProjInfo aeaProjInfoById = aeaProjInfoMapper.getAeaProjInfoById(vo.getProjInfoId());
            aeaExProjCertBuild.setProjInfoId(vo.getProjInfoId());//项目ID
            //查询该项目编码是否存在建设工程施工许可证
            AeaExProjCertBuild aeaExProjCertBuildByProjId = aeaExProjCertBuildMapper.findAeaExProjCertBuildByProjId(vo.getProjInfoId());
            if (aeaExProjCertBuildByProjId != null) {
                aeaExProjCertBuild.setBuildId(aeaExProjCertBuildByProjId.getBuildId());//主键
                aeaExProjCertBuild.setCertBuildCode(vo.getCertProjectCode());//建设工程规划许可证编号
                aeaExProjCertBuild.setGovOrgCode(vo.getPublishOrgCodeProject());//核发机关组织机构代码
                aeaExProjCertBuild.setGovOrgName(vo.getPublishOrgNameProject());//核发机关
                aeaExProjCertBuild.setPublishTime(vo.getPublishTimeProject());//核发日期
                aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuild);
                return new ContentResultForm<>(true, "同步信息成功", "success");
            } else {
                //查询建设单位名称
                List<AeaUnitInfo> unitInfo = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(vo.getProjInfoId(), "1");
                if (unitInfo != null && unitInfo.size() > 0) {
                    StringBuilder stringBuilder = this.spliceUnit(unitInfo);//拼接建设单位
                    aeaExProjCertBuild.setBuildId(UuidUtil.generateUuid());//主键
                    aeaExProjCertBuild.setCertBuildCode(vo.getCertProjectCode());//建设工程规划许可证编号
                    aeaExProjCertBuild.setGovOrgCode(vo.getPublishOrgCodeProject());//核发机关组织机构代码
                    aeaExProjCertBuild.setGovOrgName(vo.getPublishOrgNameProject());//核发机关
                    aeaExProjCertBuild.setPublishTime(vo.getPublishTimeProject());//核发日期
                    aeaExProjCertBuild.setProjName(aeaProjInfoById.getProjName());//工程名称
                    aeaExProjCertBuild.setConstructionAddr(aeaProjInfoById.getProjAddr());//建设地址
                    aeaExProjCertBuild.setConstructionUnit(stringBuilder.toString());//建设单位
                    aeaExProjCertBuild.setCreater(SecurityContext.getCurrentUserName());
                    aeaExProjCertBuild.setCreateTime(new Date());
                    aeaExProjCertBuild.setRootOrgId(SecurityContext.getCurrentOrgCode());
                    aeaExProjCertBuildMapper.insertAeaExProjCertBuild(aeaExProjCertBuild);
                    return new ContentResultForm<>(true, "同步信息成功", "success");
                } else {
                    return new ContentResultForm<>(false, "同步信息失败，请在申报页面添加建设单位信息", "error");
                }
            }
        } catch (Exception e) {
            return new ContentResultForm<>(false, "同步信息失败 " + e.getMessage(), "error");
        }
    }

    @Override
    public ContentResultForm<String> SynchronizeDataByAeaExProjContractForm(AeaExProjContract vo) throws Exception {
        try {
            AeaExProjCertBuild aeaExProjCertBuild = new AeaExProjCertBuild();
            //查询工程名称
            AeaProjInfo aeaProjInfoById = aeaProjInfoMapper.getAeaProjInfoById(vo.getProjInfoId());
            aeaExProjCertBuild.setProjInfoId(vo.getProjInfoId());//项目ID
            //查询该项目编码是否存在建设工程施工许可证
            AeaExProjCertBuild aeaExProjCertBuildByProjId = aeaExProjCertBuildMapper.findAeaExProjCertBuildByProjId(vo.getProjInfoId());
            if (aeaExProjCertBuildByProjId != null) {
                aeaExProjCertBuild.setBuildId(aeaExProjCertBuildByProjId.getBuildId());//主键
                aeaExProjCertBuild.setContractPrice(vo.getContractMoeny());//合同价格，单位：万元
                aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuild);
                return new ContentResultForm<>(true, "同步信息成功", "success");
            } else {
                //查询建设单位名称
                List<AeaUnitInfo> unitInfo = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(vo.getProjInfoId(), "1");
                if (unitInfo != null && unitInfo.size() > 0) {
                    StringBuilder stringBuilder = this.spliceUnit(unitInfo);//拼接建设单位
                    aeaExProjCertBuild.setBuildId(UuidUtil.generateUuid());//主键
                    aeaExProjCertBuild.setCertBuildCode("0");//建设工程规划许可证编号
                    aeaExProjCertBuild.setContractPrice(vo.getContractMoeny());//合同金额
                    aeaExProjCertBuild.setProjName(aeaProjInfoById.getProjName());//工程名称
                    aeaExProjCertBuild.setConstructionAddr(aeaProjInfoById.getProjAddr());//建设地址
                    aeaExProjCertBuild.setConstructionUnit(stringBuilder.toString());//建设单位
                    aeaExProjCertBuild.setCreater(SecurityContext.getCurrentUserName());
                    aeaExProjCertBuild.setCreateTime(new Date());
                    aeaExProjCertBuild.setRootOrgId(SecurityContext.getCurrentOrgCode());
                    aeaExProjCertBuildMapper.insertAeaExProjCertBuild(aeaExProjCertBuild);
                    return new ContentResultForm<>(true, "同步信息成功", "success");
                } else {
                    return new ContentResultForm<>(false, "同步信息失败，请在申报页面添加建设单位信息", "error");
                }
            }
        } catch (Exception e) {
            return new ContentResultForm<>(false, "同步信息失败 " + e.getMessage(), "error");
        }
    }

    @Override
    public ContentResultForm<String> SynchronizeDataByAeaProjDrawingForm(AeaProjDrawingVo vo) throws Exception {
        try {
            AeaExProjCertBuild aeaExProjCertBuild = new AeaExProjCertBuild();
            String kcUnit = null;
            String kcUnitLeader = null;
            String sjUnit = null;
            String sjUnitLeader = null;

            //查询工程名称
            AeaProjInfo aeaProjInfoById = aeaProjInfoMapper.getAeaProjInfoById(vo.getProjInfoId());
            aeaExProjCertBuild.setProjInfoId(vo.getProjInfoId());//项目ID

            //获取勘察单位以及负责人
            List<AeaProjDrawing> aeaProjDrawing = vo.getAeaProjDrawing();
            for (AeaProjDrawing projDrawing : aeaProjDrawing) {
                if (projDrawing.getUnitType().equals("4")) {
                    kcUnit = projDrawing.getApplicant();
                    kcUnitLeader = projDrawing.getProjectLeader();
                }
                if (projDrawing.getUnitType().equals("3")) {
                    sjUnit = projDrawing.getApplicant();
                    sjUnitLeader = projDrawing.getProjectLeader();
                }
            }

            //查询该项目编码是否存在建设工程施工许可证
            AeaExProjCertBuild aeaExProjCertBuildByProjId = aeaExProjCertBuildMapper.findAeaExProjCertBuildByProjId(vo.getProjInfoId());
            if (aeaExProjCertBuildByProjId != null) {
                aeaExProjCertBuild.setBuildId(aeaExProjCertBuildByProjId.getBuildId());//主键
                aeaExProjCertBuild.setKcUnit(kcUnit);//勘察单位
                aeaExProjCertBuild.setKcUnitLeader(kcUnitLeader);//勘察单位负责人
                aeaExProjCertBuild.setSjUnit(sjUnit);//设计单位
                aeaExProjCertBuild.setSjUnitLeader(sjUnitLeader);//设计单位负责人
                aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuild);
                return new ContentResultForm<>(true, "同步信息成功", "success");
            } else {
                //查询建设单位名称
                List<AeaUnitInfo> unitInfo = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(vo.getProjInfoId(), "1");
                if (unitInfo != null && unitInfo.size() > 0) {
                    StringBuilder stringBuilder = this.spliceUnit(unitInfo);//拼接建设单位
                    aeaExProjCertBuild.setBuildId(UuidUtil.generateUuid());//主键
                    aeaExProjCertBuild.setCertBuildCode("0");//建设工程规划许可证编号
                    aeaExProjCertBuild.setProjName(aeaProjInfoById.getProjName());//工程名称
                    aeaExProjCertBuild.setConstructionAddr(aeaProjInfoById.getProjAddr());//建设地址
                    aeaExProjCertBuild.setConstructionUnit(stringBuilder.toString());//建设单位
                    aeaExProjCertBuild.setKcUnit(kcUnit);//勘察单位
                    aeaExProjCertBuild.setKcUnitLeader(kcUnitLeader);//勘察单位负责人
                    aeaExProjCertBuild.setSjUnit(sjUnit);//设计单位
                    aeaExProjCertBuild.setSjUnitLeader(sjUnitLeader);//设计单位负责人
                    aeaExProjCertBuild.setCreater(SecurityContext.getCurrentUserName());
                    aeaExProjCertBuild.setCreateTime(new Date());
                    aeaExProjCertBuild.setRootOrgId(SecurityContext.getCurrentOrgCode());
                    aeaExProjCertBuildMapper.insertAeaExProjCertBuild(aeaExProjCertBuild);
                    return new ContentResultForm<>(true, "同步信息成功", "success");
                } else {
                    return new ContentResultForm<>(false, "同步信息失败，请在申报页面添加建设单位信息", "error");
                }
            }
        } catch (Exception e) {
            return new ContentResultForm<>(false, "同步信息失败 " + e.getMessage(), "error");
        }
    }

    @Override
    public ContentResultForm<String> SynchronizeDataByAeaExProjBuild(AeaExProjBuild vo) {
        try {
            AeaExProjCertBuild aeaExProjCertBuild = new AeaExProjCertBuild();
            String sgUnit = null;
            String sgUnitLeader = null;
            String jlUnit = null;
            String jlUnitLeader = null;
            String gczcbUnit = null;
            String gczcbUnitLeader = null;
            //查询工程名称
            AeaProjInfo aeaProjInfoById = aeaProjInfoMapper.getAeaProjInfoById(vo.getProjInfoId());
            aeaExProjCertBuild.setProjInfoId(vo.getProjInfoId());//项目ID
            //获取合同工期
            StringBuilder stringTime = new StringBuilder();
            stringTime.append(vo.getContractStartBuildTime());
            stringTime.append("~");
            stringTime.append(vo.getContractEndBuildTime());
            //获取施工、监理单位信息、工程总承包
            List<AeaExProjBuildUnitInfo> list = JSON.parseArray(vo.getAeaExProjBuildUnitInfo(), AeaExProjBuildUnitInfo.class);
            if (list != null && list.size() > 0) {
                for (AeaExProjBuildUnitInfo aeaExProjBuildUnitInfo : list) {
                    if (aeaExProjBuildUnitInfo.getUnitType().equals(GDUnitType.CONSTRUCTION_CONTRACTOR.getValue())) {
                        sgUnit = aeaExProjBuildUnitInfo.getApplicant();
                        sgUnitLeader = aeaExProjBuildUnitInfo.getLinkmanName();
                    }
                    if (aeaExProjBuildUnitInfo.getUnitType().equals(GDUnitType.SUPERVISION_UNIT.getValue())) {
                        jlUnit = aeaExProjBuildUnitInfo.getApplicant();
                        jlUnitLeader = aeaExProjBuildUnitInfo.getLinkmanName();
                    }
                    if (aeaExProjBuildUnitInfo.getUnitType().equals(GDUnitType.GENERAL_CONTRACTING_UNIT.getValue())) {
                        gczcbUnit = aeaExProjBuildUnitInfo.getApplicant();
                        gczcbUnitLeader = aeaExProjBuildUnitInfo.getLinkmanName();
                    }
                }
            }
            //查询该项目编码是否存在建设工程施工许可证
            AeaExProjCertBuild aeaExProjCertBuildByProjId = aeaExProjCertBuildMapper.findAeaExProjCertBuildByProjId(vo.getProjInfoId());
            if (aeaExProjCertBuildByProjId != null) {
                aeaExProjCertBuild.setBuildId(aeaExProjCertBuildByProjId.getBuildId());//主键
                aeaExProjCertBuild.setSgUnit(sgUnit);//施工单位
                aeaExProjCertBuild.setSgUnitLeader(sgUnitLeader);//施工单位负责人
                aeaExProjCertBuild.setJlUnit(jlUnit);//监理单位
                aeaExProjCertBuild.setJlUnitLeader(jlUnitLeader);//总监理工程施
                aeaExProjCertBuild.setGczcbUnit(gczcbUnit);//工程总承包
                aeaExProjCertBuild.setGczcbUnitLeader(gczcbUnitLeader);//工程总承包项目经理
                aeaExProjCertBuild.setContractPeriod(stringTime.toString());//合同工期
                aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuild);
                return new ContentResultForm<>(true, "同步信息成功", "success");
            } else {
                //查询建设单位名称
                List<AeaUnitInfo> unitInfo = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(vo.getProjInfoId(), "1");
                if (unitInfo != null && unitInfo.size() > 0) {
                    StringBuilder stringBuilder = this.spliceUnit(unitInfo);//拼接建设单位
                    aeaExProjCertBuild.setBuildId(UuidUtil.generateUuid());//主键
                    aeaExProjCertBuild.setCertBuildCode("0");//建设工程规划许可证编号
                    aeaExProjCertBuild.setProjName(aeaProjInfoById.getProjName());//工程名称
                    aeaExProjCertBuild.setConstructionAddr(aeaProjInfoById.getProjAddr());//建设地址
                    aeaExProjCertBuild.setConstructionUnit(stringBuilder.toString());//建设单位
                    aeaExProjCertBuild.setSgUnit(sgUnit);//施工单位
                    aeaExProjCertBuild.setSgUnitLeader(sgUnitLeader);//施工单位负责人
                    aeaExProjCertBuild.setJlUnit(jlUnit);//监理单位
                    aeaExProjCertBuild.setJlUnitLeader(jlUnitLeader);//总监理工程施
                    aeaExProjCertBuild.setGczcbUnit(gczcbUnit);//工程总承包
                    aeaExProjCertBuild.setGczcbUnitLeader(gczcbUnitLeader);//工程总承包项目经理
                    aeaExProjCertBuild.setContractPeriod(stringTime.toString());//合同工期
                    aeaExProjCertBuild.setCreater(SecurityContext.getCurrentUserName());
                    aeaExProjCertBuild.setCreateTime(new Date());
                    aeaExProjCertBuild.setRootOrgId(SecurityContext.getCurrentOrgCode());
                    aeaExProjCertBuildMapper.insertAeaExProjCertBuild(aeaExProjCertBuild);
                    return new ContentResultForm<>(true, "同步信息成功", "success");
                } else {
                    return new ContentResultForm<>(false, "同步信息失败，请在申报页面添加建设单位信息", "error");
                }
            }
        } catch (Exception e) {
            return new ContentResultForm<>(false, "同步信息失败 " + e.getMessage(), "error");
        }
    }

    @Override
    public void saveForm(AeaExProjCertBuild aeaExProjCertBuild) throws Exception {
        if (StringUtils.isBlank(aeaExProjCertBuild.getBuildId())) {
            //查询项目名称
            AeaProjInfo aeaProjInfoById = aeaProjInfoMapper.getAeaProjInfoById(aeaExProjCertBuild.getProjInfoId());
            aeaExProjCertBuild.setCertBuildCode("0");
            aeaExProjCertBuild.setProjName(aeaProjInfoById.getProjName());
            aeaExProjCertBuild.setBuildId(UuidUtil.generateUuid());
            aeaExProjCertBuild.setCreateTime(new Date());
            aeaExProjCertBuild.setCreater(SecurityContext.getCurrentUserName());
            aeaExProjCertBuild.setRootOrgId(SecurityContext.getCurrentOrgCode());
            aeaExProjCertBuildMapper.insertAeaExProjCertBuild(aeaExProjCertBuild);
            if (StringUtils.isBlank(aeaExProjCertBuild.getFormId())) throw new Exception("缺少formId");
            FormDataOptResult formDataOptResult = this.formSave(aeaExProjCertBuild.getFormId(), aeaExProjCertBuild.getBuildId(), EDataOpt.INSERT.getOpareteType(), null);
            //关联表单实例和申请实例
            AeaApplyinstForminst aeaApplyinstForminst = new AeaApplyinstForminst();
            aeaApplyinstForminst.setApplyinstId(aeaExProjCertBuild.getRefEntityId());
            aeaApplyinstForminst.setForminstId(formDataOptResult.getActStoForminst().getStoForminstId());
            aeaApplyinstForminst.setStoFormId(aeaExProjCertBuild.getFormId());
            aeaApplyinstForminst.setCreateTime(new Date());
            aeaApplyinstForminst.setCreater(SecurityContext.getCurrentUserId());
            restStageService.bindForminst(aeaApplyinstForminst);
        } else {
            aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuild);
        }
    }

    @Override
    public AeaExProjCertBuild findByProjId(String projId) throws Exception {
        return aeaExProjCertBuildMapper.findAeaExProjCertBuildByProjId(projId);
    }

    public StringBuilder spliceUnit(List<AeaUnitInfo> unitInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        if (unitInfo != null && unitInfo.size() > 0) {
            for (int i = 0; i < unitInfo.size(); i++) {
                AeaUnitInfo aeaUnitInfo = unitInfo.get(i);
                stringBuilder.append(aeaUnitInfo.getApplicant());
                if (i < unitInfo.size() - 1) {
                    stringBuilder.append("、");
                }
            }
        }
        return stringBuilder;
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
