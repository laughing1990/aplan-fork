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
      if (value === '' || value ===undefined || value ===null) {
        callback(new Error('该输入项为必输项！'));
      }else if(value.toString().trim() === ''){
        callback(new Error('该输入项为必输项！'));
      } else {
        callback();
      }
    };
    // 输入为整数数字 大于等于0
    var checkMissNum = function (rule, value, callback) {
      if (value) {
        var flag = !/^[+]{0,1}(\d+)$/.test(value);
        if (flag) {
          return callback(new Error('格式错误'));
        } else {
          callback();
        }

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
    var checkPhoneNum = function (rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('必填字段！'));
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
      isEdit: false, // 编辑or新增
      demoStageId:"",
      isParallel: true, // true为并联
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
      statusList: [],  // 申报阶段列表
      themeList: [
        {themeType: "",
        themeTypeId: "",
        themeTypeName: "并联审批",
        themesList: []
        }
      ],  // 主题类型主题列表
      themeType: '', // 项目所属主题类型
      themeTypeIndex: 0, // 项目所属主题类型无值默认选第一个
      themeDialogIndex: 0, // 并联项目所属主题
      themeId: '', // 项目所属主题id
      selTheme: {}, // 已选主题信息
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
      themeInfoList: [], // 主题列表
      themeInfoListP: [], // 并联主题列表
      parallelThemeList: [], // 并联主题列表
      activeNames: ['1', '2'], // el-collapse 默认展开列表
      ctx: ctx,
      verticalTabData: [ // 左侧纵向导航数据
        {
          label: '项目基本信息',
          target: 'baseInfo'
        }, {
          label: '申报主体信息',
          target: 'applyInfo'
        }
      ],
      curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      selThemeActive: -1, // 选择主题弹窗已选主题
      selThemeActive1: -1, // 辅线主题弹窗已选主题
      popStateList: [], // 新增情形列表
      matsTableData: [], // 材料列表
      model:{
        rules:{
          getPaper:{ required:true,message:"必选",trigger:["change"]},
          getCopy:{ required:true,message:"必选",trigger:["change"]},
          realPaperCount:[
            { required: true,message: '必填！', trigger: ['blur'] },
            {required: true,validator:checkMissNum,trigger:['blur']}
          ],
          realCopyCount: [
            { required: true,message: '必填！', trigger: ['blur'] },
            { required:true,validator:checkMissNum,trigger:['blur']}
          ],
          realPaperCount1:[
            {validator:checkMissNum,trigger:['blur']}
          ],
          realCopyCount1: [
            { validator:checkMissNum,trigger:['blur']}
          ]
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
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanCertNo: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanMobilePhone:[
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ]
      },
      unitInfoIdList: [], // 根据项目id查找相关联的单位列表
      rulesCompanyForm: { // 编辑新增单位信息校验
        unitType: [
          { required: true,message: '请选择单位类型！', trigger: 'change' },
        ],
        applicant: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        idrepresentative: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanCertNo: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanMobilePhone: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanName: [
          { required: true,message: '请选择联系人！', trigger: ['change','blur'] },
        ],
        unifiedSocialCreditCode: [
          { required: true,message: '请输入统一社会信用代码！', trigger: ['blur'] },
        ]
      },
      rulesApplyPersonForm: { // 编辑新增个人申报主体信息校验
        applyLinkmanName: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        applyLinkmanIdCard: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        applyLinkmanTel: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        linkLinkmanName: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        linkLinkmanIdCard: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        linkLinkmanTel: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ]
      },
      projBascInfoShowFromRules: {  // 基本信息校验
        projName: [
          { required: true,message: '请输入项目/工程名称！', trigger: 'change' },
        ],
        localCode: [
          { required: true,message: '请输入投资平台登记的项目代码！', trigger: ['change','blur'] },
        ],
        gcbm: [
          { required: true,message: '请输入工程代码！', trigger: ['change'] },
        ],
        regionalism: [
          { required: true,message: '请选择审批行政区划！', trigger: ['change'] },
        ],
        projType: [
          { required: true,message: '请选择立项类型！', trigger: ['change'] },
        ],
        projectAddress: [
          { required: true,message: '请输入建设地点！', trigger: ['blur'] },
        ],
        financialSource: [
          { required: true,message: '请选择资金来源！', trigger: ['change'] },
        ],
        landSource: [
          { required: true,message: '请选择土地来源！', trigger: ['change'] },
        ],
        isDesignSolution: [
          { required: true,message: '请选择土地是否带设计方案！', trigger: ['change'] },
        ],
        isAreaEstimate: [
          { required: true,message: '请选择是否完成区域评估！', trigger: ['change'] },
        ],
        projNature: [
          { required: true,message: '请选择建设性质！', trigger: ['change'] },
        ],
        investType: [
          { required: true,message: '请选择投资类型！', trigger: ['change'] },
        ],
        gbCodeYear: [
          { required: true,message: '请输入国标行业代码发布年代！', trigger: ['change'] },
        ],
        theIndustry: [
          { required: true,message: '请选择国标行业！', trigger: ['blur', 'change'] },
        ],
        nstartTime: [
          { required: true,message: '请选择拟开工时间！', trigger: ['change'] },
        ],
        endTime: [
          { required: true,message: '请选择拟建成时间！', trigger: ['change'] },
        ],
        scaleContent: [
          { required: true,message: '请输入建设规模及内容！', trigger: ['change'] },
        ],
        xmYdmj: [
          {validator:checkNumFloat, trigger: ['blur'] },
          { required: true,message: '请填写用地面积！', trigger: ['change'] }
        ],
        xzydmj: [
          {validator:checkNumFloat, trigger: ['blur'] },
        ],
        buildAreaSum: [
          {validator:checkNumFloat, trigger: ['blur'] },
          { required: true,message: '请填写建筑面积！', trigger: ['change'] }
        ],
        aboveGround: [
          {validator:checkNumFloat, trigger: ['blur'] },
        ],
        underGround: [
          {validator:checkNumFloat, trigger: ['blur'] },
        ],
        investSum: [
          {validator:checkNumFloat, trigger: ['blur'] },
          { required: true,message: '请输入总投资！', trigger: ['change'] }
        ]
      },
      statusLineList: [], // 主题下阶段类型
      statusLineListActive: 0, // 主题下阶段类型选中状态
      AllFileList: [], // 智能分拣区所选择文件
      fileList: [],
      checkAll: true, // 智能分拣区文件是否全选
      checkedSelFlie: [], // 智能分拣区已选文件
      AllFileListId: [], // 已选文件name集合
      matIds: [], // 材料matIdS
      selMatinstId: '', // 已选材料实例Id
      matinstIds: [], // 材料实例Ids
      selMatinstIds: [],
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
      showVerLen: 1, // 显示左侧纵向导航栏的长度
      showMoreProjInfo: false, // 查询项目后展示更多信息
      loadingFile: false, // 文件上传loading
      loadingFileWin: false, // 窗口文件上传loading
      dialogHtml: '', // 样本弹窗html
      showMatFilesDialogShow: false, // 是否展示样本弹窗
      showUploadWindowFlag: false, // 是否展示文件上传窗口
      showUploadWindowTitle: '材料附件', // 文件上传窗口header
      getFileListWindowFlag: false, // 是否展示导入电子材料弹窗
      fileSelectionList: [], // 所选电子件
      selMatRowData: {}, // 所选择的材料信息
      uploadMatinstId: '', // 上传返回材料实例
      submitCommentsFlag: false, // 是否展示收件意见框
      submitCommentsFlag1: false, // 展示常用意见管理框
      submitCommentsFlag2: false, // 展示编辑常用意见框
      submitCommentsTitle: '收件意见对话框', // 意见框title
      showMatList: false,//收件意见弹框是否显示材料列表
      submitCommentsType: 0, // 0发起申报 1打印回执 3不受理
      receiveList: [],//回执列表
      // receiveList: [{"applyinstId":"307c74b6-2071-456b-bc58-8581c185b826","applyinstCode":"2019080618516841","name":"立项用地许可阶段","receiveList":[{"receiveId":"02085df1-54da-4fdc-8af5-5bed5de01018","applyinstId":"307c74b6-2071-456b-bc58-8581c185b826","outinstId":"13d9f3d3-9583-44a9-9514-60ab8bd60b4d,ecc86fea-399c-4b0d-b992-02621d44c000,99b3a9ae-161b-46f6-9f3d-b3ceeb0d5aa8,d32facb4-dc91-4193-baa2-42a4d92432da","receiveUserName":"李红英","receiveCertNo":null,"receiveTime":1565088701000,"approveDeptRegion":null,"subDeptRegion":null,"receiveMemo":"","creater":"ckry","createTime":1565088701000,"modifier":null,"modifyTime":null,"receiveUserMobile":"15931912705","serviceAddress":null,"documentNum":null,"receiptType":"2","fileUrl":null,"documentName":null,"rootOrgId":null},{"receiveId":"0caa1263-4f55-40a4-9459-88faac7954d6","applyinstId":"307c74b6-2071-456b-bc58-8581c185b826","outinstId":"13d9f3d3-9583-44a9-9514-60ab8bd60b4d,ecc86fea-399c-4b0d-b992-02621d44c000,99b3a9ae-161b-46f6-9f3d-b3ceeb0d5aa8,d32facb4-dc91-4193-baa2-42a4d92432da","receiveUserName":"李红英","receiveCertNo":null,"receiveTime":1565088701000,"approveDeptRegion":null,"subDeptRegion":null,"receiveMemo":"","creater":"ckry","createTime":1565088701000,"modifier":null,"modifyTime":null,"receiveUserMobile":"15931912705","serviceAddress":null,"documentNum":null,"receiptType":"1","fileUrl":null,"documentName":null,"rootOrgId":null}],"isSeries":"0"},{"applyinstId":"307c74b6-2071-456b-bc58-8581c185b826","applyinstCode":"2019080618516841","name":"立项用地许可阶段","receiveList":[{"receiveId":"02085df1-54da-4fdc-8af5-5bed5de01018","applyinstId":"307c74b6-2071-456b-bc58-8581c185b826","outinstId":"13d9f3d3-9583-44a9-9514-60ab8bd60b4d,ecc86fea-399c-4b0d-b992-02621d44c000,99b3a9ae-161b-46f6-9f3d-b3ceeb0d5aa8,d32facb4-dc91-4193-baa2-42a4d92432da","receiveUserName":"李红英","receiveCertNo":null,"receiveTime":1565088701000,"approveDeptRegion":null,"subDeptRegion":null,"receiveMemo":"","creater":"ckry","createTime":1565088701000,"modifier":null,"modifyTime":null,"receiveUserMobile":"15931912705","serviceAddress":null,"documentNum":null,"receiptType":"2","fileUrl":null,"documentName":null,"rootOrgId":null},{"receiveId":"0caa1263-4f55-40a4-9459-88faac7954d6","applyinstId":"307c74b6-2071-456b-bc58-8581c185b826","outinstId":"13d9f3d3-9583-44a9-9514-60ab8bd60b4d,ecc86fea-399c-4b0d-b992-02621d44c000,99b3a9ae-161b-46f6-9f3d-b3ceeb0d5aa8,d32facb4-dc91-4193-baa2-42a4d92432da","receiveUserName":"李红英","receiveCertNo":null,"receiveTime":1565088701000,"approveDeptRegion":null,"subDeptRegion":null,"receiveMemo":"","creater":"ckry","createTime":1565088701000,"modifier":null,"modifyTime":null,"receiveUserMobile":"15931912705","serviceAddress":null,"documentNum":null,"receiptType":"1","fileUrl":null,"documentName":null,"rootOrgId":null}],"isSeries":"0"}],
      receiveModalShow: false,//回执弹窗控制
      commentInfo: {
        id:'',
        comment: '',
        sort: ''
      },
      commentsList: [], // 常用意见列表
      selComment: '', // 已选常用意见
      comments: '', // 已填审批意见
      treeRightBtnList: false, // 项目树是否展示右键操作按钮
      treeBtnTop: '',
      treeBtnLeft: '',
      childProjText: '', // 子项目工程备注
      childProjName: '', // 子项目工程名称
      parentProjInfoId: '', // 父工程id
      loadingLinkMan: false,
      stageId: '',  // 所选阶段ID
      applyinstIds: [], // 申请实例id
      stateIds: [], // 情形id集合
      getPaperAll: false,
      getCopyAll: false,
      provinceList: [], // 所有的省份信息
      cityList: [], // 所有市区信息
      countyList: [], // 所有地区信息
      smsInfoId: '', // 领证人id
      colStatusActive: 0, // 事项一单清 纵向状态
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
      selectMat: '',//选择的材料
      coreItemQuestionFlag: false, // 单项事项是否包含情形
      parallelItemsQuestionFlag: true, // 并联事项是否包含情形
      showCoreItemsKey: [], // 单项事项展开展示情形数组
      showParallelItemsKey: [], // 并联事项展开情形数组
      propulsionItemStateIds: [], // 并行事项情形Map集合
      parallelItemStateIds: [], // 简单合并申报，选择的并联事项情形
      uploadPercentage: 0, // 进度条百分比

      showFileListKey: [], // 展示材料下文件列表

      rulesResultForm: { // 办证结果取件方式信息校验
        addresseeName: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        addresseePhone: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        addresseeIdcard:[
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        receiveMode: [
          { required: true, message:"必选",trigger: 'change' },
        ],
      },
      rulesResultForm1: {
        addresseeName: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        addresseePhone: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        addresseeIdcard:[
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        receiveMode: [
          { required: true, message:"必选",trigger: 'change' },
        ],
        smsType: [
          { required: true, message:"必选",trigger: 'change' },
        ],
        addresseeProvince: [
          { required: true,validator: checkMissValue, trigger: 'change' },
        ],
        addresseeCity: [
          { required: true,validator: checkMissValue, trigger: 'change' },
        ],
        addresseeCounty: [
          { required: true,validator: checkMissValue, trigger: 'chenge' },
        ],
        addresseeAddr: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
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
      shareFileList: [], // 共享材料列表
      uploadTableData: [],
      selCoreItems: {}, // 选择分局承办的事项
      progressIntervalStop: false, // 定时器
      progressDialogVisible: false, // 是否显示进度条loading
      searchFileListfilter: '', // 导入材料附件列表搜索关键字
      oneFormData: [], // 一张表单列表
      oneFormDialogVisible: false, // 是否展示一张表单
      firstGetneForm: 0,
      showMoreBaseInfo: false, // 是否展示更多项目基本信息
      removeParallelItemsMats: [],  // 移除的并联事项材料
      isAddParallelItemsMats: true, // 是否添加并联材料
      showMatTableExpand: false, // 材料列表是否展示expand
      flashAttributes: '',
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
      pdfSrc: '', // 回执预览地址
      showUnitMore: [], // 展示更多单位信息
      sloading:false,//补全遮罩
      //材料补全dialog
      isShowMatmend:false,
      // 接口获取到的补全的数据
      matMendDialogData: {},
      // 材料不全form
      matMendForm:{
        applyinstCode: "",
        approveOrgName: "",
        iteminstName: "",
        localCode: "",
        owner: "",
        projInfoName: "",
        correctDueDays: '',
      },
      // 是否展示待补全材料清单
      inSeletcedMaterialPandel: true,
      // 待补全材料-补全
      matCorrectDtos:[],
      // 已提交材料-补全
      submittedMats:[],
      supplementFormRules: {
        correctDueDays: [{required: true, trigger: blur, message: '请输入补全办理时限'}]
      },
      // isMend:true,//是否可以材料补全
      filePreviewCount: 0, // 查询预览是否成功次数
      parallelApplyinstId: '', // 并联申报实例id
      IsJustApplyinst: 0, //
      projInfoByKeyword: '', // 模糊查询项目Keyword
      itemVerIdsString: '', // 并联事项不包含情形时 实施事项版本id集合 逗号隔开
      itemVerIdsStringAll: [], // 并联事项所有实施id
      addChildLoading: false, // 新增子工程按钮loading
      unitIdList: [], // 已查是否黑名单建设单位id集合
      isBlack: false, // 是否黑名单
      loseCreditCount: 0, // 失信条数
      creditTypeList: [], // 信用类型数据
      creditDiaVisible: false, // 是否展示信用弹窗
      creditDiaLoading: false, // 是否展示信用弹窗loading
      attIsRequireFlag: true, // 电子件已通过
      parallelLoading: false, // 并联事项loading
      noptItemShowNum: 0, // 并联事项展示条数 等于0展示全部  大于0展示对应数量 超过隐藏
      optItemShowNum: 0, // 并联事项展示条数 等于0展示全部  大于0展示对应数量 超过隐藏
      parallelItemsShowCount: 0, // 并联事项展示的数量
      coreItemsShowCount: 0, // 并行事项展示的数量
      gbhySelectData: [], // 国标行业下拉选项数据
      gbhyProp: {
        children: 'children',
        label: 'itemName'
      }, // 树配置信息，节点属性以及显示文案的属性
      gbhyShowMsg: '', // 国标行业选中数据的展示
      isShowGbhy: false, // 是否显示国标行业tree模块
      formItemsIdStr: '', // 一张表单itemids参数
    }
  },
  mounted: function () {
    var _that = this;
    _that.getThemeInfo('0');
    _that.getThemeInfo('1');
    _that.getDistrictList();  // 获取行政区划
    _that.getGbhy();
    _that.getProjTypeNature('XM_DWLX,XM_PROJECT_STEP,XM_PROJECT_LEVEL,XM_TZLX,XM_ZJLY,XM_GBHY,XM_TDLY,XM_GCFL,XM_NATURE');
    window.addEventListener('scroll',_that.handleScroll);
    window.addEventListener('resize', function (ev) {
      _that.curWidth=(document.documentElement.clientWidth || document.body.clientWidth);
    });
    // _that.$nextTick(function() {
    //   this.$refs.searchProjInfo.$el.querySelector('input').click()
    // })
    if(parent.vm&&parent.vm.activeTabSelData){
      if(parent.vm.activeTabSelData.projInfoId){
        _that.projInfoId = parent.vm.activeTabSelData.projInfoId;
        _that.linkQuery();
      }
    }

    // 获取项目id，存在即编辑状态，需要初始化数据
    _that.projInfoId = _that.QueryString('projInfoId');
    if (_that.projInfoId) {
      _that.isEdit = true;
      _that.searchProjAllInfo();
    } else {
      _that.isEdit = false;
    }
  },
  watch: {
    searchProjfilterText: function(val) {
      this.$refs.projTree.filter(val);
    },
    searchOrgfilterText: function(val) {
      this.$refs.orgTree.filter(val);
    },
    searchFileListfilter: function (val) {
      this.searchShareFileList(this.selMatRowData)
    }
  },
  methods: {
    // 自动生成项目代码
    autoCreateCode: function() {
      var _that = this;

      if (!_that.projBascInfoShow.projName) {
        _that.$message.error('请输入项目/工程名称');
        return false;
      }

      request('', {
        url: ctx + 'rest/project/save/zbm',
        type: 'post',
        data: {
          projName: _that.projBascInfoShow.projName
        },
      }, function (res) {
        if (res.success) {
          vm.$set(_that.projBascInfoShow, 'localCode', res.content.localCode);
          vm.$set(_that.projBascInfoShow, 'gcbm', res.content.gcbm);
          vm.$set(_that.projBascInfoShow, 'projInfoId', res.content.projInfoId);

          vm.projInfoId = res.content.projInfoId;
        }
      }, function (msg) {})
    },
    // 获取页面的URL参数
    QueryString: function(val) {
        var svalue = location.search.match(new RegExp("[\?\&]" + val + "=([^\&]*)(\&?)", "i"));
        return svalue ? svalue[1] : svalue;
    },
    closeGbhyTree: function() {
      this.isShowGbhy = false;
    },
    getGbhy: function() {
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
        }
      }, function (msg) {})
    },
    handleCheckChange: function(data, checked, indeterminate) {
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
    // 设置并联并行事项展示条数
    setItemShowLen: function(){
      var _that = this;
      var parallelEle = $('#parallelItemsTable .el-table__body tr');
      var coreEle = $('#coreItemsTable .el-table__body tr');
      if(this.noptItemShowNum > 0){
        this.parallelItemsShowCount = this.noptItemShowNum;
      }else {
        this.parallelItemsShowCount = this.parallelItems.length;
      }
      if(this.optItemShowNum > 0){
        this.coreItemsShowCount = this.optItemShowNum;
      }else {
        this.coreItemsShowCount = this.coreItems.length;
      }
      if(this.parallelItems.length == this.parallelItemsShowCount){
        parallelEle.removeClass('hide');
      }else {
        parallelEle.map(function (index,item) {
          if(index+1>_that.parallelItemsShowCount){
            $(item).addClass('hide');
          }else {
            $(item).removeClass('hide');
          }
        });
      }
      if(this.coreItems.length == this.coreItemsShowCount){
        coreEle.removeClass('hide');
      }else {
        coreEle.map(function (index,item) {
          if(index+1>_that.coreItemsShowCount){
            $(item).addClass('hide');
          }else {
            $(item).removeClass('hide');
          }
        });
      }
    },
    changeItenShowLen: function(flag){  // flag: parallel 并联
      var _that = this;
      var parallelEle = $('#parallelItemsTable .el-table__body tr');
      var coreEle = $('#coreItemsTable .el-table__body tr');
      if(flag=='parallel'){
        this.parallelItemsShowCount = (this.noptItemShowNum == this.parallelItemsShowCount)?this.parallelItems.length:this.noptItemShowNum;
        if(this.parallelItems.length == this.parallelItemsShowCount){
          parallelEle.removeClass('hide');
        }else {
          parallelEle.map(function (index,item) {
            if(index+1>_that.parallelItemsShowCount){
              $(item).addClass('hide');
            }else {
              $(item).removeClass('hide');
            }
          });
        }
      }else {
        this.coreItemsShowCount = (this.optItemShowNum == this.coreItemsShowCount)?this.coreItems.length:this.optItemShowNum;
        if(this.coreItems.length == this.coreItemsShowCount){
          coreEle.removeClass('hide');
        }else {
          coreEle.map(function (index,item) {
            if(index+1>_that.coreItemsShowCount){
              $(item).addClass('hide');
            }else {
              $(item).removeClass('hide');
            }
          });
        }
      }
    },
    // 选择切换申办主体单位
    selUnitInfo: function(val,flag, index){ // val选中单位信息 flag 单位类型（jingban,jianshe） index单位索引
      console.log(val,flag);
      if(flag=='jingban'){
        this.agentUnits[index] = val;
      }else {
        this.jiansheFrom[index] = val;
      }
      this.unitOrUserIsBlack(val);
    },
    // 展示信用记录弹窗
    creditDiaVisibleShow: function(data){
      this.creditTypeList = data.creditTypeList;
      this.loseCreditCount = data.loseCreditCount;
      this.creditDiaVisible = true;
      this.isBlack = data.isBlack;
    },
    // 判断是否为企业或个人是否黑名单
    unitOrUserIsBlack: function(reqData){
      var _that = this, _bizId = '', _bizType='';
      if(_that.applySubjectType==0){
        _bizId = reqData.applyLinkmanId;
        _bizType = 'l';
      }else {
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
          if(result.content&&result.content.length>0){
            if(typeof reqData.isBlack == "undefined"){
              Vue.set(reqData,'isBlack',true);
            }else {
              reqData.isBlack = true;
            }
          }else {
            if(typeof reqData.isBlack == "undefined"){
              Vue.set(reqData,'isBlack',false);
            }else {
              reqData.isBlack = false;
            }
          }
          _that.listCreditSummaryDetailByBizId(reqData,_bizId,_bizType);
        }
      }, function (msg) {})

    },
    // 通过企业或者联系人id获取汇总与详情信息数据
    listCreditSummaryDetailByBizId: function(reqData,_bizId,_bizType){
      // creditDiaLoading
      var _that = this;
      _that.creditDiaLoading = true;
      request('', {
        url: ctx + 'aea/credit/redblack/listCreditSummaryDetailByBizId',
        type: 'get',
        data: {bizId: _bizId, bizType: _bizType},
      }, function (result) {
        _that.creditDiaLoading = false;
        if (result.success&&result.content) {
          if(typeof reqData.creditTypeList == "undefined"){
            Vue.set(reqData,'creditTypeList',result.content);
          }else {
            reqData.creditTypeList = result.content;
          }
          if(typeof reqData.loseCreditCount == "undefined"){
            Vue.set(reqData,'loseCreditCount',0);
          }else {
            reqData.loseCreditCount = 0;
          }
          reqData.creditTypeList.map(function(item){
            if(item.itemCode=='bad'&&item.summaries.length>0){
              reqData.loseCreditCount = item.summaries.length;
            }
            console.log(reqData.loseCreditCount)
            item.summaries.map(function(itemSum){
              if(typeof itemSum.isOpen=='undefined'){
                Vue.set(itemSum, 'isOpen', false);
              }else{
                itemSum.isOpen = false;
              }

            })
          });
          if(reqData.isBlack==1){
            _that.creditDiaVisibleShow(reqData);
          };
        }
      }, function (msg) {
        _that.creditDiaLoading = false;
      })
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
        if (scrollTop >= ($(ele[i]).offset().top-60)) {
          _that.activeTab = i
        }
      }
    },
    // keyword模糊查询项目信息
    searchProjInfoByKeyword: function (queryString) {
      var _that = this;
      _that.projInfoByKeyword=queryString;
      _that.projInfoId = '';
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
    createFilter:function(queryString) {
      return function(projNameSelect) {
        return (projNameSelect.value.toLowerCase());
      };
    },
    // 选择要查的项目
    projNameSel: function(item){
      this.projInfoId = item.projInfoId;
      this.projName = item.projName;
      this.linkQuery();
    },
    // 清空数据
    clearSearchData: function(){
      this.selTheme={};
      this.stateList=[];
      this.parallelItems=[];
      this.coreItems=[];
      this.model.matsTableData=[];
      this.parallelApplyinstId = '';
    },
    // 获取项目详细信息
    searchProjAllInfo: function(){
      var _that = this;
      _that.loading = true;
      _that.clearSearchData();
      request('', {
        url: ctx+'rest/project/one/'+_that.projInfoId,
        type: 'get',
      }, function (data) {
        if(data.success){
          if(data.content){
            var result = data.content;
            _that.searchKeyword = result.localCode;
            _that.showMoreProjInfo = true;
            _that.showVerLen = _that.verticalTabData.length;
            _that.projBascInfoShow = result; // 项目主要信息
            _that.getProjThemeIdList();
            _that.themeId = result.themeId;
            _that.themeType = result.themeType;
            _that.applySubjectType = Number(result.applySubjectType); // 申办主体类型

            if (!_that.projBascInfoShow.isAreaEstimate) _that.projBascInfoShow.isAreaEstimate = '0';
            if (!_that.projBascInfoShow.isDesignSolution) _that.projBascInfoShow.isDesignSolution = '0';
            if (!_that.projBascInfoShow.gbCodeYear) _that.projBascInfoShow.gbCodeYear = '2017';
            if (!!_that.projBascInfoShow.projectAddress) _that.projBascInfoShow.projectAddress = _that.projBascInfoShow.projectAddress.split(',');
            if (!!_that.projBascInfoShow.theIndustry) _that.$refs.gbhy.setCheckedKeys(_that.projBascInfoShow.theIndustry.split(','));
            if(result.personalApplicant){
              _that.applyPersonFrom = result.personalApplicant; // 个人申报主体信息
            }
            if(result.buildUnits){
              _that.buildUnits = result.buildUnits; // 企业申报主体信息
            }
            if(result.otherUnits){
              _that.otherUnits = result.otherUnits; // 个人申报主体信息
            }
            if(result.agentUnits){ // 存在经办单位
              _that.agentUnits = result.agentUnits;  // 经办单位信息
              if(result.agentUnits.length>0){
                _that.agentChecked = true; // 经办勾选
              }
            }else {
              _that.agentUnits = [];
              _that.agentChecked = false;
            }
            _that.setJiansheFrom();
            _that.getSelThemeInfo(_that.themeType,_that.themeId)
            _that.linkQuerySucc = true;
            // 判断项目是否无编码申报
            if((result.localCode.slice(0,3) == 'ZBM')){
              _that.projSelect = false;
              _that.approveNumClazz = false; // 不显示备案文号
            }else {
              _that.projSelect = true;
              _that.approveNumClazz = true; // 显示备案文号
            }
            // _that.getStageByThemeIdAndThemeStageId(_that.themeId,_that.projInfoId); // 获取阶段
          }
          _that.loading = false;
        }else {
          _that.showMoreProjInfo = false;
          _that.showVerLen = 1;
          _that.$message({
            message: data.message?data.message:'没有查到该项目信息！',
            type: 'error'
          });
          _that.loading = false;
        }
      }, function (msg) {
        _that.showMoreProjInfo = false;
        _that.showVerLen = 1;
        _that.loading = false;
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    // 联网查询
    linkQuery: function(){
      var _that = this;
      _that.showUnitMore=[]; // 清空企业展示更多信息列表
      if(_that.projInfoId){
        _that.searchProjAllInfo();
      }else {
        if(!_that.searchKeyword){
          _that.$message({
            message: '请输入项目（工程）的代码或名称！',
            type: 'error'
          });
        }else {
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
      if(this.applySubjectType == 1){  // 企业申报
        this.buildUnits.map(function(item){
          if(_that.unitIdList.indexOf(item.unitInfoId)<0) {
            _that.unitOrUserIsBlack(item);
          }
        })
        this.jiansheFrom = this.buildUnits;
        this.isJg = false;
        this.getSentPerInfo(this.buildUnits[0])
      }else if(this.applySubjectType == 2){ // 非企业申报
        this.otherUnits.map(function(item){
          if(_that.unitIdList.indexOf(item.unitInfoId)<0) {
            _that.unitOrUserIsBlack(item);
          }
        })
        this.jiansheFrom = this.otherUnits;
        this.isJg = true;
        this.getSentPerInfo(this.otherUnits[0]);
      }else { // 个人申报
        var perData = {
          linkmanName: this.applyPersonFrom.applyLinkmanName,
          linkmanId: this.applyPersonFrom.applyLinkmanId,
          linkmanMobilePhone: this.applyPersonFrom.applyLinkmanTel,
          linkmanCertNo: this.applyPersonFrom.applyLinkmanIdCard
        }
        this.getSentPerInfo(perData);
        if(_that.unitIdList.indexOf(this.applyPersonFrom.applyLinkmanId)<0){
          this.unitOrUserIsBlack(this.applyPersonFrom)
        }
      }
      this.rootUnitInfoId = '';
      this.rootLinkmanInfoId = '';
      this.rootApplyLinkmanId = '';
      if(this.jiansheFrom.length>0) {
        this.rootUnitInfoId = this.jiansheFrom[0].unitInfoId;
        this.rootLinkmanInfoId = this.jiansheFrom[0].linkmanId
      }
      if(this.agentUnits.length>0){
        this.rootUnitInfoId = this.agentUnits[0].unitInfoId;
        this.rootLinkmanInfoId = this.agentUnits[0].linkmanId;
      }
      if(this.applySubjectType == 0&&this.applyPersonFrom.applyLinkmanId){
        this.rootApplyLinkmanId = this.applyPersonFrom.applyLinkmanId
      };
    },
    // 切换申报主体类型
    changeApplySubjectSelect: function (val) { // 申办主题类型切换事件
      this.setJiansheFrom();
      this.showUnitMore=[];
    },
    // 查询主题信息
    getThemeInfo: function (category) {
      var _that = this;
      request('', {
        url: ctx + 'rest/theme/getMainThemeTypeCategory/'+category,
        type: 'get',
      }, function (data) {
        if (data.success) {
          if(category==1){
            data.content.map(function(item){
              if(item){
                _that.themeList.push(item);
              }
            });
          }else {
            _that.themeList[0].themesList=_that.sortByKey(data.content,'sortNo');
          }
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    // 获取已选主题
    getSelThemeInfo: function(themeType,themeId,flag){
      var _that = this;
      _that.selThemeActive = -1;
      _that.selThemeActive1 = -1;
      _that.themeList.forEach(function(typeItem,index){
        if(index==0){
          _that.parallelThemeList=typeItem.themesList;
        }else {
          if(typeItem.themeType == themeType){
            _that.themeInfoList = typeItem.themes;
          }
        }
      })
      if(flag=='single'){
        var themeLen = _that.themeInfoList.length;
        for(var i=0;i<themeLen;i++){
          if(_that.themeInfoList[i].themeId == themeId){
            _that.selThemeActive1 = i;
            // _that.chooseTheme(_that.themeInfoList[i],i,flag);
            return true;
          }
        }
      }else {
        var pData = _that.parallelThemeList;
        var pDataLen = pData.length;
        for(var j=0;j<pDataLen;j++){
          if(pData[j].themeType == themeType){
            _that.themeTypeIndex = 0;
            for(var i=0;i<pData[j].themes.length;i++){
              if(pData[j].themes[i].themeId==themeId){
                _that.selTheme = pData[j].themes[i];
                _that.selThemeActive = i;
                // _that.chooseTheme(pData[j].themes[i],i,flag);
                return true;
              }else {
                _that.selThemeActive = -1;
              }
            }
            return true;
          }else {
            if(themeId){
              _that.themeTypeIndex = _that.themeList.length-1;
            }
            _that.getSelThemeInfo(themeType,themeId,'single');
          }
        }
      }
    },
    // 获取项目性质项目类型
    getProjTypeNature: function (code) {
      var _that = this;
      request('', {
        url: ctx + 'rest/dict/code/multi/items/list',
        type: 'get',
        data: {'dicCodeTypeCodes': code}
      }, function (result) {
        if (result.content) {
          _that.projTypeList = result.content.XM_PROJECT_STEP;
          _that.projLevelList=result.content.XM_PROJECT_LEVEL;
          _that.tzlxList=result.content.XM_TZLX;
          _that.zjlyList=result.content.XM_ZJLY;
          _that.gbhyList=result.content.XM_GBHY;
          _that.tdlyList=result.content.XM_TDLY;
          _that.jsxzList=result.content.XM_NATURE;
          _that.gcflList=result.content.XM_GCFL;
          _that.xmdwlxList = result.content.XM_DWLX;
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
      if(_that.projInfoId == 'undefined' || _that.projInfoId == '' || _that.projInfoId == null){
        alertMsg('', '请先联网查询项目', '关闭', 'error', true);
      }else {
        request('', {
          url: ctx + 'rest/project/tree/'+_that.projInfoId,
          type: 'get',
        }, function (result) {
          if (result.success) {
            _that.projectTreeInfoModal = true;
            _that.projTree = _that.toTree(result.content);
          }else {
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
    filterProjNode: function(value,data){
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    // 清空筛选项目树条件
    clearProjfilterText: function () {
      this.searchProjfilterText = '';
    },
    // 获取办证结果领取人信息
    getSentPerInfo: function (data) {
      if(data){
        this.getResultForm.addresseeName = data.linkmanName;
        this.getResultForm.addresseePhone = data.linkmanMobilePhone;
        this.getResultForm.addresseeIdcard = data.linkmanCertNo;
      }else {
        this.getResultForm.addresseeName = '';
        this.getResultForm.addresseePhone = '';
        this.getResultForm.addresseeIdcard = '';
      }
    },
    // 编辑保存项目基本信息
    saveOrUpdateProjFrom: function (isParallel) {
      var _that = this;
      var props = JSON.parse(JSON.stringify(_that.projBascInfoShow));
      props.projectAddress = props.projectAddress.join(',');

      var url = '';
      // 存在项目id为编辑状态，反之新增状态
      if (_that.projInfoId) {
        url = ctx + 'rest/project/edit';
      } else {
        url = ctx + 'rest/project/save';
      }
      _that.$refs['projBascInfoShowFrom'].validate(function (valid) {
        if(valid){
          request('', {
            url: url,
            data: props,
            type: 'post'
          }, function (data) {
            if(data.success){
              // 保存成功后显示‘申报主体信息’模块
              _that.showVerLen = _that.verticalTabData.length;
              _that.showMoreProjInfo = true;
              
              _that.$message({
                message: '信息保存成功',
                type: 'success'
              });
            }else {
              _that.$message({
                message: data.message ? data.message : '信息保存失败',
                type: 'error'
              });
            }
          },function(msg){
            _that.$message({
              message: msg.message ? msg.message : '服务请求失败',
              type: 'error'
            });
          })
        }else {
          _that.$message({
            message: '请完善项目/工程信息',
            type: 'error'
          });
        }
      });

    },
    // 新增单位信息
    addUnitInfoForm: function(isOwner){
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
      };
      if(isOwner=="1"){  // 新增建设单位
        _that.jiansheFrom.push(obj);
      }else {
        _that.agentUnits.push(obj);
      }
    },
    // 删除单位信息
    delUnitInfoProj: function(data,index,flag){
      var _that = this;
      var unitProjIdFlag = data.unitInfoId && _that.projInfoId;
      var applyinstId = data.applyinstId;
      var unitInfoId = data.unitInfoId;
      if(applyinstId||!unitProjIdFlag){
        if(flag=='jingban'){
          _that.agentUnits.splice(index,1);
        }else {
          _that.jiansheFrom.splice(index,1);
        }
        return;
      }
      confirmMsg('提示信息：','你确定要删除该单位吗？',function(){
        _that.loading = true;
        request('', {
          url: ctx + "rest/unit/delete",
          type: 'post',
          data: {unitInfoId: unitInfoId,projInfoId: _that.projInfoId}
        }, function (data) {
          if(data.success){
            _that.$message({
              message: '删除成功',
              type: 'success'
            });
            if(flag=='jingban'){
              _that.agentUnits.splice(index,1);
            }else {
              _that.jiansheFrom.splice(index,1);
            }
            _that.loading = false;
          }else {
            _that.$message({
              message: data.message ? data.message : '删除失败',
              type: 'error'
            });
            _that.loading = false;
          }
        },function(msg){
          _that.$message({
            message: msg.message ? msg.message : '删除失败',
            type: 'error'
          });
          _that.loading = false;
        })
      },function(){},'确定','取消','warning',true)

    },
    // 保存编辑新增单位
    saveProjinfoids: function(props,index,flag){
      var _that = this;
      var url,msg;
      var projInfoIds=[];
      props.projInfoId = _that.projInfoId;
      props.linkmanInfoId = props.linkmanId;
      projInfoIds.push(_that.projInfoId);
      props.projInfoIds = projInfoIds.join(',')
      if(props.unitInfoId){
        url = 'rest/unit/edit';
        msg = '单位信息保存成功';
      }else {
        url = 'rest/unit/save'
        msg = '单位信息保存成功';
      }
      var formRef=flag+index;
      var validFun;
      if((typeof(_that.$refs[formRef].validate)) == 'function' ){
        validFun = _that.$refs[formRef].validate
      }else {
        validFun = _that.$refs[formRef][0].validate
      }
      validFun(function (valid){
        if(valid){ // idcard idtype
          _that.loading = true;
          request('', {
            url: ctx + url,
            type: 'post',
            data: props
          }, function (data) {
            if(data.success){
              _that.$message({
                message: msg,
                type: 'success'
              });
              props.unitInfoId = data.content;
              if(_that.unitIdList.indexOf(props.unitInfoId)<0) {
                _that.unitOrUserIsBlack(props);
              }
              _that.loading = false;
            }else {
              _that.$message({
                message: data.message ? data.message : '保存失败',
                type: 'error'
              });
              _that.loading = false;
            }
          },function(msg){
            _that.$message({
              message: '服务请求失败',
              type: 'error'
            });
            _that.loading = false;
          })
        }else {
          _that.$message({
            message: '请完善企业信息！',
            type: 'error'
          });
        }
      });
    },
    // 根据项目ID查找关联的单位列表
    getUnitsListByProjInfoId: function(){
      var _that = this;
      _that.loading = true;
      request('', {
        url: ctx + 'rest/unit/list/by/'+_that.projInfoId,
        type: 'get',
      }, function (result) {
        if(result.success){
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
    addLinkman: function(data,parData){
      var _that = this;
      _that.addEditManModalShow = true;
      _that.getUnitsListByProjInfoId();
      _that.addEditManPerform = parData
      if(_that.projInfoId){
        if(data){
          _that.addEditManModalTitle = '编辑联系人';
          _that.addEditManModalFlag = 'edit';
          if(!data.linkmanId){
            alertMsg('提示信息', '请选择联系人！', '关闭', 'error', true);
            return;
          }else{
            _that.backDLinkmanInfo(data,parData);
          }
        }else {
          _that.addEditManModalTitle = '新增联系人';
          _that.addEditManform = {};
          _that.addEditManModalFlag = 'add';
          if(parData.unitInfoId){
            _that.addEditManform.unitInfoId = parData.unitInfoId;
            _that.addEditManform.unitName = parData.applicant;
          }else {
            _that.addEditManform.unitInfoId = '';
            _that.addEditManform.unitName = parData.applicant
          }
        }
      }else {
        alertMsg('', '请先查出项目信息！', '关闭', 'error', true);
      }
    },
    // 反显联系人信息
    backDLinkmanInfo: function(data,parData){
      var _that = this;
      if(data.linkmanId){
        request('', {
          url: ctx + 'rest/linkman/one/'+data.linkmanId,
          type: 'get'
        }, function (result) {
          if(result.success){
            _that.addEditManform = result.content;
            _that.addEditManform.unitInfoId = parData.unitInfoId;
            _that.addEditManform.unitName = parData.applicant;
          }
        }, function (msg) {
          alertMsg('', '服务请求失败', '关闭', 'error', true);
        });
      }else{
        _that.aeaLinkmanInfoList = {};
      }
    },
    // 保存联系人信息
    saveAeaLinkmanInfo: function (linkmanType) {
      var _that = this;
      _that.addEditManform.linkmanType=linkmanType
      _that.$refs['addEditManform'].validate(function (valid) {
        if (valid) {
          _that.loading = true;
          var url, msg;
          if(_that.addEditManModalFlag=='edit'){
            url = 'rest/linkman/edit';
            msg = '编辑联系人信息保存成功';
          }else {
            url = 'rest/linkman/save'
            msg = '新增联系人信息保存成功';
          }
          request('', {
            url: ctx + url,
            type: 'post',
            data: _that.addEditManform
          }, function (result) {
            if(result.success){
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
              message: msg.message?msg.message:'保存失败！',
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
    resetForm: function(formName) {
      this.$refs[formName].resetFields();
      this.selectMat = '';
    },
    // 联系人模糊查询
    querySearchLinkMan: function(queryString, cb){
      var _that = this;
      if (typeof (queryString) != 'undefined') queryString = queryString.trim();
      if (queryString != '' && queryString.length >= 1) {
        request('', {
          url: ctx + 'rest/linkman/list',
          type: 'get',
          data: {"keyword": queryString,"projInfoId": _that.projInfoId},
        }, function (result) {
          if (result.success) {
            _that.projNameSelect = result.content;
            _that.projNameSelect.map(function (item) {
              if(item){
                Vue.set(item, 'value', item.addressName);
              }
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
    querySearchApplyLinkMan: function(item) {
      this.applyPersonFrom.applyLinkmanName = item.addressName;
      this.applyPersonFrom.applyLinkmanIdCard = item.addressIdCard;
      this.applyPersonFrom.applyLinkmanTel = item.addressPhone;
      this.applyPersonFrom.applyLinkmanEmail = item.addressMail;
      this.applyPersonFrom.applyLinkmanId = item.addressId;
    },
    querySearchLinkLinkMan: function(item) {
      this.applyPersonFrom.linkLinkmanName = item.addressName;
      this.applyPersonFrom.linkLinkmanIdCard = item.addressIdCard;
      this.applyPersonFrom.linkLinkmanTel = item.addressPhone;
      this.applyPersonFrom.linkLinkmanEmail = item.addressMail;
      this.applyPersonFrom.linkLinkmanId = item.addressId;
    },
    // 联系人信息是否与申请人一致
    copyApplyLinkmanInfo: function (val) {
      if(this.sameAsApplyLink){
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
      if(flag){ // 清空申请人信息
        this.applyPersonFrom.applyLinkmanName = '';
        this.applyPersonFrom.applyLinkmanIdCard = '';
        this.applyPersonFrom.applyLinkmanTel = '';
        this.applyPersonFrom.applyLinkmanEmail = '';
        this.applyPersonFrom.applyLinkmanId = '';
      }else { // 清空联系人信息
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
      var personalInfos={personalInfos:[
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
      ]};
      _that.$refs['applicantPer'].validate(function (valid) {
        if (valid) {
          _that.loading = true;
          request('', {
            url: ctx + 'rest/linkman/save/personal',
            type: 'post',
            ContentType: 'application/json',
            data: JSON.stringify(personalInfos)
          }, function (result) {
            if(result.success){
              _that.applyPersonFrom.applyLinkmanId = result.content.applyInfoId;
              _that.applyPersonFrom.linkLinkmanId = result.content.linkmanInfoId;
              _that.$message({
                message: '保存成功',
                type: 'success'
              });
              if(_that.unitIdList.indexOf(_that.applyPersonFrom.applyLinkmanId)<0) {
                _that.unitOrUserIsBlack(_that.applyPersonFrom);
              }
              _that.loading = false;
            }
          }, function (msg) {
            _that.$message({
              message: msg.message?msg.message:'保存失败！',
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
    querySearchJiansheName: function(queryString, cb){
      var _that = this;
      if (typeof (queryString) != 'undefined') queryString = queryString.trim();
      if (queryString != '' && queryString.length >= 2) {
        request('', {
          url: ctx + 'rest/unit/list',
          type: 'get',
          data: {"keyword": queryString,"projInfoId": _that.projInfoId},
        }, function (result) {
          if (result) {
            _that.projNameSelect = result.content;

            _that.projNameSelect.map(function (item) {
              if(item){
                Vue.set(item, 'value', item.applicant);
              }
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
    // 切换申报主题类型
    changeThemeType: function (item,index,flag) {
      var _that = this;
      if(index==0&&flag=='single'){
        flag='parallel';
        if(_that.selTheme.themeId){
          this.chooseTheme(_that.selTheme,0,flag);
        }else {
          _that.statusLineList = [];
          _that.statusList = [];
          _that.stateList = [];
          _that.parallelItems=[];
          _that.coreItems=[];
          _that.model.matsTableData=[];
          this.parallelApplyinstId = '';
        }
      }
      if(flag=='parallel'){
        this.getSelThemeInfo(this.themeType,this.themeId,flag);
        _that.themeInfoListP = item.themes;
        _that.themeType = item.themeType;
        this.themeDialogIndex = index;
        this.themeTypeIndex = 0;
      }else {
        this.getSelThemeInfo(this.themeType,this.themeId,flag);
        this.themeInfoList = item.themes;
        this.themeType = item.themeType;
        this.themeTypeIndex = 1;
        if(_that.selThemeActive1>-1){
          this.chooseTheme(this.themeInfoList[_that.selThemeActive1],_that.selThemeActive1,flag);
        }else {
          _that.statusLineList = [];
          _that.statusList = [];
          _that.stateList = [];
          _that.parallelItems=[];
          _that.coreItems=[];
          _that.model.matsTableData=[];
        }
      }
    },
    // 展示选择主题弹窗
    showSelThemeDialog: function () {
      this.selThemeDialogShow = true;
      this.changeThemeType(this.parallelThemeList[0],0,'parallel');
    },
    // 选择并保存主题
    chooseTheme: function (data,index,flag) {
      var themeId = data.themeId;
      var themeName = data.themeName;
      this.parallelApplyinstId = '';
      var _that =this;
      // confirmMsg('注意：选择主题后不可更改','是否选择主题”'+themeName+'“？',function(){
        if(flag=='parallel'){
          _that.selThemeActive = index;
        }else {
          _that.selThemeActive1 = index;
        }
        _that.themeId = themeId;
        _that.getSelThemeInfo(_that.themeType,_that.themeId,flag);
        _that.getStageByThemeIdAndThemeStageId(_that.themeId,_that.projInfoId);
        _that.selThemeDialogShow = false;
      // },function(){},'','','',true)
    },
    // 获取stage by themeId
    getStageByThemeIdAndThemeStageId: function (themeIdSel,projInfoId) {
      var _that = this;
      themeId = themeIdSel?themeIdSel:_that.themeId;
      _that.loading = true;
      request('', {
        url: ctx + 'rest/theme/stages/' + themeId,
        type: 'get',
        data: {themeId:themeId,projInfoId:projInfoId}
      }, function (result) {
        if (result.success) {
          _that.statusLineList = [];
          var mainLineStatus = [];
          var auxiliaryStatus = [];
          var technicalStatus = [];
          if(result.content){
            result.content.map(function(item){
              if(item){
                if(item.key=='main'){
                  mainLineStatus = item.stages;
                }else if(item.key=='help') {
                  auxiliaryStatus = item.stages;
                }else if(item.key=='check'){
                  technicalStatus = item.stages;
                }
              }
            });
          }
          if(mainLineStatus.length>0){
            _that.statusLineList.push({
              title:'主线阶段',
              statusList: mainLineStatus,
              name: 'mainLine'
            });
          }
          if(auxiliaryStatus.length>0){
            _that.statusLineList.push({
              title:'辅线服务',
              statusList: auxiliaryStatus,
              name: 'auxiliary'
            });
          }
          if (technicalStatus.length>0){
            _that.statusLineList.push({
              title:'技术审查',
              statusList: technicalStatus,
              name: 'technical'
            });
          }
          if(_that.statusLineList.length==0){
            _that.statusLineList.push({
              title:'并联阶段',
              statusList: mainLineStatus,
              name: 'mainLine'
            });
          }
          _that.statusList = _that.statusLineList[0].statusList;
          var _stageId = '';
          var _isSelItem = '';
          if(_that.statusList.length>0){
            _that.statusList.map(function (item) { // 图片切换为新的图片
              Vue.set(item,'classIcon','');
              if(item.stageName.indexOf('工程')==0){
                item.bigImgPath = 'apply/imgs/工程.png';
                item.classIcon = 'purple-circle'
              }else if(item.stageName.indexOf('施工')==0){
                item.bigImgPath = 'apply/imgs/施工.png';
                item.classIcon = 'ye-circle'
              }else if(item.stageName.indexOf('竣工')==0){
                item.bigImgPath = 'apply/imgs/竣工.png';
                item.classIcon = 'gre-circle'
              }else {
                item.bigImgPath = 'apply/imgs/立项.png';
              }
            });
            _stageId = _that.statusList[0].stageId;
            _isSelItem = _that.statusList[0].isSelItem;
            _that.selStatus(_that.statusList[0],0,_stageId,_isSelItem); // 默认选择阶段1
          }else {
            _that.stageId = '';
            _that.stateList = [];
            _that.coreItems = [];
            _that.parallelItems = [];
          }
        }else {
          _that.$message({
            message: '获取阶段失败',
            type: 'error'
          });
        }
        _that.loading = false;
      }, function (msg) {
        _that.loading = false;
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    // 获取情形列表id
    getStatusListId: function(){
      var _that = this;
      var stateSel = _that.stateList;
      var stateListLen = stateSel.length;
      var selStateIds = [];
      for(var i=0;i<stateListLen;i++){  // 已选并联情形id集合
        if(stateSel[i].selectAnswerId!==undefined||stateSel[i].selectAnswerId!==null){
          // selStateIds=[];
          // return true;
          if(typeof(stateSel[i].selectAnswerId)=='object'){
            if(stateSel[i].selectAnswerId.length>0){
              selStateIds = selStateIds.concat(stateSel[i].selectAnswerId);
            }

          }else if(stateSel[i].selectAnswerId !== '') {
            selStateIds.push(stateSel[i].selectAnswerId);
          }
        }
      }
      selStateIds=selStateIds.filter(function(item, index) {
        //当前元素，在原始数组中的第一个索引==当前索引值，否则返回当前元素
        return selStateIds.indexOf(item, 0) === index
      })
      return selStateIds;
    },
    // 根据阶段获取情形和材料
    getStatusStateMats: function (pData,data,stageId,indexQues,flag,checkFlag) {
      var selStateIds = [];
      var parentStateId = data.parentStateId?data.parentStateId:'ROOT';
      var questionStateId = pData.parStateId?pData.parStateId:'';
      var stateParallelItems=data.stateParallelItems?data.stateParallelItems:[];
      var stateOptionItems=data.stateOptionItems?data.stateOptionItems:[];
      var _that = this;
      var _parentId = data.parStateId?data.parStateId:'ROOT';
      if(checkFlag==false){
        selStateIds = _that.getStatusListId();
        for (var i = 0;i< _that.stateList.length;i++) { // 清空情形下所对应情形
          var obj = _that.stateList[i];
          if(obj&&(obj.parentStateId == _parentId)||(obj&&obj.parentStateId!=null&&(selStateIds.indexOf(obj.parentStateId)==-1))){
            if(typeof(obj.selectAnswerId)=='object'&&obj.selectAnswerId.length>0){
              obj.selectAnswerId.map(function(itemSel){
                selStateIds=selStateIds.filter(function(item, index) {
                  return item !== itemSel;
                })
              });
            }else if(obj.selectAnswerId !== '') {
              selStateIds=selStateIds.filter(function(item, index) {
                return item !== obj.selectAnswerId;
              })
            }
            _that.stateList.splice(i, 1);
            i--
          }
        }
        selStateIds = _that.getStatusListId();
        // 清空对应情形下所有材料
        for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
          var obj = _that.model.matsTableData[i];
          if(obj && (obj._parStateId&&(obj._parStateId.indexOf(_parentId)>-1||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0)))){
            if(obj._parStateId.length==1){
              obj._parStateId = [];
              _that.model.matsTableData.splice(i, 1);
              i--
            }else {
              var index = obj._parStateId.indexOf(_parentId);
              if (index > -1) {
                obj._parStateId.splice(index, 1);
              }
            }
          }
        }
        // 清空情形下所对应并联事项
        for (var i = 0; i < _that.parallelItems.length; i++) {
          var obj = _that.parallelItems[i];
          if(obj && (obj._parStateId&&(obj._parStateId.indexOf(_parentId)>-1||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0)))){
            if(obj._parStateId.length==1){
              obj._parStateId = [];
              _that.parallelItems.splice(i, 1);
              i--
            }else {
              var index = obj._parStateId.indexOf(_parentId);
              if (index > -1) {
                obj._parStateId.splice(index, 1);
              }
            }

          }
        }
        // 清空情形下所对应并行事项
        for (var i = 0; i < _that.coreItems.length; i++) {
          var obj = _that.coreItems[i];
          if(obj && (obj._parStateId&&(obj._parStateId.indexOf(_parentId)>-1||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0)))){
            if(obj._parStateId.length==1){
              obj._parStateId = [];
              _that.coreItems.splice(i, 1);
              i--
            }else {
              var index = obj._parStateId.indexOf(_parentId);
              if (index > -1) {
                obj._parStateId.splice(index, 1);
              }
            }

          }
        }
        return false
      }
      request('', {
        url: ctx+'rest/mats/parallel/states/mats',
        type: 'get',
        data: {"stageId":stageId,"parentId":_parentId,"projInfoId":_that.projInfoId}
      }, function (data) {
        if(data.success){
          if(flag){
            _that.matIds = [];
            _that.stateList = data.content.questionStates;
            _that.model.matsTableData = _that.unique(data.content.stateMats,'mats');
            _that.popStateList = [];
            _that.model.matsTableData.map(function(item,index){
              if(item){
                _that.matIds.push(item.matId);
                if(item._parStateId=='undefined'||item._parStateId==undefined){
                  Vue.set(item, '_parStateId', [_parentId]);
                }else {
                  var parInd = item._parStateId.indexOf(_parentId)
                  if(parInd==-1){
                    item._parStateId.push(_parentId);
                  }
                }
                if(item._itemVerIds=='undefined'||item._itemVerIds==undefined){
                  Vue.set(item,'_itemVerIds',['ROOT']);
                }
              }
            });
            _that.stateList = _that.sortByKey(_that.stateList,'sortNo');
            _that.getStageItems(stageId); // 获取事项列表
          }else {
            if(checkFlag==true){
              data.content.questionStates.map(function(item,ind){ // 情形下插入对应的情形
                _that.stateList.splice((indexQues+1+ind),0,item);
              });
              console.log(_parentId);
              data.content.stateMats.map(function(item,index){
                if(item._parStateId=='undefined'||item._parStateId==undefined){
                  Vue.set(item, '_parStateId', [_parentId]);
                }else {
                  var parInd = item._parStateId.indexOf(_parentId)
                  if(parInd==-1){
                    item._parStateId.push(_parentId);
                  }
                }
              });
              // 当前情形附带的并行事项列表
              stateOptionItems.map(function(item,index){
                _that.setImplementItem(item);
                if(item._parStateId=='undefined'||item._parStateId==undefined){
                  Vue.set(item, '_parStateId', [_parentId]);
                }else {
                  var parInd = item._parStateId.indexOf(_parentId)
                  if(parInd==-1){
                    item._parStateId.push(_parentId);
                  }
                }
              });
              // 当前情形附带的并联事项列表
              stateParallelItems.map(function(item,index){
                _that.setImplementItem(item);
                if(item._parStateId=='undefined'||item._parStateId==undefined){
                  Vue.set(item, '_parStateId', [_parentId]);
                }else {
                  var parInd = item._parStateId.indexOf(_parentId)
                  if(parInd==-1){
                    item._parStateId.push(_parentId);
                  }

                }
              });
            }else {
              var stateListLen = _that.stateList.length;
              selStateIds = _that.getStatusListId();
              for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
                var obj = _that.stateList[i];
                if(obj&&(obj.parentQuestionStateId == questionStateId)||(obj&&obj.parentStateId!=null&&(selStateIds.indexOf(obj.parentStateId)==-1))){
                  if(typeof(obj.selectAnswerId)=='object'&&obj.selectAnswerId.length>0){
                    obj.selectAnswerId.map(function(itemSel){
                      selStateIds=selStateIds.filter(function(item, index) {
                        return item !== itemSel;
                      })
                    });
                  }else if(obj.selectAnswerId !== '') {
                    selStateIds=selStateIds.filter(function(item, index) {
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
                  if(typeof item.selValue == "undefined"){
                    Vue.set(item, 'selValue', []);
                  }else {
                    item.selValue = [];
                  }

                  item.selectAnswerId=item.selValue;
                }
                _that.stateList.splice((indexQues + 1 + ind), 0, item);
              });
              data.content.stateMats.map(function(item,index){
                if(item._parStateId=='undefined'||item._parStateId==undefined){
                  Vue.set(item, '_parStateId', [parentStateId]);
                }else {
                  var parInd = item._parStateId.indexOf(parentStateId)
                  if(parInd==-1){
                    item._parStateId.push(parentStateId);
                  }
                }
              });
              selStateIds = _that.getStatusListId();
              for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
                var obj = _that.model.matsTableData[i];
                if(obj._parStateId=='undefined'||obj._parStateId==undefined){
                  Vue.set(obj, '_parStateId', []);
                }else {
                  if(obj &&((obj._parStateId&&obj._parStateId.indexOf(parentStateId)>-1)||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0))){
                    if(obj._parStateId.length==1){
                      obj._parStateId = [];
                      _that.model.matsTableData.splice(i, 1);
                      i--
                    }else {
                      var index = obj._parStateId.indexOf(parentStateId);
                      if (index > -1) {
                        obj._parStateId.splice(index, 1);
                      }
                    }
                  }
                }
              }
              // 当前情形附带的并行事项列表
              stateOptionItems.map(function(item,index){
                _that.setImplementItem(item);
                if(item._parStateId=='undefined'||item._parStateId==undefined){
                  Vue.set(item, '_parStateId', [parentStateId]);
                }else {
                  var parInd = item._parStateId.indexOf(parentStateId)
                  if(parInd==-1){
                    item._parStateId.push(parentStateId);
                  }
                }
              });
              for (var i = 0; i < _that.coreItems.length; i++) { // 清空情形下所对应事项
                var obj = _that.coreItems[i];
                if(obj._parStateId=='undefined'||obj._parStateId==undefined){
                  Vue.set(obj, '_parStateId', []);
                }else {
                  if (obj && (obj._parStateId.indexOf(parentStateId)>-1||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0))) {
                    if(obj._parStateId.length==1){
                      obj._parStateId = [];
                      _that.coreItems.splice(i, 1);
                      i--
                    }else {
                      var index = obj._parStateId.indexOf(parentStateId);
                      if (index > -1) {
                        obj._parStateId.splice(index, 1);
                      }
                    }
                  }
                }
              };
              // 当前情形附带的并联事项列表
              stateParallelItems.map(function(item,index){
                _that.setImplementItem(item);
                if(item._parStateId=='undefined'||item._parStateId==undefined){
                  Vue.set(item, '_parStateId', [parentStateId]);
                }else {
                  var parInd = item._parStateId.indexOf(parentStateId)
                  if(parInd==-1){
                    item._parStateId.push(parentStateId);
                  }
                }
                if(_that.parallelItemsQuestionFlag==true){
                  _that.parallelItemsSelItem(_that.parallelItems,item,'autoGetSel');
                }
              });
              for (var i = 0; i < _that.parallelItems.length; i++) { // 清空情形下所对应事项
                var obj = _that.parallelItems[i];
                if(obj._parStateId=='undefined'||obj._parStateId==undefined){
                  Vue.set(obj, '_parStateId', []);
                }else {
                  if (obj && (obj._parStateId.indexOf(parentStateId)>-1||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0))) {
                    if(obj._parStateId.length==1){
                      obj._parStateId = [];
                      _that.parallelItems.splice(i, 1);
                      i--
                    }else {
                      var index = obj._parStateId.indexOf(parentStateId);
                      if (index > -1) {
                        obj._parStateId.splice(index, 1);
                      }
                    }
                  }
                }
              }
            }
          }
          _that.matIds = [];
          _that.stateList.map(function (item) {
            if(item.answerType!='s'&&item.answerType!='y'){
              if(typeof item.selValue == "undefined"){
                Vue.set(item, 'selValue', []);
                item.selectAnswerId = item.selValue;
              }
            }
            item.answerStates = _that.sortByKey(item.answerStates,'sortNo');
          })
          _that.coreItems= _that.unique(_that.coreItems.concat(stateOptionItems),'stage');
          _that.parallelItems= _that.unique(_that.parallelItems.concat(stateParallelItems),'stage');
          _that.model.matsTableData= _that.unique(_that.model.matsTableData.concat(data.content.stateMats),'mats');
          var matinstIdsObj = [];
          for(var i=0;i<_that.model.matsTableData.length;i++){
            var item = _that.model.matsTableData[i];
            if(item){
              if(item.children=='undefined'||item.children==undefined){
                Vue.set(item,'children',[]);
              }
              if(item.matinstId=='undefined'||item.matinstId==undefined){
                Vue.set(item,'matinstId','');
              }
              if(item.getPaper=='undefined'||item.getPaper==undefined){
                Vue.set(item,'getPaper','');
              }
              if(item.getCopy=='undefined'||item.getCopy==undefined){
                Vue.set(item,'getCopy','');
              }
              if(item.realPaperCount=='undefined'||item.realPaperCount==undefined){
                Vue.set(item,'realPaperCount',item.duePaperCount);
              }
              if(item.realCopyCount=='undefined'||item.realCopyCount==undefined){
                Vue.set(item,'realCopyCount',item.dueCopyCount);
              }
              if(matinstIdsObj.indexOf(item.matinstId)<0&&item.matinstId!=''){
                matinstIdsObj.push(item.matinstId);
              }
              _that.matIds.push(item.matId);
              if(item && (item._itemType=='coreItem')){ // 清空事项下材料
                if(typeof item._parStateId=='undefined'||item._parStateId.length==0||(item.parStateId&&selStateIds.indexOf(item.parStateId)<0)){
                  item._itemVerIds = [];
                  _that.model.matsTableData.splice(i, 1);
                  i--
                }

              }
            }
          }
          _that.sortByKey(_that.model.matsTableData,'sortNo');
          _that.ifMatsSelAll();
          _that.matinstIds = matinstIdsObj.join(',');
          // _that.parallelItemsSelAll(stateParallelItems);
          _that.$nextTick(function(){
            _that.toggleSelection(_that.parallelItems,'parallelItemsTable');
            if(_that.parallelItems){
              _that.parallelItemsSelAll(_that.parallelItems,'autoGetSel');
            }
            _that.setItemShowLen(); // 事项展示长度
          });
        }else {
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
    // 点击选择申报阶段
    selStatus: function (data,index,stageId,isSelItem) {
      this.isSelItem = isSelItem;
      this.statusActiveIndex = index;
      this.itemVerIdsStringAll = [];
      this.stageId = stageId;
      this.optItemShowNum = data.optItemShowNum; // 并行事项展示条数
      this.noptItemShowNum = data.noptItemShowNum; // 并联事项展示条数
      this.parallelApplyinstId = '';
      this.getStatusStateMats('','',stageId,'',true);  // 获取材料情形列表
        //TODO
        //todo 先写死
        var fix_stageid='1dc08dc1-7ba6-4ccf-96cb-a8f7255cddbb';
        this.demoStageId=fix_stageid;
        if(data.useOneForm==1){
            this.getOneFormList(stageId);//写死之前屏蔽
            // this.getOneFormList(fix_stageid);
        }else {
            this.oneFormData=[]; //写死之前屏蔽
            // this.getOneFormList(fix_stageid);
        }
      if(data&&(data.handWay==0)){
        this.parallelItemsQuestionFlag=true;
      }else {
        this.parallelItemsQuestionFlag=false;
      }
    },
    // 获取事项列表
    getStageItems: function(stageId){
      var _that = this;
      _that.showCoreItemsKey=[];
      _that.showParallelItemsKey = [];
      request('', {
        url: ctx+'rest/apply/stage/items',
        type: 'get',
        data: {"stageId":stageId,"projInfoId":_that.projInfoId}
      }, function (data) {
        if(data.success){
          _that.coreItems = data.content.coreItems;
          _that.parallelItems = data.content.parallelItems;
          _that.coreItems.map(function(item){
            if(item){
              _that.setImplementItem(item);
            }
            if(typeof item._parStateId == 'undefined'){
              Vue.set(item,'_parStateId',['ROOT']);
            }else {
              item._parStateId.push('ROOT');
            }
          });
          _that.parallelItems.map(function(item){
            if(item){
              _that.setImplementItem(item);
              if(item.isDone != 'FINISHED' && item.isDone != 'HANDLING'&&(!item.notRegionData)){
//                 _that.showParallelItemsKey.push(item.itemBasicId)
              }
            }
            if(typeof item._parStateId == 'undefined'){
              Vue.set(item,'_parStateId',['ROOT']);
            }else {
              item._parStateId.push('ROOT');
            }
          });
          if(_that.parallelItems){
            _that.parallelItemsSelAll(_that.parallelItems,'autoGetSel');
          }
          _that.$nextTick(function(){
            _that.toggleSelection(_that.parallelItems,'parallelItemsTable');
            _that.setItemShowLen(); // 事项展示长度
          });

        }else {
          _that.$message({
            message: '获取事项列表失败',
            type: 'error'
          });
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    // 事项设置实施事项标准事项
    setImplementItem: function(item){
      var _that = this;
      Vue.set(item, 'isImplement', true); // 是否实施事项
      Vue.set(item, 'disabled', false); // 行政区划是否可选
      Vue.set(item, 'implementItemVerId', ''); // 实施事项版本Id
      Vue.set(item, 'notRegionData', false); // 无匹配的行政区划及承办单位
      Vue.set(item, 'itemHaved', false); // 判断实施事项是否已存在
      if(_that.itemVerIdsStringAll.indexOf(item.itemVerId)<0){
        _that.itemVerIdsStringAll.push(item.itemVerId);
      }
      if(item.isCatalog!=1){
        // item.patType = '';
        item.implementItemVerId = item.itemVerId
      }else {
        item.isImplement = false;
        if(item.regionRange==1){ // 属地办理时 行政区划不可以选
          item.disabled = true;
        }
        if(item.currentCarryOutItem){
          if(_that.itemVerIdsStringAll.indexOf(item.currentCarryOutItem.itemVerId)>-1){
            item.itemHaved = true;
          }
          item.implementItemVerId = item.currentCarryOutItem.itemVerId
          item.orgId = item.currentCarryOutItem.orgId;
        }else if(item.regionRange==1) {
          item.disabled = true;
          item.notRegionData = true;
        }
      }
    },
    // 行政区划承办机构选中获得orgId
    getItemOrgId: function(data,index,item,flag){ // flag = 'core' 并行事项
      var selItemVer = this.$refs.parallelItemsTable.selection; // 所有选择的并联审批事项
      var selCoreItemVer = this.$refs.coreItemsTable.selection; // 所有选择的并行审批事项
      var _itemVerIdS = [],selAllItem=[];
      var _that = this;
      var flagSelItem = true; // 是否查询事项下情形材料
      if(flag == 'core'){selAllItem=selCoreItemVer}else {selAllItem=selItemVer}
      if(data){
        if (data.isDone == 'FINISHED' || data.isDone == 'HANDLING' || data.notRegionData){
          return false;
        }else{
          selAllItem.map(function(ind,item1){
            if(item1.itemVerId&&index!=ind){
              if(item.itemVerId==item1.implementItemVerId){
                data.itemHaved = true;
                _that.$message({
                  message: '该实施事项已存在，请勿重复选择！',
                  type: 'error'
                });
                flagSelItem = false;
              }else {
                _itemVerIdS.push(item1.itemVerId);
              }
            }
          });
        }
      }
      if(flagSelItem){
        data.itemHaved = false;
        data.orgId = item.orgId;
        data.implementItemVerId = item.itemVerId;
        console.log(_that.itemVerIdsString);
        if(flag == 'core'){
          _that.coreItemsSelItem(selCoreItemVer,data);
        }else {
          if(_that.itemVerIdsString == (_itemVerIdS.join(','))){
            return false;
          }else {
            if(_that.parallelItemsQuestionFlag!==true) {
              _that.itemVerIdsString = _itemVerIdS.join(',');
              _that.getOfficeMats(_that.itemVerIdsString);
            }else {
              if(data){
                _that.parallelItemsSelItem(selItemVer,data,'autoGetSel');
              }
            }
          }
        }

      }

    },
    // 主题下主线辅线切换事件
    selStatusLine: function(data,index){
      this.statusLineListActive = index;
      this.statusList = data.statusList;
      this.selStatus(data,0,data.statusList[0].stageId,data.statusList[0].isSelItem);
    },
    toTree: function(data) {
      var result = []
      if(!Array.isArray(data)) {
        return result
      }
      data.forEach(function(item){
        if(item){
          delete item.children;
        }
      });
      var map = {};
      data.forEach(function(item){
        if(item){
          map[item.id] = item;
        }
      });
      data.forEach(function(item){
        if(item){
          var parent = map[item.pId];
          if(parent) {
            (parent.children || (parent.children = [])).push(item);
          } else {
            result.push(item);
          }
        }
      });
      return result;
    },
    // 材料 事项去重
    unique: function(arr,flag) {
      var newArr = [];
      if(flag=='mats'){
        if(arr && arr.length>0){

          arr.forEach(function(item,index){
            var flag = true;
            if(newArr.length > 0) {
              // 此处 不能用foreach，不然没法使用break跳出循环
              for(var i= 0;i<newArr.length;i++){
                if(newArr[i].matId == item.matId){
                  if(typeof item._parStateId == 'undefined'){
                    Vue.set(item,'_parStateId','');
                  }else {
                    if(typeof item._parStateId == 'object'){
                      newArr[i]._parStateId = newArr[i]._parStateId.concat(item._parStateId)
                    }else {
                      newArr[i]._parStateId.push(item._parStateId)
                    }
                  }
                  flag = false;
                  break;
                }
              }
            }
            if(flag){
              newArr.push(item)
            }
          })

        }
      }else if(flag=='stage'){
        // result[arr[i].itemVerId]=arr[i];
        if(arr && arr.length>0){
          // var newArr = [];
          arr.forEach(function(item,index){
            var flag = true;
            if(newArr.length > 0) {
              // 此处 不能用foreach，不然没法使用break跳出循环
              for(var i= 0;i<newArr.length;i++){
                if(newArr[i].itemVerId == item.itemVerId){
                  if(typeof item._parStateId == 'undefined'){
                    Vue.set(item,'_parStateId','');
                  }else {
                    if(typeof item._parStateId == 'object'){
                      newArr[i]._parStateId = newArr[i]._parStateId.concat(item._parStateId)
                    }else {
                      newArr[i]._parStateId.push(item._parStateId)
                    }
                  }
                  flag = false;
                  break;
                }
              }
            }
            if(flag){
              newArr.push(item)
            }
          })

        }
      }else if(flag=='file'){
        // result[arr[i].name]=arr[i];
        if(arr && arr.length>0){
          // var newArr = [];
          arr.forEach(function(item,index){
            var flag = true;
            if(newArr.length > 0) {
              // 此处 不能用foreach，不然没法使用break跳出循环
              for(var i= 0;i<newArr.length;i++){
                if(newArr[i].name == item.name){
                  if(typeof item._parStateId == 'undefined'){
                    Vue.set(item,'_parStateId','');
                  }else {
                    if(typeof item._parStateId == 'object'){
                      newArr[i]._parStateId = newArr[i]._parStateId.concat(item._parStateId)
                    }else {
                      newArr[i]._parStateId.push(item._parStateId)
                    }
                  }
                  flag = false;
                  break;
                }
              }
            }
            if(flag){
              newArr.push(item)
            }
          })

        }
      }
      return newArr;

    },
    // 判断事项checkbox是否可勾选
    checkboxInit: function(row){
      if(row){
        if (row.isDone == 'FINISHED' || row.isDone == 'HANDLING' || row.notRegionData)
          return 0;//不可勾选
        else
          return 1;//可勾选
      }
    },
    // 事项默认全选
    toggleSelection: function(rows,tableRef) {
      var _that = this;
      if (rows.length>0) {
        rows.forEach(function(row){
          if(row){
            if(row.isDone !== 'FINISHED' && row.isDone !== 'HANDLING'&&(!row.notRegionData)){
              _that.$refs[tableRef].toggleRowSelection(row,true);
            }
          }
        });
        var selItemVer = _that.$refs.parallelItemsTable.selection; // 所有选择的并联审批事项
        if(selItemVer.length==0){
          var tabList = _that.model.matsTableData;
          var tabListLen = tabList.length;
          for(var i=0;i<tabListLen;i++){
            if(tabList[i]&&typeof(tabList[i]._parStateId)!="undefined"&&tabList[i]._parStateId.length==1&&tabList[i]._parStateId[0]=='ROOT'){
              _that.model.matsTableData.splice(i,1);
              i--;
            }
          }
        }
      } else {
        _that.$refs[tableRef].clearSelection();
      }
    },
    // 文件上传 change事件
    fileChange: function(file, fileList){
      var _that = this;
      _that.AllFileListId = [];
      if(fileList.length>0){
        this.AllFileList = this.unique(fileList,'file');
        this.AllFileList.map(function(item){
          _that.AllFileListId.push(item.name);
        });
        _that.checkedSelFlie=_that.AllFileListId;
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
      if(_that.checkedSelFlie.length==0){
        alertMsg('', '请选择需要自动分拣的文件', '关闭', 'warning', true);
        return false;
      }else {
        _that.loadingFile = true;
        axios.post(ctx+'rest/mats/att/upload/auto',_that.fileData).then(function(res){
          _that.checkedSelFlie = [];
          _that.AllFileList = [];
          _that.loadingFile = false;
          _that.$refs.uploadFile.clearFiles();
          _that.checkAll = true;
          var matinstIdsObj = [];
          res.data.content.map(function(item){
            if(item){
              _that.showFileListKey.push(item.matId);
              _that.model.matsTableData.map(function(matItem){
                if(matItem){
                  if(item.matId == matItem.matId){
                    matItem.matinstId = item.matinstId;
                    matItem.children=item.fileList;
                  }
                  if(matItem.children.length>0){
                    _that.showMatTableExpand = true;
                  }
                  if(matinstIdsObj.indexOf(matItem.matinstId)<0&&matItem.matinstId!=''){
                    matinstIdsObj.push(matItem.matinstId);
                  }
                }
              });
            }
          });
          _that.matinstIds = matinstIdsObj.join(',');
        }).catch(function(error){
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
    myUploadFile: function(file){
      var _that = this;
      _that.checkedSelFlie.map(function(item){
        if(item == file.file.name){
          _that.fileData.append('file', file.file);
        }
      });
    },
    handleCheckAllChange: function(val) {
      this.checkedSelFlie = val ? this.AllFileListId : [];
    },
    handleCheckedCitiesChange: function(value) {
      var checkedCount = value.length;
      this.checkAll = checkedCount === this.AllFileListId.length;
      this.checkedSelFlie = value;
    },
    showMatFiles: function(matId,docType){  // bsc/att/listAttLinkAndDetailNoPage.do
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
              '                        <a href="'+_that.kpFileOpenEvent(file.detailId)+'" target="_blank" title="' + file.attName + '">' + _that.cutString(file.attName, 50) + '</a>\n' +
              '                    </div>\n' +
              '                </div>';
          });
          _that.dialogHtml = trHtml;
        }else if (result.length == 1) {
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
    kpFileOpenEvent: function(detailId) {
      var url = ctx + 'file/showFile.do?detailId=' + detailId;
      return ctx + 'file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url);
    },
    getFileIcon: function(fm) {
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
    showUploadWindow: function(data){ // 展示文件上传下载弹窗
      var _that = this;
      _that.showUploadWindowFlag = true;
      _that.selMatRowData = data;
      _that.selMatinstId = data.matinstId?data.matinstId:'',
      _that.showUploadWindowTitle = '材料附件 - '+ data.matName
      _that.getFileListWin(data.matinstId,data);
    },
    // 获取已上传文件列表
    getFileListWin: function(matinstId,rowData){
      var _that = this;
      request('', {
        url: ctx + 'rest/mats/att/list',
        type: 'get',
        data: {matinstId: matinstId}
      }, function (res) {
        if(res.success){
          if(res.content){
            _that.uploadTableData = res.content;
            if(rowData){
              rowData.children=res.content;
            }
            if(rowData.children.length>0){
              _that.showMatTableExpand = true;
            }
          }
        }else {
          _that.$message({
            message: res.message?res.message:'加载已上传材料列表失败',
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
    // 预览电子件
    filePreview: function(data,flag){ // flag==pdf 查看施工图
      var detailId = data.fileId;
      var _that = this;
      var regText = /doc|docx|pdf|ppt|pptx|xls|xlsx|txt$/;
      var fileName=data.fileName;
      var fileType = this.getFileType(fileName);
      var flashAttributes = '';
      _that.filePreviewCount++
      if(flag=='pdf'){
        var tempwindow=window.open(); // 先打开页面
        setTimeout(function(){
          tempwindow.location=ctx+'cod/drawing/drawingCheck?detailId='+detailId;
        },1000)
      }else {
        if(fileType=='pdf'){
          var tempwindow=window.open(); // 先打开页面
          setTimeout(function(){
            tempwindow.location=ctx+'previewPdf/view?detailId='+detailId;
          },1000)
        }else if(regText.test(fileType)){
          // previewPdf/pdfIsCoverted
          _that.loading = true;
          request('', {
            url: ctx + 'previewPdf/pdfIsCoverted?detailId='+detailId,
            type: 'get',
          }, function (result) {
            if(result.success){
              _that.loading = false;
              var tempwindow=window.open(); // 先打开页面
              setTimeout(function(){
                tempwindow.location=ctx+'previewPdf/view?detailId='+detailId;
              },1000)
            }else {
              if(_that.filePreviewCount>9){
                confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                  _that.filePreviewCount=0;
                  _that.filePreview(data);
                }, function () {
                  _that.filePreviewCount=0;
                  _that.loading = false;
                  return false;
                }, '确定', '取消', 'warning', true)
              }else {
                setTimeout(function(){
                  _that.filePreview(data);
                },1000)
              }
            }
          }, function (msg) {
            _that.loading = false;
            _that.$message({
              message: '文件预览失败',
              type: 'error'
            });
          })
        }else {
          _that.loading = false;
          var tempwindow=window.open(); // 先打开页面
          setTimeout(function(){
            tempwindow.location=ctx+'rest/mats/att/preview?detailId='+detailId+'&flashAttributes='+flashAttributes;
          },1000)
        }
      }
    },
    // 勾选电子件
    selectionFileChange: function(val) {
      this.fileSelectionList = val;
    },
    //下载单个附件
    downOneFile:function(data){
      window.open( ctx+'rest/mats/att/download?detailIds='+data.fileId,'_blank')
    },
    //删除单个文件附件
    delOneFile:function(data,matData){
      var _that = this;
      request('', {
        url: ctx + 'rest/mats/att/delelte',
        type: 'post',
        data: {matinstId: matData.matinstId,detailIds:data.fileId}
      }, function (res) {
        if(res.success){
          _that.$message({
            message: '删除成功',
            type: 'success'
          });
          _that.getFileListWin(res.content,_that.selMatRowData);
        }else {
          _that.$message({
            message: res.message?res.message:'删除失败',
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
    // 文件上传弹窗页面-下载电子件
    downloadFile: function(){
      var _that = this;
      var detailIds = [];
      if (_that.fileSelectionList.length == 0) {
        _that.$message({
          message: '请勾选数据后操作！',
          type: 'error'
        });
        return false;
      }
      _that.fileSelectionList.map(function(item,index){
        detailIds.push(item.fileId);
      });
      detailIds = detailIds.join(',');
      window.open( ctx+'rest/mats/att/download?detailIds='+detailIds,'_blank')
    },
    debounceHandler: function(file){
      this.loadingFileWin = true;
      this.debounce(this.uploadFileCom, file);
    },
    debounce: function(func, file){
      var vm = this;
      window.clearTimeout(func.tId);
      func.temArr = func.temArr || [];
      func.temArr.push(file);
      console.log(file);
      func.tId = window.setTimeout(function(){
        this.loadingFileWin = false;
        func(func.temArr);
        func.temArr = [];
      }, 1000);
    },
    // 文件上传弹窗页面-上传电子件
    uploadFileCom: function(file){
      var _that = this;
      var rowData = _that.selMatRowData;
      this.fileWinData = new FormData();
      file.forEach(function (u){
        Vue.set(u.file,'fileName',u.file.name);
        _that.fileWinData.append('file', u.file);
      })
      // Vue.set(file.file,'fileName',file.file.name);
      // this.fileWinData.append('file', file.file);
      this.fileWinData.append("matId", rowData.matId);
      this.fileWinData.append("matinstCode", rowData.matCode);
      this.fileWinData.append("matinstName", rowData.matName?rowData.matName:'');
      this.fileWinData.append("projInfoId", _that.projInfoId);
      this.fileWinData.append("unitInfoId", _that.rootUnitInfoId);
      this.fileWinData.append("matinstId", rowData.matinstId?rowData.matinstId:'');
      _that.loadingFileWin = true;
      axios.post(ctx+'rest/mats/att/upload',_that.fileWinData).then(function(res){
        file.forEach(function (u){
          Vue.set(u.file,'matinstId',res.data.content);
        })
         // Vue.set(file.file,'matinstId',res.data.content)
        _that.selMatinstId = res.data.content;
        _that.getFileListWin(res.data.content,rowData);
        var matinstIdsObj = [];
        _that.model.matsTableData.map(function(item){
          if(item){
            if(item.matId == rowData.matId){
              item.matinstId = res.data.content;
              _that.showFileListKey.push(item.matId)
            }
            if(matinstIdsObj.indexOf(item.matinstId)<0&&item.matinstId!=''){
              matinstIdsObj.push(item.matinstId);
            };
          }
        });
        _that.matinstIds = matinstIdsObj.join(',');
        _that.loadingFileWin = false;
        _that.$message({
          message: '上传成功',
          type: 'success'
        });

      }).catch(function(error){
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
    // 文件上传弹窗页面-删除电子件
    delSelectFileCom: function(){
      var _that = this;
      var detailIds = [];
      if (_that.fileSelectionList.length == 0) {
        _that.$message({
          message: '请勾选数据后操作！',
          type: 'error'
        });
        return false;
      }
      _that.fileSelectionList.map(function(item,index){
        detailIds.push(item.fileId);
      });
      detailIds = detailIds.join(',');
      var url=ctx + 'rest/mats/att/delelte';
      request('', {
        url: url,
        type: 'post',
        data: {matinstId: _that.selMatinstId,detailIds:detailIds}
      }, function (res) {
        if(res.success){
          _that.getFileListWin(res.content,_that.selMatRowData);
          _that.$message({
            message: '删除成功',
            type: 'success'
          });
        }else {
          _that.$message({
            message: res.message?res.message:'删除失败',
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
    openPhotoWindow: function(data, flag) {
      if(flag == 'lt'){
        lt_openPhotoWindow(data.matinstId, data.iteminstId, data.inoutId)
      }else {
        fz_openPhotoWindow(null, null, null);
      }
    },
    // 获取省份信息
    queryProvince: function(){
      var _that = this;
      request('', {
        url: ctx + 'rest/apply/province',
        type: 'get'
      }, function (data) {
        _that.provinceList = data.content;
        _that.queryCityData(0);
      })
    },
    // 获取已选事项是否有无匹配的行政区划
    getItemSelectionIsNotAllow: function(){
      var selItemVer = this.$refs.parallelItemsTable.selection; // 所有选择的并联审批事项
      var selCoreItems = this.$refs.coreItemsTable.selection; // 所有选择的并行审批事项
      var notAllow=false;
      selItemVer.map(function(item){
        if(item.notRegionData){
          notAllow=true;
        }
      });
      selCoreItems.map(function(item){
        if(item.notRegionData){
          notAllow=true;
        }
      });
      return notAllow;
    },
    // 意见窗口
    showCommentDialog: function(val){
      var _that = this;
      var stateSel = _that.stateList;
      var stateSelLen = stateSel.length;
      var allowToApply=_that.getItemSelectionIsNotAllow(); // 获取已选事项是否有无匹配的行政区划
      _that.stateIds = [];
      _that.submitCommentsType = val;
      if(allowToApply&&_that.submitCommentsType!='4'){
        alertMsg('', '事项无匹配的行政区划及承办单位', '关闭', 'error', true);
        return true;
      }
      var selItemVer = _that.$refs.parallelItemsTable.selection; // 所有选择的并联审批事项
      var selCoreItems = _that.$refs.coreItemsTable.selection; // 所有选择的并行审批事项
      for(var i=0;i<stateSelLen;i++){  // 并联情形id集合
        if(stateSel[i].selectAnswerId==null||stateSel[i].selectAnswerId==''){
          if(selItemVer.length>0&&stateSel[i].mustAnswer==1){
            _that.stateIds=[];
            if(_that.submitCommentsType!='4'){
              alertMsg('', '请选择情形', '关闭', 'error', true);
              return true;
            }
          }
        }else {
          if(typeof(stateSel[i].selectAnswerId)=='object'&&stateSel[i].selectAnswerId.length>0){
            _that.stateIds = _that.stateIds.concat(stateSel[i].selectAnswerId);
          }else if(stateSel[i].selectAnswerId !== '') {
            _that.stateIds.push(stateSel[i].selectAnswerId);
          }
        }
      }
      var coreStateSel = [];
      _that.propulsionItemStateIds=[];
      for(var j=0;j< selCoreItems.length; j++){
        coreStateSel=selCoreItems[j].questionStates;
        if(coreStateSel){
          var stateIds = [];
          for(var i=0;i<coreStateSel.length;i++){  // 并联情形id集合
            if(coreStateSel[i].selectAnswerId==null||coreStateSel[i].selectAnswerId==''){
              if(selCoreItems.length>0&&coreStateSel[i].mustAnswer==1){
                _that.propulsionItemStateIds=[];
                if(_that.submitCommentsType!='4'){
                  alertMsg('', '请选择并行事项情形', '关闭', 'error', true);
                  return true;
                }
              }
            }else {
              if(typeof(coreStateSel[i].selectAnswerId)=='object'&&coreStateSel[i].selectAnswerId.length>0){
                stateIds = stateIds.concat(coreStateSel[i].selectAnswerId);
              }else if(coreStateSel[i].selectAnswerId !== '') {
                stateIds.push(coreStateSel[i].selectAnswerId);
              }
            }
          }
          _that.propulsionItemStateIds.push({
            "itemVerId": selCoreItems[j].implementItemVerId,
            "stateIds": stateIds
          });
        }
      }
      if(_that.parallelItemsQuestionFlag==true){
        var parallelStateSel = [];
        _that.parallelItemStateIds=[];
        for(var ind=0;ind< selItemVer.length; ind++){
          parallelStateSel=selItemVer[ind].questionStates;
          if(parallelStateSel){
            var stateIds = [];
            for(var ind1=0;ind1<parallelStateSel.length;ind1++){  // 并联情形id集合
              if(parallelStateSel[ind1].selectAnswerId==null||parallelStateSel[ind1].selectAnswerId==''){
                if(selItemVer.length>0&&parallelStateSel[ind1].mustAnswer==1){
                  _that.parallelItemStateIds=[];
                  if(_that.submitCommentsType!='4') {
                    alertMsg('', '请选择并联事项情形', '关闭', 'error', true);
                    return true;
                  }
                }
              }else {
                if(typeof(parallelStateSel[ind1].selectAnswerId)=='object'&&parallelStateSel[ind1].selectAnswerId.length>0){
                  stateIds = stateIds.concat(parallelStateSel[ind1].selectAnswerId);
                }else if(parallelStateSel[ind1].selectAnswerId !== '') {
                  stateIds.push(parallelStateSel[ind1].selectAnswerId);
                }
              }
            }
            _that.parallelItemStateIds.push({
              "itemVerId": selItemVer[ind].implementItemVerId,
              "stateIds": stateIds
            });
          }
        }
      }else {
        _that.parallelItemStateIds=[];
      }
      var jiansheUnitFormEleLen = $('.save-jansheUnit-info').length;
      var jinbanUnitFormEleLen = $('.save-jinbanUnit-info').length;
      var applicantPerFlag=true;// 个人申报校验通过
      var jiansheUnitFlag=true;// 企业申报校验通过
      var jinbanUnitFlag=true;// 经办申报校验通过
      var projBascInfoFlag = true; // 项目/工程信息校验通过
      var perUnitMsg="";
      // 判断个人申报主体必填是否已填
      if(_that.applySubjectType==0){
        _that.$refs['applicantPer'].validate(function(valid){
          if(valid){
            applicantPerFlag=true;
          }else{
            if(_that.submitCommentsType!='4') {
              applicantPerFlag=false;
              perUnitMsg = "请完善申办主体个人信息";
              return false;
            }
          }
        });
      }
      _that.$refs['projBascInfoShowFrom'].validate(function (valid) {
        if(valid){
          projBascInfoFlag=true;
        }else{
          if(_that.submitCommentsType!='4') {
            projBascInfoFlag=false;
            perUnitMsg = "请完善项目/工程信息";
            return false;
          }
        }
      })
      // 判断建设单位必填是否已填
      if(jiansheUnitFormEleLen>0){
        for(var i=0;i<jiansheUnitFormEleLen;i++){
          var formRef = 'jianshe_'+i;
          var validFun;
          if((typeof(_that.$refs[formRef].validate)) == 'function' ){
            validFun = _that.$refs[formRef].validate
          }else {
            validFun = _that.$refs[formRef][0].validate
          }
          validFun(function(valid){
            if(valid){
              jiansheUnitFlag=true;
            }else{
              if(_that.submitCommentsType!='4') {
                jiansheUnitFlag=false;
                perUnitMsg = "请完善申办主体建设单位信息"
                return false;
              }
            }
          });
        }
      }else if(jiansheUnitFormEleLen==0&&_that.applySubjectType!=0) {
        if(_that.submitCommentsType!='4') {
          jiansheUnitFlag = false;
          perUnitMsg = "请添加申办主体建设单位信息"
        }
      }
      // 判断经办单位必填是否已填
      if(jinbanUnitFormEleLen>0){
        for(var i=0;i<jinbanUnitFormEleLen;i++){
          var formRef = 'jingban_'+i;
          var validFun;
          if((typeof(_that.$refs[formRef].validate)) == 'function' ){
            validFun = _that.$refs[formRef].validate
          }else {
            validFun = _that.$refs[formRef][0].validate
          }
          validFun(function(valid){
            if(valid){
              jinbanUnitFlag=true;
            }else{

              if(_that.submitCommentsType!='4') {
                jinbanUnitFlag=false;
                perUnitMsg = "请完善申办主体经办单位信息"
                return false;
              }
            }
          });
        }
      }else if(jinbanUnitFormEleLen==0&&_that.applySubjectType!=0&&_that.agentChecked){
        if(_that.submitCommentsType!='4') {
          jinbanUnitFlag = false;
          perUnitMsg = "请添加申办主体经办单位信息"
        }
      }

      if(projBascInfoFlag&&applicantPerFlag&&jiansheUnitFlag&&jinbanUnitFlag){
        _that.$refs['formTest'].validate(function(valid){
          if(valid||_that.submitCommentsType=='4'||_that.submitCommentsType=='5'){
            // 判断是否选择主题
            if(_that.themeId==null||_that.themeId==undefined||_that.themeId==''){
              alertMsg('', '请选择项目主题', '关闭', 'error', true);
              return false;
            }
            if(_that.smsInfoId||_that.submitCommentsType=='4'){
              _that.getMatinstIds();
              if(!_that.attIsRequireFlag&&_that.submitCommentsType!='4'&&_that.submitCommentsType!='5'){
                alertMsg('', '请上传所有必传的电子件', '关闭', 'warning', true);
                return false;
              }
              if (_that.submitCommentsType == '3') {
                _that.submitCommentsTitle = '不予受理意见对话框'
                _that.showMatList = true;
                _that.submitCommentsFlag = true;
                _that.getUserComments();
              } else if(_that.submitCommentsType == '0') {
                _that.submitCommentsTitle = '收件意见对话框'
                _that.showMatList = false;
                _that.submitCommentsFlag = true;
                _that.getUserComments();
              }else {
                _that.submitCommentsFlag = false;
              }
            }else {
              alertMsg('', '请确认及保存办证取件方式！', '关闭', 'error', true);
            }

          }else {
            alertMsg('', '请选择材料', '关闭', 'error', true);
          }
        });
      }else {
        alertMsg('', perUnitMsg, '关闭', 'error', true);
      }
    },
    // 根据材料定义id获取材料实例id
    getMatinstIds: function () {
      var _that = this;
      var matCountVos = [];
      var selMatinstId=[];
      _that.attIsRequireFlag = true;
      _that.model.matsTableData.map(function(item){
        if(item){
          var copyCnt=0;
          var paperCnt=0;
          if(item.zcqy==0&&item.attIsRequire==1){
            console.log(item.matinstId);
            if(!item.matinstId||item.children.length==0){
              _that.attIsRequireFlag = false;
            }
          }
          if(item.matinstId){
            selMatinstId.push(item.matinstId)
          }
          if(item.getCopy==true){
            copyCnt=item.realCopyCount;
          }
          if(item.getPaper==true){
            paperCnt=item.realPaperCount;
          }
          if(item.getCopy==true||item.getPaper==true){
              matCountVos.push({
                copyCnt: copyCnt,
                paperCnt: paperCnt,
                matId: item.matId,
            })
          }
        }
      });
      if(!_that.attIsRequireFlag&&_that.submitCommentsType!=4&&_that.submitCommentsType!=5){
        return false;
      }
      if(matCountVos.length==0){
        _that.selMatinstIds = selMatinstId;
        if(_that.submitCommentsType==1||_that.submitCommentsType==4||_that.submitCommentsType==5){
          _that.startParalleApprove();
        }
        return false;
      }
      var parmas = {
        matCountVos: matCountVos,
        projInfoId: _that.projInfoId,
        unitInfoId: _that.rootUnitInfoId
      }
      request('', {
        url: ctx + 'rest/mats/matinst/batch/save',
        type: 'post',
        ContentType: 'application/json',
        data: JSON.stringify(parmas)
      }, function (data) {
        if(data.success){
          var tmpArr = [];
          data.content.map(function(item){
            // _that.selMatinstIds = item.matinstIds;
            tmpArr = tmpArr.concat(item.matinstIds);
          });
          _that.selMatinstIds = tmpArr.concat(selMatinstId);
          if(_that.submitCommentsType==1||_that.submitCommentsType==4||_that.submitCommentsType==5){
            _that.startParalleApprove();
          }
        }
      }, function (msg) {
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    //查询回执列表
    queryReceiveList: function (applyinstIds) {
      var _that = this;
      request('', {
        url: ctx + 'rest/receive/getStageOrSeriesReceiveByApplyinstIds',
        type: 'get',
        data: {'applyinstIds': applyinstIds}
      }, function (res) {
        if (res.success) {
          //显示列表弹框
          if(res.content.length>0){
            _that.receiveList = res.content;
            _that.receiveList.map(function(item){
              Vue.set(item,'show',true);
            });
            _that.printReceive1(_that.receiveList[0].receiveList[0],0,0);
            _that.receiveModalShow = true;
          }else {
            _that.reloadPage()
          }
        }else {
          _that.$message({
            message: res.message?res.message:'获取回执列表失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: '获取回执列表失败',
          type: 'error'
        });
      });
    },
    //打印回执
    printReceive: function (row) {
      var url = ctx + 'rest/receive/toPrintPage/' + row.applyinstId + '/' + row.receiptType;
      setTimeout(function () {
        window.open(ctx + 'rest/ntko/ntkoOpenWin?jumpUrl=' + encodeURIComponent(url));
      }, 500);
    },
    //打印回执new
    printReceive1: function (row,index,ind) {
      this.receiveItemActive = index;
      this.receiveActive = ind;
      var fileUrl = ctx + 'rest/receive/toPDF/'+row.receiveId;
      this.pdfSrc=ctx + 'preview/pdfjs/web/viewer.html?file='+encodeURIComponent(fileUrl)
    },
    // 展开收起打印回执
    toggleReceiveListShow: function(item){
      item.show=!item.show;
    },
    // 发起申报
    startParalleApprove: function(){
      var _that = this;
      if(!_that.comments&&_that.submitCommentsType!=1&&_that.submitCommentsType!=4&&_that.submitCommentsType!=5){
        alertMsg('', '请填写收件意见', '关闭', 'error', true);
        return false;
      }
      var projInfoIds=[],handleUnitIds=[]; // 项目ID集合 经办单位ID集合
      var branchOrgMap=[],itemVerIds=[]; // 分局承办 并联申报事项版本ID
      var propulsionBranchOrgMap=[], propulsionItemVerIds=[]; // 并行推进事项分局承办 并行推进事项版本ID
      var buildProjUnitMap=[]; // 建设单位Map集合
      var applySubjectType='';
      // 申报主体为1、2
      if(_that.applySubjectType==2){
        applySubjectType=1;
      }else {
        applySubjectType=_that.applySubjectType;
      }
      projInfoIds.push(_that.projInfoId);
      _that.agentUnits.map(function(item){
        handleUnitIds.push(item.unitInfoId);
      });
      _that.jiansheFrom.map(function(item){
        buildProjUnitMap.push({
          "projectInfoId": _that.projInfoId,
          "unitIds": [item.unitInfoId]
        });
      });
      var selItemVer = _that.$refs.parallelItemsTable.selection; // 所有选择的并联审批事项
      var selCoreItems = _that.$refs.coreItemsTable.selection; // 所有选择的并行审批事项
      var strVer = ''; // 并联事项id集合
      var selItemVerFlag = true; // 并连事项行政区划或承办单位是否选择
      var selCoreItemsFlag = true; // 并行事项行政区划或承办单位是否选择
      var flagMeg = ''; // 错误提示
      // 并联分局承办集合
      selItemVer.map(function(item){
        if(item.implementItemVerId){
          itemVerIds.push(item.implementItemVerId);
        }else{
          // alertMsg('', '请选择并联事项行政区划或承办单位', '关闭', 'error', true);
          selItemVerFlag = false;
          flagMeg = '请选择并联事项行政区划或承办单位';
        }
        strVer += 'itemids='+item.itemId+'&'
        // if(item.isBranch==true){
          branchOrgMap.push({
            itemVerId: item.implementItemVerId,
            branchOrg: item.orgId,
          });
        // }
      });
      if(branchOrgMap.length>0){
        branchOrgMap=JSON.stringify(branchOrgMap)
      }else {
        branchOrgMap=''
      }
      // 并行事项分局承办集合
      selCoreItems.map(function(item){
        if(item.implementItemVerId){
          propulsionItemVerIds.push(item.implementItemVerId);
        }else{
          selCoreItemsFlag = false;
          flagMeg = '请选择并行事项行政区划或承办单位';
          // alertMsg('', '请选择并行事项行政区划或承办单位', '关闭', 'error', true);
          // return false;
        }
        // if(item.isBranch==true){
          propulsionBranchOrgMap.push({
            itemVerId: item.implementItemVerId,
            branchOrg: item.orgId,
          });
        // }
      });
      if(!(selItemVerFlag&&selCoreItemsFlag)){
        alertMsg('', flagMeg, '关闭', 'error', true);
        return false;
      }
      if(propulsionBranchOrgMap.length>0){
        propulsionBranchOrgMap=JSON.stringify(propulsionBranchOrgMap)
      }else {
        propulsionBranchOrgMap=''
      }
      _that.rootUnitInfoId = '';
      _that.rootLinkmanInfoId = '';
      _that.rootApplyLinkmanId = '';
      if(_that.jiansheFrom.length>0) {
        _that.rootUnitInfoId = _that.jiansheFrom[0].unitInfoId;
        _that.rootLinkmanInfoId = _that.jiansheFrom[0].linkmanId;
      }
      if(this.agentUnits.length>0){
        _that.rootUnitInfoId = _that.agentUnits[0].unitInfoId;
        _that.rootLinkmanInfoId = _that.agentUnits[0].linkmanId;
      }
      if(_that.applySubjectType == 0&&_that.applyPersonFrom.applyLinkmanId){
        _that.rootApplyLinkmanId = _that.applyPersonFrom.applyLinkmanId;
        _that.rootLinkmanInfoId = _that.applyPersonFrom.linkLinkmanId
      };
      var parmas={
        applyLinkmanId: _that.rootApplyLinkmanId,
        applySource: 'win',
        applySubject: applySubjectType,
        applyinstIds: _that.applyinstIds,
        parallelApplyinstId: _that.parallelApplyinstId,
        branchOrgMap: branchOrgMap,
        comments: _that.comments,
        handleUnitIds: handleUnitIds,
        itemVerIds: itemVerIds,
        linkmanInfoId: _that.rootLinkmanInfoId,
        matinstsIds: _that.selMatinstIds,
        projInfoIds: projInfoIds,
        propulsionBranchOrgMap: propulsionBranchOrgMap,
        propulsionItemVerIds: propulsionItemVerIds,
        smsInfoId : _that.smsInfoId,
        stageId: _that.stageId,
        stateIds: _that.stateIds,
        themeId: _that.themeId,
        propulsionItemStateIds: _that.propulsionItemStateIds,
        parallelItemStateIds: _that.parallelItemStateIds,
        buildProjUnitMap: buildProjUnitMap,
        isJustApplyinst: _that.IsJustApplyinst,
      }
      _that.progressDialogVisible = true;
      _that.submitCommentsFlag = false;
      var succMsg = '发起申报成功';
      var errorMsg = '发起申报失败！';
      var url = 'rest/apply/parallel/processflow/start';//发起申报
      if (_that.submitCommentsType == '1'||_that.submitCommentsType == '5') {//打印回执
        url = 'rest/apply/parallel/inst';
        succMsg = '获取回执成功';
        errorMsg = '打印回执失败！';
      } else if (_that.submitCommentsType == '3') { // 不受理
        url = 'rest/apply/parallel/processflow/inadmissible';
        succMsg = '操作成功';
        errorMsg = '不受理失败！';
      }else if (_that.submitCommentsType == '4') { // 仅实例化
        url = 'rest/apply/parallel/onlyInstApply';
        succMsg = '实例化成功';
        errorMsg = '实例化失败';
      }
      _that.progressIntervalStop=false;
      _that.setUploadPercentage();
      request('', {
        url: ctx+url,
        type: 'post',
        ContentType: 'application/json',
        timeout: 1000000,
        data: JSON.stringify(parmas)
      }, function (res) {
        if(res.success){
          var applyinstIdsParallelApplyinstId=[];
          _that.progressIntervalStop=true;
          _that.uploadPercentage=100;
          _that.progressDialogVisible = false;
          _that.$message({
            message: succMsg,
            type: 'success'
          });
          // applyinstIds 并行
          // parallelApplyinstId 并联
          //isJustApplyinst "1"  是否仅生成申报实例
          if(_that.submitCommentsType==4){
            _that.IsJustApplyinst = 1;
          }else {
            _that.IsJustApplyinst = 2;
          }
          if(_that.submitCommentsType==4){
            _that.parallelApplyinstId=res.content;
            _that.formItemsIdStr = strVer;
            _that.getOneFormrender2(strVer,_that.parallelApplyinstId,_that.stageId);
            return false;
          }else {
            applyinstIdsParallelApplyinstId=res.content.applyinstIds;
            applyinstIdsParallelApplyinstId.push(res.content.parallelApplyinstId);
            _that.parallelApplyinstId=res.content.parallelApplyinstId;
          }
          // 判断材料补全按钮是否可以点击
          // if(_that.applyinstIds.length>0 && _that.submitCommentsType!='3') {
          //   _that.isMend=false;
          // }else{
          //   _that.isMend=true;
          // }
          var applyinstIdsList = [];
          // 去重
          for(var i=0, len=applyinstIdsParallelApplyinstId.length; i<len; i++){
            if(applyinstIdsParallelApplyinstId.indexOf(applyinstIdsParallelApplyinstId[i]) == i){
              applyinstIdsList.push(applyinstIdsParallelApplyinstId[i]);
            }
          }
          _that.applyinstIds = applyinstIdsList;
          var applyinstIds=applyinstIdsList.join(',');
          setTimeout(function(){
            // _that.progressDialogVisible = false;
            // _that.uploadPercentage=0;
            if(_that.submitCommentsType==5){
              _that.getLackMatsMatmend()
            } else{
              _that.queryReceiveList(applyinstIds);
            }
          },500);
        }else {
          _that.progressIntervalStop=true;
          _that.progressDialogVisible = false;
          _that.$message({
            message: res.message?res.message:errorMsg,
            type: 'error'
          });
        }
      }, function (msg) {
        _that.progressDialogVisible = false;
        _that.progressIntervalStop=true;
        _that.uploadPercentage=0;
        _that.$message({
          message: msg.message?msg.message:errorMsg,
          type: 'error'
        });
      });
    },
    formatUploadPercentage: function(percentage){
      return percentage === 100 ? '成功' : (percentage+'%');
    },
    //项目树右键事件
    showRightBtnList: function(e,data){
      this.treeBtnTop = e.clientY;
      this.treeBtnLeft = e.clientX;
      this.treeRightBtnList = true;
      this.parentProjInfoId = data.id;
    },
    // 新增子工程
    addChildChildProj: function(){
      this.addChildProjShow = true;
      this.treeRightBtnList = false;
    },
    // 保存新增子工程
    saveAddChildProj: function(){
      var _that = this;
      var parmas = {
        isSecond: false,
        parentProjInfoId: _that.parentProjInfoId,
        projName: _that.childProjName,
        foreignRemark: _that.childProjText
      }
      _that.addChildLoading = true;
      request('', {
        url: ctx + 'rest/project/add/child',
        type: 'post',
        data: parmas
      }, function (data) {
        _that.addChildLoading = false;
        if(data.success){
          _that.$message({
            message: '新增子项目成功',
            type: 'success'
          });
          _that.showProjTree();
          _that.addChildProjShow = false;
        }
      }, function (msg) {
        _that.addChildLoading = false;
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    // 关闭工程树窗口回调
    clearTreeChildInfo: function(){
      this.childProjName='';
      this.childProjText='';
      this.addChildProjShow=false;
      this.treeRightBtnList=false;
    },
    // 删除子工程
    delChildChildProj: function(){  // rest/project/delete/child
      var _that =this;
      _that.treeRightBtnList=false;
      request('', {
        url: ctx + 'rest/project/delete/child',
        type: 'post',
        data: {childProjInfoId: _that.parentProjInfoId}
      }, function (data) {
        if(data.success){
          _that.$message({
            message: '项目删除成功',
            type: 'success'
          });
        }else {
          _that.$message({
            message: data.message == '1'?'该项目工程不能删除':data.message == '2'?'已申报的项目不能删除':'删除失败',
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
    selChildProj: function(data){
      this.projInfoId = data.id;
      this.projName=data.projName
      this.linkQuery();
      this.projectTreeInfoModal=false;
    },
    // 根据单位id获取关联联系人
    getLinkManListByUnitId: function(item){
      var _that = this;
      _that.loadingLinkMan = true;
      if(item.unitInfoId){
        request('', {
          url: ctx + 'rest/linkman/list',
          type: 'get',
          data: {
            keyword: '',
            unitInfoId: item.unitInfoId
          }
        }, function (data) {
          if(data.success){
            _that.loadingLinkMan = false;
            item.aeaLinkmanInfoList = data.content;
          }
        }, function (msg) {
          _that.$message({
            message: '获取单位关联联系人失败',
            type: 'error'
          });
        });
      }else {
        item.aeaLinkmanInfoList=[];
      }
    },
    // 删除联系人
    delLinkman: function (data,parentData,ind) {
      var _that = this;
      if(!data.addressId){
        alertMsg('提示信息', '联系人ID为空', '关闭', 'warning', true);
        return;
      }else{
        confirmMsg('此操作影响：','确定要删除该联系人吗？',function(){
          request('', {
            url: ctx + 'rest/linkman/delete/by/unit',
            type: 'post',
            data: {linkmanInfoId:data.addressId,unitInfoId:parentData.unitInfoId}
          }, function (result) {
            if(result.success){
              _that.$message({
                message: '联系人删除成功',
                type: 'success'
              });
            }else {
              _that.$message({
                message: result.message?result.message:'联系人删除失败',
                type: 'error'
              });
            }
          },function(msg){
            _that.$message({
              message: msg.message ? msg.message : '删除失败',
              type: 'error'
            });
          })
        },function(){},'确定','取消','warning',true)
      }
    },
    // 选择企业联系人信息
    selLinkman: function(data,ind1,flag){
      var _that = this;
      if(data){
        if(flag==false){
          _that.agentUnits[ind1].linkmanName = data.addressName;
          _that.agentUnits[ind1].linkmanId = data.addressId;
          _that.agentUnits[ind1].linkmanMail = data.addressMail;
          _that.agentUnits[ind1].linkmanCertNo = data.addressIdCard;
          _that.agentUnits[ind1].linkmanMobilePhone = data.addressPhone;

        }else {
          _that.jiansheFrom[ind1].linkmanName = data.addressName;
          _that.jiansheFrom[ind1].linkmanId = data.addressId;
          _that.jiansheFrom[ind1].linkmanMail = data.addressMail;
          _that.jiansheFrom[ind1].linkmanCertNo = data.addressIdCard;
          _that.jiansheFrom[ind1].linkmanMobilePhone = data.addressPhone;
        }
      }
    },
    // 材料全选
    checkAllMatChange: function(val,flag){
      if(val==false){
        val='';
      }
      var _that = this;
      _that.model.matsTableData.map(function(item){
        if(item){
          if(flag=='paper'){
            item.getPaper=val;
            _that.getPaperAll = val;
          }else {
            item.getCopy=val;
            _that.getCopyAll = val;
          }
        }
      });
    },
    // 材料单选
    checkedMatChange: function(val,index,flag){
      var _that = this;
      if(val==false){
        val='';
      }
      if(flag=='paper'){
        _that.model.matsTableData[index].getPaper=val;
      }else {
        _that.model.matsTableData[index].getCopy=val;
      }
      _that.ifMatsSelAll();
    },
    // 判断是否材料全选
    ifMatsSelAll: function () {
      var getCopyCount=0;
      var _that = this;
      var getPaperCount=0;
      _that.model.matsTableData.map(function(item){
        if(item){
          if(item.getCopy==true){
            getCopyCount++
          }
          if(item.getPaper==true){
            getPaperCount++
          }
        }
      })
      if(getCopyCount==_that.model.matsTableData.length){
        _that.getCopyAll = true;
      }else {
        _that.getCopyAll = '';
      }
      if(getPaperCount==_that.model.matsTableData.length){
        _that.getPaperAll = true;
      }else {
        _that.getPaperAll = '';
      }
    },

    // 选择省份
    queryCityData: function (index) {
      this.cityList=this.provinceList[index].cityList;
      this.queryAreaData(0);
      this.getResultForm.addresseeProvince = this.provinceList[index].code;
    },
    // 选择市地区
    queryAreaData: function (index) {
      this.countyList=this.cityList[index].areaList;
      this.getResultForm.addresseeCity=this.cityList[index].code;
      this.queryCountyData(this.countyList[0].code);
    },
    // 选择地区
    queryCountyData:function(code){
      this.getResultForm.addresseeCounty=code;
    },
    // 保存取证人信息
    saveSmsinfo: function () {
      var _that = this;
      _that.getResultForm.id=_that.smsInfoId;
      _that.$refs['resultForm'].validate(function (valid) {
        if(valid){
          _that.loading = true;
          request('', {
            url: ctx+'rest/apply/save/or/update/smsinfo',
            type: 'post',
            data: _that.getResultForm
          }, function (data) {
            if(data.success){
              _that.smsInfoId = data.content;
              _that.loading = false;
              _that.$message({
                message: '保存成功',
                type: 'success'
              });
            }else {
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
      if(val==0){
        this.getResultForm.smsType=2;
        this.queryProvince();
      }
    },
    // 选取领证信息
    queryGetResultMan: function(item) {
      this.getResultForm.addresseeName = item.addressName;
      this.getResultForm.addresseePhone = item.addressPhone;
      this.getResultForm.addresseeIdcard = item.addressIdCard;
    },
    setColStatusActive: function(index){
      this.colStatusActive=index;
    },
    /*********** showRefuseDialog 不收件弹窗 ***********/
    //填写选择的材料名称
    getSelectedMat: function (val) {
      var _this = this;
      //追加，如果需要替换， 将一下两行替换为    _this.refusedRecepitForm.receiveMemo=val;
      var memo = _this.refusedRecepitForm.receiveMemo;
      _this.refusedRecepitForm.receiveMemo = memo + val + ":";
    },
    // 管理常用意见
    mUseComments: function(){
        this.submitCommentsFlag1 = true;
    },
    //保存常用意见
    setUseComment: function(){
      var _that = this;
      if(_that.comments&&_that.comments.trim()!=''){
        request('', {
            url: ctx + 'rest/comment/saveUserOpinion',
            type: 'post',
            data: {message:_that.comments}
        }, function (result) {
            if(result.success){
                _that.$message({
                    message: '常用意见保存成功',
                    type: 'success'
                });
                _that.getUserComments();
            }else {
                _that.$message({
                    message: result.message?result.message:'常用意见保存失败',
                    type: 'error'
                });
            }
        },function(msg){
            _that.$message({
                message: msg.message ? msg.message : '常用意见保存失败',
                type: 'error'
            });
        })
      }else {
        _that.$message({
            message: '当前意见为空',
            type: 'warning'
        });
      }
    },
    //选择常用意见
    changeUserComment: function(val){
      var _that = this;
      _that.comments = _that.comments+val;
    },
    //加载常用意见
    getUserComments: function(){
      var _that = this;
      request('', {
        url: ctx + 'rest/comment/getAllActiveUserOpinions',
        type: 'get'
      }, function (result) {
        if(result.success){
          _that.commentsList = result.content;
        }else {
          _that.$message({
              message: result.message?result.message:'获取常用意见失败',
              type: 'error'
          });
        }
      },function(msg){
        _that.$message({
          message: msg.message ? msg.message : '获取常用意见失败',
          type: 'error'
        });
      })
    },
    //编辑常用意见
    editComment: function(row){
      this.submitCommentsFlag2 = true;
      this.commentInfo.comment = row.opinionContent;
      this.commentInfo.sort = row.sortNo;
      this.commentInfo.id = row.opinionId;
    },
    //修改常用意见
    updateComment: function(){
      var _that = this;
        _that.submitCommentsFlag2 = false;
      request('', {
          url: ctx + 'rest/comment/editUserOpinion',
          type: 'put',
          data:{opinionContent:_that.commentInfo.comment,opinionId:_that.commentInfo.id,sortNo:_that.commentInfo.sort}
      }, function (result) {
          if(result.success){
              _that.$message({
                  message: '意见修改成功',
                  type: 'success'
              });
              _that.getUserComments();
          }else {
              _that.$message({
                  message: result.message?result.message:'意见修改失败',
                  type: 'error'
              });
          }
      },function(msg){
          _that.$message({
              message: msg.message ? msg.message : '意见修改失败',
              type: 'error'
          });
      })
    },
    //删除常用意见
    deleteUseComment: function(opinionId){
          var _that = this;
        confirmMsg('此操作影响：','确定要删除该意见吗？',function(){
          request('', {
              url: ctx + 'rest/comment/deleteUserOpinion/'+opinionId,
              type: 'delete'
          }, function (result) {
              if(result.success){
                  _that.$message({
                      message: '删除意见成功',
                      type: 'success'
                  });
                  _that.getUserComments();
              }else {
                  _that.$message({
                      message: result.message?result.message:'删除常用意见失败',
                      type: 'error'
                  });
              }
          },function(msg){
              _that.$message({
                  message: msg.message ? msg.message : '删除常用意见失败',
                  type: 'error'
              });
          })
        },function(){},'确定','取消','warning',true)
      },
    // 并行事项单选事件
    coreItemsSelItem: function(selArr,row) {
      var flag = false;
      var _that = this;
      if(selArr.length==0){
        flag=false;
        _that.showCoreItemsKey=[];
        var selStateIds = _that.getStatusListId();
        for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
          var obj = _that.model.matsTableData[i];
          if(obj && (obj._itemVerIds&&(obj._itemVerIds.indexOf(row.itemVerId)>-1))){ // 清空事项下材料
            if(obj._itemVerIds.length==1&&(typeof obj._parStateId=='undefined'||obj._parStateId.length==0||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0))){
              obj._itemVerIds = [];
              _that.model.matsTableData.splice(i, 1);
              i--
            }else {
              var index = obj._itemVerIds.indexOf(row.itemVerId);
              if (index > -1) {
                obj._itemVerIds.splice(index, 1);
              }
            }
          }
        }
        return false;
      }
      selArr.forEach(function (item) {
        if(item){
          if(item.itemVerId==row.itemVerId){
            flag=true;
          }
        }
      });
      if(flag){
        if(row.implementItemVerId){
          _that.getStatusMatItemsByStatus(row,'coreItem');
        }
      }else {
        row.questionStates=[];
        row.stateMats=[];
        var selStateIds = _that.getStatusListId();
        for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
          var obj = _that.model.matsTableData[i];
          if(obj._itemVerIds=='undefined'||obj._itemVerIds==undefined){
            Vue.set(obj, '_itemVerIds', []);
          }else {
            if (obj._itemVerIds.indexOf(row.itemVerId) > -1) {
              if (obj._itemVerIds.length == 1 && (typeof obj._parStateId == 'undefined' || obj._parStateId.length == 0 || (obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0))) {
                obj._itemVerIds = [];
                _that.model.matsTableData.splice(i, 1);
                i--
              } else {
                var index = obj._itemVerIds.indexOf(row.itemVerId);
                if (index > -1) {
                  obj._itemVerIds.splice(index, 1);
                }
              }
            }
          }
        }
        _that.ifMatsSelAll();
        _that.showCoreItemsKey = _that.showCoreItemsKey.filter(function(item) {
          return item != row.itemBasicId;
        });
      }
    },
    // 并行并联事项 根据情形获取子情形 材料
    getStatusMatItemsByStatus: function(row,flag){ // flag='coreItem'并行事项
      var _that = this;
      var params={
        itemVerId: row.implementItemVerId,
        parentId: row.parentId?row.parentId:'ROOT',
      }
      request('', {
        url: ctx+'rest/mats/states/mats/'+row.implementItemVerId+'/'+(row.parentId?row.parentId:'ROOT'),
        data: params,
        type: 'get'
      }, function (data) {
        if(data.success){
          var objQuestionStates = data.content.questionStates;
          data.content.stateMats.map(function(item){
            if(item){
              if(item.children=='undefined'||item.children==undefined){
                Vue.set(item,'children',[]);
              }
              if(item.matinstId=='undefined'||item.matinstId==undefined){
                Vue.set(item,'matinstId','');
              }
              if(item.getPaper=='undefined'||item.getPaper==undefined){
                Vue.set(item,'getPaper','');
              }
              if(item.getCopy=='undefined'||item.getCopy==undefined){
                Vue.set(item,'getCopy','');
              }
              if(item.realPaperCount=='undefined'||item.realPaperCount==undefined){
                Vue.set(item,'realPaperCount',item.duePaperCount);
              }
              if(item.realCopyCount=='undefined'||item.realCopyCount==undefined){
                Vue.set(item,'realCopyCount',item.dueCopyCount);
              }
              if(item._itemVerIds=='undefined'||item._itemVerIds==undefined){
                Vue.set(item,'_itemVerIds',[row.itemVerId]);
              }
              if(flag=='coreItem'){
                if(item._itemType=='undefined'||item._itemType==undefined){
                  Vue.set(item,'_itemType','coreItem');
                }
              }else {
                if(item._itemType=='undefined'||item._itemType==undefined){
                  Vue.set(item,'_itemType','parallelItems');
                }
              }
            }
          });
          var objStateMats = data.content.stateMats;
          objQuestionStates.map(function(item){
            if(item.answerType!='s'&&item.answerType!='y'){
              if(typeof item.selValue == "undefined"){
                Vue.set(item, 'selValue', []);
              }else {
                item.selValue = [];
              }

              item.selectAnswerId=item.selValue;
            }
          });
          if(row.questionStates=='undefined'||row.questionStates==undefined){
            Vue.set(row, 'questionStates', objQuestionStates);
          }else {
            row.questionStates= objQuestionStates
          }
          if(row.stateMats=='undefined'||row.stateMats==undefined){
            Vue.set(row, 'stateMats', objStateMats);
          }else {
            row.stateMats= objStateMats;
          }

          if(objQuestionStates.length>0){
            _that.coreItemQuestionFlag=true;
            if(flag=='coreItem'){
              _that.showCoreItemsKey.push(row.itemBasicId);
            }else {
              _that.showParallelItemsKey.push(row.itemBasicId);
            }

          };
          _that.model.matsTableData=_that.unique(_that.model.matsTableData.concat(objStateMats),'mats');
          _that.matIds=[];
          _that.model.matsTableData.map(function (item) {
            if(item){
              _that.matIds.push(item.matId);
            }
          });
          _that.ifMatsSelAll();
        }
      })
    },
    // 并行事项全选
    coreItemsSelAll: function(selArr){
      var _that = this;
      var flag = true;
      _that.showCoreItemsKey=[];
      if(selArr.length==0){
        flag=false;
        var selStateIds = _that.getStatusListId();
        for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
          var obj = _that.model.matsTableData[i];
          if(obj && (obj._itemType=='coreItem')){ // 清空事项下材料
            if(typeof obj._parStateId=='undefined'||obj._parStateId.length==0||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0)){
              obj._itemVerIds = [];
              _that.model.matsTableData.splice(i, 1);
              i--
            }else {
              var index = obj._itemVerIds.indexOf(row.itemVerId);
              if (index > -1) {
                obj._itemVerIds.splice(index, 1);
              }
            }

          }
        }
        return false;
      }
      selArr.map(function(row){
        if(row){
          _that.showCoreItemsKey.push(row.itemBasicId);
          if(row.isDone == 'FINISHED' || row.isDone == 'HANDLING' || row.notRegionData){
            flag=false;
          }else {
            flag = true;
          }
          if(flag&&row.implementItemVerId){
            _that.getStatusMatItemsByStatus(row,'coreItem');
          }
        }
      });
    },
    // 并联事项单选事件
    parallelItemsSelItem: function(selArr,row,selflag){ // selflag 调用方式 autoGetSel手动触发
      var _that=this;
      var flag = false;
      if(selArr.length==0) {
        flag=false;
        _that.showParallelItemsKey = [];
        if(selflag !='autoGetSel'){
          alertMsg('', '您已取消所有并联事项！', '关闭', 'warning', true);
        }
        var tabList = _that.model.matsTableData;
        var tabListLen = tabList.length;
        var selStateIds = _that.getStatusListId();
        for (var i = 0; i < tabListLen; i++) {
          var obj = tabList[i];
          if(obj && (obj._itemVerIds&&(obj._itemVerIds.indexOf(row.itemVerId)>-1))){ // 清空事项下材料
            if(obj._itemVerIds.length==1&&(typeof obj._parStateId=='undefined'||obj._parStateId.length==0||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0))){
              _that.removeParallelItemsMats.push(obj);
              obj._itemVerIds = [];
              _that.model.matsTableData.splice(i, 1);
              i--
            }else {
              var index = obj._itemVerIds.indexOf(row.itemVerId);
              if (index > -1) {
                obj._itemVerIds.splice(index, 1);
              }
            }
          }
        }
        _that.itemVerIdsString = '';
      }else {
        if(_that.parallelItemsQuestionFlag==true){
          selArr.forEach(function (item) {
            if(item.itemVerId==row.itemVerId){
              flag=true;
            }
          });
          if(flag&&row.implementItemVerId){
            _that.getStatusMatItemsByStatus(row);
          }else {
            row.questionStates=[];
            row.stateMats=[];
            var selStateIds = _that.getStatusListId();
            for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
              var obj = _that.model.matsTableData[i];
              if(obj._itemVerIds=='undefined'||obj._itemVerIds==undefined){
                Vue.set(obj, '_itemVerIds', []);
              }else {
                if (obj && (obj._itemVerIds.indexOf(row.itemVerId)>-1)) {
                  if(obj._itemVerIds.length==1&&(typeof obj._parStateId=='undefined'||obj._parStateId.length==0||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0))){
                    obj._parStateId = [];
                    _that.model.matsTableData.splice(i, 1);
                    i--
                  }else {
                    var index = obj._itemVerIds.indexOf(row.itemVerId);
                    if (index > -1) {
                      obj._itemVerIds.splice(index, 1);
                    }
                  }
                }
              }
            }
            _that.ifMatsSelAll();
            _that.showParallelItemsKey = _that.showParallelItemsKey.filter(function(item) {
              return item != row.itemBasicId;
            });
          }
        }else {
          var rowsItemVerIds = [];
          selArr.forEach(function (item) {
            if(item.itemVerId){
              rowsItemVerIds.push(item.itemVerId);
            }
          });
          _that.itemVerIdsString = rowsItemVerIds.join(',');
        }
      }
      if(_that.parallelItemsQuestionFlag!==true) {
        _that.getOfficeMats(_that.itemVerIdsString);
      }
    },
    // 并联事项不包含情形时勾选并联事项获取的材料
    getOfficeMats: function(_itemVerIdS){ // rest/mats/getOfficeMats
      var _that = this;
      _that.parallelLoading = true;
      request('', {
        url: ctx + 'rest/mats/getOfficeMats',
        type: 'post',
        data: {stageId: _that.stageId, itemVerIds: _itemVerIdS}
      }, function (result) {
        _that.parallelLoading = false;
        if (result.success) {
          result.content.map(function(item){
            if(item){
              Vue.set(item,'_itemType','officeMatsType');
              Vue.set(item,'children',[]);
              if(item.matinstId=='undefined'||item.matinstId==undefined) {
                Vue.set(item, 'matinstId', '');
              }
              Vue.set(item,'getPaper','');
              Vue.set(item,'getCopy','');
              Vue.set(item,'realPaperCount',item.duePaperCount);
              Vue.set(item,'realCopyCount',item.dueCopyCount);
              if(_that.matIds.indexOf(item.matId)<0){
                _that.matIds.push(item.matId);
              }
            }

          });
          for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空不包含情形时勾选并联事项所对应材料
            var obj = _that.model.matsTableData[i];
            if(obj &&obj._itemType=='officeMatsType'){
              _that.model.matsTableData.splice(i, 1);
              i--
            }
          }
          _that.model.matsTableData=_that.unique(_that.model.matsTableData.concat(result.content),'mats'); // 合并去重材料
        }else {
          _that.$message({
            message: '并联事项材料失败！',
            type: 'error'
          });
        }
      },function(res){
        _that.parallelLoading = false;
        _that.$message({
          message: '并联事项材料失败！',
          type: 'error'
        });
      })
    },
    // 并联事项全选事件
    parallelItemsSelAll: function(selArr,selflag){ // selflag 调用方式 autoGetSel手动触发
      var _that = this;
      var flag = false;
      _that.showParallelItemsKey=[];
      if(selArr.length==0){
        flag=false;
        if(selflag!='autoGetSel'){
          alertMsg('', '您已取消所有并联事项！', '关闭', 'warning', true);
        }
        var tabList = _that.model.matsTableData;
        var tabListLen = tabList.length;
        var selStateIds = _that.getStatusListId();
        for(var i=0;i<tabListLen;i++){
          var obj = tabList[i];
          if(obj && (obj._itemType=="parallelItems")){ // 清空事项下材料
            if(obj._itemVerIds.length==1&&(typeof obj._parStateId=='undefined'||obj._parStateId.length==0||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0))){
              _that.removeParallelItemsMats.push(obj);
              _that.model.matsTableData.splice(i, 1);
              obj._itemVerIds = [];
              i--
            }else {
              var index = obj._itemVerIds.indexOf(row.itemVerId);
              if (index > -1) {
                obj._itemVerIds.splice(index, 1);
              }
            }
          }
        }
        _that.itemVerIdsString = '';
      }else {
        var rowsItemVerIds = [];
        selArr.map(function(row){
          if(row){
            if(row.isDone !== 'FINISHED' && row.isDone !== 'HANDLING'&&(!row.notRegionData)){
              flag=true;
            }else {
              flag = false;
            }
            if(flag){
              if(_that.parallelItemsQuestionFlag==true&&row.implementItemVerId) { // 并联事项包含情形时
                var tabList = _that.model.matsTableData;
                var tabListLen = tabList.length;
                var selStateIds = _that.getStatusListId();
                for(var i=0;i<tabListLen;i++) {
                  var obj = tabList[i];
                  if (obj && (obj._itemType == "parallelItems")) { // 清空事项下材料
                    if (obj._itemVerIds.length == 1 && (typeof obj._parStateId == 'undefined' || obj._parStateId.length == 0 || (obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0))) {
                      _that.removeParallelItemsMats.push(obj);
                      _that.model.matsTableData.splice(i, 1);
                      obj._itemVerIds = [];
                      i--
                    } else {
                      var index = obj._itemVerIds.indexOf(row.itemVerId);
                      if (index > -1) {
                        obj._itemVerIds.splice(index, 1);
                      }
                    }
                  }
                }
                _that.getStatusMatItemsByStatus(row);
//                 _that.showParallelItemsKey.push(row.itemBasicId);
              }else { // 并联事项不包含情形时
                if(row.itemVerId){
                  rowsItemVerIds.push(row.itemVerId);
                }
              }
            }
          }
        });
        _that.itemVerIdsString = rowsItemVerIds.join(',');
      }
      if(_that.parallelItemsQuestionFlag!==true) {
        _that.getOfficeMats(_that.itemVerIdsString);
      }
    },
    // 获取并行情形列表id
    getCoreItemsStatusListId: function(cStateList){
      var stateListLen = cStateList.length;
      var selStateIds = [];
      for(var i=0;i<stateListLen;i++){  // 已选并联情形id集合
        if(cStateList[i].selectAnswerId!==undefined||cStateList[i].selectAnswerId!==null){
          // selStateIds=[];
          // return true;
          if(typeof(cStateList[i].selectAnswerId)=='object'&&cStateList[i].selectAnswerId.length>0){
            selStateIds = selStateIds.concat(cStateList[i].selectAnswerId);
          }else if(cStateList[i].selectAnswerId !== '') {
            selStateIds.push(cStateList[i].selectAnswerId);
          }
        }
      }
      selStateIds=selStateIds.filter(function(item, index) {
        //当前元素，在原始数组中的第一个索引==当前索引值，否则返回当前元素
        return selStateIds.indexOf(item, 0) === index
      })
      return selStateIds;
    },
    // 根据并行事项情形获取情形和材料
    getCoreItemsStateMats: function (cStateList,pData,data,indexQues,flag,checkFlag) { // flag='coreItem' 并行事项， checkFlag复选框是否选中 选中true
      var parentStateId = data.parentStateId;
      var _that = this;
      var questionStateId = pData.itemStateId?pData.itemStateId:'';
      var _itemVerId = data.itemVerId;
      var _parentId = data.itemStateId?data.itemStateId:'ROOT';
      var selStateIds = [];
      if(checkFlag==false){
        var stateListLen = cStateList.length;
        selStateIds=_that.getCoreItemsStatusListId(cStateList);
        for (var i = 0;i<stateListLen;i++) { // 清空情形下所对应情形
          var obj = cStateList[i];
          if(obj&&(obj.parentStateId == _parentId)||(obj&&obj.parentStateId!=null&&(selStateIds.indexOf(obj.parentStateId)==-1))){
            if(typeof(obj.selectAnswerId)=='object'&&obj.selectAnswerId.length>0){
              obj.selectAnswerId.map(function(itemSel){
                selStateIds=selStateIds.filter(function(item, index) {
                  return item !== itemSel;
                })
              });
            }else if(obj.selectAnswerId !== '') {
              selStateIds=selStateIds.filter(function(item, index) {
                return item !== obj.selectAnswerId;
              })
            }
            cStateList.splice(i, 1);
            i--
          }
        }
        selStateIds=_that.getCoreItemsStatusListId(cStateList);
        // 清空对应情形下所有材料
        for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
          var obj = _that.model.matsTableData[i];
          if(obj &&obj.itemStateId!=null&& (obj._parStateId.indexOf(_parentId)>-1||selStateIds.indexOf(obj.itemStateId)==-1||(obj.parStateId&&selStateIds.indexOf(obj.parStateId)<0))){
            if(obj._parStateId.length==1){
              obj._parStateId = [];
              _that.model.matsTableData.splice(i, 1);
              i--
            }else {
              var index = obj._parStateId.indexOf(_parentId);
              if (index > -1) {
                obj._parStateId.splice(index, 1);
              }
            }

          }
        }
        return false
      }
      var params={
        itemVerId: _itemVerId,
        parentId: _parentId,
      }
      request('', {
        url: ctx+'rest/mats/states/mats/'+_itemVerId+'/'+_parentId,
        data: params,
        type: 'get'
      }, function (data) {
        if(data.success){
          var coreItemsMats = [];
          if(checkFlag==true){
            data.content.questionStates.map(function(item,ind){ // 情形下插入对应的情形
              if(item.answerType!='s'&&item.answerType!='y'){
                if(typeof item.selValue == "undefined"){
                  Vue.set(item, 'selValue', []);
                }else {
                  item.selValue = [];
                }

                item.selectAnswerId=item.selValue;
              }
              cStateList.splice((indexQues+1+ind),0,item);
            });
            // 选择情形后返回的材料列表
            data.content.stateMats.map(function(item){
              if(item._parStateId=='undefined'||item._parStateId==undefined){
                Vue.set(item, '_parStateId', [_parentId]);
              }else {
                var parInd = item._parStateId.indexOf(_parentId)
                if(parInd==-1){
                  item._parStateId.push(_parentId);
                }
              }
              if(item._itemVerIds=='undefined'||item._itemVerIds==undefined){
                Vue.set(item,'_itemVerIds',[_itemVerId]);
              }else {
                item._itemVerIds.push(_itemVerId);
              }
              item.itemStateId=_parentId;
              coreItemsMats.push(item)
            });
          }else {
            var stateListLen = cStateList.length;
            selStateIds=_that.getCoreItemsStatusListId(cStateList);
            for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
              var obj = cStateList[i];
              if(obj&&(obj.parentQuestionStateId == questionStateId)||(obj&&obj.parentStateId!=null&&(selStateIds.indexOf(obj.parentStateId)==-1))){
                if(typeof(obj.selectAnswerId)=='object'&&obj.selectAnswerId.length>0){
                  obj.selectAnswerId.map(function(itemSel){
                    selStateIds=selStateIds.filter(function(item, index) {
                      return item !== itemSel;
                    })
                  });
                }else if(obj.selectAnswerId !== '') {
                  selStateIds=selStateIds.filter(function(item, index) {
                    return item !== obj.selectAnswerId;
                  })
                }
                cStateList.splice(i, 1);
                i--
              }
            }
            data.content.questionStates.map(function (item, ind) { // 情形下插入对应的情形
              if (item.answerType != 's' && item.answerType != 'y') {
                if(typeof item.selValue == "undefined"){
                  Vue.set(item, 'selValue', []);
                }else {
                  item.selValue = [];
                }
                item.selectAnswerId=item.selValue;
              }
              cStateList.splice((indexQues + 1 + ind), 0, item);
            });
            // 选择情形后返回的材料列表
            data.content.stateMats.map(function(item){
              if(item._parStateId=='undefined'||item._parStateId==undefined){
                Vue.set(item, '_parStateId', [parentStateId]);
              }else {
                item._parStateId.push(parentStateId);
              }
              if(item._itemVerIds=='undefined'||item._itemVerIds==undefined){
                Vue.set(item,'_itemVerIds',[_itemVerId]);
              }else {
                item._itemVerIds.push(_itemVerId);
              }
              item.itemStateId=_parentId;
              coreItemsMats.push(item)
            });
            selStateIds=_that.getCoreItemsStatusListId(cStateList);
            for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
              var obj = _that.model.matsTableData[i];
              if(obj._parStateId=='undefined'||obj._parStateId==undefined){
                Vue.set(obj, '_parStateId', []);
              }else {
                if(obj &&obj.itemStateId!=null&&(obj._parStateId.indexOf(parentStateId)>-1||selStateIds.indexOf(obj.itemStateId)==-1)){
                  if(obj._parStateId.length==1){
                    obj._parStateId = [];
                    _that.model.matsTableData.splice(i, 1);
                    i--
                  }else {
                    var index = obj._parStateId.indexOf(parentStateId);
                    if (index > -1) {
                      obj._parStateId.splice(index, 1);
                    }
                  }
                }
              }
            }
          }
          coreItemsMats.map(function(item){
            if(item){
              if(item.children=='undefined'||item.children==undefined){
                Vue.set(item,'children',[]);
              }
              if(item.matinstId=='undefined'||item.matinstId==undefined){
                Vue.set(item,'matinstId','');
              }
              if(item.getPaper=='undefined'||item.getPaper==undefined){
                Vue.set(item,'getPaper','');
              }
              if(item.getCopy=='undefined'||item.getCopy==undefined){
                Vue.set(item,'getCopy','');
              }
              if(item.realPaperCount=='undefined'||item.realPaperCount==undefined){
                Vue.set(item,'realPaperCount',item.duePaperCount);
              }
              if(item.realCopyCount=='undefined'||item.realCopyCount==undefined){
                Vue.set(item,'realCopyCount',item.dueCopyCount);
              }
              if(flag=='coreItem'){
                if(item._itemType=='undefined'||item._itemType==undefined){
                  Vue.set(item,'_itemType','coreItem');
                }
              }else {
                if(item._itemType=='undefined'||item._itemType==undefined){
                  Vue.set(item,'_itemType','parallelItems');
                }
              }
            }
          });
          coreItemsMats=_that.unique(coreItemsMats,'mats');

          _that.model.matsTableData=_that.unique(_that.model.matsTableData.concat(coreItemsMats),'mats');
          _that.matIds=[];
          _that.model.matsTableData.map(function (item) {
            if(item){
              _that.matIds.push(item.matId);
            }
          });
          _that.ifMatsSelAll();
        }else {
          _that.$message({
            message: data.message?data.message:'获取情形材料失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: msg.message?msg.message:'获取情形材料失败',
          type: 'error'
        });
      });
    },
    sortByKey: function (array,key){  //(数组、排序的列)
      return array.sort(function(a,b){
        var x=a[key];
        var y=b[key];
        return ((x<y)?-1:((x>y)?1:0));
      });
    },
    // 分局承办部门组织树
    showOrgTree: function (data) {
      var _that = this;
      var parentOrgId = data.orgId?data.orgId:'';
      if(data.branchOrg==null||data.branchOrg==undefined){
        Vue.set(data,'branchOrg','');
      }
      _that.selCoreItems=data;
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
        }else {
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
      this.selCoreItems.branchOrg = rowData.id;
      this.selCoreItems.orgName = rowData.name;
      this.orgTreeInfoModal = false;
    },
    // 刷新页面
    reloadPage: function(){
      if(this.submitCommentsType==0||this.submitCommentsType==3||this.submitCommentsType==5){
        window.location.reload()
      }
    },
    // 设置进度条的值
    setUploadPercentage: function () {
      var _that = this;
      _that.uploadPercentage=0;
      var interval = setInterval(function(){
        _that.uploadPercentage += 3;
        if(_that.uploadPercentage >= 96 || _that.progressIntervalStop == true){
          clearInterval(interval);
        }
      }, 300);
    },
    // 显示共享材料
    showShareModal: function (data) {
      this.selMatRowData = data;
      this.searchShareFileList(data);
      this.getFileListWindowFlag=true;
    },
    // 查询要导入的电子件列表
    searchShareFileList: function (data) {
      var _that = this;
      var unitInfoId='';
      var unitInfoIds=[];
      _that.jiansheFrom.map(function(item){
        if(item){
          unitInfoIds.push(item.unitInfoId);
        }
      })
      _that.agentUnits.map(function(item){
        if(item){
          unitInfoIds.push(item.unitInfoId);
        }
      })
      unitInfoId=unitInfoIds.join(',');
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
        }else {
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
    fileImport: function(){
      var _that = this;
      var unitInfoId='';
      var unitInfoIds=[];
      var fileIds = '';
      var fileIdList = [];
      var selFileList = _that.$refs.shareFileList.selection;
      if(selFileList.length==0){
        _that.$message({
          message: '请选择需要导入的文件！',
          type: 'error'
        });
        return true;
      }
      selFileList.map(function(item){
        if(item){
          fileIdList.push(item.detailId);
        }
      });
      fileIds=fileIdList.join(',')
      _that.jiansheFrom.map(function(item){
        if(item){
          unitInfoIds.push(item.unitInfoId);
        }
      })
      _that.agentUnits.map(function(item){
        if(item){
          unitInfoIds.push(item.unitInfoId);
        }
      })
      unitInfoId=unitInfoIds.join(',');
      var parmas= {
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
        }else {
          _that.$message({
            message: '导入文件失败！',
            type: 'error'
          });
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    // 根据阶段id查询关联的表单
    getOneFormList: function (_stageId) {
      var _that = this;
      request('', {
        url: ctx + 'rest/stage/oneforms',
        type: 'get',
        data: {stageId: _stageId}
      }, function (result) {
        if (result.success) {
          _that.oneFormData=result.content;
          if(_that.oneFormData.length>0&&_that.firstGetneForm==0){
            var obj = {
              label: '一张表单',
              target: 'oneForm'
            }
            _that.verticalTabData.splice(3, 0, obj);
            _that.showVerLen = _that.verticalTabData.length;
            _that.firstGetneForm++
          }else if(_that.oneFormData.length==0&&_that.firstGetneForm>0){
            _that.verticalTabData.splice(3, 1);
            _that.firstGetneForm=0
          }
        }else {
          _that.$message({
            message: '获取一张表单失败！',
            type: 'error'
          });
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    getOneFormrender2: function(str,_applyinstId,_stageId){ // 获取一张表单render
      var _that = this;
      request('', {
        url: ctx + 'rest/oneform/common/renderHtmlFormContainer?applyinstId='+_applyinstId+'&stageId='+_stageId+'&'+str,
        type: 'get',
      }, function (result) {
        if (result.success) {
          _that.oneFormDialogVisible = true;
          $('#oneFormContent').html(result.content)
          _that.$nextTick(function(){
            $('#oneFormContent').html(result.content)
          });
        }else {
          _that.$message({
            message: result.content?result.content:'获取表单失败！',
            type: 'error'
          });
        }
      },function(res){
        _that.$message({
          message: '获取表单失败！',
          type: 'error'
        });
      })
    },
    // 展示一张表单弹窗
    showOneFormDialog: function(){
      var _that = this;
      var selItemVer = _that.$refs.parallelItemsTable.selection; // 所有选择的并联审批事项
      var str = '';
      var _applyinstId = _that.parallelApplyinstId;
      if(selItemVer.length==0){
        _that.$message({
          message: '未选择并联事项，无关联一张表单！',
          type: 'error'
        });
        return false;
      }
      selItemVer.map(function(item){
        if(item){
          str += 'itemids='+item.itemId+'&'
        }
      });
      _that.formItemsIdStr = str;
      if(_applyinstId==''){
        _that.showCommentDialog('4')
      }else {
        _that.getOneFormrender2(str,_applyinstId,_that.stageId)
      }

    },
    //  打开全景流程图
    goToDiagramPage: function () {
      var _that = this;
      request('', {
        url: ctx + 'rest/project/diagram/status',
        type: 'get',
        data: {projInfoId: _that.projInfoId, themeId: _that.themeId}
      }, function (result) {

      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    // 获取文件后缀
    getFileType: function(fileName){
      var index1=fileName.lastIndexOf(".");
      var index2=fileName.length;
      var suffix=fileName.substring(index1+1, index2).toLowerCase();//后缀名
      return suffix;
    },
    // 根据顶级组织ID查询区划列表  rest/org/region/list
    getDistrictList: function () {
      var _that = this;
      request('', {
        url: ctx + 'rest/org/region/list',
        type: 'get',
      }, function (result) {
        if(result.content){
          _that.districtList = result.content;
        }else {
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
    getProjThemeIdList: function(){
      var _that = this;
      _that.loadingThemeIdList = true;
      request('', {
        url: ctx + 'rest/theme/theme/type/list',
        type: 'get',
        data: {themeType: _that.projBascInfoShow.projType}
      }, function (result) {
        if(result.content){
          _that.projThemeIdList = result.content;
          _that.loadingThemeIdList = false;
        }else {
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
    setShowUnitMore: function(index){
      if(this.showUnitMore.indexOf(index)>-1){
        this.showUnitMore = this.showUnitMore.filter(function(item) {
          return item != index;
        });
      }else {
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
          applyinstId:ts.parallelApplyinstId?ts.parallelApplyinstId:ts.applyinstIds[0]
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
          ts.$message.error(res.message?res.message:'网络错误！');
        }
      }, function () {
        ts.sloading = false;
        ts.$message.error('网络错误！');
      });
    },
    // 处理材料补全回显数据
    handelSupplementDataMend: function(){
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
    getMatsMend: function(matsMendList){
      matsMendList.map(function(item){
        if(typeof item.isNeedOrign == "undefined"){
          Vue.set(item,'isNeedOrign', false);
        }else {
          item.isNeedOrign = false;
        }
        if(typeof item.isNeedCopy == "undefined"){
          Vue.set(item,'isNeedCopy', false);
        }else {
          item.isNeedCopy = false;
        }
        if(typeof item.isNeedElectron == "undefined"){
          Vue.set(item,'isNeedElectron', false);
        }else {
          item.isNeedElectron = false;
        }
        if(item.paperCount && item.paperCount!==null && parseInt(item.paperCount) > 0){
          //是否需要展示原件
          item.isNeedOrign = true;
        }
        if(item.copyCount  && item.copyCount!==null && parseInt(item.copyCount) > 0){
          //是否需要展示复印件
          item.isNeedCopy = true;
        }
        if(item.isNeedAtt=='1'){
          //是否需要展示电子件
          item.isNeedElectron = true;
          //当需要扎实电子件时，默认将isNeedAtt置为true
          // item.isNeedAtt = '1';
        }
      });
    },
    // 处理已提交材料列表 已上传文件
    handelMatCorrectDtosMend: function(){
      var _that = this;
      var subLen = _that.submittedMats.length;
      var matLen = _that.matCorrectDtos.length;
      _that.submittedMats.map(function(item){
        if(typeof item.isNeedOrignSel == "undefined"){
          Vue.set(item,'isNeedOrignSel', false);
        }else {
          item.isNeedOrignSel = false;
        }
        if(typeof item.isNeedCopySel == "undefined"){
          Vue.set(item,'isNeedCopySel', false);
        }else {
          item.isNeedCopySel = false;
        }
        if(typeof item.isNeedElectronSel == "undefined"){
          Vue.set(item,'isNeedElectronSel', false);
        }else {
          item.isNeedElectronSel = false;
        }
        _that.matCorrectDtos.map(function(item1){
          if(item.matId == item1.matId){
            if(item.paperCount>0&&item1.paperCount>0){
              item.isNeedOrignSel = true;
              item1.isNeedOrign = true;
            }
            if(item.copyCount>0&&item1.copyCount>0){
              item.isNeedCopySel = true;
              item1.isNeedCopy = true;
            }
            if(item.attCount>0&&item1.attCount>0){
              item.isNeedElectronSel = true;
              item1.isNeedElectron = true;
            }
          }
        })
      });
    },
    // 删除待补全材料列表中的某个材料的某一项（原件|复印件|电子件）
    delTobaMakeupMaterialMend: function(attr,item,index){ // attr移除的材料类型 item材料信息 index
      var _that = this;
      if(attr == 'isNeedOrign'){ // 删除原件
        item.isNeedOrign = false;
        item.paperCount = 0;
      }
      if(attr == 'isNeedCopy'){ // 删除复印件
        item.isNeedCopy = false;
        item.copyCount = 0;
      }
      if(attr == 'isNeedElectron'){ // 删除电子件
        item.isNeedAtt = '0';
        item.attCount = 0;
        item.isNeedElectron = false;
      }
      _that.handelMatCorrectDtosMend();
      if(!item.isNeedOrign&&!item.isNeedCopy&&!item.isNeedElectron){
        _that.matCorrectDtos.splice(index,1);
      }

    },
    // 保存材料补全的数据
    saveMaterialCorrectionMend: function(){
      var ts = this, saveData = {};
      this.$refs['matMendForm'].validate(function(valid) {
        if (valid) {
          // 校验补正份数的大小
          var _noPip = {};
          for(var i = 0; i<ts.matCorrectDtos.length; i++){
            var _curMat = ts.matCorrectDtos[i];
            if(_curMat.isNeedOrign && _curMat.paperCount < 1){
              _noPip = _curMat;
              break;
            }
            if(_curMat.isNeedCopy && _curMat.copyCount < 1){
              _noPip = _curMat;
              break;
            }
          }
          if(JSON.stringify(_noPip) !== "{}"){
            return ts.$message.error('材料：‘' + _noPip.matName + '的补全份数不能为0！请重新输入');
            return false;
          }
          // return true;
          if(ts.matCorrectDtos.length==0){
            return ts.$message.error('材料补全份数不能为0！');
            return false;
          }
          saveData = {
            applyinstId: ts.parallelApplyinstId?ts.parallelApplyinstId:ts.applyinstIds[0],
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
              confirmMsg('提示信息：','材料补全成功，是否打印回执？',function(){
                ts.queryReceiveList(ts.applyinstIds.join(','));
              },function(){
                ts.reloadPage();
              },'是','否','success',true)
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
    clickTobaSelectMaterial: function(val,dataMat,attrFlag) { // attrFlag 选择的材料类型
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
        };
        if(flag){
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
      }else {
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
      _that.matCorrectDtos.map(function (item,index) {
        if(!item.isNeedOrign&&!item.isNeedCopy&&!item.isNeedElectron){
          _that.matCorrectDtos.splice(index,1);
        }
      })
    },
    isGetReceiveList: function(){
      var _that = this;
      confirmMsg('提示信息：','是否打印回执？',function(){
        _that.queryReceiveList(_that.applyinstIds.join(','));
      },function(){
        _that.reloadPage();
      },'是','否','success',true)
    },
  },
  filters: {
    changeReceiveType: function (value) {
      var defaultValue='其他回执';
      //{{(itemBtn.receiptType==2)?'收件回执':(itemBtn.receiptType==1)?'物料回执':(itemBtn.receiptType==3)?'不受理回执':'其他回执'}}
      if(value){
          switch(value) {
          case '1':
            defaultValue='物料回执';
            break;
          case '2':
            defaultValue='受理回执';
            break;
          case '3':
            defaultValue='不受理回执';
            break;
          case '4':
            defaultValue='退件回执';
            break;
          case '5':
            defaultValue='送达回证';
            break;
          case '6':
            defaultValue='材料补正回执';
            break;
          case '7':
            defaultValue='行政许可申请书';
            break;
          default:
            defaultValue='其他回执';
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

  }
});

