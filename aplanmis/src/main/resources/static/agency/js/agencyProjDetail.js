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

var allowPreType = {
  'doc': true,
  'docx': true,
  'pdf': true,
  'ppt': true,
  'pptx': true,
  'xls': true,
  'xlsx': true,
  'txt': true
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
  el: '#app',
  data: function () {
    var checkMissValue = function (rule, value, callback) {
      if (value === '' || value === undefined || value === null) {
        callback(new Error('该输入项为必输项！'));
      } else if (value.toString().trim() === '') {
        callback(new Error('该输入项为必输项！'));
      } else {
        callback();
      }
    };
    // 输入为数字 大于0
    var checkMissNum = function (rule, value, callback) {
      if (value === '' || value === undefined || value === null || value.toString().trim() === '') {
        callback(new Error('必填！'));
      } else if (value) {
        var flag = !/^[0-9]\d*$/.test(value) && !(/^[0-9]\d*$/.test(value));
        if (flag) {
          return callback(new Error('格式错误'));
        } else {
          if (parseInt(value) <= 0) {
            return callback(new Error('不能为0'));
          } else {
            callback();
          }
        }
      } else {
        // callback();
        if (parseInt(value) <= 0) {
          return callback(new Error('不能为0'));
        } else {
          callback();
        }
      }
    };
    var checkNumber = function (rule, value, callback) {
      var reg = /[^\d^\.]+/g
      // console.log(reg.test(value));
      if (!reg.test(value)) {
        callback();
      } else {
        return callback(new Error('请输入数字'));
      }
    };
    // 输入为数字 大于0 ===>使用在材料的电子件当中
    var checkMissNumFormat = function (rule, value, callback) {
      if (value === '' || value === undefined || value === null || value.toString().trim() === '') {
        callback(new Error('请上传！'));
      } else if (value) {
        var flag = !/^[0-9]\d*$/.test(value) && !(/^[0-9]\d*$/.test(value));
        if (flag) {
          return callback(new Error('格式错误'));
        } else {
          if (parseInt(value) <= 0) {
            return callback(new Error('请上传！'));
          } else {
            callback();
          }
        }
      } else {
        // callback();
        if (parseInt(value) <= 0) {
          return callback(new Error('请上传！'));
        } else {
          callback();
        }
      }
    };
    // 校验不能纯数字(非必填)
    var checkNotAllNumber = function (rule, value, callback) {
      var reg = /[^\d^\.]+/g
      // console.log(reg.test(value));
      if (!value || reg.test(value)) {
        callback();
      } else {
        return callback(new Error('不能为纯数字'));
      }
    };
    var checkPhoneNum = function (rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('必填！'));
      } else if (value) {
        var flag = !/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/.test(value) && !(/^1(3|4|5|6|7|8|9)\d{9}$/.test(value));
        if (flag) {
          return callback(new Error('格式不正确'));
        } else {
          callback();
        }

      } else {
        callback();
      }
    };
    return {
      // 全局loading
      mloading: false,
      activeNames: ['1', '2', '3', '4', '5', '6'], // el-collapse 默认展开列表
      verticalTabData: [ // 左侧纵向导航数据
        {
          label: '项目单位信息',
          target: 'projunitInfo'
        }, {
          label: '项目基本信息',
          target: 'projbaseInfo'
        }, {
          label: '代办信息',
          target: 'commissionagentinfo'
        }, {
          label: '委托代办协议',
          target: 'commissionagentagreement'
        }
      ],
      activeTab: 0, // 纵向导航active状态index

      projInfoForm: {
        localCode: '', //项目代码
        projName: '', //项目名称
        investSum: '', //总投资
        xmYdmj: '', //用地面积
        investType: '', //投资类型
        landSource: '', //土地来源
        projType: '', //项目类型
        projectDept: '', //立项部门
        financialSource: '', //资金来源
        projLevel: '', //重点项目
        projNature: '', //建设性质
        projCategory: '', //工程分类
        foreignApproveNum: '', //备案文号
        buildAreaSum: '', //建筑面积
        district: '', //行政区划
        projAddr: '', //项目地址
        scaleContent: '', //建设内容  
        projInfoId: '', //项目id 注意：有项目id是编辑原来项目，没有时是分成子项目时
        foreignBuildingArea: '',
        projectAddress: '',
        isAreaEstimate: '0',
        areaDetailCode: '', //建设地点详情
        currentProgress: '', //项目当前进度
        agentStageState: [], //具体委托阶段

        // 项目单位信息
        applicant: '', //单位名称
        applicantDetailSite: '', //单位地址
        email: '', // 电子邮箱
        unitNature: '', //单位性质
        leaderName: '', //项目负责人
        leaderMobilePhone: '', //联系方式
        leaderDuty: '', //负责人职务
        operatorName: '', //项目经办人
        operatorMobilePhone: '', //经办人联系方式
        operatorDuty: '' //经办人职务
      },
      rules: {
        projName: [{
          required: true,
          message: '请输入项目名称',
          trigger: 'blur'
        }, ],
        localCode: [{
          required: true,
          message: '请输入项目代码',
          trigger: 'blur'
        }, ],
        // gcbm: [{
        //   required: true,
        //   message: '请输入工程代码',
        //   trigger: 'blur'
        // }, ],
        regionalism: [{
          required: true,
          message: '请选择行政区划',
          trigger: 'change'
        }, ],
        projectAddress: [{
          required: true,
          message: '请选择建设地点',
          trigger: 'change'
        }, ],
        financialSource: [{
          required: true,
          message: '请选择资金来源',
          trigger: 'change'
        }, ],
        investType: [{
          required: true,
          message: '请选择申请人类型码',
          trigger: 'change'
        }, ],
        landSource: [{
          required: true,
          message: '请选择土地来源',
          trigger: 'change'
        }, ],
        projType: [{
          required: true,
          message: '请选择立项类型',
          trigger: 'change'
        }, ],
        themeId: [{
          required: true,
          message: '请选择项目类型',
          trigger: 'change'
        }, ],
        projNature: [{
          required: true,
          message: '请选择建设性质',
          trigger: 'change'
        }, ],
        projCategory: [{
          required: true,
          message: '请选择工程分类',
          trigger: 'change'
        }, ],
        nstartTime: [{
          required: true,
          message: '请选择拟开工时间',
          trigger: 'blur'
        }, ],
        endTime: [{
          required: true,
          message: '请选择拟建成时间',
          trigger: 'change'
        }, ],
        investSum: [{
          required: true,
          message: '请输入总投资',
          trigger: 'blur'
        }, {
          required: true,
          validator: checkNumber,
          trigger: 'blur'
        }],
        foreignBuildingArea: [{
          required: true,
          message: '请输入总建筑面积',
          trigger: 'blur'
        }, {
          required: true,
          validator: checkNumber,
          trigger: 'blur'
        }],
        xmYdmj: [{
          required: true,
          message: '请输入用地面积',
          trigger: 'blur'
        }, {
          required: true,
          validator: checkNumber,
          trigger: 'blur'
        }],
        gbCodeYear: [{
          required: true,
          message: '请输入项目所属年份',
          trigger: 'blur'
        }, {
          required: true,
          validator: checkNumber,
          trigger: 'blur'
        }],
        foreignRemark: [{
          required: true,
          message: '请输入建设规模及内容',
          trigger: 'blur'
        }, ],
        projAddr: [{
          require: true,
          validator: checkNotAllNumber,
          trigger: 'blur'
        }],
        projLevel: [{
          required: true,
          message: '请选择项目重点级别',
          trigger: 'blur'
        }],
        compareTime: [{
          required: true,
          message: '开工时间必须小于建成时间',
          trigger: ['change', 'blur']
        }, ],
      },

      // 页面的下拉选项
      proJAllOptions: {
        XM_PROJECT_STEP: [], //项目类型
        XM_NATURE: [], //建设性质
        XM_PROJECT_LEVEL: [], //项目级别
        XM_PROJECT_STEP: [], //立项类型
        XM_TZLX: [], // 投资类型
        XM_ZJLY: [], // 资金来源
        XM_GBHY: [], // 行业类别
        XM_TDLY: [], // 土地来源
        XM_NATURE: [], // 建设性质
        XM_GCFL: [] // 工程分类
      },
      // 行政区划下拉列表
      districtList: [], 
    }
  },
  mounted: function () {
    var _that = this;
    window.addEventListener('scroll', _that.handleScroll);
    window.addEventListener('resize', throttle(function (ev) {
      _that.navLeft()
    }, 100, 600));
    _that.navLeft();

    this.fetchDicContents();
  },
  created: function(){
    // this.getRegionListData();
  },
  methods: {
    // 页面resize
    navLeft: function () {
      var _windW = $(window).width();
      // console.log(_windW)
      setTimeout(function () {
        if (_windW < 1240) {
          $('.right-handel-pandel').addClass('box-shadow-pandel')
        } else {
          $('.right-handel-pandel').removeClass('box-shadow-pandel')
        }
      }, 100)
    },
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
    // 页面导航操作方法
    targetCollapse: function (target, ind) { // 纵向导航点击事件
      this.activeTab = ind;
      $(document).scrollTop($('#' + target).offset().top)
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


    // 获取待选项
    fetchDicContents: function () {
      var ts = this;
      ts.mloading = true;
      request('', {
        url: ctx + 'aea/proj/info/getAllDicContent',
        type: 'get',
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.proJAllOptions = res.content;
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.mloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },

    // 获取所有的行政区划下拉数据
    getRegionListData: function () {
      var ts = this;
      request('', {
        url: ctx + 'rest/org/region/list',
        type: 'get',
      }, function (result) {
        if (result.success) {
          ts.districtList = result.content;
        }
      }, function (msg) {
        alertMsg('', '网络加载失败！', '关闭', 'error', true);
      })
    },
  },
  filters: {
    formatDate: function (val, mat) {
      if (!val) return '暂无'
      var _date = new Date(val);
      return formatDate(_date, mat)
    },
  },
});