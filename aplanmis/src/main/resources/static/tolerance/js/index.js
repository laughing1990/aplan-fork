// 节流函数
var throttle = function (method, delay, time) {
  var timeout, startTime = +new Date();
  return function () {
    var context = this,
      args = arguments,
      curTime = +new Date();
    clearTimeout(timeout);
    // 如果达到了规定的触发时间间隔，触发 handler
    if (curTime - startTime >= time) {
      method.apply(context, args);
      startTime = curTime;
    } else {
      // 没达到触发间隔，重新设定定时器
      timeout = setTimeout(method, delay);
    }
  }
}

// 简单解密
function uncompile(code) {
  code = unescape(code);
  var c = String.fromCharCode(code.charCodeAt(0) - code.length);
  for (var i = 1; i < code.length; i++) {
    c += String.fromCharCode(code.charCodeAt(i) - c.charCodeAt(i - 1));
  }
  return c;
};

// 侧边栏导航数据
var NAVLEFTDATA = [{
  label: '项目基本信息',
  target: 'projBaseInfo'
}];

function formatDate(date, fmt) {
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  var o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'h+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds()
  }
  for (var k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      var str = o[k] + ''
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str))
    }
  }
  return fmt
}

function padLeftZero(str) {
  return ('00' + str).substr(str.length)
}

