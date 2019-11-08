package com.augurit.aplanmis.admin.market.unit.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.unit.service.AeaImUnitRequireService;
import com.augurit.aplanmis.common.domain.AeaImItemExplain;
import com.augurit.aplanmis.common.domain.AeaImQual;
import com.augurit.aplanmis.common.domain.AeaImUnitRequire;
import com.augurit.aplanmis.common.mapper.AeaImItemExplainMapper;
import com.augurit.aplanmis.common.mapper.AeaImQualMapper;
import com.augurit.aplanmis.common.mapper.AeaImUnitRequireMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
* -Service服务接口实现类
*/
@Service
@Transactional
public class AeaImUnitRequireServiceImpl implements AeaImUnitRequireService {

    private static Logger logger = LoggerFactory.getLogger(AeaImUnitRequireServiceImpl.class);

    @Autowired
    private AeaImUnitRequireMapper aeaImUnitRequireMapper;

    @Autowired
    private AeaImQualMapper aeaImQualMapper;

    @Autowired
    private AeaImItemExplainMapper aeaImItemExplainMapper;

    @Override
    public void saveAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire) throws Exception{
        aeaImUnitRequire.setUnitRequireId(UUID.randomUUID().toString());
        aeaImUnitRequireMapper.insertAeaImUnitRequire(aeaImUnitRequire);
        String itemVerId = aeaImUnitRequire.getItemVerId();
        if(StringUtils.isNotBlank(itemVerId)){
            AeaImItemExplain explain = aeaImItemExplainMapper.getAeaImItemExplainByItemVerId(itemVerId);
            //设置关联
            if(explain != null){
                explain.setUnitRequireId(aeaImUnitRequire.getUnitRequireId());
                aeaImItemExplainMapper.updateAeaImItemExplain(explain);
            }
        }
    }
    @Override
    public void updateAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire) throws Exception{
        aeaImUnitRequireMapper.updateAeaImUnitRequire(aeaImUnitRequire);
    }
    @Override
    public void deleteAeaImUnitRequireById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaImUnitRequireMapper.deleteAeaImUnitRequire(id);
    }
    @Override
    public PageInfo<AeaImUnitRequire> listAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaImUnitRequire> list = aeaImUnitRequireMapper.listAeaImUnitRequire(aeaImUnitRequire);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaImUnitRequire>(list);
    }
    @Override
    public AeaImUnitRequire getAeaImUnitRequireById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaImUnitRequireMapper.getAeaImUnitRequireById(id);
    }
    @Override
    public List<AeaImUnitRequire> listAeaImUnitRequire(AeaImUnitRequire aeaImUnitRequire) throws Exception{
        List<AeaImUnitRequire> list = aeaImUnitRequireMapper.listAeaImUnitRequire(aeaImUnitRequire);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public AeaImUnitRequire getAeaImUnitRequireByItemVerId(String itemVerId) throws Exception {
        AeaImUnitRequire unitRequire = aeaImUnitRequireMapper.getAeaImUnitRequireByItemVerId(itemVerId);
        if(unitRequire != null && StringUtils.isNotBlank(unitRequire.getQualRequire())){
            List<String> qualIdList = new ArrayList<>();
            String[] qualIdArr = unitRequire.getQualRequire().split("\\.");
            for(String id:qualIdArr){
                if(StringUtils.isNotBlank(id)){
                    qualIdList.add(id);
                }
            }
            List<AeaImQual> aeaImQuals = aeaImQualMapper.listAeaImQualByQualIds(qualIdList.toArray(new String[qualIdList.size()]));
            if(aeaImQuals != null && aeaImQuals.size() > 0){
                StringBuilder sb = new StringBuilder();
                for(int i=0,len=aeaImQuals.size();i<len;i++){
                    sb.append("、").append(aeaImQuals.get(i).getQualName());
                }
                if(sb.length() > 0){
                    unitRequire.setQualRequireName(sb.toString().substring(1));
                }
            }
        }
        return unitRequire;
    }
}

