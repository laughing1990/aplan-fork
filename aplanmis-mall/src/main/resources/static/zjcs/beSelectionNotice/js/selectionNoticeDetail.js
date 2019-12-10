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
	    purchaseDetails: {},//采购项目详情
	    approveProjInfo: {},//投资审批项目详情
	    activeName: 4,
	    fileList: [],
	    qualificationTree: false, // 是否展示资质树
	    selectedQuals: [], // 资质列表
	    defaultProps: {
		    children: 'children',
		    label: 'name'
	    },
	    curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
	    contentMinHeight: '',
      ctx:'',
    }
  },
  mounted: function () {
    this.ctx = ctx;
    var url = document.location.toString();
    this.projPurchaseId = url.split("?projPurchaseId=")[1];
    this.getPublicProjPurchaseDatail();
    this.contentMinHeight = this.curHeight - 295;
  },
  methods: {
    getPublicProjPurchaseDatail: function () {
      var _that = this;
      request('', {
        url: ctx + 'aea/supermarket/notice/getSelectionNoticeDetail',
        type: 'get',
        data: {projPurchaseId: _that.projPurchaseId}
      }, function (data) {
        if(data.content){
	        _that.purchaseDetails = data.content;
	        _that.approveProjInfo = data.content.approveProjInfo;
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    
  },
  filters: {
    formatDate: function(time) {
      var date = new Date(time);
      return formatDate(date, 'yyyy-MM-dd');
    },
    formatDateTime: function(time){
      var date = new Date(time);
      return tempFormatDate(time);
    },
    // 竞价方式format
    biddingTypeFormat: function(type){
      if(!type) return '无';
      return type == 1 ? '随机抽取': (type == 2? '直接抽取': '竞价抽取');
    },
    // 服务金额format
    procurePriceFormat: function(item){
      if(!item)return '暂无数据'
      // 随机选取
      if(item.biddingType == 1){
        if(item.isDefineAmount == 1){
          return !item.basePrice ? '暂无数据': ('￥' + item.basePrice.toLocaleString() + '元');
        }
      };
      // 直接选取
      if(item.biddingType == 2){
        if(item.isDefineAmount == 1){
          return !item.basePrice ? '暂无数据': ('￥' + item.basePrice.toLocaleString() + '元');
        }
        if(item.isDefineAmount == 0){
          return '暂不做评估与预算' ;
        }
      };
       // 竞价选取
       if(item.biddingType == 3){
        if(item.quoteType == 0){
          if(!item.basePrice && !item.highestPrice){
            return '暂无数据'
          }
          return '￥' + item.basePrice.toLocaleString() + '~' + item.highestPrice.toLocaleString() + '元';
        }
        if(item.quoteType == 1){
          if(!item.baseRate && !item.highestRate){
            return '暂无数据'
          }
          return item.baseRate + '% ~ ' + item.highestRate + '%';
        }
      };
      return '暂无数据'
    },
  }
});
