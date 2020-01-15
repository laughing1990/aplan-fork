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
          field: 'concludedTime',
          sort: 'desc'
        },
        theme: '',
        acceptStartTime: '',
        acceptEndTime: '',
        applySource: '',
        applyType: '',
        instState: '',
        processStartTime: '',
        processEndTime: '',
        keyword: ''
      }
    }
  },
  methods: {
    //刷新列表
    fetchTableData: function () {
      var ts = this;

      this.searchFrom.pagination.pages = this.searchFrom.pagination.page;

      if (ts.searchFrom.acceptStartTime != '' && ts.searchFrom.acceptEndTime != '') {
        if (ts.searchFrom.acceptStartTime > ts.searchFrom.acceptEndTime) {
          ts.apiMessage('受理开始时间不能大于结束时间', 'error');
          return;
        }
      }

      if (ts.searchFrom.processStartTime != '' && ts.searchFrom.processEndTime != '') {
        if (ts.searchFrom.processStartTime > ts.searchFrom.processEndTime) {
          ts.apiMessage('办理开始时间不能大于结束时间', 'error');
          return;
        }
      }

      ts.loading = true;

      request('', {
        url: ctx + 'rest/conditional/query/listPickupCheckTasksExpress',
        type: 'get',
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
    //查看详情
    viewDetail: function (row) {
      var url = ctx + 'apanmis/page/stageApproveIndex?taskId=' + row.taskId + '&viewId=' + row.viewId;
      if (row.busRecordId) {
        url = url + '&busRecordId=' + row.busRecordId;
      }
      window.open(url, '_blank');
    },
    // 邮政下单
    clickEMS: function(row) {
      var menuName = '邮政下单';
      var menuInnerUrl = ctx + 'rest/certificate/registerIndex?applyinstId=' + row.applyinstId;
      menuInnerUrl += '&isEMS=true';
      var id = 'menu_' + new Date().getTime();
      var data = {
        'menuName': menuName,
        'menuInnerUrl': menuInnerUrl,
        'id': id,
      };
      try {
        parent.vm.addTab('', data, '', '');
      } catch (e) {
        window.open(menuInnerUrl, '_blank');
      }
    },
    // 查看寄件信息
    clickSeeEMS: function(row){
      var menuName = '寄件信息-' + row.projName;
      var menuInnerUrl = ctx + 'rest/certificate/sendInfo?applyinstId=' + row.applyinstId;
      var id = 'menu_' + new Date().getTime();
      var data = {
        'menuName': menuName,
        'menuInnerUrl': menuInnerUrl,
        'id': id,
      };
      try {
        parent.vm.addTab('', data, '', '');
      } catch (e) {
        window.open(menuInnerUrl, '_blank');
      }
    },
    //取件登记
    pickupCheck: function (row) {
      var menuName = '取件登记';
      var menuInnerUrl = ctx + 'rest/certificate/registerIndex?applyinstId=' + row.applyinstId;
      var id = 'menu_' + new Date().getTime();
      var data = {
        'menuName': menuName,
        'menuInnerUrl': menuInnerUrl,
        'id': id,
      };
      try {
        parent.vm.addTab('', data, '', '');
      } catch (e) {
        window.open(menuInnerUrl, '_blank');
      }
    }
  },
  created: function () {
    this.conditionalQueryDic();
    this.fetchTableData();
  }
});