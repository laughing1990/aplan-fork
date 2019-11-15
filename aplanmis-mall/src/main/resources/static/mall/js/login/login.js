var login = (function () {
    var vm = new Vue({
        el:'#login',
        data:{
            loginBtnLoading:false,
            userName:'',
            password:'',
            verifyCode:'',
            errorMsg:'',
            errorMsgFlag:false,
            code:ctx + "rest/verifycode/getCode",
        },
        mounted:function() {
            var vm = this;
        },
        methods: {
            login:function(){
                var vm = this;
                if(vm.userName == ""){
                    vm.errorMsg = '请输入用户'
                    vm.$message.error(vm.errorMsg)
                    return false;
                }
                if(vm.password == ""){
                    vm.errorMsg = '请输入密码'
                    vm.$message.error(vm.errorMsg)
                    return false;
                }
                if(vm.verifyCode == ""){
                    vm.errorMsg = '请输入验证码'
                    vm.$message.error(vm.errorMsg)
                    return false;
                }
                request('', {
                    url: ctx + 'rest/mall/checkVerifyCode',
                    type: 'get',
                    data:{
                        verifyCode:vm.verifyCode
                    }
                }, function (res) {
                    if(res.success){
                        vm.loginBtnLoading = true;

                        request('', {
                            url: ctx + 'rest/mall/login',
                            type: 'get',
                            data:{
                                userName:vm.userName,
                                password:sm3(hex_md5(vm.password)),
                            }
                        }, function (res) {
                            vm.loginBtnLoading = false;
                            if(res.success){
                                localStorage.setItem('loginInfo', JSON.stringify(res.content))
                                location.href= ctx + "rest/main/toIndexPage?#MyHomeIndex";
                                return false;
                            }else{
                                vm.$message.error(res.message)
                            }

                        },function () {
                            console.log(123456)
                            vm.loginBtnLoading = false;
                            vm.$message.error('请求服务器接口报错！')
                        });
                    }else{
                        vm.$message.error(res.message)
                    }
                });
            },
            changeCode:function () {
                var vm = this;
                this.code = ctx + 'rest/verifycode/getCode?t='+new Date();
                debugger;
            }
        },
        watch:{

        }
    });
    return {
        vm:vm,
    }
})();
