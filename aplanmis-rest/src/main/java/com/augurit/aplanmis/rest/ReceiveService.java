package com.augurit.aplanmis.rest;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiReceive;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiReceiveMapper;
import com.augurit.aplanmis.common.mapper.AeaHiSmsInfoMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.rest.userCenter.vo.ReceiveVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author xiaohutu--20190715
 * 回执接口
 */
@Service
public class ReceiveService {
    private static Logger logger = LoggerFactory.getLogger(ReceiveService.class);
    /**
     * 回执类型（1：物料回执 2：受理回执 3：不收件回执 4：退件回执 5：领证回执）
     */
    private static final String[] RECEIVE_TYPES = {"1", "2", "3", "4", "5"};
    @Autowired
    private AeaHiIteminstMapper aeaHiIteminstMapper;
    @Autowired
    private AeaHiReceiveMapper aeaHiReceiveMapper;
    @Autowired
    private AeaHiSmsInfoMapper aeaHiSmsInfoMapper;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    /**
     * 保存回执实例
     *
     * @param applyinstIds 申请实例ID
     * @param receiptTypes 需要保存的回执类型
     * @param currentUser  操作人
     * @param comments     意见，非必须
     * @return 插入的数量
     * @throws Exception e
     */
    public ReceiveVo saveReceive(String[] applyinstIds, String[] receiptTypes, String currentUser, String comments) throws Exception {
        ReceiveVo vo = new ReceiveVo();
        if (null == applyinstIds || applyinstIds.length == 0) {
            logger.info("[applyinstIds]，参数不能为空");
            return vo;
        }
        if (null == receiptTypes || receiptTypes.length == 0) {
            logger.info("[receiptTypes]，参数不能为空");
            return vo;
        }
        for (String applysinstId : applyinstIds) {
            //先根据申请实例ID查询事项实例
            List<AeaHiIteminst> aeaHiIteminstList = aeaHiIteminstMapper.getAeaHiIteminstListByApplyinstId(applysinstId);
            //在查询联系人信息
            AeaHiSmsInfo aeaHiSmsInfo = aeaHiSmsInfoMapper.getAeaHiSmsInfoByApplyinstId(applysinstId);
            if (null == aeaHiSmsInfo) {
                return vo;
            }
            if (aeaHiIteminstList.size() == 0) {
                logger.info("[{}]参数异常，无法查询到申请实例", applysinstId);
                continue;
            }
            String itemVerIds = aeaHiIteminstList.stream().map(AeaHiIteminst::getItemVerId).collect(Collectors.joining(","));
            for (String receiptType : receiptTypes) {

                AeaHiReceive aeaHiReceive = new AeaHiReceive();
                aeaHiReceive.setReceiveId(UUID.randomUUID().toString());
                aeaHiReceive.setApplyinstId(applysinstId);
                aeaHiReceive.setReceiptType(receiptType);
                aeaHiReceive.setOutinstId(itemVerIds);
                aeaHiReceive.setReceiveMemo(comments);
                aeaHiReceive.setReceiveUserName(aeaHiSmsInfo.getAddresseeName());
                aeaHiReceive.setReceiveCertNo(aeaHiSmsInfo.getAddresseeIdcard());
                aeaHiReceive.setReceiveUserMobile(aeaHiSmsInfo.getAddresseePhone());
                aeaHiReceive.setServiceAddress(aeaHiSmsInfo.getAddresseeAddr());
                aeaHiReceive.setCreater(currentUser);
                aeaHiReceive.setCreateTime(new Date());
                aeaHiReceive.setReceiveTime(new Date());
                aeaHiReceive.setRootOrgId(SecurityContext.getCurrentOrgId());
                aeaHiReceiveMapper.insertAeaHiReceive(aeaHiReceive);
            }
            //1并联 返回阶段名+
        }
        return vo;
    }
}
