package com.augurit.aplanmis.supermarket.job;

import com.augurit.agcloud.framework.util.DateUtils;
import com.augurit.aplanmis.common.domain.AeaImBiddingPrice;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
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

    @Scheduled(fixedDelay = 30000)
    public void projPurchaseJob() {
        String jobName = "采购需求项目状态变换";
        long startTime = System.currentTimeMillis();
        logger.info(jobName + " － 任务开始");

        // 查询采购需求项目列表 6：报名中，7：选取中，8：选取开始，11：待选取
        String[] auditFlags = {"6", "7", "8", "11"};
        AeaImProjPurchase aeaImProjPurchase = new AeaImProjPurchase();
        aeaImProjPurchase.setAuditFlags(auditFlags);
        List<AeaImProjPurchase> aeaImProjPurchaseList = bidProjectService.listProjPurchaseBidding(aeaImProjPurchase);

        if (aeaImProjPurchaseList != null && aeaImProjPurchaseList.size() > 0) {
            aeaImProjPurchaseList.forEach(item -> {
                try {
                    // 报名中
                    if ("6".equals(item.getAuditFlag())) {
                        // 判断是否到报名截止日期，修改状态为 11：待选取
                        Date expirationDate = item.getExpirationDate();
                        if (expirationDate != null && !expirationDate.after(new Date())) {
                            logger.info("采购需求项目【{}】报名截止", item.getProjPurchaseId());
                            updateAuditFlag(item.getProjPurchaseId(), "6", "11", "采购需求项目报名截止");
                        }
                    }

                    // 选取中
                    if ("7".equals(item.getAuditFlag())) {
                        // 判断是否竞价结束，修改状态为 8：选取开始 或 12：已过时
                        int timeout = 10;// 10分钟无人选取，选取结束
                        Date beginTime = item.getLastBiddingTime() != null ? item.getLastBiddingTime() : item.getChoiceImunitTime();
                        Date timeoutDate = DateUtils.addMinute(beginTime, timeout);
                        if (!timeoutDate.after(new Date())) {
                            logger.info("采购需求项目【{}】竞价结束", item.getProjPurchaseId());
                            String flag = item.getLastBiddingTime() != null ? "8" : "12";
                            updateAuditFlag(item.getProjPurchaseId(), "7", flag, "采购需求项目竞价结束");
                        }
                    }

                    // 选取开始
                    if ("8".equals(item.getAuditFlag())) {
                        String biddingType = item.getBiddingType();
                        if ("1".equals(biddingType) || "3".equals(biddingType)) {
                            String type = "1".equals(biddingType) ? "1" : "2";
                            AeaImBiddingPrice aeaImBiddingPrice = bidProjectService.biddingPrice(item.getProjPurchaseId(), type);
                            if (aeaImBiddingPrice != null) {
                                // 选取结束，修改状态为 9：已选取
                                updateAuditFlag(item.getProjPurchaseId(), "8", "9", "采购需求项目选取完成");
                                logger.info("采购需求项目【{}】选取结束；中选【unitBiddingId：{}，biddingPriceId：{}】", item.getProjPurchaseId(), aeaImBiddingPrice.getUnitBiddingId(), aeaImBiddingPrice.getBiddingPriceId());
                            }
                        }
                    }

                    // 待选取
                    if ("11".equals(item.getAuditFlag())) {
                        // 判断是否竞价开始，修改状态为 7：选取中
                        Date beginTime = item.getChoiceImunitTime();
                        if (beginTime != null && !beginTime.after(new Date())) {
                            logger.info("采购需求项目【{}】竞价开始", item.getProjPurchaseId());
                            updateAuditFlag(item.getProjPurchaseId(), "11", "8", "采购需求项目竞价开始");
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
}
