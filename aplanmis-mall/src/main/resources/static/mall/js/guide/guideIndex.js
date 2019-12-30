var guideIndex = (function () {
    var ele,
        eleLen;
    var window = this;

    var viewer;// viewer 对象
    getGuideDetail = function (e, stageId) {
        if (e) {
            // $(e.target).parent().addClass('activeLi').siblings().removeClass('activeLi');
            $(e).addClass('activeLi').siblings().removeClass('activeLi');
        } else {
            var mainLine = $('#stageImg .show');
            mainLine.removeClass('activeLi');
            for (var i = 0; i < mainLine.length; i++) {
                if (mainLine.eq(i).attr('id') == stageId) {
                    mainLine.eq(i).addClass('activeLi');
                }
            }
            // $('#stageImg .show').eq(0).addClass('activeLi')
        }
        var _this = vm;
        _this.tableToggle('', 0);
        request('', {
            url: ctx + 'rest/guide/guide/detailed/' + stageId,
            type: 'get',
        }, function (res) {
            _this.orgLoading = false;
            if (res.success) {
                _this.guideDetailed = res.content;
                if(_this.guideDetailed.guideAtt){
                    _this.previewFile(_this.guideDetailed.guideAtt,true);
                }
            } else {
                _this.orgLoading = false;
                _this.$message.error(res.message);
            }
        }, function () {
            _this.$message.error('获取办事指南数据接口失败，请重试！');
        });
    }
    getGuideDetail2 = function (e, stageId) {
        if (e) {
            // $(e.target).parent().addClass('activeLi').siblings().removeClass('activeLi');
            $(e).addClass('activeLi').siblings().removeClass('activeLi');
        } else {
            var auxiliary = $('#stageAuxiliaryImg .show');
            auxiliary.removeClass('activeLi');
            for (var i = 0; i < auxiliary.length; i++) {
                if (auxiliary.eq(i).attr('id') == stageId) {
                    auxiliary.eq(i).addClass('activeLi');
                }
            }
            // $('#stageAuxiliaryImg .show').eq(0).addClass('activeLi')
        }
        var _this = vm;
        _this.tableToggle2('', 0);
        request('', {
            url: ctx + 'rest/guide/guide/detailed/' + stageId,
            type: 'get',
        }, function (res) {
            _this.orgLoading = false;
            if (res.success) {
                _this.guideDetailed2 = res.content;
            } else {
                _this.orgLoading = false;
                _this.$message.error(res.message);
            }
        }, function () {
            _this.$message.error('获取办事指南数据接口失败，请重试！');
        });
    }
    matChange = function (target) {
        var obj = {
            itemStateId: $(target).attr("id"),
            parentStateId: $(target).attr("parentId")
        }
        vm.findByParentItemStateId(obj.itemStateId, obj.parentStateId);

        for (var i = 0; i < vm.mustAnswer.length; i++) {
            if (obj.parentStateId == vm.mustAnswer[i]) {
                vm.mustAnswer.splice(i, 1);
            }
        }
        if (vm.matId.length == 0) {
            vm.matId.push(obj);
            return;
        }
        for (var i = 0; i < vm.matId.length; i++) {
            if (obj.parentStateId == vm.matId[i].parentStateId) {
                vm.matId[i].itemStateId = obj.itemStateId;
                return;
            }
        }
        vm.matId.push(obj);
    }
    var vm = new Vue({
        el: '#guideIndex',
        data: {
            ctx: ctx,
            approvalwayTypeFlag: true,
            approvalWayActiveName: 1,
            tableData: [],
            pageSize: 10,
            page: 1,
            total: 0,
            themeListData: [],
            ellipsisLn: [],
            activeSub: 0,
            themeLoading: false,
            themeMemo: '',
            themeId: '',
            orgNum: 0,
            OrgListData: [],
            orgLoading: false,
            areaOnIndex:'',
            cityOnIndex:'',
            orgItem: '',
            orgId: '',
            curIndex: 0,
            curIndex2: 0,
            isSinglePage: false,
            guideDetailed: {},
            guideDetailed2: {},
            memoLoading: false,
            singleTable1: false,
            singleTable2: false,
            singleTable3: false,
            singleTable4Data: [],
            singleTable9Data: [],
            singleTable10Data: [],
            question: [],
            score: 5,
            detailData: {},
            wslct: false,
            cklct: false,
            showAll: false,
            singleTitle: '',
            countStageType1: false,
            countStageType2: false,
            stateRadio: '',
            statecheckList: [],
            matId: [],//情形答案id集合
            firstMat: [],//页面首次加载的材料列表
            matDialog: false,
            mats: [],
            __QITA__: [],
            childState: [],
            mustAnswer: [],//必选答案的集合
            sessionStorage: {},
            sessionStorage2: {},
            navList: [
                "基本信息",
                "范围与条件",
                "办理流程",
                "办事情形",
                "所需材料",
                "咨询监督",
                "窗口办理",
                "许可收费",
                '中介服务',
                "设定依据",
                '权利与义务',
                '法律救济'
            ],
            infoData: ["基本信息",
                "审批依据",
                "办理程序",
                "流程图",
                "并联审批事项",
                "可并行推进事项",
                "并联审批申请材料",
                "可并行推进事项材料"],
            itemProjInfoId: '', // 单项申报项目id
            stateListFlag: false,
            filePreviewCount: 0,
            showProcessLoading: false,
            getViewIframeSrc: '',
            imgUrl: ["common/stage/mainLine/images/立项用地许可.png", "common/stage/mainLine/images/工程建设许可.png", "common/stage/mainLine/images/施工许可.png", "common/stage/mainLine/images/竣工验收.png", "common/stage/mainLine/images/立项用地许可.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/工程建设许可.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png"],
            itemListKeyword:'',
            stagesData:{}, // 某主题下的阶段和事项
            fullscreenLoading: false,
        },
        mounted: function () {
            var vm = this;
            vm.init()
            vm.GetRequest();

            this.sessionStorage = JSON.parse(sessionStorage.getItem("stageData"))|| {origin:false};
            this.sessionStorage2 = JSON.parse(sessionStorage.getItem("orgData")) || {origin2:false};

        },
        methods: {
            init: function () {
                this.getThemeList();
            },
            hideMoreDepart: function () {
                for (var i = 0; i < eleLen; i++) {
                    if (i > 11) {
                        ele.eq(i).hide();
                    } else {
                        ele.eq(i).show();
                    }
                }
            },
            // 格式化时间
            dateFormat: function (val) {
                var daterc = val;
                if (daterc != null) {
                    var dateMat = new Date(parseInt(daterc));
                    return formatDate(dateMat, 'yyyy.MM.dd')
                }
            },
            toMeterial: function () {
                $(document).scrollTop(1600);
            },
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
            download: function (item) {
                window.open(this.ctx + 'rest/file/applydetail/mat/download/' + item.detailId);
            },

            //按主题申报-->获取主题列表
            getThemeList: function () {
                var vm = this;
                vm.themeLoading = true;
                vm.fullscreenLoading = true;
                request('', {
                    url: ctx + 'rest/main/theme/list',
                    type: 'get'
                }, function (res) {
                    vm.themeLoading = false;
                    vm.fullscreenLoading = false;
                    if (res.success) {
                        var content = res.content;
                        vm.themeListData = content;
                        if (vm.sessionStorage && vm.sessionStorage.origin) {
                            vm.$nextTick(function () {
                                vm.goToGuid('', vm.sessionStorage.themeId, 0, vm.sessionStorage.themeMemo);
                            })
                        }
                        setTimeout(function(){
                            if(vm.themeListData.length  === 1){
                                $(".theme-type .theme-type-list").css('height',664+'px');
                            }else if(vm.themeListData.length  === 2){
                                $(".theme-type .theme-type-list").css('height',604+'px');
                            }else if(vm.themeListData.length  === 3){
                                $(".theme-type .theme-type-list").css('height',549+'px');
                            }else if(vm.themeListData.length  === 4){
                               $(".theme-type .theme-type-list").css('height',494+'px');
                            }else if(vm.themeListData.length  === 5){
                                $(".theme-type .theme-type-list").css('height',438+'px');
                            }else if(vm.themeListData.length  === 6){
                                $(".theme-type .theme-type-list").css('height',386+'px');
                            }else if(vm.themeListData.length  === 7){
                                $(".theme-type .theme-type-list").css('height',203+'px');
                            }else if(vm.themeListData.length  === 8){
                                $(".theme-type .theme-type-list").css('height',146+'px');
                            }
                        },200);
                    } else {
                        vm.themeLoading = false;
                        vm.fullscreenLoading =false;
                        vm.$message.error(res.message);
                    }
                    if (!vm.sessionStorage.origin && vm.themeListData.length > 0) {
                        vm.goToGuid('', vm.themeListData[0].themeList[0].themeId, 0, vm.themeListData[0].themeList[0].themeMemo);
                    }
                }, function () {
                    vm.$message.error('获取主题列表数据接口失败，请重试！');
                });
            },
            toUserCenterindexPage: function (itemVerId) {
                window.location.href = ctx + "rest/main/toIndexPage?itemVerId=" + itemVerId + '&&projInfoId=null&&flag=singleD#declare';
            },
            tolistMatter:function(){
                window.location.href =ctx + "rest/main/toIndexPage?#/listmatter";
            },
            // 办事指南 搜索
            searchItemList:function(){
                this.getSearchThemeList();
            },
            getSearchThemeList:function () {
                var vm = this;
                if(!vm.itemListKeyword){
                    vm.$message.warning('请输入关键词，再进行查询！')
                    return
                }
                vm.themeLoading = true;
                request('', {
                    url: ctx + 'rest/guide/search/theme/list/'+ vm.itemListKeyword,
                    type: 'get',
                }, function (res) {
                    vm.themeLoading = false;
                    vm.fullscreenLoading = false;
                    if (res.success) {
                        var content = res.content;
                        vm.themeListData = content;
                        if (vm.sessionStorage && vm.sessionStorage.origin) {
                            vm.$nextTick(function () {
                                vm.goToGuid('', vm.sessionStorage.themeId, 0, vm.sessionStorage.themeMemo);
                            })
                        }
                        setTimeout(function(){
                            if(vm.themeListData.length  === 1){
                                $(".theme-type .theme-type-list").css('height',664+'px');
                            }else if(vm.themeListData.length  === 2){
                                $(".theme-type .theme-type-list").css('height',604+'px');
                            }else if(vm.themeListData.length  === 3){
                                $(".theme-type .theme-type-list").css('height',549+'px');
                            }else if(vm.themeListData.length  === 4){
                                $(".theme-type .theme-type-list").css('height',494+'px');
                            }else if(vm.themeListData.length  === 5){
                                $(".theme-type .theme-type-list").css('height',438+'px');
                            }else if(vm.themeListData.length  === 6){
                                $(".theme-type .theme-type-list").css('height',386+'px');
                            }else if(vm.themeListData.length  === 7){
                                $(".theme-type .theme-type-list").css('height',203+'px');
                            }else if(vm.themeListData.length  === 8){
                                $(".theme-type .theme-type-list").css('height',146+'px');
                            }
                        },200);
                    } else {
                        vm.themeLoading = false;
                        vm.$message.error(res.message);
                    }
                    if (!vm.sessionStorage.origin && vm.themeListData.length > 0) {
                        vm.goToGuid('', vm.themeListData[0].themeList[0].themeId, 0, vm.themeListData[0].themeList[0].themeMemo);
                    }
                },function () {
                    vm.$message.error('查询接口请求失败');
                    vm.themeLoading = false;
                });
            },
            // 获取文件后缀
            getFileType: function(fileName){
              var index1=fileName.lastIndexOf(".");
              var index2=fileName.length;
              var suffix=fileName.substring(index1+1, index2).toLowerCase();//后缀名
              return suffix;
            },
            // 预览电子件
            previewFile: function(data,getSrc){ // getSrc TRUE不打开新页面仅获取src
            var detailId = data.detailId;
            var _that = this;
            var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
            var fileName=data.attName;
            var fileType = this.getFileType(fileName);
            var flashAttributes = '';
            _that.filePreviewCount++
            if(fileType=='pdf'){
                var tempwindow=window.open(); // 先打开页面
                setTimeout(function(){
                    tempwindow.location=ctx+'/previewPdf/view?detailId='+detailId;
                },1000)

            }else {
              if(regText.test(fileType)){
                // previewPdf/pdfIsCoverted
                _that.showProcessLoading = true;
                request('', {
                  url: ctx + 'previewPdf/pdfIsCoverted?detailId='+detailId,
                  type: 'get',
                }, function (result) {
                  if(result.success){
                    _that.showProcessLoading = false;
                    if(getSrc){
                        _that.getViewIframeSrc = ctx+'previewPdf/view?detailId='+detailId;
                    }else {
                        var tempwindow=window.open(); // 先打开页面
                        setTimeout(function(){
                            tempwindow.location=ctx+'previewPdf/view?detailId='+detailId;
                        },1000)
                    }
                  }else {
                    if(_that.filePreviewCount>9){
                      confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                        _that.filePreviewCount=0;
                        _that.previewFile(data);
                      }, function () {
                        _that.filePreviewCount=0;
                        _that.showProcessLoading = false;
                        return false;
                      }, '确定', '取消', 'warning', true)
                    }else {
                      setTimeout(function(){
                        _that.previewFile(data);
                      },1000)
                    }
                  }
                }, function (msg) {
                  _that.showProcessLoading = false;
                  _that.$message({
                    message: '文件预览失败',
                    type: 'error'
                  });
                })
              }else {
                _that.showProcessLoading = false;
                if(getSrc){
                    _that.getViewIframeSrc = ctx + 'rest/file/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
                }else {
                    var tempwindow=window.open(); // 先打开页面
                    setTimeout(function(){
                        tempwindow.location = ctx + 'rest/file/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
                    },1000)
                }
              }
            }
          },
            previewResultGuide:function(detailId){
                    window.open(ctx+'rest/file/att/preview?detailId='+detailId);
            },

            // 根据主题获取阶段接口
            goToGuid: function (e, themeId, indexSub, themeMemo) {
                var vm = this;
                vm.activeSub = indexSub;
                vm.themeMemo = themeMemo;
                vm.themeId = themeId;
                var keyword = vm.itemListKeyword?vm.itemListKeyword:'%E2%80%98%20%E2%80%99';
                var item = $('.theme-type-item');
                $('.theme-type-item').removeClass('active');
                if (e.target) {
                    $(e.target).addClass('active');
                } else {
                    for (var i = 0; i < item.length; i++) {
                        if (item.eq(i).attr('id') == themeId) {
                            item.eq(i).addClass('active');
                        }
                    }
                }
                this.memoLoading = true;
                $.ajax({
                    //url: ctx + "rest/guide/stageAndItem/list/" + themeId,
                    url: ctx + "rest/guide/search/stageAndItem/list/"+themeId+"/"+ keyword,
                    type: "get",
                    async: false,
                    success: function (res) {
                        var result = res.content
                        vm.memoLoading = false;
                        vm.stagesData = result;
                    }
                })
            },
            approvelWayActiveChange: function (val) {
                var vm = this;
                if (val) {
                    $(".theme-list").removeClass('active');
                    for (var i = 0; i < val.length; i++) {
                        $(".theme-list").eq(val[i] - 1).addClass('active');
                    }
                }

            },

            // 获取url参数
            GetRequest: function () {
                var url = location.search; //获取url中"?"符后的字串
                var theRequest = new Object();
                if (url.indexOf("?") != -1) {
                    var str = url.substr(1);
                    strs = str.split("&");
                    for (var i = 0; i < strs.length; i++) {
                        theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
                    }
                }
                this.itemProjInfoId = theRequest.projInfoId ? theRequest.projInfoId : null;
                if (theRequest.flag === 'singleD') {
                    this.approvalwayTypeFlag = false;
                    this.getOrgList(this.orgId);
                } else if (theRequest.flag === 'bszn' && theRequest.itemVerId && theRequest.itemName) {
                    this.singleTitle = decodeURIComponent(theRequest.itemName);
                    var row = {
                        itemVerId:theRequest.itemVerId
                    }
                    this.switchSinglePage(row);
                }
                return theRequest;
            },
            getOrgId:function(){
                var secOrgId = this.secOrgId;
                this.getOrgList(secOrgId);
            },
            //根据父情形查找子情形
            findByParentItemStateId: function (itemStateId, parentId) {
                var vm = this;
                request('', {
                    url: ctx + 'rest/guide/itemState/findByParentItemStateId/' + itemStateId,
                    type: 'get',
                }, function (res) {
                    if (res.success) {
                        if (res.content.length > 0) {
                            var childState = $(".childState");
                            for (var i = 0; i < res.content.length; i++) {
                                var childT = '';
                                if (res.content[i].mustAnswer == '1' && vm.mustAnswer.indexOf(res.content[i].itemStateId) != -1) {
                                    vm.mustAnswer.push(res.content[i].itemStateId);
                                }
                                if (res.content[i].answerType == 's' || res.content[i].answerType == 'y') {
                                    if (res.content[i].mustAnswer == '1') {
                                        childT += "<span data-id=" + parentId + " class='child'><p><h3 style='margin: 10px 0'>" + res.content[i].stateName + "</h3><i class='red-waring'>*</i></p>"
                                    } else {
                                        childT += "<span data-id=" + parentId + " class='child'><p><h3 style='margin: 10px 0'>" + res.content[i].stateName + "</h3></p>"
                                    }

                                    var childC = '';
                                    for (var j = 0; j < res.content[i].answerStates.length; j++) {
                                        childC += "<label class='radiobox'>" +
                                            "<input id=" + res.content[i].answerStates[j].itemStateId + " name=" + 'radio' + i + parentId + " data-value=" + res.content[i].answerStates[j] + " type='radio' parentId=" + res.content[i].answerStates[j].parentStateId + " onchange=matChange(this) >" + res.content[i].answerStates[j].stateName + "<span></span></label>"
                                    }
                                    childT = childT + childC + '</span>'
                                } else {
                                    if (res.content[i].mustAnswer == '1') {
                                        childT += "<span data-id=" + parentId + " class='child'><p><h3 style='margin: 10px 0'>" + res.content[i].stateName + "</h3><i class='red-waring'>*</i></p>"
                                    } else {
                                        childT += "<span data-id=" + parentId + " class='child'><p><h3 style='margin: 10px 0'>" + res.content[i].stateName + "</h3></p>"
                                    }
                                    var childC = '';
                                    for (var j = 0; j < res.content[i].answerStates.length; j++) {
                                        childC += "<label class='radiobox'>" +
                                            "<input id=" + res.content[i].answerStates[j].itemStateId + " name='radio'" + i + parentId + " data-value=" + res.content[i].answerStates[j] + " type='checkbox' parentId=" + res.content[i].answerStates[j].parentStateId + " onchange=matChange(this)>" + res.content[i].answerStates[j].stateName + "<span></span></label>"
                                    }
                                    childT = childT + childC + '</span>'
                                }
                                childState.append(childT);
                            }
                        } else {
                            var newChild = $('.child');
                            for (var i = 0; i < newChild.length; i++) {
                                if (parentId == newChild.attr('data-id')) {
                                    newChild.remove();
                                }
                            }
                        }
                    } else {
                        vm.$message.error(res.message);
                    }
                }, function () {
                    vm.$message.error('根据父情形查找子情形接口失败，请重试！');
                });
            },
            //根据情形获取材料
            getMatState: function () {
                var vm = this;
                var id = [];
                if (this.mustAnswer.length > 0) {
                    this.$message({
                        message: '请选择必选项之后再查看材料',
                        type: 'error'
                    });
                    return;
                }
                $.each(this.matId, function (index, val) {
                    id.push(val.itemStateId);
                });
                request('', {
                    url: ctx + 'rest/guide/matState/list',
                    type: 'get',
                    data: {
                        itemStateIds: id.toString()
                    }
                }, function (res) {
                    if (res.success) {
                        vm.detailData.matList = vm.firstMat.concat(res.content)
                    } else {
                        vm.$message.error(res.message);
                    }
                }, function () {
                    vm.$message.error('根据情形获取材料接口失败，请重试！');
                });
            },


            tableToggle: function (e, index) {

                e.cancelBubble = true;

                this.curIndex = index;

                if (!e) {
                    $(".items>li").eq(0).addClass('activeLi').siblings().removeClass('activeLi');
                    return;
                }
                if (e.target.localName == "span") {
                    $(e.target).parents("li").addClass('activeLi').siblings().removeClass('activeLi');
                } else {
                    $(e.target).addClass('activeLi').siblings().removeClass('activeLi');
                }


            },
            tableToggle2: function (e, index) {

                e.cancelBubble = true;

                this.curIndex2 = index;

                if (!e) {
                    $(".items2>li").eq(0).addClass('activeLi').siblings().removeClass('activeLi');
                    return;
                }
                if (e.target.localName == "span") {
                    $(e.target).parents("li").addClass('activeLi').siblings().removeClass('activeLi');
                } else {
                    $(e.target).addClass('activeLi').siblings().removeClass('activeLi');
                }
            },
            // 跳转单项办事指南
            switchSinglePage: function (row) {
                var _this = this;
                var id = row.itemVerId;
                var outerSystemUrl = row.outerSystemUrl;
                var singleTitle= row.itemName;
                var isLink = row.isLink;
                if (!!isLink && isLink == '1') {
                    if (!outerSystemUrl) {
                        vm.$message.warning("该事项暂无办事指南！");
                    } else {
                        window.open(outerSystemUrl);
                    }
                } else {
                   //this.isSinglePage = true;
                    window.location.href = ctx + '/rest/main/toIndexPage?id='+id+'&singleTitle='+encodeURI(singleTitle)+'#/singlePage'
                }
            },
            // 查看流程图
            previewRow:function (detailId) {
                if(viewer)viewer.destroy();//当存在viewer对象，先销毁
                viewer = new Viewer(document.getElementById(detailId), {
                    url: 'data-original'
                });
                viewer.show();//展示图片
            },

            handleSizeChange: function (val) {
                this.pageSize = val;
                this.getItemList(this.orgId);
            },
            handleCurrentChange: function (val) {
                this.page = val;
                this.getItemList(this.orgId);
            },
        },
        computed: {
            dataOriginal:function() {
                return function (detailId) {
                    return ctx+'/rest/file/applydetail/mat/download/' + detailId
                }
            },
        },
        watch: {}
    })
    function goToStageApply(){
        window.location.href =ctx + "rest/main/toIndexPage?#/listmatter";
    }

    window.goToStageApply = goToStageApply;

    return {
        vm: vm,
    }
})();
