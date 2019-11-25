package com.augurit.aplanmis.common.service.project;

import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.springframework.lang.NonNull;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author yinlf
 * @Date 2019/7/5
 */
public interface AeaProjInfoService {

    /**
     * 新建项目
     *
     * @param aeaProjInfo 项目信息
     */
    void insertAeaProjInfo(AeaProjInfo aeaProjInfo);

    /**
     * 更新项目信息
     *
     * @param aeaProjInfo 项目信息
     */
    void updateAeaProjInfo(AeaProjInfo aeaProjInfo);


    /**
     * 删除项目
     *
     * @param projInfoId
     */
    void deleteAeaProjInfo(String... projInfoId);

    /**
     * 删除项目及关联关系。删除项目、单位关联、联系人关联
     *
     * @param projInfoId
     */
    void deleteAeaProjInfoCascade(String... projInfoId);

    /**
     * 查询项目信息
     *
     * @param gcbm 工程编码
     * @return 项目信息
     */
    AeaProjInfo getAeaProjInfoByGcbm(String gcbm);

    /**
     * 查询项目信息
     *
     * @param projInfoId 项目ID
     * @return 项目信息
     */
    AeaProjInfo getAeaProjInfoByProjInfoId(String projInfoId);

    /**
     * 查询申报项目列表
     *
     * @param applyinstId 申请实例ID
     * @return 申报项目列表
     */
    List<AeaProjInfo> findApplyProj(String applyinstId);

    /**
     * 查询子项目列表
     *
     * @param projInfoId 父项目ID
     * @return 项目列表
     */
    List<AeaProjInfo> findChildProj(String projInfoId);

    /**
     * 查询父项目信息
     *
     * @param projInfoId 子项目ID
     * @return 父项目信息
     */
    AeaProjInfo findParentProj(String projInfoId);

    /**
     * 搜索项目信息
     *
     * @param keyword 项目名称
     * @return 项目列表
     */
    List<AeaProjInfo> findAeaProjInfoByKeyword(String keyword);

    /**
     * 搜索项目信息
     *
     * @param keyword 项目名称
     * @param page
     * @return 项目列表
     */
    PageInfo<AeaProjInfo> listAeaProjInfoByKeyword(String keyword, Page page);

    /**
     * 搜索项目信息
     *
     * @param aeaProjInfo
     * @return 项目列表
     */
    List<AeaProjInfo> findAeaProjInfo(AeaProjInfo aeaProjInfo);

    /**
     * 搜索项目信息
     *
     * @param aeaProjInfo
     * @param page
     * @return 项目列表
     */
    PageInfo<AeaProjInfo> listAeaProjInfo(AeaProjInfo aeaProjInfo, Page page);

    /**
     * 根据申请人查询项目信息
     *
     * @param linkmanInfoId 申请人ID
     * @return
     */
    List<AeaProjInfo> findAeaProjInfoByApplyLinkmanInfoId(String linkmanInfoId);

    /**
     * 根据项目联系人查询项目信息
     *
     * @param linkmanInfoId 联系人ID
     * @return
     */
    List<AeaProjInfo> findRootAeaProjInfoByLinkmanInfoId(String linkmanInfoId);

    /**
     * 通过单位id获取项目信息
     */
    List<AeaProjInfo> findRootAeaProjInfoByUnitInfoId(String unitInfoId);

    /**
     * 通过联系人ID和单位ID查询项目信息
     *
     * @param linkmanInfoId
     * @param unitInfoId
     * @return
     */
    List<AeaProjInfo> findRootAeaProjInfoByLinkmanInfoIdAndUnitInfoId(String linkmanInfoId, String unitInfoId);


    /**
     * 用于mysql下
     * 根据项目的id获取所在树的全部项目信息
     *
     * @param projInfoId
     * @return
     */
    public List<AeaProjInfo> getListProjZtreeNodeMysql(String projInfoId);

    /**
     * 新增子项目
     *
     * @param aeaProjInfo
     * @param isSecond
     */
    public AeaProjInfo addChildProjInfo(AeaProjInfo aeaProjInfo, Boolean isSecond) throws Exception;

    /**
     * 合并子项目
     *
     * @param aeaProjInfo
     * @param projInfoIds
     * @return
     */
    public boolean mergeChildProjInfo(@NonNull AeaProjInfo aeaProjInfo, @NonNull String projInfoIds) throws Exception;

    /**
     * 删除子项目
     *
     * @param delProjId
     * @return
     * @throws Exception
     */
    public boolean deleteChildChildProj(String  delProjId) throws Exception;

    /**
     * 判断是数据库类型是否是mysql
     *
     * @param databaseIdProvider
     * @param dataSource
     * @return
     * @throws Exception
     */
    public boolean isCurrentMysql(DatabaseIdProvider databaseIdProvider, DataSource dataSource) throws Exception;

    /**
     * 根据项目id构造项目树
     *
     * @param projInfoId 项目id
     * @return 树节点
     */
    List<ZtreeNode> buildProjectTree(String projInfoId);

    List<AeaProjInfo> getAllProjectOfTree(String projInfoId, Map<String, String> child2Parent);

    List<AeaProjInfo> getProjListAndChildProjsByParent(String[] localCodes);

    /**
     * 获取即将要生成的子项目/子工程
     *
     * @param projName   父项目名称
     * @param projInfoId 父项目ID
     * @param localCode  父项目代码
     * @param gcbm       父工程编码
     * @return
     * @throws Exception
     */
    public AeaProjInfo getChildProject(String projName, String projInfoId, String localCode, String gcbm) throws Exception;

    /**
     * 查询项目的属地行政区划
     *
     * @param projInfoId
     * @return
     */
    List<String> getProjAddressRegion(String projInfoId);

    /**
     * 新增单体工程信息
     *
     * @param aeaProjInfo
     */
    public AeaProjInfo addChildProjInfo(AeaProjInfo aeaProjInfo) throws Exception;
}
