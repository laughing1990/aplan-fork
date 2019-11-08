package com.augurit.aplanmis.supermarket.serviceLinkman.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaBusCertinst;
import com.augurit.aplanmis.common.domain.AeaHiCertinst;
import com.augurit.aplanmis.common.domain.AeaImServiceLinkman;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.mapper.AeaHiCertinstMapper;
import com.augurit.aplanmis.common.mapper.AeaImServiceLinkmanMapper;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.supermarket.serviceLinkman.service.AeaImServiceLinkmanService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* -Service服务接口实现类
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:thinkpad</li>
    <li>创建时间：2019-06-12 16:11:39</li>
</ul>
*/
@Service
@Transactional
public class AeaImServiceLinkmanServiceImpl implements AeaImServiceLinkmanService {

    private static Logger logger = LoggerFactory.getLogger(AeaImServiceLinkmanServiceImpl.class);

    @Autowired
    private AeaImServiceLinkmanMapper aeaImServiceLinkmanMapper;
    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;

    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;

    public void saveAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception{
        aeaImServiceLinkmanMapper.insertAeaImServiceLinkman(aeaImServiceLinkman);
    }
    public void updateAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception{
        aeaImServiceLinkmanMapper.updateAeaImServiceLinkman(aeaImServiceLinkman);
    }
    public void deleteAeaImServiceLinkmanById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        aeaImServiceLinkmanMapper.deleteAeaImServiceLinkman(id);
    }
    public EasyuiPageInfo<AeaImServiceLinkman> listAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaImServiceLinkman> list = aeaImServiceLinkmanMapper.listAeaImServiceLinkman(aeaImServiceLinkman);
        logger.debug("成功执行分页查询！！");
        return PageHelper.toEasyuiPageInfo(new PageInfo(list));
    }
    public AeaImServiceLinkman getAeaImServiceLinkmanById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaImServiceLinkmanMapper.getAeaImServiceLinkmanById(id);
    }
    public List<AeaImServiceLinkman> listAeaImServiceLinkman(AeaImServiceLinkman aeaImServiceLinkman) throws Exception{
        List<AeaImServiceLinkman> list = aeaImServiceLinkmanMapper.listAeaImServiceLinkman(aeaImServiceLinkman);
        logger.debug("成功执行查询list！！");
        return list;
    }

    public EasyuiPageInfo<AeaLinkmanInfo> listAeaImServiceLinkmanByServiceId(String serviceId, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaLinkmanInfo> list = aeaImServiceLinkmanMapper.listAeaImServiceLinkmanByServiceId(serviceId);
        for(int i=0;i<list.size();i++){
            AeaLinkmanInfo linkmanInfo = new AeaLinkmanInfo();
            linkmanInfo = list.get(i);
            List<AeaHiCertinstBVo> certinstlist = aeaHiCertinstMapper.getAeaHiCertinstVoByLinkmanInfoId(linkmanInfo.getLinkmanInfoId());
            linkmanInfo.setCertinsts(certinstlist);
            List<String> certinstIdList = new ArrayList<String>();
            for(AeaHiCertinst certinst:certinstlist){
                certinstIdList.add(certinst.getCertinstId());
            }
            String[] certinstIdstr = certinstIdList.toArray(new String[certinstIdList.size()]);

            linkmanInfo.setBscAttDetails(aeaHiCertinstMapper.getFilesByRecordIds("aea_hi_certinst","CERTINST_ID",null,certinstIdstr));
        }
        logger.debug("成功执行分页查询！！");
        return PageHelper.toEasyuiPageInfo(new PageInfo(list));
    }

    public EasyuiPageInfo<AeaLinkmanInfo> listAeaImServiceLinkmanByUnitInfoId(String unitInfoId, String cardNo, String auditFlag, Page page) throws Exception{
        PageHelper.startPage(page);
        if(StringUtils.isNotBlank(unitInfoId)){
            List<AeaLinkmanInfo> list = aeaImServiceLinkmanMapper.listAeaImServiceLinkmanByUnitInfoId(unitInfoId,cardNo,auditFlag);
            for(int i=0;i<list.size();i++){
                AeaLinkmanInfo aeaLinkmanInfo = list.get(i);
                AeaBusCertinst aeaBusCertinst = new AeaBusCertinst();
                aeaBusCertinst.setBusRecordId(aeaLinkmanInfo.getServiceLinkmanId());
                aeaBusCertinst.setBusTableName("aea_im_service_linkman");
                List<AeaHiCertinstBVo> certinsts = aeaHiCertinstMapper.getAeaHiCertinstByBusCertinst(aeaBusCertinst);
                for (AeaHiCertinstBVo certinstBVo : certinsts) {
                    List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstBVo.getCertinstId()});
                    certinstBVo.setCertinstDetail(attInfoList);
                }
                aeaLinkmanInfo.setCertinsts(certinsts);
            }
            return PageHelper.toEasyuiPageInfo(new PageInfo(list));
        }

        return PageHelper.toEasyuiPageInfo(new PageInfo());
    }
}

