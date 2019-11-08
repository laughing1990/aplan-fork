package com.augurit.aplanmis.common.service.admin.item.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemPriv;
import com.augurit.aplanmis.common.mapper.AeaItemPrivMapper;
import com.augurit.aplanmis.common.service.admin.item.AeaItemPrivAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author ZhangXinhui
 * @date 2019/8/5 005 16:47
 * @desc
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class AeaItemPrivAdminServiceImpl implements AeaItemPrivAdminService {

    private static Logger logger = LoggerFactory.getLogger(AeaItemPrivAdminService.class);

    @Autowired
    private AeaItemPrivMapper aeaItemPrivMapper;

    @Override
    public EasyuiPageInfo<AeaItemPriv> getPrivListByItemId(String itemVerId, String keyword, Page page){

        if(StringUtils.isBlank(itemVerId)){
            throw new InvalidParameterException("事项版本Id为空！");
        }
        PageHelper.startPage(page);
        //获取事项下放列表
        List<AeaItemPriv> list = aeaItemPrivMapper.listPrivListByItemVerId(itemVerId, keyword);
        logger.debug("成功执行分页查询！！");
        EasyuiPageInfo<AeaItemPriv> pageInfo = PageHelper.toEasyuiPageInfo(new PageInfo<>(list));
        return pageInfo;
    }

    @Override
    public void saveAeaItemPriv(AeaItemPriv aeaItemPriv)throws Exception{

        String rootOrgId = SecurityContext.getCurrentOrgId();
        String userId = SecurityContext.getCurrentUserId();
        List<String> itemVerIdList = aeaItemPriv.getItemVerIds();
        if(itemVerIdList!=null&&itemVerIdList.size()>0){
            for(String itemVerId : itemVerIdList){
                aeaItemPriv.setRootOrgId(rootOrgId);
                aeaItemPriv.setItemVerId(itemVerId);
                aeaItemPriv.setCreater(userId);
                aeaItemPriv.setCreateTime(new Date());
                handOnePriv(aeaItemPriv);
            }
        }else {
            aeaItemPriv.setRootOrgId(rootOrgId);
            aeaItemPriv.setCreater(userId);
            aeaItemPriv.setCreateTime(new Date());
            handOnePriv(aeaItemPriv);
        }
    }

    private void handOnePriv(AeaItemPriv aeaItemPriv)throws Exception{

        AeaItemPriv spriv = new AeaItemPriv();
        spriv.setRootOrgId(aeaItemPriv.getRootOrgId());
        spriv.setItemVerId(aeaItemPriv.getItemVerId());
        spriv.setRegionId(aeaItemPriv.getRegionId());
        spriv.setOrgId(aeaItemPriv.getOrgId());
        List<AeaItemPriv> hasPrivList = aeaItemPrivMapper.listAeaItemPriv(spriv);
        if(hasPrivList!=null&&hasPrivList.size()>0){
            AeaItemPriv oldPriv = hasPrivList.get(0);
            String oldId = oldPriv.getPrivId();
            BeanUtils.copyProperties(oldPriv, aeaItemPriv);
            oldPriv.setPrivId(oldId);
            aeaItemPrivMapper.updateAeaItemPriv(oldPriv);
        }else{
            aeaItemPriv.setPrivId(UUID.randomUUID().toString());
            aeaItemPrivMapper.insertAeaItemPriv(aeaItemPriv);
        }
    }

    @Override
    public void deleteAeaItemPrivById(String id){

        if(id == null) {
            throw new InvalidParameterException(id);
        }
        aeaItemPrivMapper.deleteAeaItemPriv(id);
    }
}
