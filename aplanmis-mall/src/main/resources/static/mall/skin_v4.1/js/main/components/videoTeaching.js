var vm = new Vue({
    el: "#videoTeach",
    data: {
        loading: false,
        ctx: ctx,
        // 当前政策法规tab
        activeTab: 0,
        // 所有政策法规类型
        allTabList: [{
            name: '视频教学',
            key: 'fs',
            tabIndex: 0
        }, {
            name: '报建手册',
            key: 'gd',
            tabIndex: 1
        }],
        // 视频教学名字
        videoNameData:[
            {
                name:"    1. 网厅使用讲解视频",
                src:'',
                nameTab:0
            },
            {
                name:"    2. 网厅申报讲解视频",
                src:'',
                nameTab:1
            },
            {
                name:"      3. 新手用户如何使用智能引导",
                src:'',
                nameTab:2
            },
            {
                name:"     4. 如何根据证照找到需要办理的事项",
                src:'',
                nameTab:3
            },
            {
                name:"     5. 网厅代办流程讲解视频",
                src:'',
                nameTab:4
            },
        ],
        // 当前播放视频Tab
        nameActiveTab:0,
        // 当前选中的list
        list: [],
        // 页面查询参数
        checkData: {
            pageNum: 1,
            pageSize: 10,
            keyword: ''
        },
        // 页面选中list的总数
        total: 0,
    },
    methods: {
        // 切换不同类型政策法规
        switchTab: function (tab) {
            this.activeTab = tab.tabIndex || 0;
            this.checkData.keyword = tab.name;
            this.fetchList();
        },
        // 获取当前类型下的政策法规列表
        fetchList: function(){
            var ts = this;
            ts.loading = true;
            request('', {
                url: ctx + 'rest/legal/list',
                type: 'get',
                data: ts.checkData
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.list = res.content.list;
                    ts.total = res.content.total;
                } else {
                    return ts.$message.error(res.message)
                }
            }, function () {
                ts.loading = false;
                return ts.$message.error('网络错误！')
            });
        },
        // 切换页
        handleCurrentChange: function(val){
            this.checkData.pageNum = val;
            this.fetchList();
        },
        // 切换大小
        handleSizeChange: function(val){
            this.checkData.pageSize = val;
            this.fetchList();
        },

        // 操作-查看
        jumpToView: function(row){
            var file_id = row.legalAtts[0].detailId;
            try {
                window.open(ctx + 'rest/file/att/preview/' + file_id);
            } catch (err) {
                this.$message.err('查看文件失败！')
            }
        },
    },
    created: function () {
        // 默认页面第一次获取佛山的
        this.switchTab(this.allTabList[0]);
        // test
        return
        this.checkData.keyword = '国务院';
        this.fetchList();
    },
})