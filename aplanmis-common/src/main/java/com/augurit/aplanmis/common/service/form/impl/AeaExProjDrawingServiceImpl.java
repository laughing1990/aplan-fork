package com.augurit.aplanmis.common.service.form.impl;
import com.augurit.agcloud.bpm.admin.sto.service.impl.AbstractFormDataOptManager;
import com.augurit.agcloud.bpm.common.constant.EDataOpt;
import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.domain.ActStoForminst;
import com.augurit.agcloud.bpm.common.domain.vo.FormDataOptResult;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaExProjDrawing;
import com.augurit.aplanmis.common.mapper.AeaExProjDrawingMapper;
import com.augurit.aplanmis.common.service.form.AeaExProjDrawingService;
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

    public void saveAeaExProjDrawing(AeaExProjDrawing aeaExProjDrawing) throws Exception{
        aeaExProjDrawing.setCreater(SecurityContext.getCurrentUser().getUserName());
        aeaExProjDrawing.setCreateTime(new Date());
        aeaExProjDrawing.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaExProjDrawingMapper.insertAeaExProjDrawing(aeaExProjDrawing);
        if (StringUtils.isBlank(aeaExProjDrawing.getFormId())) throw new Exception("缺少formId");
        this.formSave(aeaExProjDrawing.getFormId(), aeaExProjDrawing.getDrawingId(), EDataOpt.INSERT.getOpareteType(), null);
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

