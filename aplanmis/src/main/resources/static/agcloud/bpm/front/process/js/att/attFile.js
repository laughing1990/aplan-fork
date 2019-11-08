
var ADMIN_ATT_URL = ctx + "/bsc/att/admin";
var COMMON_ATT_URL = ctx + "/me/bsc/att";
var isCommon = 1;//0代表管理员界面,1代表业务界面
var maxFileSize = 209715200;

//业务附件上传参数
// var tableName = parent.vm._tableName;
var tableName = 'AEA_HI_APPLYINST';
// var pkName = parent.vm._pkName;
var pkName = 'APPLYINST_ID';
// var attLink = parent.vm.attLink;
var attLink = '';

var recordIds = parent.app.recordIds ? parent.vm.recordIds : "";//用于附件查询
var recordId = parent.app.recordId;//用于附件上传 2018-11-15 将taskId 改为 recordId

//开关
// var btnDisable = parent.vm.btnDisable ? parent.vm.btnDisable : false;//按钮链接是否可以点

var reNameBtn = isCommon ? '' : '<img src="' + ctx + '/agcloud/bpm/front/att/images/gd.png" class="pointer rename" title="重命名"/>';
var showCreateTime = function (date) {
    return isCommon ? '' : '<td class="uploadTime">' + custDateFormat(date) + '</td>';
};
var join = ",";

var maxlen = 15;//名称超过最大长度截取
var cutDateStr = false;//是否只显示日期
//页面变量
var isShowView = false;//是否以视图方式显示
var showRootDom;//视图显示区域根元素
var timeTask = null;//定时调度任务

var dirId = '';//当前目录ID

