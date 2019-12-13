var commonQueryParams = [], aedit_item_validator, customTable_tree;
var treegridCurrentPageNumber = 1;  //记录当前页面
var treegridCurrentPageSize = 10;   //记录页面大小
var modalLevel = 1;//设置Bootstrap，多个modal展示的前后z-index
$(function () {

	// 初始化高度
	$('#mainContentPanel').css('height', $(document.body).height() - 10);

	// 组织机构事项列表
	$('#all_item_list_tb').bootstrapTable('resetView', {
		height: $('#westPanel').height() - 116
	});

	initItemBasicTreeGrid();

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

	$('#searchItemType').change(function () {

		searchAllItemList();
	});

	// 选择行政区划
	$('#selectRegionBtn').click(function () {

		var isSelectRegionSearch = $('#isSelectRegionSearch').val();
		var bscDicRegionTree = $.fn.zTree.getZTreeObj("bscDicRegionTree");
		var nodes = bscDicRegionTree.getSelectedNodes();
		if (nodes != null && nodes.length > 0) {
			var selectNode = nodes[0];
			if (isSelectRegionSearch == 'search') {
				$("#aedit_item_form input[name='regionId']").val(selectNode.id);
				$("#aedit_item_form input[name='regionName']").val(selectNode.name);
			} else {
				$("#aedit_item_form input[name='regionId']").val(selectNode.id);
				$("#aedit_item_form input[name='regionName']").val(selectNode.name);
			}
			closeSelectBscDicRegionZtree();
		} else {
			swal('提示信息', '请选择行政区划！', 'info');
		}
	});

	//隐藏中介事项新增实施事项按钮
	// if (itemNature == '8') {
	// 	$("#zjsssx").hide()
	// } else {
	// 	$("#zjsssx").show()
	// }

});

function initItemBasicTreeGrid() {

	var url = ctx + '/aea/item/basic/listLatestAeaItemBasicTreeByPage.do?itemNature=' + itemNature;
	customTable_tree = $('#all_item_list_tb').bootstrapTable({
		url: url,
		columns: getItemBasicTreeGridColumns(),
		pagination: true,
		pageSize: 10,
		paginationHAlign: 'right',
		paginationVAlign: 'bottom',
		paginationDetailHAlign: "left",
		paginationShowPageGo: true,
		onPageChange: function (number, size) {
			treegridCurrentPageNumber = number;
			treegridCurrentPageSize = size;
		},
		pageList: [10, 20, 50, 100],
		method: 'post',
		contentType: "application/x-www-form-urlencoded",
		queryParams: allItemListParam,
		sidePagination: 'server',
		singleSelect: true,
		treeShowField: 'itemName',
		parentIdField: 'pid',
		idField: 'itemId',
		onPreBody: function (data) {
			if (data.length > 0) {
				var ids = [];
				$(data).each(function (i, item) {
					ids.push(item.itemId);
					item.pid = item.parentItemId;   //pid属性用于树表格显示
				});
				$(data).each(function (i, item) {
					if (item.pid != null && $.inArray(item.pid, ids) == -1) { //父节点不在返回数据中的，设置pid为null
						item.pid = null;
					}
				});
			}
		},
		onLoadSuccess: function (result) {

			$('#all_item_list_tb').treegrid({
				initialState: 'expanded',// 所有节点都折叠
				treeColumn: 0,
				onChange: function () {
					$('#all_item_list_tb').bootstrapTable('resetWidth');
				}
			});
		}
	});
}

//定义树表格的显示列
var getItemBasicTreeGridColumns = function () {

	var columns = [
		// {
		//     field: 'Number',
		//     title: '序号',
		//     align: 'center',
		//     width: 10,
		//     formatter: function (value, row, index) {
		//         if(row.pid != null)return '';
		//         return treegridCurrentPageSize * (treegridCurrentPageNumber - 1) + index;//返回每条的序号： 每页条数 * （当前页 - 1 ）+ 序号
		//     }
		// },
		// {
		//     checkbox:true
		// },
		{
			field: 'itemName',
			title: '标准/实施事项名称',
			align: 'left',
			width: 300
		},
		{
			field: 'itemCode',
			title: '标准/实施编码',
			width: 130,
			align: 'left'
		},
		{
			field: 'orgName',
			title: '主管/实施部门名称',
			width: 130,
			align: 'left',
			formatter: function (value, row) {

				if (row.isCatalog == '1') {
					return row.guideOrgName;
				} else {
					return value;
				}
			}
		},
		{
			field: 'isCatalog',
			align: 'center',
			width: 60,
			title: '是否实施事项',
			formatter: function (value, row) {
				return value == '1' ? '标准事项' : '实施事项';
			}
		},
		{
			field: 'itemType',
			align: 'center',
			width: 60,
			title: '事项类型',
			formatter: itemTypeFormatter
		},
		// {
		//     field: 'sxmlzt',
		//     align: 'center',
		//     width: 40,
		//     title: '事项状态',
		//     formatter: itemStatusFormatter
		// },
		{
			field: 'verNumVo',
			align: 'center',
			width: 60,
			title: '最新版本'
		},
		{
			field: 'operate_',
			align: 'center',
			title: '操作',
			width: 150,
			formatter: itemOperatorFormatter
		}
	];
	return columns;
}

