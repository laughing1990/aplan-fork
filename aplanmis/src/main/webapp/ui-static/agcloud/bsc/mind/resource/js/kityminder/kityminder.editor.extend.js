angular.module('kityminderEditor').run(['$templateCache', function ($templateCache) {
    'use strict';
    //var isMax = false;
    var isMax = UrlParam.param('maximize');
    if (currentBusiScene == BUSI_SCENE_CONFIG_SITUATION) {
        if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {
            $templateCache.put('ui/directive/topTab/topTab.html',
                "<div class='tab-content'>" +
                "<buttons minder=\"minder\"></buttons>" +
                "<undo-redo editor=\"editor\"></undo-redo>" +
                "<append-node minder=\"minder\"></append-node>" +
                "<arrange minder=\"minder\"></arrange>" +
                "<operation minder=\"minder\"></operation>" +
                "<must-select minder=\"minder\"></must-select>" +
                "<is-more minder=\"minder\"></is-more>" +
                "<link-process-start minder=\"minder\"></link-process-start>" +
                '<ul class="km-priority tool-group ng-isolate-scope" ng-disabled="commandDisabled"' +
                '    style="margin-right: 15px;;width: 60px;margin-right: 15px;" minder="minder">' +
                "<importfile-xmind minder=\"minder\"></importfile-xmind>" +
                "<exportfile-xmind minder=\"minder\"></exportfile-xmind>" +
                '</ul>' +
                "<note-btn minder=\"minder\"></note-btn>" +
                // "<link-form4situation minder=\"minder\"></link-form4situation>" +
                "<add-mat4situation minder=\"minder\"></add-mat4situation>" +
                "<link-item4mat minder=\"minder\"></link-item4mat>" +
                "<link-item4situation minder=\"minder\"></link-item4situation>" +
                // "<import-mat minder=\"minder\"></import-mat>" +
                // "<import-cert minder=\"minder\"></import-cert>" +
                "<mind-filter minder=\"minder\"></mind-filter>" +
                "</div>"
            );
        }
        else if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) {
            $templateCache.put('ui/directive/topTab/topTab.html',
                "<div class='tab-content'>" +
                "<buttons minder=\"minder\"></buttons>" +
                "<undo-redo editor=\"editor\"></undo-redo>" +
                "<append-node minder=\"minder\"></append-node>" +
                "<arrange minder=\"minder\"></arrange>" +
                "<operation minder=\"minder\"></operation>" +
                "<must-select minder=\"minder\"></must-select>" +
                "<is-more minder=\"minder\"></is-more>" +
                "<link-process-start minder=\"minder\"></link-process-start>" +
                "<is-inform-commit minder=\"minder\"></is-inform-commit>" +
                '<ul class="km-priority tool-group ng-isolate-scope" ng-disabled="commandDisabled"' +
                '    style="margin-right: 15px;;width: 60px;margin-right: 15px;" minder="minder">' +
                "<importfile-xmind minder=\"minder\"></importfile-xmind>" +
                "<exportfile-xmind minder=\"minder\"></exportfile-xmind>" +
                '</ul>' +
                "<note-btn minder=\"minder\"></note-btn>" +
                // "<link-form4situation minder=\"minder\"></link-form4situation>" +
                "<add-mat4situation minder=\"minder\"></add-mat4situation>" +
                // "<import-mat minder=\"minder\"></import-mat>" +
                // "<import-cert minder=\"minder\"></import-cert>" +
                "<state-version minder=\"minder\"></state-version>" +
                "<mind-filter minder=\"minder\"></mind-filter>" +
                "</div>"
            );
        }
    }
    else if (currentBusiScene == BUSI_SCENE_CONFIG_PRECONDITION) {
        if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM) {
            $templateCache.put('ui/directive/topTab/topTab.html',
                "<div class='tab-content'>" +
                "<buttons minder=\"minder\"></buttons>" +
                "<undo-redo editor=\"editor\"></undo-redo>" +
                "<append-node minder=\"minder\"></append-node>" +
                "<arrange minder=\"minder\"></arrange>" +
                "<operation minder=\"minder\"></operation>" +
                "<terminate-situation minder=\"minder\"></terminate-situation>" +
                "<situation-answer-num minder=\"minder\"></situation-answer-num>" +
                "</div>"
            );
        }
    }

    $templateCache.put('ui/directive/appendNode/appendNode.html',
        "<div class=\"km-btn-group append-group\" style='width: 111px'>" +
        "<div class=\"km-btn-item append-child-node\" ng-disabled=\"minder.queryCommandState('AppendChildNode') === -1\" ng-click=\"minder.queryCommandState('AppendChildNode') === -1 || execCommand('AppendChildNode')\" title=\"{{ 'appendchildnode' | lang:'ui/command' }}\"><i class=\"km-btn-icon\"></i> <span class=\"km-btn-caption\">{{ 'appendchildnode' | lang:'ui/command' }}</span></div>" +
        "<div class=\"km-btn-item append-parent-node\" ng-disabled=\"minder.queryCommandState('AppendParentNode') === -1\" ng-click=\"minder.queryCommandState('AppendParentNode') === -1 || execCommand('AppendParentNode')\" title=\"{{ 'appendparentnode' | lang:'ui/command' }}\"><i class=\"km-btn-icon\"></i> <span class=\"km-btn-caption\">{{ 'appendparentnode' | lang:'ui/command' }}</span></div>" +
        "</div>"
    );


    $templateCache.put('ui/directive/noteEditor/noteEditor.html',
        "<div class=\"panel panel-default\" ng-init=\"noteEditorOpen = false\" ng-show=\"noteEditorOpen\"><div class=\"panel-heading\"><h3 class=\"panel-title\">备注</h3> <i class=\"close-note-editor glyphicon glyphicon-remove\" ng-click=\"closeNoteEditor()\"></i></div><div class=\"panel-body\"><div ng-show=\"noteEnabled\" ui-codemirror=\"{ onLoad: codemirrorLoaded }\" ng-model=\"noteContent\" ui-codemirror-opts=\"{\n" +
        "                gfm: true,\n" +
        "                breaks: true,\n" +
        "                lineWrapping : true,\n" +
        "                mode: 'gfm',\n" +
        "                dragDrop: false,\n" +
        "                lineNumbers:true\n" +
        "             }\"></div><p ng-show=\"!noteEnabled\" class=\"km-note-tips\">请选择节点编辑备注</p></div></div>"
    );

    $templateCache.put('ui/directive/isMore/isMore.html',
        "<ul class=\"km-priority tool-group\" ng-disabled=\"commandDisabled\" style='width:60px;'>" +
        "<li class=\"km-priority-item tool-group-item\" ng-repeat=\"p in priorities\" ng-click=\"commandDisabled || minder.execCommand('priority', p)\" ng-class=\"{ active: commandValue == p }\" title=\"{{ getPriorityTitle(p) }}\">" +
        "<div class=\"{{ getPriorityClass(p) }}\" title='{{ getPriorityTitle(p) }}'><div style='height: 3px;'></div>{{ getPriorityTitle(p) }}</div>" +
        "</li></ul>"
    );

    $templateCache.put('ui/directive/linkProcessStart/linkProcessStart.html',
        "<ul class=\"km-priority tool-group\" ng-disabled=\"minder.queryCommandState('NodeTypeCodeStateCommand') != 'situation'\" style='width:60px;'>" +
        "<li class=\"km-priority-item tool-group-item\" ng-repeat=\"p in linkfieldes\" ng-click=\"(minder.queryCommandState('NodeTypeCodeStateCommand') != 'situation') || minder.execCommand('linkProcessStart', p)\" ng-class=\"{ active: commandValue == p }\" title=\"{{ getInnerTitle(p) }}\">" +
        "<div class=\"{{ getInnerClass(p) }}\" title='{{ getInnerTitle(p) }}'><div style='height: 3px;'></div>{{ getInnerTitle(p) }}</div>" +
        "</li></ul>"
    );

    $templateCache.put('ui/directive/isInformCommit/isInformCommit.html',
        "<ul class=\"km-priority tool-group\" ng-disabled=\"minder.queryCommandState('NodeTypeCodeStateCommand') != 'situation'\" style='width:60px;'>" +
        "<li class=\"km-priority-item tool-group-item\" ng-repeat=\"p in linkfieldes\" ng-click=\"(minder.queryCommandState('NodeTypeCodeStateCommand') != 'situation') || minder.execCommand('isInformCommit', p)\" ng-class=\"{ active: commandValue == p }\" title=\"{{ getInnerTitle(p) }}\">" +
        "<div class=\"{{ getInnerClass(p) }}\" title='{{ getInnerTitle(p) }}'><div style='height: 3px;'></div>{{ getInnerTitle(p) }}</div>" +
        "</li></ul>"
    );

    $templateCache.put('ui/directive/terminateSituation/terminateSituation.html',
        "<ul class=\"km-priority tool-group\" ng-disabled=\"commandDisabled\" style='width:60px;'>" +
        "<li class=\"km-priority-item tool-group-item\" ng-repeat=\"p in linkfieldes\" ng-click=\"commandDisabled || minder.execCommand('terminateSituation', p)\" ng-class=\"{ active: commandValue == p }\" title=\"{{ getInnerTitle(p) }}\">" +
        "<div class=\"{{ getInnerClass(p) }}\" title='{{ getInnerTitle(p) }}'><div style='height: 3px;'></div>{{ getInnerTitle(p) }}</div>" +
        "</li></ul>"
    );

    $templateCache.put('ui/directive/situationAnswerNum/situationAnswerNum.html',
        "<ul class=\"km-priority tool-group\" ng-disabled=\"commandDisabled\" style='width:200px;'>" +
        "<li class=\"km-priority-item tool-group-item\" ng-repeat=\"p in linkfieldes\" ng-click=\"commandDisabled || minder.execCommand('situationAnswerNum', p)\" ng-class=\"{ active: commandValue == p }\" title=\"{{ getInnerTitle(p) }}\">" +
        "<div class=\"{{ getInnerClass(p) }}\" title='{{ getInnerTitle(p) }}'><div style='height: 3px;'></div>{{ getInnerTitle(p) }}</div>" +
        "</li></ul>"
    );

    //窗口关闭按钮
    $templateCache.put('ui/directive/layclose/layclose.html',
        "<div class=\"readjust-layout\"><a ng-click=\"close()\" class=\"btn-wrap\"><span class=\"btn-icon iconfont layui-extend-close\" style=\"font-size: 20px;text-align: center;\"></span> <span class=\"btn-label\">关闭窗口</span></a></div>"
    );

    //窗口最大化按钮
    $templateCache.put('ui/directive/maximization/maximization.html',
        "<div class=\"readjust-layout\"><a ng-click=\"maximization()\" class=\"btn-wrap\" ng-disabled=\"minder.queryCommandState('resetlayout') === -1\"><span class=\"btn-icon iconfont layui-extend-maximization\" style=\"font-size: 20px;text-align: center;\"></span> <span class=\"btn-label\">{{ 'maxLayout' | lang: 'ui/command' }}</span></a></div>"
    );

    //复制情形
    $templateCache.put('ui/directive/copySituation/copySituation.html',
        "<div class=\"readjust-layout\"><a ng-click=\"copySituation()\" class=\"btn-wrap\"><span class=\"btn-icon iconfont layui-extend-batchSave1\" style=\"font-size: 20px;text-align: center;\"></span> <span class=\"btn-label\">复制情形</span></a></div>"
    );

    //保存按钮
    /*dropdown-toggle*/
    $templateCache.put('ui/directive/buttons/buttons.html',
        "<div class=\"dropdown theme-panel\" style=\"float:left;\" dropdown><div class=\" theme-item-selected\" style=\"width: 100px;padding: 0;border:none;\" dropdown-toggle ng-disabled=\"minder.queryCommandState('theme') === -1\"><a style=\"width: 100px;display:flex;flex-direction: column;line-height: 17px;\" href class=\"theme-item\" ng-style=\"getThemeThumbStyle(minder.queryCommandValue('theme'))\" ng-click=\"addHyperlink()\" title=\"全部保存\"><i class=\"iconfont layui-extend-save\" style='color:#5cb8ff;'></i><span>全部保存</span></a></div></div>"
    );

    function getHtmlMindFilter() {

        var html = '';
        html += '<div id="mindFilter" class="dropdown theme-panel ng-isolate-scope">';
        html += '<div class="checkbox">';

        if (currentBusiScene == BUSI_SCENE_CONFIG_SITUATION) {
            if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE) {

                html += '      <label class="m-checkbox m-checkbox--solid m-checkbox--brand" style="">';
                html += '        <input bindShow="showSituationLinkItem" type="checkbox" class="original-file">';
                html += '         显示事项';
                html += '        <span></span>';
                html += '      </label>';
            }
        }

        html += '	   <label class="m-checkbox m-checkbox--solid m-checkbox--brand" style="">';
        html += '        <input bindShow="showMat" type="checkbox" class="original-file" checked="">';
        html += '         显示普通材料';
        html += '        <span></span>';
        html += '      </label>';

        html += '      <label class="m-checkbox m-checkbox--solid m-checkbox--brand" style="">';
        html += '        <input bindShow="showCert" type="checkbox" class="original-file" checked="">';
        html += '         显示证照材料';
        html += '        <span></span>';
        html += '      </label>';

        html += '      <label class="m-checkbox m-checkbox--solid m-checkbox--brand" style="">';
        html += '        <input bindShow="showForm" type="checkbox" class="original-file">';
        html += '         显示在线表单材料';
        html += '        <span></span>';
        html += '      </label>';
        html += '</div>';
        html += '</div> ';
        return html;
    }

    if(handWay=='1') {
        $templateCache.put('ui/directive/mindFilter/mindFilter.html',
            getHtmlMindFilter()
        );
    }

    //预览按钮
    $templateCache.put('ui/directive/previewButton/previewButton.html',
        "<div class=\"dropdown theme-panel\" style=\"width: 120px;float:right;\" dropdown><div class=\"dropdown-toggle theme-item-selected\" style=\"width: 120px;border:1px solid #eff3fa;\" dropdown-toggle ng-disabled=\"minder.queryCommandState('theme') === -1\"><a style=\"width: 100px;\" href class=\"theme-item\" ng-style=\"getThemeThumbStyle(minder.queryCommandValue('theme'))\" ng-click=\"preview()\" title=\"预览\"><i class=\"iconfont layui-extend-siutation-preview\"></i> 预览</a></div></div>"
    );

    $templateCache.put('ui/directive/mustSelect/mustSelect.html',
        "<ul class=\"km-progress tool-group\" ng-disabled=\"commandDisabled\" style='width: 60px;'><li class=\"km-progress-item tool-group-item\" ng-repeat=\"p in progresses\" ng-click=\"commandDisabled || minder.execCommand('progress', p)\" ng-class=\"{ active: commandValue == p }\" title=\"{{ getProgressTitle(p) }}\"><div class=\"km-progress-icon tool-group-icon progress-{{p}}\"><div style='height: 3px;'></div>{{getProgressTitle(p)}}</div></li></ul>"
    );

    $templateCache.put('ui/directive/question/question.html',
        "<div class=\"btn-group-vertical\" dropdown is-open=\"isopen\"><button style=\"width: 100px;\" type=\"button\" class=\"btn btn-default hyperlink\" title=\"{{ 'link' | lang:'ui' }}\" ng-class=\"{'active': isopen}\" ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\"></button> <button type=\"button\" style=\"width: 100px;\" class=\"btn btn-default hyperlink-caption dropdown-toggle\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\" title=\"{{ 'link' | lang:'ui' }}\" dropdown-toggle><span class=\"caption\">{{ 'link' | lang:'ui' }}</span> <span class=\"caret\"></span> <span class=\"sr-only\">{{ 'link' | lang:'ui' }}</span></button><ul class=\"dropdown-menu\" role=\"menu\"><li><a href ng-click=\"addHyperlink()\">{{ 'insertlink' | lang:'ui' }}</a></li><li><a href ng-click=\"minder.execCommand('HyperLink', null)\">{{ 'removelink' | lang:'ui' }}</a></li></ul></div>"
    );

    $templateCache.put('ui/dialog/question/question.tpl.html',
        "<div class=\"modal-header\"><h3 class=\"modal-title\">下一级问题</h3></div>" +
        "<div class=\"modal-body\">" +
        "<form>" +
        "<div class=\"form-group\" ng-class=\"{'has-success' : titlePassed}\"><label for=\"link-title\">问题：</label>" +
        "<input type=\"text\" class=\"form-control\" ng-model=\"title\" ng-blur=\"titlePassed = true\" id=\"link-title\" " +
        "placeholder=\"选填：鼠标在链接上悬停时提示的文本\"></div></form></div><div class=\"modal-footer\">" +
        "<button class=\"btn btn-primary\" ng-click=\"ok()\">确定</button> <button class=\"btn btn-warning\" ng-click=\"cancel()\">取消</button></div>"
    );

    $templateCache.put('ui/directive/linkItem4situation/linkItem4situation.html',
        "<div class=\"btn-group-vertical note-btn-group\" dropdown"+getToolbarBtnState("situation")+">" +
        "<div class='d-flex flex-column align-items-center' style=\"width: 80px;cursor: pointer;\" class=\"btn btn-default \" title=\"关联情形对应事项\"  ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('nodeSelectModCmd') === -1\"> " +
        "<span class=\"caption\"><i class=\"fa fa-file-archive-o\" style='color: #5cb8ff;font-size: 20px;'></i></span>" +
        "<span style=';'>{{ 'linkItem4situation' | lang:'ui' }}</span>" +
        "</div>" +
        "</div>"
    );

    // 情形表单
    // $templateCache.put('ui/directive/linkForm4situation/linkForm4situation.html',
    //     "<div class=\"btn-group-vertical note-btn-group\" dropdown" + getToolbarBtnState("situation") + ">" +
    //     "<div class='d-flex flex-column align-items-center' style=\"width: 80px;cursor: pointer;\" class=\"btn btn-default \" title=\"关联情形对应事项\" ng-class=\"{'active': isopen}\" ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\"> " +
    //     "<span class=\"caption\"><i class=\"fa fa-file-text-o\" style='color: #5cb8ff;font-size: 20px;'></i></span>" +
    //     "<span style=';'>{{ 'linkForm4situation' | lang:'ui' }}</span>" +
    //     "</div>" +
    //     "</div>"
    // );

    if(handWay=='1') {

        // 材料事项
        $templateCache.put('ui/directive/linkItem4mat/linkItem4mat.html',
            "<div class=\"btn-group-vertical note-btn-group\" dropdown" + getToolbarBtnState("mat") +">" +
            "<div class='d-flex flex-column align-items-center'style=\"width: 80px;cursor: pointer;\"  title=\"关联材料对应事项\" ng-class=\"{'active': isopen}\" ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\"> " +
            "<span class=\"caption\"><i class=\"fa fa-file-archive-o\" style='font-size: 21px;'></i></span>" +
            "<span style=';'>{{ 'linkItem4mat' | lang:'ui' }}</span>" +
            "</div>" +
            "</div>"
        );

        // 阶段材料  " + getToolbarBtnState("situation") + "
        $templateCache.put('ui/directive/addMat4stage/addMat4stage.html',
            "<div class=\"btn-group-vertical note-btn-group\" dropdown>" +
            "<div style=\"width: 80px;\" type=\"button\" class=\"btn btn-default \" title=\"添加{{ 'addMat4stage' | lang:'ui' }}\" ng-class=\"{'active': isopen}\" ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\"> " +
            "<span class=\"caption\"><i class=\"la la-plus-square-o\" style='font-size: 27px;'></i></span>" +
            "<span>{{ 'addMat4stage' | lang:'ui' }}</span>" +
            "</div>" +
            "</div>"
        );

        // 阶段情形材料  " + getToolbarBtnState("situation") + "
        $templateCache.put('ui/directive/addMat4situation/addMat4situation.html',
            "<div class=\"btn-group-vertical note-btn-group\" dropdown>" +
            "<div  class='d-flex flex-column align-items-center' style=\"width: 80px;cursor: pointer;\"   title=\"添加{{ 'addMat4situation' | lang:'ui' }}\" ng-class=\"{'active': isopen}\" ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\"> " +
            "<span class=\"caption\"><i class=\"la la-plus-square-o\" style='font-size: 27px;'></i></span>" +
            "<span>{{ 'addMat4situation' | lang:'ui' }}</span>" +
            "</div>" +
            "</div>"
        );

        $templateCache.put('ui/directive/importMat/importMat.html',
            "<div class=\"btn-group-vertical note-btn-group\" dropdown" + getToolbarBtnState("situation") + ">" +
            "<div class='d-flex flex-column align-items-center' style=\"width: 80px;border:none;cursor: pointer;\" title=\"导入{{ 'importMat' | lang:'ui' }}\" ng-class=\"{'active': isopen}\" ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\"> " +
            "<span class=\"caption\"><i class=\"la la-plus-square-o\" style='font-size: 27px;'></i></span>" +
            "<span style='font-size: 12px;color:#595959;'>{{ 'importMat' | lang:'ui' }}</span>" +
            "</div>" +
            "</div>"
        );

        // $templateCache.put('ui/directive/importCert/importCert.html',
        //     "<div class=\"btn-group-vertical note-btn-group\" dropdown" + getToolbarBtnState("situation") + ">" +
        //     "<div class='d-flex flex-column align-items-center' style=\"width: 80px;border:none;cursor: pointer;\"   title=\"导入{{ 'importCert' | lang:'ui' }}\" ng-class=\"{'active': isopen}\" ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\"> " +
        //     "<span class=\"caption\"><i class=\"fa fa-vcard-o\" style='font-size: 20px;'></i></span>" +
        //     "<span  style='color:#595959;;'>{{ 'importCert' | lang:'ui' }}</span>" +
        //     "</div>" +
        //     "</div>"
        // );
    }

    $templateCache.put('ui/directive/stateVersion/historicVersions.html',
        "<div class=\"btn-group-vertical note-btn-group\" dropdown is-open=\"isopen\">" +
        "<div class='d-flex flex-column align-items-center' style=\"width: 80px;border:none;cursor: pointer;\"   title=\"查看{{ 'stateVersion' | lang:'ui' }}\" ng-class=\"{'active': isopen}\" ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\"> " +
        "<span class=\"caption\"><i class=\"fa fa-vcard-o\" style='font-size: 20px;'></i></span>" +
        "<span  style='color:#595959;;'>{{ 'stateVersion' | lang:'ui' }}</span>" +
        "</div>" +
        "</div>"
    );

    $templateCache.put('ui/directive/importfileXmind/importfileXmind.html',
        "<div class=\"btn-group-vertical note-btn-group\" dropdown is-open=\"isopen\">" +
        "<button style=\"width: 70px;border:none;\" type=\"button\" class=\"btn btn-default \" title=\"导入Xmind文件\" ng-class=\"{'active': isopen}\" ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\"> " +
        "<span class=\"caption\"  style='font-size: 12px;color:#595959;'><img src=\"" + ctx + "/ui-static/agcloud/bsc/mind/resource/images/app/import.png\" /> 导入</span>" +
        "</button></div>"
    );
    $templateCache.put('ui/directive/exportfileXmind/exportfileXmind.html',
        "<div class=\"btn-group-vertical note-btn-group\" dropdown is-open=\"isopen\">" +
        "<button style=\"width: 70px;border:none;\" type=\"button\" class=\"btn btn-default \" title=\"导出Xmind文件\" ng-class=\"{'active': isopen}\" ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\"> " +
        "<span class=\"caption\"  style='font-size: 12px;color:#595959;'><img src=\"" + ctx + "/ui-static/agcloud/bsc/mind/resource/images/app/export.png\" /> 导出</span>" +
        "</button></div>"
    );
    $templateCache.put('ui/directive/appendCl/appendCl.html',
        "<div class=\"btn-group-vertical note-btn-group\" dropdown is-open=\"isopen\">" +
        "<button style=\"width: 100px;\" type=\"button\" class=\"btn btn-default \" title=\"{{ 'link' | lang:'ui' }}\" ng-class=\"{'active': isopen}\" ng-click=\"addHyperlink()\" ng-disabled=\"minder.queryCommandState('HyperLink') === -1\"> " +
        "<span class=\"caption\"  style='font-size: 12px;color:#595959;'><i class=\"iconfont layui-extend-material-setting\"></i>{{ 'link' | lang:'ui' }}</span>" +
        "</button></div>"
    );


    $templateCache.put('ui/dialog/appendCl/appendCl.tpl.html',
        '<div class="modal-header"><h3 class="modal-title">添加材料</h3></div>'
        + '<div class="modal-body">'
        + '<div class="checkbox" ng-repeat="item in items"><label for="{{item.id}}"><input type="checkbox" ng-checked="{{item.checked}}" id="{{item.id}}" name="clmc" value="{{item.id}}"  ng-click="select(item,$event)">{{item.materialName}}</label></div>'
        + '</div><div class="modal-footer"><button class="btn btn-primary" ng-click="ok()">确定</button> <button class="btn btn-warning" ng-click="cancel()">取消</button></div>'
    )

}]);

