var vm = new Vue({
    el: '#app',
    data: function () {
        return {
            // 页面loading
            loading: false,

            // 页面app高度和内容content高度
            appH: '',
            contentH: '',

            // 当前账号信息
            currentLoginInfo: {
                unitInfoId: 'e03f1889-5a2d-44ce-abe0-4a9177824b84',
            },

            // 中介机构的菜单栏
            agencyMenuList: [{
                itemName: '综合展示',
                childList: [{
                    name: '综合数据展示',
                    value: '综合数据展示',
                    select: true,
                }]
            }, {
                itemName: '我的项目',
                childList: [{
                    name: '可报名的项目',
                    value: '可报名的项目',
                    select: false,
                }, {
                    name: '报名的项目',
                    value: '报名的项目',
                    select: false,
                }, {
                    name: '中选的项目',
                    value: '中选的项目',
                    select: false,
                }, {
                    name: '服务的项目',
                    value: '服务的项目',
                    select: false,
                }]
            }, {
                itemName: '服务管理',
                childList: [{
                        name: '服务事项管理',
                        value: '服务事项管理',
                        select: false,
                    }, {
                        name: '执业/职业人员管理',
                        value: '执业/职业人员管理',
                        select: false,
                    }, {
                        name: '业绩管理',
                        value: '业绩管理',
                        select: false,
                    },
                    {
                        name: '添加委托人',
                        value: '添加委托人',
                        select: false,
                    }
                ]
            }, {
                itemName: '我的资料',
                childList: [{
                    name: '基本信息',
                    value: '基本信息',
                    select: false,
                }]
            }],

            // 中介机构菜单栏选中
            agencyItemSelect: '可报名的项目',


            //  添加委托人模块
            // 委托人列表
            clientList: [],
            // 委托人查询参数
            clientCheckData: {
                pageNum: 1,
                pageSize: 10,
                isBindAccount: 1,
                keyword: "",
                unitInfoId: ''
            },
            // 委托人-总数
            clientTotal: 0,
            // 当前是添加委托人还是委托人列表
            clientIsList: true,
            // 单位联系人账号or关联其他账号
            isRelationOthers: 'list',
            // 单位联系人账号列表
            unitConcatList: [],
            //单位联系人查询参数
            unitConcatCheckData: {
                pageNum: 1,
                pageSize: 10,
                isBindAccount: 0,
                unitInfoId: ''
            },
            // 单位联系人-总数
            unitConcatTotal: 0,
            // 关联其他联系人账号列表
            otherConcatList: [],
            //关联其他联系人查询参数
            otherConcatCheckData: {
                linkmanCertNo: '',
                loginName: '',
                pageNum: 1,
                pageSize: 10,

            },
            // 关联其他联系人-总数
            otherConcatTotal: 0,
            // 绑定账号dialog
            clientAccountBindDialog: false,
            // 绑定账号，账号，密码回显
            clientAccountBindData: {
                loginName: '',
                loginPwd: '',
            },
            // 委托人当前操作行
            clientCurEditRow: {},


            // 可报名项目
            // 可报名项目列表
            canSignupProjectList: [{}],
            // 可报名项目查询参数
            canSignupPeojectCheckData: {
                pageNum: 1,
                pageSize: 10,
                itemVerId: '',
                projName: '',
                unitInfoId: ''
            },
            //可报名项目总数
            canSignupProjectTotal: 0,
            // 可报名项目列表中的中介事项下拉框
            canSignupProjectItemOptions: [{
                label: '1',
                value: '1'
            }],
            // 当前是否展示参与报名的项目的项目信息
            isCanSignupPeojectInfoPandel: false,
            // 当前是否展示参与报名的项目信息1、编辑2、成功模块3
            isCanSignupProjectPandelState: 1,


            // 报名的项目
            signupProjectList: [],
            signupPeojectCheckData: {
                pageNum: 1,
                pageSize: 10,
                unitInfoId: ''
            },
            signupProjectTotal: 0,
            isSignupProjectInfoPandel: false,
        }
    },
    computed: {

    },
    created: function () {
        // test
        // this.fetchClientList();
    },
    mounted: function () {
        var ts = this;
        ts.mainH();
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
            ts.agencyMenuList.forEach(function (item) {
                item.childList.forEach(function (i) {
                    i.select = false;
                })
            });
            mod.select = true;
            // 当前选中菜单项赋值
            ts.agencyItemSelect = mod.value;
            // 选择模块后，重新计算文档高度
            ts.mainH();
            // 获取当前模块的数据
            switch (mod.value) {
                case '可报名的项目':
                    ts.sideBarCanSignupProjectModuleSelect();
                    // ts.moduleLoad('projectCanSignup.html', '#projectCanSignup');
                    break;
                case '报名的项目':
                    ts.moduleLoad('projectSignup.html', '#projectSignup');
                    break;
                case '中选的项目':
                    ts.moduleLoad('projectSelection.html', '#projectSelection');
                    break;
                case '服务的项目':
                    ts.moduleLoad('projectService.html', '#projectService');
                    break;
                case '服务事项管理':
                    ts.moduleLoad('serviceMatterManage.html', '#serviceMatterManage');
                    break;
                case '执业/职业人员管理':
                    ts.moduleLoad('jobPeopleManage.html', '#jobPeopleManage');
                    break;
                case '业绩管理':
                    ts.moduleLoad('achieveManage.html', '#achieveManage');
                    break;
                case '添加委托人':
                    ts.fetchClientList();
                    // ts.moduleLoad('addClientPeople.html', '#addClientPeople');
                    break;
                case '综合数据展示':
                    ts.moduleLoad('allDataShow.html', '#allDataShow');
                    break;
                default:
                    ts.moduleLoad('baseInfo.html', '#baseInfo');
            }
        },

        // 委托人模块
        // 返回列表
        returnClientList: function () {
            var ts = this;
            ts.clientIsList = true; //切换成委托人列表模块
            ts.isRelationOthers = 'list'; //添加委托人模块中，默认显示“单位联系人账号模块”
            ts.unitConcatCheckData.pageNum = 1; //单位联系人查询参数默认为第一页
            ts.fetchClientList(); //返回列表-重新获取委托人list
        },
        // 获取委托人列表
        fetchClientList: function () {
            var ts = this;
            ts.clientCheckData.unitInfoId = ts.currentLoginInfo.unitInfoId;
            ts.loading = true;
            request('', {
                url: ctx + 'aea/supermarket/clientManage/getLinkmanListByCond',
                type: 'get',
                data: ts.clientCheckData,
            }, function (res) {
                console.log(res)
                ts.loading = false;
                if (res.success) {
                    ts.clientList = res.content.rows;
                    ts.clientTotal = res.content.total;
                } else {
                    ts.$message({
                        message: '加载失败',
                        type: 'error'
                    });
                }
            }, function (msg) {
                ts.loading = false;
                ts.$message({
                    message: '加载失败',
                    type: 'error'
                });
            });
        },
        // 委托人列表查询
        clientSearch: function () {
            var ts = this;
            ts.clientCheckData.pageNum = 1;
            ts.fetchClientList();
        },
        // 委托人切换页码
        clientListPageChange: function (val) {
            ts.clientCheckData.pageNum = val;
            ts.fetchClientList();
        },
        // 委托人切换页面大小
        clientListSizeChange: function (val) {
            ts.clientCheckData.pageSize = val;
            ts.fetchClientList();
        },
        // 获取单位联系人列表
        fetchUnitConcatList: function () {
            var ts = this;
            ts.unitConcatCheckData.unitInfoId = ts.currentLoginInfo.unitInfoId;
            ts.loading = true;
            request('', {
                url: ctx + 'aea/supermarket/clientManage/getLinkmanListByCond',
                type: 'get',
                data: ts.unitConcatCheckData,
            }, function (res) {
                console.log(res)
                ts.loading = false;
                if (res.success) {
                    ts.unitConcatList = res.content.rows;
                    ts.unitConcatTotal = res.content.total;
                } else {
                    ts.$message({
                        message: '加载失败',
                        type: 'error'
                    });
                }
            }, function (msg) {
                ts.loading = false;
                ts.$message({
                    message: '加载失败',
                    type: 'error'
                });
            });
        },
        // 单位联系人切换页码
        unitConcatListPageChange: function (val) {
            ts.unitConcatCheckData.pageNum = val;
            ts.fetchUnitConcatList();
        },
        // 单位联系人切换页面大小
        unitConcatListSizeChange: function (val) {
            ts.unitConcatCheckData.pageSize = val;
            ts.fetchUnitConcatList();
        },
        // 获取关联其他账号列表
        fetchOtherConcatList: function () {
            var ts = this,
                _checkData = ts.otherConcatCheckData;
            if (!_checkData.linkmanCertNo && !_checkData.loginName) return ts.$message({
                message: '请输入查询信息'
            })
            ts.otherConcatCheckData.unitInfoId = ts.currentLoginInfo.unitInfoId;
            ts.loading = true;
            request('', {
                url: ctx + 'aea/supermarket/clientManage/getLinkmanList',
                type: 'get',
                data: ts.otherConcatCheckData,
            }, function (res) {
                console.log(res)
                ts.loading = false;
                if (res.success) {
                    ts.otherConcatList = res.content.rows;
                    ts.otherConcatTotal = res.content.total;
                } else {
                    ts.$message({
                        message: '加载失败',
                        type: 'error'
                    });
                }
            }, function (msg) {
                ts.loading = false;
                ts.$message({
                    message: '加载失败',
                    type: 'error'
                });
            });
        },
        // 其他联系人切换页码
        otherConcatListPageChange: function (val) {
            ts.otherConcatCheckData.pageNum = val;
            ts.fetchOtherConcatList();
        },
        // 其他联系人切换页面大小
        otherConcatListSizeChange: function (val) {
            ts.otherConcatCheckData.pageSize = val;
            ts.fetchOtherConcatList();
        },
        // 委托人，单位联系人，其他联系人的表格操作
        handelClientUnitOther: function (row, type) {
            var ts = this,
                _sendData = {},
                _tip = '';
            ts.clientCurEditRow = row; //当前操作行赋值
            _sendData.linkmanInfoId = row.linkmanInfoId;
            _sendData.unitInfoId = ts.currentLoginInfo.unitInfoId;
            if (type == 'relieveAdmin') {
                _sendData.isAdministrators = 0;
                _tip = "您确定解除当前账号管理员身份吗？"
            } else if (type == 'setAdmin') {
                _sendData.isAdministrators = 1;
                _tip = "您确定设置当前账号为管理员吗？"
            } else if (type == 'relieveBind') {
                _sendData.isBindAccount = 0;
                _tip = "您确定解绑当前账号吗？"
            } else {
                // 绑定账号走模态框
                ts.clientAccountBindDialog = true;
                ts.clientAccountBindData.loginName = row.linkmanMobilePhone || row.linkmanCertNo;
                ts.clientAccountBindData.loginPwd = ""; //清掉上一次的数据
                return;
            }
            confirmMsg('', _tip, function () {
                ts.loading = true;
                request('', {
                    url: ctx + 'aea/supermarket/clientManage/putLinkman',
                    type: 'post',
                    data: _sendData,
                }, function (res) {
                    ts.loading = false;
                    if (res.success) {
                        if (['relieveAdmin', 'setAdmin', 'relieveBind'].indexOf(type) != -1) {
                            ts.fetchClientList();
                        } else {
                            ts.fetchUnitConcatList();
                        }
                    } else {
                        ts.$message({
                            message: '操作失败',
                            type: 'error'
                        });
                    }
                }, function (msg) {
                    ts.loading = false;
                    ts.$message({
                        message: '操作失败',
                        type: 'error'
                    });
                });
            })
        },
        // 绑定dialog绑定事件
        clientAccountBindSub: function () {
            var ts = this,
                _sendData = {};

            _sendData.linkmanInfoId = ts.clientCurEditRow.linkmanInfoId;
            _sendData.unitInfoId = ts.currentLoginInfo.unitInfoId;
            _sendData.isBindAccount = 1;
            _sendData.loginName = ts.clientAccountBindData.loginName;
            _sendData.loginPwd = ts.clientAccountBindData.loginPwd;

            if (!_sendData.loginPwd.length) return ts.$message({
                message: '请输入密码'
            })

            ts.loading = true;
            request('', {
                url: ctx + 'aea/supermarket/clientManage/putLinkman',
                type: 'post',
                data: _sendData,
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.clientAccountBindDialog = false; //隐藏dialog
                    ts.$message({
                        message: '绑定成功！',
                        type: 'success'
                    })
                    if (ts.isRelationOthers == 'list') {
                        ts.fetchUnitConcatList();
                    } else {
                        // 绑定成功后，重置 其他联系人表格
                        ts.otherConcatList = [];
                        ts.otherConcatTotal = 0;
                        ts.otherConcatCheckData = {
                            linkmanCertNo: '',
                            loginName: '',
                            pageNum: 1,
                            pageSize: 10,
                        };
                    }

                } else {
                    ts.$message({
                        message: '操作失败',
                        type: 'error'
                    });
                }
            }, function (msg) {
                ts.loading = false;
                ts.$message({
                    message: '操作失败',
                    type: 'error'
                });
            });
        },

        // 可报名项目
        // 可报名项目-菜单栏选中
        sideBarCanSignupProjectModuleSelect: function () {
            var ts = this;
            ts.isCanSignupPeojectInfoPandel = false;
            ts.fetchCanSignupProjectList();
        },
        // 获取可报名项目列表
        fetchCanSignupProjectList: function () {
            var ts = this;
            ts.canSignupPeojectCheckData.unitInfoId = ts.currentLoginInfo.unitInfoId;
            ts.loading = true;
            request('', {
                url: ctx + 'aea/supermarket/bidProjectManage/listCanBidAeaImProjPurchase',
                type: 'get',
                data: ts.canSignupPeojectCheckData,
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.canSignupProjectList = res.content.rows;
                    ts.canSignupProjectTotal = res.content.total;
                } else {
                    ts.$message({
                        message: '加载失败',
                        type: 'error'
                    });
                }
            }, function (msg) {
                ts.loading = false;
                ts.$message({
                    message: '加载失败',
                    type: 'error'
                });
            });
        },
        // 返回可报名项目列表
        returnCanSignupProjectList: function () {
            var ts = this;
            ts.isCanSignupPeojectInfoPandel = false;
            ts.isCanSignupProjectPandelState = 1;
        },
        // 展示当前竞选项目的详细信息
        showProjectInfoDetailPandel: function () {
            var ts = this;
            ts.isCanSignupPeojectInfoPandel = true;

        },
        // 点击展示当前项目的模块信息
        expandProjectInfoItem: function ($event) {
            var _mod = $($event.target);
            _mod.toggleClass('active');
        },

        // 模块加载
        moduleLoad: function (url, moduleName) {
            var url = './components/' + url;
            // $('.agency-content-wrap').html(' ');
            $.get(url + " " + moduleName, function (result) {
                $('.agency-content-wrap').html($(result))
            });
        },
    },
    filters: {
        // 身份证隐藏后几位
        sfzHideLast: function (num) {
            if (!num) return;
            var _cutNum = num.slice(0, 11);
            return num + "****";
        },
    },

});