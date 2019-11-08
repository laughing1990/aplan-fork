// 主题申报接件总数（由调用里面去计算）
var _themeAcceptSum = 0;
// 主题申报接件统计-分类的比例
var _themeAcceptObj = {
  '政府投资类': {
    percent: '54%',
    num: 108
  },
  '社会投资类': {
    percent: '32%',
    num: 64
  },
  '预先服务协同': {
    percent: '14%',
    num: 28
  },
}
// 主题申报接件统计中的接件总数label
var _themeAcceptSumLabel = {
  normal: {
    show: true,
    position: 'center',
    formatter: function (params) {
      return [
        '{a|接件总数}',
        '{b|' + _themeAcceptSum + '}'
      ].join('\n')
    },
    rich: {
      a: {
        color: '#7F8590',
        fontSize: 12,
      },

      b: {
        color: '#575962',
        fontSize: 18,
        padding: [0, 0, 8, 0],
      },
    },
    textStyle: {
      fontSize: 15,
      color: '#999'
    }
  },

};

// 阶段申报接件总数（由调用里面去计算）
var _stageAcceptSum = 0;
// 阶段申报接件统计-分类的比例
var _stageAcceptObj = {
  '立项用地规划许可': {
    percent: '25%',
    num: 50
  },
  '工程建设许可': {
    percent: '28%',
    num: 56
  },
  '施工许可': {
    percent: '22%',
    num: 44
  },
  '竣工验收': {
    percent: '16%',
    num: 32
  },
  '并行推进': {
    percent: '9%',
    num: 18
  },
}
// 阶段申报接件统计中的接件总数label
var _stageAcceptSumLabel = {
  normal: {
    show: true,
    position: 'center',
    formatter: function (params) {
      return [
        '{a|接件总数}',
        '{b|' + _stageAcceptSum + '}'
      ].join('\n')
    },
    rich: {
      a: {
        color: '#7F8590',
        fontSize: 12,
      },

      b: {
        color: '#575962',
        fontSize: 18,
        padding: [0, 0, 8, 0],
      },
    },
    textStyle: {
      fontSize: 15,
      color: '#999'
    }
  },

};

// 是否有x轴放大缩小的属性对象
var _opiontsHasDataZoomAttr = [{
  show: true,
  realtime: true,
  start: 0,
  end: 50,
}, {
  type: 'inside',
  realtime: true,
  start: 0,
  end: 50
}];


