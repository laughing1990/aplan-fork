var baseInfoVue = new Vue({
    el: "#baseInfo",
    data: {
        commentListLoading: false,
        showMasterItemMsg: true,
        showProjBaseInfo: true,
        showCommentlist: true,
        showParallelLsit: true,
        showMatinstList: true,
        showContactPeople: true,
        applyinstId: null, // 申报实例id
        processInstanceId: null, // 流程实例id
        taskId: null, // 任务ID
        busRecordId: null,

        matinstList: [],
        ParallelLsit: [],
        isShowMatlFileListDi: false,
        matinstFileList: [],
        matinstFileTotal: 0,
        matinstFilePageSize: 10,
        matinstFilePageNum: 1,
        dicCodeItems: [],
        masterItemMsg: {},
        /*********xiaohutu 新增字段**********/
        activeNames: ['1', '2', '3', '4', '5', '6'],//默认展开的列表
        verticalTabData: [ // 左侧纵向导航数据
            {
                label: '申报项目信息',
                target: 'applyDataInfo'
            }, {
                label: '项目基本信息',
                target: 'projInfo'
            }, {
                label: '办理意见列表',
                target: 'commentsInfo'
            }, {
                label: '事项列表',
                target: 'iteminstList'
            }, {
                label: '材料列表',
                target: 'matsList'
            }, {
                label: '联系人信息',
                target: 'linkInfo'
            }
        ],//滚动条
        applyDataForm: {},//申报项目信息
        projInfoForm: {},//项目详情
        commentlist: [],//审批意见列表
        linkInfoForm: {},//联系人信息
        activeTab: 0,  // 纵向导航active状态index
        showVerLen: 2, // 显示左侧纵向导航栏的长度
        projNatureList: [],//
        projTypeList: [],//

    },
    created: function () {
        var vm = this;
        vm.applyinstId = parent.app.masterEntityKey;  //申报实例id
        vm.processInstanceId = parent.app.processInstanceId;//流程实例id
        vm.busRecordId = parent.app.busRecordId // 并联才有
        vm.taskId = parent.app.taskId //任务ID
        vm.isSeries = parent.app.isSeriesinst  // 1为串联 0是并联
        this.getDicCodeItems(); //获取通用字典数据
        this.getCommentList(); // 获取办理意见列表
        this.getItemisnt() // 获取申报项目信息
        this.getProjInfo() // 取项目信息
        //this.getprojAnditemAndUnitInfo() // 获取项目信息
        this.getParallelIteminstList() // 获取并联 事项列表
        //获取数据字典
        this.getProjTypeNature('PROJECT_PROPERTY');
        this.getProjTypeNature('PROJECT_CLASS');

    },

    methods: {
        // 获取数据字典类型
        getProjTypeNature: function (code) {
            var _that = this;
            request('opus-admin', {
                url: ctx + 'rest/dict/code/items/list',
                type: 'get',
                data: {'dicCodeTypeCode': code}
            }, function (result) {
                if (result.content) {
                    if (code == 'PROJECT_PROPERTY') {
                        _that.projNatureList = result.content;
                    } else if (code == 'PROJECT_CLASS') {
                        _that.projTypeList = result.content;
                    }
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },
        //  并联审批页面查询项目 事项 单位 联系人 信息
        // getprojAnditemAndUnitInfo: function () {
        //     var vm = this
        //     var applyinstId = vm.applyinstId
        //     request('', {
        //         url: ctx + 'rest/project/getProjAnditeminstAndUnitInfo/' + applyinstId,
        //         type: 'get',
        //     }, function (res) {
        //         if (res.success) {
        //             var content = res.content
        //             vm.linkInfoForm = content
        //             var localCode = content.localCode
        //             vm.getprojectInfo(localCode)
        //         } else {
        //             vm.$message.error(res.message);
        //         }
        //     }, function () {
        //         //vm.$message.error("获取不到项目信息！");
        //     })
        // },
        // 申请项信息获取
        getItemisnt: function () {
            var vm = this;
            request('', {
                url: ctx + 'rest/approve/series/iteminst',
                data: {
                    applyinstId: vm.applyinstId,  // 申请实例id
                    busRecordId: vm.busRecordId,
                    isItemSeek: false,
                    taskId: vm.taskId    //任务id
                },
                type: 'get',
            }, function (res) {
                if (res.success) {
                    var content = res.content;
                    var itemInstId = content.itemInstId
                    vm.applyDataForm = content;
                    parent.app.iteminstId = itemInstId
                    if (vm.isSeries == '1') {
                        vm.getSeriesMatinstList(itemInstId) // 获取串联材料列表
                    } else {
                        vm.getParallelMatinstList(itemInstId) // 获取并联材料列表
                    }
                } else {
                    vm.$message.error(res.message);
                }
            }, function () {
                vm.$message.error("获取不到下个环节办理人范围！");
            })
        },
        //获取项目信息
        getProjInfo: function () {
            var vm = this;
            request('', {
                url: ctx + 'rest/project/getProjectInfoByApplyinstId',
                data: {
                    applyinstId: vm.applyinstId,  // 申请实例id
                },
                type: 'get',
            }, function (res) {
                if (res.success) {
                    vm.projInfoForm = res.content;
                } else {
                    vm.$message.error(res.message);
                }
            }, function () {
                vm.$message.error("获取项目信息失败！");
            })
        },
        // 根据编号code获取项目信息
        getprojectInfo: function (projectCode) {
            var vm = this;
            var projectCode = projectCode
            request('', {
                url: ctx + 'rest/project/getProjectInfo/' + projectCode,
                type: 'get',
            }, function (res) {
                if (res.success) {
                    vm.projInfoForm = res.content;
                } else {
                    vm.$message.error(res.message);
                }
            }, function () {
                vm.$message.error("获取不到项目信息！");
            })
        },
        // 查询审批意见列表
        getCommentList: function () {
            var vm = this;
            vm.commentListLoading = true;
            request('', {
                url: ctx + 'rest/bpm/approve/comment/list',
                type: 'get',
                data: {
                    applyinstId: vm.applyinstId,
                    processInstanceId: vm.processInstanceId
                }
            }, function (res) {
                vm.commentListLoading = false;
                if (res.success) {
                    vm.commentlist = res.content
                    console.log(vm.commentlist)
                } else {
                    console.log(res.message);
                }
            }, function () {
                vm.commentListLoading = false;
                console.log("查询办理意见列表请求接口出错");
            })
        },
        // 获取材料列表 （并联）
        getParallelMatinstList: function (itemInstId) {
            var vm = this;
            request('', {
                url: ctx + 'rest/approve/matinst/list',
                type: 'get',
                data: {
                    applyinstId: vm.applyinstId,
                    itemInstId: itemInstId
                }
            }, function (res) {
                if (res.success) {
                    vm.matinstList = res.content
                } else {
                    console.log(res.message);
                }
            }, function () {
                console.log("获取材料列表 （并联）失败");
            })
        },
        // 获取材料列表 （串联）
        getSeriesMatinstList: function (itemInstId) {
            var vm = this;
            request('', {
                url: ctx + 'rest/approve/series/matinst/list',
                type: 'get',
                data: {
                    iteminstId: itemInstId,
                    isSeries: 1
                }
            }, function (res) {
                if (res.success) {
                    vm.matinstList = res.content;
                } else {
                    console.log(res.message);
                }


            }, function () {
                console.log("查询串联材料列表失败");
            })
        },
        // 显示材料列表的已上传文件---fixme
        showAttMatinstWindow: function (matinstId) {
            var vm = this;
            vm.isShowMatlFileListDi = true;
            request('', {
                url: ctx + 'bpm/getAttFiles.do',
                type: 'post',
                data: {
                    matinstId: matinstId,
                }
            }, function (res) {
                vm.matinstFileList = res;
                vm.matinstFileTotal = res.length;
            }, function () {
                vm.$message.error("查询文件列表失败");
            })
        },
        // 下载已经上传的文件---fixme
        downloadSelectFile: function (row) {
            window.location.href = ctx + '/bpm/downloadAttachment.do?detailIds=' + row.fileId;
        },
        // 通用接口  根据dicCodeTypeCode获取数据字典
        getDicCodeItems: function () {
            var vm = this;
            request('', {
                url: ctx + 'rest/dict/code/items/list',
                type: 'get',
                data: {
                    dicCodeTypeCode: 'MAT_FROM',
                }
            }, function (res) {
                if (res.success) {
                    var result = res.content;
                    for (var i = 0; i < result.length; i++) {
                        var item = {};
                        item['itemCode'] = result[i]['itemCode'];
                        item['itemName'] = result[i]['itemName'];
                        vm.dicCodeItems.push(item);
                    }
                } else {
                    console.log(res.message);
                }
            }, function () {
                console.log("获取数据字典失败！");
            })
        },
        handleSizeChangeMatFile: function () {
            this.pageSize = val;
        },
        handleCurrentChangeMatFile: function () {
            this.page = val;
        },
        // 获取事项列表(并联)
        getParallelIteminstList: function () {
            var vm = this;
            request('', {
                url: ctx + 'rest/approve/iteminst/list',
                type: 'get',
                data: {
                    applyinstId: vm.applyinstId,
                    busRecordId: vm.busRecordId,// 业务记录id,
                    isItemSeek: false, // 是否意见征集，默认false
                }
            }, function (res) {
                if (res.success) {
                    vm.ParallelLsit = res.content
                } else {
                    vm.$message.error(res.message);
                }
            }, function () {
                vm.$message.error("请求接口出错了哦");
            })
        },
        showParState: function () {
            parent.app.checkStates()
        },
        toggle: function (item, index) {
            var numIndex = (index + 1).toString();
            this.activeNames.push(numIndex);
        },
        // 获取页面的URL参数
        QueryString: function (val) {
            var svalue = location.search.match(new RegExp("[\?\&]" + val + "=([^\&]*)(\&?)", "i"));
            return svalue ? svalue[1] : svalue;
        },
        //转化材料来源
        formatMatFrom: function (row, column, cellValue, index) {

        },
        //转换节点状态
        formatTaskState: function (row, column, cellValue, index) {
            var value = "处理中";
            switch (cellValue) {
                case '1':
                    value = '处理中';
                    break;
                case '2':
                    value = '已完成';
                    break;
                case '3':
                    value = '已回退';
                    break;
                case '4':
                    value = '已转交';
                    break;

            }
            return value;
        }
    },
    computed: {},
    filters: {
        formatDate: function (time) {
            var date = new Date(time);
            if (!date) return;
            return formatDate(date, 'yyyy-MM-dd hh:mm');
        },
        dicCodeItem: function (value) {
            if (value != null) {
                var arr = value.split(',');
                for (var j = 0; j < arr.length; j++) {
                    for (var i = 0; i < baseInfoVue.dicCodeItems.length; i++) {
                        var item = baseInfoVue.dicCodeItems[i];
                        if (item["itemCode"] == arr[j]) {
                            arr[j] = item["itemName"];
                            break;
                        }
                    }
                }
                return arr.join(',');
            }
            return value;
        },
        changeProjNatureFromDic: function (value) {
            if (value != null) {
                for (var i = 0; i < baseInfoVue.projNatureList.length; i++) {
                    var item = baseInfoVue.projNatureList[i];
                    if (item["itemCode"] == value) {
                        return item["itemName"];
                    }
                }
            }
            return value;
        },
        changeProjTypeFromDic: function (value) {
            if (value != null) {
                for (var i = 0; i < baseInfoVue.projTypeList.length; i++) {
                    var item = baseInfoVue.projTypeList[i];
                    if (item["itemCode"] == value) {
                        return item["itemName"];
                    }
                }
            }
            return value;
        },
    }
})
