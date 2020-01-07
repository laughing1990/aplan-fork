package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttDetail;
import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.utils.GeneratePasswordUtils;
import com.augurit.aplanmis.common.vo.AeaHiCertinstBVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 联系人表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:44:47</li>
 * </ul>
 */
@Data
@ApiModel("人员信息")
public class AeaLinkmanInfo implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private java.lang.String linkmanInfoId; // (主键)
    private java.lang.String linkmanType; // (联系人类型。c表示个人联系人，u表示企业联系人)
    private java.lang.String linkmanCate; // (联系人类别，来自于数据字典)
    @ApiModelProperty("联系人姓名")
    private java.lang.String linkmanName; // (联系人姓名)
    private java.lang.String linkmanAddr; // (联系人住址)
    private java.lang.String linkmanOfficePhon; // (办公电话)
    @ApiModelProperty("手机号码")
    private java.lang.String linkmanMobilePhone; // (手机号码)
    private java.lang.String linkmanFax; // (传真)
    private java.lang.String linkmanMail; // (电子邮件)
    @ApiModelProperty("证件号")
    private java.lang.String linkmanCertNo; // (证件号)
    private java.lang.String isActive; // (是否启用，0表示不启用，1表示启用)
    private java.lang.String isDeleted; // (是否逻辑删除，0表示未删除，1表示已删除)
    private java.lang.String linkmanMemo; // (备注)
    private java.lang.String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private java.lang.String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private java.lang.String loginName; // (用户登录名)
    private java.lang.String loginPwd; // (？ΗΒ？Γ？Βλ)
    private java.lang.String imgUrl; // (图片地址)
    private String rootOrgId;//根组织ID
    //非表字段
    private String keyword;
    private String applicant; // (单位名称)

    private String isBindAccount;//是否绑定 1是 0 否
    private String unitInfoId;//单位ID
    private String isAdministrators;//是否管理员

    private List<BscAttFileAndDir> fileList;//证照列表-全局联系人库使用

    private String serviceLinkmanId;
    private String serviceId;
    private String isHead;
    private Date practiseDate;
    private List<String> certinstId;
    private List<AeaHiCertinstBVo> certinsts;
    private List<BscAttDetail> bscAttDetails;
    private String serviceNames;
    private String certNames;
    private String serviceName;
    private String auditFlag;
    private String unitServiceIds;//中介发布服务ID
    private String isExternal;//是否外部数据

    public void create() {
        this.setLinkmanInfoId(UUID.randomUUID().toString());
        this.setLoginName(this.getLinkmanCertNo());
        this.setLoginPwd(GeneratePasswordUtils.generatePassword(8));
        this.setCreater(SecurityContext.getCurrentUserId());
        this.setCreateTime(new Date());
        this.setIsActive("1");
        this.setIsDeleted(DeletedStatus.NOT_DELETED.getValue());
    }
}
