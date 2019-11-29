/*
 * @Author: ZL
 * @Date:   2019/05/29
 * @Last Modified by:   ZL
 * @Last Modified time: $ $
 */
var vm = new Vue({
  el: '#app',
  data: function() {
    return {
      step: '2',
      dialogTitie: '',
      pageLoading: false,
      formData: {},
      formData2: {},
      formDatawtr: {},
      activeNames: ['1', '2', '3'],
      activeNames2: ['1', '2', '3'],
      activeNames3: ['1'],
      activeNames4: ['1', '2', '3'],
      loginName: '',
      password: '',
      tableData: [{
        name: '统一社会信用代码的营业执照（属社会组织的提供社会团体法人登记证或民办非企业单位法人登记证，属事业单位的提供法人登记证）',
        bb: '是',
        fileList: []
      }, {
        name: '法定代表人身份证（原件正反面扫描件）',
        bb: '是',
        fileList: []
      }],
      tableData2: [{
        name: '采购委托人身份证（原件正反面扫描件）',
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
        name: '11',
        aa: '22',
        bb: '33'
      }],
      formPersonInfo: {},
      value: [],
      districtList: [],
      IdList: [],
      moneyList: [],
      projTypeList: [],
      typeList: [],
      serviceType: [],
      recordList: [],
      matTypeList: [],
      personList: [],
      IdType: [],
      idCard: '1',
      getFileListWindowFlag: false,
      searchFileListfilter: '',
      tabBind: 'one',
      matDialog: false,
      shareFileList: [{
        attName1: '广东凯信工程造价咨询有限公司',
        attName2: '营业执照附件.jpg',
        attName3: '2019-02-16 02:11',
      }],
      certigierData: [{
        name: "王权利",
        aa: '123847847',
        bb: '244871878174871847',
        cc: '测绘',
        dd: "是"
      }],
      matType: '',
      options: [],
      props: {
        value: 'regionNum',
        label: 'regionName'
      },
      curUpData: [],
      uploadTableData: [],
      loadingFileWin: false,
      showUploadWindowTitle: '附件',
      showUploadWindowFlag: false,
      fileList: [],
      fileList2: [],
      uploadTableData: [],
      type: '',
      rules: {
        applicant: [
          { required: true, message: '请输入单位名称' },
        ],
        projTypeList: [
          { required: true, message: '请选择机构代码类型' },
        ],
        unifiedSocialCreditCode: [
          { required: true, message: '请输入统一社会信用码' },
        ],
        applicantDistrict: [
          { required: true, message: '请选择注册地行政区划' },
        ],
        regionalism: [
          { required: true, message: '请输入法定代表人' },
        ],
        idno: [
          { required: true, message: '请输入法人代表证件号' },
        ],
        applicantDetailSite: [
          { required: true, message: '请输入注册地址' },
        ],
        managementScope: [
          { required: true, message: '请输入经营或业务范围' },
        ],
        registeredCapital: [
          { required: true, message: '请输入注册资本' },
        ],
      },
      rules2: {
        linkmanName: [
          { required: true, message: '请输入联系人' },
        ],
        linkmanMail: [
          { required: true, message: '请输入电子邮箱' },
        ],
        linkmanMobilePhone: [
          { required: true, message: '请输入移动电话' },
        ]
      },
      ruleswtr: {
        projName: [
          { required: true, message: '请输入所属科室' },
        ],
        linkmanName: [
          { required: true, message: '请输入采购人姓名' },
        ],
        linkmanMobilePhone: [
          { required: true, message: '请输入移动电话' },
        ],
        linkmanCertNo: [
          { required: true, message: '请输入采购委托人证件号' },
        ],
        linkmanMail: [
          { required: true, message: '请输入邮箱' },
        ]
      }
    }
  },
  mounted: function() {
    this.getRegion();
    this.getDicCode();
  },
  methods: {
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
    backToIndex: function() {
      window.parent.location.href = ctx + '/aplanmis-mall/supermarket/main/index.html';

    },
    upload: function(row, type) {
      this.curUpData = row;
      this.uploadTableData = this.curUpData.fileList;
      this.type = type;
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
          } else if (_this.type == 'weituoren') {
            _this.fileList2.push(item.raw);
          }
          _this.curUpData.fileList.push(item.raw);

        }
      })
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
    // 保存
    saveForm: function() {
      var a = this.tableData;
      var _that = this;
      _that.$refs['formwtr'].validate(function(valid) {
        if (valid) {
          if (_that.tableData2[0].fileList.length == 0) {
            _that.$message({
              message: '请上传完完整附件内容',
              type: 'error'
            });
            return;
          } else {
            var param = {
              //第一页数据
              unitInfo: {
                applicant: _that.formData.applicant || '',
                idtype: _that.formData.idtype || '',
                unifiedSocialCreditCode: _that.formData.unifiedSocialCreditCode || '',
                applicantDistrict: _that.formData.applicantDistrict[_that.formData.applicantDistrict.length - 1] || '',
                regionalism: _that.formData.regionalism || '',
                idno: _that.formData.idno || '',
                applicantDetailSite: _that.formData.applicantDetailSite || '',
                managementScope: _that.formData.managementScope || '',
                registeredCapital: _that.formData.registeredCapital || '',

              },
              contactManInfo: {
                linkmanName: _that.formData2.linkmanName || '', //联系人姓名
                linkmanMail: _that.formData2.linkmanMail || '', //电子邮箱
                linkmanMobilePhone: _that.formData2.linkmanMobilePhone || '', //移动电话
                linkmanOfficePhon: _that.formData2.linkmanOfficePhon || '', //固定电话
                linkmanFax: _that.formData2.linkmanFax || '', //传真
              },
              authorManInfo: {
                linkmanName: _that.formDatawtr.linkmanName || '', //姓名
                linkmanMobilePhone: _that.formDatawtr.linkmanMobilePhone || '', //移动电话
                linkmanCertNo: _that.formDatawtr.linkmanCertNo || '', //证件号
                linkmanMail: _that.formDatawtr.linkmanMail || '', //证件号
              }


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
              formData.append('authorManFile', item)
            })


            // _that.$refs['addEditManform'].validate(function(valid) {
            // if (valid) {
            _that.loading = true;
            $.ajax({
              url: ctx + 'supermarket/ownerRegister/saveRegisterForm',
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
                _that.step = '3';
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
        } else {
          _that.$message({
            message: '请先填写完整本页信息',
            type: 'error'
          });
          return;
        }
      })


    },
    // 解决sleect选不中值问题
    forceUpdate: function() {
      this.$forceUpdate()
    },
  }
});