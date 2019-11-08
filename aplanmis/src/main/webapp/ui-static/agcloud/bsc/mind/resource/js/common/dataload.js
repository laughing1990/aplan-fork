/**
 * 加载数据
 * @param dom 回显的范围
 * @param data 回显的数据
 * @param ruleSelector 选择器规则
 */
function funDataEcho(dom, data, ruleSelector) {
    var $dom = $(dom),
        selector = ruleSelector || '[data-name]',
        $controls = $(dom).find(selector)
    ;
    if ($dom.length > 0) {
        $controls.each(function (index, ct) {
            var $ct = $(ct);
            var nm = $ct.data('name');

            $ct.html(data[nm]);
        })
    }
}

/**
 * 加载数据
 * @param dom 包含的dom
 * @param connector 接口地址
 * @param ruleSelector 选择器规则
 */
function funDataLoad(dom, connector, callback, ruleSelector) {
    var $dom = $(dom),
        selector = ruleSelector || '[data-name]',
        $controls = $(dom).find(selector)
    ;
    if ($dom.length > 0) {
        globalAsyncPost(connector.url, connector.param, function (response) {
            if (response.code == '1') {
                var data = response.data;
                if (data) {
                    $controls.each(function (index, ct) {
                        var $ct = $(ct);
                        var nm = $ct.data('name');

                        $ct.html(data[nm]);
                    })
                    if (typeof callback == 'function') {
                        callback(data);
                    }
                }
            } else {
                if (response.msg)
                    top.msg(response.msg);
                else
                    top.msg(todo + '失败');
            }
        });
    }
}


function funFormDataLoad(data) {
    if (!data) return;
    for (var p in data) {// 遍历json对象的每个key/value对,p为key
        if (typeof (data[p]) == "object") {//如果字段是对象。。又遍历一次咯
            funFormDataLoad(data[p]);
        } else {
            var d = $('input[name=' + p + '],select[name=' + p + '],textarea[name=' + p + ']')
                , type = d.prop('tagName')
                , val = data[p];
            ;
            if (d.length > 0) {
                if ('INPUT' == type) {
                    var formType = d.attr('type');
                    if (formType == 'text') {
                        if (d.hasClass('date-picker')) {
                            d.val(new Date(val).Format("yyyy年MM月dd日"));
                        } else {
                            d.val(val);
                        }
                    } else if (formType == 'number') {
                        d.val(val);
                    } else if (formType == 'radio') {
                        $('[name=' + p + '][value=' + val + ']').prop('checked', true);
                    } else if (formType == 'hidden') {
                        d.val(val);
                    }
                } else if ('SELECT' == type) {
                    d.attr('va', val);
                    d.find('option').attr({'selected': false});
                    d.find('option[value="' + val + '"]').attr({'selected': true});
                } else if ('TEXTAREA' == type) {
                    d.text(val).val(val);
                } else if ('RADIO' == type) {
                } else {
                    var role = d.data('role');
                    if ('echo' == role) {
                        d.text(val).val(val);
                    }
                }
            }
        }
    }
    layui.form.render();
}

function loadDeptName(nameInputId, deptIds) {
    globalSyncPost(WEB_ROOT + '/authGroup/findGroupNamesByIds', {
        groupType: 'dept',
        groupIds: deptIds
    }, function (result) {
        if (result && result.code == 1) {
            $('#' + nameInputId).val(result.data);
        }
    })
}

function loadDeptName2Html(nameInputId, deptIds) {
    globalSyncPost(WEB_ROOT + '/authGroup/findGroupNamesByIds', {
        groupType: 'dept',
        groupIds: deptIds
    }, function (result) {
        if (result && result.code == 1) {
            $('#' + nameInputId).html(result.data);
        }
    })
}

//格式化日期
function formatDate(rowData) {
    var dateType = "";
    var date = new Date();
    date.setTime(rowData[this.field]);
    dateType += date.getFullYear();  //年
    dateType += "-" + getMonth(date); //月
    dateType += "-" + getDay(date);  //日
    return dateType;
}

//返回 01-12 的月份值
function getMonth(date) {
    var month = "";
    month = date.getMonth() + 1; //getMonth()得到的月份是0-11
    if (month < 10) {
        month = "0" + month;
    }
    return month;
}

//返回01-30的日期
function getDay(date) {
    var day = "";
    day = date.getDate();
    if (day < 10) {
        day = "0" + day;
    }
    return day;
}
