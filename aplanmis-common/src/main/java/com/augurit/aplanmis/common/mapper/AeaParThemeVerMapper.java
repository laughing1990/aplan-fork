package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 主题最大版本号表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParThemeVerMapper extends BaseMapper<AeaParThemeVer> {

    /**
     * 查询
     *
     * @param aeaParThemeVer
     * @return
     */
    List<AeaParThemeVer> listAeaParThemeVer(AeaParThemeVer aeaParThemeVer);

    /**
     * 根据主题ID和版本号获取主题版本信息
     *
     * @param themeId
     * @param verNum
     * @param rootOrgId
     * @return
     */
    AeaParThemeVer getAeaParThemeVerByThemeIdAndVerNum(@Param("themeId") String themeId,
                                                       @Param("verNum") Double verNum,
                                                       @Param("rootOrgId") String rootOrgId);

    /**
     * 过时其他所有试运行或者发布版本，除了themeVerId这个版本
     *
     * @param themeId
     * @param themeVerId
     * @param rootOrgId
     */
    void deprecateAllTestRunAndPublishedVersion(@Param("themeId") String themeId,
                                                @Param("themeVerId") String themeVerId,
                                                @Param("rootOrgId") String rootOrgId);

    /**
     * 通过事项与事项版本查询对应主题版本
     *
     * @param itemId
     * @param itemVerId
     * @return
     */
    List<AeaParThemeVer> listThemeVerByItemIdAndItemVerId(@Param("itemId") String itemId,
                                                          @Param("itemVerId") String itemVerId,
                                                          @Param("rootOrgId") String rootOrgId);

    AeaParThemeVer getAeaParThemeVerById(@Param("id") String id);

    /**
     * 通过主题ID查询所有已发布、试运行、已过时版本的主题版本
     *
     * @param themeIds 主题ID。多个ID用单引号和逗号拼接成字符串
     * @return
     */
    List<AeaParThemeVer> listThemeVerByThemeIds(@Param("themeIds") String themeIds);

    void setThemeVerDiagramNull(@Param("themeVerId") String themeVerId);

    AeaParThemeVer getTestRunOrPublishedVer(@Param("themeId") String themeId, @Param("rootOrgId") String rootOrgId);

    List<AeaParThemeVer> getNotExpireThemeverByItemId(@Param("itemId") String itemId, @Param("rootOrgId")String rootOrgId);
}
