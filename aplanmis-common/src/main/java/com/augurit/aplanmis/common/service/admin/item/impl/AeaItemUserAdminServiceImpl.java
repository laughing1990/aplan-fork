package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserMapper;
import com.augurit.aplanmis.common.domain.AeaItemUser;
import com.augurit.aplanmis.common.mapper.AeaItemUserMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemUserAdminService;
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
* 用户事项管理-Service服务接口实现类
*/
@Service
@Transactional
public class AeaItemUserAdminServiceImpl implements AeaItemUserAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemUserAdminServiceImpl.class);

    @Autowired
    private OpuOmUserMapper opuOmUserMapper;

    @Autowired
    private AeaItemUserMapper aeaItemUserMapper;

    @Override
    public Long getMaxSortNo(String rootOrgId){

        Long sortNo = aeaItemUserMapper.getMaxSortNo(rootOrgId);
        return sortNo==null?1L:(sortNo+1L);
    }

    @Override
    public void changIsActive(String id, String rootOrgId){

        aeaItemUserMapper.changIsActive(id, rootOrgId);
    }

    @Override
    public void saveAeaItemUser(AeaItemUser aeaItemUser) {

        aeaItemUser.setIsActive(Status.ON);
        aeaItemUser.setCreateTime(new Date());
        aeaItemUser.setCreater(SecurityContext.getCurrentUserId());
        aeaItemUser.setRootOrgId(SecurityContext.getCurrentOrgId());
        aeaItemUserMapper.insertAeaItemUser(aeaItemUser);
    }

    @Override
    public void updateAeaItemUser(AeaItemUser aeaItemUser) {

        aeaItemUser.setModifyTime(new Date());
        aeaItemUser.setModifier(SecurityContext.getCurrentUserId());
        aeaItemUserMapper.updateAeaItemUser(aeaItemUser);
    }

    @Override
    public void deleteAeaItemUserById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaItemUserMapper.deleteAeaItemUser(id);
    }

    @Override
    public void batchDelItemUserByIds(String[] ids){

        if(ids!=null&&ids.length>0){
            aeaItemUserMapper.batchDelItemUserByArrIds(ids);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public PageInfo<AeaItemUser> listAeaItemUser(AeaItemUser aeaItemUser, Page page) {

        aeaItemUser.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemUser> list = aeaItemUserMapper.listAeaItemUser(aeaItemUser);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemUser>(list);
    }

    @Override
    public AeaItemUser getAeaItemUserById(String id) {

        if(StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaItemUserMapper.getAeaItemUserById(id);
    }

    @Override
    public List<AeaItemUser> listAeaItemUser(AeaItemUser aeaItemUser) {

        aeaItemUser.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemUser> list = aeaItemUserMapper.listAeaItemUser(aeaItemUser);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaItemUser> listUserItemRelItemInfo(AeaItemUser aeaItemUser, Page page){

        aeaItemUser.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaItemUser> list = aeaItemUserMapper.listUserItemRelItemInfo(aeaItemUser);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaItemUser>(list);
    }

    @Override
    public List<AeaItemUser> listUserItemRelItemInfo(AeaItemUser aeaItemUser){

        aeaItemUser.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaItemUser> list = aeaItemUserMapper.listUserItemRelItemInfo(aeaItemUser);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<OpuOmUser> listAllUserRelOrgByOrgId(String orgId){

        List<OpuOmUser> allNodes = new ArrayList<>();
        OpuOmUser rootNode = new OpuOmUser();
        rootNode.setId("root");
        rootNode.setName("组织用户树");
        rootNode.setType("root");
        rootNode.setOpen(true);
        rootNode.setIsParent(true);
        rootNode.setNocheck(true);
        allNodes.add(rootNode);
        List<OpuOmUser> list = opuOmUserMapper.listAllUserRelOrgByOrgId(orgId);
        if(list!=null&&list.size()>0){
            for(OpuOmUser user : list){
                convertUserNode(user);
                allNodes.add(user);
            }
        }
        return allNodes;
    }

    private void convertUserNode(OpuOmUser user){

        user.setId(user.getUserId());
        user.setName(user.getLoginName());
        if (StringUtils.isNotBlank(user.getOrgName())) {
            user.setName(user.getLoginName() + "【" + user.getOrgName() + "】");
        }
        user.setpId("root");
        user.setType("user");
        user.setOpen(true);
        user.setIsParent(false);
        user.setNocheck(false);
    }

    @Override
    public void batchSaveUserItem(String curUserId, String[] itemIds, String[] sortNos){

        if (StringUtils.isNotBlank(curUserId)) {
            String userId = SecurityContext.getCurrentUserId();
            String rootOrgId = SecurityContext.getCurrentOrgId();
            if(itemIds!=null&&itemIds.length>0) {
                // 查找需要删除的
                List<String> needDelIdList = new ArrayList<String>();
                List<AeaItemUser> needDelList = new ArrayList<AeaItemUser>();
                AeaItemUser sUserItem = new AeaItemUser();
                sUserItem.setUserId(curUserId);
                sUserItem.setRootOrgId(rootOrgId);
                List<AeaItemUser> userItemList = aeaItemUserMapper.listAeaItemUser(sUserItem);
                if(userItemList!=null&&userItemList.size()>0){
                    for(AeaItemUser item : userItemList){
                        int count=0;
                        for (String itemId : itemIds) {
                            if(item.getItemId().equals(itemId)){
                                break;
                            }else{
                                count++;
                            }
                        }
                        if(count==itemIds.length){
                            needDelList.add(item);
                            needDelIdList.add(item.getItemUserId());
                        }
                    }
                }
                // 先删除
                if(needDelList!=null&&needDelList.size()>0){

                    userItemList.removeAll(needDelList);
                    aeaItemUserMapper.batchDelItemUserByListIds(needDelIdList);
                }

                // 保存
                for (int i=0; i<itemIds.length;i++) {
                    AeaItemUser updateVo = null;
                    if (userItemList != null && userItemList.size() > 0) {
                        for (AeaItemUser item : userItemList) {
                            if(item.getItemId().equals(itemIds[i])){
                                updateVo = item;
                                break;
                            }
                        }
                    }
                    if(updateVo==null){
                        AeaItemUser aeaItemUser = new AeaItemUser();
                        aeaItemUser.setItemUserId(UUID.randomUUID().toString());
                        aeaItemUser.setUserId(curUserId);
                        aeaItemUser.setItemId(itemIds[i]);
                        aeaItemUser.setSortNo(new Long(sortNos[i]));
                        aeaItemUser.setIsActive(Status.ON);
                        aeaItemUser.setCreater(userId);
                        aeaItemUser.setCreateTime(new Date());
                        aeaItemUser.setRootOrgId(rootOrgId);
                        aeaItemUserMapper.insertAeaItemUser(aeaItemUser);
                    }else{
                        updateVo.setModifier(userId);
                        updateVo.setModifyTime(new Date());
                        updateVo.setSortNo(new Long(sortNos[i]));
                        aeaItemUserMapper.updateAeaItemUser(updateVo);
                    }
                }
            }else{
                aeaItemUserMapper.batchDelItemByUserId(curUserId, rootOrgId);
            }
        }
    }

    @Override
    public void batchDelItemByUserId(String userId, String rootOrgId){

        if(StringUtils.isNotBlank(userId)){
            aeaItemUserMapper.batchDelItemByUserId(userId, rootOrgId);
        }
    }
}

