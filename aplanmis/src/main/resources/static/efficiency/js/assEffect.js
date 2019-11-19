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

      // 当前展示的是哪一个督查模块  1:全部   2:工作量及有效性专题   3: 时限分析
      curEffectModule: 3,

      // 整个页面的督查的查询数据
      pageSearchData: {
        startTime: '',
        endTime: '',
        type: 'M',
        regionId: '', //区划id
      },
      // 当前部门所在省市下面的区域
      regionList: [{
        regionId: '123',
        regionName: '天河区'
      }, {
        regionId: '13',
        regionName: '番禺区'
      }, ],
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


      // 时限分析专题
      // 办件时限状态-饼图数据
      approveTimelimitStatusPieChart: '',

      // 部门办件时限状态
      approveStageAverageTimeBarChart: '',

      // 部门办件用时
      // 部门办件用时-柱状图
      themeApproveTimeBarChart: '',
      // 部门办件用时-切换主题的数据-总的数据
      themeAllList: [],
      // 部门办件用时-切换主题的数据-当前第几页
      themePage: 1,
      // 部门办件用时-切换主题的数据-当前页数据(定好了 7 条数据)
      themeShowList: [],

    }
  },
  methods: {
    // 页面echart渲染resize
    render: function () {
      // 有效性专题
      this.acceptSumPieChart && this.acceptSumPieChart.resize();
      this.accepConnectionBarChart && this.accepConnectionBarChart.resize();
      this.accepConnectionEffectLineChart && this.accepConnectionEffectLineChart.resize();

      // 时限专题
      this.approveTimelimitStatusPieChart &&  this.approveTimelimitStatusPieChart.resize();
      this.approveStageAverageTimeBarChart && this.approveStageAverageTimeBarChart.resize();
      this.themeApproveTimeBarChart && this.themeApproveTimeBarChart.resize();
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
    fetchRegionList: function () {
      var ts = this;
      ts.loading = true;
      request('', {
        url: ctx + 'org/efficiency/supervision/getCurrentCityRegions',
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
            ts.clearTimeLimitChart();
            ts.initEffectApi();
            break;
          case 3:
            ts.clearEffectChart();
            ts.initTimeLimitApi();
            break;
          default:
            ts.initEffectApi();
            ts.clearEffectChart();
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

    // 页面区域下拉选中，触发整个效能督查的查询条件修改
    regionChange: function (val) {
      console.log(val)
      this.moduleSwitch(this.curEffectModule);
    },


    // 工作量及有效性模块
    // 页面效能督查页面接口(工作量及有效性模块)
    initEffectApi: function () {
      this.fetchAccepSumData();
      this.fetchAccepConnectionEffectWinList();
    },
    // 清除掉当前专题下已渲染的echarts图
    clearEffectChart: function () {
      this.acceptSumPieChart && this.acceptSumPieChart.clear();
      this.accepConnectionBarChart && this.accepConnectionBarChart.clear();
      this.accepConnectionEffectLineChart && this.accepConnectionEffectLineChart.clear();
    },

    // 接件总量与接件受理情况
    // 接件总量与接件受理情况-获取后台数据
    fetchAccepSumData: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'org/efficiency/supervision/getOrgReceiveStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            _acceptSum = res.content.leftData.total; //接件总数赋值
            ts.handelAccepSumData(res.content.leftData.data,res.content.leftData.total);
            ts.handelAccepConnectionBarChartData(res.content.rightData);
          } else {
            ts.acceptSumPieChart.clear();
            ts.acceptSumPieChart = '';
            ts.accepConnectionBarChart.clear();
            ts.accepConnectionBarChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.acceptSumPieChart.clear();
          ts.acceptSumPieChart = '';
          ts.accepConnectionBarChart.clear();
          ts.accepConnectionBarChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.acceptSumPieChart.clear();
        ts.acceptSumPieChart = '';
        ts.accepConnectionBarChart.clear();
        ts.accepConnectionBarChart = '';
      });
    },
    // 接件总量处理后端数据
    handelAccepSumData: function (list,total) {
      if (!list || !list.length) return;
      assEchartOptions.acceptSumOpt.legend.data = [];
      assEchartOptions.acceptSumOpt.series[0].data = [];
      for (var i = 0; i < list.length; i++) {
        // 随机颜色
        var colorHash = new ColorHash();
        var hex = colorHash.hex(list[i].name)

        var opt = list[i].name;
        _acceptSumObj[opt] = {
          num: '',
          percent: ''
        }
        _acceptSumObj[opt]['percent'] =  list[i].value == 0 ? "0%" :((Number(list[i].value / total) * 100).toFixed(0) + '%');
        _acceptSumObj[opt]['num'] = list[i].value;

        assEchartOptions.acceptSumOpt.legend.data.push(opt);
        assEchartOptions.acceptSumOpt.series[0].data.push({
          name: list[i].name || '',
          value: list[i].value || '',
          itemColor: hex || '' //加上自定义的颜色
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

    // 接件受理情况-处理后端数据
    handelAccepConnectionBarChartData: function(list){
      var _titleList = ['pro', '已受理', '材料补正', '不予受理'];
      list.unshift(_titleList);
      assEchartOptions.acceptConnectionOpt.dataset.source = list;
      if (list.length && list.length > 15) {
        assEchartOptions.acceptConnectionOpt.dataZoom = _acceptConnectionOpt_datazoom;
      } else {
        assEchartOptions.acceptConnectionOpt.dataZoom = [];
        assEchartOptions.acceptConnectionOpt.grid.y2 = 20;
      }
      this.renderAccepConnectionBarChart();
    },
    // 接件受理情况-渲染echart
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
        url: ctx + 'org/efficiency/supervision/getRegionOrgAcceptStatistics',
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
      var ts = this;
      this.accepConnectionWinAllList = list.map(function (item) {
        return {
          winId: ts.pageSearchData.regionId ? item.orgId : item.regionId,
          winName: ts.pageSearchData.regionId ? item.orgName : item.regionName,
          apply: item.apply, //接件数
          acceptDeal: item.acceptDeal, //受理数
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
      ts.pageSearchData.regionId? (_getData.orgId = obj.winId) : (_getData.regionId = obj.winId);
      request('', {
        url: ctx + 'org/efficiency/supervision/getRegionOrgAcceptHistoryStatistics',
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



    // 时限分析专题
    // 初始化时限专题
    initTimeLimitApi: function () {
      this.fetchApproveTimelimitStatusPieChartData();
      this.fetchAllThemeList();
    },
    // 清除掉已渲染当前专题下的echart图
    clearTimeLimitChart: function () {
      this.approveTimelimitStatusPieChart &&  this.approveTimelimitStatusPieChart.clear();
      this.approveStageAverageTimeBarChart && this.approveStageAverageTimeBarChart.clear();
      this.themeApproveTimeBarChart && this.themeApproveTimeBarChart.clear();
    },

    // 办件时限状态+部门办件时限状态
    // 办件时限状态+部门办件时限状态-获取后端接口
    fetchApproveTimelimitStatusPieChartData: function(){
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'org/efficiency/supervision/getOrgReceiveLimitTimeStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            _approveTimelimitAccSum =res.content.leftData.total;
            ts.handelApproveTimelimitStatusPieChartData(res.content.leftData.total,res.content.leftData.data);
            ts.handelApproveStageAverageTimeBarChartData(res.content.rightData);
          } else {
            ts.approveTimelimitStatusPieChart.clear();
            ts.approveStageAverageTimeBarChart.clear();
            ts.approveTimelimitStatusPieChart = '';
            ts.approveStageAverageTimeBarChart = '';
          }
        } else {
          ts.apiMessage(res.message, 'error')
          ts.approveTimelimitStatusPieChart.clear();
          ts.approveStageAverageTimeBarChart.clear();
          ts.approveTimelimitStatusPieChart = '';
          ts.approveStageAverageTimeBarChart = '';
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
        ts.approveTimelimitStatusPieChart.clear();
        ts.approveStageAverageTimeBarChart.clear();
        ts.approveTimelimitStatusPieChart = '';
        ts.approveStageAverageTimeBarChart = '';
      });
    },
    // 办件时限状态-处理后端接口数据
    handelApproveTimelimitStatusPieChartData: function(total,list){
      if (!list || !list.length) return;
      assEchartOptions.approveTimelimitStatusPieChartOpt.legend.data = [];
      assEchartOptions.approveTimelimitStatusPieChartOpt.series[0].data = [];
      for (var i = 0; i < list.length; i++) {
        var opt = list[i].name;
        _approveTimelimitLendObj[opt] = {
          num: '',
          percent: ''
        }
        _approveTimelimitLendObj[opt]['percent'] = list[i].value == 0 ? "0%" :((Number(list[i].value / total) * 100).toFixed(0) + '%');
        _approveTimelimitLendObj[opt]['num'] = list[i].value;

        assEchartOptions.approveTimelimitStatusPieChartOpt.legend.data.push(opt);
        assEchartOptions.approveTimelimitStatusPieChartOpt.series[0].data.push({
          name: list[i].name || '',
          value: list[i].value || 0,
        })
      }
      assEchartOptions.approveTimelimitStatusPieChartOpt.series[0].data[0].label = _approveTimelimitAccSumLabel;
      this.renderApproveTimelimitStatusPieChart();
    },
    // 办件时限状态-渲染echart
    renderApproveTimelimitStatusPieChart: function(){
      this.approveTimelimitStatusPieChart = echarts.init(document.getElementById('approveTimelimitStatusPieChart'));
      this.approveTimelimitStatusPieChart.setOption(assEchartOptions.approveTimelimitStatusPieChartOpt);
    },
    // 部门办件时限状态-处理后端数据
    handelApproveStageAverageTimeBarChartData: function(list){
      var _stageList = ['product','正常','预警','逾期'];
      list.unshift(_stageList)
      assEchartOptions.approveStageAverageTimeBarChartOpt.dataset.source = list;
      // 超过13条出现横向放大缩小工具
      if (list && list < 13) {
        assEchartOptions.approveStageAverageTimeBarChartOpt.dataZoom = [];
        assEchartOptions.approveStageAverageTimeBarChartOpt.grid.y2 = 20;
      } 
      this.renderApproveStageAverageTimeBarChart();
    },
    // 部门办件时限状态-渲染echarts
    renderApproveStageAverageTimeBarChart: function(){
      this.approveStageAverageTimeBarChart = echarts.init(document.getElementById('approveStageAverageTimeBarChart'));
      this.approveStageAverageTimeBarChart.setOption(assEchartOptions.approveStageAverageTimeBarChartOpt);
    },

    // 部门办件用时
    // 获取的部门办件用时-头部主题数据
    fetchAllThemeList: function () {
      var ts = this;
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      request('', {
        url: ctx + 'org/efficiency/supervision/getRegionOrgItemUseTimeStatistics',
        type: 'get',
        data: ts.pageSearchData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            ts.handelAllThemeList(res.content)
          }
        } else {
          ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error')
      });
    },
    // 处理部门办件用时-头部主题数据
    handelAllThemeList: function (list) {
      var ts = this;
      this.themeAllList = list.map(function (item) {
        return {
          winId: ts.pageSearchData.regionId? item.orgId: item.regionId,
          winName:ts.pageSearchData.regionId? item.orgName: item.regionName,
          lgTime: item.maxUseTime,  //最长用时
          AveTime: item.avgUseTime, //平均用时
          minTime: item.minUseTime, //最短用时
          select: false //是否选中状态
        }
      })
      this.themePage = 1
      // 截取前七个用来显示
      this.themeShowList = this.themeAllList.slice(0, 5)
      // 加载第一个主题下各窗口用时的统计数据
      this.fetchSelectedThemeWinData(this.themeAllList[0]);
    },
    // 高亮当前选中的主题
    activeCurThemeItem: function (obj) {
      this.themeAllList.forEach(function (item) {
        item.select = false;
      })
      obj.select = true;
    },
    // 获取当前选中窗口的统计数据
    fetchSelectedThemeWinData: function (obj) {
      this.activeCurThemeItem(obj); //高亮当前主题
      var ts = this,
        _getData = {};
      ts.loading = true;
      ts.pageSearchData.startTime = formatDate(new Date(ts.pageDateRange[0]), 'yyyy-MM-dd') || '';
      ts.pageSearchData.endTime = formatDate(new Date(ts.pageDateRange[1]), 'yyyy-MM-dd') || '';
      _getData.startTime = ts.pageSearchData.startTime;
      _getData.endTime = ts.pageSearchData.endTime;
      _getData.type = ts.pageSearchData.type;
      ts.pageSearchData.regionId? (_getData.orgId = obj.winId) : (_getData.regionId = obj.winId);
      request('', {
        url: ctx + 'org/efficiency/supervision/getRegionOrgItemUseTimeStatistics',
        type: 'get',
        data: _getData,
      }, function (res) {
        ts.loading = false;
        if (res.success) {
          if (res.content && res.content !== null) {
            ts.handelSelectedThemeWinBarChartData(res.content);
          } else {
            ts.themeApproveTimeBarChart.clear();
            ts.themeApproveTimeBarChart = "";
          }
        } else {
          ts.apiMessage(res.message, 'error');
          ts.themeApproveTimeBarChart.clear();
          ts.themeApproveTimeBarChart = "";
        }
      }, function () {
        ts.loading = false;
        ts.apiMessage('网络错误！', 'error');
        ts.themeApproveTimeBarChart.clear();
        ts.themeApproveTimeBarChart = "";
      });
    },
    // 部门办件用时-处理当前选中主题下各窗口的统计数据
    handelSelectedThemeWinBarChartData: function (handData) {
      var ts = this,
        winList = [], //窗口数据list
        _lgList = [], //最长用时list
        _minList = [],  //最短用时list
        _avgList = [], //平均用时list
        _ln = handData.length;  //后端数据-窗口的总个数
      for (var i = 0; i < _ln; i++) {
        var xname = '';
        handData[i].itemId?  (xname = handData[i].itemName):(xname = handData[i].orgName);
        winList.push(xname);
        _lgList.push(handData[i].maxUseTime);
        _minList.push(handData[i].minUseTime);
        _avgList.push(handData[i].avgUseTime);
      }
      assEchartOptions.themeApproveTimeBarChartOpt.xAxis.data = winList;
      assEchartOptions.themeApproveTimeBarChartOpt.series[0].data = _lgList;
      assEchartOptions.themeApproveTimeBarChartOpt.series[1].data = _avgList;
      assEchartOptions.themeApproveTimeBarChartOpt.series[2].data = _minList;

      if(_ln < 12){
        assEchartOptions.themeApproveTimeBarChartOpt.dataZoom = [];
        assEchartOptions.themeApproveTimeBarChartOpt.grid.y2 = 20;
      }

      this.renderThemeApproveTimeBarChart();
    },
    // 部门办件用时-渲染echart
    renderThemeApproveTimeBarChart: function () {
      this.themeApproveTimeBarChart = echarts.init(document.getElementById('themeApproveTimeBarChart'));
      this.themeApproveTimeBarChart.setOption(assEchartOptions.themeApproveTimeBarChartOpt);
    },
    // 部门办件用时-头部主题栏左右控制
    themeHandelToRight: function () {
      var ts = this,
        _all = this.themeAllList,
        _list = this.themeShowList,
        _page = this.themePage,
        _maxPage = "";
      _maxPage = Math.floor(_all.length / 5);
      this.themeShowList = _all.slice((_page) * 5, (_page + 1) * 5);
      if (_page > 0 && _page <= _maxPage) {
        _page++;
      }
      this.themePage = _page;
    },
    themeHandelToLeft: function () {
      var ts = this,
        _all = this.themeAllList,
        _list = this.themeShowList,
        _page = this.themePage,
        _maxPage = "";
      _maxPage = Math.floor(_all.length / 5);
      // debugger
      this.themeShowList = _all.slice((_page - 2) * 5, (_page - 1) * 5);
      if (_page >= 2) {
        _page--;
      }
      this.themePage = _page;
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

    // 时限分析专题-部门办件用时
    isTLeft: function () {
      var _crP = this.themePage;
      if (_crP < 2) {
        return false;
      }
      return true;
    },
    isTRight: function () {
      var _crP = this.themePage;
      if (_crP * 5 >= this.themeAllList.length) {
        return false;
      }
      return true;
    },
  },
  created: function () {
    this.fetchApplyStatistics();
    this.fetchRegionList();
    (this.curEffectModule == 2) && this.initEffectApi();
    (this.curEffectModule == 3) && this.initTimeLimitApi();
    // test
    return
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
    // this.renderAcceptSumPieChart();
    // this.renderAccepConnectionBarChart();
    // this.renderAccepConnectionEffectLineChart();

    // this.renderApproveTimelimitStatusPieChart();
    // this.renderApproveStageAverageTimeBarChart();
    // this.renderThemeApproveTimeBarChart();
  },
  filters: {},
})