angular.module('kityminderEditor')
    .directive('appendCl', ['commandBinder', '$modal', function (commandBinder, $modal) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/appendCl/appendCl.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {
                //初始化材料
                var ywqxId = UrlParam.param('ywqxId');
                var urlList = WEB_ROOT + '/affairItemMaterialMain/all/list';
                urlList = ctx + '/rest/mind' + '/affairItemMaterialMain/all/list.do';
                $scope.addHyperlink = function () {
                    var minder = $scope.minder;
                    var nodes = minder.getSelectedNodes();
                    var clNodeList = nodes[0].children;
                    var clNodeIds = [];
                    var currentNode = minder.getSelectedNode();
                    var currentData = currentNode.data;
                    if (currentData.nodeTypeCode == AeaMindConst_MIND_NODE_TYPE_CODE_SITUATION) {
                        if (currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_STAGE
                            || currentBusiType == AeaMindConst_MIND_NODE_TYPE_CODE_ITEM
                        ) {

                        }

                    }
                    else {
                        agcloud.ui.metronic.showSwal({type: 'info', message: '仅支持在情形下添加材料!'});
                    }
                }
            }
        }
    }]);


angular.module('kityminderEditor')
    .directive('isMore', ['commandBinder', function (commandBinder) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/isMore/isMore.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {
                var minder = $scope.minder;
                var priorities = [];

                priorities.push(2);
                priorities.push(0);

                commandBinder.bind(minder, 'priority', $scope);
                $scope.priorities = priorities;
                $scope.getPriorityClass = function (p) {
                    switch (p) {
                        case 1:
                            return 'km-priority-icon tool-group-icon';
                        case 2:
                            return 'km-priority-icon tool-group-icon priority-2';
                        case 0:
                            return 'km-priority-icon tool-group-icon priority-0';
                    }
                }
                $scope.getPriorityTitle = function (p) {
                    switch (p) {
                        case 2:
                            return '多选';
                        case 0:
                            return '取消';
                    }
                }
            }
        }
    }]);


