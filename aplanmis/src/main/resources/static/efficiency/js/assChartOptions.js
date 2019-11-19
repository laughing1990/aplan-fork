// 自定义好的颜色数组
var _diyColorList = ['#38a1d9', '#32C4E8', '#66DFE2', '#9EE5B8', '#FEDA5F', '#FE9E7F', '#FA7292', '#E062AC', '#E690D0', '#E7BCF2',
  '#9D96F3', '#8378E8', '#96BFFD', '#5C86FF', '#169AFF', '#4EB2FD', '#07CDC6', '#00C161', '#FFB822', '#785447', '#785447'
];

// 接件总数（由调用里面去计算）
var _acceptSum = 0;
// 主题申报接件统计-分类的比例
var _acceptSumObj = {

}

// 接件总量options-twoLabel
var _accepNumTwoLabel = {
  normal: {
    show: true,
    position: 'center',
    formatter: function (params) {
      return [
        '{a|接件总量}',
        '{b|' + _acceptSum + '}'
      ].join('\n')
    },
    rich: {
      a: {
        color: '#7F8590',
        fontSize: 12,
      },

      b: {
        color: '#575962',
        fontSize: 28,
        fontWeight: 900,
        padding: [0, 0, 12, 0],
      },
    },
    textStyle: {
      fontSize: 15,
      color: '#999'
    }
  },

}


// 接件受理情况-放大缩小组件
var _acceptConnectionOpt_datazoom = [{
  show: true,
  realtime: true,
  start: 0,
  end: 15
}, {
  type: 'inside',
  realtime: true,
  start: 0,
  end: 50
}]



// 时限分析专题相关配置项
// 申报时限状态-全市申报量
var _approveTimelimitAccSum = 0;
// 申报时限状态-饼图右侧lend图例栏
var _approveTimelimitLendObj = {};  
// 申报时限状态-饼图中间全市申报量的显示label
var _approveTimelimitAccSumLabel = {
  normal: {
    show: true,
    position: 'center',
    formatter: function (params) {
      return [
        '{a|接件总数}',
        '{b|' + _approveTimelimitAccSum + '}'
      ].join('\n')
    },
    rich: {
      a: {
        color: '#7F8590',
        fontSize: 12,
      },
      b: {
        color: '#575962',
        fontSize: 28,
        padding: [0, 0, 8, 0],
      },
    },
    textStyle: {
      fontSize: 15,
      color: '#999'
    }
  },
};



