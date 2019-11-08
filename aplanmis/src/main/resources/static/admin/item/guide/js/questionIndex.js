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
            id: '',
            itemVerId: '',
            question: '',
            answer: '',
            ordernum: ''
        },
        dialogEditTable: false,
        dialogTitie: '',
        dialogTitieFlag: '',
        tableData: {},
        rules: {
            question: [
                {required: true, message: '请输入问题', trigger: 'blur'}
            ],
            answer: [
                {required: true, message: '请输入答案', trigger: 'blur'}
            ],
            ordernum: [
                { required: true, message: '请输入排序', trigger: 'blur' },
                { type: 'number', message: '排序必须为数字值'}
            ],
        },
        initSortNo: 1,
        isShow: true,
        curIsEditable: curIsEditable
    },
    mounted: function () {

        this.getDataTable();
    },
    methods:{
        getDataTable: function(props){

            var _that = this;
            _that.tableData = {};

            if(!curIsEditable){
                alertMsg('提示信息', '当前事项版本下数据不可编辑!', '关闭', 'error', true);
            }

            var prop = {
                pageNum: this.page ? this.page : 1,
                pageSize: this.pageSize,
                keyword: this.searchKey,
                itemVerId: itemVerId
            };
            if(!props) {
                props = prop;
            }
            request('aplanmis',{
                url: ctx + 'aea/item/guide/questions/page.do',
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
            dialogTitie ? (curIsEditable?this.dialogTitie = '编辑问题答案':this.dialogTitie = '查看问题答案'): this.dialogTitie = '新增问题答案';
            $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
            if(!curIsEditable&&!dialogTitie){
                _that.dialogEditTable = false;
                _that.isShow = false;
                alertMsg('提示信息', '当前事项版本下数据不可编辑!', '关闭', 'error', true);
            }else{
                _that.dialogEditTable = true;
                if(curIsEditable){
                    _that.isShow = true;
                }else{
                    _that.isShow = false;
                }
            }
            if(rowData){
                this.selRowData = {
                    id: rowData.id,
                    itemVerId: rowData.itemVerId,
                    question: rowData.question,
                    answer: rowData.answer,
                    ordernum: rowData.ordernum
                };
            }else {

                _that.initSortNo = 1;
                request('aplanmis',{
                    url: ctx + 'aea/item/guide/getQueMaxSortNoByItemVerId.do',
                    type: 'post',
                    data: {'itemVerId': itemVerId},
                    async: false,
                },function (data) {
                    _that.initSortNo = data;
                }, function(msg){});
                this.selRowData = {
                    id: '',
                    itemVerId: itemVerId,
                    question: '',
                    answer: '',
                    ordernum:  _that.initSortNo? _that.initSortNo:1,
                };
            }
        },
        handleSelChange: function (val) {

            this.multipleSelection = val;
            var ids = [];
            this.multipleSelection.map(function (item) {
                ids.push(item.id)
            })
            this.selectedIds = ids;
            return this.selectedIds;
        },
        deleteData: function(data){

            if(!curIsEditable){
                alertMsg('提示信息', '当前事项版本下数据不可编辑!', '关闭', 'error', true);
                return;
            }

            var _that = this;
            if (data && data.id) {
                var delData = {
                    'id': data.id
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/item/guide/delQuestAnswerById.do',
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

            if(!curIsEditable){
                alertMsg('提示信息', '当前事项版本下数据不可编辑!', '关闭', 'error', true);
                return;
            }
            var _that = this;
            var ids = _that.selectedIds;
            if ((ids == null || ids.length == 0 || ids == 'undefined')) {
                alertMsg('', '请选择数据!', '关闭', 'error', true);
            } else {
                var delData = {
                    'ids': ids.toString()
                };
                confirmMsg('','确定要批量删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/item/guide/batchDelQuestAnswerByIds.do',
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
                itemVerId: itemVerId
            };
            this.getDataTable(propSearch)
        },
        dbclickRow: function(row){

            this.editRow(row, 1);
        },
        clickRow: function(row, column, event){

            this.$refs.questionTb.toggleRowSelection(row);
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
                url: ctx + 'aea/item/guide/saveOrUpdateQuestAnswer.do',
                data:data
            },function () {
                _that.$message({message: '保存成功', type: 'success'});
                _that.dialogEditTable = false;
                _that.getDataTable();
            }, function(){
                alertMsg('','加载失败','关闭','error',true);
            });
        }
    }
});