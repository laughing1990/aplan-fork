$(function(){
    // reviewToolBox();
    initThemeFlowChart();
    setContentCenter();

    paper.on('element:pointerdblclick', function (a,b,c){
        var type = a.$el.attr('data-type');
        if(type == 'bpmn.Activity'){
            $('#westPanel').show(),$('#westPanel').parent().show();
            $('#westPanel').data('modelId', a.model.id);
            bindOptionItemAndStageId(a.model);
            if(a.model.attributes.attrs.item){
                var leftNode = selectItemTree.getNodeByParam('itemId', a.model.attributes.attrs.item.itemVerId.split('*')[0]);
                selectItemTree.checkNode(leftNode, true, true, true);
            }
        }else if(type.indexOf( 'bpmn.HPool')>=0 || type==( 'bpmn.SPool')){
            $('#stageModel').modal('show');
            $('#stageModel').data('modelId', a.model.id);
            $('#stageModel').find('input[name=auEleName]').val(a.model.attributes.lanes.label);
            setStageEleVal(a.model, $('#stageModel'), type);
        }else if(type.indexOf('bpmn.Text')>=0 || type == 'bpmn.Group'){
            $('#commonModal').modal('show');
            $('#commonModal').data('modelId', a.model.id);
            $('#commonModal').find('input[name=auEleName]').val(a.model.attr('.content/html'));
            setTextModelVal(a.model);
        }else if(type == 'bpmn.Pool'){
            updateParallelModal();
            $('#parallelModal').modal('show');
            $('#parallelModal').data('modelId', a.model.id);
            $('#parallelModal').find('input[name=auEleName]').val(a.model.attributes.lanes.label);
            setParallelVal(a.model, $('#parallelModal'));
        }
    });
    $('.modal').draggable();
    $(document).on('dblclick', 'input[data-attribute="attrs/item/itemVerId"]', function(a, b){
        $('#westPanel').toggle();
    });
    $('#saveStageAttrBtn').on('click', function(a, b){
        saveStageAttrBtn();
    })
    $('#commonModal input[name=auEleName],#commonModal input[name=auEleFontSize]').on('input', function(a, b){
        saveCommonAttrBtn($(this));
    })
    $('#commonModal input').on('change', function(a, b){
        saveCommonAttrBtn($(this));
    })
    $('#range_01').on('input', function(){
        var $this = $(this);
        var rad = $this.val()/$this.attr('max');
        $('.range_tip').css('left', parseInt(rad*$('#range_01').width()+17)).text($this.val());
    })
    $('#saveParallelAttrBtn').on('click', function(a, b){
        saveParallelAttrBtn();
    });
    $('#commonModal').on('shown.bs.modal',function(e){
        var $this = $('#range_01');
        var rad = $this.val()/$this.attr('max');
        $('.range_tip').css('left', parseInt(rad*$('#range_01').width()+17)).text($this.val());
    })
    //实施选项单选
    $(document).on('click','#rightExeItem input[type=checkbox]', function(a, b){
        $('#right')
    })
    $('.toolDelete').on('click', function(a,b){
        graph.clear();
    })
    $('.toolSave').on('click', function(a,b){
        toolbarCommands.saveGraph();
    })
    $('.toolPanel, .toolBtn').on('click', function(a,b){
        var node = $('.toolPanel');
        var nodeWidth = $('.toolPanel').width();
        if(nodeWidth < 150){　　//如果node是隐藏的则显示node元素，否则隐藏
            node.css('width','200px');
            $('.toolMain').css('background', '#1C9AFD url(/aplanmis-front/rappid/apps/BPMNEditor/images/set_blue.png) no-repeat center center ');
        }else{
            node.css('width','60px');
            $('.toolMain').css('background', '#1C9AFD url(/aplanmis-front/rappid/apps/BPMNEditor/images/set_white.png) no-repeat center center');
        }
    })
})

//设置事项类型和实际parentId
function bindOptionItemAndStageId(model){
    var parentId = model.attributes.parent;
    var ele = getElementByEleId(parentId);
    var type = ele.attributes.type;
    if(!model.attributes.attrs.item){
        model.attributes.attrs.item={};
    }
    if(type.indexOf('bpmn.HPool') >= 0||type == 'bpmn.SPool'){
        model.attributes.attrs.item.isOptionItem='0';
    }else if(type == 'bpmn.Pool'){
        model.attributes.attrs.item.isOptionItem='1';
    }
}

