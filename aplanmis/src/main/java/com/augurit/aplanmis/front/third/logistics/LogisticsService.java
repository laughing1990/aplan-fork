package com.augurit.aplanmis.front.third.logistics;

import com.augurit.aplanmis.front.third.logistics.vo.LogisticsOrderDetailVo;
import com.augurit.aplanmis.front.third.logistics.vo.LogisticsOrderResultVo;
import com.augurit.aplanmis.front.third.logistics.vo.LogisticsOrderVo;

import java.util.List;

/**
 * 物流下单
 */
public interface LogisticsService {

    /**
     * 物流下单
     */
    LogisticsOrderResultVo order(LogisticsOrderVo logisticsOrderVo) throws Exception;

    /**
     * 跟踪物流信息
     *
     * @param expressNum 快递单号
     * @return 物流跟踪信息
     */
    List<LogisticsOrderDetailVo> detail(String expressNum) throws Exception;
}
