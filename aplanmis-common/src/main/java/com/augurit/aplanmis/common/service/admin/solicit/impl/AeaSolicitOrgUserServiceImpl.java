package com.augurit.aplanmis.common.service.admin.solicit.impl;

import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaSolicitOrgUser;
import com.augurit.aplanmis.common.mapper.AeaSolicitOrgUserMapper;
import com.augurit.aplanmis.common.service.admin.solicit.AeaSolicitOrgUserService;
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
* 按组织征求的人员名单表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaSolicitOrgUserServiceImpl implements AeaSolicitOrgUserService {

    private static Logger logger = LoggerFactory.getLogger(AeaSolicitOrgUserServiceImpl.class);

    @Autowired
    private AeaSolicitOrgUserMapper aeaSolicitOrgUserMapper;

    @Override
    public void saveAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) {

        aeaSolicitOrgUser.setSortNo(getMaxSortNo());
        aeaSolicitOrgUser.setIsActive(Status.ON);
        aeaSolicitOrgUser.setCreater(SecurityContext.getCurrentUserId());
        aeaSolicitOrgUser.setCreateTime(new Date());
        aeaSolicitOrgUserMapper.insertAeaSolicitOrgUser(aeaSolicitOrgUser);
    }

    @Override
    public void updateAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) {

        aeaSolicitOrgUser.setModifier(SecurityContext.getCurrentUserId());
        aeaSolicitOrgUser.setModifyTime(new Date());
        aeaSolicitOrgUserMapper.updateAeaSolicitOrgUser(aeaSolicitOrgUser);
    }

    @Override
    public void deleteAeaSolicitOrgUserById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        aeaSolicitOrgUserMapper.deleteAeaSolicitOrgUser(id);
    }

    @Override
    public void batchDelSolicitOrgUserByIds(String[] ids){

        if(ids!=null&&ids.length>0){

            aeaSolicitOrgUserMapper.batchDelSolicitOrgUserByIds(ids);
        }else{
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @Override
    public AeaSolicitOrgUser getAeaSolicitOrgUserById(String id) {

        if(StringUtils.isBlank(id)){
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaSolicitOrgUserMapper.getAeaSolicitOrgUserById(id);
    }

    @Override
    public PageInfo<AeaSolicitOrgUser> listAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser,Page page) {

        PageHelper.startPage(page);
        List<AeaSolicitOrgUser> list = aeaSolicitOrgUserMapper.listAeaSolicitOrgUser(aeaSolicitOrgUser);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitOrgUser>(list);
    }

    @Override
    public List<AeaSolicitOrgUser> listAeaSolicitOrgUser(AeaSolicitOrgUser aeaSolicitOrgUser) {

        List<AeaSolicitOrgUser> list = aeaSolicitOrgUserMapper.listAeaSolicitOrgUser(aeaSolicitOrgUser);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaSolicitOrgUser> listAeaSolicitOrgUserRelInfo(AeaSolicitOrgUser aeaSolicitOrgUser, Page page){

        PageHelper.startPage(page);
        List<AeaSolicitOrgUser> list = aeaSolicitOrgUserMapper.listAeaSolicitOrgUserRelInfo(aeaSolicitOrgUser);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaSolicitOrgUser>(list);
    }

    @Override
    public List<AeaSolicitOrgUser> listAeaSolicitOrgUserRelInfo(AeaSolicitOrgUser aeaSolicitOrgUser){

        List<AeaSolicitOrgUser> list = aeaSolicitOrgUserMapper.listAeaSolicitOrgUserRelInfo(aeaSolicitOrgUser);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public void batchSaveSolicitOrgUser(String solicitOrgId, String[] userIds, String[] sortNos){

        String creater = SecurityContext.getCurrentUserId();
        if(userIds!=null&&userIds.length>0) {
            // 查找需要删除的
            List<String> needDelIdList = new ArrayList<String>();
            List<AeaSolicitOrgUser> needDelList = new ArrayList<AeaSolicitOrgUser>();
            AeaSolicitOrgUser sSolicitOrgUser = new AeaSolicitOrgUser();
            sSolicitOrgUser.setSolicitOrgId(solicitOrgId);
            List<AeaSolicitOrgUser> orgUserList = aeaSolicitOrgUserMapper.listAeaSolicitOrgUser(sSolicitOrgUser);
            if(orgUserList!=null&&orgUserList.size()>0){
                for(AeaSolicitOrgUser item : orgUserList){
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
                        needDelIdList.add(item.getOrgUserId());
                    }
                }
            }
            // 先删除
            if(needDelList!=null&&needDelList.size()>0){

                orgUserList.removeAll(needDelList);
                String[] needDelIdArr = new String[needDelIdList.size()];
                needDelIdList.toArray(needDelIdArr);
                batchDelSolicitOrgUserByIds(needDelIdArr);
            }

            // 保存
            for (int i=0; i<userIds.length;i++) {
                AeaSolicitOrgUser updateVo = null;
                if (orgUserList != null && orgUserList.size() > 0) {
                    for (AeaSolicitOrgUser item : orgUserList) {
                        if(item.getUserId().equals(userIds[i])){
                            updateVo = item;
                            break;
                        }
                    }
                }
                if(updateVo==null){
                    AeaSolicitOrgUser solicitOrgUser = new AeaSolicitOrgUser();
                    solicitOrgUser.setOrgUserId(UUID.randomUUID().toString());
                    solicitOrgUser.setUserId(userIds[i]);
                    solicitOrgUser.setSolicitOrgId(solicitOrgId);
                    solicitOrgUser.setSortNo(new Long(sortNos[i]));
                    solicitOrgUser.setIsActive(Status.ON);
                    solicitOrgUser.setCreater(creater);
                    solicitOrgUser.setCreateTime(new Date());
                    aeaSolicitOrgUserMapper.insertAeaSolicitOrgUser(solicitOrgUser);
                }else{
                    updateVo.setSortNo(new Long(sortNos[i]));
                    updateVo.setModifier(creater);
                    updateVo.setModifyTime(new Date());
                    aeaSolicitOrgUserMapper.updateAeaSolicitOrgUser(updateVo);
                }
            }
        }else{
            batchDelSolicitOrgUserBySolicitOrgId(solicitOrgId);
        }
    }

    @Override
    public void batchDelSolicitOrgUserBySolicitOrgId(String solicitOrgId){

        aeaSolicitOrgUserMapper.batchDelSolicitOrgUserBySolicitOrgId(solicitOrgId);
    }

    @Override
    public Long getMaxSortNo(){

        Long sortNo = aeaSolicitOrgUserMapper.getMaxSortNo();
        return sortNo==null?1L:(sortNo+1L);
    }

    @Override
    public void changeIsActive(String id){

        aeaSolicitOrgUserMapper.changeIsActive(id);
    }
}

