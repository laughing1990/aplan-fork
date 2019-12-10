package com.augurit.aplanmis.supermarket.job;

import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaImBiddingPrice;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.supermarket.bidProPurchase.service.BidProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Configurable
@EnableScheduling
public class ProjPurchaseJob {
    private static final Logger logger = LoggerFactory.getLogger(ProjPurchaseJob.class);

    @Autowired
    private BidProjectService bidProjectService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    @Scheduled(fixedDelay = 30000)
    public void projPurchaseJob() {
        String jobName = "采购需求项目状态变换";
        long startTime = System.currentTimeMillis();
        logger.info(jobName + " － 任务开始");

        // 查询采购需求项目列表 6：报名中，7：选取中，8：选取开始，11：竞价中
        String[] auditFlags = {"6", "7", "8", "11"};
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        aeaImProjPurchase.setAuditFlags(auditFlags);
        List<AeaImProjPurchase> aeaImProjPurchaseList = bidProjectService.listProjPurchaseBidding(aeaImProjPurchase);

        if (aeaImProjPurchaseList != null && aeaImProjPurchaseList.size() > 0) {
            aeaImProjPurchaseList.forEach(item -> {
                try {
//                    System.out.println(System.getProperty("user.timezone"));
                    String applyinstCode = item.getApplyinstCode();
                    // 报名中
                    if ("6".equals(item.getAuditFlag())) {
                        // 判断是否到报名截止日期，修改状态为 11：竞价中
                        Date expirationDate = item.getExpirationDate();
                        if (expirationDate != null && !expirationDate.after(new Date())) {
                            logger.info("采购需求项目【{}】报名截止", item.getProjPurchaseId());
                            updateAuditFlag(item.getProjPurchaseId(), "6", "11", "采购需求项目报名截止");
                        }
                    }

                    // 选取中
                    if ("7".equals(item.getAuditFlag())) {
                        // 判断是否竞价结束，修改状态为 8：选取开始 或 12：已过时
                        //int timeout = 10;// 10分钟无人选取，选取结束
//                        Date beginTime = item.getLastBiddingTime() != null ? item.getLastBiddingTime() : item.getChoiceImunitTime();
                        /*Date timeoutDate = DateUtils.addMinute(beginTime, timeout);
                        if (!timeoutDate.after(new Date())) {*/
                        logger.info("采购需求项目【{}】竞价结束", item.getProjPurchaseId());
                        String flag = item.getLastBiddingTime() != null ? "8" : "12";//无人出价，则项目12 无效，有竞价，则修改为8 选取开始
                        updateAuditFlag(item.getProjPurchaseId(), "7", flag, "采购需求项目竞价结束");
                        //}
                    }

                    // 选取开始
                    if ("8".equals(item.getAuditFlag())) {
                        String biddingType = item.getBiddingType();//biddingType=竞价类型：1 随机中标，2 直接抽取,3 竞价选取
                        //if ("1".equals(biddingType) || "3".equals(biddingType)) {
                        if ("1".equals(biddingType)) {//随机中标
                            String type = "1".equals(biddingType) ? "1" : "2";
                            AeaImBiddingPrice aeaImBiddingPrice = bidProjectService.biddingPrice(item.getProjPurchaseId(), type);
                            if (aeaImBiddingPrice != null) {
                                // 选取结束，修改状态为 9：已选取
                                updateAuditFlag(item.getProjPurchaseId(), "8", "9", "采购需求项目选取完成");
                                updateApplyStatus(applyinstCode, "待确认中介机构", "8");
                                logger.info("采购需求项目【{}】选取结束；中选【unitBiddingId：{}，biddingPriceId：{}】", item.getProjPurchaseId(), aeaImBiddingPrice.getUnitBiddingId(), aeaImBiddingPrice.getBiddingPriceId());
                            } else {
                                //没人报名，项目状态设置为无效
                            }
                        } else if ("3".equals(biddingType)) {//竞价选取

                        }
                    }

                    // 竞价中
                    if ("11".equals(item.getAuditFlag())) {
                        // 判断是否竞价开始，修改状态为 7：选取中
                        Date beginTime = item.getChoiceImunitTime();//选取中介时间
                        if (beginTime != null && !beginTime.after(new Date())) {
                            logger.info("采购需求项目【{}】竞价开始", item.getProjPurchaseId());
                            updateAuditFlag(item.getProjPurchaseId(), "11", "7", "采购需求项目竞价开始");
                            updateApplyStatus(applyinstCode, "待选取中介机构", "7");
                            //修改申报状态为 带选取
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("采购需求项目【{}】异常：{}", item.getProjPurchaseId(), e.getMessage());
                }
            });
        }

        logger.info(jobName + " － 任务结束 {}ms", System.currentTimeMillis() - startTime);
    }

    private void updateAuditFlag(String projPurchaseId, String beforeFlag, String afterFlag, String operateDescribe) throws Exception {
        String[] auditFlags = {beforeFlag, afterFlag};
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        aeaImProjPurchase.setProjPurchaseId(projPurchaseId);
        aeaImProjPurchase.setAuditFlags(auditFlags);
        aeaImProjPurchase.setModifier("admin");
        aeaImProjPurchase.setModifyTime(new Date());
        bidProjectService.updateProjPurchaseAuditFlag(aeaImProjPurchase, operateDescribe);
    }

    private void updateApplyStatus(String applyinstCode, String opsAction, String applyStatus) throws Exception {
        //更新申请状态及维护状态历史
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstByCode(applyinstCode);
        if (null == applyinst) return;
        aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertOpsAeaLogItemStateHist(applyinst.getApplyinstId(), "系统自动操作", opsAction, null, applyStatus, null);

    }
}
