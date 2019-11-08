package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.StageTotalForm;
import com.augurit.aplanmis.common.domain.TaskCountForm;
import com.augurit.aplanmis.common.domain.TotalItemForm;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/8/13
 */
@Repository
@Mapper
public interface PortMapper {
    List<TotalItemForm> findTotalItemBaseOnTheme();

    List<TotalItemForm> findTotalItemBaseOnStandardTheme();

    List<TaskCountForm> countItemAndApply();

    List<StageTotalForm> findStageUseDay();

    List<StageTotalForm> findStandardStageUseDay();
}
