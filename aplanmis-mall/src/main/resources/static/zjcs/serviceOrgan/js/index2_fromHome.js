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
      serviceTable: [],
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
      serviceName:'',
      imgUrl:'',
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
      getServiceDetail:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'aea/supermarket/serviceMatter/getServiceMatterNoPage',type: 'get',
          data: {
              serviceId: this.serviceId
          }
        },function (data) {
            if(data.success) {
                _this.serviceTable = data.content;
            }else{
                _this.$message({
                    message: '加载失败',
                    type: 'error'
                });
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
          this.serviceName = params.serviceName;
          this.imgUrl = params.imgUrl;
          this.getServiceDetail();
      }
    },
    computed:{
    },
    watch:{
    }
});
