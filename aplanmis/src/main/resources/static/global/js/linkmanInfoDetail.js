/*
 * @Author: Lfuqiang
 * @Date:   2019/6/4
 * @Last Modified by:   Lfuqiang
 * @Last Modified time: $ $
 */
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
var vm = new Vue({
    el: '#linkmanInfo',
    mixins: [mixins],
    data: function () {
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

        return {
            loading: false,
            linkmanId: '',
            isEdit: false,
            activeNames: ['1', '2'],
            activeTab: 0,  // 纵向导航active状态index
            verticalTabData: [ // 左侧纵向导航数据
                {
                    label: '基本信息',
                    target: 'baseInfo'
                }
            ],
            tableData: [],
            // 当前编辑行
            curEditRow: {
            },
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
                    trigger: 'change'
                }], //联系人名称
                linkmanType: [{
                    required: true,
                    message: '请选择联系人类型',
                    trigger: 'change'
                }], //联系人类型
                linkmanCertNo: [{
                    required: true,
                    message: '请输入身份证号',
                    trigger: 'change'
                }], //身份证号
                linkmanMobilePhone: [{
                    required: true,
                    validator: checkPhone,
                    trigger: 'change'
                }], //手机号码
                linkmanAddr: [{
                    required: true,
                    message: '请输入联系人住址',
                    trigger: 'change'
                }], //联系人住址
            },
            enclosureFileListArr: [], // 附件上传时，从电脑中选择的文件列表            
            isShowSelectDropBox: false, // 是否展示单位列表选择框
            unitList: [], // dialog-单位列表
        }
    },
    mounted: function () {
        // 获取联系人id，存在即编辑状态，需要初始化数据
        this.linkmanId = this.QueryString('linkmanId');
        if (this.linkmanId) {
            this.isEdit = true;
            this.verticalTabData.push(
                {
                    label: '单位信息',
                    target: 'units'
                }
            );
            this.fetchRowDetail(this.linkmanId);
            this.getUnitData();
        } else {
            this.isEdit = false;
        }
    },
    methods: {
        // 选中下拉框中的一项单位数据
        selectDropItem: function (item) {
            this.manForm.applicant = item.applicant;
            this.manForm.unitInfoId = item.unitInfoId;
            this.hideSelectDropBox()
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
        // 删除单位
        delUnit: function (row) {
            var _that = this;

            request('', {
                url: ctx + 'rest/unit/delete/by/' + _that.linkmanId,
                type: 'post',
                data: {
                    unitInfoId: row.unitInfoId
                }
            }, function (result) {
                if (result.success) {
                    _that.getUnitData();

                    _that.$message({
                        message: '删除成功！',
                        type: 'success'
                    });

                    _that.loading = false;
                } else {
                    _that.$message({
                        message: '删除失败！',
                        type: 'error'
                    });
                    _that.loading = false;
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败！',
                    type: 'error'
                });
                _that.loading = false;
            });
        },
        // 获取页面的URL参数
        QueryString: function (val) {
            var svalue = location.search.match(new RegExp("[\?\&]" + val + "=([^\&]*)(\&?)", "i"));
            return svalue ? svalue[1] : svalue;
        },
        targetCollapse: function (target, ind) { // 纵向导航点击事件
            this.activeTab = ind;
            $(document).scrollTop($('#' + target).offset().top)
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
                    if (ts.isEdit) {
                        _saveForm.append('linkmanInfoId', ts.linkmanId);
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
                                if (res.message != '') {
                                    var _resetData = {};
                                    _resetData.linkmanInfoId = res.message;
                                    ts.loading = true;
                                    request('', {
                                        url: ctx + 'rest/linkman/generatePassword',
                                        type: 'get',
                                        data: _resetData
                                    }, function (res) {
                                        ts.loading = false;
                                        if (res.success) {
                                            var data = res.content;
                                            // ts.apiMessage('生成密码成功！', 'success');
                                            // 打印回执
                                            // var url = ctx + "/aea/hi/receive/toPrintPageForPassword.do?type=linkman";
                                            // window.open(ctx + '/file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url));
                                            var url = ctx + "/rest/receive/savePasswordPdf/" + data.loginName + "/" + data.loginPwd;
                                            window.open(ctx + 'preview/pdfjs/web/viewer.html?file=' + encodeURIComponent(url));
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
        // 查询单位信息列表数据
        getUnitData: function () {
            var ts = this;
            ts.loading = true;
            request('', {
                url: ctx + 'rest/unit/list/' + this.linkmanId,
                type: 'get',
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.tableData = res.content;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
    }
})

