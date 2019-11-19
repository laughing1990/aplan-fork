// 此处的echart参数适用于单个窗口的效能督查页面

// 接件受理情况总数
var _acceptHandelPieSum = 0;

var singleWinChartOptions = {

  // 接件受理情况options
  acceptHandelPieChartOpt: {
    title: {
      show: true,
      text: '窗口接件总数：' + _acceptHandelPieSum,
      left: -4,
      top: 'top',
      textStyle: {
        color: '#575962',
        fontSize: 16,
      },
    },
    tooltip: {
      trigger: 'item',
      // formatter: "{b}<br/> " + '' + "{c} ({d}%)",
      formatter: function(params){
        // console.log(params)
        return [
          params.marker + params.name + '</br>' + '数量：' + params.value + '</br>' + '占比：' + params.percent + '%'
        ].join('\n')
      },
    },
    series: [{
      name: '接件分布',
      type: 'pie',
      radius: '55%',
      center: ['50%', '50%'],
      color: ['#32C4E8', '#40ABFF', '#FD634D', '#FEDA5F', '#E7BCF2'],
      itemStyle: {
        normal: {
          borderWidth: 2,
          borderColor: '#ffffff',
        }
      },
      label: {
        normal: {
          formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：{c}}  {per|{d}%}  ',
          backgroundColor: '#FEFEFE',
          borderColor: '#DCDFE6',
          borderWidth: 1,
          borderRadius: 4,
          // shadowBlur:3,
          // shadowOffsetX: 2,
          // shadowOffsetY: 2,
          // shadowColor: '#999',
          // padding: [0, 7],
          rich: {
            a: {
              color: '#999',
              lineHeight: 34,
              align: 'center',
              fontSize: 14,
            },
            // abg: {
            //     backgroundColor: '#333',
            //     width: '100%',
            //     align: 'right',
            //     height: 22,
            //     borderRadius: [4, 4, 0, 0]
            // },
            hr: {
              borderColor: '#DCDFE6',
              width: '100%',
              borderWidth: 0.5,
              height: 0
            },
            b: {
              color: '#575962',
              fontSize: 14,
              lineHeight: 33,
            },
            per: {
              color: '#eee',
              backgroundColor: '#42AFFA',
              padding: [4, 4],
              borderRadius: 4
            }
          }
        }
      },
      data: [{
          value: 335,
          name: '直接访问'
        },
        {
          value: 310,
          name: '邮件营销'
        },
        {
          value: 234,
          name: '联盟广告'
        },
        {
          value: 135,
          name: '视频广告'
        },
        {
          value: 1548,
          name: '搜索引擎'
        }
      ],
    }]
  },

  //阶段受理情况options
  stageHandelBarChartOpt: {
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
      data: ['已受理', '材料补全', '不予受理'],
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
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
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
      data: [200, 200, 200, 200, 200, 200, 200],
      name: '已受理',
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
      name: '材料补全',
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
      name: '不予受理',
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

  // 接件历史分析
  accepHistoryEffectLineChartOpt: {
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
}