angular.module('kityminderEditor')
    .directive('linkProcessStart', ['commandBinder', function (commandBinder) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/linkProcessStart/linkProcessStart.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {

                var minder = $scope.minder;
                var linkfieldes = [];
                linkfieldes.push(1);
                linkfieldes.push(0);

                commandBinder.bind(minder, 'linkProcessStart', $scope);
                $scope.linkfieldes = linkfieldes;
                $scope.getInnerClass = function (p) {
                    switch (p) {
                        case 1:
                            return 'km-priority-icon tool-group-icon';
                        case 0:
                            return 'km-priority-icon tool-group-icon priority-0';
                    }
                }
                $scope.getInnerTitle = function (p) {
                    switch (p) {
                        case 1:
                            return '流程';
                        case 0:
                            return '取消';
                    }
                }
            }
        }
    }]);



angular.module('kityminderEditor')
    .directive('isInformCommit', ['commandBinder', function (commandBinder) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/isInformCommit/isInformCommit.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {

                var minder = $scope.minder;
                var linkfieldes = [];
                linkfieldes.push(1);
                linkfieldes.push(0);

                commandBinder.bind(minder, 'isInformCommit', $scope);
                $scope.linkfieldes = linkfieldes;
                $scope.getInnerClass = function (p) {
                    switch (p) {
                        case 1:
                            return 'km-isInformCommit-icon tool-group-icon';
                        case 0:
                            return 'km-priority-icon tool-group-icon priority-0';
                    }
                }
                $scope.getInnerTitle = function (p) {
                    switch (p) {
                        case 1:
                            return '告知';
                        case 0:
                            return '取消';
                    }
                }
            }
        }
    }]);