var att = new Vue({
    el:"#attFile",
    data:{
        tableHeigth:document.documentElement.clientHeight - 170,
        attDirsList:[],// 文件夹列表
        attFilesList:[], // 文件列表
        attFileLoading:false,
        activefile:false,
        keyword:'',//搜索文件关键词
        DirsMultipleSelection:[],
        FilesMultipleSelection:[],
        checkedfile:'',
    },
    created:function(){
        this.getFileList();
    },
    methods: {
        getFileList: function (dirId) {
            var vm = this;
            //vm.attFileLoading = true
            var params = {
                tableName: tableName,
                recordIds: recordId,
                isSeries: parent.app.isSeriesinst
            }
            if (dirId) {
                params.dirId = dirId
            }
            request('', {
                url: ctx+'rest/approve/att/dirs/flle/list',
                type: 'get',
                data: params,
            }, function (res) {
                vm.attFileLoading = false
                vm.attDirsList = res.content.dirs
                vm.attFilesList = res.content.files
            }, function () {
                vm.attFileLoading = false
            });
        },
        /**
         * 中等图标双击事件查看详情
         * @param row
         */
        dbclickDetail: function (row) {
            dirId = row.dirId
            this.getFileList(row.dirId);
        },
        fileTableRowClick: function (row) {
            console.log(row)
            dirId = row.dirId
            this.getFileList(row.dirId);
        },
        /**
         * 点击选中文件夹
         */
        toggleCheckfile: function (item,type) { // type  1表示选中文件夹  2表示选中文件
            var vm = this;
            if(type == 1){
                var index1 = -1
                vm.DirsMultipleSelection.forEach(function (el1,ind1) {
                    if(el1.detailId == item.detailId){
                        index1 = ind1
                    }
                })
                if(index1>=0){
                    vm.DirsMultipleSelection.splice(index1,1);
                }else{
                    vm.DirsMultipleSelection.push(item);
                }
            }else{
                var index2 = -1
               vm.FilesMultipleSelection.forEach(function (value,ind) {
                   console.log(value)
                    if(value.detailId == item.detailId){
                        index2 = ind
                    }
                })
                if(index2>=0){
                    vm.FilesMultipleSelection.splice(index2,1);
                }else{
                    vm.FilesMultipleSelection.push(item);
                }
            }
            if (item.checkfile) {
                item.checkfile = !item.checkfile
            } else {
                this.$set(item, 'checkfile', true)
            }
            this.checkfileNum()
        },
        /**
         * 全选/取消全选 文件夹
         */
        checkfileAll: function () {
            var vm = this;
            var isCheckAll = $("#checkAll_view").prop('checked');
            if (vm.attDirsList.length > 0) {
                vm.attDirsList.forEach(function (item) {
                    if (item.checkfile) {
                        item.checkfile = isCheckAll
                    } else {
                        vm.$set(item, 'checkfile', isCheckAll)
                    }
                })
            } else {
                vm.attFilesList.forEach(function (item) {
                    if (item.checkfile) {
                        item.checkfile = isCheckAll
                    } else {
                        vm.$set(item, 'checkfile', isCheckAll)
                    }
                })
            }
            vm.checkfileNum()
        },
        /**
         * 已选中文件/文件夹的个数
         */
        checkfileNum: function () {
            var vm = this;
            var total = 0;
            if (vm.attDirsList.length > 0) {
                vm.attDirsList.forEach(function (item) {
                    if (item.checkfile) {
                        total += 1;
                        if (total >= vm.attDirsList.length) {
                            $("#total_select").text("全选");
                            $("#checkAll_view").prop('checked', true);
                        }
                        $("#total_select").text("已选中" + total + "个文件/文件夹");
                    } else {

                        $("#total_select").text("已选中" + total + "个文件/文件夹");
                        if (total <= 0) {
                            $("#total_select").text("全选");
                            $("#checkAll_view").prop('checked', false);
                        }
                    }
                })
            } else {
                vm.attFilesList.forEach(function (item) {
                    if (item.checkfile) {
                        total += 1;
                        if (total >= vm.attFilesList.length) {
                            $("#total_select").text("全选");
                            $("#checkAll_view").prop('checked', true);
                        }
                        $("#total_select").text("已选中" + total + "个文件/文件夹");
                    } else {

                        $("#total_select").text("已选中" + total + "个文件/文件夹");
                        if (total <= 0) {
                            $("#total_select").text("全选");
                            $("#checkAll_view").prop('checked', false);
                        }
                    }
                })
            }

        },
        // 重置选中的文件
        resetFileData:function(){
            var vm = this;
            vm.DirsMultipleSelection = [];
            vm.FilesMultipleSelection = [];
            vm.attDirsList.forEach(function (item,index) {
                vm.$set(item, 'checkfile', false)
            })
            vm.attFilesList.forEach(function (item) {
                vm.$set(item, 'checkfile', false)
            })
        },
        //绑定返回上一级目录事件
        backToLast: function () {
            this.getFileList()
        },
        // 刷新
        refresh: function () {
            var vm = this;
            $('#searchKey').val('');
            if (vm.attDirsList.length > 0) {
                vm.getFileList();
            } else {
                vm.getFileList(dirId);
            }
        },
        /**
         * 搜索文件
         */
        searchFileData: function () {
            var vm = this;
            var params = {
                keyword: vm.keyword,
            }
            request('', {
                url: ctx + '/me/bsc/att/search.do',
                type: 'post',
                data: params,
            }, function (res) {
                vm.attFileLoading = false
                vm.attDirsList = res.dirs
                vm.attFilesList = res.files
            }, function () {
                vm.attFileLoading = false
            });
        },
        /**
         * 勾选的文件夹
         */
        changeDirsSelection: function (val) {
            this.DirsMultipleSelection = val
        },
        changeFilesSelection: function (val) {
            this.FilesMultipleSelection = val
        },
        filesMouseEnter:function(row, column, cell, event) {
            $('#'+row.detailId).show()
        },
        filesMouseLeave:function(row, column, cell, event) {
            $('#'+row.detailId).hide()
        },
        //处理下载,0 是根据 每一行的点击下载按钮进行下载， 1 是根据勾选的文件批量下载， 2 是单个文件夹下载， 3 是多个文件夹下载
        doDownload: function (type, key) {
            console.log('执行下载')
            var vm = this;
            var type = type
            var key = key
            var DirsMultipleSelection = vm.DirsMultipleSelection
            var FilesMultipleSelection = vm.FilesMultipleSelection
            if(!type && DirsMultipleSelection.length>0){
                type = 2
            }
            if(!type && FilesMultipleSelection.length>0){
                type = 1
            }

            if (type == 0) {
                var url = ctx + "rest/mats/att/download?fileId=" + key;
                window.location.href = url;
            } else if (type == 1) {

                if (!key && FilesMultipleSelection.length === 0) {
                    //alertMsg('', '你还没选中文件');
                    vm.$notify.info({
                        title: '错误',
                        message: '你还没选中文件'
                    });
                    return;
                } else if (!key && DirsMultipleSelection.length > 0) {
                    vm.$message.info('只支持批量文件下载，不支持文件夹下载');
                    return;
                } else {
                    if(!key){
                        var fileIdsArry = [];
                        FilesMultipleSelection.forEach(function (item, index) {
                            fileIdsArry.push(item.detailId);
                        });
                        var fileIdsStr = fileIdsArry.join(',')
                    }else{
                        var fileIdsStr = key
                    }

                    var url = ctx + "rest/apply/att/download?detailIds=" + fileIdsStr; //本地磁盘下载
                   // window.location.href = url;
                    // 转换完成，创建一个a标签用于下载
                    var a = document.createElement('a');
                    a.href = url;
                    $("body").append(a);    // 修复firefox中无法触发click
                    a.click();
                    $(a).remove();
                }
            } else if (type == 2) {
                // att.$message.info('文件夹下载功能暂未开放');
                vm.$notify.info({
                    title: '提示',
                    message: '文件夹下载功能暂未开放'
                });
            } else {
                vm.$notify.info({
                    title: '提示',
                    message: '你没选中要下载的文件'
                });
            }
        },
    },
    watch:{
        activefile:function(newVal,oldVal){
            this.resetFileData()
            if(newVal){
                $("#show_list").hide();
                $("#show_view").show();
            }else{
                $("#show_list").show();
                $("#show_view").hide();
            }
        }
    }
})

