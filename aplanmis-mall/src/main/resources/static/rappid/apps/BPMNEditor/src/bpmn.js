/*! Rappid v2.4.0 - HTML5 Diagramming Framework - TRIAL VERSION

Copyright (c) 2015 client IO

 2019-07-09 


This Source Code Form is subject to the terms of the Rappid Trial License
, v. 2.0. If a copy of the Rappid License was not distributed with this
file, You can obtain one at http://jointjs.com/license/rappid_v2.txt
 or from the Rappid archive as was distributed by client IO. See the LICENSE file.*/


joint.setTheme('bpmn');

/* GRAPH */

var example = window.example;
var gdAuth = window.gdAuth;
var gdLoad = window.gdLoad;
var gdSave = window.gdSave;
var inputs = window.inputs;
var toolbarConfig= window.toolbarConfig;

var graph = new joint.dia.Graph({ type: 'bpmn' });

var commandManager = new joint.dia.CommandManager({ graph: graph });

var keyboard = new joint.ui.Keyboard();
/* PAPER + SCROLLER */
var paper = new joint.dia.Paper({
    width: 3000,
    height: 3000,
    model: graph,
    gridSize: 5,
    drawGrid:drawGrid,
   /* viewport: function(view) {
        var model = view.model;
        var bbox = model.getBBox();
        if (model.isLink()) {
            // vertical/horizontal links have zero width/height
            // we need to manually inflate the bounding box
            bbox.inflate(1);
        }
        // Return true if there is an intersection
        // Return true if bbox is within viewportRect
        return viewportRect.intersect(bbox);
    },*/
    // preventDefaultBlankAction:drawGrid,
    /*guard:function(evt, view){
      console.log(evt);
      console.log(view);
    },*/
    interactive: drawGrid,
        /*{
        linkMove: false,
        labelMove: false,
        arrowheadMove: false,
        vertexMove: false,
        vertexAdd: false,
        vertexRemove: false,
        useLinkTools: false,
        elementMove: false,
        addLinkFromMagnet: false
    },*/ //can't drag
    // frozen: true,
    // origin: {x: 60, y: 40},
    // validateMagnet:drawGrid,
    // allowLink:drawGrid,
    position: {x: 60, y: 40},
    defaultLink: new joint.shapes.bpmn.Flow,
    validateConnection: function(cellViewS, magnetS, cellViewT, magnetT, end) {

        // don't allow loop links
        if (cellViewS == cellViewT) return false;

        var view = (end === 'target' ? cellViewT : cellViewS);

        // don't allow link to link connection
        return !view.model.isLink();
    },
    embeddingMode: true,
    frontParentOnly: false,
    defaultAnchor: { name: 'perpendicular' },
    defaultConnectionPoint: { name: 'boundary', args: { sticky: true, stroke: true }},
    validateEmbedding: function(childView, parentView) {
        var Pool = joint.shapes.bpmn.Pool, SPool = joint.shapes.bpmn.SPool;
        var HPool = joint.shapes.bpmn.HPool, HPool1 = joint.shapes.bpmn.HPool1, HPool2 = joint.shapes.bpmn.HPool2,HPool3 = joint.shapes.bpmn.HPool3, HPool4 = joint.shapes.bpmn.HPool4;
        var Group = joint.shapes.bpmn.Group;
        return ((parentView.model instanceof Pool) && !(childView.model instanceof Pool))
            || ((parentView.model instanceof SPool) && !(childView.model instanceof SPool))
            || ((parentView.model instanceof HPool) && !(childView.model instanceof HPool))
            || ((parentView.model instanceof HPool1) && !(childView.model instanceof HPool1))
            || ((parentView.model instanceof HPool2) && !(childView.model instanceof HPool2))
            || ((parentView.model instanceof HPool3) && !(childView.model instanceof HPool3))
            || ((parentView.model instanceof HPool4) && !(childView.model instanceof HPool4))
            || ((parentView.model instanceof Group) && !(childView.model instanceof Group));
    }
}).on({

    'blank:pointerdown': function(evt, x, y) {

        if (keyboard.isActive('shift', evt)) {
            selection.startSelecting(evt, x, y);
        } else {
            selection.cancelSelection();
            paperScroller.startPanning(evt, x, y);
        }
    },

    'element:pointerdown': function(cellView, evt) {

        // Select an element if CTRL/Meta key is pressed while the element is clicked.
        if (keyboard.isActive('ctrl meta', evt) && !(cellView.model instanceof joint.shapes.bpmn.Pool)) {
            selection.collection.add(cellView.model);
        }
    },

    'element:pointerup': openTools,
    'link:options': openTools
});

