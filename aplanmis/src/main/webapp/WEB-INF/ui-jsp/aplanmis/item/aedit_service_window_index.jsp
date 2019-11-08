<%@ page contentType="text/html;charset=UTF-8" %>

<div id="service_window_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="service_window_modal" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered  modal-lg" role="document" style="max-width:900px;">
        <div class="modal-content">
            <!-- 标题 -->
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="service_window_modal_title">编辑服务窗口</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="service_window_form" method="post" enctype="multipart/form-data">
                <div class="modal-body" style="padding: 10px;">
                    <div id="service_window_scroll" style="height: 500px;overflow-x: hidden;overflow-y: auto;">

                        <input type="hidden" name="windowId" value=""/>
                        <input type="hidden" name="regionId" value=""/>
                        <input type="hidden" name="orgId" value=""/>

                        <div class="form-group m-form__group row" >
                            <label class="col-2 col-form-label" style="text-align:right;"><span style="color:red">*</span>窗口名称:</label>
                            <div class="col-10">
                                <input class="form-control m-input" type="text" value="" name="windowName" placeholder="请输入窗口名称...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>行政区划:</label>
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

                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>所属部门:</label>
                            <div class="col-lg-4 input-group">
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
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>排序号:</label>
                            <div class="col-4">
                                <input class="form-control m-input" type="number" value="" name="sortNo" placeholder="请输入排列顺序号...">
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>窗口类型:</label>
                            <div class="col-4">
                                <select type="text" class="form-control" name="windowType" value="">
                                    <option value="">请选择</option>
                                    <c:forEach items="${windowTypes}" var="windowType">
                                        <option value="${windowType.itemCode}">${windowType.itemName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否启用:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="isActive" value="1" checked>是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isActive" value="0">否<span></span>
                                    </label>
                                </div>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否所有单项通办:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="isAllItem" value="1" checked>是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isAllItem" value="0">否<span></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否公示:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="isPublic" value="1" checked>是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isPublic" value="0">否<span></span>
                                    </label>
                                </div>
                            </div>

                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>是否所有阶段通办:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="isAllStage" value="1" checked>是<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="isAllStage" value="0">否<span></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>办理范围:</label>
                            <div class="col-4">
                                <div class="m-radio-inline">
                                    <label class="m-radio">
                                        <input type="radio" name="regionRange" value="1" checked>属地办理<span></span>
                                    </label>
                                    <label class="m-radio">
                                        <input type="radio" name="regionRange" value="0">全市通办<span></span>
                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>联系电话:</label>
                            <div class="col-lg-10 input-group">
                                <input class="form-control m-input" type="text" value="" name="linkPhone" placeholder="请输入联系电话...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-lg-2 col-form-label" style="text-align: right;"><font color="red">*</font>办公时间:</label>
                            <div class="col-lg-10 input-group">
                                <input class="form-control m-input" type="text" value="" name="workTime" placeholder="请输入办公时间...">
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-2 col-form-label" style="text-align:right;"><span style="color:red">*</span>窗口地址:</label>
                            <div class="col-10">
                                <textarea name="windowAddress" class="form-control m-input" placeholder="请输入窗口地址..." rows="5"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row" >
                            <label class="col-2 col-form-label" style="text-align:right;"><span style="color:red">*</span>交通指引:</label>
                            <div class="col-10">
                                <textarea name="trafficGuide" class="form-control m-input" placeholder="请输入交通指引.." rows="5"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align:right;">地图链接:</label>
                            <div class="col-10">
                                <textarea name="mapUrl" class="form-control m-input" placeholder="请输入地图链接..." rows="5"></textarea>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-lg-2 col-form-label" style="text-align:right;">地图附件:</label>
                            <div class="col-8">
                                <input id="mapAttFile" type="file" class="form-control m-input"
                                       name="mapAttFile" multiple="multiple" onchange="uploadFileChange(this);"/>
                                <span class="custorm-style">
                                    <button class="left-button">选择文件</button>
                                    <span class="right-text">未选择任何文件</span>
                                </span>
                            </div>
                            <div id="mapAttFileDiv" class="col-2">
                                <button id="mapAttFileButton" type="button" class="btn btn-info"
                                        onclick="showServiceWindAtt('matAtt');">查看附件</button>
                            </div>
                        </div>

                        <div class="form-group m-form__group row">
                            <label class="col-2 col-form-label" style="text-align:right;">备注说明:</label>
                            <div class="col-10">
                                <textarea name="windowMemo" class="form-control m-input" placeholder="请输入窗口描述..." rows="5"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 按钮区域 -->
                <div class="modal-footer" style="padding: 10px;height: 60px;">
                    <button id="save_service_wind_btn" type="submit" class="btn btn-info">保存</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                </div>
            </form>
        </div>
    </div>
</div>