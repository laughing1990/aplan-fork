package com.augurit.aplanmis.common.form.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.constants.GDUnitType;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.service.unit.AeaUnitInfoService;
import com.augurit.aplanmis.common.form.service.AeaExProjBidService;
import com.augurit.aplanmis.common.form.vo.AeaExProjBidVo;
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
                    List<AeaUnitInfo> winBidUnitList = aeaExProjBidService.findUnitProjByProjInfoIdAndType(projId, GDUnitType.CONTRACTING_UNIT.getValue());//中标（承包）单位
                    List<AeaUnitInfo> agencyUnitList = aeaExProjBidService.findUnitProjByProjInfoIdAndType(projId, GDUnitType.BIDDING_AGENCY.getValue());//招商代理机构
                    List<AeaUnitInfo> costUnitList = aeaExProjBidService.findUnitProjByProjInfoIdAndType(projId, GDUnitType.COST_CONSULTING.getValue());//造价咨询
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
            logger.error("获取招标信息失败！原因："+e.getMessage());
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
    public ResultForm saveAeaExProjBid(@RequestBody  AeaExProjBidVo aeaExProjBidVo) {
        ContentResultForm<AeaExProjBidVo> resultForm = new ContentResultForm<AeaExProjBidVo>(true);
        try {
            if (aeaExProjBidVo != null) {
                //判断项目是否存在
                AeaProjInfo aeaProjInfo = aeaExProjBidService.getProjInfoByProjId(aeaExProjBidVo.getProjInfoId());
                if (aeaProjInfo != null) {
                    Assert.isTrue(aeaExProjBidVo.getWinBidUnits() != null, "中标单位信息不能为空！");
                    //除了直接委托，其它三类招标方式则须包含招标代理机构
                    if (aeaExProjBidVo.getBidMode() != null && !DIRECT_CONSIGN.equals(aeaExProjBidVo.getBidMode())) {
                        Assert.isTrue(aeaExProjBidVo.getAgencyUnits() != null, "其他类委托时，代理机构信息不能为空！");
                    }
                    //如果是直接委托，需要删除掉 招标代理、造价咨询 类型的关联表信息(暂时不保存)
                    if (DIRECT_CONSIGN.equals(aeaExProjBidVo.getBidMode())) {
                        List<String> unitTypes = new ArrayList<String>();
                        unitTypes.add(GDUnitType.BIDDING_AGENCY.getValue());
                        unitTypes.add(GDUnitType.COST_CONSULTING.getValue());
                        aeaExProjBidService.delUnitProjInfo(aeaExProjBidVo.getProjInfoId(), unitTypes,"0");
                    }
                    //招投标信息
                    if (aeaExProjBidVo.getBidId() != null && !"".equals(aeaExProjBidVo.getBidId())) {
                        aeaExProjBidService.updateAeaExProjBid(aeaExProjBidVo);

                    } else {
                        aeaExProjBidVo.setBidId(UUID.randomUUID().toString());
                        aeaExProjBidService.saveAeaExProjBid(aeaExProjBidVo);
                    }

                    //新增的单位项目关联表信息
                    List<AeaUnitProj> aeaUnitProjNewList = new ArrayList<AeaUnitProj>();
                    //中标单位信息
                    List<AeaUnitInfo> winBidUnits = aeaExProjBidVo.getWinBidUnits();
                    aeaExProjBidService.saveOrUpdateUnitInfo(aeaExProjBidVo, winBidUnits, GDUnitType.CONTRACTING_UNIT.getValue(), aeaUnitProjNewList);

                    //招商代理机构信息
                    List<AeaUnitInfo> agencyUnits = aeaExProjBidVo.getAgencyUnits();
                    aeaExProjBidService.saveOrUpdateUnitInfo(aeaExProjBidVo, agencyUnits, GDUnitType.BIDDING_AGENCY.getValue(), aeaUnitProjNewList);

                    //造价咨询单位信息
                    List<AeaUnitInfo> costUnits = aeaExProjBidVo.getCostUnits();
                    aeaExProjBidService.saveOrUpdateUnitInfo(aeaExProjBidVo, costUnits, GDUnitType.COST_CONSULTING.getValue(), aeaUnitProjNewList);

                    //新增保存项目与单位关联表信息
                    if (!aeaUnitProjNewList.isEmpty()) {
                        aeaUnitInfoService.batchInserAeaUnitProj(aeaUnitProjNewList);
                    }

                    resultForm.setSuccess(true);
                    resultForm.setMessage("保存成功！");
                    resultForm.setContent(aeaExProjBidVo);

                } else {
                    logger.debug("根据项目Id找不到项目信息，请重新确认！");
                    resultForm.setSuccess(false);
                    resultForm.setMessage("根据项目Id找不到项目信息，请重新确认！");
                }
            } else {
                logger.debug("保存信息不能为空！");
                resultForm.setSuccess(false);
                resultForm.setMessage("保存信息不能为空！");
            }

        } catch (Exception e) {
            logger.error("保存招标信息失败！原因：" + e.getMessage());
            resultForm.setSuccess(false);
            resultForm.setMessage("保存招标信息失败！原因：" + e.getMessage());
            resultForm.setContent(aeaExProjBidVo);
        }
        return resultForm;
    }

}
