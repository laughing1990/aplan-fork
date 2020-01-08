var listmatter = (function(window){
    var vm = new Vue({
        el:"#listmatter",
        data:{
            ctx:ctx,
            show1:true,
            show2:true,
            show3:true,
            contentLoading:false,
            materialLoading:false,
            radio:'1',
            stageId:null,// 当前阶段id
            listmatterData:{},
            coreItemList:[],
            coreItemCheckData:[], // 筛选出isDoneItem为“1”的数据
            noRequireCoreItemCheckData:[], // 非必选的并行推进事项（用户后来可以自由勾选的）
            parallelItemList:[],
            parallelItemCheckData:[],// 筛选出isDoneItem为“1”的数据
            noRequireParallelItemCheckData:[], // 非必选的并联事项（用户后来可以自由勾选的）
            itemStateList:[], // 事项情形列表
            requireMat:[],// 必选材料
            noRequireMat:[], //可选材料

            AIleadDialogFlag:false, // 智能引导弹出框
            stateList: [], // 情形列表
            stateSelVal: {},
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
                        vm.stateList = content.stateList;
                        vm.listmatterData = content;
                        vm.parallelItemList = content.parallelItemList; //  并联事项
                        //筛选出isDoneItem为“1”的数据，该数据勾选状态需要设置为勾选且禁用
                        if(vm.parallelItemList.length > 0){
                             vm.parallelItemCheckData = vm.parallelItemList.filter(function (item) {
                                return item.isDoneItem == '1'
                            });
                            vm.$nextTick(function(){
                                if(vm.parallelItemCheckData.length > 0){
                                    vm.parallelItemCheckData.forEach(function (item) {
                                        vm.$refs.parallelItematable.toggleRowSelection(item,true);
                                    })
                                }
                            })
                        }
                        vm.coreItemList = content.coreItemList; // 并行推进事项
                        //筛选出isDoneItem为“1”的数据，该数据勾选状态需要设置为勾选且禁用
                        if(vm.coreItemList.length >0 ){
                             vm.coreItemCheckData = vm.coreItemList.filter(function (item) {
                               return item.isDoneItem == '1';
                            });
                            vm.$nextTick(function () {
                                if(vm.coreItemCheckData.length > 0){
                                    vm.coreItemCheckData.forEach(function (item) {
                                        vm.$refs.coreItemListTable.toggleRowSelection(item.true);
                                    })
                                }
                            })
                        }
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

                // 默认勾选的事项，用户不能操作勾选
                if(vm.parallelItemCheckData.length > 0){
                    vm.parallelItemCheckData.forEach(function (item) {
                        if(item.currentCarryOutItem.itemVerId){
                            parallelItemVerIds.push(item.currentCarryOutItem.itemVerId);
                        }
                        parallelItemVerIds.push(item.currentCarryOutItem.itemVerId);
                        paraParentllelItemVerIds.push(item.itemVerId);
                    })
                }
                if(vm.coreItemCheckData.length > 0){
                    vm.coreItemCheckData.forEach(function (item) {
                        if(item.currentCarryOutItem.itemVerId){
                            coreItemVerIds.push(item.currentCarryOutItem.itemVerId);
                        }
                        coreParentItemVerIds.push(item.itemVerId);
                    })
                }

                // 非必选，用户后来自由勾选
                if(vm.noRequireParallelItemCheckData.length > 0){
                    vm.noRequireParallelItemCheckData.forEach(function (item) {
                        if(item.currentCarryOutItem.itemVerId){
                            parallelItemVerIds.push(item.currentCarryOutItem.itemVerId);
                        }
                        paraParentllelItemVerIds.push(item.itemVerId);
                    })
                }
                if(vm.noRequireCoreItemCheckData.length > 0){
                    vm.noRequireCoreItemCheckData.forEach(function (item) {
                        if(item.currentCarryOutItem.itemVerId){
                            coreItemVerIds.push(item.currentCarryOutItem.itemVerId);
                        }
                        coreParentItemVerIds.push(item.itemVerId);
                    })
                }

                var params = {
                    "paraParentllelItemVerIds":paraParentllelItemVerIds, // 并联标准事项版本ID数组
                    "parallelItemVerIds":parallelItemVerIds, // 并联事项版本ID数组(对应下面是标准事项下实施事项的事项版本id)
                    "coreParentItemVerIds":coreParentItemVerIds, // 并行标准事项版本ID数组
                    "coreItemVerIds":coreItemVerIds, // 并行事项版本ID数组(对应下面是标准事项下实施事项的事项版本id)
                    "stageId":vm.stageId, // 阶段id
                    "itemStateIds":itemStateIds, // 事项情形ID数组
                    "stageStateIds":stageStateIds, //阶段情形ID数组
                }
                console.log(params)
                $.ajax({
                    url: ctx + 'rest/guide/mat/list',
                    type: 'post',
                    data:JSON.stringify(params),
                    contentType: 'application/json;charset=utf-8',
                    success:function(res){
                        vm.materialLoading = false;
                        if (res.success) {
                            var content = res.content;
                            vm.requireMat = content.requireMat;
                            vm.noRequireMat = content.noRequireMat;
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
            //二、单独勾选表格中某条数据所对应的勾选框
            selectParallelItem:function(selection,row){
                var vm = this;
                vm.noRequireParallelItemCheckData = [];
                selection.forEach(function (item) {
                    if(item){
                        vm.noRequireParallelItemCheckData.push(item);
                    }
                })
                vm.getMateriallist();
            },
            selectCoreItem:function(selection,row){
                var vm = this;
                vm.noRequireCoreItemCheckData = [];
                selection.forEach(function (item) {
                    if(item){
                        vm.noRequireCoreItemCheckData.push(item);
                    }
                })
                vm.getMateriallist();
            },
            // 控制事项一单清是否可以勾选
            selectable:function(row,index) {
                var vm = this;
                if(row.isDoneItem=='1'){
                   return  false
                }else {
                    return true
                }
            },
            gotoGuideIndex:function () {
                window.location.hash='/DeclarGuidePage';
                window.location.search='';
            },
            // 跳转智能引导页
            openAILeadDia:function(){
                this.AIleadDialogFlag = true;
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