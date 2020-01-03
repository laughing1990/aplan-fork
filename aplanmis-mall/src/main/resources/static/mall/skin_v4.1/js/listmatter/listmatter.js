var listmatter = (function(window){
    var vm = new Vue({
        el:"#listmatter",
        data:{
            ctx:ctx,
            show1:true,
            contentLoading:false,
            materialLoading:false,
            tableData: [{
                date: '2016-05-03',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'
            }, {
                date: '2016-05-02',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'
            }, {
                date: '2016-05-04',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'
            }, {
                date: '2016-05-01',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'
            }, {
                date: '2016-05-08',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'
            }, {
                date: '2016-05-06',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'
            }, {
                date: '2016-05-07',
                name: '王小虎',
                address: '上海市普陀区金沙江路 1518 弄'
            }],
            radio:'1',
            stageId:null,// 当前阶段id
            listmatterData:{},
            coreItemList:[],
            parallelItemList:[],
            itemStateList:[], // 事项情形列表
        },
        created:function(){
            this.GetRequest();
        },
        methods:{
            // 获取事项一单清
            getItemList:function (stageId) {
                var vm = this;
                this.contentLoading = true
                request('', {
                    url: ctx + 'rest/guide/itemAndState/list/'+ stageId,
                    type: 'get'
                }, function (res) {

                    if (res.success) {
                        vm.contentLoading = false;
                        var content = res.content;
                        vm.listmatterData = content;
                        vm.parallelItemList = content.parallelItemList; //  并联事项
                        vm.coreItemList = content.coreItemList; // 并行推进事项
                        vm.getMateriallist();
                    } else {
                        vm.$message.error(res.message);
                    }
                }, function () {
                    vm.contentLoading = false;
                    vm.$message.error('获取事项一清单数据接口失败，请稍后重试！');
                });
            },
            // 获取材料一单清
            getMateriallist:function(){
                var vm = this;
                this.materialLoading = true;
                var coreItemVerIds = [] , coreParentItemVerIds = [] ,paraParentllelItemVerIds = [] , parallelItemVerIds = [] ,stageStateIds = [] ,itemStateIds = [] ;
                vm.coreItemList.forEach(function (item,index) {
                    coreItemVerIds.push(item.itemVerId);
                    coreParentItemVerIds.push(item.baseItemVerId);
                });
                vm.parallelItemList.forEach(function (item,index) {
                    parallelItemVerIds.push(item.itemVerId);
                    paraParentllelItemVerIds.push(item.baseItemVerId);
                });
                var params = {
                    "coreItemVerIds":coreItemVerIds, // 并行事项版本ID数组(对应下面是标准事项下实施事项的事项版本id)
                    "coreParentItemVerIds":coreParentItemVerIds, // 并行标准事项版本ID数组
                    "paraParentllelItemVerIds":paraParentllelItemVerIds, // 并联标准事项版本ID数组(对应下面是标准事项下实施事项的事项版本id)
                    "parallelItemVerIds":parallelItemVerIds, // 并联事项版本ID数组
                    "stageId":vm.stageId, // 阶段id
                    "itemStateIds":itemStateIds, // 事项情形ID数组
                    "stageStateIds":stageStateIds, //阶段情形ID数组
                }
                console.log(params)
                $.ajax({
                    url: ctx + 'rest/userCenter/apply/mat/list',
                    type: 'post',
                    data:JSON.stringify(params),
                    contentType: 'application/json;charset=utf-8',
                    success:function(res){
                        vm.materialLoading = false;
                        if (res.success) {

                        } else {
                            vm.$message.error(res.message);
                        }
                    },
                    error:function () {
                        vm.materialLoading = false;
                        vm.$message.error('获取材料一清单数据接口失败，请稍后重试！');
                    }
                })
            },
            //根据事项版本号获取根情形列表
            currentItemVerIdChangehandle:function(itemVerId){
                this.getItemStateList(itemVerId);
            },
            getItemStateList:function(itemVerId){
                var vm = this;
                request('', {
                    url: ctx + 'rest/userCenter/apply/series/itemState/list/'+itemVerId,
                    type: 'get',
                }, function (res) {
                    if (res.success) {
                        vm.itemStateList = res.content;
                    } else {
                        vm.$message.error(res.message);
                    }

                }, function () {
                    vm.$message.error('根据事项版本号获取根情形列表接口失败，请稍后重试！');
                });
            },
            gotoGuideIndex:function () {
                window.location.hash='/';
                window.location.search='';
            },
            // 获取url参数
            GetRequest: function () {
                var url = location.search; //获取url中"?"符后的字串
                var theRequest = new Object();
                if (url.indexOf("?") != -1) {
                    var str = url.substr(1);
                    strs = str.split("&");
                    for (var i = 0; i < strs.length; i++) {
                        theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
                    }
                }
                this.stageId = theRequest.stageId? theRequest.stageId : null;
                this.getItemList(this.stageId);
                return theRequest;
            },
        }
    })
})(window);