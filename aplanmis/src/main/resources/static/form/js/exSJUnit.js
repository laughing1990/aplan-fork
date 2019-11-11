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
        return{
            projInfoId:'',
            loading: false, // 页面加载遮罩
            verticalTabData: [ // 左侧纵向导航数据
                {
                    label: '施工和监理单位信息',
                    target: 'baseInfo'
                },
            ],
            activeNames: ['1', '2', '3', '4', '5', '6'],// el-collapse 默认展开列表
            unitInfoShowFromRules: {  // 基本信息校验
                provinceProjCode: [
                    { required: true,message: '请输入省级项目编号！', trigger: ['change','blur'] },
                ],
                contractStartBuildTime: [
                    { required: true,message: '请选择合同开工时间！', trigger: ['change'] },
                ],
                contractEndBuildTime: [
                    { required: true,message: '请选择合同竣工时间！', trigger: ['change'] },
                ],
                // structureSystem: [
                //     { required: true,message: '请选择结构体系！', trigger: ['change'] },
                // ],
                buildArea: [
                    {validator:checkNumFloat, trigger: ['blur'] },
                    { required: true,message: '请填写施工面积！', trigger: ['change'] }
                ]
            },
            gongchengzongFromRules:{  //工程总承包单位信息校验
                applicant:[
                    { required: true,message: '请输入单位名称！', trigger: ['change'] },
                ],
                idrepresentative:[
                    { required: true,message: '请输入法定代表人！', trigger: ['change'] },
                ],
                gongchengzongManager:[
                    { required: true,message: '请输入工程总承包项目经理！', trigger: ['change'] },
                ],
                idmobile:[
                    {validator:checkNumFloat, trigger: ['blur'] },
                    { required: true,message: '请填写法定代表人联系电话！', trigger: ['change'] }
                ],
                gongchengzongManagerTel:[
                    {validator:checkNumFloat, trigger: ['blur'] },
                    { required: true,message: '请填写工程总承包项目经理联系电话！', trigger: ['change'] }
                ]
            },
            rulesApplyShigongzongFrom: {//施工总承包单位校验
                applicant: [
                    { required: true,message: '请输入单位名称！', trigger: ['change'] },
                ],
                qualLevelName:[
                    { required: true,message: '请输入资质等级！', trigger: ['change'] },
                ],
                certinstCode:[
                    { required: true,message: '请输入证书编号！', trigger: ['change'] },
                ],
                safeLicenceNum:[
                    { required: true,message: '请输入安全生产许可证编号！', trigger: ['change'] },
                ],
                idrepresentative:[
                    { required: true,message: '请输入法定代表人！', trigger: ['change'] },
                ],
                idmobile:[
                    {validator:checkNumFloat, trigger: ['blur'] },
                    { required: true,message: '请填写法定代表人联系电话！', trigger: ['change'] }
                ],
                projectManager:[
                    { required: true,message: '请输入项目负责人（项目经理）！', trigger: ['change'] },
                ],
                registerNum:[
                    { required: true,message: '请输入注册编号！', trigger: ['change'] },
                ],
                safeLicenceNum:[
                    { required: true,message: '请输入安全生产考核合格证号！', trigger: ['change'] },
                ],
                projectManagerTel:[
                    { required: true,message: '请输入项目负责人（项目经理）联系电话！', trigger: ['change'] },
                ],
                idCard:[
                    { required: false,message: '请输入身份证号码！', trigger: ['change'] },
                ]
            },
            rulesShigongzhuanyefenbaoFrom:{
                applylinkUnitName:[
                    { required: true,message: '请输入单位名称!', trigger: ['change'] },
                ],
                qualificationLevel:[
                    { required: true,message: '请输入资质等级!', trigger: ['change'] },
                ],
                certificateNo:[
                    { required: true,message: '请输入证书编号!', trigger: ['change'] },
                ],
                safetyLicenseNo:[
                    { required: true,message: '请输入安全生产许可证编号!', trigger: ['change'] },
                ],
                legalPeople:[
                    { required: true,message: '请输入法定代表人!', trigger: ['change'] },
                ],
                legalPeopleTel:[
                    { required: true,message: '请输入法定代表人联系电话!', trigger: ['change'] },
                ],
                projectManager:[
                    { required: true,message: '请输入专业分包技术负责人!', trigger: ['change'] },
                ],
                registrationNo:[
                    { required: true,message: '请输入注册编号!', trigger: ['change'] },
                ],
                pSafetyAssessNo:[
                    { required: true,message: '请输入安全生产考核合格证号!', trigger: ['change'] },
                ],
                applyLinkmanTel:[
                    { required: true,message: '请输入专业分包技术负责人联系电话!', trigger: ['change'] },
                ],
                // idCard:[
                //     { required: true,message: '请输入身份证号码!', trigger: ['change'] },
                // ]
            },
            rulesShigonglaowufenbaoFrom: {
                applylinkUnitName:[
                    { required: true,message: '单位名称!', trigger: ['change'] },
                ],
                qualificationLevel:[
                    { required: true,message: '请输入资质等级!', trigger: ['change'] },
                ],
                certificateNo:[
                    { required: true,message: '请输入证书编号!', trigger: ['change'] },
                ],
                safetyLicenseNo:[
                    { required: true,message: '请输入安全生产许可证编号!', trigger: ['change'] },
                ],
                legalPeople:[
                    { required: true,message: '请输入法定代表人!', trigger: ['change'] },
                ],
                legalPeopleTel:[
                    { required: true,message: '请输入法定代表人联系电话!', trigger: ['change'] },
                ],
                projectManager:[
                    { required: true,message: '请输入劳务分包技术负责人!', trigger: ['change'] },
                ],
                registrationNo:[
                    { required: true,message: '请输入注册编号!', trigger: ['change'] },
                ],
                pSafetyAssessNo:[
                    { required: true,message: '请输入安全生产考核合格证号!', trigger: ['change'] },
                ],
                projectManagerTel:[
                    { required: true,message: '请输入劳务分包技术负责人联系电话!', trigger: ['change'] },
                ]
            },
            rulesJianliFrom:{
                orgCode:[
                    { required: true,message: '请输入组织机构代码!', trigger: ['change'] },
                ],
                creditCode:[
                    { required: true,message: '请输入统一社会信用代码!', trigger: ['change'] },
                ],
                unitName:[
                    { required: true,message: '请输入单位名称!', trigger: ['change'] },
                ],
                proType:[
                    { required: true,message: '请输入项目主体类型!', trigger: ['change'] },
                ],
                isProvincial:[
                    { required: true,message: '请选择是否省内企业!', trigger: ['change'] },
                ],
                projectDirectorName:[
                    { required: true,message: '请输入项目总监姓名!', trigger: ['change'] },
                ],
                projectDirectorID:[
                    { required: true,message: '请输入项目总监身份证号码!', trigger: ['change'] },
                ]
            },
            loadingUnitLinkMan:false,
            unitInfoShowFrom:{
                projInfoId:'',//项目id
            },//单位基本信息
            showVerLen: 1, // 显示左侧纵向导航栏的长度
            activeTab: 0,
            linkQuerySucc: false, // 项目代码工程编码是否可输入修改
            structureSystem:[],//结构体系
            projUnitLinkmanType:[],//人员类型
            professionCertType:[],//执业注册证类型
            jiangliLinkmanType:[],//监理承担角色
            titleCertNum:[],//职称等级
            qualLevelName:[],//资历等级
            unitLinkManOptions:[],//联系人
            applyShigongzongFrom: {
                personSetting:[{
                    linkmanType:'',
                    linkmanInfoId:'',
                    linkmanName:'',
                    safeLicenceNum:'',
                    professionCertType:'',
                    titleCertNum:'',
                    linkmanCertNo:''
                }]
            },  // 施工总承包单位信息
            applyShigongzhuanyefenbaoFrom:{
                personSetting:[{
                    linkmanType:'',
                    linkmanInfoId:'',
                    linkmanName:'',
                    safeLicenceNum:'',
                    professionCertType:'',
                    titleCertNum:'',
                    linkmanCertNo:''
                }],
            },//施工专业分包单位信息
            applyShigonglaowufenbaoFrom:{
                personSetting:[{
                    linkmanType:'',
                    linkmanInfoId:'',
                    linkmanName:'',
                    safeLicenceNum:'',
                    professionCertType:'',
                    titleCertNum:'',
                    linkmanCertNo:''
                }],
            },//施工劳务分包单位信息
            applyJianliFrom:{
                personSetting:[{
                    linkmanType:'',
                    linkmanInfoId:'',
                    linkmanName:'',
                    safeLicenceNum:'',
                    professionCertType:'',
                    titleCertNum:'',
                    linkmanCertNo:''
                }],
            },//监理单位信息
            gongchengzongFrom:{},//工程总承包单位信息
            exSJAllUnit:{
                aeaExProjBuildUnitInfo:'',
            },//所有表单集合
        }
    },
    mounted:function(){
        var _that = this;
        _that.initPage();
        _that.getInfoByDataDictionary('STRUCTURESYSTEM,PROJ_UNIT_LINKMAN_TYPE,XM_DWLX,JIANGLI_LINKMAN_TYPE,PROFESSION_CERT_TYPE,TITLE_CERT_NUM');
        _that.getQualLevel('danweizilidengji');
        _that.initExSJUnitFromPage();
    },
    methods:{
        getUrlParam: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return unescape(r[2]);
            }
            return null;
        },
        initPage:function(){
            var _that = this;
            _that.unitInfoShowFrom.projInfoId = _that.getUrlParam('projInfoId');
        },
        initExSJUnitFromPage:function(){//初始化表单页面
            var _that = this;
            var projInfoId = _that.unitInfoShowFrom.projInfoId;
            request('',{
                url: ctx + '/rest/from/exSJUnit/initExSJUnit',
                data: {
                    projInfoId:projInfoId
                },
                type:'get',
            },function (data) {
                debugger;
                if( data.content.unitInfoShowFrom){
                    _that.unitInfoShowFrom = data.content.unitInfoShowFrom;
                }
                if(data.content.gongchengzongFrom){
                    _that.gongchengzongFrom = data.content.gongchengzongFrom;
                }
                if(data.content.applyShigongzongFrom){
                    _that.applyShigongzongFrom = data.content.applyShigongzongFrom;
                }
                if(data.content.applyShigongzhuanyefenbaoFrom){
                    _that.applyShigongzhuanyefenbaoFrom = data.content.applyShigongzhuanyefenbaoFrom;

                }
                if(data.content.applyShigonglaowufenbaoFrom){
                    _that.applyShigonglaowufenbaoFrom = data.content.applyShigonglaowufenbaoFrom;
                }
                if(data.content.applyJianliFrom){
                    _that.applyJianliFrom = data.content.applyJianliFrom;
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
        // 新增人员设置
        delLinkmanTypes: function(row,index){
            row.splice(index,1);
        },
        //保存（更新）表单信息
        saveOrUpdateSJUnitFrom(){
            var _that = this;
            _that.exSJAllUnit = JSON.parse (JSON.stringify(_that.unitInfoShowFrom));
            var gongchengzong = JSON.parse(JSON.stringify(_that.gongchengzongFrom));
            gongchengzong.unitType = "9";
            gongchengzong.linkmanType = "104001";
            var shigongzong = JSON.parse(JSON.stringify(_that.applyShigongzongFrom));
            shigongzong.unitType = "10";
            shigongzong.linkmanType = "104001";
            var shigongzhuanyefenbao = JSON.parse(JSON.stringify(_that.applyShigongzhuanyefenbaoFrom));
            shigongzhuanyefenbao.unitType="6";
            shigongzhuanyefenbao.linkmanType = "104002";
            var shigonglaowufenbao = JSON.parse(JSON.stringify(_that.applyShigonglaowufenbaoFrom));
            shigonglaowufenbao.unitType="8";
            shigonglaowufenbao.linkmanType = "104002";
            var applyJianli = JSON.parse(JSON.stringify(_that.applyJianliFrom));
            applyJianli.unitType="19"
            shigonglaowufenbao.linkmanType = "105001";
            var a = [gongchengzong,shigongzong,shigongzhuanyefenbao,shigonglaowufenbao,applyJianli];
            _that.exSJAllUnit.aeaExProjBuildUnitInfo = JSON.stringify(a);
            _that.$refs['unitInfoShowFrom'].validate(function (valid){
                if(valid){
                    request('',{
                        url: ctx + '/rest/from/exSJUnit/saveOrUpdateSJUnitInfo',
                        data: _that.exSJAllUnit,
                        type: 'post',

                    },function (data) {
                        if(data.success){
                            _that.$message({
                                message: '保存成功',
                                type: 'success'
                            });
                        }else {
                            _that.$message({
                                message: '保存失败',
                                type: 'error'
                            });
                        }
                    })
                }
            })
        },
        //从数据字典获取信息
        getInfoByDataDictionary:function(code){
            var _that = this;
            request('',{
                url: ctx + 'rest/dict/code/multi/items/list',
                type: 'get',
                data: {'dicCodeTypeCodes': code}
            },function (data) {
                if(data.content){
                    _that.structureSystem = data.content.STRUCTURESYSTEM;
                    _that.projUnitLinkmanType = data.content.PROJ_UNIT_LINKMAN_TYPE;
                    _that.jiangliLinkmanType = data.content.JIANGLI_LINKMAN_TYPE;
                    _that.professionCertType = data.content.PROFESSION_CERT_TYPE;
                    _that.titleCertNum = data.content.TITLE_CERT_NUM;
                }else {
                    _that.$message({
                        message: '服务请求失败',
                        type: 'error'
                    });
                }
            })
        },
        //获取资历等级
        getQualLevel:function(code){
            var _that = this;
            request('',{
                url: ctx + '/rest/from/exSJUnit/listQualLevel',
                type: 'get',
                data: {'parentQualLevelId': code}
            },function (data) {
                if(data.content){
                    _that.qualLevelName = data.content;
                }else {
                    _that.$message({
                        message: '服务请求失败',
                        type: 'error'
                    });
                }
            })
        },
        // 根据单位id获取关联联系人
        getUnitLinkManList: function(row) {
            var _that = this;
            _that.loadingUnitLinkMan = true;
            request('', {
                url: ctx + 'rest/linkman/list',
                type: 'get',
                data: {
                    keyword: '',
                    unitInfoId: row.unitInfoId ? row.unitInfoId : '',
                    projInfoId: _that.projInfoId,

                }
            }, function (data) {
                _that.loadingUnitLinkMan = false;
                if (data.success) {
                    _that.unitLinkManOptions = data.content;
                }
            }, function (msg) {
                _that.loadingUnitLinkMan = false;
            });
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
            console.log(val,flag);
            if(flag=='shigongzongchengbao'){
                val.personSetting = JSON.parse (JSON.stringify(val.personSetting))
                this.applyShigongzongFrom = val;
            }else if(flag == 'gongchengzongFrom'){
                val.personSetting = JSON.parse (JSON.stringify(val.personSetting))
                this.gongchengzongFrom = val;
            }else if(flag == 'applyShigongzhuanyefenbaoFrom'){
                val.personSetting = JSON.parse (JSON.stringify(val.personSetting))
                this.applyShigongzhuanyefenbaoFrom = val;
            }else if(flag == 'applyShigonglaowufenbaoFrom'){
                val.personSetting = JSON.parse (JSON.stringify(val.personSetting))
                this.applyShigonglaowufenbaoFrom = val;
            }else if(flag == 'applyJianliFrom'){
                val.personSetting = JSON.parse (JSON.stringify(val.personSetting))
                this.applyJianliFrom = val;
            }
        },
        // 人员设置选择人员
        selTypeLinkman: function (typeData,manData) {
            typeData.linkmanName = manData.addressName;
            typeData.linkmanCertNo = manData.addressIdCard;
            typeData.safeLicenceNum = manData.addressIdCard;
            typeData.linkmanInfoId = manData.addressId
        },
    }
})