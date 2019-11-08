var vm = new Vue({
    el:"#material",
    data:{
        userId:'',
        pageNumDA:1,
        pageSizeDA:10,
        totalDA:0,
        materialListData: [],
    },
    mounted:function () {
        this.getMaterialList()
    },
    methods:{
        // 材料补全 --> 材料补全列表查询接口
        getMaterialList:function(){
            var vm = this;
            vm.applyinstFlag = 2;
            vm.searchBtnLoading = true;
            request('', {
                url: ctx + 'rest/user/matComplet/list',
                type: 'get',
                data:{
                    userId:vm.userId,
                    pageNum:vm.pageNumDA,
                    pageSize:vm.pageSizeDA,
                }
            }, function (res) {
                if(res.success){
                    vm.materialListData = res.content.list;
                    vm.totalDA = res.content.total;
                }else{
                    vm.$message.error(res.message)
                }
            },function () {
                vm.$message.error(res.message)
            });
        },

        // 补全材料列表页面
        approveMat: function(){
            var url = "";
            this.isShowDeclarePandel = true;
            if (document.location.protocol == "file:") {
                url = './components/uploadMatList.html';
            } else {
                url = ctx + 'rest/user/uploadMatList'
            }
            $.get(url, function (result) {
                $('#decalarePandel').html(result);
            });
        },

        getProjectByLocalCode:function(){
            this.getSearchProjList();
        },
        handleSizeChangeDe:function(val){
            this.pageSizeDe = val;
            this.getSearchProjList()
        },
        handleCurrentChangeDe:function(val){
            this.pageNumDe = val;
            this.getSearchProjList()
        },
    }
})