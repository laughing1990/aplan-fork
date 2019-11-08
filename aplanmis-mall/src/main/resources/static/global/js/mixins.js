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

    navLeft: function(){
      var _windW = $(window).width();
      var _contentL = $('.module-wrap').offset().left;
      setTimeout(function(){
        if(_windW>1260){
          // return _contentL -20 + 'px'
          // debugger
          $('.module-nav').css('left', _contentL -20 + 'px')
        }else{
          $('.module-nav').css('left','20px')
        }
      },100)
      
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
    }
  }
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

var dateFormat = function (fmt) { //author: meizz   
  var o = {
    "M+": this.getMonth() + 1, //月份   
    "d+": this.getDate(), //日   
    "h+": this.getHours(), //小时   
    "m+": this.getMinutes(), //分   
    "s+": this.getSeconds(), //秒   
    "q+": Math.floor((this.getMonth() + 3) / 3), //季度   
    "S": this.getMilliseconds() //毫秒   
  };
  if (/(y+)/.test(fmt))
    fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt))
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
}