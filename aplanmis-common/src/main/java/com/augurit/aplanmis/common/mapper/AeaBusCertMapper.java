package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaBusCert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:12:19</li>
 * </ul>
 */
@Mapper
public interface AeaBusCertMapper {

    public void insertAeaBusCert(AeaBusCert aeaBusCert) throws Exception;

    public void updateAeaBusCert(AeaBusCert aeaBusCert) throws Exception;

    public void deleteAeaBusCert(@Param("id") String id) throws Exception;

    public List<AeaBusCert> listAeaBusCert(AeaBusCert aeaBusCert) throws Exception;

    public AeaBusCert getAeaBusCertById(@Param("id") String id) throws Exception;

    //取消业务表的关联关系（逻辑删除）
    void cancelRelation(@Param("tableName") String tableName, @Param("pkName") String pkName, @Param("busRecordId") String busRecordId,
                        @Param("cancelCertIds") String[] cancelCertIds) throws Exception;
}
