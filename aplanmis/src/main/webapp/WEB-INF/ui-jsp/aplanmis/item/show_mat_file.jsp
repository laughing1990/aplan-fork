<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<head>
    <title>图片预览</title>
	<!-- 所有JSP必须引入的公共JSP子页面 -->
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-meta.jsp"%>
	<%@ include file="/ui-static/agcloud/framework/jsp/agcloud-common.jsp"%>

    <%@ include file="/ui-static/agcloud/framework/jsp/lib-jquery-viewer1.jsp"%>
    <script>
        window.addEventListener('DOMContentLoaded', function () {
            var galley = document.getElementById('galley');
            galley.addEventListener('hidden', function () {
                // 重置window.opener用来获取打开当前窗口的窗口引用
                // 这里置为null,避免IE下弹出关闭页面确认框
                window.opener = null;
                // JS重写当前页面
                window.open("", "_self", "");
                // 顺理成章的关闭当前被重写的窗口
                window.close();
            });
            var viewer = new Viewer(galley, {
                url: 'data-original',
                toolbar: {
                    oneToOne: {size: 'large'},
                    zoomIn: {size: 'large'},
                    zoomOut: {size: 'large'},
                    reset:{size: 'large'},
                    rotateLeft:{size: 'large'},
                    rotateRight:{size: 'large'},
                    flipHorizontal:{size: 'large'},
                    flipVertical:{size: 'large'},
                    download: {
                        size: 'large',
                        click:function() {
                            var a = document.createElement('a');
                            a.href = viewer.image.src;
                            a.download = viewer.image.alt;
                            document.body.appendChild(a);
                            a.click();
                            document.body.removeChild(a);
                        }
                    },
                }
            });
            viewer.show();
            var myVar = setInterval(function(){
                if($('.viewer-container .viewer-canvas img').length<=0){
                    viewer.destroy();
                    viewer = new Viewer(galley, {
                        url: 'data-original',
                        toolbar: {
                            oneToOne: {size: 'large'},
                            zoomIn: {size: 'large'},
                            zoomOut: {size: 'large'},
                            reset:{size: 'large'},
                            rotateLeft:{size: 'large'},
                            rotateRight:{size: 'large'},
                            flipHorizontal:{size: 'large'},
                            flipVertical:{size: 'large'},
                            download: {
                                size: 'large',
                                click:function() {
                                var a = document.createElement('a');
                                a.href = viewer.image.src;
                                a.download = viewer.image.alt;
                                document.body.appendChild(a);
                                a.click();
                                document.body.removeChild(a);
                                }
                            },
                        }
                    });
                    viewer.show();
                }else{
                    clearInterval(myVar);
                }
            }, 500);
        });
    </script>
</head>
<body>
    <c:choose>
        <c:when test="${isExistImg}">
            <div class="container">
                <div id="galley" style="display:none">
                    <ul class="pictures">
                        <li>
                            <img data-original="data:image/${imgData.fileType};base64,${imgData.base64Content}"
                                 src="data:image/${imgData.fileType};base64,${imgData.base64Content}" alt="${imgData.fileName}"/>
                        </li>
                    </ul>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <h1 style="margin: 15px;text-align: center;font-size: 16px;color: blue"><<<&nbsp;${msg}&nbsp;>>></h1>
        </c:otherwise>
    </c:choose>
</body>
</html>