package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.constants.NeedStateStatus;
import com.augurit.aplanmis.common.constants.PublishStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * -模型
 */
@Data
@ApiModel("事项实体")
public class AeaItemBasic implements Serializable {

    private static final long serialVersionUID = 1L;

    private String itemBasicId;// (事项内容ID，主键ID)
    private String itemId; // (关联aea_item表id)
    private String itemVerId;//(关联aea_item_ver表id)
    private String itemCode; // (事项编号)
    @ApiModelProperty(value = "事项名称")
    private String itemName; // (事项名称)
    private String orgId; // (组织ID)
    private String itemType; // 事项性质小分类，来自于数据字典
    private String bjType; // (承诺时限单位)
    private Long acceptMode; // (排序号)
    private String handleArticle; // (图标CSS样式)
    private String handleFlow; // (背景CSS样式)
    private Double dueNum; // (办理时限，以工作日为单位)
    private String sendResultMode; // (办理结果送达方式)
    private byte[] docTemplate; // (文书模版)
    private byte[] applyTableDemo; // (申请表范本)
    private byte[] applyTableTemplate; // (申请表模版)
    private String standardItemCode; // (对应标准审批事项编码)
    private String itemMemo; // (备注说明)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; // (修改时间)
    private String appId; // (所属业务流程模板)
    private String itemDirId;//所属事项目录ID
    private String innerAppId;//（事项审批部门）内部业务流程模板定义ID
    private String itemProperty; // 办件类型
    private String directorycode; // (临时事项编码)
    private String directoryId; // (通用事(子)项ID)
    private String basecode; // (事项基本编码)
    private String wtbm; // (网厅编码)
    private String sqjb; // (事权级别)
    private String sfzhlr; // (是否暂缓录入)
    private String slyj; // (设立依据)
    private String bbh; // (版本号)
    private String sxmlzt; // (事项状态: 1 在用 2暂停 3取消 4删除)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fbsj; // (发布时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sssj; // (实施时间)
    private String isold; // (是否旧目录系统事项)
    private String sfwsbl; // (是否网上办理)
    private String bwsblly; // (不网上办理理由)
    private String unonlinereasonother; // (其他不网上办理理由)
    private String sfczwtxftzssqk; // (是否存在委托、下放、停止实施情况)
    private String fzcd; // (复杂程度)
    private String sscode; // (实施编码)
    private String sxglfslx; // (事项管理方式类型（委托、下放、停止实施）)
    private String wtfqfx; // (委托放权放向（SXGLFSLX 为委托、放权时必填）)
    private String wtfqqtsm; // (委托方式其他说明（WTFQFX  为其他时填写）)
    private String slfs; // (受理方式)
    private String isNeedState;//是否分情形（0 不分 情形 1 分情形  分情形时情形才能填）
    private String isMilestoneItem; // 是否里程碑事项,0表示否,1表示阶段内所有事项办结才算阶段办结,2表示阶段内任一事项办结阶段就办结
    private String outerSystemHandle;   //是否本系统处理事项，0表示由由本系统处理，1表示由第三方系统处理
    private String outerSystemDesc;     //由第三方系统处理说明
    private String outerSystemUrl;      //跳转至第三方系统的URL地址
    private String isAsyn; // (是否同步的数据 1是 0 否)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date asynTime; // (同步时间)
    private String isLocal;  // 是否本地事项
    private String isFee; //是否收费 0 不收费 1 收费
    @ApiModelProperty(value = "服务对象")
    private String xkdx; // (服务对象)
    private String itemAlias; // (事项别名)
    private String isNeedFrontCond; // (是否有前置条件，0表示没有 1表示有)
    private String areaCode; // (行政区划代码)
    private String areaCodeName; // (行政区划名称)
    private String kpItemId; // 开普事项库事项id
    private String itemNature; //事项性质大分类：0-行政事项，8-中介服务事项，9-服务协同，6-市政公用服务
    private String sfsxgzcnz; // 0: 实行审批制  1: 实行告知承诺制 同一事项事项审批制与告知承诺制，应赋予不同的事项编号
    private String rootOrgId;//根组织ID
    private String isCatalog;//是否标准事项  1标准事项 0 实施事项
    private String guideOrgName;

    /**
     * 行政区域
     */
    private String regionId;

    /**
     * 事项分类唯一标记（仅供工作流使用）
     */
    private String itemCategoryMark;

    /**
     * 实施事项换算方式 0 按照审批行政区划和属地行政区划换算 1 仅按照审批行政区划换算
     */
    private String itemExchangeWay;

    /**
     * 是否为链接式办事指南 1 是 0 否
     */
    private String isLink;

    /**
     * 是否允许设置"一张表单" 0 禁用 1允许
     */
    private String useOneForm;

    /**
     * 是否允许设置前置检查事项  0 禁止  1允许
     */
    private String isCheckItem;

