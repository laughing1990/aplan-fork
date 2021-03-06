var vm = new Vue({
    el: '#searchTable',
    mixins: [mixins],
    data: function () {
        return {
            //查询数据
            searchFrom: {
                pagination:{
                    page:  1,
                    pages: 1,
                    perpage: 10
                },
                sort:{
                    field: 'modifyTime',
                    sort: 'desc'
                },
                keyword:'',
                //立项类型
                projType:'',
                //行政分区
                regionalism:'',
                //建设性质
                projNature:'',
                //项目级别
                projLevel:'',
                //最小建筑面积
                minBuildAreaSum:'',
                //最大建筑面积
                maxBuildAreaSum:'',
                //修改时间-开始
                modifyStartTime:'',
                //修改时间-结束
                modifyEndTime:'',
                onlyRegion:onlyRegion,
                onlyOrg:onlyOrg,
                handler:handler

            },
            // 是否为本地
            isLocal: false,

            // 页面所有待选项
            allDicItemListData: {
                XM_NATURE:[],
                XM_PROJECT_STEP:[],
                XM_PROJECT_LEVEL:[]
            },

            baseRules: {
                minBuildAreaSum: [{
                    required: false,
                    validator: checkNumber,
                    trigger: 'blur'
                }],
                maxBuildAreaSum: [{
                    required: false,
                    validator: checkNumber,
                    trigger: 'blur'
                }]
            },

            //列表选中
            projSelectList:[]
        }
    },
    components:{
        'v-sync-cascader':SyncCascader
    },
    methods: {
        //刷新列表
        fetchTableData: function () {
            var ts = this;

            this.searchFrom.pagination.pages = this.searchFrom.pagination.page;


            if(this.$refs.regionalismSyncCascader) {
                this.searchFrom.regionalism = this.$refs.regionalismSyncCascader.getSelectecValue();
            }

            ts.loading = true;

            request('', {
                url: ctx + 'aea/proj/info/conditionalQueryAeaProjInfo',
                type: 'get',
                data: ts.searchFrom
            }, function (res) {
                ts.loading = false;
                if (res.success) {
                    ts.tableData = res.content.list;
                    ts.dataTotal = res.content.total;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        // 跳转到详情页面
        jumpToDetail: function (id) {
            var _projInfoId = id;
            var parentIfreamUrl = window.frames.location.href;
            var _url = "",_tabName="";
            if (_projInfoId === 'add'){
                _tabName = "新增项目";
                _url = ctx + 'rest/project/detail?parentIfreamUrl='+parentIfreamUrl;
            }else{
                _tabName = "编辑项目"
                _url = ctx + 'rest/project/detail?projInfoId=' + _projInfoId +'&parentIfreamUrl='+parentIfreamUrl;
            }
            var _jumpData = {
                'menuName': _tabName,
                'menuInnerUrl': _url,
                'id': _projInfoId + '_pjdetail'
            };
            parent.vm.addTab('', _jumpData, parent.vm.activeTabIframe, '');
        },
        projPanorama:function(projInfoId){
            var url = ctx + 'rest/project/diagram/status/projInfo/?projInfoId='+projInfoId;
            var tempwindow = window.open();
            setTimeout(function () {
                tempwindow.location = url;
            }, 200);
        },
        dateFormatCn:function(row){
            if(row.modifyTime) {
                return formatDate(new Date(row.modifyTime), 'yyyy-MM-dd hh:mm');
            }else{
                return "-";
            }
        },
        //获取查询条件的数据
        conditionalQueryDic: function () {
            var ts = this;
            request('', {
                url: ctx + 'aea/proj/info/getConditionalQueryDic',
                type: 'get',
                data: {}
            }, function (res) {
                if (res.success) {
                    ts.allDicItemListData = res.content;
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        load: function(row, treeNode, resolve) {
            var ts = this;
            // ts.loading = true;
            request('', {
                url: ctx + 'aea/proj/info/listChildProjInfoByProjInfoId',
                type: 'get',
                data: {projInfoId:row.projInfoId}
            }, function (res) {
                // ts.loading = false;
                if (res.success) {
                    resolve(res.content);
                } else {
                    return ts.apiMessage(res.message, 'error')
                }
            }, function () {
                // ts.loading = false;
                return ts.apiMessage('网络错误！', 'error')
            });
        },
        //列表选中
        handleSelectionChange: function (list) {
            this.projSelectList = list;
        },
        // 批量删除
        batchDel: function () {
            var ts = this,
                _idList = [],
                _delData = {};
            if(!ts.projSelectList.length) return this.apiMessage('您尚未选中项目，请选择');
            confirmMsg('', '您确定删除选中项目吗？', function () {
                for (var i = 0; i < ts.projSelectList.length; i++) {
                    _idList.push(ts.projSelectList[i].projInfoId);
                }
                _delData.projInfoIds = _idList.join(',');
                ts.loading = true;
                request('', {
                    url: ctx + 'aea/proj/info/batchDeleteProjInfo',
                    type: 'get',
                    data: _delData
                }, function (res) {
                    ts.loading = false;
                    if (res.success) {
                        ts.apiMessage('删除成功！', 'success');
                        ts.fetchTableData();
                    } else {
                        ts.apiMessage(res.message, 'error')
                    }
                }, function (msg) {
                    ts.loading = false;
                    ts.apiMessage('删除失败！', 'error');
                });
            });
        },
    },
    created: function () {
        this.conditionalQueryDic();
        this.fetchTableData();
        if (document.location.protocol == "file:") {
            this.isLocal = true;
        }
    }
});