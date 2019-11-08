var vm = new Vue({
    el: '#index',
    data: function () {
        return {
            unitTableData: [],
            globalUnitTableData: [],
            page: 1,
            pageSize: 10,
            page2: 1,
            pageSize2: 5,
            searchKey: null,
            searchKey2: null,
            loading: true,
            loading2: true,
            unitTypeOptions: null,
            IDTypeOptions: null,
            unitDialogState: false,
            globalUnitDialogState: false,
            dialogTitle: null,
            roles: [],
            currentGlobalUnitRow: null,
            multipleSelection: null,
            selectedIds: [],
            unitFormData: {
                creditUnitInfoId: null,
                loginName: null,
                loginPwd: null,
                applicant: '', //单位名
                idtype: '', // 单位证照类型
                unifiedSocialCreditCode: '', //统一社会信用代码
                organizationalCode: '',//组织机构代码
                induCommRegNum: '',//工商登记号
                taxpayerNum: '',//纳税人识别号
                creditFlagNum: '',//信用标记码
                isPrimaryUnit: '', //是否主单位 0-副单位   1-主单位
                isOwnerUnit: '', //角色-是否是业主
                isImUnit: '', //角色-是否是中介机构
                unitNature: '', //单位性质
                unitType: '', //单位类型
                registeredCapital: '', //注册资本
                registerAuthority: '', //注册登记机关
                postalCode: '', //邮政编码
                managementScope: '', //经营范围
                idrepresentative: '', // 法定代表人
                idno: '', //身份证号码
                contact: '', //联系人姓名
                idmobile: '', //联系电话
                email: '', //联系人电子邮箱
                applicantDetailSite: '', //具体地址
                parentId: null,
                unitInfoId: null,
                isSync: null,
                syncSource: null,
                syncTime: null,
                globalUnitInfoName: null
            },
            rules: {
                applicant: [{
                    required: true,
                    message: '请输入单位名',
                    trigger: 'blur'
                }], //单位名
                unifiedSocialCreditCode: [{
                    required: true,
                    message: '请输入统一社会信用代码',
                    trigger: 'blur'
                }],
                globalUnitInfoName:[{
                    required: true,
                    message: '请请选择单位',
                    trigger: 'blur'
                }],
                isPrimaryUnit: [{
                    required: true,
                    message: '请选择是否主单位',
                    trigger: 'change'
                }], //是否主单位 0-副单位   1-主单位
                unitNature: [{
                    required: true,
                    message: '请选择单位性质',
                    trigger: 'change'
                }], //单位性质
                unitType: [{
                    required: true,
                    message: '请选择单位类型',
                    trigger: 'change'
                }], //单位类型
                registeredCapital: [{
                    required: true,
                    message: '请输入注册资本',
                    trigger: 'blur'
                }], //注册资本
                registerAuthority: [{
                    required: true,
                    message: '请输入注册登记机关',
                    trigger: 'blur'
                }], //注册登记机关
                postalCode: [{
                    required: true,
                    message: '请输入邮政编码',
                    trigger: 'blur'
                }], //邮政编码
                managementScope: [{
                    required: true,
                    message: '请输入经营范围',
                    trigger: 'blur'
                }], //经营范围
                idrepresentative: [{
                    required: true,
                    message: '请输入法定代表人',
                    trigger: 'blur'
                }], // 法定代表人
                idno: [{
                    required: true,
                    message: '请输入身份证号码',
                    trigger: 'blur'
                }], //身份证号码
                contact: [{
                    required: true,
                    message: '请输入联系人姓名',
                    trigger: 'blur'
                }], //联系人姓名
                idmobile: [{
                    required: true,
                    message: '请输入联系电话',
                    trigger: 'blur'
                }], //联系电话
                applicantDetailSite: [{
                    required: true,
                    message: '请输入具体地址',
                    trigger: 'blur'
                }], //具体地址
            },
            fullHeight: document.documentElement.clientHeight,
            dialogSelUnit: false,
            selUnitTreeData: false,
            unitFilterText: '',
            selUnitRootNodeId: [],
            selUnitInfoList: [],

        }
    },
    mounted: mounted,
    methods: {
        getTableData: getTableData,
        getTableDataDef: getTableDataDef,
        clearAndReloadDataTable: clearAndReloadDataTable,
        reloadDataTable: reloadDataTable,
        handleSizeChange: handleSizeChange,
        handleCurrentChange: handleCurrentChange,
        handleSizeChange2: handleSizeChange2,
        handleCurrentChange2: handleCurrentChange2,
        getUnitType: getUnitType,
        getIDType: getIDType,
        addCreditUnit: addCreditUnit,
        editCreditUnit: editCreditUnit,
        saveCreditUnit: saveCreditUnit,
        delCreditUnit: delCreditUnit,
        roleChange: roleChange,
        dialogClose: dialogClose,
        initFormData: initFormData,
        showGlobalUnitTable: showGlobalUnitTable,
        globalUnitTableCurrentChange: globalUnitTableCurrentChange,
        clearGlobalUnitSelection: clearGlobalUnitSelection,
        getGlobalUnitTableData: getGlobalUnitTableData,
        clearAndReloadGlobalUnitDataTable: clearAndReloadGlobalUnitDataTable,
        reloadGlobalUnitDataTable: reloadGlobalUnitDataTable,
        saveGlobalUnitSelection: saveGlobalUnitSelection,
        handleSelChange: handleSelChange,
        batchDelData: batchDelData,
        load: loadChildren,
        selUnitInfo: function () {
            var _that = this;
            _that.dialogSelUnit = true;
            _that.selUnitTreeData = [];
            _that.selUnitInfoList = [];
            request('aplanmis', {
                url: ctx + 'aea/credit/redblack/gtreeUnitInfoForEui.do',
                type: 'get',
                data: {'keyword': _that.unitFilterText}
            }, function (data) {
                if (data.success) {
                    _that.selUnitTreeData = data.content;
                    if (data.content[0].id) {
                        _that.selUnitRootNodeId.push(data.content[0].id);
                    }
                    if (_that.unitFormData.unitInfoId&&_that.unitFormData.globalUnitInfoName) {
                        _that.selUnitInfoList = [];
                        _that.selUnitInfoList.push({
                            id: _that.unitFormData.unitInfoId,
                            label: _that.unitFormData.globalUnitInfoName
                        });
                    }
                }
            }, function () {
                _that.selUnitTreeData = [];
            });
        },
        clearUnitSearch: function () {

            var _that = this;
            _that.unitFilterText = '';
            _that.selUnitInfo();
        },
        clickSelUnit: function (data, node, event) {

            if (data && data.id != 'root') {
                var _that = this;
                _that.selUnitInfoList = [];
                _that.selUnitInfoList.push(data);
            } else {
                return;
            }
        },
        removeSelUnit: function () {
            var _that = this;
            _that.selUnitInfoList = [];
        },
        saveSelUnit: function () {
            var _that = this;
            if (_that.selUnitInfoList.length > 0) {
                _that.unitFormData.unitInfoId = _that.selUnitInfoList[0].id;
                _that.unitFormData.globalUnitInfoName = _that.selUnitInfoList[0].label;
            } else {
                _that.unitFormData.unitInfoId = '';
                _that.unitFormData.globalUnitInfoName = '';
            }
            _that.dialogSelUnit = false;
        },
        closeSelUnitInfo: function () {
            var _that = this;
            _that.dialogSelUnit = false;
            _that.unitFilterText = '';
        },
    },
    computed: {
        displaySync: function () {
            if (this.unitFormData.isSync == '0') {
                return 'none';
            } else {
                return 'block';
            }
        }
    }
});

