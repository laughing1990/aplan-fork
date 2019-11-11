package com.augurit.aplanmis.front.form.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaExProjBid;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.front.form.service.AeaExProjBidService;
import com.augurit.aplanmis.front.form.vo.AeaExProjBidVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 招投标信息-Controller 页面控制转发类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:刘赵雄</li>
 * <li>创建时间：2019-10-31 15:56:12</li>
 * </ul>
 */
@RestController
@RequestMapping("/rest/form/project/bid")
@Api(value = "招投标信息登记表单")
public class AeaExProjBidController {

    private static Logger logger = LoggerFactory.getLogger(AeaExProjBidController.class);

    public static final String WINBID_UNIT = "17";//中标单位
    public static final String AGENT_UNIT = "2";//招标代理机构
    public static final String COST_UNIT = "14";//造价咨询

    public static final String DIRECT_CONSIGN = "3";//直接委托

    @Autowired
    private AeaExProjBidService aeaExProjBidService;

    @Autowired
    private AeaUnitInfoService aeaUnitInfoService;

    @RequestMapping("/index.html")
    public ModelAndView projectBidIndex(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("form/biddingInfo");
        return modelAndView;
    }

    @RequestMapping("/getAeaExProjBidByProjId")
    @ApiOperation(value = "通过项目id获取招投标单位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projId", value = "项目id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getAeaExProjBidByProjId(String projId){
        try {
            if (projId != null && !"".equals(projId)) {
                logger.debug("根据ID获取AeaExProjBid对象，ID为：{}", projId);
                //判断项目是否存在
                AeaProjInfo aeaProjInfo = aeaExProjBidService.getProjInfoByProjId(projId);
                if (aeaProjInfo != null) {
                    AeaExProjBidVo aeaExProjBidVo = new AeaExProjBidVo();
                    AeaExProjBid aeaExProjBid = aeaExProjBidService.getAeaExProjBidByProjId(projId);
                    if (aeaExProjBid != null) {
                        aeaExProjBidVo = AeaExProjBidVo.from(aeaExProjBid);
                    }
                    List<AeaUnitInfo> winBidUnitList = aeaUnitInfoService.findUnitProjByProjInfoIdAndType(projId, WINBID_UNIT);//WINBID_UNIT
                    List<AeaUnitInfo> agencyUnitList = aeaUnitInfoService.findUnitProjByProjInfoIdAndType(projId, AGENT_UNIT);//AGENT_UNIT
                    List<AeaUnitInfo> costUnitList = aeaUnitInfoService.findUnitProjByProjInfoIdAndType(projId, COST_UNIT);//COST_UNIT
                    aeaExProjBidVo.setWinBidUnits(winBidUnitList);
                    aeaExProjBidVo.setAgencyUnits(agencyUnitList);
                    aeaExProjBidVo.setCostUnits(costUnitList);
                    return new ContentResultForm<AeaExProjBidVo>(true, aeaExProjBidVo);
                } else {
                    logger.debug("根据项目Id找不到项目信息，请重新确认！");
                    return new ContentResultForm<AeaExProjBidVo>(false, null,"根据项目Id找不到项目信息，请重新确认！");
                }
            } else {
                logger.debug("构建新的AeaExProjBid对象");
                return new ContentResultForm<AeaExProjBidVo>(true, new AeaExProjBidVo());
            }
        }catch(Exception e){
            return new ContentResultForm<AeaExProjBidVo>(false, new AeaExProjBidVo(),"获取招标信息失败！原因："+e.getMessage());
        }
    }

