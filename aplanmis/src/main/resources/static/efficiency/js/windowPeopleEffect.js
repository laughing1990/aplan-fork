// 定义一天的秒数
var oneDaySeconds = 3600 * 1000 * 24;


var pager = new Vue({
  el: '#effectSupervisionWindows',
  mixins: [mixins],
  data: function () {
    return {
      // 申报统计list
      statusCardData: [{
          name: '网上待预审',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'waring-status',
          jumpUrl: 'win/efficiency/supervision/queryPreliminaryTasksIndex.html'//跳转模块路径
        },
        {
          name: '待补全申报',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'waring-correct',
          jumpUrl: 'win/efficiency/supervision/queryNeedCompletedApplyIndex.html'//跳转模块路径
        },
        {
          name: '补全待确认申报',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'alerdy-correct',
          jumpUrl: 'win/efficiency/supervision/queryNeedConfirmCompletedApplyIndex.html'//跳转模块路径
        },
        {
          name: '不予受理',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'not-shouli',
          jumpUrl: 'win/efficiency/supervision/queryDismissedApplyIndex.html'//跳转模块路径
        },
        {
          name: '申报时限预警',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'time-yujing',
          jumpUrl: 'win/efficiency/supervision/queryWarnApplyIndex.html'//跳转模块路径
        },
        {
          name: '申报时限逾期',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'time-yuqi',
          jumpUrl: 'win/efficiency/supervision/queryOverdueApplyIndex.html'//跳转模块路径
        }
      ],

      // el-日期控件快捷键
      pickerOptions: {
        shortcuts: [{
            text: '今天',
            onClick: function (picker) {
              var date = new Date()
              var end = formatDate(date, 'yyyy-MM-dd')
              var start = date.getTime() - oneDaySeconds;
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

      // 窗口申报受理统计相关
      // 窗口申报受理统计柱状图实例
      windowAcceptBarChart: '',
      // 窗口申报受理统计-时间筛选器
        windowAcceptTimeRange: culatorCurMonthDateRange() || [],
      // 窗口申报受理统计的查询参数
      windowAcceptBarCheckData: {
        startTime: '',
        endTime: ''
      },
      // 窗口申报受理统计的表格数据
      windowAcceptTableList: [],

      // 月度申报受理统计相关
      // 月度申报受理统计线状图实例
      monthAcceptLineChart: '',
      //月度申报受理统计-时间筛选器
      monthAcceptTimeRange: culatorCurYearDateRage() || [],
      // 月度申报受理统计的查询参数
      monthAcceptLineCheckData: {
        startTime: '',
        endTime: ''
      },

      // 地区申报统计相关
      // 地区申报统计柱状图实例
      areaDeclareBarChart: '',
      // 地区申报统计-时间范围筛选
      areaDeclareBarChartTimeRange: culatorCurMonthDateRange() || [],
      // 地区申报统计的查询参数
      areaDeclareBarCheckData: {
        startTime: '',
        endTime: ''
      },

      // 主题申报统计相关
      // 主题申报统计饼状图实例
      themeDeclarePieChart: '',
      // 主题申报统计的查询参数
      themeDeclarePieCheckData: {
        startTime: '',
        endTime: ''
      },
      // 主题、阶段申报时间筛选
      themeAndStageDeclareTimeRange: culatorCurMonthDateRange() || [],
      // 阶段申报统计相关
      // 阶段申报统计饼状图实例
      stageDeclarePieChart: '',
      // 主题申报统计的查询参数
      stageDeclareCheckData: {
        startTime: '',
        endTime: ''
      },

      // 主题申报异常排名相关
      // 主题申报异常排名-表格数据
      themeDeclareTableList: [],
      // 主题申报异常排名-表格数据
      themeDeclareTableCheckData: {},

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
      this.fetchWindowAcceptData();
      this.fetchMonthAcceptData();
      this.fetchAreaDeclareData();
      this.fetchThemeDeclareData();
      this.fetchStageDeclareData();
      this.fetchthemeDeclareTableListData();
      this.fetchprojectParallelApprovalData();
      this.fetchDeclareSourceData();
      this.fetchApplyStatistics();
    },
    // 页面echart渲染resize
    render: function () {
      this.windowAcceptBarChart && this.windowAcceptBarChart.resize();
      this.monthAcceptLineChart && this.monthAcceptLineChart.resize();
      this.areaDeclareBarChart && this.areaDeclareBarChart.resize();
      this.themeDeclarePieChart && this.themeDeclarePieChart.resize();
      this.stageDeclarePieChart && this.stageDeclarePieChart.resize();
      this.projectParallelApprovalPieChart && this.projectParallelApprovalPieChart.resize();
      this.declareSourcePieChart && this.declareSourcePieChart.resize();
    },

    //窗口申报受理统计相关
    // 窗口申报受理统计-时间范围控件变化
    windowAcceptTimeRangeChange: function (time) {
      this.windowAcceptTimeRange = time;
      console.log(time)
      this.fetchWindowAcceptData();
    },
    // 获取窗口申报受理统计数据
    fetchWindowAcceptData: function () {
      var ts = this;
      ts.loading = true;
      ts.windowAcceptBarCheckData.startTime = ts.windowAcceptTimeRange[0] || '';
      ts.windowAcceptBarCheckData.endTime = ts.windowAcceptTimeRange[1] || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getWinShouliStatistics',
        type: 'get',
        data: ts.windowAcceptBarCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null && res.content.length) {
            ts.windowAcceptTableList = res.content;
            ts.handelWindowAcceptBarChartData(res.content);
          } else {
            ts.windowAcceptBarChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.windowAcceptBarChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.windowAcceptBarChart = '';
      });
    },
    // 窗口申报受理统计-柱状图处理获取到的数据
    handelWindowAcceptBarChartData: function (handData) {
      var leftData = {
        title: [],
        acceptCount: [],
        notAcceptCount: [],
      };
      for (var i = 0; i < handData.length; i++) {
        leftData.title.push(handData[i].windowName);
        leftData.acceptCount.push(handData[i].applyCount)
        leftData.notAcceptCount.push(handData[i].buyushouliCount)
      }

      var _maxBgList = [];
      // 获取最大值，用于柱状图柱子背景的填充数据
      var _max = Math.max.apply(null, leftData.acceptCount);
      if(0<_max<100){
        _max = Math.ceil(_max/10) * 10
      }else if(100<_max<1000){
        _max = Math.ceil(_max/100) * 100
      }else if(1000<_max<10000){
        _max = Math.ceil(_max/1000) * 1000
      }
      for (var i = 0; i < leftData.title.length; i++) {
        _maxBgList.push(_max);
      }
      // console.log(leftData)
      // 根据接口数据的长度来动态修改zoom功能的范围值
      var _len = handData.length;
      if(_len > 40){
        windowEchartOptions.windowAcceptDeclareOpt.dataZoom[0].end = 8
      }else if(_len >60){
        windowEchartOptions.windowAcceptDeclareOpt.dataZoom[0].end = 4
      }

      windowEchartOptions.windowAcceptDeclareOpt.xAxis.data = leftData.title;
      windowEchartOptions.windowAcceptDeclareOpt.series[0].data = _maxBgList;
      windowEchartOptions.windowAcceptDeclareOpt.series[1].data = leftData.acceptCount;
      windowEchartOptions.windowAcceptDeclareOpt.series[2].data = leftData.notAcceptCount;
      this.renderWindowAcceptBarChart();
    },
    // 窗口申报受理统计-渲染echart
    renderWindowAcceptBarChart: function () {
      var ts = this;
      this.windowAcceptBarChart = echarts.init(document.getElementById('windowAcceptBarChart'));
      // console.log(this.windowAcceptBarChart)
      this.windowAcceptBarChart.setOption(windowEchartOptions.windowAcceptDeclareOpt);
    },

    //月度申报受理统计相关
    //月度申报受理时间切换
    monthAcceptTimeRangeChange: function (time) {
      this.monthAcceptTimeRange = time;
      this.fetchMonthAcceptData();
    },
    //获取月度申报受理统计数据
    fetchMonthAcceptData: function () {
      var ts = this;
      ts.loading = true;
      ts.monthAcceptLineCheckData.startTime = formatDate(new Date(ts.monthAcceptTimeRange[0]), 'yyyy-MM') || '';
      ts.monthAcceptLineCheckData.endTime = formatDate(new Date(ts.monthAcceptTimeRange[1]), 'yyyy-MM') || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getWinShouliStatisticsByMonth',
        type: 'get',
        data: ts.monthAcceptLineCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null && res.content.length) {
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
    // 月度申报受理-处理接口数据
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
      windowEchartOptions.monthAcceptDeclareOpt.xAxis.data = monthlyList;
      windowEchartOptions.monthAcceptDeclareOpt.series[0].data = _acceptApplyList;
      windowEchartOptions.monthAcceptDeclareOpt.series[1].data = _completedApplyList;
      windowEchartOptions.monthAcceptDeclareOpt.series[2].data = _overTimeApplyList;
      windowEchartOptions.monthAcceptDeclareOpt.series[3].data = _outScopeApply;
      this.renderMonthAcceptBarChart();
    },
    // 月度申报受理统计-渲染echart
    renderMonthAcceptBarChart: function () {
      this.monthAcceptLineChart = echarts.init(document.getElementById('monthAcceptLineChart'));
      this.monthAcceptLineChart.setOption(windowEchartOptions.monthAcceptDeclareOpt);
    },

    //地区申报统计相关
    // 地区申报统计-时间范围控件变化
    areaDeclareTimeRangeChange: function (time) {
      this.areaDeclareBarChartTimeRange = time;
      console.log(time)
      this.fetchAreaDeclareData();
    },
    //地区申报统计获取数据
    fetchAreaDeclareData: function () {
      var ts = this;
      ts.loading = true;
      ts.areaDeclareBarCheckData.startTime = ts.areaDeclareBarChartTimeRange[0] || '';
      ts.areaDeclareBarCheckData.endTime = ts.areaDeclareBarChartTimeRange[1] || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getRegionShenbaoStatistics',
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
    // 处理地区申报统计从后端获取的数据
    handelAreaDeclareData: function (handData) {
      var value = [],
        title = [];
      for (var i = 0; i < handData.length; i++) {
        value.push(handData[i].applyCount || '0');
        title.push(handData[i].regionName);
      }
      var _maxBgList = [];
      // 获取最大值，用于柱状图柱子背景的填充数据
      var _max = Math.max.apply(null, value);
      for (var i = 0; i < title.length; i++) {
        _maxBgList.push(_max);
      }
      windowEchartOptions.areaDeclareOpt.xAxis.data = title;
      windowEchartOptions.areaDeclareOpt.series[0].data = _maxBgList;
      windowEchartOptions.areaDeclareOpt.series[1].data = value;
      this.renderAreaDeclareBarChart();
    },
    //地区申报统计-渲染echart
    renderAreaDeclareBarChart: function () {
      this.areaDeclareBarChart = echarts.init(document.getElementById('areaDeclareBarChart'));
      this.areaDeclareBarChart.setOption(windowEchartOptions.areaDeclareOpt);
    },

    // 主题申报-阶段申报-时间范围改变
    themeAndStageApproveTimeRangeChange: function (time) {
      this.themeAndStageDeclareTimeRange = time;
      this.fetchThemeDeclareData();
      this.fetchStageDeclareData();
    },
    //主题申报统计相关
    // 获取主题申报统计数据
    fetchThemeDeclareData: function () {
      var ts = this;
      ts.loading = true;
      ts.themeDeclarePieCheckData.startTime = ts.themeAndStageDeclareTimeRange[0] || '';
      ts.themeDeclarePieCheckData.endTime = ts.themeAndStageDeclareTimeRange[1] || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getThemeShenbaoStatistics',
        type: 'get',
        data: ts.themeDeclarePieCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.conetent !== null) {
            ts.handelThemeDeclareData(res.content)
          } else {
            ts.themeDeclarePieChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.themeDeclarePieChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.themeDeclarePieChart = '';
      });
    },
    // 处理接口主题申报统计数据
    handelThemeDeclareData: function (handData) {
      _themeAcceptSum = handData.total;
      var _allTheme = handData.applyStatistics;
      windowEchartOptions.themeDeclareOpt.legend.data = [];
      windowEchartOptions.themeDeclareOpt.series[0].data = [];
      for (var i = 0; i < _allTheme.length; i++) {
        var obj = _allTheme[i];
        var attr = obj.name;
        _themeAcceptObj[attr] = {};
        _themeAcceptObj[attr].percent = obj.precent
        _themeAcceptObj[attr].num = obj.count

        windowEchartOptions.themeDeclareOpt.legend.data.push(attr)
        // windowEchartOptions.stageDeclareOpt.series[0].data[i].value = obj.count;
        windowEchartOptions.themeDeclareOpt.series[0].data.push({});
        windowEchartOptions.themeDeclareOpt.series[0].data[i].name = attr;
        windowEchartOptions.themeDeclareOpt.series[0].data[i].value = obj.count;
      }
      console.log(windowEchartOptions.themeDeclareOpt)
      windowEchartOptions.themeDeclareOpt.series[0].data[0].label = _themeAcceptSumLabel;
      this.renderThemeDeclarePieChart();
    },
    //主题申报统计-渲染echart
    renderThemeDeclarePieChart: function () {
      this.themeDeclarePieChart = echarts.init(document.getElementById('themeDeclarePieChart'));
      this.themeDeclarePieChart.setOption(windowEchartOptions.themeDeclareOpt);
    },

    //阶段申报统计相关
    //获取阶段申报统计数据
    fetchStageDeclareData: function () {
      var ts = this;
      ts.loading = true;
      ts.stageDeclareCheckData.startTime = ts.themeAndStageDeclareTimeRange[0] || '';
      ts.stageDeclareCheckData.endTime = ts.themeAndStageDeclareTimeRange[1] || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getStageShenbaoStatistics',
        type: 'get',
        data: ts.stageDeclareCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.conetent !== null) {
            ts.handelStageDeclareData(res.content)
          } else {
            ts.stageDeclarePieChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.stageDeclarePieChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.stageDeclarePieChart = '';
      });
    },
    // 处理接口阶段申报统计数据
    handelStageDeclareData: function (handData) {
      _stageAcceptSum = handData.total;
      var _allStage = handData.applyStatistics;
      windowEchartOptions.stageDeclareOpt.legend.data = [];
      windowEchartOptions.stageDeclareOpt.series[0].data = [];
      for (var i = 0; i < _allStage.length; i++) {
        var obj = _allStage[i];
        var attr = obj.name;
        _stageAcceptObj[attr] = {};
        _stageAcceptObj[attr].percent = obj.precent
        _stageAcceptObj[attr].num = obj.count

        windowEchartOptions.stageDeclareOpt.legend.data.push(attr)
        windowEchartOptions.stageDeclareOpt.series[0].data.push({});
        windowEchartOptions.stageDeclareOpt.series[0].data[i].name = attr;
        windowEchartOptions.stageDeclareOpt.series[0].data[i].value = obj.count;
      }
      windowEchartOptions.stageDeclareOpt.series[0].data[0].label = _stageAcceptSumLabel;
      this.renderStageDeclarePieChart();
    },
    //阶段申报统计-渲染echart
    renderStageDeclarePieChart: function () {
      this.stageDeclarePieChart = echarts.init(document.getElementById('stageDeclarePieChart'));
      this.stageDeclarePieChart.setOption(windowEchartOptions.stageDeclareOpt);
    },

    //项目并联审批统计相关
    //项目并联审批统计获取数据
    fetchprojectParallelApprovalData: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'win/efficiency/supervision/getProjStatisticsByApplyType',
        type: 'get',
        data: ts.projectParallelApprovalCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            windowEchartOptions.projectParallelApprovalOpt.series[0].data = res.content;
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
      this.projectParallelApprovalPieChart.setOption(windowEchartOptions.projectParallelApprovalOpt);
    },

    //申报来源统计相关
    //申报来源获取数据
    fetchDeclareSourceData: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'win/efficiency/supervision/getShenbaoStatisticsByApplySource',
        type: 'get',
        data: ts.declareSourceCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            res.content[0].selected = true;
            windowEchartOptions.declareSourceOpt.series[0].data = res.content;
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
      this.declareSourcePieChart.setOption(windowEchartOptions.declareSourceOpt);
    },

    // 主题申报异常排名相关
    // 主题申报异常排名获取接口
    fetchthemeDeclareTableListData: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'win/efficiency/supervision/getThemShenbaoExceptionStatistics',
        type: 'get',
        data: ts.themeDeclareTableCheckData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content !== null) {
            ts.themeDeclareTableList = res.content;
          } else {
            ts.themeDeclareTableList = [];
          }

        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
      });
    },

    // 获取申报统计的主要内容
    fetchApplyStatistics: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'win/efficiency/supervision/getWinShenbaoStatistics',
        type: 'get',
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          ts.statusCardData.forEach(function (item, index) {
            item.count = res.content[index].count;
            item.onYearonyearBasis = res.content[index].onYearonyearBasis;
            item.linkRelativeRatio = res.content[index].linkRelativeRatio;
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
    // ts.renderWindowAcceptBarChart();
    // ts.renderMonthAcceptBarChart();
    // ts.renderAreaDeclareBarChart();

    // ts.renderThemeDeclarePieChart();
    // ts.renderStageDeclarePieChart();

    // ts.renderprojectParallelApprovalPieChart();
    // ts.renderdeclareSourcePieChart();
  },
  created: function () {
    this.init();
  },
});