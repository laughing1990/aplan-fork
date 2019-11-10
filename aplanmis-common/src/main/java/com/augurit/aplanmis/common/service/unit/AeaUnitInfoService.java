package com.augurit.aplanmis.common.service.unit;

import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import com.augurit.aplanmis.common.domain.ExSJUnitFromDetails;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;


/**
 * @author yinlf
 * @Date 2019/7/5
 */
public interface AeaUnitInfoService {

    /**
     * 新建单位
     *
     * @param aeaUnitInfo
     */
    void insertAeaUnitInfo(AeaUnitInfo aeaUnitInfo);

    /**
     * 更新单位信息
     *
     * @param aeaUnitInfo
     */
    void updateAeaUnitInfo(AeaUnitInfo aeaUnitInfo);

    /**
     * 新增项目单位
     *
     * @param aeaUnitProj
     */
    void insertAeaUnitProj(AeaUnitProj aeaUnitProj);

    /**
     * 批量新增项目单位
     *
     * @param aeaUnitProjList
     */
    void batchInserAeaUnitProj(List<AeaUnitProj> aeaUnitProjList);

    /**
     * 删除单位信息
     *
     * @param unitInfoId 单位ID
     */
    void deleteAeaUnitInfo(String... unitInfoId);

    /**
     * 删除单位及关联。删除单位、项目单位关联、单位联系人关联
     *
     * @param unitInfoId 单位ID
     */
    void deleteAeaUnitInfoCascade(String... unitInfoId);

    /**
     * 单位ID查询单位信息
     *
     * @param unitInfoId 单位ID
     * @return
     */
    AeaUnitInfo getAeaUnitInfoByUnitInfoId(String unitInfoId);

    /**
     * 项目添加建设单位
     *
     * @param projInfoId 项目ID
     * @param unitInfoId 单位ID
     */
    void insertOwnerUnitProj(String projInfoId, String... unitInfoId);

    /**
     * 多项目添加建设单位
     *
     * @param unitProjMap key：projInfoId 项目ID，value：List<unitInfoId> 单位ID列表
     */
    void insertOwnerUnitProj(Map<String, List<String>> unitProjMap);

    /**
     * 项目添加经办单位
     *
     * @param projInfoId 项目ID
     * @param unitInfoId 单位ID
     */
    void insertNonOwnerUnitProj(String projInfoId, String... unitInfoId);

    /**
     * 多个项目添加经办单位
     *
     * @param projInfoIds 多个项目ID
     * @param unitInfoId  单位ID
     */
    void insertNonOwnerUnitProj(List<String> projInfoIds, String... unitInfoId);

    /**
     * 删除项目下单位。删除项目单位关联关系.
     *
     * @param projInfoId 项目ID
     * @param isOwner    是否建设单位。1：删除建设单位，0删除经办单位。<code>null</code>删除建设单位和经办单位。
     * @param unitInfoId 单位ID
     */
    void deleteUnitProj(String projInfoId, String isOwner, String... unitInfoId);

    /**
     * 新增项目申报建设单位
     *
     * @param applyinstId 申报实例ID
     * @param projInfoId  项目ID
     * @param unitInfoId  单位ID
     */
    void insertApplyOwnerUnitProj(String applyinstId, String projInfoId, String... unitInfoId);

    /**
     * 新增项目申报建设单位
     *
     * @param applyinstId 申报实例ID
     * @param unitProjMap key：projInfoId 项目ID，value：List<unitInfoId> 单位ID列表
     */
    void insertApplyOwnerUnitProj(String applyinstId, Map<String, List<String>> unitProjMap);

    /**
     * 新增项目申报经办单位
     *
     * @param applyinstId 申报实例ID
     * @param projInfoId  项目ID
     * @param unitInfoId  单位ID
     */
    void insertApplyNonOwnerUnitProj(String applyinstId, String projInfoId, String... unitInfoId);

    /**
     * 新增项目申报经办单位
     *
     * @param applyinstId 申报实例ID
     * @param projInfoIds 项目单位ID列表
     */
    void insertApplyNonOwnerUnitProj(String applyinstId, String[] projInfoIds, String... unitInfoId);

