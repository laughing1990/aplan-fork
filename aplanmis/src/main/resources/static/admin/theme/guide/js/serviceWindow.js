var vm = new Vue({
    el: '#index',
    data: {
        // 窗口
        page: 1,
        pageSize: 10,
        loading: true,
        searchKey: null,
        multipleSelection: '',
        selectedIds: [],
        tableData: {},
        // 服务
        windPage: 1,
        windPageSize: 10,
        windLoading: true,
        windSearchKey: null,
        windMultipleSelection: '',
        windSelectedIds: [],
        windTableData: {},
        dialogTbShow: false,
        curIsEditable: curIsEditable
    },
    mounted: function () {
        this.loadStageWindows();
    },
    methods:{
        loadStageWindows: function(props){

            var _that = this;
            _that.tableData = {};

            if(!curIsEditable){
                alertMsg('提示信息', '当前主题版本下数据不可编辑!', '关闭', 'error', true);
            }

            var prop = {
                'pageNum': this.page ? this.page : 1,
                'pageSize': this.pageSize ? this.pageSize : 10,
                'keyword': this.searchKey,
                'stageId': stageId
            };
            if(!props) {
                props = prop;
            }
            request('aplanmis',{
                url: ctx + 'aea/par/stage/listStageWindowByPageForEui.do',
                type: 'post',
                data: props
            },function (data) {

                _that.tableData = data;
                _that.loading = false;

            }, function(msg){

                alertMsg('','加载失败','关闭','error',true);
                _that.loading = false;
            });
        },
        searchRow: function(){
            var propSearch = {
                'pageNum': this.page ? this.page : 1,
                'pageSize': this.pageSize ? this.pageSize : 10,
                'keyword': this.searchKey,
                'stageId': stageId
            };
            this.loadStageWindows(propSearch)
        },
        stageWindClickSel: function(row, column, event){
            this.$refs.stageWindTable.toggleRowSelection(row);
        },
        handleSelChange: function (data) {

            this.multipleSelection = data;
            var ids = [];
            this.multipleSelection.map(function (item) {
                ids.push(item.windStageId)
            })
            this.selectedIds = ids;
            return this.selectedIds;
        },
        handleSizeChange: function(value) {

            this.pageSize = value;
            this.loadStageWindows();
        },
        handleCurrentChange: function(value) {

            this.page = value;
            this.loadStageWindows();
        },
        deleteData: function(data){

            if(!curIsEditable){
                alertMsg('提示信息', '当前主题版本下数据不可编辑!', '关闭', 'error', true);
                return;
            }
            var _that = this;
            if (data && data.windStageId) {
                var delData = {
                    'windStageId': data.windStageId,
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/par/stage/delAeaServiceWindowStageById.do',
                        type: 'post',
                        data: delData
                    },function () {

                        _that.$message({message: '删除成功', type: 'success'});
                        _that.loadStageWindows();

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }else{
                alertMsg('', '请选择一条记录!', '关闭', 'info', true);
            }
        },
        batchDelData: function(){

            if(!curIsEditable){
                alertMsg('提示信息', '当前主题版本下数据不可编辑!', '关闭', 'error', true);
                return;
            }
            var _that = this;
            var ids = _that.selectedIds;
            if ((ids == null || ids.length == 0 || ids == 'undefined')) {
                alertMsg('', '请选择数据!', '关闭', 'error', true);
            } else {
                var delData = {
                    'windStageIds': ids.toString()
                };
                confirmMsg('','确定要批量删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/par/stage/batchDelAeaServiceWindowStageByIds.do',
                        data: delData
                    },function () {

                        _that.$message({message: '批量删除成功', type: 'success'});
                        _that.loadStageWindows();

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }
        },
        openServiceWindow: function(){

            if(!curIsEditable){
                alertMsg('提示信息', '当前主题版本下数据不可编辑!', '关闭', 'error', true);
                return;
            }else{
                var _that = this;
                _that.dialogTbShow = true;
                _that.windLoading = true;
                _that.windSearchKey = '';
                _that.loadStageNoSelectWindows();
            }
        },
        loadStageNoSelectWindows: function(props){

            var _that = this;
            _that.windTableData = {};
            var prop = {
                'pageNum': this.windPage ? this.windPage : 1,
                'pageSize': this.windPageSize ? this.windPageSize : 10,
                'keyword': this.windSearchKey,
                'stageId': stageId
            };
            if(!props) {
                props = prop;
            }
            request('aplanmis',{
                url: ctx + 'aea/service/window/listStageUnselectedWindowByPageForEui.do',
                type: 'post',
                data: props
            },function (data) {

                _that.windTableData = data;
                _that.windLoading = false;

            }, function(msg){

                alertMsg('','加载失败','关闭','error',true);
                _that.windLoading = false;
            });
        },
        windSearchRow: function(){
            var propSearch = {
                'pageNum': this.windPage ? this.windPage : 1,
                'pageSize': this.windPageSize ? this.windPageSize : 10,
                'keyword': this.windSearchKey,
                'stageId': stageId
            };
            this.loadStageNoSelectWindows(propSearch)
        },
        windClickSel: function(row, column, event){

            this.$refs.windTable.toggleRowSelection(row);
        },
        windHandleSelChange: function (data) {

            this.windMultipleSelection = data;
            var ids = [];
            this.windMultipleSelection.map(function (item) {
                ids.push(item.windowId)
            })
            this.windSelectedIds = ids;
            return this.windSelectedIds;
        },
        windHandleSizeChange: function(value) {

            this.windPageSize = value;
            this.loadStageNoSelectWindows();
        },
        windHandleCurrentChange: function(value) {

            this.windPage = value;
            this.loadStageNoSelectWindows();
        },
        importStageWindows: function(){

            var _that = this;
            var ids = _that.windSelectedIds;
            if ((ids == null || ids.length == 0 || ids == 'undefined')) {
                alertMsg('', '请选择数据!', '关闭', 'error', true);
            } else {
                var saveData = {
                    "stageId": stageId,
                    "windowIds": ids.toString(),
                };
                request('aplanmis',{
                    url: ctx + 'aea/par/stage/batchSaveStageWindows.do',
                    data: saveData
                },function () {
                    _that.dialogTbShow = false;
                    _that.$message({message: '批量导入成功!', type: 'success'});
                    _that.loadStageWindows();
                }, function(){
                    alertMsg('', '批量导入失败!', '关闭', 'error', true);
                });
            }
        }
    }
});