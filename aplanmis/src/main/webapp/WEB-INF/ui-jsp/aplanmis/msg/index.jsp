<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html style="overflow-x: hidden;height: 100%;
    margin: 0;
    padding: 0;
    border: 0;
    overflow: auto;">
<head>
	<title>个人消息库</title>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/theme-libs/metronic-v5/template-default.jsp"%>

	<script src="${pageContext.request.contextPath}/ui-static/aplanmis/msg/index.js" type="text/javascript"></script>
	<style type="text/css">
		.btn-lg, .btn-group-lg > .btn {
			padding: 1rem 1.0rem;
			font-size: 1.25rem;
			line-height: 1.5;
			border-radius: 0.3rem;
		}
		.m-header-menu {
			display: table;
			float: left;
			width: auto;
			height: 100%;
			margin: 0px 0px 0px 0px;
		}
		.m-portlet .m-portlet__body {
			padding: 0px;
		}
		.modal-header {
			background-color: rgb(88, 103, 221);
		}
		.btn-brand {
			background-color: rgb(88, 103, 221);
			border-color: rgb(88, 103, 221);
		}
	</style>
</head>
<body>
   <div style="width: 100%;height: 100%; padding: 15px 10px 5px 10px;">
	   <div class="row" style="width: 100%;height: 100%;margin: 0px;">
		   <div class="col-xl-2" style="padding: 0px 2px 0px 8px;" id="westSpace">

				   <div class="m-portlet m-portlet--primary m-portlet--head-solid-bg">
					   <div class="m-portlet__head">
						   <div class="m-portlet__head-caption">
							   <div class="m-portlet__head-title" style="width: 100%;text-align: center;">
								   <h3 class="m-portlet__head-text">
									   我的消息
								   </h3>
							   </div>
						   </div>
					   </div>
					   <div class="m-portlet__body" style="" id="west_body">
						   <div class="row" style="margin-left: 20px;padding-top: 15px">
							   <button type="button" class="btn btn-outline-info btn-lg" onclick="boxShow('1')" id="inbox" style="width: 100px;height: 100px">
								   收件箱
							   </button>&nbsp;&nbsp;&nbsp;&nbsp;
							   <button type="button" class="btn btn-outline-info btn-lg" onclick="boxShow('2')" style="width: 100px;height: 100px;">
								   发件箱
							   </button>
						   </div>
						   <%--<div class="row" style="margin-top: 10px;margin-left: 20px;">--%>
							   <%--<button type="button" class="btn btn-outline-info btn-lg" onclick="boxShow('3')" style="width: 100px;height: 100px;">--%>
								   <%--未读消息--%>
							   <%--</button>--%>
						   <%--</div>--%>
					   </div>
				   </div>
		   </div>

		   <div class="col-xl-10" style="padding: 0px 8px 0px 2px;">
			   <div class="m-portlet m-portlet--primary m-portlet--head-solid-bg">
				   <div class="m-portlet__head">
					   <div class="m-portlet__head-caption">
						   <div class="m-portlet__head-title" style="width: 100%;">
							   <h3 class="m-portlet__head-text" id="center_title">
								   收件箱
							   </h3>
						   </div>
					   </div>
				   </div>
				   <div class="m-portlet__body" style="" id="center_body">
						<div class="row" style="margin-left: 20px;padding-top: 10px">
							<div class="col-6">
								<button type="button" class="btn m-btn--square  btn-info" onclick="newPersonMsg();">
									新建消息
								</button>&nbsp;&nbsp;
								<button type="button" class="btn m-btn--square  btn-info">
									未读消息
								</button>&nbsp;&nbsp;
								<button type="button" class="btn m-btn--square  btn-info">
									答复
								</button>&nbsp;&nbsp;
								<button type="button" class="btn m-btn--square  btn-info" onclick="deleteMsgList();">
									删除
								</button>&nbsp;&nbsp;
								<button type="button" class="btn m-btn--square  btn-info">
									<div id="m_header_menu" class="m-header-menu m-header-menu--submenu-skin-light"  >
										<ul class="m-menu__nav  m-menu__nav--submenu-arrow ">
											<li class="m-menu__item  m-menu__item--submenu m-menu__item--rel"  data-menu-submenu-toggle="click" data-redirect="true" aria-haspopup="true">
												<a  href="#" class="m-menu__link m-menu__toggle">
													<span class="m-menu__link-text" style="color: #FFFFFF">
														标记为
													</span>
													<i class="m-menu__hor-arrow la la-angle-down" style="color: #FFFFFF"></i>
												</a>
												<div class="m-menu__submenu m-menu__submenu--classic m-menu__submenu--left">
													<span class="m-menu__arrow m-menu__arrow--adjust"></span>
													<ul class="m-menu__subnav" style="width: 200px">
														<li class="m-menu__item "  aria-haspopup="true">
															<a  href="javascript:void(0);" class="m-menu__link ">
																<span class="m-menu__link-text">
																	未读消息
																</span>
															</a>
														</li>
														<li class="m-menu__item "  data-redirect="true" aria-haspopup="true">
															<a  href="javascript:void(0);" class="m-menu__link ">
																<span class="m-menu__link-text">
																	垃圾消息
																</span>
															</a>
														</li>
													</ul>
												</div>
											</li>
										</ul>
									</div>
								</button>
							</div>
							<div class="col-4">
								<form id="search_msg_list_form" method="post">
									<div class="m-input-icon m-input-icon--left">
										<input type="text" id="searchMsgKeyword" class="form-control m-input" placeholder="请输入主题查询" name="keyword" value=""/>
										<span class="m-input-icon__icon m-input-icon__icon--left">
										<span><i class="la la-search"></i></span>
									</span>
									</div>
								</form>
							</div>
							<div class="col-2">
								<button type="button" class="btn  btn-info" onclick="searchMsg();">查询</button>
								<button type="button" class="btn  btn-danger" onclick="clearSearch();">清空</button>
							</div>
						</div>
					   <div class="row" style="margin-left: 0px;padding-top: 10px">
						   <div id="index_msg_list_tb" class="m_datatable table-sm"></div>
					   </div>
				   </div>
			   </div>
		   </div>
	   </div>
   </div>

   <%--新建消息框--%>
   <div id="person_msg_modal" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="person_msg_modal_title" aria-hidden="true">
	   <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		   <div class="modal-content">
			   <div class="modal-header" style="padding: 15px;height: 45px;">
				   <h5 class="modal-title" id="person_msg_modal_title" style="color: #FFFFFF">
					   新建消息
				   </h5>
				   <%--<button type="button" class="close" data-dismiss="modal" aria-label="Close">--%>
					   <%--<span aria-hidden="true" style="color: #FFFFFF">--%>
						   <%--<i class="fa fa-window-close" style=""></i>--%>
					   <%--</span>--%>
				   <%--</button>--%>
			   </div>
			   <div class="modal-body" style="padding: 10px;padding-right: 10px">
				   <form id="person_msg_form" method="post">

					   <input type="hidden" id="rangeId" name="rangeId" value=""/>
					   <input type="hidden" id="sendUserId" name="sendUserId" value=""/>
					   <input type="hidden" id="isImportant" name="isImportant" value=""/>

					   <div class="form-group m-form__group row" >
						   <label class="col-lg-2 col-form-label" style="text-align: right;"><font style="color:red">*</font>收件人:</label>
						   <div class="col-lg-9">
							   <input type="text" class="form-control m-input" name="sendUserName" value=""/>
						   </div>
					   </div>
					   <div class="form-group m-form__group row" >
						   <label class="col-lg-2 col-form-label" style="text-align: right;"><font style="color:red">*</font>主题:</label>
						   <div class="col-lg-9">
							   <input type="text" class="form-control m-input" name="contentTitle" value=""/>
						   </div>
					   </div>
					   <div class="form-group m-form__group row" >
						   <label class="col-lg-2 col-form-label" style="text-align: right;"><font style="color:red">*</font>内容:</label>
						   <div class="col-lg-9">
							   <textarea class="form-control" name="contentText" rows="10"></textarea>
						   </div>
					   </div>
					   <div class="form-group m-form__group row" >
						   <div class="col-lg-2"></div>
						   <div class="col-lg-3">
							   <label class="m-checkbox m-checkbox--state-primary" style="margin-top: 10px">
								   <input type="checkbox" id="check0">
								   重要
								   <span></span>
							   </label>
						   </div>
						   <div class="col-lg-7">
							   <label class="m-checkbox m-checkbox--state-primary" style="margin-top: 10px">
								   <input type="checkbox" id="check1">
								   一般
								   <span></span>
							   </label>
						   </div>
					   </div>

					   <div class="form-group m-form__group row" style="text-align: right;">
						   <div class="col-lg-11">
							   <button type="submit" class="btn btn-info">发送</button>&nbsp;&nbsp;
							   <button type="button" class="btn btn-secondary" onclick="$('#person_msg_modal').modal('hide');">关闭</button>
						   </div>
					   </div>
				   </form>
			   </div>
		   </div>
	   </div>
   </div>

   <%--收件消息框--%>
   <div id="view_person_msg_modal" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="view_person_msg_modal_title" aria-hidden="true">
	   <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
		   <div class="modal-content">
			   <div class="modal-header" style="padding:0px;height: 45px;">
				   <h5 class="modal-title" id="view_person_msg_modal_title" style="color: #FFFFFF;padding: 13px;">
					   收件消息
				   </h5>
				   <a href="javaScript:void(0);" onclick="$('#view_person_msg_modal').modal('hide');" class="btn btn-brand m-btn m-btn--icon">
						<span>
							<i class="fa flaticon-cancel"></i>
						</span>
				   </a>
			   </div>
			   <div class="modal-body" style="padding: 10px;padding-right: 10px">
				   <form id="view_person_msg_form" method="post">

					   <div class="form-group m-form__group row" >
						   <label class="col-lg-2 col-form-label" style="text-align: right;">发件人:</label>
						   <div class="col-lg-9">
							   <input type="text" class="form-control m-input" name="sendUserName" value="" readonly/>
						   </div>
					   </div>
					   <div class="form-group m-form__group row" >
						   <label class="col-lg-2 col-form-label" style="text-align: right;">主题:</label>
						   <div class="col-lg-9">
							   <input type="text" class="form-control m-input" name="contentTitle" value="" readonly/>
						   </div>
					   </div>
					   <div class="form-group m-form__group row" >
						   <label class="col-lg-2 col-form-label" style="text-align: right;">内容:</label>
						   <div class="col-lg-9">
							   <textarea class="form-control" name="contentText" rows="10" readonly></textarea>
						   </div>
					   </div>
					   <div class="form-group m-form__group row" >
						   <div class="col-lg-2"></div>
						   <div class="col-lg-3">
							   <label class="m-checkbox m-checkbox--state-primary" style="margin-top: 10px">
								   <input type="checkbox" id="view_check0" onclick="return false" >
								   重要
								   <span></span>
							   </label>
						   </div>
						   <div class="col-lg-7">
							   <label class="m-checkbox m-checkbox--state-primary" style="margin-top: 10px">
								   <input type="checkbox" id="view_check1" onclick="return false" >
								   一般
								   <span></span>
							   </label>
						   </div>
					   </div>
				   </form>
			   </div>
		   </div>
	   </div>
   </div>

</body>
</html>