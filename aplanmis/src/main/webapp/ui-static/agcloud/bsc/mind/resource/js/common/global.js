var WEB_ROOT = '/igsp';
// var HOST_PATH = /^http(s)?:\/\/(.*?)\//.exec(top.window.document.location.href)[0];//例： http://localhost:88/
var HOST_PATH="";
var LOGIN_URL = HOST_PATH + 'cas/oauth2.0/authorize?client_id=platform-inner-cas_oauth-cid&response_type=code&redirect_uri=' + HOST_PATH;
var LOGOUT_URL = HOST_PATH + 'cas/logout?service=' + HOST_PATH;
var MOCK_ROOT = '/mock/mock/5afcf45fb3da6b4aafaebacf/affair';
var LAYUI_ROOT = '/layui';
var MAIN_COLOR = '#1e9fff';

var ajaxSetupOpts = {headers : {'token':window.localStorage.getItem('tokenCode')}};
$.ajaxSetup(ajaxSetupOpts);
var ajaxSuccess = function(event, xhr, settings) {
    if (xhr && xhr.responseText) {
        var result = $.parseJSON(xhr.responseText);
        if (result && result.code == 102) {
            if(top.showLoginDiv) top.showLoginDiv(LOGIN_URL);//默认使用页面弹出层登录模式
            else {//保留支持原跳转登录模式
                var msg = '当前访问：' + settings.url + '<br/>未检测到当前已登录票据';
                var leaveTime = $('#leaveTime').text();
                if (!leaveTime) {
                    if (layui) {
                        var s = 5;
                        layui.layer.msg(msg + '<br/><a style="color:' + MAIN_COLOR + '" href="' + LOGIN_URL + '">立即跳转</a>&nbsp;<span id="leaveTime">' + s + '</span>秒后将跳转登录...', {time: false});
                        setInterval(function () {
                            var leaveTime = $('#leaveTime').text();
                            leaveTime = parseInt(leaveTime) - 1;
                            if (leaveTime > 0) {
                                $('#leaveTime').text(leaveTime);
                            }
                            else
                                top.location.href = LOGIN_URL;
                        }, 1000)
                    }
                    else {
                        alert(msg.replace('<br/>', '\n'));
                        top.location.href = LOGIN_URL;
                    }
                }
            }
        }
        else if(result && result.code == 401) {
            if(top.msg) top.msg(result.msg);
            else
                alert(result.msg);
        }
    }
}
$(document).ajaxSuccess(ajaxSuccess);

$(function()
{
    $('input[type="text"]').attr('autocomplete','off');
    //加载所有与页面id同名的参数，值到value中
    var params = UrlParam.paramMap();
    for(var name in params)
    {
        var value = params[name].join();
        $('[id="' + name + '"]').val(value);
    }

    if(typeof strRight == 'undefined' || !strRight) return;
    //隐藏/显示权限相关按钮，需保证页面已执行var strRight = loadOpList
    $('button[op]').hide();
    var rights = strRight.split(',');
    for(var i=0;i < rights.length;i++) $('button[op="' + rights[i] + '"]').show();
})
//获取权限信息
function loadOpList()
{
    var menuId = UrlParam.param("menuId");
    if(!menuId) return '';

    var strRight = '';
    globalSyncPost(WEB_ROOT + '/auth/loadOpList',{menuId:menuId},function(result)
    {
        if(result.data) strRight = result.data;
        if(!strRight) strRight = '';
    })
    return strRight;
}
//同步post
function globalSyncPost(url,params,successCallBack)
{
    globalPost(url,params,false,null,successCallBack);
}
//异步post
function globalAsyncPost(url,params,successCallBack)
{
    var mask = layui.layer.msg('处理中，请稍侯...',
    {
        icon: 16,
        shade: [0.5, '#f5f5f5'],
        scrollbar: false,
        time: 100 * 1000,
        success: function () {
            globalPost(url,params,true,null,function(result)
            {
                if(successCallBack) successCallBack(result);
            },function(){layui.layer.close(mask);});

        }
    });
}

//异步postJson
function globalAsyncPostJson(url,params,successCallBack)
{
    globalPost(url,params,true,'application/json',successCallBack);
}
//同步postJson 
function globalSyncPostJson(url,params,successCallBack)
{
	globalPost(url,params,false,'application/json',successCallBack);
}

