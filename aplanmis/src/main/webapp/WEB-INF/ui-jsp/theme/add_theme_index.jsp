<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 新增主题信息 -->
<div id="add_theme_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_theme_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_theme_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="add_theme_form" method="post">
                <div class="modal-body" style="padding: 10px;">
                    <ul class="nav nav-tabs" role="tablist">
                        <li class="nav-item">
                            <a id="themeBasicInfo" class="nav-link active" data-toggle="tab" href="#m_tabs_1_1">
                                <i class="la la-map-marker"></i>
                                基本信息
                            </a>
                        </li>
                        <li class="nav-item">
                            <a id="themeOtherInfo" class="nav-link" data-toggle="tab" href="#m_tabs_1_2">
                                <i class="la la-gear"></i>
                                扩展信息
                            </a>
                        </li>
                    </ul>
                    <div class="tab-content">

                        <input type="hidden" name="themeId" value=""/>

                        <div id="m_tabs_1_1" class="tab-pane active" role="tabpanel" style="height: 450px;overflow-x: hidden;overflow-y: auto;">

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>主题名称:</label>
                                <div class="col-lg-10">
                                    <input type="text" class="form-control m-input" name="themeName" value=""/>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>主题编码:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="themeCode" value=""/>
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否允许网上申报:</label>
                                <div class="col-lg-4">
                                    <select type="text" class="form-control" name="isOnlineSb" value="">
                                        <option value="1">允许</option>
                                        <option value="0">不予许</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group m-form__group row"  >

                                <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>主题类型:</label>
                                <div class="col-lg-4">
                                    <select type="text" class="form-control" name="themeType" value="">
                                        <option value="">请选择</option>
                                        <c:forEach items="${themeTypes}" var="themeType">
                                            <option value="${themeType.itemCode}">${themeType.itemName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>审批流程类型:</label>
                                <div class="col-lg-4">
                                    <select type="text" class="form-control" name="gjbzsplclx" value="">
                                        <option value="">请选择</option>
                                        <c:forEach items="${gzbzsplclxs}" var="gzbzsplclx">
                                            <option value="${gzbzsplclx.itemCode}">${gzbzsplclx.itemName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <%--<label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序:</label>--%>
                                <%--<div class="col-lg-4">--%>
                                    <%--<input type="number" class="form-control m-input" name="sortNo" value="1"/>--%>
                                <%--</div>--%>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">承诺办结时限计量:</label>
                                <div class="col-lg-4">
                                    <input type="number" class="form-control m-input" name="dueNum" value="1"/>
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;">承诺办结时限单位:</label>
                                <div class="col-lg-4">
                                    <select type="text" class="form-control" name="dueUnit" value="">
                                        <option value="">请选择</option>
                                        <c:forEach items="${dueUnits}" var="dueUnit">
                                            <option value="${dueUnit.itemCode}">${dueUnit.itemName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用并联审批:</label>
                                <div class="col-4">
                                    <select type="text" class="form-control" name="isMainline" value="">
                                        <option value="">请选择</option>
                                        <option value="1">启用</option>
                                        <option value="0">禁用</option>
                                    </select>
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;">并联审批别名:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="mainlineAlias" value=""/>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用服务协同:</label>
                                <div class="col-4">
                                    <select type="text" class="form-control" name="isAuxiline" value="">
                                        <option value="">请选择</option>
                                        <option value="1">启用</option>
                                        <option value="0">禁用</option>
                                    </select>
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;">服务协同别名:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="auxilineAlias" value=""/>
                                </div>
                            </div>

                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用技术审查线:</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<select type="text" class="form-control" name="isTechspectline" value="">--%>
                                        <%--<option value="">请选择</option>--%>
                                        <%--<option value="0">禁用</option>--%>
                                        <%--<option value="1">启用</option>--%>
                                    <%--</select>--%>
                                <%--</div>--%>

                                <%--<label class="col-lg-2 col-form-label" style="text-align: right;">技术审查线别名:</label>--%>
                                <%--<div class="col-lg-4">--%>
                                    <%--<input type="text" class="form-control m-input" name="techspectlineAlias" value=""/>--%>
                                <%--</div>--%>
                            <%--</div>--%>

                            <div class="form-group m-form__group row">
                                <label class="col-2 col-form-label" style="text-align: right;">图标样式启用:</label>
                                <div class="col-4">
                                    <select type="text" class="form-control" name="isImgIcon" value="">
                                        <option value="">请选择</option>
                                        <option value="0">CSS样式图标</option>
                                        <option value="1">图片样式图标</option>
                                    </select>
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;">图标CSS样式:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="themeIconCss" value=""/>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">大图标路径:</label>
                                <div class="col-10 input-group">
                                    <input id="bigImgPath" class="form-control m-input" type="text" value=""
                                           name="bigImgPath" placeholder="请选择大图标(32*32)" onclick="isSelectImgType(this, 'big');">
                                    <div class="input-group-append">
                                        <span class="input-group-text open-big-ico" onclick="selectImgType('theme','bigImgPath','big');">
                                            <i class="la la-tag">大图标</i>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>申报说明:</label>
                                <div class="col-lg-10">
                                    <textarea class="form-control" name="themeMemo" rows="3"></textarea>
                                </div>
                            </div>

                        </div>
                        <div class="tab-pane" id="m_tabs_1_2" role="tabpanel" style="height: 450px;">

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">投诉电话:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="complainPhone" value=""  />
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;">咨询电话:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="hotlinePhone" value=""  />
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">办理地点:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="handleAddress" value=""  />
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;">办理时间:</label>
                                <div class="col-lg-4">
                                    <input type="datetime-local" class="form-control m-input" name="handleTime" value=""/>
                                </div>
                            </div>

                            <%--<div class="form-group m-form__group row">--%>
                                <%--<label class="col-2 col-form-label" style="text-align: right;">图标样式启用:</label>--%>
                                <%--<div class="col-4">--%>
                                    <%--<select type="text" class="form-control" name="isImgIcon" value="">--%>
                                        <%--<option value="">请选择</option>--%>
                                        <%--<option value="0">CSS样式图标</option>--%>
                                        <%--<option value="1">图片样式图标</option>--%>
                                    <%--</select>--%>
                                <%--</div>--%>

                                <%--<label class="col-lg-2 col-form-label" style="text-align: right;">图标CSS样式:</label>--%>
                                <%--<div class="col-lg-4">--%>
                                    <%--<input type="text" class="form-control m-input" name="themeIconCss" value=""/>--%>
                                <%--</div>--%>
                            <%--</div>--%>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">小图标路径:</label>
                                <div class="col-10 input-group">
                                    <input id="smallImgPath" class="form-control m-input" type="text" value=""
                                           name="smallImgPath" placeholder="请选择小图标(16*16)" onclick="isSelectImgType(this, 'small');">
                                    <div class="input-group-append">
                                        <span class="input-group-text open-small-ico"
                                              onclick="selectImgType('theme','smallImgPath','small');">
                                            <i class="la la-tag">小图标</i>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">中图标路径:</label>
                                <div class="col-10 input-group">
                                    <input id="middleImgPath" class="form-control m-input" type="text" value=""
                                           name="middleImgPath" placeholder="请选择中图标(24*24)" onclick="isSelectImgType(this, 'middle');">
                                    <div class="input-group-append">
                                <span class="input-group-text open-middle-ico" onclick="selectImgType('theme','middleImgPath','middle');">
                                    <i class="la la-tag">中图标</i>
                                </span>
                                    </div>
                                </div>
                            </div>

                            <%--<div class="form-group m-form__group row" >--%>
                                <%--<label class="col-lg-2 col-form-label" style="text-align: right;">大图标路径:</label>--%>
                                <%--<div class="col-10 input-group">--%>
                                    <%--<input id="bigImgPath" class="form-control m-input" type="text" value=""--%>
                                           <%--name="bigImgPath" placeholder="请选择大图标(32*32)" >--%>
                                    <%--<div class="input-group-append">--%>
                                <%--<span class="input-group-text open-mat-type" onclick="selectImgType('theme','bigImgPath','big');">--%>
                                    <%--<i class="la la-tag">大图标</i>--%>
                                <%--</span>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">超大图标路径:</label>
                                <div class="col-10 input-group">
                                    <input id="hugeImgPath" class="form-control m-input" type="text" value=""
                                           name="hugeImgPath" placeholder="请选择超大图标(64*64)" onclick="isSelectImgType(this, 'huge');">
                                    <div class="input-group-append">
                                <span class="input-group-text open-huge-ico" onclick="selectImgType('theme','hugeImgPath','huge');">
                                    <i class="la la-tag">超大图标</i>
                                </span>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>

                    <div class="modal-footer" style="padding: 10px;">
                        <button id="submitAndNew" type="submit" class="btn btn-info">保存并新增</button>
                        <button id="submitAndNoNew" type="submit" class="btn btn-info">保存</button>
                        <button id="closeAddThemeBtn" type="button" class="btn btn-secondary">关闭</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>