/*
 * @Author:
 * @Date:
 * @Last Modified by:
 * @Last Modified time: $ $
 */
var vm = new Vue({
    el: '#approve',
    data: function () {
        var checkMissValue = function (rule, value, callback) {
            if (value === '' || value === undefined || value === null) {
                callback(new Error('该输入项为必输项！'));
            } else if (value.toString().trim() === '') {
                callback(new Error('该输入项为必输项！'));
            } else {
                callback();
            }
        };
        var checkPhoneNum = function (rule, value, callback) {
            if (value === '' || value === undefined || value.trim() === '') {
                callback(new Error('该输入项为必输项！'));
            } else if (value) {
                var flag = !/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/.test(value) && !(/^1(3|4|5|6|7|8|9)\d{9}$/.test(value));
                if (flag) {
                    return callback(new Error('格式不正确'));
                } else {
                    callback();
                }

            } else {
                callback();
            }
        };
        return {
            querForm: {
                keyword: ''
            },
            total: 0,
            currentPage: 1,
            pageSize: 10,
            tableData: [],
            multipleSelection: [],
            isShow: true
        }
    },
    mounted: function () {
        var _this = this;
        _this.fetchData();

    },
    watch: {},
    methods: {
        //按关键字查询
        doSearch: function () {
            this.fetchData();
        },
        //进入补正待却认页面
        confirmMatCorrect: function (idx, row) {
            var parentFreamUrl = window.frames.location;
            var urltemp = ctx + '/rest/correct/matConfirm.html?correctId=' + row.correctId + '&parentFreamUrl=' + parentFreamUrl;
            var data = {
                'menuName': "材料补正",
                'menuInnerUrl': urltemp,
                'id': row.correctId
            }
            parent.vm.addTab('', data, '', '');
            return;
        },
        //填充列表数据
        fetchData: function () {
            var _this = this;
            request('', {
                url: ctx + 'rest/matCorrectConfirm/search',
                type: 'post',
                data: {'keyword': _this.querForm.keyword, 'pageNum': _this.currentPage, 'pageSize': _this.pageSize}
            }, function (result) {
                if (result.success) {
                    _this.total = result.content.total;
                    _this.currentPage = result.content.pageNum;
                    _this.pageSize = result.content.pageSize;
                    _this.tableData = result.content.list;
                    if (result.content.total > 10) {
                        _this.isShow = false;
                    }
                }

            }, function (msg) {
                alertMsg('', "服务请求失败！", '关闭', 'error', true);
            });
        },
        toggleSelection(rows) {
            if (rows) {
                rows.forEach(function (row) {
                    this.$refs.multipleTable.toggleRowSelection(row);
                });
            } else {
                this.$refs.multipleTable.clearSelection();
            }
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleSizeChange: function (val) {
            this.pageSize = val;
            this.currentPage = 1;
            this.fetchData(1, val);
            // console.log(`每页 ${val} 条`);
        },
        handleCurrentChange: function (val) {
            this.currentPage = val;
            this.fetchData(val, this.pageSize);
            // console.log(`当前页: ${val}`);
        },
        // 重置表单
        resetForm: function (formName) {
            this.$refs[formName].resetFields();
            this.selectMat = '';
        },
        // 刷新页面
        reloadPage: function () {
            if (this.submitCommentsType == 0 || this.submitCommentsType == 3) {
                window.location.reload()
            }
        },
    },
});

