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
            isDisable:true,
            showSaveBtn: true,

            //证照
            activeNames: ['1', '2', '3', '4', '5'], // el-collapse 默认展开列表
            verticalTabData: [ // 左侧纵向导航数据
                {
                    label: '领件人信息',
                    target: 'receiver'
                }
            ],
            curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
            curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
            loading: false,
            ctx: ctx,
            fileList: [],
            activeTab: 0,  // 纵向导航active状态index
            showVerLen: 1, // 显示左侧纵向导航栏的长度
            //===
            specialForm: {
                specialId:'',
                iteminstCode: '1',
                iteminstName: '',
                specialDueWay: '',
                specialDueTime: '',
                customer: '',
                linkAddr: '',
                linkPhone: '',
                linkman: '',
                specialType: '',
                specialDueDays: '',
                specialReason: '',
                specialMemo: '',
                approveResult: '',
                approveUserName: '',
                approveTime: '',
                approveOpinion: '',
                chargeOrgName: '',
                chargeOrgId: '',
                windowUserName: '',
                windowUserId: '',
                windowPhone: '',
                opsUserName: '',
                opsUserId: '',
                opsTime: '',
                printUserName: '',
                printTime: '',
                isFlowTrigger:''
            },
            specialTypeList: [],
            specialFormRules: {//证照实例校验
                specialDueWay: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                specialDueTime: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                specialType: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                specialDueDays: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                specialReason: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ]/*,
                specialMemo:[
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ]*/,
                approveResult: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                approveUserName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                approveTime: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                approveOpinion: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                chargeOrgName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                windowUserName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                windowPhone: [
                    {required: true, validator: checkPhoneNum, trigger: 'blur'},
                ],
                opsUserName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                opsTime: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
            },
        }
    },
    mounted: function () {
        var _that = this;
        _that.initBasicInfo();
        _that.getSpecialTypeList();


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
        /** 填充特殊程序基本信息*/
        initBasicInfo: function () {
            var _this = this;
            request('', {
                url: ctx + 'rest/specialProcedure/getBasicInfo',
                type: 'get',
                data: {'applyinstId': applyinstId, 'iteminstId': iteminstId}
            }, function (result) {
                if (result.success) {
                    _this.specialForm =  result.content;

                    // if ("0" == isApprover) {//暂时无法判断是否窗口人员
                        _this.specialForm.windowUserName = currentUserName;
                        _this.specialForm.windowUserId = currentUserId;

                        _this.specialForm.printUserName = currentUserName;
                        _this.specialForm.printUserId = currentUserId;
                    // }

                    _this.specialForm.opsUserName = currentUserName;
                    _this.specialForm.opsUserId = currentUserId;
                    if(_this.specialForm.specialId =='' || _this.specialForm.specialId ==null){
                        _this.isDisable = false;
                    }

                }else{
                    alertMsg('', "服务请求失败！", '关闭', 'error', true);
                }

            }, function (msg) {
                /*_this.$message({
                    message: '服务请求失败',
                    type: 'error'
                });*/
                alertMsg('', "服务请求失败！", '关闭', 'error', true);
            });
        },
        /** 获取特殊程序类型list**/
        getSpecialTypeList: function () {
            var _this = this;
            request('', {
                url: ctx + 'rest/specialProcedure/getDicItemByType',
                type: 'get',
                data: {'codeType': "SPECIAL_TYPE"}
            }, function (result) {
                if (result.success) {
                    _this.specialTypeList = result.content;

                }

            }, function (msg) {
                /*_this.$message({
                    message: '服务请求失败',
                    type: 'error'
                });*/
                alertMsg('', "服务请求失败！", '关闭', 'error', true);
            });
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
        //保存特殊程序
        saveSpecial:function(){
            var _this = this;
            console.info(_this.specialForm);
            _this.specialForm.taskId = taskinstId;
            _this.specialForm.isFlowTrigger = '1';
            _this.specialForm.applyinstId = applyinstId;
            _this.specialForm.iteminstId = iteminstId;
            _this.specialForm.appinstId = processInstanceId;


            _this.$refs['specialInfoForm'].validate(function (valid) {
                if (valid) {
                    request('', {
                        url: ctx + "rest/specialProcedure/saveSpecial",
                        type: 'post',
                        data: _this.specialForm
                    }, function (result) {
                        if(result.success){
                            _this.$message({
                                message: '保存成功',
                                type: 'success'
                            });
                            setTimeout(function () {
                                window.opener=null;window.close();
                            },1000);
                        }
                    }, function (msg) {
                        _this.$message({
                            message: '保存失败！',
                            type: 'error'
                        });
                    });
                } else {
                    _this.$message({
                        message: '请输入完整信息！',
                        type: 'error'
                    });
                    return false;
                }
            });
        },
        //打印通知书
        doPrint:function(){

        },
        //结束特殊程序
        stopSpecial:function(){
            var _this = this;
            var obj = _this.specialForm;
          /*  console.info(window.location)
            window.parent.vm.removeTab(obj.iteminstId);
            _this.specialEndRefresh();
             return ;*/
            request('', {
                url: ctx + 'rest/specialProcedure/stopSpecial',
                type: 'post',
                data: obj
            }, function (result) {
                if (result.success) {
                    alertMsg('', "停止特殊程序成功！", '关闭', 'success', true);
                    setTimeout(function () {
                        window.parent.vm.removeTab(obj.iteminstId);
                        // window.opener=null;window.close();
                        _this.specialEndRefresh();
                    },2000);
                }

            }, function (msg) {
                /*_this.$message({
                    message: '服务请求失败',
                    type: 'error'
                });*/
                alertMsg('', "服务请求失败！", '关闭', 'error', true);
            });
        }
        ,
        // 如果是补正结束，那就刷新对应的列表页
        specialEndRefresh: function() {

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

        // 获取url中的查询参数
        getSerachParamsForUrl: function (name) {
            var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return unescape(r[2]);
            }
            return null;
        },
        //关闭当前窗口
        close:function(){
            window.opener=null;window.close();
        },
        dateFormat: function (dateStr) {
            debugger
            var t = new Date(dateStr);//row 表示一行数据, updateTime 表示要格式化的字段名称
            var month = t.getMonth() + 1 + "";
            var day = t.getDate() + "";
            var year =  t.getFullYear() +"";
            var date = year + '-' + month.length == 1 ? ('0' + month ): month + '-' + day.length == 1 ? ('0' + day) : day;
            return date;
        },
        // 重置表单
        resetForm: function (formName) {
            this.$refs[formName].resetFields();
            this.selectMat = '';
        },
        // 刷新页面
        reloadPage: function () {
            if (this.submitCommentsType == 0 || this.submitCommentsType == 3) {
                window.location.reload()
            }
        },
    },
});

