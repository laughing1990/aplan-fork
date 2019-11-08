if (typeof gDrawingViewer === "undefined")
    gDrawingViewer = {};
/**
 *  参数设置内容
 *  @param Number speed = 1         浏览速度: 1极速浏览，2普通浏览，3高清浏览
 *  @param String scale = '1:200'   图纸比例尺
 *  @param Number between = 50      标记间距
 *  @param Number maxBetween = 500  标记最大间距
 *  @param Number marker = 100      标记数量
 *  @param Number maxMarker = 500   标记最大数量
 * */
gDrawingViewer.params = {
    speed: 2,
    scale: '1:200',
    between: 50,
    minBetween: 1,
    maxBetween: 500,
    marker: 100,
    minMarker: 1,
    maxMarker: 500
};

gDrawingViewer.OpenModel = function () {
    var modal = $("#check_params_config_modal");
    modal.on('hidden.bs.modal', $.proxy(function () {
        this.off('hidden.bs.modal');
        $('input', this).each(function () {
            $(this).removeClass('is-invalid').removeClass('is-valid');
        });
    }, modal));
    modal.on('shown.bs.modal', $.proxy(function () {
        this.off('shown.bs.modal');
        this.draggable();
        this.off('click', '.modal-footer>button.btn-primary').on('click', '.modal-footer>button.btn-primary', gDrawingViewer.SaveSetting);
        this.off('click', '.modal-footer>button.btn-light').on('click', '.modal-footer>button.btn-light', gDrawingViewer.CloseModel);
        this.off('keyup', 'input[type="tel"]').on('keyup', 'input[type="tel"]', function () {
            var minBetween = gDrawingViewer.params.minBetween,
                maxBetween = gDrawingViewer.params.maxBetween,
                minMarker = gDrawingViewer.params.minMarker,
                maxMarker = gDrawingViewer.params.maxMarker;
            if (!this.value) return;
            this.name === 'between' && this.value > maxBetween && (this.value = maxBetween);
            this.name === 'between' && this.value < minBetween && (this.value = minBetween);
            this.name === 'marker' && this.value > maxMarker && (this.value = maxMarker);
            this.name === 'marker' && this.value < minMarker && (this.value = minMarker);
            this.value = this.value >>> 0;
        });
        this.off('change', 'input[name="scale"]').on('change', 'input[name="scale"]', function () {
            var reg = new RegExp('\\d+:\\d+');
            var value = this.value;
            if (reg.test(value)) {
                this.value = value.split(':').map(function (chart) {
                    var num = (chart * 1);
                    return num === 0 ? 1 : Math.abs(num);
                }).join(':');
                return;
            }
            this.value = '1:' + (isNaN(value * 1) || (value * 1) === 0 ? 200 : Math.abs(value));
        });
        this.off('blur', 'input:not(input[type="radio"])').on('blur', 'input:not(input[type="radio"])', function () {
            !!this.value
            && $(this).removeClass('is-invalid').addClass('is-valid')
            || $(this).removeClass('is-valid').addClass('is-invalid');
        });
        var paramArray = $('#config_form').data('config');
        if (!paramArray || !paramArray.length) {
            paramArray = Object.keys(gDrawingViewer.params).map(function (key) {
                return {name: key, value: gDrawingViewer.params[key]}
            })
        }
        paramArray.forEach(function (per) {
            var target = $('input[name="' + per.name + '"]', '#config_form');
            if (!target.length) return;
            if (target.length > 1) {
                target.eq(Math.ceil(per.value) - 1).attr('checked', true);
            } else {
                target.val(per.value);
            }
        });
    }, modal)).modal("show");
}

