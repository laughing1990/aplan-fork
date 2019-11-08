var app = new Vue({
  el: '#singleWinEffect',
  mixins: [mixins],
  data: {
    // 卡片list
    statusCardData: [{
        name: '网上待预审',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'waring-status',
        jumpUrl: 'win/efficiency/supervision/queryPreliminaryTasksIndex.html?entrust=y' //跳转模块路径
      },
      {
        name: '待补全申报',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'waring-correct',
        jumpUrl: 'win/efficiency/supervision/queryNeedCompletedApplyIndex.html?entrust=y' //跳转模块路径
      },
      {
        name: '补全待确认申报',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'alerdy-correct',
        jumpUrl: 'win/efficiency/supervision/queryNeedConfirmCompletedApplyIndex.html?entrust=y' //跳转模块路径
      },
      {
        name: '不予受理',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'not-shouli',
        jumpUrl: 'win/efficiency/supervision/queryDismissedApplyIndex.html?entrust=y' //跳转模块路径
      },
      {
        name: '申报时限预警',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'time-yujing',
        jumpUrl: 'win/efficiency/supervision/queryWarnApplyIndex.html?entrust=y' //跳转模块路径
      },
      {
        name: '申报时限逾期',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'time-yuqi',
        jumpUrl: 'win/efficiency/supervision/queryOverdueApplyIndex.html?entrust=y' //跳转模块路径
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
        }
      ]
    },
    // 整个页面的督查的查询数据
    pageSearchData: {
      startTime: '',
      endTime: '',
      type: 'M',
    },
    // 时间范围
    pageDateRange: culatorCurMonthDateRange() || [],

    // 接件受理情况
    // 接件受理情况-饼图
    acceptHandelPieChart: "",

    // 阶段受理情况
    // 阶段受理情况-柱状图
    stageHandelBarChart: '',

    // 接件受理率
    // 接件受理率-折线图
    accepHistoryEffectLineChart: '',
  },
  methods: {
    // 页面echart渲染resize
    render: function () {
      this.acceptHandelPieChart && this.acceptHandelPieChart.resize();
      this.stageHandelBarChart && this.stageHandelBarChart.resize();
      this.accepHistoryEffectLineChart && this.accepHistoryEffectLineChart.resize();
    },

    // 获取头部卡片数据
    fetchApplyStatistics: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'win/efficiency/supervision/getCurWinShenbaoStatistics',
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
    // 头部卡片点击事件
    selectStateForWorkbench: function (item) {
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

    // 页面时间范围筛选改变-触发整个效能督查的查询数据条件
    pageDateRangeChange: function (range) {
      this.pageSearchData.type = ""; //自定义时间时，清除掉时间段激活状态
      this.pageDateRange = range || culatorCurMonthDateRange() || ['', ''];
      this.initEffectApi();
    },
    searchPageEffectDataForTimeRange: function (type) {
      if (type == 'D') {
        this.pageDateRange = culatorYesterDateRange() || ['', ''];
      } else if (type == 'W') {
        this.pageDateRange = culatorCurWeekDateRange() || ['', ''];
      } else {
        this.pageDateRange = culatorCurMonthDateRange() || ['', '']
      }
      this.pageSearchData.type = type;
      this.initEffectApi();
    },

    // 初始化页面接口
    initEffectApi: function () {
      this.fetchAcceptHandelPieChartData();
      this.fetchStageHandelBarChartData();
      this.fetchAccepHistoryEffectLineChartData();
    },

    // 接件受理情况-获取后端接口
    fetchAcceptHandelPieChartData: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getCurrentWinAcceptStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            ts.handelAcceptHandelPieChartData(res)
          } else {
            ts.acceptHandelPieChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.acceptHandelPieChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.acceptHandelPieChart = '';
      });
    },
    // 接件受理情况-处理后端接口数据
    handelAcceptHandelPieChartData: function (res) {
      // _acceptHandelPieSum = res.content.total;
      singleWinChartOptions.acceptHandelPieChartOpt.title.text = "窗口接件总数：" + res.content.total
      singleWinChartOptions.acceptHandelPieChartOpt.series[0].data = res.content.data;
      this.renderAcceptHandelPieChart();
    },
    // 接件受理情况-渲染echrt
    renderAcceptHandelPieChart: function () {
      this.acceptHandelPieChart = echarts.init(document.getElementById('acceptHandelPieChart'));
      this.acceptHandelPieChart.setOption(singleWinChartOptions.acceptHandelPieChartOpt);
    },

    // 阶段受理情况-获取后端数据
    fetchStageHandelBarChartData: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getCurrentWinStageAcceptStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            ts.handelStageHandelBarChartData(res)
          } else {
            ts.stageHandelBarChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.stageHandelBarChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.stageHandelBarChart = '';
      });
    },
    // 阶段受理情况-处理后端数据
    handelStageHandelBarChartData: function (res) {
      singleWinChartOptions.stageHandelBarChartOpt.xAxis.data = res.content.title;
      singleWinChartOptions.stageHandelBarChartOpt.series[0].data = res.content.yiShouLi;
      singleWinChartOptions.stageHandelBarChartOpt.series[1].data = res.content.caiLiaoBuQuan;
      singleWinChartOptions.stageHandelBarChartOpt.series[2].data = res.content.buYuShouLi;
      if (res.content.title.length < 10) {
        singleWinChartOptions.stageHandelBarChartOpt.dataZoom = [];
        singleWinChartOptions.stageHandelBarChartOpt.grid.y2 = 20;
      }
      this.renderStageHandelBarChart();
    },
    // 阶段受理情况-渲染echart
    renderStageHandelBarChart: function () {
      this.stageHandelBarChart = echarts.init(document.getElementById('stageHandelBarChart'));
      this.stageHandelBarChart.setOption(singleWinChartOptions.stageHandelBarChartOpt);
    },

    // 接件历史分析-获取后端数据
    fetchAccepHistoryEffectLineChartData: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getCurWinAcceptStatisticsByDay',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            ts.handelAccepHistoryEffectLineChartData(res.content)
          } else {
            ts.stageHandelBarChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.stageHandelBarChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.stageHandelBarChart = '';
      });
    },
    // 接件历史分析-处理后端数据
    handelAccepHistoryEffectLineChartData: function (list) {
      var _dayList = [], //时间段
        _applyList = [], //  已接件
        _acceptDealList = []; //已受理
      for (var i = 0, ln = list.length; i < ln; i++) {
        _dayList.push(list[i].day);
        _applyList.push(list[i].apply);
        _acceptDealList.push(list[i].acceptDeal);
      }
      singleWinChartOptions.accepHistoryEffectLineChartOpt.xAxis.data = _dayList;
      singleWinChartOptions.accepHistoryEffectLineChartOpt.series[0].data = _applyList;
      singleWinChartOptions.accepHistoryEffectLineChartOpt.series[1].data = _acceptDealList;
      this.renderAccepHistoryEffectLineChart();
    },
    // 接件历史分析-渲染echart
    renderAccepHistoryEffectLineChart: function () {
      this.accepHistoryEffectLineChart = echarts.init(document.getElementById('accepHistoryEffectLineChart'));
      this.accepHistoryEffectLineChart.setOption(singleWinChartOptions.accepHistoryEffectLineChartOpt);
    },
  },
  mounted: function () {
    var ts = this
    window.onresize = throttle(function () {
      ts.render();
    }, 100, 500, 100)
    // this.renderAcceptHandelPieChart();
    // this.renderStageHandelBarChart();
    // this.renderAccepHistoryEffectLineChart();
  },
  created: function () {
    this.fetchApplyStatistics();
    this.initEffectApi();
  },
})