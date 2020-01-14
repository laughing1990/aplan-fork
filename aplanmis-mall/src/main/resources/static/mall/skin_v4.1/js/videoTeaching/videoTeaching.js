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
                src:'https://vdept.bdstatic.com/483332573134486144696c4737557067/335a663737756478/f4443076bee0709a11bf70d362caf2eaa6e7e90637eb20a37448d1b379d146988a68ee6e53b9af7f3acb7fa80fe71e3f.mp4?auth_key=1578976730-0-0-32ac44d1e4fd61b4e52095f03b5c2f8e',
                nameTab:0
            },
            {
                name:"    2. 网厅申报讲解视频",
                src:'https://vdept.bdstatic.com/704c3444703966536d434a504c784c74/795973634c42486e/4fc5b20d7a4f8ef774bee073d4fe54eb0ed2b8f339a4e3844693a5077eac60e053e799dbf865a76612af372e1b726335.mp4?auth_key=1578976792-0-0-d63adfe0a673756d6b0c1717354721a4',
                nameTab:1
            },
            {
                name:"      3. 新手用户如何使用智能引导",
                src:'https://vdept.bdstatic.com/446b796b596c64377364444848557354/7143746843503348/3839954a814de8a7fdfd8543ca48cc938afd16069bcac22d723401903348e5d0584e96dc1f73f216d2fefb5659725173.mp4?auth_key=1578976820-0-0-00a1e6586dca1e64d76a05d592fe1e94',
                nameTab:2
            },
            {
                name:"     4. 如何根据证照找到需要办理的事项",
                src:'https://vdept.bdstatic.com/754737774851656b726b4a4469593432/787a6836734c6c57/eec1a8b7e7f3307287db972d7494e3a7fe58e349d337c8e0df306d8c016cfa200918dd5653f48c3dfef905b886a2874d.mp4?auth_key=1578976841-0-0-fe0f988bd7b559c2a5c4abb2841f1799',
                nameTab:3
            },
            {
                name:"     5. 网厅代办流程讲解视频",
                src:'https://vdept.bdstatic.com/44635438313547707053347676745477/444250586d334535/bb1a5158153c96d209010d2c298f1e96c5254ab726803b11307c2218de20641b8fcb311717f2b2257a675e3428e7cfbb.mp4?auth_key=1578976902-0-0-3fd64752584d25df563377c0f69dc014',
                nameTab:4
            },
        ],
        // 当前播放视频Tab
        nameActiveTab:0,
        // 当前播放的视频
        movieUrl:'',
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
        videoHandleFn:function (item) {
            console.log(item)
            this.movieUrl = item.src;
            this.nameActiveTab = item.nameTab;
        }
    },
    created: function () {
        // 默认页面第一次获取佛山的
        this.switchTab(this.allTabList[0]);
        this.movieUrl = this.videoNameData[0].src;
        // test
        return
        this.checkData.keyword = '国务院';
        this.fetchList();
    },
})