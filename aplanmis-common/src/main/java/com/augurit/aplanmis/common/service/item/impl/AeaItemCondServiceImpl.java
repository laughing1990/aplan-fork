package com.augurit.aplanmis.common.service.item.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.AeaItemCond;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.mapper.AeaItemCondMapper;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.service.item.AeaItemCondService;
import com.augurit.aplanmis.common.utils.CommonTools;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class AeaItemCondServiceImpl implements AeaItemCondService {
    @Autowired
    private AeaItemCondMapper aeaItemCondMapper;

    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Override
    public List<AeaItemCond> getAeaItemCondListByItemVerId(String itemVerId,String isQuestion) throws Exception {
        if(StringUtils.isBlank(itemVerId)) throw new InvalidParameterException("参数itemVerId为空！");
        AeaItemCond query=new AeaItemCond();
        query.setItemVerId(itemVerId);
        query.setIsActive("1");  // 1 启用  0 禁用
        if(StringUtils.isNotBlank(isQuestion)){
            query.setIsQuestion(isQuestion); // 1 问题  0 答案
        }
        return aeaItemCondMapper.listAeaItemCond(query);
    }

    @Override
    public List<AeaItemCond> getChildAeaItemCondListByCondId(String condId) throws Exception {
        if(StringUtils.isBlank(condId)) throw new InvalidParameterException("参数condId为空！");
        AeaItemCond query=new AeaItemCond();
        query.setParentCondId(condId);
        query.setIsActive("1");  // 1 启用  0 禁用
        List<AeaItemCond> questions = aeaItemCondMapper.listAeaItemCond(query);
        convertTreeCondList(questions);
        return questions;
    }

    @Override
    public boolean judgeElExpression(String itemVerId, String projInfoId) throws Exception {
        if(StringUtils.isBlank(itemVerId)) throw new InvalidParameterException("参数itemVerId为空！");
        if(StringUtils.isBlank(projInfoId)) throw new InvalidParameterException("参数projInfoId为空！");
        boolean flag = true;
        AeaItemCond aeaItemCond = new AeaItemCond();
        aeaItemCond.setIsActive("1");
        aeaItemCond.setItemVerId(itemVerId);
        List<AeaItemCond> aeaItemConds = aeaItemCondMapper.listAeaItemCond(aeaItemCond);// 获取事项前置条件
        if (aeaItemConds.size() > 0) {
            aeaItemCond = aeaItemConds.get(0);
            if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(aeaItemCond.getCondEl())) {
                List<String> tags = CommonTools.getTags(aeaItemCond.getCondEl());//  获取表达式的所有标签
                if (tags.size() > 0) {
                    AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
                    if (aeaProjInfo == null) {
                        return false;
                    }
                    Object obj = CommonTools.convertToCode(aeaItemCond.getCondEl(), CommonTools.getProperty(aeaProjInfo, tags));// 执行el表达式进行判断
                    flag = ((Boolean) obj).booleanValue();
                }
            }
        }
        return flag;
    }

    @Override
    public void saveAeaItemCond(AeaItemCond aeaItemCond) throws Exception {
        if(StringUtils.isBlank(aeaItemCond.getItemCondId())){
            aeaItemCond.setItemCondId(UUID.randomUUID().toString());
            aeaItemCondMapper.insertAeaItemCond(aeaItemCond);
        }else{
            aeaItemCondMapper.updateAeaItemCond(aeaItemCond);
        }
    }

    @Override
    public void deleteAeaItemCond(String id) throws Exception {
        if(StringUtils.isBlank(id)) throw new InvalidParameterException("参数id为空！");
        aeaItemCondMapper.deleteAeaItemCond(id);
    }

    @Override
    public List<AeaItemCond> listAeaItemCond(AeaItemCond aeaItemCond) throws Exception {
        return aeaItemCondMapper.listAeaItemCond(aeaItemCond);
    }

    @Override
    public AeaItemCond getAeaItemCondById(String id) throws Exception {
        if(StringUtils.isBlank(id)) throw new InvalidParameterException("参数id为空！");
        return aeaItemCondMapper.getAeaItemCondById(id);
    }

    @Override
    public PageInfo<AeaItemCond> listAeaItemCond(AeaItemCond aeaItemCond, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaItemCond> list=listAeaItemCond(aeaItemCond);
        return new PageInfo<>(list);
    }

    @Override
    public List<AeaItemCond> getAeaItemCondTreeListByItemVerId(String itemVerId) throws Exception {
        List<AeaItemCond> list = getAeaItemCondTopListByItemVerId(itemVerId);
        convertTreeCondList(list);
        return list;
    }

    @Override
    public List<AeaItemCond> getAeaItemCondTopListByItemVerId(String itemVerId) throws Exception {
        if(StringUtils.isBlank(itemVerId)) throw new InvalidParameterException("参数itemVerId为空！");
        AeaItemCond query=new AeaItemCond();
        query.setItemVerId(itemVerId);
        query.setIsActive("1");  // 1 启用  0 禁用
        query.setIsQuestion("1"); // 1 问题  0 答案
        query.setParentCondId("root");
        return aeaItemCondMapper.listAeaItemCond(query);
    }

    private void convertTreeCondList(List<AeaItemCond> list) throws Exception {
        for (AeaItemCond cond:list){
            AeaItemCond query=new AeaItemCond();
            query.setParentCondId(cond.getItemCondId());
            query.setIsActive("1");  // 1 启用  0 禁用
            query.setIsQuestion("0");// 1 问题  0 答案
            List<AeaItemCond> childList=aeaItemCondMapper.listAeaItemCond(query);
            //List<AeaItemCond> childList = getChildAeaItemCondListByCondId(cond.getItemCondId());
            cond.setChildCondList(childList);
            //convertTreeCondList(childList);
        }
    }


}
