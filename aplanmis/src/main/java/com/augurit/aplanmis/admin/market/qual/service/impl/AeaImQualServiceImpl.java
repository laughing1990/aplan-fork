package com.augurit.aplanmis.admin.market.qual.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.qual.service.AeaImQualService;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.front.constant.CommonConstant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* -Service服务接口实现类
*/
@Service
@Transactional
public class AeaImQualServiceImpl implements AeaImQualService {

    private static Logger logger = LoggerFactory.getLogger(AeaImQualServiceImpl.class);

    @Autowired
    private AeaImQualMapper aeaImQualMapper;

    @Autowired
    private AeaImServiceMajorMapper aeaImServiceMajorMapper;

    @Autowired
    private AeaImQualLevelMapper aeaImQualLevelMapper;

    @Autowired
    private AeaImItemExplainMapper aeaImItemExplainMapper;

    @Autowired
    private AeaImUnitRequireMapper aeaImUnitRequireMapper;

    @Override
    public void saveAeaImQual(AeaImQual aeaImQual) throws Exception{
        aeaImQual.setQualId(UUID.randomUUID().toString());
        aeaImQual.setCreater(SecurityContext.getCurrentUserName());
        aeaImQual.setCreateTime(new Date());
        aeaImQual.setIsDelete("0");
        aeaImQualMapper.insertAeaImQual(aeaImQual);
        //系统自动创建对应资质的专业类别根节点
        AeaImServiceMajor aeaImServiceMajor = new AeaImServiceMajor();
        aeaImServiceMajor.setMajorId(UUID.randomUUID().toString());
        aeaImServiceMajor.setMajorName(aeaImQual.getQualName());
        aeaImServiceMajor.setCreater(SecurityContext.getCurrentUserName());
        aeaImServiceMajor.setCreateTime(new Date());
        aeaImServiceMajor.setIsDelete("0");
        String seq = CommonConstant.SEQ_SEPARATOR + aeaImServiceMajor.getMajorId() + CommonConstant.SEQ_SEPARATOR ;
        aeaImServiceMajor.setMajorSeq(seq);
        aeaImServiceMajor.setMemo("system auto create major root node!");
        aeaImServiceMajor.setQualId(aeaImQual.getQualId());
        aeaImServiceMajorMapper.insertAeaImServiceMajor(aeaImServiceMajor);
    }
    @Override
    public void updateAeaImQual(AeaImQual aeaImQual) throws Exception{
        AeaImServiceMajor rootMajor = aeaImServiceMajorMapper.getAeaImServiceMajorRootNodeByQualId(aeaImQual.getQualId());
        aeaImQualMapper.updateAeaImQual(aeaImQual);
        if(!aeaImQual.getQualName().equals(rootMajor.getMajorName())){
            //修改专业根节点名称与资质名称一致
            rootMajor.setMajorName(aeaImQual.getQualName());
            aeaImServiceMajorMapper.updateAeaImServiceMajor(rootMajor);
        }
    }
    @Override
    public void deleteAeaImQualById(String id) throws Exception{
        if(id == null){
            throw new InvalidParameterException(id);
        }
        aeaImQualMapper.deleteAeaImQual(id);
        //关联删除对应的专业树
        aeaImServiceMajorMapper.deleteAeaImServiceMajorTreeByQualId(id);
    }
    @Override
    public PageInfo<AeaImQual> listAeaImQual(AeaImQual aeaImQual, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaImQual> list = aeaImQualMapper.listAeaImQual(aeaImQual);
        logger.debug("成功执行分页查询！！");
        setIsCheckByItemVerId(aeaImQual, list);
        return new PageInfo<AeaImQual>(list);
    }

    private void setIsCheckByItemVerId(AeaImQual aeaImQual, List<AeaImQual> list) throws Exception {
        if(list != null && list.size() > 0 && aeaImQual != null && StringUtils.isNotBlank(aeaImQual.getItemVerId())){
            List<String> qualIdList = this.queryQualIdListByItemVerId(aeaImQual.getItemVerId());
            for(int i=0,len=list.size(); i<len; i++){
                AeaImQual qual = list.get(i);
                if(qualIdList.contains(qual.getQualId())){
                    qual.setIscheck(1);
                }
            }
        }
    }