angular.module('kityminderEditor')
    .directive('terminateSituation', ['commandBinder', function (commandBinder) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/terminateSituation/terminateSituation.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {

                var minder = $scope.minder;
                var linkfieldes = [];
                linkfieldes.push(1);
                linkfieldes.push(0);

                commandBinder.bind(minder, 'terminateSituation', $scope);
                $scope.linkfieldes = linkfieldes;
                $scope.getInnerClass = function (p) {
                    switch (p) {
                        case 1:
                            return 'km-terminate-situation-icon tool-group-icon';
                        case 0:
                            return 'km-terminate-situation-icon tool-group-icon terminate-situation-0';
                    }
                }
                $scope.getInnerTitle = function (p) {
                    switch (p) {
                        case 1:
                            return '终止';
                        case 0:
                            return '取消';
                    }
                }
            }
        }
    }]);


angular.module('kityminderEditor')
    .directive('situationAnswerNum', ['commandBinder', function (commandBinder) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/situationAnswerNum/situationAnswerNum.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {
                var minder = $scope.minder;
                var linkfieldes = [];

                linkfieldes.push(0);
                linkfieldes.push(1);
                linkfieldes.push(2);
                linkfieldes.push(3);
                linkfieldes.push(4);
                linkfieldes.push(5);
                linkfieldes.push(6);
                linkfieldes.push(7);
                linkfieldes.push(8);
                linkfieldes.push(9);

                commandBinder.bind(minder, 'situationAnswerNum', $scope);
                $scope.linkfieldes = linkfieldes;
                $scope.getInnerClass = function (p) {
                    switch (p) {
                        /*case 1: return 'km-priority-icon tool-group-icon';
                        case 2: return 'km-priority-icon tool-group-icon priority-2';
                        case 0: return 'km-priority-icon tool-group-icon priority-0';
                        */
                        case 0:
                            return 'km-situation-answer-num-icon tool-group-icon situation-answer-num-0';
                        case 1:
                            return 'km-situation-answer-num-icon tool-group-icon situation-answer-num';
                        case 2:
                            return 'km-situation-answer-num-icon tool-group-icon situation-answer-num-2';
                        case 3:
                            return 'km-situation-answer-num-icon tool-group-icon situation-answer-num-3';
                        case 4:
                            return 'km-situation-answer-num-icon tool-group-icon situation-answer-num-4';
                        case 5:
                            return 'km-situation-answer-num-icon tool-group-icon situation-answer-num-5';
                        case 6:
                            return 'km-situation-answer-num-icon tool-group-icon situation-answer-num-6';
                        case 7:
                            return 'km-situation-answer-num-icon tool-group-icon situation-answer-num-7';
                        case 8:
                            return 'km-situation-answer-num-icon tool-group-icon situation-answer-num-8';
                        case 9:
                            return 'km-situation-answer-num-icon tool-group-icon situation-answer-num-9';

                    }
                }
                $scope.getInnerTitle = function (p) {
                    // switch(p) {
                    //     case 0: return '0';
                    //     case 1: return '1';
                    //     case 2: return '2';
                    //     case 3: return '3';
                    //     case 4: return '4';
                    //     case 5: return '5';
                    //     case 6: return '6';
                    //     case 7: return '7';
                    // }
                    return "";
                }
            }
        }
    }]);

