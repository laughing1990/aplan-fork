var listmatter = (function(window){
    var vm = new Vue({
        el:"#listmatter",
        data:{
            ctx:ctx,
            contentLoading:false,
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
        },
        created:function(){
            this.GetRequest();
        },
        methods:{
            getlistMatter:function (stageId) {
                var vm = this;
                this.contentLoading = true
                request('', {
                    url: ctx + 'rest/guide/itemAndState/list/'+ stageId,
                    type: 'get'
                }, function (res) {

                    if (res.success) {
                        vm.contentLoading = false;
                        var content = res.content;

                    } else {
                        vm.$message.error(res.message);
                    }

                }, function () {
                    vm.contentLoading = false;
                    vm.$message.error('获取事项一清单数据接口失败，请稍后重试！');
                });
            },
            gotoGuideIndex:function () {
                window.location.hash='/guideIndex';
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
                this.getlistMatter(this.stageId);
                return theRequest;
            },
        }
    })
})(window);