var app = new Vue({
  el: '#singleAssEffect',
  mixins: [mixins],
  data: {
    // 卡片list
    statusCardData: [{
        name: '容缺受理件',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'waring-status',
        jumpUrl: 'efficacyJump/page/queryItemToleranceAcceptIndex.html?entrust=true' //跳转模块路径
      },
      {
        name: '待补正件',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'waring-correct',
        jumpUrl: 'efficacyJump/page/queryNeedCorrectTasksIndex.html?entrust=true' //跳转模块路径
      },
      {
        name: '补正待确认件',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'alerdy-correct',
        jumpUrl: 'efficacyJump/page/queryUnConfirmItemCorrectTasksIndex.html?entrust=true' //跳转模块路径
      },
      {
        name: '特殊程序件',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'not-shouli',
        jumpUrl: 'efficacyJump/page/querySpecialProcedureTasksIndex.html?entrust=true' //跳转模块路径
      },
      {
        name: '容缺通过件',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'time-yujing',
        jumpUrl: 'efficacyJump/page/queryItemAgreeToleranceIndex.html?entrust=true' //跳转模块路径
      },
      {
        name: '办理不通过件',
        count: 0,
        linkRelativeRatio: '', //环比
        onYearonyearBasis: '', //同比
        statusClass: 'time-yuqi',
        jumpUrl: 'efficacyJump/page/queryItemDisgreeIndex.html?entrust=true' //跳转模块路径
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
      isCurrent: true,
      orgId: ''
    },
    // 时间范围
    pageDateRange: culatorCurMonthDateRange() || [],

    // 部门接件量
    // 部门接件量-饼图
    departAcceptPieChart: "",

    // 事项受理情况
    // 事项受理情况-表格
    itemHandelTableList: [],

    // 部门接件历史分析
    // 部门接件历史分析-折线图
    accepHistoryEffectLineChart: '',
    // 部门接件历史分析-切换窗口的数据-总的数据
    accepConnectionWinAllList: [],
    // 部门接件历史分析-切换窗口的数据-当前第几页
    accepConnectionWinPage: 1,
    // 部门接件历史分析-切换窗口的数据-当前页数据(定好了 7 条数据)
    accepConnectionWinList: [],
  },
  methods: {
    // 页面echart渲染resize
    render: function () {
      this.departAcceptPieChart && this.departAcceptPieChart.resize();
      this.accepHistoryEffectLineChart && this.accepHistoryEffectLineChart.resize();
    },

    // 获取头部卡片数据
    fetchApplyStatistics: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'org/efficiency/supervision/itemStateStatisticsByOrg',
        type: 'get',
        data: {
          isCurrent:true,
          orgId: ''
        }
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
      this.fetchDepartAcceptPieChartData();
      this.fetchDepartAccepHistoryEffectWinList();
    },

    // 部门接件量-获取后端数据
    fetchDepartAcceptPieChartData: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'org/efficiency/supervision/getOrgItemStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            _dacceptSum = res.content.total;
            ts.itemHandelTableList = res.content.data;
            ts.handelDepartAcceptPieChartData(res.content.data);
          } else {
            ts.departAcceptPieChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.departAcceptPieChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.departAcceptPieChart = '';
      });
    },
    // 部门接件量-处理后端数据
    handelDepartAcceptPieChartData: function (list) {
      if (!list || !list.length) return;
      singleAssChartOptions.departAcceptPieChartOpt.legend.data = [];
      singleAssChartOptions.departAcceptPieChartOpt.series[0].data = [];
      for (var i = 0; i < list.length; i++) {
        // 根据itemName生成颜色值
        var colorHash = new ColorHash();
        var hex = colorHash.hex(list[i].itemName)

        var opt = list[i].itemName;
        _dacceptSumObj[opt] = {
          num: '',
          percent: ''
        }
        _dacceptSumObj[opt]['percent'] = (Number(list[i].receiptCount / _dacceptSum) * 100).toFixed(0) + '%';
        _dacceptSumObj[opt]['num'] = list[i].receiptCount;

        singleAssChartOptions.departAcceptPieChartOpt.legend.data.push(opt);
        singleAssChartOptions.departAcceptPieChartOpt.series[0].data.push({
          name: list[i].itemName || '',
          value: list[i].receiptCount || '',
          itemColor: hex || '' //加上自定义的颜色
        })
      }
      singleAssChartOptions.departAcceptPieChartOpt.series[0].data[0].label = _doneLabel;
      
      this.renderDepartAcceptPieChart();
    },
    // 部门接件量-渲染echrt
    renderDepartAcceptPieChart: function () {
      this.departAcceptPieChart = echarts.init(document.getElementById('departAcceptPieChart'));
      this.departAcceptPieChart.setOption(singleAssChartOptions.departAcceptPieChartOpt);
    },

    // 获取部门接件历史分析的窗口数据
    fetchDepartAccepHistoryEffectWinList: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'org/efficiency/supervision/getOrgItemAcceptStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            ts.handelAccepConnectionEffectWinList(res.content)
          }
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
      });
    },
    // 处理部门接件历史分析窗口数据
    handelAccepConnectionEffectWinList: function (list) {
      if(list && !list.length){
        return;
      }
      this.accepConnectionWinAllList = list.map(function (item) {
        return {
          winId: item.itemId,  //事项id
          winName: item.itemName,  //事项名称
          apply: item.apply, //接件数
          acceptDeal: item.acceptDeal, //已受理数
          // percent: item.acceptDealRate, //受理率
          // percentNum: Number(item.acceptDealRate.replace('%', '')) || 0, //受理率（纯百分比）
          select: false //是否选中状态
        }
      })
      // 截取前七个用来显示
      this.accepConnectionWinList = this.accepConnectionWinAllList.slice(0, 7)
      // 加载第一个窗口的统计数据
      this.fetchSelectedWinAccepConnectionEffectData(this.accepConnectionWinAllList[0]);
    },
    // 高亮当前选中的窗口
    activeCurWinItem: function (obj) {
      this.accepConnectionWinAllList.forEach(function (item) {
        item.select = false;
      })
      obj.select = true;
    },
    // 获取当前选中窗口的统计数据
    fetchSelectedWinAccepConnectionEffectData: function (obj) {
      this.activeCurWinItem(obj); //高亮当前窗口
      var ts = this,
        _getData = {};
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      _getData.startTime = ts.pageSearchData.startTime;
      _getData.endTime = ts.pageSearchData.endTime;
      _getData.type = ts.pageSearchData.type;
      _getData.itemId = obj.winId;
      request('', {
        url: ctx + 'org/efficiency/supervision/getOrgItemAcceptHistoryStatistics',
        type: 'get',
        data: _getData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            ts.handelAccepHistoryEffectLineChartData(res.content);
          } else {
            ts.accepHistoryEffectLineChart = "";
          }
        } else {
          ts.apiMessage(res.message, 'error');
          ts.accepHistoryEffectLineChart = "";
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error');
        ts.accepHistoryEffectLineChart = "";
      });
    },

    // 部门接件历史-处理后端数据
    handelAccepHistoryEffectLineChartData: function(handData){
      var dateList = [], //日期数据list
      _acceptApplyList = [], //已接件list
      _completedApplyList = []; //已受理list
      for (var i = 0; i < handData.length; i++) {
        dateList.push(handData[i].day);
        _acceptApplyList.push(handData[i].apply);
        _completedApplyList.push(handData[i].acceptDeal);
      }
      singleAssChartOptions.accepHistoryEffectLineChartOpt.xAxis.data = dateList;
      singleAssChartOptions.accepHistoryEffectLineChartOpt.series[0].data = _acceptApplyList;
      singleAssChartOptions.accepHistoryEffectLineChartOpt.series[1].data = _completedApplyList;
      this.renderAccepHistoryEffectLineChart();
    },

    // 部门接件历史分析窗口左右控制
    winHandelToRight: function () {
      var ts = this,
        _all = this.accepConnectionWinAllList,
        _list = this.accepConnectionWinList,
        _page = this.accepConnectionWinPage,
        _maxPage = "";
      _maxPage = Math.floor(_all.length / 7);
      this.accepConnectionWinList = _all.slice((_page) * 7, (_page + 1) * 7);
      if (_page > 0 && _page <= _maxPage) {
        _page++;
      }
      this.accepConnectionWinPage = _page;
    },
    winHandelToLeft: function () {
      var ts = this,
        _all = this.accepConnectionWinAllList,
        _list = this.accepConnectionWinList,
        _page = this.accepConnectionWinPage,
        _maxPage = "";
      _maxPage = Math.floor(_all.length / 7);
      // debugger
      this.accepConnectionWinList = _all.slice((_page - 2) * 7, (_page - 1) * 7);
      if (_page >= 2) {
        _page--;
      }
      this.accepConnectionWinPage = _page;
    },

    // 接件历史分析-渲染echart
    renderAccepHistoryEffectLineChart: function () {
      this.accepHistoryEffectLineChart = echarts.init(document.getElementById('accepHistoryEffectLineChart'));
      this.accepHistoryEffectLineChart.setOption(singleAssChartOptions.accepHistoryEffectLineChartOpt);
    },
  },
  mounted: function () {
    var ts = this
    window.onresize = throttle(function () {
      ts.render();
    }, 100, 500, 100)
    // this.renderDepartAcceptPieChart();
    // this.renderAccepHistoryEffectLineChart();
  },
  created: function () {
    this.fetchApplyStatistics();
    this.initEffectApi();

    // test
    // for (var i = 0; i < 15; i++) {
    //   this.accepConnectionWinAllList.push({
    //     winName: '窗口' + i,
    //     select: false,
    //     winId: i,
    //   })
    // }
    // this.accepConnectionWinList = this.accepConnectionWinAllList.slice(0, 7)
  },
  computed: {
    isLeft: function () {
      var _crP = this.accepConnectionWinPage;
      if (_crP < 2) {
        return false;
      }
      return true;
    },
    isRight: function () {
      var _crP = this.accepConnectionWinPage;
      if (_crP * 7 >= this.accepConnectionWinAllList.length) {
        return false;
      }
      return true;
    },
  },
})