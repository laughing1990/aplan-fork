// var ctx = "http://192.168.14.2:8084/aplanmis-mall/" //振强
function add0(m) {
  return m < 10 ? '0' + m : m
};

function tempFormatDate(temp) {
  if (!temp) return;
  var time = new Date(date);
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
      isScroll: true, // 页面是否出现滚动条
      projPurchaseId: '',
      purchaseDetails: {
        contract: {},
      },
      activeName: 4,
      fileList: [],
      qualificationTree: false, // 是否展示资质树
      selectedQuals: [], // 资质列表
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight), //当前屏幕高度
      contentMinHeight: '',
      ctx: '',
    }
  },
  mounted: function () {
    this.ctx = ctx;
    var url = document.location.toString();
    this.projPurchaseId = url.split("?projPurchaseId=")[1] ;//|| '8b883803-e0b0-4e5f-96a3-5fa6316ca70d';
    this.getPublicProjPurchaseDatail();
    this.contentMinHeight = this.curHeight - 295;
  },
  methods: {
    getPublicProjPurchaseDatail: function () {
      var _that = this;
      request('', {
        url: ctx + 'aea/supermarket/notice/getContractNoticeDetail',
        type: 'get',
        data: {
          projPurchaseId: _that.projPurchaseId
        }
      }, function (data) {
        if (data.content) {
          _that.purchaseDetails = data.content;
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
        // alertMsg('', '服务请求失败', '关闭', 'error', true);
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
    downloadContractEnclosure: function (obj) {
      window.open(ctx + 'supermarket/purchase/downloadAttachment/' + obj.detailId);
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
  }
});