var pager = new Vue({
  el: '#app',
  data: function () {
    var checkMissValue = function (rule, value, callback) {
      if (value === '' || value === undefined || value === null) {
        callback(new Error('该输入项为必输项！'));
      } else if (value.toString().trim() === '') {
        callback(new Error('该输入项为必输项！'));
      } else {
        callback();
      }
    };
    // 输入为数字 大于0
    var checkMissNum = function (rule, value, callback) {
      if (value === '' || value === undefined || value === null || value.toString().trim() === '') {
        callback(new Error('必填！'));
      } else if (value) {
        var flag = !/^[0-9]\d*$/.test(value) && !(/^[0-9]\d*$/.test(value));
        if (flag) {
          return callback(new Error('格式错误'));
        } else {
          if (parseInt(value) <= 0) {
            return callback(new Error('不能为0'));
          } else {
            callback();
          }
        }
      } else {
        // callback();
        if (parseInt(value) <= 0) {
          return callback(new Error('不能为0'));
        } else {
          callback();
        }
      }
    };
    var checkNumber = function (rule, value, callback) {
      var reg = /[^\d^\.]+/g
      // console.log(reg.test(value));
      if (!reg.test(value)) {
        callback();
      } else {
        return callback(new Error('请输入数字'));
      }
    };
    // 输入为数字 大于0 ===>使用在材料的电子件当中
    var checkMissNumFormat = function (rule, value, callback) {
      if (value === '' || value === undefined || value === null || value.toString().trim() === '') {
        callback(new Error('请上传！'));
      } else if (value) {
        var flag = !/^[0-9]\d*$/.test(value) && !(/^[0-9]\d*$/.test(value));
        if (flag) {
          return callback(new Error('格式错误'));
        } else {
          if (parseInt(value) <= 0) {
            return callback(new Error('请上传！'));
          } else {
            callback();
          }
        }
      } else {
        // callback();
        if (parseInt(value) <= 0) {
          return callback(new Error('请上传！'));
        } else {
          callback();
        }
      }
    };
    // 校验不能纯数字(非必填)
    var checkNotAllNumber = function (rule, value, callback) {
      var reg = /[^\d^\.]+/g
      // console.log(reg.test(value));
      if (!value || reg.test(value)) {
        callback();
      } else {
        return callback(new Error('不能为纯数字'));
      }
    };
    var checkPhoneNum = function (rule, value, callback) {
      if (value === '' || value === undefined || value.trim() === '') {
        callback(new Error('必填！'));
      } else if (value) {
        var flag = !/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/.test(value) && !(/^1(3|4|5|6|7|8|9)\d{9}$/.test(value));
        if (flag) {
          return callback(new Error('格式不正确'));
        } else {
          callback();
        }

      } else {
        callback();
      }
    };
    return {
      // 全局loading
      mloading: false,
      // 右侧栏导航
      activeNames: ['1', '2', '3', '4', '5', '6'], // el-collapse 默认展开列表
      verticalTabData: [ // 左侧纵向导航数据
        {
          label: '项目基本信息',
          target: 'projBaseInfo'
        }, {
          label: '容缺材料待审核',
          target: 'waitAudit'
        }
      ],
      activeTab: 0, // 纵向导航active状态index
      pageInfoData: {}, //页面数据
      projInfoForm: {
        tableList: [],
        rules: {
          // 审核意见校验
          fillOpinion: [{
            required: true,
            message: '请填写',
            trigger: ['blur']
          }]
        },
      },

      allPageData: {}, //所有的页面数据
    }
  },
  mounted: function () {
    var _that = this;
    window.addEventListener('scroll', _that.handleScroll);
    window.addEventListener('resize', throttle(function (ev) {
      _that.navLeft()
    }, 100, 600));
    _that.navLeft();
  },
  created: function () {
    this.fetchPagerInfo();
  },
  methods: {
    // 页面resize
    navLeft: function () {
      var _windW = $(window).width();
      // console.log(_windW)
      setTimeout(function () {
        if (_windW < 1240) {
          $('.right-handel-pandel').addClass('box-shadow-pandel')
        } else {
          $('.right-handel-pandel').removeClass('box-shadow-pandel')
        }
      }, 100)
    },
    // 公共方法
    // 获取url中的查询参数
    getSerachParamsForUrl: function (name) {
      var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      }
      return null;
    },
    // ui方法
    apiMessage: function (msg, type) {
      this.$message({
        message: msg,
        type: type
      })
    },
    // 页面导航操作方法
    targetCollapse: function (target, ind) { // 纵向导航点击事件
      this.activeTab = ind;
      $(document).scrollTop($('#' + target).offset().top)
    },
    handleScroll: function () { // 屏幕滚动纵向导航相应获取焦点
      var scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;
      var ele = $('.el-collapse-item');
      var eleLen = ele.length;
      var _that = this;
      for (var i = 0; i < eleLen; i++) {
        if (scrollTop >= ($(ele[i]).offset().top - 60)) {
          _that.activeTab = i
        }
      }
    },
    // 表格缺省
    tableCell: function (row, column, cellValue, index) {
      if (cellValue) return cellValue;
      return '-';
    },


    // 获取页面中回显的数据
    fetchPagerInfo: function () {
      var ts = this,
        getData = {};
      getData.fillId = ts.getSerachParamsForUrl('fillId');
      ts.mloading = true;
      request('', {
        url: ctx + 'rest/item/fill/detail',
        type: 'get',
        data: getData
      }, function (res) {
        ts.mloading = false;
        if (res.success) {
          ts.allPageData = res.content;
          ts.projInfoForm.tableList = res.content.itemFillDueIninstList
        } else {
          return ts.apiMessage(res.message, 'error')
        }
      }, function () {
        ts.mloading = false;
        return ts.apiMessage('网络错误！', 'error')
      });
    },

    // 获取文件类型图标
    getFileTypeIcon: function (fileType) {
      var fileType = "." + fileType;
      if (/\.(jpg|jpeg)$/.test(fileType)) {
        return 'ag-filetype-jpg';
      } else if (/\.(gif|JPG)$/.test(fileType)) {
        return 'ag-filetype-gif';
      } else if (/\.(png|PNG)$/.test(fileType)) {
        return 'ag-filetype-png';
      } else if (/\.(doc|DOC|docx|DOCX)$/.test(fileType)) {
        return 'ag-filetype-doc';
      } else if (/\.(pdf|PDF)$/.test(fileType)) {
        return 'ag-filetype-pdf';
      } else if (/\.(excl|EXCL|xlsx)$/.test(fileType)) {
        return 'ag-filetype-xsl';
      } else if (/\.(svg|SVG)$/.test(fileType)) {
        return 'ag-filetype-svg';
      } else if (/\.(ppt|PPT|pptx|PPTX)$/.test(fileType)) {
        return 'ag-filetype-ppt';
      } else if (/\.(rar|RAR)$/.test(fileType)) {
        return 'ag-filetype-rar';
      } else if (/\.(7z|7Z)$/.test(fileType)) {
        return 'ag-filetype-7z';
      } else if (/\.(zip|ZIP)$/.test(fileType)) {
        return 'ag-filetype-zip';
      } else if (/\.(html|HTML)$/.test(fileType)) {
        return 'ag-filetype-html';
      } else if (/\.(txt|TXT)$/.test(fileType)) {
        return 'ag-filetype-txt';
      } else if (/\.(dir|DIR)$/.test(fileType)) {
        return 'ag-folder-fill';
      } else if (/\.(mov|MOV)$/.test(fileType)) {
        return 'ag-folder-fill';
      } else if (/\.(rp|RP)$/.test(fileType)) {
        return 'ag-filetype-rp';
      } else if (/\.(exe|EXE)$/.test(fileType)) {
        return 'ag-filetype-exe';
      } else {
        return 'ag-filetype-wps';
      }
    },

    // 下载电子件
    downloadFile: function (detailIds) {
      window.open(ctx + 'rest/file/applydetail/mat/download/' + detailIds)
    },
    // 预览电子件
    previewFile: function (data) {
      var detailId = data.detailId;
      var _that = this;
      var regText = /doc|docx|pdf|ppt|pptx|xls|xlsx|txt$/;
      var fileName = data.attName;
      var fileType = this.getFileType(fileName);
      var flashAttributes = '';
      _that.filePreviewCount++
      if (fileType == 'pdf') {
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
          tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
        }, 1000)
      }
    },
    // 预览源文件
    filePreview: function (data) {
      var detailId = data.detailId;
      var flashAttributes = '';
      var tempwindow = window.open(); // 先打开页面
      setTimeout(function () {
        tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
      }, 1000)
    },
    // 获取文件后缀
    getFileType: function (fileName) {
      var index1 = fileName.lastIndexOf(".");
      var index2 = fileName.length;
      var suffix = fileName.substring(index1 + 1, index2).toLowerCase(); //后缀名
      return suffix;
    },

    // 重置校验规则
    clearValidate: function(index, tag){
      // console.log(tag)
      if(tag == 1){
        this.$refs['projInfoForm'].clearValidate(index);
      }    
    },
    // 确定保存审核后数据
    saveAuditData: function () {
      var ts = this;
      ts.$refs['projInfoForm'].validate(function (valid) {
        if (valid) {
          console.log(ts.projInfoForm.tableList)
          ts.mloading = true;
          request('', {
            url: ctx + 'rest/item/fill/saveLeranceAproveOpinion',
            type: 'post',
            ContentType: 'application/json;charset=utf-8',
            data: JSON.stringify(ts.projInfoForm.tableList)
          }, function (res) {
            ts.mloading = false;
            if (res.success) {
              ts.$message.success('保存成功！');
            } else {
              return ts.apiMessage(res.message, 'error');
            }
          }, function () {
            ts.mloading = false;
            return ts.apiMessage('网络错误！', 'error');
          });
        } else {
          ts.$message.warning('请完善审核内容');
          return false;
        }
      });
    },
  },
  filters: {
    formatDate: function (val, mat) {
      if (!val) return '暂无'
      var _date = new Date(val);
      return formatDate(_date, mat)
    },
    // 补齐截止期限-类型
    timeLimitTypeFormat: function (val) {
      var list = ['证件领取前', '证件签发前', '证件领取后3个月'];
      return val ? list[val] : '-';
    },
  },
});