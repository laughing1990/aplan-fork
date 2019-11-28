<%@ page contentType="text/html;charset=UTF-8" %>

<!-- 设置图标信息 -->
<div id="common_set_img_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="common_set_img_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document" style="max-width: 950px;">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="common_set_img_modal_title"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px;">
                <div id="common_set_img_scroll" style="height: 380px;overflow-x: auto;overflow-y: auto;">
                    <input id="relBusiId" type="hidden" name="relBusiId" value=""/> <!-- 业务表id -->
                    <input id="selectedImg" type="hidden" name="selectedImg" value=""/> <!-- 选择的图标 -->
                    <input id="imgSizeType" type="hidden" name="imgSizeType" value=""/>  <!-- 选择的图标类型 -->
                    <div id="imgDataList"></div><!-- 图标数据集合 -->
                </div>
            </div>
            <div class="modal-footer" style="padding: 10px;">
                <%--<button type="button" class="btn btn-info">保存</button>--%>
                <button type="button" class="btn btn-secondary" onclick="$('#common_set_img_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">

    var needSelectImgDivId = null;
    /**
     * 设置图标点击函数
     * @param cId 需要赋值容器id
     * @param type 图标大小
     */
    function selectImgType(bizType,cId,type){

        $('#common_set_img_modal').modal('show');
        $('#common_set_img_modal_title').html('设置图标&nbsp;&nbsp;(&nbsp;<font color="red">*</font>双击选择&nbsp;)');

        // 图表展示集合
        var imgList = $("#imgDataList");

        //获取图标的大小
        var imgSize = getImgSizeByType(type);

        // 清空
        var bizId = null;
        needSelectImgDivId = null;
        $('#relBusiId').val("");
        $("#imgSizeType").val("");
        $('#selectedImg').val("");
        imgList.html("");

        // 重新赋值
        needSelectImgDivId = cId;
        if(bizType=='theme'){
            bizId = $('#add_theme_form input[name="themeId"]').val();
        }else if(bizType=='stage'){
            bizId = $('#add_stage_form input[name="stageId"]').val();
        }
        $("#imgSizeType").val(type);
        $("#relBusiId").val(bizId);

        // 加载图标数据
        loadImgData(imgSize,type,bizId,bizType,imgList);
    }

    /**
     * 获取图片列表
     * @param imgSize 图标大小
     * @param type 图标大小类型
     * @param bizId 业务表数据id
     * @param bizType 业务表 theme 主题  stage 阶段
     * @param imgList 图标数据集合
     */
    function loadImgData(imgSize,type,bizId,bizType,imgList){

        $.ajax({
            url: ctx+"/aea/par/theme/setThemeStageImg.do",
            type: "POST",
            dataType: "JSON",
            data: {"imgSize" : imgSize, "type" : type, "bizId" : bizId, "bizType": bizType},
            success: function(result){
                if(result){
                    var fileNameList = result.fileNameList;
                    var linkImg = result.linkImg;
                    if(fileNameList!=null&&fileNameList.length>0){
                        for(var i=0;i<fileNameList.length;i++){
                            var imgObj = fileNameList[i];
                            var fileName = imgObj.fileName;
                            var fullName = imgObj.fullName;
                            var src = ctx + fullName;
                            var divClass = "mouseOutImage";
                            if(linkImg == fullName){
                                divClass = "selectedImage  mouseOutImage";
                            }
                            var addHtml = "<div class=\""+divClass+"\"  id=\""+fileName+"\" " +
                                             "onmouseover= \"mouseImgOver(this.id)\" " +
                                             "onmouseout= \"mouseImgOut(this.id)\"  " +
                                             "onclick=\"selectImg(this.id)\"  " +
                                             "ondblclick=\"finishSelectImg(this.id)\">" +
                                             "<img src=\""+ src +"\">" +
                                          "</div>";

                            imgList.html(imgList.html()+addHtml);
                        }
                    }else{
                        imgList.html("<h5 style='text-alogn: center;'>暂无图片...</h5>");
                    }
                }
            }
        });
    }

    /**
     * 通过图标类型获得该类型的图标大小
     * @param type
     * @returns {Number}
     */
    function getImgSizeByType(type){

        var imgSize = 16;
        if(type == "small"){
            imgSize = 16;
        }else if(type == "middle"){
            imgSize = 24;
        }else if(type == "big"){
            imgSize = 32;
        }else if(type=='huge'){
            imgSize = 64;
        }
        return imgSize;
    }

    /**
     * 设置鼠标移动到图标上时，图标DIV的样式
     * @param id
     */
    function mouseImgOver(id){

        $("#"+id).removeClass("mouseOutImage");
        $("#"+id).addClass("mouseOverImage");
    }

    /**
     * 设置鼠标离开图标时，图标DIV的样式
     * @param id
     */
    function mouseImgOut(id){

        $("#"+id).removeClass("mouseOverImage");
        $("#"+id).addClass("mouseOutImage");
    }

    /**
     * 单击选择图标的操作
     * @param id
     */
    function selectImg(id){

        //移除之前的选中状态
        $("[onclick]").removeClass("selectedImage");
        $("#"+id).addClass("selectedImage");

        //获取当前id的DIV中的图片的src值
        var src = $("#"+id).children("img").attr("src");
        var newSrc = getNewSrc(src);
        //将图片路径保存到变量中
        $("#selectedImg").val(newSrc);
    }

    /**
     * 获取菜单路径(除去工程名)
     * @param src
     * @returns
     */
    function getNewSrc(src){

        var newsrc = src.substring(ctx.length,src.length);
        return newsrc;
    }

    /**
     * 双击选择图标的操作
     * @param id
     */
    function finishSelectImg(id){

        //获取当前id的DIV中的图片的src值
        var src = $("#"+id).children("img").attr("src");
        var newSrc = getNewSrc(src);
        //将图片路径保存到变量中
        $("#selectedImg").val(newSrc);
        setImgPath(needSelectImgDivId, newSrc);
    }

    /**
     * 赋值图标路径数据
     * @param divId  赋值容器id
     * @param path  图标相对路径
     */
    function setImgPath(divId, path){

        $("#"+divId).val(path);
        $('#common_set_img_modal').modal('hide');
    }
</script>