package com.augurit.aplanmis.common.service.form.impl;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaExProjCertLand;
import com.augurit.aplanmis.common.mapper.AeaExProjCertLandMapper;
import com.augurit.aplanmis.common.service.form.AeaExProjCertLandService;
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
* 建设项目用地规划许可证-Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-10-29 19:48:12</li>
</ul>
*/
@Service
@Transactional
public class AeaExProjCertLandServiceImpl implements AeaExProjCertLandService {

    private static Logger logger = LoggerFactory.getLogger(AeaExProjCertLandServiceImpl.class);

    @Autowired
    private AeaExProjCertLandMapper aeaExProjCertLandMapper;

    public void saveAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand) throws Exception{
        aeaExProjCertLand.setCreater(SecurityContext.getCurrentUser().getUserName());
        aeaExProjCertLand.setCreateTime(new Date());
        aeaExProjCertLand.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaExProjCertLandMapper.insertAeaExProjCertLand(aeaExProjCertLand);
    }
    public void updateAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand) throws Exception{
        aeaExProjCertLand.setModifier(SecurityContext.getCurrentUser().getUserName());
        aeaExProjCertLand.setModifyTime(new Date());
        aeaExProjCertLandMapper.updateAeaExProjCertLand(aeaExProjCertLand);
    }
    public void deleteAeaExProjCertLandById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaExProjCertLandMapper.deleteAeaExProjCertLand(id);
    }
    public PageInfo<AeaExProjCertLand> listAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaExProjCertLand> list = aeaExProjCertLandMapper.listAeaExProjCertLand(aeaExProjCertLand);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaExProjCertLand>(list);
    }
    public AeaExProjCertLand getAeaExProjCertLandById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaExProjCertLandMapper.getAeaExProjCertLandById(id);
    }
    public List<AeaExProjCertLand> listAeaExProjCertLand(AeaExProjCertLand aeaExProjCertLand) throws Exception{
        List<AeaExProjCertLand> list = aeaExProjCertLandMapper.listAeaExProjCertLand(aeaExProjCertLand);
        logger.debug("成功执行查询list！！");
        return list;
    }
}

