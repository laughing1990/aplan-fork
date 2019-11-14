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
                    field: 'concludedTime',
                    sort: 'desc'
                },
                //行政分区
                region:'',
                projType: '',
                theme: '',
                investCategory: '',
                industry: '',
                buildNature: '',
                applyStartTime: '',
                applyEndTime: '',
                applySource: '',
                concludedStartTime:'',
                concludedEndTime:'',
                projLevel: '',
                applyType: '',
                keyword:'',
                stageIndex:stageIndex
            },

            //所在区域
            regionalismList: [],
            //立项类型
            projTypeList:[],
            //投资类型
            investCategoryList:[],
            //行业分类
            industryList:[],
            //建设性质
            buildNatureList:[],
            //项目级别
            projLevelList:[]

        }
    },
    components:{
        'v-sync-tree-panel':SyncTreePanel,
        'v-sync-cascader':SyncCascader
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

            if(ts.searchFrom.concludedStartTime!='' && ts.searchFrom.concludedEndTime!=''){
                if(ts.searchFrom.concludedStartTime > ts.searchFrom.concludedEndTime){
                    ts.apiMessage('办结开始时间不能大于结束时间', 'error');
                    return;
                }
            }

            if(this.$refs.regionalismSyncCascader) {
                this.searchFrom.region = this.$refs.regionalismSyncCascader.getSelectecValue();
            }

            if(this.$refs.industryTreePanel) {
                this.searchFrom.industry = this.$refs.industryTreePanel.getSelectecValue("itemCode");
            }

            ts.loading = true;

            request('', {
                url: ctx + 'rest/conditional/query/listStageConcludedApplyInfo',
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
                url: ctx + 'rest/conditional/query/apply/dic/list',
                type: 'get',
                data: {}
            }, function (res) {
                // ts.loading = false;
                if (res.success) {
                    // ts.regionalismList = res.content.regionalismList;
                    ts.themeList = res.content.themeList;
                    ts.projTypeList = res.content.projTypeList;
                    ts.investCategoryList = res.content.investCategoryList;
                    ts.industryList = res.content.industryList;
                    ts.buildNatureList = res.content.buildNatureList;
                    ts.applySourceList = res.content.applySourceList;
                    ts.applyTypeList = res.content.applyTypeList;
                    ts.projLevelList = res.content.projLevelList;
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
            this.searchFrom.region='';
            this.searchFrom.projType='';
            this.searchFrom.theme='';
            this.searchFrom.investCategory='';
            this.searchFrom.industry='';
            this.searchFrom.buildNature='';
            this.searchFrom.applyStartTime='';
            this.searchFrom.applyEndTime='';
            this.searchFrom.concludedStartTime='';
            this.searchFrom.concludedEndTime='';
            this.searchFrom.applySource='';
            this.searchFrom.projLevel='';
            this.searchFrom.applyType='';
            this.searchFrom.keyword='';
            //清空行政区划
            if(this.$refs.regionalismSyncCascader) {
                this.$refs.regionalismSyncCascader.resetChecked();
            }
            //清空行业树
            if(this.$refs.industryTreePanel) {
                this.$refs.industryTreePanel.resetChecked();
            }
            this.tableDataSearch();
        },
        //查看详情
        viewDetail:function (row) {
            var ts = this;
            ts.loading = true;
            request('', {
                url: ctx + 'rest/conditional/query/queryApplyInfoTaskId',
                type: 'get',
                data: {applyinstId: row.applyinstId}
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