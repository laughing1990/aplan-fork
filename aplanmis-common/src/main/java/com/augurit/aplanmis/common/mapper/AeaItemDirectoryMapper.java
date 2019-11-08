package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemDirectory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通用事(子)项表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:12:38</li>
 * </ul>
 */
@Mapper
public interface AeaItemDirectoryMapper {

    public void insertAeaItemDirectory(AeaItemDirectory aeaItemDirectory) throws Exception;

    public void updateAeaItemDirectory(AeaItemDirectory aeaItemDirectory) throws Exception;

    public void deleteAeaItemDirectory(@Param("id") String id) throws Exception;

    public List<AeaItemDirectory> listAeaItemDirectory(AeaItemDirectory aeaItemDirectory) throws Exception;

    public AeaItemDirectory getAeaItemDirectoryById(@Param("id") String id) throws Exception;
}
