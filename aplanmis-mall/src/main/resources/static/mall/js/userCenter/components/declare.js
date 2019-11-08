var vm = new Vue({
    el:"#declareVue",
    data:{
        ctx: ctx,
        keyWord:'',
        pageNumDe:1,
        pageSizeDe:10,
        pageNumSearchDe:1,
        pageSizeSearchDe:10,
        totalDe:0,
        totalSeachDe:0,
        searchProData: [],
        searchProSearchData:[],
        itemVerId: '', // 单项申报时事项版本id
        // 是否展示申报窗口
        isShowDeclarePandel: false,
        // 当前编辑的项目数据
        curEditProjRow: {},
        projInfoId: '', // 当前申报项目id
    },
    mounted:function () {
        this.getprojList()
        this.GetRequest();
    },
    methods:{
        // 我要申报
        getprojList:function(){
            var vm = this;
            vm.applyinstFlag = 2;
            vm.searchBtnLoading = true
            request('', {
                url: ctx + 'rest/user/proj/list',
                type: 'get',
                data:{
                    pageNum:vm.pageNumDe,
                    pageSize:vm.pageSizeDe,
                }
            }, function (res) {
                vm.searchProSearchData = [];
                if(res.success){
                    vm.searchProData = res.content.list ||{};
                    vm.totalDe = res.content.total;
                }
            },function () {
            });
        },
        // 查询项目列表
        getSearchProjList:function(){
            var vm = this;
            vm.applyinstFlag = 2;
            vm.searchBtnLoading = true
            request('', {
                url: ctx + 'rest/user/searchProj/list',
                type: 'get',
                data:{
                    keyWord:vm.keyWord,
                    pageNum:vm.pageNumSearchDe,
                    pageSize:vm.pageSizeSearchDe,
                }
            }, function (res) {
                vm.searchProData = [];
                vm.searchProSearchData = res.content.list;
                vm.totalSearchDe = res.content.total;
            },function () {

            });
        },
        getProjectByLocalCode:function(){
            this.getSearchProjList();
        },
        handleSizeChangeDe:function(val){
            vm.pageSizeDe = val;
            this.getprojList()
        },
        handleCurrentChangeDe:function(val){
            vm.pageNumDe = val;
            this.getprojList()
        },
        handleSizeChangeSearchDe:function(val){
            vm.pageSizeSearchDe = val;
            this.getSearchProjList()
        },
        handleCurrentChangeSearchDe:function(val){
            vm.pageNumSearchDe = val;
            this.getSearchProjList()
        },

        // test
        // 并联单项申报页面加载
        parallelDeclareModuleLoad: function(item,flag){ // flag true并联 false单项
          var url = "";
          this.projInfoId=item.projInfoId;
          this.isShowDeclarePandel = true;
          if (document.location.protocol == "file:") {
              url = './components/parallelDeclare.html';
          } else {
            if(flag){
              url = ctx + 'rest/userCenter/apply/toParaApplyPage';
            }else {
              if(this.itemVerId){
                url = ctx + 'rest/userCenter/apply/series/toSingleApplyPage?itemVerId='+ this.itemVerId+'&&projInfoId='+this.projInfoId;
                location.search='?itemVerId='+ this.itemVerId+'&&projInfoId='+this.projInfoId+'&&flag=singleD'
              }else {
                // rest/main/toIndexPage#/guideIndex
                window.location.href = ctx + 'rest/main/toIndexPage?projInfoId='+this.projInfoId+'&&flag=singleD#/guideIndex';
                return false;
              }
            }
          }
          $.get(url, function (result) {
              $('#decalarePandel').html(result);
          });
        },
        GetRequest: function() {
          var url = location.search; //获取url中"?"符后的字串
          var theRequest = new Object();
          if (url.indexOf("?") != -1) {
              var str = url.substr(1);
              strs = str.split("&");
              for(var i = 0; i < strs.length; i ++) {
                  theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
              }
          }
          this.itemVerId=theRequest.itemVerId;
          var flag=true;
          if(theRequest.flag&&theRequest.flag=='singleD'){
            flag=false;
          }
          if(theRequest.projInfoId&&theRequest.projInfoId!='undefined'){
            this.projInfoId = theRequest.projInfoId;
            var data={projInfoId:theRequest.projInfoId?theRequest.projInfoId:null}
            this.parallelDeclareModuleLoad(data,flag);
          }
          return theRequest;
        },
        // 编辑-加载编辑页面
        splitProjectModuleLoad: function(row){
          var url = "";
          this.isShowDeclarePandel = true;
          this.curEditProjRow = row;
          if (document.location.protocol == "file:") {
              url = './components/splitProject.html';
          } else {
             url = ctx + 'rest/user/splitProj'
          }
          $.get(url, function (result) {
              $('#decalarePandel').html(result);
          });
        },
        // 隐藏编辑-项目详情页面
        hideSplitProjectModule: function(){
          this.isShowDeclarePandel = false;
          $('#decalarePandel').html('');
        },
    }
})