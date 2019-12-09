package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 主题定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParThemeMapper extends BaseMapper<AeaParTheme> {

    /**
     * 获取主题最大排序号
     *
     * @return
     */
    Long getMaxSortNo(@Param("rootOrgId") String rootOrgId);

    /**
     * 根据查询条件查询主题列表
     *
     * @param aeaParTheme 查询条件实体
     * @return List<AeaParTheme>
     * @ e
     */
    List<AeaParTheme> listAeaParTheme(AeaParTheme aeaParTheme);

    /**
     * 通过事项与事项版本查询唯一主题集合
     *
     * @param itemId
     * @param itemVerId
     * @param rootOrgId
     * @return
     */
    List<AeaParTheme> listUniqueThemeByItemIdAndItemVerId(@Param("itemId") String itemId,
                                                          @Param("itemVerId") String itemVerId,
                                                          @Param("rootOrgId") String rootOrgId);

    /**
     * 根据主题类型查询该类型下主题列表
     *
     * @param themeType 主题类型，数字字典获取的值
     * @return List<AeaParTheme>
     * @ 异常
     */
    List<AeaParTheme> getAeaParThemeListByThemeType(@Param("themeType") String themeType, @Param("rootOrgId") String rootOrgId);

    /**
     * 关联查询所有或主题类型下的最大主题版本列表
     *
     * @param themeType 主题类型 可选条件
     * @return List<AeaParTheme>
     * @ e
     */
    List<AeaParTheme> getMaxVerAeaParTheme(@Param("themeType") String themeType, @Param("themeId") String themeId);

    AeaParTheme getAeaParThemeByThemeVerId(String themeVerId);

    List<AeaParTheme> getTestRunOrPublishedVerAeaParTheme(@Param("themeType") String themeType, @Param("rootOrgId") String rootOrgId);

    //根据主题名称，阶段名称查询主题列表
    List<AeaParTheme> listAeaParThemeByKeyword(AeaParTheme aeaParTheme);

    List<AeaParTheme> listAeaParThemeByDygjbzfxfw(@Param("dygjbzfxfw")String dygjbzfxfw,@Param("isOnlineSb")String isOnlineSb);
}
