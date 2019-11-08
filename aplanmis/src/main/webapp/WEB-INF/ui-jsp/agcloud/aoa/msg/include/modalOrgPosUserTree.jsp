<%@ page contentType="text/html;charset=UTF-8" %>
<div id="modalOrgPosUserTree" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="modalOrgPosUserTreeTitle" aria-hidden="true" style="">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" style="color: #333">
                    选择发送人
                </h5>
            </div>
            <div class="modal-body" style="padding: 10px;padding-right: 10px;width:100%;height:500px;overflow:auto;">
                <div style="display: flex;">
                    <div style="flex:1;">
                        <ul id="omOrgLeftTree" class="ztree" ></ul>
                    </div>
                    <div style="flex:3;">
                        <div class="m-section">
                            <h3 class="m-section__heading">
                            </h3>
                            <div class="m-section__content">
                                <div class="m-demo">
                                    <div class="m-demo__preview" style="background: #F7F8FC;">
                                        <div class="m-list-badge">
                                            <div class="m-list-badge__label m--font-success">
                                                <%--发送人列表--%>
                                            </div>
                                            <div class="m-list-badge__items" id="containerUserOfOrg">
                                                <%--<a href="#" class="m-list-badge__item">
                                                    张伟宁
                                                </a>--%>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-group m-form__group row" style="text-align: right;">
                <div class="col-lg-11">
                    <button type="button" class="btn btn-info" onclick="confirmChooseUser();">确定</button>&nbsp;&nbsp;
                    <button type="button" class="btn btn-secondary"
                            onclick="$('#modalOrgPosUserTree').modal('hide');">关闭
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<style>
    article, aside, dialog, figcaption, figure, footer, header, hgroup, main, nav, section, span {
        display: inline;
    }
    .m-demo .m-demo__preview {
        padding: 0px;
    }
    .m-section {
        margin: 0 0 00px 0;
    }
    .m-section .m-section__heading {
        margin: 0 0 0px 0;
    }


    .m-list-badge .m-list-badge__items a.m-list-badge__item.active {
        color: #ffffff;
        background-color: #36a3f7;
    }
</style>
<script>
    var currentOrgId='${currentOrgId}';

    var settingLeft = {
        edit: {
            enable: false, //设置 zTree 是否处于编辑状态
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
            selectedMulti: false,//设置是否允许同时选中多个节点
            showTitle : false, //设置 zTree 是否显示节点的 title 提示信息(即节点 DOM 的 title 属性)。
            showLine: false //设置 zTree 是否显示节点之间的连线。
        },
        //使用异步加载，必须设置 setting.async 中的各个属性
        async: {
            //设置 zTree 是否开启异步加载模式
            enable: true,
            autoParam: ["id"],
            url:ctx+"/opus/common/om/org/getOpuOmOrgAsyncZTreeForId.do?rootId="+currentOrgId
            // url:"http://192.168.30.125:7080/opus-admin"+"/rest/opus/om/getOpuOmOrgAsyncZTreeForId.do?rootId="+currentOrgId
        },
        callback: {
            //单击事件
            onClick: onClickLeftOrgTree
            //右击事件
            // ,onRightClick:onRightClickLeftOrgTree
            //用于捕获异步加载正常结束的事件回调函数
            // ,onAsyncSuccess: onAsyncSuccessLeftOrgTree
        }
    };
    var orgLeftAsyncTree=null;
    $(document).ready(function () {
    })
    function showModalOrgPosUserTree() {
        $('#modalOrgPosUserTree').modal('show');
        orgLeftAsyncTree = $.fn.zTree.init($("#omOrgLeftTree"), settingLeft);
    }

    function onClickLeftOrgTree(event, treeId, treeNode, clickFlag) {
        var orgId=treeNode.id;
        var totalLevel=2;

        $.ajax({
            type: "post",
            url:ctx+"/opus/common/om/org/getUserOfOpuOmOrgByOrgIdAndLevel.do",
            data: {'orgId': orgId,'totalLevel':totalLevel},
            success: function (data) {
                initListUser('containerUserOfOrg',data)
            }
        })
    }
    function initListUser(containerId,dataList) {
        var container=$('#'+containerId);
        container.empty();
        var html='';
        $(dataList).each(function () {
            html+='<a href="#" class="m-list-badge__item" userId='+this.id+'>';
            html+=''+this.name+'';
            html+='</a>';
        })
        $(html).appendTo(container);
        initChooseUser()
    }
    function initChooseUser() {
        $('a.m-list-badge__item').each(function () {
            $(this).on('click',function () {
                $(this).toggleClass('active')
            })
        })
    }

    function confirmChooseUser() {
        var userIdStr='';
        var userNameStr='';
        $('a.m-list-badge__item.active').each(function () {
            userIdStr+=$(this).attr('userId')+';';
            userNameStr+=$(this).text()+';';
        })
        $('#person_msg_form #userId').val(userIdStr);
        $('#person_msg_form #userName').removeAttr('readonly');
        $('#person_msg_form #userName').val(userNameStr);
        $('#person_msg_form #userName').attr('readonly','readonly');
        $('#modalOrgPosUserTree').modal('hide');
    }
</script>
