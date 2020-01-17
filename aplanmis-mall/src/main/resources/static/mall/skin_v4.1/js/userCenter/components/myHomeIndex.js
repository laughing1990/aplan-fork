var myHomeIndex = (function () {
    var vm = new Vue({
        el: "#myHomeindex",
        data: {
            ctx: ctx,
            applyIngData: [],
            approvalNum: 0,
            matCompletionNum: 0,
            draftNum: 0,
            applyNum: 0,
            supplyNum: 0,
            completedNum: 0,

            // 用户的信息
            aeaLinkmanInfo: {},
            aeaUnitInfo: {},
            option: {},
            user: 2,
            isBlack: null,

            declare: {
                pageSize: 5,
                currentPage: 1,
                total: 0
            }
        },
        created: function () {
            var ts = this;
            ts.getwarningNum()
            ts.getApplyIngData()
            ts.getUserinfo()
        },
        methods: {
        iteminstStateColorFormat:function(val){
            if(val==1) return "#457EFF";
            if(val==2) return "#4EB1FD";
            if(val==3) return "#00C161";
            if(val==4) return "#eee";
            if(val==5) return "#eee";
            if(val==6) return "#447EFF";
            if(val==7) return "#2BB49E";
            if(val==8) return "#447EFF";
            if(val==9) return "#447EFF";
            if(val==10) return "#2BB49E";
            if(val==11) return "#00C161";
            if(val==12) return "#00C161";
            if(val==13) return "#FF4B47";
            if(val==14) return "#FF4B47";
            if(val==15) return "#FF4B47";
            return "#000";
        },
            // 获取正在进行中的申报列表数据
            getApplyIngData: function () {
                var ts = this;
                request('', {
                    url: ctx + 'rest/user/hadApplyItem/list',
                    type: 'get',
                    data: {
                        pageNum: ts.declare.currentPage,
                        pageSize: ts.declare.pageSize,
                        state: 1,
                    }
                }, function (res) {
                    if (res.success) {
                        ts.applyIngData = res.content.list;
                        ts.declare.total = res.content.total;
                    } else {
                        ts.$message.error(res.message)
                    }
                }, function () {
                    ts.$message.error('网络错误！')
                });
            },

            // 统计分析数量
            getwarningNum: function () {
                request('', {
                    url: ctx + 'rest/user/approvalAndMatCompletion/num',
                    type: 'get',
                }, function (res) {
                    if (res.success) {
                        vm.approvalNum = res.content.approvalNum;
                        vm.matCompletionNum = res.content.matCompletionNum;
                        vm.draftNum = res.content.draftNum;
                        vm.applyNum = res.content.applyNum;
                        vm.supplyNum = res.content.supplyNum;
                        vm.completedNum = res.content.completedNum;
                    }
                }, function () {

                });
            },

            // 获取信息
            getUserinfo: function () {
                var _this = this;
                request('', {
                    url: ctx + 'rest/user/userinfo',
                    type: 'get',
                    data: {}
                }, function (res) {
                    if (res.success) {
                        if (res.content.aeaLinkmanInfo || res.content.aeaUnitInfo) {
                            _this.aeaLinkmanInfo = res.content.aeaLinkmanInfo;
                            _this.aeaUnitInfo = res.content.aeaUnitInfo;
                            _this.option = res.content.aeaUnitList;
                            _this.user = res.content.role;
                            _this.getPersonOrUnitBlackByBizCode();
                        }
                    } else {
                        _this.$message.error(res.message);
                    }

                }, function () {
                    _this.$message.error('获取数据失败，请重试');
                });
            },

            // 请求接口判断 信用状况
            getPersonOrUnitBlackByBizCode: function () {
                var _this = this;
                var params = {};
                if (_this.user == 2) { //企业时，传统一信用社会编码
                    params = {
                        bizCode: _this.aeaUnitInfo.unifiedSocialCreditCode,
                        bizType: 'u'
                    }
                } else {
                    params = {
                        bizCode: _this.aeaLinkmanInfo.linkmanCertNo,
                        bizType: 'l'
                    }
                }
                request('', {
                    url: ctx + 'rest/user/credit/redblack/getPersonOrUnitBlackByBizCode',
                    type: 'get',
                    data: params,
                }, function (res) {
                    if (res.success) {
                        _this.isBlack = res.content.isBlack;
                    }
                }, function () {
                });
            },

            // 点击编辑个人信息
            toUserCenterPage: function () {
                userCenter.vm.toUserCenterPage();
            },

            // 点击并联申报
            goToStageApply: function () {
                userCenter.vm.selectNav = "我的项目";
                localStorage.setItem('selectNav', userCenter.vm.selectNav);
                location.href = ctx + "rest/main/toIndexPage?#/myParallelPage";
            },

            // 点击我的项目库
            toDeclare: function () {
                var mod = {
                    name: '我的项目',
                    value: 'declare',
                    select: true,
                }
                userCenter.vm.userCenterMenuSelect(mod)
            },

            // 点击我的材料
            toMyMaterials: function () {
                var mod = {
                    name: '我的材料',
                    value: 'MyMaterials',
                    select: true,
                }
                userCenter.vm.userCenterMenuSelect(mod)
            },

            // 点击我的证件照
            toMyCertificateLibrary:function(){
                var mod = {
                    name: '我的证件照库',
                    value: 'MyCertificateLibrary',
                    select: true,
                }
                userCenter.vm.userCenterMenuSelect(mod)
            },

            // 点击我的云盘
            toMyCloundSpaces: function () {
                window.location.href = ctx + '/rest/main/toIndexPage?#/myCloundSpaces';
            },

            // 点击企业信用
            toCreditDetail: function () {
                var mod = {
                    name: '企业信用',
                    open: true,
                    value: 'CreditUnitList',
                }
                userCenter.vm.userCenterMenuSelect(mod)
            },

            // 点击查看 申报详细
            toDetail: function (item) {
                var mod = {
                    name: '申报列表',
                    value: 'declareHave',
                    select: true,
                }
                var formMyHomeIndexData = {
                    originFlag: true,
                    item: item
                };
                sessionStorage.setItem("formMyHomeIndexData", JSON.stringify(formMyHomeIndexData))
                userCenter.vm.userCenterMenuSelect(mod);
            },

            handleSizeChange: function (val) {
                this.declare.pageSize = val;
                this.getApplyIngData();
            },
            handleCurrentChange: function (val) {
                this.declare.currentPage = val;
                this.getApplyIngData();
            }
        },
        filters:{
            iteminstStateFormat: function (val) {
                if(val==1) return "已接件";
                if(val==2) return "已撤件";
                if(val==3) return "已受理";
                if(val==4) return "不受理";
                if(val==5) return "不予受理";
                if(val==6) return "补正（开始）";
                if(val==7) return "补正（结束）";
                if(val==8) return "部门开始办理";
                if(val==9) return "特别程序（开始）";
                if(val==10) return "特别程序（结束）";
                if(val==11) return "办结（通过）";
                if(val==12) return "办结（容缺通过）";
                if(val==13) return "办结（不通过）";
                if(val==14) return "撤回";
                if(val==15) return "撤销";
                return "-";
            }
        }
    })
    return {
        vm: vm
    }
})();