var gGreen = '#00C161';
var gGray = '#8E9CA3';
var gGraySlow = '#B8C8DC';
var gRed = '#FF4B47';
var gBlue = '#169AFF';
var gWhile = '#F6FBFA';
$(function () {
  $("#uploadProgress").modal("show");
  var proj = {};
  var cells = JSON.parse(themeVerDiagram).cells;
  var Main = {
    data() {
      return {
        ctx: ctx, //url前缀
        // 下拉树
        data: toTree(JSON.parse(projTree)),
        // 下拉树-prop
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        // 页面rappid的缩放参数
        c: {
          min: .2,
          max: 5,
          step: .1
        },
      };
    },
    methods: {
      handleNodeClick(data, checked, b) {
        var nodes = this.$children[0].getCheckedNodes();
        reqStatusListAndResetThemeverDiagram(data, nodes, proj, cells);
      },

      // 页面事件绑定
      domBindEvent: function () {
        // 头部项目名左侧下拉点击
        $('#proHead-icon').on('click', function () {
          var $picTip = $('#elTreeApp');
          if ($picTip.is(':hidden')) {
            $('#proHead-icon').attr('src', aUp);
            $picTip.show();
          } else {
            $('#proHead-icon').attr('src', aDown);
            $picTip.hide();
          }
        })

        // 图例点击
        $('.proHeadPicTip').on('click', function () {
          var $picTip = $('.itemColorRark');
          if ($picTip.is(':hidden')) {
            $picTip.show();
          } else {
            $picTip.hide();
          }
        })

        // 工具栏点击事件
        $('.toolMain, .toolFold').on('click', function (a, b) {
          var node = $('.toolPanel');
          var nodeWidth = $('.toolPanel').width();
          $('.toolMain').toggleClass('toolMain-active')
          if (nodeWidth < 150) { //如果node是隐藏的则显示node元素，否则隐藏
            node.css('width', '200px');
          } else {
            node.css('width', '66px');
          }
        });

        // 缩放
        var c = this.c;
        $('.zoomIn').on('click', function (a, b) {
          paperScroller.zoom(0.2, {
            max: c.max,
            grid: c.step
          });
        })
        $('.zoomOut').on('click', function (a, b) {
          paperScroller.zoom(-0.2, {
            min: c.min,
            grid: c.step
          });
        })
        $('.toolPng').on('click', function (a, b) {
          paper.showTools().openAsPNG();
        })
        $('.toolSvg').on('click', function (a, b) {
          paper.showTools().openAsSVG();
        })
      },
    },
    mounted: function () {
      var c = this.c;
      if (projName) {
        $('.proHeadContent').text(projName);
        $('.proHeadContent').attr('title', projName);
      }
      initThemeFlowChart(cells);
      proj[projInfoId] = JSON.parse(statusList);
      setTimeout("$('#uploadProgress').modal('hide');", 450);
      paperScroller.zoom(-0.2, {
        min: c.min,
        grid: c.step
      });
      this.domBindEvent();
    },
  };
  var Ctor = Vue.extend(Main);
  new Ctor().$mount('#app');
})



// 根据id获取元素
function getElementByEleId(id) {
  var eles = graph.getElements();
  var obj = null;
  $.each(eles, function (ind, ele) {
    if (ele.id == id) {
      obj = ele;
      return;
    }
  })
  return obj;
}

// 事项通过改变颜色
function changeEleColorIfPass(itemIds, type, json) {
  var color, fontcolor, png;
  switch (type) {
    case 'FINISHED': //办理通过
      png = 'finished.png', color = gGreen, fontcolor = gWhile;
      break;
    case 'NOT_PASS': //不通过
      png = 'not_pass.png', color = gRed, fontcolor = gWhile;
      break;
    case 'TOLERENCE_PASS': //容缺通过
      png = 'tolerence_pass.png', color = gGreen, fontcolor = gWhile;
      break;
    case 'UN_FINISHED': //未办
      png = 'un_finished.png', color = gWhile, fontcolor = gGray;
      break;
    case 'CORRECT_MATERIAL_START': //补正开始
      png = 'correct_material_start.png', color = gBlue, fontcolor = gWhile;
      break;
    case 'CORRECT_MATERIAL_END': //补正结束
      png = 'correct_material_end.png', color = gBlue, fontcolor = gWhile;
      break;
    case 'SPECIFIC_PROC_START': //特程开始
      png = 'specific_proc_start.png', color = gBlue, fontcolor = gWhile;
      break;
    case 'SPECIFIC_PROC_END': //特程结束
      png = 'specific_proc_end.png', color = gBlue, fontcolor = gWhile;
      break;
    case 'NORMAL_ACCEPT': //正常受理
      png = '', color = gBlue, fontcolor = gWhile;
      break;
    case 'HANDLING': //正常受理
      png = '', color = gBlue, fontcolor = gWhile;
      break;
    case 'NEED_NOT_HANDLE': //不用办
      png = 'need_not_handle.png', color = gWhile, fontcolor = gGray;
      break;
    case 'NEED_HANDLE_BUT_UNDO': //要办未办
      png = '', color = gWhile, fontcolor = gGray;
      break;
    default: //未知
      png = 'unknown.png', color = gWhile, fontcolor = gGray;
  }
  setEleJsonAttr(itemIds, json, color, fontcolor, '特别程序', type, png);

}

