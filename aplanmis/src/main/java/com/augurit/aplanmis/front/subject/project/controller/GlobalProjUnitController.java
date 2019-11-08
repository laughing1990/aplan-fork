package com.augurit.aplanmis.front.subject.project.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.front.subject.project.service.GlobalProjUnitService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/aea/projUnit")
@Api(value = "全局项目库单位管理", tags = "项目库单位管理接口")
@Slf4j
public class GlobalProjUnitController {

    private static Logger logger = LoggerFactory.getLogger(GlobalProjController.class);

    @Autowired
    private GlobalProjUnitService globalProjUnitService;
    /**
     * 新建企业信息
     */
    @RequestMapping("/saveAeaUnitInfo")
    public ResultForm saveAeaUnitInfo(AeaUnitInfo aeaUnitInfo){
        try {
            AeaUnitInfo unitInfo = new AeaUnitInfo();
            if (aeaUnitInfo!=null&& StringUtils.isBlank(aeaUnitInfo.getUnitInfoId())){
                unitInfo=globalProjUnitService.saveAeaUnitInfo(aeaUnitInfo);
            }else{
                unitInfo = globalProjUnitService.updateAeaUnitInfo(aeaUnitInfo);
            }
            return new ContentResultForm<AeaUnitInfo>(true,unitInfo);
        }catch (Exception e){
            return new ResultForm(false,"保存失败,"+e.getMessage());
        }


    }
    /**
     * 新建企业信息并关联多个项目
     */
    @RequestMapping("/saveAeaUnitInfoAndUnitProjs")
    public ResultForm saveAeaUnitInfoAndUnitProjs(AeaUnitInfo aeaUnitInfo, String[] projInfoIds){
        try {
            AeaUnitInfo unitInfo =globalProjUnitService.saveAeaUnitInfoAndUnitProjs(aeaUnitInfo,projInfoIds);
            return new ContentResultForm<AeaUnitInfo>(true,unitInfo);
        }catch (Exception e){
            return new ResultForm(false,"保存失败,"+e.getMessage());
        }
    }

    @GetMapping("/listAeaUnitInfo")
    public List<AeaUnitInfo> listAeaUnitInfo(AeaUnitInfo aeaUnitInfo){
        return globalProjUnitService.listAeaUnitInfo(aeaUnitInfo);
        //globalProjUnitService.save

    }

    @GetMapping("/getAeaUnitInfoByUnitInfoId")
    public ResultForm getAeaUnitInfoByUnitInfoId(String unitInfoId){
        try {
            AeaUnitInfo aeaUnitInfo= globalProjUnitService.getAeaUnitInfoByUnitInfoId(unitInfoId);
            return new ContentResultForm<AeaUnitInfo>(true,aeaUnitInfo);
        }catch (Exception e){
            return new ResultForm(false,"获取单位信息失败");
        }
    }
    @RequestMapping("/deleteAeaUnitProj")
    public ResultForm deleteAeaUnitProj(String unitProjId){
        try {
            globalProjUnitService.deleteAeaUnitProj(unitProjId);
            return new ResultForm(true);
        }catch (Exception e){
            return new ResultForm(false);
        }
    }
    @RequestMapping("/batchDeleteAeaUnitProj")
    public ResultForm batchDeleteAeaUnitProj(String[] unitProjId){
        try {
            globalProjUnitService.batchDeleteAeaUnitProj(unitProjId);
            return new ResultForm(true);
        }catch (Exception e){
            return new ResultForm(false);
        }
    }
}
