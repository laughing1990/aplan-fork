var datagrid;
function initDataList(id) {
    var datagridId = id || 'datagrid'
        , $datagrid = $('#' + datagridId);
    if ($datagrid.length > 0) {
        var url = $datagrid.attr('url');
        if (typeof module != 'undefined' && !url) $datagrid.attr('url', module + '/list');
        var id = $datagrid.attr('id');
        //动态截取高度赋予表格
        var headDivHight = $('.headDiv').outerHeight() + 10;
        //头部div折叠了，获取里面的layui-elem-quote高度
        if($('.headDiv').hasClass('fold')){
            headDivHight = $('.headDiv').find('.layui-elem-quote').outerHeight() + 10;
        }
        var defaultHight = $(window).height() - headDivHight - $('.moreDiv').outerHeight() - $('.blankDiv').outerHeight() - $('.actionDiv').outerHeight() - ($('.bottomDiv').length > 0 ? ($('.bottomDiv').outerHeight()) : 0) - 25;
        var height = $datagrid.attr('height') || defaultHight;
        if (typeof layui != 'undefined') {
            /*datagrid = layui.table.init(datagridId,
                {//转化静态表格
                    id: id,
                    height: height,
                    limit: 15,
                    page:
                        {
                            layout: ['count', 'prev', 'page', 'next'],
                            prev: '上一页',
                            next: '下一页'
                        }
                });*/
            //需求将跳转页面放出来
            datagrid = layui.table.init(datagridId,
                {//转化静态表格
                    id: id,
                    height: height,
                    limit: 15,
                    page: true
                });
            searchList(true);
        }

        //添加enter键回车提交查询
        var $form = $('#datagrid').parents('form:first');
        if($form.length > 0){
            if($form.find('[type=submit]').length == 0){
                $form.prepend(
                    $('<input />', {type: 'submit'}).css({display: 'block', overflow: 'hidden', width: '0px', height: '0px',position: 'absolute','z-index': '-999'})
                ).bind('submit', function(){
                    searchList();
                    return false;
                })
            }
        }
    }
}

function bindRowClick(id) {
    var tb = $('#' + id).next('.layui-table-view').find('table.layui-table');
    tb.click(function (e) {
        if ($(e.target).is('.layui-icon') && $(e.target).parent().is('.layui-form-checkbox')) return;
        if ($(e.target).is('.layui-form-checkbox')) return;
        if ($(e.target).is('a')) return;

        var tr = $(e.target).closest('tr')[0];
        var dataIndex = $(tr).attr('data-index');
        if (datagrid && datagrid.cache['datagrid'][$(tr).index()]) {
            var checkVal = datagrid.cache['datagrid'][$(tr).index()][datagrid.config.checkName];
            var newVal = !checkVal;

            datagrid.cache['datagrid'][$(tr).index()][datagrid.config.checkName] = newVal;
            var ck = $(tr).find('input[name="layTableCheckbox"]')[0];
            if (ck) ck.checked = newVal;

            var fixTr = $('.layui-table-box .layui-table-fixed .layui-table-body .layui-table').find('tr[data-index=' + dataIndex + ']');
            var fixCk = fixTr.find('input[name="layTableCheckbox"]')[0];
            if (fixCk) fixCk.checked = newVal;

            layui.form.render('checkbox', 'LAY-table-' + datagrid.index);
        }
    });
}

//查询
function list() {
    searchList();
}

function searchList(isGo, id) {
    var datagridId = id || 'datagrid'
        , $datagrid = $('#' + datagridId);
    if ($datagrid.length > 0) {
        var inputs = $('input[name]')
        var params = {};
        for (var i = 0; i < inputs.length; i++) {
            var input = inputs[i];
            if ($(input).attr('notForSearch') == 'true') continue;
            eval('if(params["' + $(input).attr('name') + '"] != undefined) params["' + $(input).attr('name') + '"] += "," + $(input).val();else params["' + $(input).attr("name") + '"]="' + ($(input).val() ? $(input).val() : '') + '"');
        }
        var selects = $('select[name]');
        for (var i = 0; i < selects.length; i++) {
            var select = selects[i];
            eval('params["' + $(select).attr('name') + '"]="' + ($(select).val() ? $(select).val() : '') + '"');
        }

        var url = $datagrid.attr('url');
        layui.table.reload(datagridId,
            {
                url: url,
                method: 'post',
                where: params,
                page: (!isGo ? {curr: 1} : {}),
                response: {
                    statusName: 'code', //数据状态的字段名称
                    statusCode: 1, //成功的状态码
                    msgName: 'msg', //状态信息的字段名称
                    countName: 'totalCount',//数据总数的字段名称
                    dataName: 'data' //数据列表的字段名称
                }
            });
        bindRowClick(datagridId);

        $('#loadList').val('false');
    }
}

//清空查询
function clearSearch() {
    $('input[type="text"]', $('#selField')).val('');
}

function msg(msg,title,timeout)
{
    if(title)
    {
        layer.open
        ({
            type		: 1,
            offset		: 'rb',//右下
            title		: (title?title:'提示'),
            content		: '<div style="padding:20px 20px;">'+ msg +'</div>',
            shade		: 0,
            time		: (timeout?timeout:3000),
            anim 		: 2//从最底部往上滑入
        });
    }
    else
        layer.msg(msg,{time:(timeout?timeout:3000)});
}

