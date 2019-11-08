var commonQueryParams = [], aedit_linkman_validator;
var LINKMAN_URL_PREFIX = ctx + '/aea/linkman/';
var elecFileTable = null;
var selectedFileArray = [];
var global_linkmanIofoId = "";
$(function () {

	// 初始化高度
	$('#mainContentPanel').css('height', $(document.body).height() - 10);


	$('#show_linkman_att_tb').bootstrapTable('resetView', {
		height: 400
	});

	setTimeout(function () {
		$("#aedit_linkman_global_body").css({'overflow-y': 'scroll'})
	}, 1000)
	$(".fixed-table-body").niceScroll({
		cursorcolor: "#e2e5ec",//#CC0071 光标颜色
		cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
		cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
		touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
		cursorwidth: "4px", //像素光标的宽度
		cursorborder: "0", //   游标边框css定义
		cursorborderradius: "2px",//以像素为光标边界半径
		autohidemode: true  //是否隐藏滚动条
	});

	// 初始化表单校验
	initValidate();
});

// 查询事件
function searchLinkManSto() {

	var params = $('#search_linkman_sto_form').serializeArray();
	commonQueryParams = [];
	if (params != "") {
		$.each(params, function () {
			commonQueryParams.push({name: this.name, value: this.value});
		});
	}
	$("#customTable").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

function refreshLinkManSto() {

	$('#search_linkman_sto_form')[0].reset();
	searchLinkManSto();
}

// 清空查询
function clearSearchLinkManSto() {

	$('#search_linkman_sto_form')[0].reset();
	searchLinkManSto();
}

// 列表查询参数处理
function linkManStoParam(params) {

	var pageNum = (params.offset / params.limit) + 1;
	var pagination = {
		page: pageNum,
		pages: pageNum,
		perpage: params.limit
	};
	var sort = {
		field: params.sort,
		sort: params.order
	};
	var queryParam = {
		pagination: pagination,
		sort: sort
	};
	//组装查询参数
	var buildParam = {};
	if (commonQueryParams) {
		for (var i = 0; i < commonQueryParams.length; i++) {
			buildParam[commonQueryParams[i].name] = commonQueryParams[i].value;
		}
		queryParam = $.extend(queryParam, buildParam);
	}
	return queryParam;
}


function linkManStotResponseData(res) {

	return res;
}

// 修改数据
function loadGlobalLinkManData(id) {
	selectedFileArray = [];
	global_linkmanIofoId = id;

	$('#aedit_linkman_modal_title').html('编辑联系人');
	$('#aedit_linkman_modal').modal('show');
	$('#aedit_linkman_global_body').animate({scrollTop: 0}, 800);//滚动到顶部
	$("#aedit_linkman_form")[0].reset();
	$("#aedit_linkman_form").resetForm();
	$("#aedit_linkman_form input[name='linkmanInfoId']").val('');

	$.ajax({
		url: LINKMAN_URL_PREFIX + 'getAeaLinkManById.do',
		type: 'post',
		data: {'id': id},
		async: false,
		success: function (data) {
			if (data) {
				// 记载表单数据
				loadFormData(true, '#aedit_linkman_form', data);
				if (data.linkmanType == "u") {
					$('#applicant_div').addClass("show").removeClass("hidden");
					$('#applicant_div1').addClass("show").removeClass("hidden");
					$("#applicant_ul").removeClass("show").addClass("hidden");
				} else {
					$('#applicant_div').addClass("hidden").removeClass("show");
					$('#applicant_div1').addClass("hidden").removeClass("show");
				}
				loadElectronicFileTable(data.fileList);
			}
		},
		error: function () {
			swal('错误信息', "获取联系人信息失败！", 'error');
		}
	});
}

//生成密码
function generatePassWord(id) {
	swal({
		text: '此操作将会重置密码，您确定执行吗？',
		type: 'warning',
		showCancelButton: true,
		confirmButtonText: '确定',
		cancelButtonText: '取消'
	}).then(function (result) {
		if (result.value) {
			$.ajax({
				type: 'get',
				url: LINKMAN_URL_PREFIX + 'generatePassword.do?linkmanInfoId=' + id,
				dataType: 'json',
				contentType: false,
				processData: false,
				success: function (result) {
					if (result.success) {
						$('#aedit_linkman_modal').modal('hide');
						var url = ctx + "/aea/hi/receive/toPrintPageForPassword.do?type=linkman";
						window.open(ctx + '/file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url));
					} else {
						swal('错误信息', '生成密码失败!', 'error');
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					swal('错误信息', XMLHttpRequest.responseText, 'error');
				}
			});
		}
	});

}

// 格式化操作栏
function operatorFormatter(value, row, index, field) {
	var operatorStr = '<a href="javascript:generatePassWord(\'' + row.linkmanInfoId + '\',' + 0 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="生成密码"><i class="la la-unlock"></i></a>';
	operatorStr += '<a href="javascript:loadGlobalLinkManData(\'' + row.linkmanInfoId + '\',' + 0 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
	operatorStr += '<a href="javascript:delGlobalLinkMan(\'' + row.linkmanInfoId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
	return operatorStr;
}

// 初始化表单检验
function initValidate() {

	aedit_linkman_validator = $("#aedit_linkman_form").validate({
		// 定义校验规则
		rules: {
			linkmanName: {
				required: true,
				maxlength: 200
			},
			linkmanType: {
				required: true
			},
			linkmanCertNo: {
				required: true,
				maxlength: 200
			},
			linkmanMobilePhone: {
				required: true,
				maxlength: 200
			},
			linkmanAddr: {
				required: true,
				maxlength: 200
			}
		},
		messages: {
			linkmanName: {
				required: '<font color="red">此项必填！</font>',
			},
			linkmanType: {
				required: '<font color="red">此项必填！</font>',
			},
			linkmanCertNo: {
				required: '<font color="red">此项必填！</font>',
			},
			linkmanMobilePhone: {
				required: '<font color="red">此项必填！</font>',
			},
			linkmanAddr: {
				required: '<font color="red">此项必填！</font>',
			}
		},
		// 提交表单
		submitHandler: function () {

			var formData = new FormData(document.getElementById("aedit_linkman_form"));
			formData.append('tableName', "AEA_LINKMAN_INFO");
			formData.append('pkName', "LINKMAN_INFO_ID");
			$.each(selectedFileArray, function (i, file) {
				formData.append('files', file.fileObj);
			});
			$.ajax({
				type: 'post',
				url: LINKMAN_URL_PREFIX + 'saveAeaLinkMan.do',
				dataType: 'json',
				data: formData,
				contentType: false,
				processData: false,
				success: function (result) {
					if (result.success) {
						swal({
							type: 'success',
							title: '保存成功！',
							showConfirmButton: false,
							timer: 1000
						});
						$('#aedit_linkman_modal').modal('hide');
						if (result.message != "") { //返回值不为空表示为新增，自动生成联系人密码
							$.ajax({
								type: 'get',
								url: LINKMAN_URL_PREFIX + 'generatePassword.do?linkmanInfoId=' + result.message,
								dataType: 'json',
								contentType: false,
								processData: false,
								success: function (result) {
									if (result.success) {
										$('#aedit_linkman_modal').modal('hide');
										var url = ctx + "/aea/hi/receive/toPrintPageForPassword.do?type=linkman";
										window.open(ctx + '/file/ntkoOpenWin.do?jumpUrl=' + encodeURIComponent(url));
									} else {
										swal('错误信息', '生成密码失败!', 'error');
									}
								},
								error: function (XMLHttpRequest, textStatus, errorThrown) {
									swal('错误信息', XMLHttpRequest.responseText, 'error');
								}
							});
						}
						// 列表数据重新加载
						searchLinkManSto();
					} else {
						swal('错误信息', '保存失败,' + result.message, 'error');
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					swal('错误信息', XMLHttpRequest.responseText, 'error');
				}
			});
		}
	});
}

// 显示新增窗口
function addGlobalLinkMan() {
	loadElectronicFileTable([]);
	$('#aedit_linkman_modal_title').html('新增单位');
	$('#aedit_linkman_modal').modal('show');
	$('#aedit_linkman_global_body').animate({scrollTop: 0}, 800);//滚动到顶部

	$("#aedit_linkman_form")[0].reset();
	$("#aedit_linkman_form").resetForm();
	$("#aedit_linkman_form input[name='linkmanInfoId']").val('');
}

// 批量删除数据
function batchDelGlobalLinkMan() {

	var rows = $("#customTable").bootstrapTable('getSelections');
	if (rows != null && rows.length > 0) {
		var ids = [];
		for (var i = 0; i < rows.length; i++) {
			ids.push(rows[i].linkmanInfoId);
		}
		swal({
			text: '此操作将批量删除当前库联系人，您确定执行吗？',
			type: 'warning',
			showCancelButton: true,
			confirmButtonText: '确定',
			cancelButtonText: '取消'
		}).then(function (result) {
			if (result.value) {
				$.ajax({
					type: 'post',
					url: LINKMAN_URL_PREFIX + 'batchDeleteAeaLinkMan.do',
					dataType: 'json',
					data: {'ids': ids.toString()},
					success: function (result) {
						if (result.success) {
							// swal('提示信息', result.message ,'success');
							// 列表数据重新加载
							searchLinkManSto();
						} else {
							swal('错误信息', result.message, 'error');
						}
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
						swal('错误信息', XMLHttpRequest.responseText, 'error');
					}
				});
			}
		});
	} else {
		swal('提示信息', '请选择操作记录！', 'info');
	}
}

// 删除单条数据
function delGlobalLinkMan(id) {

	swal({
		text: '此操作将删除当前库联系人，您确定执行吗？',
		type: 'warning',
		showCancelButton: true,
		confirmButtonText: '确定',
		cancelButtonText: '取消'
	}).then(function (result) {
		if (result.value) {
			$.ajax({
				type: 'post',
				url: LINKMAN_URL_PREFIX + 'deleteAeaLinkManById.do',
				dataType: 'json',
				data: {'id': id},
				success: function (result) {
					if (result.success) {
						// swal('提示信息', result.message ,'success');
						searchLinkManSto();
					} else {
						swal('错误信息', result.message, 'error');
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					swal('错误信息', XMLHttpRequest.responseText, 'error');
				}
			});
		}
	});
}

/**
 * 选择单位
 */
function selApplicant(obj) {
	var selValue = $(obj).find("option:selected").val();
	if (selValue == "u") {
		$('#applicant_div').addClass("show").removeClass("hidden");
		$('#applicant_div1').addClass("show").removeClass("hidden");
	} else {
		$('#applicant_div').addClass("hidden").removeClass("show");
		$('#applicant_div1').addClass("hidden").removeClass("show");
	}
	$('#applicant_ul').removeClass("show").addClass("hidden");
}


function queryApplicant(obj) {
	var keyword = $("#applicant_div").find('input[name="applicant"]').val();
	commonQueryApplicant(keyword);
}

function commonQueryApplicant(keyword) {
	var applicant_ul = $('#applicant_ul');
	$(applicant_ul).empty();
	if (keyword) {
		$(applicant_ul).removeClass("hidden").addClass("show");
		$.ajax({
			url: ctx + '/aea/applicant/listApplicantsNoPage.do',
			type: 'get',
			data: {
				"keyword": keyword
			},
			async: false,
			success: function (data) {
				if (data != null && data.length > 0) {
					$(applicant_ul).removeClass("hidden").addClass("show");
					for (var i = 0; i < data.length; i++) {
						var row = data[i];
						var li = '<li onclick="selSingleApplicant(this)" id="' + row.unitInfoId + '">' + row.applicant + '</li>';
						$(applicant_ul).append(li);
					}
				} else {
					swal({
						type: 'success',
						title: '没有相关单位信息！',
						showConfirmButton: false,
						timer: 1000
					});
					$(applicant_ul).removeClass("show").addClass("hidden");
				}
			}
		});
	} else {
		$(applicant_ul).removeClass("show").addClass("hidden");
	}
}

function selSingleApplicant(obj) {
	var applicant = $(obj).text();
	var unitInfoId = $(obj).attr("id"); //$(obj).prop("id");
	$('#applicant_div').find("input[name='applicant']").val(applicant);
	$('#applicant_div').find("input[name='unitInfoId']").val(unitInfoId);
	$("#applicant_ul").removeClass("show").addClass("hidden");

}

//选择文件上传
$("#uploadFile_").click(function (e) {
	e.preventDefault();
	$('#realUploadDo_').click();
});

$('#realUploadDo_').on('change', function (e) {
	var fileData = [];
	$.each(this.files, function (i, file) {
		var tempFileId = guid();
		if (fileTyleLimit(file.name) && file.size <= 10 * 1024 * 1024) {
			selectedFileArray.push({tempFileId: tempFileId, fileObj: file});
			fileData.push({fileId: '', fileName: file.name, fileType: file.type, tempFileId: tempFileId, file: file});
		} else {
			swal('文件有问题', "文件不符合要求", 'error');
		}
	});
	if (elecFileTable) {
		elecFileTable.bootstrapTable('append', fileData);
	} else {
		loadElectronicFileTable(fileData);
	}
});

//为临时文件生成一个随机唯一字符串，用于删除
function guid() {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
		var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
		return v.toString(16);
	});
}

function loadElectronicFileTable(fileData) {
	if (elecFileTable) {
		elecFileTable.bootstrapTable('destroy');
	}
	elecFileTable = $('#fileTable_').bootstrapTable({
		columns: [{
			field: "fileId",
			title: "",
			width: 10,
			align: 'center',
			formatter: function (value, row, index) {
				return "<div class=\"checkbox\"><label><input name='checkIds' type=\"checkbox\" value='" + row.fileId + "'></label></div>";
			}
		}, {
			field: "num",
			title: "序号",
			width: 20,
			align: 'center',
			formatter: function (value, row, index) {
				return index + 1;
			}
		}, {
			field: "fileName",
			title: "文件名",
			width: 280,
			align: 'center',
			formatter: function (value, row, index) {
				console.log(row)
				if (row.fileId) {//已上传文件
					return "<a href='javascript:void(0)'  onclick='filePreview(\"" + row.fileId + "\",\"" + row.fileType + "\")'><span>" + value + "</span></a>";
				} else {//本地文件
					return "<a href='javascript:void(0)'  onclick='showLocalFile(\"" + row.tempFileId + "\")'><span>" + value + "</span></a>";
				}
			}
		}, {
			field: "tempFileId",
			visible: false,
			formatter: function (value, row, index) {
				return !row.tempFileId ? '' : row.tempFileId;
			}
		}, {
			field: "operator",
			width: 40,
			title: "操作",
			align: 'center',
			formatter: function (value, row, index) {
				if (row.fileId) {
					return '<i class="flaticon-download"  onclick="downFile(\'' + row.fileId + '\', \'' + row.fileType + '\')"></i><i onclick="deleteFile(\'' + row.fileId + '\', \'' + row.tempFileId + '\')" class="la la-trash-o"></i>';
				} else {
					return '<i onclick="deleteFile(\'' + row.fileId + '\', \'' + row.tempFileId + '\')" class="la la-trash-o"></i>';
				}
			}
		}],
		sortOrder: "asc",
		pagination: true,
		width: '100%',
		pageSize: 10,
		cache: false,
		pageList: [10, 20, 50, 100],
		paginationHAlign: 'right',
		paginationDetailHAlign: "right",
		paginationVAlign: 'bottom',
		formatShowingRows: function (pageFrom, pageTo, totalRows) {
			return '共 ' + totalRows + ' 条';
		},
		sidePagination: "client",
		data: fileData,
		queryParams: self.dealQueryParams
	});
}

function deleteFile(fileId, tempFileId) {
	if (!fileId) {//新选择的
		elecFileTable.bootstrapTable('remove', {field: 'tempFileId', values: [tempFileId]});
		var tempArr = [];
		for (var i = 0; i < selectedFileArray.length; i++) {
			if (selectedFileArray[i].tempFileId != tempFileId) {
				tempArr.push(selectedFileArray[i]);
			}
		}
		selectedFileArray = tempArr;
	} else {//之前已上传的
		agcloud.ui.metronic.showConfirm("是否要删除该文件？", function () {
			var data_del = {};
			data_del.detailIds = fileId;
			$.ajax({
				url: ctx + "/aea/linkman/deleteFile.do",
				type: "POST",
				dataType: "json",
				data: data_del,
				success: function (result) {
					if (result.success) {
						loadElectronicFiles(global_linkmanIofoId);
					}
				}
			})
		});
	}
}

function loadElectronicFiles(id) {
	$.ajax({
		url: ctx + "/aea/linkman/getAeaLinkManById.do?id=" + id,
		dataType: "json",
		type: "POST",
		success: function (result) {
			loadElectronicFileTable(result.fileList);
		}
	})
}

function showLocalFile(tempFileId) {
	var params = new FormData(document.getElementById("previewDoc"));
	var fileTemp
	$.each(selectedFileArray, function (i, file) {
		if (file.tempFileId == tempFileId) {
			params.append('files', file.fileObj);
			params.append('fileTemp', file.fileObj);
			fileTemp = file.fileObj
			return;
		}
	})
	console.log(fileTemp.type)
	// 获取 window 的 URL 工具
	var URL = window.URL || window.webkitURL;
	// 通过 file 生成目标 url
	var imgURL = URL.createObjectURL(fileTemp);
	//window.open(imgURL)
	var url = ctx + '/file/showLocalFile.do';
	//$.post(url,params);
	$.ajax({
		url: url,
		type: "POST",
		dataType: "json",
		data: params,
		processData: false,
		contentType: false,
		success: function (result) {
			//debugger;
		}
	})
	/*var form =$("#previewDoc");
	form.attr({"action":url});
	$("#previewDoc input[name='files']").val(fileTemp);*/
	//form.submit();
	/*var body = $(document.body),
			form = $("<form method='post' enctype='multipart/form-data'></form>"),
			input;
	form.attr({"action":url});
	form.attr({"target":"_blank"});
	input = $("<input type='hidden'>");
	input.attr({"name":"fileTemp"});
	debugger;
	input.val(fileTemp);
	form.append(input);
	form.appendTo(document.body);
	form.submit();
	document.body.removeChild(form[0]);*/

}

function fileTyleLimit(fileName) {
	var filesuffix = fileName.slice(fileName.lastIndexOf(".") + 1).toLowerCase();
	if (filesuffix == 'exe' || filesuffix == 'sh' || filesuffix == 'bat' || filesuffix == 'com' || filesuffix == 'dll') {
		return false;
	} else {
		return true;
	}
}

//下载文件
function downFile(fileId, fileType) {
	window.open(ctx + '/bpm/downloadAttachment.do?detailIds=' + fileId)

}

//文件预览
function filePreview(fileId, fileType) {
	window.open(ctx + '/file/showFile.do?detailId=' + fileId);
}

