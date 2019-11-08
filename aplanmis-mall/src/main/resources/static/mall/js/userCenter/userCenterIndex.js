var userCenter = (function () {
    var vm = new Vue({
        el: '#userCenter',
        data: function () {
            return {
                ctx: ctx,
                // 页面loading
                loading: false,

                // 当前账号信息
                curentLoginInfo: {
                    imgUrl: null,
                    isAdministrators: "1",
                    isOwner: "0",
                    isPersonAccount: "0",
                    personName: "丘典",
                    sid: "E2D72F38C3B8085DF5F647CDCF88766B",
                    unitId: "1d052551-9c5c-4f01-87ad-b0c62552ad23",
                    unitName: "英德市福红茶业有限公司",
                    userId: "b84e41eb-f733-4e61-a436-f77e7e92b691"
                },

                // 业主单位的菜单栏
                userCenterMenuList: [
                    {
                        name: '我的首页',
                        value: 'MyHomeIndex',
                        open: true,
                    },
                    {
                    name: '项目管理',
                    open: true,
                    childList: [{
                        name: '我的项目库',
                        value: 'declare',
                        select: true,
                    }, {
                        name: '新增项目',
                        value: 'AddProj',
                        select: false,
                    },
                        {
                            name: '项目进度',
                            value: 'scheduleInquire',
                            select: false,
                        },
                    ]
                }, {
                    name: '申报管理',
                    open: true,
                    childList: [
                        {
                            name: '申报列表',
                            value: 'declareHave',
                            select: false,
                        }/*,
                        {
                            name: '待申报',
                            value: 'drafts',
                            select: false,
                        }*/,
                        {
                            name: '材料补全',
                            value: 'matCompletionList',
                            select: false,
                        }]
                    },
                    {
                        name: '办件管理',
                        open: true,
                        childList: [
                            {
                                name: '办件列表',
                                value: 'approve',
                                select: false,
                            }
                            , {
                                name: '材料补正',
                                value: 'matSupplementList',
                                select: false,
                            }
                        ]
                    },
                    {
                        name: '我的资料库',
                        open: true,
                        childList: [
                          /*  {
                                name: '我的云盘',
                                value: '',
                                select: false,
                            },*/
                            {
                                name: '我的材料库',
                                value: 'MyMaterials',
                                select: false,
                            },
                            /*{
                                name: '我的证照库',
                                value: '',
                                select: false,
                            },*/
                        ]
                    },
                    {
                        name: '企业信用',
                        open: true,
                        value: 'CreditDetail',
                    },

                ],

                // 菜单栏选中
                userCenterItemSelect: 'MyHomeIndex',
                selectNav:'',
                approvalNum: 0,
                matCompletionNum: 0,
				draftNum:0,
                applyNum:0,
                supplyNum:0,
                withdrawalNum:0,
                myMatNum:0

            }
        },
        computed: {
            // 获取浏览器高度
            clientH: function () {
                var _h = $(window).height()+70
                return _h + 'px'
            },
            // 获取浏览器高度
            clientRightH: function () {
                var _h = $('#my-pro-right_m').height();
                return _h + 'px'
            }
        },
        created: function () {
            this.init();
            this.getwarningNum();
        },
        mounted: function () {
        },
        methods: {
            handleOpenMenu:function(item,row,index){
                var ts = this;
                if(row && row.length > 0){
                    item.open = !item.open;
                }else{
                    ts.userCenterMenuSelect(item);
                }
            },
            hangleIcon: function (index) {
                if (index === 0) {
                    return "icon-1"
                } else if(index === 1){
                    return "icon-2"
                }else if(index === 2){
                    return "icon-3"
                }else if(index === 3){
                    return "icon-4"
                }else if(index === 4){
                    return "icon-5"
                }else if(index === 5){
                    return "icon-6"
                }
            },
            // 消息数量
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
						vm.myMatNum=res.content.myMatNum;
                    }
                }, function () {

                });
            },
            toUserCenterPage: function () {
                var mod = {
                    name: '个人中心',
                    value: 'UserInfo',
                    select: true,
                }
                this.userCenterMenuSelect(mod)
            },
            // 业主单位菜单栏选中
            userCenterMenuSelect: function (mod) {
                var ts = this;
                // 当前选中菜单项赋值
                ts.userCenterItemSelect = mod.value;
                ts.selectNav = mod.name;
                localStorage.setItem('selectNav',ts.selectNav);
                if(mod.value == "/userCenterIndex"){
                    localStorage.removeItem('selectNav');
                    ts.selectNav = "我的首页";
                    ts.userCenterItemSelect = "MyHomeIndex";
                    ts.moduleLoad('myHomeIndex.html', '#MyHomeIndex',true);
                }
                ts.moduleLoad(mod.value + '.html', '#' + mod.value);
            },

            // 模块加载
            moduleLoad: function (_url, moduleName,flag) { // flag=true 刷新页面
                var url = "";
                if (document.location.protocol == "file:") {
                    url = './components/' + _url;
                } else {
                    url = ctx + 'rest/user/' + 'to' + moduleName.slice(1) + 'Page';
                }

                $.get(url + " " + moduleName, function (result) {
                    $('#my-pro-right_m').html(result);
                    // 选中模块后添加进url
                    var _idx = _url.indexOf('.');
                    if(moduleName=='#declare'&&!flag){
                        location.hash = _url.slice(0, _idx);
                    }else {
                        location.hash = _url.slice(0, _idx);
                        location.search = '';
                    }
                });
            },
            // init
            init: function () {
                // 刷新回显当前模块
               /* if (location.hash) {  旧的写法逻辑
                    var _mod = location.hash.slice(1);
                    if(_mod == "/userCenterIndex"){
                        this.moduleLoad('myHomeIndex.html', '#MyHomeIndex',true);
                    }else {
                        this.moduleLoad(_mod + '.html', '#' + _mod);
                    }
                    this.userCenterItemSelect = _mod;

                } else {
                    this.moduleLoad('myHomeIndex.html', '#MyHomeIndex');
                }*/

               var hashVal = location.hash.slice(1) || userCenterItemSelect;
               var  mod = {
                   name: localStorage.getItem('selectNav') || '我的首页',
                   value:hashVal,
                   select: true,
               };
                this.userCenterMenuSelect(mod);

                // 登陆用户数据
                var _curLoginInfo = localStorage.getItem('loginInfo');

                if (_curLoginInfo) {
                    this.$nextTick(function () {

                    })
                    this.curentLoginInfo = JSON.parse(_curLoginInfo);
                } else {
                    window.location.href = window.location.origin + '/aplanmis-mall/rest/mall/loginIndex';
                    return this.$message({
                        message: '您尚未登陆，请先登陆！',
                        type: 'warning'
                    })
                }
            },

            // 退出
            logout: function () {
                var ts = this;
                request('', {
                    url: ctx + 'rest/mall/logout',
                    type: 'get',
                }, function (res) {
                    if (res.success) {
                        localStorage.clear();
                        window.location.href = ctx + 'rest/main/toIndexPage';
                        return ts.$message({
                            message: '退出成功！',
                            type: 'success'
                        })
                    } else {
                        return ts.$message({
                            message: '退出失败！',
                            type: 'error'
                        })
                    }
                }, function () {
                    return ts.$message({
                        message: '退出失败！',
                        type: 'error'
                    })
                });
            },
        }
    });
    return {
        vm: vm
    }
})();
