var vm = new Vue({
    el: '#searchTable',
    mixins: [mixins],
    data: function () {
        return {
            //查询数据
            searchFrom: {
                pagination: {
                    page: 1,
                    pages: 1,
                    perpage: 10
                },
                sort: {},
                theme: '',
                applyStartTime: '',
                applyEndTime: '',
                applySource: '',
                applyType: '',
                instState: '',
                dismissedStartTime: '',
                dismissedEndTime: '',
                keyword: ''
            },
            //节点选择窗口
            desActDialogVisible: false,
            //待选节点数组
            desActArr: [{destActName: "节点1111111111111", destActId: 123}, {
                destActName: "节点222222222222222222",
                destActId: 1222
            }],
            //当前操作节点的taskId
            currentTaskId: null,
            //选择发送的下一环节节点id
            nextTask: null,

        }
    },
    methods: {
        //刷新列表
        fetchTableData: function () {
            var ts = this;

            this.searchFrom.pagination.pages = this.searchFrom.pagination.page;

            ts.loading = true;

            request('', {
                url: ctx + '/rest/conditional/query/listUnCompleteFlow',
                type: 'get',
                data: ts.searchFrom
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.tableData = res.content.list;
                    ts.dataTotal = res.content.total;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        // 列表查询
        tableDataSearch: function () {
            this.searchFrom.pagination.page = 1;
            if (!this.searchFrom.keyword) return;
            this.fetchTableData();
        },
        //人工办理
        personOperation: function (row) {
            var ts = this;
            ts.loading = true;
            request('', {
                url: ctx + '/rest/conditional/query/checkDoCompleteFlow',
                type: 'get',
                data: {taskId: row.taskId}
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.currentTaskId = row.taskId;
                    var content = res.content;
                    if (content.length > 1) {
                        //多于一个节点则需要提示选择
                        ts.nextTask = null,
                            ts.desActArr = content;
                        ts.desActDialogVisible = true;
                    } else {
                        ts.$confirm('确定人工结束此节点吗?', '结束节点', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning',
                        }).then(function (obj) {
                            ts.doCompleteFlow(content[0].destActId);
                        }).catch(function () {
                        });
                    }
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });

        },
        //人工办理，完成接口
        doCompleteFlow: function (desActId) {
            var ts = this;
            ts.loading = true;
            request('', {
                url: ctx + '/rest/conditional/query/doCompleteFlow',
                type: 'get',
                data: {taskId: ts.currentTaskId, desActId: desActId}
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.apiMessage(res.message, 'success');
                    setTimeout(function () {
                        window.location.reload();
                    }, 1000);
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        doSendOperation: function () {
            var ts = this;
            if (!ts.nextTask) {
                return ts.apiMessage("必须选择一个节点", 'error')
            } else {
                ts.doCompleteFlow(ts.nextTask);
            }
        }


    },
    created: function () {
        // this.fetchTableData();
    }
});