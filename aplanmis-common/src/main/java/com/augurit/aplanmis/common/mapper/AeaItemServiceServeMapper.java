package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemServiceServe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Michael
 * @version: v1.0
 * @date: 10/31/2018
 **/
@Mapper
@Repository
public interface AeaItemServiceServeMapper {

    public void insertAeaItemServiceServe(AeaItemServiceServe aeaItemServiceServe);

    public void deleteAeaItemServiceServeById(@Param("serviceServeId") String serviceServeId);

    public void batchDeleteAeaItemServiceServe(@Param("serviceServeIds") String[] serviceServeIds);

    public void updateAeaItemServiceServeById(AeaItemServiceServe aeaItemServiceServe);

    public AeaItemServiceServe getAeaItemServiceServeById(@Param("serviceServeId") String serviceServeId);

    public List<AeaItemServiceServe> listAeaItemServiceServe(AeaItemServiceServe aeaItemServiceServe);

    public AeaItemServiceServe getAeaItemServiceServeByitemBasicId(@Param("itemBasicId") String itemBasicId);

}