$(function () {
    //var textlength = $('#text-td').width() - 90;
    //maxlen = textlength / 12;
    cutDateStr = $('#modifyTime-td').width() < 125;
    bingGlobalAction();//执行绑定事件
    // loadPersonData();//加载文件夹下子文件和文件
    showRootDom = $('#file_list');

    //webUploader
    var uploadUrl = ctx + 'rest/approve/upload?attType=0&tableName='+tableName+'&pkName='+pkName+'&recordId='+recordId;
    var uploader;
    uploader = WebUploader.create({
        pick:{
            id: '#uploadel',
        },
        dnd: document.body, //拖拽上传区域
        paste: document.body, //截屏Ctrl+V上传区域
        swf: ctx + '/ui-static/agcloud/bsc/att/me/js/Uploader.swf',
        auto:true,//是否自动上传
        compress: false,//是否压缩上传
        chunked: false, //是否要分片处理大文件上传
        chunkSize: 512 * 1024, //分片大小
        server: uploadUrl, //上传地址
        disableGlobalDnd: true, // 禁掉全局的拖拽功能。这样不会出现图片拖进页面的时候，把图片打开。
        fileNumLimit: 300, //验证文件总数量, 超出则不允许加入队列
        fileSizeLimit: maxFileSize,    //验证文件总大小是否超出限制, 超出则不允许加入队列
        fileSingleSizeLimit: maxFileSize    //验证单个文件大小是否超出限制, 超出则不允许加入队列。
    });
    uploader.on('ready', function() {
        window.uploader = uploader;
       /* $('#upload').find('input[name=file]').parent()
            .height(30).width(83);//调整上传按钮的宽高*/
    });
    uploader.on( 'uploadSuccess', function(file,response) {
        //agcloud.ui.metronic.showSuccessTip("上传完成",1000);
        att.getFileList();  // 上传成成刷新目录
        if(attLink && response && response.success){
            var backDetailId = response.content;
            console.log(backDetailId);
            var bscAttLink = {};
            $.extend(bscAttLink,attLink, {detailId:backDetailId});
            $.ajax({
                url: ctx +'me/bsc/att/saveAttLink.do',
                type: "post",
                cache: false,
                data: bscAttLink,
                dataType: 'json',
                success: function (result) {
                    if(parent.vm && parent.vm.initHistoryComment)
                        parent.vm.initHistoryComment();
                },
                error:function () {
                    console.log('保存关联出错');
                    //agcloud.ui.metronic.showErrorTip("上传文件出错", 1000, false);
                }
            });
        }
    });
    //上传之前加入文件夹参数
    uploader.on( 'uploadBeforeSend', function(object, data, headers) {
        var param = {
            dirId:dirId
        };
        $.extend(data, param);
    });
});
//绑定相关全局事件
function bingGlobalAction() {
    // if (btnDisable) {
    //     $("#upload").attr("disabled",true).css("pointer-events","none");
    //     return;
    // }
    //绑定上传按钮的点击事件
    //$("#upload").bind('click',uploadClick);
    //良田拍照上传
    $("#lt_upload").bind('click',function (ev) {
        parent.lt_openPhotoWindow(null,null,null,uplaodDetail);
    });
    //方正拍照上传
    $("#fz_upload").bind('click',function (ev) {
        parent.fz_openPhotoWindow(null,null,null,uplaodDetail);
    });

    //绑定上面工具栏，删除按钮功能，批量删除，文件删除和文件夹删除区别对待
    $("#remove").bind('click',function () {
        doRemove();
    });
    //绑定创建目录事件
    $("#createDir").bind('click',function () {
        //限制新建文件夹保存之后才能再新建
        if($('#currentEditInput').length>0)return;
        createDir();
    });
    //绑定返回上一级目录事件
    // $("#backToLast").bind('click',function () {
    //     backToLast();
    // });
    $("#refresh").bind('click',function () {
        $('#searchKey').val('');
        loadPersonData();
    });
    //绑定全部文件事件
    $("#all_file").click(function () {
        if($(this).hasClass('allfile-link')){
            if(dirId=='sbcl_2018_augurit'){
                reBindgGlobalAction();
            }
            dirId = "";//置空默认到 根目录
            loadPersonData();
        }
    });
    //绑定查询按钮事件
    $("#search").click(function () {
        globalSearch();
    });
    //绑定回车查询事件
    $("#searchKey").bind('keypress', function (event) {
        if (event.keyCode == "13") {
            event.preventDefault();
            //回车执行查询
            globalSearch();
        }
    });
    $('#reload').bind('click', function (event) {
        loadPersonData();
        $("#searchKey").val('');
    });
    //绑定checkbox的事件
    $("#checkAll").on('click', function () {
        var flag = false;
        if ($(this).is(':checked')) {
            flag = true;
        }
        $("#fileDirTable tbody").find('input:checkbox').each(function () {
            $(this).prop('checked', flag);//此处不要用attr方法，会点击后失效
        });
    });
    $("#checkAll_view").on('click', function () {
        if ($(this).is(':checked')) {
           var selectNum =  $("#file_view").find('.td-cust').addClass('check-file').length;
           $('#total_select').text('已选中'+selectNum+'个文件/文件夹');
        }else{
            $("#file_view").find('.td-cust').removeClass('check-file');
            $('#total_select').text('全选');
        }
    });
}
function unbingGlobalAction() {
    // $("#upload").unbind();
    $('#upload').find('label').hide();
    $("#lt_upload").unbind();
    $("#fz_upload").unbind();
    $("#remove").unbind();
    $("#createDir").unbind();
}
function reBindgGlobalAction() {
    //绑定上传按钮的点击事件
    //$("#upload").bind('click',uploadClick);
    $('#upload').find('label').show();
    //良田拍照上传
    $("#lt_upload").bind('click',function (ev) {
        parent.lt_openPhotoWindow(null,null,null,uplaodDetail);
    });
    //方正拍照上传
    $("#fz_upload").bind('click',function (ev) {
        parent.fz_openPhotoWindow(null,null,null,uplaodDetail);
    });
    //绑定上面工具栏，删除按钮功能，批量删除，文件删除和文件夹删除区别对待
    $("#remove").bind('click',function () {
        doRemove();
    });
    //绑定创建目录事件
    $("#createDir").bind('click',function () {
        createDir();
    });
}
function bindViewAction() {
    $('.td-cust').bind('click',function () {
        var _this = $(this);
        clearTimeout(timeTask);
        timeTask = setTimeout(function () {
            viewSelected(_this);
        },250);
    });
    $('.td-cust').bind('dblclick',function () {
        clearTimeout(timeTask);
        var entityId = $(this).data('key');
        if('dir'==$(this).data('type')){
            doEnterDir(entityId);
        }else{
            // parent.filePreview(entityId,$(this).data('format'));
        }
    });
}
function viewSelected(_$file){
    _$file.toggleClass('check-file');
    var selectNum =  $("#file_view").find('.td-cust.check-file').length;
    if(selectNum>0){
        $('#total_select').text('已选中'+selectNum+'个文件/文件夹');
    }else{
        $('#total_select').text('全选');
    }
}

