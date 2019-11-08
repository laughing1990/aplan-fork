!(function(){
    var j = 0; // 控制文件上传数量
    var uploadFile = []; // 控制实质上传的文件对象
    var myCloundSpaces = new Vue({
        el:"#myCloundSpaces",
        data:{
            ctx:ctx,
            mloading:false,
            tableLoading:false,
            keyword:'',
            pageNum:1,
            pageSize:10,
            total:0,
            matFileList:[],
            mode:'列表',
            DirTreeData: [],
            DirTreeDataCopy:[],
            fileListData:[],
            TableClickRow:{}, // table 右键点击选中的数据
            dirId:'', // 选中的文件夹Id
            delDirId:'', // 右键删除文件夹id
            level:'',// 右键的等级  1表示父  2表示子
            dirName:'您还没选中文件夹', // 选中的文件名称
            chooseFileNum:"已选中0个文件",
            isViewModelCheckedAll:false,
            DirsMultipleSelection:[],
            FilesMultipleSelection:[],

            // 上传的文件(控制页面样式)
            uploadFile:[],

            // 新增文件夹
            addDirDialogFlag:false,
            superDir:[],
            addDirFormData: {},
            addDirFormRules:{
                dirName:[
                    { required: true, message: '请填写目录名称', trigger:'blur'},
                ],
                dirCode:[
                    { required: true, message: '请填写文件夹编码', trigger:'blur'},
                ],
            },

            // 移动文件
            moveFilesDialogFlag:false,
            targetDirId:'',

            // 重命名
            isReNameFile:0,
            detailId:'', // 当前文件Id
            reNameDialogFlag:false,
            reNameDirFormData:{},
            parentId:null,
        },
        created:function(){
           this.getDirTree();
        },
        mounted:function(){
            
        },
        methods:{
            // 用户信息查询文件夹Tree
            getDirTree:function () {
                var vm = this;
                vm.mloading = true;
                request('', {
                    url: ctx + 'rest/cloud/root/dir/tree',
                    type: 'get'
                }, function (res) {
                    console.log(res)
                    if(res.success){
                        var content = res.content;
                        vm.superDir = [];
                        content.forEach(function (item,index) {
                            if(item.childDirs && item.childDirs.length > 0 ) {
                                item.open = true;
                            }else{
                                item.open = false;
                            }
                            if(vm.superDir.length !== content.length ){
                                vm.superDir.push(item);
                            }
                        })
                        vm.DirTreeData = content;
                        vm.DirTreeDataCopy = JSON.parse(JSON.stringify(content))
                        vm.mloading = false;
                        for (var i = 0; i < content.length ; i++) {
                            if(content[i].childDirs && content[i].childDirs.length > 0 ) {
                                vm.getFileList(content[i].childDirs[0]);
                                break;
                            }else{
                                vm.getFileList(content[i]);
                                break;
                            }
                        }
                    }else{
                        vm.$message.error(res.message);
                        vm.mloading = false;
                    }

                },function () {
                    vm.$message.error('获取数据接口失败，请重试！');
                    vm.mloading = false;
                });
            },

            // 根据文件夹查询文件列表
            getFileList:function (itemSub) {
                var vm = this;
                var dirId = itemSub.dirId;
                vm.dirId = itemSub.dirId;
                vm.dirName = itemSub.dirName;
                vm.FilesMultipleSelection = [];
                vm.isViewModelCheckedAll = false;
                request('', {
                    url: ctx + 'rest/cloud/att/list/',
                    type: 'get',
                    data:{
                        dirId:dirId,
                        pageNum:vm.pageNum,
                        pageSize:vm.pageSize,
                        keyword:vm.keyword,
                    },
                }, function (res) {
                        var content = res.list;
                        content.forEach(function (item,index) {
                            item.selectFlag = false;
                        })
                        vm.fileListData = content;
                        vm.total = res.total;
                },function () {
                    vm.$message.error('获取数据接口失败，请重试！');
                });
            },
            handleOpenMenu:function(item,row,index){
                if(row){
                    item.open = !item.open;
                }
            },
            //Tree右键
            rigthClickTree:function($event,item,level){
                var _this = this;
                $event.returnValue = false;
                $event.cancelBubble = true;
                if (item) {
                    $('#right-click-opt-tree').show().css({ 'left': $event.pageX, 'top':$event.pageY });
                }
                this.delDirId = item.dirId;
                this.level = level;
                console.log(this.delDirId)
                setTimeout(function () {
                    _this.$set(_this.addDirFormData,'chooseSuperDir',_this.delDirId)
                },1000)
            },

            // table 右键
            rigthClickTable:function($event,item){
                $event.returnValue = false;
                $event.cancelBubble = true;
                if (item) {
                    $('#right-click-opt-table').show().css({ 'left': $event.pageX, 'top':$event.pageY });
                }
                this.TableClickRow = item;
            },

            // 新建文件夹
            sureAddDir:function(){
                var vm = this;
                vm.$refs['addDirForm'].validate(function(valid){
                    if (valid) {
                        var addDirFormData = vm.addDirFormData;
                        var parmas = {
                            isRoot:1,// 是否根文件夹 0否 1是
                            dirName:vm.addDirFormData.dirName,
                            dirCode:vm.addDirFormData.dirCode,
                        };
                        if(addDirFormData.chooseSuperDir){
                            parmas.isRoot = 0;
                            parmas.parentId = addDirFormData.chooseSuperDir
                        }
                        request('', {
                            url: ctx + 'rest/cloud/insertOrUpdateRootDir',
                            type: 'post',
                            data:parmas,
                        }, function (res) {
                            if(res.success){
                                vm.$message.success('新建成功');
                                vm.addDirFormData ={};
                                vm.getDirTree();
                            }else{
                                vm.$message.error('操作失败')
                            }
                        },function () {
                            vm.$message.error('请求接口错误，请稍后重试！');
                        });
                        vm.addDirDialogFlag = false
                    } else {
                        console.log('error submit!!');
                        //return false;
                    }
                });
            },

            // 删除文件夹 删除文件夹(会把子级的文件夹及文件也删掉)
            deleteDir:function(){
                var _this = this;
                confirmMsg('', '确定要删除吗？', function() {
                    request('bpmAdminUrl', {
                        type: 'get',
                        url: ctx + 'rest/cloud/deleteDirAndFiles/'+  _this.delDirId,
                    }, function(res) {
                        if(res.success){
                            _this.$message.success('删除成功');
                            _this.getDirTree();
                            var item = {
                                dirId: _this.dirId,
                                dirName:_this.dirName,
                            }
                            _this.getFileList(item);
                        }else{
                            _this.$message.error('删除失败')
                        }
                    }, function(err) {
                        _this.$message.error('请求接口出错了哦!');
                    })
                },function(){
                    console.log("取消");
                }, '确定', '取消')
            },

            // 重命名
            sureReName:function(){
                var _this = this;
                console.log(_this.reNameDirFormData.dirName);
                console.log(_this.detailId);
                if(_this.isReNameFile){
                    request('bpmAdminUrl', {
                        type: 'post',
                        url: ctx + 'rest/cloud/renameCloudFile',
                        data:{
                            attName:_this.reNameDirFormData.dirName,
                            detailId:_this.detailId,  // 文件ID
                        },
                    }, function(res) {
                        if(res.success){
                            _this.$message.success('重命名成功');
                            _this.getDirTree();
                        }else{
                            _this.$message.error('重命名失败')
                        }
                    }, function(err) {
                        _this.$message.error('请求接口出错了哦!');
                    })
                }else{
                    var parmas = {
                        dirId:_this.delDirId,
                        dirName:_this.reNameDirFormData.dirName,
                    }
                    if(_this.parentId){
                        parmas.parentId =_this.parentId;
                    }
                    request('bpmAdminUrl', {
                        type: 'POST',
                        url: ctx + 'rest/cloud/insertOrUpdateRootDir',
                        data:parmas,
                    }, function(res) {
                        if(res.success){
                            _this.$message.success('重命名成功');
                            _this.getDirTree();
                        }else{
                            _this.$message.error('重命名失败')
                        }
                    }, function(err) {
                        _this.$message.error('请求接口出错了哦!');
                    })
                }
            },

            // 选择文件
            chooseFile:function(e){
                var files = e.target.files;
                this.uploadFile = [];
                var totalSize = 0; // 控制选择的文件总大小不能超过100M
                for(var i=0,len = files.length;i < len;i++){
                    console.log(files[i])
                    var size = files[i].size;
                    var nameIndex = files[i].name.lastIndexOf(".");
                    var fileLen = files[i].name.length;
                    var attFormat = files[i].name.substring(nameIndex,fileLen);
                    files[i].attFormat = attFormat;
                    if(size > 1024 * 1024){
                        files[i].filesSize = (Math.round(size / (1024 * 1024))).toString() + "MB";
                        files[i].uploadedSize = 0 + "MB";
                    }else{
                        files[i].filesSize = (Math.round(size / 1024)).toString() + "KB";
                        files[i].uploadedSize = 0 + "KB";
                    }
                    totalSize = totalSize + size;
                    files[i].uploadedPer = 0;
                    this.uploadFile.push(files[i]);
                }

                if(totalSize > 100 * 1024 *1024){
                    this.$message.error("单次上传最大限制度为100M");
                    $("#fileMutiply").val(""); // 允许再次选择相同的文件
                    return false;
                }
                uploadFile = files;
                $("#el-notification").addClass("right");
                this.uploadCloudFiles();
            },

            // 云盘电子文件上传
            uploadCloudFiles:function(){
                var vm = this;

                if(j >= uploadFile.length) //采用递归的方式循环发送ajax请求
                {
                    $("#fileMutiply").val("");
                    setTimeout(function(){
                        $("#el-notification").removeClass("right");
                    },5000)
                    j = 0;
                    return;
                }
                var fileformData = new FormData();
                fileformData.append('files',uploadFile[j]);
                fileformData.append('dirId',this.dirId);
                $.ajax({

                    url: ctx + 'rest/cloud/uploadCloudFiles',

                    type: 'POST',

                    cache: false,

                    data: fileformData,

                    // 告诉jQuery不要去处理发送的数据
                    processData: false,

                    // 告诉jQuery不要去设置Content-Type请求头

                    contentType: false,

                    xhr:function() { //监听用于上传显示进度
                        var xhr = $.ajaxSettings.xhr();
                        if(onprogress && xhr.upload) {
                            xhr.upload.addEventListener("progress",onprogress, false);
                            return xhr;
                        }
                    },

                    success: function (data) {
                        var item = {
                            dirId: vm.dirId,
                            dirName:vm.dirName,
                        }
                        vm.getFileList(item);
                        // var index = vm.uploadFile.indexOf(uploadFile[j]);
                        // vm.uploadFile.splice(index,1);//移除当前文件进度条样式
                        j++; //递归条件
                        vm.uploadCloudFiles();

                    },
                    error:function(){
                        vm.$message.error('上传出错，请稍后重试！');
                        vm.$set(vm.uploadFile[j],'error','1');
                    }
                });
            },

            // 批量下载 批量下载电子件
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
                    var url = ctx + "rest/cloud/att/download?detailIds=" + key;
                    window.location.href = url;
                } else if (type == 1) {

                    if (!key && FilesMultipleSelection.length === 0) {
                        //alertMsg('', '你还没选中文件');
                        vm.$message.info({
                            showClose: true,
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
                            if(FilesMultipleSelection.length == 0){
                                vm.$message('您还没选中文件！');
                                return;
                            }
                            FilesMultipleSelection.forEach(function (item, index) {
                                fileIdsArry.push(item.detailId);
                            });
                            var fileIdsStr = fileIdsArry.join(',')
                        }else{
                            var fileIdsStr = key
                        }

                        var url = ctx + "rest/cloud/att/download?detailIds=" + fileIdsStr; //本地磁盘下载
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

            /* 移动文件

            *   type=0代表是单个移动，type=1代表多个移动  it
            *   item是要移动的数据
            *
            * */
            moveFiles:function(type,item){
                this.moveFilesDialogFlag = true;
                if(type == 0 && item){
                    vm.FilesMultipleSelection.push(item);
                }
            },
            chooseMoveDir:function(item){
                this.targetDirId = item.dirId;
            },
            sureMoveFile:function(){
                var vm= this;
                var FilesMultipleSelection = vm.FilesMultipleSelection;
                var detailIdsArry = [];
                if(FilesMultipleSelection.length == 0){
                    vm.$message('您还没选中文件！');
                    return;
                }
                FilesMultipleSelection.forEach(function (item, index) {
                    detailIdsArry.push(item.detailId);
                });
                debugger;
                var params = {
                    detailIds:JSON.stringify(detailIdsArry),              // 文件ID数组
                    currentDirId:vm.dirId.toString(),       // 当前目录ID
                    targetDirId:vm.targetDirId.toString(),  // 目标目录ID
                }
                console.log(params)
                request('', {
                    url: ctx + 'rest/cloud/moveFilesFromDir',
                    type: 'get',
                    data:params,
                }, function (res) {
                    if(res.success){
                        vm.$message.success("移动成功");
                        var item = {
                            dirId: vm.dirId,
                            dirName:vm.dirName,
                        }
                        vm.getFileList(item);
                    }else{
                        vm.$message.error("移动失败")
                    }
                },function () {
                    vm.$message.error('请求接口错误，请稍后重试！');
                });
            },

            // 删除云盘列表中电子文件
            // type = 0 为单个删除  type = 1 为批量删除
            delelteCloundFile:function(type,key,recordId){
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
                if(type == 1){
                    if (!key && FilesMultipleSelection.length === 0) {
                        vm.$message.info({
                            showClose: true,
                            title: '错误',
                            message: '你还没选中文件'
                        });
                        return;
                    } else if (!key && DirsMultipleSelection.length > 0) {
                        vm.$message.info('只支持批量文件删除，不支持文件夹删除');
                        return;
                    } else {
                        if(key){
                            vm.FilesMultipleSelection.push(key);
                        }
                        var fileIdsArry = [];
                        if(FilesMultipleSelection.length == 0){
                            vm.$message('您还没选中文件！');
                            return;
                        }
                        FilesMultipleSelection.forEach(function (item, index) {
                            fileIdsArry.push(item.detailId);
                        });
                        var fileIdsStr = fileIdsArry.join(',')
                        if(!recordId){
                            recordId  = FilesMultipleSelection[0].recordId
                        }
                        confirmMsg('', '确定要删除吗？', function() {
                            var loading = vm.$loading({
                                lock: true,
                                text: 'Loading',
                                spinner: 'el-icon-loading',
                                background: 'rgba(0, 0, 0, 0.7)'
                            });
                            request('', {
                                url: ctx + 'rest/file/delelteAttachmentOfCloud',
                                type: 'get',
                                data:{
                                    detailIds:fileIdsStr,
                                    recordId:recordId,
                                },
                            }, function (res) {
                                loading.close();
                                if(res.success){
                                    vm.$message.success('删除成功');
                                    var item = {
                                        dirId: vm.dirId,
                                        dirName:vm.dirName,
                                    }
                                    vm.getFileList(item)
                                }else{
                                    vm.$message.error(res.message);
                                }

                            },function () {
                                vm.$message.error('数据接口出错，请稍后重试！');
                                loading.close();
                            });
                        }, '', '确定', '取消')
                    }
                }
            },

            // 搜索
            searchFile:function(){
                this.restData();
                var item = {
                    dirId: this.dirId,
                    dirName:this.dirName,
                }
                this.getFileList(item);
            },

            hangleIcon: function (index) {
                if (index === 0) {
                    return "icon-1"
                } else if(index === 1){
                    return "icon-2"
                }else if(index === 2){
                    return "icon-3"
                }else if(index === 3){
                    return "icon-4"
                }else if(index === 4){
                    return "icon-5"
                }else if(index === 5){
                    return "icon-6"
                }
            },
            // 获取文件类型图标
            getFileTypeIcon:function(fileType){
                var fileType = "." + fileType;
                if (/\.(jpg|jpeg)$/.test(fileType)){
                    return 'ag-filetype-jpg';
                }else if(/\.(gif|JPG)$/.test(fileType)){
                    return 'ag-filetype-gif';
                }else if(/\.(png|PNG)$/.test(fileType)){
                    return 'ag-filetype-png';
                }else if(/\.(doc|DOC|docx|DOCX)$/.test(fileType)){
                    return 'ag-filetype-doc';
                }else if(/\.(pdf|PDF)$/.test(fileType)){
                    return 'ag-filetype-pdf';
                }else if(/\.(excl|EXCL|xlsx)$/.test(fileType)){
                    return 'ag-filetype-xsl';
                }else if(/\.(svg|SVG)$/.test(fileType)){
                    return 'ag-filetype-svg';
                }else if(/\.(ppt|PPT|pptx|PPTX)$/.test(fileType)){
                    return 'ag-filetype-ppt';
                }else if(/\.(rar|RAR)$/.test(fileType)){
                    return 'ag-filetype-rar';
                }else if(/\.(7z|7Z)$/.test(fileType)){
                    return 'ag-filetype-7z';
                }else if(/\.(zip|ZIP)$/.test(fileType)){
                    return 'ag-filetype-zip';
                }else if(/\.(html|HTML)$/.test(fileType)){
                    return 'ag-filetype-html';
                }else if(/\.(txt|TXT)$/.test(fileType)){
                    return 'ag-filetype-txt';
                }else if(/\.(dir|DIR)$/.test(fileType)){
                    return 'ag-folder-fill';
                }else if(/\.(mov|MOV)$/.test(fileType)){
                    return 'ag-folder-fill';
                }else if(/\.(rp|RP)$/.test(fileType)){
                    return 'ag-filetype-rp';
                }else if(/\.(exe|EXE)$/.test(fileType)){
                    return 'ag-filetype-exe';
                }else{
                    return 'ag-filetype-wps';
                }
            },

            handleSelectionChange:function(){
                this.FilesMultipleSelection = this.$refs.fileListTable.selection;
                this.chooseFileNum = "已选中" + this.FilesMultipleSelection.length + "个文件";
            },

            // 点击table的行，实现选中
            tableRowClick:function(row){
                this.$refs.fileListTable.toggleRowSelection(row);
            },

            viewModelChoose:function(item){
                if(item.selectFlag){
                    item.selectFlag = ! item.selectFlag;
                    var index = this.FilesMultipleSelection.indexOf(item);
                    this.FilesMultipleSelection.splice(index,1);
                }else{
                    item.selectFlag = true;
                    this.FilesMultipleSelection.push(item);
                }
                if(this.FilesMultipleSelection.length == this.total || this.FilesMultipleSelection.length > 9){
                    this.isViewModelCheckedAll = true;
                }else{
                    this.isViewModelCheckedAll = false;
                }
            },

            // 视图模式下，点击check全选
            viewModelCheckedChange:function(val){
                var ts = this;
                if(val){
                    ts.FilesMultipleSelection = [];
                    this.fileListData.forEach(function (item,index) {
                            item.selectFlag = true;
                            ts.FilesMultipleSelection.push(item);
                    })
                }else{
                    this.fileListData.forEach(function (item,index) {
                        if(item.selectFlag){
                            item.selectFlag = false;
                        }
                        ts.FilesMultipleSelection = [];
                    })
                }
            },

            // 列表分页
            handleSizeChange: function(val){
                this.pageSize = val;
                var item = {
                    dirId: this.dirId,
                    dirName:this.dirName,
                }
                this.getFileList(item)
            },
            handleCurrentChange: function(val){
                this.pageNum = val;
                var item = {
                    dirId: this.dirId,
                    dirName:this.dirName,
                }
                this.getFileList(item)
            },
            hideClickOpt: function() {
                $('.right-click-opt').hide()
            },
            // 重置数据（新增文件夹的对象）
            clearFormData:function(){
                var _this = this;
                _this.addDirFormData = {}
                setTimeout(function(){
                    _this.$refs['addDirForm'].clearValidate();
                },500)

            },
            // 重置数据（页面的分页）
            restData:function () {
                this.pageNum = 1;
                this.pageSize = 10;
            }

        },
        filters: {
            formatDate: function(time) {
                if(!time) return "";
                var date = new Date(time);
                return formatDate(date, 'yyyy-MM-dd');
            },
            getFileSize:function(fileByte) {
                var fileSizeByte = fileByte;
                var fileSizeMsg = "";
                if (fileSizeByte < 1048576) fileSizeMsg = (fileSizeByte / 1024).toFixed(1) + "KB";
                else if (fileSizeByte == 1048576) fileSizeMsg = "1MB";
                else if (fileSizeByte > 1048576 && fileSizeByte < 1073741824) fileSizeMsg = (fileSizeByte / (1024 * 1024)).toFixed(1) + "MB";
                else if (fileSizeByte > 1048576 && fileSizeByte == 1073741824) fileSizeMsg = "1GB";
                else if (fileSizeByte > 1073741824 && fileSizeByte < 1099511627776) fileSizeMsg = (fileSizeByte / (1024 * 1024 * 1024)).toFixed(1) + "GB";
                else fileSizeMsg = "文件超过1TB";
                return fileSizeMsg;
            },
        },
        watch:{
            mode:function (val) {
                var ts = this;
                this.FilesMultipleSelection = [];
                this.chooseFileNum = "已选中" + this.FilesMultipleSelection.length + "个文件";
                this.isViewModelCheckedAll = false;
                this.$refs.fileListTable.clearSelection();
                this.fileListData.forEach(function (item,index) {
                    if(item.selectFlag){
                        item.selectFlag = false;
                    }
                })
            },
            keyword:function () {
                this.searchFile();
            }
        }
    })
    function onprogress(evt) {
        console.log(33333333333333333)
        var vm = myCloundSpaces;
        var loaded = evt.loaded; //已经上传大小情况
        console.log('上传的大小',loaded);
        var tot = evt.total; //附件总大小
        var per = Math.floor(100 * loaded / tot); //已经上传的百分比

        if(loaded > 1024 * 1024) {
            var uploadedSize = (Math.round(loaded / (1024 * 1024))).toString() + 'MB';
            vm.uploadFile[j].uploadedSize = uploadedSize;
        } else {
            var uploadedSize = (Math.round(loaded / 1024)).toString() + 'KB';
            vm.uploadFile[j].uploadedSize = uploadedSize;
        }
        vm.uploadFile[j].uploadedPer = per;
    };
})();