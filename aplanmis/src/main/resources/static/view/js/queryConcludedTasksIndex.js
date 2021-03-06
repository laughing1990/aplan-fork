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
                    field: 'isGreenWay,concludedTime',
                    sort: 'desc'
                },
                theme: '',
                acceptStartTime: '',
                acceptEndTime: '',
                applySource: '',
                applyType: '',
                instState:'',
                concludedStartTime:'',
                concludedEndTime:'',
                keyword:''
            }

        }
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

            if(ts.searchFrom.concludedStartTime!='' && ts.searchFrom.concludedEndTime!=''){
                if(ts.searchFrom.concludedStartTime > ts.searchFrom.concludedEndTime){
                    ts.apiMessage('办结开始时间不能大于结束时间', 'error');
                    return;
                }
            }

            if(ts.searchFrom.sort["field"] == "concludedTime" && ts.searchFrom.sort["sort"] == "desc"){
                ts.searchFrom.sort["field"] = "isGreenWay,concludedTime";
            }

            ts.loading = true;

            request('', {
                url: ctx + 'rest/conditional/query/listConcludedTasks',
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
        showOrHideGreenWay:function () {
            if(this.tableData){
                for(var i=0;i<this.tableData.length;i++){
                    if(this.tableData[i].isGreenWay=='1'){
                        return true;
                    }
                }
            }
            return false;
        }

    },
    created: function () {
        this.conditionalQueryDic();
        this.fetchTableData();
    }
});