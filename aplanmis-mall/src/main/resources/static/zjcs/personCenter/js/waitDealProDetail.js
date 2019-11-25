var vm = new Vue({
    el: '#app',
    data: function () {
        return {
            activeNames: ['1', '2', '3'],
            form: {},
            projScaleOptions: [{
                value: '投资额（元）',
                label: '投资额（元）'
            }, {
                value: '建筑面积（平方米）',
                label: '建筑面积（平方米）'
            }, {
                value: '资产总额（元）',
                label: '资产总额（元）'
            }, {
                value: '用地面积（平方米）',
                label: '用地面积（平方米）'
            }]
        }
    },
    mounted: function () {
        this.getDetail();
    },
    methods: {
        // 获取项目详情
        getDetail: function () {
            var url = 'supermarket/purchase/getPurchaseDetail';
            var projPurchaseId = this.getUrlParam('id');
            request('', {
                url: ctx + url,
                type: 'get',
                data: { projPurchaseId: projPurchaseId },
            }, function (result) {
                if (result.success) {

                }

            }, function (msg) { })
        },
        // 获取页面的URL参数
        getUrlParam: function (val) {
            var svalue = location.search.match(new RegExp("[\?\&]" + val + "=([^\&]*)(\&?)", "i"));
            return svalue ? svalue[1] : svalue;
        },
    }
});