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
                sort: {
                    field: 'isGreenWay,acceptTime',
                    sort: 'desc'
                },
                theme: '',
                acceptStartTime: '',
                acceptEndTime: '',
                applySource: '',
                applyType: '',
                instState: '',
                arriveStartTime: '',
                arriveEndTime: '',
                keyword: ''
            },

            isShowMsgDetail: false,
            state: state,
            msgDetail: {
                remindContent: '',
                sendUserName: '',
                sendDate: ''
            }

        }
    },
    methods: {
        //刷新列表
        fetchTableData: function () {

            var ts = this;

            this.searchFrom.pagination.pages = this.searchFrom.pagination.page;

            if (ts.searchFrom.acceptStartTime != '' && ts.searchFrom.acceptEndTime != '') {
                if (ts.searchFrom.acceptStartTime > ts.searchFrom.acceptEndTime) {
                    ts.apiMessage('受理开始时间不能大于结束时间', 'error');
                    return;
                }
            }

            if (ts.searchFrom.arriveStartTime != '' && ts.searchFrom.arriveEndTime != '') {
                if (ts.searchFrom.arriveStartTime > ts.searchFrom.arriveEndTime) {
                    ts.apiMessage('到达开始时间不能大于结束时间', 'error');
                    return;
                }
            }

            if (ts.searchFrom.sort["field"] == "acceptTime" && ts.searchFrom.sort["sort"] == "desc") {
                ts.searchFrom.sort["field"] = "isGreenWay,acceptTime";
            }

            ts.loading = true;
            var _url = ts.state == 1 ? ctx + 'rest/conditional/query/listDoneCancelTasksByBm' : ctx + 'rest/conditional/query/listWaitCancelTasksByBm';
            request('', {
                url: _url,
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
        //办理
        viewDetail: function (row) {
            var url = ctx + 'apanmis/page/stageApproveIndex?taskId=' + row.taskId + '&viewId=' + row.viewId + '&itemNature=' + row.itemNature;
            if (row.busRecordId) {
                url = url + '&busRecordId=' + row.busRecordId;
            }
            window.open(url, '_blank');
        },

        //判断是否已签收
        isSign: function (row) {
            if (row.bmSignState) {
                if ('1' == row.bmSignState) {
                    return true;
                }
            }
            return false;
        },

        //签收
        signTask: function (event, row) {
            var ts = this;
            var taskId = row.taskId;
            var viewId = row.viewId;
            var busRecordId = row.busRecordId;
            var iteminstCancelId = row.iteminstCancelId;
            event.preventDefault();
            ts.loading = true;
            request('bpmFrontUrl', {
                url: ctx + 'rest/applyinst/signUpIteminstCancelTask?iteminstCancelId=' + iteminstCancelId + '&taskId=' + taskId,
                type: 'get',
                data: {}
            }, function (result) {
                ts.loading = false;
                if (result.success) {
                    ts.$message.success(result.message);
                    window.setTimeout(function () {
                        window.open(ctx + 'apanmis/page/stageApproveIndex?taskId=' + taskId + '&viewId=' + viewId + '&busRecordId=' + busRecordId + '&itemNature=' + row.itemNature, '_blank');
                        window.location.reload();
                    }, 500);
                } else {
                    ts.$message.error(result.message);
                }
            }, function () {
                ts.loading = false;
                ts.$message.error("签收失败！");
            });
        },
        setRemindMessageRead: function (row, remindReceiverId) {

            var ts = this;
            request('', {
                url: ctx + 'rest/conditional/query/updateRemindRead',
                type: 'get',
                data: {'remindReceiverIds': remindReceiverId}
            }, function (res) {
                if (res.success) {
                    for (var i = 0; i < row.remindList.length; i++) {
                        if (row.remindList[i].remindReceiverId == remindReceiverId) {
                            row.remindList.splice(i, 1);
                            break;
                        }
                    }
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        showRemindInfo: function (row, remindInfo) {
            this.setRemindMessageRead(row, remindInfo.remindReceiverId);
            this.msgDetail.remindContent = remindInfo.remindContent;
            this.msgDetail.sendUserName = remindInfo.sendUserName;
            this.msgDetail.sendDate = this.formatDatetimeCommon(remindInfo.sendDate, 'yyyy-MM-dd hh:mm');
            this.isShowMsgDetail = true;
        },
        getFirstColumnWidth: function () {
            if (this.tableData) {
                for (var i = 0; i < this.tableData.length; i++) {
                    if (this.tableData[i].isGreenWay == '1') {
                        return '90';
                    }
                }
            }
            return '65';
        }

    },
    created: function () {
        this.conditionalQueryDic();
        this.fetchTableData();
    }
});