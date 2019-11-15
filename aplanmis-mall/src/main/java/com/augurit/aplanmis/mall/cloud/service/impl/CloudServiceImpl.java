package com.augurit.aplanmis.mall.cloud.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttDirMapper;
import com.augurit.agcloud.bsc.mapper.BscAttMapper;
import com.augurit.agcloud.bsc.sc.att.service.BscAttDirService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.cloud.service.CloudService;
import com.augurit.aplanmis.mall.cloud.vo.AttAndDirVo;
import com.augurit.aplanmis.mall.cloud.vo.AttAndDirWithChildVo;
import com.augurit.aplanmis.mall.cloud.vo.CloudAttDir;
import com.augurit.aplanmis.mall.cloud.vo.CloudDirListVo;
import com.augurit.aplanmis.mall.userCenter.service.RestFileService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CloudServiceImpl implements CloudService {

    @Autowired
    BscAttDirMapper bscAttDirMapper;
    @Autowired
    BscAttMapper bscAttMapper;
    @Autowired
    AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private RestFileService restFileService;
    @Autowired
    private BscAttDirService bscAttDirService;
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;

    @Override
    public AttAndDirVo getMyCloudRootDirAndAttList(String unitInfoId, String userInfoId) throws Exception{
        AttAndDirVo attAndDirVo = new AttAndDirVo();
        List<BscAttDir> rootDirs = null;//根目录
        List<BscAttForm> rootAtts = null;//根文件
        if (StringUtils.isNotBlank(unitInfoId)){//单位
            String[] unitInfoIdArr = new String[1];
            unitInfoIdArr[0] = unitInfoId;
            rootDirs = bscAttDirMapper.listRootDirsByRecordIds(SecurityContext.getCurrentOrgId(),"AEA_UNIT_INFO",unitInfoIdArr);
            rootAtts = aeaHiItemMatinstMapper.listBscRootAttByUser("AEA_UNIT_INFO","UNIT_INFO_ID",unitInfoId,SecurityContext.getCurrentOrgId());
        }else {
            String[] userInfoIdArr = new String[1];
            userInfoIdArr[0] = userInfoId;
            rootDirs = bscAttDirMapper.listRootDirsByRecordIds(SecurityContext.getCurrentOrgId(),"AEA_LINKMAN_INFO",userInfoIdArr);
            rootAtts = aeaHiItemMatinstMapper.listBscRootAttByUser("AEA_LINKMAN_INFO","LINKMAN_INFO_ID",unitInfoId,SecurityContext.getCurrentOrgId());

        }
        attAndDirVo.setDirs(rootDirs);
        attAndDirVo.setAtts(rootAtts);
        return attAndDirVo;
    }

    @Override
    public AttAndDirVo getMyCloudChildDirAndAttList(String dirId) throws Exception{

        AttAndDirVo attAndDirVo = new AttAndDirVo();
        BscAttDir dirParam = new BscAttDir();
        dirParam.setParentId(dirId);
        List<BscAttDir> childDirList =  bscAttDirMapper.listBscAttDirByParentId(dirParam);
        attAndDirVo.setDirs(childDirList);
        BscAttDetail attParam = new BscAttDetail();
        attParam.setOrgId(SecurityContext.getCurrentOrgId());
        attParam.setDirId(dirId);
        List<BscAttDetail> childAttList =  bscAttDetailMapper.listBscAttDetail(attParam);
        List<BscAttForm> list = new ArrayList<>();
        childAttList.stream().forEach(bscAttDetail -> {
            BscAttForm bscAttForm = new BscAttForm();
            BeanUtils.copyProperties(bscAttDetail,bscAttForm);
            list.add(bscAttForm);
        });
        attAndDirVo.setAtts(list);
        return attAndDirVo;
    }


    @Override
    public AttAndDirWithChildVo getMyCloudDirAndAttList(HttpServletRequest request)throws Exception{
        AttAndDirWithChildVo attAndDirWithChildVo = new AttAndDirWithChildVo();
        AttAndDirVo attAndDirVo;
        LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
        if("1".equals(loginInfo.getIsPersonAccount())){//个人
            attAndDirVo = getMyCloudRootDirAndAttList("",loginInfo.getUserId());
        }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
            attAndDirVo = getMyCloudRootDirAndAttList("",loginInfo.getUserId());
        }else{//企业
            attAndDirVo = getMyCloudRootDirAndAttList(loginInfo.getUnitId(),"");
        }
        attAndDirWithChildVo.setAtts(attAndDirVo.getAtts());
        attAndDirWithChildVo.setDirs(getCloudAttDirs(attAndDirVo.getDirs()));

        return setChildDirAndAttList(attAndDirWithChildVo);
    }

    @Override
    public List<BscAttForm> listAttLinkAndDetailList(String keyword,HttpServletRequest request) {
        LoginInfoVo loginInfo = SessionUtil.getLoginInfo(request);
        String[] recordId = new String[1];
        if("1".equals(loginInfo.getIsPersonAccount())){//个人
            recordId[0] = loginInfo.getUserId();
            return bscAttMapper.listAttLinkAndDetailList("AEA_LINKMAN_INFO","LINKMAN_INFO_ID", recordId, org.apache.commons.lang3.StringUtils.EMPTY, SecurityContext.getCurrentOrgId(),keyword);
        }else if(StringUtils.isNotBlank(loginInfo.getUserId())){//委托人
            recordId[0] = loginInfo.getUserId();
            return bscAttMapper.listAttLinkAndDetailList("AEA_LINKMAN_INFO","LINKMAN_INFO_ID", recordId, org.apache.commons.lang3.StringUtils.EMPTY, SecurityContext.getCurrentOrgId(),keyword);
        }else{//企业
            recordId[0] = loginInfo.getUnitId();
            return bscAttMapper.listAttLinkAndDetailList("AEA_UNIT_INFO","UNIT_INFO_ID", recordId, org.apache.commons.lang3.StringUtils.EMPTY, SecurityContext.getCurrentOrgId(),keyword);
        }
    }

    @Override
    public void renameAttName(String detailId, String attName) throws Exception{
        BscAttForm bscAttForm = new BscAttForm();
        bscAttForm.setAttName(attName);
        bscAttForm.setDetailId(detailId);
        bscAttMapper.updateDetail(bscAttForm);
    }

    public AttAndDirWithChildVo setChildDirAndAttList(AttAndDirWithChildVo attAndDirWithChildVo)throws Exception{



        for (int i = 0; i < attAndDirWithChildVo.getDirs().size(); i++) {
            AttAndDirVo childAttAndDirVo =   getMyCloudChildDirAndAttList(attAndDirWithChildVo.getDirs().get(i).getDirId());
            CloudAttDir cloudAttDir = new CloudAttDir();
            AttAndDirWithChildVo childAttAndDirWithChildVo = new AttAndDirWithChildVo();
            childAttAndDirWithChildVo.setAtts(childAttAndDirVo.getAtts());
            childAttAndDirWithChildVo.setDirs(getCloudAttDirs(childAttAndDirVo.getDirs()));
            attAndDirWithChildVo.getDirs().get(i).setChildAttAndDir(childAttAndDirWithChildVo);
            if (childAttAndDirVo.getDirs()!=null&&childAttAndDirVo.getDirs().size()>0){
                setChildDirAndAttList(childAttAndDirWithChildVo);
            }
        }
        return attAndDirWithChildVo;
    }

    private List<CloudAttDir> getCloudAttDirs(List<BscAttDir> dirList) {
        List<CloudAttDir> cloudAttDirList = new ArrayList<>(dirList.size());
        dirList.stream().forEach(dir ->{
            CloudAttDir cloudAttDir = new CloudAttDir();
            BeanUtils.copyProperties(dir,cloudAttDir);
            cloudAttDirList.add(cloudAttDir);
        });
        return cloudAttDirList;
    }


    @Override
    public List<CloudDirListVo> getDirTreeByUser(String unitInfoId, String userInfoId) throws Exception{
            List<BscAttDir> rootDirs = null;//根目录
            if (StringUtils.isNotBlank(unitInfoId)){//单位
                String[] unitInfoIdArr = new String[1];
                unitInfoIdArr[0] = unitInfoId;
                rootDirs = bscAttDirMapper.listRootDirsByRecordIds(SecurityContext.getCurrentOrgId(),"AEA_UNIT_INFO",unitInfoIdArr);
                return setChildDirList(rootDirs);

            }else {
                String[] userInfoIdArr = new String[1];
                userInfoIdArr[0] = userInfoId;
                rootDirs = bscAttDirMapper.listRootDirsByRecordIds(SecurityContext.getCurrentOrgId(),"AEA_LINKMAN_INFO",userInfoIdArr);
                return setChildDirList(rootDirs);
            }
    }

    @Override
    public PageInfo<BscAttForm> getAttsByDirId(String dirID,int pageNum,int pageSize,String keyword) throws Exception{
        if(StringUtils.isBlank(dirID)) return new PageInfo<>(new ArrayList<>());
        List<BscAttForm> paramList = new ArrayList<>(1);
        BscAttForm param = new BscAttForm();
        param.setDirId(dirID);
        param.setOrgId(SecurityContext.getCurrentOrgId());
        paramList.add(param);
        PageHelper.startPage(pageNum, pageSize);
        List<BscAttForm> list = bscAttMapper.listDetailAndLinkCommonBatch(paramList,keyword);
        return new PageInfo<>(list);
    }

    private List<CloudDirListVo> setChildDirList(List<BscAttDir> rootDirs) throws Exception {
        List<CloudDirListVo> list = new ArrayList<>();
        for (BscAttDir bscAttDir : rootDirs) {
            {
                CloudDirListVo cloudDirListVo = new CloudDirListVo();
                cloudDirListVo.setDirId(bscAttDir.getDirId());
                cloudDirListVo.setDirName(bscAttDir.getDirName());
                BscAttDir paramDir = new BscAttDir();
                paramDir.setParentId(bscAttDir.getDirId());
                List<BscAttDir> childDirs = bscAttDirMapper.listBscAttDirByParentId(paramDir);
                cloudDirListVo.setChildDirs(childDirs);
                list.add(cloudDirListVo);
            }
        }
        return list;
    }

    @Override
    public void deleteDirAndFiles(String dirId, String recordId) throws Exception {
        AttAndDirVo dirAndFiles = getMyCloudChildDirAndAttList(dirId);
        List<BscAttForm> atts = dirAndFiles.getAtts();
        List<BscAttDir> dirs = dirAndFiles.getDirs();
        //删除该文件夹下的文件
        if (atts.size() > 0) {
            restFileService.delelteAttachmentByCloud(atts.stream().map(BscAttForm::getDetailId).toArray(String[]::new), recordId);
        }
        List<BscAttForm> allChildFiles = new ArrayList<>();
        //删除子文件夹
        if (dirs.size() > 0) {
            for (BscAttDir bscAttDir : dirs) {
                deleteAttDir(recordId, bscAttDir.getDirId());
                AttAndDirVo childs = getMyCloudChildDirAndAttList(bscAttDir.getDirId());
                allChildFiles.addAll(childs.getAtts());
            }
        }

        //删除子文件夹下的文件
        if (allChildFiles.size() > 0) {
            restFileService.delelteAttachmentByCloud(allChildFiles.stream().map(BscAttForm::getDetailId).toArray(String[]::new), recordId);
        }
        //删除当前文件夹
        deleteAttDir(recordId, dirId);
    }

    private void deleteAttDir(String recordId, String dirId) throws Exception {
        bscAttDirService.deleteBscAttDirById(dirId);
        BscAttLink bscAttLink = new BscAttLink();
        bscAttLink.setRecordId(recordId);
        bscAttLink.setDirId(dirId);
        aeaHiItemMatinstMapper.deleteBscAttLink(bscAttLink);
    }

    @Override
    public void moveFilesFromDir(String currentDirId, String targetDirId, String[] detailIds, String recordId) throws Exception {
        BscAttLink bscAttLink = new BscAttLink();
        bscAttLink.setRecordId(recordId);
        bscAttLink.setDirId(currentDirId);
        List<BscAttLink> list = bscAttMapper.listBscAttLink(bscAttLink);
        batchUpdateBscAttLink(list, targetDirId, detailIds);//更新关联关系
        batchUpdateBscAttForm(detailIds, targetDirId);//更新文件的文件夹ID
    }

    private void batchUpdateBscAttLink(List<BscAttLink> list, String targetDirId, String[] detailIds) throws Exception {
        if (detailIds.length == 0) return;
        List<String> detailIdList = Arrays.asList(detailIds);
        if (list.size() == 0) return;
        for (BscAttLink link : list) {
            if (detailIdList.contains(link.getDetailId())) {
                link.setDirId(targetDirId);
                bscAttMapper.updateLink(link);
            }
        }
    }

    private void batchUpdateBscAttForm(String[] detailIds,String targetDirId){
        BscAttForm form = new BscAttForm();
        form.setDirId(targetDirId);
        for (String detailId : detailIds){
            form.setDetailId(detailId);
            bscAttMapper.updateDetail(form);
        }
    }

    @Override
    public void uploadCloudFiles(String dirId, HttpServletRequest request) throws Exception {
        LoginInfoVo user = SessionUtil.getLoginInfo(request);
        if(user!=null) {
            if ("1".equals(user.getIsPersonAccount())) {//个人
                fileUtilsService.uploadAttachments("AEA_LINKMAN_INFO", "LINKMAN_INFO_ID", user.getUserId(), dirId, request);
            } else if (StringUtils.isNotBlank(user.getUserId())) {//委托人
                fileUtilsService.uploadAttachments("AEA_LINKMAN_INFO", "LINKMAN_INFO_ID", user.getUserId(), dirId, request);
            } else {//企业
                fileUtilsService.uploadAttachments("AEA_UNIT_INFO", "UNIT_INFO_ID", user.getUnitId(), dirId, request);
            }
        }
    }
}