//重写良田拍照上传方法
function uplaodDetail(formData) {
    var url = COMMON_ATT_URL + '/gaoPaiYiUpload.do';
    formData.append('tableName', tableName);
    formData.append('pkName', pkName);
    formData.append('recordId', recordId);
    formData.append('dirId', dirId);//标识高拍仪上传
    formData.append('existFile', false);
    $.ajax({
        url: url,
        type: "post",
        cache: false,
        data: formData,
        contentType: false,
        processData: false,
        // data:{
        //     base64Data:base64Data,
        //     realFileName: realFileName,
        //     tableName: tableName,
        //     pkName: pkName,
        //     recordId: recordId,
        //     dirId: dirId,
        //     attName:realFileName,
        //     existFile:false
        // },
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                loadPersonData();
                parent.closeGaopaiyiModal();
            } else {
                parent.vm.$message.error(result.message);
            }
        },
        error: function (msg) {
            parent.vm.$message.error("上传文件出错");
        }
    });
}

function setDirId(_dirId) {
    dirId = _dirId;
}

/**
 * 2018-12-4 抽取出来，负责根据参数请求数据并展示
 * @param url
 * @param params
 * @param callback
 */
function doQueryAndShowData(url, params, callback) {
    $.ajax({
        type: "post",
        url: url,
        data: params,
        success: function (data) {
            setDirId('');//搜索切换到根目录
            if (!data || data.list.length <= 0) {
                showRootDom.html('<div style="width: 100%;text-align: center">未查询到文件</div>');
                $('.file-total').html(0);//统计文件数量
                return;
            }
            var entitys = data.list, total = data.total,
                dirTag = "", fileTag = "",
                entity, isDir,
                dirName, dirId,
                userName, orgName;
            if (isShowView) {
                for (var i in entitys) {
                    entity = entitys[i];
                    isDir = entity.fileType == 'dir';
                    dirName = entity.parentName ? entity.parentName : '根目录';
                    dirId = entity.parentId ? entity.parentId : '';
                    if (isDir) {
                        dirTag += '<div class="td-cust" ddata-type="dir" data-key="' + entity.fileId + '">\n' +
                            '                    <div class="file-image folder-icon"></div>\n' +
                            '                    <div class="file-name">\n' +
                            '                        <a href="javascript:void(0);" title="' + entity.fileName + '">' + entity.fileName + '</a>\n' +
                            '                    </div>\n' +
                            '                </div>';
                    } else {
                        entity.attName = entity.fileName;
                        setFileIcon(entity);
                        fileTag += '<div class="td-cust" data-type="file" data-key="' + entity.fileId + '"  data-format="' + entity.fileType + '">\n' +
                            '                    <div class="file-image"><img src="' + ctx + '/agcloud/bpm/front/att/images/file-view/file_' + entity.icon + '.png" /></div>\n' +
                            '                    <div class="file-name">\n' +
                            '                        <a href="javascript:void(0);" title="' + entity.fileName + '">' + entity.fileName + '</a>\n' +
                            '                    </div>\n' +
                            '                </div>';
                    }
                }
                showRootDom.html(dirTag + fileTag);
                $("#checkAll_view").attr("checked", false);//取消全选
                $('#total_select').text('全选');
                bindViewAction();
            } else {
                for (var i in entitys) {
                    entity = entitys[i];
                    isDir = entity.fileType == 'dir';
                    dirName = entity.parentName ? entity.parentName : '根目录';
                    dirId = entity.parentId ? entity.parentId : '';
                    if (isDir) {
                        dirTag += '<tr>' +
                            '<td class="name">' +
                            '<label class="m-checkbox m-checkbox--solid m-checkbox--brand"><input type="checkbox" data-type="dir" data-key="' + entity.fileId + '"/><span></span></label>' +
                            '<img src="' + ctx + '/agcloud/bpm/front/att/images/new-file/folder.png" class="folderIcon" /><span class="itemName pointer" title="' + entity.fileName + '">' + cutString(entity.fileName, maxlen) + '</span>' +
                            '<span class="editName hide">' +
                            '<input type="text" class="editItem"/>' +
                            '<span>' +
                            '<img src="' + ctx + '/agcloud/bpm/front/att/images/confirm.png" class="pointer editConfirm" />' +
                            '<img src="' + ctx + '/agcloud/bpm/front/att/images/close.png" class="pointer editCancel" />' +
                            '</span>' +
                            '</span>' +
                            '<span class="wjj-tool hide" data-type="dir" data-key="' + entity.fileId + '">' +
                            '<img src="' + ctx + '/agcloud/bpm/front/att/images/xz.png" class="pointer download" title="下载"/>' +
                            reNameBtn +
                            '</span>' +
                            '</td>' +
                            /*'<td class="size">-</td>' +*/
                            showCreateTime(entity.modifyTime) +
                            /*'<td class="uploadTime">' + custDateFormat(entity.createTime) + '</td>' +*/
                            /*'<td class="dir"><a href="#" class="toDir" data-key="' + dirId + '">' + dirName + '</a></td>' +*/
                            '</tr>';
                    } else {
                        entity.attName = entity.fileName;
                        userName = entity.createrName ? entity.createrName : '';
                        orgName = entity.orgName ? entity.orgName : '';
                        setFileIcon(entity);
                        fileTag += '<tr>' +
                            '<td class="name">' +
                            '<label class="m-checkbox m-checkbox--solid m-checkbox--brand"><input type="checkbox" data-type="file" data-key="' + entity.fileId + '"  data-format="' + entity.fileType + '"/><span></span></label>' +
                            '<img src="' + ctx + '/agcloud/bpm/front/att/images/new-file/file_' + entity.icon + '.png" class="fileIcon"/><span class="itemName pointer" title="' + entity.fileName + '">' + cutString(entity.fileName, maxlen) + '</span>' +
                            '<span class="editName hide">' +
                            '<input type="text" class="editItem"/>' +
                            '<span>' +
                            '<img src="' + ctx + '/agcloud/bpm/front/att/images/confirm.png" class="pointer editConfirm" />' +
                            '<img src="' + ctx + '/agcloud/bpm/front/att/images/close.png" class="pointer editCancel" />' +
                            '</span>' +
                            '</span>' +
                            '<span class="wjj-tool hide" data-type="file" data-key="' + entity.fileId + '">' +
                            '<img src="' + ctx + '/agcloud/bpm/front/att/images/xz.png" class="pointer download" title="下载"/>' +
                            reNameBtn +
                            '</span>' +
                            '</td>' +
                            /*'<td class="size">' + formatSize(entity.fileSize) + '</td>' +*/
                            showCreateTime(entity.modifyTime) +
                            /*'<td class="uploadTime">' + custDateFormat(entity.createTime) + '</td>' +*/
                            '<td class="uploadTime">' + userName + '</td>' +
                            '<td class="uploadTime">' + orgName + '</td>' +
                            /*'<td class="dir"><a href="#" class="toDir" data-key="' + dirId + '">' + dirName + '</a></td>' +*/
                            '</tr>';
                    }
                }
                //显示搜索结果
                showRootDom.html(dirTag + fileTag);
                $('.file-total').html(total);//统计文件数量

                $("#checkAll").attr("checked", false);//取消全选

                //$(".dir").removeClass("hide");
                $(".toDir").click(function () {
                    var key = $(this).attr("data-key");
                    doEnterDir(key);
                });
                bindingAction();
            }
            $('#all_file').addClass('allfile-link').css('color', '#36A0F7');
            $('#dirXpath').empty();
            if (callback) {
                callback();
            }
        }
    });
}

