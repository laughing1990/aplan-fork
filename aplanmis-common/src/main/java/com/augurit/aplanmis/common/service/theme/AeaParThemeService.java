package com.augurit.aplanmis.common.service.theme;

import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeSeq;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;

import java.util.List;

/**
 * @author 小糊涂
 */
public interface AeaParThemeService {
    /**
     * 插入主题表
     *
     * @param aeaParTheme 主题信息
     * @return 插入的条数
     * @throws Exception e
     */
    void insertAeaParTheme(AeaParTheme aeaParTheme) throws Exception;

    /**
     * 更新主题
     *
     * @param aeaParTheme 更新的主题信息
     * @return 更新的条数
     * @throws Exception e
     */
    void updateAeaParTheme(AeaParTheme aeaParTheme) throws Exception;

    /**
     * 根据themeId删除主题
     *
     * @param themeId 主题ID
     * @return 删除的条数
     * @throws Exception e
     */
    void deleteAeaParThemeByThemeId(String themeId) throws Exception;

    /**
     * 根据查询条件查询主题列表
     *
     * @param aeaParTheme 查询条件实体
     * @return List<AeaParTheme>
     * @throws Exception e
     */
    List<AeaParTheme> listAeaParTheme(AeaParTheme aeaParTheme) throws Exception;

    /**
     * 根据主题ID查询主题信息
     *
     * @param themeId 主题ID
     * @return AeaParTheme
     * @throws Exception e
     */
    AeaParTheme getAeaParThemeByThemeId(String themeId) throws Exception;

    /**
     * 根据主题类型查询该类型下主题列表：单表查询
     *
     * @param themeType 主题类型，数字字典获取的值
     * @return List<AeaParTheme>
     * @throws Exception 异常
     */
    List<AeaParTheme> getAeaParThemeListByThemeType(String themeType) throws Exception;

    /**
     * 根据主题ID获取最大序列信息
     *
     * @param themeId 主题ID
     * @return AeaParTheme
     * @throws Exception 异常
     */
    AeaParThemeSeq getAeaParThemeSeqByThemeId(String themeId,String rootOrgId) throws Exception;

    /**
     * 根据主题ID和版本号获取主题版本信息
     *
     * @param themeId 主题ID 必输
     * @param verNum  版本号 非必须
     * @return AeaParThemeVer
     * @throws Exception 异常
     */
    AeaParThemeVer getAeaParThemeVerByThemeIdAndVerNum(String themeId, Double verNum,String rootOrgId) throws Exception;

    /**
     * 根据项目ID或localCode||工程编码查询 项目绑定的主题
     *
     * @param projInfoId 项目主键ID
     * @param projCode   localCode||工程编码
     * @return AeaParTheme
     * @throws Exception 异常
     */
    AeaParTheme getAeaParThemeByProjIdorProjCode(String projInfoId, String projCode) throws Exception;

    /**
     * 关联查询所有或主题类型下的已发布或试运行状态的最大主题版本列表
     *
     * @param themeType 主题类型 可选查询参数
     * @param themeId   主题ID 可选查询参数
     * @return List<AeaParTheme>
     * @throws Exception e
     */
    List<AeaParTheme> getMaxVerAeaParTheme(String themeType, String themeId) throws Exception;

    /**
     * 根据主题版本ID查询主题信息
     * @param themeVerId 必须字段 主题版本ID
     * @return
     */
    AeaParTheme getAeaParThemeByThemeVerId(String themeVerId) throws Exception;

    List<AeaParTheme> getTestRunOrPublishedVerAeaParTheme(String themeType, String rootOrgId);

    /**
     *
     * @param dygjbzfxfw 对应国家标准辅线服务:多评合一（51）、方案联审（52）、联合审图（53）、联合测绘（54C）、联合验收（54Y）
     * @param isOnlineSb 网厅是否启用 非必传参数
     * @return
     */
    List<AeaParTheme> getTestRunOrPublishedVerAeaParThemeByDygjbzfxfw(String dygjbzfxfw,String isOnlineSb);
}