// 窗口人员-效能督查
var windowEchartOptions = {
  // 窗口受理申报统计
  windowAcceptDeclareOpt: {
    backgroundColor: '#fff',
    // x轴与画板底部的距离
    grid: {
      top: 48,
      // y2: 20
      y2: 60
    },
    legend: {
      data: ['接件总数', '不予受理数'],
      textStyle: {

      },
      icon: "circle",
    },
    tooltip: {
      trigger: 'axis',
      formatter: function (params) {
        var str = "";
        if (params[1]) {
          str += (params[1].name + '<br /> ' + params[1].marker + '  接件总数：' + params[1].value)
        }
        if (params[2]) {
          str += ('<br /> ' + params[2].marker + '  不予受理数：' + params[2].value)
        }
        return [str].join('\n');
        // return [
        //   params[1].name + '<br /> ' + params[1].marker + '  接件总数：' + params[1].value + '<br /> ' + params[2].marker + '  不予受理数：' + params[2].value
        // ].join('\n')
      },
      axisPointer: {
        type: 'shadow'
      },
    },
    // 放大缩小
    dataZoom: [{
      show: true,
      realtime: true,
      start: 0,
      end: 15
    }, {
      type: 'inside',
      realtime: true,
      start: 0,
      end: 50
    }],
    xAxis: {
      type: 'category',
      axisLine: {
        lineStyle: {
          color: "#999",
        }
      },
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      name: '单位（个）',
      type: 'value',
      axisLine: {
        show: false
      },
      splitLine: {
        show: true,
        lineStyle: {
          type: 'dashed'
        }
      }
    },
    series: [{
      data: [200, 200, 200, 200, 200, 200, 200],
      type: 'bar',
      barWidth: 10,
      itemStyle: {
        normal: {
          color: "rgba(229,229,229, 0.4)",
          barBorderRadius: [10, 10, 10, 10],
        }
      },
    }, {
      data: [100, 100, 140, 30, 50, 30, 110],
      name: '接件总数',
      type: 'bar',
      barWidth: 10,
      barGap: '-100%',
      itemStyle: {
        normal: {
          color: '#3AA1FF',
          label: {
            show: false,
            position: 'top',
            formatter: '{b}\n{c}'
          },
          barBorderRadius: [10, 10, 10, 10],
        }
      },
    }, {
      data: [120, 200, 150, 80, 70, 110, 130],
      name: '不予受理数',
      type: 'bar',
      barWidth: 10,
      barGap: '-100%',
      itemStyle: {
        normal: {
          color: '#F2637B',
          label: {
            show: false,
            position: 'top',
            formatter: '{b}\n{c}'
          },
          barBorderRadius: [10, 10, 10, 10],
        }
      },
    }, ]
  },

  // 月度申报受理统计图
  monthAcceptDeclareOpt: {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['受理申报数', '办结申报数', '逾期申报数', '不予受理数']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    // toolbox: {
    //   feature: {
    //     saveAsImage: {}
    //   }
    // },
    xAxis: {
      type: 'category',
      boundaryGap: true,
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value',
      name: '单位（天）',
      axisLine: {
        show: false
      },
    },
    series: [{
        name: '受理申报数',
        type: 'line',
        color: '#3AA1FF',
        smooth: true,
        data: [110, 102, 101, 134, 390, 30, 10]
      },
      {
        name: '办结申报数',
        type: 'line',
        color: '#36CBCB',
        smooth: true,
        data: [20, 122, 198, 204, 990, 330, 310]
      },
      {
        name: '逾期申报数',
        type: 'line',
        color: '#F2637B',
        smooth: true,
        data: [120, 132, 101, 34, 60, 230, 210]
      },
      {
        name: '不予受理数',
        type: 'line',
        color: '#9FA0B1',
        smooth: true,
        data: [10, 192, 151, 34, 190, 200, 20]
      },
    ]
  },

  // 地区申报统计
  areaDeclareOpt: {
    backgroundColor: '#fff',
    // x轴与画板底部的距离
    grid: {
      top: 48,
      y2: 20
    },
    legend: {
      data: ['申报数'],
      textStyle: {

      },
      icon: "circle",
    },
    tooltip: {
      trigger: 'axis',
      formatter: function (params, ticket, callback) {
        // console.log(params);
        if(params[1]){
          return params[1].marker + params[1].name + ' ' + params[1].value;
        }
        return '';
      },
      axisPointer: {
        type: 'shadow'
      },
    },
    xAxis: {
      type: 'category',
      axisLine: {
        lineStyle: {
          color: "#999",
        }
      },
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      name: '单位（个）',
      type: 'value',
      axisLine: {
        show: false
      },
      splitLine: {
        show: true,
        lineStyle: {
          type: 'dashed'
        }
      }
    },
    series: [{
      data: [200, 200, 200, 200, 200, 200, 200],
      type: 'bar',
      barWidth: 10,
      itemStyle: {
        normal: {
          color: "rgba(229,229,229, 0.4)",
          barBorderRadius: [10, 10, 10, 10],
        }
      },
    }, {
      data: [120, 200, 150, 80, 70, 110, 130],
      name: '申报数',
      type: 'bar',
      barWidth: 10,
      barGap: '-100%',
      itemStyle: {
        normal: {
          color: '#3AA1FF',
          label: {
            show: false,
            position: 'top',
            formatter: '{b}\n{c}'
          },
          barBorderRadius: [10, 10, 10, 10],
        }
      },
    }]
  },

  // 主题申报统计图options
  themeDeclareOpt: {
    title: {
      text: '主题申报统计',
      color: '#999',
      x: 'left',
      textStyle: {
        color: '#7F8590',
        fontWeight: 'normal',
        fontSize: '14px'
      },
    },
    tooltip: {
      trigger: 'item',
      formatter: function (params) {
        // console.log(params)
        return [
          params.marker + params.name + '</br>' + '比例：' + params.percent + '%</br>' + '数量：' + params.value
        ].join('\n')
      },

    },
    legend: {
      orient: 'vertical',
      x2: 50,
      y: 'center',
      icon: 'circle',
      itemHeight: 10,
      formatter: function (name) {
        return [
          // '{b|' + ' ' + name + '}{a|' + '  |  ' + _themeAcceptObj[name].percent + '}{x|' + '    ' + _themeAcceptObj[name].num + '}'
          '{c|' + ' ' + name + '}{a|' + '  |  ' + _themeAcceptObj[name].percent + '}{x|' + '      ' + _themeAcceptObj[name].num + '}'
        ].join('\n')
      },
      textStyle: {
        lineHeight: 86,
        //rich放在textStyle里面
        rich: {
          a: {
            color: '#999',
            width: 40
          },
          b: {
            color: '#7F8590',

          },
          x: {
            color: '#7F8590',
            fontSize: 14,
            float: 'right'
          },
          c: {
            width: 80,
          },
        }
      },
      data: ['政府投资类', '社会投资类', '预先服务协同']
    },
    color: ['#3AA1FF', '#4ECB73', '#FBD437'],
    series: [{
      name: '访问来源',
      type: 'pie',
      radius: ['40%', '54%'],
      center: ['28%', '50%'],
      avoidLabelOverlap: false,
      label: {
        normal: {
          show: false,
        },

      },
      labelLine: {
        normal: {
          show: false
        }
      },
      itemStyle: {
        borderWidth: 3, //设置border的宽度有多大
        borderColor: '#fff',
      },
      data: [{
          value: 335,
          name: '政府投资类',
          label: {
            normal: {
              show: true,
              position: 'center',
              formatter: function (params) {
                return [
                  '{a|接件总数}',
                  '{b|' + _themeAcceptSum + '}'
                ].join('\n')
              },
              rich: {
                a: {
                  color: '#7F8590',
                  fontSize: 12,
                },

                b: {
                  color: '#575962',
                  fontSize: 18,
                  padding: [0, 0, 8, 0],
                },
              },
              textStyle: {
                fontSize: 15,
                color: '#999'
              }
            },

          },
        },
        {
          value: 310,
          name: '社会投资类'
        },
        {
          value: 234,
          name: '预先服务协同'
        },

      ]
    }]
  },

  // 阶段申报统计图options
  stageDeclareOpt: {
    title: {
      text: '阶段申报统计',
      color: '#999',
      x: 'left',
      textStyle: {
        color: '#7F8590',
        fontWeight: 'normal',
        fontSize: '14px'
      },
    },
    tooltip: {
      trigger: 'item',
      formatter: function (params) {
        // console.log(params)
        return [
          params.marker + params.name + '</br>' + '比例：' + params.percent + '%</br>' + '数量：' + params.value
        ].join('\n')
      },
    },
    legend: {
      orient: 'vertical',
      x2: 50,
      y: 'center',
      icon: 'circle',
      itemHeight: 10,
      formatter: function (name) {
        return [
          // '{b|' + ' ' + name + '}{a|' + '  |  ' + _stageAcceptObj[name].percent + '}{x|' + '    ' + _stageAcceptObj[name].num + '}'
          '{c|' + ' ' + name + '}{a|' + '  |  ' + _stageAcceptObj[name].percent + '}{x|' + '      ' + _stageAcceptObj[name].num + '}'
        ].join('\n')
      },
      textStyle: {
        lineHeight: 86,
        //rich放在textStyle里面
        rich: {
          a: {
            color: '#999',
            width: 40
          },
          b: {
            color: '#7F8590',

          },
          x: {
            color: '#7F8590',
            fontSize: 14,
            float: 'right'
          },
          c: {
            width: 100
          }
        }
      },
      data: ['立项用地规划许可', '工程建设许可', '施工许可', '竣工验收', '并行推进']
    },
    color: ['#3AA1FF', '#36CBCB', '#4ECB73', '#FBD437', '#F2637B'],
    series: [{
      name: '阶段',
      type: 'pie',
      radius: ['40%', '54%'],
      center: ['28%', '50%'],
      avoidLabelOverlap: false,
      label: {
        normal: {
          show: false,
        },

      },
      labelLine: {
        normal: {
          show: false
        }
      },
      itemStyle: {
        borderWidth: 3, //设置border的宽度有多大
        borderColor: '#fff',
      },
      data: [{
          value: 50,
          name: '立项用地规划许可',
          label: {
            normal: {
              show: true,
              position: 'center',
              formatter: function (params) {
                return [
                  '{a|接件总数}',
                  '{b|' + _stageAcceptSum + '}'
                ].join('\n')
              },
              rich: {
                a: {
                  color: '#7F8590',
                  fontSize: 12,
                },

                b: {
                  color: '#575962',
                  fontSize: 18,
                  padding: [0, 0, 8, 0],
                },
              },
              textStyle: {
                fontSize: 15,
                color: '#999'
              }
            },

          },
        },
        {
          value: 56,
          name: '工程建设许可'
        },
        {
          value: 44,
          name: '施工许可'
        },
        {
          value: 32,
          name: '竣工验收'
        },
        {
          value: 18,
          name: '并行推进'
        },

      ]
    }]
  },

  // 项目并联审批统计-options
  projectParallelApprovalOpt: {
    tooltip: {
      trigger: 'item',
      // formatter: "{a} <br/>{b} : {c} ({d}%)"
      formatter: function (params) {
        // console.log(params)
        return [
          params.marker + params.name + '</br>' + '比例：' + params.percent + '%</br>' + '数量：' + params.value
        ].join('\n')
      },
    },

    calculable: true,
    label: {
      normal: {
        show: true,
      },
    },
    labelLine: {
      normal: {
        show: true
      }
    },
    color: ['#3AA1FF', '#4ECB73', '#F2637B'],
    series: [{
      name: '项目',
      type: 'pie',
      radius: [18, 80],
      center: ['50%', '50%'],
      roseType: 'radius',
      label: {
        normal: {
          show: true,
          formatter: function (params) {
            // console.log(params)
            return [
              '{a|' + params.name + '}',
              '{a|' + params.value + '}{a|' + ' (' + params.percent + '%)' + '}'
            ].join('\n')
          },
          rich: {
            a: {
              color: '#7F8590',
              fontSize: 12,
              padding: [0, 0, 6, 0],
            },
          },
        },
      },
      data: [{
          value: 10,
          name: '全并联项目',
        },
        {
          value: 5,
          name: '全单项审批项目'
        },
        {
          value: 15,
          name: '半并联项目'
        },
      ]
    }]
  },

  // 申报来源统计-options
  declareSourceOpt: {
    tooltip: {
      trigger: 'item',
      // formatter: "{a} <br/>{b} : {c} ({d}%)"
      formatter: function (params) {
        // console.log(params)
        return [
          params.marker + params.name + '</br>' + '比例：' + params.percent + '%</br>' + '数量：' + params.value
        ].join('\n')
      },
    },

    calculable: true,
    label: {
      normal: {
        show: true,
      },
    },
    labelLine: {
      normal: {
        show: true
      }
    },
    color: ['#FBD437', '#3AA1FF'],
    series: [{
      name: '来源',
      type: 'pie',
      radius: [52, 80],
      center: ['50%', '50%'],
      // roseType: 'radius',
      itemStyle: {
        borderWidth: 3, //设置border的宽度有多大
        borderColor: '#fff',
      },

      label: {
        normal: {
          show: true,
          formatter: function (params) {
            // console.log(params)
            return [
              '{a|' + params.name + '}',
              '{a|' + params.value + '}{a|' + ' (' + params.percent + '%)' + '}'
            ].join('\n')
          },
          rich: {
            a: {
              color: '#7F8590',
              fontSize: 12,
              padding: [0, 0, 6, 0],
            },
          },
        },
      },
      data: [{
          value: 10,
          name: '网上大厅',
          selected: true,
        },

        {
          value: 15,
          name: '现场大厅'
        },
      ]
    }]
  },
};

