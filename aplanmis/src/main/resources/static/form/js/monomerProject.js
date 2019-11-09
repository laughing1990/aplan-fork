(function () {
    var vm = new Vue({
        el: '#monomerProject',
        data: function () {
            // 输入为数字 大于等于0（浮点数）
            var checkNumFloat = function (rule, value, callback) {
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
            var checkMissNum = function (rule, value, callback) {
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

            return {
                activeNames: ['1'],
                projInfoId: '', // 父项目id
                childInfoId: '', // list对应项数据的id
                tableData: [],
                formTitle: '新增单体工程',
                formData: {},
                isShowDialog: false,
                isAdd: '1', // 增加or编辑
                rules: {
                    projName: { required: true, message: "工程名称不得为空！", trigger: ["change"] },
                    aboveGround: [{ required: true, validator: checkNumFloat, trigger: 'change' },
                    { required: true, message: "地上面积不得为空！", trigger: ["change"] },
                    ],
                    underGround: [{ required: true, validator: checkNumFloat, trigger: 'change' },
                    { required: true, message: "地下面积不得为空！", trigger: ["change"] }]
                    ,
                    aboveFloor: [{ required: true, validator: checkMissNum, trigger: 'change' },
                    { required: true, message: "地上层数不得为空！", trigger: ["change"] }],
                    underFloor: [{ required: true, validator: checkMissNum, trigger: 'change' },
                    { required: true, message: "地下层数不得为空！", trigger: ["change"] }],
                    aboveHeight: [{ required: true, validator: checkNumFloat, trigger: 'change' },
                    { required: true, message: "地上建筑高度不得为空！", trigger: ["change"] }],
                    underDepth: [{ required: true, validator: checkNumFloat, trigger: 'change' },
                    { required: true, message: "地下建筑深度不得为空！", trigger: ["change"] }],
                    length: [{ required: true, validator: checkNumFloat, trigger: 'change' },
                    { required: true, message: "长度不得为空！", trigger: ["change"] }],
                    span: [{ required: true, validator: checkNumFloat, trigger: 'change' },
                    { required: true, message: "跨度不得为空！", trigger: ["change"] }],
                },
            }
        },
        created: function () {
            // this.projInfoId = this.getUrlParam('projInfoId');
            this.projInfoId = '012c4996-b14f-42c4-8d87-b2f347b27276';
        },
        mounted: function () {
            this.getDetail();
        },
        methods: {
            // 重置弹窗表单数据
            resetForm: function () {
                this.$refs.form.resetFields();
            },
            cancleForm: function () {
                this.resetForm();
                this.isShowDialog = false;
            },
            // 关闭弹窗前回调
            handleClose: function (done) {
                this.resetForm();
                done();
            },
            // ,新增数据
            addData: function () {
                this.isShowDialog = true;
                this.isAdd = '1';
                this.formTitle = '新增单体工程';
            },
            // 编辑数据
            editData: function (item) {
                this.isShowDialog = true;
                this.formData = JSON.parse(JSON.stringify(item));
                this.isAdd = '0';
                this.formTitle = '编辑单体工程';
                this.childInfoId = item.projInfoId;
            },
            // 删除数据
            delData: function (item) {
                var _that = this;
                var params = {
                    childProjInfoId: item.projInfoId
                }

                request('', {
                    url: ctx + 'rest/project/delete/childProject',
                    data: params,
                    type: 'post'
                }, function (data) {
                    if (data.success) {
                        _that.$message({
                            message: '删除成功',
                            type: 'success'
                        });

                        _that.getDetail();
                    } else {
                        _that.$message({
                            message: data.message ? data.message : '删除失败',
                            type: 'error'
                        });
                    };
                }, function (msg) {
                    _that.$message({
                        message: msg.message ? msg.message : '服务请求失败',
                        type: 'error'
                    });
                });
            },
            // from数据提交
            submitForm: function () {
                var _that = this;
                var params = this.formData;

                _that.$refs['form'].validate(function (valid) {
                    if (valid) {
                        if (_that.isAdd === '1') {
                            delete params.projInfoId;
                            params.parentProjId = _that.projInfoId;
                            // 新增
                            request('', {
                                url: ctx + 'rest/project/save/childProject',
                                data: JSON.stringify(params),
                                type: 'post',
                                ContentType: 'application/json'
                            }, function (data) {
                                if (data.success) {
                                    _that.$message({
                                        message: '操作成功',
                                        type: 'success'
                                    });

                                    _that.resetForm();
                                    _that.isShowDialog = false;
                                    _that.getDetail();
                                } else {
                                    _that.$message({
                                        message: data.message ? data.message : '操作失败',
                                        type: 'error'
                                    });
                                };
                            }, function (msg) {
                                _that.$message({
                                    message: msg.message ? msg.message : '服务请求失败',
                                    type: 'error'
                                });
                            });
                        } else {
                            delete params.parentProjId;
                            params.projInfoId = _that.projInfoId;
                            // 编辑                           
                            request('', {
                                url: ctx + 'rest/project/edit/childProject',
                                data: JSON.stringify(params),
                                type: 'post',
                                ContentType: 'application/json'
                            }, function (data) {
                                if (data.success) {
                                    _that.$message({
                                        message: '操作成功',
                                        type: 'success'
                                    });

                                    _that.resetForm();
                                    _that.isShowDialog = false;
                                    _that.getDetail();
                                } else {
                                    _that.$message({
                                        message: data.message ? data.message : '操作失败',
                                        type: 'error'
                                    });
                                };
                            }, function (msg) {
                                _that.$message({
                                    message: msg.message ? msg.message : '服务请求失败',
                                    type: 'error'
                                });
                            });
                        }
                    }
                });
            },
            // save: function () {

            // },
            // 获取table相关数据
            getDetail: function () {
                var _that = this;
                var params = {
                    projInfoId: this.projInfoId
                };

                request('', {
                    url: ctx + 'rest/project/childProjectList/' + this.projInfoId,
                    data: {},
                    type: 'get'
                }, function (data) {
                    if (data.success) {
                        _that.tableData = data.content;
                    } else {
                        _that.$message({
                            message: data.message ? data.message : '信息查询失败',
                            type: 'error'
                        });
                    };
                }, function (msg) {
                    _that.$message({
                        message: msg.message ? msg.message : '服务请求失败',
                        type: 'error'
                    });
                });
            },
            // 获取页面的URL参数
            getUrlParam: function (val) {
                var svalue = location.search.match(new RegExp("[\?\&]" + val + "=([^\&]*)(\&?)", "i"));
                return svalue ? svalue[1] : svalue;
            },
        }
    })
})()
