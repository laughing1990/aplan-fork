var vm = new Vue({
    el: '#approve',
    data: function () {
        var validateMoney = function (rule, value, callback) {
            var pattern = /^(((\d|[1-9]\d)(\.\d{1,2})?)|100(\.0{1,2})?)$/;
            if (value === '' || value === undefined || value === null) {
                callback(new Error('该输入项为必输项！'));
            } else if (!pattern.test(value)) {
                callback(new Error('请输入正确的资金来源比例！'));
            } else {
                callback();
            }
        };
        return {
            loading: false, // 页面加载遮罩
            activeNames: ['1'], // el-collapse 默认展开列表
            buildTypeList: [], // 项目分类列表
            projNatureList: [], // 项目性质列表
            projFunctionList: [], // 项目用途列表
            scaleTypeList: [], // 建设规模类型列表
            projLevelList: [], // 立项级别列表
            exProjFrom: {},
            exProjFromRules: {
                buildType: [
                    { required: true, message: '请选择项目分类', trigger: 'change' }
                ],
                projNature: [
                    { required: true, message: '请选择项目性质', trigger: 'change' }
                ],
                projFunction: [
                    { required: true, message: '请选择项目用途', trigger: 'change' }
                ],
                scaleType: [
                    { required: true, message: '请选择建设规模类型', trigger: 'change' }
                ],
                scaleContent: [
                    { required: true, message: '请填写建设规模', trigger: 'blur' },
                    { max: 500, message: '最大长度500个字符', trigger: 'blur' }
                ],
                projNum: [
                    { required: true, message: '请填写立项文号', trigger: 'blur' }
                ],
                projLevel: [
                    { required: true, message: '请选择立项级别', trigger: 'change' }
                ],
                approvalDept: [
                    { required: true, message: '请填写立项批准机关', trigger: 'blur' }
                ],
                approvalTime: [
                    { required: true, message: '请选择立项批准时间', trigger: 'blur' }
                ],
                govFinance: [
                    { required: true, validator: validateMoney, trigger: 'blur' }
                ],
                stateEnterprice: [
                    { required: true, validator: validateMoney, trigger: 'blur' }
                ],
                stateInvestment: [
                    { required: true, validator: validateMoney, trigger: 'blur' }
                ],
                internationalInvestment: [
                    { required: true, validator: validateMoney, trigger: 'blur' }
                ],
                collectiveInvestment: [
                    { required: true, validator: validateMoney, trigger: 'blur' }
                ],
                foreignInvestment: [
                    { required: true, validator: validateMoney, trigger: 'blur' }
                ],
                hkInvestment: [
                    { required: true, validator: validateMoney, trigger: 'blur' }
                ],
                privateInvestment: [
                    { required: true, validator: validateMoney, trigger: 'blur' }
                ],
                otherInvestment: [
                    { required: true, validator: validateMoney, trigger: 'blur' }
                ],
                govOrgConfirmTime: [
                    { required: true, message: '请选择建设行业主管部门确认时间', trigger: 'blur' }
                ],
                govOrgCode: [
                    { required: true, message: '请填写建设行业主管部门确认的行政单位机构代码', trigger: 'blur' }
                ],
                govOrgName: [
                    { required: true, message: '请填写建设行业主管部门确认的行政单位名称', trigger: 'blur' }
                ],
                govAreaCode: [
                    { required: true, message: '请填写建设行业主管部门确认的行政单位区域码', trigger: 'blur' }
                ]
            }
        }
    },
    mounted: function () {
        var _that = this;
        _that.exProjFrom.projInfoId = projInfoId;
        _that.getDictList('XM_JZLX,PROJECT_PROPERTY,XM_FUNCTION,XM_SCALE_TYPE,XM_PROJECT_LEVEL');
        _that.getExProjForm();
    },
    computed: {
        total: function () {
            var _that = this;
            var param = ['govFinance', 'stateEnterprice', 'stateInvestment', 'internationalInvestment',
                'collectiveInvestment', 'foreignInvestment', 'hkInvestment', 'privateInvestment' ,'otherInvestment'];

            var sum = 0;
            for (var i = 0; i < param.length; i++) {
                var value = _that.exProjFrom[param[i]];

                if (value !== '' && value !== undefined && value !== null && !isNaN(value)) {
                    sum += parseFloat(value);
                }
            }
            return sum;
        }
    },
    methods: {
        // 获取数据字典数据
        getDictList: function (code) {
            var _that = this;
            request('opus-admin', {
                url: ctx + 'rest/dict/code/multi/items/list',
                type: 'get',
                data: {'dicCodeTypeCodes': code}
            }, function (result) {
                if (result.content) {
                    _that.buildTypeList = result.content.XM_JZLX;
                    _that.projNatureList = result.content.PROJECT_PROPERTY;
                    _that.projFunctionList = result.content.XM_FUNCTION;
                    _that.scaleTypeList = result.content.XM_SCALE_TYPE;
                    _that.projLevelList = result.content.XM_PROJECT_LEVEL;
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },
        getExProjForm: function () {
            var _that = this;
            var projInfoId = _that.exProjFrom.projInfoId;
            if (projInfoId !== '' && projInfoId !== undefined && projInfoId !== null) {
                _that.loading = true;
                request('', {
                    url: ctx + 'rest/form/ex/project/' + projInfoId,
                    type: 'get'
                }, function (data) {
                    _that.loading = false;
                    if (data.success) {
                        if (data.content) {
                            _that.exProjFrom = data.content;
                        }
                    } else {
                        _that.$message({
                            message: data.message ? data.message : '获取表单内容失败',
                            type: 'error'
                        });
                    }
                }, function (msg) {
                    _that.loading = false;
                    _that.$message({
                        message: msg.message ? msg.message : '服务请求失败',
                        type: 'error'
                    });
                })
            } else {
                _that.$message({
                    message: 'projInfoId为空',
                    type: 'error'
                });
            }
        },
        saveExProjForm: function () {
            var _that = this;
            if (_that.total !== 100) {
                _that.$message({
                    message: '项目资金来源构成比例需为100%',
                    type: 'error'
                });
                return;
            }

            _that.$refs['exProjFrom'].validate(function (valid) {
                if (valid) {
                    request('', {
                        url: ctx + 'rest/form/ex/project/save',
                        data: _that.exProjFrom,
                        type: 'post'
                    }, function (data) {
                        if (data.success) {
                            _that.$message({
                                message: data.message ? data.message : '信息保存成功',
                                type: 'success'
                            });
                        } else {
                            _that.$message({
                                message: data.message ? data.message : '信息保存失败',
                                type: 'error'
                            });
                        }
                    }, function (msg) {
                        _that.$message({
                            message: msg.message ? msg.message : '服务请求失败',
                            type: 'error'
                        });
                    })
                }
            });
        }
    }
});