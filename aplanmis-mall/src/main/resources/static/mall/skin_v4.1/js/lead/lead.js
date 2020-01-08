(function(){
    var vm = new Vue({
        el:"#lead",
        data:{
            projInfoDetail:{},
            guideList: [], // 主题智能引导
            guideSelVal: {}, // 主题智能引导选项

            stageSelVal: {}, // 选择阶段信息
            stateList: [], // 情形列表
        },
        created:function(){
            this.getFactorList();

        },
        methods:{
            // 获取智能根因子列表
            getFactorList:function () {
                var vm = this;
                request('', {
                    url: ctx + 'rest/userCenter/apply/factor/list',
                    type: 'get',
                }, function (res) {
                    if (res.success) {
                        vm.guideList = res.content;
                        if(vm.guideList && vm.guideList.length > 0){
                            vm.guideList.map(function(item){
                                if (typeof item.selectAnswerId == 'undefined' || item.selectAnswerId == undefined) {
                                    Vue.set(item, 'selectAnswerId', '');
                                }
                                if (item.answerType != 's' && item.answerType != 'y') {
                                    Vue.set(item, 'selValue', []);
                                    item.selectAnswerId = item.selValue;
                                }
                                item.answerFactors = vm.sortByKey(item.answerFactors, 'sortNo');
                            });
                        }
                    }else {
                        vm.$message({
                            message: res.message?res.message:'获取智能引导失败！',
                            type: 'error'
                        })
                    }
                },function(res){
                    vm.$message({
                        message: res.message?res.message:'获取智能引导失败！',
                        type: 'error'
                    })
                });
            },
            //根据父因子获取子因子列表
            getChildFactorList:function (factorId) {
                var vm = this;
                request('', {
                    url: ctx + 'rest/userCenter/apply/factor/child/list/' + factorId,
                    type: 'get',
                }, function (res) {
                    if (res.success) {
                        vm.guideList = res.content;

                    }else {
                        vm.$message({
                            message: res.message?res.message:'获取智能引导失败！',
                            type: 'error'
                        })
                    }
                },function(res){
                    vm.$message({
                        message: res.message?res.message:'获取智能引导失败！',
                        type: 'error'
                    })
                });
            },
            sortByKey: function (array, key) {  //(数组、排序的列)
                return array.sort(function (a, b) {
                    var x = a[key];
                    var y = b[key];
                    return ((x < y) ? -1 : ((x > y) ? 1 : 0));
                });
            },
        }
    })
})();