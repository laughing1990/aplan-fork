
var isMax = UrlParam.param('maximize');
var isMultiple = UrlParam.param('isMultiple');
function changeSfpzqx(flag){
    var text = '分情形配置';
    if(!flag)
    {
        text = '不分情形配置';
    }


    layer.confirm('确定切换为'+text+'吗？', {
        btn: ['确认','取消'] //按钮
    }, function(){
        var itemcode = UrlParam.param('itemcode');
        var url = WEB_ROOT + '/affairItemSituation/version/read'
            ,param = {itemcode: itemcode, qxzt: '0'}
        ;
        if(flag){

        }
        else{

        }
        location.href = url;
        /*
        globalAsyncPost(url, param, function (response) {
            if(response.code === 1){
                if(response.data){
                    var data = response.data;
                    var save_url = WEB_ROOT + '/affairItemSituation/version/save'
                        ,ywqxId = data.id
                        ,sffqx = flag ? '1' : '0'
                        ,save_param = {ywqxId: ywqxId, sffqx: sffqx,qxly:"1"}
                    ;
                    globalAsyncPost(save_url, save_param, function (response) {
                        if(response.code === 1){
                            funGotoPage(sffqx);
                        }else{
                            top.msg('操作失败，请刷新页面重试')
                        }
                    })
                }
            }
        });
        */
    }, function(){

    });
}


function funGotoPage(sffqx){
    var href = 'itemSituation_index.html'
    if(sffqx === '0'){
        href = 'material_list.html'
    }
    toHrefTakeParam(href);
}



function funCombination()
{
    layer.confirm('确认发布？<br/><label style="color: red;">发布之前请先确认是否保存情形</label><br/><label style="color: red;">发布之前请先确认是否已配置通用流程</label>', function(){
        var ywqxId = UrlParam.param('ywqxId');
        var url = WEB_ROOT + '/affairItemSituation/v3/publish';

        globalAsyncPost(url, {ywqxId: ywqxId}, function(response){
            if(response.code === 1){
                top.msg('操作成功');
                $(window.parent.document).find('.issuesUl li:eq(4)').click();

                if(isMax){
                    location.reload();
                }
                else {
                    $(window.parent.document).find('.issuesUl li:eq(4)').click();
                }
            }else{
                top.msg(response.msg || '操作失败');
            }
        });
    })
}

function funPreView() {
    var ywqxId = UrlParam.param('ywqxId');
    var url = '/html/affair/catalogues/issuesManagement/itemSituation/itemSituationPreview.html?ywqxId=' + ywqxId;
    if(isMax)  url = url +"&maximize="+isMax;
    top.showData(url, '情形预览');
}

function funCopySituation() {
    var ywqxId = UrlParam.param('ywqxId');
    var url = '/html/affair/catalogues/issuesManagement/itemSituation/itemSituationCopy.html?ywqxId=' + ywqxId;
    if(isMax)  url = url +"&maximize="+isMax;

    top.showData(url, '复制情形');
}

function funShowbackVersionPanel()
{
    var itemcode = UrlParam.param('itemcode');
    var isMultiple = UrlParam.param('isMultiple');

    var url = '/html/affair/catalogues/issuesManagement/itemSituation/itemSituationHist_list.html?itemcode=' + itemcode;
    if(isMax)  url = url +"&maximize="+isMax;
    if(isMultiple)  url = url +"&isMultiple="+isMultiple;
    top.showData(url, '情形历史版本');
}

