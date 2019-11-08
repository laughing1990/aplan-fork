// 0:办结1:办理中2:草稿
var vm = new Vue({
    el:"#draftsVue",
    data:{
        state:2,
        projName:'',
        localCode:'',
        keyword:'',
        pageNumDrafts:1,
        pageSizeDrafts:10,
        totalDrafts:0,
        draftsData: [],
    },
    mounted:function () {
        this.getDraftList()
    },
    methods:{
        getDraftList:function(){
            var vm = this;
            vm.applyinstFlag = 2;
            vm.searchBtnLoading = true;
            request('', {
                url: ctx + 'rest/user/draftApplyItem/list',
                type: 'get',
                data:{
                    state:vm.state,
                    localCode:vm.localCode,
                    keyword:vm.keyword,
                    projName:vm.projName,
                    pageNum:vm.pageNumDrafts,
                    pageSize:vm.pageSizeDrafts,
                }
            }, function (res) {
                vm.draftsData = res.content.list;
                vm.totalDrafts = res.content.total;
            },function () {

            });
        },
        handleSizeChangeDrafts:function(val){
            this.pageSizeDrafts = val;
            this.getDraftList()
        },
        handleCurrentChangeDrafts:function(val){
            this.pageNumDrafts = val;
            this.getDraftList()
        },
    }
})