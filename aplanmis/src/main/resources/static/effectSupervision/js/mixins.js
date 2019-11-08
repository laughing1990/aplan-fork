var mixins = {
  data: function () {
    return {
      // 全局页面loading
      loading: false,
      // 当前天-时间戳范围
      curDayStampRange: {
        startTime: '',
        endTime: ''
      },

      // 计算屏幕宽度，小于多少时隐藏后两列（中小屏）
      pcClientSmallTag: false,
      // 计算屏幕宽度，小于多少时隐藏中间列 （超小屏）
      pcClientTooSmallTag: false,
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

    // 计算当前天零点从当前时间的时间戳范围
    calculateCurDayStempRange: function () {
      var curDate = new Date();
      this.curDayStampRange.endTime = curDate.getTime();
      curDate.setHours(0);
      curDate.setMinutes(0);
      curDate.setSeconds(0);
      curDate.setMilliseconds(0);
      this.curDayStampRange.startTime = new Date(curDate).getTime();
      // console.log(this.curDayStampRange)
    },

    // 是否是当前天-时间戳计算
    isCurDate: function (val) {
      if (!val) return false;
      if (this.curDayStampRange.startTime < val && val < this.curDayStampRange.endTime) {
        return true;
      } else {
        return false;
      }
    },

    // 是否是当前三天内
    isPreThreeDate: function(val){
      var startTime = '', endTime = '';
      var curDate = new Date();
      endTime = curDate.getTime();
      curDate.setDate(curDate.getDate() - 3)
      curDate.setHours(0);
      curDate.setMinutes(0);
      curDate.setSeconds(0);
      curDate.setMilliseconds(0);
      startTime = new Date(curDate).getTime();
      if (!val) return false;
      if (startTime < val && val < endTime) {
        return true;
      } else {
        return false;
      }
    },

    // 表格剩余时间 范围计算
    dueNumFn: function(time){
      var time = Number(time) || 0
      // console.log(time)
      if(1<time){
        return 'day-success-badge'
      }
      if(0<time && time<=1){
        return 'day-warn-badge'
      }
      if(time<=0){
        return 'day-error-badge'
      }
    },

    //判断是否已签收-待办列表
    isSign: function (row) {
      // console.log(row.signState)
      if(row.signState){
          if(1.0==row.signState || 1==row.signState){
              return true;
          }
      }

      return false;
    },

    // 表格无值填充-=el-table
    formatterTableCell: function(row, column, cellValue, index){
      if(!cellValue)return '-';
      return cellValue;
    },
    // 表格单元格样式
    tabCellName: function(){
      return 'tab-cell-class'
    },
    // 计算屏幕的尺寸-并决定隐藏哪些lie
    calculatePcClientTag: function () {
      // console.log(document.body.offsetWidth)
      var bodyOffsetW = document.body.offsetWidth;
      if (bodyOffsetW < 1722) {
        this.pcClientSmallTag = true;
      }else{
        this.pcClientSmallTag = false;
      }

      if (bodyOffsetW < 1420) {
        this.pcClientTooSmallTag = true;
      }else{
        this.pcClientTooSmallTag = false;
      }
    },
    // 处理日期
    formatDatetimeCommon:function(value,format) {
      if (value == null || value == '') {
          return '';
      }
      var dt;
      if (value instanceof Date) {
          dt = value;
      } else if(typeof value == "number"){
          dt = new Date(value);
      }else if(typeof value == "string"){
          try{
              dt = new Date(Date.parse(value.replace(/-/g,"/")));
          }catch (e){
              return value;
          }
      }
      return dt.format(format);
    },

    // 处理阶段/事项/辅线名称列
    stageFormatter : function (row) {
        if(row.stageName && row.itemName){
            return '<div class="step-status" >' +
                '        <span class="step-status-item green-status"><span class="item-name ellipsis">'+row.stageName+'</span></span>' +
                '        <span class="step-status-item gary-status long-status-item"><span class="item-name ellipsis">'+row.itemName+'</span></span>' +
                '    </div>';
        }else if(row.stageName && !row.itemName ){
            var html = "<div class=\"flow_steps\">" +
                "<ul class='stage'>";
            if(row.stageName.indexOf("立项用地") != -1){
                html += "<li class=\"current\">立项用地</li>" +
                    "<li>&nbsp;</li>" +
                    "<li>&nbsp;</li>" +
                    "<li >&nbsp;</li>";
            }else if(row.stageName.indexOf("工程建设") != -1){
                html += "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"current\">工程建设</li>" +
                    "<li>&nbsp;</li>" +
                    "<li >&nbsp;</li>";
            }else if(row.stageName.indexOf("施工许可") != -1){
                html += "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"current\">施工许可</li>" +
                    "<li >&nbsp;</li>";
            }else if(row.stageName.indexOf("竣工验收") != -1){
                html += "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"stage-success\">&nbsp;</li>" +
                    "<li class=\"current\">竣工验收</li>";
            }else{
                return '<span>'+row.stageName+'</span>';
            }

            html += "</ul>" +
                "</div>";

            return html;
        }else if(row.itemName){
            return '<span>'+row.itemName+'</span>';
        }

        return "-";
    },

    // 显示主题、阶段、事项信息
    showThemeStageItemInfo:function (row) {
        var html = '';
        if (row.themeName) {
            html = html + "所属主题 : " + row.themeName + "</br></br>";
        }

        if(row.stageName) {
            html = html + "所处阶段 : " + row.stageName + "</br></br>";
        }

        if(row.itemName) {
            html = html + "申报事项 : " + row.itemName;
        }

        return html;
    },
  },
  created: function () {
    this.calculateCurDayStempRange();
  },
  mounted: function(){
    var ts = this;
    this.calculatePcClientTag();
    window.onresize = throttle(function () {
      ts.calculatePcClientTag();
    }, 100, 500, 100)
  },
  filters: {
    // 剩余时间-显示处理
    dueNumFormat: function(num){
      var num = Number(num);
      if(num < 0){
        return '逾期' + Math.abs( num ) + '天'
      }else{
        return '剩余' + num + '天'
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

// 获取当前月的开始与结束日期
function culatorCurMonthDateRange() {
  var date = new Date()
  var end = formatDate(date, 'yyyy-MM-dd');
  date.setDate(1)
  var start = formatDate(date, 'yyyy-MM-dd');
  return [start, end] || [];
};

Date.prototype.format = function (format) {
  var o = {
      "M+": this.getMonth() + 1, // month
      "d+": this.getDate(), // day
      "h+": this.getHours(), // hour
      "m+": this.getMinutes(), // minute
      "s+": this.getSeconds(), // second
      "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
      "S": this.getMilliseconds()// millisecond
  }
  if (/(y+)/.test(format))
      format = format.replace(RegExp.$1, (this.getFullYear() + "")
          .substr(4 - RegExp.$1.length));
  for (var k in o)
      if (new RegExp("(" + k + ")").test(format))
          format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
  return format;
}