/**
 * 外部调用接口，可共其他窗口调用 params格式跟当前窗口globalSearch方法中的params格式一致
 * @param params  主要是 tableName，pkName，recordIds ， 可以增加关键字查询和分页
 * @param callback
 */
function doQueryDetailUnion(params,callback) {
    var tempUrl = COMMON_ATT_URL + "/search.do";
    if(params){
        params = $.extend({
            page:1,
            rows:1000,
            sort:'fileName',
            order:'desc'
        },params);
        doQueryAndShowData(tempUrl,params,callback);
    }
}
//搜索文件列表
function globalSearch(type, callback) {
    var keyword = $("#searchKey").val();
    var url;
    var params;
    if (!keyword) {//搜索关键词为空时,跳到根目录
        dirId = '';
        loadPersonData();
        return;
    }
    if (isCommon) {
        url = COMMON_ATT_URL + "/search.do";
        params = {
            keyword: keyword,
            tableName: tableName,
            pkName: pkName,
            recordIds: recordIds,
            page:1,
            rows:1000,
            sort:'fileName',
            order:'desc'
        };//updateTime,fileName,fileSize
    } else {
        url = ADMIN_ATT_URL + "/search.do";
        params = {
            fileName: keyword,
        };
    }
    doQueryAndShowData(url, params, callback);
}

