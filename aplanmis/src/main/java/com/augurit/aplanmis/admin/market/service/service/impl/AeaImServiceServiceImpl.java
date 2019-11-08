package com.augurit.aplanmis.admin.market.service.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.service.service.AeaImServiceService;
import com.augurit.aplanmis.common.domain.AeaImService;
import com.augurit.aplanmis.common.domain.AeaImServiceItem;
import com.augurit.aplanmis.common.mapper.AeaImServiceItemMapper;
import com.augurit.aplanmis.common.mapper.AeaImServiceMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * -Service服务接口实现类
 */
@Service
@Transactional
public class AeaImServiceServiceImpl implements AeaImServiceService {

    private static Logger logger = LoggerFactory.getLogger(AeaImServiceServiceImpl.class);

    @Autowired
    private AeaImServiceMapper aeaImServiceMapper;

    @Autowired
    private AeaImServiceItemMapper aeaImServiceItemMapper;

    @Override
    public void saveAeaImService(AeaImService aeaImService) throws Exception {
        aeaImServiceMapper.insertAeaImService(aeaImService);
    }

    @Override
    public void updateAeaImService(AeaImService aeaImService) throws Exception {
        aeaImServiceMapper.updateAeaImService(aeaImService);
    }

    @Override
    public void deleteAeaImServiceById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        aeaImServiceMapper.deleteAeaImServiceQual(id);
        aeaImServiceMapper.deleteAeaImService(id);
    }

    @Override
    public PageInfo<AeaImService> listAeaImService(AeaImService aeaImService, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaImService> list = aeaImServiceMapper.listAeaImService(aeaImService);
        if (list != null && list.size() > 0 && aeaImService != null && StringUtils.isNotBlank(aeaImService.getItemVerId())) {
            //设置itemVerId是否关联了该服务
            List<AeaImServiceItem> serviceItems = aeaImServiceItemMapper.listAeaImServiceItemByItemVerId(aeaImService.getItemVerId());
            if (serviceItems != null && serviceItems.size() > 0) {
                List<String> serviceIdList = new ArrayList<>();
                for (AeaImServiceItem imServiceItem : serviceItems) {
                    serviceIdList.add(imServiceItem.getServiceId());
                }
                for (int i = 0, len = list.size(); i < len; i++) {
                    AeaImService service = list.get(i);
                    if (serviceIdList.contains(service.getServiceId())) {
                        service.setIsCheck(true);
                    }
                }
            }
        }
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaImService>(list);
    }

    @Override
    public AeaImService getAeaImServiceById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaImServiceMapper.getAeaImServiceById(id);
    }

    @Override
    public List<AeaImService> listAeaImService(AeaImService aeaImService) throws Exception {
        List<AeaImService> list = aeaImServiceMapper.listAeaImService(aeaImService);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public ResultForm saveConfigService(String itemVerId, String[] saveServiceIds, String[] cancelServiceIds) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        if (StringUtils.isNotBlank(itemVerId)) {
            this.saveServiceRelation(itemVerId, saveServiceIds);
            this.cancelServiceRelation(itemVerId, cancelServiceIds);
            resultForm.setSuccess(true);
        } else {
            resultForm.setMessage("事项版本ID不能为空");
        }
        return resultForm;
    }

    private void saveServiceRelation(String itemVerId, String[] saveServiceIds) throws Exception {
        if (StringUtils.isNotBlank(itemVerId) && saveServiceIds != null && saveServiceIds.length > 0) {
            AeaImServiceItem serviceItem = new AeaImServiceItem();
            for (int i = 0, len = saveServiceIds.length; i < len; i++) {
                String serviceId = saveServiceIds[i];
                serviceItem.setServiceItemId(UUID.randomUUID().toString());
                serviceItem.setItemVerId(itemVerId);
                serviceItem.setServiceId(serviceId);
                serviceItem.setIsDelete("0");
                serviceItem.setCreater(SecurityContext.getCurrentUserName());
                serviceItem.setCreateTime(LocalDateTime.now());
                aeaImServiceItemMapper.insertAeaImServiceItem(serviceItem);
                logger.debug("成功插入记录{}！！", JsonUtils.toJson(serviceItem));
            }
        }
    }

    private void cancelServiceRelation(String itemVerId, String[] cancelServiceIds) throws Exception {
        if (StringUtils.isNotBlank(itemVerId) && cancelServiceIds != null && cancelServiceIds.length > 0) {
            aeaImServiceItemMapper.cancelServiceRelation(itemVerId, cancelServiceIds);
            logger.debug("成功取消关联：{}！！", JsonUtils.toJson(cancelServiceIds));
        }
    }

    @Override
    public List<AeaImService> listAeaImServiceNoPageByItemVerId(String itemVerId) throws Exception {
        List<AeaImService> list = StringUtils.isBlank(itemVerId) ? new ArrayList<>() :
                aeaImServiceMapper.listAeaImServiceNoPageByItemVerId(itemVerId);
        return list;
    }
}

