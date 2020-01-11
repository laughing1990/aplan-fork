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

// 简单解密
function uncompile(code) {
  code = unescape(code);
  var c = String.fromCharCode(code.charCodeAt(0) - code.length);
  for (var i = 1; i < code.length; i++) {
    c += String.fromCharCode(code.charCodeAt(i) - c.charCodeAt(i - 1));
  }
  return c;
};

// 侧边栏导航数据
var NAVLEFTDATA = [ 
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
    label: '代办委托协议',
    target: 'commissionagentagreement'
  },{
    label: '代办办结单',
    target: 'endagentagreement'
  },{
    label: '代办委托终止单',
    target: 'stopagentagreement'
  }
];

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
      // 时间选择器配置
      pickerOptions: {
        disabledDate: function (time) {
          return time.getTime() < Date.now() - 8.64e7;
        },
      },
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
          label: '代办委托协议',
          target: 'commissionagentagreement'
        },{
          label: '代办办结单',
          target: 'endagentagreement'
        },{
          label: '代办委托终止单',
          target: 'stopagentagreement'
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
        operatorDuty: '', //经办人职务

        // 代办信息
        agreementCode: '', //协议编号
        agentStageName: '', //具体委托事项
        unitName: '', //委托单位
        applyUserName: '', //申请人姓名
        applyUserPhone: '', //申请人联系方式
        windowName: '', //受托单位
        agentUserId: '', //代办人id
        agentUserName: '', //代办人姓名
        agentUserMobile: '', //代办人联系方式
        agreementSignTime: '', //协议签订时间
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
          message: '请选择投资类型',
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

        agreementCode: [{
          required: true,
          message: '请输入协议编号',
          trigger: 'blur'
        }],
        agentUserId: [{
          required: true,
          message: '请选择代办人',
          trigger: 'change'
        }],
        agentUserMobile: [{
          required: true,
          message: '请输入代办人员联系方式',
          trigger: 'blur'
        }],
        agreementSignTime: [{
          required: true,
          message: '请选择协议签订时间',
          trigger: 'change'
        }],
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
      // 代办信息-代办人员下拉待选数据
      agencyPeopleOptions: [],

      // 页面获取的总的数据
      agencyDetailApiData: {},
      // 是否展示签订协议书dialog
      prePdfVisible: false,
      // 签订协议书的pdf的src
      pdfSrc: '',

      // 从列表过来是签订还是查看
      formListIsDo: true,
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
  created: function () {
    this.formListIsDoFn();
    this.fetchAgencyPeopleOptions();
    this.fetchPagerInfo();
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

    // 从列表跳转-是否是为查看或者签订
    formListIsDoFn: function () {
      var type = this.getSerachParamsForUrl('type');
      type = uncompile(type);
      type == 'do' ?
        this.formListIsDo = true :
        this.formListIsDo = false;
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

    // 获取代办信息模块中-代办人员姓名下来待选项
    fetchAgencyPeopleOptions: function () {
      var ts = this;
      ts.mloading = true;
      request('', {
        url: ctx + 'aea/proj/apply/agent/getCurrAgencyWinUserList',
        type: 'get',
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.agencyPeopleOptions = res.content;
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.mloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },

    // 获取页面中回显的数据
    fetchPagerInfo: function () {
      var ts = this,
        getData = {};
      getData.applyAgentId = ts.getSerachParamsForUrl('applyAgentId');
      ts.mloading = true;
      request('', {
        url: ctx + 'aea/proj/apply/agent/getAgencyDetail',
        type: 'get',
        data: getData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.handelPagerInfo(res.content);
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.mloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },
    // 处理页面回显数据
    handelPagerInfo: function (data) {
      var form = {};
      form = $.extend({}, data.aeaProjApplyAgent, data.aeaProjInfo, data.aeaUnitProjLinkmanVo);
      // 操作按钮与委托代办协议的隐藏显示处理-处理侧边栏
      this.agencyDetailApiData = form;
      var state = +this.agencyDetailApiData.agentApplyState;
      if (state < 3) {
        this.verticalTabData.splice(3,1);
      } else {
        if(state == 5){
          this.verticalTabData.splice(4,1);
        }else if(state == 7){
          this.verticalTabData.splice(5,1);
        }else{
          this.verticalTabData = JSON.parse(JSON.stringify(NAVLEFTDATA));
        }  
      }
      for (var k in this.projInfoForm) {
        if (form[k]) {
          this.projInfoForm[k] = form[k];
        }
      }
      this.projInfoForm.agentUserId = this.projInfoForm.agentUserId == '' ?
        form.currentUserId :
        form.agentUserId;
      this.projInfoForm.agentUserName = this.projInfoForm.agentUserName == '' ?
        form.currentUserName :
        form.agentUserName;
      this.projInfoForm.agentUserMobile = this.projInfoForm.agentUserMobile == '' ?
        form.currentUserMobile :
        form.agentUserMobile;
    },
    // 选择代办人员
    agencyPeopleSelect: function (obj) {
      // console.log(obj)
      this.projInfoForm.agentUserName = obj.userName;
      this.projInfoForm.agentUserMobile = obj.userMobile;
      // console.log(this.projInfoForm.agentUserId)
    },
    // 校验输入的协议编号是不是唯一
    checkAgreementCode: function () {
      var ts = this,
        checkData = {};
      checkData.agreementCode = ts.projInfoForm.agreementCode;
      if (!checkData.agreementCode) return;
      ts.mloading = true;
      request('', {
        url: ctx + 'aea/proj/apply/agent/checkAgreementCodeUnique',
        type: 'get',
        data: checkData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {

        } else {
          ts.projInfoForm.agreementCode = "";
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.mloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },
    // 保存代办信息
    saveAgencyInfo: function () {
      var ts = this;
      this.$refs['projInfoForm'].validate(function (valid) {
        if (valid) {
          ts.checkBtnHandel(1, doFn)
        } else {
          ts.apiMessage('请完善代办信息！', 'error')
          return false;
        }
      });

      function doFn(){
        var saveData = {
          agreementCode: ts.projInfoForm.agreementCode,
          agreementSignTime: ts.projInfoForm.agreementSignTime,
          agentUserId: ts.projInfoForm.agentUserId,
          agentUserName: ts.projInfoForm.agentUserName,
          agentUserMobile: ts.projInfoForm.agentUserMobile,
          applyAgentId: ts.getSerachParamsForUrl('applyAgentId')
        };
        // console.log(this.projInfoForm)
        // return
        ts.mloading = true;
        request('', {
          url: ctx + 'aea/proj/apply/agent/saveAeaProjApplyAgent',
          type: 'post',
          data: saveData
        }, function (res) {
          ts.mloading = false;
          if (res.success) {
            ts.apiMessage('保存成功！', 'success')
          } else {
            return ts.apiMessage(res.message, 'error')
          }
        }, function () {
          ts.mloading = false;
          return ts.apiMessage('网络错误！', 'error')
        });
      }
    },

    // 签订协议操作
    signAgreementFn: function () {
      // var fileUrl = ctx + 'aea/proj/apply/agent/getAgencyAgreement?applyAgentId=' + this.getSerachParamsForUrl('applyAgentId');
      // this.pdfSrc = ctx + 'preview/pdfjs/web/viewer.html?file=' + encodeURIComponent(fileUrl);
      // this.prePdfVisible = true;
      // return
      var ts = this;

      function doFn() {
        var fileUrl = ctx + 'aea/proj/apply/agent/getAgencyAgreement?applyAgentId=' + ts.getSerachParamsForUrl('applyAgentId');
        ts.pdfSrc = ctx + 'preview/pdfjs/web/viewer.html?file=' + encodeURIComponent(fileUrl);
        ts.prePdfVisible = true;
      }
      ts.checkBtnHandel(2, doFn)
    },

    // 校验是否可以操作代办信息保存与签订协议操作
    checkBtnHandel: function (type, cb) {
      var ts = this,
        checkData = {};
      checkData.applyAgentId = ts.getSerachParamsForUrl('applyAgentId');
      checkData.operateType = type;
      if (!checkData.applyAgentId) return;
      ts.mloading = true;
      request('', {
        url: ctx + 'aea/proj/apply/agent/checkOpreatePermit',
        type: 'post',
        data: checkData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          cb && cb();
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.mloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },

    // 关闭签订协议书dialog
    closePdfDialog: function () {
      this.pdfSrc = '';
    },
    // 协议下载
    agreementDown: function () {
      var fileUrl = ctx + 'aea/proj/apply/agent/getAgencyAgreement?applyAgentId=' + this.getSerachParamsForUrl('applyAgentId');
      window.open(fileUrl);
    },
    // 拒绝代办操作
    refuseToAccept: function () {
      var ts = this;
      confirmMsg('', '请确认拒绝代办该项目的代办申请?', function () {
        var saveData = {
          agentApplyState: 6,
          applyAgentId: ts.getSerachParamsForUrl('applyAgentId')
        };
        // console.log(saveData)
        // return
        ts.mloading = true;
        request('', {
          url: ctx + 'aea/proj/apply/agent/saveAeaProjApplyAgent',
          type: 'post',
          data: saveData
        }, function (res) {
          ts.mloading = false;
          if (res.success) {
            ts.apiMessage('保存成功！', 'success')
          } else {
            return ts.apiMessage(res.message, 'error')
          }
        }, function () {
          ts.mloading = false;
          return ts.apiMessage('网络错误！', 'error')
        });
      })

    },
    // 委托代办协议模块的预览
    previewAgreement: function(id){
      // console.log(this.agencyDetailApiData.currentUserName)
      var _url = ctx + 'previewPdf/view?detailId=' + id;
      window.open(_url, '_blank');
    },
    // 委托代办协议模块的预览
    downAgreement: function(id){
      var _url = ctx + 'previewPdf/downLoadPdf?detailId=' + id;
      window.open(_url);
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