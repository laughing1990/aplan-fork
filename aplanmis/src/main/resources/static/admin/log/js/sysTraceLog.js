var vm = new Vue({
  el: '#index',
  data: {
      formLabelWidth: '30%',
      pageSize: 10,
      page: 1,
      loading: true,
      searchKey: null,
      startTime: null,
      endTime: null,
      isOnlyShowUser: false,
      selRowData: {
         logId: '',
         logTitle: '',
         logTime: '',
         logLevel: '',
         logThread: '',
         logMessage: '',
         logException: '',
         userId: '',
         userName: '',
         posId: '',
         posName: '',
         orgId: '',
         orgName: '',
         operationApp: '',
         operationModule: '',
         operationFunc: '',
         operationClass: '',
         operationMethod: '',
         operationDesc: '',
         remoteAddr: '',
         userAgent: '',
         requestPath: '',
         requestParams: '',
         requestMethod: '',
         operationResult: '',
         operationDuration: ''
      },
      dialogEditTable: false,
      tableData: {},
      pickerOptions: {
          shortcuts: [{
              text: '今天',
              onClick: function(picker) {
                  picker.$emit('pick', new Date());
              }
          }, {
              text: '昨天',
              onClick: function(picker) {
                  const date = new Date();
                  date.setTime(date.getTime() - 3600 * 1000 * 24);
                  picker.$emit('pick', date);
              }
          }, {
              text: '一周前',
              onClick: function(picker) {
                  const date = new Date();
                  date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                  picker.$emit('pick', date);
              }
          }]
      },
  },
  mounted: function () {
      this.getDataTable();
  },
  methods:{
      getDataTable: function(props){
          var _that = this;
          var prop = {
              'pageNum': this.page ? this.page : 1,
              'pageSize': this.pageSize? this.pageSize : 10,
              'keyword': this.searchKey,
              'startTime': this.startTime,
              'endTime': this.endTime,
              'isOnlyShowUser': this.isOnlyShowUser
          };
          if(!props) {
              props = prop;
          }
          request('aplanmis',{
              url: ctx + 'aea/trace/log/listByPage.do',
              type: 'get',
              data: props
          },function (data) {

            _that.tableData = data;
            _that.loading = false;

          }, function(msg){

            alertMsg('','加载失败','关闭','error',true);
            _that.loading = false;
          });
      },
      editRow: function(rowData){

          var _that = this;
          this.dialogEditTable = true;
          $(".el-form").animate({scrollTop: 0}, 800);//滚动到顶部
          if(rowData){
              for (var key in this.selRowData) {
                  this.selRowData[key] = rowData[key];
              }
              this.selRowData.operationDuration = this.selRowData.operationDuration?this.selRowData.operationDuration+'毫秒':''
          }else {
              this.selRowData = {
                  logId: '',
                  logTitle: '',
                  logTime: '',
                  logLevel: '',
                  logThread: '',
                  logMessage: '',
                  logException: '',
                  userId: '',
                  userName: '',
                  posId: '',
                  posName: '',
                  orgId: '',
                  orgName: '',
                  operationApp: '',
                  operationModule: '',
                  operationFunc: '',
                  operationClass: '',
                  operationMethod: '',
                  operationDesc: '',
                  remoteAddr: '',
                  userAgent: '',
                  requestPath: '',
                  requestParams: '',
                  requestMethod: '',
                  operationResult: '',
                  operationDuration: ''
              }
          }
      },
      handleSizeChange: function(value) {

          this.pageSize = value;
          this.getDataTable();
      },
      handleCurrentChange: function(value) {

          this.page = value;
          this.getDataTable();
      },
      searchRow: function(){

          var propSearch = {
              'pageNum': this.page ? this.page : 1,
              'pageSize': this.pageSize? this.pageSize : 10,
              'keyword': this.searchKey,
              'startTime': this.startTime,
              'endTime': this.endTime,
              'isOnlyShowUser': this.isOnlyShowUser
          };
          this.getDataTable(propSearch)
      },
      dbclickRow: function(row){

          this.editRow(row);
      },
      clearFormData: function(){

          this.$refs['selRowData'].resetFields();
      },
      dateFormatByExp: function(date, fmt){

          var o = {
              "M+" : date.getMonth()+1,                 //月份
              "d+" : date.getDate(),                    //日
              "h+" : date.getHours(),                   //小时
              "m+" : date.getMinutes(),                 //分
              "s+" : date.getSeconds(),                 //秒
              "q+" : Math.floor((date.getMonth()+3)/3), //季度
              "S"  : date.getMilliseconds()             //毫秒
          };
          if(/(y+)/.test(fmt)) {
              fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
          }
          for(var k in o) {
              if(new RegExp("("+ k +")").test(fmt)){
                  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
              }
          }
          return fmt;
      },
      //直接调用公共JS里面的时间类处理的办法
      formatOperTime: function(value, exp) {

          return this.dateFormatByExp(new Date(value), exp);
      },
      isNullOrBank: function(value){

          if(typeof value == "undefined" || value == null || value == ""){
              return true;
          }else{
              return false;
          }
      }
  }
});