//post
function globalPost(url,params,async,contentType,successCallBack,endCallBack)
{
    var strParams = '';
    if(contentType == 'application/json') strParams = JSON.stringify(params);
    $.ajax({
        url: url,
        type: 'post',
        data: (strParams == ''?params:strParams),
        // data: {},
        async:async,
        dataType: 'text',
        contentType: contentType || 'application/x-www-form-urlencoded',
        success : function(res) {
            try {
                var result =  $.parseJSON(res);
                if(endCallBack) endCallBack();
                if(result && (result.code == 102 || result.code == 401)) return;
                if(successCallBack) successCallBack(result);
            }
            catch(ex){
                if((typeof top.msg != 'undefined')){
                    top.msg('系统响应错误');
                }
                if(typeof console != 'undefined') console.log(ex); else alert(ex);
            }
        },
        error : function(error) {
            if(successCallBack) successCallBack({code:0,msg:'系统响应错误'});
            top.msg('系统响应错误');
        }
    });
}
//加载表单信息
if (!Array.isArray) {
    Array.isArray = function(arg) {
        return Object.prototype.toString.call(arg) === '[object Array]';
    };
}
function loadFormData(data)
{
    if(!data) return;
    for ( var p in data) {
        if (data[p] && typeof (data[p]) == "object") {
            for ( var a in data[p]) {
                setDefaultValue(a,data[p][a]);
            }
        }else if(!Array.isArray(data[p])){
            setDefaultValue(p,data[p]);
        }
    }
}
function setDefaultValue(key,value)
{//按规则设置键值到表单中
    var hasValue = (value != undefined && value != null);
    var el = $("#" + key);//规则1：按id设置
    if(hasValue && el.length > 0) {
        if(el.is('input[type="text"]') || el.is('input[type="hidden"]') || el.is('select') || el.is('textarea'))
            {el[0].defaultValue = value;if($.browser.msie) el.val(value);}
        else if(!el.is('input[type="file"]'))
            el.text(value);
    }
    else {
        el = $('[name="' + key + '"]');//规则2：按name设置
        if(el.length > 0) {
            if(hasValue
                && (el.is('input[type="text"]')|| el.is('input[type="hidden"]') || el.is('select') || el.is('textarea')))
                {el[0].defaultValue = value;if($.browser.msie) el.val(value);}
            else if(el.is('input[type="radio"]') || el.is('input[type="checkbox"]')) {
                el.filter('[value="' + value + '"]').prop('checked',true);
                el.filter('[value!="' + value + '"]').prop('checked',false);
                //默认值
                if(el.is('input[type="radio"]') && el.filter(':checked').length == 0)
                    el.eq(0).prop('checked',true);
            }
            else if(!el.is('input[type="file"]') && hasValue)
                el.text(value);
        }
    }
}
//加载字典名称
function loadDictName(elId,dictRootName,dictValue)
{
    globalSyncPost(WEB_ROOT + '/sysDict/findDictName',{dictRootName:dictRootName,value:dictValue},function(result)
    {
        if(result && result.code == 1)
        {
            if($('#' + elId).is('input')) $('#' + elId).val(result.data);
            else
                $('#' + elId).text(result.data);
        }
    })
}

var UrlParam = function() { // url参数
    var data, index;
    (function init() {
        data = [];    //值，如[["1","2"],["zhangsan"],["lisi"]]
        index = {};   //键:索引，如{a:0,b:1,c:2}
        var u = window.location.search.substr(1);
        if (u != '') {
            var params = decodeURIComponent(u).split('&');
            for (var i = 0, len = params.length; i < len; i++) {
                if (params[i] != '') {
                    var p = params[i].split("=");
                    if (p.length == 1 || (p.length == 2 && p[1] == '')) {// p | p= | =
                        data.push(['']);
                        index[p[0]] = data.length - 1;
                    } else if (typeof(p[0]) == 'undefined' || p[0] == '') { // =c 舍弃
                        continue;
                    } else if (typeof(index[p[0]]) == 'undefined') { // c=aaa
                        data.push([p[1]]);
                        index[p[0]] = data.length - 1;
                    } else {// c=aaa
                        data[index[p[0]]].push(p[1]);
                    }
                }
            }
        }
    })();
    return {
        // 获得参数,类似request.getParameter()
        param : function(o) { // o: 参数名或者参数次序
            try {
                return (typeof(o) == 'number' ? data[o][0] : data[index[o]][0]);
            } catch (e) {
            }
        },
        //获得参数组, 类似request.getParameterValues()
        paramValues : function(o) { //  o: 参数名或者参数次序
            try {
                return (typeof(o) == 'number' ? data[o] : data[index[o]]);
            } catch (e) {}
        },
        //是否含有paramName参数
        hasParam : function(paramName) {
            return typeof(paramName) == 'string' ? typeof(index[paramName]) != 'undefined' : false;
        },
        // 获得参数Map ,类似request.getParameterMap()
        paramMap : function() {
            var map = {};
            try {
                for (var p in index) {  map[p] = data[index[p]];  }
            } catch (e) {}
            return map;
        }
    }
}();
function loadMind(url,param) {
    var ywqxId = UrlParam.param('ywqxId');
    ywqxId="ff80808165a38a890165a77f5b160776";
    var itemName = UrlParam.param('itemName');
    itemName="建设工程规划许可证核发（城市建/构筑物工程）";
    if (ywqxId) {
        funInitTreeData(url,param,ywqxId, function (resData) {
            var data = {};
            if (resData && resData.length > 0) {
                data = transformData(resData);
            }else{
                data = {root: {data: {id: 'root', text: itemName}}};
            }
            window.minder.importJson(data);
            // window.minder.execCommand('hand');
            window.minder.execCommand('template', "right");//设置初始化模板类型
            window.minder.execCommand('theme', "fresh-blue-compat");//设置初始化模板类型

            //window.minder.execCommand('MoveCommand', "left");//设置初始化模板类型
        });
    }
}