function changeStageColorIfPass(eleId, json, status, color) {
  for (var i in json.cells) {
    if (json.cells[i].id == eleId) {
      // json.cells[i]['attrs/.body/fill']=color;
      if (!json.cells[i].attrs['.body']) {
        json.cells[i].attrs['.body'] = {};
      }
      if (!json.cells[i].attrs['.header']) {
        json.cells[i].attrs['.header'] = {};
        json.cells[i].attrs['.header']['stroke'] = {};
      }
      json.cells[i].attrs['.body']['stroke'] = color;
      json.cells[i].attrs['.header']['stroke'] = color;
      json.cells[i].attrs['.header']['fill'] = color;
    }
  }

}
var ai = 0;

function setEleJsonAttr(itemIds, json, color, fontColor, statusName, type, png) {
  for (var i in json.cells) {
    if (json.cells[i].type == 'bpmn.Activity') {
      ai++;
      var attrs = json.cells[i].attrs;
      if (json.cells[i].attrs.item && json.cells[i].attrs.item.itemVerId) {
        var itemid = json.cells[i].attrs.item.itemVerId;
        itemid = itemid.substring(0, itemid.indexOf("*"));
        if (itemIds.indexOf(itemid) >= 0) {
          // json.cells[i]['attrs/.body/fill']=color;
          if (json.cells[i].attrs['.content']) {
            json.cells[i].attrs['.content']['gid'] = itemIds;
            json.cells[i].attrs['.content']['png'] = png;
            json.cells[i].attrs['.content']['col'] = color;
            if (!json.cells[i].attrs['.content']['style']) {
              json.cells[i].attrs['.content']['style'] = {}
            }
            json.cells[i].attrs['.content']['style']['color'] = {};
            json.cells[i].attrs['.content']['style']['border-left'] = '5px solid  ' + color;
            json.cells[i].attrs['.content']['style']['border-top'] = '1px solid  ' + color;
            json.cells[i].attrs['.content']['style']['border-right'] = '5px solid  ' + color;
            json.cells[i].attrs['.content']['style']['border-bottom'] = '1px solid  ' + color;
            if (!json.cells[i].attrs['.outer'] || !json.cells[i].attrs['.outer']['stroke']) {
              json.cells[i].attrs['.outer'] = {}, attrs['.outer']['stroke'] = {};
            }
            // json.cells[i].attrs['.outer']['stroke']=color;
          }
          if (!json.cells[i].attrs['.body']) {
            json.cells[i].attrs['.body'] = {};
          }

          if (type == 'NEED_NOT_HANDLE') { //无需办理
            json.cells[i].attrs['.content']['style']['color'] = fontColor;
            json.cells[i].attrs['.body']['fill'] = color;
            json.cells[i].attrs['.outer']['stroke'] = fontColor;
          }
          break;
        }
        /*if(json.cells[i].attrs['.content']['html']){
            json.cells[i].attrs['.content']['html'] = attrs['.content']['html'].replace('状态展示',statusName);
        }*/
      }
    }
  }
}

function changeStatus(statusList, json) {
  for (var i in statusList) {
    if (statusList[i]) {
      if (statusList[i].statusValue == 'FINISHED') {
        changeStageColorIfPass(statusList[i].stageId, json, statusList[i].statusValue, gGreen);
      } else if (statusList[i].statusValue == 'UN_FINISHED') {
        changeStageColorIfPass(statusList[i].stageId, json, statusList[i].statusValue, gBlue);
      } else {
        changeStageColorIfPass(statusList[i].stageId, json, statusList[i].statusValue, gBlue);
      }
    }
    var itemList = statusList[i].diagramItemList;
    if (itemList) {
      for (var j in itemList) {
        changeEleColorIfPass(itemList[j].itemIds, itemList[j].statusValue, json);
      }
    }
  }

  handleParallelStage(json);

  themeVerDiagram = JSON.stringify(json);
  themeVerDiagram = themeVerDiagram.replace(/<span>状态展示<\/span>/g, "");
  themeVerDiagram = themeVerDiagram.replace(/<span>无需办理<\/span>/g, "");
}

