package com.augurit.aplanmis.front.third.multidiscipline.mapper;

import com.augurit.aplanmis.front.third.multidiscipline.vo.ThirdApproveInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ThirdApproveInfoMapper {

    @Select("select t1.STAGEINST_STATE stateValue, t2.stage_name stageName, t2.SORT_NO sortNo, t1.APPLYINST_ID applyinstId, t3.create_time  applyinstCreateTime from aea_hi_par_stageinst t1 " +
            "join aea_par_stage t2 on t1.stage_id=t2.stage_id " +
            "join aea_hi_applyinst t3 on t3.APPLYINST_ID=t1.APPLYINST_ID " +
            "join aea_applyinst_proj t4 on t4.APPLYINST_ID=t3.APPLYINST_ID " +
            "join aea_proj_info t5 on t5.PROJ_INFO_ID=t4.PROJ_INFO_ID " +
            "join ( " +
            "select tt2.stage_name, max(tt3.create_time) create_time  from aea_hi_par_stageinst tt1 " +
            "join aea_par_stage tt2 on tt1.stage_id=tt2.stage_id " +
            "join aea_hi_applyinst tt3 on tt3.APPLYINST_ID=tt1.APPLYINST_ID " +
            "join aea_applyinst_proj tt4 on tt4.APPLYINST_ID=tt3.APPLYINST_ID " +
            "join aea_proj_info tt5 on tt5.PROJ_INFO_ID=tt4.PROJ_INFO_ID " +
            "where tt5.LOCAL_CODE=#{projCode} " +
            "group by tt2.stage_name " +
            ") t6 on t2.stage_name=t6.stage_name and t3.CREATE_TIME=t6.create_time " +
            "where t5.LOCAL_CODE=#{projCode} " +
            "order by t2.sort_no")
    List<ThirdApproveInfo.ThirdApproveStage> listApproveStages(@Param("projCode") String projCode);
}
