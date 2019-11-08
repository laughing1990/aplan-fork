var vm = new Vue({
    el: '#index',
    data: {
        pageSize: 10,
        page: 1,
        loading: true,
        searchKey: null,
        tableData: {},
        fullHeight: document.documentElement.clientHeight,
        sampleTypeList: [],
        dialogEditTable: false,
        dialogTitie: '',
        multipleSelection: '',
        selectedIds: [],
        selRowData: {
            themeSampleId: '',
            sampleName: '',
            sampleDesc: '',
            sampleType: '',
            sampleContent: ''
        },
        rules: {
            sampleType: [
                {required: true, message: '请选择样本类型', trigger: 'change'}
            ],
            sampleName: [
                {required: true, message: '请填写样本名称', trigger: 'blur'}
            ],
            sampleContent: [
                {required: true, message: '请填写样本模板', trigger: 'blur'}
            ]
        }
    },
    mounted: mounted,
    methods: {
        init: init,
        parseSampleType: parseSampleType,
        getDataTable: getDataTable,
        handleSizeChange: handleSizeChange,
        handleCurrentChange: handleCurrentChange,
        reloadDataTable: reloadDataTable,
        clearFormData: clearFormData,
        addThemeSample: addThemeSample,
        editThemeSample: editThemeSample,
        saveThemeSample: saveThemeSample,
        delThemeSample: delThemeSample,
        handleSelChange: handleSelChange,
        batchDelData: batchDelData,
        clearAndReloadDataTable:clearAndReloadDataTable
    }
});

function mounted() {

    var that = this;
    that.init();
    window.addEventListener("resize", function () {
        return (function () {
            that.fullHeight = document.documentElement.clientHeight;
        })();
    });
}

function init() {

    var that = this;
    request('aplanmis', {
        url: ctx + 'aea/par/themeSample/listSampleType.do',
        type: 'get'
    }, function (data) {
        if (data) {
            that.sampleTypeList = data;
        }

        that.getDataTable();
    }, function (msg) {

    });
}

function delThemeSample(data) {

    var _that = this;
    if (data && data.themeSampleId) {
        var delData = {
            'id': data.themeSampleId
        };
        confirmMsg('', '确定要删除选中的记录吗？', function () {
            request('aplanmis', {
                url: ctx + 'aea/par/themeSample/delAeaParThemeSampleById.do',
                type: 'post',
                data: delData
            }, function () {

                _that.$message({message: '删除成功!', type: 'success'});
                _that.getDataTable();

            }, function () {

                alertMsg('', '删除失败', '关闭', 'error', true);
            });
        }, '', '确定', '取消')
    } else {
        alertMsg('', '请选择一条记录!', '关闭', 'info', true);
    }
}

function batchDelData() {

    var _that = this;
    var ids = _that.selectedIds;
    if (ids == null || ids.length == 0 || ids == 'undefined') {
        alertMsg('', '请选择数据!', '关闭', 'error', true);
    } else {
        var delData = {
            'ids': ids.toString()
        };
        confirmMsg('', '确定要删除选中的记录吗？', function () {
            request('aplanmis', {
                url: ctx + 'aea/par/themeSample/batchDelThemeSampleByIds.do',
                data: delData
            }, function () {

                _that.$message({message: '批量删除成功!', type: 'success'});
                _that.getDataTable();

            }, function () {

                alertMsg('', '删除失败', '关闭', 'error', true);
            });
        }, '', '确定', '取消')
    }
}

function handleSelChange(val) {

    this.multipleSelection = val;
    var ids = [];
    this.multipleSelection.map(function (item) {
        ids.push(item.themeSampleId)
    })
    this.selectedIds = ids;
    return this.selectedIds;
}

function saveThemeSample() {

    var _that = this;
    this.$refs['selRowData'].validate(function (valid) {
        if (valid) {
            request('aplanmis', {
                url: ctx + 'aea/par/themeSample/saveOrUpdateAeaParThemeSample.do',
                data: _that.selRowData
            }, function (res) {
                if (res.success) {
                    _that.$message({message: '保存成功', type: 'success'});
                    _that.dialogEditTable = false;
                    _that.getDataTable();
                } else {
                    alertMsg('', '操作失败', '关闭', 'error', true);
                }

            }, function () {
                alertMsg('', '操作失败', '关闭', 'error', true);
            });
        } else {
            alertMsg('', '请输入完整信息', '关闭', 'warning', true);
            _that.dialogEditTable = true;
            return false;
        }
    });
}

function editThemeSample(data) {

    var that = this;
    that.dialogTitie = '编辑主题样本';
    that.dialogEditTable = true;
    request('aplanmis', {
        url: ctx + 'aea/par/themeSample/getAeaParThemeSampleById.do',
        type: 'post',
        data: {id: data.themeSampleId}
    }, function (res) {
        if (res.success) {

            for (var key in that.selRowData) {
                that.selRowData[key] = res.content[key];
            }

        }
    }, function (msg) {
        alertMsg('', '加载失败', '关闭', 'error', true);
    });
}

function addThemeSample() {

    var that = this;
    that.dialogTitie = '新建主题样本';
    that.dialogEditTable = true;
    that.selRowData.themeSampleId = null;
}

function clearFormData() {

    this.$refs['selRowData'].resetFields();
}

function getDataTable() {

    var _that = this;
    var prop = {
        pageNum: _that.page ? _that.page : 1,
        pageSize: _that.pageSize,
        keyword: _that.searchKey
    };
    request('aplanmis', {
        url: ctx + 'aea/par/themeSample/listAeaParThemeSamplePage.do',
        type: 'post',
        data: prop
    }, function (data) {

        _that.tableData = data;
        _that.loading = false;

    }, function (msg) {

        alertMsg('', '加载失败', '关闭', 'error', true);
        _that.loading = false;
    });
}

function parseSampleType(sampleType) {

    for (var i = 0; i < this.sampleTypeList.length; i++) {
        if (this.sampleTypeList[i].itemCode == sampleType) {
            return this.sampleTypeList[i].itemName;
        }
    }
    return "-";
}

function handleSizeChange(val) {

    //this.pageSize = val;
    this.getDataTable();
}

function handleCurrentChange(val) {

    //this.page = val;
    this.getDataTable();
}

function reloadDataTable() {

    this.page = 1;
    this.getDataTable()
}

function clearAndReloadDataTable() {

    this.searchKey = null;
    this.reloadDataTable();
}