function initThemeFlowChart(cells) {
  graph.clear();
  if (errorMsg || errorMsg != '') {
    swal('提示信息', errorMsg, 'info');
    return;
  }
  var startDate = new Date();
  console.log('start handle time:' + startDate);
  if (themeVerDiagram) {
    // statusList = '[ { "stageId": "0bd5329d-a8a4-4148-9abd-a06e071c7dee", "statusValue": "UN_FINISHED", "statusName": "未办", "diagramItemList": [] }, { "stageId": "be2e337e-1164-42a6-bb3b-af8d1df6d898", "statusValue": "FINISHED", "statusName": "未办", "diagramItemList": [ { "itemVerId": "d32facb4-dc91-4193-baa2-42a4d92432da", "itemId": "0a854cf6-1314-4d10-92db-6c67a3d01d97", "statusValue": "NEED_NOT_HANDLE", "statusName": "无需办理" }, { "itemVerId": "9afda944-f16e-4d81-85a5-67308ce5178a", "itemId": "ef804cd6-2603-4ff6-a56d-e1381aa94109", "statusValue": "finished", "statusName": "办结" }, { "itemVerId": "1162463c-0c17-4d5e-9ce8-850c30afc869", "itemId": "4ea04a71-afe9-431f-877c-70b7dc8770ca", "statusValue": "NOT_PASS", "statusName": "不通过" }, { "itemVerId": "99b3a9ae-161b-46f6-9f3d-b3ceeb0d5aa8", "itemId": "148ece42-c816-4882-a414-ef483d480f2a", "statusValue": "TOLERENCE_PASS", "statusName": "容缺通过" }, { "itemVerId": "90dd8260-4e5f-465e-a28f-4e2cdbe4f050", "itemId": "90dd8260-4e5f-465e-a28f-4e2cdbe4f050", "statusValue": "CORRECT_MATERIAL_START", "statusName": "补正(开始)" }, { "itemVerId": "90dd8260-4e5f-465e-a28f-4e2cdbe4f050", "itemId": "90dd8260-4e5f-465e-a28f-4e2cdbe4f050", "statusValue": "SPECIFIC_PROC_START", "statusName": "特别程序(开始)" } ] }, { "stageId": "c89c452a-961c-4d4e-a1a5-0cc49fb02f52", "statusValue": "normal", "statusName": "未办", "diagramItemList": [ { "itemVerId": "6317a830-09c2-407f-927c-b7a52643978b", "itemId": "4b1d9375-99c2-4d38-880c-296f1bd35eb6", "statusValue": "NEED_NOT_HANDLE", "statusName": "无需办理" }, { "itemVerId": "193c6b15-b11b-4dad-a3a0-43ccde2c7523", "itemId": "4a95ea3f-1516-4432-bb5c-c2c2f9f32ee7", "statusValue": "finished", "statusName": "办结" }, { "itemVerId": "2d2289b6-dca0-4d07-b406-2e27f0337b0c", "itemId": "7bf4450d-9858-45f4-a204-3d70bb96b6f7", "statusValue": "NOT_PASS", "statusName": "不通过" }, { "itemVerId": "fef6f8ff-dd28-4a4a-9c5e-25e59873949d", "itemId": "94a8bf5f-4b0d-4d81-a941-c585b7324c87", "statusValue": "TOLERENCE_PASS", "statusName": "容缺通过" }, { "itemVerId": "6b8792ae-6454-449e-b091-70f0ca548406", "itemId": "468cd031-c730-493f-a4e3-1f1a7f915ad8", "statusValue": "NEED_NOT_HANDLE", "statusName": "无需办理" }, { "itemVerId": "a8040f80-abeb-472a-bfa0-c7bcb81d5a4f", "itemId": "239d4b22-aa0f-4d62-9dca-8f334e726286", "statusValue": "NORMAL_ACCEPT", "statusName": "正常受理" }, { "itemVerId": "603fec03-50d8-4dc1-b362-7be71c58f949", "itemId": "9ba1ab49-3917-46eb-a51d-f975b451aa82", "statusValue": "CORRECT_MATERIAL_START", "statusName": "补正(开始)" }, { "itemVerId": "feca331b-716e-47fb-8c5d-531d87ed259e", "itemId": "210c1852-903a-490f-a738-6e2ff9b89d71", "statusValue": "CORRECT_MATERIAL_END", "statusName": "补正(结束)" }, { "itemVerId": "95f4244a-0ba8-49aa-807f-617d448003dd", "itemId": "e2608245-2292-401c-9fdd-cd2d9960f10f", "statusValue": "SPECIFIC_PROC_START", "statusName": "特别程序(开始)" }, { "itemVerId": "32ece91c-fb6d-4fe8-8b1d-8b888587ce64", "itemId": "380b7109-5c39-45c7-b38c-0c453922e9bb", "statusValue": "SPECIFIC_PROC_END", "statusName": "特别程序(结束)" }, { "itemVerId": "e444b9ac-dbaa-48a8-9172-0d29e6018337", "itemId": "6ea3d5f6-f77c-4357-a598-a971e7103a51", "statusValue": "HANDLING", "statusName": "办理中" }, { "itemVerId": "58806307-19ed-48d0-b42d-bd240dbb576f", "itemId": "4dace7d6-7b86-41d0-8327-95fda6c457ea", "statusValue": "TOLERENCE_PASS", "statusName": "容缺通过" }, { "itemVerId": "a4c2e67e-c35a-4a29-82bf-f672549815b8", "itemId": "472f1b34-f48f-4b62-b440-8f3875d298a4", "statusValue": "HANDLING", "statusName": "办理中" }, { "itemVerId": "b313d6ba-f24f-4c58-83e2-15a578a649f6", "itemId": "47292521-c8e1-4a15-a114-1c66cdb2ad02", "statusValue": "finished", "statusName": "办结" }, { "itemVerId": "93d091da-be45-4159-b347-026767cecf38", "itemId": "fe9f70b1-4a55-40a5-a7ed-b87e833d7476", "statusValue": "NEED_NOT_HANDLE", "statusName": "无需办理" }, { "itemVerId": "2fecbf18-6f53-4a48-ace0-25bf2799c456", "itemId": "7b77fa2c-bdfe-4c4a-9891-1b3811aa9d01", "statusValue": "NORMAL_ACCEPT", "statusName": "正常受理" }, { "itemVerId": "51779b20-4f39-4b64-ad7d-040172628d0b", "itemId": "b5e25920-72c8-47e0-b0d9-a81f79ff4365", "statusValue": "CORRECT_MATERIAL_START", "statusName": "补正(开始)" }, { "itemVerId": "f33595fc-4896-449d-aec7-ac8a4003d052", "itemId": "72ac804c-948d-4834-a2d1-2a474a611fbe", "statusValue": "NEED_NOT_HANDLE", "statusName": "无需办理" }, { "itemVerId": "dc8b9de7-6029-42c5-84f6-5df1f462656c", "itemId": "62795bfd-80dc-4152-aa4a-e11dfe6238e6", "statusValue": "NEED_NOT_HANDLE", "statusName": "无需办理" }, { "itemVerId": "aea5c8d5-877d-468d-84db-a4bcef3dc3e0", "itemId": "9dd0b61b-8d06-47b1-a25d-49fd158a7f87", "statusValue": "HANDLING", "statusName": "办理中" }, { "itemVerId": "ed8682d1-796c-48f7-a9e5-08a685489966", "itemId": "04474818-afa4-49a6-bae0-284dafdf1879", "statusValue": "HANDLING", "statusName": "办理中" }, { "itemVerId": "9f1ce330-0e1b-4e37-b018-98e23ee919ca", "itemId": "c1cb7450-a5d7-4f95-ad83-010439c9588d", "statusValue": "HANDLING", "statusName": "办理中" }, { "itemVerId": "a1fcf507-e5e3-43cc-8283-31e6bc4f1f5e", "itemId": "c2702879-7f4c-472c-8b9a-64ca97549c1b", "statusValue": "CORRECT_MATERIAL_START", "statusName": "补正(开始)" }, { "itemVerId": "a726cf92-631a-4601-9963-dd9d0a40dd89", "itemId": "35856212-d948-4e07-bac9-f87905679da3", "statusValue": "HANDLING", "statusName": "办理中" }, { "itemVerId": "b8c94dc7-e8cd-4632-ae88-8f351c52ef98", "itemId": "f0fd268f-33f0-4097-9b3b-f07b8f466189", "statusValue": "HANDLING", "statusName": "办理中" }, { "itemVerId": "62f74edd-4ccf-432f-9d88-6255d354d5d1", "itemId": "e9a023bc-c134-48cd-b7af-bb298803ba3f", "statusValue": "HANDLING", "statusName": "办理中" } ] } ]';
    //加载前修改阶段的辅线信息
    handleAssistStage();
    /* if(statusList){
         statusList = JSON.parse(statusList);
         var json = JSON.parse(themeVerDiagram);
         //处理并行装填
         changeStatus(statusList, json);

     }*/
    graph.fromJSON(JSON.parse(themeVerDiagram));
    globalSetStageTitleDay();
    //加载后渲染
    if (statusList) {
      setViewByProjResult(JSON.parse(statusList), projInfoId, cells);
      // setStageStatusWord(statusList);
      // setEleIconAndPosition();
    }
    var endDate = new Date();
    console.log('start handle time:' + endDate + ', handle seconds:' + (endDate - startDate) / 1000);
    return true;
  }
}

