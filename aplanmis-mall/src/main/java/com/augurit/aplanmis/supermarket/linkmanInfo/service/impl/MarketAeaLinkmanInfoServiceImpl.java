package com.augurit.aplanmis.supermarket.linkmanInfo.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaBusCertinst;
import com.augurit.aplanmis.common.domain.AeaImServiceLinkman;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.domain.AeaUnitLinkman;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.utils.Md5Utils;
import com.augurit.aplanmis.supermarket.linkmanInfo.service.AeaLinkmanInfoService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 联系人表-Service服务接口实现类
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:thinkpad</li>
 * <li>创建时间：2019-06-12 20:19:45</li>
 * </ul>
 */
@Service
@Transactional
public class MarketAeaLinkmanInfoServiceImpl implements AeaLinkmanInfoService {

    private static Logger logger = LoggerFactory.getLogger(MarketAeaLinkmanInfoServiceImpl.class);

    @Autowired
    private AeaLinkmanInfoMapper aeaLinkmanInfoMapper;
    @Autowired
    private AeaImServiceLinkmanMapper aeaImServiceLinkmanMapper;
    @Autowired
    private AeaUnitLinkmanMapper aeaUnitLinkmanMapper;
    @Autowired
    private AeaBusCertinstMapper aeaBusCertinstMapper;
    @Autowired
    private AeaImUnitServiceMapper aeaImUnitServiceMapper;
    @Autowired
    private AeaImUnitServiceLinkmanMapper aeaImUnitServiceLinkmanMapper;

    public AeaLinkmanInfo insertLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception {

        try {
            aeaLinkmanInfo.setLinkmanInfoId(UUID.randomUUID().toString());
            aeaLinkmanInfo.setCreater("admin");
            aeaLinkmanInfo.setCreateTime(new Date());
            aeaLinkmanInfo.setIsActive("1");
            aeaLinkmanInfo.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
            aeaLinkmanInfoMapper.insertAeaLinkmanInfo(aeaLinkmanInfo);

            AeaUnitLinkman aeaUnitLinkman = new AeaUnitLinkman();
            aeaUnitLinkman.setUnitLinkmanId(UUID.randomUUID().toString());
            aeaUnitLinkman.setUnitInfoId(aeaLinkmanInfo.getUnitInfoId());
            aeaUnitLinkman.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
            aeaUnitLinkman.setCreater("admin");
            aeaUnitLinkman.setCreateTime(new Date());
            aeaUnitLinkmanMapper.insertAeaUnitLinkman(aeaUnitLinkman);
            return aeaLinkmanInfo;
        } catch (Exception e) {
            aeaLinkmanInfo = null;
            return aeaLinkmanInfo;
        }


    }

    public AeaLinkmanInfo updateLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception {

        try {
            aeaLinkmanInfo.setCreateTime(new Date());
            if (aeaLinkmanInfo.getLoginPwd() != "" || aeaLinkmanInfo.getLoginPwd() != null) {
                String pwd = Md5Utils.encrypt32(aeaLinkmanInfo.getLoginPwd());
                aeaLinkmanInfo.setLoginPwd(pwd);
            }
            aeaLinkmanInfoMapper.updateAeaLinkmanInfo(aeaLinkmanInfo);
            return aeaLinkmanInfo;
        } catch (Exception e) {
            aeaLinkmanInfo = null;
            return aeaLinkmanInfo;
        }
    }


    public boolean saveAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception {
        try {


            AeaImServiceLinkman aeaImServiceLinkman = new AeaImServiceLinkman();
            aeaImServiceLinkman.setServiceLinkmanId(UUID.randomUUID().toString());
            aeaImServiceLinkman.setServiceId(aeaLinkmanInfo.getServiceId());
            aeaImServiceLinkman.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
            aeaImServiceLinkman.setUnitInfoId(aeaLinkmanInfo.getUnitInfoId());
            aeaImServiceLinkman.setCreater(SecurityContext.getCurrentUserName());
            aeaImServiceLinkman.setCreateTime(new Date());
            aeaImServiceLinkman.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
            aeaImServiceLinkman.setAuditFlag("2");
            aeaImServiceLinkman.setIsHead(aeaLinkmanInfo.getIsHead());
            aeaImServiceLinkman.setPractiseDate(aeaLinkmanInfo.getPractiseDate());
            aeaImServiceLinkmanMapper.insertAeaImServiceLinkman(aeaImServiceLinkman);

            List<String> list = aeaLinkmanInfo.getCertinstId();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    AeaBusCertinst aeaBusCertinstByService = new AeaBusCertinst();
                    aeaBusCertinstByService.setBusCertinstId(UUID.randomUUID().toString());
                    aeaBusCertinstByService.setBusTableName("aea_im_service_linkman");
                    aeaBusCertinstByService.setCertinstId(list.get(i));
                    aeaBusCertinstByService.setAuditFlag("2");
                    aeaBusCertinstByService.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                    aeaBusCertinstByService.setCreater(SecurityContext.getCurrentUserName());
                    aeaBusCertinstByService.setCreateTime(new Date());
                    aeaBusCertinstByService.setPkName("SERVICE_LINKMAN_ID");
                    aeaBusCertinstByService.setBusRecordId(aeaImServiceLinkman.getServiceLinkmanId());
                    aeaBusCertinstMapper.insertAeaBusCertinst(aeaBusCertinstByService);
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }


    }

