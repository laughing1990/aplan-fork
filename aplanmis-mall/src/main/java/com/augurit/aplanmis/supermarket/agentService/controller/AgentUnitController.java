package com.augurit.aplanmis.supermarket.agentService.controller;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaImUnitService;
import com.augurit.aplanmis.common.domain.AeaUnitBiddingAndEvaluation;
import com.augurit.aplanmis.common.domain.AgentCertinst;
import com.augurit.aplanmis.common.domain.AgentLinkmanCert;
import com.augurit.aplanmis.supermarket.agentService.service.UnitAgentService;
import com.augurit.aplanmis.supermarket.agentService.vo.AgentUnitBiddingVo;
import com.augurit.aplanmis.supermarket.agentService.vo.AgentUnitDetailVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/supermarket/agentservice")
@RestController
@Api(value = "中介机构相关接口", description = "中介机构相关接口")
public class AgentUnitController {

    @Autowired
    private UnitAgentService unitAgentService;

    @GetMapping("/getAgentUnitDetail")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true)})
    @ApiOperation(value = "查询入住机构详情", notes = "查询入住机构详情", httpMethod = "GET")
    public ResultForm getAgentUnitDetail(String unitInfoId) {
        try {
            if (StringUtils.isBlank(unitInfoId)) {
                return new ResultForm(false, "必填参数为空");
            }

            AgentUnitDetailVo vo = unitAgentService.getAgentUnitDetail(unitInfoId);
            return new ContentResultForm(true, vo, "成功查询中介详情");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    @GetMapping("/listWinBidService")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true),
            @ApiImplicitParam(name = "projName", value = "项目名称，查询参数"),
            @ApiImplicitParam(name = "serviceId", value = "服务ID，查询参数"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "查询页", required = true, dataType = "int")})
    @ApiOperation(value = "查询中介机构下中选记录列表", notes = "查询中介机构下中选记录列表", httpMethod = "GET")
    public ResultForm listWinBidService(String unitInfoId, String projName, String serviceId, Integer pageSize, Integer pageNum) {
        try {
            if (StringUtils.isBlank(unitInfoId) || pageSize == null || pageNum == null) {
                return new ResultForm(false, "必填参数为空");
            }

            Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            List<AeaUnitBiddingAndEvaluation> list = unitAgentService.listWinBidService(unitInfoId, projName, serviceId, page);

            // 查询综合评价
            AeaUnitBiddingAndEvaluation aeaUnitBiddingAndEvaluation = unitAgentService.getUnitServiceEvaluation(unitInfoId);

            AgentUnitBiddingVo agentUnitBiddingVo = new AgentUnitBiddingVo();
            agentUnitBiddingVo.setBiddingList(PageHelper.toEasyuiPageInfo(new PageInfo(list)));
            agentUnitBiddingVo.setCompEvaluation(aeaUnitBiddingAndEvaluation != null ? aeaUnitBiddingAndEvaluation.getCompEvaluation() : "");

            return new ContentResultForm(true, agentUnitBiddingVo, "成功查询中介机构下中选记录列表");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    @GetMapping("/listAgentLinkmanCertinst")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true), @ApiImplicitParam(name = "linkmanName", value = "执业人员姓名"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "查询页", required = true, dataType = "int")})
    @ApiOperation(value = "查询单位人员资格证书", notes = "查询单位人员资格证书", httpMethod = "GET")
    public ResultForm listAgentLinkmanCertinst(String unitInfoId, String linkmanName, Integer pageSize, Integer pageNum) {
        try {
            if (StringUtils.isBlank(unitInfoId) || pageSize == null || pageNum == null) {
                return new ResultForm(false, "必填参数为空");
            }

            Page page = new Page(pageNum, pageSize > 0 ? pageSize : 10);
            List<AgentLinkmanCert> list = unitAgentService.listAgentLinkmanCertinst(unitInfoId, linkmanName, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(new PageInfo(list)), "成功查询单位人员资格证书");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    @GetMapping("/listAgentHeadLinkman")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true)})
    @ApiOperation(value = "查询中介机构业务授权人信息", notes = "查询中介机构业务授权人信息", httpMethod = "GET")
    public ResultForm listAgentHeadLinkman(String unitInfoId) {
        try {
            if (StringUtils.isBlank(unitInfoId)) {
                return new ResultForm(false, "必填参数为空");
            }

            List list = unitAgentService.listAgentHeadLinkman(unitInfoId);
            return new ContentResultForm(true, list, "成功查询中介机构业务授权人信息");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    @GetMapping("/listAgentCertinst")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitInfoId", value = "单位ID", required = true),
            @ApiImplicitParam(name = "unitServiceId", value = "中介机构发布服务ID")})
    @ApiOperation(value = "查询中介机构下资格证书列表", notes = "查询中介机构下资格证书列表", httpMethod = "GET")
    public ResultForm listAgentCertinst(String unitInfoId, String unitServiceId) {
        try {
            if (StringUtils.isBlank(unitInfoId)) {
                return new ResultForm(false, "必填参数为空");
            }

            List<AgentCertinst> list = unitAgentService.listAgentCertinst(unitInfoId, unitServiceId);
            return new ContentResultForm(true, list, "成功查询中介机构下证书列表");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    @GetMapping("/getCertinstAndMajorTree")
    @ApiImplicitParams({@ApiImplicitParam(name = "certinstId", value = "证照实例ID", required = true)})
    @ApiOperation(value = "查询证照资质证书及对应的专业树", notes = "查询证照资质证书及对应的专业树", httpMethod = "GET")
    public ResultForm getCertinstAndMajorTree(String certinstId) {
        try {
            if (StringUtils.isBlank(certinstId)) {
                return new ResultForm(false, "必填参数为空");
            }

            AgentCertinst agentCertinst = unitAgentService.getCertinstAndMajorTree(certinstId);
            return new ContentResultForm(true, agentCertinst, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }

    @GetMapping("/getUnitServiceDetail")
    @ApiImplicitParams({@ApiImplicitParam(name = "unitServiceId", value = "中介机构发布服务ID", required = true)})
    @ApiOperation(value = "查询中介机构发布服务详情", notes = "查询中介机构发布服务详情", httpMethod = "GET")
    public ResultForm getUnitServiceDetail(String unitServiceId) {
        try {
            if (StringUtils.isBlank(unitServiceId)) {
                return new ResultForm(false, "必填参数为空");
            }

            AeaImUnitService aeaImUnitService = unitAgentService.getAeaImUnitServiceDetail(unitServiceId);
            return new ContentResultForm(true, aeaImUnitService, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统繁忙");
        }
    }


}
