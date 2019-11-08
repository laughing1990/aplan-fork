var selectBscDicRegionTree;
//右边行政区划树 配置setting信息
var bscDicRegionTreeSetting = {

    edit: {
        enable: false, //设置 zTree 是否处于编辑状态
        showRemoveBtn: false,//设置是否显示删除按钮
        showRenameBtn: false//设置是否显示编辑名称按钮
    },
    data: {
        simpleData: {
            enable: true,
            idKey: "id",
            pIdKey: "pId",
            rootPId: 0
        }
    },
    view: {
        selectedMulti: false,//设置是否允许同时选中多个节点
        showTitle : true, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
        showLine: true, //设置 zTree 是否显示节点之间的连线。
        showHorizontal:false//设置是否水平平铺树（自定义属性）

    },
    //用于捕获节点被点击的事件回调函数
    callback: {
        //双击事件
        onDblClick: onDblClickRegionTreeNode

    }
};

$(function(){

    $('#bscDicRegionDiv').niceScroll({

        cursorcolor: "#e2e5ec",//#CC0071 光标颜色
        cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
        cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "4px", //像素光标的宽度
        cursorborder: "0", //   游标边框css定义
        cursorborderradius: "2px",//以像素为光标边界半径
        autohidemode: true  //是否隐藏滚动条
    });

});

/**
 * 打开行政区划树
 */
function selectBscDicRegionZtree(){

    $('#select_bsc_dic_region_ztree_modal').modal('show');
    $('#select_bsc_dic_region_ztree_modal_title').html('选择行政区划');
    $('#bscDicRegionDiv').animate({scrollTop: 0}, 800);//滚动到顶部
    showBscDicRegionTree();
    //初始化模糊搜索方法
    fuzzySearch('bscDicRegionTree','#bscDicRegionZtreeKeyWord',null,true);
}

/**
 * 关闭行政区划树
 */
function closeSelectBscDicRegionZtree() {

    $('#select_bsc_dic_region_ztree_modal').modal('hide');
    showBscDicRegionTree();
}

//加载选择行政区划树
function showBscDicRegionTree() {

    $.ajax({
        type: "get",
        url: ctx + '/aea/service/window/listSelfAndChildRegionByOrgId.do',
        async: false,
        data: {},
        success: function (data) {
            if (data && data.length > 0) {
                selectBscDicRegionTree = $.fn.zTree.init($("#bscDicRegionTree"), bscDicRegionTreeSetting, data);
            }
        }
    });
}

function onDblClickRegionTreeNode(event, treeId, treeNode) {

    $("#selectRegionBtn").trigger("click");
}

//折叠ztree树全部节点
function collapseBscDicRegionZtreeAllNode(treeId) {

    var treeObj = $.fn.zTree.getZTreeObj(treeId);
    treeObj.expandAll(false);
}

//展开异步ztree树全部节点
function expandBscDicRegionZtreeAllNode(treeId) {

    var zTree = $.fn.zTree.getZTreeObj(treeId);
    expandNodes(zTree.getNodes(),treeId);
}


//清空搜索左树文字框
function clearBscDicRegionZtreeForTree() {

    $("#bscDicRegionZtreeKeyWord").val("");
    showBscDicRegionTree();
}

//展开nods下全部子孙节点
function expandNodes(nodes,treeId) {

    if (!nodes) return;
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    for (var i=0, l=nodes.length; i<l; i++) {
        zTree.expandNode(nodes[i], true, false, false);
        //nodes[i].zAsync：记录 treeNode 节点是否已经进行过异步加载，避免父节点反复异步加载数据。
        if (nodes[i].isParent && nodes[i].zAsync) {
            expandNodes(nodes[i].children,treeId);
        }
    }
}

/**
 *  模糊查询ztree
 * @param zTreeId the ztree id used to get the ztree object
 * @param searchField selector of your input for fuzzy search
 * @param isHighLight whether highlight the match words, default true
 * @param isExpand whether to expand the node, default false
 *
 * @returns
 */
