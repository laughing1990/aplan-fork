var vm = new Vue({
    el:"#approveVue",
    data:{
        dataLoading:false,
        projName:'',
        localCode:'',
        keyword:'',
        pageSizeDe:10,
        pageNumDe:1,
        totalDe:0,
        ApproveListData: [],
        noDataTip:'',
    },
    mounted:function () {
        this.getApproveList()
    },
    methods:{
        // 审批情况 --> 审批情况列表查询接口
        getApproveList:function(){
            var vm = this;
            vm.noDataTip = ""
            vm.applyinstFlag = 2;
            vm.searchBtnLoading = true;
            vm.dataLoading = true;
            request('', {
                url: ctx + 'rest/user/approve/list',
                type: 'get',
                data:{
                    localCode:vm.localCode,
                    projName:vm.projName,
                    keyword:vm.keyword,
                    pageNum:vm.pageNumDe,
                    pageSize:vm.pageSizeDe,
                }
            }, function (res) {
                vm.dataLoading = false;
                if(res.success){
                    vm.ApproveListData = res.content.list;
                    if(vm.ApproveListData.length <= 0){
                        vm.noDataTip = "暂无数据"
                    }
                    vm.totalDe = res.content.total;
                }else{
                    vm.$message.error(res.message)
                }
            },function () {
                vm.dataLoading = false;
                vm.$message.error(res.message)
            });
        },
        getProjectByLocalCode:function(){
            this.getSearchProjList();
        },
        handleSizeChangeDe:function(val){
            this.pageSizeDe = val;
            this.getApproveList()
        },
        handleCurrentChangeDe:function(val){
            this.pageNumDe = val;
            this.getApproveList()
        },
    }
})