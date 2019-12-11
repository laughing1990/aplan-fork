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
            projList: [],
            applyProjList: [],
            nonAcceptList: [],
            onlyA2CountList: []
        }
    },
    mounted: function () {
        var _that = this;
        _that.getApplyProjCount();
        _that.getNonAcceptApplyProjCount();
        _that.getOnlyA2ApplyProjCount();
        _that.getApplyProjList();
        _that.getHasBlxxxxApplyProjCount();
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
        getApplyProjCount: function () {
            var _that = this;
            request('', {
                url: ctx + 'data/route/aplanmis/apply_proj/count',
                type: 'get',
            }, function (result) {
                if (result.success) {
                    _that.applyProjTrans.applyProjCount = result.content;
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
                    _that.applyProjList = result.content;
                }
            }, function (msg) {
            })
        },
        getNonAcceptApplyProjCount: function () {
            var _that = this;
            request('', {
                url: ctx + 'data/route/aplanmis/apply_proj/count/non_accept',
                type: 'get',
            }, function (result) {
                if (result.success) {
                    _that.applyProjTrans.nonAcceptCount = result.content;
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
                    _that.nonAcceptList = result.content;
                }
            }, function (msg) {
            })
        },
        getHasBlxxxxApplyProjCount: function () {
            var _that = this;
            request('', {
                url: ctx + 'data/route/aplanmis/apply_proj/count/has_blxxxx',
                type: 'get',
            }, function (result) {
                if (result.success) {
                    _that.applyProjTrans.hasBlxxxxCount = result.content;
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
        getOnlyA2ApplyProjCount: function () {
            var _that = this;
            request('', {
                url: ctx + 'data/route/aplanmis/apply_proj/count/only_a2',
                type: 'get',
            }, function (result) {
                if (result.success) {
                    _that.applyProjTrans.onlyA2Count = result.content;
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
                    _that.onlyA2CountList = result.content;
                }
            }, function (msg) {
            })
        },
        getJgProjList: function () {
            var _that = this;
            var jgProjList = JSON.parse(JSON.stringify(this.applyProjList));
            for(var i=0;i<_that.nonAcceptList.length;i++){
                jgProjList.splice(jgProjList.indexOf(_that.nonAcceptList[i]),1);
            }
            for(var i=0;i<_that.onlyA2CountList.length;i++){
                jgProjList.splice(jgProjList.indexOf(_that.onlyA2CountList[i]),1);
            }
            _that.projList = jgProjList;
        }
    }
});