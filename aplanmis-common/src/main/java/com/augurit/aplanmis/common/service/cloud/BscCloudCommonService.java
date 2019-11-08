package com.augurit.aplanmis.common.service.cloud;

import com.augurit.agcloud.bsc.domain.BscAttDir;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.sc.att.service.BscAttDirService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.vo.BscAttDirParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.StoreType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class BscCloudCommonService {
    @Autowired
    private BscAttDirService bscAttDirService;
    /**
     * 11.移动文件
     * 12.文件下载(已有)
     * 13.批量下载文件
     * 14.批量移动文件
     */

    //1.新增父级文件夹
    public void insertOrUpdateRootDir(BscAttDirParamVo bscAttDirParamVo, String tableName, String pkName, String recordId) throws Exception {
        String dirName = bscAttDirParamVo.getDirName();
        String dirCode = bscAttDirParamVo.getDirCode();
        String dirDemo = bscAttDirParamVo.getDirDemo();
        String isRoot = bscAttDirParamVo.getIsRoot();
        String parentId = bscAttDirParamVo.getParentId();
        String parentDirName = bscAttDirParamVo.getParentDirName();
        String dirId = bscAttDirParamVo.getDirId();
        BscAttDir bscAttDir = initBscAttDir(dirName, dirCode, dirDemo, isRoot, parentId, parentDirName);
        if (StringUtils.isNotBlank(dirId)) {//
            bscAttDir.setDirId(dirId);
            bscAttDir.setDirSeq(StringUtils.isNotBlank(parentId) ? (parentId + "." + dirId) : dirId);
            bscAttDir.setModifier(SecurityContext.getCurrentUserName());
            bscAttDir.setModifyTime(new Date());
            bscAttDirService.updateBscAttDir(bscAttDir);
        } else {
            dirId = UUID.randomUUID().toString();
            BscAttLink link = initBscAttLink(dirId, tableName, pkName, recordId);
            bscAttDir.setDirId(dirId);
            bscAttDir.setDirSeq(StringUtils.isNotBlank(parentId) ? (parentId + "." + dirId) : dirId);
            bscAttDir.setCreater(SecurityContext.getCurrentUserName());
            bscAttDir.setCreateTime(new Date());
            bscAttDirService.saveBscAttDirAndBscAttLink(bscAttDir, link);
        }
    }

    private BscAttLink initBscAttLink(String dirId, String tableName, String pkName, String recordId) {
        BscAttLink link = new BscAttLink();
        link.setLinkId(UUID.randomUUID().toString());
        link.setTableName(tableName);
        link.setPkName(pkName);
        link.setRecordId(recordId);
        link.setDirId(dirId);
        link.setLinkType("d");//a表示附件，d代表文件夹
        return link;
    }

    private BscAttDir initBscAttDir(String dirName, String dirCode, String dirDemo, String isRoot, String parentId, String parentDirName) {
        BscAttDir bscAttDir = new BscAttDir();
        bscAttDir.setOrgId(SecurityContext.getCurrentOrgId());
        bscAttDir.setDirCode(dirCode);
        bscAttDir.setDirName(dirName);
        bscAttDir.setParentId(parentId);
        bscAttDir.setDirMemo(dirDemo);
        bscAttDir.setDirLevel("4");//0为虚拟机业务域、1为业务模块对象、2为对象分类属性、3为业务对象主键、4为业务对象下用户自定义目录
        bscAttDir.setStoreType(StoreType.MONGODB.name());//文件存储类型，包括mongodb、hdfs、windows_disk、linux_disk等，来自于数据字典
        bscAttDir.setDirNameSeq(StringUtils.isNotBlank(parentDirName) ? (parentDirName + "." + dirName) : dirName);
        bscAttDir.setIsRoot(isRoot);
        return bscAttDir;
    }


}
