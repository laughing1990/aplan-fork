package com.augurit.aplanmis.supermarket.bidProPurchase.controller;

import com.alibaba.fastjson.JSONException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaImUnitBidding;
import com.augurit.aplanmis.common.domain.AeaItemService;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.vo.AeaImProjPurchaseDetailVo;
import com.augurit.aplanmis.supermarket.agentService.service.UnitAgentService;
import com.augurit.aplanmis.supermarket.agentService.vo.AgentUnitDetailVo;
import com.augurit.aplanmis.supermarket.bidProPurchase.service.BidProjectService;
import com.augurit.aplanmis.supermarket.projPurchase.service.AeaImProjPurchaseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/aea/supermarket/bidProjectManage")
@RestController
@Api(value = "中介机构项目管理接口", description = "中介机构项目管理接口")
public class BidProjectController {


    @Autowired
    private BidProjectService projectService;

    @Autowired
    private AeaImProjPurchaseService aeaImProjPurchaseService;

    @Autowired
    private UnitAgentService unitAgentService;

    /**
     * 查询企业可竞价项目
     *
     * @param unitInfoId 企业ID
     * @param pageSize   分页大小
     * @param pageNum    查询开始页
     * @return
     * @throws Exception
     */
    @GetMapping("/listCanBidAeaImProjPurchase")
    @ApiOperation(value = "查询中介机构可竞价项目", notes = "查询中介机构可竞价项目", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "projName", value = "项目名称", dataType = "String"),
            @ApiImplicitParam(name = "itemVerId", value = "中介事项版本ID", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true), @ApiImplicitParam(name = "pageNum", value = "查询页", required = true)})
    public ResultForm listCanBidAeaImProjPurchase(String unitInfoId, String projName, String itemVerId, Integer pageSize, Integer pageNum) {
        try {
            if (StringUtils.isBlank(unitInfoId) || pageSize == null || pageNum == null) {
                return new ResultForm(false, "必填参数为空");
            }

            Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            List<AeaImProjPurchaseDetailVo> list = projectService.listCanBidAeaImProjPurchase(unitInfoId, projName, itemVerId, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    /**
     * 综合数据展示
     *
     * @param unitInfoId
     * @return
     * @throws Exception
     */
    @GetMapping("/info")
    @ApiOperation(value = "中介机构个人中心综合数据展示", notes = "综合数据展示", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true)})
    public ResultForm info(String unitInfoId) {
        try {
            if (StringUtils.isBlank(unitInfoId)) {
                return new ResultForm(false, "必填参数为空");
            }

            Map<String, Object> result = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(unitInfoId)) {
                result = projectService.getCompInfo(unitInfoId);
            }
            return new ContentResultForm(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    /**
     * 中介事项列表
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/listAeaItemBasic")
    @ApiOperation(value = "查询中介事项列表", notes = "查询中介事项列表", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true),
            @ApiImplicitParam(name = "agentItemName", value = "事项名称"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "查询页", required = true, dataType = "int")})
    public ResultForm listAeaItemBasic(AeaItemService aeaItemService, Integer pageNum, Integer pageSize) {
        try {
            if (aeaItemService == null || StringUtils.isBlank(aeaItemService.getUnitInfoId())
                    || pageSize == null || pageNum == null) {
                return new ResultForm(false, "必填参数为空");
            }

            Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            List<AeaItemService> list = projectService.listAeaItemBasic(aeaItemService, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    /**
     * 中介机构报价项目列表
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/listUnitBiddingProjPurchase")
    @ApiOperation(value = "查询中介机构报价项目列表", notes = "查询中介机构报价项目列表", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true),
            @ApiImplicitParam(name = "type", value = "类型：0-已报名项目 1-已中选项目 2-已签约项目 3-服务中项目", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "查询页", required = true, dataType = "int")})
    public ResultForm listUnitBiddingProjPurchase(AeaImUnitBidding aeaImUnitBidding, String type, Integer pageNum, Integer pageSize) {
        try {
            if (aeaImUnitBidding == null || StringUtils.isBlank(aeaImUnitBidding.getUnitInfoId()) || StringUtils.isBlank(type)
                    || pageSize == null || pageNum == null) {
                return new ResultForm(false, "必填参数为空");
            }

            Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            List<AeaImUnitBidding> list = projectService.listUnitBiddingProjPurchase(aeaImUnitBidding, type, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    /**
     * 中介机构项目报名接口
     *
     * @param projPurchaseId
     * @param unitInfoId
     * @param isParticipate
     * @param linkmanInfoId
     * @param serviceLinkmanIds
     * @return
     */
    @PostMapping("/signUpProjPurchase")
    @ApiOperation(value = "中介机构项目报名接口", notes = "中介机构项目报名接口", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "projPurchaseId", value = "项目采购需求发布ID", required = true),
            @ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true),
            @ApiImplicitParam(name = "isParticipate", value = "是否参与现场摇号，0-否，1-是", required = true),
            @ApiImplicitParam(name = "linkmanInfoId", value = "业务授权人ID", required = true),
            @ApiImplicitParam(name = "serviceLinkmanIds", value = "执业/职业人员ID", required = true)})
    public ResultForm signUpProjPurchase(String projPurchaseId, String unitInfoId, String isParticipate, String linkmanInfoId, String serviceLinkmanIds) {
        try {
            if (StringUtils.isBlank(projPurchaseId) || StringUtils.isBlank(unitInfoId) || StringUtils.isBlank(isParticipate)
                    || StringUtils.isBlank(linkmanInfoId)) {
                return new ResultForm(false, "必填参数为空");
            }

            return projectService.signUpProjPurchase(projPurchaseId, unitInfoId, isParticipate, linkmanInfoId, serviceLinkmanIds);
        } catch (JSONException e) {
            e.printStackTrace();
            return new ResultForm(false, "参数格式错误");
        } catch (Throwable t) {
            t.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    /**
     * 中介机构取消报名接口
     *
     * @param unitBiddingId
     * @return
     */
    @PostMapping("/cancelProjPurchase")
    @ApiOperation(value = "中介机构取消报名接口", notes = "中介机构取消报名接口", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitBiddingId", value = "企业报价ID", required = true)})
    public ResultForm cancelProjPurchase(String unitBiddingId) {
        try {
            if (StringUtils.isBlank(unitBiddingId)) {
                return new ResultForm(false, "必填参数为空");
            }

            return projectService.cancelProjPurchase(unitBiddingId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    /**
     * 查看报名接口
     *
     * @param projPurchaseId
     * @param unitInfoId
     * @param unitBiddingId
     * @return
     */
    @GetMapping("/getSignUpDetail")
    @ApiOperation(value = "查看报名接口", notes = "查看报名接口", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "projPurchaseId", value = "项目采购需求发布ID", required = true),
            @ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true),
            @ApiImplicitParam(name = "unitBiddingId", value = "企业报价ID", required = true)})
    public ResultForm getSignUpDetail(String projPurchaseId, String unitInfoId, String unitBiddingId) {
        try {
            if (StringUtils.isBlank(projPurchaseId) || StringUtils.isBlank(unitInfoId) || StringUtils.isBlank(unitBiddingId)) {
                return new ResultForm(false, "必填参数为空");
            }

            Map result = new HashMap();

            // 查询项目详情
            AeaImProjPurchaseDetailVo aeaImProjPurchaseDetailVo = aeaImProjPurchaseService.getPublicProjPurchaseDatail(projPurchaseId);
            result.put("projPurchase", aeaImProjPurchaseDetailVo);

            // 查询中介详情
            AgentUnitDetailVo agentUnitDetailVo = unitAgentService.getAgentUnitDetail(unitInfoId);
            result.put("unitInfo", agentUnitDetailVo);

            // 查询报名详情
            AeaImUnitBidding aeaImUnitBidding = projectService.getUnitBiddingDetail(unitBiddingId);
            result.put("unitBidding", aeaImUnitBidding);

            return new ContentResultForm(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    /**
     * 查看竞价金额接口
     *
     * @param unitBiddingId
     * @param projPurchaseId
     * @return
     */
    @GetMapping("/getBiddingPriceDetail")
    @ApiOperation(value = "查看竞价金额接口", notes = "查看竞价金额接口", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitBiddingId", value = "企业报价ID", required = true),
            @ApiImplicitParam(name = "projPurchaseId", value = "项目采购需求发布ID", required = true)})
    public ResultForm getBiddingPriceDetail(String unitBiddingId, String projPurchaseId) {
        try {
            if (StringUtils.isBlank(unitBiddingId) || StringUtils.isBlank(projPurchaseId)) {
                return new ResultForm(false, "必填参数为空");
            }

            Map result = projectService.getBiddingPriceDetail(unitBiddingId, projPurchaseId);
            return new ContentResultForm(true, result != null ? result : new HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    /**
     * 中介机构竞价接口
     *
     * @param unitBiddingId
     * @return
     */
    @PostMapping("/biddingProjPurchase")
    @ApiOperation(value = "中介机构竞价接口", notes = "中介机构竞价接口", httpMethod = "POST")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitBiddingId", value = "企业报价ID", required = true),
            @ApiImplicitParam(name = "price", value = "价格", required = true)})
    public ResultForm biddingProjPurchase(String unitBiddingId, String price) {
        try {
            if (StringUtils.isBlank(unitBiddingId) || StringUtils.isBlank(price)) {
                return new ResultForm(false, "必填参数为空");
            }

            String regex = "[0-9]*";
            if (!price.matches(regex)) {
                return new ResultForm(false, "参数格式错误");
            }

            return projectService.biddingProjPurchase(unitBiddingId, price);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    /**
     * 中介机构业务授权人列表
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/listClientServiceLinkmanInfo")
    @ApiOperation(value = "查询中介机构业务授权人列表", notes = "查询中介机构业务授权人列表", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true),
            @ApiImplicitParam(name = "serviceId", value = "服务ID", required = true)})
    public ResultForm listClientServiceLinkmanInfo(String serviceId, String unitInfoId) {
        try {
            if (StringUtils.isBlank(serviceId) || StringUtils.isBlank(unitInfoId)) {
                return new ResultForm(false, "必填参数为空");
            }

            List<AeaLinkmanInfo> list = projectService.listClientServiceLinkmanInfo(serviceId, unitInfoId);
            return new ContentResultForm(true, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

}
