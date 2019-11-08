<%@ page contentType="text/html;charset=UTF-8" %>

<div id="theme_ver_opera_modal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="add_theme_opera_modal_title" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header" style="padding: 15px;height: 45px;">
                <h5 class="modal-title" id="theme_ver_opera_modal_title">帮助指引</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" style="padding: 10px 30px;">
                <%--<p style="font-size: 15px;">1、【复制版本】：点击此按钮将复制最新试运行或已发布情形版本下数据，用户需要对上一个已发布版本进行编辑操作时，可以点击此按钮;</p>--%>
                <p style="font-size: 15px;">1、【复制】：点击此按钮复制当前版本下的数据并产生最新的未启用版本，用户需要需要使用当前版本数据，可以点击此按钮；</p>
                <p style="font-size: 15px;">2、【试运行】：点击此按钮将更改当前版本为试运行状态，此时版本下数据可以编辑；</p>
                <p style="font-size: 15px;">3、【发布】：点击此按钮将更改当前版本为发布状态，此时版本下数据将不可编辑；</p>
                <%--<p style="font-size: 15px;">4、【查看】：点击此按钮用户可以查看当前情形版本下的相关数据；</p>--%>
                <p style="font-size: 15px;">4、【版本状态】：未发布 --> ( 试运行/发布 ) --> 已过时。</p>
            </div>
            <div class="modal-footer" style="padding: 10px;height: 60px;">
                <button type="button" class="btn btn-secondary" onclick="$('#theme_ver_opera_modal').modal('hide');">关闭</button>
            </div>
        </div>
    </div>
</div>