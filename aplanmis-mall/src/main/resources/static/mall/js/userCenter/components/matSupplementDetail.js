// 节流函数
var throttle = function (method, delay, time) {
  var timeout, startTime = +new Date();
  return function () {
    var context = this,
      args = arguments,
      curTime = +new Date();
    clearTimeout(timeout);
    // 如果达到了规定的触发时间间隔，触发 handler
    if (curTime - startTime >= time) {
      method.apply(context, args);
      startTime = curTime;
    } else {
      // 没达到触发间隔，重新设定定时器
      timeout = setTimeout(method, delay);
    }
  }
}

function formatDate(date, fmt) {
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  var o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds()
  }
  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      var str = o[k] + ''
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str))
    }
  }
  return fmt
}

function padLeftZero(str) {
  return ('00' + str).substr(str.length)
}
var module = new Vue({
  el: '#matSupplementDetail',
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
    // 输入为数字 大于0
    var checkMissNum = function (rule, value, callback) {
      if (value === '' || value === undefined || value === null || value.toString().trim() === '') {
        callback(new Error('必填！'));
      } else if (value) {
        var flag = !/^[0-9]\d*$/.test(value) && !(/^[0-9]\d*$/.test(value));
        if (flag) {
          return callback(new Error('格式错误'));
        } else {
          if (parseInt(value) <= 0) {
            return callback(new Error('不能为0'));
          } else {
            callback();
          }
        }
      } else {
        // callback();
        if (parseInt(value) <= 0) {
          return callback(new Error('不能为0'));
        } else {
          callback();
        }
      }
    };
    // 输入为数字 大于0 ===>使用在材料的电子件当中
    var checkMissNumFormat = function (rule, value, callback) {
      if (value === '' || value === undefined || value === null || value.toString().trim() === '') {
        callback(new Error('请上传！'));
      } else if (value) {
        var flag = !/^[0-9]\d*$/.test(value) && !(/^[0-9]\d*$/.test(value));
        if (flag) {
          return callback(new Error('格式错误'));
        } else {
          if (parseInt(value) <= 0) {
            return callback(new Error('请上传！'));
          } else {
            callback();
          }
        }
      } else {
        // callback();
        if (parseInt(value) <= 0) {
          return callback(new Error('请上传！'));
        } else {
          callback();
        }
      }
    };
    var checkPhoneNum = function (rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('必填！'));
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
      ctx: ctx,
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight), //当前屏幕高度
      loading: false, // 全屏loading
      loadingFile: false, // 文件上传loading
      loadingFileWin: false, // 窗口文件上传loading
      // 全局loading
      mloading: false,
      correctId: '',
      activeNames: ['1', '2', '3', '4', '5', '6'], // el-collapse 默认展开列表
      verticalTabData: [ // 左侧纵向导航数据
        {
          label: '基本信息',
          target: 'baseInfo'
        }, {
          label: '材料补正',
          target: 'materialSupplement'
        },
      ],
      // 材料列表
      materialListData: [],
      // 列表总数
      total: 0,
      // 列表查询
      listSearchData: {
        pageNum: 1,
        pageSize: 10,
        userId: '',
        keyword: '',
      },
      isProjDetail: false,

      activeTab: 0, // 纵向导航active状态index

      // 获取到的页面所有信息
      allPageInfoData: {},

      // 基本信息和窗口信息-form
      supplemetForm: {
        applyinstCode: "",
        approveOrgName: "",
        iteminstName: "",
        localCode: "",
        owner: "",
        projInfoName: "",
        correctDueDays: '', //补正天数
        correctMemo: '',
        correctDueTime: '', //补正期限
        chargeOrgName: '',

        windowUserName: '', //窗口负责人姓名
        opsUserName: '', //经办人姓名
        opsTime: '', //经办日期
        correctUserName: '', //补正结束人
        correctEndTime: '', //补正结束日期
        printUserName: '', //打印人
        printTime: '', //打印日期
        regionName: '', //行政名称

      },
      // 基本信息和窗口信息-form校验
      supplementFormRules: {
        correctDueDays: [{
          required: true,
          trigger: blur,
          message: '请输入补正办理时限'
        }]
      },

      // 材料列表-form
      materialListForm: {
        // 表格数据
        materialList: [],
        // 表格数据的校验
        rules: {
          realPaperCount: {
            validator: checkMissNum,
            required: true,
            trigger: ['blur']
          },
          realCopyCount: {
            validator: checkMissNum,
            required: true,
            trigger: ['blur']
          },
          attCount: {
            validator: checkMissNumFormat,
            required: true,
            trigger: ['blur', 'change']
          },
        },
        // 材料补正的备注
        materialSupplementRemark: "",
        // 窗口信息的补正意见
        materialSupplementDesc: "",
      },
      // 材料列表-当前操作行
      curHandelRow: {},
      // 材料列表上传的url (此处为材料补正上传url)
      uploadActionUrl: ctx + 'rest/user/att/upload',
      // 当前材料的附件列表的展示dialog
      fileListDialog: false,
      // 当前材料的附件列表数据
      enclosureListForMaterial: [],
      // 当前材料的附件列表-批量中选中的数据
      enclosureListForMaterialSelections: [],

      // 是否展示文件上传dialog
      showUploadWindowFlag: false,
      // 文件上传dialog-的文件列表数据
      enclosureFileListArr: [],
      filePreviewCount: 0,
      // 当前的事项实例
      row: null,
      noDataTip: '暂无数据',
      selMatRowData: {}, // 所选择的材料信息
      selMatRowDataInd: 0, // 所选择的材料的index
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
      formUrlList: [], // 一张表单list
      formItemsIdStr: '',
      stageFrontCheckFlag: true, // 阶段前置检测是否通过
      stageFrontCheckMsg: '', // 阶段前置检测失败提示
      leftTabClosed: true, //
      selThemeDialogShow: false, // 是否展示
      fileList: [],
      showFileListKey: [], // 展示材料下文件列表
      allMatsTableData: [], // 材料列表
      showMatsTableData: [], // 可展示的材料列表
      uploadTableData: [],
      progressDialogVisible: false, // 是否显示进度条loading
      uploadPercentage: 0, // 进度条百分比
      progressStus: null, // 进度条状态
      selAttRealIninstId: '', //选择电子件id
      projInfoId: '', // 查询项目id
      linkmanInfoList: [], // 联系人列表
      aeaUnitList: [], // 单位列表
      itemVerIds: [], //事项版本ID
      parallelApplyinstId: '', //申报实例ID

    }
  },
  mounted: function () {
    var _that = this;
    window.addEventListener('scroll', _that.handleScroll);
    window.addEventListener('resize', throttle(function (ev) {
      _that.navLeft()
    }, 100, 600));
    _that.navLeft()
    _that.getMaterialList();
    _that.fetchPageInfo();
    _that.correctId = pager.correctId;  //correctId赋值
  },
  watch: {

  },
  methods: {
    // 页面resize
    navLeft: function () {
      var _windW = $(window).width();
      // console.log(_windW)
      setTimeout(function () {
        if (_windW < 1240) {
          $('.right-handel-pandel').addClass('box-shadow-pandel')
        } else {
          $('.right-handel-pandel').removeClass('box-shadow-pandel')
        }
      }, 100)

    },
    // 公共方法
    // 获取url中的查询参数
    getSerachParamsForUrl: function (name) {
      var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      }
      return null;
    },
    // ui方法
    apiMessage: function (msg, type) {
      this.$message({
        message: msg,
        type: type
      })
    },
    // 页面导航操作方法
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

    // 材料补全 --> 材料补全列表查询接口
    getMaterialList: function () {
      var ts = this;
      ts.mloading = true;
      request('', {
        url: ctx + 'rest/user/matSupply/list',
        type: 'get',
        data: ts.listSearchData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.materialListData = res.content.list;
          ts.total = res.content.total;
        } else {
          ts.$message.error(res.message)
        }
      }, function () {
        ts.mloading = false;
        ts.$message.error('网络错误！')
      });
    },

    // 输入关键词查询
    searchMatSupplementList: function () {
      this.getMaterialList();
    },

    handleSizeChange: function (val) {
      this.listSearchData.pageSize = val;
      this.getSearchProjList()
    },
    // 页面页码切换
    handleCurrentChange: function (val) {
      this.listSearchData.pageNum = val;
      this.getSearchProjList()
    },

    // 获取页面信息
    fetchPageInfo: function () {
      var row = pager.row;
      var ts = this,
        _getData = {};
      ts.row = row;
      _getData.correctId = row.correctId || pager.correctId;
      ts.correctId = row.correctId;
      ts.mloading = true;
      ts.isProjDetail = true;
      request('', {
        url: ctx + 'rest/user/getMatCorrectInfo',
        type: 'get',
        data: _getData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.allPageInfoData = res.content;
          // test
          // ts.allPageInfoData.correctState = 6;
          ts.materialListForm.materialList = res.content.matCorrectDtos;
          ts.materialListForm.materialSupplementRemark = res.content.correctMemo;
          ts.materialListForm.materialSupplementDesc = res.content.correctDesc;
          ts.allMatsTableData = res.content.matCorrectDtos;
          ts.projInfoId = res.content.projInfoId;
          ts.parallelApplyinstId = res.content.applyinstId;
          ts.allApplySubjectInfo = res.content.unitInfos;
          ts.itemVerIds = res.content.itemVerIds;
          ts.handelBaseInfo();
          // 重新获取页面数据后移除form的校验显示
          ts.$refs['materialListForm'].clearValidate();
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function (msg) {
        ts.mloading = false;
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },

    // 处理基础信息
    handelBaseInfo: function () {
      var _allData = this.allPageInfoData;
      for (var k in this.supplemetForm) {
        if (_allData[k]) {
          this.supplemetForm[k] = _allData[k];
        } else {
          this.supplemetForm[k] = "";
        }
      }
    },

    // 电子件相关
    // 当前操作行
    handelRow: function (row) {
      this.curHandelRow = row;
      // console.log(row)
    },
    // 文件校验-格式不对false,否则为true
    fileCheck: function (file) {
      var ts = this,
        file = file;
      var fileMaxSize = 1024 * 1024 * 10; // 10MB为最大限制
      // 文件类型
      // 检查文件类型
      var index = file.name.lastIndexOf(".");
      var ext = file.name.substr(index + 1);
      if (['exe', 'sh', 'bat', 'com', 'dll'].indexOf(ext) !== -1) {
        ts.$message({
          message: '请上传非.exe,.sh,.bat,.com,.dll文件',
          type: 'warning'
        });
        return false;
      };
      // 检查文件大小
      if (file.size > fileMaxSize) {
        ts.$message({
          message: '请上传大小在10M以内的文件',
          type: 'warning'
        });
        return false;
      };
      return true;
    },
    // 文件改动，只选择过程
    fileSelectChange: function (file, fileList) {
      // var _typeSure = true,
      //   ts = this;
      // for (var i = 0; i < fileList.length; i++) {
      //   if (!ts.fileCheck(fileList[i])) {
      //     _typeSure = false;
      //     break;
      //   }
      // }
      // if (_typeSure) {
      //   console.log('文件格式都正确')
      // }

    },
    // 文件上传前
    fileBeforeUpload: function (file) {
      var _typeSure = this.fileCheck(file);
      if (_typeSure) {
        // console.log('文件格式正确')
      }
      return _typeSure;
    },
    // 文件上传成功
    fileUploadSuccess: function (res, file, fileList) {
      if (res.success) {
        this.apiMessage('上传成功！', 'success')
        this.fetchPageInfo();
      } else {
        this.apiMessage(res.message, 'error')
      }
    },

    // 查看材料的电子件的附件列表
    showFileListDialog: function (row) {
      var ts = this,
        _getData = {};
      ts.curHandelRow = row;
      ts.fileListDialog = true;
      _getData = {};
      _getData.attRealIninstId = ts.curHandelRow.attRealIninstId;
      ts.mloading = true;
      request('', {
        url: ctx + 'rest/user/att/list',
        type: 'get',
        data: _getData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.enclosureListForMaterial = res.content;
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function (msg) {
        ts.mloading = false;
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    // 材料的电子件列表中的批量选中
    handleSelectionChangeForMaterial: function (list) {
      this.enclosureListForMaterialSelections = list;
    },
    // 材料的电子件列表中的删除-id存在即删除单个，否则为批量
    delFileForMaterial: function (id) {
      var ts = this,
        _delids = [];
      if (id !== 'batch') {
        _delids = id;
      } else {
        var _allDelList = ts.enclosureListForMaterialSelections
        if (!_allDelList.length) {
          return ts.apiMessage('您尚未选中需要删除的文件！', 'info')
        }
        _allDelList.forEach(function (item) {
          _delids.push(item.fileId)
        })
      }
      // console.log(_delids.toString())
      // return
      confirmMsg('', '您确定删除选中附件吗？', function () {
        ts.mloading = true;
        request('', {
          url: ctx + 'rest/user/att/delelte',
          type: 'post',
          data: {
            detailIds: _delids.toString(),
            attRealIninstId: ts.curHandelRow.attRealIninstId
          }
        }, function (res) {
          ts.mloading = false;
          if (res.success) {
            ts.enclosureListForMaterial = res.content;
            ts.showFileListDialog(ts.curHandelRow); //删除后重新调用获取文件列表的接口
          } else {
            return ts.apiMessage(res.message, 'error')
          }
        }, function (msg) {
          ts.mloading = false;
          alertMsg('', '服务请求失败', '关闭', 'error', true);
        });
      });
    },
    // // 文件预览
    // previewFile: function (id) {
    //     if (!id) return;
    //     if (window.frames.length != parent.frames.length) {
    //         window.parent.open(ctx + 'rest/file/att/preview/' + id);
    //     } else {
    //         window.open(ctx + 'rest/file/att/preview/' + id);
    //     }
    //
    // },
    // 获取文件后缀
    getFileType: function (fileName) {
      var index1 = fileName.lastIndexOf(".");
      var index2 = fileName.length;
      var suffix = fileName.substring(index1 + 1, index2).toLowerCase(); //后缀名
      return suffix;
    },
    // 预览电子件
    previewFile: function (data, flag) { // flag==pdf 查看施工图
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
          _that.mloading = true;
          request('', {
            url: ctx + 'previewPdf/pdfIsCoverted?detailId=' + detailId,
            type: 'get',
          }, function (result) {
            if (result.success) {
              _that.mloading = false;
              var tempwindow = window.open(); // 先打开页面
              setTimeout(function () {
                tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
              }, 1000)
            } else {
              if (_that.filePreviewCount > 9) {
                confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                  _that.filePreviewCount = 0;
                  _that.previewFile(data);
                }, function () {
                  _that.filePreviewCount = 0;
                  _that.mloading = false;
                  return false;
                }, '确定', '取消', 'warning', true)
              } else {
                setTimeout(function () {
                  _that.previewFile(data);
                }, 1000)
              }
            }
          }, function (msg) {
            _that.mloading = false;
            _that.$message({
              message: '文件预览失败',
              type: 'error'
            });
          })
        } else {
          _that.mloading = false;
          var tempwindow = window.open(); // 先打开页面
          setTimeout(function () {
            tempwindow.location = ctx + 'rest/file/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
          }, 1000)
        }
      }
    },
    // 补正
    saveMaterialList: function (state) {
      var ts = this;
      if (state == 6) {
        ts.ajaxMaterialList(state);
      } else {
        this.$refs['materialListForm'].validate(function (valid, obj) {
          if (valid) {
            console.log(ts.materialListForm.materialList)
            var _alllist = ts.materialListForm.materialList,
              _canSave = true;
            if(ts.allPageInfoData.correctState == 6){
              for(var i = 0; i<_alllist.length; i++){
                if(_alllist[i].paperDueIninstId && _alllist[i].realPaperCount<=0 || 
                  _alllist[i].copyDueIninstId && _alllist[i].realCopyCount<=0 ||
                  _alllist[i].attDueIninstId && _alllist[i].attCount<=0){
                    _canSave = false;
                    break;
                  }
              }
            }
            if(!_canSave){
              return ts.apiMessage('原件、复印件请前往政务办事大厅窗口办理。目前可【暂存】', 'warning');
            }
            ts.ajaxMaterialList(state);
          } else {   
            return ts.apiMessage('原件、复印件请前往政务办事大厅窗口办理。目前可【暂存】', 'warning');
          }
        })
      }
    },
    ajaxMaterialList: function (state) {
      var ts = this;
      var _saveData = {};
      _saveData.correctId = ts.correctId || ts.getSerachParamsForUrl('correctId');
      _saveData.correctState = state;
      _saveData.correctDesc = ts.materialListForm.materialSupplementDesc;
      _saveData.correctMemo = ts.materialListForm.materialSupplementRemark;
      _saveData.matCorrectDtosJson = JSON.stringify(ts.materialListForm.materialList);
      // console.log(_saveData)
      // return;
      ts.mloading = true;
      request('', {
        url: ctx + 'rest/user/saveMatCorrectInfo',
        type: 'post',
        data: _saveData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          return ts.apiMessage('保存材料补正实例成功！', 'success');
          setTimeout(function () {
            ts.fetchPageInfo();
          }, 1500)
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function (msg) {
        ts.mloading = false;
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },

    // 文件上传dialog-打开，每次都先清掉文件暂存的数据数据
    showUploadWindow: function (data, index, flag) {
      debugger;
      var _that = this;
      this.curHandelRow = data;
      this.selMatRowData = data;
      debugger;
      this.selMatRowDataInd = index;
      _that.selAttRealIninstId = data.attRealIninstId ? data.attRealIninstId : null;
      this.enclosureFileListArr = [];
      if (typeof _that.selMatRowData.childs == 'undefined' || _that.selMatRowData.childs == undefined) {
        Vue.set(_that.selMatRowData, 'childs', []);
      }
      if (typeof _that.selMatRowData.certChild == 'undefined' || _that.selMatRowData.certChild == undefined) {
        Vue.set(_that.selMatRowData, 'certChild', []);
      }
      if (flag == 'certList') {
        _that.showCertWindowFlag = true;
        _that.getCertFileListWin(data);
      } else {
        _that.showUploadWindowFlag = true;
        _that.getMyMatsList();
        _that.getFileListWin(_that.selAttRealIninstId, data);
      }
    },
    // 获取证照文件列表
    getCertFileListWin: function (matData, flag) { // flag==oneUnit 查询单个建设单位
      var _that = this,
        _identityNumber = '';
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
        _identityNumber = matData.unifiedSocialCreditCode;
      } else {
        var identityNumbers = [];
        _that.allApplySubjectInfo.map(function (unitItem) {
          identityNumbers.push(unitItem.unifiedSocialCreditCode);
        });
        _identityNumber = identityNumbers.join(',');
      }
      request('', {
        url: ctx + 'aea/cert/getLicenseAuthRes',
        type: 'get',
        data: {
          identityNumber: _identityNumber,
          itemVerIds: _that.selMatRowData.itemVerId
        }
      }, function (res) {
        if (res) {
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
    // close文件上传dialog
    closeUploadDialog: function () {
      this.$refs.enclosureFileUploadRef.clearFiles()
      this.fetchPageInfo();
    },
    // 附件相关
    // 附件上传--before
    enclosureBeforeUpload: function (file) {
      var ts = this,
        file = file;
      var fileMaxSize = 1024 * 1024 * 10; // 10MB为最大限制
      // 文件类型
      // 检查文件类型
      var index = file.name.lastIndexOf(".");
      var ext = file.name.substr(index + 1);
      if (['exe', 'sh', 'bat', 'com', 'dll'].indexOf(ext) !== -1) {
        ts.$message({
          message: '请上传非.exe,.sh,.bat,.com,.dll文件',
        });
        return false;
      };
      // 检查文件大小
      if (file.size > fileMaxSize) {
        ts.$message({
          message: '请上传大小在10M以内的文件',
        });
        return false;
      };
      return true;
    },
    // 附件上传文件列表变动
    enclosureFileSelectChange: function (file, fileList) {
      var ts = this;
      ts.enclosureFileListArr = [];
      // 选择后检验
      if (!ts.enclosureBeforeUpload(file)) {
        var fileIndex = fileList.indexOf(file.name);
        fileList.splice(fileIndex, 1);
      }
      fileList.forEach(function (item) {
        ts.enclosureFileListArr.push(item.raw)
      })

    },
    // 附件上传文件列表移除某一项
    enclosureFileSelectRemove: function (file, fileList) {
      var ts = this;
      ts.enclosureFileListArr = [];
      // 选择后检验
      if (!ts.enclosureBeforeUpload(file)) {
        var fileIndex = fileList.indexOf(file.name);
        fileList.splice(fileIndex, 1);
      }
      fileList.forEach(function (item) {
        ts.enclosureFileListArr.push(item.raw)
      })
    },
    // 附件上传dialog确定上传
    fileUploadAndSaveFile: function () {
      var ts = this;
      // console.log(ts.enclosureFileListArr)
      if (ts.enclosureFileListArr.length <= 0) {
        return ts.apiMessage('您尚未选择文件，请先选择后再上传！', 'info');
      }
      var fileFormData = new FormData();
      for (var i = 0; i < ts.enclosureFileListArr.length; i++) {
        fileFormData.append('file', ts.enclosureFileListArr[i]);
      }
      fileFormData.append('attRealIninstId', ts.curHandelRow.attRealIninstId);
      // debugger
      ts.mloading = true;
      $.ajax({
        url: ts.uploadActionUrl,
        type: 'post',
        data: fileFormData,
        contentType: false,
        processData: false,
        success: function (res) {
          ts.mloading = false;
          if (res.success) {
            ts.fileUploadDialogState = false;
            // 保存成功后将需要上传的附件文件列表清空
            ts.enclosureFileListArr = [];
            ts.fetchPageInfo(ts.row);
            ts.apiMessage('上传成功！', 'success');
          } else {
            ts.apiMessage(res.message, 'error')
          }
        },
        error: function (msg) {
          ts.mloading = false;
          ts.apiMessage('网络错误！', 'errorF')
        },
      })
    },



    /*************************************云盘和我的材料库新增方法***************************************************/
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
    // 文件上传获取我的云盘列表
    getMyDirsList: function (row, treeNode, resolve) {
      var _that = this;
      var dirId = row ? row.dirId : 'root';
      _that.mySpaceLoading = true;
      request('', {
        url: ctx + 'rest/cloud/dirAndAtt/list/' + dirId,
        type: 'get',
        data: {
          dirId: dirId
        }
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
    // 切换我的材料选中状态 flag ==dir 我的空间选中材料
    changeMyMatSel: function (val, selMatData, flag) {
      console.log(val, selMatData);
      console.log(this.selMatRowData);
      var _that = this;
      var fileId = flag == 'dir' ? selMatData.detailId : selMatData.fileId;
      var param = {
        detailIds: fileId,
        attRealIninstId: _that.selMatRowData.attRealIninstId ? _that.selMatRowData.attRealIninstId : ''
      };
      if (val) {
        request('', {
          url: ctx + 'rest/user/att/uploadFileByCloud',
          type: 'post',
          data: param
        }, function (result) {
          if (result) {
            console.log(result);
            _that.selMatRowData.attRealIninstId = result.content;
            _that.getFileListWin(result.content, _that.selMatRowData);
          }
        }, function (msg) {});
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
        data: {
          dirId: dirId
        }
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
    // 获取已上传文件列表
    getFileListWin: function (attRealIninstId, rowData) {
      var _that = this;
      _that.selMatLoading = true;
      request('', {
        url: ctx + 'rest/user/att/list',
        type: 'get',
        data: {
          attRealIninstId: attRealIninstId
        }
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
    // 判断文件类型是否存在规定的类型里
    isSpecifiedFileType: function (fileName) {
      var fileTypes = [".jpg", ".png", ".rar", ".txt", ".zip", ".doc", ".ppt", ".xls", ".pdf", ".docx", ".xlsx", ".dwg"];
      var getFileType = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
      if (fileTypes.indexOf(getFileType) > -1) {
        return true;
      } else {
        return false;
      }
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
      this.fileWinData.append("attRealIninstId", rowData.attRealIninstId ? rowData.attRealIninstId : '');

      _that.loadingFileWin = true;
      axios.post(ctx + 'rest/user/att/upload', _that.fileWinData).then(function (res) {
        if (res.data.success) {
          file.forEach(function (u) {
            Vue.set(u.file, 'attRealIninstId', rowData.attRealIninstId);
          })
          // Vue.set(file.file,'matinstId',res.data.content)
          _that.selAttRealIninstId = rowData.attRealIninstId;
          _that.getFileListWin(rowData.attRealIninstId, rowData);
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
    // 删除文件 flag ==dir 我的空间选中材料
    delSelectFileCom: function (rowData, fileData, index, flag) {
      debugger;
      var _that = this;
      var detailIds = flag == 'dir' ? fileData.detailId : fileData.fileId;
      request('', {
        url: ctx + 'rest/user/att/delelte',
        type: 'post',
        data: {
          attRealIninstId: rowData.attRealIninstId,
          detailIds: detailIds
        }
      }, function (res) {
        if (res.success) {
          _that.getFileListWin(rowData.attRealIninstId, rowData);
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
    // 预览电子件
    filePreview: function (data, flag) { // flag==pdf 查看施工图
      var detailId = data.fileId;
      var _that = this;
      var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
      var fileName = data.fileName;
      var fileType = this.getFileType(fileName);
      var flashAttributes = '';
      _that.filePreviewCount++
      if (flag == 'pdf' || flag == 'PDF') {
        var tempwindow = window.open(); // 先打开页面
        setTimeout(function () {
          tempwindow.location = ctx + 'cod/drawing/drawingCheck?detailId=' + detailId;
        }, 1000)
      } else {
        if (fileType == 'pdf' || flag == 'PDF') {
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
    // 切换选中的材料 flag==prev 上一个 next 下一个
    changeRowDataSel: function (flag) {
      debugger;
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
      this.selAttRealIninstId = this.selMatRowData.attRealIninstId ? this.selMatRowData.attRealIninstId : null;
      this.getFileListWin(this.selAttRealIninstId, this.selMatRowData);
    },
    // 展示材料一张表单弹窗
    showOneFormDialog1: function (oneformMat) {
      var _that = this;
      var _applyinstId = _that.parallelApplyinstId;
      _that.selMatRowData = oneformMat;
      _that.matformNameTitle = oneformMat.matName
      if (_applyinstId == '') {
        _that.getParallelApplyinstId('matForm', oneformMat.stoFormId)
      } else {
        _that.getOneFormrender3(_applyinstId, oneformMat.stoFormId)
      }
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
    // 查看证照
    cretPreview: function (authCode) {
      var _that = this;
      var tempwindow = window.open(); // 先打开页面
      request('', {
        url: ctx + 'aea/cert/getViewLicenseURL',
        type: 'get',
        data: {
          authCode: authCode
        }
      }, function (res) {
        if (res.success) {
          setTimeout(function () {
            tempwindow.location = res.content;
          }, 500)
        } else {
          tempwindow.close();
          _that.$message({
            message: res.message ? res.message : '证照查看失败',
            type: 'error'
          });
        }
      }, function (msg) {
        tempwindow.close();
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
      if (_that.jiansheFrom.length > 0) {
        _that.rootUnitInfoId = _that.jiansheFrom[0].unitInfoId;
        _that.rootLinkmanInfoId = _that.jiansheFrom[0].linkmanId;
      }
      if (_that.applySubjectType == 0 && _that.applyPersonFrom.applyLinkmanId) {
        _that.rootApplyLinkmanId = _that.applyPersonFrom.applyLinkmanId;
        _that.rootLinkmanInfoId = _that.applyPersonFrom.linkLinkmanId
      };
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
        url: ctx + 'aea/cert/bind/cert',
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
          _that.$message({
            message: '证照关联成功',
            type: 'success'
          });
          if (_that.showFileListKey.indexOf(_that.selMatRowData.matId) < 0) {
            _that.showFileListKey.push(_that.selMatRowData.matId)
          }
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
        url: ctx + 'aea/cert/unbind/cert',
        type: 'post',
        data: {
          matinstId: matinstId
        }
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
    // 获取申报主体信息
    getApplyObjectInfo: function () {
      var _that = this;
      request('', {
        url: ctx + 'rest/apply/common/applyObject',
        type: 'get',
        data: {
          projInfoId: _that.projInfoId
        }
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
    // 一张表单获取并联申报实例化id
    getParallelApplyinstId: function (flag, _stoFormId) {
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
            if (flag == 'matForm') {
              _that.getOneFormrender3(_that.parallelApplyinstId, _stoFormId);
            } else {
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

    // 材料列表-下载空表样表
    downLoadEmptyAndSampleTable: function(row){
      if(!row.ybKbDetailIds || !row.ybKbDetailIds.length) return this.apiMessage('暂无空表样表', 'info');
      window.open(ctx + 'rest/cloud/att/download?detailIds=' + row.ybKbDetailIds);
    },

    // 点击返回按钮，调用pager下的方法，显示pager下的列表
    returnToPageList: function(){
      pager.isProjDetail = false;
      return;
    },

  },
  filters: {
    formatDate: function (val, mat) {
      if (!val) return '暂无'
      var _date = new Date(val);
      return formatDate(_date, 'yyyy-MM-dd hh:mm')
    },
  },
});