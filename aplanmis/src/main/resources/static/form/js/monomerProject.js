(function () {
    var vm = new Vue({
        el: '#monomerProject',
        data: function () {
            return {
                activeNames: ['1'],
                tableData: [
                    {
                        name: 'qe',
                        data: '123',
                        address: '312'
                    }
                ],
                formTitle: '新增单体工程',
                formData: {},
                isShowDialog: true,
                rules: {
                    name: { required: true, message: "xxx不得为空！", trigger: ["change"] },
                },
            }
        },
        created: function () {
            // this.projInfoId = this.getUrlParam('projInfoId');
            this.projInfoId = '012c4996-b14f-42c4-8d87-b2f347b27276';
        },
        mounted: function () {

        },
        methods: {
            // 重置弹窗表单数据
            resetForm: function () {
                this.$refs.form.resetFields();
            },
            // 关闭弹窗前回调
            handleClose: function(done) {
                this.resetForm();
                done();
            },
            // ,新增数据
            addData: function () {
                this.isShowDialog = true;
            },
            // 编辑数据
            editData: function (item) {
                this.isShowDialog = true;
            },
            // 删除数据
            delData: function (item) {

            },
            // from数据提交
            submitForm: function () {
                var _that = this;

                _that.$refs['form'].validate(function (valid) {
                    if (valid) {
                        _that.resetForm();
                        _that.isShowDialog = false;
                    }
                });
            },
            save: function () {

            },
            // 获取页面的URL参数
            getUrlParam: function (val) {
                var svalue = location.search.match(new RegExp("[\?\&]" + val + "=([^\&]*)(\&?)", "i"));
                return svalue ? svalue[1] : svalue;
            },
        }
    })
})()
