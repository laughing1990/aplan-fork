var vm = new Vue({
    el: '#data-route',
    data: function () {
        return {
            applyProjTrans: {
                applyProjCount: 0,
                nonAcceptCount: 0,
                hasBlxxxxCount: 0,
                onlyA2Count: 0
            },
            projList:[]
        }
    },
    mounted: function () {
        var _that = this;
        _that.getApplyProjAllCount();
        _that.getApplyProjList();
    },
    methods: {
        getApplyProjAllCount: function () {
            var _that = this;
            request('', {
                url: ctx + 'data/route/apply_proj/all_count',
                type: 'get',
            }, function (result) {
                if (result.success) {
                    _that.applyProjTrans = result.content;
                }
            }, function (msg) {
            })
        },
        getApplyProjList: function () {
            var _that = this;
            request('', {
                url: ctx + 'data/route/aplanmis/apply_proj/list',
                type: 'get',
            }, function (result) {
                if (result.success) {
                    _that.projList = result.content;
                }
            }, function (msg) {
            })
        },
        getNonAcceptApplyProjList: function () {
            var _that = this;
            request('', {
                url: ctx + 'data/route/aplanmis/apply_proj/list/non_accept',
                type: 'get',
            }, function (result) {
                if (result.success) {
                    _that.projList = result.content;
                }
            }, function (msg) {
            })
        },
        getHasBlxxxxApplyProjList: function () {
            var _that = this;
            request('', {
                url: ctx + 'data/route/aplanmis/apply_proj/list/has_blxxxx',
                type: 'get',
            }, function (result) {
                if (result.success) {
                    _that.projList = result.content;
                }
            }, function (msg) {
            })
        },
        getOnlyA2ApplyProjList: function () {
            var _that = this;
            request('', {
                url: ctx + 'data/route/aplanmis/apply_proj/list/only_a2',
                type: 'get',
            }, function (result) {
                if (result.success) {
                    _that.projList = result.content;
                }
            }, function (msg) {
            })
        },
        formatterTableCell: function(row, column, cellValue, index){
            if(!cellValue)return '-';
            return cellValue;
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
    }
});