var paperScroller = new joint.ui.PaperScroller({
    autoResizePaper: true,
    // padding: 50,
    padding: 0,
    paper: paper
});

paperScroller.$el.appendTo('#paper-container');
paperScroller.center();

/* SELECTION */

var selection = new joint.ui.Selection({
    paper: paper,
    graph: graph,
    filter: ['bpmn.Pool'] // don't allow to select a pool
}).on({

    'selection-box:pointerdown': function(cellView, evt) {
        // Unselect an element if the CTRL/Meta key is pressed while a selected element is clicked.
        if (keyboard.isActive('ctrl meta', evt)) {
            selection.collection.remove(cellView.model);
        }
    }
});

/* STENCIL */

var stencil = new joint.ui.Stencil({
    graph: graph,
    paper: paper,
    dragEndClone: function(cell) {

        var clone = cell.clone();
        var type = clone.get('type');

        // some types of the elements need resizing after they are dropped
        var sizeMultiplier = { 'bpmn.Pool': 8, 'bpmn.Choreography': 2 }[type];
        var sizeMultiplierH = { 'bpmn.SPool': 8,'bpmn.HPool': 8,'bpmn.HPool1': 8,'bpmn.HPool2': 8,'bpmn.HPool3': 8,'bpmn.HPool4': 8, 'bpmn.Choreography': 2 }[type];

        if (sizeMultiplier) {
            var originalSize = clone.get('size');
            clone.set('size', {
                width: originalSize.width * sizeMultiplier,
                height: originalSize.height * sizeMultiplier
            });
        }
        if (sizeMultiplierH) {
            var originalSize = clone.get('size');
            clone.set('size', {
                width: originalSize.width * sizeMultiplierH,
                height: originalSize.height * sizeMultiplierH
            });
        }

        return clone;
    }
});

stencil.render().$el.appendTo('#stencil-container');

stencil.load([
    // new joint.shapes.bpmn.Gateway,
    new joint.shapes.bpmn.Activity({attrs:{'.label':'事项'}}),
    // new joint.shapes.bpmn.Event,
    // new joint.shapes.bpmn.Annotation,
    // a groups and pools can't be connected with any other elements
    new joint.shapes.bpmn.Pool({
        attrs: {
            '.': { magnet: false },
            '.header': { fill: '#B8C8DC' }
        },
        lanes: { label: '并行事项' }
    }),
    new joint.shapes.bpmn.HPool1({
        attrs: {
            '.': { magnet: false },
            '.header': { fill: '#B8C8DC' }
        },
        lanes: { label: '阶段一' }
    }),
    new joint.shapes.bpmn.HPool2({
        attrs: {
            '.': { magnet: false },
            '.header': { fill: '#B8C8DC' }

        },
        lanes: { label: '阶段二' }
    }),
    new joint.shapes.bpmn.HPool3({
        attrs: {
            '.': { magnet: false },
            '.header': { fill: '#B8C8DC' }
        },
        lanes: { label: '阶段三' }
    }),
    new joint.shapes.bpmn.HPool4({
        attrs: {
            '.': { magnet: false },
            '.header': { fill: '#B8C8DC' }
        },
        lanes: { label: '阶段四' }
    }),
    new joint.shapes.bpmn.SPool({
        attrs: {
            '.': { magnet: false },
            '.header': { fill: '#B8C8DC' }
        },
        lanes: { label: '辅线' }
    }),
    /*new joint.shapes.bpmn.Group({
        attrs: {
            '.': { magnet: false },
            '.label': { text: '辅线' }
        }
    }),*/
    // new joint.shapes.bpmn.Conversation,
    new joint.shapes.bpmn.Text({attrs:{'.label':'文本'},lanes: { label: '文本' }}),
    new joint.shapes.bpmn.TextD({attrs:{'.label':'文本'},lanes: { label: '文本' }}),
    new joint.shapes.bpmn.TextB({attrs:{'.label':'文本'},lanes: { label: '文本' }}),
    // new joint.shapes.bpmn.Choreography({
    //     participants: ['Participant 1', 'Participant 2']
    // }),
    // new joint.shapes.bpmn.Message,
    // new joint.shapes.bpmn.DataObject,

]);

joint.layout.GridLayout.layout(stencil.getGraph(), {
    columns: 70,
    columnWidth: 80,
    rowHeight: 90,
    dy: 20,
    dx: 20,
    resizeToFit: true
});