function setStageStatusWord(statusList) {
  var k = 1;
  for (var i in statusList) {
    var list = statusList[i].diagramItemList;
    /*if(list){
        for(var j in list){
            // var div = $('div[gid='+list[j].itemId+']');
            // if(div.length > 0){
                // div.find('span:eq(1)').css('background-color',div.attr('col'));
                changeEleIconAndPosition(list[j]);
                j++;
            // }
        }
    }*/

    //控制阶段的颜色和图标
    var color = '',
      src = '';
    var $pool = $('g[model-id=' + statusList[i].stageId + ']');
    if (statusList[i].statusValue == 'HANDING' || statusList[i].statusValue == 'UN_FINISHED') { //正在处理
      color = gBlue, src = ctx + '/rappid/apps/BPMNEditor/images/pen.png';
    } else if (statusList[i].statusValue == 'FINISHED') { //完成
      color = gGreen, src = ctx + '/rappid/apps/BPMNEditor/images/tick.png';
    } else { //未开始
      color = gGraySlow, src = null; //src = ctx+'/rappid/apps/BPMNEditor/images/unfinesh.png';
    }

    if (color != '') {
      var $text = $pool.find('text:eq(1)');
      $text.find('tspan').attr('fill', '#fff');
      $pool.find('rect.header').attr('fill', color);
      if ($text.find('tspan:eq(0)').length > 0) {
        var len = $text.text().length * parseInt($text.find('tspan:eq(0)').css('font-size').replace('px', ''));
        var svgimg = document.createElementNS('http://www.w3.org/2000/svg', 'image');
        svgimg.setAttributeNS(null, 'height', '36');
        svgimg.setAttributeNS(null, 'width', '36');
        if (src) {
          svgimg.setAttributeNS('http://www.w3.org/1999/xlink', 'href', src);
        }
        svgimg.setAttributeNS(null, 'x', parseInt($pool.find('svg').attr('width')) / 2 - 12);
        svgimg.setAttributeNS(null, 'y', '46');
        svgimg.setAttributeNS(null, 'visibility', 'visible');
        $pool.find('text:eq(1)').after(svgimg);

        //箭头图标弃用
        /* var slast = document.createElementNS('http://www.w3.org/2000/svg','image');
         slast.setAttributeNS(null,'height','20');
         slast.setAttributeNS(null,'width','20');
         slast.setAttributeNS('http://www.w3.org/1999/xlink','href', src);
         slast.setAttributeNS(null,'x',(parseInt($pool.find('svg').attr('width'))+len)/2+5);
         slast.setAttributeNS(null,'y','22');
         slast.setAttributeNS(null, 'visibility', 'visible');
         slast.setAttributeNS('http://www.w3.org/1999/xlink','href', ctx+'/rappid/apps/BPMNEditor/images/arrow.png');
         slast.setAttributeNS(null,'x',($pool.find('svg').attr('width'))-30);
         $pool.find('text:eq(1)').after(slast);*/

        //阶段文字长度


      }
    }
    k++;

  }
}


