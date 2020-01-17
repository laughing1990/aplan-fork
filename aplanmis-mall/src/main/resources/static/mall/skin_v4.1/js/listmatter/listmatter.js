var listmatter = (function(window){
    var vm = new Vue({
        el:"#listmatter",
        data:{
            ctx:ctx,
            show1:true,
            show2:true,
            show3:true,
            contentLoading:false,
            materialLoading:false,
            radio:'1',
            stageId:null,// 当前阶段id
            listmatterData:{},
            coreItemList:[],
            coreItemCheckData:[], // 筛选出isDoneItem为“1”的数据
            noRequireCoreItemCheckData:[], // 非必选的并行推进事项（用户后来可以自由勾选的）
            parallelItemList:[],
            parallelItemCheckData:[],// 筛选出isDoneItem为“1”的数据
            noRequireParallelItemCheckData:[], // 非必选的并联事项（用户后来可以自由勾选的）
            itemStateIds:[], // 事项情形ID数组

            requireMat:[],// 必选材料
            noRequireMat:[], //可选材料
            mats: [],        // 空表下载、样表下载

            AIleadDialogFlag:false, // 智能引导弹出框
            stateList: [], // 情形列表
            stateSelVal: {},
            getViewIframeSrc:'',
            filePreviewCount: 0,
            matDialog:false,
        },
        created:function(){
            this.GetRequest();
        },
        methods:{
            // 预览电子件（查看项目报建流程）
            previewFile: function(data,getSrc){ // getSrc TRUE不打开新页面仅获取src
                var _this = this;
                var detailId = data.detailId;
                var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
                var fileName=data.attName || 'xxx.png';
                var fileType = this.getFileType(fileName);
                var flashAttributes = '';
                _this.filePreviewCount++
                if(fileType=='pdf'){
                    var tempwindow=window.open(); // 先打开页面
                    setTimeout(function(){
                        tempwindow.location=ctx+'/previewPdf/view?detailId='+detailId;
                    },1000)
                }else {
                    if(regText.test(fileType)){
                        // previewPdf/pdfIsCoverted
                        _this.showProcessLoading = true;
                        request('', {
                            url: ctx + 'previewPdf/pdfIsCoverted?detailId='+detailId,
                            type: 'get',
                        }, function (result) {
                            if(result.success){
                                _this.showProcessLoading = false;
                                if(getSrc){
                                    _this.getViewIframeSrc = ctx+'previewPdf/view?detailId='+detailId;
                                }else {
                                    var tempwindow=window.open(); // 先打开页面
                                    setTimeout(function(){
                                        tempwindow.location=ctx+'previewPdf/view?detailId='+detailId;
                                    },1000)
                                }
                            }else {
                                if(_this.filePreviewCount>9){
                                    confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                                        _this.filePreviewCount=0;
                                        _this.previewFile(data);
                                    }, function () {
                                        _this.filePreviewCount=0;
                                        _this.showProcessLoading = false;
                                        return false;
                                    }, '确定', '取消', 'warning', true)
                                }else {
                                    setTimeout(function(){
                                        _this.previewFile(data);
                                    },1000)
                                }
                            }
                        }, function (msg) {
                            _this.showProcessLoading = false;
                            _this.$message({
                                message: '文件预览失败',
                                type: 'error'
                            });
                        })
                    }else {
                        _this.showProcessLoading = false;
                        if(getSrc){
                            _this.getViewIframeSrc = ctx + 'rest/file/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
                        }else {
                            var tempwindow=window.open(); // 先打开页面
                            setTimeout(function(){
                                tempwindow.location = ctx + 'rest/file/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
                            },1000)
                        }
                    }
                }
            },
            // 获取事项一单清
            getItemList:function (stageId) {
                var vm = this;
                this.contentLoading = true
                request('', {
                    url: ctx + 'rest/guide/itemAndState/list/'+ stageId,
                    type: 'get'
                }, function (res) {
                    if (res.success) {
                        vm.contentLoading = false;
                        var content = res.content;
                        vm.stateList = content.stateList;
                        vm.stateList.map(function (item, ind) { // 情形下插入对应的情形
                            if (item.answerType != 's' && item.answerType != 'y') {
                                Vue.set(item, 'selValue', []);
                                item.selectAnswerId = item.selValue;
                            }
                        });
                        vm.listmatterData = content;
                        vm.parallelItemList = content.parallelItemList; //  并联事项
                        //筛选出isDoneItem为“1”的数据，该数据勾选状态需要设置为勾选且禁用
                        if(vm.parallelItemList.length > 0){
                             vm.parallelItemCheckData = vm.parallelItemList.filter(function (item) {
                                return item.isDoneItem == '1'
                            });
                            vm.$nextTick(function(){
                                if(vm.parallelItemCheckData.length > 0){
                                    vm.parallelItemCheckData.forEach(function (item) {
                                        vm.$refs.parallelItematable.toggleRowSelection(item,true);
                                    })
                                }
                            })
                        }
                        vm.coreItemList = content.coreItemList; // 并行推进事项
                        //筛选出isDoneItem为“1”的数据，该数据勾选状态需要设置为勾选且禁用
                        if(vm.coreItemList.length >0 ){
                             vm.coreItemCheckData = vm.coreItemList.filter(function (item) {
                               return item.isDoneItem == '1';
                            });
                            vm.$nextTick(function () {
                                if(vm.coreItemCheckData.length > 0){
                                    vm.coreItemCheckData.forEach(function (item) {
                                        vm.$refs.coreItemListTable.toggleRowSelection(item,true);
                                    })
                                }
                            })
                        }
                        vm.getMateriallist();
                    } else {
                        vm.$message.error(res.message);
                    }
                }, function () {
                    vm.contentLoading = false;
                    vm.$message.error('获取事项一清单数据接口失败，请稍后重试！');
                });
            },
            // 获取材料一单清
            getMateriallist:function(){
                var vm = this;
                this.materialLoading = true;
                var coreItemVerIds = [] , coreParentItemVerIds = [] ,paraParentllelItemVerIds = [] , parallelItemVerIds = [] ,stageStateIds = [] ,itemStateIds = [] ;

                // 默认勾选的事项，用户不能操作勾选
                if(vm.parallelItemCheckData.length > 0){
                    vm.parallelItemCheckData.forEach(function (item) {
                        if(item.currentCarryOutItem && item.currentCarryOutItem.itemVerId){
                            parallelItemVerIds.push(item.currentCarryOutItem.itemVerId);
                        }
                        paraParentllelItemVerIds.push(item.itemVerId);
                    })
                }
                if(vm.coreItemCheckData.length > 0){
                    vm.coreItemCheckData.forEach(function (item) {
                        if(item.currentCarryOutItem && item.currentCarryOutItem.itemVerId){
                            coreItemVerIds.push(item.currentCarryOutItem.itemVerId);
                        }
                        coreParentItemVerIds.push(item.itemVerId);
                    })
                }

                // 非必选，用户后来自由勾选
                if(vm.noRequireParallelItemCheckData.length > 0){
                    vm.noRequireParallelItemCheckData.forEach(function (item) {
                        if(item.currentCarryOutItem && item.currentCarryOutItem.itemVerId){
                            parallelItemVerIds.push(item.currentCarryOutItem.itemVerId);
                        }
                        paraParentllelItemVerIds.push(item.itemVerId);
                    })
                }
                if(vm.noRequireCoreItemCheckData.length > 0){
                    vm.noRequireCoreItemCheckData.forEach(function (item) {
                        if(item.currentCarryOutItem && item.currentCarryOutItem.itemVerId){
                            coreItemVerIds.push(item.currentCarryOutItem.itemVerId);
                        }
                        coreParentItemVerIds.push(item.itemVerId);
                    })
                }

                // 用户勾选的事项情形
                itemStateIds = vm.itemStateIds;

                var params = {
                    "paraParentllelItemVerIds":paraParentllelItemVerIds, // 并联标准事项版本ID数组
                    "parallelItemVerIds":parallelItemVerIds, // 并联事项版本ID数组(对应下面是标准事项下实施事项的事项版本id)
                    "coreParentItemVerIds":coreParentItemVerIds, // 并行标准事项版本ID数组
                    "coreItemVerIds":coreItemVerIds, // 并行事项版本ID数组(对应下面是标准事项下实施事项的事项版本id)
                    "stageId":vm.stageId, // 阶段id
                    "itemStateIds":itemStateIds, // 事项情形ID数组
                    "stageStateIds":stageStateIds, //阶段情形ID数组
                }
                console.log(params)
                $.ajax({
                    url: ctx + 'rest/guide/mat/list',
                    type: 'post',
                    data:JSON.stringify(params),
                    contentType: 'application/json;charset=utf-8',
                    success:function(res){
                        vm.materialLoading = false;
                        if (res.success) {
                            var content = res.content;
                            vm.requireMat = content.requireMat;
                            vm.noRequireMat = content.noRequireMat;
                        } else {
                            vm.$message.error(res.message);
                        }
                    },
                    error:function () {
                        vm.materialLoading = false;
                        vm.$message.error('获取材料一清单数据接口失败，请稍后重试！');
                    }
                })
            },
            // 切换审批层次
            parallelItemCurrentItemVerIdChangehandle:function(item ,index){
                this.parallelItemList[index].resultMats = item.resultMats;
                this.parallelItemList[index].currentCarryOutItem.itemName = item.itemName;
                this.parallelItemList[index].currentCarryOutItem.orgName = item.orgName;
                this.parallelItemList[index].currentCarryOutItem.dueNum = item.dueNum;
                this.parallelItemList[index].currentCarryOutItem.paraStateList = item.paraStateList;
                this.ispandEx(vm.parallelItemList,item);
            },
            coreItemCurrentItemVerIdChangehandle:function(item ,index){
                this.coreItemList[index].resultMats = item.resultMats;
                this.coreItemList[index].currentCarryOutItem.itemName = item.itemName;
                this.coreItemList[index].currentCarryOutItem.orgName = item.orgName;
                this.coreItemList[index].currentCarryOutItem.dueNum = item.dueNum;
                this.coreItemList[index].currentCarryOutItem.paraStateList = item.paraStateList;
                this.ispandEx(vm.coreItemList,item);
            },
            // 判断是否必选和之前用户是否已经选中事项，是则请求材料数据
            ispandEx:function(data,item){
                if(item.isDoneItem == 1){
                    this.getMateriallist();
                }else{
                    if(data.indexOf(item)){
                        this.getMateriallist();
                    }
                }
            },
            // 单独勾选表格中某条数据所对应的勾选框
            selectParallelItem:function(selection,row){
                var vm = this;
                vm.noRequireParallelItemCheckData = [];
                selection.forEach(function (item) {
                    if(item){
                        vm.noRequireParallelItemCheckData.push(item);
                    }
                })
                vm.getMateriallist();
            },
            selectCoreItem:function(selection,row){
                var vm = this;
                vm.noRequireCoreItemCheckData = [];
                selection.forEach(function (item) {
                    if(item){
                        vm.noRequireCoreItemCheckData.push(item);
                    }
                })
                vm.getMateriallist();
            },
            // 控制事项一单清是否可以勾选
            selectable:function(row,index) {
                var vm = this;
                if(row.isDoneItem=='1'){
                   return  false
                }else {
                    return true
                }
            },
            // 根据情形id获取子情形列表
            getChildsStateList: function(cStateList,pData, answerData, index, checkFlag,guideflag){
                var _this = this, questionStateId = '',_parentId = '',selStateIds = [], getUrl='';
                var stateListLen = cStateList?cStateList.length:0;
                if(guideflag=='guide'){
                    questionStateId = pData.factorId;
                    _parentId = answerData.factorId ? answerData.factorId : 'ROOT';
                    getUrl = 'rest/userCenter/apply/factor/child/list/' + _parentId;
                    _this.projInfoDetail.themeId = answerData.themeId;
                    if(answerData.themeId&&answerData.themeId!=''){
                        _this.projInfoDetail.themeId = answerData.themeId;
                        _this.themeId = answerData.themeId;
                        _this.saveThemeAndNext('guide');
                    }else {
                        _this.stateList = [];
                    }
                }else {
                    questionStateId = pData.parStateId;
                    _parentId = answerData.parStateId ? answerData.parStateId : 'ROOT';
                    getUrl = 'rest/userCenter/apply/childState/list/' + _parentId + '/' + _this.stageId
                }
                selStateIds = _this.getCoreItemsStatusListId(cStateList);
                if (checkFlag == false) {
                    for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
                        var obj = cStateList[i];
                        var _parentStateId = '';
                        if(obj){
                            if(guideflag=='guide'){
                                _parentStateId = obj.parentFactorId;
                            }else {
                                _parentStateId = obj.parentStateId;
                            }
                            if ((_parentStateId == _parentId) || (obj && obj.parentStateId != null && (selStateIds.indexOf(_parentStateId) == -1))) {
                                cStateList.splice(i, 1);
                                if (typeof (obj.selectAnswerId) == 'object' && obj.selectAnswerId.length > 0) {
                                    obj.selectAnswerId.map(function (itemSel) {
                                        selStateIds = selStateIds.filter(function (item, index) {
                                            return item !== itemSel;
                                        })
                                    });
                                } else if (obj.selectAnswerId !== '') {
                                    selStateIds = selStateIds.filter(function (item, index) {
                                        return item !== obj.selectAnswerId;
                                    })
                                }
                                i--
                            }
                        }
                    }
                    return false
                }
                request('', {
                    url: ctx + getUrl,
                    type: 'get'
                }, function (data) {
                    if (data.success) {
                        if (checkFlag !== true) {
                            for (var i = 0; i < stateListLen; i++) { // 清空情形下所对应情形
                                var obj = cStateList[i];
                                var parentQuestionStateId='',_parentStateId='';
                                if(obj){
                                    if(guideflag=='guide'){
                                        _parentStateId = obj.parentFactorId;
                                        parentQuestionStateId = obj.parentQuestionFactorId;
                                    }else {
                                        _parentStateId = obj.parentStateId;
                                        parentQuestionStateId = obj.parentQuestionStateId;
                                    }
                                    if ((parentQuestionStateId == questionStateId) || (_parentStateId && _parentStateId != '' && (selStateIds.indexOf(_parentStateId) == -1))) {
                                        cStateList.splice(i, 1);
                                        if (typeof (obj.selectAnswerId) == 'object' && obj.selectAnswerId.length > 0) {
                                            obj.selectAnswerId.map(function (itemSel) {
                                                selStateIds = selStateIds.filter(function (item, index) {
                                                    return item !== itemSel;
                                                })
                                            });
                                        } else if (obj.selectAnswerId !== '') {
                                            selStateIds = selStateIds.filter(function (item, index) {
                                                return item !== obj.selectAnswerId;
                                            })
                                        }
                                        i--
                                    }

                                }
                            }
                        }
                        data.content.map(function (item, ind) { // 情形下插入对应的情形
                            if (item.answerType != 's' && item.answerType != 'y') {
                                Vue.set(item, 'selValue', []);
                                item.selectAnswerId = item.selValue;
                            }
                            cStateList.splice((index + 1 + ind), 0, item);
                        });
                    } else {
                        _this.$message({
                            message: '获取情形失败',
                            type: 'error'
                        });
                    }
                }, function (msg) {
                    _this.$message({
                        message: '获取情形失败',
                        type: 'error'
                    });
                });
            },
            // 获取单事项情形列表id
            getCoreItemsStatusListId: function (cStateList) {
                var stateListLen = cStateList?cStateList.length:0;
                var selStateIds = [];
                for (var i = 0; i < stateListLen; i++) {  // 已选并联情形id集合
                    if (cStateList[i].selectAnswerId !== undefined || cStateList[i].selectAnswerId !== null) {
                        // selStateIds=[];
                        // return true;
                        if (typeof (cStateList[i].selectAnswerId) == 'object' && cStateList[i].selectAnswerId.length > 0) {
                            selStateIds = selStateIds.concat(cStateList[i].selectAnswerId);
                        } else if (cStateList[i].selectAnswerId !== '') {
                            selStateIds.push(cStateList[i].selectAnswerId);
                        }
                    }
                }
                selStateIds = selStateIds.filter(function (item, index) {
                    //当前元素，在原始数组中的第一个索引==当前索引值，否则返回当前元素
                    return selStateIds.indexOf(item, 0) === index
                })
                return selStateIds;
            },
            // 空表下载、样表下载
            showMatFiles: function (item, _docType) {
                var _this = this;
                $.ajax({
                    url: ctx + 'rest/guide/attLinkAndDetailNoPage/list',
                    type: "get",
                    cache: false,
                    data: {
                        tableName: 'AEA_ITEM_MAT',
                        pkName: _docType,
                        recordId: item.matId,
                        attType: '',
                    },
                    dataType: 'json',
                    success: function (result) {
                        _this.mats = [];
                        if (result != null && result.content.length > 1) {
                            _this.matDialog = true;
                            _this.mats = result.content;
                        } else if (result.content.length == 1) {
                            window.open(_this.ctx + 'rest/file/applydetail/mat/download/' + result.content[0].detailId);
                        } else {
                            _this.matDialog = true;
                        }
                    }
                });
            },
            // 点击开始指引
            startAILeadFn:function(){
                var stateList = this.stateList;
                this.AIleadDialogFlag= false;
                this.getAIleadData(stateList);
            },
            getAIleadData:function(stateList){
                var vm = this;
                // 取到选择的情形id列表
                var stateIds = this.fetchStateList(stateList) || [];
                var params = {
                    "projectAddress": "", // 建设地点
                    "regionalism": "", //行政区划
                    "stageId":vm.stageId, // 阶段id
                    "stateIds": stateIds // 情形ID列表
                };
                $.ajax({
                    url: ctx + 'rest/guide/itGuide/item/list', // 智能引导获取事项一单清列表数据
                    type: 'post',
                    data:JSON.stringify(params),
                    contentType: 'application/json;charset=utf-8',
                    success:function(res){
                        vm.materialLoading = false;
                        if (res.success) {
                            var content = res.content;
                            var coreItemList = content.coreItemList;
                            var parallelItemList = content.parallelItemList;
                            // 和原来的数据对比，页面的事项对应有的话要勾选
                            vm.compareAutoSelect(coreItemList,parallelItemList);
                        } else {
                            vm.$message.error(res.message);
                        }
                    },
                    error:function () {
                        vm.materialLoading = false;
                        vm.$message.error('智能引导获取事项一单清列表数据接口失败，请稍后重试！');
                    }
                })
            },
        },
        mixins:[listmatterMixin],
    });
    return {
        vm: vm,
    }
})(window);