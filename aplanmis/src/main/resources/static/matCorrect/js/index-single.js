/*
 * @Author: ZL
 * @Date:   2019/6/4
 * @Last Modified by:   ZL
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
        // 输入为数字 大于等于0
        var checkMissNum = function (rule, value, callback) {
            if (value === '' || value === undefined || value === null || value.toString().trim() === '') {
                callback(new Error('必填字段！'));
            } else if (value) {
                var flag = !/^[0-9]\d*$/.test(value) && !(/^[0-9]\d*$/.test(value));
                if (flag) {
                    return callback(new Error('格式不正确'));
                } else {
                    callback();
                }

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
            isParallel: false, // true为并联
            isBranch: false,//是否分局承办
            itemBasicInfo: {},//事项信息
            itemVerId: '',//当前申报事项版本
            projBascInfoShow: {}, // 项目信息
            localCode: '', // 项目编码
            loading: false, // 页面加载遮罩
            searchKeyword: '', // 查询关键字
            projInfoId: '', // 查询项目id
            projName: '', // 查询项目name
            otherUnits: [], // 申报主体为非企业（机关或事业单位）列表
            buildUnits: [], // 申报主体为企业列表
            agentUnits: [], // 经办单位信息
            agentChecked: false, // 申办主体信息 是否经办
            jiansheFrom: [],  // 申报主体为企业或非企业（机关或事业单位）时个人信息
            selThemeDialogShow: false, // 是否展示选择主题弹窗
            linkQuerySucc: false, // 项目代码工程编码是否可输入修改
            projTypeList: [], // 项目类型列表
            projType: '', // 项目类型
            projNatureList: [], // 建设性质列表
            projNature: '', // 建设性质
            approveNumClazz: true, // 是否显示备案文号
            projectTreeInfoModal: false, // 是否展示项目树窗口
            orgTreeInfoModal: false, // 部门组织树弹窗
            projTree: [], // 项目树 数据
            orgTree: [], // 部门组织树
            projTreeDefaultProps: {
                children: 'children',
                label: 'name'
            },
            isJg: false, // 是否机关单位 false为企业单位
            getResultForm: {
                receiveMode: 1,
                smsType: '',
                addresseeName: '',
                addresseePhone: '',
                addresseeIdcard: '',
                id: '',
            }, // 结果领取方式
            themeInfoList: [], // 主题列表
            themeInfoListP: [], // 并联主题列表
            parallelThemeList: [], // 并联主题列表
            activeNames: ['1', '2', '3', '4', '5', '6'], // el-collapse 默认展开列表
            ctx: ctx,
            verticalTabData: [ // 左侧纵向导航数据
                {
                    label: '申报事项信息',
                    target: 'itemBaseInfo'
                },
                {
                    label: '基本信息',
                    target: 'baseInfo'
                }, {
                    label: '申办主体信息',
                    target: 'applyInfo'
                }, {
                    label: '事项一单清',
                    target: 'applyStage'
                }, {
                    label: '材料一单清',
                    target: 'matsList'
                }, {
                    label: '办证结果取件方式',
                    target: 'getResult'
                }
            ],
            curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
            curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
            popStateList: [], // 新增情形列表
            matsTableData: [], // 材料列表
            model: {
                rules: {
                    getPaper: {required: true, message: "必选", trigger: ["change"]},
                    getCopy: {required: true, message: "必选", trigger: ["change"]},
                    realPaperCount: {validator: checkMissValue, required: true, message: "必填字段", trigger: ['blur']},
                    realCopyCount: {validator: checkMissValue, required: true, message: "必填字段", trigger: ['blur']},
                },
                matsTableData: []
            },
            coreItems: [], // 并行推进事项列表
            parallelItems: [], // 并联推进事项
            isSelItem: 0, // 事项列表是否展示复选框 0否 1是
            addEditManModalTitle: '新增联系人',
            addEditManModalFlag: 'edit',
            addEditManModalShow: false, // 是否显示新增编辑联系人窗口
            addEditManform: {},  // 新增编辑联系人信息
            addEditManPerform: {},  // 新增编辑联系人信息
            addLinkManRules: { // 新增编辑联系人校验
                linkmanName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                linkmanCertNo: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                linkmanMobilePhone: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ]
            },
            unitInfoIdList: [], // 根据项目id查找相关联的单位列表
            rulesCompanyForm: { // 编辑新增单位信息校验
                applicant: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                idrepresentative: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                linkmanCertNo: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                linkmanMobilePhone: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                linkmanName: [
                    {required: true, message: '请选择联系人！', trigger: 'change'},
                ]
            },
            rulesApplyPersonForm: { // 编辑新增个人申报主体信息校验
                applyLinkmanName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                applyLinkmanIdCard: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                applyLinkmanTel: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                linkLinkmanName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                linkLinkmanIdCard: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                linkLinkmanTel: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ]
            },
            statusLineList: [], // 主题下阶段类型
            statusLineListActive: 0, // 主题下阶段类型选中状态
            AllFileList: [], // 智能分拣区所选择文件
            fileList: [],
            checkAll: true, // 智能分拣区文件是否全选
            checkedSelFlie: [], // 智能分拣区已选文件
            AllFileListId: [], // 已选文件name集合
            matIds: [], // 材料matIdS
            selMatinstId: '', // 已选材料实例Id
            matinstIds: [], // 材料实例Ids
            rootUnitInfoId: '', // 默认第一个单位id 有经办为第一个经办单位id
            rootLinkmanInfoId: '', // 默认第一个id 有经办为第一个经办id
            rootApplyLinkmanId: '', // 申请联系人ID
            statusActiveIndex: -1, // 申报阶段选中阶段index
            sameAsApplyLink: false, // 联系人信息是否与经办人一致
            projNameSelect: [], // 下拉选择项目名数组
            searchProjfilterText: '', // 项目树窗口过滤查询
            searchOrgfilterText: '',
            applyPersonFrom: {
                applyLinkmanId: '',
                linkLinkmanId: '',
                applyLinkmanName: '',
                applyLinkmanIdCard: '',
                applyLinkmanTel: '',
                applyLinkmanEmail: '',
                linkLinkmanName: '',
                linkLinkmanIdCard: '',
                linkLinkmanTel: '',
                linkLinkmanEmail: '',
            },  // 申报主体为个人时个人信息
            applySubjectType: 0,  // 申办主体信息类型 0个人 1企业 2非企业
            projSelect: false, // 项目类型建设性质是否可下拉选择
            activeTab: 0,  // 纵向导航active状态index
            showVerLen: 2, // 显示左侧纵向导航栏的长度
            showMoreProjInfo: false, // 查询项目后展示更多信息
            loadingFile: false, // 文件上传loading
            loadingFileWin: false, // 窗口文件上传loading
            dialogHtml: '', // 样本弹窗html
            showMatFilesDialogShow: false, // 是否展示样本弹窗
            showUploadWindowFlag: false, // 是否展示文件上传窗口
            showUploadWindowTitle: '材料附件', // 文件上传窗口header
            getFileListWindowFlag: false, // 是否展示导入电子材料弹窗
            fileSelectionList: [], // 所选电子件
            selMatRowData: {}, // 所选择的材料信息
            uploadMatinstId: '', // 上传返回材料实例

            treeRightBtnList: false, // 项目树是否展示右键操作按钮
            treeBtnTop: '',
            treeBtnLeft: '',
            childProjText: '', // 子项目工程备注
            childProjName: '', // 子项目工程名称
            parentProjInfoId: '', // 父工程id
            loadingLinkMan: false,
            applyinstId: '', // 申请实例id
            stateIds: [], // 情形id集合
            getPaperAll: false,
            getCopyAll: false,
            provinceList: [], // 所有的省份信息
            cityList: [], // 所有市区信息
            countyList: [], // 所有地区信息
            smsInfoId: '', // 领证人id
            showFileListKey: [], // 展示材料下文件列表
            rulesResultForm: { // 办证结果取件方式信息校验
                addresseeName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseePhone: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeIdcard: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                receiveMode: [
                    {required: true, message: "必选", trigger: 'change'},
                ],
            },
            rulesResultForm1: {
                addresseeName: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseePhone: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                addresseeIdcard: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ],
                receiveMode: [
                    {required: true, message: "必选", trigger: 'change'},
                ],
                smsType: [
                    {required: true, message: "必选", trigger: 'change'},
                ],
                addresseeCounty: [
                    {required: true, validator: checkMissValue, trigger: 'change'},
                ],
                addresseeCounty: [
                    {required: true, validator: checkMissValue, trigger: 'change'},
                ],
                addresseeCounty: [
                    {required: true, validator: checkMissValue, trigger: 'chenge'},
                ],
                addresseeAddr: [
                    {required: true, validator: checkMissValue, trigger: 'blur'},
                ]
            },
            jingbanFlag: false, // 是否为经办
            stateList: [], // 情形选中列表
            stateSelVal: {},
            checked: [],
            fileData: [], // 一键分拣参数列表
            fileWinData: [], // 上传窗口上传参数列表
            uploadLogo: '',
            addChildProjShow: false, // 新增子项目input
            shareFileList: [],
            uploadTableData: [],
            /****** 审批意见字段 ******/
            submitCommentsFlag: false, // 是否展示收件意见框
            submitCommentsFlag1: false, // 展示常用意见管理框
            submitCommentsFlag2: false, // 展示编辑常用意见框
            commentsList: [], // 常用意见列表
            selComment: '', // 已选常用意见
            comments: '', // 已填审批意见
            commentTitle: '收件意见对话框',//收件意见框标题
            addCommentModalShow: false,//是否显示新增意见弹框
            manageCommentModalShow: false,//是否显示常用意见管理弹窗
            editCommentModalShow: false,//编辑意见弹框

            commentForm: {selectComment: '', inputComment: ''},
            commentFormRule: {
                inputComment: [
                    {required: true, message: '请输入意见', trigger: 'change'},
                    /*{ min: 1, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }*/
                ],
            },//填写审批意见校验
            editCommentForm: {opinionId: '', opinionContent: '', userId: '', sortNo: ''},//编辑的意见对象
            editCommentFormRule: {
                opinionContent: [
                    {required: true, message: '请输入意见', trigger: 'change'},
                    /*{ min: 1, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }*/
                ],
                sortNo: [
                    {required: true, message: '请输入数量', trigger: 'blur'}
                ],
            },//意见编辑框保存校验
            multipleOptionSelection: [],//选择的意见
            /**********不收件参数********/
            refusedRecepitForm: {
                receiveId: '',
                itemVerId: '',
                applyinstSource: 'win',
                isSeriesApprove: '1',
                projInfoId: '',
                projName: '',
                receiptType: '3',
                receiveMemo: '',
                receiveCertNo: '',
                receiveUserName: '',
                receiveUserMobile: '',
                receiveMode: '1',//领取方式
                serviceAddress: '政务服务大厅',
                issueUserMobile: '',
                linkmanInfoId: '',
                iteminstState: '3'
            },//不收件参数
            refusedRecepitFormRule: {
                receiveMemo: [{required: true, message: '请输入意见', trigger: 'blur'}],
                issueUserMobile: [{validator: checkPhoneNum, trigger: 'blur'}]
            },//不收件参数校验
            refusedRecepitModalShow: false,//不收件弹窗
            selectMat: '',//选择的材料
            buttonStyle: '',//点击的按钮 0 发起申报；1 打印回执 。。。。。
            showMatList: false,//收件意见弹框是否显示材料列表
            //selectedStates:[],//选择的情形，可能有单选，或者多选{stateId:'',parentStateId:''}
            receiveList: [],//回执列表
            receiveModalShow: false,//回执弹窗控制
            //fit: ['fill', 'contain', 'cover', 'none', 'scale-down'],
            branchOrg: '', // 分局承办id
            uploadPercentage: 0, // 进度条百分比
            progressIntervalStop: false, // 定时器
            progressDialogVisible: false,
            progressStus: '',
            searchFileListfilter: '',
            selMatinstIds: [],
        }
    },
    mounted: function () {
        var _that = this;
        _that.itemVerId = itemVerId;
        _that.refusedRecepitForm.itemVerId = itemVerId;
        _that.getItemInfo();
        _that.getProjTypeNature('PROJECT_PROPERTY');
        _that.getProjTypeNature('PROJECT_CLASS');
        //_that.queryProvince();
        window.addEventListener('scroll', _that.handleScroll);
        window.addEventListener('resize', function (ev) {
            _that.curWidth = (document.documentElement.clientWidth || document.body.clientWidth);
        });
    },
    watch: {
        searchProjfilterText: function (val) {
            this.$refs.projTree.filter(val);
        },
        searchOrgfilterText: function (val) {
            this.$refs.orgTree.filter(val);
        },
        searchFileListfilter: function (val) {
            this.searchShareFileList(this.selMatRowData)
        }
    },
    methods: {
        getItemInfo: function () {
            var _that = this;
            request('', {
                url: ctx + 'rest/item/detail/' + _that.itemVerId,
                type: 'get',
            }, function (data) {
                if (data.success) {
                    if (data.content) {
                        _that.itemBasicInfo = data.content;
                    }
                }
            }, function (msg) {
                //_that.showMoreProjInfo = false;
                //_that.showVerLen = 2;
                alertMsg('', '服务请求失败', '关闭', 'error', true);
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
        // keyword模糊查询项目信息
        searchProjInfoByKeyword: function (queryString, cb) {
            var _that = this;
            _that.projInfoId = '';
            if (typeof (queryString) != 'undefined') queryString = queryString.trim();
            if (queryString != '' && queryString.length >= 2) {
                request('opus-admin', {
                    url: ctx + 'rest/project/info',
                    type: 'get',
                    data: {"keyword": queryString},
                }, function (result) {
                    if (result.content) {
                        _that.projNameSelect = result.content;
                        _that.projNameSelect.map(function (item) {
                            Vue.set(item, 'value', item.projName + ' (' + item.localCode + ')');
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
        createFilter: function (queryString) {
            return function (projNameSelect) {
                return (projNameSelect.value.toLowerCase());
            };
        },
        // 选择要查的项目
        projNameSel: function (item) {
            this.projInfoId = item.projInfoId;
            this.projName = item.projName;
            this.linkQuery();
        },
        // 清空数据
        clearSearchData: function () {
            this.stateList = [];
            this.model.matsTableData = [];
        },

        // 获取项目详细信息
        searchProjAllInfo: function () {
            var _that = this;
            _that.loading = true;
            _that.clearSearchData();
            request('', {
                url: ctx + 'rest/project/one/' + _that.projInfoId,
                type: 'get',
            }, function (data) {
                if (data.success) {
                    if (data.content) {
                        var result = data.content;
                        _that.showMoreProjInfo = true;
                        _that.showVerLen = _that.verticalTabData.length;
                        _that.projBascInfoShow = result; // 项目主要信息
                        _that.applySubjectType = Number(result.applySubjectType); // 申办主体类型
                        if (result.personalApplicant) {
                            _that.applyPersonFrom = result.personalApplicant; // 个人申报主体信息
                        }
                        if (result.buildUnits) {
                            _that.buildUnits = result.buildUnits; // 企业申报主体信息
                        }
                        if (result.otherUnits) {
                            _that.otherUnits = result.otherUnits; // 个人申报主体信息
                        }
                        if (result.agentUnits) { // 存在经办单位
                            _that.agentUnits = result.agentUnits;  // 经办单位信息
                            if (result.agentUnits.length > 0) {
                                _that.agentChecked = true; // 经办勾选
                            }
                        } else {
                            _that.agentUnits = [];
                            _that.agentChecked = false;
                        }
                        _that.setJiansheFrom();
                        _that.linkQuerySucc = true;
                        // 判断项目是否无编码申报
                        if ((result.localCode.slice(0, 3) == 'ZBM')) {
                            _that.projSelect = false;
                            _that.approveNumClazz = false; // 不显示备案文号
                        } else {
                            _that.projSelect = true;
                            _that.approveNumClazz = true; // 显示备案文号
                        }
                        _that.getStatusStateMats('', 'ROOT', '', '', true); // 获取阶段
                    }
                    _that.loading = false;
                } else {
                    _that.showMoreProjInfo = false;
                    _that.showVerLen = 2;
                    _that.$message({
                        message: data.message ? data.message : '没有查到该项目信息！',
                        type: 'error'
                    });
                    _that.loading = false;
                }
            }, function (msg) {
                _that.showMoreProjInfo = false;
                _that.showVerLen = 2;
                _that.loading = false;
                alertMsg('', '服务请求失败', '关闭', 'error', true);
            });
        },
        // 联网查询
        linkQuery: function () {
            var _that = this;
            if (_that.projInfoId) {
                _that.searchProjAllInfo();
            } else {
                if (!_that.searchKeyword) {
                    _that.$message({
                        message: '请输入项目（工程）的代码或名称！',
                        type: 'error'
                    });
                } else {
                    _that.$message({
                        message: '请先选择要查询的项目！',
                        type: 'error'
                    });
                }
            }
        },
        // 企业非企业申报主体列表信息
        setJiansheFrom: function () {
            if (this.applySubjectType == 1) {  // 企业申报
                this.jiansheFrom = this.buildUnits;
                this.isJg = false;
                this.getSentPerInfo(this.buildUnits[0])
            } else if (this.applySubjectType == 2) { // 非企业申报
                this.jiansheFrom = this.otherUnits;
                this.isJg = true;
                this.getSentPerInfo(this.otherUnits[0]);
            } else { // 个人申报
                var perData = {
                    linkmanName: this.applyPersonFrom.applyLinkmanName,
                    linkmanId: this.applyPersonFrom.applyLinkmanId,
                    linkmanMobilePhone: this.applyPersonFrom.applyLinkmanTel,
                    linkmanCertNo: this.applyPersonFrom.applyLinkmanIdCard
                }
                this.getSentPerInfo(perData);
            }
            if (this.jiansheFrom.length > 0) {
                this.rootUnitInfoId = this.jiansheFrom[0].unitInfoId;
                this.rootLinkmanInfoId = this.jiansheFrom[0].linkmanId
            }
            if (this.agentUnits.length > 0) {
                this.rootUnitInfoId = this.agentUnits[0].unitInfoId;
                this.rootLinkmanInfoId = this.agentUnits[0].linkmanId;
            }
            if (this.applySubjectType == 0 && this.applyPersonFrom.applyLinkmanId) {
                this.rootApplyLinkmanId = this.applyPersonFrom.applyLinkmanId
            }
            ;
        },
        // 切换申报主体类型
        changeApplySubjectSelect: function (val) { // 申办主题类型切换事件
            this.setJiansheFrom();
        },
        // 获取项目性质项目类型
        getProjTypeNature: function (code) {
            var _that = this;
            request('opus-admin', {
                url: ctx + 'rest/dict/code/items/list',
                type: 'get',
                data: {'dicCodeTypeCode': code}
            }, function (result) {
                if (result.content) {
                    if (code == 'PROJECT_PROPERTY') {
                        _that.projNatureList = result.content;
                    } else if (code == 'PROJECT_CLASS') {
                        _that.projTypeList = result.content;
                    }
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },
        //查看项目树
        showProjTree: function () {
            var _that = this;
            if (_that.projInfoId == 'undefined' || _that.projInfoId == '' || _that.projInfoId == null) {
                alertMsg('', '请先联网查询项目', '关闭', 'error', true);
            } else {
                request('', {
                    url: ctx + 'rest/project/tree/' + _that.projInfoId,
                    type: 'get',
                }, function (result) {
                    if (result.success) {
                        _that.projectTreeInfoModal = true;
                        _that.projTree = _that.toTree(result.content);
                    } else {
                        _that.$message({
                            message: '项目工程查询失败！',
                            type: 'error'
                        });
                    }
                }, function (msg) {
                    alertMsg('', '网络加载失败！', '关闭', 'error', true);
                })
            }
        },
        // 项目树节点进行筛选时执行的方法
        filterProjNode: function (value, data) {
            if (!value) return true;
            return data.name.indexOf(value) !== -1;
        },
        // 清空筛选项目树条件
        clearProjfilterText: function () {
            this.searchProjfilterText = '';
        },
        // 获取办证结果领取人信息
        getSentPerInfo: function (data) {
            if (data) {
                this.getResultForm.addresseeName = data.linkmanName;
                this.getResultForm.addresseePhone = data.linkmanMobilePhone;
                this.getResultForm.addresseeIdcard = data.linkmanCertNo;
            } else {
                this.getResultForm.addresseeName = '';
                this.getResultForm.addresseePhone = '';
                this.getResultForm.addresseeIdcard = '';
            }
        },
        // 编辑保存项目基本信息
        saveOrUpdateProjFrom: function (isParallel) {
            var _that = this;
            var props = {
                buildAreaSum: _that.projBascInfoShow.buildAreaSum,
                endTime: _that.projBascInfoShow.endTime,
                foreignApproveNum: _that.projBascInfoShow.foreignApproveNum,
                foreignRemark: _that.projBascInfoShow.foreignRemark,
                investSum: _that.projBascInfoShow.investSum,
                localCode: _that.projBascInfoShow.localCode,
                nstartTime: _that.projBascInfoShow.nstartTime,
                projAddr: _that.projBascInfoShow.projAddr,
                projName: _that.projBascInfoShow.projName,
                projNature: _that.projBascInfoShow.projNature,
                projType: _that.projBascInfoShow.projType,
                scaleContent: _that.projBascInfoShow.scaleContent,
                xmYdmj: _that.projBascInfoShow.xmYdmj,
                gcbm: _that.projBascInfoShow.gcbm,
                projInfoId: _that.projInfoId
            }
            request('', {
                url: ctx + 'rest/project/edit',
                data: props,
                type: 'post'
            }, function (data) {
                if (data.success) {
                    _that.$message({
                        message: '信息保存成功',
                        type: 'success'
                    });
                } else {
                    _that.$message({
                        message: data.message ? data.message : '信息保存失败',
                        type: 'error'
                    });
                }
            }, function (msg) {
                _that.$message({
                    message: msg.message ? msg.message : '服务请求失败',
                    type: 'error'
                });
            })
        },
        // 新增单位信息
        addUnitInfoForm: function (isOwner) {
            var _that = this;
            var projInfoIds = '';
            var obj = {
                applicant: '',
                idcard: '',
                idno: '',
                idrepresentative: '',
                idtype: '',
                isOwner: isOwner,
                linkmanInfoId: '',
                projInfoId: _that.projInfoId,
            };
            if (isOwner == "1") {  // 新增建设单位
                _that.jiansheFrom.push(obj);
            } else {
                _that.agentUnits.push(obj);
            }
        },
        // 删除单位信息
        delUnitInfoProj: function (data, index, flag) {
            var _that = this;
            var unitProjIdFlag = data.unitInfoId && _that.projInfoId;
            var applyinstId = data.applyinstId;
            var unitInfoId = data.unitInfoId;
            if (applyinstId || !unitProjIdFlag) {
                if (flag == 'jingban') {
                    _that.agentUnits.splice(index, 1);
                } else {
                    _that.jiansheFrom.splice(index, 1);
                }
                return;
            }
            confirmMsg('提示信息：', '你确定要删除该单位吗？', function () {
                _that.loading = true;
                request('opus-admin', {
                    url: ctx + "rest/unit/delete",
                    type: 'post',
                    data: {unitInfoId: unitInfoId, projInfoId: _that.projInfoId}
                }, function (data) {
                    if (data.success) {
                        _that.$message({
                            message: '删除成功',
                            type: 'success'
                        });
                        if (flag == 'jingban') {
                            _that.agentUnits.splice(index, 1);
                        } else {
                            _that.jiansheFrom.splice(index, 1);
                        }
                        _that.loading = false;
                    } else {
                        _that.$message({
                            message: data.message ? data.message : '删除失败',
                            type: 'error'
                        });
                        _that.loading = false;
                    }
                }, function (msg) {
                    _that.$message({
                        message: msg.message ? msg.message : '删除失败',
                        type: 'error'
                    });
                    _that.loading = false;
                })
            }, function () {
            }, '确定', '取消', 'warning', true)

        },
        // 保存编辑新增单位
        saveProjinfoids: function (props, index, flag) {
            var _that = this;
            var url, msg;
            props.projInfoId = _that.projInfoId;
            props.linkmanInfoId = props.linkmanId;
            if (props.unitInfoId) {
                url = 'rest/unit/edit';
                msg = '单位信息保存成功';
            } else {
                url = 'rest/unit/save'
                msg = '单位信息保存成功';
            }
            var formRef = flag + index;
            var validFun;
            if ((typeof (_that.$refs[formRef].validate)) == 'function') {
                validFun = _that.$refs[formRef].validate
            } else {
                validFun = _that.$refs[formRef][0].validate
            }
            validFun(function (valid) {
                if (valid) { // idcard idtype
                    if (props.idtype) {
                        if (props.idcard) {
                            _that.loading = true;
                            request('', {
                                url: ctx + url,
                                type: 'post',
                                data: props
                            }, function (data) {
                                if (data.success) {
                                    _that.$message({
                                        message: msg,
                                        type: 'success'
                                    });
                                    _that.loading = false;
                                } else {
                                    _that.$message({
                                        message: data.message ? data.message : '保存失败',
                                        type: 'error'
                                    });
                                    _that.loading = false;
                                }
                            }, function (msg) {
                                _that.$message({
                                    message: '服务请求失败',
                                    type: 'error'
                                });
                                _that.loading = false;
                            })
                        } else {
                            _that.$message({
                                message: '请输入单位注册号！',
                                type: 'error'
                            });
                        }
                    } else {
                        _that.$message({
                            message: '请选择注册号类型！',
                            type: 'error'
                        });
                    }
                } else {
                    _that.$message({
                        message: '请完善企业信息！',
                        type: 'error'
                    });
                }
            });
        },
        // 根据项目ID查找关联的单位列表
        getUnitsListByProjInfoId: function () {
            var _that = this;
            _that.loading = true;
            request('', {
                url: ctx + 'rest/unit/list/by/' + _that.projInfoId,
                type: 'get',
            }, function (result) {
                if (result.success) {
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
        // 新增编辑联系人信息
        addLinkman: function (data, parData) {
            var _that = this;
            _that.addEditManModalShow = true;
            _that.getUnitsListByProjInfoId();
            _that.addEditManPerform = parData
            if (_that.projInfoId) {
                if (data) {
                    _that.addEditManModalTitle = '编辑联系人';
                    _that.addEditManModalFlag = 'edit';
                    if (!data.linkmanId) {
                        alertMsg('提示信息', '请选择联系人！', '关闭', 'error', true);
                        return;
                    } else {
                        _that.backDLinkmanInfo(data, parData);
                    }
                } else {
                    _that.addEditManModalTitle = '新增联系人';
                    _that.addEditManform = {};
                    _that.addEditManModalFlag = 'add';
                    if (parData.unitInfoId) {
                        _that.addEditManform.unitInfoId = parData.unitInfoId;
                        _that.addEditManform.unitName = parData.applicant;
                    }
                }
            } else {
                alertMsg('', '请先查出项目信息！', '关闭', 'error', true);
            }
        },
        // 反显联系人信息
        backDLinkmanInfo: function (data, parData) {
            var _that = this;
            if (data.linkmanId) {
                request('', {
                    url: ctx + 'rest/linkman/one/' + data.linkmanId,
                    type: 'get'
                }, function (result) {
                    if (result.success) {
                        _that.addEditManform = result.content;
                        _that.addEditManform.unitInfoId = parData.unitInfoId;
                        _that.addEditManform.unitName = parData.applicant;
                    }
                }, function (msg) {
                    alertMsg('', '服务请求失败', '关闭', 'error', true);
                });
            } else {
                _that.aeaLinkmanInfoList = {};
            }
        },
        // 保存联系人信息
        saveAeaLinkmanInfo: function (linkmanType) {
            var _that = this;
            _that.addEditManform.linkmanType = linkmanType
            _that.$refs['addEditManform'].validate(function (valid) {
                if (valid) {
                    _that.loading = true;
                    var url, msg;
                    if (_that.addEditManModalFlag == 'edit') {
                        url = 'rest/linkman/edit';
                        msg = '编辑联系人信息保存成功';
                    } else {
                        url = 'rest/linkman/save'
                        msg = '新增联系人信息保存成功';
                    }
                    request('', {
                        url: ctx + url,
                        type: 'post',
                        data: _that.addEditManform
                    }, function (result) {
                        if (result.success) {
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
                            message: msg.message ? msg.message : '保存失败！',
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
        resetForm: function (formName) {
            this.$refs[formName].resetFields();
            this.selectMat = '';
        },
        // 联系人模糊查询
        querySearchLinkMan: function (queryString, cb) {
            var _that = this;
            if (typeof (queryString) != 'undefined') queryString = queryString.trim();
            if (queryString != '' && queryString.length >= 1) {
                request('', {
                    url: ctx + 'rest/linkman/list',
                    type: 'get',
                    data: {"keyword": queryString, "projInfoId": _that.projInfoId},
                }, function (result) {
                    if (result.success) {
                        _that.projNameSelect = result.content;
                        _that.projNameSelect.map(function (item) {
                            Vue.set(item, 'value', item.addressName);
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
        // 选取申请人联系人信息
        querySearchApplyLinkMan: function (item) {
            this.applyPersonFrom.applyLinkmanName = item.addressName;
            this.applyPersonFrom.applyLinkmanIdCard = item.addressIdCard;
            this.applyPersonFrom.applyLinkmanTel = item.addressPhone;
            this.applyPersonFrom.applyLinkmanEmail = item.addressMail;
            this.applyPersonFrom.applyLinkmanId = item.addressId;
        },
        querySearchLinkLinkMan: function (item) {
            this.applyPersonFrom.linkLinkmanName = item.addressName;
            this.applyPersonFrom.linkLinkmanIdCard = item.addressIdCard;
            this.applyPersonFrom.linkLinkmanTel = item.addressPhone;
            this.applyPersonFrom.linkLinkmanEmail = item.addressMail;
            this.applyPersonFrom.linkLinkmanId = item.addressId;
        },
        // 联系人信息是否与申请人一致
        copyApplyLinkmanInfo: function (val) {
            if (this.sameAsApplyLink) {
                this.applyPersonFrom.linkLinkmanName = this.applyPersonFrom.applyLinkmanName;
                this.applyPersonFrom.linkLinkmanIdCard = this.applyPersonFrom.applyLinkmanIdCard;
                this.applyPersonFrom.linkLinkmanTel = this.applyPersonFrom.applyLinkmanTel;
                this.applyPersonFrom.linkLinkmanEmail = this.applyPersonFrom.applyLinkmanEmail;
                this.applyPersonFrom.linkLinkmanId = this.applyPersonFrom.applyLinkmanId;
            }
        },
        // 清空已输入申请人联系人信息
        clearApplyLinkManInfo: function (flag) {
            this.sameAsApplyLink = false;
            if (flag) { // 清空申请人信息
                this.applyPersonFrom.applyLinkmanName = '';
                this.applyPersonFrom.applyLinkmanIdCard = '';
                this.applyPersonFrom.applyLinkmanTel = '';
                this.applyPersonFrom.applyLinkmanEmail = '';
                this.applyPersonFrom.applyLinkmanId = '';
            } else { // 清空联系人信息
                this.applyPersonFrom.linkLinkmanName = '';
                this.applyPersonFrom.linkLinkmanIdCard = '';
                this.applyPersonFrom.linkLinkmanTel = '';
                this.applyPersonFrom.linkLinkmanEmail = '';
                this.applyPersonFrom.linkLinkmanId = '';
            }
        },
        //保存个人申办主体的申请人信息和联系人信息
        saveApplyAndLinkmanInfo: function () {
            var _that = this;
            var personalInfos = {
                personalInfos: [
                    {
                        applyOrLinkType: 'apply',
                        linkmanCertNo: _that.applyPersonFrom.applyLinkmanIdCard,
                        linkmanInfoId: _that.applyPersonFrom.applyLinkmanId,
                        linkmanMail: _that.applyPersonFrom.applyLinkmanEmail,
                        linkmanMobilePhone: _that.applyPersonFrom.applyLinkmanTel,
                        linkmanName: _that.applyPersonFrom.applyLinkmanName
                    },
                    {
                        applyOrLinkType: 'link',
                        linkmanCertNo: _that.applyPersonFrom.linkLinkmanIdCard,
                        linkmanInfoId: _that.applyPersonFrom.linkLinkmanId,
                        linkmanMail: _that.applyPersonFrom.linkLinkmanEmail,
                        linkmanMobilePhone: _that.applyPersonFrom.linkLinkmanTel,
                        linkmanName: _that.applyPersonFrom.linkLinkmanName
                    }
                ]
            };
            _that.$refs['applicantPer'].validate(function (valid) {
                if (valid) {
                    _that.loading = true;
                    request('', {
                        url: ctx + 'rest/linkman/save/personal',
                        type: 'post',
                        ContentType: 'application/json',
                        data: JSON.stringify(personalInfos)
                    }, function (result) {
                        if (result.success) {
                            _that.$message({
                                message: '保存成功',
                                type: 'success'
                            });
                            _that.loading = false;
                        }
                    }, function (msg) {
                        _that.$message({
                            message: msg.message ? msg.message : '保存失败！',
                            type: 'error'
                        });
                        _that.loading = false;
                    });
                } else {
                    _that.$message({
                        message: '请输入完整的申请人联系人信息！',
                        type: 'error'
                    });
                    return false;
                }
            });
        },
        // 申办主体信息 企业非企业申报 单位名称模糊查询
        querySearchJiansheName: function (queryString, cb) {
            var _that = this;
            if (typeof (queryString) != 'undefined') queryString = queryString.trim();
            if (queryString != '' && queryString.length >= 2) {
                request('opus-admin', {
                    url: ctx + 'rest/unit/list',
                    type: 'get',
                    data: {"keyword": queryString, "projInfoId": _that.projInfoId},
                }, function (result) {
                    if (result) {
                        _that.projNameSelect = result.content;

                        _that.projNameSelect.map(function (item) {
                            Vue.set(item, 'value', item.applicant);
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
        // 获取并行情形列表id
        getCoreItemsStatusListId: function(){
            var _that = this;
            var stateListLen = _that.stateList.length;
            var selStateIds = [];
            for(var i=0;i<stateListLen;i++){  // 已选并联情形id集合
                if(_that.stateList[i].selectAnswerId!==undefined||_that.stateList[i].selectAnswerId!==null){
                    // selStateIds=[];
                    // return true;
                    if(typeof(_that.stateList[i].selectAnswerId)=='object'&&_that.stateList[i].selectAnswerId.length>0){
                        selStateIds = selStateIds.concat(_that.stateList[i].selectAnswerId);
                    }else if(_that.stateList[i].selectAnswerId !== '') {
                        selStateIds.push(_that.stateList[i].selectAnswerId);
                    }
                }
            }
            selStateIds=selStateIds.filter(function(item, index) {
                //当前元素，在原始数组中的第一个索引==当前索引值，否则返回当前元素
                return selStateIds.indexOf(item, 0) === index
            })
            return selStateIds;
        },
        // 获取事项情形和材料
        getStatusStateMats: function (data, parentId, parentStateId, index, flag, checkFlag) {
            var _that = this;
            var _parentId = parentId ? parentId : 'ROOT';
            var _itemVerId = data.itemVerId;
            var selStateIds = [];
            if (checkFlag == false) {
                var stateListLen = _that.stateList.length;
                // for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
                //     var obj = _that.stateList[i];
                //     if (obj && (obj && (obj.parentStateId == _parentId))) {
                //         _that.stateList.splice(i, 1);
                //         i--
                //     }
                // }
                // // 清空对应情形下所有材料
                // for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
                //     var obj = _that.model.matsTableData[i];
                //     if (obj && (obj.parStateId == _parentId)) {
                //         _that.model.matsTableData.splice(i, 1);
                //         i--
                //     }
                // }
                selStateIds=_that.getCoreItemsStatusListId();
                for (var i = 0;i<stateListLen;i++) { // 清空情形下所对应情形
                    var obj = _that.stateList[i];
                    if (obj&&(obj.parentStateId == _parentId)||(obj&&obj.parentStateId!=null&&(selStateIds.indexOf(obj.parentStateId)==-1))) {
                        _that.stateList.splice(i, 1);
                        i--
                    }
                }
                selStateIds=_that.getCoreItemsStatusListId();
                // 清空对应情形下所有材料
                for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
                    var obj = _that.model.matsTableData[i];
                    if(obj &&obj.itemStateId!=null&& (selStateIds.indexOf(obj.itemStateId)==-1)){
                        _that.model.matsTableData.splice(i, 1);
                        i--
                    }
                }
                return false
            }
            request('opus-admin', {
                url: ctx + 'rest/mats/states/mats/' + _that.itemVerId + '/' + parentId,
                type: 'get',
            }, function (data) {
                if (data.success) {
                    var coreItemsMats = [];
                    if (flag) {//root节点数据
                        _that.matIds = [];
                        _that.stateList = data.content.questionStates;
                        _that.model.matsTableData = _that.unique(data.content.stateMats, 'mats');
                        _that.popStateList = [];
                        _that.stateList.map(function (item) {
                            if (item.answerType != 's' && item.answerType != 'y') {
                                Vue.set(item, 'selValue', []);
                            }
                        })
                        _that.model.matsTableData.map(function (item, index) {
                            _that.matIds.push(item.matId);
                        });
                    } else {
                        if (checkFlag == true) {
                            data.content.questionStates.map(function (item, ind) { // 情形下插入对应的情形
                                if (item.answerType != 's' && item.answerType != 'y') {
                                    Vue.set(item, 'selValue', []);
                                }
                                _that.stateList.splice((index + 1 + ind), 0, item);
                            });
                            // 选择情形后返回的材料列表
                            data.content.stateMats.map(function (item) {
                                if (item._parStateId == 'undefined' || item._parStateId == undefined) {
                                    Vue.set(item, '_parStateId', [parentStateId]);
                                } else {
                                    item._parStateId.push(parentStateId);
                                }
                                if (item._itemVerIds == 'undefined' || item._itemVerIds == undefined) {
                                    Vue.set(item, '_itemVerIds', [_itemVerId]);
                                } else {
                                    item._itemVerId.push(_itemVerId);
                                }
                                item.itemStateId=_parentId;
                                coreItemsMats.push(item)
                            });
                        } else {
                            var stateListLen = _that.stateList.length;
                            // for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
                            //     var obj = _that.stateList[i];
                            //     if (obj && (obj.parentQuestionStateId == parentStateId)) {
                            //         _that.stateList.splice(i, 1);
                            //         i--
                            //     }
                            // }
                            selStateIds=_that.getCoreItemsStatusListId();
                            for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
                                var obj = _that.stateList[i];
                                if(obj&&(obj.parentQuestionStateId == parentStateId)||(obj&&obj.parentStateId!=null&&(selStateIds.indexOf(obj.parentStateId)==-1))){
                                    _that.stateList.splice(i, 1);
                                    i--
                                }
                            }
                            data.content.questionStates.map(function (item, ind) { // 情形下插入对应的情形
                                if (item.answerType != 's' && item.answerType != 'y') {
                                    Vue.set(item, 'selValue', []);
                                }
                                _that.stateList.splice((index + 1 + ind), 0, item);
                            });
                            // 选择情形后返回的材料列表
                            data.content.stateMats.map(function (item) {
                                if (item._parStateId == 'undefined' || item._parStateId == undefined) {
                                    Vue.set(item, '_parStateId', [parentStateId]);
                                } else {
                                    item._parStateId.push(parentStateId);
                                }
                                if (item._itemVerIds == 'undefined' || item._itemVerIds == undefined) {
                                    Vue.set(item, '_itemVerIds', [_itemVerId]);
                                } else {
                                    item._itemVerId.push(_itemVerId);
                                }
                                item.itemStateId=_parentId;
                                coreItemsMats.push(item)
                            });
                            // for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
                            //     var obj = _that.model.matsTableData[i];
                            //     if (obj._parStateId == 'undefined' || obj._parStateId == undefined) {
                            //         Vue.set(obj, '_parStateId', []);
                            //     } else {
                            //         if (obj && (obj._parStateId.indexOf(parentStateId) > -1) && (obj._parStateId.length == 1)) {
                            //
                            //             obj._parStateId = obj._parStateId.filter(function (item) {
                            //                 return item != parentStateId;
                            //             });
                            //             _that.model.matsTableData.splice(i, 1);
                            //             i--
                            //         }
                            //     }
                            // }
                            selStateIds=_that.getCoreItemsStatusListId();
                            for (var i = 0; i < _that.model.matsTableData.length; i++) { // 清空情形下所对应材料
                                var obj = _that.model.matsTableData[i];
                                if(obj._parStateId=='undefined'||obj._parStateId==undefined){
                                    Vue.set(obj, '_parStateId', []);
                                }else {
                                    // if (obj && (obj._parStateId.indexOf(parentStateId)>-1)&&(obj._parStateId.length==1)) {
                                    if(obj &&obj.itemStateId!=null&&(selStateIds.indexOf(obj.itemStateId)==-1)&&(obj._parStateId.length==1)){
                                        obj._parStateId = obj._parStateId.filter(function(item) {
                                            return item != parentStateId;
                                        });
                                        _that.model.matsTableData.splice(i, 1);
                                        i--
                                    }
                                }
                            }
                        }
                    }
                    coreItemsMats = _that.unique(coreItemsMats, 'mats');
                    _that.model.matsTableData = _that.model.matsTableData.concat(coreItemsMats);
                    _that.matIds = [];
                    var matinstIdsObj = [];
                    _that.model.matsTableData.map(function (item) {
                        if (item.children == 'undefined' || item.children == undefined) {
                            Vue.set(item, 'children', []);
                        }
                        if (item.matinstId == 'undefined' || item.matinstId == undefined) {
                            Vue.set(item, 'matinstId', '');
                        }
                        if (item.getPaper == 'undefined' || item.getPaper == undefined) {
                            Vue.set(item, 'getPaper', '');
                        }
                        if (item.getCopy == 'undefined' || item.getCopy == undefined) {
                            Vue.set(item, 'getCopy', '');
                        }
                        if (item.realPaperCount == 'undefined' || item.realPaperCount == undefined) {
                            Vue.set(item, 'realPaperCount', item.duePaperCount);
                        }
                        if (item.realCopyCount == 'undefined' || item.realCopyCount == undefined) {
                            Vue.set(item, 'realCopyCount', item.dueCopyCount);
                        }
                        matinstIdsObj.push(item.matinstId);
                        _that.matIds.push(item.matId);

                    });
                    _that.matinstIds = matinstIdsObj.filter(function (current) {
                        return current !== null && current !== undefined && current !== '';
                    })
                    _that.ifMatsSelAll();
                } else {
                    _that.$message({
                        message: '获取情形材料失败',
                        type: 'error'
                    });
                }
            }, function (msg) {
                _that.$message({
                    message: '获取情形材料失败',
                    type: 'error'
                });
            });
        },
        // 判断是否材料全选
        ifMatsSelAll: function () {
            var getCopyCount = 0;
            var _that = this;
            var getPaperCount = 0;
            _that.model.matsTableData.map(function (item) {
                if (item.getCopy == true) {
                    getCopyCount++
                }
                if (item.getPaper == true) {
                    getPaperCount++
                }
            })
            if (getCopyCount == _that.model.matsTableData.length) {
                _that.getCopyAll = true;
            } else {
                _that.getCopyAll = '';
            }
            if (getPaperCount == _that.model.matsTableData.length) {
                _that.getPaperAll = true;
            } else {
                _that.getPaperAll = '';
            }
        },
        toTree: function (data) {
            var result = []
            if (!Array.isArray(data)) {
                return result
            }
            data.forEach(function (item) {
                delete item.children;
            });
            var map = {};
            data.forEach(function (item) {
                map[item.id] = item;
            });
            data.forEach(function (item) {
                var parent = map[item.pId];
                if (parent) {
                    (parent.children || (parent.children = [])).push(item);
                } else {
                    result.push(item);
                }
            });
            return result;
        },
        // 材料 事项去重
        unique: function (arr, flag) {
            var result = {};
            var finalResult = [];
            for (var i = 0; i < arr.length; i++) {
                if (flag == 'mats') {
                    result[arr[i].matId] = arr[i];
                } else if (flag == 'stage') {
                    result[arr[i].itemVerId] = arr[i];
                } else if (flag == 'file') {
                    result[arr[i].name] = arr[i];
                }
            }
            for (item in result) {
                finalResult.push(result[item]);
            }
            return finalResult;
        },
        // 判断事项checkbox是否可勾选
        checkboxInit: function (row) {
            if (row.isDone == 1)
                return 0;//不可勾选
            else
                return 1;//可勾选
        },
        // 事项默认全选
        toggleSelection: function (rows, tableRef) {
            var _that = this;
            if (rows) {
                rows.forEach(function (row) {
                    if (row.isDone != 1) {
                        _that.$refs[tableRef].toggleRowSelection(row);
                    }
                });
            } else {
                _that.$refs[tableRef].clearSelection();
            }
        },
        // 文件上传 change事件
        fileChange: function (file, fileList) {
            var _that = this;
            _that.AllFileListId = [];
            if (fileList.length > 0) {
                this.AllFileList = this.unique(fileList, 'file');
                this.AllFileList.map(function (item) {
                    _that.AllFileListId.push(item.name);
                });
                _that.checkedSelFlie = _that.AllFileListId;
            }
        },
        // 文件上传 submit事件
        submitUpload: function () {
            this.fileData = new FormData();
            this.$refs.uploadFile.submit();
            var _that = this;
            this.fileData.append("projInfoId", _that.projInfoId);
            this.fileData.append("matIds", _that.matIds);
            this.fileData.append("matinstIds", _that.matinstIds);
            this.fileData.append("unitInfoId", _that.rootUnitInfoId);
            this.uploadLogo = "1";
            if (_that.checkedSelFlie.length == 0) {
                alertMsg('', '请选择需要自动分拣的文件', '关闭', 'warning', true);
                return false;
            } else {
                _that.loadingFile = true;
                axios.post(ctx + 'rest/mats/att/upload/auto', _that.fileData).then(function (res) {
                    _that.checkedSelFlie = [];
                    _that.AllFileList = [];
                    _that.loadingFile = false;
                    _that.$refs.uploadFile.clearFiles();
                    _that.checkAll = false;
                    var matinstIdsObj = [];
                    res.data.content.map(function (item) {
                        _that.showFileListKey.push(item.matId);
                        _that.model.matsTableData.map(function (matItem) {
                            if (item.matId == matItem.matId) {
                                matItem.matinstId = item.matinstId;
                                matItem.children = matItem.children.concat(item.fileList);
                            }
                            matinstIdsObj.push(matItem.matinstId);
                        });
                    });
                    _that.matinstIds = _that.matinstIds = matinstIdsObj.filter(function (current) {
                        return current !== null && current !== undefined && current !== '';
                    })
                }).catch(function (error) {
                    if (error.response) {
                        // content.onError('配时文件上传失败(' + error.response.status + ')，' + error.response.data);
                    } else if (error.request) {
                        // content.onError('配时文件上传失败，服务器端无响应');
                    } else {
                        // content.onError('配时文件上传失败，请求封装失败');
                    }
                    _that.loadingFile = false;
                });
            }

        },
        myUploadFile: function (file) {
            var _that = this;
            _that.checkedSelFlie.map(function (item) {
                if (item == file.file.name) {
                    _that.fileData.append('file', file.file);
                }
            });
        },
        handleCheckAllChange: function (val) {
            this.checkedSelFlie = val ? this.AllFileListId : [];
        },
        handleCheckedCitiesChange: function (value) {
            var checkedCount = value.length;
            this.checkAll = checkedCount === this.AllFileListId.length;
            this.checkedSelFlie = value;
        },
        showMatFiles: function (matId, docType) {  // bsc/att/listAttLinkAndDetailNoPage.do
            var _that = this;
            // var tempwindow = window.open('_blank');
            request('', {
                url: ctx + '/bsc/att/listAttLinkAndDetailNoPage.do',
                type: 'post',
                data: {
                    tableName: 'AEA_ITEM_MAT',
                    pkName: docType,
                    recordId: matId,
                    attType: null,
                },
            }, function (result) {
                if (result != null && result.length > 1) {

                    var trHtml = '';
                    _that.showMatFilesDialogShow = true;
                    $.each(result, function (i, file) {
                        trHtml += '<div class="td-cust clearfix" data-type="file" data-key="' + file.detailId + '"  data-format="' + file.attFormat + '">\n' +
                            '                    <div class="file-image fl"><img src="' + _that.getFileIcon(file.attName) + '" /></div>\n' +
                            '                    <div class="file-name fl">\n' +
                            '                        <a href="' + _that.kpFileOpenEvent(file.detailId) + '" target="_blank" title="' + file.attName + '">' + _that.cutString(file.attName, 50) + '</a>\n' +
                            '                    </div>\n' +
                            '                </div>';
                    });
                    _that.dialogHtml = trHtml;
                } else if (result.length == 1) {
                    var url = ctx + '/file/showFile.do?detailId=' + result[0].detailId;
                    window.open.location = ctx + '/file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url);
                } else {
                    _that.showMatFilesDialogShow = true;
                    _that.dialogHtml = '无模板';
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },
        kpFileOpenEvent: function (detailId) {
            var url = ctx + 'file/showFile.do?detailId=' + detailId;
            return ctx + 'file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url);
        },
        getFileIcon: function (fm) {
            var fileRegexs = {
                text: (/\.(txt|md)$/i),
                word: (/\.(docx|doc|wps)$/i),
                pdf: (/\.pdf$/i),
                picture: (/\.(png|jpg|jpeg|bmp|gif|svg|tiff)$/i),
                excel: (/\.(xls|xlsx)$/i),
                music: (/\.(mp3|wav|cad|wma|ra|midi|ogg|ape|flac|aac)$/i),
                video: (/\.(rm|rmvb|avi|flv|mp4|mkv|wmv|3gp|amv|mpeg1-4|mov|mtv|dat|dmv)$/i),
                code: (/\.(java|js|css|html)/i),
                exe: (/\.exe/i),
                psd: (/\.psd$/i),
                zip: (/\.(zip|rar|7z|gz|tar|bz2)$/i)
            };
            for (var key in fileRegexs) {
                if (fileRegexs[key].test(fm)) {
                    var icon = ctx + 'approve/css/att/images/file-view/file_' + key + '.png';
                    return icon;
                }
            }
            return ctx + 'approve/css/att/images/file-view/file_empty.png';
        },
        cutString: function (str, len) {
            //length属性读出来的汉字长度为1
            if (str.length * 2 <= len) {
                return str;
            }
            var strlen = 0;
            var s = "";
            for (var i = 0; i < str.length; i++) {
                s = s + str.charAt(i);
                if (str.charCodeAt(i) > 128) {
                    strlen = strlen + 2;
                    if (strlen >= len) {
                        return s.substring(0, s.length - 1) + "...";
                    }
                } else {
                    strlen = strlen + 1;
                    if (strlen >= len) {
                        return s.substring(0, s.length - 2) + "...";
                    }
                }
            }
            return s;
        },
        showUploadWindow: function (data) { // 展示文件上传下载弹窗
            var _that = this;
            _that.showUploadWindowFlag = true;
            _that.selMatRowData = data;
            _that.selMatinstId = data.matinstId ? data.matinstId : '',
                _that.showUploadWindowTitle = '材料附件 - ' + data.matName
            _that.getFileListWin(data.matinstId, data);
        },
        // 获取已上传文件列表
        getFileListWin: function (matinstId, rowData) {
            var _that = this;
            request('', {
                url: ctx + 'rest/mats/att/list',
                type: 'get',
                data: {matinstId: matinstId}
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
            var detailId = data.matId;
            var flashAttributes = '';
            request('', {
                url: ctx + 'rest/mats/att/preview',
                type: 'get',
                data: {detailId: detailId, flashAttributes: flashAttributes}
            }, function (res) {
                if (res.success) {
                    _that.uploadTableData = res.content;
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
        // 勾选电子件
        selectionFileChange: function (val) {
            this.fileSelectionList = val;
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
            this.fileWinData.append("matId", rowData.matId);
            this.fileWinData.append("matinstCode", rowData.matCode);
            this.fileWinData.append("matinstName", rowData.matName ? rowData.matName : '');
            this.fileWinData.append("projInfoId", _that.projInfoId);
            this.fileWinData.append("unitInfoId", _that.rootUnitInfoId);
            this.fileWinData.append("matinstId", rowData.matinstId ? rowData.matinstId : '');
            _that.loadingFileWin = true;
            axios.post(ctx + 'rest/mats/att/upload', _that.fileWinData).then(function (res) {
                Vue.set(file.file, 'matinstId', res.data.content)
                _that.selMatinstId = res.data.content;
                _that.getFileListWin(res.data.content, rowData);
                var matinstIdsObj = [];
                _that.model.matsTableData.map(function (item) {
                    if (item.matId == rowData.matId) {
                        item.matinstId = res.data.content;
                        _that.showFileListKey.push(item.matId)
                    }
                    matinstIdsObj.push(item.matinstId);
                });
                _that.matinstIds = matinstIdsObj.filter(function (current) {
                    return current !== null && current !== undefined && current !== '';
                })
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
                detailIds.push(item.matinstId);
            });
            detailIds = detailIds.join(',')
            request('', {
                url: ctx + 'rest/mats/matinst/delete',
                type: 'post',
                data: {matinstId: detailIds}
            }, function (res) {
                if (res.success) {
                    _that.getFileListWin(_that.selMatRowData.matinstId, _that.selMatRowData);
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
        // 获取省份信息
        queryProvince: function () {
            var _that = this;
            request('', {
                url: ctx + 'rest/apply/province',
                type: 'get'
            }, function (data) {
                _that.provinceList = data.content;
                _that.queryCityData(0);
            })
        },
        //项目树右键事件
        showRightBtnList: function (e, data) {
            this.treeBtnTop = e.clientY;
            this.treeBtnLeft = e.clientX;
            this.treeRightBtnList = true;
            this.parentProjInfoId = data.id;

        },
        // 新增子工程
        addChildChildProj: function () {
            this.addChildProjShow = true;
            this.treeRightBtnList = false;
        },
        // 保存新增子工程
        saveAddChildProj: function () {
            var _that = this;
            var parmas = {
                isSecond: false,
                parentProjInfoId: _that.parentProjInfoId,
                projName: _that.childProjName,
                foreignRemark: _that.childProjText
            }
            request('', {
                url: ctx + 'rest/project/add/child',
                type: 'post',
                data: parmas
            }, function (data) {
                if (data.success) {
                    _that.$message({
                        message: '新增子项目成功',
                        type: 'success'
                    });
                    _that.showProjTree();
                    _that.addChildProjShow = false;
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },
        // 关闭工程树窗口回调
        clearTreeChildInfo: function () {
            this.childProjName = '';
            this.childProjText = '';
            this.addChildProjShow = false;
            this.treeRightBtnList = false;
        },
        // 删除子工程
        delChildChildProj: function () {  // rest/project/delete/child
            var _that = this;
            request('', {
                url: ctx + 'rest/project/delete/child',
                type: 'post',
                data: {childProjInfoId: _that.parentProjInfoId}
            }, function (data) {
                if (data.success) {
                    _that.$message({
                        message: '项目删除成功',
                        type: 'success'
                    });
                } else {
                    _that.$message({
                        message: data.message == '1' ? '该项目工程不能删除' : data.message == '2' ? '已申报的项目不能删除' : '删除失败',
                        type: 'success'
                    });
                }
                _that.showProjTree();
                _that.addChildProjShow = false;
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
                _that.addChildProjShow = false;
            });
        },
        // 选中子项目
        selChildProj: function (data) {
            this.projInfoId = data.id;
            this.projName = data.projName
            this.linkQuery();
            this.projectTreeInfoModal = false;
        },
        // 根据单位id获取关联联系人
        getLinkManListByUnitId: function (item) {
            var _that = this;
            _that.loadingLinkMan = true;
            request('', {
                url: ctx + 'rest/linkman/list',
                type: 'get',
                data: {
                    keyword: '',
                    unitInfoId: item.unitInfoId
                }
            }, function (data) {
                if (data.success) {
                    _that.loadingLinkMan = false;
                    item.aeaLinkmanInfoList = data.content;
                }
            }, function (msg) {
                _that.$message({
                    message: '获取单位关联联系人失败',
                    type: 'error'
                });
            });
        },
        // 删除联系人
        delLinkman: function (data, parentData, ind) {
            var _that = this;
            if (!data.addressId) {
                alertMsg('提示信息', '联系人ID为空', '关闭', 'warning', true);
                return;
            } else {
                confirmMsg('此操作影响：', '确定要删除该联系人吗？', function () {
                    request('', {
                        url: ctx + 'rest/linkman/delete/by/unit',
                        type: 'post',
                        data: {linkmanInfoId: data.addressId, unitInfoId: parentData.unitInfoId}
                    }, function (result) {
                        if (result.success) {
                            _that.$message({
                                message: '联系人删除成功',
                                type: 'success'
                            });
                        } else {
                            _that.$message({
                                message: result.message ? result.message : '联系人删除失败',
                                type: 'error'
                            });
                        }
                    }, function (msg) {
                        _that.$message({
                            message: msg.message ? msg.message : '删除失败',
                            type: 'error'
                        });
                    })
                }, function () {
                }, '确定', '取消', 'warning', true)
            }
        },
        // 选择企业联系人信息
        selLinkman: function (data, ind1, flag) {
            var _that = this;
            if (data) {
                if (flag == false) {
                    _that.agentUnits[ind1].linkmanName = data.addressName;
                    _that.agentUnits[ind1].linkmanId = data.addressId;
                    _that.agentUnits[ind1].linkmanMail = data.addressMail;
                    _that.agentUnits[ind1].linkmanCertNo = data.addressIdCard;
                    _that.agentUnits[ind1].linkmanMobilePhone = data.addressPhone;

                } else {
                    _that.jiansheFrom[ind1].linkmanName = data.addressName;
                    _that.jiansheFrom[ind1].linkmanId = data.addressId;
                    _that.jiansheFrom[ind1].linkmanMail = data.addressMail;
                    _that.jiansheFrom[ind1].linkmanCertNo = data.addressIdCard;
                    _that.jiansheFrom[ind1].linkmanMobilePhone = data.addressPhone;

                }
            }
        },
        // 材料全选
        checkAllMatChange: function (val, flag) {
            if (val == false) {
                val = '';
            }
            var _that = this;
            // this.checkedCities[[index]] = val ? this.id[[index]] : []
            _that.model.matsTableData.map(function (item) {
                if (flag == 'paper') {
                    item.getPaper = val;
                    _that.getPaperAll = val;
                } else {
                    item.getCopy = val;
                    _that.getCopyAll = val;
                }
            });
            // this.isIndeterminate[index] = false
        },
        // 材料单选
        checkedMatChange: function (val, index, flag) {
            var _that = this;
            if (val == false) {
                val = '';
            }
            if (flag == 'paper') {
                _that.model.matsTableData[index].getPaper = val;
                _that.getPaperAll = val;
            } else {
                _that.model.matsTableData[index].getCopy = val;
                _that.getCopyAll = val;
            }
        },
        // 意见窗口
        showCommentDialog: function (val) {
            var _that = this;
            _that.buttonStyle = val;
            var stateSel = _that.stateList
            var stateSelLen = stateSel.length;
            _that.stateIds = [];
            for (var i = 0; i < stateSelLen; i++) {
                if (stateSel[i].mustAnswer == 1) {
                    if (stateSel[i].selectAnswerId == null || stateSel[i].selectAnswerId == '') {
                        _that.stateIds = [];
                        alertMsg('', '请选择情形：' + stateSel[i].stateName, '关闭', 'error', true);
                        return true;
                    } else {
                        _that.stateIds.push(stateSel[i].itemStateId);
                    }
                }
            }
            var jiansheUnitFormEleLen = $('.save-jansheUnit-info').length;
            var jinbanUnitFormEleLen = $('.save-jinbanUnit-info').length;
            var applicantPerFlag = true;// 个人申报校验通过
            var jiansheUnitFlag = true;// 企业申报校验通过
            var jinbanUnitFlag = true;// 经办申报校验通过
            var perUnitMsg = "";
            // 判断个人申报主体必填是否已填
            if (_that.applySubjectType == 0) {
                _that.$refs['applicantPer'].validate(function (valid) {
                    if (valid) {
                        applicantPerFlag = true;
                    } else {
                        applicantPerFlag = false;
                        perUnitMsg = "请完善申办主体个人信息";
                        return false;
                    }
                });
            }
            // 判断建设单位必填是否已填
            if (jiansheUnitFormEleLen > 0) {
                for (var i = 0; i < jiansheUnitFormEleLen; i++) {
                    var formRef = 'jianshe_' + i;
                    var validFun;
                    if ((typeof (_that.$refs[formRef].validate)) == 'function') {
                        validFun = _that.$refs[formRef].validate
                    } else {
                        validFun = _that.$refs[formRef][0].validate
                    }
                    validFun(function (valid) {
                        if (valid) {
                            jiansheUnitFlag = true;
                        } else {
                            jiansheUnitFlag = false;
                            perUnitMsg = "请完善申办主体建设单位信息"
                            return false;
                        }
                    });
                }
            } else if (jiansheUnitFormEleLen == 0 && _that.applySubjectType != 0) {
                jiansheUnitFlag = false;
                perUnitMsg = "请添加申办主体建设单位信息"
            }
            // 判断经办单位必填是否已填
            if (jinbanUnitFormEleLen > 0) {
                for (var i = 0; i < jinbanUnitFormEleLen; i++) {
                    var formRef = 'jingban_ ' + i;
                    var validFun;
                    console.log(typeof (_that.$refs[formRef].validate))
                    if ((typeof (_that.$refs[formRef].validate)) == 'function') {
                        validFun = _that.$refs[formRef].validate
                    } else {
                        validFun = _that.$refs[formRef][0].validate
                    }
                    validFun(function (valid) {
                        if (valid) {
                            jinbanUnitFlag = true;
                        } else {
                            jinbanUnitFlag = false;
                            perUnitMsg = "请完善申办主体经办单位信息"
                            return false;
                        }
                    });
                }
            } else if (jinbanUnitFormEleLen == 0 && _that.applySubjectType != 0 && _that.agentChecked) {
                jinbanUnitFlag = false;
                perUnitMsg = "请添加申办主体经办单位信息"
            }
            if (applicantPerFlag && jiansheUnitFlag && jinbanUnitFlag) {
                _that.$refs['formTest'].validate(function (valid) {
                    if (valid) {
                        if (_that.smsInfoId) {
                            _that.getCommnetList();
                            _that.getMatinstIds();
                            if (_that.buttonStyle == '3') {
                                _that.commentTitle = '不受理意见对话框'
                                _that.showMatList = true;
                            } else {
                                _that.commentTitle = '收件意见对话框'
                                _that.showMatList = false;
                            }
                            _that.submitCommentsFlag = true;
                        } else {
                            alertMsg('', '请保存取证人信息', '关闭', 'error', true);
                        }

                    } else {
                        alertMsg('', '请选择材料', '关闭', 'error', true);
                    }
                });
            } else {
                alertMsg('', perUnitMsg, '关闭', 'error', true);
            }
        },
        /********************** 意见编辑、修改、删除、批量删除、新增 *******************************/
        getCommnetList: function () {
            var _that = this;
            request('', {
                url: ctx + 'rest/comment/getAllActiveUserOpinions',
                type: 'get'
            }, function (result) {
                if (result.success) {
                    _that.commentsList = result.content;
                }
            }, function (msg) {
                _that.$message({
                    message: msg.message ? msg.message : '保存失败！',
                    type: 'error'
                });
                _that.loading = false;
            });
        },
        getSelectedComment: function (val) {
            var _that = this;
            //**********以下各项目组根据需求启动对应的方法*******
            //替换已输入的意见
            _that.comments = val;
            //在已输入意见追加
            //_that.comments =_that.comments+ val;

        },
        //保存常用意见
        setUseComment: function () {
            var _that = this;
            if (_that.comments == '') {

            } else {
                request('', {
                    url: ctx + 'rest/comment/saveUserOpinion',
                    type: 'post',
                    data: {'message': _that.comments}
                }, function (result) {
                    if (result.success) {
                        _that.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                        _that.getCommnetList();
                    }
                }, function (msg) {
                    _that.$message({
                        message: msg.message ? msg.message : '保存失败！',
                        type: 'error'
                    });
                    _that.loading = false;
                });
            }

        },
        //单项删除审批意见
        deleteUseComment: function (val) {
            var _that = this;
            request('', {
                url: ctx + 'rest/comment/deleteUserOpinion/' + val,
                type: 'DELETE'
            }, function (result) {
                if (result.success) {
                    _that.$message({
                        message: '删除成功',
                        type: 'success'
                    });
                    _that.getCommnetList();
                }
            }, function (msg) {
                _that.$message({
                    message: msg.message ? msg.message : '保存失败！',
                    type: 'error'
                });
                _that.loading = false;
            });
        },
        //批量删除意见
        batchDeleteUserOption: function () {
            var _that = this;
            var optionIds = ""
            if (_that.multipleOptionSelection.length == 0) {
                this.$message({
                    type: 'warning',
                    message: '请选择要删除的项'
                });
            } else {
                this.$confirm('即将删除多个, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    $.each(_that.multipleOptionSelection, function (index, item) {
                        optionIds = optionIds + item.opinionId + ","
                    })
                    optionIds = optionIds.substr(0, optionIds.length - 1);
                debugger;
                    request('', {
                        url: ctx + 'rest/comment/batchDeleteUserOpinion',
                        type: 'DELETE',
                        data: {'opinionIds': opinionIds}
                    }, function (result) {
                        if (result.success) {
                            _that.$message({
                                message: '批量删除成功',
                                type: 'success'
                            });
                            _that.getCommnetList();
                        }
                    }, function (msg) {
                        _that.$message({
                            message: msg.message ? msg.message : '保存失败！',
                            type: 'error'
                        });
                        _that.loading = false;
                    });
                }).catch(function () {
                    _that.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }


        },
        //编辑单个意见弹框
        getUserOptionById: function (val) {
            var _that = this;
            _that.editCommentForm = $.extend({}, _that.editCommentForm, val);
            _that.submitCommentsFlag2 = true;
        },
        //保存编辑后的意见
        editUserOption: function () {
            var _that = this;
            _that.$refs['editCommentForm'].validate(function (valid) {
                if (valid) {
                    request('', {
                        url: ctx + 'rest/comment/editUserOpinion',
                        type: 'put',
                        data: _that.editCommentForm
                    }, function (result) {
                        if (result.success) {
                            _that.$message({
                                message: '保存成功',
                                type: 'success'
                            });
                            _that.getCommnetList();
                            _that.submitCommentsFlag2 = false;
                        }
                    }, function (msg) {
                        _that.$message({
                            message: msg.message ? msg.message : '保存失败！',
                            type: 'error'
                        });
                        _that.loading = false;
                    });
                } else {
                    _that.$message({
                        message: '请检查参数',
                        type: 'error'
                    });
                    return false;
                }
            });

        },
        handleSelectionChange: function (val) {
            var _that = this;
            _that.multipleOptionSelection = val;
        },
        //不收件弹窗填写选择的材料名称
        getSelectedMat: function (val) {
            var _this = this;
            //默认追加；如果需要替换， 将以下两行替换为  _this.comments=val;
            var memo = _this.comments;
            _this.comments = memo + val + ":";
        },
        // 选择省份
        queryCityData: function (index) {
            this.cityList = this.provinceList[index].cityList;
            this.queryAreaData(0);
            this.getResultForm.addresseeProvince = this.provinceList[index].code;
        },
        // 选择市地区
        queryAreaData: function (index) {
            this.countyList = this.cityList[index].areaList;
            this.getResultForm.addresseeCity = this.cityList[index].code;
            this.queryCountyData(this.countyList[0].code);
        },
        // 选择地区
        queryCountyData: function (code) {
            this.getResultForm.addresseeCounty = code;
        },
        // 保存取证人信息
        saveSmsinfo: function () {
            var _that = this;
            _that.getResultForm.id = _that.smsInfoId;
            _that.$refs['resultForm'].validate(function (valid) {
                if (valid) {
                    _that.loading = true;
                    request('', {
                        url: ctx + 'rest/apply/save/or/update/smsinfo',
                        type: 'post',
                        data: _that.getResultForm
                    }, function (data) {
                        if (data.success) {
                            _that.smsInfoId = data.content;
                            _that.loading = false;
                            _that.$message({
                                message: '保存成功',
                                type: 'success'
                            });
                        } else {
                            _that.loading = false;
                            _that.$message({
                                message: data.message ? data.message : '保存领件人失败',
                                type: 'error'
                            });
                        }
                    }, function (msg) {
                        _that.loading = false;
                        alertMsg('', '保存领件人失败', '关闭', 'error', true);
                    });
                }
            });
        },
        // 切换领证模式
        changeReceiveMode: function (val) {
            if (val == 0) {
                this.getResultForm.smsType = 2;
                this.queryProvince();
            }
        },
        // 选取领证信息
        queryGetResultMan: function (item) {
            this.getResultForm.addresseeName = item.addressName;
            this.getResultForm.addresseePhone = item.addressPhone;
            this.getResultForm.addresseeIdcard = item.addressIdCard;
        },
        // 根据材料定义id获取材料实例id
        getMatinstIds: function () {
            var _that = this;
            var matCountVos = [];
            var selMatIds = [];
            _that.model.matsTableData.map(function (item) {
                var copyCnt = 0;
                var paperCnt = 0;
                if (item.getCopy == true) {
                    copyCnt = item.realCopyCount;
                }
                if (item.getPaper == true) {
                    paperCnt = item.realPaperCount;
                }
                if (item.getCopy == true || item.getPaper == true) {
                    selMatIds.push(item.matId)
                    matCountVos.push({
                        copyCnt: copyCnt,
                        paperCnt: paperCnt,
                        matId: item.matId,
                    });
                }
            });
            if (matCountVos.length == 0) {
                return false;
            }
            var parmas = {
                matCountVos: matCountVos,
                projInfoId: _that.projInfoId,
                unitInfoId: _that.rootUnitInfoId
            }
            request('', {
                url: ctx + 'rest/mats/matinst/batch/save',
                type: 'post',
                ContentType: 'application/json',
                data: JSON.stringify(parmas)
            }, function (data) {
                if (data.success) {
                    data.content.map(function (item) {
                        _that.selMatinstIds = _that.selMatinstIds.concat(item.matinstIds);
                    });
                }
            }, function (msg) {
                _that.$message({
                    message: '服务请求失败',
                    type: 'error'
                });
            });
        },
        //查询回执列表及分类
        queryReceiveList: function (applyinstIds) {
            var _that = this;
            //var applyinstId = '480bc49b-2f58-4cd8-ba4b-b2458d0684ab,144569b5-c678-48ed-b42f-9a7598bcae3e'
            request('', {
                url: ctx + 'rest/receive/getStageOrSeriesReceiveByApplyinstIds',
                type: 'get',
                data: {'applyinstIds': applyinstIds}
            }, function (res) {
                if (res.success) {
                    _that.receiveList = res.content;
                    //显示列表弹框
                    _that.receiveModalShow = true;
                }
            }, function (msg) {
                alertMsg('', '服务请求失败', '关闭', 'error', true);
            });
        },
        //打印回执
        printReceive: function (row) {
            console.log(row);
            var url = ctx + 'rest/receive/toPrintPage/' + row.receiveId;
            setTimeout(function () {
                window.open(ctx + 'rest/ntko/ntkoOpenWin?jumpUrl=' + encodeURIComponent(url));
            }, 500);
        },
        // 发起申报
        startSingleApprove: function () {
            var projInfoIds = [], handleUnitIds = []; // 项目ID集合 经办单位ID集合
            var branchOrgMap = [];// 分局承办
            var _that = this;
            if (!_that.comments) {
                alertMsg('', '请填写收件意见', '关闭', 'error', true);
                return false;
            }
            var buildProjUnitMap = [], maptemp = {'projectInfoId': '', 'unitIds': []}, unitIdTemp = [];
            projInfoIds.push(_that.projInfoId);
            _that.agentUnits.map(function (item) {
                handleUnitIds.push(item.unitInfoId);
            });
            _that.jiansheFrom.map(function (item) {
                unitIdTemp.push(item.unitInfoId);
            });
            maptemp.projectInfoId = _that.projInfoId;
            maptemp.unitIds = unitIdTemp;
            //maptemp[_that.projInfoId] = unitIdTemp;
            buildProjUnitMap.push(maptemp);

            if (_that.isBranch) {//勾选了分局承办
                branchOrgMap.push({
                    itemVerId: _that.itemVerId,
                    branchOrg: _that.branchOrg,
                });
            }
            if (branchOrgMap.length > 0) {
                branchOrgMap = JSON.stringify(branchOrgMap)
            } else {
                branchOrgMap = ''
            }
            //选择的情形
            var parmas = {
                applyLinkmanId: _that.rootApplyLinkmanId,
                applySource: 'win',
                applySubject: _that.applySubjectType,
                applyinstId: _that.applyinstId,
                branchOrgMap: branchOrgMap,
                comments: _that.comments,
                handleUnitIds: handleUnitIds,
                itemVerId: _that.itemVerId,
                linkmanInfoId: _that.rootLinkmanInfoId,
                matinstsIds: _that.selMatinstIds,
                projInfoIds: projInfoIds,
                smsInfoId: _that.smsInfoId,
                buildProjUnitMap: buildProjUnitMap,
                stateIds: _that.stateIds,
            }
            _that.progressDialogVisible = true;
            _that.submitCommentsFlag = false;
            var url = 'rest/apply/series/processflow/start';//发起申报
            if (_that.buttonStyle == '1') {//打印回执
                url = 'rest/apply/series/inst';
            } else if (_that.buttonStyle == '3') {//不受理
                url = 'rest/apply/series/inadmissible';
            }
            _that.progressIntervalStop = false;
            _that.setUploadPercentage();
            request('', {
                url: ctx + url,
                type: 'post',
                ContentType: 'application/json',
                timeout: 1000000,
                data: JSON.stringify(parmas)
            }, function (res) {
                if (res.success) {
                    _that.uploadPercentage = 100;
                    _that.progressIntervalStop = true;
                    _that.progressStus = 'success';
                    var applyinstId = res.content;
                    _that.applyinstId = res.content
                    _that.$message({
                        message: '操作成功',
                        type: 'success'
                    });
                    setTimeout(function () {
                        _that.progressDialogVisible = false;
                        _that.queryReceiveList(applyinstId);
                    }, 300);
                } else {
                    _that.progressIntervalStop = true;
                    _that.progressStus = 'error';
                    _that.$message({
                        message: res.message ? res.message : errorMsg,
                        type: 'error'
                    });
                    setTimeout(function () {
                        _that.progressDialogVisible = false;
                    }, 300);
                }
            }, function (msg) {
                alertMsg('', '服务请求失败', '关闭', 'error', true);
                _that.progressDialogVisible = false;
                _that.progressIntervalStop = true;
                _that.progressStus = 'error';
            });
        },
        //显示机构树
        showOrgTree: function (val, data) {
            console.log(data);
            if (val == false) {
                return true
            }
            var _that = this;
            var parentOrgId = data.orgId ? data.orgId : '';
            request('', {
                url: ctx + 'rest/org/tree',
                type: 'get',
                data: {
                    dataType: '',
                    orgName: '',
                    parentOrgId: parentOrgId
                }
            }, function (result) {
                if (result.success) {
                    _that.orgTreeInfoModal = true;
                    _that.orgTree = _that.toTree(result.content);
                } else {
                    _that.$message({
                        message: '项目工程查询失败！',
                        type: 'error'
                    });
                }
            }, function (msg) {
                alertMsg('', '网络加载失败！', '关闭', 'error', true);
            })
        },
        selChildOrg: function (rowData) {
            this.branchOrg = rowData.id;
            this.itemBasicInfo.orgName = rowData.name;
            this.orgTreeInfoModal = false;
        },
        // 刷新页面
        reloadPage: function () {
            if (this.submitCommentsType == 0 || this.submitCommentsType == 3) {
                window.location.reload()
            }
        },
        // 设置进度条的值
        setUploadPercentage: function () {
            var _that = this;
            _that.uploadPercentage = 0;
            var interval = setInterval(function () {
                _that.uploadPercentage += 3;
                if (_that.uploadPercentage >= 96 || _that.progressIntervalStop == true) {
                    clearInterval(interval);
                }
            }, 300);
        },
        // 显示共享材料
        showShareModal: function (data) {
            this.selMatRowData = data;
            this.searchShareFileList(data);
            this.getFileListWindowFlag = true;
        },
        // 查询要导入的电子件列表
        searchShareFileList: function (data) {
            var _that = this;
            var unitInfoId = '';
            var unitInfoIds = [];
            _that.jiansheFrom.map(function (item) {
                unitInfoIds.push(item.unitInfoId);
            })
            _that.agentUnits.map(function (item) {
                unitInfoIds.push(item.unitInfoId);
            })
            unitInfoId = unitInfoIds.join(',');
            var parmas = {
                keyword: _that.searchFileListfilter,
                matId: data.matId,
                matinstId: data.matinstId,
                page: '',
                unitInfoId: unitInfoId,
            };
            request('', {
                url: ctx + 'rest/mats/att/import/list',
                type: 'get',
                data: parmas
            }, function (result) {
                if (result.success) {
                    _that.shareFileList = result.content;
                } else {
                    _that.$message({
                        message: '可共享文件查询失败！',
                        type: 'error'
                    });
                }
            }, function (msg) {
                alertMsg('', '网络加载失败！', '关闭', 'error', true);
            })
        },
        // 导入电子件
        fileImport: function () {
            var _that = this;
            var unitInfoId = '';
            var unitInfoIds = [];
            var fileIds = '';
            var fileIdList = [];
            var selFileList = _that.$refs.shareFileList.selection;
            if (selFileList.length == 0) {
                _that.$message({
                    message: '请选择需要导入的文件！',
                    type: 'error'
                });
                return true;
            }
            selFileList.map(function (item) {
                fileIdList.push(item.detailId);
            });
            fileIds = fileIdList.join(',')
            _that.jiansheFrom.map(function (item) {
                unitInfoIds.push(item.unitInfoId);
            })
            _that.agentUnits.map(function (item) {
                unitInfoIds.push(item.unitInfoId);
            })
            unitInfoId = unitInfoIds.join(',');
            var parmas = {
                fileIds: fileIds,
                matId: _that.selMatRowData.matId,
                matinstId: _that.selMatRowData.matinstId,
                projInfoId: _that.projInfoId,
                unitInfoId: unitInfoId
            };
            request('', {
                url: ctx + 'rest/mats/att/import',
                type: 'post',
                data: parmas
            }, function (result) {
                if (result.success) {
                    _that.$message({
                        message: '导入文件成功！',
                        type: 'success'
                    });
                } else {
                    _that.$message({
                        message: '导入文件失败！',
                        type: 'error'
                    });
                }
            }, function (msg) {
                alertMsg('', '网络加载失败！', '关闭', 'error', true);
            })
        },
    },
});