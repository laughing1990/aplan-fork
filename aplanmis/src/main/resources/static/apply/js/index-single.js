/*
 * @Author: ZL
 * @Date:   2019/6/4
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var vm = new Vue({
  el: '#approve',
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
    // 输入为数字 大于等于0（浮点数）
    var checkNumFloat = function (rule, value, callback) {
      if (value) {
        var flag = !/^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/.test(value);
        if (flag) {
          return callback(new Error('格式错误'));
        } else {
          callback();
        }
        
      } else {
        callback();
      }
    };
    // 输入为数字 大于等于0
    var checkMissNum = function (rule, value, callback) {
      if (value === '' || value === undefined || value === null || value.toString().trim() === '') {
        callback(new Error('必填字段！'));
      } else if (value) {
        var flag = !/^[+]{0,1}(\d+)$/.test(value);
        if (flag) {
          return callback(new Error('格式不正确'));
        } else {
          callback();
        }
        
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
    var checkUnifiedSocialCreditCode = function (rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('请输入统一社会信用代码！'));
      } else if (value) {
        var flag = !/^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/.test(value);
        if (flag) {
          return callback(new Error('请输入正确的统一社会信用代码！'));
        } else {
          callback();
        }
        
      } else {
        callback();
      }
    };
    return {
      isParallel: false, // true为并联
      isBranch: false,//是否分局承办
      itemBasicInfo: {},//事项信息
      itemVerId: '',//当前申报事项版本
      projBascInfoShow: {
        isAreaEstimate: '0',
        isDesignSolution: '0',
        gbCodeYear: '2017'
      }, // 项目信息
      localCode: '', // 项目编码
      loading: false, // 页面加载遮罩
      searchKeyword: '', // 查询关键字
      projInfoId: '', // 查询项目id
      projName: '', // 查询项目name
      otherUnits: [], // 申报主体为非企业（机关或事业单位）列表
      buildUnits: [], // 申报主体为企业列表
      agentUnits: [], // 经办单位信息
      agentChecked: false, // 申办主体信息 是否经办
      jiansheFrom: [],  // 申报主体为企业或非企业（机关或事业单位）时个人信息
      selThemeDialogShow: false, // 是否展示选择主题弹窗
      linkQuerySucc: false, // 项目代码工程编码是否可输入修改
      projTypeList: [], // 项目类型列表
      projType: '', // 项目类型
      projNatureList: [], // 建设性质列表
      projNature: '', // 建设性质
      approveNumClazz: true, // 是否显示备案文号
      projectTreeInfoModal: false, // 是否展示项目树窗口
      orgTreeInfoModal: false, // 部门组织树弹窗
      projTree: [], // 项目树 数据
      orgTree: [], // 部门组织树
      projTreeDefaultProps: {
        children: 'children',
        label: 'name'
      },
      isJg: false, // 是否机关单位 false为企业单位
      getResultForm: {
        receiveMode: 1,
        smsType: '',
        addresseeName: '',
        addresseePhone: '',
        addresseeIdcard: '',
        id: '',
      }, // 结果领取方式
      parallelThemeList: [], // 并联主题列表
      activeNames: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'], // el-collapse 默认展开列表
      ctx: ctx,
      verticalTabData: [ // 左侧纵向导航数据
        {
          label: '申报事项信息',
          target: 'itemBaseInfo'
        },
        {
          label: '项目基本信息',
          target: 'baseInfo'
        }, {
          label: '申报主体信息',
          target: 'applyInfo'
        }, {
          label: '事项情形',
          target: 'applyStage'
        },
        {
          label: '一张表单',
          target: 'oneForm'
        }, {
          label: '材料一单清',
          target: 'matsList'
        }, {
          label: '办证结果取件方式',
          target: 'getResult'
        }
      ],
      curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      popStateList: [], // 新增情形列表
      matsTableData: [], // 材料列表
      model: {
        rules: {
          getPaper: {required: true, message: "必选", trigger: ["change"]},
          getCopy: {required: true, message: "必选", trigger: ["change"]},
          realPaperCount: {validator: checkMissValue, required: true, message: "必填字段", trigger: ['blur']},
          realCopyCount: {validator: checkMissValue, required: true, message: "必填字段", trigger: ['blur']},
        },
        matsTableData: []
      },
      coreItems: [], // 并行推进事项列表
      parallelItems: [], // 并联推进事项
      isSelItem: 0, // 事项列表是否展示复选框 0否 1是
      addEditManModalTitle: '新增联系人',
      addEditManModalFlag: 'edit',
      addEditManModalShow: false, // 是否显示新增编辑联系人窗口
      addEditManform: {},  // 新增编辑联系人信息
      addEditManPerform: {},  // 新增编辑联系人信息
      addLinkManRules: { // 新增编辑联系人校验
        linkmanName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        linkmanCertNo: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        linkmanMobilePhone: [
          {required: true, validator: checkPhoneNum, trigger: 'blur'},
        ]
      },
      unitInfoIdList: [], // 根据项目id查找相关联的单位列表
      rulesCompanyForm: { // 编辑新增单位信息校验
        unitType: [
          {required: true, message: '请选择单位类型！', trigger: 'change'},
        ],
        applicant: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        idrepresentative: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        linkmanCertNo: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        linkmanMobilePhone: [
          {required: true, validator: checkPhoneNum, trigger: 'blur'},
        ],
        linkmanName: [
          {required: true, message: '请选择联系人！', trigger: ['change', 'blur']},
        ],
        unifiedSocialCreditCode: [
          {required: true, validator: checkUnifiedSocialCreditCode, trigger: ['blur']},
        ]
      },
      rulesApplyPersonForm: { // 编辑新增个人申报主体信息校验
        applyLinkmanName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        applyLinkmanIdCard: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        applyLinkmanTel: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        linkLinkmanName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        linkLinkmanIdCard: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        linkLinkmanTel: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ]
      },
      statusLineList: [], // 主题下阶段类型
      statusLineListActive: 0, // 主题下阶段类型选中状态
      AllFileList: [], // 智能分拣区所选择文件
      fileList: [],
      stoFormId: '',
      submitCommentsMatFlag: '',
      checkAll: true, // 智能分拣区文件是否全选
      checkedSelFlie: [], // 智能分拣区已选文件
      AllFileListId: [], // 已选文件name集合
      matIds: [], // 材料matIdS
      selMatinstId: '', // 已选材料实例Id
      matinstIds: [], // 材料实例Ids
      rootUnitInfoId: '', // 默认第一个单位id 有经办为第一个经办单位id
      rootLinkmanInfoId: '', // 默认第一个id 有经办为第一个经办id
      rootApplyLinkmanId: '', // 申请联系人ID
      statusActiveIndex: -1, // 申报阶段选中阶段index
      sameAsApplyLink: false, // 联系人信息是否与经办人一致
      projNameSelect: [], // 下拉选择项目名数组
      searchProjfilterText: '', // 项目树窗口过滤查询
      searchOrgfilterText: '',
      applyPersonFrom: {
        applyLinkmanId: '',
        linkLinkmanId: '',
        applyLinkmanName: '',
        applyLinkmanIdCard: '',
        applyLinkmanTel: '',
        applyLinkmanEmail: '',
        linkLinkmanName: '',
        linkLinkmanIdCard: '',
        linkLinkmanTel: '',
        linkLinkmanEmail: '',
      },  // 申报主体为个人时个人信息
      applySubjectType: 0,  // 申办主体信息类型 0个人 1企业 2非企业
      projSelect: false, // 项目类型建设性质是否可下拉选择
      activeTab: 0,  // 纵向导航active状态index
      showVerLen: 2, // 显示左侧纵向导航栏的长度
      showMoreProjInfo: false, // 查询项目后展示更多信息
      loadingFile: false, // 文件上传loading
      loadingFileWin: false, // 窗口文件上传loading
      dialogHtml: '', // 样本弹窗html
      showMatFilesDialogShow: false, // 是否展示样本弹窗
      showUploadWindowFlag: false, // 是否展示文件上传窗口
      showUploadWindowBtn: true, // 是否展示文件上传窗口操作按钮
      showUploadWindowTitle: '材料附件', // 文件上传窗口header
      getFileListWindowFlag: false, // 是否展示导入电子材料弹窗
      fileSelectionList: [], // 所选电子件
      selMatRowData: {}, // 所选择的材料信息
      uploadMatinstId: '', // 上传返回材料实例
      
      treeRightBtnList: false, // 项目树是否展示右键操作按钮
      treeBtnTop: '',
      treeBtnLeft: '',
      childProjText: '', // 子项目工程备注
      childProjName: '', // 子项目工程名称
      parentProjInfoId: '', // 父工程id
      loadingLinkMan: false,
      applyinstId: '', // 申请实例id
      stateIds: [], // 情形id集合
      getPaperAll: false,
      getCopyAll: false,
      provinceList: [], // 所有的省份信息
      cityList: [], // 所有市区信息
      countyList: [], // 所有地区信息
      smsInfoId: '', // 领证人id
      showFileListKey: [], // 展示材料下文件列表
      rulesResultForm: { // 办证结果取件方式信息校验
        addresseeName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseePhone: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeIdcard: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        receiveMode: [
          {required: true, message: "必选", trigger: 'change'},
        ],
      },
      rulesResultForm1: {
        addresseeName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseePhone: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeIdcard: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        receiveMode: [
          {required: true, message: "必选", trigger: 'change'},
        ],
        smsType: [
          {required: true, message: "必选", trigger: 'change'},
        ],
        addresseeCounty: [
          {required: true, validator: checkMissValue, trigger: 'change'},
        ],
        addresseeCity: [
          {required: true, validator: checkMissValue, trigger: 'change'},
        ],
        addresseeProvince: [
          {required: true, validator: checkMissValue, trigger: 'chenge'},
        ],
        addresseeAddr: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ]
      },
      jingbanFlag: false, // 是否为经办
      stateList: [], // 情形选中列表
      stateSelVal: {},
      checked: [],
      fileData: [], // 一键分拣参数列表
      fileWinData: [], // 上传窗口上传参数列表
      uploadLogo: '',
      addChildProjShow: false, // 新增子项目input
      shareFileList: [],
      uploadTableData: [],
      /****** 审批意见字段 ******/
      submitCommentsFlag: false, // 是否展示收件意见框
      submitCommentsFlag1: false, // 展示常用意见管理框
      submitCommentsFlag2: false, // 展示编辑常用意见框
      commentsList: [], // 常用意见列表
      selComment: '', // 已选常用意见
      comments: '', // 已填审批意见
      commentTitle: '收件意见对话框',//收件意见框标题
      addCommentModalShow: false,//是否显示新增意见弹框
      manageCommentModalShow: false,//是否显示常用意见管理弹窗
      editCommentModalShow: false,//编辑意见弹框
      
      commentForm: {selectComment: '', inputComment: ''},
      commentFormRule: {
        inputComment: [
          {required: true, message: '请输入意见', trigger: 'change'},
          /*{ min: 1, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }*/
        ],
      },//填写审批意见校验
      editCommentForm: {opinionId: '', opinionContent: '', userId: '', sortNo: ''},//编辑的意见对象
      editCommentFormRule: {
        opinionContent: [
          {required: true, message: '请输入意见', trigger: 'change'},
          /*{ min: 1, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }*/
        ],
        sortNo: [
          {required: true, message: '请输入数量', trigger: 'blur'}
        ],
      },//意见编辑框保存校验
      multipleOptionSelection: [],//选择的意见
      /**********不收件参数********/
      refusedRecepitForm: {
        receiveId: '',
        itemVerId: '',
        applyinstSource: 'win',
        isSeriesApprove: '1',
        projInfoId: '',
        projName: '',
        receiptType: '3',
        receiveMemo: '',
        receiveCertNo: '',
        receiveUserName: '',
        receiveUserMobile: '',
        receiveMode: '1',//领取方式
        serviceAddress: '政务服务大厅',
        issueUserMobile: '',
        linkmanInfoId: '',
        iteminstState: '3'
      },//不收件参数
      refusedRecepitFormRule: {
        receiveMemo: [{required: true, message: '请输入意见', trigger: 'blur'}],
        issueUserMobile: [{validator: checkPhoneNum, trigger: 'blur'}]
      },//不收件参数校验
      refusedRecepitModalShow: false,//不收件弹窗
      selectMat: '',//选择的材料
      buttonStyle: '',//点击的按钮 0 发起申报；1 打印回执 。。。。。
      
      
      showMatList: false,//收件意见弹框是否显示材料列表
      //selectedStates:[],//选择的情形，可能有单选，或者多选{stateId:'',parentStateId:''}
      receiveList: [],//回执列表
      receiveModalShow: false,//回执弹窗控制
      //fit: ['fill', 'contain', 'cover', 'none', 'scale-down'],
      branchOrg: '', // 分局承办id
      uploadPercentage: 0, // 进度条百分比
      progressIntervalStop: false, // 定时器
      progressDialogVisible: false,
      searchFileListfilter: '',
      selMatinstIds: [],
      showMatTableExpand: false, // 材料列表是否展示expand
      projBascInfoShowFromRules: {  // 基本信息校验
        projName: [
          {required: true, message: '请输入项目/工程名称！', trigger: 'change'},
        ],
        localCode: [
          {required: true, message: '请输入投资平台登记的项目代码！', trigger: ['change', 'blur']},
        ],
        gcbm: [
          {required: true, message: '请输入工程代码！', trigger: ['change']},
        ],
        regionalism: [
          {required: true, message: '请选择审批行政区划！', trigger: ['change']},
        ],
        projType: [
          {required: true, message: '请选择立项类型！', trigger: ['change']},
        ],
        projectAddress: [
          {required: true, message: '请输入建设地点！', trigger: ['blur']},
        ],
        financialSource: [
          {required: true, message: '请选择资金来源！', trigger: ['change']},
        ],
        landSource: [
          {required: true, message: '请选择土地来源！', trigger: ['change']},
        ],
        isDesignSolution: [
          {required: true, message: '请选择土地是否带设计方案！', trigger: ['change']},
        ],
        isAreaEstimate: [
          {required: true, message: '请选择是否完成区域评估！', trigger: ['change']},
        ],
        projNature: [
          {required: true, message: '请选择建设性质！', trigger: ['change']},
        ],
        investType: [
          {required: true, message: '请选择投资类型！', trigger: ['change']},
        ],
        gbCodeYear: [
          {required: true, message: '请输入国标行业代码发布年代！', trigger: ['change']},
        ],
        theIndustry: [
          {required: true, message: '请选择国标行业！', trigger: ['blur', 'change']},
        ],
        nstartTime: [
          {required: true, message: '请选择拟开工时间！', trigger: ['change']},
        ],
        endTime: [
          {required: true, message: '请选择拟建成时间！', trigger: ['change']},
        ],
        scaleContent: [
          {required: true, message: '请输入建设规模及内容！', trigger: ['change']},
        ],
        xmYdmj: [
          {validator: checkNumFloat, trigger: ['blur']},
          {required: true, message: '请填写用地面积！', trigger: ['change']}
        ],
        xzydmj: [
          {validator: checkNumFloat, trigger: ['blur']},
        ],
        buildAreaSum: [
          {validator: checkNumFloat, trigger: ['blur']},
          {required: true, message: '请填写建筑面积！', trigger: ['change']}
        ],
        aboveGround: [
          {validator: checkNumFloat, trigger: ['blur']},
        ],
        underGround: [
          {validator: checkNumFloat, trigger: ['blur']},
        ],
        investSum: [
          {validator: checkNumFloat, trigger: ['blur']},
          {required: true, message: '请输入总投资！', trigger: ['change']}
        ]
      },
      showMoreBaseInfo: false,
      projLevelList: [],//项目级别
      districtList: [],//行政区划
      tzlxList: [], // 投资类型
      zjlyList: [], // 资源来源
      gbhyList: [], // 行业类别
      tdlyList: [], // 土地来源
      jsxzList: [], // 建设性质
      gcflList: [], // 工程分类
      xmdwlxList: [], // 项目单位类型
      projInfoList: [], // 查询项目列表
      projThemeIdList: [], // 项目类型
      loadingThemeIdList: false, // 查询项目类型列表
      loadingProjInfo: false, // 查询项目列表
      receiveItemActive: '', // 回执列表li active状态
      receiveActive: '', // 回执列表 div active状态
      pdfSrc: '',
      showUnitMore: [], // 展示更多单位信息
      filePreviewCount: 0, // 查询预览是否成功次数
      projInfoByKeyword: '', // 模糊查询项目Keyword
      sloading: false,//补全遮罩
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
      // 是否在选择材料内
      inSeletcedMaterialPandel: true,
      // 已提价材料列表
      submittedMats: [],
      // 待补全材料列表
      matCorrectDtos: [],
      supplementFormRules: {
        correctDueDays: [{required: true, trigger: blur, message: '请输入补全办理时限'}]
      },
      // isMend:true,//是否可以材料补全
      unitIdList: [], // 已查是否黑名单建设单位id集合
      isBlack: false, // 是否黑名单
      loseCreditCount: 0, // 失信条数
      creditTypeList: [], // 信用类型数据
      creditDiaVisible: false, // 是否展示信用弹窗
      creditDiaLoading: false, // 是否展示信用弹窗loading
      attIsRequireFlag: true, // 电子件已通过
      gbhySelectData: [], // 国标行业下拉选项数据
      gbhyProp: {
        children: 'children',
        label: 'itemName'
      }, // 树配置信息，节点属性以及显示文案的属性
      gbhyShowMsg: '', // 国标行业选中数据的展示
      isShowGbhy: false, // 是否显示国标行业tree模块
      projUnitLinkmanType: [], // 人员类型
      loadingUnitLinkMan: false,
      unitLinkManOptions: [],
      stageId: '', // 阶段id
      isParallel: '', // 是否并行事项
      selThemeActive: -1, // 选中主题index
      themeInfoList: [], // 可选主题列表
      selThemeShow: false, // 是否显示选择主题
      selTheme: {}, // 已选主题
      themeId: '', // 主题id
      themeName: '', // 所选主题name
      beforeCheckShowMore: false,
      matCodes: [], // 材料code集合
      showCertWindowFlag: false, // 是否展示证照库窗口
      certMatTableData: [], // 证照库列表
      certMatAllData: {}, // 材料证照信息
      searchInline: {
        identityNumberType: '0',
        showNumberType: true,
      }, // 证照库条件查询
      allApplySubjectInfo: [], // 证照库条件查询
      selApplySubject: {}, // 选中的建设单位 申报主体
      matFormDialogVisible: false, // 是否展示材料一张表单
      matformNameTitle: '材料表单',
      IsJustApplyinst: 0, //
      preItemCheckPassed: true, // 前置检测是否通过
      preItemCheckkMsg: '', // 前置检测失败提示
      // 一张表单
      oneFormInfo: [],
      oneFormDialogVisible: false,
      oneFormInputFlag: false, // 一张表单是否填写
      showTempSaveBtn: false,
      themeVerId: '',
      approveOrgId: '',
      isTempSavePage: false,
      oneformActiveName: '',
      tempSaveProjName: '',
      forminstVos: [],
      formAllNum: 0,
      formFilledNum: 0,
      formUnFillNum: 0,
      oneFormOpened: false,
      needCallOneFormCb: false,
      isGreenWay: false, // 是否设置为绿色通道
    }
  },
  mounted: function () {
    var _that = this;
    var count = 4;
    _that.loading = true;
    _that.itemVerId = itemVerId;
    _that.refusedRecepitForm.itemVerId = itemVerId;
    _that.getItemInfo(callback);
    _that.getProjTypeNature(
        'PROJ_UNIT_LINKMAN_TYPE,XM_DWLX,XM_PROJECT_STEP,XM_PROJECT_LEVEL,XM_TZLX,XM_ZJLY,XM_GBHY,XM_TDLY,XM_NATURE,XM_GCFL',
        callback
    );
    _that.getDistrictList(callback);  // 获取行政区划
    _that.getGbhy(callback);
    //_that.queryProvince();
    window.addEventListener('scroll', _that.handleScroll);
    window.addEventListener('resize', function (ev) {
      _that.curWidth = (document.documentElement.clientWidth || document.body.clientWidth);
    });
    _that.$nextTick(function () {
      this.$refs.searchProjInfo.$el.querySelector('input').click()
    });
    
    function callback() {
      --count == 0 && _that.showTempData();
    }
  },
  watch: {
    searchProjfilterText: function (val) {
      this.$refs.projTree.filter(val);
    },
    searchOrgfilterText: function (val) {
      this.$refs.orgTree.filter(val);
    },
    searchFileListfilter: function (val) {
      this.searchShareFileList(this.selMatRowData)
    }
  },
  methods: {
    relateFormCallback: function(obj){
      var vm = this;
      request('', {
        url: ctx + 'rest/stage/bind/forminst',
        type: 'POST',
        ContentType: 'application/json',
        data: JSON.stringify({
          applyinstId: vm.applyinstId,
          forminstId: obj.actStoForminst.stoForminstId,
        }),
      }, function (res) {
        if (res.success) {
          vm.oneFormInfo.forEach(function(u){
            if (u.formId == vm.oneformActiveName) {
              u.createTime = new Date();
              if (!u.isFilled) {
                u.isFilled = true;
                vm.formFilledNum++;
                vm.formUnFillNum--;
              }
            }
          });
        } else {
          vm.$message.error(res.message || '表单关联失败');
        }
      }, function(){
        vm.$message.error('表单关联失败');
      });
    },
    // 回显
    showTempData: function () {
      var vm = this;
      vm.loading = false;
      var applyinstId = __STATIC.getUrlParam('applyinstId');
      if (!applyinstId || applyinstId == '') return null;
      vm.isTempSavePage = true;
      vm.applyinstId = applyinstId;
      vm.loading = true;
      request('', {
        url: ctx + 'rest/apply/unstash',
        type: 'get',
        data: {applyinstId: applyinstId},
      }, function (res) {
        vm.loading = false;
        if (res.success) {
          vm.projInfoId = res.content.projInfoId;
          vm.themeId = res.content.themeId;
          vm.themeVerId = res.content.themeVerId;
          vm.stageId = res.content.stageId;
          vm.stateIds = res.content.stateIds;
          vm.approveOrgId = res.content.approveOrgId;
          vm.forminstVos = res.content.forminstVos;
          vm.isGreenWay = res.content.aeaHiApplyinst.isGreenWay=='1'?true:false;
          vm.beforeCheck();
        } else {
          vm.$message.error(res.message || '获取暂存数据失败');
        }
      }, function () {
        vm.loading = false;
        vm.$message.error('获取暂存数据失败');
      });
    },
    // 暂时保存
    tempSave: function () {
      this.buttonStyle = 'tempSave';
      this.startSingleApprove();
    },
    // 渲染表单回调
    oneFormCallback: function(){
      var vm = this;
      vm.$nextTick(function(){
        vm.oneFormInfo.forEach(function(u, index){
          if (u.smartForm){
            $('#smartFormBox_' + index).html(u.html);
          }
        });
      });
    },
    // 打开一张表单弹窗
    openOneFormDialog: function (row) {
      var vm = this;
      if (row) {
        vm.oneformActiveName = row.formId;
      } else {
        vm.oneformActiveName = oneFormInfo[0].formId;
      }
      vm.oneFormDialogVisible = true;
      if (vm.oneFormOpened) return null;
      vm.oneFormOpened = true;
      if (vm.applyinstId == '') {
        vm.showOneFormDialog1();
        vm.needCallOneFormCb = true;
      } else {
        vm.oneFormCallback();
      }
      // vm.oneFormOpened = true;
      // vm.$nextTick(function(){
      //   vm.getOneFormData();
      //   vm.oneFormInfo.forEach(function(u, index){
      //     if (u.smartForm){
      //       $('#smartFormBox_' + index).html(u.html);
      //     }
      //   });
      // });
      // vm.getOneFormData();
    },
    // 得到一张表单信息
    getOneFormData: function () {
      var vm = this;
      request('', {
        url: ctx + 'rest/oneform/common/getListForm4ItemOneForm',
        // url: ctx + 'rest/stage/item/part/forms',
        type: 'get',
        data: {
          applyinstId: vm.applyinstId,
          projInfoId: vm.projInfoId,
          itemId: vm.itemVerId,
          showBasicButton: true,
          includePlatformResource: false,
          // itemVerIds: vm.itemVerId,
        },
      }, function (res) {
        if (res.success) {
          res.content.forEach(function (u, index) {
            if (u.smartForm && vm.applyinstId!='') {
              getHtml(u, index);
            }
            u.isFilled = false;
          });
          vm.oneFormInfo = res.content;
          vm.formAllNum = vm.oneFormInfo.length;
          vm.formUnFillNum = vm.oneFormInfo.length;
          // 回显表单是否已填
          if (vm.isTempSavePage) {
            vm.forminstVos.forEach(function(u,i){
              vm.oneFormInfo.forEach(function(uu, ii) {
                if (u.stoFormId == uu.formId){
                  uu.isFilled = true;
                  uu.createTime = u.modifyTime||u.createTime;
                  vm.formFilledNum++;
                  vm.formUnFillNum--;
                }
              });
            });
          }
        } else {
          vm.$message.error(res.content || '获取一张表单信息失败');
        }
      }, function () {
        vm.$message.error('获取一张表单信息失败');
      });
      
      function getHtml(data, index) {
        request('', {
          url: ctx + data.formUrl,
          type: 'get',
        }, function (res) {
          if (res.success) {
            vm.$nextTick(function(){
              $('#smartFormBox_' + index).html(res.content);
            });
            vm.oneFormInfo[index].html = res.content;
            // $('#smartFormBox_' + index).html(res.content);
          } else {
            // vm.$message.error('获取智能表单数据失败');
          }
        }, function () {
          // vm.$message.error('获取智能表单数据失败');
        })
      }
    },
    // 获取可共享材料列表
    getShareMatsList: function (matData) {
      var _that = this, _matCode = '';
      var matChild = _that.selMatRowData&&_that.selMatRowData.matChild ? _that.selMatRowData.matChild : [];
      var matChildIds = [];
      if (matChild.length > 0) {
        matChild.map(function (item) {
          if (matChildIds.indexOf(item.fileId) < 0) {
            matChildIds.push(item.fileId);
          }
        })
      }
      if (_that.matCodes.length > 0) {
        if (matData && matData.matCode) {
          _matCode = matData.matCode;
        } else {
          _matCode = _that.matCodes.join(',');
        }
        request('', {
          url: ctx + 'rest/mats/getHistoryAttMatList',
          // url: ctx + 'apply/js/fileList.json',
          type: 'post',
          data: {projInfoId: _that.projInfoId, matCode: _matCode}
        }, function (result) {
          if (result.success) {
            if (matData) {
              _that.uploadTableData = result.content[_matCode].FileList;
              _that.uploadTableData.map(function (fileListItem) {
                if (matChildIds.indexOf(fileListItem.fileId) > -1) {
                  _that.$refs['uploadTableData'].toggleRowSelection(fileListItem, true);
                } else {
                  _that.$refs['uploadTableData'].toggleRowSelection(fileListItem, false);
                }
                _that.$nextTick(function () {
                  if (matChildIds.indexOf(fileListItem.fileId) > -1) {
                    _that.$refs['uploadTableData'].toggleRowSelection(fileListItem, true);
                  } else {
                    _that.$refs['uploadTableData'].toggleRowSelection(fileListItem, false);
                  }
                })
              });
            } else {
              _that.model.matsTableData.map(function (matItem) {
                if (result.content && result.content[matItem.matCode] && result.content[matItem.matCode].FileList) {
                  matItem.matinstId = result.content[matItem.matCode].matinstId;
                  matItem.matChild = result.content[matItem.matCode].FileList;
                  _that.showMatTableExpand = true;
                  if (_that.showFileListKey.indexOf(matItem.matId) < 0) {
                    _that.showFileListKey.push(matItem.matId)
                  }
                }
              });
            }
          }
        }, function (msg) {
        })
      }
    },
    // 选择并保存主题
    chooseTheme: function (data, index) {
      var _that = this;
      _that.projBascInfoShow.projApplyType = data.themeName;
      _that.themeName = data.themeName;
      _that.selThemeActive = index;
      _that.themeId = data.themeId;
      _that.isParallel = data.isParallel;
      _that.stageId = data.stageId;
      _that.selTheme = data;
      _that.linkQuery();
      _that.selThemeDialogShow = false;
    },
    closeGbhyTree: function () {
      this.isShowGbhy = false;
    },
    showSelThemeDia: function () {
      if (this.themeInfoList == null || (this.themeInfoList && this.themeInfoList.length == 0)) {
        alertMsg('', '申报的项目绑定的主题或者阶段下没有配置对应的事项', '关闭', 'error', true);
        return false;
      } else {
        this.selThemeDialogShow = true
      }
    },
    // 前置条件判断
    beforeCheck: function () {
      var _that = this;
      if (_that.projInfoId) {
        request('', {
          url: ctx + 'rest/apply/series/check',
          type: 'get',
          data: {
            projInfoId: _that.projInfoId,
            itemVerId: _that.itemVerId
          },
        }, function (result) {
          if (result.success) {
            _that.stageId = result.content.stageId;
            _that.isParallel = result.content.isParallel;
            _that.themeInfoList = result.content.themeStageList;
            _that.showTempSaveBtn = true;
            if (result.content.stageId) {
              _that.linkQuery();
              _that.selTheme = {};
              _that.beforeCheckShowMore = true;
              _that.selThemeShow = false;
            } else {
              _that.showMoreProjInfo = false;
              _that.beforeCheckShowMore = false;
              _that.selThemeShow = true;
            }
            
          } else {
            alertMsg('', '申报的项目没有绑定主题或者阶段下没有配置对应的并行事项', '关闭', 'error', true);
          }
        }, function (msg) {
        })
      } else {
        if (!_that.searchKeyword) {
          _that.$message({
            message: '请输入项目（工程）的代码或名称！',
            type: 'error'
          });
        } else {
          _that.$message({
            message: '请先选择要查询的项目！',
            type: 'error'
          });
        }
      }
    },
    getGbhy: function (cb) {
      var _that = this;
      request('', {
        url: ctx + 'bsc/dic/code/getItemTreeByTypeId.do',
        type: 'get',
        data: {typeId: 'fadff496-cde1-4c72-90b8-766744b18cb9'},
      }, function (result) {
        if (result) {
          var arr = result;
          
          if (arr.length) {
            for (var i = 0; i < arr.length; i++) {
              arr[i].disabled = true;
            }
          }
          _that.gbhySelectData = arr;
          typeof cb == 'function' && cb();
        }
      }, function (msg) {
      })
    },
    handleCheckChange: function (data, checked, indeterminate) {
      // console.log(data, checked, indeterminate);
      var arr = this.$refs.gbhy.getCheckedNodes(true);
      var str = [];
      var ids = [];
      for (var i = 0; i < arr.length; i++) {
        str.push(arr[i].itemName);
        ids.push(arr[i].itemCode);
      }
      
      this.gbhyShowMsg = str.join(',');
      this.projBascInfoShow.theIndustry = ids.join(',');
    },
    // 选择切换申办主体单位
    selUnitInfo: function (val, flag, index) { // val选中单位信息 flag 单位类型（jingban,jianshe） index单位索引
      if (flag == 'jingban') {
        this.agentUnits[index] = val;
      } else {
        this.jiansheFrom[index] = val;
      }
      this.unitOrUserIsBlack(val);
    },
    // 展示信用记录弹窗
    creditDiaVisibleShow: function (data) {
      this.creditTypeList = data.creditTypeList;
      this.loseCreditCount = data.loseCreditCount;
      this.creditDiaVisible = true;
      this.isBlack = data.isBlack;
    },
    // 判断是否为企业或个人是否黑名单
    unitOrUserIsBlack: function (reqData) {
      var _that = this, _bizId = '', _bizType = '';
      if (_that.applySubjectType == 0) {
        _bizId = reqData.applyLinkmanId;
        _bizType = 'l';
      } else {
        _bizId = reqData.unitInfoId;
        _bizType = 'u';
      }
      request('', {
        url: ctx + 'aea/credit/redblack/listPersonOrUnitBlackByBizId',
        type: 'get',
        data: {bizId: _bizId, bizType: _bizType},
      }, function (result) {
        if (result.success) {
          _that.unitIdList.push(_bizId);
          if (result.content && result.content.length > 0) {
            if (typeof reqData.isBlack == "undefined") {
              Vue.set(reqData, 'isBlack', true);
            } else {
              reqData.isBlack = true;
            }
          } else {
            if (typeof reqData.isBlack == "undefined") {
              Vue.set(reqData, 'isBlack', false);
            } else {
              reqData.isBlack = false;
            }
          }
          _that.listCreditSummaryDetailByBizId(reqData, _bizId, _bizType);
        }
      }, function (msg) {
      })
      
    },
    // 通过企业或者联系人id获取汇总与详情信息数据
    listCreditSummaryDetailByBizId: function (reqData, _bizId, _bizType) {
      // creditDiaLoading
      var _that = this;
      _that.creditDiaLoading = true;
      request('', {
        url: ctx + 'aea/credit/redblack/listCreditSummaryDetailByBizId',
        type: 'get',
        data: {bizId: _bizId, bizType: _bizType},
      }, function (result) {
        _that.creditDiaLoading = false;
        if (result.success && result.content) {
          if (typeof reqData.creditTypeList == "undefined") {
            Vue.set(reqData, 'creditTypeList', result.content);
          } else {
            reqData.creditTypeList = result.content;
          }
          if (typeof reqData.loseCreditCount == "undefined") {
            Vue.set(reqData, 'loseCreditCount', 0);
          } else {
            reqData.loseCreditCount = 0;
          }
          reqData.creditTypeList.map(function (item) {
            if (item.itemCode == 'bad' && item.summaries.length > 0) {
              reqData.loseCreditCount = item.summaries.length;
            }
            console.log(reqData.loseCreditCount)
            item.summaries.map(function (itemSum) {
              if (typeof itemSum.isOpen == 'undefined') {
                Vue.set(itemSum, 'isOpen', false);
              } else {
                itemSum.isOpen = false;
              }
              
            })
          });
          if (reqData.isBlack == 1) {
            _that.creditDiaVisibleShow(reqData);
          }
          ;
        }
      }, function (msg) {
        _that.creditDiaLoading = false;
      })
    },
    // 生成项目编码
    getLocalcode: function(){
      var _that = this;
      if(!_that.projBascInfoShow.projName){
        _that.$message({
          message: '请输入项目名称！',
          type: 'error'
        });
        return false;
      }
      request('', {
        url: ctx + 'rest/project/save/zbm',
        type: 'post',
        data: {projName: _that.projBascInfoShow.projName},
      }, function (result) {
        if (result.success) {
          _that.searchKeyword = result.content.localCode;
          _that.localCode = result.content.localCode;
          _that.showMoreProjInfo = true;
          _that.projName = _that.projBascInfoShow.projName;
          _that.showVerLen = _that.verticalTabData.length;
          _that.projBascInfoShow = result.content; // 项目主要信息
          _that.getProjThemeIdList();
          _that.themeId = result.content.themeId;
          _that.applySubjectType = Number(result.content.applySubjectType); // 申办主体类型
          _that.projInfoId = result.content.projInfoId;
          if (!_that.projBascInfoShow.isAreaEstimate) _that.projBascInfoShow.isAreaEstimate = '0';
          if (!_that.projBascInfoShow.isDesignSolution) _that.projBascInfoShow.isDesignSolution = '0';
          if (!_that.projBascInfoShow.gbCodeYear) _that.projBascInfoShow.gbCodeYear = '2017';
          if (!!_that.projBascInfoShow.projectAddress) _that.projBascInfoShow.projectAddress = _that.projBascInfoShow.projectAddress.split(',');
          _that.$nextTick(function () {
            if (!!_that.projBascInfoShow.theIndustry) _that.$refs.gbhy.setCheckedKeys(_that.projBascInfoShow.theIndustry.split(','));
          })
          if(result.content.personalApplicant){
            _that.applyPersonFrom = result.content.personalApplicant; // 个人申报主体信息
          }
          if(result.content.buildUnits){
            _that.buildUnits = result.content.buildUnits; // 企业申报主体信息
          }
          if(result.content.otherUnits){
            _that.otherUnits = result.content.otherUnits; // 个人申报主体信息
          }
          if(result.content.agentUnits){ // 存在经办单位
            result.content.agentUnits.map(function(item){
              var dataType = {
                linkmanInfoId: '',
                linkmanType: '',
                linkmanName: ''
              }
              if(item.linkmanTypes&&item.linkmanTypes.length==0){
                item.linkmanTypes.push(dataType)
              }
            })
            _that.agentUnits = result.content.agentUnits;  // 经办单位信息
            if(result.content.agentUnits.length>0){
              _that.agentChecked = true; // 经办勾选
            }
          }else {
            _that.agentUnits = [];
            _that.agentChecked = false;
          }
          _that.setJiansheFrom();
          _that.linkQuerySucc = true;
          // 判断项目是否无编码申报
          if((result.content.localCode.slice(0,3) == 'ZBM')){
            _that.projSelect = false;
            _that.approveNumClazz = false; // 不显示备案文号
          }else {
            _that.projSelect = true;
            _that.approveNumClazz = true; // 显示备案文号
          }
          _that.getStatusStateMats('', '', 'ROOT', '', '', true); // 获取阶段
          _that.$nextTick(function () {
            if ((!_that.applyinstId || _that.applyinstId == '')&&vm.needCallOneFormCb) {
              // _that.showOneFormDialog1();
              vm.oneFormCallback();
            } else {
              _that.getOneFormData();
            }
          });
        }
        _that.loading = false;
      }, function (msg) {})
    },
    getItemInfo: function (cb) {
      var _that = this;
      request('', {
        url: ctx + 'rest/item/detail/' + _that.itemVerId,
        type: 'get',
      }, function (data) {
        _that.loading = false;
        if (data.success) {
          typeof cb == 'function' && cb();
          if (data.content) {
            _that.itemBasicInfo = data.content;
          }
        }
      }, function (msg) {
        //_that.showMoreProjInfo = false;
        //_that.showVerLen = 2;
        _that.loading = false;
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    targetCollapse: function (target, ind) { // 纵向导航点击事件
      this.activeTab = ind;
      $(document).scrollTop($('#' + target).offset().top)
    },
    handleScroll: function () { // 屏幕滚动纵向导航相应获取焦点
      var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
      var ele = $('.el-collapse-item');
      var eleLen = ele.length;
      var _that = this;
      for (var i = 0; i < eleLen; i++) {
        if (scrollTop >= ($(ele[i]).offset().top - 60)) {
          _that.activeTab = i
        }
      }
    },
    // keyword模糊查询项目信息
    searchProjInfoByKeyword: function (queryString) {
      var _that = this;
      _that.projInfoId = '';
      _that.projInfoByKeyword = queryString;
      if (typeof (queryString) != 'undefined') queryString = queryString.trim();
      if (queryString != '') {
        _that.loadingProjInfo = true;
        request('', {
          url: ctx + 'rest/project/info',
          type: 'get',
          data: {"keyword": queryString},
        }, function (result) {
          if (result.content) {
            _that.projInfoList = result.content;
            _that.loadingProjInfo = false;
          }
        }, function (msg) {
          _that.projInfoList = [];
          _that.loadingProjInfo = false;
        })
      } else {
        _that.projInfoList = [];
      }
    },
    // 项目名称过滤
    createFilter: function (queryString) {
      return function (projNameSelect) {
        return (projNameSelect.value.toLowerCase());
      };
    },
    // 选择要查的项目
    projNameSel: function (item) {
      this.projInfoId = item.projInfoId;
      this.projName = item.projName;
      this.beforeCheck();
    },
    // 清空数据
    clearSearchData: function () {
      this.stateList = [];
      this.model.matsTableData = [];
    },
    
    // 获取项目详细信息
    searchProjAllInfo: function () {
      var _that = this;
      _that.loading = true;
      _that.matCodes = [];// 材料code集合
      _that.clearSearchData();
      request('', {
        url: ctx + 'rest/project/one/' + _that.projInfoId,
        type: 'get',
      }, function (data) {
        if (data.success) {
          if (data.content) {
            var result = data.content;
            _that.showMoreProjInfo = true;
            _that.showVerLen = _that.verticalTabData.length;
            _that.projBascInfoShow = result; // 项目主要信息
            if (_that.isTempSavePage){
              _that.tempSaveProjName = _that.projBascInfoShow.projName;
            }
            if (_that.themeId != '') {
              _that.projBascInfoShow.themeId = _that.themeId;
              _that.projBascInfoShow.projApplyType = _that.themeName;
            }
            _that.getProjThemeIdList();
            _that.applySubjectType = Number(result.applySubjectType); // 申办主体类型
            
            if (!_that.projBascInfoShow.isAreaEstimate) _that.projBascInfoShow.isAreaEstimate = '0';
            if (!_that.projBascInfoShow.isDesignSolution) _that.projBascInfoShow.isDesignSolution = '0';
            if (!_that.projBascInfoShow.gbCodeYear) _that.projBascInfoShow.gbCodeYear = '2017';
            if (!!_that.projBascInfoShow.projectAddress) _that.projBascInfoShow.projectAddress = _that.projBascInfoShow.projectAddress.split(',');
            
            _that.$nextTick(function () {
              if (!!_that.projBascInfoShow.theIndustry) _that.$refs.gbhy.setCheckedKeys(_that.projBascInfoShow.theIndustry.split(','));
            })
            
            if (result.personalApplicant) {
              _that.applyPersonFrom = result.personalApplicant; // 个人申报主体信息
            }
            if (result.buildUnits) {
              _that.buildUnits = result.buildUnits; // 企业申报主体信息
            }
            if (result.otherUnits) {
              _that.otherUnits = result.otherUnits; // 个人申报主体信息
            }
            if (result.agentUnits) { // 存在经办单位
              result.agentUnits.map(function (item) {
                var dataType = {
                  linkmanInfoId: '',
                  linkmanType: '',
                  linkmanName: ''
                }
                if (item.linkmanTypes.length == 0) {
                  item.linkmanTypes.push(dataType)
                }
              })
              _that.agentUnits = result.agentUnits;  // 经办单位信息
              if (result.agentUnits.length > 0) {
                _that.agentChecked = true; // 经办勾选
              }
            } else {
              _that.agentUnits = [];
              _that.agentChecked = false;
            }
            _that.setJiansheFrom();
            _that.linkQuerySucc = true;
            // 判断项目是否无编码申报
            if ((result.localCode.slice(0, 3) == 'ZBM')) {
              _that.projSelect = false;
              _that.approveNumClazz = false; // 不显示备案文号
            } else {
              _that.projSelect = true;
              _that.approveNumClazz = true; // 显示备案文号
            }
            _that.getStatusStateMats('', '', 'ROOT', '', '', true); // 获取阶段
          }
          _that.$nextTick(function () {
            if ((!_that.applyinstId || _that.applyinstId == '')&&vm.needCallOneFormCb) {
              // _that.showOneFormDialog1();
              vm.oneFormCallback();
            } else {
              _that.getOneFormData();
            }
          });
          _that.loading = false;
        } else {
          _that.showMoreProjInfo = false;
          _that.showVerLen = 2;
          _that.$message({
            message: data.message ? data.message : '没有查到该项目信息！',
            type: 'error'
          });
          _that.loading = false;
        }
      }, function (msg) {
        _that.showMoreProjInfo = false;
        _that.showVerLen = 2;
        _that.loading = false;
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    // 联网查询
    linkQuery: function () {
      var _that = this;
      // _that.beforeCheckShowMore = true;
      _that.showUnitMore = []; // 清空企业展示更多信息列表
      if (_that.projInfoId) {
        _that.searchProjAllInfo();
        var param = {
          projInfoId: _that.projInfoId,
          itemVerIds: _that.itemVerId,
        };
        if (_that.itemVerId) {
          request('', { // 判断并行实施事项是否可选
            url: ctx + 'rest/check/itemFrontCheck',
            type: 'get',
            ContentType: 'application/json',
            // data: JSON.stringify(param),
            data: param,
          }, function (result) {
            if (result.success) {
              _that.preItemCheckPassed = true;
            } else {
              _that.preItemCheckPassed = false;
              // _that.preItemCheckkMsg = result.message?result.message:'事项前置检测失败'
              confirmMsg('前置事项检测不通过', result.message, function () {
                _that.preItemCheckPassed = true;
              }, function () {
                _that.preItemCheckPassed = false
                return false;
              }, '继续申报', '放弃申报', 'error', true);
            }
          }, function (msg) {
          })
        }
      } else {
        if (!_that.searchKeyword) {
          _that.$message({
            message: '请输入项目（工程）的代码或名称！',
            type: 'error'
          });
        } else {
          _that.$message({
            message: '请先选择要查询的项目！',
            type: 'error'
          });
        }
      }
    },
    
    // 企业非企业申报主体列表信息
    setJiansheFrom: function () {
      var _that = this;
      var dataType = {
        linkmanInfoId: '',
        linkmanType: '',
        linkmanName: ''
      }
      if (this.applySubjectType == 1) {  // 企业申报
        this.buildUnits.map(function (item) {
          if (_that.unitIdList.indexOf(item.unitInfoId) < 0) {
            _that.unitOrUserIsBlack(item);
          }
          if (item.linkmanTypes.length == 0) {
            item.linkmanTypes.push(dataType)
          }
        })
        this.jiansheFrom = this.buildUnits;
        this.isJg = false;
        this.getSentPerInfo(this.buildUnits[0])
      } else if (this.applySubjectType == 2) { // 非企业申报
        this.otherUnits.map(function (item) {
          if (_that.unitIdList.indexOf(item.unitInfoId) < 0) {
            _that.unitOrUserIsBlack(item);
          }
          if (item.linkmanTypes.length == 0) {
            item.linkmanTypes.push(dataType)
          }
        })
        this.jiansheFrom = this.otherUnits;
        this.isJg = true;
        this.getSentPerInfo(this.otherUnits[0]);
      } else { // 个人申报
        var perData = {
          linkmanName: this.applyPersonFrom.applyLinkmanName,
          linkmanId: this.applyPersonFrom.applyLinkmanId,
          linkmanMobilePhone: this.applyPersonFrom.applyLinkmanTel,
          linkmanCertNo: this.applyPersonFrom.applyLinkmanIdCard
        }
        this.getSentPerInfo(perData);
        if (_that.unitIdList.indexOf(this.applyPersonFrom.applyLinkmanId) < 0) {
          this.unitOrUserIsBlack(this.applyPersonFrom)
        }
      }
      this.rootUnitInfoId = '';
      this.rootLinkmanInfoId = '';
      this.rootApplyLinkmanId = '';
      if (this.agentUnits.length > 0) {
        this.rootUnitInfoId = this.agentUnits[0].unitInfoId;
        this.rootLinkmanInfoId = this.agentUnits[0].linkmanId;
      }
      if (this.jiansheFrom.length > 0) {
        this.rootUnitInfoId = this.jiansheFrom[0].unitInfoId;
        this.rootLinkmanInfoId = this.jiansheFrom[0].linkmanId
      }
      if (this.applySubjectType == 0 && this.applyPersonFrom.applyLinkmanId) {
        this.rootApplyLinkmanId = this.applyPersonFrom.applyLinkmanId
      }
      ;
    },
    // 切换申报主体类型
    changeApplySubjectSelect: function (val) { // 申办主题类型切换事件
      this.setJiansheFrom();
      this.showUnitMore = [];
    },
    // 获取项目性质项目类型
    getProjTypeNature: function (code, cb) {
      var _that = this;
      request('', {
        url: ctx + 'rest/dict/code/multi/items/list',
        type: 'get',
        data: {'dicCodeTypeCodes': code}
      }, function (result) {
        if (result.content) {
          _that.projTypeList = result.content.XM_PROJECT_STEP;
          _that.projLevelList = result.content.XM_PROJECT_LEVEL;
          _that.tzlxList = result.content.XM_TZLX;
          _that.zjlyList = result.content.XM_ZJLY;
          _that.gbhyList = result.content.XM_GBHY;
          _that.tdlyList = result.content.XM_TDLY;
          _that.jsxzList = result.content.XM_NATURE;
          _that.gcflList = result.content.XM_GCFL;
          _that.xmdwlxList = result.content.XM_DWLX;
          _that.projUnitLinkmanType = result.content.PROJ_UNIT_LINKMAN_TYPE;
          typeof cb == 'function' && cb();
        }
      }, function (msg) {
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    //查看项目树
    showProjTree: function () {
      var _that = this;
      if (_that.projInfoId == 'undefined' || _that.projInfoId == '' || _that.projInfoId == null) {
        alertMsg('', '请先联网查询项目', '关闭', 'error', true);
      } else {
        request('', {
          url: ctx + 'rest/project/tree/' + _that.projInfoId,
          type: 'get',
        }, function (result) {
          if (result.success) {
            _that.projectTreeInfoModal = true;
            _that.projTree = _that.toTree(result.content);
          } else {
            _that.$message({
              message: '项目工程查询失败！',
              type: 'error'
            });
          }
        }, function (msg) {
          alertMsg('', '网络加载失败！', '关闭', 'error', true);
        })
      }
    },
    // 项目树节点进行筛选时执行的方法
    filterProjNode: function (value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    // 清空筛选项目树条件
    clearProjfilterText: function () {
      this.searchProjfilterText = '';
    },
    // 获取办证结果领取人信息
    getSentPerInfo: function (data) {
      if (data) {
        this.getResultForm.addresseeName = data.linkmanName;
        this.getResultForm.addresseePhone = data.linkmanMobilePhone;
        this.getResultForm.addresseeIdcard = data.linkmanCertNo;
      } else {
        this.getResultForm.addresseeName = '';
        this.getResultForm.addresseePhone = '';
        this.getResultForm.addresseeIdcard = '';
      }
    },
    // 编辑保存项目基本信息
    saveOrUpdateProjFrom: function (isParallel) {
      var _that = this;
      var props = JSON.parse(JSON.stringify(_that.projBascInfoShow));
      props.projectAddress = props.projectAddress ? props.projectAddress.join(',') : '';
      _that.$refs['projBascInfoShowFrom'].validate(function (valid) {
        if (valid) {
          request('', {
            url: ctx + 'rest/project/edit',
            data: props,
            type: 'post'
          }, function (data) {
            if (data.success) {
              _that.$message({
                message: '信息保存成功',
                type: 'success'
              });
              _that.beforeCheckShowMore = true;
            } else {
              _that.$message({
                message: data.message ? data.message : '信息保存失败',
                type: 'error'
              });
            }
          }, function (msg) {
            _that.$message({
              message: msg.message ? msg.message : '服务请求失败',
              type: 'error'
            });
          })
        }
      });
    },
    // 新增单位信息
    addUnitInfoForm: function (isOwner) {
      var _that = this;
      var projInfoIds = '';
      var obj = {
        applicant: '',
        idcard: '',
        idno: '',
        idrepresentative: '',
        idtype: '',
        isOwner: isOwner,
        linkmanInfoId: '',
        linkmanName: '',
        linkmanCertNo: '',
        linkmanId: '',
        linkmanMobilePhone: '',
        linkmanMail: '',
        projInfoId: _that.projInfoId,
        projInfoIds: '',
        linkmanTypes: [{
          linkmanInfoId: '',
          linkmanType: '',
          linkmanName: ''
        }]
      };
      if (isOwner == "1") {  // 新增建设单位
        obj.unitType = '1';
        _that.jiansheFrom.push(obj);
      } else {
        _that.agentUnits.push(obj);
      }
    },
    // 删除单位信息
    delUnitInfoProj: function (data, index, flag) {
      var _that = this;
      var unitProjIdFlag = data.unitInfoId && _that.projInfoId;
      var applyinstId = data.applyinstId;
      var unitInfoId = data.unitInfoId;
      if (applyinstId || !data.applicant) {
        if (flag == 'jingban') {
          _that.agentUnits.splice(index, 1);
        } else {
          _that.jiansheFrom.splice(index, 1);
        }
        return;
      }
      confirmMsg('提示信息：', '你确定要删除该单位吗？', function () {
        if (!unitProjIdFlag) {
          if (flag == 'jingban') {
            _that.agentUnits.splice(index, 1);
          } else {
            _that.jiansheFrom.splice(index, 1);
          }
          return;
        }
        _that.loading = true;
        request('opus-admin', {
          url: ctx + "rest/unit/delete",
          type: 'post',
          data: {unitInfoId: unitInfoId, projInfoId: _that.projInfoId}
        }, function (data) {
          if (data.success) {
            _that.$message({
              message: '删除成功',
              type: 'success'
            });
            if (flag == 'jingban') {
              _that.agentUnits.splice(index, 1);
            } else {
              _that.jiansheFrom.splice(index, 1);
            }
            _that.loading = false;
          } else {
            _that.$message({
              message: data.message ? data.message : '删除失败',
              type: 'error'
            });
            _that.loading = false;
          }
        }, function (msg) {
          _that.$message({
            message: msg.message ? msg.message : '删除失败',
            type: 'error'
          });
          _that.loading = false;
        })
      }, function () {
      }, '确定', '取消', 'warning', true)
      
    },
    // 保存编辑新增单位
    saveProjinfoids: function (props, index, flag) {
      var _that = this;
      var url, msg;
      props.projInfoId = _that.projInfoId;
      props.projInfoIds = _that.projInfoId;
      props.linkmanInfoId = props.linkmanId;
      props.linkmanType = JSON.stringify(props.linkmanTypes);
      if (props.unitInfoId) {
        url = 'rest/unit/edit';
        msg = '单位信息保存成功';
      } else {
        url = 'rest/unit/save'
        msg = '单位信息保存成功';
      }
      var formRef = flag + index;
      var validFun;
      if ((typeof (_that.$refs[formRef].validate)) == 'function') {
        validFun = _that.$refs[formRef].validate
      } else {
        validFun = _that.$refs[formRef][0].validate
      }
      validFun(function (valid) {
        if (valid) { // idcard idtype
          _that.loading = true;
          request('', {
            url: ctx + url,
            type: 'post',
            data: props
          }, function (data) {
            if (data.success) {
              _that.$message({
                message: msg,
                type: 'success'
              });
              props.unitInfoId = data.content;
              if (_that.unitIdList.indexOf(props.unitInfoId) < 0) {
                _that.unitOrUserIsBlack(props);
              }
              _that.loading = false;
            } else {
              _that.$message({
                message: data.message ? data.message : '保存失败',
                type: 'error'
              });
              _that.loading = false;
            }
          }, function (msg) {
            _that.$message({
              message: '服务请求失败',
              type: 'error'
            });
            _that.loading = false;
          })
          
        } else {
          _that.$message({
            message: '请完善企业信息！',
            type: 'error'
          });
        }
      });
    },
    // 根据项目ID查找关联的单位列表
    getUnitsListByProjInfoId: function () {
      var _that = this;
      _that.loading = true;
      request('', {
        url: ctx + 'rest/unit/list/by/' + _that.projInfoId,
        type: 'get',
      }, function (result) {
        if (result.success) {
          _that.unitInfoIdList = result.content;
          _that.loading = false;
        }
      }, function (msg) {
        _that.$message({
          message: '服务请求失败！',
          type: 'error'
        });
        _that.loading = false;
      });
    },
    // 新增编辑联系人信息
    addLinkman: function (data, parData) {
      var _that = this;
      console.log(parData)
      _that.addEditManModalShow = true;
      _that.getUnitsListByProjInfoId();
      _that.addEditManPerform = parData;
      if (_that.projInfoId) {
        if (data) {
          _that.addEditManModalTitle = '编辑联系人';
          _that.addEditManModalFlag = 'edit';
          if (!data.linkmanId) {
            alertMsg('提示信息', '请选择联系人！', '关闭', 'error', true);
            return;
          } else {
            _that.backDLinkmanInfo(data, parData);
          }
        } else {
          _that.addEditManModalTitle = '新增联系人';
          _that.addEditManform = {};
          _that.addEditManModalFlag = 'add';
          if (parData.unitInfoId) {
            _that.addEditManform.unitInfoId = parData.unitInfoId;
            _that.addEditManform.unitName = parData.applicant;
          } else {
            _that.addEditManform.unitInfoId = '';
            _that.addEditManform.unitName = parData.applicant
          }
        }
      } else {
        alertMsg('', '请先查出项目信息！', '关闭', 'error', true);
      }
    },
    // 反显联系人信息
    backDLinkmanInfo: function (data, parData) {
      var _that = this;
      if (data.linkmanId) {
        request('', {
          url: ctx + 'rest/linkman/one/' + data.linkmanId,
          type: 'get'
        }, function (result) {
          if (result.success) {
            _that.addEditManform = result.content;
            _that.addEditManform.unitInfoId = parData.unitInfoId;
            _that.addEditManform.unitName = parData.applicant;
          }
        }, function (msg) {
          alertMsg('', '服务请求失败', '关闭', 'error', true);
        });
      } else {
        _that.aeaLinkmanInfoList = {};
      }
    },
    // 保存联系人信息
    saveAeaLinkmanInfo: function (linkmanType) {
      var _that = this;
      _that.addEditManform.linkmanType = linkmanType
      _that.$refs['addEditManform'].validate(function (valid) {
        if (valid) {
          _that.loading = true;
          var url, msg;
          if (_that.addEditManModalFlag == 'edit') {
            url = 'rest/linkman/edit';
            msg = '编辑联系人信息保存成功';
          } else {
            url = 'rest/linkman/save'
            msg = '新增联系人信息保存成功';
          }
          request('', {
            url: ctx + url,
            type: 'post',
            data: _that.addEditManform
          }, function (result) {
            if (result.success) {
              _that.$message({
                message: '保存成功',
                type: 'success'
              });
              _that.addEditManPerform.linkmanName = _that.addEditManform.linkmanName;
              _that.addEditManPerform.linkmanId = result.content;
              _that.addEditManPerform.linkmanMail = _that.addEditManform.linkmanMail;
              _that.addEditManPerform.linkmanCertNo = _that.addEditManform.linkmanCertNo;
              _that.addEditManPerform.linkmanMobilePhone = _that.addEditManform.linkmanMobilePhone;
              _that.addEditManModalShow = false;
              _that.loading = false;
            }
          }, function (msg) {
            _that.$message({
              message: msg.message ? msg.message : '保存失败！',
              type: 'error'
            });
            _that.loading = false;
          });
        } else {
          _that.$message({
            message: '请输入完整的联系人信息！',
            type: 'error'
          });
          return false;
        }
      });
    },
    // 重置表单
    resetForm: function (formName) {
      this.$refs[formName].resetFields();
      this.selectMat = '';
    },
    // 联系人模糊查询
    querySearchLinkMan: function (queryString, cb) {
      var _that = this;
      if (typeof (queryString) != 'undefined') queryString = queryString.trim();
      if (queryString != '' && queryString.length >= 1) {
        request('', {
          url: ctx + 'rest/linkman/list',
          type: 'get',
          data: {"keyword": queryString, "projInfoId": _that.projInfoId},
        }, function (result) {
          if (result.success) {
            _that.projNameSelect = result.content;
            _that.projNameSelect.map(function (item) {
              Vue.set(item, 'value', item.addressName);
            })
            var results = queryString ? _that.projNameSelect.filter(_that.createFilter(queryString)) : _that.projNameSelect;
            // 调用 callback 返回建议列表的数据
            cb(results);
          }
        }, function (msg) {
          cb([]);
        })
      } else {
        cb([]);
      }
    },
    // 选取申请人联系人信息
    querySearchApplyLinkMan: function (item) {
      this.applyPersonFrom.applyLinkmanName = item.addressName;
      this.applyPersonFrom.applyLinkmanIdCard = item.addressIdCard;
      this.applyPersonFrom.applyLinkmanTel = item.addressPhone;
      this.applyPersonFrom.applyLinkmanEmail = item.addressMail;
      this.applyPersonFrom.applyLinkmanId = item.addressId;
    },
    querySearchLinkLinkMan: function (item) {
      this.applyPersonFrom.linkLinkmanName = item.addressName;
      this.applyPersonFrom.linkLinkmanIdCard = item.addressIdCard;
      this.applyPersonFrom.linkLinkmanTel = item.addressPhone;
      this.applyPersonFrom.linkLinkmanEmail = item.addressMail;
      this.applyPersonFrom.linkLinkmanId = item.addressId;
    },
    // 联系人信息是否与申请人一致
    copyApplyLinkmanInfo: function (val) {
      if (this.sameAsApplyLink) {
        this.applyPersonFrom.linkLinkmanName = this.applyPersonFrom.applyLinkmanName;
        this.applyPersonFrom.linkLinkmanIdCard = this.applyPersonFrom.applyLinkmanIdCard;
        this.applyPersonFrom.linkLinkmanTel = this.applyPersonFrom.applyLinkmanTel;
        this.applyPersonFrom.linkLinkmanEmail = this.applyPersonFrom.applyLinkmanEmail;
        this.applyPersonFrom.linkLinkmanId = this.applyPersonFrom.applyLinkmanId;
      }
    },
    // 清空已输入申请人联系人信息
    clearApplyLinkManInfo: function (flag) {
      this.sameAsApplyLink = false;
      if (flag) { // 清空申请人信息
        this.applyPersonFrom.applyLinkmanName = '';
        this.applyPersonFrom.applyLinkmanIdCard = '';
        this.applyPersonFrom.applyLinkmanTel = '';
        this.applyPersonFrom.applyLinkmanEmail = '';
        this.applyPersonFrom.applyLinkmanId = '';
      } else { // 清空联系人信息
        this.applyPersonFrom.linkLinkmanName = '';
        this.applyPersonFrom.linkLinkmanIdCard = '';
        this.applyPersonFrom.linkLinkmanTel = '';
        this.applyPersonFrom.linkLinkmanEmail = '';
        this.applyPersonFrom.linkLinkmanId = '';
      }
    },
    //保存个人申办主体的申请人信息和联系人信息
    saveApplyAndLinkmanInfo: function () {
      var _that = this;
      var personalInfos = {
        personalInfos: [
          {
            applyOrLinkType: 'apply',
            linkmanCertNo: _that.applyPersonFrom.applyLinkmanIdCard,
            linkmanInfoId: _that.applyPersonFrom.applyLinkmanId,
            linkmanMail: _that.applyPersonFrom.applyLinkmanEmail,
            linkmanMobilePhone: _that.applyPersonFrom.applyLinkmanTel,
            linkmanName: _that.applyPersonFrom.applyLinkmanName,
            projInfoId: _that.projInfoId
          },
          {
            applyOrLinkType: 'link',
            linkmanCertNo: _that.applyPersonFrom.linkLinkmanIdCard,
            linkmanInfoId: _that.applyPersonFrom.linkLinkmanId,
            linkmanMail: _that.applyPersonFrom.linkLinkmanEmail,
            linkmanMobilePhone: _that.applyPersonFrom.linkLinkmanTel,
            linkmanName: _that.applyPersonFrom.linkLinkmanName,
            projInfoId: _that.projInfoId
          }
        ]
      };
      _that.$refs['applicantPer'].validate(function (valid) {
        if (valid) {
          _that.loading = true;
          request('', {
            url: ctx + 'rest/linkman/save/personal',
            type: 'post',
            ContentType: 'application/json',
            data: JSON.stringify(personalInfos)
          }, function (result) {
            if (result.success) {
              _that.$message({
                message: '保存成功',
                type: 'success'
              });
              if (_that.unitIdList.indexOf(_that.applyPersonFrom.applyLinkmanId) < 0) {
                _that.unitOrUserIsBlack(_that.applyPersonFrom);
              }
              _that.loading = false;
            }
          }, function (msg) {
            _that.$message({
              message: msg.message ? msg.message : '保存失败！',
              type: 'error'
            });
            _that.loading = false;
          });
        } else {
          _that.$message({
            message: '请输入完整的申请人联系人信息！',
            type: 'error'
          });
          return false;
        }
      });
    },
    // 申办主体信息 企业非企业申报 单位名称模糊查询
    querySearchJiansheName: function (queryString, cb) {
      var _that = this;
      if (typeof (queryString) != 'undefined') queryString = queryString.trim();
      if (queryString != '' && queryString.length >= 2) {
        request('opus-admin', {
          url: ctx + 'rest/unit/list',
          type: 'get',
          data: {"keyword": queryString, "projInfoId": _that.projInfoId},
        }, function (result) {
          if (result) {
            _that.projNameSelect = result.content;
            
            _that.projNameSelect.map(function (item) {
              Vue.set(item, 'value', item.applicant);
            })
            var results = queryString ? _that.projNameSelect.filter(_that.createFilter(queryString)) : _that.projNameSelect;
            // 调用 callback 返回建议列表的数据
            cb(results);
          }
        }, function (msg) {
          cb([]);
        })
      } else {
        cb([]);
      }
    },
    // 获取并行情形列表id
    getCoreItemsStatusListId: function () {
      var _that = this;
      var stateListLen = _that.stateList.length;
      var selStateIds = [];
      for (var i = 0; i < stateListLen; i++) {  // 已选并联情形id集合
        if (_that.stateList[i].selectAnswerId !== undefined || _that.stateList[i].selectAnswerId !== null) {
          // selStateIds=[];
          // return true;
          if (typeof (_that.stateList[i].selectAnswerId) == 'object' && _that.stateList[i].selectAnswerId.length > 0) {
            selStateIds = selStateIds.concat(_that.stateList[i].selectAnswerId);
          } else if (_that.stateList[i].selectAnswerId !== '') {
            selStateIds.push(_that.stateList[i].selectAnswerId);
          }
        }
      }
      selStateIds = selStateIds.filter(function (item, index) {
        //当前元素，在原始数组中的第一个索引==当前索引值，否则返回当前元素
        return selStateIds.indexOf(item, 0) === index
      })
      return selStateIds;
    },
    // 获取事项情形和材料
    getStatusStateMats: function (pData, data, parentId, parentStateId, index, flag, checkFlag) {
      var _that = this;
      var _parentId = parentId ? parentId : 'ROOT';
      var questionStateId = pData.parStateId ? pData.parStateId : '';
      var _itemVerId = data.itemVerId;
      var selStateIds = [];
      if (checkFlag == false) {
        var stateListLen = _that.stateList.length;
        selStateIds = _that.getCoreItemsStatusListId();
        for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
          var obj = _that.stateList[i];
          if (obj && (obj.parentStateId == _parentId) || (obj && obj.parentStateId != null && (selStateIds.indexOf(obj.parentStateId) == -1))) {
            if (typeof (obj.selectAnswerId) == 'object' && obj.selectAnswerId.length > 0) {
              obj.selectAnswerId.map(function (itemSel) {
                selStateIds = selStateIds.filter(function (item, index) {
                  return item !== itemSel;
                })
              });
            } else if (obj.selectAnswerId !== '') {
              selStateIds = selStateIds.filter(function (item, index) {
                return item !== obj.selectAnswerId;
              })
            }
            _that.stateList.splice(i, 1);
            i--
          }
        }
        selStateIds = _that.getCoreItemsStatusListId();
        // 清空对应情形下所有材料
        for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
          var obj = _that.model.matsTableData[i];
          if (obj && obj.itemStateId != null && (selStateIds.indexOf(obj.itemStateId) == -1)) {
            _that.model.matsTableData.splice(i, 1);
            i--
          }
        }
        return false
      }
      request('opus-admin', {
        url: ctx + 'rest/mats/states/mats/' + _that.itemVerId + '/' + parentId,
        type: 'get',
      }, function (data) {
        if (data.success) {
          var coreItemsMats = [];
          if (flag) {//root节点数据
            _that.matIds = [];
            _that.stateList = data.content.questionStates;
            _that.model.matsTableData = _that.unique(data.content.stateMats, 'mats');
            _that.popStateList = [];
            _that.stateList.map(function (item) {
              if (item.answerType != 's' && item.answerType != 'y') {
                Vue.set(item, 'selValue', []);
                item.selectAnswerId = item.selValue;
              }
            })
            _that.model.matsTableData.map(function (item, index) {
              _that.matIds.push(item.matId);
              if (_that.matCodes.indexOf(item.matCode) < 0) {
                _that.matCodes.push(item.matCode);
              }
            });
            // 回显
            if (_that.stateIds.length&&_that.stateList.length){
              _that.stateList.forEach(function(u, index){
                u.selectAnswerId = _that.stateIds[index];
              });
            }
            _that.getShareMatsList();
          } else {
            if (checkFlag == true) {
              data.content.questionStates.map(function (item, ind) { // 情形下插入对应的情形
                if (item.answerType != 's' && item.answerType != 'y') {
                  Vue.set(item, 'selValue', []);
                  item.selectAnswerId = item.selValue;
                }
                _that.stateList.splice((index + 1 + ind), 0, item);
              });
              // 选择情形后返回的材料列表
              data.content.stateMats.map(function (item) {
                if (item._parStateId == 'undefined' || item._parStateId == undefined) {
                  Vue.set(item, '_parStateId', [parentStateId]);
                } else {
                  item._parStateId.push(parentStateId);
                }
                if (item._itemVerIds == 'undefined' || item._itemVerIds == undefined) {
                  Vue.set(item, '_itemVerIds', [_itemVerId]);
                } else {
                  item._itemVerId.push(_itemVerId);
                }
                item.itemStateId = _parentId;
                coreItemsMats.push(item)
              });
            } else {
              var stateListLen = _that.stateList.length;
              selStateIds = _that.getCoreItemsStatusListId();
              for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
                var obj = _that.stateList[i];
                if (obj && (obj.parentQuestionStateId == questionStateId) || (obj && obj.parentStateId != null && (selStateIds.indexOf(obj.parentStateId) == -1))) {
                  if (typeof (obj.selectAnswerId) == 'object' && obj.selectAnswerId.length > 0) {
                    obj.selectAnswerId.map(function (itemSel) {
                      selStateIds = selStateIds.filter(function (item, index) {
                        return item !== itemSel;
                      })
                    });
                  } else if (obj.selectAnswerId !== '') {
                    selStateIds = selStateIds.filter(function (item, index) {
                      return item !== obj.selectAnswerId;
                    })
                  }
                  // if(obj&&obj.stateSeq.indexOf(parentStateId)>-1&&(obj.parStateId!=parentStateId)){
                  _that.stateList.splice(i, 1);
                  i--
                }
              }
              data.content.questionStates.map(function (item, ind) { // 情形下插入对应的情形
                if (item.answerType != 's' && item.answerType != 'y') {
                  Vue.set(item, 'selValue', []);
                  item.selectAnswerId = item.selValue;
                }
                _that.stateList.splice((index + 1 + ind), 0, item);
              });
              // 选择情形后返回的材料列表
              data.content.stateMats.map(function (item) {
                if (item._parStateId == 'undefined' || item._parStateId == undefined) {
                  Vue.set(item, '_parStateId', [parentStateId]);
                } else {
                  item._parStateId.push(parentStateId);
                }
                if (item._itemVerIds == 'undefined' || item._itemVerIds == undefined) {
                  Vue.set(item, '_itemVerIds', [_itemVerId]);
                } else {
                  item._itemVerId.push(_itemVerId);
                }
                item.itemStateId = _parentId;
                coreItemsMats.push(item)
              });
              selStateIds = _that.getCoreItemsStatusListId();
              for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
                var obj = _that.model.matsTableData[i];
                if (obj._parStateId == 'undefined' || obj._parStateId == undefined) {
                  Vue.set(obj, '_parStateId', []);
                } else {
                  // if (obj && (obj._parStateId.indexOf(parentStateId)>-1)&&(obj._parStateId.length==1)) {
                  if (obj && obj.itemStateId != null && (selStateIds.indexOf(obj.itemStateId) == -1) && (obj._parStateId.length == 1)) {
                    obj._parStateId = obj._parStateId.filter(function (item) {
                      return item != parentStateId;
                    });
                    _that.model.matsTableData.splice(i, 1);
                    i--
                  }
                }
              }
            }
          }
          coreItemsMats = _that.unique(coreItemsMats, 'mats');
          _that.model.matsTableData = _that.model.matsTableData.concat(coreItemsMats);
          _that.matIds = [];
          var matinstIdsObj = [];
          _that.model.matsTableData.map(function (item) {
            if (item.matChild == 'undefined' || item.matChild == undefined) {
              Vue.set(item, 'matChild', []);
            }
            if (item.certChild == 'undefined' || item.certChild == undefined) {
              Vue.set(item, 'certChild', []);
            }
            if (item.matinstId == 'undefined' || item.matinstId == undefined) {
              Vue.set(item, 'matinstId', '');
            }
            if (item.getPaper == 'undefined' || item.getPaper == undefined) {
              Vue.set(item, 'getPaper', '');
            }
            if (item.getCopy == 'undefined' || item.getCopy == undefined) {
              Vue.set(item, 'getCopy', '');
            }
            if (item.realPaperCount == 'undefined' || item.realPaperCount == undefined) {
              Vue.set(item, 'realPaperCount', item.duePaperCount);
            }
            if (item.realCopyCount == 'undefined' || item.realCopyCount == undefined) {
              Vue.set(item, 'realCopyCount', item.dueCopyCount);
            }
            if (matinstIdsObj.indexOf(item.matinstId) && item.matinstId != '') {
              matinstIdsObj.push(item.matinstId);
            }
            if (_that.matCodes.indexOf(item.matCode) < 0) {
              _that.matCodes.push(item.matCode);
            }
            _that.matIds.push(item.matId);
            
          });
          _that.getShareMatsList();
          _that.matinstIds = matinstIdsObj.join(',')
          _that.ifMatsSelAll();
        } else {
          _that.$message({
            message: '获取情形材料失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: '获取情形材料失败',
          type: 'error'
        });
      });
    },
    // 判断是否材料全选
    ifMatsSelAll: function () {
      var getCopyCount = 0;
      var _that = this;
      var getPaperCount = 0;
      _that.model.matsTableData.map(function (item) {
        if (item.getCopy == true) {
          getCopyCount++
        }
        if (item.getPaper == true) {
          getPaperCount++
        }
      })
      if (getCopyCount == _that.model.matsTableData.length) {
        _that.getCopyAll = true;
      } else {
        _that.getCopyAll = '';
      }
      if (getPaperCount == _that.model.matsTableData.length) {
        _that.getPaperAll = true;
      } else {
        _that.getPaperAll = '';
      }
    },
    toTree: function (data) {
      var result = []
      if (!Array.isArray(data)) {
        return result
      }
      data.forEach(function (item) {
        delete item.children;
      });
      var map = {};
      data.forEach(function (item) {
        map[item.id] = item;
      });
      data.forEach(function (item) {
        var parent = map[item.pId];
        if (parent) {
          (parent.children || (parent.children = [])).push(item);
        } else {
          result.push(item);
        }
      });
      return result;
    },
    // 材料 事项去重
    unique: function (arr, flag) {
      var result = {};
      var finalResult = [];
      var arr = arr.reverse();
      for (var i = 0; i < arr.length; i++) {
        if (flag == 'mats') {
          result[arr[i].matId] = arr[i];
        } else if (flag == 'stage') {
          result[arr[i].itemVerId] = arr[i];
        } else if (flag == 'file') {
          result[arr[i].name] = arr[i];
        }
      }
      var keysItems = Object.keys(result);
      keysItems.map(function (item) {
        finalResult.push(result[item]);
      })
      return finalResult.reverse();
    },
    // 文件上传 change事件
    fileChange: function (file, fileList) {
      var _that = this;
      _that.AllFileListId = [];
      if (fileList.length > 0) {
        this.AllFileList = this.unique(fileList, 'file');
        this.AllFileList.map(function (item) {
          _that.AllFileListId.push(item.name);
        });
        _that.checkedSelFlie = _that.AllFileListId;
      }
    },
    // 文件上传 submit事件
    submitUpload: function () {
      this.fileData = new FormData();
      this.$refs.uploadFile.submit();
      var _that = this;
      this.fileData.append("projInfoId", _that.projInfoId);
      this.fileData.append("matIds", _that.matIds);
      this.fileData.append("matinstIds", _that.matinstIds);
      this.fileData.append("unitInfoId", _that.rootUnitInfoId);
      this.uploadLogo = "1";
      if (_that.checkedSelFlie.length == 0) {
        alertMsg('', '请选择需要自动分拣的文件', '关闭', 'warning', true);
        return false;
      } else {
        _that.loadingFile = true;
        axios.post(ctx + 'rest/mats/att/upload/auto', _that.fileData).then(function (res) {
          _that.checkedSelFlie = [];
          _that.AllFileList = [];
          _that.loadingFile = false;
          _that.$refs.uploadFile.clearFiles();
          _that.checkAll = false;
          var matinstIdsObj = [];
          res.data.content.map(function (item) {
            _that.showFileListKey.push(item.matId);
            _that.model.matsTableData.map(function (matItem) {
              if (item.matId == matItem.matId) {
                matItem.matinstId = item.matinstId;
                matItem.matChild = item.fileList;
              }
              if (matItem.matChild.length > 0) {
                _that.showMatTableExpand = true;
              }
              if (matinstIdsObj.indexOf(matItem.matinstId) < 0 && matItem.matinstId != '') {
                matinstIdsObj.push(matItem.matinstId);
              }
            });
          });
          _that.matinstIds = matinstIdsObj.join(',')
        }).catch(function (error) {
          if (error.response) {
            // content.onError('配时文件上传失败(' + error.response.status + ')，' + error.response.data);
          } else if (error.request) {
            // content.onError('配时文件上传失败，服务器端无响应');
          } else {
            // content.onError('配时文件上传失败，请求封装失败');
          }
          _that.loadingFile = false;
        });
      }
      
    },
    myUploadFile: function (file) {
      var _that = this;
      _that.checkedSelFlie.map(function (item) {
        if (item == file.file.name) {
          _that.fileData.append('file', file.file);
        }
      });
    },
    handleCheckAllChange: function (val) {
      this.checkedSelFlie = val ? this.AllFileListId : [];
    },
    handleCheckedCitiesChange: function (value) {
      var checkedCount = value.length;
      this.checkAll = checkedCount === this.AllFileListId.length;
      this.checkedSelFlie = value;
    },
    showMatFiles: function (matId, docType) {  // bsc/att/listAttLinkAndDetailNoPage.do
      var _that = this;
      // var tempwindow = window.open('_blank');
      request('', {
        url: ctx + 'bsc/att/listAttLinkAndDetailNoPage.do',
        type: 'post',
        data: {
          tableName: 'AEA_ITEM_MAT',
          pkName: docType,
          recordId: matId,
          attType: null,
        },
      }, function (result) {
        if (result != null && result.length > 1) {
          
          var trHtml = '';
          _that.showMatFilesDialogShow = true;
          $.each(result, function (i, file) {
            trHtml += '<div class="td-cust clearfix" data-type="file" data-key="' + file.detailId + '"  data-format="' + file.attFormat + '">\n' +
                '                    <div class="file-image fl"><img src="' + _that.getFileIcon(file.attName) + '" /></div>\n' +
                '                    <div class="file-name fl">\n' +
                '                        <a href="' + _that.kpFileOpenEvent(file.detailId) + '" target="_blank" title="' + file.attName + '">' + _that.cutString(file.attName, 50) + '</a>\n' +
                '                    </div>\n' +
                '                </div>';
          });
          _that.dialogHtml = trHtml;
        } else if (result.length == 1) {
          var url = ctx + 'file/showFile.do?detailId=' + result[0].detailId;
          window.open.location = ctx + 'file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url);
        } else {
          _that.showMatFilesDialogShow = true;
          _that.dialogHtml = '无模板';
        }
      }, function (msg) {
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    kpFileOpenEvent: function (detailId) {
      var url = ctx + 'file/showFile.do?detailId=' + detailId;
      return ctx + 'file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url);
    },
    getFileIcon: function (fm) {
      var fileRegexs = {
        text: (/\.(txt|md)$/i),
        word: (/\.(docx|doc|wps)$/i),
        pdf: (/\.pdf$/i),
        picture: (/\.(png|jpg|jpeg|bmp|gif|svg|tiff)$/i),
        excel: (/\.(xls|xlsx)$/i),
        music: (/\.(mp3|wav|cad|wma|ra|midi|ogg|ape|flac|aac)$/i),
        video: (/\.(rm|rmvb|avi|flv|mp4|mkv|wmv|3gp|amv|mpeg1-4|mov|mtv|dat|dmv)$/i),
        code: (/\.(java|js|css|html)/i),
        exe: (/\.exe/i),
        psd: (/\.psd$/i),
        zip: (/\.(zip|rar|7z|gz|tar|bz2)$/i)
      };
      for (var key in fileRegexs) {
        if (fileRegexs[key].test(fm)) {
          var icon = ctx + 'approve/css/att/images/file-view/file_' + key + '.png';
          return icon;
        }
      }
      return ctx + 'approve/css/att/images/file-view/file_empty.png';
    },
    cutString: function (str, len) {
      //length属性读出来的汉字长度为1
      if (str.length * 2 <= len) {
        return str;
      }
      var strlen = 0;
      var s = "";
      for (var i = 0; i < str.length; i++) {
        s = s + str.charAt(i);
        if (str.charCodeAt(i) > 128) {
          strlen = strlen + 2;
          if (strlen >= len) {
            return s.substring(0, s.length - 1) + "...";
          }
        } else {
          strlen = strlen + 1;
          if (strlen >= len) {
            return s.substring(0, s.length - 2) + "...";
          }
        }
      }
      return s;
    },
    // 切换证照查询条件
    applySubjectChange: function (val) {
      console.log(val);
      this.selApplySubject = val;
      if (val.applySubjectType == "applyLinkman") {
        this.searchInline.showNumberType = false;
      } else {
        this.searchInline.showNumberType = true;
      }
      this.getCertFileListWin(val, 'oneUnit');
    },
    // 切换证照查询条件
    identityNumberTypeChange: function (val) {
      this.getCertFileListWin(this.selApplySubject, 'oneUnit');
    },
    // 展示文件上传下载弹窗
    showUploadWindow: function (data, flag) {
      var _that = this;
      _that.selMatRowData = data;
      _that.selMatinstId = data.matinstId ? data.matinstId : '';
      _that.allApplySubjectInfo = [];
      _that.jiansheFrom.map(function (unitItem) {
        if (unitItem.unifiedSocialCreditCode) {
          unitItem.applySubjectName = unitItem.applicant;
          _that.allApplySubjectInfo.push(unitItem);
        }
      });
      if (_that.applyPersonFrom.applyLinkmanIdCard) {
        _that.applyPersonFrom.applySubjectType = 'applyLinkman';
        _that.applyPersonFrom.applySubjectName = _that.applyPersonFrom.applyLinkmanName;
        _that.applyPersonFrom.linkmanCertNo = _that.applyPersonFrom.applyLinkmanIdCard;
        _that.allApplySubjectInfo.push(_that.applyPersonFrom);
      }
      if (flag == 'history') {
        _that.showUploadWindowFlag = true;
        _that.showUploadWindowTitle = '材料库 - ' + data.matName;
        _that.showUploadWindowBtn = false;
        _that.getShareMatsList(data);
      } else if (flag == 'certList') {
        _that.showCertWindowFlag = true;
        _that.getCertFileListWin(data);
      } else {
        _that.showUploadWindowFlag = true;
        _that.showUploadWindowBtn = true;
        _that.showUploadWindowTitle = '本地上传 - ' + data.matName;
        _that.getFileListWin(data.matinstId, data);
      }
    },
    // 获取已上传文件列表
    getFileListWin: function (matinstId, rowData) {
      var _that = this;
      request('', {
        url: ctx + 'rest/mats/att/list',
        type: 'get',
        data: {matinstId: matinstId}
      }, function (res) {
        if (res.success) {
          // if (res.content) {
          _that.uploadTableData = res.content ? res.content : [];
          if (rowData) {
            rowData.matChild = res.content ? res.content : [];
          }
          if (rowData.matChild.length > 0) {
            _that.showMatTableExpand = true;
          }
          // }
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
    // 获取证照文件列表
    getCertFileListWin: function (matData, flag) { // flag==oneUnit 查询单个建设单位
      var _that = this, _identityNumber = '';
      var certChild = _that.selMatRowData.certChild;
      var certChildIds = [];
      if (certChild.length > 0) {
        certChild.map(function (item) {
          if (certChildIds.indexOf(item.licenseCode) < 0) {
            certChildIds.push(item.licenseCode);
          }
        })
      }
      if (flag == 'oneUnit') {
        if (matData.applySubjectType == 'applyLinkman') {
          _identityNumber = matData.applyLinkmanIdCard;
        } else {
          if (_that.searchInline.identityNumberType == '0') {
            _identityNumber = matData.unifiedSocialCreditCode;
          } else {
            _identityNumber = matData.idno;
          }
        }
      } else {
        if (_that.applySubjectType == 0) {
          _identityNumber = _that.applyPersonFrom.applyLinkmanIdCard;
        } else {
          var identityNumbers = [];
          _that.jiansheFrom.map(function (unitItem) {
            identityNumbers.push(unitItem.unifiedSocialCreditCode);
          });
          _identityNumber = identityNumbers.join(',');
        }
      }
      ;
      _that.certMatAllData = {};
      _that.certMatTableData = [];
      request('', {
        url: ctx + 'aea/cert/getLicenseAuthRes.do',
        type: 'get',
        data: {identityNumber: _identityNumber, itemVerIds: _that.selMatRowData.itemVerId}
      }, function (res) {
        if (res) {
          _that.certMatAllData = res.content;
          _that.certMatTableData = res.content.data ? res.content.data : [];
          _that.certMatTableData.map(function (certItem) {
            if (certItem.bind == 'undefined' || certItem.bind == undefined) {
              Vue.set(certItem, 'bind', false);
            } else {
              certItem.bind = false;
            }
            if (certChildIds.indexOf(certItem.license_code) > -1) {
              certItem.bind = true
            } else {
              certItem.bind = false;
            }
          })
        } else {
          _that.$message({
            message: res.message ? res.message : '加载证照库材料失败',
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
    // 查看证照
    cretPreview: function (authCode) {
      var _that = this;
      request('', {
        url: ctx + 'aea/cert/getViewLicenseURL.do',
        type: 'get',
        data: {authCode: authCode}
      }, function (res) {
        if (res.success) {
          var tempwindow = window.open(); // 先打开页面
          setTimeout(function () {
            tempwindow.location = res.content;
          }, 1000)
        } else {
          _that.$message({
            message: res.message ? res.message : '证照查看失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: msg.message ? msg.message : '证照查看失败',
          type: 'error'
        });
      });
    },
    // 关联证照
    setCretLinked: function (certRowData) {
      var _that = this;
      _that.rootUnitInfoId = '';
      _that.rootLinkmanInfoId = '';
      _that.rootApplyLinkmanId = '';
      if (this.agentUnits.length > 0) {
        _that.rootUnitInfoId = _that.agentUnits[0].unitInfoId;
        _that.rootLinkmanInfoId = _that.agentUnits[0].linkmanId;
      }
      if (_that.jiansheFrom.length > 0) {
        _that.rootUnitInfoId = _that.jiansheFrom[0].unitInfoId;
        _that.rootLinkmanInfoId = _that.jiansheFrom[0].linkmanId;
      }
      if (_that.applySubjectType == 0 && _that.applyPersonFrom.applyLinkmanId) {
        _that.rootApplyLinkmanId = _that.applyPersonFrom.applyLinkmanId;
        _that.rootLinkmanInfoId = _that.applyPersonFrom.linkLinkmanId
      }
      ;
      var param = {
        "authCode": certRowData.auth_code,
        "certId": _that.selMatRowData.certId,
        "certOwner": certRowData.holder_name,
        "certinstCode": certRowData.license_code,
        "certinstId": "",
        "certinstName": certRowData.name,
        "issueDate": certRowData.issue_time,
        "issueOrgId": certRowData.issue_org_code,
        "managementScope": "",
        "matId": _that.selMatRowData.matId,
        "memo": certRowData.remark,
        "projInfoId": _that.projInfoId,
        "termEnd": certRowData.expiry_date,
        "termStart": certRowData.begin_date,
        "unitInfoId": _that.rootUnitInfoId,
        "linkmanInfoId": _that.rootLinkmanInfoId
      };
      request('', {
        url: ctx + 'rest/mats/bind/cert',
        type: 'post',
        ContentType: 'application/json',
        data: JSON.stringify(param)
      }, function (res) {
        if (res.success) {
          res.content.certName = certRowData.name;
          res.content.licenseCode = certRowData.license_code;
          if (_that.selMatRowData.certChild == 'undefined' || _that.selMatRowData.certChild == undefined) {
            Vue.set(_that.selMatRowData, 'certChild', [res.content]);
          } else {
            _that.selMatRowData.certChild.push(res.content);
          }
          if (_that.selMatRowData.certMatinstIds == 'undefined' || _that.selMatRowData.certMatinstIds == undefined) {
            Vue.set(_that.selMatRowData, 'certMatinstIds', [res.content.matinstId]);
          } else {
            if (_that.selMatRowData.certMatinstIds.indexOf(res.content.matinstId) < 0) {
              _that.selMatRowData.certMatinstIds.push(res.content.matinstId);
            }
          }
          certRowData.bind = true;
          _that.showMatTableExpand = true;
          if (_that.showFileListKey.indexOf(_that.selMatRowData.matId) < 0) {
            _that.showFileListKey.push(_that.selMatRowData.matId)
          }
          _that.$message({
            message: res.message ? res.message : '证照关联成功',
            type: 'success'
          });
        }
      }, function (msg) {
        _that.$message({
          message: msg.message ? msg.message : '证照关联失败',
          type: 'error'
        });
      });
    },
    // 解除关联
    unbindCert: function (matinstId, matDataCertChild, index) {
      var _that = this;
      request('', {
        url: ctx + 'rest/mats/unbind/cert',
        type: 'post',
        data: {matinstId: matinstId}
      }, function (data) {
        if (data.success) {
          matDataCertChild.splice(index, 1);
        }
      }, function (msg) {
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    //打印回执new
    printReceive1: function (row, index, ind) {
      this.receiveItemActive = index;
      this.receiveActive = ind;
      var fileUrl = ctx + 'rest/receive/toPDF/' + row.receiveId;
      this.pdfSrc = ctx + 'preview/pdfjs/web/viewer.html?file=' + encodeURIComponent(fileUrl)
    },
    // 预览电子件
    filePreview: function (data, flag) { // flag==pdf 查看施工图
      var detailId = data.fileId;
      var _that = this;
      console.log(data);
      var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
      var fileName = data.fileName;
      var fileType = this.getFileType(fileName);
      var flashAttributes = '';
      _that.filePreviewCount++;
      if (flag == 'pdf') {
        var tempwindow = window.open(); // 先打开页面
        setTimeout(function () {
          tempwindow.location = ctx + 'cod/drawing/drawingCheck?detailId=' + detailId;
        }, 1000)
      } else {
        if (fileType == 'pdf') {
          var tempwindow = window.open(); // 先打开页面
          setTimeout(function () {
            tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
          }, 1000)
        } else if (regText.test(fileType)) {
          // previewPdf/pdfIsCoverted
          _that.loading = true;
          request('', {
            url: ctx + 'previewPdf/pdfIsCoverted?detailId=' + detailId,
            type: 'get',
          }, function (result) {
            if (result.success) {
              _that.loading = false;
              var tempwindow = window.open(); // 先打开页面
              setTimeout(function () {
                tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
              }, 1000)
            } else {
              if (_that.filePreviewCount > 9) {
                confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                  _that.filePreviewCount = 0;
                  _that.filePreview(data);
                }, function () {
                  _that.filePreviewCount = 0;
                  _that.loading = false;
                  return false;
                }, '确定', '取消', 'warning', true)
                
              } else {
                setTimeout(function () {
                  _that.filePreview(data);
                }, 1000)
              }
            }
          }, function (msg) {
            _that.loading = false;
            _that.$message({
              message: '文件预览失败',
              type: 'error'
            });
          })
        } else {
          _that.loading = false;
          var tempwindow = window.open(); // 先打开页面
          setTimeout(function () {
            tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
          }, 1000)
        }
      }
    },
    // 预览源文件
    filePreview1: function (data) {
      var detailId = data.fileId;
      var flashAttributes = '';
      var tempwindow = window.open(); // 先打开页面
      setTimeout(function () {
        tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
      }, 1000)
    },
    // 勾选电子件
    selectionFileChange: function (val) {
      this.fileSelectionList = val;
    },
    //下载单个附件
    downOneFile: function (data) {
      window.open(ctx + 'rest/mats/att/download?detailIds=' + data.fileId, '_blank')
    },
    //删除单个文件附件
    delOneFile: function (data, matData) {
      var _that = this;
      console.log(data);
      console.log(matData)
      request('', {
        url: ctx + 'rest/mats/att/delelte',
        type: 'post',
        data: {matinstId: matData.matinstId, detailIds: data.fileId}
      }, function (res) {
        if (res.success) {
          _that.getFileListWin(res.content, matData);
          _that.$message({
            message: '删除成功',
            type: 'success'
          });
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
        detailIds.push(item.fileId);
      });
      detailIds = detailIds.join(',')
      window.open(ctx + 'rest/mats/att/download?detailIds=' + detailIds, '_blank')
    },
    debounceHandler: function (file) {
      this.loadingFileWin = true;
      this.debounce(this.uploadFileCom, file);
    },
    debounce: function (func, file) {
      var vm = this;
      window.clearTimeout(func.tId);
      func.temArr = func.temArr || [];
      func.temArr.push(file);
      console.log(file);
      func.tId = window.setTimeout(function () {
        this.loadingFileWin = false;
        func(func.temArr);
        func.temArr = [];
      }, 1000);
    },
    // 文件上传弹窗页面-上传电子件
    uploadFileCom: function (file) {
      var _that = this;
      var rowData = _that.selMatRowData;
      this.fileWinData = new FormData();
      file.forEach(function (u) {
        Vue.set(u.file, 'fileName', u.file.name);
        _that.fileWinData.append('file', u.file);
      })
      // Vue.set(file.file, 'fileName', file.file.name);
      // this.fileWinData.append('file', file.file);
      this.fileWinData.append("matId", rowData.matId);
      this.fileWinData.append("matinstCode", rowData.matCode);
      this.fileWinData.append("matinstName", rowData.matName ? rowData.matName : '');
      this.fileWinData.append("projInfoId", _that.projInfoId);
      this.fileWinData.append("unitInfoId", _that.rootUnitInfoId);
      this.fileWinData.append("matinstId", rowData.matinstId ? rowData.matinstId : '');
      this.fileWinData.append("matProp", rowData.matProp ? rowData.matProp : '');
      this.fileWinData.append("certId", rowData.certId ? rowData.certId : '');
      this.fileWinData.append("stoFormId", rowData.stoFormId ? rowData.stoFormId : '');
      _that.loadingFileWin = true;
      axios.post(ctx + 'rest/mats/att/upload', _that.fileWinData).then(function (res) {
        file.forEach(function (u) {
          Vue.set(u.file, 'matinstId', res.data.content);
        })
        // Vue.set(file.file, 'matinstId', res.data.content)
        _that.selMatinstId = res.data.content;
        _that.getFileListWin(res.data.content, rowData);
        var matinstIdsObj = [];
        _that.model.matsTableData.map(function (item) {
          if (item.matId == rowData.matId) {
            item.matinstId = res.data.content;
            _that.showFileListKey.push(item.matId)
          }
          if (matinstIdsObj.indexOf(item.matinstId) < 0 && item.matinstId != '') {
            matinstIdsObj.push(item.matinstId);
          }
          ;
        });
        _that.matinstIds = matinstIdsObj.join(',')
        _that.loadingFileWin = false;
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
    //文件上传弹窗页面- 删除电子件
    delSelectFileCom: function () {
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
        detailIds.push(item.fileId);
      });
      detailIds = detailIds.join(',')
      request('', {
        url: ctx + 'rest/mats/att/delelte',
        type: 'post',
        data: {matinstId: _that.selMatinstId, detailIds: detailIds}
      }, function (res) {
        if (res.success) {
          _that.getFileListWin(res.content, _that.selMatRowData);
          _that.$message({
            message: '删除成功',
            type: 'success'
          });
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
    },
    //打开拍照窗口
    openPhotoWindow: function (data, flag) {
      if (flag == 'lt') {
        lt_openPhotoWindow(data.matinstId, data.iteminstId, data.inoutId)
      } else {
        fz_openPhotoWindow(null, null, null);
      }
    },
    // 获取省份信息
    queryProvince: function () {
      var _that = this;
      request('', {
        url: ctx + 'rest/apply/province',
        type: 'get'
      }, function (data) {
        _that.provinceList = data.content;
        _that.queryCityData(0);
      })
    },
    //项目树右键事件
    showRightBtnList: function (e, data) {
      this.treeBtnTop = e.clientY;
      this.treeBtnLeft = e.clientX;
      this.treeRightBtnList = true;
      this.parentProjInfoId = data.id;
      
    },
    // 新增子工程
    addChildChildProj: function () {
      this.addChildProjShow = true;
      this.treeRightBtnList = false;
    },
    // 保存新增子工程
    saveAddChildProj: function () {
      var _that = this;
      var parmas = {
        isSecond: false,
        parentProjInfoId: _that.parentProjInfoId,
        projName: _that.childProjName,
        foreignRemark: _that.childProjText
      }
      console.log(parmas)
      request('', {
        url: ctx + 'rest/project/add/child',
        type: 'post',
        data: parmas
      }, function (data) {
        if (data.success) {
          _that.$message({
            message: '新增子项目成功',
            type: 'success'
          });
          _that.showProjTree();
          _that.addChildProjShow = false;
        }
      }, function (msg) {
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    // 关闭工程树窗口回调
    clearTreeChildInfo: function () {
      this.childProjName = '';
      this.childProjText = '';
      this.addChildProjShow = false;
      this.treeRightBtnList = false;
    },
    // 删除子工程
    delChildChildProj: function () {
      var _that = this;
      request('', {
        url: ctx + 'rest/project/delete/child',
        type: 'post',
        data: {childProjInfoId: _that.parentProjInfoId}
      }, function (data) {
        if (data.success) {
          _that.$message({
            message: '项目删除成功',
            type: 'success'
          });
        } else {
          _that.$message({
            message: data.message == '1' ? '该项目工程不能删除' : data.message == '2' ? '已申报的项目不能删除' : '删除失败',
            type: 'success'
          });
        }
        _that.showProjTree();
        _that.addChildProjShow = false;
      }, function (msg) {
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
        _that.addChildProjShow = false;
      });
    },
    // 选中子项目
    selChildProj: function (data) {
      this.projInfoId = data.id;
      this.projName = data.projName
      this.beforeCheck();
      this.projectTreeInfoModal = false;
    },
    // 根据单位id获取关联联系人
    getLinkManListByUnitId: function (item) {
      var _that = this;
      if (item.unitInfoId) {
        _that.loadingLinkMan = true;
        request('', {
          url: ctx + 'rest/linkman/list',
          type: 'get',
          data: {
            keyword: '',
            unitInfoId: item.unitInfoId
          }
        }, function (data) {
          if (data.success) {
            _that.loadingLinkMan = false;
            item.aeaLinkmanInfoList = data.content;
          }
        }, function (msg) {
          _that.$message({
            message: '获取单位关联联系人失败',
            type: 'error'
          });
        });
      } else {
        item.aeaLinkmanInfoList = [];
        _that.loadingLinkMan = false;
      }
    },
    // 删除联系人
    delLinkman: function (data, parentData, ind) {
      var _that = this;
      if (!data.addressId) {
        alertMsg('提示信息', '联系人ID为空', '关闭', 'warning', true);
        return;
      } else {
        confirmMsg('此操作影响：', '确定要删除该联系人吗？', function () {
          request('', {
            url: ctx + 'rest/linkman/delete/by/unit',
            type: 'post',
            data: {linkmanInfoId: data.addressId, unitInfoId: parentData.unitInfoId}
          }, function (result) {
            if (result.success) {
              _that.$message({
                message: '联系人删除成功',
                type: 'success'
              });
            } else {
              _that.$message({
                message: result.message ? result.message : '联系人删除失败',
                type: 'error'
              });
            }
          }, function (msg) {
            _that.$message({
              message: msg.message ? msg.message : '删除失败',
              type: 'error'
            });
          })
        }, function () {
        }, '确定', '取消', 'warning', true)
      }
    },
    // 选择企业联系人信息
    selLinkman: function (data, ind1, flag) {
      var _that = this;
      if (data) {
        if (flag == false) {
          _that.agentUnits[ind1].linkmanName = data.addressName;
          _that.agentUnits[ind1].linkmanId = data.addressId;
          _that.agentUnits[ind1].linkmanInfoId = data.addressId;
          _that.agentUnits[ind1].linkmanMail = data.addressMail;
          _that.agentUnits[ind1].linkmanCertNo = data.addressIdCard;
          _that.agentUnits[ind1].linkmanMobilePhone = data.addressPhone;
          
        } else {
          _that.jiansheFrom[ind1].linkmanName = data.addressName;
          _that.jiansheFrom[ind1].linkmanId = data.addressId;
          _that.jiansheFrom[ind1].linkmanInfoId = data.addressId;
          _that.jiansheFrom[ind1].linkmanMail = data.addressMail;
          _that.jiansheFrom[ind1].linkmanCertNo = data.addressIdCard;
          _that.jiansheFrom[ind1].linkmanMobilePhone = data.addressPhone;
          
        }
      }
    },
    // 材料全选
    checkAllMatChange: function (val, flag) {
      if (val == false) {
        val = '';
      }
      var _that = this;
      // this.checkedCities[[index]] = val ? this.id[[index]] : []
      _that.model.matsTableData.map(function (item) {
        if (flag == 'paper') {
          item.getPaper = val;
          _that.getPaperAll = val;
        } else {
          item.getCopy = val;
          _that.getCopyAll = val;
        }
      });
      // this.isIndeterminate[index] = false
    },
    // 材料单选
    checkedMatChange: function (val, index, flag) {
      var _that = this;
      if (val == false) {
        val = '';
      }
      if (flag == 'paper') {
        _that.model.matsTableData[index].getPaper = val;
        _that.getPaperAll = val;
      } else {
        _that.model.matsTableData[index].getCopy = val;
        _that.getCopyAll = val;
      }
    },
    // 意见窗口
    showCommentDialog: function (val, flag, _stoFormId) {
      var _that = this;
      _that.buttonStyle = val;
      _that.submitCommentsMatFlag = flag;
      _that.stoFormId = _stoFormId;
      var stateSel = _that.stateList
      var stateSelLen = stateSel.length;
      _that.stateIds = [];
      for (var i = 0; i < stateSelLen; i++) {
        
        if (stateSel[i].selectAnswerId == null || stateSel[i].selectAnswerId == '') {
          if (stateSel[i].mustAnswer == 1 && _that.buttonStyle != '4') {
            _that.stateIds = [];
            alertMsg('', '请选择情形：' + stateSel[i].stateName, '关闭', 'error', true);
            return true;
          }
        } else {
          if (typeof (stateSel[i].selectAnswerId) == 'object' && stateSel[i].selectAnswerId.length > 0) {
            _that.stateIds = _that.stateIds.concat(stateSel[i].selectAnswerId);
          } else if (stateSel[i].selectAnswerId !== '') {
            _that.stateIds.push(stateSel[i].selectAnswerId);
          }
        }
      }
      var jiansheUnitFormEleLen = $('.save-jansheUnit-info').length;
      var jinbanUnitFormEleLen = $('.save-jinbanUnit-info').length;
      var applicantPerFlag = true;// 个人申报校验通过
      var jiansheUnitFlag = true;// 企业申报校验通过
      var jinbanUnitFlag = true;// 经办申报校验通过
      var projBascInfoFlag = true; // 项目/工程信息校验通过
      var perUnitMsg = "";
      // 判断个人申报主体必填是否已填
      if (_that.applySubjectType == 0) {
        flag && _that.$refs['applicantPer'].validate(function (valid) {
          if (valid) {
            applicantPerFlag = true;
          } else {
            if (_that.buttonStyle != '4') {
              applicantPerFlag = false;
              perUnitMsg = "请完善申办主体个人信息";
              return false;
            }
          }
        });
      }
      flag && _that.$refs['projBascInfoShowFrom'].validate(function (valid) {
        if (valid) {
          projBascInfoFlag = true;
        } else {
          if (_that.buttonStyle != '4') {
            projBascInfoFlag = false;
            perUnitMsg = "请完善项目/工程信息";
            return false;
          }
        }
      })
      // 判断建设单位必填是否已填
      if (jiansheUnitFormEleLen > 0) {
        for (var i = 0; i < jiansheUnitFormEleLen; i++) {
          var formRef = 'jianshe_' + i;
          var validFun;
          if ((typeof (_that.$refs[formRef].validate)) == 'function') {
            validFun = _that.$refs[formRef].validate
          } else {
            validFun = _that.$refs[formRef][0].validate
          }
          validFun(function (valid) {
            if (!valid) {
              if (_that.buttonStyle != '4') {
                jiansheUnitFlag = false;
                perUnitMsg = "请完善申办主体建设单位信息"
                return false;
              }
            }
          });
        }
      } else if (jiansheUnitFormEleLen == 0 && _that.applySubjectType != 0) {
        if (_that.buttonStyle != '4') {
          jiansheUnitFlag = false;
          perUnitMsg = "请添加申办主体建设单位信息"
        }
      }
      // 判断经办单位必填是否已填deboun
      if (jinbanUnitFormEleLen > 0) {
        for (var i = 0; i < jinbanUnitFormEleLen; i++) {
          var formRef = 'jingban_' + i;
          var validFun;
          // console.log(typeof (_that.$refs[formRef].validate))
          if ((typeof (_that.$refs[formRef].validate)) == 'function') {
            validFun = _that.$refs[formRef].validate
          } else {
            validFun = _that.$refs[formRef][0].validate
          }
          validFun(function (valid) {
            if (!valid) {
              if (_that.buttonStyle != '4') {
                jinbanUnitFlag = false;
                perUnitMsg = "请完善申办主体经办单位信息"
                return false;
              }
            }
          });
        }
      } else if (jinbanUnitFormEleLen == 0 && _that.applySubjectType != 0 && _that.agentChecked) {
        if (_that.buttonStyle != '4') {
          jinbanUnitFlag = false;
          perUnitMsg = "请添加申办主体经办单位信息"
        }
      }
      if (projBascInfoFlag && applicantPerFlag && jiansheUnitFlag && jinbanUnitFlag) {
        _that.$refs['formTest'].validate(function (valid) {
          if (valid || _that.buttonStyle == 5 || _that.buttonStyle == '4') {
            if (_that.smsInfoId || _that.buttonStyle == '4') {
              // if(_that.oneFormInfo.length>0&&!_that.oneFormInputFlag){
              if (_that.oneFormInfo.length > 0 && _that.buttonStyle != '4') {
                confirmMsg('确认信息', '是否已完成一张表单填写？', function () {
                  _that.getMatinstIds();
                  if (!_that.attIsRequireFlag && _that.buttonStyle != 5 && _that.buttonStyle != '4') {
                    alertMsg('', '请上传所有必传的电子件', '关闭', 'warning', true);
                    return false;
                  }
                  if (_that.buttonStyle == '3') {
                    _that.submitCommentsTitle = '不予受理意见对话框'
                    _that.showMatList = true;
                    _that.submitCommentsFlag = true;
                    _that.getCommnetList();
                  } else if (_that.buttonStyle == '0') {
                    _that.submitCommentsTitle = '收件意见对话框'
                    _that.showMatList = false;
                    _that.submitCommentsFlag = true;
                    _that.getCommnetList();
                  } else {
                    _that.submitCommentsFlag = false;
                  }
                }, function () {
                  _that.openOneFormDialog();
                  return false;
                }, '已完成', '去填写', 'warning', true)
              } else {
                _that.getMatinstIds();
                if (!_that.attIsRequireFlag && _that.buttonStyle != 5 && _that.buttonStyle != '4') {
                  alertMsg('', '请上传所有必传的电子件', '关闭', 'warning', true);
                  return false;
                }
                if (_that.buttonStyle == '3') {
                  _that.submitCommentsTitle = '不予受理意见对话框'
                  _that.showMatList = true;
                  _that.submitCommentsFlag = true;
                  _that.getCommnetList();
                } else if (_that.buttonStyle == '0') {
                  _that.submitCommentsTitle = '收件意见对话框'
                  _that.showMatList = false;
                  _that.submitCommentsFlag = true;
                  _that.getCommnetList();
                } else {
                  _that.submitCommentsFlag = false;
                }
              }
            } else {
              alertMsg('', '请确认及保存办证结果取件方式！', '关闭', 'error', true);
            }
            
          } else {
            alertMsg('', '请选择材料', '关闭', 'error', true);
          }
        });
      } else {
        alertMsg('', perUnitMsg, '关闭', 'error', true);
      }
      
      
    },
    /********************** 意见编辑、修改、删除、批量删除、新增 *******************************/
    getCommnetList: function () {
      var _that = this;
      request('', {
        url: ctx + 'rest/comment/getAllActiveUserOpinions',
        type: 'get'
      }, function (result) {
        if (result.success) {
          _that.commentsList = result.content;
        }
      }, function (msg) {
        _that.$message({
          message: msg.message ? msg.message : '保存失败！',
          type: 'error'
        });
        _that.loading = false;
      });
    },
    getSelectedComment: function (val) {
      var _that = this;
      //**********以下各项目组根据需求启动对应的方法*******
      //替换已输入的意见
      _that.comments = val;
      //在已输入意见追加
      //_that.comments =_that.comments+ val;
      
    },
    //保存常用意见
    setUseComment: function () {
      var _that = this;
      if (_that.comments == '') {
      
      } else {
        request('', {
          url: ctx + 'rest/comment/saveUserOpinion',
          type: 'post',
          data: {'message': _that.comments}
        }, function (result) {
          if (result.success) {
            _that.$message({
              message: '保存成功',
              type: 'success'
            });
            _that.getCommnetList();
          }
        }, function (msg) {
          _that.$message({
            message: msg.message ? msg.message : '保存失败！',
            type: 'error'
          });
          _that.loading = false;
        });
      }
      
    },
    //单项删除审批意见
    deleteUseComment: function (val) {
      var _that = this;
      request('', {
        url: ctx + 'rest/comment/deleteUserOpinion/' + val,
        type: 'DELETE'
      }, function (result) {
        if (result.success) {
          _that.$message({
            message: '删除成功',
            type: 'success'
          });
          _that.getCommnetList();
        }
      }, function (msg) {
        _that.$message({
          message: msg.message ? msg.message : '保存失败！',
          type: 'error'
        });
        _that.loading = false;
      });
    },
    //批量删除意见
    batchDeleteUserOption: function () {
      var _that = this;
      var optionIds = ""
      if (_that.multipleOptionSelection.length == 0) {
        this.$message({
          type: 'warning',
          message: '请选择要删除的项'
        });
      } else {
        this.$confirm('即将删除多个, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(function () {
          $.each(_that.multipleOptionSelection, function (index, item) {
            optionIds = optionIds + item.opinionId + ","
          })
          optionIds = optionIds.substr(0, optionIds.length - 1);
          request('', {
            url: ctx + 'rest/comment/batchDeleteUserOpinion',
            type: 'DELETE',
            data: {'opinionIds': opinionIds}
          }, function (result) {
            if (result.success) {
              _that.$message({
                message: '批量删除成功',
                type: 'success'
              });
              _that.getCommnetList();
            }
          }, function (msg) {
            _that.$message({
              message: msg.message ? msg.message : '保存失败！',
              type: 'error'
            });
            _that.loading = false;
          });
        }).catch(function () {
          _that.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
      
      
    },
    //编辑单个意见弹框
    getUserOptionById: function (val) {
      var _that = this;
      _that.editCommentForm = $.extend({}, _that.editCommentForm, val);
      _that.submitCommentsFlag2 = true;
    },
    //保存编辑后的意见
    editUserOption: function () {
      var _that = this;
      _that.$refs['editCommentForm'].validate(function (valid) {
        if (valid) {
          request('', {
            url: ctx + 'rest/comment/editUserOpinion',
            type: 'put',
            data: _that.editCommentForm
          }, function (result) {
            if (result.success) {
              _that.$message({
                message: '保存成功',
                type: 'success'
              });
              _that.getCommnetList();
              _that.submitCommentsFlag2 = false;
            }
          }, function (msg) {
            _that.$message({
              message: msg.message ? msg.message : '保存失败！',
              type: 'error'
            });
            _that.loading = false;
          });
        } else {
          _that.$message({
            message: '请检查参数',
            type: 'error'
          });
          return false;
        }
      });
      
    },
    handleSelectionChange: function (val) {
      var _that = this;
      _that.multipleOptionSelection = val;
    },
    //不收件弹窗填写选择的材料名称
    getSelectedMat: function (val) {
      var _this = this;
      //默认追加；如果需要替换， 将以下两行替换为  _this.comments=val;
      var memo = _this.comments;
      _this.comments = memo + val + ":";
    },
    // 选择省份
    queryCityData: function (index) {
      this.cityList = this.provinceList[index].cityList;
      this.queryAreaData(0);
      this.getResultForm.addresseeProvince = this.provinceList[index].code;
    },
    // 选择市地区
    queryAreaData: function (index) {
      this.countyList = this.cityList[index].areaList;
      this.getResultForm.addresseeCity = this.cityList[index].code;
      this.queryCountyData(this.countyList[0].code);
    },
    // 选择地区
    queryCountyData: function (code) {
      this.getResultForm.addresseeCounty = code;
    },
    // 保存取证人信息
    saveSmsinfo: function () {
      var _that = this;
      _that.getResultForm.id = _that.smsInfoId;
      _that.getResultForm.applyinstId = _that.applyinstId;
      _that.$refs['resultForm'].validate(function (valid) {
        if (valid) {
          _that.loading = true;
          request('', {
            url: ctx + 'rest/apply/save/or/update/smsinfo',
            type: 'post',
            data: _that.getResultForm
          }, function (data) {
            if (data.success) {
              _that.smsInfoId = data.content;
              _that.loading = false;
              _that.$message({
                message: '保存成功',
                type: 'success'
              });
            } else {
              _that.loading = false;
              _that.$message({
                message: data.message ? data.message : '保存领件人失败',
                type: 'error'
              });
            }
          }, function (msg) {
            _that.loading = false;
            alertMsg('', '保存领件人失败', '关闭', 'error', true);
          });
        }
      });
    },
    // 切换领证模式
    changeReceiveMode: function (val) {
      if (val == 0) {
        this.getResultForm.smsType = 2;
        this.queryProvince();
      }
    },
    // 选取领证信息
    queryGetResultMan: function (item) {
      this.getResultForm.addresseeName = item.addressName;
      this.getResultForm.addresseePhone = item.addressPhone;
      this.getResultForm.addresseeIdcard = item.addressIdCard;
    },
    // 根据材料定义id获取材料实例id
    getMatinstIds: function () {
      var _that = this;
      var matCountVos = [];
      var selMatinstId = [];
      _that.attIsRequireFlag = true;
      _that.model.matsTableData.map(function (item) {
        var copyCnt = 0;
        var paperCnt = 0;
        if (item.zcqy == 0 && item.attIsRequire == 1) {
          if (!item.matinstId || item.matChild.length == 0) {
            _that.attIsRequireFlag = false;
          }
        }
        if (item.matinstId) {
          selMatinstId.push(item.matinstId)
        }
        if (item.certMatinstIds && item.certMatinstIds.length > 0) {
          selMatinstId = selMatinstId.concat(item.certMatinstIds)
        }
        if (item.getCopy == true) {
          copyCnt = item.realCopyCount;
        }
        if (item.getPaper == true) {
          paperCnt = item.realPaperCount;
        }
        if (item.getCopy == true || item.getPaper == true) {
          matCountVos.push({
            "certId": item.certId,
            "certName": item.certName,
            "copyCnt": copyCnt,
            "formName": item.formName,
            "itemVerId": item.itemVerId,
            "matId": item.matId,
            "matProp": item.matProp,
            "paperCnt": paperCnt,
            "stoFormId": item.stoFormId
          });
        }
      });
      if (!_that.attIsRequireFlag && _that.buttonStyle != 5) {
        alertMsg('', '请上传所有必传的电子件', '关闭', 'warning', true);
        return false;
      }
      if (matCountVos.length == 0) {
        _that.selMatinstIds = selMatinstId;
        if (_that.buttonStyle == 1 || _that.buttonStyle == 5 || _that.buttonStyle == 4) {
          _that.startSingleApprove();
        }
        return false;
      }
      var parmas = {
        matCountVos: matCountVos,
        projInfoId: _that.projInfoId,
        unitInfoId: _that.rootUnitInfoId,
        linkmanInfoId: _that.rootLinkmanInfoId
      }
      request('', {
        url: ctx + 'rest/mats/matinst/batch/save',
        type: 'post',
        ContentType: 'application/json',
        data: JSON.stringify(parmas)
      }, function (data) {
        if (data.success) {
          var tmpArr = [];
          data.content.map(function (item) {
            tmpArr = tmpArr.concat(item.matinstIds);
          });
          _that.selMatinstIds = tmpArr.concat(selMatinstId);
          if (_that.buttonStyle == 1 || _that.buttonStyle == 5) {
            _that.startSingleApprove();
          }
        }
      }, function (msg) {
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    //查询回执列表及分类
    queryReceiveList: function (applyinstIds) {
      var _that = this;
      //var applyinstId = '480bc49b-2f58-4cd8-ba4b-b2458d0684ab,144569b5-c678-48ed-b42f-9a7598bcae3e'
      request('', {
        url: ctx + 'rest/receive/getStageOrSeriesReceiveByApplyinstIds',
        type: 'get',
        data: {'applyinstIds': applyinstIds}
      }, function (res) {
        if (res.success) {
          _that.receiveList = res.content;
          _that.receiveList.map(function (item) {
            Vue.set(item, 'show', true);
          });
        debugger;
          // 默认选择第一个回执
          _that.printReceive1(_that.receiveList[0].receiveList[0], 0, 0);
          //显示列表弹框
          _that.receiveModalShow = true;
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    //打印回执
    printReceive: function (row) {
      console.log(row);
      var url = ctx + 'rest/receive/toPrintPage/' + row.receiveId;
      setTimeout(function () {
        window.open(ctx + 'rest/ntko/ntkoOpenWin?jumpUrl=' + encodeURIComponent(url));
      }, 500);
    },
    // 发起申报
    startSingleApprove: function () {
      var projInfoIds = [], handleUnitIds = []; // 项目ID集合 经办单位ID集合
      var branchOrgMap = [];// 分局承办
      var errorMsg = '';
      var _that = this;
      if (_that.preItemCheckPassed == false && _that.buttonStyle != 'tempSave') {
        confirmMsg('前置事项检测不通过', result.message, function () {
          _that.preItemCheckPassed = true;
        }, function () {
          _that.preItemCheckPassed = false
          return false;
        }, '继续申报', '放弃申报', 'error', true);
      }
      if (!_that.comments &&
          _that.buttonStyle != 1 &&
          _that.buttonStyle != 5 &&
          _that.buttonStyle != 4 &&
          _that.buttonStyle != 'tempSave'
      ) {
        alertMsg('', '请填写收件意见', '关闭', 'error', true);
        return false;
      }
      var buildProjUnitMap = [], maptemp = {'projectInfoId': '', 'unitIds': []}, unitIdTemp = [];
      projInfoIds.push(_that.projInfoId);
      _that.agentUnits.map(function (item) {
        handleUnitIds.push(item.unitInfoId);
      });
      _that.jiansheFrom.map(function (item) {
        unitIdTemp.push(item.unitInfoId);
      });
      maptemp.projectInfoId = _that.projInfoId;
      maptemp.unitIds = unitIdTemp;
      //maptemp[_that.projInfoId] = unitIdTemp;
      buildProjUnitMap.push(maptemp);
      
      if (_that.isBranch) {//勾选了分局承办
        branchOrgMap.push({
          itemVerId: _that.itemVerId,
          branchOrg: _that.branchOrg,
        });
      }
      if (branchOrgMap.length > 0) {
        branchOrgMap = JSON.stringify(branchOrgMap)
      } else {
        branchOrgMap = ''
      }
      _that.rootUnitInfoId = '';
      _that.rootLinkmanInfoId = '';
      _that.rootApplyLinkmanId = '';
      
      if (this.agentUnits.length > 0) {
        _that.rootUnitInfoId = _that.agentUnits[0].unitInfoId;
        _that.rootLinkmanInfoId = _that.agentUnits[0].linkmanId;
      }
      if (_that.jiansheFrom.length > 0) {
        _that.rootUnitInfoId = _that.jiansheFrom[0].unitInfoId;
        _that.rootLinkmanInfoId = _that.jiansheFrom[0].linkmanId;
      }
      if (_that.applySubjectType == 0 && _that.applyPersonFrom.applyLinkmanId) {
        _that.rootApplyLinkmanId = _that.applyPersonFrom.applyLinkmanId;
        _that.rootLinkmanInfoId = _that.applyPersonFrom.linkLinkmanId;
      }
      ;
      if (_that.buttonStyle == 4) {
        _that.IsJustApplyinst = 1;
      }
      //选择的情形
      var _isGreenWay = _that.isGreenWay==true?'1':'0';
      var parmas = {
        applyLinkmanId: _that.rootApplyLinkmanId,
        applySource: 'win',
        applySubject: _that.applySubjectType,
        applyinstId: _that.applyinstId,
        branchOrgMap: branchOrgMap,
        comments: _that.comments,
        handleUnitIds: handleUnitIds,
        itemVerId: _that.itemVerId,
        linkmanInfoId: _that.rootLinkmanInfoId,
        matinstsIds: _that.selMatinstIds,
        projInfoIds: projInfoIds,
        smsInfoId: _that.smsInfoId,
        buildProjUnitMap: buildProjUnitMap,
        stateIds: _that.stateIds,
        stageId: _that.stageId,
        isParallel: _that.isParallel,
        isJustApplyinst: _that.IsJustApplyinst,
        isGreenWay: _isGreenWay,
      }
      _that.progressDialogVisible = true;
      _that.submitCommentsFlag = false;
      var url = 'rest/apply/series/processflow/start';//发起申报
      if (_that.buttonStyle == '1' || _that.buttonStyle == '5') {//打印回执
        url = 'rest/apply/series/inst';
      } else if (_that.buttonStyle == '3') {//不受理
        url = 'rest/apply/series/inadmissible';
      } else if (_that.buttonStyle == '4') { // 仅实例化
        url = 'rest/apply/series/onlyInstApply';
      } else if (_that.buttonStyle == 'tempSave') { // 暂时保存
        url = 'rest/apply/series/stash';
        errorMsg = '暂存失败';
        parmas = {
          applySubject: _that.applySubjectType + '',
          applyinstId: _that.applyinstId,
          branchOrgMap: branchOrgMap,
          isParallel: _that.isParallel,
          itemVerId: _that.itemVerId,
          linkmanInfoId: _that.rootLinkmanInfoId,
          matinstsIds: _that.selMatinstIds,
          projInfoId: projInfoIds[0],
          stageId: _that.stageId,
          stateIds: _that.stateIds,
          isGreenWay: _isGreenWay,
        }
      }
      _that.progressIntervalStop = false;
      _that.setUploadPercentage();
      request('', {
        url: ctx + url,
        type: 'post',
        timeout: 1000000,
        ContentType: 'application/json',
        data: JSON.stringify(parmas),
      }, function (res) {
        if (res.success) {
          _that.progressIntervalStop = true;
          _that.uploadPercentage = 100;
          _that.progressDialogVisible = false;
          var applyinstId = res.content;
          _that.applyinstId = res.content
          _that.$message({
            message: '操作成功',
            type: 'success'
          });
          if (_that.buttonStyle == 4) {
            if (_that.submitCommentsMatFlag == 'matForm') {
              _that.getOneFormrender3(_that.applyinstId, _that.stoFormId);
            } else {
              _that.getOneFormData();
            }
            return false;
          }
          setTimeout(function () {
            _that.progressDialogVisible = false;
            if (_that.buttonStyle == 5) {
              _that.getLackMatsMatmend()
            } else if (_that.buttonStyle != 'tempSave') {
              _that.queryReceiveList(applyinstId);
            }
          }, 300);
        } else {
          _that.progressIntervalStop = true;
          _that.progressDialogVisible = false;
          _that.uploadPercentage = 0;
          _that.$message({
            message: res.message ? res.message : errorMsg,
            type: 'error'
          });
        }
      }, function (msg) {
        _that.progressDialogVisible = false;
        _that.progressIntervalStop = true;
        _that.uploadPercentage = 0;
        _that.$message({
          message: msg.message ? msg.message : '网络请求失败！',
          type: 'error'
        });
      });
    },
    // 获取材料一张表单render
    getOneFormrender3: function (_applyinstId, _formId) {
      var _that = this;
      // _formId = 'ecbebb64-a29c-41c6-abb7-e7b337a1a2cb';
      var sFRenderConfig = '&showBasicButton=true&includePlatformResource=false&busiScence=mat';
      request('', {
        url: ctx + 'bpm/common/sf/renderHtmlFormContainer?listRefEntityId=' + _applyinstId + '&listFormId=' + _formId + sFRenderConfig,
        type: 'get',
      }, function (result) {
        if (result.success) {
          _that.matFormDialogVisible = true;
          $('#matFormContent').html(result.content)
          _that.$nextTick(function () {
            $('#matFormContent').html(result.content)
          });
        } else {
          _that.$message({
            message: result.content ? result.content : '获取材料表单失败！',
            type: 'error'
          });
        }
      }, function (res) {
        _that.$message({
          message: '获取材料表单失败！',
          type: 'error'
        });
      })
    },
    // 展示材料一张表单弹窗
    showOneFormDialog1: function (oneformMat) {
      var _that = this;
      var _applyinstId = _that.applyinstId;
      _that.selMatRowData = oneformMat;
      _that.matformNameTitle = oneformMat ? oneformMat.matName : '';
      if (_applyinstId == '') {
        if (oneformMat) {
          _that.showCommentDialog('4', 'matForm', oneformMat.stoFormId)
        } else {
          _that.showCommentDialog('4')
        }
      } else {
        _that.getOneFormrender3(_applyinstId, oneformMat.stoFormId)
      }
    },
    //显示机构树
    showOrgTree: function (val, data) {
      console.log(data);
      if (val == false) {
        return true
      }
      var _that = this;
      var parentOrgId = data.orgId ? data.orgId : '';
      request('', {
        url: ctx + 'rest/org/tree',
        type: 'get',
        data: {
          dataType: '',
          orgName: '',
          parentOrgId: parentOrgId
        }
      }, function (result) {
        if (result.success) {
          _that.orgTreeInfoModal = true;
          _that.orgTree = _that.toTree(result.content);
        } else {
          _that.$message({
            message: '项目工程查询失败！',
            type: 'error'
          });
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    selChildOrg: function (rowData) {
      this.branchOrg = rowData.id;
      this.itemBasicInfo.orgName = rowData.name;
      this.orgTreeInfoModal = false;
    },
    // 刷新页面
    reloadPage: function () {
      if (this.buttonStyle == 0 || this.buttonStyle == 3 || this.buttonStyle == 5) {
        window.location.reload()
      }
    },
    // 设置进度条的值
    setUploadPercentage: function () {
      var _that = this;
      _that.uploadPercentage = 0;
      var interval = setInterval(function () {
        _that.uploadPercentage += 3;
        if (_that.uploadPercentage >= 96 || _that.progressIntervalStop == true) {
          clearInterval(interval);
        }
      }, 300);
    },
    formatUploadPercentage: function (percentage) {
      return percentage === 100 ? '成功' : (percentage + '%');
    },
    // 显示共享材料
    showShareModal: function (data) {
      this.selMatRowData = data;
      this.searchShareFileList(data);
      this.getFileListWindowFlag = true;
    },
    // 展开收起打印回执
    toggleReceiveListShow: function (item) {
      item.show = !item.show;
    },
    // 查询要导入的电子件列表
    searchShareFileList: function (data) {
      var _that = this;
      var unitInfoId = '';
      var unitInfoIds = [];
      _that.jiansheFrom.map(function (item) {
        unitInfoIds.push(item.unitInfoId);
      })
      _that.agentUnits.map(function (item) {
        unitInfoIds.push(item.unitInfoId);
      })
      unitInfoId = unitInfoIds.join(',');
      var parmas = {
        keyword: _that.searchFileListfilter,
        matId: data.matId,
        matinstId: data.matinstId,
        page: '',
        unitInfoId: unitInfoId,
      };
      request('', {
        url: ctx + 'rest/mats/att/import/list',
        type: 'get',
        data: parmas
      }, function (result) {
        if (result.success) {
          _that.shareFileList = result.content;
        } else {
          _that.$message({
            message: '可共享文件查询失败！',
            type: 'error'
          });
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    // 导入电子件
    fileImport: function () {
      var _that = this;
      var unitInfoId = '';
      var unitInfoIds = [];
      var fileIds = '';
      var fileIdList = [];
      var selFileList = _that.$refs.shareFileList.selection;
      if (selFileList.length == 0) {
        _that.$message({
          message: '请选择需要导入的文件！',
          type: 'error'
        });
        return true;
      }
      selFileList.map(function (item) {
        fileIdList.push(item.detailId);
      });
      fileIds = fileIdList.join(',')
      _that.jiansheFrom.map(function (item) {
        unitInfoIds.push(item.unitInfoId);
      })
      _that.agentUnits.map(function (item) {
        unitInfoIds.push(item.unitInfoId);
      })
      unitInfoId = unitInfoIds.join(',');
      var parmas = {
        fileIds: fileIds,
        matId: _that.selMatRowData.matId,
        matinstId: _that.selMatRowData.matinstId,
        projInfoId: _that.projInfoId,
        unitInfoId: unitInfoId
      };
      request('', {
        url: ctx + 'rest/mats/att/import',
        type: 'post',
        data: parmas
      }, function (result) {
        if (result.success) {
          _that.$message({
            message: '导入文件成功！',
            type: 'success'
          });
        } else {
          _that.$message({
            message: '导入文件失败！',
            type: 'error'
          });
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    // 获取文件后缀
    getFileType: function (fileName) {
      var index1 = fileName.lastIndexOf(".");
      var index2 = fileName.length;
      var suffix = fileName.substring(index1 + 1, index2).toLowerCase();//后缀名
      if (suffix == 'docx') {
        suffix = 'doc';
      }
      return suffix;
    },
    // 根据顶级组织ID查询区划列表  rest/org/region/list
    getDistrictList: function (cb) {
      var _that = this;
      request('', {
        url: ctx + 'rest/org/region/list',
        type: 'get',
      }, function (result) {
        if (result.content) {
          _that.districtList = result.content;
          typeof cb == 'function' && cb();
        } else {
          _that.$message({
            message: '获取行政区划列表失败',
            type: 'error'
          });
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    // 获取主题，对应主题的项目类型  rest/theme/theme/type/list
    getProjThemeIdList: function () {
      var _that = this;
      _that.loadingThemeIdList = true;
      request('', {
        url: ctx + 'rest/theme/theme/type/list',
        type: 'get',
        data: {themeType: _that.projBascInfoShow.projType}
      }, function (result) {
        if (result.content) {
          _that.projThemeIdList = result.content;
          _that.loadingThemeIdList = false;
        } else {
          _that.$message({
            message: '获取项目类型列表失败',
            type: 'error'
          });
          _that.loadingThemeIdList = false;
        }
      }, function (msg) {
        _that.loadingThemeIdList = false;
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    setShowUnitMore: function (index) {
      if (this.showUnitMore.indexOf(index) > -1) {
        this.showUnitMore = this.showUnitMore.filter(function (item) {
          return item != index;
        });
      } else {
        this.showUnitMore.push(index);
      }
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
          applyinstId: ts.applyinstId
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
          ts.$message.error(res.message ? res.message : '网络错误！');
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
        approveOrgName: this.matMendDialogData.approveOrgName,
        iteminstName: this.matMendDialogData.iteminstName,
        localCode: this.matMendDialogData.localCode,
        owner: this.matMendDialogData.owner,
        projInfoName: this.matMendDialogData.projInfoName,
        correctDueDays: '',
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
            applyinstId: ts.applyinstId,
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
              // ts.$message.success('保存成功！');
              ts.isShowMatmend = false;
              confirmMsg('提示信息：', '材料补全成功，是否打印回执？', function () {
                ts.queryReceiveList(ts.applyinstId);
              }, function () {
                ts.reloadPage();
              }, '是', '否', 'success', true)
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
    clickTobaSelectMaterial: function (val, dataMat, attrFlag) { // attrFlag 选择的材料类型
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
              _that.matCorrectDtos[i].isNeedAtt = '1';
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
    isGetReceiveList: function () {
      var _that = this;
      confirmMsg('提示信息：', '是否打印回执？', function () {
        _that.queryReceiveList(_that.applyinstId);
      }, function () {
        _that.reloadPage();
      }, '是', '否', 'success', true)
    },
    // 根据单位id获取关联联系人
    getUnitLinkManList: function (row) {
      var _that = this;
      // if(row.unitInfoId){
      _that.loadingUnitLinkMan = true;
      request('', {
        url: ctx + 'rest/linkman/list',
        type: 'get',
        data: {
          keyword: '',
          unitInfoId: row.unitInfoId ? row.unitInfoId : '',
          projInfoId: _that.projInfoId
        }
      }, function (data) {
        _that.loadingUnitLinkMan = false;
        if (data.success) {
          _that.unitLinkManOptions = data.content;
        }
      }, function (msg) {
        _that.loadingUnitLinkMan = false;
      });
      // }
    },
    // 人员设置选择人员
    selTypeLinkman: function (typeData, manData) {
      typeData.linkmanName = manData.addressName;
      typeData.linkmanInfoId = manData.addressId;
    },
    // 新增人员设置
    addLinkmanTypes: function (row) {
      var dataType = {
        linkmanInfoId: '',
        linkmanType: '',
        linkmanName: ''
      }
      row.push(dataType);
    },
    // 新增人员设置
    delLinkmanTypes: function (row, index) {
      row.splice(index, 1);
    },
  },
  filters: {
    changeReceiveType: function (value) {
      var defaultValue = '其他回执';
      //{{(itemBtn.receiptType==2)?'收件回执':(itemBtn.receiptType==1)?'物料回执':(itemBtn.receiptType==3)?'不受理回执':'其他回执'}}
      if (value) {
        switch (value) {
          case '1':
            defaultValue = '物料回执';
            break;
          case '2':
            defaultValue = '受理回执';
            break;
          case '3':
            defaultValue = '不受理回执';
            break;
          case '4':
            defaultValue = '退件回执';
            break;
          case '5':
            defaultValue = '送达回证';
            break;
          case '6':
            defaultValue = '材料补正回执';
            break;
          case '7':
            defaultValue = '行政许可申请书';
            break;
          default:
            defaultValue = '其他回执';
        }
      }
      return defaultValue;
    },
    formatDate: function (time) {
      if (!time) return '-';
      var date = new Date(time);
      if (!date) return;
      return formatDate(date, 'yyyy-MM-dd hh:mm');
    },
    formatBjType: function (bjType) {
      switch (bjType) {
        case '1':
          return '（工作日）';
        case '2':
          return '（自然日）';
      }
      return '个工作日'
    },
    formatLicenseType: function (value) {
      var defaultValue = '';
      if (value) {
        switch (value) {
          case 'CERTIFICATE':
            defaultValue = '证件执照';
            break;
          case 'PROOF':
            defaultValue = '证明文件';
            break;
          case 'APPROVAL':
            defaultValue = '批文批复';
            break;
          case 'REPORT':
            defaultValue = '鉴定报告';
            break;
          case 'RESULT':
            defaultValue = '办事结果';
            break;
        }
      }
      return defaultValue;
    },
    formatLicenseStatus: function (value) {
      var defaultValue = '';
      if (value) {
        switch (value) {
          case 'DRAFT':
            defaultValue = '草案';
            break;
          case 'REGISTERED':
            defaultValue = '已制证（未签发）';
            break;
          case 'ISSUED':
            defaultValue = '已签发';
            break;
          case 'ABOLISHED':
            defaultValue = '已废止';
            break;
        }
      }
      return defaultValue;
    },
  }
});

function callbackAfterSaveSFForm(result, sFRenderConfig, formModelVal, actStoForminst) {
  console.log(result, sFRenderConfig.busiScence, formModelVal, actStoForminst);
  vm.relateFormCallback({
    result: result,
    sFRenderConfig: sFRenderConfig,
    formModelVal: formModelVal,
    actStoForminst: actStoForminst,
  });
  // if (sFRenderConfig.busiScence == 'mat') {
  //   var parma = {
  //     "linkmainInfoId": vm.rootApplyLinkmanId,
  //     "matId": vm.selMatRowData.matId,
  //     "projInfoId": vm.projInfoId,
  //     "stoForminstId": actStoForminst.stoForminstId,
  //     "unitInfoId": vm.rootUnitInfoId
  //   };
  //   request('', {
  //     url: ctx + 'rest/mats/bind/form',
  //     type: 'POST',
  //     ContentType: 'application/json',
  //     data: JSON.stringify(parma),
  //   }, function (result1) {
  //   }, function (msg) {
  //   })
  // } else {
  //   vm.oneFormInputFlag = true;
  // }
}