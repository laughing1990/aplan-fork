package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaLocalDirectory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 本级事项目录-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:14:27</li>
 * </ul>
 */
@Mapper
public interface AeaLocalDirectoryMapper {

    public void insertAeaLocalDirectory(AeaLocalDirectory aeaLocalDirectory) throws Exception;

    public void updateAeaLocalDirectory(AeaLocalDirectory aeaLocalDirectory) throws Exception;

    public void deleteAeaLocalDirectory(@Param("id") String id) throws Exception;

    public List<AeaLocalDirectory> listAeaLocalDirectory(AeaLocalDirectory aeaLocalDirectory) throws Exception;

    public AeaLocalDirectory getAeaLocalDirectoryById(@Param("id") String id) throws Exception;
}
