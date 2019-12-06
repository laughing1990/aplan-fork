package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaHiItemInoutinst;
import com.augurit.aplanmis.common.domain.AeaMatinst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 事项输入输出实例表-Mapper数据与持久化接口类
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:13</li>
 * </ul>
 */
@Mapper
@Repository
public interface AeaHiItemInoutinstMapper {

    public void insertAeaHiItemInoutinst(AeaHiItemInoutinst aeaHiItemInoutinst) throws Exception;

    public void updateAeaHiItemInoutinst(AeaHiItemInoutinst aeaHiItemInoutinst) throws Exception;

    public void deleteAeaHiItemInoutinst(@Param("id") String id) throws Exception;

    public List<AeaHiItemInoutinst> listAeaHiItemInoutinst(AeaHiItemInoutinst aeaHiItemInoutinst) throws Exception;

    public AeaHiItemInoutinst getAeaHiItemInoutinstById(@Param("id") String id) throws Exception;


    /**
     * 批量插入
     *
     * @param aeaHiItemInoutinstList
     * @return 插入的条数
     */
    int batchInsertAeaHiItemInoutinst(@Param("list") List<AeaHiItemInoutinst> aeaHiItemInoutinstList);

    /**
     * 批量修改
     *
     * @param aeaHiItemInoutinstList
     * @return 插入的条数
     */
    int batchUpdateAeaHiItemInoutinst(@Param("list") List<AeaHiItemInoutinst> aeaHiItemInoutinstList);

    /**
     * 根据材料实例ID删除记录
     *
     * @param matinstId 材料实例ID
     * @return 删除的条数
     * @throws Exception e
     */
    int deleteAeaHiItemInoutinstByMatinstId(@Param("matinstId") String matinstId) throws Exception;

    /**
     * @param matinstId
     * @return
     */
    List<AeaHiItemInoutinst> getAeaHiItemInoutinstByMatinstId(@Param("matinstId") String matinstId);

    /**
     * 根据iteminst找到其输出证照的实例，关联了inout表
     * @param iteminstId
     * @return
     * @throws Exception
     */
    List<AeaHiItemInoutinst> getAeaHiItemInoutinstOutByIteminstId(String iteminstId) throws Exception;
    /**
     * 根据iteminst找到其输出证照的实例，不关联inout表
     * @param iteminstId
     * @param rootOrgId
     * @return
     * @throws Exception
     */
    List<AeaHiItemInoutinst> getAeaHiItemInoutinstOutByIteminstIdWithoutInOut(@Param("iteminstId") String iteminstId, @Param("rootOrgId") String rootOrgId) throws Exception;

    List<AeaHiItemInoutinst> getAeaHiIteminstCertByIteminstId(String iteminstId);

    //根据证照实例ID删除输入输出实例
    int deleteAeaHiItemInoutinstByCertinstIds(@Param("certinstIds") String[] certinstIds);

    int deleteAeaHiItemInoutinstByMatinstIds(@Param("matinstIds") String[] matinstIds);

    //根据证照实例ID获取单个输入输出实例
    List<AeaHiItemInoutinst> getAeaHiIteminstCertByCertinstId(@Param("certinstId") String certinstId);

    //获取申报实例下所有事项的证照实例列表
    List<AeaHiItemInoutinst> getAeaHiIteminstCertByIteminstIds(@Param("iteminstIds") String iteminstIds);

    List<AeaMatinst> getSeriesMatingList(@Param("iteminstId") String iteminstId, @Param("isOfficeDoc") String isOfficeDoc);

    /**
     * 根据iteminstId查询所有的材料实例
     *
     * @param iteminstId
     * @param isOfficeDoc
     * @return
     */
    List<AeaMatinst> getAllUnionMatinstList(@Param("iteminstId") String iteminstId, @Param("isOfficeDoc") String isOfficeDoc);

    /**
     * 根据iteminstIds查询单项或并联事项实例的材料
     *
     * @param iteminstIds 事项实例ID 必输项
     * @param isOfficeDoc 是否批文批复 0 否，1 是 非必须
     * @return
     */
    List<AeaMatinst> getMatinstListBy(@Param("iteminstIds") String[] iteminstIds, @Param("isOfficeDoc") String isOfficeDoc);

    /**
     * 根据事项实例ID和材料ID获取材料实例列表---单项申报
     *
     * @param iteminstId 事项实例ID
     * @param matIds     材料ID
     * @return
     */
    List<AeaMatinst> getMatinstListByiteminstIdAndMatId(@Param("iteminstId") String iteminstId, @Param("matIds") String[] matIds);

    List<AeaHiItemInoutinst> getAeaHiItemInoutinstByIteminstIds(@Param("ids")String[] oldIteminstIds);

    void batchDeleteAeaHiItemInoutinst(@Param("ids")String[] outinstIds);
}
