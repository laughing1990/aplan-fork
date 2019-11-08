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
                    field: 'applyTime',
                    sort: 'desc'
                },
                theme: '',
                approvalStage: '',
                applyStartTime: '',
                applyEndTime: '',
                applySource: '',
                applyType: '',
                iteminstState:'',
                instState:'',
                keyword:'',
                sublineType:sublineType
            }
        }
    },
    methods: {
        //刷新列表
        fetchTableData: function () {
            var ts = this;

            this.searchFrom.pagination.pages = this.searchFrom.pagination.page;

            if(ts.searchFrom.applyStartTime!='' && ts.searchFrom.applyEndTime!=''){
                if(ts.searchFrom.applyStartTime > ts.searchFrom.applyEndTime){
                    ts.apiMessage('申报开始时间不能大于结束时间', 'error');
                    return;
                }
            }

            ts.loading = true;

            request('', {
                url: ctx + 'rest/conditional/query/listSublineParts',
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

        //获取查询条件的数据
        conditionalQueryDic: function () {
            var ts = this;
            // ts.loading = true;
            request('', {
                url: ctx + 'rest/conditional/query/parts/dic/list',
                type: 'get',
                data: {}
            }, function (res) {
                // ts.loading = false;
                if (res.success) {
                    ts.themeList = res.content.themeList;
                    ts.applySourceList = res.content.applySourceList;
                    ts.applyTypeList = res.content.applyTypeList;
                    ts.applyStateList = res.content.applyStateList;
                    ts.iteminstStateList = res.content.iteminstStateList;
                    ts.instStateList = res.content.instStateList;
                    ts.globalThemeList = res.content.themeList;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                // ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        //重置
        clearSearch:function(){
            this.searchFrom.theme='';
            this.searchFrom.approvalStage='';
            this.searchFrom.applyStartTime='';
            this.searchFrom.applyEndTime='';
            this.searchFrom.applySource='';
            this.searchFrom.applyType='';
            this.searchFrom.iteminstState='';
            this.searchFrom.instState='';
            this.searchFrom.keyword='';
            this.tableDataSearch();
        },
        //导出
        exportExcel:function(){
            var param = encodeURI(JSON.stringify(this.searchFrom));
            window.location.href = ctx + "rest/conditional/query/export/sublineParts" + "?param=" + param;
        },
        //查看详情
        viewDetail:function (row) {
            var ts = this;
            ts.loading = true;
            request('', {
                url: ctx + 'rest/conditional/query/queryItemInfoTaskId',
                type: 'get',
                data: {iteminstId: row.iteminstId,handler:handler}
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    window.open(ctx+'apanmis/page/stageApproveIndex?isNotCompareAssignee=true&taskId='+res.content.taskId+'&viewId='+res.content.viewId,'_blank');
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        clearThemeList : function () {
            var ts = this;
            if(ts.searchFrom.applyType=='1'){
                ts.themeList = [];
                ts.searchFrom.theme='';
                ts.searchFrom.approvalStage='';
            }else{
                if(ts.globalThemeList.lenght==0){
                    ts.conditionalQueryDic();
                }else{
                    ts.themeList = ts.globalThemeList;
                }
            }

            this.tableDataSearch();
        }

    },
    created: function () {
        this.conditionalQueryDic();
        this.fetchTableData();
    }
});