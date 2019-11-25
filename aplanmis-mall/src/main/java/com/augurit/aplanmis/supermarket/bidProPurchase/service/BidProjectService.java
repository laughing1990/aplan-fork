package com.augurit.aplanmis.supermarket.bidProPurchase.service;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.DateUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.vo.AeaImProjPurchaseDetailVo;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class BidProjectService {

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private AeaImCertinstMajorMapper aeaImCertinstMajorMapper;

    @Autowired
    private AeaImUnitBiddingMapper aeaImUnitBiddingMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;

    @Autowired
    private AeaImUnitServiceMapper aeaImUnitServiceMapper;

    @Autowired
    private AeaImBiddingEmployeesMapper aeaImBiddingEmployeesMapper;

    @Autowired
    private AeaImBiddingPriceMapper aeaImBiddingPriceMapper;

    @Autowired
    private AeaImPurchaseinstMapper aeaImPurchaseinstMapper;

    @Autowired
    private AeaImClientServiceMapper aeaImClientServiceMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;

    public List<AeaImProjPurchaseDetailVo> listCanBidAeaImProjPurchase(String unitInfoId, String projName, String itemVerId, Page page) {
        PageHelper.startPage(page);
        return listCanBidAeaImProjPurchase(unitInfoId, projName, itemVerId, "");
    }

    public Map getCompInfo(String unitInfoId) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        // 可报名项目
        List<AeaImProjPurchaseDetailVo> canBidList = listCanBidAeaImProjPurchase(unitInfoId, "", "", "");
        int canBidNum = canBidList.size();
        result.put("canBidNum", canBidNum);

        // 已报名项目
        Long signUpNum = aeaImUnitBiddingMapper.countUnitBiddingInfo(unitInfoId, "0");
        result.put("signUpNum", signUpNum);

        // 已中选项目
        Long biddingNum = aeaImUnitBiddingMapper.countUnitBiddingInfo(unitInfoId, "1");
        result.put("biddingNum", biddingNum);

        // 已签约项目
        Long signContractNum = aeaImUnitBiddingMapper.countUnitBiddingInfo(unitInfoId, "2");
        result.put("signContractNum", signContractNum);

        // 服务中项目
        Long serviceNum = aeaImUnitBiddingMapper.countUnitBiddingInfo(unitInfoId, "3");
        result.put("serviceNum", serviceNum);

        // 综合评价
        AeaUnitBiddingAndEvaluation evaluation = aeaImUnitBiddingMapper.getUnitServiceEvaluation(unitInfoId);
        result.put("compEvaluation", evaluation != null ? evaluation.getCompEvaluation() : "");

        return result;
    }

    public List<AeaImProjPurchaseDetailVo> listCanBidAeaImProjPurchase(String unitInfoId, String projName, String itemVerId, String projPurchaseId) {
        List<AeaImProjPurchaseDetailVo> result = new ArrayList<AeaImProjPurchaseDetailVo>();
        List<AeaImProjPurchaseDetailVo> list = aeaImProjPurchaseMapper.listCanBidProjPurchaseMajorRequire(unitInfoId, projName, itemVerId, projPurchaseId);
        if (list != null && list.size() > 0) {
            // 获取中介结构资质等级
            List<AeaImCertinstMajor> unitMajorList = aeaImCertinstMajorMapper.listCertinstMajorByUnitInfoId(unitInfoId);
            for (AeaImProjPurchaseDetailVo aeaImProjPurchaseDetailVo : list) {
                boolean bidFlag = true;
                String isQualRequire = aeaImProjPurchaseDetailVo.getIsQualRequire();

                // 需要资质要求
                if ("1".equals(isQualRequire)) {
                    // 1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项
                    String qualRequireType = aeaImProjPurchaseDetailVo.getQualRequireType();
                    bidFlag = !"1".equals(qualRequireType);
                    List<AeaImMajorQual> majorQualList = aeaImProjPurchaseDetailVo.getMajorQualList();

                    if (majorQualList != null && majorQualList.size() > 0
                            &&unitMajorList != null && unitMajorList.size() > 0) {
                        for (AeaImMajorQual aeaImMajorQual : majorQualList) {
                            String majorId = aeaImMajorQual.getMajorId();
                            String priority = aeaImMajorQual.getPriority();
                            boolean qualFlag = false;

                            for (AeaImCertinstMajor aeaImCertinstMajor : unitMajorList) {
                                String unitMajorSeq = aeaImCertinstMajor.getMajorSeq();
                                String unitPriority = aeaImCertinstMajor.getPriority();

                                if (StringUtils.isNotBlank(majorId) && StringUtils.isNotBlank(priority)
                                        &&StringUtils.isNotBlank(unitMajorSeq) && StringUtils.isNotBlank(unitPriority)) {
                                    try {
                                        if (unitMajorSeq.indexOf(majorId) > 0 && Integer.parseInt(unitPriority) <= Integer.parseInt(priority)) {
                                            qualFlag = true;
                                            break;
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            if (qualFlag) {
                                if ("1".equals(qualRequireType)) {
                                    bidFlag = true;
                                    break;
                                }
                            } else {
                                bidFlag = false;
                            }
                        }
                    }
                }
                if (bidFlag) {
                    result.add(aeaImProjPurchaseDetailVo);
                }
            }
        }

        return result;
    }

    public List<AeaItemService> listAeaItemBasic(AeaItemService aeaItemService, Page page) {
        PageHelper.startPage(page);
        return aeaItemBasicMapper.listAgentItemServiceByUnitInfoId(aeaItemService);
    }

    public List<AeaImUnitBidding> listUnitBiddingProjPurchase(AeaImUnitBidding aeaImUnitBidding, String type, Page page) throws Exception {
        PageHelper.startPage(page);
        return aeaImUnitBiddingMapper.listUnitBidding(aeaImUnitBidding, type);
    }

    public ResultForm signUpProjPurchase(String projPurchaseId, String unitInfoId, String isParticipate, String linkmanInfoId, String serviceLinkmanIds) throws Exception {
        AeaImProjPurchaseDetailVo aeaImProjPurchaseDetailVo = null;
        // 保存联系人信息
        List<AeaImBiddingEmployees> aeaImBiddingEmployeesList = new ArrayList<AeaImBiddingEmployees>();

        // 判断是否为可报名项目
        List<AeaImProjPurchaseDetailVo> projPurchaseList = listCanBidAeaImProjPurchase(unitInfoId, "", "", projPurchaseId);
        if (projPurchaseList != null && projPurchaseList.size() > 0) {
            aeaImProjPurchaseDetailVo = projPurchaseList.get(0);
        } else {
            return new ResultForm(false, "项目不存在");
        }

        // 获取中介机构发布服务信息
        List<AeaImUnitService> aeaImUnitServices = aeaImUnitServiceMapper.getAeaImUnitServiceByServiceIds(aeaImProjPurchaseDetailVo.getServiceId(), unitInfoId);
        if (aeaImUnitServices.size() == 0) {
            return new ResultForm(false, "发布服务不存在");
        }

        AeaImUnitService aeaImUnitService = aeaImUnitServices.get(0);
        // 保存企业报价信息
        Date createTime = new Date();
        String creater = SecurityContext.getCurrentUserName();
        String unitBiddingId = UUID.randomUUID().toString();

        AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
        aeaImUnitBidding.setUnitBiddingId(unitBiddingId);
        aeaImUnitBidding.setProjPurchaseId(projPurchaseId);
        aeaImUnitBidding.setUnitInfoId(unitInfoId);
        aeaImUnitBidding.setIsCancelSignup("0");
        aeaImUnitBidding.setIsWonBid("0");
        aeaImUnitBidding.setAuditFlag("1");
        aeaImUnitBidding.setIsUploadContract("0");
        aeaImUnitBidding.setIsUploadResult("0");
        aeaImUnitBidding.setIsEvaluate("0");
        aeaImUnitBidding.setIsDelete("0");
        aeaImUnitBidding.setCreateTime(createTime);
        aeaImUnitBidding.setCreater(creater);

        aeaImUnitBidding.setUnitServiceId(aeaImUnitService.getUnitServiceId());
        aeaImUnitBidding.setLinkmanInfoId(linkmanInfoId);

        if ("1".equals(aeaImProjPurchaseDetailVo.getIsRegisterRequire())) {
            if (StringUtils.isBlank(serviceLinkmanIds)) {
                return new ResultForm(false, "请选择执业/职业人员");
            }

            // 判断联系人是否存在
            List serviceLinkmanIdList = JSONArray.parseArray(serviceLinkmanIds);
            List<AeaLinkmanInfo> linkmanList = aeaLinkmanInfoMapper.getLinkmanList(unitInfoId, aeaImProjPurchaseDetailVo.getServiceId(), serviceLinkmanIdList);
            if (!(linkmanList != null && linkmanList.size() == serviceLinkmanIdList.size())) {
                return new ResultForm(false, "联系人不存在");
            }

            linkmanList.forEach(item -> {
                AeaImBiddingEmployees aeaImBiddingEmployees = new AeaImBiddingEmployees();
                aeaImBiddingEmployees.setBiddingEmployeesId(UUID.randomUUID().toString());
                aeaImBiddingEmployees.setServiceLinkmanId(item.getServiceLinkmanId());
                aeaImBiddingEmployees.setIsDelete("0");
                aeaImBiddingEmployees.setCreater(creater);
                aeaImBiddingEmployees.setCreateTime(createTime);
                aeaImBiddingEmployees.setUnitBiddingId(unitBiddingId);
                aeaImBiddingEmployeesList.add(aeaImBiddingEmployees);
            });
        }
        aeaImUnitBidding.setRootOrgId(topOrgId);
        aeaImUnitBiddingMapper.insertAeaImUnitBidding(aeaImUnitBidding);

        if (aeaImBiddingEmployeesList.size() > 0) {
            aeaImBiddingEmployeesMapper.insertAeaImBiddingEmployeesList(aeaImBiddingEmployeesList);
        }

        return new ResultForm(true, "报名成功");
    }

    public ResultForm cancelProjPurchase(String unitBiddingId) throws Exception {
        AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
        aeaImUnitBidding.setUnitBiddingId(unitBiddingId);
        aeaImUnitBidding.setIsCancelSignup("1");
        aeaImUnitBidding.setModifier(SecurityContext.getCurrentUserName());
        aeaImUnitBidding.setModifyTime(new Date());
        aeaImUnitBiddingMapper.updateAeaImUnitBidding(aeaImUnitBidding);
        return new ResultForm(true, "取消报名成功");
    }

    public AeaImUnitBidding getUnitBiddingDetail(String unitBiddingId) throws Exception {
        AeaImUnitBidding aeaImUnitBidding = aeaImUnitBiddingMapper.getUnitBiddingAndLinkman(unitBiddingId);

        if (aeaImUnitBidding != null) {
            List<AeaImBiddingEmployees> biddingEmployeesList = aeaImBiddingEmployeesMapper.listAeaImBiddingEmployeesByUnitBiddingId(unitBiddingId);
            aeaImUnitBidding.setBiddingEmployeesList(biddingEmployeesList);
        }

        return aeaImUnitBidding;
    }

    public Map getBiddingPriceDetail(String unitBiddingId, String projPurchaseId) {
        return aeaImBiddingPriceMapper.getBiddingPriceByProjPurchaseId(unitBiddingId, projPurchaseId);
    }

    public ResultForm biddingProjPurchase(String unitBiddingId, String price) throws Exception {
        Date currentTime = new Date();
        int timeout = 10;// 10分钟无人选取，选取结束
        AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
        aeaImUnitBidding.setUnitBiddingId(unitBiddingId);
        // 判断报名项目是否有效
        List<AeaImUnitBidding> aeaImUnitBiddingList = aeaImUnitBiddingMapper.listUnitBidding(aeaImUnitBidding, "0");

        if (!(aeaImUnitBiddingList != null && aeaImUnitBiddingList.size() > 0)) {
            return new ResultForm(false, "项目不存在");
        }
        aeaImUnitBidding = aeaImUnitBiddingList.get(0);
        String biddingType = aeaImUnitBidding.getBiddingType();
//采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时
        String purchaseAuditFlag = aeaImUnitBidding.getPurchaseAuditFlag();
        String[] purchaseAuditFlags = new String[]{"7", "8", "11"};
        if (Arrays.binarySearch(purchaseAuditFlags, purchaseAuditFlag) < 0) {
            // return new ResultForm(false, "项目无效");
        }
        String highestPrice = aeaImUnitBidding.getHighestPrice();
        String basePrice = aeaImUnitBidding.getBasePrice();
        Date choiceImunitTime = aeaImUnitBidding.getChoiceImunitTime();
        String currentPrice = null; // 当前价格
        System.out.println(choiceImunitTime);
        System.out.println(currentTime);
        System.out.println(choiceImunitTime.after(currentTime));
        // 判断是否到选取时间
        if (choiceImunitTime != null && !choiceImunitTime.after(currentTime)) {
            return new ResultForm(false, "未到竞价时间");
        }

        // 获取竞价信息
        Map biddingMap = aeaImBiddingPriceMapper.getBiddingPriceByProjPurchaseId(unitBiddingId, aeaImUnitBidding.getProjPurchaseId());
        if (biddingMap != null) {
            currentPrice = (String) biddingMap.get("highestPrice");
            choiceImunitTime = (Date) biddingMap.get("lastCreateTime");
        }

        // 判断是否竞价结束
        Date timeoutDate = DateUtils.addMinute(choiceImunitTime, timeout);
        if (!timeoutDate.after(currentTime)) {
            if ("7".equals(aeaImUnitBidding.getPurchaseAuditFlag())) {
                // 竞价结束 修改采购需求项目状态 8：选取开始 12：已过时
                String flag = biddingMap != null ? "8" : "12";
                String[] auditFlags = {aeaImUnitBidding.getPurchaseAuditFlag(), flag};
                AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
                aeaImProjPurchase.setProjPurchaseId(aeaImUnitBidding.getProjPurchaseId());
                aeaImProjPurchase.setAuditFlags(auditFlags);
                aeaImProjPurchase.setModifier(SecurityContext.getCurrentUserName());
                aeaImProjPurchase.setModifyTime(new Date());
                updateProjPurchaseAuditFlag(aeaImProjPurchase, "采购需求项目竞价结束");
            }
            return new ResultForm(false, "竞价已结束");
        }

        // 判断出价价格
        Integer priceInt = Integer.parseInt(price);
        Integer basePriceInt = StringUtils.isNotBlank(basePrice) ? Integer.parseInt(basePrice) : null;
        Integer highestPriceInt = StringUtils.isNotBlank(highestPrice) ? Integer.parseInt(highestPrice) : null;
        Integer currentPriceInt = StringUtils.isNotBlank(currentPrice) ? Integer.parseInt(currentPrice) : null;

        // 随机选取只能出价一次
        if ("1".equals(biddingType)) {
            if (biddingMap != null && StringUtils.isNotBlank((String) biddingMap.get("unitPrice"))) {
                return new ResultForm(false, "只能出价一次");
            }
        }

        if (basePriceInt != null && priceInt < basePriceInt) {
            return new ResultForm(false, "不能低于最低价");
        }

        if (highestPriceInt != null && priceInt > highestPriceInt) {
            return new ResultForm(false, "不能高于最高价");
        }

        if ("3".equals(biddingType) && currentPriceInt != null && priceInt < currentPriceInt) {
            return new ResultForm(false, "不能低于当前价");
        }


        // 保存竞价信息
        AeaImBiddingPrice aeaImBiddingPrice = new AeaImBiddingPrice();
        aeaImBiddingPrice.setBiddingPriceId(UUID.randomUUID().toString());
        aeaImBiddingPrice.setIsChoice("0");
        aeaImBiddingPrice.setIsDelete("0");
        aeaImBiddingPrice.setUnitBiddingId(unitBiddingId);
        aeaImBiddingPrice.setCreater(SecurityContext.getCurrentUserName());
        aeaImBiddingPrice.setCreateTime(currentTime);
        aeaImBiddingPrice.setPrice(price);
        aeaImBiddingPrice.setRootOrgId(topOrgId);
        aeaImBiddingPriceMapper.insertAeaImBiddingPrice(aeaImBiddingPrice);

        return new ResultForm(true, "出价成功");
    }

    public List<AeaImProjPurchase> listProjPurchaseBidding(AeaImProjPurchase aeaImProjPurchase) {
        return aeaImProjPurchaseMapper.listProjPurchaseBidding(aeaImProjPurchase);
    }

    public void updateProjPurchaseAuditFlag(AeaImProjPurchase aeaImProjPurchase, String operateDescribe) throws Exception {

        aeaImProjPurchaseMapper.updateAeaImProjPurchaseAuditFlag(aeaImProjPurchase);

        AeaImPurchaseinst aeaImPurchaseinst = new AeaImPurchaseinst();
        aeaImPurchaseinst.setPurchaseinstId(UuidUtil.generateUuid());
        aeaImPurchaseinst.setProjPurchaseId(aeaImProjPurchase.getProjPurchaseId());
        aeaImPurchaseinst.setCreater(aeaImProjPurchase.getModifier());
        aeaImPurchaseinst.setCreateTime(aeaImProjPurchase.getModifyTime());
        aeaImPurchaseinst.setOldPurchaseFlag(aeaImProjPurchase.getAuditFlags()[0]);
        aeaImPurchaseinst.setNewPurchaseFlag(aeaImProjPurchase.getAuditFlags()[1]);
        if ("9".equals(aeaImProjPurchase.getAuditFlags()[1])) {
            aeaImPurchaseinst.setOperateAction("8");
        }
        aeaImPurchaseinst.setOperateDescribe(operateDescribe);
        aeaImPurchaseinst.setIsOwnFile("0");
        aeaImPurchaseinstMapper.insertPurchaseinst(aeaImPurchaseinst);
    }

    public AeaImBiddingPrice biddingPrice(String projPurchaseId, String type) throws Exception {
        AeaImBiddingPrice aeaImBiddingPrice = aeaImBiddingPriceMapper.getBiddingPrice(projPurchaseId, type);
        if (aeaImBiddingPrice != null) {
            Date biddingTime = new Date();
            AeaImUnitBidding aeaImUnitBidding = new AeaImUnitBidding();
            aeaImUnitBidding.setUnitBiddingId(aeaImBiddingPrice.getUnitBiddingId());
            aeaImUnitBidding.setIsWonBid("1");
            aeaImUnitBidding.setBiddingTime(biddingTime);
            aeaImUnitBidding.setModifier("admin");
            aeaImUnitBidding.setModifyTime(biddingTime);
            aeaImUnitBiddingMapper.updateAeaImUnitBidding(aeaImUnitBidding);

            aeaImBiddingPrice.setIsChoice("1");
            aeaImBiddingPriceMapper.updateAeaImBiddingPrice(aeaImBiddingPrice);
        }

        return aeaImBiddingPrice;
    }

    public List<AeaLinkmanInfo> listClientServiceLinkmanInfo(String serviceId, String unitInfoId) throws Exception {
        return aeaImClientServiceMapper.listClientServiceLinkmanInfo(serviceId, unitInfoId);
    }

}
