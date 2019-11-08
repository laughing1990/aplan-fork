var viewer;// viewer 对象
var vm = new Vue({
    el: '#app',
    data: {
      loading:false,
      fullHeight:document.documentElement.clientHeight,
      treeLeftHeight:document.documentElement.clientHeight-200,
      dialogEditTable:false,
      serviceDetail:{
        serviceName:'',
        feeReference:'',
        serviceContent:''
      },
      tableData2:[],
      tableData3:[],
      tableData4:[],
      pageSize2 :10,
      currentPage2 :1,
      page2 :1,
      total2 :0,
      pageSize3 :10,
      currentPage3 :1,
      page3 :1,
      total3 :0,
      pageSize4 :10,
      currentPage4 :1,
      page4 :1,
      total4 :0,
      certinstInfo:{
        termStart:'',
        termEnd:'',
        managementScope:''
      },
      tabData:true,
      organDetail:{},
      isScroll:false,
      activeName:1,
      serviceId:'',
      unitInfoId:'',
      unitServiceId:'',
      ctx:ctx
    },
    created: function () {
        this.getURLArgs();
        
        console.log(localStorage.getItem("access_token"));
    },
    mounted: function () {
      var that = this;
      window.addEventListener("resize", function() {
        return (function(){
          window.fullHeight = document.documentElement.clientHeight
          that.fullHeight = window.fullHeight;
          that.treeLeftHeight = that.fullHeight - 100;
        })();
      });
    },
    methods: {
      showServiceDetail:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'supermarket/agentservice/getUnitServiceDetail',type: 'get',
          data: {
             unitServiceId:this.unitServiceId
          }
        },function (data) {
          _this.serviceDetail = data.content;
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      showlistServiceItem:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'supermarket/agentservice/listServiceItem',type: 'get',
          data: {
            pageNum : this.currentPage2,
            pageSize : this.pageSize2,
            serviceId : JSON.stringify(this.serviceId.split(" "))
          }
        },function (data) {
          _this.tableData2 = data.content.rows;
          _this.total2 = data.content.total;
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      showlistAgentCertinst:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'supermarket/agentservice/listAgentCertinst',type: 'get',
          data: {
            unitInfoId:this.unitInfoId,
            unitServiceId:this.unitServiceId
          }
        },function (data) {
          _this.certinstInfo = data.content[0];
          _this.certinstId = data.content[0].certinstId;
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      //执业人员信息
      showlistAgentLinkmanCertinst:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'supermarket/agentservice/listAgentLinkmanCertinst',type: 'get',
          data: {
            pageNum : this.currentPage3,
            pageSize : this.pageSize3,
            unitInfoId:this.unitInfoId
          }
        },function (data) {
          _this.tableData3 = data.content.rows;
          _this.total3 = data.content.total;
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      //入驻机构详情
      showOrganDetail:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'supermarket/agentservice/getAgentUnitDetail',type: 'get',
          data: {
            unitInfoId:this.unitInfoId
          }
        },function (data) {
          _this.organDetail = data.content;
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      //参与项目
      showlistWinBidService:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'supermarket/agentservice/listWinBidService',type: 'get',
          data: {
            pageNum : this.currentPage4,
            pageSize : this.pageSize4,
            unitInfoId:this.unitInfoId
          }
        },function (data) {
          _this.tableData4 = data.content.biddingList.rows;
          _this.total4 = data.content.biddingList.total;
          if(_this.tableData4.length>0) {
            tabData=true;
          }else{
            tabData=false;
          }
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      // 格式化时间
      dateFormat: function (val){
        if(!val)return'暂无数据';
          var daterc = val;
          if(daterc!=null){
              var dateMat= new Date(parseInt(daterc));
              return formatDate(dateMat, 'yyyy.MM.dd hh:mm')
          }
      },
      // 跳转证照页面
      switchPageBook:function(){
        // this.showMajorTree();
        // window.open("../../../templates/zjcs/serviceOrgan/index3.html?id="+ id,"_blank");
        window.open("/aplanmis-mall/supermarket/main/qualCertInfo.html?certinstId="+ this.certinstId,"_blank");
      },
      itemClick:function(){
        // window.open("../../../templates/zjcs/serviceOrgan/index.html?unitInfoId="+ this.unitInfoId+'?tab=2',"_blank");

          window.location.href="/aplanmis-mall/supermarket/main/imUnits.html?unitInfoId="+ this.unitInfoId+'?tab=2'
        },
      itemClick2:function(item){
        window.open("/aplanmis-mall/supermarket/main/imServices.html?agentItemBasicId="+item,"_blank");
        // window.open("../../../templates/zjcs/serviceMatters/index.html?agentItemBasicId="+ item,"_blank");
      },
      getURLArgs:function() {
        //解析地址栏中的中文字符
          var search = decodeURI(window.location.search);
          // if(!search) return
          var params = {}
          search = search.substring(1, search.length);
          var group = search.split('?')
          for (var i = 0; i < group.length; i++) {
              var entry = group[i].split('=')
              params[entry[0]] = entry[1]
          }
          this.serviceId = params.serviceId;
          this.unitInfoId = params.unitInfoId;
          this.unitServiceId = params.unitServiceId;

          this.showOrganDetail();
          this.showServiceDetail();
          this.showlistServiceItem();
          this.showlistAgentCertinst();
          this.showlistAgentLinkmanCertinst();
          this.showlistWinBidService();
      },
      previewRow:function () {
          // this.preViewFlag = true;
          if(viewer)viewer.destroy();//当存在viewer对象，先销毁
          viewer = new Viewer(document.getElementById("book"), {
              url: 'imgUrl'
          });
          viewer.show();//展示图片
      },
      handleSizeChange2:function(val) {
        this.pageSize2 = val;
      },
      handleCurrentChange2:function(val) {
        this.currentPage2 = val;
      },
      handleSizeChange3:function(val) {
        this.pageSize3 = val;
        this.showlistAgentLinkmanCertinst();
      },
      handleCurrentChange3:function(val) {
        this.currentPage3 = val;
        this.showlistAgentLinkmanCertinst();
      },
      handleSizeChange4:function(val) {
        this.pageSize4 = val;
        this.showlistWinBidService();
      },
      handleCurrentChange4:function(val) {
        this.currentPage4 = val;
        this.showlistWinBidService();
      },

    },
    computed:{
      headMsg:function(){
        return {
          "Authorization":"bearer "+localStorage.getItem("access_token")
        }
      }
    },
    watch:{
      filterText1:function(val){

      }
    }
});
