/*
 * @Author: ZL
 * @Date:   2019/06/13
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
//  var ctx = "http://192.168.14.2:8084/aplanmis-mall/" //振强
function add0(m) {
  return m < 10 ? '0' + m : m
};

function tempFormatDate(temp) {
  if (!temp) return;
  var time = new Date(temp);
  var y = time.getFullYear();
  var m = time.getMonth() + 1;
  var d = time.getDate();
  var h = time.getHours();
  var mm = time.getMinutes();
  var s = time.getSeconds();
  return y + '-' + add0(m) + '-' + add0(d) + " " + add0(h) + ':' + add0(mm) + ':' + add0(s);
}
var vm = new Vue({
  el: '#details',
  data: function () {
    return {
      isScroll: false, // 页面是否出现滚动条
      projPurchaseId: '',
      purchaseDetails: {},
      approveProjInfo: {},
      activeName: 3,
      fileList: [],
      qualificationTree: false, // 是否展示资质树
      selectedQuals: [], // 资质列表
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      contentMinHeight: '',
      ctx: '',
      // 当前是否显示我要报名按钮
      isShowSignBtn: false,
    }
  },
  mounted: function () {
    this.ctx = ctx;
    var url = document.location.toString();
    this.projPurchaseId = url.split("?projPurchaseId=")[1];
    var _this = this;
    this.getPublicProjPurchaseDatail();
    this.contentMinHeight = this.curHeight - 295;
  },
  methods: {
    getPublicProjPurchaseDatail: function () {
      var _that = this;
      request('', {
        url: ctx + 'supermarket/purchase/getPublicProjPurchaseDatail',
        type: 'post',
        data: { projPurchaseId: _that.projPurchaseId }
      }, function (data) {
        if (data.content) {
          _that.purchaseDetails = data.content;
          _that.approveProjInfo = data.content.approveProjInfo;

          var curLoginInfo = localStorage.getItem('loginInfo');
          if (curLoginInfo) {
            curLoginInfo = JSON.parse(curLoginInfo);
            if (curLoginInfo.isOwner !== '1' && _that.purchaseDetails.expirationDate > new Date().getTime()) {
              _that.isShowSignBtn = true;
            } else {
              _that.isShowSignBtn = false;
            }
          }

          if (_that.purchaseDetails.requireExplainFile) {
            _that.getRequireExplainFileList(_that.purchaseDetails.requireExplainFile);
          }
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    getRequireExplainFileList: function (requireExplainFile) {
      var _that = this;
      request('', {
        url: ctx + 'supermarket/purchase/getRequireExplainFileList/' + requireExplainFile,
        type: 'post'
      }, function (data) {
        if (data.content) {
          _that.fileList = data.content;
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    downloadFile: function (itemBasicId) {
      var _that = this;
      request('', {
        url: ctx + 'supermarket/purchase/downloadAttachment/' + itemBasicId,
        type: 'post'
      }, function (data) {
        _that.$message({
          message: '下载文件成功',
          type: 'success'
        });
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    showQualificationTree: function (purchaseDetails) { // 展示资质树
      var _that = this;
      request('', {
        url: ctx + 'supermarket/purchase/getSelectedQualMajorRequire/' + purchaseDetails,
        type: 'post'
      }, function (data) {
        if (data.success) {
          if (data.content.selectedQuals) {
            _that.selectedQuals = data.content.selectedQuals;
            _that.isScroll = false;
          }
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },

    // 跳转到我要报名页
    jumpToSignPandel: function () {
      if (!localStorage.getItem('loginInfo')) {
        return this.$message({
          message: '您尚未登陆,请先登陆！'
        })
      }
      window.location.href = ctx + "/supermarket/main/ownerCenter.html#projectSignup";
    },
  },
  filters: {
    formatDate: function (time) {
      var date = new Date(time);
      return formatDate(date, 'yyyy-MM-dd');
    },
    formatDateTime: function (time) {
      var date = new Date(time);
      return tempFormatDate(time);
    },
    // 竞价方式format
    biddingTypeFormat: function (type) {
      if (!type) return '无';
      return type == 1 ? '随机抽取' : (type == 2 ? '直接抽取' : '竞价抽取');
    },
    // 中介机构要求
    agentRequireFormat: function (detail) {
      var _arr = [];
      var _keyList = [{
        key: 'isQualRequire', name: '资质（资格）要求'
      }, {
        key: 'isRegisterRequire', name: '执业（职业）人员要求'
      }, {
        key: 'isRecordRequire', name: '备案要求'
      }, {
        key: 'promiseService', name: '仅承诺服务'
      }];
      for (var i = 0; i < _keyList.length; i++) {
        if (detail[_keyList[i].key] == 1) {
          _arr.push(_keyList[i].name);
        }
      }
      if (!_arr.length) return '暂无'
      return _arr.join('，');
    }
  }
});
