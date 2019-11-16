var app = new Vue({
  el: '#assEffect',
  mixins: [mixins],
  data: function () {
    return {
      // 卡片list
      statusCardData: [{
          name: '容缺受理件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'waring-status',
          jumpUrl: 'efficacyJump/page/queryItemToleranceAcceptIndex.html' //跳转模块路径
        },
        {
          name: '待补正件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'waring-correct',
          jumpUrl: 'efficacyJump/page/queryNeedCorrectTasksIndex.html' //跳转模块路径
        },
        {
          name: '补正待确认件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'alerdy-correct',
          jumpUrl: 'efficacyJump/page/queryUnConfirmItemCorrectTasksIndex.html' //跳转模块路径
        },
        {
          name: '特殊程序件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'not-shouli',
          jumpUrl: 'efficacyJump/page/querySpecialProcedureTasksIndex.html' //跳转模块路径
        },
        {
          name: '容缺通过件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'time-yujing',
          jumpUrl: 'efficacyJump/page/queryItemAgreeToleranceIndex.html' //跳转模块路径
        },
        {
          name: '办理不通过件',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'time-yuqi',
          jumpUrl: 'efficacyJump/page/queryItemDisgreeIndex.html' //跳转模块路径
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

      // 当前展示的是哪一个督查模块  1:全部   2:工作量及有效性专题   3: 并联专题   4: 时限分析
      curEffectModule: 2,

      // 整个页面的督查的查询数据
      pageSearchData: {
        startTime: '',
        endTime: '',
        type: 'M',
        regionId: '',  //区划id
      },
      // 当前部门所在省市下面的区域
      regionList: [{regionId: '123', regionName: '天河区'},{regionId: '13', regionName: '番禺区'},],
      // 时间范围
      pageDateRange: culatorCurMonthDateRange() || [],

      // 接件总量
      // 接件总量-饼图
      acceptSumPieChart: "",

      // 接件受理情况
      // 接件受理情况-柱状图
      accepConnectionBarChart: '',

      // 接件受理率
      // 接件受理率-折线图
      accepConnectionEffectLineChart: '',
      // 接件受理率-切换窗口的数据-总的数据
      accepConnectionWinAllList: [],
      // 接件受理率-切换窗口的数据-当前第几页
      accepConnectionWinPage: 1,
      // 接件受理率-切换窗口的数据-当前页数据(定好了 7 条数据)
      accepConnectionWinList: [],

    }
  },
  methods: {
    // 页面echart渲染resize
    render: function () {
      // 有效性专题
      this.acceptSumPieChart && this.acceptSumPieChart.resize();
      this.accepConnectionBarChart && this.accepConnectionBarChart.resize();
      this.accepConnectionEffectLineChart && this.accepConnectionEffectLineChart.resize();
    },

    // 获取头部卡片数据
    fetchApplyStatistics: function () {
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
    // 获取各区划的下拉列表
    fetchRegionList: function(){
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'org/efficiency/supervision/itemStateStatistics',
        type: 'get',
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          ts.regionList = res.content;
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
      });
    },

    // 页面各分析模块切换
    moduleSwitch: function (mud) {
      var ts = this;
      mud = Number(mud);
      this.curEffectModule = mud;
      setTimeout(function () {
        switch (mud) {
          case 2: 
            ts.initEffectApi();
            break;
          default:  
            ts.initEffectApi();
            ts.clearEffectChart();
        };
        ts.render();
      }, 200)

    },

    // 页面时间范围筛选改变-触发整个效能督查的查询数据条件
    pageDateRangeChange: function (range) {
      this.pageSearchData.type = ""; //自定义时间时，清除掉时间段激活状态
      this.pageDateRange = range || culatorCurMonthDateRange() || ['', ''];
      this.moduleSwitch(this.curEffectModule);
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
      this.moduleSwitch(this.curEffectModule);
    },

    // 页面区域下拉选中，触发整个效能督查的查询条件修改
    regionChange: function(val){
      console.log(val)
      // this.moduleSwitch(this.curEffectModule);
    },


    // 工作量及有效性模块
    // 页面效能督查页面接口(工作量及有效性模块)
    initEffectApi: function () {
      this.fetchAccepSumData();
      this.fetchAccepConnectionData();
      this.fetchAccepConnectionEffectWinList();
    },
    // 清除掉当前专题下已渲染的echarts图
    clearEffectChart: function () {
      this.acceptSumPieChart && this.acceptSumPieChart.clear();
      this.accepConnectionBarChart && this.accepConnectionBarChart.clear();
      this.accepConnectionEffectLineChart && this.accepConnectionEffectLineChart.clear();
    },

    // 接件总量
    // 接件总量-获取后台数据
    fetchAccepSumData: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      debugger
      request('', {
        url: ctx + 'win/efficiency/supervision/getWinAcceptTotalStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            _acceptSum = res.content.total; //接件总数赋值
            ts.handelAccepSumData(res.content.data)
          } else {
            ts.acceptSumPieChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.acceptSumPieChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.acceptSumPieChart = '';
      });
    },
    // 接件总量处理后端数据
    handelAccepSumData: function (list) {
      if (!list || !list.length) return;
      assEchartOptions.acceptSumOpt.legend.data = [];
      assEchartOptions.acceptSumOpt.series[0].data = [];
      for (var i = 0; i < list.length; i++) {
        var opt = list[i].windowName;
        _acceptSumObj[opt] = {
          num: '',
          percent: ''
        }
        _acceptSumObj[opt]['percent'] = (Number(list[i].winJieJianRate) * 100).toFixed(0) + '%';
        _acceptSumObj[opt]['num'] = list[i].applyCount;

        assEchartOptions.acceptSumOpt.legend.data.push(opt);
        assEchartOptions.acceptSumOpt.series[0].data.push({
          name: list[i].windowName || '',
          value: list[i].applyCount || '',
          itemColor: list[i].echartsColor || '' //加上自定义的颜色
        })
      }
      assEchartOptions.acceptSumOpt.series[0].data[0].label = _accepNumTwoLabel;
      if (list.length < 2) {
        assEchartOptions.acceptSumOpt.series[0].itemStyle.borderWidth = 0;        
      }

      this.renderAcceptSumPieChart();
    },
    // 接件总量-渲染echrt
    renderAcceptSumPieChart: function () {
      this.acceptSumPieChart = echarts.init(document.getElementById('acceptSumPieChart'));
      this.acceptSumPieChart.setOption(assEchartOptions.acceptSumOpt);
    },


    // 接件受理
    // 接件受理-获取后台数据
    fetchAccepConnectionData: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getWinAcceptStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            assEchartOptions.acceptConnectionOpt.xAxis.data = res.content.title;
            assEchartOptions.acceptConnectionOpt.series[0].data = res.content.yiShouLi;
            assEchartOptions.acceptConnectionOpt.series[1].data = res.content.caiLiaoBuQuan;
            assEchartOptions.acceptConnectionOpt.series[2].data = res.content.buYuShouLi;
            // 超过15条出现横向放大缩小工具
            if (res.content.length && res.content.length > 15) {
              assEchartOptions.acceptConnectionOpt.dataZoom = _acceptConnectionOpt_datazoom;
            } else {
              assEchartOptions.acceptConnectionOpt.dataZoom = [];
              assEchartOptions.acceptConnectionOpt.grid.y2 = 20;
            }
            ts.renderAccepConnectionBarChart();
          } else {
            ts.accepConnectionBarChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.accepConnectionBarChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.accepConnectionBarChart = '';
      });
    },
    // 接件受理-渲染echart
    renderAccepConnectionBarChart: function () {
      this.accepConnectionBarChart = echarts.init(document.getElementById('accepConnectionBarChart'));
      this.accepConnectionBarChart.setOption(assEchartOptions.acceptConnectionOpt);
    },


    // 接件历史分析
    // 获取接件历史分析的窗口数据
    fetchAccepConnectionEffectWinList: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getWinAcceptDealStatistics',
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
    // 处理接件历史分析窗口数据
    handelAccepConnectionEffectWinList: function (list) {
      this.accepConnectionWinAllList = list.map(function (item) {
        return {
          winId: item.windowId,
          winName: item.windowName,
          apply: item.apply, //接件数
          acceptDeal: item.acceptDeal, //受理数
          percent: item.acceptDealRate, //受理率
          percentNum: Number(item.acceptDealRate.replace('%', '')) || 0, //受理率（纯百分比）
          select: false //是否选中状态
        }
      })
      this.accepConnectionWinPage = 1
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
      _getData.windowId = obj.winId;
      request('', {
        url: ctx + 'win/efficiency/supervision/getWinAcceptStatisticsByDay',
        type: 'get',
        data: _getData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            ts.handelAccepConnectionEffectLineChartData(res.content);
          } else {
            ts.accepConnectionEffectLineChart = "";
          }
        } else {
          ts.apiMessage(res.message, 'error');
          ts.accepConnectionEffectLineChart = "";
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error');
        ts.accepConnectionEffectLineChart = "";
      });
    },
    // 接件历史分析-处理当前选中窗口的统计数据
    handelAccepConnectionEffectLineChartData: function (handData) {
      var dateList = [], //日期数据list
        _acceptApplyList = [], //已接件list
        _completedApplyList = []; //已受理list
      for (var i = 0; i < handData.length; i++) {
        dateList.push(handData[i].day);
        _acceptApplyList.push(handData[i].apply);
        _completedApplyList.push(handData[i].acceptDeal);
      }
      assEchartOptions.acceptHandelOpt.xAxis.data = dateList;
      assEchartOptions.acceptHandelOpt.series[0].data = _acceptApplyList;
      assEchartOptions.acceptHandelOpt.series[1].data = _completedApplyList;
      this.renderAccepConnectionEffectLineChart();
    },
    // 接件历史分析-渲染echart
    renderAccepConnectionEffectLineChart: function () {
      this.accepConnectionEffectLineChart = echarts.init(document.getElementById('accepConnectionEffectLineChart'));
      this.accepConnectionEffectLineChart.setOption(assEchartOptions.acceptHandelOpt);
    },
    // 接件历史分析窗口左右控制
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
  created: function () {
    this.fetchApplyStatistics();
    // this.fetchRegionList();
    // (ts.curEffectModule == 2) && ts.initEffectApi();
    // test
    // return
    for (var i = 0; i < 15; i++) {
      this.accepConnectionWinAllList.push({
        winName: '窗口' + i,
        select: false,
        winId: i,
      })
    }
    this.accepConnectionWinList = this.accepConnectionWinAllList.slice(0, 7)
  },
  mounted: function () {
    var ts = this
    window.onresize = throttle(function () {
      ts.render();
    }, 100, 500, 100)

    // test
    this.renderAcceptSumPieChart();
    this.renderAccepConnectionBarChart();
    this.renderAccepConnectionEffectLineChart();
  },
  filters: {},
})