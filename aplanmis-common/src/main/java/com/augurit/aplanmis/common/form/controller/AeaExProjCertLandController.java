package com.augurit.aplanmis.common.form.controller;


import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.form.service.AeaExProjCertBuildService;
import com.augurit.aplanmis.common.service.form.AeaExProjCertLandService;
import com.augurit.aplanmis.common.service.form.impl.AeaExCertiService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.vo.AeaCertiVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 两证一书 -Controller 页面控制转发类
 */
@RestController
@RequestMapping("/rest/form/tceop")
public class AeaExProjCertLandController {

    private static Logger logger = LoggerFactory.getLogger(AeaExProjCertLandController.class);

    @Autowired
    private AeaExProjCertLandService aeaExProjCertLandService;


    @Autowired
    private AeaExCertiService aeaExCertiService;

    @Autowired
    private AeaProjInfoService aeaProjInfoService;

    @Autowired
    private AeaExProjCertBuildService aeaExProjCertBuildService;


    @RequestMapping("/index.html")
    public ModelAndView indexAeaExProjCertLand(String projInfoId,String formId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("projInfoId", projInfoId);
        modelAndView.addObject("formId", formId);
        modelAndView.setViewName("form/cardBookForm");
        return modelAndView;
    }

    //数据回显
    @RequestMapping("/getTceop.do")
    public ResultForm getAeaExProjCertLand(String projInfoId) throws Exception {
        if (projInfoId == null || "".equals(projInfoId)) {
            return new ResultForm(false, "获取项目信息失败，项目id " + projInfoId);
        }

        AeaProjInfo aeaProjInfoByProjInfoId = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if (aeaProjInfoByProjInfoId == null) {
            return new ResultForm(false, "获取项目信息失败，项目id " + projInfoId);
        }

        AeaCertiVo aeaCertiVo = aeaExCertiService.index(projInfoId);

        return new ContentResultForm<AeaCertiVo>(true, aeaCertiVo);


    }


    /**
     * 保存或编辑建设项目用地规划许可证
     *
     * @param aeaCertiVo 建设项目用地规划许可证
     */
    @RequestMapping("/saveTceop.do")
    public ResultForm saveAeaExProjCertLand(AeaCertiVo aeaCertiVo) throws Exception {

        if (aeaCertiVo.getProjInfoId() == null || "".equals(aeaCertiVo.getProjInfoId())) {
            return new ResultForm(false, "获取项目信息失败，项目id为空 " + aeaCertiVo.getProjInfoId());
        }
        AeaProjInfo aeaProjInfoByProjInfoId = aeaProjInfoService.getAeaProjInfoByProjInfoId(aeaCertiVo.getProjInfoId());
        if (aeaProjInfoByProjInfoId == null) {
            return new ResultForm(false, "获取项目信息失败，项目id " + aeaCertiVo.getProjInfoId());
        }
        aeaExCertiService.save(aeaCertiVo);
        return new ContentResultForm<AeaCertiVo>(true, aeaCertiVo);
    }

    @RequestMapping("/deleteAeaExProjCertLandById.do")
    public ResultForm deleteAeaExProjCertLandById(String id) throws Exception {
        logger.debug("删除建设项目用地规划许可证Form对象，对象id为：{}", id);
        if (id != null)
            aeaExProjCertLandService.deleteAeaExProjCertLandById(id);
        return new ResultForm(true);
    }

}
