var vm = new Vue({
  el: "#approvalDocument",
  data: {
    ctx: ctx,
    iteminstProcessinstId: null,// 事项子流程实例ID
    iteminstId: getUrlParam('iteminstId'),
    applyinstId: getUrlParam('masterEntityKey'),
    taskId: getUrlParam('taskId'),
    processInstanceId: getUrlParam('processInstanceId'),
    isApprover: getUrlParam('isApprover'),
    
    // isApprover: 1, // 是否部门审批人员
    // iteminstId: 'ff8cc721-0b19-40ec-b126-d602bc17a209', // 部门人员  1
    // applyinstId: 'a5008bde-a5dd-456a-813a-22a90f5da7de', // 窗口人员  0
    // taskId: '4910196',
    
    isSeries: '0',
    tableloading: false,
    styleObj: {
      background: '#f5f5f5',
    },
    approvalList: [],
    officeDocList: [],
    approvalDialogTitle: '新建批复',
    showApprovalDialog: false,
    officialDocForm: {
      applyinstId: '',
      iteminstId: '',
      matinstId: '',
      officialDocNo: '',
      officialDocTitle: '',
      officialDocDueDate: '',
      officialDocPublishDate: '',
      paperCount: '',
      attCount: '',
      taskId: '',
      attId: '',
      docCount: '',
      attFiles: [],
    },
    EditFormRules: {
      matId: [
        {required: true, message: '请选择类型', trigger: 'change'}
      ],
      officialDocTitle: [
        {required: true, message: '请输入名称', trigger: 'change'}
      ],
      officialDocNo: [
        {required: true, message: '请输入文号', trigger: 'change'}
      ],
      officialDocDueDate: [
        {required: true, message: '请选择文件有效期', trigger: 'change'}
      ],
      officialDocPublishDate: [
        {required: true, message: '请选择文件生效日期', trigger: 'change'}
      ]
    },
    CountType: 'paper',
    approveListMultipleSelection: [],
    isEditApprocal: false,
    matinstId: '', //批文批复id
    fileList: [],//文件列表
    uploadTableData: [],
    itemOfficeMatList: [],//当前事项对应的批文批复定义列表
    selectMatName: '',//批文批复 选择的所属材料
    canSelect: true,//编辑时是批文批复 是否可以选择所属材料
    preUploadFiles: [],
    binded: false,
    cacheMatType: [],
    onlyLook: true,
    pageLoading: false,
    licenseList: [],
    licenseDiaVisible: false,
    licenseDiaLoading: false,
    licenseFormData: {
      projScale: '',
      memo: '',
    },
    licenseTypeList: [],
    cacheTypeList: [],
    orgList: [],
    licenseFormRules: {
      certId: [{required: true, message: '请选择类型', trigger: 'change'}],
      certinstCode: [{required: true, message: '请输入证照编码', trigger: 'blur'}],
      issueDate: [{required: true, message: '请选择发证日期', trigger: 'blur'}],
      projScale: [{required: true, message: '请输入项目规模', trigger: 'blur'}],
    },
    isAddLicense: true,
    isLoadedType: false,
    fileLoading: false,
  },
  created: function () {
    //this.initData();
  },
  mounted: function () {
    var vm = this;
    vm.getItemInOfficeMat();
    
    vm.getLicenseData(function(){
      vm.initData();
    });
    
    $('body').on('click', '.meterial-col', function () {
      var _this = $(this);
      _this.parent().find('.el-icon.el-icon-arrow-right').trigger('click');
    });
  },
  methods: {
    // 证件类型下拉handler
    certSelectChange: function (val) {
      var vm = this;
      this.licenseTypeList.forEach(function (u) {
        if (u.certId == val) {
          vm.licenseFormData.certinstName = u.certName;
        }
      })
    },
    // 关闭制证弹窗
    closeLicenseForm: function () {
      this.$refs['licenseFormRef'].resetFields();
      this.licenseFormData.certinstName = '';
      this.licenseFormData.issueOrgId = '';
      this.licenseFormData.memo = '';
    },
    // 点击新建制证
    addLicense: function () {
      var vm = this;
      vm.filterType();
      if (vm.licenseTypeList.length == 0) {
        return vm.$message('当前事项没有定义的证件类型或证件模板');
      }
      vm.licenseDiaVisible = true;
      vm.isAddLicense = true;
      delete vm.licenseFormData.certinstId;
    },
    // icon 字体文件颜色
    getIconColor: function (type) {
      return __STATIC.getIconColor(type || "DEFAULT");
    },
    // 点击编辑制证
    editLicense: function (row) {
      var vm = this;
      vm.licenseDiaVisible = true;
      vm.isAddLicense = false;
      vm.licenseFormData = $.extend({}, row);
      vm.licenseFormData.createTime = vm.$options.filters.formatOnlyDate(row.createTime);
      var tmpArr = [];
      vm.cacheTypeList.forEach(function (u) {
        var flag = true;
        vm.licenseList.forEach(function (uu) {
          if (uu.certId == u.certId && row.certId != u.certId) {
            flag = false;
          }
        });
        flag && tmpArr.push(u);
      })
      vm.licenseTypeList = tmpArr;
    },
    // 点击预览制证
    preLicense: function (row) {
      parent.vm.openPrintLicenceDialog(row);
    },
    // 点击删除制证
    deleteLicense: function (row) {
      var vm = this;
      this.$confirm('此操作将永久删除该证照, 是否继续?', '删除证照', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(function (obj) {
        ensureDelete();
      }).catch(function () {
      });
      
      function ensureDelete() {
        vm.pageLoading = true;
        request('', {
          url: ctx + 'rest/approve/certinst/batch/delete',
          type: 'post',
          data: {certinstIds: row.certinstId,},
        }, function (res) {
          vm.pageLoading = false;
          if (res.success) {
            vm.$message.success('删除成功');
            vm.getLicenseData();
          } else {
            vm.$message.error(res.message || '删除失败')
          }
        }, function (msg) {
          vm.pageLoading = false;
          vm.$message.error('服务请求失败');
        });
      }
    },
    // 保存制证信息
    saveLisence: function () {
      var vm = this;
      vm.$refs['licenseFormRef'].validate(function (valid) {
        if (valid) {
          save();
        } else {
          return false;
        }
      });
      
      function save() {
        vm.licenseFormData.applyinstId = vm.applyinstId;
        vm.licenseFormData.iteminstId = vm.iteminstId;
        vm.licenseDiaLoading = true;
        request('', {
          url: ctx + 'rest/approve/saveAeaCertinst',
          type: 'post',
          data: vm.licenseFormData,
        }, function (res) {
          vm.licenseDiaLoading = false;
          if (res.success) {
            //
            vm.licenseDiaVisible = false;
            vm.$message.success('保存成功');
            vm.getLicenseData(); // 刷新表格数据
          } else {
            vm.$message.error(res.message);
          }
        }, function () {
          vm.licenseDiaLoading = false;
          vm.$message.error('保存失败')
        })
      }
    },
    getLicenseData: function (callback) {
      var vm = this;
      request('', {
        url: ctx + 'rest/approve/getCertinstListByApplyinstIdAndIteminstId',
        type: 'get',
        data: {
          iteminstId: vm.iteminstId,
          applyinstId: vm.applyinstId,
        },
      }, function (res) {
        if (res.success) {
          vm.licenseList = res.content || [];
          // 加载证照类型数据
          vm.getLicenseType(callback);
        } else {
          vm.$message.error(res.message || '加载制证列表失败');
        }
      }, function () {
        vm.$message.error('加载制证列表失败')
      })
    },
    getLicenseType: function (callback) {
      var vm = this;
      if (vm.isLoadedType) {
        return vm.filterType();
      }
      request('', {
        url: ctx + 'rest/approve/getItemOutputCert',
        type: 'get',
        data: {
          iteminstId: vm.iteminstId,
          applyinstId: vm.applyinstId,
        },
      }, function (res) {
        if (res.success) {
          //
          vm.licenseTypeList = res.content.certList || [];
          vm.orgList = res.content.opuOmOrgList || [];
          vm.licenseFormData.projScale = res.content.projScale || '';
          vm.cacheTypeList = [].concat(vm.licenseTypeList);
          vm.isLoadedType = true;
          vm.filterType();
          typeof callback === 'function' && callback()
        } else {
          vm.$message.error(res.message)
        }
      }, function () {
        vm.$message.error('获取证照类型失败')
      });
    },
    filterType: function () {
      var tmpArr = [];
      vm.cacheTypeList.forEach(function (u) {
        var flag = true;
        vm.licenseList.forEach(function (uu) {
          if (uu.certId == u.certId) {
            flag = false;
          }
        });
        flag && tmpArr.push(u);
      })
      vm.licenseTypeList = tmpArr;
    },
    addEditDialogOpen: function () {
      if (this.binded) return;
      this.binded = true;
      vm.bindFileInputChange();
    },
    bindFileInputChange: function () {
      // debugger
      var vm = this;
      setTimeout(function () {
        $('.dia-upload-input').change(function (e) {
          var files = vm.$refs.uploadInput.files;
          var timeStamp = new Date().getTime();
          files[0].timestamp = timeStamp;
          vm.preUploadFiles.push(files[0]);
          if (!vm.officialDocForm.attFiles) {
            vm.officialDocForm.attFiles = [];
          }
          vm.officialDocForm.attFiles.push({
            fileName: files[0].name,
            updateTime: timeStamp,
            timestamp: timeStamp,
             createrName :currentUserName
          });
          return null;
          var formData = new FormData()
          formData.append('files', files[0]);
          formData.append('tableName', tableName);
          formData.append('pkName', tableName);
          formData.append('recordId', vm.masterEntityKey);
          formData.append('dirId', vm.selectedNodeId);
          vm.fileLoading = true;
          axios.defaults.withCredentials = true;
          axios.post(ctx + 'rest/approve/att/file/upload', formData).then(function (res) {
            if (res.data && res.data.success) {
              // 重新加载节点
              vm.getAttachment(_this.selectedNodeId);
            } else {
              vm.$message.error(res.message);
            }
          }).catch(function (e) {
            vm.$message.error('文件上传失败');
          }).finally(function () {
            vm.fileLoading = false;
          })
        });
      }, 10)
    },
    initData: function () {
      var vm = this;
      // vm.isApprover = '1';
      /*vm.taskId = parent.app.taskId // 任务ID
      vm.applyinstId = parent.app.masterEntityKey //申报实例id
      vm.iteminstId = parent.app.busRecordId;//串联才会有值，并联为空  事项实例id
      vm.isApprover = parent.app.isApprover;
      if (!vm.iteminstId || vm.iteminstId == 'undefined') {
        vm.iteminstId = parent.app.iteminstId;
      }*/
      if (vm.iteminstId == '' || vm.iteminstId == 'undefined' || vm.iteminstId == 'null') {
        vm.getIteminstIdByTaskId();
      } else {
        this.getmainTbaleList() // 获取批文批复列表
      }
      
    },
    getIteminstIdByTaskId: function () {
      var vm = this;
      var url = ctx + 'rest/approve/basic/getIteminstId?taskId=' + vm.taskId;
      request('', {
        url: url,
        type: 'get',
      }, function (res) {
        // console.log('批文批复列表', res);
        if (res.content) {
          vm.iteminstId = res.content.iteminstId;
          vm.iteminstProcessinstId = res.content.procinstId;
        }
        // vm.isApprover = (vm.iteminstProcessinstId != null && vm.iteminstProcessinstId == vm.processInstanceId) ? '1' : '0';
        vm.getmainTbaleList() // 获取批文批复列表
      }, function () {
        vm.tableloading = false;
      });
      
    },
    /**
     * 并联申报批文批复列表
     */
    getmainTbaleList: function () {
      var vm = this;
      var statesUrl;
      var params;
      if (vm.isSeries == '1') {
        // statesUrl = ctx + 'rest/approve/docs/single'; // 串联
        statesUrl = ctx + 'rest/approve/docs/list'; // 串联
        params = {
          iteminstId: vm.iteminstId
        }
      } else {
        // statesUrl = ctx + 'rest/approve/docs/parallel';// 并联
        statesUrl = ctx + 'rest/approve/docs/list';// 并联
        params = {
          applyinstId: vm.applyinstId,
          isApprover: vm.isApprover
        }
      }
      this.pageLoading = true;
      request('', {
        url: statesUrl,
        type: 'get',
        data: params,
      }, function (res) {
        // console.log('批文批复列表', res)
        vm.pageLoading = false;
        if (res.success && res.content.length > 0) {
          vm.officeDocList = res.content;
          vm.approvalList = res.content[0].itemMatinst;
        }else if((res.success && res.content.length == 0)){
          vm.officeDocList = res.content;
          vm.approvalList = [];
        }
      }, function () {
        vm.pageLoading = false;
      });
    },
    /**
     * 保存批文
     */
    saveNewApproval: function (formName) {
      var vm = this
      this.$refs[formName].validate(function (valid) {
        if (valid) {
          vm.officialDocForm.taskId = vm.taskId;
          vm.officialDocForm.applyinstId = vm.applyinstId;
          vm.officialDocForm.iteminstId = vm.iteminstId;
          vm.officialDocForm.files = vm.uploadTableData;
          vm.officialDocForm.attCount = vm.uploadTableData.length;
          var formData = new FormData();
          vm.preUploadFiles.forEach(function (u, index) {
            formData.append("files", vm.preUploadFiles[index])
          });
          formData.append("taskId", vm.taskId);// 文件对象   
          formData.append("applyinstId", vm.applyinstId);// 文件对象   
          formData.append("iteminstId", vm.iteminstId);// 文件对象    
          formData.append("attCount", vm.preUploadFiles.length); //
          // formData.append("attCount", vm.uploadTableData.length);// 文件对象   
          formData.append("matId", vm.officialDocForm.matId);// 文件对象   
          formData.append("memo", vm.officialDocForm.memo || '');// 文件对象     
          formData.append("matinstId", vm.officialDocForm.matinstId);// 文件对象
          formData.append("officialDocTitle", vm.officialDocForm.officialDocTitle);// 文件对象   
          formData.append("officialDocNo", vm.officialDocForm.officialDocNo);// 文件对象   
          formData.append("officialDocPublishDate", vm.officialDocForm.officialDocPublishDate);// 文件对象   
          formData.append("officialDocDueDate", vm.officialDocForm.officialDocDueDate);// 文件对象   
          formData.append("paperCount", vm.officialDocForm.paperCount);// 文件对象   
          var urltemp = ctx;
          var methodType = 'post';
          if (vm.isEditApprocal) {
            vm.officialDocForm.matinstId = vm.matinstId; // 批文批复id
            methodType = 'post'
            urltemp = urltemp + 'rest/approve/docs/edit';
          } else {
            urltemp = urltemp + 'rest/approve/docs/create';
          }
          console.log(vm.officialDocForm);
          // http
          axios.defaults.withCredentials = true;
          vm.fileLoading = true;
          axios[methodType](urltemp, formData).then(function (res) {
            if (res.data && res.data.success) {
              vm.$message.success('保存成功');
              vm.getmainTbaleList();
              vm.showApprovalDialog = false;
              vm.reSetEditFormRuleData('officialDocForm')
              vm.fileLoading = false;
            } else {
              vm.fileLoading = false;
              vm.$message.error(res.message);
            }
          }).catch(function (e) {
            vm.$message.error('保存失败');
          }).finally(function () {
            vm.fileLoading = false;
          })
          return null
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    /**
     * 修改批文
     */
    editApproval: function (row) {
      var vm = this;
      vm.showApprovalDialog = true;
      vm.approvalDialogTitle = '修改批复'
      vm.isEditApprocal = true;
      vm.matinstId = row.matinstId;
      vm.officialDocForm = JSON.parse(JSON.stringify(row));
      vm.canSelect = true;
      vm.onlyLook = false;
      if (vm.officialDocForm.docTypeName == "纸质") {
        vm.$set(vm.officialDocForm, 'paperCount', vm.officialDocForm.docCount)
        vm.CountType = 'paper'
      } else {
        //需要现请求材料列表回来 TODO xiaohutu
        vm.$set(vm.officialDocForm, 'attCount', vm.officialDocForm.docCount)
        vm.CountType = 'att'
      }
      vm.itemOfficeMatList = vm.cacheMatType;
    },
    /**
     * 查看批文
     */
    checkApproval: function (row) {
      var vm = this;
      vm.showApprovalDialog = true;
      vm.approvalDialogTitle = '查看批复';
      vm.isEditApprocal = false;
      vm.matinstId = row.matinstId;
      vm.officialDocForm = JSON.parse(JSON.stringify(row));
      vm.canSelect = true;
      vm.onlyLook = true;
      if (vm.officialDocForm.docTypeName == "纸质") {
        vm.$set(vm.officialDocForm, 'paperCount', vm.officialDocForm.docCount)
        vm.CountType = 'paper'
      } else {
        vm.$set(vm.officialDocForm, 'attCount', vm.officialDocForm.docCount)
        vm.CountType = 'att'
      }
      vm.itemOfficeMatList = [{
        matId: row.matId,
        matName: row.matinstName,
      }]
    },
    /*
    * 单个删除批文
    * */
    deleteApproval: function (row) {
      var matinstId = row.matinstId;
      var vm = this;
      confirmMsg('提示', '确定要删除选中的批文吗？', function () {
        vm.pageLoading = true;
        request('', {
          url: ctx + 'rest/approve/docs/delete/' + matinstId,
          type: 'delete',
          
        }, function (result) {
          vm.pageLoading = false;
          if (result.success) {
            vm.$message.success('删除成功');
            vm.showApprovalDialog = false;
            vm.getmainTbaleList();
          } else {
            vm.$message.error(result.message);
          }
        }, function () {
          vm.pageLoading = false;
          vm.$message.error("删除失败！");
        });
      })
    },
    /*
    * 批量删除批文
    * */
    batchDeleteApproval: function () {
      var vm = this;
      var approveListMultipleSelection = this.approveListMultipleSelection
      if (approveListMultipleSelection.length === 0) {
        alertMsg('', '你还没选中');
      } else {
        var matinstIdsArry = [];
        approveListMultipleSelection.forEach(function (item, index) {
          matinstIdsArry.push(item.matinstId);
        });
        var matinstIds = matinstIdsArry.join(',');
        confirmMsg('提示', '确定要删除选中的批文吗？', function () {
          request('', {
            url: ctx + 'rest/approve/docs/batch_delete',
            type: 'post',
            data: {
              'matinstIds': matinstIds
            },
          }, function (result) {
            if (result.success) {
              vm.$message.success('删除成功');
              vm.getmainTbaleList();
            } else {
              vm.$message.error(result.message);
            }
          }, function () {
            vm.$message.error("删除失败！");
          });
        })
      }
    },
    
    changeApprovelListSelection: function (val) {
      this.approveListMultipleSelection = val
    },
    /**
     * 上传文件
     * @param formName
     */
    realUploadDo: function () {
      var vm = this;
      var _file = vm.$refs.realUploadDo_.files[0];
      vm.$refs.realUploadDo_.value = '';
      var _canUpload = vm.uploadCheckFile(_file);
      if (_canUpload) {
        vm.fileList.push(_file);
      }
    },
    /**
     * 删除文件
     */
    deluploadImg: function (item, index) {
      var vm = this;
      confirmMsg('确定移除' + item.name + '?', '', function () {
        vm.fileList.splice(index, 1);
      })
    },
    /**
     * 校验文件类型
     * @param formName
     */
    uploadCheckFile: function (file) {
      var ts = this,
          file = file;
      // 文件类型
      // 检查文件类型
      var imgReg = /(.*)\.(exe|sh|bat|com|dll)$/;
      if (imgReg.test(file.name)) {
        ts.$message({
          message: '上传的文件是非法禁止的文件类型',
          type: 'error'
        });
        return false;
      }
      ;
      return true;
    },
    reSetEditFormRuleData: function (formName) {
      var vm = this;
      Object.keys(vm.officialDocForm).forEach(function (u) {
        vm.officialDocForm[u] = ''
      })
      vm.officialDocForm.attFiles = [];
      this.fileList = [];
      this.CountType = 'paper';
      this.preUploadFiles = [];
      
      setTimeout(function () {
        vm.$refs[formName].clearValidate();
      }, 100);
    },
    uploadFileCom: function (fileData) {
      var file = fileData.file;
      var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
      var extension = testmsg === 'exe'
      var extension2 = testmsg === 'sh'
      var extension3 = testmsg === 'bat'
      var extension4 = testmsg === 'com'
      var extension5 = testmsg === 'dll'
      if (extension || extension2 || extension3 || extension4 || extension5) {
        this.$message({
          message: '禁止上传以下的附件类型：.exe,.sh,.bat,.com,.dll!',
          type: 'error'
        });
      }
      if (file.size > 10485760) {
        this.$message({
          message: '上传文件大小不能超过 10MB!',
          type: 'error'
        });
      }
      if (extension || extension2 || extension3 || extension4 || extension5 || file.size > 10485760) {
        var fileIndex = this.fileList.indexOf(file.name);
        this.fileList.splice(fileIndex, 1);
        return false;
      } else {
        this.uploadTableData.push(file);
      }
      
    },
    fileDel: function (file) {
      var fileIndex = this.fileList.indexOf(file.name);
      this.fileList.splice(fileIndex, 1);
      this.uploadTableData.splice(fileIndex, 1);
    },
    selectionFileChange: function () {
    
    },
    //新增批文批复
    addOffice: function () {
      var _this = this;
      _this.canSelect = false;
      _this.onlyLook = false;
      //_this.reSetEditFormRuleData('officialDocForm');
      // this.$refs['officialDocForm'].resetFields();
      if (_this.itemOfficeMatList.length == 0) {
        _this.getItemInOfficeMat(function () {
          if (_this.itemOfficeMatList.length == 0) {
            _this.$message('当前事项没有定义的批文批复');
          } else {
            _this.showApprovalDialog = true;
            _this.approvalDialogTitle = '新增';
            _this.isEditApprocal = false
          }
        });
      } else {
        _this.showApprovalDialog = true;
        _this.approvalDialogTitle = '新增';
        _this.isEditApprocal = false
      }
    },
    //新增批文批复时先查出来当前事项下定义的所有的批文批复定义列表
    getItemInOfficeMat: function (callback) {
      var vm = this;
      vm.pageLoading = true;
      request('bpmFrontUrl', {
        url: ctx + 'rest/approve/getItemInOfficeMat',
        type: 'get',
        data: {
          iteminstId: vm.iteminstId,
          applyinstId: vm.applyinstId,
        },
        // async: false
      }, function (res) {
        vm.pageLoading = false;
        if (res.success) {
          vm.itemOfficeMatList = res.content;
          vm.cacheMatType = res.content;
        }
        typeof callback == 'function' && callback()
      }, function () {
        vm.pageLoading = false;
      });
    },
    setMatName: function (val) {
      
      // this.officialDocForm.officialDocTitle = val.matName;
      this.officialDocForm.matId = val;
      
      
    },
    // 获取文件后缀
    getFileType: function (fileName) {
      var index1 = fileName.lastIndexOf(".");
      var index2 = fileName.length;
      var suffix = fileName.substring(index1 + 1, index2);//后缀名
      if (suffix == 'docx') {
        suffix = 'doc';
      }
      return suffix;
    },
    // 上传文件
    clickUplodFile: function () {
      $('.dia-upload-input').trigger('click');
    },
    // 预览电子件
    filePreview: function (data) {
      // 调用父iframe预览方法
      parent.vm.filePreview(data);
    },
    //下载单个附件
    downOneFile: function (data) {
      window.open(ctx + 'rest/mats/att/download?detailIds=' + data.fileId, '_blank')
    },
    //删除单个文件附件
    delOneFile: function (data, matData) {
      if (!data.fileId) {
        // 删除tmp 不需要请求接口
        var index = -1;
        matData.attFiles.forEach(function (u, ind) {
          if (u.timestamp == data.timestamp) {
            index = ind;
          }
        })
        matData.attFiles = matData.attFiles.slice(0, index).concat(matData.attFiles.slice(index + 1))
        // 删除vm.preUploadFiles
        vm.preUploadFiles.forEach(function (u, ind) {
          if (u.timestamp == data.timestamp) {
            index = ind;
          }
        })
        vm.preUploadFiles = vm.preUploadFiles.slice(0, index).concat(vm.preUploadFiles.slice(index + 1))
        return;
      }
      var _that = this;
      this.$confirm('此操作将永久删除该文件, 是否继续?', '删除文件', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(function (obj) {
        ensureDelete();
      }).catch(function () {
      });
      
      function ensureDelete() {
        request('', {
          url: ctx + 'rest/mats/att/delelte',
          type: 'post',
          data: {matinstId: matData.matinstId, detailIds: data.fileId}
        }, function (res) {
          if (res.success) {
            // _that.getFileListWin(_that.selMatRowData.matinstId, _that.selMatRowData);
            _that.$message({
              message: '删除成功',
              type: 'success'
            });
            var index = -1;
            matData.attFiles.forEach(function (u, ind) {
              if (u.fileId == data.fileId) {
                index = ind
              }
            })
            matData.attFiles = matData.attFiles.slice(0, index).concat(matData.attFiles.slice(index + 1))
          } else {
            _that.$message({
              message: res.message ? res.message : '删除失败',
              type: 'error'
            });
          }
        }, function (msg) {
          _that.$message({
            message: '服务请求失败',
            type: 'error'
          });
        });
      }
    },
  },
  filters: {
    formatDate: function (time) {
      var date = new Date(time);
      if (!date) return;
      return formatDate(date, 'yyyy-MM-dd hh:mm');
    },
    formatOnlyDate: function (time) {
      var date = new Date(time);
      if (!date) return '-';
      return formatDate(date, 'yyyy-MM-dd');
    },
    dicCodeItem: function (value) {
      if (value != null) {
        var arr = value.split(',');
        for (var j = 0; j < arr.length; j++) {
          for (var i = 0; i < baseInfoVue.dicCodeItems.length; i++) {
            var item = baseInfoVue.dicCodeItems[i];
            if (item["itemCode"] == arr[j]) {
              arr[j] = item["itemName"];
              break;
            }
          }
        }
        return arr.join(',');
      }
      return value;
    },
    changeProjNatureFromDic: function (value) {
      if (value != null) {
        for (var i = 0; i < baseInfoVue.projNatureList.length; i++) {
          var item = baseInfoVue.projNatureList[i];
          if (item["itemCode"] == value) {
            return item["itemName"];
          }
        }
      }
      return value;
    },
    changeProjTypeFromDic: function (value) {
      if (value != null) {
        for (var i = 0; i < baseInfoVue.projTypeList.length; i++) {
          var item = baseInfoVue.projTypeList[i];
          if (item["itemCode"] == value) {
            return item["itemName"];
          }
        }
      }
      return value;
    },
  },
});

function getUrlParam(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return unescape(r[2]);
  }
  ;
  return null;
}