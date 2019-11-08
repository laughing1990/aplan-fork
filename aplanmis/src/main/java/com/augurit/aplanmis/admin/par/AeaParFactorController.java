package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaParFactor;
import com.augurit.aplanmis.common.service.admin.par.AeaParFactorAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/8/30 030 19:53
 * @desc
 **/
@RestController
@RequestMapping("/aea/par/factor")
public class AeaParFactorController {
    private static final Logger log = LoggerFactory.getLogger(AeaParFactorController.class);
    @Autowired
    private AeaParFactorAdminService aeaParFactorService;

    @RequestMapping("/listAeaParFactorPage.do")
    public EasyuiPageInfo<AeaParFactor> findAeaParFactorPage(AeaParFactor aeaParFactor, Page page) {

        aeaParFactor.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageInfo pageInfo = aeaParFactorService.listAeaParFactorPage(aeaParFactor, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaParFactorByNoPage.do")
    public List<AeaParFactor> listAeaParFactorByNoPage(AeaParFactor aeaParFactor) {

        aeaParFactor.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaParFactorService.listAeaParFactor(aeaParFactor);
    }

    @RequestMapping("/gtreeAeaParFactor.do")
    public List<AeaParFactor> gtreeAeaParFactor(AeaParFactor aeaParFactor) {

        aeaParFactor.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaParFactorService.gtreeAeaParFactor(aeaParFactor);
    }

    @RequestMapping("/getMaxSortNo.do")
    public Long getMaxSortNo(String navId) {

        return aeaParFactorService.getMaxSortNo(navId, SecurityContext.getCurrentOrgId());
    }

    @RequestMapping("/getAeaParFactorWithThemeById.do")
    public ResultForm getAeaParFactorWithThemeById(String id)throws Exception {

        AeaParFactor res = aeaParFactorService.getAeaParFactorWithThemeById(id, SecurityContext.getCurrentOrgId());
        return new ContentResultForm<AeaParFactor>(res != null, res);
    }

    @RequestMapping("/deleteAeaParFactorById.do")
    public ResultForm deleteAeaParFactorById(String id) {

        if (StringUtils.isBlank(id)) {
           throw new InvalidParameterException("参数id为空!");
        }
        aeaParFactorService.deleteAeaParFactorById(id, SecurityContext.getCurrentOrgId());
        return new ResultForm(true);
    }

    @RequestMapping("/batchDelAeaParFactorByIds.do")
    public ResultForm batchDelAeaParFactorByIds(String[] ids) {

        if(ids==null){
            throw new InvalidParameterException("参数ids为空!");
        }
        if(ids!=null&&ids.length==0){
            throw new InvalidParameterException("参数ids为空!");
        }
        aeaParFactorService.batchDelAeaParFactorByIds(ids, SecurityContext.getCurrentOrgId());
        return new ResultForm(true);
    }

    @RequestMapping("/saveOrUpdateAeaParFactor.do")
    public ResultForm saveOrUpdateAeaParFactor(AeaParFactor aeaParFactor) {

        if (StringUtils.isBlank(aeaParFactor.getFactorId())) {

            aeaParFactor.setFactorId(UuidUtil.generateUuid());
            aeaParFactorService.saveAeaParFactor(aeaParFactor);
        } else {

            aeaParFactorService.updateAeaParFactor(aeaParFactor);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/unbindTheme.do")
    public ResultForm unbindTheme(String factorId){

        if(StringUtils.isBlank(factorId)){
            return new ResultForm(false);
        }
        aeaParFactorService.unbindTheme(factorId);
        return new ResultForm(true);
    }

}
