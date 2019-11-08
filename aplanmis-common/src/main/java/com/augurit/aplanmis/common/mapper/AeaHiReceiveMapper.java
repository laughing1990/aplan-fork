package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiReceive;
import com.augurit.aplanmis.common.dto.AeaHiReceiveDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 领取登记表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:20</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiReceiveMapper {

    public void insertAeaHiReceive(AeaHiReceive aeaHiReceive) throws Exception;

    public void updateAeaHiReceive(AeaHiReceive aeaHiReceive) throws Exception;

    public void deleteAeaHiReceive(@Param("id") String id) throws Exception;

    public List<AeaHiReceive> listAeaHiReceive(AeaHiReceive aeaHiReceive) throws Exception;

    public AeaHiReceive getAeaHiReceiveById(@Param("id") String id) throws Exception;

    List<AeaHiReceive> getAeaHiReceiveByApplyinstIdAndReceiptType(@Param("applyinstId") String applyinstId, @Param("receiveType") String receiveType);

    /**
     * 根据申请实例ID集合获取回执列表--只查AEA_HI_RECEIVE 不区分回执所属事项或阶段
     *
     * @param applyinstIds
     * @return
     */
    List<AeaHiReceive> getAeaHiReceiveByApplyinstIds(@Param("applyinstIds") String[] applyinstIds);

    /**
     * 获取指定申报实例下指定类型的回执列表
     *
     * @param applyinstIds
     * @param receiptTypes
     * @return
     */
    List<AeaHiReceive> listReceiveByApplyinstIdAndTypes(@Param("applyinstIds") String[] applyinstIds, @Param("receiptTypes") String[] receiptTypes);

    /**
     * 根据申请实例ID集合获取回执列表及回执所属事项或阶段
     * 方法有问题，启用
     *
     * @param applyinstIds
     * @return
     */
    List<AeaHiReceiveDto> getAeaHiReceiveListByApplyinstIds(@Param("applyinstIds") String[] applyinstIds);

    List<AeaHiReceiveDto> getAeaHiReceiveListByApplyinstId(@Param("applyinstId") String applyinstId);
}
