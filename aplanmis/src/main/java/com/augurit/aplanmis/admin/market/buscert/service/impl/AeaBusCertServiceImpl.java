package com.augurit.aplanmis.admin.market.buscert.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.buscert.service.AeaBusCertService;
import com.augurit.aplanmis.admin.market.constants.ConfigTableInfo;
import com.augurit.aplanmis.common.domain.AeaBusCert;
import com.augurit.aplanmis.common.domain.AeaCert;
import com.augurit.aplanmis.common.mapper.AeaBusCertMapper;
import com.augurit.aplanmis.common.mapper.AeaCertMapper;
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
public class AeaBusCertServiceImpl implements AeaBusCertService {

    private static Logger logger = LoggerFactory.getLogger(AeaBusCertServiceImpl.class);

    @Autowired
    private AeaBusCertMapper aeaBusCertMapper;

    @Autowired
    private AeaCertMapper aeaCertMapper;

    @Override
    public void saveAeaBusCert(AeaBusCert aeaBusCert) throws Exception{
        aeaBusCert.setBusCertId(UUID.randomUUID().toString());
        aeaBusCertMapper.insertAeaBusCert(aeaBusCert);
    }
    @Override
    public void updateAeaBusCert(AeaBusCert aeaBusCert) throws Exception{
        aeaBusCertMapper.updateAeaBusCert(aeaBusCert);
    }
    @Override
    public void deleteAeaBusCertById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaBusCertMapper.deleteAeaBusCert(id);
    }
    @Override
    public PageInfo<AeaBusCert> listAeaBusCert(AeaBusCert aeaBusCert, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaBusCert> list = aeaBusCertMapper.listAeaBusCert(aeaBusCert);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaBusCert>(list);
    }
    @Override
    public AeaBusCert getAeaBusCertById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaBusCertMapper.getAeaBusCertById(id);
    }
    @Override
    public List<AeaBusCert> listAeaBusCert(AeaBusCert aeaBusCert) throws Exception{
        List<AeaBusCert> list = aeaBusCertMapper.listAeaBusCert(aeaBusCert);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public PageInfo<AeaCert> listAeaCert(AeaCert aeaCert, Page page, String tableName) throws Exception{
        PageHelper.startPage(page);
        List<AeaCert> list = aeaCertMapper.listAeaCert(aeaCert);
        if(list != null && list.size() > 0){
            if(StringUtils.isNotBlank(aeaCert.getConfigCertRecordId())){
                AeaBusCert aeaBusCert = new AeaBusCert();
                List<AeaBusCert> busCerts = null;
                aeaBusCert.setBusRecordId(aeaCert.getConfigCertRecordId());
                convertBusCert(tableName, aeaBusCert);
                busCerts = aeaBusCertMapper.listAeaBusCert(aeaBusCert);
                if(busCerts != null && busCerts.size() > 0){
                    List<String> configIds = new ArrayList<>();
                    for(int i=0,len=busCerts.size();i<len;i++){
                        String certId = busCerts.get(i).getCertId();
                        configIds.add(certId);
                    }
                    for(int i=0,len=list.size();i<len;i++){
                        AeaCert cert = list.get(i);
                        if(configIds.contains(cert.getCertId())){
                            cert.setIsConfig(true);
                        }
                    }
                }
            }
        }
        logger.debug("成功执行分页查询！！");
        return new PageInfo<>(list);
    }

    @Override
    public void saveRelation(String[] saveCertIds, String busRecordId,String tableName) throws Exception {
            if(saveCertIds != null && saveCertIds.length > 0 && StringUtils.isNotBlank(busRecordId)){
                AeaBusCert busCert = new AeaBusCert();
                for(int i=0,len=saveCertIds.length;i<len;i++){
                    busCert.setBusCertId(UUID.randomUUID().toString());
                    busCert.setBusRecordId(busRecordId);
                    busCert.setIsDelete("0");
                    busCert.setCertId(saveCertIds[i]);
                    busCert.setCreater(SecurityContext.getCurrentUserName());
                    busCert.setCreateTime(new Date());
                    convertBusCert(tableName, busCert);
                    aeaBusCertMapper.insertAeaBusCert(busCert);
                    logger.debug("成功插入记录{}！！", JsonUtils.toJson(busCert));
                }
            }
    }

    private void convertBusCert(String tableName, AeaBusCert busCert) {
        if(ConfigTableInfo.ITEM_BASIC_TABLE_NAME.equalsIgnoreCase(tableName)){
            busCert.setBusTableName(ConfigTableInfo.ITEM_BASIC_TABLE_NAME);
            busCert.setPkName(ConfigTableInfo.ITEM_BASIC_PK_NAME);
        }else if(ConfigTableInfo.QUAL_TABLE_NAME.equalsIgnoreCase(tableName)){
            busCert.setBusTableName(ConfigTableInfo.QUAL_TABLE_NAME);
            busCert.setPkName(ConfigTableInfo.QUAL_PK_NAME);
        }else{
            throw new RuntimeException("业务表信息缺失！");
        }
    }

    @Override
    public void cancelRelation(String[] cancelCertIds, String busRecordId,String tableName) throws Exception {
        if(cancelCertIds != null && cancelCertIds.length > 0 && StringUtils.isNotBlank(busRecordId)){
            String tbName = "";
            String pkName = "";
            if(ConfigTableInfo.ITEM_BASIC_TABLE_NAME.equalsIgnoreCase(tableName)){
                tbName = ConfigTableInfo.ITEM_BASIC_TABLE_NAME;
                pkName = ConfigTableInfo.ITEM_BASIC_PK_NAME;
            }else if(ConfigTableInfo.QUAL_TABLE_NAME.equalsIgnoreCase(tableName)){
                tbName = ConfigTableInfo.QUAL_TABLE_NAME;
                pkName = ConfigTableInfo.QUAL_PK_NAME;
            }else{
                throw new RuntimeException("业务表信息缺失！");
            }
            aeaBusCertMapper.cancelRelation(tbName,pkName,busRecordId,cancelCertIds);
            logger.debug("成功取消关联！！");
        }
    }

    @Override
    public ContentResultForm<List> getSavedCert(AeaBusCert aeaBusCert, String tableName) throws Exception {
        ContentResultForm resultForm = new ContentResultForm(true);
        if(StringUtils.isNotBlank(aeaBusCert.getBusRecordId())){
            AeaBusCert searchCert = new AeaBusCert();
            searchCert.setBusRecordId(aeaBusCert.getBusRecordId());
            List<AeaBusCert> list = null;
            convertBusCert(tableName, aeaBusCert);
            list = aeaBusCertMapper.listAeaBusCert(aeaBusCert);
            logger.debug("成功查询list{}！！",JsonUtils.toJson(list));
            if(list != null && list.size() > 0){
                List<String> certIds = new ArrayList<>();
                for(int i=0,len=list.size();i<len;i++){
                    certIds.add(list.get(i).getCertId());
                }
                resultForm.setContent(certIds);
            }
        }
        return resultForm;
    }
}

