/*
 * @Author: ZL
 * @Date:   2019/6/4
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var module1 = new Vue({
  el: '#parallelDeclare',
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
    // 输入为数字 大于等于0
    var checkMissNum = function (rule, value, callback) {
      if (value === '' || value === undefined || value === null || value.toString().trim() === '') {
        callback(new Error('必填字段！'));
      } else if (value) {
        var flag = !/^[0-9]\d*$/.test(value) && !(/^[0-9]\d*$/.test(value));
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
    var checkUnifiedSocialCreditCode = function (rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('请输入统一社会信用代码！'));
      } else if (value) {
        var flag = !/[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}/.test(value);
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
      ctx: ctx,
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      loading: false, // 全屏loading
      loadingFile: false, // 文件上传loading
      loadingFileWin: false, // 窗口文件上传loading
      declareStep: 1, // 申报步骤 1-补全信息 2-选择主题 3-选择阶段 4-事项材料一单清
      declareStepList: [  // 所有的步骤
        { num: '1', name: '补全信息' },
        { num: '2', name: '主题引导' },
        { num: '3', name: '选择主题' },
        { num: '4', name: '选择服务' },
        { num: '5', name: '事项一单清' },
        { num: '6', name: '一张表单' },
        { num: '7', name: '材料一单清' },
        { num: '8', name: '完成申报' },
      ],
      itemTabSelect: '', // 选择主题-选中tab
      themeActive: '', // 选中主题样式
      isShowAll: false,  // 是否展示更多事项材料一单清
      projInfoId: '', // 查询项目id
      projInfoDetail: {
        isAreaEstimate: '0',
        isDesignSolution: '0',
        gbCodeYear: '2017'
      }, // 项目详细信息
      projTypeList: [], // 项目类型列表
      districtList: [],//行政区划
      projLevelList: [],//项目级别
      projClassList: [],//立项类型
      projNatureList: [], // 建设性质
      tzlxList: [], // 投资类型
      zjlyList: [], // 资源来源
      gbhyList: [], // 行业类别
      tdlyList: [], // 土地来源
      jsxzList: [], // 建设性质
      gcflList: [], // 工程分类
      themeList: [], // 主题列表
      orgList: [], // 组织列表
      applyObjectInfo: {}, // 申报主体信息
      showObjectRole: false, // 是否展示申报主体切换radio
      colStatusActive: 0, // 事项一单清 纵向状态
      guideList: [], // 引导列表
      guideCheckboxAnswer: [],
      factorIds: [],
      projInfoList: [],//项目信息
      searchKeyword: '',
      getResultForm: {
        receiveMode: 1,
        smsType: '',
        addresseeName: '',
        addresseePhone: '',
        addresseeIdcard: '',
      }, // 结果领取方式
      linkmanInfoList: [], // 联系人列表
      aeaUnitList: [], // 单位列表
      provinceList: [], // 省份列表
      cityList: [], // 市列表
      countyList: [], // 区列表
      rulesResultForm: { // 办证结果取件方式信息校验
        addresseeName: [
          { required: true, validator: checkMissValue, trigger: 'change' },
        ],
        addresseePhone: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        addresseeIdcard: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ]
      },
      rulesResultForm1: {
        addresseeName: [
          { required: true, validator: checkMissValue, trigger: 'change' },
        ],
        addresseePhone: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        addresseeIdcard: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        receiveMode: [
          { required: true, message: "必选", trigger: 'change' },
        ],
        smsType: [
          { required: true, message: "必选", trigger: 'change' },
        ],
        addresseeProvince: [
          { required: true, validator: checkMissValue, trigger: ['change', 'blur'] },
        ],
        addresseeCity: [
          { required: true, validator: checkMissValue, trigger: ['change', 'blur'] },
        ],
        addresseeCounty: [
          { required: true, validator: checkMissValue, trigger: ['change', 'blur'] },
        ],
        addresseeAddr: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ]
      },
      rulesCompanyForm: { // 编辑新增单位信息校验
        unitType: [
          { required: true, message: '请选择单位类型！', trigger: 'change' },
        ],
        applicant: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        idrepresentative: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanCertNo: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanMobilePhone: [
          { required: true, validator: checkPhoneNum, trigger: 'blur' },
        ],
        linkmanName: [
          { required: true, message: '请选择联系人！', trigger: ['change', 'blur'] },
        ],
        unifiedSocialCreditCode: [
          { required: true, validator: checkUnifiedSocialCreditCode, trigger: ['blur'] },
        ]
      },
      themeTypeList: [], // 主题类型主题列表
      smsInfoId: '', // 领件人实例id
      themeId: '', // 所选主题id
      themeName: '', // 所选主题名称
      themeIdFlag: false, // 项目是否已存在主题id
      themeType: '', // 所选主题类型
      stageList: [], // 阶段列表
      showStageType: false, // 是否展示阶段分类
      statusLineList: [], // 主题下阶段类型
      statusLineListActive: 0, // 主题下阶段类型选中状态
      isSelItem: 0, // 事项列表是否展示复选框 0否 1是
      statusActiveIndex: -1, // 申报阶段选中阶段index
      stageId: '', // 选中阶段id
      stageName: '', // 选中阶段name
      useOneForm: '', // 是否有一张表单
      stateList: [], // 情形列表
      stateSelVal: {},
      guideSelVal: {},
      parallelItems: [], // 并联事项列表
      coreItems: [], // 并行事项列表
      selCoreItemsKey: [], // 单项事项展开展示情形数组
      selParallelItemsKey: [], // 并联事项展开展示情形数组
      parallelItemsQuestionFlag: true, // 并联事项是否包含情形
      AllFileList: [], // 智能分拣区所选择文件
      checkedSelFlie: [], // 智能分拣区已选文件
      checkAll: true, // 智能分拣区文件是否全选
      fileList: [],
      showFileListKey: [], // 展示材料下文件列表
      allMatsTableData: [], // 材料列表
      showMatsTableData: [], // 可展示的材料列表
      matinstIds: [], // 材料实例Ids
      matIds: [], // 材料ids
      unitInfoId: '', // 单位申报单位id
      userInfoId: '', // 个人申报用户ID
      unifiedSocialCreditCode: '', // 企业统一社会信用代码
      userLinkmanCertNo: '', // 个人申报时个人身份证号
      applyinstIds: [], // 申请实例ID
      selMatRowData: {}, // 所选择的材料信息
      selMatRowDataInd: 0, // 所选择的材料的index
      showUploadWindowFlag: false, // 上传材料弹窗
      uploadTableData: [],
      regionalism: '', // 区域编码
      progressDialogVisible: false, // 是否显示进度条loading
      progressIntervalStop: false, // 定时器
      uploadPercentage: 0, // 进度条百分比
      progressStus: null, // 进度条状态
      propulsionItemVerIds: [], // 并行事项版本ids
      itemVerIds: [], // 并联事项版本ids
      propulsionItemStateIds: [], // 并行事项情形Map集合
      parallelItemStateIds: [], // 简单合并申报，选择的并联事项情形
      applySuccessProjInfo: {}, // 申报成功返回项目信息
      _stageStateIds: [],
      _itemStateIds: [],
      baseInfoShow: true, // 是否展示项目基本信息
      applyObjectInfoShow: true, // 是否展示申报主体信息
      getResultFormShow: true, // 是否展示取件方式信息
      showStageList: true, // 是否展示选择阶段
      showStateList: true, // 是否展示选择情形
      showParallelItems: true, // 是否展示并联事项列表
      showCoreItems: true, // 是否展示并行事项列表
      applySuccessProjInfo1: true, // 是否展示完成申报信息
      applySuccessProjInfo2: true, // 是否展示完成申报信息
      applySuccessProjInfo3: true, // 是否展示完成申报信息
      applySuccessProjInfo4: true, // 是否展示完成申报信息
      showThemeLen: 10, //
      showThemeLenText: true,
      statusLineListAllow: false, // 是否可选服务
      oneFormDataAllow: false, // 是否有一张表单
      intelligenceAllow: false, // 是否有智能引导
      isAble: false,
      rules: {
        projName: [
          { required: true, message: '请输入项目/工程名称', trigger: 'blur' },
        ],
        localCode: [
          { required: true, message: '请输入项目代码', trigger: 'blur' },
        ],
        gcbm: [
          { required: true, message: '请输入工程代码', trigger: 'blur' },
        ],
        regionalism: [
          { required: true, message: '请选择行政区划', trigger: 'change' },
        ],
        projectAddress: [
          { required: true, message: '请选择建设地点', trigger: 'change' },
        ],
        financialSource: [
          { required: true, message: '请选择资金来源', trigger: 'change' },
        ],
        investType: [
          { required: true, message: '请选择投资类型', trigger: 'change' },
        ],
        landSource: [
          { required: true, message: '请选择土地来源', trigger: 'change' },
        ],
        projType: [
          { required: true, message: '请选择立项类型', trigger: 'change' },
        ],
        themeId: [
          { required: true, message: '请选择项目类型', trigger: 'change' },
        ],
        projNature: [
          { required: true, message: '请选择建设性质', trigger: 'change' },
        ],
        projCategory: [
          { required: true, message: '请选择工程分类', trigger: 'change' },
        ],
        nstartTime: [
          { required: true, message: '请选择拟开工时间', trigger: 'blur' },
        ],
        endTime: [
          { required: true, message: '请选择拟建成时间', trigger: 'change' },
        ],
        investSum: [
          { required: true, message: '请输入总投资', trigger: 'blur' },
        ],
        foreignBuildingArea: [
          { required: true, message: '请输入总建筑面积', trigger: 'blur' },
        ],
        xmYdmj: [
          { required: true, message: '请输入用地面积', trigger: 'blur' },
        ],
        theIndustry: [
          { required: true, message: '请选择国标行业！', trigger: ['blur', 'change'] },
        ],
        gbCodeYear: [
          { required: true, message: '请输入国标行业代码发布年代', trigger: 'blur' },
        ],
        scaleContent: [
          { required: true, message: '请输入建设规模及内容', trigger: 'blur' },
        ]
      },
      addLinkManRules: { // 新增编辑联系人校验
        linkmanName: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanCertNo: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanMobilePhone: [
          { required: true, validator: checkPhoneNum, trigger: 'blur' },
        ]
      },
      addEditManModalTitle: '新增联系人',
      paraParentllelItemVerIds: [], // 并联标准事项版本ID数组
      coreParentItemVerIds: [], // 并行标准事项版本ID数组
      loadingOneForm: false, // 一张表单loading
      parallelApplyinstId: '', // 并联申报实例id
      branchOrgMap: [],
      propulsionBranchOrgMap: [],
      creditTypeList: [], // 信用类型数据
      creditDiaVisible: false, // 是否展示信用弹窗
      creditDiaLoading: false, // 是否展示信用弹窗loading
      isBlack: false, // 是否黑名单
      loseCreditCount: 0, // 失信记录条数
      showUnitMore: false, // 是否展示更多企业信息
      filePreviewCount: 0,
      attIsRequireFlag: true, // 电子件已通过
      gbhySelectData: [], // 国标行业下拉选项数据
      gbhyProp: {
        children: 'children',
        label: 'itemName'
      }, // 树配置信息，节点属性以及显示文案的属性
      gbhyShowMsg: '', // 国标行业选中数据的展示
      isShowGbhy: false, // 是否显示国标行业tree模块
      jiansheFrom: [], // 建设单位列表
      projUnitList: [], // 建设单位列表
      xmdwlxList: [], // 单位类型
      projUnitLinkmanType: [], // 人员设置类型
      showUnitListMore: [], // 展示更多单位信息

      addEditManModalShow: false, // 是否展示新增编辑联系人窗口
      addEditManform: {},  // 新增编辑联系人信息
      addEditManPerform: {}, // 所选编辑的联系人
      unitProjIds: [],
      fileSpaceActive: 'myMats',
      fileSelActive: 'myUploadMat',
      myMatsList: [], // 我的材料列表
      myDirsList: [], // 我的云盘列表
      myMatsPageNum: 1, // 我的材料分页
      myMatsPageSize: 10, // 我的材料分页
      myMatsKeyword: '', // 我的材料分页
      myMatsTotal: 0, // 我的材料总数
      mySpaceLoading: false, // 我的材料云盘loading
      selMatLoading: false, // 已上传文件loading
      myDirPageNum: 1, // 我的云盘分页
      myDirPageSize: 10, // 我的云盘分页
      myDirKeyword: '', // 我的云盘分页
      myDirTotal: 0, // 我的云盘总数
      uploadFilesCount: 0, // 已上传文件数量
      myDirMatsList: [], // 我的空间材料列表
      myDirMatsId: [], // 我的空间材料列表Id
      showCertWindowFlag: false, // 是否展示证照库窗口
      certMatTableData: [], // 证照库列表
      searchInline: {
        identityNumberType: '0',
        showNumberType: true,
      }, // 证照库条件查询
      allApplySubjectInfo: [], // 证照库条件查询
      selApplySubject: {}, // 选中的建设单位 申报主体
      matformNameTitle: '材料表单',
      matFormDialogVisible: false,
    }
  },
  mounted: function () {
    var _that = this;
    if (_that.projInfoId != 'null') {
      _that.searchProjAllInfo(); // 获取项目信息
    }
  },
  created: function () {
    this.GetRequest();
    this.getDicContent(); // 数据字典
    this.getGbhy();
  },
  methods: {
    // 文件上传获取我的材料列表
    getMyMatsList: function () {
      var _that = this;
      var param = {
        pageSize: _that.myMatsPageSize,
        pageNum: _that.myMatsPageNum,
        keyword: _that.myMatsKeyword
      };
      _that.mySpaceLoading = true;
      request('', {
        url: ctx + 'rest/user/mat/list',
        type: 'get',
        data: param
      }, function (result) {
        _that.mySpaceLoading = false;
        if (result) {
          _that.myMatsList = result.list ? result.list : [];
          var uploadFileIds = [];
          if (_that.uploadTableData.length > 0) {
            _that.uploadTableData.map(function (uploadItem) {
              if (uploadFileIds.indexOf(uploadItem.fileId) < 0) {
                uploadFileIds.push(uploadItem.fileId);
              }
            });
          }
          if (_that.myMatsList.length > 0) {
            _that.myMatsList.map(function (item) {
              Vue.set(item, 'checked', false);
              if (uploadFileIds.indexOf(item.fileId) > -1) {
                item.checked = true;
              }
            })
          }
          _that.myMatsTotal = result.total ? result.total : 0;
        }
      }, function (msg) {
        _that.mySpaceLoading = false;
        alertMsg('', '我的材料获取失败', '关闭', 'error', true);
      });
    },
    // 文件上传获取我的云盘列表
    getMyDirsList: function (row, treeNode, resolve) {
      var _that = this;
      var dirId = row ? row.dirId : 'root';
      _that.mySpaceLoading = true;
      request('', {
        url: ctx + 'rest/cloud/dirAndAtt/list/' + dirId,
        type: 'get',
        data: { dirId: dirId }
      }, function (result) {
        _that.mySpaceLoading = false;
        if (result) {
          if (result.content.dirs.length > 0) {
            result.content.dirs.map(function (dirsItem) {
              if (typeof dirsItem.hasChildren == 'undefined') {
                Vue.set(dirsItem, 'hasChildren', true)
              }
              if (typeof dirsItem.id == 'undefined') {
                Vue.set(dirsItem, 'id', dirsItem.dirId)
              } else {
                attsItem.id = attsItem.dirId
              }
            });
          }
          if (_that.myDirMatsList.length > 0) {
            _that.myDirMatsList.map(function (dirMatsItem) {
              if (_that.myDirMatsId.indexOf(dirMatsItem.detailId) < 0) {
                _that.myDirMatsId.push(dirMatsItem.detailId);
              }
            });
          }
          if (result.content.atts.length > 0) {
            result.content.atts.map(function (attsItem) {
              if (typeof attsItem.checked == 'undefined') {
                Vue.set(attsItem, 'checked', false)
              }
              if (typeof attsItem.hasCheck == 'undefined') {
                Vue.set(attsItem, 'hasCheck', true)
              }
              if (typeof attsItem.id == 'undefined') {
                Vue.set(attsItem, 'id', attsItem.detailId)
              } else {
                attsItem.id = attsItem.detailId
              }
              if (_that.myDirMatsId.indexOf(attsItem.detailId) < 0) {
                _that.myDirMatsList.push(attsItem);
              }
            });
          }
          if (!row) {
            _that.myDirsList = result.content.atts.concat(result.content.dirs);
          } else {
            var childdrenArr = result.content.atts.concat(result.content.dirs);
            resolve(childdrenArr)
          }

          var uploadFileIds = [];
          if (_that.uploadTableData.length > 0) {
            _that.uploadTableData.map(function (uploadItem) {
              if (uploadFileIds.indexOf(uploadItem.fileId) < 0) {
                uploadFileIds.push(uploadItem.fileId);
              }
            });
          }
          if (_that.myDirMatsList.length > 0) {
            _that.myDirMatsList.map(function (item) {
              if (typeof item.checked == 'undefined') {
                Vue.set(item, 'checked', false)
              }
              if (uploadFileIds.indexOf(item.detailId) > -1) {
                item.checked = true;
              }
            })
          }
        }
      }, function (msg) {
        _that.mySpaceLoading = false;
        alertMsg('', '我的空间材料获取失败', '关闭', 'error', true);
      });
    },
    // 模糊查询文件上传获取我的云盘c材料列表
    getMyDirsListByKeyword: function () {
      var _that = this;
      if (!_that.myDirKeyword) {
        _that.getMyDirsList();
      } else {
        _that.mySpaceLoading = true;
        request('', {
          url: ctx + 'rest/cloud/attByKeyword/list/' + _that.myDirKeyword,
          type: 'get',
        }, function (result) {
          _that.mySpaceLoading = false;
          if (result) {
            _that.myDirsList = result.content ? result.content : [];
            var uploadFileIds = [];
            if (_that.uploadTableData.length > 0) {
              _that.uploadTableData.map(function (uploadItem) {
                if (uploadFileIds.indexOf(uploadItem.fileId) < 0) {
                  uploadFileIds.push(uploadItem.fileId);
                }
              });
            }
            if (_that.myDirsList.length > 0) {
              _that.myDirsList.map(function (item) {
                if (typeof item.checked == 'undefined') {
                  Vue.set(item, 'checked', false)
                }
                if (typeof item.hasCheck == 'undefined') {
                  Vue.set(item, 'hasCheck', true)
                }
                if (uploadFileIds.indexOf(item.detailId) > -1) {
                  item.checked = true;
                }
                if (typeof item.id == 'undefined') {
                  Vue.set(item, 'id', item.detailId)
                } else {
                  item.id = item.detailId
                }
              })
            }
            _that.myDirMatsList = _that.myDirsList;
          }
        }, function (msg) {
          _that.mySpaceLoading = false;
          alertMsg('', '我的空间材料获取失败', '关闭', 'error', true);
        });
      }

    },
    // 我的材料我的空间切换
    fileSpaceTabClick: function (tab, event) {
      if (tab.name == 'myMats') {
        if (this.myMatsList.length == 0) {
          this.getMyMatsList();
        }
      } else {
        if (this.myDirsList.length == 0) {
          this.getMyDirsList();
        }
      }
    },
    // 切换我的材料选中状态 flag ==dir 我的空间选中材料
    changeMyMatSel: function (val, selMatData, flag) {
      console.log(val, selMatData);
      console.log(this.selMatRowData);
      var _that = this;
      var fileId = flag == 'dir' ? selMatData.detailId : selMatData.fileId;
      var param = {
        detailIds: fileId,
        matId: _that.selMatRowData.matId,
        matinstCode: _that.selMatRowData.matCode,
        matinstName: _that.selMatRowData.matName,
        projInfoId: _that.projInfoId,
        matinstId: _that.selMatRowData.matinstId ? _that.selMatRowData.matinstId : ''
      };
      if (val) {
        request('', {
          url: ctx + 'rest/file/cloud/uploadFile',
          type: 'post',
          data: param
        }, function (result) {
          if (result) {
            console.log(result);
            _that.selMatRowData.matinstId = result.content;
            _that.getFileListWin(result.content, _that.selMatRowData);
          }
        }, function (msg) { });
      } else {
        _that.delSelectFileCom(_that.selMatRowData, selMatData, '', flag);
      }

    },
    // 改变我的材料pageSize
    myMatSizeChange: function (val) {
      this.myMatsPageSize = val;
      this.getMyMatsList();
    },
    // 改变我的材料pageNum
    myMatCurrentChange: function (val) {
      this.myMatsPageNum = val;
      this.getMyMatsList();
    },
    // 改变我的材料pageSize
    myDirSizeChange: function (val) {
      this.myDirPageSize = val;
      this.getMyMatsList();
    },
    // 改变我的材料pageNum
    myDirCurrentChange: function (val) {
      this.myDirPageNum = val;
      this.getMyMatsList();
    },
    // 重置表单
    resetForm: function (formName) {
      this.$refs[formName].resetFields();
      this.selectMat = '';
    },
    // 选择企业联系人信息
    selLinkman: function (pData, data, ind1) {
      if (pData) {
        pData.selectedLinkman = {
          linkmanName: data.linkmanName,
          linkmanInfoId: data.linkmanInfoId,
          linkmanMail: data.linkmanMail,
          linkmanCertNo: data.linkmanCertNo,
          linkmanMobilePhone: data.linkmanMobilePhone,
        }
      }
      if (data) {
        pData.linkmanName = data.linkmanName;
        pData.linkmanInfoId = data.linkmanInfoId;
        pData.linkmanMail = data.linkmanMail;
        pData.linkmanCertNo = data.linkmanCertNo;
        pData.linkmanMobilePhone = data.linkmanMobilePhone;
      }
    },
    // 人员设置选择人员
    selLinkmanTypes: function (pData, data) {
      if (pData) {
        pData.linkmanName = data.linkmanName;
        pData.linkmanInfoId = data.linkmanInfoId;
        pData.linkmanMail = data.linkmanMail;
        pData.linkmanCertNo = data.linkmanCertNo;
        pData.linkmanMobilePhone = data.linkmanMobilePhone;
      }
    },
    // 新增单位信息
    addUnitInfoForm: function () {
      var _that = this;
      var obj = {
        applicant: '',
        idcard: '',
        idno: '',
        idrepresentative: '',
        idtype: '',
        linkmanInfoId: '',
        linkmanName: '',
        linkmanCertNo: '',
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
      _that.jiansheFrom.push(obj);
    },
    // 新增人员设置
    addLinkmanTypes: function (row) {
      var dataType1 = {
        linkmanInfoId: '',
        linkmanType: '',
        linkmanName: ''
      }
      row.push(dataType1);
    },
    // 移除人员设置
    delLinkmanTypes: function (row, index) {
      row.splice(index, 1);
    },
    // 新增编辑联系人信息
    addLinkman: function (data, parData) {
      var _that = this;
      _that.addEditManModalShow = true;
      _that.addEditManPerform = parData;
      if (_that.projInfoId) {
        if (data) {
          _that.addEditManModalTitle = '编辑联系人';
          _that.addEditManModalFlag = 'edit';
          if (!data.linkmanInfoId) {
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
      if (data.linkmanInfoId) {
        request('', {
          url: ctx + 'rest/unit/getLinkmanInfoById/' + data.linkmanInfoId,
          type: 'get',
          ContentType: 'application/json',
          data: { linkmanInfoId: data.linkmanInfoId }
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
        _that.aeaLinkmanInfoList = [];
      }
    },
    // 保存联系人信息
    saveAeaLinkmanInfo: function (linkmanType) {
      var _that = this;
      _that.addEditManform.linkmanType = linkmanType
      _that.$refs['addEditManform'].validate(function (valid) {
        if (valid) {
          _that.loading = true;
          request('', {
            url: ctx + 'rest/user/linkman/save',
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
    // 删除联系人
    delLinkman: function (data, parentData, ind) {
      var _that = this;
      if (!data.linkmanInfoId) {
        alertMsg('提示信息', '联系人ID为空', '关闭', 'warning', true);
        return;
      } else {
        confirmMsg('此操作影响：', '确定要删除该联系人吗？', function () {
          request('', {
            url: ctx + 'rest/user/linkman/delete/' + parentData.unitInfoId + '/' + data.linkmanInfoId,
            type: 'post',
            data: { linkmanInfoId: data.linkmanInfoId, unitInfoId: parentData.unitInfoId }
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
        }, function () { }, '确定', '取消', 'warning', true)
      }
    },
    // 根据单位id获取关联联系人
    getLinkManListByUnitId: function (item, unitInfoId, flag) {
      var _that = this;
      if (unitInfoId) {
        request('', {
          url: ctx + 'rest/user/linkman/getByUnitInfoId/' + unitInfoId,
          type: 'get',
          data: {
            unitInfoId: unitInfoId
          }
        }, function (data) {
          if (data.success) {
            if (flag == 'applyObjectInfo') {
              item.linkmanInfoList = data.content
            } else {
              item.aeaLinkmanInfoList = data.content;
            }
          }
        }, function (msg) {
          _that.$message({
            message: '获取单位关联联系人失败',
            type: 'error'
          });
        });
      } else {
        item.aeaLinkmanInfoList = [];
      }
    },
    setShowUnitMore: function (index) {
      if (this.showUnitListMore.indexOf(index) > -1) {
        this.showUnitListMore = this.showUnitListMore.filter(function (item) {
          return item != index;
        });
      } else {
        this.showUnitListMore.push(index);
      }
    },
    // 申办主体信息 企业非企业申报 单位名称模糊查询
    querySearchJiansheName: function (queryString, cb) {
      var _that = this;
      if (typeof (queryString) != 'undefined') queryString = queryString.trim();
      if (queryString != '') {
        _that.projNameSelect = _that.projUnitList;

        _that.projNameSelect.map(function (item) {
          if (item) {
            Vue.set(item, 'value', item.applicant);
          }
        })
        var results = queryString ? _that.projNameSelect.filter(_that.createFilter(queryString)) : _that.projNameSelect;
        // 调用 callback 返回建议列表的数据
        cb(results);
      }
    },
    // 选择切换申办主体单位
    selUnitInfo: function (val, index) { // val选中单位信息 index单位索引
      if (this.jiansheFrom[index].unitInfoId != val.unitInfoId) {
        this.getPersonOrUnitBlackByBizId(val);
      }
      this.jiansheFrom[index] = val;
      if (val.aeaLinkmanInfoList && val.aeaLinkmanInfoList.length > 0) {
        this.jiansheFrom[index].linkmanInfoId = val.aeaLinkmanInfoList[0].linkmanInfoId;
        this.jiansheFrom[index].linkmanMail = val.aeaLinkmanInfoList[0].linkmanMail;
        this.jiansheFrom[index].linkmanMobilePhone = val.aeaLinkmanInfoList[0].linkmanMobilePhone;
        this.jiansheFrom[index].linkmanName = val.aeaLinkmanInfoList[0].linkmanName;
        this.jiansheFrom[index].linkmanCertNo = val.aeaLinkmanInfoList[0].linkmanCertNo;
      }
    },
    closeGbhyTree: function () {
      this.isShowGbhy = false;
    },
    getGbhy: function () {
      var _that = this;
      request('', {
        url: ctx + 'bsc/dic/code/getItemTreeByTypeId.do',
        type: 'get',
        data: { typeId: 'fadff496-cde1-4c72-90b8-766744b18cb9' },
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
      this.projInfoDetail.theIndustry = ids.join(',');
    },
    // 获取项目id 事项id
    GetRequest: function () {
      var url = location.search; //获取url中"?"符后的字串
      var theRequest = new Object();
      console.log(theRequest);
      if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
          theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
      }
      this.projInfoId = theRequest.projInfoId;
      return theRequest;
    },
    //联动操作
    // projTypeChange:function(value){
    //     var _that=this;
    //     if(value!=null&&""!=value) {
    //         _that.querySelecTheme(value);
    //     }
    // },
    // keyword模糊查询项目信息
    searchProjInfoByKeyword: function (queryString) {
      var _that = this;
      if (typeof (queryString) != 'undefined') queryString = queryString.trim();
      if (queryString != '') {
        request('', {
          url: ctx + 'rest/user/searchProj/projlist/' + queryString,
          type: 'get',
        }, function (result) {
          if (result.content) {
            _that.projInfoList = result.content;
          } else {
            _that.projInfoList = [];
          }
        }, function (msg) {
          _that.projInfoList = [];
        })
      } else {
        _that.projInfoList = [];
      }
    },

    // 获取项目详细信息
    searchProjAllInfo: function () {
      var _that = this;
      _that.loading = true;
      request('', {
        url: ctx + 'rest/apply/common/projInfo/' + _that.projInfoId,
        type: 'get',
      }, function (data) {
        _that.loading = false;
        if (data.success) {
          _that.projInfoDetail = data.content;
          _that.themeType = data.content.themeType;
          _that.themeId = data.content.themeId;
          _that.jiansheFrom = data.content.aeaUnitInfos ? data.content.aeaUnitInfos : [];
          _that.projUnitList = JSON.parse(JSON.stringify(data.content.aeaUnitInfos));
          _that.regionalism = data.content.regionalism;
          _that.rootOrgId = data.content.rootOrgId;
          _that.projType = data.content.projType;
          _that.projInfoId = data.content.projInfoId;
          _that.searchKeyword = data.content.projName + ' （' + data.content.localCode + '）';
          _that.getApplyObjectInfo(); // 获取申报主体
          _that.getGuideList();
          if (!_that.projInfoDetail.isAreaEstimate) _that.projInfoDetail.isAreaEstimate = '0';
          if (!_that.projInfoDetail.isDesignSolution) _that.projInfoDetail.isDesignSolution = '0';
          if (!_that.projInfoDetail.gbCodeYear) _that.projInfoDetail.gbCodeYear = '2017';
          if (!!_that.projInfoDetail.projectAddress) _that.projInfoDetail.projectAddress = _that.projInfoDetail.projectAddress.split(',');
          if (!!_that.projInfoDetail.theIndustry) _that.$refs.gbhy.setCheckedKeys(_that.projInfoDetail.theIndustry.split(','));
          if (data.content.rootOrgId != null && "" != data.content.rootOrgId) {
            _that.querySelentDistrict(data.content.rootOrgId);
            _that.querySelectDept(data.content.rootOrgId);
          }
          _that.querySelecTheme(data.content.projType);
          if (data.content.themeId != null && "" != data.content.themeId) {
            _that.isAble = true;
            _that.intelligenceAllow = true;
          } else {
            _that.isAble = false;
            _that.intelligenceAllow = false;
          }

        } else {
          _that.$message({
            message: data.message ? data.message : '获取项目信息失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.loading = false;
        _that.$message({
          message: msg.message ? msg.message : '获取项目信息失败',
          type: 'error'
        });
      });
    },

    projNameSel: function (item) {
      this.projInfoId = item.projInfoId;
      this.searchProjAllInfo();
      // this.searchKeyword = item.projName+' （'+item.localCode+'）';
    },

    linkQuery: function () {
      var _that = this;
      _that.showUnitListMore = []; // 清空企业展示更多信息列表
      if ("null" != _that.projInfoId && _that.projInfoId) {
        _that.searchProjAllInfo();
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

    // 获取数据字典
    getDicContent: function () {
      var _that = this;
      request('', {
        url: ctx + 'rest/user/getDicContents',
        type: 'get',
      }, function (result) {
        if (result.content) {
          _that.projTypeList = result.content.XM_PROJECT_STEP;
          _that.projNatureList = result.content.XM_NATURE;
          _that.projLevelList = result.content.XM_PROJECT_LEVEL;
          _that.projClassList = result.content.PROJECT_CLASS;
          _that.tzlxList = result.content.XM_TZLX;
          _that.zjlyList = result.content.XM_ZJLY;
          _that.gbhyList = result.content.XM_GBHY;
          _that.tdlyList = result.content.XM_TDLY;
          _that.jsxzList = result.content.XM_NATURE;
          _that.gcflList = result.content.XM_GCFL;
          _that.xmdwlxList = result.content.XM_DWLX;
          _that.projUnitLinkmanType = result.content.PROJ_UNIT_LINKMAN_TYPE;
        } else {
          _that.$message({
            message: data.message ? data.message : '获取项目类型失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: msg.message ? msg.message : '服务请求失败',
          type: 'error'
        });
      });
    },
    // 获取申报主体信息
    getApplyObjectInfo: function () {
      var _that = this;
      request('', {
        url: ctx + 'rest/apply/common/applyObject',
        type: 'get',
        data: { projInfoId: _that.projInfoId }
      }, function (result) {
        if (result.success) {
          var dataType2 = {
            linkmanInfoId: '',
            linkmanType: '',
            linkmanName: ''
          }
          var dataType3 = {
            linkmanInfoId: '',
            linkmanType: '',
            linkmanName: ''
          }
          _that.applyObjectInfo = result.content;
          if (_that.applyObjectInfo.role == 1) {
            _that.showObjectRole = true
            _that.applyObjectInfo.role = '2';
          }
          _that.jiansheFrom.map(function (unitItem) {
            Vue.set(unitItem, 'linkmanInfoId', '');
            Vue.set(unitItem, 'linkmanMail', '');
            Vue.set(unitItem, 'linkmanMobilePhone', '');
            Vue.set(unitItem, 'linkmanName', '');
            Vue.set(unitItem, 'linkmanCertNo', '');
            if (unitItem.linkmanTypes && unitItem.linkmanTypes.length == 0) {
              unitItem.linkmanTypes.push(dataType2)
            }
            if (unitItem && _that.applyObjectInfo.role == 2) {
              _that.getPersonOrUnitBlackByBizId(unitItem);
            }
            if (unitItem.aeaLinkmanInfoList && unitItem.aeaLinkmanInfoList.length > 0) {
              unitItem.linkmanInfoId = unitItem.aeaLinkmanInfoList[0].linkmanInfoId;
              unitItem.linkmanMail = unitItem.aeaLinkmanInfoList[0].linkmanMail;
              unitItem.linkmanMobilePhone = unitItem.aeaLinkmanInfoList[0].linkmanMobilePhone;
              unitItem.linkmanName = unitItem.aeaLinkmanInfoList[0].linkmanName;
              unitItem.linkmanCertNo = unitItem.aeaLinkmanInfoList[0].linkmanCertNo;
            }
          })
          if (_that.applyObjectInfo.aeaLinkmanInfo == null) {
            _that.applyObjectInfo.aeaLinkmanInfo = {
              linkmanName: '',
              linkmanCertNo: '',
              linkmanMobilePhone: '',
            }
          } else {
            _that.userInfoId = _that.applyObjectInfo.aeaLinkmanInfo.linkmanInfoId;
            _that.userLinkmanCertNo = _that.applyObjectInfo.aeaLinkmanInfo.linkmanCertNo;
          };
          if (_that.applyObjectInfo.aeaUnitInfo == null) {
            _that.applyObjectInfo.aeaUnitInfo = {
              applicant: '',
              idcard: '',
            }
          } else {
            _that.unitInfoId = _that.applyObjectInfo.aeaUnitInfo.unitInfoId;
            _that.unifiedSocialCreditCode = _that.applyObjectInfo.aeaUnitInfo.unifiedSocialCreditCode;
            if (_that.applyObjectInfo.aeaUnitInfo.linkmanTypes && _that.applyObjectInfo.aeaUnitInfo.linkmanTypes.length == 0) {
              _that.applyObjectInfo.aeaUnitInfo.linkmanTypes.push(dataType3)
            }
          };
          if (result.content.linkmanInfoList && result.content.linkmanInfoList.length > 0) {
            _that.linkmanInfoList = result.content.linkmanInfoList;
            _that.applyObjectInfo.aeaUnitInfo.linkmanInfoId = result.content.linkmanInfoList[0].linkmanInfoId;
            _that.applyObjectInfo.aeaUnitInfo.linkmanMail = result.content.linkmanInfoList[0].linkmanMail;
            _that.applyObjectInfo.aeaUnitInfo.linkmanMobilePhone = result.content.linkmanInfoList[0].linkmanMobilePhone;
            _that.applyObjectInfo.aeaUnitInfo.linkmanName = result.content.linkmanInfoList[0].linkmanName;
            _that.applyObjectInfo.aeaUnitInfo.linkmanCertNo = result.content.linkmanInfoList[0].linkmanCertNo;
            var selFirstMan = result.content.linkmanInfoList[0];
            _that.queryGetResultMan(selFirstMan)
            _that.queryUnitLinkMan(selFirstMan, _that.applyObjectInfo.aeaUnitInfo);
          } else {
            _that.queryGetResultMan(_that.applyObjectInfo.aeaLinkmanInfo)
          }
          if (result.content.aeaUnitList && result.content.aeaUnitList.length > 0) {
            _that.aeaUnitList = result.content.aeaUnitList;
            _that.querySelentUnit(_that.aeaUnitList[0]);
          }
          _that.getPersonOrUnitBlackByBizId();
        } else {
          _that.$message({
            message: result.message ? result.message : '获取申报主体信息失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: msg.message ? msg.message : '获取申报主体信息失败',
          type: 'error'
        });
      });
    },
    // 获取联系人输入建议
    querySuggesResultMan: function (queryString, cb) {

      var linkmanInfoList = this.linkmanInfoList;
      linkmanInfoList.map(function (item) {
        if (item) {
          Vue.set(item, 'value', item.linkmanName);
        }
      })
      var results = queryString ? linkmanInfoList.filter(this.createFilter(queryString)) : linkmanInfoList;
      // 调用 callback 返回建议列表的数据
      cb(results);
      console.log(queryString);
    },
    createFilter: function (queryString) {
      return function (linkmanInfoList) {
        return (linkmanInfoList.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    // 获取办证结果领取人信息
    queryGetResultMan: function (data) {
      if (data) {
        var selData = JSON.parse(JSON.stringify(data));
        this.getResultForm.addresseeName = selData.linkmanName;
        this.getResultForm.addresseePhone = selData.linkmanMobilePhone;
        this.getResultForm.addresseeIdcard = selData.linkmanCertNo;
      } else {
        this.getResultForm.addresseeName = '';
        this.getResultForm.addresseePhone = '';
        this.getResultForm.addresseeIdcard = '';
      }
    },
    // 获取企业联系人信息
    queryUnitLinkMan: function (data, pData) {
      var selData = JSON.parse(JSON.stringify(data));
      this.userInfoId = data.linkmanInfoId;
      this.userLinkmanCertNo = this.applyObjectInfo.aeaLinkmanInfo.linkmanCertNo;
      this.applyObjectInfo.aeaLinkmanInfo = selData;
      if (pData) {
        pData.selectedLinkman = {
          linkmanName: data.linkmanName,
          linkmanInfoId: data.linkmanInfoId,
          linkmanMail: data.linkmanMail,
          linkmanCertNo: data.linkmanCertNo,
          linkmanMobilePhone: data.linkmanMobilePhone,
        }
      }

    },
    // 委托人获取单位信息
    querySelentUnit: function (data) {
      var selData = JSON.parse(JSON.stringify(data));
      this.applyObjectInfo.aeaUnitInfo = selData;
      if (this.unitInfoId != this.applyObjectInfo.aeaUnitInfo.unitInfoId) {
        this.getPersonOrUnitBlackByBizId(this.applyObjectInfo.aeaUnitInfo);
        this.unitInfoId = this.applyObjectInfo.aeaUnitInfo.unitInfoId;
      }
      this.unifiedSocialCreditCode = this.applyObjectInfo.aeaUnitInfo.unifiedSocialCreditCode;
    },
    // 切换领证模式
    changeReceiveMode: function (val) {
      if (val == 0) {
        this.getResultForm.smsType = 2;
        this.queryProvince();
      }
    },
    // 获取省份信息
    queryProvince: function () {
      var _that = this;
      request('', {
        url: ctx + 'rest/provinces',
        type: 'get'
      }, function (data) {
        _that.provinceList = data.content;
        _that.queryCityData(0);
      })
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
    //获取智能指引列表
    getGuideList: function () {
      var _that = this;
      request('', {
        url: ctx + '/rest/userCenter/apply/factor/list',
        type: 'get',
      }, function (res) {
        if (res.success) {
          _that.guideList = res.content;
        }
      });
    },
    //获取智能指引子列表
    getSubGuide: function (item, index, value) {
      var _that = this;
      request('', {
        url: ctx + '/rest/userCenter/apply/factor/child/list/' + item.factorId,
        type: 'get',
      }, function (res) {
        if (res.success) {
          var parentId = item.parentFactorId;
          if (!value) {
            _that.removeCheckbox(item);
          }
          for (var i = 0; i < _that.guideList.length; i++) {
            for (var j = 0; j < _that.guideList[i].answerFactors.length; j++) {
              if (_that.guideList[i].answerFactors[j].parentFactorId == parentId) {
                _that.getSubqueAndAns(_that.guideList[i].answerFactors[j]);
              }
            }
          }
          if (value) {
            res.content.map(function (item, ind) {
              _that.guideList.splice((index + 1 + ind), 0, item);
            });
          }
        }
      });
    },
    /***迭代循环子问题及答案***/
    getSubqueAndAns: function (obj) {
      var _that = this;
      var guide = _that.guideList;
      for (var i = 0; i < guide.length; i++) {
        if (obj.factorId == guide[i].parentFactorId) {
          for (var j = 0; j < guide[i].answerFactors.length; j++) {
            _that.getSubqueAndAns(_that.guideList[i].answerFactors[j]);
          }
          /***切换问题时移除复选框选中数据***/
          for (var j = 0; j < guide[i].answerFactors.length; j++) {
            _that.guideCheckboxAnswer.map(function (item, ind) {
              if (item == _that.guideList[i].answerFactors[j].factorId) {
                _that.guideCheckboxAnswer.splice(ind, 1);
              }
            });
          }
          guide.splice(i, 1);
          i--;
        }
      }
    },
    /***删除取消复选框的数据***/
    removeCheckbox: function (obj) {
      var _that = this;
      guideCheckboxAnswer = _that.guideCheckboxAnswer;
      for (var i = 0; i < _that.guideList.length; i++) {
        if (obj.factorId == _that.guideList[i].parentFactorId) {
          for (var j = 0; j < _that.guideList[i].answerFactors.length; j++) {
            _that.removeCheckbox(_that.guideList[i].answerFactors[j]);
            guideCheckboxAnswer.map(function (item, ind) {
              if (item == _that.guideList[i].answerFactors[j].factorId) {
                guideCheckboxAnswer.splice(ind, 1);
              }
            });
          }
        }
      }
    },
    //智能指引保存下一步
    saveGuideAndNext: function () {
      var _that = this;
      _that.factorIds = [];
      /***单选***/
      for (var i = 0; i < _that.guideList.length; i++) {
        if (_that.guideList[i].selectAnswerId != null &&
          _that.guideList[i].selectAnswerId != '') {
          _that.factorIds.push(_that.guideList[i].selectAnswerId);
        }
      }
      /***复选***/
      for (var i = 0; i < _that.guideCheckboxAnswer.length; i++) {
        _that.factorIds.push(_that.guideCheckboxAnswer[i]);
      }
      if (_that.factorIds.length == 0) {
        alertMsg('请选择智能指引');
        return;
      }

      request('', {
        url: ctx + '/rest/userCenter/apply/intelligent/theme/list/' + _that.factorIds,
        type: 'get',
        data: { factorIds: _that.factorIds }
      }, function (res) {
        if (res.success) {
          _that.$message({
            message: '保存成功',
            type: 'success'
          });
          if (res.content != null && res.content != "" &&
            res.content.themeId != null && res.content.themeId != "") {
            _that.themeActive = res.content.themeId;
            _that.itemTabSelect = 'tab_' + res.content.themeType;
            _that.declareStep = 3;
          } else {
            alertMsg('当前未定位到主题，可重新选择引导选项或跳过此步直接人工定位主题');
          }

        }
      });


    },

    // 保存保存领件人、项目信息 补全信息保存并下一步
    saveProOrSmsinfo: function () {
      var msg = '', perUnitMsg = '';
      var _that = this;
      var unitFlag = false; // 单位信息未完善
      var aeaUnitInfo = this.applyObjectInfo.aeaUnitInfo;
      var aeaLinkmanInfo = this.applyObjectInfo.aeaLinkmanInfo;
      var _linkmanTypes = [];
      if (_that.applyObjectInfo.aeaUnitInfo && _that.applyObjectInfo.aeaUnitInfo.linkmanTypes && _that.applyObjectInfo.aeaUnitInfo.linkmanTypes.length > 0) {
        _that.applyObjectInfo.aeaUnitInfo.linkmanTypes.map(function (itemType) {
          if (itemType.linkmanInfoId) {
            var typeObj = {
              linkmanType: itemType.linkmanType,
              linkmanInfoId: itemType.linkmanInfoId,
              linkmanName: itemType.linkmanName,
              unitInfoId: _that.unitInfoId,
              projInfoId: _that.projInfoId,
            }
            _linkmanTypes.push(typeObj);
          }

        })
      }
      if (aeaUnitInfo.applicant || aeaUnitInfo.unifiedSocialCreditCode || aeaUnitInfo.idrepresentative || aeaLinkmanInfo.linkmanName || aeaLinkmanInfo.linkmanMobilePhone || aeaLinkmanInfo.linkmanCertNo) {
        unitFlag = true;
      }
      if (this.applyObjectInfo.role == 0) {
        if (!this.userInfoId) {
          msg = '请前往企业中心完善申报主体申请人信息！';
        }
      } else {
        if (!this.unitInfoId || !unitFlag) {
          msg = '请前往企业中心完善申报主体企业信息！';
        }
      }
      if (this.isBlack == true) {
        msg = '申报主体已被纳入信用黑名单，无法申报项目！'
      }
      if (msg) {
        alertMsg('', msg, '关闭', 'error', true);
        return false;
      }
      // 判断单位必填是否已填
      var jiansheUnitFormEleLen = _that.jiansheFrom.length;
      var jiansheUnitFlag = true;
      for (var i = 0; i < jiansheUnitFormEleLen; i++) {
        var formRef = 'jianshe_' + i;
        var validFun;
        if ((typeof (_that.$refs[formRef].validate)) == 'function') {
          validFun = _that.$refs[formRef].validate
        } else {
          validFun = _that.$refs[formRef][0].validate
        }
        validFun(function (valid) {
          if (valid) {
            jiansheUnitFlag = true;
          } else {
            jiansheUnitFlag = false;
            perUnitMsg = "请完善申办主体建设单位信息"
            return false;
          }
        });
      }
      var projNameValue = this.$refs.projName.value.trim();
      var localCodeValue = this.$refs.localCode.value.trim();
      var regionalism = this.$refs.regionalism.value.trim();
      if (projNameValue == null || projNameValue == "") {
        return;
      }
      if (localCodeValue == null || localCodeValue == "") {
        return;
      }
      if (regionalism == null || regionalism == "") {
        return;
      }
      _that.getResultForm.id = _that.smsInfoId;
      _that.$refs['projInfoForm'].validate(function (valid1) {
        if (valid1) {
          if (jiansheUnitFlag) {
            _that.$refs['resultForm'].validate(function (valid) {
              if (valid) {
                _that.loading = true;
                _that.projInfoDetail.projectAddress = _that.projInfoDetail.projectAddress.join(',');
                var params = $.extend(_that.getResultForm, _that.projInfoDetail);
                Vue.set(params, 'linkmanTypeVos', _linkmanTypes)
                request('', {
                  url: ctx + 'rest/apply/common/completioninfo/saveOrUpdate',
                  type: 'post',
                  ContentType: 'application/json',
                  data: JSON.stringify(params)
                }, function (data) {
                  if (data.success) {
                    _that.projInfoDetail.projectAddress = _that.projInfoDetail.projectAddress.split(',');
                    _that.loading = false;
                    _that.smsInfoId = data.content.smsId;
                    _that.regionalism = data.content.regionalism;//更新区划ID
                    if (_that.themeId == '') {
                      _that.themeId = _that.projInfoDetail.themeId;
                    }
                    _that.getThemeList();
                    _that.unitProjIds = data.content.unitProjIds;
                    if (_that.intelligenceAllow || _that.themeId != '') {
                      _that.declareStep = 3;
                    } else {
                      _that.declareStep = 2;
                    }
                    _that.$message({
                      message: '保存成功',
                      type: 'success'
                    });
                  } else {
                    _that.loading = false;
                    _that.$message({
                      message: data.message ? data.message : '保存失败',
                      type: 'error'
                    });
                  }
                }, function (msg) {
                  _that.loading = false;
                  alertMsg('', '保存失败', '关闭', 'error', true);
                });
              } else {
                _that.$message({
                  message: '请完善领件人信息',
                  type: 'error'
                });
              }
            });
          } else {
            _that.$message({
              message: '请完善申办主体单位信息',
              type: 'error'
            });
          }

        } else {
          _that.$message({
            message: '请完善项目/工程信息',
            type: 'error'
          });
        }
      })

    },
    // 获取主题列表
    getThemeList: function () {
      var _that = this;
      _that.loading = true;
      request('', {
        url: ctx + 'rest/main/theme/list',
        type: 'get',
      }, function (data) {
        if (data.success) {
          _that.loading = false;
          _that.themeTypeList = data.content;
          if (_that.themeType) {
            _that.itemTabSelect = 'tab_' + _that.themeType;
            if (_that.themeId) {
              _that.themeActive = _that.themeId;
              _that.themeIdFlag = true;
              var copyThemeTypeList = JSON.parse(JSON.stringify(_that.themeTypeList));
              copyThemeTypeList = copyThemeTypeList.filter(function (item, index) {
                return item.themeTypeCode === _that.themeType;
              })
              copyThemeTypeList[0].themeList = copyThemeTypeList[0].themeList.filter(function (item, index) {
                return item.themeId === _that.themeId;
              })
              _that.themeName = copyThemeTypeList[0].themeList[0].themeName;
            }
          } else {
            _that.themeIdFlag = false;
            _that.itemTabSelect = 'tab_' + _that.themeTypeList[0].themeTypeCode;
            _that.chooseTheme(_that.themeTypeList[0].themeList[0]);
          }
        } else {
          _that.loading = false;
          _that.$message({
            message: data.message ? data.message : '获取主题列表失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.loading = false;
        alertMsg('', '获取主题列表失败', '关闭', 'error', true);
      });
    },
    // 选择主题展示十条超过隐藏
    themeListShowHide: function (item) {
      console.log(item);
      if (this.showThemeLen == 10) {
        this.showThemeLenText = false;
        this.showThemeLen = item.themeList.length;
      } else {
        this.showThemeLenText = true;
        this.showThemeLen = 10;
      }
    },
    // 选择并保存主题
    chooseTheme: function (data) {
      var _that = this;
      //if(_that.themeIdFlag){
      // alertMsg('', '主题已锁定，不可更改', '关闭', 'error', true);
      //   return false;
      // }else {
      _that.themeActive = data.themeId;
      _that.themeId = data.themeId;
      _that.themeName = data.themeName;
      // }
    },
    // 选择主题保存并下一步根据主题获取阶段
    saveThemeAndNext: function () {
      var _that = this;
      var node1StageList = [], node2StageList = [], node3StageList = [];
      _that.loading = true;
      _that.statusLineList = [];
      _that.selCoreItemsKey = [];
      var _unitInfoId = '';
      if (_that.showObjectRole && (_that.applyObjectInfo.role == '2')) {
        console.log(_that.applyObjectInfo.aeaUnitInfo.unitInfoId);
        _unitInfoId = _that.applyObjectInfo.aeaUnitInfo.unitInfoId
      }
      request('', {
        url: ctx + 'rest/main/stage/list/' + _that.themeId,
        type: 'get',
        data: { themeId: _that.themeId, projInfoId: _that.projInfoId, unitInfoId: _unitInfoId }
      }, function (data) {
        if (data.success) {
          _that.loading = false;
          data.content.map(function (item) {
            if (item.isNode == 1) {
              node1StageList.push(item);
            } else if (item.isNode == 2) {
              node2StageList.push(item);
            } else {
              node3StageList.push(item);
            }
          });
          if (node1StageList.length > 0) {
            _that.statusLineList.push({
              title: '按阶段并联申报',
              statusList: node1StageList,
              name: 'mainLine',
              imgUrl: 'mall/images/userCenter/status_type_2.png',
            });
          }
          if (node2StageList.length > 0) {
            _that.statusLineList.push({
              title: '按预先服务协同',
              statusList: node2StageList,
              name: 'auxiliary',
              imgUrl: 'mall/images/userCenter/status_type_1.png',
            });
          }
          if (node3StageList.length > 0) {
            _that.statusLineList.push({
              title: '技术审查',
              statusList: node3StageList,
              name: 'technical',
              imgUrl: 'mall/images/userCenter/status_type_2.png',
            });
          }
          if (_that.statusLineList.length > 0) {
            _that.stageList = _that.statusLineList[0].statusList;
          }
          if (_that.statusLineList.length > 1) {
            _that.statusLineListAllow = false;
            _that.showStageType = true;
            _that.declareStep = 4;
          } else {
            _that.statusLineListAllow = true
            _that.declareStep = 5;
            _that.selStatus(_that.stageList[0], 0);
          }
          _that.selStatusLine(_that.statusLineList[0], 0)
          // _that.selStatus(_that.stageList[0],0,_that.stageList[0].stageId,_that.stageList[0].isSelItem); // 默认选择阶段1
        } else {
          _that.loading = false;
          _that.$message({
            message: data.message ? data.message : '保存主题失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.loading = false;
        alertMsg('', '保存主题失败', '关闭', 'error', true);
      });
    },
    // 主题下主线辅线切换事件
    selStatusLine: function (data, index) {
      var _that = this;
      this.statusLineListActive = index;
      this.stageList = data.statusList;
      _that.stageList.map(function (item) { // 图片切换为新的图片
        if (item.stageName.indexOf('工程') == 0) {
          item.bigImgPath = 'mall/images/userCenter/stage_2.png';
        } else if (item.stageName.indexOf('施工') == 0) {
          item.bigImgPath = 'mall/images/userCenter/stage_3.png';
        } else if (item.stageName.indexOf('竣工') == 0) {
          item.bigImgPath = 'mall/images/userCenter/stage_4.png';
        } else {
          item.bigImgPath = 'mall/images/userCenter/stage_1.png';
        }
      });
      // this.selStatus(data,0,data.statusList[0].stageId,data.statusList[0].isSelItem);
    },
    // 点击选择申报阶段
    selStatus: function (data, index) {
      if (!data) {
        alertMsg('', '该主题下无可申报事项！', '关闭', 'warning', true);
        return false;
      }
      this.isSelItem = data.isSelItem;
      this.statusActiveIndex = index;
      this.stageId = data.stageId;
      this.stageName = data.stageName;
      this.useOneForm = data.useOneForm;
      if (data && (data.handWay == 0)) {
        this.parallelItemsQuestionFlag = true;
      } else {
        this.parallelItemsQuestionFlag = false;
      }
      this.getStatusStateMats(this.stageId);  // 获取事项情形列表
      // this.getStageItems(stageId); // 获取事项列表
    },
    // 判断事项checkbox是否可勾选
    checkboxInit: function (row) {
      if (row.orgName === null || !row.preItemCheckPassed || row.notRegionData)
        // if (row.isDone=='HANDLING'||row.isDone=='FINISHED'||row.orgName===null)
        return 0;//不可勾选
      else
        return 1;//可勾选
    },
    // 根据阶段获取情形事项
    getStatusStateMats: function (stageId) {
      var _that = this;
      _that.loading = true;
      _that.selCoreItemsKey = [];
      var projectAddress = _that.projInfoDetail.projectAddress.join(',');
      // _that.parallItemExpandChange = [];
      var regionalism = _that.regionalism ? _that.regionalism : null;
      request('', {
        url: ctx + 'rest/userCenter/apply/itemAndState/list/' + stageId + '/' + _that.projInfoId + '/' + regionalism,
        type: 'get',
        timeout: 100000,
        data: {
          "stageId": stageId,
          "projectAddress": projectAddress
        }
      }, function (res) {
        if (res.success) {
          _that.loading = false;
          _that.stateList = res.content.stateList;
          _that.stateList = _that.sortByKey(_that.stateList, 'sortNo');
          _that.stateList.map(function (item, ind) { // 情形下插入对应的情形
            if (item.answerType != 's' && item.answerType != 'y') {
              Vue.set(item, 'selValue', []);
              item.selectAnswerId = item.selValue;
            }
          });
          // 当前情形附带的并联事项列表
          res.content.parallelItemList.map(function (item) {
            if (item.paraStateList && item.paraStateList.length > 0) {
              item.paraStateList.map(function (itemState) {
                if (itemState.answerType != 's' && itemState.answerType != 'y') {
                  Vue.set(itemState, 'selValue', []);
                  itemState.selectAnswerId = itemState.selValue;
                }
              });
            }
          });
          // 当前情形附带的并行事项列表
          res.content.coreItemList.map(function (item) {
            if (item.coreStateList && item.coreStateList.length > 0) {
              item.coreStateList.map(function (itemState) {
                if (itemState.answerType != 's' && itemState.answerType != 'y') {
                  Vue.set(itemState, 'selValue', []);
                  itemState.selectAnswerId = itemState.selValue;
                }
              });
            }
          });
          _that.parallelItems = res.content.parallelItemList;
          _that.coreItems = res.content.coreItemList;
          _that.coreItems.map(function (item) {
            if (item) {
              _that.setImplementItem(item);
            }
          });
          _that.parallelItems.map(function (item) {
            if (item) {
              _that.setImplementItem(item);
            }
          });
          _that.declareStep = 5;
          _that.$nextTick(function () {
            _that.toggleSelection(_that.parallelItems, 'parallelItemsTable');
          });
          _that.parallelItemsSelAll(_that.parallelItems, 'autoGetSel')
        } else {
          _that.$message({
            message: '获取阶段下情形事项失败',
            type: 'error'
          });
          _that.loading = false;
        }
      }, function (msg) {
        _that.$message({
          message: '获取阶段下情形事项失败',
          type: 'error'
        });
        _that.loading = false;
      });
    },
    // 根据情形获取情形事项
    getStatusStateBystatus: function (pData, data, index, checkFlag) {
      var _that = this;
      var questionStateId = pData.parStateId;
      var _stageId = _that.stageId;
      var _stateId = data.parStateId;
      var selStateIds = []; // 情形列表id集合
      var parentStateId = data.parentStateId;
      var _parentId = data.parStateId ? data.parStateId : 'ROOT';
      var _regionalism = _that.regionalism ? _that.regionalism : null;
      var _projectAddress = _that.projInfoDetail.projectAddress.join(',');
      if (checkFlag == false) {
        selStateIds = _that.getStatusListId();
        for (var i = 0; i < _that.stateList.length; i++) { // 清空情形下所对应情形
          var obj = _that.stateList[i];
          console.log(selStateIds, obj)
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
            // if(obj&&obj.stateSeq.indexOf(parentStateId)>-1&&(obj.parStateId!=parentStateId)){
            _that.stateList.splice(i, 1);
            i--
          }
        }
        // 清空情形下所对应并联事项
        for (var i = 0; i < _that.parallelItems.length; i++) {
          var obj = _that.parallelItems[i];
          if (obj && (selStateIds.indexOf(obj.parStateId) == -1)) {
            _that.parallelItems.splice(i, 1);
            i--
          }
        }
        // 清空情形下所对应并行事项
        for (var i = 0; i < _that.coreItems.length; i++) {
          var obj = _that.coreItems[i];
          if (obj && (selStateIds.indexOf(obj.parStateId) == -1)) {
            _that.coreItems.splice(i, 1);
            i--
          }
        }
        return false
      }
      request('', {
        url: ctx + 'rest/userCenter/apply/itemByState/list/' + _stateId + '/' + _stageId + '/' + _regionalism,
        type: 'get',
        data: {
          stateId: _stateId,
          stageId: _stageId,
          regionalism: _regionalism,
          projectAddress: _projectAddress
        }
      }, function (res) {
        if (res.success) {
          if (checkFlag == true) {
            res.content.stateList.map(function (item, ind) { // 情形下插入对应的情形
              if (item.answerType != 's' && item.answerType != 'y') {
                Vue.set(item, 'selValue', []);
                item.selectAnswerId = item.selValue;
              }
              _that.stateList.splice((index + 1 + ind), 0, item);
            });
            _that.stateList = _that.sortByKey(_that.stateList, 'sortNo');
            // 当前情形附带的并联事项列表
            res.content.itemList.map(function (item, index) {
              if (item._parStateId == 'undefined' || item._parStateId == undefined) {
                Vue.set(item, '_parStateId', [parentStateId]);
              } else {
                if (item._parStateId.indexOf(parentStateId) < 0) {
                  item._parStateId.push(parentStateId);
                }
              }
              if (item.paraStateList && item.paraStateList.length > 0) {
                item.paraStateList.map(function (itemState) {
                  if (itemState.answerType != 's' && itemState.answerType != 'y') {
                    Vue.set(itemState, 'selValue', []);
                    itemState.selectAnswerId = itemState.selValue;
                  }
                });
              }
            });
            // 当前情形附带的并行事项列表
            res.content.coreItemList.map(function (item, index) {
              if (item._parStateId == 'undefined' || item._parStateId == undefined) {
                Vue.set(item, '_parStateId', [parentStateId]);
              } else {
                if (item._parStateId.indexOf(parentStateId) < 0) {
                  item._parStateId.push(parentStateId);
                }
              }
              if (item.coreStateList && item.coreStateList.length > 0) {
                item.coreStateList.map(function (itemState) {
                  if (itemState.answerType != 's' && itemState.answerType != 'y') {
                    Vue.set(itemState, 'selValue', []);
                    itemState.selectAnswerId = itemState.selValue;
                  }
                });
              }
            });
          } else {
            var stateListLen = _that.stateList.length;
            selStateIds = _that.getStatusListId();
            for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
              var obj = _that.stateList[i];
              console.log(selStateIds)

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
            // 情形下插入对应的情形
            if (res.content.stateList.length > 0) {
              res.content.stateList.map(function (item, ind) {
                if (item.answerType != 's' && item.answerType != 'y') {
                  Vue.set(item, 'selValue', []);
                  item.selectAnswerId = item.selValue;
                }
                _that.stateList.splice((index + 1 + ind), 0, item);
              });
              _that.stateList = _that.sortByKey(_that.stateList, 'sortNo');
            }


            selStateIds = _that.getStatusListId();
            // 当前情形附带的并行事项列表
            for (var i = 0; i < _that.coreItems.length; i++) { // 清空情形下所对应事项
              var obj = _that.coreItems[i];
              if (obj._parStateId == 'undefined' || obj._parStateId == undefined) {
                Vue.set(obj, '_parStateId', []);
              } else {
                if (obj && (obj._parStateId.length == 1)) {
                  var str = obj._parStateId.join('.')
                  if (selStateIds.indexOf(str) == -1) {
                    _that.coreItems.splice(i, 1);
                    i--
                  }
                } else {
                  var index = obj._parStateId.indexOf(_parentId);
                  if (index > -1) {
                    obj._parStateId.splice(index, 1);
                  }
                }
              }
            };
            if (res.content.coreItemList.length > 0) {
              res.content.coreItemList.map(function (item, index) {
                if (item._parStateId == 'undefined' || item._parStateId == undefined) {
                  Vue.set(item, '_parStateId', [_parentId]);
                } else {
                  if (item._parStateId.indexOf(_parentId) < 0) {
                    item._parStateId.push(_parentId);
                  }
                }
                if (item.coreStateList && item.coreStateList.length > 0) {
                  item.coreStateList.map(function (itemState) {
                    if (itemState.answerType != 's' && itemState.answerType != 'y') {
                      Vue.set(itemState, 'selValue', []);
                      itemState.selectAnswerId = itemState.selValue;
                    }
                  });
                }
                _that.setImplementItem(item);
              });
            }
            for (var i = 0; i < _that.parallelItems.length; i++) { // 清空情形下所对应并联事项
              var obj = _that.parallelItems[i];
              if (obj._parStateId == 'undefined' || obj._parStateId == undefined) {
                Vue.set(obj, '_parStateId', []);
              } else {
                if (obj && (obj._parStateId.length == 1)) {
                  var str = obj._parStateId.join('.')
                  if (selStateIds.indexOf(str) == -1) {
                    _that.parallelItems.splice(i, 1);
                    i--
                  }
                } else {
                  var index = obj._parStateId.indexOf(_parentId);
                  if (index > -1) {
                    obj._parStateId.splice(index, 1);
                  }
                }
              }
            }
          }
          // 当前情形附带的并联事项列表
          if (res.content.itemList.length > 0) {
            res.content.itemList.map(function (item, index) {
              if (item._parStateId == 'undefined' || item._parStateId == undefined) {
                Vue.set(item, '_parStateId', [_parentId]);
              } else {
                if (item._parStateId.indexOf(_parentId) < 0) {
                  item._parStateId.push(_parentId);
                }
              }
              if (item.paraStateList && item.paraStateList.length > 0) {
                item.paraStateList.map(function (itemState) {
                  if (itemState.answerType != 's' && itemState.answerType != 'y') {
                    Vue.set(itemState, 'selValue', []);
                    itemState.selectAnswerId = itemState.selValue;
                  }
                });
              }
              _that.parallelItemsSelItem(_that.parallelItems, item, 'autoGetSel');
              _that.setImplementItem(item);
            });
          }
          // 当前情形附带的并联事项列表
          if (res.content.coreItemList.length > 0) {
            res.content.coreItemList.map(function (item, index) {
              if (item._parStateId == 'undefined' || item._parStateId == undefined) {
                Vue.set(item, '_parStateId', [_parentId]);
              } else {
                if (item._parStateId.indexOf(_parentId) < 0) {
                  item._parStateId.push(_parentId);
                }
              }
              if (item.coreStateList && item.coreStateList.length > 0) {
                item.coreStateList.map(function (itemState) {
                  if (itemState.answerType != 's' && itemState.answerType != 'y') {
                    Vue.set(itemState, 'selValue', []);
                    itemState.selectAnswerId = itemState.selValue;
                  }
                });
              }
              _that.setImplementItem(item);
            });
          }
          _that.coreItems = _that.unique(_that.coreItems.concat(res.content.coreItemList), 'stage');
          _that.parallelItems = _that.unique(_that.parallelItems.concat(res.content.itemList), 'stage');
          _that.$nextTick(function () {
            _that.toggleSelection(_that.parallelItems, 'parallelItemsTable');
          });
          _that.parallelItemsSelAll(_that.parallelItems, 'autoGetSel')
        } else {
          _that.$message({
            message: '获取阶段下情形失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: '获取阶段下情形失败',
          type: 'error'
        });
      });
    },
    sortByKey: function (array, key) {  //(数组、排序的列)
      return array.sort(function (a, b) {
        var x = a[key];
        var y = b[key];
        return ((x < y) ? -1 : ((x > y) ? 1 : 0));
      });
    },
    // 根据情形获取子情形
    getCoreStateBystatus: function (cStateList, pData, answerData, index, checkFlag) {
      var parentStateId = answerData.parentStateId;
      var questionStateId = pData.itemStateId;
      var _that = this;
      var _itemVerId = answerData.itemVerId;
      var _parentId = answerData.itemStateId ? answerData.itemStateId : 'ROOT';
      var selStateIds = [];
      if (checkFlag == false) {
        var stateListLen = cStateList.length;
        selStateIds = _that.getCoreItemsStatusListId(cStateList);
        for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
          var obj = cStateList[i];
          console.log(selStateIds)
          if (obj && (obj.parentStateId == _parentId) || (obj && obj.parentStateId != null && (selStateIds.indexOf(obj.parentStateId) == -1))) {
            cStateList.splice(i, 1);
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
            i--
          }
        }
        return false
      }
      request('', {
        url: ctx + 'rest/apply/common/itemState/findByParentItemStateId/' + _parentId + '/' + _itemVerId,
        data: { itemStateId: _parentId },
        type: 'get'
      }, function (data) {
        if (data.success) {
          if (checkFlag == true) {
            data.content.map(function (item, ind) { // 情形下插入对应的情形
              if (item.answerType != 's' && item.answerType != 'y') {
                Vue.set(item, 'selValue', []);
                item.selectAnswerId = item.selValue;
              }
              cStateList.splice((index + 1 + ind), 0, item);
            });
          } else {
            var stateListLen = cStateList.length;
            selStateIds = _that.getCoreItemsStatusListId(cStateList);
            for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
              var obj = cStateList[i];
              console.log(selStateIds)
              // if(obj&&obj.stateSeq.indexOf(parentStateId)>-1&&(obj.itemStateId!=parentStateId)){
              if (obj && (obj.parentQuestionStateId == questionStateId) || (obj && obj.parentStateId != null && (selStateIds.indexOf(obj.parentStateId) == -1))) {
                cStateList.splice(i, 1);
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
                i--
              }
            }
            data.content.map(function (item, ind) { // 情形下插入对应的情形
              if (item.answerType != 's' && item.answerType != 'y') {
                Vue.set(item, 'selValue', []);
                item.selectAnswerId = item.selValue;
              }
              cStateList.splice((index + 1 + ind), 0, item);
            });
          }
        } else {
          _that.$message({
            message: '获取情形失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: '获取情形失败',
          type: 'error'
        });
      });
    },
    // 获取单事项情形列表id
    getCoreItemsStatusListId: function (cStateList) {
      var stateListLen = cStateList.length;
      var selStateIds = [];
      for (var i = 0; i < stateListLen; i++) {  // 已选并联情形id集合
        if (cStateList[i].selectAnswerId !== undefined || cStateList[i].selectAnswerId !== null) {
          // selStateIds=[];
          // return true;
          if (typeof (cStateList[i].selectAnswerId) == 'object' && cStateList[i].selectAnswerId.length > 0) {
            selStateIds = selStateIds.concat(cStateList[i].selectAnswerId);
          } else if (cStateList[i].selectAnswerId !== '') {
            selStateIds.push(cStateList[i].selectAnswerId);
          }
        }
      }
      selStateIds = selStateIds.filter(function (item, index) {
        //当前元素，在原始数组中的第一个索引==当前索引值，否则返回当前元素
        return selStateIds.indexOf(item, 0) === index
      })
      return selStateIds;
    },
    // 单项事项列表收取展开事项
    itemExpandChange: function (row, expandedRows) {
      var _that = this;
      _that.coreItemsSelItem(expandedRows, row);
      expandedRows.map(function (item) {
        if (item.itemVerId == row.itemVerId) {
          _that.$refs.coreItemsTable.toggleRowSelection(row, true);
        }
      })
    },
    // 并联事项列表收取展开事项
    parallItemExpandChange: function (row, expandedRows) {
      this.parallelItemsSelItem(expandedRows, row, 'autoGetSel');
      expandedRows.map(function (item) {
        if (item.itemVerId == row.itemVerId) {
          this.$refs.parallelItemsTable.toggleRowSelection(row);
        }
      })
    },
    // 获取已选中情形列表id
    getStatusListId: function () {
      var _that = this;
      var stateSel = _that.stateList;
      var stateListLen = stateSel.length;
      var selStateIds = [];
      for (var i = 0; i < stateListLen; i++) {  // 已选并联情形id集合
        if (stateSel[i].selectAnswerId !== undefined || stateSel[i].selectAnswerId !== null) {
          if (typeof (stateSel[i].selectAnswerId) == 'object' && stateSel[i].selectAnswerId.length > 0) {
            selStateIds = selStateIds.concat(stateSel[i].selectAnswerId);
          } else if (stateSel[i].selectAnswerId !== '') {
            selStateIds.push(stateSel[i].selectAnswerId);
          }
        }
      }
      selStateIds = _that.distinct(selStateIds, [])
      return selStateIds;
    },
    // 并联事项默认全选
    toggleSelection: function (rows, tableRef) {
      var _that = this;
      if (rows) {
        rows.forEach(function (row) {
          if (row.orgName !== null && row.preItemCheckPassed && !row.notRegionData) {
            // if(row.isDone!='HANDLING'&&row.isDone!='FINISHED'&&row.orgName!==null){
            _that.$refs[tableRef].toggleRowSelection(row, true);
          }
        });
      } else {
        _that.$refs[tableRef].clearSelection();
      }
    },
    // 单项事项单选事件
    coreItemsSelItem: function (selArr, row) {
      var _that = this;
      if (selArr.length == 0) {
        _that.selCoreItemsKey = [];
      }
      request('', { // 判断并行实施事项是否可选
        url: ctx + 'rest/apply/common/item/frontItemIsDone/' + row.itemVerId + '/' + _that.projInfoId,
        type: 'POST',
        data: {
          projInfoId: _that.projInfoId,
          itemVerId: row.itemVerId
        },
      }, function (result) {
        if (result.content && result.content.length > 0) {
          row.preItemCheckPassed = false;
          _that.checkboxInit(row);
        } else {
          row.preItemCheckPassed = true;
        }
        if (row.preItemCheckPassed == false) {
          _that.$message({
            message: '该事项有前置事项未办！',
            type: 'error'
          });
        }
        selArr.map(function (item, index) {
          if (item.preItemCheckPassed == false) {
            selArr.splice(index, 1);
          } else {
            if (item.itemVerId == row.itemVerId && row.preItemCheckPassed == true) {
              _that.selCoreItemsKey.push(row.itemId);
            } else {
              for (var i = 0; i < _that.selCoreItemsKey.length; i++) {
                if (_that.selCoreItemsKey[i] == row.itemId) {
                  _that.selCoreItemsKey.splice(i, 1);
                  return true;
                }
              }
            }
          }
        });
        _that.selCoreItemsKey = _that.distinct(_that.selCoreItemsKey, [])
      }, function (msg) { })
    },
    // 单项事项全选事件
    coreItemsSelAll: function (selArr) {
      var _that = this;
      var flag = true;
      if (selArr.length == 0) {
        flag = false;
        _that.selCoreItemsKey = [];
        return false;
      } else {
        selArr.map(function (item) {
          if (item.orgName === null || !item.preItemCheckPassed || item.notRegionData) {
            // if(item.isDone=='HANDLING'||item.isDone=='FINISHED'||item.orgName===null){
            flag = false;
          }
          if (flag) {
            _that.selCoreItemsKey.push(item.itemId);
          }
        });
        _that.selCoreItemsKey = _that.distinct(_that.selCoreItemsKey, [])
      }
    },
    // 并联事项单选事件
    parallelItemsSelItem: function (selArr, row, selflag) {
      var _that = this;
      if (selArr.length == 0) {
        _that.selParallelItemsKey = [];
        if (selflag != 'autoGetSel') {
          alertMsg('', '您已取消所有并联事项！', '关闭', 'warning', true);
        }
      }
      selArr.map(function (item) {
        if (item.itemVerId == row.itemVerId) {
          _that.selParallelItemsKey.push(row.itemId);
        } else {
          for (var i = 0; i < _that.selParallelItemsKey.length; i++) {
            if (_that.selParallelItemsKey[i] == row.itemId) {
              _that.selParallelItemsKey.splice(i, 1);
              return true;
            }
          }
        }
      });
      _that.selParallelItemsKey = _that.distinct(_that.selParallelItemsKey, [])
    },
    // 并联事项全选事件
    parallelItemsSelAll: function (selArr, selflag) { // selflag 调用方式 autoGetSel手动触发
      var _that = this;
      var flag = true;
      if (selArr.length == 0) {
        flag = false;
        _that.selParallelItemsKey = [];
        if (selflag != 'autoGetSel') {
          alertMsg('', '您已取消所有并联事项！', '关闭', 'warning', true);
        }
      } else {
        selArr.map(function (row) {
          if (row.orgName !== null && row.preItemCheckPassed && !row.notRegionData) {
            // if(row.isDone!='HANDLING'&&row.isDone!='FINISHED'&&row.orgName!==null){
            flag = true;
          } else {
            flag = false;
          }
          if (flag) {
            _that.selParallelItemsKey.push(row.itemId);
          }
        });
        _that.selParallelItemsKey = _that.distinct(_that.selParallelItemsKey, [])
      }
    },
    // 数组简单去重
    distinct: function (a, b) {
      var arr = a.concat(b);
      return arr.filter(function (item, index) {
        return arr.indexOf(item) === index
      })
    },
    // 上一页
    prevPage: function (page) {
      if (page == 4 && this.statusLineList.length < 2) {
        this.declareStep = 3;
      } else if (page == 6 && this.useOneForm != 1) {
        this.declareStep = 5;
      } else if (page == 2 && this.intelligenceAllow && this.themeId != '') {
        this.declareStep = 1;
      } else {
        this.declareStep = page;
      }
    },
    // 保存并下一步  获取一张表单列表
    saveAndGetOneForm: function () {
      var _that = this;
      var _itemStateIds = [], _stageStateIds = [];
      var selItemVer = []; // 所有选择的并联审批事项
      var selCoreItems = []; // 所有选择的并行审批事项
      var stateSel = _that.stateList;
      var stateSelLen = stateSel.length;
      _that.itemVerIds = [];
      _that.propulsionItemVerIds = [];
      _that.coreParentItemVerIds = [];
      _that.paraParentllelItemVerIds = [];
      _that.propulsionBranchOrgMap = [];
      _that.branchOrgMap = [];
      if (_that.coreItems.length > 0) {
        selCoreItems = _that.$refs.coreItemsTable.selection; // 所有选择的并行审批事项
      }
      if (_that.parallelItems.length > 0) {
        selItemVer = _that.$refs.parallelItemsTable.selection; // 所有选择的并行审批事项
      }

      // 并联itemVerids集合
      selItemVer.map(function (item) {
        _that.itemVerIds.push(item.itemVerId);
        if (item.baseItemVerId) {
          _that.paraParentllelItemVerIds.push(item.baseItemVerId);
        }
        _that.branchOrgMap.push({
          itemVerId: item.itemVerId,
          branchOrg: item.orgId,
        });
      });
      if (_that.branchOrgMap.length > 0) {
        _that.branchOrgMap = JSON.stringify(_that.branchOrgMap)
      } else {
        _that.branchOrgMap = ''
      }
      // 并行itemVerids集合
      selCoreItems.map(function (item) {
        _that.propulsionItemVerIds.push(item.itemVerId);
        if (item.baseItemVerId) {
          _that.coreParentItemVerIds.push(item.baseItemVerId);
        }
        _that.propulsionBranchOrgMap.push({
          itemVerId: item.itemVerId,
          branchOrg: item.orgId,
        });
      });
      if (_that.propulsionBranchOrgMap.length > 0) {
        _that.propulsionBranchOrgMap = JSON.stringify(_that.propulsionBranchOrgMap)
      } else {
        _that.propulsionBranchOrgMap = ''
      }
      _that.stateIds = [];
      for (var i = 0; i < stateSelLen; i++) {  // 并联情形id集合
        if (stateSel[i].mustAnswer == 1 && (stateSel[i].selectAnswerId == null || stateSel[i].selectAnswerId == '')) {
          _that.stateIds = [];
          if (selItemVer.length > 0) {
            alertMsg('', '请选择情形', '关闭', 'error', true);
            return true;
          }
        }
        if (stateSel[i].selectAnswerId !== null || stateSel[i].selectAnswerId !== '') {

          if (typeof stateSel[i].selectAnswerId == 'object') {
            _that.stateIds = _that.stateIds.concat(stateSel[i].selectAnswerId);
            _stageStateIds = _stageStateIds.concat(stateSel[i].selectAnswerId);
          } else {
            _that.stateIds.push(stateSel[i].selectAnswerId);
            _stageStateIds.push(stateSel[i].selectAnswerId);
          }
        }
      }
      var coreStateSel = [];
      _that.propulsionItemStateIds = [];
      for (var j = 0; j < selCoreItems.length; j++) {
        coreStateSel = selCoreItems[j].coreStateList;
        var coreStateIds = []; // 并行已选情形id集合
        if (coreStateSel) {
          for (var i = 0; i < coreStateSel.length; i++) {  // 并联情形id集合
            if (coreStateSel[i].selectAnswerId == null || coreStateSel[i].selectAnswerId == '') {
              if (coreStateSel[i].mustAnswer == 1) {
                _that.propulsionItemStateIds = [];
                alertMsg('', '请选择并行事项情形', '关闭', 'error', true);
                return true;
              }
            } else {
              if (typeof (coreStateSel[i].selectAnswerId) == 'object') {
                coreStateIds.concat(coreStateSel[i].selectAnswerId);
                _itemStateIds.concat(coreStateSel[i].selectAnswerId);
              } else {
                coreStateIds.push(coreStateSel[i].selectAnswerId);
                _itemStateIds.push(coreStateSel[i].selectAnswerId);
              }
            }
          }
          _that.propulsionItemStateIds.push({
            "itemVerId": selCoreItems[j].itemVerId,
            "stateIds": coreStateIds
          });
        }
      }
      if (_that.parallelItemsQuestionFlag == true) {
        var parallelStateSel = [];
        _that.parallelItemStateIds = [];
        for (var ind = 0; ind < selItemVer.length; ind++) {
          parallelStateSel = selItemVer[ind].paraStateList;
          var parallelStateIds = []; // 并联已选情形id集合
          if (parallelStateSel) {
            for (var ind1 = 0; ind1 < parallelStateSel.length; ind1++) {  // 并联情形id集合
              if (parallelStateSel[ind1].selectAnswerId == null || parallelStateSel[ind1].selectAnswerId == '') {
                if (parallelStateSel[ind1].mustAnswer == 1) {
                  _that.parallelItemStateIds = [];
                  alertMsg('', '请选择并联事项情形', '关闭', 'error', true);
                  return true;
                }
              } else {
                if (typeof (parallelStateSel[ind1].selectAnswerId) == 'object') {
                  _itemStateIds.concat(parallelStateSel[ind1].selectAnswerId);
                  parallelStateIds.concat(parallelStateSel[ind1].selectAnswerId);
                } else {
                  _itemStateIds.push(parallelStateSel[ind1].selectAnswerId);
                  parallelStateIds.push(parallelStateSel[ind1].selectAnswerId);
                }
              }
            }
            _that.parallelItemStateIds.push({
              "itemVerId": selItemVer[ind].itemVerId,
              "stateIds": parallelStateIds
            });
          }
        }
      } else {
        _that.parallelItemStateIds = [];
      }
      _that._itemStateIds = _itemStateIds;
      _that._stageStateIds = _stageStateIds;
      if (_that.useOneForm == 1) {
        if (selItemVer.length > 0) { // 已选择并联事项
          _that.getParallelApplyinstId();// 实例化并联申报  获取一张表单
        } else {
          _that.declareStep = 7;
          _that.saveAndGetMats();
        }

        // this.getOneFormList(fix_stageid);
      } else {
        _that.oneFormDataAllow = true;
        _that.declareStep = 7;
        _that.saveAndGetMats();
        // this.getOneFormList(fix_stageid);
      }
    },
    // 保存并根据阶段ID、情形ID集合、事项版本ID集合获取材料一单清列表数据
    saveAndGetMats: function () {
      var _that = this;
      var parmas = {
        stageId: _that.stageId,
        coreItemVerIds: _that.propulsionItemVerIds,
        parallelItemVerIds: _that.itemVerIds,
        itemStateIds: _that._itemStateIds,
        stageStateIds: _that._stageStateIds,
        coreParentItemVerIds: _that.coreParentItemVerIds,
        paraParentllelItemVerIds: _that.paraParentllelItemVerIds
      };
      _that.loading = true;
      request('', {
        url: ctx + 'rest/userCenter/apply/mat/list',
        type: 'post',
        ContentType: 'application/json',
        data: JSON.stringify(parmas)
      }, function (result) {
        _that.declareStep = 7;
        _that.showFileListKey = [];
        result.content.map(function (item) {
          if (typeof item.childs == 'undefined' || item.childs == undefined) {
            Vue.set(item, 'childs', []);
          }
          if(typeof item.certChild=='undefined'||item.certChild==undefined){
            Vue.set(item,'certChild',[]);
          }
        });
        _that.allMatsTableData = result.content;
        _that.showMatsTableData = result.content.slice(0, 10);
        _that.loading = false;
      }, function (msg) {
        _that.loading = false;
      })
    },
    // 文件上传 change事件
    fileChange: function (file, fileList) {
      var _that = this;

      if (!_that.isSpecifiedFileType(file.name)) {
        _that.$message({
          message: '不允许上传的文件类型',
          type: 'error'
        });
        return false;
      }

      _that.AllFileListId = [];
      var newFileList = [];
      if (fileList.length > 0) {
        fileList.map(function (item) {
          if (_that.isSpecifiedFileType(item.name)) {
            newFileList.push(item);
          };
        });

        this.AllFileList = this.unique(newFileList, 'file');
        this.AllFileList.map(function (item) {
          _that.AllFileListId.push(item.name);
        });
        _that.checkedSelFlie = _that.AllFileListId;
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
    // 一键分拣全选
    handleCheckAllChange: function (val) {
      this.checkedSelFlie = val ? this.AllFileListId : [];
    },
    // 一键分拣勾选文件
    handleCheckedCitiesChange: function (value) {
      var checkedCount = value.length;
      this.checkAll = checkedCount === this.AllFileListId.length;
      this.checkedSelFlie = value;
    },
    // 获取材料id 材料实例id
    getMatIdAndMatinstId: function () {
      var _that = this;
      _that.matIds = [];
      _that.matinstIds = [];
      _that.attIsRequireFlag = true;
      _that.allMatsTableData.map(function (item) {
        if (item.childs == 'undefined' || item.childs == undefined) {
          Vue.set(item, 'childs', []);
        }
        if(item.certChild=='undefined'||item.certChild==undefined){
          Vue.set(item,'certChild',[]);
        }
        if (item.matinstId == 'undefined' || item.matinstId == undefined) {
          Vue.set(item, 'matinstId', '');
        }
        if (item.zcqy == 0 && item.attIsRequire == 1) {
          console.log(item.matinstId);
          if (!item.matinstId || item.childs.length == 0) {
            _that.attIsRequireFlag = false;
          }
        }
        _that.matIds.push(item.matId);
        if (typeof item.matinstId !== "undefined" && item.matinstId != '') {
          if(_that.matinstIds.indexOf(item.matinstId)<0){
            _that.matinstIds.push(item.matinstId)
          }
        }
        if(item.certMatinstIds&&item.certMatinstIds.length>0){
          _that.matinstIds = _that.matinstIds.concat(item.certMatinstIds)
        }
      });
    },
    // 文件上传 submit事件
    submitUpload: function () {
      this.fileData = new FormData();
      this.$refs.uploadFile.submit();
      var _that = this;
      this.getMatIdAndMatinstId();
      this.fileData.append("projInfoId", _that.projInfoId);
      this.fileData.append("matIds", _that.matIds);
      this.fileData.append("matinstIds", _that.matinstIds);
      this.fileData.append("unitInfoId", _that.unitInfoId);
      this.fileData.append("userInfoId", _that.userInfoId);
      this.uploadLogo = "1";
      if (_that.checkedSelFlie.length == 0) {
        alertMsg('', '请选择需要自动分拣的文件', '关闭', 'warning', true);
        return false;
      } else {
        _that.loadingFile = true;
        axios.post(ctx + 'rest/apply/common/att/upload/auto', _that.fileData).then(function (res) {
          _that.checkedSelFlie = [];
          _that.AllFileList = [];
          _that.loadingFile = false;
          _that.$refs.uploadFile.clearFiles();
          _that.checkAll = true;
          var matinstIdsObj = [];
          res.data.content.map(function (item) {
            _that.showFileListKey.push(item.matId);
            _that.allMatsTableData.map(function (matItem) {
              if (item.matId == matItem.matId) {
                matItem.matinstId = item.matinstId;
                matItem.childs = item.fileList;
              }
            });
          });
        }).catch(function (error) {
          if (error.response) {
            // content.onError('配时文件上传失败(' + error.response.status + ')，' + error.response.data);
          } else if (error.request) {
            // content.onError('配时文件上传失败，服务器端无响应');
            alertMsg('', '文件上传失败', '关闭', 'error', true);
          } else {
            // content.onError('配时文件上传失败，请求封装失败');
          }
          _that.loadingFile = false;
        });
      }

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
      for (item in result) {
        finalResult.push(result[item]);
      }
      return finalResult.reverse();
    },
    // 是否展示更多材料
    toggleShowMatsMore: function () {
      this.isShowAll = !this.isShowAll;
      if (this.isShowAll === true) {
        this.showMatsTableData = this.allMatsTableData
      } else {
        this.showMatsTableData = this.allMatsTableData.slice(0, 10);
      }
    },
    // 删除文件 flag ==dir 我的空间选中材料
    delSelectFileCom: function (rowData, fileData, index, flag) {
      var _that = this;
      var detailIds = flag == 'dir' ? fileData.detailId : fileData.fileId;
      request('', {
        url: ctx + 'rest/file/delelteAttachment',
        type: 'get',
        data: { matinstId: rowData.matinstId, detailIds: detailIds }
      }, function (res) {
        if (res.success) {
          _that.getFileListWin(res.content, rowData);
          // rowData.childs.splice(index,1);
          var succMsg = index ? '删除成功' : '移除成功'
          if (_that.myMatsList.length > 0) {
            _that.myMatsList.map(function (item) {
              if (item.fileId == detailIds) {
                item.checked = false;
              }
            });
          }
          _that.$message({
            message: succMsg,
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
    // // 预览文件
    // filePreview: function(detailId){
    //   var _that = this;
    //   var flashAttributes = '';
    //   window.open(ctx + 'rest/file/att/preview/'+detailId);
    // },
    // 获取文件后缀
    getFileType: function (fileName) {
      var index1 = fileName.lastIndexOf(".");
      var index2 = fileName.length;
      var suffix = fileName.substring(index1 + 1, index2).toLowerCase();//后缀名
      return suffix;
    },
    // 预览电子件
    filePreview: function (data, flag) { // flag==pdf 查看施工图
      var detailId = data.fileId;
      var _that = this;
      var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
      var fileName = data.fileName;
      var fileType = this.getFileType(fileName);
      var flashAttributes = '';
      _that.filePreviewCount++
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
            tempwindow.location = ctx + 'rest/file/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
          }, 1000)
        }
      }
    },
    // 下载电子件
    downloadFile: function (detailIds) {
      window.open(ctx + 'rest/file/applydetail/mat/download/' + detailIds)
    },
    debounceHandler: function (file) {
      var _that = this;
      _that.loadingFileWin = true;

      if (!_that.isSpecifiedFileType(file.file.name)) {
        _that.$message({
          message: '不允许上传的文件类型',
          type: 'error'
        });
        _that.loadingFileWin = false;

        return false;
      }

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
    // 上传电子件
    uploadFileCom: function (file) {
      var _that = this;
      var isAllowFile;
      var rowData = _that.selMatRowData;
      this.fileWinData = new FormData();
      file.forEach(function (u) {
        // if (!_that.isAllowFile(u.file.name)){
        //   _that.$message({
        //     message: '不允许上传的文件类型',
        //     type: 'error'
        //   });
        //   isAllowFile = false;
        // }
        Vue.set(u.file, 'fileName', u.file.name);
        _that.fileWinData.append('file', u.file);

      })
      //if (!isAllowFile){ return ;}
      // Vue.set(file.file,'fileName',file.file.name);
      // this.fileWinData.append('file', file.file);
      this.fileWinData.append("matId", rowData.matId);
      this.fileWinData.append("matinstCode", rowData.matCode);
      this.fileWinData.append("matinstName", rowData.matName ? rowData.matName : '');
      this.fileWinData.append("projInfoId", _that.projInfoId);
      this.fileWinData.append("matinstId", rowData.matinstId ? rowData.matinstId : '');
      this.fileWinData.append("matProp", rowData.matProp?rowData.matProp:'');
      this.fileWinData.append("certId", rowData.certId?rowData.certId:'');
      this.fileWinData.append("stoFormId", rowData.stoFormId?rowData.stoFormId:'');
      _that.loadingFileWin = true;
      axios.post(ctx + 'rest/file/uploadFile', _that.fileWinData).then(function (res) {
        if (res.data.success) {
          file.forEach(function (u) {
            Vue.set(u.file, 'matinstId', res.data.content);
          })
          // Vue.set(file.file,'matinstId',res.data.content)
          _that.selMatinstId = res.data.content;
          _that.getFileListWin(res.data.content, rowData);
          _that.allMatsTableData.map(function (item) {
            if (item.matId == rowData.matId) {
              item.matinstId = res.data.content;
              _that.showFileListKey.push(item.matId)
            }
          });
          _that.loadingFileWin = false;
          _that.$message({
            message: '上传成功',
            type: 'success'
          });
        } else {
          _that.loadingFileWin = false;
          _that.$message({
            message: '上传失败',
            type: 'error'
          });
        }
      }).catch(function (error) {
        console.log(error, JSON.stringify(error));
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
    // 判断文件类型是否存在规定的类型里
    isSpecifiedFileType: function (fileName) {
      var fileTypes = [".jpg", ".png", ".rar", ".txt", ".zip", ".doc", ".ppt", ".xls", ".pdf", ".docx", ".xlsx"];
      var getFileType = fileName.substring(fileName.indexOf(".")).toLowerCase();
      if (fileTypes.indexOf(getFileType) > -1) {
        return true;
      } else {
        return false;
      }
    },
    // 一张表单获取并联申报实例化id
    getParallelApplyinstId: function (flag,_stoFormId) {
      var _that = this;
      var _applySubject = '';
      if (_that.applyObjectInfo.role == 2) {
        _applySubject = 1;
      } else {
        _applySubject = 0;
      }
      if (_applySubject == 0) {
        _that.userInfoId = this.applyObjectInfo.aeaLinkmanInfo.linkmanInfoId;
        _that.userLinkmanCertNo = _that.applyObjectInfo.aeaLinkmanInfo.linkmanCertNo;
      }
      var parmas = {
        applySource: 'net',
        applySubject: _applySubject,
        linkmanInfoId: _that.userInfoId,
      };
      _that.loading = true;
      request('', {
        url: ctx + 'rest/userCenter/apply/net/process/form/start',
        type: 'post',
        data: parmas
      }, function (res) {
        if (res.success) {
          _that.loading = false;
          if (res.content) {
            _that.parallelApplyinstId = res.content;
            if(flag=='matForm'){
              _that.getOneFormrender3(_that.parallelApplyinstId,_stoFormId);
            }else {
              _that.oneFormDataAllow = false;
              _that.declareStep = 6;
              _that.getOneFormList(_that.stageId); // 获取一张表单html
            }
          } else {
            _that.declareStep = 7;
            _that.saveAndGetMats(); // 获取材料
          }
        } else {
          _that.loading = false;
          _that.$message({
            message: '实例化失败',
            type: 'error'
          });
        }
      })
    },
    // 提交申请
    startParalleApprove: function () {
      var _that = this;
      var projInfoIds = []; // 项目ID集合
      var applySubjectType = '';
      // 申报主体为1、2
      if (_that.applyObjectInfo.role == 2) {
        applySubjectType = 1;
      } else {
        applySubjectType = 0;
      }
      if (applySubjectType == 0) {
        _that.unitInfoId = '';
        _that.unifiedSocialCreditCode = '';
        _that.userInfoId = this.applyObjectInfo.aeaLinkmanInfo.linkmanInfoId;
        _that.userLinkmanCertNo = _that.applyObjectInfo.aeaLinkmanInfo.linkmanCertNo;
      } else {
        _that.unitInfoId = this.applyObjectInfo.aeaUnitInfo.unitInfoId;
        _that.unifiedSocialCreditCode = _that.applyObjectInfo.aeaUnitInfo.unifiedSocialCreditCode;
      }
      projInfoIds.push(_that.projInfoId);
      _that.getMatIdAndMatinstId();
      if (!_that.attIsRequireFlag) {
        alertMsg('', '请上传所有必传的电子件', '关闭', 'warning', true);
        return false;
      }
      var parmas = {
        applySource: 'net',
        applySubject: applySubjectType,
        applyinstIds: _that.applyinstIds,
        applyinstId: _that.parallelApplyinstId,
        itemVerIds: _that.itemVerIds,
        linkmanInfoId: _that.userInfoId,
        matinstsIds: _that.matinstIds,
        projInfoIds: projInfoIds,
        propulsionItemVerIds: _that.propulsionItemVerIds,
        smsInfoId: _that.smsInfoId,
        stageId: _that.stageId,
        stateIds: _that.stateIds,
        themeId: _that.themeId,
        propulsionItemStateIds: _that.propulsionItemStateIds,
        parallelItemStateIds: _that.parallelItemStateIds,
        unitInfoId: _that.unitInfoId,
        propulsionBranchOrgMap: _that.propulsionBranchOrgMap,
        branchOrgMap: _that.branchOrgMap,
        unitProjIds: _that.unitProjIds,
      }
      _that.progressDialogVisible = true;
      _that.progressIntervalStop = false;
      _that.progressStus = null;
      _that.setUploadPercentage();
      request('', {
        url: ctx + 'rest/userCenter/apply/net/process/start',
        type: 'post',
        ContentType: 'application/json',
        timeout: 1000000,
        data: JSON.stringify(parmas)
      }, function (res) {
        if (res.success) {
          _that.uploadPercentage = 100;
          _that.progressIntervalStop = true;
          _that.progressStus = 'success';
          _that.progressDialogVisible = false;
          _that.declareStep = 8;
          _that.applySuccessProjInfo = res.content;
          // _that.applySuccessProjInfo.parallelItemNameStr=_that.applySuccessProjInfo.parallelItemNames.join('， ');
          var nowDate = new Date();
          var yearstr = nowDate.getFullYear();
          var monthstr = Number(nowDate.getMonth()) + 1;
          var datestr = nowDate.getDate();
          var hourstr = Number(nowDate.getHours());
          var minstr = Number(nowDate.getMinutes());
          var printstr = "您于" + yearstr + "年" + monthstr + "月" + datestr + "日" + hourstr + "时" + minstr + "分在唐山网上办事大厅申请的【" + _that.applySuccessProjInfo.projName + "】项目已经完成申报。";
          _that.applySuccessProjInfo.printstr = printstr;
          if (_that.applySuccessProjInfo.coreItemList) {
            _that.applySuccessProjInfo.coreItemList.map(function (item) {
              Vue.set(item, 'statusCss', '');
              Vue.set(item, 'statusText', '');
              Vue.set(item, 'statusImg', '')
              _that.getStatusStyle(item);
            });
          }
          if (_that.applySuccessProjInfo.paraItemVoList) {
            _that.applySuccessProjInfo.paraItemVoList.map(function (item) {
              Vue.set(item, 'statusCss', '');
              Vue.set(item, 'statusText', '');
              Vue.set(item, 'statusImg', '')
              _that.getStatusStyle(item);
            });
          }
        } else {
          _that.progressIntervalStop = true;
          _that.progressStus = 'error';
          _that.$message({
            message: res.message ? res.message : '提交申请失败！',
            type: 'error'
          });
          _that.$nextTick(function () {
            _that.progressDialogVisible = false;
          });
        }
      }, function (msg) {
        _that.progressDialogVisible = false;
        _that.progressIntervalStop = true;
        _that.progressStus = 'error';
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    // 切换证照查询条件
    applySubjectChange: function(val){
      console.log(val);
      this.selApplySubject = val;
      if(val.applySubjectType == "applyLinkman"){
        this.searchInline.showNumberType = false;
      }else {
        this.searchInline.showNumberType = true;
      }
      this.getCertFileListWin(val,'oneUnit');
    },
    // 切换证照查询条件
    identityNumberTypeChange: function(val){
      this.getCertFileListWin(this.selApplySubject,'oneUnit');
    },
    showUploadWindow: function (data, index,flag) { // 展示文件上传下载弹窗
      var _that = this;
      _that.selMatRowData = data;
      _that.selMatRowDataInd = index;
      _that.selMatinstId = data.matinstId ? data.matinstId : null;
      _that.allApplySubjectInfo = [];
      if(_that.applyObjectInfo.aeaUnitInfo){
        _that.allApplySubjectInfo.push(_that.applyObjectInfo.aeaUnitInfo);
      }
      _that.jiansheFrom.map(function(unitItem){
        if(unitItem.unifiedSocialCreditCode){
          unitItem.applySubjectName = unitItem.applicant;
          _that.allApplySubjectInfo.push(unitItem);
        }
      });
      if(_that.applyObjectInfo.aeaLinkmanInfo.applyLinkmanIdCard){
        _that.applyObjectInfo.aeaLinkmanInfo.applySubjectType = 'applyLinkman';
        _that.applyObjectInfo.aeaLinkmanInfo.applySubjectName = _that.applyObjectInfo.aeaLinkmanInfo.applyLinkmanName;
        _that.applyObjectInfo.aeaLinkmanInfo.linkmanCertNo = _that.applyObjectInfo.aeaLinkmanInfo.applyLinkmanIdCard;
        _that.allApplySubjectInfo.push(_that.applyObjectInfo.aeaLinkmanInfo);
      }
      if(flag=='certList') {
        _that.showCertWindowFlag = true;
        _that.getCertFileListWin(data);
      }else {
        _that.showUploadWindowFlag = true;
        _that.getMyMatsList();
        _that.getFileListWin(_that.selMatinstId, data);
      }
    },
    // 获取证照文件列表
    getCertFileListWin: function (matData,flag) { // flag==oneUnit 查询单个建设单位
      var _that = this, _identityNumber='';
      var certChild = _that.selMatRowData.certChild;
      var certChildIds = [];
      if(certChild.length>0){
        certChild.map(function(item){
          if(certChildIds.indexOf(item.authCode)<0){
            certChildIds.push(item.authCode);
          }
        })
      }
      if(flag=='oneUnit'){
        if(matData.applySubjectType == 'applyLinkman'){
          _identityNumber = matData.applyLinkmanIdCard;
        }else {
          if(_that.searchInline.identityNumberType=='0'){
            _identityNumber = matData.unifiedSocialCreditCode;
          }else {
            _identityNumber = matData.idno;
          }
        }
      }else {
        if(_that.applySubjectType == 0){
          _identityNumber = _that.applyPersonFrom.applyLinkmanIdCard;
        }else {
          var identityNumbers = [];
          _that.jiansheFrom.map(function (unitItem) {
            identityNumbers.push(unitItem.unifiedSocialCreditCode);
          });
          _identityNumber = identityNumbers.join(',');
        }
      }
      request('', {
        url: ctx + 'aea/cert/getLicenseAuthRes',
        type: 'get',
        data: {identityNumber: _identityNumber,itemVerIds: _that.selMatRowData.itemVerId}
      }, function (res) {
        if(res){
          // if(res.content){
          // res.content = {
          //   "ack_code": "SUCCESS",
          //   "errors": [],
          //   "sign": null,
          //   "sign_method": null,
          //   "timestamp": null,
          //   "correlation_id": "386ae0f3-be3e-40e8-8b3d-b6d0fba55289",
          //   "response_id": "671a4b04-0e1f-4ec0-bc82-3c6b1766a730",
          //   "data": [
          //     {
          //       "name": "普通义工（志愿者）证",
          //       "division": "",
          //       "remark": null,
          //       "creator": null,
          //       "issuer": null,
          //       "abolisher": null,
          //       "algorithm": null,
          //       "license_code": "44070020190000090V",
          //       "license_type": null,
          //       "id_code": "122230",
          //       "doc_name": null,
          //       "doc_summary": null,
          //       "doc_keyword": null,
          //       "holder_name": "测试",
          //       "holder_identity_type": "10",
          //       "holder_identity_num": "440782199411112150",
          //       "issue_org_name": "江门政务服务数据管理局",
          //       "issue_org_code": "593330629",
          //       "division_code": "440700",
          //       "issue_date": "2019-08-19 00:00:00",
          //       "begin_date": null,
          //       "expiry_date": null,
          //       "data_fields": null,
          //       "attachment_fields": null,
          //       "trust_level": "A",
          //       "extend_props": "",
          //       "biz_num": null,
          //       "license_item_code": "113002201",
          //       "license_status": "ISSUED",
          //       "creation_time": "2019-08-22 16:50:41",
          //       "issue_time": null,
          //       "abolish_time": null,
          //       "abolish_reason": null,
          //       "correlative_license": null,
          //       "public_key": null,
          //       "last_modificator": "*SYSADM*",
          //       "last_modification_time": "2019-08-22 16:50:41",
          //       "s_sign_cert": null,
          //       "s_sign_data": null,
          //       "auth_code": "20191115101022412NC011344709_44070020190000090V"
          //     },
          //     {
          //       "name": "普通义工（志愿者）证",
          //       "division": "",
          //       "remark": null,
          //       "creator": null,
          //       "issuer": null,
          //       "abolisher": null,
          //       "algorithm": null,
          //       "license_code": "44070020190000090U",
          //       "license_type": null,
          //       "id_code": "12223",
          //       "doc_name": null,
          //       "doc_summary": null,
          //       "doc_keyword": null,
          //       "holder_name": "测试",
          //       "holder_identity_type": "10",
          //       "holder_identity_num": "440782199411112150",
          //       "issue_org_name": "江门政务服务数据管理局",
          //       "issue_org_code": "593330629",
          //       "division_code": "440700",
          //       "issue_date": "2019-08-19 00:00:00",
          //       "begin_date": null,
          //       "expiry_date": null,
          //       "data_fields": null,
          //       "attachment_fields": null,
          //       "trust_level": "A",
          //       "extend_props": "",
          //       "biz_num": null,
          //       "license_item_code": "113002201",
          //       "license_status": "ISSUED",
          //       "creation_time": "2019-08-22 16:49:07",
          //       "issue_time": null,
          //       "abolish_time": null,
          //       "abolish_reason": null,
          //       "correlative_license": null,
          //       "public_key": null,
          //       "last_modificator": "*SYSADM*",
          //       "last_modification_time": "2019-08-22 16:49:07",
          //       "s_sign_cert": null,
          //       "s_sign_data": null,
          //       "auth_code": "20191115101022412NC011344709_44070020190000090U"
          //     },
          //     {
          //       "name": "志愿服务记录证明",
          //       "division": "",
          //       "remark": null,
          //       "creator": null,
          //       "issuer": null,
          //       "abolisher": null,
          //       "algorithm": null,
          //       "license_code": "44070020190000080W",
          //       "license_type": null,
          //       "id_code": "1122",
          //       "doc_name": null,
          //       "doc_summary": null,
          //       "doc_keyword": null,
          //       "holder_name": "黄大三",
          //       "holder_identity_type": "10",
          //       "holder_identity_num": "440782199411112150",
          //       "issue_org_name": "江门市网络信息统筹局",
          //       "issue_org_code": "MB2C44198",
          //       "division_code": "440700",
          //       "issue_date": "2019-07-26 00:00:00",
          //       "begin_date": null,
          //       "expiry_date": "2023-07-01 00:00:00",
          //       "data_fields": null,
          //       "attachment_fields": null,
          //       "trust_level": "A",
          //       "extend_props": "",
          //       "biz_num": null,
          //       "license_item_code": "213002401",
          //       "license_status": "ISSUED",
          //       "creation_time": "2019-07-26 18:02:17",
          //       "issue_time": null,
          //       "abolish_time": null,
          //       "abolish_reason": null,
          //       "correlative_license": null,
          //       "public_key": null,
          //       "last_modificator": "4f070706-c30c-481c-a837-9f39136c62de",
          //       "last_modification_time": "2019-07-26 18:04:59",
          //       "s_sign_cert": null,
          //       "s_sign_data": null,
          //       "auth_code": "20191115101022412NC011344709_44070020190000080W"
          //     },
          //     {
          //       "name": "普通义工（志愿者）证",
          //       "division": "",
          //       "remark": null,
          //       "creator": null,
          //       "issuer": null,
          //       "abolisher": null,
          //       "algorithm": null,
          //       "license_code": "4407002019000008CS",
          //       "license_type": null,
          //       "id_code": "12333",
          //       "doc_name": null,
          //       "doc_summary": null,
          //       "doc_keyword": null,
          //       "holder_name": "测试",
          //       "holder_identity_type": "10",
          //       "holder_identity_num": "440782199411112150",
          //       "issue_org_name": "江门市网络信息统筹局",
          //       "issue_org_code": "MB2C44198",
          //       "division_code": "440700",
          //       "issue_date": "2019-01-04 00:00:00",
          //       "begin_date": null,
          //       "expiry_date": "2020-01-03 00:00:00",
          //       "data_fields": null,
          //       "attachment_fields": null,
          //       "trust_level": "A",
          //       "extend_props": "",
          //       "biz_num": null,
          //       "license_item_code": "113002201",
          //       "license_status": "ISSUED",
          //       "creation_time": "2019-07-26 14:40:38",
          //       "issue_time": null,
          //       "abolish_time": null,
          //       "abolish_reason": null,
          //       "correlative_license": null,
          //       "public_key": null,
          //       "last_modificator": "4f070706-c30c-481c-a837-9f39136c62de",
          //       "last_modification_time": "2019-07-26 14:41:12",
          //       "s_sign_cert": null,
          //       "s_sign_data": null,
          //       "auth_code": "20191115101022412NC011344709_4407002019000008CS"
          //     }
          //   ],
          //   "total_count": 4
          // };
          _that.certMatTableData = res.content.data?res.content.data:[];
          res.content.data.map(function(certItem){
            if(certItem.bind=='undefined'||certItem.bind==undefined){
              Vue.set(certItem,'bind',false);
            }else {
              certItem.bind = false;
            }
            if(certChildIds.indexOf(certItem.auth_code)>-1){
              certItem.bind = true
            }else {
              certItem.bind = false;
            }
          })
        }else {
          _that.$message({
            message: res.message?res.message:'加载证照库材料失败',
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
    // 展示材料一张表单弹窗
    showOneFormDialog1: function(oneformMat){
      var _that = this;
      var _applyinstId = _that.parallelApplyinstId;
      _that.matformNameTitle = oneformMat.matName
      if(_applyinstId==''){
        _that.getParallelApplyinstId('matForm',oneformMat.stoFormId)
      }else {
        _that.getOneFormrender3(_applyinstId,oneformMat.stoFormId)
      }
    },
    // 获取材料一张表单render
    getOneFormrender3: function(_applyinstId,_formId){
      var _that = this;
      // _formId = 'ecbebb64-a29c-41c6-abb7-e7b337a1a2cb';
      var sFRenderConfig='&showBasicButton=true&includePlatformResource=false';
      request('', {
        url: ctx + 'bpm/common/sf/renderHtmlFormContainer?listRefEntityId='+_applyinstId+'&listFormId='+_formId+sFRenderConfig,
        type: 'get',
      }, function (result) {
        if (result.success) {
          _that.matFormDialogVisible = true;
          $('#matFormContent').html(result.content)
          _that.$nextTick(function(){
            $('#matFormContent').html(result.content)
          });
        }else {
          _that.$message({
            message: result.content?result.content:'获取材料表单失败！',
            type: 'error'
          });
        }
      },function(res){
        _that.$message({
          message: '获取材料表单失败！',
          type: 'error'
        });
      })
    },
    // 查看证照
    cretPreview: function (authCode) {
      var _that = this;
      var tempwindow=window.open(); // 先打开页面
      request('', {
        url: ctx + 'aea/cert/getViewLicenseURL',
        type: 'get',
        data: {authCode: authCode}
      }, function (res) {
        if(res.success){
          setTimeout(function(){
            tempwindow.location=res.content;
          },500)
        }else {
          tempwindow.close();
          _that.$message({
            message: res.message?res.message:'证照查看失败',
            type: 'error'
          });
        }
      }, function (msg) {
        tempwindow.close();
        _that.$message({
          message: msg.message?msg.message:'证照查看失败',
          type: 'error'
        });
      });
    },
    // 关联证照
    setCretLinked: function(certRowData){
      var _that = this;
      _that.rootUnitInfoId = '';
      _that.rootLinkmanInfoId = '';
      _that.rootApplyLinkmanId = '';
      if(_that.jiansheFrom.length>0) {
        _that.rootUnitInfoId = _that.jiansheFrom[0].unitInfoId;
        _that.rootLinkmanInfoId = _that.jiansheFrom[0].linkmanId;
      }
      if(_that.applySubjectType == 0&&_that.applyPersonFrom.applyLinkmanId){
        _that.rootApplyLinkmanId = _that.applyPersonFrom.applyLinkmanId;
        _that.rootLinkmanInfoId = _that.applyPersonFrom.linkLinkmanId
      };
      var param = {
        "authCode": certRowData.auth_code,
        "certId": _that.selMatRowData.certId,
        "certOwner": certRowData.holder_name,
        "certinstCode": certRowData.license_code,
        "certinstId": "",
        "certinstName": certRowData.license_code,
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
        url: ctx + 'aea/cert/bind/cert',
        type: 'post',
        ContentType: 'application/json',
        data: JSON.stringify(param)
      }, function (res) {
        if(res.success){
          res.content.certName = certRowData.name;
          if(_that.selMatRowData.certChild=='undefined'||_that.selMatRowData.certChild==undefined){
            Vue.set(_that.selMatRowData,'certChild',[res.content]);
          }else {
            _that.selMatRowData.certChild.push(res.content);
          }
          if(_that.selMatRowData.certMatinstIds=='undefined'||_that.selMatRowData.certMatinstIds==undefined){
            Vue.set(_that.selMatRowData,'certMatinstIds',[res.content.matinstId]);
          }else {
            if(_that.selMatRowData.certMatinstIds.indexOf(res.content.matinstId)<0){
              _that.selMatRowData.certMatinstIds.push(res.content.matinstId);
            }
          }
          certRowData.bind = true;
          _that.$message({
            message: '证照关联成功',
            type: 'success'
          });
          if(_that.showFileListKey.indexOf(_that.selMatRowData.matId)<0){
            _that.showFileListKey.push(_that.selMatRowData.matId)
          }
        }
      }, function (msg) {
        _that.$message({
          message: msg.message?msg.message:'证照关联失败',
          type: 'error'
        });
      });
    },
    // 解除关联
    unbindCert: function(matinstId,matDataCertChild,index){
      var _that = this;
      request('', {
        url: ctx + 'aea/cert/unbind/cert',
        type: 'post',
        data: {matinstId: matinstId}
      }, function (data) {
        if(data.success){
          matDataCertChild.splice(index,1);
        }
      }, function (msg) {
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    // 切换选中的材料 flag==prev 上一个 next 下一个
    changeRowDataSel: function (flag) {
      if (flag == 'prev') {
        if (this.selMatRowDataInd > 0) {
          this.selMatRowDataInd--
        }
      } else {
        if (this.selMatRowDataInd < (this.allMatsTableData.length - 1)) {
          this.selMatRowDataInd++
        }
      }
      this.selMatRowData = this.allMatsTableData[this.selMatRowDataInd];
      this.selMatinstId = this.selMatRowData.matinstId ? this.selMatRowData.matinstId : null;
      this.getFileListWin(this.selMatinstId, this.selMatRowData);
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
    // 获取已上传文件列表
    getFileListWin: function (matinstId, rowData) {
      var _that = this;
      _that.selMatLoading = true;
      request('', {
        url: ctx + 'rest/file/getAttFiles/' + matinstId,
        type: 'get',
        data: { matinstId: matinstId }
      }, function (res) {
        _that.selMatLoading = false;
        if (res.success) {
          if (res.content) {
            _that.uploadTableData = res.content;
            _that.uploadFilesCount = res.content.length;
            var uploadFileIds = [];
            if (_that.uploadTableData.length > 0) {
              _that.uploadTableData.map(function (uploadItem) {
                if (uploadFileIds.indexOf(uploadItem.fileId) < 0) {
                  uploadFileIds.push(uploadItem.fileId);
                }
              });
            }
            if (_that.myMatsList.length > 0) {
              _that.myMatsList.map(function (item) {
                if (typeof item.checked == undefined) {
                  Vue.set(item, 'checked', false);
                }
                if (uploadFileIds.length > 0 && uploadFileIds.indexOf(item.fileId) > -1) {
                  item.checked = true;
                } else {
                  item.checked = false;
                }
              })
            }
            if (_that.myDirMatsList.length > 0) {
              _that.myDirMatsList.map(function (item) {
                if (typeof item.checked == undefined) {
                  Vue.set(item, 'checked', false);
                }
                if (uploadFileIds.length > 0 && uploadFileIds.indexOf(item.detailId) > -1) {
                  item.checked = true;
                } else {
                  item.checked = false;
                }
              })
            }
            if (_that.uploadFilesCount > 0 && _that.showFileListKey.indexOf(rowData.matId) < 0) {
              _that.showFileListKey.push(rowData.matId);
            }
            if (rowData) {
              rowData.childs = res.content;
            }
          }
        } else {
          _that.$message({
            message: res.message ? res.message : '加载已上传材料列表失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.selMatLoading = false;
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    // 跳转办事指南
    goToBszn: function (data) {
      var _that = this;
      var itemVerId, itemName;
      if (data.isCatalog == 1) {
        itemVerId = data.baseItemVerId
      } else {
        itemVerId = data.itemVerId
      }
      itemName = data.itemName;
      var outerSystemUrl = data.outerSystemUrl;
      var isLink = data.isLink;
      if (!!isLink && isLink == '1') {
        if (!outerSystemUrl) {
          _that.$message.warning("该事项暂无办事指南！");
        } else {
          window.open(outerSystemUrl);
        }
      } else {
        var tempwindow = window.open();//先打开页面
        setTimeout(function () {
          tempwindow.location = ctx + 'rest/main/toIndexPage?itemVerId=' + itemVerId + '&&itemName=' + encodeURIComponent(encodeURIComponent(itemName)) + '&&flag=bszn#/guideIndex';
        }, 400)
      }

    },
    // 跳转到单项申报
    goToSingleDeclare: function (itemVerId) {
      var projInfoIdDec = this.projInfoId;
      var tempwindow = window.open(); // 先打开页面
      setTimeout(function () {
        tempwindow.location = ctx + 'rest/main/toIndexPage?itemVerId=' + itemVerId + '&&projInfoId=' + projInfoIdDec + '&&flag=singleD#declare';
      }, 400)
    },
    // 根据阶段id查询关联的表单
    getOneFormList: function (_stageId) {
      var _that = this;
      var selItemVer = _that.$refs.parallelItemsTable.selection; // 所有选择的并联审批事项
      var str = '';
      var sFRenderConfig = '&showBasicButton=true&includePlatformResource=false';
      console.log(selItemVer);
      selItemVer.map(function (item) {
        str += 'itemids=' + item.itemId + '&'
      });
      _that.loadingOneForm = true;
      request('', {
        url: ctx + 'rest/oneform/common/renderHtmlFormContainer?' + str + sFRenderConfig,
        type: 'post',
        data: { stageId: _stageId, applyinstId: _that.parallelApplyinstId }
      }, function (result) {
        if (result.success) {
          _that.loadingOneForm = false;
          _that.oneFormHtml = result.content
          $('#oneFormContent').html(result.content)
          _that.$nextTick(function () {
            $('#oneFormContent').html(result.content)
          });
        } else {
          _that.loadingOneForm = false;
          _that.$message({
            message: result.content ? result.content : '获取表单失败！',
            type: 'error'
          });
        }
      }, function (res) {
        _that.loadingOneForm = false;
        _that.$message({
          message: '获取表单失败！',
          type: 'error'
        });
      })
    },
    // 审批状态对应样式
    getStatusStyle: function (item) {
      var str = '';
      switch (item.iteminstState) {
        case '1':
          str = '已接件';
          break;
        case '2':
          str = '已撤件'
          break;
        case '3':
          str = '已受理'
          break;
        case '4':
          str = '不受理'
          break;
        case '5':
          str = '不予受理'
          break;
        case '6':
          str = '补正（开始）'
          break;
        case '7':
          str = '补正（结束）'
          break;
        case '8':
          str = '部门开始办理'
          break;
        case '9':
          str = '特别程序（开始）'
          break;
        case '10':
          str = '特别程序（结束）'
          break;
        case '11':
          str = '办结（通过）'
          break;
        case '12':
          str = '办结（容缺通过）'
          break;
        case '13':
          str = '办结（不通过）'
          break;
        case '14':
          str = '撤回'
          break;
        case '15':
          str = '撤销'
          break;
        default:
          str = '-'
      }
      item.statusCss = 'status-css_' + item.iteminstState;
      item.statusImg = 'mall/images/icon/statusImg_' + item.iteminstState + '.png';
      item.statusText = str;
    },
    //获取行政分区列表
    querySelentDistrict: function (rootOrgId) {
      var _that = this;
      request('', {
        url: ctx + 'rest/org/region/list',
        type: 'get',
        data: { rootOrgId: rootOrgId }
      }, function (result) {
        if (result.success) {
          _that.districtList = result.content;
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    //获取部门组织
    querySelectDept: function (rootOrgId) {
      var _that = this;
      request('', {
        url: ctx + 'rest/user/getOrgs',
        type: 'get',
        data: { rootOrgId: rootOrgId }
      }, function (result) {
        if (result.success) {
          _that.orgList = result.content;
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },

    querySelecTheme: function (themeType) {
      var _that = this;
      request('', {
        url: ctx + 'rest/user/getThemes',
        type: 'get',
        //data: {themeType:themeType}
      }, function (result) {
        if (result.success) {
          _that.themeList = result.content;
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    // 根据企业或者个人id查询是否属于黑名单及信用记录
    getPersonOrUnitBlackByBizId: function (unitItem) {
      var _that = this;
      var _bizCode = '', _bizType = '';
      _that.applyObjectInfo.role
      if (_that.applyObjectInfo.role == 0) {
        _bizCode = _that.userLinkmanCertNo;
        _bizType = 'l';
      } else if (_that.applyObjectInfo.role == 2) {
        if (unitItem) {
          _bizCode = unitItem.unifiedSocialCreditCode;
        } else {
          _bizCode = _that.unifiedSocialCreditCode;
        }
        _bizType = 'u';
      }
      request('', {
        url: ctx + 'rest/user/credit/redblack/getPersonOrUnitBlackAndCreditListByBizCode',
        type: 'get',
        data: { bizCode: _bizCode, bizType: _bizType }
      }, function (result) {
        if (result.success) {
          if (result.content.isBlack == 1) {
            _that.creditDiaVisible = true; // 展示信用记录弹窗
            _that.isBlack = true;
          } else {
            _that.isBlack = false;
          }
          _that.loseCreditCount = 0;
          _that.creditTypeList = result.content.creditList;
          result.content.creditList.map(function (item) {
            if (item.itemCode == 'bad' && item.summaries.length > 0) {
              _that.loseCreditCount = item.summaries.length;
            }
            item.summaries.map(function (itemSum) {
              Vue.set(itemSum, 'isOpen', false);
            })
          });
          console.log(_that.creditTypeList);
        } else {
          // _that.creditDiaVisible = false; // 展示信用记录弹窗
          _that.$message.error(result.message ? result.message : '获取信用状态失败！');
        }
      }, function (msg) {
        // _that.creditDiaVisible = false; // 展示信用记录弹窗
        // alertMsg('', msg.message?msg.message:'网络加载失败！', '关闭', 'error', true);
      })
    },
    // 事项设置实施事项标准事项
    setImplementItem: function (item) {
      var _that = this;
      Vue.set(item, 'disabled', false); // 行政区划是否可选
      Vue.set(item, 'notRegionData', false); // 无匹配的行政区划及承办单位
      Vue.set(item, 'preItemCheckPassed', true); // 前置事项检查是否可选
      if (item.isCatalog != 1) {
        item.itemVerId = item.itemVerId
      } else {
        if (item.currentCarryOutItem) {
          item.itemVerId = item.currentCarryOutItem.itemVerId
          item.orgId = item.currentCarryOutItem.orgId;
          item.orgName = item.currentCarryOutItem.orgName;
          item.paraStateList = item.currentCarryOutItem.paraStateList;
          item.coreStateList = item.currentCarryOutItem.coreStateList;
          if (item.coreStateList && item.coreStateList.length > 0) {
            item.coreStateList.map(function (itemState) {
              if (itemState.answerType != 's' && itemState.answerType != 'y') {
                Vue.set(itemState, 'selValue', []);
                itemState.selectAnswerId = itemState.selValue;
              }
            });
          }
          if (item.paraStateList && item.paraStateList.length > 0) {
            item.paraStateList.map(function (itemState) {
              if (itemState.answerType != 's' && itemState.answerType != 'y') {
                Vue.set(itemState, 'selValue', []);
                itemState.selectAnswerId = itemState.selValue;
              }
            });
          }
          item.disabled = false;
        } else {
          item.orgId = '';
          item.orgName = '';
          item.disabled = true;
          item.notRegionData = true;
        }
      }
    },
    // 行政区划承办机构选中获得orgId
    getItemOrgId: function (data, index, item, flag) { // flag = 'core' 并行事项
      var selItemVer = this.$refs.parallelItemsTable.selection; // 所有选择的并联审批事项
      var selCoreItemVer = this.$refs.coreItemsTable ? this.$refs.coreItemsTable.selection : ''; // 所有选择的并行审批事项
      var _itemVerIdS = [], selAllItem = [];
      var _that = this;
      var flagSelItem = true; // 是否查询事项下情形材料
      if (flag == 'core') { selAllItem = selCoreItemVer } else { selAllItem = selItemVer }
      if (data) {
        if (data.notRegionData) {
          // if (data.isDone == 'FINISHED' || data.isDone == 'HANDLING' || data.notRegionData){
          return false;
        } else {
          selAllItem.map(function (ind, item1) {
            if (item1.itemVerId && index != ind) {
              if (item.itemVerId == item1.itemVerId) {
                _that.$message({
                  message: '该实施事项已存在，请勿重复选择！',
                  type: 'error'
                });
                flagSelItem = false;
              } else {
                _itemVerIdS.push(item1.itemVerId);
              }
            }
          });
        }
      }
      if (flagSelItem) {
        data.orgId = item.orgId;
        data.orgName = item.orgName;
        data.itemVerId = item.itemVerId;
        if (item.coreStateList && item.coreStateList.length > 0) {
          item.coreStateList.map(function (itemState) {
            if (itemState.answerType != 's' && itemState.answerType != 'y') {
              Vue.set(itemState, 'selValue', []);
              itemState.selectAnswerId = itemState.selValue;
            }
          });
        }
        if (item.paraStateList && item.paraStateList.length > 0) {
          item.paraStateList.map(function (itemState) {
            if (itemState.answerType != 's' && itemState.answerType != 'y') {
              Vue.set(itemState, 'selValue', []);
              itemState.selectAnswerId = itemState.selValue;
            }
          });
        }

        data.coreStateList = item.coreStateList;
        data.paraStateList = item.paraStateList;

        if (flag == 'core') {
          _that.coreItemsSelItem(selCoreItemVer, data);
        } else {
          _that.parallelItemsSelItem(selItemVer, data, 'autoGetSel');
        }
      }
    },
  },
  filters: {
    formatDate: function (time) {
      if (!time) return '-';
      var date = new Date(time);
      if (!date) return;
      return formatDate(date, 'yyyy-MM-dd hh:mm');
    },
    formatLicenseType: function (value) {
      var defaultValue='';
      if(value){
        switch(value) {
          case 'CERTIFICATE':
            defaultValue='证件执照';
            break;
          case 'PROOF':
            defaultValue='证明文件';
            break;
          case 'APPROVAL':
            defaultValue='批文批复';
            break;
          case 'REPORT':
            defaultValue='鉴定报告';
            break;
          case 'RESULT':
            defaultValue='办事结果';
            break;
        }
      }
      return defaultValue;
    },
    formatLicenseStatus: function (value) {
      var defaultValue='';
      if(value){
        switch(value) {
          case 'DRAFT':
            defaultValue='草案';
            break;
          case 'REGISTERED':
            defaultValue='已制证（未签发）';
            break;
          case 'ISSUED':
            defaultValue='已签发';
            break;
          case 'ABOLISHED':
            defaultValue='已废止';
            break;
        }
      }
      return defaultValue;
    },
  },
});

