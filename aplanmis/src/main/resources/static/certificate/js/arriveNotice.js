/*
 * @Author:
 * @Date:
 * @Last Modified by:
 * @Last Modified time: $ $
 */
var vm = new Vue({
  el: '#approve',
  data: function () {
    return {
      activeTab: 0,
      activeNames: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'], // el-collapse 默认展开列表
      verticalTabData: [ // 左侧纵向导航数据
        {
          label: '基本信息',
          target: 'baseInfo'
        }, {
          label: '事项列表信息',
          target: 'itemList'
        }, {
          label: '材料补齐',
          target: 'matBq'
        }, {
          label: '缴费信息',
          target: 'payInfo'
        }, {
          label: '短信通知模版',
          target: 'msgModule'
        }
      ],
      curWidth: (document.documentElement.clientWidth || document.body.clientWidth),//当前屏幕宽度
      curHeight: (document.documentElement.clientHeight || document.body.clientHeight),//当前屏幕高度
      loading: false,
    }
  },
  mounted: function () {
  },
  methods: {
    targetCollapse: function(target, index){
      this.activeTab = index;
    },
    handleScroll: function () { // 屏幕滚动纵向导航相应获取焦点
      var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
      var ele = $('.el-collapse-item');
      var eleLen = ele.length;
      var vm = this;
      for (var i = 0; i < eleLen; i++) {
        if (scrollTop >= ($(ele[i]).offset().top - 60)) {
          vm.activeTab = i
        }
      }
    },
    dateFormat: function (dateStr) {
      var t = new Date(dateStr);//row 表示一行数据, updateTime 表示要格式化的字段名称
      var month = t.getMonth() + 1 + "";
      var day = t.getDate() + "";
      var date = t.getFullYear() + '-' + month.length == 1 ? '0' + month : month + '-' + day.length == 1 ? '0' + day : day;
      return date;
    },

    // 预览电子件
    filePreview: function (data) {
      var vm = this;
      var detailId = data.fileId;
      var flashAttributes = '';
      // console.log(data);
      var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
      var fileName = data.fileName;
      var fileType = this.getFileType(fileName);
      vm.filePreviewCount++;
      if (fileType == 'pdf') {
        var tempwindow = window.open(); // 先打开页面
        setTimeout(function () {
          tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
        }, 1000)
      } else if (regText.test(fileType)) {
        // previewPdf/pdfIsCoverted
        vm.loading = true;
        request('', {
          url: ctx + 'previewPdf/pdfIsCoverted?detailId=' + detailId,
          type: 'get',
        }, function (result) {
          if (result.success) {
            vm.loading = false;
            var tempwindow = window.open(); // 先打开页面
            setTimeout(function () {
              tempwindow.location = ctx + 'previewPdf/view?detailId=' + detailId;
            }, 1000)
          } else {
            if (vm.filePreviewCount > 9) {
              confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                vm.filePreviewCount = 0;
                vm.filePreview(data);
              }, function () {
                vm.filePreviewCount = 0;
                vm.loading = false;
                return false;
              }, '确定', '取消', 'warning', true)

            } else {
              setTimeout(function () {
                vm.filePreview(data);
              }, 1000)
            }
          }
        }, function (msg) {
          vm.loading = false;
          vm.$message({
            message: '文件预览失败',
            type: 'error'
          });
        })
      } else {
        vm.loading = false;
        var tempwindow = window.open(); // 先打开页面
        setTimeout(function () {
          tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
        }, 1000)
      }
    },
    // 获取文件后缀
    getFileType: function (fileName) {
      var index1 = fileName.lastIndexOf(".");
      var index2 = fileName.length;
      var suffix = fileName.substring(index1 + 1, index2).toLowerCase();//后缀名
      if (suffix == 'docx') {
        suffix = 'doc';
      }
      return suffix;
    },
    // 下载电子件
    downloadFile: function () {
      var vm = this;
      var detailIds = [];
      if (vm.fileSelectionList.length == 0) {
        vm.$message({
          message: '请勾选数据后操作！',
          type: 'error'
        });
        return false;
      }
      vm.fileSelectionList.map(function (item, index) {
        detailIds.push(item.matinstId);
      });
      detailIds = detailIds.join(',')
      window.location.href = ctx + 'rest/mats/att/download?detailIds=' + detailIds
    },

    // 关闭窗口-(这里使用的是父级window下面的vue实例中的方法来移除tab,这个tab是ele控件)
    closeWindowTab: function () {
      var ts = this;
      ts.smsSendEndRefresh();

      setTimeout(function () {
        window.parent.vm.removeTab(ts.getSerachParamsForUrl('applyinstId'));
      }, 1000)
    },
  },

});