function fuzzySearch(zTreeId, searchField, isHighLight, isExpand){

    var zTreeObj = $.fn.zTree.getZTreeObj(zTreeId);//get the ztree object by ztree id
    if(!zTreeObj){
        alter("fail to get ztree object");
    }
    var nameKey = zTreeObj.setting.data.key.name; //get the key of the node name
    isHighLight = isHighLight===false?false:true;//default true, only use false to disable highlight
    isExpand = isExpand?true:false; // not to expand in default
    zTreeObj.setting.view.nameIsHTML = isHighLight; //allow use html in node name for highlight use

    var metaChar = '[\\[\\]\\\\\^\\$\\.\\|\\?\\*\\+\\(\\)]'; //js meta characters
    var rexMeta = new RegExp(metaChar, 'gi');//regular expression to match meta characters

    // keywords filter function
    function ztreeFilter(zTreeObj,_keywords,callBackFunc) {
        if(!_keywords){
            _keywords =''; //default blank for _keywords
        }

        // function to find the matching node
        function filterFunc(node) {
            if(node && node.oldname && node.oldname.length>0){
                node[nameKey] = node.oldname; //recover oldname of the node if exist
            }
            zTreeObj.updateNode(node); //update node to for modifications take effect
            if (_keywords.length == 0) {
                //return true to show all nodes if the keyword is blank
                zTreeObj.showNode(node);
                zTreeObj.expandNode(node,isExpand);
                return true;
            }
            //transform node name and keywords to lowercase
            if (node[nameKey] && node[nameKey].toLowerCase().indexOf(_keywords.toLowerCase())!=-1) {
                if(isHighLight){ //highlight process
                    //a new variable 'newKeywords' created to store the keywords information
                    //keep the parameter '_keywords' as initial and it will be used in next node
                    //process the meta characters in _keywords thus the RegExp can be correctly used in str.replace
                    var newKeywords = _keywords.replace(rexMeta,function(matchStr){
                        //add escape character before meta characters
                        return '\\' + matchStr;
                    });
                    node.oldname = node[nameKey]; //store the old name
                    var rexGlobal = new RegExp(newKeywords, 'gi');//'g' for global,'i' for ignore case
                    //use replace(RegExp,replacement) since replace(/substr/g,replacement) cannot be used here
                    node[nameKey] = node.oldname.replace(rexGlobal, function(originalText){
                        //highlight the matching words in node name
                        var highLightText =
                            // '<span style="color:#1e83ef; font-weight: bolder;">'
                           '' + originalText;
                            // +'</span>';
                        return 	highLightText;
                    });
                    zTreeObj.updateNode(node); //update node for modifications take effect
                }
                zTreeObj.showNode(node);//show node with matching keywords
                return true; //return true and show this node
            }

            zTreeObj.hideNode(node); // hide node that not matched
            return false; //return false for node not matched
        }

        var nodesShow = zTreeObj.getNodesByFilter(filterFunc); //get all nodes that would be shown
        processShowNodes(nodesShow, _keywords);//nodes should be reprocessed to show correctly
    }

    /**
     * reprocess of nodes before showing
     */
    function processShowNodes(nodesShow,_keywords){
        if(nodesShow && nodesShow.length>0){
            //process the ancient nodes if _keywords is not blank
            if(_keywords.length>0){
                $.each(nodesShow, function(n,obj){
                    var pathOfOne = obj.getPath();//get all the ancient nodes including current node
                    if(pathOfOne && pathOfOne.length>0){
                        //i < pathOfOne.length-1 process every node in path except self
                        for(var i=0;i<pathOfOne.length-1;i++){
                            zTreeObj.showNode(pathOfOne[i]); //show node
                            zTreeObj.expandNode(pathOfOne[i],true); //expand node
                        }
                    }
                });
            }else{ //show all nodes when _keywords is blank and expand the root nodes
                var rootNodes = zTreeObj.getNodesByParam('level','0');//get all root nodes
                $.each(rootNodes,function(n,obj){
                    zTreeObj.expandNode(obj,true); //expand all root nodes
                });
            }
        }
    }

    //listen to change in input element
    $(searchField).bind('input propertychange', function() {
        var _keywords = $(this).val();
        searchNodeLazy(_keywords); //call lazy load
    });

    var timeoutId = null;
    // excute lazy load once after input change, the last pending task will be cancled
    function searchNodeLazy(_keywords) {
        if (timeoutId) {
            //clear pending task
            clearTimeout(timeoutId);
        }
        timeoutId = setTimeout(function() {
            ztreeFilter(zTreeObj,_keywords); //lazy load ztreeFilter function
            $(searchField).focus();//focus input field again after filtering
        }, 500);
    }
}