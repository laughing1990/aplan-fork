package com.augurit.aplanmis.common.mapper;
import com.augurit.aplanmis.common.domain.AeaLogThemeItemChange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
    /**
    * 主题及事项申报变更记录表-Mapper数据与持久化接口类
    */

@Mapper
@Repository
public interface AeaLogThemeItemChangeMapper {

    public void insertAeaLogThemeItemChange(AeaLogThemeItemChange aeaLogThemeItemChange) throws Exception;

    public void updateAeaLogThemeItemChange(AeaLogThemeItemChange aeaLogThemeItemChange) throws Exception;

    public void deleteAeaLogThemeItemChange(@Param("id") String id) throws Exception;

    public List <AeaLogThemeItemChange> listAeaLogThemeItemChange(AeaLogThemeItemChange aeaLogThemeItemChange) throws Exception;

    public AeaLogThemeItemChange getAeaLogThemeItemChangeById(@Param("id") String id) throws Exception;
}
