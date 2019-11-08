<%@ page contentType="text/html;charset=UTF-8" %>
<style type="text/css">
    #matTypeRMenu {
        position: absolute;
        visibility: hidden;
        z-index: 90;
        background-color: #fff;
        width: 170px;
    }
    .list-group-item-mini {
        display: block;
        color: #595959;
        text-indent: 10px;
        height: 26px;
        font-size: 12px;
        padding: 5px;
        border: 1px solid rgba(0, 0, 0, 0.125);
        background-color: #fff;
        margin-bottom: -1px;
    }
    .list-group-item-mini:first-child {
        border-top-left-radius: 0.25rem;
        border-top-right-radius: 0.25rem;
    }
    .list-group-item-mini:last-child {
        border-bottom-left-radius: 0.25rem;;
        border-bottom-right-radius: 0.25rem;
        margin-bottom: 0;
    }
    #rMenu a:hover {
        background: #36a3f7;
        color: #ffff;
    }
    .list-group-item-mini i{
        margin-right: 10px;
    }
</style>
<div class="m-portlet__body" style="padding: 10px 0px;">
    <div class="row">
        <div class="col-xl-5">
            <input id="matTypeZtreeKeyWord" type="text" class="form-control m-input m-input--solid empty" placeholder="请输入关键字..."
                   style="background-color: #f0f0f075;border-color: #c4c5d6;">
        </div>
        <div class="col-xl-7">
            <button  type="button" class="btn btn-info" onclick="searchSelectMatTypeNode()" >查询</button>
            <button type="button" class="btn btn-danger" onclick="clearSearchItemType('#matTypeZtreeKeyWord');">清空</button>
            <%--<button type="button" class="btn btn-success" onclick="addItemMatType();">新增</button>--%>
            <button type="button" class="btn btn-secondary" onclick="expandTreeAllNode();">展开</button>
            <button type="button" class="btn btn-secondary" onclick="collapseAllNode();">折叠</button>
        </div>
    </div>
    <div style="margin: 10px 0px;border-bottom: 1px solid #e8e8e8;"></div>
    <div style="height:420px;overflow:auto; ">
        <ul id="matTypeTree" class="ztree"></ul>
    </div>
</div>

<div class="modal-footer" style="padding: 10px;height: 60px;">
    <button id="selectMatTypeBtn" type="button" onclick="selectMatType();" class="btn btn-info">选择</button>
    <button type="button" class="btn btn-secondary" onclick="goSetp('mat');">返回</button>
</div>

<%--<div id="matTypeRMenu">--%>
    <%--<a href="#" class="list-group-item-mini" onclick="addChildItemType();">--%>
        <%--<i class="fa flaticon-plus"></i>新增子类别--%>
    <%--</a>--%>
    <%--<a href="#" class="list-group-item-mini" onclick="editItemType();">--%>
        <%--<i class="fa flaticon-edit-1"></i>编辑类别--%>
    <%--</a>--%>
    <%--<a href="#" class="list-group-item-mini" onclick="deleteItemType();">--%>
        <%--<i class="fa fa-times"></i>删除类别--%>
    <%--</a>--%>
<%--</div>--%>


<div id="add_item_mat_type_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_item_mat_type_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_item_mat_type_modal_title"></h5>
                <button type="button" class="close" onclick="$('#add_item_mat_type_modal').modal('hide');" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">

                <form id="add_item_mat_type_form" method="post">

                    <input id="matTypeId" type="hidden" name="matTypeId" value=""/>
                    <input id="parentTypeId" type="hidden" name="parentTypeId" value=""/>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" for="typeCode" style="text-align: right;"><font color="red">*</font>分类编号:</label>
                        <div class="col-lg-4">
                            <input type="text" id="typeCode" name="typeCode" class="form-control m-input" placeholder="请输入分类编号"/>
                        </div>

                        <label class="col-lg-2 col-form-label" for="typeName" style="text-align: right;"><font color="red">*</font>分类名称:</label>
                        <div class="col-lg-4">
                            <input type="text" id="typeName" name="typeName" class="form-control m-input" placeholder="请输入分类名称"/>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" >
                        <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                        <div class="col-lg-10">
                            <textarea class="form-control" name="typeMemo" rows="3"></textarea>
                        </div>
                    </div>

                    <div class="form-group m-form__group row" style="text-align: right;">
                        <div class="col-lg-12">
                            <button type="submit" class="btn btn-info">保存</button>
                            <button type="button" class="btn btn-secondary" onclick="$('#add_item_mat_type_modal').modal('hide');">关闭</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>