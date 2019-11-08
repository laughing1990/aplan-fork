/*
 * @Author: ZL
 * @Date:   2019/06/13
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var vm = new Vue({
  el: '#index',
  data: function () {
    return {
      searchData: {
        pageNum: 1,
        pageSize: 10,
        projName: '',
        serviceIdList: [],
        startTime: '',
        endTime: '',
        biddingType: '',
      },  // 查询数据
      pageNum: 1, // 默认第一页
      pageSize: 10, // 默认每页10条
      serviceIdList: [], // 服务类型列表
      serviceIdListShow: [], // 初始化可见服务类型列表
      showMoreServiceId: true, // 展示更多服务类型
      purchaseList: [], // 展示列表
      isScroll: false, // 页面是否出现滚动条
      activeName: 3,
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      contentMinHeight: '',
    }
  },
  mounted: function () {
    this.getPublicProjPurchaseList();
    this.getAllService();
    var _this = this;
    this.contentMinHeight = this.curHeight - 295;
  },
  methods: {
    getPublicProjPurchaseList: function () {
      var _that = this;
      _that.searchData.pageNum = _that.pageNum;
        _that.searchData.pageSize = _that.pageSize;
        _that.searchData.serviceId = JSON.stringify(_that.searchData.serviceIdList);
        // _that.searchData.serviceId = _that.searchData.serviceIdList.length > 0 ? _that.searchData.serviceIdList : null;
        request('', {
            url: ctx + 'supermarket/purchase/getPublicProjPurchaseList',
        type: 'post',
        data: _that.searchData
      }, function (data) {
        if(data.content){
          _that.purchaseList = data.content;
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    getAllService: function () {
      var _that = this;
      request('', {
        url: ctx + 'supermarket/purchase/getAllService',
        type: 'post',
      }, function (data) {
        if(data.content){
          _that.serviceIdList = data.content.reverse();
          if(data.content.length>3){
            _that.serviceIdListShow =_that.serviceIdList.slice(0,4);
          }else {
            _that.serviceIdListShow =_that.serviceIdList;
          }
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    toggleShow: function(){  // 展开收起项目类型
      this.showMoreServiceId ? this.showMoreServiceId=false : this.showMoreServiceId=true;
      if(this.showMoreServiceId){
        this.serviceIdListShow =this.serviceIdList.slice(0,4);
      }else {
        this.serviceIdListShow =this.serviceIdList;
      }
    },
    handleSizeChange: function (val) {
      this.pageSize = val;
      this.getPublicProjPurchaseList();
    },
    handleCurrentChange: function(val) {
      this.pageNum = val;
      this.getPublicProjPurchaseList();
    },
    formatDate: function (date, fmt) {
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
      }
      var o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'h+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds()
      };
      for (var k in o) {
        if (new RegExp('('+k+')').test(fmt)) {
          var str = o[k] + '';
          fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : this.padLeftZero(str));
        }
      }
      return fmt;
    },
     padLeftZero:function (str) {
      return ('00' + str).substr(str.length);
    },
  },
  filters: {
    formatDate: function(time) {
      var date = new Date(time);
      return formatDate(date, 'yyyy-MM-dd');
    }
  }
});