    private  List<String>  queryQualIdListByItemVerId(String itemVerId) throws Exception {
        List<String> qualIdList = new ArrayList<>();
        if(StringUtils.isNotBlank(itemVerId)){
            //查询中介机构资质要求的资质
            AeaImItemExplain aeaImItemExplain = aeaImItemExplainMapper.getAeaImItemExplainByItemVerId(itemVerId);
            if(aeaImItemExplain != null && StringUtils.isNotBlank(aeaImItemExplain.getUnitRequireId())){
                AeaImUnitRequire unitRequire = aeaImUnitRequireMapper.getAeaImUnitRequireById(aeaImItemExplain.getUnitRequireId());
                if(unitRequire != null && StringUtils.isNotBlank(unitRequire.getQualRequire())){
                    String[] qualIdArr = unitRequire.getQualRequire().split("\\.");
                    for(String id:qualIdArr){
                        if(StringUtils.isNotBlank(id)){
                            qualIdList.add(id);
                        }
                    }
                }
            }
        }
        return qualIdList;
    }

    @Override
    public PageInfo<AeaImQual> listAeaImNotInQual(AeaImQual aeaImQual, List<String> qualIds, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaImQual> list = aeaImQualMapper.listAeaImNotInQual(aeaImQual,qualIds);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaImQual>(list);
    }
    @Override
    public PageInfo<AeaImQual> listAeaImInQual(AeaImQual aeaImQual, List<String> qualIds, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaImQual> list = aeaImQualMapper.listAeaImInQual(aeaImQual,qualIds);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaImQual>(list);
    }
    @Override
    public AeaImQual getAeaImQualById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaImQualMapper.getAeaImQualById(id);
    }
    @Override
    public List<AeaImQual> listAeaImQual(AeaImQual aeaImQual) throws Exception{
        List<AeaImQual> list = aeaImQualMapper.listAeaImQual(aeaImQual);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public boolean checkUniqueQualCode(String qualId, String qualCode) {
        Integer count = aeaImQualMapper.checkUniqueQualCode(qualId, qualCode);
        logger.debug("成功执行查询资质编号{}是否存在！，查询结果：{}",qualCode,count);
        return count == null || count <= 0;
    }

    @Override
    public void batchDeleteQual(String[] ids) throws Exception {
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                this.deleteAeaImQualById(id);
            }
        }
        logger.debug("成功批量删除资质！ID为：{}", JsonUtils.toJson(ids));
    }

    @Override
    public ResultForm saveQualLevelCfg(String qualId, String qualLevelId) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        AeaImQual aeaImQual = this.getAeaImQualById(qualId);
        if(aeaImQual != null){
            AeaImQualLevel aeaImQualLevel = aeaImQualLevelMapper.getAeaImQualLevelById(qualLevelId);
            if(aeaImQualLevel != null && "root".equals(aeaImQualLevel.getParentQualLevelId())){
                aeaImQual.setQualLevelId(qualLevelId);
                aeaImQualMapper.updateAeaImQual(aeaImQual);
                resultForm.setSuccess(true);
            }
        }
        return resultForm;
    }

    @Override
    public ContentResultForm getQualIdsByItemVerId(String itemVerId) throws Exception {
        ContentResultForm resultForm = new ContentResultForm(false);
        if(StringUtils.isNotBlank(itemVerId)){
            List<String> list = this.queryQualIdListByItemVerId(itemVerId);
            List<AeaImQual> aeaImQuals = new ArrayList<>();
            if(list != null && list.size() > 0){
                aeaImQuals = aeaImQualMapper.listAeaImQualByQualIds(list.toArray(new String[list.size()]));
            }
            resultForm.setSuccess(true);
            resultForm.setContent(aeaImQuals);
        }
        return resultForm;
    }

    @Override
    public List<AeaImQual> getQualNamesByIds(String idSeq) throws Exception {
        List<AeaImQual> list = new ArrayList<>();
        if(StringUtils.isNotBlank(idSeq)){
            String[] ids = idSeq.split("\\.");
            list = aeaImQualMapper.listAeaImQualByQualIds(ids);
        }
        return list;
    }
}

