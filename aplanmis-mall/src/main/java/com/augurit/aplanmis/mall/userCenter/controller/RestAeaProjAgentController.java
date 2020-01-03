package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyService;
import com.augurit.aplanmis.mall.userCenter.vo.AeaGuideItemVo;
import com.augurit.aplanmis.mall.userCenter.vo.StageStateParamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("rest/apply/agent")
@Api(value = "代办申请接口",tags = "代办申请 --> 代办申请接口")
public class RestAeaProjAgentController {
    Logger logger= LoggerFactory.getLogger(RestAeaProjAgentController.class);

    @Autowired
    RestApplyService restApplyService;

//    @GetMapping("itGuide/item/list")
//    @ApiOperation(value = "阶段申报 --> 智能引导获取事项一单清列表数据")
//    public ContentResultForm listItemAndStateByStageId(@Valid @RequestBody ProjAgentParamVo projAgentParamVo) {
//        try {
//            Map<String, List<AeaGuideItemVo>> map=new HashMap<>(2);
//            List<AeaGuideItemVo> coreItemList = restParallerApplyService.listItemByStageIdAndStateList(stageStateParamVo,"1");//并行
//            List<AeaGuideItemVo> parallelItemList = restParallerApplyService.listItemByStageIdAndStateList(stageStateParamVo,"0");//并联
//            map.put("coreItemList",coreItemList);
//            map.put("parallelItemList",parallelItemList);
//            return new ContentResultForm(true,map);
//        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
//            return new ContentResultForm(false,"","智能引导获取事项一单清列表数据异常");
//        }
//    }

}
