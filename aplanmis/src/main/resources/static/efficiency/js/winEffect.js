var app = new Vue({
  el: '#winEffect',
  mixins: [mixins],
  data: function () {
    return {
      // 卡片list
      statusCardData: [{
          name: '网上待预审',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'waring-status',
          jumpUrl: 'win/efficiency/supervision/queryPreliminaryTasksIndex.html' //跳转模块路径
        },
        {
          name: '待补全申报',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'waring-correct',
          jumpUrl: 'win/efficiency/supervision/queryNeedCompletedApplyIndex.html' //跳转模块路径
        },
        {
          name: '补全待确认申报',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'alerdy-correct',
          jumpUrl: 'win/efficiency/supervision/queryNeedConfirmCompletedApplyIndex.html' //跳转模块路径
        },
        {
          name: '不予受理',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'not-shouli',
          jumpUrl: 'win/efficiency/supervision/queryDismissedApplyIndex.html' //跳转模块路径
        },
        {
          name: '申报时限预警',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'time-yujing',
          jumpUrl: 'win/efficiency/supervision/queryWarnApplyIndex.html' //跳转模块路径
        },
        {
          name: '申报时限逾期',
          count: 0,
          linkRelativeRatio: '', //环比
          onYearonyearBasis: '', //同比
          statusClass: 'time-yuqi',
          jumpUrl: 'win/efficiency/supervision/queryOverdueApplyIndex.html' //跳转模块路径
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
      },
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


      // 并联专题
      // 主题申报量
      // 主题申报量-饼图
      themeApproveNumPieChart: '',
      // 主题申报量-表格数据
      themeApproveNumTableData: [],

      // 主题分布情况
      // 主题分布情况-柱状图
      themeScatterBarChart: '',

      // 阶段申报数
      // 阶段申报数-饼状图
      stageApprovePieChart: '',
    }
  },
  methods: {
    // 页面echart渲染resize
    render: function () {
      // 有效性专题
      this.acceptSumPieChart && this.acceptSumPieChart.resize();
      this.accepConnectionBarChart && this.accepConnectionBarChart.resize();
      this.accepConnectionEffectLineChart && this.accepConnectionEffectLineChart.resize();

      // 并联专题
      this.themeApproveNumPieChart && this.themeApproveNumPieChart.resize();
      this.themeScatterBarChart && this.themeScatterBarChart.resize();
      this.stageApprovePieChart && this.stageApprovePieChart.resize();
    },

    // 获取头部卡片数据
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

    // 页面各分析模块切换
    moduleSwitch: function (mud) {
      var ts = this;
      mud = Number(mud);
      this.curEffectModule = mud;
      setTimeout(function () {
        switch (mud) {
          case 2:
            ts.clearParallelChart();
            ts.clearTimeLimitChart();
            ts.initEffectApi();
            break;
          case 3:
            ts.clearEffectChart();
            ts.clearTimeLimitChart();
            ts.initParallelApi();
            break;
          case 4:
            ts.clearParallelChart();
            ts.clearEffectChart();
            ts.initTimeLimitApi();
            break;
          default:
            ts.clearParallelChart();
            ts.initEffectApi();
            ts.clearEffectChart();
            ts.initParallelApi();
            ts.clearTimeLimitChart();
            ts.initTimeLimitApi();
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
      windowEchartOptions.acceptSumOpt.legend.data = [];
      windowEchartOptions.acceptSumOpt.series[0].data = [];
      for (var i = 0; i < list.length; i++) {
        var opt = list[i].windowName;
        _acceptSumObj[opt] = {
          num: '',
          percent: ''
        }
        _acceptSumObj[opt]['percent'] = (Number(list[i].winJieJianRate) * 100).toFixed(0) + '%';
        _acceptSumObj[opt]['num'] = list[i].applyCount;

        windowEchartOptions.acceptSumOpt.legend.data.push(opt);
        windowEchartOptions.acceptSumOpt.series[0].data.push({
          name: list[i].windowName || '',
          value: list[i].applyCount || '',
          itemColor: list[i].echartsColor || '' //加上自定义的颜色
        })
      }
      if (list.length > 2) {
        // windowEchartOptions.acceptSumOpt.series[0].data[0].label = _accepNumOneLabel;
        // windowEchartOptions.acceptSumOpt.series[0].data[0].labelLine = _accepNumOneLabelLine;
        windowEchartOptions.acceptSumOpt.series[0].data[1].label = _accepNumTwoLabel;
        if (list[0] && list[0].applyCount == 0 || list[0] && list[0].winJieJianRate == 0) {
          windowEchartOptions.acceptSumOpt.series[0].data[0].label = {};
        }
      } else { //单个的不显示label
        windowEchartOptions.acceptSumOpt.series[0].itemStyle.borderWidth = 0;
        windowEchartOptions.acceptSumOpt.series[0].data[0].label = _accepNumTwoLabel;
      }

      this.renderAcceptSumPieChart();
    },
    // 接件总量-渲染echrt
    renderAcceptSumPieChart: function () {
      this.acceptSumPieChart = echarts.init(document.getElementById('acceptSumPieChart'));
      this.acceptSumPieChart.setOption(windowEchartOptions.acceptSumOpt);
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
            windowEchartOptions.acceptConnectionOpt.xAxis.data = res.content.title;
            windowEchartOptions.acceptConnectionOpt.series[0].data = res.content.yiShouLi;
            windowEchartOptions.acceptConnectionOpt.series[1].data = res.content.caiLiaoBuQuan;
            windowEchartOptions.acceptConnectionOpt.series[2].data = res.content.buYuShouLi;
            // 超过15条出现横向放大缩小工具
            if (res.content.length && res.content.length > 15) {
              windowEchartOptions.acceptConnectionOpt.dataZoom = _acceptConnectionOpt_datazoom;
            } else {
              windowEchartOptions.acceptConnectionOpt.dataZoom = [];
              windowEchartOptions.acceptConnectionOpt.grid.y2 = 20;
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
      this.accepConnectionBarChart.setOption(windowEchartOptions.acceptConnectionOpt);
    },


    // 接件受理率
    // 获取接件受理率的窗口数据
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
    // 处理接件受理率窗口数据
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
    // 接件受理率-处理当前选中窗口的统计数据
    handelAccepConnectionEffectLineChartData: function (handData) {
      var dateList = [], //日期数据list
        _acceptApplyList = [], //已接件list
        _completedApplyList = []; //已受理list
      for (var i = 0; i < handData.length; i++) {
        dateList.push(handData[i].day);
        _acceptApplyList.push(handData[i].apply);
        _completedApplyList.push(handData[i].acceptDeal);
      }
      windowEchartOptions.acceptHandelOpt.xAxis.data = dateList;
      windowEchartOptions.acceptHandelOpt.series[0].data = _acceptApplyList;
      windowEchartOptions.acceptHandelOpt.series[1].data = _completedApplyList;
      this.renderAccepConnectionEffectLineChart();
    },
    // 接件受理率-渲染echart
    renderAccepConnectionEffectLineChart: function () {
      this.accepConnectionEffectLineChart = echarts.init(document.getElementById('accepConnectionEffectLineChart'));
      this.accepConnectionEffectLineChart.setOption(windowEchartOptions.acceptHandelOpt);
    },
    // 接件受理窗口左右控制
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


    // 并联专题相关
    // 初始化并联专题
    initParallelApi: function () {
      this.fetchThemeApproveNumData();
      this.fetchThemeScatterData();
      this.fetchStageApproveData();
    },
    // 清除掉已渲染当前专题下的echart图
    clearParallelChart: function () {
      this.themeApproveNumPieChart && this.themeApproveNumPieChart.clear();
      this.themeScatterBarChart && this.themeScatterBarChart.clear();
      this.stageApprovePieChart && this.stageApprovePieChart.clear();
    },

    // 主题申报量
    // 主题申报量-点击表格高亮pie中的某一项
    leftChartHightLight: function (index) {
      this.themeApproveNumPieChart.dispatchAction({
        type: 'highlight',
        seriesIndex: 0,
        dataIndex: index
      });
    },
    // 主题申报量-点击表格高亮pie中的某一项
    leftChartHideLight: function (index) {
      this.themeApproveNumPieChart.dispatchAction({
        type: 'downplay',
        seriesIndex: 0,
        dataIndex: index
      });
    },
    // 主题申报量-获取后端数据
    fetchThemeApproveNumData: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getThemeApplyStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null && res.content.theme && res.content.theme.length) {
            ts.handelThemeApproveNumData(res.content)
          } else {
            ts.themeApproveNumTableData = [];
            ts.themeApproveNumPieChart.clear();
            ts.themeApproveNumPieChart = "";
          }
        } else {
          ts.themeApproveNumTableData = []; 
          ts.themeApproveNumPieChart.clear();
          ts.themeApproveNumPieChart = "";
          ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        ts.themeApproveNumTableData = [];
        ts.themeApproveNumPieChart.clear();
        ts.themeApproveNumPieChart = "";
        ts.apiMessage('网络错误！', 'error')
      });
    },
    // 主题申报量-处理后端数据
    handelThemeApproveNumData: function (content) {
      this.themeApproveNumTableData = content.theme || []; //右侧表格数据赋值

      var allThemeList = content.theme || [];
      _cityAproveSum = content.total || 0;
      windowEchartOptions.themeApproveNumOpt.series[0].data = [];
      for (var i = 0; i < allThemeList.length; i++) {
        // 如果是后端没有给颜色值，那就自己根据主题名称生成一个颜色值
        var _curItemColor = allThemeList[i].color;
        if (!_curItemColor) {
          var colorHash = new ColorHash();
          _curItemColor = colorHash.hex(allThemeList[i].themeName)
        }
        var opt = {
          name: allThemeList[i].themeName, //主题名
          value: allThemeList[i].apply, //申报数
          itemColor: _curItemColor, //颜色
          acceptDealRate: (Number(allThemeList[i].acceptDealRate) * 100).toFixed(2) + "%" || 0, //受理率
          overTimeRate: (Number(allThemeList[i].overTimeRate) * 100).toFixed(2) + "%" || 0, //逾期率
        }
        windowEchartOptions.themeApproveNumOpt.series[0].data.push(opt);
        this.themeApproveNumTableData[i].itemColor = _curItemColor;  //表格中的图例列也处理一下颜色值=>跟左侧的每一项对应
      }
      windowEchartOptions.themeApproveNumOpt.series[0].data[0].label = _themeApproveOneLabel;
      if (_cityAproveSum == 1) { //单个不显示块与块的间隔
        windowEchartOptions.themeApproveNumOpt.series[0].itemStyle.borderWidth = 0;
      }
      this.renderThemeApproveNumPieChart();
    },
    // 主题申报量-饼图渲染
    renderThemeApproveNumPieChart: function () {
      this.themeApproveNumPieChart = echarts.init(document.getElementById('themeApproveNumPieChart'));
      this.themeApproveNumPieChart.setOption(windowEchartOptions.themeApproveNumOpt);
    },

    // 主题分布情况
    // 主题分布情况-获取后端数据
    fetchThemeScatterData: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getThemeDistributionStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null && res.content.data && res.content.data.length) {
            ts.handelThemeScatterData(res)
          } else {
            ts.themeScatterBarChart.clear();
            ts.themeScatterBarChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error');
          ts.themeScatterBarChart.clear();
          ts.themeScatterBarChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error');
        ts.themeScatterBarChart.clear();
        ts.themeScatterBarChart = '';
      });
    },
    // 主题分布情况-处理后端数据
    handelThemeScatterData: function (res) {
      var datalist = res.content.data || [];
      if (datalist.length < 8) {
        windowEchartOptions.themeScatterOpt.dataZoom = {};
        windowEchartOptions.themeScatterOpt.grid.y2 = 20;
      }
      windowEchartOptions.themeScatterOpt.dataset.source = datalist;
      this.renderThemeScatterBarChart();
    },
    // 主题分布情况-柱状图渲染
    renderThemeScatterBarChart: function () {
      this.themeScatterBarChart = echarts.init(document.getElementById('themeScatterBarChart'));
      this.themeScatterBarChart.setOption(windowEchartOptions.themeScatterOpt);
    },

    // 阶段申报数
    // 阶段申报数-获取后端数据
    fetchStageApproveData: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'win/efficiency/supervision/getStageApplyStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            ts.handelStageApproveData(res)
          } else {
            ts.stageApprovePieChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.stageApprovePieChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.stageApprovePieChart = '';
      });
    },
    // 阶段申报数-处理后端数据
    handelStageApproveData: function (res) {
      windowEchartOptions.stageApproveOpt.series[0].data = [];
      var allData = res.content.data || [];
      windowEchartOptions.stageApproveOpt.series[0].data = allData.map(function (item) {
        return {
          name: item.name.length > 4 ? item.name.slice(0, 4) : item.name,
          value: item.data,
        }
      });
      this.renderStageApprovePieChart();
    },
    // 阶段申报数-饼状图渲染
    renderStageApprovePieChart: function () {
      this.stageApprovePieChart = echarts.init(document.getElementById('stageApprovePieChart'));
      this.stageApprovePieChart.setOption(windowEchartOptions.stageApproveOpt);
    },



    // 时限分析专题
    // 初始化时限专题
    initTimeLimitApi: function () {
    
    },
    // 清除掉已渲染当前专题下的echart图
    clearTimeLimitChart: function () {
    
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
    // this.accepConnectionWinList = this.accepConnectionWinAllList.slice(0, 7)
    (this.curEffectModule == 2) && this.initEffectApi();
    (this.curEffectModule == 3) && this.initParallelApi();
    (this.curEffectModule == 4) && this.initTimeLimitApi();
  },
  mounted: function () {
    var ts = this
    ts.isSmallWinFn(); //计算是否小屏
    window.onresize = throttle(function () {
      ts.render();
      ts.isSmallWinFn();
    }, 100, 500, 100)

    // test
    // this.renderAcceptSumPieChart();
    // this.renderAccepConnectionBarChart();
    // this.renderAccepConnectionEffectLineChart();

    // this.renderThemeApproveNumPieChart();
    // this.renderThemeScatterBarChart();
    // this.renderStageApprovePieChart();
  },
  filters: {},
})