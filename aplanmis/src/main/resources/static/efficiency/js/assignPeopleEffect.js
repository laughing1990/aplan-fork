// 定义一天的秒数
var oneDaySeconds = 3600 * 1000 * 24;


var pager = new Vue({
  el: '#effectSupervisionAssign',
  mixins: [mixins],
  data: function () {
    return {
      // 办件状态统计list
      statusCardData: [{
          name: '容缺受理件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'rongque_jian',
          jumpUrl: 'efficacyJump/page/queryItemToleranceAcceptIndex.html'//跳转模块路径
        },
        {
          name: '待补正件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'daibuzheng_jian',
          jumpUrl: 'efficacyJump/page/queryNeedCorrectTasksIndex.html'//跳转模块路径
        },
        {
          name: '补正待确认件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'buquandaiqueren_jian',
          jumpUrl: 'efficacyJump/page/queryUnConfirmItemCorrectTasksIndex.html'//跳转模块路径
        },
        {
          name: '特殊程序件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'teshuchengxu_jian',
          jumpUrl: 'efficacyJump/page/querySpecialProcedureTasksIndex.html'//跳转模块路径
        },
        {
          name: '容缺通过件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'rongquetongguo_jian',
          jumpUrl: 'efficacyJump/page/queryItemAgreeToleranceIndex.html'//跳转模块路径
        },
        {
          name: '办理不通过件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'banlibutongguo_jian',
          jumpUrl: 'efficacyJump/page/queryItemDisgreeIndex.html'//跳转模块路径
        },
        {
          name: '不予受理件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'buyushouli_jian',
          jumpUrl: 'efficacyJump/page/queryItemOutScopeIndex.html'//跳转模块路径
        }
      ],

      // el-日期控件快捷键
      pickerOptions: {
        shortcuts: [{
            text: '今天',
            onClick: function (picker) {
              var date = new Date()
              var end = formatDate(date, 'yyyy-MM-dd')
              date.setHours(0);
              date.setMinutes(0);
              date.setSeconds(0);
              date.setMilliseconds(0);
              var start = date.getTime()
              // var start = date.getTime() - oneDaySeconds;
              start = formatDate(new Date(start), 'yyyy-MM-dd')
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '本周',
            onClick: function (picker) {
              var date = new Date()
              var weekday = date.getDay() || 7
              var end = formatDate(date, 'yyyy-MM-dd');
              date.setDate(date.getDate() - weekday + 1)
              var start = formatDate(date, 'yyyy-MM-dd');
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '本月',
            onClick: function (picker) {
              var date = new Date()
              var end = formatDate(date, 'yyyy-MM-dd');
              date.setDate(1)
              var start = formatDate(date, 'yyyy-MM-dd');
              picker.$emit('pick', [start, end])
            }
          },
          // {
          //   text: '全年',
          //   onClick: function (picker) {
          //     var date = new Date()
          //     var end = date.getTime()
          //     date.setDate(1)
          //     date.setMonth(0)
          //     var start = date.getTime()
          //     picker.$emit('pick', [start, end])
          //   }
          // },
        ]
      },

      // 部门受理事项统计相关
      // 部门受理事项统计柱状图实例
      departAcceptBarChart: '',
      // 部门受理事项统计-时间筛选器
      departAcceptTimeRange: culatorCurMonthDateRange() || [],
      // 部门受理事项统计的查询参数
      departAcceptBarCheckData: {
        startTime: '',
        endTime: ''
      },
      // 部门受理事项统计的表格数据
      departAcceptTableList: [],
      // 部门受理事项统计-阶段类型 昨日，本周，本月
      selectedDepartAcceptType: 'curMonth',

      // 月度事项受理统计相关
      // 月度事项受理统计线状图实例
      monthAcceptLineChart: '',
      //月度事项受理统计-时间筛选器
      monthAcceptTimeRange: culatorCurYearDateRage() || [],
      // 月度事项受理统计的查询参数
      monthAcceptLineCheckData: {
        startMonth: '',
        endMonth: ''
      },

      // 地区办件统计相关
      // 地区办件统计柱状图实例
      areaDeclareBarChart: '',
      // 地区办件统计-时间范围筛选
      areaDeclareBarChartTimeRange: culatorCurMonthDateRange() || [],
      // 地区办件统计的查询参数
      areaDeclareBarCheckData: {
        startTime: '',
        endTime: ''
      },

      // 事项办理异常排名相关
      // 事项办理异常排名-表格数据
      matterHandelTableList: [],
      // 事项办理异常排名-查询数据
      matterHandelTableCheckData: {},

      // 项目并联审批统计相关
      // 项目并联审批饼统计实例
      projectParallelApprovalPieChart: '',
      // 项目并联审批统计查询参数
      projectParallelApprovalCheckData: {},

      // 申报来源统计相关
      // 申报来源统计实例
      declareSourcePieChart: '',
      // 申报来源统计查询参数
      declareSourceCheckData: {},
    }
  },
  methods: {
    // 获取页面数据
    init: function () {
      this.fetchDepartAcceptData();
      this.fetchMonthAcceptData();
      this.fetchAreaDeclareData();
      this.fetchprojectParallelApprovalData();
      this.fetchDeclareSourceData();
      this.fetchMatterHandelAbnormalTableListData();
      this.fetchPieceStatueStatistics();
    },
    // 页面echart渲染resize
    render: function () {
      this.departAcceptBarChart && this.departAcceptBarChart.resize();
      this.monthAcceptLineChart && this.monthAcceptLineChart.resize();
      this.areaDeclareBarChart && this.areaDeclareBarChart.resize();
      this.projectParallelApprovalPieChart && this.projectParallelApprovalPieChart.resize();
      this.declareSourcePieChart && this.declareSourcePieChart.resize();
    },

    //部门受理事项统计相关
    searchDepartAcceptForTimeRange: function (type) {
      var _timeRange = [], _url = '';
      this.selectedDepartAcceptType = type;
      if(type === 'yesterDay'){
        _timeRange = culatorYesterDateRange();
        _url = ctx + 'org/efficiency/supervision/queryYesterdayOrgHandleItemStatistics'
      }else if(type === 'curWeek'){
        _timeRange = culatorCurWeekDateRange()
        _url = ctx + 'org/efficiency/supervision/queryThisWeekOrgHandleItemStatistics'
      }else{
        _timeRange = culatorCurMonthDateRange()
        _url = ctx + 'org/efficiency/supervision/queryThisMonthOrgHandleItemStatistics'
      }
      this.departAcceptTimeRange = _timeRange;
      // console.log(_timeRange)
      this.fetchDepartAcceptDataForTimeRange(_url)
    },
    // 部门受理事项统计-时间段直接获取
    fetchDepartAcceptDataForTimeRange: function(url){
      var ts = this;
      ts.loading = true;
      request('', {
        url: url,
        type: 'get',
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            ts.departAcceptTableList = res.content;
            ts.handelWindowAcceptBarChartData(res.content);
          } else {
            ts.departAcceptBarChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.departAcceptBarChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.departAcceptBarChart = '';
      });
    },
    // 部门受理事项统计-时间范围控件变化
    departAcceptTimeRangeChange: function (time) {
      this.departAcceptTimeRange = time;
      // console.log(time)
      this.fetchDepartAcceptData();
    },
    // 获取部门受理事项统计数据
    fetchDepartAcceptData: function (url) {
      var ts = this;
      ts.loading = true;
      ts.departAcceptBarCheckData.startTime = ts.departAcceptTimeRange[0] || '';
      ts.departAcceptBarCheckData.endTime = ts.departAcceptTimeRange[1] || '';
      request('', {
        url: ctx + 'org/efficiency/supervision/queryOrgHandleItemStatistics',
        type: 'get',
        data: ts.departAcceptBarCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            ts.departAcceptTableList = res.content;
            ts.handelWindowAcceptBarChartData(res.content);
          } else {
            ts.departAcceptBarChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.departAcceptBarChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.departAcceptBarChart = '';
      });
    },
    // 部门受理事项统计-柱状图处理获取到的数据
    handelWindowAcceptBarChartData: function (handData) {
      // handData = handData.slice(0,20)
      var _maxBgList = [],
        _title = [],
        _receiptCount = [],
        _notAcceptCount = [];
      for (var k = 0; k < handData.length; k++) {
        _title.push(handData[k].orgName)
        _receiptCount.push(handData[k].receiptCount)
        _notAcceptCount.push(handData[k].notAcceptCount)
      }
      // 获取最大值，用于柱状图柱子背景的填充数据
      var _max = Math.max.apply(null, _receiptCount);
      if(0<_max<100){
        _max = Math.ceil(_max/10) * 10
      }else if(100<_max<1000){
        _max = Math.ceil(_max/100) * 100
      }else if(1000<_max<10000){
        _max = Math.ceil(_max/1000) * 1000
      }
      for (var i = 0; i < handData.length; i++) {
        _maxBgList.push(_max);
      }
      // 根据接口数据的长度来动态修改zoom功能的范围值
      var _len = handData.length;
      if(_len > 40){
        assignEchartOptions.departAcceptDeclareOpt.dataZoom[0].end = 8
      }else if(_len >60){
        assignEchartOptions.departAcceptDeclareOpt.dataZoom[0].end = 4
      }
      // assignEchartOptions.departAcceptDeclareOpt.yAxis.max = _max; 
      assignEchartOptions.departAcceptDeclareOpt.xAxis.data = _title;
      assignEchartOptions.departAcceptDeclareOpt.series[0].data = _maxBgList;
      assignEchartOptions.departAcceptDeclareOpt.series[1].data = _receiptCount;
      assignEchartOptions.departAcceptDeclareOpt.series[2].data = _notAcceptCount;
      this.renderDepartAcceptBarChart();
    },
    // 部门受理事项统计-渲染echart
    renderDepartAcceptBarChart: function () {
      var ts = this;
      this.departAcceptBarChart = echarts.init(document.getElementById('departAcceptBarChart'));
      this.departAcceptBarChart.setOption(assignEchartOptions.departAcceptDeclareOpt);
    },

    //月度事项受理统计相关
    //月度实现受理时间切换
    monthAcceptTimeRangeChange: function (time) {
      this.monthAcceptTimeRange = time;
      this.fetchMonthAcceptData();
    },
    //获取月度事项受理统计数据
    fetchMonthAcceptData: function () {
      var ts = this;
      ts.loading = true;
      ts.monthAcceptLineCheckData.startMonth = formatDate(new Date(ts.monthAcceptTimeRange[0]), 'yyyy-MM') || '';
      ts.monthAcceptLineCheckData.endMonth = formatDate(new Date(ts.monthAcceptTimeRange[1]), 'yyyy-MM') || '';
      request('', {
        url: ctx + 'org/efficiency/supervision/queryStatisticsByMonth',
        type: 'get',
        data: ts.monthAcceptLineCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            ts.handelMonthAcceptLineChartData(res.content);
          } else {
            ts.monthAcceptLineChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.monthAcceptLineChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.monthAcceptLineChart = '';
      });
    },
    // 月度事项受理-处理接口数据
    handelMonthAcceptLineChartData: function (handData) {
      var monthlyList = [], //月度list
        _acceptApplyList = [], //受理申报数list
        _completedApplyList = [], //办结申报数list
        _overTimeApplyList = [], //逾期申报数list
        _outScopeApply = []; //不予受理申报数list
      for (var i = 0; i < handData.length; i++) {
        monthlyList.push(handData[i].month);
        _acceptApplyList.push(handData[i].shouliCount);
        _completedApplyList.push(handData[i].banjieCount);
        _overTimeApplyList.push(handData[i].yuqiCount);
        _outScopeApply.push(handData[i].buyushouliCount);
      }
      assignEchartOptions.monthAcceptDeclareOpt.xAxis.data = monthlyList;
      assignEchartOptions.monthAcceptDeclareOpt.series[0].data = _acceptApplyList;
      assignEchartOptions.monthAcceptDeclareOpt.series[1].data = _completedApplyList;
      assignEchartOptions.monthAcceptDeclareOpt.series[2].data = _overTimeApplyList;
      assignEchartOptions.monthAcceptDeclareOpt.series[3].data = _outScopeApply;
      this.renderMonthAcceptBarChart();
    },
    // 月度申报受理统计-渲染echart
    renderMonthAcceptBarChart: function () {
      this.monthAcceptLineChart = echarts.init(document.getElementById('monthAcceptLineChart'));
      this.monthAcceptLineChart.setOption(assignEchartOptions.monthAcceptDeclareOpt);
    },

    //地区办件统计相关
    // 地区办件统计-时间范围控件变化
    areaDeclareTimeRangeChange: function (time) {
      this.areaDeclareBarChartTimeRange = time;
      // console.log(time)
      this.fetchAreaDeclareData();
    },
    //地区办件统计获取数据
    fetchAreaDeclareData: function () {
      var ts = this;
      ts.loading = true;
      ts.areaDeclareBarCheckData.startTime = ts.areaDeclareBarChartTimeRange[0] || '';
      ts.areaDeclareBarCheckData.endTime = ts.areaDeclareBarChartTimeRange[1] || '';
      request('', {
        url: ctx + 'org/efficiency/supervision/queryItemStatisticsByArea',
        type: 'get',
        data: ts.areaDeclareBarCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.conetent !== null) {
            ts.handelAreaDeclareData(res.content)
          } else {
            ts.areaDeclareBarChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.areaDeclareBarChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.areaDeclareBarChart = '';
      });
    },
    // 处理地区办件统计从后端获取的数据
    handelAreaDeclareData: function (handData) {
      var _title = [],
        _value = [],
        _maxBgList = [];
      for (var j = 0; j < handData.length; j++) {
        _title.push(handData[j].regionName)
        _value.push(handData[j].areaCount)
      }
      // 获取最大值，用于柱状图柱子背景的填充数据
      var _max = Math.max.apply(null, _value);
      for (var i = 0; i < _title.length; i++) {
        _maxBgList.push(_max);
      }
      assignEchartOptions.areaDeclareOpt.xAxis.data = _title;
      assignEchartOptions.areaDeclareOpt.series[0].data = _maxBgList;
      assignEchartOptions.areaDeclareOpt.series[1].data = _value;
      this.renderAreaDeclareBarChart();
    },
    //地区办件统计-渲染echart
    renderAreaDeclareBarChart: function () {
      this.areaDeclareBarChart = echarts.init(document.getElementById('areaDeclareBarChart'));
      this.areaDeclareBarChart.setOption(assignEchartOptions.areaDeclareOpt);
    },

    //项目并联审批统计相关
    //项目并联审批统计获取数据
    fetchprojectParallelApprovalData: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'org/efficiency/supervision/countItemForSeriesAndStage',
        type: 'get',
        data: ts.projectParallelApprovalCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            assignEchartOptions.projectParallelApprovalOpt.series[0].data = res.content;
            ts.renderprojectParallelApprovalPieChart();
          } else {
            ts.projectParallelApprovalPieChart = '';
          }

        } else {
          ts.apiMessage(res.message, 'error')
          ts.projectParallelApprovalPieChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.projectParallelApprovalPieChart = '';
      });
    },
    //项目并联审批统计-渲染echart
    renderprojectParallelApprovalPieChart: function () {
      this.projectParallelApprovalPieChart = echarts.init(document.getElementById('projectParallelApprovalPieChart'));
      this.projectParallelApprovalPieChart.setOption(assignEchartOptions.projectParallelApprovalOpt);
    },

    //申报来源统计相关
    //申报来源获取数据
    fetchDeclareSourceData: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'org/efficiency/supervision/countItemByApplyinstSource',
        type: 'get',
        data: ts.declareSourceCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            res.content[0].selected = true;
            assignEchartOptions.declareSourceOpt.series[0].data = res.content;
            ts.renderdeclareSourcePieChart();
          } else {
            ts.declareSourcePieChart = '';
          }

        } else {
          ts.apiMessage(res.message, 'error')
          ts.declareSourcePieChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.declareSourcePieChart = '';
      });
    },
    //申报来源统计-渲染echart
    renderdeclareSourcePieChart: function () {
      this.declareSourcePieChart = echarts.init(document.getElementById('declareSourcePieChart'));
      this.declareSourcePieChart.setOption(assignEchartOptions.declareSourceOpt);
    },

    // 事项办理异常排名相关
    // 事项办理异常排名获取接口
    fetchMatterHandelAbnormalTableListData: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'org/efficiency/supervision/queryItemExceptionStatistics',
        type: 'get',
        data: ts.matterHandelTableCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            ts.matterHandelTableList = res.content;
          } else {
            ts.matterHandelTableList = [];
          }

        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
      });
    },

    // 获取办件状态统计的主要内容
    fetchPieceStatueStatistics: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'org/efficiency/supervision/itemStateStatistics',
        type: 'get',
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          ts.statusCardData.forEach(function (item, index) {
            item.count = res.content[index].count;
          })
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
      });
    },

    // 头部状态统计点击事件
    selectStateForWorkbench: function (item) {
      var ts = this;
      // 左侧菜单栏-所有数据
      var _allNavList = parent.vm.leftData;
      var _index = -1; //当前工作台选中数据在菜单中的索引
      for (var i = 0, len = _allNavList.length; i < len; i++) {
        var _curViewUrl = (_allNavList[i].menuInnerUrl).replace(/\s/g, "");
        if (_curViewUrl && _curViewUrl.indexOf(item.jumpUrl) > -1) {
          _index = i;
          break;
        }
      }

      if (_index > -1) {
        parent.vm.addTab('', parent.vm.leftData[_index], parent.vm.activeTabIframe, _index);
      } else {
        var _jumpData = {
          'menuName': item.name,
          'menuInnerUrl': item.jumpUrl,
          'id': item.statusClass
        };
        parent.vm.addTab('', _jumpData, parent.vm.activeTabIframe, '');
      }

    },
  },
  mounted: function () {
    var ts = this
    window.onresize = throttle(function () {
      ts.render();
    }, 100, 500, 100)
    // ts.renderDepartAcceptBarChart();
    // ts.renderMonthAcceptBarChart();
    // ts.renderAreaDeclareBarChart();

    // ts.renderprojectParallelApprovalPieChart();
    // ts.renderdeclareSourcePieChart();
  },
  created: function () {
    this.init();
  },
});