//格式化文件大小
function formatSize(size) {
    var unit = 1024;
    var unit1 = unit * unit;
    var unit2 = unit1 * unit;
    var unit3 = unit2 * unit;
    var result;
    if (size >= unit && size < unit1) {
        result = (size / unit).toFixed(1) + "KB";
    } else if (size >= unit1 && size < unit2) {
        result = (size / unit1).toFixed(1) + "MB";
    } else if (size >= unit2 && size < unit3) {
        result = (size / unit2).toFixed(1) + "GB";
    } else if (size >= unit3) {
        result = (size / unit3).toFixed(1) + "TB";
    } else {
        result = size + "B";
    }
    return result;
}

//绑定表格中的各个事件
function bindingAction() {
    if (btnDisable) {
        return;
    }
    //绑定tr相关事件，悬浮显示工具栏
    $("#fileDirTable").find("tr").each(function () {
        //悬浮显示工具栏
        $(this).mouseover(function () {
            $(this).find(".wjj-tool").removeClass("hide");
            $(this).find(".wjj-tool").show();
        });
        $(this).mouseout(function () {
            $(this).find(".wjj-tool").hide();
        });
    });
    //绑定表格中下载按钮功能
    $(".download").on('click', function () {
        var parent = $(this).parent("span");
        var key = parent.attr("data-key");
        var type = parent.attr("data-type");
        if ("file" == type) {
            doDownload(0, key);
        } else {
            doDownload(2, key);//暂时没做
        }
    });
    //绑定新建文件夹编辑框确认按钮事件
    $(".createConfirm").on('click', function () {
        var name = $("#currentEditInput").val();
        doCreateDir(name);
    });
    //绑定编辑框取消按钮事件
    $(".createCancel").on('click', function () {
        loadPersonData();
    })
    //绑定编辑框确认按钮事件
    $(".editConfirm").on('click', function () {
        var name = $(this).parent('span').prev('.editItem').val();
        var $input = $(this).parent('span').parent('span').siblings('label').find('input[type=checkbox]');
        var type = $input.attr("data-type");
        var id = $input.attr("data-key");
        var data = {type: type, id: id, name: name};
        doEdit(data);
    })
    //绑定编辑框取消按钮事件
    $(".editCancel").on('click', function () {
        loadPersonData();
    })
    //绑定重命名按钮事件
    $(".rename").on('click', function () {
        var $itemName = $(this).parent().siblings('.itemName');
        var title = $itemName.attr('title');
        $itemName.html(title);
        $itemName.addClass("hide");
        var $editName = $(this).parent().siblings('.editName');
        $editName.removeClass("hide");
        $editName.children('input').val($itemName.text());
    })
    //绑定文件夹\文件点击事件
    $(".itemName").on('click', function () {
        var $input = $(this).siblings('label').find('input[type=checkbox]');
        var type = $input.attr("data-type");
        var id = $input.attr("data-key");
        var format = $input.attr("data-format");
        if ("dir" == type) {
            //进入文件夹
            doEnterDir(id);
        } else {
            //暂时不开发预览功能后面可扩展 文件预览
            // parent.filePreview(id,format);
        }
    })
    $('input[type=checkbox]').click(function (e) {
        // e.stopPropagation();
    })
    $("#fileDirTable").find("tbody tr").on('click', function () {
        var $input = $(this).find('input[type=checkbox]');
        var flag = $input.prop("checked");
        $input.prop('checked', !flag);
    })
}

//处理删除，批量删除
function doRemove() {
    var fileIds = att.FilesMultipleSelection
    var dirIds = att.DirsMultipleSelection
    if( (fileIds == null || fileIds.length <= 0) && dirIds.length <= 0){
        att.$notify.info({
            title: '提示',
            message: '请选择要删除的文件或文件夹'
        });
        return;
    }
    if(dirIds.length > 0){
        att.$notify.info({
            title: '提示',
            message: '文件夹删除功能暂未开放'
        });
        return;
    }
    if ((fileIds == null || fileIds.length <= 0)) {
        att.$notify.info({
            title: '提示',
            message: '请选择要删除的文件或文件夹'
        });
        return;
    }
    var fileIdsArry = [];
    fileIds.forEach(function (item, index) {
        fileIdsArry.push(item.detailId);
    });
    var dirIdsArry = [];
    dirIds.forEach(function (item, index) {
        dirIdsArry.push(item.detailId);
    });
    var params = {
        dirIds: dirIdsArry.join(','),
        fileIds: fileIdsArry.join(',')
    };
    var msg = '你确定要删除吗？';
    if(parent.confirmMsg){
        parent.confirmMsg("删除附件","确定要删除该附件吗？",function () {
            var url = ctx + "/remove.do";
            $.ajax({
                url: url,
                data: params,
                type: 'POST',
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        att.$message.success('删除成功');
                        att.getFileList(dirId);
                    }else{
                        att.$message.error('删除失败,'+data.message);
                    }
                },
                error: function () {
                    att.$message.error('删除失败');
                }
            });
        });
    }
}

//获取勾选的文件
function getCheckedFiles() {
    var filesId = [];//文件key列表
    if(isShowView){
        $('#file_view').find('.td-cust.check-file').each(function () {
            if("file" == $(this).attr("data-type")){
                var key = $(this).attr("data-key");
                filesId.push(key);
            }
        });
    }else{
        $("#fileDirTable tbody").find('input:checkbox').each(function () {
            if ($(this).is(':checked') && "file" == $(this).attr("data-type")) {
                var key = $(this).attr("data-key");
                filesId.push(key);
            }
        });
    }
    console.log('勾选文件的id',filesId)
    return filesId;
}

