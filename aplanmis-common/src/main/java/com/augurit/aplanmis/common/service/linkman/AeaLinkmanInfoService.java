package com.augurit.aplanmis.common.service.linkman;

import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/7/5
 */
public interface AeaLinkmanInfoService {

    /**
     * 新建联系人
     *
     * @param aeaLinkmanInfo
     */
    void insertAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo);

    /**
     * 为项目添加联系人
     *
     * @param applyinstId 申报实例ID
     * @param projInfoId  项目ID
     * @param linkmanInfoId   联系人ID列表
     */
    void insertProjLinkman(String applyinstId, String projInfoId, String... linkmanInfoId);

    /**
     * 新增项目申请人
     *
     * @param applyinstId 申报实例ID
     * @param projInfoId  项目ID
     * @param linkmanInfoId   申请人ID
     */
    void insertApplyProjLinkman(String applyinstId, String projInfoId, String linkmanInfoId);

    /**
     * 为多个项目新增联系人和申请人
     *
     * @param applyinstId    申报实例ID
     * @param projInfoIds    项目ID数组
     * @param applyLinkmanId 申请人ID
     * @param linkmanInfoId      联系人ID
     */
    void insertApplyAndLinkProjLinkman(String applyinstId, String[] projInfoIds, String applyLinkmanId, String linkmanInfoId);

    /**
     * 为单位添加联系人
     *
     * @param unitInfoId 单位ID
     * @param linkmanInfoId  联系人ID列表
     */
    void insertUnitLinkman(String unitInfoId, String... linkmanInfoId);

    /**
     * 更新联系人信息
     *
     * @param aeaLinkmanInfo
     */
    void updateAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo);

    /**
     * 删除联系人信息
     *
     * @param linkmanInfoId 联系人ID
     */
    void deleteAeaLinkmanInfo(String... linkmanInfoId);

    /**
     * 删除联系人及联系人关联。删除联系人、项目联系人关联、单位联系人关联
     *
     * @param linkmanInfoId 联系人ID
     */
    void deleteAeaLinkmanInfoCascade(String linkmanInfoId);

    /**
     * 删除项目下联系人
     *
     * @param projInfoId 项目ID
     * @param linkmanInfoId  联系人ID列表
     */
    void deleteProjLinkman(String projInfoId, String... linkmanInfoId);

    /**
     * 查询联系人信息
     *
     * @param linkmanInfoId
     * @return 联系人信息
     */
    AeaLinkmanInfo getAeaLinkmanInfoByLinkmanInfoId(String linkmanInfoId);

    /**
     * 删除单位下联系人
     *
     * @param unitInfoId 单位ID
     * @param linkmanInfoId  联系人ID列表
     */
    void deleteUnitLinkman(String unitInfoId, String... linkmanInfoId);


    /**
     * 查询项目的联系人
     *
     * @param projInfoId 项目ID
     * @return 联系人信息
     */
    List<AeaLinkmanInfo> getProjLinkman(String applyinstId, String projInfoId);

    /**
     * 查询项目申请人
     *
     * @param applyinstId 申报实例ID
     * @param projInfoId  项目ID
     * @return 申请人信息
     */
    AeaLinkmanInfo getApplyProjLinkman(String applyinstId, String projInfoId);

    /**
     * 查询申报联系人
     *
     * @param applyinstId 申报实例ID
     * @return
     */
    AeaLinkmanInfo getApplyLinkman(String applyinstId);

    /**
     * 查询单位的所有联系人
     *
     * @param unitInfoId 单位ID
     * @return 联系人列表
     */
    List<AeaLinkmanInfo> findAllUnitLinkman(String unitInfoId);

    /**
     * 根据登录用户名查询联系人
     *
     * @param loginName
     * @return 联系人信息
     */
    AeaLinkmanInfo getAeaLinkmanInfoByLoginName(String loginName);

    /**
     * 搜索联系人。在以下中搜索：联系人姓名
     *
     * @param keyword 联系人名称
     * @return
     */
    List<AeaLinkmanInfo> findLinkmanInfoByKeyword(String keyword);

    /**
     * 搜索联系人
     *
     * @param keyword 联系人名称
     * @param page
     * @return
     */
    PageInfo<AeaLinkmanInfo> listLinkmanInfoByKeyword(String keyword, Page page);

    /**
     * 搜索联系人
     *
     * @param aeaLinkmanInfo
     * @return
     */
    List<AeaLinkmanInfo> findLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo);

    /**
     * 搜索联系人
     *
     * @param aeaLinkmanInfo
     * @param page
     * @return
     */
    PageInfo<AeaLinkmanInfo> listLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo, Page page);

    /**
     * 根据单位ID和是否绑定查询联系人/委托人
     * @param unitInfoId 必需参数 企业ID
     * @param isBindAccount 是否绑定 1是  0否  =1时，查询委托人列表，null时，查询所有联系人
     * @return
     * @throws Exception
     */
    List<AeaLinkmanInfo> getAeaLinkmanInfoByUnitInfoIdAndIsBindAccount(String unitInfoId, String isBindAccount) throws Exception;

    /**
     * 修改单位联系关联表信息
     * @param aeaUnitLinkman
     */
    void updateAeaUnitLinkmanByUnitAndLinkman(AeaUnitLinkman aeaUnitLinkman) throws Exception;

    /**
     * 根据关键字查询联系人信息
     * @param keyword 姓名，身份证号，电话
     * @param unitInfoId
     * @return
     */
    List<AeaLinkmanInfo> getByKeyword(String keyword, String unitInfoId) throws Exception;

    List<AeaLinkmanInfo> getAeaLinkmanInfoListByCertNo(String certNo);

    List<LinkmanTypeVo> findLinkmanTypes(String projInfoId, String unitInfoId);
}
