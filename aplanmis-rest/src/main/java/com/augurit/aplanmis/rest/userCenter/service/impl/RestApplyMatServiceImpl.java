package com.augurit.aplanmis.rest.userCenter.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.mapper.AeaHiItemMatinstMapper;
import com.augurit.aplanmis.common.mapper.AeaItemMatMapper;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.augurit.aplanmis.common.utils.SimFeatureUtil;
import com.augurit.aplanmis.rest.userCenter.service.RestApplyMatService;
import com.augurit.aplanmis.rest.userCenter.vo.AutoImportParamVo;
import com.augurit.aplanmis.rest.userCenter.vo.UploadMatReturnVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RestApplyMatServiceImpl implements RestApplyMatService {

    @Autowired
    AeaHiItemMatinstMapper aeaHiItemMatinstMapper;
    @Autowired
    AeaItemMatMapper aeaItemMatMapper;
    @Autowired
    FileUtilsService fileUtilsService;


    /**
     * 一件分拣功能
     */
    @Override
    public List<UploadMatReturnVo> saveFilesAuto(AutoImportParamVo autoImportVo, HttpServletRequest request) throws Exception {
        String matIdtemp = autoImportVo.getMatIds();
        String projInfoId = autoImportVo.getProjInfoId();
        String unitInfoId = autoImportVo.getUnitInfoId();
        String matinstId = autoImportVo.getMatinstIds();
        Assert.notNull(matIdtemp, "matIds 不能为空");
        Assert.notNull(unitInfoId, "unitInfoId 不能为空");
        Assert.notNull(projInfoId, "projInfoId 不能为空");
        String[] matIds = matIdtemp.split(",");
        String[] matinstIds = matinstId.split(",");
        List<AeaHiItemMatinst> aeaHiItemMatinsts = aeaHiItemMatinstMapper.listAeaHiItemMatinstByIds(matinstIds);

        List<AeaItemMat> itemMatList = aeaItemMatMapper.listAeaItemMatByIds(matIds);
        List<AeaHiItemMatinst> returnList = new ArrayList<>();
        StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
        List<String> fileNames = new ArrayList<>();
        List<MultipartFile> files = req.getFiles("file");
        //获取文件根据文件名，与材料定义匹配新建对应的材料实例保存，如果存在材料实例跟新attcount字段每次+1
        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            boolean flag1 = true;
            for (AeaItemMat aeaItemMat : itemMatList) {
                //判断当前材料是否已经存在材料实例
                for (int i = 0; i < aeaHiItemMatinsts.size(); i++) {
                    AeaHiItemMatinst itemMatinst = aeaHiItemMatinsts.get(i);
                    if (itemMatinst.getMatId().equals(aeaItemMat.getMatId())) {
                        returnList.add(itemMatinst);
                    }
                }
                double length;
                AeaHiItemMatinst aeaHiItemMatinst = new AeaHiItemMatinst();
                //只勾选了一个材料不用名称匹配
                if (itemMatList.size() == 1) {
                    length = 1;
                } else {
                    //ie下上传文件名带真实路径
                    if (filename.lastIndexOf(File.separator) != -1) {
                        filename = filename.substring(filename.lastIndexOf(File.separator) + 1);
                    }
                    String[] filenames = StringUtils.split(filename, ".");
                    //如果材料实例名称包含材料定义名称则直接通过匹配  ---- 2019.7.22 czh
                    length = StringUtils.isNotBlank(filenames[0]) ? (filenames[0].trim().indexOf(aeaItemMat.getMatName().trim()) != -1 ? 1 : SimFeatureUtil.sim(filenames[0], aeaItemMat.getMatName())) : 0;
                }
                //大于0.8则存入数据库，否则把没有存入的返回给前端
                if (length > 0.8) {
                    boolean flag = true;
                    flag1 = false;
                    for (AeaHiItemMatinst temp : returnList) {
                        if (temp.getMatId().equals(aeaItemMat.getMatId())) {
                            flag = false;
                            aeaHiItemMatinst = temp;
                            aeaHiItemMatinst.setAttCount(aeaHiItemMatinst.getAttCount() + 1);
                            aeaHiItemMatinstMapper.updateAeaHiItemMatinst(aeaHiItemMatinst);
                        }
                    }
                    if (flag) {
                        aeaHiItemMatinst.setMatId(aeaItemMat.getMatId());
                        aeaHiItemMatinst.setProjInfoId(projInfoId);
                        aeaHiItemMatinst.setUnitInfoId(unitInfoId);
                        aeaHiItemMatinst.setMatinstId(UUID.randomUUID().toString());
                        aeaHiItemMatinst.setMatinstCode(aeaItemMat.getMatCode());
                        aeaHiItemMatinst.setMatinstName(aeaItemMat.getMatName());
                        aeaHiItemMatinst.setAttCount(1L);
                        aeaHiItemMatinst.setCreater(SecurityContext.getCurrentUserId());
                        aeaHiItemMatinst.setCreateTime(new Date());
                        aeaHiItemMatinst.setRootOrgId(SecurityContext.getCurrentOrgId());
                        aeaHiItemMatinstMapper.insertAeaHiItemMatinst(aeaHiItemMatinst);
                        returnList.add(aeaHiItemMatinst);
                    }
                    List<MultipartFile> multipartFiles = new ArrayList<>();
                    multipartFiles.add(file);
                    fileUtilsService.uploadAttachments("AEA_HI_ITEM_MATINST", "MATINST_ID", aeaHiItemMatinst.getMatinstId(), multipartFiles);
                }
            }

            if (flag1) {
                fileNames.add(filename);
            }
        }

        return getUploadMatReturnVos(returnList);
    }

    private List<UploadMatReturnVo> getUploadMatReturnVos(List<AeaHiItemMatinst> returnList) throws Exception {
        List<UploadMatReturnVo> list = new ArrayList<>();
        for (AeaHiItemMatinst matinst : returnList) {
            String matId = matinst.getMatId();
            String matinstId = matinst.getMatinstId();
            List<BscAttFileAndDir> matAttDetailByMatinstId = fileUtilsService.getMatAttDetailByMatinstId(matinstId);
            UploadMatReturnVo vo = new UploadMatReturnVo(matId, matAttDetailByMatinstId, matinstId);
            list.add(vo);
        }
        return list;
    }
}
