// 节流函数
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
var pager = new Vue({
  el: '#materialSupplement',
  data: function () {
    return {
      ctx: ctx,
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight), //当前屏幕高度
      loading: false, // 全屏loading
      loadingFile: false, // 文件上传loading
      loadingFileWin: false, // 窗口文件上传loading
      // 全局loading
      mloading: false,

      // 补正用到
      correctId: '',

      // 补全用到
      applyinstCorrectId: '',
      
      // 暂无数据提示
      noDataTip: '暂无数据',
      // 材料列表
      materialListData: [],
      // 列表总数
      total: 0,
      // 列表查询
      listSearchData: {
        pageNum: 1,
        pageSize: 10,
        userId: '',
        keyword: '',
      },
      isProjDetail: false,
   
    }
  },
  mounted: function () {
    var _that = this;
    window.addEventListener('scroll', _that.handleScroll);
    _that.getMaterialList();
  },
  
  methods: {
    
    // 公共方法
    // 获取url中的查询参数
    getSerachParamsForUrl: function (name) {
      var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      }
      return null;
    },
    // ui方法
    apiMessage: function (msg, type) {
      this.$message({
        message: msg,
        type: type
      })
    },
    
    handleScroll: function () { // 屏幕滚动纵向导航相应获取焦点
      var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
      var ele = $('.el-collapse-item');
      var eleLen = ele.length;
      var _that = this;
      for (var i = 0; i < eleLen; i++) {
        if (scrollTop >= ($(ele[i]).offset().top - 60)) {
          _that.activeTab = i
        }
      }
    },

    // 材料补全 --> 材料补全列表查询接口
    getMaterialList: function () {
      var ts = this;
      ts.mloading = true;
      request('', {
        url: ctx + 'rest/user/matCorrectAndSupply/list',
        type: 'get',
        data: ts.listSearchData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.materialListData = res.content.list;
          ts.total = res.content.total;
        } else {
          ts.$message.error(res.message)
        }
      }, function () {
        ts.mloading = false;
        ts.$message.error('网络错误！')
      });
    },

    // 输入关键词查询
    searchMatSupplementList: function () {
      this.getMaterialList();
    },

    handleSizeChange: function (val) {
      this.listSearchData.pageSize = val;
      this.getSearchProjList()
    },
    // 页面页码切换
    handleCurrentChange: function (val) {
      this.listSearchData.pageNum = val;
      this.getSearchProjList()
    },

    // 加载补正的详情操作页面
    jumpToSupplementDetail: function (row) {
      var ts = this;
      ts.isProjDetail = true;
      ts.row = row;
      // ts.correctId = row.correctId || ts.getSerachParamsForUrl('correctId');
      ts.correctId = row.applyinstCorrectAndSuppleId || ts.getSerachParamsForUrl('correctId');
      $.get(ctx + 'rest/user/toMatSupplementDetail', function (result) {
        $('#projectDetail').html(result);
      });
    },

    // 加载补全的详情操作页面
    jumpToCompletionDetail: function (row) {
      var ts = this;
      ts.isProjDetail = true;
      ts.row = row;
      // ts.applyinstCorrectId = row.applyinstCorrectId;
      ts.applyinstCorrectId = row.applyinstCorrectAndSuppleId;
      $.get(ctx + 'rest/user/toMatCompletionDetail', function (result) {
        $('#projectDetail').html(result);
      });
    },

  },
  filters: {
    formatDate: function (val, mat) {
      if (!val) return '暂无'
      var _date = new Date(val);
      return formatDate(_date, 'yyyy-MM-dd hh:mm')
    },
  },
});