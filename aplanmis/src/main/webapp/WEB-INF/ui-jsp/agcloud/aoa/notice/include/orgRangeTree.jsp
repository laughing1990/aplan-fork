<%@ page contentType="text/html;charset=UTF-8" %>
<div id="treeDialogAoaNoticeRange" class="modal fade" tabindex="-1" role="dialog"
	 aria-labelledby="treeDialogAoaNoticeRange_title" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-lg" role="document" >
		<div class="modal-content">
			<div class="modal-header" style="padding: 10px;padding-left: 10px;height: 44px;">
				<h5 id="treeDialogAoaNoticeRange_title" class="modal-title">发送范围组织</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="m-portlet__body miniScrollbar" style="height: 500px">
				<div class="m-form m-form--label-align-right m--margin-bottom-5">
					<div class="row" style="margin-top: 5px; margin-left: 0px;margin-right: 10px;">
						<div class="col-md-6"style="text-align: left;">
							<button type="button" class="btn  btn-info" onclick="saveTreeNodeAoaNoticeRange()">保存</button>
						</div>
					</div>
				</div>
				<div style="padding:10px;">
					<ul id="treeAoaNoticeRange" class="ztree" style="overflow:auto;"></ul>
				</div>
			</div>
		</div>
	</div>
</div>
<style>
	.miniScrollbar{
		overflow: auto;
	}
	.miniScrollbar::-webkit-scrollbar {/*滚动条整体样式*/
		width: 4px;     /*高宽分别对应横竖滚动条的尺寸*/
		height: 4px;
	}
	.miniScrollbar::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
		border-radius: 5px;
		-webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
		background: rgba(0,0,0,0.2);
	}
	.miniScrollbar::-webkit-scrollbar-track {/*滚动条里面轨道*/
		-webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
		border-radius: 0;
		background: rgba(0,0,0,0.1);
	}
</style>
<script>
    var treeOptionAoaNoticeRange = {
        edit: {
            enable: true, //设置 zTree 是否处于编辑状态
            showRemoveBtn: false,//设置是否显示删除按钮
            showRenameBtn: false//设置是否显示编辑名称按钮
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId"
            }
        },
        view: {
            selectedMulti: true,//设置是否允许同时选中多个节点
            showTitle : false, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
            showLine: false //设置 zTree 是否显示节点之间的连线。
        },
        check: {
            enable: true,
            chkboxType:{ "Y" : "", "N" : "" },
            chkStyle: "checkbox"
        },
        async: {
            //设置 zTree 是否开启异步加载模式
            enable: true,
            autoParam:["id"],
            dataType:"json",
            type:"post",
            url:ctx+"/aoa/notice/content/getListOpusOmZtreeNode.do"
        },
        callback: {
            //onClick: ,
            //onRightClick:,
            onAsyncSuccess: onAsyncSuccessAoaNoticeRange
            ,onExpand:function(event, treeId, treeNode, msg){
                var zTree = $.fn.zTree.getZTreeObj(treeId);
            }
        }
    };
    function loadTreeAoaNoticeRange(){
        $('#treeDialogAoaNoticeRange').modal('show');
        $.fn.zTree.init($("#treeAoaNoticeRange"), treeOptionAoaNoticeRange);
    }
    function onAsyncSuccessAoaNoticeRange(event, treeId, treeNode, msg) {
        var zTree = $.fn.zTree.getZTreeObj(treeId);
        //对于根节点，展开下一级
        if(treeNode==null){
            var nodes = zTree.getNodes();
            for (var i=0, l=nodes.length; i<l; i++) {
                zTree.expandNode(nodes[i], true, false, false);
            }
        }
		checkTreeNodeAoaNoticeRange(zTree);
    };
    function checkTreeNodeAoaNoticeRange(ztreeObj){
        $.post(ctx + "/aoa/notice/range/listAllAoaNoticeRangeByAoaNoticeRange.do", {contentId:currentAoaNoticeContentId}, function (data) {
            if (data.length > 0) {
                $.each(data, function (i, n) {
                    var node = ztreeObj.getNodeByParam("id",n.orgId,null);
                    if(node!=null)ztreeObj.checkNode(node, true, true,true);
                });
            }
        }, 'json');
    }

    function saveTreeNodeAoaNoticeRange(){
        var treeAoaNoticeRangeObj = $.fn.zTree.getZTreeObj("treeAoaNoticeRange");
        var node = treeAoaNoticeRangeObj.getCheckedNodes(true);
        var orgIds = '';
        if(node!=null){
			$.each(node, function (i, n) {
                orgIds = orgIds + n.id + ',';
            });
            orgIds = orgIds.substring(0,orgIds.length-1);
            $.ajax({
                url:  ctx+'/aoa/notice/range/saveBatchAoaNoticeRange.do',
                type: 'POST',
                data: {contentId:currentAoaNoticeContentId,orgId:orgIds},
                success: function (result) {
                    if(result.success){
                        $('#treeDialogAoaNoticeRange').modal('hide');
                        swal('保存成功', result.message, 'success');
                    }else{
                        swal('错误信息', result.message, 'error');
                    }
                },
                error: function () {
                    swal('错误信息', '服务器异常！', 'error');
                }
            });
        }
    }
</script>