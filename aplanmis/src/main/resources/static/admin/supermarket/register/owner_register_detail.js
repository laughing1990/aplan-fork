var vm = new Vue({
    el: '#app',
    data: function () {
        return {
            loading: false,
            activeNames: ['1', '2', '3', '4'],
            form: {
                unitInfo: {},
                contactManList: [],
                authorManInfo: {
                    certinsts: []
                },
                unitFileList: [],
                authorManFileList: []
            },
            auditForm:{
                auditFlag: "",
                memo: '',
                unitInfoId: ''
            },
            filePreviewCount: 0,
            dialogFormVisible: false
        }
    },
    mounted: function () {
        this.getDetail();
    },
    methods: {
        // 获取项目详情
        getDetail: function () {
            var vm = this;
            var url = 'supermarket/register/getOwnerRegisterDetail';
            var unitInfoId = this.getUrlParam('unitInfoId');

            vm.loading = true;
            request('', {
                url: ctx + url,
                type: 'get',
                data: { unitInfoId: unitInfoId },
            }, function (res) {
                if (res.success) {
                    vm.form = res.content;
                    vm.auditForm.unitInfoId = vm.form.unitInfo.unitInfoId;

                    vm.loading = false;
                } else {
                    vm.loading = false;
                    vm.$message({
                        message: res.message ? res.message : '信息获取失败',
                        type: 'error'
                    });
                }

            }, function (msg) {
                vm.loading = false;
                var result=res.responseJSON;
                vm.$message({
                    message: result.message ? result.message : '服务请求失败',
                    type: 'error'
                });
            })
        },
        // 获取页面的URL参数
        getUrlParam: function (val) {
            var svalue = location.search.match(new RegExp("[\?\&]" + val + "=([^\&]*)(\&?)", "i"));
            return svalue ? svalue[1] : svalue;
        },
        // 预览电子件
        filePreview: function (data, flag) { // flag==pdf 查看施工图
            var detailId = data.fileId;
            var _that = this;
            var regText = /doc|docx|pdf|ppt|pptx|xls|xlsx|txt$/;
            var fileName = data.fileName;
            var fileType = this.getFileType(fileName);
            var flashAttributes = '';
            _that.filePreviewCount++
            if (flag == 'pdf') {
                var tempwindow = window.open(); // 先打开页面
                setTimeout(function () {
                    tempwindow.location = ctx + 'cod/drawing/drawingCheck?detailId=' + detailId;
                }, 1000)
            } else {
                if (fileType == 'pdf') {
                    var tempwindow = window.open(); // 先打开页面
                    setTimeout(function () {
                        tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
                    }, 1000)
                } else if (regText.test(fileType)) {
                    // previewPdf/pdfIsCoverted
                    _that.mloading = true;
                    request('', {
                        url: ctx + 'previewPdf/pdfIsCoverted?detailId=' + detailId,
                        type: 'get',
                    }, function (result) {
                        if (result.success) {
                            _that.mloading = false;
                            var tempwindow = window.open(); // 先打开页面
                            setTimeout(function () {
                                tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
                            }, 1000)
                        } else {
                            if (_that.filePreviewCount > 9) {
                                confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                                    _that.filePreviewCount = 0;
                                    _that.filePreview(data);
                                }, function () {
                                    _that.filePreviewCount = 0;
                                    _that.mloading = false;
                                    return false;
                                }, '确定', '取消', 'warning', true)
                            } else {
                                setTimeout(function () {
                                    _that.filePreview(data);
                                }, 1000)
                            }
                        }
                    }, function (msg) {
                        _that.mloading = false;
                        _that.$message({
                            message: '文件预览失败',
                            type: 'error'
                        });
                    })
                } else {
                    _that.mloading = false;
                    var tempwindow = window.open(); // 先打开页面
                    setTimeout(function () {
                        tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
                    }, 1000)
                }
            }
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
        //下载单个附件
        downOneFile: function (data) {
            window.open(ctx + 'supermarket/purchase/mat/att/download?detailIds=' + data.fileId, '_blank')
        },
        // icon 字体文件颜色
        getIconColor: function (type) {
            return __STATIC.getIconColor(type || "DEFAULT");
        },
        examineService: function (value) {
            var _this = this;
            _this.auditForm.auditFlag = value;
            _this.dialogFormVisible = true;

        },
        changeAuditFlag: function () {
            var _this = this;
            _this.dialogFormVisible = false;
            var url = ctx + '/supermarket/register/examineOwnerUnitService';
            request('', {
                url: url,
                data: _this.auditForm,
                type: 'post'
            }, function (data) {
                console.log(data)
                if (data.success) {
                    _this.toPre()
                }
            }, function (msg) {
            })
        },
        //返回上一页
        toPre: function () {
            window.location.href = '/aplanmis-front/supermarket/register/index.html'

        }
    }
});