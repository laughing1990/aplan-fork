var curIframeIndex;
function hideData()
{
    if(!curIframeIndex){
        curIframeIndex = parent.layer.getFrameIndex(window.name);
	}
    parent.layer.close(curIframeIndex);
}

function showData(url,title,width,height,endCallback)
{
	if(layer)
	{
		title = (title || '');
		title = '<i class="layui-icon" style="margin-right:10px;">&#xe63c;</i>' + title;
		curIframeIndex = layer.open
		({
        	type	: 2, //2:iframe
            title	: [title,'color:#FFFFFF;background-color:#1e9fff;font-size:16px;'],
        	area	: initArea(width,height),
        	content	: url,
       		zIndex	: 10,
       		success	: function(layero, index)
       		{
       			if(top.$('#layui-layer' + index).find('iframe').length > 0 && top.$('#layui-layer' + index).find('iframe')[0].contentWindow.prevIframeIndex){
                    top.$('#layui-layer' + index).find('iframe')[0].contentWindow.prevIframeIndex = prevIframeIndex;
                    top.$('#layui-layer' + index).find('iframe')[0].contentWindow.iframeIndex = index;
				}
			},end: function(){
                if(typeof(endCallback) == 'function'){
                    endCallback();
                }
                if(typeof searchList == 'function'){
                    searchList();
				}
			}
      	});
	}
}

function openDialog4Call(url,title,width,height,openCallback,endCallback)
{
    if(layer)
    {
        title = (title || '');
        title = '<i class="layui-icon" style="margin-right:10px;">&#xe63c;</i>' + title;
        curIframeIndex = layer.open
        ({
            type	: 2, //2:iframe
            title	: [title,'color:#FFFFFF;background-color:#1e9fff;font-size:16px;'],
            area	: initArea(width,height),
            content	: url,
            zIndex	: 10,
            btnAlign: 'c',
            success	: function(layero, index)
            {
                if(top.$('#layui-layer' + index).find('iframe').length > 0 && top.$('#layui-layer' + index).find('iframe')[0].contentWindow.prevIframeIndex){
                    top.$('#layui-layer' + index).find('iframe')[0].contentWindow.prevIframeIndex = prevIframeIndex;
                    top.$('#layui-layer' + index).find('iframe')[0].contentWindow.iframeIndex = index;
                }
                openCallback(layero, index);
            },end: function(){
                if(typeof(endCallback) == 'function'){
                    endCallback();
                }
                if(typeof searchList == 'function'){
                    searchList();
                }
            }
        });
    }
}

function openDialog4Qxpz(url,title,width,height,openCallback,successCallback,successCallback2)
{
    if(layer)
    {
        title = (title || '');
        title = '<i class="layui-icon" style="margin-right:10px;">&#xe63c;</i>' + title;
        curIframeIndex = layer.open
        ({
            type	: 2, //2:iframe
            title	: [title,'color:#FFFFFF;background-color:#1e9fff;font-size:16px;'],
            area	: initArea(width,height),
            content	: url,
            zIndex	: 10,
            btn: ['确定','重置','取消'],
            success	: function(layero, index)
            {
                if(top.$('#layui-layer' + index).find('iframe').length > 0 && top.$('#layui-layer' + index).find('iframe')[0].contentWindow.prevIframeIndex){
                    top.$('#layui-layer' + index).find('iframe')[0].contentWindow.prevIframeIndex = prevIframeIndex;
                    top.$('#layui-layer' + index).find('iframe')[0].contentWindow.iframeIndex = index;
                }
                openCallback(layero, index);
            },
            yes: function (index, layero) {
                successCallback(layero,index);
            },
            btn2: function(index, layero){
                successCallback2(layero,index);
            }
        });
    }
}

function initArea(width,height){
	var documentWidth = $(window).width();
	if(!width || documentWidth < width){
		width = documentWidth;
	}
    var documentHeight = $(window).height();
    if(!height || documentHeight < height){
        height = documentHeight;
    }
    return [width + 'px', height + 'px'];
}