angular.module('kityminderEditor')
    .directive('mustSelect', ['commandBinder', function (commandBinder) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/mustSelect/mustSelect.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {
                var minder = $scope.minder;
                var progresses = [];

                progresses.push(9);
                progresses.push(0);

                commandBinder.bind(minder, 'progress', $scope);

                $scope.progresses = progresses;

                $scope.getProgressTitle = function (p) {
                    switch (p) {
                        case 9:
                            return '必选';
                        case 0:
                            return '取消';
                    }
                }
            }
        }
    }])

angular.module('kityminderEditor')
    .controller('appendCl.ctrl', ["$scope", "$modalInstance", "data", function ($scope, $modalInstance, data) {
        $scope.items = data.items;
        $scope.selectedItem = [];
        $scope.selectIdArray = data.selectIdArray;
        $scope.select = function (item, event) {
            var action = event.target;
            var id = item.id;
            if (action.checked) {//选中，就添加
                if ($scope.selectIdArray.indexOf(id) == -1) {//不存在就添加
                    $scope.selectIdArray.push(id);
                }
            } else {//去除就删除result里
                var idx = $scope.selectIdArray.indexOf(id);
                if (idx != -1) {//不存在就添加
                    $scope.selectIdArray.splice(idx, 1);
                }
            }
        }

        $scope.ok = function () {
            //筛选选中材料
            for (var i in data.items) {
                if ($scope.selectIdArray.indexOf(data.items[i].id) > -1) {
                    if (!data.items[i].checked) {
                        $scope.selectedItem.push(data.items[i]);
                    }
                }
            }

            $modalInstance.close({
                items: $scope.selectedItem,
                itemIdArray: $scope.selectIdArray
            });
            editor.receiver.selectAll();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
            editor.receiver.selectAll();
        };

    }]);

angular.module('kityminderEditor')
    .directive('question', ['$modal', function ($modal) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/question/question.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {
                var minder = $scope.minder;
                $scope.addHyperlink = function () {
                    var link = minder.queryCommandValue('HyperLink');
                    var questionModal = $modal.open({
                        animation: true,
                        templateUrl: 'ui/dialog/question/question.tpl.html',
                        controller: 'question.ctrl',
                        size: 'md',
                        resolve: {
                            link: function () {
                                return link;
                            }
                        }
                    });

                    questionModal.result.then(function (result) {
                        minder.execCommand('HyperLink', "javaScript:;", result.title || '');
                    });
                }
            }
        }
    }]);


