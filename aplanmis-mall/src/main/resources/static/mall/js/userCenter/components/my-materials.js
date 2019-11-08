var myMaterials = (function () {
    var vm = new Vue({
        el:"#my-materials",
        data:{
            mloading:false,
            tableLoading:false,
            filePreviewCount:0,
            keyword:'',
            pageNum:1,
            pageSize:10,
            total:0,
            matFileList:[],
        },
        created:function(){
            var ts = this;
            ts.getMatList();
        },
        methods:{
            // 获取我的材料库列表
            getMatList:function(){
                var ts = this;
                ts.tableLoading = true;
                request('', {
                    url: ctx + 'rest/user/mat/list',
                    type: 'get',
                    data:{
                        pageNum:ts.pageNum,
                        pageSize:ts.pageSize,
                        keyword:ts.keyword,
                    }
                }, function (res) {
                    ts.tableLoading = false;
                    if (res) {
                        ts.matFileList = res.list;
                        ts.total = res.total;
                    }
                }, function () {
                    ts.tableLoading = false;
                });
            },

            // 列表分页
            handleSizeChange: function(val){
                this.pageSize = val;
                this.getMatList(this.num);
            },
            handleCurrentChange: function(val){
                this.pageNum = val;
                this.getMatList(this.num);
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

            // 下载电子件
            downloadFile: function(detailIds){
                window.open(ctx+'rest/file/applydetail/mat/download/'+detailIds)
            },

            // 预览电子件
            previewFile: function(data,flag){ // flag==pdf 查看施工图
                var detailId = data.fileId;
                var _that = this;
                var regText = /doc|docx|ppt|pptx|xls|xlsx|txt$/;
                var fileName=data.fileName;
                var fileType = data.fileType;
                var flashAttributes = '';
                _that.filePreviewCount++
                if(flag=='pdf'){
                    var tempwindow=window.open(); // 先打开页面
                    setTimeout(function(){
                        tempwindow.location=ctx+'cod/drawing/drawingCheck?detailId='+detailId;
                    },1000)
                }else {
                    if(fileType=='pdf'){
                        var tempwindow=window.open(); // 先打开页面
                        setTimeout(function(){
                            tempwindow.location=ctx+'previewPdf/view?detailId='+detailId;
                        },1000)
                    }else if(regText.test(fileType)){
                        // previewPdf/pdfIsCoverted
                        _that.mloading = true;
                        request('', {
                            url: ctx + 'previewPdf/pdfIsCoverted?detailId='+detailId,
                            type: 'get',
                        }, function (result) {
                            if(result.success){
                                _that.mloading = false;
                                var tempwindow=window.open(); // 先打开页面
                                setTimeout(function(){
                                    tempwindow.location=ctx+'previewPdf/view?detailId='+detailId;
                                },1000)
                            }else {
                                if(_that.filePreviewCount>9){
                                    confirmMsg('提示信息：', '文件预览请求中，是否继续等待？', function () {
                                        _that.filePreviewCount=0;
                                        _that.previewFile(data);
                                    }, function () {
                                        _that.filePreviewCount=0;
                                        _that.mloading = false;
                                        return false;
                                    }, '确定', '取消', 'warning', true)
                                }else {
                                    setTimeout(function(){
                                        _that.previewFile(data);
                                    },1000)
                                }
                            }
                        }, function (msg) {
                            _that.mloading = false;
                            _that.$message({
                                message: '文件预览失败',
                                type: 'error'
                            });
                        })
                    }else {
                        _that.mloading = false;
                        var tempwindow=window.open(); // 先打开页面
                        setTimeout(function(){
                            tempwindow.location = ctx + 'rest/file/att/preview?detailId=' + detailId + '&flashAttributes=' + flashAttributes;
                        },1000)
                    }
                }
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
            keyword:function () {
                this.restData();
                this.getMatList();
            }
        }
    });

    return vm = vm;
})();