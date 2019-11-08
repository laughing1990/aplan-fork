function customTreeBox(textId,url,queryParams) {
    var TreeBox = function (textId) {
        this.preId = textId;//相关id的前缀
        this.customTreeBoxValueText = $("#"+textId);//要显示选中值的元素
        this.customTreeBoxPanelId = this.preId + '_customTreeBoxPanel';//组件面板ID
        this.customTreeBoxKeyWordId = this.preId + '_customTreeBoxKeyWord';//关键字查询框ID
        this.treeId = this.preId + '_customTree';//treeId
        this.url = url;//树数据的请求地址
        this.queryParams = queryParams;//树数据的请求参数
        this.selectTree = null;//树对象
        this.queryBtnId = this.preId + '_queryBtn';//查询按钮ID
        this.allCheckBtnId = this.preId + '_allCheckBtn';//全选按钮ID
        this.expandAllNodeBtnId = this.preId + '_expandAllNodeBtn';//展开按钮ID
        this.foldAllNodeBtnId = this.preId + '_foldAllNodeBtn';//收起按钮ID
        this.allCheckFalseBtnId = this.preId + '_allCheckFalseBtn';//清空按钮ID
        this.lastQueryIdArray = new Array();//符合关键字的tId数组
        this.lastKeyword = null;//关键字

        this.treeSetting = {
            edit: {
                showRemoveBtn: false,//设置是否显示删除按钮
                showRenameBtn: false//设置是否显示编辑名称按钮
            },
            check: {
                enable: true,
                chkStyle: "checkbox",
                chkboxType: { "Y": "", "N": "" }
                // chkboxType: { "Y": "s", "N": "s" }
            },
            data: {
                simpleData: {
                    enable: true,
                    idKey: "id",
                    pIdKey: "pId",
                    // rootPId: 0
                }
            },
            view: {
                selectedMulti: true,//设置是否允许同时选中多个节点
                showTitle : true, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
                showLine: true, //设置 zTree 是否显示节点之间的连线。
                showHorizontal: false,//设置是否水平平铺树（自定义属性）
                fontCss: function (treeId, treeNode) {//样式
                    return (!!treeNode.highlight) ? {color: "#FF9900", "font-weight": "bold"} : {color: "inherit", "font-weight": "normal"};
                }

            },
            callback: {
                onCheck: this.onCustomTreeBoxCheck,
                // onClick: onClickSelectItemNode
            }
        };
    };


    TreeBox.prototype = {
        /**
         * 初始化
         * @returns {TreeBox}
         */
        init: function () {

            currentTreeBox.addCustomTreeBoxPanel();

            var _customTreeBoxPanel =  $("#"+currentTreeBox.customTreeBoxPanelId);

            _customTreeBoxPanel.hide();

            currentTreeBox.customTreeBoxValueText.bind('click',function(e){

                //计算弹框出现的位置
                var top = $(this).offset().top;
                var left = $(this).offset().left;
                // _customTreeBoxPanel.css("top",top+currentTreeBox.customTreeBoxValueText.outerHeight());
                _customTreeBoxPanel.css("top",top+currentTreeBox.customTreeBoxValueText.height());
                _customTreeBoxPanel.css("left",left);
                _customTreeBoxPanel.css("min-width",$(this).width());
                _customTreeBoxPanel.show();
                e.stopPropagation();
            });

            $(document).bind('click',function(){
                _customTreeBoxPanel.hide();
            });

            _customTreeBoxPanel.bind('click',function(e){
                e.stopPropagation();
            });

            currentTreeBox.initTree();


            $("#"+currentTreeBox.queryBtnId).bind('click',function(){

                currentTreeBox.clearSuggest();

                currentTreeBox.selectTree.expandAll(true);

                var _key =  $("#"+currentTreeBox.customTreeBoxKeyWordId).val();

                if(_key != currentTreeBox.lastKeyword){
                    currentTreeBox.lastKeyword = '';
                    currentTreeBox.lastQueryIdArray = new Array();
                }

                if(_key!=null && _key.trim() != ''){
                    // var treeObj = $.fn.zTree.getZTreeObj(currentTreeBox.treeId);
                    // var nodes = treeObj.getNodes();
                    var nodes = currentTreeBox.selectTree.getNodes();
                    if(!currentTreeBox.checkKeyword(_key,nodes)){
                        if(currentTreeBox.lastQueryIdArray.length>0 && _key == currentTreeBox.lastKeyword){
                            //查询一遍没有新的符合的节点，向上返回到第一个符合的
                            var lastQueryId = currentTreeBox.lastQueryIdArray[0];
                            currentTreeBox.lastQueryIdArray = new Array();
                            currentTreeBox.lastQueryIdArray.push(lastQueryId);
                            currentTreeBox.scrollTop(_key,lastQueryId,null);
                        }else if(_key != currentTreeBox.lastKeyword){
                            currentTreeBox.clearSearchNode();
                        }
                    }
                }else{
                    currentTreeBox.clearSearchNode();
                }

            });

            $("#"+currentTreeBox.allCheckBtnId).bind('click',function(){
                // var treeObj = $.fn.zTree.getZTreeObj(currentTreeBox.treeId);
                // treeObj.checkAllNodes(true);
                currentTreeBox.selectTree.checkAllNodes(true);
                currentTreeBox.selectAllNode();
                var names = currentTreeBox.getSelectedName();
                currentTreeBox.setCustomTreeBoxText(names.join(','));
            });


            $("#"+currentTreeBox.expandAllNodeBtnId).bind('click',function(){
                currentTreeBox.selectTree.expandAll(true);
            });

            $("#"+currentTreeBox.foldAllNodeBtnId).bind('click',function(){
                currentTreeBox.selectTree.expandAll(false);
            });

            $("#"+currentTreeBox.allCheckFalseBtnId).bind('click',function(){
                currentTreeBox.selectTree.checkAllNodes(false);
                currentTreeBox.selectTree.cancelSelectedNode();
                currentTreeBox.clearSearchNode();
                currentTreeBox.setCustomTreeBoxText("");
            });

            /**
             * 监听显示建议框
             */
            $("#"+currentTreeBox.customTreeBoxKeyWordId).on(" input propertychange",function(){
                var _key =  $("#"+currentTreeBox.customTreeBoxKeyWordId).val();
                if(_key!=null && _key.trim() != ''){
                    var nameList = currentTreeBox.getAllName();
                    if(nameList.length>0){
                        var arr = new Array();
                        for (var i = 0; i < nameList.length; i++) {
                            if ((nameList[i]).indexOf(_key)!=-1){
                                arr.push(nameList[i]);
                            }
                        }

                        if(arr.length>0){
                            currentTreeBox.setSuggest(arr);
                        }else{
                            currentTreeBox.clearSuggest();
                        }
                    }
                }else{
                    currentTreeBox.clearSuggest();
                }
            });

            return this;
        },

        /**
         * 设置显示选中数据
         * @param text
         */
        setCustomTreeBoxText:function(text){
            if(this.customTreeBoxValueText[0].tagName.toLowerCase() == 'input' || this.customTreeBoxValueText[0].tagName.toLowerCase().indexOf('text')!=-1){
                this.customTreeBoxValueText.val(text);
            }else{
                this.customTreeBoxValueText.html(text);
            }
        },

        /**
         * 显示
         */
        addCustomTreeBoxPanel:function(){
            var panelHtml = ''
                            +'<div id="'+this.customTreeBoxPanelId+'" class="dropdown-menu" style="z-index: 99;display: block;visibility: visible;overflow: visible;position: absolute;">'
                                +'<div>'

                                    +'<div class="row" style="margin: 0px;">'
                                        +'<div class="col-xl-5">'
                                            +'<input id="'+this.customTreeBoxKeyWordId+'" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..." style="background-color: #f0f0f075;border-color: #c4c5d6;">'
                                            +'<div style="position:absolute;z-index:10;" id="'+this.customTreeBoxKeyWordId+'_popup">'
                                                +'<table id="'+this.customTreeBoxKeyWordId+'_table" border="0" cellspacing="0" cellpadding="0">'
                                                    +'<tbody id="'+this.customTreeBoxKeyWordId+'_table_body"></tbody>'
                                                +'</table>'
                                            +'</div>'
                                        +'</div>'
                                        +'<div class="col-xl-7">'
                                            +'<button type="button" class="btn btn-secondary" style="margin-right: 5px;" id="'+this.queryBtnId+'">查询</button>'
                                            +'<button type="button" class="btn btn-secondary" style="margin-right: 5px;" id="'+this.expandAllNodeBtnId+'">展开</button>'
                                            +'<button type="button" class="btn btn-secondary" style="margin-right: 5px;" id="'+this.foldAllNodeBtnId+'">收起</button>'
                                            +'<button type="button" class="btn btn-secondary" style="margin-right: 5px;" id="'+this.allCheckBtnId+'">全选</button>'
                                            +'<button type="button" class="btn btn-secondary" style="margin-right: 5px;" id="'+this.allCheckFalseBtnId+'">清空</button>'
                                        +'</div>'
                                    +'</div>'

                                    +'<div style="overflow: auto; position: relative; zoom: 1;">'
                                        +'<div style="position: relative; zoom: 1;">'
                                            +'<div style="position: relative; zoom: 1;">'
                                                +'<div id="'+this.treeId+'" class="ztree" style="height:250px;overflow: auto;"></div>'
                                            +'</div>'
                                        +'</div>'
                                    +'</div>'
                                +'</div>'
                            +'</div>';
            $("body").append(panelHtml);
        },

        /**
         * 加载树
         */
        initTree:function(){

            var _queryParams = this.queryParams;

            if(_queryParams==null){
                _queryParams = {};
            }

            $.ajax({
                url: currentTreeBox.url,
                type:'post',
                data:_queryParams,
                async: false,
                dataType: 'json',
                success: function(data){

                    // var data = [
                    //     {id :"1",pId:"0",name :"maomao"},
                    //     {id :"2",pId:"0",name : "maomao"},
                    //     {id :"3",pId:"0",name : "maomao"},
                    //     {id :"4",pId:"0",name : "maomao"},
                    //     {id :"11",pId:"1",name: "maomao"},
                    //     {id :"12",pId:"1",name: "maomao"},
                    //     {id :"21",pId:"2",name: "maomao"},
                    //     {id :"22",pId:"2",name: "maomao"},
                    //     {id :"31",pId:"3",name: "maomao"},
                    //     {id :"32",pId:"3",name: "maomao"},
                    //     {id :"41",pId:"4",name: "maomao"},
                    //     {id :"42",pId:"4",name: "maomao"},
                    //     {id :"43",pId:"4",name: "maomao"},
                    //     {id :"111",pId:"11",name:"maomao"},
                    //     {id :"112",pId:"11",name: "maomao"},
                    //     {id :"121",pId:"12",name: "maomao"},
                    //     {id :"122",pId:"12",name: "maomao"}
                    // ];

                    if(data!=null && data.length>0){
                        if(currentTreeBox.selectTree!=null){
                            currentTreeBox.selectTree.destroy();
                        }
                        currentTreeBox.selectTree = $.fn.zTree.init($("#"+currentTreeBox.treeId), currentTreeBox.treeSetting,data);
                    }
                },
                error: function(){
                    swal('错误信息', '初始化数据异常!', 'error');
                }
            });
        },

        /**
         * 选中或取消事件
         * @param event
         * @param treeId
         * @param treeNode
         */
        onCustomTreeBoxCheck:function(event, treeId, treeNode){
            var treeObj = $.fn.zTree.getZTreeObj(currentTreeBox.treeId);
            if(treeNode.checked){
                treeObj.selectNode(treeNode,true);
                var names = currentTreeBox.getSelectedName();
                currentTreeBox.setCustomTreeBoxText(names.join(','));
            }else{
                treeObj.cancelSelectedNode(treeNode);
                var names = currentTreeBox.getSelectedName();
                if(names.length>0) {
                    currentTreeBox.setCustomTreeBoxText(names.join(','));
                }else{
                    currentTreeBox.setCustomTreeBoxText("");
                }
            }

        },

        /**
         * 查询符合的节点
         * @param keyword
         * @param nodes
         * @returns {boolean}
         */
        checkKeyword:function(keyword,nodes){
            if(nodes!=null && nodes.length>0){
                for(var i=0;i<nodes.length;i++){
                    var node = nodes[i];
                    if(node.name.indexOf(keyword.trim())!=-1){
                        if(keyword == this.lastKeyword && this.lastQueryIdArray.length>0){
                            if(this.lastQueryIdArray.indexOf(node.tId) != -1){

                            }else{
                                this.scrollTop(keyword,node.tId,null);
                                return true;
                            }
                        }else {
                            this.scrollTop(keyword,node.tId,null);
                            return true;
                        }
                    }

                    if(node.children!=null && node.children.length>0){
                        for(var j=0;j<node.children.length;j++){
                            if(this.checkKeyword(keyword,node.children)){
                                return true;
                            }
                        }
                    }
                }
            }

            return false;
        },

        /**
         * 滚动到目标节点
         * @param keyword
         * @param tId
         * @param top
         */
        scrollTop:function(keyword,tId,top){
            if(this.lastQueryIdArray.indexOf(tId) == -1 ){
                this.lastQueryIdArray.push(tId);
            }
            this.lastKeyword = keyword;

            if(top==null){
                // $("#"+this.treeId).scrollTop(0);
                top = $("#"+tId).position().top;
                top = $("#"+this.treeId).scrollTop()+top;
            }
            $("#"+this.treeId).animate({scrollTop: top}, 500);

            this.changeSearchNode(tId);

        },

        /**
         * 修改目标节点的样式
         * @param tId
         * @param nodes
         */
        changeSearchNode:function (tId,nodes) {

            if(!nodes){
                nodes = this.selectTree.getNodes();
            }

            if(nodes!=null && nodes.length>0){
                for(var i=0;i<nodes.length;i++){
                    var node = nodes[i];
                    if(node.tId == tId){
                        node.highlight = true;
                        this.selectTree.updateNode(node);
                    }else{
                        node.highlight = false;
                        this.selectTree.updateNode(node);
                    }

                    if(node.children!=null && node.children.length>0){
                        this.changeSearchNode(tId,node.children);
                    }
                }
            }
        },

        /**
         * 清空样式
         * @param nodes
         */
        clearSearchNode:function (nodes) {

            if(!nodes){
                nodes = this.selectTree.getNodes();
            }
            if(nodes!=null && nodes.length>0){
                for(var i=0;i<nodes.length;i++){
                    var node = nodes[i];
                    node.highlight = false;
                    this.selectTree.updateNode(node);

                    if(node.children!=null && node.children.length>0){
                        this.clearSearchNode(node.children);
                    }
                }
            }
        },

        /**
         * 全选
         * @param nodes
         */
        selectAllNode:function (nodes) {

            if(!nodes){
                nodes = this.selectTree.getNodes();
            }
            if(nodes!=null && nodes.length>0){
                for(var i=0;i<nodes.length;i++){
                    var node = nodes[i];
                    this.selectTree.selectNode(node,true);

                    if(node.children!=null && node.children.length>0){
                        this.selectAllNode(node.children);
                    }
                }
            }
        },

        /**
         * 获取选中节点
         * @returns {*|Array}
         */
        getCustomTreeBoxSelectedNodes:function () {
            var nodes = this.selectTree.getSelectedNodes();
            return nodes;
        },

        /**
         * 获取选中节点的值
         * @returns {any[]}
         */
        getSelectedValue:function () {
            var nodes = this.getCustomTreeBoxSelectedNodes();
            var checkIds = new Array();
            if(nodes!=null && nodes.length>0){
                for(var i=0;i<nodes.length;i++){
                    checkIds.push(nodes[i].id);
                }
            }

            return checkIds;
        },

        /**
         * 获取选中节点的显示内容
         * @returns {any[]}
         */
        getSelectedName:function () {
            var nodes = this.getCustomTreeBoxSelectedNodes();
            var checkNames = new Array();
            if(nodes!=null && nodes.length>0){
                for(var i=0;i<nodes.length;i++){
                    checkNames.push(nodes[i].name);
                }
            }

            return checkNames;
        },

        /**
         * 获取所有节点的显示内容数组
         * @param nodes
         * @param names
         * @returns {*}
         */
        getAllName:function (nodes,names) {
            if(!nodes){
                nodes = this.selectTree.getNodes();
            }

            if(!names){
                names = new Array();
            }
            if(nodes!=null && nodes.length>0){
                for(var i=0;i<nodes.length;i++){
                    var node = nodes[i];
                    names.push(node.name);

                    if(node.children!=null && node.children.length>0){
                        this.getAllName(node.children,names);
                    }
                }
            }

            return names;
        },

        /**
         * 显示建议框
         * @param arr
         */
        setSuggest:function (arr) {
            var size = arr.length;
            this.setSuggestOffsets();
            var row, cell, txtNode;
            for (var i = 0; i < size; i++) {
                var nextNode = arr[i];
                row = document.createElement("tr");
                cell = document.createElement("td");
                //鼠标悬停或离开选项时颜色变化
                cell.onmouseout = function() {
                    this.setAttribute("bgcolor", "#FFFAFA");
                };
                cell.onmouseover = function() {
                    this.setAttribute("bgcolor", "#FF9900");
                };
                cell.setAttribute("bgcolor", "#FFFAFA");
                cell.setAttribute("border", "0");

                //鼠标点击选项给输入框赋值
                cell.onclick = function() {
                    currentTreeBox.populate(this);
                } ;
                txtNode = document.createTextNode(nextNode);
                cell.appendChild(txtNode);
                row.appendChild(cell);
                var tableBody = document.getElementById(this.customTreeBoxKeyWordId+'_table_body');
                tableBody.appendChild(row);
            }
        },

        /**
         * 建议框样式设置
         */
        setSuggestOffsets:function () {
            var inputField = document.getElementById(this.customTreeBoxKeyWordId);
            var end = inputField.offsetWidth;
            // var left = this.calculateOffsetLeft(inputField);
            // var top = this.calculateOffsetTop(inputField) + inputField.offsetHeight;
            var completeDiv = document.getElementById(this.customTreeBoxKeyWordId+'_popup');
            completeDiv.style.border = "black 1px solid";
            // completeDiv.style.left = left + "px";
            // completeDiv.style.top = top + "px";
            var table = document.getElementById(this.customTreeBoxKeyWordId+'_table');
            table.style.width = end + "px";
        },

        // calculateOffsetLeft:function (field) {
        //     return this.calculateOffset(field, "offsetLeft");
        // },
        //
        // calculateOffsetTop:function (field) {
        //     return this.calculateOffset(field, "offsetTop");
        // },
        // calculateOffset:function (field, attr) {
        //     var offset = 0;
        //     while(field) {
        //         offset += field[attr];
        //         field = field.offsetParent;
        //     }
        //     return offset;
        // },
        /**
         * 选中建议框的选项
         * @param cell
         */
        populate:function (cell) {
            var inputField = document.getElementById(this.customTreeBoxKeyWordId);
            inputField.value = cell.firstChild.nodeValue;
            this.clearSuggest();
        },

        /**
         * 清除建议框
         */
        clearSuggest:function () {
            var tableBody = document.getElementById(this.customTreeBoxKeyWordId+'_table_body');
            if(tableBody){
                var ind = tableBody.childNodes.length;
                for (var i = ind - 1; i >= 0 ; i--) {
                    tableBody.removeChild(tableBody.childNodes[i]);
                }
                var completeDiv = document.getElementById(this.customTreeBoxKeyWordId+'_popup');
                completeDiv.style.border = "none";
            }
        }
    };

    var currentTreeBox = new TreeBox(textId,url,queryParams);

    return currentTreeBox;
}

