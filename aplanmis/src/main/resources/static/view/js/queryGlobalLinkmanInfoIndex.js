var vm = new Vue({
    el: '#searchTable',
    mixins: [mixins],
    data: function () {
        return {
            //查询数据
            searchFrom: {
                pagination:{
                    page:  1,
                    pages: 1,
                    perpage: 10
                },
                sort:{},
                keyword:''
            },

            // 当前编辑行
            curEditRow: {
            },
            // 附件列表中当前选中附件list
            enclosureSelectList: [],
            // 附件上传时，从电脑中选择的文件列表
            enclosureFileListArr: [],

            // 联系人新增-编辑相关
            // 新增-编辑联系人-dialog
            linkManDialogState: false,
            // 当前为新增还是编辑： 0-新增， 1-编辑
            isEditLinkManFlag: 0,
            // 表单数据
            manForm: {
                linkmanName: '', //联系人名称
                linkmanType: '', //联系人类型
                linkmanCertNo: '', //身份证号
                linkmanMobilePhone: '', //手机号码
                linkmanOfficePhon: '', //办公电话
                linkmanMail: '', //电子邮件
                linkmanFax: '', //传真
                linkmanAddr: '', //联系人住址
                linkmanMemo: '', //备注
                // tableName: 'AEA_LINKMAN_INFO',
                // pkName: 'LINKMAN_INFO_ID'
                applicant: '' //单位名-联系人类型为企业时
            },
            // 表单验证
            manRules: {
                linkmanName: [{
                    required: true,
                    message: '请输入联系人姓名',
                    trigger: 'blur'
                }], //联系人名称
                linkmanType: [{
                    required: true,
                    message: '请选择联系人类型',
                    trigger: 'change'
                }], //联系人类型
                linkmanCertNo: [{
                    required: true,
                    message: '请输入身份证号',
                    trigger: 'blur'
                }], //身份证号
                linkmanMobilePhone: [{
                    required: true,
                    validator: checkPhone,
                    trigger: 'blur'
                }], //手机号码
                linkmanAddr: [{
                    required: true,
                    message: '请输入联系人住址',
                    trigger: 'blur'
                }], //联系人住址
            },

            // dialog-单位列表
            unitList: [],
            // 是否展示单位列表选择框
            isShowSelectDropBox: false
        }
    },
    methods: {
        // 跳转到详情页面
        jumpToDetail: function (id) {
            var _linkmanId = id;
            var parentIfreamUrl = window.frames.location.href;
            var _url = "",_tabName="";
            if (_linkmanId === 'add'){
                _tabName = "新增人员";
                _url = ctx + 'rest/linkman/lib/detail?parentIfreamUrl='+parentIfreamUrl;
            }else{
                _tabName = "编辑人员"
                _url = ctx + 'rest/linkman/lib/detail?linkmanId=' + _linkmanId +'&parentIfreamUrl='+parentIfreamUrl;
            }
            var _jumpData = {
                'menuName': _tabName,
                'menuInnerUrl': _url,
                'id': _linkmanId + '_linkmanDetail'
            };
            parent.vm.addTab('', _jumpData, parent.vm.activeTabIframe, '');
        },
        //刷新列表
        fetchTableData: function () {
            var ts = this;

            this.searchFrom.pagination.pages = this.searchFrom.pagination.page;

            ts.loading = true;

            request('', {
                url: ctx + 'rest/linkman/listAeaLinkMansByPage',
                type: 'get',
                data: ts.searchFrom
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.tableData = res.content.list;
                    ts.dataTotal = res.content.total;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        // 联系人表格操作
        // 生成密码
        generatePassword: function (row) {
            var ts = this,
                _resetData = {};
            confirmMsg('', '此操作将会重置密码，您确定执行吗？', function () {
                _resetData.linkmanInfoId = row.linkmanInfoId;
                ts.loading = true;
                request('', {
                    url: ctx + 'rest/linkman/generatePassword',
                    type: 'get',
                    data: _resetData
                }, function (res) {
                    ts.loading = false;
                    if (res.success) {
                        var data=res.content;
                        ts.apiMessage('生成密码成功！', 'success');
                        // 打印回执
                        setTimeout(function () {
                            //var url = ctx + "rest/receive/toPrintPageForPassword?type=linkman";
                            //window.open(ctx + 'rest/ntko/ntkoOpenWin?jumpUrl=' + encodeURIComponent(url));

                            var url = ctx + "rest/receive/savePasswordPdf/"+data.loginName+"/"+data.loginPwd;
                            window.open(ctx + 'preview/pdfjs/web/viewer.html?file='+encodeURIComponent(url));
                        },1000);
                        /*var url = ctx + "/aea/hi/receive/toPrintPageForPassword.do?type=linkman";
                        window.open(ctx + '/file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url));*/
                    } else {
                        ts.apiMessage('生成密码失败！', 'error');
                    }
                }, function (msg) {
                    ts.loading = false;
                    ts.apiMessage('网络错误！', 'error');
                });
            });
        },
        // 删除联系人
        delLinkMan: function (row) {
            var ts = this,
                _delData = {};
            confirmMsg('', '您确定删除当前联系人吗？', function () {
                _delData.id = row.linkmanInfoId;
                ts.loading = true;
                request('', {
                    url: ctx + 'rest/linkman/deleteAeaLinkManById',
                    type: 'get',
                    data: _delData
                }, function (res) {
                    ts.loading = false;
                    if (res.success) {
                        ts.apiMessage('删除成功！', 'success');
                        ts.fetchTableData();
                    } else {
                        ts.apiMessage('删除失败！', 'error');
                    }
                }, function (msg) {
                    ts.loading = false;
                    ts.apiMessage('删除失败！', 'error');
                });
            });
        },

        // 联系人-dialog相关
        // 关闭dialog后，重置掉里面表单的已显示的校验
        dialogClose: function () {
            this.$refs['manForm'].resetFields();
        },
        // 新增联系人
        addLinkMan: function () {
            this.linkManDialogState = true;
            this.isEditLinkManFlag = 0;
            // 新增时重置掉manForm数据
            this.manForm = JSON.parse(JSON.stringify(MANFORM));
            this.curEditRow = {};
        },
        // 编辑联系人
        editLinkMan: function (row) {
            this.isEditLinkManFlag = 1;
            this.fetchRowDetail(row.linkmanInfoId);
        },
        // 编辑联系人时，获取联系人的详情
        fetchRowDetail: function (id) {
            var ts = this;
            ts.loading = true;
            request('', {
                url: ctx + 'rest/linkman/getAeaLinkManById',
                type: 'get',
                data: {
                    id: id
                }
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.curEditRow = res.content;
                    // ts.manForm = JSON.parse(JSON.stringify(res.content));
                    ts.formEditInitData(JSON.parse(JSON.stringify(res.content)))
                    ts.linkManDialogState = true;
                } else {
                    return ts.apiMessage('联系人详情获取失败！', 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        // 编辑联系人时，对manForm进行已有值赋值
        formEditInitData: function (obj) {
            var ts = this;
            for (var k in MANFORM) {
                ts.manForm[k] = obj[k];
            }
        },

        // 保存联系人信息
        saveLinkManInfo: function () {
            var ts = this;
            this.$refs['manForm'].validate(function (valid, obj) {
                if (valid) {
                    // console.log(ts.manForm)
                    var _saveForm = new FormData();
                    for (var k in ts.manForm) {
                        _saveForm.append(k, ts.manForm[k]);
                    }
                    // 如果是编辑联系人-带上联系人的id
                    if (ts.isEditLinkManFlag == 1) {
                        _saveForm.append('linkmanInfoId', ts.curEditRow.linkmanInfoId);
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
                    _saveForm.append('tableName', 'AEA_LINKMAN_INFO')
                    _saveForm.append('pkName', 'LINKMAN_INFO_ID')
                    // return
                    ts.loading = true;
                    $.ajax({
                        url: ctx + 'rest/linkman/saveAeaLinkMan',
                        type: 'post',
                        // dataType: 'json',
                        data: _saveForm,
                        contentType: false,
                        processData: false,
                        // mimeType: "multipart/form-data",
                        success: function (res) {
                            ts.loading = false;
                            if (res.success) {
                                if(res.message != ''){
                                    var _resetData = {};
                                    _resetData.linkmanInfoId = res.message;
                                    ts.loading = true;
                                    request('', {
                                        url: ctx + 'rest/linkman/generatePassword',
                                        type: 'get',
                                        data: _resetData
                                    }, function (res) {
                                        debugger
                                        ts.loading = false;
                                        if (res.success) {
                                            var data=res.content;
                                            // ts.apiMessage('生成密码成功！', 'success');
                                            // 打印回执
                                            // var url = ctx + "/aea/hi/receive/toPrintPageForPassword.do?type=linkman";
                                            // window.open(ctx + '/file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url));
                                            var url = ctx + "rest/receive/savePasswordPdf/"+data.loginName+"/"+data.loginPwd;
                                            window.open(ctx + 'preview/pdfjs/web/viewer.html?file='+encodeURIComponent(url));
                                        } else {
                                            ts.apiMessage('生成密码失败！', 'error');
                                        }
                                    }, function (msg) {
                                        ts.loading = false;
                                        ts.apiMessage('网络错误！', 'error');
                                    });
                                }
                                ts.apiMessage('保存成功！', 'success')
                                ts.linkManDialogState = false;
                                ts.enclosureFileListArr = [];
                                ts.fetchTableData();
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
            confirmMsg('', '您确定删除当前文件吗？', function () {
                _delData.detailIds = obj.fileId;
                ts.loading = true;
                request('', {
                    url: ctx + 'rest/linkman/deleteFile',
                    type: 'get',
                    data: _delData
                }, function (res) {
                    ts.loading = false;
                    if (res.success) {
                        // 前端重新获取一下当前编辑联系人的详情
                        ts.fetchRowDetail(ts.curEditRow.linkmanInfoId)
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

        // change联系人类型
        changeLinkmanType: function (val) {
            // console.log(val)
            // return
            if (val == 'c') {
                delete this.manForm.applicant;
                delete this.manForm.unitInfoId;
            }
        },

        // 单位列表获取哦
        fetchUnitList: function () {
            var ts = this;
            var query = ts.manForm.applicant;
            if (!query) return;
            request('', {
                url: ctx + 'rest/applicant/listApplicantsNoPage',
                type: 'get',
                data: {
                    keyword: query
                }
            }, function (res) {
                // 展示可选择的单位下拉框
                ts.showSelectDropBox();
                ts.unitList = res.map(function (item) {
                    return {
                        applicant: item.applicant,
                        unitInfoId: item.unitInfoId
                    }
                })
            }, function () {
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        // 选中下拉框显示
        showSelectDropBox: function () {
            var ts = this;
            this.isShowSelectDropBox = true;
            // test-当点击外部时，关闭下拉弹框
            document.onclick = function () {
                ts.hideSelectDropBox();
                document.onclick = null;
            }
        },
        hideSelectDropBox: function () {
            this.isShowSelectDropBox = false;
        },

        // 选中下拉框中的一项单位数据
        selectDropItem: function (item) {
            this.manForm.applicant = item.applicant;
            this.manForm.unitInfoId = item.unitInfoId;
            this.hideSelectDropBox()
        },

        // 下载文件
        downFile: function (row) {
            window.open(ctx + 'rest/mats/att/download?detailIds=' + row.fileId);
        },
        // 预览文件
        filePreview:function(row) {
            window.open(ctx + 'rest/mats/att/preview?detailId=' + row.fileId);
        }
    },
    created: function () {
        this.fetchTableData();
    }
});
// 手机号校验ele
var checkPhone = function (rule, value, callback) {
    if (!value) {
        return callback(new Error('手机号不能为空'));
    } else {
        var reg = /^1[3|4|5|7|8][0-9]\d{8}$/
        // console.log(reg.test(value));
        if (reg.test(value)) {
            callback();
        } else {
            return callback(new Error('请输入正确的手机号'));
        }
    }
};
// 联系热表格查询初始数据
var LINKMANSEARCHDDATA = {
    pageNum: 1,
    pageSize: 10,
    keyword: ''
}
// 联系人dialog，初始数据
var MANFORM = {
    linkmanName: '', //联系人名称
    linkmanType: '', //联系人类型
    linkmanCertNo: '', //身份证号
    linkmanMobilePhone: '', //手机号码
    linkmanOfficePhon: '', //办公电话
    linkmanMail: '', //电子邮件
    linkmanFax: '', //传真
    linkmanAddr: '', //联系人住址
    linkmanMemo: '', //备注
    applicant: '' //单位名-联系人类型为企业时
}