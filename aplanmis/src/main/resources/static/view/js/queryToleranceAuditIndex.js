var vm = new Vue({
  el: '#searchTable',
  mixins: [mixins],
  data: function () {
    return {
      //查询数据
      searchFrom: {
        pagination: {
          page: 1,
          pages: 1,
          perpage: 10
        },
        sort: {
          field: 'applyTime',
          sort: 'desc'
        },
        fillState: '',
        applyStartTime: '',
        applyEndTime: '',
        applySource: '',
        isSeriesApprove: '',
        // correctStartTime: '',
        // correctEndTime: '',
        keyword: ''
      }
    }
  },
  methods: {
    //刷新列表
    fetchTableData: function () {
      var ts = this;
      ts.searchFrom.pagination.pages = ts.searchFrom.pagination.page;
      if (ts.searchFrom.applyStartTime != '' && ts.searchFrom.applyEndTime != '') {
        if (ts.searchFrom.applyStartTime > ts.searchFrom.applyEndTime) {
          ts.apiMessage('申报开始时间不能大于结束时间', 'error');
          return;
        }
      }
      ts.loading = true;
      request('', {
        url: ctx + 'rest/conditional/query/listItemFills',
        type: 'post',
        data: ts.searchFrom
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          ts.tableData = res.content.list;
          ts.dataTotal = res.content.total;
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },


    // 审核操作
    jumpToAuditDetail: function (row) {
      var _url = ctx + 'rest/item/fill/toleranceDetailIndex?fillId=' + row.fillId;
      var tabsData = {
        menuName: '容缺审核',
        id: 'toLerance_detail' + row.applyAgentId,
        menuInnerUrl: _url,
      }
      try {
        //获取父window vue对象
        var parentVue = window.parent.vm
        parentVue.addTab('', tabsData, 'MENU_', parentVue.leftMenuActive++);
      } catch (err) {
        top.postMessage(tabsData, '*');
        window.open(_url, '_blank');
      }
    },
  },
  created: function () {
    this.conditionalQueryDic();
    this.fetchTableData();
  },
  filters: {
    formatDate: function (value, format) {
      return new Date(value).format(format);
    },
    // 来源
    applyinstSourceFormat: function (value) {
      if (!value) return '-';
      return value == 'win' ? '窗口' : '网上';
    },
    // 类型
    isSeriesApproveFormat: function (value) {
      if (!value) return '-';
      return value == '0' ? '并联' : '单项';
    },
    // 状态
    fillStateFormat: function (value) {
      var stateList = ['', '材料待补齐', '材料待审核', '材料已补齐'];
      return value ? stateList[value] : '-';
    },
  },
});