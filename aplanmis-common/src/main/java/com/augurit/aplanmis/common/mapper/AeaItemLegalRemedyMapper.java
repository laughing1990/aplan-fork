package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemLegalRemedy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 法律救济-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemLegalRemedyMapper {

    public void insertAeaItemLegalRemedy(AeaItemLegalRemedy aeaItemLegalRemedy) throws Exception;

    public void updateAeaItemLegalRemedy(AeaItemLegalRemedy aeaItemLegalRemedy) throws Exception;

    public void deleteAeaItemLegalRemedy(@Param("id") String id) throws Exception;

    public List<AeaItemLegalRemedy> listAeaItemLegalRemedy(AeaItemLegalRemedy aeaItemLegalRemedy) throws Exception;

    public AeaItemLegalRemedy getAeaItemLegalRemedyById(@Param("id") String id) throws Exception;

    public AeaItemLegalRemedy getAeaItemLegalRemedyByItemId(@Param("itemId") String itemId);
}
