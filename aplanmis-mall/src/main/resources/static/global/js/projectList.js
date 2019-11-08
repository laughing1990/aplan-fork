// 表格查询初始数据
var LINKMANSEARCHDDATA = {
  pageNum: 1,
  pageSize: 10,
  keyword: ''
}
var vm = new Vue({
  el: '#projectList',
  mixins: [mixins],
  data: function () {
    return {
      // 项目列表
      list: [],
      // 项目查询数据
      searchData: {
        pageNum: 1,
        pageSize: 10,
        keyword: ''
      },
      total: 0,

      // 是否为本地
      isLocal: false,
    }
  },
  methods: {
    // 获取项目列表
    fetchList: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'aea/proj/info/listAeaProjInfo',
        type: 'get',
        data: ts.searchData
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          ts.list = res.content.list;
          ts.total = res.content.total;
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },
    // 列表查询
    linkManListSearch: function () {
      this.searchData.pageNum = 1;
      this.fetchList();
    },
    //列表选中
    handleSelectionChange: function (list) {
      this.linkmanSelectList = list;
    },
    // 列表换页
    handleCurrentChange: function (val) {
      this.searchData.pageNum = val;
      this.fetchList();
    },
    // 页面大小change
    handleSizeChange: function (val) {
      this.searchData.pageSize = val;
      this.fetchList();
    },

    // 清空搜索
    clearSearchList: function(){
      this.searchData.keyword = "";
      // this.pageNum = 1;
      this.fetchList();
    },

    // 表格-排序
    listSort: function (obj) {
      console.log(obj)
    },

    // 跳转到详情页面
    jumpToDetail: function (id) {
      var _projInfoId = id;
      // if (_projInfoId === 'add') {
      //   // window.parent.location.href = ctx + 'rest/project/detail';
      //   if (window.frames.length != parent.frames.length) {
      //     window.parent.open(ctx + 'rest/project/detail', '_blank')
      //   } else {
      //     window.open(ctx + 'rest/project/detail', '_blank')
      //   }
      //   // window.parent.open(ctx + 'rest/project/detail','_blank')
      // } else {
      //   // window.parent.location.href = ctx + 'rest/project/detail?projInfoId=' + _projInfoId;
      //   if (window.frames.length != parent.frames.length) {
      //     window.parent.open(ctx + 'rest/project/detail?projInfoId=' + _projInfoId, '_blank')
      //   } else {
      //     window.open(ctx + 'rest/project/detail?projInfoId=' + _projInfoId, '_blank')
      //   }
      //   // window.parent.open(ctx + 'rest/project/detail?projInfoId=' + _projInfoId, '_blank')
      // }
      var _url = "",_tabName="";
      if (_projInfoId === 'add'){
        _tabName = "新增项目";
        _url = ctx + 'rest/project/detail';
      }else{
        _tabName = "编辑项目"
        _url = ctx + 'rest/project/detail?projInfoId=' + _projInfoId
      }
      var _jumpData = {
        'menuName': _tabName,
        'menuInnerUrl': _url,
        'id': _projInfoId + '_pjdetail'
      };
      parent.vm.addTab('', _jumpData, parent.vm.activeTabIframe, '');
    },

  },
  created: function () {
    this.fetchList();
    if (document.location.protocol == "file:") {
      this.isLocal = true;
    }
  },
});