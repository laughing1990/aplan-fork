/*
 * @Author:
 * @Date:
 * @Last Modified by:
 * @Last Modified time: $ $
 */
var vm = new Vue({
  el: '#approve',
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
    var checkPhoneNum = function (rule, value, callback) {
      if (value === '' || value === undefined) {
        callback(new Error('该输入项为必输项！'));
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
      orgUserList: [],
      isDisable: true,
      showSaveBtn: true,
      
      //证照
      activeNames: ['1', '2', '3', '4', '5'], // el-collapse 默认展开列表
      verticalTabData: [ // 左侧纵向导航数据
        {
          label: '领件人信息',
          target: 'receiver'
        }
      ],
      curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      loading: true,
      ctx: ctx,
      fileList: [],
      activeTab: 0,  // 纵向导航active状态index
      showVerLen: 1, // 显示左侧纵向导航栏的长度
      //===
      specialForm: {
        specialId: '',
        iteminstCode: '1',
        iteminstName: '',
        specialDueWay: '',
        specialDueTime: '',
        customer: '',
        linkAddr: '',
        linkPhone: '',
        linkman: '',
        specialType: '',
        specialDueDays: '',
        specialReason: '',
        specialMemo: '',
        approveResult: '1',
        approveUserName: '',
        approveTime: '',
        approveOpinion: '',
        chargeOrgName: '',
        chargeOrgId: '',
        windowUserName: '',
        windowUserId: '',
        windowPhone: '',
        opsUserName: '',
        opsUserId: '',
        opsTime: '',
        printUserName: '',
        printTime: '',
        isFlowTrigger: ''
      },
      specialTypeList: [],
      specialFormRules: {//证照实例校验
        specialDueWay: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        specialDueTime: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        specialType: [
          {required: true, validator: checkMissValue, trigger: 'blur,change'},
        ],
        specialDueDays: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        specialReason: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ]/*,
        specialMemo: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ]*/,
        approveResult: [
          {required: true, validator: checkMissValue, trigger: 'change'},
        ],
        approveUserName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        approveTime: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        approveOpinion: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        chargeOrgName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        windowUserName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        windowPhone: [
          {required: true, validator: checkPhoneNum, trigger: 'blur'},
        ],
        opsUserName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        opsTime: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
      },
    }
  },
  mounted: function () {
    var _that = this;
    _that.initBasicInfo();
    _that.getSpecialTypeList();
    _that.getOrgUserList();
    
    
  },
  watch: {
    searchProjfilterText: function (val) {
      this.$refs.projTree.filter(val);
    },
    searchOrgfilterText: function (val) {
      this.$refs.orgTree.filter(val);
    },
  },
  methods: {
    getOrgUserList: function () {
      var _this = this;
      request('', {
        url: ctx + 'rest/specialProcedure/getOrgUserList',
        type: 'get',
        data: {'iteminstId': iteminstId}
      }, function (result) {
        if (result.success) {
          _this.orgUserList = result.content;
          
        }
        
      }, function (msg) {
        /*_this.$message({
            message: '服务请求失败',
            type: 'error'
        });*/
        alertMsg('', "服务请求失败！", '关闭', 'error', true);
      });
    },
    /** 填充特殊程序基本信息*/
    initBasicInfo: function () {
      var _this = this;
      _this.loading = true;
      request('', {
        url: ctx + 'rest/specialProcedure/getBasicInfo',
        type: 'get',
        data: {'applyinstId': applyinstId, 'iteminstId': iteminstId}
      }, function (result) {
        _this.loading = false;
        if (result.success) {
          _this.specialForm = result.content;
          
          // if ("0" == isApprover) {//暂时无法判断是否窗口人员
          _this.specialForm.windowUserName = result.content.currentUserName;
          _this.specialForm.windowUserId = result.content.currentUserId;
          _this.specialForm.windowPhone = result.content.currentUserPhone;
          
          _this.specialForm.printUserName = result.content.currentUserName;
          _this.specialForm.printUserId = result.content.currentUserId;
          // }
          
          _this.specialForm.opsUserName = result.content.currentUserName;
          _this.specialForm.opsUserId = result.content.currentUserId;
          var _approveResult = _this.specialForm.approveResult == null ? '1' : _this.specialForm.approveResult;
          _this.specialForm.approveResult = _approveResult;
          if (_this.specialForm.specialId == '' || _this.specialForm.specialId == null) {
            _this.isDisable = false;
          }
          _this.specialForm.opsTime = _this.todayString();//new Date();
        } else {
          alertMsg('', "服务请求失败！", '关闭', 'error', true);
        }
        
      }, function (msg) {
        _this.loading = false;
        /*_this.$message({
            message: '服务请求失败',
            type: 'error'
        });*/
        alertMsg('', "服务请求失败！", '关闭', 'error', true);
      });
    },
    specialDueTimeChange: function (date) {
      var _this = this;
      var today = new Date();
      var selectDate = new Date(date);
      var days = selectDate.getTime() - today.getTime();
      var day = "";
      if (days < 0) {
        _this.specialForm.specialDueTime = _this.todayString();
        day = "0";
      } else {
        day = parseInt(days / (1000 * 60 * 60 * 24)) + 1 + '';
      }
      _this.specialForm.specialDueDays = day;
      console.info(day)
    },
    currentSel: function (uid) {
      var _this = this;
      for (var i = 0, len = _this.orgUserList.length; i < len; i++) {
        if (uid == _this.orgUserList[i].userId) {
          _this.specialForm.approveUserName = _this.orgUserList[i].userName;
          break;
        }
      }
    },
    /** 获取特殊程序类型list**/
    getSpecialTypeList: function () {
      var _this = this;
      request('', {
        url: ctx + 'rest/specialProcedure/getDicItemByType',
        type: 'get',
        data: {'codeType': "SPECIAL_TYPE"}
      }, function (result) {
        if (result.success) {
          _this.specialTypeList = result.content;
          
        }
        
      }, function (msg) {
        /*_this.$message({
            message: '服务请求失败',
            type: 'error'
        });*/
        alertMsg('', "服务请求失败！", '关闭', 'error', true);
      });
    },
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
    //保存特殊程序
    saveSpecial: function () {
      var _this = this;
      _this.specialForm.taskId = taskinstId;
      _this.specialForm.isFlowTrigger = '1';
      _this.specialForm.applyinstId = applyinstId;
      _this.specialForm.iteminstId = iteminstId;
      _this.specialForm.appinstId = processInstanceId;
      
      console.info(_this.specialForm);
      _this.$refs['specialInfoForm'].validate(function (valid) {
        if (valid) {
          _this.loading = true;
          request('', {
            url: ctx + "rest/specialProcedure/saveSpecial",
            type: 'post',
            data: _this.specialForm
          }, function (result) {
            _this.loading = false;
            if (result.success) {
              parent.vm.$message.success('保存成功');
              _this.close();
            } else {
              parent.vm.$message.error(result.message);
            }
          }, function (msg) {
            _this.loading = false;
            parent.vm.$message.error('保存失败！');
          });
        } else {
          parent.vm.$message.error('请输入完整信息！');
          return false;
        }
      });
    },
    //打印通知书
    doPrint: function () {
    
    },
    //结束特殊程序
    stopSpecial: function () {
      var _this = this;
      var obj = _this.specialForm;
      _this.loading = true;
      request('', {
        url: ctx + 'rest/specialProcedure/stopSpecial',
        type: 'post',
        data: obj
      }, function (result) {
        _this.loading = false;
        if (result.success) {
          parent.vm.$message.success('特殊程序已停止');
          _this.close();
        } else {
          parent.vm.$message.error(result.message);
        }
        
      }, function (msg) {
        _this.loading = false;
        /*_this.$message({
            message: '服务请求失败',
            type: 'error'
        });*/
        alertMsg('', "服务请求失败！", '关闭', 'error', true);
      });
    },
    //关闭当前窗口
    close: function () {
      parent.delayRefreshWindow();
      parent.vm.specialDiaVisible = false;
      parent.vm.specialSrc = '';
    },
    dateFormat: function (dateStr) {
      var t = new Date(dateStr);//row 表示一行数据, updateTime 表示要格式化的字段名称
      var month = t.getMonth() + 1 + "";
      var day = t.getDate() + "";
      var year = t.getFullYear() + "";
      var date = year + '-' + month.length == 1 ? ('0' + month) : month + '-' + day.length == 1 ? ('0' + day) : day;
      return date;
    },
    // 重置表单
    resetForm: function (formName) {
      this.$refs[formName].resetFields();
      this.selectMat = '';
    },
    // 刷新页面
    reloadPage: function () {
      if (this.submitCommentsType == 0 || this.submitCommentsType == 3) {
        window.location.reload()
      }
    },
    todayString: function () {
      var t = new Date();//row 表示一行数据, updateTime 表示要格式化的字段名称
      var month = t.getMonth() + 1 + "";
      month = month.length == 1 ? '0' + month : month;
      var day = t.getDate() + "";
      day = day.length == 1 ? '0' + day : day;
      var year = t.getFullYear() + '';
      var date = year + '-' + month + '-' + day;
      return date;
    }
  },
});

