package com.augurit.aplanmis.front.receive.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaHiReceive;
import com.augurit.aplanmis.common.dto.AeaHiReceiveDto;
import com.augurit.aplanmis.common.mapper.AeaHiReceiveMapper;
import com.augurit.aplanmis.common.service.receive.ReceiveService;
import com.augurit.aplanmis.common.service.receive.vo.RefusedRecepitVo;
import com.augurit.aplanmis.front.constant.ReceiveConstant;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/rest/receive")
@Api(tags = "回执统一入口")
public class RestReceiveController {

    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private AeaHiReceiveMapper aeaHiReceiveMapper;

    @PostMapping("/save/refused")
    @ApiOperation("保存不收件或者不受理回执")
    public ContentResultForm saveRefuseAeaHiReceive(@ApiParam(value = "保存回执传的参数对象") RefusedRecepitVo refusedRecepitVo) throws Exception {
        String applyinstId = receiveService.saveRefuseReceive(refusedRecepitVo);
        return new ContentResultForm<>(true, applyinstId);
    }

    @GetMapping("/toPrintPage/{applyinstId}/{receiveType}")
    @ApiOperation(value = "跳转回执打印")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例主键 ", name = "applyinstId", required = true),
            @ApiImplicitParam(name = "receiveType", value = "回执类型（1：物料回执 2：受理回执 3：不收件回执 4：退件回执 5：领证回执）", required = true),
            @ApiImplicitParam(name = "isMakeUp", value = "是否补正材料，0 否；1 是", defaultValue = "0")})
    public ModelAndView printReceive(@PathVariable String applyinstId, @PathVariable String receiveType, String isMakeUp) {
        /*1 物料回执 2收件回执 3不收件 4退件回执 5 行政许可送达回执 6行政许可申请书 7不受理回执 8退件回执 9补正回执 10  todo xiaohutu*/
        ModelAndView mv = new ModelAndView("receive/unAccept_advice_note");
        mv.setViewName(ReceiveConstant.getModelByReceiveType(receiveType));
        mv.addObject("applyinstId", applyinstId);
        mv.addObject("receiveType", receiveType);
        mv.addObject("isMakeUp", isMakeUp);
        return mv;
    }

    @GetMapping("/getOneReceiveByApplyinstIdAndReceiveType")
    @ApiOperation(value = "根据申请实例ID和回执类型查询单个回执")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例主键 ", name = "applyinstId", required = true),
            @ApiImplicitParam(name = "receiveType", value = "回执类型（1：物料回执 2：受理回执 3：不收件回执 4：退件回执 5：领证回执 6：补正回执 7：补全回执）", required = true),
            @ApiImplicitParam(name = "isMakeUp", value = "是否补交材料,1 是，0 否")})
    public ResultForm getAeaHiReceiveByApplyinstIdAndReceiveType(String applyinstId, String receiveType, String isMakeUp) throws Exception {
        /*AeaHiReceiveVo aeaHiReceiveVo = receiveService.getAeaHiReceiveByApplyinstIdAndReceiveType(applyinstId, receiveType, isMakeUp);
        ReceiveVo receiveVo = ReceiveVo.getReceiveVo(aeaHiReceiveVo);*/
        return new ContentResultForm<>(true, receiveService.getAeaHiReceiveByApplyinstIdAndReceiveType(applyinstId, receiveType, isMakeUp), "查询回执成功");
    }

    @GetMapping("/toPrintPage/{receiveId}")
    @ApiOperation(value = "跳转回执打印-根据回执主键ID")
    @ApiImplicitParams({@ApiImplicitParam(value = "回执ID ", name = "receiveId", required = true),
            @ApiImplicitParam(name = "isMakeUp", value = "是否补正材料，0 否；1 是", defaultValue = "0")})
    public ModelAndView printReceive(@PathVariable String receiveId, String isMakeUp) throws Exception {
        /*回执类型（1：物料回执 2：受理回执 3：不收件回执 4：退件回执 5：领证回执 6：材料补正回执）*/
        ModelAndView mv = new ModelAndView("receive/default/unAccept_advice_note");
        AeaHiReceive aeaHiReceive = aeaHiReceiveMapper.getAeaHiReceiveById(receiveId);
        if (null == aeaHiReceive) return mv;
        String receiveType = aeaHiReceive.getReceiptType();
        mv.setViewName(ReceiveConstant.getModelByReceiveType(receiveType));
        mv.addObject("receiveId", receiveId);
        mv.addObject("isMakeUp", isMakeUp);
        return mv;
    }

    @GetMapping("/getOneReceiveByReceiveId/{receiveId}")
    @ApiOperation(value = "根据回执D查询单个回执")
    @ApiImplicitParam(value = "回执主键 ", name = "receiveId", required = true)
    public ResultForm getOneReceiveByReceiveId(@PathVariable String receiveId, String isMakeUp) throws Exception {
        return new ContentResultForm<>(true, receiveService.getOneReceiveByReceiveId(receiveId, isMakeUp), "查询回执成功");
    }

    @GetMapping("/getStageOrSeriesReceiveByApplyinstIds")
    @ApiOperation(value = "根据申请实例ID查询回执列表及其所属阶段或事项")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例主键数组 ", name = "applyinstIds", required = true, type = "array")})
    public ContentResultForm getStageOrSeriesReceiveByApplyinstIds(String[] applyinstIds) throws Exception {
        List<AeaHiReceiveDto> aeaHiReceive = receiveService.getStageOrSeriesReceiveByApplyinstIds(applyinstIds);
        return new ContentResultForm<>(true, aeaHiReceive, "查询回执列表及分类成功");
    }


    @GetMapping("/getReceivesByApplyinstIds")
    @ApiOperation(value = "根据申请实例ID查询回执列表-单表-不包括所属阶段或事项")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例主键数组 ", name = "applyinstIds", required = true, type = "array")})
    public ContentResultForm getReceiveByApplyinstIds(String[] applyinstIds) throws Exception {
        List<AeaHiReceive> receives = aeaHiReceiveMapper.getAeaHiReceiveByApplyinstIds(applyinstIds);
        return new ContentResultForm<>(true, receives, "查询回执列表成功");
    }

    @GetMapping("/toPrintPageForPassword")
    @ApiOperation(value = "打印密码回执")
    @ApiImplicitParams({@ApiImplicitParam(value = "类型（联系人或企业） ", name = "type", required = true, type = "string")})
    public ModelAndView toPrintPageForPassword(HttpServletRequest request, String type) throws Exception {
//        ModelAndView mv = new ModelAndView("receive/default/unAccept_advice_note");
        ModelAndView mv = new ModelAndView("receive/default/applicant_linkman_password_note");
        if ("applicant".equals(type)) {
            mv.addObject("loginName", request.getSession().getAttribute("applicantLoginName"));
            mv.addObject("password", request.getSession().getAttribute("applicantLoginPwd"));
        } else {
            mv.addObject("loginName", request.getSession().getAttribute("linkmanLoginName"));
            mv.addObject("password", request.getSession().getAttribute("linkmanLoginPwd"));
        }
        return mv;
    }


    @GetMapping("/getReceiveIdByCorrectId")
    @ApiOperation(value = "根据补正ID获取回执ID")
    @ApiImplicitParams({@ApiImplicitParam(value = "补正|补全 ID ", name = "correctId", required = true, type = "string")})
    public ResultForm getReceiveIdByCorrectId(String correctId) throws Exception {
        AeaHiReceive receive = new AeaHiReceive();
        receive.setReceiveMemo(correctId);
        List<AeaHiReceive> aeaHiReceives = aeaHiReceiveMapper.listAeaHiReceive(receive);
//        String receiveId = "";
//        if (aeaHiReceives.size() > 0) {
//            receiveId = aeaHiReceives.get(0).getReceiveId();
//        }
        return new ContentResultForm<>(true, aeaHiReceives, "success");
    }

    @GetMapping("/listReceiveByApplyinstIdAndTypes")
    @ApiOperation(value = "获取指定申报实例下指定类型的回执列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "申请实例ID，多个用，号拼接", name = "applyinstId", required = true, type = "string")
            , @ApiImplicitParam(value = "回执类型，多个用，号拼接 ", name = "types", required = true, type = "string")
    })
    public ResultForm listReceiveByApplyinstIdAndTypes(String applyinstId, String types) {

        List<AeaHiReceive> aeaHiReceives = aeaHiReceiveMapper.listReceiveByApplyinstIdAndTypes(applyinstId.split(","), types.split(","));
        return new ContentResultForm<>(true, aeaHiReceives, "success");
    }

}
