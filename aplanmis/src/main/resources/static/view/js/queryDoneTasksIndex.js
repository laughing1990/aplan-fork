var vm = new Vue({
    el: '#searchTable',
    mixins: [mixins],
    data: function () {
        return {
            //查询数据
            searchFrom: {
                pagination:{
                    page:  1,
                    pages: 1,
                    perpage: 10
                },
                sort:{
                    field: 'processTime',
                    sort: 'desc'
                },
                theme: '',
                acceptStartTime: '',
                acceptEndTime: '',
                applySource: '',
                applyType: '',
                instState:'',
                processStartTime:'',
                processEndTime:'',
                keyword:''
            },

            isShowMsgDetail:false,
            msgDetail :{
                remindContent:'',
                sendUserName:'',
                sendDate:''
            }
        }
    },
    components:{
        'v-send-remind': SendRemind
    },
    methods: {
        //刷新列表
        fetchTableData: function () {
            var ts = this;

            this.searchFrom.pagination.pages = this.searchFrom.pagination.page;

            if(ts.searchFrom.acceptStartTime!='' && ts.searchFrom.acceptEndTime!=''){
                if(ts.searchFrom.acceptStartTime > ts.searchFrom.acceptEndTime){
                    ts.apiMessage('受理开始时间不能大于结束时间', 'error');
                    return;
                }
            }

            if(ts.searchFrom.processStartTime!='' && ts.searchFrom.processEndTime!=''){
                if(ts.searchFrom.processStartTime > ts.searchFrom.processEndTime){
                    ts.apiMessage('办理开始时间不能大于结束时间', 'error');
                    return;
                }
            }

            ts.loading = true;

            request('', {
                url: ctx + 'rest/conditional/query/listDoneTasks',
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
        //查看详情
        viewDetail:function (row) {
            var url = ctx+'apanmis/page/stageApproveIndex?taskId='+row.taskId+'&viewId='+row.viewId;
            if(row.busRecordId){
                url = url + '&busRecordId='+row.busRecordId;
            }
            window.open(url,'_blank');
        },
        //撤回
        withdrawTask:function (row) {
            var ts = this;
            confirmMsg("确认信息", "确认撤回任务吗？", function () {
                ts.loading = true;
                request('bpmFrontUrl', {
                    url: ctx + 'rest/front/task/withdrawTask/' + row.taskId,
                    type: 'get',
                    data: {}
                }, function (result) {
                    ts.loading = false;
                    if (result.success) {
                        ts.$message.success(result.message);
                    } else {
                        ts.$message.error(result.message);
                    }
                }, function () {
                    ts.loading = false;
                    ts.$message.error("撤回失败！");
                });
            });
        },

        setRemindMessageRead:function (row,remindReceiverId) {

            var ts = this;
            request('', {
                url: ctx + 'rest/conditional/query/updateRemindRead',
                type: 'get',
                data: {'remindReceiverIds':remindReceiverId}
            }, function (res) {
                if (res.success) {
                    for(var i=0;i<row.remindList.length;i++){
                        if(row.remindList[i].remindReceiverId==remindReceiverId){
                            row.remindList.splice(i,1);
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
        showRemindInfo:function (row,remindInfo) {
            this.setRemindMessageRead(row,remindInfo.remindReceiverId);
            this.msgDetail.remindContent = remindInfo.remindContent;
            this.msgDetail.sendUserName = remindInfo.sendUserName;
            this.msgDetail.sendDate = this.formatDatetimeCommon(remindInfo.sendDate,'yyyy-MM-dd hh:mm');
            this.isShowMsgDetail = true;
        }
    },
    created: function () {
        this.conditionalQueryDic();
        this.fetchTableData();
    }
});