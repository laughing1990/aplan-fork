var vm = new Vue({
    el: '#app',
    data: function () {
        return {
            loading: false,
            activeNames: ['1', '2', '3', '4'],
            form: {},
        }
    },
    mounted: function () {
        this.getDetail();
    },
    methods: {
        // 获取项目详情
        getDetail: function () {
            var vm = this;
            var url = 'supermarket/register/getRegisterDetail';
            var unitInfoId = this.getUrlParam('unitInfoId');
            request('', {
                url: ctx + url,
                type: 'get',
                data: { unitInfoId: unitInfoId },
            }, function (res) {
                if (res.success) {
                    vm.form = res.content;
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