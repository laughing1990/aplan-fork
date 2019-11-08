//显示右键菜单
function showRMenu(contextMenuId,x, y) {

    $('#'+contextMenuId).show();
    $('#'+contextMenuId).css({"top": y + "px", "left" :x + "px", "visibility":"visible"}); //设置右键菜单的位置、可见
    // 鼠标按下事件
    $("body").bind("mousedown",contextMenuId,function(){
        if (!(event.target.id == contextMenuId || $(event.target).parents('#'+contextMenuId).length > 0)) {
            $('#'+contextMenuId).hide();
        }
    });
}

//隐藏右键菜单
function hideRMenu(contextMenuId) {

    //设置右键菜单不可见
    if ($('#'+contextMenuId))$('#'+contextMenuId).css({"visibility": "hidden"});
    // 鼠标按下事件
    $("body").unbind("mousedown",contextMenuId,function(){
        if (!(event.target.id == contextMenuId || $(event.target).parents('#'+contextMenuId).length > 0)) {
            $('#'+contextMenuId).hide();
        }
    });
}