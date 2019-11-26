var isDevelop = 0;
if (isDevelop) {
  // ctx = '[[@{/}]]';
  // ctx = 'http://localhost:8083/aplanmis-front/'; // localhost
  // ctx = 'http://106.52.77.101:8083/aplanmis-front/'; // 101
  // ctx = 'http://106.52.77.101:8090/aplanmis-front/'; // 101
  // ctx = 'http://192.168.32.46:8083/aplanmis-front/'; // 李文钦
  ctx = 'http://192.168.32.59:8083/aplanmis-front/'; // 赵雄
  // ctx = 'http://192.168.30.24:8083/aplanmis-front/'; // 陈锭星
  // ctx = 'http://192.168.30.110:8083/aplanmis-front/'; // 邹永胜
  // ctx = 'http://192.168.30.98:8083/aplanmis-front/'; // 邹永胜
  // ctx = 'http://192.168.14.2:8083/aplanmis-front/'; // 邓振强
  // ctx = 'http://192.168.17.15:8083/aplanmis-front/'; // 陈泽浩
  // ctx = 'http://192.168.15.59:8083/aplanmis-front/'; // 王玲
  // ctx = 'http://192.168.15.179:8083/aplanmis-front/'; // 陈健锋
  // ctx = 'http://192.168.32.53:8083/aplanmis-front/'; // 陈杰文
  // ctx = 'http://192.168.30.93:8083/aplanmis-front/'; // 张伟宁
  // ctx = 'http://192.168.30.120:8083/aplanmis-front/'; // 蒋进涛
  // ctx = 'http://192.168.15.101:8083/aplanmis-front/'; // 王超
  // ctx = 'http://192.168.30.125:8083/aplanmis-front/'; // 黄治亮
}

