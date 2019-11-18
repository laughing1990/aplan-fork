package com.augurit.aplanmis.front.form.service.impl;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaExProjCertBuildMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjMapper;
import com.augurit.aplanmis.common.vo.AeaCertiVo;
import com.augurit.aplanmis.common.vo.AeaProjDrawing;
import com.augurit.aplanmis.common.vo.AeaProjDrawingVo;
import com.augurit.aplanmis.front.form.service.AeaExProjCertBuildService;
import com.augurit.aplanmis.front.form.vo.ExProjFormVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.augurit.agcloud.bsc.util.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class AeaExProjCerBuildServiceImpl implements AeaExProjCertBuildService {
    @Autowired
    private AeaExProjCertBuildMapper aeaExProjCertBuildMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaUnitProjMapper aeaUnitProjMapper;

    @Override
    public ContentResultForm<String> SynchronizeDataByExProjForm(ExProjFormVo vo) {
        try {
            AeaExProjCertBuild aeaExProjCertBuild = new AeaExProjCertBuild();
            //查询工程名称
            AeaProjInfo aeaProjInfoById = aeaProjInfoMapper.getAeaProjInfoById(vo.getProjInfoId());
            //查询该项目编码是否存在建设工程施工许可证
            AeaExProjCertBuild aeaExProjCertBuildByProjId = aeaExProjCertBuildMapper.findAeaExProjCertBuildByProjId(vo.getProjInfoId());
            if(aeaExProjCertBuildByProjId != null){
                aeaExProjCertBuild.setBuildId(aeaExProjCertBuildByProjId.getBuildId());//主键
                aeaExProjCertBuild.setConstructionsSize(vo.getScaleContent());//建设规模
                aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuild);
                aeaProjInfoById.setProjAddr(vo.getScaleContent());
                aeaProjInfoMapper.updateAeaProjInfo(aeaProjInfoById);//同步更新到申报页面
                return new ContentResultForm<>(true,"同步信息成功","success");
            }else{
                //查询建设单位名称
                List<AeaUnitInfo> unitInfo = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(vo.getProjInfoId(), "1");
                if(unitInfo != null && unitInfo.size()>0){
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
                    return new ContentResultForm<>(true,"同步信息成功","success");
                }else {
                    return new ContentResultForm<>(false,"同步信息失败，请在申报页面添加建设单位信息","error");
                }
            }
        }catch (Exception e){
            return new ContentResultForm<>(false,"同步信息失败，请在申报页面添加建设单位信息 " + e.getMessage(),"error");
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
            if(aeaExProjCertBuildByProjId != null){
                aeaExProjCertBuild.setBuildId(aeaExProjCertBuildByProjId.getBuildId());//主键
                aeaExProjCertBuild.setCertBuildCode(vo.getCertProjectCode());//建设工程规划许可证编号
                aeaExProjCertBuild.setGovOrgCode(vo.getPublishOrgCodeProject());//核发机关组织机构代码
                aeaExProjCertBuild.setGovOrgName(vo.getPublishOrgNameProject());//核发机关
                aeaExProjCertBuild.setPublishTime(vo.getPublishTimeProject());//核发日期
                aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuild);
                return new ContentResultForm<>(true,"同步信息成功","success");
            }else {
                //查询建设单位名称
                List<AeaUnitInfo> unitInfo = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(vo.getProjInfoId(), "1");
                if(unitInfo != null && unitInfo.size()>0){
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
                    return new ContentResultForm<>(true,"同步信息成功","success");
                }else {
                    return new ContentResultForm<>(false,"同步信息失败，请在申报页面添加建设单位信息","error");
                }
            }
        }catch (Exception e){
            return new ContentResultForm<>(false,"同步信息失败，请在申报页面添加建设单位信息 " + e.getMessage(),"error");
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
            if(aeaExProjCertBuildByProjId != null){
                aeaExProjCertBuild.setBuildId(aeaExProjCertBuildByProjId.getBuildId());//主键
                aeaExProjCertBuild.setContractPrice(vo.getContractMoeny());//合同价格，单位：万元
                aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuild);
                return new ContentResultForm<>(true,"同步信息成功","success");
            }else {
                //查询建设单位名称
                List<AeaUnitInfo> unitInfo = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(vo.getProjInfoId(), "1");
                if(unitInfo != null && unitInfo.size()>0){
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
                    return new ContentResultForm<>(true,"同步信息成功","success");
                }else {
                    return new ContentResultForm<>(false,"同步信息失败，请在申报页面添加建设单位信息","error");
                }
            }
        }catch (Exception e){
            return new ContentResultForm<>(false,"同步信息失败，请在申报页面添加建设单位信息 " + e.getMessage(),"error");
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
                if (projDrawing.getUnitType().equals("4")){
                    kcUnit = projDrawing.getApplicant();
                    kcUnitLeader = projDrawing.getProjectLeader();
                }
                if (projDrawing.getUnitType().equals("3")){
                    sjUnit = projDrawing.getApplicant();
                    sjUnitLeader = projDrawing.getProjectLeader();
                }
            }

            //查询该项目编码是否存在建设工程施工许可证
            AeaExProjCertBuild aeaExProjCertBuildByProjId = aeaExProjCertBuildMapper.findAeaExProjCertBuildByProjId(vo.getProjInfoId());
            if(aeaExProjCertBuildByProjId != null){
                aeaExProjCertBuild.setBuildId(aeaExProjCertBuildByProjId.getBuildId());//主键
                aeaExProjCertBuild.setKcUnit(kcUnit);//勘察单位
                aeaExProjCertBuild.setKcUnitLeader(kcUnitLeader);//勘察单位负责人
                aeaExProjCertBuild.setSjUnit(sjUnit);//设计单位
                aeaExProjCertBuild.setSjUnitLeader(sjUnitLeader);//设计单位负责人
                aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuild);
                return new ContentResultForm<>(true,"同步信息成功","success");
            }else {
                //查询建设单位名称
                List<AeaUnitInfo> unitInfo = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(vo.getProjInfoId(), "1");
                if(unitInfo != null && unitInfo.size()>0){
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
                    return new ContentResultForm<>(true,"同步信息成功","success");
                }else {
                    return new ContentResultForm<>(false,"同步信息失败，请在申报页面添加建设单位信息","error");
                }
            }
        }catch (Exception e){
            return new ContentResultForm<>(false,"同步信息失败，请在申报页面添加建设单位信息 " + e.getMessage(),"error");
        }
    }

    @Override
    public ContentResultForm<String> SynchronizeDataByAeaExProjBuild(AeaExProjBuild vo){
        try {
            AeaExProjCertBuild aeaExProjCertBuild = new AeaExProjCertBuild();
            String sgUnit = null;
            String sgUnitLeader = null;
            String jlUnit = null;
            String jlUnitLeader = null;
            //查询工程名称
            AeaProjInfo aeaProjInfoById = aeaProjInfoMapper.getAeaProjInfoById(vo.getProjInfoId());
            aeaExProjCertBuild.setProjInfoId(vo.getProjInfoId());//项目ID
            //获取合同工期
            StringBuilder stringTime = new StringBuilder();
            stringTime.append(vo.getContractStartBuildTime());
            stringTime.append("~");
            stringTime.append(vo.getContractEndBuildTime());
            //获取施工和监理单位信息
            List<AeaExProjBuildUnitInfo> list = JSON.parseArray(vo.getAeaExProjBuildUnitInfo(), AeaExProjBuildUnitInfo.class);
            if(list!=null && list.size()>0){
                for (AeaExProjBuildUnitInfo aeaExProjBuildUnitInfo : list) {
                    if(aeaExProjBuildUnitInfo.getUnitType().equals("6")){
                        sgUnit = aeaExProjBuildUnitInfo.getApplicant();
                        sgUnitLeader = aeaExProjBuildUnitInfo.getLinkmanName();
                    }
                    if(aeaExProjBuildUnitInfo.getUnitType().equals("11")){
                        jlUnit = aeaExProjBuildUnitInfo.getApplicant();
                        jlUnitLeader = aeaExProjBuildUnitInfo.getLinkmanName();
                    }
                }
            }
            //查询该项目编码是否存在建设工程施工许可证
            AeaExProjCertBuild aeaExProjCertBuildByProjId = aeaExProjCertBuildMapper.findAeaExProjCertBuildByProjId(vo.getProjInfoId());
            if(aeaExProjCertBuildByProjId != null) {
                aeaExProjCertBuild.setBuildId(aeaExProjCertBuildByProjId.getBuildId());//主键
                aeaExProjCertBuild.setSgUnit(sgUnit);//施工单位
                aeaExProjCertBuild.setSgUnitLeader(sgUnitLeader);//施工单位负责人
                aeaExProjCertBuild.setJlUnit(jlUnit);//监理单位
                aeaExProjCertBuild.setJlUnitLeader(jlUnitLeader);//总监理工程施
                aeaExProjCertBuild.setContractPeriod(stringTime.toString());//合同工期
                aeaExProjCertBuildMapper.updateAeaExProjCertBuild(aeaExProjCertBuild);
                return new ContentResultForm<>(true,"同步信息成功","success");
            }else {
                //查询建设单位名称
                List<AeaUnitInfo> unitInfo = aeaUnitProjMapper.findUnitInfoByProjIdAndUnitType(vo.getProjInfoId(), "1");
                if(unitInfo != null && unitInfo.size()>0){
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
                    aeaExProjCertBuild.setContractPeriod(stringTime.toString());//合同工期
                    aeaExProjCertBuild.setCreater(SecurityContext.getCurrentUserName());
                    aeaExProjCertBuild.setCreateTime(new Date());
                    aeaExProjCertBuild.setRootOrgId(SecurityContext.getCurrentOrgCode());
                    aeaExProjCertBuildMapper.insertAeaExProjCertBuild(aeaExProjCertBuild);
                    return new ContentResultForm<>(true,"同步信息成功","success");
                }else {
                    return new ContentResultForm<>(false,"同步信息失败，请在申报页面添加建设单位信息","error");
                }
            }
            }catch (Exception e){
            return new ContentResultForm<>(false,"同步信息失败，请在申报页面添加建设单位信息 " + e.getMessage(),"error");
        }
    }

    public StringBuilder spliceUnit(List<AeaUnitInfo> unitInfo){
        StringBuilder stringBuilder = new StringBuilder();
        if(unitInfo != null && unitInfo.size()>0){
            for (int i = 0; i < unitInfo.size(); i++) {
                AeaUnitInfo aeaUnitInfo = unitInfo.get(i);
                stringBuilder.append(aeaUnitInfo.getApplicant());
                if (i < unitInfo.size() - 1){
                    stringBuilder.append("、");
                }
            }
        }
        return stringBuilder;
    }
}
