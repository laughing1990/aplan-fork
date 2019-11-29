/*
 * @Author: ZL
 * @Date:   2019/05/29
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var vm = new Vue({
  el: '#app',
  data: function() {
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
    var checkProjectLeaderCertNum = function(rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('请输入单位负责人身份证号码！'));
      } else if (value) {
        var flag = !/^[0-9a-zA-Z]*$/.test(value) || /^[\u4E00-\u9FA5]+$/.test(value);
        var len = value.length;
        if (flag) {
          return callback(new Error('请输入正确的单位负责人身份证号码！'));
        } else {
          if (len != 18) {
            return callback(new Error('请输入18位单位负责人身份证号码！'));
          } else {
            callback();
          }
        }

      } else {
        callback();
      }
    };
    return {
      step: '1',
      dialogTitie: '',
      pageLoading: false,
      mloading: false,
      formData: {},
      formData2: {},
      formDataZgxx: {},
      activeNames: ['1', '2', '3'],
      activeNames2: ['1', '2', '3'],
      activeNames3: ['1'],
      activeNames4: ['1', '2', '3'],
      fileList: [],
      tableData: [{
        name: '统一社会信用代码的营业执照（属社会组织的提供社会团体法人登记证或民办非企业单位法人登记证，属事业单位的提供法人登记证）',
        bb: '是',
        fileList: []
      }, {
        name: '法定代表人身份证（原件正反面扫描件）',
        bb: '是',
        fileList: []
      }],
      certData: [{
        name: "王权利",
        aa: '123847847',
        bb: '244871878174871847',
        cc: '测绘',

      }],
      tableData2: [{
        name: '资质（资格）证书（原件扫描件）',
        bb: '是',
        fileList: []
      }],
      certData: [{
        name: "王权利",
        aa: '123847847',
        bb: '244871878174871847',
        cc: '测绘',

      }],
      matData: [{
        name: '执业/执业人员身份证（原件正反面扫描件）',
        bb: '是',
        fileList: []
      }],
      formPersonInfo: {},
      value: [],
      districtList: [],
      IdList: [],
      moneyList: [],
      projTypeList: [],
      typeList: [],
      serviceType: [],
      serviceType2: [],
      recordList: [],
      matTypeList: [],
      personList: [],
      IdType: [],
      getFileListWindowFlag: false,
      searchFileListfilter: '',
      tabBind: 'one',
      matDialog: false,
      shareFileList: [{
        attName1: '广东凯信工程造价咨询有限公司',
        attName2: '营业执照附件.jpg',
        attName3: '2019-02-16 02:11',
      }],
      certigierData: [],
      matType: '',
      // 上传附件
      fileList: [], //第一页附件
      fileList2: [], //第二页附件
      fileList3: [], //第三页附件
      loadingFileWin: false,
      showUploadWindowTitle: '上传附件',
      showUploadWindowFlag: false,
      uploadTableData: [],
      options: [],
      curUpData: [], //当前上传的表格行数据
      type: '',
      //资质专业等级
      needServiceList2: [],
      activeSN: '',
      activeLE: '',
      qualArry: [],
      aptTreeaFlag: true,
      serviceTreeData: [],
      jobPeopleDetailObj: {
        certinstBVos: []
      },
      props: {
        value: 'regionNum',
        label: 'regionName'
      },
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      curGrade: '',
      certTypeZige: [],
      gradeType: [],
      qualId: null,
      qualLevelId: null,
      selectdRecord: [],
      majorQualRequiresArry: [],
      curSeviceId: '', //当前选择的服务类型
      //新增业务授权人
      addEditManModalTitle: '添加业务授权人',
      addEditManModalShow: false,
      addEditManform: {},
      // 职业人员
      idCard: '身份证',
      addCert: '新增证书',
      addCertShow: false,
      certificateDetailObj: {
        certId: '', //证书类型
        certinstCode: '', //证书编码
        certinstName: '', //证书名称
        managementScope: '', //业务范围
        majorId: [], //所属专业
        qualLevelId: '', //等级
        qualId: '', //资质
        bscAttFileAndDirs: [], //附件列表
      },
      certRule: {},
      addCertificateOptions: [],
      serviceQual: [],
      certinstDetail: [],
      curSelectQualiTreeData: [],
      allSubordinateMajorData: [],
      treeProps: {
        children: 'child',
        label: 'name'
      },
      certificateEditFlag: '',
      certList: [],
      enclosureFileListArr: [],
      // 证书详情初始数据
      CERTIFICATE_DETAIL_OBJ: {
        certId: '', //证书类型
        certinstCode: '', //证书编码
        certinstName: '', //证书名称
        managementScope: '', //业务范围
        termStart: '', //有效期限-start
        termEnd: '', //有效期限-end
        majorId: [], //所属专业
        qualLevelId: '', //等级
        bscAttFileAndDirs: '', //附件列表
      },
      loginName: '1231231',
      password: '123213',
      addLinkManRules: {
        linkmanName: [
          { required: true, message: '请输入联系人姓名' },
        ],
        linkmanMobilePhone: [
          { required: true, validator: checkPhoneNum, trigger: ['blur'] },
        ],
        linkmanCertNo: [
          { required: true, validator: checkProjectLeaderCertNum, trigger: ['blur'] },
        ],
        unitServiceIds: [
          { required: true, message: '请输入服务类型' },
        ]
      },
      rules: {
        applicant: [
          { required: true, message: '请输入机构名称' },
        ],
        unitNature: [
          { required: true, message: '请选择机构性质' },
        ],
        idtype: [
          { required: true, message: '请选择机构代码类型' },
        ],
        unifiedSocialCreditCode: [
          { required: true, message: '统一社会信用码' },
        ],
        idrepresentative: [
          { required: true, message: '请输入法定代表人' },
        ],
        idno: [
          { required: true, message: '请输入法人代表证件号' },
        ],
        registeredCapital: [
          { required: true, message: '请输入注册资本' },
        ],
        managementScope: [
          { required: true, message: '请输入经营范围' },
        ],
        applicantDistrict: [
          { required: true, message: '请选择注册地行政区划' },
        ],
        applicantDetailSite: [
          { required: true, message: '请输入注册详细地址' },
        ],
        registerAuthority: [
          { required: true, message: '请输入登记机关' },
        ]

      },
      rules2: {
        linkmanName: [
          { required: true, message: '请输入联系人' },
        ],
        linkmanMail: [
          { required: true, message: '请输入电子邮箱' },
        ],
        linkmanMobilePhone: [
          { required: true, message: '移动电话' },
        ]
      },
      rulesZgxx: {
        serviceId: [
          { required: true, message: '请选择服务类型' },
        ],
        certId: [
          { required: true, message: '请选择证书类型' },
        ],
        certinstCode: [
          { required: true, message: '请输入资格证书编号' },
        ],
        termStart: [
          { required: true, message: '请选择资格有效期起' },
        ],
        termEnd: [
          { required: true, message: '请选择资格有效期止' },
        ],
        managementScope: [
          { required: true, message: '请输入业务范围' },
        ]
      },
      rulesPersonInfo: {
        linkmanName: [
          { required: true, message: '请对输入姓名', trigger: ['blur'] }
        ],
        linkmanCertNo: [
          { required: true, message: '请对输入证件号', trigger: ['blur'] },
        ],
        practiseDate: [
          { required: true, message: '请选择从业时间', trigger: ['blur'] },
        ],
        serviceId: [
          { required: true, message: '请选择服务类型', trigger: ['blur'] },
        ]
      }
    }
  },
  mounted: function() {
    this.getRegion();
    this.getService();
    this.getDicCode();
    this.getAeaCertInfo(); //获取证书类型
  },
  methods: {
    // 请求table数据
    getRegion: function() {
      var vm = this;
      request('', {
        type: 'get',
        url: ctx + 'rest/org/region/tree',
        data: {}
      }, function(res) {
        vm.options = res.content;
        vm.clearchildren(vm.options);
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    clearchildren(arr) {
      var _this = this;
      for (var i = 0; i < arr.length; i++) {
        if (!arr[i].children.length) {
          arr[i].children = null;
        } else {
          _this.clearchildren(arr[i].children);
        }
      }
      return arr;
    },
    // 请求服务类型
    getService: function() {
      var _this = this;
      request('', {
        type: 'post',
        url: ctx + 'supermarket/purchase/getAllService ',
        data: {}
      }, function(res) {
        _this.serviceType = res.content;
        _this.serviceType2 = res.content;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 请求专业
    getGrade: function(id) {
      var vm = this;

      request('', {
        type: 'post',
        url: ctx + 'aea/supermarket/serviceMatter/getMajorTreeByServiceId.do',
        data: {
          serviceId: id
        }
      }, function(res) {
        vm.qualArry = res;
        vm.needServiceList2 = vm.qualArry[0].levelList;
        vm.serviceTreeData = vm.qualArry[0].majorTree;
        vm.qualLevelId = vm.qualArry[0].levelList[0].qualLevelId;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    // 请求机构代码类型
    getDicCode: function() {
      var vm = this;
      request('', {
        type: 'get',
        url: ctx + 'rest/dict/code/multi/items/list',
        data: {
          dicCodeTypeCodes: 'IDTYPE'
        }
      }, function(res) {
        vm.projTypeList = res.content.IDTYPE;
      }, function(err) {
        vm.$message.error('服务器错了哦!');
      })
    },
    selectService: function(id) {

      this.getGrade(id);
      this.curSeviceId = id;
      this.selectdRecord = []; //清掉上一次的选中的资质树数据

    },
    onfocus: function() {
      if (!this.formDataZgxx.serviceId) {
        vm.$message({
          message: '请先选择服务类型',
          type: 'error'
        });
        return;
      }
    },
    toggleGrade: function(val) {

    },

    handleClickAtiveQA: function(tab) {
      var vm = this;
      var tabIndex = Number(tab.index);
      vm.activeSN = tab.index;

      vm.needServiceList2 = vm.qualArry[tabIndex].levelList;
      vm.serviceTreeData = vm.qualArry[tabIndex].majorTree;
      vm.qualLevelId = vm.needServiceList2[tabIndex].qualLevelId;

      if (vm.selectdRecord.length != 0 && vm.selectdRecord[0].qualLevelId == vm.needServiceList2[vm.activeLE].qualLevelId) {
        this.$refs.treeService.setCheckedKeys(vm.selectdRecord[0].majorQualId);
      } else {
        this.$refs.treeService.setCheckedKeys([]);
      }
    },
    handleClickAtiveLE: function(tab) {
      var vm = this;
      var tabIndex = Number(tab.index);

      if (vm.selectdRecord.length != 0 && vm.selectdRecord[0].qualLevelId == vm.needServiceList2[tabIndex].qualLevelId) {
        this.$refs.treeService.setCheckedKeys(vm.selectdRecord[0].majorQualId);
      } else {
        this.$refs.treeService.setCheckedKeys([]);
      }
      vm.activeLE = tab.index;

      vm.qualLevelId = vm.needServiceList2[tabIndex].qualLevelId

    },
    handleServiceTreeData: function(data, checkState) {
      var vm = this;

      var majorQualRequiresArry = []
      majorQualRequiresObj = {
        qualLevelId: vm.qualLevelId, // 资质等级ID
        majorQualId: checkState.checkedKeys || ''
      }
      vm.selectdRecord = [];
      vm.selectdRecord.push(majorQualRequiresObj);

    },
    setCheckedKeys: function() {
      var vm = this;
      var key = []
      if (vm.selectdRecord) {
        vm.selectdRecord.forEach(function(el, index) {
          if (el.level1 == vm.activeSN && el.leve2 == vm.activeSN + '-' + vm.activeLE) {
            key = el.data;
          }
        })
        try {
          vm.$refs.treeService.setCheckedKeys(key[0]);
        } catch (e) {
          console.log('还没选专业树')
        } finally {

        }
      }
    },
    backToIndex: function() {
      window.parent.location.href = ctx + '/aplanmis-mall/supermarket/main/index.html';

    },
    upload: function(row, type) {
      this.curUpData = row;
      this.uploadTableData = this.curUpData.fileList;
      this.type = type;
    },
    deleteFile: function(row) {
      var _this = this;
      this.uploadTableData.forEach(function(item) {
        if (item == row) {
          _this.uploadTableData.splice(row, 1);
        }
      })
    },
    deleteAttr: function(data, row) {
      var _this = this;

      data.splice(row, 1);

    },
    //下载单个附件
    downLoad: function(data) {
      window.open(ctx + 'supermarket/purchase/mat/att/download?detailIds=' + data.fileId, '_blank')
    },
    // 预览源文件
    preview: function(data) {
      var detailId = data.fileId;
      var flashAttributes = '';
      var tempwindow = window.open(); // 先打开页面
      setTimeout(function() {
        tempwindow.location = ctx + 'supermarket/purchase/mat/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
      }, 1000)
    },
    // 附件上传文件列表变动
    enclosureFileSelectChange: function(file, fileLi) {
      var _this = this;

      // 选择后检验
      // if (!_this.enclosureBeforeUpload(file)) {

      // }
      fileLi.forEach(function(item) {
        if (item.raw) {
          if (_this.type == 'jiben') {
            _this.fileList.push(item.raw);
          } else if (_this.type == 'zige') {
            _this.fileList2.push(item.raw);
          } else {
            _this.fileList3.push(item.raw);
          }
          _this.curUpData.fileList.push(item.raw);

        }
      })
    },
    addlinkman: function() {
      var _this = this;
      if (this.formDataZgxx.serviceId) {
        this.serviceType.forEach(function(item) {
          if (item.serviceId == _this.formDataZgxx.serviceId) {
            _this.addEditManform.unitServiceIds = item.serviceName;
          }
        })
        this.addEditManModalShow = true
      } else {
        this.$message({
          message: '请在资格信息页面选择服务类型',
          type: 'error'
        });
        return;
      }

    },
    savaLinkman: function() {
      this.certigierData.push(this.addEditManform);
      this.addEditManModalShow = false;
      this.addEditManform = {};
      this.$nextTick(function() {
        this.$refs['addEditManform'].clearValidate();
      });
    },
    // 转成formdata格式
    toFormData: function(val) {
      var formData = new FormData();
      for (var i in val) {
        isArray(val[i], i);
      }

      function isArray(array, key) {
        if (array == undefined || typeof array == "function") {
          return false;
        }
        if (typeof array != "object") {
          formData.append(key, array);
        } else if (array instanceof Array) {
          if (array.length == 0) {
            formData.append(`${key}`, "");
          } else {
            for (var i in array) {
              if (typeof(array[i]) == 'string') {
                formData.append(`${key}[${i}]`, array[i]);
              } else {
                for (var j in array[i]) {
                  isArray(array[i][j], `${key}[${i}].${j}`);
                }
              }

            }
          }
        } else {
          var arr = Object.keys(array);
          if (arr.length != 0 && arr.indexOf("uid") == -1) {
            for (var j in array) {
              isArray(array[j], `${key}.${j}`);
            }
          } else {
            formData.append(`${key}`, array);
          }
        }
      }
      return formData;

    },
    // formatNumber: function(n) {
    //   n = n.toString()
    //   return n[1] ? n : '0' + n
    // },
    // formatTime: function(number, format) {

    //   var formateArr = ['Y', 'M', 'D'];
    //   var returnArr = [];

    //   var date = new Date(number);
    //   returnArr.push(date.getFullYear());
    //   returnArr.push(this.formatNumber(date.getMonth() + 1));
    //   returnArr.push(this.formatNumber(date.getDate()));

    //   for (var i in returnArr) {
    //     format = format.replace(formateArr[i], returnArr[i]);
    //   }
    //   return format;
    // },
    toStep2: function() {
      var _this = this;
      _this.$refs['form'].validate(function(valid) {
        _this.$refs['form2'].validate(function(valid) {
          if (valid) {
            if (_this.tableData[0].fileList.length == 0 || _this.tableData[1].fileList.length == 0) {
              _this.$message({
                message: '请上传完完整两份附件内容',
                type: 'error'
              });
              return;
            } else {
              _this.step = '2';

            }
          } else {
            _this.$message({
              message: '请先填写完整本页信息',
              type: 'error'
            });
            return;
          }
        })
      })
    },
    toStep3: function() {
      var _this = this;
      _this.$refs['formZgxx'].validate(function(valid) {
        if (valid) {
          if (_this.tableData2[0].fileList.length == 0) {
            _this.$message({
              message: '请上传完完整附件内容',
              type: 'error'
            });
            return;
          } else {
            if (_this.selectdRecord[0].majorQualId.length == 0) {
              _this.$message({
                message: '请选择资格专业证书',
                type: 'error'
              });
              return;
            } else {
              _this.step = '3';
            }
          }
        } else {
          _this.$message({
            message: '请先填写完整本页信息',
            type: 'error'
          });
          return;
        }

      })
    },
    toStep4: function() {
      var _this = this;
      if (_this.certigierData.length != 0) {
        _this.step = '4';
      } else {
        _this.$message({
          message: '请添加业务授权人',
          type: 'error'
        });
        return;
      }


    },
    // 保存
    saveForm: function() {
      var a = this.tableData;
      var _that = this;
      _that.$refs['formPersonInfo'].validate(function(valid) {
        if (valid) {
          if (_that.matData[0].fileList.length == 0) {
            _that.$message({
              message: '请上传完完整附件内容',
              type: 'error'
            });
            return;
          } else {
            if (_that.jobPeopleDetailObj.certinstBVos.length == 0) {
              _that.$message({
                message: '请添加证书',
                type: 'error'
              });
              return;
            } else {
              var certinstId = [];
              _that.jobPeopleDetailObj.certinstBVos.forEach(function(item) {
                certinstId.push(item.certinstId);
              })
              var param = {
                // 第一页数据
                "unitInfo.applicant": _that.formData.applicant || '',
                "unitInfo.unitNature": _that.formData.unitNature || '',
                "unitInfo.idtype": _that.formData.idtype || '',
                "unitInfo.unifiedSocialCreditCode": _that.formData.unifiedSocialCreditCode || '',
                "unitInfo.idrepresentative": _that.formData.idrepresentative || '',
                "unitInfo.idno": _that.formData.idno || '',
                "unitInfo.registeredCapital": _that.formData.registeredCapital || '',
                "unitInfo.managementScope": _that.formData.managementScope || '',
                "unitInfo.applicantDistrict": _that.formData.applicantDistrict[_that.formData.applicantDistrict.length - 1] || '',
                "unitInfo.applicantDetailSite": _that.formData.applicantDetailSite || '',
                "unitInfo.registerAuthority": _that.formData.registerAuthority || '',
                "contactManInfo.linkmanName": _that.formData.linkmanName || '',
                "contactManInfo.linkmanMail": _that.formData.linkmanMail || '',
                "contactManInfo.linkmanMobilePhone": _that.formData.linkmanMobilePhone || '',
                "contactManInfo.linkmanOfficePhon": _that.formData.linkmanOfficePhon || '',
                "contactManInfo.linkmanFax": _that.formData.linkmanFax || '',

                // 第二页数据
                'serviceAndQualVo.aeaImUnitService.serviceId': _that.formDataZgxx.serviceId || '',
                'serviceAndQualVo.aeaImUnitService.serviceContent': _that.formDataZgxx.serviceContent || '',
                'serviceAndQualVo.aeaImUnitService.feeReference': _that.formDataZgxx.feeReference || '',
                'serviceAndQualVo.aeaHiCertinstBVo.qualLevelId': _that.selectdRecord[0].qualLevelId,
                'serviceAndQualVo.aeaHiCertinstBVo.majorId': _that.selectdRecord[0].majorQualId.join(','),
                'serviceAndQualVo.aeaHiCertinstBVo.serviceId': _that.formDataZgxx.serviceId || '',
                'serviceAndQualVo.aeaHiCertinstBVo.certId': _that.formDataZgxx.certId || '',
                'serviceAndQualVo.aeaHiCertinstBVo.serviceContent': _that.formDataZgxx.serviceContent || '',
                'serviceAndQualVo.aeaHiCertinstBVo.feeReference': _that.formDataZgxx.feeReference || '',
                'serviceAndQualVo.aeaHiCertinstBVo.certinstCode': _that.formDataZgxx.certinstCode || '',
                'serviceAndQualVo.aeaHiCertinstBVo.termStart': _that.formDataZgxx.termStart || '',
                'serviceAndQualVo.aeaHiCertinstBVo.termEnd': _that.formDataZgxx.termEnd || '',
                'serviceAndQualVo.aeaHiCertinstBVo.managementScope': _that.formDataZgxx.managementScope || '',

                //第三页数据
                authorManList: _that.certigierData,


                //第四页数据
                practiceManInfo: {
                  linkmanName: _that.formPersonInfo.linkmanName || '',
                  linkmanCertNo: _that.formPersonInfo.linkmanCertNo || '',
                  practiseDate: _that.formPersonInfo.practiseDate || '',
                  serviceId: _that.formPersonInfo.serviceId || '',
                  certinstId: certinstId
                },
                // 'unitInfo.unitInfoId': '05fc27ef-a9bc-4ba8-97fd-e1fb84526437'

              };
              var formData = _that.toFormData(param);

              // console.log(param)
              // var formData = new FormData();
              // for (var k in param) {
              //   formData.append(k, param[k])
              // }

              _that.fileList.forEach(function(item) {
                formData.append('unitFile', item)
              })
              _that.fileList2.forEach(function(item) {
                formData.append('file', item)
              })
              _that.fileList3.forEach(function(item) {
                formData.append('practiceManFile', item)
              })

              // _that.$refs['addEditManform'].validate(function(valid) {
              // if (valid) {
              _that.loading = true;
              $.ajax({
                url: ctx + 'supermarket/agentRegister/saveRegisterForm',
                type: 'post',
                data: formData,
                dataType: 'json',
                contentType: false,
                processData: false,
                success: function(res) {
                  if (res.success) {
                    _that.$message({
                      message: '保存成功',
                      type: 'success'
                    });
                  }
                  _that.password = res.content.password;
                  _that.loginName = res.content.loginName;
                },
                error: function(msg) {
                  _that.$message({
                    message: '保存失败',
                    type: 'error'
                  });
                },
              })

            }
          }
        } else {
          _that.$message({
            message: '请先填写完整本页基本信息',
            type: 'error'
          });
          return;
        }
      })


    },
    // 证书详情相关
    // 添加证书
    addCertificate: function() {
      var ts = this;
      if (!this.formPersonInfo.serviceId) {
        this.$message({
          message: '请在先选择服务类型',
          type: 'error'
        });
        return;
      }
      this.addCertShow = true;
      ts.isPeoplePandelState = 2;
      ts.certificateEditFlag = false; //当前为添加证书
      ts.certificateDetailObj = this.CERTIFICATE_DETAIL_OBJ; //新增的的证书详情赋初始值
      ts.enclosureFileListArr = [];
      // ts.backToTop(); //body位置默认为返回顶部

      this.certificateDetailObj = new Object;
      // this.getAeaCertInfo();
      this.listAeaImServiceQual();
      this.getMajorTreeByQualId();
    },
    // 编辑证书
    editCertificate: function(idx, row) {
      var ts = this;
      this.addCertShow = true;

      ts.isPeoplePandelState = 2;
      ts.certificateEditFlag = true; //当前为编辑证书
      ts.certificateDetailObj = row; //编辑的当前行的证书详情赋值
      ts.curEditCertificateIndex = idx; //编辑的当前行索引赋值

      // ts.getAeaCertInfo(); // 获取证书类型的下拉options
      ts.listAeaImServiceQual(); //获取资质列表
      ts.getMajorTreeByQualId(); //加载所属专业树的数据
      // ts.backToTop(); //body位置默认为返回顶部
    },


    // 所属专业树-选中当前资质
    selectTreeForQuality: function(quality, idx) {
      var ts = this;
      ts.curSelectQualiTreeData = quality.majorTree; //当前选中的资质下树数据赋值
      ts.curSelectQualiRankDataOptions = quality.levelList; //当前选中的资质下等级的options数据赋值

      // 所属专业树获取成功后展开树并初始化
      ts.initMajorTree();
      // 所属专业树获取成功后-编辑时，对选中的树节点回显   certificateDetailObj
      if (ts.certificateDetailObj.majorId && ts.certificateDetailObj.majorId !== null) {
        ts.$refs.tree.setCheckedKeys(ts.certificateDetailObj.majorId);
      }
      // 所属专业树选中资质后的当前按钮变化
      var _curBtn = $('.major-tree-box').find('.quality-btn');
      _curBtn.eq(idx).addClass('el-button--primary');
      _curBtn.eq(idx).siblings().removeClass('el-button--primary');
    },
    // 获取所属专业人员树当中的选中节点数据
    getTreeCheck: function() {
      console.log(this.$refs.tree.getCheckedKeys());
      return this.$refs.tree.getCheckedKeys();
    },
    // 证书详情-所属专业树默认展开
    initMajorTree: function() {
      var ts = this;
      ts.$nextTick(function() {
        for (var i = 0; i < ts.$refs.tree.store._getAllNodes().length; i++) {
          ts.$refs.tree.store._getAllNodes()[i].expanded = true;
        }
      });
    },


    // 附件相关
    // 附件上传--before
    enclosureBeforeUpload: function(file) {
      var ts = this,
        file = file;
      var fileMaxSize = 1024 * 1024 * 10; // 10MB为最大限制
      // 文件类型
      // 检查文件类型
      var index = file.name.lastIndexOf(".");
      var ext = file.name.substr(index + 1);
      if (['exe', 'sh', 'bat', 'com', 'dll'].indexOf(ext) !== -1) {
        ts.$message({
          message: '请上传非.exe,.sh,.bat,.com,.dll文件',
        });
        return false;
      };
      // 检查文件大小
      if (file.size > fileMaxSize) {
        ts.$message({
          message: '请上传大小在10M以内的文件',
        });
        return false;
      };
      return true;
    },
    // 附件上传文件列表变动
    enclosureFileSelectChange2: function(file, fileList) {
      console.log(fileList)
      var ts = this;
      ts.enclosureFileListArr = [];
      fileList.forEach(function(item) {
        ts.enclosureFileListArr.push(item.raw)
      })
    },
    // 附件上传文件列表移除某一项
    enclosureFileSelectRemove: function(file, fileList) {
      console.log(fileList)
      var ts = this;
      ts.enclosureFileListArr = [];
      fileList.forEach(function(item) {
        ts.enclosureFileListArr.push(item.raw)
      })
    },

    // 附件列表中删除掉一条数据
    delOneEnclosure: function(row) {
      var ts = this,
        _delData = {};
      confirmMsg('', '您确定删除当前附件吗？', function() {
        _delData.detailId = row.detailId;
        // test
        // ts.delOneEnclosureFrontSelf( _delData.detailId);
        // return;

        ts.mloading = true;
        request('', {
          url: ctx + 'supermarket/certinst/deleteAeaHiCertinstByDetailId.do',
          type: 'get',
          data: _delData
        }, function(res) {
          ts.mloading = false;
          if (res.success) {
            ts.delOneEnclosureFrontSelf(_delData.detailId);
            ts.$message({
              message: '删除成功！',
              type: 'success'
            });
          } else {
            ts.$message({
              message: '删除失败',
              type: 'error'
            });
          }
        }, function(msg) {
          ts.mloading = false;
          ts.$message({
            message: '删除失败',
            type: 'error'
          });
        });
      })
    },
    // 附件列表中删除接口调用后，成功后前端自己删除掉一条数据
    delOneEnclosureFrontSelf: function(enclosureId) {
      var ts = this;
      var _allEnclosure = ts.certificateDetailObj.certinstDetail; //所有的附件list
      var _idx = -1; //当前删除的附件索引
      for (var i = 0; i < _allEnclosure.length; i++) {
        var _tmpEnclosureId = _allEnclosure[i].fileId;
        if (enclosureId == _tmpEnclosureId) {
          _idx = i;
          break;
        }
      }
      console.log(_idx)
      if (_allEnclosure.length && _idx > -1) {
        ts.certificateDetailObj.certinstDetail.splice(_idx, 1);
      }
    },
    // 删除一条证书数据
    delOneCertificate: function(row) {
      var ts = this,
        _delData = {};

      confirmMsg('', '您确定删除当前证书吗？', function() {

        _delData.certinstId = row.certinstId;


        ts.delOneCertificateFrontSelf(_delData.certinstId);

      })
    },
    // 删除一条证书数据-接口成功后，前端自己删除对应证书
    delOneCertificateFrontSelf: function(certificateId) {
      var ts = this;

      var _allCertificate = ts.jobPeopleDetailObj.certinstBVos; //所有的证书List
      var _idx = -1; //当前删除的附件索引
      for (var i = 0; i < _allCertificate.length; i++) {
        var _tmpCertificateId = _allCertificate[i].certinstId;
        if (_tmpCertificateId == certificateId) {
          _idx = i;
          break;
        }
      }
      console.log(_idx);
      if (_allCertificate.length && _idx > -1) {
        ts.jobPeopleDetailObj.certinstBVos.splice(_idx, 1);
      }
    },
    // 点击dialog查看某一条证书列表的详情
    showDetailFormItem: function(item) {
      var ts = this;
      window.open(ctx + 'file/showFile.do?detailId=' + item.fileId);
    },

    //获取证书类型
    getAeaCertInfo: function() {
      var ts = this;
      ts.mloading = true;
      request('', {
        url: ctx + 'supermarket/cert/getAeaCertInfo.do',
        type: 'get',
        data: {}
      }, function(res) {
        ts.mloading = false;
        ts.addCertificateOptions = res;
        ts.certTypeZige = res;
      }, function(msg) {
        ts.mloading = false;
        ts.$message({
          message: '加载失败',
          type: 'error'
        });
      });
    },
    //获取资质列表
    listAeaImServiceQual: function() {
      var ts = this;
      ts.mloading = true;
      request('', {
        url: ctx + 'supermarket/certinst/listAeaImServiceQual.do',
        type: 'get',
        data: {
          serviceId: this.formPersonInfo.serviceId
        }
      }, function(res) {
        ts.mloading = false;
        ts.serviceQual = res;
      }, function(msg) {
        ts.mloading = false;
        ts.$message({
          message: '加载失败',
          type: 'error'
        });
      });
    },
    //获取资质证书树
    getMajorTreeByQualId: function() {
      var ts = this;
      ts.mloading = true;
      request('', {
        url: ctx + 'aea/supermarket/serviceMatter/getMajorTreeByServiceId.do',
        type: 'post',
        data: {
          serviceId: this.formPersonInfo.serviceId
        }
      }, function(res) {
        ts.mloading = false;
        ts.allSubordinateMajorData = res;
        // 初始化树-默认选中第一个资质
        setTimeout(function() {
          $('.major-tree-box').find('.quality-btn').eq(0).trigger(
            'click');
        }, 0)
      }, function(msg) {
        ts.mloading = false;
        ts.$message({
          message: '加载失败',
          type: 'error'
        });
      });
    },
    // 证书详情的保存操作
    saveCertificateDetail: function() {
      var ts = this,
        _canSaveFlag = true,
        _saveData = {};
      ts.certificateDetailObj.majorId = ts.getTreeCheck(); //获取所属专业树当中选中的专业id

      _saveData = {
        certId: '', //证书类型
        certinstCode: '', //证书编码
        certinstName: '', //证书名称
        managementScope: '', //业务范围
        termStart: '', //有效期限-start
        termEnd: '', //有效期限-end
        qualId: '', //资质
        majorId: [], //所属专业
        qualLevelId: '', //等级
        // bscAttFileAndDirs: [], //附件列表
      };
      for (var k in _saveData) {
        _saveData[k] = ts.certificateDetailObj[k];
      }

      if (_saveData.certId === null || !_saveData.certId.length) {
        return ts.$message({
          message: '请选择证书类型！'
        })
      }
      if (_saveData.qualId === null || !_saveData.qualId.length) {
        return ts.$message({
          message: '请选择资质！'
        })
      }
      if (_saveData.certinstCode === null || !_saveData.certinstCode.length) {
        return ts.$message({
          message: '请填写证件编码！'
        })
      }
      if (_saveData.certinstName === null || !_saveData.certinstName.length) {
        return ts.$message({
          message: '请填写证件名称！'
        })
      }
      if (_saveData.managementScope === null || !_saveData.managementScope.length) {
        return ts.$message({
          message: '请填写业务范围！'
        })
      }
      //new Date(Date.parse(str.replace(/-/g,"/")));
      /*if (_saveData.termStart === null || !_saveData.termStart.length || _saveData.termEnd ===
          null || !_saveData.termEnd.length) {
        return ts.$message({
          message: '请选择有效期限！'
        })
      } else {
        if (_saveData.termEnd < _saveData.termStart) {
          return ts.$message({
            message: '有效期限起始时间不能大于结束时间！'
          })
        }
      }*/
      if (_saveData.majorId === null || !_saveData.majorId.length) {
        return ts.$message({
          message: '请选择所属专业！'
        })
      }
      if (_saveData.qualLevelId !== null && !_saveData.qualLevelId.length) {
        return ts.$message({
          message: '请选择等级！'
        })
      }
      // if (_saveData.bscAttFileAndDirs !== null && !_saveData.bscAttFileAndDirs.length && ts
      //     .certificateEditFlag) {
      //     return ts.$message({
      //         message: '请选择上传文件！'
      //     })
      // }

      // 新增证书并且没有打算选择上传文件
      if (!ts.enclosureFileListArr.length && !ts.certificateEditFlag) {
        return ts.$message({
          message: '请上传文件附件！'
        })
      }
      // 校验文件是否能上传
      if (ts.enclosureFileListArr.length) {
        var _fileCanUpload = true;
        for (var i = 0; i < ts.enclosureFileListArr.length; i++) {
          if (!ts.enclosureBeforeUpload(ts.enclosureFileListArr[i])) {
            _fileCanUpload = false;
            break;
          }
        }
        if (!_fileCanUpload) return ts.$message({
          message: '请上传非.exe,.sh,.bat,.com,.dll文件'
        });
      }
      // 如果是证书实例编辑,带上当前编辑证书的certinstId
      if (ts.certificateEditFlag) {
        _saveData.certinstId = ts.certificateDetailObj.certinstId;
      }
      // 带上unitId
      // _saveData.unitInfoId = this.unitInfo.unitId;

      // return;
      var formData = new FormData();
      for (var k in _saveData) {
        formData.append(k, _saveData[k])
      }

      ts.enclosureFileListArr.forEach(function(item) {
        formData.append('file', item)
      })
      // return;

      ts.mloading = true;
      $.ajax({
        url: ctx + 'supermarket/certinst/uploadAeaHiCertinstFile.do',
        type: 'post',
        data: formData,
        contentType: false,
        processData: false,
        // mimeType: "multipart/form-data",
        success: function(res) {
          ts.mloading = false;
          if (res.success) {
            // 保存成功后将当前保存的证书实例添加到事项详情的证书列表中，若为编辑保存，则替换
            if (!ts.certificateEditFlag) {
              ts.jobPeopleDetailObj.certinstBVos.push(res.content)
            } else {
              ts.jobPeopleDetailObj.certinstBVos.splice(ts
                .curEditCertificateIndex, res.content)
            }
            ts.addCertShow = false;
            // 保存成功后将需要上传的附件文件列表清空
            ts.enclosureFileListArr = [];
            ts.certList = [];

            ts.$message({
              message: '保存成功',
              type: 'success'
            });
          } else {
            ts.$message({
              message: '保存失败',
              type: 'error'
            });
          }
        },
        error: function(msg) {
          ts.mloading = false;
          ts.$message({
            message: '保存失败',
            type: 'error'
          });
        },
      })
    },
    // 解决sleect选不中值问题
    forceUpdate: function() {
      this.$forceUpdate()
    },
  },
  filters: {
    certTypeFun: function(value) {
      for (var i = 0; i < vm.addCertificateOptions.length; i++) {
        if (vm.addCertificateOptions[i].certId == value) {
          return vm.addCertificateOptions[i].certName;
        }
      }
    }
  }
});