function loadChildren(tree, treeNode, resolve) {
    this.getTableData(function (data) {
        resolve(data.list);
    }, {
        parentId: treeNode.rowKey,
        keyword: null
    })
}

function mounted() {
    var that = this;
    that.getUnitType();
    that.getIDType();
    that.getTableDataDef();
    window.addEventListener("resize", function () {
        return (function () {
            window.fullHeight = document.documentElement.clientHeight
            that.fullHeight = window.fullHeight;
        })();
    });
}

function getTableData(resolve, param) {
    var that = this;
    that.loading = true;
    var prop = {
        pageNum: that.page ? that.page : 1,
        pageSize: that.pageSize,
        keyword: that.searchKey
    };
    $.extend(prop, param);

    request('aplanmis', {
        url: ctx + 'aea/credit/unit/info/listAeaCreditUnitInfo.do',
        type: 'post',
        data: prop
    }, function (data) {
        if (data) {
            if (typeof resolve === 'function') {
                resolve.call(that, data);
            }
        }
        that.loading = false;
    }, function (msg) {
        alertMsg('', '加载失败', '关闭', 'error', true);
        that.loading = false;
    });
}

function getTableDataDef(param) {
    var that = this;
    var p = {};
    if (!that.searchKey) {
        p.parentId = 'root';
    }
    $.extend(p, param);
    that.getTableData(function (data) {
        that.unitTableData = data;
    }, p);
}

