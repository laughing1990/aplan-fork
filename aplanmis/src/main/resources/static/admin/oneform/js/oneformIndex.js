var vm = new Vue({
    el: '#index',
    data: {
        formLabelWidth: '30%',
        pageSize: 10,
        page: 1,
        loading: true,
        searchKey: null,
        multipleSelection: '',
        selectedIds: [],
        selRowData: {
            oneformId: '',
            oneformName: '',
            oneformDesc: '',
            oneformUrl: '',
            sortNo: '',
            isActive: '',
        },
        dialogEditTable: false,
        dialogTitie: '',
        dialogTitieFlag: '',
        tableData: {},
        rules: {
            oneformName: [
                {required: true, message: '请输入表单名称', trigger: 'blur'}
            ],
            oneformDesc: [
                {required: true, message: '请输入表单描述', trigger: 'blur'}
            ],
            oneformUrl: [
                {required: true, message: '请输入表单加载地址', trigger: 'blur'}
            ],
            sortNo: [
                { required: true, message: '请输入排序', trigger: 'blur' },
                { type: 'number', message: '排序必须为数字值'}
            ],
            isActive: [
                {required: true, message: '请选择是否有效', trigger: 'change'}
            ],
        },
        initSortNo: 1,
    },
    mounted: function () {
        this.getDataTable();
        var that = this;
    },
    methods:{
        getDataTable: function(props){
            var _that = this;
            var prop = {
                pageNum: this.page ? this.page : 1,
                pageSize: this.pageSize,
                keyword: this.searchKey,
            };
            if(!props) {
                props = prop;
            }
            request('aplanmis',{
                url: ctx + 'aea/par/oneform/page.do',
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
        editRow: function(rowData, dialogTitie){

            var _that = this;
            _that.dialogTitieFlag = dialogTitie;
            dialogTitie ? this.dialogTitie = '编辑表单': this.dialogTitie = '新增表单';
            this.dialogEditTable = true;

            if(rowData){
                this.selRowData = {
                    oneformId: rowData.oneformId,
                    oneformName: rowData.oneformName,
                    oneformDesc: rowData.oneformDesc,
                    oneformUrl: rowData.oneformUrl,
                    sortNo: rowData.sortNo,
                    isActive: rowData.isActive,
                };
            }else {
                _that.initSortNo = 1;
                request('aplanmis',{
                    url: ctx + 'aea/par/getMaxSortNo.do',
                    type: 'post',
                    async: false,
                    data: {}
                },function (data) {
                    _that.initSortNo = data;
                }, function(msg){});
                this.selRowData = {
                    oneformId: '',
                    oneformName: '',
                    oneformDesc: '',
                    oneformUrl: '',
                    sortNo:  _that.initSortNo? _that.initSortNo:1,
                    isActive: '1',
                };
            }
        },
        handleSelChange: function (val) {

            this.multipleSelection = val;
            var ids = [];
            this.multipleSelection.map(function (item) {
                ids.push(item.oneformId)
            })
            this.selectedIds = ids;
            return this.selectedIds;
        },
        deleteData: function(data){

            var _that = this;
            if (data && data.oneformId) {
                var delData = {
                    'id': data.oneformId
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/par/deleteOneformById.do',
                        type: 'post',
                        data: delData
                    },function () {

                        _that.$message({message: '删除成功!', type: 'success'});
                        _that.getDataTable();

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }else{
                alertMsg('', '请选择一条记录!', '关闭', 'info', true);
            }
        },
        batchDelData: function(){

            var _that = this;
            var ids = _that.selectedIds;
            if ((ids == null || ids.length == 0 || ids == 'undefined')) {
                alertMsg('', '请选择数据!', '关闭', 'error', true);
            } else {
                var delData = {
                    'ids': ids.toString()
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/par/deleteMulOneformByIds.do',
                        data: delData
                    },function () {

                        _that.$message({message: '批量删除成功!', type: 'success'});
                        _that.getDataTable();

                    }, function(){

                        alertMsg('', '删除失败', '关闭', 'error', true);
                    });
                },'','确定','取消')
            }
        },
        handleSizeChange: function(val) {

            this.pageSize = val;
            this.getDataTable();
        },
        handleCurrentChange: function(val) {

            this.page = val;
            this.getDataTable();
        },
        searchRow: function(){

            var propSearch = {
                pageNum: this.page ? this.page : 1,
                pageSize: this.pageSize,
                keyword: this.searchKey,
            };
            this.getDataTable(propSearch)
        },
        dbclickRow: function(row, column, event){

            this.editRow(row, 1);
        },
        clickRow:function(row, column, event){

            this.$refs.oneformTb.toggleRowSelection(row);
        },
        clearFormData: function(){

            this.$refs['selRowData'].resetFields();
        },
        saveEdit: function(refsD, rowData){

            var _that = this;
            this.$refs[refsD].validate(function(valid) {
                if (valid) {
                    _that.submitEdit(rowData);
                } else {
                    alertMsg('','请输入完整信息','关闭','warning',true);
                    this.dialogEditTable = true;
                    return false;
                }
            });
        },
        submitEdit: function(data){

            var _that = this;
            request('aplanmis',{
                url: ctx + 'aea/par/saveParOneform.do',
                data:data
            },function () {
                _that.$message({message: '保存成功!', type: 'success'});
                _that.dialogEditTable = false;
                _that.getDataTable();
            }, function(){
                alertMsg('','加载失败','关闭','error',true);
            });
        },
        changeIsActive: function (dataSend, e) {
            
            e.cancelBubble = true;
            var _that = this;
            request('aplanmis', {
                url: ctx + 'aea/par/enOrDisableIsActive.do',
                type: 'get',
                data: {'id': dataSend.oneformId}
            }, function (data) {
                if (data.success) {
                    _that.$message({
                        message: '修改成功',
                        type: 'success'
                    });
                    _that.getDataTable();
                } else {
                    _that.$message({
                        message: data.message ? data.message : '修改失败，请稍后再试',
                        type: 'error'
                    });
                }
            }, function (msg) {
                _that.$message({
                    message: msg.message ? msg.message : '服务请求失败',
                    type: 'error'
                });
            });
        },
    }
});