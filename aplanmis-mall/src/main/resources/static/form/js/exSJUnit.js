var vm = new Vue({
    el: '#approve',
    data: function() {
        var checkNumFloat = function(rule, value, callback) {
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
        var checkMissValue = function(rule, value, callback) {
            if (value === '' || value === undefined || value === null) {
                callback(new Error('该输入项为必输项！'));
            } else if (value.toString().trim() === '') {
                callback(new Error('该输入项为必输项！'));
            } else {
                callback();
            }
        };
        return {
            C_PRJ_ENT_TYPE: {
                gongchengzongchengbao: '28', //工程总承包单位
                shigongzongbao: '22', //施工总包
                shigongfenbao: '23', //施工分包
                laowufenbao: '24', //劳务分包
                gongchengjianli: '5' //监理单位
            },
            C_PRJ_PERSON_POST: {
                gongchengzongchengbao: '1', //工程总承包单位
                shigongzongbao: '1', //施工总包
                shigongfenbao: '4', //施工分包
                laowufenbao: '4', //劳务分包
                gongchengjianli: '3' //工程监理
            },
            projInfoId: '',
            prjEntType: '', //项目主体类型
            loadingThemeIdList: false, // 查询项目类型列表
            loading: false, // 页面加载遮罩
            verticalTabData: [ // 左侧纵向导航数据
                {
                    label: '施工和监理单位信息',
                    target: 'baseInfo'
                },
            ],
            addEditManModalTitle: '新增联系人',
            addEditManModalFlag: 'edit',
            addEditManModalShow: false, // 是否显示新增编辑联系人窗口
            addEditManform: {
                unitInfoId: '',
            }, // 新增编辑联系人信息
            addEditManPerform: {}, // 新增编辑联系人信息
            projInfoId: '', // 查询项目id
            addLinkManRules: { // 新增编辑联系人校验
                linkmanName: [
                    { required: true, validator: checkMissValue, trigger: 'blur' },
                ],
                linkmanCertNo: [
                    { required: true, validator: checkMissValue, trigger: 'blur' },
                ],
                linkmanMobilePhone: [
                    { required: true, validator: checkPhoneNum, trigger: 'blur' },
                ]
            },
            activeNames: ['1', '2', '3', '4', '5', '6'], // el-collapse 默认展开列表
            unitInfoShowFromRules: { // 基本信息校验
                provinceProjCode: [
                    { required: true, message: '请输入省级项目编号！', trigger: ['change', 'blur'] },
                ],
                contractStartBuildTime: [
                    { required: true, message: '请选择合同开工时间！', trigger: ['change'] },
                ],
                contractEndBuildTime: [
                    { required: true, message: '请选择合同竣工时间！', trigger: ['change'] },
                ],
                structureSystem: [
                    { required: true, message: '请选择结构体系！', trigger: ['change'] },
                ],
                buildArea: [
                    { validator: checkNumFloat, trigger: ['blur'] },
                    { required: true, message: '请填写施工面积！', trigger: ['change'] }
                ]
            },
            gongchengzongFromRules: { //工程总承包单位信息校验
                organizationalCode: [
                    { required: true, message: '请输入组织机构代码!', trigger: ['change'] },
                ],
                unifiedSocialCreditCode: [
                    { required: true, message: '请输入统一社会信用代码!', trigger: ['change'] },
                ],
                applicant: [
                    { required: true, message: '请输入单位名称！', trigger: ['change'] },
                ],
                idrepresentative: [
                    { required: true, message: '请输入法定代表人！', trigger: ['change'] },
                ],
                linkmanName: [
                    { required: true, message: '请输入工程总承包项目经理！', trigger: ['change'] },
                ],
                idmobile: [
                    { validator: checkNumFloat, trigger: ['blur'] },
                    { required: true, message: '请填写法定代表人联系电话！', trigger: ['change'] }
                ],
                linkmanMobilePhone: [
                    { validator: checkPhoneNum, trigger: ['blur'] },
                    { required: true, message: '请填写工程总承包项目经理联系电话！', trigger: ['change'] }
                ]
            },
            rulesApplyShigongzongFrom: { //施工总承包单位校验
                organizationalCode: [
                    { required: true, message: '请输入组织机构代码!', trigger: ['change'] },
                ],
                unifiedSocialCreditCode: [
                    { required: true, message: '请输入统一社会信用代码!', trigger: ['change'] },
                ],
                applicant: [
                    { required: true, message: '请输入单位名称！', trigger: ['change'] },
                ],
                // qualLevelName:[
                //     { required: true,message: '请输入资质等级！', trigger: ['change'] },
                // ],
                certinstCode: [
                    { required: true, message: '请输入证书编号！', trigger: ['change'] },
                ],
                unitSafeLicenceNum: [
                    { required: true, message: '请输入安全生产许可证编号！', trigger: ['change'] },
                ],
                idrepresentative: [
                    { required: true, message: '请输入法定代表人！', trigger: ['change'] },
                ],
                idmobile: [
                    { validator: checkPhoneNum, trigger: ['blur'] },
                    { required: true, message: '请填写法定代表人联系电话！', trigger: ['change'] }
                ],
                linkmanName: [
                    { required: true, message: '请输入项目负责人（项目经理）！', trigger: ['change'] },
                ],
                registerNum: [
                    { required: true, message: '请输入注册编号！', trigger: ['change'] },
                ],
                personSafeLicenceNum: [
                    { required: true, message: '请输入安全生产考核合格证号！', trigger: ['change'] },
                ],
                linkmanMobilePhone: [
                    { required: true, message: '请输入项目负责人（项目经理）联系电话！', trigger: ['change'] },
                ],
                personSetting: {
                    linkmanCertNo: [
                        { required: false, message: '请输入身份证号码！', trigger: ['change'] },
                    ]
                }
            },
            rulesShigongzhuanyefenbaoFrom: {
                organizationalCode: [
                    { required: true, message: '请输入组织机构代码!', trigger: ['change'] },
                ],
                unifiedSocialCreditCode: [
                    { required: true, message: '请输入统一社会信用代码!', trigger: ['change'] },
                ],
                applicant: [
                    { required: true, message: '请输入单位名称!', trigger: ['change'] },
                ],
                // qualificationLevel:[
                //     { required: true,message: '请输入资质等级!', trigger: ['change'] },
                // ],
                certinstCode: [
                    { required: true, message: '请输入证书编号!', trigger: ['change'] },
                ],
                unitSafeLicenceNum: [
                    { required: true, message: '请输入安全生产许可证编号!', trigger: ['change'] },
                ],
                idrepresentative: [
                    { required: true, message: '请输入法定代表人!', trigger: ['change'] },
                ],
                idmobile: [
                    { required: true, message: '请输入法定代表人联系电话!', trigger: ['change'] },
                ],
                linkmanName: [
                    { required: true, message: '请输入专业分包技术负责人!', trigger: ['change'] },
                ],
                registerNum: [
                    { required: true, message: '请输入注册编号!', trigger: ['change'] },
                ],
                personSafeLicenceNum: [
                    { required: true, message: '请输入安全生产考核合格证号!', trigger: ['change'] },
                ],
                linkmanMobilePhone: [
                    { required: true, message: '请输入专业分包技术负责人联系电话!', trigger: ['change'] },
                ]
            },
            rulesShigonglaowufenbaoFrom: {
                organizationalCode: [
                    { required: true, message: '请输入组织机构代码!', trigger: ['change'] },
                ],
                unifiedSocialCreditCode: [
                    { required: true, message: '请输入统一社会信用代码!', trigger: ['change'] },
                ],
                applicant: [
                    { required: true, message: '单位名称!', trigger: ['change'] },
                ],
                certinstCode: [
                    { required: true, message: '请输入证书编号!', trigger: ['change'] },
                ],
                unitSafeLicenceNum: [
                    { required: true, message: '请输入安全生产许可证编号!', trigger: ['change'] },
                ],
                idrepresentative: [
                    { required: true, message: '请输入法定代表人!', trigger: ['change'] },
                ],
                idmobile: [
                    { required: true, message: '请输入法定代表人联系电话!', trigger: ['change'] },
                ],
                linkmanName: [
                    { required: true, message: '请输入劳务分包技术负责人!', trigger: ['change'] },
                ],
                registerNum: [
                    { required: true, message: '请输入注册编号!', trigger: ['change'] },
                ],
                personSafeLicenceNum: [
                    { required: true, message: '请输入安全生产考核合格证号!', trigger: ['change'] },
                ],
                linkmanMobilePhone: [
                    { required: true, message: '请输入劳务分包技术负责人联系电话!', trigger: ['change'] },
                ]
            },
            rulesJianliFrom: {
                organizationalCode: [
                    { required: true, message: '请输入组织机构代码!', trigger: ['change'] },
                ],
                unifiedSocialCreditCode: [
                    { required: true, message: '请输入统一社会信用代码!', trigger: ['change'] },
                ],
                applicant: [
                    { required: true, message: '请输入单位名称!', trigger: ['change'] },
                ],
                unitType: [
                    { required: true, message: '请输入项目主体类型!', trigger: ['change'] },
                ],
                isGd: [
                    { required: true, message: '请选择是否省内企业!', trigger: ['change'] },
                ],
                linkmanInfoId: [
                    { required: true, message: '请输入项目总监姓名!', trigger: ['change'] },
                ],
                linkmanCertNo: [
                    { required: true, message: '请输入项目总监身份证号码!', trigger: ['change'] },
                ]
            },
            loadingUnitLinkMan: false,
            unitInfoShowFrom: {
                aeaExProjBuildUnitInfo: '',
                buildArea: "",
                buildId: "",
                contractEndBuildTime: "",
                contractStartBuildTime: "",
                projInfoId: "",
                provinceProjCode: "",
                quaCheckNum: "",
                structureSystem: ""
            }, //单位基本信息
            showVerLen: 1, // 显示左侧纵向导航栏的长度
            activeTab: 0,
            linkQuerySucc: false, // 项目代码工程编码是否可输入修改
            structureSystem: [], //结构体系
            projUnitLinkmanType: [], //人员类型
            chengbaoProjUnitLinkmanType: [], //承包单位人员类型
            fenbaoProjUnitLinkmanType: [], //分包单位人员类型
            jianliProjUnitLinkmanType: [], //监理单位人员类型
            professionCertType: [], //执业注册证类型
            jiangliLinkmanType: [], //监理承担角色
            titleCertNum: [], //职称等级
            qualLevelName: [], //资历等级
            unitLinkManOptions: [], //联系人
            applyShigongzongFrom: {
                registerNum: '',
                personSafeLicenceNum: '',
                personSetting: [{
                    linkmanType: '',
                    linkmanInfoId: '',
                    linkmanName: '',
                    safeLicenceNum: '',
                    professionCertType: '',
                    titleCertNum: '',
                    linkmanCertNo: ''
                }]
            }, // 施工总承包单位信息
            applyShigongzhuanyefenbaoFrom: {
                registerNum: '',
                personSafeLicenceNum: '',
                personSetting: [{
                    linkmanType: '',
                    linkmanInfoId: '',
                    linkmanName: '',
                    safeLicenceNum: '',
                    professionCertType: '',
                    titleCertNum: '',
                    linkmanCertNo: ''
                }],
            }, //施工专业分包单位信息
            applyShigonglaowufenbaoFrom: {
                registerNum: '',
                personSafeLicenceNum: '',
                personSetting: [{
                    linkmanType: '',
                    linkmanInfoId: '',
                    linkmanName: '',
                    safeLicenceNum: '',
                    professionCertType: '',
                    titleCertNum: '',
                    linkmanCertNo: ''
                }],
            }, //施工劳务分包单位信息
            applyJianliFrom: {
                registerNum: '',
                personSafeLicenceNum: '',
                personSetting: [{
                    linkmanType: '',
                    linkmanInfoId: '',
                    linkmanName: '',
                    safeLicenceNum: '',
                    professionCertType: '',
                    titleCertNum: '',
                    linkmanCertNo: '',
                }],
            }, //监理单位信息
            gongchengzongFrom: {}, //工程总承包单位信息
            exSJAllUnit: {
                aeaExProjBuildUnitInfo: '',
            }, //所有表单集合
            personIdList: [], //人员批量删除

        }
    },
    mounted: function() {
        var _that = this;
        _that.initPage();
        _that.getInfoByDataDictionary('C_TITLE,C_STRUCT_TYPE,C_PRJ_PERSON_POST,C_REG_LCN_TYPE,C_PRJ_ENT_TYPE,XM_DWLX,PROJ_UNIT_LINKMAN_TYPE');
        //_that.getQualLevel('danweizilidengji');
        _that.initExSJUnitFromPage();
    },
    methods: {
        getUrlParam: function(name) {
            var _that = this;
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return unescape(r[2]);
            } else {
                _that.$message({
                    message: '项目ID不能为空',
                    type: 'error'
                });
            }
            return null;
        },
        initPage: function() {
            var _that = this;
            _that.unitInfoShowFrom.projInfoId = _that.getUrlParam('projInfoId');
        },
        initExSJUnitFromPage: function() { //初始化表单页面
            var _that = this;
            var projInfoId = _that.unitInfoShowFrom.projInfoId;
            request('', {
                url: ctx + '/rest/from/exSJUnit/initExSJUnit',
                data: {
                    projInfoId: projInfoId
                },
                type: 'get',
            }, function(data) {
                if (data.content.unitInfoShowFrom) {
                    _that.unitInfoShowFrom.buildArea = data.content.unitInfoShowFrom.buildArea;
                    _that.unitInfoShowFrom.buildId = data.content.unitInfoShowFrom.buildId;
                    _that.unitInfoShowFrom.contractEndBuildTime = data.content.unitInfoShowFrom.contractEndBuildTime;
                    _that.unitInfoShowFrom.contractStartBuildTime = data.content.unitInfoShowFrom.contractStartBuildTime;
                    _that.unitInfoShowFrom.projInfoId = data.content.unitInfoShowFrom.projInfoId;
                    _that.unitInfoShowFrom.provinceProjCode = data.content.unitInfoShowFrom.provinceProjCode;
                    _that.unitInfoShowFrom.quaCheckNum = data.content.unitInfoShowFrom.quaCheckNum;
                    _that.unitInfoShowFrom.structureSystem = data.content.unitInfoShowFrom.structureSystem;
                }
                if (data.content.gongchengzongFrom) {
                    _that.gongchengzongFrom = data.content.gongchengzongFrom;
                }
                if (data.content.applyShigongzongFrom) {
                    _that.applyShigongzongFrom = data.content.applyShigongzongFrom;
                }
                if (data.content.applyShigongzhuanyefenbaoFrom) {
                    _that.applyShigongzhuanyefenbaoFrom = data.content.applyShigongzhuanyefenbaoFrom;

                }
                if (data.content.applyShigonglaowufenbaoFrom) {
                    _that.applyShigonglaowufenbaoFrom = data.content.applyShigonglaowufenbaoFrom;
                }
                if (data.content.applyJianliFrom) {
                    _that.applyJianliFrom = data.content.applyJianliFrom;
                }
            })
        },
        // 联网查询
        searchFromInternet: function(data, code, type) {
            var _this = this;
            if (!code) {
                _this.$message({
                    message: '请输入要查询的组织机构代码！',
                    type: 'error'
                });
                return false;
            }

            request('', {
                type: 'get',
                url: ctx + 'rest/form/project/webservice/getGdEnterpriseInfo',
                data: {
                    orgCode: code
                },
            }, function(res) {
                if (!res.success) {
                    _this.$message({
                        message: '未查询到该组织机构代码！',
                        type: 'error'
                    });
                    return false;
                }
                _this.$set(data, 'unitInfoId', res.content.unitInfoId);
                _this.$set(data, 'applicant', res.content.applicant);
                _this.$set(data, 'organizationalCode', res.content.organizationalCode);
                _this.$set(data, 'unifiedSocialCreditCode', res.content.unifiedSocialCreditCode);
                if(type == 'applyJianliFrom'){
                    _this.applyJianliFrom.unitType = '5';
                }

            }, function(err) {
                vm.$message.error('服务器错了哦!');
            })
        },
        // 新增人员设置
        addLinkmanTypes: function(row) {
            var dataType = {
                personSetting: [{
                    linkmanType: '',
                    linkmanInfoId: '',
                    linkmanName: '',
                    safeLicenceNum: '',
                    professionCertType: '',
                    titleCertNum: '',
                    linkmanCertNo: ''
                }],
            };
            row.push(dataType);
        },
        // 删除人员设置
        delLinkmanTypes: function(row, index) {
            var _that = this;
            if (row[index].projLinkmanId) {
                _that.personIdList.push(row[index].projLinkmanId)
            }
            row.splice(index, 1);
        },
        //保存（更新）表单信息
        saveOrUpdateSJUnitFrom:function() {
            var _that = this;
            _that.exSJAllUnit = JSON.parse(JSON.stringify(_that.unitInfoShowFrom));
            //_that.exSJAllUnit.creatTime = new Date(_that.exSJAllUnit.creatTime);
            var gongchengzong = JSON.parse(JSON.stringify(_that.gongchengzongFrom));
            gongchengzong.unitType = _that.C_PRJ_ENT_TYPE.gongchengzongchengbao;
            gongchengzong.linkmanType = _that.C_PRJ_PERSON_POST.gongchengzongchengbao;
            var shigongzong = JSON.parse(JSON.stringify(_that.applyShigongzongFrom));
            shigongzong.unitType = _that.C_PRJ_ENT_TYPE.shigongzongbao;
            shigongzong.linkmanType = _that.C_PRJ_PERSON_POST.shigongzongbao;
            var shigongzhuanyefenbao = JSON.parse(JSON.stringify(_that.applyShigongzhuanyefenbaoFrom));
            shigongzhuanyefenbao.unitType = _that.C_PRJ_ENT_TYPE.shigongfenbao;;
            shigongzhuanyefenbao.linkmanType = _that.C_PRJ_PERSON_POST.shigongfenbao;
            var shigonglaowufenbao = JSON.parse(JSON.stringify(_that.applyShigonglaowufenbaoFrom));
            shigonglaowufenbao.unitType = _that.C_PRJ_ENT_TYPE.laowufenbao;
            shigonglaowufenbao.linkmanType = _that.C_PRJ_PERSON_POST.laowufenbao;
            var applyJianli = JSON.parse(JSON.stringify(_that.applyJianliFrom));
            applyJianli.unitType = _that.C_PRJ_ENT_TYPE.gongchengjianli;
            applyJianli.linkmanType = _that.C_PRJ_PERSON_POST.gongchengjianli;
            var a = [gongchengzong, shigongzong, shigongzhuanyefenbao, shigonglaowufenbao, applyJianli];
            _that.exSJAllUnit.aeaExProjBuildUnitInfo = JSON.stringify(a);
            _that.$refs['unitInfoShowFrom'].validate(function(valid) {
                _that.$refs['gongchengzongFrom'].validate(function(valid) {
                    _that.$refs['applyShigongzongFrom'].validate(function(valid) {
                        _that.$refs['applyShigongzhuanyefenbaoFrom'].validate(function(valid) {
                            _that.$refs['applyShigonglaowufenbaoFrom'].validate(function(valid) {
                                _that.$refs['applyJianliFrom'].validate(function(valid) {
                                    if (valid) {
                                        _that.delPeronSetting();
                                        request('', {
                                            url: ctx + '/rest/from/exSJUnit/saveOrUpdateSJUnitInfo',
                                            data: _that.exSJAllUnit,
                                            type: 'post',

                                        }, function(data) {
                                            if (data.success) {
                                                _that.$message({
                                                    message: '保存成功',
                                                    type: 'success'
                                                });
                                            } else {
                                                _that.$message({
                                                    message: '保存失败',
                                                    type: 'error'
                                                });
                                            }
                                        })
                                    } else {
                                        _that.$message({
                                            message: '请输入必填字段',
                                            type: 'error'
                                        });
                                    }
                                })
                            })
                        })
                    })
                })
            })
        },
        delPeronSetting: function() {
            var _that = this;
            if (_that.personIdList && _that.personIdList != "") {
                var parm = _that.personIdList.join();
                request('', {
                    url: ctx + '/rest/from/exSJUnit/delPersonSetting',
                    data: {
                        parm: parm
                    },
                    type: 'get',
                }, function(data) {
                    if (data.success) {
                        console.log("删除成功")
                    } else {
                        _that.$message({
                            message: '删除人员失败',
                            type: 'error'
                        });
                    }
                })
            }
        },
        //从数据字典获取信息
        getInfoByDataDictionary: function(code) {
            var _that = this;
            request('', {
                url: ctx + 'rest/dict/code/multi/items/list',
                type: 'get',
                data: { 'dicCodeTypeCodes': code }
            }, function(data) {
                if (data.content) {
                    _that.structureSystem = data.content.C_STRUCT_TYPE;
                    _that.projUnitLinkmanType = data.content.PROJ_UNIT_LINKMAN_TYPE;
                    _that.projUnitLinkmanType.map(function(value, index) {
                        if (value.itemCode != "1") {
                            _that.chengbaoProjUnitLinkmanType.push(value);
                        }
                    });
                    _that.projUnitLinkmanType.map(function(value, index) {
                        if (value.itemCode != "4") {
                            _that.fenbaoProjUnitLinkmanType.push(value);
                        }
                    });
                    _that.projUnitLinkmanType.map(function(value, index) {
                        if (value.itemCode != "3") {
                            _that.jiangliLinkmanType.push(value);
                        }
                    });
                    _that.professionCertType = data.content.C_REG_LCN_TYPE;
                    _that.titleCertNum = data.content.C_TITLE;
                    _that.prjEntType = data.content.XM_DWLX;
                } else {
                    _that.$message({
                        message: '服务请求失败',
                        type: 'error'
                    });
                }
            })
        },
        //获取资历等级
        getQualLevel: function(code) {
            var _that = this;
            request('', {
                url: ctx + '/rest/from/exSJUnit/listQualLevel',
                type: 'get',
                data: { 'parentQualLevelId': code }
            }, function(data) {
                if (data.content) {
                    _that.qualLevelName = data.content;
                } else {
                    _that.$message({
                        message: '服务请求失败',
                        type: 'error'
                    });
                }
            })
        },
        // 根据单位id获取关联联系人
        getUnitLinkManList: function(row) {
            if (row.unitInfoId) {
                var _that = this;
                _that.loadingUnitLinkMan = true;
                request('', {
                    url: ctx + 'rest/linkman/list',
                    type: 'get',
                    data: {
                        keyword: '',
                        unitInfoId: row.unitInfoId ? row.unitInfoId : '',
                        projInfoId: _that.projInfoId,

                    }
                }, function(data) {
                    _that.loadingUnitLinkMan = false;
                    if (data.success) {
                        _that.unitLinkManOptions = data.content;
                    }
                }, function(msg) {
                    _that.loadingUnitLinkMan = false;
                });
            }
        },
        //单位名称模糊查询
        querySearchJiansheName: function(queryString, cb) {
            var _that = this;
            if (typeof(queryString) != 'undefined') queryString = queryString.trim();
            if (queryString != '' && queryString.length >= 2) {
                request('', {
                    url: ctx + '/rest/from/exSJUnit/list',
                    type: 'get',
                    data: { "keyword": queryString, "projInfoId": _that.projInfoId },
                }, function(result) {
                    if (result) {
                        _that.projNameSelect = result.content;

                        _that.projNameSelect.map(function(item) {
                            if (item) {
                                Vue.set(item, 'value', item.applicant);
                            }
                        })
                        var results = queryString ? _that.projNameSelect.filter(_that.createFilter(queryString)) : _that.projNameSelect;
                        // 调用 callback 返回建议列表的数据
                        cb(results);
                    }
                }, function(msg) {
                    cb([]);
                })
            } else {
                cb([]);
            }
        },
        // 项目名称过滤
        createFilter: function(queryString) {
            return function(projNameSelect) {
                return (projNameSelect.value.toLowerCase());
            };
        },
        //选择单位
        selUnitInfo: function(val, flag, index) {
            if (flag == 'shigongzongchengbao') {
                val.personSetting = JSON.parse(JSON.stringify(val.personSetting));
                this.applyShigongzongFrom = val;
            } else if (flag == 'gongchengzongFrom') {
                val.personSetting = JSON.parse(JSON.stringify(val.personSetting));
                this.gongchengzongFrom = val;
            } else if (flag == 'applyShigongzhuanyefenbaoFrom') {
                val.personSetting = JSON.parse(JSON.stringify(val.personSetting));
                this.applyShigongzhuanyefenbaoFrom = val;
            } else if (flag == 'applyShigonglaowufenbaoFrom') {
                val.personSetting = JSON.parse(JSON.stringify(val.personSetting));
                this.applyShigonglaowufenbaoFrom = val;
            } else if (flag == 'applyJianliFrom') {
                val.personSetting = JSON.parse(JSON.stringify(val.personSetting));
                this.applyJianliFrom = val;
                this.applyJianliFrom.unitType = '5';
                this.applyJianliFrom.linkmanType = '2';
            }
        },
        // 人员设置选择人员
        selTypeLinkman: function(typeData, manData) {
            typeData.linkmanName = manData.addressName;
            typeData.linkmanCertNo = manData.addressIdCard;
            typeData.linkmanInfoId = manData.addressId
        },
        //选择项目负责人
        selLinkman: function(data, ind1, type) {
            var _that = this;
            if (type == 'shigongzongchenbao') {
                if (data) {
                    _that.applyShigongzongFrom.linkmanName = data.addressName;
                    _that.applyShigongzongFrom.linkmanId = data.addressId;
                    _that.applyShigongzongFrom.linkmanInfoId = data.addressId;
                    _that.applyShigongzongFrom.linkmanMail = data.addressMail;
                    _that.applyShigongzongFrom.linkmanCertNo = data.addressIdCard;
                    _that.applyShigongzongFrom.linkmanMobilePhone = data.addressPhone;
                    _that.applyShigongzongFrom.registerNum = '';
                    _that.applyShigongzongFrom.personSafeLicenceNum = '';
                }
            }
            if (type == 'shigongzhuanyefenbao') {
                if (data) {
                    _that.applyShigongzhuanyefenbaoFrom.linkmanName = data.addressName;
                    _that.applyShigongzhuanyefenbaoFrom.linkmanId = data.addressId;
                    _that.applyShigongzhuanyefenbaoFrom.linkmanInfoId = data.addressId;
                    _that.applyShigongzhuanyefenbaoFrom.linkmanMail = data.addressMail;
                    _that.applyShigongzhuanyefenbaoFrom.linkmanCertNo = data.addressIdCard;
                    _that.applyShigongzhuanyefenbaoFrom.linkmanMobilePhone = data.addressPhone;
                    _that.applyShigongzhuanyefenbaoFrom.registerNum = '';
                    _that.applyShigongzhuanyefenbaoFrom.personSafeLicenceNum = '';
                }
            }
            if (type == 'shigonglaowufenbao') {
                if (data) {
                    _that.applyShigonglaowufenbaoFrom.linkmanName = data.addressName;
                    _that.applyShigonglaowufenbaoFrom.linkmanId = data.addressId;
                    _that.applyShigonglaowufenbaoFrom.linkmanInfoId = data.addressId;
                    _that.applyShigonglaowufenbaoFrom.linkmanMail = data.addressMail;
                    _that.applyShigonglaowufenbaoFrom.linkmanCertNo = data.addressIdCard;
                    _that.applyShigonglaowufenbaoFrom.linkmanMobilePhone = data.addressPhone;
                    _that.applyShigonglaowufenbaoFrom.registerNum = '';
                    _that.applyShigonglaowufenbaoFrom.personSafeLicenceNum = '';
                }
            }
            if (type == 'jianli') {
                if (data) {
                    _that.applyJianliFrom.linkmanName = data.addressName;
                    _that.applyJianliFrom.linkmanId = data.addressId;
                    _that.applyJianliFrom.linkmanInfoId = data.addressId;
                    _that.applyJianliFrom.linkmanMail = data.addressMail;
                    _that.applyJianliFrom.linkmanCertNo = data.addressIdCard;
                    _that.applyJianliFrom.linkmanMobilePhone = data.addressPhone;
                    _that.applyJianliFrom.linkmanType = '3';
                    _that.applyJianliFrom.registerNum = '';
                    _that.applyJianliFrom.personSafeLicenceNum = '';
                }
            }
            if (type == 'gongchengzongFrom') {
                if (data) {
                    _that.gongchengzongFrom.linkmanName = data.addressName;
                    _that.gongchengzongFrom.linkmanId = data.addressId;
                    _that.gongchengzongFrom.linkmanInfoId = data.addressId;
                    _that.gongchengzongFrom.linkmanMail = data.addressMail;
                    _that.gongchengzongFrom.linkmanCertNo = data.addressIdCard;
                    _that.gongchengzongFrom.linkmanMobilePhone = data.addressPhone;
                    _that.gongchengzongFrom.registerNum = '';
                    _that.gongchengzongFrom.personSafeLicenceNum = '';
                }
            }
            if(type == 'applyShigongzongFrom.personsetting'){
                if (data) {
                    _that.applyShigongzongFrom.linkmanName = data.addressName;
                    _that.applyShigongzongFrom.linkmanId = data.addressId;
                    _that.applyShigongzongFrom.linkmanInfoId = data.addressId;
                    _that.applyShigongzongFrom.linkmanMail = data.addressMail;
                    _that.applyShigongzongFrom.linkmanCertNo = data.addressIdCard;
                    _that.applyShigongzongFrom.linkmanMobilePhone = data.addressPhone;
                    _that.applyShigongzongFrom.registerNum = '';
                    _that.applyShigongzongFrom.personSafeLicenceNum = '';
                }
            }
        },
        // 删除联系人
        delLinkman: function(data, parentData, ind) {
            var _that = this;
            if (!data.addressId) {
                alertMsg('提示信息', '联系人ID为空', '关闭', 'warning', true);
                return;
            } else {
                confirmMsg('此操作影响：', '确定要删除该联系人吗？', function() {
                    request('', {
                        url: ctx + 'rest/linkman/delete/by/unit',
                        type: 'post',
                        data: { linkmanInfoId: data.addressId, unitInfoId: parentData.unitInfoId }
                    }, function(result) {
                        if (result.success) {
                            _that.$message({
                                message: '联系人删除成功',
                                type: 'success'
                            });
                        } else {
                            _that.$message({
                                message: result.message ? result.message : '联系人删除失败',
                                type: 'error'
                            });
                        }
                    }, function(msg) {
                        _that.$message({
                            message: msg.message ? msg.message : '删除失败',
                            type: 'error'
                        });
                    })
                }, function() {}, '确定', '取消', 'warning', true)
            }
        },
        // 新增编辑联系人信息
        addLinkman: function(data, parData) {
            var _that = this;
            _that.addEditManModalShow = true;
            _that.getUnitsListByProjInfoId();
            _that.addEditManPerform = parData
            if (!_that.projInfoId) {
                if (data) {
                    _that.addEditManModalTitle = '编辑联系人';
                    _that.addEditManModalFlag = 'edit';
                    if (!data.linkmanInfoId) {
                        alertMsg('提示信息', '请选择联系人！', '关闭', 'error', true);
                        return;
                    } else {
                        _that.backDLinkmanInfo(data, parData);
                    }
                } else {
                    _that.addEditManModalTitle = '新增联系人';
                    _that.addEditManform = {};
                    _that.addEditManModalFlag = 'add';
                    if (parData.unitInfoId) {
                        _that.addEditManform.unitInfoId = parData.unitInfoId;
                        _that.addEditManform.unitName = parData.applicant;
                    } else {
                        _that.addEditManform.unitInfoId = '';
                        _that.addEditManform.unitName = parData.applicant
                    }
                }
            } else {
                alertMsg('', '请先查出项目信息！', '关闭', 'error', true);
            }
        },
        // 根据项目ID查找关联的单位列表
        getUnitsListByProjInfoId: function() {
            var _that = this;
            _that.loading = true;
            if (_that.applyShigongzongFrom.unitInfoId) {
                var unitInfoId = _that.applyShigongzongFrom.unitInfoId;
            }
            request('', {
                url: ctx + 'rest/unit/list/by/' + unitInfoId,
                type: 'get',
            }, function(result) {
                if (result.success) {
                    _that.unitInfoIdList = result.content;
                    _that.loading = false;
                }
            }, function(msg) {
                _that.$message({
                    message: '服务请求失败！',
                    type: 'error'
                });
                _that.loading = false;
            });
        },
        // 反显联系人信息
        backDLinkmanInfo: function(data, parData) {
            var _that = this;
            if (data.linkmanInfoId) {
                request('', {
                    url: ctx + 'rest/linkman/one/' + data.linkmanInfoId,
                    type: 'get'
                }, function(result) {
                    if (result.success) {
                        _that.addEditManform = result.content;
                        _that.addEditManform.unitInfoId = parData.unitInfoId;
                        _that.addEditManform.unitName = parData.applicant;
                    }
                }, function(msg) {
                    alertMsg('', '服务请求失败', '关闭', 'error', true);
                });
            } else {
                _that.aeaLinkmanInfoList = {};
            }
        },
        // 保存联系人信息
        saveAeaLinkmanInfo: function(linkmanType) {
            var _that = this;
            _that.addEditManform.linkmanType = linkmanType
            _that.$refs['addEditManform'].validate(function(valid) {
                if (valid) {
                    _that.loading = true;
                    var url, msg;
                    if (_that.addEditManModalFlag == 'edit') {
                        url = 'rest/linkman/edit';
                        msg = '编辑联系人信息保存成功';
                    } else {
                        url = 'rest/linkman/save'
                        msg = '新增联系人信息保存成功';
                    }
                    request('', {
                        url: ctx + url,
                        type: 'post',
                        data: _that.addEditManform
                    }, function(result) {
                        if (result.success) {
                            _that.$message({
                                message: '保存成功',
                                type: 'success'
                            });
                            // _that.addEditManPerform.linkmanName = _that.addEditManform.linkmanName;
                            // _that.addEditManPerform.linkmanId = result.content;
                            // _that.addEditManPerform.linkmanMail = _that.addEditManform.linkmanMail;
                            // _that.addEditManPerform.linkmanCertNo = _that.addEditManform.linkmanCertNo;
                            // _that.addEditManPerform.linkmanMobilePhone = _that.addEditManform.linkmanMobilePhone;
                            _that.addEditManModalShow = false;
                            _that.loading = false;
                        }
                    }, function(msg) {
                        _that.$message({
                            message: msg.message ? msg.message : '保存失败！',
                            type: 'error'
                        });
                        _that.loading = false;
                    });
                } else {
                    _that.$message({
                        message: '请输入完整的联系人信息！',
                        type: 'error'
                    });
                    return false;
                }
            });
        },
        // 重置表单
        resetForm: function(formName) {
            this.$refs[formName].resetFields();
            this.selectMat = '';
        },

        //联系人联网查询
        searchLinkmanFromInternet:function (data,idCode) {
            var _this = this;
            if (!idCode) {
                _this.$message({
                    message: '请输入要查询的身份证号码！',
                    type: 'error'
                });
                return false;
            }

            request('', {
                type: 'get',
                url: ctx + 'rest/form/project/webservice/getGdPersonInfo',
                data: {
                    idNum: idCode
                },
            }, function(res) {
                if (!res.success) {
                    _this.$message({
                        message: '未查询到该身份人员！',
                        type: 'error'
                    });
                    return false;
                }
                _this.$set(data, 'linkmanName', res.content.linkmanName);
                _this.$set(data, 'linkmanMobilePhone', res.content.linkmanMobilePhone);
                _this.$set(data, 'linkmanOfficePhon', res.content.linkmanOfficePhon);
                _this.$set(data, 'linkmanFax', res.content.linkmanFax);
                _this.$set(data, 'linkmanMail', res.content.linkmanMail);
                _this.$set(data, 'linkmanAddr', res.content.linkmanAddr);
                _this.$set(data, 'linkmanMemo', res.content.linkmanMemo);
            }, function(err) {
                vm.$message.error('服务器异常');
            })
        }
    }
})