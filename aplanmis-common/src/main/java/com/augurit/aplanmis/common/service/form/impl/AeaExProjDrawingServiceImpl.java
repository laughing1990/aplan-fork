package com.augurit.aplanmis.common.service.form.impl;
import com.augurit.agcloud.bpm.admin.sto.service.impl.AbstractFormDataOptManager;
import com.augurit.agcloud.bpm.common.constant.EDataOpt;
import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.domain.ActStoForminst;
import com.augurit.agcloud.bpm.common.domain.vo.FormDataOptResult;
import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.form.vo.LinkmanAddVo;
import com.augurit.aplanmis.common.mapper.AeaExProjDrawingMapper;
import com.augurit.aplanmis.common.mapper.AeaProjLinkmanMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitLinkmanMapper;
import com.augurit.aplanmis.common.service.form.AeaExProjDrawingService;
import com.augurit.aplanmis.common.service.linkman.AeaLinkmanInfoService;
import com.augurit.aplanmis.front.basis.stage.service.RestStageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
/**
* 施工图审查信息-Service服务接口实现类

*/
@Service
@Transactional
public class AeaExProjDrawingServiceImpl extends AbstractFormDataOptManager implements AeaExProjDrawingService {

    private static Logger logger = LoggerFactory.getLogger(AeaExProjDrawingServiceImpl.class);

    @Autowired
    private AeaExProjDrawingMapper aeaExProjDrawingMapper;
    @Autowired
    private RestStageService restStageService;

    @Autowired
    private AeaLinkmanInfoService aeaLinkmanInfoService;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private AeaProjLinkmanMapper aeaProjLinkmanMapper;

    public void saveAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing) throws Exception{
        aeaExProjDrawing.setCreater(SecurityContext.getCurrentUser().getUserName());
        aeaExProjDrawing.setCreateTime(new Date());
        aeaExProjDrawing.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaExProjDrawingMapper.insertAeaExProjDrawing(aeaExProjDrawing);
        if (StringUtils.isBlank(aeaExProjDrawing.getFormId())) throw new Exception("缺少formId");
        FormDataOptResult formDataOptResult =  this.formSave(aeaExProjDrawing.getFormId(), aeaExProjDrawing.getDrawingId(), EDataOpt.INSERT.getOpareteType(), null);
        //关联表单实例和申请实例
        AeaApplyinstForminst aeaApplyinstForminst = new AeaApplyinstForminst();
        aeaApplyinstForminst.setApplyinstId(aeaExProjDrawing.getRefEntityId());
        aeaApplyinstForminst.setForminstId(formDataOptResult.getActStoForminst().getStoForminstId());
        aeaApplyinstForminst.setStoFormId(aeaExProjDrawing.getFormId());
        aeaApplyinstForminst.setCreateTime(new Date());
        aeaApplyinstForminst.setCreater(SecurityContext.getCurrentUserId());
        restStageService.bindForminst(aeaApplyinstForminst);
    }
    public void updateAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing) throws Exception{
        aeaExProjDrawingMapper.updateAeaExProjDrawing(aeaExProjDrawing);
    }
    public void deleteAeaExProjDrawingById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaExProjDrawingMapper.deleteAeaExProjDrawing(id);
    }
    public PageInfo<AeaExProjDrawing> listAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaExProjDrawing> list = aeaExProjDrawingMapper.listAeaExProjDrawing(aeaExProjDrawing);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaExProjDrawing>(list);
    }
    public AeaExProjDrawing getAeaExProjDrawingById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaExProjDrawingMapper.getAeaExProjDrawingById(id);
    }
    public List<AeaExProjDrawing> listAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing) throws Exception{
        List<AeaExProjDrawing> list = aeaExProjDrawingMapper.listAeaExProjDrawing(aeaExProjDrawing);
        logger.debug("成功执行查询list！！");
        return list;
    }


    public String save(LinkmanAddVo linkmanAddVo) {
        AeaLinkmanInfo aeaLinkmanInfo = linkmanAddVo.toAeaLinkmanInfo();
        aeaLinkmanInfoService.insertAeaLinkmanInfo(aeaLinkmanInfo);
        // 企业单位关联
        if (StringUtils.isNotBlank(linkmanAddVo.getUnitInfoId())) {
            insertAeaUnitLinkman(aeaLinkmanInfo.getLinkmanInfoId(), linkmanAddVo.getUnitInfoId());
        }
        // 项目关联
        if (StringUtils.isNotBlank(linkmanAddVo.getProjInfoId())) {
            insertAeaProjLinkman(aeaLinkmanInfo.getLinkmanInfoId(), linkmanAddVo.getProjInfoId());
        }
        return aeaLinkmanInfo.getLinkmanInfoId();
    }

    private void insertAeaProjLinkman(String linkmanInfoId, String projInfoId) {
        aeaProjLinkmanMapper.insertAeaProjLinkman(buildAeaProjLinkman(linkmanInfoId, projInfoId));
    }

    private void insertAeaUnitLinkman(String linkmanInfoId, String unitInfoId) {
        try {
            aeaUnitLinkmanMapper.insertAeaUnitLinkman(buildAeaUnitLinkman(linkmanInfoId, unitInfoId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Linkman associate unit failed, linkmanInfoId: " + linkmanInfoId + ", unitInfoId: " + unitInfoId, e);
        }
    }
    private AeaUnitLinkman buildAeaUnitLinkman(String linkmanInfoId, String unitInfoId) {
        AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
        aeaUnitLinkman.setUnitLinkmanId(UuidUtil.generateUuid());
        aeaUnitLinkman.setUnitInfoId(unitInfoId);
        aeaUnitLinkman.setLinkmanInfoId(linkmanInfoId);
        aeaUnitLinkman.setCreater(SecurityContext.getCurrentUserId());
        aeaUnitLinkman.setCreateTime(new Date());
        return aeaUnitLinkman;
    }
    private AeaProjLinkman buildAeaProjLinkman(String linkmanInfoId, String projInfoId) {
        AeaProjLinkman aeaProjLinkman = new AeaProjLinkman();
        aeaProjLinkman.setProjLinkmanId(UuidUtil.generateUuid());
        aeaProjLinkman.setProjInfoId(projInfoId);
        aeaProjLinkman.setLinkmanInfoId(linkmanInfoId);
        aeaProjLinkman.setType("link");
        aeaProjLinkman.setCreater(SecurityContext.getCurrentUserId());
        aeaProjLinkman.setCreateTime(new Date());
        return aeaProjLinkman;
    }

    @Override
    public FormDataOptResult doformSave(String formId, String metaTableId, Integer opType, Object dataEntity) throws Exception {
        FormDataOptResult result = new FormDataOptResult();
        result.setSuccess(true);
        ActStoForminst actStoForminst = new ActStoForminst();
        actStoForminst.setFormId(formId);
        actStoForminst.setFormPrimaryKey(metaTableId);
        result.setActStoForminst(actStoForminst);
        result.setDataOpt(EDataOpt.INSERT);
        return result;
    }

    @Override
    public FormDataOptResult doformDelete(ActStoForm formVo, Object dataEntity) throws Exception {
        return null;
    }
}

