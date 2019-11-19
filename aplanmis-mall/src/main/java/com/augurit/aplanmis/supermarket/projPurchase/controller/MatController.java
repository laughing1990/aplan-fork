package com.augurit.aplanmis.supermarket.projPurchase.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.mall.userCenter.service.RestApplyMatService;
import com.augurit.aplanmis.mall.userCenter.vo.AutoImportParamVo;
import com.augurit.aplanmis.mall.userCenter.vo.UploadMatReturnVo;
import com.augurit.aplanmis.supermarket.projPurchase.service.MatStateService;
import com.augurit.aplanmis.supermarket.projPurchase.vo.mat.ItemMatVo;
import com.augurit.aplanmis.supermarket.projPurchase.vo.mat.Mat2MatInstVo;
import com.augurit.aplanmis.supermarket.projPurchase.vo.mat.MatUploadVo;
import com.augurit.aplanmis.supermarket.projPurchase.vo.mat.SaveMatinstVo;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "项目需求采购管理接口-材料相关", tags = "项目采购管理接口-材料相关")
@RequestMapping("/supermarket/purchase/mat")
@RestController
public class MatController {

    @Autowired
    private MatStateService matStateService;
    @Autowired
    private AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private RestApplyMatService restApplyMatService;

    @ApiOperation(value = "获取中介事项材料-不分情形", tags = "项目采购页---根据itemVerId获取所有材料列表")
    @GetMapping("/getItemMatList")
    public ResultForm getAeaItemInoutMatListByItemVerId(String itemVerId) throws Exception {
        List<ItemMatVo> applyMatList = matStateService.getApplyMatList(itemVerId);
        return new ContentResultForm<>(true, applyMatList, "success");
    }

    @ApiOperation(value = "上传事项材料附件-不分情形", tags = "项目采购页-单个matId下的附件上传")
    @GetMapping("/att/upload")
    public ResultForm uploadMat(MatUploadVo uploadVo, HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("utf-8");//设置编码，防止附件名称乱码
        AeaHiItemMatinst itemMatinst = new AeaHiItemMatinst();
        BeanUtils.copyProperties(uploadVo, itemMatinst);
        String matinstId = aeaHiItemMatinstService.saveAeaHiItemMatinst(itemMatinst, request);
        return new ContentResultForm<>(true, matinstId, "Mat attachment upload success");
    }

    @GetMapping("/att/download")
    @ApiOperation(value = "中介超市项目采购页面--> 下载事项材料电子件")
    @ApiImplicitParam(name = "detailIds", value = "附件ID", dataType = "string", required = true)
    public ContentResultForm downloadAttachment(String detailIds, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(detailIds)) {
                return new ContentResultForm<>(false, null, "找不到文件!");
            }
            String[] str = detailIds.split(",");
            fileUtilsService.downloadAttachment(str, response, request, false);
            return new ContentResultForm<>(true);
        } catch (Exception e) {
            return new ContentResultForm<>(false, null, "找不到文件!");
        }
    }

    @GetMapping("/att/preview")
    @ApiOperation(value = "中介超市项目采购页面--> 预览电子件")
    @ApiImplicitParam(name = "detailId", value = "附件ID", dataType = "string", required = true)
    public ModelAndView preview(String detailId, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
        ModelAndView modelAndView = fileUtilsService.preview(detailId, request, response, redirectAttributes);
        return modelAndView;
    }

    @PostMapping("/att/upload/auto")
    @ApiOperation(value = "中介超市项目采购页面--> 一键自动分拣", tags = "项目采购页-一键分拣材料")
    public ContentResultForm<List<UploadMatReturnVo>> saveFilesAuto(@ModelAttribute AutoImportParamVo autoImportVo, HttpServletRequest request) throws Exception {
        List<UploadMatReturnVo> list = restApplyMatService.saveFilesAuto(autoImportVo, request);
        return new ContentResultForm<>(true, list, "success");
    }

    @PostMapping("/matinst/delete")
    @ApiOperation(value = "中介超市项目采购页面--> 根据matinsId删除材料实例")
    @ApiImplicitParam(name = "matinstId", value = "材料实例id, 多个用逗号隔开", dataType = "string", required = true)
    public ContentResultForm deleteMatinst(@RequestParam String matinstId) {
        Assert.isTrue(StringUtils.isNotBlank(matinstId), "matinstId is null");

        matStateService.deleteMatinst(matinstId);
        return new ContentResultForm<>(true, null, "Matinst is Deleted");
    }

    @PostMapping("/matinst/batch/save")
    @ApiOperation(value = "中介超市项目采购页面---根据材料定义生成材料实例id", tags = "项目采购页-批量保存材料，当勾线全部时使用")
    public ContentResultForm<List<Mat2MatInstVo>> saveMatinsts(@RequestBody SaveMatinstVo saveMatinstVo) {
        List<Mat2MatInstVo> mat2MatInstVos = matStateService.saveMatinsts(saveMatinstVo);
        return new ContentResultForm<>(true, mat2MatInstVos, "Batch save matinst success");
    }
}
