package com.augurit.aplanmis.common.mapper;

import com.augurit.aplanmis.common.domain.AeaParFactorTheme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 阶段与事项关联定义表-Mapper数据与持久化接口类
 */
@Mapper
@Repository
public interface AeaParFactorThemeMapper {

    void insertAeaParFactorTheme(AeaParFactorTheme aeaParFactorTheme) ;

    void updateAeaParFactorTheme(AeaParFactorTheme aeaParFactorTheme) ;

    void deleteAeaParFactorTheme(@Param("id") String id) ;

    void deleteAeaParFactorThemeByFactorId(@Param("factorId") String factorId) ;

    List<AeaParFactorTheme> listAeaParFactorTheme(AeaParFactorTheme aeaParFactorTheme) ;

    AeaParFactorTheme getAeaParFactorThemeById(@Param("id") String id) ;
}
