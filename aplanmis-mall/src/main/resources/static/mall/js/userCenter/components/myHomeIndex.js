var myHomeIndex = (function(){
    var vm = new Vue({
        el:"#myHomeindex",
        data:{
            ctx:ctx,
            applyIngData:[],
            approvalNum: 0,
            matCompletionNum: 0,
            draftNum:0,
            applyNum:0,
            supplyNum:0,
            withdrawalNum:0,

            // 用户的信息
            aeaLinkmanInfo:{},
            aeaUnitInfo:{},
            option:{},
            user:2,
            isBlack:null,
        },
        created:function(){
            var ts = this;
            ts.getwarningNum()
            ts.getApplyIngData()
            ts.getUserinfo()
        },
        methods:{
            // 获取正在进行中的申报列表数据
            getApplyIngData:function () {
                var ts = this;
                request('', {
                    url: ctx + 'rest/user/hadApplyItem/list',
                    type: 'get',
                    data: {
                        pageNum:0,
                        pageSize:5,
                        state:1,
                    }
                }, function (res) {
                    if (res.success) {
                        ts.applyIngData = res.content.list.slice(0,5);
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
                        vm.withdrawalNum = res.content.withdrawalNum;
                    }
                }, function () {

                });
            },

            // 获取信息
            getUserinfo:function(){
                var _this = this;
                request('', {
                    url: ctx + 'rest/user/userinfo',
                    type: 'get',
                    data:{}
                }, function (res) {
                    if(res.success){
                        if(res.content.aeaLinkmanInfo || res.content.aeaUnitInfo){
                            _this.aeaLinkmanInfo = res.content.aeaLinkmanInfo;
                            _this.aeaUnitInfo = res.content.aeaUnitInfo;
                            _this.option = res.content.aeaUnitList;
                            _this.user = res.content.role;
                            _this.getPersonOrUnitBlackByBizCode();
                        }
                    }else{
                        _this.$message.error(res.message);
                    }

                },function () {
                    _this.$message.error('获取数据失败，请重试');
                });
            },

            // 请求接口判断 信用状况
            getPersonOrUnitBlackByBizCode:function(){
                var _this = this;
                var params = {};
                if(_this.user == 2){ //企业时，传统一信用社会编码
                    params = {
                        bizCode:_this.aeaUnitInfo.unifiedSocialCreditCode,
                        bizType:'u'
                    }
                }else{
                    params = {
                        bizCode:_this.aeaLinkmanInfo.linkmanCertNo,
                        bizType: 'l'
                    }
                }
                request('', {
                    url: ctx + 'rest/user/credit/redblack/getPersonOrUnitBlackByBizCode',
                    type: 'get',
                    data:params,
                }, function (res) {
                    if(res.success){
                        _this.isBlack = res.content.isBlack;
                    }
                },function () {
                });
            },
            
            // 点击编辑个人信息
            toUserCenterPage:function () {
                userCenter.vm.toUserCenterPage();
            },

            // 点击并联申报
            goToStageApply:function(){
                userCenter.vm.selectNav="我的项目库";
                localStorage.setItem('selectNav',userCenter.vm.selectNav);
                location.href =ctx + "rest/main/toIndexPage?projInfoId=null#declare";
            },

            // 点击我的项目库
            toDeclare: function () {
                var mod = {
                    name: '我的项目库',
                    value: 'declare',
                    select: true,
                }
                userCenter.vm.userCenterMenuSelect(mod)
            },

            // 点击我的材料库
            toMyMaterials: function () {
                var mod = {
                    name: '我的材料库',
                    value: 'MyMaterials',
                    select: true,
                }
                userCenter.vm.userCenterMenuSelect(mod)
            },

            // 点击我的云盘
            toMyCloundSpaces:function(){
                window.location.href = ctx + '/rest/main/toIndexPage?#/myCloundSpaces';
            },

            // 点击企业信用
            toCreditDetail: function () {
                var mod = {
                    name: '企业信用',
                    open: true,
                    value: 'CreditDetail',
                }
                userCenter.vm.userCenterMenuSelect(mod)
            },

            // 点击查看 申报详细
            toDetail:function (item) {
                var mod = {
                    name: '申报列表',
                    value: 'declareHave',
                    select: true,
                }
                var formMyHomeIndexData={
                    originFlag:true,
                    item:item
                };
                sessionStorage.setItem("formMyHomeIndexData",JSON.stringify(formMyHomeIndexData))
                userCenter.vm.userCenterMenuSelect(mod);
            },


        },
    })
    return {
        vm: vm
    }
})();