angular.module('kityminderEditor')
    .directive('previewButton', ['$modal', function ($modal) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/previewButton/previewButton.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {
                var minder = $scope.minder;

                $scope.preview = function () {
                    var ywqxId = UrlParam.param('ywqxId');
                    if (ywqxId) {
                        // window.open('itemSituationPreview.html?ywqxId=' + ywqxId);
                        var url = 'itemSituation/itemSituationPreview.html?ywqxId=' + ywqxId;
                        if (UrlParam.param('maximize')) {
                            url = 'itemSituationPreview.html?ywqxId=' + ywqxId + '&maximize=true';
                        }
                        parent.layer.open({
                            type: 2
                            , title: ['情形预览', 'color:#FFFFFF;background-color:#1E9FFF;font-size:16px;']
                            // ,title: false
                            , content: url
                            , shade: [0.1, '#fff']
                            //,shade: false
                            , scrollbar: false
                            , shadeClose: true
                            , maxmin: true
                            , closeBtn: 1
                            // ,offset: 'lt'
                            , anim: 2
                            , skin: 'layer-ext-block'
                            , area: ['95%', '95%']
                        });
                    } else {
                        top.msg('参数配置错误，请刷新页面重试');
                    }
                }
            }
        }
    }]);

angular.module('kityminderEditor')
    .directive('buttons', ['$modal', function ($modal) {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/buttons/buttons.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {
                var minder = $scope.minder;
                $scope.addHyperlink = function () {
                    // var ywqxId = UrlParam.param('ywqxId');
                    var ywqxId = "ff80808165a38a890165a77f5b160776";
                    if (ywqxId) {
                        var json = minder.exportJson()
                            , param = {ywqxId: ywqxId, situationJson: JSON.stringify(json.root)};

                        var tempdata = {};
                        tempdata = {
                            "data": {
                                "text": "是",
                                "id": "ff80808165a38a890165a77f5bdd07d2",
                                "priority": "",
                                "progress": ""
                            },
                            "children": [
                                {
                                    "data": {
                                        "text": "土地登记资料查询表",
                                        "id": "ff80808165a38a890165a77f5b350788",
                                        "priority": "3",
                                        "progress": ""
                                    },
                                    "children": []
                                }
                            ]
                        };
                        if (curIsEditable) {

                            $("#uploadProgress").modal("show");
                            $('#saveItemBasic').hide();
                            $('#uploadProgressMsg').html("保存数据中,请勿点击,耐心等候...");

                            param = json;
                            var url = currentUrlSaveData;
                            $.ajax({
                                type: "POST",
                                url: url,
                                contentType: "application/json",
                                data: JSON.stringify(json.root),
                                dataType: "json",
                                success: function (response) {
                                    if (response.code === 1) {

                                        setTimeout(function(){
                                            $("#uploadProgress").modal('hide');
                                            swal({
                                                title: '提示信息',
                                                text: "保存成功!",
                                                type: 'info',
                                                confirmButtonText: '确认!'
                                            }).then(function (result) {
                                                refreshMind();
                                            });
                                        },500);

                                    } else {

                                        setTimeout(function(){
                                            $("#uploadProgress").modal('hide');
                                            swal({
                                                title: '错误信息',
                                                text: response.msg,
                                                type: 'error',
                                                confirmButtonText: '确认!'
                                            }).then(function (result) {
                                                refreshMind();
                                            });
                                        },500);
                                    }
                                }
                            });
                        }else{
                            agcloud.ui.metronic.showSwal({type: 'info', message: '当前版本下数据不可编辑!'});
                        }
                    } else {
                        top.msg('参数配置错误，请刷新页面重试');
                    }
                }
            }
        }
    }]);

if(handWay=='1') {

    angular.module('kityminderEditor')
        .directive('mindFilter', ['$modal', function ($modal) {
            return {
                restrict: 'E',
                templateUrl: 'ui/directive/mindFilter/mindFilter.html',
                scope: {
                    minder: '='
                },
                replace: true,
                link: function ($scope) {
                    var minder = $scope.minder;
                    $scope.addHyperlink = function () {
                        // var ywqxId = UrlParam.param('ywqxId');
                        var ywqxId = "ff80808165a38a890165a77f5b160776";
                        if (ywqxId) {
                            var json = minder.exportJson()
                                , param = {ywqxId: ywqxId, situationJson: JSON.stringify(json.root)};

                            param = json;
                            var url = ctx + '/rest/mind/affairItemSituation/v3/save.do?stateVerId=' + currentStateVerId;
                            $.ajax({
                                type: "POST",
                                url: url,
                                contentType: "application/json",
                                data: JSON.stringify(json.root),
                                dataType: "json",
                                success: function (response) {
                                    if (response.code === 1) {

                                        swal({
                                            title: '提示信息',
                                            text: "保存成功!",
                                            type: 'info',
                                            confirmButtonText: '确认!'
                                        }).then(function (result) {
                                            location.reload();
                                        });
                                    } else {

                                        swal({
                                            title: '错误信息',
                                            text: response.msg,
                                            type: 'error',
                                            confirmButtonText: '确认!'
                                        }).then(function (result) {
                                            location.reload();
                                        });
                                    }
                                }
                            })
                        } else {
                            top.msg('参数配置错误，请刷新页面重试');
                        }
                    }
                }
            }
        }]);
}

angular.module('kityminderEditor')
    .directive('maximization', function () {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/maximization/maximization.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {
                var minder = $scope.minder;

                $scope.maximization = function () {
                    var href = location.href;
                    if (href.indexOf('?') > -1) {
                        href += '&maximize=true'
                    }
                    window.open(href);
                }
            }
        }
    });


angular.module('kityminderEditor')
    .directive('copySituation', function () {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/copySituation/copySituation.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {
                $scope.copySituation = function () {
                    funCopySituation();
                }
            }
        }
    });

angular.module('kityminderEditor')
    .directive('layclose', function () {
        return {
            restrict: 'E',
            templateUrl: 'ui/directive/layclose/layclose.html',
            scope: {
                minder: '='
            },
            replace: true,
            link: function ($scope) {
                var minder = $scope.minder;

                $scope.close = function () {

                    window.close();

                }
            }
        }
    });

