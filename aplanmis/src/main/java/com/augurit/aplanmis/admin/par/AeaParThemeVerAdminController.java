package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.constants.PublishStatus;
import com.augurit.aplanmis.common.domain.AeaParThemeVer;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeVerAdminService;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主题定义版本表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/theme/ver")
public class AeaParThemeVerAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaParThemeVerAdminController.class);

    @Autowired
    private AeaParThemeVerAdminService aeaParThemeVerAdminService;

    @Autowired
    private BscDicCodeService bscDicCodeService;

    @Autowired
    private FileUtilsService fileUtilsService;

    @RequestMapping("/listAeaParThemeVerNoPage.do")
    public List<AeaParThemeVer> listAeaParThemeVerNoPage(AeaParThemeVer aeaParThemeVer) {

        if(aeaParThemeVer==null){
            aeaParThemeVer = new AeaParThemeVer();
        }
        aeaParThemeVer.setRootOrgId(SecurityContext.getCurrentOrgId());
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParThemeVer);
        return aeaParThemeVerAdminService.listAeaParThemeVer(aeaParThemeVer);
    }

    /**
     * 获取主题下的所有版本定义数据
     *
     * @param themeId
     * @return
     */
    @RequestMapping("/listThemeVerSyncZTree.do")
    public List<AeaParThemeVer> listThemeVerSyncZTree(String themeId) {

        if (StringUtils.isNotBlank(themeId)) {
            return aeaParThemeVerAdminService.listThemeVerSyncZTree(themeId);
        }
        return null;
    }

    @RequestMapping("/unpublished/num.do")
    public ResultForm getUnpublishedNum(String themeId) {

        if(StringUtils.isBlank(themeId)){
            throw new IllegalArgumentException("主题themeId为空!");
        }
        AeaParThemeVer ver = new AeaParThemeVer();
        ver.setThemeId(themeId);
        ver.setRootOrgId(SecurityContext.getCurrentOrgId());
        ver.setIsPublish(PublishStatus.UNPUBLISHED.getValue());
        ver.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
        List<AeaParThemeVer> vers = aeaParThemeVerAdminService.listAeaParThemeVer(ver);
        if(vers!=null&&vers.size()>0){
            return new ResultForm(false,"存在最新未发布版本");
        }else{
            return new ResultForm(true, "不存在最新未发布版本!");
        }
    }

    @RequestMapping("/copy/version.do")
    public ResultForm copyOnePublishedOrTestRunVer(String themeId, String themeVerId)throws Exception {

        if(StringUtils.isBlank(themeId)){
            throw new IllegalArgumentException("主题themeId为空！");
        }
        if(StringUtils.isBlank(themeVerId)){
            throw new IllegalArgumentException("主题版本themeVerId为空！");
        }
        AeaParThemeVer newThemeVer = aeaParThemeVerAdminService.copyThemeVerRelData(themeId, themeVerId);
        logger.debug("复制成功,新版本对象", newThemeVer);
        return new ContentResultForm<AeaParThemeVer>(true, newThemeVer, "复制成功!");
    }

    @RequestMapping("/testRunOrPublished.do")
    public ResultForm testRunOrPublished(String themeId, String themeVerId, Double verNum, String type) {

        if(StringUtils.isBlank(themeId)){
            throw new IllegalArgumentException("主题themeId为空！");
        }
        if(StringUtils.isBlank(themeVerId)){
            throw new IllegalArgumentException("主题版本themeVerId为空！");
        }
        Assert.notNull(verNum, "主题版本序号verNum为空!");
        if(StringUtils.isBlank(type)){
            throw new IllegalArgumentException("当前操作不明确！");
        }
        if(!(type.equals("2")||type.equals("1"))){
            throw new IllegalArgumentException("当前操作不明确,可以是试运行或者发布！");
        }
        aeaParThemeVerAdminService.testRunOrPublished(themeId, themeVerId, verNum, type);
        return new ResultForm(true, "操作成功！");
    }

    @RequestMapping("/gtreeOkThemeRelStage.do")
    public List<ZtreeNode> gtreeOkThemeRelStage() {

        String rootOrgId = SecurityContext.getCurrentOrgId();
        return aeaParThemeVerAdminService.gtreeOkThemeRelStage(rootOrgId);
    }

    /**
     *
     * 删除主题附件
     *
     * @param detailId
     * @return
     */
    @RequestMapping("/delRelAtt.do")
    public ResultForm delRelAtt(String detailId) throws Exception {

        if(StringUtils.isBlank(detailId)){
            throw new IllegalArgumentException("附件detailId为空！");
        }
        fileUtilsService.deleteAttachment(detailId);

        return new ResultForm(true);
    }

    @RequestMapping("/delRelAtts.do")
    public ResultForm delRelAtts(String[] detailIds) throws Exception {

        if(detailIds!=null&&detailIds.length>0){
            for(String detailId:detailIds){
                if(StringUtils.isNotBlank(detailId)){
                    fileUtilsService.deleteAttachment(detailId);
                }
            }
            return new ResultForm(true);
        }
        return new ResultForm(false,"附件detailIds为空！");
    }

    /**
     * 处理主题版本附件
     *
     * @param themeVerId
     * @return
     * @throws Exception
     */
    @RequestMapping("/handleThemeVerAtt.do")
    public ResultForm handleThemeVerAtt(String themeVerId, HttpServletRequest request) throws Exception {

        if(StringUtils.isBlank(themeVerId)){
            throw new InvalidParameterException("参数themeVerId为空!");
        }
        aeaParThemeVerAdminService.handleThemeVerAtt(themeVerId, request);
        return new ResultForm(true);
    }

    /**
     * 保存主题版本拖拉拽视图
     *
     * @param themeVerId 主题版本id
     * @param themeVerDiagram  视图json
     * @return
     */
    @RequestMapping("/saveThemeVerDiagram.do")
    public ResultForm saveThemeVerDiagram(String themeVerId, String themeVerDiagram)throws Exception{

        aeaParThemeVerAdminService.saveThemeVerDiagram(themeVerId, themeVerDiagram);
        return new ResultForm(true);
    }

    @RequestMapping("/createBpmnDiagram.do")
    public ResultForm createBpmnDiagram(String themeVerId)throws Exception{

        return aeaParThemeVerAdminService.createBpmnDiagram2(themeVerId);
    }

    /**
     * 展示主题版本在线运行图页面
     *
     * @param themeVerId
     * @return
     * @throws Exception
     */
    @RequestMapping("/showThemeVerDiagram.do")
    public ModelAndView showThemeVerDiagram(ModelMap modelMap, String themeVerId)throws Exception{

        boolean curIsEditable = false;
        if(themeVerId!=null) {
            AeaParThemeVer ver = aeaParThemeVerAdminService.getAeaParThemeVerById(themeVerId);
            if (ver != null && ver.isEditable()) {
                curIsEditable = true;
            }
            ModelAndView view = new ModelAndView("ui-jsp/theme/rappid");
            view.addObject("themeVerId", themeVerId);
            view.addObject("curIsEditable", curIsEditable);
            List<BscDicCodeItem> dueUnitType = bscDicCodeService.getActiveItemsByTypeCode("DUE_UNIT_TYPE", SecurityContext.getCurrentOrgId());
            Map unitMap = null;
            if(dueUnitType != null && dueUnitType.size() > 0){
                unitMap = new HashMap<>(dueUnitType.size());
                for(BscDicCodeItem item: dueUnitType){
                    unitMap.put(item.getItemCode(), item.getItemName());
                }
            }
            view.addObject("dueUnit", dueUnitType);
            view.addObject("dueUnitMap", unitMap);
            AeaParThemeVer aeaParThemeVer = aeaParThemeVerAdminService.getAeaParThemeVerById(themeVerId);
            view.addObject("themeVerDiagram", aeaParThemeVer.getThemeVerDiagram());
            // 对应标准审批阶段序号
            List<BscDicCodeItem> dybzspjdxhs = bscDicCodeService.getActiveItemsByTypeCode("GJBZSPJD", SecurityContext.getCurrentOrgId());
            modelMap.put("dybzspjdxhs", dybzspjdxhs);
            return view;
        }else{
            throw new InvalidParameterException("无法获取阶段数据!");
        }
    }
    @RequestMapping("/showThemeVerDiagram.do1")
    public ModelAndView showThemeVerDiagram1(ModelMap modelMap, String themeVerId)throws Exception{

        boolean curIsEditable = false;
        if(themeVerId!=null) {
            AeaParThemeVer ver = aeaParThemeVerAdminService.getAeaParThemeVerById(themeVerId);
            if (ver != null && ver.isEditable()) {
                curIsEditable = true;
            }
            ModelAndView view = new ModelAndView("ui-jsp/theme/rappid_proj_view");
            view.addObject("themeVerId", themeVerId);
            view.addObject("curIsEditable", curIsEditable);
            List<BscDicCodeItem> dueUnitType = bscDicCodeService.getActiveItemsByTypeCode("DUE_UNIT_TYPE", SecurityContext.getCurrentOrgId());
            view.addObject("dueUnit", dueUnitType);
            AeaParThemeVer aeaParThemeVer = aeaParThemeVerAdminService.getAeaParThemeVerById(themeVerId);
            view.addObject("themeVerDiagram", aeaParThemeVer.getThemeVerDiagram());
            return view;
        }else{
            throw new InvalidParameterException("无法获取阶段数据!");
        }
    }
}
