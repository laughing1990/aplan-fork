package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemGuideDepartments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AeaItemGuideDepartmentsMapper {

    void insertAeaItemGuideDepartments(AeaItemGuideDepartments aeaItemGuideDepartments);

    void updateAeaItemGuideDepartments(AeaItemGuideDepartments aeaItemGuideDepartments);

    void deleteAeaItemGuideDepartments(@Param("id") String id);

    void batchDeleteGuideDepartmentsByItemVerId(@Param("itemVerId") String itemVerId,
                                                @Param("rootOrgId")String rootOrgId);

    List<AeaItemGuideDepartments> listAeaItemGuideDepartments(AeaItemGuideDepartments aeaItemGuideDepartments);

    AeaItemGuideDepartments getAeaItemGuideDepartmentsById(@Param("id") String id);
}