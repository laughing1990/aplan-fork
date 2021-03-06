//  var ctx = "http://192.168.15.101:8084/aplanmis-mall/"
var vm = new Vue({
  el: "#declareHaveVue",
  data: {
    // 全局loading
    mloading: false,
    mloading2:false,
    noDataTip:"",
    projName: '',
    keyword: '',
    localCode: '',
    pageNumDH: 1,
    pageSizeDH: 10,
    totalDH: 0,
    hadApplyItemData: [],
    proType: 1,

    // 是否是为项目详情
    isProjDetail: false,
    // 项目详情数据
    projDetailData: {
      isSeriesApprove: '', // 0:并联   1：单项
      aeaProjInfo: {}, //项目基本信息
      aeaUnitInfo: {}, //申报主体信息
      aeaLinkmanInfo: {}, //人员信息（用于申报主体）
      aeaHiIteminstList: [], //事项列表（并联才有）
      stateList: [], //办事情形list
      matList: [], //材料列表
      aeaHiSmsInfo: {}, //取件方式
      isNeedState: '', //是否需要办事情形
    },
    // 当前操作已申报项目
    curHandelProjRow: {},


    // 基本信息
    // 是否展示基本信息所有数据
    isShowAll: false,

    // 事项列表
    // 所有的事项列表
    allMatterList: [],
    // 展示的事项列表
    showMatterList: [],
    // 事项列表是否展示所有数据
    isMatterShowAll: false,

    // 材料列表
    // 所有的事材料列表
    allMaterialList: [],
    // 展示的材料列表
    showMaterialList: [],
    // 材料列表是否展示所有数据
    isMaterialShowAll: false,
    // 是否展示电子件dialog
    isShowElectronDialog: false,
    // 是否展示证照dialog
    isShowLicenseDialog: false,
    //证照附件
    licenseFileList:[],
    // 所有电子件表格数据
    electronFileList: [],
    receiveList: [],//回执列表
    receiveModalShow: false,//回执弹窗控制
    curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
    curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
    receiveItemActive: '', // 回执列表li active状态
    receiveActive: '', // 回执列表 div active状态
    pdfSrc: '', // 回执预览地址
  },
  mounted: function () {
    this.getHadApplyItemList()
    this.isFormMyHomeIndex()
  },
  methods: {
    isFormMyHomeIndex:function(){
        var formMyHomeIndexData =JSON.parse(sessionStorage.getItem("formMyHomeIndexData"))|| {};
        if(formMyHomeIndexData && formMyHomeIndexData.originFlag){
          this.lookProjDetail(formMyHomeIndexData.item);
          sessionStorage.removeItem("formMyHomeIndexData");
        }
    },
    getHadApplyItemList: function () {
      var vm = this;
      vm.mloading = true;
      vm.noDataTip = ""
      request('', {
        url: ctx + 'rest/user/hadApplyItem/list',
        type: 'get',
        data: {
          state: vm.proType,
          localCode: vm.localCode,
          projName: vm.projName,
          keyword: vm.keyword,
          pageNum: vm.pageNumDH,
          pageSize: vm.pageSizeDH,
        }
      }, function (res) {
        vm.mloading = false;
        if (res.success) {
          vm.hadApplyItemData = res.content.list;
          if(vm.hadApplyItemData.length<=0){
            vm.noDataTip = "暂无数据"
          }
          vm.totalDe = res.content.total;
          vm.totalDH = res.content.total;
        } else {
          return vm.$message({
            message: '加载失败！',
            type: 'error'
          })
        }
      }, function () {
        vm.mloading = false;
        return vm.$message({
          message: '加载失败！',
          type: 'error'
        })
      });
    },

    //  切换项目类型tab-办理中：已办理
    loadProjByUnit: function (num) {
      this.proType = num;
      //  清掉上一次查询中的keyword
      this.localCode = '';
      this.projName = '';
      this.keyword = '';
      //  重置页数跟大小
      this.pageSizeDH = 10;
      this.pageNumDH = 1;
      this.getHadApplyItemList();
    },
    handleSizeChangeDe: function (val) {
      this.pageSizeDH = val;
      this.getHadApplyItemList()
    },
    handleCurrentChangeDe: function (val) {
      this.pageNumDH = val;
      this.getHadApplyItemList()
    },

    // 查看项目详情
    lookProjDetail: function (row) {
      var ts = this;
      this.isProjDetail = true;
      this.curHandelProjRow = row;
          // _url = ctx + 'rest/user/applydetail/' + row.applyinstId + '/' + row.localCode + '/' + row.isSeriesApprove;
          _url = ctx + 'rest/user/applydetail/' + row.applyinstId + '/' + row.projInfoId + '/' + row.isSeriesApprove;
      //  _url = ctx + 'rest/user/applydetail/83855950-48c3-467a-9e86-d05fdd93cda0/2016-130224-44-03-000063/1';
      ts.mloading2 = true;
      request('', {
        url: _url,
        type: 'get',
      }, function (res) {
        ts.mloading2 = false;
        if (res.success) {
          ts.projDetailData = res.content;
          //  事项列表-及初始化
          ts.allMatterList = ts.projDetailData.aeaHiIteminstList;
          ts.changeMatterListShow(ts.showMatterList, ts.allMatterList, ts.isMatterShowAll, 'matter');
          // 材料列表-及初始化
          ts.allMaterialList = ts.projDetailData.matList;
          // debugger
          ts.changeMatterListShow(ts.showMaterialList, ts.allMaterialList, ts.isMaterialShowAll, 'material');

        } else {
          return ts.$message({
            message: res.message,
            type: 'error'
          })
        }
      }, function () {
        ts.mloading2 = false;
        return ts.$message({
          message: '网络错误！',
          type: 'error'
        })
      });
    },
    //撤件
    withDraw:function(item){
        var _this=this;
        confirmMsg('','此操作将撤回申报,确定要撤回吗？',function(){
          request('',{
            type:'post',
            url:ctx+'/rest/user/withDraw/'+item.applyinstId,
            data:{
              applyInstId : item.applyinstId
            }
          },function (res) {
            _this.$message({
              message: '撤回成功',
              type: 'success'
            });
            _this.getHadApplyItemList();
          },function (err) {
            _this.$message.error('服务器错了哦!');
          })
        })
    },
    // 项目详情-事项列表-收起展开事项数据
    changeMatterListShow: function (showlist, alllist, identify, type) {
      //  console.log(showlist, alllist, identify)
      if (identify) {
        showlist = alllist;
      } else {
        if (alllist) {
          showlist = [];
          alllist.slice(0, 5).forEach(function (item) {
            showlist.push(item)
          })
        }
      }
      if (type === 'material') {
        this.showMaterialList = showlist;
      } else {
        this.showMatterList = showlist;
      }
    },
    // 项目详情-材料列表-已上传操作按钮
    showElectronDialog: function (row) {
      this.isShowElectronDialog = true;
      this.electronFileList = row.fileList; //附件列表赋值
    },
    showLicenseDialog: function (row) {
      this.isShowLicenseDialog = true;
      this.licenseFileList = row.certFileList; //附件列表赋值
    },
    getFileType: function (fileName) {
      var index1 = fileName.lastIndexOf(".");
      var index2 = fileName.length;
      var suffix = fileName.substring(index1 + 1, index2).toLowerCase();//后缀名
      return suffix;
    },
    // 预览电子件
    filePreview: function (data, flag) { // flag==pdf 查看施工图
      debugger
      var detailId = data.fileId;
      var _that = this;
      var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
      var fileName = data.fileName;
      var fileType = this.getFileType(fileName);
      var flashAttributes = '';
      _that.filePreviewCount++
      if (flag == 'pdf'||flag == 'PDF') {
        var tempwindow = window.open(); // 先打开页面
        setTimeout(function () {
          tempwindow.location = ctx + 'cod/drawing/drawingCheck?detailId=' + detailId;
        }, 1000)
      } else {
        if (fileType == 'pdf'||flag == 'PDF') {
          var tempwindow = window.open(); // 先打开页面
          setTimeout(function () {
            tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
          }, 1000)
        } else if (regText.test(fileType)) {
          // previewPdf/pdfIsCoverted
          _that.loading = true;
          request('', {
            url: ctx + 'previewPdf/pdfIsCoverted?detailId=' + detailId,
            type: 'get',
          }, function (result) {
            if (result.success) {
              _that.loading = false;
              var tempwindow = window.open(); // 先打开页面
              setTimeout(function () {
                tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
              }, 1000)
            } else {
              if (_that.filePreviewCount > 9) {
                confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                  _that.filePreviewCount = 0;
                  _that.filePreview(data);
                }, function () {
                  _that.filePreviewCount = 0;
                  _that.loading = false;
                  return false;
                }, '确定', '取消', 'warning', true)
              } else {
                setTimeout(function () {
                  _that.filePreview(data);
                }, 1000)
              }
            }
          }, function (msg) {
            _that.loading = false;
            _that.$message({
              message: '文件预览失败',
              type: 'error'
            });
          })
        } else {
          _that.loading = false;
          var tempwindow = window.open(); // 先打开页面
          setTimeout(function () {
            tempwindow.location = ctx + 'rest/file/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
          }, 1000)
        }
      }
    },
    // 预览证照
    licensePreview: function (row) {
      request('', {
        url: ctx + '/aea/cert/getViewLicenseURL?authCode=' + row.fileId,
        type: 'get',
      }, function (result) {
        if (result.success) {
          window.open(result.content);
        } else {
          _that.loading = false;
          _that.$message({
            message: '文件预览失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.loading = false;
        _that.$message({
          message: '文件预览失败',
          type: 'error'
        });
      })
    },
    // 下载电子件
    downloadFile: function (row) {
      var detailIds = row.fileId;
      window.open(ctx + 'rest/file/applydetail/mat/download/' + detailIds)
    }
    ,
    applyState:function (val) {
      if(val== '已受理') return "bg1";
      if(val=='已接件未审核') return "bg2";
      if(val=='不受理') return "bg3";
      if(val==4) return "bg4";
      if(val==5) return "bg5";
      if(val==6) return "bg6";
      if(val==7) return "bg7";
      if(val==8) return "bg8";
      if(val==9) return "bg9";
      if(val==10) return "bg10";
      if(val==11) return "bg11";
      if(val==12) return "bg12";
      if(val==13) return "bg13";
      if(val==14) return "bg14";
      if(val==15) return "bg15";
      return "bg1";
    },
    //查询回执列表
    showReceive: function (row) {debugger
      var _that = this;
      request('', {
        url: ctx + '/rest/user/receive/getStageOrSeriesReceiveByApplyinstIds',
        type: 'get',
        data: {'applyinstIds': row.applyinstId}
      }, function (res) {
        if (res.success) {
          //显示列表弹框
          if(res.content.length>0){
            _that.receiveList = res.content;
            _that.receiveList.map(function(item){
              Vue.set(item,'show',true);
            });
            _that.printReceive1(_that.receiveList[0].receiveList[0],0,0);
            _that.receiveModalShow = true;
          }else {
            _that.$message({
              message: '暂无回执',
              type: 'warning'
            });
          }
        }else {
          _that.$message({
            message: res.message?res.message:'获取回执列表失败',
            type: 'error'
          });
        }
      }, function (msg) {
        _that.$message({
          message: '获取回执列表失败',
          type: 'error'
        });
      });
    },
    //打印回执new
    printReceive1: function (row,index,ind) {
      this.receiveItemActive = index;
      this.receiveActive = ind;
      var fileUrl = ctx + '/rest/user/receive/toPDF/'+row.receiveId;
      this.pdfSrc=ctx + 'preview/pdfjs/web/viewer.html?file='+encodeURIComponent(fileUrl)
    },
    iteminstStateColorFormat:function(val){
      if(val==1) return "#457EFF";
      if(val==2) return "#4EB1FD";
      if(val==3) return "#00C161";
      if(val==4) return "#eee";
      if(val==5) return "#eee";
      if(val==6) return "#447EFF";
      if(val==7) return "#2BB49E";
      if(val==8) return "#447EFF";
      if(val==9) return "#447EFF";
      if(val==10) return "#2BB49E";
      if(val==11) return "#00C161";
      if(val==12) return "#00C161";
      if(val==13) return "#FF4B47";
      if(val==14) return "#FF4B47";
      if(val==15) return "#FF4B47";
      return "#000";
    }
  },
  filters: {
    projTypeFormat: function (val) {
      if (!val) return '暂无';
      return val == 'A00001' ? '审批类' : (val == 'A00002' ? '核准类' : '备案类');
    },
    timeFormat: function (val) {
      if (!val) return '暂无';
      // 获取年月日时分秒值  slice(-2)过滤掉大于10日期前面的0
      var datetime=new Date(val);
      var year = datetime.getFullYear(),
          month = ("0" + (datetime.getMonth() + 1)).slice(-2),
          date = ("0" + datetime.getDate()).slice(-2),
          hour = ("0" + datetime.getHours()).slice(-2),
          minute = ("0" + datetime.getMinutes()).slice(-2),
          second = ("0" + datetime.getSeconds()).slice(-2);
      // 拼接
      return year + "-"+ month +"-"+ date +" "+ hour +":"+ minute +":" + second;
    },
    iteminstStateFormat: function (val) {
      if(val==1) return "已接件";
      if(val==2) return "已撤件";
      if(val==3) return "已受理";
      if(val==4) return "不受理";
      if(val==5) return "不予受理";
      if(val==6) return "补正（开始）";
      if(val==7) return "补正（结束）";
      if(val==8) return "部门开始办理";
      if(val==9) return "特别程序（开始）";
      if(val==10) return "特别程序（结束）";
      if(val==11) return "办结（通过）";
      if(val==12) return "办结（容缺通过）";
      if(val==13) return "办结（不通过）";
      if(val==14) return "撤回";
      if(val==15) return "撤销";
      return "-";
    },
    changeReceiveType: function (value) {
      var defaultValue='其他回执';
      //{{(itemBtn.receiptType==2)?'收件回执':(itemBtn.receiptType==1)?'物料回执':(itemBtn.receiptType==3)?'不受理回执':'其他回执'}}
      if(value){
        switch(value) {
          case '1':
            defaultValue='物料回执';
            break;
          case '2':
            defaultValue='受理回执';
            break;
          case '3':
            defaultValue='不受理回执';
            break;
          case '4':
            defaultValue='退件回执';
            break;
          case '5':
            defaultValue='送达回证';
            break;
          case '6':
            defaultValue='材料补正回执';
            break;
          case '7':
            defaultValue='行政许可申请书';
            break;
          default:
            defaultValue='其他回执';
        }
      }
      return defaultValue;
    },
    computed:{

    }
  },
})