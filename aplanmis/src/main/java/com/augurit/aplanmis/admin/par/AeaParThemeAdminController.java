package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeAdminService;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeVerAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * 主题表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/theme")
public class AeaParThemeAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaParThemeAdminController.class);

    @Autowired
    private AeaParThemeAdminService themeService;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private AeaParThemeVerAdminService themeVerService;

    @Autowired
    private AeaParStageAdminService stageService;

    /**
     * 主题管理
     *
     * @param modelMap
     * @param theme
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelMap modelMap, AeaParTheme theme) {

        //前端传递的参数回显
        if(theme!=null){
            modelMap.put("keyword", theme.getKeyword());
            modelMap.put("themeType", theme.getThemeType());
            modelMap.put("themeTypeName", theme.getThemeTypeName());
        }else{
            theme = new AeaParTheme();
        }

        //主题数据
        theme.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaParTheme> themes = themeService.listAeaParTheme(theme);
        if (themes != null && themes.size() > 0) {
            for (AeaParTheme th : themes) {
                // 最大版本号
                AeaParThemeVer sthemeVer = new AeaParThemeVer();
                sthemeVer.setThemeId(th.getThemeId());
                sthemeVer.setRootOrgId(SecurityContext.getCurrentOrgId());
                sthemeVer.setIsActive(ActiveStatus.ACTIVE.getValue());
                sthemeVer.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
                List<AeaParThemeVer> themeVerList = themeVerService.listAeaParThemeVer(sthemeVer);
                if (themeVerList != null && themeVerList.size() > 0) {
                    th.setThemeVerName("V" + themeVerList.get(0).getVerNum());
                }
            }
        }
        modelMap.put("themes", themes);

        // 数据字典
        loadBscDicDatas(modelMap);

        return new ModelAndView("ui-jsp/theme/theme_index");
    }

    /**
     * 主题版本
     *
     * @param modelMap
     * @param themeId
     * @return
     */
    @RequestMapping("/themeVerIndex.do")
    public ModelAndView editIndex(ModelMap modelMap, String themeId) {

        if (StringUtils.isBlank(themeId)) {
            throw new InvalidParameterException("参数themeId为空!");
        }
        AeaParTheme theme = themeService.getAeaParThemeById(themeId);
        modelMap.put("theme", theme);
        // 数据字典
        loadBscDicDatas(modelMap);
        return new ModelAndView("ui-jsp/theme/version/theme_ver_index");
    }

    /**
     * 获取数据字典的值
     *
     * @param modelMap
     */
    private void loadBscDicDatas(ModelMap modelMap) {

        // 数据字典主题类型 --> 原先数据字典编号: XM_PROJECT_STEP
        String topOrgId = SecurityContext.getCurrentOrgId();
        List<BscDicCodeItem> themeTypes = bscDicCodeService.getActiveItemsByTypeCode("THEME_TYPE", topOrgId);
        modelMap.put("themeTypes", themeTypes);

        // 数据字段承诺时限单位
        List<BscDicCodeItem> dueUnits = bscDicCodeService.getActiveItemsByTypeCode("DUE_UNIT_TYPE", topOrgId);
        modelMap.put("dueUnits", dueUnits);

        // 国家审批流程类型
        List<BscDicCodeItem> gzbzsplclxs = bscDicCodeService.getActiveItemsByTypeCode("GJBZSPLCLX", topOrgId);
        modelMap.put("gzbzsplclxs", gzbzsplclxs);

        // 对应标准审批阶段序号
        List<BscDicCodeItem> dybzspjdxhs = bscDicCodeService.getActiveItemsByTypeCode("GJBZSPJD", topOrgId);
        modelMap.put("dybzspjdxhs", dybzspjdxhs);

        // 对应标准辅线服务
        List<BscDicCodeItem> dybzfxfws = bscDicCodeService.getActiveItemsByTypeCode("GJBZFXFW", topOrgId);
        modelMap.put("dybzfxfws", dybzfxfws);
    }

    @RequestMapping("/listAeaParTheme.do")
    public PageInfo<AeaParTheme> listAeaParTheme(AeaParTheme aeaParTheme, Page page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParTheme);
        return themeService.listAeaParTheme(aeaParTheme, page);
    }

    @RequestMapping("/listAeaParThemeNoPage.do")
    public List<AeaParTheme> listAeaParThemeNoPage(AeaParTheme aeaParTheme) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParTheme);
        return themeService.listAeaParTheme(aeaParTheme);
    }

    @RequestMapping("/getAeaParTheme.do")
    public AeaParTheme getAeaParTheme(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取AeaParTheme对象，ID为：{}", id);
            return themeService.getAeaParThemeById(id);
        } else {
            logger.debug("构建新的AeaParTheme对象");
            return new AeaParTheme();
        }
    }

    @RequestMapping("/updateAeaParTheme.do")
    public ResultForm updateAeaParTheme(AeaParTheme aeaParTheme) {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaParTheme);
        themeService.updateAeaParTheme(aeaParTheme);
        return new ResultForm(true);
    }

    /**
     * 保存或编辑主题表
     *
     * @param aeaParTheme 主题表
     * @return 返回结果对象 包含结果信息
     */
    @RequestMapping("/saveThemeData.do")
    public ResultForm saveThemeData(AeaParTheme aeaParTheme) {

        if (StringUtils.isNotBlank(aeaParTheme.getThemeId())) {
            themeService.updateAeaParTheme(aeaParTheme);
        } else {
            aeaParTheme.setThemeId(UUID.randomUUID().toString());
            aeaParTheme.setRootOrgId(SecurityContext.getCurrentOrgId());
            themeService.saveAeaParTheme(aeaParTheme);
        }
        return new ContentResultForm<>(true, aeaParTheme);
    }

    @RequestMapping("/deleteAeaParThemeById.do")
    public ResultForm deleteAeaParThemeById(String id) {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("删除主题表Form对象，对象id为：{}", id);
            themeService.deleteAeaParThemeById(id);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/enableOrDisableThemeById.do")
    public ResultForm enableOrDisableThemeById(AeaParTheme theme) {

        if (theme != null && StringUtils.isNotBlank(theme.getThemeId())) {
            themeService.updateAeaParTheme(theme);
            return new ResultForm(true);
        } else {
            return new ResultForm(false, "传递参数themeId为空!");
        }
    }

    @RequestMapping("/setThemeStageImg.do")
    public Map<String,Object> setThemeStageImg(String bizId, String bizType, String type,
                                               String imgSize, HttpServletRequest request) throws Exception {

        List<Map<String,String>> fileNameList = listClassPathImageFiles(imgSize, false);
        String linkImg = "";
        if(StringUtils.isNotBlank(bizType)){
            if("theme".equals(bizType)){
                AeaParTheme theme = themeService.getAeaParThemeById(bizId);
                if(type !=null&&theme!=null){
                    if("small".equals(type)){
                        linkImg = theme.getSmallImgPath();
                    }else if("middle".equals(type)){
                        linkImg = theme.getMiddleImgPath();
                    }else if("big".equals(type)){
                        linkImg = theme.getBigImgPath();
                    }else if("huge".equals(type)){
                        linkImg = theme.getHugeImgPath();
                    }
                }
            }else if("stage".equals(bizType)){
                AeaParStage stage = stageService.getAeaParStageById(bizId);
                if(type !=null&&stage!=null){
                    if("small".equals(type)){
                        linkImg = stage.getSmallImgPath();
                    }else if("middle".equals(type)){
                        linkImg = stage.getMiddleImgPath();
                    }else if("big".equals(type)){
                        linkImg = stage.getBigImgPath();
                    }else if("huge".equals(type)){
                        linkImg = stage.getHugeImgPath();
                    }
                }
            }
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("fileNameList", fileNameList);
        map.put("linkImg", linkImg);
        return map;
    }

    private List<Map<String, String>> listClassPathImageFiles(String imgSize, boolean isNeedContextPath) throws Exception {

        String basePath = "/";
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (isNeedContextPath) {
            String path = request.getContextPath();
            basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        }

        List<Map<String, String>> returnList = new ArrayList();
        String relativePath = "static/admin/theme/index/imgs/" + imgSize;
        String realPath = basePath + "admin/theme/index/imgs/" + imgSize + "/";
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(relativePath + "/*.*");
            if (resources != null && resources.length > 0) {
                Resource[] var11 = resources;
                int var12 = resources.length;

                for(int var13 = 0; var13 < var12; ++var13) {
                    Resource resource = var11[var13];
                    String fileName = resource.getFilename();
                    if (fileName.toLowerCase().endsWith(".png") || fileName.toLowerCase().endsWith(".gif")) {
                        Map<String, String> map = new HashMap();
                        map.put("fileName", fileName.substring(0, fileName.indexOf(".")) + "_" + fileName.substring(fileName.indexOf(".") + 1));
                        map.put("fullName", realPath + fileName);
                        returnList.add(map);
                    }
                }
            }
        } catch (Exception var17) {
            System.out.println("==== 此处已报错,报错信息 ===：" + var17.getMessage());
        }

        return returnList;
    }

    @RequestMapping("/gtreeTheme.do")
    public List<ZtreeNode> gtreeTheme(AeaParTheme theme){

        return themeService.gtreeTheme(theme);
    }

    @RequestMapping("/gtreeThemeForEUi.do")
    public List<ElementUiRsTreeNode> gtreeThemeForEUi(AeaParTheme theme){

        return themeService.gtreeThemeForEUi(theme);
    }
}
