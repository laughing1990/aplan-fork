<%@ page contentType="text/html;charset=UTF-8" %>
<style>
.selectlist{
width: 70px !important;
padding: 0  !important;
height: 27px  !important;
float: left;
}
.thback input{
float: left;
border: 1px solid #E4E4E4;
}
.modal-footer{
float: left;
border-top: 1px solid #ccc;
    width:100%;
}
.selectlist{
float: left;
}
.title{
background: #bfebff;
height: 37px;
text-align: center;
line-height: 37px;
}
.sm{
    width:95%;
}
td{
padding: 0.45rem !important;
}
.thback{
background:#EBFFFF;
}
table, tr, th, td{
border-color:#eaeaea !important;
vertical-align: middle;
}
.red{
color: red;
font-size: 19px;
}
</style>

<!-- 新增受理分类信息 -->
<div id="add_timeLimit_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_timeLimit_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_timeLimit_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <form id="add_timeLimit_form" method="post">

                    <input type="hidden" name="id" value=""/>
                    <input type="hidden" name="itemId" id="itemId" value=""/>

                    <table class="table table-bordered">
                        <tbody>
                        <tr>
                            <th scope="row" style="width:18%;" class="thback">
                                申请时限
                            </th>
                            <td colspan="5">
                                <input type="text" name="sqsx" placeholder="20个工作日" style="width:54%;" value=""/>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="thback">
                                受理时限
                            </th>
                            <td colspan="3" style="width:25%;">
                                <input type="text" name="slsx" placeholder="5"><span class="red">*</span>
                                <select class="form-control m-input m-input--square selectlist" name="slsxdw">
                                    <option  value="d">
                                        工作日
                                    </option>
                                    <option  value="w">
                                        周
                                    </option>
                                    <option  value="y">
                                        年
                                    </option>
                                </select>
                            </td>
                            <th scope="row" class="thback">
                                受理时限说明
                            </th>
                            <td colspan="3">
                                <input type="text" name="slsxsm" value="" placeholder="分局治安部门接到申请材料后，在5个工作日内（由治安管理大队）对申请材料" class="sm"/><span class="red">*</span>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="thback">
                                是否分情形
                            </th>
                            <td scope="row"  colspan="5">
                                <div class="m--padding-15">
                                    <label class="m-radio m-radio--info">
                                        <input type="radio" name="sffqx" value="1">
                                        <span></span>是
                                    </label>
                                    <label class="m-radio m-radio--info">
                                        <input type="radio" name="sffqx" value="2" checked="checked">否
                                        <span></span>
                                    </label>
                                </div>
                            </td>
                        </tr>
                        <%--<tr>
                            <th scope="row" class="thback">
                                法定办理时限
                            </th>
                            <td colspan="3">
                                <input type="text" name="" placeholder="10"><span class="red">*</span>
                                <select class="form-control m-input m-input--square selectlist" name="">
                                    <option  value="d">
                                        工作日
                                    </option>
                                    <option value="w">
                                        周
                                    </option>
                                    <option value="y">
                                        年
                                    </option>
                                </select>
                                <i class="fa fa-star"></i>
                            </td>
                            <th scope="row" class="thback">
                                法定办理时限说明
                            </th>
                            <td colspan="3">
                                <input type="text" name="" value="" placeholder="基层指导科收到申请材料后，在10个工作日内组织专家组人员会同公安分局" class="sm"/><span class="red">*</span>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="thback">
                                承诺办理时限
                            </th>
                            <td colspan="3">
                                <input type="text" name="" placeholder="5"><span class="red">*</span>
                                <select class="form-control m-input m-input--square selectlist" name="">
                                    <option  value="d">
                                        工作日
                                    </option>
                                    <option value="w">
                                        周
                                    </option>
                                    <option value="y">
                                        年
                                    </option>
                                </select>
                            </td>
                            <th scope="row" class="thback">
                                承诺办理时限说明
                            </th>
                            <td colspan="3">
                                <input type="text" name="" placeholder="在5日内（由基层指导科、治安巡警支队、分管局领导）进行审批后，发放" class="sm"/><span class="red">*</span>
                            </td>
                        </tr>
                        <tr>--%>
                            <th scope="row" rowspan="5" class="thback">
                                单部门跨层级承诺办理时限划分
                            </th>
                            <th scope="row" class="thback">
                                国家级办理时限
                            </th>
                            <td colspan="5">
                                <input type="text" name="gjjblsx" placeholder="5">
                                <select class="form-control m-input m-input--square selectlist" name="gjjblsxdw" >
                                    <option  value="d">
                                        工作日
                                    </option>
                                    <option  value="w">
                                        周
                                    </option>
                                    <option  value="y">
                                        年
                                    </option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="thback">
                                省级办理时限
                            </th>
                            <td colspan="5">
                                <input type="text" name="sjblsx" placeholder="5">
                                <select class="form-control m-input m-input--square selectlist" name="sjblsxdw" >
                                    <option  value="d">
                                        工作日
                                    </option>
                                    <option  value="w">
                                        周
                                    </option>
                                    <option  value="y">
                                        年
                                    </option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="thback">
                                市级办理时限
                            </th>
                            <td colspan="5">
                                <input type="text" name="dsjblsx" placeholder="5">
                                <select class="form-control m-input m-input--square selectlist" name="dsjblsxdw" >
                                    <option  value="d">
                                        工作日
                                    </option>
                                    <option value="w">
                                        周
                                    </option>
                                    <option value="y">
                                        年
                                    </option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="thback">
                                县（市、区）级办理时限
                            </th>
                            <td colspan="5">
                                <input type="text" name="xjblsx" placeholder="5">
                                <select class="form-control m-input m-input--square selectlist" name="xjblsxdw">
                                    <option  value="d">
                                        工作日
                                    </option>
                                    <option value="w">
                                        周
                                    </option>
                                    <option value="y">
                                        年
                                    </option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="thback">
                                镇（街）级办理时限
                            </th>
                            <td colspan="5">
                                <input type="text" name="zjblsx" placeholder="5">
                                <select class="form-control m-input m-input--square selectlist" name="zjblsxdw">
                                    <option  value="d">
                                        工作日
                                    </option>
                                    <option  value="w">
                                        周
                                    </option>
                                    <option value="y">
                                        年
                                    </option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="thback">
                                有无特殊程序
                            </th>
                            <td scope="row"  colspan="5">
                                <div class="m--padding-15">
                                    <label class="m-radio m-radio--info">
                                        <input type="radio" name="ywtbcx" value="1">
                                        <span></span>有
                                    </label>
                                    <label class="m-radio m-radio--info">
                                        <input type="radio" name="ywtbcx" value="2" checked="checked">无
                                        <span></span>
                                    </label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th scope="row" class="thback">
                                时限分类
                            </th>
                            <td scope="row"  colspan="5">
                                <div class="m--padding-15">
                                    <label class="m-radio m-radio--info">
                                        <input type="radio" name="timelimitType" value="c">
                                        <span></span>串联审批
                                    </label>
                                    <label class="m-radio m-radio--info">
                                        <input type="radio" name="timelimitType" value="b" checked="checked">并联审批
                                        <span></span>
                                    </label>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <div class="modal-footer" style="padding: 15px;">
                        <div class="btnbox">
                            <button type="button" class="btn btn-info">暂存</button>
                            <button type="submit" class="btn btn-info">保存</button>
                            <button type="button" class="btn btn-info">提交审查</button>
                            <button type="button" class="btn btn-info">关闭</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>