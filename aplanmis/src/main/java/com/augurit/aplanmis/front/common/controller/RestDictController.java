package com.augurit.aplanmis.front.common.controller;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.front.common.vo.BscDicCodeItemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/dict")
@Api(value = "数据字典", tags = "数据字典")
public class RestDictController {

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @GetMapping({"/code/items/list"})
    @ApiOperation("根据dicCodeTypeCode获取数据字典")
    @ApiImplicitParam(name = "dicCodeTypeCode", value = "数据字典编码", required = true, type = "string")
    public ContentResultForm<List<BscDicCodeItemVo>> listBscDicCodeItemByDicTypeCode(String dicCodeTypeCode) {
        if (dicCodeTypeCode != null) {
            String orgId = SecurityContext.getCurrentOrgId();
            if (StringUtils.isNotBlank(orgId)) {
                List<BscDicCodeItem> list = bscDicCodeService.getActiveItemsByTypeCode(dicCodeTypeCode, orgId);
                List<BscDicCodeItemVo> bscDicCodeItemVos = BscDicCodeItemVo.toBscDicCodeItemVos(list);
                return new ContentResultForm<>(true, bscDicCodeItemVos);
            }
        }
        return new ContentResultForm<>(false, null, "");
    }

    @GetMapping({"/code/multi/items/list"})
    @ApiOperation("根据dicCodeTypeCode获取数据字典")
    @ApiImplicitParam(name = "dicCodeTypeCodes", value = "数据字典编码", required = true)
    public ContentResultForm<Map<String, List<BscDicCodeItemVo>>> listBscDicCodeItemByDicTypeCode(@RequestParam List<String> dicCodeTypeCodes) {
        if (dicCodeTypeCodes != null) {
            String orgId = SecurityContext.getCurrentOrgId();
            Map<String, List<BscDicCodeItemVo>> vos = new HashMap<>();
            for (String typeCode : dicCodeTypeCodes) {
                List<BscDicCodeItem> list = bscDicCodeService.getActiveItemsByTypeCode(typeCode, orgId);
                List<BscDicCodeItemVo> bscDicCodeItemVos = BscDicCodeItemVo.toBscDicCodeItemVos(list);
                vos.put(typeCode, bscDicCodeItemVos);
            }
            return new ContentResultForm<>(true, vos);
        }
        return new ContentResultForm<>(false, null, "无法获取数据字典信息");
    }
}