//获取勾选的文件夹
function getCheckedDirs() {
    var dirsId = [];//文件key列表
    if(isShowView){
        $('#file_view').find('.td-cust.check-file').each(function () {
            if("dir" == $(this).data("type")){
                var key = $(this).data("key");
                dirsId.push(key);
            }
        });
    }else {
        $("#fileDirTable tbody").find('input:checkbox').each(function () {
            if ($(this).is(':checked') && "dir" == $(this).attr("data-type")) {
                var key = $(this).attr("data-key");
                dirsId.push(key);
            }
        });
    }
    return dirsId;
}
//创建文件夹相关操作
function createDir() {
    if(isShowView){
        $('#file_view').prepend('<div class="td-cust">\n' +
            '                    <div class="file-image folder-icon"></div>\n' +
            '                    <div class="file-name">\n' +
            '                        <input name="" value="" id="currentEditInput"  style="width:100px;"/>\n' +
            '                    </div>\n' +
            '                </div>');
        $('#currentEditInput').focus().val('新建文件夹');
        $('#currentEditInput').bind('blur',function () {
            var dirName = $(this).val();
            doCreateDir(dirName);
        });
    }else{
        var tr = '<tr><td class="name" colspan="3">' +
        '<label class="m-checkbox m-checkbox--solid m-checkbox--brand"><input type="checkbox" data-type="dir"/><span></span></label>' +
        '<img src="' + ctx + '/agcloud/bpm/front/att/images/new-file/folder.png" class="folderIcon" />'+
        '<span class="editName">' +
            '<input type="text" class="editInput" id="currentEditInput" value="新建文件夹"/>' +
            '<span>' +
                '<img src="' + ctx + '/agcloud/bpm/front/att/images/confirm.png" class="pointer createConfirm" />' +
                '<img src="' + ctx + '/agcloud/bpm/front/att/images/close.png" class="pointer createCancel" />' +
            '</span>' +
        '</span>' +
        '</td></tr>';
        if ($("#fileDirTable").find("tbody tr").first().length > 0) {
            $("#fileDirTable").find("tbody tr").first().before(tr);
        } else {
            $("#fileDirTable").find("tbody").append(tr);
        }
        bindingAction();
    }
}

//创建文件夹提交接口
// function doCreateDir(dirName) {
//     var url = ADMIN_ATT_URL + "/createDir.do";
//     var params = {
//         dirName: dirName,
//         parentId: dirId,
//         dirLevel: "4"
//     };
//     $.ajax({
//         url: url,
//         data: params,
//         type: 'POST',
//         dataType: "json",
//         success: function (_data) {
//             if (_data.success) {
//                 var dir = _data.content;
//                 if(isShowView){
//                     $('#currentEditInput').parents('.td-cust').data({
//                         type:'dir',
//                         key:dir.dirId
//                     }).bind('click',function () {
//                         viewSelected($(this));
//                     }).bind('dblclick',function () {
//                         clearTimeout(timeTask);
//                         var entityId = $(this).data('key');
//                         if('dir'==$(this).data('type')){
//                             doEnterDir(entityId);
//                         }else{
//                             // parent.filePreview(entityId,$(this).data('format'));
//                         }
//                     });
//                     $('#currentEditInput').parent().html('<a>'+dir.dirName+'</a>');
//                 }else{
//                     var newtr = $("#currentEditInput").parent().parent().parent();
//                     var newDir = '<td class="name">' +
//                     '<label class="m-checkbox m-checkbox--solid m-checkbox--brand"><input type="checkbox" data-type="dir" data-key="' + dir.dirId + '"/><span></span></label>' +
//                     '<img src="' + ctx + '/agcloud/bpm/front/att/images/new-file/folder.png" class="folderIcon" /><span class="itemName pointer" title=' + dir.dirName + '>' + cutString(dir.dirName, maxlen) + '</span>' +
//                     '<span class="editName hide">' +
//                     '<input type="text" class="editItem"/>' +
//                     '<span>' +
//                     '<img src="' + ctx + '/agcloud/bpm/front/att/images/confirm.png" class="pointer editConfirm" />' +
//                     '<img src="' + ctx + '/agcloud/bpm/front/att/images/close.png" class="pointer editCancel" />' +
//                     '</span>' +
//                     '</span>' +
//                     '<span class="wjj-tool hide" data-type="dir" data-key="' + dir.dirId + '">' +
//                     '<img src="' + ctx + '/agcloud/bpm/front/att/images/xz.png" class="pointer download" title="下载"/>' +
//                     '<img src="' + ctx + '/agcloud/bpm/front/att/images/gd.png" class="pointer rename" title="重命名"/>' +
//                     '</span>' +
//                     '</td>' +
//                     showCreateTime(dir.modifyTime) +
//                     '<td class="uploadTime">-</td>'+
//                     '<td class="uploadTime">-</td>';
//                     newtr.html(newDir);
//                     bindingAction();
//                 }
//             }
//         },
//         error: function (status) {
//             parent.vm.$message.error('创建文件夹失败!');
//         }
//     });
// }

