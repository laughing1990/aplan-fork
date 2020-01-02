package com.augurit.aplanmis.front.solicit.vo;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import lombok.Data;

import java.util.List;

@Data
public class SolicitLhpsFile {

    private String fileTypeCode;//附件类型编码

    private String fileTypeName;//附件类型名称

    private List<BscAttFileAndDir> fileAndDirs;//附件集合

}
