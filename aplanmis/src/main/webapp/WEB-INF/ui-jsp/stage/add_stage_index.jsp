<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 新增阶段信息 -->
<div id="add_stage_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_stage_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 900px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="add_stage_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="add_stage_form" method="post">
                <div class="modal-body" style="padding: 10px;">

                    <ul class="nav nav-tabs" role="tablist">
                        <li class="nav-item">
                            <a id="stageBasicInfo" class="nav-link active" data-toggle="tab" href="#m_tabs_1_1">
                                <i class="la la-map-marker"></i>
                                基本信息
                            </a>
                        </li>
                        <li class="nav-item">
                            <a id="stageOtherInfo" class="nav-link" data-toggle="tab" href="#m_tabs_1_2">
                                <i class="la la-gear"></i>
                                扩展信息
                            </a>
                        </li>
                    </ul>

                    <div class="tab-content">

                        <input type="hidden" name="stageId" value=""/>
                        <input type="hidden" name="themeVerId" value="" />
                        <input type="hidden" name="isNode" value="1"/>
                        <input type="hidden" name="isOptionItem" value=""/>
                        <input type="hidden" name="isSelItem" value=""/>
                        <input type="hidden" name="useEl" value=""/>
                        <input type="hidden" name="isShowItem" value=""/>

                        <input type="hidden" name="useOneForm" value=""/>
                        <input type="hidden" name="isCheckItem" value=""/>
                        <input type="hidden" name="isCheckItemform" value=""/>
                        <input type="hidden" name="isCheckPartform" value=""/>
                        <input type="hidden" name="isCheckProj" value=""/>
                        <input type="hidden" name="isCheckStage" value=""/>
                        <input type="hidden" name="isCreateSubproj" value=""/>

                        <div id="m_tabs_1_1" class="tab-pane active" role="tabpanel" style="height: 450px;overflow-x: hidden;overflow-y: auto;">

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>名称:</label>
                                <div class="col-lg-10">
                                    <input type="text" class="form-control m-input" name="stageName" value=""/>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>编号:</label>
                                <div class="col-lg-10">
                                    <input type="text" class="form-control m-input" name="stageCode" value=""/>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >

                                <label class="col-2 col-form-label" style="text-align: right;"></label>
                                <div class="col-10">
                                    <div class="m-checkbox-inline">
                                        <label class="m-checkbox" style="width: 289px;">
                                            <input type="checkbox" name="isOptionItem1" onclick="checkboxOnclick('isOptionItem1','isOptionItem',false);">是否允许设置并行推进事项<span></span>
                                        </label>

                                        <label class="m-checkbox">
                                            <input type="checkbox" name="isSelItem1" onclick="checkboxOnclick('isSelItem1','isSelItem',false);">是否允许申报时勾选审批事项<span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >

                                <label class="col-2 col-form-label" style="text-align: right;"></label>
                                <div class="col-10">
                                    <div class="m-checkbox-inline">
                                        <label class="m-checkbox" style="width: 289px;">
                                            <input type="checkbox" name="useOneForm1" onclick="checkboxOnclick('useOneForm1','useOneForm',false);">是否启用"一张表单"<span></span>
                                        </label>

                                        <label class="m-checkbox">
                                            <input type="checkbox" name="useEl1" onclick="checkboxOnclick('useEl1','useEl',false);">是否启用EL<span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-2 col-form-label" style="text-align: right;"></label>
                                <div class="col-10">
                                    <div class="m-checkbox-inline">
                                        <label class="m-checkbox" style="width: 289px;">
                                            <input type="checkbox" name="isCheckItem1" onclick="checkboxOnclick('isCheckItem1','isCheckItem', false);">是否检测前置事项<span></span>
                                        </label>

                                        <label class="m-checkbox" style="width: 289px;">
                                            <input type="checkbox" name="isCheckItemform1" onclick="checkboxOnclick('isCheckItemform1','isCheckItemform',false);">是否检测事项表单<span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-2 col-form-label" style="text-align: right;"></label>
                                <div class="col-10">
                                    <div class="m-checkbox-inline">
                                        <label class="m-checkbox" style="width: 289px;">
                                            <input type="checkbox" name="isCheckPartform1" onclick="checkboxOnclick('isCheckPartform1','isCheckPartform', false);">是否检测扩展表单<span></span>
                                        </label>

                                        <label class="m-checkbox" style="width: 289px;">
                                            <input type="checkbox" name="isCheckProj1" onclick="checkboxOnclick('isCheckProj1','isCheckProj',false);">是否检测项目信息<span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-2 col-form-label" style="text-align: right;"></label>
                                <div class="col-10">
                                    <div class="m-checkbox-inline">
                                        <label class="m-checkbox" style="width: 289px;">
                                            <input type="checkbox" name="isCheckStage1" onclick="checkboxOnclick('isCheckStage1','isCheckStage', false);">是否检测前置阶段<span></span>
                                        </label>

                                        <label id="isShowItemDiv" class="m-checkbox">
                                            <input type="checkbox" name="isShowItem1" onclick="checkboxOnclick('isShowItem1', 'isShowItem', false);">是否允许在线运行图辅线展示事项<span></span>
                                        </label>

                                        <label id="isCreateSubprojDiv" class="m-checkbox">
                                            <input type="checkbox" name="isCreateSubproj1" onclick="checkboxOnclick('isCreateSubproj1', 'isCreateSubproj', false);">是否允许创建子工程<span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div id="isCreateSubprojDiv2" class="form-group m-form__group row">
                                <label class="col-2 col-form-label" style="text-align: right;"></label>
                                <div class="col-10">
                                    <div class="m-checkbox-inline">
                                        <label class="m-checkbox">
                                            <input type="checkbox" name="isCreateSubproj2" onclick="checkboxOnclick('isCreateSubproj2', 'isCreateSubproj', false);">是否允许创建子工程<span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-2 col-form-label" style="text-align: right;"><font color="red">*</font>梳理方式:</label>
                                <div class="col-10">
                                    <div class="m-radio-inline">
                                        <label class="m-radio" style="width: 289px;">
                                            <input type="radio" name="handWay" value="1" checked>面向阶段的情形与材料梳理<span></span>
                                        </label>
                                        <label class="m-radio">
                                            <input type="radio" name="handWay" value="0">面向阶段的单事项合并梳理<span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-2 col-form-label" style="text-align: right;"><font color="red">*</font>里程碑事项类型:</label>
                                <div class="col-10">
                                    <div class="m-radio-inline">
                                        <label class="m-radio">
                                            <input type="radio" name="lcbsxlx" value="1" checked>所有里程碑事项办结，该审批阶段才算办结<span></span>
                                        </label>
                                        <label class="m-radio">
                                            <input type="radio" name="lcbsxlx" value="2">任一项里程碑事项办结，该审批阶段就算办结<span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-lg-2 col-form-label" style="text-align: right;padding-top: 0px;">申报时并联审&nbsp;&nbsp;<br/>批事项显示数量:</label>
                                <div class="col-lg-4">
                                    <input type="number" class="form-control m-input" name="noptItemShowNum" value="0"/>
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;padding-top: 0px;">申报时并行推&nbsp;&nbsp;<br/>进事项显示数量:</label>
                                <div class="col-lg-4">
                                    <input type="number" class="form-control m-input" name="optItemShowNum" value="0"/>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-2 col-form-label" style="text-align: right;padding-top: 0px;"><font color="red">*</font>对应国家标&nbsp;<br/>准审批阶段:</label>
                                <input type="hidden" name="dybzspjdxh" value=""/>
                                <div class="col-10">
                                    <div class="m-checkbox-inline">
                                        <c:forEach items="${dybzspjdxhs}" var="dybzspjdxh">
                                            <label class="m-checkbox">
                                                <input type="checkbox" name="dybzspjdxh1" value="${dybzspjdxh.itemCode}" onclick="checkboxOnclick('dybzspjdxh1', 'dybzspjdxh', true)">${dybzspjdxh.itemName}<span></span>
                                            </label>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>

                            <div id="dybzfxfwDiv" class="form-group m-form__group row">
                                <label class="col-2 col-form-label" style="text-align: right;padding-top: 0px;"><font color="red">*</font>对应国家标&nbsp;<br/>准辅线服务:</label>
                                <div class="col-10">
                                    <div class="m-radio-inline">
                                        <c:forEach items="${dybzfxfws}" var="dybzfxfw">
                                            <label class="m-radio">
                                                <input type="radio" name="dygjbzfxfw" value="${dybzfxfw.itemCode}">${dybzfxfw.itemName}<span></span>
                                            </label>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>

                            <div id="stageElDiv" class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">
                                    <font color="red">*</font>EL表达式:<br/>
                                    <button type="button" class="btn btn-info" onclick="openSelectMetaDbTbcolModal('stageEl');" style="margin-top: 10px;">选择</button>
                                </label>
                                <div class="col-lg-10">
                                    <textarea id="stageEl" class="form-control" name="stageEl" rows="5"></textarea>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                                <div class="col-lg-10">
                                    <textarea class="form-control" name="stageMemo" rows="3"></textarea>
                                </div>
                            </div>

                        </div>
                        <div id="m_tabs_1_2" class="tab-pane" role="tabpanel" style="height: 450px;">

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
                                <label class="col-lg-2 col-form-label" style="text-align: right;">法定办结时限计量:</label>
                                <div class="col-lg-4">
                                    <input type="number" class="form-control m-input" name="anticipateDay" value="1" />
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;">法定办结时限单位:</label>
                                <div class="col-lg-4">
                                    <select type="text" class="form-control" name="anticipateType" value="">
                                        <option value="">请选择</option>
                                        <c:forEach items="${dueUnits}" var="dueUnit">
                                            <option value="${dueUnit.itemCode}">${dueUnit.itemName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group m-form__group row">
                                <label class="col-2 col-form-label" style="text-align: right;">图标样式启用:</label>
                                <div class="col-4">
                                    <select type="text" class="form-control" name="isImgIcon" value="">
                                        <%--<option value="">请选择</option>--%>
                                        <option value="0">CSS样式图标</option>
                                        <option value="1">图片样式图标</option>
                                    </select>
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;">排序:</label>
                                <div class="col-lg-4">
                                    <input type="number" class="form-control m-input" name="sortNo" value="1"/>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">图标CSS样式:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="iconCss" value=""  />
                                </div>

                                <label class="col-lg-2 col-form-label" style="text-align: right;">背景CSS样式:</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control m-input" name="bgCss" value=""  />
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">小图标路径:</label>
                                <div class="col-10 input-group">
                                    <input id="smallImgPath" class="form-control m-input" type="text" value=""
                                           name="smallImgPath" placeholder="请选择小图标(16*16)" onclick="isSelectImgType(this, 'small');">
                                    <div class="input-group-append">
                                <span class="input-group-text open-mat-type" onclick="selectImgType('stage','smallImgPath','small');">
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
                                <span class="input-group-text open-mat-type" onclick="selectImgType('stage','middleImgPath','middle');">
                                    <i class="la la-tag">中图标</i>
                                </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">大图标路径:</label>
                                <div class="col-10 input-group">
                                    <input id="bigImgPath" class="form-control m-input" type="text" value=""
                                           name="bigImgPath" placeholder="请选择大图标(32*32)" onclick="isSelectImgType(this, 'big');">
                                    <div class="input-group-append">
                                <span class="input-group-text open-mat-type" onclick="selectImgType('stage','bigImgPath','big');">
                                    <i class="la la-tag">大图标</i>
                                </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">超大图标路径:</label>
                                <div class="col-10 input-group">
                                    <input id="hugeImgPath" class="form-control m-input" type="text" value=""
                                           name="hugeImgPath" placeholder="请选择超大图标(64*64)" onclick="isSelectImgType(this, 'huge');">
                                    <div class="input-group-append">
                                <span class="input-group-text open-mat-type" onclick="selectImgType('stage','hugeImgPath','huge');">
                                    <i class="la la-tag">超大图标</i>
                                </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group m-form__group row" >
                                <label class="col-lg-2 col-form-label" style="text-align: right;">状态检测URL:</label>
                                <div class="col-lg-10">
                                    <input type="text" class="form-control m-input" name="statusCheckUrl" value=""/>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;">
                    <button id="addStageBtn" type="submit" class="btn btn-info">保存</button>
                    <button id="closeAddStageBtn" type="button" class="btn btn-secondary">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>