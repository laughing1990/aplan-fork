//选择办理人树
var zTreeSetting = {
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pId"
        }
    },
    check: {
        enable: true,
        chkboxType: {"Y": "", "N": ""},
        chkStyle: "checkbox",
        radioType: "all"   //对所有节点设置单选
    },
    //使用异步加载，必须设置 setting.async 中的各个属性
    async: {
        //设置 zTree 是否开启异步加载模式
        enable: true,
        autoParam: ["id", "dataType"],
        dataType: "json",
        type: "get"
        // url:ctx+"/front/task/getAssigneeRangeZTree.do?assigneeRangeKey=$ROLE.a73fc518-7ec6-4d74-8f06-4786b45f01c9,$POS.801dc783-2e5f-4cba-b4f1-c4f3d0094559,$POS.97f92189-6aad-4f5f-bd2f-569960bcc880," + assigneeRangeKey
        // url:ctx+"/front/task/getAssigneeRangeZTree.do?assigneeRangeKey=" + assigneeRangeKey + "&keyword=" + assigneeSearchKeyword
    },
    //4、zTree树加载完成后相关回调函数，
    // onAsyncSuccess 处理初始化默认选择办理人数据同步
    // onClick 和 onCheck 根据环节办理人个数要求 限制树的选择，并同时同步一选择办理人数据。
    callback: {
        onAsyncSuccess: function (e, treeId, treeNode) {
            if (vm.checkNotNull(vm.tempDefaultSelectedAssigneeId)) {
                if (vm.tempDefaultSelectedAssigneeId.indexOf(vm.ID_SPLIT) != -1) {
                    var texts = vm.nextTaskAssignee.split(vm.TEXT_SPLIT);
                    var ids = vm.tempDefaultSelectedAssigneeId.split(vm.ID_SPLIT);
                    for (var i = 0; i < ids.length; i++) {
                        vm.addAssignee(ids[i], texts[i]);
                        vm.toggleCheckAssignee(ids[i], true);
                    }
                } else {
                    vm.addAssignee(vm.tempDefaultSelectedAssigneeId, vm.nextTaskAssignee);
                    vm.toggleCheckAssignee(vm.tempDefaultSelectedAssigneeId, true);
                }
            }
        },
        onClick: function () {
            var nodes = vm.zTreeObj.getSelectedNodes();
            for (var i = 0; i < nodes.length; i++) {
                if (nodes[i].dataType != 'USER' || nodes[i].id == "userRoot") continue;
                if (nodes[i].checked) {
                    vm.zTreeObj.checkNode(nodes[i], false, false);
                    vm.uncheckRemoveAssignee(nodes[i].textValue);
                } else {
                    vm.zTreeObj.checkNode(nodes[i], true, false);
                    //改为直接在树上勾选时添加办理人
                    vm.addAssignee(nodes[i].textValue, nodes[i].name);
                    //根据节点配置，过滤单选
                    if (!vm.sendTaskInfo.multiTask) {
                        var nodes1 = vm.zTreeObj.getCheckedNodes(true);
                        for (var j = 0; j < nodes1.length; j++) {
                            if (nodes[i].id != nodes1[j].id) {
                                vm.zTreeObj.checkNode(nodes1[j], false, false);
                            }
                        }
                    }
                }
            }
        },
        onCheck: function (e, treeId, treeNode) {
            //根据节点配置，过滤单选
            if (!vm.sendTaskInfo.multiTask && treeNode.checked) {
                var nodes = vm.zTreeObj.getCheckedNodes(true);
                for (var i = 0; i < nodes.length; i++) {
                    if (treeNode.id != nodes[i].id) {
                        vm.zTreeObj.checkNode(nodes[i], false, false);
                    }
                }
            }
            //同步选择办理人
            if (treeNode.checked) {
                vm.addAssignee(treeNode.textValue, treeNode.name);
            } else {
                vm.uncheckRemoveAssignee(treeNode.textValue);
            }
            //暂时在check时去掉节点的选择状态
            var nodes = vm.zTreeObj.getSelectedNodes();
            for (var i = 0; i < nodes.length; i++) {
                vm.zTreeObj.cancelSelectedNode(nodes[i]);
            }
        }
    }
};
//Vue对象
var app = new Vue({
    el: '#app',
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
            iteminstId: '', // 事项实例id，从baseInfo.js获取后保存，如果是审批人员，查询到的是审批人员对应的单个事项；窗口人员；并联会有多个
            processViewLoading: false,
            processStatesLoading: false,
            formTabsContentLoading: true,
            winHeight: document.documentElement.clientHeight - 120,
            isSeriesinst: 0, // 判斷是否是串联还是并联  1为是串   1: 表示事项审批; 0: 表示阶段审批
            isApprover: 0,//  0: 窗口人员; 1: 审批人员
            //审批界面初始化参数
            appId: null,//业务流程模板id，如果存在则表示起草界面，还不是审批界面
            appComment: null,//业务流程模板名称，表示流程的执行的内容
            processInstanceId: null,//流程实例id
            isSuspended: null,//流程是否挂起 1挂起0没挂起
            flowdefKey: null,//流程定义key（流程编号）
            taskId: null,//任务实例id
            taskName: null,//任务实例名称
            viewId: null,//视图id，从哪个视图进入审批界面
            busRecordId: null,//业务数据id，单项时为iteminstId，并联==undefined
            masterEntityKey: null,//流程关联的主业务表数据id---applyinstId 申请实例ID
            appFlowdefId: null,//业务流程模板和流程关联id
            flowModel: null,//模板元素权限配置模式
            currTaskDefId: null,//当前任务节点定义id（节点编号）
            currProcDefVersion: 0,//当前流程实例版本号
            isCheck: null,//是否流程查看，即已办、办结的标志
            formJson: null,//流程关联表单信息json格式
            formData: null,//流程关联表单信息数组格式
            initError: null,//审批界面初始化标识
            _tableName: null,//附件管理中附件上传参数之一，默认等于AEA_HI_APPLYINST
            _pkName: null,//附件管理中附件上传参数之一，默认等于APPLYINST_ID
            recordId: null,//附件管理中附件上传参数之一，默认等于masterEntityKey
            recordIds: null,//附件管理中附件查询参数，默认等于流程所有任务实例id用逗号拼接成的字符串
            attLink: {
                tableName: 'AEA_HI_TASK',
                pkName: 'ID_',
                recordId: null,
            },//attLink用于附件关联多个业务ID
            btnDisable: true,//附件管理的按钮是否可点击，如果还没启动流程，处于起草页面则不能点击

            //审批界面业务逻辑参数
            buttonData: [], // 动态按钮数据
            showMenuMore: false,
            menuCount: 1,
            buttonDataShow: [],
            buttonDataHide: [],
            hasPrivShowButton: [],//存储显示的按钮
            mainFormIframe: "busFormIframe",
            busFormIframeArray: [],//表单iframe集合
            requestErrorText: "请求服务器失败，请检查链接有效性！",
            sendTaskInfos: [],//当前环节发送相关信息
            sendTaskInfo: null,//当前环节发送相关信息
            nextTaskAssignee: null,//下一环节办理人
            nextTaskAssigneeId: null,//下一环节办理人id（登录名）
            nextTask: null,//下一环节名称
            sendParam: null,//发送参数
            sendDialog: false,//发送对话框
            sendForm: {},//发送表单参数
            sendFormRule: {},//发送表单校验规则

//      *********** 意见弹框参数 start *********************
            opinionForm: {},//办理意见表单参数
            opinionFormRule: {
                opinionText: [
                    {required: true, message: '请填写意见内容', trigger: 'blur'}
                ]
            },//办理意见表单校验规则
            manageOpinionDialog: false,//管理常用办理意见对话框
            opinionTableData: [],//常用办理意见表格数据
            editOpinionDialog: false,//编辑意见对话框
            editOpinionForm: {},//编辑意见form
            editOpinionFormRule: {
                opinionContent: [
                    {required: true, message: '请填写审批意见', trigger: 'blur'}
                ]
            },//编辑意见校验规则
            //选择办理人树
            submitCommentsShow: false,//更改状态用弹框控制
            submitCommentsFlag1: false, // 展示常用意见管理框
            submitCommentsFlag2: false, // 展示编辑常用意见框
            commentTitle: "特别程序",//更改状态是标题
// *********** 意见弹框参数 start *********************

            selectAssigneeDialog: false,//选择办理人对话框
            treeLeftHeight: document.documentElement.clientHeight - 150,
            defaultProps: {
                label: 'name',
                isLeaf: 'leaf'
            },
            treeShowNode: '',
            assigneeRangeKey: null,//下一环节办理人范围
            assigneeSearchKeyword: '',//查询
            treeData: [],//树数据
            zTreeObj: null,//树对象
            tempDefaultSelectedAssigneeId: null,
            ID_SPLIT: ",",
            TEXT_SPLIT: "、",
            selectedAssignee: [],
            isNeedSelectAssignee: false,//是否需要选择办理人
            processDiagramDialog: false,//流程跟踪对话框
            processStatesDialog: false, //查看情形对话框
            statesData: [], // 查看情形数据
            typeAstateData: {}, // 申报方式和状态
            rightTabActive: 1, // 1为流程视图  2为对话框视图
            rightVerticalTabActive: 1, // 1为办理意见  2为材料附件
            rightIconTab: true, // true 为展开宽度25%  false为 展开宽度50%
            saveAndSendTitle: '发送对话框',

            receiptPrintDialog: false,//回执弹框
            receiptPrintLoading: false,
            receiptTable: [],//回执列表

            enumActionCode: '',//OpsActionConstants 对应的枚举字段
            enumApplyStatus: '',//ApplyState 对应的枚举值 ---需要改变的申请状态
            enumItemStatus: '',//ItemStatus 对应的枚举值 ---事项状态

            sendUrlPath: '',//发送地址
            requestMappingType: 'put',//访问方式
//--------------------新增证照--------------------

//------------------证照--------------------------------------------
            certinstModalShow: false,//证照实例列表弹窗控制
            selectCertinstList: [],//选择的证照实例行数
            editCertinstModel: false,//证照编辑/新增
            editCertModalTitle: '新增',
            uploadCertData: [],//已上传文件列表
            showUpdateBtn: true,
            ctx: ctx,
            fileList: [],
            certinstList: [],//证照实例表
            editCertForm: {},
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
            certList: [],//证照定义列表
            uploadTableData: [],
            fileSelectionList: [], // 所选电子件
            selMatRowData: {}, // 所选择的材料信息
            fileWinData: [], // 上传窗口上传参数列表
            loadingFileWin: false, // 窗口文件上传loading

            // 材料补正按钮相关
            // 是否展示材料补正的dialog
            isShowMaterialSupplementDialog: false,
            //材料补全dialog
            isShowMatmend:false,
            // 材料补正dialog的loading
            sloading: false,
            // 接口获取到的补正的数据
            supplementDialogData: {},
            // 接口获取到的补正的数据全的数据
            matMendDialogData: {},
            // 材料不全form
            matMendForm:{
              applyinstCode: "",
              approveOrgName: "",
              iteminstName: "",
              localCode: "",
              owner: "",
              projInfoName: "",
              correctDueDays: '',
            },
            // 材料补正-form
            supplemetForm: {
              applyinstCode: "",
              approveOrgName: "",
              iteminstName: "",
              localCode: "",
              owner: "",
              projInfoName: "",
              correctDueDays: '',
            },
            // 材料补正-form校验
            supplementFormRules: {
              correctDueDays: [{required: true, trigger: blur, message: '请输入补正办理时限'}]
            },
            // 是否在选择材料内
            inSeletcedMaterialPandel: false,
            // 未选择前的待选材料列表
            tobeSelectMaterialList: [],
            // 选择后的材料列表
            tobaMakeupMaterialList: [],
            // 未选择前的待选材料列表-补全
            tobeSelectMaterialList2:[],
            // 选择后的材料列表-补全
            tobaMakeupMaterialList2:[],
            unitAndProjInfo: {}


        }
    },

    created: function () {
        this.initCss();
        this.initPage();
        this.getLackMatsMatmend();
        window.vm = this;
    },
    mounted: function () {
        var vm = this;
        window.addEventListener("resize", function () {
            vm.setButtonShow();
        });
    },
    methods: {
        //获取url带的参数
        getUrlParam: function (paramName) {
            var paramValue = "", isFound = !1;
            if (window.location.search.indexOf("?") == 0 && window.location.search.indexOf("=") > 1) {
                var arrSource = unescape(window.location.search).substring(1, window.location.search.length).split("&"),
                    i = 0;
                while (i < arrSource.length && !isFound) arrSource[i].indexOf("=") > 0 && arrSource[i].split("=")[0].toLowerCase() == paramName.toLowerCase() && (paramValue = arrSource[i].split("=")[1], isFound = !0), i++
            }
            return paramValue == "" && (paramValue = null), paramValue
        },
        //检查字符串为空
        checkNull: function (str) {
            if (!str || (typeof str == 'string' && !str.replace(/(^\s*)|(\s*$)/g, '')) || 'null' == str)
                return true;
            return false;
        },
        checkNotNull: function (str) {
            return !this.checkNull(str);
        },
        dealNull: function (str) {
            if (this.checkNull(str))
                return "";
            return str;
        },
        //界面初始化接口
        //初始化界面，需要获取初始化参数，并根据参数选择初始化方式
        initPage: function () {
            var vm = this;
            try {
                vm.taskId = vm.getUrlParam("taskId");//html请求时获取参数的格式
                vm.busRecordId = vm.getUrlParam("busRecordId");//单项申报时才有值，并联申报==undefined
                if (!vm.busRecordId || vm.busRecordId == 'undefined') {//并联
                    vm.isSeriesinst = '0';
                } else {
                    vm.isSeriesinst = '1';//并联
                }
            } catch (e) {
                vm.taskId = taskId;
            }
            //如果当前不存在任务实例id，则表示流程还没发起，则转到流程起草界面
            if (vm.checkNull(vm.taskId)) {
                // vm.appId = vm.getUrlParam("appId");
                vm.appId = appId;
                if (vm.checkNull(vm.appId)) {
                    vm.$message.error("获取不到业务流程模板id，无法初始化起草界面！");
                    return;
                } else {
                    //初始化流程起草界面
                    vm.initProcessDraftPage(vm.appId);
                    //修改标题
                    document.title = "流程起草界面";
                }
            } else {
                vm.btnDisable = false;
                vm.attLink.recordId = vm.taskId;
                vm.viewId = vm.getUrlParam("viewId");
                vm.initProcessTurningPage(vm.taskId);
            }
        },
        //初始化流程起草界面
        initProcessDraftPage: function (appId) {
            var vm = this
            request('bpmFrontUrl', {
                url: ctx + 'rest/front/process/getProcessDraftInitParams/' + appId,
                type: 'get'
            }, function (res) {
                if (res.success) {
                    //获取到初始化参数
                    vm.flowdefKey = res.content.flowdefKey;
                    vm.appFlowdefId = res.content.appFlowdefId;
                    vm.formJson = res.content.formJson;
                    vm.flowModel = res.content.flowModel;
                    vm.recordIds = res.content.recordIds;
                    vm.appComment = res.content.appComment;
                    //标题
                    $("#currentProcessApproveName").text(vm.appComment);
                    //做其他初始化操作
                    vm.formData = JSON.parse(vm.formJson);
                    //获取页面元素（工具栏按钮）权限信息
                    vm.initFormElementPriv();
                    //初始化附件管理页面
                    vm.initAttachment();
                } else {
                    vm.$message.error(res.message);
                }
            }, function () {
                vm.$message.error(vm.requestErrorText);
            });
        },
        //初始化流程审批流转界面
        initProcessTurningPage: function (taskId) {
            var vm = this
            request('bpmFrontUrl', {
                url: ctx + 'rest/front/process/getProcessTurningParams/' + taskId+'?isNotCompareAssignee=false',
                type: 'get'
            }, function (res) {
                if (res.success) {
                    console.log("getProcessTurningParams:");
                    console.log(res.content)
                    //获取初始化参数
                    vm.taskName = res.content.taskName;
                    vm.appFlowdefId = res.content.appFlowdefId;
                    vm.formJson = res.content.formJson;
                    vm.flowModel = res.content.flowModel;
                    vm.appComment = res.content.appComment;
                    vm.processInstanceId = res.content.processInstanceId;
                    vm.currTaskDefId = res.content.currTaskDefId;
                    vm.currProcDefVersion = res.content.currProcDefVersion;
                    vm.isSuspended = res.content.isSuspended;//挂起流程
                    vm.masterEntityKey = res.content.masterEntityKey;//applyinstId
                    vm.isCheck = res.content.isCheck;
                    //附件管理相关参数
                    vm._tableName = res.content.masterEntityKey;
                    vm._pkName = res.content.masterEntityKey;
                    vm.recordId = vm.taskId;
                    vm.recordIds = res.content.recordIds;// 附件关联id
                    vm.attLink.recordId = vm.taskId;
                    vm.getWayAType();
                    //做其他初始化操作
                    vm.formData = JSON.parse(vm.formJson);
                    //console.log(vm.formData);
                    //console.log(JSON.stringify(vm.formData))
                    //获取页面元素（工具栏按钮）权限信息
                    vm.initFormElementPriv();
                    //获取审批历史意见信息
                    vm.initHistoryComment();
                    //初始化附件管理页面
                    vm.initAttachment();
                    //标题
                    $("#currentProcessApproveName").text(vm.appComment);
                } else {
                    vm.$message.error(res.message);
                }
            }, function () {
                vm.$message.error(vm.requestErrorText);
            });
        },
        //初始化表单
        initForms: function () {
            var vm = this;
            var formDatas = vm.formData;
            var $formTab = $("#form-tabsId");
            var $formTabContent = $("#form-tabs-content");
            $formTab.empty();
            $formTabContent.empty();
            var index = 1;
            var masterFormName = "主表单"
            var disabledSubFormTab = "disabled";
            if (vm.checkNotNull(vm.masterEntityKey)) {
                disabledSubFormTab = "";
            }actId
            for (var i in formDatas) {
                var form = formDatas[i];
                if (form.isMasterForm == "1") {
                    var formUrl = vm.initFormUrl(form.formLoadUrl);
                    $formTab.append("<li class='nav-item m-tabs__item'>" +
                        "<a class='nav-link m-tabs__link active' data-toggle='tab' href='#m_tabs_6_" + index + "' role='tab'>" +
                        form.formName +
                        "</a>" +
                        "</li>");
                    $formTabContent.append('<div class="tab-pane active" style="height: 100%;" id="m_tabs_6_' + index + '" role="tabpanel">' +
                        '<iframe id="' + vm.mainFormIframe + '" name="' + vm.mainFormIframe + '" src="' + formUrl
                        + '" scrolling="no" style="width: 100%;height: 100%;" frameborder="0"/>' +
                        '</div>');
                    //同时设置表单权限，表单权限已由工作流标签控制，js控制已弃用
                    // setFormEditPower(vm.mainFormIframe,form.formCode,form.formField);
                    //只设置权限信息，用于校验保存的表单值
                    // setFormFieldPrivData(vm.mainFormIframe,form.formField);
                    index++;
                    masterFormName = form.formName;
                }
            }
            for (var i in formDatas) {
                var form = formDatas[i];
                var formUrl = vm.initFormUrl(form.formLoadUrl);
                if (form.isMasterForm == "0") {
                    $formTab.append("<li class='nav-item m-tabs__item'>" +
                        "<a class='nav-link m-tabs__link " + disabledSubFormTab + "' data-toggle='tab' href='#m_tabs_6_" + index + "' role='tab'>" +
                        form.formName +
                        "</a>" +
                        "</li>");
                    var frameName = "busFormIframe_" + index;
                    $formTabContent.append('<div class="tab-pane" id="m_tabs_6_' + index + '" style="height: 100%;" role="tabpanel">' +
                        '<iframe id="' + frameName + '"  name="' + frameName + '" src="' + formUrl + '" scrolling="no" style="width: 100%;height: 100%;" frameborder="0"/>' +
                        '</div>');
                    // setFormEditPower(frameName,form.formCode,form.formField);
                    // setFormFieldPrivData(frameName,form.formField);
                    index++;
                    vm.busFormIframeArray.push(frameName);
                }
            }
            //添加从表单tab的点击事件，确保主表单保存成功后，才能进行从表单填写
            $(".second .nav-item").click(function () {
                var index = $(this).index()
                if ($(this).hasClass("disabled")) {
                    var currentFormName = $(this).text();
                    vm.$message.error("请先暂存" + masterFormName + "信息！再填写" + currentFormName + "信息！");
                    $(this).css("outline", "none");
                    return;
                } else {
                    $(this).children().addClass("active").parent().siblings().children().removeClass("active");

                    $("#form-tabs-content .tab-pane").eq(index).addClass("active").siblings().removeClass("active");
                }
            })
        },
        //初始化表单url，待优化
        initFormUrl: function (url) {
            if (url.indexOf("http://") == 0 || url.indexOf("https://") == 0 || url.indexOf("file:///") == 0) {
                return url + '?appFlowdefId=' + vm.appFlowdefId + "&currTaskDefId=" + vm.currTaskDefId + "&currProcDefVersion=" + vm.currProcDefVersion + "&viewId=" + vm.viewId + "&isSeriesinst=" + vm.isSeriesinst + "&isApprover=" + vm.isApprover;
            } else {
                var temp = ctx;
                temp = temp.substring(0, temp.length - 1);
                return temp + url + '?appFlowdefId=' + vm.appFlowdefId + "&currTaskDefId=" + vm.currTaskDefId + "&currProcDefVersion=" + vm.currProcDefVersion + "&viewId=" + vm.viewId + "&isSeriesinst=" + vm.isSeriesinst + "&isApprover=" + vm.isApprover;
            }
        },
//******************** 初始化工具栏按钮 *************************************
        initButtons: function () {
            var vm = this;
            var defaultBtn = {
                elementName: "查看流程图",
                elementCode: "wfBusSave",
                columnType: "button",
                isReadonly: '0',
                isHidden: '0',
                elementRender: '&nbsp;&nbsp;<button class="btn btn-outline-info" onclick="showDiagramDialog()">查看流程图</button> '
            };
            var testBtn = {
                elementName: "测试弹窗",
                elementCode: "wfBusSave",
                columnType: "button",
                isReadonly: '0',
                isHidden: '0',
                // elementRender: '&nbsp;&nbsp;<button class="btn btn-outline-info" onclick="makeCertification()">制证</button> '
                elementRender: '&nbsp;&nbsp;<button class="btn btn-outline-info" onclick="getPrintList()">回执</button> '
            };


            vm.buttonData.push(defaultBtn);
            // vm.buttonData.push(testBtn);

            var buttonDatas = vm.buttonData;
            if (vm.checkNull(vm.taskId)) {//如果是起草界面则暂时去掉流程跟踪按钮
                buttonDatas = [buttonDatas[0]];
            }
            var newButtonData = [];
            for (var i in buttonDatas) {
                var button = buttonDatas[i];
                if (button.isHidden == 0) {
                    //替换 id为 当前页面的任务id 变量值
                    var realText = button.elementRender.replace("#[id]", vm.taskId);
                    if (button.isReadonly == 1) {
                        //如果当前按钮别锁定，即不可点击，则设置为disabled 且去掉 onclick事件，避免被前端修改。
                        var id = "button_" + vm.taskId + i;
                        if (realText.indexOf("onclick") != -1) {
                            realText = realText.replace("onclick", " id='" + id + "' disabled='disabled' title = '已被锁定' data-clc");
                        } else {
                            realText = realText.replace("onClick", " id='" + id + "' disabled='disabled' title = '已被锁定' data-clc");
                        }
                        button.elementRender = realText;
                        $("#" + id).removeAttr("data-clc");
                    } else {
                        button.elementRender = realText;
                    }
                    newButtonData.push(button);
                }
            }
            vm.buttonData = newButtonData;
            // 控制动态按钮，如果超过一定的宽度，给出省略号
            vm.setButtonShow();
        },

        setButtonShow: function () {
            var vm = this;
            var winWidth = document.documentElement.clientWidth;
            var buttonRightWidth = $("#applyStatus").outerWidth();
            var butttonLeftWidth = winWidth - buttonRightWidth;
            if (vm.buttonData !== null && vm.buttonData.length > 0) {
                if (butttonLeftWidth <= 40 && vm.buttonData.length > 0) {
                    vm.showMenuMore = true;
                    vm.menuCount = -1;
                } else if (butttonLeftWidth <= 80 && butttonLeftWidth > 40 && vm.buttonData.length > 1) {
                    vm.showMenuMore = true;
                    vm.menuCount = 0;
                } else if (butttonLeftWidth <= 150 && butttonLeftWidth > 80 && vm.buttonData.length > 2) {
                    vm.showMenuMore = true;
                    vm.menuCount = 1;
                } else if (butttonLeftWidth <= 360 && butttonLeftWidth > 150 && vm.buttonData.length > 3) {
                    vm.showMenuMore = true;
                    vm.menuCount = 2;
                } else if (butttonLeftWidth <= 395 && butttonLeftWidth > 360 && vm.buttonData.length > 4) {
                    vm.showMenuMore = true;
                    vm.menuCount = 3;
                } else if (butttonLeftWidth <= 504 && butttonLeftWidth > 395 && vm.buttonData.length > 5) {
                    vm.showMenuMore = true;
                    vm.menuCount = 4;
                } else if (butttonLeftWidth <= 527 && butttonLeftWidth > 504 && vm.buttonData.length > 6) {
                    vm.showMenuMore = true;
                    vm.menuCount = 5;
                } else if (butttonLeftWidth <= 680 && butttonLeftWidth > 527 && vm.buttonData.length > 7) {
                    vm.showMenuMore = true;
                    vm.menuCount = 6;
                } else if (butttonLeftWidth <= 800 && butttonLeftWidth > 680 && vm.buttonData.length > 8) {
                    vm.showMenuMore = true;
                    vm.menuCount = 7;
                } else {
                    vm.showMenuMore = false;
                    vm.menuCount = vm.buttonData.length;
                }
            } else {
                vm.showMenuMore = false;
                if (vm.buttonData) {
                    vm.menuCount = vm.buttonData.length;
                }
            }
            vm.initButtonDataCount(vm.buttonData, vm.menuCount)
        },
        initButtonDataCount: function (eleLi, n) {  // 重置按钮数据
            if (eleLi) {
                this.buttonDataShow = eleLi.slice(0, n + 1);
                this.buttonDataHide = eleLi.slice(n + 1);
            }
        },
        //根据初始化参数，获取页面元素权限信息
        initFormElementPriv: function () {
            var vm = this;
            if (vm.checkNotNull(vm.currTaskDefId) && vm.currProcDefVersion > 0) {
                request('bpmFrontUrl', {
                    url: ctx + 'rest/front/task/getAuthorizePublicElement',
                    type: 'get',
                    data: {
                        appFlowdefId: vm.appFlowdefId,
                        privMode: "act",
                        actId: vm.currTaskDefId,
                        viewId: vm.viewId,
                        version: vm.currProcDefVersion
                    },
                }, function (res) {
                    if (res.success) {
                        var result = res.content;
                        if (vm.checkNull(result)) {
                            return;
                        }
                        //1、判断是否获取到权限信息
                        if (result.noDefaultDealView) {
                            vm.$message.info("保存成功！因当前系统没有配置流程待办视图，请到待办工作中继续办理！");
                            return;
                        }
                        if (result.publicEleList && result.publicEleList.length > 0) {
                            //这里可以做交集，确保起草时有暂存按钮，审批时 有流程跟踪按钮
                            vm.buttonData = result.publicEleList;
                            vm.filterButtonArray(vm.buttonData)
                            vm.sortButtonArray(vm.buttonData);
                        }
                        vm.initButtons();
                        for (var i in result) {
                            if (i != "publicEleList") {
                                for (var j in formDatas) {
                                    if (i == formDatas[j].formId) {
                                        formDatas[j].formField = privInfo[i];
                                        break;
                                    }
                                }
                            }
                        }
                        vm.initForms();
                    } else {
                        vm.$message.error(res.message);
                    }
                }, function () {
                    vm.$message.error(" 没有获取工具栏按钮权限数据");
                });
            } else {
                vm.initForms();
                vm.initButtons();
            }
        },
        //工具栏按钮排序
        sortButtonArray: function (arr) {
            var compare = function (obj1, obj2) {
                var val1 = obj1.sortNo;
                var val2 = obj2.sortNo;
                if (val1 && val2) {
                    if (val1 < val2) {
                        return -1;
                    } else if (val1 > val2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
            arr.sort(compare);
        },
        //工具栏按钮过滤
        filterButtonArray: function (arr) {
            for (var i = 0; i < arr.length; i++) {
                if (arr[i].isHidden == "0") {
                    vm.hasPrivShowButton.push(arr[i]);
                }
                //如果是挂起，则把挂起按钮隐藏，否则把激活按钮隐藏
                if (vm.isSuspended == '1') {
                    if (arr[i].elementCode == "suspendProcess") {
                        arr[i].isHidden = '1';
                    }
                } else {
                    if (arr[i].elementCode == "activateProcess") {
                        arr[i].isHidden = '1';
                    }
                }
            }
        },
        //初始化右边意见列表
        initHistoryComment: function () {
            var vm = this;
            $("#processView").empty();
            vm.processViewLoading = true;
            $("#processAssignComment_dialog").empty();
            request('bpmFrontUrl', {
                    url: ctx + 'rest/front/process/getHistoryCommentTree/' + vm.processInstanceId,
                    type: 'get',
                }, function (result) {
                    vm.processViewLoading = false;
                    if (!result.success) {
                        vm.$message.info("获取不到历史审批意见！");
                        return;
                    }
                    result = result.content;
                    if (result.length > 0) {
                        //拼接意见树HTML
                        var text = vm.concatCommentTreeText(result, 1);
                        $("#processView").append(text);
                        //拼接意见对话框HTML
                        var text2 = vm.concatCommentDialogText(result, 1);
                        $("#processAssignComment_dialog").append(text2);

                        //控制最后一个节点显示的方法体
                        function showAndHide() {
                            var _this = $(this);
                            var ele = _this.next('.m-accordion__item-body.collapse').find('.m-accordion__item');
                            var elelen = ele.length;
                            if ((!(_this.hasClass('collapsed')) && elelen > 0)) {
                                for (var i = 0; i < elelen - 1; i++) {
                                    $(ele[i]).addClass('hide');
                                }
                                _this.next('.m-accordion__item-body').addClass('show-b');
                            } else {
                                ele.removeClass('hide');
                                _this.next('.m-accordion__item-body').removeClass('show-b');
                            }
                        }

                        //初始化显示部门审批的最后一个节点
                        if ($(".accordion-purple") && $(".accordion-purple").length > 0) {
                            //三级流程则针对二级进行收缩
                            $(".accordion-blue").find(".m-accordion__item-head:first").click(showAndHide);
                            setTimeout(function () {
                                $(".accordion-blue").find(".m-accordion__item-head:first").click();

                            }, 200);
                        } else {
                            //二级流程则针对一级进行收缩
                            setTimeout(function () {
                                $(".m-accordion__item").each(function () {
                                    if ($(this).find(".accordion-blue").length > 0) {
                                        $(this).find(".m-accordion__item-head:first").click(showAndHide);
                                        $(this).find(".m-accordion__item-head:first").click();
                                    }
                                })
                            }, 200);
                        }
                    } else {
                        $("#processView").html("暂无办理意见！");
                    }
                }, function () {
                    vm.processViewLoading = false;
                }
            );
        },
        //拼接历史意见树
        concatCommentTreeText: function (result, level) {
            var text = "";
            var headColor = "";
            var isCollapsed = false;
            if (level == 2) {
                headColor = " accordion-blue";
            }
            if (level == 3) {
                headColor = " accordion-purple";
            }
            if (result && result.length > 0) {
                var total = [];
                var temp = [];
                var lastNode = "";
                for (var j = 0; j < result.length; j++) {
                    var currentNode = result[j].nodeName;
                    if (currentNode != lastNode) {
                        temp = [];
                        temp.push(result[j]);
                        total.push(temp);
                        lastNode = currentNode;
                        continue;
                    } else {
                        temp.push(result[j]);
                        continue;
                    }
                }
                //倒序显示意见信息
                //total.reverse();
                //组装html
                for (var i = 0; i < total.length; i++) {
                    var total2 = total[i];
                    var color = "green";
                    var isDealing = false;
                    if (total2[0].dealingTask) {
                        color = "blue";
                        isDealing = true;
                    }
                    var firstNodeName = total2[0].nodeName;//用于判断是否同一节点多工作项
                    var nodeShowName = total2[0].nodeName;//用于显示节点标题
                    if (nodeShowName.length > 30) {
                        nodeShowName = nodeShowName.substr(0, 30) + "...";
                    }
                    for (var k = 0; k < total2.length; k++) {
                        var timeStamp = i + k + agcloud.bpm.utils.uuidGenerator();
                        if (k == 0 || (k > 0 && total2[k].nodeName != firstNodeName)) {
                            var org = '';
                            if (vm.checkNull(total2[0].firstOrgShortName)) {
                                if (vm.checkNull(total2[0].orgName))
                                    org = nodeShowName;
                                else
                                    org = total2[0].orgName;
                            } else {
                                org = total2[0].firstOrgShortName;
                            }
                            var orgTitle = vm.checkNull(total2[0].orgName) ? total2[0].nodeName : total2[0].orgName;

                            text += "<div class=\"m-accordion__item " + headColor + "\">";
                            if (isCollapsed) {
                                text += "   <div class=\"m-accordion__item-head collapsed\" role=\"tab\" id=\"processView_item_" + timeStamp + "_head\" data-toggle=\"collapse\" href=\"#processView_item_" + timeStamp + "_body\" aria-expanded=\"false\">";
                            } else {
                                text += "   <div class=\"m-accordion__item-head\" role=\"tab\" id=\"processView_item_" + timeStamp + "_head\" data-toggle=\"collapse\" href=\"#processView_item_" + timeStamp + "_body\" aria-expanded=\"true\">";
                            }
                            text += "      <span class=\"m-accordion__item-title\" title=\"" + orgTitle + "\">";
                            text += "           <div class=\"m-accordion-logo\">";
                            text += org;
                            if (!total2[k].dealingTask) {
                                text += "                <i class=\"el-icon-success\"></i>";
                            }
                            text += "           </div>";
                            text += "            <span class=\"ellipsis\">" + nodeShowName + "</span>";
                            text += "      </span>";
                            text += "      <span class=\"el-icon-arrow-up el-icon\"></span>";
                            text += "   </div>";
                            if (isCollapsed) {
                                text += "   <div class=\"m-accordion__item-body collapse\" id=\"processView_item_" + timeStamp + "_body\" role=\"tabpanel\" aria-labelledby=\"processView_item_" + timeStamp + "_head\" data-parent=\"\">";
                            } else {
                                text += "   <div class=\"m-accordion__item-body collapse show\" id=\"processView_item_" + timeStamp + "_body\" role=\"tabpanel\" aria-labelledby=\"processView_item_" + timeStamp + "_head\" data-parent=\"\">";
                            }
                            text += "   <div class=\"m-accordion__item-content\">";
                        }

                        if (total2[k].childNode) {
                            //有子节点的进行递归
                            text += '<div class="m-accordion m-accordion--toggle-arrow" id="processView1" role="tablist">';
                            text += this.concatCommentTreeText(total2[k].childNode, level + 1);
                            text += '</div>'
                        } else {
                            //显示节点办理信息，要判断办理状态
                            if (total2[k].dealingTask) {
                                text += "<p class=\"idea-title\">" + (total2[k].commentMessage == null ? "暂无办理意见" : total2[k].commentMessage) + "</p>";
                            } else {
                                text += "<p class=\"idea-title el-icon-success\">" + (total2[k].commentMessage == null ? "暂无办理意见" : total2[k].commentMessage) + "</p>";
                            }
                            if (total2[k].attDetailNum && total2[k].attDetailNum > 0) {
                                text += "<span class=\"approval-file\" onclick=\"vm.queryDetailUnion('" + total2[k].taskId + "')\"><i class=\"flaticon-attachment\"></i>" + total2[k].attDetailNum + "</span>";
                            }
                            text += "<p class=\"approver-info clearfix\"><span class=\"approver\"><i class=\"el-icon-user\"></i>&nbsp;" + total2[k].taskAssignee + "</span> <span class=\"approver-time\">" + agcloud.bpm.utils.dateTimeNoSecendFormat(total2[k].sigeInDate) + "</span></p>";
                        }
                        //针对多工作项的判断
                        if (total2.length > k + 1 && total2[k].nodeName == total2[k + 1].nodeName) continue;

                        text += '</div></div>'
                        text += "</div>"
                    }
                }
            }
            return text;
        },
        //拼接历史意见对话列表
        concatCommentDialogText: function (data, level) {
            var text = '';
            if (data && data.length > 0) {
                var commentMessage = '', isblue = '', firstOrgShortName = '';
                for (var i in data) {
                    var node = data[i];
                    if (!node.taskAssignee) {
                        if (data[i].childNode) {
                            text += this.concatCommentDialogText(data[i].childNode, level + 1);
                        }
                        continue;
                    }
                    if (vm.checkNull(node.firstOrgShortName)) {
                        if (vm.checkNull(node.orgName))
                            firstOrgShortName = nodeShowName;
                        else
                            firstOrgShortName = node.orgName;
                    } else {
                        firstOrgShortName = node.firstOrgShortName;
                    }
                    if (node.dealingTask) {
                        isblue = ' message-logo-blue';
                    }
                    if (node.commentMessage) {
                        commentMessage = '<span class="pro-name">' + node.commentMessage + '</span>';
                    } else {
                        commentMessage = '<span class="pro-name" style="color:gray">无办理意见</span>';
                    }
                    text += '<div class="m-messenger__wrapper">\n' +
                        '                    <div class="message-username ' + (!node.isApprover ? "" : "fr") + '">\n' +
                        agcloud.bpm.utils.dateTimeNoSecendFormat(node.sigeInDate) +
                        '                    </div>\n' +
                        '                    <div class="m-messenger__message m-messenger__message--' + (!node.isApprover ? "in" : "out") + '">\n' +
                        (node.isApprover ? '' : '                      <div class="message-logo ' + isblue + '" title="' + node.orgName + '  ' + node.taskAssignee + '">\n' +
                            firstOrgShortName + '                      </div>\n') +
                        '                      <div class="m-messenger__message-body">\n' +
                        '                        <div class="m-messenger__message-arrow"></div>\n' +
                        '                        <div class="m-messenger__message-content">\n' +
                        '                          <div class="m-messenger__message-text">\n' +
                        commentMessage +
                        this.showTaskDetailNum(node.attDetailNum, node.taskId) +
                        '                             <div class="message-time ar d-flex justify-content-between">\n' +
                        '                            </div>\n' +
                        '                          </div>\n' +
                        '                        </div>\n' +
                        '                      </div>\n' +
                        (!node.isApprover ? '' : '<div class="message-logo ' + isblue + '" title="' + node.orgName + '  ' + node.taskAssignee + '">\n' +
                            firstOrgShortName +
                            '                      </div>\n') +
                        '                    </div>\n' +
                        '                  </div>\n';
                    if (data[i].childNode) {
                        text += this.concatCommentDialogText(data[i].childNode, level + 1);
                    }
                }
            }
            return text;
        },
        //显示节点上传的附件个数，并可以链接到附件管理中查看
        showTaskDetailNum: function (num, taskId) {
            if (num) {
                return '<p class="pro-file"><i class="flaticon-tool-1" style="cursor:pointer" onclick="vm.queryDetailUnion(\'' + taskId + '\')"></i>' + num + '</p>';
            } else {
                return '';
            }
        },
        //查询节点上传的附件
        queryDetailUnion: function (currTaskId) {
            //切换意见列表到材料附件
            $("#attachFileClick a").click();
            //查询出当前节点关联的附件,doQueryDetailUnion接口必须在附件管理中实现
            var attachWindow = $("#attachContentFrame")[0].contentWindow;
            if (attachWindow && attachWindow.doQueryDetailUnion) {
                var queryParams = {
                    tableName: "AEA_HI_TASK",
                    pkName: "ID_",
                    recordIds: currTaskId
                };
                attachWindow.doQueryDetailUnion(queryParams);
            }
        },
        //初始化附件管理
        initAttachment: function () {
            //var src = ctx + "rest/entrance/official/attFile";
            var src = ctx + "rest/approve/official/attFile";
            $("#attachContentFrame").attr("src", src);
            $("#attachContentFrame").height('60rem');
        },
        //初始化样式，必须由js修改的
        initCss: function () {
            var interval = setInterval(function () {
                if ($(".el-header").length > 0) {
                    $(".el-header").height(45);
                    $(".el-header").css("line-height", "45px");
                    window.clearInterval(interval);
                }
            }, 100);
        },
//******************************************** 流程操作接口 ***************************************
        //暂存按钮功能，保存表单，启动流程
        wfBusSave: function () {
            vm.loading = true;
            //保存当前 正在编辑 的 表单
            var curIframe = $("#form-tabs-content").find(".tab-pane.active").find("iframe")[0];
            //如果是主表单
            if (curIframe && curIframe.name == vm.mainFormIframe) {
                //调用子窗口中的工作流保存方法，会保存表单数据同时启动一个流程，并返回表单主键和流程相关变量信息
                // checkFormSubmitPower(vm.mainFormIframe);//这个 要和setFormFieldPrivData一起使用，但是有格式问题，暂时弃用，
                // 2018-08-10 为兼容柔性工作流，加入flowModel参数 proc 表示BPMM标准工作流，case 表示平台自定义柔性工作流，cmmn 表示cmmn工作流
                var content = null;
                if (busFormIframe.window.wfBusSave) {
                    content = busFormIframe.window.wfBusSave(vm.flowdefKey, vm.taskId, vm.masterEntityKey, vm.appFlowdefId, vm.processInstanceId, vm.flowModel);
                }
                vm.loading = false;
                //如果返回content不为空，则流程启动成功；如果为空则是流程已经启动过了，当前只是表单的更新操作
                if (vm.checkNotNull(content)) {
                    //如果流程启动失败，给出失败提示信息
                    if (!content.success) {
                        vm.$message.error(content.failMessage);
                        return;
                    }
                    //获取流程启动后的相关信息
                    vm.masterEntityKey = content.masterEntityKey;    //主表单主键
                    vm.flowdefKey = content.flowdefKey;              //流程定义key
                    vm.processInstanceId = content.processInstanceId;//流程实例id
                    vm.taskId = content.taskId;                      //当前任务id
                    vm.taskName = content.taskName;                  //当前任务名称
                    vm.currTaskDefId = content.currTaskDefId;        //当前任务定义id
                    vm.currProcDefVersion = content.currProcDefVersion;//当前流程定义版本
                    vm.flowModel = content.flowModel;                  //当前流程类型
                    vm.viewId = content.viewId;// 流程待办视图id
                    //附件管理相关参数
                    vm._tableName = content.masterEntityKey;
                    vm._pkName = content.masterEntityKey;
                    vm.recordId = content.taskId;
                    vm.recordIds = content.recordIds;// 附件关联id
                    vm.attLink.recordId = vm.taskId;
                    vm.btnDisable = false;
                    //初始化附件管理界面
                    vm.initAttachment();

                    vm.$message.success("暂存成功，流程发起成功！");
                    //如果 流程启动后，当前节点不是当前登录人处理节点，给出提示，关闭窗口。所有工作流类型都一样。
                    if (!content.currUserTask) {
                        var message = "流程启动成功！已发送至 <span style='color:#22D479;font-size:18px' >&nbsp;" + vm.taskName + "</span>&nbsp;环节";
                        if (vm.checkNotNull(content.nextTaskAssigneeName)) {
                            message += "，下一环节审批人为：<span style='color:#22D479;font-size:18px' >&nbsp;" + content.nextTaskAssigneeName + "&nbsp;。</span>";
                        }
                        vm.showSuccessTip(message);
                        return;
                    }
                    //如果当前节点 是 当前登录人 处理节点，则回显标题
                    // $("#currentTask").text("当前环节：" + vm.taskName);
                    if (vm.checkNotNull(vm.viewId)) {
                        var newUrl = window.location.href.substring(0, window.location.href.indexOf("?") + 1) + "taskId=" + vm.taskId + "&viewId=" + vm.viewId;
                        window.location.href = newUrl;
                    } else {
                        //刷新权限信息，会重新生成主从表单，如果无法获取流程待办视图的权限配置信息，则给出提示，关闭窗口。
                        vm.initFormElementPriv();
                    }
                } else {
                    //不会重新生成表单
                    vm.loading = false;
                    vm.$message.success("暂存成功！");
                }
            } else {
                //如果主表单id不为空，则可以开始保存当前 正在编辑 的从表单，当前加上从表单的保存方法是 wfSave
                if (vm.checkNotNull(vm.masterEntityKey)) {
                    if (document.getElementById(curIframe.id).contentWindow.wfSave) {
                        // checkFormSubmitPower(curIframe.name);
                        document.getElementById(curIframe.id).contentWindow.wfSave(vm.masterEntityKey);
                        // refreshForm(curIframe.name);
                    }
                }
                vm.$message.success("暂存成功！");
            }
        },

        //下一环节选择事件，需要切换展示信息
        selectNextNode: function (destActId) {
            if (destActId) {
                var sendTaskInfos = vm.sendTaskInfos;
                for (var i = 0; i < sendTaskInfos.length; i++) {
                    var currSendTask = sendTaskInfos[i];
                    var currDestActId = currSendTask.destActId;
                    if (currDestActId == destActId) {
                        vm.sendTaskInfo = sendTaskInfos[i];
                        vm.nextTaskAssignee = vm.sendTaskInfo.defaultSendAssignees;
                        vm.nextTaskAssigneeId = vm.sendTaskInfo.defaultSendAssigneesId;
                        vm.nextTask = vm.sendTaskInfo.destActName;
                        vm.isNeedSelectAssignee = vm.sendTaskInfo.needSelectAssignee;
                        vm.$set(vm.sendForm, 'nextTaskAssignee', vm.nextTaskAssignee);
                        //流程流转的提交参数
                        vm.sendParam = {
                            taskId: vm.taskId,
                            sendConfigs: [{
                                isUserTask: vm.sendTaskInfo.userTask,
                                isEnableMultiTask: vm.sendTaskInfo.multiTask,
                                assignees: vm.nextTaskAssigneeId,
                                destActId: vm.sendTaskInfo.destActId
                            }]
                        };
                        //初始化下一环节审批人和提示信息
                        vm.initNextAssigneeAndTip(vm.nextTaskAssignee, vm.sendTaskInfo.needSelectAssignee, vm.sendTaskInfo.message);
                    }
                }
            }
        },

        //添加当前节点审批意见
        saveTaskComment: function (message, isShowTip) {
            var d = {};
            d["message"] = message;
            d["taskId"] = vm.taskId;
            d["processInstanceId"] = vm.processInstanceId;
            request('bpmFrontUrl', {
                url: ctx + 'rest/front/task/addTaskComment',
                type: 'post',
                data: d
            }, function (result) {
                if (result.success) {
                    if (isShowTip) {
                        vm.$message.success("保存审批意见成功！");
                        vm.initHistoryComment();//刷新右侧意见列表
                    }
                } else {
                    if (isShowTip) {
                        vm.$message.error("保存审批意见失败！");
                    }
                }
            }, function () {
                vm.$message.error(vm.requestErrorText);
            });
        },

        handleSelectionChange: function () {

        },
        //启用/禁用短信框
        showMasses: function () {
            var vm = this;
            //获取短信的基础数据
            $.ajax({
                url: ctx + '/aea/approve/getSMSInfo.do',
                type: 'POST',
                async: true,
                data: {"taskId": vm.taskId, "applyinstId": vm.masterEntityKey},
                success: function (data) {
                    if (data.success) {
                        var projName = data.content.projName;
                        var content = data.content.smsInfo;
                        if (content) {
                            var type = content.type;
                            var currNode = content.currNode;
                            var applyType = '';
                            var commentEnd = '';
                            if (type && type.indexOf("串联") > -1) {
                                applyType = '事项';
                            } else if (type && type.indexOf("并联") > -1) {
                                applyType = '阶段';
                            }
                            if (currNode && currNode.indexOf("fazheng") > -1) {
                                commentEnd = '，已经出证办结，请携带《材料收取通知书》和身份证到大厅领取相关材料、证件（如果没有提交相关材料，请忽略）';
                            } else if (currNode && currNode.indexOf("banjie") > -1) {
                                commentEnd = '，已经退件，请携带《材料收取通知书》和身份证到大厅领取相关材料、证件（如果没有提交相关材料，请忽略）';
                            }
                            var msg = '尊敬的' + content.linkmanName + '先生/女士: 您好! 你所申报的' + applyType + ' 【' + content.iteminstStageinstName + '】' +
                                '申报流水号为：' + content.applyinstCode + commentEnd + ', 申报项目为：' + projName + '';
                            vm.sendForm.contentDesc = msg;
                            vm.sendForm.phoneNo = content.linkmanMobilePhone;
                        }
                    }
                },
                error: function () {
                    vm.$message.error('获取短信基础数据失败!')

                }
            });
        },


        //选择办理人树相关
        getAssigneeTree: function () {
            //1、记录临时变量
            vm.tempDefaultSelectedAssigneeId = vm.nextTaskAssigneeId;
            var tempDestActId = vm.sendTaskInfo.destActId;
            //2、清空已选的办理人列表，查询框
            $("#selectedAssignees").empty();
            vm.selectedAssignee = [];
            vm.assigneeSearchKeyword = '';
            //获取办理人范围key
            request('bpmFrontUrl', {
                    url: ctx + 'rest/front/task/getNextTaskAssigneeRange',
                    type: 'get',
                    ContentType: 'application/json',
                    data: {taskId: vm.taskId, destActId: vm.sendTaskInfo.destActId}
                }, function (result) {
                    if (result.success) {
                        vm.assigneeRangeKey = result.content.assigneeRange;
                        if (vm.checkNull(vm.assigneeRangeKey)) return;
                        zTreeSetting.async.url = ctx + "rest/front/task/getAssigneeRangeZTree?assigneeRangeKey=" + vm.assigneeRangeKey + "&procInstId=" + vm.processInstanceId + "&destActId=" + tempDestActId;
                        vm.zTreeObj = jQuery.fn.zTree.init(jQuery("#assigneeOrgTree"), zTreeSetting);
                    } else {
                        vm.$message.error(result.message);
                    }
                }, function () {
                    vm.$message.error("获取不到下个环节办理人范围！");
                }
            );
        },
        //确认选择
        confirmSelectAssignee: function () {
            var selectedAssignee = vm.selectedAssignee;
            //点击确定按钮，拼接参数回填到上一级窗口的表单上，如果选择为空，则还是使用默认办理人
            if (selectedAssignee.length > 0) {
                var assignee = "";
                var assigneeCn = "";
                for (var i in selectedAssignee) {
                    assignee += selectedAssignee[i].id + vm.ID_SPLIT;
                    assigneeCn += selectedAssignee[i].text + vm.TEXT_SPLIT;//中文名用顿号
                }
                vm.nextTaskAssigneeId = assignee.substring(0, assignee.length - 1);
                vm.nextTaskAssignee = assigneeCn.substring(0, assigneeCn.length - 1);
                vm.sendTaskInfo.defaultSendAssignees = vm.nextTaskAssignee;
                vm.sendTaskInfo.defaultSendAssigneesId = vm.nextTaskAssigneeId;
            } else {
                vm.nextTaskAssignee = vm.sendTaskInfo.defaultSendAssignees;
                vm.nextTaskAssigneeId = vm.sendTaskInfo.defaultSendAssigneesId;
            }
            //设置到提交参数中
            vm.sendParam.sendConfigs[0].assignees = vm.nextTaskAssigneeId;
            //回显到发送信息框中
            vm.$set(vm.sendForm, 'nextTaskAssignee', vm.nextTaskAssignee);
            //关闭选择框
            vm.selectAssigneeDialog = false;
        },
        //组织机构树查询
        assigneeSearch: function () {
            if (vm.checkNotNull(vm.assigneeSearchKeyword)) {
                vm.assigneeSearchKeyword = vm.assigneeSearchKeyword.trim();
            } else {
                vm.assigneeSearchKeyword = '';
            }
            var url = zTreeSetting.async.url;
            if (url.indexOf("keyword=") == -1) {
                zTreeSetting.async.url = url + "&keyword=" + encodeURI(vm.assigneeSearchKeyword);
            } else {
                zTreeSetting.async.url = url.substring(0, url.lastIndexOf("keyword=")) + "keyword=" + vm.assigneeSearchKeyword;
            }
            zTreeObj = jQuery.fn.zTree.init(jQuery("#assigneeOrgTree"), zTreeSetting);
        },
        //双击删除已选参与人
        removeAssignee: function (event) {
            var currentTarget = event.currentTarget;
            var id = $(currentTarget).attr("data-id").trim();
            var index = vm.arrayIndexOf(vm.selectedAssignee, id);
            if (index != -1) {
                //从数组的index脚标开始，删除一个元素
                vm.selectedAssignee.splice(index, 1);
                //删除html节点
                $(currentTarget).remove();
                //同步去掉zTree上的勾
                vm.toggleCheckAssignee(id, false);
                //多余一步，为了兼容zTree的异步加载完成操作同步默认办理人
                if (id == vm.tempDefaultSelectedAssigneeId)
                    vm.tempDefaultSelectedAssigneeId = null;
            }
        },
        //添加参与人，会根据当前可选办理人个数，进行控制。
        addAssignee: function (id, text) {
            //判断是否已经选过了
            var index = vm.arrayIndexOf(vm.selectedAssignee, id);
            if (index == -1) {
                //如果不是多工作项的话，就只能选择一个审批人
                if (!vm.sendTaskInfo.multiTask) {
                    $("#selectedAssignees").empty();
                    vm.selectedAssignee = [];
                }
                var str = '<a id="selected_assignee_' + id + '" data-id="' + id + '" href="#" class="m-list-search__result-item" style="display: block;border-bottom: 1px solid #ebedf2;" title="双击可移除">' +
                    '<span class="m-list-search__result-item-icon">' +
                    '<i class="fa fa-user-md" style="font-size: 18px;padding: 0px 5px;"></i>' +
                    '</span>' +
                    '<span class="m-list-search__result-item-text">' + text + '</span>' +
                    '</a>';
                $("#selectedAssignees").append(str);
                $("#selectedAssignees").find("a").dblclick(vm.removeAssignee);
                vm.selectedAssignee.push({id: id, text: text});
            }
        },
        //zTree树上勾掉时，移除办理人
        uncheckRemoveAssignee: function (id) {
            var index = vm.arrayIndexOf(vm.selectedAssignee, id);
            if (index != -1) {
                //从数组的index脚标开始，删除一个元素
                vm.selectedAssignee.splice(index, 1);
                $("#selected_assignee_" + id).remove();
                //多余一步，为了兼容zTree的异步加载完成操作同步默认办理人
                if (id == vm.tempDefaultSelectedAssigneeId)
                    vm.tempDefaultSelectedAssigneeId = null;
            }
        },
        //zTree的勾选办理人方法
        toggleCheckAssignee: function (id, flag) {
            if (vm.zTreeObj) {
                var nodes = vm.zTreeObj.getNodesByParam("textValue", id, null);
                if (nodes && nodes.length > 0) {
                    for (var i = 0; i < nodes.length; i++) {
                        vm.zTreeObj.checkNode(nodes[i], flag, false);
                    }
                }
            }
        },
        //判断数组中是否存在value数据，-1 没有，否则有
        arrayIndexOf: function (arr, value) {
            var index = -1;
            for (var i = 0; i < arr.length; i++) {
                if (value == arr[i].id) {
                    index = i;
                    break;
                }
            }
            return index;
        },
        //********************************** 查看流程图相关接口 *************************************

        checkParentProExist: function (procInstId) {
            request('bpmFrontUrl', {
                url: ctx + 'rest/front/process/getParentProcessInstanceId/' + procInstId,
                type: 'get'
            }, function (result) {
                if (result.success) {
                    $('#parentPro').show();
                    $('#parentPro').empty();
                    var btn = '<button class="btn btn-default" onclick="vm.getParentProcess(' + result.content.processInstanceId + ',' + result.content.isCheck + ')">返回上级流程</button>';
                    $('#parentPro').append(btn);
                } else {
                    $('#parentPro').hide();
                }
            }, function () {
            });
        },
        //显示父流程流程图
        getParentProcess: function (procInstId, isCheck) {
            $('#bpmnModel').html('');//先清空流程图容器div
            _showParentProcessDiagram(procInstId, isCheck);
        },

        //流程挂起、激活相关接口
        //根据code获取流程挂起/激活按钮
        getActiveOrSuspendButton: function (elementCode) {
            for (var i = 0; i < vm.hasPrivShowButton.length; i++) {
                if (vm.hasPrivShowButton[i].elementCode == elementCode) {
                    return vm.hasPrivShowButton[i];
                }
            }
            return null;
        },

        //部门审批通过或不通过流程
        isApproval: function (flag) {
            if (vm.isSuspended == '1') {
                // agcloud.ui.metronic.showTip('当前流程已被挂起，无法继续办理！', "info", 2000, false);
                alertMsg('', '当前流程已被挂起，无法继续办理！');
                return;
            }
            if (flag == '1') {
                $("#m_modal_saveAndSend_title").text("审批通过对话框");
                vm.saveAndSendTitle = "审批通过对话框"
                //wfBusSendDialog(null,null,null,"8");
                vm.wfBusSend()
            } else {
                $("#m_modal_saveAndSend_title").text("审批不通过对话框");
                vm.saveAndSendTitle = "审批不通过对话框"
                //wfBusSendDialog(null,null,null,"9");
                vm.wfBusSend()
            }
        },
        //发送对话框
        wfBusSendDialog: function (taskSendConfigUrl, destActId, destTaskAssigneeIsFromItem, state) {
            var tempUrl = ctx + '/front/task/getTaskSendConfig.do';
            if (taskSendConfigUrl) {
                tempUrl = taskSendConfigUrl;
            }
            if (checkNotNull(vm.taskId)) {
                $.ajax({
                    url: tempUrl,
                    data: {
                        taskId: vm.taskId,
                        applyinstId: masterEntityKey,
                        destActId: destActId,
                        destTaskAssigneeIsFromItem: destTaskAssigneeIsFromItem
                    },
                    type: "post",
                    dataType: "json",
                    success: function (result) {
                        if (result.success) {
                            //1、获取事项列表
                            // getApproveItemList();
                            // if(approveItemList == null) return false;

                            //2、获取下一环节信息
                            sendTaskInfos = result.content;
                            sendTaskInfo = sendTaskInfos[0];//默认节点信息是 第一个节点的
                            nextTask = sendTaskInfo.destActName;
                            nextTaskAssignee = sendTaskInfo.defaultSendAssignees;
                            nextTaskAssigneeId = sendTaskInfo.defaultSendAssigneesId;
                            //4、流程流转的提交参数
                            sendParam = {
                                taskId: taskId,
                                sendConfigs: [{
                                    isUserTask: sendTaskInfo.userTask,
                                    isEnableMultiTask: sendTaskInfo.multiTask,
                                    assignees: nextTaskAssigneeId,
                                    destActId: sendTaskInfo.destActId
                                }]
                            };
                            if (sendTaskInfo.directSend) {
                                if (state == "bumenshenpi") {
                                    doConfirm(state);
                                } else if (state == "cailiaobuquan") {
                                    doConfirm(state);
                                } else if (state == "yushenshoujian") {
                                    doConfirm(state);
                                } else {
                                    doSendOperation(sendTaskInfo.directSend, state);
                                }
                            } else {
                                //不是定向发送 则弹出下一环节信息框，显示环节信息和办理人信息，包括环节选择和办理人选择。
                                var options = {
                                    width: 800,
                                    height: 600,
                                    notCloseModal: true,
                                    confirm: function () {
                                        //发送窗口的发送按钮事件，发送前校验发送条件是否满足,2018-12-5 改为如果原本有默认办理人，而用户又不选择的话，则给出提示。
                                        if (sendTaskInfo.userTask && !nextTaskAssigneeId && sendTaskInfo.defaultSendAssigneesId) {
                                            agcloud.ui.metronic.showErrorTip('您选择的下一环节需要选择办理人，没有办理人无法发送！');
                                            return;
                                        }
                                        if (state == "bumenshenpi") {
                                            doConfirm(state);
                                        } else if (state == "cailiaobuquan") {
                                            doConfirm(state);
                                        } else if (state == "yushenshoujian") {
                                            doConfirm(state);
                                        } else {
                                            doSendOperation(sendTaskInfo.directSend, state);
                                        }
                                    },
                                    callback: function () {
                                        //初始化下一环节信息，这里后面可能有多个环节，要根据选择的环节获取该环节的处理人，再供用户选择。
                                        // 如果多个环节时要绑定单选按钮事件，更新选择的环节到nextTask变量中
                                        $("#nextTaskList").empty();
                                        var nextTaskInfo = '';
                                        for (var i = 0; i < sendTaskInfos.length; i++) {
                                            var temp = sendTaskInfos[i];
                                            var checked = '';
                                            if (i == 0) {
                                                checked = '" checked="checked';
                                            }
                                            nextTaskInfo += '<label class="m-radio m-radio--solid m-radio--brand" style="margin-right: 40px">' +
                                                '<input type="radio" onclick="selectNextNode(\'' + temp.destActId + '\')" name="nextTask" value="' + temp.destActId + checked + '">' +
                                                '<span></span>' +
                                                '<div style="display: inline;font-size: 16px;height: 21px;line-height: 20px;">' + dealNull(temp.destActName) + '</div>' +
                                                '</label>';
                                        }

                                        $("#nextTaskList").append(nextTaskInfo);
                                        //下一环节参与人信息
                                        if (checkNotNull(nextTaskAssignee)) {
                                            $("#nextTaskAssigneeTipPanel").show();
                                            $("#nextAssignee").val(nextTaskAssignee);
                                        } else {
                                            if (!sendTaskInfo.userTask && sendTaskInfo.destActId && sendTaskInfo.destActId.indexOf("endEvent") != -1) {
                                                $("#nextTaskAssigneeTipPanel").hide();
                                            } else {
                                                $("#nextTaskAssigneeTipPanel").show();
                                                $("#nextAssignee").val(nextTask + "审批人");
                                            }
                                        }
                                        //备注说明信息
                                        var commentTip = "提示：";
                                        if (sendTaskInfo.message) {
                                            commentTip += sendTaskInfo.message;
                                        }
                                        if (sendTaskInfo.needSelectAssignee) {
                                            commentTip += "然后请点击“发送”按钮发送到所选环节";
                                        } else {
                                            commentTip += "请点击“发送”按钮发送到所选环节";
                                        }
                                        $("#commentTip").text(commentTip);
                                        //选择按钮启用/禁用
                                        if (!sendTaskInfo.needSelectAssignee) {
                                            $("#selectAsingee").hide();
                                        }
                                    }
                                }
                                $("#nextTaskNextHandle").show();
                                $("#approver").show();
                                if (state == "bumenshenpi") {
                                    $("#approver").hide();
                                } else if (state == "cailiaobuquan") {
                                    $("#nextTaskNextHandle").hide();
                                    $("#approver").hide();
                                } else if (state == "yushenshoujian") {
                                    $("#nextTaskNextHandle").hide();
                                    $("#approver").hide();
                                }
                                showModal("m_modal_saveAndSend", options);
                            }
                        } else {
                            //agcloud.ui.metronic.showErrorTip('保存并发送失败!' + result.content);
                            alertMsg('', '保存并发送失败!' + result.content);
                        }
                    },
                    error: function () {
                        //agcloud.ui.metronic.showErrorTip('保存并发送失败!');
                        alertMsg('', '保存并发送失败!');
                    }
                });
            } else {
                // agcloud.ui.metronic.showErrorTip("请先点击暂存按钮，保存表单信息！");
                alertMsg('', '请先点击暂存按钮，保存表单信息！');
            }
        },


//*************************************** 4.0 接口方法 ********************************---start
        //************** 意见框    ----意见下拉选择事件
        //打印回执
        printReceive: function (row) {
            console.log(row);
            var url = ctx + 'rest/receive/toPrintPage/' + row.receiveId;
            setTimeout(function () {
                window.open(ctx + 'rest/ntko/ntkoOpenWin?jumpUrl=' + encodeURIComponent(url));
            }, 500);
        },
        opinionSelectChange: function (value) {
            vm.$set(vm.opinionForm, 'opinionText', value);
        },
        //设为常用办理意见
        setCommonOpinion: function () {
            if (vm.checkNull(vm.opinionForm.opinionText)) {
                vm.$message.info('办理意见不能为空，请填写！');
                return;
            }
            request('bpmFrontUrl', {
                url: ctx + 'rest/comment/saveUserOpinion',
                type: 'post',
                data: {message: vm.opinionForm.opinionText}
            }, function (result) {
                if (result.success) {
                    vm.$message.success(result.message);
                    //刷新列表
                    vm.getOpinionList();
                } else {
                    vm.$message.error(result.message);
                }
            }, function () {
                vm.$message.error('设为常用办理意见失败！');
            });
        },
        //获取审批意见列表
        getOpinionList: function () {
            request('bpmFrontUrl', {
                url: ctx + 'rest/comment/getAllActiveUserOpinions',
                type: 'get',
                async: false,
            }, function (result) {
                if (result.success) {
                    vm.opinionTableData = result.content;
                }
            }, function () {
                vm.$message.error('获取常用办理意见失败！');
            });
        },
        //编辑意见-改变流程和状态弹窗用
        editOpinion: function (row) {
            vm.editOpinionForm.opinionId = row.opinionId;
            //显示出的值必须用这种方式 赋值，如果用等于号赋值则会导致校验不了
            vm.$set(vm.editOpinionForm, 'opinionContent', row.opinionContent);
            vm.editOpinionDialog = true;
        },
        //编辑意见，只改变状态弹窗用
        getUserOptionById: function (row) {
            var _that = this;
            vm.editOpinionForm.opinionId = row.opinionId;
            vm.$set(vm.editOpinionForm, 'opinionContent', row.opinionContent);
            vm.$set(vm.editOpinionForm, 'sortNo', row.sortNo);
            _that.submitCommentsFlag2 = true;
        },
        //保存编辑意见
        doSaveOpinion: function () {
            var _that = this;
            vm.$refs['editOpinionForm'].validate(function (valid) {
                if (valid) {
                    request('bpmFrontUrl', {
                        url: ctx + 'rest/comment/editUserOpinion',
                        type: 'put',
                        data: _that.vm.editOpinionForm
                    }, function (result) {
                        if (result.success) {
                            vm.editOpinionDialog = false;
                            vm.getOpinionList();
                            vm.$message.success(result.message);
                        } else {
                            vm.$message.error(result.message);
                        }
                    }, function () {
                        vm.$message.error('编辑办理意见失败！');
                    });
                }
            });
        },

        deleteOpinion: function (id) {
            if (vm.checkNull(id)) {
                vm.$message.error('删除的意见参数不能为空！');
                return;
            }
            confirmMsg("删除意见", "确定要删除该意见吗？", function () {
                request('bpmFrontUrl', {
                    url: ctx + 'rest/comment/deleteUserOpinion/' + id,
                    type: 'delete'
                }, function (result) {
                    if (result.success) {
                        vm.getOpinionList();
                        vm.$message.success('删除办理意见成功！');
                    } else {
                        vm.$message.error('删除办理意见失败！');
                        console.log(result.message)
                    }
                }, function () {
                    console.log('删除办理意见失败！');
                });
            });
        },

//4.0 ************************************************* 意见框方法 *************---end

        //*************************查看情形对话框 ***************
        checkStates: function () {
            var vm = this
            vm.processStatesDialog = true
            vm.processStatesLoading = true
            var applyinstId = vm.masterEntityKey
            var statesUrl;
            if (vm.isSeriesinst == '1') {
                statesUrl = ctx + 'rest/approve/series/selected/states?applyinstId=' + applyinstId // 查看串联申报已选择情形
            } else {
                statesUrl = ctx + 'rest/approve/parallel/selected/states?applyinstId=' + applyinstId // 查看并联申报已选择情形
            }
            request('bpmFrontUrl', {
                url: statesUrl,
                type: 'get'
            }, function (res) {
                vm.processStatesLoading = false;
                if (res.success) {
                    vm.statesData = res.content
                    //console.log(vm.statesData)
                } else {
                    console.log(res.msg)
                }
            }, function () {
                vm.processStatesLoading = false
            });
        },
        /**
         *审批详情页 --> 获取申报方式和状态
         */
        getWayAType: function () {
            var vm = this
            request('bpmFrontUrl', {
                url: ctx + 'rest/approve/type/and/state',
                type: 'get',
                data: {
                    applyinstId: vm.masterEntityKey, // 申请实例Id
                    taskId: vm.taskId,
                },
            }, function (res) {
                //console.log("获取申报方式和状态", res)
                if (res.success) {
                    vm.typeAstateData = res.content
                    vm.isSeriesinst = vm.typeAstateData.isSeriesinst //传参用来判断是否串并联
                    vm.isApprover = vm.typeAstateData.isApprover
                    document.title = res.content.stageOrItemName
                } else {
                    console.log(res.message)
                }
            }, function () {
            });
        },

        //********************** 流程图相关 **********************************
        //显示当前流程的流程图
        showDiagramDialog: function () {
            var vm = this;
            $('#bpmnModel').html('');//先清空流程图容器div
            _showProcessDiagram(vm.processInstanceId, vm.isCheck);
            vm.processDiagramDialog = true;
        },
        //显示子流程流程图
        showChildrenDiagramDialog: function (childProcInstId, isCheck) {
            $('#bpmnModel').html('');//先清空流程图容器div
            _showChildrenProcessDiagram(childProcInstId, isCheck);
        },
//==========================================================工程建设 相关审批按钮 start ========


        //办理按钮功能，弹窗发送选择框----窗口办理/事项办理--只推动流程流转---使用原来的
        wfBusSend: function () {
            if (vm.checkNotNull(vm.taskId)) {
                request('bpmFrontUrl', {
                    url: ctx + 'rest/front/task/getTaskSendConfig/' + vm.taskId,
                    type: 'get'
                }, function (result) {
                    if (result.success) {
                        //获取下一环节信息
                        vm.sendTaskInfos = result.content;
                        vm.sendTaskInfo = vm.sendTaskInfos[0];//默认节点信息是 第一个节点的
                        vm.nextTask = vm.sendTaskInfo.destActName;
                        vm.nextTaskAssignee = vm.sendTaskInfo.defaultSendAssignees;
                        vm.nextTaskAssigneeId = vm.sendTaskInfo.defaultSendAssigneesId;
                        vm.isNeedSelectAssignee = vm.sendTaskInfo.needSelectAssignee;
                        //流程流转的提交参数
                        vm.sendParam = {
                            taskId: vm.taskId,
                            sendConfigs: [{
                                isUserTask: vm.sendTaskInfo.userTask,
                                isEnableMultiTask: vm.sendTaskInfo.multiTask,
                                assignees: vm.nextTaskAssigneeId,
                                destActId: vm.sendTaskInfo.destActId
                            }]
                        };
                        if (vm.sendTaskInfo.directSend) {
                            vm.doSendOperation(vm.sendTaskInfo.directSend);
                        } else {
                            vm.showSendDialog(vm.sendTaskInfos);
                        }
                    } else {
                        vm.$message.error('获取下一节点信息失败!' + result.content);
                    }
                }, function () {
                    vm.$message.error('保存并发送失败!');
                });
            } else {
                vm.$message.error("请先点击暂存按钮，保存表单信息！");
            }
        },
        //流程发送
        doSendOperation: function (directSend) {
            if (!directSend) {
                if (vm.sendTaskInfo.userTask && vm.checkNull(vm.nextTaskAssigneeId)) {
                    vm.$message.error('您选择的下一环节需要选择办理人，没有办理人无法发送！');
                    return;
                }
                if (vm.checkNull(vm.opinionForm.opinionText)) {
                    vm.$message.error('请填写办理意见！');
                    return;
                } else {
                    vm.saveTaskComment(vm.opinionForm.opinionText, false);
                }
            }
            if (vm.checkNotNull(vm.masterEntityKey) && vm.checkNotNull(vm.taskId)) {
                if (busFormIframe.window.wfBusSave) {
                    busFormIframe.window.wfBusSave(vm.flowdefKey, vm.taskId, vm.masterEntityKey, vm.appFlowdefId, vm.processInstanceId);
                }
                if (vm.sendTaskInfos.length > 1) {
                    var sendTip = "确认发送至 <span style='color:#22D479;font-size:18px' >&nbsp;" + vm.dealNull(vm.nextTask) + "</span>&nbsp;环节？";
                    if (vm.checkNotNull(vm.nextTaskAssignee)) {
                        sendTip += "下一环节审批人为：<span style='color:#22D479;font-size:18px' >&nbsp;" + vm.nextTaskAssignee + "&nbsp;</span>";
                    }
                    confirmMsg("确认发送信息", sendTip, function () {
                        vm.sendOperation(directSend);
                    }, null, null, null, null, null, true);
                } else {
                    vm.sendOperation(directSend);
                }
            } else {
                if (busFormIframe.window.wfBusSave) {
                    busFormIframe.window.wfBusSave(vm.flowdefKey, vm.taskId, vm.masterEntityKey, vm.appFlowdefId, vm.processInstanceId);
                    vm.$message.success("保存成功！");
                } else {
                    vm.$message.error("流程表单没有实现表单保存接口，无法保存表单信息！");
                }
            }
        },
        //弹窗发送对话框，并初始化信息
        showSendDialog: function (sendTaskInfos) {
            //显示窗口
            vm.sendDialog = true;
            //1、初始化下一环节信息，这里后面可能有多个环节，要根据选择的环节获取该环节的处理人，再供用户选择。
            // 如果多个环节时要绑定单选按钮事件，更新选择的环节到nextTask变量中
            $("#nextTaskList").empty();
            var nextTaskInfo = '';
            for (var i = 0; i < sendTaskInfos.length; i++) {
                var temp = sendTaskInfos[i];
                var checked = '';
                if (i == 0) {
                    checked = '" checked="checked';
                }
                nextTaskInfo += '<label class="m-radio m-radio--solid m-radio--brand">' +
                    '<input type="radio" onclick="vm.selectNextNode(\'' + temp.destActId + '\')" name="nextTask" value="' + temp.destActId + checked + '">' +
                    '<span></span>' +
                    '<div style="display: inline;font-size: 16px;height: 21px;line-height: 20px;">' + temp.destActName + '</div>' +
                    '</label>';
            }
            vm.delaySetHtmlToElement("nextTaskList", nextTaskInfo);
            //2、初始化下一环节审批人和提示信息
            vm.initNextAssigneeAndTip(vm.nextTaskAssignee, vm.sendTaskInfo.needSelectAssignee, vm.sendTaskInfo.message);
            //3、个人常用办理意见信息
            if (vm.opinionTableData.length == 0) {
                vm.getOpinionList();
            }

        },
        //初始化下一环节审批人和提示信息
        initNextAssigneeAndTip: function (nextTaskAssignee, needSelectAssignee, tipMessage) {
            //2、下一环节参与人信息
            if (vm.checkNotNull(nextTaskAssignee)) {
                vm.$set(vm.sendForm, 'nextTaskAssignee', nextTaskAssignee);
            } else {
                vm.$set(vm.sendForm, 'nextTaskAssignee', "同时发送给主办及协办人员");
            }
            //选择按钮启用/禁用
            if (!needSelectAssignee) {
                $("#selectAsingee").attr("disabled", true);
            } else {
                $("#selectAsingee").attr("disabled", false);
            }
            //3、备注说明信息
            var commentTip = "提示：";
            if (vm.checkNotNull(tipMessage)) {
                commentTip += tipMessage;
            }
            if (needSelectAssignee) {
                commentTip += "然后请点击“发送”按钮发送到所选环节";
            } else {
                commentTip += "请点击“发送”按钮发送到所选环节";
            }
            vm.delaySetHtmlToElement("sendTip", commentTip);
        },
        //延迟设置元素的值，由于element-ui解析的原因，第一次可能获取不到元素对象
        delaySetHtmlToElement: function (id, text) {
            var it = setInterval(function () {
                if ($("#" + id).length > 0) {
                    $("#" + id).html(text);
                    window.clearInterval(it);
                }
            }, 100);
        },
        //发送操作
        sendOperation: function (directSend) {
            vm.loading = true;
            request('bpmFrontUrl', {
                //url: ctx + 'rest/front/task/wfSend',
                url: vm.sendUrlPath,
                type: vm.requestMappingType,
                data: {
                    sendObjectStr: JSON.stringify(vm.sendParam),
                    //iteminstState: null,
                    //iteminstId: vm.busRecordId,
                    isSendSMS: vm.sendForm.isSendnuSMS,
                    isSendToNextHandle: vm.sendForm.isSendToNextHandle,
                    phoneNo: vm.sendForm.phoneNo,
                    contentDesc: vm.sendForm.contentDesc,
                    applyinstId: vm.masterEntityKey,
                    //新增参数---更改事项状态并推动流程
                    iteminstId: vm.iteminstId,
                    itemState: vm.enumItemStatus,
                    //更改申请状态并推动流程
                    applyinstId: vm.masterEntityKey,
                    applyState: vm.enumApplyStatus,
                }
            }, function (result) {
                if (result.success) {
                    vm.loading = false;
                    vm.sendDialog = false;
                    var message = "";
                    if (directSend) {
                        //直接发送的，不提示下一环节具体处理人。
                        message = "<span style='color:#22D479;font-size:18px' >流程发送成功！</span>";
                        message += "<span style='font-size:18px'>任务处理完毕，正等待其他用户处理！</span>";

                    } else {
                        //这里要在提交后的回调中调用到下一环节的提示信息，这两个变量会在操作过程中被改变，包括环节名称和处理人名称。
                        message = "流程已发送至 <span style='color:#22D479;font-size:18px' >&nbsp;" + vm.nextTask + "</span>&nbsp;环节";
                        if (vm.checkNotNull(vm.nextTaskAssignee)) {
                            message += "，下一环节审批人为：<span style='color:#22D479;font-size:18px' >&nbsp;" + vm.nextTaskAssignee + "&nbsp;。</span>";
                        }
                    }

                    //vm.showSuccessTip(message);

                    //获取下一个任务实例id---
                    var nextTaskId = result.content;
                    if (nextTaskId) {
                        //给出提示
                        vm.$message({dangerouslyUseHTMLString: true, message: message, type: 'success'});
                        setTimeout(function () {
                            //直接刷新界面好了
                            var newUrl = window.location.href.substring(0, window.location.href.indexOf("?") + 1) + "taskId=" + nextTaskId + "&viewId=" + vm.viewId;
                            window.location.href = newUrl;
                        }, 1500);
                    } else {
                        vm.showSuccessTip(message);
                    }
                } else {
                    vm.$message.error(result.message);
                }
            }, function () {
                vm.loading = false;
                vm.$message.error("流程发送失败！");
            });
        },
// ******** 只更改状态的（窗口和事项通用） 材料补正（事项）/退出特别程序（事项）/开始特别程序（事项）***************
        updateItemState: function () {
            //var vm = this;
            vm.loading = true;
            //var url = ctx + "/approve/btn/item/changeItemState";
            var params = {
                userOpinion: vm.opinionForm.opinionText,
                actionCode: vm.enumActionCode,
                //更改事项状态使用字段
                iteminstId: vm.iteminstId,
                itemState: vm.enumItemStatus,
                //任务ID（当需要挂起流程时，需要传参）
                taskId: vm.taskId,
                //更改窗口状态使用字段
                applyinstId: vm.masterEntityKey,
                applyState: vm.enumApplyStatus
            }
            request('', {
                url: vm.sendUrlPath,
                type: vm.requestMappingType,
                data: params
            }, function (result) {
                if (result.success) {
                    vm.loading = false;
                    vm.submitCommentsShow = false;
                    vm.showSuccessTip('操作成功');
                    //关闭当前页
                    closeCurrentTab();
                } else {
                    console.log(result.message);
                }
            }, function () {
                vm.loading = false;
                console.log("流程发送失败！");
            });
        },

        //显示成功提示信息，会关闭窗口
        showSuccessTip: function (message) {
            //刷新父页面
            window.opener.location.reload();
            window.opener.location.href = window.opener.location.href;
            this.$message({
                showClose: true,
                dangerouslyUseHTMLString: true,
                message: message,
                type: 'success'
            });
            setTimeout(function () {
                window.close();
            }, 3000);

            /* this.$alert(message, '提示信息', {
                 confirmButtonText: '确定',
                 dangerouslyUseHTMLString: true,
                 callback: function (action) {
                     window.close();
                 }
             });*/
        },
//----------- 制证 ---------------------
        getCertinstList: function () {
            //查询当前事项下的证照实例
            var _that = this;
            request('', {
                url: ctx + 'rest/certificate/certinst/list',
                type: 'get',
                data: {iteminstId: vm.iteminstId}
            }, function (result) {
                if (result.success) {
                    _that.certinstList = result.content;
                } else {
                    console.log(result.message);
                }
            }, function () {
                _that.loading = false;
                console.log("获取制证信息失败！");
            });
        },
        makeCertification: function () {
            var _that = this;
            _that.getCertinstList();
            _that.certinstModalShow = true;
        },
        //新增证照实例弹框
        addItemCert: function () {
            //查询当前事项实例下定义的证照列表；需要过滤掉已经存在的实例；如果集合为空，则提示不能创建
            //及业主单位ID，单位名称，项目ID,项目名称
            var _that = this;
            request('', {
                url: ctx + 'rest/certificate/cert/category/list',
                type: 'get',
                data: {
                    iteminstId: vm.iteminstId,
                    applyinstId: vm.masterEntityKey,
                    isFilter: true
                }
            }, function (result) {
                if (result.success) {
                    var content = result.content;
                    _that.certList = content.certList;
                    Vue.set(_that.editCertForm, 'applicant', content.applicant);
                    Vue.set(_that.editCertForm, 'projInfoId', content.projInfoId);
                    Vue.set(_that.editCertForm, 'unitInfoId', content.unitInfoId);
                    Vue.set(_that.editCertForm, 'issueOrgId', content.issueOrgId);
                    Vue.set(_that.editCertForm, 'issueOrgName', content.issueOrgName);
                    Vue.set(_that.editCertForm, 'iteminstId', _that.iteminstId);
                    if (_that.certList.length == 0) {
                        _that.$message({
                            message: '当前事项下没有证照定义或已经存在该证照！无法新增',
                            type: 'error'
                        });
                        return false;
                    } else {
                        _that.editCertinstModel = true;
                    }
                } else {
                    console.log(result.message);
                }
            }, function () {
                _that.loading = false;
                console.log("获取制证信息失败！");
            });

        },
        editCertinst: function (row) {
            var _that = this;
            //_that.editCertForm = row;
            Vue.set(_that.editCertForm, 'applicant', row.applicant);
            Vue.set(_that.editCertForm, 'projInfoId', row.projInfoId);
            Vue.set(_that.editCertForm, 'unitInfoId', row.unitInfoId);
            Vue.set(_that.editCertForm, 'issueOrgId', row.issueOrgId);
            Vue.set(_that.editCertForm, 'issueOrgName', row.issueOrgName);
            Vue.set(_that.editCertForm, 'iteminstId', _that.iteminstId);
            Vue.set(_that.editCertForm, 'termStart', _that.formatDate(row.termStart));
            Vue.set(_that.editCertForm, 'termEnd', _that.formatDate(row.termEnd));
            Vue.set(_that.editCertForm, 'issueDate', _that.formatDate(row.issueDate));
            Vue.set(_that.editCertForm, 'certinstId', row.certinstId);
            Vue.set(_that.editCertForm, 'certId', row.certId);
            Vue.set(_that.editCertForm, 'certinstName', row.certinstName);
            Vue.set(_that.editCertForm, 'certinstCode', row.certinstCode);
            Vue.set(_that.editCertForm, 'rootOrgId', row.rootOrgId);
            Vue.set(_that.editCertForm, 'certOwner', row.certOwner);
            _that.getFileListWin(row.certinstId);
            //_that.fileList = row.bscAttDetails;
            //反显证照ID对应的名称
            request('', {
                url: ctx + 'rest/certificate/cert/category/list',
                type: 'get',
                data: {
                    iteminstId: vm.iteminstId,
                    applyinstId: vm.masterEntityKey,
                    isFilter: false
                }
            }, function (result) {
                if (result.success) {
                    var content = result.content;
                    _that.certList = content.certList;
                    _that.editCertinstModel = true;
                } else {
                    console.log(result.message);
                }
            }, function () {
                _that.loading = false;
                console.log("获取制证信息失败！");
            });


        },
        //保存或修改证照实例
        saveOrUpdateCertinst: function () {
            var _that = this;
            console.log(_that.editCertForm);
            this.$refs['editCertForm'].validate(function (valid) {
                if (valid) {
                    request('', {
                        url: ctx + 'rest/certificate/updateCertInfo',
                        type: 'post',
                        data: _that.editCertForm
                    }, function (result) {
                        if (result.success) {
                            _that.getCertinstList();
                            _that.editCertinstModel = false;
                        } else {
                            console.log(result.message);
                        }
                    }, function () {
                        _that.loading = false;
                        console.log("保存或修改证照实例失败！");
                    });

                } else {
                    _that.$message({
                        message: '数据校验失败',
                        type: 'error'
                    });

                }
            });


        },
        deleteCertinst: function (id) {
            confirmMsg('提示', '确定要删除选中的批文吗？', function () {
                request('', {
                    url: ctx + 'rest/certificate/certinst/one/delete',
                    type: 'post',
                    data: {
                        'certinstId': id
                    },
                }, function (result) {
                    if (result.success) {
                        _that.$message.success('删除成功');
                        _that.getCertinstList();
                    } else {
                        console.log(result.message);
                    }
                }, function () {
                    vm.$message.error("删除失败！");
                });
            })
        },
        batchDeleteItemCert: function () {//
            var _that = this;
            if (_that.selectCertinstList.length == 0) {
                vm.$message.error("请选择待删除项！");
            } else {
                var certinstIds = [];
                _that.selectCertinstList.map(function (item) {
                    certinstIds.push(item.certinstId)
                })
                var certinstId = certinstIds.join(',');
                confirmMsg('提示', '确定要删除选中的批文吗？', function () {
                    request('', {
                        url: ctx + 'rest/certificate/certinst/batch/delete',
                        type: 'post',
                        data: {
                            'certinstIds': certinstId
                        },
                    }, function (result) {
                        if (result.success) {
                            _that.$message.success('删除成功');
                            _that.getCertinstList();
                        } else {
                            console.log(result.message);
                        }
                    }, function () {
                        vm.$message.error("删除失败！");
                    });
                })
            }
        },
        //选中的证照实例列表
        selecCertinstListChange: function (row) {
            //选择行数
            var _that = this;
            _that.selectCertinstList = row;
        },
        // 勾选de电子件
        selectFileChange: function (val) {
            this.fileSelectionList = val;
        },
        // 预览电子件
        filePreview: function (data) {
            var _that = this;
            var detailId = data.fileId;
            var flashAttributes = '';
            url = ctx + 'rest/certificate/consignerAtt/preview' + "?detailId=" + detailId + "&flashAttributes=" + flashAttributes;
            try {
                window.open(url);
            } catch (e) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            }
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
            this.fileWinData = new FormData();
            if (!_that.editCertForm.certId || !_that.editCertForm.unitInfoId) {
                _that.$message.error("缺少参数");
                return false;
            }
            Vue.set(file.file, 'fileName', file.file.name);
            this.fileWinData.append('file', file.file);
            this.fileWinData.append('iteminstId', _that.iteminstId);
            this.fileWinData.append("certId", _that.editCertForm.certId);
            this.fileWinData.append("certinstCode", _that.editCertForm.certinstCode);
            this.fileWinData.append("certinstName", _that.editCertForm.certinstName ? _that.editCertForm.certinstName : '');
            this.fileWinData.append("projInfoId", _that.editCertForm.projInfoId);
            this.fileWinData.append("unitInfoId", _that.editCertForm.unitInfoId);
            this.fileWinData.append("certinstId", _that.editCertForm.certinstId ? _that.editCertForm.certinstId : '');
            _that.loadingFileWin = true;
            axios.post(ctx + 'rest/certificate/certinst/att/upload', _that.fileWinData).then(function (res) {
                Vue.set(file.file, 'certinstId', res.data.content);
                _that.editCertForm.certinstId = res.data.content;
                _that.getFileListWin(res.data.content);
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
        // 获取已上传文件列表
        getFileListWin: function (certinstId) {
            var _that = this;
            request('', {
                url: ctx + 'rest/certificate/certinst/att/list',
                type: 'get',
                data: {certinstId: certinstId}
            }, function (res) {
                if (res.content) {
                    _that.uploadCertData = res.content;
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
        //批量删除附件
        batchDeleteAttach: function () {
            var _that = this;
            if (_that.fileSelectionList.length == 0) {
                this.$message.error("请选择文件");
                return false;
            } else {
                var ids = []
                _that.fileSelectionList.map(function (item) {
                    ids.push(item.fileId);
                });

                _that.deleteFileAttach(ids.join(","));

            }

        },
        //删除单个附件
        deleteOneAttach: function (id) {
            var _that = this;
            _that.deleteFileAttach(id);
        },
        deleteFileAttach: function (ids) {
            var _that = this;
            request('', {
                url: ctx + 'rest/certificate/certinst/att/delelte',
                type: 'post',
                data: {detailIds: ids}
            }, function (res) {
                if (res.success) {
                    _that.getFileListWin(_that.editCertForm.certinstId);
                } else {
                    console.log(res.message)
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },
        //单个删除附件
        /*//勾选证照附件
        selectCertAttChange: function (val) {
            this.certSelectionList = val;
        },*/
        testBtn: function () {
            var message = "<span style='color:#22D479;font-size:18px' >流程发送成功！</span>";
            message += "<span style='font-size:18px'>任务处理完毕，正等待其他用户处理！</span>";

            var message1 = "流程已发送至 <span style='color:#22D479;font-size:18px' >&nbsp;" + vm.nextTask + "</span>&nbsp;环节";
            message1 += "，下一环节审批人为：<span style='color:#22D479;font-size:18px' >&nbsp;" + vm.nextTaskAssignee + "&nbsp;。</span>";
            this.$message({
                showClose: true,
                dangerouslyUseHTMLString: true,
                message: message1,
                type: 'success',
                center: true
            });
        },

        resetForm: function (formName) {
            this.$refs[formName].resetFields();
        },
        formatDate: function (date) {
            date = date.substring(0, 10);
            date = date.replace(/-/g, '/');
            return new Date(date);
        },//
//=========================================工程建设 相关审批按钮 end =================
    },

    computed: {
        changeRightWidth: function () {
            return {
                rightWidth: !this.rightIconTab
            }
        }
    }
});

//4.0 ******************** 新接口  start********************************************

//---------------------- 制证 ----------------------------------------
function makeCertification() {
    vm.makeCertification();

}

function testBtn() {
    var message = "<span style='color:#22D479;font-size:18px' >流程发送成功！</span>";
    message += "<span style='font-size:18px'>任务处理完毕，正等待其他用户处理！</span>";

    var message1 = "流程已发送至 <span style='color:#22D479;font-size:18px' >&nbsp;" + vm.nextTask + "</span>&nbsp;环节";
    message1 += "，下一环节审批人为：<span style='color:#22D479;font-size:18px' >&nbsp;" + vm.nextTaskAssignee + "&nbsp;。</span>";
    this.$message({
        showClose: true,
        dangerouslyUseHTMLString: true,
        message: message,
        type: 'success',
        center: true
    });

}

//--------------更改事项状态和流程------------------------------------------

//事项-容缺通过
function passOfToleranceForItem() {
    vm.enumItemStatus = 'AGREE_TOLERANCE';
    vm.requestMappingType = 'put';
    urlForItem();
}

//事项-不受理
function rejectForItem() {
    vm.enumItemStatus = 'REFUSE_DEAL';
    vm.requestMappingType = 'put';
    urlForItem();
}

//事项-办结（通过）
function passForItem() {
    vm.enumItemStatus = 'AGREE';
    vm.requestMappingType = 'put';
    urlForItem();
}

//事项-办结（不通过）
function denyForItem() {
    vm.enumItemStatus = 'DISAGREE';
    vm.requestMappingType = 'put';
    urlForItem();
}

//事项-受理
function acceptForItem() {
    //vm.enumItemStatus = 'ACCEPT_DEAL';//3

    //20190813 改为部门受理状态
    vm.enumItemStatus = 'DEPARTMENT_DEAL_START';//3
    vm.requestMappingType = 'put';
    urlForItem();
}

//事项-容缺受理
function toleranceAcceptForItem() {
    //20190813 改为部门受理状态
    vm.enumItemStatus = 'DEPARTMENT_DEAL_START:TOLERANCE_ACCEPT';//部门开始受理：3，并且标识为容缺受理
    vm.requestMappingType = 'put';
    urlForItem();
}

//更改事项状态和流程url
function urlForItem() {
    vm.sendUrlPath = ctx + "/approve/btn/item/wfSendAndChangeItemState";
    vm.requestMappingType = 'put';
    vm.wfBusSend();
}

//--------------------------更改窗口-状态及流程--------------------------

//窗口-办结   更改状态并推动流程
function finishForWin() {
    vm.enumApplyStatus = 'COMPLETED';//6
    vm.sendUrlPath = ctx + "/approve/btn/win/wfSendAndChangeApplyState";
    vm.requestMappingType = 'put';
    vm.wfBusSend();
}

//窗口-受理  更改状态并推动流程
function acceptForWin() {//2
    vm.enumApplyStatus = 'ACCEPT_DEAL'; //2
    vm.sendUrlPath = ctx + "/approve/btn/win/wfSendAndChangeApplyState";
    vm.requestMappingType = 'put';
    vm.wfBusSend();
}


//-----------------------以下使用原来的流程---------------------------------

//窗口-办理---只推动流程流转
function handleForWin() {
    vm.sendUrlPath = ctx + 'rest/front/task/wfSend';
    vm.requestMappingType = 'post';
    vm.wfBusSend();
}

//事项-办理---只推动流程流转
function handleForItem() {
    vm.sendUrlPath = ctx + 'rest/front/task/wfSend';
    vm.requestMappingType = 'post';
    vm.wfBusSend();

    /*vm.enumItemStatus = 'DEPARTMENT_DEAL_START';//3
    vm.requestMappingType = 'put';
    urlForItem();*/
}

//************** 以下只改变状态 start ***************************

//事项---材料补正开始
function startSupplementForItem() {
    vm.enumActionCode = 'ITEM_CAILIAOBUZHENG';
    vm.enumItemStatus = 'CORRECT_MATERIAL_START';
    vm.commentTitle = '材料补正';
    vm.requestMappingType = 'put';
    vm.sendUrlPath = ctx + "/approve/btn/item/changeItemState";
    // showCommentDialog();
    showSupplementForItem();
}
// 材料补正按钮-触发vue实例事件
function showSupplementForItem(){
  vm.isShowMaterialSupplementDialog = true;
  vm.fetchLackMatsByApplyinstIdAndIteminstId();
}

//开始特别程序方法---事项
function startSpecialProcessForItem() {
    /*vm.enumActionCode = 'ITEM_JINRU_TEBIECHENGXU';
    vm.enumItemStatus = 'SPECIFIC_PROC_START';
    vm.commentTitle = '开始特别程序';
    vm.requestMappingType = 'put';
    vm.sendUrlPath = ctx + "/approve/btn/item/changeItemState";
    console.info('====');*/
    //判断当前事项实例状态，补正状态和特殊撤销以及开始的不能发起特殊撤销
    request('', {
        url: ctx + 'rest/specialProcedure/getCurrentSpecialStatus',
        type: 'get',
        data: {'applyinstId': vm.masterEntityKey,'iteminstId':vm.iteminstId}
    }, function (res) {
        if (res.success) {
            console.info(res.content);
            debugger
            /*if(res.content.special.specialState == '9'){
                alertMsg('', "特殊程序已开始，不能再进入！", '关闭', 'error', true);
                return;
            }*/
            if(res.content.iteminst.iteminstState == '6') {
                alertMsg('', "材料补录阶段，不能进入特殊程序！", '关闭', 'error', true);
                return;
            }
            var _url=   ctx + 'rest/specialProcedure/index?iteminstId='+vm.iteminstId+'&applyinstId='+vm.masterEntityKey+'&isApprover='+vm.isApprover+"&taskId="+vm.taskId+"&processInstanceId="+vm.processInstanceId;
            window.open(_url,'_self');

        }
    }, function (msg) {
        alertMsg('', '进入特殊程序失败', '关闭', 'error', true);
    });

}

//弹出意见对话框
function showCommentDialog() {
    //只改变状态
    if (vm.opinionTableData.length == 0) {
        vm.getOpinionList();
    }
    vm.submitCommentsShow = true;
}

//打印回执列表接口
function getPrintList() {
    vm.receiptPrintDialog = true;
    vm.receiptPrintLoading = true;
    if (vm.receiptTable.length == 0) {
        request('', {
            url: ctx + 'rest/receive/getStageOrSeriesReceiveByApplyinstIds',
            type: 'get',
            data: {'applyinstIds': vm.masterEntityKey}
        }, function (res) {
            if (res.success) {
                vm.receiptTable = res.content;
            }
        }, function (msg) {
            alertMsg('', '查询回执失败', '关闭', 'error', true);
        });
    }


}

//流程跟踪
function showDiagramDialog() {
    vm.showDiagramDialog();
}

//查看子流程
function showChildrenDiagramDialog(node) {
    //这个 是在 后台生成流程图节点信息的接口约定的
    var procInstId = $(node).attr("data-procInstId");
    var isCheck = $(node).attr("data-isCheck");
    vm.showChildrenDiagramDialog(procInstId, isCheck);
}

function closeCurrentTab() {
    var userAgent = navigator.userAgent;
    if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Presto") != -1) {
        window.location.replace("about:blank");
    } else {
        window.opener = null;
        window.open("", "_self");
        window.close();
    }
}

//4.0 ******************** 新接口  end********************************************

//以下方法是工具栏按钮对应的接口----启用，修改完后删除
//工具栏按钮接口
//暂存
function wfBusSave() {
    vm.wfBusSave();
}


//挂起流程
function suspendProcess() {
    agcloud.bpm.engine.metronic.suspendProcessInstance(vm.processInstanceId);
}

//激活流程
function activateProcess() {
    agcloud.bpm.engine.metronic.activateProcessInstance(vm.processInstanceId);
}

//工具栏调用接口使用到的相关私有方法
function checkParentProExist(procInstId) {
    vm.checkParentProExist(procInstId);
}

//******************** 以下为样式-*****************************************
// 右侧点击放大 效果
$('.tab-icon-right').click(function (e) {
    e.stopPropagation();
    if ($(this).hasClass('expand')) {
        $('.content-right').css('width', '50%');
        $('#processHistoryComment>ul').css('padding-left', '38%');
        $(this).removeClass('expand').addClass('compress');
        $('.m-accordion__item-title > span').removeClass('ellipsis').css('width', 'auto');
    } else {
        $('.content-right').css('width', '25%');
        $('#processHistoryComment>ul').css('padding-left', '20%');
        $(this).addClass('expand').removeClass('compress');
        $('.m-accordion__item-title > span').addClass('ellipsis').css('width', '160px');
    }

});
// 右侧展开后 点击其他区域 收起
$(document).click(function (e) {
    var _con = $('.content-right');   // 设置目标区域
    if (!_con.is(e.target) && _con.has(e.target).length === 0) {
        $('.content-right').css('width', '25%');
        $('#processHistoryComment>ul').css('padding-left', '20%');
        $('.content-right .tab-icon-right').addClass('expand').removeClass('compress');
        $('.content-right .m-accordion__item-title > span').addClass('ellipsis').css('width', '160px');
    }
    ;
});

function enterfullscreen() {//进入全屏

    var docElm = document.documentElement;
//W3C
    if (docElm.requestFullscreen) {
        docElm.requestFullscreen();
    }
//FireFox
    else if (docElm.mozRequestFullScreen) {
        docElm.mozRequestFullScreen();
    }
//Chrome等
    else if (docElm.webkitRequestFullScreen) {
        docElm.webkitRequestFullScreen();
    }
//IE11
    else if (docElm.msRequestFullscreen) {
        docElm.msRequestFullscreen();
        var widthFull = $(window).width();
        $('#wrapper').css('width', widthFull);
    } else if (typeof window.ActiveXObject !== "undefined") {//for Internet Explorer
        var wscript = new ActiveXObject("WScript.Shell");
        if (wscript !== null) {
            wscript.SendKeys("{F11}");
        }
    }
}

function exitfullscreen() { //退出全屏
    if (document.exitFullscreen) {
        document.exitFullscreen();
    } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
    } else if (document.webkitCancelFullScreen) {
        document.webkitCancelFullScreen();
    } else if (document.msExitFullscreen) {
        document.msExitFullscreen();
    } else if (typeof window.ActiveXObject !== "undefined") {//for Internet Explorer
        var wscript = new ActiveXObject("WScript.Shell");
        if (wscript !== null) {
            wscript.SendKeys("{F11}");
        }
    }
}

var a = 0;
$('.full-screen').on('click', function () {
    $('.full-screen i').toggleClass('min');
    a++;
    a % 2 == 1 ? enterfullscreen() : exitfullscreen();
})
// 关闭当前页面
$('.close-window').click(function () {  // ie能实现  谷歌只能实现关闭当前窗口打开空白窗口
    var userAgent = navigator.userAgent;
    if (confirm("您确定要关闭本页吗？")) {
        if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") != -1) {
            window.location.href = "about:blank";
        } else {
            window.opener = null;
            window.open("", "_self");
            window.close();
        }
    }
});

// 初始化设置固定的宽度和高度
$(window).scroll(function () {
    contenShowView();
});
contenShowView();

function contenShowView() {
    var windowHeight = $(window).height();
    var offset = $('.content-left .tab-content').offset().top;
    $('.content-left .tab-content').css('height', windowHeight - offset + 'px');
}


function contenShowHeight() {
    var autoHeightForm = $(window).height();
    var eleOffset = $("#form-tabs-content").offset().top;
    $("#form-tabs-content").height(autoHeightForm - eleOffset - 44);
    $("#right_tab_1").height(autoHeightForm - 120);
}

function compute() {
    var paddigRightVal = $(".content-right").width();
    $("#page-content-wrapper").css("padding-right", paddigRightVal);
    $("#applyStatus").css("padding-right", paddigRightVal);
}

compute();
contenShowHeight();
$(window).resize(function () {
    contenShowHeight();
    compute();
})

$("#processView").on('hidden.bs.collapse', function (eve) {
    $('#' + eve.target.id).parent().find('.el-icon').removeClass('el-icon-arrow-up').addClass('el-icon-arrow-down')
})
$("#processView").on('show.bs.collapse', function (eve) {
    $('#' + eve.target.id).parent().find('.el-icon').removeClass('el-icon-arrow-down').addClass('el-icon-arrow-up')
})