// 部门人员-效能督查
var assEchartOptions = {

  // 接件总量options
  acceptSumOpt: {
    title: {
      show: false,
      text: '主题申报统计',
      color: '#999',
      x: 'right',
      y: 'top',
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
          params.marker + params.name + '</br>' + '比例：' + Number(params.percent).toFixed(0) + '%</br>' + '数量：' + params.value
        ].join('\n')
      },

    },
    legend: {
      type: 'scroll',
      orient: 'vertical',
      x2: 2,
      y: 'center',
      // y: '20',
      icon: 'circle',
      itemHeight: 12,
      itemGap: 20,
      pageIconSize: 10,
      pageIconColor: '#6495ed',
      pageTextStyle: {
        color: '#333',
      },
      pageIcons: {

      },
      formatter: function (name) {
        // console.log(name)
        return [
          '{c|' + ' ' + (name.length > 5 ? name.substring(0, 5) + '...' : name) + '}{a|' + '    ' + _acceptSumObj[name].num + '}{x|' + '   ' + _acceptSumObj[name].percent + '}'
        ].join('\n')
      },
      textStyle: {
        lineHeight: 86,
        //rich放在textStyle里面
        rich: {
          a: {
            color: '#575962',
            width: 80,
            fontSize: 14,
            fontWeight: 900
          },
          b: {
            color: '#7F8590',
            fontSize: 14,
          },
          x: {
            color: '#575962',
            fontSize: 14,
            float: 'right'
          },
          c: {
            color: '#575962',
            width: 100,
            fontSize: 14,
          },
        }
      },
      data: ['社会投资类', '政府投资类', '预先服务协同', '预先服务协同1', '预先服务协同2', '预先服务协同3', '预先服务协同4', '预先服务协同5']
    },
    // color: ['#3AA1FF', '#4ECB73', '#FBD437'],
    // 根据窗口名得到指定的颜色值
    color: function (value) {
      // console.log(value)
      // var colorHash = new ColorHash();
      // var hex = colorHash.hex(value.name)
      return value.data.itemColor;
    },
    series: [{
      name: '访问来源',
      type: 'pie',
      radius: ['40%', '54%'],
      // center: ['28%', '50%'],
      center: ['19%', '50%'],
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
          value: 0,
          name: '社会投资类',
          label: {
            normal: {
              show: true,
              position: 'center',
              formatter: function (params) {
                return [
                  '{a|接件总量}',
                  '{b|' + _acceptSum + '}'
                ].join('\n')
              },
              rich: {
                a: {
                  color: '#7F8590',
                  fontSize: 12,
                },

                b: {
                  color: '#575962',
                  fontSize: 28,
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
          value: 50,
          name: '政府投资类',

        }
      ]
    }]
  },

  //接件受理情况options
  acceptConnectionOpt: {
    dataset: {
      source: [
        ['pro', '已受理', '材料补正', '不予受理'],
        ['区1', 43.3, 85.8, 93.7],
        ['区2', 83.1, 73.4, 55.1],
        ['区3', 86.4, 65.2, 82.5],
        ['区4', 72.4, 53.9, 39.1]
      ]
    },
    backgroundColor: '#fff',
    // x轴与画板底部的距离
    grid: {
      top: 48,
      // y2: 20
      y2: 60,
      left: 30,
      right: 6,
    },
    legend: {
      // data: ['已受理', '材料补正', '不予受理'],
      textStyle: {

      },
      icon: "circle",
      x: 'right',
    },
    tooltip: {
      trigger: 'axis',
      // formatter: function (params) {
      //   console.log(params)
      //   var str = "";
      //   if (params[0]) {
      //     str += (params[0].name + '<br /> ' + params[0].marker + '  已受理：' + params[0].value)
      //   }
      //   if (params[1]) {
      //     str += ('<br /> ' + params[1].marker + '  材料补全：' + params[1].value)
      //   }
      //   if (params[2]) {
      //     str += ('<br /> ' + params[2].marker + '  不予受理数：' + params[2].value)
      //   }
      //   return [str].join('\n');
      // },
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
      // data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
      name: '单位（件）',
      type: 'value',
      axisLine: {
        show: false
      },
      splitLine: {
        show: true,
        lineStyle: {
          type: 'solid',
          color: ['#EBEDF2']
        }
      }
    },
    series: [{
      // data: [200, 200, 200, 200, 200, 200, 200],
      name: '已受理',
      type: 'bar',
      barMaxWidth: 20,
      itemStyle: {
        normal: {
          barBorderRadius: 4,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: '#2EBCDA'
          }, {
            offset: 1,
            color: '#4E84FD'
          }])
        }
      },
    }, {
      // data: [100, 100, 140, 30, 50, 30, 110],
      name: '材料补正',
      type: 'bar',
      barMaxWidth: 20,
      barGap: '100%',
      itemStyle: {
        normal: {  
          label: {
            show: false,
            position: 'top',
            formatter: '{b}\n{c}'
          },
          barBorderRadius: 4,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: '#FABF4B'
          }, {
            offset: 1,
            color: '#FF9622'
          }])
        }
      },
    }, {
      // data: [120, 200, 150, 80, 70, 110, 130],
      name: '不予受理',
      type: 'bar',
      barMaxWidth: 20,
      barGap: '100%',
      itemStyle: {
        normal: {
          label: {
            show: false,
            position: 'top',
            formatter: '{b}\n{c}'
          },
          barBorderRadius: 4,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: '#FE7674'
          }, {
            offset: 1,
            color: '#F15957'
          }])
        }
      },
    }, ]
  },

  // 接件历史分析
  acceptHandelOpt: {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['已接件', '已受理'],
      // x:'right',
      x2: 30,
      y: 30,
    },
    grid: {
      left: '1%',
      right: '1%',
      bottom: '2%',
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
      name: '单位（件）',
      // minInterval: 1,
      axisLine: {
        show: false
      },
    },
    series: [{
        name: '已接件',
        type: 'line',
        color: '#4EB2FD',
        smooth: false,
        symbolSize: 8,
        data: [110, 102, 101, 204, 390, 30, 10]
      },
      {
        name: '已受理',
        type: 'line',
        color: '#1DCCD6',
        smooth: false,
        symbolSize: 8,
        data: [20, 122, 198, 204, 990, 330, 310]
      }
    ]
  },


   // 时限分析专题
  // 申报时限状态-饼图options
  approveTimelimitStatusPieChartOpt: {
    title: {
      show: false,
      text: '主题申报统计',
      color: '#999',
      x: 'right',
      y: 'top',
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
          params.marker + params.name + '</br>' + '申报数：' + params.value  + '</br>占比：' +  Number(params.percent).toFixed(0) + '%'
        ].join('\n')
      },

    },
    legend: {
      type: 'scroll',
      orient: 'vertical',
      x2: 2,
      y: 'center',
      // y: '20',
      icon: 'circle',
      itemHeight: 12,
      itemGap: 20,
      pageIconSize: 10,
      pageIconColor: '#6495ed',
      pageTextStyle: {
        color: '#333',
      },
      pageIcons: {

      },
      formatter: function (name) {
        // console.log(name)
        return [
          '{c|' + ' ' + (name.length > 5 ? name.substring(0, 5) + '...' : name) + '}{a|' + '    ' + _approveTimelimitLendObj[name].num + '}{x|' + '' + _approveTimelimitLendObj[name].percent + '}'
        ].join('\n')
      },
      textStyle: {
        lineHeight: 86,
        //rich放在textStyle里面
        rich: {
          a: {
            color: '#575962',
            width: 60,
            fontSize: 14,
            fontWeight: 900
          },
          b: {
            color: '#7F8590',
            fontSize: 14,
          },
          x: {
            color: '#575962',
            fontSize: 14,
            align: 'center',
            width: 40,
          },
          c: {
            color: '#575962',
            width: 50,
            fontSize: 14,
          },
        }
      },
      data: ['正常', '预警', '逾期']
    },
    color: ['#3FA1FD', '#FFB822', '#F1637A'],
    series: [{
      name: '访问来源',
      type: 'pie',
      radius: ['40%', '54%'],
      // center: ['28%', '50%'],
      center: ['24%', '50%'],
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
          value: 145,
          name: '正常',
          label: {
            normal: {
              show: true,
              position: 'center',
              formatter: function (params) {
                return [
                  '{a|全市申报}',
                  '{b|' + _approveTimelimitAccSum + '}'
                ].join('\n')
              },
              rich: {
                a: {
                  color: '#7F8590',
                  fontSize: 12,
                },

                b: {
                  color: '#575962',
                  fontSize: 28,
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
          value: 50,
          name: '预警',        
        },
        {
          value: 5,
          name: '逾期',     
        }
      ]
    }]
  },

  // 申报阶段平均用时-柱状图opt
  approveStageAverageTimeBarChartOpt: {
    dataset: {
      source: [
        ['pro', '2015', '2016', '2017'],
        ['Matcha Latte', 43.3, 85.8, 93.7],
        ['Milk Tea', 83.1, 73.4, 55.1],
        ['Cheese Cocoa', 86.4, 65.2, 82.5],
        ['Walnut Brownie', 72.4, 53.9, 39.1]
      ]
    },
    backgroundColor: '#fff',
    // x轴与画板底部的距离
    grid: {
      top: 48,
      // y2: 20
      y2: 60,
      left: 30,
      right: 6,
    },
    legend: {
      icon: "circle",
      x: 'right',
      itemGap: 40,
    },
    tooltip: {
      trigger: 'axis',
      // formatter: function (params) {
      //   console.log(params)
      //   var str = "";
      //   if (params[0]) {
      //     str += (params[0].name + '<br /> ' + params[0].marker + '  已受理：' + params[0].value)
      //   }
      //   if (params[1]) {
      //     str += ('<br /> ' + params[1].marker + '  材料补全：' + params[1].value)
      //   }
      //   if (params[2]) {
      //     str += ('<br /> ' + params[2].marker + '  不予受理数：' + params[2].value)
      //   }
      //   return [str].join('\n');
      // },
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
      // axisLine: {
      //   lineStyle: {
      //     color: "#999",
      //   }
      // },
    },
    yAxis: {
      name: '单位（件）',
      type: 'value',
      axisLine: {
        show: true
      },
      splitLine: {
        show: true,
        lineStyle: {
          type: 'solid',
          color: ['#EBEDF2']
        }
      }
    },
    series: [{
        type: 'bar',
        // barWidth: 20,
        barMaxWidth: 20,
        itemStyle: {
          normal: {
            color: '#3FA1FD',
            barBorderRadius: [4, 4, 4, 4],
          }
        },
      },
      {
        type: 'bar',
        // barWidth: 20,
        barMaxWidth: 20,
        barGap: '30%',
        itemStyle: {
          normal: {
            color: '#FFB822',
            barBorderRadius: [4, 4, 4, 4],
          }
        },
      },
      {
        type: 'bar',
        // barWidth: 20,
        barMaxWidth: 20,
        barGap: '30%',
        itemStyle: {
          normal: {
            color: '#F1637A',
            barBorderRadius: [4, 4, 4, 4],
          }
        },
      },
      
    ]
  },

  // 主题申报用时-柱状图options
  themeApproveTimeBarChartOpt: {
    backgroundColor: '#fff',
    // x轴与画板底部的距离
    grid: {
      top: 48,
      // y2: 20
      y2: 60,
      left: 30,
      right: 6,
    },
    legend: {
      data: ['最长用时', '平均用时', '最短用时'],
      textStyle: {

      },
      icon: "circle",
      x: 'right',
    },
    tooltip: {
      trigger: 'axis',
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
      name: '单位（天）',
      type: 'value',
      axisLine: {
        show: false
      },
      splitLine: {
        show: true,
        lineStyle: {
          type: 'solid',
          color: ['#EBEDF2']
        }
      }
    },
    series: [{
      data: [200, 200, 200, 200, 200, 200, 200],
      name: '最长用时',
      type: 'bar',
      barMaxWidth: 20,
      itemStyle: {
        normal: {
          // color: "#05CCC5",
          barBorderRadius: 4,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: '#05CCC5'
          }, {
            offset: 1,
            color: '#2AC6E8'
          }])
        }
      },
    }, {
      data: [100, 100, 140, 30, 50, 30, 110],
      name: '平均用时',
      type: 'bar',
      barMaxWidth: 20,
      barGap: '30%',
      itemStyle: {
        normal: {
          color: '#1C9AFD',
          label: {
            show: false,
            position: 'top',
            formatter: '{b}\n{c}'
          },
          barBorderRadius: 4,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: '#70A1FF'
          }, {
            offset: 1,
            color: '#40ABFF'
          }])
        }
      },
    }, {
      data: [120, 200, 150, 80, 70, 110, 130],
      name: '最短用时',
      type: 'bar',
      barMaxWidth: 20,
      barGap: '30%',
      itemStyle: {
        normal: {
          color: '#FEB729',
          label: {
            show: false,
            position: 'top',
            formatter: '{b}\n{c}'
          },
          barBorderRadius: 4,
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: '#FD8280'
          }, {
            offset: 1,
            color: '#F26A68'
          }])
        }
      },
    }, ]
  },

};