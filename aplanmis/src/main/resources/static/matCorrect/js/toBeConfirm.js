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
var pager = new Vue({
  el: '#matCorrectToBeConfirm',
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
      // 全局loading
      mloading: false,
      activeNames: ['1', '2', '3', '4', '5', '6'], // el-collapse 默认展开列表
      verticalTabData: [ // 左侧纵向导航数据
        {
          label: '基本信息',
          target: 'baseInfo'
        }, {
          label: '补正待确认',
          target: 'materialSupplement'
        }, {
              label: '继续补正清单',
          target: 'supplementDetailListDom'
        }
      ],
      activeTab: 0, // 纵向导航active状态index

      // 获取到的页面所有信息
      allPageInfoData: {},
        pdfSrc: '',
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

        // 窗口人员的意见
        windowOpinion: ''
      },
      // 材料列表-当前操作行
      curHandelRow: {},
      // 材料列表上传的url
      uploadActionUrl: ctx + 'rest/correct/att/upload',
      // 当前材料的附件列表的展示dialog
      fileListDialog: false,
        prePdfVisible: false,
      // 当前材料的附件列表数据
      enclosureListForMaterial: [],
      // 当前材料的附件列表-批量中选中的数据
      enclosureListForMaterialSelections: [],

      // 是否展示文件上传dialog
      fileUploadDialogState: false,
      // 文件上传dialog-的文件列表数据
      enclosureFileListArr: [],


      // 补正清单相关
      // 补正清单列表
      supplementDetailList: [],
    }
  },
  mounted: function () {
    var _that = this;
    window.addEventListener('scroll', _that.handleScroll);
    window.addEventListener('resize', throttle(function (ev) {
      _that.navLeft()
    }, 100, 600));
    _that.navLeft()
    this.fetchPageInfo();
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

    // 获取页面信息
    fetchPageInfo: function () {
      var ts = this,
        _getData = {};
      _getData.correctId = ts.getSerachParamsForUrl('correctId');
      ts.mloading = true;
      request('', {
        url: ctx + 'rest/correct/getItemCorrectRealIninst',
        type: 'get',
        data: _getData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.allPageInfoData = res.content;
          //  窗口人员的意见
            // ts.materialListForm.windowOpinion = res.content.correctMemo;
            ts.materialListForm.windowOpinion = res.content.correctDesc;
            // ts.materialListForm.materialSupplementDesc = res.content.correctDesc;
          ts.handelSuppleToBeConfirmList();
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

    // 处理补正待确认列表数组数据
    handelSuppleToBeConfirmList: function () {
      this.materialListForm.materialList = [];
      var _allMaterialList = this.allPageInfoData.matCorrectDtos;
      for (var i = 0; i < _allMaterialList.length; i++) {
        // this.$set(_allMaterialList[i], 'isSurePaper', true);
        // this.$set(_allMaterialList[i], 'isSureCopy', true);
        // this.$set(_allMaterialList[i], 'isSureAtt', true);
        // test
        this.$set(_allMaterialList[i], 'isSurePaper', false);
        this.$set(_allMaterialList[i], 'isSureCopy', false);
        this.$set(_allMaterialList[i], 'isSureAtt', false);

        // 临时的补正意见，用于前端
        this.$set(_allMaterialList[i], 'paperDueIninstOpinion2', _allMaterialList[i].paperDueIninstOpinion);
        this.$set(_allMaterialList[i], 'copyDueIninstOpinion2', _allMaterialList[i].copyDueIninstOpinion);
        this.$set(_allMaterialList[i], 'attDueIninstOpinion2', _allMaterialList[i].attDueIninstOpinion);

        // 默认当前材料当前类型下是符合的
        if (_allMaterialList[i].paperMatinstId) {
          _allMaterialList[i].paperIsPass = 1;
          this.$set(_allMaterialList[i], 'isSurePaper', true);
        }
        if (_allMaterialList[i].copyMatinstId) {
          _allMaterialList[i].copyIsPass = 1;
          this.$set(_allMaterialList[i], 'isSureCopy', true);
        }
        if (_allMaterialList[i].attMatinstId) {
          _allMaterialList[i].attIsPass = 1;
          this.$set(_allMaterialList[i], 'isSureAtt', true);
        }
        this.materialListForm.materialList.push(_allMaterialList[i]);
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
        url: ctx + 'rest/correct/att/list',
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
          url: ctx + 'rest/correct/att/delelte',
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
    // 文件预览
    previewFile: function (id) {
      if (!id) return;
      if (window.frames.length != parent.frames.length) {
        window.parent.open(ctx + 'rest/mats/att/preview/' + id);
      } else {
        window.open(ctx + 'rest/mats/att/preview/' + id);
      }

    },
    // 文件下载
    downFileForMaterial: function (id) {
      if (!id) return;
      var ts = this,
        _delids = [];
      if (id !== 'batch') {
        _delids = id;
      } else {
        var _allDelList = ts.enclosureListForMaterialSelections
        if (!_allDelList.length) {
          return ts.apiMessage('您尚未选中需要下载的文件！', 'info')
        }
        _allDelList.forEach(function (item) {
          _delids.push(item.fileId)
        })
      }
      if (window.frames.length != parent.frames.length) {
        window.parent.open(ctx + 'rest/mats/att/download?detailIds=' + _delids.toString());
      } else {
        window.open(ctx + 'rest/mats/att/download?detailIds=' + _delids.toString());
      }
    },


    // 文件上传dialog-打开，每次都先清掉文件暂存的数据数据
    openUploadDialog: function (row) {
      this.curHandelRow = row;
      this.fileUploadDialogState = true;
      this.enclosureFileListArr = [];
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


    // 补正清单相关
    // 是否符合  补正待确认=》补正清单
    supplementToBeConfirmAccord: function (index) {
      this.supplementDetailList = [];
      // console.log( this.supplementDetailList)
      // 得到不符合的补正清单列表
      // for (var i = 0; i < this.materialListForm.materialList.length; i++) {
      //   var item = this.materialListForm.materialList[i];
      //   if (!item.isSurePaper || !item.isSureCopy || !item.isSureAtt) {
      //     this.supplementDetailList.push(JSON.parse(JSON.stringify(item)));
      //   }
      // }
      this.supplementDetailList = this.materialListForm.materialList.filter(function (item) {
        // if (!item.isSurePaper || !item.isSureCopy || !item.isSureAtt) {
        //   return item;
        // }
        if ((!item.isSurePaper && item.paperMatinstId) || (!item.isSureCopy && item.copyMatinstId) || (!item.isSureAtt && item.attMatinstId)) {
          return item;
        }
      })
      // this.materialListForm.materialList.forEach(function (item) {
      //   if (!item.isSurePaper && !item.isSureCopy && !item.isSureAtt) {
      //     $('.my-el-table').find('.el-table__body-wrapper table .el-table__row').eq(index).css('display', 'none');
      //   }
      // })
      if(index<0){return}
      if (!this.materialListForm.materialList[index].isSurePaper && !this.materialListForm.materialList[index].isSureCopy && !this.materialListForm.materialList[index].isSureAtt) {
        $('.my-el-table').find('.el-table__body-wrapper table .el-table__row').eq(index).css('display', 'none');
      }
      // console.log(this.materialListForm.materialList)
    },
    // 是否符合，补正清单=》补正待确认
    supplementDetailItemAccord: function (index, row, type) {
      this.supplementToBeConfirmAccord(-1);
      var _idx = -1;
      var _id = row.matId;
      for(var i = 0; i< this.materialListForm.materialList.length; i++){
        if(_id == this.materialListForm.materialList[i].matId){
          _idx = i;
          break;
        }
      }
      console.log(_idx)
      $('.my-el-table').find('.el-table__body-wrapper table .el-table__row').eq(_idx).css('display', 'table-row');
    },
    // 处理要保存的材料列表数据
    preSaveMaterialList: function () {
      var ts = this;
      var _allList = JSON.parse(JSON.stringify(ts.materialListForm.materialList));
      for (var i = 0; i < _allList.length; i++) {
        if (!_allList[i].isSurePaper && _allList[i].paperMatinstId) {
          _allList[i].paperIsPass = 0;
        }
        if (!_allList[i].isSureCopy && _allList[i].copyMatinstId) {
          _allList[i].copyIsPass = 0;
        }
        if (!_allList[i].isSureAtt && _allList[i].attMatinstId) {
          _allList[i].attIsPass = 0;
        }
        // _allList[i].paperIsPass == 0 && (_allList[i].paperDueIninstOpinion = _allList[i].paperDueIninstOpinion2);
        // _allList[i].copyIsPass == 0 && (_allList[i].copyDueIninstOpinion = _allList[i].copyDueIninstOpinion2);
        // _allList[i].attIsPass == 0 && (_allList[i].attDueIninstOpinion = _allList[i].attDueIninstOpinion2);
        if(_allList[i].paperIsPass == 0){
          _allList[i].paperDueIninstOpinion = _allList[i].paperDueIninstOpinion2;
        }else{
          _allList[i].paperDueIninstOpinion2 = "";
        }
        if(_allList[i].copyIsPass == 0){
          _allList[i].copyDueIninstOpinion = _allList[i].copyDueIninstOpinion2;
        }else{
          _allList[i].copyDueIninstOpinion2 = "";
        }
        if( _allList[i].attIsPass == 0){
          _allList[i].attDueIninstOpinion = _allList[i].attDueIninstOpinion2;
        }else{
          _allList[i].attDueIninstOpinion2 = "";
        }
      }
      // console.log(_allList)
      return JSON.stringify(_allList);
    },
// 预览电子件 必须要有detailId
      filePreview: function (data) {
          if (!data.detailId) {
              data.detailId = data.fileId;
          } // 设置detailId
          if (!data.attFormat) {
              data.attFormat = data.fileType;
          } // 文件类型
          if (allowPreType[data.attFormat]) {
              return this.preFile(data);
          } // 预览pdf、doc等
          // 预览图片等
          var detailId = data.detailId;
          var flashAttributes = '';
          var tempwindow = window.open('_blank'); // 先打开页面
          tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
      },
      preFile: function (obj) {
          var vm = this;
          if (obj.attFormat == 'pdf') {
              // vm.doPreFile(obj);
              // return null;
              // pdf格式的使用另一个接口直接打开文件
              var tempwindow = window.open(); // 先打开页面
              setTimeout(function () {
                  tempwindow.location = ctx + 'cod/drawing/drawingCheck?detailId=' + obj.detailId;
              }, 1000);
              return null;
          }
          // 判断服务器端文件是否已经转换成功，转换成功才能预览
          vm.parentPageLoading = true;
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
                      vm.doPreFile(obj);
                  } else {
                      if (++count > 9) {
                          confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                              count = 0;
                              doRequest();
                          }, function () {
                              count = 0;
                              vm.parentPageLoading = false;
                              return false;
                          }, '确定', '取消', 'warning', true)
                      } else {
                          setTimeout(function () {
                              vm.doRequest();
                          }, 1000)
                      }
                      // vm.$message('文件还未转换成功，请稍后再进行预览操作');
                  }
              }, function (res) {
                  count = 0;
                  vm.parentPageLoading = false;
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
      closePdfDialog: function () {
          this.pdfSrc = '';
      },
    // 页面保存
    saveMaterialList: function (state) {
      var ts = this;
      // test
      // ts.closeWindowTab();
        // return
      ts.preSaveMaterialList();
      this.$refs['materialListForm'].validate(function (valid, obj) {
        if (valid) {
          var _saveData = {};
          _saveData.correctId = ts.getSerachParamsForUrl('correctId');
          _saveData.correctState = state;
          _saveData.correctMemo = ts.materialListForm.materialSupplementRemark;
          _saveData.matCorrectDtosJson = ts.preSaveMaterialList();
          // console.log(JSON.parse(_saveData.matCorrectDtosJson))
          // return;
          ts.mloading = true;
          request('', {
            url: ctx + 'rest/correct/matCorrectConfirm',
            type: 'post',
            data: _saveData
          }, function (res) {
              ts.mloading = false;
              if (res.success) {
                  // 如果是保存，成功后关掉打开的页面
                  ts.apiMessage('保存成功！', 'success');
                  return setTimeout(ts.closeWindowTab(), 4500);
              } else {
                  return ts.apiMessage(res.message, 'error')
              }
          }, function (msg) {
            ts.mloading = false;
            alertMsg('', '服务请求失败', '关闭', 'error', true);
          });
        } else {
          return ts.apiMessage('请完善材料列表', 'warning');
        }
      })
    },

    // 关闭窗口-(这里使用的是父级window下面的vue实例中的方法来移除tab,这个tab是ele控件)
    closeWindowTab: function () {
      var ts = this;
      ts.correctEndRefresh();

        // setTimeout(function () {
        //   window.parent.vm.removeTab(parent.vm.activeTabIframe);
        // }, 500)
    },

    // 如果是补正结束，那就刷新对应的列表页
    correctEndRefresh: function () {
      var _iframs = $(window.parent.document).find('iframe');
        var _panentIframsrc = this.getSerachParamsForUrl('parentIfreamUrl');
      var _tagIframe = '';
      for (var i = 0; i < _iframs.length; i++) {
        var _src = _iframs.eq(i).attr('src');
        if (_src && (_src == _panentIframsrc)) {
          _tagIframe = _iframs.eq(i);
          break;
        }
      }
        // console.log(_panentIframsrc)
        // console.log(_tagIframe[0])
        if (_tagIframe[0] != '') {
            var _panentIframsrc = this.getSerachParamsForUrl('currentTabId');
            parent.vm.activeTabIframe = _panentIframsrc;
            _tagIframe[0].contentWindow.location.reload(true);
            window.parent.vm.removeTab(this.getSerachParamsForUrl('correctId'));
        }

    },
  },
  filters: {
    formatDate: function (val, mat) {
      if (!val) return '暂无'
      var _date = new Date(val);
      return formatDate(_date, mat)
    },
  },
});