//删除
function del(todo, url, tip) {
    var checkStatus = layui.table.checkStatus('datagrid');
    var rows = checkStatus.data;
    var ids = [];
    for (var i = 0; i < rows.length; i++) {
        if (rows[i]['isSys'] == '1') {
            msg('系统记录不能被' + todo);
            return;
        }
        else
            ids.push(rows[i][$('#datagrid').attr('idField')]);
    }
    if (ids.length == 0) {
        msg('请选择一条记录');
        return;
    }
    else
        ajaxDel(todo, ids, url, tip);
}

function ajaxDel(todo, ids, url, tip) {
    var strIds = '', strDesc = '';
    if (typeof ids == 'string') {
        strIds = ids;
        strDesc = '确定' + todo + '？' + (tip ? tip : '');
    }
    else {
        strIds = ids.join(',');
        strDesc = '确定' + todo + '选中的记录？(' + ids.length + '条)' + (tip ? tip : '');
    }
    if (typeof layui != 'undefined') {
        layui.layer.confirm(strDesc, function (index) {
            globalAsyncPost(url, {'ids': strIds}, function (result) {
                if (result.code == 1) {
                    msg(todo + '成功');
                    searchList();
                }
                else {
                    if (result.msg)
                        msg(result.msg);
                    else
                        msg(todo + '失败');
                }
                layui.layer.close(index);
            })
        })
    }
}

//发送请求
function ajax(todo, url) {
    if (todo && url) {
        globalAsyncPost(url, {}, function (result) {
            if (result.code == 1) {
                top.msg(todo + '成功');
                searchList();
            }
            else {
                if (result.msg)
                    top.msg(result.msg);
                else
                    top.msg(todo + '失败');
            }
        });
    }
}

//common setter
function setName(rowData) {
    var value = rowData[this.field];

    if (typeof urlView == 'undefined') return value;

    var todo = ($('#forSel').val() == 'true') ? 'window.open' : 'top.showData';
    var strId = $('#datagrid').attr('idField');

    return '<a class="link" onclick="' + todo + '(\'' + urlView
        + '?' + strId + '=' + rowData[strId] + '\',\'查看\');">'
        + value
        + '</a>';
}

function setOP(rowData) {
    if (typeof urlEdit == 'undefined' || typeof urlDel == 'undefined' || typeof strRight == 'undefined') return value;

    var strId = $('#datagrid').attr('idField');
    return ('<div class="layui-btn-container">' +
        (strRight.indexOf('修改') >= 0 ?
            '<a class="layui-btn layui-btn-sm" title="修改" onclick="top.showData(\''
            + urlEdit + '?' + strId + '=' + rowData[strId]
            + '\',\'修改\')">修改</a>'
            : '')
        + (strRight.indexOf('删除') >= 0 ?
            '<a class="layui-btn layui-btn-danger layui-btn-sm" title="删除" onclick="ajaxDel(\'删除\',\''
            + rowData[strId] + '\',\'' + urlDel
            + '\',\'\')" >删除</a>'
            : '')
        + '</div>');
}

function addTitle(rowData) {
    var value = rowData[this.field];
    value = value ? value : '';
    return '<a href="javasscript:void(0);" title="' + value + '">' + value + '</a>';
}

function layout() {
    //调整列表结构
    setBodyDivTop();
    setBottomDivTop();
    setBodyDivHeight();
}

function setBodyDivTop() {
    $('.bodyDiv').css('top', $('.headDiv').outerHeight() + $('.moreDiv').outerHeight() + $('.blankDiv').outerHeight() + $('.actionDiv').outerHeight());
}

function setBottomDivTop() {
    $('.bottomDiv').css('top', $(window).height() - $('.bottomDiv').outerHeight());
    $('.bottomDiv').css('width', $('.contaner').width());
}

function setBodyDivHeight() {
    $('.bodyDiv').css('height', $(window).height() - $('.headDiv').outerHeight() - $('.moreDiv').outerHeight() - $('.blankDiv').outerHeight() - $('.actionDiv').outerHeight() - ($('.bottomDiv').length > 0 ? ($('.bottomDiv').outerHeight()) : 0) - 25);
}

function setVersion(rowData){
    var value = rowData[this.field]
        ,retData = '1';
    if(value){
        retData = value;
    }
    return 'v' + retData + '.0';
}

function setTime(rowData) {

    var value = rowData[this.field];
    if (value != null) {
        value = value.substr(0, value.lastIndexOf(':'));
    } else {
        value = '';
    }
    return value;
}

function setDate(rowData) {

    var value = rowData[this.field];

    if (value != null) {
        value = value.substr(0, value.lastIndexOf(' '));
    } else {
        value = '';
    }

    return value;
}


function setFormDate(rowData){
    var value = rowData[this.field]
        ,retData = new Date();
    if(value){
        retData = new Date(value);
    }
    return retData.format('yyyy年MM月dd日');
}


function setFormTime(rowData){
    var value = rowData[this.field]
        ,retData = new Date();
    if(value){
        retData = new Date(value);
    }
    return retData.format('yyyy年MM月dd日 hh时mm分');
}