function addItemBasicChildById(itemId, itemName, isCatalog, itemBasicId) {

	if (isCatalog == '0') {
		swal({
			text: '请确认是否为事项【' + itemName + '】添加子实施事项？',
			type: 'warning',
			showCancelButton: true,
			confirmButtonText: '确定',
			cancelButtonText: '取消',
		}).then(function (result) {
			if (result.value) {
				addItemInfo(false, false);
				$("#aedit_item_form input[name='itemCategoryMark']").attr("readOnly", "true");
				$("#aedit_item_form input[name='parentItemId']").val(itemId);
				$.ajax({
					url: ctx + '/aea/item/basic/getAeaItemBasic.do',
					type: 'post',
					data: {'id': itemBasicId},
					async: false,
					success: function (data) {
						if (data) {
							$("#aedit_item_form input[name='basecode']").val(data.itemCode);
							$("#aedit_item_form input[name='itemCategoryMark']").val(data.itemCategoryMark);
						}
					}
				});
			}
		});
	} else {
		swal({
			text: '请选择子事项类型？',
			type: 'question',
			input: 'radio',
			showCancelButton: true,
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			inputOptions: {
				"0": "实施事项",
				"1": "标准事项"
			},
			inputValidator: function (value) {
				if (!value) {
					return '请选择事项的类型!'
				}
			}
		}).then(function (result) {
			if (result.dismiss == 'cancel') {
				return;
			}
			addItemInfo(false, (result.value != '0'));
			$.ajax({
				url: ctx + '/aea/item/basic/getAeaItemBasic.do',
				type: 'post',
				data: {'id': itemBasicId},
				async: false,
				success: function (data) {
					if (data) {
						loadFormData(true, '#aedit_item_form', data);
						$("#aedit_item_form input[name='itemId']").val('');
						$("#aedit_item_form input[name='itemVerId']").val('');
						$("#aedit_item_form input[name='itemBasicId']").val('');
						$("#aedit_item_form input[name='parentItemId']").val(itemId);
						$("#aedit_item_form input[name='basecode']").val(data.itemCode);
						$("#aedit_item_form input[name='orgId']").val('');
						$("#aedit_item_form input[name='isCatalog']").val((result.value != '0') ? '1' : '0');
					}
				}
			});

		});
	}
}

// 事项操作指南
function viewItemOperaGuide(itemId, itemCode) {

	window.open('https://www.gdzwfw.gov.cn/portal/guide/' + itemCode, '查看操作指南');
}

$(document).on('hidden.bs.modal', '.modal', function (e) {
	$(this).css("z-index", 1050);
	modalLevel--;
});
$(document).on('show.bs.modal', '.modal', function (e) {
	$(this).css("z-index", 1050 + modalLevel);
	modalLevel++;
});

