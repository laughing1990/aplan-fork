// var ver_itemId,
//     ver_itemVerId,
//     hasNoActiveVer,
//     maxVerNum;
//
// // 加载定义版本下的事项
// function loadItemVerData(containerId){
//
//     // 清空
//     $(containerId).html('');
//     // 获取数据
//     $.ajax({
//         url: ctx+'/aea/item/ver/listAeaItemVerNoPage.do',
//         type:'post',
//         data:{'itemId': ver_itemId},
//         dataType: 'json',
//         success: function(data){
//             if(data!=null&&data.length>0){
//                 for(var i in data){
//                     var btn = null;
//                     var sxqxBtn = '<button type="button" class="btn btn-info btn-sm"  onclick="editItemBasicById(\''+data[i].itemBasicId+'\',\''+data[i].itemVerStatus+'\');">查看事项</button>';
//                     var sxBtn = '<button type="button" class="btn btn-primary btn-sm sxxq"  onclick="setItemVersionStates(\''+data[i].itemVerId+'\');">事项详情</button>';
//                     if(data[i].itemVerStatus=='UNPUBLISHED'){
//                         sxqxBtn = '<button type="button" class="btn btn-info btn-sm" onclick="editItemBasicById(\''+data[i].itemBasicId+'\',\''+data[i].itemVerStatus+'\');">编辑事项</button>';
//                         btn = '<button type="button" class="btn btn-metal btn-sm" onclick="setActiveCurItemVer(\''+data[i].itemVerId+'\',\''+data[i].verNum+'\',\''+data[i].itemVerStatus+'\',\''+data[i].isDeleted+'\');">未发布</button>';
//                     }else if(data[i].itemVerStatus=='TEST_RUN'){
//                         sxqxBtn = '<button type="button" class="btn btn-info btn-sm" onclick="editItemBasicById(\''+data[i].itemBasicId+'\',\''+data[i].itemVerStatus+'\');">编辑事项</button>';
//                         btn= '<button type="button" class="btn btn-warning btn-sm" onclick="setActiveCurItemVer(\''+data[i].itemVerId+'\',\''+data[i].verNum+'\',\''+data[i].itemVerStatus+'\',\''+data[i].isDeleted+'\');">试运行</button>';
//                     }else if(data[i].itemVerStatus=='PUBLISHED'){
//                         btn = '<button type="button" class="btn btn-warning btn-sm">已发布</button>';
//                     }else if(data[i].itemVerStatus=='DEPRECATED'){
//                         btn = '<button type="button" class="btn btn-metal btn-sm">已过时</button>';
//                     }
//                     var trContent = '<tr id="item_ver_'+ data[i].itemVerId +'" onclick="itemVerTrOnClick(this);" onmouseover="itemVerTrOnmouseover(this);" onmouseout="itemVerTrOnmouseout(this);">'+
//                                         '<td style="text-align: center;">'+
//                                             '<div class="row" style="margin: 0px;">'+
//                                                 '<div class="col-md-3">V'+ data[i].verNum +'</div>'+
//                                                 '<div class="col-md-9">'+ btn + '&#12288;' + sxqxBtn + '&#12288;' + sxBtn +'</div>'+
//                                             '</div>'+
//                                         '</td>'+
//                                     '</tr>';
//                     $(containerId).append(trContent);
//                     $(".sxxq").css("margin-right","-300px");
//                 }
//             }
//         }
//     });
// }
//
// // 行点击
// function itemVerTrOnClick(obj){
//
//     $("#item_ver_table tr").removeClass("trclick");
//     $(obj).addClass("trclick");
// }
//
// // 行鼠标移上
// function itemVerTrOnmouseover(obj){
//     $(obj).addClass("trover");
// }
//
// // 行鼠标移除
// function itemVerTrOnmouseout(obj){
//     $(obj).removeClass("trover");
// }
//
// // 默认选中第一行
// function selectFirstRow(){
//
//     $("#item_ver_table tr").removeClass("trclick");
//     $('#item_ver_table tr:eq(0)').addClass("trclick");
//     $('#item_ver_table tr:eq(0) td:eq(0)').trigger("click");
// }
//
// // 选中某一行
// function selectOneRow(id){
//
//     $("#item_ver_table tr").removeClass("trclick");
//     $('#item_ver_'+id).addClass("trclick");
//     $('#item_ver_'+id +' td:eq(0)').trigger("click");
// }
//
// function initItemVer(){
//
//     $('#itemVerTreeScrollable').niceScroll({
//
//         cursorcolor: "#e2e5ec",//#CC0071 光标颜色
//         cursoropacitymin: 0, // 当滚动条是隐藏状态时改变透明度, 值范围 1 到 0
//         cursoropacitymax: 1, // 当滚动条是显示状态时改变透明度, 值范围 1 到 0
//         touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
//         cursorwidth: "4px", //像素光标的宽度
//         cursorborder: "0", //   游标边框css定义
//         cursorborderradius: "2px",//以像素为光标边界半径
//         autohidemode: true  //是否隐藏滚动条
//     });
//
//     // 提示是否存在版本定义以及是否激活
//     if(hasNoActiveVer=='1'){
//         swal('提示信息', "没检测到当前事项发布版本,请设置!", 'info');
//     }
//     // 初始化版本库
//     loadItemVerData('#item_ver_tbd');
//     // 默认选中第一个节点
//     selectFirstRow();
//
//     //当Bootstrap 模态框（Modal）隐藏的时候，触发该事件
//     $('#item_ver_modal').on('hide.bs.modal', function () {
//         ver_itemId = '';
//         hasNoActiveVer = '';
//         maxVerNum = '';
//         // 列表数据重新加载
//         searchAllItemList();
//     })
// }
//
// // 设置事项版本
// function setItemVersionStates(itemVerId){
//
//     //此处需要控制事项情形是否可以编辑？
//     openFullWindow(ctx + '/rest/mind/mindIndex.do?busiType=item&busiId=' + itemVerId);
// }
//
// // 新增版本定义
// function addItemVer(){
//
//     var isCopy = false;
//     //判断最新版本是否已发布，如果已发布则可以进行版本复制，否则不可以
//     $.ajax({
//         url: ctx + '/aea/item/ver/listAeaItemVerNoPage.do',
//         type: 'POST',
//         data: {'itemId': ver_itemId, 'itemVerStatus': 'UNPUBLISHED','verNum': maxVerNum}, // 返回数据表示最大版本未发布
//         async: false,
//         success: function (data) {
//             // 已经存在最大启用版本
//            if(data!=null&&data.length>0){
//                swal('提示信息', '检测到存在最新未发布版本,不能进行版本复制!', 'info');
//            }else{
//                isCopy = true;
//            }
//         }
//     });
//     if(isCopy){
//         copyItemVerData();
//     }
// }
//
// //刷新页面
// function refreshItemVer(){
//
//     loadItemVerData('#item_ver_tbd');
//     selectFirstRow();
// }
//
// // 复制版本定义数据
// function copyItemVerData() {
//
//     $.ajax({
//         url: ctx + '/aea/item/ver/copy',
//         type: 'POST',
//         data: {'itemId': ver_itemId, 'itemVerStatus': 'PUBLISHED', 'verNum': maxVerNum},
//         success: function (result) {
//             if (result.success) {
//                 swal({
//                     type: 'success',
//                     text: '请先设置好相关情形、材料后再点击发布版本！',
//                     showConfirmButton: false,
//                     timer: 3000
//                 });
//                 var itemVerContent = result.content;
//                 maxVerNum = itemVerContent.verNum;
//                 loadItemVerData('#item_ver_tbd');
//                 selectFirstRow();
//             } else {
//                 swal('错误信息', result.message, 'error');
//             }
//         },
//         error: function () {
//             swal('错误信息', '事项版本复制失败！', 'error');
//         }
//     });
// }
//
// // 启用当前版本
// function setActiveCurItemVer(id,verNum,itemVerStatus,isDeleted){
//
//     if(verNum.indexOf('.') != -1 && itemVerStatus=='UNPUBLISHED'){
//         verNum = parseInt(verNum.split('.')[0]) + 1;                      //版本号更改为整数
//     }
//     if(itemVerStatus=='UNPUBLISHED' || itemVerStatus=='TEST_RUN'){
//         var msg="";
//         var param="";
//         if(itemVerStatus=='UNPUBLISHED'){
//             msg = "试运行";
//             param="TEST_RUN";
//         }else{
//             msg = "正式";
//             param="PUBLISHED";
//         }
//         swal({
//             title: '此操作影响：',
//             text: '此操作将发布当前事项为【'+msg+'】,您确定操作吗？',
//             type: 'warning',
//             showCancelButton: true,
//             confirmButtonText: '确定',
//             cancelButtonText: '取消',
//         }).then(function(result) {
//             if (result.value) {
//                 $.ajax({
//                     url: ctx + '/aea/item/ver/setActiveCurItemVer.do',
//                     type: 'POST',
//                     data: {'itemId': ver_itemId,'itemVerId': id,'verNum':verNum,'itemVerStatus':param},
//                     async: false,
//                     success: function (result) {
//                         if (result.success){
//                             swal({
//                                 type: 'success',
//                                 title: '启用成功！',
//                                 showConfirmButton: false,
//                                 timer: 1000
//                             });
//                             maxVerNum = verNum;
//                             // 重新记载数据
//                             loadItemVerData('#item_ver_tbd');
//                             selectFirstRow();
//                         }else {
//                             swal('错误信息', result.message, 'error');
//                         }
//                     },
//                     error: function () {
//                         swal('错误信息', '设置失败！', 'error');
//                     }
//                 });
//             } else {
//
//             }
//         });
//     }else{
//         swal({
//             type: 'success',
//             title: '当前版本已发布！',
//             showConfirmButton: false,
//             timer: 1000
//         });
//     }
// }
