package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * -Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:22</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiSmsInfoMapper {

    public void insertAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo) throws Exception;

    public void updateAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo) throws Exception;

    public void deleteAeaHiSmsInfo(@Param("id") String id) throws Exception;

    public List<AeaHiSmsInfo> listAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo) throws Exception;

    public AeaHiSmsInfo getAeaHiSmsInfoById(@Param("id") String id) throws Exception;

    public AeaHiSmsInfo getAeaHiSmsInfoByApplyinstId(@Param("applyinstId") String applyinstId) throws Exception;

    public List<AeaHiSmsInfo> listAeaHiSmsInfoLikeSenderName(@Param("senderName") String senderName,@Param("rootOrgId") String rootOrgId) throws Exception;

    public AeaHiSmsInfo getAeaHiSmsInfoByOrderIdOrExpressNum(@Param("orderId") String orderId,@Param("expressNum") String expressNum,@Param("rootOrgId") String rootOrgId) throws Exception;
}