var __STATIC = {
  // 组装补正数据
  packageBzData: function (arr, flag) {
    var tmp = [];
    arr.forEach(function (u, uIndex) {
      var count = 0;
      var hasPaper = typeof u.paperCount == "number" && u.paperCount > 0;
      var hasCopy = typeof u.copyCount == "number" && u.copyCount > 0;
      var hasAtt = typeof u.isNeedAtt == 'string' && u.isNeedAtt != 0;
      // hasPaper = true;
      // hasCopy = true;
      // hasAtt = true;
      if (hasPaper) {
        count++;
      }
      if (hasCopy) {
        count++;
      }
      if (hasAtt) {
        count++;
      }
      var resultO = {};
      if (hasPaper) {
        // 原件
        resultO = $.extend({}, u);
        resultO.rowType = 1;
        resultO.rowTypeText = '原件';
        resultO.tmpMatId = u.matId + '_paper';
        resultO.rowspan = count;
        resultO.colspan = 1;
        resultO.uIndex = uIndex;
        if (!flag) {
          resultO.paperDueIninstOpinion = '';
          resultO.paperCount = 1;
        }
        tmp.push(resultO);
      }
      if (hasCopy) {
        // 复印件
        resultO = $.extend({}, u);
        resultO.rowType = 2;
        resultO.rowTypeText = '复印件';
        resultO.tmpMatId = u.matId + '_copy';
        if (hasPaper) {
          resultO.rowspan = 0;
          resultO.colspan = 0;
        } else {
          resultO.rowspan = count == 2 ? 2 : 1;
          resultO.colspan = count == 2 ? 1 : 1;
        }
        resultO.uIndex = uIndex;
        if (!flag) {
          resultO.copyDueIninstOpinion = '';
          resultO.copyCount = 1;
        }
        tmp.push(resultO);
      }
      if (hasAtt) {
        // 电子件
        resultO = $.extend({}, u);
        resultO.rowType = 3;
        resultO.rowTypeText = '电子件';
        resultO.tmpMatId = u.matId + '_att';
        resultO.rowspan = count == 1 ? 1 : 0;
        resultO.colspan = count == 1 ? 1 : 0;
        resultO.uIndex = uIndex;
        if (!flag) {
          resultO.attDueIninstOpinion = '';
          resultO.isNeedAtt = '1';
        }
        tmp.push(resultO);
      }
    });
    return tmp;
  },
  chineseIndexArr: ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二', '十三', '十四', '十五', '十六', '十七', '十八', '十九', '二十'],
  getChineseIndex: function (index, length) {
    var _index = length - index - 1;
    return __STATIC.chineseIndexArr[_index] || '20+';
  },
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
  // 关闭当前页面
  closeCurrentTab: function () {
    var userAgent = navigator.userAgent;
    if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Presto") != -1) {
      window.location.replace("about:blank");
    }
    window.location.href = 'about:blank';
    window.opener = null;
    window.open("", "_self");
    window.close();
  },
  // 默认延迟1000毫秒刷新页面
  delayRefreshWindow: function (time) {
    window.setTimeout(function () {
      window.location.reload();
    }, time || 1000);
  },
  // 默认延迟2000毫秒关闭页面
  delayCloseWindow: function (time) {
    window.setTimeout(function () {
      __STATIC.closeCurrentTab();
    }, time || 2000);
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
};
var bzMockData = [{
  "correctId": "7f59fe1a-d91f-4f01-aa3d-99b7a9dffff2",
  "iteminstId": "c59d29cc-3bb7-4adc-844c-d80389f79b80",
  "applyinstId": "6e5fc6c9-82df-4aef-b278-60cdceb845ad",
  "projInfoId": "1d0a3b68-00ba-4963-bb3f-62d20eba12be",
  "isFlowTrigger": "1",
  "appinstId": "3ffeb85b-4b53-4c84-95f9-dc6940efe2fa",
  "correctDueDays": 2,
  "correctDueTime": 1573111054000,
  "matinstIds": "081e64d2-53b6-419e-b3a4-1add78c2bc8c,fef42af4-9fc5-4845-9981-b44600d1f925,56857bfd-ce7d-4335-ad9d-4e7314842658,fd82840e-0b46-4541-8af4-a2d050ced14d",
  "correctDesc": null,
  "correctMemo": "memo",
  "chargeOrgId": "10c242cf-5d4a-4314-8fc0-4cf85e9e53aa",
  "chargeOrgName": "市住建局",
  "windowUserId": "3a7d6d4d-8b73-496c-85e0-390be1da35f4",
  "windowUserName": "窗口人员",
  "windowPhone": null,
  "opsUserId": "3a7d6d4d-8b73-496c-85e0-390be1da35f4",
  "opsUserName": "窗口人员",
  "opsTime": 1572943901288,
  "correctUserName": null,
  "correctEndTime": null,
  "printUserId": null,
  "printUserName": null,
  "printTime": null,
  "correctState": "6",
  "isDeleted": "0",
  "creater": "fbe5f199-450d-4a3e-9903-5a59f1c1bc71",
  "createTime": 1572938254000,
  "modifier": null,
  "modifyTime": null,
  "rootOrgId": "0368948a-1cdf-4bf8-a828-71d796ba89f6",
  "projInfoName": "溪南镇仙门村70.56KW光伏扶贫项目",
  "localCode": "2019-440515-44-03-062744",
  "applyinstCode": "201910110032",
  "owner": "胡凯",
  "iteminstName": "燃气设施建设工程竣工验收备案",
  "applySubject": "0",
  "linkmanInfoId": "32042b15-e4a4-4ac1-841f-f9b29097f6ef",
  "matCorrectDtos": [{
    "matId": null,
    "matName": null,
    "matCode": null,
    "reviewKeyPoints": "无",
    "reviewSampleDoc": null,
    "sampleDoc": null,
    "templateDoc": null,
    "matinstName": "建设工程质量监管部门批准的符合要求的竣工验收备案材料",
    "attMatinstId": "fef42af4-9fc5-4845-9981-b44600d1f925",
    "paperMatinstId": null,
    "copyMatinstId": null,
    "paperCount": null,
    "copyCount": null,
    "isNeedAtt": "1",
    "attIsCollected": null,
    "paperIsCollected": null,
    "copyIsCollected": null,
    "attCount": 0,
    "realPaperCount": null,
    "realCopyCount": null,
    "attInoutinstId": "6712ea7c-9592-4080-95e8-9a3ebba854cd",
    "paperInoutinstId": null,
    "copyInoutinstId": null,
    "attDueIninstId": "b5ca9e7d-8776-4755-b89c-a7cd6be9ec04",
    "copyDueIninstId": null,
    "paperDueIninstId": null,
    "attDueIninstOpinion": "意见2",
    "copyDueIninstOpinion": null,
    "paperDueIninstOpinion": null,
    "attRealIninstId": "ab0e63e7-199e-4885-9f93-092162793179",
    "copyRealIninstId": null,
    "paperRealIninstId": null,
    "attIsPass": null,
    "copyIsPass": null,
    "paperIsPass": null
  }, {
    "matId": null,
    "matName": null,
    "matCode": null,
    "reviewKeyPoints": "无",
    "reviewSampleDoc": null,
    "sampleDoc": null,
    "templateDoc": null,
    "matinstName": "法规、规章、规定必须提供的其他文件",
    "attMatinstId": "fd82840e-0b46-4541-8af4-a2d050ced14d",
    "paperMatinstId": null,
    "copyMatinstId": null,
    "paperCount": null,
    "copyCount": null,
    "isNeedAtt": "1",
    "attIsCollected": null,
    "paperIsCollected": null,
    "copyIsCollected": null,
    "attCount": 0,
    "realPaperCount": null,
    "realCopyCount": null,
    "attInoutinstId": "3772634b-aa0f-40ce-8405-f59b2e61d20b",
    "paperInoutinstId": null,
    "copyInoutinstId": null,
    "attDueIninstId": "da2886c3-bf71-4f43-8e3b-7e6b5f0b04d2",
    "copyDueIninstId": null,
    "paperDueIninstId": null,
    "attDueIninstOpinion": "意见4",
    "copyDueIninstOpinion": null,
    "paperDueIninstOpinion": null,
    "attRealIninstId": "e99c2a5d-e17e-4d40-9b7b-ebfdb2f80610",
    "copyRealIninstId": null,
    "paperRealIninstId": null,
    "attIsPass": null,
    "copyIsPass": null,
    "paperIsPass": null
  }, {
    "matId": null,
    "matName": null,
    "matCode": null,
    "reviewKeyPoints": "无",
    "reviewSampleDoc": null,
    "sampleDoc": null,
    "templateDoc": null,
    "matinstName": "五方责任主体出具的竣工验收合格文件",
    "attMatinstId": "56857bfd-ce7d-4335-ad9d-4e7314842658",
    "paperMatinstId": null,
    "copyMatinstId": null,
    "paperCount": null,
    "copyCount": null,
    "isNeedAtt": "1",
    "attIsCollected": null,
    "paperIsCollected": null,
    "copyIsCollected": null,
    "attCount": 0,
    "realPaperCount": null,
    "realCopyCount": null,
    "attInoutinstId": "e4ae868d-1994-473a-9315-c49755fa581c",
    "paperInoutinstId": null,
    "copyInoutinstId": null,
    "attDueIninstId": "06179f2d-f7bf-40bf-ac16-881970b1ed24",
    "copyDueIninstId": null,
    "paperDueIninstId": null,
    "attDueIninstOpinion": "意见3",
    "copyDueIninstOpinion": null,
    "paperDueIninstOpinion": null,
    "attRealIninstId": "5fff340d-5bf3-4f25-8661-ce86357b6a63",
    "copyRealIninstId": null,
    "paperRealIninstId": null,
    "attIsPass": null,
    "copyIsPass": null,
    "paperIsPass": null
  }, {
    "matId": null,
    "matName": null,
    "matCode": null,
    "reviewKeyPoints": "无",
    "reviewSampleDoc": null,
    "sampleDoc": null,
    "templateDoc": null,
    "matinstName": "九江市燃气工程竣工验收备案表",
    "attMatinstId": "081e64d2-53b6-419e-b3a4-1add78c2bc8c",
    "paperMatinstId": null,
    "copyMatinstId": null,
    "paperCount": null,
    "copyCount": null,
    "isNeedAtt": "1",
    "attIsCollected": null,
    "paperIsCollected": null,
    "copyIsCollected": null,
    "attCount": 0,
    "realPaperCount": null,
    "realCopyCount": null,
    "attInoutinstId": "8d8ac8af-86b0-472b-86b8-8c31a9127a91",
    "paperInoutinstId": null,
    "copyInoutinstId": null,
    "attDueIninstId": "42e4eddf-4602-4fd7-8b2c-0eafd4b33a93",
    "copyDueIninstId": null,
    "paperDueIninstId": null,
    "attDueIninstOpinion": "意见1",
    "copyDueIninstOpinion": null,
    "paperDueIninstOpinion": null,
    "attRealIninstId": "603cb428-a85d-44a5-83cd-74ba51f5a02b",
    "copyRealIninstId": null,
    "paperRealIninstId": null,
    "attIsPass": null,
    "copyIsPass": null,
    "paperIsPass": null
  }],
  "regionalism": "185",
  "regionName": null
}, {
  "correctId": "ebedbeab-23c9-4832-b8df-cbe588ca53a6",
  "iteminstId": "c59d29cc-3bb7-4adc-844c-d80389f79b80",
  "applyinstId": "6e5fc6c9-82df-4aef-b278-60cdceb845ad",
  "projInfoId": "1d0a3b68-00ba-4963-bb3f-62d20eba12be",
  "isFlowTrigger": "1",
  "appinstId": "3ffeb85b-4b53-4c84-95f9-dc6940efe2fa",
  "correctDueDays": 3,
  "correctDueTime": 1571741691000,
  "matinstIds": "2894d89b-9ae9-45c7-a9f7-c93fb69d7037",
  "correctDesc": "暂无",
  "correctMemo": "test",
  "chargeOrgId": "10c242cf-5d4a-4314-8fc0-4cf85e9e53aa",
  "chargeOrgName": "市住建局",
  "windowUserId": "3a7d6d4d-8b73-496c-85e0-390be1da35f4",
  "windowUserName": "窗口人员",
  "windowPhone": null,
  "opsUserId": "3a7d6d4d-8b73-496c-85e0-390be1da35f4",
  "opsUserName": "窗口人员",
  "opsTime": 1571309847000,
  "correctUserName": "窗口人员",
  "correctEndTime": 1571309847000,
  "printUserId": null,
  "printUserName": null,
  "printTime": null,
  "correctState": "8",
  "isDeleted": "0",
  "creater": "fbe5f199-450d-4a3e-9903-5a59f1c1bc71",
  "createTime": 1571309691000,
  "modifier": "李英华",
  "modifyTime": 1571309903000,
  "rootOrgId": "0368948a-1cdf-4bf8-a828-71d796ba89f6",
  "projInfoName": "溪南镇仙门村70.56KW光伏扶贫项目",
  "localCode": "2019-440515-44-03-062744",
  "applyinstCode": "201910110032",
  "owner": "胡凯",
  "iteminstName": "燃气设施建设工程竣工验收备案",
  "applySubject": "0",
  "linkmanInfoId": "32042b15-e4a4-4ac1-841f-f9b29097f6ef",
  "matCorrectDtos": [{
    "matId": null,
    "matName": null,
    "matCode": null,
    "reviewKeyPoints": "无",
    "reviewSampleDoc": null,
    "sampleDoc": null,
    "templateDoc": null,
    "matinstName": "九江市燃气工程竣工验收备案表",
    "attMatinstId": null,
    "paperMatinstId": "2894d89b-9ae9-45c7-a9f7-c93fb69d7037",
    "copyMatinstId": null,
    "paperCount": 2,
    "copyCount": null,
    "isNeedAtt": null,
    "attIsCollected": null,
    "paperIsCollected": null,
    "copyIsCollected": null,
    "attCount": null,
    "realPaperCount": 2,
    "realCopyCount": null,
    "attInoutinstId": null,
    "paperInoutinstId": "88dee0c0-3075-4cef-a180-e42f12b8563b",
    "copyInoutinstId": null,
    "attDueIninstId": null,
    "copyDueIninstId": null,
    "paperDueIninstId": "e6b77922-90fb-4ab1-bb30-c2afba961f81",
    "attDueIninstOpinion": null,
    "copyDueIninstOpinion": null,
    "paperDueIninstOpinion": null,
    "attRealIninstId": null,
    "copyRealIninstId": null,
    "paperRealIninstId": "2f191d4d-7aec-48b2-b804-fb65f45d13d4",
    "attIsPass": null,
    "copyIsPass": null,
    "paperIsPass": null
  }],
  "regionalism": "185",
  "regionName": null
}];
//-------------------------------------------------------------------------
var idLibResMock = {
  "success": true,
  "message": "Query attachment success",
  "content": {
    "ack_code": null,
    "errors": null,
    "sign": null,
    "sign_method": null,
    "timestamp": null,
    "correlation_id": null,
    "response_id": null,
    "total_count": 4,
    "data": [
      {
        "license_code": "44070020190000090V",
        "name": "普通义工（志愿者）证",
        "license_type": null,
        "id_code": "122230",
        "doc_name": null,
        "doc_summary": null,
        "doc_keyword": null,
        "holder_name": "测试",
        "holder_identity_type": "10",
        "holder_identity_num": "440782199411112150",
        "issue_org_name": "江门政务服务数据管理局",
        "issue_org_code": "593330629",
        "division": "",
        "division_code": "440700",
        "issue_date": "2019-08-19 00:00:00",
        "begin_date": null,
        "expiry_date": null,
        "data_fields": null,
        "attachment_fields": null,
        "trust_level": "A",
        "extend_props": "",
        "remark": null,
        "biz_num": null,
        "license_item_code": "113002201",
        "license_status": "ISSUED",
        "creator": null,
        "creation_time": "2019-08-22 16:50:41",
        "issuer": null,
        "issue_time": null,
        "abolisher": null,
        "abolish_time": null,
        "abolish_reason": null,
        "correlative_license": null,
        "public_key": null,
        "s_sign_cert": null,
        "algorithm": null,
        "s_sign_data": null,
        "last_modificator": "*SYSADM*",
        "last_modification_time": "2019-08-22 16:50:41",
        "auth_code": "20191116155646494NC011300354_44070020190000090V",
        "license_status_name": "已签发",
        "license_type_name": null
      },
      {
        "license_code": "44070020190000090U",
        "name": "普通义工（志愿者）证",
        "license_type": null,
        "id_code": "12223",
        "doc_name": null,
        "doc_summary": null,
        "doc_keyword": null,
        "holder_name": "测试",
        "holder_identity_type": "10",
        "holder_identity_num": "440782199411112150",
        "issue_org_name": "江门政务服务数据管理局",
        "issue_org_code": "593330629",
        "division": "",
        "division_code": "440700",
        "issue_date": "2019-08-19 00:00:00",
        "begin_date": null,
        "expiry_date": null,
        "data_fields": null,
        "attachment_fields": null,
        "trust_level": "A",
        "extend_props": "",
        "remark": null,
        "biz_num": null,
        "license_item_code": "113002201",
        "license_status": "ISSUED",
        "creator": null,
        "creation_time": "2019-08-22 16:49:07",
        "issuer": null,
        "issue_time": null,
        "abolisher": null,
        "abolish_time": null,
        "abolish_reason": null,
        "correlative_license": null,
        "public_key": null,
        "s_sign_cert": null,
        "algorithm": null,
        "s_sign_data": null,
        "last_modificator": "*SYSADM*",
        "last_modification_time": "2019-08-22 16:49:07",
        "auth_code": "20191116155646494NC011300354_44070020190000090U",
        "license_status_name": "已签发",
        "license_type_name": null
      },
      {
        "license_code": "44070020190000080W",
        "name": "志愿服务记录证明",
        "license_type": null,
        "id_code": "1122",
        "doc_name": null,
        "doc_summary": null,
        "doc_keyword": null,
        "holder_name": "黄大三",
        "holder_identity_type": "10",
        "holder_identity_num": "440782199411112150",
        "issue_org_name": "江门市网络信息统筹局",
        "issue_org_code": "MB2C44198",
        "division": "",
        "division_code": "440700",
        "issue_date": "2019-07-26 00:00:00",
        "begin_date": null,
        "expiry_date": "2023-07-01 00:00:00",
        "data_fields": null,
        "attachment_fields": null,
        "trust_level": "A",
        "extend_props": "",
        "remark": null,
        "biz_num": null,
        "license_item_code": "213002401",
        "license_status": "ISSUED",
        "creator": null,
        "creation_time": "2019-07-26 18:02:17",
        "issuer": null,
        "issue_time": null,
        "abolisher": null,
        "abolish_time": null,
        "abolish_reason": null,
        "correlative_license": null,
        "public_key": null,
        "s_sign_cert": null,
        "algorithm": null,
        "s_sign_data": null,
        "last_modificator": "4f070706-c30c-481c-a837-9f39136c62de",
        "last_modification_time": "2019-07-26 18:04:59",
        "auth_code": "20191116155646494NC011300354_44070020190000080W",
        "license_status_name": "已签发",
        "license_type_name": null
      },
      {
        "license_code": "4407002019000008CS",
        "name": "普通义工（志愿者）证",
        "license_type": null,
        "id_code": "12333",
        "doc_name": null,
        "doc_summary": null,
        "doc_keyword": null,
        "holder_name": "测试",
        "holder_identity_type": "10",
        "holder_identity_num": "440782199411112150",
        "issue_org_name": "江门市网络信息统筹局",
        "issue_org_code": "MB2C44198",
        "division": "",
        "division_code": "440700",
        "issue_date": "2019-01-04 00:00:00",
        "begin_date": null,
        "expiry_date": "2020-01-03 00:00:00",
        "data_fields": null,
        "attachment_fields": null,
        "trust_level": "A",
        "extend_props": "",
        "remark": null,
        "biz_num": null,
        "license_item_code": "113002201",
        "license_status": "ISSUED",
        "creator": null,
        "creation_time": "2019-07-26 14:40:38",
        "issuer": null,
        "issue_time": null,
        "abolisher": null,
        "abolish_time": null,
        "abolish_reason": null,
        "correlative_license": null,
        "public_key": null,
        "s_sign_cert": null,
        "algorithm": null,
        "s_sign_data": null,
        "last_modificator": "4f070706-c30c-481c-a837-9f39136c62de",
        "last_modification_time": "2019-07-26 14:41:12",
        "auth_code": "20191116155646494NC011300354_4407002019000008CS",
        "license_status_name": "已签发",
        "license_type_name": null
      }
    ],
    "auth_codes": [
      "20191116155646494NC011300354_44070020190000090V",
      "20191116155646494NC011300354_44070020190000090U",
      "20191116155646494NC011300354_44070020190000080W",
      "20191116155646494NC011300354_4407002019000008CS"
    ]
  }
};