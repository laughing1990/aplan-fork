package com.augurit.aplanmis.admin.market.service.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.admin.market.service.service.AeaImServiceMajorService;
import com.augurit.aplanmis.common.domain.AeaImServiceMajor;
import com.augurit.aplanmis.common.mapper.AeaImServiceMajorMapper;
import com.augurit.aplanmis.front.constant.CommonConstant;
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
public class AeaImServiceMajorServiceImpl implements AeaImServiceMajorService {

    private static Logger logger = LoggerFactory.getLogger(AeaImServiceMajorServiceImpl.class);

    @Autowired
    private AeaImServiceMajorMapper aeaImServiceMajorMapper;

    @Override
    public void saveAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor) throws Exception{
        aeaImServiceMajor.setMajorId(UUID.randomUUID().toString());
        aeaImServiceMajor.setCreater(SecurityContext.getCurrentUserName());
        aeaImServiceMajor.setCreateTime(new Date());
        aeaImServiceMajor.setIsDelete("0");
        String seq = aeaImServiceMajor.getMajorId() + CommonConstant.SEQ_SEPARATOR ;
        String parentMajorId = aeaImServiceMajor.getParentMajorId();
        if(StringUtils.isNotBlank(parentMajorId)){
            AeaImServiceMajor parentMajor = aeaImServiceMajorMapper.getAeaImServiceMajorById(parentMajorId);
            seq = parentMajor.getMajorSeq() + seq;
        }else{
            seq = CommonConstant.SEQ_SEPARATOR + seq;
        }
        aeaImServiceMajor.setMajorSeq(seq);
        aeaImServiceMajorMapper.insertAeaImServiceMajor(aeaImServiceMajor);
    }
    @Override
    public void updateAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor) throws Exception{
        aeaImServiceMajorMapper.updateAeaImServiceMajor(aeaImServiceMajor);
    }
    @Override
    public void deleteAeaImServiceMajorById(String id) throws Exception{
        if(id == null){
            throw new InvalidParameterException(id);
        }
        aeaImServiceMajorMapper.deleteAeaImServiceMajor(id);
        logger.debug("成功删除专业类别{}",id);
    }
    @Override
    public PageInfo<AeaImServiceMajor> listAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AeaImServiceMajor> list = aeaImServiceMajorMapper.listAeaImServiceMajor(aeaImServiceMajor);
        logger.debug("成功执行分页查询！！");
        return new PageInfo<AeaImServiceMajor>(list);
    }
    @Override
    public AeaImServiceMajor getAeaImServiceMajorById(String id) throws Exception{
        if(id == null)
        throw new InvalidParameterException(id);
        logger.debug("根据ID获取Form对象，ID为：{}", id);
        return aeaImServiceMajorMapper.getAeaImServiceMajorById(id);
    }
    @Override
    public List<AeaImServiceMajor> listAeaImServiceMajor(AeaImServiceMajor aeaImServiceMajor) throws Exception{
        List<AeaImServiceMajor> list = aeaImServiceMajorMapper.listAeaImServiceMajor(aeaImServiceMajor);
        logger.debug("成功执行查询list！！");
        return list;
    }

    @Override
    public List<ZtreeNode> getMajorTreeByQualId(String QualId) throws Exception {
        AeaImServiceMajor search = new AeaImServiceMajor();
        search.setQualId(QualId);
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
        logger.debug("成功执行资质id{}下查询专业类别树节点数据！",QualId);
        return allNodes;
    }

    @Override
    public boolean checkUniqueMajorTypeCode(String majorId, String majorCode) {
        Integer count = aeaImServiceMajorMapper.checkUniqueMajorTypeCode(majorId, majorCode);
        logger.debug("成功执行查询专业类别编号{}是否存在！，查询结果：{}",majorCode,count);
        return count == null || count <= 0;
    }

    @Override
    public void batchDeleteMajor(String[] ids) throws Exception {
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                this.deleteAeaImServiceMajorById(id);
            }
        }
        logger.debug("成功批量删除专业类别！ID为：{}", JsonUtils.toJson(ids));
    }

    @Override
    public ResultForm deleteAeaImServiceMajorById(String id, Boolean delChildren) throws Exception {
        ResultForm resultForm = new ResultForm(false);
        AeaImServiceMajor major = aeaImServiceMajorMapper.getAeaImServiceMajorById(id);
        if(major == null){
            resultForm.setMessage("要删除的专业类别不存在");
        }
        //父专业ID
        String parentMajorId = major.getParentMajorId();
        if(StringUtils.isBlank(parentMajorId)){
            resultForm.setMessage("专业类别根节点不在此删除");
        }
        if(major != null && StringUtils.isNotBlank(parentMajorId)){
            //查询子节点
            List<AeaImServiceMajor> majorList = aeaImServiceMajorMapper.listChildrenAeaImServiceMajorByMajorId(id);
            if(majorList != null && majorList.size() > 0){
                for(int i=0,len=majorList.size(); i<len; i++){
                    AeaImServiceMajor childMajor = majorList.get(i);
                    if(delChildren){
                        //删除子专业
                        this.deleteAeaImServiceMajorById(childMajor.getMajorId());
                    }else{
                        if(childMajor.getParentMajorId().equals(major.getMajorId())){
                            childMajor.setParentMajorId(parentMajorId);
                        }
                        String majorSeq = childMajor.getMajorSeq();
                        if(StringUtils.isNotBlank(majorSeq)){
                            String[] ids = majorSeq.split("\\.");
                            StringBuilder sb = new StringBuilder();
                            sb.append(CommonConstant.SEQ_SEPARATOR);
                            for(String majorId: ids){
                                if(StringUtils.isNotBlank(majorId) && !majorId.equalsIgnoreCase(major.getMajorId())){
                                    sb.append(majorId).append(CommonConstant.SEQ_SEPARATOR);
                                }
                            }
                            childMajor.setMajorSeq(sb.toString());
                        }
                        this.updateAeaImServiceMajor(childMajor);
                    }
                }
                resultForm.setSuccess(true);
                resultForm.setMessage("执行删除操作成功！");
            }
            this.deleteAeaImServiceMajorById(id);
        }
        return resultForm;
    }
}