gDrawingViewer.SaveSetting = function (e) {
    e && e.preventDefault();
    var form = $('#config_form');
    var paramArray = form.serializeArray();
    var isValid = paramArray.every(function (per) {
        if (per.name === 'speed') return !0;
        if (!per.value) {
            $('input[name="' + per.name + '"]', '#config_form').removeClass('is-valid').addClass('is-invalid').focus();
            console.log($('input[name="' + per.name + '"]', '#config_form'))
            return !!0;
        }
        return !0;
    }) || false;

    if (!isValid) return;
    paramArray.forEach(function (per) {
        gDrawingViewer.params.hasOwnProperty(per.name) && (gDrawingViewer.params[per.name] = per.value >>> 0 || per.value);
        $('input[name="' + per.name + '"]', '#config_form').removeClass('is-invalid').removeClass('is-valid');
    });
    console.log(gDrawingViewer.params);
    form.data('config', paramArray);
    gDrawingViewer.scale = gDrawingViewer.params.speed || 2;
    reloadPDFOverlay.apply(gDrawingViewer, [gDrawingViewer.page, gDrawingViewer.scale, gDrawingViewer.rotation, gDrawingViewer.map]);
    if (typeof agDrawingCompare.canvasSource !== 'undefined') {
        reloadPDFOverlay.apply(agDrawingCompare, [agDrawingCompare.page, gDrawingViewer.scale, gDrawingViewer.rotation, agDrawingCompare.mapControl]);
    }
    try {
        initPageToolbar();
        var current = agCompareBox.reload();
        current && current.trigger('change');
    } catch (err) {
        swal("出现异常", err.message, "error");
    } finally {
        gDrawingViewer.CloseModel();
    }
}

gDrawingViewer.CloseModel = function () {
    $("#check_params_config_modal").modal("hide");
}

gDrawingViewer.GetSetting = function () {
    return gDrawingViewer.params;
}

/**
 * 审查图纸单选清单
 * agReview
 * This modal support dragging
 * Example:
 * To show the modal agReview.init()
 * To hide the modal agReview.close()
 * To get the form  var paramsArray = agReview.save()
 * */
var agReview = {
    template: [''],
    init: function () {
        var modal = $('#review_modal')
        modal.on('shown.bs.modal', $.proxy(function () {
            this.off('shown.bs.modal');
            this.draggable();
            this.on('click', '.modal-footer>button.btn-primary', $.proxy(agReview, 'save'));
            this.on('click', '.modal-footer>button.btn-light', agReview.close);
        }, modal)).modal('show');
    },
    save: function (e) {
        e && e.preventDefault();
        var form = $('#review_form');
        var paramArray = form.serializeArray();
        this.close();
        console.log(paramArray);
        return paramArray
    },
    close: function () {
        $('#review_modal').modal('hide');
    }
}

/**
 * 参考图纸列表
 * agDrawings
 * This modal support dragging
 * Example:
 * To show the modal agDrawings.init()
 * To hide the modal agDrawings.close()
 * */