function reloadDataTable() {
    this.page = 1;
    this.getTableDataDef()
}

function clearAndReloadDataTable() {
    this.searchKey = null;
    this.getTableDataDef();
}

function handleSizeChange(val) {
    this.getTableDataDef();
}

function handleCurrentChange(val) {
    this.getTableDataDef();
}

function handleSizeChange2(val) {
    this.getGlobalUnitTableData();
}

function handleCurrentChange2(val) {
    this.getGlobalUnitTableData();
}

function getUnitType() {
    var that = this;
    request('', {
        url: ctx + 'bsc/dic/code/lgetItems.do',
        type: 'get',
        data: {
            typeCode: 'XM_DWLX'
        }
    }, function (res) {
        that.unitTypeOptions = res;
    }, function () {

    });
}

function getIDType() {

    var that = this;
    request('', {
        url: ctx + 'bsc/dic/code/lgetItems.do',
        type: 'get',
        data: {
            typeCode: 'IDTYPE'
        }
    }, function (res) {
        that.IDTypeOptions = res;
    }, function () {

    });
}

function addCreditUnit(row) {

    var that = this;
    that.dialogTitle = '新建单位';
    $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
    that.unitFormData.creditUnitInfoId = null;
    that.unitFormData.parentId = null;
    that.unitFormData.unitInfoId = null;
    that.unitFormData.isSync = '0';
    that.roles = [];
    if (row) {
        that.dialogTitle = '新建子单位';
        that.unitFormData.parentId = row.creditUnitInfoId;
        that.unitFormData.unitInfoId = row.unitInfoId;
        that.unitFormData.globalUnitInfoName = row.globalUnitInfoName;
    }
    that.unitDialogState = true;
}

function editCreditUnit(row) {

    var that = this;
    that.dialogTitle = '编辑单位';
    that.unitDialogState = true;
    $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
    request('', {
        url: ctx + 'aea/credit/unit/info/getAeaCreditUnitInfo.do',
        type: 'get',
        data: {
            id: row.creditUnitInfoId
        }
    }, function (res) {
        if (res && res.creditUnitInfoId) {
            that.initFormData(res);
        } else {
            alertMsg('', '获取失败', '关闭', 'error', true);
        }
    }, function () {
        alertMsg('', '获取失败', '关闭', 'error', true);
    });
}

function saveCreditUnit() {

    var that = this;
    this.$refs['manForm'].validate(function (valid) {
        if (!valid) {
            return;
        }
        request('', {
            url: ctx + 'aea/credit/unit/info/saveAeaCreditUnitInfo.do',
            type: 'post',
            data: that.unitFormData
        }, function (res) {
            if (res && res.success) {
                that.unitDialogState = false;
                alertMsg('', '保存成功', '关闭', 'success', true);
                that.getTableDataDef();
            } else {
                alertMsg('', '保存失败', '关闭', 'error', true);
            }
        }, function () {
            alertMsg('', '保存失败', '关闭', 'error', true);
        });
    })

}

function initFormData(data) {

    var that = this;
    for (var key in that.unitFormData) {
        that.unitFormData[key] = data[key];
    }
    that.roles = [];
    if (data.isImUnit == 1) {
        that.roles.push('isImUnit')
    }
    if (data.isOwnerUnit == 1) {
        that.roles.push('isOwnerUnit')
    }
}

