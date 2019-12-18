package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemServiceBasic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 设立依据-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaItemServiceBasicMapper {

    void insertAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) ;

     void updateAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) ;

     void deleteAeaItemServiceBasic(@Param("id") String id) ;

     List<AeaItemServiceBasic> listAeaItemServiceBasicNoRel(AeaItemServiceBasic aeaItemServiceBasic) ;

     List<AeaItemServiceBasic> listAeaItemServiceBasic(AeaItemServiceBasic aeaItemServiceBasic) ;

     AeaItemServiceBasic getAeaItemServiceBasicById(@Param("id") String id) ;

     List<AeaItemServiceBasic> listAeaItemServiceBasicAndClauseContent(AeaItemServiceBasic aeaItemServiceBasic) ;

    List<AeaItemServiceBasic> listAeaItemServiceBasicWithLegal(AeaItemServiceBasic aeaItemServiceBasic);

    void deleteAeaItemServiceBasicByRecordId(@Param("recordId") String recordId,
                                             @Param("rootOrgId")String rootOrgId);

    void deleteAeaItemServiceBasicByCondition(@Param("aeaItemServiceBasic") AeaItemServiceBasic aeaItemServiceBasic,
                                              @Param("legalClauseIds") String[] legalClauseIds) ;
}

