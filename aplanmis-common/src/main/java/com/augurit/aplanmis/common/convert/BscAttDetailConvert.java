package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttLink;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.aplanmis.common.vo.AeaItemMatAttr;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class BscAttDetailConvert implements Serializable {

    private static final long serialVersionUID = 1L;

    public static BscAttLink createBscAttLink(String tableName, String pkName,
                                              String recordId, String detailId){

        BscAttLink link = new BscAttLink();
        link.setLinkId(UUID.randomUUID().toString());
        link.setTableName(tableName);
        link.setPkName(pkName);
        link.setRecordId(recordId);
        link.setDetailId(detailId);
        return link;
    }

    public static BscAttDetail createBscAttDetail(AeaItemMatAttr attr){

        BscAttDetail detail = new BscAttDetail();
        detail.setDetailId(attr.getIdNum());
        detail.setAttCode(attr.getIdNum());
        detail.setAttName(attr.getName());
        detail.setAttSize(attr.getSize());
        detail.setAttFormat(attr.getNameExt());
        detail.setStoreType(UploadType.MONGODB.getValue());
        detail.setAttPath(attr.getPath());
        detail.setIsEncrypt("0");
        detail.setAttDiskName(attr.getName());
        detail.setIsRelativePath("1");
        detail.setOrgId("0368948a-1cdf-4bf8-a828-71d796ba89f6");
        detail.setCreater("9ceb5b2e-ca84-4b6b-8f0d-5f60ee4361f0");
        detail.setCreateTime(new Date());
        return detail;
    }
}