function itemOperatorFormatter(value, row, index) {

	var itemTitle = "编辑";
	var icoCSS = "la la-edit";
	if (row.itemVerStatus == "1" || row.itemVerStatus == "3") {
		itemTitle = "查看";
		icoCSS = "la la-search";
	}

	var editBtn = '<a href="javascript:editItemBasicById(\'' + row.itemBasicId + '\',\'' + row.itemVerStatus + '\',\'' + row.isCatalog + '\')" ' +
					 'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
					 'title="' + itemTitle + '"><i class="' + icoCSS + '"></i>' +
				 '</a>';

	// var addChildBtn='';
	// if(row.itemNature!='8'){
		var addChildBtn = '<a href="javascript:addItemBasicChildById(\'' + row.itemId + '\',\'' + row.itemName + '\',\'' + row.isCatalog + '\',\'' + row.itemBasicId + '\',)" ' +
								'class="m-portlet__nav-link btn m-btn m-btn--hover-info m-btn--icon m-btn--icon-only m-btn--pill"' +
								'title="添加子事项"><i class="la la-plus"></i>' +
			              '</a>';
	// }
	var deleteBtn = '<a href="javascript:deleteItemById(\'' + row.itemId + '\')" ' +
						'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
						'title="删除"><i class="la la-trash"></i>' +
					'</a>';

	var setState = '<a href="javascript:openItemVerModal(\'' + row.itemId + '\',\'' + row.itemVerId + '\',\'' + row.itemNature + '\')" ' +
						'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
						'title="事项版本"><i class="la la-cog"></i>' +
					'</a>';

	// var editMaterial='';
    //
	// if(row.itemNature=='8'){
	// 	editMaterial='<a href="javascript:openItemVerMaterialModal(\'' + row.itemVerId + '\')" ' +
	// 		'class="m-portlet__nav-link btn m-btn m-btn--hover-danger m-btn--icon m-btn--icon-only m-btn--pill" ' +
	// 		'title="输出材料"><i class="la la-cogs"></i>' +
	// 		'</a>';
	// }


	return editBtn + addChildBtn + setState + deleteBtn/*+editMaterial*/;
}

function allItemListParam(params) {

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
			buildParam[commonQueryParams[i].name] = commonQueryParams[i].value.trim();
		}
		queryParam = $.extend(queryParam, buildParam);
	}
	return queryParam;
}

// 查询
function searchAllItemList() {

	var params = $('#search_all_item_list_form').serializeArray();
	commonQueryParams = [];
	// commonQueryParams.push({name: 'itemNature', value: itemNature});
	if (params != "") {
		$.each(params, function () {
			commonQueryParams.push({name: this.name, value: this.value});
		});
	}
	$("#all_item_list_tb").bootstrapTable('selectPage', 1);  //跳转到第一页，防止其他页查询第一次不显示数据的问题。
	$("#all_item_list_tb").bootstrapTable('refresh');       //无参数刷新
}

// 清空
function clearSearchAllItemList() {

	$('#search_all_item_list_form')[0].reset();
	$("#search_all_item_list_form input[name='orgId']").val('');
	searchAllItemList();
}

// 刷新
function refreshAllItemList() {

	searchAllItemList();
}