function setEleIconAndPosition() {
  var cells = JSON.parse(themeVerDiagram).cells;
  $.each(cells, function (ind, ele) {
    if (ele.type == 'bpmn.Activity') {
      var $model = $('g[model-id=' + ele.id + ']');
      var png = $model.find('body div.content').attr('png');
      var $cell = $model.find('image');
      if ($cell.length > 0 && png && png != '') {
        var t = $model.find('div').width();
        var e = $model.find('div').height();
        var w = $cell.width();
        var h = $cell.height();
        //ctx+'/rappid/apps/BPMNEditor/images/';
        png = ctx + '/rappid/apps/BPMNEditor/images/' + png;
        $cell.attr('xlink:href', png);
        $cell.attr('transform', 'matrix(1,0,0,1,' + ((t - w) < 0 ? t : (t - w + 5)) + ',' + ((e - h) < 0 ? 0 : (e - h + 1)) + ')');
      }
    }
  });
}
//隐藏辅线事项，
// 1.先隐藏并记录隐藏事项的高度和，
// 2.记录辅线隐藏的最低高度,
// 3.设置阶段高度，设置辅线其实位置
function handleAssistStage() {
  var allSubY = 0; //隐藏的最低高度
  var json = JSON.parse(themeVerDiagram);
  var eleStageInfo = {};
  //分类阶段
  $.each(json.cells, function (ind, ele) {
    if (ele.type.indexOf('bpmn.HPool') >= 0) {
      allSubY = ele.position.y + ele.size.height;
      eleStageInfo[ele.id] = {};
      eleStageInfo[ele.id].id = ele.id;
      eleStageInfo[ele.id].hideAssistId = ''; //隐藏辅线id
      eleStageInfo[ele.id].showAssistId = ''; //显示辅线id
      eleStageInfo[ele.id].maxY = 0; //阶段最高元素y
      eleStageInfo[ele.id].subHeight = 0; //阶段调整高度
      eleStageInfo[ele.id].newMaxY = 0; //调整后的最高元素y
      eleStageInfo[ele.id].lastStageItemY = 0; //阶段最高元素的y
      eleStageInfo[ele.id].type = ele.type; //未使用
      eleStageInfo[ele.id].assistNum = 0; //辅线计数
      eleStageInfo[ele.id].countHight = 0; //辅线累计调整高度
      eleStageInfo[ele.id].ind = parseInt(ele.type.substring(ele.type.length - 1, ele.type.length));
      eleStageInfo[ele.id].stageY = ele.position.y + ele.size.height; //阶段最高y
    }
  });
  //记录需要隐藏辅线的id //记录最高元素的y
  $.each(json.cells, function (ind, ele) {
    if (ele.type.indexOf('bpmn.SPool') >= 0 && ele.attrs.stage.isShowItem) {
      if (ele.attrs.stage.isShowItem == '0') {
        eleStageInfo[ele.parent].hideAssistId = eleStageInfo[ele.parent].hideAssistId + '#' + ele.id;
      } else {
        eleStageInfo[ele.parent].showAssistId = eleStageInfo[ele.parent].showAssistId + '#' + ele.id;
      }
    }
  });
  //隐藏所有辅线事项以及调整辅线阶段高度为0
  $.each(json.cells, function (ind, ele) {
    //隐藏辅线事项
    if (ele.type == 'bpmn.Activity') {
      for (var p in eleStageInfo) {
        if (eleStageInfo[p].hideAssistId.indexOf(ele.parent) >= 0) {
          json.cells[ind].size.height = 0;
        }
      }
    }
    //调整辅线阶段高度为0
    if (ele.type.indexOf('bpmn.SPool') >= 0) {
      eleStageInfo[ele.parent]['assistInfo'] = {};
      for (var p in eleStageInfo) {
        if (eleStageInfo[p] && eleStageInfo[p].hideAssistId.indexOf(ele.id) >= 0) {
          // eleStageInfo[p].subHeight = eleStageInfo[p].subHeight+ele.size.height;
          if (eleStageInfo[ele.parent]['assistNum'] == 0) {
            eleStageInfo[ele.parent][ele.id] = {};
            eleStageInfo[ele.parent][ele.id]['ajustHieght'] = 0;
            eleStageInfo[ele.parent].countHight = ele.size.height - 25;
          } else {
            if (eleStageInfo[ele.parent][ele.id] == undefined) {
              eleStageInfo[ele.parent][ele.id] = {}
            }
            eleStageInfo[ele.parent][ele.id]['ajustHieght'] = eleStageInfo[ele.parent].countHight;
            eleStageInfo[ele.parent].countHight = eleStageInfo[ele.parent].countHight + ele.size.height - 25;
            eleStageInfo[p].subHeight = eleStageInfo[ele.parent].countHight;
          }
          eleStageInfo[ele.parent].assistNum = eleStageInfo[ele.parent].assistNum + 1;
          json.cells[ind].size.height = 0;
        } else if (eleStageInfo[p] && eleStageInfo[p].showAssistId.indexOf(ele.id) >= 0) {
          if (eleStageInfo[ele.parent]['assistNum'] == 0) {
            eleStageInfo[ele.parent][ele.id] = {};
            eleStageInfo[ele.parent][ele.id]['ajustHieght'] = 0;
            eleStageInfo[ele.parent].countHight = 0;
          } else {
            if (eleStageInfo[ele.parent][ele.id] == undefined) {
              eleStageInfo[ele.parent][ele.id] = {}
            }
            eleStageInfo[ele.parent][ele.id]['ajustHieght'] = eleStageInfo[ele.parent].countHight;
            eleStageInfo[ele.parent].countHight = eleStageInfo[ele.parent].countHight + 0;
            eleStageInfo[p].subHeight = eleStageInfo[ele.parent].countHight;
          }
          eleStageInfo[ele.parent].assistNum = eleStageInfo[ele.parent].assistNum + 1;
        }
      }
    }
  });

  //记录需要隐藏辅线的id //记录最高元素的y
  $.each(json.cells, function (ind, ele) {
    if (ele.type == 'bpmn.Activity' || ele.type.indexOf('bpmn.SPool') >= 0) {
      if (eleStageInfo[ele.parent] && eleStageInfo[ele.parent].maxY < ele.position.y) {
        eleStageInfo[ele.parent].maxY = ele.position.y + ele.size.height;
      }
      if (ele.type == 'bpmn.Activity' && eleStageInfo[ele.parent] && (ele.size.height + ele.position.y) > eleStageInfo[ele.parent].lastStageItemY) {
        eleStageInfo[ele.parent].lastStageItemY = ele.size.height + ele.position.y;
      }
    }
  });


  var resizeY = 0; //并行调整位置resizey, 主线调整高度resizey
  //计算stageInfo
  for (var p in eleStageInfo) {
    eleStageInfo[p].newMaxY = eleStageInfo[p].maxY - eleStageInfo[p].subHeight;
  }

  //调整同一阶段多个辅线的位置、事项位置
  $.each(json.cells, function (ind, ele) {
    if (ele.type.indexOf('bpmn.SPool') >= 0 && eleStageInfo[ele.parent]) {
      if (eleStageInfo[ele.parent].newMaxY && eleStageInfo[ele.parent]) {
        json.cells[ind].position.y = json.cells[ind].position.y - eleStageInfo[ele.parent][ele.id]['ajustHieght'];

      }
    }
    if (ele.type == 'bpmn.Activity') {
      for (var p in eleStageInfo) {
        if (eleStageInfo[p].showAssistId.indexOf(ele.parent) >= 0) {
          if (eleStageInfo[p][ele.parent] && eleStageInfo[p][ele.parent]['ajustHieght']) {
            json.cells[ind].position.y = json.cells[ind].position.y - eleStageInfo[p][ele.parent]['ajustHieght'];
            json.cells[ind].hasResize = true;
          }
        }
      }
    }
  })

  var tempH = 0;
  for (var p in eleStageInfo) {
    if (tempH == 0) {
      tempH = eleStageInfo[p].stageY - eleStageInfo[p].newMaxY - 20;
      if (tempH <= 0) {
        return; //只要有一个不需要调高则所有不需要调高
      }
      resizeY = eleStageInfo[p].newMaxY;
      allSubY = tempH;
    } else {
      if ((eleStageInfo[p].stageY - eleStageInfo[p].newMaxY - 10) < tempH) {
        tempH = eleStageInfo[p].stageY - eleStageInfo[p].newMaxY - 20;
        if (tempH <= 0) {
          return; //只要有一个不需要调高则所有不需要调高
        }
        resizeY = eleStageInfo[p].newMaxY;
        allSubY = tempH;

      }
    }

    // if(eleStageInfo[p].newMaxY > resizeY && eleStageInfo[p].subHeight > 0){
    // }
  }

  allSubY = allSubY - 5; //添加底部事项与阶段底部间隔
  //移动所有阶段和事项
  $.each(json.cells, function (ind, ele) {
    if (ele.type.indexOf('bpmn.HPool') >= 0) {
      json.cells[ind].size.height = json.cells[ind].size.height - allSubY;
    }
    if (ele.type == 'bpmn.Pool') {
      json.cells[ind].position.y = json.cells[ind].position.y - allSubY;
    }
    if (ele.type == 'bpmn.Activity' && (ele.position.y + ele.size.height) > resizeY && !eleStageInfo[ele.parent]) {
      if (ele.hasResize == undefined) {
        json.cells[ind].position.y = json.cells[ind].position.y - allSubY;
      }
    }
  });
  themeVerDiagram = JSON.stringify(json);
}

