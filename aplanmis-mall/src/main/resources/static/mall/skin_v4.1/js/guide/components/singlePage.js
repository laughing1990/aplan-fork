var singlePage = (function () {
    var vm = new Vue({
        el:"#singlePage",
        data:{
            ctx: ctx,
            contentLoading: true,
            approvalwayTypeFlag: true,
            approvalWayActiveName: 1,
            tableData: [],
            pageSize: 10,
            page: 1,
            total: 0,
            themeListData: [],
            ellipsisLn: [],
            activeSub: 0,

            themeMemo: '',
            themeId: '',
            orgNum: 0,
            OrgListData: [],
            orgLoading: false,
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
        },
        created:function() {
            this.GetRequest();
        },
        mounted: function () {
            this.stepScroll();
        },
        methods:{
            getSingleData:function (id) {
                var _this = this;
                _this.contentLoading = true;
                request('', {
                    url: ctx + 'rest/guide/guide/single/detailed/' + id,
                    type: 'get',
                }, function (res) {
                    _this.contentLoading = false;
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
                    _this.contentLoading = false;
                    vm.$message.error('单项办事指南接口失败，请重试！');
                });
            },
            GetRequest: function() {
                var url =decodeURI(decodeURI(location.search)); //获取url中"?"符后的字串
                var theRequest = new Object();
                if (url.indexOf("?") != -1) {
                    var str = url.substr(1);
                    strs = str.split("&");
                    for(var i = 0; i < strs.length; i ++) {
                        theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
                    }
                }
                var id = theRequest.id;
                this.singleTitle = theRequest.singleTitle;

                this.getSingleData(id);
                return theRequest;
            },
            stepScroll:function(){
                setTimeout(function () {
                    ele = $('#floorTab .department-name');
                    eleLen = ele.length;
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
        },

    })
})()