var commonQueryParams = [];

$(function () {

	// 初始化高度
	$('#mainContentPanel').css('height', $(document.body).height() - 10);


	$('#show_linkman_att_tb').bootstrapTable('resetView', {
		height: 400
	});

	$("#aedit_linkman_global_body").niceScroll({

		cursorcolor: "#e2e5ec",//#CC0071 光标颜色
		cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
		cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
		touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
		cursorwidth: "4px", //像素光标的宽度
		cursorborder: "0", //   游标边框css定义
		cursorborderradius: "2px",//以像素为光标边界半径
		autohidemode: true  //是否隐藏滚动条
	});

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
	var matinstId = getUrlParam('matinstId');
	getFileDataCom(matinstId);
});

function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]);
	return null;
}

// 文件预览
function filePreview(id) {
	window.open(ctx + '/file/showFile.do?detailId=' + id);
}

//获取文件数据
function getFileDataCom(id) {
	$.ajax({
		url: ctx + "/bpm/getAttFiles.do?matinstId=" + id,
		dataType: "json",
		type: "GET",
		cache: false,
		async: false,
		success: function (result) {
			initFileCommonTable(result);
		}
	});
}

function initFileCommonTable(data) {
	$("#fileCommonTable").bootstrapTable('destroy');
	$('#fileCommonTable').bootstrapTable({
		columns: [
			{
				checkbox: true,
				align: "center"
			},
			{
				field: "fileId",
				title: "",
				visible: false
			},
			{
				field: "fileName",
				title: "文件名",
				halign: "center",
				align: "center",
			},
			{
				field: "fileId",
				title: "操作",
				width: 200,
				align: "center",
				formatter: function (value, row, index) {
					return "<button class=\"btn btn-primary\" onclick='filePreview(\"" + value + "\",\"" + row.fileType + "\")' >预览</button>";
				}
			}
		],
		sortOrder: "asc",
		width: '100%',
		cache: false,
		pageSize: 3,
		pageList: [3, 20, 50, 100],
		paginationHAlign: 'right',
		paginationDetailHAlign: "right",
		paginationVAlign: 'bottom',
		paginationShowPageGo: true,
		formatShowingRows: function (pageFrom, pageTo, totalRows) {
			return '共 ' + totalRows + ' 条';
		},
		sidePagination: "client",
		data: data,
	})
}

