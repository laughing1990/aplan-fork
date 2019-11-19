/*
 * 审核界面申请表iframe
 */
var vm = new Vue({
  el: '#applyForm',
  data: function () {
    return {
      labelWidth: '165px',
      isItemSeek: false,
      applyinstId: getUrlParam('masterEntityKey'),
      taskId: getUrlParam('taskId'),
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
      applySubject: '0',
      personalApplyInfo: {},
      personalLinkInfo: {},
      unitTypeList: [],
      isZJItem: getUrlParam('isZJItem'),
    }
  },
  methods: {
    seeCredit: function (row, type, isBlackDia) {
      var reqData = {bizType: type};
      reqData.bizId = type == 'u' ? row.unitInfoId : row.linkmanInfoId;
      reqData.isBlackDia = isBlackDia || false;
      parent.vm.openCreditDialog(reqData);
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
          (--len == 0) && vm.getBaseApplyForm();
        }
      });
    });
  },
  mounted: function () {
  },
  filters: {
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

function getUrlParam(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  }
  ;
  return null;
}