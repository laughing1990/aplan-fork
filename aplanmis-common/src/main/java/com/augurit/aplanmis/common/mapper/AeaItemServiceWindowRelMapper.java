package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaItemServiceWindowRel;
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
 * <li>创建时间：2019-07-04 17:13:57</li>
 * </ul>
 */
@Mapper
public interface AeaItemServiceWindowRelMapper {

    public void insertAeaItemServiceWindowRel(AeaItemServiceWindowRel aeaItemServiceWindowRel) throws Exception;

    public void updateAeaItemServiceWindowRel(AeaItemServiceWindowRel aeaItemServiceWindowRel) throws Exception;

    public void deleteAeaItemServiceWindowRel(@Param("id") String id) throws Exception;

    public List<AeaItemServiceWindowRel> listAeaItemServiceWindowRel(AeaItemServiceWindowRel aeaItemServiceWindowRel) throws Exception;

    public AeaItemServiceWindowRel getAeaItemServiceWindowRelById(@Param("id") String id) throws Exception;

    List<AeaItemServiceWindowRel> listAeaItemServiceWindowRelAndWindowInfo(AeaItemServiceWindowRel query);

    List<AeaItemServiceWindowRel> listAeaItemServiceWindowRelAndWindowInfoByKeyword(AeaItemServiceWindowRel aeaItemServiceWindowRel);
}
