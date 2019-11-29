package com.augurit.aplanmis.common.check;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.check.exception.ItemItemCheckException;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemFrontItem;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemBasicAdminService;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontItemAdminService;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 事项的前置事项检查
 */
@Component
@Order(AbstractChecker.HIGHEST_PRECEDENCE + 10)
@Slf4j
public class ItemItemChecker extends AbstractChecker<AeaItemBasic> {

    @Autowired
    private AeaItemFrontItemAdminService aeaItemFrontAdminService;

    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaItemBasicAdminService aeaItemBasicAdminService;

    @Override
    public void doCheck(AeaItemBasic aeaItemBasic, CheckerContext checkerContext) throws ItemItemCheckException {
        if (Status.ON.equals(aeaItemBasic.getIsCheckItem())) {
            log.info("事项: " + aeaItemBasic.getItemName() + " 需要对前置事项进行检查");

            Assert.notNull(aeaItemBasic, "aeaItemBasic must not null.");
            Assert.notNull(checkerContext, "checkerContext must not null.");

            String projInfoId = checkerContext.getProjInfoId();
            Assert.hasText(projInfoId, "projInfoId must not null.");

            List<AeaItemFrontItem> aeaItemFronts = aeaItemFrontAdminService.listItemsByItemVerId(aeaItemBasic.getItemVerId());
            if (aeaItemFronts != null && aeaItemFronts.size() > 0) {
                String currentOrgId = SecurityContext.getCurrentOrgId();
                // 存放是否通过信息
                Map<String, Boolean> passedMap = new HashMap<>(aeaItemFronts.size());
                List<String> frontItemVerIds = aeaItemFronts.stream().map(frontItem -> {
                    passedMap.put(frontItem.getFrontCkItemVerId(), false);
                    return frontItem.getFrontCkItemVerId();
                }).collect(Collectors.toList());
                // 如果前置事项配置里有标准事项，则标准事项下的任一实施事项通过就算通过
                Map<String, AeaItemBasic> frontItems = aeaItemBasicMapper.getAeaItemBasicListByItemVerIds(frontItemVerIds).stream().collect(Collectors.toMap(AeaItemBasic::getItemVerId, item -> item));
                Collection<AeaItemBasic> values = frontItems.values();
                if (!values.isEmpty()) {
                    // 实施事项与标准事项对应关系， 如果本身就是实施事项， 则对应自己
                    Map<String, String> carryOutItemAndCatalogItemMap = new HashMap<>();
                    List<String> carryOutItemVerIds = new ArrayList<>();
                    values.forEach(item -> {
                        //  如果是标准事项
                        if (Status.ON.equals(item.getIsCatalog())) {
                            List<AeaItemBasic> carryOutItems = aeaItemBasicAdminService.findCarryOutItemsByItemId(item.getItemId(), currentOrgId);
                            carryOutItems.forEach(coi -> {
                                carryOutItemVerIds.add(coi.getItemVerId());
                                carryOutItemAndCatalogItemMap.put(coi.getItemVerId(), item.getItemVerId());
                            });
                        } else {
                            carryOutItemVerIds.add(item.getItemVerId());
                            carryOutItemAndCatalogItemMap.put(item.getItemVerId(), item.getItemVerId());
                        }
                    });
                    List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstMapper.listAeaHiIteminstByProjInfoIdAndItemVerIds(projInfoId, carryOutItemVerIds, currentOrgId);
                    aeaHiIteminstList.forEach(iteminst -> {
                        // 判断实施事项是否办理通过
                        if (ItemStatus.isAgreed(iteminst.getIteminstState())) {
                            passedMap.put(carryOutItemAndCatalogItemMap.get(iteminst.getItemVerId()), true);
                        }
                    });
                }
                StringBuilder errroMessage = new StringBuilder();
                List<AeaItemBasic> resultItems = new ArrayList<>();
                for (Map.Entry<String, Boolean> entry : passedMap.entrySet()) {
                    // 审批不通过
                    if (!entry.getValue()) {
                        AeaItemBasic frontItem = frontItems.get(entry.getKey());
                        resultItems.add(frontItem);
                        errroMessage.append(frontItem != null ? frontItem.getItemName() + "事项" : "").append(",");
                    }
                }
                if (errroMessage.length() > 0) {
                    String error = "【" + errroMessage.substring(0, errroMessage.length() - 1) + "】" + "尚未审批通过，无法申报【" + aeaItemBasic.getItemName() + "事项】。";
                    throw new ItemItemCheckException(error, resultItems);
                }

                log.info("事项: " + aeaItemBasic.getItemName() + " 前置事项检查通过.");
            }
        }
    }
}
