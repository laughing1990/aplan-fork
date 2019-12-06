/*
 * 审核界面
 */
var tableName = 'AEA_HI_APPLYINST';
var pkName = 'APPLYINST_ID';
//选择办理人树
var zTreeSetting = {
  data: {
    simpleData: {
      enable: true,
      idKey: "id",
      pIdKey: "pId"
    }
  },
  check: {
    enable: true,
    chkboxType: {"Y": "", "N": ""},
    chkStyle: "checkbox",
    radioType: "all"   //对所有节点设置单选
  },
  //使用异步加载，必须设置 setting.async 中的各个属性
  async: {
    //设置 zTree 是否开启异步加载模式
    enable: true,
    autoParam: ["id", "dataType"],
    dataType: "json",
    type: "get"
    // url:ctx+"/front/task/getAssigneeRangeZTree.do?assigneeRangeKey=$ROLE.a73fc518-7ec6-4d74-8f06-4786b45f01c9,$POS.801dc783-2e5f-4cba-b4f1-c4f3d0094559,$POS.97f92189-6aad-4f5f-bd2f-569960bcc880," + assigneeRangeKey
    // url:ctx+"/front/task/getAssigneeRangeZTree.do?assigneeRangeKey=" + assigneeRangeKey + "&keyword=" + assigneeSearchKeyword
  },
  //4、zTree树加载完成后相关回调函数，
  // onAsyncSuccess 处理初始化默认选择办理人数据同步
  // onClick 和 onCheck 根据环节办理人个数要求 限制树的选择，并同时同步一选择办理人数据。
  callback: {
    onAsyncSuccess: function (e, treeId, treeNode) {
      if (vm.checkNotNull(vm.tempDefaultSelectedAssigneeId)) {
        if (vm.tempDefaultSelectedAssigneeId.indexOf(vm.ID_SPLIT) != -1) {
          var texts = vm.nextTaskAssignee.split(vm.TEXT_SPLIT);
          var ids = vm.tempDefaultSelectedAssigneeId.split(vm.ID_SPLIT);
          for (var i = 0; i < ids.length; i++) {
            vm.addAssignee(ids[i], texts[i]);
            vm.toggleCheckAssignee(ids[i], true);
          }
        } else {
          vm.addAssignee(vm.tempDefaultSelectedAssigneeId, vm.nextTaskAssignee);
          vm.toggleCheckAssignee(vm.tempDefaultSelectedAssigneeId, true);
        }
      }
    },
    onClick: function () {
      var nodes = vm.zTreeObj.getSelectedNodes();
      for (var i = 0; i < nodes.length; i++) {
        if (nodes[i].dataType != 'USER' || nodes[i].id == "userRoot") continue;
        if (nodes[i].checked) {
          vm.zTreeObj.checkNode(nodes[i], false, false);
          vm.uncheckRemoveAssignee(nodes[i].textValue);
        } else {
          vm.zTreeObj.checkNode(nodes[i], true, false);
          //改为直接在树上勾选时添加办理人
          vm.addAssignee(nodes[i].textValue, nodes[i].name);
          //根据节点配置，过滤单选
          if (!vm.sendTaskInfo.multiTask) {
            var nodes1 = vm.zTreeObj.getCheckedNodes(true);
            for (var j = 0; j < nodes1.length; j++) {
              if (nodes[i].id != nodes1[j].id) {
                vm.zTreeObj.checkNode(nodes1[j], false, false);
              }
            }
          }
        }
      }
    },
    onCheck: function (e, treeId, treeNode) {
      //根据节点配置，过滤单选
      if (!vm.sendTaskInfo.multiTask && treeNode.checked) {
        var nodes = vm.zTreeObj.getCheckedNodes(true);
        for (var i = 0; i < nodes.length; i++) {
          if (treeNode.id != nodes[i].id) {
            vm.zTreeObj.checkNode(nodes[i], false, false);
          }
        }
      }
      //同步选择办理人
      if (treeNode.checked) {
        vm.addAssignee(treeNode.textValue, treeNode.name);
      } else {
        vm.uncheckRemoveAssignee(treeNode.textValue);
      }
      //暂时在check时去掉节点的选择状态
      var nodes = vm.zTreeObj.getSelectedNodes();
      for (var i = 0; i < nodes.length; i++) {
        vm.zTreeObj.cancelSelectedNode(nodes[i]);
      }
    }
  }
};

