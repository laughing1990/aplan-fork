// var ctx = 'http://192.168.32.46:8084/aplanmis-mall/';
var vm = new Vue({
    el: '#app',
    mixins: [mixins],
    data: function () {
        return {
            // 页面loading
            loading: false,

            // 页面app高度和内容content高度
            appH: '',
            contentH: '',

            isScroll: false,
            activeName: 0,

            // 当前账号信息
            currentLoginInfo: {
                unitInfoId: '',
                loginName: '',
            },

            // 中介机构的菜单栏
            agencyMenuList: [{
                itemName: '综合展示',
                childList: [{
                    name: '综合数据展示',
                    value: 'allDataShow',
                    select: true,
                }]
            }, {
                itemName: '我的项目',
                childList: [{
                    name: '可报名的项目',
                    value: 'projectCanSignup',
                    select: false,
                }, {
                    name: '报名的项目',
                    value: 'projectSignup',
                    select: false,
                }, {
                    name: '中选的项目',
                    value: 'projectSelection',
                    select: false,
                }, {
                    name: '服务的项目',
                    value: 'projectService',
                    select: false,
                }]
            }, {
                itemName: '服务管理',
                childList: [{
                        name: '服务事项管理',
                        value: 'serviceMatterManage',
                        select: false,
                    }, {
                        name: '执业/职业人员管理',
                        value: 'jobPeopleManage',
                        select: false,
                    },
                    {
                        name: '添加委托人',
                        value: 'addClientPeople',
                        select: false,
                    }
                ]
            }, {
                itemName: '我的资料',
                childList: [{
                    name: '基本信息',
                    value: 'baseInfo',
                    select: false,
                }]
            }],

            // 中介机构菜单栏选中
            agencyItemSelect: 'allDataShow',
        }
    },
    computed: {

    },
    created: function () {
        // test
        // this.fetchClientList();
        this.init();
    },
    mounted: function () {
        var ts = this;
        ts.mainH();
        // ts.moduleLoad('allDataShow.html', '#allDataShow');
    },
    methods: {
        // 计算index,app高度
        mainH: function () {
            var ts = this;
            ts.$nextTick(function () {
                var _h = Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
                ts.appH = _h + 'px';
                var _clientH = $(window).height();
                if (_h > _clientH + 200) {
                    ts.contentH = "calc(100% - 255px)";
                } else {
                    ts.contentH = "100%";
                }

            })

        },
        // 中介机构菜单栏选中
        agencyMenuSelect: function (mod) {
            var ts = this;
            // 当前选中菜单项赋值
            ts.agencyItemSelect = mod.value;
            // 选择模块后，重新计算文档高度
            ts.mainH();
            ts.moduleLoad(mod.value + '.html', '#' + mod.value);
        },

        // 模块加载
        moduleLoad: function (_url, moduleName) {
            var ts = this,url = "";
            if (document.location.protocol == "file:") {
                url = './components/client/' + _url;
            } else {
                url = ctx + '/supermarket/main/' + _url;
            }

            $.get(url + " " + moduleName, function (result) {
                $('.agency-content-wrap').html(result);
                url = '';
                // 选中模块后添加进url
                var _idx = _url.indexOf('.');
                location.hash = _url.slice(0, _idx);
                // ts.backToTop();
            });
        },

        // 页面初始话
        init: function () {

            // // test
            // var test = {
            //     isPersonAccount: 0,
            //     personName: '黎明',
            //     unitName: '测试的中介机构',
            //     // unitId: 'c7d6fdda-1e41-4bcd-91df-0b4312a286c2',
            //     unitId: '1d052551-9c5c-4f01-87ad-b0c62552ad23',
            //     userId: 'sdsdsdsd',
            //     isAdministrators: 1,
            //     unitInfos: [{
            //         applicant: "英德市福红茶业有限公司",
            //         isAdministrators: null,
            //         isImUnit: "1",
            //         isOwnerUnit: "1",
            //         unitInfoId: "1d052551-9c5c-4f01-87ad-b0c62552ad23",
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
            // this.currentLoginInfo.loginName = _curLoginInfo.unitName;
            // 委托人账号
            if (_curLoginInfo.isPersonAccount == 0 && _curLoginInfo.userId !== null) {
                // sAdminstrator = 0 或NULL整个服务管理模块都没了
                if (_curLoginInfo.isAdministrators == 0 || _curLoginInfo.isAdministrators === null) {
                    this.agencyMenuList.splice(2, 1)
                }
            }
            // 刷新回显当前模块
            if (location.hash) {
                var _mod = location.hash.slice(1);
                this.moduleLoad(_mod + '.html', '#' + _mod);
                this.agencyItemSelect = _mod;
            } else {
                this.moduleLoad('allDataShow.html', '#allDataShow');
            }
        },
    },
    filters: {

    },

});