// 绑定事项
function bindItemNode(){

    //实施事项
   /* var ele = $('.rightExeItem div:eq(0)').find('input[type=radio]:checked');
    if(ele.length<1){
        alert('必须选择一个实施事项');
        return;
    }
    if(ele.length>1){
        alert('只能选择一个实施事项');
        return;
    }*/

    var objId = $('#westPanel').data('modelId');
    // $('g[model-id='+obj.id+']').find('body').text(nodes[0].name);
    var obj = getElementByEleId(objId);
    if(!obj.attributes.attrs.item){
        obj.attributes.attrs.item = {};
    }

    //事项类型
    // obj.attributes.attrs.item.isOptionItem =ele.val();


    //基本事项
    var nodes = selectItemTree.getCheckedNodes();
    if(nodes && nodes.length == 1){
        var itemName = nodes[0].name;
        if(itemName.indexOf('标准事项')>0){
            itemName = itemName.replace('【标准事项】','');
        }
        if(itemName.indexOf('实施事项')>0){
            itemName = itemName.replace('【实施事项】','');
        }
        var workDay = '【<span>'+nodes[0].dueNum+(nodes[0].bjType=='1'?'</span>个工作日':'</span>个自然日')+'】';
        obj.attr('.content/html', '<div><div>'+itemName+'</div><span >'+workDay+'</span></div>'),
        // obj.attributes.content=itemName;
        obj.attr('.content/title', itemName);
        obj.attr('.content/itemId', nodes[0].itemId);
        obj.attributes.auEleName = itemName;
        obj.attributes.attrs.item.itemVerId = nodes[0].itemId+'*'+nodes[0].itemVerId;
        obj.attributes.attrs.item.itemId = nodes[0].itemId;

        $('#westPanel').parent().hide(),$('#westPanel').hide();
    }else{
        swal('提示', '只能选择一个事项！', 'info');
    }
}

