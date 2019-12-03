/*
 * @Author: Lfuqiang
 * @Date:   2019/6/4
 * @Last Modified by:   Lfuqiang
 * @Last Modified time: $ $
 */
var vm = new Vue({
    el: '#unitsDetail',
    mixins: [mixins],
    data: function () {
        return {
            activeTab: 0,  // 纵向导航active状态index
            verticalTabData: [ // 左侧纵向导航数据
                {
                    label: '基本信息',
                    target: 'baseInfo'
                }, {
                    label: '单位信息',
                    target: 'units'
                }
            ],
            activeNames: ['1', '2'],
            tableData: [{
                name: 'Amy',
                idcard: 'Amy',
                mobileNum: 'Amy',
                email: 'Amy',
                address: 'Amy',
                dlName: 'Amy'
            },
            {
                name: 'Amy',
                idcard: 'Amy',
                mobileNum: 'Amy',
                email: 'Amy',
                address: 'Amy',
                dlName: 'Amy'
            },
            {
                name: 'Amy',
                idcard: 'Amy',
                mobileNum: 'Amy',
                email: 'Amy',
                address: 'Amy',
                dlName: 'Amy'
            }],
            // 表单数据
            manForm: {
                applicant: '', //单位名
                unifiedSocialCreditCode: '', //统一社会信用代码
                organizationalCode: '',//组织机构代码
                induCommRegNum: '',//工商登记号
                taxpayerNum: '',//纳税人识别号
                creditFlagNum: '',//信用标记码
                isPrimaryUnit: '', //是否主单位 0-副单位   1-主单位
                isOwnerUnit: '', //角色-是否是业主
                isImUnit: '', //角色-是否是中介机构
                unitNature: '', //单位性质
                unitType: '', //单位类型
                registeredCapital: '', //注册资本
                registerAuthority: '', //注册登记机关
                postalCode: '', //邮政编码
                managementScope: '', //经营范围
                idrepresentative: '', // 法定代表人
                idno: '', //身份证号码
                contact: '', //联系人姓名
                idmobile: '', //联系电话
                email: '', //联系人电子邮箱
                applicantDetailSite: '', //具体地址
            },
            // 当前编辑行
            curEditRow: {
            },
            // 附件列表中当前选中附件list
            enclosureSelectList: [],
            // 附件上传时，从电脑中选择的文件列表
            enclosureFileListArr: [],
            // 表单验证
            manRules: {
                applicant: [{
                    required: true,
                    message: '请输入单位名',
                    trigger: 'blur'
                }], //单位名
                unifiedSocialCreditCode: [{
                    required: true,
                    message: '请输入统一社会信用代码',
                    trigger: 'blur'
                }],
                isPrimaryUnit: [{
                    required: true,
                    message: '请选择是否主单位',
                    trigger: 'change'
                }], //是否主单位 0-副单位   1-主单位
                unitNature: [{
                    required: true,
                    message: '请选择单位性质',
                    trigger: 'change'
                }], //单位性质
                unitType: [{
                    required: true,
                    message: '请选择单位类型',
                    trigger: 'change'
                }], //单位类型
                registeredCapital: [{
                    required: true,
                    message: '请输入注册资本',
                    trigger: 'blur'
                }], //注册资本
                registerAuthority: [{
                    required: true,
                    message: '请输入注册登记机关',
                    trigger: 'blur'
                }], //注册登记机关
                postalCode: [{
                    required: true,
                    message: '请输入邮政编码',
                    trigger: 'blur'
                }], //邮政编码
                managementScope: [{
                    required: true,
                    message: '请输入经营范围',
                    trigger: 'blur'
                }], //经营范围
                idrepresentative: [{
                    required: true,
                    message: '请输入法定代表人',
                    trigger: 'blur'
                }], // 法定代表人
                idno: [{
                    required: true,
                    message: '请输入身份证号码',
                    trigger: 'blur'
                }], //身份证号码
                contact: [{
                    required: true,
                    message: '请输入联系人姓名',
                    trigger: 'blur'
                }], //联系人姓名
                idmobile: [{
                    required: true,
                    message: '请输入联系电话',
                    trigger: 'blur'
                }], //联系电话
                applicantDetailSite: [{
                    required: true,
                    message: '请输入具体地址',
                    trigger: 'blur'
                }], //具体地址
            },
            roles: [],
        }
    },
    mounted: function () {
        this.fetchRowDetail();
    },
    methods: {
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
        // dialog-角色切换
        roleChange: function (list) {
            // console.log(list)
            this.manForm.isImUnit = 0;
            this.manForm.isOwnerUnit = 0;
            if (!list.length) return;
            if (list.indexOf('isImUnit') !== -1) {
                this.manForm.isImUnit = 1;
            }
            if (list.indexOf('isOwnerUnit') !== -1) {
                this.manForm.isOwnerUnit = 1;
            }
        },
        // 关闭窗口-仅限于新打开的窗口
        closeWindowTab: function () {
            var ts = this;
            var _tabId = ts.getSerachParamsForUrl('projInfoId');
            !!_tabId ?
                (_tabId = _tabId + '_pjdetail') :
                (_tabId = 'add_pjdetail');
            setTimeout(function () {
                window.parent.vm.removeTab(_tabId);
            }, 500)
        },
        // 新增单位
        addApplicant: function () {
            this.applicantDialogState = true;
            this.isEditApplicantFlag = 0;
            // 新增时重置掉manForm数据
            this.manForm = JSON.parse(JSON.stringify(MANFORM));
        },
        // 编辑单位
        editApplicant: function (row) {
            this.isEditApplicantFlag = 1;
            this.fetchRowDetail(row.unitInfoId);
        },
        // 编辑单位时，获取单位的详情
        fetchRowDetail: function (id) {
            var ts = this;
            ts.loading = true;
            request('', {
                url: ctx + 'rest/applicant/getApplicantById',
                type: 'get',
                data: {
                    id: '4bd06452-0e03-438f-b2d8-d798c1e0faf5'
                }
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.curEditRow = res.content;
                    // ts.manForm = JSON.parse(JSON.stringify(res.content));
                    ts.formEditInitData(JSON.parse(JSON.stringify(res.content)))
                    ts.applicantDialogState = true;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        // 编辑单位时，对manForm进行已有值赋值
        formEditInitData: function (obj) {
            var ts = this;
            for (var k in MANFORM) {
                ts.manForm[k] = obj[k];
            }
            // 处理单位的角色
            ts.initUnitRolesData(obj);
        },
        // 处理单位的角色
        initUnitRolesData: function (data) {
            this.roles = [];
            var _roles = ['isImUnit', 'isOwnerUnit'];
            for (var i = 0; i < _roles.length; i++) {
                if (data[_roles[i]] && data[_roles[i]] == 1) {
                    this.roles.push(_roles[i]);
                }
            }
        },
        // 文件上传相关
        // 附件上传--before
        enclosureBeforeUpload: function (file) {
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
        enclosureFileSelectChange: function (file, fileList) {
            var ts = this;
            ts.enclosureFileListArr = [];
            // 选择后检验
            if (!ts.enclosureBeforeUpload(file)) {
                var fileIndex = fileList.indexOf(file.name);
                fileList.splice(fileIndex, 1);
            }
            fileList.forEach(function (item) {
                ts.enclosureFileListArr.push(item.raw)
            })

        },
        // 附件上传文件列表移除某一项
        enclosureFileSelectRemove: function (file, fileList) {
            var ts = this;
            ts.enclosureFileListArr = [];
            // 选择后检验
            if (!ts.enclosureBeforeUpload(file)) {
                var fileIndex = fileList.indexOf(file.name);
                fileList.splice(fileIndex, 1);
            }
            fileList.forEach(function (item) {
                ts.enclosureFileListArr.push(item.raw)
            })

        },
        // 附件删除（删除已上传的附件）
        deleteContractOne: function (obj) {
            var ts = this,
                _delData = {};
            confirmMsg('', '您确定删除当前附件吗？', function () {
                _delData.detailId = obj.detailId;
                ts.loading = true;
                request('', {
                    url: ctx + 'rest/applicant/deleteFile',
                    type: 'get',
                    data: _delData
                }, function (res) {
                    ts.loading = false;
                    if (res.success) {
                        // 前端重新获取一下当前编辑单位的详情
                        ts.fetchRowDetail(ts.curEditRow.unitInfoId)
                    } else {
                        ts.apiMessage(res.message, 'error')
                    }
                }, function (msg) {
                    ts.loading = false;
                    ts.apiMessage('网络错误！', 'error')
                });
            });
        },
        // 附件列表批量选中
        handleFileSelectionChange: function (list) {
            this.enclosureSelectList = list;
            // console.log(list)
        },

        // 下载文件
        downFile: function (row) {
            window.open(ctx + 'rest/mats/att/download?detailIds=' + row.fileId);
        },
        // 预览文件
        filePreview: function (row) {
            window.open(ctx + 'rest/mats/att/preview?detailId=' + row.fileId);
        },
        // 保存
        saveApplicantInfo: function () {
            var ts = this;
            this.$refs['manForm'].validate(function (valid, obj) {
                if (valid) {
                    // console.log(ts.manForm)
                    var _saveForm = new FormData();
                    for (var k in ts.manForm) {
                        _saveForm.append(k, ts.manForm[k]);
                    }
                    // 如果是编辑单位-带上单位的id
                    if (ts.isEditApplicantFlag == 1) {
                        _saveForm.append('unitInfoId', ts.curEditRow.unitInfoId);
                    }
                    // 如果选择了本地的文件上传
                    if (ts.enclosureFileListArr.length) {
                        for (var j = 0; j < ts.enclosureFileListArr.length; j++) {
                            _saveForm.append('files', ts.enclosureFileListArr[j]);
                        }
                    }
                    // 加上后端说的跟用户屁关系没有的数据
                    // tableName: 'AEA_LINKMAN_INFO',
                    // pkName: 'LINKMAN_INFO_ID'
                    _saveForm.append('tableName', 'AEA_UNIT_INFO')
                    _saveForm.append('pkName', 'UNIT_INFO_ID')
                    // return
                    ts.loading = true;
                    $.ajax({
                        url: ctx + 'rest/applicant/saveGlobalApplicant',
                        type: 'post',
                        data: _saveForm,
                        contentType: false,
                        processData: false,
                        // mimeType: "multipart/form-data",
                        success: function (res) {
                            ts.loading = false;
                            if (res.success) {
                                ts.apiMessage('保存成功！', 'success')
                                if (res.message != '') {//进行密码的自动生成
                                    var _resetData = {};
                                    _resetData.unitInfoId = res.message;
                                    ts.loading = true;
                                    request('', {
                                        url: ctx + 'rest/applicant/generatePassword',
                                        type: 'get',
                                        data: _resetData
                                    }, function (res) {
                                        var data = res.content;
                                        ts.loading = false;
                                        if (res.success) {
                                            // ts.apiMessage('生成密码成功！', 'success');
                                            // 打印回执
                                            //var url = ctx + "/aea/hi/receive/toPrintPageForPassword.do?type=applicant";
                                            //window.open(ctx + '/file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url));
                                            var url = ctx + "/rest/receive/savePasswordPdf/" + data.loginName + "/" + data.loginPwd;
                                            window.open(ctx + 'preview/pdfjs/web/viewer.html?file=' + encodeURIComponent(url));
                                        } else {
                                            ts.apiMessage(res.message, 'error');
                                        }
                                    }, function (msg) {
                                        ts.loading = false;
                                        ts.apiMessage('网络错误！', 'error');
                                    });

                                }
                                ts.applicantDialogState = false;
                                ts.enclosureFileListArr = [];

                                // 刷新列表数据
                                // ts.fetchTableData();
                            } else {
                                ts.apiMessage(res.message, 'error')
                            }
                        },
                        error: function (msg) {
                            ts.loading = false;
                            ts.apiMessage('保存失败！', 'error')
                        },
                    })

                } else {
                    var noPassageArr = []
                    for (var i in obj) {
                        noPassageArr.push(obj[i]); //值
                    }
                    if (noPassageArr.length) {
                        ts.apiMessage(noPassageArr[0][0].message, 'error')
                    }
                    return false;
                }
            });
        }
    }
})

// 单位dialog表单初始数据
var MANFORM = {
    applicant: '', //单位名
    unifiedSocialCreditCode: '', //统一社会信用代码
    organizationalCode: '',//组织机构代码
    induCommRegNum: '',//工商登记号
    taxpayerNum: '',//纳税人识别号
    creditFlagNum: '',//信用标记码
    //idtype: '', // 单位证照类型
    isPrimaryUnit: '', //是否主单位 0-副单位   1-主单位
    isOwnerUnit: '', //角色-是否是业主
    isImUnit: '', //角色-是否是中介机构
    unitNature: '', //单位性质
    unitType: '', //单位类型
    registeredCapital: '', //注册资本
    registerAuthority: '', //注册登记机关
    postalCode: '', //邮政编码
    managementScope: '', //经营范围
    idrepresentative: '', // 法定代表人
    idno: '', //身份证号码
    contact: '', //联系人姓名
    idmobile: '', //联系电话
    email: '', //联系人电子邮箱
    applicantDetailSite: '', //具体地址
}