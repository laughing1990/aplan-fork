package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.mall.userCenter.service.impl.RestIteminstService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaHiIteminstDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:chendx
 * @date: 2019-10-30
 * @time: 16:19
 */
@RestController
@RequestMapping("/rest/iteminst")
@Api(value = "事项实例关联信息接口", tags = "事项审批实例查询-[事项关联信息]")
public class RestIteminstController {

    @Autowired
    private RestIteminstService restIteminstService;

    @GetMapping("/detail/{iteminstId}")
    @ApiOperation(value = "全景图 --> 事项审批详情信息接口", httpMethod = "GET")
    @ApiImplicitParam(name = "iteminstId", value = "事项实例id", required = true, dataType = "string", paramType = "path", readOnly = true)
    public ResultForm getIteminstDetailInfo(@PathVariable String iteminstId) throws Exception {
        if (StringUtils.isBlank(iteminstId) || "undefined".equals(iteminstId)) {
            return new ResultForm(false, "参数iteminstId不能为空！");
        }
        AeaHiIteminstDetailVo iteminstDetailInfo = restIteminstService.getIteminstDetailInfo(iteminstId);
        return new ContentResultForm<>(true, iteminstDetailInfo);
    }

}