function handleParallelStage(json) {
  var cells = json.cells;
  var parallelInfo = {};
  $.each(cells, function (ind, ele) {
    if (ele.type == 'bpmn.Pool') {
      if (ele.embeds && ele.embeds.length > 0) {
        var color = gGraySlow;
        var total = 0;
        $.each(ele.embeds, function (ind2, embed) {
          $.each(cells, function (ind3, ele2) {
            if (embed == ele2.id) {
              if (ele2.attrs['.content'] && ele2.attrs['.content']['col']) {
                if (ele2.attrs['.content']['col'] == gBlue || ele2.attrs['.content']['col'] == gRed) {
                  color = gBlue;
                  return false;
                }
                if (ele2.attrs['.content']['col'] == gGreen) {
                  total++;
                }
              }
            }
          })
        });
        if (color == gGray && total == ele.embeds.length) {
          color = gGreen;
        }
        changeStageColorIfPass(ele.id, json, '', color);
      }
    }
  })
}


function changeViewItemProp(itemId, itemIds, fontColor, backColor, borderColor, icon, stageId, projId, cells) {
  var $div = null;
  for (var i in cells) {
    if (cells[i].type == 'bpmn.Activity') {
      if (itemIds.indexOf(cells[i].attrs.item.itemVerId.split('*')[0]) >= 0) {
        $div = $('g[model-id=' + cells[i].id + '] div.content');
        break;
      }
    }
  }
  if ($div) {
    if ($div.length > 1) {
      var div1 = findViewEleBy(itemId, stageId);
      if (div1) {
        $div = div1;
      }
      $div = $($div[0]);
    }
    var $image = $div.parent().parent().parent().find('image');
    var $model = $image.parent();
    if (borderColor) {
      $div.css('border-color', borderColor);
    }
    /*if(fontColor){
        $div.css('color', fontColor);
    }*/
    if (backColor) {
      $div.css('background-color', backColor);
    }
    $div.prop('proj', projId);
    setImageIconAndPosition($image, $model, icon);
  }
}

