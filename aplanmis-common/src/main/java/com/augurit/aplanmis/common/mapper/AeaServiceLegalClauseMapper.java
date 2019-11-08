package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemServiceBasic;
import com.augurit.aplanmis.common.domain.AeaServiceLegalClause;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 法律法规条款-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:13:52</li>
 * </ul>
 */
@Mapper
public interface AeaServiceLegalClauseMapper {

    void insertAeaServiceLegalClause(AeaServiceLegalClause aeaServiceLegalClause) ;

    void updateAeaServiceLegalClause(AeaServiceLegalClause aeaServiceLegalClause) ;

    void deleteAeaServiceLegalClause(@Param("id") String id) ;

    List<AeaServiceLegalClause> listAeaServiceLegalClause(AeaServiceLegalClause aeaServiceLegalClause) ;

    List<AeaServiceLegalClause> listAeaServiceLegalClauseForRecordId(AeaServiceLegalClause aeaServiceLegalClause) ;

    AeaServiceLegalClause getAeaServiceLegalClauseById(@Param("id") String id) ;

    void deleteAeaServiceLegalClauseBylegalId(@Param("legalId") String legalId) ;

    List<AeaServiceLegalClause> listAeaServiceLegalClauseByChargeGroupId(AeaItemServiceBasic aeaItemServiceBasic);

    AeaServiceLegalClause getLegalAndClauseByClauseId(@Param("legalClauseId")String legalClauseId) ;

    Long getMaxSortNo(String rootOrgId);
}
