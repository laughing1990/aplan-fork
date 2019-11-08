package com.augurit.aplanmis.front.basis.item.controller;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.bsc.mapper.BscDicRegionMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmOrg;
import com.augurit.agcloud.opus.common.mapper.OpuOmOrgMapper;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemPriv;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaItemPrivService;
import com.augurit.aplanmis.front.basis.item.service.RestItemService;
import com.augurit.aplanmis.front.basis.item.vo.SpotRegistrationItemVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/item")
@Api(value = "事项接口", tags = "申报-[主题 阶段 事项]")
public class RestItemController {

    private static final String SERVICE_OBJECT_DICT_NAME = "ITEM_FWJGXZ";
    private static final String SERVICE_OBJECT_CODE = "5";

    @Autowired
    private RestItemService restItemService;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;
    @Autowired
    private OpuOmOrgMapper opuOmOrgMapper;
    @Autowired
    private AeaItemPrivService aeaItemPrivService;
    @Autowired
    private BscDicRegionMapper bscDicRegionMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;

    @GetMapping("/detail/{itemVerId}")
    @ApiOperation(value = "现场登记 --> 事项信息接口", httpMethod = "GET")
    @ApiImplicitParam(name = "itemVerId", value = "事项版本id", required = true, dataType = "string", paramType = "path", readOnly = true)
    public ContentResultForm<SpotRegistrationItemVo> detail(@PathVariable String itemVerId) throws Exception {
        AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
        if (aeaItemBasic == null) {
            return new ContentResultForm<>(false, null, "no result");
        }
        if (StringUtils.isNotBlank(aeaItemBasic.getOrgId())) {
            aeaItemBasic.setOrgName(opuOmOrgMapper.getOrg(aeaItemBasic.getOrgId()).getOrgName());
        }
        //设置事项办件类型
        BscDicCodeItem item_property = bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId("ITEM_PROPERTY", aeaItemBasic.getItemProperty(), "012aa547-7104-418d-87cc-824f24f1a278");
        if (null != item_property) {
            aeaItemBasic.setItemProperty(item_property.getItemName());
        }
        // 行政区划
        if (StringUtils.isNotBlank(aeaItemBasic.getRegionId())) {
            BscDicRegion bscDicRegion = bscDicRegionMapper.getBscDicRegionById(aeaItemBasic.getRegionId());
            if (bscDicRegion != null) {
                aeaItemBasic.setRegionName(bscDicRegion.getRegionName());
            }
        }
        String currentOrgId = SecurityContext.getCurrentOrgId();

        String serviceObjectCode = StringUtils.isNotBlank(aeaItemBasic.getXkdx()) ? aeaItemBasic.getXkdx() : SERVICE_OBJECT_CODE;
        String serviceObject = restItemService.getServiceObject(SERVICE_OBJECT_DICT_NAME, serviceObjectCode, currentOrgId);

        SpotRegistrationItemVo spotRegistrationItemVo = SpotRegistrationItemVo.fromAeaItemBasic(aeaItemBasic, serviceObject);

        if ("8".equals(aeaItemBasic.getItemNature())) {
            String itemId = aeaItemBasic.getItemId();
            String rootOrgId = aeaItemBasic.getRootOrgId();
            List<AeaItemBasic> parentItems = aeaItemBasicMapper.getAgentParentItem(itemId, rootOrgId);
            String names = parentItems.stream().map(AeaItemBasic::getItemName).collect(Collectors.joining(","));
            spotRegistrationItemVo.setProcedureName(names);

        }
        // 设置事项承办机构信息
        List<AeaItemPriv> currentUserItemPriv = aeaItemPrivService.findCurrentUserItemPriv(itemVerId);
        if (CollectionUtils.isNotEmpty(currentUserItemPriv) && StringUtils.isNotBlank(currentUserItemPriv.get(0).getOrgId())) {
            AeaItemPriv aeaItemPriv = currentUserItemPriv.get(0);
            spotRegistrationItemVo.setAllowManual(aeaItemPriv.getAllowManual());
            OpuOmOrg opuomOrg = opuOmOrgMapper.getOrg(aeaItemPriv.getOrgId());
            if (opuomOrg != null) {
                spotRegistrationItemVo.setOrgId(opuomOrg.getOrgId());
                spotRegistrationItemVo.setOrgName(opuomOrg.getName());
            }
        }
        return new ContentResultForm<>(true, spotRegistrationItemVo);
    }
}
