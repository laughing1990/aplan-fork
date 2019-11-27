package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.aplanmis.common.check.exception.StageItemCheckException;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 阶段事项前置检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 20)
public class StageItemChecker extends AbstractChecker<AeaParStage> {

    @Autowired
    private AeaParFrontItemService aeaParFrontItemService;

    @Autowired
    private AeaItemBasicAdminService aeaItemBasicAdminService;

    @Override
    public String doCheck(AeaParStage stage, CheckerContext checkerContext) throws StageItemCheckException {

        if (Status.ON.equals(stage.getIsCheckItem())) {

            Assert.notNull(stage, "aeaParStage must not null.");
            Assert.notNull(checkerContext, "checkerContext must not null.");

            String projInfoId = checkerContext.getProjInfoId();
            Assert.hasText(projInfoId, "projInfoId must not null.");

            //获取阶段下的前置事项
            List<AeaParFrontItem> parFrontItemList = aeaParFrontItemService.listAeaParFrontItemByStageId(stage.getStageId());

            if (parFrontItemList.size() > 0) {

                //获取项目所有已办结通过或者办结容缺通过的事项
                List<AeaItemBasic> itemBasics = aeaItemBasicAdminService.getCompletedItemBasicByProjInfoId(projInfoId);

                StringBuffer message = new StringBuffer();

                if (itemBasics.size() < 1) {
                    parFrontItemList.forEach(aeaParFrontItemVo -> message.append(aeaParFrontItemVo.getItemName() + "事项").append("、"));
                    String error = "【" + message.substring(0, message.length() - 1) + "】";
                    return error + "尚未审批通过，无法申报【" + stage.getStageName() + "】。";
                }

                List<String> itemSeq = new ArrayList<>();
                String ids = itemBasics.stream().map(aeaItemBasic -> {
                    itemSeq.add(aeaItemBasic.getItemSeq());
                    return aeaItemBasic.getItemVerId();
                }).collect(Collectors.joining(","));

                //实施事项的事项ID序列
                String itemSeqStr = String.join(",", itemSeq);

                parFrontItemList.forEach(aeaParFrontItemVo -> {
                    if ("1".equals(aeaParFrontItemVo.getIsCatalog())) {
                        if (!itemSeqStr.contains(aeaParFrontItemVo.getItemId())) {
                            message.append(aeaParFrontItemVo.getItemName() + "事项").append("、");
                        }
                    } else {
                        if (!ids.contains(aeaParFrontItemVo.getItemVerId())) {
                            message.append(aeaParFrontItemVo.getItemName() + "事项").append("、");
                        }
                    }
                });

                if (message.length() > 0) {
                    String error = "【" + message.substring(0, message.length() - 1) + "】";
                    return error + "尚未审批通过，无法申报【" + stage.getStageName() + "】。";
                }
            }
        }
        return null;
    }
}
