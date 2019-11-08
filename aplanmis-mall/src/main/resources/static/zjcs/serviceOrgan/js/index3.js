var viewer;// viewer 对象
var vm = new Vue({
    el: '#app',
    data: {
      loading:false,
      imgUrlCss:'../../../static/zjcs/serviceOrgan/img/book.jpg',
      treeData:[],
      fullHeight:document.documentElement.clientHeight,
      treeLeftHeight:document.documentElement.clientHeight-200,
      dialogEditTable:false,
      treeTab:'tabs-first',
      defaultProps: {
        label: 'majorName'
      },
      dataInfo:[],
      isScroll:false,
      activeName:1,
      serviceId:'',
      unitInfoId:'',
      unitServiceId:'',
      treeLabel:''
    },
    created: function () {
        this.getURLArgs();
        // this.showMajorTree();
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
      getURLArgs:function() {
        //解析地址栏中的中文字符
          var search = decodeURI(window.location.search);
          if(!search) return
          var params = {}
          search = search.substring(1, search.length);
          var group = search.split('?')
          for (var i = 0; i < group.length; i++) {
              var entry = group[i].split('=')
              params[entry[0]] = entry[1]
          }
          this.certinstId = params.certinstId;
          this.showMajorTree();
      },
      //证书页面，查询证照资质证书及对应的专业树
      showMajorTree:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'supermarket/agentservice/getCertinstAndMajorTree',type: 'get',
          data: {
            certinstId :this.certinstId
          }
        },function (data) {
          _this.treeLabel = data.content.qualLevelName;
          _this.dataInfo = data.content;
          _this.treeData = data.content.majorTree;
          if(_this.dataInfo.bscAttFileAndDirs.length==0) return;
          _this.getImgFun(_this.dataInfo.bscAttFileAndDirs[0].fileId);
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      getImgFun:function(id){
        var _this =this;
        // window.open(ctx+"supermarket/main/getFileStream.do?detailId="+id);
        // 获取图片
        request('',{url:ctx + 'supermarket/main/getFileStream.do',type: 'post',
          data: {
            detailId :id
          }
        },function (data) {
          _this.imgUrlCss = data.fullBase64;
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      previewRow:function () {
          // this.preViewFlag = true;
          if(viewer)viewer.destroy();//当存在viewer对象，先销毁
          viewer = new Viewer(document.getElementById("book"), {
              url: 'imgUrl'
          });
          viewer.show();//展示图片
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


