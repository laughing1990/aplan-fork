var vm = new Vue({
  el: '#index',
  data: {
    curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
    ctx: ctx,
    selStatus: '', // 选中的卡片name
    selStatusIndex: 0, // 选中的卡片索引
    statusCardData: [
      {
        name: '预警办件',
        count: 0,
        imgSrc: '/aplanmis/front/effect/images/ico1_yjbj.png',
        statusClass: 'waring-status'
      },
      {
        name: '逾期办件',
        count: 0,
        imgSrc: '/aplanmis/front/effect/images/ico2_yqbj.png',
        statusClass: 'error-status'
      },
      {
        name: '网上待预审',
        count: 0,
        imgSrc: '/aplanmis/front/effect/images/ico3_wsdys.png',
        statusClass: 'waiting1-status'
      },
      {
        name: '申报待补全',
        count: 0,
        imgSrc: '/aplanmis/front/effect/images/ico4_cldbq.png',
        statusClass: 'waiting2-status'
      },
      {
        name: '办件待补正',
        count: 0,
        imgSrc: '/aplanmis/front/effect/images/ico5_dgcbz.png',
        statusClass: 'waiting1-status'
      },
      {
        name: '进入特殊程序',
        count: 0,
        imgSrc: '/aplanmis/front/effect/images/ico6_jrtxcx.png',
        statusClass: 'special-status'
      },
      {
        name: '办理不通过',
        count: 0,
        imgSrc: '/aplanmis/front/effect/images/ico7_blbtg.png',
        statusClass: 'error-status'
      }
    ],
    statusCardName:[
      'yuJingBanJian','yuQiBanJian','wangShangDaiYuShen','caiLiaoBuQuan','daiGuoChengBuZheng','jinRuTeShuChengXu','banLiBuTongGuo'
    ],
    listTableData:{},
    pageSize: 10,
    page: 1,
    keyword:null,
    handleTitle:'',
    tableLoading:false,
    handleDialog:false,//督办弹窗
    tableData:[],//督办弹窗人员列表
    recievePeron:[],//接受人数据
    copySendPeron:[],//抄送人数据
    textarea:'',//督办内容
    type:'n',//督办方式
    level:'u',//督办等级
    recieveSerch:'',
    loading:false,
    copySendSerch:'',
    processInstanceId:'',
    multipleSelection:[],
    currentProjName:"",
    currentAppName:"",
  },
  beforeMount: function (){
    this.selStatus = this.statusCardData[0].name;
    this.getDataTable(0);

    this.getDataCount(1);

    this.getDataCount(2);

    this.getDataCount(3);

    this.getDataCount(4);
  },
  mounted: function () {
    window.addEventListener("resize", function() {
      vm.curHeight = document.documentElement.clientHeight;
    });
  },
  methods:{
    changeTableData: function(data,index){
      this.selStatus = data.name;
      this.selStatusIndex = index;
      this.pageSize = 10;
      this.page = 1;
      this.getDataTable(index);
    },
    handleSizeChange: function(val) {
      this.pageSize = val;
      this.getDataTable(this.selStatusIndex);
    },
    handleCurrentChange: function(val) {
      this.page = val;
      this.getDataTable(this.selStatusIndex);
    },
    getDataTable: function(typeIndex,props){
      var _that = this;
      _that.tableLoading = true;

      var prop = {
        pageNum: _that.page ? this.page : 1,
        pageSize: _that.pageSize
      };
      if(!props) props = prop;
      var url = ctx+'/effect/inspection/'+_that.statusCardName[typeIndex]+'/page';
      if(_that.keyword!=null&&$.trim(_that.keyword)!=''){
        url = ctx+'/effect/inspection/'+_that.statusCardName[typeIndex]+'/page?keyword='+_that.keyword;
      }

      request('',{url:url,type: 'get',data:props},function (data) {
        if(data.success){
          _that.listTableData = data.content;
          _that.statusCardData[typeIndex].count = _that.listTableData.total;
        }else{
          alertMsg('','列表数据加载失败','关闭','error',true);
        }
        _that.tableLoading = false;
      }, function(msg){
        alertMsg('','列表数据加载失败','关闭','error',true);
        _that.tableLoading = false;
      });
    },
    getDataCount: function(typeIndex){
      var _that = this;
      request('',{url:ctx+'/effect/inspection/'+_that.statusCardName[typeIndex]+'/count',type: 'get'},function (data) {
        if(data.success){
          _that.statusCardData[typeIndex].count = data.content;
          _that.tableLoading = false;
        }
      }, function(msg){
        alertMsg('','列表数据加载失败','关闭','error',true);
        _that.tableLoading = false;
      });
    },
    searchTableData:function(){
      this.page=1;
      this.getDataTable(this.selStatusIndex);
    },
    //督办弹窗
    handleRemind:function(index,row){
      this.processInstanceId = row.procInstId;
      this.currentProjName = row.projName;
      this.handleDialog =true;
      this.getpersonData();
      this.getAppName();
      this.intHandleDialog();
      this.$nextTick(function(){
        $(".person .el-select  .el-input .el-input__inner").removeAttr("placeholder");
      })
    },
    filter:function(val){
      var _this = this;
      if(!val){
        _this.focus();
        return
      }
      _this.recieveSerch = _this.tableData;

      $(".person1 .personSelect").removeClass('personSelect2');
      var currentData = [];
      for(var i=0; i<_this.recieveSerch.length;i++){
        if(_this.recieveSerch[i].userName.indexOf(val)!=-1){
          currentData.push(_this.recieveSerch[i])
        }
      }
      this.$nextTick(function(){
        $(".el-select-dropdown__item.hover").hide();
      })

      if(currentData.length==0){
        this.$nextTick(function(){
          $(".el-select-dropdown__item.hover").show().css({"pointer-events":"none","cursor":'default !important'}).children('span').text("无匹配人员");
        })
      }
      _this.recieveSerch = currentData;

    },
    filter2:function(val){
      var _this = this;
      if(!val){
        _this.focus2();
        return
      }
      this.loading=true;
      this.searchPerson(val);
    },
    focus:function(){
      $(".person1 .personSelect").addClass('personSelect2')
    },
    // 删除人员
    removeTag:function(item){
      var _this = this;
      var removeObj = {}
      for (var i = 0;i<this.currentData.length;i++){
        var obj = this.currentData[i];
        if (obj.userId == item){
          removeObj = obj;
          this.currentData.splice(i,1);
          i--
        }
      }
      this.multipleSelection = this.currentData;
      this.$refs.table.toggleRowSelection(removeObj,false);
      if(!this.multipleSelection[0]){
        this.$refs.table.clearSelection();
        return
      }
    },
    focus2:function(){
      $(".person2 .personSelect").addClass('personSelect2')
    },
    // 发送数据
    sendData:function(){
      var _this = this;

      if(this.recievePeron.length==0){
        _this.$message({
          message:"请选择接收人",
          type: 'error'
        });
        return
      }
      if(this.textarea.length==0){
        _this.$message({
          message:"请填写发送内容",
          type: 'error'
        });
        return
      }
      confirmMsg('','确定发送吗？',function(){
        for(var i=0; i<_this.recievePeron.length; i++){
          if((_this.recievePeron.length-1)==i){
            _this.last=true;
          }
          var userIdJson = [];

          var s = {
            remindReceiverType:"s",
            receiverUserId:_this.recievePeron[i]
          }
          userIdJson.push(s);
          if(_this.copySendPeron.length!=0){
            for(var j=0; j<_this.copySendPeron.length; j++){
              var c = {
                remindReceiverType:"c",
                receiverUserId:_this.copySendPeron[j]
              }
            }
            userIdJson.push(c);
          }
          // 查找所选userId对应的taskId
          for(var j=0; j < _this.multipleSelection.length; j++){
            if(_this.recievePeron[i] == _this.multipleSelection[j].userId){
              var taskId = _this.multipleSelection[j].taskId;
            }
          }
          var param = JSON.stringify({
            procInstId:_this.processInstanceId,
            remindContent:_this.textarea,
            remindLevel:_this.level,
            remindMode:"p",
            remindType:_this.type,
            taskInstId:taskId,
            userIdJson:JSON.stringify(userIdJson)
          })
          request('',{url:ctx + 'bpm/front/remind/remind/msg/save?projName='+_this.currentProjName+"&appName="+_this.currentAppName,type: 'post',ContentType:'application/json',
            data:param
          },function (data) {
            if(data.success){
              if(_this.last){
                _this.handleDialog =false;
                _this.$message({
                  message:"督办信息发送成功",
                  type: 'success'
                });
                _this.last=false;
              }
            }
          }, function(msg){
            alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
          });
        }
      })
    },
    // 初始化
    intHandleDialog:function(){
      this.recievePeron=[];
      this.copySendPeron=[];
      this.textarea="";
      this.type="n";
      this.level="u";
      this.currentData=[];
      this.recieveSerch='';
      this.copySendSerch='';
    },
    handleSelectionChange:function(val) {
      this.multipleSelection = val;
      // this.recieveSerch = this.multipleSelection;
      this.recievePeron = [];
      this.currentData = this.multipleSelection;
      this.multipleSelection.map((item)=> {
          this.recievePeron.push(item.userId)
      })
    },
    handleSelectionChange2:function(row, column, event){
      for(var i=0; i<this.multipleSelection.length; i++){
          if(this.multipleSelection[i]==row){
              this.$refs.table.toggleRowSelection(row,false);
              // this.multipleSelection.splice(i,1);
              this.recievePeron = [];
              this.currentData = this.multipleSelection;
              this.multipleSelection.map((item)=> {
                  this.recievePeron.push(item.userId);
          })
              return;
          }
      }
      this.multipleSelection.push(row);
      this.$refs.table.toggleRowSelection(row,true);

      // this.multipleSelection.push(row);
    },
    //获取在办人员
    getpersonData:function(){
      var _this = this;
      request('',{url:ctx + 'bpm/front/remind/process/runtime/users',type: 'get',
        data:{
          procInstId : _this.processInstanceId
        }
      },function (data) {
        _this.tableData = data.content;
        _this.recieveSerch = data.content;
      }, function(msg){
        alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
      });
    },
    // 搜索用户
    searchPerson:function(val){
      var _this = this;
      request('',{url:ctx + 'bpm/front/remind/users/'+val,type: 'get',
        data:{}
      },function (data) {
        $(".person2 .personSelect").removeClass('personSelect2')
        $(".el-select-dropdown__item.hover").hide();
        if(data.content.length==0){
          $(".el-select-dropdown__item.hover").show().css({"pointer-events":"none","cursor":'default !important'}).children('span').text("无匹配人员");
        }
        _this.copySendSerch = data.content;
        _this.loading=false;
      }, function(msg){
        alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
      });
    },
    // 获取名称
    getAppName:function(){
      var _this = this;
      request('',{url:ctx + 'bpm/front/remind/tpl/app/name',type: 'get',
        data:{
          procInstId : _this.processInstanceId
        }
      },function (data) {
        _this.handleTitle = data.message+"--工作督办";
        _this.currentAppName = data.message;
      }, function(msg){
        alertMsg('',msg.message?msg.message:'服务器异常','关闭','error',true);
      });
    },
  }
});
// });