function roleChange(list) {

    this.unitFormData.isImUnit = 0;
    this.unitFormData.isOwnerUnit = 0;
    if (!list.length) {
        return;
    }
    if (list.indexOf('isImUnit') !== -1) {
        this.unitFormData.isImUnit = 1;
    }
    if (list.indexOf('isOwnerUnit') !== -1) {
        this.unitFormData.isOwnerUnit = 1;
    }
}

function dialogClose() {
    this.$refs['manForm'].resetFields();
}

function showGlobalUnitTable() {

    this.page2 = 1;
    this.searchKey2 = null;
    this.globalUnitDialogState = true;
    this.globalUnitTableData = [];
    this.getGlobalUnitTableData();

}

function globalUnitTableCurrentChange(val) {
    this.currentGlobalUnitRow = val;
}

function clearGlobalUnitSelection() {
    this.$refs.globalUnitTable.setCurrentRow();
}

function getGlobalUnitTableData(param) {
    var that = this;
    that.loading2 = true;
    var prop = {
        pageNum: that.page2 ? that.page2 : 1,
        pageSize: that.pageSize2,
        keyword: that.searchKey2
    };
    $.extend(prop, param);
    request('', {
        url: ctx + 'rest/applicant/listApplicants',
        type: 'get',
        data: prop
    }, function (res) {
        if (res && res.success) {
            that.globalUnitTableData = res.content;
        }
        that.loading2 = false;
    }, function (msg) {
        alertMsg('', '加载失败', '关闭', 'error', true);
        that.loading2 = false;
    });
}

function clearAndReloadGlobalUnitDataTable() {
    this.searchKey2 = null;
    this.getGlobalUnitTableData();
}

function reloadGlobalUnitDataTable() {
    this.page2 = 1;
    this.getGlobalUnitTableData();
}

function saveGlobalUnitSelection() {
    if (this.currentGlobalUnitRow) {
        this.unitFormData.unitInfoId = this.currentGlobalUnitRow.unitInfoId;
        this.unitFormData.globalUnitInfoName = this.currentGlobalUnitRow.applicant;
        this.globalUnitDialogState = false;
    } else {
        alertMsg('', '请选择单位', '关闭', 'warning', true);
    }
}


function handleSelChange(val) {

    this.multipleSelection = val;
    var ids = [];
    this.multipleSelection.map(function (item) {
        ids.push(item.creditUnitInfoId)
    })
    this.selectedIds = ids;

}

function batchDelData() {
    var that = this;
    var ids = that.selectedIds;
    if (ids == null || ids.length == 0 || ids == 'undefined') {
        alertMsg('', '请选择数据!', '关闭', 'error', true);
    } else {
        var delData = {
            'ids': ids.toString()
        };
        confirmMsg('', '确定要删除选中的记录吗？', function () {
            request('aplanmis', {
                url: ctx + 'aea/credit/unit/info/batchDelAeaCreditUnitInfoByIds.do',
                data: delData
            }, function () {

                alertMsg('', '删除成功', '关闭', 'success', true);
                that.getTableDataDef();

            }, function () {

                alertMsg('', '删除失败', '关闭', 'error', true);
            });
        }, '', '确定', '取消')
    }
}

function delCreditUnit(data) {

    var _that = this;
    if (data && data.creditUnitInfoId) {
        var delData = {
            'id': data.creditUnitInfoId
        };
        confirmMsg('', '确定要删除选中的记录吗？', function () {
            request('aplanmis', {
                url: ctx + 'aea/credit/unit/info/batchDelAeaCreditUnitInfoById.do',
                type: 'post',
                data: delData
            }, function () {

                alertMsg('', '删除成功', '关闭', 'success', true);
                _that.getTableDataDef();

            }, function () {

                alertMsg('', '删除失败', '关闭', 'error', true);
            });
        }, '', '确定', '取消')
    } else {
        alertMsg('', '请选择一条记录!', '关闭', 'info', true);
    }
}

