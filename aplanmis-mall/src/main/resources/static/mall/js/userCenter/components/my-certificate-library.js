var myCertificateLibs = (function () {

    var vm = new Vue({
        el:"#my-certificate-lib",
        data:{
            mloading:false,
            tableLoading:false,
            cCertificateList:[],
            uCertificateList:[],
            pCertificateList:[],
            c:'c',
            u:'u',
            p:'p',
            cPageNum:1,
            cPageSize:10,
            cTotal:0,
            uPageNum:1,
            uPageSize:10,
            uTotal:0,
            pPageNum:1,
            pPageSize:10,
            pTotal:0,
            keyword:'',
            addDirDialogFlag:false,
            addCertifiFormData: {},
            addDirFormRules:{
                certinstName:[
                    { required: true, message: '请填写证照名称', trigger:'blur'},
                ],
                certType:[
                    { required: true, message: '请选择证照类型', trigger:'blur'},
                ],
                certHolder:[
                    { required: true, message: '请选择证照所属', trigger:'blur'},
                ],
            },
            chooseCertHolder:[
                {
                    value: 'c',
                    label: '企业证照库'
                }, {
                    value: 'p',
                    label: '法人证照库'
                },
            ],
            chooseCertType:[
                {
                    value: 'c',
                    label: '企业证照库'
                }, {
                    value: 'p',
                    label: '法人证照库'
                },
            ]
        },
        created:function(){
            this.getCerList(this.c,this.cPageNum,this.cPageSize);
            this.getCerList(this.u,this.uPageNum,this.uPageSize);
            this.getCerList(this.p,this.pPageNum,this.pPageSize);
        },
        methods:{
            // 获取系统证照实例列表
            getCerList:function(certHolder,pageNum,pageSize){
                var ts = this;
                ts.tableLoading = true;
                request('', {
                    url: ctx + '/aea/cert/getCertintListByCertHolder',
                    type: 'get',
                    data:{
                        certHolder:certHolder,// 证照所属，c表示企业，u表示个人，p表示项目
                        pageNum:pageNum,
                        pageSize:pageSize,
                        keyword:ts.keyword,
                    }
                }, function (res) {
                    ts.tableLoading = false;
                    if (res) {
                        var content = res.content;
                        switch (certHolder) {
                            case 'c':
                                ts.cCertificateList = content.list;
                                ts.cTotal = content.total;
                                break;
                            case 'u':
                                ts.uCertificateList = content.list;
                                ts.uTotal = content.total;
                                break;
                            case 'p':
                                if(content.list){
                                    ts.pCertificateList = content.list;
                                }
                                ts.pTotal = content.total;
                                break;
                        }
                    }
                }, function () {
                    ts.tableLoading = false;
                });
            },
            // 新增
            sureAddcertifi:function(){
                var vm = this;
                vm.$refs['addDirForm'].validate(function(valid){
                    if (valid) {
                        var addCertifiFormData = vm.addCertifiFormData;
                        var parmas = {
                            isRoot:1,// 是否根文件夹 0否 1是
                            dirName:vm.addCertifiFormData.dirName,
                            dirCode:vm.addCertifiFormData.dirCode,
                        };
                        if(addCertifiFormData.chooseSuperDir){
                            parmas.isRoot = 0;
                            parmas.parentId = addCertifiFormData.chooseSuperDir
                        }
                        request('', {
                            url: ctx + '/aea/cert/saveCertint',
                            type: 'post',
                            data:parmas,
                        }, function (res) {
                            if(res.success){
                                vm.$message.success('新建成功');
                                vm.addCertifiFormData ={};
                                vm.getDirTree();
                            }else{
                                vm.$message.error('操作失败')
                            }
                        },function () {
                            vm.$message.error('请求接口错误，请稍后重试！');
                        });
                        vm.addDirDialogFlag = false
                    } else {
                        console.log('error submit!!');
                        //return false;
                    }
                });
            },

            // 查询
            searchcertificate:function(){
                this.retData();
                this.getCerList(this.c,this.cPageNum,this.cPageSize);
                this.getCerList(this.u,this.uPageNum,this.uPageSize);
                this.getCerList(this.p,this.pPageNum,this.pPageSize);
            },

            // 查看
            lookvViewLicenseURL:function(item){
                var _this = this;
                var authCode = item.authCode
                request('', {
                    url: ctx + '/aea/cert/getViewLicenseURL',
                    type: 'get',
                    data:{
                        authCode:authCode,// 证照编码
                    }
                }, function (res) {
                    if (res) {
                        if(res.content){
                            var tempwindow=window.open();
                            tempwindow.location = res.content
                        }else{
                            _this.$message.error("接口返回的查看地址为空")
                        }
                    }
                }, function () {

                });
            },
            ChandleSizeChange:function () {

            },
            ChandleCurrentChange:function () {

            },
            phandleSizeChange:function (val) {
                this.uPageSize = val;
                this.getCerList(this.u,this.uPageNum,this.uPageSize);
            },
            phandleCurrentChange:function (val) {
                this.uPageNum = val;
                this.getCerList(this.u,this.uPageNum,this.uPageSize);
            },
            retData:function(){
                this.cPageNum = 1
                this.cPageSize=10
                this.uPageNum = 1
                this.uPageSize=10
                this.pPageNum = 1
                this.pPageSize=10
            }

        },
        filters: {
            formatDate: function(time) {
                if(!time) return "";
                var date = new Date(time);
                return formatDate(date, 'yyyy-MM-dd');
            },
        },
    })
    return {
        vm: vm
    }
})();