    /**
     * 查询项目申报单位
     *
     * @param applyinstId 申报实例ID
     * @param projInfoId  项目ID
     * @return 申报单位列表
     */
    List<AeaUnitInfo> findApplyUnitProj(String applyinstId, String projInfoId);

    /**
     * 查询项目申报建设单位
     *
     * @param applyinstId 申报实例ID
     * @param projInfoId  项目ID
     * @return 申报建设单位列表
     */
    List<AeaUnitInfo> findApplyOwnerUnitProj(String applyinstId, String projInfoId);

    /**
     * 查询项目申报经办单位
     *
     * @param applyinstId 申报实例ID
     * @param projInfoId  项目ID
     * @return 申报经办单位列表
     */
    List<AeaUnitInfo> findApplyNonOwnerUnitProj(String applyinstId, String projInfoId);

    /**
     * 根据单位类型和项目ID查询单位
     *
     * @param projInfoId 项目ID
     * @param unitType   单位类型
     * @return 单位列表
     */
    List<AeaUnitInfo> findUnitProjByProjInfoIdAndType(String projInfoId, String unitType);

    /**
     * 查询项目的所有单位
     *
     * @param projInfoId 项目ID
     * @return 单位列表
     */
    List<AeaUnitInfo> findAllProjUnit(String projInfoId);

    /**
     * 查询项目建设单位
     *
     * @param projInfoId 项目ID
     * @return 建设单位
     */
    List<AeaUnitInfo> findOwerUnitProj(String projInfoId);

    /**
     * 查询项目非建设单位
     *
     * @param projInfoId 项目ID
     * @return 非建设单位
     */
    List<AeaUnitInfo> findNonOwerUnitProj(String projInfoId);

    /**
     * 查询项目建设单位
     *
     * @param projInfoId 项目ID
     * @return 建设单位
     */
    List<AeaUnitInfo> findPrimaryUnitProj(String projInfoId);

    /**
     * 查询项目非建设单位
     *
     * @param projInfoId 项目ID
     * @return 非建设单位
     */
    List<AeaUnitInfo> findNonPrimaryUnitProj(String projInfoId);

    /**
     * 查询子单位列表
     *
     * @param unitInfoId 父单位ID
     * @return 单位列表
     */
    List<AeaUnitInfo> findChildUnit(String unitInfoId);

    /**
     * 查询父单位信息
     *
     * @param unitInfoId 子单位ID
     * @return 父单位信息
     */
    AeaUnitInfo getParentUnit(String unitInfoId);

    /**
     * 根据登录用户名查询单位信息
     *
     * @param loginName
     * @Exception
     */
    AeaUnitInfo getAeaUnitInfoByLoginName(String loginName);

    /**
     * 搜索单位信息
     *
     * @param keyword 单位名称
     * @return 单位列表
     */
    List<AeaUnitInfo> findAeaUnitInfoByKeyword(String keyword);

    /**
     * 搜索单位信息
     *
     * @param keyword 单位名称
     * @param page
     * @return 单位列表
     */
    PageInfo<AeaUnitInfo> listAeaUnitInfoByKeyword(String keyword, Page page);

    /**
     * 搜索单位信息
     *
     * @param aeaUnitInfo
     * @return 单位列表
     */
    List<AeaUnitInfo> findAeaUnitInfo(AeaUnitInfo aeaUnitInfo);

    /**
     * 搜索单位信息
     *
     * @param aeaUnitInfo
     * @param page
     * @return 单位列表
     */
    PageInfo<AeaUnitInfo> listAeaUnitInfo(AeaUnitInfo aeaUnitInfo, Page page);

    /**
     * 根据联系人ID查找所属单位
     * @param linkmanInfoId
     * @return
     */
    List<AeaUnitInfo> getUnitInfoByLinkmanInfoId(String linkmanInfoId);

    List<AeaUnitInfo> getUnitInfoListByIdCard(String idcard);

    /**
     * 单位模糊搜索，供施工和监理单位信息使用
     * @param keyword
     * @return
     */
    List<ExSJUnitFromDetails> findAeaExProBuildUnitInfoByKeyword(String keyword);
}