stencil.getPaper().fitToContent(0, 0, 10);

// Create tooltips for all the shapes in stencil.
stencil.getGraph().get('cells').each(function(cell) {
    new joint.ui.Tooltip({
        target: '.joint-stencil [model-id="' + cell.id + '"]',
        content: cell.get('type').split('.')[1],
        bottom: '.joint-stencil',
        direction: 'bottom',
        padding: 0
    });
});

/* CELL ADDED: after the view of the model was added into the paper */
graph.on('add', function(cell, collection, opt) {

    if (!opt.stencil) return;

    // open inspector after a new element dropped from stencil
    var view = paper.findViewByModel(cell);
    if (view) openTools(view);
});

/* KEYBOARD */

keyboard.on('delete backspace', function() {

    graph.removeCells(selection.collection.toArray());
});

function openTools(cellView) {

    var cell = cellView.model;
    var type = cell.get('type');

    window.inspector = joint.ui.Inspector.create('#inspector-container', {
        cell: cell,
        inputs: inputs[type],
        groups: {
            general: { label: type, index: 1 },
            appearance: { index: 2 }
        }
    });

    if (!cell.isLink() && !selection.collection.contains(cell)) {

        selection.collection.reset([]);
        // Add the cell into the selection collection silently
        // so no selection box is rendered above the cellview.
        selection.collection.add(cell, { silent: true });

        new joint.ui.FreeTransform({
            cellView: cellView,
            allowOrthogonalResize: drawGrid,
            allowRotation: false
        }).render();

        if(drawGrid){
            var halo = new joint.ui.Halo({
                cellView: cellView,
                theme: 'default',
                boxContent: function(cellView) {
                    return cellView.model.get('type');
                }
            });
            halo.render();
            halo.removeHandle('rotate');
            halo.removeHandle('resize');
        }
    }
}

function showStatus(message, type) {

    $('.status').removeClass('info error success').addClass(type).html(message);
    $('#statusbar-container').dequeue().addClass('active').delay(3000).queue(function() {
        $(this).removeClass('active');
    });
}

function setRealParent(ele, cells, parentIds){
    var parent = ele.parent;
    parent = getElementByEleId(parent);
    var parentId = [];
    if(parent && parent.attributes.type == 'bpmn.Pool'){
        var parallel = parent.attributes.parallel;
        // if(parallel){
        //     for(var i=0; i< parallel.length; i++){
        //         parentId[i] = parentIds[parseInt(parallel[i])];
        //     }
            ele.realParent = parallel;
        // }
    }
}



function saveParallel(){
    var result = false;
    var cells = graph.toJSON().cells;
    $.each(cells, function(ind, ele){
        if(ele.type == 'bpmn.Pool'){
            if(!ele.parallel){
                result = true;
                return;
            }
        }
    })
    return result;
}

function reviewToolBox(){
    var toolCells = $('.joint-stencil g.joint-cell');
    if(toolCells && toolCells.length > 0){
        $.each(toolCells, function(ind, ele){
            var $ele = $(ele);
            var type = $ele.attr('data-type');
            if(type){
                if(type.indexOf('bpmn.HPool') >= 0 || type == 'bpmn.Pool' || type == 'bpmn.SPool'){
                    $ele.find('rect.body').attr('width', 620);
                    $ele.find('rect.body').attr('height', 300);
                    $ele.find('rect.body').attr('stroke-width', 1.5);
                    $ele.find('rect.header').attr('height', 10);
                    $ele.find('rect.header').attr('width', 50);
                    $ele.find('rect.header').attr('fill', '#fff');
                    $ele.find('rect.header').attr('transform', 'matrix(1,0,0,1,0,-7)');
                    $ele.find('text.label').attr('transform', 'matrix(1,0,0,1,25,-7)');
                    if(type=='bpmn.Pool'|| type == 'bpmn.SPool' ){
                        $ele.find('text.label').attr('transform', 'matrix(1,0,0,1,25,-1)');
                    }
                }else if(type == 'bpmn.Group'){
                    $ele.find('rect.body').attr('width', 245);
                    $ele.find('rect.label-rect').attr('transform', 'matrix(1,0,0,1,25,-30)');
                    $ele.find('g.label-group').attr('transform', 'matrix(1,0,0,1,25,-30)');
                    $ele.find('g.rotatable').css('transform', 'matrix(0,-1,1,0,-165,6)');
                }else if(type == 'bpmn.Activity'){
                    $ele.find('g.rotatable').css('transform', 'matrix(0,-1,1,0,-15,7)');
                    $ele.find('div.content').css('width', '117%');
                    $ele.find('div.content').css('height', '100%');
                    $ele.find('div.content').text('事项');
                    $ele.find('rect.body').attr('width', 125);
                }else{
                    $ele.find('rect.body').attr('width', 125);
                }
            }
        })
    }
}

