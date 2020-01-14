package com.augurit.aplanmis.mall.cloud.service;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttDir;
import com.augurit.agcloud.bsc.domain.BscAttForm;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.bsc.mapper.BscAttDirMapper;
import com.augurit.agcloud.bsc.sc.att.service.BscAttDirService;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.mall.cloud.vo.AttAndDirVo;
import com.augurit.aplanmis.mall.cloud.vo.CloudDirListVo;
import com.augurit.aplanmis.mall.userCenter.service.RestFileService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TemporaryStorageService {
    @Autowired
    private FileUtilsService fileUtilsService;
    @Autowired
    private BscAttDirMapper bscAttDirMapper;
    @Autowired
    private AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    private BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    private RestFileService restFileService;
    @Autowired
    private BscAttDirService bscAttDirService;

    public void uploadCloudFiles(String dirId, HttpServletRequest request) throws Exception {
        fileUtilsService.uploadAttachments("MALL_TEMPORARY_STORAGET", "MALL_TEMPORARY_STORAGET", "MALL_TEMPORARY_STORAGET", dirId, request);
    }

    public List<CloudDirListVo> getDirTree(String topOrgId) throws Exception {
        String[] unitInfoIdArr = new String[]{"MALL_TEMPORARY_STORAGET"};
        List<BscAttDir> rootDirs = bscAttDirMapper.listRootDirsByRecordIds(topOrgId, "MALL_TEMPORARY_STORAGET", unitInfoIdArr);
        return setChildDirList(rootDirs);
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

    public PageInfo<BscAttForm> getAttsByDirId(String dirId, int pageNum, int pageSize, String keyword,String rootOrgId) throws Exception {
        if(StringUtils.isBlank(dirId)) return new PageInfo<>(new ArrayList<>());
        List<BscAttForm> paramList = new ArrayList<>(1);
        BscAttForm param = new BscAttForm();
        param.setDirId(dirId);
        param.setOrgId(rootOrgId);
        param.setTableName("MALL_TEMPORARY_STORAGET");
        param.setPkName("MALL_TEMPORARY_STORAGET");
        paramList.add(param);
        PageHelper.startPage(pageNum, pageSize);
        List<BscAttForm> list = aeaHiItemMatinstMapper.listDetailCommonBatch(paramList,keyword);
        return new PageInfo<>(list);
    }

    public void deleteDirAndFiles(String dirId, String recordId,String rootOrgId) throws Exception {
        AttAndDirVo dirAndFiles = getMyCloudChildDirAndAttList(dirId,rootOrgId);
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
                AttAndDirVo childs = getMyCloudChildDirAndAttList(bscAttDir.getDirId(),rootOrgId);
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

    public AttAndDirVo getMyCloudChildDirAndAttList(String dirId,String rootOrgId) throws Exception{

        AttAndDirVo attAndDirVo = new AttAndDirVo();
        BscAttDir dirParam = new BscAttDir();
        dirParam.setParentId(dirId);
        List<BscAttDir> childDirList =  bscAttDirMapper.listBscAttDirByParentId(dirParam);
        attAndDirVo.setDirs(childDirList);
        BscAttDetail attParam = new BscAttDetail();
        attParam.setOrgId(rootOrgId);
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
}
