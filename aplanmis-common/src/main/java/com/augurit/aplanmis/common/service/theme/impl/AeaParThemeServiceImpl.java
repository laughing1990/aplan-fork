package com.augurit.aplanmis.common.service.theme.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeSeq;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaParThemeMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeSeqMapper;
import com.augurit.aplanmis.common.mapper.AeaParThemeVerMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.theme.AeaParThemeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 小糊涂
 */
@Service
public class AeaParThemeServiceImpl implements AeaParThemeService {

    private static Logger LOGGER = LoggerFactory.getLogger(AeaParThemeServiceImpl.class);

    private AeaParThemeMapper aeaParThemeMapper;
    private AeaParThemeSeqMapper aeaParThemeSeqMapper;
    private AeaParThemeVerMapper aeaParThemeVerMapper;
    private AeaProjInfoMapper aeaProjInfoMapper;
    private AeaProjInfoService aeaProjInfoService;


    /**
     * 插入主题表
     *
     * @param aeaParTheme 主题信息
     * @return 插入的条数
     * @throws Exception e
     */
    @Override
    public void insertAeaParTheme(AeaParTheme aeaParTheme) throws Exception {

        aeaParThemeMapper.insertOne(aeaParTheme);
    }

    @Override
    public void updateAeaParTheme(AeaParTheme aeaParTheme) throws Exception {

        aeaParThemeMapper.updateOne(aeaParTheme);
    }

    @Override
    public void deleteAeaParThemeByThemeId(String themeId) throws Exception {

        aeaParThemeMapper.deleteById(themeId);
    }

    @Override
    public List<AeaParTheme> listAeaParTheme(AeaParTheme aeaParTheme) throws Exception {
        return aeaParThemeMapper.listAeaParTheme(aeaParTheme);
    }

    @Override
    public AeaParTheme getAeaParThemeByThemeId(String themeId) throws Exception {
        return aeaParThemeMapper.selectOneById(themeId);
    }

    @Override
    public List<AeaParTheme> getAeaParThemeListByThemeType(String themeType) throws Exception {
        String currentOrgId = SecurityContext.getCurrentOrgId();
        return aeaParThemeMapper.getAeaParThemeListByThemeType(themeType, currentOrgId);
    }

    /**
     * 根据主题ID获取最大序列信息
     *
     * @param themeId 主题ID
     * @return AeaParTheme
     * @throws Exception 异常
     */
    @Override
    public AeaParThemeSeq getAeaParThemeSeqByThemeId(String themeId,String rootOrgId) throws Exception {

        return aeaParThemeSeqMapper.getAeaParThemeSeqByThemeId(themeId, rootOrgId);
    }

    /**
     * 根据主题ID和版本号获取主题版本信息
     *
     * @param themeId 主题ID
     * @param verNum  版本号 可选参数
     * @return AeaParThemeVer
     * @throws Exception 异常
     */
    @Override
    public AeaParThemeVer getAeaParThemeVerByThemeIdAndVerNum(String themeId, Double verNum,String rootOrgId) throws Exception {

        if (StringUtils.isEmpty(verNum)) {
            List<AeaParThemeVer> list = aeaParThemeVerMapper.listTestRunOrPublishedVer(themeId, rootOrgId);
            if(list!=null&&list.size()>0){
                return list.get(0);
            }
        }
        return aeaParThemeVerMapper.getAeaParThemeVerByThemeIdAndVerNum(themeId, verNum, rootOrgId);
    }

    /**
     * 根据项目ID或localCode||工程编码查询 项目绑定的主题
     * 参数必输传一个
     *
     * @param projInfoId 项目主键ID
     * @param projCode   localCode||工程编码
     * @return AeaParTheme
     * @throws Exception 异常
     */
    @Override
    public AeaParTheme getAeaParThemeByProjIdorProjCode(String projInfoId, String projCode) throws Exception {

        if (!StringUtils.isEmpty(projInfoId)) {
            AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
            if (null != aeaProjInfo && !StringUtils.isEmpty(aeaProjInfo.getThemeId())) {
                return aeaParThemeMapper.selectOneById(aeaProjInfo.getThemeId());
            }
        } else if (!StringUtils.isEmpty(projCode)) {
            //1 搜索 查询；可能查询多个或者一个，默认取第一个
            List<AeaProjInfo> projList = aeaProjInfoService.findAeaProjInfoByKeyword(projCode);
            if (projList.size() > 0 && !StringUtils.isEmpty(projList.get(0).getThemeId())) {
                return aeaParThemeMapper.selectOneById(projList.get(0).getThemeId());
            }
        }
        return null;
    }

    /**
     * 关联查询所有或主题类型下的最大主题版本列表
     *
     * @param themeType 主题类型
     * @return List<AeaParTheme>
     * @throws Exception e
     */
    @Override
    public List<AeaParTheme> getMaxVerAeaParTheme(String themeType, String themeId) throws Exception {
        return aeaParThemeMapper.getMaxVerAeaParTheme(themeType, themeId);
    }

    @Override
    public AeaParTheme getAeaParThemeByThemeVerId(String themeVerId) throws Exception{
        return aeaParThemeMapper.getAeaParThemeByThemeVerId(themeVerId);
    }

    @Override
    public List<AeaParTheme> getTestRunOrPublishedVerAeaParTheme(String themeType, String rootOrgId) {
        return aeaParThemeMapper.getTestRunOrPublishedVerAeaParTheme(themeType, rootOrgId);
    }

    @Override
    public List<AeaParTheme> getTestRunOrPublishedVerAeaParThemeByDygjbzfxfw(String dygjbzfxfw,String isOnlineSb){
        return aeaParThemeMapper.listAeaParThemeByDygjbzfxfw(dygjbzfxfw,isOnlineSb);
    }

    @Autowired
    public void setAeaParThemeMapper(AeaParThemeMapper aeaParThemeMapper) {
        this.aeaParThemeMapper = aeaParThemeMapper;
    }
    @Autowired
    public void setAeaParThemeSeqMapper(AeaParThemeSeqMapper aeaParThemeSeqMapper) {
        this.aeaParThemeSeqMapper = aeaParThemeSeqMapper;
    }
    @Autowired
    public void setAeaParThemeVerMapper(AeaParThemeVerMapper aeaParThemeVerMapper) {
        this.aeaParThemeVerMapper = aeaParThemeVerMapper;
    }
    @Autowired
    public void setAeaProjInfoMapper(AeaProjInfoMapper aeaProjInfoMapper) {
        this.aeaProjInfoMapper = aeaProjInfoMapper;
    }
    @Autowired
    public void setAeaProjInfoService(AeaProjInfoService aeaProjInfoService) {
        this.aeaProjInfoService = aeaProjInfoService;
    }
}
