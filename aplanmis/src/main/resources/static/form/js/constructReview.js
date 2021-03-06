var app = new Vue({
  el: '#app',
  data: function() {
    var checkMissValue = function(rule, value, callback) {
      if (value === '' || value === undefined || value === null) {
        callback(new Error('该输入项为必输项！'));
      } else if (value.toString().trim() === '') {
        callback(new Error('该输入项为必输项！'));
      } else {
        callback();
      }
    };
    // 输入为数字 大于等于0（浮点数）
    var checkNumFloat = function(rule, value, callback) {
      if (value) {
        var flag = !/^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/.test(value);
        if (flag) {
          return callback(new Error('格式错误'));
        } else {
          callback();
        }

      } else {
        callback();
      }
    };
    // 输入为整数数字 大于等于0
    var checkMissNum = function(rule, value, callback) {
      if (value) {
        var flag = !/^[+]{0,1}(\d+)$/.test(value);
        if (flag) {
          return callback(new Error('格式错误'));
        } else {
          callback();
        }

      } else {
        callback();
      }
    };
    var checkPhoneNum = function(rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('必填字段！'));
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
    var checkUnifiedSocialCreditCode = function(rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('请输入统一社会信用代码！'));
      } else if (value) {
        var flag = !/^[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}$/.test(value);
        if (flag) {
          return callback(new Error('请输入正确的统一社会信用代码！'));
        } else {
          callback();
        }

      } else {
        callback();
      }
    };
    var checkProjectLeaderCertNum = function(rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('请输入身份证号码！'));
      } else if (value) {
        var flag = !/^[0-9a-zA-Z]*$/.test(value) || /^[\u4E00-\u9FA5]+$/.test(value);
        var len = value.length;
        if (flag) {
          return callback(new Error('请输入正确的身份证号码！'));
        } else {
          if (len != 18) {
            return callback(new Error('请输入18位身份证号码！'));
          } else {
            callback();
          }
        }

      } else {
        callback();
      }
    };
    var checkOrganizationalCode = function(rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('请输入组织机构代码！'));
      } else if (value) {
        var flag = !/^[0-9A-Z]*$/.test(value) || /^[\u4E00-\u9FA5]+$/.test(value);
        var len = value.length;
        if (flag) {
          return callback(new Error('请输入正确的组织机构代码！'));
        } else {
          if (len != 9) {
            return callback(new Error('请输入9位组织机构代码！'));
          } else {
            callback();
          }
        }

      } else {
        callback();
      }
    };
    return {
      dialogTitie: '',
      pageLoading: false,
      formData: {
        drawings: []
      },
      formDataTuShen: {},
      formDataKanCha: {},
      formDataSheJj: {},
      total: 0,
      tableData: [],
      activeNames: '1',
      fieldType: [],
      landAreaUnitSite: [],
      moneyType: '人民币',
      proType: [],
      CertifiType: [],
      jobTitle: [],
      prjType: [],
      linkmanType: [],
      linkmanType2: [], //不包含101001 勘察项目负责人  502001施工图审查机构项目负责人  102001设计项目负责人
      person: [],
      projNameSelect: [],
      addEditManModalShow: false,
      addEditManModalTitle: '',
      addEditManform: {},
      unitInfoIdList: [],
      addEditManModalFlag: 'add',
      shenchaPerson: [],
      shejiPerson: [],
      kanchaPerson: [],
      curUnit: '',
      addLinkManRules: {
        linkmanName: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanCertNo: [
          { required: true, validator: checkProjectLeaderCertNum, trigger: 'blur' },
        ],
        linkmanMobilePhone: [
          { required: true, validator: checkPhoneNum, trigger: 'blur' },
        ]
      },
      rules: {
        'drawingQuabookCode': [
          { required: true, message: '请输入施工图审查合格书编号' },
        ],
        'inverstmentMoeny': [
          { validator: checkNumFloat, trigger: ['blur'] },
          { required: true, message: '请输入投资额', trigger: ['blur', 'change'] },
        ],
        'approveDrawingArea': [
          { validator: checkNumFloat, trigger: ['blur'] },
          { required: true, message: '请输入图审面积' },
        ],
        'approveStartTime': [
          { required: true, message: '请选择开始审查日期' },
        ],

        'approveEndTime': [
          { required: true, message: '请选择审查完成日期' },
        ],
        'isOncePass': [
          { required: true, message: '请选择一次审查是否通过' },
        ],
        'oncePassAgainstCount': [
          { required: true, message: '请输入一次审查时违反强条数' },
        ],
        'oncePassAgainstItem': [
          { required: true, message: '请输入一次审查时违反的强条条目' },
        ],
        'approveOpinion': [
          { required: true, message: '请输入施工图审查意见' },
        ],
        'approveConfirmTime': [
          { required: true, message: '请选择审图确认时间' },
        ],
        'govOrgCode': [
          { required: true, message: '请输入审图确认的行政单位机构代码' },
        ],
        'govOrgName': [
          { required: true, message: '请输入审图确认的行政单位名称' },
        ],
        'govOrgAreaCode': [
          { required: true, message: '请输入审图确认的行政单位区域码' },
        ],

      },
      rules2: {
        'organizationalCode': [
          { required: true, validator: checkOrganizationalCode, trigger: ['blur'] },
        ],
        'unifiedSocialCreditCode': [
          { required: true, validator: checkUnifiedSocialCreditCode, trigger: ['blur'] },
        ],
        'applicant': [
          { required: true, message: '请输入单位名称' },
        ],
        'unitType': [
          { required: true, message: '请选择项目主体类型' },
        ],
        'prjSpty': [
          { required: true, message: '请选择审查专业' },
        ],
        'projectLeader': [
          { required: true, message: '请选择图审机构项目负责人' },
        ],
        'projectLeaderCertNum': [
          { required: true, validator: checkProjectLeaderCertNum, trigger: ['blur'] },
        ],
      },
      rules3: {
        'organizationalCode': [
          { required: true, validator: checkOrganizationalCode, trigger: ['blur'] },
        ],
        'unifiedSocialCreditCode': [
          { required: true, validator: checkUnifiedSocialCreditCode, trigger: ['blur'] },
        ],
        'applicant': [
          { required: true, message: '请输入单位名称' },
        ],
        'unitType': [
          { required: true, message: '请选择项目主体类型' },
        ],
        'prjSpty': [
          { required: true, message: '请选择审查专业' },
        ],
        'projectLeader': [
          { required: true, message: '请选择勘查单位项目负责人' },
        ],
        'projectLeaderCertNum': [
          { required: true, validator: checkProjectLeaderCertNum, trigger: ['blur'] },
        ],
      },
      rules4: {
        'organizationalCode': [
          { required: true, validator: checkOrganizationalCode, trigger: ['blur'] },
        ],
        'unifiedSocialCreditCode': [
          { required: true, validator: checkUnifiedSocialCreditCode, trigger: ['blur'] },
        ],
        'applicant': [
          { required: true, message: '请输入单位名称' },
        ],
        'unitType': [
          { required: true, message: '请选择项目主体类型' },
        ],
        'prjSpty': [
          { required: true, message: '请选择审查专业' },
        ],
        'projectLeader': [
          { required: true, message: '请选择设计单位项目负责人' },
        ],
        'projectLeaderCertNum': [
          { required: true, validator: checkProjectLeaderCertNum, trigger: ['blur'] },
        ],
      },

    }
  },
  created: function() {
    // this.projInfoId = '0087be4f-acb6-4727-ab3a-e7f8440ff815';
    this.projInfoId = this.getUrlParam('projInfoId');
  },
  mounted: function() {

    this.getAllType();
    this.showData();
    // this.getUnit();
  },
  methods: {
    // 获取页面的URL参数
    getUrlParam: function(val) {
      var svalue = location.search.match(new RegExp("[\?\&]" + val + "=([^\&]*)(\&?)", "i"));
      return svalue ? svalue[1] : svalue;
    },
    init: function(val) {
      if (val == 'sheji') {
        var dataType3 = {
          linkmanInfoId: '',
          linkmanType: '6',
          linkmanName: '',
          linkmanCertNo: '',
          prjSpty: '1',
          unitProjId: '',
          unitInfoId: ''
        }
        this.formDataSheJj.linkmen.push(dataType3);

      } else {
        var dataType = {
          linkmanInfoId: '',
          linkmanType: '7',
          linkmanName: '',
          linkmanCertNo: '',
          prjSpty: '1',
          unitProjId: '',
          unitInfoId: ''
        }
        var dataType2 = {
          linkmanInfoId: '',
          linkmanType: '8',
          linkmanName: '',
          linkmanCertNo: '',
          prjSpty: '1',
          unitProjId: '',
          unitInfoId: ''
        }

        this.formDataTuShen.linkmen.push(dataType);
        this.formDataTuShen.linkmen.push(dataType2);
      }


    },
    // 请求table数据
    showData: function() {
      var vm = this;
      // vm.loading = true;
      request('', {
          type: 'post',
          url: ctx + 'rest/form/drawing/index.do',
          data: {
            projInfoId: this.projInfoId
          },
        }, function(res) {
          if (!res.success) {
            vm.formDataTuShen = {};
            vm.formDataKanCha = {};
            vm.formDataSheJj = {};
          } else {
            for (var i = 0; i < res.content.drawings.length; i++) {
              if (res.content.drawings[i].unitType == '25') {
                vm.formDataTuShen = res.content.drawings[i] || {};
              } else if (res.content.drawings[i].unitType == '4') {
                vm.formDataSheJj = res.content.drawings[i] || {};
              } else {
                vm.formDataKanCha = res.content.drawings[i] || {};
              }
            }
          }

          debugger;
          if (vm.formDataTuShen.linkmen == undefined || vm.formDataTuShen.linkmen.length == 0) {
            vm.formDataTuShen.linkmen = [];
            vm.init('tushen');
          }
          if (vm.formDataSheJj.linkmen == undefined || vm.formDataSheJj.linkmen.length == 0) {
            vm.formDataSheJj.linkmen = [];
            vm.init('sheji');
          }


          vm.formDataTuShen.linkmanType = '1';
          vm.formDataKanCha.linkmanType = '1';
          vm.formDataSheJj.linkmanType = '1';
          vm.formDataTuShen.unitType = '25';
          vm.formDataKanCha.unitType = '3';
          vm.formDataSheJj.unitType = '4';

          if (!res.success) {
            vm.$message({
              message: res.message,
              type: 'error'
            });

            return;
          }

          for (var key in res.content.aeaExProjDrawing) {
            vm.$set(vm.formData, key, res.content.aeaExProjDrawing[key])
          }


          vm.$nextTick(function() {
            vm.$refs['form'].clearValidate();

          });
        },
        function(err) {
          vm.$message.error('服务器错了哦!');
        })
    },
    // 请求各个类型数据
    getAllType: function() {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'get',
        url: ctx + 'rest/dict/code/multi/items/list',
        data: {
          dicCodeTypeCodes: 'XM_DWLX,C_TITLE,C_PRJ_SPTY,PROJ_UNIT_LINKMAN_TYPE,C_REG_LCN_TYPE'
        },
      }, function(res) {
        vm.proType = res.content.XM_DWLX; //请求项目主体类型
        vm.jobTitle = res.content.C_TITLE; //请求职称等级
        vm.prjType = res.content.C_PRJ_SPTY; //请求审查人员专业
        vm.linkmanType = res.content.PROJ_UNIT_LINKMAN_TYPE; //请求项目单位联系人类型
        vm.linkmanType2 = res.content.PROJ_UNIT_LINKMAN_TYPE; //请求项目单位联系人类型
        vm.CertifiType = res.content.C_REG_LCN_TYPE; //请求执业注册证类型


        for (var i = 0; i < vm.linkmanType2.length; i++) {
          if (vm.linkmanType2[i].itemCode == '1' || vm.linkmanType2[i].itemCode == '4' || vm.linkmanType2[i].itemCode == '5') {
            vm.linkmanType2[i].disabled = true;
          }
        }
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 联网查询
    searchFromInternet: function(data, code) {
      var _this = this;
      if (!code) {
        _this.$message({
          message: '请输入要查询的组织机构代码！',
          type: 'error'
        });
        return false;
      }

      request('', {
        type: 'get',
        url: ctx + 'rest/form/project/webservice/getGdEnterpriseInfo',
        data: {
          orgCode: code
        },
      }, function(res) {
        if (!res.success) {
          _this.$message({
            message: '未查询到该组织机构代码！',
            type: 'error'
          });
          return false;
        }
        _this.$set(data, 'unitInfoId', res.content.unitInfoId);
        _this.$set(data, 'applicant', res.content.applicant);
        _this.$set(data, 'organizationalCode', res.content.organizationalCode);
        _this.$set(data, 'unifiedSocialCreditCode', res.content.unifiedSocialCreditCode);

      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 联网查询联系人
    searchMan: function(data, code) {
      var _this = this;
      if (!code) {
        _this.$message({
          message: '请输入要查询的联系人证件号！',
          type: 'error'
        });
        return false;
      }

      request('', {
        type: 'get',
        url: ctx + 'rest/form/project/webservice/getGdPersonInfo',
        data: {
          idNum: code
        },
      }, function(res) {
        if (!res.success) {
          _this.$message({
            message: '未查询到该证件号所属联系人！',
            type: 'error'
          });
          return false;
        }
        _this.$set(_this.addEditManform, 'linkmanName', res.content.linkmanName);
        _this.$set(_this.addEditManform, 'linkmanMobilePhone', res.content.linkmanMobilePhone);
        _this.$set(_this.addEditManform, 'linkmanOfficePhon', res.content.linkmanOfficePhon);
        _this.$set(_this.addEditManform, 'linkmanFax', res.content.linkmanFax);
        _this.$set(_this.addEditManform, 'linkmanMail', res.content.linkmanMail);
        _this.$set(_this.addEditManform, 'linkmanAddr', res.content.linkmanAddr);
        _this.$set(_this.addEditManform, 'linkmanMemo', res.content.linkmanMemo);

      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 模糊查询人员
    getPerson: function(val, type) {
      var vm = this;
      if (!val.unitInfoId) {
        return;
      }
      request('', {
        type: 'get',
        url: ctx + 'rest/linkman/list',
        data: {
          keyword: '',
          unitInfoId: val.unitInfoId
        },
      }, function(res) {
        if (type == "shencha") {
          vm.shenchaPerson = res.content;
        } else if (type == "sheji") {
          vm.shejiPerson = res.content;
        } else {
          vm.kanshaPerson = res.content;
        }
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 获取单位
    getUnit: function() {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'get',
        url: ctx + 'rest/unit/list/by/' + this.projInfoId,
        data: {},
      }, function(res) {
        vm.unitInfoIdList = res.content;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    changeData: function(oldData, newData) {
      var _this = this;
      oldData.forEach(function(item) {
        if (item.addressId == newData.addressId) {
          item.addressIdCard = newData.addressIdCard;
          item.addressMail = newData.addressMail;
          item.addressName = newData.addressName;
          item.addressPhone = newData.addressPhone;
        }
      });
    },
    // 保存联系人信息
    saveAeaLinkmanInfo: function(linkmanType) {
      var _that = this;
      _that.addEditManform.linkmanType = linkmanType
      _that.$refs['addEditManform'].validate(function(valid) {
        if (valid) {
          _that.loading = true;
          var url, msg;
          if (_that.addEditManModalFlag == 'edit') {
            url = 'rest/linkman/edit';
            msg = '编辑联系人信息保存成功';
          } else {
            url = 'rest/linkman/save'
            msg = '新增联系人信息保存成功';
          }
          request('', {
            url: ctx + url,
            type: 'post',
            data: _that.addEditManform
          }, function(result) {
            if (result.success) {
              _that.$message({
                message: msg,
                type: 'success'
              });

              _that.addEditManModalShow = false;
              _that.loading = false;
              var item = {
                addressId: result.content,
                addressIdCard: _that.addEditManform.linkmanCertNo,
                addressMail: _that.addEditManform.linkmanMail,
                addressName: _that.addEditManform.linkmanName,
                addressPhone: _that.addEditManform.linkmanMobilePhone,
              }
              if (_that.addEditManModalFlag == 'add') {
                if (_that.curUnit == 'shencha') {
                  _that.shenchaPerson.push(item);
                } else if (_that.curUnit == 'sheji') {
                  _that.shejiPerson.push(item);
                } else {
                  _that.kanchaPerson.push(item);
                }
              } else {
                if (_that.curUnit == 'shencha') {
                  _that.changeData(_that.shenchaPerson, item);
                } else if (_that.curUnit == 'sheji') {
                  _that.changeData(_that.shejiPerson, item);
                } else {
                  _that.changeData(_that.kanchaPerson, item);
                }
              }
            }
          }, function(msg) {
            _that.$message({
              message: msg.message ? msg.message : '保存失败！',
              type: 'error'
            });
            _that.loading = false;
          });
        } else {
          _that.$message({
            message: '请输入完整的联系人信息！',
            type: 'error'
          });
          return false;
        }
      });
    },
    // 反显联系人信息
    backDLinkmanInfo: function(data, formData) {
      var _that = this;
      if (data.linkmanInfoId) {
        request('', {
          url: ctx + 'rest/linkman/one/' + data.linkmanInfoId,
          type: 'get'
        }, function(result) {
          if (result.success) {
            _that.addEditManform = result.content;
            if (formData == '') {
              _that.addEditManform.unitName = data.applicant;
              _that.addEditManform.unitInfoId = data.unitInfoId;
            } else {
              _that.addEditManform.unitName = formData.applicant;
              _that.addEditManform.unitInfoId = formData.unitInfoId;
            }
          }
        }, function(msg) {
          alertMsg('', '服务请求失败', '关闭', 'error', true);
        });
      } else {
        _that.aeaLinkmanInfoList = {};
      }
    },
    addPerson: function(row, type) {
      this.curUnit = type;
      this.addEditManform = {};
      this.addEditManModalShow = true;
      this.addEditManModalTitle = '新增联系人';
      this.addEditManform.unitName = row.applicant;
      this.addEditManform.unitInfoId = row.unitInfoId;
    },
    add: function(row, type) {
      this.curUnit = type;

      this.addEditManModalShow = true;
      this.addEditManModalTitle = '新增联系人';
      this.addEditManform.unitName = row.applicant;
      this.addEditManform.unitInfoId = row.unitInfoId;
    },
    edit: function(row, formData, type) {
      // this.addEditManform = row;
      this.curUnit = type;
      this.addEditManModalShow = true;
      this.addEditManModalTitle = '编辑联系人';
      this.backDLinkmanInfo(row, formData);
      // this.addEditManform.linkmanName = row.projectLeader;
      // this.addEditManform.linkmanCertNo = row.projectLeaderCertNum;
      // this.addEditManform.linkmanInfoId = row.linkmanInfoId;

    },
    // 人员设置选择人员
    selTypeLinkman: function(typeData, manData) {
      this.$set(typeData, 'linkmanCertNo', manData.addressName);
      this.$set(typeData, 'linkmanInfoId', manData.addressId);
      this.$set(typeData, 'projectLeaderCertNum', manData.addressIdCard);

      // typeData.linkmanName = manData.addressName;
      // typeData.linkmanInfoId = manData.addressId;
      // typeData.projectLeaderCertNum = manData.addressIdCard;
    },
    // 人员设置选择人员
    selTypeLinkman2: function(typeData, manData) {
      this.$set(typeData, 'linkmanName', manData.addressName);
      this.$set(typeData, 'linkmanInfoId', manData.addressId);
      this.$set(typeData, 'linkmanCertNo', manData.addressIdCard);
      this.forceUpdate();
      // typeData.linkmanName = manData.addressName;
      // typeData.linkmanInfoId = manData.addressId;
      // typeData.linkmanCertNo = manData.addressIdCard;
    },
    // 新增人员设置
    addLinkmanTypes: function(row, data) {
      var dataType = {
        linkmanInfoId: '',
        linkmanType: data.unitType == '3' ? '6' : '7',
        linkmanName: '',
        linkmanCertNo: '',
        prjSpty: '1',
        unitInfoId: data.unitInfoId,
        unitProjId: data.unitProjId
      }
      row.push(dataType);
    },
    // 新增人员设置
    delLinkmanTypes: function(row, index) {
      row.splice(index, 1);
    },
    // 项目名称过滤
    createFilter: function(queryString) {
      return function(projNameSelect) {
        return (projNameSelect.value.toLowerCase());
      };
    },
    // 切换单位
    selUnitInfo: function(val, data) { // val选中单位信息 flag 单位类型（jingban,jianshe） index单位索引
      this.$set(data, 'organizationalCode', val.organizationalCode);
      this.$set(data, 'unifiedSocialCreditCode', val.unifiedSocialCreditCode);
      this.$set(data, 'applicant', val.applicant);
      this.$set(data, 'unitInfoId', val.unitInfoId);
      if (data.linkmen && data.linkmen.length != 0) {
        for (var i = 0; i < data.linkmen.length; i++) {
          data.linkmen[i].unitInfoId = val.unitInfoId;
        }
      }

    },
    //单位名称模糊查询
    querySearchJiansheName: function(queryString, cb) {
      var _that = this;
      if (typeof(queryString) != 'undefined') queryString = queryString.trim();
      if (queryString != '' && queryString.length >= 2) {
        request('', {
          url: ctx + 'rest/unit/list',
          type: 'get',
          data: { "keyword": queryString, "projInfoId": _that.projInfoId },
        }, function(result) {
          if (result) {
            _that.projNameSelect = result.content;

            _that.projNameSelect.map(function(item) {
              if (item) {
                Vue.set(item, 'value', item.applicant);
              }
            })
            var results = queryString ? _that.projNameSelect.filter(_that.createFilter(queryString)) : _that.projNameSelect;
            // 调用 callback 返回建议列表的数据
            cb(results);
          }
        }, function(msg) {
          cb([]);
        })
      } else {
        cb([]);
      }
    },

    formatNumber: function(n) {
      n = n.toString()
      return n[1] ? n : '0' + n
    },
    formatTime: function(number, format) {

      var formateArr = ['Y', 'M', 'D'];
      var returnArr = [];

      var date = new Date(number);
      returnArr.push(date.getFullYear());
      returnArr.push(this.formatNumber(date.getMonth() + 1));
      returnArr.push(this.formatNumber(date.getDate()));

      for (var i in returnArr) {
        format = format.replace(formateArr[i], returnArr[i]);
      }
      return format;
    },
    save: function(formData, formDataTuShen, formDataKanCha, formDataSheJj) {
      var _this = this;
      _this.$refs['form'].validate(function(valid) {
        _this.$refs['form2'].validate(function(valid) {
          _this.$refs['form3'].validate(function(valid) {
            _this.$refs['form4'].validate(function(valid) {
              if (!valid) {
                _this.$message({
                  message: '请输入完整的联系人信息！',
                  type: 'error'
                });
                return false;
              };
              for (var i = 0; i < formDataTuShen.linkmen.length; i++) {
                if (formDataTuShen.linkmen[i].linkmanName == '') {
                  _this.$message({
                    message: '请设置人员！',
                    type: 'error'
                  });
                  return false;
                }
              }
              for (var i = 0; i < formDataTuShen.linkmen.length; i++) {
                if (formDataTuShen.linkmen[i].prjSpty == '') {
                  _this.$message({
                    message: '请选择审查专业！',
                    type: 'error'
                  });
                  return false;
                }
              }
              for (var i = 0; i < formDataSheJj.linkmen.length; i++) {
                if (formDataSheJj.linkmen[i].linkmanName == '') {
                  _this.$message({
                    message: '请设置人员！',
                    type: 'error'
                  });
                  return false;
                }
              }
              for (var i = 0; i < formDataSheJj.linkmen.length; i++) {
                if (formDataSheJj.linkmen[i].prjSpty == '') {
                  _this.$message({
                    message: '请选择审查专业！',
                    type: 'error'
                  });
                  return false;
                }
              }
              var shenChaLinkManInfoIds = '';
              var kanChaLinkManInfoIds = '';
              var sheJiLinkManInfoIds = '';

              _this.shenchaPerson.forEach(function(item) {
                if (item['addressId'] != '' & item['addressId'] != '1') {
                  shenChaLinkManInfoIds += item['addressId'] + ",";
                }
              });

              _this.shejiPerson.forEach(function(item) {
                if (item['addressId'] != '' & item['addressId'] != '1') {
                  sheJiLinkManInfoIds += item['addressId'] + ",";
                }
              });

              _this.kanchaPerson.forEach(function(item) {
                if (item['addressId'] != '' & item['addressId'] != '1') {
                  kanChaLinkManInfoIds += item['addressId'] + ",";
                }
              });

              formDataTuShen.linkManInfoIds = shenChaLinkManInfoIds.length > 0 ? shenChaLinkManInfoIds.substring(0, shenChaLinkManInfoIds.length - 1) : '';
              formDataKanCha.linkManInfoIds = kanChaLinkManInfoIds.length > 0 ? kanChaLinkManInfoIds.substring(0, kanChaLinkManInfoIds.length - 1) : '';
              formDataSheJj.linkManInfoIds = sheJiLinkManInfoIds.length > 0 ? sheJiLinkManInfoIds.substring(0, sheJiLinkManInfoIds.length - 1) : '';

              var drawings = [];
              drawings.push(formDataTuShen);
              drawings.push(formDataKanCha);
              drawings.push(formDataSheJj);
              var aeaExProjDrawing = {};
              _this.formData.approveStartTime = _this.formatTime(_this.formData.approveStartTime, 'Y-M-D') || '';
              _this.formData.approveEndTime = _this.formatTime(_this.formData.approveEndTime, 'Y-M-D') || '';
              // _this.formData.approveConfirmTime = _this.formatTime(_this.formData.approveConfirmTime, 'Y-M-D') || '';
              _this.formData.projInfoId = _this.projInfoId;

              aeaExProjDrawing = _this.formData;
              aeaExProjDrawing.aeaProjDrawing = drawings;
              request('', {
                type: 'post',
                url: ctx + 'rest/form/drawing/saveAeaExProjDrawing.do',
                ContentType: 'application/json',
                data: JSON.stringify(aeaExProjDrawing)
              }, function(res) {
                if (res.success) {
                  _this.$message({
                    message: '保存成功',
                    type: 'success'
                  });
                  _this.showData();
                } else {
                  _this.$message({
                    message: '保存失败' + res.content,
                    type: 'error'
                  });
                }
              }, function(err) {
                _this.$message.error('服务器错了哦!');
              })
            })
          })
        })
      })

    },
    // 解决sleect选不中值问题
    forceUpdate: function() {
      this.$forceUpdate()
    },
  },
  watch: {

  }
})