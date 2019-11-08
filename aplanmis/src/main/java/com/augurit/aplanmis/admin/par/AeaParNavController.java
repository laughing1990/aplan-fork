package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaParNav;
import com.augurit.aplanmis.common.service.admin.par.AeaParNavAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/8/29 029 16:41
 * @desc
 **/
@RestController
@RequestMapping("/aea/par/nav")
public class AeaParNavController {

    private static final Logger log = LoggerFactory.getLogger(AeaParNavController.class);

    @Autowired
    private AeaParNavAdminService aeaParNavService;

    @RequestMapping("/navIndex.do")
    public ModelAndView navIndex() {

        return new ModelAndView("ui-jsp/aplanmis/par/nav/navIndex");
    }

    @RequestMapping("/listAeaParNavPage.do")
    public EasyuiPageInfo<AeaParNav> findAeaParNavPage(AeaParNav aeaParNav, Page page) {

        aeaParNav.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageInfo res = aeaParNavService.listAeaParNavPage(aeaParNav, page);
        return PageHelper.toEasyuiPageInfo(res);
    }

    @RequestMapping("/listAeaParNavByNoPage.do")
    public List<AeaParNav> listAeaParNavByNoPage(AeaParNav aeaParNav) {

        aeaParNav.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaParNav.setIsActive(ActiveStatus.ACTIVE.getValue());
        return aeaParNavService.listAeaParNavPage(aeaParNav);
    }

    @RequestMapping("/getMaxSortNo.do")
    public long getMaxSortNo() {

        return aeaParNavService.getMaxSortNo(SecurityContext.getCurrentOrgId());
    }

    @RequestMapping("/getAeaParNavById.do")
    public ContentResultForm<AeaParNav> getAeaParNavById(String id) {

        if (StringUtils.isBlank(id)) {
            return new ContentResultForm(false);
        }
        AeaParNav aeaParNav = aeaParNavService.getAeaParNavById(id, SecurityContext.getCurrentOrgId());
        if (aeaParNav == null) {
            return new ContentResultForm(false);
        }
        return new ContentResultForm<AeaParNav>(true, aeaParNav);
    }

    @RequestMapping("/saveOrUpdateAeaParNav.do")
    public ResultForm saveOrUpdateAeaParNav(AeaParNav aeaParNav) {

        if (StringUtils.isBlank(aeaParNav.getNavId())) {
            aeaParNav.setNavId(UuidUtil.generateUuid());
            aeaParNavService.insertAeaParNav(aeaParNav);
        } else {
            aeaParNavService.updateAeaParNav(aeaParNav);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/deleteAeaParNavById.do")
    public ResultForm deleteAeaParNavById(String id) {

        if (StringUtils.isBlank(id)) {
            return new ContentResultForm(false);
        }
        aeaParNavService.deleteAeaParNav(id, SecurityContext.getCurrentOrgId());
        return new ContentResultForm(true);
    }

    @RequestMapping("/batchDelAeaParNavByIds.do")
    public ResultForm batchDelAeaParNavByIds(String[] ids) {

        if(ids==null){
          throw  new InvalidParameterException("参数ids为空!");
        }
        if(ids!=null&&ids.length==0){
           throw  new InvalidParameterException("参数ids为空!");
        }
        aeaParNavService.batchDelAeaParNavByIds(ids, SecurityContext.getCurrentOrgId());
        return new ContentResultForm(true);
    }

    @RequestMapping("/changIsActiveState.do")
    public ResultForm changIsActiveState(String navId) {

        if (StringUtils.isBlank(navId)) {
           throw new InvalidParameterException("参数navId为空!");
        }
        aeaParNavService.changIsActiveState(navId, SecurityContext.getCurrentOrgId());
        return new ContentResultForm(true);
    }
}
