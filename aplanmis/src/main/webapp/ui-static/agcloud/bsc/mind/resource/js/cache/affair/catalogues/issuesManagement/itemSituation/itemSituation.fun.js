/**
 * 情形模块共同调用的一些函数
 * Created by lilm on 2017/11/3.
 */
var path = getRootPath() + '/affair/catalogues/issuesManagement/itemSituation'
    , ROOT_ID = 'treeRoot'
    ;


/**
 * 修改值方法
 * @param cfg {id,value,newValue}
 */
function funEditValue(id,value){
    parent.layer.confirm('确认修改？',function(){
        var params = {ID: id, NAME: value,tm: Math.random()};
        funCommonSubmitData(params);
    })
}

function funCommonSubmitData(url, params, callback){
    if(typeof(callback) !== 'function'){
        callback = function(response){
            if(response.code == 1){
                msg('操作成功');
            }else{
                msg(response.msg || '操作失败');
            }
        }
    }
    globalAsyncPostJson(url,params,callback);
}


/**
 * 具体调用条件数据的方法
 * @param itemcode 事项编码
 * @param isSimple 是否是简单的数据
 * @param callback 回调函数
 */
function initData(url,param,ywqxId, isSimple, callback){
    // var url = WEB_ROOT + '/affairItemSituation/v3/datajson/view/list'
    //     ,param = {ywqxId: ywqxId, isSimple: isSimple}
    // ;

    globalAsyncPost(url, param, function (response) {
        if(response.code === 1){
            if(typeof(callback) === 'function'){
                callback(response.data);
            }
        }else{
            msg('服务调用失败，请稍后重试')
        }
    })
}

/**
 * 获取列表结构的数据
 */
function funInitArrayData(ywqxId, callback){
    initData(ywqxId, 1 ,callback);
}

/**
 * 获取树状结构的数据
 */
function funInitTreeData(url,param,ywqxId, callback){
    initData(url,param,ywqxId, 0 ,callback);
}

/**
 * 初始化组合数据
 */
function funInitCanvas(cfg){
    var id = 'jsmind_container';
    var loader = $('<div></div>').append('<i class="layui-icon">&#xe63d;</i>').append('&nbsp;数据加载中，请稍后...')
    $('#'+id).append(loader);
    var itemcode = UrlParam.param('itemcode');
    funInitArrayData(itemcode, function (resData) {
        loader.remove();
        var data = [];
        if(resData && resData.length > 0){
            data = transformData(resData);
        }else{
            var itemName = UrlParam.param('itemName');
            data.push({"id": ROOT_ID, "isroot": true, "topic": itemName})
        }
        $('#'+id).empty();
        load_jsmind(data);
    })
}

/**
 * 打开增加条件面板
 * @param id
 */
function funAddDataPageFn(id){
    var itemcode = UrlParam.param('itemcode');
    var itemName = UrlParam.param('itemName');
    var url = path + '/itemSituation_edit.html?id=' + id + '&itemcode=' + itemcode + '&itemName=' + itemName + '&selected=0';
    funShowDataPanel(url, function(){
        funInitCanvas();
    });
}


//打开批量操作条件面板函数
function funShowBatchDataPanel(url, closefn){
    var height = '100%';
    parent.layer.open({
        type: 2
        // ,title	: [title,'color:#FFFFFF;background-color:#1E9FFF;font-size:16px;']
        ,title: false
        ,content: url
        ,closeBtn: 1
        ,shade: [0.1, '#fff']
        //,shade: false
        ,scrollbar: false
        ,shadeClose: true
        ,maxmin: false
        ,closeBtn: 0
        ,offset: 'rt'
        ,anim: 2
        ,skin: 'layer-ext-block'
        ,area: ['70%',height]
        ,end:closefn
    });
}

//打开数据信息面板函数
function funShowDataPanel(url, closefn, skin, layerSet){
    var title = '<i class="layui-icon" style="margin-right:10px;">&#xe614;</i>条件选项操作';
    var height = '100%';
    var showDataLayer = parent.layer.open({
        type: 2
        // ,title	: [title,'color:#FFFFFF;background-color:#1E9FFF;font-size:16px;']
        ,title: false
        ,content: url
        ,closeBtn: 1
        ,shade: [0.1, '#fff']
        //,shade: false
        ,scrollbar: false
        ,shadeClose: true
        ,maxmin: false
        ,closeBtn: 0
        ,offset: 'lt'
        ,anim: 2
        ,skin: 'layer-ext-block'
        ,area: ['70%',height]
        ,end:closefn
    });
}

function resizeLayer(height){
    $('.layui-main').height(height)
    if(parent.layer && parent.layer.getFrameIndex){
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.iframeAuto(index);
    }
}

function closeLayer(flag){
    if(parent.layer && parent.layer.getFrameIndex) {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    }
}

function trim(str){
    return str.replace(/(^\s+)|(\s+$)/g,"").replace(/\s/g,"");
}

/**
 * 转换数据格式给修改条件模块回显使用
 * @param data
 * @returns {Array}
 */
function transformTjEchoData(data){
    var resData = '';
    for(var d in data){
        var nd = data[d];
        resData += nd + ','
    }
    resData = '[' + resData.substr(0, resData.length - 1) + ']';
    return resData;
}

/**
 * 转换数据格式给jsmind使用
 * @param data
 * @returns {Array}
 */
function transformData(data){
    var resData = [];
    for(var d in data){
        var nd = data[d];
        var rd = {id: nd.id, parentid: nd.pid, topic: nd.name};
        if(nd.pid === 'treeRoot'){
            rd.isroot = true;
        }
        resData.push(rd);
    }
    return resData;
}

/**
 * 判断是否是最后一级节点
 * @param id
 * @param data
 */
function funIsLastNode(id, data){
    var ret = true;
    if(data && data.length > 0){
        for(var d in data){
            var nd = data[d];
            if(nd.pid === id){
                ret = false;
                break;
            }
        }
    }else{
        ret = false;
    }
    return ret;
}

/**
 * 转换数据格式给jsmind使用
 * @param data
 * @param ids 选中插件集合
 * @returns {Array}
 */
function transformDataCheckbox(data, ids){
    var resData = [];
    for(var d in data){
        var nd = data[d];
        var topic = nd.name;
        if(funIsLastNode(nd.id, data)){
            topic = '<input type="checkbox" class="qxCheckbox" lay-skin="primary" lay-filter="qxCheckbox" value='+nd.combId+' title="'+nd.name + '" ';
            if(ids && nd.combId && ids.indexOf(nd.combId) > -1){
                topic += 'checked';
            }
            topic += '/>'
        }
        var rd = {id: nd.id, parentid: nd.pid, topic: topic};
        if(nd.pid === 'treeRoot'){
            rd.isroot = true;
        }
        resData.push(rd);
    }
    return resData;
}

/**
 * 获取项目根路径
 * @returns
 */
function getRootPath(){
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath=window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
    return(localhostPaht+projectName);
}