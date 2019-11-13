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
    if (new RegExp('('+k+')').test(fmt)) {
      var str = o[k] + ''
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str))
    }
  }
  return fmt
}

function padLeftZero(str) {
  return ('00' + str).substr(str.length)
}
var module1 = new Vue({
  el: '#materialCompletion',
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
      // 材料列表
      materialListData: [],
      // 列表查询
      listSearchData: {
        pageNum: 1,
        pageSize: 10,
        userId: '',
        keyword:'',
      },
      // 列表总数
      total: 0,
      // 是否是为项目详情
      isProjDetail: false,
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
        regionName:'',//行政名称

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
      },
      // 材料列表-当前操作行
      curHandelRow: {},
      // 材料列表上传的url
      uploadActionUrl: ctx + 'rest/user/comp/att/upload',
      // 当前材料的附件列表的展示dialog
      fileListDialog: false,
      // 当前材料的附件列表数据
      enclosureListForMaterial: [],
      // 当前材料的附件列表-批量中选中的数据
      enclosureListForMaterialSelections: [],

      // 是否展示文件上传dialog
      fileUploadDialogState: false,
      // 文件上传dialog-的文件列表数据
      enclosureFileListArr: [],
      //是否保存
      filePreviewCount: 0, // 查询预览是否成功次数
      applyinstCorrectId: '', // 材料补全实例id
      noDataTip: '暂无数据'
    }
  },
  mounted: function () {
    var _that = this;
    window.addEventListener('scroll', _that.handleScroll);
    window.addEventListener('resize', throttle(function (ev) {
      _that.navLeft()
    }, 100, 600));
    _that.navLeft();
    _that.getMaterialList();
  },
  methods: {
    // 材料补全 --> 材料补全列表查询接口
    getMaterialList: function () {
      var ts = this;
      ts.mloading = true;
      request('', {
        url: ctx + 'rest/user/matComplet/list',
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
    searchMatCompletionList:function(){
      this.getMaterialList();
    },

    // 页面大小切换
    handleSizeChange: function (val) {
      this.listSearchData.pageSize = val;
      this.getSearchProjList()
    },
    // 页面页码切换
    handleCurrentChange: function (val) {
      this.listSearchData.pageNum = val;
      this.getSearchProjList()
    },
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
    // ui方法
    apiMessage: function (msg, type) {
      this.$message({
        message: msg,
        type: type
      })
    },

    // 获取页面信息
    lookProjDetail: function (row) {
      var ts = this;
      ts.mloading = true;
      ts.applyinstCorrectId = row.applyinstCorrectId;
      request('', {
        url: ctx + 'rest/user/getMatCompCorrectInfo',
        type: 'get',
        data: {applyinstCorrectId: ts.applyinstCorrectId}
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.isProjDetail = true;
          ts.allPageInfoData = res.content;
          ts.materialListForm.materialList = res.content.matCorrectDtos;
          ts.materialListForm.materialSupplementRemark = res.content.correctMemo?res.content.correctMemo:'暂无';
          ts.handelBaseInfo();
          // 重新获取页面数据后移除form的校验显示
          // ts.$refs['materialListForm'].clearValidate();
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
      this.supplemetForm = {
        applyinstCode: this.allPageInfoData.applyinstCode,
        iteminstName: this.allPageInfoData.iteminstName,
        localCode: this.allPageInfoData.localCode,
        owner: this.allPageInfoData.owner,
        projInfoName: this.allPageInfoData.projInfoName,
        correctDueDays: this.allPageInfoData.correctDueDays, //补正天数
        correctMemo: this.allPageInfoData.correctMemo,
        correctDueTime: this.allPageInfoData.correctDueTime, //补正期限
        chargeOrgName: this.allPageInfoData.chargeOrgName,
        regionName:this.allPageInfoData.regionName,
      };
    },

    // 电子件相关
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
        // this.fetchPageInfo();
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
      _getData.attRealIninstId = ts.curHandelRow.attRealIninstId;
      ts.mloading = true;
      request('', {
        url: ctx + 'rest/user/comp/att/list',
        type: 'get',
        data: _getData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          row.attCount = res.content.length;
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
          return ts.apiMessage('您尚未选中需要删除的文件！', 'error')
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
          url: ctx + 'rest/user/comp/att/delelte',
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
    // 页面保存
    saveMaterialList: function (state) {
      var ts = this;
      var saveUrl = '';
      saveUrl = 'rest/user/saveMatCompCorrectInfo';
      debugger
      this.$refs['materialListForm'].validate(function (valid) {
        console.log(valid);
        if (valid||state=='6') {
          var _saveData = {
            correctId: ts.applyinstCorrectId,
            correctState: state,
            matCorrectDtosJson: JSON.stringify(ts.materialListForm.materialList),
          };
          ts.mloading = true;
          request('', {
            url: ctx + saveUrl,
            type: 'post',
            data: _saveData
          }, function (res) {
            ts.mloading = false;
            if (res.success) {
              ts.getMaterialList();
              ts.isProjDetail = false;
              ts.allPageInfoData.correctState = state;
              return ts.apiMessage('操作成功！', 'success');
            } else {
              return ts.apiMessage(res.message?res.message:'操作失败', 'error')
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
    // 文件上传dialog-打开，每次都先清掉文件暂存的数据数据
    openUploadDialog: function (row) {
      this.curHandelRow = row;
      this.fileUploadDialogState = true;
      this.enclosureFileListArr = [];
    },
    // close文件上传dialog
    closeUploadDialog: function () {
      this.$refs.enclosureFileUploadRef.clearFiles()
      // this.fetchPageInfo();
    },
    // 关闭文件列表弹窗
    uploadAttRow: function(){
      this.curHandelRow.attCount = this.enclosureListForMaterial?this.enclosureListForMaterial.length:0;
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
  },
  filters: {
    formatDate: function (val, mat) {
      if (!val) return '暂无'
      var _date = new Date(val);
      return formatDate(_date, mat)
    },
  },
});