    /**
     * 是否前置检测阶段事项扩展表单  0 禁止  1允许
     */
    private String isCheckPartform;

    /**
     * 是否前置检测项目信息  0 禁止  1允许
     */
    private String isCheckProj;

    /**
     * 是否共享 0不共享 1共享（仅供中介超市使用）
     */
    private String isShare;

    //非表字段
    @ApiModelProperty(value = "审批组织名称")
    private String orgName;
    private List<AeaItemMat> matList;//事项材料列表
    private String isDone;//是否已办 1 是 0 否
    private String isRecommended;//是否推荐 1是 0否
    private String sortNo;//排序号
    private String regionName;

    // 扩展字段
    private String[] orgIds; // 组织集合
    private String keyword;
    private String orgCode;
    private Double verNum;
    private String verNumVo;
    private String parentItemId;
    private String itemSeq;
    private String isDeleted;
    private String hasNoActiveVer;
    private PublishStatus itemVerStatus;
    private AeaItemExeorg aeaItemExeorg;
    private AeaItemServiceServe aeaItemServiceServe;
    private AeaItemAcceptRange aeaItemAcceptRange;
    private AeaItemServiceFlow aeaItemServiceFlow;
    private List<AeaItemInout> aeaItemInout;
    private List<AeaItemServiceConsulting> aeaItemServiceConsulting;
    private AeaServiceWindow aeaServiceWindow;
    private AeaItemServiceCharge aeaItemServiceCharge;
    private List<AeaItemAuxService> aeaItemAuxService;
    private List<AeaServiceLegalClause> aeaServiceLegalClause;
    private List<AeaItemRightsObligations> aeaItemRightsObligations;
    private AeaItemLegalRemedy aeaItemLegalRemedy;

    //拓展字段 20190220 xiaohutu com/augurit/aplanmis/front/window/service/impl/WindowAcceptServiceImpl.java  gtreeItemAsyncZTree()
    private String id;
    private String name;
    private String PId;
    private String type;
    private Boolean open;
    private Boolean isParent;
    private Boolean nocheck;

    public AeaItemBasic() {

    }

    public String getpId() {
        return PId;
    }

    public void setpId(String PId) {
        this.PId = PId;
    }

    // 是否需要分情形
    public boolean needState() {
        return NeedStateStatus.NEED_STATE.getValue().equals(this.getIsNeedState());
    }

    //20190418 新增字段
    /*
    是否必选事项
    是否必选事项 0表示否 1表示是
     */
    private String isOptionItem;
    /**
     * 事项排序字段
     */
    private String isRecommend;//是否推荐 0 不推荐 1推荐：未办理【isDone=0】且耗时超过10个工作日的

    //工程建设项目对接省平台申报事项信息使用
    private String projName;//项目名称
    private String localCode;//地方代码
    private String centralCode;//中央代码
    private String applicant;//项目(法人)单位
    private String idCard;//单位证照号码
    private String approveOrgId;//审批部门id

    /* 中介服务事项关联行政事项使用  */
    private String currItemId;
    private String[] searchItemIds;
    private Boolean isCheck = false;

    /*省平台对接使用*/
    private String projCode;//有中央为中央代码，否则为地方代码

    private String stageName;//阶段名称

    private String isNode; // 主线还是辅线

    //事项实例
    @ApiModelProperty(value = "事项实例")
    private AeaHiIteminst aeaHiIteminst;

    @ApiModelProperty(name = "baseItemVerId", value = "标准事项版本ID", dataType = "string")
    private String baseItemVerId;
    @ApiModelProperty(name = "baseItemName", value = "标准事项名称", dataType = "string")
    private String baseItemName;
    @ApiModelProperty(name = "coreStateList", value = "并行事项情形list")
    private List<AeaItemState> coreStateList;
    @ApiModelProperty(name = "paraStateList", value = "并联事项情形list")
    private List<AeaItemState> paraStateList;

    private String[] regionIds;//行政区划ID和建设地点所对应的区划ID
    @ApiModelProperty(name = "carryOutItems", value = "实施事项列表")
    private List<AeaItemBasic> carryOutItems;
    @ApiModelProperty(name = "currentCarryOutItem", value = "默认实施事项")
    private AeaItemBasic currentCarryOutItem;
    @ApiModelProperty(name = "childAeaItemBasic", value = "子事项")
    private List<AeaItemBasic> childAeaItemBasic;

    //实施事项对应的受理部门咨询电话
    @ApiModelProperty(value = "咨询电话")
    private String orgTel;
    @ApiModelProperty(value = "服务对象文本")
    private String serveTypeText;


    public AeaItemBasic(String itemNature, String isDeleted, String rootOrgId) {
        this.itemNature = itemNature;
        this.isDeleted = isDeleted;
        this.rootOrgId = rootOrgId;
    }

}
