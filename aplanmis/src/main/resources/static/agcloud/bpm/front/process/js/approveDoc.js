var vm = new Vue({
    el: "#approvalDocument",
    data: {
        ctx: ctx,
        taskId: '',
        applyinstId: '',
        iteminstId: '',
        isApprover: 1, // 是否部门审批人员
        tableloading: false,
        styleObj: {
            background: '#f5f5f5',
        },
        isSeries: '0',
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
            docCount: ''
        },
        EditFormRules: {
            officialDocTitle: [
                {required: true, message: '请输入文件标题', trigger: 'change'}
            ],
            officialDocNo: [
                {required: true, message: '请输入文件编号', trigger: 'change'}
            ],
            officialDocDueDate: [
                {required: true, message: '请输入文件有效期', trigger: 'change'}
            ],
            officialDocPublishDate: [
                {required: true, message: '请输入文件生效日期', trigger: 'change'}
            ]
        },
        CountType: 'paper',
        approveListMultipleSelection: [],
        isEditApprocal: false,
        matinstId: '', //批文批复id
        fileList: [],//文件列表
        showUploadWindowFlag: false,//上传文件弹框
        loadingFileWin: false,//
        uploadTableData: [],
        itemOfficeMatList: [],//当前事项对应的批文批复定义列表
        selectMatName: '',//批文批复 选择的所属材料
        canSelect: true,//编辑时是批文批复 是否可以选择所属材料

    },
    created: function () {
        //this.initData();
    },
    mounted: function () {
        var vm = this;
        setTimeout(function () {
            vm.initData();
        }, 400)
        setTimeout(function () {
            vm.getItemInOfficeMat();
        }, 1000)

    },
    methods: {
        initData: function () {
            var vm = this;
            vm.taskId = parent.app.taskId // 任务ID
            vm.applyinstId = parent.app.masterEntityKey //申报实例id
            vm.iteminstId = parent.app.busRecordId;//串联才会有值，并联为空  事项实例id
            vm.isApprover = parent.app.isApprover;
            if (!vm.iteminstId || vm.iteminstId == 'undefined') {
                vm.iteminstId = parent.app.iteminstId;
            }
            this.getmainTbaleList() // 获取批文批复列表
        },
        /**
         * 并联申报批文批复列表
         */
        getmainTbaleList: function () {
            var vm = this
            vm.processStatesDialog = true
            var statesUrl;
            var params;
            if (vm.isSeries == '1') {
                statesUrl = ctx + 'rest/approve/docs/single' // 串联
                params = {
                    iteminstId: vm.iteminstId
                }
            } else {
                statesUrl = ctx + 'rest/approve/docs/parallel' // 并联
                params = {
                    applyinstId: vm.applyinstId,
                    isApprover: vm.isApprover
                }
            }
            this.tableloading = true;
            request('bpmFrontUrl', {
                url: statesUrl,
                type: 'get',
                data: params,
            }, function (res) {
                console.log('批文批复列表', res)
                vm.tableloading = false;
                if (res.success && res.content.length > 0) {
                    vm.officeDocList = res.content;
                    vm.approvalList = res.content[0].itemMatinst;

                }
            }, function () {
                vm.tableloading = false;
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
                    /*if (vm.CountType == 'att') {
                        vm.officialDocForm.attCount = vm.newApprovalData.attCount
                    } else {
                        vm.officialDocForm.paperCount = vm.newApprovalData.paperCount
                    }*/
                    var urltemp = ctx;
                    var methodType = 'post';
                    if (vm.isEditApprocal) {
                        vm.officialDocForm.matinstId = vm.matinstId; // 批文批复id
                        methodType = 'put'
                        urltemp = urltemp + 'rest/approve/docs/edit';
                    } else {
                        urltemp = urltemp + 'rest/approve/docs/create';
                    }

                    request('', {
                        url: urltemp,
                        type: methodType,
                        data: vm.officialDocForm,
                    }, function (result) {
                        if (result.success) {
                            vm.$message.success('保存成功');
                            vm.getmainTbaleList();
                            vm.showApprovalDialog = false;
                            vm.reSetEditFormRuleData('officialDocForm')
                        } else {
                            vm.$message.error(result.message);
                        }
                    }, function () {
                        vm.$message.error("保存失败！");
                    });
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
            vm.isEditApprocal = true
            vm.matinstId = row.matinstId
            vm.officialDocForm = JSON.parse(JSON.stringify(row));
            vm.canSelect = true;
            if (vm.officialDocForm.docTypeName == "纸质") {
                vm.$set(vm.officialDocForm, 'paperCount', vm.officialDocForm.docCount)

                vm.CountType = 'paper'
            } else {
                //需要现请求材料列表回来 TODO xiaohutu


                vm.$set(vm.officialDocForm, 'attCount', vm.officialDocForm.docCount)
                vm.CountType = 'att'
            }
            console.log(vm.officialDocForm)
        },
        /**
         * 查看批文
         */
        checkApproval: function (row) {
            var vm = this;
            vm.showApprovalDialog = true;
            vm.approvalDialogTitle = '查看批复';
            vm.isEditApprocal = true;
            vm.matinstId = row.matinstId;
            console.log(row);
            vm.officialDocForm = JSON.parse(JSON.stringify(row));
            if (vm.officialDocForm.docTypeName == "纸质") {
                vm.$set(vm.officialDocForm, 'paperCount', vm.officialDocForm.docCount)

                vm.CountType = 'paper'
            } else {
                vm.$set(vm.officialDocForm, 'attCount', vm.officialDocForm.docCount)
                vm.CountType = 'att'
            }
        },
        /*
        * 单个删除批文
        * */
        deleteApproval: function (row) {
            console.log(row);
            var matinstId = row.matinstId;
        debugger;
            confirmMsg('提示', '确定要删除选中的批文吗？', function () {
                request('', {
                    url: ctx + 'rest/approve/docs/delete/' + matinstId,
                    type: 'delete',

                }, function (result) {
                    if (result.success) {
                        vm.$message.success('删除成功');
                        vm.getmainTbaleList();
                        vm.showApprovalDialog = false;
                    } else {
                        vm.$message.error(result.message);
                    }
                }, function () {
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
                console.log(matinstIds);
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
            console.log(1233)
            var _file = vm.$refs.realUploadDo_.files[0];
            vm.$refs.realUploadDo_.value = '';
            var _canUpload = vm.uploadCheckFile(_file);
            if (_canUpload) {
                vm.fileList.push(_file);
                console.log(vm.fileList);
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
            if (this.$refs[formName]) {
                this.$refs[formName].resetFields();
            }
            this.$nextTick(function () {
                this.$refs[formName].resetFields();
            });

            //this.$refs[formName].resetFields();
            this.fileList = [];
            this.CountType = 'paper';
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
            console.log(this.uploadTableData)
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
            //_this.reSetEditFormRuleData('officialDocForm');
            //this.$refs['officialDocForm'].resetFields();
            if (_this.itemOfficeMatList.length == 0) {
                _this.getItemInOfficeMat();
            }
            if (_this.itemOfficeMatList.length == 0) {
                alertMsg('', '当前事项没有定义的批文批复')
            } else {
                _this.showApprovalDialog = true;
                _this.approvalDialogTitle = '新增';
                _this.isEditApprocal = false
            }

        },
        //新增批文批复时先查出来当前事项下定义的所有的批文批复定义列表
        getItemInOfficeMat: function () {
            var vm = this;
            request('bpmFrontUrl', {
                url: ctx + 'rest/approve/getItemInOfficeMat',
                type: 'get',
                data: {iteminstId: vm.iteminstId},
                async: false
            }, function (res) {
                console.log('批文批复定义列表', res)
                vm.tableloading = false;
                if (res.success) {
                    vm.itemOfficeMatList = res.content;
                }
            }, function () {
                vm.tableloading = false;
            });
        },
        setMatName: function (val) {
            console.log(val);
            //vm.$set(vm.officialDocForm,'officialDocTitle',val)
            this.officialDocForm.officialDocTitle = val.matName;
            this.officialDocForm.matId = val.matId;


        }
    }
})