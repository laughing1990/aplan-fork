package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaCreditSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 信用管理-红黑名单管理-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaCreditSummaryMapper {

     void insertAeaCreditSummary(AeaCreditSummary aeaCreditSummary) ;

     void updateAeaCreditSummary(AeaCreditSummary aeaCreditSummary) ;

     void deleteAeaCreditSummary(@Param("id") String id) ;

     List <AeaCreditSummary> listAeaCreditSummary(AeaCreditSummary aeaCreditSummary) ;

     AeaCreditSummary getAeaCreditSummaryById(@Param("id") String id) ;

     List<AeaCreditSummary> listAeaCreditSummaryByUnifiedSocialCreditCode(@Param("unifiedSocialCreditCode") String unifiedSocialCreditCode);
}
