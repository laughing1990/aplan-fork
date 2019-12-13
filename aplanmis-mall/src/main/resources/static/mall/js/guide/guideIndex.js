/* 
* @Author: anchen
* @Date:   2019-07-15 09:32:28
* @Last Modified by:   anchen
* @Last Modified time: 2019-08-05 13:53:25
*/

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
            secOrgId:'',
            firAeaOrgVo:[{orgName:''}], // 区域市
            secAeaOrgVo:[{orgName:''}], // 区域 宣城
            childAeaOrgVo:[],
            cityOrgName:'',
            aeaActiveOrgName:'',
            childAeaOrgVoOrgName:'选择区县',
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
            imgUrl: ["common/stage/mainLine/images/立项用地许可.png", "common/stage/mainLine/images/工程建设许可.png", "common/stage/mainLine/images/施工许可.png", "common/stage/mainLine/images/竣工验收.png", "common/stage/mainLine/images/立项用地许可.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png"],
        },
        mounted: function () {
            var vm = this;

            vm.stepScroll();
            vm.GetRequest();
            vm.getRegionList();

            this.sessionStorage = JSON.parse(sessionStorage.getItem("stageData"))|| {origin:false};
            this.sessionStorage2 = JSON.parse(sessionStorage.getItem("orgData")) || {origin2:false};

        },
        methods: {
            init: function () {
                this.getThemeList();
            },
            stepScroll:function(){
                setTimeout(function () {
                    ele = $('#floorTab .department-name');
                    eleLen = ele.length;
                    vm.init()
                }, 20)
                var flag = true;
                // end 滚动时，固定左侧的menu 并导航到相对位置
                // 点击左侧滚动导航条 start
                $('.ciclebox').click(function () {
                    var ele = $(this).children('a').attr('href');
                    $(ele).addClass('active').siblings('.div_step').removeClass('active');
                    $(this).addClass('active').siblings('.ciclebox').removeClass('active');
                });
                var rightFixed = $('.right-content .other');
                var top = rightFixed.offset().top;
                $(document).scroll(function () {
                    var scroH = $(this).scrollTop();
                    if (scroH > 630) {
                        rightFixed.css({"position": "fixed"});
                    } else if (scroH < 630) {
                        rightFixed.css({"position": "static"});
                    }

                    if (flag) {

                        var items = $(".div-step");
                        var menu = $("#menu");
                        var top = $(document).scrollTop();
                        var currentId = ""; //滚动条现在所在位置的item id
                        var cl = '';
                        var h = $(window).height() / 2;
                        items.each(function () {
                            var m = $(this);
                            //m.offset().top代表每一个item的顶部位置
                            if (top > m.offset().top - h / 2) {
                                currentId = "#" + m.attr("id");
                                cl = m.attr("id");
                            } else {
                                return false;
                            }
                        });
                        var currentLink = menu.find(".active");
                        if (currentId && currentLink.attr("href") != currentId) {
                            currentLink.removeClass("active");
                            menu.find("[data-name=" + cl + "]").addClass("active");
                            $(currentId).addClass("active").siblings().removeClass("active");
                        }
                    }
                })
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
                request('', {
                    url: ctx + 'rest/main/theme/list',
                    type: 'get'
                }, function (res) {
                    vm.themeLoading = false;
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
                }, function () {
                    vm.$message.error('获取主题列表数据接口失败，请重试！');
                });
            },
            toUserCenterindexPage: function (itemVerId) {
                window.location.href = ctx + "rest/main/toIndexPage?itemVerId=" + itemVerId + '&&projInfoId=null&&flag=singleD#declare';
            },
            //查看主题流程图
            showProcess: function () {
                var vm = this;
                request('', {
                    url: ctx + 'rest/guide/theme/file/list/' + vm.themeId,
                    type: 'get'
                }, function (res) {
                    if (res.success) {
                        if (!res.content.detailId) {
                            vm.$message({
                                message: '暂无流程图',
                                type: 'error'
                            });
                            return
                        }
                        // window.open(ctx + 'rest/file/att/preview/' + res.content.detailId);
                       vm.previewFile(res.content)
                    } else {
                        vm.$message.error(res.message);
                    }
                }, function () {
                    vm.$message.error('获取附件失败，请重试！');
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
                // if(getSrc){
                //     _that.getViewIframeSrc = ctx+'cod/drawing/drawingCheck?detailId='+detailId;
                // }else {
                //     var tempwindow=window.open(); // 先打开页面
                //     setTimeout(function(){
                //         tempwindow.location=ctx+'cod/drawing/drawingCheck?detailId='+detailId;
                //     },1000)
                // }
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
                debugger;
                    window.open(ctx+'rest/file/att/preview?detailId='+detailId);
            },

            goToGuid: function (e, themeId, indexSub, themeMemo) {
                var vm = this;
                vm.activeSub = indexSub;
                vm.themeMemo = themeMemo;
                vm.themeId = themeId;
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
                // onloadStageTypeList(isMainline,isAuxiline,mainlineAlias,auxilineAlias);
                $.ajax({
                    url: ctx + "rest/main/stage/list/" + themeId,  // 根据主题获取阶段接口
                    type: "get",
                    async: false,
                    success: function (res) {
                        var result = res.content
                        if (result == null || result.length == 0 || result == "" || result == "根据主题获取阶段接口异常") {
                            $("#mainLine #stageImg").empty();
                            $('#auxiliaryLine #stageAuxiliaryImg').empty();
                            $("#mainLine #stageImg").append("该主题下暂无阶段信息，请配置");
                            $('#auxiliaryLine #stageAuxiliaryImg').append("该主题下暂无阶段信息，请配置");
                            vm.countStageType1 = false;
                            vm.countStageType2 = false;
                            $('#mainLine').children("p").css("display", 'none');
                            $('#auxiliaryLine').children("p").css("display", 'none');
                            vm.memoLoading = false;
                        } else {
                            vm.memoLoading = false;

                            $("#stageImg").empty();
                            $('#stageAuxiliaryImg').empty();
                            var content = "";
                            var content_aux = "";
                            var countStageType = 0;

                            var mainLine = false;
                            var assitLine = false;
                            var mainId = [];
                            var assitId = [];
                            var imgUrl= vm.imgUrl;
                            for (var i = 0; i < result.length; i++) {
                                countStageType++;
                                if (result[i].isNode == "1") {//主线
                                    // flag=firstFlag?false:true;
                                    mainLine = true;
                                    mainId.push(result[i].stageId)
                                    content += '<li class="show" style="cursor: pointer;" onclick=getGuideDetail(this,"' + result[i].stageId + '") id=' + result[i].stageId + '>';
                                    content += '<img src="' + ctx + imgUrl[i] + '" alt=""><div class="stage-name"><p class="ellipsis">' + result[i].stageName + '</p><p class="stage-dealine">' + result[i].dueNum + '个工作日</p></div><img src="' + ctx + 'mall/images/icon/1.png" alt="" class="sing"><p class="sing-text">' + (i + 1) + '</p><div class="wrapperGuide">\n' +
                                        '<p  onclick="goToStageApply()">我要申报</p>\n' +
                                        '</div></li>';
                                    vm.countStageType1 = true;
                                } else {//辅线
                                    // flag=firstFlag?true:false;  备注 之前的动态图片路径   ctx + result[i].hugeImgPath
                                    assitLine = true;
                                    assitId.push(result[i].stageId)
                                    content_aux += '<li class="show" style="cursor: pointer;" onclick=getGuideDetail2(this,"' + result[i].stageId + '") id=' + result[i].stageId + '>';
                                    content_aux += '<img src="' + ctx + imgUrl[i] + '" alt=""><div class="stage-name"><p class="ellipsis">' + result[i].stageName + '</p><p class="stage-dealine">' + result[i].dueNum + '个工作日</p></div><img src="' + ctx + 'mall/images/icon/1.png" alt="" class="sing"><p class="sing-text">1</p><div class="wrapperGuide">\n' +
                                        '<p  onclick="goToStageApply()">我要申报</p>\n' +
                                        '</div></li>';
                                    vm.countStageType2 = true;
                                }
                            }
                            if (mainLine) {
                                vm.countStageType1 = true;
                            } else {
                                vm.countStageType1 = false;
                            }
                            if (assitLine) {
                                vm.countStageType2 = true;
                            } else {
                                vm.countStageType2 = false;
                            }
                            if (mainLine && assitLine) {
                                // $('#mainLine').children("p").css("display", 'block');
                                $('#auxiliaryLine').children("p").css("display", 'block');
                            } else {
                                // $('#mainLine').children("p").css("display", 'none');
                                $('#auxiliaryLine').children("p").css("display", 'none');
                            }
                            $("#mainLine #stageImg").append(content);
                            $('#auxiliaryLine #stageAuxiliaryImg').append(content_aux);

                            if (mainId.length > 0) {
                                if (vm.sessionStorage.origin && vm.sessionStorage.mainLineStageId) {
                                    getGuideDetail('', vm.sessionStorage.mainLineStageId);
                                } else {
                                    getGuideDetail('', mainId[0]);
                                }
                                if (vm.sessionStorage.origin && vm.sessionStorage.auxiliaryLineStageId) {
                                    getGuideDetail2('', vm.sessionStorage.auxiliaryLineStageId);
                                } else {
                                    getGuideDetail2('', assitId[0]);
                                }
                            }
                        }
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
            //首页 --> 按部门申报 --> 获取所有区域列表
            getRegionList:function(){
                var vm = this;
                request('', {
                    url: ctx + 'rest/main/region/list',
                    type: 'get'
                }, function (res) {
                    if(res.success){
                        var content = res.content;
                        vm.firAeaOrgVo = [];
                        vm.firAeaOrgVo.push(content.firAeaOrgVo);
                        vm.secAeaOrgVo = content.secAeaOrgVo;
                        vm.secAeaOrgVo = content.secAeaOrgVo;

                        if(vm.secAeaOrgVo[0]  && vm.secAeaOrgVo[0].childAeaOrgVo){
                            vm.childAeaOrgVo = vm.secAeaOrgVo[0].childAeaOrgVo;
                        }


                        vm.cityOrgName = vm.firAeaOrgVo[0].orgName;

                        if(vm.sessionStorage2 && vm.sessionStorage2.aeaActiveOrgName){
                            vm.aeaActiveOrgName = vm.sessionStorage2.aeaActiveOrgName;
                            vm.childAeaOrgVoOrgName = vm.sessionStorage2.childAeaOrgVoOrgName;

                            vm.childAeaOrgVo = vm.sessionStorage2.childAeaOrgVo;

                            vm.areaOnIndex = vm.sessionStorage2.areaOnIndex;
                            vm.cityOnIndex = vm.sessionStorage2.cityOnIndex;
                            vm.secOrgId = vm.sessionStorage2.secOrgId;
                            vm.getOrgList(vm.secOrgId);
                            return;
                        }else{
                            if(vm.secAeaOrgVo[0] && vm.secAeaOrgVo[0].orgName){
                                vm.aeaActiveOrgName = vm.secAeaOrgVo[0].orgName;
                            }
                        }

                        if(content.firAeaOrgVo && content.firAeaOrgVo.orgDeptList.length>0){
                            vm.OrgListData = content.firAeaOrgVo.orgDeptList;
                            vm.orgNum = content.firAeaOrgVo.orgDeptList.length;
                            vm.aeaActiveOrgName = "请选择"
                            vm.childAeaOrgVoOrgName = "请选择"
                        }else{
                            vm.secOrgId = vm.secAeaOrgVo[0].orgId;
                            vm.getOrgList(vm.secAeaOrgVo[0].orgId);
                        }

                    }else{
                        vm.$message.error(res.message);
                    }
                },function () {
                    vm.$message.error('获取部门列表数据接口失败，请重试！');
                });
            },
            // 选城市或省
            chooseCityItem:function(){
                var vm = this;
                if(this.firAeaOrgVo[0].orgDeptList instanceof Array){
                    if(this.firAeaOrgVo[0].orgDeptList.length>0){
                        this.OrgListData = this.firAeaOrgVo[0].orgDeptList;
                        this.aeaActiveOrgName = "请选择"
                        this.childAeaOrgVoOrgName = "请选择"
                    }else{
                        this.$message.warning("选择的地方暂时没有部门列表呢")
                    }
                }else{
                    console.log("接口返回的orgDeptList不是一个数组类型")
                }
            },
            // 选择区
            chooseAreaItem:function(item,index){
                this.areaOnIndex = index;
                this.aeaActiveOrgName = item.orgName;
                vm.childAeaOrgVo = item.childAeaOrgVo;
                this.secOrgId = item.orgId;
                this.childAeaOrgVoOrgName = "选择区县"
                this.cityOnIndex = "-1";
                this.getOrgList(item.orgId);
            },
            choosechildAeaOrgVoItem:function(item,index){
                this.cityOnIndex = index;
                this.childAeaOrgVoOrgName = item.orgName;
                this.secOrgId = item.orgId;
                this.getOrgList(item.orgId);
            },
            //获取部门列表
            getOrgList: function (orgId) {
                var vm = this;
                vm.orgLoading = true;
                request('', {
                    url: ctx + 'rest/main/org/list?orgId='+ orgId,
                    type: 'get'
                }, function (res) {
                    if (res.success) {
                        vm.OrgListData = res.content;
                        vm.orgNum = res.content.length;
                        if(vm.orgNum>0){
                            vm.getItemList(res.content[0].orgId);
                            vm.orgId = res.content[0].orgId;
                            vm.orgLoading = false;
                            vm.orgItem = res.content[0].orgName;
                            vm.$nextTick(function () {
                                if (vm.sessionStorage2.origin2) {
                                    vm.approvalwayTypeFlag = false;
                                    vm.toggleOrg('', vm.sessionStorage2);
                                }
                            })
                        }else{
                            vm.orgLoading = false;
                            //vm.$message.warning('获取部门数据为空');
                        }
                    } else {
                        vm.$message.error(res.message);
                        vm.orgLoading = false;
                    }
                }, function () {
                    vm.$message.error('获取部门列表数据接口失败，请重试！');
                });
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
            //按部门获取列表
            getItemList: function (orgId) {
                var vm = this;
                vm.orgLoading = true;
                request('', {
                    url: ctx + 'rest/guide/item/list',
                    type: 'get',
                    data: {
                        pageNum: this.page,
                        pageSize: this.pageSize,
                        orgId: orgId
                    }
                }, function (res) {
                    vm.orgLoading = false;
                    if (res.success) {
                        vm.tableData = res.content.list;
                        vm.orgNum = res.content.total;
                        vm.total = res.content.total;
                    } else {
                        vm.orgLoading = false;
                        vm.$message.error(res.message);
                    }
                }, function () {
                    vm.$message.error('按部门获取事项列表接口失败，请重试！');
                });
            },
            toggleOrg: function (e, item) {
                this.getItemList(item.orgId);
                this.orgId = item.orgId;
                this.orgItem = item.orgName;
                if (e) {
                    $(e.target).addClass('orgActive').siblings().removeClass('orgActive');
                } else {
                    var orgs = $('.department-name');
                    orgs.removeClass('orgActive');
                    for (var i = 0; i < orgs.length; i++) {
                        if (orgs.eq(i).attr('id') == item.orgId) {
                            orgs.eq(i).addClass('orgActive');
                            this.getItemList(item.orgId);
                        }
                    }
                }

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
                var isLink = row.isLink;
                if (!!isLink && isLink == '1') {
                    if (!outerSystemUrl) {
                        vm.$message.warning("该事项暂无办事指南！");
                    } else {
                        window.open(outerSystemUrl);
                    }
                } else {
                    this.isSinglePage = true;
                    request('', {
                        url: ctx + 'rest/guide/guide/single/detailed/' + id,
                        type: 'get',
                    }, function (res) {
                        if (res.success) {
                            _this.detailData = res.content;
                            _this.firstMat = _this.detailData.matList;
                            $.each(_this.detailData.stateList, function (index, val) {
                                if (val.mustAnswer == '1') {
                                    _this.mustAnswer.push(val.itemStateId);
                                }
                            });
                            if (_this.detailData.stateList.length > 0) {
                                _this.stateListFlag = true;
                            } else {
                                _this.stateListFlag = false;
                            }
                        } else {
                            vm.$message.error(res.message);
                        }
                    }, function () {
                        vm.$message.error('单项办事指南接口失败，请重试！');
                    });
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
        location.href =ctx + "rest/main/toIndexPage?projInfoId=null#declare";
    }

    window.goToStageApply = goToStageApply;

    return {
        vm: vm,
    }
})();
