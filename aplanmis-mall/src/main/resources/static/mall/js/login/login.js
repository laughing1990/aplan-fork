var login = (function () {
    var vm = new Vue({
        el:'#login',
        data:{
            loginBtnLoading:false,
            userName:'',
            password:'',
            errorMsg:'',
            errorMsgFlag:false,
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

            },
        },
        watch:{

        }
    });
    return {
        vm:vm,
    }
})();
