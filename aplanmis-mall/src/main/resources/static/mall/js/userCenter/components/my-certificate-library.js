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
            cPageSize:5,
            cTotal:0,
            uPageNum:1,
            uPageSize:5,
            uTotal:0,
            pPageNum:1,
            pPageSize:5,
            pTotal:0,
            keyword:'',
            addDirDialogFlag:false,
            addCertifiTitle:'新增',
            addCertifiFormData: {},
            addDirFormRules:{
                certinstName:[
                    { required: true, message: '请填写证照名称', trigger:'blur'},
                ],
                certType:[
                    { required: true, message: '请选择证照类型', trigger:'change'},
                ],
                certHolder:[
                    { required: true, message: '请选择证照所属', trigger:'change'},
                ],
                certinstCode:[
                    { required: true, message: '请填写证照编号', trigger:'blur'},
                ],
                certId:[
                    { required: true, message: '请选择证照', trigger:'change'},
                ]
            },
            chooseCertHolder:[
                {
                    value: 'c',
                    label: '企业证照库'
                }, {
                    value: 'u',
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
            ],
            chooseCert:[],
            certTypeId:'',
            certHolder:'',
            ctx:ctx,
            dialogImageUrl: '',
            dialogVisible: false,
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
                                if(content){
                                    ts.cCertificateList = content.list;
                                    ts.cTotal = content.total;
                                }
                                break;
                            case 'u':
                                if(content){
                                    ts.uCertificateList = content.list;
                                    ts.uTotal = content.total;
                                }
                                break;
                            case 'p':
                                if(content){
                                    ts.pCertificateList = content.list;
                                    ts.pTotal = content.total;
                                }
                                break;
                        }
                    }
                }, function () {
                    ts.tableLoading = false;
                });
            },

            // 获取系统证照类型列表
            getCertTypes:function(certHolder,pageNum,pageSize){
                var ts = this;
                request('', {
                    url: ctx + '/aea/cert/getCertTypes',
                    type: 'get',
                }, function (res) {
                    if (res) {
                        var content = res;
                        ts.chooseCertType = content;
                    }
                }, function () {
                    ts.tableLoading = false;
                });
            },

            certTypeHandleChange:function(val){
                var vm = this;
                if(val){
                    this.certTypeId = val
                    this.getCertListByType(this.certTypeId,this.certHolder)
                    this.$set(this.addCertifiFormData,'certId','');
                    setTimeout(function () {
                        vm.$refs.addDirForm.clearValidate();
                    },50)
                }
            },
            certHolderHangChange:function(val){
                var vm = this;
                this.certHolder = val
                if(this.certTypeId){
                    this.getCertListByType(this.certTypeId,this.certHolder)
                };
                this.$set(this.addCertifiFormData,'certId','');
                setTimeout(function () {
                    vm.$refs.addDirForm.clearValidate();
                },50)

            },

            // 获取系统证照定义列表
            getCertListByType:function(certTypeId,certHolder){
                var ts = this;
                request('', {
                    url: ctx + '/aea/cert/getCertListByType',
                    type: 'get',
                    data:{
                        certTypeId:certTypeId,
                        certHolder:certHolder
                    }
                }, function (res) {
                    if (res) {
                        var content = res;
                        ts.chooseCert = content;
                    }
                }, function () {

                });
            },
            // 新增
            sureAddcertifi:function(){
                var vm = this;
                vm.$refs['addDirForm'].validate(function(valid){
                    if (valid) {
                        var addCertifiFormData = vm.addCertifiFormData;
                        console.log(addCertifiFormData)
                        request('', {
                            url: ctx + '/aea/cert/saveCertint',
                            type: 'post',
                            //contentType: "application/json",
                            data:addCertifiFormData,
                        }, function (res) {
                            if(res.success){
                                vm.$message.success('新建成功');
                                vm.addCertifiFormData ={};
                                vm.getCerList(vm.c,vm.cPageNum,vm.cPageSize);
                                vm.getCerList(vm.u,vm.uPageNum,vm.uPageSize);
                                vm.getCerList(vm.p,vm.pPageNum,vm.pPageSize);
                            }else{
                                vm.$message.error('操作失败,失败原因：'+res.message)
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

            // 编辑
            editCertifi:function(item){
                console.log(item);
                var ts = this;
                ts.addCertifiTitle = "编辑"
                ts.addDirDialogFlag = true;
                ts.addCertifiFormData = item;
            },
            // 删除
            delCertifi:function(item){
                var _this = this;
                var certinstId = item.certinstId;
                confirmMsg('', '确定要删除吗？', function() {
                    request('bpmAdminUrl', {
                        type: 'post',
                        url: ctx + 'aea/cert/deleteCertint/'+ certinstId,
                    }, function(res) {
                        if(res.success){
                            _this.$message.success('删除成功');
                            _this.getCerList(_this.c,_this.cPageNum,_this.cPageSize);
                            _this.getCerList(_this.u,_this.uPageNum,_this.uPageSize);
                            _this.getCerList(_this.p,_this.pPageNum,_this.pPageSize);
                        }else{
                            _this.$message.error('删除失败')
                        }
                    }, function(err) {
                        _this.$message.error('请求接口出错了哦!');
                    })
                },function(){
                    console.log("取消");
                }, '确定', '取消')
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
            // 上传附件
            handleRemove:function(file, fileList) {
                console.log(file, fileList);
            },
            handlePictureCardPreview:function(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            handleSuccess:function(response, file, fileList){
                console.log(response)
                this.addCertifiFormData.attLinkId = response.content; // 附件关联ID
            },
            handleError:function(err, file, fileList){
                this.$message.error("上传文件失败，原因"+err);
            },


            ChandleSizeChange:function () {
                this.cPageSize = val;
                this.getCerList(this.c,this.cPageNum,this.cPageSize);
            },
            ChandleCurrentChange:function () {
                this.cPageNum = val;
                this.getCerList(this.c,this.cPageNum,this.cPageSize);
            },
            UhandleSizeChange:function (val) {
                this.uPageSize = val;
                this.getCerList(this.u,this.uPageNum,this.uPageSize);
            },
            UhandleCurrentChange:function (val) {
                this.uPageNum = val;
                this.getCerList(this.u,this.uPageNum,this.uPageSize);
            },
            PhandleSizeChange:function (val) {
                this.pPageSize = val;
                this.getCerList(this.p,this.pPageNum,this.pPageSize);
            },
            PhandleCurrentChange:function (val) {
                this.pPageNum = val;
                this.getCerList(this.p,this.pPageNum,this.pPageSize);
            },
            retData:function(){
                this.cPageNum = 1
                this.cPageSize=5
                this.uPageNum = 1
                this.uPageSize=5
                this.pPageNum = 1
                this.pPageSize=5
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