package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaImUnitServiceLinkman;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-06-11 15:44:50</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaImUnitServiceLinkmanMapper {

    public void insertAeaImUnitServiceLinkman(AeaImUnitServiceLinkman aeaImUnitServiceLinkman) throws Exception;

    public void updateAeaImUnitServiceLinkman(AeaImUnitServiceLinkman aeaImUnitServiceLinkman) throws Exception;

    public void deleteAeaImUnitServiceLinkman(@Param("id") String id) throws Exception;

    public List<AeaImUnitServiceLinkman> listAeaImUnitServiceLinkman(AeaImUnitServiceLinkman aeaImUnitServiceLinkman) throws Exception;

    public AeaImUnitServiceLinkman getAeaImUnitServiceLinkmanById(@Param("id") String id) throws Exception;

    void batchInsert(List<AeaImUnitServiceLinkman> unitServiceLinkmen);

    void deleteAeaImUnitServiceLinkmanByUnitServiceId(@Param("unitServiceId") String unitServiceId);

    public List<AeaLinkmanInfo> getAeaLinkmanInfoByUnitInfoId(@Param("unitInfoId") String unitInfoId, @Param("cardNo") String cardNo, @Param("auditFlag") String auditFlag) throws Exception;
}
