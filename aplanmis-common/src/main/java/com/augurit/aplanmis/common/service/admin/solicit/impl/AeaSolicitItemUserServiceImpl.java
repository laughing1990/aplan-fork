package com.augurit.aplanmis.common.service.admin.solicit.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaSolicitItemUser;
import com.augurit.aplanmis.common.mapper.AeaSolicitItemUserMapper;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitItemUserService;
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
* 按事项征求的人员名单表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaSolicitItemUserServiceImpl implements AeaSolicitItemUserService {

    private static Logger logger = LoggerFactory.getLogger(AeaSolicitItemUserServiceImpl.class);

    @Autowired
    private AeaSolicitItemUserMapper aeaSolicitItemUserMapper;

    @Override
    public void saveAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) {

        aeaSolicitItemUser.setSortNo(getMaxSortNo());
        aeaSolicitItemUser.setIsActive(Status.ON);
        aeaSolicitItemUser.setCreater(SecurityContext.getCurrentUserId());
        aeaSolicitItemUser.setCreateTime(new Date());
        aeaSolicitItemUserMapper.insertAeaSolicitItemUser(aeaSolicitItemUser);
    }

    @Override
    public void updateAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) {

        aeaSolicitItemUser.setModifier(SecurityContext.getCurrentUserId());
        aeaSolicitItemUser.setModifyTime(new Date());
        aeaSolicitItemUserMapper.updateAeaSolicitItemUser(aeaSolicitItemUser);
    }

    @Override
    public void deleteAeaSolicitItemUserById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaSolicitItemUserMapper.deleteAeaSolicitItemUser(id);
    }

    @Override
    public void batchDelSolicitItemUserByIds(String[] ids){

        if(ids!=null&&ids.length>0){

            aeaSolicitItemUserMapper.batchDelSolicitItemUserByIds(ids);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public AeaSolicitItemUser getAeaSolicitItemUserById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaSolicitItemUserMapper.getAeaSolicitItemUserById(id);
    }

    @Override
    public PageInfo<AeaSolicitItemUser> listAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser,Page page) {

        PageHelper.startPage(page);
        List<AeaSolicitItemUser> list = aeaSolicitItemUserMapper.listAeaSolicitItemUser(aeaSolicitItemUser);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitItemUser>(list);
    }

    @Override
    public List<AeaSolicitItemUser> listAeaSolicitItemUser(AeaSolicitItemUser aeaSolicitItemUser) {

        List<AeaSolicitItemUser> list = aeaSolicitItemUserMapper.listAeaSolicitItemUser(aeaSolicitItemUser);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaSolicitItemUser> listAeaSolicitItemUserRelInfo(AeaSolicitItemUser aeaSolicitItemUser, Page page){

        PageHelper.startPage(page);
        List<AeaSolicitItemUser> list = aeaSolicitItemUserMapper.listAeaSolicitItemUserRelInfo(aeaSolicitItemUser);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitItemUser>(list);
    }

    @Override
    public List<AeaSolicitItemUser> listAeaSolicitItemUserRelInfo(AeaSolicitItemUser aeaSolicitItemUser){

        List<AeaSolicitItemUser> list = aeaSolicitItemUserMapper.listAeaSolicitItemUserRelInfo(aeaSolicitItemUser);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void batchSaveSolicitItemUser(String solicitItemId, String[] userIds, String[] sortNos){

        String creater = SecurityContext.getCurrentUserId();
        if(userIds!=null&&userIds.length>0) {
            // 查找需要删除的
            List<String> needDelIdList = new ArrayList<String>();
            List<AeaSolicitItemUser> needDelList = new ArrayList<AeaSolicitItemUser>();
            AeaSolicitItemUser sSolicitItemUser = new AeaSolicitItemUser();
            sSolicitItemUser.setSolicitItemId(solicitItemId);
            List<AeaSolicitItemUser> ItemUserList = aeaSolicitItemUserMapper.listAeaSolicitItemUser(sSolicitItemUser);
            if(ItemUserList!=null&&ItemUserList.size()>0){
                for(AeaSolicitItemUser item : ItemUserList){
                    int count=0;
                    for (String userId : userIds) {
                        if(item.getUserId().equals(userId)){
                            break;
                        }else{
                            count++;
                        }
                    }
                    if(count==userIds.length){
                        needDelList.add(item);
                        needDelIdList.add(item.getItemUserId());
                    }
                }
            }
            // 先删除
            if(needDelList!=null&&needDelList.size()>0){

                ItemUserList.removeAll(needDelList);
                String[] needDelIdArr = new String[needDelIdList.size()];
                needDelIdList.toArray(needDelIdArr);
                batchDelSolicitItemUserByIds(needDelIdArr);
            }

            // 保存
            for (int i=0; i<userIds.length;i++) {
                AeaSolicitItemUser updateVo = null;
                if (ItemUserList != null && ItemUserList.size() > 0) {
                    for (AeaSolicitItemUser item : ItemUserList) {
                        if(item.getUserId().equals(userIds[i])){
                            updateVo = item;
                            break;
                        }
                    }
                }
                if(updateVo==null){
                    AeaSolicitItemUser solicitItemUser = new AeaSolicitItemUser();
                    solicitItemUser.setItemUserId(UUID.randomUUID().toString());
                    solicitItemUser.setUserId(userIds[i]);
                    solicitItemUser.setSolicitItemId(solicitItemId);
                    solicitItemUser.setSortNo(new Long(sortNos[i]));
                    solicitItemUser.setIsActive(Status.ON);
                    solicitItemUser.setCreater(creater);
                    solicitItemUser.setCreateTime(new Date());
                    aeaSolicitItemUserMapper.insertAeaSolicitItemUser(solicitItemUser);
                }else{
                    updateVo.setSortNo(new Long(sortNos[i]));
                    updateVo.setModifier(creater);
                    updateVo.setModifyTime(new Date());
                    aeaSolicitItemUserMapper.updateAeaSolicitItemUser(updateVo);
                }
            }
        }else{
            batchDelSolicitItemUserBySolicitItemId(solicitItemId);
        }
    }

    @Override
    public void batchDelSolicitItemUserBySolicitItemId(String solicitItemId){

        aeaSolicitItemUserMapper.batchDelSolicitItemUserBySolicitItemId(solicitItemId);
    }

    @Override
    public Long getMaxSortNo(){

        Long sortNo = aeaSolicitItemUserMapper.getMaxSortNo();
        return sortNo==null?1L:(sortNo+1L);
    }

    @Override
    public void changeIsActive(String id){

        aeaSolicitItemUserMapper.changeIsActive(id);
    }
}

