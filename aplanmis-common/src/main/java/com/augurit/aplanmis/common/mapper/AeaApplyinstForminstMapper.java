package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaApplyinstForminst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaApplyinstForminstMapper {

    void insertAeaApplyinstForminst(AeaApplyinstForminst AeaApplyinstForminst) throws Exception;

    void updateAeaApplyinstForminst(AeaApplyinstForminst AeaApplyinstForminst) throws Exception;

    void deleteAeaApplyinstForminstByApplyinstId(@Param("applyinstId") String applyinstId) throws Exception;

    void deleteAeaApplyinstForminstByForminstIds(@Param("forminstIds") List<String> forminstIds) throws Exception;

    AeaApplyinstForminst getAeaApplyinstForminstByForminstId(@Param("forminstId") String forminstId) throws Exception;

    /**
     * 根据申请实例获取表单实例列表
     */
    List<AeaApplyinstForminst> listAeaApplyinstForminstByApplyinstId(@Param("applyinstId") String applyinstId) throws Exception;
}
