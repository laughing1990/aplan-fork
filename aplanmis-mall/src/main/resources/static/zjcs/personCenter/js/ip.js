var isDevelop = 0;
if (isDevelop) {
  // ctx = '[[@{/}]]';
  // ctx = 'http://localhost:8083/aplanmis-front/';  // localhost
  ctx = 'http://39.107.139.174:8083/aplanmis-front/';  // 174
  // ctx = 'http://192.168.32.46:8083/aplanmis-front/';  // 李文钦
  // ctx = 'http://192.168.30.24:8083/aplanmis-front/';  // 陈锭星
  // ctx = 'http://192.168.30.110:8083/aplanmis-front/'; // 邹永胜
  // ctx = 'http://192.168.14.2:8083/aplanmis-front/'; // 邓振强
  // ctx = 'http://192.168.17.15:8083/aplanmis-front/'; // 陈泽浩
  // ctx = 'http://192.168.15.59:8083/aplanmis-front/'; // 王玲
  // ctx = 'http://192.168.15.179:8083/aplanmis-front/'; // 陈健锋
  // ctx = 'http://192.168.32.53:8083/aplanmis-front/'; // 陈杰文
  // ctx = 'http://192.168.30.93:8083/aplanmis-front/';  // 张伟宁
  // ctx = 'http://192.168.30.120:8083/aplanmis-front/';  // 蒋进涛
  // ctx = 'http://192.168.15.101:8083/aplanmis-front/';  // 王超
}

var __STATIC = {
  chineseIndexArr: ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十'],
  // time => 2019-09-09 12:12:12
  formatDate: function (time, formatStr) {
    if (!time) return '';
    var date = new Date(time);
    if (!date) return '';
    var str = 'yyyy-MM-dd hh:mm'
    return formatDate(date, formatStr || str);
  },
  // 获取数据字典值
  getDicList: function (obj) {
    // obj.code,obj.vm,obj.dataStr,obj.callback,
    obj.vm[obj.dataStr] = []; // 清空
    request('', {
      url: ctx + 'rest/dict/code/items/list',
      type: 'get',
      data: {dicCodeTypeCode: obj.code},
    }, function (res) {
      if (res.success) {
        // itemCode  itemName
        var tmp = res.content || [];
        obj.vm[obj.dataStr] = tmp;
        typeof obj.callback == 'function' && obj.callback(res);
      } else {
        obj.vm.$message.error(res.message);
      }
    }, function () {
      obj.vm.$message.error('获取数据字典失败');
    })
  },
  allowPreType: {
    'doc': true,
    'docx': true,
    'pdf': true,
    'ppt': true,
    'pptx': true,
    'xls': true,
    'xlsx': true,
    'txt': true,
    'dwg': true,
  },
  getUrlParam: function (name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
      return unescape(r[2]);
    }
    return null;
  },
  previewFile: function (data) {
    var detailId = data.fileId;
    if (!detailId) return alert('fileId不能为空');
    var flashAttributes = '';
    var tempwindow = window.open('_blank'); // 先打开页面
    tempwindow.location = ctx + 'rest/mats/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
  },
  staticIconData: {
    "7Z": {text: "&#xe63c", color: "rgb(87, 193, 255)"},
    "AI": {text: "&#xe63d", color: "rgb(255, 132, 0)"},
    "AVI": {text: "&#xe63e", color: "rgb(239, 52, 80)"},
    "BMP": {text: "&#xe63f", color: "rgb(201, 190, 24)"},
    "EPS": {text: "&#xe640", color: "rgb(64, 123, 248)"},
    "EXE": {text: "&#xe641", color: "rgb(246, 228, 0)"},
    "FLV": {text: "&#xe642", color: "rgb(218, 60, 9)"},
    "DOC": {text: "&#xe643", color: "rgb(56, 120, 222)"},
    "GIF": {text: "&#xe644", color: "rgb(132, 220, 0)"},
    "MOV": {text: "&#xe645", color: "rgb(176, 215, 0)"},
    "HTML": {text: "&#xe646", color: "rgb(108, 182, 0)"},
    "MP4": {text: "&#xe647", color: "rgb(52, 130, 240)"},
    "PDF": {text: "&#xe648", color: "rgb(234, 67, 24)"},
    "MP3": {text: "&#xe649", color: "rgb(71, 77, 226)"},
    "PPT": {text: "&#xe64a", color: "rgb(248, 82, 7)"},
    "PNG": {text: "&#xe64b", color: "rgb(252, 204, 0)"},
    "PSD": {text: "&#xe64c", color: "rgb(54, 118, 244)"},
    "RAR": {text: "&#xe64d", color: "rgb(26, 142, 247)"},
    "SVG": {text: "&#xe64e", color: "rgb(26, 142, 247)"},
    "SWF": {text: "&#xe64f", color: "rgb(181, 107, 0)"},
    "RP": {text: "&#xe650", color: "rgb(113, 60, 190)"},
    "TIF": {text: "&#xe651", color: "rgb(67, 167, 59)"},
    "TXT": {text: "&#xe652", color: "rgb(85, 215, 224)"},
    "ZIP": {text: "&#xe653", color: "rgb(220, 78, 193)"},
    "JPG": {text: "&#xe654", color: "rgb(61, 182, 251)"},
    "WPS": {text: "&#xe655", color: "rgb(51, 211, 157)"},
    "TAR": {text: "&#xe656", color: "rgb(186, 76, 215)"},
    "XSL": {text: "&#xe657", color: "rgb(139, 199, 85)"},
  },
  getIconData: function (type) {
    var defalutData = {text: "&#xe6b1", color: "rgb(56, 120, 222)"};
    return __STATIC.staticIconData[type.toUpperCase()] || defalutData;
  },
  getIconText: function (type) {
    return __STATIC.getIconData(type).text;
  },
  getIconColor: function (type) {
    return __STATIC.getIconData(type).color;
  },
}