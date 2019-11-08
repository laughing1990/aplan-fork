var vm = new Vue({
  el: '#searchTable',
  mixins: [mixins],
  data: function () {
    return {
      // loading-tip
      loadingTip: '',
      //查询数据
      searchFrom: {
        pageNum: 1,
        pageSize: 10,
        keyword: ''
      },
      // 表格数据
      tableData: [],
      // 表格数据总数
      dataTotal: 0,

      // 当前编辑行
      curEditRow: {},
      // 附件列表中当前选中附件list
      enclosureSelectList: [],
      // 附件上传时，从电脑中选择的文件列表
      enclosureFileListArr: [],

      // 操作行

      // 操作夯-统计表下拉选项
      statisticsOptions: [{
          name: '部门办件统计',
          value: 1
        },
        {
          name: '主题申报统计',
          value: 2
        },
        {
          name: '窗口申报统计',
          value: 3
        },
      ],
      pickerOptions0: {
        disabledDate: function(time) {
          return time.getTime() > Date.now() - 8.64e6
        }
      },

      // 操作行-表单
      handelRowForm: {
        // 操作行-统计表下拉
        statisticsValue: "",
        // 操作行-日期
        statisticsDate: culatorYesterDateRange() || ['',''],
      },
      // 校验操作行
      handelRowRules: {
        statisticsValue: [{
          required: true,
          message: '请选择统计表类型',
          trigger: 'change'
        }],
        statisticsDate: [{
          required: true,
          message: '请选择日期',
          trigger: 'change'
        }],
      },
    }
  },
  methods: {
    // 查询列表
    tableDataSearch: function () {
      this.searchFrom.pageNum = 1;
      this, this.fetchTableData();
    },
    //刷新列表
    fetchTableData: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'statistics/record/listStatisticsRecord',
        type: 'get',
        data: ts.searchFrom
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          ts.tableData = res.content.rows;
          ts.dataTotal = res.content.total;
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },
    handleSizeChange: function (val) {
      this.searchFrom.pageSize = val;
      this.fetchTableData();
    },
    handleCurrentChange: function (val) {
      this.searchFrom.pageNum = val;
      this.fetchTableData();
    },

    // 生成表格数据操作
    createTable: function(){
      var ts = this;
      ts.$refs['handelRowForm'].validate(function(valid){
        if (valid) {
          ts.createTableApi();
        } else {
          return false;
        }
      });
    },

    // 操作行-生成表数据
    createTableApi: function () {
      var ts = this,
        type = "",
        url = "";
      type = ts.handelRowForm.statisticsValue;
      if (type == 1) {
        url = "executeStatistics/org"
      } else if (type == 2) {
        url = "executeStatistics/theme"
      } else {
        url = "executeStatistics/win"
      }
      // console.log(ts.handelRowForm)
      // return
      ts.loading = true;
      ts.loadingTip = "正在执行统计，统计数据所需时间比较长，请耐心等待...";
      $.ajax({
        url: ctx + url,
        type: 'GET',
        data: {
          // dateStr: ts.handelRowForm.statisticsDate
          startDay: ts.handelRowForm.statisticsDate[0],
          endDay: ts.handelRowForm.statisticsDate[1],
        },
        success: function (res) {
          ts.loading = false;
          if (res.success) {
            ts.apiMessage('生成成功！', 'success');
            ts.tableDataSearch();
          } else {
            return ts.apiMessage(res.message, 'error')
          }
        },
        error: function () {
          ts.loading = false;
          return ts.apiMessage('网络错误！', 'error')
        },
      })
    },
  },

  created: function () {
    this.fetchTableData();
  },
  filters: {
    // 日期filter
    dateFormat: function (val, type) {
      if (!val) return '-';
      return formatDate(new Date(val), type)
    },
    // 生成来源filter
    operateSourceFormat: function (val) {
      if (!val) return;
      return val == 1 ? '程序生成' : '人工生成';
    },
    // 统计类型filter
    statisticsTypeFormat: function (val) {
      if (!val) return '-';
      return val == 's' ? '申报统计' : '办件统计'
    },
  },
});
// 手机号校验ele
var checkPhone = function (rule, value, callback) {
  if (!value) {
    return callback(new Error('手机号不能为空'));
  } else {
    var reg = /^1[3|4|5|7|8][0-9]\d{8}$/
    if (reg.test(value)) {
      callback();
    } else {
      return callback(new Error('请输入正确的手机号'));
    }
  }
};