var vm = new Vue({
    el:'#approve',
    data:function () {
        var checkNumFloat = function (rule, value, callback) {
            if (value) {
                var flag = !/^[+]{0,1}(\d+)$|^[+]{0,1}(\d+\.\d+)$/.test(value);
                if (flag) {
                    return callback(new Error('格式错误'));
                } else {
                    callback();
                }

            } else {
                callback();
            }
        };
        var checkPhoneNum = function (rule, value, callback) {
            if (value === '' || value === undefined || value.trim() === '') {
                callback(new Error('必填字段！'));
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
        var checkMissValue = function (rule, value, callback) {
            if (value === '' || value ===undefined || value ===null) {
                callback(new Error('该输入项为必输项！'));
            }else if(value.toString().trim() === ''){
                callback(new Error('该输入项为必输项！'));
            } else {
                callback();
            }
        };
        return{
            loading: false, // 页面加载遮罩
            projInfoId:'',
            showBasicButton:'true',
            addEditManModalTitle: '新增联系人',
            addEditManModalFlag: 'edit',
            addEditManModalShow: false, // 是否显示新增编辑联系人窗口
            gcunitLinkManOptions: [],
            sgunitLinkManOptions: [],
            kcunitLinkManOptions: [],
            sjunitLinkManOptions: [],
            jlunitLinkManOptions: [],
            addEditManform: {
                unitInfoId:'',
            },  // 新增编辑联系人信息
            addEditManPerform: {},  // 新增编辑联系人信息
            projInfoId: '', // 查询项目id
            addLinkManRules: { // 新增编辑联系人校验
                linkmanName: [
                    { required: true,validator: checkMissValue, trigger: 'blur' },
                ],
                linkmanCertNo: [
                    { required: true,validator: checkMissValue, trigger: 'blur' },
                ],
                linkmanMobilePhone:[
                    { required: true,validator: checkPhoneNum, trigger: 'blur' },
                ]
            },
            activeNames: ['1', '2', '3', '4', '5', '6'],// el-collapse 默认展开列表
            certBuildFromRules: {  // 基本信息校验
                constructionUnit: [
                    { required: true,message: '请输入建设单位！', trigger: ['change','blur'] },
                ],
                contractStartBuildTime: [
                    { required: true,message: '请选择合同开工时间！', trigger: ['change'] },
                ],
                contractEndBuildTime: [
                    { required: true,message: '请选择合同竣工时间！', trigger: ['change'] },
                ],
                constructionAddr: [
                    { required: true,message: '请输入建设地址！', trigger: ['change','blur'] },
                ],
                constructionsSize: [
                    { required: true,message: '请输入建设规模！', trigger: ['change','blur'] },
                ],
                contractPrice: [
                    { required: true,message: '请输入合同价格！', trigger: ['change','blur'] },
                ],
                gczcbUnit: [
                    { required: true,message: '请输入工程总承包单位！', trigger: ['change','blur'] },
                ],
                gczcbUnitLeader: [
                    { required: true,message: '请输入工程总承包单位负责人！', trigger: ['change','blur'] },
                ],
                kcUnit: [
                    { required: true,message: '请输入勘察单位！', trigger: ['change','blur'] },
                ],
                kcUnitLeader:[
                    { required: true,message: '请输入勘察单位负责人！', trigger: ['change','blur'] },
                ],
                sjUnit:[
                    { required: true,message: '请输入设计单位！', trigger: ['change','blur'] },
                ],
                sjUnitLeader:[
                    { required: true,message: '请输入设计单位负责人！', trigger: ['change','blur'] },
                ],
                sgUnit:[
                    { required: true,message: '请输入施工单位！', trigger: ['change','blur'] },
                ],
                sgUnitLeader:[
                    { required: true,message: '请输入施工单位负责人！', trigger: ['change','blur'] },
                ],
                jlUnit:[
                    { required: true,message: '请输入监理单位！', trigger: ['change','blur'] },
                ],
                jlUnitLeader:[
                    { required: true,message: '请输入总监理工程师！', trigger: ['change','blur'] },
                ],
            },
            loadingUnitLinkMan:false,
            certBuildFrom:{
                projInfoId:'',
                constructionUnit:'',
                kcUnit:'',
                kcUnitId:'',
                sgUnit:'',
                sgUnitId:'',
                sjUnit:'',
                sjUnitId:'',
                jlUnit:'',
                jlUnitId:'',
                gczcbUnit:'',
                gczcbUnitId:'',
                contractPeriod:''
            },//证照信息
            showVerLen: 1, // 显示左侧纵向导航栏的长度
            activeTab: 0,
            linkQuerySucc: false, // 项目代码工程编码是否可输入修改
        }
    },
    mounted:function(){
        var _that = this;
        _that.initPage();
        //_that.getQualLevel('danweizilidengji');
        _that.initFromPage();
    },
    methods:{
        getUrlParam: function (name) {
            /*var _that = this;
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return unescape(r[2]);
            }else {
                _that.$message({
                    message: '项目ID不能为空',
                    type: 'error'
                });
            }
            return null;*/
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return unescape(r[2]);
            }
            return null;
        },
        initPage:function(){
            var _that = this;
            _that.certBuildFrom.projInfoId = _that.getUrlParam('projInfoId');
            if (_that.getUrlParam('showBasicButton') == 'false') {
                _that.showBasicButton = 'false'
            }
        },
        initFromPage :function(){//初始化表单页面
            var _that = this;
            var projInfoId = _that.certBuildFrom.projInfoId;
            request('',{
                url: ctx + '/rest/from/certbuild/initAeaExProjCertBuild',
                data: {
                    projInfoId:projInfoId
                },
                type:'get',
            },function (data) {
                if(data.success){
                    if( data.content){
                        _that.certBuildFrom = data.content;
                        var time = data.content.contractPeriod.split("~");
                        _that.certBuildFrom.contractStartBuildTime = time[0];
                        _that.certBuildFrom.contractEndBuildTime = time[1];
                    }
                }else {
                    _that.$message({
                        message: '请求数据失败',
                        type: 'error'
                    });
                }

            })
        },

        // 新增人员设置
        addLinkmanTypes: function(row){
            var dataType = {
                personSetting:[{
                    linkmanType:'',
                    linkmanInfoId:'',
                    linkmanName:'',
                    safeLicenceNum:'',
                    professionCertType:'',
                    titleCertNum:'',
                    linkmanCertNo:''
                }],
            };
            row.push(dataType);
        },
        // 删除人员设置
        delLinkmanTypes: function(row,index){
            var _that = this;
            if (row[index].projLinkmanId){
                _that.personIdList.push(row[index].projLinkmanId)
            }
            row.splice(index,1);
        },
        //保存（更新）表单信息
        saveOrUpdateCertBuildFrom(){
            var _that = this;
            // _that.certBuildFrom = JSON.parse (JSON.stringify(_that.certBuildFrom));
            _that.$refs['certBuildFrom'].validate(function (valid){
                if(valid){
                    _that.certBuildFrom.contractPeriod = _that.certBuildFrom.contractEndBuildTime.replace(/-/g,".") + '~' + _that.certBuildFrom.contractStartBuildTime.replace(/-/g,".");
                    request('',{
                        url: ctx + '/rest/from/certbuild/saveAeaExProjCertBuild',
                        data: _that.certBuildFrom,
                        type: 'post',

                    },function (data) {
                        if(data.success){
                            _that.$message({
                                message: '保存成功',
                                type: 'success'
                            });
                            window.location.reload();
                        }else {
                            _that.$message({
                                message: data.message,
                                type: 'error'
                            });
                        }
                    })
                }else {
                    _that.$message({
                        message: '请输入必填字段',
                        type: 'error'
                    });
                }
            })
        },
        delPeronSetting:function(){
            var _that = this;
            if(_that.personIdList && _that.personIdList!=""){
                var parm = _that.personIdList.join();
                request('',{
                    url: ctx + '/rest/from/exSJUnit/delPersonSetting',
                    data: {
                        parm: parm
                    },
                    type: 'get',
                },function (data) {
                    if (data.success) {
                        console.log("删除成功")
                    }else {
                        _that.$message({
                            message: '删除人员失败',
                            type: 'error'
                        });
                    }
                })
            }
        },
        // 根据单位id获取关联联系人
        //projInfoId: _that.certBuildFrom.projInfoId,
        getUnitLinkManList: function(row,type) {
            var _that = this;
            if(row){
                var _that = this;
                _that.loadingUnitLinkMan = true;
                request('', {
                    url: ctx + 'rest/linkman/list',
                    type: 'get',
                    data: {
                        keyword: '',
                        unitInfoId: row ? row: ''
                    }
                }, function (data) {
                    _that.loadingUnitLinkMan = false;
                    if (data.success) {
                        if(type == 'gc'){
                            _that.gcunitLinkManOptions = data.content;
                        }else if(type == 'kc'){
                            _that.kcunitLinkManOptions = data.content;
                        }else if(type == 'sj'){
                            _that.sjunitLinkManOptions = data.content;
                        }else if(type == 'sg'){
                            _that.sgunitLinkManOptions = data.content;
                        }else if(type == 'jl'){
                            _that.jlunitLinkManOptions = data.content;
                        }
                    }
                }, function (msg) {
                    _that.loadingUnitLinkMan = false;
                });
            }
        },
        //单位名称模糊查询
        querySearchJiansheName: function(queryString, cb){
            var _that = this;
            if (typeof (queryString) != 'undefined') queryString = queryString.trim();
            if (queryString != '' && queryString.length >= 2) {
                request('', {
                    url: ctx + '/rest/from/exSJUnit/list',
                    type: 'get',
                    data: {"keyword": queryString,"projInfoId": _that.projInfoId},
                }, function (result) {
                    if (result) {
                        _that.projNameSelect = result.content;

                        _that.projNameSelect.map(function (item) {
                            if(item){
                                Vue.set(item, 'value', item.applicant);
                            }
                        })
                        var results = queryString ? _that.projNameSelect.filter(_that.createFilter(queryString)) : _that.projNameSelect;
                        // 调用 callback 返回建议列表的数据
                        cb(results);
                    }
                }, function (msg) {
                    cb([]);
                })
            } else {
                cb([]);
            }
        },
        // 项目名称过滤
        createFilter:function(queryString) {
            return function(projNameSelect) {
                return (projNameSelect.value.toLowerCase());
            };
        },
        //选择单位
        selUnitInfo: function(val,flag, index){
            if(flag=='constructionUnit'){
                this.certBuildFrom.constructionUnit = val.applicant;
            }else if(flag == 'kcUnit'){
                this.certBuildFrom.kcUnit = val.applicant;
                this.certBuildFrom.kcUnitId = val.unitInfoId;
            }else if(flag == 'gczcbUnit'){
                this.certBuildFrom.gczcbUnit = val.applicant;
                this.certBuildFrom.gczcbUnitId = val.unitInfoId;
            }else if(flag == 'sjUnit'){
                this.certBuildFrom.sjUnit = val.applicant;
                this.certBuildFrom.sjUnitId = val.unitInfoId;
            }else if(flag == 'sgUnit'){
                this.certBuildFrom.sgUnit = val.applicant;
                this.certBuildFrom.sgUnitId = val.unitInfoId;
            }else if(flag == 'jlUnit'){
                this.certBuildFrom.jlUnit = val.applicant;
                this.certBuildFrom.jlUnitId = val.unitInfoId;
            }
        },
        // 人员设置选择人员
        selTypeLinkman: function (typeData,manData) {
            typeData.linkmanName = manData.addressName;
            typeData.linkmanCertNo = manData.addressIdCard;
            typeData.linkmanInfoId = manData.addressId
        },
        //选择项目负责人
        selLinkman: function(data,ind1,type){
            var _that = this;
            if(type == 'gczcbUnit'){
                if(data){
                    _that.certBuildFrom.gczcbUnitLeader = data.addressName;
                    _that.certBuildFrom.gczcbUnitLeaderId = data.addressId;
                }
            }
            if(type == 'kcUnit'){
                if(data){
                    _that.certBuildFrom.kcUnitLeader = data.addressName;
                    _that.certBuildFrom.kcUnitLeaderId = data.addressId;
                }
            }
            if(type == 'sgUnit'){
                if(data){
                    _that.certBuildFrom.sgUnitLeader = data.addressName;
                    _that.certBuildFrom.sgUnitLeaderId = data.addressId;
                }
            }
            if(type == 'jlUnit'){
                if(data){
                    _that.certBuildFrom.jlUnitLeader = data.addressName;
                    _that.certBuildFrom.jlUnitLeaderId = data.addressId;
                }
            }
            if(type == 'sjUnit'){
                if(data){
                    _that.certBuildFrom.sjUnitLeader = data.addressName;
                    _that.certBuildFrom.sjUnitLeaderId = data.addressId;
                }
            }
        },
        // 删除联系人
        delLinkman: function (data,parentData,ind) {
            var _that = this;
            if(!data.addressId){
                alertMsg('提示信息', '联系人ID为空', '关闭', 'warning', true);
                return;
            }else{
                confirmMsg('此操作影响：','确定要删除该联系人吗？',function(){
                    request('', {
                        url: ctx + 'rest/linkman/delete/by/unit',
                        type: 'post',
                        data: {linkmanInfoId:data.addressId,unitInfoId:parentData.unitInfoId}
                    }, function (result) {
                        if(result.success){
                            _that.$message({
                                message: '联系人删除成功',
                                type: 'success'
                            });
                        }else {
                            _that.$message({
                                message: result.message?result.message:'联系人删除失败',
                                type: 'error'
                            });
                        }
                    },function(msg){
                        _that.$message({
                            message: msg.message ? msg.message : '删除失败',
                            type: 'error'
                        });
                    })
                },function(){},'确定','取消','warning',true)
            }
        },
        // 新增编辑联系人信息
        addLinkman: function(linkmanid,id,name){
            var _that = this;
            _that.addEditManModalShow = true;
            //_that.getUnitsListByProjInfoId();
            //_that.addEditManPerform = parData
            if(_that.certBuildFrom.projInfoId){
                if(linkmanid){
                    _that.addEditManModalTitle = '编辑联系人';
                    _that.addEditManModalFlag = 'edit';
                    if(!linkmanid){
                        alertMsg('提示信息', '请选择联系人！', '关闭', 'error', true);
                        return;
                    }else{
                        _that.backDLinkmanInfo(linkmanid,id,name);
                    }
                }else {
                    _that.addEditManModalTitle = '新增联系人';
                    _that.addEditManform = {};
                    _that.addEditManModalFlag = 'add';
                    if(id){
                        _that.addEditManform.unitInfoId = id;
                        _that.addEditManform.unitName = name;
                    }else {
                        _that.addEditManform.unitInfoId = '';
                        _that.addEditManform.unitName = name;
                    }
                }
            }else {
                alertMsg('', '请先查出项目信息！', '关闭', 'error', true);
            }
        },
        // 根据项目ID查找关联的单位列表
        getUnitsListByProjInfoId: function(){
            var _that = this;
            _that.loading = true;
            if(_that.certBuildFrom.unitInfoId){
                var unitInfoId = _that.certBuildFrom.unitInfoId;
            }
            request('', {
                url: ctx + 'rest/unit/list/by/'+unitInfoId,
                type: 'get',
            }, function (result) {
                if(result.success){
                    _that.unitInfoIdList = result.content;
                    _that.loading = false;
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败！',
                    type: 'error'
                });
                _that.loading = false;
            });
        },
        // 反显联系人信息
        backDLinkmanInfo: function(linkmanid,id,name){
            var _that = this;
            if(linkmanid){
                request('', {
                    url: ctx + 'rest/linkman/one/'+linkmanid,
                    type: 'get'
                }, function (result) {
                    if(result.success){
                        _that.addEditManform = result.content;
                        _that.addEditManform.unitInfoId = id;
                        _that.addEditManform.unitName = name;
                    }
                }, function (msg) {
                    alertMsg('', '服务请求失败', '关闭', 'error', true);
                });
            }else{
                _that.aeaLinkmanInfoList = {};
            }
        },
        // 保存联系人信息
        saveAeaLinkmanInfo: function (linkmanType) {
            var _that = this;
            _that.addEditManform.linkmanType=linkmanType
            _that.$refs['addEditManform'].validate(function (valid) {
                if (valid) {
                    _that.loading = true;
                    var url, msg;
                    if(_that.addEditManModalFlag=='edit'){
                        url = 'rest/linkman/edit';
                        msg = '编辑联系人信息保存成功';
                    }else {
                        url = 'rest/linkman/save'
                        msg = '新增联系人信息保存成功';
                    }
                    request('', {
                        url: ctx + url,
                        type: 'post',
                        data: _that.addEditManform
                    }, function (result) {
                        if(result.success){
                            _that.$message({
                                message: '保存成功',
                                type: 'success'
                            });
                            _that.addEditManPerform.linkmanName = _that.addEditManform.linkmanName;
                            _that.addEditManPerform.linkmanId = result.content;
                            _that.addEditManPerform.linkmanMail = _that.addEditManform.linkmanMail;
                            _that.addEditManPerform.linkmanCertNo = _that.addEditManform.linkmanCertNo;
                            _that.addEditManPerform.linkmanMobilePhone = _that.addEditManform.linkmanMobilePhone;
                            _that.addEditManModalShow = false;
                            _that.loading = false;
                        }
                    }, function (msg) {
                        _that.$message({
                            message: msg.message?msg.message:'保存失败！',
                            type: 'error'
                        });
                        _that.loading = false;
                    });
                } else {
                    _that.$message({
                        message: '请输入完整的联系人信息！',
                        type: 'error'
                    });
                    return false;
                }
            });
        },
        // 重置表单
        resetForm: function(formName) {
            this.$refs[formName].resetFields();
            this.selectMat = '';
        },
    }
})