// 此处的echart参数适用于单个窗口的效能督查页面

// 部门接件量-接件总数（由调用里面去计算）
var _dacceptSum = 0;
// 部门接件量接件统计-分类的比例
var _dacceptSumObj = {

}
// 部门接件总量-饼图中间的总量显示-用label替代显示
var _doneLabel = {
  normal: {
    show: true,
    position: 'center',
    formatter: function (params) {
      return [
        '{a|部门接件量}',
        '{b|' + _dacceptSum + '}'
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

var singleAssChartOptions = {

  // 部门接件量饼图-options
  departAcceptPieChartOpt: {
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
          params.marker + params.name + '</br>' + '申报数：' + params.value + '</br>' + '比例：' + Number(params.percent).toFixed(0) + '%'
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
          '{c|' + ' ' + (name.length > 10 ? name.substring(0, 10) + '...' : name) + '}{a|' + '    ' + _dacceptSumObj[name].num + '}{x|' + '   ' + _dacceptSumObj[name].percent + '}'
        ].join('\n')
      },
      textStyle: {
        lineHeight: 86,
        //rich放在textStyle里面
        rich: {
          a: {
            color: '#575962',
            width:44,
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
            width: 170,
            fontSize: 14,
          },
        }
      },
      data: []
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
      center: ['18%', '50%'],
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
      data: [
      ]
    }]
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