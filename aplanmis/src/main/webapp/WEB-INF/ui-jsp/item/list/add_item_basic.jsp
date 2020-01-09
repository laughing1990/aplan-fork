<%@ page contentType="text/html;charset=UTF-8" %>

<style type="text/css">
   .m-form__help{
       display: inline-block;
       color: #7b7e8a;
       font-weight: 300;
       font-size: 0.85rem;
       padding-top: 7px;
   }
</style>

<!-- 添加/编辑 -->
<div id="aedit_item_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="dialog_item_dept" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width: 1000px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="aedit_item_modal_title">新增事项</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="aedit_item_form" method="post" enctype="multipart/form-data">
                <div class="modal-body" style="padding: 10px;">
                    <div id="aedit_item_scroll" style="height: 500px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="itemBasicId" value=""/>
                        <input type="hidden" name="itemId" value=""/>
                        <input type="hidden" name="parentItemId" value=""/>
                        <input type="hidden" name="orgId" value=""/>
                        <input type="hidden" name="itemVerId" value=""/>
                        <input type="hidden" name="isLocal" value=""/>
                        <input type="hidden" name="itemNature" value=""/>
                        <input type="hidden" name="isCatalog" value=""/>
                        <input type="hidden" name="regionId" value=""/>
                        <input type="hidden" name="sxmlzt" value=""/>
                        <input type="hidden" name="appId" value=""/>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>事项名称:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="itemName" value="" onchange="changeItemNameCnToEn(this);"/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">事项别名:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="itemAlias" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>编码:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="itemCode" value=""/>
                                <span class="m-form__help">
                                    可填写标准事项编码或者实施事项编码
                                </span>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">上级编码:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="basecode" value=""/>
                                <span class="m-form__help">
                                    实施事项的上级编码为标准事项编码,子实施事项的上级编码为实施事项编码，如此类推
                                </span>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">国家标准事项编码:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="standardItemCode" value=""/>
                                <span class="m-form__help">
                                    参照住建部数据共享交换标准要求填写！
                                </span>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>唯一分类标记:</label>
                            <div class="col-lg-10">
                                <input type="text" class="form-control m-input" name="itemCategoryMark" value=""/>
                                <span class="m-form__help">事项分类唯一标记（仅供工作流使用）,标准事项新增时填写，实施事项沿用！</span>
                            </div>
                        </div>

                        <div id="itemExchangeWayDiv" class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;padding-top: 6px;"><font color="red">*</font>实施事项换算方式:</label>
                            <div class="col-10">
                                <div class="m-radio-inline">
                                    <label class="m-radio" style="width: 200px;">
                                        <input type="radio" name="itemExchangeWay" value="1" checked>仅按照审批行政区划<span></span>
                                    </label>
                                    <label class="m-radio" style="width: 280px;">
                                        <input type="radio" name="itemExchangeWay" value="0" >按照审批行政区划和属地行政区划<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="itemExchangeWay" value="2" >按照EL表达式<span></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div id="elContentDiv" class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align: right;padding-top: 6px;"><font color="red">*</font>EL表达式:</label>
                            <div class="col-10">
                                <textarea class="form-control" name="elContent" rows="3"></textarea>
                                <span class="m-form__help">
                                    如所属标准事项的“实施事项换算方式”项设置为“按照EL表达式”，则须填写EL表达式
                                </span>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">审批行政区划:</label>
                            <div class="col-lg-4 input-group">
                                <input type="text" class="form-control m-input" name="regionName"
                                       placeholder="请点击选择" aria-describedby="select_region_Id" readonly
                                       onclick="isSelectBscDicRegion(this, false);">
                                <div class="input-group-append">
                                    <span id="select_region_Id" class="input-group-text"
                                          onclick="isSelectBscDicRegion(null, false);">
                                        <i class="la la-search"></i>
                                    </span>
                                </div>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red" id="org_necessary_mark">*</font>所属部门:</label>
                            <div class="col-lg-4 input-group" id="item_basic_org_name">
                                <input type="text" class="form-control m-input" name="orgName"
                                       placeholder="请点击选择" aria-describedby="select_org_Id" readonly
                                       onclick="isSelectOpuOmOrg(this, false);">
                                <div class="input-group-append">
                                    <span id="select_org_Id" class="input-group-text"
                                          onclick="isSelectOpuOmOrg(null, false);">
                                        <i class="la la-group"></i>
                                    </span>
                                </div>
                            </div>

                            <%--标准事项机构--%>
                            <div class="col-lg-4" id="item_basic_guide_org_name">
                                <input type="text" class="form-control m-input" name="guideOrgName" value=""/>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                <font color="red">*</font>是否分情形:
                            </label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="isNeedState" value="">
                                    <option value="1">是</option>
                                    <option value="0">否</option>
                                </select>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">事项类型:</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="itemType" value="">
                                    <option value="">请选择</option>
                                    <c:forEach items="${itemTypes}" var="itemTypeVo">
                                        <option value="${itemTypeVo.itemCode}">${itemTypeVo.itemName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序号:</label>
                            <div class="col-lg-4">
                                <input type="number" class="form-control m-input" name="acceptMode" value="1"/>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">办件类型:</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="itemProperty" value="">
                                    <option value="">请选择</option>
                                    <c:forEach items="${itemPropertys}" var="itemProperty">
                                        <option value="${itemProperty.itemCode}">${itemProperty.itemName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-2 col-form-label" style="text-align: right;">受理方式:</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="slfs" value="">
                                    <option value="">请选择</option>
                                    <c:forEach items="${slfss}" var="slfs">
                                        <option value="${slfs.itemCode}">${slfs.itemName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">事项机制:</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="sfsxgzcnz" value="">
                                    <option value="0">实行审批制</option>
                                    <option value="1">实行告知承诺制</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                               是否里程碑事项:
                            </label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="isMilestoneItem" value="">
                                    <option value="">请选择</option>
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                是否本系统处理事项:
                            </label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="outerSystemHandle" value="">
                                    <option value="">请选择</option>
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-2 col-form-label" style="text-align: right;">承诺办结时限计量:</label>
                            <div class="col-lg-4">
                                <input type="number" class="form-control m-input" name="dueNum" value="1" placeholder="请填写整数..."/>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">承诺办结时限单位:</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="bjType" value="">
                                    <option value="">请选择</option>
                                    <c:forEach items="${dueUnits}" var="dueUnit">
                                        <option value="${dueUnit.itemCode}">${dueUnit.itemName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >

                            <label class="col-lg-2 col-form-label" style="text-align: right;">法定办结时限计量:</label>
                            <div class="col-lg-4">
                                <input type="number" class="form-control m-input" name="anticipateDay" value="1" placeholder="请填写整数..."/>
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

                        <div id="isLinkDiv" class="form-group m-form__group row" >

                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用"一张表单":</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="useOneForm" value="">
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">
                                是否链接式办事指南:
                            </label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="isLink" value="">
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>
                        </div>

                        <div id="isCheckItemDiv" class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">是否检测前置事项:</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="isCheckItem" value="">
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;">是否检测扩展表单:</label>
                            <div class="col-lg-4">
                                <select type="text" class="form-control" name="isCheckPartform" value="">
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label isCheckProjDiv" style="text-align: right;">是否检测项目信息:</label>
                            <div class="col-lg-4 isCheckProjDiv">
                                <select type="text" class="form-control" name="isCheckProj" value="">
                                    <option value="0">否</option>
                                    <option value="1">是</option>
                                </select>
                            </div>
                            <div class="col-lg-6"></div>

                            <%--<label class="col-lg-2 col-form-label" style="text-align: right;">是否绿色通道:</label>--%>
                            <%--<div class="col-lg-4">--%>
                                <%--<select type="text" class="form-control" name="isGreenWay" value="">--%>
                                    <%--<option value="0">否</option>--%>
                                    <%--<option value="1">是</option>--%>
                                <%--</select>--%>
                            <%--</div>--%>
                        </div>

                        <%--<div class="form-group m-form__group row" style="display:${itemNature=='8'?'':'none'}">--%>
                            <%--<label class="col-lg-2 col-form-label" style="text-align: right;">是否共享:</label>--%>
                            <%--<div class="col-lg-4">--%>
                                <%--<select type="text" class="form-control" name="isShare" value="">--%>
                                    <%--<option value="0">否</option>--%>
                                    <%--<option value="1">是</option>--%>
                                <%--</select>--%>
                            <%--</div>--%>
                            <%--<div class="col-lg-6"></div>--%>
                        <%--</div>--%>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">办理结果送达方式:</label>
                            <div class="col-lg-10">
                                <input type="hidden" name="sendResultMode" value=""/>
                                <div class="m-checkbox-inline">
                                    <c:forEach items="${sendResultModes}" var="sendResultMode">
                                        <label class="m-checkbox">
                                            <input type="checkbox" name="sendResultMode1" value="${sendResultMode.itemCode}" onclick="checkboxOnclick('sendResultMode1','sendResultMode',true)">${sendResultMode.itemName}<span></span>
                                        </label>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">服务对象:</label>
                            <div class="col-lg-10">
                                <input type="hidden" name="xkdx" value=""/>
                                <div class="m-checkbox-inline">
                                    <c:forEach items="${itemFwjgxzs}" var="xkdx">
                                        <label class="m-checkbox">
                                            <input type="checkbox" name="xkdx1" value="${xkdx.itemCode}" onclick="checkboxOnclick('xkdx1','xkdx',true)">${xkdx.itemName}<span></span>
                                        </label>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">文书模版:</label>
                            <div class="col-lg-10">
                                <input id="docTemplateFile" type="file" name="docTemplateFile" class="form-control m-input"
                                       placeholder="" accept="image/*" >
                                <span class="custorm-style">
								 <button class="left-button">选择文件</button>
								 <span class="right-text" id="rightText1">未选择任何文件</span>
							    </span>
                                <span id="docTemplateAtt">
									已经上传的：<a id="docTemplateAtta" href="#" target="_blank">文书模版附件</a>
                                    <span id="docTemplateAtts" style="margin-left: 10px;" onclick="delItemBasicAtt('docTemplate');">&times;</span>
								</span>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">申请表范本:</label>
                            <div class="col-lg-10">
                                <input id="applyTableDemoFile" type="file" name="applyTableDemoFile" class="form-control m-input"
                                       placeholder="" accept="image/*" >
                                <span class="custorm-style">
								 	<button class="left-button">选择文件</button>
								 	<span class="right-text" id="rightText2">未选择任何文件</span>
								</span>
                                <span id="applyTableDemoAtt">
									已经上传的：<a id="applyTableDemoAtta" href="#" target="_blank">申请表范本附件</a>
                                    <span id="applyTableDemoAtts" style="margin-left: 10px;" onclick="delItemBasicAtt('applyTableDemo');">&times;</span>
								</span>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align: right;">申请表模版:</label>
                            <div class="col-lg-10">
                                <input id="applyTableTemplateFile" type="file" name="applyTableTemplateFile" class="form-control m-input"
                                       placeholder="" accept="image/*" >
                                <span class="custorm-style">
									 <button class="left-button">选择文件</button>
									 <span class="right-text" id="rightText3">未选择任何文件</span>
							    </span>
                                <span id="applyTableTemplateAtt">
									已经上传的：<a id="applyTableTemplateAtta" href="#" target="_blank">申请表模版附件</a>
                                    <span id="applyTableTemplateAtts" style="margin-left: 10px;" onclick="delItemBasicAtt('applyTableTemplate');">&times;</span>
								</span>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">由第三方系统处理说明:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="outerSystemDesc" rows="3"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">跳转至第三方系统的URL地址:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="outerSystemUrl" rows="3"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;">备注:</label>
                            <div class="col-lg-10">
                                <textarea class="form-control" name="itemMemo" rows="3"></textarea>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button type="submit" class="btn btn-info" id="saveItemBasic">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>