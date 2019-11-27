/*
 * 审核界面申请表iframe
 */
var vm = new Vue({
  el: '#applyForm',
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
    return {
      labelWidth: '165px',
      isItemSeek: false,
      applyinstId: '',
      taskId: '',
      iteminstId: '',
      showMore: false,
      // 申办主体信息
      unitInfoList: [],
      projInfoList: [],
      iteminstList: [],
      applyinst: {},
      linkmanInfo: {},
      dicCodeItems: [],
      linkManTypeList: [],
      itemStateList: [],
      processStatesDialog: false,//情形弹框
      processStatesLoading: false,
      statesData: [],//情形列表
      pageLoading: true,
      applyTableName: parent.vm.stageOrItemName,
      applySubject: '0', // 0 个人， 1 企业
      personalApplyInfo: {},
      personalLinkInfo: {},
      unitTypeList: [],
      isZJItem: '',
      zjItemInfo: [],
      matsTableData: [],
      showFileListKey:[],
      getPaperAll: false,
      getCopyAll: false,
      isShowMatForm: '',
      model: {
        rules: {
          getPaper: {required: true, message: "必选", trigger: ["change"]},
          getCopy: {required: true, message: "必选", trigger: ["change"]},
          realPaperCount: {validator: checkMissValue, required: true, message: "必填字段", trigger: ['blur']},
          realCopyCount: {validator: checkMissValue, required: true, message: "必填字段", trigger: ['blur']},
        },
        matsTableData: []
      },
      showMatTableExpand: true,
      showUploadWindowFlag: false, // 是否展示文件上传窗口
      fileSelectionList: [], // 所选电子件
      selMatRowData: {}, // 所选择的材料信息
      uploadMatinstId: '', // 上传返回材料实例
      selMatinstId: '',
      showUploadWindowTitle: '材料附件',
      loadingFileWin: false,
      uploadTableData: [],
      fileList: [],
      matinstIds: '',
      matCodes: [],
      matIds: [],
    }
  },
  methods: {
    // 点击保存
    matFormSave: function(){
      var vm = this;
      var tmpArr = [];
      vm.model.matsTableData.forEach(function(u){
        var tmp = { matId: u.matId };
        var flag = false;
        if (!u.paperMatinstId&&u.getPaper){
          if (u.duePaperCount == 0) {
            if (u.realPaperCount !=0) {
              tmp.paperCnt = u.realPaperCount;
              tmp.paperMatinstId = u.paperMatinstId;
              flag = true;
            }
          } else {
            if (u.realPaperCount!=0) {
              tmp.paperCnt = u.realPaperCount;
              tmp.paperMatinstId = u.paperMatinstId;
              flag = true;
            }
          }
        }
        if (!u.copyMatinstId&&u.getCopy){
          if (u.dueCopyCount == 0) {
            if (u.realCopyCount !=0) {
              tmp.copyCnt = u.realCopyCount;
              tmp.copyMatinstId = u.copyMatinstId;
              flag = true;
            }
          } else {
            if (u.realCopyCount!=0) {
              tmp.copyCnt = u.realCopyCount;
              tmp.copyMatinstId = u.copyMatinstId;
              flag = true;
            }
          }
        }
        flag && tmpArr.push(tmp);
      });
      if (tmpArr.length == 0) {
        // return vm.$message.info('请勾选并填写份数再保存');
      }
      var params = {
        saveMatinstVo: {
          matCountVos: tmpArr,
          unitInfoId: vm.zjItemInfo[0].publishUnitInfoId||'',
          projInfoId: vm.zjItemInfo[0].projInfoId,
          linkmanInfoId: vm.zjItemInfo[0].publishLinkmanInfoId||'',
          applyinstId: vm.applyinstId,
          iteminstId: vm.iteminstId,
        },
      };
      // console.log(params);
      // if(window) return null;
      vm.pageLoading = true;
      request('', {
        url: ctx + 'market/approve/receivePaperAndStartProcess',
        type: 'post',
        ContentType: 'application/json',
        data: JSON.stringify(params),
      }, function(res) {
        vm.pageLoading = false;
        if (res.successs){
          vm.$message.success('保存成功');
        } else {
          vm.$message.error(res.message || '保存失败');
        }
      }, function(){
        vm.pageLoading = false;
        vm.$message.error('保存失败');
      })
    },
    //删除单个文件附件
    delOneFile: function (data, matData) {
      var _that = this;
      _that.loadingFileWin = true;
      request('', {
        url: ctx + 'rest/mats/att/delelte',
        type: 'post',
        data: {matinstId: matData.attMatinstId, detailIds: data.fileId}
      }, function (res) {
        _that.loadingFileWin = false;
        if (res.success) {
          _that.getFileListWin(matData.attMatinstId, matData);
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
        _that.loadingFileWin = false;
        _that.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    //下载单个附件
    downOneFile: function (data) {
      window.open(ctx + 'rest/mats/att/download?detailIds=' + data.fileId, '_blank')
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
    filePreview: function(row){
      parent.vm.filePreview(row);
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
      this.fileWinData.append("matinstId", rowData.attMatinstId);
      this.fileWinData.append("applyinstId", _that.applyinstId);
      this.fileWinData.append("matId", rowData.matId);
      _that.loadingFileWin = true;
      axios.defaults.withCredentials = true;
      axios.post(ctx + 'market/approve/uploadServiceResultAtt', _that.fileWinData).then(function (res) {
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
      detailIds = detailIds.join(',');
      _that.loadingFileWin = true;
      request('', {
        url: ctx + 'rest/mats/att/delelte',
        type: 'post',
        data: {matinstId: _that.selMatRowData.attMatinstId, detailIds: detailIds}
      }, function (res) {
        _that.loadingFileWin = false;
        if (res.success) {
          _that.getFileListWin(_that.selMatRowData.attMatinstId, _that.selMatRowData);
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
        _that.loadingFileWin = false;
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
    // 勾选电子件
    selectionFileChange: function (val) {
      this.fileSelectionList = val;
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
      func.tId = window.setTimeout(function () {
        this.loadingFileWin = false;
        func(func.temArr);
        func.temArr = [];
      }, 1000);
    },
    showUploadWindow: function (data) { // 展示文件上传下载弹窗
      var _that = this;
      _that.showUploadWindowFlag = true;
      _that.selMatRowData = data;
      _that.selMatinstId = data.matinstId ? data.matinstId : '',
          _that.showUploadWindowTitle = '材料附件 - ' + data.matName
      _that.getFileListWin(data.attMatinstId, data);
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
          item.getPaper = item.paperDisabled ? true : val;
          _that.getPaperAll = val;
        } else {
          item.getCopy = item.copyDisabled ? true : val;
          _that.getCopyAll = val;
        }
      });
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
    seeCredit: function (row, type, isBlackDia) {
      var reqData = {bizType: type};
      reqData.bizId = type == 'u' ? row.unitInfoId : row.linkmanInfoId;
      reqData.isBlackDia = isBlackDia || false;
      parent.vm.openCreditDialog(reqData);
    },
    getZJItemInfo: function () {
      var vm = this;
      request('', {
        url: ctx + 'market/approve/basic/apply/info',
        type: 'get',
        data: {
          taskId: vm.taskId,
          applyinstId: vm.applyinstId,
          isItemSeek: false,
        },
      }, function (res) {
        vm.pageLoading = false;
        if (res.success) {
          vm.projInfoList = [res.content.aeaProjInfo];
          vm.applyinst = res.content.iteminst;
          vm.zjItemInfo = [res.content.purchaseProj];
          vm.getMatTableData();
        } else {
          vm.$message.error(res.message || '获取申请表数据失败');
        }
      }, function () {
        vm.pageLoading = false;
        vm.$message.error('获取申请表数据失败');
      })
    },
    getMatTableData: function(){
      var vm = this;
      var _that = this;
      vm.pageLoading = true;
      request('', {
        url: ctx + 'market/approve/getProjPurchaseMatinstList',
        type: 'get',
        data: {
          applyinstId: vm.applyinstId,
          iteminstId: vm.iteminstId,
        },
      }, function(res) {
        vm.pageLoading = false;
        if (res.success) {
          vm.model.matsTableData = res.content;
          _that.model.matsTableData.map(function (item) {
            _that.model.matsTableData.map(function (item) {
              if (item.matChild == 'undefined' || item.matChild == undefined) {
                Vue.set(item, 'matChild', item.fileList);
              }
              if(item.certChild=='undefined'||item.certChild==undefined){
                Vue.set(item,'certChild',[]);
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
              if (item.paperMatinstId || item.duePaperCount == 0) {
                Vue.set(item, 'getPaper', true);
                Vue.set(item, 'paperDisabled', true);
              }
              if (item.copyMatinstId || item.dueCopyCount == 0) {
                Vue.set(item, 'getCopy', true);
                Vue.set(item, 'copyDisabled', true);
              }
              if(_that.matCodes.indexOf(item.matCode)<0){
                _that.matCodes.push(item.matCode);
              }
              _that.matIds.push(item.matId);
      
            });
          });
        } else {
          vm.$message.error(res.message || '获取申请表数据失败');
        }
      }, function(){
        vm.pageLoading = false;
        vm.$message.error('获取材料数据失败');
      })
    },
    getBaseApplyForm: function () {
      var vm = this;
      request('', {
        url: ctx + 'rest/approve/basic/apply/info',
        type: 'get',
        data: {
          applyinstId: vm.applyinstId,
          taskId: vm.taskId,
          isItemSeek: vm.isItemSeek
        }
      }, function (res) {
        vm.pageLoading = false;
        parent.vm.parentPageLoading = false;
        if (res.success) {
          res.content.unitInfoVoList && res.content.unitInfoVoList.length && res.content.unitInfoVoList.forEach(function (u) {
            u.showMore = false;
          })
          vm.unitInfoList = res.content.unitInfoVoList;
          vm.projInfoList = res.content.projInfoList;
          vm.iteminstList = res.content.iteminstList;
          vm.applyinst = res.content.applyInfoVo;
          vm.linkmanInfo = res.content.linkmanInfoVo;
          vm.applySubject = res.content.applySubject;
          var isApprover = getUrlParam('isApprover');
          var busRecordId = getUrlParam('busRecordId');
          var isNotSingle = (busRecordId == 'undefined' || busRecordId == 'null' || busRecordId == '');
          
          if (isNotSingle) {
            if (isApprover == '1') {
              // 找到当前事项
              var iteminstId = getUrlParam('iteminstId');
              var _index = 0;
              vm.iteminstList.forEach(function (u, index) {
                if (u.iteminstId == iteminstId) {
                  _index = index;
                }
              })
              parent.vm.iteminstName = '【' + vm.iteminstList[_index]['iteminstName'] + '】';
            }
          } else {
            parent.vm.iteminstName = '【' + vm.iteminstList[0]['iteminstName'] + '】';
          }
          // 是否黑名单
          var isBlack = false; // false为自动弹出失信信息，true为不自动弹出
          // 个人申报主体信息
          if (vm.applySubject == '0') {
            res.content.linkmanVoList.forEach(function (u) {
              if (u.type == 'link') {
                vm.personalLinkInfo = u;
              } else if (u.type == 'apply') {
                vm.personalApplyInfo = u;
              }
              // 个人是否黑名单
              if (u.creditType === false && isBlack == false) {
                isBlack = true;
                vm.seeCredit(u, 'l', true);
              }
            });
          } else if (vm.applySubject == '1') {
            parent.vm.applyMainType = '1';
            parent.vm.applyUnitList = vm.unitInfoList.concat([]);
            parent.vm.idLibSearchOpt.chooseUnit = vm.unitInfoList[0].unitInfoId;
            // 企业是否黑名单
            vm.unitInfoList.forEach(function (u) {
              if (u.creditType === false && isBlack == false) {
                isBlack = true;
                vm.seeCredit(u, 'u', true);
              }
            });
          }
          
        } else {
          vm.$message.error(res.message);
        }
      }, function () {
        vm.pageLoading = false;
        parent.vm.parentPageLoading = false;
        parent.vm.$message.error('获取申请表基本信息失败');
      })
    },
// 通用接口  根据dicCodeTypeCode获取数据字典
    getDicCodeItems: function () {
      var vm = this;
      request('', {
        url: ctx + 'rest/dict/code/items/list',
        type: 'get',
        data: {
          dicCodeTypeCode: 'IDTYPE',
        }
      }, function (res) {
        if (res.success) {
          // console.log(res.content);
          var result = res.content;
          for (var i = 0; i < result.length; i++) {
            var item = {};
            item['itemCode'] = result[i]['itemCode'];
            item['itemName'] = result[i]['itemName'];
            vm.dicCodeItems.push(item);
          }
        } else {
          console.log(res.message);
        }
      }, function () {
        console.log("获取数据字典失败！");
      })
    },
    showParState: function () {
      parent.vm.showParState();
    },
    getStatusText: function (status) {
      var text = '未知';
      this.itemStateList.forEach(function (u) {
        if (u.itemCode == status) {
          text = u.itemName;
        }
      })
      return text;
    },
    manTypeToText: function (type) {
      var vm = this;
      var text = '';
      vm.linkManTypeList.forEach(function (u) {
        if (u.itemCode == type) {
          text = u.itemName;
        }
      });
      return text;
    },
    // 单位类型转换
    changeUnitType: function (val) {
      var text = '-';
      this.unitTypeList.forEach(function (u) {
        if (u.itemCode == val) {
          text = u.itemName;
        }
      });
      return text
    },
    //
    getLinkManLabel: function (unitType, linkManType) {
      var vm = this;
      var str = vm.changeUnitType(unitType) + '项目' + vm.manTypeToText(linkManType);
      return str;
    },
  },
  created: function () {
    // this.getDicCodeItems(); //获取通用字典数据
    var vm = this;
    vm.applyinstId = getUrlParam('masterEntityKey');
    vm.taskId = getUrlParam('taskId');
    vm.isShowMatForm = getUrlParam('isShowMatForm');
    vm.iteminstId = getUrlParam('iteminstId');
    vm.isZJItem = getUrlParam('isZJItem');
    vm.isZJItem = (vm.isZJItem == 'true');
    var list = [
      {code: 'IDTYPE', dataStr: 'dicCodeItems'},
      {code: 'PROJ_UNIT_LINKMAN_TYPE', dataStr: 'linkManTypeList'},
      {code: 'ITEMINST_STATE', dataStr: 'itemStateList'},
      {code: 'XM_DWLX', dataStr: 'unitTypeList'},
    ];
    var len = list.length;
    list.forEach(function (u) {
      __STATIC.getDicList({
        code: u.code,
        vm: vm,
        dataStr: u.dataStr,
        callback: function () {
          if (--len == 0) {
            if (vm.isZJItem) {
              vm.getZJItemInfo();
            } else {
              vm.getBaseApplyForm();
            }
          }
        }
      });
    });
  },
  mounted: function () {
  },
  filters: {
    substrings: function (value) {
      return value.substring(0, 10);
    },
    dicCodeItem: function (value) {
      if (value != null) {
        var arr = value.split(',');
        for (var j = 0; j < arr.length; j++) {
          for (var i = 0; i < vm.dicCodeItems.length; i++) {
            var item = vm.dicCodeItems[i];
            if (item["itemCode"] == arr[j]) {
              arr[j] = item["itemName"];
              break;
            }
          }
        }
        return arr.join(',');
      }
      return value;
    },
    changeIDtypeFromDic: function (value) {
      if (value != null) {
        for (var i = 0; i < vm.dicCodeItems.length; i++) {
          var item = vm.dicCodeItems[i];
          if (item["itemCode"] == value) {
            return item["itemName"];
          }
        }
      }
      return value;
    },
    
    //将null转化为 0
    changeNullToZero: function (value) {
      if (value) {
        return value;
      } else {
        return 0;
      }
    },
    //将null转化为 无
    changeNullToWU: function (value) {
      if (value) {
        return value;
      } else {
        return '无';
      }
    },
    //将null转化为 是|否
    changeNullToNo: function (value) {
      if (value) {
        return '是';
      } else {
        return '否';
      }
    },
  }
});
function getUrlParam(name){
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  }
  return null;
}