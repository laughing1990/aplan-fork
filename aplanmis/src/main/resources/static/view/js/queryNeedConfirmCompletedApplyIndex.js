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
                    field: 'correctTime',
                    sort: 'desc'
                },
                theme: '',
                applyStartTime: '',
                applyEndTime: '',
                applySource: '',
                applyType: '',
                correctStartTime:'',
                correctEndTime:'',
                keyword:''
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

            if(ts.searchFrom.correctStartTime!='' && ts.searchFrom.correctEndTime!=''){
                if(ts.searchFrom.correctStartTime > ts.searchFrom.correctEndTime){
                    ts.apiMessage('补全开始时间不能大于设定补全结束时间', 'error');
                    return;
                }
            }

            ts.loading = true;

            request('', {
                url: ctx + '/rest/conditional/query/listNeedConfirmCompletedApply',
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
        //补全确认
        viewDetail:function (row) {
            var correctId = row.correctId;
            var urltemp = ctx + '/applyinst/correct/applyinstConfirm.html?applyinstCorrectId=' + correctId;
            var parentIfreamUrl = window.frames.location.href;
            var currentTabId = parent.vm.activeTabIframe;
            urltemp = urltemp + '&parentIfreamUrl=' + parentIfreamUrl + '&currentTabId' + currentTabId;
            var data = {
                'menuName':'材料补全确认',
                'menuInnerUrl':urltemp,
                'id': 'dqr' + correctId
            };
            try{
                parent.vm.addTab('',data,'','');
            }catch (e) {
                window.open(urltemp,'_blank');
            }
        }

    },
    created: function () {
        this.conditionalQueryDic();
        this.fetchTableData();
    }
});