package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.dic.code.service.BscDicCodeService;
import com.augurit.agcloud.framework.page.PageParam;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParThemeSample;
import com.augurit.aplanmis.common.service.admin.par.AeaParThemeSampleAdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhangXinhui
 * @date 2019/9/11 011 14:29
 * @desc
 **/
@RestController
@RequestMapping("/aea/par/themeSample")
public class AeaParThemeSampleAdminController {

    @Autowired
    private AeaParThemeSampleAdminService aeaParThemeSampleAdminService;
    @Autowired
    private BscDicCodeService bscDicCodeService;

    @RequestMapping(value = "/listAeaParThemeSamplePage.do", method = {RequestMethod.POST, RequestMethod.GET})
    public EasyuiPageInfo<AeaParThemeSample> listAeaParThemeSamplePage(AeaParThemeSample aeaParThemeSample,
                                                                       @ModelAttribute PageParam page) {

        PageInfo<AeaParThemeSample> pageInfo = aeaParThemeSampleAdminService
                .listAeaParThemeSamplePage(aeaParThemeSample, page.convertPage());

        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping(value = "/listSampleType.do", method = {RequestMethod.POST, RequestMethod.GET})
    public List<BscDicCodeItem> listSampleType() {

        return bscDicCodeService.getActiveItemsByTypeCode("AEA_PAR_THEME_SAMPLE_TYPE", SecurityContext.getCurrentOrgId());
    }

    @RequestMapping(value = "/saveOrUpdateAeaParThemeSample.do")
    public ResultForm saveOrUpdateAeaParThemeSample(AeaParThemeSample aeaParThemeSample) {

        if (StringUtils.isNotBlank(aeaParThemeSample.getThemeSampleId())) {
            aeaParThemeSampleAdminService.updateThemeSample(aeaParThemeSample);
        } else {
            aeaParThemeSampleAdminService.saveThemeSample(aeaParThemeSample);
        }
        return new ResultForm(true);
    }

    @RequestMapping(value = "/getAeaParThemeSampleById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm getAeaParThemeSampleById(String id) {
        if (StringUtils.isBlank(id)) {
            return new ResultForm(false);
        }
        AeaParThemeSample aeaParThemeSample = aeaParThemeSampleAdminService.getAeaParThemeSampleById(id);
        return new ContentResultForm<AeaParThemeSample>(aeaParThemeSample != null, aeaParThemeSample);
    }

    @RequestMapping(value = "/delAeaParThemeSampleById.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm delAeaParThemeSampleById(String id){
        if(StringUtils.isBlank(id)){
            return new ResultForm(false);
        }
        aeaParThemeSampleAdminService.deleteAeaParThemeSampleById(id);
        return new ResultForm(true);
    }

    @RequestMapping(value = "/batchDelThemeSampleByIds.do", method = {RequestMethod.POST, RequestMethod.GET})
    public ResultForm batchDelThemeSampleByIds(String[] ids){

        aeaParThemeSampleAdminService.batchDelThemeSampleByIds(ids);
        return new ResultForm(true);
    }

}
