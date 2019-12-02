
var mainIndex = (function () {
    var ele,
    eleLen;
    var vm = new Vue({
    el:'#mainIndex',
    data:{
        ctx:ctx,
        searchBtnLoading:false,
        showProcessLoading: false, // 查看流程图loading
        themeLoading:false,
        themeContentLoading:false,
        approvalwayTypeFlag:true,
        approvalWayActiveName:1,
        activeSub:0,
        staticData:{},
        OrgListData:{},
        secOrgId:'',
        firAeaOrgVo:[{orgName:''}], // 区域市
        secAeaOrgVo:[{orgName:''}], // 区域 宣城
        childAeaOrgVo:[],
        cityOrgName:'',
        aeaActiveOrgName:'',
        childAeaOrgVoOrgName:'请选择',
        areaOnIndex:'',
        cityOnIndex:'',
        themeListData:{},
        approveListData:[],
        applyinstListTableData:[],
        themeMemo:'',
        themeId:'',
        keyword:'',
        pageNum:1,
        pageSize:10,
        total:0,
        applyinstFlag:1,
        proApprovalNum:1,
        ProApproveListData:[],
        pageSizeProApprove:10,
        pageNumProApprove:1,
        applyinstCode:'',
        projInfoName:'',
        ProApproveTotal:0,
        num:'',
        isNode:true,
        imgUrl:["common/stage/mainLine/images/立项用地许可.png","common/stage/mainLine/images/工程建设许可.png","common/stage/mainLine/images/施工许可.png","common/stage/mainLine/images/竣工验收.png","common/stage/mainLine/images/立项用地许可.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png","common/stage/mainLine/images/5.png"],
        itemStateDes:["本阶段分为项目立项、用地预审（如涉及）、建设用地规划许可三个审批事项办理。","本阶段为设计方案审查和建设工程规划许可证核发两个事项。","本阶段为施工图设计文件审查和施工许可证核发两个事项。","竣工验收阶段分为各部门限时联合验收服务和竣工验收备案两个事项。"],
        filePreviewCount: 0,

        // 查询事项和主题
        stageImgUrl:['mall/images/index/1.png','mall/images/index/2.png','mall/images/index/3.png','mall/images/index/4.png','mall/images/index/1.png','mall/images/index/2.png','mall/images/index/3.png','mall/images/index/4.png'],
        selectType:'1', // 默认选中事项
        itemListKeyword:"",
        itemListPageNum:1,
        itemListPageSzie:10,
        itemListTotal:0,
        itemListLoading:false,
        itemListMoreFlag:true,
        itemListActiveName:1,
        itemListData:[],
        themeListKeyword:"",
        themeListPageNum:1,
        themeListPageSzie:10,
        themeListTotal:0,
        themeListLoading:false,
        themeListMoreFlag:true,
        themeListData2:[],
    },
    mounted:function() {
        var vm = this;
      //do something after mounting vue instance
        vm.getStatics();
        vm.getThemeList();
        vm.getApproveList();
        vm.getRegionList();

        $(window).scroll(function(){
            if ($(window).scrollTop()>100){
                $("#toTop").fadeIn(1500);
            }
            else
            {
                $("#toTop").fadeOut(1500);
            }
        });

    },
    methods: {
        //首页-->根据项目名称编码流水号查询功能接口
        getApplyinstList:function(){
            var vm = this;
             vm.applyinstFlag = 2;
             vm.searchBtnLoading = true;
            request('', {
                   url: ctx + 'rest/main/applyinst/list',
                   type: 'get',
                   data:{
                      keyword:vm.keyword,
                      pageNum:vm.pageNum,
                      pageSize:vm.pageSize,
                   }
               }, function (res) {
                    vm.searchBtnLoading = false;
                      var list = res.list;
                      vm.applyinstListTableData = list;
                      vm.total = res.total;
               },function () {
                    vm.searchBtnLoading = false;
               });
        },
        handleSizeChange:function(val){
            this.pageSize = val;
            this.getApplyinstList()
        },
        handleCurrentChange:function(val){
            this.pageNum = val;
            this.getApplyinstList()
        },
        backToIndex:function(){
            vm.applyinstFlag = 1;
            vm.keyword = "";
        },
        //首页-->获取审批情况列表数据
        getApproveList:function(){
            var vm = this;
            var demo2 = document.getElementById("demo2");
            request('', {
                   url: ctx + 'rest/main/approve/list',
                   type: 'get'
               }, function (res) {
                  console.log(res)
                  if(res.success){
                      var content = res.content;
                      vm.approveListData = content;
                      if(vm.approveListData && vm.approveListData.length == 0){
                          $("#demo2").empty();
                      }else{
                          $("#demo1 .noData").empty();
                      }
                      //首页审批情况滚动
                       var speed = 50;
                       demo2.innerHTML = demo1.innerHTML;
                       function Marquee() {
                           if ($("#demo2").length>0) {
                           if(demo2 != 'undefined'){
                               if (demo2.offsetTop - demo.scrollTop <= 0) {
                                   demo.scrollTop -= demo1.offsetHeight;
                               } else {
                                   demo.scrollTop++;
                               }
                           }else{
                               clearInterval(MyMar)
                           }
                       }
                       }
                       var MyMar = setInterval(Marquee, speed);
                       demo.onmouseover = function() {
                           clearInterval(MyMar);
                       }

                       demo.onmouseout = function() {
                           MyMar = setInterval(Marquee, speed);
                       }
                  }else{
                      vm.$message.error(res.message);
                  }
               },function () {

               });
        },
        //首页-->获取首页所有静态数字接口
        getStatics:function(){
            var vm = this;
            request('', {
                   url: ctx + 'rest/main/statictics/item',
                   type: 'get'
               }, function (res) {
                  console.log(res)
                  if(res.success){
                      var content = res.content;
                      vm.staticData = content
                  }else{
                      vm.$message.error(res.message);
                  }

               },function () {
                   vm.$message.error('获取首页静态统计数据接口失败，请重试！');
               });
        },
        //首页-->按主题申报-->获取主题列表
        getThemeList:function(){
            var vm = this;
            vm.themeLoading = true;
            request('', {
                   url: ctx + 'rest/main/theme/list',
                   type: 'get'
               }, function (res) {
                   vm.themeLoading = false;
                  if(res.success){
                      var content = res.content;
                      vm.themeListData = content;
                      setTimeout(function(){
                          if(vm.themeListData.length  === 1){
                              $(".theme-type .theme-type-list").css({'height':664+'px'});
                          }else if(vm.themeListData.length  === 2){
                              $(".theme-type .theme-type-list").css({'height':604+'px'});
                          }else if(vm.themeListData.length  === 3){
                              $(".theme-type .theme-type-list").css({'height':549+'px'});
                          }else if(vm.themeListData.length  === 4){
                              $(".theme-type .theme-type-list").css({'height':494+'px'});
                          }else if(vm.themeListData.length  === 5){
                              $(".theme-type .theme-type-list").css({'height':438+'px'});
                          }else if(vm.themeListData.length  === 6){
                              $(".theme-type .theme-type-list").css({'height':386+'px'});
                          }else if(vm.themeListData.length  === 7){
                              $(".theme-type .theme-type-list").css({'height':203+'px'});
                          }else if(vm.themeListData.length  === 8){
                              $(".theme-type .theme-type-list").css({'height':146+'px'});
                          }
                      },0);
                  }else{
                      vm.themeLoading = false;
                      vm.$message.error(res.message);
                  }
                  vm.goToGuid(vm.themeListData[0].themeList[0],0);
               },function () {
                   vm.$message.error('获取部门列表数据接口失败，请重试！');
               });
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

                    if(vm.secAeaOrgVo[0] && vm.secAeaOrgVo[0].childAeaOrgVo){
                        vm.childAeaOrgVo = vm.secAeaOrgVo[0].childAeaOrgVo;
                    }
                    vm.cityOrgName = vm.firAeaOrgVo[0].orgName;

                    if(vm.secAeaOrgVo[0] && vm.secAeaOrgVo[0].orgName){
                        vm.aeaActiveOrgName = vm.secAeaOrgVo[0].orgName;
                    }

                    if(content.firAeaOrgVo && content.firAeaOrgVo.orgDeptList.length>0){
                        vm.OrgListData = content.firAeaOrgVo.orgDeptList;
                        vm.aeaActiveOrgName = "请选择"
                        vm.childAeaOrgVoOrgName = "请选择"
                        vm.init();
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
                if(this.firAeaOrgVo[0].orgDeptList && this.firAeaOrgVo[0].orgDeptList.length>0){
                    this.OrgListData = this.firAeaOrgVo[0].orgDeptList;
                }else{
                    this.OrgListData = this.firAeaOrgVo[0].orgDeptList;
                }
                this.aeaActiveOrgName = "请选择"
                this.childAeaOrgVoOrgName = "请选择"
                vm.init();
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
            this.childAeaOrgVoOrgName = "请选择"
            this.cityOnIndex = "-1";
            this.getOrgList(item.orgId);
        },
        choosechildAeaOrgVoItem:function(item,index){
            this.cityOnIndex = index;
            this.childAeaOrgVoOrgName = item.orgName;
            this.secOrgId = item.orgId;
            this.getOrgList(item.orgId);
        },
        //首页-->按部门申报-->获取所有部门列表
        getOrgList:function(orgId){
            var vm = this;
            request('', {
                   url: ctx + 'rest/main/org/list?orgId='+ orgId,
                   type: 'get'
               }, function (res) {
                  if(res.success){
                      var content = res.content;
                      vm.OrgListData = content;
                  }else{
                      vm.$message.error(res.message);
                  }
                  vm.init();
               },function () {
                   vm.$message.error('获取部门列表数据接口失败，请重试！');
               });
        },
        init:function(){
            var vm = this;
            // 按部门申报  部门默认最多展示12条
            setTimeout(function(){
                ele = $('#floorTab .department-name');
                eleLen = ele.length;
                if (eleLen <= 12) {
                    $('.show-more-department').hide();
                } else {
                    vm.hideMoreDepart();
                    $('.show-more-department').show();
                }
                $('.show-more-department').text("全部展开").removeClass('close');
                vm.hideMoreDepart();
            },20)

        },
        goToGuid:function(itemSub,indexSub){
            var vm =this;
            vm.themeContentLoading = true;
            vm.activeSub = indexSub;
            var isMainline = itemSub.isMainline;
            var isAuxiline= itemSub.isAuxiline;
            var mainlineAlias = itemSub.mainlineAlias;
            var auxilineAlias = itemSub.auxilineAlias;
            var themeType = itemSub.themeType;
            var themeId = itemSub.themeId;
            var themeMemo = itemSub.themeMemo;
            vm.themeMemo = themeMemo;
            vm.themeId = themeId;
            onloadStageTypeList(isMainline,isAuxiline,mainlineAlias,auxilineAlias);
            //var themeType=$('#mallThemeTypeList').children(".active").attr("id");
            $.ajax({
                    url:ctx+"rest/main/stage/list/"+themeId,  // 根据主题获取阶段接口
                    type: "GEt",
                    async:false,
                    data: {'themeId':themeId},
                    success:function (res) {
                        vm.themeContentLoading = false;
                        var result = res.content
                        if(result == null || result.length==0 || result ==""){
                            $("#mainLine #stageImg").empty();
                            $("#mainLine2 #stageImg2").empty();
                            $('#auxiliaryLine #stageAuxiliaryImg').empty();
                            $("#mainLine #stageImg").append("该主题下暂无阶段信息，请配置");
                            $('#auxiliaryLine #stageAuxiliaryImg').append("该主题下暂无阶段信息，请配置");
                        }else{
                            $("#stageImg").empty();
                            $("#stageImg2").empty();
                            $('#stageAuxiliaryImg').empty();
                            var content="";
                            var content_aux="";
                            var countStageType=0;
                            var flag=true;
                            var firstFlag=result[0].isNode=="1"?true:false;
                            var imgUrl= vm.imgUrl;
                            var itemStateDes = vm.itemStateDes;
                            var minHeight = false; // 当 stageName 长度不一致时(有的一行，有的三行)，控制工作日对齐
                            var minHeightNum1 = 22;
                            var minHeightNum2 = 22;
                            for(var i = 0;i<result.length;i++) {
                                countStageType++;
                                if(result[i].isNode=="1"){//主线
                                    flag=firstFlag?false:true;
                                    content+='<li class="show">';
                                    content+='<img src="'+ctx+imgUrl[i]+'" alt=""><div class="stage-name"><p class="state-name-text">'+result[i].stageName+'</p><p class="stage-dealine">'+result[i].dueNum+'工作日</p><div class="stage-des">'+result[i].stageMemo+'</div></div><div class="wrapper">\n' +
                                        '<p  onclick="goToStageApply()">我要申报</p><p onclick="goToStage(\''+themeId+'\',\''+result[i].stageId+'\',\'\',\''+themeMemo+'\')">办事指南</p>\n' +
                                        '</div><img src="'+ctx+'mall/images/icon/1.png" alt="" class="sing"><p class="sing-text">'+(i+1)+'</p></li>';
                                }else{//辅线
                                    flag=firstFlag?true:false;
                                    content_aux+='<li class="show" onclick="goToStage(\''+themeId+'\',\'\',\''+result[i].stageId+'\',\''+themeMemo+'\')">';
                                    content_aux+='<img src="'+ctx+imgUrl[i]+'" alt=""><div class="stage-name"><p class="state-name-text">'+result[i].stageName+'</p><p class="stage-dealine">'+result[i].dueNum+'工作日</p></div><div class="wrapper">\n' +
                                        '<p  onclick="goToStageApply()">我要申报</p><p onclick="goToStage(\''+themeId+'\',\'\',\''+result[i].stageId+'\',\''+themeMemo+'\')">办事指南</p>\n' +
                                        '</div><img src="'+ctx+'mall/images/icon/1.png" alt="" class="sing"><p class="sing-text">1</p></li>';
                                }

                                if(12 < result[i].stageName.length && result[i].stageName.length< 23){
                                    minHeight = true;
                                    minHeightNum1 = 44;
                                }else if(23 < result[i].stageName.length && result[i].stageName.length< 30){
                                    minHeight = true;
                                    minHeightNum2 = 66;
                                }
                            }
                            if(!flag || countStageType==0){//并联审批和服务协调只有其一时，或者两者都没配置时，隐藏标题
                                $('#mainLine').children("p").attr("hidden",true);
                                $('#auxiliaryLine').children("p").attr("hidden",true);
                            }else{
                                $('#mainLine').children("p").attr("hidden",false);
                                $('#auxiliaryLine').children("p").attr("hidden",false);
                            }
                            if(content == null || content == ""){
                                $("#mainLine #stageImg").css('display','none');
                                $("#mainLine2 #stageImg2").css('display','none');
                              vm.isNode = true;
                            }else{
                                $("#mainLine #stageImg").append(content);
                              $("#mainLine2 #stageImg2").css('display','block');
                                $("#mainLine2 #stageImg2").append(content);
                              vm.isNode = false;
                            }
                            // if(content_aux){
                            //     vm.isNode = true;
                            // }else{
                            //     vm.isNode = false;
                            // }
                            $('#auxiliaryLine #stageAuxiliaryImg').append(content_aux);

                            if(minHeight){
                                $("#mainLine2 li .stage-name p:nth-child(1)").css("min-height",Math.max(minHeightNum1,minHeightNum2)+"px");
                            }

                        }
                    },
                    error:function(){
                        vm.themeContentLoading = false;
                    }
                })
        },
        //查看主题流程图
        showProcess:function(e,themeId){
            var vm = this;
            if(typeof themeId == 'object'){
                vm.$message.warning("themeId值不对，请检查!");
                return;
            }
            var themeId = themeId || vm.themeId;
            request('', {
                url: ctx + 'rest/guide/theme/file/list/'+ themeId,
                type: 'get'
            }, function (res) {
                if(res.success){
                    if(!res.content.detailId){
                        vm.$message({
                            message: '暂无流程图',
                            type: 'error'
                        });
                        return
                    }
                    // window.open(ctx + 'rest/file/att/preview/' + res.content.detailId);
                    vm.previewFile(res.content)
                }else{
                    vm.$message.error(res.message);
                }
            },function () {
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
        previewFile: function(data){ // flag==pdf 查看施工图
          var detailId = data.detailId;
          var _that = this;
          var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
          var fileName=data.attName;
          var fileType = this.getFileType(fileName);
          var flashAttributes = '';
          _that.filePreviewCount++
          if(fileType=='pdf'){
              var tempwindow=window.open(); // 先打开页面
              var loading = _that.$loading({
                  lock: true,
                  text: 'Loading',
                  spinner: 'el-icon-loading',
                  background: 'rgba(0, 0, 0, 0.7)'
              });
            setTimeout(function(){
              tempwindow.location=ctx+'previewPdf/view?detailId='+detailId;
              loading.close();
            },300)
          }else {
            if(regText.test(fileType)){
              _that.showProcessLoading = true;
              request('', {
                url: ctx + 'previewPdf/pdfIsCoverted?detailId='+detailId,
                type: 'get',
              }, function (result) {
                if(result.success){
                  _that.showProcessLoading = false;
                  var tempwindow=window.open(); // 先打开页面
                    var loading = _that.$loading({
                        lock: true,
                        text: 'Loading',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0.7)'
                    });
                  setTimeout(function(){
                    tempwindow.location=ctx+'previewPdf/view?detailId='+detailId;
                      loading.close();
                  },300)
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
                    },300)
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
              var tempwindow=window.open(); // 先打开页面
                var loading = _that.$loading({
                    lock: true,
                    text: 'Loading',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0.7)'
                });
              setTimeout(function(){
                tempwindow.location = ctx + 'rest/file/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
                  loading.close();
              },300)
            }
          }
        },
        // 点击全部展开
        expandAllDep:function(e){
          e.preventDefault();
          var showmoredepartment = $(this.$refs.showmoredepartment);
          if (!showmoredepartment.hasClass('close')) {
            ele.show();
            showmoredepartment.text("收起全部").addClass('close');
          } else {
            showmoredepartment.text("全部展开").removeClass('close');
            this.hideMoreDepart();
          }
      },
      hideMoreDepart:function(){
         for (var i = 0; i < eleLen; i++) {
          if (i > 11) {
            ele.eq(i).hide();
          } else {
            ele.eq(i).show();
          }
        }
      },
      approvelWayActiveChange:function(val){
          var vm = this;
          setTimeout(function(){
               vm.goToGuid(vm.themeListData[val-1].themeList[0],0);
          },500)

      },
        showApprovalPublicity:function (num) {
            this.applyinstFlag = 3;
            this.proApprovalNum = num;
            this.getApproveDataList(num);
        },
        // 首页-->查询受理情况、拟审批意见、审批决定数据接口  0:受理情况,1:拟审批意见,2:审批决定
        getApproveDataList:function(num){
            var vm = this;
            vm.num = num;
            var params = {
                pageSize:vm.pageSizeProApprove,
                pageNum:vm.pageNumProApprove,
                applyinstCode:vm.applyinstCode,
                projInfoName:vm.projInfoName,
                applyState:num-1,
            }
            request('', {
                url: ctx + 'rest/main/approveData/list',
                type: 'get',
                data:params,
            }, function (res) {
                vm.ProApproveListData = res.list;
                vm.ProApproveTotal = res.total;
                vm.total = res.total;
            },function () {

            });
        },
        // 受理情况的列表分页
        handleSizeChangeApprove: function(val){
          this.pageSizeProApprove = val;
          this.getApproveDataList(this.num);
        },
        handleCurrentChangeApprove: function(val){
          this.pageNumProApprove = val;
          this.getApproveDataList(this.num);
        },

        // 查询事项
        getItemList:function () {
            var vm = this;
            vm.applyinstFlag = 4;
            vm.itemListLoading = true;
            vm.getThemeIdList();
            request('', {
                url: ctx + 'rest/main/item/main/list',
                type: 'get',
                data:{
                    keyword:vm.itemListKeyword,
                    pageNum:vm.itemListPageNum,
                    pageSize:vm.itemListPageSzie,
                },
            }, function (res) {
                vm.itemListLoading = false;
                vm.itemListData = vm.itemListData.concat(res.list);
                if(res.list && res.list.length < 10){
                    vm.itemListMoreFlag = false;
                }else{
                    vm.itemListMoreFlag = true;
                }
                vm.itemListTotal = res.total;
            },function () {
                vm.itemListLoading = false;
            });
        },
        searchItemList:function(){
            this.itemListPageNum = 1;
            this.itemListData = [];
            this.getItemList()
        },
        getMoreItemList:function(){
            this.itemListPageNum++;
            this.getItemList()
        },
        // 跳转办事指南
        goToBszn: function(data){
            var itemVerId,itemName;
            if(data.isCatalog==1){
                itemVerId=data.baseItemVerId
            }else {
                itemVerId=data.itemVerId
            }
            itemName = data.itemName;
            var tempwindow=window.open(); // 先打开页面
            setTimeout(function(){
                tempwindow.location = ctx + 'rest/main/toIndexPage?itemVerId='+itemVerId+'&&itemName='+encodeURIComponent(encodeURIComponent(itemName))+'&&flag=bszn#/guideIndex';
            },500)
        },
        // 跳转到单项申报  // 我要申报
        goToSingleDeclare: function(itemVerId,parentItemId){
            var projInfoIdDec=parentItemId;
            var tempwindow=window;
            setTimeout(function(){
                tempwindow.location = ctx + 'rest/main/toIndexPage?itemVerId='+ itemVerId+'&&projInfoId='+projInfoIdDec+'&&flag=singleD#declare';
            },200)
        },

        // 查询主题
        getThemeIdList:function () {
            var vm = this;
            vm.applyinstFlag = 4;
            vm.themeListLoading = true;
            request('', {
                url: ctx + 'rest/main/theme/main/list',
                type: 'get',
                data:{
                    keyword:vm.itemListKeyword,
                    pageNum:vm.themeListPageNum,
                    pageSize:vm.themeListPageSzie,
                },
            }, function (res) {
                vm.themeListLoading = false;
                vm.themeListData2 = vm.themeListData2.concat(res.list);
                if(res.list && res.list.length == 0){
                    vm.themeListMoreFlag = false;
                }else{
                    vm.themeListMoreFlag = true;
                }
                vm.themeListTotal = res.total;
            },function () {
                vm.themeListLoading = false;
            });
        },
        searchThemeList:function(){
            this.themeListPageNum = 1;
            this.themeListData2 = [];
            this.getThemeIdList()
        },
        getMoreThemeList:function(){
            this.themeListPageNum++;
            this.getThemeIdList()
        },
        // 跳到对应的阶段下
        goToGuideStage:function(item,itemSub,idnex){
            var themeId = item.themeId;
            var mainLineStageId = itemSub.stageId;
            var auxiliaryLineStageId = item.auxiliaryLineStageId;
            var themeMemo = item.themeMemo;
            if(!themeId) this.$message.waring("主题Id为空，请检查这条数据是否完整")
            goToStage(themeId,mainLineStageId,auxiliaryLineStageId,themeMemo);
        },

        // 返回到顶部
        returnTop:function () {
            $('body,html').animate({scrollTop:0},500);
            return false;
        },
    },
        filters: {
            formatDate: function(time) {
                if(!time) return "";
                var date = new Date(time);
                return formatDate(date, 'yyyy-MM-dd');
            }
        },
        watch:{
            proApprovalNum:function(newVal,oldVal){
                var vm = this;
                vm.pageSizeProApprove = 10
                vm.pageNumProApprove = 1
                vm.applyinstCode = ""
                vm.projInfoName = ""
                this.getApproveDataList(newVal);
            },
         }
})
    /**
     *  加载阶段类型
     * @param isMainline 是否开启主线 1开启，0关闭
     * @param isAuxiline 是否开启辅线 1开启，0关闭
     * @param mainlineAlias 主线名称
     * @param auxilineAlias 辅线名称
     *  stageTypeList
     */
    function onloadStageTypeList(isMainline,isAuxiline,mainlineAlias,auxilineAlias) {
        $('#stageTypeList ul').empty();
        var lis='';
        if(isMainline==1 && isAuxiline==1){//主线辅线全开
            lis='<li class="active" data-id="#mainLine" onclick="showStageList(this)">'+mainlineAlias+'</li>'+
                '<li data-id="#auxiliaryLine" onclick="showStageList(this)">'+auxilineAlias+'</li>';
            $('#stageTypeList').addClass("show").removeClass("hide");
        }else if(isMainline==1&&isAuxiline==0){//开启主线
            lis='<li class="active" data-id="#mainLine" onclick="showStageList(this)">'+mainlineAlias+'</li>';
            $('#stageTypeList').addClass("hide").removeClass("show");
        }else if(isMainline==0 && isAuxiline==1){//开启辅线
            lis= '<li class="active" data-id="#auxiliaryLine"  onclick="showStageList(this)">'+auxilineAlias+'</li>';
            $('#stageTypeList').addClass("hide").removeClass("show");
        }
        $('#stageTypeList ul').append(lis);
        $('#stageTypeList ul').children(".active").trigger("click");
    }
    function goToOrg(item){
         var orgData = {
             origin2:true,
             aeaActiveOrgName:vm.aeaActiveOrgName,
             childAeaOrgVoOrgName:vm.childAeaOrgVoOrgName,
             secOrgId:vm.secOrgId,
             areaOnIndex:vm.areaOnIndex,
             cityOnIndex:vm.cityOnIndex,
             childAeaOrgVo:vm.childAeaOrgVo,
             orgId:item.id,
             orgName:item.getAttribute("name")
         }
         console.log(orgData);
        sessionStorage.setItem('orgData',JSON.stringify(orgData))
        location.hash =  "/guideIndex";
    }
    // 调转到办事指南的对应的每个阶段下
    function  goToStage(themeId,mainLineStageId,auxiliaryLineStageId,themeMemo) {
        location.hash =  "/guideIndex";
         var stageData = {
             origin:true,
             themeId:themeId,
             themeMemo:themeMemo,
             mainLineStageId:mainLineStageId,
             auxiliaryLineStageId:auxiliaryLineStageId,
         }
        sessionStorage.setItem('stageData',JSON.stringify(stageData))

        var orgData = {
            origin2:false,
            themeId:themeId,
            themeMemo:themeMemo,
            mainLineStageId:mainLineStageId,
            auxiliaryLineStageId:auxiliaryLineStageId,
        }
        sessionStorage.setItem('orgData',JSON.stringify(orgData))
    }

    function goToStageApply(){
        location.href =ctx + "rest/main/toIndexPage?projInfoId=null#declare";
    }

    window.goToStageApply = goToStageApply;
    window.goToStage = goToStage;
    window.goToOrg = goToOrg;
    return {
        vm:vm,
    }
})();
