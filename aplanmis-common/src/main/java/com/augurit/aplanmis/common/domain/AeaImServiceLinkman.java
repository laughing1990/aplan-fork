package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
* -中介服务与从业人员关联关系
*/
@Data
public class AeaImServiceLinkman implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
        private String serviceLinkmanId; // ()
        private String serviceId; // (服务ID)
        private String linkmanInfoId; // (联系人ID)
        private String unitInfoId; // (单位ID)
        private String isDelete; // (是否删除，0表示未删除，1表示已删除)
        private String creater; // (创建人)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // (创建时间)
        private String isHead; // (是否项目负责人：1 是，0 否)
        private String auditFlag; // (0 未审核，1 审核中，2 审核通过，3 审核失败，4 已发布，5 已完成,6 已过时，7 编辑中)
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date practiseDate; // (从业时间)
        private String memo;//审核意见
        @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date auditTime; // (审核时间)

        //扩展字段
        private String keyword;//查询
        private String linkmanName;//从业人员姓名
        private String serviceName;//服务名称
        private AeaImService service;//服务实体
        private String certinstName;//资质证书名称
        private String qualLevel;//资质等级
        private String applicant;//企业名称
        private AeaUnitInfo unitInfo;//企业实体
        private AeaLinkmanInfo linkmanInfo;//联系人实体
        private List<AeaHiCertinstBVo> certinst;//证照实例
        private List<ZtreeNode> majorZTree;

}
