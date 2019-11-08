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
                applyStartTime: '',
                applyEndTime: '',
                applyType: '',
                keyword:''
            },
            sendApplyVisible:false,
            sendApplyLoading:false,
            sendApplyComment: {
                comment: ''
            },
            applyinstId:''
        }
    },
    methods: {
        //刷新列表
        fetchTableData: function () {
            var ts = this;

            this.searchFrom.pagination.pages = this.searchFrom.pagination.page;

            if(ts.searchFrom.applyStartTime!='' && ts.searchFrom.applyEndTime!=''){
                if(ts.searchFrom.applyStartTime > ts.searchFrom.applyEndTime){
                    ts.apiMessage('收件开始时间不能大于结束时间', 'error');
                    return;
                }
            }

            ts.loading = true;

            request('', {
                url: ctx + 'rest/conditional/query/listMyDrafts',
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
        //发起申报
        sendApply: function () {
            var ts = this;
            confirmMsg('提示信息：','你确定发起申报吗？',function(){
                ts.sendApplyLoading = true;
                request('', {
                    url: ctx + "rest/apply/common/processflow/start",
                    type: 'post',
                    data: {applyinstId: ts.applyinstId,comment:ts.sendApplyComment.comment}
                }, function (result) {
                    ts.sendApplyLoading = false;
                    if (result.success) {
                        this.sendApplyVisible = false;
                        ts.$message.success('申报成功！');

                        window.setTimeout(function(){
                            location.reload();
                        }, 500);
                    }else{
                        ts.apiMessage('发起申报失败!', 'error');
                    }
                },function(){
                    ts.sendApplyLoading = false;
                    ts.apiMessage('发起申报失败!', 'error');
                },'确定','取消','warning',true);
            });
        },
        openSendApplyDialog:function (row) {
            this.sendApplyVisible = true;
            this.applyinstId = row.applyinstId;
        },
        closeSendApplyDialog: function (){
            this.$refs.sendApplyRef.resetFields();
        }
    },
    created: function () {
        this.conditionalQueryDic();
        this.fetchTableData();
    }
});