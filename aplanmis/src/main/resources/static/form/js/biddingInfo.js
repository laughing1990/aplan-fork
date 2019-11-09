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
                        unitType: '8c0173dd-f2cb-48f9-8233-e27d0b7ed941'
                    }],
                    agencyUnits: [{
                        organizationalCode: '',
                        unifiedSocialCreditCode: '',
                        applicant: '',
                        unitType: '31725803-1710-4376-be08-78abae406a02'
                    }],
                    costUnits: [{
                        organizationalCode: '',
                        unifiedSocialCreditCode: '',
                        applicant: '',
                        unitType: '1e18c273-7531-4fa6-86f3-fd5bfb6bd9e1'
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
                    bidConfirmTime: { required: true, message: "招投标确认时间不得为空！", trigger: ["change"] },
                    govOrgCode: { required: true, message: "招投标确认的行政单位机构代码不得为空！", trigger: ["change"] },
                    govOrgName: { required: true, message: "招投标确认的行政单位名称不得为空！", trigger: ["change"] },
                    govAreaCode: { required: true, message: "招投标确认的行政单位区域码不得为空！", trigger: ["change"] },
                    organizationalCode: { required: true, message: "组织机构代码不得为空！", trigger: ["change"] },
                    unitType: { required: true, message: "项目主体类型不得为空！", trigger: ["change"] },
                },
                bidTypeOptions: [], // 招标类型下拉选项arr
                bidModeOptions: [], // 招标方式下拉选项arr
                govAreaCodeOptions: [], // 招投标确认的行政单位区域码下拉选项arr
                projectType: [], // 项目主体类型下拉选项arr
                isShowFoot: false // 是否显示‘招标代理机构’和‘造价咨询单位’
            }
        },
        created: function () {
            // this.projInfoId = this.getUrlParam('projInfoId');
            this.projInfoId = '09485014-cfe9-4815-b9e3-13f24035220b';
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

                        if (_that.biddingInfo.bidMode != 'f3632ed9-f728-436f-92f6-4c88c7bbba86') {
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
            bidModeSelect: function(val) {                
                if (val == 'f3632ed9-f728-436f-92f6-4c88c7bbba86') {
                    // 直接委托
                    this.biddingInfo.agencyUnits.splice(0, 1);
                    this.biddingInfo.costUnits.splice(0, 1);
                    this.isShowFoot = false;
                } else {
                    // 其他委托
                    // 招标代理机构
                    this.biddingInfo.agencyUnits.splice(0, 1, {
                        organizationalCode: '',
                        unifiedSocialCreditCode: '',
                        applicant: '',
                        unitType: '31725803-1710-4376-be08-78abae406a02'
                    });
                    // 造价咨询单位
                    this.biddingInfo.costUnits.splice(0, 1, {
                        organizationalCode: '',
                        unifiedSocialCreditCode: '',
                        applicant: '',
                        unitType: '1e18c273-7531-4fa6-86f3-fd5bfb6bd9e1'
                    });
                    this.isShowFoot = true;
                }
            }
        }
    })
})()
