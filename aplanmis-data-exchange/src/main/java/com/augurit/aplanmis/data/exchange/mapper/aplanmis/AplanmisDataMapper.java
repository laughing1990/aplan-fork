package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.data.exchange.domain.aplanmis.ItemStateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/11/8
 */
@Mapper
@Repository
public interface AplanmisDataMapper {
    /**
     * 查询申报项目数
     *
     * @return
     */
    Long getAplanmisProjCount();

    /**
     * 查询申报项目编码列表
     *
     * @return
     */
    List<String> findAplanmisProjLocalCodeList();


    /**
     * 查询未受理项目数
     *
     * @return
     */
    Long getNonAcceptAplanmisProjCount();

    /**
     * 查询申报未受理项目编码列表
     *
     * @return
     */
    List<String> findNonAcceptAplanmisProjLocalCodeList();

    /**
     * 查询已上传上传事项办理详细的项目数
     *
     * @return
     */
    Long getAplanmisProjCountHasItemStateLog();

    /**
     * 查询已上传上传事项办理详细的项目列表
     *
     * @return
     */
    List<String> findAplanmisProjLocalCodeListHasItemStateLog();

    /**
     * 查询只申报了服务协同的项目数
     *
     * @return
     */
    Long getOnlyA2ProjCount();

    List<String> getOnlyA2ProjLocalCodeList();

    /**
     * 查询项目的事项办理详细信息
     *
     * @param localCodes 项目代码
     * @return 事项实例状态变更日志列表
     */
    ItemStateLog findItemStateLogByLocalCodes(@Param("localCodes") String localCodes);

}
