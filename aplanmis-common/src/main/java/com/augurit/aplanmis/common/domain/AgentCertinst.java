package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author xiaohutu
 * 中介机构资质证书
 */
@Data
public class AgentCertinst implements Serializable {
    /** (证照名称)*/
    private String certinstName;
    private String certinstId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issueDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date termStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date termEnd;
    private String issueOrgId;
    private String orgName;
    private String applicant;
    private String qualLevelName;
    private String qualName;
    private String managementScope;

    private List<AeaImServiceMajor> majorTree; // 资质专业树
    private List<BscAttFileAndDir> bscAttFileAndDirs; // 证照文件列表
}
