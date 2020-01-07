package com.augurit.aplanmis.common.service.form.impl;
import com.augurit.agcloud.bpm.admin.sto.service.impl.AbstractFormDataOptManager;
import com.augurit.agcloud.bpm.common.constant.EDataOpt;
import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.domain.ActStoForminst;
import com.augurit.agcloud.bpm.common.domain.vo.FormDataOptResult;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaApplyinstForminst;
import com.augurit.aplanmis.common.domain.AeaExProjContract;
import com.augurit.aplanmis.common.mapper.AeaExProjContractMapper;
import com.augurit.aplanmis.common.service.form.AeaExProjContractService;
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
* 合同信息-Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-10-30 14:17:17</li>
</ul>
*/
@Service
@Transactional
public class AeaExProjContractServiceImpl extends AbstractFormDataOptManager implements AeaExProjContractService {

    private static Logger logger = LoggerFactory.getLogger(AeaExProjContractServiceImpl.class);

    @Autowired
    private AeaExProjContractMapper aeaExProjContractMapper;
    @Autowired
    private RestStageService restStageService;

    public void saveAeaExProjContract(AeaExProjContract aeaExProjContract) throws Exception{
        aeaExProjContract.setCreater(SecurityContext.getCurrentUser().getUserName());
        aeaExProjContract.setCreateTime(new Date());
        aeaExProjContract.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaExProjContractMapper.insertAeaExProjContract(aeaExProjContract);
        if (StringUtils.isBlank(aeaExProjContract.getFormId())) throw new Exception("缺少formId");
        FormDataOptResult formDataOptResult = this.formSave(aeaExProjContract.getFormId(), aeaExProjContract.getContractId(), EDataOpt.INSERT.getOpareteType(), null);

        //关联表单实例和申请实例
        AeaApplyinstForminst aeaApplyinstForminst = new AeaApplyinstForminst();
        aeaApplyinstForminst.setApplyinstId(aeaExProjContract.getRefEntityId());
        aeaApplyinstForminst.setForminstId(formDataOptResult.getActStoForminst().getStoForminstId());
        aeaApplyinstForminst.setStoFormId(aeaExProjContract.getFormId());
        aeaApplyinstForminst.setCreateTime(new Date());
        aeaApplyinstForminst.setCreater(SecurityContext.getCurrentUserId());
        restStageService.bindForminst(aeaApplyinstForminst);
    }
    public void updateAeaExProjContract(AeaExProjContract aeaExProjContract) throws Exception{
        aeaExProjContract.setModifier(SecurityContext.getCurrentUser().getUserName());
        aeaExProjContract.setModifyTime(new Date());
        aeaExProjContractMapper.updateAeaExProjContract(aeaExProjContract);
    }
    public void deleteAeaExProjContractById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaExProjContractMapper.deleteAeaExProjContract(id);
    }
    public PageInfo<AeaExProjContract> listAeaExProjContract(AeaExProjContract aeaExProjContract,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaExProjContract> list = aeaExProjContractMapper.listAeaExProjContract(aeaExProjContract);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaExProjContract>(list);
    }
    public AeaExProjContract getAeaExProjContractById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaExProjContractMapper.getAeaExProjContractById(id);
    }
    public List<AeaExProjContract> listAeaExProjContract(AeaExProjContract aeaExProjContract) throws Exception{
        List<AeaExProjContract> list = aeaExProjContractMapper.listAeaExProjContract(aeaExProjContract);
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

