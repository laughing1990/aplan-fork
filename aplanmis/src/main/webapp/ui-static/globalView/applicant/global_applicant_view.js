var commonQueryParams = [], aedit_applicant_validator;
var APPLICANT_URL_PREFIX = ctx + '/aea/applicant/';
var elecFileTable = null;
var selectedFileArray = [];
var global_applicantId = "";
var globalUnitTypeList = [];
var globalIDTypeList = [];
$(function () {

	// 初始化高度
	$('#mainContentPanel').css('height', $(document.body).height() - 10);


	$('#show_applicant_att_tb').bootstrapTable('resetView', {
		height: 400
	});

	setTimeout(function () {
		$("#aedit_applicant_global_body").css({'overflow-y': 'scroll'})
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
function searchApplicantSto() {

	var params = $('#search_applicant_sto_form').serializeArray();
	commonQueryParams = [];
	if (params != "") {
		$.each(params, function () {
			commonQueryParams.push({name: this.name, value: this.value});
		});
	}
	$("#customTable").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
}

function refreshApplicantSto() {

	$('#search_applicant_sto_form')[0].reset();
	searchApplicantSto();
}

// 清空查询
function clearSearchApplicantSto() {

	$('#search_applicant_sto_form')[0].reset();
	searchApplicantSto();
}

// 列表查询参数处理
function applicantStoParam(params) {

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


function applicantStotResponseData(res) {

	return res;
}

function onloadUnitTypeList() {
	var sels = $('#aedit_applicant_modal #unitType');
	$(sels).empty();
	if (!globalUnitTypeList || globalUnitTypeList.length == 0) {
		var url = ctx + '/bsc/dic/code/lgetItems.do?typeCode=XM_DWLX';
		$.ajax({
			url: url,
			type: 'get',
			async: false,
			success: function (data) {
				if (data && data.length > 0) {
					globalUnitTypeList = data;
				}
			},
			error: function () {
				swal('错误信息', "加载单位类型错误！", 'error');
			}
		});
	}

	for (var i = 0; i < globalUnitTypeList.length; i++) {
		$(sels).append("<option value=" + globalUnitTypeList[i].value + ">" + globalUnitTypeList[i].label + "</option>");
	}

}


function onloadIDTypeList() {
	var idsels = $('#aedit_applicant_modal #idtype');
	$(idsels).empty();
	if (!globalIDTypeList || globalIDTypeList.length == 0) {
		var url = ctx + '/bsc/dic/code/lgetItems.do?typeCode=IDTYPE';
		$.ajax({
			url: url,
			type: 'get',
			async: false,
			success: function (data) {
				if (data && data.length > 0) {
					globalIDTypeList = data;
				}
			},
			error: function () {
				swal('错误信息', "加载证件类型错误！", 'error');
			}
		});
	}

	for (var i = 0; i < globalIDTypeList.length; i++) {
		$(idsels).append("<option value=" + globalIDTypeList[i].value + ">" + globalIDTypeList[i].label + "</option>");
	}

}

// 显示新增窗口
function addGlobalApplicant() {
	debugger;

	$('#aedit_applicant_modal_title').html('新增单位');
	$('#aedit_applicant_modal').modal('show');
	$('#aedit_applicant_global_body').animate({scrollTop: 0}, 800);//滚动到顶部

	$("#aedit_applicant_form")[0].reset();
	$("#aedit_applicant_form").resetForm();
	$("#aedit_applicant_form input[name='unitInfoId']").val('');
	//加载单位类型
	onloadUnitTypeList();
	//加载证件类型
	onloadIDTypeList();
	$('#aedit_applicant_modal #idtype').selectpicker('destroy');
	$('#aedit_applicant_modal #idtype').selectpicker('render');

	$('#aedit_applicant_modal #unitType').selectpicker('destroy');
	$('#aedit_applicant_modal #unitType').selectpicker('render');
	loadElectronicFileTable([]);
}

// 修改数据
function loadGlobalApplicantData(id) {
	selectedFileArray = [];
	global_applicantId = id;

	$('#aedit_applicant_modal_title').html('编辑单位');
	$('#aedit_applicant_modal').modal('show');
	$('#aedit_applicant_global_body').animate({scrollTop: 0}, 800);//滚动到顶部
	$("#aedit_applicant_form")[0].reset();
	$("#aedit_applicant_form").resetForm();
	$("#aedit_applicant_form input[name='unitInfoId']").val('');

	$.ajax({
		url: APPLICANT_URL_PREFIX + 'getApplicantById.do',
		type: 'post',
		data: {'id': id},
		async: false,
		success: function (data) {
			if (data) {
				//加载单位类型
				onloadUnitTypeList();
				//加载证件类型
				onloadIDTypeList();
				// 记载表单数据
				loadFormData(true, '#aedit_applicant_form', data);
				//加载文件
				loadElectronicFileTable(data.fileList);
			}
		},
		error: function () {
			swal('错误信息', "获取单位信息失败！", 'error');
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
				url: APPLICANT_URL_PREFIX + 'generatePassword.do?unitInfoId=' + id,
				dataType: 'json',
				contentType: false,
				processData: false,
				success: function (result) {
					if (result.success) {
						$('#aedit_applicant_modal').modal('hide');
						var url = ctx + "/aea/hi/receive/toPrintPageForPassword.do?type=applicant";
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

	var operatorStr = '<a href="javascript:generatePassWord(\'' + row.unitInfoId + '\',' + 0 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="生成密码"><i class="la la-unlock"></i></a>';
	operatorStr += '<a href="javascript:loadGlobalApplicantData(\'' + row.unitInfoId + '\',' + 0 + ')" class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill" title="编辑"><i class="la la-edit"></i></a>';
	operatorStr += '<a href="javascript:delGlobalApplicant(\'' + row.unitInfoId + '\')" class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" title="删除"><i class="la la-trash"></i></a> </span>';
	return operatorStr;
}

// 初始化表单检验
function initValidate() {

	aedit_applicant_validator = $("#aedit_applicant_form").validate({
		// 定义校验规则
		rules: {
			applicant: {
				required: true
			},
			idcard: {
				required: true,
				maxlength: 200
			},
			idtype: {
				required: true
			},
			isPrimaryUnit: {
				required: true
			},
			unitType: {
				required: true
			},
			unitNature: {
				required: true
			},
			managementScope: {
				required: true,
				maxlength: 200
			},
			registeredCapital: {
				required: true,
				maxlength: 200
			},
			registerAuthority: {
				required: true,
				maxlength: 200
			},
			postalCode: {
				required: true,
				maxlength: 200
			},
			applicantDetailSite: {
				required: true,
				maxlength: 200
			},
			idrepresentative: {
				required: true,
				maxlength: 200
			},
			idno: {
				required: true,
				maxlength: 200
			},
			contact: {
				required: true,
				maxlength: 200
			},
			idmobile: {
				required: true,
				maxlength: 200
			}
		},
		messages: {
			applicant: {
				required: '<font color="red">此项必填！</font>',
			},
			idcard: {
				required: '<font color="red">此项必填！</font>',
			},
			idtype: {
				required: '<font color="red">此项必填！</font>',
			},
			isPrimaryUnit: {
				required: '<font color="red">此项必填！</font>',
			},
			unitType: {
				required: '<font color="red">此项必填！</font>',
			},
			unitNature: {
				required: '<font color="red">此项必填！</font>',
			},
			managementScope: {
				required: '<font color="red">此项必填！</font>',
			},
			registeredCapital: {
				required: '<font color="red">此项必填！</font>',
			},
			applicantDetailSite: {
				required: '<font color="red">此项必填！</font>',
			},
			registerAuthority: {
				required: '<font color="red">此项必填！</font>',
			},
			postalCode: {
				required: '<font color="red">此项必填！</font>',
			},
			idrepresentative: {
				required: '<font color="red">此项必填！</font>',
			},
			idno: {
				required: '<font color="red">此项必填！</font>',
			},
			contact: {
				required: '<font color="red">此项必填！</font>',
			},
			idmobile: {
				required: '<font color="red">此项必填！</font>',
			}
		},
		// 提交表单
		submitHandler: function () {

			var formData = new FormData(document.getElementById("aedit_applicant_form"));
			formData.append('tableName', "AEA_UNIT_INFO");
			formData.append('pkName', "UNIT_INFO_ID");
			$.each(selectedFileArray, function (i, file) {
				formData.append('files', file.fileObj);
			});
			$.ajax({
				type: 'post',
				url: APPLICANT_URL_PREFIX + 'saveGlobalApplicant.do',
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
						$('#aedit_applicant_modal').modal('hide');
						if (result.message != "") { //返回值不为空表示为新增，自动生成单位密码
							$.ajax({
								type: 'get',
								url: APPLICANT_URL_PREFIX + 'generatePassword.do?unitInfoId=' + result.message,
								dataType: 'json',
								contentType: false,
								processData: false,
								success: function (result) {
									if (result.success) {
										$('#aedit_applicant_modal').modal('hide');
										var url = ctx + "/aea/hi/receive/toPrintPageForPassword.do?type=applicant";
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
						searchApplicantSto();
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


// 批量删除数据
function batchDelGlobalApplicant() {

	var rows = $("#customTable").bootstrapTable('getSelections');
	if (rows != null && rows.length > 0) {
		var ids = [];
		for (var i = 0; i < rows.length; i++) {
			ids.push(rows[i].unitInfoId);
		}
		swal({
			text: '此操作将批量删除当前库单位，您确定执行吗？',
			type: 'warning',
			showCancelButton: true,
			confirmButtonText: '确定',
			cancelButtonText: '取消'
		}).then(function (result) {
			if (result.value) {
				$.ajax({
					type: 'post',
					url: APPLICANT_URL_PREFIX + 'batchDeleteAeaApplicantByIds.do',
					dataType: 'json',
					data: {'ids': ids.toString()},
					success: function (result) {
						if (result.success) {
							swal('提示信息', result.message, 'success');
							// 列表数据重新加载
							searchApplicantSto();
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
function delGlobalApplicant(id) {

	swal({
		text: '此操作将删除当前库单位，您确定执行吗？',
		type: 'warning',
		showCancelButton: true,
		confirmButtonText: '确定',
		cancelButtonText: '取消'
	}).then(function (result) {
		if (result.value) {
			$.ajax({
				type: 'post',
				url: APPLICANT_URL_PREFIX + 'deleteGlobalApplicant.do',
				dataType: 'json',
				data: {'id': id},
				success: function (result) {
					if (result.success) {
						// swal('提示信息', result.message ,'success');
						searchApplicantSto();
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
				url: ctx + "/aea/applicant/deleteFile.do",
				type: "POST",
				dataType: "json",
				data: data_del,
				success: function (result) {
					if (result.success) {
						loadElectronicFiles(global_applicantId);
					}
				}
			})
		});
	}
}

function loadElectronicFiles(id) {
	$.ajax({
		url: ctx + "/aea/applicant/getApplicantById.do?id=" + id,
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