var agDrawings = {
    template: '',
    drawingListTable: null,
    profession: null,
    // itemId: "50b40244-0d8e-464a-b6d7-1c5240646f71",
    itemId: "",
    init: function () {
        var modal = $('#drawings_modal')
        modal.on('shown.bs.modal', $.proxy(function () {
            this.off('shown.bs.modal');
            this.draggable();
            agDrawings.loadProfession(ctx + '/bsc/dic/code/listBscDicCodeItemByDicTypeCode.do?dicCodeTypeCode=DRAWING_PROFESSIONAL', '#drawingProfession', 1, 'profession');
        }, modal)).modal('show');
    },
    close: function () {
        $('#drawings_modal').modal('hide');
    },
    loadProfession: function (url, selectTagId, value, name) {
        $(selectTagId).empty();
        $.ajax({
            type: 'post',
            url: url,
            dataType: "Json",
            success: function (data) {
                if (data != '' && data.length > 0) {
                    var html = "";
                    $.each(data, function (i, n) {
                        // if (i % 7 == 0) {
                        //     html = "<div class=\"input-group\">"
                        // }
                        // html += "<div class=\"m-checkbox-inline\">";
                        html += "   <label class=\"m-radio m-radio--solid m-radio--brand\">";
                        if (value == n['itemCode']) {
                            html += "<input name=\"" + name + "\" type=\"radio\" checked value=\"" + n['itemCode'] + "\" showName=\"" + n['itemName'] + "\">" + n['itemName'];
                        } else {
                            html += "<input name=\"" + name + "\" type=\"radio\" value=\"" + n['itemCode'] + "\" showName=\"" + n['itemName'] + "\">" + n['itemName'];
                        }
                        html += "<span></span></label>";// +
                        //"</div>";
                        if ((i + 1) % 7 == 0 || (i + 1) == data.length || data.length == 0) {
                            //html += "</div>";
                            $(selectTagId).append(html);
                        }
                    });
                }

                agDrawings.profession = value;
                $("#drawings_form input[name='" + name + "']").unbind("change").bind("change", function () {
                    agDrawings.changeType($(this).val());
                });
                agDrawings.loadDrawings();
            }
        })
    },

    loadDrawings: function () {
        var idParm = {
            "detailId": agDrawings.itemId,
            "type": agDrawings.profession
        };
        if (agDrawings.drawingListTable != null)
            agDrawings.drawingListTable.bootstrapTable("destroy");
        agDrawings.drawingListTable = $('#drawingListTable').bootstrapTable({
            url: ctx + '/cod/drawing/listCodDrawingByDetailId.do',
            type: "POST",
            dataType: "json",
            columns: [
                {
                    checkbox: "true",
                    width: "5%"
                },
                {
                    title: '编号',
                    width: "10%",
                    align: 'center',
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        // var pageNumber = $('#drawingListTable').bootstrapTable('getOptions').pageNumber;
                        // var pageSize = $('#drawingListTable').bootstrapTable('getOptions').pageSize;
                        // return (pageNumber - 1) * pageSize + index + 1;
                        return index + 1;
                    }
                },
                {
                    field: "fullName",
                    title: "图纸名称",
                    width: "45%",
                    align: 'center',
                    valign: 'middle'
                },
                {
                    field: "code",
                    title: "图号",
                    visible: false
                },
                {
                    field: '_operate', // 数据字段
                    title: '操作', // 页面字段显示
                    sortable: false, // 禁用排序
                    width: "40%",  // 宽度,单位: px
                    align: 'center', // 字段列标题和数据排列方式
                    valign: 'middle',
                    formatter: function (value, row, index) {
                        var btn;
                        var func = "agDrawings.setCurrent";
                        //class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"
                        btn = '<a href="javascript:void(0);" style="padding: 0 3px" onclick="' + func + '(\'' + '' + '\'' + ');">置为当前</a>';
                        func = "agDrawings.locationDrawing";
                        btn += '<a href="javascript:void(0);" style="padding: 0 3px" onclick="' + func + '(\'' + '' + '\'' + ');" >定位图纸</a>';
                        func = "agDrawings.openInNewWindow";
                        btn += '<a href="javascript:void(0);" style="padding: 0 3px" onclick="' + func + '(\'' + '' + '\'' + ');" >新页面打开</a>';
                        return btn;
                    }
                }
            ],

            sortOrder: "asc",
            pagination: true,
            width: '100%',
            cache: false,
            pageSize: 5,
            // pageList: [10, 20, 50, 100],
            paginationHAlign: 'right',
            paginationDetailHAlign: "right",
            paginationVAlign: 'bottom',
            paginationShowPageGo: true,
            formatShowingRows: function (pageFrom, pageTo, totalRows) {
                return '显示第 ' + pageFrom + ' 到第 ' + pageTo + ' 条记录，共 ' + totalRows + ' 条记录'
            },
            sidePagination: "client",
            queryParams: idParm
        });
    },
    changeType: function (profession) {
        agDrawings.profession = profession;
        agDrawings.loadDrawings();
    },
    setCurrent: function (drawingPath) {

    },
    locationDrawing: function () {

    },
    openInNewWindow: function (drawingPath) {

    }
}

/**
 * 图纸对比选项框
 * agCompareBox
 * This modal support dragging
 * Example:
 * To show the modal agCompareBox.init()
 * To hide the modal agCompareBox.close()
 * */
