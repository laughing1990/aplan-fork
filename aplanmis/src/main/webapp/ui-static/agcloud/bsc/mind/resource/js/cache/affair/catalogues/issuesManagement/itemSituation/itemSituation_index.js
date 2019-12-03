$(function () {
    var element = layui.element;
    element.init();
    element.on('tab(myTab)', function (data) {
        var doId = UrlParam.param("id");
        if (data.index === 1) {
            $('input[name=itemcode]').val(doId);
            initDataList();
        }
    });
});

angular.module('kityminderDemo', ['kityminderEditor'])
    .controller('MainController', function ($scope) {
        $scope.initEditor = function (editor, minder) {
            window.editor = editor;
            window.minder = minder;
            url = currentUrlLoadData;
            param = {};
            param.busiType=currentBusiType;
            param.busiId=currentBusiId;
            param.stateVerId = currentStateVerId;
            param.showMat = true;
            param.showCert = true;
            param.showSituationLinkItem = false;
            param.showForm = false;
            if(handWay=='0'){
                param.showMat = false;
                param.showCert = false;
                param.showSituationLinkItem = true;
                param.showForm = false;
            }
            loadMind(url, param);
        };
    });

function zuanData(node){
    var data = {text: node.name
        , id: node.id
        ,name:node.name
        ,priority:node.priority
        ,progress:node.progress
    };
    if(node.bz){
        data.note = node.bz;
    }
    data.nodeTypeCode=node.nodeTypeCode;
    data.nodeTypeName=node.nodeTypeName;
    data.note=node.note;
    data.bindDestTypeCode=node.bindDestTypeCode;
    data.bindDestId=node.bindDestId;
    data.bindDisplayJson=node.bindDisplayJson;
    data.isGlobal=node.isGlobal;
    data.text=getNodeText(data.name,data.bindDisplayJson);
    data.extra=node.extra;
    data.linkProcessStart=node.linkProcessStart;
    data.terminateSituation=node.terminateSituation;
    data.situationAnswerNum=node.situationAnswerNum;
    data.isInformCommit='1';
    // data.isInformCommit=node.isInformCommit;


    // if(node.name){
    //     data.hyperlink = 'javaScript:;'
    //     data.hyperlinkTitle = node.name;
    // }
    return data;
}

function zuan(node){
    var json = {};
    json.data = zuanData(node);
    if(node.childs && node.childs.length > 0){
        json.children = [];
        for(var c in node.childs){
            var child = node.childs[c];
            json.children.push(zuan(child));
        }
    }
    return json;
}

function transformData(resData){
    var node = resData[0];
    var json = {};
    json.root = zuan(node);
    return json;
}