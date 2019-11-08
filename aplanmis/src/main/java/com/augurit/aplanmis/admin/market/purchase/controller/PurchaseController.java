package com.augurit.aplanmis.admin.market.purchase.controller;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.purchase.service.PurchaseService;
import com.augurit.aplanmis.admin.market.purchase.vo.UnitRequireQualVo;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.vo.AeaImProjPurchaseDetailVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 项目需求采购发布管理-Controller
 */
@RestController
@RequestMapping("/supermarket/purchase")
@Api(value = "采购需求审核")
public class PurchaseController {


    @Autowired
    private PurchaseService purchaseService;

    @RequestMapping("/index.do")
    public ModelAndView index(AeaImProjPurchase aeaImProjPurchase) {
        return new ModelAndView("ui-jsp/supermarket_manage/purchase/index");
    }

    @RequestMapping("/index.html")
    public ModelAndView purchaseIndex() {
        return new ModelAndView("admin/supermarket/purchase/purchase-index");
    }

    @ApiOperation(value = "采购需求详情页")
    @ApiImplicitParam(name = "projPurchaseId", value = "projPurchaseId")
    @RequestMapping("/purchase-detail.html")
    public ModelAndView purchaseDetail(String projPurchaseId) {
        ModelAndView modelAndView = new ModelAndView("admin/supermarket/purchase/purchase-detail");
        modelAndView.addObject("projPurchaseId", projPurchaseId);
        return modelAndView;
    }

    @RequestMapping("/list.do")
    public EasyuiPageInfo<AeaImProjPurchase> list(AeaImProjPurchase aeaImProjPurchase, Page page) throws Exception {
        List<AeaImProjPurchase> list = purchaseService.listAeaImProPurchase(aeaImProjPurchase, page);
        return PageHelper.toEasyuiPageInfo(new PageInfo(list));
    }

    @PostMapping("/listPurchase")
    @ApiOperation(value = "查询项目采购列表")
    public ResultForm listPurchase(AeaImProjPurchase aeaImProjPurchase, Integer pageSize, Integer pageNum) throws Exception {
        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        Page page = new Page(pageNum, pageSize);
        List<AeaImProjPurchase> list = purchaseService.listAeaImProPurchase(aeaImProjPurchase, page);
        return new ContentResultForm<>(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)), "success");
    }

    @RequestMapping("/getProPurchase.do")
    public ModelAndView getProPurchase(ModelMap modelMap, String projPurchaseId) throws Exception {
        AeaImProjPurchaseDetailVo aeaImProjPurchase = purchaseService.getAeaImProPurchase(projPurchaseId);
        modelMap.put("aeaImProjPurchase", aeaImProjPurchase);
        return new ModelAndView("ui-jsp/supermarket_manage/purchase/detail");
    }

    @RequestMapping("/getPurchaseDetail")
    public ResultForm getPurchaseDetail(String projPurchaseId) throws Exception {
        AeaImProjPurchaseDetailVo aeaImProjPurchase = purchaseService.getAeaImProPurchase(projPurchaseId);
        return new ContentResultForm<>(true, aeaImProjPurchase, "success");
    }

    @RequestMapping("/audit.do")
    public ResultForm audit(AeaImProjPurchase aeaImProjPurchase) {
        try {
            if (StringUtils.isBlank(aeaImProjPurchase.getProjPurchaseId())
                    || StringUtils.isBlank(aeaImProjPurchase.getAuditOpinion())
                    || StringUtils.isBlank(aeaImProjPurchase.getAuditFlag())) {

                return new ResultForm(false);
            }
            purchaseService.audit(aeaImProjPurchase);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }

        return new ResultForm(true);
    }

    @RequestMapping("/getMajorTreeByUnitRequireId.do")
    public List<UnitRequireQualVo> getMajorTreeByUnitRequireId(String unitRequireId) throws Exception {
        return purchaseService.getMajorTreeByUnitRequireId(unitRequireId);
    }

}
