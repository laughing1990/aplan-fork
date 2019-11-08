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
            questionId: '',
            stageId: '',
            question: '',
            answer: '',
            sortNo: ''
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
            sortNo: [
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
                alertMsg('提示信息', '当前主题版本下数据不可编辑!', '关闭', 'error', true);
            }

            var prop = {
                pageNum: this.page ? this.page : 1,
                pageSize: this.pageSize,
                keyword: this.searchKey,
                stageId: stageId
            };
            if(!props) {
                props = prop;
            }
            request('aplanmis',{
                url: ctx + 'aea/par/stage/questions/page.do',
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
                alertMsg('', '当前事项版本下数据不可编辑', '关闭', 'info', true);
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
                    questionId: rowData.questionId,
                    stageId: rowData.stageId,
                    question: rowData.question,
                    answer: rowData.answer,
                    sortNo: rowData.sortNo,
                };
            }else {
                _that.initSortNo = 1;
                request('aplanmis',{
                    url: ctx + 'aea/par/stage/questions/getMaxSortNo.do',
                    type: 'post',
                    data: {'stageId': stageId},
                    async: false,
                },function (data) {
                    _that.initSortNo = data;
                }, function(msg){});
                this.selRowData = {
                    questionId: '',
                    stageId: stageId,
                    question: '',
                    answer: '',
                    sortNo:  _that.initSortNo? _that.initSortNo:1,
                };
            }
        },
        handleSelChange: function (val) {

            this.multipleSelection = val;
            var ids = [];
            this.multipleSelection.map(function (item) {
                ids.push(item.questionId)
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
            if (data && data.questionId) {
                var delData = {
                    'id': data.questionId
                };
                confirmMsg('','确定要删除选中的记录吗？',function(){
                    request('aplanmis',{
                        url: ctx + 'aea/par/stage/questions/delQuestAnswerById.do',
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
                        url: ctx + 'aea/par/stage/questions/batchDelQuestAnswerByIds.do',
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
                stageId: stageId
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
                url: ctx + 'aea/par/stage/questions/saveOrUpdateQuestAnswer.do',
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