package com.augurit.aplanmis.common.service.form.impl;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaExProjContract;
import com.augurit.aplanmis.common.mapper.AeaExProjContractMapper;
import com.augurit.aplanmis.common.service.form.AeaExProjContractService;
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
public class AeaExProjContractServiceImpl implements AeaExProjContractService {

    private static Logger logger = LoggerFactory.getLogger(AeaExProjContractServiceImpl.class);

    @Autowired
    private AeaExProjContractMapper aeaExProjContractMapper;

    public void saveAeaExProjContract(AeaExProjContract aeaExProjContract) throws Exception{
        aeaExProjContract.setCreater(SecurityContext.getCurrentUser().getUserName());
        aeaExProjContract.setCreateTime(new Date());
        aeaExProjContract.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaExProjContractMapper.insertAeaExProjContract(aeaExProjContract);
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
}

