/*
 * @Author:
 * @Date:
 * @Last Modified by:
 * @Last Modified time: $ $
 */
var vm = new Vue({
    el: '#approve',
    data: function () {
        var checkMissValue = function (rule, value, callback) {
            if (value === '' || value === undefined || value === null) {
                callback(new Error('该输入项为必输项！'));
            } else if (value.toString().trim() === '') {
                callback(new Error('该输入项为必输项！'));
            } else {
                callback();
            }
        };
        var checkPhoneNum = function (rule, value, callback) {
            if (value === '' || value === undefined || value.trim() === '') {
                callback(new Error('该输入项为必输项！'));
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
        return {

            receiveMode_dig: '',
            isConsigner_dig: '',
            addresseeName: 'gggg',
            addresseePhone: 'gggg',
            addresseeIdcard: 'gggg',
            addresseeP: 'gggg',
            addresseeAddr: 'gggg',
            senderName: 'gggg',
            senderPhone: 'gggg',
            senderAddr: 'gggg',

            tableData3: [],
            needSendItem: [],
            editCertFormType: '',
            editCertForm: {
                /* certinstId: '',
                 certId: '',
                 certinstCode: '',
                 certinstName: '',
                 termStart: '',
                 termEnd: '',
                 issueDate: '',
                 certOwner: '',
                 unitInfoId: ''*/
            },
            certTableTabs: [],
            tabActive: '',
            showUpdateBtn: true,
            editCertModalShow: false,
            editCertModalTitle: '证件材料详情',
            editItemSmsInfoModalTitle: '取件详情',
            showSaveBtn: true,

            sendInfoForm: {
                receiveMode: "1",
                smsType: '1',
                senderName: '',
                senderPhone: '',
                senderProvince: '',
                senderCity: '',
                senderCounty: '',
                senderAddr: '',

                orderId: '',//订单号
                expressNum: '',//运单号
                addresseeName: '',
                addresseePhone: '',
                addresseeIdcard: '',
                addresseeProvince: '',
                addresseeCity: '',
                addresseeCounty: '',
                addresseeAddr: '',
                consignerAttId: '',
                isConsigner: '0'

            },
            consignerForm: {
                addresseeName: '',
                addresseePhone: '',
                addresseeIdcard: '',
                addresseeProvince: '',
                addresseeCity: '',
                addresseeCounty: '',
                addresseeAddr: ''
            },

            ItemSmsInfoForm: {
                receiveMode: '1',
                smsType: '1',
                senderName: '',
                senderPhone: '',
                senderProvince: '',
                senderCity: '',
                senderCounty: '',
                senderAddr: '',

                orderId: '',//订单号
                expressNum: '',//运单号
                addresseeName: '',
                addresseePhone: '',
                addresseeIdcard: '',
                addresseeProvince: '',
                addresseeCity: '',
                addresseeCounty: '',
                addresseeAddr: '',
                consignerAttId: '',
                isConsigner: '0'

            },
            editItemSmsInfoModalShow: false,
            isConsigner: '0',
            multipleSelection: [],
            hadSelectionTmp: [],
            applyinstId: '',
            consignerInfoRules: {
                addresseePhone: [
                    {required: true, validator: checkPhoneNum, trigger: 'blur'},

                ],
                addresseeAddr: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseePhone: [
                    {required: true, validator: checkPhoneNum, trigger: 'blur'},
                ],
                addresseeIdcard: [
                    {required: true, validator: checkMissValue, trigger: 'blur'}
                ],
                addresseeProvince: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeCity: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeCounty: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ]
            },
            itemSmsInfoFormRules: {
                senderName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                senderPhone: [
                    {required: true, validator: checkPhoneNum, trigger: 'blur'},
                ],
                senderProvince: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                senderCity: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                senderCounty: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                senderAddr: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],

                addresseeName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseePhone: [
                    {required: true, validator: checkPhoneNum, trigger: 'blur'},
                ],

                orderId: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                expressNum: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeIdcard: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                    {
                        pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
                        message: '证件号码格式有误！',
                        trigger: 'blur'
                    }
                ],
                addresseeProvince: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeCity: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeCounty: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeAddr: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],


            },

            sendSmsInfoRules: { // 新增编辑校验
                senderName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                senderPhone: [
                    {required: true, validator: checkPhoneNum, trigger: 'blur'},
                ],
                senderProvince: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                senderCity: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                senderCounty: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                senderAddr: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseePhone: [
                    {required: true, validator: checkPhoneNum, trigger: 'blur'},
                ],
                orderId: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                expressNum: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeIdcard: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                    {
                        pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
                        message: '证件号码格式有误！',
                        trigger: 'blur'
                    }
                ],
                addresseeProvince: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeCity: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeCounty: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeAddr: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ]

            },
            certFormRules: {//证照实例校验
                certId: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                certinstCode: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                certinstName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                issueOrgId: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                termStart: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                termEnd: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                issueDate: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                certOwner: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
            },
            uploadTableData: [],
            fileSelectionList: [], // 所选电子件
            selMatRowData: {}, // 所选择的材料信息
            fileWinData: [], // 上传窗口上传参数列表
            loadingFileWin: false, // 窗口文件上传loading

            //证照
            uploadCertData: [],
            certSelectionList: [],
            certWinData: [],
            currentIteminstId: '',//当前iteminstId
            activeNames: ['1', '2', '3', '4', '5'], // el-collapse 默认展开列表
            verticalTabData: [ // 左侧纵向导航数据
                {
                    label: '事项信息列表',
                    target: 'baseInfo'
                }, {
                    label: '领取方式',
                    target: 'receiveMode'
                }, {
                    label: '领件人信息',
                    target: 'receiver'
                }, {
                    label: '寄件人信息',
                    target: 'sender'
                }
            ],
            curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
            curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
            loading: false,
            ctx: ctx,
            fileList: [],
            activeTab: 0,  // 纵向导航active状态index
            showVerLen: 3, // 显示左侧纵向导航栏的长度
            certList: [],
            orgList: [],
            unitInfoList: [],
            provinceList: [], // 所有的省份信息
            cityList: [], // 所有市区信息
            countyList: [], // 所有地区信息
            provinceList_jjr: [], // 所有的省份信息
            cityList_jjr: [], // 所有市区信息
            countyList_jjr: [], // 所有地区信息
            isSmsSend: '0',//是否已经出件了
        }
    },
    mounted: function () {
        var _that = this;
        _that.applyinstId = applyinstId;
        _that.windowUserName = windowUserName;
        _that.searchSmsInfo();
        _that.getFileListWin('', _that.selMatRowData);
        // _that.getCertBasicInfoList();
        _that.queryProvince();
    },
    watch: {
        searchProjfilterText: function (val) {
            this.$refs.projTree.filter(val);
        },
        searchOrgfilterText: function (val) {
            this.$refs.orgTree.filter(val);
        },
    },
    methods: {
        //一次出件
        doOnceSend: function () {
            var _this = this;
            if (_this.needSendItem.length == '0') {
                return;
            }

            if (_this.multipleSelection.length > 0 && _this.hadSelectionTmp.length > 0) {
                _this.$message({
                    message: '已有个别事项出件，不能一次出件，请手动勾选！！',
                    type: 'error'
                });
            } else {
                // _this.toggleSelection(_this.tableData3);
                $.each(_this.tableData3, function (idx, obj) {
                    if (obj.hasOutCertinst == '1' && (obj.iteminstState == '11' || obj.iteminstState == '12')) {
                        _this.$refs.multipleTable.toggleRowSelection(obj);
                    }
                });
            }
        },
        // 获取省份信息
        queryProvince: function () {
            var _that = this;
            request('', {
                url: ctx + 'rest/apply/province',
                type: 'get'
            }, function (data) {
                _that.provinceList = data.content;
                _that.provinceList_jjr = data.content;
                _that.queryCityData(0, 'jjr');
                _that.queryCityData(0, 'lz');
            })
        },
        // 选择省份
        queryCityData: function (index, type) {

            if ('lz' == type) {
                this.cityList = this.provinceList[index].cityList;
            } else {
                this.cityList_jjr = this.provinceList_jjr[index].cityList;
            }

            this.queryAreaData(0, type);
            // this.sendInfoForm.addresseeProvince = this.provinceList[index].code;
        },
        // 选择市地区
        queryAreaData: function (index, type) {
            if (type == 'lz') {
                this.countyList = this.cityList[index].areaList;
            } else {
                this.countyList_jjr = this.cityList_jjr[index].areaList;
            }
            // this.queryCountyData(this.countyList[0].code);
        },
        // 选择地区
        queryCountyData: function (code) {
            // this.sendInfoForm.addresseeCounty=code;
        },
        //给已出件的添加底色
        tableRowClass: function (obj) {
            if (obj.row.isSmsSend == '1' || obj.row.hasOutCertinst == "0" || obj.row.iteminstState == '13') {
                return 'warning-row';
            }
            return '';
        },
        /** 获取制证所用基本信息**/
        /*getCertBasicInfoList: function () {
            var _this = this;
            request('', {
                url: ctx + 'rest/certificate/getBasicInfo',
                type: 'get'
            }, function (result) {
                if (result.content) {
                    _this.certList = result.content.certList;
                    _this.orgList = result.content.orgList;
                    _this.unitInfoList = result.content.orgList;
                }

            }, function (msg) {
                _this.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },*/
        //状态转换
        statuFormatter: function (row, col) {
            if (row.isSmsSend == '1') {
                return "已出件";
            } else {
                if (row.hasOutCertinst == '1' && (row.iteminstState == '11' || row.iteminstState == '12')) {
                    return "未出件";
                }
                return "无输出材料";
            }
        },
        itemStatuFormatter: function (row, col) {
            if (row.iteminstState == '13') {
                return "办结不通过";
            } else if (row.iteminstState == '12' || row.iteminstState == '11') {
                return "办结";
            } else if (row.iteminstState == '4'){
                return "不受理";
            } else if (row.iteminstState == '8'){
                return "部门办理";
            } else if (row.iteminstState == '5'){
                return "不予受理";
            } else if (row.iteminstState == '14'){
                return "撤回";
            } else if (row.iteminstState == '15'){
                return "15撤销";
            } else {
                return "-";
            }
        },
        targetCollapse: function (target, ind) { // 纵向导航点击事件
            this.activeTab = ind;
            $(document).scrollTop($('#' + target).offset().top)
        },
        handleScroll: function () { // 屏幕滚动纵向导航相应获取焦点
            var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
            var ele = $('.el-collapse-item');
            var eleLen = ele.length;
            var _that = this;
            for (var i = 0; i < eleLen; i++) {
                if (scrollTop >= ($(ele[i]).offset().top - 60)) {
                    _that.activeTab = i
                }
            }
        },
        //更新证照信息
        updateItemCert: function () {
            var _this = this;
            if (_this.editCertForm.issueDate == '' || _this.editCertForm.issueDate == null) {
                _this.$message({
                    message: '请选择日期',
                    type: 'error'
                });
            }
            var detailId = '';
            if (_this.uploadCertData.length > 0) {
                $.each(_this.uploadCertData, function (idx, obj) {
                    detailId += obj.fileId + ",";
                });
            } else {
                _this.$message({
                    message: '请先上传证照！！',
                    type: 'error'
                });
                return;
            }

            detailId = detailId.substring(0, detailId.length - 1);
            _this.$refs['editCertForm'].validate(function (valid) {
                if (valid) {
                    var obj = _this.editCertForm;
                    obj.attLinkId = detailId;
                    obj.iteminstId = _this.currentIteminstId;
                    obj.termStart = _this.dateFormat(obj.termStart);
                    obj.termEnd = _this.dateFormat(obj.termEnd);
                    obj.issueDate = _this.dateFormat(obj.issueDate);
                    obj.bscAttDetails = [];
                    obj.applyinstId = applyinstId;
                    request('', {
                        url: ctx + 'rest/certificate/updateCertInfo',
                        type: 'post',
                        data: obj
                    }, function (result) {
                        _this.editCertModalShow = false;


                    }, function (msg) {
                        _this.$message({
                            message: '服务请求失败',
                            type: 'error'
                        });
                    });
                }

            });
        },
        dateFormat: function (dateStr) {
            var t = new Date(dateStr);//row 表示一行数据, updateTime 表示要格式化的字段名称
            var month = t.getMonth() + 1 + "";
            var day = t.getDate() + "";
            var date = t.getFullYear() + '-' + month.length == 1 ? '0' + month : month + '-' + day.length == 1 ? '0' + day : day;
            return date;
        },
        //已出件的不给勾选
        checkboxInit2: function (row, idx) {
            var _this = this;
            if (row.isSmsSend == '1' || row.hasOutCertinst == "0" || row.iteminstState == '13') {
                return false;
            } else {
                return true;
            }
        },
        /**
         * 点击tab显示不同的证照实例详情
         */
        showCertPage: function (event) {
            var _this = this;
            var id = event.name;
            for (var i = 0, len = _this.certTableTabs.length; i < len; i++) {
                if (id == _this.certTableTabs[i].certinstId) {
                    _this.editCertForm = _this.certTableTabs[i];
                    break;
                }
            }
            _this.getCertListWin(id, _this.editCertFormType);
        },
        /** 查看cert信息*/
        handleEdit: function (index, row) {
            var _this = this;
            _this.currentIteminstId = row.iteminstId;

            request('', {
                url: ctx + 'rest/certificate/out/materials/view',
                type: 'get',
                data: {'iteminstId': row.iteminstId, 'applyinstId': applyinstId}
            }, function (result) {
                if (result.content) {
                    _this.certTableTabs = result.content;
                    _this.tabActive = result.content[0].id;
                    _this.editCertFormType = result.content[0].type;
                    _this.editCertForm = result.content[0].data;
                    _this.getCertListWin(_this.tabActive, _this.editCertFormType);
                }

            }, function (msg) {
                _this.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
            _this.editCertModalShow = true;

        },
        toggleSelection: function (rows) {
            if (rows) {
                //ES6 iE 不支持
                /*rows.forEach(row = > {
                    this.$refs.multipleTable.toggleRowSelection(row);
            })*/
                rows.forEach(function (row, index) {
                    this.$refs.multipleTable.toggleRowSelection(row);
                })
            } else {
                this.$refs.multipleTable.clearSelection();
            }
        },
        handleSelectionChange: function (val) {
            this.multipleSelection = val;
        },
        //过滤只显示有输出证照实例certinst的事项
        getShow: function (iteminsts) {
            /*var result =[];
            for(var i=0 ,len = iteminsts.length;i<len;i++){
                if(iteminsts[i].hasOutCertinst=="1"){
                    result.push(iteminsts[i]);
                }
            }
            return result;*/
            return iteminsts;
        },
        //根据applyinstId获取寄件收件人及事项信息信息
        searchSmsInfo: function () {
            var _that = this;
            var _windowUserName = this.windowUserName;
            var _applyinstId = this.applyinstId;
            var tmp = [];
            request('', {
                url: ctx + 'rest/certificate/out/materials/register',
                type: 'post',
                data: {'applyinstId': _applyinstId}
            }, function (result) {

                if (result.content) {
                    _that.tableData3 = _that.getShow(result.content.iteminsts);
                    _that.sendInfoForm = result.content.smsInfo;
                    // _that.sendInfoForm.senderPhone = '';
                    // _that.sendInfoForm.senderName = _windowUserName;
                    _that.sendInfoForm.smsType = '1';
                    _that.sendInfoForm.isConsigner = '0';
                    _that.$nextTick(function () {
                        $.each(_that.tableData3, function (idx, obj) {
                            if (obj.isSmsSend == '1') {
                                _that.$refs.multipleTable.toggleRowSelection(obj);

                                _that.setHadSendIndex();
                            }
                            if (obj.hasOutCertinst == '1') {
                                _that.needSendItem.push(obj);
                            }
                        });

                        /*if (_that.tableData3.length == _that.multipleSelection.length) {
                            _that.showSaveBtn = false;
                        }*/
                        if (_that.needSendItem.length == _that.multipleSelection.length) {
                            _that.showSaveBtn = false;
                        }
                        if (_that.tableData3.length == 0) {
                            _that.showSaveBtn = false;
                        }
                        _that.hadSelectionTmp = _that.multipleSelection;



                        //回显省市县



                    });
                }

            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });


        },

        /**
         * 保存出证信息
         */
        saveSmsFormInfo: function () {
            var _this = this;
            //封装采集参数
            var param = {};

            var validateResult = true;
            _this.$refs['receInfo'].validate(function (valid) {
                if (!valid) {
                    _this.$message({
                        message: '请输入完整信息！',
                        type: 'error'
                    });
                    validateResult = validateResult && false;
                    return false;
                }
            });
            _this.$refs['consignerInfo'].validate(function (valid) {
                if (!valid) {
                    _this.$message({
                        message: '请输入完整信息！',
                        type: 'error'
                    });
                    validateResult = validateResult && false;
                    return false;
                }
            });
            if (_this.sendInfoForm.receiveMode == '1') {
                _this.$refs['sendInfo'].validate(function (valid) {
                    if (!valid) {
                        _this.$message({
                            message: '请输入完整信息！',
                            type: 'error'
                        });
                        validateResult = validateResult && false;
                        return false;
                    }
                });
            } else {

                _this.$refs['sendInfo_jjr'].validate(function (valid) {
                    if (!valid) {
                        _this.$message({
                            message: '请输入完整信息！',
                            type: 'error'
                        });
                        validateResult = validateResult && false;
                        return false;
                    }
                });
            }

            _this.$refs['ItemSmsInfoForm'].validate(function (valid) {
                if (!valid) {
                    _this.$message({
                        message: '请输入完整信息！',
                        type: 'error'
                    });
                    validateResult = validateResult && false;
                    return false;
                }
            });


            //判断是否一次上传
            var _tmpSelection = [];
            if (_this.multipleSelection.length == 0) {
                this.$message({
                    message: '请勾选要出证的事项！',
                    type: 'error'
                });
                return;

            } else {
                $.each(_this.multipleSelection, function (idx, obj) {
                    if (obj.isSmsSend != '1') {
                        _tmpSelection.push(obj);
                    }
                });
                if (_tmpSelection.length == _this.needSendItem.length) {
                    param.isOnceSend = '1';
                } else {
                    param.isOnceSend = '0';
                }
            }
            // debugger
            param.applyinstId = applyinstId;
            param.isSeriesApprove = _this.multipleSelection[0].isSeriesApprove;
            _this.sendInfoForm.windowUserName = windowUserName;
            _this.sendInfoForm.windowUserId = windowUserId;
            param.sendBean = _this.sendInfoForm;
            param.consignerForm = _this.consignerForm;


            if (_this.sendInfoForm.isConsigner == '1') {
                if (_this.uploadTableData.length == 0) {
                    this.$message({
                        message: '委托领取请上传委托书！',
                        type: 'error'
                    });
                    return;
                }
                param.sendBean.isConsigner = '1';
            } else {
                param.sendBean.isConsigner = '0';
            }
            if (_this.uploadTableData.length > 0) {
                var fileIds = [];
                $.each(_this.uploadTableData, function (idx, obj) {
                    fileIds.push(obj.fileId);
                });
                _this.sendInfoForm.consignerAttId = fileIds.join(",");
            }

            param.iteminsts = _this.multipleSelection;

            if (validateResult) {
                request('', {
                    url: ctx + 'rest/certificate/out/materials/confirm',
                    type: 'post',
                    data: JSON.stringify(param),
                    ContentType: "application/json"
                }, function (result) {
                    if (result.success) {
                        _this.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                        _this.closeWindowTab();
                    }

                }, function (msg) {
                    _this.$message({
                        message: '服务请求失败',
                        type: 'error'
                    });
                });
            }


        },
        /**
         * 更新事项的出证登记历史
         * @param index
         * @param row
         */
        editSMSSend: function (index, row) {
            var _this = this;
            request('', {
                url: ctx + 'rest/certificate/out/materials/detail',
                type: 'get',
                data: {'iteminstId': row.iteminstId, 'applyinstId': applyinstId}
            }, function (result) {
                if (result.content) {
                    _this.ItemSmsInfoForm = result.content;
                    _this.showDifName(_this.ItemSmsInfoForm.isConsigner, _this.ItemSmsInfoForm.receiveMode)
                    _this.receiveMode_dig = _this.ItemSmsInfoForm.receiveMode;
                    _this.isConsigner_dig = _this.ItemSmsInfoForm.isConsigner
                    var proviceCode = _this.ItemSmsInfoForm.addresseeProvince
                    var cityCode = _this.ItemSmsInfoForm.addresseeCity;
                    var proviceCode_jjr = _this.ItemSmsInfoForm.senderProvince
                    var cityCode_jjr = _this.ItemSmsInfoForm.senderCity;
                    for (var i = 0, len = _this.provinceList.length; i < len; i++) {
                        if (proviceCode == _this.provinceList[i].code) {
                            var list = _this.provinceList[i].cityList;
                            _this.cityList = list;
                            for (var j = 0, len = list.length; j < len; j++) {
                                if (cityCode == list[j].code) {
                                    _this.countyList = list[j].areaList;
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    for (var i = 0, len = _this.provinceList.length; i < len; i++) {
                        if (proviceCode_jjr == _this.provinceList[i].code) {
                            _this.queryCityData(i,"jjr");
                            var list = _this.provinceList_jjr[i].cityList;
                            for (var j = 0, len = list.length; j < len; j++) {
                                if (cityCode_jjr == list[j].code) {
                                    _this.queryAreaData(j,"jjr");
                                    break;
                                }
                            }
                            break;
                        }
                    }


                }

            }, function (msg) {
                _this.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });

            _this.editItemSmsInfoModalShow = true;
        },

        /**
         * 某事项出证登记历史弹框，根据不同状态显示不同名称
         * @param isConsigner
         * @param receiveMode
         */
        showDifName: function (isConsigner, receiveMode) {
            var _this = this;
            if (isConsigner == '0') {
                _this.addresseeName = "领证人姓名：";
                _this.addresseePhone = "领证人电话：";
                _this.addresseeIdcard = "领证人身份证：";
                _this.addresseeP = "领证人区域：";
                _this.addresseeAddr = "领证人详细地址：";

            } else {
                _this.addresseeName = "委托人姓名：";
                _this.addresseePhone = "委托人电话：";
                _this.addresseeIdcard = "委托人身份证：";
                _this.addresseeP = "委托人区域：";
                _this.addresseeAddr = "委托人详细地址：";
            }

            if (receiveMode == '0') {
                _this.senderName = "寄件人姓名：";
                _this.senderPhone = "寄件人电话：";
                _this.senderAddr = "寄件人详细地址：";
            } else {
                _this.senderName = "出件人姓名：";
                _this.senderPhone = "出件人电话：";
                _this.senderAddr = "出件人详细地址：";
            }
        },
        //更新单个事项出证详情
        updateSendItemInfo: function () {
            var _this = this;
            var validateResult = true;
            _this.$refs['ItemSmsInfoForm'].validate(function (valid) {
                if (!valid) {
                    _this.$message({
                        message: '请输入完整信息！',
                        type: 'error'
                    });
                    validateResult = validateResult && false;
                    return false;
                }
            });

            if (_this.ItemSmsInfoForm.isConsigner == '1') {
                if (_this.uploadTableData.length == 0) {
                    this.$message({
                        message: '委托领取请上传委托书！',
                        type: 'error'
                    });
                    return;
                }

            }
            if (_this.uploadTableData.length > 0) {
                var fileIds = [];
                $.each(_this.uploadTableData, function (idx, obj) {
                    fileIds.push(obj.fileId);
                });
                _this.ItemSmsInfoForm.consignerAttId = fileIds.join(",");
            }
            //封装采集参数
            var param = _this.ItemSmsInfoForm;
            if(validateResult){
                request('', {
                    url: ctx + 'rest/certificate/updateSendItemInfo',
                    type: 'post',
                    data: JSON.stringify(param),
                    ContentType: "application/json"
                }, function (result) {
                    if (result.success) {
                        _this.$message({
                            message: '更新成功',
                            type: 'success'
                        });
                        _this.editItemSmsInfoModalShow = false;
                    }

                }, function (msg) {
                    _this.$message({
                        message: '服务请求失败',
                        type: 'error'
                    });
                });
            }



        },
        //===分割点

        // 重置表单
        resetForm: function (formName) {
            this.$refs[formName].resetFields();
        },

//获取已上传证照
        getCertListWin: function (instId, type) {
            var _that = this;

            request('', {
                url: ctx + 'rest/certificate/out/materials/attachments',
                type: 'get',
                data: {'instId': instId, "type": type}
            }, function (res) {
                if (res.success) {
                    if (res.content) {
                        _that.uploadCertData = res.content;
                    }
                } else {
                    _that.$message({
                        message: res.message ? res.message : '加载已上传材料列表失败',
                        type: 'error'
                    });
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },


        // 获取已上传文件列表
        getFileListWin: function (matid, rowData) {
            var _that = this;
            request('', {
                url: ctx + 'rest/certificate/consignerAtt/list',
                type: 'get',
                data: {'applyinstId': applyinstId}
            }, function (res) {
                if (res.success) {
                    if (res.content) {
                        _that.uploadTableData = res.content;
                        if (rowData) {
                            rowData.children = res.content;
                        }
                    }
                } else {
                    _that.$message({
                        message: res.message ? res.message : '加载已上传材料列表失败',
                        type: 'error'
                    });
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },

        // 预览电子件
        filePreview: function (data) {
            var _that = this;
            var detailId = data.fileId;
            var flashAttributes = '';
            // console.log(data);
           var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
           var fileName = data.fileName;
           var fileType = this.getFileType(fileName);
           _that.filePreviewCount++;
           if (fileType == 'pdf') {
               var tempwindow = window.open(); // 先打开页面
               setTimeout(function () {
                   tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
               }, 1000)
           } else if (regText.test(fileType)) {
               // previewPdf/pdfIsCoverted
               _that.loading = true;
               request('', {
                   url: ctx + 'previewPdf/pdfIsCoverted?detailId=' + detailId,
                   type: 'get',
               }, function (result) {
                   if (result.success) {
                       _that.loading = false;
                       var tempwindow = window.open(); // 先打开页面
                       setTimeout(function () {
                           tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
                       }, 1000)
                   } else {
                       if (_that.filePreviewCount > 9) {
                           confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                               _that.filePreviewCount = 0;
                               _that.filePreview(data);
                           }, function () {
                               _that.filePreviewCount = 0;
                               _that.loading = false;
                               return false;
                           }, '确定', '取消', 'warning', true)

                       } else {
                           setTimeout(function () {
                               _that.filePreview(data);
                           }, 1000)
                       }
                   }
               }, function (msg) {
                   _that.loading = false;
                   _that.$message({
                       message: '文件预览失败',
                       type: 'error'
                   });
               })
           } else {
               _that.loading = false;
               var tempwindow = window.open(); // 先打开页面
               setTimeout(function () {
                   tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
               }, 1000)
           }
            // url = ctx + 'rest/certificate/consignerAtt/preview' + "?detailId=" + detailId + "&flashAttributes=" + flashAttributes;
            /*url = ctx + 'rest/mats/att/preview?detailId='  + detailId + "&flashAttributes=" + flashAttributes;

            try {
                window.open(url);
            } catch (e) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            }*/
        },
        // 获取文件后缀
        getFileType: function (fileName) {
            var index1 = fileName.lastIndexOf(".");
            var index2 = fileName.length;
            var suffix = fileName.substring(index1 + 1, index2).toLowerCase();//后缀名
            if (suffix == 'docx') {
                suffix = 'doc';
            }
            return suffix;
        },
        // 勾选电子件
        selectionFileChange: function (val) {
            this.fileSelectionList = val;
        },
        //勾选证照
        selectionCertChange: function (val) {
            this.certSelectionList = val;
        },
        // 下载电子件
        downloadFile: function () {
            var _that = this;
            var detailIds = [];
            if (_that.fileSelectionList.length == 0) {
                _that.$message({
                    message: '请勾选数据后操作！',
                    type: 'error'
                });
                return false;
            }
            _that.fileSelectionList.map(function (item, index) {
                detailIds.push(item.matinstId);
            });
            detailIds = detailIds.join(',')
            window.location.href = ctx + 'rest/mats/att/download?detailIds=' + detailIds
        },

        // 上传电子件
        uploadFileCom: function (file) {
            var _that = this;
            var rowData = _that.selMatRowData;
            this.fileWinData = new FormData();
            Vue.set(file.file, 'fileName', file.file.name);
            this.fileWinData.append('file', file.file);
            this.fileWinData.append("applyinstId", applyinstId);

            _that.loadingFileWin = true;
            axios.post(ctx + 'rest/certificate/consignerAtt/upload', _that.fileWinData).then(function (res) {
                Vue.set(file.file, 'matinstId', res.data.content)
                // _that.sendInfoForm.consignerAttId = res.data.content;
                _that.getFileListWin(res.data.content, rowData);


                _that.loadingFileWin = false;
                _that.$message({
                    message: '上传成功',
                    type: 'success'
                });

            }).catch(function (error) {
                _that.loadingFileWin = false;
                if (error.response) {
                    _that.$message({
                        message: '文件上传失败(' + error.response.status + ')，' + error.response.data,
                        type: 'error'
                    });
                } else if (error.request) {
                    _that.$message({
                        message: '文件上传失败，服务器端无响应',
                        type: 'error'
                    });
                } else {
                    _that.$message({
                        message: '文件上传失败，请求封装失败',
                        type: 'error'
                    });
                }

            });
        },
        //删除证照
        delSelectCertCom: function () {

            var _that = this;
            var detailIds = [];
            if (_that.certSelectionList.length == 0) {
                _that.$message({
                    message: '请勾选数据后操作！',
                    type: 'error'
                });
                return false;
            }
            _that.certSelectionList.map(function (item, index) {
                detailIds.push(item.fileId);
            });
            detailIds = detailIds.join(',')
            request('', {
                url: ctx + 'rest/certificate/cert/delelte',
                type: 'post',
                data: {'detailIds': detailIds}
            }, function (res) {
                if (res.success) {
                    _that.getCertListWin(_that.tabActive, _that.editCertFormType);
                    _that.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                } else {
                    _that.$message({
                        message: res.message ? res.message : '删除失败',
                        type: 'error'
                    });
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },
        // 删除电子件
        delSelectFileCom: function () {
            var _that = this;
            var detailIds = [];
            if (_that.fileSelectionList.length == 0) {
                _that.$message({
                    message: '请勾选数据后操作！',
                    type: 'error'
                });
                return false;
            }
            _that.fileSelectionList.map(function (item, index) {
                detailIds.push(item.fileId);
            });
            detailIds = detailIds.join(',')
            request('', {
                url: ctx + 'rest/certificate/consigner/att/delelte',
                type: 'post',
                data: {'detailIds': detailIds, 'applyinstId': applyinstId}
            }, function (res) {
                if (res.success) {
                    _that.getFileListWin('', _that.selMatRowData);
                    _that.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                } else {
                    _that.$message({
                        message: res.message ? res.message : '删除失败',
                        type: 'error'
                    });
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },
        //打开拍照窗口
        openPhotoWindow: function (data, flag) {
            if (flag == 'lt') {
                lt_openPhotoWindow(data.matinstId, data.iteminstId, data.inoutId, callback)
            } else {
                fz_openPhotoWindow(null, null, null, uploadPhotoFile);
            }
        },


        selChildOrg: function (rowData) {
            this.selCoreItems.branchOrg = rowData.id;
            this.selCoreItems.orgName = rowData.name;
            this.orgTreeInfoModal = false;
        },
        // 刷新页面
        reloadPage: function () {
            if (this.submitCommentsType == 0 || this.submitCommentsType == 3) {
                window.location.reload()
            }
        },

        // 获取url中的查询参数
        getSerachParamsForUrl: function (name) {
            var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return unescape(r[2]);
            }
            return null;
        },
        // 关闭窗口-(这里使用的是父级window下面的vue实例中的方法来移除tab,这个tab是ele控件)
        closeWindowTab: function () {
            var ts = this;
            ts.smsSendEndRefresh();

            setTimeout(function () {
                window.parent.vm.removeTab(ts.getSerachParamsForUrl('applyinstId'));
            }, 500)
        },
        //// 如果是保存取件登记后，那就刷新对应的列表页
        smsSendEndRefresh: function () {
            debugger
            var _iframs = $(window.parent.document).find('iframe');
            var _panentIframsrc = this.getSerachParamsForUrl('parentIfreamUrl')
            var _tagIframe = '';
            for (var i = 0; i < _iframs.length; i++) {
                var _src = _iframs.eq(i).attr('src');
                if (_src && (_src == _panentIframsrc)) {
                    _tagIframe = _iframs.eq(i);
                    break;
                }
            }
            _tagIframe[0].contentWindow.location.reload(true);
        },
        //设置已取件页面
        setHadSendIndex: function () {
            var _that = this;
            _that.isSmsSend = '1';
            var _verticalTabData = [];
            _verticalTabData.push(_that.verticalTabData[0]);
            _that.verticalTabData = _verticalTabData;
        }
    },

});

