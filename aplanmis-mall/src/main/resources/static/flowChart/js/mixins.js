var mixins = {
  data: function () {
    return {
      // 全局页面loading
      loading: false,
    }
  },
  methods: {
    // 处理接口message
    apiMessage: function (msg, type) {
      this.$message({
        message: msg,
        type: type
      })
    },

    // 处理ele-table中的单元格字段中值为null或为空
    formatTableCell: function (row, column, cellValue, index) {
      if (!cellValue || cellValue == null) {
        return '无'
      }
      return cellValue;
    },

    // 展开-关闭module
    collapseModule: function ($event) {
      // var _mod = $($event.target);
      // _mod.toggleClass('active');
      // _mod.siblings(":first").slideToggle(300)
      // if ($event.target.tagName.toLowerCase() == 'h2') {
      //   _mod.parent().toggleClass('active')
      //   _mod.parent().siblings(":first").slideToggle(300)
      // }
      // $event.currentTarget能指向绑定这个事件的元素上面，无论你是点击的是绑定事件的元素还是里面的子元素
      var _mod = $($event.currentTarget);
      _mod.toggleClass('active');
      _mod.siblings(":first").slideToggle(300)
    },

    // div滚动行为
    whereDiv: function (cid) {
      var currentscrollTop = $(document).scrollTop();
      var top = $(cid).offset().top;
      var height = $(cid).outerHeight(true);
      if (currentscrollTop - top > 0 && currentscrollTop - top < height) {
        console.log("当前正压着" + $(cid).data('module'));
        this.curInEyeModule = $(cid).data('module');
      }
    },

    // 获取url中的查询参数
    getSerachParamsForUrl: function (name) {
      var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      }
      return null;

    },

    navLeft: function () {
      var _windW = $(window).width();
      var _contentL = $('.module-wrap').offset().left;
      setTimeout(function () {
        if (_windW > 1260) {
          // return _contentL -20 + 'px'
          // debugger
          $('.module-nav').css('left', _contentL - 20 + 'px')
        } else {
          $('.module-nav').css('left', '20px')
        }
      }, 100)

    },

    // 防抖动
    debounce: function (fn, delay) {
      // 维护一个 timer
      var timer = null;

      return function () {
        // 通过 ‘this’ 和 ‘arguments’ 获取函数的作用域和变量
        var context = this;
        var args = arguments;

        clearTimeout(timer);
        timer = setTimeout(function () {
          fn.apply(context, args);
        }, delay);
      }
    },

    // 列表过度方法
    beforeEnter: function (dom) {
      dom.classList.add("list-enter", "list-enter-active");
    },
    enter: function (dom, done) {
      var delay = "";
      if (dom.dataset) {
        delay = dom.dataset.delay
      } else {
        delay = dom.getAttribute('data-delay')
      }
      // var delay = $(dom).data('delay');
      setTimeout(function () {
        dom.classList.remove("list-enter");
        dom.classList.add("list-enter-to");
        //监听 transitionend 事件
        var transitionend = window.ontransitionend ?
          "transitionend" :
          "webkitTransitionEnd";
        dom.addEventListener(transitionend, function onEnd() {
          dom.removeEventListener(transitionend, onEnd);
          done(); //调用done() 告诉vue动画已完成，以触发 afterEnter 钩子
        });
      }, delay);
    },
    afterEnter: function (dom) {
      dom.classList.remove("list-enter-to", "list-enter-active");
    },
  },
  filters: {
    // 阶段名format
    stageNameFormat: function(val){
      if(!val)return '暂无';
      var stageList = ['立项用地', '工程建设', '施工许可','竣工验收'];
      for(var i = 0, len = stageList.length; i<len; i++){
        if(val.indexOf(stageList[i])!== -1){
          return stageList[i];
          break;
        }
      }
    },
  },
}
var debounce = function (fn, delay) {
  // 维护一个 timer
  var timer = null;

  return function () {
    // 通过 ‘this’ 和 ‘arguments’ 获取函数的作用域和变量
    var context = this;
    var args = arguments;

    clearTimeout(timer);
    timer = setTimeout(function () {
      fn.apply(context, args);
    }, delay);
  }
}

var throttle = function (method, delay, time) {
  var timeout, startTime = +new Date();
  return function () {
    var context = this,
      args = arguments,
      curTime = +new Date();
    clearTimeout(timeout);
    // 如果达到了规定的触发时间间隔，触发 handler
    if (curTime - startTime >= time) {
      method.apply(context, args);
      startTime = curTime;
    } else {
      // 没达到触发间隔，重新设定定时器
      timeout = setTimeout(method, delay);
    }
  }
}

// dateFormat
function formatDate(date, fmt) {
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  var o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds()
  }
  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      var str = o[k] + ''
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str))
    }
  }
  return fmt
}

function padLeftZero(str) {
  return ('00' + str).substr(str.length)
}
// 获取当前天的开始与结束日期
function culatorCurDayDateRange() {
  var date = new Date()
  var end = formatDate(date, 'yyyy-MM-dd')
  date.setHours(0);
  date.setMinutes(0);
  date.setSeconds(0);
  date.setMilliseconds(0);
  var start = date.getTime()
  start = formatDate(new Date(start), 'yyyy-MM-dd')
  return [start, end] || [];
};
// 获取当前周的开始与结束日期
function culatorCurWeekDateRange() {
  var date = new Date()
  var weekday = date.getDay() || 7
  var end = formatDate(date, 'yyyy-MM-dd');
  date.setDate(date.getDate() - weekday + 1)
  var start = formatDate(date, 'yyyy-MM-dd');
  return [start, end] || [];
};
// 获取当前月的开始与结束日期
function culatorCurMonthDateRange() {
  var date = new Date()
  var end = formatDate(date, 'yyyy-MM-dd');
  date.setDate(1)
  var start = formatDate(date, 'yyyy-MM-dd');
  return [start, end] || [];
};
// 获取当前月的开始与结束日期-前一天
function culatorCurMonthDateRangePreOne() {
  var date = new Date()
  var preDate = new Date(date.getTime() - 24*60*60*1000)
  var end = formatDate(preDate, 'yyyy-MM-dd');
  date.setDate(1)
  var start = formatDate(date, 'yyyy-MM-dd');
  return [start, end] || [];
};
// 获取当前年的开始与结束日期
function culatorCurYearDateRage(){
  var date = new Date()
  var end = formatDate(date, 'yyyy-MM-dd');
  date.setMonth(0)
  var start = formatDate(date, 'yyyy-MM-dd');
  return [start, end] || [];
};