var listmatter = (function(window){
    var vm = new Vue({
        el:"#listmatter",
        data:{
            ctx:ctx,
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
        },
        methods:{
            getlistMatter:function (stageId) {
                request('', {
                    url: ctx + 'rest/guide/itemAndState/list/'+ stageId,
                    type: 'get'
                }, function (res) {

                    if (res.success) {
                        var content = res.content;

                    } else {
                        vm.$message.error(res.message);
                    }

                }, function () {
                    vm.$message.error('获取事项一清单数据接口失败，请稍后重试！');
                });
            },
            gotoGuideIndex:function () {
                window.location.hash='/guideIndex';
                window.location.search='';
            }
        }
    })
})(window);