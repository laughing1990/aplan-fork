package com.augurit.aplanmis.common.service.guide;

import com.augurit.aplanmis.common.domain.AeaItemServiceBasic;

import java.util.List;
import java.util.Map;

public interface AeaItemServiceBasicService {
    /**
     *  根据阶段ID查询设立依据列表
     * @param stageId 必须参数 阶段ID
     * @return
     * @throws Exception
     */
    List<AeaItemServiceBasic> getAeaItemServiceBasicListByStageId(String stageId,String rootOrgId) throws Exception;

    /**
     *  根据阶段ID查询阶段下所有事项的设立依据列表
     * @param stageId 必须参数 阶段ID
     * @param rootOrgId 必须参数 顶级机构ID
     * @return
     * @throws Exception
     */
    List<Map> getSubAeaItemServiceBasicListByStageId(String stageId,String rootOrgId) throws Exception;

    /**
     * 根据事项版本ID查找设立依据列表
     * @param itemVerId 必须参数 事项版本ID
     * @return
     * @throws Exception
     */
    public List<AeaItemServiceBasic> listAeaItemServiceBasicByitemVerId(String itemVerId,String rootOrgId) throws Exception;
}