var agCompareBox = {
    template: '',
    self: {},
    init: function () {
        this.self = $('#aside_modal');
        this.self.on('shown.bs.modal', $.proxy(function () {
            this.off('shown.bs.modal');
            this.draggable({
                start: function () {
                    $(this).css({bottom: 'auto', right: 'auto'});
                }
            });
            $('input', '.ag-checkbox-dsp').on('change', function () {
                var index = $(this).val();
                $('.ag-filename-dsp p').eq(index).toggleClass('hidden');
            });
            $('input', '.ag-radio-dsp').on('change', function () {
                var target = $(this);
                var active = target.val();
                var dBase = target.data('base');
                var dDsp = target.data('dsp');

                removeDspMarkers();
                agDrawingCompare.removeContours();
                if (dBase && dDsp) {
                    active === 'spot' && $.loading({
                        onOpen: function () {
                            agDrawingCompare.uploadDrawing(gDrawingViewer, agDrawingCompare, agDrawingCompare.mapControl, 'leaflet-green-icon', dDsp)
                                .done($.proxy(agCompareBox._pointHandler, target, 'dsp', 6, agCompareBox._panToHandler));
                            agDrawingCompare.uploadDrawing(agDrawingCompare, gDrawingViewer, map, 'leaflet-red-icon', dBase)
                                .done($.proxy(agCompareBox._pointHandler, target, 'base', 6, agCompareBox._panToHandler));
                        }
                    });
                    active === 'line' && $.loading({
                        onOpen: function () {
                            compareDrawingContours(gDrawingViewer, agDrawingCompare, agDrawingCompare.mapControl, ['#218838', '#21883880'], dDsp)
                                .done($.proxy(agCompareBox._pointHandler, target, 'dsp', 6, agCompareBox._polygonHandler, agDrawingCompare));
                            compareDrawingContours(agDrawingCompare, gDrawingViewer, map, ['#c82333', '#dc354580'], dBase)
                                .done($.proxy(agCompareBox._pointHandler, target, 'base', 6, agCompareBox._polygonHandler, gDrawingViewer));
                        }
                    });
                } else {
                    active === 'spot' && $.loading({
                        onOpen: function () {
                            agDrawingCompare.uploadDrawing(gDrawingViewer, agDrawingCompare, agDrawingCompare.mapControl, 'leaflet-green-icon')
                                .done($.proxy(agCompareBox._pointHandler, target, 'dsp', 6, agCompareBox._panToHandler));
                            agDrawingCompare.uploadDrawing(agDrawingCompare, gDrawingViewer, map, 'leaflet-red-icon')
                                .done($.proxy(agCompareBox._pointHandler, target, 'base', 6, agCompareBox._panToHandler));
                        }
                    });
                    active === 'line' && $.loading({
                        onOpen: function () {
                            compareDrawingContours(gDrawingViewer, agDrawingCompare, agDrawingCompare.mapControl, ['#218838', '#21883880'])
                                .done($.proxy(agCompareBox._pointHandler, target, 'dsp', 6, agCompareBox._polygonHandler, agDrawingCompare));
                            compareDrawingContours(agDrawingCompare, gDrawingViewer, map, ['#c82333', '#dc354580'])
                                .done($.proxy(agCompareBox._pointHandler, target, 'base', 6, agCompareBox._polygonHandler, gDrawingViewer));
                        }
                    });
                }
            });
        }, this.self)).modal('show');
        this.self.on('hide.bs.modal', function () {
            $(this).css({top: 'auto', left: 'auto', bottom: '0', right: '0'});
            $('input', '.ag-radio-dsp').each(function () {
                $(this).removeData();
            });
        })
    },
    close: function () {
        this.self.modal('hide');
        this.reload();
    },
    _pointHandler: function (key, size, fn) {
        var args = [].slice.call(arguments, 3);
        var value = [].slice.call(arguments, -1)[0];
        this.data(key, value);
        var parent = this.closest('form');
        var _className = key === 'dsp' ? '.badge-success' : '.badge-danger';
        var $badge = parent.find(_className);
        $badge.text(value.length);
        var $nav = $badge.next();
        $nav.data(key, value).find('li:not(:last-of-type):not(:first-of-type)').remove();
        var idx = 0;
        var items = value.slice(0, size).map(function (item) {
            var _pan = Object.values(item).join(',');
            return ['<li class="page-item"><a class="page-link" href="#" data-idx="', idx, '" data-pan-to="', _pan, '">', ++idx, '</a></li>'].join('')
        }).join('');
        $nav.find('li:last-of-type').removeClass('disabled').data('next-index', idx);
        var $first = $nav.find('li:first-of-type').addClass('disabled').data('prev-index', 1);
        $(items).insertAfter($first);
        $nav.off('click', 'a.page-link').on('click', 'a.page-link', {_value: value, _args: args}, function (evt) {
            var value = evt.data._value;
            var args = evt.data._args;
            if ($(this).hasClass('disabled')) return;
            var parent = $(this).closest('li');
            if ($(this).hasClass('ag-next')) {
                var nextIndex = parent.data('next-index') * 1 || 8;
                if (nextIndex >= value.length) return;
                parent.siblings().eq(0).removeClass('disabled').data('prev-index', nextIndex);
                parent.siblings().not(':first-child').remove();
                var items = value.slice(nextIndex, nextIndex + size > value.length ? value.length : nextIndex + size)
                    .map(function (item) {
                        var _pan = Object.values(item).join(',');
                        return ['<li class="page-item"><a class="page-link" href="#"  data-idx="', nextIndex, '" data-pan-to="', _pan, '">', ++nextIndex, '</a></li>'].join('')
                    }).join('');
                $(items).insertBefore(parent);
                parent.data('next-index', nextIndex);
                if (nextIndex >= value.length) {
                    parent.addClass('disabled');
                }
                return;
            }
            if ($(this).hasClass('ag-prev')) {
                var prevIndex = parent.data('prev-index') * 1 || 0;
                if (prevIndex <= 0) return;
                if (prevIndex < size) {
                    prevIndex = size;
                }
                parent.siblings().last().removeClass('disabled').data('next-index', prevIndex);
                parent.siblings().not(':last-child').remove();

                var rang = prevIndex - size <= 0 ? 0 : prevIndex - size;
                parent.data('prev-index', rang);
                if (rang <= 0) {
                    parent.addClass('disabled');
                }
                var items = value.slice(rang, prevIndex).map(function (item) {
                    var _pan = Object.values(item).join(',');
                    return ['<li class="page-item"><a class="page-link" href="#" data-idx="', rang, '" data-pan-to="', _pan, '">', ++rang, '</a></li>'].join('')
                }).join('');
                $(items).insertAfter(parent);
                return;
            }
            if ($.isFunction(fn)) {
                return fn.apply(this, args);
            }
        });
    },
    _panToHandler: function () {
        var point = $(this).data('pan-to').split(',').map(function (point) {
            return point * 1
        });
        var xy = function (point) {
            var lat = point[0] - gDrawingViewer.width / 2;
            var lng = point[1] - gDrawingViewer.height / 2;
            //旋转90度
            var latlng = [-lng, lat];
            if (L.Util.isArray(point)) {
                return latlng;
            }
            return latlng;
        }
        agDrawingCompare.mapControl.panTo(xy(point));
        map.panTo(xy(point));
    },
    _polygonHandler: function (viewer) {
        var polygonIdx = $(this).data('idx') * 1;
        map.fitBounds(viewer.polygons[polygonIdx].getBounds(), {maxZoom: map.getZoom()});
        agDrawingCompare.mapControl.fitBounds(viewer.polygons[polygonIdx].getBounds(), {maxZoom: agDrawingCompare.mapControl.getZoom()});
    },
    reload: function () {
        var parent = this.self || $('#aside_modal');
        var current = '';
        $('input[name="dspmodal"]', parent).each(function () {
            $(this).removeData();
            if ($(this).is(':checked')) {
                current = $(this);
            }
        });
        return current;
    }
}
