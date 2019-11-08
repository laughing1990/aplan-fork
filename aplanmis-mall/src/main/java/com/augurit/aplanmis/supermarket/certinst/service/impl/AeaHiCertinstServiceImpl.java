package com.augurit.aplanmis.supermarket.certinst.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.common.utils.FileUtils;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import com.augurit.aplanmis.supermarket.certinst.service.AeaHiCertinstService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
* 电子证照实例表-Service服务接口实现类
*/
@Service
@Transactional
public class AeaHiCertinstServiceImpl implements AeaHiCertinstService {

    private static Logger logger = LoggerFactory.getLogger(AeaHiCertinstServiceImpl.class);

    @Autowired
    private AeaHiCertinstMapper aeaHiCertinstMapper;
    @Autowired
    private AeaImCertinstMajorMapper  aeaImCertinstMajorMapper;
    @Autowired
    private AeaImServiceMajorMapper aeaImServiceMajorMapper;
    @Autowired
    private AeaImServiceQualMapper aeaImServiceQualMapper;
    @Autowired
    private AeaImServiceMapper aeaImServiceMapper;
    @Autowired
    private AeaBusCertinstMapper aeaBusCertinstMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    private String topOrgId;

    public void saveAeaHiCertinst(AeaHiCertinstBVo aeaHiCertinst) throws Exception{

            aeaHiCertinst.setCertinstId(UUID.randomUUID().toString());
            aeaHiCertinst.setCreater(SecurityContext.getCurrentUserName());
            aeaHiCertinst.setCreateTime(new Date());
            aeaHiCertinstMapper.insertAeaHiCertinst(aeaHiCertinst);

        List<String> list = aeaHiCertinst.getMajorId();
        if(aeaHiCertinst!=null&&list.size()>0){
            for(int i=0;i<list.size();i++){
                String majorId = list.get(i);
                AeaImCertinstMajor aeaImCertinstMajor = new AeaImCertinstMajor();
                aeaImCertinstMajor.setCertinstMajorId(UUID.randomUUID().toString());
                aeaImCertinstMajor.setCertinstId(aeaHiCertinst.getCertinstId());
                aeaImCertinstMajor.setMajorId(majorId);
                aeaImCertinstMajor.setCreater(SecurityContext.getCurrentUserName());
                aeaImCertinstMajor.setCreateTime(new Date());
                aeaImCertinstMajor.setIsDelete("0");
                aeaImCertinstMajorMapper.insertAeaImCertinstMajor(aeaImCertinstMajor);
            }
        }


    }
    public void updateAeaHiCertinst(AeaHiCertinst aeaHiCertinst) throws Exception{
        aeaHiCertinstMapper.updateAeaHiCertinst(aeaHiCertinst);
    }
    public boolean deleteAeaHiCertinstById(String certinstId) throws Exception{
        if(StringUtils.isNotBlank(certinstId)){
            try{
                aeaBusCertinstMapper.deleteAeaBusCertinstByCertinstId(certinstId);
                aeaHiCertinstMapper.deleteAeaHiCertinst(certinstId);
                return true;
            }catch(Exception e){
                return false;
            }

        }else{
            return false;
        }
    }
    @Override
    public boolean deleteContractFile(String detailId)throws Exception{

        if(StringUtils.isNotBlank(detailId)) {
            String[] detailIds = detailId.split(",");

            if(detailIds!=null && detailIds.length>0) {
                return FileUtils.deleteFiles(detailIds);
            }
        }

        return false;

    }
    public PageInfo<AeaHiCertinst> listAeaHiCertinst(AeaHiCertinst aeaHiCertinst, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaHiCertinst> list = aeaHiCertinstMapper.listAeaHiCertinst(aeaHiCertinst);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaHiCertinst>(list);
    }
    public AeaHiCertinst getAeaHiCertinstById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaHiCertinstMapper.getAeaHiCertinstById(id);
    }
    public List<AeaHiCertinst> listAeaHiCertinst(AeaHiCertinst aeaHiCertinst) throws Exception{
        List<AeaHiCertinst> list = aeaHiCertinstMapper.listAeaHiCertinst(aeaHiCertinst);
        logger.debug("成功执行查询list！！");
        return list;
    }
    public PageInfo<AeaHiCertinst> getAeaImMajorLevelAndCertinstByServiceId(String serviceId, String unitInfoId, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaHiCertinst> list = aeaHiCertinstMapper.getAeaImMajorLevelAndCertinstByServiceId(serviceId,unitInfoId);
        for(int i=0;i<list.size();i++){
            AeaHiCertinst aeaHiCertinst = new AeaHiCertinst();
            aeaHiCertinst = list.get(i);
            aeaHiCertinst.setBscAttDetails(aeaHiCertinstMapper.getFilesByRecordIds("aea_hi_certinst","CERTINST_ID",null,new String[]{aeaHiCertinst.getCertinstId()}));
        }

        return new PageInfo<AeaHiCertinst>(list);
    }

    @Override
    public List<ZtreeNode> getMajorTreeByQualId(String qualId) throws Exception {
        AeaImServiceMajor search = new AeaImServiceMajor();
        search.setQualId(qualId);
        search.setRootOrgId(topOrgId);
        List<AeaImServiceMajor> majorList = aeaImServiceMajorMapper.listMajorTree(search);
        List<ZtreeNode> allNodes = new ArrayList<>();
        if(majorList != null && majorList.size() > 0){
            List<String> parentIdList = new ArrayList<>();
            for(AeaImServiceMajor m:majorList){
                if(StringUtils.isNotBlank(m.getParentMajorId())){
                    parentIdList.add(m.getParentMajorId());
                }
            }
            for(int i=0,len=majorList.size();i<len;i++){
                AeaImServiceMajor major = majorList.get(i);
                ZtreeNode node = new ZtreeNode();
                String id = major.getMajorId();
                node.setId(id);
                node.setName(major.getMajorName());
                node.setpId(major.getParentMajorId());
                node.setType("majorType");
                node.setNocheck(true);
                node.setIsParent(StringUtils.isBlank(major.getParentMajorId())?true:parentIdList.contains(id));
                node.setOpen(true);
                allNodes.add(node);
            }
        }
        logger.debug("成功执行资质id{}下查询专业类别树节点数据！",qualId);
        return allNodes;
    }
    @Override
    public List<AeaImServiceQual> listAeaImServiceQual(String serviceId) throws Exception{
        List<AeaImServiceQual> list = aeaImServiceQualMapper.getAeaImServiceQualListByServiceId(serviceId);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<AeaImService> listAeaImService() throws Exception{
       return aeaImServiceMapper.getAeaImServiceAllList();
    }

    @Override
    public List<AeaHiCertinstBVo> listCertinstLibrary(String type, String typeId, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaHiCertinstBVo> aeaHiCertinstBVoList = aeaHiCertinstMapper.listAeaHiCertinstByBusRecord(type, typeId);

        if (aeaHiCertinstBVoList != null && aeaHiCertinstBVoList.size() > 0) {
            for (AeaHiCertinstBVo aeaHiCertinstBVo : aeaHiCertinstBVoList) {
                List<BscAttFileAndDir> attInfoList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_CERTINST", "CERTINST_ID", new String[]{aeaHiCertinstBVo.getCertinstId()});
                aeaHiCertinstBVo.setCertinstDetail(attInfoList);
            }
        }

        return aeaHiCertinstBVoList;
    }

    @Override
    public boolean deleteCertinst(String certinstId) throws Exception {
        // 删除业务与证照关联
        aeaBusCertinstMapper.deleteByCertinstId(certinstId);
        // 删除资质专业与证照关联
        aeaImCertinstMajorMapper.deleteAeaImCertinstMajorByCertinstId(certinstId);
        // 删除证照实例
        aeaHiCertinstMapper.deleteAeaHiCertinst(certinstId);
        // 删除证照附件
        List<BscAttFileAndDir> attFileAndDirList = bscAttDetailMapper.searchFileAndDirsSimple(null, SecurityContext.getCurrentOrgId(), "AEA_HI_CERTINST", "CERTINST_ID", new String[]{certinstId});
        List<String> detailIds = new ArrayList<>();
        if (attFileAndDirList != null && attFileAndDirList.size() > 0) {
            for (BscAttFileAndDir fileAndDir : attFileAndDirList) {
                detailIds.add(fileAndDir.getFileId());
            }
        }
        String[] array = detailIds.toArray(new String[detailIds.size()]);
        FileUtils.deleteFiles(array);
        return true;
    }


}