// 保存阶段值
function saveStageAttrBtn(){
    var $stageModel =  $('#stageModel');
    var objId = $stageModel.data('modelId');
    var name = $stageModel.find('input[name=auEleName]').val();
    obj = getElementByEleId(objId);
    var $gmodel = $('g[model-id='+objId+']');
    $gmodel.find('text>tspan').text(name);

    obj.attributes.lanes.label = obj.attributes.auEleName = name;
    var eles = $stageModel.find('form input[type]');
    $.each(eles, function(ind, ele){
        if($(ele).attr('type') != 'checkbox' || $(ele).attr('type') != 'radio'){
            if($(ele).attr('name')=='stageOther'){
                obj.attr($(ele).attr('data-field'), $(ele).prop('checked')?'1':'0');
            }else{
                obj.attr($(ele).attr('data-field'), $(ele).val());
            }
        }
    });
    // obj.attributes.attrs.stage['dueUnit'] = getCheckboxOrRadioValue($stageModel.find('input[name=dueUnit]'));
    var dueUnit = $stageModel.find('select[name=dueUnit]').val();
    var dueNum = $stageModel.find('input[name=dueNum]').val();
    obj.attributes.attrs.stage['dueUnit'] = dueUnit;
    obj.attributes.attrs.stage['dueNum'] = dueNum;
    obj.attributes.attrs.stage['isNode'] = getCheckboxOrRadioValue($stageModel.find('input[name=isNode]'));
    obj.attributes.attrs.stage['handWay'] = getCheckboxOrRadioValue($stageModel.find('input[name=handWay]'));
    obj.attributes.attrs.stage['lcbsxlx'] = getCheckboxOrRadioValue($stageModel.find('input[name=lcbsxlx]'));

    var values = getCheckboxOrRadioValue($stageModel.find('input[name=dybzspjdxh]'));
    obj.attributes.attrs.stage['dybzspjdxh'] = values.join(',');

    var workText = $stageModel.find('select[name=dueUnit] option:selected').text();
    var workday = '【'+dueNum+'个'+workText+'】';
    if($gmodel.find('text').length > 2){
        $gmodel.find('text:eq(2)').remove();
    }
    $gmodel.find('rect:eq(1)').attr('width')
    var newText = document.createElementNS('http://www.w3.org/2000/svg',"text");
    // newText.setAttributeNS(null,"transform",'matrix(1,0,0,1,'+(220-2-parseInt((Math.ceil((workday.length-workText.length-3)/2)+workText.length+3)/2)*12)+',22)');
    newText.setAttributeNS(null,"font-size","14");
    var textNode = document.createTextNode(workday);
    newText.appendChild(textNode);
    $gmodel.find('text.label').after(newText);
    $gmodel.find('text:eq(2)').attr('transform', 'matrix(1,0,0,1,'+($gmodel.find('rect:eq(1)').attr('width')-$gmodel.find('text:eq(2)').width())/2+',43)');
    obj.attributes.stageTitle=$gmodel.find('text:eq(2)').prop('outerHTML');
    //将双引号转为~`  两个字符，便于处理
    obj.attributes.stageTitle = obj.attributes.stageTitle.replace(/"/g,'~`');
    $stageModel.modal('hide');
}

// 保存并行阶段值
function saveParallelAttrBtn(){
    var $stageModel =  $('#parallelModal');
    var objId = $stageModel.data('modelId');
    var name = $stageModel.find('input[name=auEleName]').val();
    obj = getElementByEleId(objId);
    $('g[model-id='+objId+']').find('text>tspan').text(name);
    obj.attributes.lanes.label = obj.attributes.auEleName = name;
    if(!obj.attributes.parallel) obj.attributes.parallel={};
    obj.attributes.parallel = getCheckboxOrRadioValue($stageModel.find('input[name=parallel]'));
    // obj.attributes.parallel = obj.attributes.parallel.join(',');

    $stageModel.modal('hide');
}

// 保存公共窗口值
function saveCommonAttrBtn($this){
    if($this.attr('name') == 'backColor' || $this.attr('name') == 'borderColor'){
        $this.next('input').prop('checked', false);
    }

    var $commonModal =  $('#commonModal');
    var objId = $commonModal.data('modelId');
    var name = $('#commonModal input[name=auEleName]').val();
    var obj = getElementByEleId(objId);
    var type = obj.attributes.type;
    if( type.indexOf('bpmn.Text')>=0){
        obj.attr('.content/html', name);
        obj.attr('.label/text', name);
        obj.attributes.content=name;
        obj.attributes.lanes.label=name;
        var fontSize = $('#commonModal input[name=auEleFontSize]').val();
        $('g[model-id='+objId+']').find('body>div').css('font-size', fontSize); //ele.text(name); ele.css('font-size',  $('#commonModal input[name=auEleFontSize]').val());
        obj.attr('div/style/font-size', fontSize+'px');

        var $fontColor = $($('#commonModal input[name=fontColor]')[0]);

        var $backColor = $($('#commonModal input[name=backColor]')[0]);
        var backColor =  $backColor.val();
        if($backColor.next('input').is(':checked')){
            backColor = 'none';
        }

        $('g[model-id='+objId+']').find('body>div').css('color', $fontColor.val());
        obj.attr('div/style/color', $fontColor.val());
        if($backColor.val() != '#000000'){
            obj.attr($backColor.attr('data-field'), backColor);
        }

        var $borderColor = $($('#commonModal input[name=borderColor]')[0]);
        var borderColor = $borderColor.val();
        if($borderColor.next('input').is(':checked')){
            borderColor = 'rgba(0,0,0,0)';
        }
        obj.attr($borderColor.attr('data-field'), borderColor);
        // $('g[model-id='+objId+']').find('body>div').css('border', '1px solid '+ borderColor);
        // obj.attr('div/style/border','1px solid '+ borderColor);

        var $fontBold = $($('#commonModal input[name=fontBold]:checked')[0]);
        $('g[model-id='+objId+']').find('body>div').css('font-weight', $fontBold.val());
        obj.attr('div/style/font-weight', $fontBold.val());



       /* var $borderType = $('#commonModal input[name=borderType]:checked');
        if($borderType.length > 0){
            $borderType = $($borderType[0]);
            obj.attr($borderType.attr('data-field'), $borderType.val());
        }*/
        // $('g[model-id='+objId+']').find('body>div').css('font-size', fontSize);


        // model.attributes.content = name;  model.attributes.lanes.label =name;  model.attributes.attrs['.content'].html = name;
    }else if(type == 'bpmn.Group'){
        obj.attr('.label/text', name);
        // model.attributes.attrs[".label"].text=name;
        $('g[model-id='+objId+']').find('text>tspan').text(name);
    }
}

// 获取radio和checkbox选中值
function getCheckboxOrRadioValue(eles){
    var values = [];
    $.each(eles, function(ind, ele){
        if($(ele).prop('checked')){
            values.push(ele.value);
        }
    })
    if($(eles[0]).attr('type') == 'radio'){
        return values[0];
    }
    return values;
}


// 根据id获取元素
function getElementByEleId(id){
    var eles = graph.getElements();
    var obj = null;
    $.each(eles, function(ind, ele){
        if(ele.id == id){
            obj = ele;
            return;
        }
    })
    return obj;
}

// 设置阶段窗口值
function setStageEleVal(obj, $model, type){
    if(obj.attributes.attrs.stage){
        var stage = obj.attributes.attrs.stage;
        if(type.indexOf('HPool')>=0) stage.isNode = 1;
        if(type.indexOf('SPool')>=0) stage.isNode = 2;
        $model.find('input[name=auEleName]').val(obj.attributes.auEleName);
        $model.find('input[name=stageCode]').val(stage.stageCode);
        $model.find('input[name=dueNum]').val(stage.dueNum);
        $model.find('select[name=dueUnit]').val(stage.dueUnit);
        $model.find('input[name=isNode][value='+stage.isNode+']').prop('checked', true);
        $model.find('input[name=handWay][value='+stage.handWay+']').prop('checked', true);
        $model.find('input[name=lcbsxlx][value='+stage.lcbsxlx+']').prop('checked', true);

        $model.find('input[data-field="stage/isOptionItem"]').prop('checked', (stage.isOptionItem=='1'?true:false));
        $model.find('input[data-field="stage/isSelItem"]').prop('checked', (stage.isSelItem=='1'?true:false));
        $model.find('input[data-field="stage/isCheckItem"]').prop('checked', (stage.isFrontCheckItem=='1'?true:false));
        $model.find('input[data-field="stage/useOneForm"]').prop('checked', (stage.useOneForm=='1'?true:false));
        $model.find('input[data-field="stage/isCheckItemform"]').prop('checked', (stage.isCheckItemform=='1'?true:false));
        $model.find('input[data-field="stage/isCheckPartform"]').prop('checked', (stage.isCheckPartform=='1'?true:false));
        $model.find('input[data-field="stage/isCheckProj"]').prop('checked', (stage.isCheckProj=='1'?true:false));
        $model.find('input[data-field="stage/isCheckStage"]').prop('checked', (stage.isCheckStage=='1'?true:false));

        $('#stageModel').find('input[name=dybzspjdxh]').prop('checked', false);
        var dybgzsp = stage.dybzspjdxh.split(',');
        for(var i in dybgzsp){
            $model.find('input[name=dybzspjdxh][value='+dybgzsp[i]+']').prop('checked', true);
        }
    }
}

function setParallelVal(obj, $model){
    if(obj.attributes.parallel){
        var parallel = obj.attributes.parallel
        $model.find('input[name=auEleName]').val(obj.attributes.auEleName);
        $('#parallelModal').find('input[name=parallel]').prop('checked', false);
        for(var i in parallel){
            $model.find('input[name=parallel][value='+parallel[i]+']').prop('checked', true);
        }
    }
}

// 事项通过改变颜色
function changeEleColorIfPass(eleId, type, json){
    if(type == 'finished'){ // 办理通过
        setEleJsonAttr(eleId, json, '#00C161','#F6FBFA','办结通过', type);
    }else if( type == 'NOT_PASS'){  //办理不通过
        setEleJsonAttr(eleId, json, '#FF4B47','#F6FBFA', '不&nbsp;通&nbsp;过', type);
    }else if( type == 'TOLERENCE_PASS'){  //容缺通过
        setEleJsonAttr(eleId, json, '#00C161','#F6FBFA', '容缺通过', type);
    }else if( type == 'NEED_NOT_HANDLE'){  //不用办
        setEleJsonAttr(eleId, json, '#EBF0F3','#8E9CA3', '无需办理', type);
    }else if( type == 'NORMAL_ACCEPT'){  //正常受理
        setEleJsonAttr(eleId, json, '#008cf7','#F6FBFA', '正常受理', type);
    }else if( type == 'CORRECT_MATERIAL_START'){  //容缺受理-补正(开始)
        setEleJsonAttr(eleId, json, '#008cf7','#F6FBFA', '材料补正', type);
    }else if( type == 'CORRECT_MATERIAL_END'){  //容缺受理-补正(结束)
        setEleJsonAttr(eleId, json, '#008cf7','#F6FBFA', '材料补正', type);
    }else if( type == 'SPECIFIC_PROC_START'){  //容缺受理-特别程序(开始)
        setEleJsonAttr(eleId, json, '#008cf7','#F6FBFA', '特别程序', type);
    }else if( type == 'SPECIFIC_PROC_END'){  //容缺受理-特别程序(结束)
        setEleJsonAttr(eleId, json, '#008cf7','#F6FBFA', '特别程序', type);
    }else if( type == 'HANDLING'){  //办理中
        setEleJsonAttr(eleId, json, '#008cf7','#F6FBFA', '正常受理', type);
    }else{
        setEleJsonAttr(eleId, json, '#008cf7','#575962', '待&nbsp;&nbsp;办', type);

    }

}

function changeStageColorIfPass(eleId, json, status, color){
    for(var i in json.cells){
        if(json.cells[i].id == eleId){
            // json.cells[i]['attrs/.body/fill']=color;
            if(!json.cells[i].attrs['.body']){
                json.cells[i].attrs['.body']={};
            }
            if(!json.cells[i].attrs['.header']){
                json.cells[i].attrs['.header']={};
                json.cells[i].attrs['.header']['stroke']={};
            }
            json.cells[i].attrs['.body']['stroke']=color;
            json.cells[i].attrs['.header']['stroke']=color;
        }
    }
    if(status == 'UN_FINISHED'){

    }

}

function setEleJsonAttr(eleId, json, color, fontColor, statusName, type){
    for(var i in json.cells){
        if(json.cells[i].type == 'bpmn.Activity'){
            if( json.cells[i].attrs.item.itemVerId && json.cells[i].attrs.item.itemVerId.indexOf(eleId) >=0 ){
                // json.cells[i]['attrs/.body/fill']=color;
                if(json.cells[i].attrs['.content']){
                    json.cells[i].attrs['.content']['gid'] = eleId;
                    json.cells[i].attrs['.content']['col'] = color;
                    if(!json.cells[i].attrs['.content']['style']){
                        json.cells[i].attrs['.content']['style']={}
                    }
                    json.cells[i].attrs['.content']['style']['color']={};
                    if(!json.cells[i].attrs['.outer'] || !json.cells[i].attrs['.outer']['stroke']){
                        json.cells[i].attrs['.outer'] = {},json.cells[i].attrs['.outer']['stroke']={};
                    }
                    json.cells[i].attrs['.outer']['stroke']=color;
                }
                if(!json.cells[i].attrs['.body']){
                    json.cells[i].attrs['.body']={};
                }

                if(type == 'NEED_NOT_HANDLE'){ //无需办理
                    json.cells[i].attrs['.content']['style']['color']=fontColor;
                    json.cells[i].attrs['.body']['fill']=color;
                    json.cells[i].attrs['.outer']['stroke']=fontColor;
                }

                if(json.cells[i].attrs['.content']['html']){
                    json.cells[i].attrs['.content']['html'] = json.cells[i].attrs['.content']['html'].replace('状态展示',statusName);
                }
            }
        }
    }
}

function changeStatus(statusList, json){
    for(var i in statusList){
        if(statusList[i]){
            if( statusList[i].statusValue == 'FINISHED'){
                changeStageColorIfPass(statusList[i].stageId, json, statusList[i].statusValue, '#3DBB93');
            }else if(statusList[i].statusValue == 'UN_FINISHED'){

            }else{
                changeStageColorIfPass(statusList[i].stageId, json, statusList[i].statusValue, '#169AFF');
            }
        }
        var itemList = statusList[i].diagramItemList;
        if(itemList){
            for(var j in itemList){
                changeEleColorIfPass(itemList[j].itemId, itemList[j].statusValue,  json);
            }
        }
    }
    themeVerDiagram = JSON.stringify(json);
    themeVerDiagram = themeVerDiagram.replace(/<span>状态展示<\/span>/g,"");
    themeVerDiagram = themeVerDiagram.replace(/<span>无需办理<\/span>/g,"");
}

function initThemeFlowChart(){
        graph.clear();
        if(themeVerDiagram){
            console.log(themeVerDiagram);
            if(statusList){
                statusList = JSON.parse(statusList);
                var json = JSON.parse(themeVerDiagram);
                changeStatus(statusList, json);
            }
            graph.fromJSON(JSON.parse(themeVerDiagram));
            if(statusList){
                setStatusWord(statusList);
            }
            globalSetStageTitleDay();
            return true;
        }else{
            // swal('提示信息', '当前主题版本尚未设置流程图！', 'info');
            var a = { "type": "bpmn", "cells": [ ] };
            graph.fromJSON(a);
            return false;
        }
}
function setStatusWord(statusList){
    var k=1;
    for(var i in statusList){
        var list = statusList[i].diagramItemList;
        if(list){
            for(var j in list){
                var div = $('div[gid='+list[j].itemId+']');
                if(div.length > 0){
                    div.find('span:eq(1)').css('background-color',div.attr('col'));
                }
            }
        }

        var color='', src = '';
        console.log('1k',k);
        var $pool = $('g[model-id='+statusList[i].stageId+']');
        if(statusList[i].statusValue == 'UN_FINISHED'){
            color = '#000000', src = '/aplanmis-front/rappid/apps/BPMNEditor/images/unfinesh.png';
        }else if( statusList[i].statusValue == 'FINISHED'){
            color = '#00C161', src = '/aplanmis-front/rappid/apps/BPMNEditor/images/finesh.png';
        }else{
            color = '#169AFF', src = '/aplanmis-front/rappid/apps/BPMNEditor/images/working.png';
        }
        if(color != '' && src != ''){
            var $text = $pool.find('text:eq(1)');
            $text.find('tspan').attr('fill', color);
            if($text.find('tspan:eq(0)').length > 0){
                var len = $text.text().length * parseInt($text.find('tspan:eq(0)').css('font-size').replace('px',''));
                var svgimg = document.createElementNS('http://www.w3.org/2000/svg','image');
                svgimg.setAttributeNS(null,'height','20');
                svgimg.setAttributeNS(null,'width','20');
                svgimg.setAttributeNS('http://www.w3.org/1999/xlink','href', src);
                svgimg.setAttributeNS(null,'x',(parseInt($pool.find('svg').attr('width'))+len)/2+5);
                svgimg.setAttributeNS(null,'y','-5');
                svgimg.setAttributeNS(null, 'visibility', 'visible');
                $pool.find('text:eq(1)').after(svgimg);
            }
        }
        k++;

    }
}
function setTextModelVal(model){
    if(model){
        var $model = $('#g[model-id='+model.id+']');
        var attrs = model.attributes.attrs;
        var fontBold = attrs.div.style['font-weight'];
        if(!fontBold){
            fontBold = 'normal';
        }
        $('#commonModal').find('input[name=fontBold][value='+fontBold+']').prop('checked',true);
        $('#commonModal').find('input[name=auEleFontSize]').val(attrs.div.style['font-size']?attrs.div.style['font-size'].replace('px',''):13);
        $('#commonModal').find('input[name=fontColor]').val(attrs.div.style.color);

        $('#commonModal').find('input[name=borderColor]').next('input').prop('checked',false);
        if(attrs['.body'].stroke == 'rgba(0,0,0,0)'|| attrs['.body'].stroke == 'none'){
            $('#commonModal').find('input[name=borderColor]').next('input').prop('checked',true);
        }else{
            $('#commonModal').find('input[name=borderColor]').val(attrs['.body'].stroke);
        }

        $('#commonModal').find('input[name=backColor]').next('input').prop('checked',false);
        if(attrs['.body'].fill == 'none'){
            $('#commonModal').find('input[name=backColor]').next('input').prop('checked',true);
        }else{
            $('#commonModal').find('input[name=backColor]').val(attrs['.body'].fill);
        }

    }
}

function updateParallelModal(){
    var $div = $('#parallelStages');
    $div.empty();
    var stages = {};
    var cells = graph.toJSON().cells;
    cells.forEach(function(ele){
        if(ele.type.indexOf('bpmn.HPool') >= 0){
            stages[ele.id] = ele.lanes.label;
            $div.append('<input type="checkbox" name="parallel" data-field="stage/dybzspjdxh1" value="'+ele.id+'"/> '+ele.lanes.label+'</br>');
        }
    });
}


