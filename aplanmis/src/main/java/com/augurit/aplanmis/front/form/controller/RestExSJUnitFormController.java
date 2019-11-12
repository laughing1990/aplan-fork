package com.augurit.aplanmis.front.form.controller;

import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.admin.market.qual.service.AeaImQualLevelService;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaUnitProjLinkmanMapper;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.front.form.service.RestExSJUnitFormService;
import com.augurit.aplanmis.front.subject.unit.vo.UnitVo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredConstructors;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
    private AeaImQualLevelService aeaImQualLevelService;
    @Autowired
    private RestExSJUnitFormService restExSJUnitFormService;

    @PostMapping("/saveOrUpdateSJUnitInfo")
    public ContentResultForm<String> saveOrUpdateSJUnitInfo(AeaExProjBuild aeaExProjBuild) throws Exception{
        restExSJUnitFormService.saveOrUpdateSJUnitInfo(aeaExProjBuild);
        return new ContentResultForm<>(true,"", "Save success");
    }

    @GetMapping("/initExSJUnit")
    public ContentResultForm<Map<String,Object>> initExSJUnit(@RequestParam(required = false) String projInfoId) throws Exception {
        Map<String, Object> map =null;
        if(projInfoId!=null){
            map = restExSJUnitFormService.initExSJUnit(projInfoId);
        }
        ContentResultForm<Map<String, Object>> query_success = new ContentResultForm<>(true, map, "Query success");
        return new ContentResultForm<>(true, map, "Query success");
    }

    @GetMapping("/list")
    public ContentResultForm<Set<ExSJUnitFromDetails>> list(@RequestParam(required = false) String keyword, @RequestParam(required = false) String projInfoId){
        List<ExSJUnitFromDetails> aeaExProBuildUnitInfoByKeyword = aeaUnitInfoService.findAeaExProBuildUnitInfoByKeyword(keyword);
        Set<ExSJUnitFromDetails> unitsByKeyword = aeaExProBuildUnitInfoByKeyword.stream().map(this::getUnitVo).collect(Collectors.toSet());
        Map<String,Object> map = new HashMap<String,Object>();
        return new ContentResultForm<>(true, unitsByKeyword, "Query success");
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
        List<AeaImQualLevel> aeaImQualLevels = aeaImQualLevelService.listAeaImQualLevel(aeaImQualLevel);
        Set<AeaImQualLevel> collect = null;
        if(aeaImQualLevels.size() > 0 || aeaImQualLevels != null){
            AeaImQualLevel query = new AeaImQualLevel();
            query.setQualLevelId(aeaImQualLevels.get(0).getQualLevelId());
            collect = aeaImQualLevelService.listAeaImQualLevel(query).stream().collect(Collectors.toSet());
        }
        return new ContentResultForm<>(true, collect, "Query success");
    }
}
