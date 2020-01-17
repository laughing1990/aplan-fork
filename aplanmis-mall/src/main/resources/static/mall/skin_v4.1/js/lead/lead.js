(function(){
    var vm = new Vue({
        el:"#lead",
        data:{
            projInfoDetail:{},
            guideList: [], // 主题智能引导
            guideSelVal: {}, // 主题智能引导选项
            stageListBefore: [], // 智能引导选择阶段
            stageSelVal: {}, // 选择阶段信息
            stateList: [], // 情形列表
            stateSelVal: {},
            stageId:'',
        },
        created:function(){
            this.getFactorList();

        },
        methods:{
            // 获取智能根因子列表
            getFactorList:function () {
                var vm = this;
                request('', {
                    url: ctx + 'rest/userCenter/apply/factor/list',
                    type: 'get',
                }, function (res) {
                    if (res.success) {
                        vm.guideList = res.content;
                        if(vm.guideList && vm.guideList.length > 0){
                            vm.guideList.map(function(item){
                                if (typeof item.selectAnswerId == 'undefined' || item.selectAnswerId == undefined) {
                                    Vue.set(item, 'selectAnswerId', '');
                                }
                                if (item.answerType != 's' && item.answerType != 'y') {
                                    Vue.set(item, 'selValue', []);
                                    item.selectAnswerId = item.selValue;
                                }
                                item.answerFactors = vm.sortByKey(item.answerFactors, 'sortNo');
                            });
                        }
                    }else {
                        vm.$message({
                            message: res.message?res.message:'获取智能引导失败！',
                            type: 'error'
                        })
                    }
                },function(res){
                    vm.$message({
                        message: res.message?res.message:'获取智能引导失败！',
                        type: 'error'
                    })
                });
            },
            // 根据情形id获取子情形列表
            getChildsStateList: function(cStateList,pData, answerData, index, checkFlag,guideflag){
                var _that = this, questionStateId = '',_parentId = '',selStateIds = [], getUrl='';
                var stateListLen = cStateList?cStateList.length:0;
                if(guideflag=='guide'){
                    questionStateId = pData.factorId;
                    _parentId = answerData.factorId ? answerData.factorId : 'ROOT';
                    getUrl = 'rest/userCenter/apply/factor/child/list/' + _parentId;
                    _that.projInfoDetail.themeId = answerData.themeId;
                    if(answerData.themeId&&answerData.themeId!=''){
                        _that.projInfoDetail.themeId = answerData.themeId;
                        _that.themeId = answerData.themeId;
                        _that.saveThemeAndNext('guide');
                    }else {
                        _that.stateList = [];
                    }
                }else {
                    questionStateId = pData.parStateId;
                    _parentId = answerData.parStateId ? answerData.parStateId : 'ROOT';
                    getUrl = 'rest/userCenter/apply/childState/list/' + _parentId + '/' + _that.stageId
                }
                selStateIds = _that.getCoreItemsStatusListId(cStateList);
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
                        _that.$message({
                            message: '获取情形失败',
                            type: 'error'
                        });
                    }
                }, function (msg) {
                    _that.$message({
                        message: '获取情形失败',
                        type: 'error'
                    });
                });
            },
            // 选择主题保存并下一步根据主题获取阶段
            saveThemeAndNext: function (flag) {
                var _that = this;
                _that.loading = true;
                _that.selCoreItemsKey = [];
                _that.stateList = [];
                var _unitInfoId = '';
                if (_that.showObjectRole && (_that.applyObjectInfo.role == '2')) {
                    _unitInfoId = _that.applyObjectInfo.aeaUnitInfo.unitInfoId
                }
                var params = {
                    themeId: _that.themeId,
                    projInfoId: _that.projInfoId,
                    unitInfoId: _unitInfoId,
                };
                if (this.dygjbzfxfw) {
                    params.dygjbzfxfw = this.dygjbzfxfw;
                }
                request('', {
                    url: ctx + 'rest/main/stage/list/' + _that.themeId,
                    type: 'get',
                    data: params,
                }, function (data) {
                    if (data.success) {
                        _that.loading = false;
                        _that.stageList = data.content;
                        _that.stageListBefore = data.content;
                        if(data.content&&data.content.length>0){
                            if(flag=='guide'){
                                if(_that.stageId&&_that.stageId !== ''){
                                    _that.getStateList(_that.stageId);
                                }
                            }else {
                                if(_that.stageinstId == ''||_that.stageId == ''){
                                    _that.selStatus(_that.stageList[0], 0);
                                }else {
                                    _that.stageList.map(function(stageItem,index){
                                        if(stageItem.stageId==_that.stageId){
                                            _that.selStatus(stageItem, index, 'isHistory');
                                        }
                                    });
                                }
                            }
                        }else {
                            alertMsg('', '该主题下无可申报阶段！', '关闭', 'warning', true);
                        }
                    } else {
                        _that.loading = false;
                        _that.$message({
                            message: data.message ? data.message : '保存主题失败',
                            type: 'error'
                        });
                    }
                }, function (msg) {
                    _that.loading = false;
                    alertMsg('', '保存主题失败', '关闭', 'error', true);
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
            // 根据阶段id获取情形列表
            getStateList: function(val){
                var _that = this;
                _that.stageId = val;
                request('', {
                    url: ctx + 'rest/userCenter/apply/state/list/'+val,
                    type: 'get',
                }, function (res) {
                    if (res.success) {
                        _that.stateList = res.content;
                        _that.stateList.map(function (item, ind) { // 情形下插入对应的情形
                            if (item.answerType != 's' && item.answerType != 'y') {
                                Vue.set(item, 'selValue', []);
                                item.selectAnswerId = item.selValue;
                            }
                        });
                    }else {
                        _that.$message({
                            message: res.message?res.message:'获取智能引导失败！',
                            type: 'error'
                        })
                    }
                },function(res){
                    _that.$message({
                        message: res.message?res.message:'获取智能引导失败！',
                        type: 'error'
                    })
                });
            },
            sortByKey: function (array, key) {  //(数组、排序的列)
                return array.sort(function (a, b) {
                    var x = a[key];
                    var y = b[key];
                    return ((x < y) ? -1 : ((x > y) ? 1 : 0));
                });
            },
            // 跳转办事指南一单清
            toListMatter:function(){
                if(!this.stageId){
                    this.$message.error("请继续引导，选择阶段id")
                    return
                }
                var stageId = this.stageId;
                var stateList = this.stateList;  // 情形列表数据（选择的情形id数据里面）
                sessionStorage.setItem('stateList',JSON.stringify(stateList));
                window.location.href =ctx + "rest/main/toIndexPage?stageId="+stageId+"#/listmatter";
            }
        }
    })
})();