function changeViewStageProp(stageId, statusValue) {
  //控制阶段的颜色和图标
  var color = '',
    src = '';
  var $pool = $('g[model-id=' + stageId + ']');
  if (statusValue == 'HANDING' || statusValue == 'UN_FINISHED') { //正在处理
    color = gBlue, src = ctx + '/rappid/apps/BPMNEditor/images/pen.png';
  } else if (statusValue == 'FINISHED') { //完成
    color = gGreen, src = ctx + '/rappid/apps/BPMNEditor/images/tick.png';
  } else { //未开始
    color = gGraySlow, src = null; //src = ctx+'/rappid/apps/BPMNEditor/images/unfinesh.png';
  }

  if (color != '') {
    var $text = $pool.find('text:eq(1)');
    $text.find('tspan').attr('fill', '#fff');
    $pool.find('rect.header').attr('fill', color);
    if ($pool.find('image').length > 0) {
      $pool.find('image').attr('xlink:href', src);
    } else {
      if ($text.find('tspan:eq(0)').length > 0) {
        var len = $text.text().length * parseInt($text.find('tspan:eq(0)').css('font-size').replace('px', ''));
        var svgimg = document.createElementNS('http://www.w3.org/2000/svg', 'image');
        svgimg.setAttributeNS(null, 'height', '36');
        svgimg.setAttributeNS(null, 'width', '36');
        if (src) {
          svgimg.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', src);
        }
        svgimg.setAttributeNS(null, 'x', parseInt($pool.find('svg').attr('width')) / 2 - 12);
        svgimg.setAttributeNS(null, 'y', '46');
        svgimg.setAttributeNS(null, 'visibility', 'visible');
        $pool.find('text:eq(1)').after(svgimg);
      }
    }
  }
}

function clearStageAndItem() {
  var cells = JSON.parse(themeVerDiagram).cells;
  $.each(cells, function (ind, ele) {
    if (ele.type.indexOf('Pool') >= 0) {
      var $pool = $('g[model-id=' + ele.id + ']');
      $pool.find('rect.header').attr('fill', gGraySlow);
      $pool.find('rect.body').attr('stroke', gGraySlow);
      $pool.find('image').attr('xlink:href', '');
    }
  });
  $('.fobj.actFobj div.content').css('border-color', gGraySlow);
  $('.fobj.actFobj div.content').parent().parent().parent().find('image').attr('xlink:href', '');

}

