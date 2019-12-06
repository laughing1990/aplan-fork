package com.augurit.aplanmis.common.service.item.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaToleranceTimeInst;
import com.augurit.aplanmis.common.mapper.AeaToleranceTimeInstMapper;
import com.augurit.aplanmis.common.service.item.AeaToleranceTimeInstService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class AeaToleranceTimeInstServiceImpl implements AeaToleranceTimeInstService {

    private static Logger logger = LoggerFactory.getLogger(AeaToleranceTimeInstServiceImpl.class);

    @Autowired
    private AeaToleranceTimeInstMapper aeaToleranceTimeInstMapper;

    public void saveAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception{
        aeaToleranceTimeInst.setCreater(SecurityContext.getCurrentUserName());
        aeaToleranceTimeInst.setCreateTime(new Date());
        aeaToleranceTimeInstMapper.insertAeaToleranceTimeInst(aeaToleranceTimeInst);
    }
    public void updateAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception{
        aeaToleranceTimeInst.setModifier(SecurityContext.getCurrentUserName());
        aeaToleranceTimeInst.setModifyTime(new Date());
        aeaToleranceTimeInstMapper.updateAeaToleranceTimeInst(aeaToleranceTimeInst);
    }
    public void deleteAeaToleranceTimeInstById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaToleranceTimeInstMapper.deleteAeaToleranceTimeInst(id);
    }
    public PageInfo<AeaToleranceTimeInst> listAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst,Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaToleranceTimeInst> list = aeaToleranceTimeInstMapper.listAeaToleranceTimeInst(aeaToleranceTimeInst);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaToleranceTimeInst>(list);
    }
    public AeaToleranceTimeInst getAeaToleranceTimeInstById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaToleranceTimeInstMapper.getAeaToleranceTimeInstById(id);
    }
    public List<AeaToleranceTimeInst> listAeaToleranceTimeInst(AeaToleranceTimeInst aeaToleranceTimeInst) throws Exception{
        List<AeaToleranceTimeInst> list = aeaToleranceTimeInstMapper.listAeaToleranceTimeInst(aeaToleranceTimeInst);
        logger.debug("成功执行查询list！！");
        return list;
    }

    /**
     * 当办件容缺的材料补正完成且审批确认通过后，可调用此接口，结束容缺时限实例的计时
     * @param iteminstId 事项实例id
     * @throws Exception
     */
    @Override
    public void completeAeaToleranceTimeinst(String iteminstId) throws Exception {
        AeaToleranceTimeInst query = new AeaToleranceTimeInst();
        query.setOrgId(SecurityContext.getCurrentOrgId());
        query.setIsCompleted("0");
        query.setIteminstId(iteminstId);
        List<AeaToleranceTimeInst> aeaToleranceTimeInsts = aeaToleranceTimeInstMapper.listAeaToleranceTimeInst(query);
        for(int i=0,len=aeaToleranceTimeInsts.size(); i<len; i++){
            AeaToleranceTimeInst temp = new AeaToleranceTimeInst();
            temp.setToleranceTimeInstId(aeaToleranceTimeInsts.get(i).getToleranceTimeInstId());
            temp.setIsCompleted("1");
            updateAeaToleranceTimeInst(temp);
        }
    }
}