var vm = new Vue({
  el: '#approvePage',
  data: function () {
    var checkMissValue = function (rule, value, callback) {
      if (value === '' || value === undefined || value === null) {
        callback(new Error('该输入项为必输项！'));
      } else if (value.toString().trim() === '') {
        callback(new Error('该输入项为必输项！'));
      } else {
        callback();
      }
    };
    var checkPhoneNum = function (rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('该输入项为必输项！'));
      } else if (value) {
        var flag = !/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/.test(value) && !(/^1(3|4|5|6|7|8|9)\d{9}$/.test(value));
        if (flag) {
          return callback(new Error('格式不正确'));
        } else {
          callback();
        }
      } else {
        callback();
      }
    };
    return {
      iteminstName: '',
      fileLoading: false, // 文件材料是否显示加载动画
      // ---------- 需要保存的数据 start
      iteminstId: '', // 事项实例id，从baseInfo.js获取后保存，如果是审批人员，查询到的是审批人员对应的单个事项；窗口人员；并联会有多个
      iteminstProcessinstId: '',// 事项子流程实例ID
      processViewLoading: false,
      processStatesLoading: false,
      formTabsContentLoading: true,
      winHeight: document.documentElement.clientHeight - 120,
      isSeriesinst: 0, // 判斷是否是串联还是并联  1为是串   1: 表示事项审批; 0: 表示阶段审批
      isApprover: 0,//  0: 窗口人员; 1: 审批人员
      //审批界面初始化参数
      appId: null,//业务流程模板id，如果存在则表示起草界面，还不是审批界面
      appComment: null,//业务流程模板名称，表示流程的执行的内容
      appinstId: null,//业务流程模板实例id
      processInstanceId: null,//流程实例id
      isSuspended: null,//流程是否挂起 1挂起0没挂起
      flowdefKey: null,//流程定义key（流程编号）
      taskId: null,//任务实例id
      taskName: null,//任务实例名称
      viewId: null,//视图id，从哪个视图进入审批界面
      busRecordId: null,//业务数据id，单项时为iteminstId，并联==undefined
      masterEntityKey: null,//流程关联的主业务表数据id---applyinstId 申请实例ID
      appFlowdefId: null,//业务流程模板和流程关联id
      flowModel: null,//模板元素权限配置模式
      currTaskDefId: null,//当前任务节点定义id（节点编号）
      currProcDefVersion: 0,//当前流程实例版本号
      isCheck: null,//是否流程查看，即已办、办结的标志
      formJson: null,//流程关联表单信息json格式
      formData: [],//流程关联表单信息数组格式
      initError: null,//审批界面初始化标识
      _tableName: null,//附件管理中附件上传参数之一，默认等于masterEntityKey
      _pkName: null,//附件管理中附件上传参数之一，默认等于masterEntityKey
      recordId: null,//附件管理中附件上传参数之一，默认等于masterEntityKey
      recordIds: null,//附件管理中附件查询参数，默认等于流程所有任务实例id用逗号拼接成的字符串
      attLink: {
        tableName: 'AEA_HI_TASK',
        pkName: 'ID_',
        recordId: null,
      },//attLink用于附件关联多个业务ID
      //审批界面业务逻辑参数
      buttonData: [], // 动态按钮数据
      showMenuMore: false,
      menuCount: 1,
      buttonDataShow: [],
      buttonDataHide: [],
      hasPrivShowButton: [],//存储显示的按钮
      mainFormIframe: "busFormIframe",
      busFormIframeArray: [],//表单iframe集合
      requestErrorText: "请求服务器失败，请检查链接有效性！",
      sendTaskInfos: [],//当前环节发送相关信息
      sendTaskInfo: null,//当前环节发送相关信息
      nextTaskAssignee: null,//下一环节办理人
      nextTaskAssigneeId: null,//下一环节办理人id（登录名）
      nextTask: null,//下一环节名称
      sendParam: null,//发送参数
      sendDialog: false,//发送对话框
      sendForm: {},//发送表单参数
      sendFormRule: {},//发送表单校验规则
      onlySuggestForm: {}, // 只有办理意见的弹窗form
      
      // *********** 意见弹框参数 start *********************
      opinionForm: {},//办理意见表单参数
      opinionFormRule: {
        opinionText: [
          {required: true, message: '请填写意见内容', trigger: 'blur'}
        ]
      },//办理意见表单校验规则
      manageOpinionDialog: false,//管理常用办理意见对话框
      opinionTableData: [],//常用办理意见表格数据
      editOpinionDialog: false,//编辑意见对话框
      editOpinionForm: {},//编辑意见form
      editOpinionFormRule: {
        opinionContent: [
          {required: true, message: '请填写审批意见', trigger: 'blur'}
        ],
        sortNo: [
          {required: true, message: '请填写排序号', trigger: 'blur'}
        ]
      },//编辑意见校验规则
      //选择办理人树
      submitCommentsShow: false,//更改状态用弹框控制
      submitCommentsFlag1: false, // 展示常用意见管理框
      submitCommentsFlag2: false, // 展示编辑常用意见框
      commentTitle: "特别程序",//更改状态是标题
// *********** 意见弹框参数 start *********************
      selectAssigneeDialog: false,//选择办理人对话框
      treeLeftHeight: document.documentElement.clientHeight - 150,
      defaultProps: {
        label: 'name',
        isLeaf: 'leaf'
      },
      treeShowNode: '',
      assigneeRangeKey: null,//下一环节办理人范围
      assigneeSearchKeyword: '',//查询
      zTreeObj: null,//树对象
      tempDefaultSelectedAssigneeId: null,
      ID_SPLIT: ",",
      TEXT_SPLIT: "、",
      selectedAssignee: [],
      isNeedSelectAssignee: false,//是否需要选择办理人
      processDiagramDialog: false,//流程跟踪对话框
      processStatesDialog: false, //查看情形对话框
      statesData: [], // 查看情形数据
      typeAstateData: {}, // 申报方式和状态
      rightTabActive: 1, // 1为流程视图  2为对话框视图
      rightVerticalTabActive: 1, // 1为办理意见  2为材料附件
      rightIconTab: true, // true 为展开宽度25%  false为 展开宽度50%
      saveAndSendTitle: '发送对话框',
      
      receiptPrintDialog: false,//回执弹框
      receiptPrintLoading: false,
      receiveList: [],//回执列表
      
      enumActionCode: '',//OpsActionConstants 对应的枚举字段
      enumApplyStatus: '',//ApplyState 对应的枚举值 ---需要改变的申请状态
      enumItemStatus: '',//ItemStatus 对应的枚举值 ---事项状态
      
      sendUrlPath: '',//发送地址
      requestMappingType: 'put',//访问方式
      
      //------------------证照--------------------------------------------
      certinstModalShow: false,//证照实例列表弹窗控制
      selectCertinstList: [],//选择的证照实例行数
      editCertinstModel: false,//证照编辑/新增
      editCertModalTitle: '新增',
      uploadCertData: [],//已上传文件列表
      showUpdateBtn: true,
      ctx: ctx,
      fileList: [],
      certinstList: [],//证照实例表
      editCertForm: {},
      certFormRules: {//证照实例校验
        certId: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        certinstCode: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        certinstName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        issueOrgId: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        termStart: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        termEnd: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        issueDate: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        certOwner: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
      },
      certList: [],//证照定义列表
      uploadTableData: [],
      fileSelectionList: [], // 所选电子件
      selMatRowData: {}, // 所选择的材料信息
      fileWinData: [], // 上传窗口上传参数列表
      loadingFileWin: false, // 窗口文件上传loading
      
      // 材料补正按钮相关
      // 是否展示材料补正的dialog
      isShowMaterialSupplementDialog: false,
      // 材料补正dialog的loading
      sloading: false,
      // 接口获取到的补正的数据
      supplementDialogData: {},
      // 材料补正-form
      supplemetForm: {
        applyinstCode: "",
        approveOrgName: "",
        iteminstName: "",
        localCode: "",
        owner: "",
        projInfoName: "",
        correctDueDays: '',
        chargeOrgName: ''
      },
      // 材料补正-form校验
      supplementFormRules: {
        correctDueDays: [{required: true, trigger: blur, message: '请输入补正办理时限'}]
      },
      // 是否在选择材料内
      inSeletcedMaterialPandel: true,
      inSeletcedIteminstMaterialPandel: false,
      // 未选择前的待选材料列表
      tobeSelectMaterialList: [],
      // 待选择材料列表的MatIds
      tobeSelectMatIds: [],
      // 选择后的材料列表
      tobaMakeupMaterialList: [],
      unitAndProjInfo: {},
      //----------- 需要保存的数据 end
      
      expanded: true,// 右边aside是否展开
      sendDialogVisible: false,// 发送对话框是否显示
      processDialogVisible: false,// 流程图弹窗是否显示
      lTabsData: [], // 左边iframes
      
      // 右边四个标签
      asideTabs: [
        "审批过程",
        "所有附件",
        // "主题导航",
        // "项目导航",
      ],
      // 是否展示所有附件面板
      isShowMatiPanel: false,
      // 当前标签index
      asideIndex: 0,
      // 审批过程展示数据
      approveStepList: [],
      treeData: [],
      treeProps: {
        children: 'list',
        label: 'fileText'
      },
      preUploadNode: null,
      selectedNodeId: -1,
      selectedNode: null,
      approveProcessLoading: true,
      parentPageLoading: true,
      onlySuggestDialog: false,
      sendDialogLoading: false,
      stageOrItemName: '',
      onlySuggestLoading: false,
      iteminstState: '',
      specialDiaVisible: false,
      prePdfVisible: false,
      specialSrc: '',
      pdfSrc: '',
      forwardTaskVisible: false,
      forwardDiaLoading: false,
      nextTaskManList: [],
      forwardForm: {
        nextTaskManVal: '',
        comment: '',
      },
      forwardRules: {
        loginName: [{required: true, message: '请选择下一处理人', trigger: 'change'}],
        comment: [{required: true, message: '请填写转发意见', trigger: 'blur'}],
      },
      //预审操作参数
      preApproveOperation: null,
      creditTypeList: [], // 信用类型数据
      creditDiaVisible: false,
      creditDiaLoading: false,
      creditHintTitle: '该企业为失信企业',
      creditHintIconClass: 'el-icon-warning',
      //材料补全dialog
      isShowMatmend: false,
      // 接口获取到的补全的数据
      matMendDialogData: {},
      // 材料不全form
      matMendForm: {
        applyinstCode: "",
        approveOrgName: "",
        iteminstName: "",
        localCode: "",
        owner: "",
        projInfoName: "",
        correctDueDays: '',
      },
      // 未选择前的待选材料列表-补全
      tobeSelectMaterialList2: [],
      // 已提交的材料列表Ids
      tobeSelectMatIds2: [],
      // 选择后的材料列表-补全
      tobaMakeupMaterialList2: [],
      matSendLoading: false,
      // 批文批复 打印预览
      licenceDialogVisible: false,
      preLicenceIframeSrc: '',
      prePrintIframeSrc: '',
      printLicenseLoading: false,
      conclusionOptionValue: '1',
      nextTaskCheckedId: '',
      nextTaskListData: [],
      // 审批过程 特殊程序和补正详情弹窗
      processDetailTitle: '该弹窗内容为测试数据',
      processDetailVisible: false,
      processDetailLoading: false,
      processDetList: [],
      // 回执dialog
      receiveItemActive: '',
      receiveActive: '',
      receivePdfSrc: '',
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      proDetNodeName: '',
      currentStateValue: '',
      checkIsApprover: '',
      projectCode: '', // 一张蓝图需要传projectCode
      notAllowFileOpe: { // 所有附件不允许操作的文件夹
        'sbcl_2018_augurit': true, // 申办材料文件夹
        'pwpf_2018_augurit': true, // 批文批复文件夹
      },
      friNodeId: -1,
      // 待补全材料-补全
      matCorrectDtos: [],
      // 材料补全
      submittedMats: [],
      // 下一环节办理人多选交互
      isMultiFlow: false,
      mutiCheckedNames: [],
      mutiCheckedMan: [],
      treeTargetRow: [],
      // 特殊程序
      speTabIndex: 0,
      speBaseInfoForm: {
        specialType: '',
        chargeOrgName: '',
        creator: '',
        specialDueDays: '',
        opsTime: '',
        specialDueTime: '',
        specialReason: '',
        specialMemo: '',
      },
      speBaseInfoRules: {
        specialType: [{required: true, message: '请选择类型', trigger: 'change'}],
        specialDueDays: [{required: true, message: '请输入时限', trigger: 'blur'}],
        opsTime: [{required: true, message: '请选择开始日期', trigger: 'blur'}],
        specialReason: [{required: true, message: '请填写申请内容', trigger: 'blur'}],
        specialMemo: [{required: true, message: '请填写启动理由或法律依据', trigger: 'blur'}],
      },
      specialTypeList: [],
      speDiaLoading: false,
      speFileList: [],
      speTableName: 'AEA_HI_ITEM_SPECIAL',
      speStartPkName: 'SPECIAL_START_MAT_ID',
      speEndPkName: 'SPECIAL_END_MAT_ID',
      hasSpecial: 0,
      hasSupply: 0,
      specialStartMatId: '',
      specialEndMatId: '',
      specialDetailInfo: [],
      approverSpecialDetailList: [],
      collapseActiveIndex: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
      endSpecialRules: {
        specialResult: [{required: true, message: '结果不能为空', trigger: 'blur'}],
      },
      nowEndRow: {},
      isDraftPage: 'false',
      // 材料补正 新
      bzLoading: false,
      bzDiaVisible: false,
      bzFormData: {
        applyinstCode: '',
        projInfoName: '',
        localCode: '',
        owner: '',
        correctDueDays: '',
        correctMemo: '',
      },
      bzFormRules: {
        correctDueDays: [{required: true, message: '时限不能为空', trigger: 'blur'}],
      },
      showBzLeftTable: true,
      bzSubmitData: [],
      bzCorrectData: [],
      bzApproveData: [],
      isShowOneForm: '0',
      // 证照材料、材料库材料、表单材料
      idLibSearchOpt: {
        chooseUnit: '',
        chooseType: 1,
      },
      idLibLoading: false,
      idLibVisible: false,
      matLibLoading: false,
      matLibVisible: false,
      idLibTableList: [],
      matLibTableList: [],
      applyMainType: '0',
      applyUnitList: [],
      itemVerids: '',
      currentMatRow: '',
      refreshMatIframe: function () {
      },
      matFormVisible: false,
      // 回退
      backDiaLoading: false,
      backDiaVisible: false,
      backNodeList: [],
      backFormData: {
        returnToActId: '',
        comment: '',
        selectOpinion: '',
      },
      isBackDialog: true,
      backDiaTitle: '回退',
      backformRules: {
        comment: [{required: true, message: '请填写意见', trigger: 'blur'}],
      },
      // 中介事项审批
      isZJItem: false, // 是否为中介事项
      smartFormInfo: [],
      processDialogLoading: false,
      stageId: '',
      projInfoId: '',
      itemVersionId: '',
      currentCertinstId: '',
      RQtimeVisible: false,
      RQtimeLoading: false,
      rqTimeForm: {
        toleranceTime: 1,
        timeruleId: '',
      },
      rqTimeformRules: {
        toleranceTime: [{required: true, message: '请填写时限', trigger: 'blur'}],
        timeruleId: [{required: true, message: '请选择时限规则', trigger: 'blur'}],
      },
      rqRulesList: [],
      isRQDialog: false,
    }
  },
  filters: {
    defaultManFilter: function (value) {
      return value || '暂无审批人';
    },
    formatDate: function (time) {
      if (!time) return '-';
      var date = new Date(time);
      if (!date) return;
      return formatDate(date, 'yyyy-MM-dd hh:mm');
    },
    changeReceiveType: function (value) {
      var defaultValue = '其他回执';
      if (value) {
        switch (value) {
          case '1':
            defaultValue = '物料回执';
            break;
          case '2':
            defaultValue = '收件回执';
            break;
          case '3':
            defaultValue = '不受理回执';
            break;
          case '6':
            defaultValue = '材料补正回执';
            break;
          default:
            defaultValue = '其他回执';
        }
      }
      return defaultValue;
    },
  },
  methods: {
    // 得到容缺时限规则数据
    getRQRuleList: function(){
      var vm = this;
      if (vm.rqRulesList.length) {
        return null;
      }
      request('', {
        url: ctx + 'rest/act/sto/timerule/getActStoTimeruleByOrgId',
        type: 'get',
      }, function(res){
        if (res.success) {
          vm.rqRulesList = res.content;
          vm.rqTimeForm.timeruleId = vm.rqRulesList[0].timeruleId;
          vm.rqTimeForm.toleranceTime = 1;
        } else {
          vm.$message.error(res.message || '获取时限规则数据失败');
        }
      },function(){
        vm.$message.error('获取时限规则数据失败');
      })
    },
    // 关闭容缺时限配置弹窗
    closeRQtimeDialog: function(){
      this.$refs.rqTimeFormRef.resetFields();
    },
    // 打开容缺时限配置弹窗
    openRQtimeDialog: function(){
      var vm = this;
      if (vm.rqRulesList.length) {
        vm.rqTimeForm.timeruleId = vm.rqRulesList[0].timeruleId;
        vm.rqTimeForm.toleranceTime = 1;
        vm.RQtimeVisible = true;
        return null;
      }
      vm.parentPageLoading = true;
      request('', {
        url: ctx + 'rest/act/sto/timerule/getActStoTimeruleByOrgId',
        type: 'get',
      }, function(res){
        vm.parentPageLoading = false;
        if (res.success) {
          vm.rqRulesList = res.content;
          vm.rqTimeForm.timeruleId = vm.rqRulesList[0].timeruleId;
          vm.rqTimeForm.toleranceTime = 1;
          vm.RQtimeVisible = true;
        } else {
          vm.$message.error(res.message || '获取时限规则数据失败');
        }
      },function(){
        vm.parentPageLoading = false;
        vm.$message.error('获取时限规则数据失败');
      })
    },
    // 关闭填写材料表单弹窗
    closeMatFormDia: function () {
      this.$nextTick(function () {
        $("#matFormBox").html('');
      });
      typeof this.refreshMatIframe == 'function' && this.refreshMatIframe();
    },
    // 打开填写材料表单弹窗
    openMatFormDialog: function (row, refreshMatIframe) {
      var vm = this;
      vm.parentPageLoading = true;
      vm.currentMatRow = row;
      vm.refreshMatIframe = refreshMatIframe;
      request('', {
        url: ctx + 'bpm/common/sf/renderHtmlFormContainer',
        type: 'get',
        data: {
          listRefEntityId: vm.masterEntityKey,
          listFormId: row.stoFormId,
          showBasicButton: true,
          includePlatformResource: false,
          busiScence: 'mat',
        },
      }, function (res) {
        vm.parentPageLoading = false;
        if (res.success) {
          vm.matFormVisible = true;
          vm.$nextTick(function () {
            $("#matFormBox").html(res.content);
          })
        } else {
          vm.$message.error(res.content || '获取材料表单数据失败');
        }
      }, function () {
        vm.parentPageLoading = false;
        vm.$message.error('获取材料表单数据失败');
      })
    },
    // 打开材料库弹窗
    openMatLibDialog: function (row, refreshMatIframe) {
      var vm = this;
      vm.parentPageLoading = true;
      vm.currentMatRow = row;
      vm.refreshMatIframe = refreshMatIframe;
      request('', {
        url: ctx + 'rest/approve/matinst/matinstFileLibrary',
        type: 'post',
        data: {
          applyinstId: vm.masterEntityKey,
          matId: row.matId,
        }
      }, function (res) {
        vm.parentPageLoading = false;
        if (res.content && res.content.length) {
          vm.matLibVisible = true;
          vm.matLibTableList = res.content;
        } else {
          vm.$message.info('暂无材料数据');
        }
      }, function (res) {
        vm.parentPageLoading = false;
        vm.$message.error('获取材料列表失败');
      })
      if (window) return null;
      window.setTimeout(function () {
        vm.parentPageLoading = false;
        vm.matLibVisible = true;
        vm.matLibTableList = [
          {fileName: 'test1.doc', fileType: 'doc'},
          {fileName: 'test2.doc', fileType: 'doc'},
        ];
      }, 500);
    },
    // 材料列表弹窗 查看材料
    matTableListSee: function (row) {
    },
    // 材料列表弹窗 选择材料
    matTabListChoose: function (row) {
      var vm = this;
      var params = {
        fileIds: row.fileId,
      };
      if (vm.currentMatRow.attMatinstId) {
        params.matinstId = vm.currentMatRow.attMatinstId;
      } else {
        params.applyinstId = vm.masterEntityKey;
        params.matId = vm.currentMatRow.matId;
      }
      vm.matLibLoading = true;
      request('', {
        url: ctx + 'rest/approve/matinst/createMatinstAndFileLink',
        type: 'post',
        data: params,
      }, function (res) {
        vm.matLibLoading = false;
        if (res.success) {
          vm.$message.success('关联成功');
          vm.matLibVisible = false;
          typeof vm.refreshMatIframe == 'function' && vm.refreshMatIframe();
        } else {
          vm.$message.error(res.message || '关联失败');
        }
      }, function () {
        vm.matLibLoading = false;
        vm.$message.error('关联失败');
      });
    },
    // 打开证照库弹窗
    openIdLibDialog: function (row, refreshMatIframe) {
      var vm = this;
      vm.parentPageLoading = true;
      vm.currentMatRow = row;
      vm.refreshMatIframe = refreshMatIframe;
      vm.itemVerids = row.itemVerIds;
      vm.loadIdLibList(function () {
        vm.idLibVisible = true;
        vm.parentPageLoading = false;
      });
    },
    // 加载证照列表
    loadIdLibList: function (callback) {
      var vm = this;
      if (typeof callback != 'function') {
        vm.idLibLoading = true;
      }
      var params = {};
      if (vm.applyMainType == '1') {
        vm.applyUnitList.forEach(function (u) {
          if (u.unitInfoId == vm.idLibSearchOpt.chooseUnit) {
            if (vm.idLibSearchOpt.chooseType == 1) {
              // 企业信用代码
              params.identityNumber = u.unifiedSocialCreditCode;
            } else {
              // 法人身份证
              params.identityNumber = u.idno;
            }
          }
        });
      }
      params.itemVerIds = vm.itemVerids;
      if (isDevelop) {
        window.setTimeout(function () {
          vm.idLibLoading = false;
          idLibResMock.content.data.forEach(function (u) {
            var flag = false;
            vm.currentMatRow.certinstList && vm.currentMatRow.certinstList.forEach(function (uu) {
              if (uu.certFileList[0].fileId.split(u.license_code).length > 1) {
                flag = true;
              }
            });
            u.isRelated = flag;
          });
          vm.idLibTableList = idLibResMock.content.data;
          typeof callback == 'function' && callback();
        }, 500);
      } else {
        request('', {
          url: ctx + 'aea/cert/getLicenseAuthRes.do',
          type: 'get',
          data: params,
        }, function (res) {
          vm.idLibLoading = false;
          vm.parentPageLoading = false;
          if (res.success) {
            if (res.content && res.content.data && res.content.data.length) {
              vm.idLibLoading = false;
              res.content.data.forEach(function (u) {
                var flag = false;
                vm.currentMatRow.certinstList && vm.currentMatRow.certinstList.forEach(function (uu) {
                  if (uu.certFileList[0].fileId.split(u.license_code).length > 1) {
                    flag = true;
                  }
                });
                u.isRelated = flag;
              });
              vm.idLibTableList = res.content.data;
              typeof callback == 'function' && callback();
            } else {
              vm.$message.error('未获取到证照信息');
            }
          } else {
            vm.$message.error(res.message || '获取证照库列表数据失败');
          }
        }, function () {
          vm.idLibLoading = false;
          vm.parentPageLoading = false;
          vm.$message.error('获取证照库列表数据失败');
        })
      }
    },
    // 证照列表弹窗 查看证照
    idTableListSee: function (row) {
      var vm = this;
      vm.idLibLoading = true;
      request('', {
        url: ctx + 'aea/cert/getViewLicenseURL.do',
        type: 'get',
        data: {authCode: row.auth_code},
      }, function (res) {
        vm.idLibLoading = false;
        if (res.success) {
          window.open(res.content);
        } else {
          vm.$message.error(res.message || '查看证照失败');
        }
      }, function () {
        vm.idLibLoading = false;
        vm.$message.error('查看证照失败');
      });
    },
    // 证照列表 解除关联
    idIisassociation: function (row) {
      var vm = this;
      this.$confirm('姝ゆ搷浣滃皢瑙ｉ櫎鍏宠仈璇ヨ瘉鐓§, 鏄¯鍚︾户缁­?', '瑙ｉ櫎鍏宠仈', {
        confirmButtonText: '纭®瀹',
        cancelButtonText: '鍙栨秷',
        type: 'warning',
      }).then(function (obj) {
        ensureDelete();
      }).catch(function () {
      });
      
      function ensureDelete() {
        vm.idLibLoading = true;
        var matinstId = '';
        vm.currentMatRow.certinstList.forEach(function (u) {
          if (u.certFileList[0].fileId.split(row.license_code).length > 1) {
            matinstId = u.certMatinstId;
          }
        });
        request('', {
          url: ctx + 'rest/approve/matinst/unbindCertinst',
          type: 'post',
          data: {
            matinstId: matinstId,
          },
        }, function (res) {
          vm.idLibLoading = false;
          if (res.success) {
            vm.$message.success('宸茶В闄ゅ叧鑱');
            row.isRelated = false;
            typeof vm.refreshMatIframe == 'function' && vm.refreshMatIframe();
          } else {
            vm.$message.error(res.message || '瑙ｉ櫎鍏宠仈澶辫触')
          }
        }, function () {
          vm.idLibLoading = false;
          vm.$message.error('瑙ｉ櫎鍏宠仈澶辫触');
        });
      }
      
      if (window) return null;
      var param = {
        "authCode": row.auth_code,
        "certId": vm.currentMatRow.certId,
        "certOwner": row.holder_name,
        "certinstCode": row.license_code,
        "certinstName": row.name,
        "issueDate": row.issue_time,
        "issueOrgId": row.issue_org_code,
        "managementScope": "",
        "matId": vm.currentMatRow.matId,
        "memo": row.remark,
        "termEnd": row.expiry_date,
        "termStart": row.begin_date,
      };
      param.applyinstId = vm.masterEntityKey;
      // if (vm.currentMatRow.certMatinstId) {
      //   param.matinstId = vm.currentMatRow.certMatinstId;
      // }
      param.certinstId = vm.currentMatRow.certinstId;
      // if (vm.currentMatRow.certinstId){
      //   param.certinstId = vm.currentMatRow.certinstId;
      // }
      vm.idLibLoading = true;
      request('', {
        url: ctx + 'rest/approve/CertTypeMatinst/update',
        type: 'post',
        ContentType: 'application/json',
        data: JSON.stringify(param),
      }, function (res) {
        vm.idLibLoading = false;
        if (res.success) {
          vm.$message.success('鍏宠仈璇佺収鎴愬姛');
          // vm.idLibVisible = false;
          row.isRelated = true;
          typeof vm.refreshMatIframe == 'function' && vm.refreshMatIframe();
        } else {
          vm.$message.error(res.message || '鍏宠仈璇佺収澶辫触');
        }
      }, function () {
        vm.idLibLoading = false;
        vm.$message.error('鍏宠仈璇佺収澶辫触');
      });
    },
    // 证照列表弹窗 关联证照
    idTabListChoose: function (row) {
      var vm = this;
      var param = {
        "authCode": row.auth_code,
        "certId": vm.currentMatRow.certId,
        "certOwner": row.holder_name,
        "certinstCode": row.license_code,
        "certinstName": row.name,
        "issueDate": row.issue_time,
        "issueOrgId": row.issue_org_code,
        "managementScope": "",
        "matId": vm.currentMatRow.matId,
        "memo": row.remark,
        "termEnd": row.expiry_date,
        "termStart": row.begin_date,
      };
      param.applyinstId = vm.masterEntityKey;
      // if (vm.currentMatRow.certMatinstId) {
      //   param.matinstId = vm.currentMatRow.certMatinstId;
      // }
      param.certinstId = vm.currentMatRow.certinstId;
      // if (vm.currentMatRow.certinstId){
      //   param.certinstId = vm.currentMatRow.certinstId;
      // }
      vm.idLibLoading = true;
      request('', {
        url: ctx + 'rest/approve/CertTypeMatinst/update',
        type: 'post',
        ContentType: 'application/json',
        data: JSON.stringify(param),
      }, function (res) {
        vm.idLibLoading = false;
        if (res.success) {
          vm.$message.success('关联证照成功');
          // vm.idLibVisible = false;
          row.isRelated = true;
          typeof vm.refreshMatIframe == 'function' && vm.refreshMatIframe();
        } else {
          vm.$message.error(res.message || '关联证照失败');
        }
      }, function () {
        vm.idLibLoading = false;
        vm.$message.error('关联证照失败');
      });
    },
    // 获取材料补正详情数据
    loadSupplyDetail: function () {
      var vm = this;
      // bzMockData.forEach(function(u){
      //   u.bzMatList = vm.packageBzData(u.matCorrectDtos, true);
      // });
      // vm.bzApproveData = bzMockData.concat([]);
      // if(window) return null;
      vm.parentPageLoading = true;
      request('', {
        url: ctx + 'rest/correct/detail/list?iteminstId=' + vm.iteminstId + '&applyinstId=' + vm.masterEntityKey,
        // url: ctx + 'rest/correct/detail/list?iteminstId=988347fc-fde9-4067-8558-f45f1e3d9955',
        type: 'get',
      }, function (res) {
        vm.parentPageLoading = false;
        if (res.success) {
          if (res.content && res.content.length) {
            res.content.forEach(function (u) {
              u.bzMatList = vm.packageBzData(u.matCorrectDtos, true);
            });
            vm.bzApproveData = res.content;
          }
        } else {
          vm.$message.error(res.message || '加载材料补正详情失败')
        }
      }, function () {
        vm.parentPageLoading = false;
        vm.$message.error('加载材料补正详情失败')
      })
    },
    // 关闭发送弹窗
    closeSendDialog: function () {
      this.mutiCheckedNames = [];
      this.mutiCheckedMan = [];
      this.isRQDialog = false;
    },
    // 特殊程序类型code转text
    specialTypeToText: function (type) {
      var result = '';
      this.specialTypeList.forEach(function (u) {
        if (u.itemCode == type) {
          result = u.itemName;
        }
      })
      return result;
    },
    // 中文序号
    getChineseIndex: function (index, length) {
      return __STATIC.getChineseIndex(index, length);
    },
    // 加载特殊程序左下tab详情数据
    loadSpecialDetail: function () {
      var vm = this;
      vm.parentPageLoading = true;
      var params = {applyinstId: vm.masterEntityKey};
      if (vm.isApprover == 1) {
        params.iteminstId = vm.iteminstId;
      }
      request('', {
        url: ctx + 'rest/specialProcedure/getItemSpecialList',
        type: 'get',
        data: params,
      }, function (res) {
        vm.parentPageLoading = false;
        if (res.success) {
          if (res.content && res.content.length) {
            var tmpArr = [];
            var today = __STATIC.formatDate(new Date().getTime(), 'yyyy-MM-dd');
            res.content.forEach(function (u) {
              // 去除空和空数组
              if (u.specialList && u.specialList.length) {
                u.specialList.forEach(function (uu) {
                  uu.tabIndex = 0;
                  uu.endSpecialForm = {
                    specialResult: '',
                    opsUserName: uu.creater,
                    opsTime: today,
                    money: 0,
                  };
                })
                tmpArr.push(u);
              }
            });
            if (tmpArr.length) {
              vm.specialDetailInfo = tmpArr;
            }
            vm.approverSpecialDetailList = [];
            if (vm.specialDetailInfo[0]) {
              vm.approverSpecialDetailList = vm.specialDetailInfo[0].specialList || [];
            }
          }
        } else {
          vm.$message.error(res.message || '加载特殊程序详情失败')
        }
      }, function () {
        vm.parentPageLoading = false;
        vm.$message.error('加载特殊程序详情失败')
      })
    },
    // 请求得到特殊程序类型
    getSpecialType: function () {
      var vm = this;
      if (this.specialTypeList.length != 0) return null;
      __STATIC.getDicList({
        dataStr: 'specialTypeList',
        code: 'SPECIAL_TYPE',
        vm: vm,
      })
    },
    // 结束特殊程序
    stopSpecialApp: function (index) {
      var str = 'specialEndForm_' + index;
      var vm = this;
      vm.$refs[str][0].validate(function (valid) {
        if (valid) {
          var params = $.extend({}, {
            iteminstId: vm.iteminstId,
            isFlowTrigger: '1',
            approveResult: '1',
            specialId: vm.approverSpecialDetailList[index].specialId,
            applyinstId: vm.approverSpecialDetailList[index].applyinstId,
            appinstId: vm.approverSpecialDetailList[index].appinstId,
          }, vm.approverSpecialDetailList[index].endSpecialForm);
          vm.parentPageLoading = true;
          request('', {
            url: ctx + 'rest/specialProcedure/stopSpecial',
            type: 'post',
            data: params,
          }, function (res) {
            vm.parentPageLoading = false;
            if (res.success) {
              vm.$message.success('特殊程序已结束');
              vm.approverSpecialDetailList[index].specialState = '10';
              vm.approverSpecialDetailList[index].specialResult = params.specialResult;
              vm.approverSpecialDetailList[index].opsTime = params.opsTime;
              vm.approverSpecialDetailList[index].money = params.money;
              delayRefreshWindow(1000);
            } else {
              vm.$message.error(res.message || '结束特殊程序失败');
            }
          }, function () {
            vm.$message.error('结束特殊程序失败');
          })
        } else {
          vm.approverSpecialDetailList[index].tabIndex = 0;
        }
      })
    },
    // 获取特殊程序开始附件
    getSpeFileList: function () {
      var vm = this;
      vm.speDiaLoading = true;
      var params = {
        tableName: vm.speTableName,
        recordIds: vm.specialStartMatId,
        isSeries: vm.isSeriesinst,
        pkName: vm.speStartPkName,
      };
      request('', {
        url: ctx + 'rest/approve/att/dirs/file/list',
        type: 'get',
        data: params,
      }, function (res) {
        vm.speDiaLoading = false;
        if (res.success) {
          vm.speFileList = res.content.files || [];
        } else {
          vm.$message.error(res.message || '获取特殊程序附件列表失败')
        }
      }, function () {
        vm.speDiaLoading = false;
        vm.$message.error('获取特殊程序附件列表失败')
      });
    },
    // 上传特殊程序开始附件
    speUploadFile: function (file) {
      var vm = this;
      var formData = new FormData();
      formData.append('files', file.file);
      formData.append('tableName', vm.speTableName);
      formData.append('pkName', vm.speStartPkName);
      formData.append('recordId', vm.specialStartMatId);
      vm.speDiaLoading = true;
      axios.post(ctx + 'rest/approve/att/file/upload', formData).then(function (res) {
        if (res.data && res.data.success) {
          vm.$message.success('文件上传成功')
          // 刷新列表
          vm.getSpeFileList();
        } else {
          vm.$message.error(res.message || '文件上传失败');
        }
      }).catch(function (e) {
        vm.$message.error('文件上传失败');
      }).finally(function () {
        vm.speDiaLoading = false;
      })
    },
    setNowEndRow: function (row) {
      this.nowEndRow = row;
    },
    // 上传特殊程序结束 附件
    speUploadEndFile: function (file) {
      var vm = this;
      var formData = new FormData();
      formData.append('files', file.file);
      formData.append('tableName', vm.speTableName);
      formData.append('pkName', vm.speEndPkName);
      formData.append('recordId', vm.nowEndRow.specialEndMatId);
      vm.speDiaLoading = true;
      axios.post(ctx + 'rest/approve/att/file/upload', formData).then(function (res) {
        if (res.data && res.data.success) {
          vm.$message.success('文件上传成功')
          // 刷新列表
          vm.getSepEndFileList();
        } else {
          vm.$message.error(res.message || '文件上传失败');
        }
      }).catch(function (e) {
        vm.$message.error('文件上传失败');
      }).finally(function () {
        vm.speDiaLoading = false;
      })
    },
    // 得到特殊结束附件列表
    getSepEndFileList: function () {
      var vm = this;
      vm.parentPageLoading = true;
      var params = {
        tableName: vm.speTableName,
        recordIds: vm.nowEndRow.specialEndMatId,
        isSeries: vm.isSeriesinst,
        pkName: vm.speEndPkName,
      };
      request('', {
        url: ctx + 'rest/approve/att/dirs/file/list',
        type: 'get',
        data: params,
      }, function (res) {
        vm.parentPageLoading = false;
        if (res.success) {
          vm.nowEndRow.specialEndMatList = res.content.files || [];
        } else {
          vm.$message.error(res.message || '获取特殊程序附件列表失败')
        }
      }, function () {
        vm.parentPageLoading = false;
        vm.$message.error('获取特殊程序附件列表失败')
      });
    },
    delSpeFile: function (row, isEnd) {
      var vm = this;
      this.$confirm('此操作将永久删除该文件, 是否继续?', '删除文件', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(function (obj) {
        ensureDelete();
      }).catch(function () {
      });
      
      function ensureDelete() {
        var param = {};
        var reqUrl = ctx + 'rest/approve/att/file/dir/delete';
        param.detailIds = row.detailId;
        vm.speDiaLoading = true;
        request('', {
          url: reqUrl,
          type: 'post',
          data: param,
        }, function (res) {
          if (res.success) {
            vm.speDiaLoading = false;
            vm.$message.success('文件已删除');
            isEnd ? vm.getSepEndFileList() : vm.getSpeFileList();
          } else {
            vm.$messsage.error(res.message || '删除文件失败');
          }
        }, function (msg) {
          vm.speDiaLoading = false;
          vm.$messsage.error('删除文件失败');
        });
      }
    },
    specialDiaSave: function () {
      var vm = this;
      if (this.speTabIndex == 0) {
        // 基本信息
        vm.$refs.speBaseInfoForm.validate(function (flag) {
          if (flag) {
            vm.speBaseInfoForm.taskId = vm.taskId;
            vm.speBaseInfoForm.isFlowTrigger = '1';
            vm.speBaseInfoForm.approveResult = '1';
            vm.speBaseInfoForm.applyinstId = vm.masterEntityKey;
            vm.speBaseInfoForm.iteminstId = vm.iteminstId;
            vm.speBaseInfoForm.appinstId = vm.appinstId;
            vm.speDiaLoading = true;
            request('', {
              url: ctx + 'rest/specialProcedure/saveSpecial',
              type: 'post',
              data: vm.speBaseInfoForm,
            }, function (res) {
              vm.speDiaLoading = false;
              vm.specialStartMatId = res.content.specialStartMatId;
              vm.specialEndMatId = res.content.specialEndMatId;
              if (res.success) {
                vm.$confirm('已经成功发起特殊程序！', '提示', {
                  confirmButtonText: '继续上传附件',
                  cancelButtonText: '关闭窗口',
                  type: 'success',
                  customClass: 'special-message-box',
                }).then(function () {
                  // 去上传附件
                  vm.speTabIndex = 1;
                }).catch(function () {
                  // 关闭窗口
                  vm.specialDiaVisible = false;
                  vm.$message.success('特殊程序已成功发起');
                  delayRefreshWindow(1000);
                });
              } else {
                vm.$message.error(res.message || '接口请求失败')
              }
            }, function () {
              vm.speDiaLoading = false;
              vm.$message.error('接口请求失败')
            });
          }
        })
      } else if (this.speTabIndex == 1) {
        // 特殊程序附件保存
        vm.$message.success('保存成功');
        // 关闭窗口
        vm.specialDiaVisible = false;
        delayRefreshWindow(1000);
      }
    },
    calcEndDate: function () {
      var vm = this;
      var day = vm.speBaseInfoForm.specialDueDays;
      var date = vm.speBaseInfoForm.opsTime;
      if (!!day && !!date) {
        vm.speDiaLoading = true;
        request('', {
          url: ctx + 'rest/specialProcedure/getSpecialEndDate',
          type: 'get',
          data: {
            dueNum: day,
            startDate: date,
          }
        }, function (res) {
          vm.speDiaLoading = false;
          if (res.success) {
            vm.speBaseInfoForm.specialDueTime = res.message;
          } else {
            vm.$message.error(res.message || '服务请求失败');
          }
        }, function () {
          vm.speDiaLoading = false;
          vm.$message.error('服务请求失败');
        })
      } else {
        vm.speBaseInfoForm.specialDueTime = '';
      }
    },
    openSpecialDialog: function () {
      var vm = this;
      this.parentPageLoading = true;
      vm.getSpecialType();
      request('', {
        url: ctx + 'rest/specialProcedure/getBasicInfo',
        type: 'get',
        data: {
          'applyinstId': vm.masterEntityKey,
          'iteminstId': vm.iteminstId
        }
      }, function (res) {
        vm.parentPageLoading = false;
        if (res.success) {
          vm.specialDiaVisible = true;
          vm.speBaseInfoForm.chargeOrgName = res.content.chargeOrgName;
          vm.speBaseInfoForm.creator = res.content.currentUserName;
          vm.speBaseInfoForm.opsTime = __STATIC.formatDate(new Date(), 'yyyy-MM-dd');
        } else {
          vm.$message.error(res.message || '服务请求失败')
        }
      }, function () {
        vm.parentPageLoading = false;
        vm.$message.error('服务请求失败')
      });
    },
    closeSpecialDialog: function () {
      if (this.speTabIndex == 1) {
        vm.$message.success('特殊程序已成功发起');
        delayRefreshWindow(1000);
      }
      this.$refs.speBaseInfoForm.resetFields();
      this.speTabIndex = 0;
      this.speFileList = [];
    },
    processFormatDate: function (d) {
      return __STATIC.formatDate(d);
    },
    formatDateStr: function (d, s) {
      return __STATIC.formatDate(d, s)
    },
    processFormatType: function (item, type) {
      var res = '';
      if (item.childNode && item.childNode.length) {
        var _index = 0;
        item.childNode.forEach(function (u, index) {
          if (u.dealingTask == false) {
            _index = index;
          }
        });
        res = item.childNode[_index][type];
        if (res) {
          if (type == 'endDate') {
            res = __STATIC.formatDate(res);
          }
        } else {
          if (type == 'commentMessage') {
            res = '暂无意见';
          }
        }
      } else {
        if (type == 'commentMessage') {
          res = '暂无意见'
        }
      }
      return res;
    },
    // 打开特殊程序或补正详情的弹窗
    openProcessDetDialog: function (row) {
      var vm = this;
      vm.parentPageLoading = true;
      request('', {
        url: ctx + 'rest/bpm/approve/item/supplement?iteminstId=' + row.iteminstId,
        type: 'get',
      }, function (res) {
        vm.parentPageLoading = false;
        vm.processDetailVisible = true;
        vm.proDetNodeName = row.nodeName;
        if (res.success) {
          var data = res.content;
          if (!data || !data.length) {
            return vm.$message('未查询到当前事项的补正信息');
          }
          var resList = [];
          for (var i = 0; i < data.length; i++) {
            var list = [];
            var matinstList = data[i].matinstList || [];
            for (var j = 0; j < matinstList.length; j++) {
              var temp = matinstList[j];
              var type = "原件";
              var need = temp.dueCopyCount || temp.duePaperCount;
              var real = temp.realCopyCount || temp.realPaperCount;
              if (temp.dueCopyCount || temp.realCopyCount) {
                type = "复印件";
              } else if (temp.attCount) {
                type = "电子件";
                need = 1;
                real = temp.attCount;
              }
              var mat0 = {
                matName: temp.matName,
                matType: type,
                need: need,
                real: real
              };
              list.push(mat0);
            }
            var tmpO = {
              id: data[i].id,
              nodeName: data[i].nodeName,
              man: data[i].man,
              reason: data[i].commentMessage,
              startTime: data[i].startDate,
              endTime: data[i].endDate,
              status: data[i].signState,
              mat: data[i].matNum,
              list: list
            };
            resList.push(tmpO);
          }
          vm.processDetList = resList;
        } else {
          vm.$message.error(res.message);
        }
      }, function () {
        vm.parentPageLoading = false;
        vm.$message.error('获取事项的补正信息失败')
      });
    },
    // 打开证件预览弹窗
    openPrintLicenceDialog: function (row) {
      var vm = this;
      vm.preLicenceIframeSrc = '';
      vm.prePrintIframeSrc = '';
      vm.printLicenseLoading = true;
      window.setTimeout(function () {
        vm.printLicenseLoading = false;
      }, 5000);
      vm.licenceDialogVisible = true;
      var src = ctx + 'rest/receive/preview/construct/permit';
      vm.currentCertinstId = row.certinstId;
      vm.preLicenceIframeSrc = src + '?print=false&certinstId=' + row.certinstId;
      vm.prePrintIframeSrc = src + '?print=true&certinstId=' + row.certinstId;
    },
    closePrintLicenseDialog: function () {
      vm.preLicenceIframeSrc = '';
      vm.prePrintIframeSrc = '';
    },
    // 点击下载证件
    clickDownloadLicense: function () {
      window.open(ctx+'rest/docTemplate/downLoad?certinstId='+vm.currentCertinstId);
    },
    // 点击打印证件
    clickPrintLicense: function () {
      // $('#prePrintIframeId')[0].contentWindow.print();
      var resumeWindow = document.getElementById("prePrintIframeId").contentWindow;
      resumeWindow.document.execCommand('print', false, null);
    },
    seeAllProcessPic: function () {
      var tempwindow = window.open();
      setTimeout(function () {
        tempwindow.location = ctx + 'rest/project/diagram/status/projInfo?applyinstId=' + vm.masterEntityKey;
      }, 200)
    },
    openCreditDialog: function (reqData) {
      // 获取信用类型数据字典
      var vm = this;
      vm.creditHintIconClass = reqData.isBlackDia ? 'el-icon-error' : 'el-icon-warning';
      if (reqData.bizType == 'u') {
        if (reqData.isBlackDia) {
          vm.creditHintTitle = '该企业已被列入黑名单';
        } else {
          vm.creditHintTitle = '该企业存在失信记录';
        }
      } else {
        if (reqData.isBlackDia) {
          vm.creditHintTitle = '该申请人已被列入黑名单';
        } else {
          vm.creditHintTitle = '该申请人存在失信记录';
        }
      }
      this.getCreditType(function () {
        vm.parentPageLoading = true;
        var len = vm.creditTypeList.length;
        var count = 0; // 请求成功个数
        var isFail = false;
        // 获取每个信用类型简要list数据
        vm.creditTypeList.forEach(function (u, index1) {
          request('', {
            url: ctx + 'aea/credit/redblack/listCreditSummaryByBizId',
            type: 'get',
            data: {
              bizType: reqData.bizType,
              bizId: reqData.bizId,
              // bizType: 'u',
              // bizId: 'fb57eb95-b5ab-40ff-b0ea-8291e63c3750', // 给定一个id
              creditType: u.itemCode,
            },
          }, function (res) {
            if (isFail) return null;
            if (res.success) {
              u.preList = res.content || [];
              u.preList.forEach(function (uu, index2) {
                uu.isOpen = false;
                uu.isLoad = false;
              })
              if (++count == len) {
                vm.parentPageLoading = false;
                vm.creditDiaVisible = true;
                // 全部请求成功，展示数据
                // console.log(vm.creditTypeList)
              }
            } else {
              isFail = true;
              vm.parentPageLoading = false;
              vm.$message.error(res.message);
            }
          }, function () {
            if (isFail) return null;
            isFail = true;
            vm.parentPageLoading = false;
            vm.$message.error('获取信用数据失败, creditType=' + u.itemCode);
          })
        });
      });
    },
    clickCreditPre: function (row) {
      var vm = this;
      if (row.isOpen) {
        row.isOpen = false;
        vm.$forceUpdate();
        return null
      } else if (row.isLoad) {
        row.isOpen = true;
        vm.$forceUpdate();
        return null;
      }
      vm.creditDiaLoading = true;
      request('', {
        url: ctx + 'aea/credit/redblack/listCreditDetailBySummaryId?summaryId=' + row.summaryId,
        type: 'get',
      }, function (res) {
        vm.creditDiaLoading = false;
        if (res.success) {
          row.isLoad = true;
          row.isOpen = true;
          row.detail = res.content || [];
          vm.$forceUpdate();
        } else {
          vm.$message.error(res.message);
        }
      }, function () {
        vm.creditDiaLoading = false;
        vm.$message.error('加载详情失败')
      })
    },
    // 获取信用类型数据字典
    getCreditType: function (done) {
      var vm = this;
      if (this.creditTypeList.length) return done();
      vm.parentPageLoading = true;
      request('', {
        url: ctx + 'aea/credit/redblack/listCreditType',
        type: 'get',
      }, function (res) {
        vm.parentPageLoading = false;
        if (res.success) {
          if (!res.content || !res.content.length) {
            return vm.$message('未获取到信用类型数据');
          }
          vm.creditTypeList = res.content;
          done();
        } else {
          vm.$message.error(res.message);
        }
      }, function () {
        vm.parentPageLoading = false;
        vm.$message.error('获取信息类型失败')
      });
    },
    closePdfDialog: function () {
      this.pdfSrc = '';
    },
    // icon 字体文件
    getIconText: function (type) {
      return __STATIC.getIconText(type || "DEFAULT");
    },
    // icon 字体文件颜色
    getIconColor: function (type) {
      return __STATIC.getIconColor(type || "DEFAULT");
    },
    getIteminstIdByTaskId: function (callback) {
      var vm = this;
      var url = ctx + 'rest/approve/basic/getIteminstId?taskId=' + vm.taskId;
      request('', {
        url: url,
        type: 'get',
      }, function (res) {
        if (res.content) {
          vm.iteminstState = res.content.iteminstState;
          vm.iteminstId = res.content.iteminstId;
          vm.iteminstProcessinstId = res.content.procinstId;
          vm.isSeriesinst = res.content.isSeriesinst;
        }
        typeof callback == 'function' && callback();
      }, function () {
      });
      
    },
    //检查字符串为空
    checkNull: function (str) {
      if (!str || (typeof str == 'string' && !str.replace(/(^\s*)|(\s*$)/g, '')) || 'null' == str)
        return true;
      return false;
    },
    checkNotNull: function (str) {
      return !this.checkNull(str);
    },
    dealNull: function (str) {
      return this.checkNull(str) ? "" : str;
    },
    getUrlParam: function (name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      }
      return null;
    },
    initPage: function () {
      var vm = this;
      vm.taskId = vm.getUrlParam('taskId');
      vm.isDraftPage = vm.getUrlParam('draft');
      vm.isZJItem = (vm.getUrlParam('itemNature') == '8');
      // vm.isZJItem = true;
      // vm.isDraftPage = 'true';
      vm.getIteminstIdByTaskId(callback);
      
      function callback() {
        vm.busRecordId = this.getUrlParam('busRecordId');
        if (vm.busRecordId != 'undefined' && vm.busRecordId != 'null' && vm.busRecordId != null && vm.busRecordId != '') {
          vm.iteminstId = vm.busRecordId;
        }
        vm.attLink.recordId = vm.taskId;
        vm.viewId = vm.getUrlParam("viewId");
        vm.initProcessTurningPage(vm.taskId);
      }
    },
    // 初始化流程审批界面
    initProcessTurningPage: function (taskId) {
      var vm = this;
      var isNotCompareAssignee = vm.getUrlParam('isNotCompareAssignee') == 'true' ? 'true' : 'false';
      request('bpmFrontUrl', {
        url: ctx + 'rest/front/process/getProcessTurningParams/' + taskId + '?isNotCompareAssignee=' + isNotCompareAssignee,
        type: 'get'
      }, function (res) {
        if (res.success) {
          //获取初始化参数
          vm.taskName = res.content.taskName;
          vm.appFlowdefId = res.content.appFlowdefId;
          vm.formJson = res.content.formJson;
          vm.flowModel = res.content.flowModel;
          vm.appComment = res.content.appComment;
          vm.appinstId = res.content.appinstId;
          vm.processInstanceId = res.content.processInstanceId;
          vm.currTaskDefId = res.content.currTaskDefId;
          vm.currProcDefVersion = res.content.currProcDefVersion;
          vm.isSuspended = res.content.isSuspended;//挂起流程
          vm.masterEntityKey = res.content.masterEntityKey;//applyinstId
          vm.isCheck = res.content.isCheck;
          //附件管理相关参数
          vm._tableName = res.content.masterEntityKey;
          vm._pkName = res.content.masterEntityKey;
          vm.recordId = vm.taskId;
          vm.recordIds = res.content.recordIds;// 附件关联id
          vm.attLink.recordId = vm.taskId;
          vm.isApprover = (vm.iteminstProcessinstId != null && vm.iteminstProcessinstId == vm.processInstanceId) ? '1' : '0';
          // vm.isApprover = 0;
          vm.getWayAType();
        } else {
          vm.$message.error(res.message);
          //初始化失败，关闭该页面
          delayCloseWindow();
        }
      }, function () {
        vm.$message.error(vm.requestErrorText);
        //初始化失败，关闭该页面
        delayCloseWindow();
      });
    },
    checkAllowOpeFile: function () {
      var flag = false;
      if (this.friNodeId == 'sbcl_2018_augurit') {
        this.$message.error('请选择其它文件，申办材料文件夹不允许操作');
        flag = true
      } else if (this.friNodeId == 'pwpf_2018_augurit') {
        this.$message.error('请选择其它文件，批文批复文件夹不允许操作');
        flag = true
      }
      return flag;
    },
    clickUploadBtn: function () {
      if (this.checkAllowOpeFile()) return null;
      // if (this.selectedNodeId == -1) {
      //   return this.$message.error('请点击选择一个文件夹后进行上传');
      // }
      $('.upload-input').trigger('click');
    },
    downloadFiles: function () {
      if (this.selectedNodeId == -1) {
        return this.$message.error('请点击选择一个文件夹后进行下载');
      }
      var ids = [];
      this.selectedNode.list.forEach(function (u) {
        u.isLeaf && ids.push(u.detailId);
      });
      window.open(ctx + 'rest/mats/att/download?detailIds=' + ids.join(','), '_blank')
    },
    getAttachment: function (dirId, node) {
      var vm = this;
      var params = {
        tableName: tableName,
        recordIds: vm.masterEntityKey,
        isSeries: vm.isSeriesinst,
        pkName: pkName,
      };
      if (dirId) {
        params.dirId = dirId
      }
      vm.fileLoading = true;
      request('', {
        url: ctx + 'rest/approve/att/dirs/file/list',
        type: 'get',
        data: params,
      }, function (res) {
        vm.fileLoading = false;
        if (res.success) {
          var tmpArr = [];
          res.content.dirs.forEach(function (u) {
            u.fileText = u.dirName;
            u.isLeaf = false;
            u.list = [];
            u.isLoaded = false;
            tmpArr.push(u)
          })
          res.content.files.forEach(function (u) {
            u.fileText = u.attName;
            u.isLeaf = true;
            u.dirId = u.detailId;
            u.isLoaded = false;
            u.list = [];
            tmpArr.push(u)
          })
          if (dirId) {
            if (node) {
              node.isLoaded = true;
              node.list = tmpArr
            } else {
              vm.selectedNode.isLoaded = true;
              vm.selectedNode.list = tmpArr;
            }
          } else {
            vm.treeData = tmpArr;
          }
        } else {
          this.$message.error(res.message)
        }
      }, function (err) {
        vm.$message.error('文件上传失败')
        vm.fileLoading = false;
      })
    },
    // 新建文件夹
    createFolder: function () {
      var vm = this;
      if (this.checkAllowOpeFile()) return null;
      this.$prompt('请输入文件夹名称', '新建文件夹', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(function (obj) {
        var flag = !!vm.selectedNode; // 是否在文件夹下新建文件夹
        var reqData = {
          isRoot: 1,
          dirName: obj.value,
          tableName: tableName,
          pkName: pkName,
          recordId: vm.masterEntityKey
        };
        if (flag) {
          // 在文件夹下新建文件夹
          reqData.isRoot = 0;
          reqData.parentId = vm.selectedNodeId;
        }
        vm.fileLoading = true;
        request('', {
          url: ctx + 'rest/approve/att/file/create/dir',
          type: 'post',
          data: reqData,
        }, function (res) {
          vm.fileLoading = false;
          if (flag) {
            //
            vm.getAttachment(vm.selectedNodeId, vm.selectedNode);
          } else {
            res.content.fileText = res.content.dirName;
            vm.treeData.push(res.content);
          }
        });
      }).catch(function () {
        vm.fileLoading = false;
      });
    },
    //根据初始化参数，获取页面元素权限信息
    initFormElementPriv: function () {
      var vm = this;
      if (vm.checkNotNull(vm.currTaskDefId) && vm.currProcDefVersion > 0) {
        var params = {
          version: vm.currProcDefVersion,
          privMode: 'act-view',
          viewId: vm.getUrlParam("viewId"),
          actId: vm.currTaskDefId,
          appFlowdefId: vm.appFlowdefId,
        }
        request('bpmFrontUrl', {
          url: ctx + 'rest/front/task/getAuthorizeElementPlus',
          type: 'get',
          data: params,
        }, function (res) {
          vm.parentPageLoading = false;
          if (res.success) {
            var result = res.content;
            if (vm.checkNull(result)) {
              return;
            }
            //1、判断是否获取到权限信息
            if (result.noDefaultDealView) {
              vm.$message.info("保存成功！因当前系统没有配置流程待办视图，请到待办工作中继续办理！");
              return;
            }
            // buttons
            if (
                result.panel && result.panel.length > 0
                && result.panel.filter(function (u) {
                  return u.elementCode === 'wf_button_toolbar'
                }).length
            ) {
              //这里可以做交集，确保起草时有暂存按钮，审批时 有流程跟踪按钮
              vm.buttonData = result.panel.filter(function (u) {
                return u.elementCode === 'wf_button_toolbar'
              })[0]['childElement'] || [];
              vm.filterButtonArray(vm.buttonData);
              vm.sortButtonArray(vm.buttonData);
            }
            vm.initButtons();
            // 设置是否展示所有附件面板
            if (
                result.panel && result.panel.length > 0
                && result.panel.filter(function (u) {
                  return u.elementCode === 'wf_mati_toolbar'
                }).length
            ) {
              var targetData = result.panel.filter(function (u) {
                return u.elementCode === 'wf_mati_toolbar'
              })[0];
              if (targetData.isHidden == '0') {
                vm.isShowMatiPanel = true;
              }
            }
            // 开发模式，直接显示材料面板
            if (isDevelop) {
              vm.isShowMatiPanel = true;
            }
            vm.isShowMatiPanel = true;
            if (vm.isShowMatiPanel) {
              vm.getAttachment(); //初始化附件管理页面
            }
            // iframes
            vm.formData = result.form || [];
            if (result.form && result.form.length) {
              var forms = result.form;
              var index = -1;
              forms.forEach(function (u, ind) {
                if (u.isMasterForm == '1') {
                  index = ind
                }
              })
              if (index != -1) {
                vm.formData = [forms[index]].concat(forms.slice(0, index).concat(forms.slice(index + 1)))
              }
            }
            vm.initForms();
          } else {
            vm.$message.error(res.message);
          }
        }, function () {
          vm.parentPageLoading = false;
          vm.$message.error(" 没有获取到工具栏按钮权限数据");
        });
      } else {
        vm.initForms();
        vm.initButtons();
      }
    },
    // 初始化左边 iframe
    initForms: function () {
      var vm = this;
      var oneFromSrc = '';
      var lTabsData = [
        {label: '申请表', labelId: "1", src: './applyForm.html'},
        {label: '一张表单', labelId: "oneFrom", src: 'urlStageOneForm.html',},
        {label: '材料附件', labelId: "2", src: './materialAnnex.html'},
        {label: '审批过程', labelId: "3", src: './opinionForm.html'},
        {label: '材料补正', labelId: "4", src: './opinionForm.html'},
        {label: '特殊程序', labelId: "5", src: './opinionForm.html'},
        {label: '批文批复', labelId: "6", src: './approvalOpinions.html',}
      ];
      //去掉基本信息、批文批复、隐藏的form
      vm.formData.forEach(function (u) {
        if (
            u.formCode != 'form-code-00000210' &&
            u.formCode != 'form-code-000001027' &&
            u.isHidden != '1'
        ) {
          var flag = (u.formLoadUrl.indexOf("http://") == 0 || u.formLoadUrl.indexOf("https://") == 0 || u.formLoadUrl.indexOf("file:///") == 0);
          lTabsData.push({
            label: u.formName,
            src: flag ? u.formLoadUrl : ctx + u.formLoadUrl,
          })
        }
      });
      var urlParam = [
        'taskId',
        'isSeriesinst',
        // 'itemInstId',
        'iteminstId',
        'processInstanceId',
        'busRecordId',
        'isApprover',
        'isZJItem',
        'masterEntityKey',
        'projectCode',
        'viewId',
        'projInfoId',
      ];
      var oneFormIndex = -1;
      lTabsData.forEach(function (u, i) {
        urlParam.forEach(function (uu, ii) {
          var chart = ii == 0 ? '?' : '&';
          if (u.labelId == 'oneFrom') {
            oneFromSrc = u.src + chart + uu + '=' + vm[uu];
            oneFormIndex = i;
          }
          u.src = u.src + chart + uu + '=' + vm[uu];
        });
      });
      vm.lTabsData = lTabsData;
      if (vm.isShowOneForm == '1' && !vm.isZJItem) { // 中介事项也不显示一张表单
        if (vm.isSeriesinst == '0') { // 多事项
          var targetUrl = oneFromSrc;
          if (vm.isApprover == '1') {
            targetUrl += '&enableParamItem=true&itemId=' + vm.itemVersionId;
          } else {
            targetUrl += '&enableParamItem=false';
          }
          request('', {
            url: targetUrl,
            type: 'get',
          }, function (res) {
            if (res.success) {
              var reder2Url = res.content;
              request('', {
                url: reder2Url,
                type: 'get',
              }, function (res2) {
                if (res2.success) {
                  res2.content.forEach(function (u, index) {
                    if (u.smartForm) {
                      u.formUrl = u.formUrl.replace('showBasicButton=true', 'showBasicButton=false')
                      getHtml(u, index);
                    } else {
                      u.formUrl += '&showBasicButton=false';
                    }
                  });
                  vm.smartFormInfo = res2.content;
                }
              }, function (res2) {
                vm.$message.error(res2.message);
                $('#oneForm').html(res2.message);
              });
            }
          }, function () {
            $('#oneForm').html('未配置一张表单信息');
          });
        } else { // 单事项
          request('', {
            url: ctx + 'rest/oneform/common/getListForm4ItemOneForm',
            type: 'get',
            data: {
              applyinstId: vm.masterEntityKey,
              // stageId: vm.stageId,
              projInfoId: vm.projInfoId,
              itemId: vm.itemVersionId,
              // applyinstId: 'fcf9d937-f670-4871-a430-34b01cafde9b',
              // stageId: 'f39985ed-9119-444f-b744-4167762a3872',
              // projInfoId: '347db5f9-f55f-44eb-9d1a-983ca263e8c4',
              showBasicButton: false,
              includePlatformResource: false,
            },
          }, function (res) {
            if (res.success) {
              res.content.forEach(function (u, index) {
                if (u.smartForm) {
                  u.formUrl = u.formUrl.replace('showBasicButton=true', 'showBasicButton=false')
                  getHtml(u, index);
                } else {
                  u.formUrl += '&showBasicButton=false';
                }
              });
              vm.smartFormInfo = res.content;
            } else {
              vm.$message.error(res.content || '获取一张表单信息失败');
            }
          }, function () {
            vm.$message.error('获取一张表单信息失败');
          });
        }
        
        function getHtml(data, index) {
          request('', {
            url: ctx + data.formUrl,
            type: 'get',
          }, function (res) {
            if (res.success) {
              $('#smartFormBox_' + index).html(res.content);
            } else {
              // vm.$message.error('获取智能表单数据失败');
            }
          }, function () {
            // vm.$message.error('获取智能表单数据失败');
          })
        }
        
        // request('', {
        //   url: oneFromSrc,
        //   type: 'get',
        // }, function (res) {
        //   if (res.success) {
        //     var reder2Url = res.content;
        //     request('', {
        //       url: reder2Url,
        //       type: 'get',
        //     }, function (res2) {
        //       if (res2.success) {
        //         $('#oneForm').html(res2.content);
        //         vm.$nextTick(function () {
        //           $('#oneForm').html(res2.content)
        //         });
        //       }
        //     }, function (res) {
        //       vm.$message.error(res.message);
        //       $('#oneForm').html(res.message);
        //
        //     });
        //   }
        // }, function () {
        //   $('#oneForm').html('未配置一张表单信息');
        // });
      } else {
        //下面是删除一张表单的tab，注意依赖数组的脚标
        if (oneFormIndex != -1) {
          vm.lTabsData.splice(oneFormIndex, 1);
        }
      }
      // 材料补正
      var tmpIndex = -1;
      vm.lTabsData.forEach(function (u, i) {
        if (u.labelId == 4) {
          tmpIndex = i
        }
      })
      if (vm.hasSupply != 1) {
        vm.lTabsData.splice(tmpIndex, 1);
      } else {
        vm.loadSupplyDetail();
      }
      // 特殊程序
      var tmpIndex = -1;
      vm.lTabsData.forEach(function (u, i) {
        if (u.labelId == 5) {
          tmpIndex = i
        }
      })
      if (vm.hasSpecial != 1) {
        vm.lTabsData.splice(tmpIndex, 1);
      } else {
        vm.getSpecialType();
        vm.loadSpecialDetail();
      }
    },
    // 初始化左边按钮组
    initButtons: function () {
      var vm = this;
      var defaultBtn = [{
        elementName: "全景图",
        elementCode: "wfBusSave",
        columnType: "button",
        isReadonly: '0',
        isHidden: '0',
        elementRender: '<button class="btn btn-outline-info" onclick="seeAllProcessPic()">查看全景图</button>'
      }, {
        elementName: "查看流程图",
        elementCode: "wfBusSave",
        columnType: "button",
        isReadonly: '0',
        isHidden: '0',
        elementRender: '<button class="btn btn-outline-info" onclick="showDiagramDialog()">查看流程图</button>'
      }];
      var matBtn = [{
        elementName: "材料补正",
        elementCode: "wfBusSave",
        columnType: "button",
        isReadonly: '0',
        isHidden: '0',
        elementRender: '<button class="btn btn-outline-info" onclick="startSupplementForItem()">材料补正</button>'
      }];
      var draftBtn = [{
        elementName: "发起申报",
        elementCode: "wfBusSave",
        columnType: "button",
        isReadonly: '0',
        isHidden: '0',
        elementRender: '<button class="btn btn-primary" onclick="startDeclare()">发起申报</button>'
      }, {
        elementName: "打印回执",
        elementCode: "wfBusSave",
        columnType: "button",
        isReadonly: '0',
        isHidden: '0',
        elementRender: '<button class="btn btn-outline-info" onclick="getPrintList()">打印回执</button>'
      }];
      if (vm.isApprover == 1) {
        defaultBtn = matBtn.concat(defaultBtn);
      }
      if (vm.isDraftPage == 'true') {
        defaultBtn = draftBtn.concat(defaultBtn);
      }
      if (!vm.checkNull(vm.taskId)) {
        //如果不是起草界面则加上流程跟踪按钮
        vm.buttonData = vm.buttonData.concat(defaultBtn)
      }
      var buttonDatas = vm.buttonData;
      var newButtonData = [];
      for (var i in buttonDatas) {
        var button = buttonDatas[i];
        if (button.isHidden == 0) {
          //替换 id为 当前页面的任务id 变量值
          var realText = button.elementRender.replace("#[id]", vm.taskId);
          if (button.isReadonly == 1) {
            //如果当前按钮别锁定，即不可点击，则设置为disabled 且去掉 onclick事件，避免被前端修改。
            var id = "button_" + vm.taskId + i;
            if (realText.indexOf("onclick") != -1) {
              realText = realText.replace("onclick", " id='" + id + "' disabled title = '已被锁定' data-clc");
            } else {
              realText = realText.replace("onclick", " id='" + id + "' disabled title = '已被锁定' data-clc");
            }
            button.elementRender = realText;
            $("#" + id).removeAttr("data-clc");
          } else {
            button.elementRender = realText;
          }
          newButtonData.push(button);
        }
      }
      vm.buttonData = newButtonData;
      // 控制动态按钮，如果超过一定的宽度，给出省略号
      vm.setButtonShow();
    },
    setButtonShow: function () {
      var vm = this;
      var winWidth = $(".top-btns-status").outerWidth();
      var buttonRightWidth = $(".right-status-box").outerWidth();
      var butttonLeftWidth = winWidth - buttonRightWidth - 42 - 15; // padding+图标宽度
      if (vm.buttonData !== null && vm.buttonData.length > 0) {
        var _len = vm.buttonData.length;
        if (_len * 125 > butttonLeftWidth) {
          vm.showMenuMore = true;
          vm.menuCount = parseInt(butttonLeftWidth / 125);
        } else {
          vm.showMenuMore = false;
          vm.menuCount = vm.buttonData.length;
        }
      } else {
        vm.showMenuMore = false;
        if (vm.buttonData) {
          vm.menuCount = vm.buttonData.length;
        }
      }
      vm.initButtonDataCount(vm.buttonData, vm.menuCount)
    },
    initButtonDataCount: function (eleLi, n) {  // 重置按钮数据
      if (eleLi) {
        this.buttonDataShow = eleLi.slice(0, n);
        this.buttonDataHide = eleLi.slice(n);
      }
    },
    //工具栏按钮排序
    sortButtonArray: function (arr) {
      var compare = function (obj1, obj2) {
        var val1 = obj1.sortNo;
        var val2 = obj2.sortNo;
        if (val1 && val2) {
          if (val1 < val2) {
            return -1;
          } else if (val1 > val2) {
            return 1;
          } else {
            return 0;
          }
        }
      }
      arr.sort(compare);
    },
    //工具栏按钮过滤
    filterButtonArray: function (arr) {
      var vm = this;
      var tmpArr = [];
      for (var i = 0; i < arr.length; i++) {
        var u = arr[i];
        //如果是挂起，则把挂起按钮隐藏，否则把激活按钮隐藏
        if (vm.isSuspended == '1') {
          if (arr[i].elementCode == "suspendProcess") {
            arr[i].isHidden = '1';
          }
        } else {
          if (arr[i].elementCode == "activateProcess") {
            arr[i].isHidden = '1';
          }
        }
        if (arr[i].isHidden == "0") {
          if (u.elementCode == 'item_tuichutebiechengxu') {
            // 当前需求永远都不显示退出特殊程序按钮
            if (vm.iteminstState == '9') {
              // tmpArr.push(u)
            }
          } else if (u.elementCode == 'item_jinrutebiechengxu') {
            // 是否有进入特殊程序按钮
            if (vm.iteminstState != '9') {
              tmpArr.push(u)
            }
          } else {
            // 不是上面的两种按钮，直接push
            tmpArr.push(u)
          }
        }
      }
      vm.buttonData = tmpArr;
    },
    /**
     *审批详情页 --> 获取申报方式和状态
     */
    getWayAType: function () {
      var vm = this;
      request('bpmFrontUrl', {
        url: ctx + 'rest/approve/type/and/state',
        type: 'get',
        data: {
          applyinstId: vm.masterEntityKey, //
          taskId: vm.taskId,
        },
      }, function (res) {
        // console.log("获取申报方式和状态", res)
        if (res.success) {
          vm.typeAstateData = res.content;
          vm.isSeriesinst = vm.typeAstateData.isSeriesinst; //传参用来判断是否串并联
          // vm.isApprover = vm.typeAstateData.isApprover;
          vm.stageOrItemName = res.content.stageOrItemName;
          vm.currentStateValue = res.content.currentStateValue;
          vm.checkIsApprover = res.content.isApprover;
          vm.projectCode = res.content.projCode;
          vm.hasSpecial = res.content.hasSpecial;
          vm.hasSupply = res.content.hasSupply;
          vm.isShowOneForm = res.content.isShowOneForm;
          vm.stageId = res.content.stageId;
          vm.projInfoId = res.content.projId;
          vm.itemVersionId = res.content.itemVerId;
          vm.initFormElementPriv();
        } else {
          vm.$message.error(res.message);
        }
      }, function () {
      });
    },
    // 加载流程图
    showDiagramDialog: function () {
      // 清空容器
      $('#flowChartBox').html('');
      // 渲染流程图
      _showProcessDiagram(this.processInstanceId, this.isCheck)
      this.processDialogVisible = true;
    },
    //获取右边所有的意见列表
    // getCommentListAll: function () {
    //   var _that = this;
    //   var vm = this;
    //   request('', {
    //     url: ctx+'rest/bpm/approve/comment/tree?processInstanceId='+vm.processInstanceId+'&applyinstId='+vm.masterEntityKey,
    //     type: 'get',
    //     // url: ctx + 'rest/bpm/approve/comments/list/all',
    //     // data: {
    //     //   processInstanceId: _that.processInstanceId,
    //     //   applyinstId: vm.masterEntityKey,
    //     // },
    //   }, function (res) {
    //     _that.approveProcessLoading = false;
    //     if (res.success) {
    //       _that.approveStepList = res.content || [];
    //     } else {
    //       vm.$message.error(res.message);
    //     }
    //   }, function () {
    //     _that.approveProcessLoading = false;
    //     vm.$message.error("获取意见列表失败");
    //   });
    // },
    // 查看情形
    showParState: function () {
      var vm = this;
      vm.parentPageLoading = true;
      var statesUrl;
      if (vm.isSeriesinst == '1') {
        statesUrl = ctx + 'rest/approve/series/selected/states?applyinstId=' + vm.masterEntityKey // 查看串联申报已选择情形
      } else {
        statesUrl = ctx + 'rest/approve/parallel/selected/states?applyinstId=' + vm.masterEntityKey // 查看并联申报已选择情形
      }
      request('bpmFrontUrl', {
        url: statesUrl,
        type: 'get'
      }, function (res) {
        vm.parentPageLoading = false;
        if (res.success) {
          vm.statesData = res.content;
          vm.processStatesDialog = true;
        } else {
          vm.$message.error(res.message || '加载情形失败');
        }
      }, function () {
        vm.parentPageLoading = false;
        vm.$message.error('加载情形失败');
      });
    },
//----------------组织机构树查询
    assigneeSearch: function () {
      if (vm.checkNotNull(vm.assigneeSearchKeyword)) {
        vm.assigneeSearchKeyword = vm.assigneeSearchKeyword.trim();
      } else {
        vm.assigneeSearchKeyword = '';
      }
      var url = zTreeSetting.async.url;
      if (url.indexOf("keyword=") == -1) {
        zTreeSetting.async.url = url + "&keyword=" + encodeURI(vm.assigneeSearchKeyword);
      } else {
        zTreeSetting.async.url = url.substring(0, url.lastIndexOf("keyword=")) + "keyword=" + vm.assigneeSearchKeyword;
      }
      zTreeObj = jQuery.fn.zTree.init(jQuery("#assigneeOrgTree"), zTreeSetting);
    },
    //选择办理人树相关
    getAssigneeTree: function () {
      //1、记录临时变量
      vm.tempDefaultSelectedAssigneeId = vm.nextTaskAssigneeId;
      var tempDestActId = vm.sendTaskInfo.destActId;
      //2、清空已选的办理人列表，查询框
      $("#selectedAssignees").empty();
      vm.selectedAssignee = [];
      vm.assigneeSearchKeyword = '';
      //获取办理人范围key
      $.ajaxSetup({
        xhrFields: {
          withCredentials: true,
        }
      })
      request('bpmFrontUrl', {
            url: ctx + 'rest/front/task/getNextTaskAssigneeRange',
            type: 'get',
            ContentType: 'application/json',
            data: {taskId: vm.taskId, destActId: vm.sendTaskInfo.destActId}
          }, function (result) {
            if (result.success) {
              vm.assigneeRangeKey = result.content.assigneeRange;
              if (vm.checkNull(vm.assigneeRangeKey)) return;
              zTreeSetting.async.url = ctx + "rest/front/task/getAssigneeRangeZTree?assigneeRangeKey=" + vm.assigneeRangeKey + "&procInstId=" + vm.processInstanceId + "&destActId=" + tempDestActId;
              vm.zTreeObj = jQuery.fn.zTree.init(jQuery("#assigneeOrgTree"), zTreeSetting);
            } else {
              vm.$message.error(result.message);
            }
          }, function () {
            vm.$message.error("获取不到下个环节办理人范围！");
          }
      );
    },
    mutiGetAssigneeTree: function (row) {
      //1、记录临时变量
      var vm = this;
      vm.treeTargetRow = row;
      vm.tempDefaultSelectedAssigneeId = row.defaultSendAssigneesId;
      vm.nextTaskAssignee = row.defaultSendAssignees;
      vm.nextTaskAssigneeId = row.defaultSendAssigneesId;
      var tempDestActId = row.destActId;
      //2、清空已选的办理人列表，查询框
      $("#selectedAssignees").empty();
      vm.selectedAssignee = [];
      vm.assigneeSearchKeyword = '';
      //获取办理人范围key
      $.ajaxSetup({
        xhrFields: {
          withCredentials: true,
        }
      })
      request('bpmFrontUrl', {
            url: ctx + 'rest/front/task/getNextTaskAssigneeRange',
            type: 'get',
            ContentType: 'application/json',
            data: {taskId: vm.taskId, destActId: row.destActId}
          }, function (result) {
            if (result.success) {
              vm.assigneeRangeKey = result.content.assigneeRange;
              if (vm.checkNull(vm.assigneeRangeKey)) return;
              zTreeSetting.async.url = ctx + "rest/front/task/getAssigneeRangeZTree?assigneeRangeKey=" + vm.assigneeRangeKey + "&procInstId=" + vm.processInstanceId + "&destActId=" + tempDestActId;
              vm.zTreeObj = jQuery.fn.zTree.init(jQuery("#assigneeOrgTree"), zTreeSetting);
            } else {
              vm.$message.error(result.message);
            }
          }, function () {
            vm.$message.error("获取不到下个环节办理人范围！");
          }
      );
    },
    //确认选择
    confirmSelectAssignee: function () {
      var vm = this;
      var selectedAssignee = vm.selectedAssignee;
      //点击确定按钮，拼接参数回填到上一级窗口的表单上，如果选择为空，则还是使用默认办理人
      var assignee = "";
      var assigneeCn = "";
      if (selectedAssignee.length > 0) {
        for (var i in selectedAssignee) {
          assignee += selectedAssignee[i].id + vm.ID_SPLIT;
          assigneeCn += selectedAssignee[i].text + vm.TEXT_SPLIT;//中文名用顿号
        }
        vm.nextTaskAssigneeId = assignee.substring(0, assignee.length - 1);
        vm.nextTaskAssignee = assigneeCn.substring(0, assigneeCn.length - 1);
        vm.sendTaskInfo.defaultSendAssignees = vm.nextTaskAssignee;
        vm.sendTaskInfo.defaultSendAssigneesId = vm.nextTaskAssigneeId;
      } else {
        vm.nextTaskAssignee = vm.sendTaskInfo.defaultSendAssignees;
        vm.nextTaskAssigneeId = vm.sendTaskInfo.defaultSendAssigneesId;
      }
      //设置到提交参数中
      vm.sendParam.sendConfigs[0].assignees = vm.nextTaskAssigneeId;
      //回显到发送信息框中
      vm.$set(vm.sendForm, 'nextTaskAssignee', vm.nextTaskAssignee);
      //关闭选择框
      vm.selectAssigneeDialog = false;
      if (vm.isMultiFlow) {
        vm.mutiCheckedMan.forEach(function (u) {
          if (u.destActId == vm.treeBtnLeft.destActId) {
            u.defaultSendAssignees = assigneeCn.substring(0, assigneeCn.length - 1);
            u.defaultSendAssigneesId = assignee.substring(0, assignee.length - 1);
          }
        })
      }
    },
    handleSelectionChange: function () {
    },
    checkParentProExist: function (procInstId) {
      request('bpmFrontUrl', {
        url: ctx + 'rest/front/process/getParentProcessInstanceId/' + procInstId,
        type: 'get'
      }, function (result) {
        if (result.success) {
          $('#parentPro').show();
          $('#parentPro').empty();
          var btn = '<button class="btn btn-default" style="margin-top:16px;" onclick="vm.getParentProcess(\'' + result.content.processInstanceId + '\',' + result.content.isCheck + ')">返回上级流程</button>';
          $('#parentPro').append(btn);
        } else {
          $('#parentPro').hide();
        }
      }, function () {
      });
    },
    //显示父流程流程图
    getParentProcess: function (procInstId, isCheck) {
      $('#bpmnModel').html('');//先清空流程图容器div
      _showParentProcessDiagram(procInstId, isCheck);
    },
    //显示子流程流程图
    showChildrenDiagramDialog: function (childProcInstId, isCheck) {
      $('#bpmnModel').html('');//先清空流程图容器div
      _showChildrenProcessDiagram(childProcInstId, isCheck);
    },
    //触发子流程
    triggerSubFlow: function (taskId, eventName) {
      vm.processDialogLoading = true;  // 打开遮罩
      var request = jQuery.ajax({
        type: 'get',
        url: ctx + 'rest/front/task/triggerSubFlow' + '?flag=' + new Date().getTime(),
        data: {taskId: taskId, eventName: eventName},
        success: function (data, textStatus, jqXHR) {
          if (data.success) {
            vm.processDialogLoading = false; // 关闭遮罩
            vm.$message.success(data.message);
            setTimeout("location.reload()", 1500);
          } else {
            vm.$message.error(data.message);
            vm.processDialogLoading = false; // 关闭遮罩
          }
        },
        error: function (jqXHR, textStatus, errorThrown) {
          vm.$message.error("请求出错了！");
          vm.processDialogLoading = false; // 关闭遮罩
        }
      });
    },
    // 点击节点
    clickNodeTitle: function (e, node, friNodeId) {
      if (!node.isLeaf) {
        var $this = $(e.target);
        $this.parent().toggleClass('open');
        // if (this.selectedNodeId == node.dirId) {
        // 	this.selectedNodeId = -1;
        // 	this.selectedNode = null;
        // } else {
        this.selectedNodeId = node.dirId;
        this.selectedNode = node;
        this.friNodeId = friNodeId;
        // }
      }
      if (node.isLeaf || node.isLoaded || (node.list && node.list.length)) {
        // 不请求
      } else {
        this.getAttachment(node.dirId);
      }
    },
    // 预览电子件 必须要有detailId
    filePreview: function (data, visibleKey) {
      if (!data.detailId) {
        data.detailId = data.fileId;
      } // 设置detailId
      if (!data.attFormat) {
        data.attFormat = data.fileType;
      } // 文件类型
      data.attFormat = (data.attFormat + '').toLowerCase();
      if (__STATIC.allowPreType[data.attFormat]) {
        return this.preFile(data, visibleKey);
      } // 预览pdf、doc等
      // 预览图片等
      var detailId = data.detailId;
      var flashAttributes = '';
      var tempwindow = window.open('_blank'); // 先打开页面
      tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
    },
    //下载单个附件
    downOneFile: function (data) {
      if (!data.detailId) {
        data.detailId = data.fileId;
      } // 设置detailId
      window.open(ctx + 'rest/mats/att/download?detailIds=' + data.detailId, '_blank')
    },
    //删除单个文件附件
    delOneFile: function (data, parentData) {
      var _that = this;
      var extraText = data.isLeaf ? "" : "夹";
      this.$confirm('此操作将永久删除该文件' + extraText + ', 是否继续?', '删除文件' + extraText, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(function (obj) {
        ensureDelete();
      }).catch(function () {
      });
      
      function ensureDelete() {
        var param = {};
        var reqUrl = ctx;
        if (data.isLeaf) {
          // 删除文件
          param.detailIds = data.detailId;
          reqUrl += 'rest/approve/att/file/dir/delete';
        } else {
          // 删除文件夹
          param.dirId = data.dirId;
          reqUrl += 'rest/approve/att/file/delete/dir';
        }
        request('', {
          url: reqUrl,
          type: 'post',
          data: param,
        }, function (res) {
          if (res.success) {
            _that.$message({
              message: '删除成功',
              type: 'success'
            });
            if (parentData) {
              _that.getAttachment(parentData.dirId, parentData)
            } else {
              _that.getAttachment();
            }
            
          } else {
            _that.$message({
              message: res.message ? res.message : '删除失败',
              type: 'error'
            });
          }
        }, function (msg) {
          _that.$message({
            message: '服务请求失败',
            type: 'error'
          });
        });
      }
    },
//*************************************** 4.0 接口方法 ********************************---start
    //************** 意见框    ----意见下拉选择事件
    //打印回执
    printReceive: function (row, index, ind) {
      this.receiveItemActive = index;
      this.receiveActive = ind;
      var fileUrl = ctx + 'rest/receive/toPDF/' + row.receiveId;
      this.receivePdfSrc = ctx + 'preview/pdfjs/web/viewer.html?file=' + encodeURIComponent(fileUrl)
    },
    // 展开收起打印回执
    toggleReceiveListShow: function (item) {
      item.show = !item.show;
    },
    opinionSelectChange: function (value) {
      vm.$set(vm.opinionForm, 'opinionText', value);
    },
    backOpChange: function (value) {
      vm.backFormData.comment = value;
    },
    //设为常用办理意见
    setCommonOpinion: function (key) {
      var text = vm.opinionForm.opinionText;
      if (key == 'backFormData') {
        text = vm.backFormData.comment;
      }
      if (vm.checkNull(text)) {
        vm.$message.info('办理意见不能为空，请填写！');
        return;
      }
      vm.sendDialogLoading = true;
      request('bpmFrontUrl', {
        url: ctx + 'rest/comment/saveUserOpinion',
        type: 'post',
        data: {message: text}
      }, function (result) {
        vm.sendDialogLoading = false;
        if (result.success) {
          vm.$message.success(result.message);
          //刷新列表
          vm.getOpinionList();
        } else {
          vm.$message.error(result.message);
        }
      }, function () {
        vm.sendDialogLoading = false;
        vm.$message.error('设为常用办理意见失败！');
      });
    },
    //获取审批意见列表
    getOpinionList: function () {
      request('bpmFrontUrl', {
        url: ctx + 'rest/comment/getAllActiveUserOpinions',
        type: 'get',
        async: false,
      }, function (result) {
        if (result.success) {
          vm.opinionTableData = result.content;
        }
      }, function () {
        vm.$message.error('获取常用办理意见失败！');
      });
    },
    //编辑意见-改变流程和状态弹窗用
    editOpinion: function (row) {
      console.log(row)
      vm.editOpinionForm.opinionId = row.opinionId;
      //显示出的值必须用这种方式 赋值，如果用等于号赋值则会导致校验不了
      vm.$set(vm.editOpinionForm, 'opinionContent', row.opinionContent);
      vm.$set(vm.editOpinionForm, 'sortNo', row.sortNo);
      vm.editOpinionDialog = true;
    },
    //编辑意见，只改变状态弹窗用
    getUserOptionById: function (row) {
      var _that = this;
      vm.editOpinionForm.opinionId = row.opinionId;
      vm.$set(vm.editOpinionForm, 'opinionContent', row.opinionContent);
      vm.$set(vm.editOpinionForm, 'sortNo', row.sortNo);
      _that.submitCommentsFlag2 = true;
    },
    //保存编辑意见
    doSaveOpinion: function () {
      var _that = this;
      vm.$refs['editOpinionForm'].validate(function (valid) {
        if (valid) {
          request('bpmFrontUrl', {
            url: ctx + 'rest/comment/editUserOpinion',
            type: 'put',
            data: vm.editOpinionForm
          }, function (result) {
            if (result.success) {
              vm.editOpinionDialog = false;
              vm.getOpinionList();
              vm.$message.success(result.message);
            } else {
              vm.$message.error(result.message);
            }
          }, function () {
            vm.$message.error('编辑办理意见失败！');
          });
        }
      });
    },
    
    deleteOpinion: function (id) {
      if (vm.checkNull(id)) {
        vm.$message.error('删除的意见参数不能为空！');
        return;
      }
      confirmMsg("删除意见", "确定要删除该意见吗？", function () {
        request('bpmFrontUrl', {
          url: ctx + 'rest/comment/deleteUserOpinion/' + id,
          type: 'delete'
        }, function (result) {
          if (result.success) {
            vm.getOpinionList();
            vm.$message.success('删除办理意见成功！');
          } else {
            vm.$message.error('删除办理意见失败！');
            console.log(result.message)
          }
        }, function () {
          console.log('删除办理意见失败！');
        });
      });
    },
    // 判断该事项是否能做推动流程的操作
    checkCanClick: function () {
      var vm = this;
      var text = '';
      if (vm.checkIsApprover == '1') {
        if (vm.currentStateValue == '6' || vm.currentStateValue == '7') {
          text = '材料补正状态，不能做此操作';
        } else if (vm.currentStateValue == '9' || vm.currentStateValue == '10') {
          text = '特殊程序状态，不能做此操作';
        }
      } else if (vm.checkIsApprover == '0') {
        if (vm.currentStateValue == '3' || vm.currentStateValue == '4') {
          text = '材料补全状态，不能做此操作';
        }
      }
      text && vm.$message.error(text);
      return text;
    },

//4.0 ************************************************* 意见框方法 *************---end

//==========================================================工程建设 相关审批按钮 start ========
    
    
    //办理按钮功能，弹窗发送选择框----窗口办理/事项办理--只推动流程流转---使用原来的
    wfBusSend: function () {
      var vm = this;
      if (vm.isRQDialog) {
        vm.getRQRuleList();
      }
      if (vm.checkCanClick()) return null;
      if (vm.checkNotNull(vm.taskId)) {
        vm.parentPageLoading = true;
        request('bpmFrontUrl', {
          url: ctx + 'rest/front/task/getTaskSendConfig/' + vm.taskId,
          type: 'get'
        }, function (result) {
          vm.parentPageLoading = false;
          if (result.success) {
            //获取下一环节信息
            vm.sendTaskInfos = result.content;
            vm.sendTaskInfo = vm.sendTaskInfos[0];//默认节点信息是 第一个节点的
            vm.nextTask = vm.sendTaskInfo.destActName;
            vm.nextTaskAssignee = vm.sendTaskInfo.defaultSendAssignees;
            vm.nextTaskAssigneeId = vm.sendTaskInfo.defaultSendAssigneesId;
            vm.isNeedSelectAssignee = vm.sendTaskInfo.needSelectAssignee;
            if (vm.sendTaskInfo && vm.sendTaskInfo.multiFlow == true) {
              vm.isMultiFlow = true;
              vm.mutiCheckedMan.push(vm.sendTaskInfo);
              vm.mutiCheckedNames.push(vm.sendTaskInfo.destActName);
            }
            //流程流转的提交参数
            vm.sendParam = {
              taskId: vm.taskId,
              sendConfigs: [{
                isUserTask: vm.sendTaskInfo.userTask,
                isEnableMultiTask: vm.sendTaskInfo.multiTask,
                assignees: vm.nextTaskAssigneeId,
                destActId: vm.sendTaskInfo.destActId
              }]
            };
            if (vm.sendTaskInfo.directSend) {
              vm.preApproveOperation = '填写办理意见';
              vm.getOpinionList();
              vm.onlySuggestDialog = true;
              // vm.doSendOperation(vm.sendTaskInfo.directSend);
            } else {
              vm.showSendDialog(vm.sendTaskInfos);
            }
          } else {
            vm.$message.error('获取下一节点信息失败!' + result.content);
          }
        }, function () {
          vm.parentPageLoading = false;
          vm.$message.error('保存并发送失败!');
        });
      } else {
        vm.$message.error("请先点击暂存按钮，保存表单信息！");
      }
    },
    changeMutiMan: function (row) {
      var vm = this;
      var name = row.destActName;
      var index = vm.mutiCheckedNames.indexOf(name);
      if (index != -1) {
        var iIndex = 0;
        var flag = false;
        vm.mutiCheckedMan.forEach(function (u, ii) {
          if (row.orderIndex > u.orderIndex) {
            iIndex = ii + 1;
          }
        });
        vm.mutiCheckedMan.splice(iIndex, 0, row);
      } else {
        var iIndex = -1;
        vm.mutiCheckedMan.forEach(function (u, ii) {
          if (u.destActName == name) {
            iIndex = ii;
          }
        });
        vm.mutiCheckedMan.splice(iIndex, 1);
      }
    },
    //下一环节选择事件，需要切换展示信息
    selectNextNode: function (destActId) {
      if (destActId) {
        var sendTaskInfos = vm.sendTaskInfos;
        for (var i = 0; i < sendTaskInfos.length; i++) {
          var currSendTask = sendTaskInfos[i];
          var currDestActId = currSendTask.destActId;
          if (currDestActId == destActId) {
            vm.sendTaskInfo = sendTaskInfos[i];
            vm.nextTaskAssignee = vm.sendTaskInfo.defaultSendAssignees;
            vm.nextTaskAssigneeId = vm.sendTaskInfo.defaultSendAssigneesId;
            vm.nextTask = vm.sendTaskInfo.destActName;
            vm.isNeedSelectAssignee = vm.sendTaskInfo.needSelectAssignee;
            vm.$set(vm.sendForm, 'nextTaskAssignee', vm.nextTaskAssignee);
            //流程流转的提交参数
            vm.sendParam = {
              taskId: vm.taskId,
              sendConfigs: [{
                isUserTask: vm.sendTaskInfo.userTask,
                isEnableMultiTask: vm.sendTaskInfo.multiTask,
                assignees: vm.nextTaskAssigneeId,
                destActId: vm.sendTaskInfo.destActId
              }]
            };
            //初始化下一环节审批人和提示信息
            vm.initNextAssigneeAndTip(vm.nextTaskAssignee, vm.sendTaskInfo.needSelectAssignee, vm.sendTaskInfo.message);
          }
        }
      }
    },
    
    //添加当前节点审批意见
    saveTaskComment: function (message, isShowTip) {
      var d = {};
      d["message"] = message;
      d["taskId"] = vm.taskId;
      d["processInstanceId"] = vm.processInstanceId;
      request('bpmFrontUrl', {
        url: ctx + 'rest/front/task/addTaskComment',
        type: 'post',
        data: d
      }, function (result) {
        if (result.success) {
          if (isShowTip) {
            vm.$message.success("保存审批意见成功！");
            typeof vm.getCommentListAll == 'function' && vm.getCommentListAll();//刷新右侧意见列表
          }
        } else {
          if (isShowTip) {
            vm.$message.error("保存审批意见失败！");
          }
        }
      }, function () {
        vm.$message.error(vm.requestErrorText);
      });
    },
    //流程发送
    doSendOperation: function (directSend) {
      var vm = this;
      if (vm.isRQDialog) {
        if (!vm.sendForm.toleranceTime) {
          return vm.$message.error('请填写容缺时限');
        }
        if (vm.sendForm.toleranceTime&&vm.sendForm.toleranceTime<=0) {
          return vm.$message.error('容缺时限请填写正数');
        }
        if(!vm.sendForm.timeruleId){
          return vm.$message.error('请选择容缺时限规则');
        }
      }
      if (!directSend) {
        // if (vm.sendTaskInfo.userTask && vm.checkNull(vm.nextTaskAssigneeId)) {
        //   vm.$message.error('您选择的下一环节需要选择办理人，没有办理人无法发送！');
        //   return;
        // }
        if (vm.checkNull(vm.opinionForm.opinionText)) {
          vm.$message.error('请填写办理意见！');
          return;
        } else {
          vm.saveTaskComment(vm.opinionForm.opinionText, false);
        }
      }
      if (vm.checkNotNull(vm.masterEntityKey) && vm.checkNotNull(vm.taskId)) {
        try {
          if (busFormIframe.window.wfBusSave) {
            busFormIframe.window.wfBusSave(vm.flowdefKey, vm.taskId, vm.masterEntityKey, vm.appFlowdefId, vm.processInstanceId);
          }
        } catch (e) {
        }
        
        if (vm.sendTaskInfos.length > 1) {
          var _nextTask = vm.dealNull(vm.nextTask);
          var _nextTaskAssignee = vm.nextTaskAssignee;
          if (vm.isMultiFlow) {
            _nextTask = vm.mutiCheckedNames.join('、');
            var _tmpArr = [];
            vm.mutiCheckedMan.forEach(function (u) {
              _tmpArr.push(u.defaultSendAssignees || '暂无审批人');
            });
            _nextTaskAssignee = _tmpArr.join('、');
          }
          var sendTip = "确认发送至 <span style='color:#22D479;font-size:18px' >&nbsp;" + _nextTask + "</span>&nbsp;环节？";
          if (vm.checkNotNull(vm.nextTaskAssignee)) {
            sendTip += "下一环节审批人为：<span style='color:#22D479;font-size:18px' >&nbsp;" + _nextTaskAssignee + "&nbsp;</span>";
          }
          confirmMsg("确认发送信息", sendTip, function () {
            vm.sendOperation(directSend);
          }, null, null, null, null, null, true);
        } else {
          vm.sendOperation(directSend);
        }
      } else {
        try {
          if (busFormIframe.window.wfBusSave) {
            busFormIframe.window.wfBusSave(vm.flowdefKey, vm.taskId, vm.masterEntityKey, vm.appFlowdefId, vm.processInstanceId);
            vm.$message.success("保存成功！");
          } else {
            vm.$message.error("流程表单没有实现表单保存接口，无法保存表单信息！");
          }
        } catch (e) {
        }
        
      }
    },
    //弹窗发送对话框，并初始化信息
    showSendDialog: function (sendTaskInfos) {
      //显示窗口
      vm.sendDialog = true;
      //1、初始化下一环节信息，这里后面可能有多个环节，要根据选择的环节获取该环节的处理人，再供用户选择。
      // 如果多个环节时要绑定单选按钮事件，更新选择的环节到nextTask变量中
      $("#nextTaskList").empty();
      var nextTaskInfo = '';
      var tmpList = [];
      vm.nextTaskCheckedId = null;
      vm.nextTaskListData = [];
      for (var i = 0; i < sendTaskInfos.length; i++) {
        var temp = sendTaskInfos[i];
        temp.orderIndex = i;
        tmpList.push(temp);
        var checked = '';
        if (i == 0) {
          checked = '" checked="checked';
        }
        nextTaskInfo += '<label class="m-radio m-radio--solid m-radio--brand">' +
            '<input type="radio" onclick="vm.selectNextNode(\'' + temp.destActId + '\')" name="nextTask" value="' + temp.destActId + checked + '">' +
            '<span></span>' +
            '<div style="display: inline;font-size: 16px;height: 21px;line-height: 20px;">' + temp.destActName + '</div>' +
            '</label>';
      }
      vm.delaySetHtmlToElement("nextTaskList", nextTaskInfo);
      vm.nextTaskListData = tmpList;
      vm.nextTaskCheckedId = vm.nextTaskListData[0].destActId;
      //2、初始化下一环节审批人和提示信息
      vm.initNextAssigneeAndTip(vm.nextTaskAssignee, vm.sendTaskInfo.needSelectAssignee, vm.sendTaskInfo.message);
      //3、个人常用办理意见信息
      if (vm.opinionTableData.length == 0) {
        vm.getOpinionList();
      }
      
    },
    //初始化下一环节审批人和提示信息
    initNextAssigneeAndTip: function (nextTaskAssignee, needSelectAssignee, tipMessage) {
      //2、下一环节参与人信息
      if (vm.checkNotNull(nextTaskAssignee)) {
        vm.$set(vm.sendForm, 'nextTaskAssignee', nextTaskAssignee);
      } else {
        vm.$set(vm.sendForm, 'nextTaskAssignee', "暂无审批人");
      }
      //选择按钮启用/禁用
      if (!needSelectAssignee) {
        $("#selectAsingee").attr("disabled", true);
      } else {
        $("#selectAsingee").attr("disabled", false);
      }
      //3、备注说明信息
      var commentTip = "提示：";
      if (vm.checkNotNull(tipMessage)) {
        commentTip += tipMessage;
      }
      if (needSelectAssignee) {
        commentTip += "然后请点击“发送”按钮发送到所选环节";
      } else {
        commentTip += "请点击“发送”按钮发送到所选环节";
      }
      vm.delaySetHtmlToElement("sendTip", commentTip);
    },
    //延迟设置元素的值，由于element-ui解析的原因，第一次可能获取不到元素对象
    delaySetHtmlToElement: function (id, text) {
      var it = setInterval(function () {
        if ($("#" + id).length > 0) {
          $("#" + id).html(text);
          window.clearInterval(it);
        }
      }, 100);
    },
    //发送操作
    sendOperation: function (directSend) {
      var vm = this;
      var sendObj = $.extend({}, vm.sendParam);
      if (vm.isRQDialog) {
        sendObj.toleranceTime = vm.sendForm.toleranceTime;
        sendObj.timeruleId = vm.sendForm.timeruleId;
      }
      if (vm.isMultiFlow) {
        sendObj.sendConfigs = [];
        vm.mutiCheckedMan.forEach(function (u) {
          sendObj.sendConfigs.push({
            assignees: u.defaultSendAssigneesId,
            destActId: u.destActId,
            isEnableMultiTask: u.multiTask,
            isUserTask: u.userTask,
          });
        })
      }
      var tmpSendParam = $.extend({}, sendObj, {
        conclusionGroupCode: 'groupCode001',
        conclusionOptionValue: vm.conclusionOptionValue,
      });
      var params = {
        sendObjectStr: JSON.stringify(tmpSendParam),
        //iteminstState: null,
        //iteminstId: vm.busRecordId,
        isSendSMS: vm.sendForm.isSendnuSMS,
        isSendToNextHandle: vm.sendForm.isSendToNextHandle,
        phoneNo: vm.sendForm.phoneNo,
        contentDesc: vm.sendForm.contentDesc,
        applyinstId: vm.masterEntityKey,
        //新增参数---更改事项状态并推动流程
        iteminstId: vm.iteminstId || vm.getUrlParam('busRecordId'),
        itemState: vm.enumItemStatus,
        //更改申请状态并推动流程
        applyinstId: vm.masterEntityKey,
        applyState: vm.enumApplyStatus,
        conclusionOptionValue: vm.conclusionOptionValue,
      }
      // console.log(tmpSendParam);
      // console.log(params);
      // if (window) return null;
      vm.sendDialogLoading = true;
      vm.onlySuggestLoading = true;
      request('bpmFrontUrl', {
        //url: ctx + 'rest/front/task/wfSend',
        url: vm.sendUrlPath,
        type: vm.requestMappingType,
        data: params,
      }, function (result) {
        vm.sendDialogLoading = false;
        vm.onlySuggestLoading = false;
        vm.sendDialog = false;
        vm.onlySuggestDialog = false;
        if (result.success) {
          var message = "";
          if (directSend) {
            //直接发送的，不提示下一环节具体处理人。
            message = "<span style='color:#22D479;font-size:18px' >流程发送成功！</span>";
            message += "<span style='font-size:18px'>任务处理完毕，正等待其他用户处理！</span>";
            
          } else {
            //这里要在提交后的回调中调用到下一环节的提示信息，这两个变量会在操作过程中被改变，包括环节名称和处理人名称。
            var _nextTask = vm.nextTask;
            var _nextTaskAssignee = vm.nextTaskAssignee;
            if (vm.isMultiFlow) {
              _nextTask = vm.mutiCheckedNames.join('、');
              var _tmpArr = [];
              vm.mutiCheckedMan.forEach(function (u) {
                _tmpArr.push(u.defaultSendAssignees || '暂无审批人');
              });
              _nextTaskAssignee = _tmpArr.join('、');
            }
            message = "流程已发送至 <span style='color:#22D479;font-size:18px' >&nbsp;" + _nextTask + "</span>&nbsp;环节";
            if (vm.checkNotNull(vm.nextTaskAssignee)) {
              message += "，下一环节审批人为：<span style='color:#22D479;font-size:18px' >&nbsp;" + _nextTaskAssignee + "&nbsp;。</span>";
            }
          }
          
          //vm.showSuccessTip(message);
          
          //获取下一个任务实例id---
          var nextTaskId = result.content;
          if (nextTaskId) {
            //给出提示
            vm.$message({dangerouslyUseHTMLString: true, message: message, type: 'success'});
            setTimeout(function () {
              //直接刷新界面好了
              var newUrl = window.location.href.substring(0, window.location.href.indexOf("?") + 1) + "taskId=" + nextTaskId + "&viewId=" + vm.viewId + "&busRecordId=" + vm.getUrlParam('busRecordId');
              if (vm.isZJItem) {
                newUrl += '&itemNature=8'
              }
              window.location.href = newUrl;
            }, 1500);
          } else {
            vm.showSuccessTip(message);
          }
        } else {
          vm.sendDialogLoading = false;
          vm.onlySuggestLoading = false;
          vm.$message.error(result.message);
        }
      }, function () {
        vm.sendDialogLoading = false;
        vm.onlySuggestLoading = false;
        vm.sendDialog = false;
        vm.onlySuggestDialog = false;
        vm.$message.error("流程发送失败！");
      });
    },
    //双击删除已选参与人
    removeAssignee: function (event) {
      var currentTarget = event.currentTarget;
      var id = $(currentTarget).attr("data-id").trim();
      var index = vm.arrayIndexOf(vm.selectedAssignee, id);
      if (index != -1) {
        //从数组的index脚标开始，删除一个元素
        vm.selectedAssignee.splice(index, 1);
        //删除html节点
        $(currentTarget).remove();
        //同步去掉zTree上的勾
        vm.toggleCheckAssignee(id, false);
        //多余一步，为了兼容zTree的异步加载完成操作同步默认办理人
        if (id == vm.tempDefaultSelectedAssigneeId)
          vm.tempDefaultSelectedAssigneeId = null;
      }
    },
    //添加参与人，会根据当前可选办理人个数，进行控制。
    addAssignee: function (id, text) {
      //判断是否已经选过了
      var index = vm.arrayIndexOf(vm.selectedAssignee, id);
      if (index == -1) {
        //如果不是多工作项的话，就只能选择一个审批人
        if (!vm.sendTaskInfo.multiTask) {
          $("#selectedAssignees").empty();
          vm.selectedAssignee = [];
        }
        var str = '<a id="selected_assignee_' + id + '" data-id="' + id + '" href="#" class="m-list-search__result-item" style="display: block;border-bottom: 1px solid #ebedf2;" title="双击可移除">' +
            '<span class="m-list-search__result-item-icon">' +
            '<i class="fa fa-user-md" style="font-size: 18px;padding: 0px 5px;"></i>' +
            '</span>' +
            '<span class="m-list-search__result-item-text">' + text + '</span>' +
            '</a>';
        $("#selectedAssignees").append(str);
        $("#selectedAssignees").find("a").dblclick(vm.removeAssignee);
        vm.selectedAssignee.push({id: id, text: text});
      }
    },
    //zTree树上勾掉时，移除办理人
    uncheckRemoveAssignee: function (id) {
      var index = vm.arrayIndexOf(vm.selectedAssignee, id);
      if (index != -1) {
        //从数组的index脚标开始，删除一个元素
        vm.selectedAssignee.splice(index, 1);
        $("#selected_assignee_" + id).remove();
        //多余一步，为了兼容zTree的异步加载完成操作同步默认办理人
        if (id == vm.tempDefaultSelectedAssigneeId)
          vm.tempDefaultSelectedAssigneeId = null;
      }
    },
    //zTree的勾选办理人方法
    toggleCheckAssignee: function (id, flag) {
      if (vm.zTreeObj) {
        var nodes = vm.zTreeObj.getNodesByParam("textValue", id, null);
        if (nodes && nodes.length > 0) {
          for (var i = 0; i < nodes.length; i++) {
            vm.zTreeObj.checkNode(nodes[i], flag, false);
          }
        }
      }
    },
    //判断数组中是否存在value数据，-1 没有，否则有
    arrayIndexOf: function (arr, value) {
      var index = -1;
      for (var i = 0; i < arr.length; i++) {
        if (value == arr[i].id) {
          index = i;
          break;
        }
      }
      return index;
    },
// ******** 只更改状态(申请实例或事项实例）的（窗口和事项通用） 材料补正（事项）/退出特别程序（事项）/开始特别程序（事项）***************
    updateItemState: function () {
      //var vm = this;
      vm.loading = true;
      //var url = ctx + "/approve/btn/item/changeItemState";
      var params = {
        userOpinion: vm.opinionForm.opinionText,
        actionCode: vm.enumActionCode,
        //更改事项状态使用字段
        iteminstId: vm.iteminstId,
        itemState: vm.enumItemStatus,
        //任务ID（当需要挂起流程时，需要传参）
        taskId: vm.taskId,
        //更改窗口状态使用字段
        applyinstId: vm.masterEntityKey,
        applyState: vm.enumApplyStatus
      }
      request('', {
        url: vm.sendUrlPath,
        type: vm.requestMappingType,
        data: params
      }, function (result) {
        if (result.success) {
          vm.loading = false;
          vm.submitCommentsShow = false;
          vm.showSuccessTip('操作成功');
          //关闭当前页
          closeCurrentTab();
        } else {
          console.log(result.message);
        }
      }, function () {
        vm.loading = false;
        console.log("操作失败！");
      });
    },
    
    //网上预审的接口
    updateApplyState: function () {
      if (vm.preApproveOperation == '填写办理意见') {
        if (vm.checkNull(vm.opinionForm.opinionText)) {
          vm.$message.error('请填写办理意见！');
          return;
        } else {
          vm.saveTaskComment(vm.opinionForm.opinionText, false);
        }
        vm.doSendOperation(vm.sendTaskInfo.directSend);
      }
      if (vm.preApproveOperation == '不予受理' || vm.preApproveOperation == '预审通过') {
        if (vm.checkNull(vm.opinionForm.opinionText)) {
          vm.$message.error('请填写办理意见！');
          return;
        } else {
          vm.saveTaskComment(vm.opinionForm.opinionText, false);
        }
        vm.sendOperation();
      } else {
        //材料补全的逻辑要写在这里
        this.updateItemState();
      }
    },
    //----------------------------------------------
    //显示成功提示信息，会关闭窗口
    showSuccessTip: function (message) {
      this.$message({
        showClose: true,
        dangerouslyUseHTMLString: true,
        message: message,
        type: 'success'
      });
      setTimeout(function () {
        //刷新父页面
        if (window.opener) {
          window.opener.location.reload();
        }
        window.close();
      }, 2500);
      
      /* this.$alert(message, '提示信息', {
               confirmButtonText: '确定',
               dangerouslyUseHTMLString: true,
               callback: function (action) {
                       window.close();
               }
       });*/
    },
//----------- 制证 ---------------------
    getCertinstList: function () {
      //查询当前事项下的证照实例
      var _that = this;
      request('', {
        url: ctx + 'rest/certificate/certinst/list',
        type: 'get',
        data: {iteminstId: _that.iteminstId}
      }, function (result) {
        if (result.success) {
          _that.certinstList = result.content;
        } else {
          console.log(result.message);
        }
      }, function () {
        _that.loading = false;
        // console.log("获取制证信息失败！");
      });
    },
    makeCertification: function () {
      var _that = this;
      _that.getCertinstList();
      _that.certinstModalShow = true;
    },
    //新增证照实例弹框
    addItemCert: function () {
      //查询当前事项实例下定义的证照列表；需要过滤掉已经存在的实例；如果集合为空，则提示不能创建
      //及业主单位ID，单位名称，项目ID,项目名称
      var _that = this;
      request('', {
        url: ctx + 'rest/certificate/cert/category/list',
        type: 'get',
        data: {
          iteminstId: vm.iteminstId,
          applyinstId: vm.masterEntityKey,
          isFilter: true
        }
      }, function (result) {
        if (result.success) {
          var content = result.content;
          _that.certList = content.certList;
          Vue.set(_that.editCertForm, 'applicant', content.applicant);
          Vue.set(_that.editCertForm, 'projInfoId', content.projInfoId);
          Vue.set(_that.editCertForm, 'unitInfoId', content.unitInfoId);
          Vue.set(_that.editCertForm, 'issueOrgId', content.issueOrgId);
          Vue.set(_that.editCertForm, 'issueOrgName', content.issueOrgName);
          Vue.set(_that.editCertForm, 'iteminstId', _that.iteminstId);
          if (_that.certList.length == 0) {
            _that.$message({
              message: '当前事项下没有证照定义或已经存在该证照！无法新增',
              type: 'error'
            });
            return false;
          } else {
            _that.editCertinstModel = true;
          }
        } else {
          console.log(result.message);
        }
      }, function () {
        _that.loading = false;
        console.log("获取制证信息失败！");
      });
      
    },
    editCertinst: function (row) {
      var _that = this;
      //_that.editCertForm = row;
      Vue.set(_that.editCertForm, 'applicant', row.applicant);
      Vue.set(_that.editCertForm, 'projInfoId', row.projInfoId);
      Vue.set(_that.editCertForm, 'unitInfoId', row.unitInfoId);
      Vue.set(_that.editCertForm, 'issueOrgId', row.issueOrgId);
      Vue.set(_that.editCertForm, 'issueOrgName', row.issueOrgName);
      Vue.set(_that.editCertForm, 'iteminstId', _that.iteminstId);
      Vue.set(_that.editCertForm, 'termStart', _that.formatDate(row.termStart));
      Vue.set(_that.editCertForm, 'termEnd', _that.formatDate(row.termEnd));
      Vue.set(_that.editCertForm, 'issueDate', _that.formatDate(row.issueDate));
      Vue.set(_that.editCertForm, 'certinstId', row.certinstId);
      Vue.set(_that.editCertForm, 'certId', row.certId);
      Vue.set(_that.editCertForm, 'certinstName', row.certinstName);
      Vue.set(_that.editCertForm, 'certinstCode', row.certinstCode);
      Vue.set(_that.editCertForm, 'rootOrgId', row.rootOrgId);
      Vue.set(_that.editCertForm, 'certOwner', row.certOwner);
      _that.getFileListWin(row.certinstId);
      //_that.fileList = row.bscAttDetails;
      //反显证照ID对应的名称
      request('', {
        url: ctx + 'rest/certificate/cert/category/list',
        type: 'get',
        data: {
          iteminstId: vm.iteminstId,
          applyinstId: vm.masterEntityKey,
          isFilter: false
        }
      }, function (result) {
        if (result.success) {
          var content = result.content;
          _that.certList = content.certList;
          _that.editCertinstModel = true;
        } else {
          console.log(result.message);
        }
      }, function () {
        _that.loading = false;
        console.log("获取制证信息失败！");
      });
      
      
    },
    //保存或修改证照实例
    saveOrUpdateCertinst: function () {
      var _that = this;
      console.log(_that.editCertForm);
      this.$refs['editCertForm'].validate(function (valid) {
        if (valid) {
          request('', {
            url: ctx + 'rest/certificate/updateCertInfo',
            type: 'post',
            data: _that.editCertForm
          }, function (result) {
            if (result.success) {
              _that.getCertinstList();
              _that.editCertinstModel = false;
            } else {
              console.log(result.message);
            }
          }, function () {
            _that.loading = false;
            console.log("保存或修改证照实例失败！");
          });
          
        } else {
          _that.$message({
            message: '数据校验失败',
            type: 'error'
          });
          
        }
      });
      
      
    },
    deleteCertinst: function (id) {
      confirmMsg('提示', '确定要删除选中的批文吗？', function () {
        request('', {
          url: ctx + 'rest/certificate/certinst/one/delete',
          type: 'post',
          data: {
            'certinstId': id
          },
        }, function (result) {
          if (result.success) {
            _that.$message.success('删除成功');
            _that.getCertinstList();
          } else {
            console.log(result.message);
          }
        }, function () {
          vm.$message.error("删除失败！");
        });
      })
    },
    batchDeleteItemCert: function () {//
      var _that = this;
      if (_that.selectCertinstList.length == 0) {
        vm.$message.error("请选择待删除项！");
      } else {
        var certinstIds = [];
        _that.selectCertinstList.map(function (item) {
          certinstIds.push(item.certinstId)
        })
        var certinstId = certinstIds.join(',');
        confirmMsg('提示', '确定要删除选中的批文吗？', function () {
          request('', {
            url: ctx + 'rest/certificate/certinst/batch/delete',
            type: 'post',
            data: {
              'certinstIds': certinstId
            },
          }, function (result) {
            if (result.success) {
              _that.$message.success('删除成功');
              _that.getCertinstList();
            } else {
              console.log(result.message);
            }
          }, function () {
            vm.$message.error("删除失败！");
          });
        })
      }
    },
    //选中的证照实例列表
    selecCertinstListChange: function (row) {
      //选择行数
      var _that = this;
      _that.selectCertinstList = row;
    },
    // 勾选de电子件
    selectFileChange: function (val) {
      this.fileSelectionList = val;
    },
    
    // 下载电子件
    downloadFile: function () {
      var _that = this;
      var detailIds = [];
      if (_that.fileSelectionList.length == 0) {
        _that.$message({
          message: '请勾选数据后操作！',
          type: 'error'
        });
        return false;
      }
      _that.fileSelectionList.map(function (item, index) {
        detailIds.push(item.matinstId);
      });
      detailIds = detailIds.join(',')
      window.location.href = ctx + 'rest/mats/att/download?detailIds=' + detailIds
    },
    
    // 上传电子件
    uploadFileCom: function (file) {
      var _that = this;
      this.fileWinData = new FormData();
      if (!_that.editCertForm.certId || !_that.editCertForm.unitInfoId) {
        _that.$message.error("缺少参数");
        return false;
      }
      Vue.set(file.file, 'fileName', file.file.name);
      this.fileWinData.append('file', file.file);
      this.fileWinData.append('iteminstId', _that.iteminstId);
      this.fileWinData.append("certId", _that.editCertForm.certId);
      this.fileWinData.append("certinstCode", _that.editCertForm.certinstCode);
      this.fileWinData.append("certinstName", _that.editCertForm.certinstName ? _that.editCertForm.certinstName : '');
      this.fileWinData.append("projInfoId", _that.editCertForm.projInfoId);
      this.fileWinData.append("unitInfoId", _that.editCertForm.unitInfoId);
      this.fileWinData.append("certinstId", _that.editCertForm.certinstId ? _that.editCertForm.certinstId : '');
      _that.loadingFileWin = true;
      axios.post(ctx + 'rest/certificate/certinst/att/upload', _that.fileWinData).then(function (res) {
        Vue.set(file.file, 'certinstId', res.data.content);
        _that.editCertForm.certinstId = res.data.content;
        _that.getFileListWin(res.data.content);
        _that.$message({
          message: '上传成功',
          type: 'success'
        });
        
      }).catch(function (error) {
        _that.loadingFileWin = false;
        if (error.response) {
          _that.$message({
            message: '文件上传失败(' + error.response.status + ')，' + error.response.data,
            type: 'error'
          });
        } else if (error.request) {
          _that.$message({
            message: '文件上传失败，服务器端无响应',
            type: 'error'
          });
        } else {
          _that.$message({
            message: '文件上传失败，请求封装失败',
            type: 'error'
          });
        }
        
      });
    },
    // 获取已上传文件列表
    getFileListWin: function (certinstId) {
      var _that = this;
      request('', {
        url: ctx + 'rest/certificate/certinst/att/list',
        type: 'get',
        data: {certinstId: certinstId}
      }, function (res) {
        if (res.content) {
          _that.uploadCertData = res.content;
        } else {
          _that.$message({
            message: res.message ? res.message : '加载已上传材料列表失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    //批量删除附件
    batchDeleteAttach: function () {
      var _that = this;
      if (_that.fileSelectionList.length == 0) {
        this.$message.error("请选择文件");
        return false;
      } else {
        var ids = []
        _that.fileSelectionList.map(function (item) {
          ids.push(item.fileId);
        });
        
        _that.deleteFileAttach(ids.join(","));
        
      }
      
    },
    //删除单个附件
    deleteOneAttach: function (id) {
      var _that = this;
      _that.deleteFileAttach(id);
    },
    deleteFileAttach: function (ids) {
      var _that = this;
      request('', {
        url: ctx + 'rest/certificate/certinst/att/delelte',
        type: 'post',
        data: {detailIds: ids}
      }, function (res) {
        if (res.success) {
          _that.getFileListWin(_that.editCertForm.certinstId);
        } else {
          console.log(res.message)
        }
      }, function (msg) {
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    //单个删除附件
    /*//勾选证照附件
    selectCertAttChange: function (val) {
            this.certSelectionList = val;
    },*/
    testBtn: function () {
      var vm = this;
      var _nextTask = vm.nextTask;
      var _nextTaskAssignee = vm.nextTaskAssignee;
      if (vm.isMultiFlow) {
        _nextTask = vm.mutiCheckedNames.join('、');
        var _tmpArr = [];
        vm.mutiCheckedMan.forEach(function (u) {
          _tmpArr.push(u.defaultSendAssignees || '暂无审批人');
        });
        _nextTaskAssignee = _tmpArr.join('、');
      }
      var message = "<span style='color:#22D479;font-size:18px' >流程发送成功！</span>";
      message += "<span style='font-size:18px'>任务处理完毕，正等待其他用户处理！</span>";
      var message1 = "流程已发送至 <span style='color:#22D479;font-size:18px' >&nbsp;" + _nextTask + "</span>&nbsp;环节";
      message1 += "，下一环节审批人为：<span style='color:#22D479;font-size:18px' >&nbsp;" + _nextTaskAssignee + "&nbsp;。</span>";
      this.$message({
        showClose: true,
        dangerouslyUseHTMLString: true,
        message: message1,
        type: 'success',
        center: true
      });
    },
    // 材料补正 新 start
    openBzDialog: function () {
      var vm = this;
      vm.parentPageLoading = true;
      request('', {
        url: ctx + 'rest/correct/getLackMats',
        type: 'get',
        data: {
          iteminstId: vm.iteminstId,
          applyinstId: vm.masterEntityKey
        }
      }, function (res) {
        vm.parentPageLoading = false;
        if (res.success) {
          vm.bzFormData = $.extend({}, vm.bzFormData, res.content);
          vm.bzSubmitData = vm.packageBzData(res.content.submittedMats.concat([]));
          vm.bzCorrectData = vm.packageBzData(res.content.matCorrectDtos.concat([]));
          vm.bzDiaVisible = true;
        } else {
          vm.$message.error(res.message || '获取补正清单失败')
        }
      }, function () {
        vm.parentPageLoading = false;
        vm.$message.error('获取补正清单失败');
      });
    },
    // 组装补正数据
    packageBzData: function (arr, flag) {
      return __STATIC.packageBzData(arr, flag);
    },
    // 点击已提交列表的CheckBox
    bzTableSelect: function (selection, row) {
      var vm = this;
      var flag = false;
      selection.forEach(function (u) {
        if (u.tmpMatId == row.tmpMatId) {
          flag = true;
        }
      });
      var _index = 0;
      var hasTarget = false; // 待补正列表是否存在这条数据
      vm.bzCorrectData.forEach(function (u) {
        if (u.tmpMatId == row.tmpMatId) {
          hasTarget = true;
        }
      });
      if (flag) {
        console.log('选中');
        if (hasTarget) return;
        var isModifed = false;
        vm.bzCorrectData.forEach(function (u, index) {
          if (u.matId == row.matId) {
            if (!isModifed) {
              isModifed = true;
              u.rowspan += 1;
            }
            if (
                (index == vm.bzCorrectData.length - 1) ||
                (vm.bzCorrectData[index + 1].matId != row.matId)
            ) {
              _index = index;
            }
          }
        });
        if (isModifed) {
          vm.bzCorrectData.splice(_index + 1, 0, $.extend({}, row, {
            rowspan: 0,
            colspan: 0,
          }));
        } else {
          vm.bzCorrectData.splice(0, 0, $.extend({}, row, {
            rowspan: 1,
            colspan: 1,
          }));
        }
      } else {
        console.log('删除');
        if (!hasTarget) return;
        vm.bzDeleteRow(row);
      }
    },
    bzDeleteRow: function (row) {
      var vm = this;
      var _index = 0;
      vm.bzCorrectData.forEach(function (u, index) {
        if (u.tmpMatId == row.tmpMatId) {
          _index = index;
        }
      });
      // console.log('删除的rowspan:'+vm.bzCorrectData[_index].rowspan);
      if (vm.bzCorrectData[_index].rowspan == 0) {
        // 删除rowspan为0的
        vm.bzCorrectData.splice(_index, 1);
        var isModifed = false;
        vm.bzCorrectData.forEach(function (u) {
          if (u.matId == row.matId) {
            if (!isModifed) {
              isModifed = true;
              u.rowspan -= 1;
            }
          }
        })
      } else if (vm.bzCorrectData[_index].rowspan == 1) {
        // 删除rowspan为1
        vm.bzCorrectData.splice(_index, 1);
      } else {
        // 删除rowspan大于1的
        var rowSpan = vm.bzCorrectData[_index].rowspan - 1;
        vm.bzCorrectData[_index + 1].rowspan = rowSpan;
        vm.bzCorrectData[_index + 1].colspan = 1;
        // console.log('设置的rowSpan:' + rowSpan);
        vm.bzCorrectData.splice(_index, 1);
      }
    },
    // 点击待补正列表的删除按钮
    bzDeleteSelect: function (row) {
      var vm = this;
      vm.bzDeleteRow(row);
      // 清空左边勾选框
      vm.bzSubmitData.forEach(function (u) {
        if (u.tmpMatId == row.tmpMatId) {
          vm.$refs.bzLeftTableRef.toggleRowSelection(u, false);
        }
      })
    },
    // 切换左边表格是否显示
    toggleBzTableExpand: function () {
      this.showBzLeftTable = !this.showBzLeftTable;
    },
    // 表格列、行宽计算
    bzTableSpanMethod: function (obj) {
      if (obj.columnIndex == 0) {
        return {
          rowspan: obj.row.rowspan,
          colspan: obj.row.colspan,
        }
      }
    },
    // 点击开始补正
    startBz: function () {
      var vm = this;
      vm.$refs.bzFormRef.validate(function (valid) {
        if (valid) {
          var result = handler(vm.bzCorrectData.concat([]));
          if (result.length == 0) return vm.$message.error('待补正列表不能为空');
          var requestData = {
            applyinstId: vm.masterEntityKey,
            iteminstId: vm.iteminstId,
            isFlowTrigger: '1',
            taskinstId: vm.taskId,
            projInfoId: vm.bzFormData.projInfoId,
            correctMemo: vm.bzFormData.correctMemo,
            correctDueDays: vm.bzFormData.correctDueDays,
            matCorrectDtosJson: JSON.stringify(result),
          };
          vm.bzLoading = true;
          request('', {
            url: ctx + 'rest/correct/createMatCorrectinst',
            type: 'post',
            data: requestData,
          }, function (res) {
            vm.bzLoading = false;
            if (res.success) {
              vm.$message.success('发起材料补正成功');
              delayCloseWindow();
            } else {
              vm.$message.error(res.message || '材料补正发起失败');
            }
          }, function () {
            vm.bzLoading = false;
            vm.$message.error('材料补正发起失败');
          });
        }
      });
      
      function handler(arr) {
        var matIds = [];
        var result = [];
        arr.forEach(function (u) {
          if (matIds.indexOf(u.matId) == -1) {
            matIds.push(u.matId);
          }
          if (u.rowType == 1) {
            u.copyCount = 0;
            u.isNeedAtt = '0';
          } else if (u.rowType == 2) {
            u.paperCount = 0;
            u.isNeedAtt = '0';
          } else if (u.rowType == 3) {
            u.paperCount = 0;
            u.copyCount = 0;
          }
        });
        matIds.forEach(function (u) {
          var tmp = {};
          var flag = false;
          arr.forEach(function (uu) {
            if (u == uu.matId) {
              if (!flag) {
                flag = true;
                tmp = $.extend({}, tmp, uu);
              } else {
                if (uu.rowType == 1) {
                  tmp.paperCount = uu.paperCount;
                  tmp.paperDueIninstOpinion = uu.paperDueIninstOpinion;
                } else if (uu.rowType == 2) {
                  tmp.copyCount = uu.copyCount;
                  tmp.copyDueIninstOpinion = uu.copyDueIninstOpinion;
                } else if (uu.rowType == 3) {
                  tmp.isNeedAtt = '1';
                  tmp.attDueIninstOpinion = uu.attDueIninstOpinion;
                }
              }
            }
          });
          result.push(tmp);
        });
        return result;
      }
    },
    // 关闭补正弹窗
    closeBzDialog: function () {
      this.$refs.bzFormRef.resetFields();
      this.showBzLeftTable = true;
      this.bzSubmitData = [];
      this.bzCorrectData = [];
    },
    // 材料补正 新 end
    
    // 材料补正按钮相关
    // 材料补正dialog,获取数据
    fetchLackMatsByApplyinstIdAndIteminstId: function () {
      var ts = this;
      ts.parentPageLoading = true;
      request('', {
        url: ctx + 'rest/correct/getLackMats',
        type: 'get',
        data: {
          iteminstId: ts.iteminstId,
          applyinstId: ts.masterEntityKey
        }
      }, function (res) {
        ts.parentPageLoading = false;
        if (res.success) {
          ts.supplementDialogData = res.content;
          ts.handelSupplementData();
          ts.handelMatCorrectDtos();
          ts.handelSubmittedMats();
          ts.isShowMaterialSupplementDialog = true;
        } else {
          if (res.content != null) {
            ts.supplementDialogData = res.content;
            ts.handelSupplementData();
            ts.handelMatCorrectDtos();
            ts.handelSubmittedMats();
            ts.isShowMaterialSupplementDialog = true;
          } else {
            ts.$message.error(res.message);
            ts.isShowMaterialSupplementDialog = false;
          }
        }
      }, function () {
        ts.parentPageLoading = false;
        ts.isShowMaterialSupplementDialog = false;
        ts.$message.error('网络错误！');
      });
    },
    // 处理材料补正回显数据
    handelSupplementData: function () {
      for (var k in this.supplemetForm) {
        if (this.supplementDialogData[k]) {
          this.supplemetForm[k] = this.supplementDialogData[k];
        } else {
          this.supplemetForm[k] = '';
        }
      }
    },
    // 处理接口中获取的未选择前的待选材料列表
    handelSubmittedMats: function () {
      this.tobeSelectMaterialList = [];
      this.tobeSelectMatIds = [];
      var submittedMats = this.supplementDialogData.submittedMats || [];
      for (var i = 0; i < submittedMats.length; i++) {
        // submittedMats[i].isNeedOrign = false;
        // submittedMats[i].isNeedCopy = false;
        // submittedMats[i].isNeedElectron = false;
        this.$set(submittedMats[i], 'isNeedOrign', false);
        this.$set(submittedMats[i], 'isNeedCopy', false);
        this.$set(submittedMats[i], 'isNeedElectron', false);
        this.tobeSelectMaterialList.push(submittedMats[i]);
        this.tobeSelectMatIds.push(submittedMats[i].matId);
      }
    },
    // 处理接口中获取到的待补正的材料
    handelMatCorrectDtos: function () {
      this.tobaMakeupMaterialList = [];
      var matCorrectDtos = this.supplementDialogData.matCorrectDtos || [];
      for (var i = 0; i < matCorrectDtos.length; i++) {
        // 初始值都置为false
        matCorrectDtos[i].isNeedOrign = false;
        matCorrectDtos[i].isNeedCopy = false;
        matCorrectDtos[i].isNeedElectron = false;
        if (matCorrectDtos[i].paperCount && matCorrectDtos[i].paperCount !== null && parseInt(matCorrectDtos[i].paperCount) > 0) {
          matCorrectDtos[i].isNeedOrign = true; //是否需要展示原件
        }
        if (matCorrectDtos[i].copyCount && matCorrectDtos[i].copyCount !== null && parseInt(matCorrectDtos[i].copyCount) > 0) {
          matCorrectDtos[i].isNeedCopy = true; //是否需要展示复印件
        }
        if (matCorrectDtos[i].isNeedAtt !== null && matCorrectDtos[i].isNeedAtt == "1") {
          matCorrectDtos[i].isNeedElectron = true; //是否需要展示电子件
          this.$set(matCorrectDtos[i], 'isNeedAtt', true); //当需要扎实电子件时，默认将isNeedAtt置为true
        }
        this.tobaMakeupMaterialList.push(matCorrectDtos[i]);
      }
    },
    // 删除待补正材料列表中的某个材料的某一项（原件|复印件|电子件）
    delTobaMakeupMaterial: function (attr, item, index) {
      // item[attr] = false;
      // debugger
      // this.tobaMakeupMaterialList[index][attr] = false;
      var ts = this;
      this.tobaMakeupMaterialList = this.tobaMakeupMaterialList.concat([]);
      var _idx = this.tobeSelectMatIds.indexOf(item.matId);
      if (_idx != -1) {
        this.$set(this.tobeSelectMaterialList[_idx], attr, false);
      }
      this.$set(ts.tobaMakeupMaterialList[index], attr, false)
      var _canRemove = false,
          removeMatIndex = -1;
      if (!item.isNeedOrign && !item.isNeedCopy && !item.isNeedElectron) {
        _canRemove = true;
      }
      if (_canRemove) {
        removeMatIndex = index;
        if (removeMatIndex !== -1) {
          this.tobaMakeupMaterialList.splice(removeMatIndex, 1)
        }
      }
      // console.log(this.tobaMakeupMaterialList)
    },
    
    // 待选择中的材料列表页面，确定并返回待补正材料面板
    selectMaterialToMakeupList: function () {
      var _selectedMaterialList = [];
      _selectedMaterialList = this.tobeSelectMaterialList.filter(function (item) {
        return (item.isNeedOrign || item.isNeedCopy || item.isNeedElectron)
      })
      // console.log(_selectedMaterialList)
      // 已选的材料的id的数组，用于后面排除在待选列表返回已选时多次添加
      var _aleradyIds = [];
      this.tobaMakeupMaterialList.forEach(function (item) {
        _aleradyIds.push(item.matId);
      })
      // console.log(_aleradyIds)
      for (var i = 0; i < _selectedMaterialList.length; i++) {
        if (_selectedMaterialList[i].isNeedElectron) {
          _selectedMaterialList[i].isNeedAtt = true;
        }
        // 如果已选的材料列表里面没有，那就添加进去
        // 修复待补正清单存在该材料时，无法继续添加该材料（电子件、纸质和复印件）
        var _idx = _aleradyIds.indexOf(_selectedMaterialList[i].matId);
        if (_idx == -1) {
          this.tobaMakeupMaterialList.push(_selectedMaterialList[i]);
        } else {
          var mat = _selectedMaterialList[i];
          if (mat.isNeedElectron) {
            
            this.tobaMakeupMaterialList[_idx]['isNeedElectron'] = mat.isNeedElectron;
            this.tobaMakeupMaterialList[_idx]['isNeedAtt'] = mat.isNeedAtt;
            this.tobaMakeupMaterialList[_idx]['attIsCollected'] = mat.attIsCollected;
            this.tobaMakeupMaterialList[_idx]['attMatinstId'] = mat.attMatinstId;
            
          } else if (mat.isNeedOrign) {
            this.tobaMakeupMaterialList[_idx]['isNeedOrign'] = mat.isNeedOrign;
            this.tobaMakeupMaterialList[_idx]['paperCount'] = mat.paperCount;
            this.tobaMakeupMaterialList[_idx]['paperIsCollected'] = mat.paperIsCollected;
            this.tobaMakeupMaterialList[_idx]['paperMatinstId'] = mat.paperMatinstId;
            this.tobaMakeupMaterialList[_idx]['realPaperCount'] = mat.realPaperCount;
            
          } else if (mat.isNeedCopy) {
            this.tobaMakeupMaterialList[_idx]['isNeedCopy'] = mat.isNeedCopy;
            this.tobaMakeupMaterialList[_idx]['copyCount'] = mat.copyCount;
            this.tobaMakeupMaterialList[_idx]['copyIsCollected'] = mat.copyIsCollected;
            this.tobaMakeupMaterialList[_idx]['copyMatinstId'] = mat.copyMatinstId;
            this.tobaMakeupMaterialList[_idx]['realCopyCount'] = mat.realCopyCount;
          }
        }
        
      }
      
      if (_selectedMaterialList.length < 1 && _aleradyIds.length > 0) {
        for (var j in _aleradyIds) {
          for (var k in this.tobeSelectMatIds) {
            if (_aleradyIds[j] == this.tobeSelectMatIds[k]) {
              var obj = this.tobaMakeupMaterialList[j];
              var obj2 = this.tobeSelectMaterialList[k];
              if (obj['attMatinstId'] != null && obj2['attMatinstId'] != null) {
                
                obj['isNeedElectron'] = obj2['isNeedElectron'];
                
              } else if (obj['paperMatinstId'] != null && obj2['paperMatinstId'] != null) {
                
                obj['isNeedOrign'] = obj2['isNeedOrign'];
                
              } else if (obj['copyMatinstId'] != null && obj2['copyMatinstId'] != null) {
                
                obj['isNeedCopy'] = obj2['isNeedCopy'];
                
              }
            }
          }
        }
      }
    },
    // 待选列表中选中状态切换调用-去除掉已选中的材料中的不需要电子件、原件、复印件的材料数据
    clickTobaSelectMaterial: function () {
      var _delIndex = -1;
      for (var i = 0; i < this.tobaMakeupMaterialList.length; i++) {
        var item = this.tobaMakeupMaterialList[i];
        if (!item.isNeedOrign && !item.isNeedCopy && !item.isNeedElectron) {
          _delIndex = i;
          break;
        }
      }
      if (_delIndex !== -1) {
        this.tobaMakeupMaterialList.splice(_delIndex, 1)
      }
    },
    
    
    // 保存材料补正的数据
    saveMaterialCorrection: function () {
      var ts = this, saveData = {};
      this.$refs['supplemetForm'].validate(function (valid) {
        if (valid) {
          // 校验补正份数的大小
          var _noPip = {};
          for (var i = 0; i < ts.tobaMakeupMaterialList.length; i++) {
            var _curMat = ts.tobaMakeupMaterialList[i];
            if (_curMat.isNeedOrign && _curMat.paperCount < 1) {
              _noPip = _curMat;
              break;
            }
            if (_curMat.isNeedCopy && _curMat.copyCount < 1) {
              _noPip = _curMat;
              break;
            }
            // test isNeedAtt 传给后端为1
            if (_curMat.isNeedElectron) {
              _curMat.isNeedAtt = '1';
            } else {
              _curMat.isNeedAtt = '0';
            }
            
            // 将待选中带过来的不需要的copyCount和paperCount清零
            if (!_curMat.isNeedCopy) {
              _curMat.copyCount = 0;
            }
            if (!_curMat.isNeedOrign) {
              _curMat.paperCount = 0;
            }
            
          }
          console.log(_noPip)
          if (JSON.stringify(_noPip) !== "{}") {
            return ts.$message.error('材料：‘' + _noPip.matName + '的补正份数不能为0！请重新输入');
            return false;
          }
          // return true;
          
          saveData = {
            applyinstId: ts.masterEntityKey,
            iteminstId: ts.iteminstId,
            isFlowTrigger: '1',
            taskinstId: ts.taskId,
            correctMemo: ts.supplementDialogData.correctMemo,
            correctDueDays: ts.supplemetForm.correctDueDays,
            matCorrectDtosJson: JSON.stringify(ts.tobaMakeupMaterialList)
          };
          ts.sloading = true;
          request('', {
            url: ctx + 'rest/correct/createMatCorrectinst',
            type: 'post',
            data: saveData
          }, function (res) {
            ts.sloading = false;
            if (res.success) {
              // ts.$message.success('保存成功！');
              vm.showSuccessTip('保存成功！');
              ts.isShowMaterialSupplementDialog = false;
            } else {
              ts.$message.error(res.message);
            }
          }, function () {
            ts.sloading = false;
            ts.$message.error('网络错误！');
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 材料补全按钮相关
    // 材料补全dialog,获取数据
    getLackMatsMatmend: function () {
      var ts = this;
      ts.sloading = true;
      request('', {
        url: ctx + 'applyinst/correct/getLackMats',
        // url: ctx+'apply/js/matMend.json',
        type: 'get',
        data: {
          applyinstId: ts.masterEntityKey
        }
      }, function (res) {
        ts.sloading = false;
        if (res.success) {
          ts.matMendDialogData = res.content;
          ts.matCorrectDtos = ts.matMendDialogData.matCorrectDtos || [];
          ts.submittedMats = ts.matMendDialogData.submittedMats || [];
          ts.handelSupplementDataMend(); // 处理材料补全回显数据
          ts.getMatsMend(ts.matCorrectDtos); // 处理补全清单  已收清单列表
          ts.getMatsMend(ts.submittedMats); // 处理补全清单  已收清单列表
          ts.handelMatCorrectDtosMend();
          ts.isShowMatmend = true;
        } else {
          if (res.content != null) {
            ts.matMendDialogData = res.content;
            ts.matCorrectDtos = ts.matMendDialogData.matCorrectDtos || [];
            ts.submittedMats = ts.matMendDialogData.submittedMats || [];
            ts.handelSupplementDataMend(); // 处理材料补全回显数据
            ts.getMatsMend(ts.matCorrectDtos); // 处理补全清单  已收清单列表
            ts.getMatsMend(ts.submittedMats); // 处理补全清单  已收清单列表
            ts.handelMatCorrectDtosMend();
            ts.isShowMatmend = true;
          } else {
            ts.$message.error(res.message ? res.message : '网络错误！');
          }
        }
      }, function () {
        ts.sloading = false;
        ts.$message.error('网络错误！');
      });
    },
    // 处理材料补全回显数据
    handelSupplementDataMend: function () {
      this.matMendForm = {
        applyinstCode: this.matMendDialogData.applyinstCode,
        approveOrgName: this.matMendDialogData.chargeOrgName,
        iteminstName: this.matMendDialogData.iteminstName,
        localCode: this.matMendDialogData.localCode,
        owner: this.matMendDialogData.owner,
        projInfoName: this.matMendDialogData.projInfoName,
        correctDueDays: this.matMendDialogData.correctDueDays ? this.matMendDialogData.correctDueDays : '',
      };
    },
    // 处理补全清单  已收清单列表
    getMatsMend: function (matsMendList) {
      matsMendList.map(function (item) {
        if (typeof item.isNeedOrign == "undefined") {
          Vue.set(item, 'isNeedOrign', false);
        } else {
          item.isNeedOrign = false;
        }
        if (typeof item.isNeedCopy == "undefined") {
          Vue.set(item, 'isNeedCopy', false);
        } else {
          item.isNeedCopy = false;
        }
        if (typeof item.isNeedElectron == "undefined") {
          Vue.set(item, 'isNeedElectron', false);
        } else {
          item.isNeedElectron = false;
        }
        if (item.paperCount && item.paperCount !== null && parseInt(item.paperCount) > 0) {
          //是否需要展示原件
          item.isNeedOrign = true;
        }
        if (item.copyCount && item.copyCount !== null && parseInt(item.copyCount) > 0) {
          //是否需要展示复印件
          item.isNeedCopy = true;
        }
        if (item.isNeedAtt == '1') {
          //是否需要展示电子件
          item.isNeedElectron = true;
          //当需要扎实电子件时，默认将isNeedAtt置为true
          // item.isNeedAtt = '1';
        }
      });
    },
    // 处理已提交材料列表 已上传文件
    handelMatCorrectDtosMend: function () {
      var _that = this;
      var subLen = _that.submittedMats.length;
      var matLen = _that.matCorrectDtos.length;
      _that.submittedMats.map(function (item) {
        if (typeof item.isNeedOrignSel == "undefined") {
          Vue.set(item, 'isNeedOrignSel', false);
        } else {
          item.isNeedOrignSel = false;
        }
        if (typeof item.isNeedCopySel == "undefined") {
          Vue.set(item, 'isNeedCopySel', false);
        } else {
          item.isNeedCopySel = false;
        }
        if (typeof item.isNeedElectronSel == "undefined") {
          Vue.set(item, 'isNeedElectronSel', false);
        } else {
          item.isNeedElectronSel = false;
        }
        _that.matCorrectDtos.map(function (item1) {
          if (item.matId == item1.matId) {
            if (item.paperCount > 0 && item1.paperCount > 0) {
              item.isNeedOrignSel = true;
              item1.isNeedOrign = true;
            }
            if (item.copyCount > 0 && item1.copyCount > 0) {
              item.isNeedCopySel = true;
              item1.isNeedCopy = true;
            }
            if (item.attCount > 0 && item1.attCount > 0) {
              item.isNeedElectronSel = true;
              item1.isNeedElectron = true;
            }
          }
        })
      });
    },
    // 删除待补全材料列表中的某个材料的某一项（原件|复印件|电子件）
    delTobaMakeupMaterialMend: function (attr, item, index) { // attr移除的材料类型 item材料信息 index
      var _that = this;
      if (attr == 'isNeedOrign') { // 删除原件
        item.isNeedOrign = false;
        item.paperCount = 0;
      }
      if (attr == 'isNeedCopy') { // 删除复印件
        item.isNeedCopy = false;
        item.copyCount = 0;
      }
      if (attr == 'isNeedElectron') { // 删除电子件
        item.isNeedAtt = '0';
        item.attCount = 0;
        item.isNeedElectron = false;
      }
      _that.handelMatCorrectDtosMend();
      if (!item.isNeedOrign && !item.isNeedCopy && !item.isNeedElectron) {
        _that.matCorrectDtos.splice(index, 1);
      }
      
    },
    // 保存材料补全的数据
    saveMaterialCorrectionMend: function () {
      var ts = this, saveData = {};
      this.$refs['matMendForm'].validate(function (valid) {
        if (valid) {
          // 校验补正份数的大小
          var _noPip = {};
          for (var i = 0; i < ts.matCorrectDtos.length; i++) {
            var _curMat = ts.matCorrectDtos[i];
            if (_curMat.isNeedOrign && _curMat.paperCount < 1) {
              _noPip = _curMat;
              break;
            }
            if (_curMat.isNeedCopy && _curMat.copyCount < 1) {
              _noPip = _curMat;
              break;
            }
          }
          if (JSON.stringify(_noPip) !== "{}") {
            return ts.$message.error('材料：‘' + _noPip.matName + '的补全份数不能为0！请重新输入');
            return false;
          }
          // return true;
          if (ts.matCorrectDtos.length == 0) {
            return ts.$message.error('材料补全份数不能为0！');
            return false;
          }
          saveData = {
            applyinstId: ts.masterEntityKey,
            isFlowTrigger: '1',
            correctMemo: ts.matMendDialogData.correctMemo,
            correctDueDays: ts.matMendForm.correctDueDays,
            matCorrectDtosJson: JSON.stringify(ts.matCorrectDtos),
            projInfoId: ts.projInfoId,
          };
          ts.sloading = true;
          request('', {
            url: ctx + 'applyinst/correct/createMatCorrectinst',
            type: 'post',
            data: saveData
          }, function (res) {
            ts.sloading = false;
            if (res.success) {
              ts.$message.success('材料补全开始');
              ts.isShowMatmend = false;
              delayCloseWindow();
            } else {
              ts.$message.error(res.message);
            }
          }, function () {
            ts.sloading = false;
            ts.$message.error('网络错误！');
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 待选列表中选中状态切换调用-去除掉已选中的材料中的不需要电子件、原件、复印件的材料数据
    clickTobaSelectMaterial1: function (val, dataMat, attrFlag) { // attrFlag 选择的材料类型
      var _that = this;
      // if(val==false){
      //   return false;
      // }
      var arr = JSON.parse(JSON.stringify(_that.matCorrectDtos));
      if (arr && arr.length > 0) {
        var flag = true;
        for (var i = 0; i < arr.length; i++) {
          
          if (arr[i].matId == dataMat.matId) {
            if (attrFlag == 'isNeedOrignSel') {
              _that.matCorrectDtos[i].isNeedOrign = true;
              _that.matCorrectDtos[i].paperCount = dataMat.paperCount;
            }
            if (attrFlag == 'isNeedCopySel') {
              _that.matCorrectDtos[i].isNeedCopy = true;
              _that.matCorrectDtos[i].copyCount = dataMat.copyCount;
            }
            if (attrFlag == 'isNeedElectronSel') {
              _that.matCorrectDtos[i].isNeedElectron = true;
              _that.matCorrectDtos[i].isNeedAtt = dataMat.isNeedAtt;
            }
            flag = false;
            break;
          }
        }
        ;
        if (flag) {
          var firstArr = JSON.parse(JSON.stringify(dataMat));
          if (attrFlag == 'isNeedOrignSel') {
            firstArr.isNeedOrign = true;
            firstArr.paperCount = dataMat.paperCount;
            firstArr.copyCount = 0;
            firstArr.isNeedAtt = '0';
            firstArr.isNeedCopy = false;
            firstArr.isNeedElectron = false;
          }
          if (attrFlag == 'isNeedCopySel') {
            firstArr.isNeedOrign = false;
            firstArr.copyCount = dataMat.copyCount;
            firstArr.paperCount = 0;
            firstArr.isNeedAtt = '0';
            firstArr.isNeedCopy = true;
            firstArr.isNeedElectron = false;
          }
          if (attrFlag == 'isNeedElectronSel') {
            firstArr.isNeedElectron = true;
            firstArr.isNeedAtt = dataMat.isNeedAtt;
            firstArr.copyCount = 0;
            firstArr.paperCount = 0;
            firstArr.isNeedOrign = false;
            firstArr.isNeedCopy = false;
          }
          _that.matCorrectDtos.push(firstArr);
        }
      } else {
        var firstArr = JSON.parse(JSON.stringify(dataMat));
        if (attrFlag == 'isNeedOrignSel') {
          firstArr.isNeedOrign = true;
          firstArr.paperCount = dataMat.paperCount;
          firstArr.copyCount = 0;
          firstArr.isNeedAtt = '0';
          firstArr.isNeedCopy = false;
          firstArr.isNeedElectron = false;
        }
        if (attrFlag == 'isNeedCopySel') {
          firstArr.isNeedOrign = false;
          firstArr.copyCount = dataMat.copyCount;
          firstArr.paperCount = 0;
          firstArr.isNeedAtt = '0';
          firstArr.isNeedCopy = true;
          firstArr.isNeedElectron = false;
        }
        if (attrFlag == 'isNeedElectronSel') {
          firstArr.isNeedElectron = true;
          firstArr.isNeedAtt = dataMat.isNeedAtt;
          firstArr.copyCount = 0;
          firstArr.paperCount = 0;
          firstArr.isNeedOrign = false;
          firstArr.isNeedCopy = false;
        }
        _that.matCorrectDtos.push(firstArr);
      }
      _that.matCorrectDtos.map(function (item, index) {
        if (!item.isNeedOrign && !item.isNeedCopy && !item.isNeedElectron) {
          _that.matCorrectDtos.splice(index, 1);
        }
      })
    },
    onlySuggestDialogShow: function () {
      var vm = this;
      if (vm.checkCanClick()) return null;
      vm.getOpinionList();
      vm.onlySuggestDialog = true;
    },
    
    resetForm: function (formName) {
      this.$refs[formName].resetFields();
    },
    formatDate: function (date) {
      date = date.substring(0, 10);
      date = date.replace(/-/g, '/');
      return new Date(date);
    },//
    startSpecialProcessForItem: function () {
      startSpecialProcessForItem();
    },
    exitSpecialProcessForItem: function () {
      var vm = this;
      vm.$confirm('此操作将退出特殊程序, 是否继续?', '退出特殊程序', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function () {
        vm.parentPageLoading = true;
        request('', {
          url: ctx + 'rest/specialProcedure/stopSpecialBtn',
          type: 'get',
          data: {
            'applyinstId': vm.masterEntityKey,
            'iteminstId': vm.iteminstId
          }
        }, function (res) {
          vm.parentPageLoading = false;
          if (res.success) {
            vm.$message.success('已退出特殊程序！2秒后将刷新页面');
            delayRefreshWindow();
          } else {
            vm.$message.error(res.message);
          }
        }, function (msg) {
          vm.parentPageLoading = false;
          alertMsg('', '查询回执失败', '关闭', 'error', true);
        });
      }).catch(function () {
      });
    },
    // 关闭回退弹窗
    closeBackDialog: function () {
      this.$refs.backFormRef.resetFields();
      this.backFormData.selectOpinion = '';
    },
    // 确认回退
    ensureBack: function () {
      this.$refs.backFormRef.validate(function (f) {
        if (f) {
          if (vm.isBackDialog) {
            vm.backDiaLoading = true;
            request('', {
              url: ctx + 'rest/front/task/returnPrevTask/' + vm.taskId,
              type: 'get',
              data: vm.backFormData,
            }, function (res) {
              vm.backDiaLoading = false;
              if (res.success) {
                vm.$message.success('回退任务成功！2秒后关闭页面');
                delayCloseWindow();
                if (window.opener) {
                  window.opener.location.reload();
                }
              } else {
                vm.$message.error(res.message);
              }
            }, function (res) {
              vm.backDiaLoading = false;
              vm.$message.error('回退任务失败！')
            });
          } else {
            vm.backDiaLoading = true;
            request('', {
              url: ctx + 'rest/apply/common/processflow/start',
              type: 'post',
              data: {
                applyinstId: vm.masterEntityKey,
                comment: vm.backFormData.comment,
              }
            }, function (res) {
              vm.backDiaLoading = false;
              if (res.success) {
                vm.$message.success('发起申报成功！2秒后关闭页面');
                delayCloseWindow();
                if (window.opener) {
                  window.opener.location.reload();
                }
              } else {
                vm.$message.error(res.message || '发起申报失败');
              }
            }, function (res) {
              vm.backDiaLoading = false;
              vm.$message.error('发起申报失败！')
            })
          }
        }
      });
    },
    // 点击回退按钮
    returnPrevTask: function () {
      var vm = this;
      if (vm.opinionTableData.length == 0) {
        vm.getOpinionList();
      }
      if (vm.isBackDialog) {
        vm.backDiaTitle = '回退';
      } else {
        vm.backDiaTitle = '发起申报';
        vm.backDiaVisible = true;
        return null;
      }
      vm.parentPageLoading = true;
      request('', {
        url: ctx + 'rest/front/task/getReturnPrevTasksByTaskId?taskId=' + vm.taskId,
        type: 'get',
      }, function (res) {
        vm.parentPageLoading = false;
        if (res.success) {
          // 默认选中最后一个可选的节点
          var _index = -1;
          res.content.forEach(function (u, i) {
            if (u.isCanSelect) {
              _index = i
            }
          });
          if (_index == -1) {
            vm.$message.error('无可选的回退节点');
          } else {
            // 打开回退弹窗
            vm.backDiaVisible = true;
            vm.backNodeList = res.content;
            vm.backFormData.returnToActId = res.content[_index].nodeId;
          }
        } else {
          vm.$message.error(res.message || '获取回退数据失败');
        }
      }, function () {
        vm.parentPageLoading = false;
        vm.$message.error('获取回退数据失败');
      });
      if (window) return null;
      vm.$prompt('请输入回退意见', '任务回退', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        customClass: 'prompt-dia',
        inputValidator: function (val) {
          return !!val
        },
        inputErrorMessage: '请填写回退意见',
      }).then(function (obj) {
        vm.parentPageLoading = true;
        request('', {
          url: ctx + 'rest/front/task/returnPrevTask/' + vm.taskId + '?comment=' + obj.value,
          type: 'get',
        }, function (res) {
          vm.parentPageLoading = false;
          if (res.success) {
            vm.$message.success('回退任务成功！2秒后关闭页面');
            delayCloseWindow();
            if (window.opener) {
              window.opener.location.reload();
            }
          } else {
            vm.$message.error(res.message);
          }
        }, function (res) {
          vm.parentPageLoading = false;
          vm.$message.error('回退任务失败！')
        })
      }).catch(function () {
      });
    },
    // 发起申报
    startDeclare: function () {
      var vm = this;
      vm.returnPrevTask();
      if (vm) return null;
      vm.$prompt('请输入申报意见', '发起申报', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        customClass: 'prompt-dia',
        inputValidator: function (val) {
          return !!val
        },
        inputErrorMessage: '请填写申报意见',
      }).then(function (obj) {
        vm.parentPageLoading = true;
        request('', {
          url: ctx + 'rest/apply/common/processflow/start',
          type: 'post',
          data: {
            applyinstId: vm.masterEntityKey,
            comment: obj.value
          }
        }, function (res) {
          vm.parentPageLoading = false;
          if (res.success) {
            vm.$message.success('发起申报成功！2秒后关闭页面');
            delayCloseWindow();
            if (window.opener) {
              window.opener.location.reload();
            }
          } else {
            vm.$message.error(res.message || '发起申报失败');
          }
        }, function (res) {
          vm.parentPageLoading = false;
          vm.$message.error('发起申报失败！')
        })
      }).catch(function () {
      });
    },
    preFile: function (obj, visibleKey) {
      var vm = this;
      if (obj.attFormat == 'dwg') {
        // 查看施工图
        var tempwindow = window.open(); // 先打开页面
        setTimeout(function () {
          tempwindow.location = ctx + 'cod/drawing/drawingCheck?detailId=' + obj.detailId;
        }, 1000)
        return null;
      }
      if (obj.attFormat == 'pdf') {
        // PDF文件直接打开预览，不需要等待转换
        return vm.doPreFile(obj);
      }
      // 判断服务器端文件是否已经转换成功，转换成功才能预览
      if (visibleKey) {
        vm[visibleKey] = true;
      } else {
        vm.parentPageLoading = true;
      }
      var count = 0;
      doRequest();
      
      function doRequest() {
        request('', {
          url: ctx + 'previewPdf/pdfIsCoverted?detailId=' + obj.detailId,
          type: 'get',
        }, function (res) {
          if (res.success) {
            count = 0;
            vm.parentPageLoading = false;
            if (visibleKey) {
              vm[visibleKey] = false;
            }
            vm.doPreFile(obj);
          } else {
            if (++count > 9) {
              confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                count = 0;
                doRequest();
              }, function () {
                count = 0;
                vm.parentPageLoading = false;
                if (visibleKey) {
                  vm[visibleKey] = false;
                }
                return false;
              }, '确定', '取消', 'warning', true)
            } else {
              setTimeout(function () {
                doRequest();
              }, 1000)
            }
            // vm.$message('文件还未转换成功，请稍后再进行预览操作');
          }
        }, function (res) {
          count = 0;
          vm.parentPageLoading = false;
          if (visibleKey) {
            vm[visibleKey] = false;
          }
          vm.$message.error('请求预览文件失败')
        });
      }
    },
    doPreFile: function (obj) {
      var vm = this;
      vm.pdfSrc = '';
      vm.pdfSrc = ctx + 'previewPdf/view?detailId=' + obj.detailId;
      vm.prePdfVisible = true;
    },
    closeForwardDialog: function () {
      this.$refs.forwardFormRef.resetFields();
    },
    openSendToOneDialog: function () {
      // 加载下一转发人下拉列表
      var vm = this;
      if (this.nextTaskManList.length === 0) {
        vm.parentPageLoading = true;
        request('', {
          url: ctx + 'rest/front/task/getCurrTaskAssigneeOrRange/' + vm.taskId,
          type: 'get',
        }, function (res) {
          vm.parentPageLoading = false;
          if (res.success) {
            //
            var tmpArr = [];
            res.content.assigneeRange.split(',').forEach(function (u) {
              u && tmpArr.push({label: u, value: u})
            })
            if (tmpArr.length == 0) {
              return vm.$message.error('未找到可转发人')
            }
            vm.nextTaskManList = tmpArr;
            vm.forwardTaskVisible = true;
          } else {
            vm.$message.error(res.message);
          }
        }, function () {
          vm.parentPageLoading = false;
          vm.$message.error('加载可转发人列表失败')
        })
      } else {
        vm.forwardTaskVisible = true;
      }
    },
    ensureForward: function () {
      var vm = this;
      this.$refs.forwardFormRef.validate(function (valid) {
        if (valid) {
          vm.forwardDiaLoading = true;
          var reqData = $.extend({}, vm.forwardForm);
          reqData.taskId = vm.taskId;
          request('', {
            url: ctx + 'rest/front/task/sendOnTask',
            type: 'post',
            data: reqData,
          }, function (res) {
            vm.forwardDiaLoading = false;
            if (res.success) {
              //
              vm.$message.success('任务已经转发至下一办理人，2秒后关闭页面')
              delayCloseWindow(2000);
              if (window.opener) {
                window.opener.location.reload();
              }
            } else {
              vm.$message.error(res.message);
            }
          }, function () {
            vm.$message.error('转发任务失败');
            vm.forwardDiaLoading = false;
          })
        }
      });
    },
//=========================================工程建设 相关审批按钮 end =================
  },
  created: function () {
    axios.defaults.withCredentials = true;
    this.initPage();
  },
  watch: {
    expanded: function (a, b) {
      // 动画结束后调整隐藏的按钮
      setTimeout(function () {
        vm.setButtonShow();
      }, 310)
    },
    iteminstName: function (val, oldVal) {
      document.title = this.stageOrItemName + this.iteminstName;
    },
    stageOrItemName: function (val, oldVal) {
      document.title = this.stageOrItemName + this.iteminstName;
    }
  },
  mounted: function () {
    var _this = this;
    window.addEventListener("resize", function () {
      vm.setButtonShow();
    });
    var fileInput = $('.upload-input')
    $('.upload-input').change(function (e) {
      var files = _this.$refs.uploadInput.files;
      var formData = new FormData()
      formData.append('files', files[0]);
      formData.append('tableName', tableName);
      formData.append('pkName', pkName);
      formData.append('recordId', _this.masterEntityKey);
      if (_this.selectedNodeId != -1) {
        formData.append('dirId', _this.selectedNodeId);
      }
      axios.defaults.withCredentials = true;
      vm.fileLoading = true;
      axios.post(ctx + 'rest/approve/att/file/upload', formData).then(function (res) {
        if (res.data && res.data.success) {
          // 重新加载节点
          _this.getAttachment(_this.selectedNodeId != -1 ? _this.selectedNodeId : null);
        } else {
          _this.$message.error(res.message);
        }
      }).catch(function (e) {
        _this.$message.error('文件上传失败');
      }).finally(function () {
        vm.fileLoading = false;
      })
    })
    
    // tmp for test
    // window.setTimeout(function(){
    //   vm.openPrintLicenceDialog();
    //   var _iframe = $('#preLicenceIframeId')[0].contentWindow;
    //   console.log(_iframe)
    // }, 2000);
    
  },
});

function showDiagramDialog() {
  vm.showDiagramDialog();
}

//工具栏调用接口使用到的相关私有方法
function checkParentProExist(procInstId) {
  vm.checkParentProExist(procInstId);
}

//查看子流程
function showChildrenDiagramDialog(node) {
  //这个 是在 后台生成流程图节点信息的接口约定的
  var procInstId = $(node).attr("data-procInstId");
  var isCheck = $(node).attr("data-isCheck");
  vm.showChildrenDiagramDialog(procInstId, isCheck);
}

//触发子流程
function triggerSubFlow(node) {
  //这个 是在 后台生成流程图节点信息的接口约定的
  var taskId = $(node).attr("data-taskId");
  var eventName = $(node).attr("data-eventName");
  vm.triggerSubFlow(taskId, eventName);
}

function getUrlParam(s) {
  return vm.getUrlParam(s);
}

// jQuery ajax
function http(params) {
  return request('', {
    type: params.type || 'get',
    url: params.url,
    data: params.data || {},
    timeout: params.timeout,
  }, function (res) {
    if (res.success) {
      typeof params.success === 'function' && params.success(res)
    } else {
      vm.$message({
        message: res.message || '接口请求失败',
        type: 'error'
      });
    }
  })
}


//4.0 ******************** 新接口  start********************************************

//---------------------- 制证 ----------------------------------------
function makeCertification() {
  vm.makeCertification();
  
}

function testBtn() {
  var message = "<span style='color:#22D479;font-size:18px' >流程发送成功！</span>";
  message += "<span style='font-size:18px'>任务处理完毕，正等待其他用户处理！</span>";
  var _nextTask = vm.nextTask;
  var _nextTaskAssignee = vm.nextTaskAssignee;
  if (vm.isMultiFlow) {
    _nextTask = vm.mutiCheckedNames.join('、');
    var _tmpArr = [];
    vm.mutiCheckedMan.forEach(function (u) {
      _tmpArr.push(u.defaultSendAssignees || '暂无审批人');
    });
    _nextTaskAssignee = _tmpArr.join('、');
  }
  
  var message1 = "流程已发送至 <span style='color:#22D479;font-size:18px' >&nbsp;" + _nextTask + "</span>&nbsp;环节";
  message1 += "，下一环节审批人为：<span style='color:#22D479;font-size:18px' >&nbsp;" + _nextTaskAssignee + "&nbsp;。</span>";
  this.$message({
    showClose: true,
    dangerouslyUseHTMLString: true,
    message: message,
    type: 'success',
    center: true
  });
  
}

//--------------更改事项状态和流程------------------------------------------

//事项-容缺通过
function passOfToleranceForItem() {
  vm.enumItemStatus = 'AGREE_TOLERANCE';
  vm.requestMappingType = 'put';
  vm.isRQDialog = true;
  urlForItem();
}

//事项-不受理
function rejectForItem() {
  vm.enumItemStatus = 'REFUSE_DEAL';
  vm.requestMappingType = 'put';
  urlForItem();
}

//事项-办结（通过）
function passForItem() {
  vm.enumItemStatus = 'AGREE';
  vm.requestMappingType = 'put';
  urlForItem();
}

//事项-办结（不通过）
function denyForItem() {
  vm.enumItemStatus = 'DISAGREE';
  vm.requestMappingType = 'put';
  urlForItem();
}

//事项-受理
function acceptForItem() {
  //vm.enumItemStatus = 'ACCEPT_DEAL';//3
  
  //20190813 改为部门受理状态
  vm.enumItemStatus = 'DEPARTMENT_DEAL_START';//3
  vm.requestMappingType = 'put';
  urlForItem();
}

//事项-容缺受理
function toleranceAcceptForItem() {
  //20190813 改为部门受理状态
  vm.enumItemStatus = 'DEPARTMENT_DEAL_START:TOLERANCE_ACCEPT';//部门开始受理：3，并且标识为容缺受理
  vm.requestMappingType = 'put';
  urlForItem();
}

//更改事项状态和流程url
function urlForItem() {
  vm.sendUrlPath = ctx + "/approve/btn/item/wfSendAndChangeItemState";
  vm.requestMappingType = 'put';
  vm.wfBusSend();
}

// 中介事项  三个按钮事件
function zjItem1(){
  vm.sendUrlPath = ctx + "market/approve/btn/win/wfSendAndChangeApplyState";
  vm.requestMappingType = 'put';
  vm.wfBusSend();
}
function zjItem2(){
  vm.sendUrlPath = ctx + "market/approve/btn/win/wfSendAndChangeApplyAndItemState";
  vm.requestMappingType = 'put';
  vm.wfBusSend();
}
function zjItem3(){
  vm.sendUrlPath = ctx + "market/approve/btn/item/wfSendAndChangeItemState";
  vm.requestMappingType = 'put';
  vm.wfBusSend();
}

//--------------------------更改窗口-状态及流程--------------------------

//窗口-办结   更改状态并推动流程
function finishForWin() {
  vm.enumApplyStatus = 'COMPLETED';//6
  vm.sendUrlPath = ctx + "/approve/btn/win/wfSendAndChangeApplyState";
  vm.requestMappingType = 'put';
  vm.wfBusSend();
}

//窗口-受理  更改状态并推动流程
function acceptForWin() {//2
  vm.enumApplyStatus = 'ACCEPT_DEAL'; //2
  vm.sendUrlPath = ctx + "/approve/btn/win/wfSendAndChangeApplyState";
  vm.requestMappingType = 'put';
  vm.wfBusSend();
}

//窗口-网上-预审通过
function preApprovePass() {
  vm.enumApplyStatus = 'ACCEPT_DEAL';//2
  vm.enumItemStatus = 'ACCEPT_DEAL';//3
  vm.sendUrlPath = ctx + "/approve/btn/win/wfSendAndChangeApplyAndItemState";
  vm.requestMappingType = 'put';
  //0材料补全，1不予受理，2预审通过
  vm.preApproveOperation = "预审通过";
  vm.sendParam = {
    taskId: vm.taskId,
    sendConfigs: [{
      isUserTask: true,
      isEnableMultiTask: false,
      destActId: "bumenshenpi"//这里是固定的，要求一级流程部门审批节点编号必须配置成这个字符串
    }]
  };
  vm.nextTask = "部门审批";
  vm.onlySuggestDialogShow();
}

//窗口-网上-不予受理
function outScope() {
  vm.enumApplyStatus = 'OUT_SCOPE';//5
  vm.enumItemStatus = 'OUT_SCOPE';//5
  vm.sendUrlPath = ctx + "/approve/btn/win/wfSendAndChangeApplyAndItemState";
  vm.requestMappingType = 'put';
  vm.preApproveOperation = '不予受理';
  vm.sendParam = {
    taskId: vm.taskId,
    sendConfigs: [{
      isUserTask: false,
      isEnableMultiTask: false,
      destActId: "jieshu"//这里是固定的，要求一级流程结束节点编号必须配置成这个字符串
    }]
  };
  vm.nextTask = "结束";
  vm.onlySuggestDialogShow();
}

//窗口-网上-材料补全
function matCompletion() {
  vm.enumApplyStatus = 'IN_THE_SUPPLEMENT';//3
  vm.sendUrlPath = ctx + "/approve/btn/win/changeApplyState";
  vm.requestMappingType = 'put';
  vm.preApproveOperation = '材料补全';
  // showSupplementForItem();
  vm.getLackMatsMatmend();
}

//-----------------------以下使用原来的流程---------------------------------

//窗口-办理---只推动流程流转
function handleForWin() {
  vm.sendUrlPath = ctx + 'rest/front/task/wfSend';
  vm.requestMappingType = 'post';
  vm.wfBusSend();
}

//事项-办理---只推动流程流转
function handleForItem() {
  // vm.sendDialogVisible = true;
  vm.sendUrlPath = ctx + 'rest/front/task/wfSend';
  vm.requestMappingType = 'post';
  vm.wfBusSend();
  
  /*vm.enumItemStatus = 'DEPARTMENT_DEAL_START';//3
  vm.requestMappingType = 'put';
  urlForItem();*/
}

//************** 以下只改变状态 start ***************************

//事项---材料补正开始
function startSupplementForItem() {
  vm.enumActionCode = 'ITEM_CAILIAOBUZHENG';
  vm.enumItemStatus = 'CORRECT_MATERIAL_START';
  vm.commentTitle = '材料补正';
  vm.requestMappingType = 'put';
  vm.sendUrlPath = ctx + "/approve/btn/item/changeItemState";
  // showCommentDialog();
  showSupplementForItem();
}

// 材料补正按钮-触发vue实例事件
function showSupplementForItem() {
  // vm.fetchLackMatsByApplyinstIdAndIteminstId();
  vm.openBzDialog();
}

//开始特别程序方法---事项
function startSpecialProcessForItem() {
  /*vm.enumActionCode = 'ITEM_JINRU_TEBIECHENGXU';
  vm.enumItemStatus = 'SPECIFIC_PROC_START';
  vm.commentTitle = '开始特别程序';
  vm.requestMappingType = 'put';
  vm.sendUrlPath = ctx + "/approve/btn/item/changeItemState";
  console.info('====');*/
  //判断当前事项实例状态，补正状态和特殊撤销以及开始的不能发起特殊撤销
  vm.parentPageLoading = true;
  request('', {
    url: ctx + 'rest/specialProcedure/getCurrentSpecialStatus',
    type: 'get',
    data: {'applyinstId': vm.masterEntityKey, 'iteminstId': vm.iteminstId}
  }, function (res) {
    vm.parentPageLoading = false;
    if (res.success) {
      
      /*if(res.content.special.specialState == '9'){
              alertMsg('', "特殊程序已开始，不能再进入！", '关闭', 'error', true);
              return;
      }*/
      if (res.content.iteminst.iteminstState == '6') {
        alertMsg('', "材料补录阶段，不能进入特殊程序！", '关闭', 'error', true);
        return;
      }
      // 现需求为弹窗
      vm.openSpecialDialog();
      // 旧需求为打开新页面
      // var _url = ctx + 'rest/specialProcedure/index?iteminstId=' + vm.iteminstId + '&applyinstId=' + vm.masterEntityKey + '&isApprover=' + vm.isApprover + "&taskId=" + vm.taskId + "&processInstanceId=" + vm.processInstanceId;
      // window.open(_url, '_self');
      
    } else {
      vm.$message.error(res.message)
    }
  }, function (msg) {
    vm.parentPageLoading = false;
    alertMsg('', '进入特殊程序失败', '关闭', 'error', true);
  });
}

function openSpecialDialog() {
  vm.specialSrc = '';
  vm.specialDiaVisible = true;
  var src = './specialProcedures.html';
  src += '?iteminstId=' + vm.iteminstId;
  src += '&applyinstId=' + vm.masterEntityKey;
  src += '&isApprover=' + vm.isApprover;
  src += '&taskId=' + vm.taskId;
  src += '&processInstanceId=' + vm.processInstanceId;
  vm.specialSrc = src;
}

// 关闭特殊程序
function exitSpecialProcessForItem() {
  vm.exitSpecialProcessForItem();
}

//任务回退
function returnPrevTask() {
  vm.isBackDialog = true;
  vm.returnPrevTask();
}

//弹出意见对话框
function showCommentDialog() {
  //只改变状态
  if (vm.opinionTableData.length == 0) {
    vm.getOpinionList();
  }
  vm.submitCommentsShow = true;
}

// 预览文件
function preFile(obj) {
  vm.preFile(obj);
}

function openSendToOneDialog() {
  vm.openSendToOneDialog();
}

//打印回执列表接口
function getPrintList() {
  // var vm = this;
  vm.parentPageLoading = true;
  if (vm.receiveList.length == 0) {
    request('', {
      url: ctx + 'rest/receive/getStageOrSeriesReceiveByApplyinstIds',
      type: 'get',
      data: {'applyinstIds': vm.masterEntityKey}
    }, function (res) {
      vm.parentPageLoading = false;
      if (res.success) {
        vm.receiveList = res.content;
        vm.receiveList.map(function (item) {
          Vue.set(item, 'show', true);
        });
        vm.printReceive(vm.receiveList[0].receiveList[0], 0, 0);
        vm.receiptPrintDialog = true;
      } else {
        vm.$message.error('查询回执失败');
      }
    }, function (msg) {
      vm.parentPageLoading = false;
      vm.$message.error('查询回执失败');
    });
  } else {
    vm.$message('无回执信息')
    vm.parentPageLoading = false;
  }
  
}

function delayCloseWindow(time) {
  __STATIC.delayCloseWindow();
}

function delayRefreshWindow(time) {
  __STATIC.delayRefreshWindow(time);
}

function closeCurrentTab() {
  __STATIC.closeCurrentTab();
}

function onlySuggestDialogShow() {
  vm.onlySuggestDialogShow();
}

function seeAllProcessPic() {
  vm.seeAllProcessPic();
}

function startDeclare() {
  vm.isBackDialog = false;
  vm.startDeclare();
}

/**
 * @param result  是否成功
 * @param sFRenderConfig 哪个业务场景
 * @param formModelVal  表单值
 * @param actStoForminst  表单实例
 */
function callbackAfterSaveSFForm(result, sFRenderConfig, formModelVal, actStoForminst) {
  console.log(result);
  // vm.matFormVisible = false;
}

// window.setTimeout(function(){
//   passOfToleranceForItem();
// }, 4000)
//4.0 ******************** 新接口  end********************************************