// 委托局人员-效能督查
var assignEchartOptions = {
  // 部门受理事项统计
  departAcceptDeclareOpt: {
    backgroundColor: '#fff',
    // x轴与画板底部的距离
    grid: {
      top: 48,
      y2: 60
      // y2: 100
    },
    legend: {
      data: ['接件总数', '不予受理数'],
      textStyle: {

      },
      icon: "circle",
    },
    tooltip: {
      trigger: 'axis',
      formatter: function (params) {
        // console.log(params)
        var str = "";
        if (params[1]) {
          str += (params[1].name + '<br /> ' + params[1].marker + '  接件总数：' + params[1].value)
        }
        if (params[2]) {
          str += ('<br /> ' + params[2].marker + '  不予受理数：' + params[2].value)
        }
        // return [
        //   params[1].name + '<br /> ' + params[1].marker + '  接件总数：' + params[1].value + '<br /> ' + params[2].marker + '  不予受理数：' + params[2].value
        // ].join('\n');
        return [str].join('\n');
      },
      axisPointer: {
        type: 'shadow'
      },
    },
    // 放大缩小
    dataZoom: [{
      show: true,
      realtime: true,
      start: 0,
      end: 15
    }, {
      type: 'inside',
      realtime: true,
      start: 0,
      end: 50
    }],
    xAxis: {
      type: 'category',
      axisLine: {
        lineStyle: {
          color: "#999",
        }
      },
      axisLabel: {
        // interval: 8,
        // interval: 0,
        // formatter: function(params){
        //   return (params.length>2? params.substring(0,2) + '...': params)
        // },
        // formatter: function (value) {
        //   return value.split("").join("\n");
        // }
      },
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      name: '单位（个）',
      type: 'value',
      // max: 0,
      axisLine: {
        show: false
      },
      splitLine: {
        show: true,
        lineStyle: {
          type: 'dashed'
        }
      }
    },
    series: [{
      data: [200, 200, 200, 200, 200, 200, 200],
      type: 'bar',
      barWidth: 10,
      itemStyle: {
        normal: {
          color: "rgba(229,229,229, 0.4)",
          barBorderRadius: [10, 10, 10, 10],
        }
      },
    }, {
      data: [100, 100, 140, 30, 50, 30, 110],
      name: '接件总数',
      type: 'bar',
      barWidth: 10,
      barGap: '-100%',
      itemStyle: {
        normal: {
          color: '#3AA1FF',
          label: {
            show: false,
            position: 'top',
            formatter: '{b}\n{c}'
          },
          barBorderRadius: [10, 10, 10, 10],
        }
      },
    }, {
      data: [120, 200, 150, 80, 70, 110, 130],
      name: '不予受理数',
      type: 'bar',
      barWidth: 10,
      barGap: '-100%',
      itemStyle: {
        normal: {
          color: '#F2637B',
          label: {
            show: false,
            position: 'top',
            formatter: '{b}\n{c}'
          },
          barBorderRadius: [10, 10, 10, 10],
        }
      },
    }, ]
  },

  // 月度事项受理统计图
  monthAcceptDeclareOpt: {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['受理申报数', '办结申报数', '逾期申报数', '不予受理数']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    // toolbox: {
    //   feature: {
    //     saveAsImage: {}
    //   }
    // },
    xAxis: {
      type: 'category',
      boundaryGap: true,
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value',
      name: '单位（天）',
      axisLine: {
        show: false
      },
    },
    series: [{
        name: '受理申报数',
        type: 'line',
        color: '#3AA1FF',
        smooth: true,
        data: [110, 102, 101, 134, 390, 30, 10]
      },
      {
        name: '办结申报数',
        type: 'line',
        color: '#36CBCB',
        smooth: true,
        data: [20, 122, 198, 204, 990, 330, 310]
      },
      {
        name: '逾期申报数',
        type: 'line',
        color: '#F2637B',
        smooth: true,
        data: [120, 132, 101, 34, 60, 230, 210]
      },
      {
        name: '不予受理数',
        type: 'line',
        color: '#9FA0B1',
        smooth: true,
        data: [10, 192, 151, 34, 190, 200, 20]
      },
    ]
  },

  // 地区办件统计
  areaDeclareOpt: {
    backgroundColor: '#fff',
    // x轴与画板底部的距离
    grid: {
      top: 48,
      y2: 20
    },
    legend: {
      data: ['申报数'],
      textStyle: {

      },
      icon: "circle",
    },
    tooltip: {
      trigger: 'axis',
      formatter: function (params, ticket, callback) {
        // console.log(params);
        if (params[1]) {
          return params[1].marker + params[1].name + ' ' + params[1].value;
        }
        return '';
      },
      axisPointer: {
        type: 'shadow'
      },
    },
    xAxis: {
      type: 'category',
      axisLine: {
        lineStyle: {
          color: "#999",
        }
      },
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      name: '单位（个）',
      type: 'value',
      axisLine: {
        show: false
      },
      splitLine: {
        show: true,
        lineStyle: {
          type: 'dashed'
        }
      }
    },
    series: [{
      data: [200, 200, 200, 200, 200, 200, 200],
      type: 'bar',
      barWidth: 10,
      itemStyle: {
        normal: {
          color: "rgba(229,229,229, 0.4)",
          barBorderRadius: [10, 10, 10, 10],
        }
      },
    }, {
      data: [120, 200, 150, 80, 70, 110, 130],
      name: '申报数',
      type: 'bar',
      barWidth: 10,
      barGap: '-100%',
      itemStyle: {
        normal: {
          color: '#3AA1FF',
          label: {
            show: false,
            position: 'top',
            formatter: '{b}\n{c}'
          },
          barBorderRadius: [10, 10, 10, 10],
        }
      },
    }]
  },

  // 主题申报统计图options
  themeDeclareOpt: {
    title: {
      text: '主题申报统计',
      color: '#999',
      x: 'left',
      textStyle: {
        color: '#7F8590',
        fontWeight: 'normal',
        fontSize: '14px'
      },
    },
    tooltip: {
      trigger: 'item',
      formatter: function (params) {
        // console.log(params)
        return [
          params.marker + params.name + '</br>' + '比例：' + params.percent + '%</br>' + '数量：' + params.value
        ].join('\n')
      },

    },
    legend: {
      orient: 'vertical',
      x2: 50,
      y: 'center',
      icon: 'circle',
      itemHeight: 10,
      formatter: function (name) {
        return [
          // '{b|' + ' ' + name + '}{a|' + '  |  ' + _themeAcceptObj[name].percent + '}{x|' + '    ' + _themeAcceptObj[name].num + '}'
          '{c|' + ' ' + name + '}{a|' + '  |  ' + _themeAcceptObj[name].percent + '}{x|' + '    ' + _themeAcceptObj[name].num + '}'
        ].join('\n')
      },
      textStyle: {
        lineHeight: 86,
        //rich放在textStyle里面
        rich: {
          a: {
            color: '#999',
            width: 40
          },
          b: {
            color: '#7F8590',

          },
          x: {
            color: '#7F8590',
            fontSize: 14,
            float: 'right'
          },
          c: {
            width: 80,
          },
        }
      },
      data: ['政府投资类', '社会投资类', '预先服务协同']
    },
    color: ['#3AA1FF', '#4ECB73', '#FBD437'],
    series: [{
      name: '访问来源',
      type: 'pie',
      radius: ['40%', '54%'],
      center: ['28%', '50%'],
      avoidLabelOverlap: false,
      label: {
        normal: {
          show: false,
        },

      },
      labelLine: {
        normal: {
          show: false
        }
      },
      itemStyle: {
        borderWidth: 3, //设置border的宽度有多大
        borderColor: '#fff',
      },
      data: [{
          value: 335,
          name: '政府投资类',
          label: {
            normal: {
              show: true,
              position: 'center',
              formatter: function (params) {
                return [
                  '{a|接件总数}',
                  '{b|' + _themeAcceptSum + '}'
                ].join('\n')
              },
              rich: {
                a: {
                  color: '#7F8590',
                  fontSize: 12,
                },

                b: {
                  color: '#575962',
                  fontSize: 18,
                  padding: [0, 0, 8, 0],
                },
              },
              textStyle: {
                fontSize: 15,
                color: '#999'
              }
            },

          },
        },
        {
          value: 310,
          name: '社会投资类'
        },
        {
          value: 234,
          name: '预先服务协同'
        },

      ]
    }]
  },

  // 阶段申报统计图options
  stageDeclareOpt: {
    title: {
      text: '阶段申报统计',
      color: '#999',
      x: 'left',
      textStyle: {
        color: '#7F8590',
        fontWeight: 'normal',
        fontSize: '14px'
      },
    },
    tooltip: {
      trigger: 'item',
      formatter: function (params) {
        // console.log(params)
        return [
          params.marker + params.name + '</br>' + '比例：' + params.percent + '%</br>' + '数量：' + params.value
        ].join('\n')
      },
    },
    legend: {
      orient: 'vertical',
      x2: 50,
      y: 'center',
      icon: 'circle',
      itemHeight: 10,
      formatter: function (name) {
        return [
          // '{b|' + ' ' + name + '}{a|' + '  |  ' + _stageAcceptObj[name].percent + '}{x|' + '    ' + _stageAcceptObj[name].num + '}'
          '{c|' + ' ' + name + '}{a|' + '  |  ' + _stageAcceptObj[name].percent + '}{x|' + '    ' + _stageAcceptObj[name].num + '}'
        ].join('\n')
      },
      textStyle: {
        lineHeight: 86,
        //rich放在textStyle里面
        rich: {
          a: {
            color: '#999',
            width: 40
          },
          b: {
            color: '#7F8590',

          },
          x: {
            color: '#7F8590',
            fontSize: 14,
            float: 'right'
          },
          c: {
            width: 100
          }
        }
      },
      data: ['立项用地规划许可', '工程建设许可', '施工许可', '竣工验收', '并行推进']
    },
    color: ['#3AA1FF', '#36CBCB', '#4ECB73', '#FBD437', '#F2637B'],
    series: [{
      name: '阶段',
      type: 'pie',
      radius: ['40%', '54%'],
      center: ['28%', '50%'],
      avoidLabelOverlap: false,
      label: {
        normal: {
          show: false,
        },

      },
      labelLine: {
        normal: {
          show: false
        }
      },
      itemStyle: {
        borderWidth: 3, //设置border的宽度有多大
        borderColor: '#fff',
      },
      data: [{
          value: 50,
          name: '立项用地规划许可',
          label: {
            normal: {
              show: true,
              position: 'center',
              formatter: function (params) {
                return [
                  '{a|接件总数}',
                  '{b|' + _stageAcceptSum + '}'
                ].join('\n')
              },
              rich: {
                a: {
                  color: '#7F8590',
                  fontSize: 12,
                },

                b: {
                  color: '#575962',
                  fontSize: 18,
                  padding: [0, 0, 8, 0],
                },
              },
              textStyle: {
                fontSize: 15,
                color: '#999'
              }
            },

          },
        },
        {
          value: 56,
          name: '工程建设许可'
        },
        {
          value: 44,
          name: '施工许可'
        },
        {
          value: 32,
          name: '竣工验收'
        },
        {
          value: 18,
          name: '并行推进'
        },

      ]
    }]
  },

  // 项目并联审批统计-options
  projectParallelApprovalOpt: {
    tooltip: {
      trigger: 'item',
      // formatter: "{a} <br/>{b} : {c} ({d}%)",
      formatter: function (params) {
        // console.log(params)
        return [
          params.marker + params.name + '</br>' + '比例：' + params.percent + '%</br>' + '数量：' + params.value
        ].join('\n')
      },
    },

    calculable: true,
    label: {
      normal: {
        show: true,
      },
    },
    labelLine: {
      normal: {
        show: true
      }
    },
    color: ['#3AA1FF', '#F2637B'],
    series: [{
      name: '项目',
      type: 'pie',
      radius: [18, 80],
      center: ['50%', '50%'],
      roseType: 'radius',
      label: {
        normal: {
          show: true,
          formatter: function (params) {
            // console.log(params)
            return [
              '{a|' + params.name + '}',
              '{a|' + params.value + '}{a|' + ' (' + params.percent + '%)' + '}'
            ].join('\n')
          },
          rich: {
            a: {
              color: '#7F8590',
              fontSize: 12,
              padding: [0, 0, 6, 0],
            },
          },
        },
      },
      data: [{
          value: 10,
          name: '一般单项',
        },
        {
          value: 5,
          name: '并联单项'
        },

      ]
    }]
  },

  // 申报来源统计-options
  declareSourceOpt: {
    tooltip: {
      trigger: 'item',
      // formatter: "{a} <br/>{b} : {c} ({d}%)"
      formatter: function (params) {
        // console.log(params)
        return [
          params.marker + params.name + '</br>' + '比例：' + params.percent + '%</br>' + '数量：' + params.value
        ].join('\n')
      },
    },

    calculable: true,
    label: {
      normal: {
        show: true,
      },
    },
    labelLine: {
      normal: {
        show: true
      }
    },
    color: ['#FBD437', '#3AA1FF'],
    series: [{
      name: '来源',
      type: 'pie',
      radius: [52, 80],
      center: ['50%', '50%'],
      // roseType: 'radius',
      itemStyle: {
        borderWidth: 0, //设置border的宽度有多大
        borderColor: '#fff',
      },

      label: {
        normal: {
          show: true,
          formatter: function (params) {
            // console.log(params)
            return [
              '{a|' + params.name + '}',
              '{a|' + params.value + '}{a|' + ' (' + params.percent + '%)' + '}'
            ].join('\n')
          },
          rich: {
            a: {
              color: '#7F8590',
              fontSize: 12,
              padding: [0, 0, 6, 0],
            },
          },
        },
      },
      data: [{
          value: 10,
          name: '网上大厅',
          selected:true
        },

        {
          value: 15,
          name: '现场大厅'
        },
      ]
    }]
  },
};