var vm = new Vue({
    el: '#app',
    data: {
      fullHeight:document.documentElement.clientHeight,
      treeLeftHeight:document.documentElement.clientHeight-200,
      dialogEditTable:false,
      pageSize:10,
      currentPage:1,
      page:1,
      pagination:true,
      multipleSelection:[],
      total:0,
      silderTitle:'新增',
      formData:[],
      rules:{},
      loading:false,
      navItemObj:["中介服务事项","详情"],
      keyword:'',
      tab:true,
      isUp:false,
      title:'',
      dialogTitle:'',
      tableData:[],
      tableData2:[{
        ruleName:'全国绿化评比表彰实施办法',
        ruleNum:'全绿字[2009]9号',
        organ:'全国绿化委员会',
        clauseNum:'第十二条',
        clauseContent:'以上各项指标必须经有甲级资质的林业调查规划设计单位鉴定，方予认可。'
      }],
      tableData3:[{
        itemName:'对在义务植树和绿化工作中成绩显著的单位和个人进行表彰、奖励',
        itemType:'行政奖励',
        connect:'行政相对人必须委托中介机构实施',
        effectDepart:'广东省林业厅'
      }],
      fileList:[],
      option1:[],
      option:[],
      detail:{},
      items:['广东省监狱管理局', 
              '广东省人力资源和社会保障厅', 
              '广东省发展和改革委员会', 
              '广东省工业和信息化厅', 
              '广东省教育厅', 
              '广东省科学技术厅', 
              '广东省民族宗教事务委员会', 
              '广东省公安厅', 
              '广东省民政厅', 
              '广东省司法厅', 
              '广东省财政厅', 
              '广东省国土资源厅', 
              '广东省环境保护厅', 
              '广东省住房和城乡建设厅', 
              '广东省交通运输厅', 
              '广东省水利厅', 
              '广东省农业厅', 
              '广东省林业厅', 
              '广东省海洋与渔业厅', 
              '广东省商务厅', 
              '广东省文化厅', 
              '广东省卫生和计划生育委员会', 
              '广东省质量技术监督局', 
              '广东省新闻出版广电局', 
              '广东省食品药品监督管理局', 
              '广东省地方金融监督管理局', 
              '广东省人民防空办公室', 
              '广东省国家保密局', 
              '广东省气象局', 
              '广东省粮食局', 
              '广东省戒毒管理局'],
      form:{},
      isScroll:false,
      activeName:2,
      getItemBasicId: '',
      checkboxs:[],
      checkboxGroup:[],
      itemBasicId:'',
      agentOrgId:''
    },
    created: function () {
        // this.showTable();
        this.getDepart();
        this.getAllService();
        this.getURLArgs();
        // this.getReference();
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
      var url = document.location.toString();
      this.getItemBasicId = url.split("?itemBasicId=")[1];
      if(this.getItemBasicId){
        this.getDetail(this.getItemBasicId);
        this.tab =false;
      }
    },
    methods: {
      // 返回列表
      returnServiceList: function(){
        this.tab=true;

        // this.showTable();
      },
      // 获取表格数据
      showTable:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'supermarket/agentservice/listServiceItem',type: 'get',
          data: {
            pageNum : this.currentPage,
            pageSize : this.pageSize,
            agentItemName : this.form.agentItemName,
            itemName : this.form.itemName,
            serviceId : this.checkboxGroup ? JSON.stringify(this.checkboxGroup) : JSON.stringify([]),
            agentOrgId : this.agentOrgId
          }
        },function (data) {
          _this.tableData = data.content.rows;
          _this.total =data.content.total;
          if(data.content.total>10){
            _this.pagination=true;
          }else{
            _this.pagination=false;
          }
          _this.loading=false;
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      // 获取表格数据
      getDepart:function(){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'supermarket/agentservice/listServiceItemOrg',type: 'get',
          data: {}
        },function (data) {
          _this.items = data.content.rows;
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      // 获取详细数据
      getDetail:function(id){
        var _this = this;
        // this.loading=true;
        request('',{url:ctx + 'supermarket/agentservice/serviceItemDetail',type: 'get',
          data: {
            itemBasicId : id
          }
        },function (data) {
          _this.detail = data.content;
          _this.tableData3 = data.content.aeaItemBasicList;
          _this.tableData2 = data.content.aeaItemServiceBasicList;
        }, function(msg){
           _this.$message({
            message: '加载失败',
            type: 'error'
          });
          _this.loading=false;
        });
      },
      // 服务类型
      getAllService: function () {
        var _that = this;
        request('', {
          url: ctx + 'supermarket/purchase/getAllService',
          type: 'post',
        }, function (data) {
          if(data.content){
            _that.checkboxs = data.content;
          }
        }, function (msg) {
          alertMsg('', '服务请求失败', '关闭', 'error', true);
        });
      },
      // 格式化时间
      dateFormat: function (val){
          var daterc = val;
          if(daterc!=null){
              var dateMat= new Date(parseInt(daterc));
              return formatDate(dateMat, 'yyyy.MM.dd')
          }
      },
      getURLArgs:function() {
        //解析地址栏中的中文字符
          var ts = this;
          var search = decodeURI(window.location.search);
          if(!search) return ts.showTable();
          var params = {}
          search = search.substring(1, search.length);
          var group = search.split('?')
          for (var i = 0; i < group.length; i++) {
              var entry = group[i].split('=')
              params[entry[0]] = entry[1]
          }
          if(params.agentItemBasicId){
            this.tab=false;
            this.getDetail(params.agentItemBasicId);
          }
          if(params.seachKey){
            // this.form.agentItemName = params.seachKey;
            this.$set(ts.form, 'agentItemName',params.seachKey);
            this.showTable();
          }
          this.showTable();
      },
      // 部门选择事件
      chooseDm:function(e,item){
        var _this = this;
        if($(e.target).hasClass('active')){
          $(e.target).removeClass('active');
          this.agentOrgId = '';
        }else{
          $(".department li").removeClass('active');
          this.agentOrgId = item.agentOrgId;
          $(e.target).addClass('active');
        }
        this.showTable();
      },
      cellClick:function(row, column, cell, event){
        if(column.label == "中介服务事项名称"){
          this.title=row.name;
        }
      },
      toggle:function(e){
        // e.cancelBubble=true;
        // var item = $(".slide-control");
        // if(item.hasClass('control-up')){
        //   this.isUp=false;
        //   // $(".checkgroup").addClass('special');
        //   item.removeClass('control-up').addClass('control-down');
        // }else{
        //   this.isUp=true;
        //   // $(".checkgroup").removeClass('special');
        //   item.removeClass('control-down').addClass('control-up');
        // }
        this.isUp = !this.isUp;
      },
      // 解决sleect选不中值问题
      forceUpdate:function(){
        this.$forceUpdate()
      },
      breadNavClick:function(index){
        if(index==0){
          this.tab = true;
        }
      },
      
      onChange:function(file,fileList){
        this.fileList=fileList
      },
     
      handleSizeChange:function(val) {
        this.pageSize = val;
        this.showTable();
      },
      handleCurrentChange:function(val) {
        this.currentPage = val;
        this.showTable();
      },
      handleSelectionChange:function(val) {
        this.multipleSelection = val;
      },
    },
    computed:{
      headMsg:function(){
        return {
          "Authorization":"bearer "+localStorage.getItem("access_token")
        }
      }
    }
});


