package com.augurit.aplanmis.mall.receive.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.dto.AeaHiReceiveDto;
import com.augurit.aplanmis.common.service.receive.ReceiveService;
import com.augurit.aplanmis.common.service.receive.utils.ReceivePDFTemplate;
import com.augurit.aplanmis.common.service.receive.utils.ReceivePDFUtils;
import com.augurit.aplanmis.common.service.receive.vo.ReceiveBaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
@RestController
@Slf4j
@Api(value = "回执相关接口", tags = "回执 --> 回执相关接口")
@RequestMapping("/rest/user/receive")
public class ReceiveController {

    @Autowired
    private ReceiveService receiveService;

    @GetMapping("/getStageOrSeriesReceiveByApplyinstIds")
    @ApiOperation(value = "根据申请实例ID查询回执列表及其所属阶段或事项")
    @ApiImplicitParams({@ApiImplicitParam(value = "申请实例主键数组 ", name = "applyinstIds", required = true, type = "array")})
    public ContentResultForm getStageOrSeriesReceiveByApplyinstIds(String[] applyinstIds) throws Exception {
        List<AeaHiReceiveDto> aeaHiReceive = receiveService.getStageOrSeriesReceiveByApplyinstIds(applyinstIds);
        return new ContentResultForm<>(true, aeaHiReceive, "查询回执列表及分类成功");
    }


    @RequestMapping(value = "/toPDF/{receiveId}", method = RequestMethod.GET)
    @ApiOperation(value = "打印回执的PDF版本")
    @ApiImplicitParams({@ApiImplicitParam(value = "回执ID", name = "receiveId", required = true),
            @ApiImplicitParam(name = "isMakeUp", value = "是否补正材料，0 否；1 是", defaultValue = "0")})
    public void testPrintPdf(@PathVariable String receiveId, String isMakeUp, HttpServletResponse resp) throws Exception {
        //根据回执D查询单个回执
        ReceiveBaseVo receiveBaseVo = null;
        if (!StringUtils.isBlank(receiveId)) {
            receiveBaseVo = receiveService.getOneReceiveByReceiveId(receiveId, "0");
        }
        String str = "";//pdf保存地址
        if (receiveBaseVo != null) {
            str = ReceivePDFUtils.createPDF(receiveBaseVo);
        }
        //读取指定路径下的pdf文件
        ReceivePDFTemplate.readPdf(str, resp);
        File file = new File(str);
        if (file.isFile()) {
            file.delete();//删除pdf文件
        }
    }
}
