/*
 * @Author: ZL
 * @Date:   2019/6/4
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var module1 = new Vue({
  el: '#singleDeclare',
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
    // 输入为数字 大于等于0
    var checkMissNum = function (rule, value, callback) {
      if (value === '' || value === undefined || value ===null || value.toString().trim() === '') {
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
        var flag = !/^[1-9A-GY]{1}[1239]{1}[1-5]{1}[0-9]{5}[0-9A-Z]{10}$/.test(value);
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
        {num: '1',  name: '补全信息'},
        {num: '2',  name: '前置条件'},
        {num: '3',  name: '情形选择'},
        {num: '4',  name: '材料一单清'},
        {num: '5',  name: '完成申报'},
      ],
      declareStepWidth: '', // 步骤类名
      isShowAll: false,  // 是否展示更多事项材料一单清
      projInfoId: '', // 查询项目id
      itemVerId: '', // 事项版本id
      itemName: '', // 事项名称
      isNeedFrontCond: '1', // 是否需要前置条件 1是 0否
      isNeedState: '1', // 是否需要情形 1是 0否
      noNext: 0, // 前置条件是否可下一步

      projInfoDetail: {
        isAreaEstimate: '0',
        isDesignSolution: '0',
        gbCodeYear: '2017'
      }, // 项目详细信息
      projTypeList: [], // 项目类型列表
      projNatureList: [], // 建设性质
      districtList:[],//行政区划
      projLevelList:[],//项目级别
      projClassList:[],//立项类型
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
          { required: true,validator: checkMissValue, trigger: 'change' },
        ],
        addresseePhone: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        addresseeIdcard:[
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ]
      },
      rulesResultForm1: {
        addresseeName: [
          { required: true,validator: checkMissValue, trigger: 'change' },
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
          { required: true,validator: checkMissValue, trigger: ['change','blur'] },
        ],
        addresseeCity: [
          { required: true,validator: checkMissValue, trigger: ['change','blur'] },
        ],
        addresseeCounty: [
          { required: true,validator: checkMissValue, trigger: ['change','blur'] },
        ],
        addresseeAddr: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ]
      },
      smsInfoId: '', // 领件人实例id
      isSelItem: 0, // 事项列表是否展示复选框 0否 1是
      stateList: [], // 情形列表
      preconditionList: [], // 前置条件列表
      stateSelVal: {},
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
      applyinstId: '', // 申请实例ID
      selMatRowData: {}, // 所选择的材料信息
      showUploadWindowFlag: false, // 上传材料弹窗
      uploadTableData: [], // 已上传文件列表
      progressDialogVisible: false, // 是否显示进度条loading
      progressIntervalStop: false, // 定时器
      uploadPercentage: 0, // 进度条百分比
      progressStus: null, // 进度条状态
        itemBasicInfo: {},//事项信息
        itemBasicInfoShow:true,//是否展示事项信息
      baseInfoShow: true, // 是否展示项目基本信息
      applyObjectInfoShow: true, // 是否展示申报主体信息
      getResultFormShow: true, // 是否展示取件方式信息
      applySuccessProjInfo: {}, // 申报成功信息
      applySuccessProjInfo1: true, // 是否展示完成申报信息
      applySuccessProjInfo2: true, // 是否展示完成申报信息
      applySuccessProjInfo3: true, // 是否展示完成申报信息
      //itemBasicInfo:{},
      isAble:false,
      rules: {
          projName: [
              {required: true, message: '请输入项目/工程名称', trigger: 'blur'},
          ],
          localCode: [
              {required: true, message: '请输入项目代码', trigger: 'blur'},
          ],
        gcbm: [
          {required: true, message: '请输入工程代码', trigger: 'blur'},
        ],
        regionalism: [
          {required: true, message: '请选择行政区划', trigger: 'change'},
        ],
        projectAddress: [
          {required: true, message: '请选择建设地点', trigger: 'change'},
        ],
        financialSource: [
          {required: true, message: '请选择资金来源', trigger: 'change'},
        ],
        investType: [
          {required: true, message: '请选择投资类型', trigger: 'change'},
        ],
        landSource: [
          {required: true, message: '请选择土地来源', trigger: 'change'},
        ],
        projType: [
          {required: true, message: '请选择立项类型', trigger: 'change'},
        ],
        themeId: [
          {required: true, message: '请选择项目类型', trigger: 'change'},
        ],
        projNature: [
          {required: true, message: '请选择建设性质', trigger: 'change'},
        ],
        projCategory: [
          {required: true, message: '请选择工程分类', trigger: 'change'},
        ],
        nstartTime: [
          {required: true, message: '请选择拟开工时间', trigger: 'blur'},
        ],
        endTime: [
          {required: true, message: '请选择拟建成时间', trigger: 'change'},
        ],
        investSum: [
          {required: true, message: '请输入总投资', trigger: 'blur'},
        ],
        foreignBuildingArea: [
          {required: true, message: '请输入总建筑面积', trigger: 'blur'},
        ],
        xmYdmj: [
          {required: true, message: '请输入用地面积', trigger: 'blur'},
        ],
        gbCodeYear: [
          {required: true, message: '请输入国标行业代码发布年代', trigger: 'blur'},
        ],
        scaleContent: [
          {required: true, message: '请输入建设规模及内容', trigger: 'blur'},
        ]
      },
      addLinkManRules: { // 新增编辑联系人校验
        linkmanName: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanCertNo: [
          { required: true,validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanMobilePhone:[
          { required: true,validator: checkPhoneNum, trigger: 'blur' },
        ]
      },
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
          { required: true,validator: checkPhoneNum, trigger: 'blur' },
        ],
        linkmanName: [
          { required: true,message: '请选择联系人！', trigger: ['change','blur'] },
        ],
        unifiedSocialCreditCode: [
          { required: true,validator: checkUnifiedSocialCreditCode, trigger: ['blur'] },
        ]
      },
      loadingProjInfo: false,
      projInfoList:[],//项目信息
      searchKeyword:'',
      creditTypeList: [], // 信用类型数据
      creditDiaVisible: false, // 是否展示信用弹窗
      creditDiaLoading: false, // 是否展示信用弹窗loading
      isBlack: false, // 是否黑名单
      loseCreditCount: 0, // 失信记录条数
      showUnitMore: false, // 是否展示更多企业信息
      unifiedSocialCreditCode: '', // 企业统一社会信用代码
      userLinkmanCertNo: '', // 个人申报时个人身份证号
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
      addEditManModalTitle: '新增联系人',
      addEditManModalShow: false, // 是否展示新增编辑联系人窗口
      addEditManform: {},  // 新增编辑联系人信息
      addEditManPerform: {}, // 所选编辑的联系人
      unitProjIds: [],
      showItemsDia: false, // 是否展示前置事项列表
      coreItems: [], // 前置事项列表
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
      selMatRowDataInd: 0, // 所选择的材料的index
      myDirMatsList: [], // 我的空间材料列表
      myDirMatsId: [], // 我的空间材料列表Id
    }
  },
  created:function(){
    this.GetRequest();
    this.getProjTypeNature(); // 数据字典查询项目类型
    this.getGbhy();
    this.getItemBaseInfo();
    //this.querySelecTheme() // 获取主题列表
  },
  mounted: function () {
    var _that = this;
    if(_that.projInfoId!='null'){
      _that.searchProjAllInfo(); // 获取项目信息
    }
  },
  methods: {
    // 文件上传获取我的材料列表
    getMyMatsList: function(){
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
        if(result){
          _that.myMatsList = result.list?result.list:[];
          var uploadFileIds = [];
          if(_that.uploadTableData.length>0){
            _that.uploadTableData.map(function(uploadItem){
              if(uploadFileIds.indexOf(uploadItem.fileId)<0){
                uploadFileIds.push(uploadItem.fileId);
              }
            });
          }
          if(_that.myMatsList.length>0){
            _that.myMatsList.map(function(item){
              Vue.set(item,'checked',false);
              if(uploadFileIds.indexOf(item.fileId)>-1){
                item.checked = true;
              }
            })
          }
          _that.myMatsTotal = result.total?result.total:0;
        }
      }, function (msg) {
        _that.mySpaceLoading = false;
        alertMsg('', '我的材料获取失败', '关闭', 'error', true);
      });
    },
    // 文件上传获取我的云盘列表
    getMyDirsList: function(row, treeNode, resolve){
      var _that = this;
      var dirId = row?row.dirId:'root';
      _that.mySpaceLoading = true;
      request('', {
        url: ctx + 'rest/cloud/dirAndAtt/list/'+dirId,
        type: 'get',
        data: {dirId: dirId}
      }, function (result) {
        _that.mySpaceLoading = false;
        if(result){
          if(result.content.dirs.length>0){
            result.content.dirs.map(function (dirsItem) {
              if(typeof dirsItem.hasChildren == 'undefined'){
                Vue.set(dirsItem,'hasChildren',true)
              }
              if(typeof dirsItem.id == 'undefined'){
                Vue.set(dirsItem,'id',dirsItem.dirId)
              }else {
                attsItem.id = attsItem.dirId
              }
            });
          }
          if(_that.myDirMatsList.length>0){
            _that.myDirMatsList.map(function (dirMatsItem) {
              if(_that.myDirMatsId.indexOf(dirMatsItem.detailId)<0){
                _that.myDirMatsId.push(dirMatsItem.detailId);
              }
            });
          }
          if(result.content.atts.length>0){
            result.content.atts.map(function (attsItem) {
              if(typeof attsItem.checked == 'undefined'){
                Vue.set(attsItem,'checked',false)
              }
              if(typeof attsItem.hasCheck == 'undefined'){
                Vue.set(attsItem,'hasCheck',true)
              }
              if(typeof attsItem.id == 'undefined'){
                Vue.set(attsItem,'id',attsItem.detailId)
              }else {
                attsItem.id = attsItem.detailId
              }
              if(_that.myDirMatsId.indexOf(attsItem.detailId)<0){
                _that.myDirMatsList.push(attsItem);
              }
            });
          }
          if(!row){
            _that.myDirsList = result.content.atts.concat(result.content.dirs);
          }else {
            var childdrenArr = result.content.atts.concat(result.content.dirs);
            resolve(childdrenArr)
          }

          var uploadFileIds = [];
          if(_that.uploadTableData.length>0){
            _that.uploadTableData.map(function(uploadItem){
              if(uploadFileIds.indexOf(uploadItem.fileId)<0){
                uploadFileIds.push(uploadItem.fileId);
              }
            });
          }
          if(_that.myDirMatsList.length>0){
            _that.myDirMatsList.map(function(item){
              if(typeof item.checked == 'undefined'){
                Vue.set(item,'checked',false)
              }
              if(uploadFileIds.indexOf(item.detailId)>-1){
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
    getMyDirsListByKeyword: function(){
      var _that = this;
      if(!_that.myDirKeyword){
        _that.getMyDirsList();
      }else {
        _that.mySpaceLoading = true;
        request('', {
          url: ctx + 'rest/cloud/attByKeyword/list/'+_that.myDirKeyword,
          type: 'get',
        }, function (result) {
          _that.mySpaceLoading = false;
          if(result){
            _that.myDirsList = result.content?result.content:[];
            var uploadFileIds = [];
            if(_that.uploadTableData.length>0){
              _that.uploadTableData.map(function(uploadItem){
                if(uploadFileIds.indexOf(uploadItem.fileId)<0){
                  uploadFileIds.push(uploadItem.fileId);
                }
              });
            }
            if(_that.myDirsList.length>0){
              _that.myDirsList.map(function(item){
                if(typeof item.checked == 'undefined'){
                  Vue.set(item,'checked',false)
                }
                if(typeof item.hasCheck == 'undefined'){
                  Vue.set(item,'hasCheck',true)
                }
                if(uploadFileIds.indexOf(item.detailId)>-1){
                  item.checked = true;
                }
                if(typeof item.id == 'undefined'){
                  Vue.set(item,'id',item.detailId)
                }else {
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
    fileSpaceTabClick: function(tab, event){
      if(tab.name=='myMats'){
        if(this.myMatsList.length==0){
          this.getMyMatsList();
        }
      }else {
        if(this.myDirsList.length==0){
          this.getMyDirsList();
        }
      }
    },
    // 切换我的材料选中状态 flag ==dir 我的空间选中材料
    changeMyMatSel: function(val,selMatData,flag){
      console.log(val,selMatData);
      console.log(this.selMatRowData);
      var _that = this;
      var fileId = flag=='dir'?selMatData.detailId:selMatData.fileId;
      var param = {
        detailIds: fileId,
        matId: _that.selMatRowData.matId,
        matinstCode: _that.selMatRowData.matCode,
        matinstName: _that.selMatRowData.matName,
        projInfoId: _that.projInfoId,
        matinstId: _that.selMatRowData.matinstId?_that.selMatRowData.matinstId:''
      };
      if(val){
        request('', {
          url: ctx + 'rest/file/cloud/uploadFile',
          type: 'post',
          data: param
        }, function (result) {
          if(result){
            console.log(result);
            _that.selMatRowData.matinstId = result.content;
            _that.getFileListWin(result.content,_that.selMatRowData);
          }
        }, function (msg) {});
      }else {
        _that.delSelectFileCom(_that.selMatRowData,selMatData,'',flag);
      }

    },
    // 改变我的材料pageSize
    myMatSizeChange: function(val){
      this.myMatsPageSize = val;
      this.getMyMatsList();
    },
    // 改变我的材料pageNum
    myMatCurrentChange: function(val){
      this.myMatsPageNum = val;
      this.getMyMatsList();
    },
    // 改变我的材料pageSize
    myDirSizeChange: function(val){
      this.myDirPageSize = val;
      this.getMyMatsList();
    },
    // 改变我的材料pageNum
    myDirCurrentChange: function(val){
      this.myDirPageNum = val;
      this.getMyMatsList();
    },
    // 前置条件判断
    beforeCheck: function(){
      var _that = this;
      if (_that.projInfoId) {
        request('', {
          url: ctx + 'rest/apply/common/item/frontItemIsDone/'+_that.itemVerId+'/'+_that.projInfoId,
          type: 'POST',
          data: {
            projInfoId: _that.projInfoId,
            itemVerId: _that.itemVerId
          },
        }, function (result) {
          if (result.success) {
            if(result.content.length>0){
              _that.coreItems = result.content;
              _that.showItemsDia = true;
            }
          }else {
            _that.$message({
              message: '获取前置事项失败！',
              type: 'error'
            });
          }
        }, function (msg) {})
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
    // 跳转办事指南
    goToBszn: function(data){
      var itemVerId,itemName;
      if(data.isCatalog==1){
        itemVerId=data.baseItemVerId
      }else {
        itemVerId=data.itemVerId
      }
      itemName = data.itemName;
      var tempwindow=window.open(); // 先打开页面
      setTimeout(function(){
        tempwindow.location = ctx + 'rest/main/toIndexPage?itemVerId='+itemVerId+'&&itemName='+encodeURIComponent(encodeURIComponent(itemName))+'&&flag=bszn#/guideIndex';
      },1000)
    },
    // 提示申报前置事项
    alertMesBefore: function(){
      if(this.coreItems.length>0){
        alertMsg('', '申报此事项前需先办理前置事项', '关闭', 'error', true);
      }
    },
    // 跳转到单项申报
    goToSingleDeclare: function(itemVerId){
      var projInfoIdDec=this.projInfoId;
      var tempwindow=window.open(); // 先打开页面
      setTimeout(function(){
        tempwindow.location = ctx + 'rest/main/toIndexPage?itemVerId='+ itemVerId+'&&projInfoId='+projInfoIdDec+'&&flag=singleD#declare';
      },400)
    },
    // 重置表单
    resetForm: function (formName) {
      this.$refs[formName].resetFields();
      this.selectMat = '';
    },
    // 选择企业联系人信息
    selLinkman: function(pData,data,ind1){
      if(pData){
        pData.selectedLinkman = {
          linkmanName: data.linkmanName,
          linkmanInfoId: data.linkmanInfoId,
          linkmanMail: data.linkmanMail,
          linkmanCertNo: data.linkmanCertNo,
          linkmanMobilePhone: data.linkmanMobilePhone,
        }
      }
      if(data){
        pData.linkmanName = data.linkmanName;
        pData.linkmanInfoId = data.linkmanInfoId;
        pData.linkmanMail = data.linkmanMail;
        pData.linkmanCertNo = data.linkmanCertNo;
        pData.linkmanMobilePhone = data.linkmanMobilePhone;
      }
    },
    // 人员设置选择人员
    selLinkmanTypes: function(pData,data){
      if(pData){
        pData.linkmanName = data.linkmanName;
        pData.linkmanInfoId = data.linkmanInfoId;
        pData.linkmanMail = data.linkmanMail;
        pData.linkmanCertNo = data.linkmanCertNo;
        pData.linkmanMobilePhone = data.linkmanMobilePhone;
      }
    },
    // 新增单位信息
    addUnitInfoForm: function(){
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
    addLinkmanTypes: function(row){
      var dataType1 = {
        linkmanInfoId: '',
        linkmanType: '',
        linkmanName: ''
      }
      row.push(dataType1);
    },
    // 移除人员设置
    delLinkmanTypes: function(row,index){
      row.splice(index,1);
    },
    // 新增编辑联系人信息
    addLinkman: function(data,parData){
      var _that = this;
      _that.addEditManModalShow = true;
      _that.addEditManPerform = parData;
      if(_that.projInfoId){
        if(data){
          _that.addEditManModalTitle = '编辑联系人';
          _that.addEditManModalFlag = 'edit';
          if(!data.linkmanInfoId){
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
      if(data.linkmanInfoId){
        request('', {
          url: ctx + 'rest/unit/getLinkmanInfoById/'+data.linkmanInfoId,
          type: 'get',
          ContentType: 'application/json',
          data: {linkmanInfoId:data.linkmanInfoId}
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
        _that.aeaLinkmanInfoList = [];
      }
    },
    // 保存联系人信息
    saveAeaLinkmanInfo: function (linkmanType) {
      var _that = this;
      _that.addEditManform.linkmanType=linkmanType
      _that.$refs['addEditManform'].validate(function (valid) {
        if (valid) {
          _that.loading = true;
          request('', {
            url: ctx + 'rest/user/linkman/save',
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
    // 删除联系人
    delLinkman: function (data,parentData,ind) {
      var _that = this;
      if(!data.linkmanInfoId){
        alertMsg('提示信息', '联系人ID为空', '关闭', 'warning', true);
        return;
      }else{
        confirmMsg('此操作影响：','确定要删除该联系人吗？',function(){
          request('', {
            url: ctx + 'rest/user/linkman/delete/'+parentData.unitInfoId+'/'+data.linkmanInfoId,
            type: 'post',
            data: {linkmanInfoId:data.linkmanInfoId,unitInfoId:parentData.unitInfoId}
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
    // 根据单位id获取关联联系人
    getLinkManListByUnitId: function(item,unitInfoId,flag){
      var _that = this;
      if(unitInfoId){
        request('', {
          url: ctx + 'rest/user/linkman/getByUnitInfoId/'+unitInfoId,
          type: 'get',
          data: {
            unitInfoId: unitInfoId
          }
        }, function (data) {
          if(data.success){
            if(flag=='applyObjectInfo'){
              item.linkmanInfoList = data.content
            }else {
              item.aeaLinkmanInfoList = data.content;
            }
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
    setShowUnitMore: function(index){
      if(this.showUnitListMore.indexOf(index)>-1){
        this.showUnitListMore = this.showUnitListMore.filter(function(item) {
          return item != index;
        });
      }else {
        this.showUnitListMore.push(index);
      }
    },
    // 申办主体信息 企业非企业申报 单位名称模糊查询
    querySearchJiansheName: function(queryString, cb){
      var _that = this;
      if (typeof (queryString) != 'undefined') queryString = queryString.trim();
      if (queryString != '') {
        _that.projNameSelect = _that.projUnitList;

        _that.projNameSelect.map(function (item) {
          if(item){
            Vue.set(item, 'value', item.applicant);
          }
        })
        var results = queryString ? _that.projNameSelect.filter(_that.createFilter(queryString)) : _that.projNameSelect;
        // 调用 callback 返回建议列表的数据
        cb(results);
      }
    },
    // 选择切换申办主体单位
    selUnitInfo: function(val, index){ // val选中单位信息 index单位索引
      if(this.jiansheFrom[index].unitInfoId != val.unitInfoId){
        this.getPersonOrUnitBlackByBizId(val);
      }
      this.jiansheFrom[index] = val;
      if(val.aeaLinkmanInfoList&&val.aeaLinkmanInfoList.length>0){
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
    // keyword模糊查询项目信息
    searchProjInfoByKeyword: function (queryString) {
      var _that = this;
      if (typeof (queryString) != 'undefined') queryString = queryString.trim();
      if (queryString != '') {
        _that.loadingProjInfo = true;
        request('', {
          url: ctx + 'rest/user/searchProj/projlist/'+queryString,
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
    projNameSel:function(item){
      this.projInfoId = item.projInfoId;
      this.searchProjAllInfo();
      // this.beforeCheck();
      // this.searchKeyword = item.projName+' （'+item.localCode+'）';
    },

    linkQuery:function(){
      var _that = this;
      if("null"!=_that.projInfoId && _that.projInfoId){
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
    //联动操作
    // projTypeChange:function(value){
    //   var _that=this;
    //   if(value!=null&&""!=value) {
    //     _that.querySelecTheme(value);
    //   }
    // },
    // 获取项目id 事项id
    GetRequest: function() {
      var url = location.search; //获取url中"?"符后的字串
      var theRequest = new Object();
      if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
          theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
      }
      this.projInfoId = theRequest.projInfoId;
      this.itemVerId=theRequest.itemVerId;
      return theRequest;
    },
      // 获取事项信息
      getItemBaseInfo: function(){
          var _that = this;
          _that.loading = true;
          request('', {
              url: ctx+'rest/userCenter/apply/series/getItemBaseInfo/'+_that.itemVerId,
              type: 'get',
          }, function (data) {
            _that.loading = false;
              if(data.success){
                  _that.itemBasicInfo = data.content;
              }else {
                  _that.$message({
                      message: data.message?data.message:'获取事项信息失败',
                      type: 'error'
                  });

              }
          }, function (msg) {
              _that.loading = false;
              _that.$message({
                  message: msg.message?msg.message:'获取事项信息失败',
                  type: 'error'
              });
          });
      },
    // 获取项目详细信息
    searchProjAllInfo: function(){
      var _that = this;
      _that.loading = true;
      request('', {
        url: ctx+'rest/userCenter/apply/series/baseInfo/'+_that.projInfoId+'/'+_that.itemVerId,
        type: 'get',
      }, function (data) {
        _that.loading = false;
        if(data.success){
          _that.beforeCheck();
          _that.projInfoDetail = data.content.aeaProjInfo;
          _that.jiansheFrom = data.content.aeaUnitInfos?data.content.aeaUnitInfos:[];
          _that.projUnitList = JSON.parse(JSON.stringify(data.content.aeaUnitInfos));
          //_that.itemName = data.content.itemName;
          _that.isNeedFrontCond = data.content.isNeedFrontCond;
          _that.isNeedState = data.content.isNeedState;
          _that.rootOrgId=data.content.aeaProjInfo.rootOrgId;
          _that.themeId =data.content.aeaProjInfo.themeId;
          _that.projType=data.content.aeaProjInfo.projType;
          _that.projInfoId = data.content.aeaProjInfo.projInfoId;
          _that.searchKeyword = data.content.aeaProjInfo.projName+' （'+data.content.aeaProjInfo.localCode+'）';
          _that.getApplyObjectInfo(); // 获取申报主体

          if (!_that.projInfoDetail.isAreaEstimate) _that.projInfoDetail.isAreaEstimate = '0';
          if (!_that.projInfoDetail.isDesignSolution) _that.projInfoDetail.isDesignSolution = '0';
          if (!_that.projInfoDetail.gbCodeYear) _that.projInfoDetail.gbCodeYear = '2017';
          if (!!_that.projInfoDetail.projectAddress) _that.projInfoDetail.projectAddress = _that.projInfoDetail.projectAddress.split(',');
          if (!!_that.projInfoDetail.theIndustry) _that.$refs.gbhy.setCheckedKeys(_that.projInfoDetail.theIndustry.split(','));
          if(data.content.aeaProjInfo.rootOrgId!=null&&""!=data.content.aeaProjInfo.rootOrgId) {
            _that.querySelentDistrict(data.content.aeaProjInfo.rootOrgId);
            _that.querySelectDept(data.content.aeaProjInfo.rootOrgId);
          }
          if(data.content.aeaProjInfo.themeId!=null&&""!=data.content.aeaProjInfo.themeId){
            _that.isAble=true;
          }else{
            _that.isAble=false;
          }
          _that.querySelecTheme(data.content.aeaProjInfo.projType);

        }else {
          _that.$message({
            message: data.message?data.message:'获取项目信息失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.loading = false;
        _that.$message({
          message: msg.message?msg.message:'获取项目信息失败',
          type: 'error'
        });
      });
    },
    // 移除declareStepList
    removeDeclareStepList: function(_arr,name){
      var length = _arr.length;
      for (var i = 0; i < length; i++) {
        if (_arr[i].name == name) {
          if (i == 0) {
            _arr.shift(); //删除并返回数组的第一个元素
            return _arr;
          }
          else if (i == length - 1) {
            _arr.pop();  //删除并返回数组的最后一个元素
            return _arr;
          }
          else {
            _arr.splice(i, 1); //删除下标为i的元素
            return _arr;
          }
        }
      }
    },
    // 获取项目性质项目类型
    getProjTypeNature: function () {
      var _that = this;
      request('', {
        url: ctx + 'rest/user/getDicContents',
        type: 'get',
      }, function (result) {
        if (result.content) {
          _that.projTypeList = result.content.XM_PROJECT_STEP;
          _that.projNatureList = result.content.XM_NATURE;
          _that.projLevelList=result.content.XM_PROJECT_LEVEL;
          _that.projClassList=result.content.PROJECT_CLASS;
          _that.tzlxList=result.content.XM_TZLX;
          _that.zjlyList=result.content.XM_ZJLY;
          _that.gbhyList=result.content.XM_GBHY;
          _that.tdlyList=result.content.XM_TDLY;
          _that.jsxzList=result.content.XM_NATURE;
          _that.gcflList=result.content.XM_GCFL;
          _that.xmdwlxList = result.content.XM_DWLX;
          _that.projUnitLinkmanType=result.content.PROJ_UNIT_LINKMAN_TYPE;
        }else {
          _that.$message({
            message: data.message ? data.message : '获取数据字典失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: msg.message?msg.message:'服务请求失败',
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
        data: {projInfoId: _that.projInfoId}
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
          _that.jiansheFrom.map(function(unitItem){
            Vue.set(unitItem, 'linkmanInfoId', '');
            Vue.set(unitItem, 'linkmanMail', '');
            Vue.set(unitItem, 'linkmanMobilePhone', '');
            Vue.set(unitItem, 'linkmanName', '');
            Vue.set(unitItem, 'linkmanCertNo', '');
            if(unitItem.linkmanTypes&&unitItem.linkmanTypes.length==0){
              unitItem.linkmanTypes.push(dataType2)
            }
            if(unitItem&&_that.applyObjectInfo.role==2){
              _that.getPersonOrUnitBlackByBizId(unitItem);
            }
            if(unitItem.aeaLinkmanInfoList&&unitItem.aeaLinkmanInfoList.length>0){
              unitItem.linkmanInfoId = unitItem.aeaLinkmanInfoList[0].linkmanInfoId;
              unitItem.linkmanMail = unitItem.aeaLinkmanInfoList[0].linkmanMail;
              unitItem.linkmanMobilePhone = unitItem.aeaLinkmanInfoList[0].linkmanMobilePhone;
              unitItem.linkmanName = unitItem.aeaLinkmanInfoList[0].linkmanName;
              unitItem.linkmanCertNo = unitItem.aeaLinkmanInfoList[0].linkmanCertNo;
            }
          })
          if(_that.applyObjectInfo.role == 1){
            _that.showObjectRole = true
            _that.applyObjectInfo.role = '2';
          }
          if(_that.applyObjectInfo.aeaLinkmanInfo==null){
            _that.applyObjectInfo.aeaLinkmanInfo={
              linkmanName: '',
              linkmanCertNo: '',
              linkmanMobilePhone: '',
            }
          }else {
            _that.userInfoId=_that.applyObjectInfo.aeaLinkmanInfo.linkmanInfoId;
            _that.userLinkmanCertNo = _that.applyObjectInfo.aeaLinkmanInfo.linkmanCertNo;
          };
          if(_that.applyObjectInfo.aeaUnitInfo==null){
            _that.applyObjectInfo.aeaUnitInfo={
              applicant: '',
              idcard: '',
            }
          }else {
            _that.unitInfoId = _that.applyObjectInfo.aeaUnitInfo.unitInfoId;
            _that.unifiedSocialCreditCode = _that.applyObjectInfo.aeaUnitInfo.unifiedSocialCreditCode;
            if(_that.applyObjectInfo.aeaUnitInfo.linkmanTypes&&_that.applyObjectInfo.aeaUnitInfo.linkmanTypes.length==0){
              _that.applyObjectInfo.aeaUnitInfo.linkmanTypes.push(dataType3)
            }
          };
          if(result.content.linkmanInfoList&&result.content.linkmanInfoList.length>0){
            _that.linkmanInfoList = result.content.linkmanInfoList;
            _that.applyObjectInfo.aeaUnitInfo.linkmanInfoId = result.content.linkmanInfoList[0].linkmanInfoId;
            _that.applyObjectInfo.aeaUnitInfo.linkmanMail = result.content.linkmanInfoList[0].linkmanMail;
            _that.applyObjectInfo.aeaUnitInfo.linkmanMobilePhone = result.content.linkmanInfoList[0].linkmanMobilePhone;
            _that.applyObjectInfo.aeaUnitInfo.linkmanName = result.content.linkmanInfoList[0].linkmanName;
            _that.applyObjectInfo.aeaUnitInfo.linkmanCertNo = result.content.linkmanInfoList[0].linkmanCertNo;
            var selFirstMan = result.content.linkmanInfoList[0];
            _that.queryGetResultMan(selFirstMan)
            _that.queryUnitLinkMan(selFirstMan,_that.applyObjectInfo.aeaUnitInfo);
          }else {
            _that.queryGetResultMan(_that.applyObjectInfo.aeaLinkmanInfo)
          }
          if(result.content.aeaUnitList&&result.content.aeaUnitList.length>0){
            _that.aeaUnitList = result.content.aeaUnitList;
            _that.querySelentUnit(_that.aeaUnitList[0]);
          }
          _that.getPersonOrUnitBlackByBizId();
        }else {
          _that.$message({
            message: result.message?result.message:'获取申报主体信息失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: msg.message?msg.message:'获取申报主体信息失败',
          type: 'error'
        });
      });
    },

    // 获取联系人输入建议
    querySuggesResultMan: function (queryString, cb) {

      var linkmanInfoList = this.linkmanInfoList;
      linkmanInfoList.map(function (item) {
        if(item){
          Vue.set(item, 'value', item.linkmanName);
        }
      })
      var results = queryString ? linkmanInfoList.filter(this.createFilter(queryString)) : linkmanInfoList;
      // 调用 callback 返回建议列表的数据
      cb(results);
      console.log(queryString);
    },
    createFilter: function(queryString) {
      return function(linkmanInfoList) {
        return (linkmanInfoList.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    // 获取办证结果领取人信息
    queryGetResultMan: function (data) {
      console.log(data);
      if(data){
        var selData = JSON.parse(JSON.stringify(data));
        this.getResultForm.addresseeName = selData.linkmanName;
        this.getResultForm.addresseePhone = selData.linkmanMobilePhone;
        this.getResultForm.addresseeIdcard = selData.linkmanCertNo;
      }else {
        this.getResultForm.addresseeName = '';
        this.getResultForm.addresseePhone = '';
        this.getResultForm.addresseeIdcard = '';
      }
    },
    // 获取企业联系人信息
    queryUnitLinkMan: function (data,pData) {
      var selData = JSON.parse(JSON.stringify(data));
      this.userInfoId = data.linkmanInfoId;
      this.userLinkmanCertNo = this.applyObjectInfo.aeaLinkmanInfo.linkmanCertNo;
      this.applyObjectInfo.aeaLinkmanInfo = selData;
      if(pData){
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
    querySelentUnit: function(data){
      var cData = JSON.parse(JSON.stringify(data))
      this.applyObjectInfo.aeaUnitInfo=cData;
      this.unitInfoId = this.applyObjectInfo.aeaUnitInfo.unitInfoId;
      this.unifiedSocialCreditCode = this.applyObjectInfo.aeaUnitInfo.unifiedSocialCreditCode;
    },
    // 切换领证模式
    changeReceiveMode: function (val) {
      if(val==0){
        this.getResultForm.smsType=2;
        this.queryProvince();
      }
    },
    // 获取省份信息
    queryProvince: function(){
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
    // 保存保存领件人、项目信息 补全信息保存并下一步
    saveProOrSmsinfo: function () {
      if(this.coreItems.length>0){
        this.showItemsDia = true;
        return false;
      }
      var msg='';
      var _that = this;
      var unitFlag = false; // 单位信息未完善
      var aeaUnitInfo = this.applyObjectInfo.aeaUnitInfo;
      var aeaLinkmanInfo = this.applyObjectInfo.aeaLinkmanInfo;
      var _linkmanTypes = [];
      if(_that.applyObjectInfo.aeaUnitInfo&&_that.applyObjectInfo.aeaUnitInfo.linkmanTypes&&_that.applyObjectInfo.aeaUnitInfo.linkmanTypes.length>0){
        _that.applyObjectInfo.aeaUnitInfo.linkmanTypes.map(function(itemType){
          if(itemType.linkmanInfoId){
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
      if(aeaUnitInfo.applicant||aeaUnitInfo.unifiedSocialCreditCode||aeaUnitInfo.idrepresentative||aeaLinkmanInfo.linkmanName||aeaLinkmanInfo.linkmanMobilePhone||aeaLinkmanInfo.linkmanCertNo){
        unitFlag = true;
      }
      if(this.applyObjectInfo.role==0){
        if(!this.userInfoId){
          msg = '请前往企业中心完善申报主体申请人信息！';
        }
      }else {
        if(!this.unitInfoId||!unitFlag){
          msg = '请前往企业中心完善申报主体企业信息！';
        }
      }
      if(this.isBlack==true){
        msg='申报主体已被纳入信用黑名单，无法申报项目！'
      }
      if(msg){
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
      var projNameValue=this.$refs.projName.value.trim();
      var localCodeValue=this.$refs.localCode.value.trim();
      var regionalism=this.$refs.regionalism.value.trim();
      if(projNameValue==null||projNameValue==""){
          return;
      }
      if(localCodeValue==null||localCodeValue==""){
          return;
      }
      if(regionalism==null||regionalism==""){
          return;
      }

      _that.getResultForm.id=_that.smsInfoId;
      _that.$refs['projInfoForm'].validate(function (valid1) {
        if (valid1) {
          if(jiansheUnitFlag) {
            _that.$refs['resultForm'].validate(function (valid) {
              if (valid) {
                _that.loading = true;
                _that.projInfoDetail.projectAddress = _that.projInfoDetail.projectAddress.join(',');
                _that.projInfoDetail.aeaUnitInfos = _that.jiansheFrom;
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
                    if (_that.isNeedFrontCond == 1) {
                      _that.declareStep = 2;
                      _that.getPrecondition();
                    } else if (_that.isNeedState == 1) {
                      // _that.declareStep = 3;
                      _that.getRootStateList();
                    } else {
                      _that.saveAndGetMats();
                    }

                    _that.smsInfoId = data.content.smsId;
                    _that.regionalism = data.content.regionalism;
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
          }else {
            _that.$message({
              message: '请完善申办主体单位信息',
              type: 'error'
            });
          }
        }else {
          _that.$message({
            message: '请完善项目/工程信息',
            type: 'error'
          });
        }
      });

    },
    // 获取前置条件
    getPrecondition: function(){
      var _that = this;
      _that.loading = true;
      request('', {
        url: ctx+'rest/userCenter/apply/series/itemState/getTreeCondByItemVerId/'+ _that.itemVerId,
        type: 'get',
      }, function (data) {
        if(data.success){
          _that.loading = false;
          _that.noNext = 0;
          data.content.map(function(item){
            if (item.muiltSelect!=null) {
              Vue.set(item, 'selectAnswerId', []);
            }else {
              Vue.set(item, 'selectAnswerId', '');
            }
          });
          _that.preconditionList = data.content;
        }else {
          _that.loading = false;
          _that.$message({
            message: data.message ? data.message : '获取前置条件失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.loading = false;
        alertMsg('', '获取前置条件失败', '关闭', 'error', true);
      });
    },

    // 选择前置条件保存并下一步
    saveThemeAndNext: function(){
      if(this.noNext>0){
        alertMsg('', '不符合申报条件', '关闭', 'error', true);
        return false;
      }
      var precondition=this.preconditionList
      var preconditionLen = precondition.length;
      for(var i=0;i<preconditionLen;i++){  // 并联情形id集合
        if(precondition[i].muiltSelect>1){
          if(precondition[i].selectAnswerId.length<precondition[i].muiltSelect){
            alertMsg('', '请完整的选择前置条件', '关闭', 'error', true);
            return true;
          }
        }else {
          if(precondition[i].selectAnswerId==''){
            alertMsg('', '请选择前置条件', '关闭', 'error', true);
            return true;
          }
        }
      }
      if(this.isNeedState==1){
        this.getRootStateList();
      }else {
        this.saveAndGetMats();
      }
    },
    // 根据事项id获取情形
    getRootStateList: function(){
      var _that = this;
      _that.loading = true;
      request('', {
        url: ctx+'rest/userCenter/apply/series/itemState/list/'+_that.itemVerId,
        type: 'get',
      }, function (data) {
        if(data.success){
          _that.loading = false;
          _that.declareStep = 3;
          _that.stateList = data.content;
          _that.stateList.map(function(item,ind){ // 情形下插入对应的情形
            if(item.answerType!='s'&&item.answerType!='y'){
              Vue.set(item, 'selValue', []);
              item.selectAnswerId=item.selValue;
            }
          });
        }else {
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
    },
    // 根据前置条件获取子前置条件
    getPreconditionClidrenList: function(pData,data,index,checkFlag){
      var _that = this;
      var _condId = data.itemCondId;
      var _parentCondId = data.parentCondId; // 父id
      var cPreconditionList = _that.preconditionList;
      console.log(data);
      var cPreconditionIds = _that.getCoreItemsStatusListId(cPreconditionList)
      if(checkFlag==false){
        var selPreconditionLen = cPreconditionList.length;
        for (var i = 0;i<selPreconditionLen;i++) { // 清空情形下所对应情形
          var obj = cPreconditionList[i];
          if (obj && (obj.condSeq.indexOf(_condId) >-1 )) {
            cPreconditionList.splice(i, 1);
            i--
          }
        }
        if(data.sfzz==1){
          _that.noNext--;
        }
        return false
      }

      if(data.sfzz==1&&cPreconditionIds.indexOf(_condId)==-1){
        _that.noNext++;
      }
      request('', {
        url: ctx+'rest/userCenter/apply/series/itemState/getChildAeaItemCondListByCondId/'+_condId,
        data: {itemStateId: _condId},
        type: 'get'
      }, function (data) {
        if(data.success){
          if(checkFlag==true){
            data.content.map(function(item,ind){ // 情形下插入对应的情形
              if(item.muiltSelect!=null){
                Vue.set(item, 'selectAnswerId', []);
              }else {
                Vue.set(item, 'selectAnswerId', '');
              }
              cPreconditionList.splice((index+1+ind),0,item);
            });
          }else {
            var stateListLen = cPreconditionList.length;
            for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
              var obj = cPreconditionList[i];
              if (obj && (obj.condSeq.indexOf(_condId) >-1 )) {
              // if (obj && (obj.parentCondId == _condId) || (obj && obj.parentCondId != null && (selPreconditionList.indexOf(obj.parentCondId) == -1))) {
                cPreconditionList.splice(i, 1);
                i--
              }
            }
            data.content.map(function (item, ind) { // 情形下插入对应的情形
              if (item.muiltSelect!=null) {
                Vue.set(item, 'selectAnswerId', []);
              }else {
                Vue.set(item, 'selectAnswerId', '');
              }
              cPreconditionList.splice((index + 1 + ind), 0, item);
            });
          }
        }else {
          _that.$message({
            message: '获取子情形失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: '获取子情形失败',
          type: 'error'
        });
      });
    },
    // 根据情形获取子情形
    getCoreStateBystatus: function(pData,data,index,checkFlag){
      var parentStateId = data.parentStateId;
      var _that = this;
      var questionStateId = pData.itemStateId;
      var cStateList = _that.stateList
      var _itemVerId = data.itemVerId;
      var _parentId = data.itemStateId?data.itemStateId:'ROOT';
      var selStateIds = [];
      if(checkFlag==false){
        var stateListLen = cStateList.length;
        selStateIds=_that.getCoreItemsStatusListId(cStateList);
        for (var i = 0;i<stateListLen;i++) { // 清空情形下所对应情形
          var obj = cStateList[i];
          // if(obj&&obj.stateSeq.indexOf(_parentId)>-1||(obj&&obj.parentStateId!=null&&(selStateIds.indexOf(obj.parentStateId)==-1))){
          if(obj&&(obj.parentStateId == _parentId)||(obj&&obj.parentStateId!=null&&(selStateIds.indexOf(obj.parentStateId)==-1))){
            cStateList.splice(i, 1);
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
            i--
          }
        }
        return false
      }
      request('', {
        url: ctx+'rest/apply/common/itemState/findByParentItemStateId/'+_parentId+'/'+_itemVerId,
        data: {itemStateId: _parentId},
        type: 'get'
      }, function (data) {
        if(data.success){
          if(checkFlag==true){
            data.content.map(function(item,ind){ // 情形下插入对应的情形
              if(item.answerType!='s'&&item.answerType!='y'){
                Vue.set(item, 'selValue', []);
                item.selectAnswerId=item.selValue;
              }
              cStateList.splice((index+1+ind),0,item);
            });
          }else {
            var stateListLen = cStateList.length;
            selStateIds = _that.getCoreItemsStatusListId(cStateList);
            for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
              var obj = cStateList[i];
              // if(obj&&obj.stateSeq.indexOf(_parentId)>-1|| (obj && obj.parentStateId != null && (selStateIds.indexOf(obj.parentStateId) == -1))){
              if(obj&&(obj.parentQuestionStateId == questionStateId)||(obj&&obj.parentStateId!=null&&(selStateIds.indexOf(obj.parentStateId)==-1))){
                cStateList.splice(i, 1);
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
        }else {
          _that.$message({
            message: '获取子情形失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: '获取子情形失败',
          type: 'error'
        });
      });
    },
    // 获取单事项情形列表id
    getCoreItemsStatusListId: function(cStateList){
      var stateListLen = cStateList.length;
      var selStateIds = [];
      for(var i=0;i<stateListLen;i++){  // 已选并联情形id集合
        if(cStateList[i].selectAnswerId!==undefined||cStateList[i].selectAnswerId!==null){
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
    // 上一页
    prevPage: function (page) {

      if(this.isNeedFrontCond!=1&&page==2){
        this.declareStep = 1;
      }else if(this.isNeedState!=1&&page==3){
        if(this.isNeedFrontCond!=1){
          this.declareStep = 1;
        }else {
          this.declareStep = 2;
        }
      }else {
        this.declareStep = page;
      }
    },
    // 保存并根据阶段ID、情形ID集合、事项版本ID集合获取材料一单清列表数据
    saveAndGetMats: function(){
      var _that = this;
      var stateSel = _that.stateList;
      var stateSelLen = stateSel.length;
      _that.stateIds = [];
      for(var i=0;i<stateSelLen;i++){  // 并联情形id集合
        if(stateSel[i].selectAnswerId==null||stateSel[i].selectAnswerId==''){
          if(stateSel[i].mustAnswer==1){
            _that.stateIds=[];
            alertMsg('', '请选择情形', '关闭', 'error', true);
            return true;
          }
        }
        if(stateSel[i].selectAnswerId!==null||stateSel[i].selectAnswerId!==''){
          if(typeof stateSel[i].selectAnswerId == 'object'){
            _that.stateIds = _that.stateIds.concat(stateSel[i].selectAnswerId);
          }else {
            _that.stateIds.push(stateSel[i].selectAnswerId);

          }
        }
      }
      var parmas={
        itemVerId: _that.itemVerId,
        stateIds: _that.stateIds,
      };
      _that.loading=true;
      request('', {
        url: ctx + 'rest/userCenter/apply/series/mat/list',
        type: 'post',
        ContentType: 'application/json',
        data: JSON.stringify(parmas)
      }, function (result) {
        _that.declareStep = 4;
        _that.showFileListKey = [];
        _that.allMatsTableData = result.content;
        _that.showMatsTableData = result.content.slice(0,10);
        _that.loading=false;
      }, function (msg) {
        _that.loading=false;
      })
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
    myUploadFile: function(file){
      var _that = this;
      _that.checkedSelFlie.map(function(item){
        if(item == file.file.name){
          _that.fileData.append('file', file.file);
        }
      });
    },
    // 一键分拣全选
    handleCheckAllChange: function(val) {
      this.checkedSelFlie = val ? this.AllFileListId : [];
    },
    // 一键分拣勾选文件
    handleCheckedCitiesChange: function(value) {
      var checkedCount = value.length;
      this.checkAll = checkedCount === this.AllFileListId.length;
      this.checkedSelFlie = value;
    },
    // 获取材料id 材料实例id
    getMatIdAndMatinstId: function(){
      var _that = this;
      _that.matIds=[];
      _that.matinstIds=[];
      _that.attIsRequireFlag = true;
      _that.allMatsTableData.map(function(item){
        if(item.childs=='undefined'||item.childs==undefined){
          Vue.set(item,'childs',[]);
        }
        if(item.matinstId=='undefined'||item.matinstId==undefined){
          Vue.set(item,'matinstId','');
        }
        if(item.zcqy==0&&item.attIsRequire==1){
          console.log(item.matinstId);
          if(!item.matinstId||item.childs.length==0){
            _that.attIsRequireFlag = false;
          }
        }
        _that.matIds.push(item.matId);
        if( typeof item.matinstId !== "undefined"&&item.matinstId!=''){
          _that.matinstIds.push(item.matinstId)
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
      if(_that.checkedSelFlie.length==0){
        alertMsg('', '请选择需要自动分拣的文件', '关闭', 'warning', true);
        return false;
      }else {
        _that.loadingFile = true;
        axios.post(ctx+'rest/apply/common/att/upload/auto',_that.fileData).then(function(res){
          _that.checkedSelFlie = [];
          _that.AllFileList = [];
          _that.loadingFile = false;
          _that.$refs.uploadFile.clearFiles();
          _that.checkAll = true;
          var matinstIdsObj = [];
          res.data.content.map(function(item){
            _that.showFileListKey.push(item.matId);
            _that.allMatsTableData.map(function(matItem){
              if(item.matId == matItem.matId){
                matItem.matinstId = item.matinstId;
                matItem.childs=item.fileList;
              }
            });
          });
        }).catch(function(error){
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
    unique: function(arr,flag) {
      var result = {};
      var  finalResult=[];
      var arr=arr.reverse();
      for(var i=0;i<arr.length;i++){
        if(flag=='mats'){
          result[arr[i].matId]=arr[i];
        }else if(flag=='stage'){
          result[arr[i].itemVerId]=arr[i];
        }else if(flag=='file'){
          result[arr[i].name]=arr[i];
        }
      }
      for(item in result){
        finalResult.push(result[item]);
      }
      return finalResult.reverse();
    },
    // 是否展示更多材料
    toggleShowMatsMore:function(){
      this.isShowAll = !this.isShowAll;
      if(this.isShowAll===true){
        this.showMatsTableData = this.allMatsTableData
      }else {
        this.showMatsTableData = this.allMatsTableData.slice(0,10);
      }
    },
    // 删除文件  flag ==dir 我的空间选中材料
    delSelectFileCom: function(rowData,fileData,index,flag){
      var _that = this;
      var detailIds = flag=='dir'?fileData.detailId:fileData.fileId;
      request('', {
        type: 'get',
        data: {matinstId: rowData.matinstId,detailIds:detailIds}
      }, function (res) {
        if(res.success){
          _that.getFileListWin(res.content,rowData);
          // rowData.childs.splice(index,1);
          var succMsg = index?'删除成功':'移除成功'
          if(_that.myMatsList.length>0){
            _that.myMatsList.map(function(item){
              if(item.fileId==detailIds){
                item.checked = false;
              }
            });
          }
          _that.$message({
            message: succMsg,
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
    // // 预览文件
    // filePreview: function(detailId){
    //   var _that = this;
    //   var flashAttributes = '';
    //   window.open(ctx + 'rest/file/att/preview/'+detailId);
    // },
    // 获取文件后缀
    getFileType: function(fileName){
      var index1=fileName.lastIndexOf(".");
      var index2=fileName.length;
      var suffix=fileName.substring(index1+1, index2).toLowerCase();//后缀名
      return suffix;
    },
    // 预览电子件
    filePreview: function(data,flag){ // flag==pdf 查看施工图
      var detailId = data.fileId;
      var _that = this;
      var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
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
            tempwindow.location = ctx + 'rest/file/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
          },1000)
        }
      }
    },
    // 下载电子件
    downloadFile: function(detailIds){
      window.open(ctx+'rest/file/applydetail/mat/download/'+detailIds)
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
    // 上传电子件
    uploadFileCom: function(file){
      var _that = this;
      var rowData = _that.selMatRowData;
      this.fileWinData = new FormData();
      var isAllowFile;
      file.forEach(function (u){
        // if (!_that.isAllowFile(u.file.name)){
        //   _that.$message({
        //     message: '不允许上传的文件类型',
        //     type: 'error'
        //   });
        //   isAllowFile = false;
        // }
        Vue.set(u.file,'fileName',u.file.name);
        _that.fileWinData.append('file', u.file);

      })
      //if (!isAllowFile){ return }
      // Vue.set(file.file,'fileName',file.file.name);
      // this.fileWinData.append('file', file.file);
      this.fileWinData.append("matId", rowData.matId);
      this.fileWinData.append("matinstCode", rowData.matCode);
      this.fileWinData.append("matinstName", rowData.matName?rowData.matName:'');
      this.fileWinData.append("projInfoId", _that.projInfoId);
      this.fileWinData.append("matinstId", rowData.matinstId?rowData.matinstId:'');
      _that.loadingFileWin = true;
      axios.post(ctx+'rest/file/uploadFile',_that.fileWinData).then(function(res){
        if(res.data.success){
          file.forEach(function (u){
            Vue.set(u.file,'matinstId',res.data.content);
          })
          // Vue.set(file.file,'matinstId',res.data.content)
          _that.selMatinstId = res.data.content;
          _that.getFileListWin(res.data.content,rowData);
          _that.allMatsTableData.map(function(item){
            if(item.matId == rowData.matId){
              item.matinstId = res.data.content;
              _that.showFileListKey.push(item.matId)
            }
          });
          _that.loadingFileWin = false;
          _that.$message({
            message: '上传成功',
            type: 'success'
          });
        }else {
          _that.loadingFileWin = false;
          _that.$message({
            message: '上传失败',
            type: 'error'
          });
        }
      }).catch(function(error){
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
    //判断是否允许上传的文件
    isAllowFile:function(fileName){
      debugger;
      var filetypes=[".jpg",".png",".rar",".txt",".zip",".doc",".ppt",".xls",".pdf",".docx",".xlsx"];
      var fileend = fileName.substring(fileName.indexOf("."));
      for(var i =0; i<filetypes.length;i++) {
        if (filetypes[i] == fileend) {
          return true;
          break;
        }
      }
      return false
    },
    // 发起申报
    startParalleApprove: function(){
      var _that = this;
      var projInfoIds=[]; // 项目ID集合
      var applySubjectType='';
      // 申报主体为1、2
      console.log(_that.applyObjectInfo.role);
      if(_that.applyObjectInfo.role==2){
        applySubjectType=1;
      }else {
        applySubjectType=0;
      }
      if(applySubjectType==0){
        _that.unitInfoId='';
        _that.unifiedSocialCreditCode = '';
        _that.userInfoId = _that.applyObjectInfo.aeaLinkmanInfo.linkmanInfoId;
        _that.userLinkmanCertNo = _that.applyObjectInfo.aeaLinkmanInfo.linkmanCertNo;
      }else {
        _that.unitInfoId = this.applyObjectInfo.aeaUnitInfo.unitInfoId;
        _that.unifiedSocialCreditCode = _that.applyObjectInfo.aeaUnitInfo.unifiedSocialCreditCode;
      }
      projInfoIds.push(_that.projInfoId);
      _that.getMatIdAndMatinstId();
      if(!_that.attIsRequireFlag){
        alertMsg('', '请上传所有必传的电子件', '关闭', 'warning', true);
        return false;
      }
      var parmas={
        applySource: 'net',
        applySubject: applySubjectType,
        applyinstId: _that.applyinstId,
        itemVerId: _that.itemVerId,
        linkmanInfoId: _that.userInfoId,
        matinstsIds: _that.matinstIds,
        projInfoIds: projInfoIds,
        smsInfoId : _that.smsInfoId,
        regionalism:_that.regionalism,
        stateIds: _that.stateIds,
        unitInfoId: _that.unitInfoId
      }
      _that.progressDialogVisible = true;
      _that.progressIntervalStop=false;
      _that.progressStus=null;
      _that.setUploadPercentage();
      request('', {
        url: ctx+'rest/userCenter/apply/series/series/processflow/start',
        type: 'post',
        ContentType: 'application/json',
        timeout: 1000000,
        data: JSON.stringify(parmas)
      }, function (res) {
        if(res.success){
          _that.uploadPercentage=100;
          _that.progressIntervalStop=true;
          _that.progressStus='success';
          _that.progressDialogVisible = false;
          _that.declareStep = 5;
          _that.applySuccessProjInfo = res.content;
          var nowDate = new Date();
          var yearstr = nowDate.getFullYear();
          var monthstr = Number(nowDate.getMonth()) + 1;
          var datestr = nowDate.getDate();
          var hourstr = Number(nowDate.getHours());
          var minstr = Number(nowDate.getMinutes());
          var printstr = "您于" + yearstr + "年" + monthstr + "月" + datestr + "日" + hourstr + "时" + minstr + "分在唐山网上办事大厅申请的【"+_that.applySuccessProjInfo.projName+"】项目已经完成申报。";

          _that.applySuccessProjInfo.printstr=printstr;
          // _that.$nextTick(function(){
          //   alertMsg('', printstr, '确认', 'success', true,function(){
          //     window.location.href=ctx+'rest/user/toUserCenterindexPage';
          //   });
          // });
        }else {
          _that.progressIntervalStop=true;
          _that.progressStus='error';
          _that.$message({
            message: res.message?res.message:'提交申请失败！',
            type: 'error'
          });
          _that.$nextTick(function(){
            _that.progressDialogVisible = false;
          });
        }
      }, function (msg) {
        _that.progressDialogVisible = false;
        _that.progressIntervalStop=true;
        _that.progressStus='error';
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    showUploadWindow: function(data,index){ // 展示文件上传下载弹窗
      var _that = this;
      _that.showUploadWindowFlag = true;
      _that.getMyMatsList();
      _that.selMatRowData = data;
      _that.selMatRowDataInd = index;
      _that.selMatinstId = data.matinstId?data.matinstId:null;
      _that.getFileListWin(_that.selMatinstId,data);
    },
    // 切换选中的材料 flag==prev 上一个 next 下一个
    changeRowDataSel: function(flag){
      if(flag=='prev'){
        if(this.selMatRowDataInd>0){
          this.selMatRowDataInd--
        }
      }else {
        if(this.selMatRowDataInd<(this.allMatsTableData.length-1)){
          this.selMatRowDataInd++
        }
      }
      this.selMatRowData = this.allMatsTableData[this.selMatRowDataInd];
      this.selMatinstId = this.selMatRowData.matinstId?this.selMatRowData.matinstId:null;
      this.getFileListWin(this.selMatinstId,this.selMatRowData);
      console.log(this.selMatRowData);
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
    // 获取已上传文件列表
    getFileListWin: function(matinstId,rowData){
      var _that = this;
      _that.selMatLoading = true;
      request('', {
        url: ctx + 'rest/file/getAttFiles/'+matinstId,
        type: 'get',
        data: {matinstId: matinstId}
      }, function (res) {
        _that.selMatLoading = false;
        if(res.success){
          if(res.content){
            _that.uploadTableData = res.content;
            _that.uploadFilesCount = res.content.length;
            var uploadFileIds = [];
            if(_that.uploadTableData.length>0){
              _that.uploadTableData.map(function(uploadItem){
                if(uploadFileIds.indexOf(uploadItem.fileId)<0){
                  uploadFileIds.push(uploadItem.fileId);
                }
              });
            }
            if(_that.myMatsList.length>0){
              _that.myMatsList.map(function(item){
                if(typeof item.checked == undefined){
                  Vue.set(item, 'checked',false);
                }
                if(uploadFileIds.length>0&&uploadFileIds.indexOf(item.fileId)>-1){
                  item.checked = true;
                }else {
                  item.checked = false;
                }
              })
            }
            if(_that.myDirMatsList.length>0){
              _that.myDirMatsList.map(function(item){
                if(typeof item.checked == undefined){
                  Vue.set(item, 'checked',false);
                }
                if(uploadFileIds.length>0&&uploadFileIds.indexOf(item.detailId)>-1){
                  item.checked = true;
                }else {
                  item.checked = false;
                }
              })
            }
            if(_that.uploadFilesCount>0&&_that.showFileListKey.indexOf(rowData.matId)<0){
              _that.showFileListKey.push(rowData.matId);
            }
            if(rowData){
              rowData.childs=res.content;
            }
          }
        }else {
          _that.$message({
            message: res.message?res.message:'加载已上传材料列表失败',
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

    //获取行政分区列表
    querySelentDistrict: function (rootOrgId) {
      var _that=this;
      request('', {
        url: ctx + 'rest/org/region/list',
        type: 'get',
        data: {rootOrgId: rootOrgId}
      }, function (result) {
        if (result.success) {
          _that.districtList=result.content;
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },

    //获取部门组织
    querySelectDept: function (rootOrgId) {
      var _that=this;
      request('', {
        url: ctx + 'rest/user/getOrgs',
        type: 'get',
        data: {rootOrgId: rootOrgId}
      }, function (result) {
        if (result.success) {
          _that.orgList=result.content;
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },

    querySelecTheme: function (themeType) {
      var _that=this;
      request('', {
        url: ctx + 'rest/user/getThemes',
        type: 'get',
        //data: {themeType:themeType}
      }, function (result) {
        if (result.success) {
          _that.themeList=result.content;
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
    // 根据企业或者个人id查询是否属于黑名单及信用记录
    getPersonOrUnitBlackByBizId: function () {
      var _that = this;
      var _bizCode = '', _bizType = '';
      _that.applyObjectInfo.role
      if(_that.applyObjectInfo.role==0){
        _bizCode = _that.userLinkmanCertNo;
        _bizType = 'l';
      }else if(_that.applyObjectInfo.role==2) {
        _bizCode = _that.unifiedSocialCreditCode;
        _bizType = 'u';
      }
      request('', {
        url: ctx + 'rest/user/credit/redblack/getPersonOrUnitBlackAndCreditListByBizCode',
        type: 'get',
        data: {bizCode:_bizCode,bizType:_bizType}
      }, function (result) {
        if (result.success) {
          if(result.content.isBlack==1) {
            _that.creditDiaVisible = true; // 展示信用记录弹窗
            _that.isBlack = true;
          }else {
            _that.isBlack = false;
          }
          _that.loseCreditCount = 0;
          _that.creditTypeList = result.content.creditList;
          result.content.creditList.map(function(item){
            if(item.itemCode=='bad'&&item.summaries.length>0){
              _that.loseCreditCount = item.summaries.length;
            }
            item.summaries.map(function(itemSum){
              Vue.set(itemSum, 'isOpen', false);
            })
          });
          console.log(_that.creditTypeList);
        }else {
          // _that.creditDiaVisible = false; // 展示信用记录弹窗
          _that.$message.error(result.message?result.message:'获取信用状态失败！');
        }
      }, function (msg) {
        // _that.creditDiaVisible = false; // 展示信用记录弹窗
        // alertMsg('', msg.message?msg.message:'网络加载失败！', '关闭', 'error', true);
      })
    },
  },
  filters: {
    formatDate: function (time) {
      if (!time) return '-';
      var date = new Date(time);
      if (!date) return;
      return formatDate(date, 'yyyy-MM-dd hh:mm');
    }
    ,
    formatBjType: function (bjType) {
        switch (bjType) {
            case '1':
                return '个工作日';
            case '2':
                return '个自然日';
        }
        return '个工作日'
    }
  },
});

