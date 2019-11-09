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
    return {
      dialogTitie: '',
      pageLoading: false,
      formData: {},
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
      addLinkManRules: {
        linkmanName: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanCertNo: [
          { required: true, validator: checkMissValue, trigger: 'blur' },
        ],
        linkmanMobilePhone: [
          { required: true, validator: checkPhoneNum, trigger: 'blur' },
        ]
      },
      rules: {
        'aeaExProjDrawing.drawingQuabookCode': [
          { required: true, message: '请输入施工图审查合格书编号' },
        ],
        'aeaExProjDrawing.inverstmentMoeny': [
          { required: true, message: '请输入投资额', trigger: ['blur', 'change'] },
        ],
        'aeaExProjDrawing.approveDrawingArea': [
          { required: true, message: '请输入图审面积' },
        ],
        'aeaExProjDrawing.approveStartTime': [
          { required: true, message: '请选择开始审查日期' },
        ],
        'aeaExProjDrawing.approveEndTime': [
          { required: true, message: '请选择审查完成日期' },
        ],
        'aeaExProjDrawing.isOncePass': [
          { required: true, message: '请选择一次审查是否通过' },
        ],
        'aeaExProjDrawing.oncePassAgainstCount': [
          { required: true, message: '请输入一次审查时违反强条数' },
        ],
        'aeaExProjDrawing.oncePassAgainstItem': [
          { required: true, message: '请输入一次审查时违反的强条条目' },
        ],
        'aeaExProjDrawing.approveOpinion': [
          { required: true, message: '请输入施工图审查意见' },
        ],
        'aeaExProjDrawing.approveConfirmTime': [
          { required: true, message: '请选择审图确认时间' },
        ],
        'aeaExProjDrawing.govOrgCode': [
          { required: true, message: '请输入审图确认的行政单位机构代码' },
        ],
        'aeaExProjDrawing.govOrgName': [
          { required: true, message: '请输入审图确认的行政单位名称' },
        ],
        'aeaExProjDrawing.govOrgAreaCode': [
          { required: true, message: '请输入审图确认的行政单位区域码' },
        ],
        'drawings[0].organizationalCode': [
          { required: true, message: '请输入组织机构代码' },
        ],
        'drawings[0].applicant': [
          { required: true, message: '请输入单位名称' },
        ],
        'drawings[0].unitType': [
          { required: true, message: '请选择项目主体类型' },
        ],
        'drawings[0].projectLeader': [
          { required: true, message: '请选择图审机构项目负责人' },
        ],
        'drawings[0].projectLeaderCertNum': [
          { required: true, message: '请输入图审机构项目负责人身份证号码' },
        ],
        'drawings[2].organizationalCode': [
          { required: true, message: '请输入组织机构代码' },
        ],
        'drawings[2].applicant': [
          { required: true, message: '请输入单位名称' },
        ],
        'drawings[2].unitType': [
          { required: true, message: '请选择项目主体类型' },
        ],
        'drawings[2].projectLeader': [
          { required: true, message: '请选择图审机构项目负责人' },
        ],
        'drawings[2].projectLeaderCertNum': [
          { required: true, message: '请输入图审机构项目负责人身份证号码' },
        ],
        'drawings[1].organizationalCode': [
          { required: true, message: '请输入组织机构代码' },
        ],
        'drawings[1].applicant': [
          { required: true, message: '请输入单位名称' },
        ],
        'drawings[1].unitType': [
          { required: true, message: '请选择项目主体类型' },
        ],
        'drawings[1].projectLeader': [
          { required: true, message: '请选择图审机构项目负责人' },
        ],
        'drawings[1].projectLeaderCertNum': [
          { required: true, message: '请输入图审机构项目负责人身份证号码' },
        ],

      }

    }
  },
  created: function() {
    this.projInfoId = '031fb1ad-71b9-41af-8fe0-7df5b449a16d';

  },
  mounted: function() {

    this.getAllType();
    this.showData();
    this.getUnit();
    $(".loading").hide();
  },
  methods: {
    // 请求各个类型数据
    getAllType: function() {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'get',
        url: ctx + 'rest/dict/code/multi/items/list',
        data: {
          dicCodeTypeCodes: 'XM_DWLX,JOB_TITLE,C_PRJ_SPTY,PROJ_UNIT_LINKMAN_TYPE,REGIST_CERTIFI_TYPE'
        },
      }, function(res) {
        vm.proType = res.content.XM_DWLX; //请求项目主体类型
        vm.jobTitle = res.content.JOB_TITLE; //请求职称等级
        vm.prjType = res.content.C_PRJ_SPTY; //请求审查人员专业
        vm.linkmanType = res.content.PROJ_UNIT_LINKMAN_TYPE; //请求项目单位联系人类型
        vm.linkmanType2 = res.content.PROJ_UNIT_LINKMAN_TYPE; //请求项目单位联系人类型
        vm.CertifiType = res.content.REGIST_CERTIFI_TYPE; //请求执业注册证类型

        for (var i = 0; i < vm.linkmanType2.length; i++) {
          if (vm.linkmanType2[i].itemCode == '101001' || vm.linkmanType2[i].itemCode == '102001' || vm.linkmanType2[i].itemCode == '502001') {
            vm.linkmanType2[i].disabled = true;
          }
        }
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 模糊查询人员
    getLinkMan: function(row) {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'get',
        url: ctx + 'rest/form/drawing/list',
        data: {
          keyword: '',
          unitInfoId: row.unitInfoId,
          projInfoId: row.projInfoId
        },
      }, function(res) {
        vm.landAreaUnitSite = res.content;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 模糊查询人员
    getPerson: function(val) {
      var vm = this;
      // vm.loading = true;
      request('', {
        type: 'get',
        url: ctx + 'rest/linkman/list',
        data: {
          keyword: '',
          unitInfoId: val.unitInfoId
        },
      }, function(res) {
        vm.person = res.content;
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
                message: '保存成功',
                type: 'success'
              });
              // _that.addEditManPerform.linkmanName = _that.addEditManform.linkmanName;
              // _that.addEditManPerform.linkmanId = result.content;
              // _that.addEditManPerform.linkmanMail = _that.addEditManform.linkmanMail;
              // _that.addEditManPerform.linkmanCertNo = _that.addEditManform.linkmanCertNo;
              // _that.addEditManPerform.linkmanMobilePhone = _that.addEditManform.linkmanMobilePhone;
              _that.addEditManModalShow = false;
              _that.loading = false;
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
    addPerson: function(row) {
      this.addEditManform = {};
      this.addEditManModalShow = true;
      this.addEditManModalTitle = '新增联系人';
      this.addEditManform.unitName = row.applicant;
      this.addEditManform.unitInfoId = row.unitInfoId;
    },
    edit: function(row, formData) {
      // this.addEditManform = row;
      this.addEditManModalShow = true;
      this.addEditManModalTitle = '编辑联系人';
      this.backDLinkmanInfo(row, formData);
      // this.addEditManform.linkmanName = row.projectLeader;
      // this.addEditManform.linkmanCertNo = row.projectLeaderCertNum;
      // this.addEditManform.linkmanInfoId = row.linkmanInfoId;

    },
    // 人员设置选择人员
    selTypeLinkman: function(typeData, manData) {
      typeData.linkmanName = manData.addressName;
      typeData.linkmanInfoId = manData.addressId;
      typeData.projectLeaderCertNum = manData.addressIdCard;
    },
    // 人员设置选择人员
    selTypeLinkman2: function(typeData, manData) {
      typeData.linkmanName = manData.addressName;
      typeData.linkmanInfoId = manData.addressId;
      typeData.linkmanCertNo = manData.addressIdCard;
    },
    // 新增人员设置
    addLinkmanTypes: function(row, data) {
      var dataType = {
        linkmanInfoId: '',
        linkmanType: '',
        linkmanName: '',
        linkmanCertNo: '',
        prjSpty: '',
        unitProjId: row[0].unitProjId
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
      console.log(val, flag);
      this.$set(data, 'organizationalCode', val.organizationalCode);
      this.$set(data, 'unifiedSocialCreditCode', val.unifiedSocialCreditCode);
      this.$set(data, 'applicant', val.applicant);
      this.$set(data, 'unitInfoId', val.unitInfoId);
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
        vm.formData = res.content;
        console.log(res.content);

        // vm.$nextTick(function() {
        //   vm.$refs['form'].clearValidate();

        // });
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
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
    save: function(formData) {
      var _this = this;
      this.$refs['form'].validate(function(valid) {
        if (!valid) {
          _that.$message({
            message: '请输入完整的联系人信息！',
            type: 'error'
          });
          return false;
        };
        // var aeaExProjDrawing = {
        //   projInfoId: 'a',
        //   drawingId: _this.formData.aeaExProjDrawing.aaa || '',
        //   provinceProjCode: _this.formData.aeaExProjDrawing.aaa || '',
        //   drawingQuabookCode: _this.formData.aeaExProjDrawing.aaa || '',
        //   inverstmentMoeny: _this.formData.aeaExProjDrawing.aaa || '',
        //   approveDrawingArea: _this.formData.aeaExProjDrawing.aaa || '',
        //   approveStartTime: _this.formData.aeaExProjDrawing.aaa || '',
        //   approveEndTime: _this.formData.aeaExProjDrawing.aaa || '',
        //   isOncePass: _this.formData.aeaExProjDrawing.aaa || '',
        //   oncePassAgainstCount: _this.formData.aeaExProjDrawing.aaa || '',
        //   oncePassAgainstItem: _this.formData.aeaExProjDrawing.aaa || '',
        //   approveOpinion: _this.formData.aeaExProjDrawing.aaa || '',
        //   approveConfirmTime: _this.formData.aeaExProjDrawing.aaa || '',
        //   govOrgCode: _this.formData.aeaExProjDrawing.aaa || '',
        //   govOrgName: _this.formData.aeaExProjDrawing.aaa || '',
        //   govOrgAreaCode: _this.formData.aeaExProjDrawing.aaa || ''
        // }
        // var aeaProjDrawing = []
        _this.formData.aeaExProjDrawing.approveStartTime = _this.formatTime(_this.formData.aeaExProjDrawing.approveStartTime, 'Y-M-D') || '';
        _this.formData.aeaExProjDrawing.approveEndTime = _this.formatTime(_this.formData.aeaExProjDrawing.approveEndTime, 'Y-M-D') || '';
        _this.formData.aeaExProjDrawing.approveConfirmTime = _this.formatTime(_this.formData.aeaExProjDrawing.approveConfirmTime, 'Y-M-D') || '';
        _this.formData.aeaExProjDrawing.aeaProjDrawing = _this.formData.drawings;
        request('', {
          type: 'post',
          url: ctx + 'rest/form/drawing/saveAeaExProjDrawing.do',
          ContentType: 'application/json',
          data: JSON.stringify(_this.formData.aeaExProjDrawing)
        }, function(res) {
          if (res.success) {
            _this.$message({
              message: '保存成功',
              type: 'success'
            });
            _this.showData();
          } else {
            _this.$message({
              message: '保存失败',
              type: 'error'
            });
          }
        }, function(err) {
          _this.$message.error('服务器错了哦!');
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