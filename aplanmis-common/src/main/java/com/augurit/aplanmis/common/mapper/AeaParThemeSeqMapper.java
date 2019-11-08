package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParThemeSeq;
import com.augurit.aplanmis.common.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 主题定义序列表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParThemeSeqMapper extends BaseMapper<AeaParThemeSeq> {

    /**
     * 查询
     *
     * @param aeaParThemeSeq
     * @return
     */
    List<AeaParThemeSeq> listAeaParThemeSeq(AeaParThemeSeq aeaParThemeSeq);

    /**
     * 通过主题id获取系列
     *
     * @param themeId
     * @param rootOrgId
     * @return
     */
    AeaParThemeSeq getAeaParThemeSeqByThemeId(@Param("themeId") String themeId,
                                              @Param("rootOrgId") String rootOrgId);
}
