package com.augurit.aplanmis.common.service.instance;

import com.augurit.aplanmis.common.domain.AeaHiItemInoutinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;

import java.util.List;

/**
 * @author xiaohutu
 */
public interface AeaHiItemInoutinstService {
    /**
     * 材料输入输出实例
     *
     * @param matinstsIds 材料实例ID
     * @param applyinstId 申报实例id
     * @param creater     创建人
     * @return 插入的条数 0 代表失败
     */
    int batchInsertAeaHiItemInoutinst(String[] matinstsIds, String applyinstId, String creater) throws Exception;

    List<AeaHiItemInoutinst> getAeaHiItemInoutinstByIteminstIds(String[] oldIteminstIds);

    void batchDeleteAeaHiItemInoutinst(String[] outinstIds);
}