/* TOOLBAR */

var toolbar = new joint.ui.Toolbar({
    tools: toolbarConfig.tools,
    references: {
        paperScroller: paperScroller,
        commandManager: commandManager
    }
});

var toolbarCommands = {
    toJSON: function() {

        var windowFeatures = 'menubar=no,location=no,resizable=yes,scrollbars=yes,status=no';
        var windowName = _.uniqueId('json_output');
        var jsonWindow = window.open('', windowName, windowFeatures);

        jsonWindow.document.write(JSON.stringify(graph.toJSON()));
    },

    loadGraph: function() {

        gdAuth(function() {

            showStatus('loading..', 'info');
            gdLoad(function(name, content) {
                try {
                    var json = JSON.parse(content);
                    graph.fromJSON(json);
                    document.getElementById('fileName').value = name.replace(/.json$/, '');
                    showStatus('loaded.', 'success');
                } catch (e) {
                    showStatus('failed.', 'error');
                }
            });

        }, true);
    },

    generateGraph: function() {
          showStatus('saving..', 'info');
          var name = document.getElementById('fileName').value;
          gdSave(name, JSON.stringify(graph.toJSON()), function(file) {

              if (file) {
                  showStatus('saved.', 'success');
              } else {
                  showStatus('failed.', 'error');
              }
          });
    },
    saveGraph: function() {
        // var themeVerId = sessionStorage.getItem("themeVerId");
        if(!curIsEditable){
            swal('提示信息', '当前版本下数据不可保存!', 'info');
            return;
        }
        var json = graph.toJSON();
        if(JSON.stringify(json).indexOf('bpmn.HPool')<0){
            swal('提示信息', '当前未添加流程图，数据不可保存!', 'info');
            return;
        }
        if(saveParallel()){
            agcloud.ui.metronic.showSwal({type: 'info', message: '您有并行阶段属性未设置!'});
            return;
        }
        var hPoolParent = {};
        var sPoolParent = {};
        var isPass = true;
        $.each(json.cells, function(ind, ele){
           if(ele.type.indexOf('bpmn.HPool') >= 0){
               hPoolParent[parseInt(ele.type.substring(ele.type.length-1,ele.type.length))] = ele.id;
               sPoolParent[ele.id]=ele.type;
               if(!ele.attrs.stage){
                   isPass = false;
                   return;
               }
               ele.attrs.stage.isNode = '1';
           }
        });
        if(!isPass){
            swal('提示信息', '您有阶段属性未设置!', 'info');
            return;
        }
        var isSubordinateStageRight = true;
        $.each(json.cells, function(ind, ele){
            if(ele.type == 'bpmn.SPool' || ele.type == 'bpmn.Pool' || ele.type.indexOf('bpmn.HPool')>=0){
                ele.auEleName = ele.lanes.label;
            }
            if(ele.type == 'bpmn.Activity'){
                ele.auEleName = ele.content;
                setRealParent(ele, json.cells, hPoolParent);
            }
            if(ele.type == 'bpmn.SPool'){
                var parent = ele.parent;
                if(!ele.attrs.stage){
                    isPass = false;
                    return;
                }
                ele.attrs.stage.isNode = '2';
                parent = getElementByEleId(parent);
                if(!(parent && parent.attributes.type.indexOf('bpmn.HPool')>=0)){
                    isSubordinateStageRight = false;
                }
                ele.parentType = sPoolParent[ele.parent];
            }
        });
        if(!isPass){
            swal('提示信息', '您有辅线属性未设置!', 'info');
            return;
        }
        if(!isSubordinateStageRight){
            swal('提示信息', '辅线没有对应的主线！!', 'info');
            return;
        }
        $("#uploadProgress").modal("show");
        $('#uploadProgressMsg').html("保存流程图中,请勿点击,耐心等候...");
        $.ajax({
            type:'POST',
            async:true,
            url:'./saveThemeVerDiagram.do',
            data:{themeVerId:themeVerId,themeVerDiagram:JSON.stringify(json)},
            success:function(res){
                $("#uploadProgress").modal('hide');
                if(res.success){
                    swal({
                        // title: '提示信息',
                        text: '保存阶段流程成功！',
                        type: 'success',
                        timer: 1500,
                        showConfirmButton: false
                    });
                }else{

                    if(res.message){
                        swal('错误信息', res.message, 'error');
                    }else{
                        swal('错误信息', '服务器异常！', 'error');
                    }
                }
            },error:function(res){
                $("#uploadProgress").modal('hide');
                if(res.responseJSON.message){
                    swal('错误信息', res.responseJSON.message , 'error');
                }else{
                    swal('错误信息', '服务器异常！', 'error');

                }
            }
        })

        /*gdAuth(function() {

            showStatus('saving..', 'info');
            var name = document.getElementById('fileName').value;
            gdSave(name, JSON.stringify(graph.toJSON()), function(file) {

                if (file) {
                    showStatus('saved.', 'success');
                } else {
                    showStatus('failed.', 'error');
                }
            });

        }, true);*/
    }
};

