package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiItemCancel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 办件撤件实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-12-12 14:18:59</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiItemCancelMapper {

    public void insertAeaHiItemCancel(AeaHiItemCancel aeaHiItemCancel) throws Exception;

    public void updateAeaHiItemCancel(AeaHiItemCancel aeaHiItemCancel) throws Exception;

    public void deleteAeaHiItemCancel(@Param("id") String id) throws Exception;

    public List<AeaHiItemCancel> listAeaHiItemCancel(AeaHiItemCancel aeaHiItemCancel) throws Exception;

    public AeaHiItemCancel getAeaHiItemCancelById(@Param("id") String id) throws Exception;
}
