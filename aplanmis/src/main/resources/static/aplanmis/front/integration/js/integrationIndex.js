var viewId='';
var vm = new Vue({
  el: '#demo',
  data: function () {
    return {
      keyword:'', // 关键词查询
      portalCountData:{}, // 数量数据
      buttonParams:[],          //视图信息
      noticeContentData: [],    //公告数据
      tableData2: [],            //代办数据
      tableData3: [],            //已办数据
      tableData4:[],
      buttonCounts:[{
          count:3                //代办数量
      },{
          count:3                //已办数量
      },{
          count:0                //网上待预审数量
      }],
      timeTypeobj:[],
      totalItem:{},
      useTimeData:{},
    }
  },
  created:function() {
    this.getPortalCount();
    // this.getStandardTheme();
    // this.getusetime();
    //this.query();
  },
  mounted: function () {
      var _that = this;
      _that.getViewInfo();
      _that.getNotice();
    //  mybjs();
      //xmlxbjs();
      _that.getMybjs();
      _that.getXmlxbj();
  },

  methods: {
      // 获取头部的数字数据
      getPortalCount:function () {
          var _that = this;
          request('', {
              url: ctx+'rest/portal/count',
              type: 'get',
          }, function (res) {
            _that.portalCountData = res.content;
          }, function (msg) {
              _that.$message.error('服务请求失败');
          });
      },
      getViewInfo:function () {
          var _that = this;
          request('', {
              url: ctx+'rest/portal/view_code_info',
              type: 'get',
          }, function (data) {
              if(data !=null ){
                  _that.buttonParams = data;
                  viewId = _that.buttonParams.yi_ban.viewId;
                  _that.getDaiban();
                  _that.getYiban();
                  _that.query();
              }
          }, function (msg) {
              alertMsg('', '服务请求失败', '关闭', 'error', true);
          });
      },
      getNotice:function () {
          var _that = this;
          var props = {
              businessFlag:1,
              pageSize:5
          };
          request('', {
              url: ctx+'/rest/aoa/notice/content/getPageAoaNoticeContentCascade4MyReceive',
              type: 'get',
              data: props
          }, function (data) {
              if(data.content!=null){
                  var rows = data.content.rows;
                  var total = data.content.total;
                  _that.noticeContentData = rows;
                  var tip = total > 0?'none':'';
                  var val = total > 5?'':'none';
                  $('#showMoreNotice').css("display",val);
                  $('#nodata').css("display",tip);
              }
          }, function (msg) {
              alertMsg('', '服务请求失败', '关闭', 'error', true);
          });
      },
      getDaiban:function () {
          var _that = this;
          var props = {
              viewId:_that.buttonParams.dai_ban.viewId,
              'pagination[page]': 1,
              'pagination[pages]': 1,
              'pagination[perpage]': 5,
              'sort[field]': 'CREATE_TIME_',
              'sort[sort]': 'desc'
          };
          request('', {
              url: ctx+'/bpm/front/view/getBootstrapPageViewData',
              type: 'post',
              data: props
          }, function (data) {
              if(data!=null){
                  // var rows = JSON.parse(JSON.stringify(data.rows));
                  // _that.tableData2 = rows.concat(data.rows);
                  var rows = data.rows;
                  var result = [];
                  var num = 5;
                  if(rows.length > num){              //取前4条数据
                      for(var i = 0; i < num; i++) {
                          _that.timeTypeobj.push(_that.dateCount(rows[i].TASK_ID,rows[i].DUE_NUM))
                          result.push(rows[i]);
                      }
                  }else{
                      for(var i = 0; i < rows.length; i++) {
                          _that.timeTypeobj.push(_that.dateCount(rows[i].TASK_ID,rows[i].DUE_NUM))
                      }
                      result = rows;
                  }
                  _that.tableData2 = result;
                  var total = data.total;
                  var val = total > 4?'':'none';
                  $('#showMoreDaiban').css("display",val);
              }
          }, function (msg) {
              alertMsg('', '服务请求失败', '关闭', 'error', true);
          });
      },
      getYiban:function () {
          var _that = this;
          var props = {
              viewId:_that.buttonParams.yi_ban.viewId,
              'pagination[page]': 1,
              'pagination[pages]': 1,
              'pagination[perpage]': 5,
              'sort[field]': 'END_TIME_',
              'sort[sort]': 'desc'
          };
          request('', {
              url: ctx+'/bpm/front/view/getBootstrapPageViewData',
              type: 'post',
              data: props
          }, function (data) {
              if(data!=null){
                  var rows = data.rows;
                  var result = [];
                  var num = 5;
                  if(rows.length > num){              //取前4条数据
                      for(var i = 0; i < num; i++) {
                          result.push(rows[i]);
                      }
                  }else{
                      result = rows;
                  }
                  _that.tableData3 = result;
                  var total = data.total;
                  var val = total > 4?'':'none';
                  $('#showMoreYiban').css("display",val);
              }
          }, function (msg) {
              alertMsg('', '服务请求失败', '关闭', 'error', true);
          });
      },
      // // 标准项目类型办件数
      // getStandardTheme:function () {
      //     var _that = this;
      //     request('', {
      //         url: ctx+'/portal/total/item/standard_theme',
      //         type: 'get',
      //     }, function (res) {
      //         console.log(res);
      //        _that.totalItem = res.content
      //        mybjs();
      //     }, function (msg) {
      //
      //     });
      // },
      // // 标准标准阶段用时
      // getusetime:function () {
      //     var _that = this;
      //     request('', {
      //         url: ctx+'portal/total/use_time/standard_stage',
      //         type: 'get',
      //     }, function (res) {
      //         console.log(res);
      //         _that.useTimeData = res.content
      //         xmlxbjs();
      //     }, function (msg) {
      //
      //     });
      // },
      showMyNotice: function () {
            var url = ctx + '/rest/aoa/notice/content/viewNotice?businessFlag=1';

          try {

              var tabsData = {
                  menuName: '我的公告',
                  id:'gonggao',
                  menuInnerUrl: url,
                  menuSeq:'.13a3cab5-8120-4031-9b4d-a8ffa39dc9fa.131ae63c-58b8-4de9-aacf-6e927c17f919.'
              }
              console.log(tabsData.menuInnerUrl)
              try{
                  //获取父iframe vue对象
                  var parentVue = window.parent.vm
                  parentVue.addTab('',tabsData,'MENU_',parentVue.leftMenuActive++);
              }
              catch(err){
                  top.postMessage(tabsData, '*')
              }

          }catch (e) {
              window.open(url,'_blank');
          }
      },
      showMoreDaiban: function () {
          var _that = this;
           var url = ctx + '/bpm/front/view/index.html?viewCode='+_that.buttonParams.dai_ban.viewCode;

          try {

              var tabsData = {
                  menuName: '待办视图',
                  id: _that.buttonParams.dai_ban.viewCode,
                  menuInnerUrl: url,
                  menuSeq:'.13a3cab5-8120-4031-9b4d-a8ffa39dc9fa.131ae63c-58b8-4de9-aacf-6e927c17f919.'
              }
              console.log(tabsData.menuInnerUrl)
              try{
                  //获取父iframe vue对象
                  var parentVue = window.parent.vm
                  parentVue.addTab('',tabsData,'MENU_'+_that.buttonParams.dai_ban.viewCode,parentVue.leftMenuActive++);
              }
              catch(err){
                  top.postMessage(tabsData, '*')
              }

          }catch (e) {
              window.open(url,'_blank');
          }

      },
      showMoreYiban: function () {
          var _that = this;
          var url = ctx + '/bpm/front/view/index.html?viewCode='+_that.buttonParams.yi_ban.viewCode;

          var tabsData = {
              menuName: '已办视图',
              id: _that.buttonParams.yi_ban.viewCode,
              menuInnerUrl: url,
              menuSeq:'.13a3cab5-8120-4031-9b4d-a8ffa39dc9fa.131ae63c-58b8-4de9-aacf-6e927c17f919.'
          }
          console.log(tabsData.menuInnerUrl)
          try {

              try{
                  //获取父iframe vue对象
                  var parentVue = window.parent.vm
                  parentVue.addTab('',tabsData,'MENU_'+_that.buttonParams.yi_ban.viewCode,parentVue.leftMenuActive++);
              }
              catch(err){
                  top.postMessage(tabsData, '*')
              }

          }catch(e)
          {
              window.open(url,'_blank');
          }

      },
      colligateQuery: function () {
          var url = ctx + 'bpm/front/view/index.html?viewCode='+ this.keyword;

          try{

              var tabsData = {
                  menuName: '综合查询',
                  id: 'chaxun',
                  menuInnerUrl: url,
                  menuSeq:'.13a3cab5-8120-4031-9b4d-a8ffa39dc9fa.131ae63c-58b8-4de9-aacf-6e927c17f919.'
              }
              console.log(tabsData.menuInnerUrl)
              try{
                  //获取父iframe vue对象
                  var parentVue = window.parent.vm
                  parentVue.addTab('',tabsData,'MENU_'+this.keyword,parentVue.leftMenuActive++);
              }
              catch(err){
                  top.postMessage(tabsData, '*')
              }

          }catch (e) {
              window.open(url,'_blank');
          }

      },
      // 综合查询请求接口 (项目审批进度)
      query:function(){
          var _that = this;
          var props = {
              viewId:_that.buttonParams.shen_bao_cha_xun.viewId
          };
          request('', {
              url: ctx+'/bpm/front/view/getBootstrapPageViewData',
              type: 'get',
              data: props
          }, function (data) {
              if(data!=null){
                  var rows = data.rows;
                  var result = [];
                  var num = 5;
                  if(rows.length > num){              //取前4条数据
                      for(var i = 0; i < num; i++) {
                          result.push(rows[i]);
                      }
                  }else{
                      result = rows;
                  }
                  _that.tableData4 = result;
              }
          }, function (msg) {

          });
      },
      statisticalAnalysis: function () {
          this.$message('暂时没开放此功能');
          // var url = 'http://192.168.30.31:8000/xmjg/xmjg/xmjg-project-info!getCsrk.action';
          // window.open(url,'_blank');
      },
      parallelApprove: function () {
          var url = ctx + 'apanmis/page/stageApplyIndex';

          var tabsData = {
              menuName: '并联申报',
              id:'c1052dcd-6a5e-4634-b1c3-3a40c303a0f7',
              menuInnerUrl: url,
              menuSeq:'.28187ca8-10ae-4abf-92c0-74718eabe879.c1052dcd-6a5e-4634-b1c3-3a40c303a0f7.'
          }
          console.log(tabsData.menuInnerUrl)
          try {

              try{
                  //获取父iframe vue对象
                  var parentVue = window.parent.vm
                  parentVue.addTab('',tabsData,'MENU_7a7d81b2-8287-4597-b468-11688b0ffc28',parentVue.leftMenuActive++);
              }
              catch(err){
                  top.postMessage(tabsData, '*')
              }

          }catch(e)
          {
              window.open(url,'_blank');
          }
      },
      getXmlxbj:function () {
         var _that = this;
         request('', {
             url: ctx+'rest/portal/total/item/standard_theme',
             type: 'get',
         }, function (data) {
             if(data.success&&data.content!=null){
                 var content = data.content;
                 xmlxbjs(content.legendData,content.xAxisData,content.seriesData);
             }
         }, function (msg) {
             alertMsg('', '服务请求失败', '关闭', 'error', true);
         });
     },
     getMybjs:function () {
         var _that = this;
         request('', {
             url: ctx+'rest/portal/total/use_time/standard_stage',
             type: 'get',
         }, function (data) {
             if(data.success&&data.content!=null){
                 var content = data.content;
                 mybjs(content.legendData,content.xAxisData,content.seriesData);
             }
         }, function (msg) {
             alertMsg('', '服务请求失败', '关闭', 'error', true);
         });
     },
      getCount: function (index) {
          var _that = this;
          var count = '';
          if(_that.buttonCounts.length > index && _that.buttonCounts[index].count>0){
              count = _that.buttonCounts[index].count;
          }
          return count;
      },

      //计算剩余的天数
      dateCount:function (taskId,limitTime){
          var restDays = 0;
          $.ajax({
              type: 'post',
              url: ctx + '/aea/business/getRestOfDay.do',
              data:{taskId:taskId,limitTime:limitTime},
              async:false,
              success: function (data) {
                  if (data.success) {
                      restDays = parseFloat(data.message);
                  }else{
                      console.log(result.message);
                  }
              }
          });
          var back = {};
          if(restDays < 0){
              back.timeType = 'danger';
              var tempDays = Math.abs(restDays);
              back.timeText = '逾期'+ tempDays +'天';
          }else if(restDays <= 2){

              back.timeType = 'warning';
              back.timeText = '剩余'+ restDays +'天';
          }else{

              back.timeType = 'safe';
              back.timeText = '剩余'+ restDays +'天';
          }
          return back;
        },
      formatDate: function (row, column) {

         var date = new Date(row[column.property])

          if (!date) return;
          return formatDate(date, 'yyyy-MM-dd');
      },
  },
  filters: {
      formatDate: function (time) {
          var date = new Date(time);
          if (!date) return;
          return formatDate(date, 'yyyy.MM.dd');
      },
  },
});
