$(function () {
    //popover
    $(document).delegate('[data-toggle="popover"]', 'mouseover', function () {
        var that = this;
        var title = $(this).data('title');
        layui.layer.tips(title, that);
    });
    //相册查看
    $(document).delegate('[data-toggle="photos"]', 'click', function () {

        var dataList = $(this).data('content');

        if (dataList && dataList.length > 0) {

            var title = $(this).data('title') || '图片预览';
            var id = $(this).data('id') || '';
            var start = $(this).data('start') || 0;
            var json = {title: title, id: id, start: start, data: []}
            for (var i in dataList) {
                var data = dataList[i];
                var src = WEB_ROOT + "/sysAttach/fileDownload?token="
                    + window.localStorage.getItem('tokenCode') + '&fileId=' + data.idNum; //原图地址
                var photo = {alt: data.name, pid: data.idNum, src: src};
                json.data.push(photo);
            }
            layer.photos({
                photos: json
                , anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
            });
        } else {
            layui.layer.msg('没有相关数据');
        }
    })
})


    /**
     * 跳转页面，并且将地址栏的参数全部传过去
     * @param href
     */
    function toHrefTakeParam(href) {
        var params = UrlParam.paramMap();
        if (!$.isEmptyObject(params)) {
            var paramSerial = '';
            if (href.lastIndexOf('?') > -1) {
                paramSerial = '&';
            } else {
                paramSerial = '?';
            }
            for (var name in params) {
                var value = params[name].join();
                paramSerial += name + '=' + value + '&';
            }
            paramSerial = paramSerial.substr(0, paramSerial.length - 1);
            href += paramSerial;
        }
        location.href = href;
    }

    /**
     * 动态载入js
     * @param url
     * @param callback
     */
    function loadScript(url, callback) {
        var script = document.createElement("script");
        script.type = "text/javascript";
        if (typeof(callback) != "undefined") {
            if (script.readyState) {
                script.onreadystatechange = function () {
                    if (script.readyState == "loaded" || script.readyState == "complete") {
                        script.onreadystatechange = null;
                        callback();
                    }
                };
            } else {
                script.onload = function () {
                    callback();
                };
            }
        }
        ;
        script.src = url;
        document.body.appendChild(script);
    }

    function msg(msg, title, timeout, callback) {
        if (title) {
            layer.open
            ({
                type: 1,
                offset: 'rb',//右下
                title: (title ? title : '提示'),
                content: '<div style="padding:20px 20px;">' + msg + '</div>',
                shade: 0,
                time: (timeout ? timeout : 3000),
                anim: 2,//从最底部往上滑入
            });
        }
        else
            layer.msg(msg, {
                time: (timeout ? timeout : 3000), end: function () {
                    if (typeof callback == 'function') {
                        callback()
                    }
                }
            });
    }

    /**
     * 获取项目根路径
     * @returns
     */
    function getRootPath() {
        //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
        var curWwwPath = window.document.location.href;
        //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
        var pathName = window.document.location.pathname;
        var pos = curWwwPath.indexOf(pathName);
        //获取主机地址，如： http://localhost:8083
        var localhostPaht = curWwwPath.substring(0, pos);
        //获取带"/"的项目名，如：/uimcardprj
        var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
        return (localhostPaht + projectName);
    }

    $.fn.serializeObject = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    }

    Date.prototype.format = function (format) {
        var o = {
            "M+": this.getMonth() + 1, //month
            "d+": this.getDate(),    //day
            "h+": this.getHours(),   //hour
            "m+": this.getMinutes(), //minute
            "s+": this.getSeconds(), //second
            "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
            "S": this.getMilliseconds() //millisecond
        }
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
            (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o) if (new RegExp("(" + k + ")").test(format))
            format = format.replace(RegExp.$1,
                RegExp.$1.length == 1 ? o[k] :
                    ("00" + o[k]).substr(("" + o[k]).length));
        return format;
    }
