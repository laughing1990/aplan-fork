package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaUnitProjLinkman;
import com.augurit.aplanmis.common.domain.PersonSetting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 项目单位联系人类型表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaUnitProjLinkmanMapper {
    /**
     * 1 根据主键查询单个
     *
     * @param projLinkmanId 主键
     * @return AeaUnitProjLinkman
     */
    AeaUnitProjLinkman getAeaUnitProjLinkmanById(@Param("projLinkmanId") String projLinkmanId);

    /**
     * 2 根据查询条件查询
     *
     * @param aeaUnitProjLinkman 查询条件
     * @return List<AeaUnitProjLinkman>
     */
    List<AeaUnitProjLinkman> listAeaUnitProjLinkman(AeaUnitProjLinkman aeaUnitProjLinkman);

    /**
     * 3 根据主键批量查询
     *
     * @param projLinkmanIds 查询集合
     * @return list
     */
    List<AeaUnitProjLinkman> listAeaUnitProjLinkmanIds(@Param("projLinkmanIds") String[] projLinkmanIds);

    /**
     * 4 更新单条信息
     *
     * @param aeaUnitProjLinkman 更新的数据
     * @return 更新的条数
     */
    int updateAeaUnitProjLinkman(AeaUnitProjLinkman aeaUnitProjLinkman);

    /**
     * 5 根据主键批量删除
     *
     * @param projLinkmanIds 删除主键集合
     * @return 删除的条数
     */
    int batchDelAeaUnitProjLinkmanByIds(@Param("projLinkmanIds") String[] projLinkmanIds);

    /**
     * 6 根据 单位ID|联系人ID|联系人类型查询
     *
     * @param unitInfoId 单位iID
     * @param linkmanInfoId 联系人ID
     * @param linkmanType 联系人类型（来源数据字典）
     * @return
     */
    List<AeaUnitProjLinkman> queryAeaUnitProjLinkman(@Param("unitInfoId") String unitInfoId, @Param("linkmanInfoId") String linkmanInfoId, @Param("linkmanType") String linkmanType);

    /**
     * 7 单个插入
     *
     * @param aeaUnitProjLinkman 插入的数据
     * @return 插入的条数
     */
    int insertAeaUnitProjLinkman(AeaUnitProjLinkman aeaUnitProjLinkman);

    /**
     * 8 批量更新数据
     */
    int batchUpdateAeaProjLinkman(List<AeaUnitProjLinkman> aeaUnitProjLinkmanList);

    /**
     * 9 批量插入
     *
     * @param aeaUnitProjLinkmanList 出入的lit
     * @return 插入的条数
     */
    int batchInsertAeaUnitProjLinkman(List<AeaUnitProjLinkman> aeaUnitProjLinkmanList);

    /**
     * 10 根据 UNIT_PROJ_ID删除数据
     *
     * @param unitProjId UNIT_PROJ_ID
     * @return 删除的行数
     */
    int deleteAllByUnitProjId(@Param("unitProjId") String unitProjId, @Param("modifier") String modifier);

    /**
     * 11
     *
     * @param unitProjId  unitProjId
     * @param linkmanType linkmanType
     * @return AeaUnitProjLinkman
     */
    List<AeaUnitProjLinkman> queryByUnitProjIdAndlinkType(@Param("unitProjId") String unitProjId, @Param("linkmanInfoId") String linkmanInfoId, @Param("linkmanType") String linkmanType);

    /**
     * 12 查找负责人
     *
     * @param aeaUnitProjLinkman 查询条件
     * @return List<AeaUnitProjLinkman>
     */
    List<AeaUnitProjLinkman> listfuzeren(AeaUnitProjLinkman aeaUnitProjLinkman);

    /**
     * 搜索单位人员信息
     */
    List<PersonSetting> findPersonSetting(@Param("unitProjId") String unitProjId);
}
