package com.augurit.aplanmis.common.form.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
//import com.augurit.aplanmis.admin.market.qual.service.AeaImQualLevelService;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaImQualLevelMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitProjLinkmanMapper;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.form.service.AeaExProjCertBuildService;
import com.augurit.aplanmis.common.form.service.RestExSJUnitFormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/from/exSJUnit")
@ResponseBody
@Slf4j
public class RestExSJUnitFormController {
    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;
    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitProjLinkmanMapper aeaUnitProjLinkmanMapper;
    @Autowired
    private AeaImQualLevelMapper aeaImQualLevelMapper;
    @Autowired
    private RestExSJUnitFormService restExSJUnitFormService;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private AeaExProjCertBuildService aeaExProjCertBuildService;

    @PostMapping("/saveOrUpdateSJUnitInfo")
    public ContentResultForm<String> saveOrUpdateSJUnitInfo(AeaExProjBuild aeaExProjBuild){
        try {
            AeaProjInfo aeaProjInfo = new AeaProjInfo();
            aeaProjInfo.setProjInfoId(aeaExProjBuild.getProjInfoId());
            List<AeaProjInfo> aeaProjInfos = aeaProjInfoMapper.listAeaProjInfo(aeaProjInfo);
            if(aeaProjInfos !=null && aeaProjInfos.size()>0){
                restExSJUnitFormService.saveOrUpdateSJUnitInfo(aeaExProjBuild);
                aeaExProjCertBuildService.SynchronizeDataByAeaExProjBuild(aeaExProjBuild);
                return new ContentResultForm<>(true,"保存成功", "Save success");
            }else {
                return new ContentResultForm<>(false,"项目编码不存在", "error");
            }
        }catch (Exception e){
            return new ContentResultForm<>(false,e.getMessage(), "error");
        }
    }

    @GetMapping("/initExSJUnit")
    public ContentResultForm<Map<String,Object>> initExSJUnit(@RequestParam(required = false) String projInfoId) throws Exception {
        Map<String, Object> map =null;
        try {
            if(projInfoId!=null){
                map = restExSJUnitFormService.initExSJUnit(projInfoId);
            }
            return new ContentResultForm<>(true, map, "Query success");
        }catch (Exception e){
            return new ContentResultForm(false, e.getMessage(), "error");
        }
    }

    @GetMapping("/list")
    public ContentResultForm<Set<ExSJUnitFromDetails>> list(@RequestParam(required = false) String keyword, @RequestParam(required = false) String projInfoId){
        try {
            List<ExSJUnitFromDetails> aeaExProBuildUnitInfoByKeyword = aeaUnitInfoService.findAeaExProBuildUnitInfoByKeyword(keyword);
            Set<ExSJUnitFromDetails> unitsByKeyword = aeaExProBuildUnitInfoByKeyword.stream().map(this::getUnitVo).collect(Collectors.toSet());
            Map<String,Object> map = new HashMap<String,Object>();
            return new ContentResultForm<>(true, unitsByKeyword, "Query success");
        }catch (Exception e){
            return new ContentResultForm(false, e.getMessage(), "error");
        }
    }

    private ExSJUnitFromDetails getUnitVo(ExSJUnitFromDetails u) {
        List<PersonSetting> personSetting = new ArrayList<>();
        if(u.getUnitProjId()!=null){
            personSetting = aeaUnitProjLinkmanMapper.findPersonSetting(u.getUnitProjId());
        }
        if (personSetting!=null && personSetting.size() == 0 ){
            personSetting.add(new PersonSetting());
        }
        u.setPersonSetting(personSetting);
        return u;
    }

    @GetMapping("/listQualLevel")
    public ContentResultForm<Set<AeaImQualLevel>> listQualLevel(String parentQualLevelId) throws Exception {
        AeaImQualLevel aeaImQualLevel = new AeaImQualLevel();
        aeaImQualLevel.setParentQualLevelId(parentQualLevelId);
        List<AeaImQualLevel> aeaImQualLevels = aeaImQualLevelMapper.listAeaImQualLevel(aeaImQualLevel);
        Set<AeaImQualLevel> collect = null;
        if(aeaImQualLevels.size() > 0 || aeaImQualLevels != null){
            AeaImQualLevel query = new AeaImQualLevel();
            query.setQualLevelId(aeaImQualLevels.get(0).getQualLevelId());
            collect = aeaImQualLevelMapper.listAeaImQualLevel(query).stream().collect(Collectors.toSet());
        }
        return new ContentResultForm<>(true, collect, "Query success");
    }

    @GetMapping("/delPersonSetting")
    public ContentResultForm<String> delPersonSetting(@RequestParam(required = false) String parm){
        try {
            String[] split = parm.split(",");
            aeaUnitProjLinkmanMapper.batchDelAeaUnitProjLinkmanByIds(split);
            return new ContentResultForm<>(true,"删除成功","success");
        }catch (Exception e){
            return new ContentResultForm<>(false,e.getMessage(),"error");
        }
    }
}
