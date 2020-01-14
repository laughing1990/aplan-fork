package com.augurit.aplanmis.front.third.logistics;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.aplanmis.front.third.logistics.vo.LogisticsOrderDetailVo;
import com.augurit.aplanmis.front.third.logistics.vo.LogisticsOrderResultVo;
import com.augurit.aplanmis.front.third.logistics.vo.LogisticsOrderVo;

import java.util.ArrayList;
import java.util.List;

public class DefaultLogisticsServiceImpl implements LogisticsService {

    @Override
    public LogisticsOrderResultVo order(LogisticsOrderVo logisticsOrderVo) throws Exception {
        LogisticsOrderResultVo logisticsOrderResultVo = new LogisticsOrderResultVo();
        logisticsOrderResultVo.setExpressNum(UuidUtil.generateUuid());
        logisticsOrderResultVo.setOrderId("这个是假的数据，需要对接第三方物流下单接口");
        return logisticsOrderResultVo;
    }

    @Override
    public List<LogisticsOrderDetailVo> detail(String expressNum) throws Exception {
        List<LogisticsOrderDetailVo> logisticsOrderDetailVos = new ArrayList<>();
        LogisticsOrderDetailVo logisticsOrderDetailVo = new LogisticsOrderDetailVo("2020-01-11 12:14:15", "邮件已下单", null);
        LogisticsOrderDetailVo logisticsOrderDetailVo2 = new LogisticsOrderDetailVo("2020-01-11 16:14:15", "中国邮政", "已取件");
        LogisticsOrderDetailVo logisticsOrderDetailVo3 = new LogisticsOrderDetailVo("2020-01-12 08:14:15", "快件到达【广州天河棠东营业部】", null);
        LogisticsOrderDetailVo logisticsOrderDetailVo4 = new LogisticsOrderDetailVo("2020-01-12 16:14:15", "快件到达【广州天河棠东营业部】", null);
        LogisticsOrderDetailVo logisticsOrderDetailVo5 = new LogisticsOrderDetailVo("2020-01-13 08:14:15", "快件到达【广州天河棠东营业部】", null);
        LogisticsOrderDetailVo logisticsOrderDetailVo6 = new LogisticsOrderDetailVo("2020-01-13 15:14:15", "快件交给快递员xxx，正在派送中，联系电话1000000000", null);
        logisticsOrderDetailVos.add(logisticsOrderDetailVo);
        logisticsOrderDetailVos.add(logisticsOrderDetailVo2);
        logisticsOrderDetailVos.add(logisticsOrderDetailVo3);
        logisticsOrderDetailVos.add(logisticsOrderDetailVo4);
        logisticsOrderDetailVos.add(logisticsOrderDetailVo5);
        logisticsOrderDetailVos.add(logisticsOrderDetailVo6);
        return logisticsOrderDetailVos;
    }
}
