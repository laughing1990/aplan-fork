(function () {
    var vm = new Vue({
        el: '#biddingInfo',
        data: function () {
            return {
                projInfoId: '',
                activeNames: ['1'],
                biddingInfo: {
                    winBidUnits: [{
                        organizationalCode: '',
                        unifiedSocialCreditCode: '',
                        applicant: '',
                        unitType: '27'
                    }],
                    agencyUnits: [{
                        organizationalCode: '',
                        unifiedSocialCreditCode: '',
                        applicant: '',
                        unitType: '21'
                    }],
                    costUnits: [{
                        organizationalCode: '',
                        unifiedSocialCreditCode: '',
                        applicant: '',
                        unitType: '26'
                    }]
                },
                biddingInfoRules: {
                    winBidNoticeCode: { required: true, message: "中标通知书编号不得为空！", trigger: ["change"] },
                    bidSectionName: { required: true, message: "标段名称不得为空！", trigger: ["change"] },
                    bidSectionAddr: { required: true, message: "标段地址不得为空！", trigger: ["change"] },
                    unitAddr: { required: true, message: "单位地址不得为空！", trigger: ["change"] },
                    bidType: { required: true, message: "招标类型不得为空！", trigger: ["change"] },
                    bidMode: { required: true, message: "招标方式不得为空！", trigger: ["change"] },
                    winBidTime: { required: true, message: "中标日期不得为空！", trigger: ["change"] },
                    realWinBiddingMoney: { required: true, message: "实际中标金额不得为空！", trigger: ["change"] },
                },
                bidTypeOptions: [], // 招标类型下拉选项arr
                bidModeOptions: [], // 招标方式下拉选项arr
                govAreaCodeOptions: [], // 招投标确认的行政单位区域码下拉选项arr
                projectType: [], // 项目主体类型下拉选项arr
                isShowFoot: false, // 是否显示‘招标代理机构’和‘造价咨询单位’
                projNameSelect: [], // 下拉选择项目名数组
            }
        },
        created: function () {
            this.projInfoId = this.getUrlParam('projInfoId');
            // this.projInfoId = 'ceb078f2-4f57-48d1-9d5e-68137de3e704';
        },
        mounted: function () {
            this.getProjTypeNature('C_TENDER_TYPE,C_AGENT_TYPE,XM_DWLX');
            this.getGovAreaCode();
            if (this.projInfoId) this.getDetail();
        },
        methods: {
            // 招投标信息模块保存
            saveBidding: function () {
                var _that = this;
                var params = this.biddingInfo;
                params.projInfoId = this.projInfoId;

                if (params.bidMode == '3') {
                    // 直接委托
                    params.agencyUnits.splice(0, 1);
                    params.costUnits.splice(0, 1);
                }

                _that.$refs['biddingInfo'].validate(function (valid) {
                    if (valid) {
                        request('', {
                            url: ctx + 'rest/form/project/bid/saveAeaExProjBid',
                            data: JSON.stringify(params),
                            type: 'post',
                            ContentType: 'application/json'
                        }, function (data) {
                            if (data.success) {
                                _that.$message({
                                    message: '信息保存成功',
                                    type: 'success'
                                });

                                _that.biddingInfo = data.content;
                            } else {
                                _that.$message({
                                    message: data.message ? data.message : '信息保存失败',
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
                });
            },
            // 招投标信息模块信息回显
            getDetail: function () {
                var _that = this;
                var params = {
                    projId: this.projInfoId
                };

                request('', {
                    url: ctx + 'rest/form/project/bid/getAeaExProjBidByProjId',
                    data: params,
                    type: 'get'
                }, function (data) {
                    if (data.success) {
                        _that.biddingInfo = data.content;

                        // 中标单位
                        if (!_that.biddingInfo.winBidUnits.length) {
                            _that.biddingInfo.winBidUnits.splice(0, 1, {
                                organizationalCode: '',
                                unifiedSocialCreditCode: '',
                                applicant: '',
                                unitType: '27'
                            });
                        }
                        // 招标代理机构
                        if (!_that.biddingInfo.agencyUnits.length) {
                            _that.biddingInfo.agencyUnits.splice(0, 1, {
                                organizationalCode: '',
                                unifiedSocialCreditCode: '',
                                applicant: '',
                                unitType: '21'
                            });
                        }
                        // 造价咨询单位
                        if (!_that.biddingInfo.costUnits.length) {
                            _that.biddingInfo.costUnits.splice(0, 1, {
                                organizationalCode: '',
                                unifiedSocialCreditCode: '',
                                applicant: '',
                                unitType: '26'
                            });
                        }

                        // 其他委托
                        if (_that.biddingInfo.bidMode != '3') {
                            _that.isShowFoot = true;
                        }
                    } else {
                        _that.$message({
                            message: data.message ? data.message : '招投标信息查询失败',
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
            // 获取招标类型，招标性质字典相关数据
            getProjTypeNature: function (code) {
                var _that = this;
                request('', {
                    url: ctx + 'rest/dict/code/multi/items/list',
                    type: 'get',
                    data: { 'dicCodeTypeCodes': code }
                }, function (result) {
                    if (result.content) {
                        var res = result.content

                        _that.bidTypeOptions = res.C_TENDER_TYPE;
                        _that.bidModeOptions = res.C_AGENT_TYPE;
                        _that.projectType = res.XM_DWLX;
                    }
                }, function (msg) {
                    _that.$message({
                        message: '服务请求失败',
                        type: 'error'
                    });
                });
            },
            // 获取招投标确认的行政单位区域码
            getGovAreaCode: function () {
                var _that = this;
                request('', {
                    url: ctx + 'rest/org/region/list',
                    type: 'get',
                    data: {}
                }, function (result) {
                    if (result.content) {
                        _that.govAreaCodeOptions = result.content;
                    }
                }, function (msg) {
                    _that.$message({
                        message: '服务请求失败',
                        type: 'error'
                    });
                });
            },
            // 获取页面的URL参数
            getUrlParam: function (val) {
                var svalue = location.search.match(new RegExp("[\?\&]" + val + "=([^\&]*)(\&?)", "i"));
                return svalue ? svalue[1] : svalue;
            },
            // 招标方式下拉改版时触发
            bidModeSelect: function (val) {
                if (val == '3') {
                    // 直接委托
                    // this.biddingInfo.agencyUnits.splice(0, 1);
                    // this.biddingInfo.costUnits.splice(0, 1);
                    this.isShowFoot = false;
                } else {
                    // 其他委托
                    // 招标代理机构
                    if (!this.biddingInfo.agencyUnits.length) {
                        this.biddingInfo.agencyUnits.splice(0, 1, {
                            organizationalCode: '',
                            unifiedSocialCreditCode: '',
                            applicant: '',
                            unitType: '2'
                        });
                    }
                    // 造价咨询单位
                    if (!this.biddingInfo.costUnits.length) {
                        this.biddingInfo.costUnits.splice(0, 1, {
                            organizationalCode: '',
                            unifiedSocialCreditCode: '',
                            applicant: '',
                            unitType: '14'
                        });
                    }
                    this.isShowFoot = true;
                }
            },
            // 项目名称过滤
            createFilter: function (queryString) {
                return function (projNameSelect) {
                    return (projNameSelect.value.toLowerCase());
                };
            },
            // 单位名称模糊查询
            querySearchJiansheName: function (queryString, cb) {
                var _that = this;
                if (typeof (queryString) != 'undefined') queryString = queryString.trim();
                if (queryString != '' && queryString.length >= 2) {
                    request('', {
                        url: ctx + 'rest/unit/list',
                        type: 'get',
                        data: { "keyword": queryString, "projInfoId": _that.projInfoId },
                    }, function (result) {
                        if (result) {
                            _that.projNameSelect = result.content;

                            _that.projNameSelect.map(function (item) {
                                if (item) {
                                    Vue.set(item, 'value', item.applicant);
                                }
                            })
                            var results = queryString ? _that.projNameSelect.filter(_that.createFilter(queryString)) : _that.projNameSelect;
                            // 调用 callback 返回建议列表的数据
                            cb(results);
                        }
                    }, function (msg) {
                        cb([]);
                    })
                } else {
                    cb([]);
                }
            },
            // 选中单位名称后设置相关信息
            setUnitInfo: function (val, obj) {
                Vue.set(obj, 'organizationalCode', val.organizationalCode);
                Vue.set(obj, 'unifiedSocialCreditCode', val.unifiedSocialCreditCode);
                Vue.set(obj, 'unitInfoId', val.unitInfoId);
            }
        }
    })
})()