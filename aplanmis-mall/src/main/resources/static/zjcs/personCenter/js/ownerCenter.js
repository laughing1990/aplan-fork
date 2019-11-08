var vm = new Vue({
    el: '#app',
    mixins: [mixins],
    data: function () {
        return {
            // 页面loading
            loading: false,

            isScroll: false,
            activeName: 0,

            // 当前账号信息
            currentLoginInfo: {
                unitInfoId: '',
                loginName: '',
            },

            // 业主单位的菜单栏
            ownerMenuList: [{
                itemName: '综合展示',
                childList: [{
                    name: '综合数据展示',
                    value: 'ownerAllDataShow',
                    select: true,
                }]
            }, {
                itemName: '我的采购',
                childList: [{
                    name: '新增采购需求',
                    value: 'addNeedPaurse',
                    select: false,
                }, {
                    name: '所有项目',
                    value: 'allProject',
                    select: false,
                }, {
                    name: '待处理项目',
                    value: 'pendingProject',
                    // value: 'detailAndContract',
                    select: false,
                }, {
                    name: '待审核项目',
                    value: 'waitAuditProject',
                    select: false,
                }, {
                    name: '待发布项目',
                    value: 'waitAnnounceProject',
                    select: false,
                }, {
                    name: '已退回项目',
                    value: 'returnProject',
                    select: false,
                }, {
                    name: '已发布项目',
                    value: 'alreadyAnnounceProject',
                    select: false,
                }, {
                    name: '已中选项目',
                    value: 'alreadySelectionProject',
                    select: false,
                }, {
                    name: '无效项目',
                    value: 'invalidProject',
                    select: false,
                }, {
                    name: '服务中项目',
                    value: 'inServiceProject',
                    select: false,
                }, {
                    name: '已完成项目',
                    value: 'accomplishProject',
                    select: false,
                }]
            }, {
                itemName: '我的信息',
                childList: [{
                    name: '基本信息',
                    value: 'baseInfo',
                    select: false,
                }]
            }],

            // 业主单位菜单栏选中
            ownerItemSelect: 'ownerAllDataShow',


        }
    },
    computed: {
        // 获取浏览器高度
        clientH: function () {
            var _h = $(window).height() - 200
            return _h + 'px'
        },
    },
    created: function () {
        this.init();
    },
    mounted: function () {},
    methods: {
        // 业主单位菜单栏选中
        ownerMenuSelect: function (mod) {
            var ts = this;
            // 当前选中菜单项赋值
            ts.ownerItemSelect = mod.value;
            ts.moduleLoad(mod.value + '.html', '#' + mod.value);
        },

        // 模块加载
        moduleLoad: function (_url, moduleName) {
            var url = "";
            if (document.location.protocol == "file:") {
                url = './components/owner/' + _url;
            } else {
                url = ctx + '/supermarket/main/' + _url;
            }

            $.get(url + " " + moduleName, function (result) {
                $('.owner-content-wrap').html(result);
                // 选中模块后添加进url
                var _idx = _url.indexOf('.');
                location.hash = _url.slice(0, _idx);
            });
        },
        // init
        init: function () {
            // test
            // var test = {
            //     isPersonAccount: 0,
            //     personName: '黎明',
            //     unitName: '测试的业主单位',
            //     // unitId: 'c7d6fdda-1e41-4bcd-91df-0b4312a286c2',
            //     unitId: '4028869e6a3326b6016a343fb9c005fb',
            //     userId: '00dba4b9-e986-47c1-9cf1-3e2b3d459980',
            //     isAdministrators: 1,
            //     unitInfos: [{
            //         applicant: "英德市福红茶业有限公司",
            //         isAdministrators: null,
            //         isImUnit: "1",
            //         isOwnerUnit: "1",
            //         unitInfoId: "cebed665-c262-4ed3-ba1f-fbf41b7cbf8a",
            //     }],
            // };
            // localStorage.setItem('loginInfo', JSON.stringify(test))
            var _curLoginInfo = JSON.parse(localStorage.getItem('loginInfo'));
            if (!_curLoginInfo) {
                window.location.href = ctx + '/supermarket/main/index.html';
            }

            // 如果是个人账号
            if (_curLoginInfo.isPersonAccount == 1) {
                this.currentLoginInfo.loginName = _curLoginInfo.personName;
            } else {
                if (_curLoginInfo.userId == null) { //企业账号-非委托人
                    this.currentLoginInfo.loginName = _curLoginInfo.unitName;
                } else { //委托人
                    this.currentLoginInfo.loginName = _curLoginInfo.personName;
                }
            }

            // 企业账号添加服务管理模块
            if(_curLoginInfo.isPersonAccount == 0 && _curLoginInfo.isAdministrators == 1){
                var serviceManageModule =  {
                    itemName: '委托人管理',
                    childList: [
                        {
                            name: '添加委托人',
                            value: 'ownerAddClientPeople',
                            select: false,
                        }
                    ]
                };
                this.ownerMenuList.splice(2,0,serviceManageModule);
            }

            // 刷新回显当前模块
            if (location.hash) {
                var _mod = location.hash.slice(1);
                this.moduleLoad(_mod + '.html', '#' + _mod);
                this.ownerItemSelect = _mod;
            } else {
                this.moduleLoad('ownerAllDataShow.html', '#allDataShow');
            }
        },

    }
});