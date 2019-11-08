package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AgentUnitService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 中介机构与服务-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AgentUnitServiceMapper {
    /**
     * 入住单位列表
     *
     * @param agentUnitService 查询参数
     * @return AgentUnitService
     */
    List<AgentUnitService> listCheckinUnit(AgentUnitService agentUnitService);

    /**
     * 入住服务列表
     *
     * @param agentUnitService 查询参数
     * @return
     */
    List<AgentUnitService> listCheckinService(AgentUnitService agentUnitService);
}
