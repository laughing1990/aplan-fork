var vm = new Vue({
  el: '#index',
  data: function () {
    return {
      isScroll: true, // 页面是否出现滚动条
      activeName: 4,
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight), //当前屏幕高度
      contentMinHeight: '',

      // 中选公告查询数据
      searchData: {
        pageNum: 1,
        pageSize: 10,
        projName: '',
        serviceIdList: [],
        startTime: '',
        endTime: '',
        biddingType: '',
      }, 
      // 中选公告总数
      purchaseTotal: 0,
      serviceIdList: [], // 服务类型列表
      serviceIdListShow: [], // 初始化可见服务类型列表
      showMoreServiceId: true, // 展示更多服务类型
      purchaseList: [], // 展示列表

      // 当前展示的是哪个公告,默认选中中选公告
      curNoticeModule: 'selectionNotice',

      // 合同公示查询数据
      contractSearchData: {
        pageSize: 10,
        pageNum:1,
        projName: ''
      },
      contractList: [],
      contractTotal: 0,
    }
  },
  mounted: function () {
    this.init();
  },
  methods: {
    // init
    init: function () {
      this.getAllService();
      this.contentMinHeight = this.curHeight - 200;

      // 添加当前公告类型到url
      var _curNoticeHash = location.hash;
      if (_curNoticeHash) {
        this.curNoticeModule = _curNoticeHash.slice(1);
        location.hash = this.curNoticeModule;
        this.noticeListLoad();
      } else {
        this.curNoticeModule = 'selectionNotice';
        location.hash = this.curNoticeModule;
        this.getPublicProjPurchaseList();
      }

    },
    // 切换公告类型
    noticeSelected: function (tab) {
      this.noticeListLoad();
      location.hash = this.curNoticeModule;
    },
    // 各个公告类型加载对应的列表
    noticeListLoad: function () {
      if (this.curNoticeModule === 'selectionNotice') {
        this.getPublicProjPurchaseList();
      } else {
        this.fetchContractNoticeList();
      }
    },
    
    // 获取中选公告查询数据 -服务类型
    getAllService: function () {
      var _that = this;
      request('', {
        url: ctx + 'supermarket/purchase/getAllService',
        type: 'post',
      }, function (data) {
        if (data.content) {
          _that.serviceIdList = data.content.reverse();
          if (data.content.length > 3) {
            _that.serviceIdListShow = _that.serviceIdList.slice(0, 4);
          } else {
            _that.serviceIdListShow = _that.serviceIdList;
          }
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    // 展开收起项目类型
    toggleShow: function () { 
      this.showMoreServiceId ? this.showMoreServiceId = false : this.showMoreServiceId = true;
      if (this.showMoreServiceId) {
        this.serviceIdListShow = this.serviceIdList.slice(0, 4);
      } else {
        this.serviceIdListShow = this.serviceIdList;
      }
    },

    // 获取中选公告列表
    getPublicProjPurchaseList: function () {
      var _that = this;
      _that.searchData.serviceId = JSON.stringify(_that.searchData.serviceIdList);
      request('', {
        url: ctx + 'aea/supermarket/notice/listSelectionNotice',
        type: 'post',
        data: _that.searchData
      }, function (data) {
        if (data.content) {
          _that.purchaseList = data.content.rows;
          _that.purchaseTotal = data.content.total;
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    handleSizeChange: function (val) {
      this.searchData.pageSize = val;
      this.getPublicProjPurchaseList();
    },
    handleCurrentChange: function (val) {
      this.searchData.pageNum = val;
      this.getPublicProjPurchaseList();
    },

    // 获取合同公告列表
    fetchContractNoticeList: function(){
      var _that = this;
      request('', {
        url: ctx + 'aea/supermarket/notice/listContractNotice',
        type: 'get',
        data: _that.contractSearchData
      }, function (data) {
        if (data.content) {
          _that.contractList = data.content.rows;
          _that.contractTotal = data.content.total;
        }
      }, function (msg) {
        alertMsg('', '服务请求失败', '关闭', 'error', true);
      });
    },
    // 合同公告列表换页
    contractListPageChange: function(val){
      this.contractSearchData.pageNum = val;
      this.fetchContractNoticeList();
    },
    // 合同公告列表页面大小切换
    contractListSizeChange: function(val){
      this.contractSearchData.pageSize = val;
      this.fetchContractNoticeList();
    }, 
    // 合同公告列表查询
    contractListSearch: function(){
      this.contractListSearch.pageNum = 1;
      this.fetchContractNoticeList();
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
        if (new RegExp('(' + k + ')').test(fmt)) {
          var str = o[k] + '';
          fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : this.padLeftZero(str));
        }
      }
      return fmt;
    },
    padLeftZero: function (str) {
      return ('00' + str).substr(str.length);
    },
  },
  filters: {
    formatDate: function (time) {
      var date = new Date(time);
      return formatDate(date, 'yyyy-MM-dd');
    }
  }
});