    public void updateAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception {
        try {

            AeaImServiceLinkman aeaImServiceLinkman = new AeaImServiceLinkman();
            aeaImServiceLinkman.setServiceLinkmanId(aeaLinkmanInfo.getServiceLinkmanId());
            aeaImServiceLinkman.setServiceId(aeaLinkmanInfo.getServiceId());
            aeaImServiceLinkman.setLinkmanInfoId(aeaLinkmanInfo.getLinkmanInfoId());
            aeaImServiceLinkman.setAuditFlag("2");
            aeaImServiceLinkman.setIsHead(aeaLinkmanInfo.getIsHead());
            aeaImServiceLinkman.setPractiseDate(aeaLinkmanInfo.getPractiseDate());
            aeaImServiceLinkmanMapper.updateAeaImServiceLinkman(aeaImServiceLinkman);

            List<String> list = aeaLinkmanInfo.getCertinstId();
            aeaBusCertinstMapper.deleteAeaBusCertinstByTableNameAndRecordId("aea_im_service_linkman", aeaLinkmanInfo.getServiceLinkmanId());
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    AeaBusCertinst aeaBusCertinstByService = new AeaBusCertinst();
                    aeaBusCertinstByService.setBusCertinstId(UUID.randomUUID().toString());
                    aeaBusCertinstByService.setBusTableName("aea_im_service_linkman");
                    aeaBusCertinstByService.setCertinstId(list.get(i));
                    aeaBusCertinstByService.setAuditFlag("2");
                    aeaBusCertinstByService.setIsDelete(DeletedStatus.NOT_DELETED.getValue());
                    aeaBusCertinstByService.setCreater(SecurityContext.getCurrentUserName());
                    aeaBusCertinstByService.setCreateTime(new Date());
                    aeaBusCertinstByService.setPkName("SERVICE_LINKMAN_ID");
                    aeaBusCertinstByService.setBusRecordId(aeaImServiceLinkman.getServiceLinkmanId());
                    aeaBusCertinstMapper.insertAeaBusCertinst(aeaBusCertinstByService);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAeaLinkmanInfoById(String serviceLinkmanId, String unitServiceId, String unitServiceLinkmanId) throws Exception {

        try {
            aeaImServiceLinkmanMapper.deleteAeaImServiceLinkman(serviceLinkmanId);
            aeaImUnitServiceMapper.deleteAeaImUnitService(unitServiceId);
            aeaImUnitServiceLinkmanMapper.deleteAeaImUnitServiceLinkmanByUnitServiceId(unitServiceLinkmanId);
        } catch (Exception e) {

        }

    }

    public PageInfo<AeaLinkmanInfo> listAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaLinkmanInfo> list = aeaLinkmanInfoMapper.listAeaLinkmanInfo(aeaLinkmanInfo);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaLinkmanInfo>(list);
    }

    public List<AeaLinkmanInfo> getAeaLinkmanInfoById(String id) throws Exception {
        if (id == null)
            throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaLinkmanInfoMapper.getAeaLinkmanInfoAndUnitInfoByLinkmanInfoId(id);
    }

    public List<AeaLinkmanInfo> getAeaLinkmanInfoByUnitInfoId(String unitInfoId, Integer isAll) throws Exception {
        if (unitInfoId == null)
            throw new InvalidParameterException(unitInfoId);
        logger.debug("根据ID获取Form对象，ID为：{}", unitInfoId);
        return aeaLinkmanInfoMapper.getAeaLinkmanInfoByUnitInfoId(unitInfoId, isAll);
    }

    public List<AeaLinkmanInfo> listAeaLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo) throws Exception {
        List<AeaLinkmanInfo> list = aeaLinkmanInfoMapper.listAeaLinkmanInfo(aeaLinkmanInfo);
        logger.debug("成功执行查询list！！");
        return list;
    }

    public AeaLinkmanInfo getOneById(String id) {
        return aeaLinkmanInfoMapper.getAeaLinkmanInfoById(id);
    }

    public int updateAeaUnitLinkman(AeaUnitLinkman aeaUnitLinkman) {
        return aeaLinkmanInfoMapper.updateAeaUnitLinkman(aeaUnitLinkman);
    }

    @Override
    public void deleteUnitLinkmanInfo(AeaUnitLinkman aeaUnitLinkman) {
        aeaLinkmanInfoMapper.deleteUnitLinkmanInfo(aeaUnitLinkman);
    }

    @Override
    public int batchDeleteAeaLinkmanInfo(List<String> ids) throws Exception {
        return aeaLinkmanInfoMapper.batchDeleteAeaLinkmanInfo(ids);
    }

    @Override
    public int batchInsertAeaLinkmanInfo(List<AeaLinkmanInfo> linkmanInfos) throws Exception {
        return aeaLinkmanInfoMapper.batchInsertAeaLinkmanInfo(linkmanInfos);
    }

    @Override
    public int batchInsertAeaUnintLinkman(List<AeaUnitLinkman> unitLinkmen) throws Exception {
        return aeaLinkmanInfoMapper.batchInsertAeaUnintLinkman(unitLinkmen);
    }
}

