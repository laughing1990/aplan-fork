var guideIndex = (function () {
    var ele,
        eleLen;
    var window = this;
    var viewer;// viewer 对象


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
            mats: [],
            __QITA__: [],
            childState: [],
            mustAnswer: [],//必选答案的集合
            sessionStorage: {},
            sessionStorage2: {},
            itemProjInfoId: '', // 单项申报项目id
            stateListFlag: false,
            filePreviewCount: 0,
            showProcessLoading: false,
            getViewIframeSrc: '',
            imgUrl: ["common/stage/mainLine/images/立项用地许可.png", "common/stage/mainLine/images/工程建设许可.png", "common/stage/mainLine/images/施工许可.png", "common/stage/mainLine/images/竣工验收.png", "common/stage/mainLine/images/立项用地许可.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/工程建设许可.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png"],
            itemListKeyword:'',
            stagesData:{}, // 某主题下的阶段和事项
            fullscreenLoading: false,
            singleDialog: false,
            singleLoading:false,
            itemName:'',//单项办事指南标题
            singleData:[] // 单项办事指南数据
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
            // 获取单项办事指南数据
            getSinleData:function(itemVerId,itemName){
                var vm = this;
                this.singleDialog = true;
                this.singleLoading = true;
                this.itemName = itemName;
                request('', {
                    url: ctx + 'rest/guide/detailed/'+ itemVerId,
                    type: 'get',
                }, function (res) {
                    vm.singleLoading = false;
                    if (res.success) {
                        var content = res.content;
                        vm.singleData = content;
                    } else {
                        vm.singleLoading = true;
                        vm.$message.error(res.message);
                    }

                },function () {
                    vm.$message.error('接口请求失败');
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
             // 跳转办事指南一单清
            goToStageApply:function(stageId){
                if(!stageId){
                    this.$message.error("获取不到当前阶段id,无法跳转")
                    return
                }
                window.location.href =ctx + "rest/main/toIndexPage?stageId="+stageId+"#/listmatter";
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


    return {
        vm: vm,
    }
})();
