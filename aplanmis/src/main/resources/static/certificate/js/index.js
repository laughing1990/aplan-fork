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
      if (value === '' || value === undefined || value.trim() === '') {
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
      filePreviewCount: 0,
      receiveModeShow: true,
      receiveMode_dig: '',
      isConsigner_dig: '',
      addresseeName: 'gggg',
      addresseePhone: 'gggg',
      addresseeIdcard: 'gggg',
      addresseeP: 'gggg',
      addresseeAddr: 'gggg',
      senderName: 'gggg',
      senderPhone: 'gggg',
      senderAddr: 'gggg',

      tableData3: [],
      needSendItem: [],
      editCertFormType: '',
      editCertForm: {
        /*certinstId: '',
        certId: '',
        certinstCode: '',
        certinstName: '',
        termStart: '',
        termEnd: '',
        issueDate: '',
        certOwner: '',
        unitInfoId: ''*/
      },
      certTableTabs: [],
      tabActive: '',
      showUpdateBtn: true,
      editCertModalShow: false,
      editCertModalTitle: '证件材料详情',
      editItemSmsInfoModalTitle: '取件详情',
      showSaveBtn: true,

      sendInfoForm: {
        receiveMode: "1",
        smsType: '1',
        senderName: '',
        senderPhone: '',
        senderProvince: '',
        senderCity: '',
        senderCounty: '',
        senderAddr: '',

        orderId: '',//订单号
        expressNum: '',//运单号
        addresseeName: '',
        addresseePhone: '',
        addresseeIdcard: '',
        addresseeProvince: '',
        addresseeCity: '',
        addresseeCounty: '',
        addresseeAddr: '',
        consignerAttId: '',
        isConsigner: '0'

      },
      consignerForm: {
        addresseeName: '',
        addresseePhone: '',
        addresseeIdcard: '',
        addresseeProvince: '',
        addresseeCity: '',
        addresseeCounty: '',
        addresseeAddr: ''
      },

      ItemSmsInfoForm: {
        receiveMode: '1',
        smsType: '1',
        senderName: '',
        senderPhone: '',
        senderProvince: '',
        senderCity: '',
        senderCounty: '',
        senderAddr: '',

        orderId: '',//订单号
        expressNum: '',//运单号
        addresseeName: '',
        addresseePhone: '',
        addresseeIdcard: '',
        addresseeProvince: '',
        addresseeCity: '',
        addresseeCounty: '',
        addresseeAddr: '',
        consignerAttId: '',
        isConsigner: '0'

      },
      editItemSmsInfoModalShow: false,
      isConsigner: '0',
      multipleSelection: [],
      hadSelectionTmp: [],
      applyinstId: '',
      consignerInfoRules: {
        addresseePhone: [
          {required: true, validator: checkPhoneNum, trigger: 'blur'},

        ],
        addresseeAddr: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseePhone: [
          {required: true, validator: checkPhoneNum, trigger: 'blur'},
        ],
        addresseeIdcard: [
          {required: true, validator: checkMissValue, trigger: 'blur'}
        ],
        addresseeProvince: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeCity: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeCounty: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ]
      },
      itemSmsInfoFormRules: {
        senderName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        senderPhone: [
          {required: true, validator: checkPhoneNum, trigger: 'blur'},
        ],
        senderProvince: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        senderCity: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        senderCounty: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        senderAddr: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],

        addresseeName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseePhone: [
          {required: true, validator: checkPhoneNum, trigger: 'blur'},
        ],

        orderId: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        expressNum: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeIdcard: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
          {
            pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
            message: '证件号码格式有误！',
            trigger: 'blur'
          }
        ],
        addresseeProvince: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeCity: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeCounty: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeAddr: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],


      },

      sendSmsInfoRules: { // 新增编辑校验
        senderName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        senderPhone: [
          {required: true, validator: checkPhoneNum, trigger: 'blur'},
        ],
        senderProvince: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        senderCity: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        senderCounty: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        senderAddr: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseePhone: [
          {required: true, validator: checkPhoneNum, trigger: 'blur'},
        ],
        orderId: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        expressNum: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeIdcard: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
          {
            pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
            message: '证件号码格式有误！',
            trigger: 'blur'
          }
        ],
        addresseeProvince: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeCity: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeCounty: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        addresseeAddr: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ]

      },
      certFormRules: {//证照实例校验
        certId: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        certinstCode: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        certinstName: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        issueOrgId: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        termStart: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        termEnd: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        issueDate: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
        certOwner: [
          {required: true, validator: checkMissValue, trigger: 'blur'},
        ],
      },
      uploadTableData: [],
      fileSelectionList: [], // 所选电子件
      selMatRowData: {}, // 所选择的材料信息
      fileWinData: [], // 上传窗口上传参数列表
      loadingFileWin: false, // 窗口文件上传loading

      //证照
      uploadCertData: [],
      certSelectionList: [],
      certWinData: [],
      currentIteminstId: '',//当前iteminstId
      activeNames: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'], // el-collapse 默认展开列表
      verticalTabData: [ // 左侧纵向导航数据
        {
          label: '事项信息列表',
          target: 'baseInfo'
        }, {
          label: '证件领取方式',
          target: 'receiveMode'
        }, {
          label: '领件人信息',
          target: 'receiver'
        }, {
          label: '出件人信息',
          target: 'sender'
        }
      ],
      curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      loading: false,
      ctx: ctx,
      fileList: [],
      activeTab: 0,  // 纵向导航active状态index
      showVerLen: 5, // 显示左侧纵向导航栏的长度
      certList: [],
      orgList: [],
      unitInfoList: [],
      provinceList: [], // 所有的省份信息
      cityList: [], // 所有市区信息
      countyList: [], // 所有地区信息
      provinceList_jjr: [], // 所有的省份信息
      cityList_jjr: [], // 所有市区信息
      countyList_jjr: [], // 所有地区信息
      isSmsSend: '0',//是否已经出件了
      isEMSPage: false,
    }
  },
  mounted: function () {
    var vm = this;
    vm.applyinstId = applyinstId;
    vm.windowUserName = windowUserName;
    vm.isEMSPage = (__STATIC.getUrlParam('isEMS')=='true');
    if (vm.isEMSPage) {
      vm.EMSconfig();
    }
    vm.searchSmsInfo();
    vm.getFileListWin('', vm.selMatRowData);
    // vm.getCertBasicInfoList();
    vm.queryProvince();
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
    EMSconfig: function(){
      var vm = this;
      vm.verticalTabData = [ // 左侧纵向导航数据
        {
          label: '事项信息列表',
          target: 'baseInfo'
        }, {
          label: '收件人信息',
          target: 'receiver'
        }, {
          label: '寄件人信息',
          target: 'sender'
        }
      ];
    },
    //一次出件
    doOnceSend: function () {
      var vm = this;
      if (vm.needSendItem.length == '0') {
        return;
      }
      if (vm.multipleSelection.length > 0 && vm.hadSelectionTmp.length > 0) {
        vm.$message({
          message: '已有个别事项出件，不能一次全选，请手动勾选！！',
          type: 'error'
        });
      } else {
        // vm.toggleSelection(vm.tableData3);
        $.each(vm.tableData3, function (idx, obj) {
          if (obj.hasOutCertinst == '1' && (obj.iteminstState == '11' || obj.iteminstState == '12')) {
            vm.$refs.multipleTable.toggleRowSelection(obj);
          }

        });
      }
    },
    // 获取省份信息
    queryProvince: function () {
      var vm = this;
      request('', {
        url: ctx + 'rest/apply/province',
        type: 'get'
      }, function (data) {
        vm.provinceList = data.content;
        vm.provinceList_jjr = data.content;
        vm.queryCityData(0, 'jjr');
        vm.queryCityData(0, 'lz');
      })
    },
    // 选择省份
    queryCityData: function (index, type) {

      if ('lz' == type) {
        this.cityList = this.provinceList[index].cityList;
      } else {
        this.cityList_jjr = this.provinceList_jjr[index].cityList;
      }

      this.queryAreaData(0, type);
      // this.sendInfoForm.addresseeProvince = this.provinceList[index].code;
    },
    // 选择市地区
    queryAreaData: function (index, type) {
      if (type == 'lz') {
        this.countyList = this.cityList[index].areaList;
      } else {
        this.countyList_jjr = this.cityList_jjr[index].areaList;
      }
      // this.queryCountyData(this.countyList[0].code);
    },
    // 选择地区
    queryCountyData: function (code) {
      // this.sendInfoForm.addresseeCounty=code;
    },
    //给已出件的添加底色
    tableRowClass: function (obj) {
      if (obj.row.isSmsSend == '1' || obj.row.hasOutCertinst == "0" || obj.row.iteminstState == '13') {
        return 'warning-row';
      }
      return '';
    },
    /** 获取制证所用基本信息**/
    /*getCertBasicInfoList: function () {
        var vm = this;
        request('', {
            url: ctx + 'rest/certificate/getBasicInfo',
            type: 'get'
        }, function (result) {
            if (result.content) {
                vm.certList = result.content.certList;
                vm.orgList = result.content.orgList;
                vm.unitInfoList = result.content.orgList;
            }

        }, function (msg) {
            vm.$message({
                message: '服务请求失败',
                type: 'error'
            });
        });
    },*/
    //状态转换
    statuFormatter: function (row, col) {
      if (row.isSmsSend == '1') {
        return "已出件";
      } else {
        if (row.hasOutCertinst == '1' && (row.iteminstState == '11' || row.iteminstState == '12')) {
          return "未出件"
        }
        return "无输出材料";
      }
    },
    itemStatuFormatter: function (row, col) {
      // 2已撤件，3部门受理 ，4不受理，5不予受理，6补正（开始），7补正（结束），8部门开始办理，9特别程序（开始），10特别程序（结束），11办结（通过），12办结（容缺通过），13办结（不通 过），14撤回，15撤销
      if (row.iteminstState == '13') {
        return "办结不通过";
      } else if (row.iteminstState == '12' || row.iteminstState == '11') {
        return "办结";
      } else if (row.iteminstState == '4') {
        return "不受理";
      } else if (row.iteminstState == '5') {
        return "不予受理";
      } else if (row.iteminstState == '8') {
        return "部门办理";
      } else if (row.iteminstState == '14') {
        return "撤回";
      } else if (row.iteminstState == '15') {
        return "15撤销";
      } else {
        return "-";
      }
    },
    targetCollapse: function (target, ind) { // 纵向导航点击事件
      this.activeTab = ind;
      // $(document).scrollTop($('#' + target).offset().top)
    },
    handleScroll: function () { // 屏幕滚动纵向导航相应获取焦点
      var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
      var ele = $('.el-collapse-item');
      var eleLen = ele.length;
      var vm = this;
      for (var i = 0; i < eleLen; i++) {
        if (scrollTop >= ($(ele[i]).offset().top - 60)) {
          vm.activeTab = i
        }
      }
    },
    //更新证照信息
    updateItemCert: function () {
      var vm = this;
      if (vm.editCertForm.issueDate == '' || vm.editCertForm.issueDate == null) {
        vm.$message({
          message: '请选择日期',
          type: 'error'
        });
      }
      var detailId = '';
      if (vm.uploadCertData.length > 0) {
        $.each(vm.uploadCertData, function (idx, obj) {
          detailId += obj.fileId + ",";
        });
      } else {
        vm.$message({
          message: '请先上传证照！！',
          type: 'error'
        });
        return;
      }

      detailId = detailId.substring(0, detailId.length - 1);
      vm.$refs['editCertForm'].validate(function (valid) {
        if (valid) {
          var obj = vm.editCertForm;
          obj.attLinkId = detailId;
          obj.iteminstId = vm.currentIteminstId;
          obj.termStart = vm.dateFormat(obj.termStart);
          obj.termEnd = vm.dateFormat(obj.termEnd);
          obj.issueDate = vm.dateFormat(obj.issueDate);
          obj.bscAttDetails = [];
          obj.applyinstId = applyinstId;
          request('', {
            url: ctx + 'rest/certificate/updateCertInfo',
            type: 'post',
            data: obj
          }, function (result) {
            vm.editCertModalShow = false;


          }, function (msg) {
            vm.$message({
              message: '服务请求失败',
              type: 'error'
            });
          });
        }

      });
    },
    dateFormat: function (dateStr) {
      var t = new Date(dateStr);//row 表示一行数据, updateTime 表示要格式化的字段名称
      var month = t.getMonth() + 1 + "";
      var day = t.getDate() + "";
      var date = t.getFullYear() + '-' + month.length == 1 ? '0' + month : month + '-' + day.length == 1 ? '0' + day : day;
      return date;
    },
    //已出件的不给勾选
    checkboxInit2: function (row, idx) {
      var vm = this;
      if (row.isSmsSend == '1' || row.hasOutCertinst == "0" || row.iteminstState == '13') {
        return false;
      } else {
        return true;
      }
    },
    /**
     * 点击tab显示不同的证照实例详情
     */
    showCertPage: function (event) {
      var vm = this;
      var id = event.name;
      for (var i = 0, len = vm.certTableTabs.length; i < len; i++) {
        if (id == vm.certTableTabs[i].id) {
          vm.editCertForm = vm.certTableTabs[i].data;
          vm.editCertFormType = vm.certTableTabs[i].type;
          break;
        }
      }
      vm.getCertListWin(id, vm.editCertFormType);
    },
    /** 查看cert信息*/
    handleEdit: function (index, row) {
      var vm = this;
      vm.currentIteminstId = row.iteminstId;


      request('', {
        url: ctx + 'rest/certificate/out/materials/view',
        type: 'get',
        data: {'iteminstId': row.iteminstId, 'applyinstId': applyinstId}
      }, function (result) {
        if (result.content) {
          vm.certTableTabs = result.content;
          vm.tabActive = result.content[0].id;
          vm.editCertFormType = result.content[0].type;
          vm.editCertForm = result.content[0].data;
          vm.getCertListWin(vm.tabActive, vm.editCertFormType);
        }

      }, function (msg) {
        vm.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
      vm.editCertModalShow = true;

    },
    toggleSelection: function (rows) {
      if (rows) {
        //ES6 iE 不支持
        /*rows.forEach(row = > {
            this.$refs.multipleTable.toggleRowSelection(row);
    })*/
        rows.forEach(function (row, index) {
          this.$refs.multipleTable.toggleRowSelection(row);
        })
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    handleSelectionChange: function (val) {
      this.multipleSelection = val;
    },
    //过滤，只显示有输出的inoutinst实例的事项
    getShow: function (iteminsts) {
      /*var result =[];
      for(var i=0 ,len = iteminsts.length;i<len;i++){
          if(iteminsts[i].hasOutCertinst=="1"){
              result.push(iteminsts[i]);
          }
      }
      return result;*/
      return iteminsts;
    },
    //根据applyinstId获取寄件收件人及事项信息信息
    searchSmsInfo: function () {
      var vm = this;
      var _windowUserName = this.windowUserName;
      var _applyinstId = this.applyinstId;
      var tmp = [];
      vm.loading = true;
      var _url = ctx + 'rest/certificate/out/materials/register';
      if (vm.isEMSPage) {
        _url = ctx + 'rest/certificate/out/materials/mail/post';
      }
      request('', {
        url: _url,
        type: 'get',
        data: {'applyinstId': _applyinstId}
      }, function (result) {
        vm.loading = false;
        if (result.content) {
          vm.tableData3 = vm.getShow(result.content.certRegistrationItemVos);
          vm.sendInfoForm = result.content.smsInfo;
          // vm.sendInfoForm.senderPhone = '';
          // vm.sendInfoForm.senderName = _windowUserName;
          vm.sendInfoForm.smsType = '1';
          vm.sendInfoForm.isConsigner = '0';
          vm.$nextTick(function () {
            $.each(vm.tableData3, function (idx, obj) {
              if (obj.isSmsSend == '1') {
                vm.$refs.multipleTable.toggleRowSelection(obj);

                vm.setHadSendIndex();
              }
              if (obj.hasOutCertinst == '1') {
                vm.needSendItem.push(obj);
              }
            });

            /*if (vm.tableData3.length == vm.multipleSelection.length) {
                vm.showSaveBtn = false;
            }*/
            if (vm.needSendItem.length == vm.multipleSelection.length) {
              vm.showSaveBtn = false;
            }
            if (vm.needSendItem.length == 0) {
              vm.showSaveBtn = false;
            }
            vm.hadSelectionTmp = vm.multipleSelection;
            if (vm.sendInfoForm.receiveMode == "0") {
              vm.receiveModeShow = true;
            } else {
              vm.receiveModeShow = false;
            }


            $.each(this.provinceList, function (idx, obj) {
              if (obj.code == result.content.smsInfo.addresseeProvince) {
                var index = idx;
                vm.queryCityData(index, "lz");
                $.each(this.cityList, function (idx2, obj2) {
                  if (obj2.code == result.content.smsInfo.addresseeCity) {
                    var index2 = idx2;
                    vm.queryAreaData(index2, "lz");
                  }
                });
              }

              if (obj.code == result.content.smsInfo.senderProvince) {
                var index = idx;
                vm.queryCityData(index, "jjr");
                $.each(this.cityList, function (idx2, obj2) {
                  if (obj2.code == result.content.smsInfo.senderCity) {
                    var index2 = idx2;
                    vm.queryAreaData(index2, "jjr");
                  }
                });
              }
            })
          });
        }

      }, function (msg) {
        vm.loading = false;
        vm.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });


    },

    /**
     * 保存出证信息
     */
    saveSmsFormInfo: function () {
      var vm = this;
      //封装采集参数
      var param = {};
      var validateResult = true;

      vm.$refs['receInfo'].validate(function (valid) {
        if (!valid) {
          vm.$message({
            message: '请输入完整信息！',
            type: 'error'
          });
          validateResult = validateResult && false;
          return false;
        }
      });
      vm.$refs['consignerInfo'].validate(function (valid) {
        if (!valid) {
          vm.$message({
            message: '请输入完整信息！',
            type: 'error'
          });
          validateResult = validateResult && false;
          return false;
        }
      });
      if (vm.sendInfoForm.receiveMode == '1') {
        vm.$refs['sendInfo'].validate(function (valid) {
          if (!valid) {
            vm.$message({
              message: '请输入完整信息！',
              type: 'error'
            });
            validateResult = validateResult && false;
            return false;
          }
        });
      } else {

        vm.$refs['sendInfo_jjr'].validate(function (valid) {
          if (!valid) {
            vm.$message({
              message: '请输入完整信息！',
              type: 'error'
            });
            validateResult = validateResult && false;
            return false;
          }
        });
      }

      !vm.isEMSPage && vm.$refs['receInfo2'].validate(function (valid) {
        if (!valid) {
          vm.$message({
            message: '请输入完整信息！',
            type: 'error'
          });
          validateResult = validateResult && false;
          return false;
        }
      });


      //判断是否一次上传
      var _tmpSelection = [];
      if (vm.multipleSelection.length == 0) {
        this.$message({
          message: '请勾选要出证的事项！',
          type: 'error'
        });
        return;

      } else {
        $.each(vm.multipleSelection, function (idx, obj) {
          if (obj.isSmsSend != '1') {
            _tmpSelection.push(obj);
          }
        });
        if (_tmpSelection.length == vm.needSendItem.length) {
          param.isOnceSend = '1';
        } else {
          param.isOnceSend = '0';
        }
      }
      // debugger
      param.applyinstId = applyinstId;
      param.isSeriesApprove = vm.multipleSelection[0].isSeriesApprove;
      vm.sendInfoForm.windowUserName = windowUserName;
      vm.sendInfoForm.windowUserId = windowUserId;
      param.sendBean = vm.sendInfoForm;
      param.consignerForm = vm.consignerForm;


      if (vm.sendInfoForm.isConsigner == '1') {
        if (vm.uploadTableData.length == 0) {
          this.$message({
            message: '委托领取请上传委托书！',
            type: 'error'
          });
          return;
        }
        param.sendBean.isConsigner = '1';
      } else {
        param.sendBean.isConsigner = '0';
      }
      if (vm.uploadTableData.length > 0) {
        var fileIds = [];
        $.each(vm.uploadTableData, function (idx, obj) {
          fileIds.push(obj.fileId);
        });
        vm.sendInfoForm.consignerAttId = fileIds.join(",");
      }

      // param.iteminsts = vm.multipleSelection;
      param.certRegistrationItemVos = vm.multipleSelection;
      var _url = ctx + 'rest/certificate/out/materials/confirm';
      if (vm.isEMSPage) {
        _url = ctx + 'rest/certificate/out/materials/mail/order';
      }
      if (validateResult) {
        vm.loading = true;
        request('', {
          url: _url,
          type: 'post',
          data: JSON.stringify(param),
          ContentType: "application/json"
        }, function (result) {
          vm.loading = false;
          if (result.success) {
            var text = '取件成功';
            if (vm.isEMSPage) {
              text = '下单成功，快递单号是：' + result.content;
            }
            vm.$message({
              message: text,
              type: 'success'
            });
            vm.closeWindowTab();
          } else {
            vm.$message.error(result.message||'操作失败');
          }
        }, function (msg) {
          vm.loading = false;
          vm.$message({
            message: '服务请求失败',
            type: 'error'
          });
        });
      } else {

      }
      // return;

    },
    /**
     * 更新事项的出证登记历史
     * @param index
     * @param row
     */
    editSMSSend: function (index, row) {
      var vm = this;
      vm.loading = true;
      request('', {
        url: ctx + 'rest/certificate/out/materials/detail',
        type: 'get',
        data: {'iteminstId': row.iteminstId, 'applyinstId': applyinstId}
      }, function (result) {
        vm.loading = false;
        if (result.content) {
          vm.ItemSmsInfoForm = result.content;
          vm.showDifName(vm.ItemSmsInfoForm.isConsigner, vm.ItemSmsInfoForm.receiveMode)
          vm.receiveMode_dig = vm.ItemSmsInfoForm.receiveMode;
          vm.isConsigner_dig = vm.ItemSmsInfoForm.isConsigner
          var proviceCode = vm.ItemSmsInfoForm.addresseeProvince
          var cityCode = vm.ItemSmsInfoForm.addresseeCity;
          for (var i = 0, len = vm.provinceList.length; i < len; i++) {
            if (proviceCode == vm.provinceList[i].code) {
              var list = vm.provinceList[i].cityList;
              vm.cityList = list;
              for (var j = 0, len = list.length; j < len; j++) {
                if (cityCode == list[j].code) {
                  vm.countyList = list[j].areaList;
                  break;
                }
              }
              break;
            }
          }

        }

      }, function (msg) {
        vm.loading = false;
        vm.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });

      vm.editItemSmsInfoModalShow = true;
    },

    /**
     * 某事项出证登记历史弹框，根据不同状态显示不同名称
     * @param isConsigner
     * @param receiveMode
     */
    showDifName: function (isConsigner, receiveMode) {
      var vm = this;
      if (isConsigner == '0') {
        vm.addresseeName = "领证人姓名：";
        vm.addresseePhone = "领证人电话：";
        vm.addresseeIdcard = "领证人身份证：";
        vm.addresseeP = "领证人区域：";
        vm.addresseeAddr = "领证人详细地址：";

      } else {
        vm.addresseeName = "委托人姓名：";
        vm.addresseePhone = "委托人电话：";
        vm.addresseeIdcard = "委托人身份证：";
        vm.addresseeP = "委托人区域：";
        vm.addresseeAddr = "委托人详细地址：";
      }

      if (receiveMode == '0') {
        vm.senderName = "寄件人姓名：";
        vm.senderPhone = "寄件人电话：";
        vm.senderAddr = "寄件人详细地址：";
      } else {
        vm.senderName = "出件人姓名：";
        vm.senderPhone = "出件人电话：";
        vm.senderAddr = "出件人详细地址：";
      }
    },
    //更新单个事项出证详情
    updateSendItemInfo: function () {
      var vm = this;
      vm.$refs['ItemSmsInfoForm'].validate(function (valid) {
        if (!valid) {
          vm.$message({
            message: '请输入完整信息！',
            type: 'error'
          });
          return false;
        }
      });

      if (vm.ItemSmsInfoForm.isConsigner == '1') {
        if (vm.uploadTableData.length == 0) {
          this.$message({
            message: '委托领取请上传委托书！',
            type: 'error'
          });
          return;
        }

      }
      if (vm.uploadTableData.length > 0) {
        var fileIds = [];
        $.each(vm.uploadTableData, function (idx, obj) {
          fileIds.push(obj.fileId);
        });
        vm.ItemSmsInfoForm.consignerAttId = fileIds.join(",");
      }
      //封装采集参数
      var param = vm.ItemSmsInfoForm;
      request('', {
        url: ctx + 'rest/certificate/updateSendItemInfo',
        type: 'post',
        data: JSON.stringify(param),
        ContentType: "application/json"
      }, function (result) {
        if (result.success) {
          vm.$message({
            message: '更新成功',
            type: 'success'
          });
          vm.editItemSmsInfoModalShow = false;
        }

      }, function (msg) {
        vm.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });


    },
    //===分割点

    // 重置表单
    resetForm: function (formName) {
      this.$refs[formName].resetFields();
    },

//获取已上传证照
    getCertListWin: function (instId, type) {
      var vm = this;

      request('', {
        url: ctx + 'rest/certificate/out/materials/attachments',
        type: 'get',
        data: {'instId': instId, "type": type}
      }, function (res) {
        if (res.success) {
          if (res.content) {
            vm.uploadCertData = res.content;
          }
        } else {
          vm.$message({
            message: res.message ? res.message : '加载已上传材料列表失败',
            type: 'error'
          });
        }
      }, function (msg) {
        vm.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },


    // 获取已上传文件列表
    getFileListWin: function (matid, rowData) {
      var vm = this;
      request('', {
        url: ctx + 'rest/certificate/consignerAtt/list',
        type: 'get',
        data: {'applyinstId': vm.applyinstId}
      }, function (res) {
        if (res.success) {
          if (res.content) {
            vm.uploadTableData = res.content;
            if (rowData) {
              rowData.children = res.content;
            }
          }
        } else {
          vm.$message({
            message: res.message ? res.message : '加载已上传材料列表失败',
            type: 'error'
          });
        }
      }, function (msg) {
        vm.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },

    // 预览电子件
    filePreview: function (data) {
      var vm = this;
      var detailId = data.fileId;
      var flashAttributes = '';
      // console.log(data);
      var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
      var fileName = data.fileName;
      var fileType = this.getFileType(fileName);
      vm.filePreviewCount++;
      if (fileType == 'pdf') {
        var tempwindow = window.open(); // 先打开页面
        setTimeout(function () {
          tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
        }, 1000)
      } else if (regText.test(fileType)) {
        // previewPdf/pdfIsCoverted
        vm.loading = true;
        request('', {
          url: ctx + 'previewPdf/pdfIsCoverted?detailId=' + detailId,
          type: 'get',
        }, function (result) {
          if (result.success) {
            vm.loading = false;
            var tempwindow = window.open(); // 先打开页面
            setTimeout(function () {
              tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
            }, 1000)
          } else {
            if (vm.filePreviewCount > 9) {
              confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                vm.filePreviewCount = 0;
                vm.filePreview(data);
              }, function () {
                vm.filePreviewCount = 0;
                vm.loading = false;
                return false;
              }, '确定', '取消', 'warning', true)

            } else {
              setTimeout(function () {
                vm.filePreview(data);
              }, 1000)
            }
          }
        }, function (msg) {
          vm.loading = false;
          vm.$message({
            message: '文件预览失败',
            type: 'error'
          });
        })
      } else {
        vm.loading = false;
        var tempwindow = window.open(); // 先打开页面
        setTimeout(function () {
          tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
        }, 1000)
      }

      // url = ctx + 'rest/certificate/consignerAtt/preview' + "?detailId=" + detailId + "&flashAttributes=" + flashAttributes;
      /* url = ctx + 'rest/mats/att/preview?detailId='  + detailId + "&flashAttributes=" + flashAttributes;
       try {
           window.open(url);
       } catch (e) {
           vm.$message({
               message: '服务请求失败',
               type: 'error'
           });
       }*/
    },
    // 获取文件后缀
    getFileType: function (fileName) {
      var index1 = fileName.lastIndexOf(".");
      var index2 = fileName.length;
      var suffix = fileName.substring(index1 + 1, index2).toLowerCase();//后缀名
      if (suffix == 'docx') {
        suffix = 'doc';
      }
      return suffix;
    },
    // 勾选电子件
    selectionFileChange: function (val) {
      this.fileSelectionList = val;
    },
    //勾选证照
    selectionCertChange: function (val) {
      this.certSelectionList = val;
    },
    // 下载电子件
    downloadFile: function () {
      var vm = this;
      var detailIds = [];
      if (vm.fileSelectionList.length == 0) {
        vm.$message({
          message: '请勾选数据后操作！',
          type: 'error'
        });
        return false;
      }
      vm.fileSelectionList.map(function (item, index) {
        detailIds.push(item.matinstId);
      });
      detailIds = detailIds.join(',')
      window.location.href = ctx + 'rest/mats/att/download?detailIds=' + detailIds
    },

    // 上传电子件
    uploadFileCom: function (file) {
      var vm = this;
      var rowData = vm.selMatRowData;
      this.fileWinData = new FormData();
      Vue.set(file.file, 'fileName', file.file.name);
      this.fileWinData.append('file', file.file);
      this.fileWinData.append("applyinstId", applyinstId);

      vm.loadingFileWin = true;
      axios.post(ctx + 'rest/certificate/consignerAtt/upload', vm.fileWinData).then(function (res) {
        Vue.set(file.file, 'matinstId', res.data.content)
        // vm.sendInfoForm.consignerAttId = res.data.content;
        vm.getFileListWin(res.data.content, rowData);


        vm.loadingFileWin = false;
        vm.$message({
          message: '上传成功',
          type: 'success'
        });

      }).catch(function (error) {
        vm.loadingFileWin = false;
        if (error.response) {
          vm.$message({
            message: '文件上传失败(' + error.response.status + ')，' + error.response.data,
            type: 'error'
          });
        } else if (error.request) {
          vm.$message({
            message: '文件上传失败，服务器端无响应',
            type: 'error'
          });
        } else {
          vm.$message({
            message: '文件上传失败，请求封装失败',
            type: 'error'
          });
        }

      });
    },
    //删除证照
    delSelectCertCom: function () {

      var vm = this;
      var detailIds = [];
      if (vm.certSelectionList.length == 0) {
        vm.$message({
          message: '请勾选数据后操作！',
          type: 'error'
        });
        return false;
      }
      vm.certSelectionList.map(function (item, index) {
        detailIds.push(item.fileId);
      });
      detailIds = detailIds.join(',')
      request('', {
        url: ctx + 'rest/certificate/cert/delelte',
        type: 'post',
        data: {'detailIds': detailIds}
      }, function (res) {
        if (res.success) {
          vm.getCertListWin(vm.tabActive, vm.editCertFormType);
          vm.$message({
            message: '删除成功',
            type: 'success'
          });
        } else {
          vm.$message({
            message: res.message ? res.message : '删除失败',
            type: 'error'
          });
        }
      }, function (msg) {
        vm.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    // 删除电子件
    delSelectFileCom: function () {
      var vm = this;
      var detailIds = [];
      if (vm.fileSelectionList.length == 0) {
        vm.$message({
          message: '请勾选数据后操作！',
          type: 'error'
        });
        return false;
      }
      vm.fileSelectionList.map(function (item, index) {
        detailIds.push(item.fileId);
      });
      detailIds = detailIds.join(',')
      request('', {
        url: ctx + 'rest/certificate/consigner/att/delelte',
        type: 'post',
        data: {'detailIds': detailIds, 'applyinstId': applyinstId}
      }, function (res) {
        if (res.success) {
          vm.getFileListWin('', vm.selMatRowData);
          vm.$message({
            message: '删除成功',
            type: 'success'
          });
        } else {
          vm.$message({
            message: res.message ? res.message : '删除失败',
            type: 'error'
          });
        }
      }, function (msg) {
        vm.$message({
          message: '服务请求失败',
          type: 'error'
        });
      });
    },
    //打开拍照窗口
    openPhotoWindow: function (data, flag) {
      if (flag == 'lt') {
        lt_openPhotoWindow(data.matinstId, data.iteminstId, data.inoutId, callback)
      } else {
        fz_openPhotoWindow(null, null, null, uploadPhotoFile);
      }
    },


    selChildOrg: function (rowData) {
      this.selCoreItems.branchOrg = rowData.id;
      this.selCoreItems.orgName = rowData.name;
      this.orgTreeInfoModal = false;
    },
    // 刷新页面
    reloadPage: function () {
      if (this.submitCommentsType == 0 || this.submitCommentsType == 3) {
        window.location.reload()
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
    // 关闭窗口-(这里使用的是父级window下面的vue实例中的方法来移除tab,这个tab是ele控件)
    closeWindowTab: function () {
      var ts = this;
      ts.smsSendEndRefresh();

      setTimeout(function () {
        window.parent.vm.removeTab(ts.getSerachParamsForUrl('applyinstId'));
      }, 1000)
    },
    //// 如果是保存取件登记后，那就刷新对应的列表页
    smsSendEndRefresh: function () {
      // debugger
      var _iframs = $(window.parent.document).find('iframe');
      var _panentIframsrc = this.getSerachParamsForUrl('parentIfreamUrl')
      var _tagIframe = '';
      for (var i = 0; i < _iframs.length; i++) {
        var _src = _iframs.eq(i).attr('src');
        if (_src && (_src == _panentIframsrc)) {
          _tagIframe = _iframs.eq(i);
          break;
        }
      }
      _tagIframe[0].contentWindow.location.reload(true);
    },
    //设置已取件页面
    setHadSendIndex: function () {
      var vm = this;
      vm.isSmsSend = '1';
      var _verticalTabData = [];
      _verticalTabData.push(vm.verticalTabData[0]);
      vm.verticalTabData = _verticalTabData;
    }
  },

});