toolbar.on({
    'tojson:pointerclick': toolbarCommands.toJSON,
    'load:pointerclick': toolbarCommands.generateGraph,
    'save:pointerclick': toolbarCommands.saveGraph,
    'clear:pointerclick': _.bind(graph.clear, graph),
    'print:pointerclick': _.bind(paper.print, paper),
    'image:pointerclick': toolbarCommands.generateGraph,
});

toolbar.render().$el.appendTo('#toolbar-container');

toolbar.$('[data-tooltip]').each(function() {

    new joint.ui.Tooltip({
        target: this,
        content: $(this).data('tooltip'),
        top: '.joint-toolbar',
        direction: 'top'
    });
});
reviewToolBox();
function globalSetStageTitleDay(){
    if(themeVerDiagram){
        var json = JSON.parse(themeVerDiagram);
        $.each(json.cells, function(ind, ele){
            if(ele.type.indexOf('bpmn.HPool') >= 0){
                if(ele.stageTitle){
                    ele.stageTitle = ele.stageTitle.replace(/~`/g, '\'');
                    var b = parseDom(ele.stageTitle);
                    if(!drawGrid){
                        $(b).attr('fill', '#fff');
                    }
                    $('g[model-id='+ele.id+'] ').find('text.label').after(b);
                }
            }
            if(ele.type.indexOf('bpmn.HPool') >= 0 || ele.type.indexOf('bpmn.SPool') >= 0 || ele.type == 'bpmn.HPool'){
                var $model = $('g[model-id='+ele.id+']');
                var width = $model.find('svg').attr('width');
                var text = $model.find('text.label tspan').text();
                text = textlength(text)*parseInt($model.find('text.label tspan').css('font-size').replace('px',''));
                if((text-width*2)>28){
                    // $model.find('text.label tspan').attr('dx', -5);
                    $model.find('text.label tspan').attr('textLength', width-10);
                    $model.find('text.label tspan').attr('lengthAdjust', 'spacingAndGlyphs');
                }
            }
        })
    }
}
function parseDom(nodelist) {
    var objE = document.createElementNS('http://www.w3.org/2000/svg','div');
    // newText.setAttributeNS(null,"fill","#ffffff");
    objE.innerHTML = nodelist;
    return objE.childNodes;
}
function textlength(str) {
    var len = 0;

    for (var i=0; i<str.length; i++) {
        var c = str.charCodeAt(i);
        //单字节加1
        if ((c >= 0x0001 && c <= 0x007e) || (0xff60<=c && c<=0xff9f)) {
            len++;
        }
        else {
            len+=2;
        }
    }
    return len;
};
function setContentCenter(){
    var area = paper.getContentArea();
    var x = 0;
    if(area.width > (document.body.offsetWidth+20)){
        x = (area.x+document.body.offsetWidth/2);
    }else{
        x = (area.x+area.width/2);
    }
    if(!drawGrid){
        if(area.height > (document.body.offsetHeight+20) || area.width > (document.body.offsetWidth+20)){
            var c = {
                min: .2,
                    max: 5,
                    step: .1
            };
            paperScroller.zoom(-0.2, {
                min: c.min,
                grid: c.step
            });
            paperScroller.center((x-(drawGrid?100:0))*1.1, ((area.y+document.body.offsetHeight/2)-(drawGrid?0:15))*1.1);
        }
    }else{
        paperScroller.center((x-(drawGrid?100:0)), ((area.y+document.body.offsetHeight/2)-(drawGrid?0:15)));
    }


}