    /**
     * 保存或编辑招投标信息
     *
     * @param aeaExProjBidVo 招投标信息
     * @return 返回结果对象 包含结果信息
     * @throws Exception
     */
    @RequestMapping("/saveAeaExProjBid")
    @ApiOperation(value = "保存招投标单位信息")
    public ResultForm saveAeaExProjBid(@RequestBody  AeaExProjBidVo aeaExProjBidVo){
        try {
            if (aeaExProjBidVo != null) {
                Assert.isTrue(aeaExProjBidVo.getWinBidUnits() != null, "中标单位信息不能为空！");
                //除了直接委托，其它三类招标方式则须包含招标代理机构
                if (aeaExProjBidVo.getBidMode() != null && !DIRECT_CONSIGN.equals(aeaExProjBidVo.getBidMode())) {
                    Assert.isTrue(aeaExProjBidVo.getAgencyUnits() != null, "其他类委托时，代理机构信息不能为空！");
                }
                //招投标信息
                if (aeaExProjBidVo.getBidId() != null && !"".equals(aeaExProjBidVo.getBidId())) {
                    aeaExProjBidService.updateAeaExProjBid(aeaExProjBidVo);

                } else {
                    aeaExProjBidVo.setBidId(UUID.randomUUID().toString());
                    aeaExProjBidService.saveAeaExProjBid(aeaExProjBidVo);
                }
                List<AeaUnitProj> aeaUnitProjList=new ArrayList<AeaUnitProj>();
                //中标单位信息
                List<AeaUnitInfo> winBidUnits = aeaExProjBidVo.getWinBidUnits();
                if (winBidUnits != null) {
                    for (AeaUnitInfo aeaUnitInfo : winBidUnits) {
                        if (aeaUnitInfo.getUnitInfoId() != null && !"".equals(aeaUnitInfo.getUnitInfoId())) {
                            aeaUnitInfoService.updateAeaUnitInfo(aeaUnitInfo);
                        } else {
                            aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
                            aeaUnitInfo.setUnitType(WINBID_UNIT);
                            aeaUnitInfoService.insertAeaUnitInfo(aeaUnitInfo);
                            aeaUnitProjList.add(aeaExProjBidVo.toAeaUnitProj(aeaUnitInfo.getUnitInfoId(), WINBID_UNIT));
                        }
                    }
                }
                //招商代理机构信息
                List<AeaUnitInfo> agencyUnits = aeaExProjBidVo.getAgencyUnits();
                if (agencyUnits != null) {
                    for (AeaUnitInfo aeaUnitInfo : agencyUnits) {
                        if (aeaUnitInfo.getUnitInfoId() != null && !"".equals(aeaUnitInfo.getUnitInfoId())) {
                            aeaUnitInfoService.updateAeaUnitInfo(aeaUnitInfo);
                        } else {
                            aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
                            aeaUnitInfo.setUnitType(AGENT_UNIT);
                            aeaUnitInfoService.insertAeaUnitInfo(aeaUnitInfo);
                            aeaUnitProjList.add(aeaExProjBidVo.toAeaUnitProj(aeaUnitInfo.getUnitInfoId(), AGENT_UNIT));
                        }
                    }
                }
                //造价咨询单位信息
                List<AeaUnitInfo> costUnits = aeaExProjBidVo.getCostUnits();
                if (costUnits != null) {
                    for (AeaUnitInfo aeaUnitInfo : costUnits) {
                        if (aeaUnitInfo.getUnitInfoId() != null && !"".equals(aeaUnitInfo.getUnitInfoId())) {
                            aeaUnitInfoService.updateAeaUnitInfo(aeaUnitInfo);
                        } else {
                            aeaUnitInfo.setUnitInfoId(UUID.randomUUID().toString());
                            aeaUnitInfo.setUnitType(COST_UNIT);
                            aeaUnitInfoService.insertAeaUnitInfo(aeaUnitInfo);
                            aeaUnitProjList.add(aeaExProjBidVo.toAeaUnitProj(aeaUnitInfo.getUnitInfoId(), COST_UNIT));
                        }
                    }
                }
                //保存项目与单位关联表信息
                if(!aeaUnitProjList.isEmpty()) {
                    aeaUnitInfoService.batchInserAeaUnitProj(aeaUnitProjList);
                }
            }
            return new ContentResultForm<AeaExProjBid>(true, aeaExProjBidVo);
        }catch(Exception e){
            return new ContentResultForm<AeaExProjBidVo>(false, new AeaExProjBidVo(),"保存招标信息失败！原因："+e.getMessage());
        }
    }

}