angular.module('kityminderEditor')
    .service('lang.zh-cn', function () {
        return {
            'zh-cn': {
                'template': {
                    'default': '思维导图',
                    'tianpan': '天盘图',
                    'structure': '组织结构图',
                    'filetree': '目录组织图',
                    'right': '逻辑结构图',
                    'fish-bone': '鱼骨头图'
                },
                'theme': {
                    'classic': '脑图经典',
                    'classic-compact': '紧凑经典',
                    'snow': '温柔冷光',
                    'snow-compact': '紧凑冷光',
                    'fish': '鱼骨图',
                    'wire': '线框',
                    'fresh-red': '清新红',
                    'fresh-soil': '泥土黄',
                    'fresh-green': '文艺绿',
                    'fresh-blue': '天空蓝',
                    'fresh-purple': '浪漫紫',
                    'fresh-pink': '脑残粉',
                    'fresh-red-compat': '紧凑红',
                    'fresh-soil-compat': '紧凑黄',
                    'fresh-green-compat': '紧凑绿',
                    'fresh-blue-compat': '紧凑蓝',
                    'fresh-purple-compat': '紧凑紫',
                    'fresh-pink-compat': '紧凑粉',
                    'tianpan': '经典天盘',
                    'tianpan-compact': '紧凑天盘'
                },
                'maintopic': '中心主题',
                'topic': '分支主题',
                'panels': {
                    'history': '历史',
                    'template': '模板',
                    'theme': '皮肤',
                    'layout': '布局',
                    'style': '样式',
                    'font': '文字',
                    'color': '颜色',
                    'background': '背景',
                    'insert': '插入',
                    'arrange': '调整',
                    'nodeop': '当前',
                    'priority': '优先级',
                    'progress': '进度',
                    'resource': '资源',
                    'note': '备注',
                    'attachment': '附件',
                    'word': '文字'
                },
                'error_message': {
                    'title': '哎呀，脑图出错了',

                    'err_load': '加载脑图失败',
                    'err_save': '保存脑图失败',
                    'err_network': '网络错误',
                    'err_doc_resolve': '文档解析失败',
                    'err_unknown': '发生了奇怪的错误',
                    'err_localfile_read': '文件读取错误',
                    'err_download': '文件下载失败',
                    'err_remove_share': '取消分享失败',
                    'err_create_share': '分享失败',
                    'err_mkdir': '目录创建失败',
                    'err_ls': '读取目录失败',
                    'err_share_data': '加载分享内容出错',
                    'err_share_sync_fail': '分享内容同步失败',
                    'err_move_file': '文件移动失败',
                    'err_rename': '重命名失败',

                    'unknownreason': '可能是外星人篡改了代码...',
                    'pcs_code': {
                        3: "不支持此接口",
                        4: "没有权限执行此操作",
                        5: "IP未授权",
                        110: "用户会话已过期，请重新登录",
                        31001: "数据库查询错误",
                        31002: "数据库连接错误",
                        31003: "数据库返回空结果",
                        31021: "网络错误",
                        31022: "暂时无法连接服务器",
                        31023: "输入参数错误",
                        31024: "app id为空",
                        31025: "后端存储错误",
                        31041: "用户的cookie不是合法的百度cookie",
                        31042: "用户未登陆",
                        31043: "用户未激活",
                        31044: "用户未授权",
                        31045: "用户不存在",
                        31046: "用户已经存在",
                        31061: "文件已经存在",
                        31062: "文件名非法",
                        31063: "文件父目录不存在",
                        31064: "无权访问此文件",
                        31065: "目录已满",
                        31066: "文件不存在",
                        31067: "文件处理出错",
                        31068: "文件创建失败",
                        31069: "文件拷贝失败",
                        31070: "文件删除失败",
                        31071: "不能读取文件元信息",
                        31072: "文件移动失败",
                        31073: "文件重命名失败",
                        31079: "未找到文件MD5，请使用上传API上传整个文件。",
                        31081: "superfile创建失败",
                        31082: "superfile 块列表为空",
                        31083: "superfile 更新失败",
                        31101: "tag系统内部错误",
                        31102: "tag参数错误",
                        31103: "tag系统错误",
                        31110: "未授权设置此目录配额",
                        31111: "配额管理只支持两级目录",
                        31112: "超出配额",
                        31113: "配额不能超出目录祖先的配额",
                        31114: "配额不能比子目录配额小",
                        31141: "请求缩略图服务失败",
                        31201: "签名错误",
                        31202: "文件不存在",
                        31203: "设置acl失败",
                        31204: "请求acl验证失败",
                        31205: "获取acl失败",
                        31206: "acl不存在",
                        31207: "bucket已存在",
                        31208: "用户请求错误",
                        31209: "服务器错误",
                        31210: "服务器不支持",
                        31211: "禁止访问",
                        31212: "服务不可用",
                        31213: "重试出错",
                        31214: "上传文件data失败",
                        31215: "上传文件meta失败",
                        31216: "下载文件data失败",
                        31217: "下载文件meta失败",
                        31218: "容量超出限额",
                        31219: "请求数超出限额",
                        31220: "流量超出限额",
                        31298: "服务器返回值KEY非法",
                        31299: "服务器返回值KEY不存在"
                    }
                },
                'ui': {
                    'shared_file_title': '[分享的] {0} (只读)',
                    'load_share_for_edit': '正在加载分享的文件...',
                    'share_sync_success': '分享内容已同步',
                    'recycle_clear_confirm': '确认清空回收站么？清空后的文件无法恢复。',

                    'fullscreen_exit_hint': '按 Esc 或 F11 退出全屏',

                    'error_detail': '详细信息',
                    'copy_and_feedback': '复制并反馈',
                    'move_file_confirm': '确定把 "{0}" 移动到 "{1}" 吗？',
                    'rename': '重命名',
                    'rename_success': '{0} 重命名成功',
                    'move_success': '{0} 移动成功到 {1}',

                    'command': {
                        'appendsiblingnode': '插入同级条件',
                        'appendparentnode': '插入上级情形',
                        'appendchildnode': '插入下级情形',
                        'removenode': '删除',
                        'editnode': '编辑',
                        'arrangeup': '上移',
                        'arrangedown': '下移',
                        'resetlayout': '整理布局',
                        'maxLayout': '全屏编辑',
                        'expandtoleaf': '展开全部节点',
                        'expandtolevel1': '展开到一级节点',
                        'expandtolevel2': '展开到二级节点',
                        'expandtolevel3': '展开到三级节点',
                        'expandtolevel4': '展开到四级节点',
                        'expandtolevel5': '展开到五级节点',
                        'expandtolevel6': '展开到六级节点',
                        'fullscreen': '全屏',
                        'outline': '大纲'
                    },

                    'search': '搜索',

                    'expandtoleaf': '展开',

                    'back': '返回',

                    'undo': '撤销 (Ctrl + Z)',
                    'redo': '重做 (Ctrl + Y)',

                    'tabs': {
                        'idea': '思路',
                        'appearence': '外观',
                        'view': '视图'
                    },

                    'quickvisit': {
                        'new': '新建 (Ctrl + Alt + N)',
                        'save': '保存 (Ctrl + S)',
                        'share': '分享 (Ctrl + Alt + S)',
                        'feedback': '反馈问题（F1）',
                        'editshare': '编辑'
                    },

                    'menu': {

                        'mainmenutext': '百度脑图', // 主菜单按钮文本

                        'newtab': '新建',
                        'opentab': '打开',
                        'savetab': '保存',
                        'sharetab': '分享',
                        'preferencetab': '设置',
                        'helptab': '帮助',
                        'feedbacktab': '反馈',
                        'recenttab': '最近使用',
                        'netdisktab': '百度云存储',
                        'localtab': '本地文件',
                        'drafttab': '草稿箱',
                        'downloadtab': '导出到本地',
                        'createsharetab': '当前脑图',
                        'managesharetab': '已分享',

                        'newheader': '新建脑图',
                        'openheader': '打开',
                        'saveheader': '保存到',
                        'draftheader': '草稿箱',
                        'shareheader': '分享我的脑图',
                        'downloadheader': '导出到指定格式',
                        'preferenceheader': '偏好设置',
                        'helpheader': '帮助',
                        'feedbackheader': '反馈'
                    },

                    'mydocument': '我的文档',
                    'emptydir': '目录为空！',
                    'pickfile': '选择文件...',
                    'acceptfile': '支持的格式：{0}',
                    'dropfile': '或将文件拖至此处',
                    'unsupportedfile': '不支持的文件格式',
                    'untitleddoc': '未命名文档',
                    'overrideconfirm': '{0} 已存在，确认覆盖吗？',
                    'checklogin': '检查登录状态中...',
                    'loggingin': '正在登录...',
                    'recent': '最近打开',
                    'clearrecent': '清空',
                    'clearrecentconfirm': '确认清空最近文档列表？',
                    'cleardraft': '清空',
                    'cleardraftconfirm': '确认清空草稿箱？',

                    'none_share': '不分享',
                    'public_share': '公开分享',
                    'password_share': '私密分享',
                    'email_share': '邮件邀请',
                    'url_share': '脑图 URL 地址：',
                    'sns_share': '社交网络分享：',
                    'sns_share_text': '“{0}” - 我用百度脑图制作的思维导图，快看看吧！（地址：{1}）',
                    'none_share_description': '不分享当前脑图',
                    'public_share_description': '创建任何人可见的分享',
                    'share_button_text': '创建',
                    'password_share_description': '创建需要密码才可见的分享',
                    'email_share_description': '创建指定人可见的分享，您还可以允许他们编辑',
                    'ondev': '敬请期待！',
                    'create_share_failed': '分享失败：{0}',
                    'remove_share_failed': '删除失败：{1}',
                    'copy': '复制',
                    'copied': '已复制',
                    'shared_tip': '当前脑图被 {0}  分享，你可以修改之后保存到自己的网盘上或再次分享',
                    'current_share': '当前脑图',
                    'manage_share': '我的分享',
                    'share_remove_action': '不分享该脑图',
                    'share_view_action': '打开分享地址',
                    'share_edit_action': '编辑分享的文件',

                    'login': '登录',
                    'logout': '注销',
                    'switchuser': '切换账户',
                    'userinfo': '个人信息',
                    'gotonetdisk': '我的网盘',
                    'requirelogin': '请 <a class="login-button">登录</a> 后使用',
                    'saveas': '保存为',
                    'filename': '文件名',
                    'fileformat': '保存格式',
                    'save': '保存',
                    'mkdir': '新建目录',
                    'recycle': '回收站',
                    'newdir': '未命名目录',

                    'bold': '加粗',
                    'italic': '斜体',
                    'forecolor': '字体颜色',
                    'fontfamily': '字体',
                    'fontsize': '字号',
                    'layoutstyle': '主题',
                    'node': '节点操作',
                    'saveto': '另存为',
                    'hand': '允许拖拽',
                    'camera': '定位根节点',
                    'zoom-in': '放大（Ctrl+）',
                    'zoom-out': '缩小（Ctrl-）',
                    'markers': '标签',
                    'resource': '资源',
                    'help': '帮助',
                    'preference': '偏好设置',
                    'expandnode': '展开到叶子',
                    'collapsenode': '收起到一级节点',
                    'template': '模板',
                    'theme': '皮肤',
                    'clearstyle': '清除样式',
                    'copystyle': '复制样式',
                    'pastestyle': '粘贴样式',
                    'appendsiblingnode': '同级主题',
                    'appendchildnode': '下级主题',
                    'arrangeup': '前调',
                    'arrangedown': '后调',
                    'editnode': '编辑',
                    'removenode': '移除',
                    'priority': '优先级',
                    'progress': {
                        'p1': '未开始',
                        'p2': '完成 1/8',
                        'p3': '完成 1/4',
                        'p4': '完成 3/8',
                        'p5': '完成一半',
                        'p6': '完成 5/8',
                        'p7': '完成 3/4',
                        'p8': '完成 7/8',
                        'p9': '已完成',
                        'p0': '清除进度'
                    },
                    'linkItem4situation': '情形事项',
                    // 'linkForm4situation': '绑定表单',
                    'linkItem4mat': '材料事项',
                    'addMat4stage': '阶段材料',
                    'addMat4situation': '绑定材料',
                    'importMat': '全局材料',
                    // 'importCert': '电子证照',
                    'stateVersion': '情形版本',
                    'link': '添加材料',
                    'image': '图片',
                    'note': '备注',
                    'insertlink': '插入下级问题名称',
                    'insertimage': '插入图片',
                    'insertnote': '插入备注',
                    'removelink': '移除下级问题名称',
                    'removeimage': '移除已有图片',
                    'removenote': '移除已有备注',
                    'resetlayout': '整理',

                    'justnow': '刚刚',
                    'minutesago': '{0} 分钟前',
                    'hoursago': '{0} 小时前',
                    'yesterday': '昨天',
                    'daysago': '{0} 天前',
                    'longago': '很久之前',

                    'redirect': '您正在打开连接 {0}，百度脑图不能保证连接的安全性，是否要继续？',
                    'navigator': '导航器',

                    'unsavedcontent': '当前文件还没有保存到网盘：\n\n{0}\n\n虽然未保存的数据会缓存在草稿箱，但是清除浏览器缓存会导致草稿箱清除。',

                    'shortcuts': '快捷键',
                    'contact': '联系与反馈',
                    'email': '邮件组',
                    'qq_group': 'QQ 群',
                    'github_issue': 'Github',
                    'baidu_tieba': '贴吧',

                    'clipboardunsupported': '您的浏览器不支持剪贴板，请使用快捷键复制',

                    'load_success': '{0} 加载成功',
                    'save_success': '{0} 已保存于 {1}',
                    'autosave_success': '{0} 已自动保存于 {1}',

                    'selectall': '全选',
                    'selectrevert': '反选',
                    'selectsiblings': '选择兄弟节点',
                    'selectlevel': '选择同级节点',
                    'selectpath': '选择路径',
                    'selecttree': '选择子树'
                },
                'popupcolor': {
                    'clearColor': '清空颜色',
                    'standardColor': '标准颜色',
                    'themeColor': '主题颜色'
                },
                'dialogs': {
                    'markers': {
                        'static': {
                            'lang_input_text': '文本内容：',
                            'lang_input_url': '链接地址：',
                            'lang_input_title': '标题：',
                            'lang_input_target': '是否在新窗口：'
                        },
                        'priority': '优先级',
                        'none': '无',
                        'progress': {
                            'title': '进度',
                            'notdone': '未完成',
                            'done1': '完成 1/8',
                            'done2': '完成 1/4',
                            'done3': '完成 3/8',
                            'done4': '完成 1/2',
                            'done5': '完成 5/8',
                            'done6': '完成 3/4',
                            'done7': '完成 7/8',
                            'done': '已完成'
                        }
                    },
                    'help': {},
                    'hyperlink': {},
                    'image': {},
                    'resource': {}
                },
                'hyperlink': {
                    'hyperlink': '链接...',
                    'unhyperlink': '移除链接'
                },
                'image': {
                    'image': '图片...',
                    'removeimage': '移除图片'
                },
                'marker': {
                    'marker': '进度/优先级...'
                },
                'resource': {
                    'resource': '资源...'
                }
            }
        }
    });