// 修复数据
function initItemVerSeq() {

	swal({
		text: '此操作将【启用事项最新版本为试运行版本、修复版本与序号关系、更新所有阶段关联事项为最新版本事项数据】,确定执行吗？',
		type: 'warning',
		showCancelButton: true,
		confirmButtonText: '确定',
		cancelButtonText: '取消',
	}).then(function (result) {
		if (result.value) {

			$("#uploadProgress").modal("show");
			$('#uploadProgressMsg').html("数据修复中,请勿点击,耐心等候...");
			$.ajax({
				url: ctx + '/aea/item/initItemVerSeq.do',
				type: 'post',
				data: {'itemNature': itemNature},
				// async: false,
				success: function (result) {
					if (result.success) {
						setTimeout(function () {
							$("#uploadProgress").modal('hide');
							swal('提示信息', "修复事项序列成功！", 'info');
							refreshAllItemList();
						}, 500);
					} else {
						setTimeout(function () {
							$("#uploadProgress").modal('hide');
							swal('错误信息', "修复事项序列失败！", 'error');
						}, 500);
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					setTimeout(function () {
						$("#uploadProgress").modal('hide');
						swal('错误信息', XMLHttpRequest.responseText, 'error');
					}, 500);
				}
			});
		}
	});
}

//同步本地指南
function syncLocalAeaItemGuide() {

	swal({
		text: '确定将事项库的基本信息同步过去办事指南表吗？',
		type: 'warning',
		showCancelButton: true,
		confirmButtonText: '确定',
		cancelButtonText: '取消'
	}).then(function (result) {
		if (result.value) {
			$("#uploadProgress").modal("show");
			$('#uploadProgressMsg').html("数据同步中,请勿点击,耐心等候...");
			$.ajax({
				url: ctx + '/aea/item/guide/syncLocalAeaItemGuide.do',
				type: 'post',
				data: {},
				// async: false,
				success: function (result) {
					if (result.success) {
						setTimeout(function () {
							$("#uploadProgress").modal('hide');
							swal('提示信息', "同步本地指南成功！", 'info');
						}, 500);
					} else {
						setTimeout(function () {
							$("#uploadProgress").modal('hide');
							swal('错误信息', "同步本地指南失败！", 'error');
						}, 500);
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
					setTimeout(function () {
						$("#uploadProgress").modal('hide');
						swal('错误信息', XMLHttpRequest.responseText, 'error');
					}, 500);
				}
			});
		}
	});
}

// 初始标准事项
function initStandItem() {

	swal({
		text: '此操作将为未创建标准事项的实施事项创建标准事项，您确定执行吗？',
		type: 'warning',
		showCancelButton: true,
		confirmButtonText: '确定',
		cancelButtonText: '取消'
	}).then(function (result) {
		if (result.value) {

			$("#uploadProgress").modal("show");
			$('#uploadProgressMsg').html("数据创建中,请勿点击,耐心等候...");

			$.ajax({
				url: ctx + '/aea/item/initStandItem.do',
				type: 'post',
				data: {},
				// async: false,
				success: function (result) {
					if (result.success) {

						setTimeout(function () {
							$("#uploadProgress").modal('hide');
							swal('提示信息', "创建标准事项成功！", 'info');
							refreshAllItemList();
						}, 500);

					} else {

						setTimeout(function () {
							$("#uploadProgress").modal('hide');
							swal('错误信息', "创建标准事项失败！", 'error');
						}, 500);
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {

					setTimeout(function () {
						$("#uploadProgress").modal('hide');
						swal('错误信息', XMLHttpRequest.responseText, 'error');
					}, 500);
				}
			});
		}
	});
}

function syncItemRegion() {

	swal({
		text: '此操作将同步实施事项所属部门关联行政区域，您确定执行吗？',
		type: 'warning',
		showCancelButton: true,
		confirmButtonText: '确定',
		cancelButtonText: '取消'
	}).then(function (result) {
		if (result.value) {

			$("#uploadProgress").modal("show");
			$('#uploadProgressMsg').html("数据创建中,请勿点击,耐心等候...");

			$.ajax({
				url: ctx + '/aea/item/syncItemRegion.do',
				type: 'post',
				data: {'isCatalog': '0'},
				success: function (result) {
					if (result.success) {

						setTimeout(function () {
							$("#uploadProgress").modal('hide');
							swal('提示信息', "同步事项行政区域成功！", 'info');
							refreshAllItemList();
						}, 500);

					} else {

						setTimeout(function () {
							$("#uploadProgress").modal('hide');
							swal('错误信息', "同步事项行政区域失败！", 'error');
						}, 500);
					}
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {

					setTimeout(function () {
						$("#uploadProgress").modal('hide');
						swal('错误信息', XMLHttpRequest.responseText, 'error');
					}, 500);
				}
			});
		}
	});
}

function createUnionItemCode() {

    swal({
        text: '此操作将为每个事项编号追加时间戳构建事项唯一编号，您确定执行吗？',
        type: 'warning',
        showCancelButton: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消'
    }).then(function (result) {
        if (result.value) {

            $("#uploadProgress").modal("show");
            $('#uploadProgressMsg').html("数据构建中,请勿点击,耐心等候...");

            $.ajax({
                url: ctx + '/aea/item/createUnionItemCode.do',
                type: 'post',
                data: {},
                success: function (result) {
                    if (result.success) {

                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal('提示信息', "构建事项唯一编号成功！", 'info');
                            refreshAllItemList();
                        }, 500);

                    } else {

                        setTimeout(function () {
                            $("#uploadProgress").modal('hide');
                            swal('错误信息', "构建事项唯一编号失败！", 'error');
                        }, 500);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {

                    setTimeout(function () {
                        $("#uploadProgress").modal('hide');
                        swal('错误信息', XMLHttpRequest.responseText, 'error');
                    }, 500);
                }
            });
        }
    });
}

function isSelectBscDicRegion(obj, isSearch) {

	if (isSearch) {
		$('#isSelectRegionSearch').val('search');
	} else {
		$('#isSelectRegionSearch').val('noSearch');
	}
	if (obj) {
		var value = $(obj).val();
		if (!value) {
			selectBscDicRegionZtree();
		}
	} else {
		selectBscDicRegionZtree();
	}
}

