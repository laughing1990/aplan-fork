package com.augurit.aplanmis.admin.market.buscert.controller;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.buscert.service.AeaBusCertService;
import com.augurit.aplanmis.admin.market.constants.ConfigTableInfo;
import com.augurit.aplanmis.common.domain.AeaBusCert;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
* -Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/bus/cert")
public class AeaBusCertController {

private static Logger logger = LoggerFactory.getLogger(AeaBusCertController.class);

    @Autowired
    private AeaBusCertService aeaBusCertService;

    @RequestMapping("/indexAeaBusCert.do")
    public ModelAndView indexAeaBusCert(AeaBusCert aeaBusCert, String infoType){
        return new ModelAndView("ui-jsp/cert/cert_index");
    }

    /**
     * 分页查询服务事项配置证书列表数据
     * @param aeaCert
     * @param page
     * @return
     */
    @RequestMapping("/listItemBasicCert.do")
    public EasyuiPageInfo<AeaCert> listItemBasicCert(AeaCert aeaCert, Page page) throws Exception{
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaCert);
        PageInfo<AeaCert>  pageInfo = aeaBusCertService.listAeaCert(aeaCert,page, ConfigTableInfo.ITEM_BASIC_TABLE_NAME);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    /**
     * 分页查询资质配置证书
     * @param aeaCert
     * @param page
     * @return
     */
    @RequestMapping("/listQualCert.do")
    public EasyuiPageInfo<AeaCert> listQualCert(AeaCert aeaCert, Page page) throws Exception{
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaCert);
        PageInfo<AeaCert>  pageInfo = aeaBusCertService.listAeaCert(aeaCert,page, ConfigTableInfo.QUAL_TABLE_NAME);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/getAeaBusCert.do")
    public AeaBusCert getAeaBusCert(String id) throws Exception {
        if (id != null){
            logger.debug("根据ID获取AeaBusCert对象，ID为：{}", id);
            return aeaBusCertService.getAeaBusCertById(id);
        }
        else {
            logger.debug("构建新的AeaBusCert对象");
            return new AeaBusCert();
        }
    }

    @RequestMapping("/updateAeaBusCert.do")
        public ResultForm updateAeaBusCert(AeaBusCert aeaBusCert) throws Exception {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaBusCert);
        aeaBusCertService.updateAeaBusCert(aeaBusCert);
        return new ResultForm(true);
    }


    /**
    * 保存关联关系
    * @param aeaBusCert
    * @param result 校验对象
    * @return 返回结果对象 包含结果信息
    * @throws Exception
    */
    @RequestMapping("/saveAeaBusCert.do")
    public ResultForm saveAeaBusCert(AeaBusCert aeaBusCert, BindingResult result) throws Exception {
        if(result.hasErrors()) {
            logger.error("保存Form对象出错");
            throw new InvalidParameterException(aeaBusCert);
        }
        ResultForm resultForm = new ResultForm(false);
        String[] saveCertIds = aeaBusCert.getSaveCertIds();
        String[] cancelCertIds = aeaBusCert.getCancelCertIds();
        if(StringUtils.isNotBlank(aeaBusCert.getBusRecordId())&&(saveCertIds != null||cancelCertIds != null)){
            aeaBusCertService.saveRelation(saveCertIds,aeaBusCert.getBusRecordId(),aeaBusCert.getBusTableName());
            aeaBusCertService.cancelRelation(cancelCertIds,aeaBusCert.getBusRecordId(),aeaBusCert.getBusTableName());
            resultForm.setSuccess(true);
        }
        return resultForm;
    }

    /**
     * 查询已关联的证书
     * @param aeaBusCert
     * @return
     * @throws Exception
     */
    @RequestMapping("/getSavedCert.do")
    public ResultForm getSavedCert(AeaBusCert aeaBusCert) throws Exception{
        ContentResultForm<List> resultForm = aeaBusCertService.getSavedCert(aeaBusCert,aeaBusCert.getBusTableName());
        return resultForm;
    }

}