function setImageIconAndPosition($image, $model, png) {
  if ($image.length > 0 && png && png != '') {
    // var t = $model.find('div').width();
    // var e = $model.find('div').height();
    // var w = $image.width();
    // var h = $image.height();
    var t = ($model.find('div'))[0].offsetWidth;
    var e = ($model.find('div'))[0].offsetHeight;
    var w = Number($image.attr('width').replace("px", ""));
    var h = Number($image.attr('height').replace("px", ""));
    png = ctx + '/rappid/apps/BPMNEditor/images/' + png;
    $image.attr('xlink:href', png);
    // $image.attr('transform', 'matrix(1,0,0,1,' + ((t - w) < 0 ? t : (t - w -5 )) + ',' + ((e - h) < 0 ? 0 : (e - h + 1)) + ')');
    $image.attr('transform', 'matrix(1,0,0,1,' + ((t - w) < 0 ? t : (t - w -5 )) + ',' + ((e - h) < 0 ? 0 : (e - h - 1)) + ')');
  }
}

function toTree(data) {
  var result = []
  if (!Array.isArray(data)) {
    return result
  }
  data.forEach(function (item) {
    delete item.children;
  });
  var map = {};
  data.forEach(function (item) {
    map[item.id] = item;
  });
  data.forEach(function (item) {
    var parent = map[item.pId];
    if (parent) {
      (parent.children || (parent.children = [])).push(item);
    } else {
      result.push(item);
    }
  });
  return result;
}

function findViewEleBy(eleId, stageId) {
  var $div = $('.joint-viewport foreignObject body div.content[item-id*=' + eleId + ']');
  var $model = $($div).parent().parent().parent().parent();
  var cell = getElementByEleId($model.attr('model-id'));
  if (cell && cell.attributes && cell.attributes.parent == stageId) {
    return $('g[model-id=' + cell.id + '] div.content');
  }
  return null;
}

function setViewByProjResult(statusList, projId, cells) {
  if (statusList.length > 0) {
    $.each(statusList, function (ind, ele) {
      changeViewStageProp(ele.stageId, ele.statusValue);
      if (ele.diagramItemList && ele.diagramItemList.length > 0) {
        $.each(ele.diagramItemList, function (ind1, ele1) {
          var color, fontcolor, png;
          var type = ele1.statusValue;
          switch (type) {
            case 'FINISHED': //办理通过
              png = 'finished.png', color = gGreen, fontcolor = gWhile;
              break;
            case 'NOT_PASS': //不通过
              png = 'not_pass.png', color = gRed, fontcolor = gWhile;
              break;
            case 'TOLERENCE_PASS': //容缺通过
              png = 'tolerence_pass.png', color = gGreen, fontcolor = gWhile;
              break;
            case 'UN_FINISHED': //未办
              png = 'un_finished.png', color = gWhile, fontcolor = gGray;
              break;
            case 'CORRECT_MATERIAL_START': //补正开始
              png = 'correct_material_start.png', color = gBlue, fontcolor = gWhile;
              break;
            case 'CORRECT_MATERIAL_END': //补正结束
              png = 'correct_material_end.png', color = gBlue, fontcolor = gWhile;
              break;
            case 'SPECIFIC_PROC_START': //特程开始
              png = 'specific_proc_start.png', color = gBlue, fontcolor = gWhile;
              break;
            case 'SPECIFIC_PROC_END': //特程结束
              png = 'specific_proc_end.png', color = gBlue, fontcolor = gWhile;
              break;
            case 'NORMAL_ACCEPT': //正常受理
              png = '', color = gBlue, fontcolor = gWhile;
              break;
            case 'HANDLING': //正常受理
              png = '', color = gBlue, fontcolor = gWhile;
              break;
            case 'NEED_NOT_HANDLE': //不用办
              png = 'need_not_handle.png', color = gWhile, fontcolor = gGray;
              break;
            case 'NEED_HANDLE_BUT_UNDO': //要办未办
              png = '', color = gWhile, fontcolor = gGray;
              break;
            default: //未知
              png = 'unknown.png', color = gWhile, fontcolor = gGray;
          }
          // setEleJsonAttr(itemIds, json, color,fontcolor, '特别程序', type, png);
          changeViewItemProp(ele1.itemId, ele1.itemIds, fontcolor, null, color, png, ele.stageId, projId, cells);
        });
      }
    });
  }
}

function reqStatusListAndResetThemeverDiagram(node, nodes, proj, cells) {
  clearStageAndItem();
  if (proj[node.id]) {
    nodes.forEach(function (ele) {
      setViewByProjResult(proj[ele.id], ele.id, cells);
    });
  } else {
    $("#uploadProgress").modal("show");
    $('#uploadProgressMsg').html("正在处理清稍后...");
    $.ajax({
      url: ctx + '/rest/project/diagram/status/json',
      method: 'GET',
      data: {
        projInfoId: node.id
      },
      success: function (res) {
        if (res.success) {
          proj[node.id] = res.content;
          nodes.forEach(function (ele) {
            setViewByProjResult(proj[ele.id], ele.id, cells);
          })
        } else {
          if (res.message) {
            swal('提示信息', res.message, 'info');
          } else {
            swal('提示信息', '请联系管理员！', 'info');
          }
        }
        setTimeout("$('#uploadProgress').modal('hide');", 400);
      },
      error: function (res) {
        setTimeout("$('#uploadProgress').modal('hide');", 400);
      }
    });
  }
}