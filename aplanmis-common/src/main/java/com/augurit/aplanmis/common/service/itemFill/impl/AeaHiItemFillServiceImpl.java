package com.augurit.aplanmis.common.service.itemFill.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.security.user.OpuOmUser;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.constants.AeaHiItemFillStateEnum;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.dto.MatCorrectInfoDto;
import com.augurit.aplanmis.common.mapper.AeaHiItemFillDueIninstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemFillMapper;
import com.augurit.aplanmis.common.mapper.AeaHiItemFillRealIninstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemInoutMapper;
import com.augurit.aplanmis.common.service.applyinst.AeaHiApplyinstCorrectService;
import com.augurit.aplanmis.common.service.itemFill.AeaHiItemFillService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 事项容缺补齐实例表-Service服务接口实现类
 */
@Service
@Transactional
public class AeaHiItemFillServiceImpl implements AeaHiItemFillService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiItemFillServiceImpl.class);

    @Autowired
    private AeaHiItemFillMapper aeaHiItemFillMapper;

    @Autowired
    private AeaHiItemFillDueIninstMapper aeaHiItemFillDueIninstMapper;

    @Autowired
    private AeaHiItemFillRealIninstMapper aeaHiItemFillRealIninstMapper;

    @Autowired
    private AeaItemInoutMapper aeaItemInoutMapper;

    @Autowired
    private AeaHiApplyinstCorrectService aeaHiApplyinstCorrectService;

    public void saveAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception {
        aeaHiItemFillMapper.insertAeaHiItemFill(aeaHiItemFill);
    }

    public void updateAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception {
        aeaHiItemFillMapper.updateAeaHiItemFill(aeaHiItemFill);
    }

    public void deleteAeaHiItemFillById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaHiItemFillMapper.deleteAeaHiItemFill(id);
    }

    public PageInfo<AeaHiItemFill> listAeaHiItemFill(AeaHiItemFill aeaHiItemFill, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiItemFill> list = aeaHiItemFillMapper.listAeaHiItemFill(aeaHiItemFill);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiItemFill>(list);
    }

    public AeaHiItemFill getAeaHiItemFillById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiItemFillMapper.getAeaHiItemFillById(id);
    }

    public List<AeaHiItemFill> listAeaHiItemFill(AeaHiItemFill aeaHiItemFill) throws Exception {
        List<AeaHiItemFill> list = aeaHiItemFillMapper.listAeaHiItemFill(aeaHiItemFill);
        logger.debug("成功执行查询list！！");
        return list;
    }

    /**
     * 创建容缺补齐实例，包括补齐的详细信息
     * @param applyinstId
     * @throws Exception
     */
    @Override
    public void createAeaHiItemFill(String applyinstId) throws Exception {
        //查询申报容缺未上传的材料信息，这里调用的是原 材料补全的接口
        MatCorrectInfoDto lackMats = aeaHiApplyinstCorrectService.getLackMatsByApplyinstId(applyinstId);
        if(lackMats != null && lackMats.getAllMats() != null && lackMats.getAllMats().size() > 0){
            List<AeaItemMat> allMats = lackMats.getAllMats();
            List matIds = Lists.newArrayList();
            for(int i=0,len=allMats.size(); i<len; i++){
                AeaItemMat aeaItemMat = allMats.get(i);
                if("0".equals(aeaItemMat.getAttIsRequire()) && (aeaItemMat.getAttMatinstList() == null || aeaItemMat.getAttMatinstList().size() == 0)){
                    //如果材料是 电子件容缺 且 暂未上传电子件的，才进行容缺补齐
                    matIds.add(aeaItemMat.getMatId());
                }
            }
            List<AeaHiIteminst> iteminstList = lackMats.getIteminstList();

            //登录信息
            OpuOmUser currentUser = SecurityContext.getCurrentUser();
            String currentOrgId = SecurityContext.getCurrentOrgId();
            Date currentTime = new Date();
            for(AeaHiIteminst iteminst : iteminstList){
                //找到需要容缺补齐的 事项输入材料定义
                List<AeaItemInout> inouts = aeaItemInoutMapper.listAeaItemInoutByMatIds(matIds, iteminst.getItemVerId(), "1");
                List<AeaHiItemFillDueIninst> fillDueIninsts = Lists.newArrayList();
                String fillId = UUID.randomUUID().toString();
                //先判断当前事项是否存在容缺材料
                for(AeaItemInout itemInout : inouts){
                    if(iteminst.getItemVerId().equals(itemInout.getItemVerId())){
                        //创建一个容缺材料实例
                        AeaHiItemFillDueIninst temp = new AeaHiItemFillDueIninst();
                        temp.setDueIninstId(UUID.randomUUID().toString());
                        temp.setFillId(fillId);
                        temp.setInoutinstId(itemInout.getInoutId());//暂时使用输入输出定义id，后续再确定要不要实例id
                        temp.setIsNeedAtt("1");
                        temp.setRootOrgId(currentOrgId);
                        temp.setCreater(currentUser.getUserId());
                        temp.setCreateTime(currentTime);
                        fillDueIninsts.add(temp);
                    }
                }
                if(fillDueIninsts.size() > 0){
                    //存在则创建容缺实例
                    AeaHiItemFill temp = new AeaHiItemFill();
                    temp.setFillId(fillId);
                    temp.setIteminstId(iteminst.getIteminstId());
                    temp.setApplyinstId(applyinstId);
                    temp.setProjInfoId(lackMats.getProjInfoId());
                    temp.setChargeOrgId(iteminst.getApproveOrgId());
                    temp.setChargeOrgName(iteminst.getApproveOrgName());
                    temp.setLastTipsCount(0L);
                    temp.setFillState(AeaHiItemFillStateEnum.UN_START.getValue());
                    temp.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
                    temp.setRootOrgId(currentOrgId);
                    temp.setCreater(currentUser.getUserId());
                    temp.setCreateTime(currentTime);
                    //这里的插入还有问题，需要先生成输入输出实例，并获取输入输出实例id,回填，
                    aeaHiItemFillMapper.insertAeaHiItemFill(temp);
                    //插入事项容缺 要求补齐材料实例
                    aeaHiItemFillDueIninstMapper.batchInsertAeaHiItemFillDueIninst(fillDueIninsts);
                }
            }
        }
    }
}