//编辑，重命名接口
function doEdit(data) {
    var url = ADMIN_ATT_URL + "/rename.do";
    $.ajax({
        url: url,
        data: data,
        type: 'POST',
        dataType: "json",
        success: function (data) {
            if (data.success) {
                loadPersonData();
            }
        },
        error: function (status) {
            parent.vm.$message.error('重命名失败！');
        }
    });
}

//进入目录接口
function doEnterDir(id) {
    if (!id) {
        dirId = '';
        loadPersonData();
        return;
    }
    if(id=='pwpf_2018_augurit'||id=='sbcl_2018_augurit'){
        dirId = id;
        $("#dirXpath").empty();
        var dirPath;
        if(id=='pwpf_2018_augurit'){
            dirPath = '<span>>批文批复</span>';
        }else{
            dirPath = '<span>>申办材料</span>';
        }
        $("#dirXpath").append(dirPath);
        if(id=='sbcl_2018_augurit'){
            unbingGlobalAction();
        }
        loadPersonData();
        return;
    }
    dirId = id;//记录当前目录id
    var url = ADMIN_ATT_URL + "/getDir.do";
    $.ajax({
        url: url,
        data: {dirId: id},
        type: 'POST',
        dataType: "json",
        success: function (data) {
            function innerFunc() {
                $("#dirXpath").empty();
                var dirNameSeq = data.dirNameSeq.split(".");
                var dirSeq = data.dirSeq.split(".");
                var dirPath = '';
                for (var i = 0; i < dirSeq.length; i++) {
                    if (i == dirSeq.length - 1) {
                        dirPath += '<span> > ' + dirNameSeq[i] + '</span>'
                    } else {
                        dirPath += '<span class="dirItem" id="' + dirSeq[i] + '"> > ' + dirNameSeq[i] + '</span>'
                    }
                }
                $("#dirXpath").append(dirPath);
                //点击任意目录路径的事件
                $(".dirItem").click(function () {
                    dirId = $(this)[0].id;
                    doEnterDir(dirId);
                });
            }

            loadPersonData(innerFunc);
        },
        error: function (status) {
            parent.vm.$message.error('进入目录失败！');
        }
    });
}

//返回上一级目录
function backToLast() {
    if(!dirId)return;
    if(dirId=='pwpf_2018_augurit'||dirId=='sbcl_2018_augurit'){
        if(dirId=='sbcl_2018_augurit'){
            reBindgGlobalAction();
        }
        dirId = '';
        loadPersonData();
        return;
    }
    var url = ADMIN_ATT_URL + "/getDir.do";
    $.ajax({
        url: url,
        data: {dirId: dirId},
        type: 'POST',
        dataType: "json",
        success: function (data) {
            dirId = data.parentId;
            if (!dirId) {
                function temp() {
                    $(".allFile").removeClass("hide");
                    $(".weizhi").addClass("hide");
                }

                loadPersonData(temp);
            } else {
                $('#dirXpath span').last().remove();
                $('#dirXpath .dirItem').last().removeClass('dirItem');
                loadPersonData();
            }
        },
        error: function (status) {
            parent.vm.$message.error('返回上级目录失败！');
        }
    });
}

function custDateFormat(dateStr) {
    if (cutDateStr) {
        return dateStr ? parent.agcloud.bpm.utils.dateFormatByExp(dateStr, 'yyyy/MM/dd') : "-";
    } else {
        return dateStr ? parent.agcloud.bpm.utils.dateFormatByExp(dateStr, 'yyyy/MM/dd hh:mm') : "-";
    }

}

function setFileIcon(file) {
    var fm = file.attName;
    for (var key in fileRegexs) {
        if (fileRegexs[key].test(fm)) {
            file.icon = key;
            return;
        }
    }
    file.icon = 'empty';
}

var fileRegexs = {
    text: (/\.(txt|md)$/i),
    word: (/\.(docx|doc|wps)$/i),
    pdf: (/\.pdf$/i),
    picture: (/\.(png|jpg|jpeg|bmp|gif|svg|tiff)$/i),
    excel: (/\.(xls|xlsx)$/i),
    music: (/\.(mp3|wav|cad|wma|ra|midi|ogg|ape|flac|aac)$/i),
    video: (/\.(rm|rmvb|avi|flv|mp4|mkv|wmv|3gp|amv|mpeg1-4|mov|mtv|dat|dmv)$/i),
    code: (/\.(java|js|css|html)/i),
    exe: (/\.exe/i),
    psd: (/\.psd$/i),
    zip: (/\.(zip|rar|7z|gz|tar|bz2)$/i)
};

function cutString(str, len) {
    //length属性读出来的汉字长度为1
    if (str.length * 2 <= len) {
        return str;
    }
    var strlen = 0;
    var s = "";
    for (var i = 0; i < str.length; i++) {
        s = s + str.charAt(i);
        if (str.charCodeAt(i) > 128) {
            strlen = strlen + 2;
            if (strlen >= len) {
                return s.substring(0, s.length - 1) + "...";
            }
        } else {
            strlen = strlen + 1;
            if (strlen >= len) {
                return s.substring(0, s.length - 2) + "...";
            }
        }
    }
    return s;
}